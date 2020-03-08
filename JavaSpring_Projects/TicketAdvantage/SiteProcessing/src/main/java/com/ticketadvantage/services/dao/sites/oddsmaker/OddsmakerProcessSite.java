/**
 * 
 */
package com.ticketadvantage.services.dao.sites.oddsmaker;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.sites.SiteProcessor;
import com.ticketadvantage.services.dao.sites.SiteTeamPackage;
import com.ticketadvantage.services.dao.sites.SiteTransaction;
import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.AccountEvent;
import com.ticketadvantage.services.model.BaseRecordEvent;
import com.ticketadvantage.services.model.EventPackage;
import com.ticketadvantage.services.model.MlRecordEvent;
import com.ticketadvantage.services.model.PendingEvent;
import com.ticketadvantage.services.model.PreviewInput;
import com.ticketadvantage.services.model.PreviewOutput;
import com.ticketadvantage.services.model.SpreadRecordEvent;
import com.ticketadvantage.services.model.TotalRecordEvent;

/**
 * @author jmiller
 *
 */
public class OddsmakerProcessSite extends SiteProcessor {
	private static final Logger LOGGER = Logger.getLogger(OddsmakerProcessSite.class);
	private static final String MOBILE_HOST = "api.cog.cr";
	private static final String MOBILE_HOST_URL = "https://api.cog.cr";
	private final OddsmakerParser OP = new OddsmakerParser();
	private String userID;
	private String siteAmount;
	private OddsmakerSiteTransaction oddsmakerSiteTransaction;
	private String website;
	private String authorizationToken;
	private String optionValue;
	private int rerunCount;
	private String accessToken;
	private static String captchaStr="";

