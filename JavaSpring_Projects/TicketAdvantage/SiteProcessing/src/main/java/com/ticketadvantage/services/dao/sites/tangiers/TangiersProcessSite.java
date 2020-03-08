/**
 * 
 */
package com.ticketadvantage.services.dao.sites.tangiers;

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

import com.ticketadvantage.services.dao.sites.SiteEventPackage;
import com.ticketadvantage.services.dao.sites.SiteTeamPackage;
import com.ticketadvantage.services.dao.sites.SiteTransaction;
import com.ticketadvantage.services.dao.sites.SiteWagers;
import com.ticketadvantage.services.dao.sites.tdsports.TDSportsProcessSite;
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
public class TangiersProcessSite extends TDSportsProcessSite {
	private static final Logger LOGGER = Logger.getLogger(TangiersProcessSite.class);
	private static final String SPORTS = "Sports.aspx";
	private static final String GET_ACTIVE = "betslip/getActiveLeagues.asp";
	private static final String GET_LINES = "betslip/getLinesbyLeague.asp";
	private static final String GET_STRAIGHT = "betslip/getstraight.asp";
	private static final String DO_BET = "betslip/doBet.asp";
	protected TangiersParser NSP = new TangiersParser();
	protected String gameId;
	protected Map<String, String> globalHiddenVariables;

