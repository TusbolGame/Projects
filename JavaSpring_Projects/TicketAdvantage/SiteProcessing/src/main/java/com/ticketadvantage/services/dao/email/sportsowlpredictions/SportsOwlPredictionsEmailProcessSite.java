/**
 * 
 */
package com.ticketadvantage.services.dao.email.sportsowlpredictions;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;

import com.ticketadvantage.services.dao.email.EmailProcessor;
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
public class SportsOwlPredictionsEmailProcessSite extends EmailProcessor {
	private static final Logger LOGGER = Logger.getLogger(SportsOwlPredictionsEmailProcessSite.class);
	private static final SportsOwlPredictionsEmailParser PP = new SportsOwlPredictionsEmailParser();

	/**
	 * 
	 */
	public SportsOwlPredictionsEmailProcessSite(String emailaddress, String password) {
		super("SportsOwlPredictionsEmail", emailaddress, password);
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final SportsOwlPredictionsEmailProcessSite sportsOwlPredictionsEmailProcessSite = new SportsOwlPredictionsEmailProcessSite("ticketadvantage@gmail.com", "ticket123");
			final EmailEvent emailContainer = new EmailEvent();
			emailContainer.setBccemail("");
			emailContainer.setBodyhtml("");
			emailContainer.setBodytext("Model 12/28\r\n" + 
					"FG:\r\n" + 
					"MIN -9.5 3.8u\r\n" + 
					"Most platforms love the lakers and our model does as well; however, I'm not sure how much ESPN, 538 and OddsShark are taking the Lebron and Rondo injury into account. The Clippers have only covered -5.5 twice in their last 13 games including 2 in their last 3.  Lakers have covered +5.5 11 of their last 15. The Lakers looked good without Lebron against the warriors and kings, so I am sending the Lakers game as a play today.  These are the units used by the model, as a reminder it is always better to be cautious than over aggressive. We never recommend changing your unit size, however with the amount of potential risk on this play you may want to lower the number of units or alter your unit size. Caution is never a bad thing.\r\n" + 
					"LAL +5.5 11.5u\r\n" + 
					"LAL ML +180 risking 4.8u\r\n" + 
					"\r\n" + 
					"On Wed, Dec 26, 2018 at 8:23 AM Sports Owl <sportsowlpredictions@gmail.com> wrote:");
			emailContainer.setDatecreated(new Date());
			emailContainer.setDatereceived(new Date());
			emailContainer.setDatesent(new Date());
			emailContainer.setEmailname("Sports Owl");
			emailContainer.setFromemail("sportsowlpredictions@gmail.com");
			emailContainer.setInet("Sports Owl");
			emailContainer.setMessagenum(1);
			emailContainer.setSubject("");
			emailContainer.setToemail("ticketadvantage@gmail.com");
			final Set<PendingEvent> pendingEvents = sportsOwlPredictionsEmailProcessSite.getPendingBets("user", emailContainer, null);

			if (pendingEvents != null && !pendingEvents.isEmpty()) {
				final Iterator<PendingEvent> itr = pendingEvents.iterator();

				while (itr != null && itr.hasNext()) {
					final PendingEvent pendingEvent = itr.next();
					LOGGER.error("PendingEvent: " + pendingEvent);
				}
			}
		} catch (BatchException be) {
			LOGGER.error(be.getMessage(), be);
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
		// Mail Service <cogservice@cog.cr>
		//
		if (emailContainer != null && emailContainer.getFromemail() != null) {
			String from = emailContainer.getFromemail();
			if (from.contains("<") && from.contains(">")) {
				int findex = from.indexOf("<");
				int bindex = from.indexOf(">");
				if (findex != -1 && bindex != -1) {
					from = from.substring(findex + 1, bindex);
					LOGGER.debug("From: " + from);
					if (from.equals("sportsowlpredictions@gmail.com") || from.equals("shootfromanywhere@gmail.com")) {
						retValue = true;
					}
				}
			} else if (emailContainer.getFromemail().equals("sportsowlpredictions@gmail.com") || emailContainer.getFromemail().equals("shootfromanywhere@gmail.com")) {
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
					emailContainer.setInet(pendingEvents.iterator().next().getInet());
				}
			} else if (emailContainer.getBodyhtml() != null && emailContainer.getBodyhtml().length() > 0) {
				final String html = emailContainer.getBodyhtml();
				final String text = Jsoup.parse(html).text();
				pendingEvents = PP.parsePendingBets(text, accountname, accountid);
				if (pendingEvents != null && !pendingEvents.isEmpty()) {
					emailContainer.setInet(pendingEvents.iterator().next().getInet());
				}
			} else {
				pendingEvents = PP.parsePendingBets(emailContainer.getSubject(), accountname, accountid);
				if (pendingEvents != null && !pendingEvents.isEmpty()) {
					emailContainer.setInet(pendingEvents.iterator().next().getInet());
				}
			}
			LOGGER.debug("pendingEvents: " + pendingEvents);

			if (pendingEvents != null && !pendingEvents.isEmpty()) {
				// Get the game details
				getGameDetails(pendingEvents);
			}
		}

