/**
 * 
 */
package com.ticketadvantage.services.dao.sites.pinnacle;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.sites.SiteEventPackage;
import com.ticketadvantage.services.dao.sites.SiteProcessor;
import com.ticketadvantage.services.dao.sites.SiteTransaction;
import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.AccountEvent;
import com.ticketadvantage.services.model.BaseRecordEvent;
import com.ticketadvantage.services.model.EventPackage;
import com.ticketadvantage.services.model.EventsPackage;
import com.ticketadvantage.services.model.MlRecordEvent;
import com.ticketadvantage.services.model.PendingEvent;
import com.ticketadvantage.services.model.SpreadRecordEvent;
import com.ticketadvantage.services.model.TotalRecordEvent;

/**
 * @author jmiller
 *
 */
public class PinnacleAPIProcessSite extends SiteProcessor {
	private static final Logger LOGGER = Logger.getLogger(PinnacleAPIProcessSite.class);
	private static final String API_HOST = "api.pinnacle.com";
	private static final String API_HOST_URL = "https://api.pinnacle.com";
	private final PinnacleAPIParser MP = new PinnacleAPIParser();
	private String authorizationToken;
	private Integer sportId;
	private Map<Integer, Integer> leagues;
	private Integer period;
	private String lineId;
	private String altLineId;
	private boolean riskwager;
	private List<PinnacleAPIEventPackage> events = new ArrayList<PinnacleAPIEventPackage>();

