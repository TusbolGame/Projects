/**
 * 
 */
package com.ticketadvantage.services.batch;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import com.ticketadvantage.services.batch.processevent.BaseSiteBatch;
import com.ticketadvantage.services.dao.email.drh.DrhParser;
import com.ticketadvantage.services.dao.sites.sportsinsights.SportsInsightsSite;
import com.ticketadvantage.services.db.EmailEventDB;
import com.ticketadvantage.services.db.EmailScrapperDB;
import com.ticketadvantage.services.db.RecordEventDB;
import com.ticketadvantage.services.email.MessageChangedListenerIMAP;
import com.ticketadvantage.services.email.MessageCountListenerIMAP;
import com.ticketadvantage.services.email.RetrieveEmailImpl;
import com.ticketadvantage.services.email.SendText;
import com.ticketadvantage.services.email.shootfromanywhere.ShootFromAnywhereGmailOath;
import com.ticketadvantage.services.email.ticketadvantage.TicketAdvantageGmailOath;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.Accounts;
import com.ticketadvantage.services.model.BaseRecordEvent;
import com.ticketadvantage.services.model.BaseScrapper;
import com.ticketadvantage.services.model.EmailAccounts;
import com.ticketadvantage.services.model.EmailEvent;
import com.ticketadvantage.services.model.EmailScrapper;
import com.ticketadvantage.services.model.EventPackage;
import com.ticketadvantage.services.model.EventsPackage;
import com.ticketadvantage.services.model.GlobalScrapper;
import com.ticketadvantage.services.model.PendingEvent;
import com.ticketadvantage.services.service.WoOMiddleRulesResource;
import com.ticketadvantage.services.transactions.RecordTransaction;

/**
 * @author jmiller
 *
 */
@WebListener
@Service
public class DrHEmailEventBatch extends BaseSiteBatch implements Runnable, ServletContextListener {
	private static final Logger LOGGER = Logger.getLogger(DrHEmailEventBatch.class);
	private static final ShootFromAnywhereGmailOath ShootFromAnywhereGmailOath = new ShootFromAnywhereGmailOath();
	private static final TicketAdvantageGmailOath TicketAdvantageGmailOath = new TicketAdvantageGmailOath();
	private static final SportsInsightsSite SportsInsightSite = new SportsInsightsSite("https://sportsinsights.actionnetwork.com", "mojaxsventures@gmail.com", "Jaxson17");
	private static final DrhParser DHP = new DrhParser();
	private final EmailEventDB EMAILEVENTDB = new EmailEventDB();
	private final EmailScrapperDB EMAILSCRAPPERDB = new EmailScrapperDB();
	private final RecordEventDB RECORDEVENTDB = new RecordEventDB();
	private final RecordTransaction RECORDTRANSACTION = new RecordTransaction(RECORDEVENTDB);
	private static Integer SCRAPPER_SERVER;
	private static EventsPackage eventsPackage;
	private volatile boolean shutdown = false;
	private List<EmailScrapper> scrappers = new ArrayList<EmailScrapper>();
	protected List<BaseScrapper> baseScrappers;

	static {
		// Get the properties
		try {
			final Properties prop = new Properties();
			final InputStream in = new FileInputStream("/opt/tomcat/server.properties");
			prop.load(in);
			SCRAPPER_SERVER = Integer.parseInt(prop.getProperty("SCRAPPER_SERVER"));
			LOGGER.error("SCRAPPER_SERVER: " + SCRAPPER_SERVER);
			in.close();

			eventsPackage = SportsInsightSite.getNextDaySport("mlblines");
//			eventsPackage = SportsInsightSite.getDayOfSport("mlblines");
			DHP.setEventsPackage(eventsPackage);
		} catch (IOException ioe) {
			LOGGER.error(ioe.getMessage(), ioe);
			System.exit(99);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			System.exit(99);
		}
	}

