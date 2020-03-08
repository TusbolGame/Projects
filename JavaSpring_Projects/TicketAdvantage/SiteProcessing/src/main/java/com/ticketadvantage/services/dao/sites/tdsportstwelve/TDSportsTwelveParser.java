/**
 * 
 */
package com.ticketadvantage.services.dao.sites.tdsportstwelve;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ticketadvantage.services.dao.sites.tdsports.TDSportsEventPackage;
import com.ticketadvantage.services.dao.sites.tdsports.TDSportsParser;
import com.ticketadvantage.services.dao.sites.tdsports.TDSportsTeamPackage;
import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.PendingEvent;

/**
 * @author jmiller
 *
 */
public class TDSportsTwelveParser extends TDSportsParser {
	private static final Logger LOGGER = Logger.getLogger(TDSportsTwelveParser.class);
	// Sep 07 12:00 PM
	private static final SimpleDateFormat PENDING_DATE_FORMAT = new SimpleDateFormat("MMM dd hh:mm a yyyy z");

	/**
	 * Constructor
	 */
	public TDSportsTwelveParser() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final TDSportsTwelveParser names777TDSportsParser = new TDSportsTwelveParser();

/*
			String xhtml = "<!DOCTYPE html><html><head id=\"ctl00_Head\"><meta http-equiv=\"cache-control\" content=\"no-cache\" /><meta http-equiv=\"pragma\" content=\"no-cache\" /><meta name=\"robots\" content=\"index,follow\" /><meta name=\"revisit\" content=\"2 days\" /><meta name=\"robots\" content=\"All\" /><meta name=\"Revisit-after\" content=\"2 days\" /><meta name=\"Distribution\" content=\"Global\" /><meta name=\"Copyright\" content=\"504bet.net\" /><meta name=\"HandheldFriendly\" content=\"true\" /><meta name=\"viewport\" content=\"width=600\" /><title>	504bet</title><meta name=\"keywords\" content=\"agents sportsbook, sportsbook for agents, online sportsbook for agents, bettors, sports betting, sports bets, sports wagering, wagering, online gambling, sportsbook, sports odds, internet odds, internet gambling, scores, live odds, sports wager, bet, bets, wagers, odds, sport book, sports book, sports books\" /><meta name=\"description\" content=\"504bet Sportsbook Online Agents offers the latest betting odds NBA and NCAA Basketball, MLB Baseball, NFL Football, Motor Sports. Find attractive Sports Betting Bonuses Promotions Online. Special for agents, credit shop not post up sportsbook. This is an agents sportsbook and sports betting credit shop.\" /><link rel=\"shortcut icon\" href=\"../App_Themes/pm247/images/favicon.ico\" type=\"image/x-icon\" /><link rel=\"icon\" href=\"../App_Themes/pm247/images/favicon.ico\" type=\"image/x-icon\" /><link href=\"../App_Themes/pm247/content/css/font-awesome.min.css\" type=\"text/css\" rel=\"stylesheet\" /><link href=\"../App_Themes/pm247/content/css/jquery.slider.css\" type=\"text/css\" rel=\"stylesheet\" /><link href=\"../App_Themes/pm247/content/css/jquery.slider.min.css\" type=\"text/css\" rel=\"stylesheet\" /><link href=\"../App_Themes/pm247/content/css/theme.min.css\" type=\"text/css\" rel=\"stylesheet\" /></head><body><form name=\"aspnetForm\" method=\"post\" action=\"Schedule.aspx?WT=0&amp;lg=78\" id=\"aspnetForm\"><div><input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"/wEPDwUKLTMxNjc3NTM3NQ9kFgJmD2QWAgIDD2QWAmYPZBYCAgEPZBYKAgEPDxYCHgRUZXh0BQRNTDAyZGQCBQ8PFgIfAAUHMzAwIFVTRGRkAgkPDxYCHwAFCjE4LDY1MCBVU0RkZAINDw8WAh8ABQkxLDY1MCBVU0RkZAIRDw8WAh8ABQUwIFVTRGRkZHubgJBv1maHWbJvZid9fwAxRN2b\" /></div><div>	<input type=\"hidden\" name=\"__EVENTVALIDATION\" id=\"__EVENTVALIDATION\" value=\"/wEWAgKo2ZGICQLqlbKLCa/Xgy3mkKYkPqjIcMhFxttlzNhu\" /></div><div class=\"menu-container\"> <div class=\"container content-menu\"> <center> <ul id=\"navmenu\" class=\"menu\"> <li> <div> <span><i class=\"fa fa-user\"></i> </span> <span class=\"info\"><span id=\"ctl00_Content3_AccountFigures3_lblAccount\">ML02</span></span> </div> </li> <li> <div> <span><span id=\"ctl00_Content3_AccountFigures3_LocalizedLabel5\">Balance:</span></span> <span class=\"info\"><span id=\"ctl00_Content3_AccountFigures3_lblCurrentBalance\">300 USD</span></span> </div> </li> <li> <div> <span><span id=\"ctl00_Content3_AccountFigures3_LocalizedLabel6\">Available:</span></span> <span class=\"info\"><span id=\"ctl00_Content3_AccountFigures3_lblRealAvailBalance\">18,650 USD</span></span> </div> </li> <li> <div> <span><span id=\"ctl00_Content3_AccountFigures3_LocalizedLabel8\">Pending:</span></span> <span class=\"info\"><span id=\"ctl00_Content3_AccountFigures3_lblAmountAtRisk\">1,650 USD</span></span> </div> </li> <li> <div> <span><span id=\"ctl00_Content3_AccountFigures3_LocalizedLabel11\">Free Play:</span></span> <span class=\"info\"><span id=\"ctl00_Content3_AccountFigures3_lblFreePlay\">0 USD</span></span> </div> </li> </ul> </center> </div></div><div class=\"header-container\"> <div class=\"container\"> <a href=\"Welcome.aspx\" class=\"logo\"> <img src=\"../App_themes/pm247/images/logo.png\" style=\"height:105px;width:305px;border-width:0px;\" /> </a> <div class=\"secondary-menu-container\"> <span id=\"ctl00_WagerLnk\"> <ul class=\"secondary-menu\"> <li class=\"has-sub\"><img width=\"20\" height=\"20\" src=\"http://logos.horizonsports.es/icons/football2.png\"><a id=\"ctl00_WagerLnk_lnkSports\" href=\"Sports.aspx\">Sports</a> <ul> <li><a id=\"ctl00_WagerLnk_lnkMatchups\" href=\"../matchups/\" target=\"_blank\">Matchups</a></li> <li><a id=\"ctl00_WagerLnk_lnkSchedule\" href=\"../schedule/\" target=\"_blank\">Schedule</a></li> </ul> </li> <li><img width=\"20\" height=\"20\" src=\"http://logos.horizonsports.es/icons/horses.png\"></li><li><img width=\"20\" height=\"20\" src=\"http://logos.horizonsports.es/icons/livebetting.png\"><a id=\"ctl00_WagerLnk_lnkLiveBet\" href=\"live.aspx?ws=0\"><b>[ExtraLive]</b></a></li> <li class=\"casino\"><img width=\"20\" height=\"20\" src=\"http://logos.horizonsports.es/icons/dice.png\"></li> <li class=\"has-sub\"><img width=\"20\" height=\"20\" src=\"http://logos.horizonsports.es/icons/player2.png\"><a id=\"ctl00_WagerLnk_lnkMyAccount\" href=\"Welcome.aspx\">My Account</a> <ul> <li><a id=\"ctl00_WagerLnk_lnkFillOpen\" href=\"FillChoose.aspx\">Fill Open</a></li> <li><a id=\"ctl00_WagerLnk_lnkOpenBets\" href=\"OpenBets.aspx\">Open Bets</a></li> <li><a id=\"ctl00_WagerLnk_lnkHistory\" href=\"History.aspx\">History</a></li> <li><a id=\"ctl00_WagerLnk_lnkFigures\" href=\"Welcome.aspx\">Figures</a></li> </ul> </li> <li class=\"logout\"><a id=\"ctl00_WagerLnk_lnkLogout\" href=\"../LogOut.aspx\">Logout</a><i class=\"fa fa-sign-out\"></i></li> </ul><ul></ul> </span> </div> </div></div><BR><BR><div class=\"main-menu-container\"> <div class=\"container\"> <ul class=\"nav menu md-menu\"> <li id=\"WT35\" class=\"tab red\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk0\" href=\"CreateSports.aspx?WT=35\">Bet It All</a></span></li> <li id=\"WT0\" class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk1\" href=\"CreateSports.aspx?WT=0\">Straight</a></span></li> <li id=\"WT1\" class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk2\" href=\"CreateSports.aspx?WT=1\">Parlay</a></span></li> <li id=\"WT2\" class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk3\" href=\"ChooseTeaser.aspx?WT=2\">Teaser</a></span></li> <li id=\"WT5\" class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk4\" href=\"CreateSports.aspx?WT=5\">Round Robin</a></span></li> <li id=\"WT3\" class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk5\" href=\"CreateSports.aspx?WT=3\">If Win Only</a></span></li> <li id=\"WT7\" class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk6\" href=\"CreateSports.aspx?WT=7\">If Win or Tie</a></span></li> <li id=\"WT4\" class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk7\" href=\"CreateSports.aspx?WT=4\">Win Reverse</a></span></li> <li id=\"WT8\" class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk8\" href=\"CreateSports.aspx?WT=8\">Action Reverse</a></span></li> <!--li class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk9\" href=\"CreateSports.aspx?WT=15\"><b>[Compact RoundRobin]</b></a></span><span class=\"tabrg\">&nbsp;</span></li> <li class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk10\" href=\"CreateSports.aspx?WT=16\"><b>[Key Parlay]</b></a></span><span class=\"tabrg\">&nbsp;</span></li> <li class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk11\" href=\"CreateSports.aspx?WT=17\"><b>[Compact IfWinOnly]</b></a></span><span class=\"tabrg\">&nbsp;</span></li> <li class=\"tab\"><span class=\"txt\"><a id=\"ctl00_SingleTemplateControl1_lnk12\" href=\"CreateSports.aspx?WT=18\"><b>[Compact IfWinOrTie]</b></a></span><span class=\"tabrg\">&nbsp;</span></li--> </ul> </div></div><div class=\"content-container\"> <div class=\"container\"> <div class=\"wagercontent\"> <center ><div class=\"header-lines-container\"><span class=\"league-header\"><img class=\"league-logo\" src=\"http://logos.horizonsports.es/leagues/NBA - 2ND HALVES.png\"><span class=\"league-name\">NBA - 2ND HALVES</span></span><span class=\"options\"><input type=\"submit\" name=\"ctl00$WagerContent$btn_Continue1\" value=\"Continue\" id=\"ctl00_WagerContent_btn_Continue1\" class=\"btn btn-default\" /><button type=\"submit\" class=\"btn btn-refresh\"><span>Refresh</span><i class=\"fa fa-refresh\"></i></button></span></div><TABLE CELLPADDING=\"0\" CELLSPACING=\"0\" BORDER=\"0\" WIDTH=\"100%\" ALIGN=\"center\"><TR CLASS=\"GameHeader\"><TD WIDTH=\"3%\"></TD><TD WIDTH=\"5%\">#</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><span>Team</span></TD><TD nowrap ALIGN=\"center\" WIDTH=\"20%\"><span>Spread</span></TD><TD nowrap ALIGN=\"center\" WIDTH=\"20%\"><span>Total</span></TD><TD nowrap ALIGN=\"center\" WIDTH=\"20%\"><span>M Line</span></TD></TR><TR ALIGN=\"left\" class=\"GameHeader gameBanner\"><TD COLSPAN=\"8\">NBA - 2ND HALVES Second Half Lines - Mar 06</TD></TR><TR ALIGN=\"left\" class=\"GameHeader gameBanner\"><TD COLSPAN=\"8\">NO PELICANS 67 - LA CLIPPERS 52</TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"right\" class=\"date\" COLSPAN=\"7\"><i class=\"fa fa-clock-o\"></i>Mar 06 11:52 PM </TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"left\"></TD><TD>2563</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/2H NO PELICANS.png\"> 2H NO PELICANS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"0_1593137_6.5_-110\" hidden=\"true\"><span class=\"line\">+6&frac12;-110</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"2_1593137_-113.5_-110\" hidden=\"true\"><span class=\"line\">o113&frac12;-110</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"4_1593137_0_335\" hidden=\"true\"><span class=\"line\">+335</span></label></div></TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"left\"></TD><TD>2514</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/2H LA CLIPPERS.png\"> 2H LA CLIPPERS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"1_1593137_-6.5_-110\" hidden=\"true\"><span class=\"line\">-6&frac12;-110</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"3_1593137_113.5_-110\" hidden=\"true\"><span class=\"line\">u113&frac12;-110</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"5_1593137_0_-425\" hidden=\"true\"><span class=\"line\">-425</span></label></div></TD></TR><TR ALIGN=\"left\" class=\"GameHeader gameBanner\"><TD COLSPAN=\"8\">BRO NETS 53 - GS WARRIORS 48</TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"right\" class=\"date\" COLSPAN=\"7\"><i class=\"fa fa-clock-o\"></i>Mar 06 11:49 PM </TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"left\"></TD><TD>2515</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/2H BRO NETS.png\"> 2H BRO NETS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"0_1593145_13.5_-110\" hidden=\"true\"><span class=\"line\">+13&frac12;-110</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"2_1593145_-119_-110\" hidden=\"true\"><span class=\"line\">o119-110</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"left\"></TD><TD>2516</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/2H GS WARRIORS.png\"> 2H GS WARRIORS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"1_1593145_-13.5_-110\" hidden=\"true\"><span class=\"line\">-13&frac12;-110</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"3_1593145_119_-110\" hidden=\"true\"><span class=\"line\">u119-110</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR ALIGN=\"left\" class=\"GameHeader gameBanner\"><TD COLSPAN=\"8\">NBA - 2ND HALVES Third Quarter Lines - Mar 07</TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"right\" class=\"date\" COLSPAN=\"7\"><i class=\"fa fa-clock-o\"></i>Mar 07 12:00 AM </TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"left\"></TD><TD>5563</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/3Q NO PELICANS.png\"> 3Q NO PELICANS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"0_1593141_3_-115\" hidden=\"true\"><span class=\"line\">+3-115</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"2_1593141_-57_-120\" hidden=\"true\"><span class=\"line\">o57-120</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"left\"></TD><TD>5514</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/3Q LA CLIPPERS.png\"> 3Q LA CLIPPERS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"1_1593141_-3_-115\" hidden=\"true\"><span class=\"line\">-3-115</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"3_1593141_57_-110\" hidden=\"true\"><span class=\"line\">u57-110</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"right\" class=\"date\" COLSPAN=\"7\"><i class=\"fa fa-clock-o\"></i>Mar 06 11:49 PM </TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"left\"></TD><TD>5515</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/3Q BRO NETS.png\"> 3Q BRO NETS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"0_1593149_6.5_100\" hidden=\"true\"><span class=\"line\">+6&frac12;EV</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"2_1593149_-60_-105\" hidden=\"true\"><span class=\"line\">o60-105</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"left\"></TD><TD>5516</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/3Q GS WARRIORS.png\"> 3Q GS WARRIORS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"1_1593149_-6.5_-130\" hidden=\"true\"><span class=\"line\">-6&frac12;-130</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"3_1593149_60_-125\" hidden=\"true\"><span class=\"line\">u60-125</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR ALIGN=\"left\" class=\"GameHeader gameBanner\"><TD COLSPAN=\"8\">NBA - 2ND HALVES Second Half Lines - Mar 07</TD></TR><TR ALIGN=\"left\" class=\"GameHeader gameBanner\"><TD COLSPAN=\"8\">NBA 2ND HALF TEAM TOTALS</TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"right\" class=\"date\" COLSPAN=\"7\"><i class=\"fa fa-clock-o\"></i>Mar 07 12:00 AM </TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"left\"></TD><TD>84025</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/2H PELICANS TOTAL POINTS.png\"> 2H PELICANS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"2_1593887_-53.5_-125\" hidden=\"true\"><span class=\"line\">o53&frac12;-125</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"left\"></TD><TD>84026</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/2H PELICANS TOTAL POINTS.png\"> 2H PELICANS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"3_1593887_53.5_-105\" hidden=\"true\"><span class=\"line\">u53&frac12;-105</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"right\" class=\"date\" COLSPAN=\"7\"><i class=\"fa fa-clock-o\"></i>Mar 07 12:00 AM </TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"left\"></TD><TD>84027</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/2H CLIPPERS TOTAL POINTS.png\"> 2H CLIPPERS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"2_1593889_-60_-115\" hidden=\"true\"><span class=\"line\">o60-115</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"left\"></TD><TD>84028</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/2H CLIPPERS TOTAL POINTS.png\"> 2H CLIPPERS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"3_1593889_60_-115\" hidden=\"true\"><span class=\"line\">u60-115</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"right\" class=\"date\" COLSPAN=\"7\"><i class=\"fa fa-clock-o\"></i>Mar 06 11:49 PM </TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"left\"></TD><TD>84029</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/2H NETS TOTAL POINTS.png\"> 2H NETS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"2_1593891_-53_-120\" hidden=\"true\"><span class=\"line\">o53-120</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"left\"></TD><TD>84030</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/2H NETS TOTAL POINTS.png\"> 2H NETS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"3_1593891_53_-110\" hidden=\"true\"><span class=\"line\">u53-110</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"right\" class=\"date\" COLSPAN=\"7\"><i class=\"fa fa-clock-o\"></i>Mar 06 11:49 PM </TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"left\"></TD><TD>84031</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/2H WARRIORS TOTAL POINTS.png\"> 2H WARRIORS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"2_1593893_-66.5_-115\" hidden=\"true\"><span class=\"line\">o66&frac12;-115</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"left\"></TD><TD>84032</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/2H WARRIORS TOTAL POINTS.png\"> 2H WARRIORS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"3_1593893_66.5_-115\" hidden=\"true\"><span class=\"line\">u66&frac12;-115</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR ALIGN=\"left\" class=\"GameHeader gameBanner\"><TD COLSPAN=\"8\">NBA - 2ND HALVES Third Quarter Lines - Mar 07</TD></TR><TR ALIGN=\"left\" class=\"GameHeader gameBanner\"><TD COLSPAN=\"8\">NBA 3RD QUARTER TEAM TOTALS</TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"right\" class=\"date\" COLSPAN=\"7\"><i class=\"fa fa-clock-o\"></i>Mar 07 12:00 AM </TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"left\"></TD><TD>87025</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/3Q PELICANS TOTAL POINTS.png\"> 3Q PELICANS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"2_1593888_-27_-115\" hidden=\"true\"><span class=\"line\">o27-115</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"left\"></TD><TD>87026</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/3Q PELICANS TOTAL POINTS.png\"> 3Q PELICANS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"3_1593888_27_-115\" hidden=\"true\"><span class=\"line\">u27-115</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"right\" class=\"date\" COLSPAN=\"7\"><i class=\"fa fa-clock-o\"></i>Mar 07 12:00 AM </TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"left\"></TD><TD>87027</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/3Q CLIPPERS TOTAL POINTS.png\"> 3Q CLIPPERS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"2_1593890_-30_-115\" hidden=\"true\"><span class=\"line\">o30-115</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"left\"></TD><TD>87028</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/3Q CLIPPERS TOTAL POINTS.png\"> 3Q CLIPPERS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"3_1593890_30_-115\" hidden=\"true\"><span class=\"line\">u30-115</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"right\" class=\"date\" COLSPAN=\"7\"><i class=\"fa fa-clock-o\"></i>Mar 06 11:49 PM </TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"left\"></TD><TD>87029</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/3Q NETS TOTAL POINTS.png\"> 3Q NETS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"2_1593892_-27_-110\" hidden=\"true\"><span class=\"line\">o27-110</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameOdd\"><TD ALIGN=\"left\"></TD><TD>87030</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/3Q NETS TOTAL POINTS.png\"> 3Q NETS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"3_1593892_27_-120\" hidden=\"true\"><span class=\"line\">u27-120</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"right\" class=\"date\" COLSPAN=\"7\"><i class=\"fa fa-clock-o\"></i>Mar 06 11:49 PM </TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"left\"></TD><TD>87031</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/3Q WARRIORS TOTAL POINTS.png\"> 3Q WARRIORS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"2_1593895_-33_-120\" hidden=\"true\"><span class=\"line\">o33-120</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR class=\"TrGameEven\"><TD ALIGN=\"left\"></TD><TD>87032</TD><TD COLSPAN=\"2\" ALIGN=\"left\"><IMG CLASS=\"BG_LOGO\" WIDTH=\"45\" HEIGHT=\"45\" SRC=\"http://logos.horizonsports.es/teams/nba/3Q WARRIORS TOTAL POINTS.png\"> 3Q WARRIORS TOTAL POINTS</TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><input type=\"checkbox\" name=\"text_\" value=\"3_1593895_33_-110\" hidden=\"true\"><span class=\"line\">u33-110</span></label></div></TD><TD ALIGN=\"center\"><div class=\"check-button\"><label><span class=\"empty\"></span></label></div></TD></TR><TR><TD COLSPAN=\"8\"></TD></TR></TABLE><BR></center> </div> </div></div><div class=\"footer-container\"> <div class=\"container\"> <span> <i class=\"fa fa-copyright\"></i> <span id=\"currentyear\">2018</span> 504bet.net All Rights Reserved </span> <a class=\"align-right btn btn-link\" href=\"/terms\" target=\"_blank\">Terms &amp; Conditions</a> </div></div></form> <script type=\"text/javascript\" src=\"../scripts/jquery.min.js\"></script> <script type=\"text/javascript\" src=\"../scripts/selector.js\"></script> <script type=\"text/javascript\" src=\"../scripts/teamnames.js\"></script> <script type=\"text/javascript\" src=\"http://arrow.scrolltotop.com/arrow59.js\"></script> <script type=\"text/javascript\"> $(function() { var casinoLink = $('ul.secondary-menu').children('.casino').children('a'); if (casinoLink.length <=0 ) { $('ul.secondary-menu').children('.casino').remove(); } }); </script></body></html>";
			Map<String, String> inputFields = new HashMap<String, String>();
			final List<SiteEventPackage> eventsPackage = names777TDSportsParser.parseGames(xhtml, null, inputFields);
			
			if (eventsPackage != null && eventsPackage.size() > 0) {
				for (SiteEventPackage siteEventPackage : eventsPackage) {
					System.out.println("SiteEventPackage: " + siteEventPackage);
				}
			}
*/

