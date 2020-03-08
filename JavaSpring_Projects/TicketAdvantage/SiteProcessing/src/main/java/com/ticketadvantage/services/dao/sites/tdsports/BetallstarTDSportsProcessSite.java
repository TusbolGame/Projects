/**
 * 
 */
package com.ticketadvantage.services.dao.sites.tdsports;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.ticketadvantage.services.errorhandling.BatchException;

/**
 * @author jmiller
 *
 */
public class BetallstarTDSportsProcessSite extends TDSportsProcessSite {
	private static final Logger LOGGER = Logger.getLogger(BetallstarTDSportsProcessSite.class);
	protected BetallstarTDSportsParser BSP = new BetallstarTDSportsParser();

	/**
	 * 
	 */
	public BetallstarTDSportsProcessSite(String host, String username, String password, boolean isMobile, boolean showRequestResponse) {
		super("BetallstarTDSports", host, username, password, isMobile, showRequestResponse);
		LOGGER.info("Entering BetallstarTDSportsProcessSite()");

		// Setup the parser
		this.siteParser = BSP;

		// Menu items
		NFL_LINES_SPORT = new String[] { "NFL FOOTBALL" };
		NFL_LINES_NAME = new String[] { "NFL - GAME LINES" };
		NFL_FIRST_SPORT = new String[] { "FOOTBALL HALFTIMES" };
		NFL_FIRST_NAME = new String[] { "NFL (1H)" };
		NFL_SECOND_SPORT = new String[] { "FOOTBALL HALFTIMES" };
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
		NCAAB_LINES_SPORT = new String[] { "BASKETBALL GAME LINES" };
		NCAAB_LINES_NAME = new String[] { "NCAA BASKETBALL - MEN" };
		NCAAB_FIRST_SPORT = new String[] { "BASKETBALL GAME LINES" };
		NCAAB_FIRST_NAME = new String[] { "NCAA BASKETBALL - (1H)" };
		NCAAB_SECOND_SPORT = new String[] { "BASKETBALL GAME LINES" };
		NCAAB_SECOND_NAME = new String[] { "NCAA BASKETBALL - (2H)" };
		NHL_LINES_SPORT = new String[] { "HOCKEY" };
		NHL_LINES_NAME = new String[] { "NHL - GAME LINES" };
		NHL_FIRST_SPORT = new String[] { "HOCKEY" };
		NHL_FIRST_NAME = new String[] { "NHL (1P)" };
		NHL_SECOND_SPORT = new String[] { "HOCKEY" };
		NHL_SECOND_NAME = new String[] { "NHL (2P)" };
		NHL_THIRD_SPORT = new String[] { "HOCKEY" };
		NHL_THIRD_NAME = new String[] { "NHL (3P)" };
		WNBA_LINES_NAME = new String[] { "WNBA" };
		WNBA_LINES_SPORT = new String[] { "Basketball" };
		WNBA_FIRST_NAME = new String[] { "WNBA 1st Half" };
		WNBA_FIRST_SPORT = new String[] { "Basketball" };
		WNBA_SECOND_NAME = new String[] { "WNBA 2nd Half" };
		WNBA_SECOND_SPORT = new String[] { "Basketball" };
		MLB_LINES_SPORT = new String[] { "BASEBALL" };
		MLB_LINES_NAME = new String[] { "MLB - GAME LINES" };
		MLB_FIRST_SPORT = new String[] { "BASEBALL" };
		MLB_FIRST_NAME = new String[] { "MLB (1H)" };
		MLB_SECOND_SPORT = new String[] { "BASEBALL" };
		MLB_SECOND_NAME = new String[] { "MLB (2H)" };

		LOGGER.info("Exiting BetallstarTDSportsProcessSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final String[][] TDSITES = new String [][] { 
				{ "http://betallstar.ag", "qzsl3152", "buc73", "100", "100", "100", "None", "ET"}
			};

			// Loop through the sites
			for (int i = 0; i < TDSITES.length; i++) {
				final BetallstarTDSportsProcessSite processSite = new BetallstarTDSportsProcessSite(TDSITES[i][0], TDSITES[i][1],
						TDSITES[i][2], false, true);
			    processSite.httpClientWrapper.setupHttpClient(TDSITES[0][6]);
			    processSite.processTransaction = false;
			    processSite.timezone = TDSITES[0][7];
			    processSite.testTotal(processSite, TDSITES);

/*
			    String xhtml = processSite.loginToSite(TDSITES[i][1], TDSITES[i][2]);

			    Set<PendingEvent> pendingEvents = processSite.getPendingBets(TDSITES[i][0], TDSITES[i][1], null);
			    Iterator<PendingEvent> pes = pendingEvents.iterator();
			    while (pes.hasNext()) {
			    		PendingEvent pe = pes.next();
			    		System.out.println("PendinEvent: " + pe);
			    }
*/
/*
			    Map<String, String> map = processSite.getMenuFromSite("ncaablines", xhtml);
			    LOGGER.debug("map: " + map);

				xhtml = processSite.selectSport("ncaablines");
				LOGGER.debug("XHTML: " + xhtml);

				final Iterator<EventPackage> ep = processSite.parseGamesOnSite("ncaablines", xhtml);   
				
				SpreadRecordEvent sre = new SpreadRecordEvent();
				sre.setSpreadinputsecondone("4");
				sre.setSpreadplusminussecondone("-");
				sre.setSpreadinputjuicesecondone("120");
				sre.setSpreadjuiceplusminussecondone("-");
				sre.setId(new Long(602));
				sre.setEventname("NCAAB #740 Iona -4 -120 for Game");
				sre.setEventtype("spread");
				sre.setSport("ncaablines"); 
				sre.setUserid(new Long(6));
				sre.setEventid(new Integer(739));
				sre.setEventid1(new Integer(739));
				sre.setEventid2(new Integer(740)); 
				// eventteam1=Cal Poly SLO, eventteam2=Cal Santa Barbara, amount=100, wtype=2, iscompleted=false, attempts=0, currentattempts=null, scrappername=UI, actiontype=Standard, textnumber=null, rotationid=740, attempttime=2018-03-05 13:13:00.0, datentime=2018-03-05 13:13:00.0, datecreated=2018-03-05 13:13:51.582, datemodified=2018-03-05 13:13:51.582] and account: Accounts [id=39, name=504Bet-ML02, username=ML02, password=yellow, url=http://www.504bet.net, spreadlimitamount=100, mllimitamount=100, totallimitamount=100, timezone=ET, ownerpercentage=100, partnerpercentage=0, proxylocation=Baltimore, sitetype=Bet504TDSports, isactive=true, ismobile=false, showrequestresponse=true, datecreated=2018-03-05 12:57:08.661, datemodified=2018-03-05 12:57:08.661] Account Event: AccountEvent [id=3478, name=504Bet-ML02, spreadid=602, totalid=null, mlid=null, eventid=null, eventname=NCAAB #7
				
				EventPackage eventPackage = processSite.findEvent(ep, sre);
				LOGGER.error("eventPackage: " + eventPackage);
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
		this.siteParser = TDP = BSP;
		this.siteParser.setTimezone(timezone);
		BSP.setTimezone(timezone);

		this.httpClientWrapper.setHost("http://betallstar.ag/");
		String xhtml = getSite("http://betallstar.ag/");
		MAP_DATA = BSP.parseIndex(xhtml);
		MAP_DATA.put("Account", username);
		MAP_DATA.put("Password", password);
		final List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
		setupNameValuesEmpty(postValuePairs, MAP_DATA, "");

		List<NameValuePair> headerValuePairs = new ArrayList<NameValuePair>(1);
		headerValuePairs.add(new BasicNameValuePair("Content-Type", "application/x-www-form-urlencoded"));
		headerValuePairs.add(new BasicNameValuePair("Upgrade-Insecure-Requests", "1"));
		headerValuePairs.add(new BasicNameValuePair("Referer", "http://betallstar.ag/?p=index1"));
		List<NameValuePair> retValue = httpClientWrapper.postSitePage("http://betallstar.ag/core/engine/", null, postValuePairs, headerValuePairs);
		xhtml = getCookiesAndXhtml(retValue);
		LOGGER.debug("HTML: " + xhtml);

		// Parse login
		MAP_DATA = BSP.parseLogin(xhtml);
		LOGGER.debug("MAP_DATA: " + MAP_DATA);

		// Setup the webapp name
		httpClientWrapper.setWebappname("core/engine/wager");

		headerValuePairs = new ArrayList<NameValuePair>(1);
		headerValuePairs.add(new BasicNameValuePair("Upgrade-Insecure-Requests", "1"));
		headerValuePairs.add(new BasicNameValuePair("Referer", "http://betallstar.ag/wager/Message.aspx"));
		retValue = httpClientWrapper.getSitePage(populateUrl("Welcome.aspx"), null, headerValuePairs);
		xhtml = httpClientWrapper.getCookiesAndXhtml(retValue);
		LOGGER.debug("HTML: " + xhtml);

		headerValuePairs = new ArrayList<NameValuePair>(1);
		headerValuePairs.add(new BasicNameValuePair("Upgrade-Insecure-Requests", "1"));
		headerValuePairs.add(new BasicNameValuePair("Referer", "http://betallstar.ag/wager/Welcome.aspx"));
		retValue = httpClientWrapper.getSitePage(populateUrl("CreateSports.aspx?WT=0"), null, headerValuePairs);
		xhtml = httpClientWrapper.getCookiesAndXhtml(retValue);
		LOGGER.debug("HTML: " + xhtml);

		LOGGER.info("Exiting loginToSite()");
		return xhtml;
	}
}