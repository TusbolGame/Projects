/**
 * 
 */
package com.ticketadvantage.services.dao.sites.tdsports;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.ticketadvantage.services.email.AccessTokenFromRefreshToken;
import com.ticketadvantage.services.email.SendText;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.AccountEvent;
import com.ticketadvantage.services.model.PendingEvent;
import com.ticketadvantage.services.model.SpreadRecordEvent;

/**
 * @author jmiller
 *
 */
public class Playsports527TDSportsProcessSite extends TDSportsProcessSite {
	private static final Logger LOGGER = Logger.getLogger(Playsports527TDSportsProcessSite.class);
	protected BetBigCityTDSportsParser TDP = new BetBigCityTDSportsParser();

	/**
	 * 
	 */
	public Playsports527TDSportsProcessSite(String host, String username, String password, boolean isMobile, boolean showRequestResponse) {
		super("Playsports527TDSports", host, username, password, isMobile, showRequestResponse);
		LOGGER.info("Entering Playsports527TDSportsProcessSite()");

		// Setup the parser
		this.siteParser = TDP;

		// Menu items
		NFL_LINES_NAME = new String[] { "NFL - WEEK", "NFL - PRESEASON WEEK", "NFL", "NFL - GAME LINES", "NFL PLAYOFFS" };
		NFL_LINES_SPORT = new String[] { "PRO FOOTBALL", "NFL FOOTBALL", "NFL", "FOOTBALL GAME LINES" };
		NFL_FIRST_NAME = new String[] { "1ST HALVES - NFL", "NFL 1ST HALVES", "NFL - FIRST HALF LINES", "NFL (1H)", "NFL 1H" };
		NFL_FIRST_SPORT = new String[] { "PRO FOOTBALL", "NFL FOOTBALL", "NFL", "FOOTBALL GAME LINES" };
		NFL_SECOND_NAME = new String[] { "2ND HALVES - NFL", "NFL 2ND HALVES", "NFL - SECOND HALF LINES", "NFL (2H)", "NFL 2H" };
		NFL_SECOND_SPORT = new String[] { "PRO FOOTBALL", "NFL FOOTBALL", "NFL", "FOOTBALL GAME LINES" };
		NCAAF_LINES_NAME = new String[] { "NCAA FOOTBALL", "NCAA FOOTBALL - GAME LINES", "NCAA FOOTBALL - MEN", "NCAA FOOTBALL  MEN", "NCAA FOOTBALL MEN" };
		NCAAF_LINES_SPORT = new String[] { "COLLEGE FOOTBALL", "NCAA FOOTBALL", "CFB" };
		NCAAF_FIRST_NAME = new String[] { "1ST HALVES - CFB", "NCAA FOOTBALL (1H)", "NCAA FOOTBALL - (1H)", "NCAA FB 1ST HALVES", "NCAA FB (1H)" };
		NCAAF_FIRST_SPORT = new String[] { "COLLEGE FOOTBALL", "NCAA FOOTBALL", "CFB" };
		NCAAF_SECOND_NAME = new String[] { "2ND HALVES - CFB", "NCAA FOOTBALL (2H)", "NCAA FOOTBALL - (2H)", "NCAA FB HALFTIMES", "NCAA FB (2H)" };
		NCAAF_SECOND_SPORT = new String[] { "COLLEGE FOOTBALL", "NCAA FOOTBALL", "CFB" };
		NBA_LINES_NAME = new String[] { "NBA", "NBA - GAME LINES", "TOMORROWS NBA", "NBA - SUMMER LEAGUE" };
		NBA_LINES_SPORT = new String[] { "PRO BASKETBALL", "NBA BASKETBALL", "NBA", "BASKETBALL GAME LINES" };
		NBA_FIRST_NAME = new String[] { "1ST HALVES - NBA", "NBA 1ST HALVES", "NBA - FIRST HALF LINES", "NBA (1H)", "NBA - 1ST HALF LINES", "NBA - 1ST HALVES", "NBA 1H" };
		NBA_FIRST_SPORT = new String[] { "PRO BASKETBALL", "NBA BASKETBALL", "NBA", "BASKETBALL GAME LINES" };
		NBA_SECOND_NAME = new String[] { "2ND HALVES - NBA", "NBA 2ND HALVES", "NBA - SECOND HALF LINES", "NBA (2H)", "NBA - 2ND HALF LINES", "NBA - 2ND HALVES", "NBA 2H" };
		NBA_SECOND_SPORT = new String[] { "PRO BASKETBALL", "NBA BASKETBALL", "NBA", "BASKETBALL GAME LINES" };
		NCAAB_LINES_NAME = new String[] { "NCAA BASKETBALL", "NCAA BASKETBALL - GAME LINES", "NCAA BASKETBALL - MEN", "NCAA BASKETBALL  MEN", "NCAA BASKETBALL MEN", "NCAA BK - GAME LINES", "COLLEGE BASKETBALL - GAME LINES", "NCAA BASKETBALL - MEN", "NCAAB MEN - EXTRA GAMES" };
		NCAAB_LINES_SPORT = new String[] { "COLLEGE BASKETBALL", "CBB", "NCAA BASKETBALL", "BASKETBALL GAME LINES" };
		NCAAB_FIRST_NAME = new String[] { "1ST HALVES - CBK", "NCAA BASKETBALL (1H)", "NCAA BASKETBALL - (1H)", "NCAA BK 1ST HALVES", "NCAA BK (1H)", "1ST HALVES - CBB", "1H COLLEGE BASKETBALL", "NCAA BK MENS 1H", "NCAA BASKETBALL - MEN 1H", "NCAAB MEN - 1H", "NCAA BASKETBALL - 1ST HALF", "NCAAB MEN - 1H EXTRA GAMES" };
		NCAAB_FIRST_SPORT = new String[] { "COLLEGE BASKETBALL", "CBB", "NCAA BASKETBALL", "BASKETBALL GAME LINES" };
		NCAAB_SECOND_NAME = new String[] { "2ND HALVES - CBK", "NCAA BASKETBALL (2H)", "NCAA BASKETBALL - (2H)", "NCAA BK HALFTIMES", "NCAA BK (2H)", "2ND HALVES - CBB", "2H COLLEGE BASKETBALL", "NCAA BK MENS 2H", "NCAA BASKETBALL - MEN 2H", "NCAAB MEN - 2H", "NCAA BASKETBALL - 2ND HALF" };
		NCAAB_SECOND_SPORT = new String[] { "COLLEGE BASKETBALL", "CBB", "NCAA BASKETBALL", "BASKETBALL GAME LINES" };
		NHL_LINES_NAME = new String[] { "NHL", "NHL - GAME LINES", "TOMORROWS NHL", "NATIONAL HOCKEY LEAGUE" }; 
		NHL_LINES_SPORT = new String[] { "NHL HOCKEY", "HOCKEY", "NHL", "HOCKEY GAME LINES" };
		NHL_FIRST_NAME = new String[] { "1ST HALVES - NHL", "NHL 1ST HALVES", "NHL - FIRST HALF LINES", "NHL (1H)", "NHL - 1ST HALF LINES", "NHL - 1ST HALVES", "NHL 1H", "NATIONAL HOCKEY LEAGUE" };
		NHL_FIRST_SPORT = new String[] { "NHL HOCKEY", "HOCKEY", "NHL", "HOCKEY GAME LINES" };
		NHL_SECOND_NAME = new String[] { "2ND HALVES - NHL", "NHL 2ND HALVES", "NHL - SECOND HALF LINES", "NHL (2H)", "NHL - 2ND HALF LINES", "NHL - 2ND HALVES", "NHL 2H", "NATIONAL HOCKEY LEAGUE" };
		NHL_SECOND_SPORT = new String[] { "NHL HOCKEY", "HOCKEY", "NHL", "HOCKEY GAME LINES" };
		WNBA_LINES_NAME = new String[] { "WNBA", "WNBA - GAME LINES" };
		WNBA_LINES_SPORT = new String[] { "WNBA" };
		WNBA_FIRST_NAME = new String[] { "1ST HALVES - WNBA", "WNBA 1ST HALVES", "WNBA - FIRST HALF LINES", "WNBA (1H)", "WNBA - 1ST HALF LINES", "WNBA - 1ST HALVES", "WNBA 1H" };
		WNBA_FIRST_SPORT = new String[] { "WNBA" };
		WNBA_SECOND_NAME = new String[] { "2ND HALVES - WNBA", "WNBA 2ND HALVES", "WNBA - SECOND HALF LINES", "WNBA (2H)", "WNBA - 2ND HALF LINES", "WNBA - 2ND HALVES", "WNBA 2H" };
		WNBA_SECOND_SPORT = new String[] { "WNBA" };
		MLB_LINES_SPORT = new String[] { "BASEBALL" };
		MLB_LINES_NAME = new String[] { "MAJOR LEAGUE BASEBALL", "TOMORROWS MLB", "EXHIBITION BASEBALL", "PRE-SEASON BASEBALL" };
		MLB_FIRST_SPORT = new String[] { "BASEBALL" };
		MLB_FIRST_NAME = new String[] { "1ST 5 INNINGS" };
		MLB_SECOND_SPORT = new String[] { "BASEBALL" };
		MLB_SECOND_NAME = new String[] { "MLB - 2ND HALVES" };
		MLB_THIRD_SPORT = new String[] { "BASEBALL" };
		MLB_THIRD_NAME = new String[] { "MLB - 3RD HALVES" };

		LOGGER.info("Exiting Playsports527TDSportsProcessSite()");
	}

