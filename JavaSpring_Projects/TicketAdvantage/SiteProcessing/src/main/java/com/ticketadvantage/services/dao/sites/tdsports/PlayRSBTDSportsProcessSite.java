/**
 * 
 */
package com.ticketadvantage.services.dao.sites.tdsports;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.sites.SiteTransaction;
import com.ticketadvantage.services.dao.sites.SiteWagers;
import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.AccountEvent;
import com.ticketadvantage.services.model.BaseRecordEvent;
import com.ticketadvantage.services.model.EventPackage;
import com.ticketadvantage.services.model.MlRecordEvent;
import com.ticketadvantage.services.model.SpreadRecordEvent;
import com.ticketadvantage.services.model.TotalRecordEvent;

/**
 * @author jmiller
 *
 */
public class PlayRSBTDSportsProcessSite extends TDSportsProcessSite {
	private static final Logger LOGGER = Logger.getLogger(PlayRSBTDSportsProcessSite.class);
	protected PlayRSBTDSportsParser TDP = new PlayRSBTDSportsParser();

	/**
	 * 
	 */
	public PlayRSBTDSportsProcessSite(String host, String username, String password, boolean isMobile, boolean showRequestResponse) {
		super("PlayRSBTDSports", host, username, password, isMobile, showRequestResponse);
		LOGGER.info("Entering PlayRSBTDSportsProcessSite()");

		// Setup the parser
		super.siteParser = TDP;

		// Menu items
		NFL_LINES_SPORT = new String[] { "NFL" };
		NFL_LINES_NAME = new String[] { "NFL" };
		NFL_FIRST_SPORT = new String[] { "NFL" };
		NFL_FIRST_NAME = new String[] { "NFL 1ST HALVES" };
		NFL_SECOND_SPORT = new String[] { "NFL" };
		NFL_SECOND_NAME = new String[] { "NFL 2ND HALF LINES" };
		NCAAF_LINES_SPORT = new String[] { "CFB" };
		NCAAF_LINES_NAME = new String[] { "CFB" };
		NCAAF_FIRST_SPORT = new String[] { "CFB" };
		NCAAF_FIRST_NAME = new String[] { "" };
		NCAAF_SECOND_SPORT = new String[] { "CFB" };
		NCAAF_SECOND_NAME = new String[] { "" };
		NBA_LINES_SPORT = new String[] { "NBA" };
		NBA_LINES_NAME = new String[] { "NBA" };
		NBA_FIRST_SPORT = new String[] { "NBA" };
		NBA_FIRST_NAME = new String[] { "NBA 1ST HALVES" };
		NBA_SECOND_SPORT = new String[] { "NBA" };
		NBA_SECOND_NAME = new String[] { "NBA 2ND HALF LINES" };
		NCAAB_LINES_SPORT = new String[] { "CBB" };
		NCAAB_LINES_NAME = new String[] { "" };
		NCAAB_FIRST_SPORT = new String[] { "CBB" };
		NCAAB_FIRST_NAME = new String[] { "" };
		NCAAB_SECOND_SPORT = new String[] { "CBB" };
		NCAAB_SECOND_NAME = new String[] { "" };
		NHL_LINES_SPORT = new String[] { "NHL" };
		NHL_LINES_NAME = new String[] { "NATIONAL HOCKEY LEAGUE" };
		NHL_FIRST_SPORT = new String[] { "NHL" };
		NHL_FIRST_NAME = new String[] { "" };
		NHL_SECOND_SPORT = new String[] { "NHL" };
		NHL_SECOND_NAME = new String[] { "" };
		WNBA_LINES_SPORT = new String[] { "NBA" };
		WNBA_LINES_NAME = new String[] { "WNBA" };
		WNBA_FIRST_SPORT = new String[] { "NBA" };
		WNBA_FIRST_NAME = new String[] { "WNBA 1ST HALVES" };
		WNBA_SECOND_SPORT = new String[] { "NBA" };
		WNBA_SECOND_NAME = new String[] { "" };
		MLB_LINES_SPORT = new String[] { "BASEBALL" };
		MLB_LINES_NAME = new String[] { "MAJOR LEAGUE BASEBALL" };
		MLB_FIRST_SPORT = new String[] { "BASEBALL" };
		MLB_FIRST_NAME = new String[] { "MLB 1ST FIVE INNINGS" };
		MLB_SECOND_SPORT = new String[] { "BASEBALL" };
		MLB_SECOND_NAME = new String[] { "MLB 2ND HALF LINES" };

		LOGGER.info("Exiting PlayRSBTDSportsProcessSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final String[][] TDSITES = new String [][] { 
				{ "http://www.playrsb.com", "2911e", "wc", "500", "500", "500", "Los Angeles", "PT"}
			};

			// Loop through the sites
			for (int i = 0; i < TDSITES.length; i++) {
				final PlayRSBTDSportsProcessSite processSite = new PlayRSBTDSportsProcessSite(TDSITES[i][0], TDSITES[i][1],
						TDSITES[i][2], false, true);
				final PlayRSBTDSportsProcessSite processSite2 = new PlayRSBTDSportsProcessSite(TDSITES[i][0], TDSITES[i][1],
						TDSITES[i][2], false, true);
			    processSite.httpClientWrapper.setupHttpClient(TDSITES[0][6]);
			    processSite.processTransaction = false;
			    processSite.timezone = TDSITES[0][7];

			    processSite2.httpClientWrapper.setupHttpClient(TDSITES[0][6]);
			    processSite2.processTransaction = false;
			    processSite2.timezone = TDSITES[0][7];

				// Now call the test suite
				PlayRSBTDSportsProcessSite.testSite2(processSite, processSite2, i, TDSITES);
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
		List<NameValuePair> retValue = httpClientWrapper.getSitePage(httpClientWrapper.getHost(), null, setupHeader(false));
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
	
	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#selectEvent(com.ticketadvantage.services.dao.sites.SiteTransaction, com.ticketadvantage.services.model.EventPackage, com.ticketadvantage.services.model.BaseRecordEvent, com.ticketadvantage.services.model.AccountEvent)
	 */
	@Override
	protected String selectEvent(SiteTransaction siteTransaction, EventPackage eventPackage, BaseRecordEvent event, AccountEvent ae) throws BatchException {
		LOGGER.info("Entering selectEvent()");
		TDSportsTeamPackage teamPackage = null;

		// Only get the first one because that's all there is for TDSports at this point
		final List<NameValuePair> postNameValuePairs = new ArrayList<NameValuePair>(1);

		// Get the appropriate record event
		if (event instanceof SpreadRecordEvent) {			
			teamPackage = (TDSportsTeamPackage)SiteWagers.setupTeam(SiteWagers.determineSpreadData((SpreadRecordEvent)event), eventPackage);
			if (teamPackage != null && 
				teamPackage.getGameSpreadInputName() != null &&
				teamPackage.getGameSpreadInputName().length() > 0 &&
				teamPackage.getGameSpreadInputValue() != null && 
				teamPackage.getGameSpreadInputValue().length() > 0) {
				postNameValuePairs.add(new BasicNameValuePair(teamPackage.getGameSpreadInputName(), teamPackage.getGameSpreadInputValue()));
			} else {
				throw new BatchException(BatchErrorCodes.XHTML_GAMES_PARSING_EXCEPTION, BatchErrorMessage.XHTML_GAMES_PARSING_EXCEPTION, "No spread available for this game");
			}
		} else if (event instanceof TotalRecordEvent) {
			teamPackage = (TDSportsTeamPackage)SiteWagers.setupTeam(SiteWagers.determineTotalData((TotalRecordEvent)event), eventPackage);
			if (teamPackage != null && 
				teamPackage.getGameTotalInputName() != null &&
				teamPackage.getGameTotalInputName().length() > 0 &&
				teamPackage.getGameTotalInputValue() != null && 
				teamPackage.getGameTotalInputValue().length() > 0) {
				postNameValuePairs.add(new BasicNameValuePair(teamPackage.getGameTotalInputName(), teamPackage.getGameTotalInputValue()));
			} else {
				throw new BatchException(BatchErrorCodes.XHTML_GAMES_PARSING_EXCEPTION, BatchErrorMessage.XHTML_GAMES_PARSING_EXCEPTION, "No total available for this game");
			}
		} else if (event instanceof MlRecordEvent) {
			teamPackage = (TDSportsTeamPackage)SiteWagers.setupTeam(SiteWagers.determineMoneyLineData((MlRecordEvent)event), eventPackage);
			if (teamPackage != null && 
				teamPackage.getGameMLInputName() != null &&
				teamPackage.getGameMLInputName().length() > 0 &&
				teamPackage.getGameMLInputValue() != null) {
				postNameValuePairs.add(new BasicNameValuePair(teamPackage.getGameMLInputName(), teamPackage.getGameMLInputValue().get("0")));
			} else {
				throw new BatchException(BatchErrorCodes.XHTML_GAMES_PARSING_EXCEPTION, BatchErrorMessage.XHTML_GAMES_PARSING_EXCEPTION, "No moneyline available for this game");
			}
		}
		MAP_DATA.remove("ctl00$WagerContent$ctl08");
		final String actionName = setupNameValuesEmpty(postNameValuePairs, MAP_DATA, httpClientWrapper.getWebappname());
		LOGGER.debug("ActionName: " + actionName);

		// Setup the wager
		final String xhtml = postSite(actionName, postNameValuePairs);

		LOGGER.info("Exiting selectEvent()");
		return xhtml;
	}
}