	/**
	 * 
	 * @param host
	 * @param username
	 * @param password
	 * @param isMobile
	 * @param showRequestResponse
	 */
	public OddsmakerProcessSite(String host, String username, String password, boolean isMobile, boolean showRequestResponse) {
		super("Oddsmaker", host, username, password, isMobile, showRequestResponse);
		LOGGER.info("Entering OddsmakerProcessSite()");

		try {
			int bindex = host.indexOf("m.");
			int eindex = host.lastIndexOf(".");
			if (bindex != -1 && eindex != -1) {
				website = host.substring(bindex + "m.".length(), eindex);
			} else {
				website = host;
			}
		} catch (Throwable t) {
			LOGGER.error(t);
			website = host;
		}

		// Set the parser
		this.siteParser = OP;

		// Setup the menu items
		NFL_LINES_SPORT = new String[] { "Football" };
		NFL_LINES_NAME = new String[] { "NFL" };
		NFL_FIRST_SPORT = new String[] { "Football" };
		NFL_FIRST_NAME = new String[] { "HF1-NFL" };
		NFL_SECOND_SPORT = new String[] { "Football" };
		NFL_SECOND_NAME = new String[] { "HF2-NFL" };
		NCAAF_LINES_SPORT = new String[] { "Football" };
		NCAAF_LINES_NAME = new String[] { "NCAA" };
		NCAAF_FIRST_SPORT = new String[] { "Football" };
		NCAAF_FIRST_NAME = new String[] { "HF1-NCAA" };
		NCAAF_SECOND_SPORT = new String[] { "Football" };
		NCAAF_SECOND_NAME = new String[] { "HF2-NCAA" };
		NBA_LINES_SPORT = new String[] { "Basketball" };
		NBA_LINES_NAME = new String[] { "NBA" };
		NBA_FIRST_SPORT = new String[] { "Basketball" };
		NBA_FIRST_NAME = new String[] { "HF1-NBA" };
		NBA_SECOND_SPORT = new String[] { "Basketball" };
		NBA_SECOND_NAME = new String[] { "HF2-NBA" };
		NCAAB_LINES_SPORT = new String[] { "Basketball" };
		NCAAB_LINES_NAME = new String[] { "NCAA Men's" };
		NCAAB_FIRST_SPORT = new String[] { "Basketball" };
		NCAAB_FIRST_NAME = new String[] { "HF1-NCAA Men's" };
		NCAAB_SECOND_SPORT = new String[] { "Basketball" };
		NCAAB_SECOND_NAME = new String[] { "HF2-NCAA Men's" };
		NHL_LINES_SPORT = new String[] { "Hockey" };
		NHL_LINES_NAME = new String[] { "NHL" };
		NHL_FIRST_SPORT = new String[] { "Hockey" };
		NHL_FIRST_NAME = new String[] { "HF1-NHL" };
		NHL_SECOND_NAME = new String[] { "Hockey" };
		NHL_SECOND_SPORT = new String[] { "HF2-NHL" };
		NHL_THIRD_NAME = new String[] { "Hockey" };
		NHL_THIRD_SPORT = new String[] { "HF3-NHL" };
		WNBA_LINES_SPORT = new String[] { "Basketball" };
		WNBA_LINES_NAME = new String[] { "WNBA" };
		WNBA_FIRST_SPORT = new String[] { "Basketball" };
		WNBA_FIRST_NAME = new String[] { "HF1-WNBA" };
		WNBA_SECOND_SPORT = new String[] { "Basketball" };
		WNBA_SECOND_NAME = new String[] { "HF2-WNBA" };
		MLB_LINES_SPORT = new String[] { "Baseball" };
		MLB_LINES_NAME = new String[] { "MLB" };
		MLB_FIRST_SPORT = new String[] { "Baseball" };
		MLB_FIRST_NAME = new String[] { "HF1-MLB" };
		MLB_SECOND_SPORT = new String[] { "Baseball" };
		MLB_SECOND_NAME = new String[] { "HF2-MLB" };
		INTERNATIONAL_BASEBALL_LINES_SPORT = new String[] { "Baseball", "International Baseball" };
		INTERNATIONAL_BASEBALL_LINES_NAME = new String[] { "Game", "Japan Baseball", "Korean Baseball" };
		INTERNATIONAL_BASEBALL_FIRST_SPORT = new String[] { "Baseball", "International Baseball" };
		INTERNATIONAL_BASEBALL_FIRST_NAME = new String[] { "1st 5 Innings", "Japan Baseball", "Korean Baseball" };
		INTERNATIONAL_BASEBALL_SECOND_SPORT = new String[] { "2nd Halves", "2nd Halves" };
		INTERNATIONAL_BASEBALL_SECOND_NAME = new String[] { "2nd Half", "International Baseball" };

		LOGGER.info("Exiting OddsmakerProcessSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			final String[][] MSITES = new String[][] {
				{"http://www.oddsmaker.ag", "hendu2", "jeremyrh2", "500", "500", "500", "None", "CT"}
			};

			final OddsmakerProcessSite processSite = new OddsmakerProcessSite(MSITES[0][0], MSITES[0][1],
					MSITES[0][2], false, true);
		    processSite.httpClientWrapper.setupHttpClient(MSITES[0][6]);
		    processSite.processTransaction = false;
		    processSite.timezone = MSITES[0][7];
		    processSite.userid = new Long(1);
		    String xhtml= processSite.loginToSite(MSITES[0][1], MSITES[0][2]);

			final PreviewInput previewInput = new PreviewInput();
			previewInput.setLinetype("spread");
			previewInput.setLineindicator("+");
			previewInput.setRotationid(new Integer(229));
			previewInput.setSporttype("ncaaflines");
			previewInput.setProxyname("None");
			previewInput.setTimezone("ET");

			final PreviewOutput previewData = processSite.previewEvent(previewInput);
			LOGGER.error("PreviewData: " + previewData);

/*
			final TotalRecordEvent mre = new TotalRecordEvent();
			mre.setAmount("100");
			mre.setTotaljuiceplusminussecondone("-");
			mre.setTotalinputjuicesecondone("110");
			mre.setTotalinputsecondone("53.5");
			mre.setId(new Long(309));
			mre.setEventname("#NCAAF #206 Fresno State u54.4 -110 for Game");
			mre.setEventtype("total");
			mre.setSport("ncaaflines");
			mre.setUserid(new Long(6));
			mre.setEventid(new Integer(205));
			mre.setEventid1(new Integer(205));
			mre.setEventid2(new Integer(206));
			mre.setRotationid(new Integer(206));
			mre.setWtype("2");

			final AccountEvent ae = new AccountEvent();
			ae.setAmount("100");
			ae.setMaxtotalamount(new Integer("100"));
			ae.setEventid(new Integer(205));
			ae.setEventname("#NCAAF #206 Fresno State u54.4 -110 for Game");
			ae.setRiskamount("110");
			ae.setTotalindicator("u");
			ae.setTotal(new Float(53.5));
			ae.setTotaljuice(new Float(-110));
			ae.setWagertype("2");

			processSite.processTotalTransaction(mre, ae);
*/
/*
			Set<PendingEvent> pendingEvents = processSite.getPendingBets(MSITES[0][0], MSITES[0][1]);
			Iterator<PendingEvent> itr = pendingEvents.iterator();
			while (itr.hasNext()) {
				PendingEvent pe = itr.next();
				LOGGER.error("pe: " + pe);
			}
*/

		    /**
			 ************************************************************************
			 *---------------------code for NCAA  College Football Processing----------------------------------
			 ************************************************************************
			 */
/*
			Map<String, String> map = processSite.getMenuFromSite("ncaaflines", xhtml);
			LOGGER.debug("map: " + map);

			xhtml = processSite.selectSport("ncaaflines");
			LOGGER.debug("XHTML: " + xhtml);

			final Iterator<EventPackage> ep = processSite.parseGamesOnSite("ncaaflines", xhtml);

			final SpreadRecordEvent mre = new SpreadRecordEvent();
			mre.setSpreadjuiceplusminussecondone("-");
			mre.setSpreadinputjuicesecondone("110");
			mre.setSpreadplusminussecondone("-");
			mre.setSpreadinputsecondone("0.5");
			mre.setId(new Long(110));
			mre.setEventname("#8 Vancouver Canucks +.5 -120");
			mre.setEventtype("spread");
			mre.setSport("ncaaflines"); 
			mre.setUserid(new Long(6));
			mre.setEventid(new Integer(205));
			mre.setEventid1(new Integer(205));
			mre.setEventid2(new Integer(206));
			mre.setRotationid(new Integer(206));
			mre.setWtype("2");
			mre.setAmount("100");
			
			// Step 9 - Find event
			EventPackage eventPackage = processSite.findEvent(ep, mre);
			if (eventPackage == null) {
				throw new BatchException(BatchErrorCodes.GAME_NOT_AVAILABLE, BatchErrorMessage.GAME_NOT_AVAILABLE, "Game cannot be found on site for " + mre.getEventname(), xhtml);
			}
*/

		    /**
			 ************************************************************************
			 *---------------------code for NCAA  College Football End here----------------------------------
			 ************************************************************************
			 */

		    /**
			 ************************************************************************
			 *---------------------code for NFL Processing----------------------------------
			 ************************************************************************
			 */

/*		  
		    Map<String, String> map = processSite.getMenuFromSite("ncaablines", xhtml); 
		    LOGGER.debug("map: " + map);
		   
		    xhtml = processSite.selectSport("ncaablines");
			LOGGER.debug("XHTML: " + xhtml);
			
			final Iterator<EventPackage> ep = processSite.parseGamesOnSite("ncaablines", xhtml);

			final MlRecordEvent mre = new MlRecordEvent();
			mre.setMlplusminusfirstone("-");
			mre.setMlinputfirstone("110");
			mre.setId(new Long(520));
			mre.setEventname("#251 San Francisco 49ers -110");
			mre.setEventtype("ml");
			mre.setSport("ncaablines"); 
			mre.setUserid(new Long(6));
			mre.setEventid(new Integer(519));
			mre.setEventid1(new Integer(519));
			mre.setEventid2(new Integer(520));
			mre.setRotationid(new Integer(520));
			mre.setWtype("2");
			mre.setAmount("50");
			
			// Step 9 - Find event
			EventPackage eventPackage = processSite.findEvent(ep, mre);
			if (eventPackage == null) {
				throw new BatchException(BatchErrorCodes.GAME_NOT_AVAILABLE, BatchErrorMessage.GAME_NOT_AVAILABLE, "Game cannot be found on site for " + mre.getEventname(), xhtml);
			}

		    /**
			 ************************************************************************
			 *---------------------code for NFL Processing End here----------------------------------
			 ************************************************************************
			 */
			//=================== Update code END here ==================

			//=========================Old commented code======================
		//	SiteTransaction siteTransaction = new SiteTransaction();
		//	processSite.processCaptcha("xyz");

/*
			final PreviewInput previewInput = new PreviewInput();
			previewInput.setLinetype("spread");
			previewInput.setLineindicator("+");
			previewInput.setRotationid(new Integer(748));
			previewInput.setSporttype("ncaablines");
			previewInput.setProxyname("None");
			previewInput.setTimezone("CT");

			final PreviewOutput previewData = processSite.previewEvent(previewInput);
			LOGGER.error("PreviewData: " + previewData);

			final String[][] MSITES = new String[][] {
					// {"http://www.Betwindycity.com", "w102", "Phillies","500", "500", "500", "Baltimore", "PT"},
					// { "http://www.Sunwager.com", "4488", "John", "300", "300", "300", "Asheville", "ET" },
					// {"http://www.greenisle.club", "Go508", "Iowa", "500","250", "300", "Dallas", "ET" },
					// {"http://www.Scotch88.com", "kwd2202", "jh", "500", "500", "500", "Dallas", "CT"}
//					{"http://www.madmax.ag", "820", "black2020", "500", "500", "500", "Dallas", "CT"}
//					{"http://www.madmax.ag", "834", "sky1212", "500", "500", "500", "Dallas", "CT"}
				// {"http://www.BetWC.ag", "Wc24006", "Demo", "500", "500", "500", "Dallas", "CT"}
				{"http://m.iron69.com", "kwd2202", "jh", "500", "500", "500", "Dallas", "CT"}
			};

			final MetallicaMobileProcessSite processSite = new MetallicaMobileProcessSite(MSITES[0][0], MSITES[0][1],
					MSITES[0][2]);
		    processSite.httpClientWrapper.setupHttpClient(MSITES[0][6]);
		    processSite.processTransaction = false;
		    processSite.timezone = MSITES[0][7];
			processSite.loginToSite("kwd2202", "jh");


			// Loop through the sites
			for (int i = 0; i < MSITES.length; i++) {
				final MetallicaMobileProcessSite processSite = new MetallicaMobileProcessSite(MSITES[i][0], MSITES[i][1],
						MSITES[i][2]);
				final MetallicaMobileProcessSite processSite2 = new MetallicaMobileProcessSite(MSITES[i][0], MSITES[i][1],
						MSITES[i][2]);

				    processSite.httpClientWrapper.setupHttpClient(MSITES[0][6]);
				    processSite.processTransaction = false;
				    processSite.timezone = MSITES[0][7];

				    processSite2.httpClientWrapper.setupHttpClient(MSITES[0][6]);
				    processSite2.processTransaction = false;
				    processSite2.timezone = MSITES[0][7];

					// Now call the test suite
				    MetallicaMobileProcessSite.testSite2(processSite, processSite2, i, MSITES);

				// Now call the test suite
//				MetallicaProcessSite.testSite(processSite, i, MSITES);
			    processSite.httpClientWrapper.setupHttpClient(MSITES[i][6]);
			    processSite.processTransaction = false;
			    processSite.timezone = MSITES[i][7];
				processSite.loginToSite(MSITES[i][1], MSITES[i][2]);
				Set<PendingEvent> pendingEvents = processSite.getPendingBets(MSITES[i][0], MSITES[i][1]);
//				Iterator<PendingEvent> itr = pendingEvents.iterator();
//				while (itr.hasNext()) {
//					PendingEvent pe = itr.next();
//				}
			}
 */
		} catch (Throwable t) {
			LOGGER.error("Throwable Exception", t);
		}
	}

	/**
	 * 
	 * @param accountName
	 * @param accountId
	 * @return
	 * @throws BatchException
	 */
	public Set<PendingEvent> getPendingBets(String accountName, String accountId) throws BatchException {
		LOGGER.info("Entering getPendingBets()");

		// TODO
		
		LOGGER.info("Exiting getPendingBets()");
		return null;
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
		OP.setTimezone(timezone);

		// Get the index page
		String xhtml = getSite(this.httpClientWrapper.getHost());
		MAP_DATA = OP.parseIndex(xhtml);

		// Setup username/password
		MAP_DATA.put("UserName", username);
		MAP_DATA.put("password", password);

		List<NameValuePair> postNameValuePairs = new ArrayList<NameValuePair>(1);
		final String actionName = setupNameValuesEmpty(postNameValuePairs, MAP_DATA, "");
		super.httpClientWrapper.setHost("http://sportsbook.oddsmaker.ag");
		super.httpClientWrapper.setDomain("sportsbook.oddsmaker.ag");

//		final HttpClientWrapper hClient = new HttpClientWrapper("http://sportsbook.oddsmaker.ag", username, password);
//		hClient.setupHttpClient(super.getHttpClientWrapper().getProxyName());

		List<NameValuePair> headerValuePairs = new ArrayList<NameValuePair>(1);
		headerValuePairs.add(new BasicNameValuePair("Content-Type", "application/x-www-form-urlencoded"));
		headerValuePairs.add(new BasicNameValuePair("Upgrade-Insecure-Requests", "1"));
		headerValuePairs.add(new BasicNameValuePair("Referer", this.httpClientWrapper.getPreviousUrl()));

		List<NameValuePair> retValue = super.httpClientWrapper.postSitePage(actionName, null, postNameValuePairs, headerValuePairs);
		String xhtml2 = getCookiesAndXhtml(retValue);
		LOGGER.debug("XHTML2: " + xhtml2);

		String redirect = super.httpClientWrapper.getRedirectLocation(retValue);
		LOGGER.debug("Location: " + redirect);
		if (redirect != null && redirect.length() > 0) {
			LOGGER.error("CHECK LOCATION NAME: " + redirect);
			LOGGER.error("xhtml: " + xhtml);
			
			headerValuePairs = new ArrayList<NameValuePair>(1);
			headerValuePairs.add(new BasicNameValuePair("Content-Type", "application/x-www-form-urlencoded"));
			headerValuePairs.add(new BasicNameValuePair("Upgrade-Insecure-Requests", "1"));
			headerValuePairs.add(new BasicNameValuePair("Referer", "http://www.oddsmaker.ag/"));
			super.httpClientWrapper.setHost("http://www.oddsmaker.ag");
			super.httpClientWrapper.setDomain("www.oddsmaker.ag");
			super.httpClientWrapper.getSitePage(redirect, null, headerValuePairs);
		}

		super.httpClientWrapper.setHost("http://www.oddsmaker.ag");
		super.httpClientWrapper.setDomain("www.oddsmaker.ag");
		postNameValuePairs = new ArrayList<NameValuePair>(1);
		MAP_DATA.clear();
		MAP_DATA.put("getUserID", "submit");
		MAP_DATA.put("username", username);
		setupNameValuesEmpty(postNameValuePairs, MAP_DATA, "");
		String json = postSite(super.httpClientWrapper.getHost() + "/sportsbook/views/form.php", postNameValuePairs);
		MAP_DATA = OP.parseLogin(json);
		userID = MAP_DATA.get("userID");

		// HTTP Header GET
		headerValuePairs = new ArrayList<NameValuePair>(3);
		headerValuePairs.add(new BasicNameValuePair("Host", "www.oddsmaker.ag"));
		headerValuePairs.add(new BasicNameValuePair("Referer", this.httpClientWrapper.getHost() + "/"));
		super.httpClientWrapper.setHost("http://www.oddsmaker.ag");
		super.httpClientWrapper.setDomain("www.oddsmaker.ag");

		// Get the menu
		json = getJSONSite(super.httpClientWrapper.getHost() + "/sportsbook/src/cli/json/Menu.json?9snf2c7go6dfvym43j7k", headerValuePairs);

		LOGGER.info("Exiting loginToSite()");
		return json;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#selectSport(java.lang.String)
	 */
	@Override
	protected String selectSport(String type) throws BatchException {
		LOGGER.info("Entering selectSport()");
		LOGGER.debug("type: " + type);
		String sportdata = null;

		// HTTP GET
		Set<String> keys = MAP_DATA.keySet();
		for (String key : keys) {
			final String url = MAP_DATA.get(key);
			this.httpClientWrapper.setHost("http://www.oddsmaker.ag");
			this.httpClientWrapper.setDomain("www.oddsmaker.ag");
			sportdata = getSite(super.httpClientWrapper.getHost() + url);
			LOGGER.error("sportdata: " + sportdata);
		}

		LOGGER.info("Exiting selectSport()");
		return sportdata;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#createSiteTransaction()
	 */
	@Override
	protected SiteTransaction createSiteTransaction() {
		LOGGER.info("Entering createSiteTransaction()");

		final OddsmakerSiteTransaction siteTransaction = new OddsmakerSiteTransaction();

		LOGGER.info("Exiting createSiteTransaction()");
		return siteTransaction;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#createSpreadTransaction(com.ticketadvantage.services.dao.sites.SiteTeamPackage, com.ticketadvantage.services.model.SpreadRecordEvent, int, float, com.ticketadvantage.services.model.AccountEvent)
	 */
	@Override
	public SiteTransaction createSpreadTransaction(SiteTeamPackage siteTeamPackage, SpreadRecordEvent spreadRecordEvent, int arrayNum, float juice, AccountEvent accountEvent) throws BatchException {
		LOGGER.info("Entering createSpreadTransaction()");
		LOGGER.debug("SpreadRecordEvent: " + spreadRecordEvent);
		LOGGER.debug("SiteTeamPackage: " + siteTeamPackage);
		LOGGER.debug("arrayNum: " + arrayNum);
		LOGGER.debug("juice: " + juice);
		LOGGER.debug("AccountEvent: " + accountEvent);

		final OddsmakerTeamPackage teamPackage = (OddsmakerTeamPackage)siteTeamPackage;
		final OddsmakerSiteTransaction siteTransaction = (OddsmakerSiteTransaction)createSiteTransaction();
		siteTransaction.setOptionValue(teamPackage.getGameSpreadOptionValue().get(Integer.toString(arrayNum)));
		siteTransaction.setSelectName(teamPackage.getGameSpreadSelectName());
		siteTransaction.setInputName(teamPackage.getGameSpreadInputName());
		siteTransaction.setSelectId(teamPackage.getGameSpreadSelectId());
		siteTransaction.setInputId(teamPackage.getGameSpreadInputId());
		siteTransaction.setInputValue(teamPackage.getGameSpreadInputValue());
		siteTransaction.setWagerOrder(teamPackage.getSpreadwagerOrder());
		siteTransaction.setWagerType(teamPackage.getSpreadwagerType());
		siteTransaction.setLineOrder(teamPackage.getSpreadlineOrder());
		siteTransaction.setLineType(teamPackage.getSpreadlineType());
		siteTransaction.setLineID(teamPackage.getSpreadlineID());

		// Set the amount
		final Double newAmount = amountToApply(juice, spreadRecordEvent.getWtype(), spreadRecordEvent.getAmount(), accountEvent.getMaxspreadamount(), accountEvent);
		LOGGER.debug("newAmount: " + newAmount);
		siteTransaction.setAmount(Double.toString(newAmount));
		accountEvent.setAmount(spreadRecordEvent.getAmount());
		accountEvent.setActualamount(Double.toString(newAmount));

		LOGGER.info("Exiting createSpreadTransaction()");
		return siteTransaction;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#createTotalTransaction(com.ticketadvantage.services.dao.sites.SiteTeamPackage, com.ticketadvantage.services.model.TotalRecordEvent, int, float, com.ticketadvantage.services.model.AccountEvent)
	 */
	@Override
	public SiteTransaction createTotalTransaction(SiteTeamPackage siteTeamPackage, TotalRecordEvent totalRecordEvent,
			int arrayNum, float juice, AccountEvent accountEvent) throws BatchException {
		LOGGER.info("Entering createTotalTransaction()");
		LOGGER.debug("TotalRecordEvent: " + totalRecordEvent);
		LOGGER.debug("SiteTeamPackage: " + siteTeamPackage);
		LOGGER.debug("arrayNum: " + arrayNum);
		LOGGER.debug("juice: " + juice);
		LOGGER.debug("AccountEvent: " + accountEvent);

		final OddsmakerTeamPackage teamPackage = (OddsmakerTeamPackage)siteTeamPackage;
		final OddsmakerSiteTransaction siteTransaction = (OddsmakerSiteTransaction)createSiteTransaction();
		siteTransaction.setOptionValue(siteTeamPackage.getGameTotalOptionValue().get(Integer.toString(arrayNum)));
		siteTransaction.setSelectName(siteTeamPackage.getGameTotalSelectName());
		siteTransaction.setInputName(siteTeamPackage.getGameTotalInputName());
		siteTransaction.setSelectId(siteTeamPackage.getGameTotalSelectId());
		siteTransaction.setInputId(siteTeamPackage.getGameTotalInputId());
		siteTransaction.setInputValue(siteTeamPackage.getGameTotalInputValue());
		siteTransaction.setWagerOrder(teamPackage.getTotalwagerOrder());
		siteTransaction.setWagerType(teamPackage.getTotalwagerType());
		siteTransaction.setLineOrder(teamPackage.getTotallineOrder());
		siteTransaction.setLineType(teamPackage.getTotallineType());
		siteTransaction.setLineID(teamPackage.getTotallineID());

		// Set the amount
		final Double newAmount = amountToApply(juice, totalRecordEvent.getWtype(), totalRecordEvent.getAmount(),
				accountEvent.getMaxtotalamount(), accountEvent);
		LOGGER.debug("newAmount: " + newAmount);
		siteTransaction.setAmount(Double.toString(newAmount));
		accountEvent.setAmount(totalRecordEvent.getAmount());
		accountEvent.setActualamount(Double.toString(newAmount));

		LOGGER.info("Exiting createTotalTransaction()");
		return siteTransaction;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#createMoneyLineTransaction(com.ticketadvantage.services.dao.sites.SiteTeamPackage, com.ticketadvantage.services.model.MlRecordEvent, int, float, com.ticketadvantage.services.model.AccountEvent)
	 */
	@Override
	public SiteTransaction createMoneyLineTransaction(SiteTeamPackage siteTeamPackage, MlRecordEvent mlRecordEvent,
			int arrayNum, float juice, AccountEvent accountEvent) throws BatchException {
		LOGGER.info("Entering createMoneyLineTransaction()");
		LOGGER.debug("MlRecordEvent: " + mlRecordEvent);
		LOGGER.debug("SiteTeamPackage: " + siteTeamPackage);
		LOGGER.debug("arrayNum: " + arrayNum);
		LOGGER.debug("juice: " + juice);
		LOGGER.debug("AccountEvent: " + accountEvent);

		final OddsmakerTeamPackage teamPackage = (OddsmakerTeamPackage)siteTeamPackage;
		final OddsmakerSiteTransaction siteTransaction = (OddsmakerSiteTransaction)createSiteTransaction();
		siteTransaction.setOptionValue(siteTeamPackage.getGameMLInputValue().get(Integer.toString(arrayNum)));
		siteTransaction.setInputName(siteTeamPackage.getGameMLInputName());
		siteTransaction.setInputId(siteTeamPackage.getGameMLInputId());
		siteTransaction.setInputValue(siteTeamPackage.getGameMLInputValue().get(Integer.toString(arrayNum)));
		siteTransaction.setWagerOrder(teamPackage.getMlwagerOrder());
		siteTransaction.setWagerType(teamPackage.getMlwagerType());
		siteTransaction.setLineOrder(teamPackage.getMllineOrder());
		siteTransaction.setLineType(teamPackage.getMllineType());
		siteTransaction.setLineID(teamPackage.getMllineID());

		// Set the amount
		final Double newAmount = amountToApply(juice, mlRecordEvent.getWtype(), mlRecordEvent.getAmount(),
				accountEvent.getMaxmlamount(), accountEvent);
		LOGGER.debug("newAmount: " + newAmount);
		siteTransaction.setAmount(Double.toString(newAmount));
		accountEvent.setAmount(mlRecordEvent.getAmount());
		accountEvent.setActualamount(Double.toString(newAmount));

		LOGGER.info("Exiting createMoneyLineTransaction()");
		return siteTransaction;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#selectEvent(com.ticketadvantage.services.dao.sites.SiteTransaction, com.ticketadvantage.services.model.EventPackage, com.ticketadvantage.services.model.BaseRecordEvent, com.ticketadvantage.services.model.AccountEvent)
	 */
	@Override
	protected String selectEvent(SiteTransaction siteTransaction, EventPackage eventPackage, BaseRecordEvent event, AccountEvent ae) throws BatchException {
		LOGGER.info("Entering selectEvent()");
		LOGGER.debug("SiteTransaction: " + siteTransaction);
		LOGGER.debug("EventPackage: " + eventPackage);
		LOGGER.debug("BaseRecordEvent: " + event);
		LOGGER.debug("AccountEvent: " + ae);

		final OddsmakerEventPackage siteEventPackage = (OddsmakerEventPackage)eventPackage;
		siteAmount = siteTransaction.getAmount();
		LOGGER.error("siteAmount: " + siteAmount);

		// Get the spread transaction
		if (event instanceof SpreadRecordEvent) {
			siteTransaction = getSpreadTransaction((SpreadRecordEvent)event, eventPackage, ae);
			siteAmount = siteTransaction.getAmount();
			Double mAmount = Double.parseDouble(siteAmount);
			LOGGER.error("mAmount: " + mAmount);

			if (mAmount != null) {
				LOGGER.error("siteTransaction.getRiskorwin().intValue(): " + siteTransaction.getRiskorwin().intValue());

				if (siteTransaction.getRiskorwin().intValue() == 2) {
					float spreadJuice = ae.getSpreadjuice();
					LOGGER.error("spreadJuice: " + spreadJuice);
					mAmount = mAmount * (spreadJuice / 100);
					LOGGER.error("mAmount: " + mAmount);
					siteAmount = Double.toString(Math.abs(round(mAmount.doubleValue(), 2)));
					LOGGER.error("siteAmount: " + siteAmount);
					LOGGER.error("siteEventPackage.getSpreadMax(): " + siteEventPackage.getSpreadMax());

					if (siteEventPackage.getSpreadMax() != null && 
						mAmount.doubleValue() > siteEventPackage.getSpreadMax().intValue()) {
						siteAmount = siteEventPackage.getSpreadMax().toString();
						LOGGER.error("siteAmount: " + siteAmount);
					}
				} else {
					float spreadJuice = ae.getSpreadjuice();
					LOGGER.error("spreadJuice: " + spreadJuice);
					Double risk = mAmount * (100 / spreadJuice);
					risk = round(risk.doubleValue(), 2);
					LOGGER.error("risk: " + risk);
					LOGGER.error("siteEventPackage.getSpreadMax(): " + siteEventPackage.getSpreadMax());

					if (siteEventPackage.getSpreadMax() != null && 
						risk.doubleValue() > siteEventPackage.getSpreadMax().intValue()) {
						siteAmount = siteEventPackage.getSpreadMax().toString();
						LOGGER.error("siteAmount: " + siteAmount);
					} else {
						siteAmount = risk.toString();
						LOGGER.error("siteAmount: " + siteAmount);
					}
				}
			}

			if (siteTransaction.getOptionValue() != null) {
				optionValue = siteTransaction.getOptionValue();
			}
		} else if (event instanceof TotalRecordEvent) {
			siteTransaction = getTotalTransaction((TotalRecordEvent)event, eventPackage, ae);
			Double mAmount = Double.parseDouble(siteAmount);
			LOGGER.debug("mAmount: " + mAmount);

			if (mAmount != null) {
				if (siteTransaction.getRiskorwin().intValue() == 2) {
					float totalJuice = ae.getTotaljuice();
					mAmount = mAmount * (totalJuice / 100);
					siteAmount = Double.toString(Math.abs(round(mAmount.doubleValue(), 2)));

					if (siteEventPackage.getTotalMax() != null && 
						mAmount.doubleValue() > siteEventPackage.getTotalMax().intValue()) {
						siteAmount = siteEventPackage.getTotalMax().toString();
					}
				} else {
					float totalJuice = ae.getTotaljuice();
					LOGGER.debug("totalJuice: " + totalJuice);
					Double risk = mAmount * (100 / totalJuice);
					risk = round(risk.doubleValue(), 2);

					if (siteEventPackage.getTotalMax() != null && risk.doubleValue() > siteEventPackage.getTotalMax().intValue()) {
						siteAmount = siteEventPackage.getTotalMax().toString();
					} else {
						siteAmount = risk.toString();
					}
				}
			}

			if (siteTransaction.getOptionValue() != null) {
				optionValue = siteTransaction.getOptionValue();
			}
		} else if (event instanceof MlRecordEvent) {
			siteTransaction = getMlTransaction((MlRecordEvent)event, eventPackage, ae);
			siteAmount = siteTransaction.getAmount();
			Double mAmount = Double.parseDouble(siteAmount);
			LOGGER.debug("mAmount: " + mAmount);

			if (mAmount != null) {
				if (siteTransaction.getRiskorwin().intValue() == 2) {
					float mlJuice = ae.getMljuice();
					Double risk = mAmount * (100 / mlJuice);
					risk = round(risk.doubleValue(), 2);
					siteAmount = risk.toString();

					if (siteEventPackage.getMlMax() != null && 
						mAmount.doubleValue() > siteEventPackage.getMlMax().intValue()) {
						siteAmount = siteEventPackage.getMlMax().toString();
					}
				} else {
					Double risk = mAmount;
					risk = round(risk.doubleValue(), 2);

					if (siteEventPackage.getMlMax() != null && 
						risk.doubleValue() > siteEventPackage.getMlMax().intValue()) {
						siteAmount = siteEventPackage.getMlMax().toString();
					} else {
						siteAmount = risk.toString();
					}
				}
			}

			optionValue = siteTransaction.getInputValue();
		}

		siteAmount = siteAmount.replace("-", "");
		siteTransaction.setAmount(siteAmount);
		ae.setActualamount(siteAmount);
		oddsmakerSiteTransaction = (OddsmakerSiteTransaction)siteTransaction;

		// HTTP Post
		String json = addBet(siteAmount, oddsmakerSiteTransaction, eventPackage, event, ae);

		LOGGER.info("Exiting selectEvent()");
		return json;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#parseEventSelection(java.lang.String, com.ticketadvantage.services.dao.sites.SiteTransaction, com.ticketadvantage.services.model.EventPackage, com.ticketadvantage.services.model.BaseRecordEvent, com.ticketadvantage.services.model.AccountEvent)
	 */
	@Override
	protected void parseEventSelection(String xhtml, SiteTransaction siteTransaction, EventPackage eventPackage, BaseRecordEvent event, AccountEvent ae) throws BatchException {
		LOGGER.info("Entering parseEventSelection()");
		Map<String, String> wagers = null;

		try {
			wagers = OP.parseEventSelection(xhtml, null, null);
			LOGGER.debug("wagers: " + wagers);
		} catch (BatchException be) {
			if (be.getErrorcode() == BatchErrorCodes.LINE_CHANGED_ERROR) {
				xhtml = processLineChange("AddBet", xhtml, siteAmount, oddsmakerSiteTransaction, eventPackage, event, ae);
				wagers = OP.parseEventSelection(xhtml, null, null);
			} else {
				throw be;
			}
		}

		// Check for a wager limit and rerun
		if (wagers.containsKey("wageramount")) {
			// Only call the select event once
			if (rerunCount++ == 0) {
				siteTransaction.setAmount(wagers.get("wageramount"));
				ae.setAmount(wagers.get("wageramount"));
				ae.setActualamount(wagers.get("wageramount"));
				xhtml = selectEvent(siteTransaction, eventPackage, event, ae);

				wagers = OP.parseEventSelection(xhtml, null, null);
				LOGGER.debug("wagers: " + wagers);

				// Check for a wager limit and rerun
				if (wagers.containsKey("wageramount")) {
					throw new BatchException(BatchErrorCodes.MAX_WAGER_AMOUNT_REACHED, BatchErrorMessage.MAX_WAGER_AMOUNT_REACHED, "The maximum wager amount has been reached " + siteTransaction.getAmount(), xhtml);
				} else if (wagers.containsKey("wageraccountexceeded")) {
					throw new BatchException(BatchErrorCodes.MAX_WAGER_AMOUNT_REACHED, BatchErrorMessage.MAX_WAGER_AMOUNT_REACHED, "The total risk amount for your selected wagers is greater than your amount available of " + wagers.get("wageraccountexceeded"), xhtml);
				}
			} else {
				throw new BatchException(BatchErrorCodes.MAX_WAGER_AMOUNT_REACHED, BatchErrorMessage.MAX_WAGER_AMOUNT_REACHED, "The maximum wager amount has been reached " + siteTransaction.getAmount(), xhtml);
			}
		} else if (wagers.containsKey("wageraccountexceeded")) {
			throw new BatchException(BatchErrorCodes.MAX_WAGER_AMOUNT_REACHED, BatchErrorMessage.MAX_WAGER_AMOUNT_REACHED, "The total risk amount for your selected wagers is greater than your amount available of " + wagers.get("wageraccountexceeded"), xhtml);
		}

		if (wagers.containsKey("wagerValidationStatus") && !wagers.get("wagerValidationStatus").equals("true")) {
			throw new BatchException(BatchErrorCodes.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, BatchErrorMessage.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, "Wager Validation Failed", xhtml);
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
		String json = null;

		// Actually process the transaction?
		if (processTransaction) {
			// Save bet
			json = saveBet(siteAmount, oddsmakerSiteTransaction, eventPackage, event, ae);

			try {
				OP.parseTicketNumber(json);
			} catch (BatchException be) {
				if (be.getErrorcode() == BatchErrorCodes.LINE_CHANGED_ERROR) {
					json = processLineChange("SaveBet", json, siteAmount, oddsmakerSiteTransaction, eventPackage, event, ae);
				} else {
					throw be;
				}
			}
		}

		LOGGER.info("Exiting completeTransaction()");
		return json;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#parseTicketTransaction(java.lang.String)
	 */
	@Override
	protected String parseTicketTransaction(String xhtml) throws BatchException {
		LOGGER.info("Entering parseTicketNumber()");		
		String ticketNumber = null;

		// Actually process the transaction?
		if (processTransaction) {
			try {
				ticketNumber = OP.parseTicketNumber(xhtml);
				LOGGER.debug("ticketNumber: " + ticketNumber);
			} catch (BatchException be) {
				throw be;
			}
		}

		LOGGER.info("Exiting parseTicketNumber()");
		return ticketNumber;		
	}

	/**
	 * 
	 * @param siteAmount
	 * @param oddsmakerSiteTransaction
	 * @param eventPackage
	 * @param event
	 * @param ae
	 * @return
	 * @throws BatchException
	 */
	private String addBet(String siteAmount, OddsmakerSiteTransaction oddsmakerSiteTransaction, EventPackage eventPackage, BaseRecordEvent event, AccountEvent ae) throws BatchException {
		LOGGER.info("Entering addBet()");

		MAP_DATA.clear();
		MAP_DATA.put("action", "checkUserSession");
		List<NameValuePair> postNameValuePairs = new ArrayList<NameValuePair>(1);
		setupNameValuesEmptyNoAction(postNameValuePairs, MAP_DATA, "");
		LOGGER.debug("postNameValuePairs: " + postNameValuePairs);
		String xhtml = postSite(super.httpClientWrapper.getHost() + "/sportsbook/views/form.php", postNameValuePairs);

		MAP_DATA.clear();
		MAP_DATA.put("action", "ValidateWager");
		MAP_DATA.put("wagersPlaced[request][userID]", userID);
		MAP_DATA.put("wagersPlaced[request][applyPromocode]", "false");
		MAP_DATA.put("wagersPlaced[request][wagers][0][wagerOrder]", oddsmakerSiteTransaction.getWagerOrder().toString());
		MAP_DATA.put("wagersPlaced[request][wagers][0][wagerType]", oddsmakerSiteTransaction.getWagerType().toString());
		MAP_DATA.put("wagersPlaced[request][wagers][0][wagerAmount]", siteAmount);
		MAP_DATA.put("wagersPlaced[request][wagers][0][wageredLines][0][lineID]", oddsmakerSiteTransaction.getLineID().toString());
		MAP_DATA.put("wagersPlaced[request][wagers][0][wageredLines][0][lineType]", oddsmakerSiteTransaction.getLineType().toString());
		MAP_DATA.put("wagersPlaced[request][wagers][0][wageredLines][0][lineOrder]", oddsmakerSiteTransaction.getLineOrder().toString());

		if (event.getEventtype().equals("spread")) {
			final Double juice = round(ae.getSpreadjuice(), 2);
			String juiceString = juice.toString();
			if (juiceString.endsWith(".0")) {
				juiceString = juiceString.replace(".0", "");
			}
			MAP_DATA.put("wagersPlaced[request][wagers][0][wageredLines][0][nonMatchupLineAttribute][originalLineOdds]", juiceString);
			MAP_DATA.put("wagersPlaced[request][wagers][0][wageredLines][0][nonMatchupLineAttribute][pointSpreadLineAttribute][originalPointSpread]", ae.getSpread().toString());
		} else if (event.getEventtype().equals("total")) {
			final Double juice = round(ae.getTotaljuice(), 2);
			String juiceString = juice.toString();
			if (juiceString.endsWith(".0")) {
				juiceString = juiceString.replace(".0", "");
			}
			MAP_DATA.put("wagersPlaced[request][wagers][0][wageredLines][0][nonMatchupLineAttribute][originalLineOdds]", juiceString);

			final String totaltype = ae.getTotalindicator();
			if (totaltype != null && totaltype.equals("o")) {
				MAP_DATA.put("wagersPlaced[request][wagers][0][wageredLines][0][nonMatchupLineAttribute][eventOverLineAttribute][overLineWagered]", ae.getTotal().toString());
			} else {
				MAP_DATA.put("wagersPlaced[request][wagers][0][wageredLines][0][nonMatchupLineAttribute][eventUnderLineAttribute][underLineWagered]", ae.getTotal().toString());
			}
		} else if (event.getEventtype().equals("ml")) {
			final Double juice = round(ae.getMljuice(), 2);
			String juiceString = juice.toString();
			if (juiceString.endsWith(".0")) {
				juiceString = juiceString.replace(".0", "");
			}
			MAP_DATA.put("wagersPlaced[request][wagers][0][wageredLines][0][nonMatchupLineAttribute][originalLineOdds]", juiceString);
			MAP_DATA.put("wagersPlaced[request][wagers][0][wageredLines][0][nonMatchupLineAttribute][moneyLineLineAttribute][baseballOptionWagered]", "1");
		}

		// PostNameValuePairs
		postNameValuePairs = new ArrayList<NameValuePair>(1);
		setupNameValuesEmptyNoAction(postNameValuePairs, MAP_DATA, "");

		// HTTP Post
		xhtml = postSite(super.httpClientWrapper.getHost() + "/sportsbook/views/form.php", postNameValuePairs);

		LOGGER.info("Exiting addBet()");
		return xhtml;
	}

	/**
	 * 
	 * @param siteAmount
	 * @param oddsmakerSiteTransaction
	 * @param eventPackage
	 * @param event
	 * @param ae
	 * @return
	 * @throws BatchException
	 */
	private String saveBet(String siteAmount, OddsmakerSiteTransaction oddsmakerSiteTransaction, EventPackage eventPackage, BaseRecordEvent event, AccountEvent ae) throws BatchException {
		LOGGER.info("Entering saveBet()");

		MAP_DATA.clear();
		MAP_DATA.put("action", "ConfirmWager");
		MAP_DATA.put("validatedWager[request][userID]", userID);
		MAP_DATA.put("validatedWager[request][applyPromocode]", "false");
		MAP_DATA.put("validatedWager[request][wagers][0][wagerOrder]", oddsmakerSiteTransaction.getWagerOrder().toString());
		MAP_DATA.put("validatedWager[request][wagers][0][wagerType]", oddsmakerSiteTransaction.getWagerType().toString());
		MAP_DATA.put("validatedWager[request][wagers][0][wagerAmount]", siteAmount);
		MAP_DATA.put("validatedWager[request][wagers][0][wageredLines][0][lineID]", oddsmakerSiteTransaction.getLineID().toString());
		MAP_DATA.put("validatedWager[request][wagers][0][wageredLines][0][lineType]", oddsmakerSiteTransaction.getLineType().toString());
		MAP_DATA.put("validatedWager[request][wagers][0][wageredLines][0][lineOrder]", oddsmakerSiteTransaction.getLineOrder().toString());

		if (event.getEventtype().equals("spread")) {
			final Double juice = round(ae.getSpreadjuice(), 2);
			String juiceString = juice.toString();
			if (juiceString.endsWith(".0")) {
				juiceString = juiceString.replace(".0", "");
			}

			MAP_DATA.put("validatedWager[request][wagers][0][wageredLines][0][nonMatchupLineAttribute][originalLineOdds]", juiceString);
			MAP_DATA.put("validatedWager[request][wagers][0][wageredLines][0][nonMatchupLineAttribute][pointSpreadLineAttribute][originalPointSpread]", ae.getSpread().toString());
		} else if (event.getEventtype().equals("total")) {
			final Double juice = round(ae.getTotaljuice(), 2);
			String juiceString = juice.toString();
			if (juiceString.endsWith(".0")) {
				juiceString = juiceString.replace(".0", "");
			}
			MAP_DATA.put("validatedWager[request][wagers][0][wageredLines][0][nonMatchupLineAttribute][originalLineOdds]", juiceString);

			final String totaltype = ae.getTotalindicator();
			if (totaltype != null && totaltype.equals("o")) {
				MAP_DATA.put("validatedWager[request][wagers][0][wageredLines][0][nonMatchupLineAttribute][eventOverLineAttribute][overLineWagered]", ae.getTotal().toString());
			} else {
				MAP_DATA.put("validatedWager[request][wagers][0][wageredLines][0][nonMatchupLineAttribute][eventUnderLineAttribute][underLineWagered]", ae.getTotal().toString());
			}
		} else if (event.getEventtype().equals("ml")) {
			final Double juice = round(ae.getMljuice(), 2);
			String juiceString = juice.toString();
			if (juiceString.endsWith(".0")) {
				juiceString = juiceString.replace(".0", "");
			}

			MAP_DATA.put("validatedWager[request][wagers][0][wageredLines][0][nonMatchupLineAttribute][originalLineOdds]", juiceString);
			MAP_DATA.put("validatedWager[request][wagers][0][wageredLines][0][nonMatchupLineAttribute][moneyLineLineAttribute][baseballOptionWagered]", "1");
		}

		// Setup transactionID
		MAP_DATA.put("validatedWager[request][emailReceipt]", "false");
		MAP_DATA.put("validatedWager[request][transactionID]", getTransactionID());

		final List<NameValuePair> postNameValuePairs = new ArrayList<NameValuePair>(1);
		setupNameValuesEmptyNoAction(postNameValuePairs, MAP_DATA, "");

		// HTTP Post
		String xhtml = postSite(super.httpClientWrapper.getHost() + "/sportsbook/views/form.php", postNameValuePairs);
		
		LOGGER.info("Exiting saveBet()");
		return xhtml;
	}

	/**
	 * 
	 * @param addSave
	 * @param xhtml
	 * @param siteAmount
	 * @param oddsmakerSiteTransaction
	 * @param eventPackage
	 * @param event
	 * @param ae
	 * @return
	 * @throws BatchException
	 */
	private String processLineChange(String addSave, String xhtml, String siteAmount, OddsmakerSiteTransaction oddsmakerSiteTransaction, EventPackage eventPackage, BaseRecordEvent event, AccountEvent ae) throws BatchException {
		LOGGER.info("Entering processLineChange()");

		final Map<String, String> lineChanges = OP.processLineChange(xhtml);
		if (lineChanges != null && !lineChanges.isEmpty()) {
			optionValue = lineChanges.get("Id");
			if (event instanceof SpreadRecordEvent) {
				final SpreadRecordEvent sre = (SpreadRecordEvent)event;
				if (determineSpreadLineChange(sre, ae, lineChanges)) {
					if (addSave.equals("AddBet")) {
						xhtml = addBet(ae.getActualamount(), oddsmakerSiteTransaction, eventPackage, event, ae);
					} else {
						xhtml = saveBet(ae.getActualamount(), oddsmakerSiteTransaction, eventPackage, event, ae);
					}
				} else {
					throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed", xhtml);
				}
			} else if (event instanceof TotalRecordEvent) {
				final TotalRecordEvent tre = (TotalRecordEvent)event;

				// Set the over/under
				if (tre.getTotalinputfirstone() != null && tre.getTotalinputfirstone().length() > 0) {
					lineChanges.put("valueindicator", "o");
				} else {
					lineChanges.put("valueindicator", "u");
				}

				if (determineTotalLineChange(tre, ae, lineChanges)) {
					if (addSave.equals("AddBet")) {
						xhtml = addBet(ae.getActualamount(), oddsmakerSiteTransaction, eventPackage, event, ae);
					} else {
						xhtml = saveBet(ae.getActualamount(), oddsmakerSiteTransaction, eventPackage, event, ae);
					}
				} else {
					throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed", xhtml);
				}
			} else if (event instanceof MlRecordEvent) {
				final MlRecordEvent mre = (MlRecordEvent)event;
				
				if (determineMlLineChange(mre, ae, lineChanges)) {
					if (addSave.equals("AddBet")) {
						xhtml = addBet(ae.getActualamount(), oddsmakerSiteTransaction, eventPackage, event, ae);
					} else {
						xhtml = saveBet(ae.getActualamount(), oddsmakerSiteTransaction, eventPackage, event, ae);
					}
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
	 * @return
	 */
	private String getTransactionID() {
		String result = "";
		int length = 64;
		char[] chars = {'0','1','2','3','4','5','6','7','8','9',
			'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
			'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'
		};
		
		for (int i = length; i > 0; --i) {
			result += chars[(int)Math.floor(Math.random() * chars.length)];        
		}

		return result;
	}

	/**
	 * 
	 * @param nameValuePairs
	 * @param map
	 * @param webappName
	 * @return
	 * @throws BatchException
	 */
	protected String setupNameValuesEmptyNoAction(List<NameValuePair> nameValuePairs, Map<String, String> map,
			String webappName) throws BatchException {
		LOGGER.info("Entering setupNameValuesEmptyNoAction()");
		LOGGER.debug("WebappName: " + webappName);
		String actionLogin = null;

		// Check for a valid map
		if (map != null && !map.isEmpty()) {
			final Set<Entry<String, String>> indexs = map.entrySet();
			if (indexs != null && !indexs.isEmpty()) {
				final Iterator<Entry<String, String>> itr = indexs.iterator();
				while (itr != null && itr.hasNext()) {
					final Entry<String, String> values = itr.next();
					String key = values.getKey();
					String value = values.getValue();
					LOGGER.debug("KEY: " + key);
					LOGGER.debug("VALUE: " + value);

					if (value != null && value.length() > 0 && key != null && key.length() > 0) {
						nameValuePairs.add(new BasicNameValuePair(key, value));
					} else if (key != null && key.length() > 0) {
						nameValuePairs.add(new BasicNameValuePair(key, ""));
					}
				}
			}
		}

		LOGGER.info("Exiting setupNameValuesEmptyNoAction()");
		return actionLogin;
	}
}