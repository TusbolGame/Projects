/**
 * 
 */
package com.ticketadvantage.services.dao.sites.play305;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
public class Play305MobileProcessSite extends SiteProcessor {
	private static final Logger LOGGER = Logger.getLogger(Play305MobileProcessSite.class);
	private static final String CREDENTIALS = "/qubic/api/System/credentials";
	private static final String GET_TOKEN = "/qubic/api/System/getToken";
	private static final String AUTHENTICATE_CUSTOMER = "/qubic/api/System/authenticateCustomer";
	private static final String VERIFY_SESSION = "/qubic/api/System/verifySession";
	private static final String GET_ACCOUNT_INFO = "/qubic/api/Customer/getAccountInfo";
	private static final String GET_SPORTS_LEAGUES = "/qubic/api/League/Get_SportsLeagues";
	private static final String GET_LEAGUE_LINES = "/qubic/api/Lines/Get_LeagueLines";
	private static final String GET_PREV_BET_ON_GAME = "/qubic/api/Wager/getPrevBetOnGame";
	private static final String GET_ACCUMULATED_STRAIGHT_COMPLETE = "/qubic/api/Limit/getAccumulatedStraightComplete";
	private static final String GET_ACCUMULATED_TEAM = "/qubic/api/Limit/getAccumulatedTeam";
	private static final String VALID_PASSWORD = "/qubic/api/Customer/validPassword";
	private static final String CHECK_WAGER_LINE = "/qubic/api/Wager/checkWagerLine";
	private static final String INSERT_WAGER_STRAIGHT = "/qubic/api/Wager/insertWagerStraight";
	private static final String WRITE = "/qubic/api/Log/write";
	private static final String WRITE_DETAIL = "/qubic/api/Log/writeDetail";
	private static final String GET_MINIMUM_WAGER = "/qubic/api/Limit/getMinimumWager";
	private static final String GET_AMOUNT_LIMIT = "/qubic/api/Limit/getAmountLimit";
	private static final String GET_EARLY_LIMIT_STRAIGHT = "/qubic/api/Limit/getEarlyLimitStraight";
	private final Play305MobileParser MP = new Play305MobileParser();
	private String customerID;
	private String agentID;
	private String token;
	private String office;
	private String store;
	private String period;
	private String periodDesc;
	private static String captchaStr="";
	private List<String> leagueList = new ArrayList<String>();

