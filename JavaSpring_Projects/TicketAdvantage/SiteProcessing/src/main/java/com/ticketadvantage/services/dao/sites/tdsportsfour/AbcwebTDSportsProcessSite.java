/**
 * 
 */
package com.ticketadvantage.services.dao.sites.tdsportsfour;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.sites.tdsports.TDSportsProcessSite;
import com.ticketadvantage.services.errorhandling.BatchException;

/**
 * @author jmiller
 *
 */
public class AbcwebTDSportsProcessSite extends TDSportsProcessSite {
	private static final Logger LOGGER = Logger.getLogger(AbcwebTDSportsProcessSite.class);
	protected AbcwebTDSportsParser ATDSP = new AbcwebTDSportsParser();

	/**
	 * 
	 */
	public AbcwebTDSportsProcessSite(String host, String username, String password, boolean isMobile, boolean showRequestResponse) {
		super("AbcwebTDSports", host, username, password, isMobile, showRequestResponse);
		LOGGER.info("Entering AbcwebTDSportsProcessSite()");

		// Setup the parser
		this.siteParser = ATDSP;

		// Menu items
		NFL_LINES_SPORT = new String[] { "FOOTBALL GAME LINES" };
		NFL_LINES_NAME = new String[] { "NFL", "NFL - PRESEASON" };
		NFL_FIRST_SPORT = new String[] { "FOOTBALL GAME LINES" };
		NFL_FIRST_NAME = new String[] { "NFL (1H)", "NFL - 1ST HALVES" };
		NFL_SECOND_SPORT = new String[] { "FOOTBALL GAME LINES" };
		NFL_SECOND_NAME = new String[] { "NFL (2H)", "NFL - 2ND HALVES" };
		NCAAF_LINES_SPORT = new String[] { "FOOTBALL GAME LINES" };
		NCAAF_LINES_NAME = new String[] { "NCAA FOOTBALL - MEN" };
		NCAAF_FIRST_SPORT = new String[] { "FOOTBALL GAME LINES" };
		NCAAF_FIRST_NAME = new String[] { "NCAA FOOTBALL (1H)", "NCAA FOOTBALL - MEN (1H)", "NCAA FOOTBALL MEN 1ST HALVES" };
		NCAAF_SECOND_SPORT = new String[] { "FOOTBALL GAME LINES" };
		NCAAF_SECOND_NAME = new String[] { "NCAA FOOTBALL (2H)", "NCAA FOOTBALL - MEN (2H)", "NCAA FOOTBALL MEN 2ND HALVES" };
		NBA_LINES_SPORT = new String[] { "BASKETBALL" };
		NBA_LINES_NAME = new String[] { "NBA", "NBA - PRESEASON" };
		NBA_FIRST_SPORT = new String[] { "BASKETBALL" };
		NBA_FIRST_NAME = new String[] { "NBA (1H)", "NCAA BASKETBALL (1H) ADDED GAMES", "NBA - 1ST HALVES"};
		NBA_SECOND_SPORT = new String[] { "BASKETBALL" };
		NBA_SECOND_NAME = new String[] { "NBA (2H)" };
		NCAAB_LINES_SPORT = new String[] { "BASKETBALL" };
		NCAAB_LINES_NAME = new String[] { "NCAA BASKETBALL - MEN", "NCAA BASKETBALL - ADDED GAMES", "NCAA BASKETBALL - EXTRA GAMES" };
		NCAAB_FIRST_SPORT = new String[] { "BASKETBALL" };
		NCAAB_FIRST_NAME = new String[] { "NCAA BASKETBALL (1H)" };
		NCAAB_SECOND_SPORT = new String[] { "BASKETBALL" };
		NCAAB_SECOND_NAME = new String[] { "NCAA BASKETBALL (2H)", "NCAA BASKETBALL (2H) ADDED GAMES" };
		NHL_LINES_SPORT = new String[] { "ICE HOCKEY" };
		NHL_LINES_NAME = new String[] { "NATIONAL HOCKEY LEAGUE", "NHL - PRESEASON" };
		NHL_FIRST_SPORT = new String[] { "ICE HOCKEY" };
		NHL_FIRST_NAME = new String[] { "NHL (1P)" };
		NHL_SECOND_SPORT = new String[] { "ICE HOCKEY" };
		NHL_SECOND_NAME = new String[] { "NHL (2P)" };
		NHL_THIRD_SPORT = new String[] { "ICE HOCKEY" };
		NHL_THIRD_NAME = new String[] { "NHL (3P)" };
		WNBA_LINES_NAME = new String[] { "WNBA" };
		WNBA_LINES_SPORT = new String[] { "BASKETBALL" };
		WNBA_FIRST_NAME = new String[] { "WNBA (1H)" };
		WNBA_FIRST_SPORT = new String[] { "BASKETBALL" };
		WNBA_SECOND_NAME = new String[] { "WNBA (2H)" };
		WNBA_SECOND_SPORT = new String[] { "BASKETBALL" };
		MLB_LINES_NAME = new String[] { "MAJOR LEAGUE BASEBALL" };
		MLB_LINES_SPORT = new String[] { "BASEBALL GAME LINES" };
		MLB_FIRST_NAME = new String[] { "MLB - 1ST 5 FULL INNINGS" };
		MLB_FIRST_SPORT = new String[] { "BASEBALL GAME LINES" };
		MLB_SECOND_NAME = new String[] { "MLB - LAST 4 INNINGS" };
		MLB_SECOND_SPORT = new String[] { "BASEBALL GAME LINES" };

		LOGGER.info("Exiting AbcwebTDSportsProcessSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final String[][] TDSITES = new String [][] { 
//				{ "http://www.abcweb.ag", "lx694", "CA", "2000", "2000", "2000", "Los Angeles", "ET"}
//				{ "http://whq.ag", "k678", "PONY23", "500", "500", "500", "Los Angeles", "ET"}
//				{ "https://betongamedaysports.com", "ad364", "der", "0", "0", "0", "None", "ET"}
//				{ "http://whq.ag", "AE146", "WALL6", "500", "500", "500", "New York", "ET"}
//				{ "http://whq.ag", "AE201", "NEWYORK", "500", "500", "500", "None", "ET"}
//				{ "http://abcweb.ag", "ts206", "27lg", "100", "100", "100", "None", "ET"}
//				{ "http://nastybets.win/", "win263", "3333", "0", "0", "0", "None", "ET"}
				{ "http://whq.ag", "pad021", "ADAM1", "500", "500", "500", "None", "ET"}
			};

			// Loop through the sites
			for (int i = 0; i < TDSITES.length; i++) {
				final AbcwebTDSportsProcessSite processSite = new AbcwebTDSportsProcessSite(TDSITES[i][0], TDSITES[i][1],
						TDSITES[i][2], false, true);
				
			    processSite.httpClientWrapper.setupHttpClient(TDSITES[0][6]);
			    processSite.processTransaction = false;
			    processSite.timezone = TDSITES[0][7];
			    processSite.testTotal(processSite, TDSITES);

/*
			    String xhtml = processSite.loginToSite(TDSITES[i][1], TDSITES[i][2]);

				Set<PendingEvent> pendingEvents = processSite.getPendingBets(TDSITES[0][0], TDSITES[0][1], null);
				if (pendingEvents != null && !pendingEvents.isEmpty()) {
					Iterator<PendingEvent> itr = pendingEvents.iterator();
					while (itr.hasNext()) {
						System.out.println("PendingEvent: " + itr.next());
					}
				}
*/
/*
			    Map<String, String> map = processSite.getMenuFromSite("ncaablines", xhtml);
			    LOGGER.debug("map: " + map);

				xhtml = processSite.selectSport("ncaablines");
				LOGGER.debug("XHTML: " + xhtml);

				final Iterator<EventPackage> ep = processSite.parseGamesOnSite("ncaablines", xhtml);   

				TotalRecordEvent mre = new TotalRecordEvent();
				mre.setAmount("100");
				mre.setTotalinputfirstone("143");
				mre.setTotaljuiceplusminusfirstone("-");
				mre.setTotalinputjuicefirstone("120");
				mre.setId(new Long(307669));
				mre.setEventname("Kansas State");
				mre.setEventtype("total");
				mre.setSport("ncaablines"); 
				mre.setUserid(new Long(6));
				mre.setEventid(new Integer(307669));
				mre.setEventid1(new Integer(307669));
				mre.setEventid2(new Integer(307670));
				mre.setRotationid(new Integer(307669));
				mre.setWtype("2");

				// Step 9 - Find event
				EventPackage eventPackage = processSite.findEvent(ep, mre);

				if (eventPackage == null) {
					throw new BatchException(BatchErrorCodes.GAME_NOT_AVAILABLE, BatchErrorMessage.GAME_NOT_AVAILABLE, "Game cannot be found on site for " + mre.getEventname(), xhtml);
				}
				LOGGER.error("eventPackage: " + eventPackage);

				final AccountEvent ae = new AccountEvent();
				ae.setAccountid(new Long(43));
				ae.setAmount("100");
				ae.setEventid(307669);
				ae.setEventdatetime(new Date());
				ae.setEventname("145 over");
				ae.setGroupid(new Long(-99));
				ae.setIscompleted(false);
				ae.setMaxtotalamount(100);
				ae.setTimezone("ET");
				ae.setName("Abcweb");
				ae.setOwnerpercentage(100);
				ae.setPartnerpercentage(0);
				ae.setProxy("None");
				ae.setSport("ncaaflines");
				ae.setTotal(new Float(143));
				ae.setTotalid(new Long(307669));
				ae.setTotalindicator("o");
				ae.setTotaljuice(new Float(-110));
				ae.setType("total");
				ae.setUserid(new Long(1));
				ae.setWagertype("2");
				ae.setRiskamount("110");
				ae.setTowinamount("100");

				// Step 10 - Setup site transaction
				final SiteTransaction siteTransaction = processSite.getTotalTransaction(mre, eventPackage, ae);

				xhtml = processSite.selectEvent(siteTransaction, eventPackage, mre, ae);
				LOGGER.debug("xhtml: " + xhtml);

				//
				// Step 13 - Parse the event selection
				//
				processSite.parseEventSelection(xhtml, siteTransaction, eventPackage, mre, ae);

				//
				// Call the Complete Wager
				//
				// Step 14 - Complete transaction
				//
				xhtml = processSite.completeTransaction(siteTransaction, eventPackage, mre, ae);
*/
/*
				SpreadRecordEvent mre = new SpreadRecordEvent();
				mre.setSpreadinputfirstone("14.5");
				mre.setSpreadinputjuicefirstone("110");
				mre.setId(new Long(387));
				mre.setEventname("Iowa State");
				mre.setEventtype("spread");
				mre.setSport("ncaaflines"); 
				mre.setUserid(new Long(6));
				mre.setEventid(new Integer(387));
				mre.setEventid1(new Integer(387));
				mre.setEventid2(new Integer(388));
				mre.setRotationid(new Integer(387));
*/
/*			
				MlRecordEvent mre = new MlRecordEvent();
				mre.setMlinputfirstone("");
				mre.setMlplusminusfirstone("");
				mre.setId(new Long(951));
				mre.setEventname("1H STL CARDINALS [M. MIKOLAS -R]");
				mre.setEventtype("ml");
				mre.setSport("mlblines"); 
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
			}
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
		this.siteParser.setTimezone(timezone);
		ATDSP.setTimezone(timezone);
		final String host = httpClientWrapper.getHost();

		MAP_DATA = new HashMap<String, String>();
		MAP_DATA.put("Account", username);
		MAP_DATA.put("Password", password.toUpperCase());
		MAP_DATA.put("login", "Log In");
//		MAP_DATA.put("IdBook", "");

		// Setup the username/password data
		final List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
		setupNameValuesEmpty(postValuePairs, MAP_DATA, "");

		// Call the login
		LOGGER.debug("HTTP POST REQUEST: " + host + "/DGS/login.aspx");
		String xhtml = authenticate(host + "/DGS/login.aspx", postValuePairs);
		LOGGER.debug("HTML: " + xhtml);

		// Setup the webapp name
		httpClientWrapper.setWebappname("/DGS/wager");

		LOGGER.debug("HTTP GET REQUEST: " + populateUrl("CreateSports.aspx?WT=0"));
		xhtml = getSite(populateUrl("CreateSports.aspx?WT=0"));
		LOGGER.debug("HTML: " + xhtml);

		LOGGER.info("Exiting loginToSite()");
		return xhtml;
	}
}