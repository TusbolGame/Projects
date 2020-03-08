/**
 * 
 */
package com.ticketadvantage.services.dao.sites.tdsportseleven;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.sites.tdsports.TDSportsProcessSite;
import com.ticketadvantage.services.errorhandling.BatchException;

/**
 * @author jmiller
 *
 */
public class TDSportsElevenProcessSite extends TDSportsProcessSite {
	private static final Logger LOGGER = Logger.getLogger(TDSportsElevenProcessSite.class);
	protected TDSportsElevenParser NSP = new TDSportsElevenParser();

	/**
	 * 
	 */
	public TDSportsElevenProcessSite(String host, String username, String password, boolean isMobile,
			boolean showRequestResponse) {
		super("TDSportsEleven", host, username, password, isMobile, showRequestResponse);
		LOGGER.info("Entering TDSportsElevenProcessSite()");

		// Setup the parser
		this.siteParser = NSP;

		// Menu items
		NFL_LINES_SPORT = new String[] { "FOOTBALL GAME LINES" };
		NFL_LINES_NAME = new String[] { "NFL", "NFL - PRESEASON" };
		NFL_FIRST_SPORT = new String[] { "FOOTBALL HALFTIMES" };
		NFL_FIRST_NAME = new String[] { "NFL -  FIRST HALF LINES" };
		NFL_SECOND_SPORT = new String[] { "FOOTBALL HALFTIMES" };
		NFL_SECOND_NAME = new String[] { "NFL -  SECOND HALF LINES" };
		NCAAF_LINES_SPORT = new String[] { "FOOTBALL GAME LINES" };
		NCAAF_LINES_NAME = new String[] { "NCAAF", "NCAAF - EXTRA GAMES" };
		NCAAF_FIRST_SPORT = new String[] { "FOOTBALL HALFTIMES" };
		NCAAF_FIRST_NAME = new String[] { "NCAAF 1H", "NCAAF 1H -EXTRA GAMES" };
		NCAAF_SECOND_SPORT = new String[] { "FOOTBALL HALFTIMES" };
		NCAAF_SECOND_NAME = new String[] { "NCAAF 2H", "NCAAF 2H -EXTRA GAMES" };
		NBA_LINES_SPORT = new String[] { "BASKETBALL GAME LINES" };
		NBA_LINES_NAME = new String[] { "NBA", "NBA PRESEASON" };
		NBA_FIRST_SPORT = new String[] { "BASKETBALL GAME LINES" };
		NBA_FIRST_NAME = new String[] { "NBA 1H" };
		NBA_SECOND_SPORT = new String[] { "BASKETBALL GAME LINES" };
		NBA_SECOND_NAME = new String[] { "NBA 2H" };
		NCAAB_LINES_SPORT = new String[] { "BASKETBALL GAME LINES" };
		NCAAB_LINES_NAME = new String[] { "NCAA BASKETBALL - MEN", "NCAAB MEN - EXTRA GAMES", "NCAAB MEN - ADDED GAMES" };
		NCAAB_FIRST_SPORT = new String[] { "BASKETBALL GAME LINES" };
		NCAAB_FIRST_NAME = new String[] { "NCAAB MEN - 1H", "NCAA BASKETBALL - (1H)", "NCAAB MEN - 1H EXTRA GAMES", "NCAAB MEN - 1H ADDED GAMES" };		
		NCAAB_SECOND_SPORT = new String[] { "BASKETBALL GAME LINES" };
		NCAAB_SECOND_NAME = new String[] { "NCAA BASKETBALL - (2H)", "NCAAB MEN - 2H EXTRA GAMES", "NCAAB MEN - 2H" };
		NHL_LINES_SPORT = new String[] { "HOCKEY" };
		NHL_LINES_NAME = new String[] { "NATIONAL HOCKEY LEAGUE" };
		NHL_FIRST_SPORT = new String[] { "HOCKEY" };
		NHL_FIRST_NAME = new String[] { "NHL - PERIOD LINES" };
		NHL_SECOND_SPORT = new String[] { "HOCKEY" };
		NHL_SECOND_NAME = new String[] { "NHL - PERIOD LINES" };
		WNBA_LINES_NAME = new String[] { "WNBA" };
		WNBA_LINES_SPORT = new String[] { "BASKETBALL GAME LINES" };
		WNBA_FIRST_NAME = new String[] { "WNBA 1st Half" };
		WNBA_FIRST_SPORT = new String[] { "BASKETBALL GAME LINES" };
		WNBA_SECOND_NAME = new String[] { "WNBA 2nd Half" };
		WNBA_SECOND_SPORT = new String[] { "BASKETBALL GAME LINES" };
		MLB_LINES_SPORT = new String[] { "BASEBALL GAME LINES" };
		MLB_LINES_NAME = new String[] { "MAJOR LEAGUE BASEBALL", "MLB - EXHIBITION BASEBALL" };
		MLB_FIRST_SPORT = new String[] { "BASEBALL GAME LINES" };
		MLB_FIRST_NAME = new String[] { "MLB 1H (5 FULL INNINGS)" };
		MLB_SECOND_SPORT = new String[] { "BASEBALL GAME LINES" };
		MLB_SECOND_NAME = new String[] { "MLB 2H (4 FULL INNINGS+EXTRA INNS)" };

		LOGGER.info("Exiting TDSportsElevenProcessSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final String[][] TDSITES = new String [][] { 
//				{ "http://names777.com/", "Rc1525", "John", "100", "100", "100", "Chicago", "ET"},
//				{ "http://Betcatalinasports.com/", "catj776", "pink", "100", "100", "100", "None", "ET"}
//				{ "http://anysport247.com", "BCC111", "bobbyx", "0", "0", "0", "New York", "ET"}
//				{ "http://1betvegas.com", "HHK199", "lamp", "0", "0", "0", "New York", "ET"}
//				{ "http://www.1BetVegas.com", "Card7", "Pokerbob1", "0", "0", "0", "None", "ET"}
//				{ "http://everysport247.com", "WB236", "zoom", "0", "0", "0", "None", "ET"}
//				{ "http://1betvegas.com", "HHK199", "lamp", "0", "0", "0", "None", "ET"}
//				{ "http://everygame247.com", "MI231", "MUSKEGON231", "0", "0", "0", "None", "ET"}
//				{ "http://www.betitall.net", "CATF285", "tiger", "0", "0", "0", "None", "ET"}
				{ "http://anysport247.com", "ktm01", "nick", "0", "0", "0", "None", "ET"}
			};

			// Loop through the sites
			for (int i = 0; i < TDSITES.length; i++) {
				final TDSportsElevenProcessSite processSite = new TDSportsElevenProcessSite(TDSITES[i][0], TDSITES[i][1],
						TDSITES[i][2], false, true);
			    processSite.httpClientWrapper.setupHttpClient(TDSITES[0][6]);
			    processSite.processTransaction = false;
			    processSite.timezone = TDSITES[0][7];
			    processSite.siteParser = processSite.TDP = processSite.NSP;
			    processSite.siteParser.setTimezone(TDSITES[0][7]);
			    processSite.NSP.setTimezone(TDSITES[0][7]);
			    processSite.testSpread(processSite, TDSITES);
			}
		} catch (Throwable t) {
			LOGGER.info("Throwable: ", t);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ticketadvantage.services.dao.sites.SiteProcessor#loginToSite(java.lang.
	 * String, java.lang.String)
	 */
	@Override
	public String loginToSite(String username, String password) throws BatchException {
		LOGGER.info("Entering loginToSite()");
		LOGGER.debug("username: " + username);
		LOGGER.debug("password: " + password);

		// Setup the timezone
		this.siteParser = TDP = NSP;
		this.siteParser.setTimezone(timezone);
		NSP.setTimezone(timezone);

		this.httpClientWrapper.setHost(this.httpClientWrapper.getHost());
		String xhtml = null;
		try {
			xhtml = getSite(this.httpClientWrapper.getHost());
		} catch (Throwable t) {
			LOGGER.error("Host: " + httpClientWrapper.getHost());
			LOGGER.error("Username: " + username);
		}
		MAP_DATA = NSP.parseIndex(xhtml);
		MAP_DATA.remove("username");
		MAP_DATA.remove("password");
		MAP_DATA.put("ctl00$MainContent$ctlLogin$_UserName", username);
		MAP_DATA.put("ctl00$MainContent$ctlLogin$_Password", password.toUpperCase());
		final List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
		setupNameValuesEmpty(postValuePairs, MAP_DATA, "");

		xhtml = postSite(this.httpClientWrapper.getHost() + "/default.aspx", postValuePairs);
		LOGGER.debug("HTML: " + xhtml);

		// Parse login
		MAP_DATA = NSP.parseLogin(xhtml);
		LOGGER.debug("MAP_DATA: " + MAP_DATA);

		// Setup the webapp name
		httpClientWrapper.setWebappname("wager");

		// welcome.aspx
		xhtml = getSite(populateUrl("welcome.aspx"));
		LOGGER.debug("HTML: " + xhtml);

		// CreateSports.aspx
		xhtml = getSite(populateUrl("CreateSports.aspx?WT=0"));
		LOGGER.debug("HTML: " + xhtml);

		LOGGER.info("Exiting loginToSite()");
		return xhtml;
	}
}