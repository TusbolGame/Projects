/**
 * 
 */
package com.ticketadvantage.services.dao.sites.tdsportsone;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.sites.sportsinsights.SportsInsightsSite;
import com.ticketadvantage.services.dao.sites.tdsports.TDSportsProcessSite;
import com.ticketadvantage.services.email.AccessTokenFromRefreshToken;
import com.ticketadvantage.services.email.SendText;
import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.EventPackage;
import com.ticketadvantage.services.model.PendingEvent;

/**
 * @author jmiller
 *
 */
public class TDSportsOneProcessSite extends TDSportsProcessSite {
	private static final Logger LOGGER = Logger.getLogger(TDSportsOneProcessSite.class);
	protected final TDSportsOneParser TDP = new TDSportsOneParser();

	/**
	 * 
	 */
	public TDSportsOneProcessSite(String host, String username, String password, boolean isMobile, boolean showRequestResponse) {
		super("TDSportsOne", host, username, password, isMobile, showRequestResponse);
		LOGGER.info("Entering TDSportsOneProcessSite()");

		// Setup the parser
		super.siteParser = TDP;

		// Menu items
		NFL_LINES_SPORT = new String[] { "NFL" };
		NFL_LINES_NAME = new String[] { "NFL - REGULAR SEASON" };
		NFL_FIRST_SPORT = new String[] { "NFL" };
		NFL_FIRST_NAME = new String[] { "" };
		NFL_SECOND_SPORT = new String[] { "NFL" };
		NFL_SECOND_NAME = new String[] { "" };
		NCAAF_LINES_SPORT = new String[] { "NCAA FOOTBALL" };
		NCAAF_LINES_NAME = new String[] { "NCAA FOOTBALL - MEN" };
		NCAAF_FIRST_SPORT = new String[] { "NCAA FOOTBALL" };
		NCAAF_FIRST_NAME = new String[] { "" };
		NCAAF_SECOND_SPORT = new String[] { "NCAA FOOTBALL" };
		NCAAF_SECOND_NAME = new String[] { "" };
		NBA_LINES_SPORT = new String[] { "NBA" };
		NBA_LINES_NAME = new String[] { "NBA", "NBA - ORLANDO SUMMER LEAGUE", "NBA - UTAH SUMMER LEAGUE", "NBA - LAS VEGAS SUMMER LEAGUE" };
		NBA_FIRST_SPORT = new String[] { "NBA" };
		NBA_FIRST_NAME = new String[] { "NBA 1H" };
		NBA_SECOND_SPORT = new String[] { "NBA" };
		NBA_SECOND_NAME = new String[] { "NBA 2H" };
		NCAAB_LINES_SPORT = new String[] { "NCAA BASKETBALL" };
		NCAAB_LINES_NAME = new String[] { "NCAA BASKETBALL - MEN" };
		NCAAB_FIRST_SPORT = new String[] { "NCAA BASKETBALL" };
		NCAAB_FIRST_NAME = new String[] { "" };
		NCAAB_SECOND_SPORT = new String[] { "NCAA BASKETBALL" };
		NCAAB_SECOND_NAME = new String[] { "" };
		NHL_LINES_SPORT = new String[] { "NHL" };
		NHL_LINES_NAME = new String[] { "NHL - OT INCLUDED" };
		NHL_FIRST_SPORT = new String[] { "NHL" };
		NHL_FIRST_NAME = new String[] { "NHL - GAME PERIODS" };
		NHL_SECOND_SPORT = new String[] { "NHL" };
		NHL_SECOND_NAME = new String[] { "NHL - GAME PERIODS" };
		NHL_THIRD_SPORT = new String[] { "NHL" };
		NHL_THIRD_NAME = new String[] { "NHL - GAME PERIODS" };
		WNBA_LINES_SPORT = new String[] { "WNBA" };
		WNBA_LINES_NAME = new String[] { "WNBA" };
		WNBA_FIRST_SPORT = new String[] { "WNBA" };
		WNBA_FIRST_NAME = new String[] { "WNBA - 1H" };
		WNBA_SECOND_SPORT = new String[] { "WNBA" };
		WNBA_SECOND_NAME = new String[] { "WNBA - 2H" };
		MLB_LINES_SPORT = new String[] { "MLB" };
		MLB_LINES_NAME = new String[] { "MAJOR LEAGUE BASEBALL" };
		MLB_FIRST_SPORT = new String[] { "MLB" };
		MLB_FIRST_NAME = new String[] { "MLB - 1ST 5 FULL INNINGS" };
		MLB_SECOND_SPORT = new String[] { "MLB" };
		MLB_SECOND_NAME = new String[] { "" };

		LOGGER.info("Exiting TDSportsOneProcessSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final String[][] TDSITES = new String [][] { 
				{ "http://mywagerlive.com", "mc811", "randy", "500", "500", "500", "None", "ET"}
//				{ "http://betlonestar.com", "RV141", "411", "500", "500", "500", "Phoenix", "ET"}
//				{ "http://wager007.com", "av16", "marlins33", "500", "500", "500", "Phoenix", "ET"}
			};

			final TDSportsOneProcessSite processSite = new TDSportsOneProcessSite(TDSITES[0][0], TDSITES[0][1], TDSITES[0][2], false, true);
		    processSite.httpClientWrapper.setupHttpClient(TDSITES[0][6]);
		    processSite.processTransaction = false;
		    processSite.timezone = TDSITES[0][7];

			String xhtml = processSite.loginToSite(TDSITES[0][1], TDSITES[0][2]);
		    LOGGER.debug("xhtml: " + xhtml);

		    final Set<PendingEvent> pendingEvents = processSite.getPendingBets("a", "a", null);
		    for (PendingEvent pe : pendingEvents) {
		    		LOGGER.debug("PendingEvent: " + pe);
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
		List<NameValuePair> retValue = httpClientWrapper.getSitePage(httpClientWrapper.getHost() + "/member_login.aspx", null, setupHeader(false));
		String xhtml = httpClientWrapper.getCookiesAndXhtml(retValue);
		LOGGER.debug("HTML: " + xhtml);
		
		if (xhtml != null && xhtml.contains("The resource cannot be found")) {
			retValue = httpClientWrapper.getSitePage(httpClientWrapper.getHost() + "/member_login.aspx", null, setupHeader(false));
			xhtml = httpClientWrapper.getCookiesAndXhtml(retValue);
			LOGGER.debug("XHTML: " + xhtml);
			
			if (xhtml != null && xhtml.contains("The resource cannot be found")) {
				throw new BatchException(BatchErrorCodes.LOGIN_EXCEPTION, BatchErrorMessage.LOGIN_EXCEPTION, "Exception in with logging into website", xhtml);
			}
		}

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
		LOGGER.debug("HTML: " + xhtml);

		// Setup the webapp name
		httpClientWrapper.setWebappname(determineWebappName(httpClientWrapper.getPreviousUrl()));

		// Parse login
		MAP_DATA = TDP.parseLogin(xhtml);
		LOGGER.debug("MAP_DATA: " + MAP_DATA);

		// Go directly into the menu page
		xhtml = getSite(populateUrl("CreateSports.aspx?WT=0"));
		LOGGER.debug("HTML: " + xhtml);

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

		// Get time
		final Calendar date = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
		int hour = date.get(Calendar.HOUR_OF_DAY);

		// Check if we are between 6 am CT and 9 pm CT
		if (hour < 22 && hour > 5) {
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

		if (xhtml != null && (xhtml.contains("No Open Bets") || xhtml.contains("Currently no bets are pending in this account"))) {
			// Do nothing
			resetAttempts = 0;
			pendingWagers = new HashSet<PendingEvent>();
		} else if (xhtml != null && (xhtml.contains("Open Bets") || xhtml.contains("Open Wagers") || xhtml.contains("OPEN WAGERS")) && (!xhtml.contains("No Open Bets") && !xhtml.contains("Currently no bets are pending in this account"))) {
			try {
				pendingWagers = this.siteParser.parsePendingBets(xhtml, accountName, accountId);
				LOGGER.debug("pendingWagers: " + pendingWagers);
			} catch (Throwable t) {
				LOGGER.error("Parsing error for " + accountName + " and " + accountId);
				throw t;
			}
			resetAttempts = 0;
		} else {
			if (xhtml != null && (xhtml.contains("loginform") || xhtml.contains("member_login.aspx") || xhtml.contains("loginD"))) {
				LOGGER.error("Resetting the connection for " + accountName + " " + accountId);
				HttpClientUtils.closeQuietly(this.httpClientWrapper.getClient());
				this.httpClientWrapper.setupHttpClient(this.httpClientWrapper.getProxyName());
				this.loginToSite(this.httpClientWrapper.getUsername(), this.httpClientWrapper.getPassword());
				xhtml = getSite(super.populateUrl(PENDING_BETS));

				try {
					pendingWagers = this.siteParser.parsePendingBets(xhtml, accountName, accountId);
				} catch (Throwable t) {
					LOGGER.error("Parsing error for " + accountName + " and " + accountId);
					throw t;
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
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#doProcessPendingEvent(com.ticketadvantage.services.model.PendingEvent)
	 */
	@Override
	public void doProcessPendingEvent(PendingEvent pe) throws BatchException {
		LOGGER.info("Entering doProcessPendingEvent()");

		try {
			if (pe.getPosturl().equals("Something here")) {
				final SportsInsightsSite processSite = new SportsInsightsSite("https://sportsinsights.actionnetwork.com",
						"mojaxsventures@gmail.com", "action1");
				
				LOGGER.debug("RotationID: " + pe.getRotationid());
				LOGGER.debug("GameSport: " + pe.getGamesport());
				final EventPackage ep = processSite.getEventByIdNoSport(pe.getRotationid());
				LOGGER.debug("EventPackage: " + ep);

				if (ep != null) {
					// Get game type
					pe.setGametype(ep.getSporttype());
					pe.setGamedate(ep.getEventdatetime());
					pe.setEventdate(ep.getDateofevent() + " " + ep.getTimeofevent());

					if ("NFL".equals(ep.getSporttype())) {
						pe.setGamesport("Football");
						pe.setGametype("NFL");
					} else if ("NCAAF".equals(ep.getSporttype())) {
						pe.setGamesport("Football");
						pe.setGametype("NCAA");
					} else if ("NBA".equals(ep.getSporttype())) {
						pe.setGamesport("Basketball");
						pe.setGametype("NBA");
					} else if ("NCAAB".equals(ep.getSporttype())) {
						pe.setGamesport("Basketball");
						pe.setGametype("NCAA");
					} else if ("WNBA".equals(ep.getSporttype())) {
						pe.setGamesport("Basketball");
						pe.setGametype("WNBA");
					} else if ("MLB".equals(ep.getSporttype())) {
						pe.setGamesport("Baseball");
						pe.setGametype("MLB");
					} else if ("NHL".equals(ep.getSporttype())) {
						pe.setGamesport("Hockey");
						pe.setGametype("NHL");								
					}
				}				
			}
		} catch (Throwable t) {
			LOGGER.error("PendingEvent: " + pe);
			LOGGER.error(t.getMessage(), t);
		}

		LOGGER.info("Exiting doProcessPendingEvent()");
	}
}