/**
 * 
 */
package com.ticketadvantage.services.dao.sites.tdsportsten;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.sites.SiteTransaction;
import com.ticketadvantage.services.dao.sites.SiteWagers;
import com.ticketadvantage.services.dao.sites.tdsports.TDSportsProcessSite;
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
public class TDSportsTenProcessSite extends TDSportsProcessSite {
	private static final Logger LOGGER = Logger.getLogger(TDSportsTenProcessSite.class);
	protected TDSportsTenParser NSP = new TDSportsTenParser();

	/**
	 * 
	 * @param host
	 * @param username
	 * @param password
	 * @param isMobile
	 * @param showRequestResponse
	 */
	public TDSportsTenProcessSite(String host, String username, String password, boolean isMobile,
			boolean showRequestResponse) {
		super("TDSportsTen", host, username, password, isMobile, showRequestResponse);
		LOGGER.info("Entering TDSportsTenProcessSite()");

		// Setup the parser
		this.siteParser = NSP;

		// Menu items
		// Menu items
		NFL_LINES_SPORT = new String[] { "FOOTBALL" };
		NFL_LINES_NAME = new String[] { "NFL", "NFL PRESEASON" };
		NFL_FIRST_SPORT = new String[] { "FOOTBALL" };
		NFL_FIRST_NAME = new String[] { "NFL - 1ST HALF" };
		NFL_SECOND_SPORT = new String[] { "FOOTBALL" };
		NFL_SECOND_NAME = new String[] { "NFL - 2ND HALF" };
		NCAAF_LINES_SPORT = new String[] { "COLLEGE FOOTBALL" };
		NCAAF_LINES_NAME = new String[] { "NCAA FOOTBALL - MEN" };
		NCAAF_FIRST_SPORT = new String[] { "COLLEGE FOOTBALL" };
		NCAAF_FIRST_NAME = new String[] { "NCAA FOOTBALL 1ST HALF", "NCAA (F) - 1ST HALF" };
		NCAAF_SECOND_SPORT = new String[] { "COLLEGE FOOTBALL" };
		NCAAF_SECOND_NAME = new String[] { "NCAA FOOTBALL MEN 2ND HALVES", "NCAA (F) - 2ND HALF" };
		NBA_LINES_SPORT = new String[] { "BASKETBALL" };
		NBA_LINES_NAME = new String[] { "NBA" };
		NBA_FIRST_SPORT = new String[] { "BASKETBALL" };
		NBA_FIRST_NAME = new String[] { "NBA (1ST HALF)" };
		NBA_SECOND_SPORT = new String[] { "BASKETBALL" };
		NBA_SECOND_NAME = new String[] { "NBA (2ND HALF)" };
		NCAAB_LINES_SPORT = new String[] { "COLLEGE BASKETBALL" };
		NCAAB_LINES_NAME = new String[] { "NCAA BASKETBALL GAME LINES", "NCAA BASKETBALL - EXTRA GAMES" };
		NCAAB_FIRST_SPORT = new String[] { "COLLEGE BASKETBALL" };
		NCAAB_FIRST_NAME = new String[] { "NCAA BASKETBALL 1ST HALF" };
		NCAAB_SECOND_SPORT = new String[] { "COLLEGE BASKETBALL" };
		NCAAB_SECOND_NAME = new String[] { "NCAA (B) - 2ND HALF" };
		NHL_LINES_SPORT = new String[] { "HOCKEY" };
		NHL_LINES_NAME = new String[] { "NATIONAL HOCKEY LEAGUE" };
		NHL_FIRST_SPORT = new String[] { "HOCKEY" };
		NHL_FIRST_NAME = new String[] { "NHL - 1ST PERIOD" };
		NHL_SECOND_SPORT = new String[] { "HOCKEY" };
		NHL_SECOND_NAME = new String[] { "NHL - 2ND PERIOD" };
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

		LOGGER.info("Exiting TDSportsTenProcessSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final String[][] TDSITES = new String [][] { 
//				{ "http://names777.com/", "Rc1525", "John", "100", "100", "100", "Chicago", "ET"},
//				{ "http://ezplay2001.com/", "G9911", "grey", "1", "1", "1", "None", "ET"}
				{ "http://ezplay2001.com/", "G9912", "green", "1", "1", "1", "None", "ET"}
			};

			// Loop through the sites
			for (int i = 0; i < TDSITES.length; i++) {
				final TDSportsTenProcessSite processSite = new TDSportsTenProcessSite(TDSITES[i][0], TDSITES[i][1],
						TDSITES[i][2], false, true);
			    processSite.httpClientWrapper.setupHttpClient(TDSITES[0][6]);
			    processSite.processTransaction = false;
			    processSite.timezone = TDSITES[0][7];
			    processSite.siteParser = processSite.TDP = processSite.NSP;
			    processSite.siteParser.setTimezone(TDSITES[0][7]);
			    processSite.NSP.setTimezone(TDSITES[0][7]);
			    String xhtml = processSite.loginToSite(TDSITES[i][1], TDSITES[i][2]);

				Set<PendingEvent> pendingEvents = processSite.getPendingBets(TDSITES[0][1], TDSITES[0][2], null);
				if (pendingEvents != null && !pendingEvents.isEmpty()) {
					Iterator<PendingEvent> itr = pendingEvents.iterator();
					while (itr.hasNext()) {
						System.out.println("PendingEvent: " + itr.next());
					}
				}

/*
			    final Map<String, String> map = processSite.getMenuFromSite("ncaaflines", xhtml);
			    	LOGGER.debug("map: " + map);

				xhtml = processSite.selectSport("ncaaflines");
				LOGGER.debug("XHTML: " + xhtml);

				final Iterator<EventPackage> ep = processSite.parseGamesOnSite("ncaaflines", xhtml);   

				final TotalRecordEvent mre = new TotalRecordEvent();
				mre.setTotalinputfirstone("");
				mre.setId(new Long(397));
				mre.setEventname("1H STL CARDINALS [M. MIKOLAS -R]");
				mre.setEventtype("ml");
				mre.setSport("ncaaflines"); 
				mre.setUserid(new Long(6));
				mre.setEventid(new Integer(397));
				mre.setEventid1(new Integer(397));
				mre.setEventid2(new Integer(398));
				mre.setRotationid(new Integer(397));

				// Step 9 - Find event
				EventPackage eventPackage = processSite.findEvent(ep, mre);
				if (eventPackage == null) {
					throw new BatchException(BatchErrorCodes.GAME_NOT_AVAILABLE, BatchErrorMessage.GAME_NOT_AVAILABLE, "Game cannot be found on site for " + mre.getEventname(), xhtml);
				}
				LOGGER.error("eventPackage: " + eventPackage);
*/
/*
				// Setup the timezone
			    processSite.siteParser = processSite.TDP = processSite.BSP;
			    processSite.siteParser.setTimezone(TDSITES[0][7]);
			    processSite.BSP.setTimezone(TDSITES[0][7]);
				
			    processSite.MAP_DATA = new HashMap<String, String>();
			    // String xhtml = "<!DOCTYPE html><html><head id=\"ctl00_Head\"><meta http-equiv=\"cache-control\" content=\"no-cache\" /><meta http-equiv=\"pragma\" content=\"no-cache\" /><meta name=\"robots\" content=\"index,follow\" /><meta name=\"revisit\" content=\"2 days\" /><meta name=\"robots\" content=\"All\" /><meta name=\"Revisit-after\" content=\"2 days\" /><meta name=\"Distribution\" content=\"Global\" /><meta name=\"Copyright\" content=\"504bet.net\" /><meta name=\"HandheldFriendly\" content=\"true\" /><meta name=\"viewport\" content=\"width=600\" /><title>	504bet</title><meta name=\"keywords\" content=\"agents sportsbook, sportsbook for agents, online sportsbook for agents, bettors, sports betting, sports bets, sports wagering, wagering, online gambling, sportsbook, sports odds, internet odds, internet gambling, scores, live odds, sports wager, bet, bets, wagers, odds, sport book, sports book, sports books\" /><meta name=\"description\" content=\"504bet Sportsbook Online Agents offers the latest betting odds NBA and NCAA Basketball, MLB Baseball, NFL Football, Motor Sports. Find attractive Sports Betting Bonuses Promotions Online. Special for agents, credit shop not post up sportsbook. This is an agents sportsbook and sports betting credit shop.\" /><link rel=\"shortcut icon\" href=\"../App_Themes/pm247/images/favicon.ico\" type=\"image/x-icon\" /><link rel=\"icon\" href=\"../App_Themes/pm247/images/favicon.ico\" type=\"image/x-icon\" /><link href=\"../App_Themes/pm247/content/css/font-awesome.min.css\" type=\"text/css\" rel=\"stylesheet\" /><link href=\"../App_Themes/pm247/content/css/jquery.slider.css\" type=\"text/css\" rel=\"stylesheet\" /><link href=\"../App_Themes/pm247/content/css/jquery.slider.min.css\" type=\"text/css\" rel=\"stylesheet\" /><link href=\"../App_Themes/pm247/content/css/theme.min.css\" type=\"text/css\" rel=\"stylesheet\" /></head><body><form name=\"aspnetForm\" method=\"post\" action=\"Schedule.aspx?WT=0&amp;lg=78\" id=\"aspnetForm\"><div><input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"/wEPDwUKLTMxNjc3NTM3NQ9kFgJmD2QWAgIDD2QWAmYPZBYCAgEPZBYKAgEPDxYCHgRUZXh0BQRNTDAyZGQCBQ8PFgIfAAUHMzAwIFVTRGRkAgkPDxYCHwAFCjE4LDY1MCBVU0RkZAINDw8WAh8ABQkxLDY1MCBVU0RkZAIRDw8WAh8ABQUwIFVTRGRkZHubgJBv1maHWbJvZid9fwAxRN2b\" /></div><div>	<input type=\"hidden\" name=\"__EVENTVALIDATION\" id=\"__EVENTVALIDATION\" value=\"/wEWAgKo2ZGICQLqlbKLCa/Xgy3mkKYkPqjIcMhFxttlzNhu\" /></div><div class=\"menu-container\"> <div class=\"container content-menu\"> <center> <ul id=\"navmenu\" class=\"menu\"> <li> <div> <span><i class=\"fa fa-user\"></i> </span> <span class=\"info\"><span id=\"ctl00_Content3_AccountFigures3_lblAccount\">ML02</span></span> </div> </li> <li> <div> <span><span id=\"ctl00_Content3_AccountFigures3_LocalizedLabel5\">Balance:</span></span> <span class=\"info\"><span id=\"ctl00_Content3_AccountFigures3_lblCurrentBalance\">300 USD</span></span> </div> </li> <li> <div> <span><span id=\"ctl00_Content3_AccountFigures3_LocalizedLabel6\">Available:</span></span> <span class=\"info\"><span id=\"ctl00_Content3_AccountFigures3_lblRealAvailBalance\">18,650 USD</span></span> </div> </li> <li> <div> <span><span id=\"ctl00_Content3_AccountFigures3_LocalizedLabel8\">Pending:</span></span> <span class=\"info\"><span id=\"ctl00_Content3_AccountFigures3_lblAmountAtRisk\">1,650 USD</span></span> </div> </li> <li> <div> <span><span id=\"ctl00_Content3_AccountFigures3_LocalizedLabel11\">Free Play:</span></span> <span class=\"info\"><span id=\"ctl00_Content3_AccountFigures3_lblFreePlay\">0 USD</span></span> </div> </li> </ul> </center> </div></div><div class=\"header-container\"> <div class=\"container\"> <a href=\"Welcome.aspx\" class=\"logo\"> <img src=\"../App_themes/pm247/images/logo.png\" style=\"height:105px;width:305px;border-width:0px;\" /> </a> <div class=\"secondary-menu-container\"> <span id=\"ctl00_WagerLnk\"> <ul class=\"secondary-menu\"> <li class=\"has-sub\"><img width=\"20\" height=\"20\" src=\"http://logos.horizonsports.es/icons/football2.png\"><a id=\"ctl00_WagerLnk_lnkSports\" href=\"Sports.aspx\">Sports</a> <ul> <li><a id=\"ctl00_WagerLnk_lnkMatchups\" href=\"../matchups/\" target=\"_blank\">Matchups</a></li> <li><a id=\"ctl00_WagerLnk_lnkSchedule\" href=\"../schedule/\" target=\"_blank\">Schedule</a></li> </ul> </li> <li><img width=\"20\" height=\"20\" src=\"http://logos.horizonsports.es/icons/horses.png\"></li><li><img width=\"20\" height=\"20\" src=\"http://logos.horizonsports.es/icons/livebetting.png\"><a id=\"ctl00_WagerLnk_lnkLiveBet\" href=\"live.aspx?ws=0\"><b>[ExtraLive]</b></a></li> <li class=\"casino\"><img width=\"20\" height=\"20\" src=\"http://logos.horizonsports.es/icons/dice.png\"></li> <li class=\"has-sub\"><img width=\"20\" height=\"20\" src=\"http://logos.horizonsports.es/icons/player2.png\"><a id=\"ctl00_WagerLnk_lnkMyAccount\" href=\"Welcome.aspx\">My Account</a> <ul> <li><a id=\"ctl00_WagerLnk_lnkFillOpen\" href=\"FillChoose.aspx\">Fill Open</a></li> <li><a id=\"ctl00_WagerLnk_lnkOpenBets\" href=\"OpenBets.aspx\">Open Bets</a></li> <li><a id=\"ctl00_WagerLnk_lnkHistory\" href=\"History.aspx\">History</a></li> <li><a id=\"ctl00_WagerLnk_lnkFigures\" href=\"Welcome.aspx\">Figures</a></li> </ul> </li> <li class=\"logout\"><a id=\"ctl00_WagerLnk_lnkLogout\" href=\"../LogOut.aspx\">Logout</a><i class=\"fa fa-sign-out\"></i></li> </ul><ul></ul> </span> </div> </div></div><BR><BR><div class=\"main-menu-container\"> <div class=\"container\"> <ul class=\"nav menu md-menu\"> <li id=\"WT35\" class=\"tab red\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk0\" href=\"CreateSports.aspx?WT=35\">Bet It All</a></span></li> <li id=\"WT0\" class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk1\" href=\"CreateSports.aspx?WT=0\">Straight</a></span></li> <li id=\"WT1\" class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk2\" href=\"CreateSports.aspx?WT=1\">Parlay</a></span></li> <li id=\"WT2\" class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk3\" href=\"ChooseTeaser.aspx?WT=2\">Teaser</a></span></li> <li id=\"WT5\" class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk4\" href=\"CreateSports.aspx?WT=5\">Round Robin</a></span></li> <li id=\"WT3\" class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk5\" href=\"CreateSports.aspx?WT=3\">If Win Only</a></span></li> <li id=\"WT7\" class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk6\" href=\"CreateSports.aspx?WT=7\">If Win or Tie</a></span></li> <li id=\"WT4\" class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk7\" href=\"CreateSports.aspx?WT=4\">Win Reverse</a></span></li> <li id=\"WT8\" class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk8\" href=\"CreateSports.aspx?WT=8\">Action Reverse</a></span></li> <!--li class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk9\" href=\"CreateSports.aspx?WT=15\"><b>[Compact RoundRobin]</b></a></span><span class=\"tabrg\">&nbsp;</span></li> <li class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk10\" href=\"CreateSports.aspx?WT=16\"><b>[Key Parlay]</b></a></span><span class=\"tabrg\">&nbsp;</span></li> <li class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk11\" href=\"CreateSports.aspx?WT=17\"><b>[Compact IfWinOnly]</b></a></span><span class=\"tabrg\">&nbsp;</span></li> <li class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk12\" href=\"CreateSports.aspx?WT=18\"><b>[Compact IfWinOrTie]</b></a></span><span class=\"tabrg\">&nbsp;</span></li--> </ul> </div></div><div class=\"content-container\"> <div class=\"container\"> <div class=\"wagercontent\"> <center ><div class=\"header-lines-container\"><span class=\"league-header\"><img class=\"league-logo\" src=\"http://logos.horizonsports.es/leagues/NBA - 2ND HALVES.png\"><span class=\"league-name\">NBA - 2ND HALVES</span></span><span class=\"options\"><input type=\"submit\" name=\"ctl00$WagerContent$btn_Continue1\" value=\"Continue\" id=\"ctl00_WagerContent_btn_Continue1\" class=\"btn btn-default\" /><button type=\"submit\" class=\"btn btn-refresh\"><span>Refresh</span><i class=\"fa fa-refresh\"></i></button></span></div><TABLE CELLPADDING=\"0\" CELLSPACING=\"0\" BORDER=\"0\" WIDTH=\"100%\" ALIGN=\"center\"><TR CLASS=\"GameHeader\"><TD WIDTH=\"3%\"></TD><TD WIDTH=\"5%\">#</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><span>Team</span></TD><TD nowrap ALIGN=\"center\" WIDTH=\"20%\"><span>Spread</span></TD><TD nowrap ALIGN=\"center\" WIDTH=\"20%\"><span>Total</span></TD><TD nowrap ALIGN=\"center\" WIDTH=\"20%\"><span>M Line</span></TD></TR><TR ALIGN=\"left\" class=\"GameHeader gameBanner\"><TD COLSPAN=\"8\">NBA - 2ND HALVES Second Half Lines - Mar 06</TD></TR><TR ALIGN=\"left\" class=\"GameHeader gameBanner\"><TD COLSPAN=\"8\">NO PELICANS 67 - LA CLIPPERS 52</TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"right\" class=\"date\" COLSPAN=\"7\"><i class=\"fa fa-clock-o\"></i>Mar 06 11:52 PM </TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"left\"></TD><TD>2563</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/2H NO PELICANS.png\"> 2H NO PELICANS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"0_1593137_6.5_-110\" hidden=\"true\"><span class=\"line\">+6&frac12;-110</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"2_1593137_-113.5_-110\" hidden=\"true\"><span class=\"line\">o113&frac12;-110</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"4_1593137_0_335\" hidden=\"true\"><span class=\"line\">+335</span></label></div></TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"left\"></TD><TD>2514</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/2H LA CLIPPERS.png\"> 2H LA CLIPPERS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"1_1593137_-6.5_-110\" hidden=\"true\"><span class=\"line\">-6&frac12;-110</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"3_1593137_113.5_-110\" hidden=\"true\"><span class=\"line\">u113&frac12;-110</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"5_1593137_0_-425\" hidden=\"true\"><span class=\"line\">-425</span></label></div></TD></TR><TR ALIGN=\"left\" class=\"GameHeader gameBanner\"><TD COLSPAN=\"8\">BRO NETS 53 - GS WARRIORS 48</TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"right\" class=\"date\" COLSPAN=\"7\"><i class=\"fa fa-clock-o\"></i>Mar 06 11:49 PM </TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"left\"></TD><TD>2515</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/2H BRO NETS.png\"> 2H BRO NETS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"0_1593145_13.5_-110\" hidden=\"true\"><span class=\"line\">+13&frac12;-110</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"2_1593145_-119_-110\" hidden=\"true\"><span class=\"line\">o119-110</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"left\"></TD><TD>2516</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/2H GS WARRIORS.png\"> 2H GS WARRIORS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"1_1593145_-13.5_-110\" hidden=\"true\"><span class=\"line\">-13&frac12;-110</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"3_1593145_119_-110\" hidden=\"true\"><span class=\"line\">u119-110</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR ALIGN=\"left\" class=\"GameHeader gameBanner\"><TD COLSPAN=\"8\">NBA - 2ND HALVES Third Quarter Lines - Mar 07</TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"right\" class=\"date\" COLSPAN=\"7\"><i class=\"fa fa-clock-o\"></i>Mar 07 12:00 AM </TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"left\"></TD><TD>5563</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/3Q NO PELICANS.png\"> 3Q NO PELICANS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"0_1593141_3_-115\" hidden=\"true\"><span class=\"line\">+3-115</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"2_1593141_-57_-120\" hidden=\"true\"><span class=\"line\">o57-120</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"left\"></TD><TD>5514</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/3Q LA CLIPPERS.png\"> 3Q LA CLIPPERS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"1_1593141_-3_-115\" hidden=\"true\"><span class=\"line\">-3-115</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"3_1593141_57_-110\" hidden=\"true\"><span class=\"line\">u57-110</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"right\" class=\"date\" COLSPAN=\"7\"><i class=\"fa fa-clock-o\"></i>Mar 06 11:49 PM </TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"left\"></TD><TD>5515</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/3Q BRO NETS.png\"> 3Q BRO NETS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"0_1593149_6.5_100\" hidden=\"true\"><span class=\"line\">+6&frac12;EV</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"2_1593149_-60_-105\" hidden=\"true\"><span class=\"line\">o60-105</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"left\"></TD><TD>5516</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/3Q GS WARRIORS.png\"> 3Q GS WARRIORS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"1_1593149_-6.5_-130\" hidden=\"true\"><span class=\"line\">-6&frac12;-130</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"3_1593149_60_-125\" hidden=\"true\"><span class=\"line\">u60-125</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR ALIGN=\"left\" class=\"GameHeader gameBanner\"><TD COLSPAN=\"8\">NBA - 2ND HALVES Second Half Lines - Mar 07</TD></TR><TR ALIGN=\"left\" class=\"GameHeader gameBanner\"><TD COLSPAN=\"8\">NBA 2ND HALF TEAM TOTALS</TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"right\" class=\"date\" COLSPAN=\"7\"><i class=\"fa fa-clock-o\"></i>Mar 07 12:00 AM </TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"left\"></TD><TD>84025</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/2H PELICANS TOTAL POINTS.png\"> 2H PELICANS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"2_1593887_-53.5_-125\" hidden=\"true\"><span class=\"line\">o53&frac12;-125</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"left\"></TD><TD>84026</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/2H PELICANS TOTAL POINTS.png\"> 2H PELICANS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"3_1593887_53.5_-105\" hidden=\"true\"><span class=\"line\">u53&frac12;-105</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"right\" class=\"date\" COLSPAN=\"7\"><i class=\"fa fa-clock-o\"></i>Mar 07 12:00 AM </TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"left\"></TD><TD>84027</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/2H CLIPPERS TOTAL POINTS.png\"> 2H CLIPPERS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"2_1593889_-60_-115\" hidden=\"true\"><span class=\"line\">o60-115</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"left\"></TD><TD>84028</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/2H CLIPPERS TOTAL POINTS.png\"> 2H CLIPPERS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"3_1593889_60_-115\" hidden=\"true\"><span class=\"line\">u60-115</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"right\" class=\"date\" COLSPAN=\"7\"><i class=\"fa fa-clock-o\"></i>Mar 06 11:49 PM </TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"left\"></TD><TD>84029</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/2H NETS TOTAL POINTS.png\"> 2H NETS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"2_1593891_-53_-120\" hidden=\"true\"><span class=\"line\">o53-120</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"left\"></TD><TD>84030</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/2H NETS TOTAL POINTS.png\"> 2H NETS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"3_1593891_53_-110\" hidden=\"true\"><span class=\"line\">u53-110</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"right\" class=\"date\" COLSPAN=\"7\"><i class=\"fa fa-clock-o\"></i>Mar 06 11:49 PM </TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"left\"></TD><TD>84031</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/2H WARRIORS TOTAL POINTS.png\"> 2H WARRIORS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"2_1593893_-66.5_-115\" hidden=\"true\"><span class=\"line\">o66&frac12;-115</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"left\"></TD><TD>84032</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/2H WARRIORS TOTAL POINTS.png\"> 2H WARRIORS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"3_1593893_66.5_-115\" hidden=\"true\"><span class=\"line\">u66&frac12;-115</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR ALIGN=\"left\" class=\"GameHeader gameBanner\"><TD COLSPAN=\"8\">NBA - 2ND HALVES Third Quarter Lines - Mar 07</TD></TR><TR ALIGN=\"left\" class=\"GameHeader gameBanner\"><TD COLSPAN=\"8\">NBA 3RD QUARTER TEAM TOTALS</TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"right\" class=\"date\" COLSPAN=\"7\"><i class=\"fa fa-clock-o\"></i>Mar 07 12:00 AM </TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"left\"></TD><TD>87025</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/3Q PELICANS TOTAL POINTS.png\"> 3Q PELICANS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"2_1593888_-27_-115\" hidden=\"true\"><span class=\"line\">o27-115</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"left\"></TD><TD>87026</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/3Q PELICANS TOTAL POINTS.png\"> 3Q PELICANS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"3_1593888_27_-115\" hidden=\"true\"><span class=\"line\">u27-115</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"right\" class=\"date\" COLSPAN=\"7\"><i class=\"fa fa-clock-o\"></i>Mar 07 12:00 AM </TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"left\"></TD><TD>87027</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/3Q CLIPPERS TOTAL POINTS.png\"> 3Q CLIPPERS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"2_1593890_-30_-115\" hidden=\"true\"><span class=\"line\">o30-115</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"left\"></TD><TD>87028</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/3Q CLIPPERS TOTAL POINTS.png\"> 3Q CLIPPERS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"3_1593890_30_-115\" hidden=\"true\"><span class=\"line\">u30-115</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"right\" class=\"date\" COLSPAN=\"7\"><i class=\"fa fa-clock-o\"></i>Mar 06 11:49 PM </TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"left\"></TD><TD>87029</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/3Q NETS TOTAL POINTS.png\"> 3Q NETS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"2_1593892_-27_-110\" hidden=\"true\"><span class=\"line\">o27-110</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"left\"></TD><TD>87030</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/3Q NETS TOTAL POINTS.png\"> 3Q NETS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"3_1593892_27_-120\" hidden=\"true\"><span class=\"line\">u27-120</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"right\" class=\"date\" COLSPAN=\"7\"><i class=\"fa fa-clock-o\"></i>Mar 06 11:49 PM </TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"left\"></TD><TD>87031</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/3Q WARRIORS TOTAL POINTS.png\"> 3Q WARRIORS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"2_1593895_-33_-120\" hidden=\"true\"><span class=\"line\">o33-120</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"left\"></TD><TD>87032</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/3Q WARRIORS TOTAL POINTS.png\"> 3Q WARRIORS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"3_1593895_33_-110\" hidden=\"true\"><span class=\"line\">u33-110</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR><TD COLSPAN=\"8\"></TD></TR></TABLE><BR></center> </div> </div></div><div class=\"footer-container\"> <div class=\"container\"> <span> <i class=\"fa fa-copyright\"></i> <span id=\"currentyear\">2018</span> 504bet.net All Rights Reserved </span> <a class=\"align-right btn btn-link\" href=\"/terms\" target=\"_blank\">Terms &amp; Conditions</a> </div></div></form> <script type=\"text/javascript\" src=\"../scripts/jquery.min.js\"></script> <script type=\"text/javascript\" src=\"../scripts/selector.js\"></script> <script type=\"text/javascript\" src=\"../scripts/teamnames.js\"></script> <script type=\"text/javascript\" src=\"http://arrow.scrolltotop.com/arrow59.js\"></script> <script type=\"text/javascript\"> $(function() { var casinoLink = $('ul.secondary-menu').children('.casino').children('a'); if (casinoLink.length <=0 ) { $('ul.secondary-menu').children('.casino').remove(); } }); </script></body></html>";

				TotalRecordEvent tre = new TotalRecordEvent();
				tre.setTotalinputsecondone("105");
				tre.setTotalinputjuicefirstone("120");
				tre.setTotaljuiceplusminusfirstone("-");
				tre.setEventname("NBA #2563 New Orleans Pelicans/Los Angeles Clippers o114.0 -120.0 for 2H");
				tre.setEventtype("total");
				tre.setSport("nbasecond"); 
				tre.setUserid(new Long(6));
				tre.setEventid(new Integer(2563));
				tre.setEventid1(new Integer(2563));
				tre.setEventid2(new Integer(2564));

				// Step 8 - Parse the games from the site
				final Iterator<EventPackage> ep = processSite.parseGamesOnSite("nbasecond", xhtml);
				LOGGER.debug("ep: " + ep);
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
		String xhtml = getSite(this.httpClientWrapper.getHost());
		MAP_DATA = NSP.parseIndex(xhtml);
		MAP_DATA.remove("username");
		MAP_DATA.remove("password");
		MAP_DATA.put("Account", username);
		MAP_DATA.put("Password", password.toLowerCase());
		final List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
		setupNameValuesEmpty(postValuePairs, MAP_DATA, "");

		xhtml = postSite(this.httpClientWrapper.getHost() + "/login.aspx", postValuePairs);
		LOGGER.debug("HTML: " + xhtml);

		// Parse login
		MAP_DATA = NSP.parseLogin(xhtml);
		LOGGER.debug("MAP_DATA: " + MAP_DATA);

		// Setup the webapp name
		httpClientWrapper.setWebappname("wager");

		// CreateSports.aspx
		xhtml = getSite(populateUrl("CreateSports.aspx?WT=0"));
		LOGGER.debug("HTML: " + xhtml);

		LOGGER.info("Exiting loginToSite()");
		return xhtml;
	}

	/*
	 * 
	 */
	@Override
	protected String confirmWager(String xhtml, SiteTransaction siteTransaction, EventPackage eventPackage, BaseRecordEvent event, AccountEvent ae, String wagerType) throws BatchException {
		LOGGER.info("Entering confirmWager()");
		
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
			MAP_DATA.put("password", httpClientWrapper.getPassword());	
			MAP_DATA.remove("toUpperCase");
		} else if (MAP_DATA.containsKey("Password")) {
			MAP_DATA.put("Password", httpClientWrapper.getPassword());
			MAP_DATA.remove("toUpperCase");
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

		LOGGER.info("Exiting confirmWager()");
		return xhtml;
	}
}