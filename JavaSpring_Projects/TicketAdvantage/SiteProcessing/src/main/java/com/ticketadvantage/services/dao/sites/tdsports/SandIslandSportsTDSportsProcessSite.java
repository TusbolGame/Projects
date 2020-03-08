/**
 * 
 */
package com.ticketadvantage.services.dao.sites.tdsports;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.log4j.Logger;

import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.EventPackage;
import com.ticketadvantage.services.model.MlRecordEvent;

/**
 * @author jmiller
 *
 */
public class SandIslandSportsTDSportsProcessSite extends TDSportsProcessSite {
	private static final Logger LOGGER = Logger.getLogger(SandIslandSportsTDSportsProcessSite.class);
	protected SandIslandSportsTDSportsParser TDP = new SandIslandSportsTDSportsParser();

	/**
	 * 
	 */
	public SandIslandSportsTDSportsProcessSite(String host, String username, String password, boolean isMobile, boolean showRequestResponse) {
		super("SandIslandSportsTDSports", host, username, password, isMobile, showRequestResponse);
		LOGGER.info("Entering SandIslandSportsTDSportsProcessSite()");

		// Setup the parser
		super.siteParser = TDP;

		// Menu items
		NFL_LINES_SPORT = new String[] { "NFL FOOTBALL" };
		NFL_LINES_NAME = new String[] { "NFL GAME LINES" };
		NFL_FIRST_SPORT = new String[] { "NFL FOOTBALL" };
		NFL_FIRST_NAME = new String[] { "NFL 1ST HALF" };
		NFL_SECOND_SPORT = new String[] { "NFL FOOTBALL" };
		NFL_SECOND_NAME = new String[] { "NFL 2ND HALF" };
		NCAAF_LINES_SPORT = new String[] { "COLLEGE FOOTBALL" };
		NCAAF_LINES_NAME = new String[] { "NCAA FOOTBALL GAME LINES" };
		NCAAF_FIRST_SPORT = new String[] { "COLLGE FOOTBALL" };
		NCAAF_FIRST_NAME = new String[] { "NCAA FOOTBALL 1ST HALF" };
		NCAAF_SECOND_SPORT = new String[] { "COLLGE FOOTBALL" };
		NCAAF_SECOND_NAME = new String[] { "NCAA FOOTBALL 2ND HALF" };
		NBA_LINES_SPORT = new String[] { "NBA" };
		NBA_LINES_NAME = new String[] { "NBA" };
		NBA_FIRST_SPORT = new String[] { "NBA" };
		NBA_FIRST_NAME = new String[] { "NBA 1ST HALF" };
		NBA_SECOND_SPORT = new String[] { "NBA" };
		NBA_SECOND_NAME = new String[] { "NBA 2ND HALF" };
		NCAAB_LINES_SPORT = new String[] { "COLLEGE BASKETBALL" };
		NCAAB_LINES_NAME = new String[] { "NCAA BASKETBALL GAME LINES" };
		NCAAB_FIRST_SPORT = new String[] { "COLLEGE BASKETBALL" };
		NCAAB_FIRST_NAME = new String[] { "NCAA BASKETBALL 1ST HALF" };
		NCAAB_SECOND_SPORT = new String[] { "COLLEGE BASKETBALL" };
		NCAAB_SECOND_NAME = new String[] { "NCAA BASKETBALL 2ND HALF" };
		NHL_LINES_SPORT = new String[] { "HOCKEY" };
		NHL_LINES_NAME = new String[] { "NHL GAME LINES" };
		NHL_FIRST_SPORT = new String[] { "HOCKEY" };
		NHL_FIRST_NAME = new String[] { "" };
		NHL_SECOND_SPORT = new String[] { "HOCKEY" };
		NHL_SECOND_NAME = new String[] { "" };
		WNBA_LINES_SPORT = new String[] { "WNBA" };
		WNBA_LINES_NAME = new String[] { "WNBA" };
		WNBA_FIRST_SPORT = new String[] { "WNBA" };
		WNBA_FIRST_NAME = new String[] { "" };
		WNBA_SECOND_SPORT = new String[] { "WNBA" };
		WNBA_SECOND_NAME = new String[] { "" };
		MLB_LINES_SPORT = new String[] { "MLB" };
		MLB_LINES_NAME = new String[] { "MLB - GAME LINES", "MLB GAME LINES" };
		MLB_FIRST_SPORT = new String[] { "MLB" };
		MLB_FIRST_NAME = new String[] { "" };
		MLB_SECOND_SPORT = new String[] { "MLB" };
		MLB_SECOND_NAME = new String[] { "" };

		LOGGER.info("Exiting SandIslandSportsTDSportsProcessSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final String[][] TDSITES = new String [][] { 
				{ "http://888east.com/", "JJ103", "JJ103", "100", "100", "100", "New York", "ET"}
			};

			// Loop through the sites
			for (int i = 0; i < TDSITES.length; i++) {
				final SandIslandSportsTDSportsProcessSite processSite = new SandIslandSportsTDSportsProcessSite(TDSITES[i][0], TDSITES[i][1],
						TDSITES[i][2], false, true);
			    processSite.httpClientWrapper.setupHttpClient(TDSITES[0][6]);
			    processSite.processTransaction = false;
			    processSite.timezone = TDSITES[0][7];
			    processSite.siteParser = processSite.TDP;
			    processSite.siteParser.setTimezone(TDSITES[0][7]);
			    processSite.TDP.setTimezone(TDSITES[0][7]);

			    String xhtml = processSite.loginToSite(TDSITES[i][1], TDSITES[i][2]);

			    Map<String, String> map = processSite.getMenuFromSite("mlblines", xhtml);
			    LOGGER.debug("map: " + map);
				xhtml = processSite.selectSport("mlblines");
				LOGGER.debug("XHTML: " + xhtml);
				final Iterator<EventPackage> ep = processSite.parseGamesOnSite("mlblines", xhtml);   
			
				MlRecordEvent mre = new MlRecordEvent();
				mre.setMlinputfirstone("");
				mre.setMlplusminusfirstone("");
				mre.setId(new Long(909));
				mre.setEventname("909 MIL BREWERS");
				mre.setEventtype("ml");
				mre.setSport("mllines"); 
				mre.setUserid(new Long(6));
				mre.setEventid(new Integer(909));
				mre.setEventid1(new Integer(909));
				mre.setEventid2(new Integer(910));
				
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
		super.siteParser.setTimezone(timezone);
		TDP.setTimezone(timezone);
		super.TDP = this.TDP;

		// Call the second page
		List<NameValuePair> retValue = httpClientWrapper.getSitePage(httpClientWrapper.getHost() + "/default.aspx", null, setupHeader(false));
		String xhtml = httpClientWrapper.getCookiesAndXhtml(retValue);
		LOGGER.debug("XHTML: " + xhtml);

		retValue = httpClientWrapper.checkForRedirect(retValue, httpClientWrapper.getHost(), setupHeader(false), null, httpClientWrapper.getWebappname() + "/");
		xhtml = httpClientWrapper.getCookiesAndXhtml(retValue);
		LOGGER.debug("XHTML: " + xhtml);

		// Get home page data
		MAP_DATA = TDP.parseIndex(xhtml);

		// Setup the username/password data
		setUsernamePassword(username, password);		
		LOGGER.debug("Map: " + MAP_DATA);

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

		// Go directly into the menu page
		xhtml = getSite(populateUrl("CreateSports.aspx?WT=0"));

		LOGGER.info("Exiting loginToSite()");
		return xhtml;
	}
}