	/**
	 * 
	 */
	public DrHEmailEventBatch() {
		super();
		LOGGER.info("Entering DrHEmailEventBatch()");
		LOGGER.info("Exiting DrHEmailEventBatch()");
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		DrHEmailEventBatch scrapperEventBatch = new DrHEmailEventBatch();
		final EmailEvent emailContainer = new EmailEvent();
		emailContainer.setBccemail("");
		emailContainer.setBodyhtml("Zimerman/Sheidz un 9.5 -110");
		emailContainer.setBodytext("Zimerman/Sheidz un 9.5 -110");
		emailContainer.setDatecreated(new Date());
		emailContainer.setDatereceived(new Date());
		emailContainer.setDatesent(new Date());
		emailContainer.setEmailname("null");
		emailContainer.setFromemail("Dr H <drh@drhpicks.com>");
		emailContainer.setInet("null");
		emailContainer.setMessagenum(499);
		emailContainer.setSubject("Release warning");
		emailContainer.setToemail("J D <drh@drhpicks.com>");
		scrapperEventBatch.processEmail(emailContainer);

		// scrapperEventBatch.run();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
    @Override
    public void contextInitialized(ServletContextEvent event) {
    		LOGGER.error("Entering contextInitialized()");

/*
    		this.RECORDEVENTDB.start();
    		this.EMAILSCRAPPERDB.start();
    		this.EMAILEVENTDB.start();
		new Thread(this).start();
*/

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
/*
	    	this.EMAILEVENTDB.complete();
	    	this.EMAILSCRAPPERDB.complete();
	    	this.RECORDEVENTDB.complete();
*/

	    	LOGGER.error("Exiting contextDestroyed()");
    }

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		LOGGER.info("Entering run()");
		RetrieveEmailImpl retrieveEmailImpl = null;
		List<EmailScrapper> tempScrappers = null;

		try {
			tempScrappers = EMAILSCRAPPERDB.findAll();
			for (EmailScrapper wscrapper : tempScrappers) {
				String name = wscrapper.getScrappername();
				if ("DrH".equals(name)) {
					scrappers.add(wscrapper);
				}
			}
			LOGGER.error("Scrappers.size(): " + scrappers.size());

			// Compare the dates to order them appropriately
			Collections.sort(scrappers, new Comparator<EmailScrapper>() {
				public int compare(EmailScrapper o1, EmailScrapper o2) {
					if (o1.getUserid() == null || o2.getUserid() == null)
						return 0;
					return o1.getUserid().compareTo(o2.getUserid());
				}
			});
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		try {
			retrieveEmailImpl = new RetrieveEmailImpl("shootfromanywhere@gmail.com", "imap.gmail.com", 993, true);
			retrieveEmailImpl.setToken("shootfromanywhere@gmail.com", ShootFromAnywhereGmailOath.getAccessToken());

			// Setup the listener
			retrieveEmailImpl.setMessageCounterListerer(new MessageCountListenerIMAP(this));
			retrieveEmailImpl.setMessageChangedListerer(new MessageChangedListenerIMAP(this));

			// Thead to start the email
			Thread t = new Thread(retrieveEmailImpl);
			t.setName("JPM-shootfromanywhere");
			t.start();

			while (!shutdown) {
				scrappers.clear();
				tempScrappers = EMAILSCRAPPERDB.findAll();
				for (EmailScrapper wscrapper : tempScrappers) {
					String name = wscrapper.getScrappername();
					if ("DrH".equals(name)) {
						scrappers.add(wscrapper);
					}
				}
				
				// Compare the dates to order them appropriately
				Collections.sort(scrappers, new Comparator<EmailScrapper>() {
					public int compare(EmailScrapper o1, EmailScrapper o2) {
						if (o1.getUserid() == null || o2.getUserid() == null)
							return 0;
						return o1.getUserid().compareTo(o2.getUserid());
					}
				});

				try {
					// Check every 30 minutes
					Thread.sleep(30 * 60 * 1000);
				} catch (InterruptedException e) {
					LOGGER.error("InterruptedException");
					// good practice
					Thread.currentThread().interrupt();
					return;
				} catch (Throwable th) {
					LOGGER.error("Throwable in thread", th);
				}
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}
	}

	/**
	 * 
	 * @param emailContainer
	 */
	public synchronized void processEmail(EmailEvent emailContainer) {
		LOGGER.error("EmailContainer: " + emailContainer);

		try {
			Set<PendingEvent> pendingEvents = null;
			if (emailContainer != null) {
				final String bodyText = emailContainer.getBodytext();
				if (bodyText != null && bodyText.length() > 0) {
					pendingEvents = DHP.parsePendingBets(bodyText, "DrH", "DrH");
				} else {
					String bodyHtml = emailContainer.getBodyhtml();
					if (bodyHtml != null && bodyHtml.length() > 0) {
						bodyHtml = Jsoup.parse(bodyHtml).text().trim();
						pendingEvents = DHP.parsePendingBets(bodyHtml, "DrH", "DrH");
					}
				}
			}

			// Check if we already have
			checkSite(pendingEvents);

			// Persist the email
			EMAILEVENTDB.persist(emailContainer);
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}
	}

	/**
	 * 
	 * @param sitePendingEvents
	 * @throws BatchException
	 */
	public void checkSite(Set<PendingEvent> sitePendingEvents) throws BatchException {
		LOGGER.info("Entering checkSite()");

		try {
			LOGGER.debug("sitePendingEvents: " + sitePendingEvents);

			if (sitePendingEvents != null && !sitePendingEvents.isEmpty()) {
				LOGGER.debug("sitePendingEvents: " + sitePendingEvents);

				for (BaseScrapper scrapper : scrappers) {
					LOGGER.debug("Scrapper: " + scrapper);

					if (scrapper.getOnoff()) {
						for (PendingEvent sitePendingEvent : sitePendingEvents) {
							final EmailScrapper emailScrapper = (EmailScrapper)scrapper;
							if (emailScrapper.getSources() != null && !emailScrapper.getSources().isEmpty()) {
								final EmailAccounts eAccount = emailScrapper.getSources().iterator().next();
								if (eAccount.getInet().equals(sitePendingEvent.getInet()) &&
									eAccount.getAccountid().equals(sitePendingEvent.getCustomerid())) {
									// Check if we need to place a transaction
									checkSitePendingEvent("global", sitePendingEvents, scrapper);
								}
							}
						}
					}
				}
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		LOGGER.info("Exiting checkSite()");
	}

	/**
	 * 
	 * @param scrapper
	 * @param pendingEvent
	 * @param eventPackage
	 */
	protected void placeSiteTransactions(BaseScrapper scrapper, PendingEvent pendingEvent, EventPackage eventPackage) {
		LOGGER.info("Entering placeTransactions()");
		LOGGER.error("PendingEvent: " + pendingEvent);

		try {
			final String type = pendingEvent.getEventtype();
			final EmailScrapper wscrapper = (EmailScrapper)scrapper;
			LOGGER.debug("wscrapper: " + wscrapper);

			Set<Accounts> destinations = null;
			if (wscrapper.getFullfill() != null && wscrapper.getFullfill().booleanValue()) {
				destinations = wscrapper.getEmailorderdestinations();
			} else {
				destinations = wscrapper.getDestinations();
			}

			// Send a message for WoO
			if (wscrapper.getUserid().longValue() == 1) {
				try {
					final SendText sendText = new SendText();
					sendText.setOAUTH2_TOKEN(TicketAdvantageGmailOath.getAccessToken());
					sendText.sendTextWithMessage(wscrapper.getMobiletext(), "DrH " + pendingEvent.getRotationid() + " " + pendingEvent.getEventtype() + " " + pendingEvent.getTeam() + " " + pendingEvent.getLineplusminus() + pendingEvent.getLine() + " " + pendingEvent.getJuiceplusminus() + pendingEvent.getJuice());
				} catch (Exception be) {
					LOGGER.error(be.getMessage(), be);
				}
			}

			// If we are middling, then execute middle rules
			String playType = "Standard";
			if (wscrapper.getMiddlerules().booleanValue()) {
				playType = "Middle";
			}

			String team1 = "";
			String team2 = "";
			final EventPackage evp = SportsInsightSite.getEventById(pendingEvent.getRotationid(), "MLB");
			LOGGER.error("EventPackage: " + evp);

			if (evp != null) {
				team1 = evp.getTeamone().getTeam();
				team2 = evp.getTeamtwo().getTeam();
			}

			if ("spread".equals(type)) {
				// RECORDTRANSACTION
			} else if ("total".equals(type)) {
				final Long totalId = RECORDTRANSACTION.recordTotalEvent(pendingEvent.getLinetype(),
					pendingEvent.getGamesport(), pendingEvent.getGametype(),
					pendingEvent.getRotationid(), team1, team2,
					pendingEvent.getLineplusminus(), pendingEvent.getLine(),
					pendingEvent.getJuiceplusminus(), pendingEvent.getJuice(),
					pendingEvent.getEventdate(), pendingEvent.getGamedate(),
					wscrapper.getTotallineadjustment(), wscrapper.getTotaljuiceindicator(),
					wscrapper.getTotaljuice(), wscrapper.getTotaljuiceadjustment(),
					pendingEvent.getCustomerid(), wscrapper.getScrappername(),
					wscrapper.getMobiletext(), playType, wscrapper.getUserid(),
					wscrapper.getTotalmaxamount(), wscrapper.getTotalmaxamount(), wscrapper.getFullfill(),
					wscrapper.getCheckdupgame(), wscrapper.getPlayotherside(),
					wscrapper.getEnableretry(), wscrapper.getOrderamount(), destinations, null, true, wscrapper.getSendtextforaccount(),
					wscrapper.getHumanspeed());

				// If we are middling, then execute middle rules
				if (wscrapper.getMiddlerules().booleanValue()) {
					new WoOMiddleRulesResource(
						totalId,
						"total",
						wscrapper.getEmailmiddledestinations(),
						RECORDEVENTDB,
						evp,
						wscrapper.getMobiletext(),
						wscrapper.getUserid(),
						wscrapper.getTotalmaxamount());
				}
			} else if ("ml".equals(type)) {
				final Long mlId = RECORDTRANSACTION.recordMlEvent(pendingEvent.getLinetype(),
					pendingEvent.getGamesport(), pendingEvent.getGametype(),
					pendingEvent.getRotationid(), team1, team2,
					pendingEvent.getLineplusminus(), pendingEvent.getLine(),
					pendingEvent.getJuiceplusminus(), pendingEvent.getJuice(),
					pendingEvent.getEventdate(), pendingEvent.getGamedate(),
					wscrapper.getMllineadjustment(), wscrapper.getMlindicator(),
					wscrapper.getMlline(), pendingEvent.getCustomerid(),
					wscrapper.getScrappername(), wscrapper.getMobiletext(), playType,
					wscrapper.getUserid(), wscrapper.getMlmaxamount(), wscrapper.getMlmaxamount(), wscrapper.getFullfill(),
					wscrapper.getCheckdupgame(), wscrapper.getPlayotherside(),
					wscrapper.getEnableretry(), wscrapper.getOrderamount(), destinations, null, true, wscrapper.getSendtextforaccount(),
					wscrapper.getHumanspeed());

				// If we are middling, then execute middle rules
				if (wscrapper.getMiddlerules().booleanValue()) {
					new WoOMiddleRulesResource(
						mlId,
						"ml",
						wscrapper.getEmailmiddledestinations(),
						RECORDEVENTDB,
						evp,
						wscrapper.getMobiletext(),
						wscrapper.getUserid(),
						wscrapper.getMlmaxamount());
				}
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		LOGGER.info("Exiting placeTransactions()");
	} 

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.batch.processevent.BaseSiteBatch#deleteRemovedPendingEvents(java.lang.String, java.util.Set, com.ticketadvantage.services.model.BaseScrapper)
	 */
	@Override
	protected void deleteRemovedPendingEvents(String pendingType, Set<PendingEvent> sitePendingEvents, BaseScrapper scrapper) throws BatchException {
		LOGGER.info("Entering deleteRemovedPendingEvents()");
		LOGGER.info("Exiting deleteRemovedPendingEvents()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.batch.processevent.BaseSiteBatch#persistPendingEvent(boolean, com.ticketadvantage.services.model.PendingEvent, com.ticketadvantage.services.model.PendingEvent)
	 */
	@Override
	protected void persistPendingEvent(boolean found, PendingEvent sitePendingEvent, PendingEvent copySitePendingEvent) {
		LOGGER.info("Entering persistPendingEvent()");

		try {
			if (!found) {
				LOGGER.debug("copySitePendingEvent: " + copySitePendingEvent);
				LOGGER.debug("copySitePendingEvent.getDoposturl(): " + copySitePendingEvent.getDoposturl());

				copySitePendingEvent = PENDINGEVENTDB.persist(copySitePendingEvent);
				LOGGER.debug("Persisted event: " + copySitePendingEvent);
			}
		} catch (Throwable t) {
			LOGGER.error(t);
		}

		LOGGER.info("Exiting persistPendingEvent()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.batch.processevent.BaseSiteBatch#setupAccountEvents(com.ticketadvantage.services.model.BaseRecordEvent, com.ticketadvantage.services.model.BaseScrapper)
	 */
	@Override
	protected void setupAccountEvents(BaseRecordEvent event, BaseScrapper scrapper) {
		LOGGER.info("Entering setupAccountEvents()");
		LOGGER.debug("Event: " + event);

		// Setup all the accounts
		setupAccounts(event, copyAccounts(((EmailScrapper)scrapper).getDestinations()), scrapper.getEnableretry(), scrapper.getSendtextforaccount(), scrapper.getHumanspeed());

		LOGGER.info("Exiting setupAccountEvents()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.batch.processevent.BaseSiteBatch#setupBuyOrder(com.ticketadvantage.services.model.BaseRecordEvent, com.ticketadvantage.services.model.BaseScrapper, java.lang.Float, java.lang.Boolean)
	 */
	@Override
	protected void setupBuyOrder(BaseRecordEvent event, BaseScrapper scrapper, Float numunits, Boolean islean) {
		LOGGER.info("Entering setupBuyOrder()");
		LOGGER.debug("Event: " + event);

		// Setup all the accounts
		final Set<Accounts> emailAccounts = copyAccounts(((EmailScrapper)scrapper).getEmailorderdestinations());
		setupBuyOrderAccounts(event, emailAccounts, scrapper, numunits, islean);

		LOGGER.info("Exiting setupBuyOrder()");		
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.batch.processevent.BaseSiteBatch#getScrapperSources(com.ticketadvantage.services.model.BaseScrapper)
	 */
	@Override
	protected Set<Accounts> getScrapperSources(BaseScrapper scrapper) {
		LOGGER.info("Entering getScrapperSources()");
		LOGGER.info("Exiting getScrapperSources()");
		return null;
	}

	@Override
	protected void setupBestPriceBuyOrder(BaseRecordEvent event, BaseScrapper scrapper, Float numunits,
			Boolean islean) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected GlobalScrapper getGlobalScrapper() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void setGlobalScrapper(GlobalScrapper globalScrapper) {
		// TODO Auto-generated method stub
		
	}


}