	/**
	 * 
	 * @param host
	 * @param username
	 * @param password
	 * @param isMobile
	 * @param showRequestResponse
	 */
	public PinnacleAPIProcessSite(String host, String username, String password, boolean isMobile, boolean showRequestResponse) {
		super("Pinnacle", host, username, password, isMobile, showRequestResponse);
		LOGGER.info("Entering PinnacleAPIProcessSite()");

		// Set the parser
		this.siteParser = MP;

		// Setup the menu items
		NFL_LINES_SPORT = new String[] { "Football", "NFL", "NFL Pre Season" };
		NFL_LINES_NAME = new String[] { "Game" };
		NFL_FIRST_SPORT = new String[] { "Football", "NFL", "NFL Pre Season" };
		NFL_FIRST_NAME = new String[] { "1st Half" };
		NFL_SECOND_SPORT = new String[] { "Football", "NFL", "NFL Pre Season" };
		NFL_SECOND_NAME = new String[] { "2nd Half" };
		NCAAF_LINES_SPORT = new String[] { "Football", "NCAA", "NCAA Added" };
		NCAAF_LINES_NAME = new String[] { "Game" };
		NCAAF_FIRST_SPORT = new String[] { "Football", "NCAA", "NCAA Added" };
		NCAAF_FIRST_NAME = new String[] { "1st Half" };
		NCAAF_SECOND_SPORT = new String[] { "Football", "NCAA", "NCAA Added" };
		NCAAF_SECOND_NAME = new String[] { "2nd Half" };
		NBA_LINES_SPORT = new String[] { "Basketball", "NBA" };
		NBA_LINES_NAME = new String[] { "Game", "NBA", "NBA - Playoffs" };
		NBA_FIRST_SPORT = new String[] { "Basketball", "NBA" };
		NBA_FIRST_NAME = new String[] { "1st Half", "NBA - Playoffs" };
		NBA_SECOND_SPORT = new String[] { "2nd Halves", "2nd Halves" };
		NBA_SECOND_NAME = new String[] { "2nd Half", "NBA" };
		NCAAB_LINES_SPORT = new String[] { "Basketball", "NCAA", "NCAA Overtime" };
		NCAAB_LINES_NAME = new String[] { "Game" };
		NCAAB_FIRST_SPORT = new String[] { "Basketball", "NCAA", "NCAA Overtime" };
		NCAAB_FIRST_NAME = new String[] { "1st Half" };
		NCAAB_SECOND_SPORT = new String[] { "Basketball", "NCAA", "NCAA Overtime" };
		NCAAB_SECOND_NAME = new String[] { "2nd Half" };
		NHL_LINES_SPORT = new String[] { "Hockey", "NHL" };
		NHL_LINES_NAME = new String[] { "Game", "NHL", "NHL - Playoffs"};
		NHL_FIRST_SPORT = new String[] { "Hockey", "NHL" };
		NHL_FIRST_NAME = new String[] { "1st Period", "NHL", "NHL - Playoffs" };
		NHL_SECOND_NAME = new String[] { "NHL Hockey - 2nd Period", "NHL Hockey - Extra - 2nd Period", "NHL Hockey Added - 2nd Period" };
		NHL_SECOND_SPORT = new String[] { "NHL", "2nd Halves (click below)" };
		WNBA_LINES_SPORT = new String[] { "Basketball", "WNBA", "WNBA - Pre Season" };
		WNBA_LINES_NAME = new String[] { "Game" };
		WNBA_FIRST_SPORT = new String[] { "Basketball", "WNBA", "WNBA - Pre Season" };
		WNBA_FIRST_NAME = new String[] { "1st Half" };
		WNBA_SECOND_SPORT = new String[] { "Basketball", "WNBA", "WNBA - Pre Season" };
		WNBA_SECOND_NAME = new String[] { "2nd Half" };
		MLB_LINES_SPORT = new String[] { "Baseball", "MLB", "MLB - Pre Season" };
		MLB_LINES_NAME = new String[] { "Game" };
		MLB_FIRST_SPORT = new String[] { "Baseball", "MLB", "MLB - Pre Season" };
		MLB_FIRST_NAME = new String[] { "1st Half" };
		MLB_SECOND_SPORT = new String[] { "Baseball", "MLB", "MLB - Pre Season" };
		MLB_SECOND_NAME = new String[] { "2nd Half" };
		INTERNATIONAL_BASEBALL_LINES_SPORT = new String[] { "Baseball", "International Baseball" };
		INTERNATIONAL_BASEBALL_LINES_NAME = new String[] { "Game", "Japan Baseball", "Korean Baseball" };
		INTERNATIONAL_BASEBALL_FIRST_SPORT = new String[] { "Baseball", "International Baseball" };
		INTERNATIONAL_BASEBALL_FIRST_NAME = new String[] { "1st 5 Innings", "Japan Baseball", "Korean Baseball" };
		INTERNATIONAL_BASEBALL_SECOND_SPORT = new String[] { "2nd Halves", "2nd Halves" };
		INTERNATIONAL_BASEBALL_SECOND_NAME = new String[] { "2nd Half", "International Baseball" };

		LOGGER.info("Exiting PinnacleAPIProcessSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			final String[][] PSITES = new String[][] {
				{"https://api.pinnacle.com", "58061", "jacob11", "500", "500", "500", "None", "CT"}
			};

			final PinnacleAPIProcessSite processSite = new PinnacleAPIProcessSite(PSITES[0][0], PSITES[0][1],
					PSITES[0][2], true, true);
	
		    processSite.httpClientWrapper.setupHttpClient(PSITES[0][6]);
		    processSite.processTransaction = false;
		    processSite.timezone = PSITES[0][7];
		    processSite.userid = new Long(1);
		    String xhtml = processSite.loginToSite(PSITES[0][1], PSITES[0][2]);
		    LOGGER.error("HTML: "+xhtml);

			final Set<PendingEvent> pendingEvents = processSite.getPendingBets(PSITES[0][0], PSITES[0][1], null);
			LOGGER.error("pendingEvents.size(): " + pendingEvents.size());
			Iterator<PendingEvent> itr = pendingEvents.iterator();
			while (itr.hasNext()) {
				final PendingEvent pe = itr.next();
				if (pe.getDoposturl()) {
					processSite.doProcessPendingEvent(pe);
				}
			}

/*			final PreviewInput previewInput = new PreviewInput();
			previewInput.setLinetype("spread");
			previewInput.setLineindicator("-");
			previewInput.setRotationid(new Integer(451));
			previewInput.setSporttype("nfllines");
			previewInput.setProxyname("None");
			previewInput.setTimezone("ET");

			final PreviewOutput previewData = processSite.previewEvent(previewInput);
			LOGGER.error("PreviewData: " + previewData);
*/

/*
			// Step 4b - Get the event ID
		    processSite.eventId = new Integer(1529);

		    final Map<String, String> map = processSite.getMenuFromSite("nbalines", xhtml);
		    LOGGER.debug("map: " + map);

			xhtml = processSite.selectSport("nbalines");
			LOGGER.debug("XHTML: " + xhtml);
		
			final Iterator<EventPackage> ep = processSite.parseGamesOnSite("nbalines", xhtml);
			LOGGER.error("ep: " + ep);

			final SpreadRecordEvent mre = new SpreadRecordEvent();
			mre.setSpreadjuiceplusminusfirstone("-");
			mre.setSpreadinputjuicefirstone("3");
			mre.setSpreadplusminusfirstone("-");
			mre.setSpreadinputfirstone("115");
			mre.setId(new Long(451));
			mre.setEventname("#529 Minnesota -3 -115");
			mre.setEventtype("spread");
			mre.setSport("nbalines"); 
			mre.setUserid(new Long(6));
			mre.setEventid(new Integer(529));
			mre.setEventid1(new Integer(529));
			mre.setEventid2(new Integer(530));
			mre.setRotationid(new Integer(529));
			mre.setWtype("2");
			mre.setAmount("60");

			final MlRecordEvent mre = new MlRecordEvent();
			mre.setMlinputfirstone("");
			mre.setMlplusminusfirstone("");
			mre.setId(new Long(951));
			mre.setEventname("#951 Los Angeles Dodgers -230");
			mre.setEventtype("ml");
			mre.setSport("nbalines"); 
			mre.setUserid(new Long(6));
			mre.setEventid(new Integer(951));
			mre.setEventid1(new Integer(951));
			mre.setEventid2(new Integer(952));
			mre.setRotationid(new Integer(951));
			
			// Step 9 - Find event
			EventPackage eventPackage = processSite.findEvent(ep, mre);
			if (eventPackage == null) {
				throw new BatchException(BatchErrorCodes.GAME_NOT_AVAILABLE, BatchErrorMessage.GAME_NOT_AVAILABLE, "Game cannot be found on site for " + mre.getEventname(), xhtml);
			}

			LOGGER.error("eventPackage: " + eventPackage);
*/
		} catch (Throwable t) {
			LOGGER.error("Throwable Exception", t);
		}
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
		List<NameValuePair> headerValuePairs = new ArrayList<NameValuePair>(3);
		headerValuePairs.add(new BasicNameValuePair("Host", API_HOST));
		headerValuePairs.add(new BasicNameValuePair("Referer", this.httpClientWrapper.getHost() + "/"));
		headerValuePairs.add(new BasicNameValuePair("Authorization", authorizationToken));

