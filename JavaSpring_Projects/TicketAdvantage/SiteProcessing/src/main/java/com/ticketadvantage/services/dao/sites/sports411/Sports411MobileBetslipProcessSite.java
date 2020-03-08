/**
 * 
 */
package com.ticketadvantage.services.dao.sites.sports411;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.ticketadvantage.services.dao.sites.ProxyContainer;
import com.ticketadvantage.services.dao.sites.SiteEventPackage;
import com.ticketadvantage.services.dao.sites.SiteProcessor;
import com.ticketadvantage.services.dao.sites.SiteTeamPackage;
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
public class Sports411MobileBetslipProcessSite extends SiteProcessor {
	private static final Logger LOGGER = Logger.getLogger(Sports411MobileBetslipProcessSite.class);
	protected final Sports411MobileBetslipParser S411P = new Sports411MobileBetslipParser();

	/**
	 * 
	 */
	public Sports411MobileBetslipProcessSite(String host, String username, String password, boolean isMobile, boolean showRequestResponse) {
		super("Sports411MobileBetslip", host, username, password, isMobile, showRequestResponse);
		LOGGER.info("Entering Sports411MobileBetslipProcessSite()");

		// Setup the parser
		this.siteParser = S411P;

		// Menu items
		NFL_LINES_SPORT = new String[] { "FOOTBALL" };
		NFL_LINES_NAME = new String[] { "NFL" };
		NFL_FIRST_SPORT = new String[] { "FOOTBALL" };
		NFL_FIRST_NAME = new String[] { "NFL - 1ST HALVES" };
		NFL_SECOND_SPORT = new String[] { "FOOTBALL" };
		NFL_SECOND_NAME = new String[] { "NFL - 2ND HALVES" };
		NCAAF_LINES_SPORT = new String[] { "FOOTBALL" };
		NCAAF_LINES_NAME = new String[] { "COLLEGE FOOTBALL" };
		NCAAF_FIRST_SPORT = new String[] { "FOOTBALL" };
		NCAAF_FIRST_NAME = new String[] { "NCAA (F) - 1ST HALVES" };
		NCAAF_SECOND_SPORT = new String[] { "FOOTBALL" };
		NCAAF_SECOND_NAME = new String[] { "NCAA (F) - 2ND HALVES" };
		NBA_LINES_SPORT = new String[] { "BASKETBALL" };
		NBA_LINES_NAME = new String[] { "NBA" };
		NBA_FIRST_SPORT = new String[] { "BASKETBALL" };
		NBA_FIRST_NAME = new String[] { "NBA - 1ST HALVES" };
		NBA_SECOND_SPORT = new String[] { "BASKETBALL" };
		NBA_SECOND_NAME = new String[] { "NBA - 2ND HALVES" };
		NCAAB_LINES_SPORT = new String[] { "BASKETBALL" };
		NCAAB_LINES_NAME = new String[] { "COLLEGE BASKETBALL" };
		NCAAB_FIRST_SPORT = new String[] { "BASKETBALL" };
		NCAAB_FIRST_NAME = new String[] { "NCAA (B) - 1ST HALVES" };
		NCAAB_SECOND_SPORT = new String[] { "BASKETBALL" };
		NCAAB_SECOND_NAME = new String[] { "NCAA (B) - 2ND HALVES" };
		NHL_LINES_SPORT = new String[] { "HOCKEY" };
		NHL_LINES_NAME = new String[] { "NHL" };
		NHL_FIRST_SPORT = new String[] { "HOCKEY" };
		NHL_FIRST_NAME = new String[] { "NHL - 1ST HALVES", "NHL - 1ST PERIOD" };
		NHL_SECOND_SPORT = new String[] { "HOCKEY" };
		NHL_SECOND_NAME = new String[] { "NHL - 2ND HALVES", "NHL - 2ND PERIOD" };
		WNBA_LINES_SPORT = new String[] { "BASKETBALL" };
		WNBA_LINES_NAME = new String[] { "WNBA" };
		WNBA_FIRST_SPORT = new String[] { "BASKETBALL" };
		WNBA_FIRST_NAME = new String[] { "WNBA", "WNBA - 1ST HALVES" };
		WNBA_SECOND_SPORT = new String[] { "BASKETBALL" };
		WNBA_SECOND_NAME = new String[] { "WNBA", "WNBA - 2ND HALVES" };
		MLB_LINES_SPORT = new String[] { "BASEBALL" };
		MLB_LINES_NAME = new String[] { "MAJOR LEAGUE BASEBALL" };
		MLB_FIRST_SPORT = new String[] { "BASEBALL" };
		MLB_FIRST_NAME = new String[] { "MLB-1ST 5 FULL INNINGS" };
		MLB_SECOND_SPORT = new String[] { "BASEBALL" };
		MLB_SECOND_NAME = new String[] { "MLB - 2ND HALVES" };

		LOGGER.info("Exiting Sports411MobileBetslipProcessSite()");
	}

	/**
	 * 
	 * @param accountSoftware
	 * @param host
	 * @param username
	 * @param password
	 */
	public Sports411MobileBetslipProcessSite(String accountSoftware, String host, String username, String password) {
		super(accountSoftware, host, username, password);
		LOGGER.info("Entering Sports411MobileBetslipProcessSite()");
		LOGGER.info("Exiting Sports411MobileBetslipProcessSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final String[][] TDSITES = new String [][] {
//				{ "http://www.sports411.ag", "8660", "rq7", "0", "0", "0", "Los Angeles", "PT"}
//				{ "http://www.sports411.ag", "9461", "lenny", "0", "0", "0", "None", "PT"}
				{ "https://be.sports411.ag", "a3327", "spain2", "100", "100", "100", "None", "ET"}
			};

			final Sports411MobileBetslipProcessSite processSite = new Sports411MobileBetslipProcessSite(TDSITES[0][0], TDSITES[0][1],
					TDSITES[0][2], true, true);
		    processSite.httpClientWrapper.setupHttpClient(TDSITES[0][6]);
		    processSite.processTransaction = true;
		    processSite.timezone = TDSITES[0][7];

			String xhtml = processSite.loginToSite(processSite.httpClientWrapper.getUsername(),
					processSite.httpClientWrapper.getPassword());
			LOGGER.debug("xhtml: " + xhtml);

			final MlRecordEvent mre = new MlRecordEvent();
			mre.setMlplusminussecondone("-");
			mre.setMlinputsecondone("155");
			mre.setId(new Long(520));
			mre.setEventname("MLB #1932 ATHLETICS (OAK) -155 for 1H");
			mre.setEventtype("ml");
			mre.setSport("mlbfirst"); 
			mre.setUserid(new Long(1));
			mre.setEventid(new Integer(1931));
			mre.setEventid1(new Integer(1931));
			mre.setEventid2(new Integer(1932));
			mre.setRotationid(new Integer(1932));
			mre.setWtype("2");
			mre.setAmount("100");
			final Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
			cal.set(Calendar.HOUR_OF_DAY, 9);
			cal.set(Calendar.MINUTE, 35);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MONTH, 3);
			cal.set(Calendar.DAY_OF_MONTH, 11);
			cal.set(Calendar.YEAR, 2019);
			mre.setEventdatetime(cal.getTime());
			mre.setDatentime(cal.getTime());
			mre.setEventteam1("CARDINALS");
			mre.setEventteam2("ATHLETICS");
			mre.setGroupid(new Long(-99));
			mre.setIscompleted(false);
			mre.setScrappername("UI");
			mre.setActiontype("Standard");
			mre.setTextnumber("");
			mre.setDatecreated(new Date());
			mre.setDatemodified(new Date());
			mre.setCurrentattempts(0);

