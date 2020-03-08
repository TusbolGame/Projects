/**
 * 
 */
package com.ticketadvantage.services.dao.sites.tdsportsthree;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ibm.icu.util.Calendar;
import com.ticketadvantage.services.dao.sites.SiteEventPackage;
import com.ticketadvantage.services.dao.sites.tdsports.TDSportsEventPackage;
import com.ticketadvantage.services.dao.sites.tdsports.TDSportsParser;
import com.ticketadvantage.services.dao.sites.tdsports.TDSportsTeamPackage;
import com.ticketadvantage.services.email.AccessTokenFromRefreshToken;
import com.ticketadvantage.services.email.SendText;
import com.ticketadvantage.services.errorhandling.AppErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.PendingEvent;

/**
 * @author jmiller
 *
 */
public class TDSportsThreeParser extends TDSportsParser {
	private static final Logger LOGGER = Logger.getLogger(TDSportsThreeParser.class);
	private static final SimpleDateFormat SITE_DATE_FORMAT = new SimpleDateFormat("MMM dd hh:mm a z");

	/**
	 * Constructor
	 */
	public TDSportsThreeParser() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final TDSportsThreeParser names777TDSportsParser = new TDSportsThreeParser();
			String xhtml = "<!DOCTYPE html><html><head id=\"ctl00_Head\"><meta http-equiv=\"cache-control\" content=\"no-cache\" /><meta http-equiv=\"pragma\" content=\"no-cache\" /><meta name=\"robots\" content=\"index,follow\" /><meta name=\"revisit\" content=\"2 days\" /><meta name=\"robots\" content=\"All\" /><meta name=\"Revisit-after\" content=\"2 days\" /><meta name=\"Distribution\" content=\"Global\" /><meta name=\"Copyright\" content=\"504bet.net\" /><meta name=\"HandheldFriendly\" content=\"true\" /><meta name=\"viewport\" content=\"width=600\" /><title>	504bet</title><meta name=\"keywords\" content=\"agents sportsbook, sportsbook for agents, online sportsbook for agents, bettors, sports betting, sports bets, sports wagering, wagering, online gambling, sportsbook, sports odds, internet odds, internet gambling, scores, live odds, sports wager, bet, bets, wagers, odds, sport book, sports book, sports books\" /><meta name=\"description\" content=\"504bet Sportsbook Online Agents offers the latest betting odds NBA and NCAA Basketball, MLB Baseball, NFL Football, Motor Sports. Find attractive Sports Betting Bonuses Promotions Online. Special for agents, credit shop not post up sportsbook. This is an agents sportsbook and sports betting credit shop.\" /><link rel=\"shortcut icon\" href=\"../App_Themes/pm247/images/favicon.ico\" type=\"image/x-icon\" /><link rel=\"icon\" href=\"../App_Themes/pm247/images/favicon.ico\" type=\"image/x-icon\" /><link href=\"../App_Themes/pm247/content/css/font-awesome.min.css\" type=\"text/css\" rel=\"stylesheet\" /><link href=\"../App_Themes/pm247/content/css/jquery.slider.css\" type=\"text/css\" rel=\"stylesheet\" /><link href=\"../App_Themes/pm247/content/css/jquery.slider.min.css\" type=\"text/css\" rel=\"stylesheet\" /><link href=\"../App_Themes/pm247/content/css/theme.min.css\" type=\"text/css\" rel=\"stylesheet\" /></head><body><form name=\"aspnetForm\" method=\"post\" action=\"Schedule.aspx?WT=0&amp;lg=78\" id=\"aspnetForm\"><div><input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"/wEPDwUKLTMxNjc3NTM3NQ9kFgJmD2QWAgIDD2QWAmYPZBYCAgEPZBYKAgEPDxYCHgRUZXh0BQRNTDAyZGQCBQ8PFgIfAAUHMzAwIFVTRGRkAgkPDxYCHwAFCjE4LDY1MCBVU0RkZAINDw8WAh8ABQkxLDY1MCBVU0RkZAIRDw8WAh8ABQUwIFVTRGRkZHubgJBv1maHWbJvZid9fwAxRN2b\" /></div><div>	<input type=\"hidden\" name=\"__EVENTVALIDATION\" id=\"__EVENTVALIDATION\" value=\"/wEWAgKo2ZGICQLqlbKLCa/Xgy3mkKYkPqjIcMhFxttlzNhu\" /></div><div class=\"menu-container\"> <div class=\"container content-menu\"> <center> <ul id=\"navmenu\" class=\"menu\"> <li> <div> <span><i class=\"fa fa-user\"></i> </span> <span class=\"info\"><span id=\"ctl00_Content3_AccountFigures3_lblAccount\">ML02</span></span> </div> </li> <li> <div> <span><span id=\"ctl00_Content3_AccountFigures3_LocalizedLabel5\">Balance:</span></span> <span class=\"info\"><span id=\"ctl00_Content3_AccountFigures3_lblCurrentBalance\">300 USD</span></span> </div> </li> <li> <div> <span><span id=\"ctl00_Content3_AccountFigures3_LocalizedLabel6\">Available:</span></span> <span class=\"info\"><span id=\"ctl00_Content3_AccountFigures3_lblRealAvailBalance\">18,650 USD</span></span> </div> </li> <li> <div> <span><span id=\"ctl00_Content3_AccountFigures3_LocalizedLabel8\">Pending:</span></span> <span class=\"info\"><span id=\"ctl00_Content3_AccountFigures3_lblAmountAtRisk\">1,650 USD</span></span> </div> </li> <li> <div> <span><span id=\"ctl00_Content3_AccountFigures3_LocalizedLabel11\">Free Play:</span></span> <span class=\"info\"><span id=\"ctl00_Content3_AccountFigures3_lblFreePlay\">0 USD</span></span> </div> </li> </ul> </center> </div></div><div class=\"header-container\"> <div class=\"container\"> <a href=\"Welcome.aspx\" class=\"logo\"> <img src=\"../App_themes/pm247/images/logo.png\" style=\"height:105px;width:305px;border-width:0px;\" /> </a> <div class=\"secondary-menu-container\"> <span id=\"ctl00_WagerLnk\"> <ul class=\"secondary-menu\"> <li class=\"has-sub\"><img width=\"20\" height=\"20\" src=\"http://logos.horizonsports.es/icons/football2.png\"><a id=\"ctl00_WagerLnk_lnkSports\" href=\"Sports.aspx\">Sports</a> <ul> <li><a id=\"ctl00_WagerLnk_lnkMatchups\" href=\"../matchups/\" target=\"_blank\">Matchups</a></li> <li><a id=\"ctl00_WagerLnk_lnkSchedule\" href=\"../schedule/\" target=\"_blank\">Schedule</a></li> </ul> </li> <li><img width=\"20\" height=\"20\" src=\"http://logos.horizonsports.es/icons/horses.png\"></li><li><img width=\"20\" height=\"20\" src=\"http://logos.horizonsports.es/icons/livebetting.png\"><a id=\"ctl00_WagerLnk_lnkLiveBet\" href=\"live.aspx?ws=0\"><b>[ExtraLive]</b></a></li> <li class=\"casino\"><img width=\"20\" height=\"20\" src=\"http://logos.horizonsports.es/icons/dice.png\"></li> <li class=\"has-sub\"><img width=\"20\" height=\"20\" src=\"http://logos.horizonsports.es/icons/player2.png\"><a id=\"ctl00_WagerLnk_lnkMyAccount\" href=\"Welcome.aspx\">My Account</a> <ul> <li><a id=\"ctl00_WagerLnk_lnkFillOpen\" href=\"FillChoose.aspx\">Fill Open</a></li> <li><a id=\"ctl00_WagerLnk_lnkOpenBets\" href=\"OpenBets.aspx\">Open Bets</a></li> <li><a id=\"ctl00_WagerLnk_lnkHistory\" href=\"History.aspx\">History</a></li> <li><a id=\"ctl00_WagerLnk_lnkFigures\" href=\"Welcome.aspx\">Figures</a></li> </ul> </li> <li class=\"logout\"><a id=\"ctl00_WagerLnk_lnkLogout\" href=\"../LogOut.aspx\">Logout</a><i class=\"fa fa-sign-out\"></i></li> </ul><ul></ul> </span> </div> </div></div><BR><BR><div class=\"main-menu-container\"> <div class=\"container\"> <ul class=\"nav menu md-menu\"> <li id=\"WT35\" class=\"tab red\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk0\" href=\"CreateSports.aspx?WT=35\">Bet It All</a></span></li> <li id=\"WT0\" class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk1\" href=\"CreateSports.aspx?WT=0\">Straight</a></span></li> <li id=\"WT1\" class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk2\" href=\"CreateSports.aspx?WT=1\">Parlay</a></span></li> <li id=\"WT2\" class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk3\" href=\"ChooseTeaser.aspx?WT=2\">Teaser</a></span></li> <li id=\"WT5\" class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk4\" href=\"CreateSports.aspx?WT=5\">Round Robin</a></span></li> <li id=\"WT3\" class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk5\" href=\"CreateSports.aspx?WT=3\">If Win Only</a></span></li> <li id=\"WT7\" class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk6\" href=\"CreateSports.aspx?WT=7\">If Win or Tie</a></span></li> <li id=\"WT4\" class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk7\" href=\"CreateSports.aspx?WT=4\">Win Reverse</a></span></li> <li id=\"WT8\" class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk8\" href=\"CreateSports.aspx?WT=8\">Action Reverse</a></span></li> <!--li class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk9\" href=\"CreateSports.aspx?WT=15\"><b>[Compact RoundRobin]</b></a></span><span class=\"tabrg\">&nbsp;</span></li> <li class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk10\" href=\"CreateSports.aspx?WT=16\"><b>[Key Parlay]</b></a></span><span class=\"tabrg\">&nbsp;</span></li> <li class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk11\" href=\"CreateSports.aspx?WT=17\"><b>[Compact IfWinOnly]</b></a></span><span class=\"tabrg\">&nbsp;</span></li> <li class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk12\" href=\"CreateSports.aspx?WT=18\"><b>[Compact IfWinOrTie]</b></a></span><span class=\"tabrg\">&nbsp;</span></li--> </ul> </div></div><div class=\"content-container\"> <div class=\"container\"> <div class=\"wagercontent\"> <center ><div class=\"header-lines-container\"><span class=\"league-header\"><img class=\"league-logo\" src=\"http://logos.horizonsports.es/leagues/NBA - 2ND HALVES.png\"><span class=\"league-name\">NBA - 2ND HALVES</span></span><span class=\"options\"><input type=\"submit\" name=\"ctl00$WagerContent$btn_Continue1\" value=\"Continue\" id=\"ctl00_WagerContent_btn_Continue1\" class=\"btn btn-default\" /><button type=\"submit\" class=\"btn btn-refresh\"><span>Refresh</span><i class=\"fa fa-refresh\"></i></button></span></div><TABLE CELLPADDING=\"0\" CELLSPACING=\"0\" BORDER=\"0\" WIDTH=\"100%\" ALIGN=\"center\"><TR CLASS=\"GameHeader\"><TD WIDTH=\"3%\"></TD><TD WIDTH=\"5%\">#</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><span>Team</span></TD><TD nowrap ALIGN=\"center\" WIDTH=\"20%\"><span>Spread</span></TD><TD nowrap ALIGN=\"center\" WIDTH=\"20%\"><span>Total</span></TD><TD nowrap ALIGN=\"center\" WIDTH=\"20%\"><span>M Line</span></TD></TR><TR ALIGN=\"left\" class=\"GameHeader gameBanner\"><TD COLSPAN=\"8\">NBA - 2ND HALVES Second Half Lines - Mar 06</TD></TR><TR ALIGN=\"left\" class=\"GameHeader gameBanner\"><TD COLSPAN=\"8\">NO PELICANS 67 - LA CLIPPERS 52</TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"right\" class=\"date\" COLSPAN=\"7\"><i class=\"fa fa-clock-o\"></i>Mar 06 11:52 PM </TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"left\"></TD><TD>2563</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/2H NO PELICANS.png\"> 2H NO PELICANS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"0_1593137_6.5_-110\" hidden=\"true\"><span class=\"line\">+6&frac12;-110</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"2_1593137_-113.5_-110\" hidden=\"true\"><span class=\"line\">o113&frac12;-110</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"4_1593137_0_335\" hidden=\"true\"><span class=\"line\">+335</span></label></div></TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"left\"></TD><TD>2514</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/2H LA CLIPPERS.png\"> 2H LA CLIPPERS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"1_1593137_-6.5_-110\" hidden=\"true\"><span class=\"line\">-6&frac12;-110</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"3_1593137_113.5_-110\" hidden=\"true\"><span class=\"line\">u113&frac12;-110</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"5_1593137_0_-425\" hidden=\"true\"><span class=\"line\">-425</span></label></div></TD></TR><TR ALIGN=\"left\" class=\"GameHeader gameBanner\"><TD COLSPAN=\"8\">BRO NETS 53 - GS WARRIORS 48</TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"right\" class=\"date\" COLSPAN=\"7\"><i class=\"fa fa-clock-o\"></i>Mar 06 11:49 PM </TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"left\"></TD><TD>2515</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/2H BRO NETS.png\"> 2H BRO NETS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"0_1593145_13.5_-110\" hidden=\"true\"><span class=\"line\">+13&frac12;-110</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"2_1593145_-119_-110\" hidden=\"true\"><span class=\"line\">o119-110</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"left\"></TD><TD>2516</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/2H GS WARRIORS.png\"> 2H GS WARRIORS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"1_1593145_-13.5_-110\" hidden=\"true\"><span class=\"line\">-13&frac12;-110</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"3_1593145_119_-110\" hidden=\"true\"><span class=\"line\">u119-110</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR ALIGN=\"left\" class=\"GameHeader gameBanner\"><TD COLSPAN=\"8\">NBA - 2ND HALVES Third Quarter Lines - Mar 07</TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"right\" class=\"date\" COLSPAN=\"7\"><i class=\"fa fa-clock-o\"></i>Mar 07 12:00 AM </TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"left\"></TD><TD>5563</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/3Q NO PELICANS.png\"> 3Q NO PELICANS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"0_1593141_3_-115\" hidden=\"true\"><span class=\"line\">+3-115</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"2_1593141_-57_-120\" hidden=\"true\"><span class=\"line\">o57-120</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"left\"></TD><TD>5514</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/3Q LA CLIPPERS.png\"> 3Q LA CLIPPERS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"1_1593141_-3_-115\" hidden=\"true\"><span class=\"line\">-3-115</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"3_1593141_57_-110\" hidden=\"true\"><span class=\"line\">u57-110</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"right\" class=\"date\" COLSPAN=\"7\"><i class=\"fa fa-clock-o\"></i>Mar 06 11:49 PM </TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"left\"></TD><TD>5515</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/3Q BRO NETS.png\"> 3Q BRO NETS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"0_1593149_6.5_100\" hidden=\"true\"><span class=\"line\">+6&frac12;EV</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"2_1593149_-60_-105\" hidden=\"true\"><span class=\"line\">o60-105</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"left\"></TD><TD>5516</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/3Q GS WARRIORS.png\"> 3Q GS WARRIORS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"1_1593149_-6.5_-130\" hidden=\"true\"><span class=\"line\">-6&frac12;-130</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"3_1593149_60_-125\" hidden=\"true\"><span class=\"line\">u60-125</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR ALIGN=\"left\" class=\"GameHeader gameBanner\"><TD COLSPAN=\"8\">NBA - 2ND HALVES Second Half Lines - Mar 07</TD></TR><TR ALIGN=\"left\" class=\"GameHeader gameBanner\"><TD COLSPAN=\"8\">NBA 2ND HALF TEAM TOTALS</TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"right\" class=\"date\" COLSPAN=\"7\"><i class=\"fa fa-clock-o\"></i>Mar 07 12:00 AM </TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"left\"></TD><TD>84025</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/2H PELICANS TOTAL POINTS.png\"> 2H PELICANS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"2_1593887_-53.5_-125\" hidden=\"true\"><span class=\"line\">o53&frac12;-125</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"left\"></TD><TD>84026</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/2H PELICANS TOTAL POINTS.png\"> 2H PELICANS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"3_1593887_53.5_-105\" hidden=\"true\"><span class=\"line\">u53&frac12;-105</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"right\" class=\"date\" COLSPAN=\"7\"><i class=\"fa fa-clock-o\"></i>Mar 07 12:00 AM </TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"left\"></TD><TD>84027</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/2H CLIPPERS TOTAL POINTS.png\"> 2H CLIPPERS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"2_1593889_-60_-115\" hidden=\"true\"><span class=\"line\">o60-115</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"left\"></TD><TD>84028</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/2H CLIPPERS TOTAL POINTS.png\"> 2H CLIPPERS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"3_1593889_60_-115\" hidden=\"true\"><span class=\"line\">u60-115</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"right\" class=\"date\" COLSPAN=\"7\"><i class=\"fa fa-clock-o\"></i>Mar 06 11:49 PM </TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"left\"></TD><TD>84029</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/2H NETS TOTAL POINTS.png\"> 2H NETS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"2_1593891_-53_-120\" hidden=\"true\"><span class=\"line\">o53-120</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"left\"></TD><TD>84030</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/2H NETS TOTAL POINTS.png\"> 2H NETS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"3_1593891_53_-110\" hidden=\"true\"><span class=\"line\">u53-110</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"right\" class=\"date\" COLSPAN=\"7\"><i class=\"fa fa-clock-o\"></i>Mar 06 11:49 PM </TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"left\"></TD><TD>84031</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/2H WARRIORS TOTAL POINTS.png\"> 2H WARRIORS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"2_1593893_-66.5_-115\" hidden=\"true\"><span class=\"line\">o66&frac12;-115</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"left\"></TD><TD>84032</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/2H WARRIORS TOTAL POINTS.png\"> 2H WARRIORS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"3_1593893_66.5_-115\" hidden=\"true\"><span class=\"line\">u66&frac12;-115</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR ALIGN=\"left\" class=\"GameHeader gameBanner\"><TD COLSPAN=\"8\">NBA - 2ND HALVES Third Quarter Lines - Mar 07</TD></TR><TR ALIGN=\"left\" class=\"GameHeader gameBanner\"><TD COLSPAN=\"8\">NBA 3RD QUARTER TEAM TOTALS</TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"right\" class=\"date\" COLSPAN=\"7\"><i class=\"fa fa-clock-o\"></i>Mar 07 12:00 AM </TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"left\"></TD><TD>87025</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/3Q PELICANS TOTAL POINTS.png\"> 3Q PELICANS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"2_1593888_-27_-115\" hidden=\"true\"><span class=\"line\">o27-115</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"left\"></TD><TD>87026</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/3Q PELICANS TOTAL POINTS.png\"> 3Q PELICANS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"3_1593888_27_-115\" hidden=\"true\"><span class=\"line\">u27-115</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"right\" class=\"date\" COLSPAN=\"7\"><i class=\"fa fa-clock-o\"></i>Mar 07 12:00 AM </TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"left\"></TD><TD>87027</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/3Q CLIPPERS TOTAL POINTS.png\"> 3Q CLIPPERS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"2_1593890_-30_-115\" hidden=\"true\"><span class=\"line\">o30-115</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"left\"></TD><TD>87028</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/3Q CLIPPERS TOTAL POINTS.png\"> 3Q CLIPPERS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"3_1593890_30_-115\" hidden=\"true\"><span class=\"line\">u30-115</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"right\" class=\"date\" COLSPAN=\"7\"><i class=\"fa fa-clock-o\"></i>Mar 06 11:49 PM </TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"left\"></TD><TD>87029</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/3Q NETS TOTAL POINTS.png\"> 3Q NETS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"2_1593892_-27_-110\" hidden=\"true\"><span class=\"line\">o27-110</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"left\"></TD><TD>87030</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/3Q NETS TOTAL POINTS.png\"> 3Q NETS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"3_1593892_27_-120\" hidden=\"true\"><span class=\"line\">u27-120</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"right\" class=\"date\" COLSPAN=\"7\"><i class=\"fa fa-clock-o\"></i>Mar 06 11:49 PM </TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"left\"></TD><TD>87031</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/3Q WARRIORS TOTAL POINTS.png\"> 3Q WARRIORS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"2_1593895_-33_-120\" hidden=\"true\"><span class=\"line\">o33-120</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"left\"></TD><TD>87032</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/3Q WARRIORS TOTAL POINTS.png\"> 3Q WARRIORS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"3_1593895_33_-110\" hidden=\"true\"><span class=\"line\">u33-110</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR><TD COLSPAN=\"8\"></TD></TR></TABLE><BR></center> </div> </div></div><div class=\"footer-container\"> <div class=\"container\"> <span> <i class=\"fa fa-copyright\"></i> <span id=\"currentyear\">2018</span> 504bet.net All Rights Reserved </span> <a class=\"align-right btn btn-link\" href=\"/terms\" target=\"_blank\">Terms &amp; Conditions</a> </div></div></form> <script type=\"text/javascript\" src=\"../scripts/jquery.min.js\"></script> <script type=\"text/javascript\" src=\"../scripts/selector.js\"></script> <script type=\"text/javascript\" src=\"../scripts/teamnames.js\"></script> <script type=\"text/javascript\" src=\"http://arrow.scrolltotop.com/arrow59.js\"></script> <script type=\"text/javascript\"> $(function() { var casinoLink = $('ul.secondary-menu').children('.casino').children('a'); if (casinoLink.length <=0 ) { $('ul.secondary-menu').children('.casino').remove(); } }); </script></body></html>";
			Map<String, String> inputFields = new HashMap<String, String>();
//			final List<SiteEventPackage> eventsPackage = names777TDSportsParser.parseGames(xhtml, null, inputFields);
			
//			if (eventsPackage != null && eventsPackage.size() > 0) {
//				for (SiteEventPackage siteEventPackage : eventsPackage) {
//					System.out.println("SiteEventPackage: " + siteEventPackage);
//				}
//			}

//			final String tn = "<!DOCTYPE html><html><head id=\"ctl00_Head\"><meta http-equiv=\"cache-control\" content=\"no-cache\" /><meta http-equiv=\"pragma\" content=\"no-cache\" /><meta name=\"robots\" content=\"index,follow\" /><meta name=\"revisit\" content=\"2 days\" /><meta name=\"robots\" content=\"All\" /><meta name=\"Revisit-after\" content=\"2 days\" /><meta name=\"Distribution\" content=\"Global\" /><meta name=\"Copyright\" content=\"504bet.net\" /><meta name=\"HandheldFriendly\" content=\"true\" /><meta name=\"viewport\" content=\"width=600\" /><title>	504bet</title><meta name=\"keywords\" content=\"agents sportsbook, sportsbook for agents, online sportsbook for agents, bettors, sports betting, sports bets, sports wagering, wagering, online gambling, sportsbook, sports odds, internet odds, internet gambling, scores, live odds, sports wager, bet, bets, wagers, odds, sport book, sports book, sports books\" /><meta name=\"description\" content=\"504bet Sportsbook Online Agents offers the latest betting odds NBA and NCAA Basketball, MLB Baseball, NFL Football, Motor Sports. Find attractive Sports Betting Bonuses Promotions Online. Special for agents, credit shop not post up sportsbook. This is an agents sportsbook and sports betting credit shop.\" /><link rel=\"shortcut icon\" href=\"../App_Themes/pm247/images/favicon.ico\" type=\"image/x-icon\" /><link rel=\"icon\" href=\"../App_Themes/pm247/images/favicon.ico\" type=\"image/x-icon\" /><link href=\"../App_Themes/pm247/content/css/font-awesome.min.css\" type=\"text/css\" rel=\"stylesheet\" /><link href=\"../App_Themes/pm247/content/css/jquery.slider.css\" type=\"text/css\" rel=\"stylesheet\" /><link href=\"../App_Themes/pm247/content/css/jquery.slider.min.css\" type=\"text/css\" rel=\"stylesheet\" /><link href=\"../App_Themes/pm247/content/css/theme.min.css\" type=\"text/css\" rel=\"stylesheet\" /></head><body><form name=\"aspnetForm\" method=\"post\" action=\"PostWager.aspx?WT=0\" onkeypress=\"javascript:return WebForm_FireDefaultButton(event, 'ctl00_WagerContent_btn_Continue1')\" id=\"aspnetForm\"><div><input type=\"hidden\" name=\"__EVENTTARGET\" id=\"__EVENTTARGET\" value=\"\" /><input type=\"hidden\" name=\"__EVENTARGUMENT\" id=\"__EVENTARGUMENT\" value=\"\" /><input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"/wEPDwUKLTMxNjc3NTM3NQ8WAh4IV2FnZXJYbWwFyA08eG1sIEN1cnJlbmN5Q29kZT0iVVNEIiBQaXRjaGVyRGVmYXVsdD0iMCIgSWRVc2VyPSIwIiBGcmVlUGxheUF2YWlsYWJsZT0iMC4wMCIgSWRQcm9maWxlPSI0MzIxIiBJZFByb2ZpbGVMaW1pdHM9IjQ4OTkiIElkUGxheWVyPSIxNDU1MTUiIExhc3RJZFdhZ2VyPSIwIiBJZENhbGw9IjU3Mjc1MTc0IiBXYWdlclRyYWNrZXI9IjUzODczNTMyIiBJZkJldEFza0Ftb3VudD0iVHJ1ZSIgQ29uZmlybT0iVHJ1ZSIgRXJyb3JDb2RlPSIwIiBFcnJvck1zZ0tleT0iIiBFcnJvck1zZ1BhcmFtcz0iIiBFcnJvck1zZz0iIiBVc2VGcmVlUGxheUF2YWlsYWJsZT0iRmFsc2UiIFNlY3VyaXR5Q29kZT0iQzMwQzJFRThERThDMzEwRUFBRkFFQUJGRDg3NkFCM0MiIElmQmV0Umlzaz0iMCIgSWZCZXRXaW49IjAiPjx3YWdlciBXYWdlclR5cGVEZXNjPSJTVFJBSUdIVCBCRVQiIFdhZ2VyVHlwZURlc2NFbmc9IlNUUkFJR0hUIEJFVCIgV2FnZXJUeXBlPSIwIiBUaWNrZXROdW1iZXI9IjY0NjQ5MzY3IiBUZWFzZXJDYW5CdXlIYWxmPSIwIiBUZWFzZXJDYW5CdXlPbmU9IjAiIFRlYXNlclBvaW50c1B1cmNoYXNlZD0iMCIgRmlsbElkV2FnZXI9Ii0xIiBBbW91bnQ9IjUwMC4wIiBSaXNrPSI1NzUuMDAiIFdpbj0iNTAwLjAwIiBJRFdUPSIxMzc2MjAiIFJpc2tXaW49IjEiIFJvdW5kUm9iaW5Db21iaW5hdGlvbnM9IjAiIENvbXBhY3RDb21iaW5hdGlvbnM9IiI+PGRldGFpbCBJc01MaW5lPSJGYWxzZSIgVGVhbURlc2NyaXB0aW9uPSJbNTcxM10gVE9UQUwgKDNRIFBPUiBCTEFaRVJTIHZycyAzUSBMQSBMQUtFUlMpIiBUZWFtRGVzY3JpcHRpb25Fbmc9Ils1NzEzXSBUT1RBTCAoM1EgUE9SIEJMQVpFUlMgdnJzIDNRIExBIExBS0VSUykiIFZlcnN1c1RlYW1EZXNjcmlwdGlvbj0iWzU3MTRdIFRPVEFMICgzUSBQT1IgQkxBWkVSUyB2cnMgM1EgTEEgTEFLRVJTKSIgTGluZURlc2NyaXB0aW9uPSJvNTcmYW1wO2ZyYWMxMjstMTE1IiBQcmV2aW91c0xpbmVEZXNjcmlwdGlvbj0iIiBEZXNjcmlwdGlvbj0iWzU3MTNdIFRPVEFMIG81NyZhbXA7ZnJhYzEyOy0xMTUgKDNRIFBPUiBCTEFaRVJTIHZycyAzUSBMQSBMQUtFUlMpIiBEZXNjcmlwdGlvbkVuZz0iWzU3MTNdIFRPVEFMIG81NyZhbXA7ZnJhYzEyOy0xMTUgKDNRIFBPUiBCTEFaRVJTIHZycyAzUSBMQSBMQUtFUlMpIiBJZFNwb3J0PSJOQkEiIElkR2FtZVR5cGU9IjcyIiBQYXJlbnRHYW1lPSIxNTkxODI3IiBGYW1pbHlHYW1lPSIxNTkxODI3IiBJZEV2ZW50PSIwIiBFdmVudERlc2NyaXB0aW9uPSIiIFBlcmlvZD0iNSIgQ2FuQ2hvb3NlUGl0Y2hlcj0iRmFsc2UiIE1hcmtGb3JEZWxldGlvbj0iRmFsc2UiIFBvaW50c1B1cmNoYXNlZEp1aWNlPSIwIiBPcmlnaW5hbE9kZHM9Ii0xMTUiIE9yaWdpbmFsUG9pbnRzPSItNTcuNSIgVmlzaXRvclBpdGNoZXI9IiIgSG9tZVBpdGNoZXI9IiIgR2FtZURhdGVUaW1lPSIwMy0wNS0yMDE4IDIzOjUwOjAwIiBHYW1lRGF0ZT0iTWFyIDA1IiBHYW1lVGltZT0iMTE6NTAgUE0iIElzRmF2b3JpdGU9IlRydWUiIElzTGluZUZyb21BZ2VudD0iRmFsc2UiIElkR2FtZT0iMTU5MjE0MCIgUGxheT0iMiIgUG9pbnRzPSItNTcuNSIgT2Rkcz0iLTExNSIgUGl0Y2hlcj0iMCIgSXNPbk9wZW5CZXRzPSJGYWxzZSIgUG9pbnRzUHVyY2hhc2VkPSIwIiBJc1NwcmVhZD0iRmFsc2UiIElzVG90YWw9IlRydWUiIElzS2V5PSJGYWxzZSIgQ2hhcnRSaXNrPSIwIiBDaGFydFdpbj0iMCIgLz48L3dhZ2VyPjwveG1sPhYCZg9kFgICAw9kFgJmD2QWAgIBD2QWCgIBDw8WAh4EVGV4dAUETUwwMmRkAgUPDxYCHwEFBzU1MCBVU0RkZAIJDw8WAh8BBQoxOSw5NzUgVVNEZGQCDQ8PFgIfAQUHNTc1IFVTRGRkAhEPDxYCHwEFBTAgVVNEZGRkGCDm25zixLwh0BxCe7FD9/df2B4=\" /></div><script type=\"text/javascript\">//<![CDATA[var theForm = document.forms['aspnetForm'];if (!theForm) { theForm = document.aspnetForm;}function __doPostBack(eventTarget, eventArgument) { if (!theForm.onsubmit || (theForm.onsubmit() != false)) { theForm.__EVENTTARGET.value = eventTarget; theForm.__EVENTARGUMENT.value = eventArgument; theForm.submit(); }}//]]></script><script src=\"/WebResource.axd?d=uQSWyjb1nPuIeYg4RgwdMQi3IVixUS2HqG2JbRHviVbWF_KTUe0XjSlBiW3hVEPtK7Vj5FfEWxO4RdVnNVKnzBOwvl81&amp;t=635019837069635093\" type=\"text/javascript\"></script><div>	<input type=\"hidden\" name=\"__EVENTVALIDATION\" id=\"__EVENTVALIDATION\" value=\"/wEWAwL/949gAuqVsosJAuqVsosJ6CCvnTz3UWpga0VV9VoANxkZ+Zw=\" /></div><div class=\"menu-container\"> <div class=\"container content-menu\"> <center> <ul id=\"navmenu\" class=\"menu\"> <li> <div> <span><i class=\"fa fa-user\"></i> </span> <span class=\"info\"><span id=\"ctl00_Content3_AccountFigures3_lblAccount\">ML02</span></span> </div> </li> <li> <div> <span><span id=\"ctl00_Content3_AccountFigures3_LocalizedLabel5\">Balance:</span></span> <span class=\"info\"><span id=\"ctl00_Content3_AccountFigures3_lblCurrentBalance\">550 USD</span></span> </div> </li> <li> <div> <span><span id=\"ctl00_Content3_AccountFigures3_LocalizedLabel6\">Available:</span></span> <span class=\"info\"><span id=\"ctl00_Content3_AccountFigures3_lblRealAvailBalance\">19,975 USD</span></span> </div> </li> <li> <div> <span><span id=\"ctl00_Content3_AccountFigures3_LocalizedLabel8\">Pending:</span></span> <span class=\"info\"><span id=\"ctl00_Content3_AccountFigures3_lblAmountAtRisk\">575 USD</span></span> </div> </li> <li> <div> <span><span id=\"ctl00_Content3_AccountFigures3_LocalizedLabel11\">Free Play:</span></span> <span class=\"info\"><span id=\"ctl00_Content3_AccountFigures3_lblFreePlay\">0 USD</span></span> </div> </li> </ul> </center> </div></div><div class=\"header-container\"> <div class=\"container\"> <a href=\"Welcome.aspx\" class=\"logo\"> <img src=\"../App_themes/pm247/images/logo.png\" style=\"height:105px;width:305px;border-width:0px;\" /> </a> <div class=\"secondary-menu-container\"> <span id=\"ctl00_WagerLnk\"> <ul class=\"secondary-menu\"> <li class=\"has-sub\"><img width=\"20\" height=\"20\" src=\"http://logos.horizonsports.es/icons/football2.png\"><a id=\"ctl00_WagerLnk_lnkSports\" href=\"Sports.aspx\">Sports</a> <ul> <li><a id=\"ctl00_WagerLnk_lnkMatchups\" href=\"../matchups/\" target=\"_blank\">Matchups</a></li> <li><a id=\"ctl00_WagerLnk_lnkSchedule\" href=\"../schedule/\" target=\"_blank\">Schedule</a></li> </ul> </li> <li><img width=\"20\" height=\"20\" src=\"http://logos.horizonsports.es/icons/horses.png\"></li><li><img width=\"20\" height=\"20\" src=\"http://logos.horizonsports.es/icons/livebetting.png\"><a id=\"ctl00_WagerLnk_lnkLiveBet\" href=\"live.aspx?ws=0\"><b>[ExtraLive]</b></a></li> <li class=\"casino\"><img width=\"20\" height=\"20\" src=\"http://logos.horizonsports.es/icons/dice.png\"></li> <li class=\"has-sub\"><img width=\"20\" height=\"20\" src=\"http://logos.horizonsports.es/icons/player2.png\"><a id=\"ctl00_WagerLnk_lnkMyAccount\" href=\"Welcome.aspx\">My Account</a> <ul> <li><a id=\"ctl00_WagerLnk_lnkFillOpen\" href=\"FillChoose.aspx\">Fill Open</a></li> <li><a id=\"ctl00_WagerLnk_lnkOpenBets\" href=\"OpenBets.aspx\">Open Bets</a></li> <li><a id=\"ctl00_WagerLnk_lnkHistory\" href=\"History.aspx\">History</a></li> <li><a id=\"ctl00_WagerLnk_lnkFigures\" href=\"Welcome.aspx\">Figures</a></li> </ul> </li> <li class=\"logout\"><a id=\"ctl00_WagerLnk_lnkLogout\" href=\"../LogOut.aspx\">Logout</a><i class=\"fa fa-sign-out\"></i></li> </ul><ul></ul> </span> </div> </div></div><BR><BR><div class=\"main-menu-container\"> <div class=\"container\"> <ul class=\"nav menu md-menu\"> <li id=\"WT35\" class=\"tab red\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk0\" href=\"CreateSports.aspx?WT=35\">Bet It All</a></span></li> <li id=\"WT0\" class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk1\" href=\"CreateSports.aspx?WT=0\">Straight</a></span></li> <li id=\"WT1\" class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk2\" href=\"CreateSports.aspx?WT=1\">Parlay</a></span></li> <li id=\"WT2\" class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk3\" href=\"ChooseTeaser.aspx?WT=2\">Teaser</a></span></li> <li id=\"WT5\" class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk4\" href=\"CreateSports.aspx?WT=5\">Round Robin</a></span></li> <li id=\"WT3\" class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk5\" href=\"CreateSports.aspx?WT=3\">If Win Only</a></span></li> <li id=\"WT7\" class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk6\" href=\"CreateSports.aspx?WT=7\">If Win or Tie</a></span></li> <li id=\"WT4\" class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk7\" href=\"CreateSports.aspx?WT=4\">Win Reverse</a></span></li> <li id=\"WT8\" class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk8\" href=\"CreateSports.aspx?WT=8\">Action Reverse</a></span></li> <!--li class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk9\" href=\"CreateSports.aspx?WT=15\"><b>[Compact RoundRobin]</b></a></span><span class=\"tabrg\">&nbsp;</span></li> <li class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk10\" href=\"CreateSports.aspx?WT=16\"><b>[Key Parlay]</b></a></span><span class=\"tabrg\">&nbsp;</span></li> <li class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk11\" href=\"CreateSports.aspx?WT=17\"><b>[Compact IfWinOnly]</b></a></span><span class=\"tabrg\">&nbsp;</span></li> <li class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk12\" href=\"CreateSports.aspx?WT=18\"><b>[Compact IfWinOrTie]</b></a></span><span class=\"tabrg\">&nbsp;</span></li--> </ul> </div></div><div class=\"content-container\"> <div class=\"container\"> <div class=\"wagercontent\"> <div class=\"wager-container\" ><table align=\"center\" class=\"main\"><thead><tr class=\"table-header\"><th colspan=\"5\">STRAIGHT BET</th></tr><tr class=\"table-header table-sub-header\"><th width=\"10%\"><span>Date</span></th><th align=\"left\"><span>Team</span></th><th align=\"left\"><span>Risking</span> / <span>To Win</span></th><th align=\"left\"><span>Ticket#:</span></th></tr></thead><TR><TD align=\"center\" class=\"date\"><i class=\"fa fa-clock-o\"></i>Mar 05</TD><TD align=\"left\">NBA [5713] TOTAL o57&frac12;-115 (3Q POR BLAZERS vrs 3Q LA LAKERS) </TD><TD>575.00 USD / 500.00 USD</TD><TD>64649367</TD></TR></table><table align=\"center\" class=\"results-table\"><tr><td colspan=\"5\"><TR><TD ALIGN=\"center\"><TABLE ALIGN=\"center\" class=\"WagerTable2\"><TR><TD width=\"33%\" ALIGN=\"center\" valign=\"top\"><br><a id=\"ctl00_WagerContent_btn_Continue1\" href=\"javascript:__doPostBack('ctl00$WagerContent$btn_Continue1','')\"></a><a id=\"ctl00_WagerContent_btn_Continue1\" href=\"javascript:__doPostBack('ctl00$WagerContent$btn_Continue1','')\">Click Here</a><BR><span>To place another bet.</span></TD><TD width=\"33%\" ALIGN=\"center\" valign=\"top\"><br><a id=\"ctl00_WagerContent_lnkOpenBets\" href=\"OpenBets.aspx\"><span>Click Here</span></a><BR><span>To review your pending plays and to ensure your wagers were placed correctly.</span></TD><TD width=\"33%\" ALIGN=\"center\" valign=\"top\"><BR><a href=\"javascript:window.print();\"><i class=\"fa fa-print\"></i><span>Print Ticket</span></a><BR><span>For Your Insurance.</span></TD></TR></TABLE><BR></TD></TR></td></tr></table></div> </div> </div></div><div class=\"footer-container\"> <div class=\"container\"> <span> <i class=\"fa fa-copyright\"></i> <span id=\"currentyear\">2018</span> 504bet.net All Rights Reserved </span> <a class=\"align-right btn btn-link\" href=\"/terms\" target=\"_blank\">Terms &amp; Conditions</a> </div></div></form> <script type=\"text/javascript\" src=\"../scripts/jquery.min.js\"></script> <script type=\"text/javascript\" src=\"../scripts/selector.js\"></script> <script type=\"text/javascript\" src=\"../scripts/teamnames.js\"></script> <script type=\"text/javascript\" src=\"http://arrow.scrolltotop.com/arrow59.js\"></script> <script type=\"text/javascript\"> $(function() { var casinoLink = $('ul.secondary-menu').children('.casino').children('a'); if (casinoLink.length <=0 ) { $('ul.secondary-menu').children('.casino').remove(); } }); </script></body></html>";
//			String ticketNumber = names777TDSportsParser.parseTicketNumber(tn);
//			LOGGER.debug("TicketNumber: " + ticketNumber);
			
