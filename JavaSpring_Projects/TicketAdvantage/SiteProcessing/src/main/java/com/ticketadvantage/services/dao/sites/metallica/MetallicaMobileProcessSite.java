/**
 * 
 */
package com.ticketadvantage.services.dao.sites.metallica;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.sites.SiteProcessor;
import com.ticketadvantage.services.dao.sites.SiteTransaction;
import com.ticketadvantage.services.dao.sites.metallica.captcha.CaptchaReader;
import com.ticketadvantage.services.email.ticketadvantage.TicketAdvantageGmailOath;
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
public class MetallicaMobileProcessSite extends SiteProcessor {
	private static final Logger LOGGER = Logger.getLogger(MetallicaMobileProcessSite.class);
	private static final String MOBILE_HOST = "api.cog.cr";
	private static final String MOBILE_HOST_URL = "https://api.cog.cr";
	private final MetallicaMobileParser MP = new MetallicaMobileParser();
	private String VERSION = "1.3.28";
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
	public MetallicaMobileProcessSite(String host, String username, String password, boolean isMobile, boolean showRequestResponse) {
		super("MetallicaMobile", host, username, password, isMobile, showRequestResponse);
		LOGGER.info("Entering MetallicaMobileProcessSite()");

		// Set the parser
		this.siteParser = MP;

		// Setup the menu items
		NFL_LINES_SPORT = new String[] { "Football", "NFL" };
		NFL_LINES_NAME = new String[] { "Game", "NFL - Pre Season", "NFL" };
		NFL_FIRST_SPORT = new String[] { "Football", "NFL" };
		NFL_FIRST_NAME = new String[] { "1st Half", "" };
		NFL_SECOND_SPORT = new String[] { "2nd Halves", "2nd Halves" };
		NFL_SECOND_NAME = new String[] { "2nd Half", "NFL", "NFL - 2nd Half", "NFL 2nd Half", "NFL - Pre Season 2nd Half" };
		NCAAF_LINES_SPORT = new String[] { "Football", "NCAA Football" };
		NCAAF_LINES_NAME = new String[] { "Game", "College Football", "College Football - Extra & FCS", "College Football Extra", "College - Bowls", "College - Playoffs", "College Football - Playoffs", "College Football - Bowls", "College Football - FCS Playoffs" };
		NCAAF_FIRST_SPORT = new String[] { "Football", "NCAA Football" };
		NCAAF_FIRST_NAME = new String[] { "1st Half", "College Football Extra 1st Half", "College Football", "College Football - Extra & FCS", "College Football Extra", "College - Bowls", "College - Playoffs", "College Football - Playoffs", "College Football - Bowls", "College Football - FCS Playoffs" };
		NCAAF_SECOND_SPORT = new String[] { "2nd Halves", "2nd Halves" };
		NCAAF_SECOND_NAME = new String[] { "2nd Half", "College Football", "College Football Extra", "College Football Extra 2nd Half", "College Football - Extra & FCS", "College Football Extra", "College - Bowls", "College - Playoffs", "College Football - Playoffs", "College Football - Bowls", "College Football - FCS Playoffs" };
		NBA_LINES_SPORT = new String[] { "Basketball", "NBA" };
		NBA_LINES_NAME = new String[] { "Game", "NBA", "NBA Playoffs", "NBA - Playoffs", "NBA - Preseason", "NBA - Summer League & Big 3" };
		NBA_FIRST_SPORT = new String[] { "Basketball", "NBA" };
		NBA_FIRST_NAME = new String[] { "1st Half", "NBA", "NBA Playoffs", "NBA - Playoffs", "NBA - Preseason", "NBA - Summer League & Big 3" };
		NBA_SECOND_SPORT = new String[] { "2nd Halves", "2nd Halves" };
		NBA_SECOND_NAME = new String[] { "2nd Half", "NBA", "NBA Playoffs", "NBA - Playoffs", "NBA - Preseason", "NBA - Summer League & Big 3" };
		NCAAB_LINES_SPORT = new String[] { "Basketball", "NCAA Basketball" };
		NCAAB_LINES_NAME = new String[] { "Game", "NCAA Basketball", "NCAA Basketball - Added", "NCAA Basketball - Added & Extra", "NCAA Basketball - Extra", "NCAA - Minor Tournaments", "NCAA - March Madness", "NIT Tournament", "March Madness", "Conference Tournaments", "Added Tournaments", "Extra Tournaments", "CIT Tournament", "CBI Tournament", "NIT & CBI & CIT" };
		NCAAB_FIRST_SPORT = new String[] { "Basketball", "NCAA Basketball" };
		NCAAB_FIRST_NAME = new String[] { "1st Half", "NCAA Basketball", "NCAA Basketball - Added", "NCAA Basketball - Added & Extra", "NCAA Basketball - Extra", "NCAA - Minor Tournaments", "NCAA Basketball Extra 1st Half", "Added Tournaments - 1st Half", "NCAA - March Madness", "NIT Tournament", "March Madness", "Conference Tournaments - 1st Half", "Added Tournaments", "Extra Tournaments", "CIT Tournament", "CBI Tournament", "NIT & CBI & CIT" };
		NCAAB_SECOND_SPORT = new String[] { "2nd Halves", "2nd Halves" };
		NCAAB_SECOND_NAME = new String[] { "2nd Half", "NCAA Basketball", "NCAA Basketball - Added", "NCAA Basketball - Added & Extra", "NCAA Basketball - Extra", "NCAA - Minor Tournaments", "NCAA Basketball Extra 2nd Half", "Added Tournaments - 2nd Half", "NCAA - March Madness", "NIT Tournament", "March Madness", "Conference Tournaments - 2nd Half", "Added Tournaments", "Extra Tournaments", "CIT Tournament", "CBI Tournament", "NIT & CBI & CIT" };
		NHL_LINES_SPORT = new String[] { "Hockey", "NHL" };
		NHL_LINES_NAME = new String[] { "Game", "NHL", "NHL Playoffs", "NHL - Playoffs"};
		NHL_FIRST_SPORT = new String[] { "Hockey", "NHL" };
		NHL_FIRST_NAME = new String[] { "1st Period", "NHL", "NHL Playoffs", "NHL - Playoffs" };
		NHL_SECOND_SPORT = new String[] { "NHL", "2nd Halves (click below)" };
		NHL_SECOND_NAME = new String[] { "NHL Hockey - 2nd Period", "NHL Playoffs", "NHL Hockey - Extra - 2nd Period", "NHL Hockey Added - 2nd Period" };
		WNBA_LINES_SPORT = new String[] { "Basketball", "WNBA" };
		WNBA_LINES_NAME = new String[] { "Game", "WNBA" };
		WNBA_FIRST_SPORT = new String[] { "Basketball", "WNBA" };
		WNBA_FIRST_NAME = new String[] { "1st Half", "" };
		WNBA_SECOND_SPORT = new String[] { "2nd Halves", "2nd Halves" };
		WNBA_SECOND_NAME = new String[] { "2nd Half", "WNBA" };
		MLB_LINES_SPORT = new String[] { "Baseball", "MLB" };
		MLB_LINES_NAME = new String[] { "Game", "MLB", "MLB - Exhibition" };
		MLB_FIRST_SPORT = new String[] { "Baseball", "MLB" };
		MLB_FIRST_NAME = new String[] { "1st 5 Innings", "MLB", "MLB - Exhibition" };
		MLB_SECOND_SPORT = new String[] { "2nd Halves", "2nd Halves" };
		MLB_SECOND_NAME = new String[] { "2nd Half", "MLB", "MLB - Exhibition" };
		INTERNATIONAL_BASEBALL_LINES_SPORT = new String[] { "Baseball", "International Baseball" };
		INTERNATIONAL_BASEBALL_LINES_NAME = new String[] { "Game", "Japan Baseball", "Korean Baseball" };
		INTERNATIONAL_BASEBALL_FIRST_SPORT = new String[] { "Baseball", "International Baseball" };
		INTERNATIONAL_BASEBALL_FIRST_NAME = new String[] { "1st 5 Innings", "Japan Baseball", "Korean Baseball" };
		INTERNATIONAL_BASEBALL_SECOND_SPORT = new String[] { "2nd Halves", "2nd Halves" };
		INTERNATIONAL_BASEBALL_SECOND_NAME = new String[] { "2nd Half", "International Baseball" };

		LOGGER.info("Exiting MetallicaProcessSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			final String[][] MSITES = new String[][] {
				// {"http://www.Betwindycity.com", "w102", "Phillies","500", "500", "500", "Baltimore", "PT"},
				// { "http://www.Sunwager.com", "4488", "John", "300", "300", "300", "Asheville", "ET" },
				// {"http://www.greenisle.club", "Go508", "Iowa", "500","250", "300", "Dallas", "ET" },
				// {"http://www.Scotch88.com", "kwd2202", "jh", "500", "500", "500", "Dallas", "CT"}
//				{"http://www.madmax.ag", "820", "black2020", "500", "500", "500", "Dallas", "CT"}
//				{"http://www.madmax.ag", "834", "sky1212", "500", "500", "500", "Dallas", "CT"}
			// {"http://www.BetWC.ag", "Wc24006", "Demo", "500", "500", "500", "Dallas", "CT"}
//				{"https://iron69.com", "kwd2202", "kh", "500", "500", "500", "None", "PT"}
//				{"https://globalsides.com", "kent35", "kent35", "100", "100", "100", "Dallas", "CT"}
//				{"https://silver333.com", "W41070", "BG63", "500", "500", "500", "Baltimore", "ET"}
				//{"https://sunwager.com", "294", "FOOTBALL", "500", "500", "500", "None", "CT"}
//				{"https://690sports.com", "POP407", "ZT05", "500", "500", "500", "None", "ET"}
				{"https://betwc.ag", "WC9313", "3id39d", "50", "50", "50", "None", "PT"}
			};

			final MetallicaMobileProcessSite processSite = new MetallicaMobileProcessSite(MSITES[0][0], MSITES[0][1],
					MSITES[0][2], true, true);
	
		    processSite.httpClientWrapper.setupHttpClient(MSITES[0][6]);
		    processSite.processTransaction = false;
		    processSite.timezone = MSITES[0][7];
		    processSite.userid = new Long(1);
		    String xhtml= processSite.loginToSite(MSITES[0][1], MSITES[0][2]);
		    LOGGER.debug("xhtml: " + xhtml);
		    
			final MlRecordEvent mre = new MlRecordEvent();
			mre.setMlplusminussecondone("+");
			mre.setMlinputsecondone("121");
			mre.setId(new Long(520));
			mre.setEventname("MLB #918 Kansas City Royals +121 for Game");
			mre.setEventtype("ml");
			mre.setSport("mlblines"); 
			mre.setUserid(new Long(1));
			mre.setEventid(new Integer(917));
			mre.setEventid1(new Integer(917));
			mre.setEventid2(new Integer(918));
			mre.setRotationid(new Integer(918));
			mre.setWtype("2");
			mre.setAmount("50");
			final Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
			cal.set(Calendar.HOUR_OF_DAY, 12);
			cal.set(Calendar.MINUTE, 15);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MONTH, 3);
			cal.set(Calendar.DAY_OF_MONTH, 11);
			cal.set(Calendar.YEAR, 2019);
			mre.setEventdatetime(cal.getTime());
			mre.setDatentime(cal.getTime());
			mre.setEventteam1("");
			mre.setEventteam2("Kansas City Royals");
			mre.setGroupid(new Long(-99));
			mre.setIscompleted(false);
			mre.setScrappername("UI");
			mre.setActiontype("Standard");
			mre.setTextnumber("");
			mre.setDatecreated(new Date());
			mre.setDatemodified(new Date());
			mre.setCurrentattempts(0);

