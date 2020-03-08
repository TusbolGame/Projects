package com.ticketadvantage.services.dao.twitter;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.sites.sportsinsights.SportsInsightsSite;
import com.ticketadvantage.services.email.SendText;
import com.ticketadvantage.services.email.ticketadvantage.TicketAdvantageGmailOath;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.BaseScrapper;
import com.ticketadvantage.services.model.EventPackage;
import com.ticketadvantage.services.model.PendingEvent;
import com.ticketadvantage.services.model.TwitterTweet;

/**
 * 
 * @author jmiller
 *
 */
public abstract class TwitterProcessor {
	private static final Logger LOGGER = Logger.getLogger(TwitterProcessor.class);
	protected final SportsInsightsSite processSite = new SportsInsightsSite("https://sportsinsights.actionnetwork.com/",
			"mojaxsventures@gmail.com", 
			"action1");
	protected String sitetype;
	protected String screenname;
	protected String handleid;
	protected String accountname;
	protected String accountid;
	protected String timezone = "ET";
	protected boolean processTransaction = true;

	/**
	 * 
	 * @param sitetype
	 * @param inet
	 * @param customerid
	 * @param screenname
	 * @param handleid
	 */
	public TwitterProcessor(String sitetype, String inet, String customerid, String screenname, String handleid) {
		super();
		this.sitetype = sitetype;
		this.accountname = inet;
		this.accountid = customerid;
		this.screenname = screenname;
		this.handleid = handleid;
	}

	/**
	 * 
	 * @param twitterContainer
	 * @param baseScrappers
	 * @return
	 * @throws BatchException
	 */
	public abstract boolean determineTwitterProcessor(TwitterTweet twitterContainer, List<BaseScrapper> baseScrappers) throws BatchException;

	/**
	 * 
	 * @param twitterContainer
	 * @param baseScrappers
	 * @return
	 * @throws BatchException
	 */
	public abstract boolean processTweet(TwitterTweet twitterContainer, List<BaseScrapper> baseScrappers) throws BatchException;

	/**
	 * 
	 * @param pendingType
	 * @param emailContainer
	 * @param baseScrappers
	 * @return
	 * @throws BatchException
	 */
	public abstract Set<PendingEvent> getPendingBets(String pendingType, TwitterTweet twitterContainer, List<BaseScrapper> baseScrappers) throws BatchException;

