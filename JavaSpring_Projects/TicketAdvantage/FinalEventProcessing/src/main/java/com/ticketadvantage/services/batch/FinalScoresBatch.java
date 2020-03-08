/**
 * 
 */
package com.ticketadvantage.services.batch;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.TimeZone;
import com.ticketadvantage.services.dao.AccountEventDAO;
import com.ticketadvantage.services.dao.AccountEventDAOImpl;
import com.ticketadvantage.services.dao.AccountEventFinalDAO;
import com.ticketadvantage.services.dao.AccountEventFinalDAOImpl;
import com.ticketadvantage.services.dao.EventsDAO;
import com.ticketadvantage.services.dao.EventsDAOImpl;
import com.ticketadvantage.services.dao.RecordEventDAO;
import com.ticketadvantage.services.dao.RecordEventDAOImpl;
import com.ticketadvantage.services.dao.sites.sportsinsights.SportsInsightsSite;
import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.AccountEvent;
import com.ticketadvantage.services.model.AccountEventFinal;
import com.ticketadvantage.services.model.ClosingLine;
import com.ticketadvantage.services.model.EventPackage;
import com.ticketadvantage.services.telegram.TelegramBotSender;

/**
 * @author calderson
 *
 */
@WebListener
@Service
public class FinalScoresBatch implements Runnable, ServletContextListener {
	private static final Logger LOGGER = Logger.getLogger(FinalScoresBatch.class);
	private static final TimeZone TIMEZONE = TimeZone.getTimeZone("America/New_York");
	private final EntityManager entityManager = Persistence.createEntityManagerFactory("entityManager").createEntityManager();
	private volatile boolean shutdown = false;
	private EventsDAO eventDAO;
	private AccountEventDAO accountEventDAO;
	private AccountEventFinalDAO accountEventFinalDAO;
	private RecordEventDAO recordEventDAO;
	private List<EventPackage> events;
	private SportsInsightsSite processSite;