	/**
	 * 
	 * @param host
	 * @param username
	 * @param password
	 * @param isMobile
	 * @param showRequestResponse
	 */
	public Play305MobileProcessSite(String host, String username, String password, boolean isMobile, boolean showRequestResponse) {
		super("Play305", host, username, password, isMobile, showRequestResponse);
		LOGGER.info("Entering Play305MobileProcessSite()");

		// Set the parser
		this.siteParser = MP;

		// Setup the menu items
		NFL_LINES_SPORT = new String[] { "Football", "NFL" };
		NFL_LINES_NAME = new String[] { "Game", "NFL - Pre Season", "NFL" };
		NFL_FIRST_SPORT = new String[] { "Football", "NFL" };
		NFL_FIRST_NAME = new String[] { "1st Half", "" };
		NFL_SECOND_SPORT = new String[] { "2nd Halves", "2nd Halves" };
		NFL_SECOND_NAME = new String[] { "2nd Half", "NFL", "NFL - 2nd Half", "NFL 2nd Half", "NFL - Pre Season 2nd Half" };
		NCAAF_LINES_SPORT = new String[] { "Football", "NCAA Football" };
		NCAAF_LINES_NAME = new String[] { "Game", "College Football", "College Football - Extra & FCS", "College Football Extra", "College - Bowls", "College - Playoffs", "College Football - Playoffs", "College Football - Bowls", "College Football - FCS Playoffs" };
		NCAAF_FIRST_SPORT = new String[] { "Football", "NCAA Football" };
		NCAAF_FIRST_NAME = new String[] { "1st Half", "College Football Extra 1st Half", "College Football", "College Football - Extra & FCS", "College Football Extra", "College - Bowls", "College - Playoffs", "College Football - Playoffs", "College Football - Bowls", "College Football - FCS Playoffs" };
		NCAAF_SECOND_SPORT = new String[] { "2nd Halves", "2nd Halves" };
		NCAAF_SECOND_NAME = new String[] { "2nd Half", "College Football", "College Football Extra", "College Football Extra 2nd Half", "College Football - Extra & FCS", "College Football Extra", "College - Bowls", "College - Playoffs", "College Football - Playoffs", "College Football - Bowls", "College Football - FCS Playoffs" };
		NBA_LINES_SPORT = new String[] { "NBA" };
		NBA_LINES_NAME = new String[] { "Game" };
		NBA_FIRST_SPORT = new String[] { "NBA" };
		NBA_FIRST_NAME = new String[] { "1st Half" };
		NBA_SECOND_SPORT = new String[] { "NBA" };
		NBA_SECOND_NAME = new String[] { "2nd Half" };
		NCAAB_LINES_SPORT = new String[] { "NCAA", "NCAA ADDED" };
		NCAAB_LINES_NAME = new String[] { "Game" };
		NCAAB_FIRST_SPORT = new String[] { "NCAA", "NCAA ADDED" };
		NCAAB_FIRST_NAME = new String[] { "1st Half" };
		NCAAB_SECOND_SPORT = new String[] { "NCAA", "NCAA ADDED" };
		NCAAB_SECOND_NAME = new String[] { "2nd Half" };
		NHL_LINES_SPORT = new String[] { "NHL" };
		NHL_LINES_NAME = new String[] { "Game" };
		NHL_FIRST_SPORT = new String[] { "NHL" };
		NHL_FIRST_NAME = new String[] { "1st Period" };
		NHL_SECOND_NAME = new String[] { "NHL" };
		NHL_SECOND_SPORT = new String[] { "2nd Period" };
		WNBA_LINES_NAME = new String[] { "Game", "WNBA" };
		WNBA_LINES_SPORT = new String[] { "Basketball", "WNBA" };
		WNBA_FIRST_NAME = new String[] { "1st Half", "" };
		WNBA_FIRST_SPORT = new String[] { "Basketball", "WNBA" };
		WNBA_SECOND_NAME = new String[] { "2nd Half", "WNBA" };
		WNBA_SECOND_SPORT = new String[] { "2nd Halves", "2nd Halves" };
		MLB_LINES_SPORT = new String[] { "MLB", "MLB PRESEASO" };
		MLB_LINES_NAME = new String[] { "Game" };
		MLB_FIRST_SPORT = new String[] { "MLB", "MLB PRESEASO" };
		MLB_FIRST_NAME = new String[] { "1st 5 Innings" };
		MLB_SECOND_SPORT = new String[] { "MLB", "MLB PRESEASO" };
		MLB_SECOND_NAME = new String[] { "2nd Half" };
		INTERNATIONAL_BASEBALL_LINES_SPORT = new String[] { "Baseball", "International Baseball" };
		INTERNATIONAL_BASEBALL_LINES_NAME = new String[] { "Game", "Japan Baseball", "Korean Baseball" };
		INTERNATIONAL_BASEBALL_FIRST_SPORT = new String[] { "Baseball", "International Baseball" };
		INTERNATIONAL_BASEBALL_FIRST_NAME = new String[] { "1st 5 Innings", "Japan Baseball", "Korean Baseball" };
		INTERNATIONAL_BASEBALL_SECOND_SPORT = new String[] { "2nd Halves", "2nd Halves" };
		INTERNATIONAL_BASEBALL_SECOND_NAME = new String[] { "2nd Half", "International Baseball" };

		LOGGER.info("Exiting Play305MobileProcessSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			final String[][] MSITES = new String[][] {
				{ "https://play305.com/", "KEV106", "nyc7447", "500", "500", "500", "None", "ET"}
			};

			final Play305MobileProcessSite processSite = new Play305MobileProcessSite(MSITES[0][0], MSITES[0][1],
					MSITES[0][2], true, true);

		    processSite.httpClientWrapper.setupHttpClient(MSITES[0][6]);
		    processSite.processTransaction = false;
		    processSite.timezone = MSITES[0][7];
		    processSite.userid = new Long(1);
		    String xhtml= processSite.loginToSite(MSITES[0][1], MSITES[0][2]);
		    LOGGER.error("json: " + xhtml);

			Map<String, String> map = processSite.getMenuFromSite("nbalines", xhtml);
			LOGGER.debug("map: " + map);

			xhtml = processSite.selectSport("nbalines");
			LOGGER.debug("XHTML: " + xhtml);

			final Iterator<EventPackage> ep = processSite.parseGamesOnSite("nbalines", xhtml);

			final SpreadRecordEvent mre = new SpreadRecordEvent();
			mre.setSpreadjuiceplusminussecondone("-");
			mre.setSpreadinputjuicesecondone("110");
			mre.setSpreadplusminussecondone("+");
			mre.setSpreadinputsecondone("1");
			mre.setId(new Long(110));
			mre.setEventname("#8 Vancouver Canucks +.5 -120");
			mre.setEventtype("spread");
			mre.setSport("nbalines");
			mre.setUserid(new Long(6));
			mre.setEventid(new Integer(547));
			mre.setEventid1(new Integer(547));
			mre.setEventid2(new Integer(548));
			mre.setRotationid(new Integer(547));
			mre.setWtype("2");
			mre.setAmount("100");

			// Step 9 - Find event
			EventPackage eventPackage = processSite.findEvent(ep, mre);
			if (eventPackage == null) {
				throw new BatchException(BatchErrorCodes.GAME_NOT_AVAILABLE, BatchErrorMessage.GAME_NOT_AVAILABLE,
						"Game cannot be found on site for " + mre.getEventname(), xhtml);
			}

/*
			Set<PendingEvent> pendingEvents = processSite.getPendingBets(MSITES[0][0], MSITES[0][1]);
			Iterator<PendingEvent> itr = pendingEvents.iterator();
			while (itr.hasNext()) {
				PendingEvent pe = itr.next();
				LOGGER.error("pe: " + pe);
			}
*/

		    /**
			 ************************************************************************
			 *---------------------code for NCAA  College Football Processing----------------------------------
			 ************************************************************************
			 */
/*
		   Map<String, String> map = processSite.getMenuFromSite("ncaaflines", xhtml); 
		   LOGGER.debug("map: " + map);
		   
		    xhtml = processSite.selectSport("ncaaflines");
			LOGGER.debug("XHTML: " + xhtml);
			
			
			final Iterator<EventPackage> ep = processSite.parseGamesOnSite("ncaaflines", xhtml);

			final SpreadRecordEvent mre = new SpreadRecordEvent();
			mre.setSpreadjuiceplusminussecondone("-");
			mre.setSpreadinputjuicesecondone("110");
			mre.setSpreadplusminussecondone("-");
			mre.setSpreadinputsecondone("0.5");
			mre.setId(new Long(110));
			mre.setEventname("#8 Vancouver Canucks +.5 -120");
			mre.setEventtype("spread");
			mre.setSport("ncaaflines"); 
			mre.setUserid(new Long(6));
			mre.setEventid(new Integer(305));
			mre.setEventid1(new Integer(305));
			mre.setEventid2(new Integer(306));
			mre.setRotationid(new Integer(306));
			mre.setWtype("2");
			mre.setAmount("100");

			// Step 9 - Find event
			EventPackage eventPackage = processSite.findEvent(ep, mre);
			if (eventPackage == null) {
				throw new BatchException(BatchErrorCodes.GAME_NOT_AVAILABLE, BatchErrorMessage.GAME_NOT_AVAILABLE, "Game cannot be found on site for " + mre.getEventname(), xhtml);
			}
			
			boolean check = false;
			while(!check) {
				SiteTransaction siteTransaction = new SiteTransaction();
				AccountEvent ae = new AccountEvent();
				ae.setMaxmlamount(new Integer(500));
				
				xhtml = processSite.selectEvent(siteTransaction, eventPackage, mre, ae);
				LOGGER.debug("xhtml: " + xhtml);
				
				//JsonResponse
				JSONObject jsonResponse = new JSONObject(xhtml);
				String captchaImage = jsonResponse.get("CaptchaImage").toString();
				
				//get Captcha String
				LOGGER.error("Captcha Image String: " + captchaImage);
				
				//Process captcha String
				CaptchaReader obj = new CaptchaReader();
				captchaStr = obj.captchaProcess(captchaImage);
				LOGGER.error("Captcha Text: " + captchaStr);

				// Clean up text if necessary
				check=Pattern.matches("[a-zA-Z0-9]{6}",captchaStr);

				if (check) {
					processSite.parseEventSelection(xhtml, siteTransaction, eventPackage, mre, ae);
				}
			}
*/

		    /**
			 ************************************************************************
			 *---------------------code for NCAA  College Football End here----------------------------------
			 ************************************************************************
			 */
			
		   

		    /**
			 ************************************************************************
			 *---------------------code for NFL Processing----------------------------------
			 ************************************************************************
			 */
		  
			/*
			 * Map<String, String> map = processSite.getMenuFromSite("ncaaflines", xhtml);
			 * LOGGER.debug("map: " + map);
			 * 
			 * xhtml = processSite.selectSport("ncaaflines"); LOGGER.debug("XHTML: " +
			 * xhtml);
			 * 
			 * final Iterator<EventPackage> ep = processSite.parseGamesOnSite("ncaaflines",
			 * xhtml);
			 */

/*
			final MlRecordEvent mre = new MlRecordEvent();
			mre.setMlplusminusfirstone("-");
			mre.setMlinputfirstone("110");
			mre.setId(new Long(520));
			mre.setEventname("#251 San Francisco 49ers -110");
			mre.setEventtype("ml");
			mre.setSport("ncaablines"); 
			mre.setUserid(new Long(6));
			mre.setEventid(new Integer(519));
			mre.setEventid1(new Integer(519));
			mre.setEventid2(new Integer(520));
			mre.setRotationid(new Integer(520));
			mre.setWtype("2");
			mre.setAmount("50");
*/
			/*
			 * final SpreadRecordEvent mre = new SpreadRecordEvent();
			 * mre.setSpreadjuiceplusminussecondone("-");
			 * mre.setSpreadinputjuicesecondone("110");
			 * mre.setSpreadplusminussecondone("-"); mre.setSpreadinputsecondone("12.5");
			 * mre.setId(new Long(110));
			 * mre.setEventname("NCAAF #278 GEORGIA -12.5 -120.0 for Game");
			 * mre.setEventtype("spread"); mre.setSport("ncaaflines"); mre.setUserid(new
			 * Long(6)); mre.setEventid(new Integer(277)); mre.setEventid1(new
			 * Integer(277)); mre.setEventid2(new Integer(278)); mre.setRotationid(new
			 * Integer(278)); mre.setWtype("2"); mre.setAmount("100");
			 * 
			 * // Step 9 - Find event EventPackage eventPackage = processSite.findEvent(ep,
			 * mre); if (eventPackage == null) { throw new
			 * BatchException(BatchErrorCodes.GAME_NOT_AVAILABLE,
			 * BatchErrorMessage.GAME_NOT_AVAILABLE, "Game cannot be found on site for " +
			 * mre.getEventname(), xhtml); }
			 */

/*
			boolean check = false;
			while(!check) {
			SiteTransaction siteTransaction = new SiteTransaction();
			AccountEvent ae = new AccountEvent();
			ae.setMaxmlamount(new Integer(500));
			xhtml = processSite.selectEvent(siteTransaction, eventPackage, mre, ae);
			LOGGER.debug("xhtml: " + xhtml);
			
			//JsonResponse
			JSONObject jsonResponse = new JSONObject(xhtml);
			String captchaImage = jsonResponse.get("CaptchaImage").toString();
			
			//get Captcha String
			System.out.println("Captcha Image String: "+ captchaImage);
			LOGGER.debug("Captcha Image String: " + captchaImage);
			
			//Process captcha String
			CaptchaReader obj = new CaptchaReader();
			captchaStr = obj.captchaProcess(captchaImage);
			System.out.println("Captcha Text: "+captchaStr);
			LOGGER.debug("Captcha Text: " + captchaStr);
			
			check=Pattern.matches("[a-zA-Z0-9]{6}",captchaStr);
			
			if(check)
				processSite.parseEventSelection(xhtml, siteTransaction, eventPackage, mre, ae);
			}*/
		    /**
			 ************************************************************************
			 *---------------------code for NFL Processing End here----------------------------------
			 ************************************************************************
			 */
			//=================== Update code END here ==================
			
			
			
			
			
		    
		    
		    
			
			//=========================Old commented code======================
		//	SiteTransaction siteTransaction = new SiteTransaction();
		//	processSite.processCaptcha("xyz");

/*
			final PreviewInput previewInput = new PreviewInput();
			previewInput.setLinetype("spread");
			previewInput.setLineindicator("+");
			previewInput.setRotationid(new Integer(748));
			previewInput.setSporttype("ncaablines");
			previewInput.setProxyname("None");
			previewInput.setTimezone("CT");

			final PreviewOutput previewData = processSite.previewEvent(previewInput);
			LOGGER.error("PreviewData: " + previewData);

			final String[][] MSITES = new String[][] {
					// {"http://www.Betwindycity.com", "w102", "Phillies","500", "500", "500", "Baltimore", "PT"},
					// { "http://www.Sunwager.com", "4488", "John", "300", "300", "300", "Asheville", "ET" },
					// {"http://www.greenisle.club", "Go508", "Iowa", "500","250", "300", "Dallas", "ET" },
					// {"http://www.Scotch88.com", "kwd2202", "jh", "500", "500", "500", "Dallas", "CT"}
//					{"http://www.madmax.ag", "820", "black2020", "500", "500", "500", "Dallas", "CT"}
//					{"http://www.madmax.ag", "834", "sky1212", "500", "500", "500", "Dallas", "CT"}
				// {"http://www.BetWC.ag", "Wc24006", "Demo", "500", "500", "500", "Dallas", "CT"}
				{"http://m.iron69.com", "kwd2202", "jh", "500", "500", "500", "Dallas", "CT"}
			};

			final MetallicaMobileProcessSite processSite = new MetallicaMobileProcessSite(MSITES[0][0], MSITES[0][1],
					MSITES[0][2]);
		    processSite.httpClientWrapper.setupHttpClient(MSITES[0][6]);
		    processSite.processTransaction = false;
		    processSite.timezone = MSITES[0][7];
			processSite.loginToSite("kwd2202", "jh");


			// Loop through the sites
			for (int i = 0; i < MSITES.length; i++) {
				final MetallicaMobileProcessSite processSite = new MetallicaMobileProcessSite(MSITES[i][0], MSITES[i][1],
						MSITES[i][2]);
				final MetallicaMobileProcessSite processSite2 = new MetallicaMobileProcessSite(MSITES[i][0], MSITES[i][1],
						MSITES[i][2]);

				    processSite.httpClientWrapper.setupHttpClient(MSITES[0][6]);
				    processSite.processTransaction = false;
				    processSite.timezone = MSITES[0][7];

				    processSite2.httpClientWrapper.setupHttpClient(MSITES[0][6]);
				    processSite2.processTransaction = false;
				    processSite2.timezone = MSITES[0][7];

					// Now call the test suite
				    MetallicaMobileProcessSite.testSite2(processSite, processSite2, i, MSITES);

				// Now call the test suite
//				MetallicaProcessSite.testSite(processSite, i, MSITES);
			    processSite.httpClientWrapper.setupHttpClient(MSITES[i][6]);
			    processSite.processTransaction = false;
			    processSite.timezone = MSITES[i][7];
				processSite.loginToSite(MSITES[i][1], MSITES[i][2]);
				Set<PendingEvent> pendingEvents = processSite.getPendingBets(MSITES[i][0], MSITES[i][1]);
//				Iterator<PendingEvent> itr = pendingEvents.iterator();
//				while (itr.hasNext()) {
//					PendingEvent pe = itr.next();
//				}
			}
 */
		} catch (Throwable t) {
			LOGGER.error("Throwable Exception", t);
		}
	}

	/**
	 * 
	 * @param accountName
	 * @param accountId
	 * @return
	 * @throws BatchException
	 */
	public Set<PendingEvent> getPendingBets(String accountName, String accountId) throws BatchException {
		LOGGER.info("Entering getPendingBets()");

		// TODO
		
		LOGGER.info("Exiting getPendingBets()");
		return null;
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

		// Get the site
		List<NameValuePair> headerValuePairs = new ArrayList<NameValuePair>(3);
		headerValuePairs.add(new BasicNameValuePair("Host", this.httpClientWrapper.getDomain()));
		headerValuePairs.add(new BasicNameValuePair("Referer", this.httpClientWrapper.getHost()));
		headerValuePairs.add(new BasicNameValuePair("X-Requested-With", "XMLHttpRequest"));

		List<NameValuePair> postNameValuePairs = new ArrayList<NameValuePair>(1);
		postNameValuePairs.add(new BasicNameValuePair("operation", "credentials"));

		// Get the credentials
		String json = postSite(super.getHttpClientWrapper().getHost() + CREDENTIALS,
				postNameValuePairs,
				headerValuePairs);
		MAP_DATA = MP.parseIndex(json);

		postNameValuePairs = new ArrayList<NameValuePair>(3);
		postNameValuePairs.add(new BasicNameValuePair("login", MAP_DATA.get("USER")));
		postNameValuePairs.add(new BasicNameValuePair("operation", "credentials"));
		postNameValuePairs.add(new BasicNameValuePair("password", MAP_DATA.get("PASS")));

		// Get the token
		json = postSite(super.getHttpClientWrapper().getHost() + GET_TOKEN,
				postNameValuePairs,
				headerValuePairs);
		MAP_DATA = MP.parseToken(json);

		postNameValuePairs = new ArrayList<NameValuePair>(6);
		postNameValuePairs.add(new BasicNameValuePair("customerID", username));
		postNameValuePairs.add(new BasicNameValuePair("password", password));
		postNameValuePairs.add(new BasicNameValuePair("multiaccount", "1"));
		postNameValuePairs.add(new BasicNameValuePair("domain", super.getHttpClientWrapper().getDomain()));
		postNameValuePairs.add(new BasicNameValuePair("token", MAP_DATA.get("Token")));
		postNameValuePairs.add(new BasicNameValuePair("operation", "authenticateCustomer"));
		token = MAP_DATA.get("Token");

		// Authenticate the customer
		json = postSite(super.getHttpClientWrapper().getHost() + AUTHENTICATE_CUSTOMER,
				postNameValuePairs,
				headerValuePairs);
		MAP_DATA = MP.parseLogin(json);

		postNameValuePairs = new ArrayList<NameValuePair>(3);
		postNameValuePairs.add(new BasicNameValuePair("customerID", MAP_DATA.get("customerID")));
		postNameValuePairs.add(new BasicNameValuePair("token", "0"));
		postNameValuePairs.add(new BasicNameValuePair("operation", "verifySession"));
		customerID = MAP_DATA.get("customerID");
		store = MAP_DATA.get("Store");
		agentID = MAP_DATA.get("AgentID");

		// Verify the session
		json = postSite(super.getHttpClientWrapper().getHost() + VERIFY_SESSION,
				postNameValuePairs,
				headerValuePairs);

		if (json.contains("\"STATE\":200")) {
			postNameValuePairs = new ArrayList<NameValuePair>(3);
			postNameValuePairs.add(new BasicNameValuePair("customerID", customerID));
			postNameValuePairs.add(new BasicNameValuePair("token", token));
			postNameValuePairs.add(new BasicNameValuePair("operation", "getAccountInfo"));

			// Get the account info
			json = postSite(super.getHttpClientWrapper().getHost() + GET_ACCOUNT_INFO,
					postNameValuePairs,
					headerValuePairs);
			MAP_DATA = MP.parseAccountInfo(json);

			postNameValuePairs = new ArrayList<NameValuePair>(6);
			postNameValuePairs.add(new BasicNameValuePair("customerID", customerID));
			postNameValuePairs.add(new BasicNameValuePair("token", token));
			postNameValuePairs.add(new BasicNameValuePair("wagerType", "Straight"));
			postNameValuePairs.add(new BasicNameValuePair("office", MAP_DATA.get("Office")));
			postNameValuePairs.add(new BasicNameValuePair("placeLateFlag", "false"));
			postNameValuePairs.add(new BasicNameValuePair("operation", "Get_SportsLeagues"));
			office = MAP_DATA.get("Office");

			// Get the sports leagues
			json = postSite(super.getHttpClientWrapper().getHost() + GET_SPORTS_LEAGUES,
					postNameValuePairs,
					headerValuePairs);
		}

		LOGGER.info("Exiting loginToSite()");
		return json;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#selectSport(java.lang.String)
	 */
	@Override
	protected String selectSport(String type) throws BatchException {
		LOGGER.info("Entering selectSport()");
		LOGGER.debug("type: " + type);
		
		if (MAP_DATA.size() > 0) {
			int number = MAP_DATA.size() / 4;

			for (int x = 0; x < number; x++) {
				// Get the site
				final List<NameValuePair> headerValuePairs = new ArrayList<NameValuePair>(3);
				headerValuePairs.add(new BasicNameValuePair("Host", this.httpClientWrapper.getDomain()));
				headerValuePairs.add(new BasicNameValuePair("Referer", this.httpClientWrapper.getHost() + "/customer.html?v=1552460916622"));
				headerValuePairs.add(new BasicNameValuePair("X-Requested-With", "XMLHttpRequest"));

				final List<NameValuePair> postNameValuePairs = new ArrayList<NameValuePair>(16);
				postNameValuePairs.add(new BasicNameValuePair("operation", "Get_LeagueLines"));
				postNameValuePairs.add(new BasicNameValuePair("customerID", customerID));
				postNameValuePairs.add(new BasicNameValuePair("token", token));
				postNameValuePairs.add(new BasicNameValuePair("sportType", MAP_DATA.get("sportType" + x)));
				postNameValuePairs.add(new BasicNameValuePair("sportSubType", MAP_DATA.get("sportSubType" + x)));
				postNameValuePairs.add(new BasicNameValuePair("period", MAP_DATA.get("period" + x)));
				postNameValuePairs.add(new BasicNameValuePair("hourFilter", "0"));
				postNameValuePairs.add(new BasicNameValuePair("propDescription", ""));
				postNameValuePairs.add(new BasicNameValuePair("wagerType", "Straight"));
				postNameValuePairs.add(new BasicNameValuePair("keyword", ""));
				postNameValuePairs.add(new BasicNameValuePair("office", office));
				postNameValuePairs.add(new BasicNameValuePair("correlationID", ""));
				postNameValuePairs.add(new BasicNameValuePair("periodNumber", MAP_DATA.get("periodNumber" + x)));
				postNameValuePairs.add(new BasicNameValuePair("grouping", ""));
				postNameValuePairs.add(new BasicNameValuePair("periods", "0"));
				postNameValuePairs.add(new BasicNameValuePair("rotOrder", "0"));
				postNameValuePairs.add(new BasicNameValuePair("placeLateFlag", "false"));
				period = MAP_DATA.get("periodNumber" + x);
				periodDesc = MAP_DATA.get("period" + x);

				// Get the league lines
				String json = postSite(super.getHttpClientWrapper().getHost() + GET_LEAGUE_LINES,
						postNameValuePairs,
						headerValuePairs);

				leagueList.add(json);
			}
		}

		LOGGER.info("Exiting selectSport()");
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#parseGamesOnSite(java.lang.String, java.lang.String)
	 */
	@Override
	protected Iterator<EventPackage> parseGamesOnSite(String sportType, String xhtml) throws BatchException {
		LOGGER.info("Entering parseGamesOnSite()");
		LOGGER.debug("sportType: " + sportType);
		gamesXhtml = xhtml;
		final EventsPackage esp = new EventsPackage();
		Iterator<EventPackage> ep = null;

		// Clear the MAP
		if (MAP_DATA != null) {
			MAP_DATA.clear();
		}

		for (String league : leagueList) {
			// Parse the games
			final List<SiteEventPackage> eventsPackage = backupEventsPackage = siteParser.parseGames(league, sportType, MAP_DATA);

			if (eventsPackage != null) {
				for (int x = 0; x < eventsPackage.size(); x++) {
					final SiteEventPackage aep = eventsPackage.get(x);
					LOGGER.debug("aep: " + aep);
					esp.addEvent(aep);
				}
			} else {
				LOGGER.error(sportType + " null eventsPackage");
				// Throw Exception
				throw new BatchException(BatchErrorCodes.XHTML_GAMES_PARSING_EXCEPTION,
						BatchErrorMessage.XHTML_GAMES_PARSING_EXCEPTION, "No games available", "<![CDATA[" + xhtml + "]]>");
			}
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

		final Play305Transaction play305Transaction = new Play305Transaction();

		LOGGER.info("Exiting createSiteTransaction()");
		return play305Transaction;
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
		final Play305EventPackage play305EventPackage = (Play305EventPackage)eventPackage;
		Play305TeamPackage theteam = null;
		final Play305TeamPackage team1 = (Play305TeamPackage)play305EventPackage.getSiteteamone();
		final Play305TeamPackage team2 = (Play305TeamPackage)play305EventPackage.getSiteteamtwo();
		
		
		if (team1.getId() == event.getRotationid()) {
			theteam = team1;
		} else {
			theteam = team2;
		}

		String lineType = "";
		String spread = "";
		String totalOU = "";
		String total = "";
		String money = "";

		if (event instanceof SpreadRecordEvent) {
			lineType = "S";
			spread = theteam.getGameSpreadOptionIndicator().get("0") + theteam.getGameSpreadOptionNumber().get("0");
			money = theteam.getGameSpreadOptionJuiceIndicator().get("0") + theteam.getGameSpreadOptionJuiceNumber().get("0");
		} else if (event instanceof TotalRecordEvent) {
			lineType = "T";
			totalOU = theteam.getGameTotalOptionIndicator().get("0").toUpperCase();
			total = theteam.getGameTotalOptionNumber().get("0");
			money = theteam.getGameTotalOptionJuiceIndicator().get("0") + theteam.getGameTotalOptionJuiceNumber().get("0");
		} else if (event instanceof MlRecordEvent) {
			lineType = "M";
			money = theteam.getGameMLOptionJuiceIndicator().get("0") + theteam.getGameMLOptionJuiceNumber().get("0");
		}

		List<NameValuePair> headerValuePairs = new ArrayList<NameValuePair>(3);
		headerValuePairs.add(new BasicNameValuePair("Host", this.httpClientWrapper.getDomain()));
		headerValuePairs.add(new BasicNameValuePair("Referer", this.httpClientWrapper.getHost() + "/customer.html?v=1552460916622"));
		headerValuePairs.add(new BasicNameValuePair("X-Requested-With", "XMLHttpRequest"));

		List<NameValuePair> postNameValuePairs = new ArrayList<NameValuePair>(5);
		postNameValuePairs.add(new BasicNameValuePair("operation", "getPrevBetOnGame"));
		postNameValuePairs.add(new BasicNameValuePair("customerID", customerID));
		postNameValuePairs.add(new BasicNameValuePair("token", token));
		postNameValuePairs.add(new BasicNameValuePair("gameNum", Integer.toString(play305EventPackage.getGameNum())));
		postNameValuePairs.add(new BasicNameValuePair("lineType", lineType));

		// Get previous bet on game
		String json = postSite(super.getHttpClientWrapper().getHost() + GET_PREV_BET_ON_GAME,
				postNameValuePairs,
				headerValuePairs);

		postNameValuePairs = new ArrayList<NameValuePair>(15);
		postNameValuePairs.add(new BasicNameValuePair("operation", "getAccumulatedStraightComplete"));
		postNameValuePairs.add(new BasicNameValuePair("customerID", customerID));
		postNameValuePairs.add(new BasicNameValuePair("token", token));
		postNameValuePairs.add(new BasicNameValuePair("gameNum", Integer.toString(play305EventPackage.getGameNum())));
		postNameValuePairs.add(new BasicNameValuePair("lineType", lineType));
		postNameValuePairs.add(new BasicNameValuePair("chosenTeamID", theteam.getTeam().replace(" ", "+")));
		postNameValuePairs.add(new BasicNameValuePair("totalOU", totalOU));
		postNameValuePairs.add(new BasicNameValuePair("periodNumber", period));
		postNameValuePairs.add(new BasicNameValuePair("spread", spread));
		postNameValuePairs.add(new BasicNameValuePair("total", total));
		postNameValuePairs.add(new BasicNameValuePair("money", money));
		postNameValuePairs.add(new BasicNameValuePair("enforceLimitByLine", ""));
		postNameValuePairs.add(new BasicNameValuePair("enforceLimitByGame", "Y"));
		postNameValuePairs.add(new BasicNameValuePair("periodDesc", periodDesc));
		postNameValuePairs.add(new BasicNameValuePair("contestantNum", "0"));

		// Get the accumulated straight team
		json = postSite(super.getHttpClientWrapper().getHost() + GET_ACCUMULATED_STRAIGHT_COMPLETE,
				postNameValuePairs,
				headerValuePairs);

		postNameValuePairs = new ArrayList<NameValuePair>(6);
		postNameValuePairs.add(new BasicNameValuePair("operation", "getAccumulatedTeam"));
		postNameValuePairs.add(new BasicNameValuePair("customerID", customerID));
		postNameValuePairs.add(new BasicNameValuePair("token", token));
		postNameValuePairs.add(new BasicNameValuePair("gameNum", Integer.toString(play305EventPackage.getGameNum())));
		postNameValuePairs.add(new BasicNameValuePair("chosenTeamID", theteam.getTeam().replace(" ", "+")));
		postNameValuePairs.add(new BasicNameValuePair("periodNumber", period));

		// Get the accumulated team
		json = postSite(super.getHttpClientWrapper().getHost() + GET_ACCUMULATED_TEAM,
				postNameValuePairs,
				headerValuePairs);

		postNameValuePairs = new ArrayList<NameValuePair>(4);
		postNameValuePairs.add(new BasicNameValuePair("operation", "validPassword"));
		postNameValuePairs.add(new BasicNameValuePair("customerID", customerID));
		postNameValuePairs.add(new BasicNameValuePair("token", token));
		postNameValuePairs.add(new BasicNameValuePair("password", super.getHttpClientWrapper().getPassword()));
				
		// Validate password
		json = postSite(super.getHttpClientWrapper().getHost() + VALID_PASSWORD,
				postNameValuePairs,
				headerValuePairs);

		postNameValuePairs = new ArrayList<NameValuePair>(10);
		postNameValuePairs.add(new BasicNameValuePair("operation", "checkWagerLine"));
		postNameValuePairs.add(new BasicNameValuePair("customerID", customerID));
		postNameValuePairs.add(new BasicNameValuePair("token", token));
		postNameValuePairs.add(new BasicNameValuePair("gameNum", Integer.toString(play305EventPackage.getGameNum())));
		postNameValuePairs.add(new BasicNameValuePair("contestantNum", ""));
		postNameValuePairs.add(new BasicNameValuePair("periodNumber", period));
		postNameValuePairs.add(new BasicNameValuePair("store", store));
		postNameValuePairs.add(new BasicNameValuePair("status", play305EventPackage.getStatus()));
		postNameValuePairs.add(new BasicNameValuePair("profile", "."));
		postNameValuePairs.add(new BasicNameValuePair("periodType", periodDesc));

		// Check wager line
		json = postSite(super.getHttpClientWrapper().getHost() + CHECK_WAGER_LINE,
				postNameValuePairs,
				headerValuePairs);
				
		LOGGER.info("Exiting selectEvent()");
		return json;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#parseEventSelection(java.lang.String, com.ticketadvantage.services.dao.sites.SiteTransaction, com.ticketadvantage.services.model.EventPackage, com.ticketadvantage.services.model.BaseRecordEvent, com.ticketadvantage.services.model.AccountEvent)
	 */
	@Override
	protected void parseEventSelection(String xhtml, SiteTransaction siteTransaction, EventPackage eventPackage, BaseRecordEvent event, AccountEvent ae) throws BatchException {
		LOGGER.info("Entering parseEventSelection()");
		
		MAP_DATA = MP.parseEventSelection(xhtml, null, null);

		LOGGER.info("Exiting parseEventSelection()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#completeTransaction(com.ticketadvantage.services.dao.sites.SiteTransaction, com.ticketadvantage.services.model.EventPackage, com.ticketadvantage.services.model.BaseRecordEvent, com.ticketadvantage.services.model.AccountEvent)
	 */
	@Override
	protected String completeTransaction(SiteTransaction siteTransaction, EventPackage eventPackage, BaseRecordEvent event, AccountEvent ae) throws BatchException {
		LOGGER.info("Entering completeTransaction()");
		LOGGER.debug("SiteTransaction: " + siteTransaction);
		LOGGER.debug("EventPackage: " + eventPackage);
		LOGGER.debug("BaseRecordEvent: " + event);
		LOGGER.debug("AccountEvent: " + ae);
		String json = null;

		// Actually process the transaction?
		if (processTransaction) {
			boolean isTeam1 = false;
			Play305TeamPackage theteam = null;
			final Play305EventPackage play305EventPackage = (Play305EventPackage)eventPackage;
			final Play305TeamPackage team1 = (Play305TeamPackage)play305EventPackage.getSiteteamone();
			final Play305TeamPackage team2 = (Play305TeamPackage)play305EventPackage.getSiteteamtwo();
			LOGGER.error("event: " + event);
			LOGGER.error("team1: " + team1);
			LOGGER.error("team2: " + team2);
			LOGGER.error("event.getRotationid(): " + event.getRotationid());

			if (event.getRotationid().toString().contains(team1.getId().toString())) {
				theteam = team1;
				isTeam1 = true;
			} else {
				theteam = team2;
			}
			LOGGER.error("theteam: " + theteam);

			String winAmount = siteTransaction.getAmount();
			String siteAmount = winAmount;
			String riskAmount = "";
			LOGGER.debug("winAmount: " + winAmount);

			// Get the spread transaction
			if (event instanceof SpreadRecordEvent) {
				Double amount = Double.parseDouble(winAmount);
				LOGGER.debug("amount: " + amount);

				if (amount != null) {
					float juice = ae.getSpreadjuice();
					final Integer accountMax = play305EventPackage.getSpreadMax();

					// User selected "risk"
					if (siteTransaction.getRiskorwin().intValue() == 1) {
						final Map<String, Double> retValue = setupRiskAmounts(amount, juice, accountMax);
						 
						winAmount = Double.toString(retValue.get("towin").doubleValue());
						siteAmount = riskAmount = Double.toString(retValue.get("risk").doubleValue());
					} else { // User selected "toWin"
						final Map<String, Double> retValue = setupToWinAmounts(amount, juice, accountMax);

						siteAmount = winAmount = Double.toString(retValue.get("towin").doubleValue());
						riskAmount = Double.toString(retValue.get("risk").doubleValue());
					}
				}
			} else if (event instanceof TotalRecordEvent) {
				Double amount = Double.parseDouble(winAmount);
				LOGGER.debug("amount: " + amount);

				if (amount != null) {
					float juice = ae.getTotaljuice();
					final Integer accountMax = play305EventPackage.getTotalMax();

					// User selected "risk"
					if (siteTransaction.getRiskorwin().intValue() == 1) {
						final Map<String, Double> retValue = setupRiskAmounts(amount, juice, accountMax);
						 
						winAmount = Double.toString(retValue.get("towin").doubleValue());
						siteAmount = riskAmount = Double.toString(retValue.get("risk").doubleValue());
					} else { // User selected "toWin"
						final Map<String, Double> retValue = setupToWinAmounts(amount, juice, accountMax);

						siteAmount = winAmount = Double.toString(retValue.get("towin").doubleValue());
						riskAmount = Double.toString(retValue.get("risk").doubleValue());
					}
				}
			} else if (event instanceof MlRecordEvent) {
				Double amount = Double.parseDouble(winAmount);
				LOGGER.debug("amount: " + amount);

				if (amount != null) {
					float juice = ae.getMljuice();
					final Integer accountMax = play305EventPackage.getMlMax();

					// User selected "risk"
					if (siteTransaction.getRiskorwin().intValue() == 1) {
						final Map<String, Double> retValue = setupRiskAmounts(amount, juice, accountMax);
						 
						winAmount = Double.toString(retValue.get("towin").doubleValue());
						siteAmount = riskAmount = Double.toString(retValue.get("risk").doubleValue());
					} else { // User selected "toWin"
						final Map<String, Double> retValue = setupToWinAmounts(amount, juice, accountMax);

						siteAmount = winAmount = Double.toString(retValue.get("towin").doubleValue());
						riskAmount = Double.toString(retValue.get("risk").doubleValue());
					}
				}
			}

			// Remove unnecessary decimals
			riskAmount = removeDecimal(riskAmount);
			winAmount = removeDecimal(winAmount);
			siteAmount = removeDecimal(siteAmount);
			siteTransaction.setAmount(siteAmount);
			ae.setActualamount(siteAmount);

			String lineType = "";
			String spread = "0";
			String totalOU = "";
			String total = "0";
			String money = "";
			String finalDecimal = "";
			String finalNumerator = "";
			String finalDenominator = "";
			String chosenTeamID = "";
			String description = "";

			if (event instanceof SpreadRecordEvent) {
				lineType = "S";
				chosenTeamID = theteam.getTeam();
				spread = MAP_DATA.get("Spread");

				if (!theteam.getTeam().equals(theteam.getFavoredTeamID())) {
					spread = spread.replace("-", "+");
				}

				if (isTeam1) {
					money = MAP_DATA.get("SpreadAdj1");
					finalDecimal = MAP_DATA.get("SpreadDecimal1");
					finalNumerator = MAP_DATA.get("SpreadNumerator1");
					finalDenominator = MAP_DATA.get("SpreadDenominator1");
				} else {
					money = MAP_DATA.get("SpreadAdj2");
					finalDecimal = MAP_DATA.get("SpreadDecimal2");
					finalNumerator = MAP_DATA.get("SpreadNumerator2");
					finalDenominator = MAP_DATA.get("SpreadDenominator2");
				}

				// 722 Baylor -3 -110
				description = theteam.getId() + " " + theteam.getTeam() + " " + spread + " " + money + " " + MAP_DATA.get("PeriodDescription");
			} else if (event instanceof TotalRecordEvent) {
				lineType = "L";
				totalOU = theteam.getGameTotalOptionIndicator().get("0").toUpperCase();
				total = MAP_DATA.get("TotalPoints");

				if (isTeam1) {
					money = MAP_DATA.get("TtlPtsAdj1");
					finalDecimal = MAP_DATA.get("TtlPointsDecimal1");
					finalNumerator = MAP_DATA.get("TtlPointsNumerator1");
					finalDenominator = MAP_DATA.get("TtlPointsDenominator1");
				} else {
					money = MAP_DATA.get("TtlPtsAdj2");
					finalDecimal = MAP_DATA.get("TtlPointsDecimal2");
					finalNumerator = MAP_DATA.get("TtlPointsNumerator2");
					finalDenominator = MAP_DATA.get("TtlPointsDenominator2");
				}

				// 698 Akron/Buffalo O 143½ -110
				description = theteam.getId() + " " + team1.getTeam() + "/" + team2.getTeam() + " " + totalOU + " " + total + " " + money + " " + MAP_DATA.get("PeriodDescription");
			} else if (event instanceof MlRecordEvent) {
				lineType = "M";

				if (isTeam1) {
					money = MAP_DATA.get("MoneyLine1");
					finalDecimal = MAP_DATA.get("MoneyLineDecimal1");
					finalNumerator = MAP_DATA.get("MoneyLineNumerator1");
					finalDenominator = MAP_DATA.get("MoneyLineDenominator1");
				} else {
					money = MAP_DATA.get("MoneyLine2");
					finalDecimal = MAP_DATA.get("MoneyLineDecimal2");
					finalNumerator = MAP_DATA.get("MoneyLineNumerator2");
					finalDenominator = MAP_DATA.get("MoneyLineDenominator2");
				}

				// 722 Baylor -110
				description = theteam.getId() + " " + theteam.getTeam() + " " + money + " " + MAP_DATA.get("PeriodDescription");
			}

			String thedate = MAP_DATA.get("ScheduleDate");
			int index = thedate.indexOf(" ");
			if (index != -1) {
				thedate = thedate.substring(0, index);
			}
			if (!spread.contains(".5")) {
				spread = removeDecimal(spread);
			}
			if (!total.contains(".5")) {
				total = removeDecimal(total);
			}
			if (!money.contains(".5")) {
				money = removeDecimal(money);
			}
			finalNumerator = removeDecimal(finalNumerator);
			finalDenominator = removeDecimal(finalDenominator);
			double docNum = Math.floor(1E9 * Math.random() + 1);

			final StringBuffer sb = new StringBuffer(500);
			sb.append("[{");
				sb.append("\"customerID\"").append(":").append("\"").append(customerID).append("\"").append(",");
				sb.append("\"docNum\"").append(":").append(docNum).append(",");
				sb.append("\"wagerType\"").append(":").append("\"S\"").append(",");
				sb.append("\"gameNum\"").append(":").append(MAP_DATA.get("GameNum")).append(",");
				sb.append("\"wagerCount\"").append(":").append("1").append(",");
				sb.append("\"gameDate\"").append(":").append("\"").append(MAP_DATA.get("GameDateTime")).append("\"").append(",");
				sb.append("\"sportType\"").append(":").append("\"").append(MAP_DATA.get("SportType")).append("\"").append(",");
				sb.append("\"sportSubType\"").append(":").append("\"").append(MAP_DATA.get("SportSubType")).append("\"").append(",");
				sb.append("\"lineType\"").append(":").append("\"").append(lineType).append("\"").append(",");
				sb.append("\"adjSpread\"").append(":").append(spread).append(",");
				sb.append("\"adjTotal\"").append(":").append(total).append(",");
				sb.append("\"totalPointsOU\"").append(":").append("\"").append(totalOU).append("\"").append(",");
				sb.append("\"priceType\"").append(":").append("\"A\"").append(",");
				sb.append("\"finalMoney\"").append(":").append(money).append(",");
				sb.append("\"finalDecimal\"").append(":").append("\"").append(finalDecimal).append("\"").append(",");
				sb.append("\"finalNumerator\"").append(":").append(finalNumerator).append(",");
				sb.append("\"finalDenominator\"").append(":").append(finalDenominator).append(",");
				sb.append("\"chosenTeamID\"").append(":").append("\"").append(chosenTeamID).append("\"").append(",");
				sb.append("\"riskAmount\"").append(":").append(riskAmount).append(",");
				sb.append("\"winAmount\"").append(":").append(winAmount).append(",");
				sb.append("\"store\"").append(":").append("\"").append(MAP_DATA.get("Store")).append("\"").append(",");
				sb.append("\"custProfile\"").append(":").append("\"").append(MAP_DATA.get("CustProfile")).append("\"").append(",");
				sb.append("\"periodNumber\"").append(":").append(MAP_DATA.get("PeriodNumber")).append(",");
				sb.append("\"periodDescription\"").append(":").append("\"").append(MAP_DATA.get("PeriodDescription")).append("\"").append(",");
				sb.append("\"oddsFlag\"").append(":").append("\"N\"").append(",");
				String lp1 = MAP_DATA.get("ListedPitcher1");
				if (lp1 == null) {
					sb.append("\"listedPitcher1\"").append(":").append("null").append(",");
				} else {
					sb.append("\"listedPitcher1\"").append(":").append("\"").append(MAP_DATA.get("ListedPitcher1")).append("\"").append(",");
				}
				sb.append("\"pitcher1ReqFlag\"").append(":").append("\"Y\"").append(",");
				String lp2 = MAP_DATA.get("ListedPitcher2");
				if (lp2 == null) {
					sb.append("\"listedPitcher2\"").append(":").append("null").append(",");
				} else {
					sb.append("\"listedPitcher2\"").append(":").append("\"").append(MAP_DATA.get("ListedPitcher2")).append("\"").append(",");
				}
				sb.append("\"pitcher2ReqFlag\"").append(":").append("\"Y\"").append(",");
				sb.append("\"percentBook\"").append(":").append("100").append(",");
				sb.append("\"volumeAmount\"").append(":").append("10000").append(",");
				sb.append("\"currencyCode\"").append(":").append("\"USD\"").append(",");
				sb.append("\"date\"").append(":").append("\"").append(thedate).append("\"").append(",");
				sb.append("\"agentID\"").append(":").append("\"").append(agentID).append("\"").append(",");
				sb.append("\"easternLine\"").append(":").append("0").append(",");
				sb.append("\"origPrice\"").append(":").append(money).append(",");
				sb.append("\"origDecimal\"").append(":").append(finalDecimal).append(",");
				sb.append("\"origNumerator\"").append(":").append(finalNumerator).append(",");
				sb.append("\"origDenominator\"").append(":").append(finalDenominator).append(",");
				sb.append("\"creditAcctFlag\"").append(":").append("\"Y\"").append(",");
				sb.append("\"wager\":{");
					sb.append("\"date\"").append(":").append("\"").append(thedate).append("\"").append(",");
					sb.append("\"minPicks\"").append(":").append("1").append(",");
					sb.append("\"totalPicks\"").append(":").append("1").append(",");
					sb.append("\"maxPayOut\"").append(":").append("0").append(",");
					sb.append("\"wagerCount\"").append(":").append("1").append(",");
					sb.append("\"riskAmount\"").append(":").append("\"").append(riskAmount).append("\"").append(",");
					sb.append("\"winAmount\"").append(":").append("\"").append(winAmount).append("\"").append(",");
					sb.append("\"description\"").append(":").append("\"").append(description).append("\"").append(",");
					sb.append("\"lineType\"").append(":").append("\"").append(lineType).append("\"").append(",");
					sb.append("\"freePlay\"").append(":").append("\"N\"").append(",");
					sb.append("\"agentID\"").append(":").append("\"").append(agentID).append("\"").append(",");
					sb.append("\"currencyCode\"").append(":").append("\"USD\"").append(",");
					sb.append("\"creditAcctFlag\"").append(":").append("\"Y\"").append(",");
					sb.append("\"playNumber\"").append(":").append("1");
				sb.append("},");
				sb.append("\"itemNumber\"").append(":").append("1").append(",");
				sb.append("\"wagerNumber\"").append(":").append("0").append(",");
				sb.append("\"origSpread\"").append(":").append(spread).append(",");
				sb.append("\"origTotal\"").append(":").append(total).append(",");
				sb.append("\"origMoney\"").append(":").append(money);
			sb.append("}]");
			LOGGER.error("list: " + sb.toString());

			final List<NameValuePair> headerValuePairs = new ArrayList<NameValuePair>(3);
			headerValuePairs.add(new BasicNameValuePair("Host", this.httpClientWrapper.getDomain()));
			headerValuePairs.add(new BasicNameValuePair("Referer", this.httpClientWrapper.getHost() + "/customer.html?v=1552460916622"));
			headerValuePairs.add(new BasicNameValuePair("X-Requested-With", "XMLHttpRequest"));

			final List<NameValuePair> postNameValuePairs = new ArrayList<NameValuePair>(5);
			postNameValuePairs.add(new BasicNameValuePair("operation", "insertWagerStraight"));
			postNameValuePairs.add(new BasicNameValuePair("customerID", customerID));
			postNameValuePairs.add(new BasicNameValuePair("token", token));
			postNameValuePairs.add(new BasicNameValuePair("agentView", "false"));
			postNameValuePairs.add(new BasicNameValuePair("list", sb.toString()));

			// Insert the wager (make the wager)
			json = postSite(super.getHttpClientWrapper().getHost() + INSERT_WAGER_STRAIGHT,
					postNameValuePairs,
					headerValuePairs);
		}

		LOGGER.info("Exiting completeTransaction()");
		return json;
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	private String removeDecimal(String value) {
		if (value != null) {
			int index = value.indexOf(".");
			if (index != -1) {
				value = value.substring(0, index);
			}
		}

		return value;
	}

	/**
	 * 
	 * @param amount
	 * @param juice
	 * @param maxAmount
	 * @return
	 */
	private Map<String, Double> setupToWinAmounts(Double amount, float juice, Integer maxAmount) {
		final Map<String, Double> retValue = new HashMap<String, Double>();
		double towin = amount.doubleValue();
		double risk = 0;

		if (juice < 0) {
			risk = amount.doubleValue() * (juice / 100);
		} else {
			risk = amount.doubleValue() * (100 / juice);	
		}

		// Check for account max
		if ((maxAmount != null) && 
			(amount.doubleValue() > maxAmount.doubleValue())) {
			final Double newAmount = new Double(maxAmount);
			towin = newAmount.doubleValue();

			// Recalculate
			if (juice < 0) {
				risk = newAmount.doubleValue() * (juice / 100);
			} else {
				risk = newAmount.doubleValue() * (100 / juice);
			}
		}

		// Setup the win and risks
		retValue.put("towin", Math.abs(towin));
		retValue.put("risk", Math.abs(risk));

		return retValue;
	}

	/**
	 * 
	 * @param amount
	 * @param juice
	 * @param maxAmount
	 * @return
	 */
	private Map<String, Double> setupRiskAmounts(Double amount, float juice, Integer maxAmount) {
		final Map<String, Double> retValue = new HashMap<String, Double>();
		double towin = 0;
		double risk = amount.doubleValue();

		// Check for good odds
		if (juice < 0) {
			towin = amount.doubleValue() * (100 / juice);
		} else {
			towin = amount.doubleValue() * (juice / 100);
		}

		// Check for account max
		if ((maxAmount != null) && 
			(risk > maxAmount.doubleValue())) {
			final Double newAmount = new Double(maxAmount);
			risk = newAmount.doubleValue();

			// Recalculate
			if (juice < 0) {
				towin = amount.doubleValue() * (100 / juice);
			} else {
				towin = amount.doubleValue() * (juice / 100);
			}
		}

		// Setup the win and risks
		retValue.put("towin", Math.abs(towin));
		retValue.put("risk", Math.abs(risk));

		return retValue;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#parseTicketTransaction(java.lang.String)
	 */
	@Override
	protected String parseTicketTransaction(String xhtml) throws BatchException {
		LOGGER.info("Entering parseTicketNumber()");

		final String ticketNumber = MP.parseTicketNumber(xhtml);

		LOGGER.info("Exiting parseTicketNumber()");
		return ticketNumber;		
	}

	/**
	 * 
	 * @param addSave
	 * @param xhtml
	 * @param event
	 * @param ae
	 * @return
	 * @throws BatchException
	 */
	protected String processLineChange(String addSave, String xhtml, BaseRecordEvent event, AccountEvent ae) throws BatchException {
		LOGGER.info("Entering processLineChange()");

		final Map<String, String> lineChanges = MP.processLineChange(xhtml);
		if (lineChanges != null && !lineChanges.isEmpty()) {
			if (event instanceof SpreadRecordEvent) {
				final SpreadRecordEvent sre = (SpreadRecordEvent)event;
				if (determineSpreadLineChange(sre, ae, lineChanges)) {
					if (addSave.equals("AddBet")) {
//						xhtml = addBet(ae.getActualamount());
					} else {
//						xhtml = saveBet(ae.getActualamount());
					}
					// Access Token
//					accessToken = getAccessToken();
				} else {
					throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed", xhtml);
				}
			} else if (event instanceof TotalRecordEvent) {
				final TotalRecordEvent tre = (TotalRecordEvent)event;

				// Set the over/under
				if (tre.getTotalinputfirstone() != null && tre.getTotalinputfirstone().length() > 0) {
					lineChanges.put("valueindicator", "o");
				} else {
					lineChanges.put("valueindicator", "u");
				}

				if (determineTotalLineChange(tre, ae, lineChanges)) {
					if (addSave.equals("AddBet")) {
//						xhtml = addBet(ae.getActualamount());
					} else {
//						xhtml = saveBet(ae.getActualamount());
					}
					// Access Token
//					accessToken = getAccessToken();
				} else {
					throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed", xhtml);
				}
			} else if (event instanceof MlRecordEvent) {
				final MlRecordEvent mre = (MlRecordEvent)event;
				
				if (determineMlLineChange(mre, ae, lineChanges)) {
					if (addSave.equals("AddBet")) {
//						xhtml = addBet(ae.getActualamount());
					} else {
//						xhtml = saveBet(ae.getActualamount());
					}
					// Access Token
//					accessToken = getAccessToken();
				} else {
					throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed", xhtml);
				}
			}
		}

		LOGGER.info("Exiting processLineChange()");
		return xhtml;
	}
}