	/**
	 * 
	 * @param pe
	 * @throws BatchException
	 */
	public void doProcessPendingEvent(PendingEvent pe) throws BatchException {
		// Do nothing
	}

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
	 * @param pendingEvent
	 * @return
	 * @throws BatchException
	 */
	protected boolean getGameDetails(PendingEvent pendingEvent) throws BatchException {
		LOGGER.error("PendingEvent: " + pendingEvent);
		EventPackage ep = null;
		final Set<EventPackage> events = processSite.getAllSportsGame().getEvents();
		Iterator<EventPackage> eItr = events.iterator();
		boolean found = false;

		// Rotation # search
		while (eItr.hasNext() && !found) {
			ep = eItr.next();
			final String team1 = ep.getTeamone().getTeam().toLowerCase();
			final String team2 = ep.getTeamtwo().getTeam().toLowerCase();
			final String team = pendingEvent.getTeam().toLowerCase();
			LOGGER.debug("team1x: " + team1);
			LOGGER.debug("team2x: " + team2);
			LOGGER.debug("teamx: " + team);

			if (pendingEvent.getGametype() != null && 
				pendingEvent.getGametype().equals(ep.getSporttype())) {
				if (pendingEvent.getRotationid() != null) {
					final Integer rotId = Integer.parseInt(pendingEvent.getRotationid());

					if (ep.getTeamone().getId().intValue() == rotId.intValue() ||
						ep.getTeamtwo().getId().intValue() == rotId.intValue()) {
						// Try to match the game
						found = findGame(team, team1, team2, pendingEvent, ep);
					}
				}
			} else if (pendingEvent.getGametype() == null) {
				if (pendingEvent.getRotationid() != null) {
					final Integer rotId = Integer.parseInt(pendingEvent.getRotationid());

					if (ep.getTeamone().getId().intValue() == rotId.intValue() ||
						ep.getTeamtwo().getId().intValue() == rotId.intValue()) {
						// Try to match the game
						found = findGame(team, team1, team2, pendingEvent, ep);
					}
				}
			}
		}

		// Full Team Name Exact
		while (eItr.hasNext() && !found) {
			ep = eItr.next();
			final String team1 = ep.getTeamone().getTeam().toLowerCase();
			final String team2 = ep.getTeamtwo().getTeam().toLowerCase();
			final String team = pendingEvent.getTeam().toLowerCase();
			LOGGER.debug("team1xx: " + team1);
			LOGGER.debug("team2xx: " + team2);
			LOGGER.debug("teamxx: " + team);

			if (pendingEvent.getGametype() != null && 
				pendingEvent.getGametype().equals(ep.getSporttype())) {
				if (team != null && team.length() > 0 && 
					team1 != null && team1.length() > 0 && 
					team2 != null && team2.length() > 0) {
					if (team1.toLowerCase().equals(team.toLowerCase()) || 
						team2.toLowerCase().equals(team.toLowerCase())) {
						// Try to match the game
						found = findGame(team, team1, team2, pendingEvent, ep);
					}
				}
			} else if (pendingEvent.getGametype() == null) {
				if (team != null && team.length() > 0 && 
					team1 != null && team1.length() > 0 && 
					team2 != null && team2.length() > 0) {
					if (team1.toLowerCase().equals(team.toLowerCase()) || 
						team2.toLowerCase().equals(team.toLowerCase())) {
						// Try to match the game
						found = findGame(team, team1, team2, pendingEvent, ep);
					}
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
			LOGGER.debug("team1xxx: " + team1);
			LOGGER.debug("team2xxx: " + team2);
			LOGGER.debug("teamxxx: " + team);

			if (pendingEvent.getGametype() != null && 
				pendingEvent.getGametype().equals(ep.getSporttype())) {
				if (team != null && team.length() > 0 && 
					team1 != null && team1.length() > 0 && 
					team2 != null && team2.length() > 0) {
					if (team1.toLowerCase().contains(team.toLowerCase()) || 
						team2.toLowerCase().contains(team.toLowerCase())) {
						// Try to match the game
						found = findGame(team, team1, team2, pendingEvent, ep);
					}
				}
			} else if (pendingEvent.getGametype() == null) {
				if (team != null && team.length() > 0 && 
					team1 != null && team1.length() > 0 && 
					team2 != null && team2.length() > 0) {
					if (team1.toLowerCase().contains(team.toLowerCase()) || 
						team2.toLowerCase().contains(team.toLowerCase())) {
						// Try to match the game
						found = findGame(team, team1, team2, pendingEvent, ep);
					}
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
			LOGGER.debug("team1xxxx: " + team1);
			LOGGER.debug("team2xxxx: " + team2);
			LOGGER.debug("teamxxxx: " + team);

			if (pendingEvent.getGametype() != null && 
				pendingEvent.getGametype().equals(ep.getSporttype())) {
				if (team != null && team.length() > 0 && 
					team1 != null && team1.length() > 0 && 
					team2 != null && team2.length() > 0) {
					final StringTokenizer st = new StringTokenizer(team, " ");

					while (st.hasMoreElements()) {
						final String partialTeam = (String) st.nextElement();
						LOGGER.debug("partialTeamyy: " + partialTeam);
						LOGGER.debug("team1yy: " + partialTeam);
						LOGGER.debug("team2yy: " + partialTeam);

						if (partialTeam.length() > 2 && (team1.toLowerCase().contains(partialTeam.toLowerCase()) || 
							team2.toLowerCase().contains(partialTeam.toLowerCase()))) {
							// Try to match the game
							found = findGame(partialTeam, team1, team2, pendingEvent, ep);
						}
					}
				}
			} else if (pendingEvent.getGametype() == null) {
				if (team != null && team.length() > 0 && 
					team1 != null && team1.length() > 0 && 
					team2 != null && team2.length() > 0) {
					final StringTokenizer st = new StringTokenizer(team, " ");
					while (st.hasMoreElements()) {
						final String partialTeam = (String) st.nextElement();

						if (partialTeam.length() > 2 && (team1.toLowerCase().contains(partialTeam.toLowerCase()) || 
							team2.toLowerCase().contains(partialTeam.toLowerCase()))) {
							// Try to match the game
							found = findGame(partialTeam, team1, team2, pendingEvent, ep);
						}
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
			LOGGER.debug("team1xxxxx: " + team1);
			LOGGER.debug("team2xxxxx: " + team2);
			LOGGER.debug("teamxxxxx: " + team);

			if (pendingEvent.getGametype() != null && 
				pendingEvent.getGametype().equals(ep.getSporttype())) {
				if (team != null && team.length() > 0 && 
					team1 != null && team1.length() > 0 && 
					team2 != null && team2.length() > 0) {
					final StringTokenizer st = new StringTokenizer(team, " ");
	
					while (st.hasMoreElements()) {
						String partialTeam = (String)st.nextElement();
	
						if (partialTeam != null && partialTeam.length() > 2) {
							partialTeam = partialTeam.substring(0, 3);
							LOGGER.debug("partialTeam: " + partialTeam);
							LOGGER.debug("team1xxx: " + partialTeam);
							LOGGER.debug("team2xxx: " + partialTeam);
	
							if (team1.toLowerCase().contains(partialTeam.toLowerCase()) || 
								team2.toLowerCase().contains(partialTeam.toLowerCase())) {
								// Try to match the game
								found = findGame(partialTeam, team1, team2, pendingEvent, ep);
							}
						}
					}
				}
			} else if (pendingEvent.getGametype() == null) {
				if (team != null && team.length() > 0 && 
					team1 != null && team1.length() > 0 && team2 != null && team2.length() > 0) {
					final StringTokenizer st = new StringTokenizer(team, " ");

					while (st.hasMoreElements()) {
						String partialTeam = (String) st.nextElement();

						if (partialTeam != null && partialTeam.length() > 2) {
							partialTeam = partialTeam.substring(0, 3);
							LOGGER.debug("partialTeam: " + partialTeam);
							LOGGER.debug("team1xxx: " + partialTeam);
							LOGGER.debug("team2xxx: " + partialTeam);

							if (team1.toLowerCase().contains(partialTeam.toLowerCase())
									|| team2.toLowerCase().contains(partialTeam.toLowerCase())) {
								// Try to match the game
								found = findGame(partialTeam, team1, team2, pendingEvent, ep);
							}
						}
					}
				}
			}
		}

		return found;
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
	protected boolean findGame(String team, String team1, String team2, PendingEvent pendingEvent, EventPackage ep) {
		boolean found = false;
		LOGGER.debug("PendingEvent: " + pendingEvent);
		LOGGER.debug("EventPackage: " + ep);

		if (pendingEvent.getGametype() != null && 
			pendingEvent.getGametype().equals(ep.getSporttype())) {
			final Calendar now = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
			now.add(Calendar.DATE, 1);
			now.set(Calendar.HOUR, 23);
			now.set(Calendar.MINUTE, 59);
			now.set(Calendar.SECOND, 59);
			
			final Calendar gamedate = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
			final Date edatetime = ep.getEventdatetime();
			gamedate.setTime(edatetime);
			final Date nowdate = now.getTime();
			LOGGER.debug("edatetime: " + edatetime);
			LOGGER.debug("nowdate: " + nowdate);

			if (edatetime.before(nowdate)) {
				if ("total".equals(pendingEvent.getEventtype())) {
					pendingEvent.setTeam(ep.getTeamone().getTeam() + "/" + ep.getTeamtwo().getTeam());
					pendingEvent.setRotationid(ep.getTeamone().getEventid());
				} else {
					if (pendingEvent.getRotationid() != null) {
						final Integer rotId = Integer.parseInt(pendingEvent.getRotationid());
						if (ep.getTeamone().getId().intValue() == rotId.intValue()) {
							pendingEvent.setTeam(ep.getTeamone().getTeam());
							pendingEvent.setRotationid(ep.getTeamone().getEventid());
						} else if (ep.getTeamtwo().getId().intValue() == rotId.intValue()) {
							pendingEvent.setTeam(ep.getTeamtwo().getTeam());
							pendingEvent.setRotationid(ep.getTeamtwo().getEventid());
						}
					} else {
						if (team1.toLowerCase().contains(team.toLowerCase())) {
							pendingEvent.setTeam(ep.getTeamone().getTeam());
							pendingEvent.setRotationid(ep.getTeamone().getEventid());
						} else {
							pendingEvent.setTeam(ep.getTeamtwo().getTeam());
							pendingEvent.setRotationid(ep.getTeamtwo().getEventid());
						}
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
				LOGGER.debug("PendingEvent: " + pendingEvent);
			} else {
				pendingEvent.setGamesport(null);
				pendingEvent.setGametype(null);
			}
		} else if (pendingEvent.getGametype() == null) {
			final Calendar now = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
			now.add(Calendar.DATE, 1);
			now.set(Calendar.HOUR, 23);
			now.set(Calendar.MINUTE, 59);
			now.set(Calendar.SECOND, 59);
			final Calendar gamedate = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
			final Date edatetime = ep.getEventdatetime();
			gamedate.setTime(edatetime);
			final Date nowdate = now.getTime();
			LOGGER.debug("edatetime: " + edatetime);
			LOGGER.debug("nowdate: " + nowdate);

			if (edatetime.before(nowdate)) {
				if ("total".equals(pendingEvent.getEventtype())) {
					pendingEvent.setTeam(ep.getTeamone().getTeam() + "/" + ep.getTeamtwo().getTeam());
					pendingEvent.setRotationid(ep.getTeamone().getEventid());
				} else {
					if (pendingEvent.getRotationid() != null) {
						final Integer rotId = Integer.parseInt(pendingEvent.getRotationid());
						if (ep.getTeamone().getId().intValue() == rotId.intValue()) {
							pendingEvent.setTeam(ep.getTeamone().getTeam());
							pendingEvent.setRotationid(ep.getTeamone().getEventid());
						} else if (ep.getTeamtwo().getId().intValue() == rotId.intValue()) {
							pendingEvent.setTeam(ep.getTeamtwo().getTeam());
							pendingEvent.setRotationid(ep.getTeamtwo().getEventid());
						}
					} else {
						if (team1.toLowerCase().contains(team.toLowerCase())) {
							pendingEvent.setTeam(ep.getTeamone().getTeam());
							pendingEvent.setRotationid(ep.getTeamone().getEventid());
						} else {
							pendingEvent.setTeam(ep.getTeamtwo().getTeam());
							pendingEvent.setRotationid(ep.getTeamtwo().getEventid());
						}
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
				LOGGER.debug("PendingEvent: " + pendingEvent);
			} else {
				pendingEvent.setGamesport(null);
				pendingEvent.setGametype(null);
			}
		}

		return found;
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
	 * @return the screenname
	 */
	public String getScreenname() {
		return screenname;
	}

	/**
	 * @param screenname the screenname to set
	 */
	public void setScreenname(String screenname) {
		this.screenname = screenname;
	}

	/**
	 * @return the handleid
	 */
	public String getHandleid() {
		return handleid;
	}

	/**
	 * @param handleid the handleid to set
	 */
	public void setHandleid(String handleid) {
		this.handleid = handleid;
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
	 * @return the processSite
	 */
	public SportsInsightsSite getProcessSite() {
		return processSite;
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
		return "TwitterProcessor [processSite=" + processSite + ", sitetype=" + sitetype + ", screenname=" + screenname
				+ ", handleid=" + handleid + ", accountname=" + accountname + ", accountid=" + accountid + ", timezone="
				+ timezone + ", processTransaction=" + processTransaction + "]";
	}
}