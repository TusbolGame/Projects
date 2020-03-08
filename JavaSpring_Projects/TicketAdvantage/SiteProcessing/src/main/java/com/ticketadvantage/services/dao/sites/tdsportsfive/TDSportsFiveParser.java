/**
 * 
 */
package com.ticketadvantage.services.dao.sites.tdsportsfive;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ticketadvantage.services.dao.sites.SiteEventPackage;
import com.ticketadvantage.services.dao.sites.tdsports.TDSportsEventPackage;
import com.ticketadvantage.services.dao.sites.tdsports.TDSportsParser;
import com.ticketadvantage.services.dao.sites.tdsports.TDSportsTeamPackage;
import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;

/**
 * @author jmiller
 *
 */
public class TDSportsFiveParser extends TDSportsParser {
	private static final Logger LOGGER = Logger.getLogger(TDSportsFiveParser.class);
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd yyyy hh:mm a z");
	private boolean isMlb;

	/**
	 * Constructor
	 */
	public TDSportsFiveParser() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final TDSportsFiveParser bet504TDSportsParser = new TDSportsFiveParser();
			String xhtml = "<!DOCTYPE html><html><head id=\"ctl00_Head\"><meta http-equiv=\"cache-control\" content=\"no-cache\" /><meta http-equiv=\"pragma\" content=\"no-cache\" /><meta name=\"robots\" content=\"index,follow\" /><meta name=\"revisit\" content=\"2 days\" /><meta name=\"robots\" content=\"All\" /><meta name=\"Revisit-after\" content=\"2 days\" /><meta name=\"Distribution\" content=\"Global\" /><meta name=\"Copyright\" content=\"504bet.net\" /><meta name=\"HandheldFriendly\" content=\"true\" /><meta name=\"viewport\" content=\"width=600\" /><title>	504bet</title><meta name=\"keywords\" content=\"agents sportsbook, sportsbook for agents, online sportsbook for agents, bettors, sports betting, sports bets, sports wagering, wagering, online gambling, sportsbook, sports odds, internet odds, internet gambling, scores, live odds, sports wager, bet, bets, wagers, odds, sport book, sports book, sports books\" /><meta name=\"description\" content=\"504bet Sportsbook Online Agents offers the latest betting odds NBA and NCAA Basketball, MLB Baseball, NFL Football, Motor Sports. Find attractive Sports Betting Bonuses Promotions Online. Special for agents, credit shop not post up sportsbook. This is an agents sportsbook and sports betting credit shop.\" /><link rel=\"shortcut icon\" href=\"../App_Themes/pm247/images/favicon.ico\" type=\"image/x-icon\" /><link rel=\"icon\" href=\"../App_Themes/pm247/images/favicon.ico\" type=\"image/x-icon\" /><link href=\"../App_Themes/pm247/content/css/font-awesome.min.css\" type=\"text/css\" rel=\"stylesheet\" /><link href=\"../App_Themes/pm247/content/css/jquery.slider.css\" type=\"text/css\" rel=\"stylesheet\" /><link href=\"../App_Themes/pm247/content/css/jquery.slider.min.css\" type=\"text/css\" rel=\"stylesheet\" /><link href=\"../App_Themes/pm247/content/css/theme.min.css\" type=\"text/css\" rel=\"stylesheet\" /></head><body><form name=\"aspnetForm\" method=\"post\" action=\"Schedule.aspx?WT=0&amp;lg=78\" id=\"aspnetForm\"><div><input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"/wEPDwUKLTMxNjc3NTM3NQ9kFgJmD2QWAgIDD2QWAmYPZBYCAgEPZBYKAgEPDxYCHgRUZXh0BQRNTDAyZGQCBQ8PFgIfAAUHMzAwIFVTRGRkAgkPDxYCHwAFCjE4LDY1MCBVU0RkZAINDw8WAh8ABQkxLDY1MCBVU0RkZAIRDw8WAh8ABQUwIFVTRGRkZHubgJBv1maHWbJvZid9fwAxRN2b\" /></div><div>	<input type=\"hidden\" name=\"__EVENTVALIDATION\" id=\"__EVENTVALIDATION\" value=\"/wEWAgKo2ZGICQLqlbKLCa/Xgy3mkKYkPqjIcMhFxttlzNhu\" /></div><div class=\"menu-container\"> <div class=\"container content-menu\"> <center> <ul id=\"navmenu\" class=\"menu\"> <li> <div> <span><i class=\"fa fa-user\"></i> </span> <span class=\"info\"><span id=\"ctl00_Content3_AccountFigures3_lblAccount\">ML02</span></span> </div> </li> <li> <div> <span><span id=\"ctl00_Content3_AccountFigures3_LocalizedLabel5\">Balance:</span></span> <span class=\"info\"><span id=\"ctl00_Content3_AccountFigures3_lblCurrentBalance\">300 USD</span></span> </div> </li> <li> <div> <span><span id=\"ctl00_Content3_AccountFigures3_LocalizedLabel6\">Available:</span></span> <span class=\"info\"><span id=\"ctl00_Content3_AccountFigures3_lblRealAvailBalance\">18,650 USD</span></span> </div> </li> <li> <div> <span><span id=\"ctl00_Content3_AccountFigures3_LocalizedLabel8\">Pending:</span></span> <span class=\"info\"><span id=\"ctl00_Content3_AccountFigures3_lblAmountAtRisk\">1,650 USD</span></span> </div> </li> <li> <div> <span><span id=\"ctl00_Content3_AccountFigures3_LocalizedLabel11\">Free Play:</span></span> <span class=\"info\"><span id=\"ctl00_Content3_AccountFigures3_lblFreePlay\">0 USD</span></span> </div> </li> </ul> </center> </div></div><div class=\"header-container\"> <div class=\"container\"> <a href=\"Welcome.aspx\" class=\"logo\"> <img src=\"../App_themes/pm247/images/logo.png\" style=\"height:105px;width:305px;border-width:0px;\" /> </a> <div class=\"secondary-menu-container\"> <span id=\"ctl00_WagerLnk\"> <ul class=\"secondary-menu\"> <li class=\"has-sub\"><img width=\"20\" height=\"20\" src=\"http://logos.horizonsports.es/icons/football2.png\"><a id=\"ctl00_WagerLnk_lnkSports\" href=\"Sports.aspx\">Sports</a> <ul> <li><a id=\"ctl00_WagerLnk_lnkMatchups\" href=\"../matchups/\" target=\"_blank\">Matchups</a></li> <li><a id=\"ctl00_WagerLnk_lnkSchedule\" href=\"../schedule/\" target=\"_blank\">Schedule</a></li> </ul> </li> <li><img width=\"20\" height=\"20\" src=\"http://logos.horizonsports.es/icons/horses.png\"></li><li><img width=\"20\" height=\"20\" src=\"http://logos.horizonsports.es/icons/livebetting.png\"><a id=\"ctl00_WagerLnk_lnkLiveBet\" href=\"live.aspx?ws=0\"><b>[ExtraLive]</b></a></li> <li class=\"casino\"><img width=\"20\" height=\"20\" src=\"http://logos.horizonsports.es/icons/dice.png\"></li> <li class=\"has-sub\"><img width=\"20\" height=\"20\" src=\"http://logos.horizonsports.es/icons/player2.png\"><a id=\"ctl00_WagerLnk_lnkMyAccount\" href=\"Welcome.aspx\">My Account</a> <ul> <li><a id=\"ctl00_WagerLnk_lnkFillOpen\" href=\"FillChoose.aspx\">Fill Open</a></li> <li><a id=\"ctl00_WagerLnk_lnkOpenBets\" href=\"OpenBets.aspx\">Open Bets</a></li> <li><a id=\"ctl00_WagerLnk_lnkHistory\" href=\"History.aspx\">History</a></li> <li><a id=\"ctl00_WagerLnk_lnkFigures\" href=\"Welcome.aspx\">Figures</a></li> </ul> </li> <li class=\"logout\"><a id=\"ctl00_WagerLnk_lnkLogout\" href=\"../LogOut.aspx\">Logout</a><i class=\"fa fa-sign-out\"></i></li> </ul><ul></ul> </span> </div> </div></div><BR><BR><div class=\"main-menu-container\"> <div class=\"container\"> <ul class=\"nav menu md-menu\"> <li id=\"WT35\" class=\"tab red\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk0\" href=\"CreateSports.aspx?WT=35\">Bet It All</a></span></li> <li id=\"WT0\" class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk1\" href=\"CreateSports.aspx?WT=0\">Straight</a></span></li> <li id=\"WT1\" class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk2\" href=\"CreateSports.aspx?WT=1\">Parlay</a></span></li> <li id=\"WT2\" class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk3\" href=\"ChooseTeaser.aspx?WT=2\">Teaser</a></span></li> <li id=\"WT5\" class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk4\" href=\"CreateSports.aspx?WT=5\">Round Robin</a></span></li> <li id=\"WT3\" class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk5\" href=\"CreateSports.aspx?WT=3\">If Win Only</a></span></li> <li id=\"WT7\" class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk6\" href=\"CreateSports.aspx?WT=7\">If Win or Tie</a></span></li> <li id=\"WT4\" class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk7\" href=\"CreateSports.aspx?WT=4\">Win Reverse</a></span></li> <li id=\"WT8\" class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk8\" href=\"CreateSports.aspx?WT=8\">Action Reverse</a></span></li> <!--li class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk9\" href=\"CreateSports.aspx?WT=15\"><b>[Compact RoundRobin]</b></a></span><span class=\"tabrg\">&nbsp;</span></li> <li class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk10\" href=\"CreateSports.aspx?WT=16\"><b>[Key Parlay]</b></a></span><span class=\"tabrg\">&nbsp;</span></li> <li class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk11\" href=\"CreateSports.aspx?WT=17\"><b>[Compact IfWinOnly]</b></a></span><span class=\"tabrg\">&nbsp;</span></li> <li class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk12\" href=\"CreateSports.aspx?WT=18\"><b>[Compact IfWinOrTie]</b></a></span><span class=\"tabrg\">&nbsp;</span></li--> </ul> </div></div><div class=\"content-container\"> <div class=\"container\"> <div class=\"wagercontent\"> <center ><div class=\"header-lines-container\"><span class=\"league-header\"><img class=\"league-logo\" src=\"http://logos.horizonsports.es/leagues/NBA - 2ND HALVES.png\"><span class=\"league-name\">NBA - 2ND HALVES</span></span><span class=\"options\"><input type=\"submit\" name=\"ctl00$WagerContent$btn_Continue1\" value=\"Continue\" id=\"ctl00_WagerContent_btn_Continue1\" class=\"btn btn-default\" /><button type=\"submit\" class=\"btn btn-refresh\"><span>Refresh</span><i class=\"fa fa-refresh\"></i></button></span></div><TABLE CELLPADDING=\"0\" CELLSPACING=\"0\" BORDER=\"0\" WIDTH=\"100%\" ALIGN=\"center\"><TR CLASS=\"GameHeader\"><TD WIDTH=\"3%\"></TD><TD WIDTH=\"5%\">#</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><span>Team</span></TD><TD nowrap ALIGN=\"center\" WIDTH=\"20%\"><span>Spread</span></TD><TD nowrap ALIGN=\"center\" WIDTH=\"20%\"><span>Total</span></TD><TD nowrap ALIGN=\"center\" WIDTH=\"20%\"><span>M Line</span></TD></TR><TR ALIGN=\"left\" class=\"GameHeader gameBanner\"><TD COLSPAN=\"8\">NBA - 2ND HALVES Second Half Lines - Mar 06</TD></TR><TR ALIGN=\"left\" class=\"GameHeader gameBanner\"><TD COLSPAN=\"8\">NO PELICANS 67 - LA CLIPPERS 52</TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"right\" class=\"date\" COLSPAN=\"7\"><i class=\"fa fa-clock-o\"></i>Mar 06 11:52 PM </TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"left\"></TD><TD>2563</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/2H NO PELICANS.png\"> 2H NO PELICANS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"0_1593137_6.5_-110\" hidden=\"true\"><span class=\"line\">+6&frac12;-110</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"2_1593137_-113.5_-110\" hidden=\"true\"><span class=\"line\">o113&frac12;-110</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"4_1593137_0_335\" hidden=\"true\"><span class=\"line\">+335</span></label></div></TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"left\"></TD><TD>2514</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/2H LA CLIPPERS.png\"> 2H LA CLIPPERS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"1_1593137_-6.5_-110\" hidden=\"true\"><span class=\"line\">-6&frac12;-110</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"3_1593137_113.5_-110\" hidden=\"true\"><span class=\"line\">u113&frac12;-110</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"5_1593137_0_-425\" hidden=\"true\"><span class=\"line\">-425</span></label></div></TD></TR><TR ALIGN=\"left\" class=\"GameHeader gameBanner\"><TD COLSPAN=\"8\">BRO NETS 53 - GS WARRIORS 48</TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"right\" class=\"date\" COLSPAN=\"7\"><i class=\"fa fa-clock-o\"></i>Mar 06 11:49 PM </TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"left\"></TD><TD>2515</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/2H BRO NETS.png\"> 2H BRO NETS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"0_1593145_13.5_-110\" hidden=\"true\"><span class=\"line\">+13&frac12;-110</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"2_1593145_-119_-110\" hidden=\"true\"><span class=\"line\">o119-110</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"left\"></TD><TD>2516</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/2H GS WARRIORS.png\"> 2H GS WARRIORS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"1_1593145_-13.5_-110\" hidden=\"true\"><span class=\"line\">-13&frac12;-110</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"3_1593145_119_-110\" hidden=\"true\"><span class=\"line\">u119-110</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR ALIGN=\"left\" class=\"GameHeader gameBanner\"><TD COLSPAN=\"8\">NBA - 2ND HALVES Third Quarter Lines - Mar 07</TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"right\" class=\"date\" COLSPAN=\"7\"><i class=\"fa fa-clock-o\"></i>Mar 07 12:00 AM </TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"left\"></TD><TD>5563</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/3Q NO PELICANS.png\"> 3Q NO PELICANS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"0_1593141_3_-115\" hidden=\"true\"><span class=\"line\">+3-115</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"2_1593141_-57_-120\" hidden=\"true\"><span class=\"line\">o57-120</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"left\"></TD><TD>5514</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/3Q LA CLIPPERS.png\"> 3Q LA CLIPPERS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"1_1593141_-3_-115\" hidden=\"true\"><span class=\"line\">-3-115</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"3_1593141_57_-110\" hidden=\"true\"><span class=\"line\">u57-110</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"right\" class=\"date\" COLSPAN=\"7\"><i class=\"fa fa-clock-o\"></i>Mar 06 11:49 PM </TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"left\"></TD><TD>5515</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/3Q BRO NETS.png\"> 3Q BRO NETS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"0_1593149_6.5_100\" hidden=\"true\"><span class=\"line\">+6&frac12;EV</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"2_1593149_-60_-105\" hidden=\"true\"><span class=\"line\">o60-105</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"left\"></TD><TD>5516</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/3Q GS WARRIORS.png\"> 3Q GS WARRIORS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"1_1593149_-6.5_-130\" hidden=\"true\"><span class=\"line\">-6&frac12;-130</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"3_1593149_60_-125\" hidden=\"true\"><span class=\"line\">u60-125</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR ALIGN=\"left\" class=\"GameHeader gameBanner\"><TD COLSPAN=\"8\">NBA - 2ND HALVES Second Half Lines - Mar 07</TD></TR><TR ALIGN=\"left\" class=\"GameHeader gameBanner\"><TD COLSPAN=\"8\">NBA 2ND HALF TEAM TOTALS</TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"right\" class=\"date\" COLSPAN=\"7\"><i class=\"fa fa-clock-o\"></i>Mar 07 12:00 AM </TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"left\"></TD><TD>84025</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/2H PELICANS TOTAL POINTS.png\"> 2H PELICANS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"2_1593887_-53.5_-125\" hidden=\"true\"><span class=\"line\">o53&frac12;-125</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"left\"></TD><TD>84026</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/2H PELICANS TOTAL POINTS.png\"> 2H PELICANS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"3_1593887_53.5_-105\" hidden=\"true\"><span class=\"line\">u53&frac12;-105</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"right\" class=\"date\" COLSPAN=\"7\"><i class=\"fa fa-clock-o\"></i>Mar 07 12:00 AM </TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"left\"></TD><TD>84027</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/2H CLIPPERS TOTAL POINTS.png\"> 2H CLIPPERS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"2_1593889_-60_-115\" hidden=\"true\"><span class=\"line\">o60-115</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"left\"></TD><TD>84028</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/2H CLIPPERS TOTAL POINTS.png\"> 2H CLIPPERS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"3_1593889_60_-115\" hidden=\"true\"><span class=\"line\">u60-115</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"right\" class=\"date\" COLSPAN=\"7\"><i class=\"fa fa-clock-o\"></i>Mar 06 11:49 PM </TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"left\"></TD><TD>84029</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/2H NETS TOTAL POINTS.png\"> 2H NETS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"2_1593891_-53_-120\" hidden=\"true\"><span class=\"line\">o53-120</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"left\"></TD><TD>84030</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/2H NETS TOTAL POINTS.png\"> 2H NETS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"3_1593891_53_-110\" hidden=\"true\"><span class=\"line\">u53-110</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"right\" class=\"date\" COLSPAN=\"7\"><i class=\"fa fa-clock-o\"></i>Mar 06 11:49 PM </TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"left\"></TD><TD>84031</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/2H WARRIORS TOTAL POINTS.png\"> 2H WARRIORS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"2_1593893_-66.5_-115\" hidden=\"true\"><span class=\"line\">o66&frac12;-115</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"left\"></TD><TD>84032</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/2H WARRIORS TOTAL POINTS.png\"> 2H WARRIORS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"3_1593893_66.5_-115\" hidden=\"true\"><span class=\"line\">u66&frac12;-115</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR ALIGN=\"left\" class=\"GameHeader gameBanner\"><TD COLSPAN=\"8\">NBA - 2ND HALVES Third Quarter Lines - Mar 07</TD></TR><TR ALIGN=\"left\" class=\"GameHeader gameBanner\"><TD COLSPAN=\"8\">NBA 3RD QUARTER TEAM TOTALS</TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"right\" class=\"date\" COLSPAN=\"7\"><i class=\"fa fa-clock-o\"></i>Mar 07 12:00 AM </TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"left\"></TD><TD>87025</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/3Q PELICANS TOTAL POINTS.png\"> 3Q PELICANS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"2_1593888_-27_-115\" hidden=\"true\"><span class=\"line\">o27-115</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"left\"></TD><TD>87026</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/3Q PELICANS TOTAL POINTS.png\"> 3Q PELICANS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"3_1593888_27_-115\" hidden=\"true\"><span class=\"line\">u27-115</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"right\" class=\"date\" COLSPAN=\"7\"><i class=\"fa fa-clock-o\"></i>Mar 07 12:00 AM </TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"left\"></TD><TD>87027</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/3Q CLIPPERS TOTAL POINTS.png\"> 3Q CLIPPERS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"2_1593890_-30_-115\" hidden=\"true\"><span class=\"line\">o30-115</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"left\"></TD><TD>87028</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/3Q CLIPPERS TOTAL POINTS.png\"> 3Q CLIPPERS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"3_1593890_30_-115\" hidden=\"true\"><span class=\"line\">u30-115</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"right\" class=\"date\" COLSPAN=\"7\"><i class=\"fa fa-clock-o\"></i>Mar 06 11:49 PM </TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"left\"></TD><TD>87029</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/3Q NETS TOTAL POINTS.png\"> 3Q NETS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"2_1593892_-27_-110\" hidden=\"true\"><span class=\"line\">o27-110</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"left\"></TD><TD>87030</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/3Q NETS TOTAL POINTS.png\"> 3Q NETS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"3_1593892_27_-120\" hidden=\"true\"><span class=\"line\">u27-120</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"right\" class=\"date\" COLSPAN=\"7\"><i class=\"fa fa-clock-o\"></i>Mar 06 11:49 PM </TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"left\"></TD><TD>87031</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/3Q WARRIORS TOTAL POINTS.png\"> 3Q WARRIORS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"2_1593895_-33_-120\" hidden=\"true\"><span class=\"line\">o33-120</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"left\"></TD><TD>87032</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/3Q WARRIORS TOTAL POINTS.png\"> 3Q WARRIORS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"3_1593895_33_-110\" hidden=\"true\"><span class=\"line\">u33-110</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR><TD COLSPAN=\"8\"></TD></TR></TABLE><BR></center> </div> </div></div><div class=\"footer-container\"> <div class=\"container\"> <span> <i class=\"fa fa-copyright\"></i> <span id=\"currentyear\">2018</span> 504bet.net All Rights Reserved </span> <a class=\"align-right btn btn-link\" href=\"/terms\" target=\"_blank\">Terms &amp; Conditions</a> </div></div></form> <script type=\"text/javascript\" src=\"../scripts/jquery.min.js\"></script> <script type=\"text/javascript\" src=\"../scripts/selector.js\"></script> <script type=\"text/javascript\" src=\"../scripts/teamnames.js\"></script> <script type=\"text/javascript\" src=\"http://arrow.scrolltotop.com/arrow59.js\"></script> <script type=\"text/javascript\"> $(function() { var casinoLink = $('ul.secondary-menu').children('.casino').children('a'); if (casinoLink.length <=0 ) { $('ul.secondary-menu').children('.casino').remove(); } }); </script></body></html>";
			Map<String, String> inputFields = new HashMap<String, String>();
			final List<SiteEventPackage> eventsPackage = bet504TDSportsParser.parseGames(xhtml, null, inputFields);
			
