package com.ticketadvantage.services.dao.email;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.sites.sportsinsights.SportsInsightsSite;
import com.ticketadvantage.services.email.SendText;
import com.ticketadvantage.services.email.ticketadvantage.TicketAdvantageGmailOath;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.BaseScrapper;
import com.ticketadvantage.services.model.EmailEvent;
import com.ticketadvantage.services.model.EventPackage;
import com.ticketadvantage.services.model.PendingEvent;
import com.ticketadvantage.services.util.ParseBullshit;

/**
 * 
 * @author jmiller
 *
 */
public abstract class EmailProcessor {
	private static final Logger LOGGER = Logger.getLogger(EmailProcessor.class);
	protected final SportsInsightsSite processSite = new SportsInsightsSite("https://sportsinsights.actionnetwork.com/",
			"mojaxsventures@gmail.com", 
			"action1");
	protected String sitetype;
	protected String emailaddress;
	protected String password;
	protected String accountname;
	protected String accountid;
	protected String timezone = "ET";
	protected boolean processTransaction = true;

	/**
	 * 
	 */
	public EmailProcessor(String sitetype, String emailaddress, String password) {
		super();
		this.sitetype = sitetype;
		this.emailaddress = emailaddress;
		this.password = password;
	}

	/**
	 * 
	 * @param emailContainer
	 * @param baseScrappers
	 * @return
	 * @throws BatchException
	 */
	public abstract boolean determineEmailProcessor(EmailEvent emailContainer, List<BaseScrapper> baseScrappers) throws BatchException;

	/**
	 * 
	 * @param emailContainer
	 * @param baseScrappers
	 * @return
	 * @throws BatchException
	 */
	public abstract boolean processEmail(EmailEvent emailContainer, List<BaseScrapper> baseScrappers) throws BatchException;

	/**
	 * 
	 * @param pendingType
	 * @param emailContainer
	 * @param baseScrappers
	 * @return
	 * @throws BatchException
	 */
	public abstract Set<PendingEvent> getPendingBets(String pendingType, EmailEvent emailContainer, List<BaseScrapper> baseScrappers) throws BatchException;

