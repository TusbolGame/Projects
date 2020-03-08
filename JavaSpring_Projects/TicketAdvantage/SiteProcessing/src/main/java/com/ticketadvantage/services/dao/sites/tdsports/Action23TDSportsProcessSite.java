/**
 * 
 */
package com.ticketadvantage.services.dao.sites.tdsports;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.cookie.Cookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.EventPackage;
import com.ticketadvantage.services.model.MlRecordEvent;
import com.ticketadvantage.services.model.PendingEvent;

/**
 * @author jmiller
 *
 */
public class Action23TDSportsProcessSite extends TDSportsProcessSite {
	private static final Logger LOGGER = Logger.getLogger(Action23TDSportsProcessSite.class);
	protected Action23TDSportsParser BSP = new Action23TDSportsParser();
	private String IdPlayer = "";

	/**
	 * 
	 */
	public Action23TDSportsProcessSite(String host, String username, String password, boolean isMobile, boolean showRequestResponse) {
		super("Action23TDSports", host, username, password, isMobile, showRequestResponse);
		LOGGER.info("Entering Action23TDSportsProcessSite()");

		// Setup the parser
		this.siteParser = BSP;

		// Menu items
		NFL_LINES_SPORT = new String[] { "NFL" };
		NFL_LINES_NAME = new String[] { "NFL - GAME LINES" };
		NFL_FIRST_SPORT = new String[] { "NFL" };
		NFL_FIRST_NAME = new String[] { "NFL - 1ST HALVES" };
		NFL_SECOND_SPORT = new String[] { "NFL" };
		NFL_SECOND_NAME = new String[] { "NFL - 2ND HALVES" };
		NCAAF_LINES_SPORT = new String[] { "CFB" };
		NCAAF_LINES_NAME = new String[] { "NCAA FOOTBALL - GAME LINES" };
		NCAAF_FIRST_SPORT = new String[] { "CFB" };
		NCAAF_FIRST_NAME = new String[] { "NCAA FOOTBALL - 1ST HALVES" };
		NCAAF_SECOND_SPORT = new String[] { "CFB" };
		NCAAF_SECOND_NAME = new String[] { "NCAA FOOTBALL - 2ND HALVES" };
		NBA_LINES_SPORT = new String[] { "NBA" };
		NBA_LINES_NAME = new String[] { "NBA - GAME LINES" };
		NBA_FIRST_SPORT = new String[] { "NBA" };
		NBA_FIRST_NAME = new String[] { "NBA - 1ST HALF LINES" };
		NBA_SECOND_SPORT = new String[] { "NBA" };
		NBA_SECOND_NAME = new String[] { "NBA - 2ND HALF LINES" };
		NCAAB_LINES_SPORT = new String[] { "CBB" };
		NCAAB_LINES_NAME = new String[] { "NCAA BK - GAME LINES" };
		NCAAB_FIRST_SPORT = new String[] { "CBB" };
		NCAAB_FIRST_NAME = new String[] { "NCAA BK - 1ST HALF LINES" };
		NCAAB_SECOND_SPORT = new String[] { "CBB" };
		NCAAB_SECOND_NAME = new String[] { "NCAA BKL - 2ND HALF LINES" };
		NHL_LINES_SPORT = new String[] { "NHL" };
		NHL_LINES_NAME = new String[] { "NHL - GAME LINES" };
		NHL_FIRST_SPORT = new String[] { "NHL" };
		NHL_FIRST_NAME = new String[] { "NHL - 1ST PERIOD LINES" };
		NHL_SECOND_SPORT = new String[] { "NHL" };
		NHL_SECOND_NAME = new String[] { "NHL - 2ND PERIOD LINES" };
		NHL_THIRD_SPORT = new String[] { "NHL" };
		NHL_THIRD_NAME = new String[] { "NHL - 3RD PERIOD LINES *O.T,NOT INCL*" };
		WNBA_LINES_SPORT = new String[] { "WNBA" };
		WNBA_LINES_NAME = new String[] { "WNBA" };
		WNBA_FIRST_SPORT = new String[] { "WNBA" };
		WNBA_FIRST_NAME = new String[] { "1H WNBA" };
		WNBA_SECOND_SPORT = new String[] { "WNBA" };
		WNBA_SECOND_NAME = new String[] { "2H WNBA" };
		MLB_LINES_SPORT = new String[] { "MLB" };
		MLB_LINES_NAME = new String[] { "MLB - GAME LINES" };
		MLB_FIRST_SPORT = new String[] { "MLB" };
		MLB_FIRST_NAME = new String[] { "MLB - 1ST 5 FULL INNINGS" };
		MLB_SECOND_SPORT = new String[] { "MLB" };
		MLB_SECOND_NAME = new String[] { "MLB - 2ND HALVES" };

		LOGGER.info("Exiting Action23TDSportsProcessSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final String[][] TDSITES = new String [][] { 
//				{ "http://backend.action23.ag/", "aa602", "Jeremy123", "100", "100", "100", "Chicago", "ET"}
//				{ "http://backend.action23.ag/", "BM2903", "P39", "100", "100", "100", "Chicago", "ET"}
//				{ "http://backend.ace23.ag", "Tony01", "Br23", "100", "100", "100", "Phoenix", "ET"}
				{ "http://backend.ace23.ag", "MR1014", "08grtris", "100", "100", "100", "None", "ET"}
			};

			// Loop through the sites
			for (int i = 0; i < TDSITES.length; i++) {
				final Action23TDSportsProcessSite processSite = new Action23TDSportsProcessSite(TDSITES[i][0], TDSITES[i][1],
						TDSITES[i][2], false, true);
			    processSite.httpClientWrapper.setupHttpClient(TDSITES[0][6]);
			    processSite.processTransaction = false;
			    processSite.timezone = TDSITES[0][7];

			    String xhtml = processSite.loginToSite(TDSITES[i][1], TDSITES[i][2]);

				final Set<PendingEvent> pendingEvents = processSite.getPendingBets(TDSITES[0][0], TDSITES[0][1], new Object());
				if (pendingEvents != null && pendingEvents.size() > 0) {
					final Iterator<PendingEvent> itr = pendingEvents.iterator();
					if (itr != null) {
						while (itr.hasNext()) {
							final PendingEvent pe = itr.next();
							System.out.println("PendingEvent: " + pe);
						}
					}
				}

/*
			    Map<String, String> map = processSite.getMenuFromSite("mlblines", xhtml);
			    LOGGER.debug("map: " + map);

				xhtml = processSite.selectSport("mlblines");
				LOGGER.debug("XHTML: " + xhtml);

				final Iterator<EventPackage> ep = processSite.parseGamesOnSite("mlblines", xhtml);   
			
				MlRecordEvent mre = new MlRecordEvent();
				mre.setMlinputfirstone("");
				mre.setMlplusminusfirstone("");
				mre.setId(new Long(1957));
				mre.setEventname("#951 San Francisco Giants -1.0 +124.0 for 1H");
				mre.setEventtype("ml");
				mre.setSport("mlblines"); 
				mre.setUserid(new Long(6));
				mre.setEventid(new Integer(951));
				mre.setEventid1(new Integer(951));
				mre.setEventid2(new Integer(952));
				
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

		try {
			// Get the pending bets data
			// http://backend.falcon.ag/wager/OpenBetsHelper.aspx?IdPlayer=159790
			String xhtml = getSite(super.populateUrl("OpenBetsHelper.aspx?IdPlayer=" + IdPlayer));
	
			// Parse the data
			pendingWagers = this.siteParser.parsePendingBets(xhtml, accountName, accountId);
		} catch (Throwable t) {
			// do nothing
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
		this.siteParser = TDP = BSP;
		this.siteParser.setTimezone(timezone);
		BSP.setTimezone(timezone);
		final String host = this.httpClientWrapper.getHost();

		String xhtml = getSite(host);
		MAP_DATA = BSP.parseIndex(xhtml);
		MAP_DATA.put("Account", username);
		MAP_DATA.put("Password", password);
		MAP_DATA.put("BtnSubmit", "");
		final List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
		setupNameValuesEmpty(postValuePairs, MAP_DATA, "");

		List<NameValuePair> headerValuePairs = new ArrayList<NameValuePair>(1);
		headerValuePairs.add(new BasicNameValuePair("Content-Type", "application/x-www-form-urlencoded"));
		headerValuePairs.add(new BasicNameValuePair("Upgrade-Insecure-Requests", "1"));
		headerValuePairs.add(new BasicNameValuePair("Referer", host));
		List<NameValuePair> retValue = httpClientWrapper.postSitePage(host + "/Login.aspx", null, postValuePairs, headerValuePairs);
		xhtml = getCookiesAndXhtml(retValue);
		LOGGER.debug("HTML: " + xhtml);

		// Parse login
		MAP_DATA = BSP.parseLogin(xhtml);
		LOGGER.debug("MAP_DATA: " + MAP_DATA);

		// Setup the webapp name
		httpClientWrapper.setWebappname("wager");

		headerValuePairs = new ArrayList<NameValuePair>(1);
		headerValuePairs.add(new BasicNameValuePair("Upgrade-Insecure-Requests", "1"));
		headerValuePairs.add(new BasicNameValuePair("Referer", host + "/wager/Message.aspx"));
		retValue = httpClientWrapper.getSitePage(populateUrl("Welcome.aspx"), null, headerValuePairs);
		xhtml = httpClientWrapper.getCookiesAndXhtml(retValue);
		LOGGER.debug("HTML: " + xhtml);

		headerValuePairs = new ArrayList<NameValuePair>(1);
		headerValuePairs.add(new BasicNameValuePair("Upgrade-Insecure-Requests", "1"));
		headerValuePairs.add(new BasicNameValuePair("Referer", host + "/wager/Welcome.aspx"));
		retValue = httpClientWrapper.getSitePage(populateUrl("CreateSports.aspx?WT=0"), null, headerValuePairs);
		xhtml = httpClientWrapper.getCookiesAndXhtml(retValue);
		LOGGER.debug("HTML: " + xhtml);

		final CookieStore cookieStore = super.getHttpClientWrapper().getContext().getCookieStore();
		final List<Cookie> cookies = cookieStore.getCookies();
		for (Cookie cookie : cookies) {
			final String cookieName = cookie.getName();
			if (cookieName.equals("ACEP_PPH_PLAYER_INFO")) {
				// ={\"IdPlayer\":159790,\"Player\":\"nh664\",\"isError\":false}; expires=Tue, 19 Mar 2019 22:31:10 GMT; path=/; domain=backend.falcon.ag
				String value = cookie.getValue();

				int index = value.indexOf("IdPlayer\":");
				if (index != -1) {
					value = value.substring(index + "IdPlayer\":".length());

					index = value.indexOf(",\"Player");
					if (index != -1) {
						IdPlayer = value.substring(0, index);
					}
				}
			}
		}

		LOGGER.info("Exiting loginToSite()");
		return xhtml;
	}
}