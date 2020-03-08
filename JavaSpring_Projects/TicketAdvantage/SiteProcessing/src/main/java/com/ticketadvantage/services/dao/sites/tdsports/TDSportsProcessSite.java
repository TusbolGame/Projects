/**
 * 
 */
package com.ticketadvantage.services.dao.sites.tdsports;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.sites.ProxyContainer;
import com.ticketadvantage.services.dao.sites.SiteEventPackage;
import com.ticketadvantage.services.dao.sites.SiteProcessor;
import com.ticketadvantage.services.dao.sites.SiteTeamPackage;
import com.ticketadvantage.services.dao.sites.SiteTransaction;
import com.ticketadvantage.services.dao.sites.SiteWagers;
import com.ticketadvantage.services.email.AccessTokenFromRefreshToken;
import com.ticketadvantage.services.email.SendText;
import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.AccountEvent;
import com.ticketadvantage.services.model.BaseRecordEvent;
import com.ticketadvantage.services.model.EventPackage;
import com.ticketadvantage.services.model.MlRecordEvent;
import com.ticketadvantage.services.model.PendingEvent;
import com.ticketadvantage.services.model.SpreadRecordEvent;
import com.ticketadvantage.services.model.TotalRecordEvent;

/**
 * @author jmiller
 *
 */
public class TDSportsProcessSite extends SiteProcessor {
	private static final Logger LOGGER = Logger.getLogger(TDSportsProcessSite.class);
	protected TDSportsParser TDP = new TDSportsParser();
	protected static final String PENDING_BETS = "OpenBets.aspx";
	protected int rerunCount = 0;
	protected boolean resetConnection = false;
	protected int resetAttempts = 0;

	/**
	 * 
	 */
	public TDSportsProcessSite(String host, String username, String password, boolean isMobile, boolean showRequestResponse) {
		super("TDSports", host, username, password, isMobile, showRequestResponse);
		LOGGER.info("Entering TDSportsProcessSite()");

		// Setup the parser
		this.siteParser = TDP;

		// Menu items
		NFL_LINES_SPORT = new String[] { "PRO FOOTBALL", "NFL FOOTBALL", "NFL", "FOOTBALL GAME LINES" };
		NFL_LINES_NAME = new String[] { "NFL - WEEK", "NFL", "NFL - GAME LINES", "NFL PLAYOFFS" };
		NFL_FIRST_SPORT = new String[] { "PRO FOOTBALL", "NFL FOOTBALL", "NFL", "FOOTBALL GAME LINES" };
		NFL_FIRST_NAME = new String[] { "1ST HALVES - NFL", "NFL 1ST HALVES", "NFL - FIRST HALF LINES", "NFL (1H)", "NFL 1H" };
		NFL_SECOND_SPORT = new String[] { "PRO FOOTBALL", "NFL FOOTBALL", "NFL", "FOOTBALL GAME LINES" };
		NFL_SECOND_NAME = new String[] { "2ND HALVES - NFL", "NFL 2ND HALVES", "NFL - SECOND HALF LINES", "NFL (2H)", "NFL 2H" };
		NCAAF_LINES_SPORT = new String[] { "COLLEGE FOOTBALL", "NCAA FOOTBALL", "CFB", "FOOTBALL GAME LINES"};
		NCAAF_LINES_NAME = new String[] { "NCAA FOOTBALL", "NCAA FOOTBALL - GAME LINES", "NCAA FOOTBALL - MEN", "NCAA FOOTBALL  MEN", "NCAA FOOTBALL MEN", "NCAAF" };
		NCAAF_FIRST_SPORT = new String[] { "COLLEGE FOOTBALL", "NCAA FOOTBALL", "CFB", "FOOTBALL GAME LINES" };
		NCAAF_FIRST_NAME = new String[] { "1ST HALVES - CFB", "NCAA FOOTBALL (1H)", "NCAA FOOTBALL - (1H)", "NCAA FB 1ST HALVES", "NCAA FB (1H)", "NCAAF 1H" };
		NCAAF_SECOND_SPORT = new String[] { "COLLEGE FOOTBALL", "NCAA FOOTBALL", "CFB", "FOOTBALL GAME LINES" };
		NCAAF_SECOND_NAME = new String[] { "2ND HALVES - CFB", "NCAA FOOTBALL (2H)", "NCAA FOOTBALL - (2H)", "NCAA FB HALFTIMES", "NCAA FB (2H)", "NCAAF 2H" };
		NBA_LINES_SPORT = new String[] { "PRO BASKETBALL", "NBA BASKETBALL", "NBA", "BASKETBALL GAME LINES" };
		NBA_LINES_NAME = new String[] { "NBA", "NBA - GAME LINES", "TOMORROWS NBA"};
		NBA_FIRST_SPORT = new String[] { "PRO BASKETBALL", "NBA BASKETBALL", "NBA", "BASKETBALL GAME LINES" };
		NBA_FIRST_NAME = new String[] { "1ST HALVES - NBA", "NBA 1ST HALVES", "NBA - FIRST HALF LINES", "NBA (1H)", "NBA - 1ST HALF LINES", "NBA - 1ST HALVES", "NBA 1H" };
		NBA_SECOND_SPORT = new String[] { "PRO BASKETBALL", "NBA BASKETBALL", "NBA", "BASKETBALL GAME LINES" };
		NBA_SECOND_NAME = new String[] { "2ND HALVES - NBA", "NBA 2ND HALVES", "NBA - SECOND HALF LINES", "NBA (2H)", "NBA - 2ND HALF LINES", "NBA - 2ND HALVES", "NBA 2H" };
		NCAAB_LINES_SPORT = new String[] { "COLLEGE BASKETBALL", "CBB", "NCAA BASKETBALL", "BASKETBALL GAME LINES" };
		NCAAB_LINES_NAME = new String[] { "NCAA BASKETBALL", "NCAA BASKETBALL - GAME LINES", "NCAA BASKETBALL - MEN", "NCAA BASKETBALL  MEN", "NCAA BASKETBALL MEN", "NCAA BK - GAME LINES", "COLLEGE BASKETBALL - GAME LINES", "NCAA BASKETBALL - MEN", "NCAAB MEN - EXTRA GAMES" };
		NCAAB_FIRST_SPORT = new String[] { "COLLEGE BASKETBALL", "CBB", "NCAA BASKETBALL", "BASKETBALL GAME LINES" };
		NCAAB_FIRST_NAME = new String[] { "1ST HALVES - CBK", "NCAA BASKETBALL (1H)", "NCAA BASKETBALL - (1H)", "NCAA BK 1ST HALVES", "NCAA BK (1H)", "1ST HALVES - CBB", "1H COLLEGE BASKETBALL", "NCAA BK MENS 1H", "NCAA BASKETBALL - MEN 1H", "NCAAB MEN - 1H", "NCAA BASKETBALL - 1ST HALF", "NCAAB MEN - 1H EXTRA GAMES" };
		NCAAB_SECOND_SPORT = new String[] { "COLLEGE BASKETBALL", "CBB", "NCAA BASKETBALL", "BASKETBALL GAME LINES" };
		NCAAB_SECOND_NAME = new String[] { "2ND HALVES - CBK", "NCAA BASKETBALL (2H)", "NCAA BASKETBALL - (2H)", "NCAA BK HALFTIMES", "NCAA BK (2H)", "2ND HALVES - CBB", "2H COLLEGE BASKETBALL", "NCAA BK MENS 2H", "NCAA BASKETBALL - MEN 2H", "NCAAB MEN - 2H", "NCAA BASKETBALL - 2ND HALF" };
		NHL_LINES_SPORT = new String[] { "NHL HOCKEY", "HOCKEY", "NHL", "HOCKEY GAME LINES" };		
		NHL_LINES_NAME = new String[] { "NHL", "NHL - GAME LINES", "TOMORROWS NHL", "NATIONAL HOCKEY LEAGUE" }; 
		NHL_FIRST_SPORT = new String[] { "NHL HOCKEY", "HOCKEY", "NHL", "HOCKEY GAME LINES" };
		NHL_FIRST_NAME = new String[] { "1ST HALVES - NHL", "NHL 1ST HALVES", "NHL - FIRST HALF LINES", "NHL (1H)", "NHL - 1ST HALF LINES", "NHL - 1ST HALVES", "NHL 1H", "NATIONAL HOCKEY LEAGUE" };
		NHL_SECOND_SPORT = new String[] { "NHL HOCKEY", "HOCKEY", "NHL", "HOCKEY GAME LINES" };
		NHL_SECOND_NAME = new String[] { "2ND HALVES - NHL", "NHL 2ND HALVES", "NHL - SECOND HALF LINES", "NHL (2H)", "NHL - 2ND HALF LINES", "NHL - 2ND HALVES", "NHL 2H", "NATIONAL HOCKEY LEAGUE" };
		WNBA_LINES_NAME = new String[] { "WNBA", "WNBA - GAME LINES" };
		WNBA_LINES_SPORT = new String[] { "WNBA" };
		WNBA_FIRST_NAME = new String[] { "1ST HALVES - WNBA", "WNBA 1ST HALVES", "WNBA - FIRST HALF LINES", "WNBA (1H)", "WNBA - 1ST HALF LINES", "WNBA - 1ST HALVES", "WNBA 1H" };
		WNBA_FIRST_SPORT = new String[] { "WNBA" };
		WNBA_SECOND_NAME = new String[] { "2ND HALVES - WNBA", "WNBA 2ND HALVES", "WNBA - SECOND HALF LINES", "WNBA (2H)", "WNBA - 2ND HALF LINES", "WNBA - 2ND HALVES", "WNBA 2H" };
		WNBA_SECOND_SPORT = new String[] { "WNBA" };
		MLB_LINES_SPORT = new String[] { "BASEBALL GAME LINES" };
		MLB_LINES_NAME = new String[] { "MLB - GAME LINES", "MLB - EXHIBITION GAME LINES" };
		MLB_FIRST_SPORT = new String[] { "BASEBALL GAME LINES" };
		MLB_FIRST_NAME = new String[] { "MLB - 1ST HALVES (5 FULL INNINGS)" };
		MLB_SECOND_SPORT = new String[] { "BASEBALL GAME LINES" };
		MLB_SECOND_NAME = new String[] { "MLB - 2ND HALVES (4 INNINGS+EXTRA INNS)" };

		LOGGER.info("Exiting TDSportsProcessSite()");
	}