		LOGGER.info("Exiting getPendingBets()");
		return pendingEvents;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.email.EmailProcessor#getGameDetails(java.util.Set)
	 */
	@Override
	protected EventPackage getGameDetails(Set<PendingEvent> pendingEvents) throws BatchException {
		EventPackage ep = null;
		final Set<EventPackage> events = processSite.getAllSportsGame().getEvents();
		final Iterator<PendingEvent> itr = pendingEvents.iterator();

		// Loop through all the games
		while (itr.hasNext()) {
			final PendingEvent pendingEvent = itr.next();
			Iterator<EventPackage> eItr = events.iterator();
			boolean found = false;

			// Full Team Name Exact
			while (eItr.hasNext() && !found) {
				ep = eItr.next();
				final String team1 = ep.getTeamone().getTeam();
				final String team2 = ep.getTeamtwo().getTeam();
				final String team = pendingEvent.getTeam();
				LOGGER.debug("team1: " + team1);
				LOGGER.debug("team2: " + team2);
				LOGGER.debug("team: " + team);

				if (team != null && team.length() > 0 &&
					team1 != null && team1.length() > 0 &&
					team2 != null && team2.length() > 0) {

					if (team1.toLowerCase().equals(team) || 
						team2.toLowerCase().equals(team)) {
						// Try to match the game
						found = findGame(team, team1, team2, pendingEvent, ep);
					}
				} 
			}

			// Full Team Name
			eItr = events.iterator();
			while (eItr.hasNext() && !found) {
				ep = eItr.next();
				final String team1 = ep.getTeamone().getTeam();
				final String team2 = ep.getTeamtwo().getTeam();
				final String team = pendingEvent.getTeam();
				LOGGER.debug("team1: " + team1);
				LOGGER.debug("team2: " + team2);
				LOGGER.debug("team: " + team);

				if (team != null && team.length() > 0 &&
					team1 != null && team1.length() > 0 &&
					team2 != null && team2.length() > 0) {

					if (team1.toLowerCase().contains(team) || 
						team2.toLowerCase().contains(team)) {
						// Try to match the game
						found = findGame(team, team1, team2, pendingEvent, ep);
					}
				} 
			}

			// Partial Team Name
			eItr = events.iterator();
			while (eItr.hasNext() && !found) {
				ep = eItr.next();
				final String team1 = ep.getTeamone().getTeam();
				final String team2 = ep.getTeamtwo().getTeam();
				final String team = pendingEvent.getTeam();
				LOGGER.debug("team1: " + team1);
				LOGGER.debug("team2: " + team2);
				LOGGER.debug("team: " + team);

				if (team != null && team.length() > 0 &&
					team1 != null && team1.length() > 0 &&
					team2 != null && team2.length() > 0) {
					
					final StringTokenizer st = new StringTokenizer(team, " ");
					while (st.hasMoreElements()) {
						final String partialTeam = (String)st.nextElement();
						if (team1.toLowerCase().contains(partialTeam) || 
							team2.toLowerCase().contains(partialTeam)) {
							// Try to match the game
							found = findGame(partialTeam, team1, team2, pendingEvent, ep);
						}
					}
				} 
			}

			// First Three Letters Partial Team Name
			eItr = events.iterator();
			while (eItr.hasNext() && !found) {
				ep = eItr.next();
				final String team1 = ep.getTeamone().getTeam();
				final String team2 = ep.getTeamtwo().getTeam();
				final String team = pendingEvent.getTeam();
				LOGGER.debug("team1: " + team1);
				LOGGER.debug("team2: " + team2);
				LOGGER.debug("team: " + team);

				if (team != null && team.length() > 0 &&
					team1 != null && team1.length() > 0 &&
					team2 != null && team2.length() > 0) {
					
					final StringTokenizer st = new StringTokenizer(team, " ");
					while (st.hasMoreElements()) {
						String partialTeam = (String)st.nextElement();
						partialTeam = partialTeam.substring(0, 3);
						if (team1.toLowerCase().contains(partialTeam) || 
							team2.toLowerCase().contains(partialTeam)) {
							// Try to match the game
							found = findGame(partialTeam, team1, team2, pendingEvent, ep);
						}
					}
				} 
			}
		}

		return ep;
	}