			if (eventsPackage != null && eventsPackage.size() > 0) {
				for (SiteEventPackage siteEventPackage : eventsPackage) {
					System.out.println("SiteEventPackage: " + siteEventPackage);
				}
			}

//			final String tn = "<!DOCTYPE html><html><head id=\"ctl00_Head\"><meta http-equiv=\"cache-control\" content=\"no-cache\" /><meta http-equiv=\"pragma\" content=\"no-cache\" /><meta name=\"robots\" content=\"index,follow\" /><meta name=\"revisit\" content=\"2 days\" /><meta name=\"robots\" content=\"All\" /><meta name=\"Revisit-after\" content=\"2 days\" /><meta name=\"Distribution\" content=\"Global\" /><meta name=\"Copyright\" content=\"504bet.net\" /><meta name=\"HandheldFriendly\" content=\"true\" /><meta name=\"viewport\" content=\"width=600\" /><title>	504bet</title><meta name=\"keywords\" content=\"agents sportsbook, sportsbook for agents, online sportsbook for agents, bettors, sports betting, sports bets, sports wagering, wagering, online gambling, sportsbook, sports odds, internet odds, internet gambling, scores, live odds, sports wager, bet, bets, wagers, odds, sport book, sports book, sports books\" /><meta name=\"description\" content=\"504bet Sportsbook Online Agents offers the latest betting odds NBA and NCAA Basketball, MLB Baseball, NFL Football, Motor Sports. Find attractive Sports Betting Bonuses Promotions Online. Special for agents, credit shop not post up sportsbook. This is an agents sportsbook and sports betting credit shop.\" /><link rel=\"shortcut icon\" href=\"../App_Themes/pm247/images/favicon.ico\" type=\"image/x-icon\" /><link rel=\"icon\" href=\"../App_Themes/pm247/images/favicon.ico\" type=\"image/x-icon\" /><link href=\"../App_Themes/pm247/content/css/font-awesome.min.css\" type=\"text/css\" rel=\"stylesheet\" /><link href=\"../App_Themes/pm247/content/css/jquery.slider.css\" type=\"text/css\" rel=\"stylesheet\" /><link href=\"../App_Themes/pm247/content/css/jquery.slider.min.css\" type=\"text/css\" rel=\"stylesheet\" /><link href=\"../App_Themes/pm247/content/css/theme.min.css\" type=\"text/css\" rel=\"stylesheet\" /></head><body><form name=\"aspnetForm\" method=\"post\" action=\"PostWager.aspx?WT=0\" onkeypress=\"javascript:return WebForm_FireDefaultButton(event, 'ctl00_WagerContent_btn_Continue1')\" id=\"aspnetForm\"><div><input type=\"hidden\" name=\"__EVENTTARGET\" id=\"__EVENTTARGET\" value=\"\" /><input type=\"hidden\" name=\"__EVENTARGUMENT\" id=\"__EVENTARGUMENT\" value=\"\" /><input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"/wEPDwUKLTMxNjc3NTM3NQ8WAh4IV2FnZXJYbWwFyA08eG1sIEN1cnJlbmN5Q29kZT0iVVNEIiBQaXRjaGVyRGVmYXVsdD0iMCIgSWRVc2VyPSIwIiBGcmVlUGxheUF2YWlsYWJsZT0iMC4wMCIgSWRQcm9maWxlPSI0MzIxIiBJZFByb2ZpbGVMaW1pdHM9IjQ4OTkiIElkUGxheWVyPSIxNDU1MTUiIExhc3RJZFdhZ2VyPSIwIiBJZENhbGw9IjU3Mjc1MTc0IiBXYWdlclRyYWNrZXI9IjUzODczNTMyIiBJZkJldEFza0Ftb3VudD0iVHJ1ZSIgQ29uZmlybT0iVHJ1ZSIgRXJyb3JDb2RlPSIwIiBFcnJvck1zZ0tleT0iIiBFcnJvck1zZ1BhcmFtcz0iIiBFcnJvck1zZz0iIiBVc2VGcmVlUGxheUF2YWlsYWJsZT0iRmFsc2UiIFNlY3VyaXR5Q29kZT0iQzMwQzJFRThERThDMzEwRUFBRkFFQUJGRDg3NkFCM0MiIElmQmV0Umlzaz0iMCIgSWZCZXRXaW49IjAiPjx3YWdlciBXYWdlclR5cGVEZXNjPSJTVFJBSUdIVCBCRVQiIFdhZ2VyVHlwZURlc2NFbmc9IlNUUkFJR0hUIEJFVCIgV2FnZXJUeXBlPSIwIiBUaWNrZXROdW1iZXI9IjY0NjQ5MzY3IiBUZWFzZXJDYW5CdXlIYWxmPSIwIiBUZWFzZXJDYW5CdXlPbmU9IjAiIFRlYXNlclBvaW50c1B1cmNoYXNlZD0iMCIgRmlsbElkV2FnZXI9Ii0xIiBBbW91bnQ9IjUwMC4wIiBSaXNrPSI1NzUuMDAiIFdpbj0iNTAwLjAwIiBJRFdUPSIxMzc2MjAiIFJpc2tXaW49IjEiIFJvdW5kUm9iaW5Db21iaW5hdGlvbnM9IjAiIENvbXBhY3RDb21iaW5hdGlvbnM9IiI+PGRldGFpbCBJc01MaW5lPSJGYWxzZSIgVGVhbURlc2NyaXB0aW9uPSJbNTcxM10gVE9UQUwgKDNRIFBPUiBCTEFaRVJTIHZycyAzUSBMQSBMQUtFUlMpIiBUZWFtRGVzY3JpcHRpb25Fbmc9Ils1NzEzXSBUT1RBTCAoM1EgUE9SIEJMQVpFUlMgdnJzIDNRIExBIExBS0VSUykiIFZlcnN1c1RlYW1EZXNjcmlwdGlvbj0iWzU3MTRdIFRPVEFMICgzUSBQT1IgQkxBWkVSUyB2cnMgM1EgTEEgTEFLRVJTKSIgTGluZURlc2NyaXB0aW9uPSJvNTcmYW1wO2ZyYWMxMjstMTE1IiBQcmV2aW91c0xpbmVEZXNjcmlwdGlvbj0iIiBEZXNjcmlwdGlvbj0iWzU3MTNdIFRPVEFMIG81NyZhbXA7ZnJhYzEyOy0xMTUgKDNRIFBPUiBCTEFaRVJTIHZycyAzUSBMQSBMQUtFUlMpIiBEZXNjcmlwdGlvbkVuZz0iWzU3MTNdIFRPVEFMIG81NyZhbXA7ZnJhYzEyOy0xMTUgKDNRIFBPUiBCTEFaRVJTIHZycyAzUSBMQSBMQUtFUlMpIiBJZFNwb3J0PSJOQkEiIElkR2FtZVR5cGU9IjcyIiBQYXJlbnRHYW1lPSIxNTkxODI3IiBGYW1pbHlHYW1lPSIxNTkxODI3IiBJZEV2ZW50PSIwIiBFdmVudERlc2NyaXB0aW9uPSIiIFBlcmlvZD0iNSIgQ2FuQ2hvb3NlUGl0Y2hlcj0iRmFsc2UiIE1hcmtGb3JEZWxldGlvbj0iRmFsc2UiIFBvaW50c1B1cmNoYXNlZEp1aWNlPSIwIiBPcmlnaW5hbE9kZHM9Ii0xMTUiIE9yaWdpbmFsUG9pbnRzPSItNTcuNSIgVmlzaXRvclBpdGNoZXI9IiIgSG9tZVBpdGNoZXI9IiIgR2FtZURhdGVUaW1lPSIwMy0wNS0yMDE4IDIzOjUwOjAwIiBHYW1lRGF0ZT0iTWFyIDA1IiBHYW1lVGltZT0iMTE6NTAgUE0iIElzRmF2b3JpdGU9IlRydWUiIElzTGluZUZyb21BZ2VudD0iRmFsc2UiIElkR2FtZT0iMTU5MjE0MCIgUGxheT0iMiIgUG9pbnRzPSItNTcuNSIgT2Rkcz0iLTExNSIgUGl0Y2hlcj0iMCIgSXNPbk9wZW5CZXRzPSJGYWxzZSIgUG9pbnRzUHVyY2hhc2VkPSIwIiBJc1NwcmVhZD0iRmFsc2UiIElzVG90YWw9IlRydWUiIElzS2V5PSJGYWxzZSIgQ2hhcnRSaXNrPSIwIiBDaGFydFdpbj0iMCIgLz48L3dhZ2VyPjwveG1sPhYCZg9kFgICAw9kFgJmD2QWAgIBD2QWCgIBDw8WAh4EVGV4dAUETUwwMmRkAgUPDxYCHwEFBzU1MCBVU0RkZAIJDw8WAh8BBQoxOSw5NzUgVVNEZGQCDQ8PFgIfAQUHNTc1IFVTRGRkAhEPDxYCHwEFBTAgVVNEZGRkGCDm25zixLwh0BxCe7FD9/df2B4=\" /></div><script type=\"text/javascript\">//<![CDATA[var theForm = document.forms['aspnetForm'];if (!theForm) { theForm = document.aspnetForm;}function __doPostBack(eventTarget, eventArgument) { if (!theForm.onsubmit || (theForm.onsubmit() != false)) { theForm.__EVENTTARGET.value = eventTarget; theForm.__EVENTARGUMENT.value = eventArgument; theForm.submit(); }}//]]></script><script src=\"/WebResource.axd?d=uQSWyjb1nPuIeYg4RgwdMQi3IVixUS2HqG2JbRHviVbWF_KTUe0XjSlBiW3hVEPtK7Vj5FfEWxO4RdVnNVKnzBOwvl81&amp;t=635019837069635093\" type=\"text/javascript\"></script><div>	<input type=\"hidden\" name=\"__EVENTVALIDATION\" id=\"__EVENTVALIDATION\" value=\"/wEWAwL/949gAuqVsosJAuqVsosJ6CCvnTz3UWpga0VV9VoANxkZ+Zw=\" /></div><div class=\"menu-container\"> <div class=\"container content-menu\"> <center> <ul id=\"navmenu\" class=\"menu\"> <li> <div> <span><i class=\"fa fa-user\"></i> </span> <span class=\"info\"><span id=\"ctl00_Content3_AccountFigures3_lblAccount\">ML02</span></span> </div> </li> <li> <div> <span><span id=\"ctl00_Content3_AccountFigures3_LocalizedLabel5\">Balance:</span></span> <span class=\"info\"><span id=\"ctl00_Content3_AccountFigures3_lblCurrentBalance\">550 USD</span></span> </div> </li> <li> <div> <span><span id=\"ctl00_Content3_AccountFigures3_LocalizedLabel6\">Available:</span></span> <span class=\"info\"><span id=\"ctl00_Content3_AccountFigures3_lblRealAvailBalance\">19,975 USD</span></span> </div> </li> <li> <div> <span><span id=\"ctl00_Content3_AccountFigures3_LocalizedLabel8\">Pending:</span></span> <span class=\"info\"><span id=\"ctl00_Content3_AccountFigures3_lblAmountAtRisk\">575 USD</span></span> </div> </li> <li> <div> <span><span id=\"ctl00_Content3_AccountFigures3_LocalizedLabel11\">Free Play:</span></span> <span class=\"info\"><span id=\"ctl00_Content3_AccountFigures3_lblFreePlay\">0 USD</span></span> </div> </li> </ul> </center> </div></div><div class=\"header-container\"> <div class=\"container\"> <a href=\"Welcome.aspx\" class=\"logo\"> <img src=\"../App_themes/pm247/images/logo.png\" style=\"height:105px;width:305px;border-width:0px;\" /> </a> <div class=\"secondary-menu-container\"> <span id=\"ctl00_WagerLnk\"> <ul class=\"secondary-menu\"> <li class=\"has-sub\"><img width=\"20\" height=\"20\" src=\"http://logos.horizonsports.es/icons/football2.png\"><a id=\"ctl00_WagerLnk_lnkSports\" href=\"Sports.aspx\">Sports</a> <ul> <li><a id=\"ctl00_WagerLnk_lnkMatchups\" href=\"../matchups/\" target=\"_blank\">Matchups</a></li> <li><a id=\"ctl00_WagerLnk_lnkSchedule\" href=\"../schedule/\" target=\"_blank\">Schedule</a></li> </ul> </li> <li><img width=\"20\" height=\"20\" src=\"http://logos.horizonsports.es/icons/horses.png\"></li><li><img width=\"20\" height=\"20\" src=\"http://logos.horizonsports.es/icons/livebetting.png\"><a id=\"ctl00_WagerLnk_lnkLiveBet\" href=\"live.aspx?ws=0\"><b>[ExtraLive]</b></a></li> <li class=\"casino\"><img width=\"20\" height=\"20\" src=\"http://logos.horizonsports.es/icons/dice.png\"></li> <li class=\"has-sub\"><img width=\"20\" height=\"20\" src=\"http://logos.horizonsports.es/icons/player2.png\"><a id=\"ctl00_WagerLnk_lnkMyAccount\" href=\"Welcome.aspx\">My Account</a> <ul> <li><a id=\"ctl00_WagerLnk_lnkFillOpen\" href=\"FillChoose.aspx\">Fill Open</a></li> <li><a id=\"ctl00_WagerLnk_lnkOpenBets\" href=\"OpenBets.aspx\">Open Bets</a></li> <li><a id=\"ctl00_WagerLnk_lnkHistory\" href=\"History.aspx\">History</a></li> <li><a id=\"ctl00_WagerLnk_lnkFigures\" href=\"Welcome.aspx\">Figures</a></li> </ul> </li> <li class=\"logout\"><a id=\"ctl00_WagerLnk_lnkLogout\" href=\"../LogOut.aspx\">Logout</a><i class=\"fa fa-sign-out\"></i></li> </ul><ul></ul> </span> </div> </div></div><BR><BR><div class=\"main-menu-container\"> <div class=\"container\"> <ul class=\"nav menu md-menu\"> <li id=\"WT35\" class=\"tab red\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk0\" href=\"CreateSports.aspx?WT=35\">Bet It All</a></span></li> <li id=\"WT0\" class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk1\" href=\"CreateSports.aspx?WT=0\">Straight</a></span></li> <li id=\"WT1\" class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk2\" href=\"CreateSports.aspx?WT=1\">Parlay</a></span></li> <li id=\"WT2\" class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk3\" href=\"ChooseTeaser.aspx?WT=2\">Teaser</a></span></li> <li id=\"WT5\" class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk4\" href=\"CreateSports.aspx?WT=5\">Round Robin</a></span></li> <li id=\"WT3\" class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk5\" href=\"CreateSports.aspx?WT=3\">If Win Only</a></span></li> <li id=\"WT7\" class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk6\" href=\"CreateSports.aspx?WT=7\">If Win or Tie</a></span></li> <li id=\"WT4\" class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk7\" href=\"CreateSports.aspx?WT=4\">Win Reverse</a></span></li> <li id=\"WT8\" class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk8\" href=\"CreateSports.aspx?WT=8\">Action Reverse</a></span></li> <!--li class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk9\" href=\"CreateSports.aspx?WT=15\"><b>[Compact RoundRobin]</b></a></span><span class=\"tabrg\">&nbsp;</span></li> <li class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk10\" href=\"CreateSports.aspx?WT=16\"><b>[Key Parlay]</b></a></span><span class=\"tabrg\">&nbsp;</span></li> <li class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk11\" href=\"CreateSports.aspx?WT=17\"><b>[Compact IfWinOnly]</b></a></span><span class=\"tabrg\">&nbsp;</span></li> <li class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk12\" href=\"CreateSports.aspx?WT=18\"><b>[Compact IfWinOrTie]</b></a></span><span class=\"tabrg\">&nbsp;</span></li--> </ul> </div></div><div class=\"content-container\"> <div class=\"container\"> <div class=\"wagercontent\"> <div class=\"wager-container\" ><table align=\"center\" class=\"main\"><thead><tr class=\"table-header\"><th colspan=\"5\">STRAIGHT BET</th></tr><tr class=\"table-header table-sub-header\"><th width=\"10%\"><span>Date</span></th><th align=\"left\"><span>Team</span></th><th align=\"left\"><span>Risking</span> / <span>To Win</span></th><th align=\"left\"><span>Ticket#:</span></th></tr></thead><TR><TD align=\"center\" class=\"date\"><i class=\"fa fa-clock-o\"></i>Mar 05</TD><TD align=\"left\">NBA [5713] TOTAL o57&frac12;-115 (3Q POR BLAZERS vrs 3Q LA LAKERS) </TD><TD>575.00 USD / 500.00 USD</TD><TD>64649367</TD></TR></table><table align=\"center\" class=\"results-table\"><tr><td colspan=\"5\"><TR><TD ALIGN=\"center\"><TABLE ALIGN=\"center\" class=\"WagerTable2\"><TR><TD width=\"33%\" ALIGN=\"center\" valign=\"top\"><br><a id=\"ctl00_WagerContent_btn_Continue1\" href=\"javascript:__doPostBack('ctl00$WagerContent$btn_Continue1','')\"></a><a id=\"ctl00_WagerContent_btn_Continue1\" href=\"javascript:__doPostBack('ctl00$WagerContent$btn_Continue1','')\">Click Here</a><BR><span>To place another bet.</span></TD><TD width=\"33%\" ALIGN=\"center\" valign=\"top\"><br><a id=\"ctl00_WagerContent_lnkOpenBets\" href=\"OpenBets.aspx\"><span>Click Here</span></a><BR><span>To review your pending plays and to ensure your wagers were placed correctly.</span></TD><TD width=\"33%\" ALIGN=\"center\" valign=\"top\"><BR><a href=\"javascript:window.print();\"><i class=\"fa fa-print\"></i><span>Print Ticket</span></a><BR><span>For Your Insurance.</span></TD></TR></TABLE><BR></TD></TR></td></tr></table></div> </div> </div></div><div class=\"footer-container\"> <div class=\"container\"> <span> <i class=\"fa fa-copyright\"></i> <span id=\"currentyear\">2018</span> 504bet.net All Rights Reserved </span> <a class=\"align-right btn btn-link\" href=\"/terms\" target=\"_blank\">Terms &amp; Conditions</a> </div></div></form> <script type=\"text/javascript\" src=\"../scripts/jquery.min.js\"></script> <script type=\"text/javascript\" src=\"../scripts/selector.js\"></script> <script type=\"text/javascript\" src=\"../scripts/teamnames.js\"></script> <script type=\"text/javascript\" src=\"http://arrow.scrolltotop.com/arrow59.js\"></script> <script type=\"text/javascript\"> $(function() { var casinoLink = $('ul.secondary-menu').children('.casino').children('a'); if (casinoLink.length <=0 ) { $('ul.secondary-menu').children('.casino').remove(); } }); </script></body></html>";
//			String ticketNumber = bet504TDSportsParser.parseTicketNumber(tn);
//			LOGGER.debug("TicketNumber: " + ticketNumber);
		} catch (BatchException be) {
			LOGGER.error(be.getMessage(), be);
		}
	}

	/**
	 * 
	 * @param xhtml
	 * @param type
	 * @param sport
	 * @return
	 * @throws BatchException
	 */
	@Override
	public Map<String, String> parseMenu(String xhtml, String[] type, String[] sport) throws BatchException {
		LOGGER.info("Entering parseMenu()");
		LOGGER.info("type: " + java.util.Arrays.toString(type));
		LOGGER.info("sport: " + java.util.Arrays.toString(sport));
		Map<String, String> map = new HashMap<String, String>();

		for (String sportName : sport) {
			if (sportName.contains("MLB") || sportName.contains("mlb")) {
				isMlb = true;
			}
		}

		// Parse to get the Document
		final Document doc = parseXhtml(xhtml);

		final String[] types = new String[] { "hidden", "submit" };
		final Elements forms = doc.select("form");
		for (int x = 0; (forms != null) && (x < forms.size()); x++) {
			final Element form = forms.get(0);
			if (form != null) {
				// Get form action field
				map.put("action", form.attr("action"));
				getAllElementsByType(form, "input", types, map);
			}			
		}

		// Find the different menu types
		map = findMenu(doc.select(".csb_option"), map, type, sport, "div b", "div input");

		// Remove the input fields that shouldn't be sent
		map = removeInputFields(map);

		// Continue button
		// map.put("ctl00$WagerContent$btn_Continue1", "Continue");

		LOGGER.info("Exiting parseMenu()");
		return map;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseGames(java.lang.String, java.lang.String, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <TDSportsEventPackage> List<TDSportsEventPackage> parseGames(String xhtml, String type, Map<String, String> inputFields) throws BatchException {
		LOGGER.info("Entering parseGames()");
		LOGGER.debug("xhtml: " + xhtml);
		LOGGER.debug("type: " + type);
		LOGGER.debug("InputFields: " + inputFields);
		List<?> events = null;
		
		// Parse to get the Document
		final Document doc = parseXhtml(xhtml);
		Map<String, String> tempFields = new HashMap<String, String>();

		final String[] types = new String[] { "hidden", "submit" };
		final Elements forms = doc.select("form");
		for (int x = 0; (forms != null) && (x < forms.size()); x++) {
			final Element form = forms.get(0);
			if (form != null) {
				// Get form action field
				tempFields.put("action", form.attr("action"));

				// Get all input elements with hidden and submit types
				getAllElementsByType(form, "input", types, tempFields);
				
				Elements elements = form.select("input");
				for (int y = 0; (elements != null) && (y < elements.size()); y++) {
					final Element input = elements.get(y);
					String nameValue = input.attr("name");
					String typeValue = input.attr("type");
					if (nameValue != null && typeValue != null && "checkbox".equals(typeValue)) {
						nameValue = nameValue.trim();
						if (nameValue.contains("linesel_")) {
							tempFields.put(nameValue, "on");
						}
					}
				}
			}
		}

		// Now get the game data
		Elements elements = doc.select(".GameLeague .Competition");
		if (elements != null && elements.size() > 0) {
			events = getGameData(elements);
		}

		// Remove the input fields that shouldn't be sent
		tempFields = removeInputFields(tempFields);
		if (tempFields != null && !tempFields.isEmpty()) {
			final Set<Entry<String, String>> indexs = tempFields.entrySet();
			if (indexs != null && !indexs.isEmpty()) {
				final Iterator<Entry<String, String>> itr = indexs.iterator();
				while (itr != null && itr.hasNext()) {
					final Entry<String, String> values = itr.next();
					String key = values.getKey();
					String value = values.getValue();
					LOGGER.info("KEY: " + key);
					LOGGER.info("VALUE: " + value);

					if (key != null && value != null && value.length() > 0) {
						inputFields.put(key, value);
					}
				}
			}
		}

		LOGGER.info("Exiting parseGames()");
		return (List<TDSportsEventPackage>)events;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.tdsports.TDSportsParser#parseTicketNumber(java.lang.String)
	 */
	@Override
	public String parseTicketNumber(String xhtml) throws BatchException {
		LOGGER.info("Entering parseTicketNumber()");
		String ticketNumber = "";

		final Document doc = parseXhtml(xhtml);
		final Elements tds = doc.select(".tdTS");
		if (tds != null && tds.size() > 0) {
			final Element td = tds.get(0);
			LOGGER.debug("td: " + td);
			ticketNumber = td.html();
			LOGGER.debug("ticketNumber: " + ticketNumber);
			if (ticketNumber != null) {
				ticketNumber = ticketNumber.trim();
			} else {
				ticketNumber = "Failed to get ticket number";
				throw new BatchException(BatchErrorCodes.FAILED_TO_SETUP_TRANSACTION_CORRECTLY,
						BatchErrorMessage.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, ticketNumber, xhtml);
			}
		} else {
			ticketNumber = "Failed to get ticket number";
			throw new BatchException(BatchErrorCodes.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, BatchErrorMessage.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, ticketNumber, xhtml);
		}

		LOGGER.info("Exiting parseTicketNumber()");
		return ticketNumber;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#getFootballData(org.jsoup.select.Elements)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected List<TDSportsEventPackage> getGameData(Elements elements) throws BatchException {
		LOGGER.info("Entering getGameData()");
		TDSportsEventPackage eventPackage = null;
		final List<TDSportsEventPackage> events = new ArrayList<TDSportsEventPackage>();

		if (elements != null) {
			TDSportsTeamPackage team1 = null;
			TDSportsTeamPackage team2 = null;

			for (int x = 0; x < elements.size(); x++) {
				boolean hasLinkedEvents = false;

				// Loop through the elements and then check for the dates
				final Element element = elements.get(x);
				final String classInfo = element.attr("class");
				final String elementhtml = element.html();
				if (elementhtml != null && elementhtml.contains("LinkedEvents")) {
					hasLinkedEvents = true;
				}
				LOGGER.debug("hasLinkedEvents: " + hasLinkedEvents);

				final Elements gameRows = element.select(".GameRow");
				if (gameRows != null && gameRows.size() > 0) {
					int t = 0;
					LOGGER.debug("gameRows.size(): " + gameRows.size());
					for (Element gameRow : gameRows) {
						final String gameRowHtml = gameRow.html();
						if ((hasLinkedEvents && 
							!gameRowHtml.contains("1H ") &&
							!gameRowHtml.contains("2H ") &&
							!gameRowHtml.contains("1P ") &&
							!gameRowHtml.contains("2P ") &&
							!gameRowHtml.contains("3P ") && 
							!gameRowHtml.contains("1Q ") &&
							!gameRowHtml.contains("2Q ") &&
							!gameRowHtml.contains("3Q ") && 
							!gameRowHtml.contains("4Q ") &&
							!gameRowHtml.contains("TEAM TOTAL")) || 
							!hasLinkedEvents) {
							LOGGER.debug("gameRowHtml: " + gameRowHtml);
							if (t++ == 0) {
								eventPackage = new TDSportsEventPackage();
								team1 = new TDSportsTeamPackage();
								getTeamData(gameRow.select("div"), team1);
								eventPackage.setId(team1.getId());
							} else {
								team2 = new TDSportsTeamPackage();
								getTeamData(gameRow.select("div"), team2);
								Date eventDate = null;
								eventDate = setupDate(DATE_FORMAT, team1.gettDate(), team2.gettDate());

								team1.setEventdatetime(eventDate);
								team2.setEventdatetime(eventDate);
								final String dateOfEvent = team1.gettDate() + " " + team2.gettDate();
								team1.settDate(dateOfEvent);
								team2.settDate(dateOfEvent);
								eventPackage.setDateofevent(dateOfEvent);

								LOGGER.debug("team1: " + team1);
								LOGGER.debug("team2: " + team2);
								eventPackage.setSiteteamone(team1);
								eventPackage.setSiteteamtwo(team2);
								eventPackage.setTeamone(team1);
								eventPackage.setTeamtwo(team2);
								events.add(eventPackage);
								t = 0;
							}
						}
					}
				}
			}
		}
		LOGGER.debug("EventPackage: " + eventPackage);

		// Reset
		isMlb = false;

		LOGGER.info("Exiting getGameData()");
		return events;
	}

	/**
	 * 
	 * @param div
	 * @param select
	 * @param sport
	 * @return
	 */
	protected boolean foundSport(Element div, String select, String type, String sport) {
		LOGGER.info("Entering foundSport()");
		LOGGER.debug("div: " + div);
		LOGGER.debug("select: " + select);
		LOGGER.debug("type: " + type);
		LOGGER.debug("sport: " + sport);
		boolean foundDiv = false;

		String b = getHtmlFromElement(div, select, 0, false);
		if (b != null && b.length() > 0) {
			b = b.trim();
			LOGGER.debug("b: " + b);
			// Check if we found div
			if (b.equals(type)) {
				foundDiv = true;
			} else if (type.startsWith("NFL - WEEK") && b.startsWith("NFL - WEEK")) { // HACK!!!
				foundDiv = true;
			}
		}

		LOGGER.info("Exiting foundSport()");
		return foundDiv;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.tdsports.TDSportsParser#getTeamData(org.jsoup.select.Elements, com.ticketadvantage.services.dao.sites.tdsports.TDSportsTeamPackage)
	 */
	@Override
	protected int getTeamData(Elements elements, TDSportsTeamPackage team) {
		LOGGER.info("Entering getTeamData()");
		int size = 0;

		if (isMlb) {
			parseMlb(elements, team);
			LOGGER.debug("team: " + team);
		} else {
			size = 0;
			for (int x = 0; (elements != null && x < elements.size()); x++) {
				final Element td = elements.get(x);
				final String cname = td.attr("class");
				if (cname.contains("col-xs-")) {
					size++;
					switch (size) {
					case 1:
						team = getDate(td, team);
						break;
					case 2:
						team = getEventId(td, team);
						break;
					case 3:
						team = getTeam(td, team);
						break;
					case 5:
						team = getSpread(td, team);
						break;
					case 6:
						team = getTotal(td, team);
						break;
					case 7:
						team = getMoneyLine(td, team);
						break;
					case 4:
					default:
						break;
					}
				}
			}
		}

		LOGGER.info("Exiting getTeamData()");
		return size;
	}

	/**
	 * 
	 * @param elements
	 * @param team
	 */
	private int parseMlb(Elements elements, TDSportsTeamPackage team) {
		LOGGER.info("Entering parseMlb()");

			int size = 7;
			int x = 0;
			for (int y = 0;(elements != null && y < elements.size()); y++) {
				final Element td = elements.get(y);
				if (td.attr("class").contains("col-xs-")) {
					switch (x) {
						case 0:
							team = getDate(td, team);
							break;
						case 1:
							team = getEventId(td, team);
							break;
						case 2:
							team = getTeam(td, team);
							break;
						case 4:
							team = getMoneyLine(td, team);
							break;
						case 5:
							team = getTotal(td, team);
							break;
						case 6:
							team = getSpread(td, team);
							break;
						case 3:
						default:
							break;
					}
					x++;
				}
			}

		LOGGER.info("Exiting parseMlb()");
		return size;
	}

	/**
	 * 
	 * @param td
	 * @param team
	 * @return
	 */
	protected TDSportsTeamPackage getDate(Element td, TDSportsTeamPackage team) {
		LOGGER.info("Entering getDate()");

		// Date String
		final String date = td.html().trim();
		LOGGER.debug("date: " + date);
		team.settDate(date);

		LOGGER.info("Exiting getDate()");
		return team;
	}

	/**
	 * 
	 * @param td
	 * @param team
	 * @return
	 */
	protected TDSportsTeamPackage getEventId(Element td, TDSportsTeamPackage team) {
		LOGGER.info("Entering getEventId()");

		final String eventId = td.html();
		if (eventId != null) {
			LOGGER.debug("eventId: " + eventId);
			team.setEventid(eventId.trim());
			team.setId(Integer.parseInt(eventId.trim()));
		}

		LOGGER.info("Exiting getEventId()");
		return team;
	}

	/**
	 * 
	 * @param td
	 * @param team
	 * @return
	 */
	protected TDSportsTeamPackage getTeam(Element td, TDSportsTeamPackage team) {
		LOGGER.info("Entering getTeam()");

		// Team String
		final Elements spans = td.select(".Team");
		if (spans != null && spans.size() > 0) {
			final Element span = spans.get(0);
			String teamName = span.html().trim();
			if (isMlb) {
//				PIT PIRATES
//				<small class="text-normal-xs text-bold-sm">[JAMESON TAILLON - R]</small>
				int index = teamName.indexOf("<small");
				if (index != -1) {
					String teamn = teamName.substring(0, index).trim();
					teamName = teamName.substring(index + 6);
					index = teamName.indexOf("[");
					if (index != -1) {
						teamName = teamName.substring(index + 1);
						index = teamName.indexOf("]");
						if (index != -1) {
							teamName = teamName.substring(0, index).trim();
							team.setTeam(teamn + " (" + teamName + ")");
						}
					}
				}
			} else {
				LOGGER.debug("TeamName: " + teamName);
				team.setTeam(teamName);
			}
		}

		LOGGER.info("Exiting getTeam()");
		return team;
	}

	/**
	 * 
	 * @param td
	 * @param team
	 * @return
	 */
	protected TDSportsTeamPackage getSpread(Element td, TDSportsTeamPackage team) {
		LOGGER.info("Entering getSpread()");

		// First get the input field information
		final Map<String, String> hashMap = parseInputField(td.select("input"), 0);
		team.setGameSpreadInputId(hashMap.get("id"));
		team.setGameSpreadInputName(hashMap.get("name"));
		team.setGameSpreadInputValue(hashMap.get("value"));

		final String spread = parseDivOut(td);

		// Setup spread
		team.addGameSpreadOptionValue("0", "0");
		team = (TDSportsTeamPackage)parseSpreadData(reformatValues(spread), 0, " ", null, team);

		LOGGER.info("Exiting getSpread()");
		return team;
	}

	/**
	 * 
	 * @param td
	 * @param team
	 * @return
	 */
	protected TDSportsTeamPackage getTotal(Element td, TDSportsTeamPackage team) {
		LOGGER.info("Entering getTotal()");

		// First get the input field information
		final Map<String, String> hashMap = parseInputField(td.select("input"), 0);
		team.setGameTotalInputId(hashMap.get("id"));
		team.setGameTotalInputName(hashMap.get("name"));
		team.setGameTotalInputValue(hashMap.get("value"));

		final String total = parseDivOut(td);

		// Parse Total Data
		team.addGameTotalOptionValue("0", "0");
		team = (TDSportsTeamPackage)parseTotalData(reformatValues(total), 0, " ", null, team);

		LOGGER.info("Exiting getTotal()");
		return team;
	}

	/**
	 * 
	 * @param td
	 * @param team
	 * @return
	 */
	protected TDSportsTeamPackage getMoneyLine(Element td, TDSportsTeamPackage team) {
		LOGGER.info("Entering getMoneyLine()");
		
		// First get the input field information
		final Map<String, String> hashMap = parseInputField(td.select("input"), 0);
		team.setGameMLInputId(hashMap.get("id"));
		team.setGameMLInputName(hashMap.get("name"));
		team.addGameMLInputValue("0", hashMap.get("value"));

		// -110; Now parse the data
		final String ml = parseDivOut(td);

		// Parse for Money Line
		team = (TDSportsTeamPackage)parseMlData(reformatValues(ml), 0, team);

		LOGGER.info("Exiting getMoneyLine()");
		return team;
	}

	/**
	 * 
	 * @param td
	 * @return
	 */
	private String parseDivOut(Element td) {
		LOGGER.info("Entering parseDivOut()");
		String retValue = null;
		
		final Elements divs = td.select(".OddsDynamicCheck");
		if (divs != null && divs.size() > 0) {
			final Element div = divs.get(0);
			retValue = div.html().trim();
			LOGGER.debug("retValue: " + retValue);
		} else {
			LOGGER.debug("why the else?");
		}

		LOGGER.info("Entering parseDivOut()");
		return retValue;
	}
	
	/**
	 * 
	 * @param td
	 * @return
	 */
	private String parseIconOut(Element td) {
		LOGGER.info("Entering parseIconOut()");
		LOGGER.debug("Element: " + td);
		String retValue = null;
		
		String html = td.html();
		if (html != null && html.length() > 0) {
			int index = html.indexOf("</i>");
			if (index != -1) {
				retValue = html.substring(index + "</i>".length()).trim();
			} else {
				retValue = html.trim();
			}
		}

		LOGGER.info("Entering parseIconOut()");
		return retValue;
	}
}