			String openBets = "<!DOCTYPE html><html><head id=\"ctl00_Head\">    <script async src=\"https://www.googletagmanager.com/gtag/js?id=UA-110080870-1\"></script>    <script>        window.dataLayer = window.dataLayer || [];        function gtag() { dataLayer.push(arguments); }        gtag('js', new Date());        gtag('config', 'UA-110080870-1');    </script>    <title>	SportsBook</title>    <!-- Required meta tags -->    <meta charset=\"utf-8\" /><meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\" />    <!-- Chrome, Firefox OS and Opera -->    <meta name=\"theme-color\" content=\"#212529\" /><meta name=\"apple-mobile-web-app-status-bar-style\" content=\"#212529\" />    <!-- Windows Phone -->    <meta name=\"msapplication-navbutton-color\" content=\"#212529\" />    <!-- Bootstrap CSS -->    <link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\" /><link href=\"https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.8/css/select2.min.css\" rel=\"stylesheet\" /><link href=\"../App_Themes/Responsive/assets/Alertify.1.2.267.0/css/alertify.min.css\" type=\"text/css\" rel=\"stylesheet\" /><link href=\"../App_Themes/Responsive/assets/Alertify.1.2.267.0/css/themes/bootstrap.min.css\" type=\"text/css\" rel=\"stylesheet\" /><link href=\"../App_Themes/Responsive/assets/Alertify.1.2.267.0/css/themes/bootstrap.rtl.min.css\" type=\"text/css\" rel=\"stylesheet\" /><link href=\"../App_Themes/Responsive/assets/Alertify.1.2.267.0/css/themes/default.min.css\" type=\"text/css\" rel=\"stylesheet\" /><link href=\"../App_Themes/Responsive/assets/Alertify.1.2.267.0/css/themes/default.rtl.min.css\" type=\"text/css\" rel=\"stylesheet\" /><link href=\"../App_Themes/Responsive/assets/Alertify.1.2.267.0/css/themes/semantic.min.css\" type=\"text/css\" rel=\"stylesheet\" /><link href=\"../App_Themes/Responsive/assets/Alertify.1.2.267.0/css/themes/semantic.rtl.min.css\" type=\"text/css\" rel=\"stylesheet\" /><link href=\"../App_Themes/Responsive/assets/css.1.2.267.0/icons.css\" type=\"text/css\" rel=\"stylesheet\" /><link href=\"../App_Themes/Responsive/assets/css.1.2.267.0/mobile.css\" type=\"text/css\" rel=\"stylesheet\" /><link href=\"../App_Themes/Responsive/assets/css.1.2.267.0/style.css\" type=\"text/css\" rel=\"stylesheet\" /><link href=\"../App_Themes/Responsive/assets/css.1.2.267.0/xs/default2019.css\" type=\"text/css\" rel=\"stylesheet\" /><link href=\"../App_Themes/Responsive/assets/selectize.1.2.267.0/css/selectize.bootstrap3.css\" type=\"text/css\" rel=\"stylesheet\" /><link href=\"../App_Themes/Responsive/assets/selectize.1.2.267.0/css/selectize.css\" type=\"text/css\" rel=\"stylesheet\" /><link href=\"../App_Themes/Responsive/assets/selectize.1.2.267.0/css/selectize.default.css\" type=\"text/css\" rel=\"stylesheet\" /><link href=\"../App_Themes/Responsive/assets/selectize.1.2.267.0/css/selectize.legacy.css\" type=\"text/css\" rel=\"stylesheet\" /></head><body class=\"t-body\">    <link rel=\"stylesheet\" href=\"/MS/ms-css2019.php?v=1.2.267.0\" />    <!--topmenu--><div id=\"ctl00_ctl00_MobileMenu\" class=\"top-menu mobile\">    <div class=\"header\">        <div class=\"row menu-container\">            <div class=\"col-4 main-menu-mobile d-flex align-items-center\">                <div class=\"burger-container\">                    <div id=\"burger\">                        <div class=\"bar topBar\"></div>                        <div class=\"bar mdBar\"></div>                        <div class=\"bar btmBar\"></div>                    </div>                </div>            </div>            <div class=\"col-4 brand text-center d-flex align-items-center\">                <a href=\"../wager/Welcome.aspx\">                    <img src=\"/MS/ms-img.php?t=logo\" id=\"ctl00_ctl00_LogoMobile\" style=\"width: 90%;\" />                </a>            </div>            <div class=\"col-2 search d-flex align-items-center\">                            </div>            <div class=\"col-2 user-container d-flex align-items-center pr-0\">                <span class=\"icon icon-user m-0\"></span>            </div>        </div>        <ul class=\"menu\" id=\"menu-products\">            <li class=\"menu-item\">                <a id=\"ctl00_ctl00_SportsMobile\" class=\"nav-link\" href=\"CreateSports.aspx\">                    <span class=\"icon icon icon-american-football\"></span>                    Sports</a>            </li>            <li class=\"menu-item\">                            </li>            <li class=\"menu-item\">                <a id=\"ctl00_ctl00_LiveCasinoMobile\" class=\"nav-link\" href=\"Bridge.aspx?to=LiveCasino\" target=\"_blank\"><span class=\"icon icon icon-dealer\"></span>Live Casino</a>            </li>            <li class=\"menu-item\">                            </li>            <li class=\"menu-item\">                            </li>            <li class=\"menu-item\">                <a id=\"ctl00_ctl00_LiveBettingMobile\" class=\"nav-link\" href=\"Bridge.aspx?to=LiveBetting\" target=\"_blank\">                    <span class=\"icon icon icon-live\"></span>                    Dynamic Live</a>            </li>                        <li class=\"menu-item\">                <a class=\"nav-link\" href=\"../../wager/OpenBets.aspx\"><span class=\"icon icon icon-list\"></span>                    Open Bets                </a>            </li>            <li class=\"menu-item\">                <a class=\"nav-link active\" href=\"../../wager/FillChoose.aspx\">                    <span class=\"icon icon icon-list\"></span>                    Fill Open                </a>            </li>            <li class=\"menu-item\">                <a class=\"nav-link\" href=\"../../wager/History.aspx\">                    <span class=\"icon icon icon-history\"></span>                    History                </a>            </li>        </ul>        <ul class=\"menu text-right w-100\" id=\"menu-user\">            <li class=\"menu-item\">                <a class=\"nav-link\" href=\"#\">                    Player:                    ID255                                    </a>            </li>            <li class=\"menu-item\">                <a class=\"nav-link\" href=\"../../wager/History.aspx\">                    Balance:                    <span class=\"text-primary\">                        5,152 </span>                </a>            </li>            <li class=\"menu-item\">                <a class=\"nav-link\" href=\"../../wager/History.aspx\">                    Available:                    <span class=\"text-success\">                        4,707 </span>                </a>            </li>            <li class=\"menu-item\">                <a class=\"nav-link\" href=\"../../wager/History.aspx\">                    At Risk:                    <span class=\"text-danger\" id=\"AtRisk_1\">                        5,445 </span>                </a>            </li>            <li class=\"menu-item Sports\">                <a class=\"nav-link\" href=\"#\" onclick=\"ShowSiteRules('sportsbook')\">                    Sportsbook Rules                    </a>            </li>            <li class=\"menu-item Horses\">                <a class=\"nav-link\" href=\"#\" onclick=\"ShowSiteRules('racebook')\">                    Racebook Rules                    </a>            </li>            <li class=\"menu-item Casino\">                <a class=\"nav-link\" href=\"#\" onclick=\"ShowSiteRules('casino')\">                    Casino Rules                </a>            </li>            <li class=\"menu-item\">                <a class=\"nav-link\" href=\"../../wager/History.aspx\">                    History                </a>            </li>            <li class=\"menu-item\">                <a class=\"nav-link\" href=\"../../wager/Welcome.aspx\">                     Home                    <span class=\"icon icon icon-home\"></span>                </a>            </li>                        <li class=\"menu-item\">                <a class=\"nav-link\" href=\"../../wager/PlayerSettings.aspx\">Manage Account <span class=\"icon icon-settings\"></span></a>            </li>            <li class=\"menu-item\">                <a class=\"nav-link\" href=\"../../LogOut.aspx\">Logout <span class=\"icon icon icon-sign-out\"></span></a>            </li>        </ul>    </div></div><!--desktop version topbar--><div id=\"ctl00_ctl00_NavDesktop\" class=\"top-bar-menu bg-secondary desktop no-print\">    <div class=\"container\">        <ul class=\"nav justify-content-start col-sm-4 py-1\" style=\"float: left;\">                    </ul>        <ul class=\"nav justify-content-end col-sm-8 py-2\">            <li class=\"nav-item\">                <a class=\"nav-link active\" href=\"../../wager/OpenBets.aspx\">                    Open Bets</a>            </li>            <li class=\"nav-item\">                <a class=\"nav-link active\" href=\"../../wager/FillChoose.aspx\">                    Fill Open</a>            </li>            <li class=\"nav-item\">                <a class=\"nav-link active\" href=\"../../wager/History.aspx\">                    History</a>            </li>            <li class=\"nav-item dropdown\">                <a class=\"nav-link dropdown-toggle\" href=\"../../wager/History.aspx\" data-toggle=\"dropdown\" href=\"#\" role=\"button\" aria-haspopup=\"true\" aria-expanded=\"false\">                    Available:                    <span class=\"text-success\">                        4,707                     </span></a>                <div class=\"dropdown-menu\">                    <a class=\"dropdown-item\" href=\"../../wager/History.aspx\">                        Balance:                        <span class=\"text-primary\">                            5,152 </span>                    </a>                    <div class=\"dropdown-divider\"></div>                    <a class=\"dropdown-item\" href=\"../../wager/History.aspx\">                        At Risk:                        <span class=\"text-danger\">                            5,445 </span>                    </a>                </div>            </li>            <li class=\"nav-item dropdown\">                <a class=\"nav-link dropdown-toggle pt-1\" data-toggle=\"dropdown\" href=\"#\" role=\"button\" aria-haspopup=\"true\" aria-expanded=\"false\"><span class=\"icon icon icon-user m-0\"></span></a>                <div class=\"dropdown-menu\">                    <a class=\"dropdown-item\" href=\"#\">                        ID255</a>                    <div class=\"dropdown-divider\"></div>                    <a class=\"dropdown-item\" href=\"#\" onclick=\"ShowSiteRules('sportsbook')\">                        Sportsbook Rules</a>                    <a class=\"dropdown-item\" href=\"#\" onclick=\"ShowSiteRules('racebook')\">                        Racebook Rules</a>                    <a class=\"dropdown-item\" href=\"#\" onclick=\"ShowSiteRules('casino')\">                        Casino Rules                    </a>                    <div class=\"dropdown-divider\"></div>                    <a class=\"dropdown-item\" href=\"../../wager/History.aspx\">                        History                    </a>                    <a class=\"dropdown-item\" href=\"../../wager/PlayerSettings.aspx\">Manage Account</a>                    <div class=\"dropdown-divider\"></div>                    <a class=\"dropdown-item\" href=\"../../LogOut.aspx\">Logout</a>                </div>            </li>        </ul>    </div></div><div class=\"container main horses\">    <div class=\"row\">        <div id=\"ctl00_ctl00_MenuDesktop\" class=\"col-sm-12 col-md-12 col-lg-2 py-3 px-0 desktop\">            <div class=\"row\">                <div class=\"brand col-sm-12 py-3 text-center\">                    <a href=\"../wager/Welcome.aspx\">                        <img src=\"/MS/ms-img.php?t=logo\" id=\"ctl00_ctl00_LogoDescktop\" style=\"width: 80%;\" />                    </a>                </div>            </div>            <div class=\"row no-print\">                <div class=\"col-sm-12 mt-3 sidebar-menu\">                    <div class=\"nav flex-column nav-pills\" role=\"tablist\" aria-orientation=\"vertical\">                        <a id=\"ctl00_ctl00_Sports\" class=\"nav-link rounded-0 Sports-option Sports\" href=\"CreateSports.aspx\">                            <span class=\"icon icon icon-american-football\"></span> Sports</a>                                                <a id=\"ctl00_ctl00_LiveCasino\" class=\"nav-link rounded-0 LiveCasino-option\" href=\"Bridge.aspx?to=LiveCasino\" target=\"_blank\">					       <span class=\"icon icon icon-dealer\"></span> Live Casino</a>                                                                        <a id=\"ctl00_ctl00_LiveBetting\" class=\"nav-link rounded-0 LiveBetting-option LiveBetting\" href=\"Bridge.aspx?to=LiveBetting\" target=\"_blank\">                            <span class=\"icon icon icon-live\"></span> Dynamic Live</a>                                            </div>                </div>            </div>        </div>        <!--desktop version content-->        <div id=\"DivConteiner\" class=\"col-sm-12 col-md-12 col-lg-10 main-content p-5\">            <!--betting progress-->    <form name=\"aspnetForm\" method=\"post\" action=\"OpenBets.aspx\" id=\"aspnetForm\"><div><input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"/wEPDwUJMzQ0MDYwMjIyD2QWAmYPZBYCAgEPZBYGZg9kFhQCAQ8WAh4Dc3JjBRUvTVMvbXMtaW1nLnBocD90PWxvZ29kAgMPFgIeB1Zpc2libGVoZAIHDw8WAh8BaGRkAgsPDxYCHwFoZGQCDQ8PFgIfAWhkZAIRDxYCHwFoZAIbDxYCHgRUZXh0BQVJRDI1NWQCHw8WAh8CBQY1LDE1MiBkAiMPFgIfAgUGNCw3MDcgZAInDxYCHwIFBjUsNDQ1IGQCAQ9kFggCCw8WAh8CBQY0LDcwNyBkAg8PFgIfAgUGNSwxNTIgZAITDxYCHwIFBjUsNDQ1IGQCFQ8WAh8CBQVJRDI1NWQCAg9kFgoCAQ8WAh8ABRUvTVMvbXMtaW1nLnBocD90PWxvZ29kAgUPDxYCHwFoZGQCCQ8PFgIfAWhkZAILDw8WAh8BaGRkAg8PDxYCHwFoZGRkQxnNSmZxDhti6tEke36Rw6HPp+A=\" /></div><div>	<input type=\"hidden\" name=\"__VIEWSTATEGENERATOR\" id=\"__VIEWSTATEGENERATOR\" value=\"FAA2D778\" /></div>            <div class=\"row main-panel\"  ><div class=\"col-sm-12 text-right no-print\"><button type=\"button\" class=\"btn btn-secondary btn-sm print-btn-openbets\" onclick=\"PrintOpenBets()\">Print</button></div><div class=\"col-sm-12\"><div class=\"table-responsive\"><table class=\"table report open-bets\"><thead><tr><th colspan=\"6\" class=\"bg-primary rounded-top text-center border-0\"><h3 class=\"m-0\">Open Wagers</h3></th></tr><tr class=\"bg-secondary\"><th>GameDate</th><th>User/Phone</th><th>Date Placed</th><th>Sport</th><th>Description</th><th>Risk/Win</th></tr></thead><tbody><tr><td>Ticket #:98129797<br />Oct 12 07:30 PM <BR /></td><td>INTERNET /                    -1</td><td>Oct 09 12:14 PM </td><td>CFB<BR /></td><td>STRAIGHT BET<br />[163] ARKANSAS +7-115<BR /></td><td>575 / 500</td></tr><tr><td>Ticket #:98131249<br />Oct 12 03:30 PM <BR /></td><td>INTERNET /                    -1</td><td>Oct 09 01:28 PM </td><td>CFB<BR /></td><td>STRAIGHT BET<br />[170] SOUTH FLORIDA +6Â½-110<BR /></td><td>440 / 400</td></tr><tr><td>Ticket #:98144899<br />Oct 12 12:00 PM <BR /></td><td>INTERNET /                    -1</td><td>Oct 09 07:13 PM </td><td>CFB<BR /></td><td>STRAIGHT BET<br />[135] MARYLAND -3-110<BR /></td><td>550 / 500</td></tr><tr><td>Ticket #:98160537<br />Oct 12 12:00 PM <BR /></td><td>MARTIN /                    4003</td><td>Oct 10 11:37 AM </td><td>CFB<BR /></td><td>STRAIGHT BET<br />[117] TOTAL o64Â½-110<BR>(TOLEDO vrs BOWLING GREEN)<BR /></td><td>550 / 500</td></tr><tr><td>Ticket #:98163133<br />Oct 10 08:20 PM <BR /></td><td>INTERNET /                    -1</td><td>Oct 10 01:31 PM </td><td>NFL<BR /></td><td>STRAIGHT BET<br />[104] TOTAL u42Â½-110 <BR>(NEW YORK GIANTS vrs NEW ENGLAND PATRIOTS) (TV Broadcast: FOX)<BR /></td><td>220 / 200</td></tr><tr><td>Ticket #:98163134<br />Oct 10 08:20 PM <BR /></td><td>INTERNET /                    -1</td><td>Oct 10 01:31 PM </td><td>MU<BR /></td><td>STRAIGHT BET<br />[100000410] TOTAL u13Â½-150 <BR>(NEW YORK GIANTS - TEAM TOTAL vrs NEW YORK GIANTS - TEAM TOTAL)<BR /></td><td>300 / 200</td></tr><tr><td>Ticket #:98164836<br />Oct 12 04:00 PM <BR /></td><td>INTERNET /                    -1</td><td>Oct 10 02:28 PM </td><td>CFB<BR /></td><td>STRAIGHT BET<br />[185] TOTAL o63Â½-110 <BR>(MIDDLE TENNESSEE STATE vrs FLORIDA ATLANTIC)<BR /></td><td>220 / 200</td></tr><tr><td>Ticket #:98165637<br />Oct 10 08:40 PM <BR /></td><td>INTERNET /                    -1</td><td>Oct 10 02:50 PM </td><td>NHL<BR /></td><td>STRAIGHT BET<br />[37] SAN JOSE SHARKS -110<BR /></td><td>220 / 200</td></tr><tr><td>Ticket #:98174713<br />Oct 10 08:00 PM <BR /></td><td>INTERNET /                    -1</td><td>Oct 10 06:02 PM </td><td>CFB<BR /></td><td>STRAIGHT BET<br />[108] NC STATE -4-110 (TV Broadcast: ESPN)<BR /></td><td>220 / 200</td></tr><tr><td>Ticket #:98178409<br />Oct 10 08:00 PM <BR /></td><td>INTERNET /                    -1</td><td>Oct 10 06:40 PM </td><td>CFB<BR /></td><td>STRAIGHT BET<br />[108] TOTAL u56Â½-110 <BR>(SYRACUSE vrs NC STATE) (TV Broadcast: ESPN)<BR /></td><td>550 / 500</td></tr><tr><td>Ticket #:98179165<br />Oct 10 08:40 PM <BR /></td><td>INTERNET /                    -1</td><td>Oct 10 06:45 PM </td><td>NHL<BR /></td><td>STRAIGHT BET<br />[37] TOTAL o6Â½-120 <BR>(SAN JOSE SHARKS vrs CHICAGO BLACKHAWKS)<BR /></td><td>240 / 200</td></tr><tr><td>Ticket #:98179167<br />Oct 10 08:35 PM <BR /></td><td>INTERNET /                    -1</td><td>Oct 10 06:45 PM </td><td>NHL<BR /></td><td>STRAIGHT BET<br />[100000145] TOTAL o3Â½+120 <BR>(SAN JOSE SHARKS - TEAM TOTAL vrs SAN JOSE SHARKS - TEAM TOTAL)<BR /></td><td>200 / 240</td></tr><tr><td>Ticket #:98191372<br />Oct 10 09:15 PM <BR /></td><td>INTERNET /                    -1</td><td>Oct 10 08:09 PM </td><td>MU<BR /></td><td>STRAIGHT BET<br />[98021] TOTAL o60Â½-130 <BR>(C TWYFORD (TSS) RSH YDS vrs C TWYFORD (TSS) RSH YDS) (Caleb Twyford (Texas State) Total Rushing Yards - Must Play For Action.)<BR /></td><td>260 / 200</td></tr><tr><td>Ticket #:98195671<br />Oct 10 09:50 PM <BR /></td><td>INTERNET /                    -1</td><td>Oct 10 09:35 PM </td><td>CFB<BR /></td><td>STRAIGHT BET<br />[2108] TOTAL u24-110 <BR>(2H SYRACUSE vrs 2H NC STATE) (Halftime Score: Syracuse 0 / NC State 13)<BR /></td><td>440 / 400</td></tr><tr><td>Ticket #:98199288<br />Oct 10 10:09 PM <BR /></td><td>INTERNET /                    -1</td><td>Oct 10 10:07 PM </td><td>NFL<BR /></td><td>STRAIGHT BET<br />[2104] 2H NEW ENGLAND PATRIOTS -7-115 (Halftime Score: Giants 14 / Patriots 21)<BR /></td><td>230 / 200</td></tr><tr><td>Ticket #:98199289<br />Oct 10 10:09 PM <BR /></td><td>INTERNET /                    -1</td><td>Oct 10 10:07 PM </td><td>NFL<BR /></td><td>STRAIGHT BET<br />[2104] TOTAL u21-115 <BR>(2H NEW YORK GIANTS vrs 2H NEW ENGLAND PATRIOTS) (Halftime Score: Giants 14 / Patriots 21)<BR /></td><td>230 / 200</td></tr><tr class=\"bg-secondary\"><td colspan=\"4\">Open Bets:16</td><td colspan=\"2\">Total Amount:5445 / 4840</td></tr></tbody></table></div></div></div>    </form>                    </div>    </div></div><div id=\"hiddenData\" class=\"d-none\">    <span id=\"ctl00_ctl01_PasswordMatch\">The Password and Confirmation Password must match.</span>    <span id=\"ctl00_ctl01_SuccessMessage_Responsive\">Data Updated !</span>    <span id=\"ctl00_ctl01_ErrorMessage_Responsive\">We Sorry,an Error Occurred !</span>    <span id=\"ctl00_ctl01_Msg_options_Responsive\">You Must Select at Least 1 Option</span>    <span id=\"ctl00_ctl01_Msg_Select_leagues_Responsive\">Please Select Your Bets</span>    <span id=\"ctl00_ctl01_PasswordLowerCase\">Passwords must be written in lowercase</span>    <span id=\"ctl00_ctl01_Msg_Select_Racing_Responsive\">Please Enter the Amount and Make a Selection</span>    <span id=\"ctl00_ctl01_FillOpenAlert\">Select {0} Games Maximum</span>    <span id=\"ctl00_ctl01_GamesAvailable\">{0} Games Available</span>    <span id=\"ctl00_ctl01_FindYourLeague\">Find Your League</span>    <span id=\"ctl00_ctl01_FindYourSports\">Find Your Games</span>            <span id='ctl00_ctl01_Msg_Select_Parlay_Responsive'>You Must Select at least 2 Bets</span><span id=\"ctl00_ctl01_Msg_Parley_Select_Items_Responsive\">{0} Teams Selected, {1} is the Maximum</span>    <span id=\"ctl00_ctl01_AllSports_Responsive\">ALL SPORTS</span>    <span id=\"ctl00_ctl01_HideBets_Responsive\">HIDE BETS</span>    <span id=\"ctl00_ctl01_ShowBets_Responsive\">MORE BETS</span>    <span id=\"ctl00_ctl01_NoGamesAvail\">There are no games available for the Specified Leagues</span>    <span id=\"ctl00_ctl01_SelectWagerTypeLarge\">Select your Wager Type</span>        <input id='H_Sport_UpComing' value='PROP' type='text' data-hidden='true' /></div><!-- JavaScript --><link rel=\"stylesheet\" href=\"../../App_Themes/Responsive/assets/css.1.2.267.0/mobile.css\"><script src=\"https://code.jquery.com/jquery-3.3.1.min.js\"></script><script src=\"../../App_Themes/Responsive/assets/js.1.2.267.0/jquery.blockUI.js\"></script><script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js\"></script><script src=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js\"></script><script src=\"../App_Themes/Responsive/assets/js.1.2.267.0/pageFunctions.js\"></script><script src=\"https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.8/js/select2.min.js\"></script><script src=\"../../App_Themes/Responsive/Site.js?v=1.2.267.0\" ></script><script type=\"text/javascript\" src=\"/MS/ms-js2019.php?v=1.2.267.0\" ></script>        <script src=\"../../App_Themes/Responsive/assets/Alertify.1.2.267.0/alertify.min.js\"></script>    <script src=\"../../App_Themes/Responsive/assets/selectize.1.2.267.0/js/standalone/selectize.min.js\"></script>    <script type=\"text/javascript\" src=\"../App_Themes/Responsive/assets/selectize.1.2.267.0/js/selectize.min.js\"></script>        </body></html>";
			final Set<PendingEvent> pendingEvents = names777TDSportsParser.parsePendingBets(openBets, "", "");