	/**
	 * 
	 */
	public FinalScoresBatch() {
		super();
		LOGGER.info("Entering FinalScoresBatch()");

		// Login to site
		processSite = new SportsInsightsSite("https://sportsinsights.actionnetwork.com",
				"mojaxsventures@gmail.com", 
				"Jaxson17");
		
		// Setup HTTP Client with proxy if there is one
		processSite.setProxy("None");

		LOGGER.info("Exiting FinalScoresBatch()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LOGGER.info("Entering main()");

		try {
			final FinalScoresBatch finalScoresBatch = new FinalScoresBatch();
			// finalScoresBatch.run();

			finalScoresBatch.eventDAO = new EventsDAOImpl();
			finalScoresBatch.accountEventDAO = new AccountEventDAOImpl();
			finalScoresBatch.recordEventDAO = new RecordEventDAOImpl();
			finalScoresBatch.eventDAO.setEntityManager(finalScoresBatch.entityManager);
			finalScoresBatch.accountEventDAO.setEntityManager(finalScoresBatch.entityManager);
			finalScoresBatch.recordEventDAO.setEntityManager(finalScoresBatch.entityManager);
			
			finalScoresBatch.entityManager.getTransaction().begin();
	
			// Login to site
			finalScoresBatch.processSite = new SportsInsightsSite("https://sportsinsights.actionnetwork.com/",
					"mojaxsventures@gmail.com", 
					"Jaxson17");
			
			// Setup HTTP Client with proxy if there is one
			finalScoresBatch.processSite.setProxy("None");
	
			// Authenticate first
			finalScoresBatch.processSite.loginToSite("mojaxsventures@gmail.com", "Jaxson17");
	
			finalScoresBatch.proccessFinalScores();
			finalScoresBatch.entityManager.getTransaction().commit();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		LOGGER.info("Exiting main()");
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
    @Override
    public void contextInitialized(ServletContextEvent event) {
    	LOGGER.error("Entering contextInitialized()");
		new Thread(this).start();
		LOGGER.error("Exiting contextInitialized()");
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextDestroyed(ServletContextEvent event) {
	    	LOGGER.error("Entering contextDestroyed()");
	    	this.shutdown = true;
	    	LOGGER.error("Exiting contextDestroyed()");
    }

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		LOGGER.info("Entering run()");

		eventDAO = new EventsDAOImpl(); 
		accountEventDAO = new AccountEventDAOImpl();
		accountEventFinalDAO = new AccountEventFinalDAOImpl();
		recordEventDAO = new RecordEventDAOImpl();
		eventDAO.setEntityManager(entityManager);
		accountEventDAO.setEntityManager(entityManager);
		accountEventFinalDAO.setEntityManager(entityManager);
		recordEventDAO.setEntityManager(entityManager);

		while (!shutdown) {
			try {
				entityManager.getTransaction().begin();
		
				// Authenticate first
				processSite.loginToSite("mojaxsventures@gmail.com", "Jaxson17");

				// Process the final scores
				proccessFinalScores();
				entityManager.getTransaction().commit();

				// Check every 15 minutes
				Thread.sleep(900000);
			} catch (Throwable t) {
				LOGGER.error(t.getMessage(), t);
				entityManager.getTransaction().commit();
			}
		}
	}

	/**
	 * 
	 */
	public void proccessFinalScores() {
		LOGGER.info("Entering proccessFinalScores()");

		try {
			// All events
			events = new ArrayList<EventPackage>(processSite.forceGetAllTodayOnEvents()); 

			// Process events
			doProcess();
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in proccessFinalScores()", t);
		}

		LOGGER.info("Exiting proccessEvents()");
	}

	/**
	 * 
	 * @throws BatchException
	 */
	private void doProcess() throws BatchException {
		LOGGER.info("Entering doProcess()");
		final List<AccountEvent> accountEvents = accountEventDAO.getOpenAccountEventsForWeek();
		LOGGER.debug("accountEvents: " + accountEvents);

		// Go through all account events to see if they are complete
		if (accountEvents != null && accountEvents.size() > 0) {
			LOGGER.debug("accountEvents.size(): " + accountEvents.size());

			for (AccountEvent accountEvent : accountEvents) {
				try {
					LOGGER.debug("accountEvent.getSport(): " + accountEvent.getSport());
					LOGGER.debug("accountEvent.getEventid(): " + accountEvent.getEventid());

					if (accountEvent.getEventid() != null && accountEvent.getEventid().intValue() != 0) {
						// Find the event (if we can) at SportsInsights
						EventPackage event = findGameEvent(accountEvent);
						if (accountEvent.getEventid().intValue() == 915 || accountEvent.getEventid().intValue() == 1915) {
							LOGGER.error("Event: " + event);
						}

						if (event != null) {
							try {
								// Get the score for this event								
								event = processSite.getScores(accountEvent.getSport(), event.getSportsInsightsEventId(), event);
								LOGGER.debug("Event: " + event);

								// Do we have a final?
								if (event != null && 
									(accountEvent.getSport().contains("lines") && event.getGameIsFinal()) || 
									(accountEvent.getSport().contains("first") && event.getFirstIsFinal()) || 
									(accountEvent.getSport().contains("second") && event.getSecondIsFinal()) || 
									(accountEvent.getSport().contains("third") && event.getThirdIsFinal()) || 
									(accountEvent.getSport().contains("live") && event.getLiveIsFinal())) {
									// Update event and create event final
									updateAndCreateEventFinal(accountEvent, event);
								} else {
									LOGGER.debug("id: " + accountEvent.getId() + " eventname: " + accountEvent.getEventname() + " eventdatetime: " + accountEvent.getEventdatetime());
									LOGGER.debug("game: " + event.getGameIsFinal() + " first: " + event.getFirstIsFinal() + " second: " + event.getSecondIsFinal());
								}
							} catch (Throwable t) {
								LOGGER.error(t.getMessage(), t);
							}
						} else {
							try {
								final String eventResult = accountEvent.getEventresult();
								if (eventResult == null || eventResult.length() == 0) {
									accountEvent.setEventresult("PUSH");
									setupPushEventResultAmount(accountEvent);
	
									TelegramBotSender.sendToTelegram("256820278", "FEP : " + accountEvent.getEventid() + " " + accountEvent.getEventname());
								} else {
									LOGGER.error("AccountEvent: " + accountEvent);
								}
							} catch (Throwable be) {
								LOGGER.warn(be.getMessage(), be);
							}
						}
					}
				} catch (Throwable t) {
					LOGGER.error(t.getMessage(), t);
				}
			}
		}

		LOGGER.info("Exiting doProcess()");
	}

	/**
	 * 
	 * @param accountEvent
	 * @return
	 */
	private EventPackage findGameEvent(AccountEvent accountEvent) {
		LOGGER.info("Entering findGameEvent()");
		LOGGER.debug("accountEvent.getSport(): " + accountEvent.getSport());
		LOGGER.debug("accountEvent.getEventid().intValue(): " + accountEvent.getEventid().intValue());
		EventPackage eventPackage = null;

		for (EventPackage ep : events) {
			Integer rotation1 = Integer.parseInt(ep.getTeamone().getEventid());
			Integer rotation2 = Integer.parseInt(ep.getTeamtwo().getEventid());
			final Date aeDate = accountEvent.getEventdatetime();
			final Date epDate = ep.getEventdatetime();

			final Calendar aeCalendar = Calendar.getInstance();
			aeCalendar.setTimeZone(FinalScoresBatch.TIMEZONE);
			aeCalendar.setTime(aeDate);

			final Calendar epCalendar = Calendar.getInstance();
			epCalendar.setTimeZone(FinalScoresBatch.TIMEZONE);
			epCalendar.setTime(epDate);

			int aeday = aeCalendar.get(Calendar.DAY_OF_MONTH);
			int epday = epCalendar.get(Calendar.DAY_OF_MONTH);
			int aemonth = aeCalendar.get(Calendar.MONTH);
			int epmonth = epCalendar.get(Calendar.MONTH);
			int aeyear = aeCalendar.get(Calendar.YEAR);
			int epyear = epCalendar.get(Calendar.YEAR);

			// Check for the correct date
			if (aemonth == epmonth && aeday == epday && aeyear == epyear) {
				if (accountEvent.getSport().contains("line")) {
					if (accountEvent.getEventid().intValue() == rotation1.intValue() ||
						accountEvent.getEventid().intValue() == rotation2.intValue()) {
						eventPackage = ep;
						break;
					}
				} else if (accountEvent.getSport().contains("first")) {
					rotation1 = rotation1 + 1000;
					rotation2 = rotation2 + 1000;
					if (accountEvent.getEventid().intValue() == rotation1.intValue() ||
						accountEvent.getEventid().intValue() == rotation2.intValue()) {
						eventPackage = ep;
						break;
					}					
				} else if (accountEvent.getSport().contains("second")) {
					rotation1 = rotation1 + 2000;
					rotation2 = rotation2 + 2000;
					if (accountEvent.getEventid().intValue() == rotation1.intValue() ||
						accountEvent.getEventid().intValue() == rotation2.intValue()) {
						eventPackage = ep;
						break;
					}									
				} else if (accountEvent.getSport().contains("third")) {
					rotation1 = rotation1 + 3000;
					rotation2 = rotation2 + 3000;
					if (accountEvent.getEventid().intValue() == rotation1.intValue() ||
						accountEvent.getEventid().intValue() == rotation2.intValue()) {
						eventPackage = ep;
						break;
					}					
				}
			}
		}

		LOGGER.info("Exiting findGameEvent()");
		return eventPackage;
	}

	/**
	 * 
	 * @param accountEvent
	 * @param event
	 * @throws BatchException
	 */
	private void updateAndCreateEventFinal(AccountEvent accountEvent, EventPackage event) throws BatchException {
		LOGGER.info("Entering updateAndCreateEventFinal()");

		updateAccountEventResult(accountEvent, event);
		final String outcome = accountEvent.getEventresult();

		createAccountEventFinal(accountEvent, event, outcome.equals("WIN"));

		LOGGER.info("Exiting updateAndCreateEventFinal()");
	}

	/**
	 * 
	 * @param accountEvent
	 * @param event
	 */
	private void updateAccountEventResult(AccountEvent accountEvent, EventPackage event) {
		LOGGER.info("Entering updateAccountEventResult()");
		LOGGER.debug("AccountEvent: " + accountEvent);
		LOGGER.debug("EventPackage: " + event);
		String result = "";
		int type = 0;

		// Spread?
		if (accountEvent.getSpreadid() != null && accountEvent.getSpreadid() != 0) {
			type = 1;
			if (accountEvent.getSport().contains("lines")) {
				result = determineSpreadLineResult(event.getTeamone().getGameScore(), event.getTeamtwo().getGameScore(), accountEvent, event);
			} else if (accountEvent.getSport().contains("first")) {
				result = determineSpreadLineResult(event.getTeamone().getFirstScore(), event.getTeamtwo().getFirstScore(), accountEvent, event);
			} else if (accountEvent.getSport().contains("second")) {
				result = determineSpreadLineResult(event.getTeamone().getSecondScore(), event.getTeamtwo().getSecondScore(), accountEvent, event);
			} else if (accountEvent.getSport().contains("third")) {
				result = determineSpreadLineResult(event.getTeamone().getThirdScore(), event.getTeamtwo().getThirdScore(), accountEvent, event);
			} else if (accountEvent.getSport().contains("live")) {
				result = determineSpreadLineResult(event.getTeamone().getLiveScore(), event.getTeamtwo().getLiveScore(), accountEvent, event);
			}
		// Total?
		} else if (accountEvent.getTotalid() != null && accountEvent.getTotalid() != 0) {
			type = 2;
			if (accountEvent.getSport().contains("lines")) {
				result = determineTotalResult(event.getTeamone().getGameScore(), event.getTeamtwo().getGameScore(), accountEvent, event);
			} else if (accountEvent.getSport().contains("first")) {
				result = determineTotalResult(event.getTeamone().getFirstScore(), event.getTeamtwo().getFirstScore(), accountEvent, event);
			} else if (accountEvent.getSport().contains("second")) {
				result = determineTotalResult(event.getTeamone().getSecondScore(), event.getTeamtwo().getSecondScore(), accountEvent, event);
			} else if (accountEvent.getSport().contains("third")) {
				result = determineTotalResult(event.getTeamone().getThirdScore(), event.getTeamtwo().getThirdScore(), accountEvent, event);
			} else if (accountEvent.getSport().contains("live")) {
				result = determineTotalResult(event.getTeamone().getLiveScore(), event.getTeamtwo().getLiveScore(), accountEvent, event);
			}
			// Money Line?
		} else if (accountEvent.getMlid() != null && accountEvent.getMlid().intValue() != 0) {
			type = 3;
			if (accountEvent.getSport().contains("lines")) {
				result = determineMlResult(event.getTeamone().getGameScore(), event.getTeamtwo().getGameScore(),
						accountEvent, event);
			} else if (accountEvent.getSport().contains("first")) {
				result = determineMlResult(event.getTeamone().getFirstScore(), event.getTeamtwo().getFirstScore(),
						accountEvent, event);
			} else if (accountEvent.getSport().contains("second")) {
				result = determineMlResult(event.getTeamone().getSecondScore(), event.getTeamtwo().getSecondScore(),
						accountEvent, event);
			} else if (accountEvent.getSport().contains("third")) {
				result = determineMlResult(event.getTeamone().getThirdScore(), event.getTeamtwo().getThirdScore(),
						accountEvent, event);
			} else if (accountEvent.getSport().contains("live")) {
				result = determineMlResult(event.getTeamone().getLiveScore(), event.getTeamtwo().getLiveScore(),
						accountEvent, event);
			}
		}
		LOGGER.debug("type: " + type);

		// Update the account event with the result
		accountEvent.setEventresult(result);

		// Is this a WIN?
		if (result.equals("WIN")) {
			final Float actualAmount = Float.parseFloat(accountEvent.getActualamount());

			if (type == 1) {
				// Spread
				final Float juice = accountEvent.getSpreadjuice();
				setupWinEventResultAmount(juice, actualAmount, accountEvent);
			} else if (type == 2) {
				// Total
				final Float juice = accountEvent.getTotaljuice();
				setupWinEventResultAmount(juice, actualAmount, accountEvent);
			} else if (type == 3) {
				// ML
				final Float juice = accountEvent.getMljuice();
				setupWinEventResultAmount(juice, actualAmount, accountEvent);
			} else {
				LOGGER.warn("unknown type");
			}
		} else if (result.equals("LOSS")) { // LOSS?
			final Float actualAmount = Float.parseFloat(accountEvent.getActualamount());

			if (type == 1) {
				// Spread
				final Float juice = accountEvent.getSpreadjuice();
				setupLossEventResultAmount(juice, actualAmount, accountEvent);
			} else if (type == 2) {
				// Total
				final Float juice = accountEvent.getTotaljuice();
				setupLossEventResultAmount(juice, actualAmount, accountEvent);
			} else if (type == 3) {
				// ML
				final Float juice = accountEvent.getMljuice();
				setupLossEventResultAmount(juice, actualAmount, accountEvent);
			} else {
				LOGGER.warn("unknown type");
			}
		} else if (result.equals("PUSH")) { // PUSH?
			setupPushEventResultAmount(accountEvent);
		}

		entityManager.persist(accountEvent);
		entityManager.flush();

		LOGGER.info("Exiting updateAccountEventResult()");
	}

	/**
	 * 
	 * @param juice
	 * @param actualAmount
	 * @param accountEvent
	 */
	private void setupWinEventResultAmount(Float juice, Float actualAmount, AccountEvent accountEvent) {
		LOGGER.info("Entering setupWinEventResultAmount()");
		LOGGER.debug("juice: " + juice);
		LOGGER.debug("actualAmount: " + actualAmount);
		final DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		String eventAmount = ""; 

		// Check for special moneyline case
		if (accountEvent.getType().equals("ml")) {
			eventAmount = df.format(actualAmount);
		} else {
			if (juice > 0) {
				eventAmount = df.format(actualAmount * (juice/100));
			} else {
				eventAmount = df.format(actualAmount);
			}
		}

		// Get rid of any ,
		eventAmount = eventAmount.replace(",", "");
		
		// Now set it up as a Float
		accountEvent.setEventresultamount(Float.parseFloat(eventAmount));

		LOGGER.info("Exiting setupWinEventResultAmount()");
	}

	/**
	 * 
	 * @param juice
	 * @param actualAmount
	 * @param accountEvent
	 */
	private void setupLossEventResultAmount(Float juice, Float actualAmount, AccountEvent accountEvent) {
		LOGGER.info("Entering setupLossEventResultAmount()");
		LOGGER.debug("juice: " + juice);
		LOGGER.debug("actualAmount: " + actualAmount);
		final DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		String eventAmount = ""; 

		if (juice > 0) {
			eventAmount = df.format(actualAmount * -1);
			eventAmount = eventAmount.replace(",", "");
		} else {
			eventAmount = df.format(actualAmount * (juice/100));
			eventAmount = eventAmount.replace(",", "");
		}

		// Now set it up as a Float
		accountEvent.setEventresultamount(Float.parseFloat(eventAmount));

		LOGGER.info("Exiting setupLossEventResultAmount()");
	}

	/**
	 * 
	 * @param accountEvent
	 */
	private void setupPushEventResultAmount(AccountEvent accountEvent) {
		LOGGER.info("Entering setupPushEventResultAmount()");

		final DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		final String eventAmount = df.format(new Double(0.00));

		// Now set it up as a Float
		accountEvent.setEventresultamount(Float.parseFloat(eventAmount));

		LOGGER.info("Exiting setupPushEventResultAmount()");
	}

	/**
	 * 
	 * @param accountEvent
	 * @param event
	 * @param outcomeWin
	 */
	private void createAccountEventFinal(AccountEvent accountEvent, EventPackage event, Boolean outcomeWin) throws BatchException {
		LOGGER.info("Entering updateAccountEventFinal()");
		LOGGER.debug("AccountEvent: " + accountEvent);
		LOGGER.debug("EventPackage: " + event);
		LOGGER.debug("outcomeWin: " + outcomeWin);

		// Setup account event finals
		AccountEventFinal accountEventFinal = new AccountEventFinal();
		accountEventFinal.setAccounteventid(accountEvent.getId());

		// --- lineType = 1 = Spread
		// --- lineType = 2 = ML
		// --- lineType = 3 = Total
		final Integer gameTypeId = getGameTypeId(accountEvent);
		final Integer sportId = getSportId(accountEvent);
		final Integer teamBet = teamBetOn(accountEvent, event);
		if (teamBet == null) {
			String aeString = accountEvent.toString();

			if (aeString.length() > 4000) {
				aeString = aeString.substring(0, 4000);
			}
			throw new BatchException(BatchErrorCodes.TEAM_BET_ON_EXCEPTION, BatchErrorMessage.TEAM_BET_ON_EXCEPTION, aeString);
		}

		// Setup the closing line
		final ClosingLine spreadClosingLine	= processSite.getClosingLine(event.getSportsInsightsEventId(), 1, gameTypeId, sportId);
		final ClosingLine mlClosingLine 	= processSite.getClosingLine(event.getSportsInsightsEventId(), 2, gameTypeId, sportId);
		final ClosingLine totalClosingLine 	= processSite.getClosingLine(event.getSportsInsightsEventId(), 3, gameTypeId, sportId);
		
		LOGGER.debug("spreadClosingLine: " + spreadClosingLine);
		LOGGER.debug("mlClosingLine: " + mlClosingLine);
		LOGGER.debug("totalClosingLine: " + totalClosingLine);

		accountEventFinal.setRotation1(event.getTeamone().getId().toString());
		accountEventFinal.setRotation2(event.getTeamtwo().getId().toString());
		accountEventFinal.setRotation1team(event.getTeamone().getTeam());
		accountEventFinal.setRotation2team(event.getTeamtwo().getTeam());

		// Check what type of game it is
		if (accountEvent.getSport().contains("lines")) {
			if (event.getTeamone().getGameScore() != null && event.getTeamtwo().getGameScore() != null) {	
				accountEventFinal.setRotation1score(event.getTeamone().getGameScore().toString());
				accountEventFinal.setRotation2score(event.getTeamtwo().getGameScore().toString());
			}
		} else if (accountEvent.getSport().contains("first")) {
			if (event.getTeamone().getFirstScore() != null && event.getTeamtwo().getFirstScore() != null) {
				accountEventFinal.setRotation1score(event.getTeamone().getFirstScore().toString());
				accountEventFinal.setRotation2score(event.getTeamtwo().getFirstScore().toString());
			}
		} else if (accountEvent.getSport().contains("second")) {
			if (event.getTeamone().getSecondScore() != null && event.getTeamtwo().getSecondScore() != null) {
				accountEventFinal.setRotation1score(event.getTeamone().getSecondScore().toString());
				accountEventFinal.setRotation2score(event.getTeamtwo().getSecondScore().toString());
			}
		} else if (accountEvent.getSport().contains("third")) {
			if (event.getTeamone().getThirdScore() != null && event.getTeamtwo().getThirdScore() != null) {
				accountEventFinal.setRotation1score(event.getTeamone().getThirdScore().toString());
				accountEventFinal.setRotation2score(event.getTeamtwo().getThirdScore().toString());
			}
		} else if (accountEvent.getSport().contains("live")) {
			if (event.getTeamone().getLiveScore() != null && event.getTeamtwo().getLiveScore() != null) {
				accountEventFinal.setRotation1score(event.getTeamone().getLiveScore().toString());
				accountEventFinal.setRotation2score(event.getTeamtwo().getLiveScore().toString());
			}
		}

		// Setup outcome win
		accountEventFinal.setOutcomewin(outcomeWin);
		
		// --- If user bet on Team 1 he/she bet on the Visiting team
		// --- If user bet on Team 2 he/she bet on the Home team
		Boolean homeTeamFavored = null;
		if (spreadClosingLine != null) {
			if (spreadClosingLine.getHomeTeamFavored() != null) {
				homeTeamFavored = spreadClosingLine.getHomeTeamFavored();
			}
		
			// --- Figure out the spread indicator (either "-" or "+")
			// --- Favored team is always the "-"
			String spreadIndicator 		= "";
			String spreadClosingLineStr = spreadClosingLine.getLine().toString();
			String spreadJuiceStr 		= "";
			String spreadJuiceIndicator = "";
	
			// --- See if the user bet on the favored team and adjust the spreadIndicator if necessary
			if (teamBet == 1 && !homeTeamFavored) {
				// --- User bet on Visiting team and Visiting team was favored
				spreadIndicator = "-";
				String money = spreadClosingLine.getMoney1().toString();
				if (money.startsWith("-")) {
					spreadJuiceIndicator = "-";
				} else {
					spreadJuiceIndicator = "+";
				}
				spreadJuiceStr = spreadClosingLine.getMoney1().toString(); // --- User bet on visiting team which is stored in Money1
			} else if (teamBet == 2 && homeTeamFavored) {
				spreadIndicator = "-";
				// --- User bet on Home team and Home team was favored
				String money = spreadClosingLine.getMoney2().toString();
				if (money.startsWith("-")) {
					spreadJuiceIndicator = "-";
				} else {
					spreadJuiceIndicator = "+";
				}
				spreadJuiceStr = spreadClosingLine.getMoney2().toString(); // --- User bet on home team which is stored in Money2
			} else if (teamBet == 1 && homeTeamFavored) {
				spreadIndicator = "+";
	
				// --- User bet on Visiting team and Visiting team was NOT favored
				String money = spreadClosingLine.getMoney1().toString();
				if (money.startsWith("-")) {
					spreadJuiceIndicator = "-";
				} else {
					spreadJuiceIndicator = "+";
				}
				spreadJuiceStr = spreadClosingLine.getMoney1().toString(); // --- User bet on visiting team which is stored in Money1
			} else {
				spreadIndicator = "+";
				// --- User bet on Home team and Home team was NOT favored
				String money = spreadClosingLine.getMoney2().toString();
				if (money.startsWith("-")) {
					spreadJuiceIndicator = "-";
				} else {
					spreadJuiceIndicator = "+";
				}
				spreadJuiceStr = spreadClosingLine.getMoney2().toString(); // --- User bet on home team which is stored in Money2
			}
			LOGGER.debug("spreadClosingLineStr: " + spreadClosingLineStr);
			accountEventFinal.setSpreadindicator(spreadIndicator);
			
			if (spreadClosingLineStr != null && spreadClosingLineStr.length() > 0) {
				accountEventFinal.setSpreadnumber(Float.parseFloat(spreadClosingLineStr));
			}
			accountEventFinal.setSpreadjuiceindicator(spreadJuiceIndicator);
			
			if (spreadJuiceStr != null && spreadJuiceStr.length() > 0) {
				accountEventFinal.setSpreadjuicenumber(Float.parseFloat(spreadJuiceStr));
			}
		}

		if (totalClosingLine != null) {
			// --- Figure out the total indicator (either "o" or "u")
			String totalJuiceStr = "";
			String totalJuiceIndicator = "";
			String totalClosingLineStr = totalClosingLine.getLine().toString();
	
			LOGGER.debug("totalClosingLineStr: " + totalClosingLineStr);
			accountEventFinal.setTotalindicator(accountEvent.getTotalindicator());
			
			if (totalClosingLineStr != null && totalClosingLineStr.length() > 0) {
				accountEventFinal.setTotalnumber(Float.parseFloat(totalClosingLineStr));
			}
	
			// --- See if the user bet on the favored team and adjust the spreadIndicator if necessary
			if (teamBet == 1) {
				// --- User bet on Visiting team
				totalJuiceStr = totalClosingLine.getMoney1().toString(); // --- User bet on visiting team which is stored in Money1
				String money = totalClosingLine.getMoney1().toString();
				if (money.startsWith("-")) {
					totalJuiceIndicator = "-";
				} else {
					totalJuiceIndicator = "+";
				}
			} else {
				totalJuiceStr = totalClosingLine.getMoney2().toString(); // --- User bet on home team which is stored in Money2
				String money = totalClosingLine.getMoney2().toString();
				if (money.startsWith("-")) {
					totalJuiceIndicator = "-";
				} else {
					totalJuiceIndicator = "+";
				}
			}
			
			// Setup total juice
			accountEventFinal.setTotaljuiceindicator(totalJuiceIndicator);
			if (totalJuiceStr != null && totalJuiceStr.length() > 0) {
				accountEventFinal.setTotaljuicenumber(Float.parseFloat(totalJuiceStr));
			}
		}

		if (mlClosingLine != null) {
			String mlJuiceIndicator = "";
			String mlJuiceStr = "";
			if (teamBet == 1) {
				if (mlClosingLine != null && mlClosingLine.getMoney1() != null) {
					// --- User bet on Visiting team
					mlJuiceStr = mlClosingLine.getMoney1().toString(); // --- User bet on visiting team which is stored in Money1
					String money = mlClosingLine.getMoney1().toString();
					if (money.startsWith("-")) {
						mlJuiceIndicator = "-";
					} else {
						mlJuiceIndicator = "+";
					}
				}
			} else {
				if (mlClosingLine != null && mlClosingLine.getMoney2() != null) {
					mlJuiceStr = mlClosingLine.getMoney2().toString(); // --- User bet on home team which is stored in Money2
					String money = mlClosingLine.getMoney2().toString();
					if (money.startsWith("-")) {
						mlJuiceIndicator = "-";
					} else {
						mlJuiceIndicator = "+";
					}
				}
			}
	
			accountEventFinal.setMlindicator(mlJuiceIndicator);
			if (mlJuiceStr != null && mlJuiceStr.length() > 0) {
				accountEventFinal.setMlnumber(Float.parseFloat(mlJuiceStr));
			}
		}

		this.accountEventFinalDAO.persist(accountEventFinal);
		entityManager.persist(accountEventFinal);
		entityManager.flush();
		
		LOGGER.info("Exiting updateAccountEventFinal()");
	}

	/**
	 * 
	 * @param team1Score
	 * @param team2Score
	 * @param accountEvent
	 * @param event
	 * @return
	 */
	private String determineSpreadLineResult(Integer team1Score, Integer team2Score, AccountEvent accountEvent, EventPackage event) {
		LOGGER.info("Entering determineSpreadLineResult()");
		LOGGER.debug("team1Score: " + team1Score);
		LOGGER.debug("team2Score: " + team2Score);
		LOGGER.debug("AccountEvent: " + accountEvent);
		LOGGER.debug("EventPackage: " + event);
		String result = "";
		Integer winningTeam = new Integer(0);
		Boolean gameTied = false;
		Integer actualSpread = 0;
		Float spreadBet = null;
		String spreadIndicator = null;	
		Integer teamBet = null;
		
		try {
			// --- Check to see if the game ended in a tie
			if (team1Score == team2Score) {
				gameTied = true;
			}
			
			// --- As long as the game didn't end in a tie, figure out which team won and then figure out the actual spread of the game
			if (!gameTied) {
				if (team1Score > team2Score) {
					winningTeam = 1;
				} else {
					winningTeam = 2;
				}
				
				if (winningTeam == 1) {
					LOGGER.debug("team1Score: " + team1Score);
					LOGGER.debug("team2Score: " + team2Score);
					actualSpread = (team1Score - team2Score);
					LOGGER.debug("actualSpread: " + actualSpread);
				} else {
					LOGGER.debug("team1Score: " + team1Score);
					LOGGER.debug("team2Score: " + team2Score);
					actualSpread = (team2Score - team1Score);
					LOGGER.debug("actualSpread: " + actualSpread);
				}
			}

			spreadBet = accountEvent.getSpread();
			spreadIndicator = accountEvent.getSpreadindicator();	
			teamBet = teamBetOn(accountEvent, event);
			LOGGER.debug("actualSpread: " + actualSpread);
			LOGGER.debug("spreadBet: " + spreadBet);
			LOGGER.debug("spreadIndicator: " + spreadIndicator);
			LOGGER.debug("teamBet: " + teamBet);

			if ((teamBet == 1) && (gameTied) && (spreadIndicator.equals("+")) && ((actualSpread + spreadBet) == 0 )) {
				// --- User BET on Team 1 
				// --- AND the game tied 
				// --- AND it was a pk (+0) 
				result = "PUSH";
			} else if ((teamBet == 2) && (gameTied) && (spreadIndicator.equals("+")) && ((actualSpread + spreadBet) == 0 )) {
				// --- User BET on Team 2 
				// --- AND the game tied 
				// --- AND it was a pk (+0) 
				result = "PUSH";
			} else if ((teamBet == 1) && (winningTeam == 1) && (spreadIndicator.equals("-")) && ((actualSpread + spreadBet) > 0 )) {
				// --- User BET on Team 1 
				// --- AND Team 1 WON 
				// --- AND Team 1 was FAVORDED (spreadIndicator = "-") 
				// --- AND the actual spread of the game PLUS the spread that was bet is a POSITIVE number 
				// --- means the user COVERED the spread then the user WINS
				result = "WIN";
			} else if ((teamBet == 1) && (winningTeam == 1 || gameTied) && (spreadIndicator.equals("+"))) {
				// --- User BET on Team 1 
				// --- AND Team 1 WON 
				// --- AND Team 1 was NOT FAVORED (spreadIndicator = "+") 
				// --- means the user COVERED the spread then the user WINS
				result = "WIN";
			} else if ((teamBet == 1) && (winningTeam == 2) && (spreadIndicator.equals("+")) && ((actualSpread - spreadBet) < 0 )) {
				// --- User BET on Team 1 
				// --- AND Team 1 LOST
				// --- AND Team 1 was NOT FAVORED (spreadIndicator = "+") 
				// --- AND the actual spread of the game MINUS the spread that was bet is a NEGATIVE number
				// --- EX: User bets KC +6 and KC loses by 5 (actual spread = 5 minus the spread bet of +6 = -1)
				// --- means the user COVERED the spread then the user WINS
				result = "WIN";
			} else if ((teamBet == 1) && (winningTeam == 2) && (spreadIndicator.equals("+")) && ((actualSpread - spreadBet) > 0 )) {
				// --- User BET on Team 1 
				// --- AND Team 1 LOST
				// --- AND Team 1 was NOT FAVORED (spreadIndicator = "+") 
				// --- AND the actual spread of the game MINUS the spread that was bet is a NEGATIVE number
				// --- EX: User bets KC +6 and KC loses by 14 (actual spread = 14 minus the spread bet of +6 = 8)
				// --- means the user COVERED the spread then the user LOSES
				result = "LOSS";
			} else if ((teamBet == 1) && (winningTeam == 1) && (spreadIndicator.equals("-")) && ((actualSpread + spreadBet) < 0 )) {
				// --- User BET on Team 1 
				// --- AND Team 1 WON 
				// --- AND Team 1 was FAVORED (spreadIndicator = "-") 
				// --- AND the actual spread of the game PLUS the spread that was bet is a NEGATIVE number 
				// --- EX: User bets KC -6 and KC wins by 5 (actual spread = 5 plus the spread bet of -6 = -1)
				// --- means the user DID NOT COVER the spread then the user LOSES
				result = "LOSS";
			} else if ((teamBet == 1) && (winningTeam == 2 || gameTied) && (spreadIndicator.equals("-"))) {
				// --- User BET on Team 1 
				// --- AND Team 1 LOST 
				// --- AND Team 1 was FAVORED (spreadIndicator = "-") 
				// --- means the user DID NOT COVER the spread then the user LOSES
				result = "LOSS";
			} else if ((teamBet == 2) && (winningTeam == 2) && (spreadIndicator.equals("-")) && ((actualSpread + spreadBet) > 0 )) {
				// --- User BET on Team 2 
				// --- AND Team 2 WON 
				// --- AND Team 2 was FAVORDED (spreadIndicator = "-") 
				// --- AND the actual spread of the game PLUS the spread that was bet is a POSITIVE number 
				// --- means the user COVERED the spread then the user WINS
				result = "WIN";
			} else if ((teamBet == 2) && (winningTeam == 2 || gameTied) && (spreadIndicator.equals("+"))) {
				// --- User BET on Team 2 
				// --- AND Team 2 WON 
				// --- AND Team 2 was NOT FAVORED (spreadIndicator = "+") 
				// --- means the user COVERED the spread then the user WINS
				result = "WIN";
			} else if ((teamBet == 2) && (winningTeam == 1) && (spreadIndicator.equals("+")) && ((actualSpread - spreadBet) < 0 )) {
				// --- User BET on Team 2
				// --- AND Team 2 LOST
				// --- AND Team 2 was NOT FAVORED (spreadIndicator = "+") 
				// --- AND the actual spread of the game MINUS the spread that was bet is a NEGATIVE number
				// --- EX: User bets KC +6 and KC loses by 5 (actual spread = 5 minus the spread bet of +6 = -1)
				// --- means the user COVERED the spread then the user WINS
				result = "WIN";
			} else if ((teamBet == 2) && (winningTeam == 1) && (spreadIndicator.equals("+")) && ((actualSpread - spreadBet) > 0 )) {
				// --- User BET on Team 2
				// --- AND Team 2 LOST
				// --- AND Team 2 was NOT FAVORED (spreadIndicator = "+") 
				// --- AND the actual spread of the game MINUS the spread that was bet is a NEGATIVE number
				// --- EX: User bets KC +6 and KC loses by 5 (actual spread = 5 minus the spread bet of +6 = -1)
				// --- means the user COVERED the spread then the user WINS
				result = "LOSS";
			} else if ((teamBet == 2) && (winningTeam == 2) && (spreadIndicator.equals("-")) && ((actualSpread + spreadBet) < 0 )) {
				// --- User BET on Team 2 
				// --- AND Team 2 WON 
				// --- AND Team 2 was FAVORED (spreadIndicator = "-") 
				// --- AND the actual spread of the game PLUS the spread that was bet is a NEGATIVE number 
				// --- EX: User bets KC -6 and KC wins by 5 (actual spread = 5 plus the spread bet of -6 = -1)
				// --- means the user DID NOT COVER the spread then the user LOSES
				result = "LOSS";
			} else if ((teamBet == 2) && (winningTeam == 1 || gameTied) && (spreadIndicator.equals("-"))) {
				// --- User BET on Team 2 
				// --- AND Team 2 LOST 
				// --- AND Team 2 was FAVORED (spreadIndicator = "-") 
				// --- means the user DID NOT COVER the spread then the user LOSES
				result = "LOSS";
			} else {
				// --- The only other scenario would be ones where the actual spread +/- the spread bet = ZERO, which would be a PUSH
				result = "PUSH";
			}
		} catch (Throwable t) {
			LOGGER.error("team1Score: " + team1Score);
			LOGGER.error("team2Score: " + team2Score);
			LOGGER.error("AccountEvent: " + accountEvent);
			LOGGER.error("EventPackage: " + event);
			LOGGER.error("actualSpread: " + actualSpread);
			LOGGER.error("spreadBet: " + spreadBet);
			LOGGER.error("spreadIndicator: " + spreadIndicator);
			LOGGER.error("teamBet: " + teamBet);
			LOGGER.error(t.getMessage(), t);
		}

		LOGGER.info("Exiting determineSpreadLineResult()");
		return result;
	}

	/**
	 * 
	 * @param team1Score
	 * @param team2Score
	 * @param accountEvent
	 * @param event
	 * @return
	 */
	private String determineTotalResult(Integer team1Score, Integer team2Score, AccountEvent accountEvent, EventPackage event) {
		LOGGER.info("Entering determineTotalResult()");
		LOGGER.debug("team1Score: " + team1Score);
		LOGGER.debug("team2Score: " + team2Score);
		LOGGER.debug("AccountEvent: " + accountEvent);
		LOGGER.debug("EventPackage: " + event);
		String result = "";
		Integer actualTotal = null;
		Float totalBet = null; 
		String totalIndicator = null;

		try {
			if (team1Score != null && team2Score != null && accountEvent != null) {
				actualTotal = team1Score + team2Score;
				totalBet = accountEvent.getTotal(); 
				totalIndicator = accountEvent.getTotalindicator();
		
				LOGGER.debug("actualTotal: " + actualTotal);
				LOGGER.debug("totalBet: " + totalBet);
				LOGGER.debug("totalIndicator: " + totalIndicator);
	
				// --- Assumption that totalIndicator is either "O" for OVER and "U" for UNDER
				if (totalIndicator.equals("o") && (totalBet < actualTotal)) {
					result = "WIN"; // --- User bet the OVER and their total bet was greater than the actualTotal so User wins
				} else if (totalIndicator.equals("o") && (totalBet > actualTotal)) {
					result = "LOSS"; // --- User bet the OVER and their total bet was less than the actualTotal so User loses
				} else if (totalIndicator.equals("u") && (totalBet > actualTotal)) {
					result = "WIN"; // --- User bet the UNDER and their total bet was greater than the actualTotal so User loses
				} else if (totalIndicator.equals("u") && (totalBet < actualTotal)) {
					result = "LOSS"; // --- User bet the UNDER and their total bet was less than the actualTotal so User wins
				} else {
					result = "PUSH"; // --- Otherwise bet was a push
				}
			}
		} catch (Throwable t) {
			LOGGER.error("team1Score: " + team1Score);
			LOGGER.error("team2Score: " + team2Score);
			LOGGER.error("AccountEvent: " + accountEvent);
			LOGGER.error("EventPackage: " + event);
			LOGGER.error("actualTotal: " + actualTotal);
			LOGGER.error("totalBet: " + totalBet);
			LOGGER.error("totalIndicator: " + totalIndicator);
			LOGGER.error(t.getMessage(), t);
		}

		LOGGER.info("Exiting determineTotalResult()");
		return result;
	}

	/**
	 * 
	 * @param team1Score
	 * @param team2Score
	 * @param accountEvent
	 * @param event
	 * @return
	 */
	private String determineMlResult(Integer team1Score, Integer team2Score, AccountEvent accountEvent, EventPackage event) {
		LOGGER.info("Entering determineMlResult()");
		LOGGER.debug("team1Score: " + team1Score);
		LOGGER.debug("team2Score: " + team2Score);
		LOGGER.debug("AccountEvent: " + accountEvent);
		LOGGER.debug("EventPackage: " + event);
		String result = "";
		Integer teamBet = null;
		
		try {
			teamBet = teamBetOn(accountEvent, event);
			LOGGER.debug("teamBet: " + teamBet);
	
			if (teamBet.intValue() == 1 && team1Score.intValue() > team2Score.intValue()) {
				result = "WIN"; // --- User bet on Team 1 and Team 1 scored more than Team 2 so user WINS
			} else if (teamBet.intValue() == 1 && team1Score.intValue() < team2Score.intValue()) {
				result = "LOSS"; // --- User bet on Team 1 and Team 1 scored less than Team 2 so user LOSES
			} else if (teamBet.intValue() == 2 && team2Score.intValue() > team1Score.intValue()) {
				result = "WIN"; // --- User bet on Team 2 and Team 2 scored more than Team 1 so user WINS
			} else if (teamBet.intValue() == 2 && team2Score.intValue() < team1Score.intValue()) {
				result = "LOSS"; // --- User bet on Team 2 and Team 2 scored less than Team 1 so user LOSES
			} else {
				result = "PUSH";
			}
		} catch (Throwable t) {
			LOGGER.error("team1Score: " + team1Score);
			LOGGER.error("team2Score: " + team2Score);
			LOGGER.error("AccountEvent: " + accountEvent);
			LOGGER.error("EventPackage: " + event);
			LOGGER.error("teamBet: " + teamBet);
			LOGGER.error(t.getMessage(), t);
		}

		LOGGER.info("Exiting determineMlResult()");
		return result;
	}

	
	/**
	 * @param accountEvent
	 * @return
	 */
	private int getSportId(AccountEvent accountEvent) {
		// --- sportId = 3  = Baseball
		// --- sportId = 4  = Hockey
		// --- sportId = 5  = Golf
		// --- sportId = 8  = Basketball
		// --- sportId = 9  = Football
		// --- sportId = 16 = Soccer
		int sportId = 0;
		String sport = accountEvent.getSport();
		LOGGER.debug("sport: " + sport);

		if (sport != null) {
			if (sport.startsWith("nfl")) {
				sportId = 9;
			} else if (sport.startsWith("ncaab")) {
				sportId = 8;
			} else if (sport.startsWith("mlb")) {
				sportId = 3;
			} else if (sport.startsWith("nba")) {
				sportId = 8;
			} else if (sport.startsWith("wnba")) {
				sportId = 8;
			} else if (sport.startsWith("ncaaf")) {
				sportId = 9;
			} else if (sport.startsWith("nhl")) {
				sportId = 4;
			}
		}

		return sportId;
	}
	
	/**
	 * @param accountEvent
	 * @return
	 */
	private Integer getGameTypeId(AccountEvent accountEvent) {
		// --- gameType = 0 = FINAL
		// --- gameType = 1 = 1st Half/Period
		// --- gameType = 2 = 2nd Half/Period
		// --- gameType = 3 = 3rd Period
		
		Integer gameTypeId = null;
		String sport = accountEvent.getSport();
		
		if (sport.toLowerCase().indexOf("lines") > 0) {
			gameTypeId = 0;
		} else if (sport.toLowerCase().indexOf("first") > 0) {
			gameTypeId = 1;
		} else if (sport.toLowerCase().indexOf("second") > 0) {
			gameTypeId = 2;
		} else if (sport.toLowerCase().indexOf("third") > 0) {
			gameTypeId = 3;
		}
		
		return gameTypeId;
	}

	/**
	 * 
	 * @param accountEvent
	 * @param eventPackage
	 * @return
	 */
	private Integer teamBetOn(AccountEvent accountEvent, EventPackage eventPackage) {
		Integer rotation1 = Integer.parseInt(eventPackage.getTeamone().getEventid());
		Integer rotation2 = Integer.parseInt(eventPackage.getTeamtwo().getEventid());
		Integer teamBetOn = null;

		// Check if it's 1H/1P, 2H/2P or 3H/3P
		if (accountEvent.getSport().contains("first")) {
			rotation1 = rotation1 + 1000;
			rotation2 = rotation2 + 1000;
		} else if (accountEvent.getSport().contains("second")) {
			rotation1 = rotation1 + 2000;
			rotation2 = rotation2 + 2000;
		} else if (accountEvent.getSport().contains("third")) {
			rotation1 = rotation1 + 3000;
			rotation2 = rotation2 + 3000;
		}

		// Now check which team was bet on
		if (accountEvent.getEventid().intValue() == rotation1.intValue()) {
			teamBetOn = 1;
		} else if (accountEvent.getEventid().intValue() == rotation2.intValue()) {
			teamBetOn = 2;
		} else {
			LOGGER.error("Could not identify team bet on for accountEvent: " + accountEvent);
			return null;
		}

		return teamBetOn;
	}
}