			final TotalRecordEvent tre = new TotalRecordEvent();
			tre.setTotalinputsecondone("41");
			tre.setTotalinputjuicesecondone("105");
			tre.setTotaljuiceplusminusfirstone("-");
			tre.setEventname("ML #955 KC Chiefs u46.5 -110.0 for Game");
			tre.setEventtype("total");
			tre.setSport("nfllines"); 
			tre.setUserid(new Long(6));
			tre.setEventid(new Integer(431));
			tre.setEventid1(new Integer(431));
			tre.setEventid2(new Integer(432));
			tre.setRotationid(432);
			tre.setWtype("2");
			tre.setAmount("100");
			tre.setEventdatetime(cal.getTime());
			tre.setDatentime(cal.getTime());
			tre.setEventteam1("Kansas City");
			tre.setEventteam2("Kansas City");
			tre.setGroupid(new Long(-99));
			tre.setIscompleted(false);
			tre.setScrappername("UI");
			tre.setActiontype("Standard");
			tre.setTextnumber("");
			tre.setDatecreated(new Date());
			tre.setDatemodified(new Date());
			tre.setCurrentattempts(0);

			final SpreadRecordEvent sre = new SpreadRecordEvent();
			sre.setSpreadplusminusfirstone("+");
			sre.setSpreadinputfirstone("1.5");
			sre.setSpreadjuiceplusminusfirstone("-");
			sre.setSpreadinputjuicefirstone("133");
			sre.setId(new Long(520));
			sre.setEventname("MLB #953 Washington Nationals +1½ (-133) for Game");
			sre.setEventtype("spread");
			sre.setSport("mlblines"); 
			sre.setUserid(new Long(1));
			sre.setEventid(new Integer(953));
			sre.setEventid1(new Integer(953));
			sre.setEventid2(new Integer(954));
			sre.setRotationid(new Integer(953));
			sre.setWtype("1");
			sre.setAmount("500");
			final Calendar scal = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
			scal.set(Calendar.HOUR_OF_DAY, 19);
			scal.set(Calendar.MINUTE, 10);
			scal.set(Calendar.SECOND, 0);
			scal.set(Calendar.MONTH, 3);
			scal.set(Calendar.DAY_OF_MONTH, 5);
			scal.set(Calendar.YEAR, 2019);
			sre.setEventdatetime(scal.getTime());
			sre.setDatentime(scal.getTime());
			sre.setEventteam1("");
			sre.setEventteam2("Washington Nationals");
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
			ae.setEventid(432);
			ae.setEventname("MLB #953 Washington Nationals +1½ (-133) for Game");
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
			ae.setMl(new Float(-155));
			ae.setMlid(new Long(1932));
			ae.setMljuice(new Float(-155));
			ae.setSpreadindicator("+");
			ae.setSpread(new Float(1.5));
			ae.setSpreadid(new Long(953));
			ae.setSpreadjuice(new Float(-133));
			ae.setTotal(new Float(41));
			ae.setTotalid(new Long(0));
			ae.setTotalindicator("u");
			ae.setTotaljuice(new Float(-105));
			ae.setType("total");
			ae.setUserid(new Long(1));
			ae.setWagertype("2");
			ae.setStatus("");
			ae.setIscomplexcaptcha(false);
			ae.setHumanspeed(false);
			
//			processSite.processMlTransaction(mre, ae);
			processSite.processTotalTransaction(tre, ae);
//			processSite.processSpreadTransaction(sre, ae);

/*
			// Loop through the sites
			for (int i = 0; i < TDSITES.length; i++) {
				final Sports411MobileBetslipProcessSite processSite = new Sports411MobileBetslipProcessSite(TDSITES[i][0], TDSITES[i][1],
						TDSITES[i][2], true, true);
				final Sports411MobileBetslipProcessSite processSite2 = new Sports411MobileBetslipProcessSite(TDSITES[i][0], TDSITES[i][1],
						TDSITES[i][2], true, true);
			    processSite.httpClientWrapper.setupHttpClient(TDSITES[0][6]);
			    processSite.processTransaction = false;
			    processSite.timezone = TDSITES[0][7];

			    processSite2.httpClientWrapper.setupHttpClient(TDSITES[0][6]);
			    processSite2.processTransaction = false;
			    processSite2.timezone = TDSITES[0][7];

				// Now call the test suite
				Sports411MobileProcessSite.testSite2(processSite, processSite2, i, TDSITES);
			}
*/
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
		S411P.setTimezone(timezone);

		// Call the second page
		String xhtml = super.getMobileSite(httpClientWrapper.getHost());
		LOGGER.debug("HTML: " + xhtml);

		// Get home page data
		MAP_DATA = S411P.parseIndex(xhtml);
		MAP_DATA.put("account", username);
		MAP_DATA.put("password", password);
		MAP_DATA.remove("Submit1");

		List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
		String actionLogin = setupNameValuesEmpty(postValuePairs, MAP_DATA, "");
		LOGGER.debug("ActionLogin: " + actionLogin);

		// Get the pending bets data
		final List<NameValuePair> headerValuePairs = new ArrayList<NameValuePair>(4);
		headerValuePairs.add(new BasicNameValuePair("Origin", this.httpClientWrapper.getHost()));
		headerValuePairs.add(new BasicNameValuePair("Referer", this.httpClientWrapper.getHost() + "/en/login?lk=-1410755172/"));
		headerValuePairs.add(new BasicNameValuePair("Authorization", "my-auth-token"));

		// https://be.sports411.ag/BetslipProxy.aspx/Login
		String jsonString = "{\"oDgs\":{\"BODgsRequestData\":{\"BODgsParameters\":{\"Player\":\"" + username + "\",\"Password\":\"" + password + "\",\"loginKey\":\"\"},\"BODgsPost\":{\"Bets\":{}},\"BOSignupRequest\":{}}}}";
		xhtml = super.postJSONSite(httpClientWrapper.getHost() + "/BetslipProxy.aspx/Login", jsonString, headerValuePairs);	
		
		// Call the login
//		xhtml = authenticateMobile(httpClientWrapper.getHost() + "/loginvalidate.aspx", postValuePairs);
		LOGGER.debug("HTML: " + xhtml);

		// WebappName
		httpClientWrapper.setWebappname("");

		// Parse login
		MAP_DATA = S411P.parseLogin2(xhtml, "login.aspx");
		LOGGER.debug("MAP_DATA: " + MAP_DATA);

		setUsernamePassword(username, password);
		postValuePairs = new ArrayList<NameValuePair>(1);
		actionLogin = setupNameValuesEmpty(postValuePairs, MAP_DATA, "");
		LOGGER.debug("ActionLogin: " + actionLogin);