			String html = "STRAIGHT BET <br> [911] TOTAL o9½+105 (SEA MARINERS vrs BAL ORIOLES) <br> ( F. HERNANDEZ -R / A. CASHNER -R ) <br>";
			int bindex = html.indexOf("TOTAL ");
			PendingEvent pe = new PendingEvent();
			pe.setGametype("MLB");
			names777TDSportsParser.getTotal(html, bindex, pe);
			LOGGER.error("pe: " + pe);
		} catch (Throwable be) {
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
		map = findMenu(doc.select(".divLeague"), map, type, sport, "div a", "div input");

		// Remove the input fields that shouldn't be sent
		map = removeInputFields(map);

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
		Elements elements = doc.select(".lines .line");
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
		String ticketNumber = "Ticket Number - ";
		final String ticketInfo = " USD</TD><TD>";

		if (xhtml.contains("Your bet expired or has already been submited.")) {
			throw new BatchException(BatchErrorCodes.WAGER_HAS_EXPIRED, BatchErrorMessage.WAGER_HAS_EXPIRED, "The wager has expired or already been bet", xhtml);	
		}
		
		if (xhtml.contains("The line changed for one (or more) of your selections.")) {
			// Line changed throw exception
			throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed and cannot complete transaction", xhtml);	
		}

		// First check for Wager has been accepted!
		if (xhtml != null && xhtml.contains(ticketInfo)) {
			// Great, wager is complete; now get the Ticket Number
			int index = xhtml.indexOf(ticketInfo);
			if (index != -1) {
				final String nxhtml = xhtml.substring(index + ticketInfo.length());
				index = nxhtml.indexOf("</TD>");
				if (index != -1) {
					ticketNumber = nxhtml.substring(0, index);
				} else {
					ticketNumber = "Failed to get ticket number";
					throw new BatchException(BatchErrorCodes.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, BatchErrorMessage.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, ticketNumber, xhtml);
				}
			} else {
				ticketNumber = "Failed to get ticket number";
				throw new BatchException(BatchErrorCodes.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, BatchErrorMessage.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, ticketNumber, xhtml);
			}
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
				// Loop through the elements and then check for the dates
				final Element element = elements.get(x);
				LOGGER.debug("Element: " + element);

				eventPackage = new TDSportsEventPackage();
				team1 = new TDSportsTeamPackage();
				team2 = new TDSportsTeamPackage();

				// First get the date
				String gdate = null;
				final Elements spans = element.select(".header-tool .control span");
				if (spans != null && spans.size() > 0) {
					gdate = spans.get(0).html().trim();
				}
				
				// Now get the first team's info
				int rownum = 0;
				final Elements rowlines = element.select(".row-line");
				for (Element rowline : rowlines) {
					if (rownum++ == 0) {
						getTeamData(rowline, team1);
						eventPackage.setId(team1.getId());
					} else {
						getTeamData(rowline, team2);
					}
				}

				String dateOfEvent = null;
				Date eventDate = null;
				try {
					final Calendar now = Calendar.getInstance();
					int offset = now.get(Calendar.DST_OFFSET);
					dateOfEvent = gdate + " " + timeZoneLookup(timezone, offset);
					eventDate = SITE_DATE_FORMAT.parse(dateOfEvent);
				} catch (ParseException pe) {
					LOGGER.error("ParseExeption for " + gdate, pe);
					// Throw an exception
					throw new BatchException(AppErrorMessage.SITE_PARSER_EXCEPTION  + " Date parsing exception for " + gdate);
				}
				team1.setEventdatetime(eventDate);
				team2.setEventdatetime(eventDate);
				team1.settDate(dateOfEvent);
				team2.settDate(dateOfEvent);
				eventPackage.setDateofevent(dateOfEvent);
				eventPackage.setSiteteamone(team1);
				eventPackage.setSiteteamtwo(team2);
				eventPackage.setTeamone(team1);
				eventPackage.setTeamtwo(team2);
				events.add(eventPackage);
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
	 * @param rowline
	 * @param team
	 */
	protected void getTeamData(Element rowline, TDSportsTeamPackage team) {
		LOGGER.info("Entering getTeamData()");
		LOGGER.debug("rowline: " + rowline);

		// Rotation #
		final String eventId = rowline.select(".rot-team span").get(0).html().trim();
		LOGGER.debug("eventId: " + eventId);
		team.setEventid(eventId);
		team.setId(Integer.parseInt(eventId));

		// Team name
		team.setTeam(rowline.select(".team-info .team label").get(0).html().trim());

		// Loop through each
		final Elements bets = rowline.select(".col-md-8");
		if (bets != null && bets.size() > 0) {
			final Element bet = bets.get(0);
			final Elements divs = bet.select("div");
			LOGGER.debug("divs.size(): " + divs.size());

			// Is this baseball?
			if (divs != null && divs.size() == 5) {
				// Pitcher
				Element div = divs.get(1);
				LOGGER.debug("div: " + div);
				final String pitcher = div.select("span").get(0).html().trim();
				LOGGER.debug("pitcher: " + pitcher);
				team.setPitcher(pitcher);

				// Moneyline
				div = divs.get(2);
				LOGGER.debug("div: " + div);
				team = getMoneyLine(div, team);

				// Total
				team = getTotal(divs.get(3), team);

				// Spread
				team = getSpread(divs.get(4), team);
			} else {
				// Spread
				team = getSpread(divs.get(1), team);

				// Total
				team = getTotal(divs.get(2), team);

				// Moneyline
				team = getMoneyLine(divs.get(3), team);
			}
		}

		LOGGER.info("Exiting getTeamData()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.tdsports.TDSportsParser#getMoneyLine(org.jsoup.nodes.Element, com.ticketadvantage.services.dao.sites.tdsports.TDSportsTeamPackage)
	 */
	@Override
	protected TDSportsTeamPackage getMoneyLine(Element div, TDSportsTeamPackage team) {
		LOGGER.info("Entering getMoneyLine()");
		LOGGER.debug("Div: " + div);
		
		// First get the input field information
		final Map<String, String> hashMap = parseInputField(div.select("span input"), 0);
		team.setGameMLInputId(hashMap.get("id"));
		team.setGameMLInputName(hashMap.get("name"));
		team.addGameMLInputValue("0", hashMap.get("value"));

		// -110; Now parse the data
		final String ml = div.select("span").get(0).html().trim();

		// Parse for Money Line
		team = (TDSportsTeamPackage)parseMlData(reformatValues(ml), 0, team);

		LOGGER.info("Exiting getMoneyLine()");
		return team;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.tdsports.TDSportsParser#getTotal(org.jsoup.nodes.Element, com.ticketadvantage.services.dao.sites.tdsports.TDSportsTeamPackage)
	 */
	@Override
	protected TDSportsTeamPackage getTotal(Element div, TDSportsTeamPackage team) {
		LOGGER.info("Entering getTotal()");
		LOGGER.debug("Div: " + div);

		// First get the input field information
		final Map<String, String> hashMap = parseInputField(div.select("span input"), 0);
		team.setGameTotalInputId(hashMap.get("id"));
		team.setGameTotalInputName(hashMap.get("name"));
		team.setGameTotalInputValue(hashMap.get("value"));

		// o40½-110; Now parse the data
		final String total = div.select("span").get(0).html().trim();

		// Parse Total Data
		team.addGameTotalOptionValue("0", "0");
		team = (TDSportsTeamPackage)parseTotalData(reformatValues(total), 0, " ", null, team);

		LOGGER.info("Exiting getTotal()");
		return team;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.tdsports.TDSportsParser#getSpread(org.jsoup.nodes.Element, com.ticketadvantage.services.dao.sites.tdsports.TDSportsTeamPackage)
	 */
	@Override
	protected TDSportsTeamPackage getSpread(Element div, TDSportsTeamPackage team) {
		LOGGER.info("Entering getSpread()");
		LOGGER.debug("Div: " + div);

		// First get the input field information
		final Map<String, String> hashMap = parseInputField(div.select("span input"), 0);
		team.setGameSpreadInputId(hashMap.get("id"));
		team.setGameSpreadInputName(hashMap.get("name"));
		team.setGameSpreadInputValue(hashMap.get("value"));

		// o40½-110; Now parse the data
		final String spread = div.select("span").get(0).html().trim();

		// Setup spread
		team.addGameSpreadOptionValue("0", "0");
		team = (TDSportsTeamPackage)parseSpreadData(reformatValues(spread), 0, " ", null, team);

		LOGGER.info("Exiting getSpread()");
		return team;
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
		} else {
			size = super.getTeamData(elements, team);
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
		int size = 0;

		if (elements != null && elements.size() == 8) {
			LOGGER.info("elements.size(): " + elements.size());
			size = 8;
			for (int x = 0;(elements != null && x < elements.size()); x++) {
				final Element td = elements.get(x);
	
				switch (x) {
					case 0: // Do nothing
					case 4:
						break;
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
						team = getMoneyLine(td, team);
						break;
					case 6:
						team = getTotal(td, team);
						break;
					case 7:
						team = getSpread(td, team);
						break;
				}
			}
		}

		LOGGER.info("Exiting parseMlb()");
		return size;
	}

	/**
	 * 
	 * @param xhtml
	 * @param accountName
	 * @param accountId
	 * @return
	 * @throws BatchException
	 */
	public Set<PendingEvent> parsePendingBets(String xhtml, String accountName, String accountId) throws BatchException {
		LOGGER.info("Entering parsePendingBets()");
		LOGGER.debug("accountName: " + accountName);
		LOGGER.debug("accountId: " + accountId);
		Set<PendingEvent> pendingEvents = new HashSet<PendingEvent>();
		
		if (xhtml != null) {
			xhtml = xhtml.replace("Â", "");
		}

		// Get the document object
		final Document doc = parseXhtml(xhtml);
		Elements trs = doc.select(".dd_content table tr");
		if (trs != null && trs.size() > 0) {
			// Do nothing
		} else {
			trs = doc.select("#CreateWagerTable tr");
		}

		LOGGER.debug("trs: " + trs);
		if (trs != null) {
			for (Element tr : trs) {
				PendingEvent pe = null;
				int x = 0;
				final String className = tr.attr("class");
				if (className != null && (className.length() == 0 || className.contains("dd_rowDark"))) {
					try {
						final Elements tds = tr.select("td");
						pe = new PendingEvent();
						pe.setDatecreated(new Date());
						pe.setDatemodified(new Date());
						pe.setCustomerid(accountName);
						pe.setInet(accountName);
						pe.setAccountname(accountName);
						pe.setAccountid(accountId);
						for (Element td : tds) {
							LOGGER.debug("td: " + td);
							LOGGER.debug("tds.size(): " + tds.size());
							if (tds.size() == 6) {
								switch (x++) {
									case 0:
										getDateAccepted(td, pe);
										break;
									case 1:
										getTicketNumber(td, pe);
										break;
									case 2:
									{
										getSport(td, pe);
										final String gameSport = pe.getGamesport();
										if ("NFL".equals(gameSport)) {
											pe.setGamesport("Football");
											pe.setGametype(gameSport);
										} else if ("CFB".equals(gameSport)) {
											pe.setGamesport("Football");
											pe.setGametype("NCAA");				
										} else if ("NBA".equals(gameSport)) {
											pe.setGamesport("Basketball");
											pe.setGametype(gameSport);
										} else if ("CBB".equals(gameSport)) {
											pe.setGamesport("Basketball");
											pe.setGametype("NCAA");
										} else if ("WNBA".equals(gameSport)) {
											pe.setGamesport("Basketball");
											pe.setGametype(gameSport);
										} else if ("NHL".equals(gameSport)) {
											pe.setGamesport("Hockey");
											pe.setGametype(gameSport);
										} else if ("MLB".equals(gameSport)) {
											pe.setGamesport("Baseball");
											pe.setGametype(gameSport);				
										} else if ("MU".equals(gameSport)) {
											pe.setGamesport("Golf");
											pe.setGametype(gameSport);
										}

										break;
									}
									case 3:
										getGameDate(td.html().trim(), pe);
										break;
									case 4:
									{
//										getEventSportAndType(td, pe);
										Pattern p = Pattern.compile("<br>");
										Matcher m = p.matcher(td.html());
										int i = 0;
										while (m.find()) {
										    i++;
										}
										if (i == 3) {
											getGameInfo(td, pe);	
										} else {
											getGameInfo2(td, pe);
										}

										String team = pe.getTeam();
										if (team != null) {
											int sindex = team.indexOf("<strong");
											if (sindex != -1) {
												team = team.substring(0, sindex).trim();
												pe.setTeam(team);
											}
										}

										String pitcher = pe.getPitcher();
										if (pitcher != null) {
											int sindex = pitcher.indexOf("<strong");
											if (sindex != -1) {
												pitcher = pitcher.substring(0, sindex).trim();
												pe.setPitcher(pitcher);
											}
										}
										break;
									}
									case 5:
										getRiskWin(td, pe);
										break;
									default:
										break;
								}
							} else if (tds.size() == 7) {
								switch (x++) {
									case 0:
										getTicketNumberAndDate(td, pe);
										break;
									case 1:
										// do nothing
										break;
									case 2:
										pe.setDateaccepted(td.html().trim());
										break;
									case 3:
										getEventSportAndType(td, pe);
										break;
									case 4:
									{
										Pattern p = Pattern.compile("<br>");
										Matcher m = p.matcher(td.html());
										int i = 0;
										while (m.find()) {
										    i++;
										}
										if (i == 3) {
											getGameInfo(td, pe);	
										} else {
											getGameInfo2(td, pe);
										}
										break;
									}
									case 5:
										String thtml = td.html();
										if (thtml != null) {
											thtml = thtml.replace("<br>", "").trim();
											pe.setRisk(thtml);
										}
										break;
									case 6:
										String whtml = td.html();
										if (whtml != null) {
											whtml = whtml.replace("<br>", "").trim();
											pe.setWin(whtml);
										}
										break;
									default:
										break;
								}							
							}
						}
						pe.setDoposturl(false);
	
						// Quick check if WNBA
						if ("NBA".equals(pe.getGametype())) {
							final String peTeam = pe.getTeam();
							if (peTeam != null) {
								for (String wnbaTeam : WNBA_TEAMS) {
									if (peTeam.toLowerCase().contains(wnbaTeam.toLowerCase())) {
										pe.setGametype("WNBA");
										break;
									}
								}
							}
						}

						pendingEvents.add(pe);
					} catch (BatchException be) {
						if (pe != null) {
							pe.setGamesport("");
							pe.setGametype("");
						}
					} catch (Throwable t) {
						LOGGER.error(t.getMessage(), t);
						if (pe != null) {
							pe.setGamesport("");
							pe.setGametype("");
						}
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
							sendText.sendTextWithMessage("9132195234@vtext.com", "Error in pendingSite " + accountName + " " + accountId + " " + tr.html());
						} catch (Throwable tt) {
							LOGGER.error(tt.getMessage(), tt);
						}
					}
				}
			}	
		}

		LOGGER.info("Exiting parsePendingBets()");
		return pendingEvents;
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 */
	protected void getDateAccepted(Element td, PendingEvent pe) {
		LOGGER.info("Entering getDateAccepted()");

		// Aug 26 08:00 PM
		// <br>
		String html = td.html();
		LOGGER.debug("html: " + html);
		if (html != null) {
			int index = html.indexOf("<br");
			if (index != -1) {
				html = html.substring(0, index);
				Calendar now = Calendar.getInstance();
				int year = now.get(Calendar.YEAR);
				pe.setDateaccepted(year + " " + html + " " + determineTimeZone(super.timezone));
			}		
		}

		LOGGER.info("Exiting getDate()");
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 */
	protected void getTicketNumber(Element td, PendingEvent pe) {
		LOGGER.info("Entering getTicketNumber()");

		// Ticket#: 31478859
		// <br>
		// Aug 26 08:00 PM
		// <br>
		String html = td.html();
		LOGGER.debug("html: " + html);
		if (html != null) {
			pe.setTicketnum(html.trim());
		}

		LOGGER.info("Exiting getTicketNumber()");
	}

	/**
	 * 
	 * @param gamedate
	 * @param pe
	 */
	protected void getGameDate(String gamedate, PendingEvent pe) {
		LOGGER.info("Entering getGameDate()");
		LOGGER.debug("gamedate: " + gamedate);

		try {
			gamedate = gamedate.replace("<br>", "").trim();
			Calendar now = Calendar.getInstance();
			int year = now.get(Calendar.YEAR);

			pe.setEventdate(year + " " + gamedate + " " + determineTimeZone(super.timezone));
			try {
				pe.setGamedate(fixDate(year + " " + gamedate + " " + determineTimeZone(super.timezone), PENDING_DATE_FORMAT.get()));
			} catch (ParseException pe1) {
				pe.setGamedate(fixDate(year + " " + gamedate + " " + determineTimeZone(super.timezone), PENDING_DATE_FORMAT_2.get()));
			}
		} catch (ParseException pee) {
			LOGGER.error("gamedate: " + gamedate + " " + determineTimeZone(super.timezone));
			LOGGER.error(pee.getMessage(), pee);
			pe.setGamedate(new Date());
		} catch (NumberFormatException nfe) {
			LOGGER.error("gamedate: " + gamedate + " " + determineTimeZone(super.timezone));
			LOGGER.error(nfe.getMessage(), nfe);
			pe.setGamedate(new Date());
		}

		LOGGER.info("Exiting getGameDate()");
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 */
	protected void getSport(Element td, PendingEvent pe) {
		LOGGER.info("Entering getSport()");

		String html = td.html();
		if (html != null) {
			html = html.replace("<br>", "").trim();
			pe.setGamesport(html);
		}

		LOGGER.info("Exiting getSport()");
	}
}