	/**
	 * 
	 * @param phoneNumber
	 * @param body
	 */
	protected void sendText(String phoneNumber, String body) {
		LOGGER.info("Entering sendText()");

		try {
			final TicketAdvantageGmailOath TicketAdvantageGmailOath = new TicketAdvantageGmailOath();
			final String accessToken = TicketAdvantageGmailOath.getAccessToken();
			final SendText sendText = new SendText();
			sendText.setOAUTH2_TOKEN(accessToken);
			sendText.sendTextWithMessage(phoneNumber, body);
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		LOGGER.info("Exiting sendText()");
	}

	/**
	 * 
	 * @param pendingEvents
	 * @throws BatchException
	 */
	protected EventPackage getGameDetails(Set<PendingEvent> pendingEvents) throws BatchException {
		EventPackage ep = null;
		final Set<EventPackage> events = processSite.getAllSportsGame().getEvents();
		final Iterator<PendingEvent> itr = pendingEvents.iterator();

		while (itr.hasNext()) {
			final PendingEvent pendingEvent = itr.next();
			Iterator<EventPackage> eItr = events.iterator();
			boolean found = false;

			while (eItr.hasNext() && !found) {
				ep = eItr.next();
				final Integer rotation1 = ep.getTeamone().getId();
				final Integer rotation2 = ep.getTeamtwo().getId();
				final String rotationId = pendingEvent.getRotationid();
				LOGGER.debug("rotation1: " + rotation1);
				LOGGER.debug("rotation2: " + rotation2);
				LOGGER.debug("rotationId: " + rotationId);

				if (rotationId != null && rotationId.length() > 0 &&
					rotation1 != null && rotation2 != null &&
					(rotationId.equals(rotation1.toString()) || 
					 rotationId.equals(rotation2.toString()))) {

					if ("total".equals(pendingEvent.getEventtype())) {
						pendingEvent.setTeam(ep.getTeamone().getTeam() + "/" + ep.getTeamtwo().getTeam());
					} else {
						if (pendingEvent.getRotationid().equals(rotation1.toString())) {
							pendingEvent.setTeam(ep.getTeamone().getTeam());
						} else {
							pendingEvent.setTeam(ep.getTeamtwo().getTeam());
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

					found = true;
					LOGGER.debug("EventPackage: " + ep);
					LOGGER.debug("PendingEvent: " + pendingEvent);
				} 
			}
				
			if (!found) {
				eItr = events.iterator();
				found = false;
				while (eItr.hasNext() && !found) {
					ep = eItr.next();
					final Integer rotation1 = ep.getTeamone().getId();
					final ParseBullshit parseBullshit = new ParseBullshit();

					if (parseBullshit.findGame(ep, pendingEvent)) {
						pendingEvent.setRotationid(rotation1.toString());
						if ("total".equals(pendingEvent.getEventtype())) {
							pendingEvent.setTeam(ep.getTeamone().getTeam() + "/" + ep.getTeamtwo().getTeam());
						} else {
							if (pendingEvent.getRotationid().equals(rotation1.toString())) {
								pendingEvent.setTeam(ep.getTeamone().getTeam());
							} else {
								pendingEvent.setTeam(ep.getTeamtwo().getTeam());
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

						found = true;
						LOGGER.error("PendingEvent: " + pendingEvent);
					}
				}

				while (eItr.hasNext() && !found) {
					ep = eItr.next();
					final Integer rotation1 = ep.getTeamone().getId();
					final ParseBullshit parseBullshit = new ParseBullshit();
					if (parseBullshit.findGameShort(ep, pendingEvent)) {
						if ("total".equals(pendingEvent.getEventtype())) {
							pendingEvent.setTeam(ep.getTeamone().getTeam() + "/" + ep.getTeamtwo().getTeam());
						} else {
							if (pendingEvent.getRotationid().equals(rotation1.toString())) {
								pendingEvent.setTeam(ep.getTeamone().getTeam());
							} else {
								pendingEvent.setTeam(ep.getTeamtwo().getTeam());
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

						found = true;
						LOGGER.error("PendingEvent: " + pendingEvent);
					}
				}
			}
		}

		return ep;
	}

	/**
	 * @return the emailaddress
	 */
	public String getEmailaddress() {
		return emailaddress;
	}

	/**
	 * @param emailaddress the emailaddress to set
	 */
	public void setEmailaddress(String emailaddress) {
		this.emailaddress = emailaddress;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the sitetype
	 */
	public String getSitetype() {
		return sitetype;
	}

	/**
	 * @param sitetype the sitetype to set
	 */
	public void setSitetype(String sitetype) {
		this.sitetype = sitetype;
	}

	/**
	 * @return the accountname
	 */
	public String getAccountname() {
		return accountname;
	}

	/**
	 * @param accountname the accountname to set
	 */
	public void setAccountname(String accountname) {
		this.accountname = accountname;
	}

	/**
	 * @return the accountid
	 */
	public String getAccountid() {
		return accountid;
	}

	/**
	 * @param accountid the accountid to set
	 */
	public void setAccountid(String accountid) {
		this.accountid = accountid;
	}

	/**
	 * @return the timezone
	 */
	public String getTimezone() {
		return timezone;
	}

	/**
	 * @param timezone the timezone to set
	 */
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	/**
	 * @return the processTransaction
	 */
	public boolean isProcessTransaction() {
		return processTransaction;
	}

	/**
	 * @param processTransaction the processTransaction to set
	 */
	public void setProcessTransaction(boolean processTransaction) {
		this.processTransaction = processTransaction;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EmailProcessor [sitetype=" + sitetype + ", emailaddress=" + emailaddress + ", password=" + password
				+ ", accountname=" + accountname + ", accountid=" + accountid + ", timezone=" + timezone
				+ ", processTransaction=" + processTransaction + "]";
	}
}