		// https://be.sports411.ag/BetslipProxy.aspx/Login
		jsonString = "{\"oDgs\":{\"BODgsRequestData\":{\"BODgsParameters\":{\"LanguageId\":\"0\"},\"BODgsPost\":{\"Bets\":{}},\"BOSignupRequest\":{}}}}";
//		xhtml = super.postJSONSite(httpClientWrapper.getHost() + "/BetslipProxy.aspx/GetLeagues", "_ga=GA1.2.1356026128.1546808902; LanguagePreference=en-US; _ga=GA1.3.1356026128.1546808902; LPVID=VmZDY5YjRkNTE1NjJjMmVk;", jsonString, headerValuePairs);
		xhtml = super.postJSONSite(httpClientWrapper.getHost() + "/BetslipProxy.aspx/GetLeagues", null, jsonString, headerValuePairs);
		LOGGER.debug("HTML: " + xhtml);

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

		final EventPackage eventPackage = setupTransaction(event, ae); 
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
			// Step 11 - Sleep for 1 second
//			sleepAsUser(1000);

			//
			// Call the Game selecti on
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

			// Step 14 - Sleep for 1 second
//			sleepAsUser(1000);

			//
			// Call the Complete Wager
			//
			// Step 15 - Complete transaction
			xhtml = completeTransaction(siteTransaction, eventPackage, event, ae);

			// Step 16 - Parse the ticket #
			String ticketNumber = parseTicketTransaction(xhtml);

			// Set the account data
			ae.setAccountconfirmation(ticketNumber);
			ae.setAccounthtml(xhtml);
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

		// Get the pending bets data
		final List<NameValuePair> headerValuePairs = new ArrayList<NameValuePair>(4);
		headerValuePairs.add(new BasicNameValuePair("Origin", this.httpClientWrapper.getHost()));
		headerValuePairs.add(new BasicNameValuePair("Referer", this.httpClientWrapper.getHost() + "/en/sports"));
		headerValuePairs.add(new BasicNameValuePair("authorization", "my-auth-token"));

		// https://be.sports411.ag/BetslipProxy.aspx/GetSchedule
		String jsonString = "{\"oDgs\":{\"BODgsRequestData\":{\"BODgsParameters\":{\"LeaguesIdList\":\"" + MAP_DATA.get("id") + "\",\"LanguageId\":\"0\",\"LineStyle\":\"E\",\"ScheduleType\":\"american\"},\"BODgsPost\":{\"Bets\":{}},\"BOSignupRequest\":{}}}}";
		String xhtml = super.postJSONSite(httpClientWrapper.getHost() + "/BetslipProxy.aspx/GetSchedule", jsonString, headerValuePairs);

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

		final Sports411Transaction tdSportsTransaction = new Sports411Transaction();

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
		Sports411TeamPackage teamPackage = null;
		final Sports411EventPackage sports411EventPackage = (Sports411EventPackage)eventPackage;
		String wagerType = "spread";
		String displayLine = "";
		String betId = "";
		String odds = "";
		String play = "";

		// Only get the first one because that's all there is for TDSports at this point
		final List<NameValuePair> postNameValuePairs = new ArrayList<NameValuePair>(1);

		// Get the appropriate record event
		if (event instanceof SpreadRecordEvent) {
			teamPackage = (Sports411TeamPackage)SiteWagers.setupTeam(SiteWagers.determineSpreadData((SpreadRecordEvent)event), eventPackage);
			displayLine = teamPackage.getGameSpreadOptionIndicator().get("0") + teamPackage.getGameSpreadOptionNumber().get("0") + "_" + teamPackage.getGameSpreadOptionJuiceIndicator().get("0") + teamPackage.getGameSpreadOptionJuiceNumber().get("0");
			odds = teamPackage.getGameSpreadOptionJuiceIndicator().get("0") + teamPackage.getGameSpreadOptionJuiceNumber().get("0");
			final Integer rotId = teamPackage.getId();

			if (rotId.intValue() % 2 == 0) {
				play = "1";
				betId = sports411EventPackage.getBetId() + "1";
			} else {
				play = "0";
				betId = sports411EventPackage.getBetId() + "0";
			}

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
			wagerType = "total"; 
			teamPackage = (Sports411TeamPackage)SiteWagers.setupTeam(SiteWagers.determineTotalData((TotalRecordEvent)event), eventPackage);
			displayLine = teamPackage.getGameTotalOptionNumber().get("0") + "_" + teamPackage.getGameTotalOptionJuiceIndicator().get("0") + teamPackage.getGameTotalOptionJuiceNumber().get("0");
			odds = teamPackage.getGameTotalOptionJuiceIndicator().get("0") + teamPackage.getGameTotalOptionJuiceNumber().get("0");
			final Integer rotId = teamPackage.getId();

			if (rotId.intValue() % 2 == 0) {
				play = "3";
				betId = sports411EventPackage.getBetId() + "3";
			} else {
				play = "2";
				betId = sports411EventPackage.getBetId() + "2";
			}

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
			wagerType = "odds";
			teamPackage = (Sports411TeamPackage)SiteWagers.setupTeam(SiteWagers.determineMoneyLineData((MlRecordEvent)event), eventPackage);
			displayLine = teamPackage.getGameMLOptionJuiceIndicator().get("0") + teamPackage.getGameMLOptionJuiceNumber().get("0");
			odds = teamPackage.getGameMLOptionJuiceIndicator().get("0") + teamPackage.getGameMLOptionJuiceNumber().get("0");
			final Integer rotId = teamPackage.getId();

			if (rotId.intValue() % 2 == 0) {
				play = "5";
				betId = sports411EventPackage.getBetId() + "5";
			} else {
				play = "4";
				betId = sports411EventPackage.getBetId() + "4";
			}

			if (teamPackage != null && 
				teamPackage.getGameMLInputName() != null &&
				teamPackage.getGameMLInputName().length() > 0 &&
				teamPackage.getGameMLInputValue() != null) {
				postNameValuePairs.add(new BasicNameValuePair(teamPackage.getGameMLInputName(), teamPackage.getGameMLInputValue().get("0")));
			} else {
				throw new BatchException(BatchErrorCodes.XHTML_GAMES_PARSING_EXCEPTION, BatchErrorMessage.XHTML_GAMES_PARSING_EXCEPTION, "No moneyline available for this game");
			}
		}

		//
		// https://be.sports411.ag/BetslipProxy.aspx/UseFreePlays
		//
		// Setup the wager
		//
		//{"oDgs":{"BODgsRequestData":{"BODgsParameters":{"WagerType":"1"},"BODgsPost":{"Bets":{"Bet":[{"guid"
		//:"d2addcd1-0a45-4e76-edae-cad377534abe","post":false,"betId":"4628298_5_4","type":"scheduleLine","gameTypeID"
		//:"82","idGame":4628298,"idlg":"5","parentIdg":4622875,"idCombinationGame":4628298,"isGameLine":true,"odds"
		//:"108","newOdds":"","oddsDisplay":"+108","selectedPitcher":0,"pitchers":"J Lucchesi - L_J Teheran - R"
		//,"play":"4","points":0,"newPoints":0,"pointsTeaser":"0","pointsDisplay":"","processed":false,"postBetAmountType"
		//:"","risk":0,"riskPost":0,"win":0,"winPost":0,"sport":"MLB","success":false,"teamName":"Atlanta Braves"
		//,"useFreePlays":false,"useFreePlaysPost":false,"valid":true,"wagerType":"odds","allowhookups":"true"
		//,"derivateIndex":0,"displayLine":"+108","oldDisplayLine":"+108","isLineChange":false,"isLineChangeInMyFavor"
		//:false,"acceptedWithLineChange":false,"errorMessage":"","errorCombinationMsgCode":0,"errorCombinationMsg"
		//:"","isGameOpen":true,"teamNumber":0,"allowBuyPoints":true,"Derivatives":null,"DerivDesc":"","allowInPost"
		//:true,"betReplaced":false,"gameType":"game","details":"","lineType":"odds","lineStyle":"E","categoryDesc"
		//:"BASEBALL","leagueDesc":"MAJOR LEAGUE BASEBALL"}]}},"BOSignupRequest":{}}}}