	/**
	 * 
	 * @param accountSoftware
	 * @param host
	 * @param username
	 * @param password
	 */
	public TDSportsProcessSite(String accountSoftware, String host, String username, String password, boolean isMobile, boolean showRequestResponse) {
		super(accountSoftware, host, username, password, isMobile, showRequestResponse);
		LOGGER.info("Entering TDSportsProcessSite()");
		LOGGER.info("Exiting TDSportsProcessSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final String[][] TDSITES = new String [][] { 
//				{ "http://www.abcgrand.ag", "q3913", "UTAH", "1000", "1000", "1000", "Asheville", "ET"}
//				{ "http://www.playthedog.net", "Hd114", "pat", "1000", "1000", "1000", "Dallas", "ET"}
//				{ "http://www.betwestcoast.com", "H19", "jake", "500", "500", "500", "Chicago", "ET"}
//				{ "http://www.bigmansports.ag", "mb8032", "sky", "1000", "1000", "1000", "Phoenix", "ET"}
//				{ "http://www.bigmansports.ag", "mb8011", "royals", "500", "500", "500", "Phoenix", "ET"}
//				{ "http://green444.ag", "5106", "69", "1000", "1000", "1000", "Dallas", "ET"},
//				{ "http://www.mybettingstore.com", "Pd1427", "123", "1000", "1000", "1000", "Asheville", "ET"},
//				{ "http://www.Bettenten.com", "xm5352", "a99", "500", "500", "500", "Los Angeles", "ET"}
//				{ "http://www.1BetVegas.com", "aa1370", "NC", "10000", "10000", "10000", "Los Angeles", "ET"}
//				{ "http://www.spot47.com", "aa1370", "NC", "10000", "10000", "10000", "Los Angeles", "ET"}
//				{ "http://www.ibet.ag", "akz", "123", "10000", "10000", "10000", "Baltimore", "ET"},
//				{ "http://www.betcatalinasports.com", "cateb02", "indi", "10000", "10000", "10000", "New York City", "ET"}
//				{ "http://724sports.com/", "BA101", "yellow", "10000", "10000", "10000", "New York City", "ET"}
//				{ "http://wager007.com/", "av16", "marlins33", "0", "0", "0", "Baltimore", "ET"}
//				{ "http://names777.com/", "Rc1525", "John", "0", "0", "0", "Chicago", "ET"}
//				{ "http://jazz247.ag", "js1277", "1277", "0", "0", "0", "Los Angeles", "PT"}
//				{ "http://anysport247.com", "BTB180", "der", "0", "0", "0", "Los Angeles", "PT"}
//				{ "http://backend.wager47.com", "ch218a", "sv1", "0", "0", "0", "Los Angeles", "ET"}
//				{ "http://www.playpls.com/", "vh2303", "poker703", "500", "500", "500", "Phoenix", "ET"}
//				{ "http://wager2.betpls.com/", "VH2329", "poker99", "500", "500", "500", "Chicago", "ET"}
				{ "http://www.fireonsports.ag", "CPS221", "Jy54", "0", "0", "0", "None", "ET"}
			};

//			final TDSportsProcessSite processSite = new TDSportsProcessSite("http://www.1betvegas.com", "MM49", "LB", false, true);
			final TDSportsProcessSite processSite = new TDSportsProcessSite(TDSITES[0][0], TDSITES[0][1], TDSITES[0][2], false, true);
		    processSite.httpClientWrapper.setupHttpClient(TDSITES[0][6]);
		    processSite.processTransaction = false;
		    processSite.timezone = TDSITES[0][7];

			final SpreadRecordEvent sre = new SpreadRecordEvent();
			sre.setSpreadplusminusfirstone("+");
			sre.setSpreadinputfirstone("16");
			sre.setSpreadjuiceplusminusfirstone("-");
			sre.setSpreadinputjuicefirstone("115");
			sre.setId(new Long(520));
			sre.setEventname("NCAAF #333 Texas A&M  +16 (-115) for Game");
			sre.setEventtype("spread");
			sre.setSport("ncaaflines"); 
			sre.setUserid(new Long(1));
			sre.setEventid(new Integer(333));
			sre.setEventid1(new Integer(333));
			sre.setEventid2(new Integer(334));
			sre.setRotationid(new Integer(333));
			sre.setWtype("1");
			sre.setAmount("100");
			final Calendar scal = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
			scal.set(Calendar.HOUR_OF_DAY, 19);
			scal.set(Calendar.MINUTE, 10);
			scal.set(Calendar.SECOND, 0);
			scal.set(Calendar.MONTH, 9);
			scal.set(Calendar.DAY_OF_MONTH, 6);
			scal.set(Calendar.YEAR, 2019);
			sre.setEventdatetime(scal.getTime());
			sre.setDatentime(scal.getTime());
			sre.setEventteam1("");
			sre.setEventteam2("Texas A&M");
			sre.setGroupid(new Long(-99));
			sre.setIscompleted(false);
			sre.setScrappername("UI");
			sre.setActiontype("Standard");
			sre.setTextnumber("");
			sre.setDatecreated(new Date());
			sre.setDatemodified(new Date());
			sre.setCurrentattempts(0);

			final AccountEvent ae = new AccountEvent();
			ae.setAccountid(new Long(451));
			ae.setAmount("100");
			ae.setActualamount("");
			ae.setAttempts(0);
			ae.setCurrentattempts(0);
			ae.setDatecreated(new Date());
			ae.setDatemodified(new Date());
			ae.setEventdatetime(scal.getTime());
			ae.setEventid(303);
			ae.setEventname("NCAAF #333 Texas A&M  +16 (-115) for Game");
			ae.setGroupid(new Long(-99));
			ae.setIscompleted(false);
			ae.setMaxspreadamount(100);
			ae.setMaxtotalamount(100);
			ae.setMaxmlamount(100);
			ae.setMlindicator("-");
			ae.setTimezone("PT");
			ae.setName("690Sports-XXX");
			ae.setOwnerpercentage(100);
			ae.setPartnerpercentage(0);
			ae.setProxy("None");
			ae.setSport("nfllines");
			ae.setMl(new Float(0));
			ae.setMlid(new Long(0));
			ae.setMljuice(new Float(0));
			ae.setSpreadindicator("+");
			ae.setSpread(new Float(16));
			ae.setSpreadid(new Long(333));
			ae.setSpreadjuice(new Float(-115));
			ae.setTotal(new Float(0));
			ae.setTotalid(new Long(0));
			ae.setTotalindicator("");
			ae.setTotaljuice(new Float(0));
			ae.setType("spread");
			ae.setUserid(new Long(1));
			ae.setWagertype("2");
			ae.setStatus("");
			ae.setIscomplexcaptcha(false);
			ae.setHumanspeed(false);

			processSite.processSpreadTransaction(sre, ae);

/*
			String xhtml = processSite.loginToSite(TDSITES[0][1], TDSITES[0][2]);
		    LOGGER.debug("xhtml: " + xhtml);
    
		    Map<String, String> map = processSite.getMenuFromSite("nbalines", xhtml);
		    LOGGER.debug("map: " + map);

			xhtml = processSite.selectSport("nbalines");
			LOGGER.debug("XHTML: " + xhtml);

			final Iterator<EventPackage> ep = processSite.parseGamesOnSite("nbalines", xhtml);
			LOGGER.debug("ep: " + ep);

			Set<PendingEvent> pendingEvents = processSite.getPendingBets(TDSITES[0][1], TDSITES[0][2], null);
			if (pendingEvents != null && !pendingEvents.isEmpty()) {
				Iterator<PendingEvent> itr = pendingEvents.iterator();
				while (itr.hasNext()) {
					System.out.println("PendingEvent: " + itr.next());
				}
			}
*/
/*
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
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#loginToSite(java.lang.String, java.lang.String)
	 */
	@Override
	public String loginToSite(String username, String password) throws BatchException {
		LOGGER.info("Entering loginToSite()");
		LOGGER.debug("username: " + username);
		LOGGER.debug("password: " + password);

		// Setup the timezone
		TDP.setTimezone(timezone);

		// Call the first page
		List<NameValuePair> retValue = httpClientWrapper.getSitePage(httpClientWrapper.getHost(), null,
				setupHeader(false));
		String xhtml = httpClientWrapper.getCookiesAndXhtml(retValue);
		LOGGER.debug("HTML: " + xhtml);

		if (xhtml.contains("document.cookie = ")) {
			LOGGER.debug("xhtml: " + xhtml);
			// { var v = 1482803726 * 3.1415926535898; v = Math.floor(v); document.cookie = "__zjc7733="+v+"; expires=Tue, 27 Dec 2016 01:56:33 UTC; path=/"; window.location='http://wager.abcgrand.ag/'}			
			int varindex = xhtml.indexOf("var v = ");
			int pieindex = xhtml.indexOf(" * 3.1415926535898;");
			if (varindex != -1 && pieindex != -1) {
				String value = xhtml.substring(varindex + "var v = ".length(), pieindex);
				LOGGER.debug("value: " + value);
				Double vInt = Double.parseDouble(value);
				LOGGER.debug("vInt BEFORE: " + vInt);
				vInt = vInt * Double.parseDouble("3.1415926535898");
				LOGGER.debug("vInt AFTER: " + vInt);
				vInt = Math.floor(vInt);
				LOGGER.debug("vInt: " + vInt);

				int cookieIndex = xhtml.indexOf("document.cookie = \"");
				int vIndex = xhtml.indexOf("=\"+v+\";");
				if (cookieIndex != -1 && vIndex != -1) {
					String cookieName = xhtml.substring(cookieIndex + "document.cookie = \"".length(), vIndex);
					LOGGER.debug("cookieName: " + cookieName);

					final BasicCookieStore cookieStore = new BasicCookieStore();
				    final BasicClientCookie cookie = new BasicClientCookie(cookieName, vInt.toString());
				    cookie.setDomain("wager.abcgrand.ag");
				    cookie.setPath("/");
				    cookieStore.addCookie(cookie);
				    LOGGER.debug("cookieStore: " + cookieStore);
				    httpClientWrapper.setCookieStore(cookieStore);
				}
			}
		}

		retValue = httpClientWrapper.checkForRedirect(retValue, httpClientWrapper.getHost(), setupHeader(false), null, httpClientWrapper.getWebappname() + "/");
		xhtml = httpClientWrapper.getCookiesAndXhtml(retValue);
		LOGGER.debug("XHTML: " + xhtml);

		// Get home page data
		MAP_DATA = TDP.parseIndex(xhtml);
		LOGGER.debug("MAP_DATA: " + MAP_DATA);

		// Setup the username/password data
		setUsernamePassword(username, password);

		if (xhtml.contains("spot47")) {
			MAP_DATA.put("ctl00$MainContent$ctlLogin$BtnSubmit.x", "54");
			MAP_DATA.put("ctl00$MainContent$ctlLogin$BtnSubmit.y", "11");
		}
		LOGGER.debug("Map: " + MAP_DATA);

		if (httpClientWrapper.getHost().contains("betpls")) {
			String action = MAP_DATA.get("action");
			action = action.replace("//wager2.betpls.com", "/");
			MAP_DATA.put("action", action);
		}

		List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
		String actionLogin = setupNameValuesEmpty(postValuePairs, MAP_DATA, "");
		LOGGER.debug("ActionLogin: " + actionLogin);

		// Call the login
		xhtml = authenticate(actionLogin, postValuePairs);
		LOGGER.debug("HTML: " + xhtml);

		// Set the webappname now ONLY IF for playthedob.net
		if (httpClientWrapper.getPreviousUrl() != null && 
			(httpClientWrapper.getPreviousUrl().contains("playthedog") ||
			 httpClientWrapper.getPreviousUrl().contains("1betvegas") ||
			 httpClientWrapper.getPreviousUrl().contains("1BetVegas") ||
			 httpClientWrapper.getPreviousUrl().contains("betcatalinasports") ||
			 httpClientWrapper.getPreviousUrl().contains("spot47") || 
			 httpClientWrapper.getPreviousUrl().contains("724sports"))) {
			httpClientWrapper.setWebappname(determineWebappName(httpClientWrapper.getPreviousUrl()));
		} else if (httpClientWrapper.getPreviousUrl() != null && 
				(httpClientWrapper.getPreviousUrl().contains("names777") ||
				 httpClientWrapper.getPreviousUrl().contains("wager47") || 
				 httpClientWrapper.getPreviousUrl().contains("sport247"))) {
			httpClientWrapper.setWebappname("wager");
		}

		// Parse login
		MAP_DATA = TDP.parseLogin(xhtml);
		LOGGER.debug("MAP_DATA: " + MAP_DATA);

		postValuePairs = new ArrayList<NameValuePair>(1);
		actionLogin = setupNameValuesEmpty(postValuePairs, MAP_DATA, "");
		LOGGER.debug("ActionLogin: " + actionLogin);

		if (httpClientWrapper.getPreviousUrl().contains("www.betcatalinasports.com") || 
			httpClientWrapper.getPreviousUrl().contains("wager47")) {
			xhtml = getSite(populateUrl("CreateSports.aspx?WT=0"));
		} else if (actionLogin != null) {
			// Call post login form
			xhtml = postSite(actionLogin, postValuePairs);
			LOGGER.debug("HTML: " + xhtml);

			String pUrl = httpClientWrapper.getPreviousUrl();
			LOGGER.debug("pUrl: " + pUrl);

			// Set the webappname
			httpClientWrapper.setWebappname(determineWebappName(pUrl));

			if (pUrl != null && !pUrl.contains("CreateSports.aspx?WT=0")) {
				xhtml = getSite(populateUrl("CreateSports.aspx?WT=0"));
			}
		}

		LOGGER.info("Exiting loginToSite()");
		return xhtml;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#processSpreadTransaction(com.ticketadvantage.services.model.SpreadRecordEvent, com.ticketadvantage.services.model.AccountEvent)
	 */
	@Override
	public void processSpreadTransaction(SpreadRecordEvent event, AccountEvent ae) throws BatchException {
		LOGGER.info("Entering processSpreadTransaction()");
		LOGGER.debug("SpreadRecordEvent: " + event);
		LOGGER.debug("AccountEvent: " + ae);

		final EventPackage eventPackage = setupTransaction(event,  ae); 
		LOGGER.debug("EventPackage" + eventPackage);

		if (eventPackage != null) {
			// Finish the transaction
			finishTransaction(null, eventPackage, event, ae);
		}

		LOGGER.info("Exiting processSpreadTransaction()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#processTotalTransaction(com.ticketadvantage.services.model.TotalRecordEvent, com.ticketadvantage.services.model.AccountEvent)
	 */
	@Override
	public void processTotalTransaction(TotalRecordEvent event, AccountEvent ae) throws BatchException {
		LOGGER.info("Entering processTotalTransaction()");
		LOGGER.debug("TotalRecordEvent: " + event);
		LOGGER.debug("AccountEvent: " + ae);
		
		final EventPackage eventPackage = setupTransaction(event,  ae); 
		LOGGER.debug("EventPackage" + eventPackage);

		if (eventPackage != null) {
			// Finish the transaction
			finishTransaction(null, eventPackage, event, ae);
		}

		LOGGER.info("Exiting processTotalTransaction()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#processMlTransaction(com.ticketadvantage.services.model.MlRecordEvent, com.ticketadvantage.services.model.AccountEvent)
	 */
	@Override
	public void processMlTransaction(MlRecordEvent event, AccountEvent ae) throws BatchException {
		LOGGER.info("Entering processMlTransaction()");
		LOGGER.debug("MlRecordEvent: " + event);
		LOGGER.debug("AccountEvent: " + ae);

		final EventPackage eventPackage = setupTransaction(event,  ae); 
		LOGGER.debug("EventPackage" + eventPackage);

		if (eventPackage != null) {
			// Finish the transaction
			finishTransaction(null, eventPackage, event, ae);
		}

		LOGGER.info("Exiting processMlTransaction()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#finishTransaction(com.ticketadvantage.services.dao.sites.SiteTransaction, com.ticketadvantage.services.model.EventPackage, com.ticketadvantage.services.model.BaseRecordEvent, com.ticketadvantage.services.model.AccountEvent)
	 */
	@Override
	public void finishTransaction(SiteTransaction siteTransaction, EventPackage eventPackage, BaseRecordEvent event, AccountEvent ae) throws BatchException {
		LOGGER.info("Entering finishTransaction()");
		String xhtml = "";

		try {
			//
			// Call the Game selection
			//
			// Step 12 - Select event and parse wager info
			xhtml = selectEvent(siteTransaction, eventPackage, event, ae);
			LOGGER.debug("xhtml: " + xhtml);
	
			// Step 13 - Parse the event selection
			parseEventSelection(xhtml, siteTransaction, eventPackage, event, ae);

			// Step 13 1/2 - Setup site transaction
			if (event instanceof SpreadRecordEvent) {
				siteTransaction = getSpreadTransaction((SpreadRecordEvent)event, eventPackage, ae);
			} else if (event instanceof TotalRecordEvent) {
				siteTransaction = getTotalTransaction((TotalRecordEvent)event, eventPackage, ae);
			} else if (event instanceof MlRecordEvent) {
				siteTransaction = getMlTransaction((MlRecordEvent)event, eventPackage, ae);	
			}

			//
			// Call the Complete Wager
			//
			// Step 15 - Complete transaction
			xhtml = completeTransaction(siteTransaction, eventPackage, event, ae);

			// Step 16 - Parse the ticket #
			String ticketNumber = parseTicketTransaction(xhtml);

			// Set the account data
			ae.setAccountconfirmation(ticketNumber);
			ae.setAccounthtml("<![CDATA[" + xhtml + "]]>");
		} catch (BatchException be) {
			if (be.getErrorcode() == BatchErrorCodes.LINE_CHANGED_ERROR) {
				final String type = event.getEventtype();
				if ("spread".equals(type)) {
					processSpreadTransaction((SpreadRecordEvent)event, ae);
				} else if ("total".equals(type)) {
					processTotalTransaction((TotalRecordEvent)event, ae);
				} else if ("ml".equals(type)) {
					processMlTransaction((MlRecordEvent)event, ae);
				}
			} else if (be.getErrorcode() == BatchErrorCodes.WAGER_HAS_EXPIRED) {
				final String type = event.getEventtype();
				if ("spread".equals(type)) {
					processSpreadTransaction((SpreadRecordEvent)event, ae);
				} else if ("total".equals(type)) {
					processTotalTransaction((TotalRecordEvent)event, ae);
				} else if ("ml".equals(type)) {
					processMlTransaction((MlRecordEvent)event, ae);
				}
			} else if (be.getErrorcode() == BatchErrorCodes.HTTP_BAD_GATEWAY_EXCEPTION) {
				final String[] proxyLocations = ProxyContainer.getProxyNames();
				for (int x = 0; (proxyLocations != null && x < proxyLocations.length); x++) {
					String proxyName = proxyLocations[x];
					if (proxyName != null && !proxyName.equals("None") && !proxyName.equals(ae.getProxy())) {
						ae.setProxy(proxyName);
						break;
					}
				}
				final String type = event.getEventtype();
				if ("spread".equals(type)) {
					processSpreadTransaction((SpreadRecordEvent)event, ae);
				} else if ("total".equals(type)) {
					processTotalTransaction((TotalRecordEvent)event, ae);
				} else if ("ml".equals(type)) {
					processMlTransaction((MlRecordEvent)event, ae);
				}
			} else { 
				if (be.getHtml() == null || (be.getHtml() != null && be.getHtml().length() == 0)) {
					be.setHtml(xhtml);
					ae.setAccounthtml(xhtml);
				}
				throw be;
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		LOGGER.info("Exitng finishTransaction()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#selectSport(java.lang.String)
	 */
	@Override
	protected String selectSport(String type) throws BatchException {
		LOGGER.info("Entering selectSport()");
		LOGGER.debug("type: " + type);

		// Process the setup page
		final List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
		final String actionName = setupNameValuesEmpty(postValuePairs, MAP_DATA, httpClientWrapper.getWebappname());
		LOGGER.debug("ActionName: " + actionName);

		// Post to site page
		LOGGER.debug("HTTP POST REQUEST: " + actionName);
		for (NameValuePair postValuePair: postValuePairs) {
			LOGGER.debug(postValuePair.getName() + ": " + postValuePair.getValue());
		}
		String xhtml = postSite(actionName, postValuePairs);
		LOGGER.debug("HTML: " + xhtml);

		LOGGER.info("Exiting selectSport()");
		return xhtml;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#createSiteTransaction()
	 */
	@Override
	protected SiteTransaction createSiteTransaction() {
		LOGGER.info("Entering createSiteTransaction()");

		final TDSportsTransaction tdSportsTransaction = new TDSportsTransaction();

		LOGGER.info("Exiting createSiteTransaction()");
		return tdSportsTransaction;
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

		final String actionName = setupNameValuesEmpty(postNameValuePairs, MAP_DATA, httpClientWrapper.getWebappname());
		LOGGER.debug("ActionName: " + actionName);

		// Setup the wager
		final String xhtml = postSite(actionName, postNameValuePairs);
		LOGGER.debug("HTML: " + xhtml);

		LOGGER.info("Exiting selectEvent()");
		return xhtml;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#parseEventSelection(java.lang.String, com.ticketadvantage.services.dao.sites.SiteTransaction, com.ticketadvantage.services.model.EventPackage, com.ticketadvantage.services.model.BaseRecordEvent, com.ticketadvantage.services.model.AccountEvent)
	 */
	@Override
	protected void parseEventSelection(String xhtml, SiteTransaction siteTransaction, EventPackage eventPackage, BaseRecordEvent event, AccountEvent ae) throws BatchException {
		LOGGER.info("Entering parseEventSelection()");
		TDSportsTeamPackage teamPackage = null;

		MAP_DATA.clear();
		gamesXhtml = xhtml;
		if (event instanceof SpreadRecordEvent) {			
			teamPackage = (TDSportsTeamPackage)SiteWagers.setupTeam(SiteWagers.determineSpreadData((SpreadRecordEvent)event), eventPackage);
			MAP_DATA = TDP.parseEventSelection(xhtml, teamPackage, "spread");
		} else if (event instanceof TotalRecordEvent) {
			teamPackage = (TDSportsTeamPackage)SiteWagers.setupTeam(SiteWagers.determineTotalData((TotalRecordEvent)event), eventPackage);
			MAP_DATA = TDP.parseEventSelection(xhtml, teamPackage, "total");
		} else if (event instanceof MlRecordEvent) {
			teamPackage = (TDSportsTeamPackage)SiteWagers.setupTeam(SiteWagers.determineMoneyLineData((MlRecordEvent)event), eventPackage);
			MAP_DATA = TDP.parseEventSelection(xhtml, teamPackage, "ml");
		}
		LOGGER.debug("MAP_DATA: " + MAP_DATA);

		// Check for a wager limit and rerun
		if (MAP_DATA.containsKey("wageramount")) {
			throw new BatchException(BatchErrorCodes.MAX_WAGER_AMOUNT_REACHED, BatchErrorMessage.MAX_WAGER_AMOUNT_REACHED, "Wager amount Exceeded. Limit For This Game is $" + MAP_DATA.get("wageramount"), xhtml);
		} else if (MAP_DATA.containsKey("wagerbalanceexceeded")) {
			throw new BatchException(BatchErrorCodes.ACCOUNT_BALANCE_LIMIT, BatchErrorMessage.ACCOUNT_BALANCE_LIMIT, "The maximum account balance has been reached for wager $" + siteTransaction.getAmount());
		}

		LOGGER.info("Exiting parseEventSelection()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#completeTransaction(com.ticketadvantage.services.dao.sites.SiteTransaction, com.ticketadvantage.services.model.EventPackage, com.ticketadvantage.services.model.BaseRecordEvent, com.ticketadvantage.services.model.AccountEvent)
	 */
	@Override
	protected String completeTransaction(SiteTransaction siteTransaction, EventPackage eventPackage, BaseRecordEvent event, AccountEvent ae) throws BatchException {
		LOGGER.info("Entering completeTransaction()");
		String xhtml = null;

		try {
			if (processTransaction) {
				// Process the wager transaction
				xhtml = processWager(siteTransaction, eventPackage, event, ae, event.getWtype());
				
				// Confirm the wager
				xhtml = confirmWager(xhtml, siteTransaction, eventPackage, event, ae, event.getWtype());
			}
		} catch (BatchException be) {
			LOGGER.error("Exception getting ticket number for account event " + ae + " event " + event, be);
			throw be;
		}

		LOGGER.info("Exiting completeTransaction()");
		return xhtml;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#parseTicketTransaction(java.lang.String)
	 */
	@Override
	protected String parseTicketTransaction(String xhtml) throws BatchException {
		LOGGER.info("Entering parseTicketTransaction()");
		
		final String ticketNumber = TDP.parseTicketNumber(xhtml);
		LOGGER.debug("ticketNumber: " + ticketNumber);

		LOGGER.info("Exiting parseTicketTransaction()");
		return ticketNumber;		
	}

	/**
	 * 
	 * @param siteTransaction
	 * @param eventPackage
	 * @param event
	 * @param ae
	 * @param wagerType
	 * @return
	 * @throws BatchException
	 */
	private String processWager(SiteTransaction siteTransaction, 
			EventPackage eventPackage, 
			BaseRecordEvent event, 
			AccountEvent ae, 
			String wagerType) throws BatchException {
		LOGGER.info("Entering processWager()");
		SiteTransaction tempSiteTransaction = null;
		SiteTeamPackage theteam = null;
		final SiteEventPackage siteEventPackage = (SiteEventPackage)eventPackage;
		final SiteTeamPackage stp1 = siteEventPackage.getSiteteamone();
		final SiteTeamPackage stp2 = siteEventPackage.getSiteteamtwo();

		if (event.getRotationid().toString().contains(stp1.getId().toString())) {
			theteam = stp1;
		} else {
			theteam = stp2;
		}

		// Get the spread transaction
		if (event instanceof SpreadRecordEvent) {
			tempSiteTransaction = getSpreadTransaction((SpreadRecordEvent)event, eventPackage, ae);

			if (tempSiteTransaction.getRiskorwin() != null && 
				tempSiteTransaction.getRiskorwin().intValue() == 2) {
				float[] sitejuices = getSiteSpreadJuice(theteam);
				float spreadJuice = getJuice(ae.getSpreadjuice(), sitejuices);

				if (spreadJuice > 0) {
					wagerType = "1";
				}
			}
		} else if (event instanceof TotalRecordEvent) {
			tempSiteTransaction = getTotalTransaction((TotalRecordEvent)event, eventPackage, ae);

			if (tempSiteTransaction.getRiskorwin() != null && 
				tempSiteTransaction.getRiskorwin().intValue() == 2) {
				float[] sitejuices = getSiteTotalJuice(theteam);
				float totalJuice = getJuice(ae.getTotaljuice(), sitejuices);

				if (totalJuice > 0) {
					wagerType = "1";
				}
			}
		} else if (event instanceof MlRecordEvent) {
			tempSiteTransaction = getMlTransaction((MlRecordEvent)event, eventPackage, ae);

			if (tempSiteTransaction.getRiskorwin() != null && 
				tempSiteTransaction.getRiskorwin().intValue() == 2) {
				float[] sitejuices = getSiteMlJuice(theteam);
				float mlJuice = getJuice(ae.getMljuice(), sitejuices);

				if (mlJuice > 0) {
					wagerType = "1";
				}
			}
		}

		// Reset the $$ if we have to based on risk vs win
		String siteAmount = determineRiskWinAmounts(siteTransaction, eventPackage, event, ae); 
		LOGGER.error("siteAmount: " + siteAmount);
		siteTransaction.setAmount(siteAmount);
		ae.setActualamount(siteAmount);

		final List<NameValuePair> postNameValuePairs = new ArrayList<NameValuePair>(1);
		MAP_DATA.put("WAMT_", siteAmount);

		// Now check if selection needs to be sent as well
		if (siteTransaction.getSelectName() != null && siteTransaction.getOptionValue() != null) {
			MAP_DATA.put(siteTransaction.getSelectName(), siteTransaction.getOptionValue());
		}

		// Check for risk or win
		if ("1".equals(wagerType)) {
			MAP_DATA.put("RISKWIN", "2");
		} else {
			MAP_DATA.put("RISKWIN", "1");
		}

		final String actionName = setupNameValuesEmpty(postNameValuePairs, MAP_DATA, httpClientWrapper.getWebappname());
		LOGGER.debug("ActionName: " + actionName);

		// Setup the wager
		final String xhtml = postSite(actionName, postNameValuePairs);
		LOGGER.debug("HTML: " + xhtml);

		LOGGER.info("Exiting processWager()");
		return xhtml;
	}

	/**
	 * 
	 * @param xhtml
	 * @param siteTransaction
	 * @param eventPackage
	 * @param event
	 * @param ae
	 * @param wagerType
	 * @return
	 * @throws BatchException
	 */
	protected String confirmWager(String xhtml, SiteTransaction siteTransaction, EventPackage eventPackage, BaseRecordEvent event, AccountEvent ae, String wagerType) throws BatchException {
		LOGGER.info("Entering confirmWager()");
		
		if (processTransaction) {
			if (xhtml.contains("The line changed for one")) {
				LOGGER.error("LINE CHANGE OCCURRED");
				MAP_DATA = TDP.processLineChange(xhtml);
				final String valindicator = MAP_DATA.get("newvalindicator");
				final String val = MAP_DATA.get("newval");
				final String juiceindicatorval = MAP_DATA.get("newjuiceindicator");
				final String juiceval = MAP_DATA.get("newjuice");
	
				// If we don't have valid values we have to produce a line change error
				if (val == null || val.length() == 0 || juiceval == null || juiceval.length() == 0) {
					throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed", xhtml);
				}
	
				if (event instanceof SpreadRecordEvent) {
					final SiteWagers sw = new SiteWagers();
					final Map<String, String> map = sw.determineSpread((SpreadRecordEvent)event);
					float spread = Float.parseFloat(map.get("valindicator") + map.get("val"));
					float siteSpread = Float.parseFloat(valindicator + val);
	
					if (spread <= siteSpread) {
						float juice = Float.parseFloat(map.get("juiceindicator") + map.get("juice"));
						float siteJuice = Float.parseFloat(juiceindicatorval + juiceval);
	
						if (juice <= siteJuice) {
							MAP_DATA.remove("ctl00$WagerContent$can_1");
							MAP_DATA.put("password", this.httpClientWrapper.getPassword());
	
							final List<NameValuePair> postNameValuePairs = new ArrayList<NameValuePair>(1);
							final String actionName = setupNameValuesEmpty(postNameValuePairs, MAP_DATA, httpClientWrapper.getWebappname());
							LOGGER.debug("ActionName: " + actionName);
	
							ae.setSpread(siteSpread);
							ae.setSpreadindicator(valindicator);
							ae.setSpreadjuice(siteJuice);
	
							// Setup the wager
							xhtml = postSite(actionName, postNameValuePairs);
							LOGGER.debug("HTML: " + xhtml);
	
							if (xhtml.contains("The line changed for one")) {
								throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed", xhtml);
							}						
						}
					}
				} else if (event instanceof TotalRecordEvent) {
					final SiteWagers sw = new SiteWagers();
					final Map<String, String> map = sw.determineTotal((TotalRecordEvent)event);
					float total = Float.parseFloat(map.get("valindicator") + map.get("val"));
					float siteTotal = Float.parseFloat(val);
					
					if ("o".equals(valindicator)) {
						if (total >= siteTotal) {
							float juice = Float.parseFloat(map.get("juiceindicator") + map.get("juice"));
							float siteJuice = Float.parseFloat(juiceindicatorval + juiceval);
	
							if (juice <= siteJuice) {
								MAP_DATA.remove("ctl00$WagerContent$can_1");
								MAP_DATA.put("password", this.httpClientWrapper.getPassword());
		
								final List<NameValuePair> postNameValuePairs = new ArrayList<NameValuePair>(1);
								final String actionName = setupNameValuesEmpty(postNameValuePairs, MAP_DATA, httpClientWrapper.getWebappname());
								LOGGER.debug("ActionName: " + actionName);
	
								ae.setTotal(siteTotal);
								ae.setTotalindicator(valindicator);
								ae.setTotaljuice(siteJuice);
								
								// Setup the wager
								xhtml = postSite(actionName, postNameValuePairs);
								LOGGER.debug("HTML: " + xhtml);
		
								if (xhtml.contains("The line changed for one")) {
									throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed", xhtml);
								}						
							}
						}
					} else {
						if (total <= siteTotal) {
							float juice = Float.parseFloat(map.get("juiceindicator") + map.get("juice"));
							float siteJuice = Float.parseFloat(juiceindicatorval + juiceval);
	
							if (juice <= siteJuice) {
								MAP_DATA.remove("ctl00$WagerContent$can_1");
								MAP_DATA.put("password", this.httpClientWrapper.getPassword());
		
								final List<NameValuePair> postNameValuePairs = new ArrayList<NameValuePair>(1);
								final String actionName = setupNameValuesEmpty(postNameValuePairs, MAP_DATA, httpClientWrapper.getWebappname());
								LOGGER.debug("ActionName: " + actionName);
		
								ae.setTotal(siteTotal);
								ae.setTotalindicator(valindicator);
								ae.setTotaljuice(siteJuice);
	
								// Setup the wager
								xhtml = postSite(actionName, postNameValuePairs);
								LOGGER.debug("HTML: " + xhtml);
		
								if (xhtml.contains("The line changed for one")) {
									throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed", xhtml);
								}						
							}
						}
					}
				} else if (event instanceof MlRecordEvent) {
					final SiteWagers sw = new SiteWagers();
					final Map<String, String> map = sw.determineMoneyLine((MlRecordEvent)event);
					float ml = Float.parseFloat(map.get("valindicator") + map.get("val"));
					float siteml = Float.parseFloat(valindicator + val);
	
					if (ml <= siteml) {
						MAP_DATA.remove("ctl00$WagerContent$can_1");
						MAP_DATA.put("password", this.httpClientWrapper.getPassword());
	
						final List<NameValuePair> postNameValuePairs = new ArrayList<NameValuePair>(1);
						final String actionName = setupNameValuesEmpty(postNameValuePairs, MAP_DATA,
								httpClientWrapper.getWebappname());
						LOGGER.debug("ActionName: " + actionName);
						
						ae.setMl(siteml);
						ae.setMlindicator(valindicator);
						ae.setMljuice(siteml);
	
						// Setup the wager
						xhtml = postSite(actionName, postNameValuePairs);
						LOGGER.debug("HTML: " + xhtml);
	
						if (xhtml.contains("The line changed for one")) {
							throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR,
									BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed", xhtml);
						}				
					}
				}
			}
	
			// Parse the Confirm Wager page
			Map<String, String> wagers = TDP.parseConfirmWager(xhtml);
	
			// Check for a wager limit and rerun
			if (wagers.containsKey("wagerminamount")) {
				throw new BatchException(BatchErrorCodes.MIN_WAGER_AMOUNT_NOT_REACHED, BatchErrorMessage.MIN_WAGER_AMOUNT_NOT_REACHED, "The minimum wager amount has NOT been reached $" + wagers.get("wagerminamount"), xhtml);
			}
	
			// Check if we have exceeded
			if (wagers.containsKey("wagerbalanceexceeded")) {
				throw new BatchException(BatchErrorCodes.ACCOUNT_BALANCE_LIMIT, BatchErrorMessage.ACCOUNT_BALANCE_LIMIT, "The maximum account balance has been reached for wager " + siteTransaction.getAmount(), xhtml);
			}
	
			// Check for a wager limit and rerun
			if (wagers.containsKey("wageramount")) {
				// Only call the select event once
				if (rerunCount++ == 0) {
					if (wagers.containsKey("goback")) {
						final List<NameValuePair> postNameValuePairs = new ArrayList<NameValuePair>(1);
						final String actionName = setupNameValuesEmpty(postNameValuePairs, MAP_DATA, httpClientWrapper.getWebappname());
						super.getSite(actionName);
					}
					siteTransaction.setAmount(wagers.get("wageramount"));
					final List<NameValuePair> postNameValuePairs = new ArrayList<NameValuePair>(1);
					MAP_DATA.put("WAMT_", wagers.get("wageramount"));
					ae.setActualamount(wagers.get("wageramount"));
	
					// Now check if selection needs to be sent as well
					if (siteTransaction.getSelectName() != null && siteTransaction.getOptionValue() != null) {
						MAP_DATA.put(siteTransaction.getSelectName(), siteTransaction.getOptionValue());
					}
	
					// Check for risk or win
					if ("1".equals(wagerType)) {
						MAP_DATA.put("RISKWIN", "2");
					} else {
						MAP_DATA.put("RISKWIN", "1");
					}
					final String actionName = setupNameValuesEmpty(postNameValuePairs, MAP_DATA, httpClientWrapper.getWebappname());
					LOGGER.debug("ActionName: " + actionName);
	
					// Setup the wager
					LOGGER.debug("HTTP POST REQUEST: " + actionName);
					for (NameValuePair postValuePair: postNameValuePairs) {
						LOGGER.debug(postValuePair.getName() + ": " + postValuePair.getValue());
					}
					xhtml = postSite(actionName, postNameValuePairs);
					LOGGER.debug("HTML: " + xhtml);
	
					// Parse the Confirm Wager page
					MAP_DATA = TDP.parseConfirmWager(xhtml);
	
					// Check for a wager limit and rerun
					if (MAP_DATA.containsKey("wageramount")) {
						throw new BatchException(BatchErrorCodes.MAX_WAGER_AMOUNT_REACHED, BatchErrorMessage.MAX_WAGER_AMOUNT_REACHED, "The maximum wager amount has been reached $" + MAP_DATA.get("wageramount"), xhtml);
					} else if (MAP_DATA.containsKey("wagerminamount")) {
						throw new BatchException(BatchErrorCodes.MIN_WAGER_AMOUNT_NOT_REACHED, BatchErrorMessage.MIN_WAGER_AMOUNT_NOT_REACHED, "The minimum wager amount has NOT been reached $" + MAP_DATA.get("wagerminamount"), xhtml);
					} else if (MAP_DATA.containsKey("wagerbalanceexceeded")) {
						throw new BatchException(BatchErrorCodes.ACCOUNT_BALANCE_LIMIT, BatchErrorMessage.ACCOUNT_BALANCE_LIMIT, "Site account balance exceeded for transaction in the amount of $" + siteTransaction.getAmount(), xhtml);
					}
				}
			} else {
				MAP_DATA = wagers;			
			}
	
			LOGGER.error("MAP_DATA: " + MAP_DATA);
	
			// Check for a valid password and upper case or not
			if (MAP_DATA.containsKey("password")) {
				if (MAP_DATA.containsKey("toUpperCase") && MAP_DATA.get("toUpperCase").equals("true")) {
					MAP_DATA.put("password", httpClientWrapper.getPassword().toUpperCase());
					MAP_DATA.remove("toUpperCase");
				} else {
					MAP_DATA.put("password", httpClientWrapper.getPassword());
				}
			} else if (MAP_DATA.containsKey("Password")) {
				if (MAP_DATA.containsKey("toUpperCase") && MAP_DATA.get("toUpperCase").equals("true")) {
					MAP_DATA.put("Password", httpClientWrapper.getPassword().toUpperCase());
					MAP_DATA.remove("toUpperCase");
				} else {
					MAP_DATA.put("Password", httpClientWrapper.getPassword());
				}
			}
	
			// Get the action
			final List<NameValuePair> postNameValuePairs = new ArrayList<NameValuePair>(1);
			final String actionName = setupNameValuesEmpty(postNameValuePairs, MAP_DATA, httpClientWrapper.getWebappname());
			LOGGER.debug("ActionName: " + actionName);
	
			// Process the wager
			LOGGER.debug("HTTP POST REQUEST: " + actionName);
			for (NameValuePair postValuePair: postNameValuePairs) {
				LOGGER.debug(postValuePair.getName() + ": " + postValuePair.getValue());
			}
			xhtml = postSite(actionName, postNameValuePairs);
			LOGGER.debug("HTML: " + xhtml);
	
			if (xhtml.contains("The line changed for one (or more) of your selections.")) {
				throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed", xhtml);
			}
		}

		LOGGER.info("Exiting confirmWager()");
		return xhtml;
	}

	/**
	 * 
	 * @param username
	 * @param password
	 */
	protected void setUsernamePassword(String username, String password) {
		LOGGER.info("Entering setUsernamePassword()");

		// Setup the username/password data
		final Set<Entry<String, String>> indexs = MAP_DATA.entrySet();
		if (indexs != null && !indexs.isEmpty()) {
			final Iterator<Entry<String, String>> itr = indexs.iterator();
			while (itr != null && itr.hasNext()) {
				final Entry<String, String> values = itr.next();
				String key = values.getKey();
				String value = values.getValue();
				LOGGER.debug("KEY: " + key);
				LOGGER.debug("VALUE: " + value);
				if (key.contains("UserName") || key.contains("account") || key.contains("Account")) {
					MAP_DATA.put(key, username);
				} else if (key.contains("password") || key.contains("Password")) {
					if (MAP_DATA.containsKey("toUpperCase") && MAP_DATA.get("toUpperCase").equals("true")) {
						MAP_DATA.put(key, password.toUpperCase());
					} else {
						MAP_DATA.put(key, password);
					}
				}
			}
		}

		// Remove the toUpperCase if there is one
		if (MAP_DATA.containsKey("toUpperCase") && MAP_DATA.get("toUpperCase").equals("true")) {
			MAP_DATA.remove("toUpperCase");
		}

		LOGGER.info("Exiting setUsernamePassword()");
	}
}