/**
 * 
 */
package com.ticketadvantage.services.dao.email.linetracker;

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;

import com.ticketadvantage.services.dao.email.EmailProcessor;
import com.ticketadvantage.services.dao.sites.sportsinsights.SportsInsightsSite;
import com.ticketadvantage.services.email.AccessTokenFromRefreshToken;
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
public class LinetrackerEmailProcessSite extends EmailProcessor {
	private static final Logger LOGGER = Logger.getLogger(LinetrackerEmailProcessSite.class);
	private static final LinetrackerEmailParser LEP = new LinetrackerEmailParser();

	/**
	 * 
	 * @param emailaddress
	 * @param password
	 */
	public LinetrackerEmailProcessSite(String emailaddress, String password) {
		super("LinetrackerEmail", emailaddress, password);
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
		// Mail Service <infobl@alerts.cr>
		//
		if (emailContainer != null && emailContainer.getFromemail() != null) {
			String from = emailContainer.getFromemail();
			if (from.contains("<") && from.contains(">")) {
				int findex = from.indexOf("<");
				int bindex = from.indexOf(">");
				if (findex != -1 && bindex != -1) {
					from = from.substring(findex + 1, bindex);
					LOGGER.debug("From: " + from);
					if (from.equals("infobl@alerts.cr")) {
						retValue = true;
					}
				}
			} else if (emailContainer.getFromemail().equals("infobl@alerts.cr")) {
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

		// Is this a pending bet?
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
				pendingEvents = LEP.parsePendingBets(emailContainer.getBodytext(), accountname, accountid);
				if (pendingEvents != null && !pendingEvents.isEmpty()) {					
					// For loop of PendingEvent
					for (PendingEvent pe : pendingEvents) {
						// Finish the pending event
						finishPendingEvent(pe);
						
						// Setup the Inet
						emailContainer.setInet(pe.getInet());
					}
				}
			} else if (emailContainer.getBodyhtml() != null && emailContainer.getBodyhtml().length() > 0) {
				final String html = emailContainer.getBodyhtml();
				final String text = Jsoup.parse(html).text();
				pendingEvents = LEP.parsePendingBets(text, accountname, accountid);
				if (pendingEvents != null && !pendingEvents.isEmpty()) {
					// For loop of PendingEvent
					for (PendingEvent pe : pendingEvents) {
						// Finish the pending event
						finishPendingEvent(pe);
						
						// Setup the Inet
						emailContainer.setInet(pe.getInet());
					}
				}
			} else {
				pendingEvents = LEP.parsePendingBets(emailContainer.getSubject(), accountname, accountid);
				if (pendingEvents != null && !pendingEvents.isEmpty()) {
					// For loop of PendingEvent
					for (PendingEvent pe : pendingEvents) {
						// Finish the pending event
						finishPendingEvent(pe);
						
						// Setup the Inet
						emailContainer.setInet(pe.getInet());
					}
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
		if (emailContainer.getBodytext() != null && emailContainer.getBodytext().length() > 0 &&
			emailContainer.getSubject() != null && emailContainer.getSubject().length() > 0) {
			String text = emailContainer.getBodytext();
			String subject = emailContainer.getSubject();
			LOGGER.debug("text: " + text);
			LOGGER.debug("subject: " + subject);
			retValue = LEP.isPendingBet(subject, text, accountname, accountid);
		} else if (emailContainer.getBodyhtml() != null && emailContainer.getBodyhtml().length() > 0 &&
				   emailContainer.getSubject() != null && emailContainer.getSubject().length() > 0) {
			String html = emailContainer.getBodyhtml();
			String subject = emailContainer.getSubject();
			String text = Jsoup.parse(html).text();
			LOGGER.debug("text: " + text);
			retValue = LEP.isPendingBet(subject, text, accountname, accountid);
		} else {
			String subject = emailContainer.getSubject();
			LOGGER.debug("subject: " + subject);
			retValue = LEP.isPendingBet(subject, null, accountname, accountid);
		}

		LOGGER.info("Exiting determineHasPendingBets()");
		return retValue;
	}

	/**
	 * 
	 * @param pe
	 */
	private void finishPendingEvent(PendingEvent pe) {
		LOGGER.info("Entering finishPendingEvent()");

		try {
			final SportsInsightsSite processSite = new SportsInsightsSite("https://sportsinsights.actionnetwork.com/",
					"mojaxsventures@gmail.com", "action1");
			final EventPackage ep = processSite.getEventByIdNoGameSport(pe.getRotationid());

			if (ep != null) {
				// Get game type
				pe.setGametype(ep.getSporttype());
				pe.setGamedate(ep.getEventdatetime());
				pe.setEventdate(ep.getDateofevent() + " " + ep.getTimeofevent());

				// Setup the game sport
				setupGamesport(pe);
			}

			// Setup the rotation ID now
			if (pe.getLinetype().equals("first")) {
				pe.setRotationid("1" + pe.getRotationid());
			} else if (pe.getLinetype().equals("second")) {
				pe.setRotationid("2" + pe.getRotationid());
			} else if (pe.getLinetype().equals("third")) {
				pe.setRotationid("3" + pe.getRotationid());
			}
		} catch (Throwable t) {
			LOGGER.error("PendingEvent: " + pe);
			LOGGER.error(t.getMessage(), t);
		}

		LOGGER.info("Exiting finishPendingEvent()");
	}

	/**
	 * 
	 * @param pe
	 */
	private void setupGamesport(PendingEvent pe) {
		LOGGER.info("Entering setupGamesport()");

		if (pe.getGametype().equals("NFL")) {
			pe.setGamesport("Football");
		} else if (pe.getGametype().equals("NCAAF")) {
			pe.setGamesport("Football");
		} else if (pe.getGametype().equals("NBA")) {
			pe.setGamesport("Basketball");
		} else if (pe.getGametype().equals("WNBA")) {
			pe.setGamesport("Basketball");
		} else if (pe.getGametype().equals("NCAAB")) {
			pe.setGamesport("Basketball");
		} else if (pe.getGametype().equals("NHL")) {
			pe.setGamesport("Hockey");
		} else if (pe.getGametype().equals("MLB")) {
			pe.setGamesport("Baseball");
		}

		LOGGER.info("Exiting setupGamesport()");
	}
}