			final SpreadRecordEvent sre = new SpreadRecordEvent();
			sre.setSpreadplusminussecondone("-");
			sre.setSpreadinputsecondone("1.5");
			sre.setSpreadjuiceplusminussecondone("+");
			sre.setSpreadinputjuicesecondone("120");
			sre.setId(new Long(520));
			sre.setEventname("MLB #904 Houston Astros -1½ (+120) for Game");
			sre.setEventtype("spread");
			sre.setSport("mlblines"); 
			sre.setUserid(new Long(1));
			sre.setEventid(new Integer(903));
			sre.setEventid1(new Integer(903));
			sre.setEventid2(new Integer(904));
			sre.setRotationid(new Integer(904));
			sre.setWtype("2");
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
			sre.setEventteam2("Houston Astros");
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
			ae.setAmount("50");
			ae.setActualamount("");
			ae.setAttempts(0);
			ae.setCurrentattempts(0);
			ae.setDatecreated(new Date());
			ae.setDatemodified(new Date());
			ae.setEventdatetime(scal.getTime());
			ae.setEventid(918);
			ae.setEventname("MLB #918 Kansas City Royals +121 for Game");
			ae.setGroupid(new Long(-99));
			ae.setIscompleted(false);
			ae.setMaxspreadamount(50);
			ae.setMaxtotalamount(50);
			ae.setMaxmlamount(50);
			ae.setMlindicator("-");
			ae.setTimezone("PT");
			ae.setSpread(new Float(-1.5));
			ae.setSpreadid(new Long(964));
			ae.setSpreadindicator("+");
			ae.setSpreadjuice(new Float(120));
			ae.setTotal(new Float(0));
			ae.setTotalid(new Long(0));
			ae.setTotalindicator("");
			ae.setTotaljuice(new Float(0));
			ae.setMlindicator("+");
			ae.setMl(new Float(121));
			ae.setMlid(new Long(964));
			ae.setMljuice(new Float(121));
			ae.setName("690Sports-XXX");
			ae.setOwnerpercentage(100);
			ae.setPartnerpercentage(0);
			ae.setProxy("None");
			ae.setSport("mlblines");
			ae.setType("spread");
			ae.setUserid(new Long(1));
			ae.setWagertype("2");
			ae.setStatus("");
			ae.setIscomplexcaptcha(false);
			ae.setHumanspeed(false);
			
