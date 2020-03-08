/**
 * 
 */
package com.ticketadvantage.services.dao.email.sportsbettingonline;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;

import com.ticketadvantage.services.dao.email.EmailProcessor;
import com.ticketadvantage.services.dao.sites.sportsinsights.SportsInsightsSite;
import com.ticketadvantage.services.email.SendText;
import com.ticketadvantage.services.email.ticketadvantage.TicketAdvantageGmailOath;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.BaseScrapper;
import com.ticketadvantage.services.model.EmailEvent;
import com.ticketadvantage.services.model.EventPackage;
import com.ticketadvantage.services.model.PendingEvent;

/**
 * 
 * @author jmiller
 *
 */
public class SportsBettingOnlineEmailProcessSite extends EmailProcessor {
	private static final Logger LOGGER = Logger.getLogger(SportsBettingOnlineEmailProcessSite.class);
	private static final SportsBettingOnlineEmailParser PP = new SportsBettingOnlineEmailParser();
	private static SportsInsightsSite SPORTS_INSIGHTS_SITE;
	
	static {
		SPORTS_INSIGHTS_SITE = new SportsInsightsSite("https://sportsinsights.actionnetwork.com/",
				"mojaxsventures@gmail.com", 
				"action1");
	}

	/**
	 * 
	 * @param emailaddress
	 * @param password
	 */
	public SportsBettingOnlineEmailProcessSite(String emailaddress, String password) {
		super("SportsBettingOnlineEmail", emailaddress, password);
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final SportsBettingOnlineEmailProcessSite proCapSportsProcessSite = new SportsBettingOnlineEmailProcessSite("support@sportsbettingonline.ag", "ticket123");
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

//		EmailContainer: EmailEvent [id=null, messagenum=94, fromemail="SportsBettingOnline.ag" <support@sportsbettingonline.ag>, toemail=ticketadvantage@gmail.com, ccemail=, bccemail=, subject=Betting Alert, bodytext=, bodyhtml=New Betting Alert<br><br>Player: WC1199<br>Amount: 28<br>Placed: 1/4/2018 3:36:11 AM<br>Detail:<br>STRAIGHT BET<br>[102] TOTAL u44-110
//				(TENNESSEE TITANS vrs KANSAS CITY CHIEFS)<br><br>
//
//				, emailname=null, inet=null, datesent=Thu Jan 04 08:37:04 UTC 2018, datereceived=Thu Jan 04 08:37:09 UTC 2018, datecreated=Thu Jan 04 08:37:15 UTC 2018, datemodified=Thu Jan 04 08:37:15 UTC 2018]

		//
		// Mail Service <support@sportsbettingonline.ag>
		//
		if (emailContainer != null && emailContainer.getFromemail() != null) {
			String from = emailContainer.getFromemail();
			if (from.contains("<") && from.contains(">")) { 
				int findex = from.indexOf("<");
				int bindex = from.indexOf(">");
				if (findex != -1 && bindex != -1) {
					from = from.substring(findex + 1, bindex);
					LOGGER.debug("From: " + from);
					if (from.equals("support@sportsbettingonline.ag")) {
						retValue = true;
					}
				}
			} else if (emailContainer.getFromemail().equals("support@sportsbettingonline.ag")) {
				retValue = true;				
			}
		}

		// Send text
		if (retValue) {
			try {
				for (BaseScrapper scrapper : baseScrappers) {
					if (accountname != null && accountid != null && (scrapper.getScrappername().toLowerCase().contains(accountname.toLowerCase()) || scrapper.getScrappername().toLowerCase().contains(accountid.toLowerCase())) && scrapper.getSendtextforaccount()) {
						TicketAdvantageGmailOath TicketAdvantageGmailOath = new TicketAdvantageGmailOath();
						final String accessToken = TicketAdvantageGmailOath.getAccessToken();
						final SendText sendText = new SendText();
						sendText.setOAUTH2_TOKEN(accessToken);
						String bodyText = emailContainer.getBodytext();
						if (bodyText == null || bodyText.length() == 0) {
							bodyText = emailContainer.getBodyhtml();
							bodyText = Jsoup.parse(bodyText).text();
						}
						sendText.sendTextWithMessage(scrapper.getMobiletext(), bodyText);
					}
				}
			} catch (Throwable t) {
				LOGGER.error(t.getMessage(), t);
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
			if (emailContainer.getBodytext() != null && emailContainer.getBodytext().length() > 0) {
				pendingEvents = PP.parsePendingBets(emailContainer.getBodytext(), accountname, accountid);
				if (pendingEvents != null && !pendingEvents.isEmpty()) {
					final Iterator<PendingEvent> itr = pendingEvents.iterator();
					while (itr.hasNext()) {
						final PendingEvent pe = itr.next();
						emailContainer.setInet(pe.getInet());
						final EventPackage ep = SPORTS_INSIGHTS_SITE.getEventByIdAllNoGameSport(pe.getRotationid());
						LOGGER.debug("PendingEvent: " + pe);
						LOGGER.debug("EventPackage: " + ep);

						if (ep != null) {
							if ("NFL".equals(ep.getSporttype())) {
								pe.setGamesport("Football");
								pe.setGametype("NFL");
							} else if ("NCAAF".equals(ep.getSporttype())) {
								pe.setGamesport("Football");
								pe.setGametype("NCAA");
							} else if ("NBA".equals(ep.getSporttype())) {
								pe.setGamesport("Basketball");
								pe.setGametype("NBA");
							} else if ("NCAAB".equals(ep.getSporttype())) {
								pe.setGamesport("Basketball");
								pe.setGametype("NCAA");
							} else if ("WNBA".equals(ep.getSporttype())) {
								pe.setGamesport("Basketball");
								pe.setGametype("WNBA");
							} else if ("MLB".equals(ep.getSporttype())) {
								pe.setGamesport("Baseball");
								pe.setGametype("MLB");
							} else if ("NHL".equals(ep.getSporttype())) {
								pe.setGamesport("Hockey");
								pe.setGametype("NHL");								
							}
	
							// Set dates
							pe.setGamedate(ep.getEventdatetime());
							pe.setEventdate(pe.getGamedate().toString());
						}

						LOGGER.debug("PE: " + pe);
					}
				} else {
					LOGGER.error("No PENDING EVENT");
				}
			} else if (emailContainer.getBodyhtml() != null && emailContainer.getBodyhtml().length() > 0) {
				final String html = emailContainer.getBodyhtml();
				final String text = Jsoup.parse(html).text();
				pendingEvents = PP.parsePendingBets(text, accountname, accountid);
				if (pendingEvents != null && !pendingEvents.isEmpty()) {
					final Iterator<PendingEvent> itr = pendingEvents.iterator();
					while (itr.hasNext()) {
						final PendingEvent pe = itr.next();
						emailContainer.setInet(pe.getInet());
						final EventPackage ep = SPORTS_INSIGHTS_SITE.getEventByIdAllNoGameSport(pe.getRotationid());
						LOGGER.debug("PendingEvent: " + pe);
						LOGGER.debug("EventPackage: " + ep);

						if (ep != null) {
							if ("NFL".equals(ep.getSporttype())) {
								pe.setGamesport("Football");
								pe.setGametype("NFL");
							} else if ("NCAAF".equals(ep.getSporttype())) {
								pe.setGamesport("Football");
								pe.setGametype("NCAA");
							} else if ("NBA".equals(ep.getSporttype())) {
								pe.setGamesport("Basketball");
								pe.setGametype("NBA");
							} else if ("NCAAB".equals(ep.getSporttype())) {
								pe.setGamesport("Basketball");
								pe.setGametype("NCAA");
							} else if ("WNBA".equals(ep.getSporttype())) {
								pe.setGamesport("Basketball");
								pe.setGametype("WNBA");
							} else if ("MLB".equals(ep.getSporttype())) {
								pe.setGamesport("Baseball");
								pe.setGametype("MLB");
							} else if ("NHL".equals(ep.getSporttype())) {
								pe.setGamesport("Hockey");
								pe.setGametype("NHL");								
							}
	
							// Set dates
							pe.setGamedate(ep.getEventdatetime());
							pe.setEventdate(pe.getGamedate().toString());
						}

						LOGGER.debug("PE: " + pe);
					}
				} else {
					LOGGER.error("No PENDING EVENT");
				}
			} else {
				pendingEvents = PP.parsePendingBets(emailContainer.getSubject(), accountname, accountid);
				if (pendingEvents != null && !pendingEvents.isEmpty()) {
					final Iterator<PendingEvent> itr = pendingEvents.iterator();
					while (itr.hasNext()) {
						final PendingEvent pe = itr.next();
						emailContainer.setInet(pe.getInet());
						final EventPackage ep = SPORTS_INSIGHTS_SITE.getEventByIdAllNoGameSport(pe.getRotationid());
						LOGGER.debug("PendingEvent: " + pe);
						LOGGER.debug("EventPackage: " + ep);

						if (ep != null) {
							if ("NFL".equals(ep.getSporttype())) {
								pe.setGamesport("Football");
								pe.setGametype("NFL");
							} else if ("NCAAF".equals(ep.getSporttype())) {
								pe.setGamesport("Football");
								pe.setGametype("NCAA");
							} else if ("NBA".equals(ep.getSporttype())) {
								pe.setGamesport("Basketball");
								pe.setGametype("NBA");
							} else if ("NCAAB".equals(ep.getSporttype())) {
								pe.setGamesport("Basketball");
								pe.setGametype("NCAA");
							} else if ("WNBA".equals(ep.getSporttype())) {
								pe.setGamesport("Basketball");
								pe.setGametype("WNBA");
							} else if ("MLB".equals(ep.getSporttype())) {
								pe.setGamesport("Baseball");
								pe.setGametype("MLB");
							} else if ("NHL".equals(ep.getSporttype())) {
								pe.setGamesport("Hockey");
								pe.setGametype("NHL");								
							}
	
							// Set dates
							pe.setGamedate(ep.getEventdatetime());
							pe.setEventdate(pe.getGamedate().toString());
						}

						LOGGER.debug("PE: " + pe);
					}
				} else {
					LOGGER.error("No PENDING EVENT");
				}
			}
		}

		LOGGER.info("Exiting getPendingBets()");
		return pendingEvents;
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
			if ("Betting Alert".equals(emailContainer.getSubject())) {
				// Check if we want to process this action
				if (emailContainer.getBodytext() != null && emailContainer.getBodytext().length() > 0) {
					String text = emailContainer.getBodytext().trim();
					LOGGER.debug("text: " + text);
					retValue = PP.isPendingBet(text, accountid);
				} else if (emailContainer.getBodyhtml() != null && emailContainer.getBodyhtml().length() > 0) {
					String html = emailContainer.getBodyhtml();
					String text = Jsoup.parse(html).text().trim();
					LOGGER.debug("text: " + text);
					retValue = PP.isPendingBet(text, accountid);
				}
			}
		}

		LOGGER.info("Exiting determineHasPendingBets()");
		return retValue;
	}
}