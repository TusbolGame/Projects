/**
 * 
 */
package com.ticketadvantage.services.dao.sites.tdsportseight;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.sites.tdsports.TDSportsProcessSite;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.AccountEvent;
import com.ticketadvantage.services.model.SpreadRecordEvent;
import com.ticketadvantage.services.model.TotalRecordEvent;

/**
 * @author jmiller
 *
 */
public class TDSportsEightProcessSite extends TDSportsProcessSite {
	private static final Logger LOGGER = Logger.getLogger(TDSportsEightProcessSite.class);
	private TDSportsEightParser NSP = new TDSportsEightParser();

	/**
	 * 
	 */
	public TDSportsEightProcessSite(String host, String username, String password, boolean isMobile, boolean showRequestResponse) {
		super("TDSportsEight", host, username, password, isMobile, showRequestResponse);
		LOGGER.info("Entering TDSportsEightProcessSite()");

		// Setup the parser
		super.siteParser = super.TDP = NSP;

		// Menu items
		NFL_LINES_SPORT = new String[] { "FOOTBALL GAME LINES" };
		NFL_LINES_NAME = new String[] { "NFL", "NFL PRESEASON" };
		NFL_FIRST_SPORT = new String[] { "FOOTBALL GAME LINES" };
		NFL_FIRST_NAME = new String[] { "NFL - 1ST HALVES" };
		NFL_SECOND_SPORT = new String[] { "FOOTBALL GAME LINES" };
		NFL_SECOND_NAME = new String[] { "NFL - 2ND HALVES" };
		NCAAF_LINES_SPORT = new String[] { "FOOTBALL" };
		NCAAF_LINES_NAME = new String[] { "NCAA FOOTBALL - MEN", "NCAAF FOOTBALL - EXTRA GAMES"};
		NCAAF_FIRST_SPORT = new String[] { "FOOTBALL" };
		NCAAF_FIRST_NAME = new String[] { "NCAA FOOTBALL MEN 1ST HALVES", "NCAAF FOOTBALL - EXTRA GAMES - 1ST HALVES" };
		NCAAF_SECOND_SPORT = new String[] { "FOOTBALL" };
		NCAAF_SECOND_NAME = new String[] { "NCAA FOOTBALL MEN 2ND HALVES", "NCAAF FOOTBALL - EXTRA GAMES - 2ND HALVES" };
		NBA_LINES_SPORT = new String[] { "NBA" };
		NBA_LINES_NAME = new String[] { "NBA" };
		NBA_FIRST_SPORT = new String[] { "NBA" };
		NBA_FIRST_NAME = new String[] { "NBA 1ST HALVES" };
		NBA_SECOND_SPORT = new String[] { "NBA" };
		NBA_SECOND_NAME = new String[] { "NBA 2ND HALF LINES" };
		NCAAB_LINES_SPORT = new String[] { "CBB" };
		NCAAB_LINES_NAME = new String[] { "" };
		NCAAB_FIRST_SPORT = new String[] { "CBB" };
		NCAAB_FIRST_NAME = new String[] { "" };
		NCAAB_SECOND_SPORT = new String[] { "CBB" };
		NCAAB_SECOND_NAME = new String[] { "" };
		NHL_LINES_SPORT = new String[] { "NHL" };
		NHL_LINES_NAME = new String[] { "NATIONAL HOCKEY LEAGUE" };
		NHL_FIRST_SPORT = new String[] { "NHL" };
		NHL_FIRST_NAME = new String[] { "" };
		NHL_SECOND_SPORT = new String[] { "NHL" };
		NHL_SECOND_NAME = new String[] { "" };
		WNBA_LINES_SPORT = new String[] { "NBA" };
		WNBA_LINES_NAME = new String[] { "WNBA" };
		WNBA_FIRST_SPORT = new String[] { "NBA" };
		WNBA_FIRST_NAME = new String[] { "WNBA 1ST HALVES" };
		WNBA_SECOND_SPORT = new String[] { "NBA" };
		WNBA_SECOND_NAME = new String[] { "" };
		MLB_LINES_SPORT = new String[] { "BASEBALL" };
		MLB_LINES_NAME = new String[] { "MAJOR LEAGUE BASEBALL" };
		MLB_FIRST_SPORT = new String[] { "BASEBALL" };
		MLB_FIRST_NAME = new String[] { "MLB 1ST FIVE INNINGS" };
		MLB_SECOND_SPORT = new String[] { "BASEBALL" };
		MLB_SECOND_NAME = new String[] { "MLB 2ND HALF LINES" };

		LOGGER.info("Exiting TDSportsEightProcessSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final String[][] TDSITES = new String [][] { 
//				{ "http://www.ticosports.com", "A2742", "RAY", "500", "500", "500", "New York", "ET"}
//				{ "http://www.ticosports.com", "AE753", "GAME", "500", "500", "500", "None", "ET"}
				{ "http://www.ticosports.com", "Dedicated", "MATTSON", "500", "500", "500", "None", "ET"}
			};

			// Loop through the sites
			for (int i = 0; i < TDSITES.length; i++) {
				final TDSportsEightProcessSite processSite = new TDSportsEightProcessSite(TDSITES[i][0], TDSITES[i][1],
						TDSITES[i][2], false, true);
				final TDSportsEightProcessSite processSite2 = new TDSportsEightProcessSite(TDSITES[i][0], TDSITES[i][1],
						TDSITES[i][2], false, true);
			    processSite.httpClientWrapper.setupHttpClient(TDSITES[0][6]);
			    processSite.processTransaction = true;
			    processSite.timezone = TDSITES[0][7];

				final SpreadRecordEvent sre = new SpreadRecordEvent();
				sre.setSpreadplusminusfirstone("-");
				sre.setSpreadinputfirstone("3.5");
				sre.setSpreadjuiceplusminusfirstone("-");
				sre.setSpreadinputjuicefirstone("110");
				sre.setId(new Long(487));
				sre.setEventname("NCAAF #479 HOUSTON TEXANS +6 (-110) for Game");
				sre.setEventtype("spread");
				sre.setSport("nfllines"); 
				sre.setUserid(new Long(1));
				sre.setEventid(new Integer(487));
				sre.setEventid1(new Integer(487));
				sre.setEventid2(new Integer(488));
				sre.setRotationid(new Integer(487));
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

				final TotalRecordEvent tre = new TotalRecordEvent();
				tre.setTotalinputsecondone("50");
				tre.setTotalinputjuicesecondone("145");
				tre.setTotaljuiceplusminusfirstone("-");
				tre.setEventname("NFL #480 HOUSTON TEXANS vrs NEW ORLEANS SAINTS u50.0 -145.0 for Game");
				tre.setEventtype("total");
				tre.setSport("nfllines"); 
				tre.setUserid(new Long(6));
				tre.setEventid(new Integer(479));
				tre.setEventid1(new Integer(479));
				tre.setEventid2(new Integer(480));
				tre.setRotationid(480);
				tre.setWtype("2");
				tre.setAmount("100");
				tre.setEventdatetime(scal.getTime());
				tre.setDatentime(scal.getTime());
				tre.setEventteam1("HOUSTON TEXANS");
				tre.setEventteam2("NEW ORLEANS SAINTS");
				tre.setGroupid(new Long(-99));
				tre.setIscompleted(false);
				tre.setScrappername("UI");
				tre.setActiontype("Standard");
				tre.setTextnumber("");
				tre.setDatecreated(new Date());
				tre.setDatemodified(new Date());
				tre.setCurrentattempts(0);

				final AccountEvent ae = new AccountEvent();
				ae.setAccountid(new Long(451));
				ae.setAmount("100");
				ae.setActualamount("");
				ae.setAttempts(0);
				ae.setCurrentattempts(0);
				ae.setDatecreated(new Date());
				ae.setDatemodified(new Date());
				ae.setEventdatetime(scal.getTime());
				ae.setEventid(487);
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
				ae.setSpreadindicator("-");
				ae.setSpread(new Float(-3.5));
				ae.setSpreadid(new Long(0));
				ae.setSpreadjuice(new Float(-110));
				ae.setTotal(new Float(50));
				ae.setTotalid(new Long(480));
				ae.setTotalindicator("u");
				ae.setTotaljuice(new Float(50));
				ae.setType("spread");
				ae.setUserid(new Long(1));
				ae.setWagertype("2");
				ae.setStatus("");
				ae.setIscomplexcaptcha(false);
				ae.setHumanspeed(false);

				processSite.processSpreadTransaction(sre, ae);
//				processSite.processTotalTransaction(tre, ae);
/*
			    String xhtml = processSite.loginToSite(TDSITES[i][1], TDSITES[i][2]);

				Set<PendingEvent> pendingEvents = processSite.getPendingBets(TDSITES[0][1], TDSITES[0][2], null);
				if (pendingEvents != null && !pendingEvents.isEmpty()) {
					Iterator<PendingEvent> itr = pendingEvents.iterator();
					while (itr.hasNext()) {
						System.out.println("PendingEvent: " + itr.next());
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
		
		String xhtml = getSite(httpClientWrapper.getHost());
		LOGGER.debug("XHTML: " + xhtml);

		// Parse the data
		MAP_DATA = TDP.parseIndex(xhtml);

		List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
		postValuePairs.add(new BasicNameValuePair("Account", username));
		postValuePairs.add(new BasicNameValuePair("IdBook", MAP_DATA.get("IdBook")));
		postValuePairs.add(new BasicNameValuePair("Password", password.toUpperCase()));

		// Call the login
		xhtml = authenticate(httpClientWrapper.getHost() + "/common/login.aspx", postValuePairs);
		LOGGER.debug("XHTML: " + xhtml);		

		// Parse login
		MAP_DATA = TDP.parseLogin(xhtml);
		LOGGER.debug("MAP_DATA: " + MAP_DATA);

		// Set the webapp to common
		httpClientWrapper.setWebappname("common/wager");

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