	/**
	 * 
	 * @param accountSoftware
	 * @param host
	 * @param username
	 * @param password
	 */
	public Playsports527TDSportsProcessSite(String accountSoftware, String host, String username, String password, boolean isMobile, boolean showRequestResponse) {
		super(accountSoftware, host, username, password, isMobile, showRequestResponse);
		LOGGER.info("Entering Playsports527TDSportsProcessSite()");
		LOGGER.info("Exiting Playsports527TDSportsProcessSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final String[][] TDSITES = new String [][] { 
				{ "http://wager1.playsports527.com/", "PREZ118", "MIKE", "0", "0", "0", "None", "ET"}
			};

			final Playsports527TDSportsProcessSite processSite = new Playsports527TDSportsProcessSite(TDSITES[0][0], TDSITES[0][1], TDSITES[0][2], false, true);
		    processSite.httpClientWrapper.setupHttpClient(TDSITES[0][6]);
		    processSite.processTransaction = false;
		    processSite.timezone = TDSITES[0][7];
		    
		    processSite.testSpread(processSite, TDSITES);

/*
			processSite.loginToSite(processSite.httpClientWrapper.getUsername(), processSite.httpClientWrapper.getPassword());
			Set<PendingEvent> pendingEvents = processSite.getPendingBets(TDSITES[0][0], TDSITES[0][1], null);
			if (pendingEvents != null && !pendingEvents.isEmpty()) {
				Iterator<PendingEvent> itr = pendingEvents.iterator();
				while (itr.hasNext()) {
					System.out.println("PendingEvent: " + itr.next());
				}
			}
			
			final TDSportsProcessSite processSite = new TDSportsProcessSite(TDSITES[0][0], TDSITES[0][1],
					TDSITES[0][2]);
		    processSite.httpClientWrapper.setupHttpClient(TDSITES[0][6]);
		    processSite.processTransaction = false;
		    processSite.timezone = TDSITES[0][7];

			processSite.loginToSite(processSite.httpClientWrapper.getUsername(), processSite.httpClientWrapper.getPassword());
			*/

			// Loop through the sites
/* for (int i = 0; i < TDSITES.length; i++) {
				final TDSportsProcessSite processSite = new TDSportsProcessSite(TDSITES[i][0], TDSITES[i][1],
						TDSITES[i][2], false, true);
				final TDSportsProcessSite processSite2 = new TDSportsProcessSite(TDSITES[i][0], TDSITES[i][1],
						TDSITES[i][2], false, true);
			    processSite.httpClientWrapper.setupHttpClient(TDSITES[0][6]);
			    processSite.processTransaction = false;
			    processSite.timezone = TDSITES[0][7];

			    processSite2.httpClientWrapper.setupHttpClient(TDSITES[0][6]);
			    processSite2.processTransaction = false;
			    processSite2.timezone = TDSITES[0][7];

				// Now call the test suite
				TDSportsProcessSite.testSite2(processSite, processSite2, i, TDSITES);
			}
*/
	} catch (Throwable t) {
			LOGGER.info("Throwable: ", t);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.tdsports.TDSportsProcessSite#getPendingBets(java.lang.String, java.lang.String, java.lang.Object)
	 */
	@Override
	public Set<PendingEvent> getPendingBets(String accountName, String accountId, Object anythingObject) throws BatchException {
		LOGGER.info("Entering getPendingBets()");
		LOGGER.debug("accountName: " + accountName);
		LOGGER.debug("accountId: " + accountId);
		Set<PendingEvent> pendingWagers = null;

		// Get time
		final Calendar date = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
		int hour = date.get(Calendar.HOUR_OF_DAY);

		// Check if we are between 6 am CT and 9 pm CT
		if (hour < 23 && hour > 5) {
			if (this.resetConnection) {
				LOGGER.error("Resetting the connection for " + accountName + " " + accountId);
				
				HttpClientUtils.closeQuietly(this.httpClientWrapper.getClient());
				this.httpClientWrapper.setupHttpClient(proxyName);
				this.loginToSite(this.httpClientWrapper.getUsername(), this.httpClientWrapper.getPassword());
				this.resetConnection = false;
			}
		} else {
			this.resetConnection = true;
		}

		// Get the pending bets data
		String xhtml = getSite(super.populateUrl(PENDING_BETS));
		LOGGER.debug("xhtml: " + xhtml);

		if (xhtml != null && (xhtml.contains("Open Wagers") && !xhtml.contains("No Open Bets"))) {
			pendingWagers = this.siteParser.parsePendingBets(xhtml, accountName, accountId);
			resetAttempts = 0;
		} else if (xhtml != null && xhtml.contains("No Open Bets")) {
			// Do nothing
			resetAttempts = 0;
			pendingWagers = new HashSet<PendingEvent>();
		} else {
			try {
				// sleep for 2.5 minutes
				Thread.sleep(150000);
			} catch (InterruptedException ie) {
				LOGGER.error(ie.getMessage(), ie);
			}

			if (xhtml != null && xhtml.contains("abcwagering.ag/?login-error")) {
				LOGGER.error("Resetting the connection for " + accountName + " " + accountId);
				HttpClientUtils.closeQuietly(this.httpClientWrapper.getClient());
				this.httpClientWrapper.setupHttpClient(proxyName);
				this.loginToSite(this.httpClientWrapper.getUsername(), this.httpClientWrapper.getPassword());
				xhtml = getSite(super.populateUrl(PENDING_BETS));
				if (xhtml != null && (xhtml.contains("Open Wagers") ||xhtml.contains("No Open Bets"))) {
					pendingWagers = this.siteParser.parsePendingBets(xhtml, accountName, accountId);					
				} else {
					LOGGER.error("ERROR ERROR ERROR: " + xhtml);
				}

				if (resetAttempts++ == 10) {
					// Get the email access token so we can update the users
					String accessToken = "";
					try {
						String clientid = "529498092660-bcn5kjtr7iqr1k7fasv28ld16hgdi8p0.apps.googleusercontent.com";
						String clientsecret = "o4VwTH0ykC3qjyeMlI7FdlaM";						
						String refreshtoken = "1/CA1PWWUpXS6Y4N0jEGtK6KnR5fCCSD4mvg2gO-ERKC4";
						String granttype = "refresh_token";
						final AccessTokenFromRefreshToken accessTokenFromRefreshToken = new AccessTokenFromRefreshToken(clientid, clientsecret, refreshtoken, granttype);
						accessToken = accessTokenFromRefreshToken.getAccessToken();
						final SendText sendText = new SendText();
						sendText.setOAUTH2_TOKEN(accessToken);
						sendText.sendTextWithMessage("9132195234@vtext.com", "10 attempts have been reset for " + accountName + " " + accountId);
					} catch (Throwable t) {
						LOGGER.error(t.getMessage(), t);
					}
					resetAttempts = 0;
				}

				if (pendingWagers != null && pendingWagers.isEmpty()) {
					LOGGER.error("ERROR ERROR ERROR setting pendingWagers to null");
					pendingWagers = null;
				}
			} else {
				pendingWagers = null;
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
		TDP.setTimezone(timezone);

		MAP_DATA = new HashMap<String, String>();
		MAP_DATA.put("account", username);
		MAP_DATA.put("ErrorURL", "//www.playsports527.com/?login-error");
		MAP_DATA.put("password", password);
		MAP_DATA.put("siteID", "266");
		List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
		setupNameValuesEmpty(postValuePairs, MAP_DATA, "");

		List<NameValuePair> headerValuePairs = new ArrayList<NameValuePair>(1);
		headerValuePairs.add(new BasicNameValuePair("Content-Type", "application/x-www-form-urlencoded"));
		headerValuePairs.add(new BasicNameValuePair("Upgrade-Insecure-Requests", "1"));
		headerValuePairs.add(new BasicNameValuePair("Referer", super.httpClientWrapper.getHost()));
		String xhtml = super.postSite(super.httpClientWrapper.getHost() + "/DefaultLogin.aspx", postValuePairs);
		LOGGER.debug("HTML: " + xhtml);

		// Parse login
		MAP_DATA = TDP.parseLogin(xhtml);
		LOGGER.debug("MAP_DATA: " + MAP_DATA);

		final Map<String, String> tempData = new HashMap<String, String>();
		tempData.put("Account", username);
		tempData.put("ErrorURL", MAP_DATA.get("ErrorURL"));
		tempData.put("IdBook", MAP_DATA.get("IdBook"));
		tempData.put("Password", password);
		tempData.put("Redir", MAP_DATA.get("Redir"));
		tempData.put("SiteID", MAP_DATA.get("SiteID"));
		postValuePairs = new ArrayList<NameValuePair>(1);
		setupNameValuesEmpty(postValuePairs, tempData, "");
		xhtml = super.postSite(super.httpClientWrapper.getHost() + "login.aspx", postValuePairs);
		LOGGER.debug("HTML: " + xhtml);
		
		httpClientWrapper.setWebappname("wager");

		LOGGER.info("Exiting loginToSite()");
		return xhtml;
	}
}