		// {"d":"true"}

		// Get the pending bets data
		final List<NameValuePair> headerValuePairs = new ArrayList<NameValuePair>(3);
		headerValuePairs.add(new BasicNameValuePair("Origin", this.httpClientWrapper.getHost()));
		headerValuePairs.add(new BasicNameValuePair("Referer", this.httpClientWrapper.getHost() + "/en/login?lk=-1410755172/"));
		headerValuePairs.add(new BasicNameValuePair("Authorization", "my-auth-token"));

		UUID uuid = UUID.randomUUID();
		String guid = uuid.toString();

		String gameTypeID = teamPackage.getGameTypeID();
		String idGame = teamPackage.getIdGame();
		String idlg = teamPackage.getIdlg();
		String parentIdg = teamPackage.getParentIdg();
		String idCombinationGame = teamPackage.getIdCombinationGame();
		String pitcher = teamPackage.getPitcher();
		
		String points = "0";
		String pointsTeaser = "0";
		String sport = teamPackage.getIdspt();
		String teamName = teamPackage.getTeam();

		Boolean isGameLine = new Boolean(true);
		String gameType = "";
		if (event.getSport().contains("lines")) {
			gameType = "game";
		} else if (event.getSport().contains("first")) {
			gameType = "first half";
			isGameLine = false;
		} else if (event.getSport().contains("second")) {
			gameType = "second half";
			isGameLine = false;
		}

		String details = "";
		String lineStyle = "E";
		String categoryDesc = sports411EventPackage.getCategoryDesc();
		String leagueDesc = sports411EventPackage.getCategoryDesc();

		// https://be.sports411.ag/BetslipProxy.aspx/UseFreePlays
		String jsonString = "{\"oDgs\":{\"BODgsRequestData\":{\"BODgsParameters\":{\"WagerType\":\"1\"},\"BODgsPost\":{\"Bets\":{\"Bet\":[{\"guid\"" + 
				":\"" + guid + "\",\"post\":false,\"betId\":\"" + betId + "\",\"type\":\"scheduleLine\",\"gameTypeID\"" + 
				":\"" + gameTypeID + "\",\"idGame\":" + idGame + ",\"idlg\":\"" + idlg + "\",\"parentIdg\":" + parentIdg + ",\"idCombinationGame\":" + idCombinationGame + ",\"isGameLine\":" + isGameLine.toString() + 
				",\"odds\":\"" + odds + "\",\"newOdds\":\"\",\"oddsDisplay\":\"" + odds + "\",\"selectedPitcher\":3,\"pitchers\":\"" + pitcher +  
				"\",\"play\":\"" + play + "\",\"points\":" + points + ",\"newPoints\":0,\"pointsTeaser\":\"" + pointsTeaser + "\",\"pointsDisplay\":\"" + pointsTeaser + "\",\"processed\":false" + 
				",\"postBetAmountType\":\"\",\"risk\":0,\"riskPost\":0,\"win\":0,\"winPost\":0,\"sport\":\"" + sport + "\",\"success\":false,\"teamName\"" + 
				":\"" + teamName + "\",\"useFreePlays\":false,\"useFreePlaysPost\":false,\"valid\":true,\"wagerType\"" + 
				":\"" + wagerType + "\",\"allowhookups\":\"true\",\"derivateIndex\":0,\"displayLine\":\"" + displayLine + "\",\"oldDisplayLine\":\"" + displayLine + "\",\"isLineChange\"" + 
				":false,\"isLineChangeInMyFavor\":false,\"acceptedWithLineChange\":false,\"errorMessage\":\"\",\"errorCombinationMsgCode\"" + 
				":0,\"errorCombinationMsg\":\"\",\"isGameOpen\":true,\"teamNumber\":0,\"allowBuyPoints\":true,\"Derivatives\":null" + 
				",\"DerivDesc\":\"\",\"allowInPost\":true,\"betReplaced\":false,\"gameType\":\"" + gameType + "\",\"details\":\"" + details + "\"" + 
				",\"lineType\":\"" + wagerType + "\",\"lineStyle\":\"" + lineStyle + "\",\"categoryDesc\":\"" + categoryDesc + "\",\"leagueDesc\":\"" + leagueDesc + "\"}]" + 
				"}},\"BOSignupRequest\":{}}}}";
		String xhtml = super.postJSONSite(httpClientWrapper.getHost() + "/BetslipProxy.aspx/UseFreePlays", jsonString, headerValuePairs);	
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
		Sports411TeamPackage teamPackage = null;

		MAP_DATA.clear();
		gamesXhtml = xhtml;
		if (event instanceof SpreadRecordEvent) {
			teamPackage = (Sports411TeamPackage)SiteWagers.setupTeam(SiteWagers.determineSpreadData((SpreadRecordEvent)event), eventPackage);
			MAP_DATA = S411P.parseEventSelection(xhtml, teamPackage, "spread");
		} else if (event instanceof TotalRecordEvent) {
			teamPackage = (Sports411TeamPackage)SiteWagers.setupTeam(SiteWagers.determineTotalData((TotalRecordEvent)event), eventPackage);
			MAP_DATA = S411P.parseEventSelection(xhtml, teamPackage, "total");
		} else if (event instanceof MlRecordEvent) {
			teamPackage = (Sports411TeamPackage)SiteWagers.setupTeam(SiteWagers.determineMoneyLineData((MlRecordEvent)event), eventPackage);
			MAP_DATA = S411P.parseEventSelection(xhtml, teamPackage, "ml");
		}
		LOGGER.debug("MAP_DATA: " + MAP_DATA);