			if (pendingEvents != null && pendingEvents.size() > 0) {
				Iterator<PendingEvent> itr = pendingEvents.iterator();
				while (itr.hasNext()) {
					final PendingEvent pe = itr.next();
					LOGGER.debug("PendingEvent: " + pe);
				}
			}

			// Ticket #</span>Â <span class="text-success">97747307</span>
			final String tn = "<!DOCTYPE html><html><head id=\"ctl00_Head\">    <script async src=\"https://www.googletagmanager.com/gtag/js?id=UA-110080870-1\"></script>    <script>        window.dataLayer = window.dataLayer || [];        function gtag() { dataLayer.push(arguments); }        gtag('js', new Date());        gtag('config', 'UA-110080870-1');    </script>    <title>	SportsBook</title>    <!-- Required meta tags -->    <meta charset=\"utf-8\" /><meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\" />    <!-- Chrome, Firefox OS and Opera -->    <meta name=\"theme-color\" content=\"#212529\" /><meta name=\"apple-mobile-web-app-status-bar-style\" content=\"#212529\" />    <!-- Windows Phone -->    <meta name=\"msapplication-navbutton-color\" content=\"#212529\" />    <!-- Bootstrap CSS -->    <link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\" /><link href=\"https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.8/css/select2.min.css\" rel=\"stylesheet\" /><link href=\"../App_Themes/Responsive/assets/Alertify.1.2.264.0/css/alertify.min.css\" type=\"text/css\" rel=\"stylesheet\" /><link href=\"../App_Themes/Responsive/assets/Alertify.1.2.264.0/css/themes/bootstrap.min.css\" type=\"text/css\" rel=\"stylesheet\" /><link href=\"../App_Themes/Responsive/assets/Alertify.1.2.264.0/css/themes/bootstrap.rtl.min.css\" type=\"text/css\" rel=\"stylesheet\" /><link href=\"../App_Themes/Responsive/assets/Alertify.1.2.264.0/css/themes/default.min.css\" type=\"text/css\" rel=\"stylesheet\" /><link href=\"../App_Themes/Responsive/assets/Alertify.1.2.264.0/css/themes/default.rtl.min.css\" type=\"text/css\" rel=\"stylesheet\" /><link href=\"../App_Themes/Responsive/assets/Alertify.1.2.264.0/css/themes/semantic.min.css\" type=\"text/css\" rel=\"stylesheet\" /><link href=\"../App_Themes/Responsive/assets/Alertify.1.2.264.0/css/themes/semantic.rtl.min.css\" type=\"text/css\" rel=\"stylesheet\" /><link href=\"../App_Themes/Responsive/assets/css.1.2.264.0/icons.css\" type=\"text/css\" rel=\"stylesheet\" /><link href=\"../App_Themes/Responsive/assets/css.1.2.264.0/mobile.css\" type=\"text/css\" rel=\"stylesheet\" /><link href=\"../App_Themes/Responsive/assets/css.1.2.264.0/style.css\" type=\"text/css\" rel=\"stylesheet\" /><link href=\"../App_Themes/Responsive/assets/css.1.2.264.0/xs/default2019.css\" type=\"text/css\" rel=\"stylesheet\" /><link href=\"../App_Themes/Responsive/assets/selectize.1.2.264.0/css/selectize.bootstrap3.css\" type=\"text/css\" rel=\"stylesheet\" /><link href=\"../App_Themes/Responsive/assets/selectize.1.2.264.0/css/selectize.css\" type=\"text/css\" rel=\"stylesheet\" /><link href=\"../App_Themes/Responsive/assets/selectize.1.2.264.0/css/selectize.default.css\" type=\"text/css\" rel=\"stylesheet\" /><link href=\"../App_Themes/Responsive/assets/selectize.1.2.264.0/css/selectize.legacy.css\" type=\"text/css\" rel=\"stylesheet\" /></head><body class=\"t-body\">    <link rel=\"stylesheet\" href=\"/MS/ms-css2019.php?v=1.2.264.0\" />    <!--topmenu--><div id=\"ctl00_ctl00_MobileMenu\" class=\"top-menu mobile\">    <div class=\"header\">        <div class=\"row menu-container\">            <div class=\"col-4 main-menu-mobile d-flex align-items-center\">                <div class=\"burger-container\">                    <div id=\"burger\">                        <div class=\"bar topBar\"></div>                        <div class=\"bar mdBar\"></div>                        <div class=\"bar btmBar\"></div>                    </div>                </div>            </div>            <div class=\"col-4 brand text-center d-flex align-items-center\">                <a href=\"../wager/Welcome.aspx\">                    <img src=\"/MS/ms-img.php?t=logo\" id=\"ctl00_ctl00_LogoMobile\" style=\"width: 90%;\" />                </a>            </div>            <div class=\"col-2 search d-flex align-items-center\">                            </div>            <div class=\"col-2 user-container d-flex align-items-center pr-0\">                <span class=\"icon icon-user m-0\"></span>            </div>        </div>        <ul class=\"menu\" id=\"menu-products\">            <li class=\"menu-item\">                <a id=\"ctl00_ctl00_SportsMobile\" class=\"nav-link\" href=\"CreateSports.aspx\">                    <span class=\"icon icon icon-american-football\"></span>                    Sports</a>            </li>            <li class=\"menu-item\">                            </li>            <li class=\"menu-item\">                <a id=\"ctl00_ctl00_LiveCasinoMobile\" class=\"nav-link\" href=\"Bridge.aspx?to=LiveCasino\" target=\"_blank\"><span class=\"icon icon icon-dealer\"></span>Live Casino</a>            </li>            <li class=\"menu-item\">                            </li>            <li class=\"menu-item\">                <a id=\"ctl00_ctl00_HorsesMobile\" class=\"nav-link\" href=\"../racing/Racing.aspx?ws=0\">                    <span class=\"icon icon icon-horses\"></span>                    Horses</a>            </li>            <li class=\"menu-item\">                <a id=\"ctl00_ctl00_LiveBettingMobile\" class=\"nav-link\" href=\"Bridge.aspx?to=LiveBetting\" target=\"_blank\">                    <span class=\"icon icon icon-live\"></span>                    Dynamic Live</a>            </li>                        <li class=\"menu-item\">                <a class=\"nav-link\" href=\"../../wager/OpenBets.aspx\"><span class=\"icon icon icon-list\"></span>                    Open Bets                </a>            </li>            <li class=\"menu-item\">                <a class=\"nav-link active\" href=\"../../wager/FillChoose.aspx\">                    <span class=\"icon icon icon-list\"></span>                    Fill Open                </a>            </li>            <li class=\"menu-item\">                <a class=\"nav-link\" href=\"../../wager/History.aspx\">                    <span class=\"icon icon icon-history\"></span>                    History                </a>            </li>        </ul>        <ul class=\"menu text-right w-100\" id=\"menu-user\">            <li class=\"menu-item\">                <a class=\"nav-link\" href=\"#\">                    Player:                    NQ449                                    </a>            </li>            <li class=\"menu-item\">                <a class=\"nav-link\" href=\"../../wager/History.aspx\">                    Balance:                    <span class=\"text-primary\">                        0 </span>                </a>            </li>            <li class=\"menu-item\">                <a class=\"nav-link\" href=\"../../wager/History.aspx\">                    Available:                    <span class=\"text-success\">                        130 </span>                </a>            </li>            <li class=\"menu-item\">                <a class=\"nav-link\" href=\"../../wager/History.aspx\">                    At Risk:                    <span class=\"text-danger\" id=\"AtRisk_1\">                        7,370 </span>                </a>            </li>            <li class=\"menu-item Sports\">                <a class=\"nav-link\" href=\"#\" onclick=\"ShowSiteRules('sportsbook')\">                    Sportsbook Rules                    </a>            </li>            <li class=\"menu-item Horses\">                <a class=\"nav-link\" href=\"#\" onclick=\"ShowSiteRules('racebook')\">                    Racebook Rules                    </a>            </li>            <li class=\"menu-item Casino\">                <a class=\"nav-link\" href=\"#\" onclick=\"ShowSiteRules('casino')\">                    Casino Rules                </a>            </li>            <li class=\"menu-item\">                <a class=\"nav-link\" href=\"../../wager/History.aspx\">                    History                </a>            </li>            <li class=\"menu-item\">                <a class=\"nav-link\" href=\"../../wager/Welcome.aspx\">                     Home                    <span class=\"icon icon icon-home\"></span>                </a>            </li>                        <li class=\"menu-item\">                <a class=\"nav-link\" href=\"../../wager/PlayerSettings.aspx\">Manage Account <span class=\"icon icon-settings\"></span></a>            </li>            <li class=\"menu-item\">                <a class=\"nav-link\" href=\"../../LogOut.aspx\">Logout <span class=\"icon icon icon-sign-out\"></span></a>            </li>        </ul>    </div></div><!--desktop version topbar--><div id=\"ctl00_ctl00_NavDesktop\" class=\"top-bar-menu bg-secondary desktop no-print\">    <div class=\"container\">        <ul class=\"nav justify-content-start col-sm-4 py-1\" style=\"float: left;\">                    </ul>        <ul class=\"nav justify-content-end col-sm-8 py-2\">            <li class=\"nav-item\">                <a class=\"nav-link active\" href=\"../../wager/OpenBets.aspx\">                    Open Bets</a>            </li>            <li class=\"nav-item\">                <a class=\"nav-link active\" href=\"../../wager/FillChoose.aspx\">                    Fill Open</a>            </li>            <li class=\"nav-item\">                <a class=\"nav-link active\" href=\"../../wager/History.aspx\">                    History</a>            </li>            <li class=\"nav-item dropdown\">                <a class=\"nav-link dropdown-toggle\" href=\"../../wager/History.aspx\" data-toggle=\"dropdown\" href=\"#\" role=\"button\" aria-haspopup=\"true\" aria-expanded=\"false\">                    Available:                    <span class=\"text-success\">                        130                     </span></a>                <div class=\"dropdown-menu\">                    <a class=\"dropdown-item\" href=\"../../wager/History.aspx\">                        Balance:                        <span class=\"text-primary\">                            0 </span>                    </a>                    <div class=\"dropdown-divider\"></div>                    <a class=\"dropdown-item\" href=\"../../wager/History.aspx\">                        At Risk:                        <span class=\"text-danger\">                            7,370 </span>                    </a>                </div>            </li>            <li class=\"nav-item dropdown\">                <a class=\"nav-link dropdown-toggle pt-1\" data-toggle=\"dropdown\" href=\"#\" role=\"button\" aria-haspopup=\"true\" aria-expanded=\"false\"><span class=\"icon icon icon-user m-0\"></span></a>                <div class=\"dropdown-menu\">                    <a class=\"dropdown-item\" href=\"#\">                        NQ449</a>                    <div class=\"dropdown-divider\"></div>                    <a class=\"dropdown-item\" href=\"#\" onclick=\"ShowSiteRules('sportsbook')\">                        Sportsbook Rules</a>                    <a class=\"dropdown-item\" href=\"#\" onclick=\"ShowSiteRules('racebook')\">                        Racebook Rules</a>                    <a class=\"dropdown-item\" href=\"#\" onclick=\"ShowSiteRules('casino')\">                        Casino Rules                    </a>                    <div class=\"dropdown-divider\"></div>                    <a class=\"dropdown-item\" href=\"../../wager/History.aspx\">                        History                    </a>                    <a class=\"dropdown-item\" href=\"../../wager/PlayerSettings.aspx\">Manage Account</a>                    <div class=\"dropdown-divider\"></div>                    <a class=\"dropdown-item\" href=\"../../LogOut.aspx\">Logout</a>                </div>            </li>        </ul>    </div></div><div class=\"container main horses\">    <div class=\"row\">        <div id=\"ctl00_ctl00_MenuDesktop\" class=\"col-sm-12 col-md-12 col-lg-2 py-3 px-0 desktop\">            <div class=\"row\">                <div class=\"brand col-sm-12 py-3 text-center\">                    <a href=\"../wager/Welcome.aspx\">                        <img src=\"/MS/ms-img.php?t=logo\" id=\"ctl00_ctl00_LogoDescktop\" style=\"width: 80%;\" />                    </a>                </div>            </div>            <div class=\"row no-print\">                <div class=\"col-sm-12 mt-3 sidebar-menu\">                    <div class=\"nav flex-column nav-pills\" role=\"tablist\" aria-orientation=\"vertical\">                        <a id=\"ctl00_ctl00_Sports\" class=\"nav-link rounded-0 Sports-option Sports\" href=\"CreateSports.aspx\">                            <span class=\"icon icon icon-american-football\"></span> Sports</a>                                                <a id=\"ctl00_ctl00_LiveCasino\" class=\"nav-link rounded-0 LiveCasino-option\" href=\"Bridge.aspx?to=LiveCasino\" target=\"_blank\">					       <span class=\"icon icon icon-dealer\"></span> Live Casino</a>                                                <a id=\"ctl00_ctl00_Horses\" class=\"nav-link rounded-0 Horses-option Horses\" href=\"../racing/Racing.aspx?ws=0\">                            <span class=\"icon icon icon-horses\"></span> Horses</a>                        <a id=\"ctl00_ctl00_LiveBetting\" class=\"nav-link rounded-0 LiveBetting-option LiveBetting\" href=\"Bridge.aspx?to=LiveBetting\" target=\"_blank\">                            <span class=\"icon icon icon-live\"></span> Dynamic Live</a>                                            </div>                </div>            </div>        </div>        <!--desktop version content-->        <div id=\"DivConteiner\" class=\"col-sm-12 col-md-12 col-lg-10 main-content p-5\">            <!--betting progress-->    <form name=\"aspnetForm\" method=\"post\" action=\"PostWager.aspx?WT=0\" onkeypress=\"javascript:return WebForm_FireDefaultButton(event, 'ctl00_WagerContent_btn_Continue1')\" id=\"aspnetForm\"><div><input type=\"hidden\" name=\"__EVENTTARGET\" id=\"__EVENTTARGET\" value=\"\" /><input type=\"hidden\" name=\"__EVENTARGUMENT\" id=\"__EVENTARGUMENT\" value=\"\" /><input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"/wEPDwUJMjYxMDQ1MDg5DxYCHghXYWdlclhtbAXhDDx4bWwgQ3VycmVuY3lDb2RlPSJVU0QiIFBpdGNoZXJEZWZhdWx0PSIwIiBJZFVzZXI9IjAiIEZyZWVQbGF5QXZhaWxhYmxlPSIwLjAwIiBJZFByb2ZpbGU9IjcyMDgiIElkUHJvZmlsZUxpbWl0cz0iODQ4OCIgSWRQbGF5ZXI9IjYyODY0MyIgTGFzdElkV2FnZXI9Ijk4MDc3NDU3IiBJZENhbGw9IjEzMTIyNjg3MCIgV2FnZXJUcmFja2VyPSI2OTQwNjIyNSIgSWZCZXRBc2tBbW91bnQ9IlRydWUiIENvbmZpcm09IlRydWUiIEVycm9yQ29kZT0iMCIgRXJyb3JNc2dLZXk9IiIgRXJyb3JNc2dQYXJhbXM9IiIgRXJyb3JNc2c9IiIgVXNlRnJlZVBsYXlBdmFpbGFibGU9IkZhbHNlIiBTZWN1cml0eUNvZGU9IkRCMDNDRkE5RkJBMzRGRUYzMjI5QjUzQzZBNUU5OTA3IiBJZkJldFJpc2s9IjAiIElmQmV0V2luPSIwIj48d2FnZXIgV2FnZXJUeXBlRGVzYz0iU1RSQUlHSFQgQkVUIiBXYWdlclR5cGVEZXNjRW5nPSJTVFJBSUdIVCBCRVQiIFdhZ2VyVHlwZT0iMCIgVGlja2V0TnVtYmVyPSI5ODA3OTIwMyIgVGVhc2VyQ2FuQnV5SGFsZj0iMCIgVGVhc2VyQ2FuQnV5T25lPSIwIiBUZWFzZXJQb2ludHNQdXJjaGFzZWQ9IjAiIEZpbGxJZFdhZ2VyPSItMSIgQW1vdW50PSIzNTAuMCIgUmlzaz0iMzY4LjAwIiBXaW49IjM1MC4wMCIgSURXVD0iMjI1MTk5IiBSaXNrV2luPSIxIiBSb3VuZFJvYmluQ29tYmluYXRpb25zPSIwIiBDb21wYWN0Q29tYmluYXRpb25zPSIiPjxkZXRhaWwgSXNNTGluZT0iRmFsc2UiIFRlYW1EZXNjcmlwdGlvbj0iWzIwNl0gU0FOIERJRUdPIFNUQVRFIiBUZWFtRGVzY3JpcHRpb25Fbmc9IlsyMDZdIFNBTiBESUVHTyBTVEFURSIgVmVyc3VzVGVhbURlc2NyaXB0aW9uPSJbMjA1XSBXWU9NSU5HIiBMaW5lRGVzY3JpcHRpb249Ii0zJmFtcDtmcmFjMTI7LTEwNSIgUHJldmlvdXNMaW5lRGVzY3JpcHRpb249IiIgRGVzY3JpcHRpb249IlsyMDZdIFNBTiBESUVHTyBTVEFURSAtMyZhbXA7ZnJhYzEyOy0xMDUiIERlc2NyaXB0aW9uRW5nPSJbMjA2XSBTQU4gRElFR08gU1RBVEUgLTMmYW1wO2ZyYWMxMjstMTA1IiBJZFNwb3J0PSJDRkIiIElkR2FtZVR5cGU9IjYxIiBQYXJlbnRHYW1lPSIxOTI3NjgzIiBGYW1pbHlHYW1lPSIxOTI3NjgzIiBJZEV2ZW50PSIwIiBFdmVudERlc2NyaXB0aW9uPSIiIFBlcmlvZD0iMCIgQ2FuQ2hvb3NlUGl0Y2hlcj0iRmFsc2UiIE1hcmtGb3JEZWxldGlvbj0iRmFsc2UiIFBvaW50c1B1cmNoYXNlZEp1aWNlPSIwIiBPcmlnaW5hbE9kZHM9Ii0xMDUiIE9yaWdpbmFsUG9pbnRzPSItMy41IiBWaXNpdG9yUGl0Y2hlcj0iIiBIb21lUGl0Y2hlcj0iIiBHYW1lRGF0ZVRpbWU9IjEwLTEyLTIwMTkgMjI6MzA6MDAiIEdhbWVEYXRlPSJPY3QgMTIiIEdhbWVUaW1lPSIxMDozMCBQTSIgSXNGYXZvcml0ZT0iVHJ1ZSIgSXNMaW5lRnJvbUFnZW50PSJGYWxzZSIgVmVyc3VzUGFyZW50RGVzY3JpcHRpb249IiIgSWRHYW1lPSIxOTI3NjgzIiBQbGF5PSIxIiBQb2ludHM9Ii0zLjUiIE9kZHM9Ii0xMDUiIFBpdGNoZXI9IjAiIElzT25PcGVuQmV0cz0iRmFsc2UiIFBvaW50c1B1cmNoYXNlZD0iMCIgSXNTcHJlYWQ9IlRydWUiIElzVG90YWw9IkZhbHNlIiBJc0tleT0iRmFsc2UiIENoYXJ0Umlzaz0iMCIgQ2hhcnRXaW49IjAiIC8+PC93YWdlcj48L3htbD4WAmYPZBYEAgEPZBYGZg9kFhICAQ8WAh4Dc3JjBRUvTVMvbXMtaW1nLnBocD90PWxvZ29kAgMPFgIeB1Zpc2libGVoZAIHDw8WAh8CaGRkAgsPDxYCHwJoZGQCEQ8WAh8CaGQCGw8WAh4EVGV4dAUFTlE0NDlkAh8PFgIfAwUCMCBkAiMPFgIfAwUEMTMwIGQCJw8WAh8DBQY3LDM3MCBkAgEPZBYIAgsPFgIfAwUEMTMwIGQCDw8WAh8DBQIwIGQCEw8WAh8DBQY3LDM3MCBkAhUPFgIfAwUFTlE0NDlkAgIPZBYIAgEPFgIfAQUVL01TL21zLWltZy5waHA/dD1sb2dvZAIFDw8WAh8CaGRkAgkPDxYCHwJoZGQCDw8PFgIfAmhkZAICD2QWAgIBD2QWAmYPZBYYAgEPDxYGHghDc3NDbGFzcwUaYnRuLXByb2dyZXNzIHJvdW5kZWQtMCBjb2weC05hdmlnYXRlVXJsBRl+L3dhZ2VyL0NyZWF0ZVNwb3J0cy5hc3B4HgRfIVNCAgJkZAIDDw8WBh8EBRpidG4tcHJvZ3Jlc3Mgcm91bmRlZC0wIGNvbB8FBR5+L3dhZ2VyL0NyZWF0ZVNwb3J0cy5hc3B4P1dUPTAfBgICZGQCBQ8PFgYfBAUaYnRuLXByb2dyZXNzIHJvdW5kZWQtMCBjb2wfBQUhfi93YWdlci9TY2hlZHVsZS5hc3B4P1dUPTAmbGc9MTM1HwYCAmRkAgcPDxYGHwQFGmJ0bi1wcm9ncmVzcyByb3VuZGVkLTAgY29sHwUFUH4vd2FnZXIvQ3JlYXRlV2FnZXIuYXNweD9XVD0wJmxnPTEzNSZzZWw9MV8xOTI3NjgzXy0zLjVfLTEwNV8xXzE5Mjc2ODNfLTMuNV8tMTA1HwYCAmRkAgkPDxYGHwQFGmJ0bi1wcm9ncmVzcyByb3VuZGVkLTAgY29sHwUFHn4vd2FnZXIvQ29uZmlybVdhZ2VyLmFzcHg/V1Q9MB8GAgJkZAILDw8WBh8EBSBidG4tcHJvZ3Jlc3Mgcm91bmRlZC0wIGNvbCBwdWxzZR8FBRt+L3dhZ2VyL1Bvc3RXYWdlci5hc3B4P1dUPTAfBgICZGQCEw8PFgYfBAURYnRuIGJ0bi1zZWNvbmRhcnkfBQUZfi93YWdlci9DcmVhdGVTcG9ydHMuYXNweB8GAgJkZAIVDw8WBh8EBRFidG4gYnRuLXNlY29uZGFyeR8FBR5+L3dhZ2VyL0NyZWF0ZVNwb3J0cy5hc3B4P1dUPTAfBgICZGQCFw8PFgYfBAURYnRuIGJ0bi1zZWNvbmRhcnkfBQUhfi93YWdlci9TY2hlZHVsZS5hc3B4P1dUPTAmbGc9MTM1HwYCAmRkAhkPDxYGHwQFEWJ0biBidG4tc2Vjb25kYXJ5HwUFUH4vd2FnZXIvQ3JlYXRlV2FnZXIuYXNweD9XVD0wJmxnPTEzNSZzZWw9MV8xOTI3NjgzXy0zLjVfLTEwNV8xXzE5Mjc2ODNfLTMuNV8tMTA1HwYCAmRkAhsPDxYGHwQFEWJ0biBidG4tc2Vjb25kYXJ5HwUFHn4vd2FnZXIvQ29uZmlybVdhZ2VyLmFzcHg/V1Q9MB8GAgJkZAIdDw8WBh8EBQ5idG4gYnRuLWFjdGl2ZR8FBRt+L3dhZ2VyL1Bvc3RXYWdlci5hc3B4P1dUPTAfBgICZGRkl2zaQbMU3lzgXqFG1w+R/OXHi+8=\" /></div><script type=\"text/javascript\">//<![CDATA[var theForm = document.forms['aspnetForm'];if (!theForm) {    theForm = document.aspnetForm;}function __doPostBack(eventTarget, eventArgument) {    if (!theForm.onsubmit || (theForm.onsubmit() != false)) {        theForm.__EVENTTARGET.value = eventTarget;        theForm.__EVENTARGUMENT.value = eventArgument;        theForm.submit();    }}//]]></script><script src=\"/WebResource.axd?d=L39JtK-AQZvLXavGRsS9SkxZQ6LEhu1L0_jg4-BgeTxPYgKUizVEALYU4pBV4mvAmNTMPcKzpS-n_bY3N5VjxHG62mg1&amp;t=636284489597151108\" type=\"text/javascript\"></script><div>	<input type=\"hidden\" name=\"__VIEWSTATEGENERATOR\" id=\"__VIEWSTATEGENERATOR\" value=\"3CBE4D1D\" />	<input type=\"hidden\" name=\"__EVENTVALIDATION\" id=\"__EVENTVALIDATION\" value=\"/wEWAgLas7qeBALqlbKLCS63sKKhShXvCvrnc8U5lg5N3bw0\" /></div>            <!--betting progress-->    <!--mobile controls--><div class=\"progress-controls mobile pulse\">    <!--progress bar mobile -->    <div class=\"row mobile-progress-bar\">        <a id=\"ctl00_WagerContent_ctl00_Step1Mobile\" class=\"btn-progress rounded-0 col\" href=\"CreateSports.aspx\"></a>        <a id=\"ctl00_WagerContent_ctl00_Step2Mobile\" class=\"btn-progress rounded-0 col\" href=\"CreateSports.aspx?WT=0\"></a>        <a id=\"ctl00_WagerContent_ctl00_Step3Mobile\" class=\"btn-progress rounded-0 col\" href=\"Schedule.aspx?WT=0&amp;lg=135\"></a>        <a id=\"ctl00_WagerContent_ctl00_Step4Mobile\" class=\"btn-progress rounded-0 col\" href=\"CreateWager.aspx?WT=0&amp;lg=135&amp;sel=1_1927683_-3.5_-105_1_1927683_-3.5_-105\"></a>        <a id=\"ctl00_WagerContent_ctl00_Step5Mobile\" class=\"btn-progress rounded-0 col\" href=\"ConfirmWager.aspx?WT=0\"></a>        <a id=\"ctl00_WagerContent_ctl00_Step6Mobile\" class=\"btn-progress rounded-0 col pulse\" href=\"PostWager.aspx?WT=0\"></a>        <div class=\"col-sm-12 text-center py-1\">            <p class=\"mb-0\">                <small>Review and Print</small>            </p>        </div>    </div>    <!--back continue clear button -->    <div class=\"row panel-buttons\">                <button type=\"button\" class=\"btn btn-clear rounded-0 border-0 col\" data-btnrefresh=\"true\">Clear</button>        <button type=\"button\" class=\"btn btn-continue rounded-0 border-0 col\" data-waitscreen=\"true\" data-btncontinue=\"true\">            Continue        </button>    </div></div><!--betting progress--><div class=\"row text-center betting-progress-bar desktop no-print\">    <h2 class=\"m-auto\">        Betting Progress</h2>    <div class=\"col-sm-12 py-3\">        <div class=\"p-3 bg-light border rounded\">            <a id=\"ctl00_WagerContent_ctl00_Step1\" class=\"btn btn-secondary\" href=\"CreateSports.aspx\">Select Wager Type</a>            <a id=\"ctl00_WagerContent_ctl00_Step2\" class=\"btn btn-secondary\" href=\"CreateSports.aspx?WT=0\">Select Sport</a>            <a id=\"ctl00_WagerContent_ctl00_Step3\" class=\"btn btn-secondary\" href=\"Schedule.aspx?WT=0&amp;lg=135\">Select teams</a>            <a id=\"ctl00_WagerContent_ctl00_Step4\" class=\"btn btn-secondary\" href=\"CreateWager.aspx?WT=0&amp;lg=135&amp;sel=1_1927683_-3.5_-105_1_1927683_-3.5_-105\">Enter Amount</a>            <a id=\"ctl00_WagerContent_ctl00_Step5\" class=\"btn btn-secondary\" href=\"ConfirmWager.aspx?WT=0\">Confirm Wager</a>            <a id=\"ctl00_WagerContent_ctl00_Step6\" class=\"btn btn-active\" href=\"PostWager.aspx?WT=0\">Review and Print</a>        </div>    </div></div>    <style  >      .progress-controls.mobile{      height:auto;      }      .panel-buttons{      display:none;      }    </style><div class=\"row main-panel review\"  ><div class=\"col-sm-12 mt-3\"><ul class=\"list-group\"><li class=\"list-group-item bg-primary text-center rounded-top\"><h3 class=\"mb-0\">STRAIGHT BET</h3></li><li class=\"list-group-item\"><div class=\"row\"><div class=\"col-sm-2 bet-type py-1\"><strong class=\"float-right\">Oct 12</strong></div><div class=\"col-sm-5 description py-1\">CFB [206] SAN DIEGO STATE -3&frac12;-105 </div><div class=\"col-sm-5 amount py-1\"><span class=\"risk text-danger\">368.00 USD</span>/<span class=\"win text-success\">350.00 USD</span><span class=\"ticket float-right\"><strong><span>Ticket #</span>Â <span class=\"text-success\">98079203</span></strong></span></div></div></li></ul></div><div class=\"col-sm-12 text-center my-5 no-print\"><div class=\"row px-3\" role=\"group\" aria-label=\"review buttons\"><input type=\"submit\" name=\"ctl00$WagerContent$btn_Continue1\" value=\"Click Here\" id=\"ctl00_WagerContent_btn_Continue1\" data-id=\"BTNcontinue\" class=\"d-none\" /><button type=\"button\" class=\"btn btn-outline-secondary col-sm p-3 m-1\" onclick=\"$(&quot;input[data-id=BTNcontinue]&quot;).click();\"><h3><span class=\"icon icon icon-re-bet\"></span><span class=\"d-block\"><span>To place another bet.</span></span></h3></button><a id=\"ctl00_WagerContent_lnkOpenBets\" class=\"btn btn-outline-secondary col-sm p-3 m-1\" href=\"OpenBets.aspx\"><h3><span class=\"d-block\">Pending Bets</span><span class=\"d-block\"><small class=\"text-lowercase\"><span>To review your pending plays and to ensure your wagers were placed correctly.</span></small></span></h3></a><button onclick=\"javascript:window.print();\" type=\"button\" class=\"btn btn-outline-secondary col-sm p-3 m-1\"><h3><span class=\"icon icon icon-print\"></span><span class=\"d-block\"><span>Print Ticket</span></span></h3></button></div></div></div>            </form>                    </div>    </div></div><div id=\"hiddenData\" class=\"d-none\">    <span id=\"ctl00_ctl01_PasswordMatch\">The Password and Confirmation Password must match.</span>    <span id=\"ctl00_ctl01_SuccessMessage_Responsive\">Data Updated !</span>    <span id=\"ctl00_ctl01_ErrorMessage_Responsive\">We Sorry,an Error Occurred !</span>    <span id=\"ctl00_ctl01_Msg_options_Responsive\">You Must Select at Least 1 Option</span>    <span id=\"ctl00_ctl01_Msg_Select_leagues_Responsive\">Please Select Your Bets</span>    <span id=\"ctl00_ctl01_PasswordLowerCase\">Passwords must be written in lowercase</span>    <span id=\"ctl00_ctl01_Msg_Select_Racing_Responsive\">Please Enter the Amount and Make a Selection</span>    <span id=\"ctl00_ctl01_FillOpenAlert\">Select {0} Games Maximum</span>    <span id=\"ctl00_ctl01_GamesAvailable\">{0} Games Available</span>    <span id=\"ctl00_ctl01_FindYourLeague\">Find Your League</span>    <span id=\"ctl00_ctl01_FindYourSports\">Find Your Games</span>            <span id='ctl00_ctl01_Msg_Select_Parlay_Responsive'>You Must Select at least 2 Bets or 1 Bet and 1 Open Spot</span><span id=\"ctl00_ctl01_Msg_Parley_Select_Items_Responsive\">{0} Teams Selected, {1} is the Maximum</span>    <span id=\"ctl00_ctl01_AllSports_Responsive\">ALL SPORTS</span>    <span id=\"ctl00_ctl01_HideBets_Responsive\">HIDE BETS</span>    <span id=\"ctl00_ctl01_ShowBets_Responsive\">MORE BETS</span>    <span id=\"ctl00_ctl01_NoGamesAvail\">There are no games available for the Specified Leagues</span>    <span id=\"ctl00_ctl01_SelectWagerTypeLarge\">Select your Wager Type</span>        </div><!-- JavaScript --><link rel=\"stylesheet\" href=\"../../App_Themes/Responsive/assets/css.1.2.264.0/mobile.css\"><script src=\"https://code.jquery.com/jquery-3.3.1.min.js\"></script><script src=\"../../App_Themes/Responsive/assets/js.1.2.264.0/jquery.blockUI.js\"></script><script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js\"></script><script src=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js\"></script><script src=\"../App_Themes/Responsive/assets/js.1.2.264.0/pageFunctions.js\"></script><script src=\"https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.8/js/select2.min.js\"></script><script src=\"../../App_Themes/Responsive/Site.js?v=1.2.264.0\" ></script><script type=\"text/javascript\" src=\"/MS/ms-js2019.php?v=1.2.264.0\" ></script>        <script src=\"../../App_Themes/Responsive/assets/Alertify.1.2.264.0/alertify.min.js\"></script>    <script src=\"../../App_Themes/Responsive/assets/selectize.1.2.264.0/js/standalone/selectize.min.js\"></script>    <script type=\"text/javascript\" src=\"../App_Themes/Responsive/assets/selectize.1.2.264.0/js/selectize.min.js\"></script>        </body></html>";
			String ticketNumber = names777TDSportsParser.parseTicketNumber(tn);
			LOGGER.error("TicketNumber: " + ticketNumber);
		} catch (BatchException be) {
			LOGGER.error(be.getMessage(), be);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.tdsports.TDSportsParser#parsePendingBets(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
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
		final Elements trs = doc.select(".open-bets tbody tr");
		LOGGER.debug("trs: " + trs);

		if (trs != null && trs.size() > 0) {
			for (Element tr : trs) {
				PendingEvent pe = null;
				int x = 0;

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
								getTicketNumberAndDate(td, pe);
								break;
							case 1:
								// do nothing
								break;
							case 2:
								pe.setDateaccepted(td.html().trim());
								break;
							case 3:
								getSport(td.html().trim(), pe);
								break;
							case 4:
								getGameInfo(td.html().trim(), pe);
								break;
							case 5:
								getRiskWin(td, pe);
								break;
							default:
								break;
							}
						}
					}
					pe.setDoposturl(false);

					// Quick check if WNBA
					if ("NBA".equals(pe.getGametype())) {
						String peTeam = pe.getTeam();
						if (peTeam != null) {
							for (String wnbaTeam : WNBA_TEAMS) {
								if (peTeam.contains(wnbaTeam)) {
									pe.setGametype("WNBA");
									break;
								}
							}
						}
					}

					pendingEvents.add(pe);
				} catch (Throwable t) {
					LOGGER.warn(t.getMessage(), t);

					if (pe != null) {
						pe.setGamesport("");
						pe.setGametype("");
					}
				}
			}	
		}

		LOGGER.info("Exiting parsePendingBets()");
		return pendingEvents;
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

		if (sport != null && sport.length > 0) {
			for (String sportName : sport) {
				if (sportName.contains("MLB") || sportName.contains("mlb") || sportName.contains("BASEBALL")) {
					isMlb = true;
				}
			}
		}
		LOGGER.debug("isMlb: " + isMlb);

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
		
		if (xhtml.contains("Your bet expired or has already been submited.")) {
			throw new BatchException(BatchErrorCodes.WAGER_HAS_EXPIRED, BatchErrorMessage.WAGER_HAS_EXPIRED, "The wager has expired or already been bet", xhtml);	
		}

		// Find the different menu types
		map = findMenu(doc.select(".sport-league li"), map, type, sport, "label", "input");
		LOGGER.debug("map: " + map);

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

		if (xhtml.contains("Your bet expired or has already been submited.")) {
			throw new BatchException(BatchErrorCodes.WAGER_HAS_EXPIRED, BatchErrorMessage.WAGER_HAS_EXPIRED, "The wager has expired or already been bet", xhtml);	
		}

		// abcgrand.ag
		Elements elements = doc.select(".odds");
		if ((events == null || events.size() == 0) && elements != null && elements.size() > 0) {
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

	@Override
	public String parseTicketNumber(String xhtml) throws BatchException {
		LOGGER.info("Entering parseTicketNumber()");
		String ticketNumber = "";
		final String ticketInfo = "Ticket #</span>Â <span class=\"text-success\">";

		if (xhtml.contains("Your bet expired or has already been submited.")) {
			throw new BatchException(BatchErrorCodes.WAGER_HAS_EXPIRED, BatchErrorMessage.WAGER_HAS_EXPIRED, "The wager has expired or already been bet", xhtml);	
		}
		
		if (xhtml.contains("The line changed for one (or more) of your selections.")) {
			// Line changed throw exception
			throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed and cannot complete transaction", xhtml);	
		}

		LOGGER.error("xhtml: " + xhtml);

		// First check for Wager has been accepted!
		if (xhtml != null && xhtml.contains("Ticket #")) {
//			<span class="ticket float-right">
//				<strong>
//					<span>Ticket #</span>Â 
//					<span class="text-success">98079203</span>
//				</strong>
//			</span>
			final Document doc = parseXhtml(xhtml);
			final Elements spans = doc.select(".ticket strong span");

			int count = 0;
			for (Element span : spans) {
				if (count++ == 1) {
					ticketNumber = span.html().trim();
				}
			}
		} else {
			throw new BatchException(BatchErrorCodes.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, BatchErrorMessage.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, ticketNumber, xhtml);
		}

		LOGGER.info("Exiting parseTicketNumber()");
		return ticketNumber;
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 * @throws BatchException
	 */
	protected void getEventData(Element td, PendingEvent pe) throws BatchException {
		LOGGER.info("Entering getEventData()");

		// <strong>
		//   STRAIGHT BET
		//   <br>
		// </strong>
		// MLB - [970] CLE INDIANS -132
		// <br>
		// ( C SALE -L / T BAUER -R )
		// <span class="date game-date">Sep 21 07:10 PM </span>
		// <br>
		
		// <strong>
		//   STRAIGHT BET
		//   <br>
		// </strong>
		// CFB - [374] TOTAL u60½-110
		// <br>
		// (NAVY vrs SMU)
		// <span class="date game-date">Sep 22 12:00 PM </span>
		// <br>

		// <strong>
		//  STRAIGHT BET
		//  <br>
		// </strong>
		// CFB - [309] WASHINGTON STATE +4½-110
		// <span class="date game-date">Sep 21 10:30 PM </span>
		// <br>
		String html = td.html().trim();
		LOGGER.debug("html: " + html);
		if (html != null && html.contains("STRAIGHT BET")) {
			int index = html.indexOf("</strong>");
			if (index != -1) {
				html = html.substring(index + "</strong>".length());
				index = html.indexOf(" - ");
				if (index != -1) {
					String gameType = html.substring(0, index).trim();
					if ("NFL".equals(gameType)) {
						pe.setGamesport("Football");
						pe.setGametype(gameType);
					} else if ("CFB".equals(gameType)) {
						pe.setGamesport("Football");
						pe.setGametype("NCAA");				
					} else if ("NBA".equals(gameType)) {
						pe.setGamesport("Basketball");
						pe.setGametype(gameType);
					} else if ("CBB".equals(gameType)) {
						pe.setGamesport("Basketball");
						pe.setGametype("NCAA");
					} else if ("WNBA".equals(html)) {
						pe.setGamesport("Basketball");
						pe.setGametype(gameType);
					} else if ("NHL".equals(gameType)) {
						pe.setGamesport("Hockey");
						pe.setGametype(gameType);
					} else if ("MLB".equals(gameType)) {
						pe.setGamesport("Baseball");
						pe.setGametype(gameType);				
					} else if ("MU".equals(gameType)) {
						pe.setGamesport("Golf");
						pe.setGametype(gameType);
					}
				}

				html = html.substring(index + " - ".length());
				getGameInfo(html, pe);
			}
		}

		LOGGER.info("Exiting getEventData()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.tdsports.TDSportsParser#getGameData(org.jsoup.select.Elements)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected List<TDSportsEventPackage> getGameData(Elements elements) throws BatchException {
		LOGGER.info("Entering getGameData()");
		TDSportsEventPackage eventPackage = null;
		final List<TDSportsEventPackage> events = new ArrayList<TDSportsEventPackage>();

		if (elements != null) {
			for (int x = 0; x < elements.size(); x++) {
				Element tbody = elements.get(x);
				TDSportsTeamPackage team1 = null;
				TDSportsTeamPackage team2 = null;
				String date = "";
				String time = "";
				Elements trs = tbody.select("tr");
				LOGGER.debug("trs.size(): " + trs.size());

				for (int y = 1; y < trs.size(); y++) {
					// Loop through the elements and then check for the dates
					final Element element = trs.get(y);
					LOGGER.debug("Element: " + element);
	
					if (y == 1) {
						eventPackage = new TDSportsEventPackage();
						team1 = new TDSportsTeamPackage();
						final Elements tds = element.select("td");
	
						for (int i = 0; i < tds.size(); i++) {
							final Element td = tds.get(i);
	
							switch (i) {
								case 0: {
									final Elements spans = td.select("span");
									LOGGER.debug("spans: " + spans);
									int s = 0;

									for (Element span : spans) {
										switch (s++) {
											case 0: {
												date = span.html().trim();
												break;
											}
											case 1: {
												team1.setEventid(span.html().trim());
												team1.setId(Integer.parseInt(span.html().trim()));
												break;
											}
											case 2: {
												team1.setTeam(span.html().trim());
												break;
											}
											default:
												break;
										}
									}
									break;
								}
								case 1: {
									final Elements labels = td.select("label");
									if (labels != null && labels.size() > 0) {
										team1 = getSpread(labels.get(0), team1);
									}
									break;
								}
								case 2: {
									final Elements labels = td.select("label");
									if (labels != null && labels.size() > 0) {
										team1 = getTotal(labels.get(0), team1);
									}
									break;
								}
								case 3: {
									final Elements labels = td.select("label");
									if (labels != null && labels.size() > 0) {
										team1 = getMoneyLine(labels.get(0), team1);
									}
									break;
								}
								default:
									break;
							}
						}
						
						eventPackage.setId(team1.getId());
					} else if (y == 2) {
						team2 = new TDSportsTeamPackage();
						Date eventDate = null;
	
						final Elements tds = element.select("td");
	
						for (int i = 0; i < tds.size(); i++) {
							final Element td = tds.get(i);
	
							switch (i) {
								case 0: {
									final Elements spans = td.select("span");
									int s = 0;

									for (Element span : spans) {
										switch (s++) {
											case 0:{
												time = span.html().trim();
												break;
											}
											case 1: {
												team2.setEventid(span.html().trim());
												team2.setId(Integer.parseInt(span.html().trim()));
												break;
											}
											case 2: {
												team2.setTeam(span.html().trim());
												break;
											}
											default:
												break;
										}
									}
									break;
								}
								case 1: {
									final Elements labels = td.select("label");
									if (labels != null && labels.size() > 0) {
										team2 = getSpread(labels.get(0), team2);
									}
									break;
								}
								case 2: {
									final Elements labels = td.select("label");
									if (labels != null && labels.size() > 0) {
										team2 = getTotal(labels.get(0), team2);
									}
									break;
								}
								case 3: {
									final Elements labels = td.select("label");
									if (labels != null && labels.size() > 0) {
										team2 = getMoneyLine(labels.get(0), team2);
									}
									break;
								}
								default:
									break;
							}
						}
	
						final String dateOfEvent = date + " " + time;
						eventDate = setupDate(DATE_FORMAT, date, time);
	
						team1.setEventdatetime(eventDate);
						team2.setEventdatetime(eventDate);
						team1.settDate(dateOfEvent);
						team2.settDate(dateOfEvent);
						eventPackage.setDateofevent(dateOfEvent);
						eventPackage.setSiteteamone(team1);
						eventPackage.setSiteteamtwo(team2);
						eventPackage.setTeamone(team1);
						eventPackage.setTeamtwo(team2);
	
						eventPackage.setId(team1.getId());
						events.add(eventPackage);
					}
				}
			}
		}

		LOGGER.info("Exiting getGameData()");
		return events;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.tdsports.TDSportsParser#findMenu(org.jsoup.select.Elements, java.util.Map, java.lang.String[], java.lang.String[], java.lang.String, java.lang.String)
	 */
	@Override
	protected Map<String, String> findMenu(Elements divs, Map<String, String> map, String[] type, String[] sport, String foundString, String menuString) {
		LOGGER.info("Entering findMenu()");

		for (int x = 0; (divs != null && x < divs.size()); x++) {
			final Element div = divs.get(x);
			final Elements labels = div.select(foundString);

			for (int l = 0; (labels != null && l < labels.size()); l++) {
				final Element label = labels.get(l);

				for (int t = 0; t < type.length; t++) {
					if (type[t].equals(label.html().trim())) {
						final Elements inputs = div.select(menuString);

						for (int i = 0; (inputs != null && i < inputs.size()); i++) {
							final Element input = inputs.get(i);

							if (input != null) {
								map.put(input.attr("name"), input.attr("value"));
							}
						}
					}
				}
			}
		}

		LOGGER.info("Exiting findMenu()");
		return map;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.tdsports.TDSportsParser#getTeamData(org.jsoup.select.Elements, com.ticketadvantage.services.dao.sites.tdsports.TDSportsTeamPackage)
	 */
	@Override
	protected int getTeamData(Elements elements, TDSportsTeamPackage team) {
		LOGGER.info("Entering getTeamData()");
		LOGGER.debug("TDSportsTeamPackage: " + team);
		int size = 0;

		if (isMlb) {
			size = parseMlb(elements, team);
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

		if (elements != null && elements.size() == 7) {
			LOGGER.info("elements.size(): " + elements.size());
			size = 7;
			for (int x = 0;(elements != null && x < elements.size()); x++) {
				final Element td = elements.get(x);
	
				switch (x) {
					case 0: // Do nothing
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
					case 4:
						team = getMoneyLine(td, team);
						break;
					case 5:
						team = getTotal(td, team);
						break;
					case 6:
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
	 * @param td
	 * @param team
	 * @return
	 */
	protected TDSportsTeamPackage getSpread(Element td, TDSportsTeamPackage team) {
		LOGGER.info("Entering getSpread()");
		LOGGER.debug("Element: " + td);

		// First get the input field information
		final Map<String, String> hashMap = parseInputField(td.select("input"), 0);
		team.setGameSpreadInputId(hashMap.get("id"));
		team.setGameSpreadInputName(hashMap.get("name"));
		team.setGameSpreadInputValue(hashMap.get("value"));

		String spread = getHtmlFromElement(td, "span", 0, false);

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
		LOGGER.debug("Element: " + td);

		// First get the input field information
		final Map<String, String> hashMap = parseInputField(td.select("input"), 0);
		team.setGameTotalInputId(hashMap.get("id"));
		team.setGameTotalInputName(hashMap.get("name"));
		team.setGameTotalInputValue(hashMap.get("value"));

		// o40½-110; Now parse the data
		String total = getHtmlFromElement(td, "span", 0, false);

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
		LOGGER.debug("Element: " + td);
		
		// First get the input field information
		final Map<String, String> hashMap = parseInputField(td.select("input"), 0);
		team.setGameMLInputId(hashMap.get("id"));
		team.setGameMLInputName(hashMap.get("name"));
		team.addGameMLInputValue("0", hashMap.get("value"));

		// -110; Now parse the data
		String ml = getHtmlFromElement(td, "span", 0, false);

		// Parse for Money Line
		team = (TDSportsTeamPackage)parseMlData(reformatValues(ml), 0, team);

		LOGGER.info("Exiting getMoneyLine()");
		return team;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.tdsports.TDSportsParser#getTicketNumberAndDate(org.jsoup.nodes.Element, com.ticketadvantage.services.model.PendingEvent)
	 */
	@Override
	protected void getTicketNumberAndDate(Element td, PendingEvent pe) {
		LOGGER.info("Entering getTicketNumberAndDate()");

		// Ticket#: 31478859
		// <br>
		// Aug 26 08:00 PM
		// <br>
		String html = td.html();
		LOGGER.debug("html: " + html);

		if (html != null) {
			html = html.replace("<strong>", "");
			html = html.replace("</strong>", "");

			int index = html.indexOf("Ticket #:");
			if (index != -1) {
				int brindex = html.indexOf("<br>");
				if (brindex != -1) {
					pe.setTicketnum(html.substring(index + "Ticket #:".length(), brindex).trim());

					html = html.substring(brindex + 4);
					html = html.replace("<br>", "").trim();

					try {
						final Calendar now = Calendar.getInstance();
						int offset = now.get(Calendar.DST_OFFSET);
						final Date gameDate = PENDING_DATE_FORMAT.parse(html + " " + String.valueOf(now.get(Calendar.YEAR)) + " " + timeZoneLookup(super.timezone, offset));
						pe.setEventdate(html + " " + String.valueOf(now.get(Calendar.YEAR)) + " " + timeZoneLookup(super.timezone, offset));
						pe.setGamedate(gameDate);
					} catch (Throwable t) {
						LOGGER.warn(t.getMessage(), t);
					}
				}
			}
		}

		LOGGER.info("Exiting getTicketNumberAndDate()");
	}

	/**
	 * 
	 * @param html
	 * @param pe
	 * @throws BatchException
	 */
	protected void getGameInfo(String html, PendingEvent pe) throws BatchException {
		LOGGER.info("Entering getGameInfo()");
		//
		// Spread
		//
		// [309] WASHINGTON STATE +4½-110
		// <span class="date game-date">Sep 21 10:30 PM </span>
		// 

		// 
		// MoneyLine
		//
		// [970] CLE INDIANS -132
		// <br>
		// ( C SALE -L / T BAUER -R )
		// <span class="date game-date">Sep 21 07:10 PM </span>
		// 

		//
		// Total
		//
		// [374] TOTAL u60½-110
		// <br>
		// (NAVY vrs SMU)
		// <span class="date game-date">Sep 22 12:00 PM </span>
		// 

		// STRAIGHT BET
		// <br>
		// [2104] 2H NEW ENGLAND PATRIOTS -7-115 (Halftime Score: Giants 14 / Patriots 21)
		// <br>

		// First get the rotation ID
		int bindex = html.indexOf("[");
		int eindex = html.indexOf("]");
		if (bindex != -1 && eindex != -1) {
			String rotationId = html.substring(bindex + 1, eindex);
			LOGGER.debug("rotationID: " + rotationId);
			pe.setRotationid(rotationId);

			// Check what type it is
			if (rotationId.length() == 1 || rotationId.length() == 2 || rotationId.length() == 3) {
				pe.setLinetype("game");
			} else if (rotationId.startsWith("1") && rotationId.length() == 4) {
				pe.setLinetype("first");
			} else if (rotationId.startsWith("2") && rotationId.length() == 4) {
				pe.setLinetype("second");
			} else if (rotationId.startsWith("3") && rotationId.length() == 4) {
				// Determine if this is a Quarter, Period or something else
				if (html.contains("1Q")) {
					pe.setEventtype("unsupported");
					throw new BatchException(BatchErrorCodes.UNSUPPORTED_SPORT_TYPE,
							BatchErrorMessage.UNSUPPORTED_SPORT_TYPE, html);
				} else {
					pe.setLinetype("third");
				}
			} else if (rotationId.length() >= 4) {
				pe.setEventtype("unsupported");
				throw new BatchException(BatchErrorCodes.UNSUPPORTED_SPORT_TYPE,
						BatchErrorMessage.UNSUPPORTED_SPORT_TYPE, html);
			}

			html = html.substring(eindex + 2); // Skip to next thing
			LOGGER.debug("html: " + html);

			// Parse out game date now
			int gbindex = html.indexOf("<span class=\"date game-date\">");
			int geindex = html.indexOf("</span>");
			
			if (gbindex != -1 && geindex != -1) {
				// Sep 22 12:00 PM 
				final String gdate = html.substring(gbindex + "<span class=\"date game-date\">".length(), geindex);
				setGameDate(gdate, pe);
				html = html.substring(0, gbindex) + html.substring(geindex + "</span>".length());
			}

			bindex = html.indexOf("TOTAL ");

			if (bindex != -1) {
				getTotal(html, bindex, pe);
			} else {
				bindex = html.indexOf(" u");
				if (bindex != -1) {
					String tempHtml = html;
					LOGGER.debug("tempHtml: " + tempHtml);

					getSpreadTotalMoneyline(html, pe);
					int index = tempHtml.indexOf("<br>");
					if (index != -1) {
						tempHtml = tempHtml.substring(index + 4);
						tempHtml = tempHtml.replace("<br>", "").replace("(", "").replace(")", "").trim();
						LOGGER.debug("tempHtml: " + tempHtml);
						pe.setPitcher(tempHtml);
					}
				} else {
					bindex = html.indexOf(" o");
					if (bindex != -1) {
						String tempHtml = html;
						getSpreadTotalMoneyline(html, pe);
						int index = tempHtml.indexOf("<br>");
						if (index != -1) {
							tempHtml = tempHtml.substring(index + 4);
							tempHtml = tempHtml.replace("<br>", "").replace("(", "").replace(")", "").trim();
							LOGGER.debug("tempHtml: " + tempHtml);
							pe.setPitcher(tempHtml);
						}
					} else {
						LOGGER.debug("html: " + html);

						getSpreadMoneyline(html, pe);
					}
				}
			}
		}

		LOGGER.info("Exiting getGameInfo()");
	}

	/**
	 * 
	 * @param gameType
	 * @param pe
	 * @return
	 */
	private void getSport(String gameType, PendingEvent pe) {
		LOGGER.debug("gameType: " + gameType);
		gameType = gameType.replace("<br>", "");

		if ("NFL".equals(gameType)) {
			pe.setGamesport("Football");
			pe.setGametype(gameType);
		} else if ("CFB".equals(gameType)) {
			pe.setGamesport("Football");
			pe.setGametype("NCAA");				
		} else if ("NBA".equals(gameType)) {
			pe.setGamesport("Basketball");
			pe.setGametype(gameType);
		} else if ("CBB".equals(gameType)) {
			pe.setGamesport("Basketball");
			pe.setGametype("NCAA");
		} else if ("WNBA".equals(gameType)) {
			pe.setGamesport("Basketball");
			pe.setGametype(gameType);
		} else if ("NHL".equals(gameType)) {
			pe.setGamesport("Hockey");
			pe.setGametype(gameType);
		} else if ("MLB".equals(gameType)) {
			pe.setGamesport("Baseball");
			pe.setGametype(gameType);				
		} else if ("MU".equals(gameType)) {
			pe.setGamesport("Golf");
			pe.setGametype(gameType);
		}
	}
}