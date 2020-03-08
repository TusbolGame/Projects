/**
 * 
 */
package com.ticketadvantage.services.dao.sites.pinnacleagent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.sites.SiteProcessor;
import com.ticketadvantage.services.dao.sites.SiteTransaction;
import com.ticketadvantage.services.dao.sites.sportsinsights.SportsInsightsSite;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.AccountEvent;
import com.ticketadvantage.services.model.BaseRecordEvent;
import com.ticketadvantage.services.model.EventPackage;
import com.ticketadvantage.services.model.EventsPackage;
import com.ticketadvantage.services.model.PendingEvent;
import com.ticketadvantage.services.model.TeamPackage;
import com.ticketadvantage.services.util.ParseBullshit;

/**
 * @author jmiller
 *
 */
public class PinnacleAgentProcessSite extends SiteProcessor {
	private static final Logger LOGGER = Logger.getLogger(PinnacleAgentProcessSite.class);
	protected final PinnacleAgentParser PAP = new PinnacleAgentParser();
	private static final SportsInsightsSite SportsInsightSite = new SportsInsightsSite("https://sportsinsights.actionnetwork.com", "mojaxsventures@gmail.com", "action1");	// 4/26/2018 6:05:38 PM PDT
	protected static final ThreadLocal<SimpleDateFormat> PENDING_DATE_FORMAT = new ThreadLocal<SimpleDateFormat>(){
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a z");
		}
	};
	private static EventsPackage EventsPackage;
	private boolean resetConnection;
	private boolean firstTime = true;
	private Set<EventPackage> events;
	private int xpike;
	private int xpike1;
	private int xpike4;
	private int xpike5;
	private int xpike6;

