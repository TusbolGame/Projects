/**
 * 
 */
package com.ticketadvantage.services.dao.sites.tdsportstwelve;

import java.util.ArrayList;
import java.util.HashMap;
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
public class TDSportsTwelveProcessSite extends TDSportsProcessSite {
	private static final Logger LOGGER = Logger.getLogger(TDSportsTwelveProcessSite.class);
	protected TDSportsTwelveParser NSP = new TDSportsTwelveParser();

	/**
	 * 
	 */
	public TDSportsTwelveProcessSite(String host, String username, String password, boolean isMobile,
			boolean showRequestResponse) {
		super("TDSportsTwelve", host, username, password, isMobile, showRequestResponse);
		LOGGER.info("Entering TDSportsTwelveProcessSite()");

		// Setup the parser
		this.siteParser = NSP;

		// Menu items
		NFL_LINES_SPORT = new String[] { "NFL" };
		NFL_LINES_NAME = new String[] { "NFL Gm: Spreads - Money LN - Totals" };
		NFL_FIRST_SPORT = new String[] { "NFL" };
		NFL_FIRST_NAME = new String[] { "NFL 1h: Spreads - Totals" };
		NFL_SECOND_SPORT = new String[] { "NFL" };
		NFL_SECOND_NAME = new String[] { "NFL 2h: Spreads - Totals" };
		NCAAF_LINES_SPORT = new String[] { "COLLEGE FOOTBALL" };
		NCAAF_LINES_NAME = new String[] { "NCAAF Gm: Spreads - Money LN - Totals" };
		NCAAF_FIRST_SPORT = new String[] { "COLLEGE FOOTBALL" };
		NCAAF_FIRST_NAME = new String[] { "NCAAF 1h: Spreads - Totals" };
		NCAAF_SECOND_SPORT = new String[] { "COLLEGE FOOTBALL" };
		NCAAF_SECOND_NAME = new String[] { "NCAAF 2h: Spreads - Totals" };
		NBA_LINES_SPORT = new String[] { "NBA" };
		NBA_LINES_NAME = new String[] { "NBA", "NBA PRESEASON" };
		NBA_FIRST_SPORT = new String[] { "NBA" };
		NBA_FIRST_NAME = new String[] { "NBA 1H" };
		NBA_SECOND_SPORT = new String[] { "NBA" };
		NBA_SECOND_NAME = new String[] { "NBA 2H" };
		NCAAB_LINES_SPORT = new String[] { "BASKETBALL GAME LINES" };
		NCAAB_LINES_NAME = new String[] { "NCAA BASKETBALL - MEN", "NCAAB MEN - EXTRA GAMES", "NCAAB MEN - ADDED GAMES" };
		NCAAB_FIRST_SPORT = new String[] { "BASKETBALL GAME LINES" };
		NCAAB_FIRST_NAME = new String[] { "NCAAB MEN - 1H", "NCAA BASKETBALL - (1H)", "NCAAB MEN - 1H EXTRA GAMES", "NCAAB MEN - 1H ADDED GAMES" };		
		NCAAB_SECOND_SPORT = new String[] { "BASKETBALL GAME LINES" };
		NCAAB_SECOND_NAME = new String[] { "NCAA BASKETBALL - (2H)", "NCAAB MEN - 2H EXTRA GAMES", "NCAAB MEN - 2H" };
		NHL_LINES_SPORT = new String[] { "NHL" };
		NHL_LINES_NAME = new String[] { "NATIONAL NHL LEAGUE", "NHL Gm - Money LN - Totals - Puck LN" };
		NHL_FIRST_SPORT = new String[] { "NHL" };
		NHL_FIRST_NAME = new String[] { "NHL - PERIOD LINES", "NHL - Periods: Money LN - Totals" };
		NHL_SECOND_SPORT = new String[] { "NHL" };
		NHL_SECOND_NAME = new String[] { "NHL - PERIOD LINES", "NHL - Periods: Money LN - Totals" };
		WNBA_LINES_NAME = new String[] { "WNBA" };
		WNBA_LINES_SPORT = new String[] { "BASKETBALL GAME LINES" };
		WNBA_FIRST_NAME = new String[] { "WNBA 1st Half" };
		WNBA_FIRST_SPORT = new String[] { "BASKETBALL GAME LINES" };
		WNBA_SECOND_NAME = new String[] { "WNBA 2nd Half" };
		WNBA_SECOND_SPORT = new String[] { "BASKETBALL GAME LINES" };
		MLB_LINES_SPORT = new String[] { "BASEBALL GAME LINES" };
		MLB_LINES_NAME = new String[] { "MAJOR LEAGUE BASEBALL", "MLB - EXHIBITION BASEBALL" };
		MLB_FIRST_SPORT = new String[] { "BASEBALL GAME LINES" };
		MLB_FIRST_NAME = new String[] { "MLB 1H (5 FULL INNINGS)" };
		MLB_SECOND_SPORT = new String[] { "BASEBALL GAME LINES" };
		MLB_SECOND_NAME = new String[] { "MLB 2H (4 FULL INNINGS+EXTRA INNS)" };

		LOGGER.info("Exiting TDSportsTwelveProcessSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final String[][] TDSITES = new String [][] { 
//				{ "http://names777.com/", "Rc1525", "John", "100", "100", "100", "Chicago", "ET"},
//				{ "http://Betcatalinasports.com/", "catj776", "pink", "100", "100", "100", "None", "ET"}
//				{ "http://anysport247.com", "BCC111", "bobbyx", "0", "0", "0", "New York", "ET"}
//				{ "http://1betvegas.com", "HHK199", "lamp", "0", "0", "0", "New York", "ET"}
//				{ "http://www.1BetVegas.com", "Card7", "Pokerbob1", "0", "0", "0", "None", "ET"}
//				{ "http://everysport247.com", "WB236", "zoom", "0", "0", "0", "None", "ET"}
//				{ "http://1betvegas.com", "HHK199", "lamp", "0", "0", "0", "None", "ET"}
//				{ "http://everygame247.com", "MI231", "MUSKEGON231", "0", "0", "0", "None", "ET"}
//				{ "https://backend.bettnt.com", "BOO102", "1025", "0", "0", "0", "None", "ET"}
//				{ "https://backend.lvaction.com", "XK443", "mattw", "0", "0", "0", "None", "ET"}
//				{ "https://backend.lvaction.com", "ID255", "frank1", "0", "0", "0", "None", "ET"}
				{ "https://backend.lvaction.com", "XK443", "mattw", "0", "0", "0", "None", "ET"}
			};

			// Loop through the sites
			for (int i = 0; i < TDSITES.length; i++) {
				final TDSportsTwelveProcessSite processSite = new TDSportsTwelveProcessSite(TDSITES[i][0], TDSITES[i][1],
						TDSITES[i][2], false, true);
			    processSite.httpClientWrapper.setupHttpClient(TDSITES[0][6]);
			    processSite.processTransaction = false;
			    processSite.timezone = TDSITES[0][7];
			    processSite.siteParser = processSite.TDP = processSite.NSP;
			    processSite.siteParser.setTimezone(TDSITES[0][7]);
			    processSite.NSP.setTimezone(TDSITES[0][7]);

			    String xhtml = processSite.loginToSite(TDSITES[i][1], TDSITES[i][2]);
				final Set<PendingEvent> pendingEvents = processSite.getPendingBets(TDSITES[i][0], TDSITES[i][1], null);

				if (pendingEvents != null && pendingEvents.size() > 0) {
					Iterator<PendingEvent> itr = pendingEvents.iterator();
					while (itr.hasNext()) {
						final PendingEvent pe = itr.next();
						if (pe.getDoposturl()) {
							processSite.doProcessPendingEvent(pe);
						}
						LOGGER.error("PendingEvent: " + pe);
					}
				}
/*
				String xhtml = processSite.loginToSite(TDSITES[i][1], TDSITES[i][2]);

				final PreviewInput previewInput = new PreviewInput();
				previewInput.setLinetype("spread");
				previewInput.setLineindicator("+");
				previewInput.setRotationid(new Integer(355));
				previewInput.setSporttype("ncaaflines");
				previewInput.setProxyname("None");
				previewInput.setTimezone("ET");
				previewInput.setAccountname("000 Bettnt-BOO102");
				previewInput.setSitetype("TDSportsTwelve");

				final PreviewOutput previewData = processSite.previewEvent(previewInput);
				LOGGER.error("PreviewData: " + previewData);

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
*/
/*
			    Map<String, String> map = processSite.getMenuFromSite("ncaablines", xhtml);
			    LOGGER.debug("map: " + map);
				xhtml = processSite.selectSport("ncaablines");
				LOGGER.debug("XHTML: " + xhtml);
				final Iterator<EventPackage> ep = processSite.parseGamesOnSite("ncaablines", xhtml);   
			
				SpreadRecordEvent sre = new SpreadRecordEvent();
				sre.setSpreadplusminussecondone("+");
				sre.setSpreadinputsecondone("6");
				sre.setSpreadjuiceplusminussecondone("-");
				sre.setSpreadinputjuicesecondone("120");
				sre.setEventname("NCAAF #394 Arizona St +6.0 -120.0 for Game");
				sre.setEventtype("spread");
				sre.setSport("ncaablines"); 
				sre.setUserid(new Long(6));
				sre.setEventid(new Integer(533));
				sre.setEventid1(new Integer(533));
				sre.setEventid2(new Integer(534));
				sre.setRotationid(533);

				// Step 9 - Find event
				EventPackage eventPackage = processSite.findEvent(ep, sre);
				if (eventPackage == null) {
					throw new BatchException(BatchErrorCodes.GAME_NOT_AVAILABLE, BatchErrorMessage.GAME_NOT_AVAILABLE, "Game cannot be found on site for " + sre.getEventname(), xhtml);
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

		this.httpClientWrapper.setHost(this.httpClientWrapper.getHost());
		MAP_DATA = new HashMap<String, String>();
		MAP_DATA.put("account", username);
		MAP_DATA.put("password", password);
		final List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
		setupNameValuesEmpty(postValuePairs, MAP_DATA, "");

		String xhtml = postSite(this.httpClientWrapper.getHost() + "/login.aspx", postValuePairs);
		LOGGER.debug("HTML: " + xhtml);

		// Parse login
		MAP_DATA = NSP.parseLogin(xhtml);
		LOGGER.debug("MAP_DATA: " + MAP_DATA);

		// Setup the webapp name
		httpClientWrapper.setWebappname("wager");

		// welcome.aspx
		xhtml = getSite(populateUrl("Message.aspx"));
		LOGGER.debug("HTML: " + xhtml);

		// CreateSports.aspx
		xhtml = getSite(populateUrl("CreateSports.aspx?WT=0"));
		LOGGER.debug("HTML: " + xhtml);

		LOGGER.info("Exiting loginToSite()");
		return xhtml;
	}
}