		// Check if we have exceeded
		if (MAP_DATA.containsKey("wagerbalanceexceeded")) {
			throw new BatchException(BatchErrorCodes.ACCOUNT_BALANCE_LIMIT, BatchErrorMessage.ACCOUNT_BALANCE_LIMIT, "The maximum account balance has been reached for wager " + siteTransaction.getAmount(), xhtml);
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
				Sports411TeamPackage teamPackage = null;
				final Sports411EventPackage sports411EventPackage = (Sports411EventPackage)eventPackage;
				String wagerType = "spread";
				String displayLine = "";
				String betId = "";
				String odds = "";
				String play = "";
				String postBetAmountType = "";
				String points = "0";
				String pointsTeaser = "0";
				int eventtype = 1;

				// Only get the first one because that's all there is for TDSports at this point
				final List<NameValuePair> postNameValuePairs = new ArrayList<NameValuePair>(1);

				// Get the appropriate record event
				if (event instanceof SpreadRecordEvent) {
					eventtype = 1;
					teamPackage = (Sports411TeamPackage)SiteWagers.setupTeam(SiteWagers.determineSpreadData((SpreadRecordEvent)event), eventPackage);
					LOGGER.error("teamPackage: " + teamPackage);
					displayLine = teamPackage.getGameSpreadOptionIndicator().get("0") + teamPackage.getGameSpreadOptionNumber().get("0") + "_" + teamPackage.getGameSpreadOptionJuiceIndicator().get("0") + teamPackage.getGameSpreadOptionJuiceNumber().get("0");
					odds = teamPackage.getGameSpreadOptionJuiceIndicator().get("0") + teamPackage.getGameSpreadOptionJuiceNumber().get("0");
					final Integer rotId = teamPackage.getId();
					final String spreadIndicator = teamPackage.getGameSpreadOptionIndicator().get("0");

					if (spreadIndicator.equals("-")) {
						points = pointsTeaser = teamPackage.getGameSpreadOptionIndicator().get("0") + teamPackage.getGameSpreadOptionNumber().get("0");
					} else {
						points = pointsTeaser = teamPackage.getGameSpreadOptionNumber().get("0");
					}

					if (rotId.intValue() % 2 == 0) {
						play = "1";
						betId = sports411EventPackage.getBetId() + "1";
					} else {
						play = "0";
						betId = sports411EventPackage.getBetId() + "0";
					}

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
					eventtype = 2;
					wagerType = "total"; 
					teamPackage = (Sports411TeamPackage)SiteWagers.setupTeam(SiteWagers.determineTotalData((TotalRecordEvent)event), eventPackage);
					displayLine = teamPackage.getGameTotalOptionNumber().get("0") + "_" + teamPackage.getGameTotalOptionJuiceIndicator().get("0") + teamPackage.getGameTotalOptionJuiceNumber().get("0");
					odds = teamPackage.getGameTotalOptionJuiceIndicator().get("0") + teamPackage.getGameTotalOptionJuiceNumber().get("0");
					final Integer rotId = teamPackage.getId();

					if (rotId.intValue() % 2 == 0) {
						play = "3";
						betId = sports411EventPackage.getBetId() + "3";
					} else {
						play = "2";
						betId = sports411EventPackage.getBetId() + "2";
					}
					points = pointsTeaser = teamPackage.getGameTotalOptionNumber().get("0");

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
					eventtype = 3;
					wagerType = "odds";
					teamPackage = (Sports411TeamPackage)SiteWagers.setupTeam(SiteWagers.determineMoneyLineData((MlRecordEvent)event), eventPackage);
					displayLine = teamPackage.getGameMLOptionJuiceIndicator().get("0") + teamPackage.getGameMLOptionJuiceNumber().get("0");
					odds = teamPackage.getGameMLOptionJuiceIndicator().get("0") + teamPackage.getGameMLOptionJuiceNumber().get("0");
					final Integer rotId = teamPackage.getId();

					if (rotId.intValue() % 2 == 0) {
						play = "5";
						betId = sports411EventPackage.getBetId() + "5";
					} else {
						play = "4";
						betId = sports411EventPackage.getBetId() + "4";
					}

					if (teamPackage != null && 
						teamPackage.getGameMLInputName() != null &&
						teamPackage.getGameMLInputName().length() > 0 &&
						teamPackage.getGameMLInputValue() != null) {
						postNameValuePairs.add(new BasicNameValuePair(teamPackage.getGameMLInputName(), teamPackage.getGameMLInputValue().get("0")));
					} else {
						throw new BatchException(BatchErrorCodes.XHTML_GAMES_PARSING_EXCEPTION, BatchErrorMessage.XHTML_GAMES_PARSING_EXCEPTION, "No moneyline available for this game");
					}
				}

				//
				// https://be.sports411.ag/BetslipProxy.aspx/SendBets
				//
				// Setup the wager
				//
				// {"oDgs":{"BODgsRequestData":{"BODgsParameters":{"PrmWagerType":"1"},"BODgsPost":{"Bets":{"openSpotPostTicket"
				//	:0,"openSpotTotalRisk":0,"postBetTotalRisk":100,"postBetTotalFreeplay":0,"postBetTotalWin":51.55,"postBetStatus"
				//	:false,"processed":false,"lineChangesSetting":0,"Bet":[{"guid":"394edabb-2aee-59f1-21b0-f2010916121d"
				//	,"post":false,"betId":"4634177_5_4","type":"scheduleLine","gameTypeID":"82","idGame":4634177,"idlg":"5"
				//	,"parentIdg":4634262,"idCombinationGame":4634177,"isGameLine":true,"odds":"-194","newOdds":"","oddsDisplay"
				//	:"-194","selectedPitcher":0,"pitchers":"V Velasquez - R_W Buehler - R","play":"4","points":0,"newPoints"
				//	:0,"pointsTeaser":"0","pointsDisplay":"","processed":false,"postBetAmountType":"R","risk":100,"riskPost"
				//	:0,"win":51.55,"winPost":0,"sport":"MLB","success":false,"teamName":"Los Angeles Dodgers","useFreePlays"
				//	:true,"useFreePlaysPost":false,"valid":true,"wagerType":"odds","allowhookups":"true","derivateIndex"
				//	:0,"displayLine":"-194","oldDisplayLine":"-194","isLineChange":false,"isLineChangeInMyFavor":false,"acceptedWithLineChange"
				//	:false,"errorMessage":"","errorCombinationMsgCode":0,"errorCombinationMsg":"","isGameOpen":true,"teamNumber"
				//	:0,"allowBuyPoints":true,"Derivatives":null,"DerivDesc":"","allowInPost":true,"betReplaced":false,"gameType"
				//	:"game","details":"","lineType":"odds","lineStyle":"E","categoryDesc":"BASEBALL","leagueDesc":"MAJOR
				//	 LEAGUE BASEBALL"}],"valid":0,"asyncPost":null}},"BOSignupRequest":{}}}}
				//
				// {"openSpotPostTicket":"0","openSpotTotalRisk":"0","postBetStatus":true,"postBetTotalWin":51.5500,"postBetTotalRisk":100.0000,"processed":true,"lineChangesSetting":0,"Bet":[{"guid":"394edabb-2aee-59f1-21b0-f2010916121d","post":false,"betId":"4634177_5_4","gameTypeID":"82","idGame":4634177,"idlg":"5","odds":"-194","pitchers":"V Velasquez - R_W Buehler - R","displayLine":"-194","oldDisplayLine":"-194","play":"4","points":0.0,"pointsTeaser":"0","processed":true,"risk":100.0,"riskPost":0.0,"sport":"MLB","success":true,"teamName":"Los Angeles Dodgers","useFreePlays":true,"useFreePlaysPost":false,"derivateIndex":0,"valid":true,"wagerType":"odds","win":51.55,"postWin":0.0,"allowBuyPoints":true,"allowhookups":"true","postBetAmountType":"R","selectedPitcher":0,"isLineChange":false,"errorMessage":"","isGameOpen":true,"isGameLine":true,"allowInPost":true,"teamNumber":0,"details":"","idCombinationGame":4634177,"lineStyle":"E","categoryDesc":"BASEBALL","leagueDesc":"MAJOR LEAGUE BASEBALL"}],"valid":"1","activeAsyncPost":false}
				//

				// Get the pending bets data
				final List<NameValuePair> headerValuePairs = new ArrayList<NameValuePair>(3);
				headerValuePairs.add(new BasicNameValuePair("Origin", this.httpClientWrapper.getHost()));
				headerValuePairs.add(new BasicNameValuePair("Referer", this.httpClientWrapper.getHost() + "/en/login?lk=-1410755172/"));
				headerValuePairs.add(new BasicNameValuePair("Authorization", "my-auth-token"));

				UUID uuid = UUID.randomUUID();
				String guid = uuid.toString();

				String gameTypeID = teamPackage.getGameTypeID();
				String idGame = teamPackage.getIdGame();
				String idlg = teamPackage.getIdlg();
				String parentIdg = teamPackage.getParentIdg();
				String idCombinationGame = teamPackage.getIdCombinationGame();
				String sport = teamPackage.getIdspt();
				String pitcher = "";
				String selectedPitcher = "3";
				if (sport.toLowerCase().contains("NFL")) {
					pitcher = "_";
					selectedPitcher = "0";
				} else {
					pitcher = teamPackage.getPitcher();
				}
				String teamName = teamPackage.getTeam();

				Boolean isGameLine = new Boolean(true);
				String gameType = "";
				String details = "";
				if (event.getSport().contains("lines")) {
					gameType = "game";
					details = sports411EventPackage.getGpd();
				} else if (event.getSport().contains("first")) {
					gameType = "first half";
					details = sports411EventPackage.getGpd();
					isGameLine = false;
				} else if (event.getSport().contains("second")) {
					gameType = "second half";
					details = sports411EventPackage.getGpd();
					isGameLine = false;
				}

				String lineStyle = "E";
				String categoryDesc = sports411EventPackage.getCategoryDesc();
				String leagueDesc = sports411EventPackage.getLeagueDesc();
				String risk = "";
				String win = "";

				SiteTransaction tempSiteTransaction = null;
				SiteTeamPackage theteam = null;
				final SiteEventPackage siteEventPackage = (SiteEventPackage)eventPackage;
				final SiteTeamPackage stp1 = siteEventPackage.getSiteteamone();
				final SiteTeamPackage stp2 = siteEventPackage.getSiteteamtwo();
				String amount = siteTransaction.getAmount();

				if (event.getRotationid().toString().contains(stp1.getId().toString())) {
					theteam = stp1;
				} else {
					theteam = stp2;
				}
				LOGGER.error("theteam: " + theteam);

				// Get the spread transaction
				if (event instanceof SpreadRecordEvent) {
					tempSiteTransaction = getSpreadTransaction((SpreadRecordEvent)event, eventPackage, ae);

					float[] sitejuices = getSiteSpreadJuice(theteam);
					float spreadJuice = getJuice(ae.getSpreadjuice(), sitejuices);
					LOGGER.error("spreadJuice: " + spreadJuice);

					if (spreadJuice > 0) {
						final Integer spreadMax = teamPackage.getSpreadMax();
						final Double sAmount = Double.valueOf(amount);

						if (spreadMax != null && sAmount > spreadMax) {
							amount = Integer.toString(spreadMax);
						}

						final Float fl = new Float(amount);
						float factor = Math.abs(spreadJuice) / 100;
						win = Float.toString(fl * factor);
						risk = amount;
						wagerType = "1";
						postBetAmountType = "R";
						LOGGER.error("fl2: " + fl);
						LOGGER.error("factor2: " + factor);
						LOGGER.error("winS: " + win);
						LOGGER.error("riskS: " + risk);
					} else {
						final Integer spreadMax = teamPackage.getSpreadMax();
						final Double sAmount = Double.valueOf(amount);

						if (spreadMax != null && sAmount > spreadMax) {
							amount = Integer.toString(spreadMax);
						}

						final Float fl = new Float(amount);
						float factor = 100 / Math.abs(spreadJuice);
						postBetAmountType = "W";
						LOGGER.error("fl2: " + fl);
						LOGGER.error("factor2: " + factor);
						risk = Float.toString(fl / factor);
						win = amount;
						LOGGER.error("win2: " + win);
						LOGGER.error("risk2: " + risk);
					}
				} else if (event instanceof TotalRecordEvent) {
					tempSiteTransaction = getTotalTransaction((TotalRecordEvent)event, eventPackage, ae);

					float[] sitejuices = getSiteTotalJuice(theteam);
					float totalJuice = getJuice(ae.getTotaljuice(), sitejuices);

					if (totalJuice > 0) {
						final Integer totalMax = teamPackage.getTotalMax();
						final Double sAmount = Double.valueOf(amount);

						if (totalMax != null && sAmount > totalMax) {
							amount = Integer.toString(totalMax);
						}

						final Float fl = new Float(amount);
						float factor = Math.abs(totalJuice) / 100;
						win = Float.toString(fl * factor);
						risk = amount;
						wagerType = "1";
						postBetAmountType = "R";
					} else {
						final Integer totalMax = teamPackage.getTotalMax();
						final Double sAmount = Double.valueOf(amount);

						if (totalMax != null && sAmount > totalMax) {
							amount = Integer.toString(totalMax);
						}

						final Float fl = new Float(amount);
						float factor = 100 / Math.abs(totalJuice);
						risk = Float.toString(fl / factor);
						win = amount;
						postBetAmountType = "W";
					}
				} else if (event instanceof MlRecordEvent) {
					tempSiteTransaction = getMlTransaction((MlRecordEvent)event, eventPackage, ae);
					LOGGER.error("tempSiteTransaction: " + tempSiteTransaction);

					float[] sitejuices = getSiteMlJuice(theteam);
					LOGGER.error("ae.getMljuice(): " + ae.getMljuice());
					float mlJuice = getJuice(ae.getMljuice(), sitejuices);
					LOGGER.error("mlJuice: " + mlJuice);

					if (mlJuice > 0) {
						final Integer mlMax = teamPackage.getMlMax();
						final Double sAmount = Double.valueOf(amount);

						if (mlMax != null && sAmount > mlMax) {
							amount = Integer.toString(mlMax);
						}

						final Float fl = new Float(amount);
						float factor = Math.abs(mlJuice) / 100;
						win = Float.toString(fl * factor);
						risk = amount;
						wagerType = "1";
						LOGGER.error("win: " + win);
						LOGGER.error("risk: " + risk);
						postBetAmountType = "R";
					} else {
						final Integer mlMax = teamPackage.getMlMax();
						final Double sAmount = Double.valueOf(amount);

						if (mlMax != null && sAmount > mlMax) {
							amount = Integer.toString(mlMax);
						}

						final Float fl = new Float(amount);
						float factor = 100 / Math.abs(mlJuice);
						risk = Float.toString(fl / factor);
						win = amount;
						LOGGER.error("fl2: " + fl);
						LOGGER.error("factor2: " + factor);
						LOGGER.error("win2: " + win);
						LOGGER.error("risk2: " + risk);
						postBetAmountType = "W";
					}
				}

				String derivativesJson = "null";
				String DerivDesc = "";

				final JSONArray derivatives = sports411EventPackage.getDerivatives();
				if (derivatives != null && !derivatives.isNull(0)) {
					// "Derivatives":[{"Index":-4,"OddsAmerican"
					//	:109,"OddsDisplay":"+109","Points":-39,"PointsDisplay":"-39","BetId":"4707238_1_3_-39_109","Id":"d1_4707238_1_3_-39_109"
					//	,"Desc":"39 +109","Sel":false,"Show":true},
					derivativesJson = "[";
					int counter = 0;

					for (int x = 0; x < derivatives.length(); x++) {
					// BetId "4707238_1_3_-39_109"
					// Desc "39 +109"
					// Id "d1_4707238_1_3_-39_109"
					// Index -4
					// OddsAmerican 109
					// OddsDisplay "+109"
					// Points -39
					// PointsDisplay "-39"
					// Sel false
					// Show true
						final JSONObject d = derivatives.getJSONObject(x);
						String dd = "";
						String dIndex = d.getString("index");
						String dDesc = "";

						if (eventtype == 1) {
							// ovoddst
							// ovt
							// unoddst : "109"
							// unt
							if (event.getRotationid().toString().contains(stp1.getId().toString())) {
								String vsprdoddst = d.getString("vsprdoddst");
								String vsprdt = d.getString("vsprdt");
								String vsproddsnrh = d.getString("vsproddsnrh");
								String dbId = betId + "_" + vsprdt + "_" + vsprdoddst;
								dDesc = vsproddsnrh;
								String dId = "d1" + "_" + dbId;
								String dOddsAmerican = vsprdoddst;
								String dOddsDisplay = vsprdoddst;
								String dPoints = vsprdt;
								String dPointsDisplay = vsprdt;
								String dSel = "false";
								String dShow = "true";
								dd = "{\"Index\":" + dIndex + 
										",\"OddsAmerican\":" + dOddsAmerican + 
										",\"OddsDisplay\":\"" + dOddsDisplay + 
										"\",\"Points\":" + dPoints + 
										",\"PointsDisplay\":\"" + dPointsDisplay + 
										"\",\"BetId\":\"" + dbId + 
										"\",\"Id\":\"" + dId + "\"" +
										",\"Desc\":\"" + dDesc + 
										"\",\"Sel\":" + dSel + 
										",\"Show\":" + dShow + "}";
							} else {
								String hsprdoddst = d.getString("hsprdoddst");
								String hsprdt = d.getString("hsprdt");
								String hsproddsnrh = d.getString("hsproddsnrh");
								String dbId = betId + "_" + hsprdt + "_" + hsprdoddst;
								dDesc = hsproddsnrh;
								String dId = "d1" + "_" + dbId;
								String dOddsAmerican = hsprdoddst;
								String dOddsDisplay = hsprdoddst;
								String dPoints = hsprdt;
								String dPointsDisplay = hsprdt;
								String dSel = "false";
								String dShow = "true";
								dd = "{\"Index\":" + dIndex + 
										",\"OddsAmerican\":" + dOddsAmerican + 
										",\"OddsDisplay\":\"" + dOddsDisplay + 
										"\",\"Points\":" + dPoints + 
										",\"PointsDisplay\":\"" + dPointsDisplay + 
										"\",\"BetId\":\"" + dbId + 
										"\",\"Id\":\"" + dId + "\"" +
										",\"Desc\":\"" + dDesc + 
										"\",\"Sel\":" + dSel + 
										",\"Show\":" + dShow + "}";
							}

							if (counter++ == 0) {
								derivativesJson = derivativesJson + dd;
							} else {
								derivativesJson = derivativesJson + "," + dd;
							}

							if (dIndex.equals("0")) {
								DerivDesc = dDesc;
							}
						} else if (eventtype == 2) {
							// ovoddst
							// ovt
							// unoddst : "109"
							// unt
							if (event.getRotationid().toString().contains(stp1.getId().toString())) {
								String ovoddst = d.getString("ovoddst");
								String ovt = d.getString("ovt");
								String ovoddsnrh = d.getString("ovoddsnrh");
								String dbId = betId + "_" + ovt + "_" + ovoddst;
								dDesc = ovoddsnrh.replace("o","");
								String dId = "d1" + "_" + dbId;
								String dOddsAmerican = ovoddst;
								String dOddsDisplay = ovoddst;
								String dPoints = ovt;
								String dPointsDisplay = ovt;
								String dSel = "false";
								String dShow = "true";
								dd = "{\"Index\":" + dIndex + 
										",\"OddsAmerican\":" + dOddsAmerican + 
										",\"OddsDisplay\":\"" + dOddsDisplay + 
										"\",\"Points\":" + dPoints + 
										",\"PointsDisplay\":\"" + dPointsDisplay + 
										"\",\"BetId\":\"" + dbId + 
										"\",\"Id\":\"" + dId + "\"" +
										",\"Desc\":\"" + dDesc + 
										"\",\"Sel\":" + dSel + 
										",\"Show\":" + dShow + "}";
							} else {
								String unoddst = d.getString("unoddst");
								String unt = d.getString("unt");
								String unoddsnrh = d.getString("unoddsnrh");
								String dbId = betId + "_" + unt + "_" + unoddst;
								dDesc = unoddsnrh.replace("u","");
								String dId = "d1" + "_" + dbId;
								String dOddsAmerican = unoddst;
								String dOddsDisplay = unoddst;
								String dPoints = unt;
								String dPointsDisplay = unt;
								String dSel = "false";
								String dShow = "true";
								dd = "{\"Index\":" + dIndex + 
										",\"OddsAmerican\":" + dOddsAmerican + 
										",\"OddsDisplay\":\"" + dOddsDisplay + 
										"\",\"Points\":" + dPoints + 
										",\"PointsDisplay\":\"" + dPointsDisplay + 
										"\",\"BetId\":\"" + dbId + 
										"\",\"Id\":\"" + dId + "\"" +
										",\"Desc\":\"" + dDesc + 
										"\",\"Sel\":" + dSel + 
										",\"Show\":" + dShow + "}";
							}

							if (counter++ == 0) {
								derivativesJson = derivativesJson + dd;
							} else {
								derivativesJson = derivativesJson + "," + dd;
							}

							if (dIndex.equals("0")) {
								DerivDesc = dDesc;
							}
						} else {
							// Fix later
						}
					}

					derivativesJson += "]";
				}

				// Reset the $$ if we have to based on risk vs win
				String siteAmount = determineRiskWinAmounts(siteTransaction, eventPackage, event, ae);

				LOGGER.error("siteAmount: " + siteAmount);
				siteTransaction.setAmount(siteAmount);
				ae.setActualamount(siteAmount);
				
				LOGGER.error("postBetAmountType: " + postBetAmountType);
				LOGGER.error("RiskXXX: " + risk);
				LOGGER.error("WinXXX: " + win);

				// https://be.sports411.ag/BetslipProxy.aspx/SendBets
				String jsonString = "{\"oDgs\":{\"BODgsRequestData\":{\"BODgsParameters\":{\"PrmWagerType\":\"1\"},\"BODgsPost\":{\"Bets\":{\"openSpotPostTicket\":0,\"openSpotTotalRisk\":0,\"postBetTotalRisk\":" + risk + ",\"postBetTotalFreeplay\":0,\"postBetTotalWin\":" + win + ",\"postBetStatus\":false,\"processed\":false,\"lineChangesSetting\":0,\"Bet\":[{\"guid\"" + 
						":\"" + guid + "\",\"post\":false,\"betId\":\"" + betId + "\",\"type\":\"scheduleLine\",\"gameTypeID\"" + 
						":\"" + gameTypeID + "\",\"idGame\":" + idGame + ",\"idlg\":\"" + idlg + "\",\"parentIdg\":" + parentIdg + ",\"idCombinationGame\":" + idCombinationGame + ",\"isGameLine\":" + isGameLine.toString() + 
						",\"odds\":\"" + odds + "\",\"newOdds\":\"\",\"oddsDisplay\":\"" + odds + "\",\"selectedPitcher\":" + selectedPitcher + ",\"pitchers\":\"" + pitcher +  
						"\",\"play\":\"" + play + "\",\"points\":" + points + ",\"newPoints\":0,\"pointsTeaser\":\"" + pointsTeaser + "\",\"pointsDisplay\":\"" + points + "\",\"processed\":false" + 
						",\"postBetAmountType\":\"" + postBetAmountType + "\",\"risk\":" + risk + ",\"riskPost\":0,\"win\":" + win + ",\"winPost\":0,\"sport\":\"" + sport + "\",\"success\":false,\"teamName\"" + 
						":\"" + teamName + "\",\"useFreePlays\":true,\"useFreePlaysPost\":false,\"valid\":true,\"wagerType\"" + 
						":\"" + wagerType + "\",\"allowhookups\":\"true\",\"derivateIndex\":0,\"displayLine\":\"" + displayLine + "\",\"oldDisplayLine\":\"" + displayLine + "\",\"isLineChange\"" + 
						":false,\"isLineChangeInMyFavor\":false,\"acceptedWithLineChange\":false,\"errorMessage\":\"\",\"errorCombinationMsgCode\"" + 
						":0,\"errorCombinationMsg\":\"\",\"isGameOpen\":true,\"teamNumber\":0,\"allowBuyPoints\":true,\"Derivatives\":" + derivativesJson + 
						",\"DerivDesc\":\"\",\"allowInPost\":true,\"betReplaced\":false,\"gameType\":\"" + gameType + "\",\"details\":\"" + details + "\"" + 
						",\"lineType\":\"" + wagerType + "\",\"lineStyle\":\"" + lineStyle + "\",\"categoryDesc\":\"" + categoryDesc + "\",\"leagueDesc\":\"" + leagueDesc + "\"}]" + 
						",\"valid\":0,\"asyncPost\":null}},\"BOSignupRequest\":{}}}}";

				xhtml = super.postJSONSite(httpClientWrapper.getHost() + "/BetslipProxy.aspx/SendBets", jsonString, headerValuePairs);	
				LOGGER.debug("HTML: " + xhtml);
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

		// {
	    //   "d": "{\"openSpotPostTicket\":\"0\",\"openSpotTotalRisk\":\"0\",\"postBetStatus\":false,\"postBetTotalWin\":0.0,\"postBetTotalRisk\":2100.00,\"processed\":false,\"lineChangesSetting\":0,\"Bet\":[{\"guid\":\"608ada7e-3ca4-4b9c-b324-e70edb46dcc1\",\"post\":false,\"betId\":\"4683129_5_3\",\"gameTypeID\":\"82\",\"idGame\":4683129,\"idlg\":\"5\",\"odds\":\"-113\",\"pitchers\":\"A Sampson - R\",\"displayLine\":\"9.5_-105\",\"oldDisplayLine\":\"9.5 -105\",\"play\":\"3\",\"points\":9.5,\"pointsTeaser\":\"9.5\",\"processed\":true,\"risk\":2100.0,\"riskPost\":0.0,\"sport\":\"MLB\",\"success\":false,\"teamName\":\"Texas Rangers\",\"useFreePlays\":true,\"useFreePlaysPost\":false,\"derivateIndex\":0,\"valid\":false,\"wagerType\":\"total\",\"win\":2000.0,\"postWin\":0.0,\"allowBuyPoints\":true,\"allowhookups\":\"true\",\"postBetAmountType\":\"W\",\"selectedPitcher\":3,\"isLineChange\":true,\"errorMessage\":\"The line changed for one (or more) of your selections.\",\"isGameOpen\":true,\"isGameLine\":true,\"allowInPost\":true,\"teamNumber\":0,\"details\":\"Game\",\"idCombinationGame\":4683129,\"lineStyle\":\"E\",\"categoryDesc\":\"BASEBALL\",\"leagueDesc\":\"MAJOR LEAGUE BASEBALL\"}],\"valid\":\"1\",\"activeAsyncPost\":false}"
	    // }
		if (xhtml.contains("The line changed for one (or more) of your selections")) {
			throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed", xhtml);
		}
		// {
	    //	"d": "{\"openSpotPostTicket\":\"0\",\"openSpotTotalRisk\":\"0\",\"postBetStatus\":false,\"postBetTotalWin\":2000.0000,\"postBetTotalRisk\":2300.0000,\"processed\":false,\"lineChangesSetting\":0,\"Bet\":[{\"guid\":\"65cd2234-49de-41eb-ad2f-18b6b123069e\",\"post\":false,\"betId\":\"4671051_1_4\",\"gameTypeID\":\"118\",\"idGame\":4671051,\"idlg\":\"1\",\"odds\":\"-115\",\"pitchers\":\"\",\"displayLine\":\"-115\",\"oldDisplayLine\":\"-115\",\"play\":\"4\",\"points\":0.0,\"pointsTeaser\":\"0\",\"processed\":true,\"risk\":2300.0,\"riskPost\":0.0,\"sport\":\"NFL\",\"success\":false,\"teamName\":\"Denver Broncos\",\"useFreePlays\":true,\"useFreePlaysPost\":false,\"derivateIndex\":0,\"valid\":false,\"wagerType\":\"odds\",\"win\":2000.0,\"postWin\":0.0,\"allowBuyPoints\":true,\"allowhookups\":\"true\",\"postBetAmountType\":\"W\",\"selectedPitcher\":3,\"isLineChange\":false,\"errorMessage\":\"Amount In Money Line Exceeded. Limit For This Game is 1000.00 USD.\",\"isGameOpen\":true,\"isGameLine\":true,\"allowInPost\":true,\"teamNumber\":0,\"details\":\"Game\",\"idCombinationGame\":4671051,\"lineStyle\":\"E\",\"categoryDesc\":\"FOOTBALL\",\"leagueDesc\":\"NFL\"}],\"valid\":\"1\",\"activeAsyncPost\":false}"
	    // }
		if (xhtml.contains("Amount In Money Line Exceeded. Limit For This Game is")) {
			throw new BatchException(BatchErrorCodes.ACCOUNT_BALANCE_LIMIT, BatchErrorMessage.ACCOUNT_BALANCE_LIMIT, "Amount in MoneyLine exceeded limit for this game", xhtml);
		}
				
		if (xhtml.contains("Amount In Total Exceeded.")) {
			throw new BatchException(BatchErrorCodes.ACCOUNT_BALANCE_LIMIT, BatchErrorMessage.ACCOUNT_BALANCE_LIMIT, "Amount in Total exceeded limit for this game", xhtml);
		}

		if (xhtml.contains("Amount In Spread Exceeded.")) {
			throw new BatchException(BatchErrorCodes.ACCOUNT_BALANCE_LIMIT, BatchErrorMessage.ACCOUNT_BALANCE_LIMIT, "Amount in Spread exceeded limit for this game", xhtml);
		}
		
		// final String ticketNumber = S411P.parseTicketNumber(xhtml);
		String ticketNumber = Long.toString(System.currentTimeMillis());
		LOGGER.debug("ticketNumber: " + ticketNumber);

		LOGGER.info("Exiting parseTicketTransaction()");
		return ticketNumber;		
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
				if (key.contains("account") || key.contains("Account")) {
					MAP_DATA.put(key, username);
				} else if (key.contains("password") || key.contains("Password")) {
					MAP_DATA.put(key, password);
				}
			}
		}

		LOGGER.info("Exiting setUsernamePassword()");
	}
}