//	protected static String[] XPIKE_WOO_PLAYERS = new String[] { "52037", "52002", "52048", "52063", "52065", "52509" };
//	protected static String[] XPIKE1_WOO_PLAYERS = new String[] { "58009", "54020", "58065", "58015", "52029" };
//	protected static String[] XPIKE1_WOO_PLAYERS = new String[] { "52004", "58009", "54020", "58065", "58015", "52029" };
//	protected static String[] XPIKE4_WOO_PLAYERS = new String[] { "54014", "54040", "58057", "58059" };
//	protected static String[] XPIKE5_WOO_PLAYERS = new String[] { "54028", "54040", "55101", "55103", "55104" };
//	protected static String[] XPIKE6_WOO_PLAYERS = new String[] { "54032", "54038", "54044" };
	protected static String[][] XPIKE_WOO_PLAYERS = new String[][] { };
	protected static String[][] XPIKE1_WOO_PLAYERS = new String[][] { };
	protected static String[][] XPIKE4_WOO_PLAYERS = new String[][] { };
	protected static String[][] XPIKE5_WOO_PLAYERS = new String[][] { };
	protected static String[][] XPIKE6_WOO_PLAYERS = new String[][] { };

	protected static String[][] XPIKE_INDI_PLAYERS = new String[][] {
		{"52026", "0", "500"},
		{"52065", "0", "500"}
	};
	protected static String[][] XPIKE1_INDI_PLAYERS = new String[][] {
		{"58009", "1000", "500"}
	};
	protected static String[][] XPIKE4_INDI_PLAYERS = new String[][] {
		{"58026", "0", "500"},
		{"58058", "0", "500"}
	};
	protected static String[][] XPIKE5_INDI_PLAYERS = new String[][] { };
	protected static String[][] XPIKE6_INDI_PLAYERS = new String[][] { };

	static {
		try {
			EventsPackage = SportsInsightSite.getNextDaySport("mlblines");
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}
	}

	/**
	 * 
	 * @param host
	 * @param username
	 * @param password
	 * @param isMobile
	 * @param showRequestResponse
	 */
	public PinnacleAgentProcessSite(String host, String username, String password, boolean isMobile, boolean showRequestResponse) {
		super("PinnacleAgent", host, username, password, isMobile, showRequestResponse);
		LOGGER.info("Entering PinnacleAgentProcessSite()");

		// Setup the parser
		this.siteParser = PAP;

		// Menu items
		NFL_LINES_SPORT = new String[] { "FOOTBALL GAME LINES" };
		NFL_LINES_NAME = new String[] { "NFL" };
		NFL_FIRST_SPORT = new String[] { "FOOTBALL HALFTIMES" };
		NFL_FIRST_NAME = new String[] { "NFL -  FIRST HALF LINES" };
		NFL_SECOND_SPORT = new String[] { "FOOTBALL HALFTIMES" };
		NFL_SECOND_NAME = new String[] { "NFL -  SECOND HALF LINES" };
		NCAAF_LINES_SPORT = new String[] { "FOOTBALL GAME LINES" };
		NCAAF_LINES_NAME = new String[] { "NCAA FOOTBALL" };
		NCAAF_FIRST_SPORT = new String[] { "FOOTBALL HALFTIMES" };
		NCAAF_FIRST_NAME = new String[] { "NCAA FOOTBALL - (1H)" };
		NCAAF_SECOND_SPORT = new String[] { "FOOTBALL HALFTIMES" };
		NCAAF_SECOND_NAME = new String[] { "NCAA FOOTBALL - (2H)" };
		NBA_LINES_SPORT = new String[] { "NBA" };
		NBA_LINES_NAME = new String[] { "NBA - GAME LINES" };
		NBA_FIRST_SPORT = new String[] { "NBA" };
		NBA_FIRST_NAME = new String[] { "NBA - 1ST HALF LINES" };
		NBA_SECOND_SPORT = new String[] { "NBA" };
		NBA_SECOND_NAME = new String[] { "NBA - 2ND HALF LINES" };
		NCAAB_LINES_SPORT = new String[] { "CBB" };
		NCAAB_LINES_NAME = new String[] { "COLLEGE BASKETBALL - GAME LINES" };
		NCAAB_FIRST_SPORT = new String[] { "CBB" };
		NCAAB_FIRST_NAME = new String[] { "1H COLLEGE BASKETBALL" };
		NCAAB_SECOND_SPORT = new String[] { "CBB" };
		NCAAB_SECOND_NAME = new String[] { "2H COLLEGE BASKETBALL" };
		NHL_LINES_SPORT = new String[] { "NHL" };
		NHL_LINES_NAME = new String[] { "NHL - GAME LINES" };
		NHL_FIRST_SPORT = new String[] { "NHL" };
		NHL_FIRST_NAME = new String[] { "NHL - 1ST PERIOD LINES" };
		NHL_SECOND_SPORT = new String[] { "NHL" };
		NHL_SECOND_NAME = new String[] { "NHL - 2ND PERIOD LINES" };
		WNBA_LINES_NAME = new String[] { "WNBA" };
		WNBA_LINES_SPORT = new String[] { "Basketball" };
		WNBA_FIRST_NAME = new String[] { "WNBA 1st Half" };
		WNBA_FIRST_SPORT = new String[] { "Basketball" };
		WNBA_SECOND_NAME = new String[] { "WNBA 2nd Half" };
		WNBA_SECOND_SPORT = new String[] { "Basketball" };
		MLB_LINES_SPORT = new String[] { "MLB" };
		MLB_LINES_NAME = new String[] { "MLB - GAME LINES" };
		MLB_FIRST_SPORT = new String[] { "MLB" };
		MLB_FIRST_NAME = new String[] { "MLB - 1ST 5 FULL INNINGS" };
		MLB_SECOND_SPORT = new String[] { "MLB" };
		MLB_SECOND_NAME = new String[] { "" };

		LOGGER.info("Exiting PinnacleAgentProcessSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final Date dateAccepted = PENDING_DATE_FORMAT.get().parse("4/26/2018 6:05:38 PM" + " PDT");
			LOGGER.error("dateAccepted: " + dateAccepted);

			final String[][] TDSITES = new String [][] { 
				{ "https://agent.pinnaclesports.com/", "Mstrp", "mstrp12345", "0", "0", "0", "Costa Rica", "ET"}
			};

			// Loop through the sites
			for (int i = 0; i < TDSITES.length; i++) {
				final PinnacleAgentProcessSite processSite = new PinnacleAgentProcessSite(TDSITES[i][0], TDSITES[i][1],
						TDSITES[i][2], false, true);
			    processSite.httpClientWrapper.setupHttpClient(TDSITES[0][6]);
			    processSite.processTransaction = false;
			    processSite.timezone = TDSITES[0][7];

			    String xhtml = processSite.loginToSite(TDSITES[i][1], TDSITES[i][2]);
			    
			    processSite.firstTime = false;
			    processSite.xpike4 = 44;
			    
			    Set<PendingEvent> pendingWagers = processSite.getPendingBets("Pinny", "Pinny", null);
			    if (pendingWagers != null && pendingWagers.size() > 0) {
			    		for (PendingEvent pe : pendingWagers) {
			    			if (pe.getLinetype() != null && pe.getEventtype() != null) {
				    			LOGGER.error("PendingEvent: " + pe);			    				
			    			}
			    		}
			    }
			}

/*
			    // String[] PLAYER_LIST = processSite.mergeArrays2(PinnacleAgentProcessSite.XPIKE5_WOO_PLAYERS, PinnacleAgentProcessSite.XPIKE5_INDI_PLAYERS);
			    String[] PLAYER_LIST = PinnacleAgentProcessSite.XPIKE5_WOO_PLAYERS;

				// setup the data
				List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
				processSite.MAP_DATA.put("__EVENTTARGET", "ddlAgentGroup");
				processSite.MAP_DATA.put("ddlAgentGroup", "xPIKE5");
				processSite.MAP_DATA.put("ddlContests", "");
				processSite.MAP_DATA.put("ddlGames", "");
				processSite.MAP_DATA.put("ddlRaces", "");
				processSite.MAP_DATA.put("txtTicket", "");
				processSite.setupNameValuesEmpty(postValuePairs, processSite.MAP_DATA, null);
		
				// Post to site page
				xhtml = processSite.postSite("https://agent.pinnaclesports.com/BetsHistory.aspx", postValuePairs);

				Set<PendingEvent> pendingWagers = processSite.PAP.parsePendingBets(xhtml, TDSITES[i][1], TDSITES[i][2], processSite.MAP_DATA, PLAYER_LIST);
			    if (pendingWagers != null && pendingWagers.size() > 0) {
			    		for (PendingEvent pe : pendingWagers) {
			    			if (pe.getLinetype() != null && pe.getEventtype() != null) {
				    			LOGGER.error("PendingEvent: " + pe);			    				
			    			}

			    		}
			    }
			}
*/
		} catch (Throwable t) {
			LOGGER.info("Throwable: ", t);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#loginToSite(java.lang.String, java.lang.String)
	 */
	@Override
	public String loginToSite(String username, String password) throws BatchException {
		LOGGER.info("Entering loginToSite()");
		LOGGER.debug("username: " + username);
		LOGGER.debug("password: " + password);

		// Setup the timezone
		this.siteParser = PAP;
		this.siteParser.setTimezone(timezone);
		PAP.setTimezone(timezone);

		this.httpClientWrapper.setHost("https://agent.pinnaclesports.com/");
		String xhtml = getSite("https://agent.pinnaclesports.com/");
		MAP_DATA = PAP.parseIndex(xhtml);
		MAP_DATA.put("txtbxAgentID", username);
		MAP_DATA.put("txtbxPassword", password);
		final List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
		setupNameValuesEmpty(postValuePairs, MAP_DATA, "");

		// Login
		xhtml = postSite("https://agent.pinnaclesports.com/AgentLogin.aspx", postValuePairs);
		LOGGER.debug("HTML: " + xhtml);

		// Get the betting pages
		xhtml = getSite("https://agent.pinnaclesports.com/BetsHistory.aspx");
		LOGGER.debug("HTML: " + xhtml);

		// Parse quick
		PAP.parsePendingBets(xhtml, "1", "2", MAP_DATA, XPIKE_INDI_PLAYERS, XPIKE_WOO_PLAYERS);

		// Get all the events for the day
		events = SportsInsightSite.getAllSportsGame().getEvents();

		LOGGER.info("Exiting loginToSite()");
		return xhtml;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#getPendingBets(java.lang.String, java.lang.String, java.lang.Object)
	 */
	@Override
	public Set<PendingEvent> getPendingBets(String accountName, String accountId, Object anythingObject) throws BatchException {
		LOGGER.info("Entering getPendingBets()");
		LOGGER.debug("accountName: " + accountName);
		LOGGER.debug("accountId: " + accountId);
		Set<PendingEvent> pendingWagers = null;
		String ddlAgentGroup = "";
		String[][] INDI_LIST = null;
		String[][] WOO_LIST = null;

		// Get time
		final Calendar date = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
		int hour = date.get(Calendar.HOUR_OF_DAY);
		int minute = date.get(Calendar.MINUTE);
		LOGGER.debug("hour: " + hour);
		LOGGER.debug("minute: " + minute);

		if (minute == 0 || minute == 3 || minute == 6 || minute == 9 || minute == 12 ||  
			minute == 15 || minute == 18 || minute == 21 || minute == 24 || minute == 27 ||  
			minute == 30 || minute == 33 || minute == 36 || minute == 39 || minute == 42 ||  
			minute == 45 || minute == 48 || minute == 51 || minute == 54 || minute == 57) {
			xpike = ThreadLocalRandom.current().nextInt(minute, minute + 3);
			xpike1 = ThreadLocalRandom.current().nextInt(minute, minute + 3);
			while (xpike1 == xpike) {
				xpike1 = ThreadLocalRandom.current().nextInt(minute, minute + 3);
			}
			xpike4 = ThreadLocalRandom.current().nextInt(minute, minute + 3);
			while (xpike4 == xpike || xpike4 == xpike1) {
				xpike4 = ThreadLocalRandom.current().nextInt(minute, minute + 3);
			}
/*			xpike5 = ThreadLocalRandom.current().nextInt(minute, minute + 4);
			while (xpike5 == xpike || xpike5 == xpike1 || xpike5 == xpike4) {
				xpike5 = ThreadLocalRandom.current().nextInt(minute, minute + 4);
			}
			xpike6 = ThreadLocalRandom.current().nextInt(minute, minute + 4);
			while (xpike6 == xpike || xpike6 == xpike1 || xpike6 == xpike4) {
				xpike6 = ThreadLocalRandom.current().nextInt(minute, minute + 4);
			}
*/

			if (xpike == minute) {
				ddlAgentGroup = "xpike";
				INDI_LIST = PinnacleAgentProcessSite.XPIKE_INDI_PLAYERS;
				WOO_LIST = PinnacleAgentProcessSite.XPIKE_WOO_PLAYERS;
			} else if (xpike1 == minute) {
				ddlAgentGroup = "xpike1";
				INDI_LIST = PinnacleAgentProcessSite.XPIKE1_INDI_PLAYERS;
				WOO_LIST = PinnacleAgentProcessSite.XPIKE1_WOO_PLAYERS;
			} else if (xpike4 == minute) {
				ddlAgentGroup = "XPIKE4";
				INDI_LIST = PinnacleAgentProcessSite.XPIKE4_INDI_PLAYERS;
				WOO_LIST = PinnacleAgentProcessSite.XPIKE4_WOO_PLAYERS;
/*			} else if (xpike5 == minute) {
				ddlAgentGroup = "xPIKE5";
				INDI_LIST = PinnacleAgentProcessSite.XPIKE5_INDI_PLAYERS;
				WOO_LIST = PinnacleAgentProcessSite.XPIKE5_WOO_PLAYERS;
			} else if (xpike6 == minute) {
				ddlAgentGroup = "xpike6";
				INDI_LIST = PinnacleAgentProcessSite.XPIKE6_INDI_PLAYERS;
				WOO_LIST = PinnacleAgentProcessSite.XPIKE6_WOO_PLAYERS;
*/
			}
		} else if (firstTime) {
			if (minute >= 0 && minute < 3) {
				minute = 0;
			} else if (minute >= 3 && minute < 6) {
				minute = 3;
			} else if (minute >= 6 && minute < 9) {
				minute = 6;
			} else if (minute >= 9 && minute < 12) {
				minute = 9;
			} else if (minute >= 12 && minute < 15) {
				minute = 12;
			} else if (minute >= 15 && minute < 18) {
				minute = 15;
			} else if (minute >= 18 && minute < 21) {
				minute = 18;
			} else if (minute >= 21 && minute < 24) {
				minute = 21;
			} else if (minute >= 24 && minute < 27) {
				minute = 24;
			} else if (minute >= 27 && minute < 30) {
				minute = 27;
			} else if (minute >= 30 && minute < 33) {
				minute = 30;
			} else if (minute >= 33 && minute <= 36) {
				minute = 33;
			} else if (minute >= 36 && minute <= 39) {
				minute = 36;
			} else if (minute >= 39 && minute <= 42) {
				minute = 39;
			} else if (minute >= 42 && minute <= 45) {
				minute = 42;
			} else if (minute >= 45 && minute <= 48) {
				minute = 45;
			} else if (minute >= 48 && minute <= 51) {
				minute = 48;
			} else if (minute >= 51 && minute <= 54) {
				minute = 51;
			} else if (minute >= 54 && minute <= 57) {
				minute = 54;
			} else if (minute >= 57 && minute <= 59) {
				minute = 57;
			}

			xpike = ThreadLocalRandom.current().nextInt(minute, minute + 3);
			xpike1 = ThreadLocalRandom.current().nextInt(minute, minute + 3);
			while (xpike1 == xpike) {
				xpike1 = ThreadLocalRandom.current().nextInt(minute, minute + 3);
			}
			xpike4 = ThreadLocalRandom.current().nextInt(minute, minute + 3);
			while (xpike4 == xpike || xpike4 == xpike1) {
				xpike4 = ThreadLocalRandom.current().nextInt(minute, minute + 3);
			}
/*			xpike5 = ThreadLocalRandom.current().nextInt(minute, minute + 3);
			while (xpike5 == xpike || xpike5 == xpike1 || xpike5 == xpike4) {
				xpike5 = ThreadLocalRandom.current().nextInt(minute, minute + 3);
			}
			xpike6 = ThreadLocalRandom.current().nextInt(minute, minute + 3);
			while (xpike6 == xpike || xpike6 == xpike1 || xpike6 == xpike4) {
				xpike6 = ThreadLocalRandom.current().nextInt(minute, minute + 3);
			}
*/
			firstTime = false;

			LOGGER.debug("xpike: " + xpike);
			LOGGER.debug("xpike1: " + xpike1);
			LOGGER.debug("xpike4: " + xpike4);
			LOGGER.debug("xpike5: " + xpike5);
			LOGGER.debug("xpike6: " + xpike6);
			LOGGER.debug("minute: " + minute);

			if (xpike == minute) {
				ddlAgentGroup = "xpike";
				INDI_LIST = PinnacleAgentProcessSite.XPIKE_INDI_PLAYERS;
				WOO_LIST = PinnacleAgentProcessSite.XPIKE_WOO_PLAYERS;
			} else if (xpike1 == minute) {
				ddlAgentGroup = "xpike1";
				INDI_LIST = PinnacleAgentProcessSite.XPIKE1_INDI_PLAYERS;
				WOO_LIST = PinnacleAgentProcessSite.XPIKE1_WOO_PLAYERS;
			} else if (xpike4 == minute) {
				ddlAgentGroup = "XPIKE4";
				INDI_LIST = PinnacleAgentProcessSite.XPIKE4_INDI_PLAYERS;
				WOO_LIST = PinnacleAgentProcessSite.XPIKE4_WOO_PLAYERS;
			} else if (xpike5 == minute) {
				ddlAgentGroup = "xPIKE5";
				INDI_LIST = PinnacleAgentProcessSite.XPIKE5_INDI_PLAYERS;
				WOO_LIST = PinnacleAgentProcessSite.XPIKE5_WOO_PLAYERS;
			} else if (xpike6 == minute) {
				ddlAgentGroup = "xpike6";
				INDI_LIST = PinnacleAgentProcessSite.XPIKE6_INDI_PLAYERS;
				WOO_LIST = PinnacleAgentProcessSite.XPIKE6_WOO_PLAYERS;
			}

			this.resetConnection = true;
		}

		LOGGER.debug("ddlAgentGroup: " + ddlAgentGroup);
		if (ddlAgentGroup != null && ddlAgentGroup.length() > 0) {
			if (INDI_LIST.length > 0 || WOO_LIST.length > 0) {
				// setup the data
				List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
				MAP_DATA.put("__EVENTTARGET", "ddlAgentGroup");
				MAP_DATA.put("ddlAgentGroup", ddlAgentGroup);
				MAP_DATA.put("ddlContests", "");
				MAP_DATA.put("ddlGames", "");
				MAP_DATA.put("ddlRaces", "");
				MAP_DATA.put("txtTicket", "");
				setupNameValuesEmpty(postValuePairs, MAP_DATA, null);
		
				// Post to site page
				String xhtml = postSite("https://agent.pinnaclesports.com/BetsHistory.aspx", postValuePairs);
	
				if (xhtml.contains("lblLogin")) {
					LOGGER.error("Resetting the connection for " + accountName + " " + accountId);
					HttpClientUtils.closeQuietly(this.httpClientWrapper.getClient());
					this.httpClientWrapper.setupHttpClient(proxyName);
					this.loginToSite(this.httpClientWrapper.getUsername(), this.httpClientWrapper.getPassword());
					this.resetConnection = false;
					events = SportsInsightSite.getAllSportsGame().getEvents();
				}
		
				if (xhtml != null && xhtml.length() > 0) {
					LOGGER.debug("xhtml: " + xhtml);
					pendingWagers = PAP.parsePendingBets(xhtml, accountName, accountId, MAP_DATA, INDI_LIST, WOO_LIST);
					if (pendingWagers != null && !pendingWagers.isEmpty()) {
						final List<PendingEvent> removeList = new ArrayList<PendingEvent>();
						final Iterator<PendingEvent> itr = pendingWagers.iterator();
						Iterator<EventPackage> eItr = events.iterator();
						boolean found = false;
		
						while (itr.hasNext()) {
							final PendingEvent pendingEvent = itr.next();
							LOGGER.debug("PinnyPendingEvent: " + pendingEvent);

							if (pendingEvent.getLinetype() != null && pendingEvent.getEventtype() != null && pendingEvent.getLine() != null) {
								String tempTeam = pendingEvent.getTeam();
								String pitcher = pendingEvent.getPitcher();

								if (tempTeam != null && tempTeam.length() > 0) {
									tempTeam = tempTeam.replace("LAA Angels", "Los Angeles Angels");
									pendingEvent.setTeam(tempTeam);
								}

								if (pitcher != null && pitcher.length() > 0) {
									pitcher = pitcher.replace("LAA Angels", "Los Angeles Angels");
									pendingEvent.setPitcher(pitcher);
								}

								eItr = events.iterator();
								found = false;
								while (eItr.hasNext() && !found) {
									final EventPackage ep = eItr.next();
									final Integer rotation1 = ep.getTeamone().getId();
									final ParseBullshit parseBullshit = new ParseBullshit();
	
									if (parseBullshit.findGame(ep, pendingEvent)) {
										final TeamPackage team1 = ep.getTeamone();
										final TeamPackage team2 = ep.getTeamtwo();
										final String team = pendingEvent.getTeam();
		
										if ("total".equals(pendingEvent.getEventtype())) {
											if ("o".equals(pendingEvent.getLineplusminus())) {
												pendingEvent.setRotationid(team1.getId().toString());
											} else {
												pendingEvent.setRotationid(team2.getId().toString());
											}
										} else {
											if (team.equals(team1.getTeam())) {
												pendingEvent.setRotationid(team1.getId().toString());	
											} else if (team.equals(team2.getTeam())) {
												pendingEvent.setRotationid(team2.getId().toString());
											} else if (team.startsWith(team1.getTeam().substring(0, 2))) {
												pendingEvent.setRotationid(team1.getId().toString());
											} else if (team.startsWith(team2.getTeam().substring(0, 2))) {
												pendingEvent.setRotationid(team2.getId().toString());
											}									
										}
		
										if ("total".equals(pendingEvent.getEventtype())) {
											pendingEvent.setTeam(ep.getTeamone().getTeam() + "/" + ep.getTeamtwo().getTeam());
										} else {
											if (pendingEvent.getRotationid() != null && rotation1 != null && pendingEvent.getRotationid().equals(rotation1.toString())) {
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
										LOGGER.debug("CustId: " + pendingEvent.getCustomerid() + " ticketNumber: " + pendingEvent.getTicketnum());
	
										// Now check to see if we have live game action
										if (pendingEvent.getLinetype().equals("game")) {
											final Calendar gameCalendar = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
											gameCalendar.setTime(pendingEvent.getGamedate());
	
											final Calendar now = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
	
											long diff = now.getTimeInMillis() - gameCalendar.getTimeInMillis();
											LOGGER.debug("diff: " + diff);
	
											int tenMinutes = 10 * 60 * 1000;
											LOGGER.debug("tenMinutes: " + tenMinutes);
	
											if (diff >= tenMinutes)
											{
											    //at least 10 minutes difference
												removeList.add(pendingEvent);
											}
										}
									}
								}
	
								if (!found) {
									LOGGER.info("PendingEvent NOT FOUND: " + pendingEvent);
								}
							} else {
								LOGGER.info("PendingEvent DOES NOT HAVE ALL DATA: " + pendingEvent);
							}
						}
	
						// Now remove items that shouldn't be part of the list
						for (PendingEvent pe : removeList) {
							LOGGER.debug("Removing: " + pe);
							pendingWagers.remove(pe);
						}
					}
				}
			}
		}

		LOGGER.info("Exiting getPendingBets()");
		return pendingWagers;
	}

	/**
	 * 
	 * @param accountName
	 * @param accountId
	 * @param anythingObject
	 * @return
	 * @throws BatchException
	 */
	public Set<PendingEvent> getPendingDrHBets(String accountName, String accountId, Object anythingObject) throws BatchException {
		LOGGER.info("Entering getPendingBets()");
		LOGGER.debug("accountName: " + accountName);
		LOGGER.debug("accountId: " + accountId);
		Set<PendingEvent> pendingWagers = null;
		final String ddlAgentGroup = "xpike6";

		LOGGER.debug("ddlAgentGroup: " + ddlAgentGroup);
		if (ddlAgentGroup != null && ddlAgentGroup.length() > 0) {
			// setup the data
			List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
			MAP_DATA.put("__EVENTTARGET", "ddlAgentGroup");
			MAP_DATA.put("ddlAgentGroup", ddlAgentGroup);
			MAP_DATA.put("ddlContests", "");
			MAP_DATA.put("ddlGames", "");
			MAP_DATA.put("ddlRaces", "");
			MAP_DATA.put("txtTicket", "");
			setupNameValuesEmpty(postValuePairs, MAP_DATA, null);
	
			// Post to site page
			String xhtml = postSite("https://agent.pinnaclesports.com/BetsHistory.aspx", postValuePairs);

			if (xhtml.contains("lblLogin")) {
				LOGGER.error("Resetting the connection for " + accountName + " " + accountId);
				HttpClientUtils.closeQuietly(this.httpClientWrapper.getClient());
				this.httpClientWrapper.setupHttpClient(proxyName);
				this.loginToSite(this.httpClientWrapper.getUsername(), this.httpClientWrapper.getPassword());
				this.resetConnection = false;
			}
	
			if (xhtml != null && xhtml.length() > 0) {
				LOGGER.debug("xhtml: " + xhtml);
				final String[][] INDI_LIST = new String[][] { };
				final String[][] WOO_LIST = new String[][] { {"54038", "0", "1000"} };
				pendingWagers = PAP.parsePendingBets(xhtml, accountName, accountId, MAP_DATA, INDI_LIST, WOO_LIST);
				if (pendingWagers != null && !pendingWagers.isEmpty()) {
					final List<PendingEvent> removeList = new ArrayList<PendingEvent>();
					final Iterator<PendingEvent> itr = pendingWagers.iterator();
					final Set<EventPackage> eventsPackage = EventsPackage.getEvents();
					boolean found = false;
	
					while (itr.hasNext()) {
						final PendingEvent pendingEvent = itr.next();
						if (pendingEvent.getLinetype() != null && pendingEvent.getEventtype() != null && pendingEvent.getLine() != null) {
							pendingEvent.setCustomerid(accountId);
							pendingEvent.setInet(accountName);

							String tempTeam = pendingEvent.getTeam();
							String pitcher = pendingEvent.getPitcher();

							if (tempTeam != null && tempTeam.length() > 0) {
								tempTeam = tempTeam.replace("LAA Angels", "Los Angeles Angels");
								pendingEvent.setTeam(tempTeam);
							}

							if (pitcher != null && pitcher.length() > 0) {
								pitcher = pitcher.replace("LAA Angels", "Los Angeles Angels");
								pendingEvent.setPitcher(pitcher);
							}

							final Iterator<EventPackage> eItr = eventsPackage.iterator();
							found = false;
							while (eItr.hasNext() && !found) {
								final EventPackage ep = eItr.next();
								final Integer rotation1 = ep.getTeamone().getId();
								final ParseBullshit parseBullshit = new ParseBullshit();

								if (parseBullshit.findGame(ep, pendingEvent)) {
									final TeamPackage team1 = ep.getTeamone();
									final TeamPackage team2 = ep.getTeamtwo();
									final String team = pendingEvent.getTeam();
	
									if ("total".equals(pendingEvent.getEventtype())) {
										if ("o".equals(pendingEvent.getLineplusminus())) {
											pendingEvent.setRotationid(team1.getId().toString());
										} else {
											pendingEvent.setRotationid(team2.getId().toString());
										}
									} else {
										if (team.equals(team1.getTeam())) {
											pendingEvent.setRotationid(team1.getId().toString());	
										} else if (team.equals(team2.getTeam())) {
											pendingEvent.setRotationid(team2.getId().toString());
										} else if (team.startsWith(team1.getTeam().substring(0, 2))) {
											pendingEvent.setRotationid(team1.getId().toString());
										} else if (team.startsWith(team2.getTeam().substring(0, 2))) {
											pendingEvent.setRotationid(team2.getId().toString());
										}									
									}
	
									if ("total".equals(pendingEvent.getEventtype())) {
										pendingEvent.setTeam(ep.getTeamone().getTeam() + "/" + ep.getTeamtwo().getTeam());
									} else {
										if (pendingEvent.getRotationid() != null && rotation1 != null && pendingEvent.getRotationid().equals(rotation1.toString())) {
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
									LOGGER.debug("CustId: " + pendingEvent.getCustomerid() + " ticketNumber: " + pendingEvent.getTicketnum());

									// Now check to see if we have live game action
									if (pendingEvent.getLinetype().equals("game")) {
										try {
											final Calendar now = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
											// 4/21/2018 8:06:41 PM
											final Date dateAccepted = PENDING_DATE_FORMAT.get().parse(pendingEvent.getDateaccepted() + " PDT");
											final Calendar acceptedCalendar = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
											acceptedCalendar.setTime(dateAccepted);
											
											LOGGER.error("now: " + now);
											LOGGER.error("dateAccepted: " + dateAccepted);
	
											int nowmonth = now.get(Calendar.MONTH);
											int nowday = now.get(Calendar.DAY_OF_MONTH);
											int nowyear = now.get(Calendar.YEAR);
											int nowhour = now.get(Calendar.HOUR);
											
											int acceptedmonth = now.get(Calendar.MONTH);
											int acceptedday = now.get(Calendar.DAY_OF_MONTH);
											int acceptedyear = now.get(Calendar.YEAR);
											int acceptedhour = now.get(Calendar.HOUR);

											if (nowmonth == acceptedmonth &&
												nowday == acceptedday &&
												nowyear == acceptedyear &&
												nowhour == acceptedhour) {
												// Happy Happy :)
											} else {
												LOGGER.error("REMOVING PENDING: " + pendingEvent);
											    // if it's not just there then don't
												removeList.add(pendingEvent);
											}
										} catch (Throwable t) {
											LOGGER.warn(t.getMessage(), t);
										}
									}
								}
							}

							if (!found) {
								LOGGER.info("PendingEvent NOT FOUND: " + pendingEvent);
							}
						} else {
							LOGGER.info("PendingEvent DOES NOT HAVE ALL DATA: " + pendingEvent);
						}
					}

					// Now remove items that shouldn't be part of the list
					for (PendingEvent pe : removeList) {
						LOGGER.error("Removing: " + pe);
						pendingWagers.remove(pe);
					}
				}
			}
		}

		LOGGER.info("Exiting getPendingBets()");
		return pendingWagers;
	}

	@Override
	protected String selectSport(String type) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void parseEventSelection(String xhtml, SiteTransaction siteTransaction, EventPackage eventPackage,
			BaseRecordEvent event, AccountEvent ae) throws BatchException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected SiteTransaction createSiteTransaction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String selectEvent(SiteTransaction siteTransaction, EventPackage eventPackage, BaseRecordEvent event,
			AccountEvent ae) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String completeTransaction(SiteTransaction siteTransaction, EventPackage eventPackage,
			BaseRecordEvent event, AccountEvent ae) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String parseTicketTransaction(String xhtml) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}
}