		if (events.size() > 0) {
			for (PinnacleAPIEventPackage event : events) {
				final Date eDate = event.getEventdatetime();
				final Date enow = new Date();

				if (eDate.after(enow)) {
					headerValuePairs = new ArrayList<NameValuePair>(3);
					headerValuePairs.add(new BasicNameValuePair("Host", API_HOST));
					headerValuePairs.add(new BasicNameValuePair("Referer", this.httpClientWrapper.getHost() + "/"));
					headerValuePairs.add(new BasicNameValuePair("Authorization", authorizationToken));
					String json = getJSONSite(API_HOST_URL + "/v1/fixtures?sportId=4&leagueIds=578", headerValuePairs);
					LOGGER.debug("json: " + json);

					// Parse the games
					final List<SiteEventPackage> eventsPackage = siteParser.parseGames(json, "", MAP_DATA);

					// HTTP Header Get
					headerValuePairs = new ArrayList<NameValuePair>(3);
					headerValuePairs.add(new BasicNameValuePair("Host", API_HOST));
					headerValuePairs.add(new BasicNameValuePair("Referer", this.httpClientWrapper.getHost() + "/"));
					headerValuePairs.add(new BasicNameValuePair("Authorization", authorizationToken));
	
					json = getJSONSite(API_HOST_URL + "/v1/odds?sportId=4" + "&eventIds=" + event.getEventid(), headerValuePairs);
					LOGGER.debug("json: " + json);
					event = MP.processOdds(json, sportType, event);

					LOGGER.error("Event: " + event);
				}
			}
		} else {
			// Get the menu
			final String json = getJSONSite(API_HOST_URL + "/v1/fixtures?sportId=4&leagueIds=578", headerValuePairs);
			LOGGER.debug("json: " + json);

			// Parse the games
			final List<SiteEventPackage> eventsPackage = siteParser.parseGames(json, sportType, MAP_DATA);

			if (eventsPackage != null) {
				for (int x = 0; x < eventsPackage.size(); x++) {
					PinnacleAPIEventPackage sep = (PinnacleAPIEventPackage)eventsPackage.get(x);
					LOGGER.error("PinnacleAPIEventPackage: " + sep);
	
					try {
						// HTTP Header Get
						headerValuePairs = new ArrayList<NameValuePair>(3);
						headerValuePairs.add(new BasicNameValuePair("Host", API_HOST));
						headerValuePairs.add(new BasicNameValuePair("Referer", this.httpClientWrapper.getHost() + "/"));
						headerValuePairs.add(new BasicNameValuePair("Authorization", authorizationToken));

						final String json2 = getJSONSite(API_HOST_URL + "/v1/odds?sportId=4" + "&eventIds=" + sep.getEventid(),
								headerValuePairs);
						LOGGER.debug("json2: " + json2);
						sep = MP.processOdds(json2, "wnbalines", sep);

						LOGGER.error("Event: " + sep);

						// Get time
						final Calendar now = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
						final Calendar newTime = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
						newTime.setTime(sep.getSiteteamone().getEventdatetime());

						int month = now.get(Calendar.MONTH);
						int day = now.get(Calendar.DAY_OF_MONTH);
						int year = now.get(Calendar.YEAR);
						int gmonth = newTime.get(Calendar.MONTH);
						int gday = newTime.get(Calendar.DAY_OF_MONTH);
						int gyear = newTime.get(Calendar.YEAR);

						if (month == gmonth && day == gday && year == gyear) {
							// Found a game today now get game time
							events.add(sep);
							LOGGER.error("PinnacleAPIEventPackage: " + sep);
						}
					} catch (Throwable t) {

					}
				}
			} else {
				events.clear();
			}
		}