			processSite.processMlTransaction(mre, ae);
//			processSite.processSpreadTransaction(sre, ae);

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
			mre.setEventid(new Integer(305));
			mre.setEventid1(new Integer(305));
			mre.setEventid2(new Integer(306));
			mre.setRotationid(new Integer(306));
			mre.setWtype("2");
			mre.setAmount("100");

			// Step 9 - Find event
			EventPackage eventPackage = processSite.findEvent(ep, mre);
			if (eventPackage == null) {
				throw new BatchException(BatchErrorCodes.GAME_NOT_AVAILABLE, BatchErrorMessage.GAME_NOT_AVAILABLE, "Game cannot be found on site for " + mre.getEventname(), xhtml);
			}
			
			boolean check = false;
			while(!check) {
				SiteTransaction siteTransaction = new SiteTransaction();
				AccountEvent ae = new AccountEvent();
				ae.setMaxmlamount(new Integer(500));
				
				xhtml = processSite.selectEvent(siteTransaction, eventPackage, mre, ae);
				LOGGER.debug("xhtml: " + xhtml);
				
				//JsonResponse
				JSONObject jsonResponse = new JSONObject(xhtml);
				String captchaImage = jsonResponse.get("CaptchaImage").toString();
				
				//get Captcha String
				LOGGER.error("Captcha Image String: " + captchaImage);
				
				//Process captcha String
				CaptchaReader obj = new CaptchaReader();
				captchaStr = obj.captchaProcess(captchaImage);
				LOGGER.error("Captcha Text: " + captchaStr);

				// Clean up text if necessary
				check=Pattern.matches("[a-zA-Z0-9]{6}",captchaStr);

				if (check) {
					processSite.parseEventSelection(xhtml, siteTransaction, eventPackage, mre, ae);
				}
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
			 * Map<String, String> map = processSite.getMenuFromSite("ncaaflines", xhtml);
			 * LOGGER.debug("map: " + map);
			 * 
			 * xhtml = processSite.selectSport("ncaaflines"); LOGGER.debug("XHTML: " +
			 * xhtml);
			 * 
			 * final Iterator<EventPackage> ep = processSite.parseGamesOnSite("ncaaflines",
			 * xhtml);
			 */

/*
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
*/
			/*
			 * final SpreadRecordEvent mre = new SpreadRecordEvent();
			 * mre.setSpreadjuiceplusminussecondone("-");
			 * mre.setSpreadinputjuicesecondone("110");
			 * mre.setSpreadplusminussecondone("-"); mre.setSpreadinputsecondone("12.5");
			 * mre.setId(new Long(110));
			 * mre.setEventname("NCAAF #278 GEORGIA -12.5 -120.0 for Game");
			 * mre.setEventtype("spread"); mre.setSport("ncaaflines"); mre.setUserid(new
			 * Long(6)); mre.setEventid(new Integer(277)); mre.setEventid1(new
			 * Integer(277)); mre.setEventid2(new Integer(278)); mre.setRotationid(new
			 * Integer(278)); mre.setWtype("2"); mre.setAmount("100");
			 * 
			 * // Step 9 - Find event EventPackage eventPackage = processSite.findEvent(ep,
			 * mre); if (eventPackage == null) { throw new
			 * BatchException(BatchErrorCodes.GAME_NOT_AVAILABLE,
			 * BatchErrorMessage.GAME_NOT_AVAILABLE, "Game cannot be found on site for " +
			 * mre.getEventname(), xhtml); }
			 */

/*
			boolean check = false;
			while(!check) {
			SiteTransaction siteTransaction = new SiteTransaction();
			AccountEvent ae = new AccountEvent();
			ae.setMaxmlamount(new Integer(500));
			xhtml = processSite.selectEvent(siteTransaction, eventPackage, mre, ae);
			LOGGER.debug("xhtml: " + xhtml);
			
			//JsonResponse
			JSONObject jsonResponse = new JSONObject(xhtml);
			String captchaImage = jsonResponse.get("CaptchaImage").toString();
			
			//get Captcha String
			System.out.println("Captcha Image String: "+ captchaImage);
			LOGGER.debug("Captcha Image String: " + captchaImage);
			
			//Process captcha String
			CaptchaReader obj = new CaptchaReader();
			captchaStr = obj.captchaProcess(captchaImage);
			System.out.println("Captcha Text: "+captchaStr);
			LOGGER.debug("Captcha Text: " + captchaStr);
			
			check=Pattern.matches("[a-zA-Z0-9]{6}",captchaStr);
			
			if(check)
				processSite.parseEventSelection(xhtml, siteTransaction, eventPackage, mre, ae);
			}*/
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
		MP.setTimezone(timezone);
		
		// Get the site
		List<NameValuePair> headerValuePairs = new ArrayList<NameValuePair>(3);
		final String originalHost = this.httpClientWrapper.getHost();

		// HTTP Header Post
		headerValuePairs = new ArrayList<NameValuePair>(3);
		headerValuePairs.add(new BasicNameValuePair("Host", MOBILE_HOST));
		headerValuePairs.add(new BasicNameValuePair("Referer", this.httpClientWrapper.getHost() + "/"));

		// Setup login POST JSON
		final List<NameValuePair> postNameValuePairs = new ArrayList<NameValuePair>(1);
		postNameValuePairs.add(new BasicNameValuePair("customerid", username));
		postNameValuePairs.add(new BasicNameValuePair("password", password));
		postNameValuePairs.add(new BasicNameValuePair("submit", ""));

		// Login
		List<NameValuePair> retValue = postSitePageNoRedirect(MOBILE_HOST_URL + "/identity/CustomerLoginRedir", null, postNameValuePairs, headerValuePairs);

		String location = "";
		String xhtml = "";
				
		// Get all new cookies and the XHTML for website
		if (retValue != null && !retValue.isEmpty()) {
			final Iterator<NameValuePair> itr = retValue.iterator();
			while (itr != null && itr.hasNext()) {
				final NameValuePair nvp = (NameValuePair) itr.next();
				LOGGER.info("Header Name: " + nvp.getName());
				if ("Location".equals(nvp.getName())) {
					location = nvp.getValue();
				} else if ("json".equals(nvp.getName())) {
					xhtml = nvp.getValue();
				}
			}
		}

		String token = "";
		if (location != null && location.length() > 0) {
			// http://www.betwc.ag/cog/index.html#/redir?t=6B256405-46DB-46A0-92CB-E8A162C1C7D7
			int index = location.indexOf("redir?t=");
			if (index != -1) {
				token = location.substring(index + "redir?t=".length());
			}
		}
		LOGGER.debug("LoCaTiOn: " + location);
		LOGGER.debug("token: " + token);

		headerValuePairs = new ArrayList<NameValuePair>(3);
		String hostName = this.httpClientWrapper.getHost().replace("http://", "").replace("https://", "");
		headerValuePairs.add(new BasicNameValuePair("Host", hostName));
		headerValuePairs.add(new BasicNameValuePair("Referer", this.httpClientWrapper.getHost() + "/"));
		headerValuePairs.add(new BasicNameValuePair("If-Modified-Since", "Fri, 23 Mar 2018 16:34:55 GMT"));
		headerValuePairs.add(new BasicNameValuePair("If-None-Match", "W/\"5ab52caf-37f\""));
		headerValuePairs.add(new BasicNameValuePair("Upgrade-Insecure-Requests", "1"));
		headerValuePairs.add(new BasicNameValuePair("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.13; rv:50.0) Gecko/20100101 Firefox/50.0"));

		// Call the redirct
		retValue = super.httpClientWrapper.getSitePageNoRedirect(location, null, headerValuePairs);
		xhtml = "";

		// Get all new cookies and the XHTML for website
		if (retValue != null && !retValue.isEmpty()) {
			final Iterator<NameValuePair> itr = retValue.iterator();
			while (itr != null && itr.hasNext()) {
				final NameValuePair nvp = (NameValuePair) itr.next();
				LOGGER.info("Header Name: " + nvp.getName());
				if ("Location".equals(nvp.getName())) {
					location = nvp.getValue();
				} else if ("xhtml".equals(nvp.getName())) {
					xhtml = nvp.getValue();
					LOGGER.debug("xhtml: " + xhtml);
				}
			}
		}
		LOGGER.debug("LoCaTiOn: " + location);
		LOGGER.debug("token: " + token);
		
		// Call the redirct
		retValue = super.httpClientWrapper.getSitePageNoRedirect(location, null, headerValuePairs);
		xhtml = "";

		// Get all new cookies and the XHTML for website
		if (retValue != null && !retValue.isEmpty()) {
			final Iterator<NameValuePair> itr = retValue.iterator();
			while (itr != null && itr.hasNext()) {
				final NameValuePair nvp = (NameValuePair) itr.next();
				LOGGER.info("Header Name: " + nvp.getName());
				if ("Location".equals(nvp.getName())) {
					location = nvp.getValue();
					// Call the redirct
					retValue = super.httpClientWrapper.getSitePageNoRedirect(location, null, headerValuePairs);
					xhtml = "";

					// Get all new cookies and the XHTML for website
					if (retValue != null && !retValue.isEmpty()) {
						final Iterator<NameValuePair> itr2 = retValue.iterator();
						while (itr2 != null && itr2.hasNext()) {
							final NameValuePair nvp2 = (NameValuePair) itr2.next();
							LOGGER.info("Header Name: " + nvp2.getName());
							if ("Location".equals(nvp2.getName())) {
								location = nvp2.getValue();
							} else if ("xhtml".equals(nvp2.getName())) {
								xhtml = nvp2.getValue();
								LOGGER.debug("xhtml: " + xhtml);
							}
						}
					}
				} else if ("xhtml".equals(nvp.getName())) {
					xhtml = nvp.getValue();
					LOGGER.debug("xhtml: " + xhtml);
				}
			}
		}

		if (xhtml != null) {
			int index = xhtml.indexOf("https://cdn.cog.cr/scripts/p_app_");
			if (index != -1) {
				xhtml = xhtml.substring(index + "https://cdn.cog.cr/scripts/p_app_".length());
				LOGGER.debug("Script Name: " + xhtml);
				index = xhtml.indexOf(".js");
				if (index != -1) {
					this.httpClientWrapper.setHost("cdn.cog.cr");
					xhtml = xhtml.substring(0, index);
					// this.httpClientWrapper.setHost("https://static.cog.cr");
					// static.cog.cr
					// HTTP Header Post
					headerValuePairs = new ArrayList<NameValuePair>(3);
					headerValuePairs.add(new BasicNameValuePair("Host", "cdn.cog.cr"));
					headerValuePairs.add(new BasicNameValuePair("Referer", originalHost + "/cog/index.html"));
					xhtml = getJSSiteNoBr("https://cdn.cog.cr/scripts/p_app_" + xhtml + ".js", headerValuePairs);
					LOGGER.debug("Script Name2: " + xhtml);
					if (xhtml != null && xhtml.length() > 0) {
						index = xhtml.indexOf("JS_VERSION:\"");
						if (index != -1) {
							xhtml = xhtml.substring(index + "JS_VERSION:\"".length());
							LOGGER.debug("JS_VERSION: " + xhtml);
							index = xhtml.indexOf("\",");
							if (index != -1) {
								xhtml = xhtml.substring(0, index);
								LOGGER.debug("VERSION: " + xhtml);
								if (xhtml != null && xhtml.length() > 0) {
									VERSION = xhtml;
								}
							}
						}
					}
				}
			}
		}

		this.httpClientWrapper.setHost(originalHost);
		StringBuffer postObj = new StringBuffer(200);
		postObj.append("{\"token\":\"").append(token);
		postObj.append("\",\"version\":\"").append(VERSION);
		postObj.append("\"}");

		headerValuePairs = new ArrayList<NameValuePair>(3);
		headerValuePairs.add(new BasicNameValuePair("Host", "api.cog.cr"));
		headerValuePairs.add(new BasicNameValuePair("Referer", this.httpClientWrapper.getHost() + "/cog/index.html"));
		String json = postJSONSite(MOBILE_HOST_URL + "/identity/customerLoginFromToken", postObj.toString(), headerValuePairs);
		MAP_DATA = MP.parseLogin(json);

		// Now pass in auth token
		authorizationToken = MAP_DATA.get("AccessToken");
		headerValuePairs.add(new BasicNameValuePair("Authorization", "Bearer " + authorizationToken));

		// Get the menu
		json = getJSONSite(MOBILE_HOST_URL + "/api/wager/sportsavailablebyplayeronleague/false", headerValuePairs);

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

		// HTTP Header Post
		final List<NameValuePair> headerValuePairs = new ArrayList<NameValuePair>(4);
		headerValuePairs.add(new BasicNameValuePair("Host", MOBILE_HOST));
		headerValuePairs.add(new BasicNameValuePair("Origin", this.httpClientWrapper.getHost()));
		headerValuePairs.add(new BasicNameValuePair("Referer", this.httpClientWrapper.getHost()));
		headerValuePairs.add(new BasicNameValuePair("Authorization", "Bearer " + authorizationToken));

		// Setup schedules POST JSON
		final StringBuffer postObj = new StringBuffer(100);
		postObj.append("[");
		Set<Entry<String, String>> entries = MAP_DATA.entrySet();
		Iterator<Entry<String, String>> itr = entries.iterator();
		int size = entries.size();
		int counter = 0;
		while (itr.hasNext()) {
			counter++;
			Entry<String, String> entry = itr.next();
			String key = entry.getKey();
			if (key.startsWith("sport")) {
				if (size == counter) {
					postObj.append(entry.getValue());
				} else {
					postObj.append(entry.getValue() + ",");
				}
			}
		}
		postObj.append("]");

		// HTTP Post
		final String json = postJSONSite(MOBILE_HOST_URL + "/api/wager/schedules/S/0", postObj.toString(), headerValuePairs);

		LOGGER.info("Exiting selectSport()");
		return json;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#createSiteTransaction()
	 */
	@Override
	protected SiteTransaction createSiteTransaction() {
		LOGGER.info("Entering createSiteTransaction()");

		final MetallicaTransaction metallicaTransaction = new MetallicaTransaction();

		LOGGER.info("Exiting createSiteTransaction()");
		return metallicaTransaction;
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

		// Reset the $$ if we have to based on risk vs win
		String siteAmount = determineRiskWinAmounts(siteTransaction, eventPackage, event, ae); 
		LOGGER.error("siteAmount: " + siteAmount);
		siteTransaction.setAmount(siteAmount);
		ae.setActualamount(siteAmount);

		if (event instanceof SpreadRecordEvent || event instanceof TotalRecordEvent) {
			if (siteTransaction.getOptionValue() != null) {
				optionValue = siteTransaction.getOptionValue();
			}
		} else {
			optionValue = siteTransaction.getInputValue();	
		}

		// HTTP Post
		String json = addBet(siteAmount);
		
		// Access Token
		accessToken = getAccessToken();
		ae.setAccesstoken(accessToken);

		if (json != null && json.contains("Your maximum wager is")) {
			// Parse the event selection
			try {
				LOGGER.error("Max wager time limit");
				MAP_DATA = MP.parseEventSelection(json, null, null);
			} catch (BatchException be) {
				if (be.getErrorcode() == BatchErrorCodes.MAX_WAGER_UNTIL_TIME) {
					String amnt = be.getBexception().replace(",", "");
					siteTransaction.setAmount(amnt);
					ae.setActualamount(amnt);

					// HTTP Post
					json = addBet(amnt);

					// Determine the type
					if ("spread".equals(ae.getType())) {
						float dFactor = determineFactor(ae.getSpreadjuice().floatValue());
						determineRiskWin(amnt, dFactor, ae);
					} else if ("total".equals(ae.getType())) {
						float dFactor = determineFactor(ae.getTotaljuice().floatValue());
						determineRiskWin(amnt, dFactor, ae);
					} else if ("ml".equals(ae.getType())) {
						float dFactor = determineFactor(ae.getMljuice().floatValue());
						determineRiskWin(amnt, dFactor, ae);
					}
				}
			}
		}

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
			LOGGER.error("siteTransaction.getAmount(): " + siteTransaction.getAmount());
			wagers = MP.parseEventSelection(xhtml, null, null);
			LOGGER.debug("wagers: " + wagers);
		} catch (BatchException be) {
			if (be.getErrorcode() == BatchErrorCodes.LINE_CHANGED_ERROR) {
				xhtml = processLineChange("AddBet", xhtml, event, ae);
				wagers = MP.parseEventSelection(xhtml, null, null);
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

				wagers = MP.parseEventSelection(xhtml, null, null);
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

		String captchadata = null;
		LOGGER.error("CaptchaImage: " + wagers.containsKey("CaptchaImage"));

		if (wagers.containsKey("CaptchaImage") && wagers.get("CaptchaImage") != null && wagers.get("CaptchaImage").length() > 0) {
			//value set by new code
			captchadata = processCaptcha(wagers.get("CaptchaImage"));
			if (!Pattern.matches("[a-zA-Z0-9]{6}",captchadata)) {
				throw new BatchException(BatchErrorCodes.RETRY_CAPTCHA, BatchErrorMessage.RETRY_CAPTCHA, captchadata, xhtml);
			}
			
			LOGGER.error("captchadata: " + captchadata);
			if (captchadata == null || captchadata.length() == 0) {
				// Throw an exception
				throw new BatchException(BatchErrorCodes.CAPTCHA_PROCESSING_EXCEPTION, BatchErrorMessage.CAPTCHA_PROCESSING_EXCEPTION, "Problem getting captcha text", xhtml);
			} else {
				wagers.put("CaptchaMessage", captchadata);
			}
		}

		String delay = wagers.get("DelayUntilUTC");
		if (delay != null && delay.length() > 0) {
			final DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_DATE_TIME;
		    final TemporalAccessor accessor = timeFormatter.parse(delay);	
		    final Date date = Date.from(Instant.from(accessor));
		    final Calendar delayDate = Calendar.getInstance();
		    delayDate.setTime(date);
		    final Calendar now = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		    long duration = delayDate.getTime().getTime() - now.getTime().getTime();
		    
		    try {
		    	if (duration > 0) {
		    		Thread.sleep(duration);
		    	}
			} catch (InterruptedException e) {
				LOGGER.error("InterruptedException");
				// good practice
				Thread.currentThread().interrupt();
			}
		}

		// Now set the MAP_DATA to what was parsed
		MAP_DATA = wagers;

		LOGGER.error("siteTransaction.getAmount(): " + siteTransaction.getAmount());
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
		
		LOGGER.error("siteTransaction.getAmount(): " + siteTransaction.getAmount());

		// Actually process the transaction?
		if (processTransaction) {
			// Save bet
			json = saveBet(siteTransaction.getAmount());

			try {
				MP.parseTicketNumber(json);
			} catch (BatchException be) {
				if (be.getErrorcode() == BatchErrorCodes.LINE_CHANGED_ERROR) {
					json = processLineChange("SaveBet", json, event, ae);
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
				ticketNumber = MP.parseTicketNumber(xhtml);
				LOGGER.debug("ticketNumber: " + ticketNumber);
			} catch (BatchException be) {
				throw be;
			}

			// HTTP Header Post
			final List<NameValuePair> headerValuePairs = new ArrayList<NameValuePair>(4);
			headerValuePairs.add(new BasicNameValuePair("Host", MOBILE_HOST));
			headerValuePairs.add(new BasicNameValuePair("Origin", this.httpClientWrapper.getHost()));
			headerValuePairs.add(new BasicNameValuePair("Referer", this.httpClientWrapper.getHost()));
			headerValuePairs.add(new BasicNameValuePair("Authorization", "Bearer " + authorizationToken));

			// Setup SaveBet POST JSON
			final StringBuffer postObj = new StringBuffer(50);
			postObj.append("{\"TicketNumber\":").append(ticketNumber);
			postObj.append("}");

			// HTTP Post
			final String json = postJSONSite(MOBILE_HOST_URL + "/api/wager/confirmBet", postObj.toString(), headerValuePairs);

			// Validate confirmation
			MP.parseConfirmation(json);
		}

		LOGGER.info("Exiting parseTicketNumber()");
		return ticketNumber;		
	}
	
	/**
	 * 
	 * @param juice
	 * @return
	 */
	private float determineFactor(float juice) {
		float factor = 1;
		if (juice < 0) {
			factor = 100/juice;
		} else {
			factor = juice/100;
		}
		return factor;
	}

	/**
	 * 
	 * @param amnt
	 * @param dFactor
	 * @param ae
	 */
	private void determineRiskWin(String amnt, float dFactor, AccountEvent ae) {
		LOGGER.info("Entering determineRiskWin()");
		final Double damount = new Double(amnt);

		if (dFactor >= 1) {
			final Double data = damount.doubleValue() * dFactor;
			ae.setRiskamount(amnt);
			ae.setTowinamount(data.toString());
		} else {
			final Double data = damount.doubleValue()/dFactor;
			ae.setTowinamount(amnt);
			ae.setRiskamount(data.toString());
		}

		LOGGER.info("Exiting determineRiskWin()");
	}

	/**
	 * 
	 * @param siteAmount
	 * @return
	 * @throws BatchException
	 */
	private String addBet(String siteAmount) throws BatchException {
		LOGGER.info("Entering addBet()");

		// HTTP POST Options
		final List<NameValuePair> headerValuePairs = new ArrayList<NameValuePair>(4);
		headerValuePairs.add(new BasicNameValuePair("Host", MOBILE_HOST));
		headerValuePairs.add(new BasicNameValuePair("Origin", this.httpClientWrapper.getHost()));
		headerValuePairs.add(new BasicNameValuePair("Referer", this.httpClientWrapper.getHost()));
		headerValuePairs.add(new BasicNameValuePair("Authorization", "Bearer " + authorizationToken));

		// Setup AddBet POST JSON
		StringBuffer postObj = new StringBuffer(200);
		postObj.append("[{\"BetType\":\"S\",\"TotalPicks\":1,\"IdTeaser\":0,\"IsFreePlay\":false,\"Amount\":")
				.append(siteAmount);
		postObj.append(",\"RoundRobinOptions\":[],\"Wagers\":[{\"Id\":\"").append(optionValue);
		postObj.append(
				"\",\"PitcherVisitor\":false,\"PitcherHome\":false}],\"AmountCalculation\":\"A\",\"ContinueOnPush\":true}]");

		// HTTP Post
		String json = postJSONSite(MOBILE_HOST_URL + "/api/wager/AddBet", postObj.toString(), headerValuePairs);
		
		LOGGER.info("Exiting addBet()");
		return json;
	}

	/**
	 * 
	 * @param siteAmount
	 * @return
	 * @throws BatchException
	 */
	private String saveBet(String siteAmount) throws BatchException {
		LOGGER.info("Entering saveBet()");

		// HTTP Header Post
		final List<NameValuePair> headerValuePairs = new ArrayList<NameValuePair>(4);
		headerValuePairs.add(new BasicNameValuePair("Host", MOBILE_HOST));
		headerValuePairs.add(new BasicNameValuePair("Origin", this.httpClientWrapper.getHost()));
		headerValuePairs.add(new BasicNameValuePair("Referer", this.httpClientWrapper.getHost()));
		headerValuePairs.add(new BasicNameValuePair("Authorization", "Bearer " + authorizationToken));

		// Setup SaveBet POST JSON
		final StringBuffer postObj = new StringBuffer(200);
		postObj.append(
				"{\"Details\":[{\"BetType\":\"S\",\"TotalPicks\":1,\"IdTeaser\":0,\"IsFreePlay\":false,\"Amount\":")
				.append(siteAmount);
		postObj.append(",\"RoundRobinOptions\":[],\"Wagers\":[{\"Id\":\"").append(optionValue);
		postObj.append(
				"\",\"PitcherVisitor\":false,\"PitcherHome\":false}],\"AmountCalculation\":\"A\",\"ContinueOnPush\":true}],\"DelayUntilSignature\":\"")
				.append(MAP_DATA.get("DelayUntilSignature"));
		postObj.append("\",\"DelayUntilUTC\":\"").append(MAP_DATA.get("DelayUntilUTC"));
		postObj.append("\",\"CaptchaMessage\":\"").append(MAP_DATA.get("CaptchaMessage"));
		final String needConfirmation = MAP_DATA.get("NeedConfirmation");
		LOGGER.error("needConfirmation: " + needConfirmation);
		if (needConfirmation != null && "true".equals(needConfirmation)) {
			postObj.append("\",\"PasswordConfirmation\":\"").append(super.getHttpClientWrapper().getPassword());
		}
		postObj.append("\"}");

		// HTTP Post
		String json = postJSONSite(MOBILE_HOST_URL + "/api/wager/SaveBet", postObj.toString(), headerValuePairs);
		
		LOGGER.info("Exiting saveBet()");
		return json;
	}

	/**
	 * 
	 * @param addSave
	 * @param xhtml
	 * @param event
	 * @param ae
	 * @return
	 * @throws BatchException
	 */
	private String processLineChange(String addSave, String xhtml, BaseRecordEvent event, AccountEvent ae) throws BatchException {
		LOGGER.info("Entering processLineChange()");

		final Map<String, String> lineChanges = MP.processLineChange(xhtml);
		if (lineChanges != null && !lineChanges.isEmpty()) {
			optionValue = lineChanges.get("Id");
			if (event instanceof SpreadRecordEvent) {
				final SpreadRecordEvent sre = (SpreadRecordEvent)event;
				if (determineSpreadLineChange(sre, ae, lineChanges)) {
					if (addSave.equals("AddBet")) {
						xhtml = addBet(ae.getActualamount());
					} else {
						xhtml = saveBet(ae.getActualamount());
					}
					// Access Token
					accessToken = getAccessToken();
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
						xhtml = addBet(ae.getActualamount());
					} else {
						xhtml = saveBet(ae.getActualamount());
					}
					// Access Token
					accessToken = getAccessToken();
				} else {
					throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed", xhtml);
				}
			} else if (event instanceof MlRecordEvent) {
				final MlRecordEvent mre = (MlRecordEvent)event;
				
				if (determineMlLineChange(mre, ae, lineChanges)) {
					if (addSave.equals("AddBet")) {
						xhtml = addBet(ae.getActualamount());
					} else {
						xhtml = saveBet(ae.getActualamount());
					}
					// Access Token
					accessToken = getAccessToken();
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
	 * @param imagedata
	 * @return
	 */
	protected String processCaptcha(String imagedata) {
		String textdata = null;

		try {
			//Process captcha String
			final CaptchaReader obj = new CaptchaReader();
			textdata = captchaStr = obj.parseCaptcha(imagedata);
			LOGGER.error("Captcha Text: " + captchaStr);

			// Clean up text if necessary
			if (!Pattern.matches("[a-zA-Z0-9]{6}",captchaStr)) {
				// TODO
				LOGGER.error("Captcha has non alphanumeric characters " + textdata);
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		return textdata;
	}

	/**
	 * 
	 * @return
	 */
	private String getAccessToken() {
		LOGGER.info("Entering getAccessToken()");

		// Get the email access token so we can update the users
		String accessToken = "";
		try {
			TicketAdvantageGmailOath TicketAdvantageGmailOath = new TicketAdvantageGmailOath();
			accessToken = TicketAdvantageGmailOath.getAccessToken();
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		LOGGER.info("Exiting getAccessToken()");
		return accessToken;
	}
}