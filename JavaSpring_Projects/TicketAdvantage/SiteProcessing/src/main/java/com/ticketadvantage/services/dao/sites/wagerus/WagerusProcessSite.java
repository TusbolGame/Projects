/**
 * 
 */
package com.ticketadvantage.services.dao.sites.wagerus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.sites.tdsports.TDSportsProcessSite;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.PendingEvent;

/**
 * @author jmiller
 *
 */
public class WagerusProcessSite extends TDSportsProcessSite {
	private static final Logger LOGGER = Logger.getLogger(WagerusProcessSite.class);
	protected WagerusParser NSP = new WagerusParser();

	/**
	 * 
	 * @param host
	 * @param username
	 * @param password
	 * @param isMobile
	 * @param showRequestResponse
	 */
	public WagerusProcessSite(String host, String username, String password, boolean isMobile,
			boolean showRequestResponse) {
		super("Wagerus", host, username, password, isMobile, showRequestResponse);
		LOGGER.info("Entering WagerusProcessSite()");

		// Setup the parser
		this.siteParser = NSP;

		// Menu items
		// Menu items
		NFL_LINES_SPORT = new String[] { "NFL FOOTBALL" };
		NFL_LINES_NAME = new String[] { "NFL - GAME LINES" };
		NFL_FIRST_SPORT = new String[] { "NFL FOOTBALL" };
		NFL_FIRST_NAME = new String[] { "NFL (1H)" };
		NFL_SECOND_SPORT = new String[] { "NFL FOOTBALL" };
		NFL_SECOND_NAME = new String[] { "NFL (2H)" };
		NCAAF_LINES_SPORT = new String[] { "NCAA FOOTBALL" };
		NCAAF_LINES_NAME = new String[] { "NCAA FOOTBALL - GAME LINES" };
		NCAAF_FIRST_SPORT = new String[] { "NCAA FOOTBALL" };
		NCAAF_FIRST_NAME = new String[] { "NCAA FB (1H)" };
		NCAAF_SECOND_SPORT = new String[] { "NCAA FOOTBALL" };
		NCAAF_SECOND_NAME = new String[] { "NCAA FB (2H)" };
		NBA_LINES_SPORT = new String[] { "NBA BASKETBALL" };
		NBA_LINES_NAME = new String[] { "NBA - GAME LINES" };
		NBA_FIRST_SPORT = new String[] { "NBA BASKETBALL" };
		NBA_FIRST_NAME = new String[] { "NBA (1H)" };
		NBA_SECOND_SPORT = new String[] { "NBA BASKETBALL" };
		NBA_SECOND_NAME = new String[] { "NBA (2H)" };
		NCAAB_LINES_SPORT = new String[] { "NCAA BASKETBALL" };
		NCAAB_LINES_NAME = new String[] { "NCAA BK - GAME LINES" };
		NCAAB_FIRST_SPORT = new String[] { "NCAA BASKETBALL" };
		NCAAB_FIRST_NAME = new String[] { "NCAA BK (1H)" };
		NCAAB_SECOND_SPORT = new String[] { "NCAA BASKETBALL" };
		NCAAB_SECOND_NAME = new String[] { "NCAA BK (2H)" };
		NHL_LINES_SPORT = new String[] { "HOCKEY" };
		NHL_LINES_NAME = new String[] { "NHL - GAME LINES" };
		NHL_FIRST_SPORT = new String[] { "HOCKEY" };
		NHL_FIRST_NAME = new String[] { "NHL (1P)" };
		NHL_SECOND_SPORT = new String[] { "HOCKEY" };
		NHL_SECOND_NAME = new String[] { "NHL (2P)" };
		NHL_THIRD_SPORT = new String[] { "HOCKEY" };
		NHL_THIRD_NAME = new String[] { "NHL (3P)" };
		WNBA_LINES_SPORT = new String[] { "OTHER BASKETBALL", "WNBA" };
		WNBA_LINES_NAME = new String[] { "WNBA GAME LINES" };
		WNBA_FIRST_SPORT = new String[] { "OTHER BASKETBALL", "WNBA" };
		WNBA_FIRST_NAME = new String[] { "WNBA - (1H)", "WNBA 1ST HALFS" };
		WNBA_SECOND_SPORT = new String[] { "OTHER BASKETBALL", "WNBA" };
		WNBA_SECOND_NAME = new String[] { "WNBA - (2H)", "WNBA 2ND HALFS" };
		MLB_LINES_SPORT = new String[] { "BASEBALL" };
		MLB_LINES_NAME = new String[] { "MLB GAME LINES", "MLB - GAME LINES" };
		MLB_FIRST_SPORT = new String[] { "BASEBALL" };
		MLB_FIRST_NAME = new String[] { "MLB 1ST 5  INNINGS", "MLB 1ST 5 FULL INNINGS" };
		MLB_SECOND_SPORT = new String[] { "BASEBALL" };
		MLB_SECOND_NAME = new String[] { "MLB 2ND HALVES", "MLB - 2ND HALVES" };

		LOGGER.info("Exiting WagerusProcessSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final String[][] TDSITES = new String [][] { 
				{ "http://wagerus.ag", "BM11204", "gg6", "1", "1", "1", "None", "ET"}
			};

			// Loop through the sites
			for (int i = 0; i < TDSITES.length; i++) {
				final WagerusProcessSite processSite = new WagerusProcessSite(TDSITES[i][0], TDSITES[i][1],
						TDSITES[i][2], false, true);
			    processSite.httpClientWrapper.setupHttpClient(TDSITES[0][6]);
			    processSite.processTransaction = false;
			    processSite.timezone = TDSITES[0][7];
			    processSite.siteParser = processSite.TDP = processSite.NSP;
			    processSite.siteParser.setTimezone(TDSITES[0][7]);
			    processSite.NSP.setTimezone(TDSITES[0][7]);
			    String xhtml = processSite.loginToSite(TDSITES[i][1], TDSITES[i][2]);

				final Set<PendingEvent> pendingEvents = processSite.getPendingBets(TDSITES[0][1], TDSITES[0][2], null);
				if (pendingEvents != null && !pendingEvents.isEmpty()) {
					final Iterator<PendingEvent> itr = pendingEvents.iterator();
					while (itr.hasNext()) {
						System.out.println("PendingEvent: " + itr.next());
					}
				}
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
		String xhtml = getSite(this.httpClientWrapper.getHost());
		MAP_DATA = NSP.parseIndex(xhtml);
		MAP_DATA.remove("username");
		MAP_DATA.remove("password");
		MAP_DATA.put("Account", username);
		MAP_DATA.put("Password", password.toLowerCase());
		final List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
		setupNameValuesEmpty(postValuePairs, MAP_DATA, "");

		xhtml = postSite(this.httpClientWrapper.getHost() + "/core/pcr/wagerus.ag/login.aspx", postValuePairs);
		LOGGER.debug("HTML: " + xhtml);

		// Parse login
		MAP_DATA = NSP.parseLogin(xhtml);
		LOGGER.debug("MAP_DATA: " + MAP_DATA);

		// Setup the webapp name
		httpClientWrapper.setWebappname("core/pcr/wagerus.ag/wager");

		// CreateSports.aspx
		xhtml = getSite(populateUrl("CreateSports.aspx?WT=0"));
		LOGGER.debug("HTML: " + xhtml);

		LOGGER.info("Exiting loginToSite()");
		return xhtml;
	}
}