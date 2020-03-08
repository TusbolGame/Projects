/**
 * 
 */
package com.ticketadvantage.services.dao.email.sportsinsights;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;

import com.ticketadvantage.services.dao.email.EmailProcessor;
import com.ticketadvantage.services.db.RecordEventDB;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.AccountEvent;
import com.ticketadvantage.services.model.Accounts;
import com.ticketadvantage.services.model.BaseScrapper;
import com.ticketadvantage.services.model.EmailEvent;
import com.ticketadvantage.services.model.EventPackage;
import com.ticketadvantage.services.model.MlRecordEvent;
import com.ticketadvantage.services.model.PendingEvent;
import com.ticketadvantage.services.model.TotalRecordEvent;
import com.ticketadvantage.services.service.PreviewAndCompleteResource;
import com.ticketadvantage.services.site.util.AccountSite;

/**
 * 
 * @author jmiller
 *
 */
public class SportsInsightsEmailProcessSite extends EmailProcessor {
	private static final Logger LOGGER = Logger.getLogger(SportsInsightsEmailProcessSite.class);
	private static final SportsInsightsEmailParser PP = new SportsInsightsEmailParser();
	private RecordEventDB RECORDEVENTDB  = new RecordEventDB();

	/**
	 * 
	 * @param emailaddress
	 * @param password
	 */
	public SportsInsightsEmailProcessSite(String emailaddress, String password) {
		super("SportsInsightsEmail", emailaddress, password);
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final SportsInsightsEmailProcessSite proCapSportsProcessSite = new SportsInsightsEmailProcessSite("support@sportsbettingonline.ag", "ticket123");
			final EmailEvent emailContainer = new EmailEvent();
			emailContainer.setBccemail("");
			emailContainer.setBodyhtml("");
//			emailContainer.setBodytext("New Betting Alert\\r\\n\\r\\nPlayer: WC24008\\r\\nAmount: 330\\r\\nPlaced: 3/3/2018 11:16:08 PM\\r\\nDetail:\\r\\nSTRAIGHT BET\\r\\n[1814] TOTAL u108½-110 (1H BROOKLYN NETS vrs 1H LOS ANGELES CLIPPERS)\\r\\n");
			emailContainer.setBodytext("New Betting Alert\r\n\r\nPlayer: WC24008\r\nAmount: 330\r\nPlaced: 3/7/2018 9:05:41 AM\r\nDetail:\r\nSTRAIGHT BET\r\n[618] TOTAL u138½-110 (LOUISIANA TECH vrs NORTH TEXAS)\r\n");
			emailContainer.setDatecreated(new Date());
			emailContainer.setDatereceived(new Date());
			emailContainer.setDatesent(new Date());
			emailContainer.setEmailname("WC24008");
			emailContainer.setFromemail("support@sportsbettingonline.ag");
			emailContainer.setInet("BigManSports");
			emailContainer.setMessagenum(1);
			emailContainer.setSubject("Betting Alert");
			emailContainer.setToemail("ticketadvantage@gmail.com");
			final Set<PendingEvent> pendingEvents = proCapSportsProcessSite.getPendingBets("user", emailContainer, null);

			if (pendingEvents != null && !pendingEvents.isEmpty()) {
				final Iterator<PendingEvent> itr = pendingEvents.iterator();
				while (itr != null && itr.hasNext()) {
					final PendingEvent pendingEvent = itr.next();
					System.out.println("PendingEvent: " + pendingEvent);
				}
			}
		} catch (BatchException be) {
			be.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.email.EmailProcessor#determineEmailProcessor(com.ticketadvantage.services.model.EmailEvent, java.util.List)
	 */
	@Override
	public boolean determineEmailProcessor(EmailEvent emailContainer, List<BaseScrapper> baseScrappers) throws BatchException {
		LOGGER.info("Entering determineEmailProcessor()");
		boolean retValue = false;

		//
		// Sports Insights <sportsinsights@sportsinsights.com>
		//
		if (emailContainer != null && emailContainer.getFromemail() != null) {
			String from = emailContainer.getFromemail();
			if (from.contains("<") && from.contains(">")) { 
				int findex = from.indexOf("<");
				int bindex = from.indexOf(">");
				if (findex != -1 && bindex != -1) {
					from = from.substring(findex + 1, bindex);
					LOGGER.debug("From: " + from);
					if (from.equals("sportsinsights@sportsinsights.com") || from.equals("kc2coast@gmail.com")) {
						retValue = true;
					}
				}
			} else if (emailContainer.getFromemail().equals("sportsinsights@sportsinsights.com") || from.equals("kc2coast@gmail.com")) {
				retValue = true;	
			}
		}

		LOGGER.info("Exiting determineEmailProcessor()");
		return retValue;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.email.EmailProcessor#processEmail(com.ticketadvantage.services.model.EmailEvent, java.util.List)
	 */
	@Override
	public boolean processEmail(EmailEvent emailContainer, List<BaseScrapper> baseScrappers) throws BatchException {
		LOGGER.info("Entering processEmail()");
		boolean retValue = false;

		if (determineHasPendingBets(emailContainer)) {
			retValue = true;
		}

		LOGGER.info("Entering processEmail()");
		return retValue;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.email.EmailProcessor#getPendingBets(java.lang.String, com.ticketadvantage.services.model.EmailEvent, java.util.List)
	 */
	@Override
	public Set<PendingEvent> getPendingBets(String pendingType, EmailEvent emailContainer, List<BaseScrapper> baseScrappers) throws BatchException {
		LOGGER.info("Entering getPendingBets()");
		Set<PendingEvent> pendingEvents = null;

		// Check for a valid email
		if (emailContainer != null) {
			final String bodyText = emailContainer.getBodytext();
			if (bodyText != null && bodyText.length() > 0) {
				pendingEvents = PP.parsePendingBets(bodyText, accountname, accountid);
			} else {
				String bodyHtml = emailContainer.getBodyhtml();
				if (bodyHtml != null && bodyHtml.length() > 0) {
					bodyHtml = Jsoup.parse(bodyHtml).text().trim();
					pendingEvents = PP.parsePendingBets(bodyHtml, accountname, accountid);
				}
			}

			if (pendingEvents != null && !pendingEvents.isEmpty()) {
				// Get the game details
				final EventPackage ep = getGameDetails(pendingEvents);

				if (ep != null) {
					for (PendingEvent pe : pendingEvents) {
						for (BaseScrapper scrapper : baseScrappers) {
							final TimeZone ptTimezone = TimeZone.getTimeZone("America/Los_Angeles");
							final Calendar yesterday = Calendar.getInstance(ptTimezone);
							final Calendar now = Calendar.getInstance(ptTimezone);

							// add one day to the date/calendar
							yesterday.add(Calendar.DAY_OF_YEAR, -1);
							yesterday.set(Calendar.HOUR_OF_DAY, 0);
							yesterday.set(Calendar.MINUTE, 0);
							yesterday.set(Calendar.SECOND, 0);
							yesterday.set(Calendar.MILLISECOND, 0);

							try {
								if ("total".equals(pe.getEventtype())) {
									final TotalRecordEvent tre = RECORDEVENTDB.getTotalEventByRotationId(Integer.parseInt(pe.getRotationid()), yesterday.getTime(), now.getTime(), getSportType(pe.getGamesport(), pe.getGametype(), pe.getLinetype()), scrapper.getUserid());
									if (tre != null) {
										final List<AccountEvent> accountEvents = RECORDEVENTDB.getTotalAccountEvents(tre.getId());
	
										// Loop through account events
										for (AccountEvent ae : accountEvents) {
											final String status = ae.getStatus();
											final Accounts account = RECORDEVENTDB.getAccount(ae.getAccountid());
											if (!"Complete".equals(status) || !"Error".equals(status)) {
												final PreviewAndCompleteResource previewAndCompleteResource = new PreviewAndCompleteResource();
												previewAndCompleteResource.setAccountEvent(ae);
												previewAndCompleteResource.setAccount(account);
												previewAndCompleteResource.setEvent(tre);
												previewAndCompleteResource.setEventType("total");
												previewAndCompleteResource.setSiteProcessor(AccountSite.GetAccountSite(account));
												previewAndCompleteResource.setCounter(new Integer(12));
												previewAndCompleteResource.setSleepTime(new Integer(10000));
												previewAndCompleteResource.startProcessing();
											}
										}
									}
								} else if ("ml".equals(pe.getEventtype())) {
									final MlRecordEvent mre = RECORDEVENTDB.getMlEventByRotationId(
											Integer.parseInt(pe.getRotationid()), 
											yesterday.getTime(), 
											now.getTime(),
											getSportType(pe.getGamesport(), 
											pe.getGametype(),
											pe.getLinetype()),
											scrapper.getUserid());
									if (mre != null) {
										final List<AccountEvent> accountEvents = RECORDEVENTDB.getMlAccountEvents(mre.getId());
	
										// Loop through account events
										for (AccountEvent ae : accountEvents) {
											final String status = ae.getStatus();
											final Accounts account = RECORDEVENTDB.getAccount(ae.getAccountid());
											if (!"Complete".equals(status) || !"Error".equals(status)) {
												final PreviewAndCompleteResource previewAndCompleteResource = new PreviewAndCompleteResource();
												previewAndCompleteResource.setAccountEvent(ae);
												previewAndCompleteResource.setAccount(account);
												previewAndCompleteResource.setEvent(mre);
												previewAndCompleteResource.setEventType("ml");
												previewAndCompleteResource.setSiteProcessor(AccountSite.GetAccountSite(account));
												previewAndCompleteResource.setCounter(new Integer(12));
												previewAndCompleteResource.setSleepTime(new Integer(10000));
												previewAndCompleteResource.startProcessing();
											}
										}
									}
								}
							} catch (Throwable t) {
								LOGGER.error(t.getMessage(), t);
							}
						}
					}
				}
			}
		}

		pendingEvents = null;
		LOGGER.info("Exiting getPendingBets()");
		return pendingEvents;
	}

	/**
	 * 
	 * @param gameSport
	 * @param gameType
	 * @param lineType
	 * @return
	 */
	private String getSportType(String gameSport, String gameType, String lineType) {
		String retValue = "";

		if (gameType != null && gameType.length() > 0) {
			if (gameType.contains("NFL")) {
				if ("game".equals(lineType)) {
					retValue = "nfllines";
				} else if ("first".equals(lineType)) {
					retValue = "nflfirst";
				} else if ("second".equals(lineType)) {
					retValue = "nflsecond";
				}
			} else if (gameType.contains("WNBA")) {
				if ("game".equals(lineType)) {
					retValue = "wnbalines";
				} else if ("first".equals(lineType)) {
					retValue = "wnbafirst";
				} else if ("second".equals(lineType)) {
					retValue = "wnbasecond";
				}
			} else if (gameType.contains("NBA")) {
				if ("game".equals(lineType)) {
					retValue = "nbalines";
				} else if ("first".equals(lineType)) {
					retValue = "nbafirst";
				} else if ("second".equals(lineType)) {
					retValue = "nbasecond";
				}
			} else if (gameType.contains("NCAAB") || (gameType + " " + gameSport).contains("NCAAB") ||
					   gameType.contains("NCAA Basketball") || (gameType + " " + gameSport).contains("NCAA Basketball") ||
					   gameType.contains("College Basketball") || (gameType + " " + gameSport).contains("College Basketball") ||
					   gameType.contains("NCAA Added Basketball") || (gameType + " " + gameSport).contains("NCAA Added Basketball")) {
				if ("game".equals(lineType)) {
					retValue = "ncaablines";
				} else if ("first".equals(lineType)) {
					retValue = "ncaabfirst";
				} else if ("second".equals(lineType)) {
					retValue = "ncaabsecond";
				}
			} else if (gameType.contains("NCAAF") || (gameType + " " + gameSport).contains("NCAAF") || 
					   gameType.contains("NCAA Football") || (gameType + " " + gameSport).contains("NCAA Football") || 
					   gameType.contains("College Football") || (gameType + " " + gameSport).contains("College Football") || 
					   gameType.contains("NCAA Added Football") || (gameType + " " + gameSport).contains("NCAA Added Football")) {
				if ("game".equals(lineType)) {
					retValue = "ncaaflines";
				} else if ("first".equals(lineType)) {
					retValue = "ncaaffirst";
				} else if ("second".equals(lineType)) {
					retValue = "ncaafsecond";
				}
			} else if (gameType.contains("NHL")) {
				if ("game".equals(lineType)) {
					retValue = "nhllines";
				} else if ("first".equals(lineType)) {
					retValue = "nhlfirst";
				} else if ("second".equals(lineType)) {
					retValue = "nhlsecond";
				} else if ("third".equals(lineType)) {
					retValue = "nhlthird";
				}
			} else if (gameType.contains("MLB")) {
				if ("game".equals(lineType)) {
					retValue = "mlblines";
				} else if ("first".equals(lineType)) {
					retValue = "mlbfirst";
				} else if ("second".equals(lineType)) {
					retValue = "mlbsecond";
				} else if ("third".equals(lineType)) {
					retValue = "mlbthird";
				}
			} else if (gameType.contains("International Baseball")) {
				if ("game".equals(lineType)) {
					retValue = "internationalbaseballlines";
				} else if ("first".equals(lineType)) {
					retValue = "internationalbaseballfirst";
				} else if ("second".equals(lineType)) {
					retValue = "internationalbaseballsecond";
				} else if ("third".equals(lineType)) {
					retValue = "internationalbaseballthrid";
				}
			}
		}

		return retValue;
	}

	/**
	 * 
	 * @param emailContainer
	 * @return
	 */
	private boolean determineHasPendingBets(EmailEvent emailContainer) {
		LOGGER.info("Entering determineHasPendingBets()");
		boolean retValue = false;

		// Check if we want to process this action
		if (emailContainer.getSubject() != null && emailContainer.getSubject().length() > 0) {
			if (" Alert ".equals(emailContainer.getSubject())) {
				retValue = true;
			}
		}

		LOGGER.info("Exiting determineHasPendingBets()");
		return retValue;
	}
}