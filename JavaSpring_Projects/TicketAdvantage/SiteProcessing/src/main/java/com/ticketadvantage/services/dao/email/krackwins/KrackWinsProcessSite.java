package com.ticketadvantage.services.dao.email.krackwins;

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

public class KrackWinsProcessSite extends EmailProcessor {
	private static final Logger LOGGER = Logger.getLogger(KrackWinsProcessSite.class);
	private static final KrackWinsParser PP = new KrackWinsParser();

	/**
	 * 
	 */
	public KrackWinsProcessSite(String emailaddress, String password) {
		super("KrackWins", emailaddress, password);
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
		// Bill Krack <krackwins@gmail.com>
		//
		if (emailContainer != null && emailContainer.getFromemail() != null) {
			String from = emailContainer.getFromemail();
			if (from.contains("<") && from.contains(">")) {
				int findex = from.indexOf("<");
				int bindex = from.indexOf(">");
				if (findex != -1 && bindex != -1) {
					from = from.substring(findex + 1, bindex);
					if (from.equals("krackwins@gmail.com")) {
						retValue = true;
					}
				}
			} else if (emailContainer.getFromemail().equals("krackwins@gmail.com")) {
				retValue = true;				
			}
		}

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

		if (emailContainer != null) {
			if (emailContainer.getBodytext() != null && emailContainer.getBodytext().length() > 0) {
				pendingEvents = PP.parsePendingBets(emailContainer.getBodytext(), accountname, accountid);
			} else if (emailContainer.getBodyhtml() != null && emailContainer.getBodyhtml().length() > 0) {
				final String html = emailContainer.getBodyhtml();
				final String text = Jsoup.parse(html).text();
				pendingEvents = PP.parsePendingBets(text, accountname, accountid);
			} else {
				pendingEvents = PP.parsePendingBets(emailContainer.getSubject(), accountname, accountid);
			}

			if (pendingEvents != null && !pendingEvents.isEmpty()) {
				final SportsInsightsSite processSite = new SportsInsightsSite("https://sportsinsights.actionnetwork.com/",
						"mojaxsventures@gmail.com", 
						"action1");
				final Set<EventPackage> events = processSite.getAllSports().getEvents();
				final Iterator<PendingEvent> itr = pendingEvents.iterator();

				while (itr.hasNext()) {
					PendingEvent pendingEvent = itr.next();
					Iterator<EventPackage> eItr = events.iterator();
					while (eItr.hasNext()) {
						EventPackage ep = eItr.next();
						Integer rotation1 = ep.getTeamone().getId();
						Integer rotation2 = ep.getTeamtwo().getId();
						if (pendingEvent.getRotationid().equals(rotation1.toString()) || 
							pendingEvent.getRotationid().equals(rotation2.toString())) {
							if ("total".equals(pendingEvent.getEventtype())) {
								pendingEvent.setTeam(ep.getTeamone().getTeam() + "/" + ep.getTeamtwo().getTeam());
							} else {
								if (pendingEvent.getRotationid().equals(rotation1.toString())) {
									pendingEvent.setTeam(ep.getTeamone().getTeam());
								} else {
									pendingEvent.setTeam(ep.getTeamtwo().getTeam());
								}
							}
							pendingEvent.setEventdate(ep.getDateofevent());
							pendingEvent.setLinetype(ep.getLinetype());
							if ("NFL".equals(ep.getSporttype())) {
								pendingEvent.setGamesport("Football");
								pendingEvent.setGametype("NFL");
							} else if ("NCAAF".equals(ep.getSporttype())) {
								pendingEvent.setGamesport("Football");
								pendingEvent.setGametype("NCAA");
							} else if ("NBA".equals(ep.getSporttype())) {
								pendingEvent.setGamesport("Basketball");
								pendingEvent.setGametype("NBA");
							} else if ("NCAAB".equals(ep.getSporttype())) {
								pendingEvent.setGamesport("Basketball");
								pendingEvent.setGametype("NCAA");
							} else if ("WNBA".equals(ep.getSporttype())) {
								pendingEvent.setGamesport("Basketball");
								pendingEvent.setGametype("WNBA");
							} else if ("MLB".equals(ep.getSporttype())) {
								pendingEvent.setGamesport("Baseball");
								pendingEvent.setGametype("MLB");
							} else if ("NHL".equals(ep.getSporttype())) {
								pendingEvent.setGamesport("Hockey");
								pendingEvent.setGametype("NHL");								
							}
						}
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

		boolean retValue = PP.hasRotationId(emailContainer.getBodytext());
		// Check if we want to process this action
		if (emailContainer.getBodytext() != null && emailContainer.getBodytext().length() > 0) {
			String text = emailContainer.getBodytext();
			LOGGER.debug("text: " + text);
			retValue = PP.hasRotationId(text);
		} else if (emailContainer.getBodyhtml() != null && emailContainer.getBodyhtml().length() > 0) {
			String html = emailContainer.getBodyhtml();
			String text = Jsoup.parse(html).text();
			LOGGER.debug("text: " + text);
			retValue = PP.hasRotationId(text);
		} else {
			String subject = emailContainer.getSubject();
			LOGGER.debug("subject: " + subject);
			retValue = PP.hasRotationId(subject);
		}

		LOGGER.info("Exiting determineHasPendingBets()");
		return retValue;
	}
}