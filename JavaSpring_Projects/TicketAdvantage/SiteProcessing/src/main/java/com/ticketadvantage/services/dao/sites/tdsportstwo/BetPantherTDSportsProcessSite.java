/**
 * 
 */
package com.ticketadvantage.services.dao.sites.tdsportstwo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.sites.tdsports.TDSportsProcessSite;
import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.EventPackage;
import com.ticketadvantage.services.model.MlRecordEvent;

/**
 * @author jmiller
 *
 */
public class BetPantherTDSportsProcessSite extends TDSportsProcessSite {
	private static final Logger LOGGER = Logger.getLogger(BetPantherTDSportsProcessSite.class);
	protected BetPantherTDSportsParser BSP = new BetPantherTDSportsParser();

	/**
	 * 
	 */
	public BetPantherTDSportsProcessSite(String host, String username, String password, boolean isMobile, boolean showRequestResponse) {
		super("BetPantherTDSports", host, username, password, isMobile, showRequestResponse);
		LOGGER.info("Entering BetPantherTDSportsProcessSite()");

		// Setup the parser
		this.siteParser = BSP;

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
		NBA_LINES_SPORT = new String[] { "BASKETBALL GAME LINES" };
		NBA_LINES_NAME = new String[] { "NBA - GAME LINES" };
		NBA_FIRST_SPORT = new String[] { "BASKETBALL GAME LINES" };
		NBA_FIRST_NAME = new String[] { "NBA - 1ST HALVES" };
		NBA_SECOND_SPORT = new String[] { "BASKETBALL GAME LINES" };
		NBA_SECOND_NAME = new String[] { "NBA - 2ND HALVES" };
		NCAAB_LINES_SPORT = new String[] { "BASKETBALL GAME LINES" };
		NCAAB_LINES_NAME = new String[] { "NCAA BASKETBALL - MEN" };
		NCAAB_FIRST_SPORT = new String[] { "BASKETBALL GAME LINES" };
		NCAAB_FIRST_NAME = new String[] { "NCAA BASKETBALL - (1H)" };
		NCAAB_SECOND_SPORT = new String[] { "BASKETBALL GAME LINES" };
		NCAAB_SECOND_NAME = new String[] { "NCAA BASKETBALL - (2H)" };
		NHL_LINES_SPORT = new String[] { "HOCKEY GAME LINES" };
		NHL_LINES_NAME = new String[] { "NHL - GAME LINES" };
		NHL_FIRST_SPORT = new String[] { "HOCKEY GAME LINES" };
		NHL_FIRST_NAME = new String[] { "NHL - PERIOD LINES" };
		NHL_SECOND_SPORT = new String[] { "HOCKEY GAME LINES" };
		NHL_SECOND_NAME = new String[] { "NHL - PERIOD LINES" };
		WNBA_LINES_NAME = new String[] { "WNBA" };
		WNBA_LINES_SPORT = new String[] { "Basketball" };
		WNBA_FIRST_NAME = new String[] { "WNBA 1st Half" };
		WNBA_FIRST_SPORT = new String[] { "Basketball" };
		WNBA_SECOND_NAME = new String[] { "WNBA 2nd Half" };
		WNBA_SECOND_SPORT = new String[] { "Basketball" };
		MLB_LINES_SPORT = new String[] { "BASEBALL GAME LINES" };
		MLB_LINES_NAME = new String[] { "MLB - GAME LINES", "MLB - EXHIBITION GAME LINES" };
		MLB_FIRST_SPORT = new String[] { "BASEBALL GAME LINES" };
		MLB_FIRST_NAME = new String[] { "MLB - 1ST HALVES (5 FULL INNINGS)" };
		MLB_SECOND_SPORT = new String[] { "BASEBALL GAME LINES" };
		MLB_SECOND_NAME = new String[] { "MLB - 2ND HALVES (4 INNINGS+EXTRA INNS)" };

		LOGGER.info("Exiting BetPantherTDSportsProcessSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final String[][] TDSITES = new String [][] { 
				{ "http://www.betpanther.com/", "BO08", "dude", "250", "250", "250", "Los Angeles", "PT"}
			};

			// Loop through the sites
			for (int i = 0; i < TDSITES.length; i++) {
				final BetPantherTDSportsProcessSite processSite = new BetPantherTDSportsProcessSite(TDSITES[i][0], TDSITES[i][1],
						TDSITES[i][2], false, true);
			    processSite.httpClientWrapper.setupHttpClient(TDSITES[0][6]);
			    processSite.processTransaction = false;
			    processSite.timezone = TDSITES[0][7];
			    processSite.siteParser = processSite.TDP = processSite.BSP;
			    processSite.siteParser.setTimezone(TDSITES[0][7]);
			    processSite.BSP.setTimezone(TDSITES[0][7]);

			    String xhtml = processSite.loginToSite(TDSITES[i][1], TDSITES[i][2]);

			    Map<String, String> map = processSite.getMenuFromSite("mlblines", xhtml);
			    LOGGER.debug("map: " + map);

				xhtml = processSite.selectSport("mlblines");
				LOGGER.debug("XHTML: " + xhtml);

				final Iterator<EventPackage> ep = processSite.parseGamesOnSite("mlblines", xhtml);   
			
				MlRecordEvent mre = new MlRecordEvent();
				mre.setMlinputfirstone("");
				mre.setMlplusminusfirstone("");
				mre.setId(new Long(929));
				mre.setEventname("#929 Pittsburgh Pirates -120 for Game");
				mre.setEventtype("ml");
				mre.setSport("mlblines"); 
				mre.setUserid(new Long(6));
				mre.setEventid(new Integer(929));
				mre.setEventid1(new Integer(929));
				mre.setEventid2(new Integer(930));
				
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
		this.siteParser = TDP = BSP;
		this.siteParser.setTimezone(timezone);
		BSP.setTimezone(timezone);

		this.httpClientWrapper.setHost("http://betpanther.com");
		String xhtml = getSite("http://betpanther.com");
		MAP_DATA = BSP.parseIndex(xhtml);
		MAP_DATA.put("ctl00$LoginForm1$_UserName", username);
		MAP_DATA.put("ctl00$LoginForm1$_Password", password);
		MAP_DATA.put("ctl00$LoginForm1$_IdBook", "");
		MAP_DATA.put("ctl00$LoginForm1$Redir", "");
		MAP_DATA.put("ctl00$LoginForm1$BtnSubmit.x", "18");
		MAP_DATA.put("ctl00$LoginForm1$BtnSubmit.y", "17");
		MAP_DATA.put("option1", "Remember");
		final List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
		setupNameValuesEmpty(postValuePairs, MAP_DATA, "");

		List<NameValuePair> headerValuePairs = new ArrayList<NameValuePair>(1);
		headerValuePairs.add(new BasicNameValuePair("Content-Type", "application/x-www-form-urlencoded"));
		headerValuePairs.add(new BasicNameValuePair("Upgrade-Insecure-Requests", "1"));
		headerValuePairs.add(new BasicNameValuePair("Referer", "http://betpanther.com"));
		List<NameValuePair> retValue = httpClientWrapper.postSitePage("http://betpanther.com/default.aspx", null, postValuePairs, headerValuePairs);
		xhtml = getCookiesAndXhtml(retValue);
		LOGGER.debug("HTML: " + xhtml);

		// Parse login
		MAP_DATA = BSP.parseLogin(xhtml);
		LOGGER.debug("MAP_DATA: " + MAP_DATA);

		// Setup the webapp name
		httpClientWrapper.setWebappname("wager");

		headerValuePairs = new ArrayList<NameValuePair>(1);
		headerValuePairs.add(new BasicNameValuePair("Upgrade-Insecure-Requests", "1"));
		headerValuePairs.add(new BasicNameValuePair("Referer", "http://betpanther.com/wager/Message.aspx"));
		retValue = httpClientWrapper.getSitePage(populateUrl("Welcome.aspx"), null, headerValuePairs);
		xhtml = httpClientWrapper.getCookiesAndXhtml(retValue);
		LOGGER.debug("HTML: " + xhtml);

		headerValuePairs = new ArrayList<NameValuePair>(1);
		headerValuePairs.add(new BasicNameValuePair("Upgrade-Insecure-Requests", "1"));
		headerValuePairs.add(new BasicNameValuePair("Referer", "http://betpanther.com/wager/Welcome.aspx"));
		retValue = httpClientWrapper.getSitePage(populateUrl("CreateSports.aspx?WT=0"), null, headerValuePairs);
		xhtml = httpClientWrapper.getCookiesAndXhtml(retValue);
		LOGGER.debug("HTML: " + xhtml);

		LOGGER.info("Exiting loginToSite()");
		return xhtml;
	}
}