		LOGGER.info("Exiting getPendingBets()");
		return pendingWagers;
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
		MP.setTimezone(timezone);

		// Encode username/password
		try {
			final String encodeString = username + ":" + password;
			authorizationToken = "Basic " + Base64.getEncoder().encodeToString(encodeString.getBytes("utf-8"));
			LOGGER.debug("authorizationToken: " + authorizationToken);
		} catch (UnsupportedEncodingException uee) {
			LOGGER.error(uee.getMessage(), uee);
		}

		// HTTP Header GET
		final List<NameValuePair> headerValuePairs = new ArrayList<NameValuePair>(3);
		headerValuePairs.add(new BasicNameValuePair("Host", API_HOST));
		headerValuePairs.add(new BasicNameValuePair("Referer", this.httpClientWrapper.getHost() + "/"));
		headerValuePairs.add(new BasicNameValuePair("Authorization", authorizationToken));

		// Get the menu
		final String json = getJSONSite(API_HOST_URL + "/v2/sports", headerValuePairs);
		LOGGER.debug("json: " + json);

		LOGGER.info("Exiting loginToSite()");
		return json;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#getMenuFromSite(java.lang.String, java.lang.String)
	 */
	protected Map<String, String> getMenuFromSite(String sportType, String xhtml) throws BatchException {
		LOGGER.info("Entering getMenuFromSite()");
		LOGGER.debug("sportType: " + sportType);
		LOGGER.debug("xhtml: " + xhtml);
		String labels[]= null;
		String sport[] = null;

		if (sportType != null && sportType.length() > 0) {
			if ("nfllines".equals(sportType)) {
				labels = NFL_LINES_NAME;
				sport = NFL_LINES_SPORT;
			} else if ("nflfirst".equals(sportType)) {
				labels = NFL_FIRST_NAME;
				sport = NFL_FIRST_SPORT;
			} else if ("nflsecond".equals(sportType)) {
				labels = NFL_SECOND_NAME;
				sport = NFL_SECOND_SPORT;
			} else if ("ncaaflines".equals(sportType)) {
				labels = NCAAF_LINES_NAME;
				sport = NCAAF_LINES_SPORT;
			} else if ("ncaaffirst".equals(sportType)) {
				labels = NCAAF_FIRST_NAME;
				sport = NCAAF_FIRST_SPORT;
			} else if ("ncaafsecond".equals(sportType)) {
				labels = NCAAF_SECOND_NAME;
				sport = NCAAF_SECOND_SPORT;
			} else if ("nbalines".equals(sportType)) {
				labels = NBA_LINES_NAME;
				sport = NBA_LINES_SPORT;
			} else if ("nbafirst".equals(sportType)) {
				labels = NBA_FIRST_NAME;
				sport = NBA_FIRST_SPORT;
			} else if ("nbasecond".equals(sportType)) {
				labels = NBA_SECOND_NAME;
				sport = NBA_SECOND_SPORT;
			} else if ("ncaablines".equals(sportType)) {
				labels = NCAAB_LINES_NAME;
				sport = NCAAB_LINES_SPORT;
			} else if ("ncaabfirst".equals(sportType)) {
				labels = NCAAB_FIRST_NAME;
				sport = NCAAB_FIRST_SPORT;
			} else if ("ncaabsecond".equals(sportType)) {
				labels = NCAAB_SECOND_NAME;
				sport = NCAAB_SECOND_SPORT;
			} else if ("nhllines".equals(sportType)) {
				labels = NHL_LINES_NAME;
				sport = NHL_LINES_SPORT;
			} else if ("nhlfirst".equals(sportType)) {
				labels = NHL_FIRST_NAME;
				sport = NHL_FIRST_SPORT;
			} else if ("nhlsecond".equals(sportType)) {
				labels = NHL_SECOND_NAME;
				sport = NHL_SECOND_SPORT;
			} else if ("nhlthird".equals(sportType)) {
				labels = NHL_THIRD_NAME;
				sport = NHL_THIRD_SPORT;
			} else if ("wnbalines".equals(sportType)) {
				labels = WNBA_LINES_NAME;
				sport = WNBA_LINES_SPORT;
			} else if ("wnbafirst".equals(sportType)) {
				labels = WNBA_FIRST_NAME;
				sport = WNBA_FIRST_SPORT;
			} else if ("wnbasecond".equals(sportType)) {
				labels = WNBA_SECOND_NAME;
				sport = WNBA_SECOND_SPORT;
			} else if ("mlblines".equals(sportType)) {
				labels = MLB_LINES_NAME;
				sport = MLB_LINES_SPORT;
			} else if ("mlbfirst".equals(sportType)) {
				labels = MLB_FIRST_NAME;
				sport = MLB_FIRST_SPORT;
			} else if ("mlbsecond".equals(sportType)) {
				labels = MLB_SECOND_NAME;
				sport = MLB_SECOND_SPORT;
			} else if ("mlbthird".equals(sportType)) {
				labels = MLB_THIRD_NAME;
				sport = MLB_THIRD_SPORT;
			} else if ("internationalbaseballlines".equals(sportType)) {
				labels = INTERNATIONAL_BASEBALL_LINES_NAME;
				sport = INTERNATIONAL_BASEBALL_LINES_SPORT;
			} else if ("internationalbaseballfirst".equals(sportType)) {
				labels = INTERNATIONAL_BASEBALL_FIRST_NAME;
				sport = INTERNATIONAL_BASEBALL_FIRST_SPORT;
			} else if ("internationalbaseballsecond".equals(sportType)) {
				labels = INTERNATIONAL_BASEBALL_SECOND_NAME;
				sport = INTERNATIONAL_BASEBALL_SECOND_SPORT;
			} else {
				throw new BatchException("Unsupported sportType");
			}
		}

		// Parse Sport
		sportId = MP.parseSport(xhtml, sport);
		LOGGER.debug("sportId: " + sportId);

		// HTTP Header GET
		final List<NameValuePair> headerValuePairs = new ArrayList<NameValuePair>(3);
		headerValuePairs.add(new BasicNameValuePair("Host", API_HOST));
		headerValuePairs.add(new BasicNameValuePair("Referer", this.httpClientWrapper.getHost() + "/"));
		headerValuePairs.add(new BasicNameValuePair("Authorization", authorizationToken));

		// Parse leagues
		String json = getJSONSite(API_HOST_URL + "/v2/leagues?sportId=" + sportId, headerValuePairs);
		leagues = MP.parseLeague(json, sport);
		LOGGER.debug("leagues: " + leagues);

		// Parse periods
		json = getJSONSite(API_HOST_URL + "/v1/periods?sportId=" + sportId, headerValuePairs);
		period = MP.parsePeriod(json, labels);
		LOGGER.debug("period: " + period);

		LOGGER.info("Exiting getMenuFromSite()");
		return MAP_DATA;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#selectSport(java.lang.String)
	 */
	@Override
	protected String selectSport(String type) throws BatchException {
		LOGGER.info("Entering selectSport()");
		LOGGER.debug("type: " + type);

		// HTTP Header Get
		final List<NameValuePair> headerValuePairs = new ArrayList<NameValuePair>(3);
		headerValuePairs.add(new BasicNameValuePair("Host", API_HOST));
		headerValuePairs.add(new BasicNameValuePair("Referer", this.httpClientWrapper.getHost() + "/"));
		headerValuePairs.add(new BasicNameValuePair("Authorization", authorizationToken));

		// Parse leagues
		String leagueIds = "";
		final Set<Integer> keys = this.leagues.keySet();
		int counter = 0;
		for (Integer key : keys) {
			if (counter == 0) {
				leagueIds += this.leagues.get(key);
			} else {
				leagueIds += ("," + this.leagues.get(key));
			}
			counter++;
		}

		final String json = getJSONSite(API_HOST_URL + "/v1/fixtures?sportId=" + sportId + "&leagueIds=" + leagueIds, headerValuePairs);
		LOGGER.debug("json: " + json);

		LOGGER.info("Exiting selectSport()");
		return json;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#parseGamesOnSite(java.lang.String, java.lang.String)
	 */
	@Override
	protected Iterator<EventPackage> parseGamesOnSite(String sportType, String xhtml) throws BatchException {
		LOGGER.info("Entering parseGamesOnSite()");
		LOGGER.debug("sportType: " +  sportType);
		gamesXhtml = xhtml;
		final EventsPackage esp = new EventsPackage();
		Iterator<EventPackage> ep = null;

		// Clear the MAP
		if (MAP_DATA != null) {
			MAP_DATA.clear();
		}

		// Parse the games
		final List<SiteEventPackage> eventsPackage = siteParser.parseGames(xhtml, sportType, MAP_DATA);
		if (eventsPackage != null) {
			for (int x = 0; x < eventsPackage.size(); x++) {
				try {
					PinnacleAPIEventPackage aep = (PinnacleAPIEventPackage)eventsPackage.get(x);
					LOGGER.debug("PinnacleAPIEventPackage:" + aep);

					int eventid = 0;
					if (eventId != null) {
						eventid = eventId.intValue();
						if (eventid % 2 == 0) {
							eventid = eventid - 1;
						}
					}

					int gamerotid = aep.getId().intValue();
					int firstrotid = aep.getId().intValue() + 1000;
					int secondrotid = aep.getId().intValue() + 2000;
					int thirdrotid = aep.getId().intValue() + 3000;
					LOGGER.error("eventid: " + eventid);
					LOGGER.error("gamerotid: " + gamerotid);
					LOGGER.error("firstrotid: " + firstrotid);
					LOGGER.error("secondrotid: " + secondrotid);
					LOGGER.error("thirdrotid: " + thirdrotid);
					
					// Check that we have the same eventId
					if (eventid != 0 && (eventid == gamerotid ||
						eventid == firstrotid ||
						eventid == secondrotid ||
						eventid == thirdrotid)) {
						// HTTP Header Get
						final List<NameValuePair> headerValuePairs = new ArrayList<NameValuePair>(3);
						headerValuePairs.add(new BasicNameValuePair("Host", API_HOST));
						headerValuePairs.add(new BasicNameValuePair("Referer", this.httpClientWrapper.getHost() + "/"));
						headerValuePairs.add(new BasicNameValuePair("Authorization", authorizationToken));
		
						final String json = getJSONSite(API_HOST_URL + "/v1/odds?sportId=" + sportId + "&eventIds=" + aep.getEventid(), headerValuePairs);
						LOGGER.debug("json: " + json);
						aep = MP.processOdds(json, sportType, aep);

						esp.addEvent(aep);
					}
				} catch (BatchException be) {
					LOGGER.warn(be.getMessage(), be);
				}
			}
		} else {
			LOGGER.error(sportType + " null eventsPackage");
			// Throw Exception
			throw new BatchException(BatchErrorCodes.XHTML_GAMES_PARSING_EXCEPTION,
				BatchErrorMessage.XHTML_GAMES_PARSING_EXCEPTION, "No games available", "<![CDATA[" + xhtml + "]]>");
		}

		// Check for valid objects
		final Set<EventPackage> events = esp.getEvents();
		if (events != null && !events.isEmpty()) {
			ep = events.iterator();
		}
		LOGGER.debug("EP: " + ep);

		LOGGER.info("Exiting parseGamesOnSite()");
		return ep;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#createSiteTransaction()
	 */
	@Override
	protected SiteTransaction createSiteTransaction() {
		LOGGER.info("Entering createSiteTransaction()");

		final SiteTransaction siteTransaction = new SiteTransaction();

		LOGGER.info("Exiting createSiteTransaction()");
		return siteTransaction;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#selectEvent(com.ticketadvantage.services.dao.sites.SiteTransaction, com.ticketadvantage.services.model.EventPackage, com.ticketadvantage.services.model.BaseRecordEvent, com.ticketadvantage.services.model.AccountEvent)
	 */
	@Override
	protected String selectEvent(SiteTransaction siteTransaction, EventPackage eventPackage, BaseRecordEvent event, AccountEvent ae) throws BatchException {
		LOGGER.info("Entering selectEvent()");
		LOGGER.debug("SiteTransaction: " + siteTransaction);
		LOGGER.debug("EventPackage: " + eventPackage);
		LOGGER.debug("BaseRecordEvent: " + event);
		LOGGER.debug("AccountEvent: " + ae);
		final SiteEventPackage siteEventPackage = (SiteEventPackage)eventPackage;
		String siteAmount = siteTransaction.getAmount();
		LOGGER.error("siteAmount: " + siteAmount);

		// Get the spread transaction
		if (event instanceof SpreadRecordEvent) {
			Double mAmount = Double.parseDouble(siteAmount);
			LOGGER.error("mAmount: " + mAmount);
			if (mAmount != null) {
				if (siteTransaction.getRiskorwin().intValue() == 1) {
					if (mAmount.doubleValue() > siteEventPackage.getSpreadMax().intValue()) {
						siteAmount = siteEventPackage.getSpreadMax().toString();
					}
				} else {
					float spreadJuice = ae.getSpreadjuice();
					LOGGER.error("spreadJuice: " + spreadJuice);
					Double risk = mAmount * (100 / spreadJuice);
					risk = round(risk.doubleValue(), 2);
					LOGGER.error("Risk: " + risk);
					LOGGER.error("siteEventPackage.getSpreadMax().intValue(): " + siteEventPackage.getSpreadMax().intValue());
					if (risk.doubleValue() > siteEventPackage.getSpreadMax().intValue()) {
						siteAmount = siteEventPackage.getSpreadMax().toString();
					} else {
						siteAmount = risk.toString();
					}
					riskwager = true;
				}
			}

			if (siteTransaction.getOptionValue() != null) {
				altLineId = siteTransaction.getOptionValue();
				LOGGER.error("altLineId: " + altLineId);
			}
		} else if (event instanceof TotalRecordEvent) {
			Double mAmount = Double.parseDouble(siteAmount);
			LOGGER.error("mAmount: " + mAmount);
			if (mAmount != null) {
				if (siteTransaction.getRiskorwin().intValue() == 1) {
					if (mAmount.doubleValue() > siteEventPackage.getTotalMax().intValue()) {
						siteAmount = siteEventPackage.getTotalMax().toString();
					}
				} else {
					float totalJuice = ae.getTotaljuice();
					LOGGER.error("totalJuice: " + totalJuice);
					Double risk = mAmount * (100 / totalJuice);
					risk = round(risk.doubleValue(), 2);
					LOGGER.error("Risk: " + risk);
					LOGGER.error("siteEventPackage.getTotalMax().intValue(): " + siteEventPackage.getTotalMax().intValue());
					if (risk.doubleValue() > siteEventPackage.getTotalMax().intValue()) {
						siteAmount = siteEventPackage.getTotalMax().toString();
					} else {
						siteAmount = risk.toString();
					}
					riskwager = true;
				}
			}

			if (siteTransaction.getOptionValue() != null) {
				altLineId = siteTransaction.getOptionValue();
				LOGGER.error("altLineId: " + altLineId);
			}
		} else if (event instanceof MlRecordEvent) {
			Double mAmount = Double.parseDouble(siteAmount);
			LOGGER.error("mAmount: " + mAmount);
			if (mAmount != null) {
				if (siteTransaction.getRiskorwin().intValue() == 1) {
					if (mAmount.doubleValue() > siteEventPackage.getTotalMax().intValue()) {
						siteAmount = siteEventPackage.getMlMax().toString();
					}
				} else {
					float mlJuice = ae.getMljuice();
					LOGGER.error("mlJuice: " + mlJuice);
					Double risk = mAmount * (100 / mlJuice);
					risk = round(risk.doubleValue(), 2);
					LOGGER.error("Risk: " + risk);
					LOGGER.error("siteEventPackage.getMlMax().intValue(): " + siteEventPackage.getMlMax().intValue());
					if (risk.doubleValue() > siteEventPackage.getMlMax().intValue()) {
						siteAmount = siteEventPackage.getMlMax().toString();
					} else {
						siteAmount = risk.toString();
					}
					riskwager = true;
				}
			}
			altLineId = siteTransaction.getInputValue();
			LOGGER.error("altLineId: " + altLineId);
		}
		siteTransaction.setAmount(siteAmount);
		ae.setActualamount(siteAmount);

		LOGGER.info("Exiting selectEvent()");
		return "";
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#parseEventSelection(java.lang.String, com.ticketadvantage.services.dao.sites.SiteTransaction, com.ticketadvantage.services.model.EventPackage, com.ticketadvantage.services.model.BaseRecordEvent, com.ticketadvantage.services.model.AccountEvent)
	 */
	@Override
	protected void parseEventSelection(String xhtml, SiteTransaction siteTransaction, EventPackage eventPackage, BaseRecordEvent event, AccountEvent ae) throws BatchException {
		LOGGER.info("Entering parseEventSelection()");
		LOGGER.info("Exiting parseEventSelection()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#completeTransaction(com.ticketadvantage.services.dao.sites.SiteTransaction, com.ticketadvantage.services.model.EventPackage, com.ticketadvantage.services.model.BaseRecordEvent, com.ticketadvantage.services.model.AccountEvent)
	 */
	@Override
	protected String completeTransaction(SiteTransaction siteTransaction, EventPackage eventPackage, BaseRecordEvent event, AccountEvent ae) throws BatchException {
		LOGGER.info("Entering completeTransaction()");
		String json = null;

		// Actually process the transaction?
		if (processTransaction) {
			// HTTP POST Options
			final List<NameValuePair> headerValuePairs = new ArrayList<NameValuePair>(3);
			headerValuePairs.add(new BasicNameValuePair("Origin", this.httpClientWrapper.getHost()));
			headerValuePairs.add(new BasicNameValuePair("Referer", this.httpClientWrapper.getHost()));
			headerValuePairs.add(new BasicNameValuePair("Authorization", authorizationToken));

	        final UUID uuid = UUID.randomUUID();
	        final String randomUUIDString = uuid.toString();
	        final PinnacleAPIEventPackage pep = (PinnacleAPIEventPackage)eventPackage;
	        lineId = pep.getLineId().toString();
	        String side = null;
	        String betType = "SPREAD";
	        String team = "TEAM1";

	        if (event instanceof SpreadRecordEvent) {
	        		final SpreadRecordEvent sre = (SpreadRecordEvent)event;
	        		if (sre.getSpreadinputfirstone() != null && sre.getSpreadinputfirstone().length() > 0) {
	        			team = "TEAM1";
	        		} else {
	        			team = "TEAM2";
	        		}
	        } else if (event instanceof TotalRecordEvent) {
	        		betType = "TOTAL_POINTS";
	        		final TotalRecordEvent tre = (TotalRecordEvent)event;
	        		if (tre.getTotalinputfirstone() != null && tre.getTotalinputfirstone().length() > 0) {
	        			side = "OVER";
	        			team = "TEAM1";
	        		} else {
	        			side = "UNDER";
	        			team = "TEAM2";
	        		}
	        } else if (event instanceof MlRecordEvent) {
	        		betType = "MONEYLINE";
	        		final MlRecordEvent mre = (MlRecordEvent)event;
	        		if (mre.getMlinputfirstone() != null && mre.getMlinputfirstone().length() > 0) {
	        			team = "TEAM1";
	        		} else {
	        			team = "TEAM2";
	        		}
	        }

			// Setup AddBet POST JSON
			StringBuffer postObj = new StringBuffer(200);
			postObj.append("{");
			postObj.append("\"oddsFormat\": \"AMERICAN\",");
			postObj.append("\"uniqueRequestId\": \"").append(randomUUIDString).append("\",");
			postObj.append("\"acceptBetterLine\": true,");
			postObj.append("\"stake\": ").append(siteTransaction.getAmount()).append(",");
			postObj.append("\"winRiskStake\": \"").append(riskwager ? "RISK" : "WIN").append("\",");
			postObj.append("\"lineId\": ").append(lineId).append(",");
			if (lineId != null && lineId.equals(altLineId)) {
				postObj.append("\"altLineId\": null,");				
			} else {
				postObj.append("\"altLineId\": ").append(altLineId).append(",");
			}
			postObj.append("\"pitcher1MustStart\": true,");
			postObj.append("\"pitcher2MustStart\": true,");
			postObj.append("\"fillType\": \"NORMAL\",");
			postObj.append("\"sportId\": ").append(sportId).append(",");
			postObj.append("\"eventId\": ").append(pep.getEventid()).append(",");
			postObj.append("\"periodNumber\": ").append(period).append(",");
			postObj.append("\"betType\": \"").append(betType).append("\",");
			postObj.append("\"team\": \"").append(team).append("\",");
			if (side == null) {
				postObj.append("\"side\": null");
			} else {
				postObj.append("\"side\": \"").append(side).append("\"");
			}
			postObj.append("}");

			// HTTP Post
			json = postJSONSite(API_HOST_URL + "/v2/bets/straight", postObj.toString(), headerValuePairs);

			try {
				MP.parseTicketNumber(json);
			} catch (BatchException be) {
				if (be.getErrorcode() == BatchErrorCodes.LINE_CHANGED_ERROR) {
//					json = processLineChange("SaveBet", json, event, ae);
				} else {
					throw be;
				}
			}
		}

		LOGGER.info("Exiting completeTransaction()");
		return json;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#parseTicketTransaction(java.lang.String)
	 */
	@Override
	protected String parseTicketTransaction(String xhtml) throws BatchException {
		LOGGER.info("Entering parseTicketNumber()");		
		String ticketNumber = null;

		// Actually process the transaction?
		if (processTransaction) {
			ticketNumber = MP.parseTicketNumber(xhtml);
			LOGGER.debug("ticketNumber: " + ticketNumber);
		}

		LOGGER.info("Exiting parseTicketNumber()");
		return ticketNumber;		
	}
}