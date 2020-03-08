/**
 * 
 */
package com.ticketadvantage.services.dao.sites.tdsports;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.cookie.Cookie;
import org.apache.log4j.Logger;

import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.PendingEvent;

/**
 * @author jmiller
 *
 */
public class FalconTDSportsProcessSite extends TDSportsProcessSite {
	private static final Logger LOGGER = Logger.getLogger(FalconTDSportsProcessSite.class);
	protected FalconTDSportsParser FP = new FalconTDSportsParser();
	private String IdPlayer = "";

	/**
	 * 
	 * @param host
	 * @param username
	 * @param password
	 * @param isMobile
	 * @param showRequestResponse
	 */
	public FalconTDSportsProcessSite(String host, String username, String password, boolean isMobile, boolean showRequestResponse) {
		super("FalconTDSports", host, username, password, isMobile, showRequestResponse);
		LOGGER.info("Entering FalconTDSportsProcessSite()");

		// Setup the parser
		this.siteParser = FP;

		// Menu items
		NFL_LINES_SPORT = new String[] { "NFL" };
		NFL_LINES_NAME = new String[] { "NFL - GAME LINES" };
		NFL_FIRST_SPORT = new String[] { "NFL" };
		NFL_FIRST_NAME = new String[] { "NFL -  FIRST HALF LINES" };
		NFL_SECOND_SPORT = new String[] { "NFL" };
		NFL_SECOND_NAME = new String[] { "NFL -  SECOND HALF LINES" };
		NCAAF_LINES_SPORT = new String[] { "CFB" };
		NCAAF_LINES_NAME = new String[] { "NCAA FOOTBALL - GAME LINES" };
		NCAAF_FIRST_SPORT = new String[] { "CFB" };
		NCAAF_FIRST_NAME = new String[] { "NCAA FOOTBALL - (1H)" };
		NCAAF_SECOND_SPORT = new String[] { "CFB" };
		NCAAF_SECOND_NAME = new String[] { "NCAA FOOTBALL - (2H)" };
		NBA_LINES_SPORT = new String[] { "NBA" };
		NBA_LINES_NAME = new String[] { "NBA - GAME LINES" };
		NBA_FIRST_SPORT = new String[] { "NBA" };
		NBA_FIRST_NAME = new String[] { "NBA - 1ST HALF LINES" };
		NBA_SECOND_SPORT = new String[] { "NBA" };
		NBA_SECOND_NAME = new String[] { "NBA - 2ND HALF LINES" };
		NCAAB_LINES_SPORT = new String[] { "CBB" };
		NCAAB_LINES_NAME = new String[] { "COLLEGE BASKETBALL - GAME LINES" };
		NCAAB_FIRST_SPORT = new String[] { "CBB" };
		NCAAB_FIRST_NAME = new String[] { "1H COLLEGE BASKETBALL" };
		NCAAB_SECOND_SPORT = new String[] { "CBB" };
		NCAAB_SECOND_NAME = new String[] { "2H COLLEGE BASKETBALL" };
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

		LOGGER.info("Exiting FalconTDSportsProcessSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final String[][] TDSITES = new String [][] { 
//				{ "http://backend.falcon.ag", "nh664", "mk2298", "0", "0", "0", "None", "ET"}
				{ "http://backend.falcon.ag", "nh7023", "mk2298", "0", "0", "0", "None", "ET"}				
			};

			// Loop through the sites
			for (int i = 0; i < TDSITES.length; i++) {
				final FalconTDSportsProcessSite processSite = new FalconTDSportsProcessSite(TDSITES[i][0], TDSITES[i][1],
						TDSITES[i][2], false, true);
			    processSite.httpClientWrapper.setupHttpClient(TDSITES[0][6]);
			    processSite.processTransaction = false;
			    processSite.timezone = TDSITES[0][7];
			    processSite.testSpread(processSite, TDSITES);

/*
			    processSite.loginToSite(TDSITES[i][1], TDSITES[i][2]);
				final Set<PendingEvent> pendingEvents = processSite.getPendingBets(TDSITES[0][0], TDSITES[0][1], null);
				if (pendingEvents != null && !pendingEvents.isEmpty()) {
					Iterator<PendingEvent> itr = pendingEvents.iterator();
					while (itr.hasNext()) {
						LOGGER.error("PendingEvent: " + itr.next());
					}
				}
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

		// Get the pending bets data
		// http://backend.falcon.ag/wager/OpenBetsHelper.aspx?IdPlayer=159790
		String xhtml = getSite(super.populateUrl("OpenBetsHelper.aspx?IdPlayer=" + IdPlayer));
		LOGGER.debug("xhtml: " + xhtml);

		// Parse the data
		pendingWagers = this.siteParser.parsePendingBets(xhtml, accountName, accountId);

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
		FP.setTimezone(timezone);

		MAP_DATA = new HashMap<String, String>();
		String xhtml = getSite(super.httpClientWrapper.getHost());
		MAP_DATA = FP.parseIndex(xhtml);

		// Login info
		MAP_DATA.put("account", username);
		MAP_DATA.put("password", password);
		MAP_DATA.put("IdBook", "1");
		MAP_DATA.put("Redir", "");

		List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
		setupNameValuesEmpty(postValuePairs, MAP_DATA, "");

		xhtml = postSite(super.httpClientWrapper.getHost() + "/Login.aspx", postValuePairs);
		LOGGER.debug("HTML: " + xhtml);

		// Parse login
		MAP_DATA = TDP.parseLogin(xhtml);
		LOGGER.debug("MAP_DATA: " + MAP_DATA);

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

		httpClientWrapper.setWebappname("wager");

		LOGGER.info("Exiting loginToSite()");
		return xhtml;
	}
}