	/**
	 * 
	 * @param team
	 * @param team1
	 * @param team2
	 * @param pendingEvent
	 * @param ep
	 * @return
	 */
	private boolean findGame(String team, String team1, String team2, PendingEvent pendingEvent, EventPackage ep) {
		boolean found = false;

		if (pendingEvent.getGametype().equals(ep.getSporttype())) {
			if ("total".equals(pendingEvent.getEventtype())) {
				pendingEvent.setTeam(ep.getTeamone().getTeam() + "/" + ep.getTeamtwo().getTeam());
				pendingEvent.setRotationid(ep.getTeamone().getEventid());
			} else {
				if (team1.toLowerCase().contains(team)) {
					pendingEvent.setTeam(ep.getTeamone().getTeam());
					pendingEvent.setRotationid(ep.getTeamone().getEventid());
				} else {
					pendingEvent.setTeam(ep.getTeamtwo().getTeam());
					pendingEvent.setRotationid(ep.getTeamtwo().getEventid());
				}
			}

			pendingEvent.setGamedate(ep.getEventdatetime());
			pendingEvent.setEventdate(ep.getDateofevent() + " " + ep.getTimeofevent());

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

			if (pendingEvent.getLinetype().equals("first")) {
				pendingEvent.setRotationid("1" + pendingEvent.getRotationid());
			} else if (pendingEvent.getLinetype().equals("second")) {
				pendingEvent.setRotationid("2" + pendingEvent.getRotationid());
			} else if (pendingEvent.getLinetype().equals("third")) {
				pendingEvent.setRotationid("3" + pendingEvent.getRotationid());
			}

			pendingEvent.setTicketnum(Long.toString(System.currentTimeMillis()));

			found = true;
			LOGGER.error("PendingEvent: " + pendingEvent);
		}

		return found;
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
		if (emailContainer.getBodytext() != null && emailContainer.getBodytext().length() > 0) {
			String text = emailContainer.getBodytext();
			LOGGER.debug("text: " + text);
			retValue = PP.isPendingBet(text, accountname, accountid);
		} else if (emailContainer.getBodyhtml() != null && emailContainer.getBodyhtml().length() > 0) {
			String html = emailContainer.getBodyhtml();
			String text = Jsoup.parse(html).text();
			LOGGER.debug("text: " + text);
			retValue = PP.isPendingBet(text, accountname, accountid);
		} else {
			String subject = emailContainer.getSubject();
			LOGGER.debug("subject: " + subject);
			retValue = PP.isPendingBet(subject, accountname, accountid);
		}

		LOGGER.info("Exiting determineHasPendingBets()");
		return retValue;
	}
}