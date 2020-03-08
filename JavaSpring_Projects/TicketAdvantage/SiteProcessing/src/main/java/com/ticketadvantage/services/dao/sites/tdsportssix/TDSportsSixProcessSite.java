/**
 * 
 */
package com.ticketadvantage.services.dao.sites.tdsportssix;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.sites.tdsports.TDSportsProcessSite;
import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.EventPackage;
import com.ticketadvantage.services.model.TotalRecordEvent;

/**
 * @author jmiller
 *
 */
public class TDSportsSixProcessSite extends TDSportsProcessSite {
	private static final Logger LOGGER = Logger.getLogger(TDSportsSixProcessSite.class);
	protected final TDSportsSixParser TDP = new TDSportsSixParser();

	/**
	 * 
	 */
	public TDSportsSixProcessSite(String host, String username, String password, boolean isMobile, boolean showRequestResponse) {
		super("TDSportsSix", host, username, password, isMobile, showRequestResponse);
		LOGGER.info("Entering TDSportsSixProcessSite()");

		// Setup the parser
		this.siteParser = TDP;

		// Menu items
		NFL_LINES_SPORT = new String[] { "NFL FOOTBALL", "FOOTBALL" };
		NFL_LINES_NAME = new String[] { "NFL" };
		NFL_FIRST_SPORT = new String[] { "NFL FOOTBALL", "FOOTBALL" };
		NFL_FIRST_NAME = new String[] { "NFL - FIRST HALFS" };
		NFL_SECOND_SPORT = new String[] { "NFL FOOTBALL", "FOOTBALL" };
		NFL_SECOND_NAME = new String[] { "NFL - SECOND HALFS" };
		NCAAF_LINES_SPORT = new String[] { "NCAA FOOTBALL", "FOOTBALL" };
		NCAAF_LINES_NAME = new String[] { "NCAA FOOTBALL - MEN" };
		NCAAF_FIRST_SPORT = new String[] { "NCAA FOOTBALL", "FOOTBALL" };
		NCAAF_FIRST_NAME = new String[] { "1ST HALF NCAA FOOTBALL" };
		NCAAF_SECOND_SPORT = new String[] { "NCAA FOOTBALL", "FOOTBALL" };
		NCAAF_SECOND_NAME = new String[] { "2ND HALF NCAA FOOTBALL" };
		NBA_LINES_SPORT = new String[] { "BASKETBALL" };
		NBA_LINES_NAME = new String[] { "NBA" };
		NBA_FIRST_SPORT = new String[] { "BASKETBALL" };
		NBA_FIRST_NAME = new String[] { "NBA 1ST HALFS", "NBA (1ST HALF)" };
		NBA_SECOND_SPORT = new String[] { "BASKETBALL" };
		NBA_SECOND_NAME = new String[] { "NBA 2ND HALFS" };
		NCAAB_LINES_SPORT = new String[] { "BASKETBALL" };
		NCAAB_LINES_NAME = new String[] { "NCAA BASKETBALL - MEN" };
		NCAAB_FIRST_SPORT = new String[] { "BASKETBALL" };
		NCAAB_FIRST_NAME = new String[] { "NCAA BASKETBALL - 1H" };
		NCAAB_SECOND_SPORT = new String[] { "BASKETBALL" };
		NCAAB_SECOND_NAME = new String[] { "NCAA BASKETBALL - 2H" };
		NHL_LINES_SPORT = new String[] { "HOCKEY" };
		NHL_LINES_NAME = new String[] { "NATIONAL HOCKEY LEAGUE" };
		NHL_FIRST_SPORT = new String[] { "HOCKEY" };
		NHL_FIRST_NAME = new String[] { "NHL 1ST HALFS" };
		NHL_SECOND_SPORT = new String[] { "HOCKEY" };
		NHL_SECOND_NAME = new String[] { "NHL 2ND HALFS" };
		NHL_THIRD_SPORT = new String[] { "HOCKEY" };
		NHL_THIRD_NAME = new String[] { "NHL 3RD HALFS" };
		WNBA_LINES_SPORT = new String[] { "BASKETBALL" };
		WNBA_LINES_NAME = new String[] { "WNBA" };
		WNBA_FIRST_SPORT = new String[] { "BASKETBALL" };
		WNBA_FIRST_NAME = new String[] { "WNBA 1st Half" };
		WNBA_SECOND_SPORT = new String[] { "BASKETBALL" };
		WNBA_SECOND_NAME = new String[] { "WNBA 2nd Half" };
		MLB_LINES_SPORT = new String[] { "BASEBALL" };
		MLB_LINES_NAME = new String[] { "MAJOR LEAGUE BASEBALL" };
		MLB_FIRST_SPORT = new String[] { "BASEBALL" };
		MLB_FIRST_NAME = new String[] { "MLB 1ST HALFS" };
		MLB_SECOND_SPORT = new String[] { "BASEBALL" };
		MLB_SECOND_NAME = new String[] { "MLB 2ND HALFS" };

		LOGGER.info("Exiting TDSportsSixProcessSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final String[][] TDSITES = new String [][] { 
				// { "http://www.betgrande.com", "7265", "jd", "2000", "2000", "2000", "Los Angeles", "ET"}
				// { "http://www.betgrande.com", "651", "tunes", "2000", "2000", "2000", "New York City", "ET"}
				// { "http://ezplay2001.com/", "gold124", "max", "1", "1", "1", "Phoenix", "ET"}
				{ "http://ezplay2001.com/", "G9911", "grey", "1", "1", "1", "None", "ET"}
			};

			// Loop through the sites
			for (int i = 0; i < TDSITES.length; i++) {
				final TDSportsSixProcessSite processSite = new TDSportsSixProcessSite(TDSITES[i][0], TDSITES[i][1],
						TDSITES[i][2], false, true);
			    processSite.httpClientWrapper.setupHttpClient(TDSITES[0][6]);
			    processSite.processTransaction = false;
			    processSite.timezone = TDSITES[0][7];

			    String xhtml = processSite.loginToSite(TDSITES[i][1], TDSITES[i][2]);

			    final Map<String, String> map = processSite.getMenuFromSite("ncaaflines", xhtml);
			    LOGGER.debug("map: " + map);

				xhtml = processSite.selectSport("ncaaflines");
				LOGGER.debug("XHTML: " + xhtml);

				final Iterator<EventPackage> ep = processSite.parseGamesOnSite("ncaaflines", xhtml);   

				final TotalRecordEvent mre = new TotalRecordEvent();
				mre.setTotalinputfirstone("");
				mre.setId(new Long(397));
				mre.setEventname("1H STL CARDINALS [M. MIKOLAS -R]");
				mre.setEventtype("ml");
				mre.setSport("ncaaflines"); 
				mre.setUserid(new Long(6));
				mre.setEventid(new Integer(397));
				mre.setEventid1(new Integer(397));
				mre.setEventid2(new Integer(398));
				mre.setRotationid(new Integer(397));

/*
				final SpreadRecordEvent mre = new SpreadRecordEvent();
				mre.setSpreadinputfirstone("");
				mre.setSpreadplusminusfirstone("");
				mre.setId(new Long(501));
				mre.setEventname("1H STL CARDINALS [M. MIKOLAS -R]");
				mre.setEventtype("ml");
				mre.setSport("nbalines"); 
				mre.setUserid(new Long(6));
				mre.setEventid(new Integer(501));
				mre.setEventid1(new Integer(501));
				mre.setEventid2(new Integer(502));
				mre.setRotationid(new Integer(501));
*/
/*
				final MlRecordEvent mre = new MlRecordEvent();
				mre.setMlinputfirstone("");
				mre.setMlplusminusfirstone("");
				mre.setId(new Long(951));
				mre.setEventname("1H STL CARDINALS [M. MIKOLAS -R]");
				mre.setEventtype("ml");
				mre.setSport("nbalines"); 
				mre.setUserid(new Long(6));
				mre.setEventid(new Integer(951));
				mre.setEventid1(new Integer(951));
				mre.setEventid2(new Integer(952));
				mre.setRotationid(new Integer(951));
*/

				// Step 9 - Find event
				EventPackage eventPackage = processSite.findEvent(ep, mre);
				if (eventPackage == null) {
					throw new BatchException(BatchErrorCodes.GAME_NOT_AVAILABLE, BatchErrorMessage.GAME_NOT_AVAILABLE, "Game cannot be found on site for " + mre.getEventname(), xhtml);
				}
				LOGGER.error("eventPackage: " + eventPackage);
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
		TDP.setTimezone(timezone);

		// Call the second page
		List<NameValuePair> retValue = httpClientWrapper.getSitePage(httpClientWrapper.getHost(), null, setupHeader(false));
		retValue = httpClientWrapper.getSitePage(httpClientWrapper.getHost(), null, setupHeader(false));
		String xhtml = httpClientWrapper.getCookiesAndXhtml(retValue);
		LOGGER.debug("XHTML: " + xhtml);

		retValue = httpClientWrapper.checkForRedirect(retValue, httpClientWrapper.getHost(), setupHeader(false), null, httpClientWrapper.getWebappname() + "/");
		xhtml = httpClientWrapper.getCookiesAndXhtml(retValue);
		LOGGER.debug("XHTML: " + xhtml);

		// Get home page data
		MAP_DATA = TDP.parseIndex(xhtml);
		LOGGER.debug("Map: " + MAP_DATA);

		// Setup the username/password data
		setUsernamePassword(username, password);

		List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
		String actionLogin = setupNameValuesEmpty(postValuePairs, MAP_DATA, "");
		LOGGER.debug("ActionLogin: " + actionLogin);

		// Call the login
		xhtml = authenticate(actionLogin, postValuePairs);
		LOGGER.debug("XHTML: " + xhtml);

		// Setup the webapp name
		httpClientWrapper.setWebappname(determineWebappName(httpClientWrapper.getPreviousUrl()));

		// Parse login
		MAP_DATA = TDP.parseLogin(xhtml);
		LOGGER.debug("MAP_DATA: " + MAP_DATA);
		
		// Get the main page
		xhtml = getSite(populateUrl("CreateSports.aspx?WT=0"));

		LOGGER.info("Exiting loginToSite()");
		return xhtml;
	}
}