	/**
	 * 
	 */
	public TangiersProcessSite(String host, String username, String password, boolean isMobile,
			boolean showRequestResponse) {
		super("Tangiers", host, username, password, isMobile, showRequestResponse);
		LOGGER.info("Entering TangiersProcessSite()");

		// Setup the parser
		this.siteParser = NSP;

		// Menu items
		NFL_LINES_SPORT = new String[] { "NFL" };
		NFL_LINES_NAME = new String[] { "NFL" };
		NFL_FIRST_SPORT = new String[] { "NFL" };
		NFL_FIRST_NAME = new String[] { "NFL - 1ST HALF" };
		NFL_SECOND_SPORT = new String[] { "NFL" };
		NFL_SECOND_NAME = new String[] { "NFL - 2ND HALF" };
		NCAAF_LINES_SPORT = new String[] { "NCAA FOOTBALL" };
		NCAAF_LINES_NAME = new String[] { "NCAAF - GAME" };
		NCAAF_FIRST_SPORT = new String[] { "NCAA FOOTBALL" };
		NCAAF_FIRST_NAME = new String[] { "NCAAF - 1ST HALF" };
		NCAAF_SECOND_SPORT = new String[] { "NCAA FOOTBALL" };
		NCAAF_SECOND_NAME = new String[] { "NCAAF - 2ND HALF" };
		NBA_LINES_SPORT = new String[] { "NBA" };
		NBA_LINES_NAME = new String[] { "NBA - GAME" };
		NBA_FIRST_SPORT = new String[] { "NBA" };
		NBA_FIRST_NAME = new String[] { "NBA - 1ST HALF" };
		NBA_SECOND_SPORT = new String[] { "NBA" };
		NBA_SECOND_NAME = new String[] { "NBA - 2ND HALF" };
		NCAAB_LINES_SPORT = new String[] { "NCAA BASKETBALL" };
		NCAAB_LINES_NAME = new String[] { "NCAA", "NCAA ADDED", "NCAA EXTRA" };
		NCAAB_FIRST_SPORT = new String[] { "NCAA BASKETBALL" };
		NCAAB_FIRST_NAME = new String[] { "NCAA 1ST HALF", "NCAA ADDED 1ST HALF", "NCAA EXTRA 1ST HALF" };
		NCAAB_SECOND_SPORT = new String[] { "NCAA BASKETBALL" };
		NCAAB_SECOND_NAME = new String[] { "NCAA 2ND HALF" };
		NHL_LINES_SPORT = new String[] { "NHL" };
		NHL_LINES_NAME = new String[] { "NHL - GAMES" };
		NHL_FIRST_SPORT = new String[] { "NHL" };
		NHL_FIRST_NAME = new String[] { "NHL - PERIODS" };
		NHL_SECOND_SPORT = new String[] { "NHL" };
		NHL_SECOND_NAME = new String[] { "NHL - PERIODS" };
		NHL_THIRD_SPORT = new String[] { "NHL" };
		NHL_THIRD_NAME = new String[] { "NHL - PERIODS" };
		WNBA_LINES_NAME = new String[] { "WNBA" };
		WNBA_LINES_SPORT = new String[] { "BASKETBALL GAME LINES" };
		WNBA_FIRST_NAME = new String[] { "WNBA 1st Half" };
		WNBA_FIRST_SPORT = new String[] { "BASKETBALL GAME LINES" };
		WNBA_SECOND_NAME = new String[] { "WNBA 2nd Half" };
		WNBA_SECOND_SPORT = new String[] { "BASKETBALL GAME LINES" };
		MLB_LINES_SPORT = new String[] { "BASEBALL GAME LINES" };
		MLB_LINES_NAME = new String[] { "MAJOR LEAGUE BASEBALL", "MLB - EXHIBITION GAME LINES" };
		MLB_FIRST_SPORT = new String[] { "BASEBALL GAME LINES" };
		MLB_FIRST_NAME = new String[] { "MLB 1H (5 FULL INNINGS)" };
		MLB_SECOND_SPORT = new String[] { "BASEBALL GAME LINES" };
		MLB_SECOND_NAME = new String[] { "MLB 2H (4 FULL INNINGS+EXTRA INNS)" };

		LOGGER.info("Exiting TangiersProcessSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final String[][] TDSITES = new String [][] { 
//				{ "https://tangiers.ag/", "WW8806", "t666", "100", "100", "100", "None", "ET"}
//				{ "https://tangiers.ag/", "SSS1191", "NR", "100", "100", "100", "None", "ET"}
//				{ "https://Vegas365.bet/", "YL825", "dark", "100", "100", "100", "None", "ET"}
//				{ "https://tangiers.ag/", "NY118", "BERRY", "100", "100", "100", "None", "ET"}
				{ "https://tangiers.ag/", "XF675", "PAPPAS4037", "100", "100", "100", "None", "ET"}
			};

			// Loop through the sites
			for (int i = 0; i < TDSITES.length; i++) {
				final TangiersProcessSite processSite = new TangiersProcessSite(TDSITES[i][0], TDSITES[i][1],
						TDSITES[i][2], false, true);
			    processSite.httpClientWrapper.setupHttpClient(TDSITES[0][6]);
			    processSite.processTransaction = false;
			    processSite.timezone = TDSITES[0][7];
			    processSite.siteParser = processSite.TDP = processSite.NSP;
			    processSite.siteParser.setTimezone(TDSITES[0][7]);
			    processSite.NSP.setTimezone(TDSITES[0][7]);

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
			    String xhtml = processSite.loginToSite(TDSITES[i][1], TDSITES[i][2]);
			    LOGGER.debug("xhtml: " + xhtml);

				final Set<PendingEvent> pendingEvents = processSite.getPendingBets(TDSITES[0][1], TDSITES[0][2], null);
				if (pendingEvents != null && !pendingEvents.isEmpty()) {
					Iterator<PendingEvent> itr = pendingEvents.iterator();
					while (itr.hasNext()) {
						System.out.println("PendingEvent: " + itr.next());
					}
				}

			    final Map<String, String> map = processSite.getMenuFromSite("nbalines", xhtml);
			    LOGGER.debug("map: " + map);

				xhtml = processSite.selectSport("nbalines");
				LOGGER.debug("XHTML: " + xhtml);

				final Iterator<EventPackage> ep = processSite.parseGamesOnSite("nbalines", xhtml);   

				final SpreadRecordEvent tre = new SpreadRecordEvent();
				tre.setSpreadplusminussecondone("-");
				tre.setSpreadinputsecondone("8");
				tre.setSpreadjuiceplusminussecondone("-");
				tre.setSpreadinputjuicesecondone("110");
				tre.setId(new Long(557));
				tre.setEventname("NBA #558 GOLDEN STATE WARRIORS -8.0 -120.0 for Game");
				tre.setEventtype("spread");
				tre.setSport("nbalines"); 
				tre.setUserid(new Long(6));
				tre.setEventid(new Integer(557));
				tre.setEventid1(new Integer(557));
				tre.setEventid2(new Integer(558));
				tre.setRotationid(new Integer(558));
				tre.setWtype("2");
				tre.setAmount("100");
*/
/*
				TotalRecordEvent tre = new TotalRecordEvent();
				tre.setTotalinputfirstone("152");
				tre.setTotalinputjuicefirstone("120");
				tre.setTotaljuiceplusminusfirstone("-");
				tre.setEventname("NCAAB #607 DENVER vrs WYOMING o152.0 -120.0 for Game");
				tre.setEventtype("total");
				tre.setSport("ncaablines"); 
				tre.setUserid(new Long(6));
				tre.setEventid(new Integer(741));
				tre.setEventid1(new Integer(741));
				tre.setEventid2(new Integer(742));
				tre.setRotationid(607);
*/
/*				final MlRecordEvent mre = new MlRecordEvent();
				mre.setMlinputfirstone("");
				mre.setMlplusminusfirstone("");
				mre.setId(new Long(1927));
				mre.setEventname("#1928 1H SEA MARINERS W LEBLANC -L -0.5 -130.0 for 1H");
				mre.setEventtype("ml");
				mre.setSport("mlbfirst");
				mre.setUserid(new Long(6));
				mre.setEventid(new Integer(1927));
				mre.setEventid1(new Integer(1927));
				mre.setEventid2(new Integer(1928));
				mre.setRotationid(new Integer(1928));
*/
/*
				// Step 9 - Find event
				final EventPackage eventPackage = processSite.findEvent(ep, tre);
				if (eventPackage == null) {
					throw new BatchException(BatchErrorCodes.GAME_NOT_AVAILABLE, BatchErrorMessage.GAME_NOT_AVAILABLE, "Game cannot be found on site for " + tre.getEventname(), xhtml);
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

		String xhtml = getSiteNoBr(this.httpClientWrapper.getHost());
		MAP_DATA = NSP.parseIndex(xhtml);
		MAP_DATA.put("Account", username);
		MAP_DATA.put("password", password.toUpperCase());
		
		List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
		setupNameValuesEmpty(postValuePairs, MAP_DATA, "");

		final List<NameValuePair> headerValuePairs = new ArrayList<NameValuePair>(4);
		headerValuePairs.add(new BasicNameValuePair("Host", "tangiers.ag"));
		headerValuePairs.add(new BasicNameValuePair("Referer", this.httpClientWrapper.getHost()));
		headerValuePairs.add(new BasicNameValuePair("Upgrade-Insecure-Requests", "1"));

		xhtml = super.postSiteNoBr(this.httpClientWrapper.getHost() + "/app/login.aspx", postValuePairs);
		LOGGER.debug("XHTML: " + xhtml);

		// Setup the webapp name
		httpClientWrapper.setWebappname("app/wager");

		xhtml = getSiteNoBr(populateUrl("Message.aspx"));
		LOGGER.debug("HTML: " + xhtml);

		// Parse login
		MAP_DATA = NSP.parseLogin(xhtml);
		LOGGER.debug("MAP_DATA: " + MAP_DATA);

		// Get the Sports.aspx
		xhtml = getSiteNoBr(populateUrl(SPORTS));
		LOGGER.debug("XHTML: " + xhtml);

		// Get the hidden input fields
		MAP_DATA = NSP.parseSports(xhtml);
		copyGlobalVariables();

		postValuePairs = new ArrayList<NameValuePair>(1);
		postValuePairs.add(new BasicNameValuePair("pid", MAP_DATA.get("pid")));
		postValuePairs.add(new BasicNameValuePair("idp", MAP_DATA.get("idp")));
		postValuePairs.add(new BasicNameValuePair("idlt", MAP_DATA.get("idlt")));
		postValuePairs.add(new BasicNameValuePair("idlan", MAP_DATA.get("idlan")));

		xhtml = postSiteNoBr(populateUrl(GET_ACTIVE), postValuePairs);
		LOGGER.debug("XHTML: " + xhtml);

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
		String xhtml = getSiteNoBr(super.populateUrl(PENDING_BETS));
		LOGGER.debug("xhtml: " + xhtml);

		if (xhtml != null && (xhtml.contains("No Open Bets") || xhtml.contains("Currently no bets are pending in this account"))) {
			// Do nothing
			resetAttempts = 0;
			pendingWagers = new HashSet<PendingEvent>();
		} else if (xhtml != null && (xhtml.contains("Open Bets") || xhtml.contains("Open Wagers") || xhtml.contains("OPEN WAGERS")) && (!xhtml.contains("No Open Bets") && !xhtml.contains("Currently no bets are pending in this account"))) {
			pendingWagers = this.siteParser.parsePendingBets(xhtml, accountName, accountId);
			resetAttempts = 0;
		} else {
			if (xhtml != null && (xhtml.contains("loginform") || xhtml.contains("member_login.aspx") || xhtml.contains("loginD"))) {
				LOGGER.error("Resetting the connection for " + accountName + " " + accountId);
				HttpClientUtils.closeQuietly(this.httpClientWrapper.getClient());
				this.httpClientWrapper.setupHttpClient(this.httpClientWrapper.getProxyName());
				this.loginToSite(this.httpClientWrapper.getUsername(), this.httpClientWrapper.getPassword());
				xhtml = getSite(super.populateUrl(PENDING_BETS));
				pendingWagers = this.siteParser.parsePendingBets(xhtml, accountName, accountId);
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
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#selectSport(java.lang.String)
	 */
	@Override
	protected String selectSport(String type) throws BatchException {
		LOGGER.info("Entering selectSport()");
		LOGGER.debug("type: " + type);

		final String idl = MAP_DATA.get("idl");
		
		// Process the setup page
		final List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
		postValuePairs.add(new BasicNameValuePair("aid", globalHiddenVariables.get("aid")));
		postValuePairs.add(new BasicNameValuePair("idc", globalHiddenVariables.get("idc")));
		postValuePairs.add(new BasicNameValuePair("idl", idl));
		postValuePairs.add(new BasicNameValuePair("idlan", globalHiddenVariables.get("idlan")));
		postValuePairs.add(new BasicNameValuePair("idls", globalHiddenVariables.get("idls")));
		postValuePairs.add(new BasicNameValuePair("idlt", globalHiddenVariables.get("idlt")));
		postValuePairs.add(new BasicNameValuePair("idp", globalHiddenVariables.get("idp")));
		postValuePairs.add(new BasicNameValuePair("idpl", globalHiddenVariables.get("idpl")));
		postValuePairs.add(new BasicNameValuePair("mlbl", globalHiddenVariables.get("mlbl")));
		postValuePairs.add(new BasicNameValuePair("nhll", globalHiddenVariables.get("nhll")));
		postValuePairs.add(new BasicNameValuePair("pid", globalHiddenVariables.get("pid")));
		postValuePairs.add(new BasicNameValuePair("utc", globalHiddenVariables.get("utc")));

		// Post to site page
		String xhtml = postSiteNoBr(populateUrl(GET_LINES), postValuePairs);
		LOGGER.debug("HTML: " + xhtml);

		LOGGER.info("Exiting selectSport()");
		return xhtml;
	}

	// https://vegas365.bet/app/wager/betslip/getstraight.asp?pid=186529&idp=1&idc=1226089&b=2_173720_-53_-115%2C&c=1&amt=0
	
	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#createSiteTransaction()
	 */
	@Override
	protected SiteTransaction createSiteTransaction() {
		LOGGER.info("Entering createSiteTransaction()");

		final SiteTransaction siteTransaction = new SiteTransaction();

		LOGGER.info("Exiting createSiteTransaction()");
		return siteTransaction;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#selectEvent(com.ticketadvantage.services.dao.sites.SiteTransaction, com.ticketadvantage.services.model.EventPackage, com.ticketadvantage.services.model.BaseRecordEvent, com.ticketadvantage.services.model.AccountEvent)
	 */
	@Override
	protected String selectEvent(SiteTransaction siteTransaction, EventPackage eventPackage, BaseRecordEvent event,
			AccountEvent ae) throws BatchException {
		LOGGER.info("Entering selectEvent()");
		SiteTeamPackage teamPackage = null;
		String idValue = null;

		// Get the appropriate record event
		if (event instanceof SpreadRecordEvent) {			
			teamPackage = SiteWagers.setupTeam(SiteWagers.determineSpreadData((SpreadRecordEvent)event), eventPackage);
			LOGGER.debug("teamPackage: " + teamPackage);
			if (teamPackage != null && 
				teamPackage.getGameSpreadInputId() != null &&
				teamPackage.getGameSpreadInputId().length() > 0) {
				idValue = teamPackage.getGameSpreadInputId();
			} else {
				throw new BatchException(BatchErrorCodes.XHTML_GAMES_PARSING_EXCEPTION, BatchErrorMessage.XHTML_GAMES_PARSING_EXCEPTION, "No spread available for this game");
			}
		} else if (event instanceof TotalRecordEvent) {
			teamPackage = SiteWagers.setupTeam(SiteWagers.determineTotalData((TotalRecordEvent)event), eventPackage);
			LOGGER.debug("teamPackage: " + teamPackage);
			if (teamPackage != null && 
				teamPackage.getGameTotalInputId() != null && 
				teamPackage.getGameTotalInputId().length() > 0) {
				idValue = teamPackage.getGameTotalInputId();
			} else {
				throw new BatchException(BatchErrorCodes.XHTML_GAMES_PARSING_EXCEPTION, BatchErrorMessage.XHTML_GAMES_PARSING_EXCEPTION, "No total available for this game");
			}
		} else if (event instanceof MlRecordEvent) {
			teamPackage = SiteWagers.setupTeam(SiteWagers.determineMoneyLineData((MlRecordEvent)event), eventPackage);
			LOGGER.debug("teamPackage: " + teamPackage);
			if (teamPackage != null && 
				teamPackage.getGameMLInputId() != null &&
				teamPackage.getGameMLInputId().length() > 0) {
				idValue = teamPackage.getGameMLInputId();
			} else {
				throw new BatchException(BatchErrorCodes.XHTML_GAMES_PARSING_EXCEPTION, BatchErrorMessage.XHTML_GAMES_PARSING_EXCEPTION, "No moneyline available for this game");
			}
		}

		//
		// https://playitez.com/wager/betslip/getstraight.asp?pid=6801&idp=2775&idc=209198423&b=4_1961452_0_175%2C&c=1&amt=0
		//
		final StringBuffer sb = new StringBuffer(200);
		sb.append(GET_STRAIGHT);
		sb.append("?pid=").append(globalHiddenVariables.get("pid"));
		sb.append("&idp=").append(globalHiddenVariables.get("idp"));
		sb.append("&idc=").append(globalHiddenVariables.get("idc"));
		sb.append("&b=").append(idValue + "%2C");
		sb.append("&c=").append("1");
		sb.append("&amt=0");

		// Copy b parameter
		globalHiddenVariables.put("b", idValue);

		// Setup the wager
		String xhtml = getSiteNoBr(populateUrl(sb.toString()));
		LOGGER.debug("XHTML: " + xhtml);

		// Check for line changes
		if (xhtml.contains("The line changed for one")) {
			throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed error", xhtml);
		}

		LOGGER.info("Exiting selectEvent()");
		return xhtml;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#parseEventSelection(java.lang.String, com.ticketadvantage.services.dao.sites.SiteTransaction, com.ticketadvantage.services.model.EventPackage, com.ticketadvantage.services.model.BaseRecordEvent, com.ticketadvantage.services.model.AccountEvent)
	 */
	@Override
	protected void parseEventSelection(String xhtml, SiteTransaction siteTransaction, EventPackage eventPackage,
			BaseRecordEvent event, AccountEvent ae) throws BatchException {
		LOGGER.info("Entering parseEventSelection()");
		SiteTeamPackage teamPackage = null;

		MAP_DATA.clear();
		gamesXhtml = xhtml;
		if (event instanceof SpreadRecordEvent) {
			teamPackage = (SiteTeamPackage)SiteWagers.setupTeam(SiteWagers.determineSpreadData((SpreadRecordEvent)event), eventPackage);
			MAP_DATA = NSP.parseEventSelection(xhtml, teamPackage, "spread");
		} else if (event instanceof TotalRecordEvent) {
			teamPackage = (SiteTeamPackage)SiteWagers.setupTeam(SiteWagers.determineTotalData((TotalRecordEvent)event), eventPackage);
			MAP_DATA = NSP.parseEventSelection(xhtml, teamPackage, "total");
		} else if (event instanceof MlRecordEvent) {
			teamPackage = (SiteTeamPackage)SiteWagers.setupTeam(SiteWagers.determineMoneyLineData((MlRecordEvent)event), eventPackage);
			MAP_DATA = NSP.parseEventSelection(xhtml, teamPackage, "ml");
		}
		LOGGER.debug("MAP_DATA: " + MAP_DATA);

		LOGGER.info("Exiting parseEventSelection()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#completeTransaction(com.ticketadvantage.services.dao.sites.SiteTransaction, com.ticketadvantage.services.model.EventPackage, com.ticketadvantage.services.model.BaseRecordEvent, com.ticketadvantage.services.model.AccountEvent)
	 */
	@Override
	protected String completeTransaction(SiteTransaction siteTransaction, EventPackage eventPackage,
			BaseRecordEvent event, AccountEvent ae) throws BatchException {
		LOGGER.info("Entering completeTransaction()");
		String xhtml = null;

		try {
			if (processTransaction) {
				// Check if we have exceeded
				if (MAP_DATA.containsKey("wagerbalanceexceeded")) {
					throw new BatchException(BatchErrorCodes.ACCOUNT_BALANCE_LIMIT, BatchErrorMessage.ACCOUNT_BALANCE_LIMIT, "The maximum account balance has been reached for wager " + siteTransaction.getAmount(), xhtml);
				}

				// Process the wager transaction
				xhtml = processWager(eventPackage, event, ae, event.getWtype());

				// Parse any errors
				NSP.parseWagerTypes(xhtml);

				// Confirm the wager
				xhtml = confirmWager(xhtml, siteTransaction, eventPackage, event, ae, event.getWtype());
				
				if (xhtml.contains("The line changed for one")) {
					xhtml = processLineChange(xhtml, event, ae);
				}
			}
		} catch (BatchException be) {
			LOGGER.error("Exception completing transaction account event " + ae + " event " + event, be);
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
		String ticketNumber = null;

		try {
			ticketNumber = NSP.parseTicketNumber(xhtml);
			LOGGER.debug("ticketNumber: " + ticketNumber);
		} catch (BatchException be) {
			throw be;
		}

		LOGGER.info("Exiting parseTicketTransaction()");
		return ticketNumber;		
	}
	
	/**
	 * 
	 * @param eventPackage
	 * @param event
	 * @param ae
	 * @param wagerType
	 * @return
	 * @throws BatchException
	 */
	protected String processWager(EventPackage eventPackage, BaseRecordEvent event, AccountEvent ae, String wagerType) throws BatchException {
		LOGGER.info("Entering processWager()");
		SiteTransaction siteTransaction = null;
		String siteAmount = null;

		// Get the spread transaction
		if (event instanceof SpreadRecordEvent) {
			siteTransaction = getSpreadTransaction((SpreadRecordEvent)event, eventPackage, ae);
			siteAmount = siteTransaction.getAmount();
			LOGGER.error("siteAmount: " + siteAmount);
			Double sAmount = Double.parseDouble(siteAmount);
			if (sAmount != null) {
				SiteEventPackage siteEventPackage = (SiteEventPackage)eventPackage;
				LOGGER.debug("SiteEventPackage: " + siteEventPackage);
				if (siteTransaction.getRiskorwin().intValue() == 1 && siteEventPackage.getSpreadMax() != null) {
					if (sAmount.doubleValue() > siteEventPackage.getSpreadMax().intValue()) {
						siteAmount = siteEventPackage.getSpreadMax().toString();
					}
				} else if (siteTransaction.getRiskorwin().intValue() == 2) {
					Double wagerAmount = Double.valueOf(siteAmount);
					float spreadJuice = ae.getSpreadjuice();
					Double risk = wagerAmount * (100 / spreadJuice);
					risk = round(risk.doubleValue(), 2);
					LOGGER.error("Risk: " + risk);
					if (siteEventPackage.getSpreadMax() != null && risk.doubleValue() > siteEventPackage.getSpreadMax().intValue()) {
						siteAmount = siteEventPackage.getSpreadMax().toString();
					} else {
						siteAmount = risk.toString();
					}
				}
			}
		} else if (event instanceof TotalRecordEvent) {
			siteTransaction = getTotalTransaction((TotalRecordEvent)event, eventPackage, ae);
			siteAmount = siteTransaction.getAmount();
			LOGGER.error("siteAmount: " + siteAmount);
			Double tAmount = Double.parseDouble(siteAmount);
			if (tAmount != null) {
				SiteEventPackage siteEventPackage = (SiteEventPackage)eventPackage;
				LOGGER.debug("SiteEventPackage: " + siteEventPackage);
				if (siteTransaction.getRiskorwin().intValue() == 1 && siteEventPackage.getTotalMax() != null) {
					if (tAmount.doubleValue() > siteEventPackage.getTotalMax().intValue()) {
						siteAmount = siteEventPackage.getTotalMax().toString();
					}
				} else if (siteTransaction.getRiskorwin().intValue() == 2) {
					Double wagerAmount = Double.valueOf(siteAmount);
					float totalJuice = ae.getTotaljuice();
					Double risk = wagerAmount * (100 / totalJuice);
					risk = round(risk.doubleValue(), 2);
					LOGGER.error("Risk: " + risk);
					if (siteEventPackage.getTotalMax() != null && risk.doubleValue() > siteEventPackage.getTotalMax().intValue()) {
						siteAmount = siteEventPackage.getTotalMax().toString();
					} else {
						siteAmount = risk.toString();
					}
				}
			}
		} else if (event instanceof MlRecordEvent) {
			siteTransaction = getMlTransaction((MlRecordEvent)event, eventPackage, ae);
			siteAmount = siteTransaction.getAmount();
			LOGGER.error("siteAmount: " + siteAmount);
			Double mAmount = Double.parseDouble(siteAmount);
			if (mAmount != null) {
				SiteEventPackage siteEventPackage = (SiteEventPackage)eventPackage;
				LOGGER.debug("SiteEventPackage: " + siteEventPackage);
				if (siteTransaction.getRiskorwin().intValue() == 1 && siteEventPackage.getMlMax() != null) {
					if (mAmount.doubleValue() > siteEventPackage.getMlMax().intValue()) {
						siteAmount = siteEventPackage.getMlMax().toString();
					}
				} else if (siteTransaction.getRiskorwin().intValue() == 2) {
					Double wagerAmount = Double.valueOf(siteAmount);
					float mlJuice = ae.getMljuice();
					Double risk = wagerAmount * (100 / mlJuice);
					risk = round(risk.doubleValue(), 2);
					LOGGER.error("Risk: " + risk);
					if (siteEventPackage.getMlMax() != null && risk.doubleValue() > siteEventPackage.getMlMax().intValue()) {
						siteAmount = siteEventPackage.getMlMax().toString();
					} else {
						siteAmount = risk.toString();
					}
				}
			}
		}

		siteAmount = siteAmount.replace(".00", "").replace(".0", "");
		LOGGER.debug("siteAmount: " + siteAmount);
		ae.setActualamount(siteAmount);

		// Setup the wager
		String xhtml = null;
		if (processTransaction) {
			// Now check if selection needs to be sent as well
			gameId = globalHiddenVariables.get("b");
			if (gameId != null) {
				final String sport = event.getSport();
				final String eventtype = event.getEventtype();
				if (sport != null && sport.contains("mlb") && eventtype.equals("ml")) {
					gameId += ("_" + siteAmount + "_3");
				} else {
					gameId += ("_" + siteAmount + "_0");
				}
			}

			//
			// https://playitez.com/wager/betslip/getstraight.asp?pid=6801&idp=2775&idc=209198423&b=4_1961452_0_175_0_3%2C&c=1&amt=1&fp=0
			//
			final StringBuffer sb = new StringBuffer(200);
			sb.append(GET_STRAIGHT);
			sb.append("?pid=").append(globalHiddenVariables.get("pid"));
			sb.append("&idp=").append(globalHiddenVariables.get("idp"));
			sb.append("&idc=").append(globalHiddenVariables.get("idc"));
			sb.append("&b=").append(gameId + "%2C");
			sb.append("&c=").append("1");
			sb.append("&amt=1");
			sb.append("&fp=0");

			// Copy b
			globalHiddenVariables.put("b", gameId);

			// Setup the wager
			xhtml = getSiteNoBr(populateUrl(sb.toString()));
			LOGGER.debug("XHTML: " + xhtml);
		}

		LOGGER.info("Exiting processWager()");
		return xhtml;
	}

	/**
	 * 
	 * @param xhtml
	 * @param eventPackage
	 * @param event
	 * @param ae
	 * @param wagerType
	 * @return
	 * @throws BatchException
	 */
	protected String confirmWager(String xhtml, SiteTransaction siteTransaction, EventPackage eventPackage, BaseRecordEvent event, AccountEvent ae, String wagerType) throws BatchException {
		LOGGER.info("Entering confirmWager()");

		// Process the wager
		if (processTransaction) {
			// https://playitez.com/wager/betslip/doBet.asp?p=BLUE&idlt=4
			final StringBuffer sb = new StringBuffer(200);
			sb.append(DO_BET);
			sb.append("?p=").append(globalHiddenVariables.get("hash"));
			sb.append("&idlt=").append(globalHiddenVariables.get("idlt"));
			sb.append("&pid=").append(globalHiddenVariables.get("pid"));

			// Setup the wager
			xhtml = getSiteNoBr(populateUrl(sb.toString()));
			LOGGER.debug("XHTML: " + xhtml);
		}

		LOGGER.info("Exiting confirmWager()");
		return xhtml;
	}

	/**
	 * 
	 * @param xhtml
	 * @param event
	 * @param ae
	 * @return
	 * @throws BatchException
	 */
	private String processLineChange(String xhtml, BaseRecordEvent event, AccountEvent ae) throws BatchException {
		LOGGER.info("Entering processLineChange()");

		final Map<String, String> lineChanges = NSP.processLineChange(xhtml);
		if (lineChanges != null && !lineChanges.isEmpty()) {
			if (event instanceof SpreadRecordEvent) {
				final SpreadRecordEvent sre = (SpreadRecordEvent)event;
				if (determineSpreadLineChange(sre, ae, lineChanges)) {
					//
					// https://playitez.com/wager/betslip/getstraight.asp?pid=6801&idc=209198423&b=4_1961452_0_175_0_3%2C&c=1&amt=1&fp=0
					//
					final StringBuffer sb = new StringBuffer(200);
					sb.append(GET_STRAIGHT);
					sb.append("?pid=").append(globalHiddenVariables.get("pid"));
					sb.append("&idc=").append(globalHiddenVariables.get("idc"));
					sb.append("&bets=").append(lineChanges.get("newurl"));
					sb.append("&p=").append(lineChanges.get("p"));
					sb.append("&wt=").append(lineChanges.get("wt"));
					sb.append("&idwt=").append(lineChanges.get("idwt"));
					sb.append("&idlt=").append(globalHiddenVariables.get("idlt"));

					// https://playitez.com/wager/betslip/doBet.asp?p=BLUE&idlt=4
//					final StringBuffer sb = new StringBuffer(200);
//					sb.append(DO_BET);
//					sb.append("?p=").append(this.httpClientWrapper.getPassword().toUpperCase());
//					sb.append("&idlt=").append(globalHiddenVariables.get("idlt"));
//					sb.append("&b=").append(globalHiddenVariables.get("newurl"));

					// Setup the wager
					xhtml = getSiteNoBr(populateUrl(sb.toString()));
					LOGGER.debug("XHTML: " + xhtml);
				} else {
					throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed", xhtml);
				}
			} else if (event instanceof TotalRecordEvent) {
				final TotalRecordEvent tre = (TotalRecordEvent)event;
				if (tre.getTotalinputfirstone() != null && tre.getTotalinputfirstone().length() > 0) {
					lineChanges.put("valueindicator", "o");					
				} else {
					lineChanges.put("valueindicator", "u");
				}
				if (determineTotalLineChange(tre, ae, lineChanges)) {
					final StringBuffer sb = new StringBuffer(200);
					sb.append(GET_STRAIGHT);
					sb.append("?pid=").append(globalHiddenVariables.get("pid"));
					sb.append("&idc=").append(globalHiddenVariables.get("idc"));
					sb.append("&bets=").append(lineChanges.get("newurl"));
					sb.append("&p=").append(lineChanges.get("p"));
					sb.append("&wt=").append(lineChanges.get("wt"));
					sb.append("&idwt=").append(lineChanges.get("idwt"));
					sb.append("&idlt=").append(globalHiddenVariables.get("idlt"));

					// https://playitez.com/wager/betslip/doBet.asp?p=BLUE&idlt=4
//					final StringBuffer sb = new StringBuffer(200);
//					sb.append(DO_BET);
//					sb.append("?p=").append(this.httpClientWrapper.getPassword().toUpperCase());
//					sb.append("&idlt=").append(globalHiddenVariables.get("idlt"));
//					sb.append("&b=").append(globalHiddenVariables.get("newurl"));

					// Setup the wager
					xhtml = getSiteNoBr(populateUrl(sb.toString()));
					LOGGER.debug("XHTML: " + xhtml);
				} else {
					throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed", xhtml);
				}
			} else if (event instanceof MlRecordEvent) {
				final MlRecordEvent mre = (MlRecordEvent)event;
				if (determineMlLineChange(mre, ae, lineChanges)) {
					// https://playitez.com/wager/betslip/doBet.asp?p=BLUE&idlt=4
					final StringBuffer sb = new StringBuffer(200);
					sb.append(GET_STRAIGHT);
					sb.append("?pid=").append(globalHiddenVariables.get("pid"));
					sb.append("&idc=").append(globalHiddenVariables.get("idc"));
					sb.append("&bets=").append(lineChanges.get("newurl"));
					sb.append("&p=").append(lineChanges.get("p"));
					sb.append("&wt=").append(lineChanges.get("wt"));
					sb.append("&idwt=").append(lineChanges.get("idwt"));
					sb.append("&idlt=").append(globalHiddenVariables.get("idlt"));

					// https://playitez.com/wager/betslip/doBet.asp?p=BLUE&idlt=4
//					final StringBuffer sb = new StringBuffer(200);
//					sb.append(DO_BET);
//					sb.append("?p=").append(this.httpClientWrapper.getPassword().toUpperCase());
//					sb.append("&idlt=").append(globalHiddenVariables.get("idlt"));
//					sb.append("&b=").append(globalHiddenVariables.get("newurl"));

					// Setup the wager
					xhtml = getSiteNoBr(populateUrl(sb.toString()));
					LOGGER.debug("XHTML: " + xhtml);
				} else {
					throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed", xhtml);
				}
			}
		}

		LOGGER.info("Exiting processLineChange()");
		return xhtml;
	}

	/**
	 * 
	 */
	private void copyGlobalVariables() {
		LOGGER.info("Entering copyGlobalVariables()");

		globalHiddenVariables = new HashMap<String, String>();
		Iterator<String> keys = MAP_DATA.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			String value = MAP_DATA.get(key);
			globalHiddenVariables.put(key, value);
		}

		LOGGER.info("Exiting copyGlobalVariables()");
	}
}