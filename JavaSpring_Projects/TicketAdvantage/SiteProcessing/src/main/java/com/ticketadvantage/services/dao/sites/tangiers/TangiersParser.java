/**
 * 
 */
package com.ticketadvantage.services.dao.sites.tangiers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.TimeZone;
import com.ticketadvantage.services.dao.sites.SelectOptionData;
import com.ticketadvantage.services.dao.sites.SiteEventPackage;
import com.ticketadvantage.services.dao.sites.SiteTeamPackage;
import com.ticketadvantage.services.dao.sites.tdsports.TDSportsEventPackage;
import com.ticketadvantage.services.dao.sites.tdsports.TDSportsParser;
import com.ticketadvantage.services.dao.sites.tdsports.TDSportsTeamPackage;
import com.ticketadvantage.services.email.AccessTokenFromRefreshToken;
import com.ticketadvantage.services.email.SendText;
import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.PendingEvent;

/**
 * @author jmiller
 *
 */
public class TangiersParser extends TDSportsParser {
	private static final Logger LOGGER = Logger.getLogger(TangiersParser.class);
	// Dec 15 2018 4:05:00 PM
	private final static SimpleDateFormat GAME_DATE = new SimpleDateFormat("yyyy MMM dd hh:mm a z");
	private final static SimpleDateFormat GAME_DATE_FORMATTER = new SimpleDateFormat("MMM dd yyyy H:mm:ss a z");

	/**
	 * Constructor
	 */
	public TangiersParser() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final TangiersParser tp = new TangiersParser();
			tp.timezone = "ET";

//			String xhtml = "<div class='BT-header'><i class='fa fa-caret-square-o-right'></i> BET(S) ACCEPTED</div><div class='BT-allcont-no-border'><div class='row' style='margin:0px;'><div class='col-lg-12 betslip-team-name' style='border:none;'>STRAIGHT BET<br><strong>Ticket Number : 40884930</strong><br />[302] KANSAS CITY CHIEFS u53-105<br>Risk: 105.00 Win: 100.00<hr /><button type='button' class='btn btn-sm btn-primary btn-new-bet' id='pwd_button' onClick='remove_all()'>Start New Bet</button><script>refreshBalance()</script></div></div></div>";
//			String ticketnumber = tp.parseTicketNumber(xhtml);
//			LOGGER.error("ticketnumber: " + ticketnumber);
//			Set<PendingEvent> pes = tp.parsePendingBets("<!DOCTYPE html><html lang=\"en\"><head id=\"ctl00_Head\"><meta charset=\"utf-8\" /><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" /><meta name=\"viewport\" content=\"width=device-width, height=device-height, initial-scale=1.0, maximum-scale=1.0, user-scalable=no\" /><title>	Sportbook</title><link href=\"/app/assets/css/root.css?v=165\" rel=\"stylesheet\" /><script type=\"text/javascript\" src=\"/app/assets/js/jquery.min.js\"></script><link href=\"../App_Themes/Classic/defaultRace.css\" type=\"text/css\" rel=\"stylesheet\" /><link href=\"../App_Themes/Classic/images/calendar/Calendar.css\" type=\"text/css\" rel=\"stylesheet\" /></head><body class=\"color6-bg\"><div class=\"loading\"><img src=\"/app/assets/img/loading.gif\" alt=\"loading-img\"></div><span id=\"ctl00_WagerLnkTop\"><div id=\"top\" class=\"clearfix\"><div class=\"applogo\"><a href=\"\" class=\"logo\"></a></div><div class=\"mobilecs hidden-lg\"></div><a href=\"#\" class=\"sidebar-open-button\"><i class=\"fa fa-bars\"></i></a><ul class=\"topmenu\"><li><a id=\"ctl00_WagerLnkTop_lnkSports\" href=\"Sports.aspx\">Sports</a></li><li><a id=\"ctl00_WagerLnkTop_lnkCasino\" href=\"Bridge.aspx?to=casino\">Casino</a></li><li><a id=\"ctl00_WagerLnkTop_lnkRacing\" href=\"Bridge.aspx?to=racing\">Racing</a></li><li class=\"dropdown\"><a href=\"#\" data-toggle=\"dropdown\" class=\"dropdown-toggle\"><span id=\"ctl00_WagerLnkTop_LocalizedLabelAccTop\">ACCOUNT</span><span class=\"caret\"></span></a><ul class=\"dropdown-menu\"><li><a id=\"ctl00_WagerLnkTop_lnkOpenBets\" href=\"OpenBets.aspx\">Open Bets</a></li><li><a id=\"ctl00_WagerLnkTop_lnkHistory\" href=\"History.aspx\">History</a></li><li><a id=\"ctl00_WagerLnkTop_lnkFigures\" href=\"Figures.aspx\">Figures</a></li><li><a id=\"ctl00_WagerLnkTop_lnkFillOpen\" href=\"FillChoose.aspx\">Fill Open</a></li><li><a id=\"ctl00_WagerLnkTop_lnkRules\" href=\"Rules.aspx\">Rules</a></li></ul></li></ul><a href=\"../LogOut.aspx\" class=\"sidepanel-open-button hidden-xs hidden-sm hidden-md\"><i class=\"fa fa-power-off\"></i></a><section id=\"mobile-top-menu\" class=\"hidden-lg\"><a class=\"main-header-open-betslip\" onclick=\"openBS()\"><span class=\"main-header-open-betslip\"><i class=\"badge badge-counter\" id=\"bs_cnt\">0</i>Bet Slip</span></a><div class=\"text-right main-header-account\"><a class=\"open-top-myAccount\" data-direction=\"bottom\"><span class=\"account-number\"></span><span class=\"amount-available amountAvailable\"></span></a></div></section></div></span><div class=\"account-info hidden-xs hidden-sm hidden-md\"><div class=\"hidden-xs hidden-sm hidden-md sidebar-plan sideBal row\"><div class=\"col-lg-4 text-right\"><span id=\"ctl00_WagerLnkSide_lblAccount\">NY118</span><br><span id=\"ctl00_WagerLnkSide_lblBalTop\">BAL</span>: <span class=\"account-info-money\">$</span><a href=\"#\"><span id=\"ctl00_WagerLnkSide_lblCurrentBalance\">-27,160 </span></a></div><div class=\"col-lg-2\"><i class=\"fa fa-2x fa-user\" style=\"color:#fbbd23;font-size: 30px;\"></i></div><div class=\"col-lg-6 text-left\"><span id=\"ctl00_WagerLnkSide_lblRiskTop\">Risk</span>: <span class=\"account-info-money\">$</span><a href=\"#\"><span id=\"ctl00_WagerLnkSide_lblAmountAtRisk\">13,200 </span></a><br><span id=\"ctl00_WagerLnkSide_lblAvailTop\">AVAIL</span>: <span class=\"account-info-money\">$</span><a href=\"#\"><span id=\"ctl00_WagerLnkSide_lblRealAvailBalance\">29,640 </span></a></div></div></div><div class=\"row searchbar\"><div class=\"col-xs-12\"><div class=\"form-group search-sport\"><input class=\"form-control search-sport-input\" name=\"searchTextInput\" id=\"searchTextInput\" type=\"text\" placeholder=\"Team or Rotation\" maxlength=\"60\"><button type=\"submit\" class=\"search-sport-icon\" onclick='searchValue();'><i class=\"fa fa-search\"></i></button></div></div></div><div class=\"leaguesmenu\" id=\"leaguesmenu\" style=\"display:none;\"></div><div class=\"content\"><div id=\"menuMobileModal\" class=\"modal fade\" role=\"dialog\"><div class=\"modal-dialog\"><div class=\"modal-content\"><div class=\"modal-body\"><div id=\"accountmenu\"><div class=\"row modal-user-information\"><div class=\"account-information-user\"><div class=\"account-information-user-header clearfix\"><div class=\"col-xs-12 not-padding clearfix\"><p class=\"account-information-user-code\"><span id=\"mobile-playerAccount\"><span id=\"ctl00_SlipAccFigures_lblAccount\">NY118</span></span></p></div></div><div class=\"account-information-user-information clearfix\"><div class=\"col-xs-6 text-right\"><span class=\"account-information-user-text-account\">CURRENT BALANCE:</span></div><div class=\"col-xs-6 text-left\"><span class=\"account-information-user-text-amount freePlayAvailable\">$<span id=\"mobile-currentBalance\"><span id=\"ctl00_SlipAccFigures_lblCurrentBalance\">-27,160 </span></span></span></div><div class=\"col-xs-6 text-right\"><span class=\"account-information-user-text-account\">AVAILABLE BALANCE:</span></div><div class=\"col-xs-6 text-left\"><span class=\"account-information-user-text-amount amountAvailable\">$<span id=\"mobile-availableBalance\"><span id=\"ctl00_SlipAccFigures_lblRealAvailBalance\">29,640 </span></span></span></div><div class=\"col-xs-6 text-right\"><span class=\"account-information-user-text-account\">PENDING WAGERS:</span></div><div class=\"col-xs-6 text-left\"><span class=\"account-information-user-text-amount pendingWager\">$<span id=\"mobile-riskBalance\"><span id=\"ctl00_SlipAccFigures_lblAmountAtRisk\">13,200 </span></span></span></div></div></div></div><div class=\"row menu-modal-dialog-wrapper-widget-02\"><div class=\"col-xs-12 text-center\"><span class=\"account-information-user-text-account\"></span></div></div><div class=\"row menu-modal-dialog-wrapper-widget-02\"><div class=\"col-xs-12\"><div class=\"tittle-sports tittle-sports-2\"><span>Account Information</span></div><div class=\"list-group\"><div class=\"panel-group\" role=\"tablist\" id=\"accordion\"><div class=\"my-account-list\"><a href=\"/app/wager/Rules.aspx\"><div class=\"collapsed main-container-panel-heading\" role=\"tab\"><h4 class=\"panel-tittle main-container-panel-tittle\">houserules</h4><i class=\"glyphicon glyphicon-menu-right main-container-right-arrow pull-right\"></i></div></a></div><div class=\"my-account-list\"><a href=\"/app/wager/FillChoose.aspx\"><div class=\"collapsed main-container-panel-heading\" role=\"tab\"><h4 class=\"panel-tittle main-container-panel-tittle\">Fill Open</h4><i class=\"glyphicon glyphicon-menu-right main-container-right-arrow pull-right\"></i></div></a></div><div class=\"my-account-list logout-js\"><a href=\"/app/Logout.aspx\" class=\"logout-js\"><div class=\"collapsed main-container-panel-heading\" role=\"tab\"><h4 class=\"panel-tittle main-container-panel-tittle\">Logout</h4><i class=\"glyphicon glyphicon-menu-right main-container-right-arrow pull-right\"></i></div></a></div></div></div></div></div></div></div></div></div></div><div id=\"scoresMobileModal\" class=\"modal fade\" role=\"dialog\"><div class=\"modal-dialog\"><div class=\"modal-content\"><div class=\"modal-header\"><button type=\"button\" class=\"close\" data-dismiss=\"modal\">&times;</button><h4 class=\"modal-title\">Scores</h4></div><div class=\"modal-body\" id=\"scoreMobileBody\"></div><div class=\"modal-footer\"><button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">Close</button></div></div></div></div><section class=\"menu-slider hidden-lg\" style=\"display:none;\"><div class=\"menu-slider-carousel\"><div class=\"menu-slider-box\" onclick=\"openScoresModal();return false;\"><a href=\"#\" class=\"sprite-scores sprite02 request_league league_item\"><span class=\"menu-slider-text\">SCORES</span></a></div><div class=\"menu-slider-box\" data-league-icon=\"1\"><a href=\"#\" class=\"sprite-nfl sprite02 request_league league_item\"><span class=\"menu-slider-text\">NFL</span></a></div><div class=\"menu-slider-box\" data-league-icon=\"2\"><a href=\"#\" class=\"sprite-ncaa sprite02 request_league league_item\"><span class=\"menu-slider-text\">NCAAF</span></a></div><div class=\"menu-slider-box\" data-league-icon=\"-1\"><a href=\"#\" class=\"sprite-horses sprite02 request_league league_item\"><span class=\"menu-slider-text\">Horses</span></a></div><div class=\"menu-slider-box\" onclick=\"openSearchModal();return false;\"><a class=\"sprite-search sprite02\"><span class=\"menu-slider-text\">SEARCH</span></a></div><div class=\"menu-slider-box\" data-league-icon=\"-2\"><a href=\"#\" class=\"sprite-bjk sprite02 request_league league_item\"><span class=\"menu-slider-text\">Blackjack</span></a></div><div class=\"menu-slider-box\" data-league-icon=\"3\"><a href=\"#\" class=\"sprite-nba sprite02 request_league league_item\"><span class=\"menu-slider-text\">NBA</span></a></div><div class=\"menu-slider-box\" data-league-icon=\"5\"><a href=\"#\" class=\"sprite-mlb sprite02 request_league league_item\"><span class=\"menu-slider-text\">MLB</span></a></div><div class=\"menu-slider-box\" data-league-icon=\"7\"><a href=\"#\" class=\"sprite-nhl sprite02 request_league league_item\"><span class=\"menu-slider-text\">NHL</span></a></div><div class=\"menu-slider-box\" data-league-icon=\"142\"><a href=\"#\" class=\"sprite-russia2018 sprite02 request_league league_item\"><span class=\"menu-slider-text\">WORLD CUP</span></a></div><div class=\"menu-slider-box\" data-league-icon=\"4\"><a href=\"#\" class=\"sprite-ncaa sprite02 request_league league_item\"><span class=\"menu-slider-text\">NCAAB</span></a></div><div class=\"menu-slider-box\" style=\"display:none;\" id=\"menu-slide-undoIcon\" onclick=\"hideSearchModal();return false;\"><a class=\"sprite-undo sprite02\"><span class=\"menu-slider-text\">BACK</span></a></div></div></section><form name=\"aspnetForm\" method=\"post\" action=\"OpenBets.aspx\" id=\"aspnetForm\"><div><input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"/wEPDwULLTEyMjQyNjg4NzIPZBYCZg9kFgQCBQ9kFggCAQ8PFgIeBFRleHQFBU5ZMTE4ZGQCBQ8PFgIfAAUILTI3LDE2MCBkZAIJDw8WAh8ABQcxMywyMDAgZGQCDQ8PFgIfAAUHMjksNjQwIGRkAgcPZBYIAgEPDxYCHwAFBU5ZMTE4ZGQCAw8PFgIfAAUILTI3LDE2MCBkZAIFDw8WAh8ABQcyOSw2NDAgZGQCBw8PFgIfAAUHMTMsMjAwIGRkZLxOf0dq3z6OFL2/b6dGYf9rodsC\" /></div><div><input type=\"hidden\" name=\"__VIEWSTATEGENERATOR\" id=\"__VIEWSTATEGENERATOR\" value=\"4FF1AA84\" /></div><div class=\"report-container\"><div class=\"page-header hidden-xs\"><ol class=\"breadcrumb\"><li>OPEN BETS</li></ol></div><div class=\"container-default\"><div class=\"table-responsive\"><TABLE CLASS=\"table\"><div class=\"open-bet-box\"><div class=\"open-bet-top-text\"><div class=\"row\"><div class=\"col-xs-6 report-important-text\"><span>Ticket #41595509</span></div><div class=\"col-xs-6 text-right\"><span class=\"placed-date\">Accepted:Jun 06 10:42 AM </span></div></div></div><div class=\"\"><div class=\"row\"><div class=\"col-xs-12\"><div class=\"text-center\"><span class=\"openbet-text\">STRAIGHT BET</span></div></div></div></div><div class=\"open-bet-top-text\"><div class=\"row open-bets-name\"><div class=\"col-xs-3\"><div class=\"text-center\"><span class=\"report-important-text\">MLB<BR /><BR /></span></div></div></div><div class=\"row open-bet-desc\"><div class=\"col-xs-3\"><div class=\"text-center\"><span>Jun 06 07:10 PM </span></div></div><div class=\"col-xs-6\"><div class=\"text-center\"><span style=\"color:#262626;\">[922] CLE INDIANS ML+101<BR>( J. BERRIOS -R / T. BAUER -R )</span></div></div></div></div><div class=\"open-risk-win report-important-text\" id=\"\"><div class=\"row\"><div class=\"col-xs-6\"><div><span>Risk Amount: $4000</span></div></div><div class=\"col-xs-6\"><div class=\"text-right\"><span>To Win Amount: $4040</span></div></div></div></div></div><div class=\"open-bet-box\"><div class=\"open-bet-top-text\"><div class=\"row\"><div class=\"col-xs-6 report-important-text\"><span>Ticket #41595510</span></div><div class=\"col-xs-6 text-right\"><span class=\"placed-date\">Accepted:Jun 06 10:42 AM </span></div></div></div><div class=\"\"><div class=\"row\"><div class=\"col-xs-12\"><div class=\"text-center\"><span class=\"openbet-text\">STRAIGHT BET</span></div></div></div></div><div class=\"open-bet-top-text\"><div class=\"row open-bets-name\"><div class=\"col-xs-3\"><div class=\"text-center\"><span class=\"report-important-text\">MLB<BR /><BR /></span></div></div></div><div class=\"row open-bet-desc\"><div class=\"col-xs-3\"><div class=\"text-center\"><span>Jun 06 01:15 PM </span></div></div><div class=\"col-xs-6\"><div class=\"text-center\"><span style=\"color:#262626;\">[906] STL CARDINALS ML-130<BR>( A. DESCLAFANI -R / DA. HUDSON -R )</span></div></div></div></div><div class=\"open-risk-win report-important-text\" id=\"\"><div class=\"row\"><div class=\"col-xs-6\"><div><span>Risk Amount: $5200</span></div></div><div class=\"col-xs-6\"><div class=\"text-right\"><span>To Win Amount: $4000</span></div></div></div></div></div><div class=\"open-bet-box\"><div class=\"open-bet-top-text\"><div class=\"row\"><div class=\"col-xs-6 report-important-text\"><span>Ticket #41595511</span></div><div class=\"col-xs-6 text-right\"><span class=\"placed-date\">Accepted:Jun 06 10:42 AM </span></div></div></div><div class=\"\"><div class=\"row\"><div class=\"col-xs-12\"><div class=\"text-center\"><span class=\"openbet-text\">STRAIGHT BET</span></div></div></div></div><div class=\"open-bet-top-text\"><div class=\"row open-bets-name\"><div class=\"col-xs-3\"><div class=\"text-center\"><span class=\"report-important-text\">MLB<BR /><BR /></span></div></div></div><div class=\"row open-bet-desc\"><div class=\"col-xs-3\"><div class=\"text-center\"><span>Jun 06 01:15 PM </span></div></div><div class=\"col-xs-6\"><div class=\"text-center\"><span style=\"color:#262626;\">[906] STL CARDINALS -1Â½+160<BR>( A. DESCLAFANI -R / DA. HUDSON -R )</span></div></div></div></div><div class=\"open-risk-win report-important-text\" id=\"\"><div class=\"row\"><div class=\"col-xs-6\"><div><span>Risk Amount: $4000</span></div></div><div class=\"col-xs-6\"><div class=\"text-right\"><span>To Win Amount: $6400</span></div></div></div></div></div><TR CLASS=\"GameHeader\"><TD colspan=\"4\" align=\"right\">Open Bets: 3</TD><TD colspan=\"2\" align=\"right\">Total Amount: $13200 / $14440</TD></TR></TABLE></div><button type=\"button\" onclick=\"topFunction()\" id=\"toTopBtn\" title=\"Back to Top\">Back to Top</button></div><div class=\"container-default\"></div></div><script>        setTimeout(function(){ hideLinesModules(); }, 500);    </script></form><footer class=\"main-footer hidden-lg\"><div class=\"main-foot\"><a href=\"#\" id=\"sports-icon\" class=\"footer-item sprite-football\" data-direction=\"bottom\"><span class=\"foot-text\">Sports</span></a><a href=\"/app/wager/OpenBets.aspx\" id=\"pending-icon\" class=\"footer-item icon-footer glyphicon glyphicon-list-alt\" data-direction=\"bottom\"><span class=\"foot-text gryph-text-align\">Pending</span></a><a href=\"/app/wager/History.aspx\" id=\"history-icon\" class=\"footer-item icon-footer glyphicon glyphicon-calendar\" data-direction=\"bottom\"><span class=\"foot-text gryph-text-align\">History</span></a><a href=\"#\" onclick=\"openMenuMobile();\" class=\"footer-item icon-footer glyphicon glyphicon-menu-hamburger\" data-direction=\"bottom\"><span class=\"foot-text gryph-text-align-menu\">Menu</span></a></div></footer></div><script src=\"/app/assets/js/bootstrap/bootstrap.min.js\"></script><script type=\"text/javascript\" src=\"/app/assets/js/bootstrap-select/bootstrap-select.js\"></script><script type=\"text/javascript\" src=\"/app/assets/js/plugins.js\"></script><script type=\"text/javascript\" src=\"/app/wager/betslip/web.js?v=149\"></script><script type=\"text/javascript\" src=\"/app/wager/betslip/md5.js\"></script><script type=\"text/javascript\">     var url = location.host;     var domain = url.split(\".\").slice(-2)[0];     $('.logo').html('<img alt=\"Logo\" width=\"100\" src=\"/app/assets/img/logos/' + domain + '.png\">');     //User Desktop Version     function getCookie(cname) {        var name = cname + \"=\";        var decodedCookie = decodeURIComponent(document.cookie);        var ca = decodedCookie.split(';');        for(var i = 0; i <ca.length; i++) {            var c = ca[i];            while (c.charAt(0) == ' ') {                c = c.substring(1);            }            if (c.indexOf(name) == 0) {                return c.substring(name.length, c.length);            }        }        return \"\";     }     var desktopversion = getCookie(\"desktopversion\");        if (desktopversion != \"\") {            if(desktopversion == \"1\")             {                 var myViewport = document.querySelector(\"meta[name=viewport]\");            if ($(window).width() < 1024){                myViewport.setAttribute(\"content\", \"width=1800; user-scalable = yes\");                } else {                myViewport.setAttribute(\"content\", \"width=device-width; initial-scale = 1.0\");            }            }        }       </script></body></html>", "accountName", "accountId");
//			Set<PendingEvent> pes = tp.parsePendingBets("<!DOCTYPE html><html lang=\"en\"><head id=\"ctl00_Head\"><meta charset=\"utf-8\" /><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" /><meta name=\"viewport\" content=\"width=device-width, height=device-height, initial-scale=1.0, maximum-scale=1.0, user-scalable=no\" /><title>	Sportbook</title><link href=\"/app/assets/css/root.css?v=165\" rel=\"stylesheet\" /><script type=\"text/javascript\" src=\"/app/assets/js/jquery.min.js\"></script><link href=\"../App_Themes/Classic/defaultRace.css\" type=\"text/css\" rel=\"stylesheet\" /><link href=\"../App_Themes/Classic/images/calendar/Calendar.css\" type=\"text/css\" rel=\"stylesheet\" /></head><body class=\"color6-bg\"><div class=\"loading\"><img src=\"/app/assets/img/loading.gif\" alt=\"loading-img\"></div><span id=\"ctl00_WagerLnkTop\"><div id=\"top\" class=\"clearfix\"><div class=\"applogo\"><a href=\"\" class=\"logo\"></a></div><div class=\"mobilecs hidden-lg\"></div><a href=\"#\" class=\"sidebar-open-button\"><i class=\"fa fa-bars\"></i></a><ul class=\"topmenu\"><li><a id=\"ctl00_WagerLnkTop_lnkSports\" href=\"Sports.aspx\">Sports</a></li><li><a id=\"ctl00_WagerLnkTop_lnkCasino\" href=\"Bridge.aspx?to=casino\">Casino</a></li><li><a id=\"ctl00_WagerLnkTop_lnkRacing\" href=\"Bridge.aspx?to=racing\">Racing</a></li><li class=\"dropdown\"><a href=\"#\" data-toggle=\"dropdown\" class=\"dropdown-toggle\"><span id=\"ctl00_WagerLnkTop_LocalizedLabelAccTop\">ACCOUNT</span><span class=\"caret\"></span></a><ul class=\"dropdown-menu\"><li><a id=\"ctl00_WagerLnkTop_lnkOpenBets\" href=\"OpenBets.aspx\">Open Bets</a></li><li><a id=\"ctl00_WagerLnkTop_lnkHistory\" href=\"History.aspx\">History</a></li><li><a id=\"ctl00_WagerLnkTop_lnkFigures\" href=\"Figures.aspx\">Figures</a></li><li><a id=\"ctl00_WagerLnkTop_lnkFillOpen\" href=\"FillChoose.aspx\">Fill Open</a></li><li><a id=\"ctl00_WagerLnkTop_lnkRules\" href=\"Rules.aspx\">Rules</a></li></ul></li></ul><a href=\"../LogOut.aspx\" class=\"sidepanel-open-button hidden-xs hidden-sm hidden-md\"><i class=\"fa fa-power-off\"></i></a><section id=\"mobile-top-menu\" class=\"hidden-lg\"><a class=\"main-header-open-betslip\" onclick=\"openBS()\"><span class=\"main-header-open-betslip\"><i class=\"badge badge-counter\" id=\"bs_cnt\">0</i>Bet Slip</span></a><div class=\"text-right main-header-account\"><a class=\"open-top-myAccount\" data-direction=\"bottom\"><span class=\"account-number\"></span><span class=\"amount-available amountAvailable\"></span></a></div></section></div></span><div class=\"account-info hidden-xs hidden-sm hidden-md\"><div class=\"hidden-xs hidden-sm hidden-md sidebar-plan sideBal row\"><div class=\"col-lg-4 text-right\"><span id=\"ctl00_WagerLnkSide_lblAccount\">NY118</span><br><span id=\"ctl00_WagerLnkSide_lblBalTop\">BAL</span>: <span class=\"account-info-money\">$</span><a href=\"#\"><span id=\"ctl00_WagerLnkSide_lblCurrentBalance\">7,800 </span></a></div><div class=\"col-lg-2\"><i class=\"fa fa-2x fa-user\" style=\"color:#fbbd23;font-size: 30px;\"></i></div><div class=\"col-lg-6 text-left\"><span id=\"ctl00_WagerLnkSide_lblRiskTop\">Risk</span>: <span class=\"account-info-money\">$</span><a href=\"#\"><span id=\"ctl00_WagerLnkSide_lblAmountAtRisk\">20,680 </span></a><br><span id=\"ctl00_WagerLnkSide_lblAvailTop\">AVAIL</span>: <span class=\"account-info-money\">$</span><a href=\"#\"><span id=\"ctl00_WagerLnkSide_lblRealAvailBalance\">57,120 </span></a></div></div></div><div class=\"row searchbar\"><div class=\"col-xs-12\"><div class=\"form-group search-sport\"><input class=\"form-control search-sport-input\" name=\"searchTextInput\" id=\"searchTextInput\" type=\"text\" placeholder=\"Team or Rotation\" maxlength=\"60\"><button type=\"submit\" class=\"search-sport-icon\" onclick='searchValue();'><i class=\"fa fa-search\"></i></button></div></div></div><div class=\"leaguesmenu\" id=\"leaguesmenu\" style=\"display:none;\"></div><div class=\"content\"><div id=\"menuMobileModal\" class=\"modal fade\" role=\"dialog\"><div class=\"modal-dialog\"><div class=\"modal-content\"><div class=\"modal-body\"><div id=\"accountmenu\"><div class=\"row modal-user-information\"><div class=\"account-information-user\"><div class=\"account-information-user-header clearfix\"><div class=\"col-xs-12 not-padding clearfix\"><p class=\"account-information-user-code\"><span id=\"mobile-playerAccount\"><span id=\"ctl00_SlipAccFigures_lblAccount\">NY118</span></span></p></div></div><div class=\"account-information-user-information clearfix\"><div class=\"col-xs-6 text-right\"><span class=\"account-information-user-text-account\">CURRENT BALANCE:</span></div><div class=\"col-xs-6 text-left\"><span class=\"account-information-user-text-amount freePlayAvailable\">$<span id=\"mobile-currentBalance\"><span id=\"ctl00_SlipAccFigures_lblCurrentBalance\">7,800 </span></span></span></div><div class=\"col-xs-6 text-right\"><span class=\"account-information-user-text-account\">AVAILABLE BALANCE:</span></div><div class=\"col-xs-6 text-left\"><span class=\"account-information-user-text-amount amountAvailable\">$<span id=\"mobile-availableBalance\"><span id=\"ctl00_SlipAccFigures_lblRealAvailBalance\">57,120 </span></span></span></div><div class=\"col-xs-6 text-right\"><span class=\"account-information-user-text-account\">PENDING WAGERS:</span></div><div class=\"col-xs-6 text-left\"><span class=\"account-information-user-text-amount pendingWager\">$<span id=\"mobile-riskBalance\"><span id=\"ctl00_SlipAccFigures_lblAmountAtRisk\">20,680 </span></span></span></div></div></div></div><div class=\"row menu-modal-dialog-wrapper-widget-02\"><div class=\"col-xs-12 text-center\"><span class=\"account-information-user-text-account\"></span></div></div><div class=\"row menu-modal-dialog-wrapper-widget-02\"><div class=\"col-xs-12\"><div class=\"tittle-sports tittle-sports-2\"><span>Account Information</span></div><div class=\"list-group\"><div class=\"panel-group\" role=\"tablist\" id=\"accordion\"><div class=\"my-account-list\"><a href=\"/app/wager/Rules.aspx\"><div class=\"collapsed main-container-panel-heading\" role=\"tab\"><h4 class=\"panel-tittle main-container-panel-tittle\">houserules</h4><i class=\"glyphicon glyphicon-menu-right main-container-right-arrow pull-right\"></i></div></a></div><div class=\"my-account-list\"><a href=\"/app/wager/FillChoose.aspx\"><div class=\"collapsed main-container-panel-heading\" role=\"tab\"><h4 class=\"panel-tittle main-container-panel-tittle\">Fill Open</h4><i class=\"glyphicon glyphicon-menu-right main-container-right-arrow pull-right\"></i></div></a></div><div class=\"my-account-list logout-js\"><a href=\"/app/Logout.aspx\" class=\"logout-js\"><div class=\"collapsed main-container-panel-heading\" role=\"tab\"><h4 class=\"panel-tittle main-container-panel-tittle\">Logout</h4><i class=\"glyphicon glyphicon-menu-right main-container-right-arrow pull-right\"></i></div></a></div></div></div></div></div></div></div></div></div></div><div id=\"scoresMobileModal\" class=\"modal fade\" role=\"dialog\"><div class=\"modal-dialog\"><div class=\"modal-content\"><div class=\"modal-header\"><button type=\"button\" class=\"close\" data-dismiss=\"modal\">&times;</button><h4 class=\"modal-title\">Scores</h4></div><div class=\"modal-body\" id=\"scoreMobileBody\"></div><div class=\"modal-footer\"><button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">Close</button></div></div></div></div><section class=\"menu-slider hidden-lg\" style=\"display:none;\"><div class=\"menu-slider-carousel\"><div class=\"menu-slider-box\" onclick=\"openScoresModal();return false;\"><a href=\"#\" class=\"sprite-scores sprite02 request_league league_item\"><span class=\"menu-slider-text\">SCORES</span></a></div><div class=\"menu-slider-box\" data-league-icon=\"1\"><a href=\"#\" class=\"sprite-nfl sprite02 request_league league_item\"><span class=\"menu-slider-text\">NFL</span></a></div><div class=\"menu-slider-box\" data-league-icon=\"2\"><a href=\"#\" class=\"sprite-ncaa sprite02 request_league league_item\"><span class=\"menu-slider-text\">NCAAF</span></a></div><div class=\"menu-slider-box\" data-league-icon=\"-1\"><a href=\"#\" class=\"sprite-horses sprite02 request_league league_item\"><span class=\"menu-slider-text\">Horses</span></a></div><div class=\"menu-slider-box\" onclick=\"openSearchModal();return false;\"><a class=\"sprite-search sprite02\"><span class=\"menu-slider-text\">SEARCH</span></a></div><div class=\"menu-slider-box\" data-league-icon=\"-2\"><a href=\"#\" class=\"sprite-bjk sprite02 request_league league_item\"><span class=\"menu-slider-text\">Blackjack</span></a></div><div class=\"menu-slider-box\" data-league-icon=\"3\"><a href=\"#\" class=\"sprite-nba sprite02 request_league league_item\"><span class=\"menu-slider-text\">NBA</span></a></div><div class=\"menu-slider-box\" data-league-icon=\"5\"><a href=\"#\" class=\"sprite-mlb sprite02 request_league league_item\"><span class=\"menu-slider-text\">MLB</span></a></div><div class=\"menu-slider-box\" data-league-icon=\"7\"><a href=\"#\" class=\"sprite-nhl sprite02 request_league league_item\"><span class=\"menu-slider-text\">NHL</span></a></div><div class=\"menu-slider-box\" data-league-icon=\"142\"><a href=\"#\" class=\"sprite-russia2018 sprite02 request_league league_item\"><span class=\"menu-slider-text\">WORLD CUP</span></a></div><div class=\"menu-slider-box\" data-league-icon=\"4\"><a href=\"#\" class=\"sprite-ncaa sprite02 request_league league_item\"><span class=\"menu-slider-text\">NCAAB</span></a></div><div class=\"menu-slider-box\" style=\"display:none;\" id=\"menu-slide-undoIcon\" onclick=\"hideSearchModal();return false;\"><a class=\"sprite-undo sprite02\"><span class=\"menu-slider-text\">BACK</span></a></div></div></section><form name=\"aspnetForm\" method=\"post\" action=\"OpenBets.aspx\" id=\"aspnetForm\"><div><input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"/wEPDwULLTEyMjQyNjg4NzIPZBYCZg9kFgQCBQ9kFggCAQ8PFgIeBFRleHQFBU5ZMTE4ZGQCBQ8PFgIfAAUGNyw4MDAgZGQCCQ8PFgIfAAUHMjAsNjgwIGRkAg0PDxYCHwAFBzU3LDEyMCBkZAIHD2QWCAIBDw8WAh8ABQVOWTExOGRkAgMPDxYCHwAFBjcsODAwIGRkAgUPDxYCHwAFBzU3LDEyMCBkZAIHDw8WAh8ABQcyMCw2ODAgZGRkwaun/rxRjUhapL2Y6FoOStYMFVI=\" /></div><div><input type=\"hidden\" name=\"__VIEWSTATEGENERATOR\" id=\"__VIEWSTATEGENERATOR\" value=\"4FF1AA84\" /></div><div class=\"report-container\"><div class=\"page-header hidden-xs\"><ol class=\"breadcrumb\"><li>OPEN BETS</li></ol></div><div class=\"container-default\"><div class=\"table-responsive\"><TABLE CLASS=\"table\"><div class=\"open-bet-box\"><div class=\"open-bet-top-text\"><div class=\"row\"><div class=\"col-xs-6 report-important-text\"><span>Ticket #41621557</span></div><div class=\"col-xs-6 text-right\"><span class=\"placed-date\">Accepted:Jun 14 11:47 AM </span></div></div></div><div class=\"\"><div class=\"row\"><div class=\"col-xs-12\"><div class=\"text-center\"><span class=\"openbet-text\">STRAIGHT BET</span></div></div></div></div><div class=\"open-bet-top-text\"><div class=\"row open-bets-name\"><div class=\"col-xs-3\"><div class=\"text-center\"><span class=\"report-important-text\">MLB<BR /><BR /></span></div></div></div><div class=\"row open-bet-desc\"><div class=\"col-xs-3\"><div class=\"text-center\"><span>Jun 14 07:20 PM </span></div></div><div class=\"col-xs-6\"><div class=\"text-center\"><span style=\"color:#262626;\">[907] PHI PHILLIES ML+130<BR>( N. PIVETTA -R / M. FRIED -L )</span></div></div></div></div><div class=\"open-risk-win report-important-text\" id=\"\"><div class=\"row\"><div class=\"col-xs-6\"><div><span>Risk Amount: $4000</span></div></div><div class=\"col-xs-6\"><div class=\"text-right\"><span>To Win Amount: $5200</span></div></div></div></div></div><div class=\"open-bet-box\"><div class=\"open-bet-top-text\"><div class=\"row\"><div class=\"col-xs-6 report-important-text\"><span>Ticket #41621558</span></div><div class=\"col-xs-6 text-right\"><span class=\"placed-date\">Accepted:Jun 14 11:47 AM </span></div></div></div><div class=\"\"><div class=\"row\"><div class=\"col-xs-12\"><div class=\"text-center\"><span class=\"openbet-text\">STRAIGHT BET</span></div></div></div></div><div class=\"open-bet-top-text\"><div class=\"row open-bets-name\"><div class=\"col-xs-3\"><div class=\"text-center\"><span class=\"report-important-text\">MLB<BR /><BR /></span></div></div></div><div class=\"row open-bet-desc\"><div class=\"col-xs-3\"><div class=\"text-center\"><span>Jun 14 07:20 PM </span></div></div><div class=\"col-xs-6\"><div class=\"text-center\"><span style=\"color:#262626;\">[907] PHI PHILLIES +1Â½-155<BR>( N. PIVETTA -R / M. FRIED -L )</span></div></div></div></div><div class=\"open-risk-win report-important-text\" id=\"\"><div class=\"row\"><div class=\"col-xs-6\"><div><span>Risk Amount: $6200</span></div></div><div class=\"col-xs-6\"><div class=\"text-right\"><span>To Win Amount: $4000</span></div></div></div></div></div><div class=\"open-bet-box\"><div class=\"open-bet-top-text\"><div class=\"row\"><div class=\"col-xs-6 report-important-text\"><span>Ticket #41621568</span></div><div class=\"col-xs-6 text-right\"><span class=\"placed-date\">Accepted:Jun 14 11:48 AM </span></div></div></div><div class=\"\"><div class=\"row\"><div class=\"col-xs-12\"><div class=\"text-center\"><span class=\"openbet-text\">STRAIGHT BET</span></div></div></div></div><div class=\"open-bet-top-text\"><div class=\"row open-bets-name\"><div class=\"col-xs-3\"><div class=\"text-center\"><span class=\"report-important-text\">MLB<BR /><BR /></span></div></div></div><div class=\"row open-bet-desc\"><div class=\"col-xs-3\"><div class=\"text-center\"><span>Jun 14 07:10 PM </span></div></div><div class=\"col-xs-6\"><div class=\"text-center\"><span style=\"color:#262626;\">[918] TB RAYS ML-162<BR>( A. HEANEY -L / B. SNELL -L )</span></div></div></div></div><div class=\"open-risk-win report-important-text\" id=\"\"><div class=\"row\"><div class=\"col-xs-6\"><div><span>Risk Amount: $6480</span></div></div><div class=\"col-xs-6\"><div class=\"text-right\"><span>To Win Amount: $4000</span></div></div></div></div></div><div class=\"open-bet-box\"><div class=\"open-bet-top-text\"><div class=\"row\"><div class=\"col-xs-6 report-important-text\"><span>Ticket #41621569</span></div><div class=\"col-xs-6 text-right\"><span class=\"placed-date\">Accepted:Jun 14 11:48 AM </span></div></div></div><div class=\"\"><div class=\"row\"><div class=\"col-xs-12\"><div class=\"text-center\"><span class=\"openbet-text\">STRAIGHT BET</span></div></div></div></div><div class=\"open-bet-top-text\"><div class=\"row open-bets-name\"><div class=\"col-xs-3\"><div class=\"text-center\"><span class=\"report-important-text\">MLB<BR /><BR /></span></div></div></div><div class=\"row open-bet-desc\"><div class=\"col-xs-3\"><div class=\"text-center\"><span>Jun 14 07:10 PM </span></div></div><div class=\"col-xs-6\"><div class=\"text-center\"><span style=\"color:#262626;\">[918] TB RAYS -1Â½+125<BR>( A. HEANEY -L / B. SNELL -L )</span></div></div></div></div><div class=\"open-risk-win report-important-text\" id=\"\"><div class=\"row\"><div class=\"col-xs-6\"><div><span>Risk Amount: $4000</span></div></div><div class=\"col-xs-6\"><div class=\"text-right\"><span>To Win Amount: $5000</span></div></div></div></div></div><TR CLASS=\"GameHeader\"><TD colspan=\"4\" align=\"right\">Open Bets: 4</TD><TD colspan=\"2\" align=\"right\">Total Amount: $20680 / $18200</TD></TR></TABLE></div><button type=\"button\" onclick=\"topFunction()\" id=\"toTopBtn\" title=\"Back to Top\">Back to Top</button></div><div class=\"container-default\"></div></div><script>        setTimeout(function(){ hideLinesModules(); }, 500);    </script></form><footer class=\"main-footer hidden-lg\"><div class=\"main-foot\"><a href=\"#\" id=\"sports-icon\" class=\"footer-item sprite-football\" data-direction=\"bottom\"><span class=\"foot-text\">Sports</span></a><a href=\"/app/wager/OpenBets.aspx\" id=\"pending-icon\" class=\"footer-item icon-footer glyphicon glyphicon-list-alt\" data-direction=\"bottom\"><span class=\"foot-text gryph-text-align\">Pending</span></a><a href=\"/app/wager/History.aspx\" id=\"history-icon\" class=\"footer-item icon-footer glyphicon glyphicon-calendar\" data-direction=\"bottom\"><span class=\"foot-text gryph-text-align\">History</span></a><a href=\"#\" onclick=\"openMenuMobile();\" class=\"footer-item icon-footer glyphicon glyphicon-menu-hamburger\" data-direction=\"bottom\"><span class=\"foot-text gryph-text-align-menu\">Menu</span></a></div></footer></div><script src=\"/app/assets/js/bootstrap/bootstrap.min.js\"></script><script type=\"text/javascript\" src=\"/app/assets/js/bootstrap-select/bootstrap-select.js\"></script><script type=\"text/javascript\" src=\"/app/assets/js/plugins.js\"></script><script type=\"text/javascript\" src=\"/app/wager/betslip/web.js?v=149\"></script><script type=\"text/javascript\" src=\"/app/wager/betslip/md5.js\"></script><script type=\"text/javascript\">     var url = location.host;     var domain = url.split(\".\").slice(-2)[0];     $('.logo').html('<img alt=\"Logo\" width=\"100\" src=\"/app/assets/img/logos/' + domain + '.png\">');     //User Desktop Version     function getCookie(cname) {        var name = cname + \"=\";        var decodedCookie = decodeURIComponent(document.cookie);        var ca = decodedCookie.split(';');        for(var i = 0; i <ca.length; i++) {            var c = ca[i];            while (c.charAt(0) == ' ') {                c = c.substring(1);            }            if (c.indexOf(name) == 0) {                return c.substring(name.length, c.length);            }        }        return \"\";     }     var desktopversion = getCookie(\"desktopversion\");        if (desktopversion != \"\") {            if(desktopversion == \"1\")             {                 var myViewport = document.querySelector(\"meta[name=viewport]\");            if ($(window).width() < 1024){                myViewport.setAttribute(\"content\", \"width=1800; user-scalable = yes\");                } else {                myViewport.setAttribute(\"content\", \"width=device-width; initial-scale = 1.0\");            }            }        }       </script></body></html>", "accountName", "accountId");
			
//			for (PendingEvent pe : pes) {
//				System.out.println("PendingEvent: " + pe);
//			}
		} catch (Throwable be) {
			LOGGER.error(be.getMessage(), be);
		}
	}

	/**
	 * 
	 * @param xhtml
	 * @param accountName
	 * @param accountId
	 * @return
	 * @throws BatchException
	 */
	public Set<PendingEvent> parsePendingBets(String xhtml, String accountName, String accountId)
			throws BatchException {
		LOGGER.info("Entering parsePendingBets()");
		LOGGER.debug("accountName: " + accountName);
		LOGGER.debug("accountId: " + accountId);
		Set<PendingEvent> pendingEvents = new HashSet<PendingEvent>();

		if (xhtml != null) {
			xhtml = xhtml.replace("Â", "");
		}

		// Get the document object
		final Document doc = parseXhtml(xhtml);
		Elements divs = doc.select(".open-bet-box");
		LOGGER.debug("divs: " + divs);

		if (divs != null && divs.size() > 0) {
			for (Element div : divs) {
				final String divHtml = div.html();
				LOGGER.debug("divhtml: " + divHtml);
				if (divHtml.contains("STRAIGHT BET")) {
					PendingEvent pe = null;
					pe = new PendingEvent();
					pe.setDatecreated(new Date());
					pe.setDatemodified(new Date());
					pe.setCustomerid(accountName);
					pe.setInet(accountName);
					pe.setAccountname(accountName);
					pe.setAccountid(accountId);

					try {
						int s = 0;
						final Elements spans = div.select(".open-bet-top-text div div span");
						LOGGER.debug("spans: " + spans);

						if (spans != null && spans.size() > 0) {
							for (Element span : spans) {
								switch (s++) {
								case 0:
									pe.setTicketnum(span.html().replace("Ticket #", "").trim());
								case 1:
									pe.setDateaccepted(span.html().replace("Accepted: ", "").trim());
								case 2:
									final String gameSport = span.html().replace("<br>", "").trim();
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
								case 3:
									// "Sep 15 12:00 PM "
									try {
										String sdate = span.html().trim();
										final Calendar now = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
										int offset = now.get(Calendar.DST_OFFSET);
										sdate = now.get(Calendar.YEAR) + " " + sdate + " " + timeZoneLookup(timezone, offset);
										LOGGER.error("sdate: " + sdate);
										Date gamedate = GAME_DATE.parse(sdate);
										LOGGER.error("gamedate: " + gamedate);
										pe.setGamedate(gamedate);
										pe.setEventdate(sdate);
									} catch (Throwable t) {
										LOGGER.warn(t.getMessage());
									}
									break;
								case 4:
									// [127] OKLAHOMA -16½-105
									// [188] ARIZONA STATE -16½-115 (B+½)
									// [188] ARIZONA STATE u53-105
									// [972] CHICAGO WHITE SOX u9-125 ( A HEANEY -L / R LOPEZ -R )
									// [922] CLE INDIANS ML+101<br>( J. BERRIOS -R / T. BAUER -R )
									String eventinfo = span.html().replace("<br>", " ").trim();

									if (eventinfo.contains("(B") && (!eventinfo.contains(" -L ") || !eventinfo.contains(" -R "))) {
										int bindex = eventinfo.indexOf("(B");
										int eindex = eventinfo.indexOf(")");
										if (bindex != -1 && eindex != -1) {
											eventinfo = eventinfo.substring(0, bindex);
											eventinfo = eventinfo.substring(0, eindex + 1);
										}
									} else if (eventinfo.contains(" -L ")) {
										int bindex = eventinfo.indexOf("(");
										if (bindex != -1) {
											pe.setPitcher(eventinfo.substring(bindex + 1).replace(")", "").trim());
											eventinfo = eventinfo.substring(0, bindex);
										}
									} else if (eventinfo.contains(" -R ")) {
										int bindex = eventinfo.indexOf("(");
										if (bindex != -1) {
											pe.setPitcher(eventinfo.substring(bindex + 1).replace(")", "").trim());
											eventinfo = eventinfo.substring(0, bindex);
										}
									} else if (eventinfo.contains(" L ")) {
										int bindex = eventinfo.indexOf("(");
										if (bindex != -1) {
											pe.setPitcher(eventinfo.substring(bindex + 1).replace(")", "").trim());
											eventinfo = eventinfo.substring(0, bindex);
										}
									} else if (eventinfo.contains(" R ")) {
										int bindex = eventinfo.indexOf("(");
										if (bindex != -1) {
											pe.setPitcher(eventinfo.substring(bindex + 1).replace(")", "").trim());
											eventinfo = eventinfo.substring(0, bindex);
										}
									} else if (eventinfo.contains(" ACTION ")) {
										int bindex = eventinfo.indexOf("(");
										if (bindex != -1) {
											pe.setPitcher(eventinfo.substring(bindex + 1).replace(")", "").trim());
											eventinfo = eventinfo.substring(0, bindex);
										}
									}

									int index = eventinfo.indexOf("[");
									if (index != -1) {
										eventinfo = eventinfo.substring(index + 1);
										index = eventinfo.indexOf("]");

										if (index != -1) {
											final String rotationid = eventinfo.substring(0, index).trim();
											pe.setRotationid(rotationid);
											eventinfo = eventinfo.substring(index + 1).trim();
											index = eventinfo.lastIndexOf(" ");
											if (index != -1) {
												String name = eventinfo.substring(0, index).trim();
												if (name.startsWith("1H")) {
													pe.setLinetype("first");
													name = name.replace("1H", "").trim();
												} else if (name.startsWith("2H")) {
													pe.setLinetype("second");
													name = name.replace("2H", "").trim();
												} else {
													pe.setLinetype("game");
												}
												pe.setTeam(name);

												eventinfo = eventinfo.substring(index + 1).trim();
												if (eventinfo.startsWith("o")) {
													pe.setEventtype("total");

													if (eventinfo != null) {
														eventinfo = eventinfo.trim();
														// u9EV or o42½-110 or
														pe.setLineplusminus(eventinfo.substring(0, 1));
														eventinfo = eventinfo.substring(1);
														int evindex = eventinfo.indexOf("EV");
														int pkindex = eventinfo.indexOf("PK");

														if (evindex != -1 || pkindex != -1) {
															if (evindex != -1) {
																LOGGER.debug("eventinfo: " + eventinfo);
																pe.setLine(reformatValues(
																		eventinfo.substring(0, evindex)));
																pe.setJuiceplusminus("+");
																pe.setJuice("100");
																eventinfo = eventinfo.substring(evindex + 2);
																if (eventinfo != null) {
																	eventinfo = eventinfo.trim();
																	final String gameType = pe.getGametype();
																	if ("MLB".equals(gameType)) {
																		// o4½-110 (1H HOU ASTROS vrs 1H TEX RANGERS)
																		int paindex = eventinfo.indexOf("(");
																		if (paindex != -1) {
																			String tempeventinfo = eventinfo
																					.substring(paindex);
																			tempeventinfo = tempeventinfo.replace("(",
																					"");
																			tempeventinfo = tempeventinfo.replace(")",
																					"");
																			tempeventinfo = tempeventinfo.trim();

																			pe.setTeam(tempeventinfo);
																		}
																	}
																}
															} else {
																LOGGER.debug("eventinfo: " + eventinfo);
																pe.setLine(reformatValues(
																		eventinfo.substring(0, pkindex)));
																pe.setJuiceplusminus("+");
																pe.setJuice("100");
															}
														} else {
															int mindex = eventinfo.indexOf("-");
															int pindex = eventinfo.indexOf("+");

															if (mindex != -1 || pindex != -1) {
																final String gameType = pe.getGametype();

																if (mindex != -1) {
																	pe.setLine(reformatValues(
																			eventinfo.substring(0, mindex)));
																	pe.setJuiceplusminus("-");

																	if ("MLB".equals(gameType)) {
																		// o4½-110 (1H HOU ASTROS vrs 1H TEX RANGERS)
																		eventinfo = eventinfo.substring(mindex + 1);
																		int paindex = eventinfo.indexOf("(");
																		if (paindex != -1) {
																			String tempeventinfo = eventinfo
																					.substring(0, paindex).trim();
																			pe.setJuice(reformatValues(tempeventinfo));
																			tempeventinfo = eventinfo
																					.substring(paindex);
																			tempeventinfo = tempeventinfo.replace("(",
																					"");
																			tempeventinfo = tempeventinfo.replace(")",
																					"");
																			tempeventinfo = tempeventinfo.trim();

																			pe.setTeam(tempeventinfo);
																		}
																	} else {
																		pe.setJuice(reformatValues(
																				eventinfo.substring(mindex + 1)));
																	}
																} else {
																	pe.setLine(reformatValues(
																			eventinfo.substring(0, pindex)));
																	pe.setJuiceplusminus("+");

																	if ("MLB".equals(gameType)) {
																		// o4½-110 (1H HOU ASTROS vrs 1H TEX RANGERS)
																		eventinfo = eventinfo.substring(pindex + 1);
																		int paindex = eventinfo.indexOf("(");
																		if (paindex != -1) {
																			String tempeventinfo = eventinfo
																					.substring(0, paindex).trim();
																			LOGGER.debug(
																					"teampeventinfo: " + tempeventinfo);

																			pe.setJuice(reformatValues(tempeventinfo));
																			tempeventinfo = eventinfo
																					.substring(paindex);
																			tempeventinfo = tempeventinfo.replace("(",
																					"");
																			tempeventinfo = tempeventinfo.replace(")",
																					"");
																			tempeventinfo = tempeventinfo.trim();

																			pe.setTeam(tempeventinfo);
																		}
																	} else {
																		pe.setJuice(reformatValues(
																				eventinfo.substring(pindex + 1)));
																	}
																}
															}
														}
													}
												} else if (eventinfo.startsWith("u")) {
													pe.setEventtype("total");

													pe.setEventtype("total");
													if (eventinfo != null) {
														eventinfo = eventinfo.trim();
														// u9EV or o42½-110 or
														pe.setLineplusminus(eventinfo.substring(0, 1));
														eventinfo = eventinfo.substring(1);
														int evindex = eventinfo.indexOf("EV");
														int pkindex = eventinfo.indexOf("PK");

														if (evindex != -1 || pkindex != -1) {
															if (evindex != -1) {
																LOGGER.debug("eventinfo: " + eventinfo);
																pe.setLine(reformatValues(
																		eventinfo.substring(0, evindex)));
																pe.setJuiceplusminus("+");
																pe.setJuice("100");
																eventinfo = eventinfo.substring(evindex + 2);
																if (eventinfo != null) {
																	eventinfo = eventinfo.trim();
																	final String gameType = pe.getGametype();
																	if ("MLB".equals(gameType)) {
																		// o4½-110 (1H HOU ASTROS vrs 1H TEX RANGERS)
																		int paindex = eventinfo.indexOf("(");
																		if (paindex != -1) {
																			String tempeventinfo = eventinfo
																					.substring(paindex);
																			tempeventinfo = tempeventinfo.replace("(",
																					"");
																			tempeventinfo = tempeventinfo.replace(")",
																					"");
																			tempeventinfo = tempeventinfo.trim();

																			pe.setTeam(tempeventinfo);
																		}
																	}
																}
															} else {
																LOGGER.debug("eventinfoXXX: " + eventinfo);
																pe.setLine(reformatValues(eventinfo.substring(0, pkindex)));
																pe.setJuiceplusminus("+");
																pe.setJuice("100");
															}
														} else {
															int mindex = eventinfo.indexOf("-");
															int pindex = eventinfo.indexOf("+");

															if (mindex != -1 || pindex != -1) {
																final String gameType = pe.getGametype();

																if (mindex != -1) {
																	pe.setLine(reformatValues(
																			eventinfo.substring(0, mindex)));
																	pe.setJuiceplusminus("-");

																	if ("MLB".equals(gameType)) {
																		// o4½-110 (1H HOU ASTROS vrs 1H TEX RANGERS)
																		eventinfo = eventinfo.substring(mindex + 1);
																		int paindex = eventinfo.indexOf("(");
																		if (paindex != -1) {
																			String tempeventinfo = eventinfo
																					.substring(0, paindex).trim();
																			pe.setJuice(reformatValues(tempeventinfo));
																			tempeventinfo = eventinfo
																					.substring(paindex);
																			tempeventinfo = tempeventinfo.replace("(",
																					"");
																			tempeventinfo = tempeventinfo.replace(")",
																					"");
																			tempeventinfo = tempeventinfo.trim();

																			pe.setTeam(tempeventinfo);
																		}
																	} else {
																		pe.setJuice(reformatValues(
																				eventinfo.substring(mindex + 1)));
																	}
																} else {
																	pe.setLine(reformatValues(
																			eventinfo.substring(0, pindex)));
																	pe.setJuiceplusminus("+");

																	if ("MLB".equals(gameType)) {
																		// o4½-110 (1H HOU ASTROS vrs 1H TEX RANGERS)
																		eventinfo = eventinfo.substring(pindex + 1);
																		int paindex = eventinfo.indexOf("(");
																		if (paindex != -1) {
																			String tempeventinfo = eventinfo
																					.substring(0, paindex).trim();
																			LOGGER.debug(
																					"teampeventinfo: " + tempeventinfo);

																			pe.setJuice(reformatValues(tempeventinfo));
																			tempeventinfo = eventinfo
																					.substring(paindex);
																			tempeventinfo = tempeventinfo.replace("(",
																					"");
																			tempeventinfo = tempeventinfo.replace(")",
																					"");
																			tempeventinfo = tempeventinfo.trim();

																			pe.setTeam(tempeventinfo);
																		}
																	} else {
																		pe.setJuice(reformatValues(
																				eventinfo.substring(pindex + 1)));
																	}
																}
															}
														}
													}
												} else {
													eventinfo = eventinfo.replace(" ", "");
													LOGGER.error("eventinfoPPP: " + eventinfo);

													// Check for money first
													if (eventinfo.startsWith("ML+") || eventinfo.startsWith("ML-")) {
														eventinfo = eventinfo.replace("ML", "");
														LOGGER.error("eventinfo2: " + eventinfo);

														// We have moneyline
														pe.setEventtype("ml");
														pe.setLineplusminus(eventinfo.substring(0, 1));
														pe.setLine(super.reformatValues(eventinfo.substring(1)));
														pe.setJuiceplusminus(eventinfo.substring(0, 1));
														pe.setJuice(super.reformatValues(eventinfo.substring(1)));
													} else if ((eventinfo.startsWith("EV") || eventinfo.startsWith("PK"))
															&& (eventinfo.length() == 2)) {
														// We have moneyline
														pe.setEventtype("ml");
														pe.setLineplusminus("+");
														pe.setLine("100");
														pe.setJuiceplusminus("+");
														pe.setJuice("100");
													} else if ((eventinfo.startsWith("+") || eventinfo.startsWith("-"))
															&& (eventinfo.length() == 4) && (!eventinfo.endsWith("EV")
																	&& !eventinfo.endsWith("PK"))) {
														// We have moneyline
														pe.setEventtype("ml");
														pe.setLineplusminus(eventinfo.substring(0, 1));
														pe.setLine(super.reformatValues(eventinfo.substring(1)));
														pe.setJuiceplusminus(eventinfo.substring(0, 1));
														pe.setJuice(super.reformatValues(eventinfo.substring(1)));
													} else {
														// Spread
														pe.setEventtype("spread");

														if (eventinfo.startsWith("EV") || eventinfo.startsWith("PK")) {
															pe.setLineplusminus("+");
															pe.setLine("100");
															// Now get the Juice
															eventinfo = eventinfo.substring(2);
															if (eventinfo.startsWith("EV") || 
																eventinfo.startsWith("PK")) {
																pe.setJuiceplusminus("+");
																pe.setJuice("100");
															} else if (eventinfo.startsWith("+") || 
																	eventinfo.startsWith("-")) {
																pe.setJuiceplusminus(eventinfo.substring(0, 1));
																pe.setJuice(super.reformatValues(eventinfo.substring(1)));
															}
														} else {
															pe.setLineplusminus(eventinfo.substring(0, 1));
															eventinfo = eventinfo.substring(1);

															int eindex = eventinfo.lastIndexOf("EV");
															int pindex = eventinfo.lastIndexOf("PK");
															int plindex = eventinfo.lastIndexOf("+");
															int mindex = eventinfo.lastIndexOf("-");
															LOGGER.debug("eindex: " + eindex);
															LOGGER.debug("pindex: " + pindex);
															LOGGER.debug("plindex: " + plindex);
															LOGGER.debug("mindex: " + mindex);

															// Now get the line value and juice
															if (eindex != -1) {
																pe.setLine(super.reformatValues(
																		eventinfo.substring(0, eindex)));
																pe.setJuiceplusminus("+");
																pe.setJuice("100");
															} else if (pindex != -1) {
																pe.setLine(super.reformatValues(
																		eventinfo.substring(0, pindex)));
																pe.setJuiceplusminus("+");
																pe.setJuice("100");
															} else if (plindex != -1) {
																pe.setLine(super.reformatValues(
																		eventinfo.substring(0, plindex)));
																pe.setJuiceplusminus("+");
																pe.setJuice(super.reformatValues(
																		eventinfo.substring(plindex + 1)));
															} else if (mindex != -1) {
																pe.setLine(super.reformatValues(
																		eventinfo.substring(0, mindex)));
																pe.setJuiceplusminus("-");
																pe.setJuice(super.reformatValues(
																		eventinfo.substring(mindex + 1)));
															}
														}
													}
												}
											}
										}
									}
									break;
								default:
									break;
								}
							}
						}

						s = 0;
						final Elements spansdos = div.select(".open-risk-win div div div span");
						LOGGER.debug("spansdos: " + spansdos);
						for (Element span : spansdos) {
							switch (s++) {
							case 0:
								pe.setRisk(span.html().replace("Risk Amount: $", "").trim());
							case 1:
								pe.setWin(span.html().replace("To Win Amount: $", "").trim());
							default:
								break;
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
							final AccessTokenFromRefreshToken accessTokenFromRefreshToken = new AccessTokenFromRefreshToken(
									clientid, clientsecret, refreshtoken, granttype);
							accessToken = accessTokenFromRefreshToken.getAccessToken();
							final SendText sendText = new SendText();
							sendText.setOAUTH2_TOKEN(accessToken);
							sendText.sendTextWithMessage("9132195234@vtext.com",
									"Error in pendingSite " + accountName + " " + accountId + " " + div.html());
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
	 * @param xhtml
	 * @return
	 * @throws BatchException
	 */
	public Map<String, String> parseSports(String xhtml) throws BatchException {
		LOGGER.info("Entering parseSports()");
		Map<String, String> map = new HashMap<String, String>();

		// Parse the xhtml
		final Document doc = parseXhtml(xhtml);

		// Get the form information
		final String[] types = new String[] { "hidden" };
		getAllElementsByType(doc, "input", types, map);

		LOGGER.info("Exiting parseSports()");
		return map;
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
		map = findMenu(doc.select(".panel"), map, type, sport, "div div div label", "div div div label input");

		// Remove the input fields that shouldn't be sent
		map = removeInputFields(map);

		LOGGER.info("Exiting parseMenu()");
		return map;
	}

	/**
	 * 
	 * @param xhtml
	 * @return
	 * @throws BatchException
	 */
	public Map<String, String> parseWagerTypes(String xhtml) throws BatchException {
		LOGGER.info("Entering parseWagerTypes()");
		LOGGER.debug("xhtml: " + xhtml);
		final Map<String, String> map = new HashMap<String, String>();

		// Parse to get the Document
		final Document doc = parseXhtml(xhtml);
		
		if (xhtml.contains("alert alert-danger")) {
			String errorMessage = doc.select(".alert p").html();

			if (errorMessage != null && errorMessage.length() > 0) {
				// Check for a wager limit and change it 
				if (errorMessage.contains("minimum required")) {
					throw new BatchException(BatchErrorCodes.MIN_WAGER_AMOUNT_NOT_REACHED, BatchErrorMessage.MIN_WAGER_AMOUNT_NOT_REACHED, errorMessage, xhtml);
				}
				// Check for a wager limit and change it 
				else if (errorMessage.contains("Max Wager Online Exceeded")) {
					throw new BatchException(BatchErrorCodes.MAX_WAGER_AMOUNT_REACHED, BatchErrorMessage.MAX_WAGER_AMOUNT_REACHED, errorMessage, xhtml);
				}
				// Check for a wager limit and change it 
				else if (errorMessage.contains("line has just changed to")) {
					// Line changed throw exception
					throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, errorMessage, xhtml);			
				}
				else {
					// Line changed throw exception
					throw new BatchException(BatchErrorCodes.ERROR_PARSING_WAGER_INFO, BatchErrorMessage.ERROR_PARSING_WAGER_INFO, errorMessage, xhtml);								
				}
			} else {
				errorMessage = doc.select(".alert").html();
				
				if (errorMessage != null && errorMessage.length() > 0) {
					// Check for a wager limit and change it 
					if (errorMessage.contains("minimum required")) {
						throw new BatchException(BatchErrorCodes.MIN_WAGER_AMOUNT_NOT_REACHED, BatchErrorMessage.MIN_WAGER_AMOUNT_NOT_REACHED, errorMessage, xhtml);
					}
					// Check for a wager limit and change it 
					else if (errorMessage.contains("Max Wager Online Exceeded")) {
						throw new BatchException(BatchErrorCodes.MAX_WAGER_AMOUNT_REACHED, BatchErrorMessage.MAX_WAGER_AMOUNT_REACHED, errorMessage, xhtml);
					}
					// Check for a wager limit and change it 
					else if (errorMessage.contains("line has just changed to")) {
						// Line changed throw exception
						throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, errorMessage, xhtml);			
					}
					else if (errorMessage.contains("Balance Exceeded")) {
						// Line changed throw exception
						throw new BatchException(BatchErrorCodes.ACCOUNT_BALANCE_LIMIT, BatchErrorMessage.ACCOUNT_BALANCE_LIMIT, errorMessage, xhtml);
					}
					else {
						// Line changed throw exception
						throw new BatchException(BatchErrorCodes.ERROR_PARSING_WAGER_INFO, BatchErrorMessage.ERROR_PARSING_WAGER_INFO, errorMessage, xhtml);								
					}
				}
			}
		} else {
			final String[] types = new String[] { "hidden" };
			getAllElementsByType(doc, "input", types, map);
		}

		LOGGER.info("Exiting parseWagerTypes()");
		return map;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.tdsports.TDSportsParser#parseTicketNumber(java.lang.String)
	 */
	@Override
	public String parseTicketNumber(String xhtml) throws BatchException {
		LOGGER.info("Entering parseTicketNumber()");
		String ticketNumber = "";
		final String ticketInfo = "Ticket Number : ";

		if (xhtml.contains("Your bet expired or has already been submited.")) {
			throw new BatchException(BatchErrorCodes.WAGER_HAS_EXPIRED, BatchErrorMessage.WAGER_HAS_EXPIRED, "The wager has expired or already been bet", xhtml);	
		}
		
		if (xhtml.contains("The line changed for one (or more) of your selections.")) {
			LOGGER.error("WagerShack Line Change: " + xhtml);
			// Line changed throw exception
			throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed and cannot complete transaction", xhtml);	
		}

		// First check for Wager has been accepted!
		if (xhtml != null && xhtml.contains(ticketInfo)) {
			// Great, wager is complete; now get the Ticket Number
			// <div class='row row_betslip row-margin'><div class='col-lg-12'><h3>BET(S) ACCEPTED</h3></div></div><div
			// class='row row_betslip row-margin'><div class='col-lg-12'><strong>Ticket Number : 149639087</strong
			//		 ><br />STRAIGHT BET<br>[930] TOTAL u7&frac12;-125 (SF GIANTS vrs SEA MARINERS)<br>(A SUAREZ       -L
			//		  / J PAXTON       -L)<br>Risk: 13.00 Win: 10.00<hr /><button type='button' class='btn btn-sm btn-block
			//		  btn-primary' id='pwd_button' onClick='remove_all()'>Start New Bet</button><script>refreshBalance()<
			//		 /script></div></div>
			int index = xhtml.indexOf("Ticket Number : ");
			if (index != -1) {
				final String nxhtml = xhtml.substring(index + "Ticket Number : ".length());
				index = nxhtml.indexOf("</strong>");
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
	 * @see com.ticketadvantage.services.dao.sites.tdsports.TDSportsParser#findMenu(org.jsoup.select.Elements, java.util.Map, java.lang.String[], java.lang.String[], java.lang.String, java.lang.String)
	 */
	@Override
	protected Map<String, String> findMenu(Elements divs, Map<String, String> map, String[] type, String[] sport, String foundString, String menuString) {
		LOGGER.info("Entering findMenu()");

		for (int x = 0; (divs != null && x < divs.size()); x++) {
			final Element div = divs.get(x);
			LOGGER.debug("div: " + div);
			boolean foundDiv = false;
			for (int y = 0; y < type.length; y++) {
				for (int z = 0; z < sport.length; z++) {
					final Elements sportDivs = div.select(foundString);

					for (Element sportDiv : sportDivs) {
						foundDiv = foundSport(sportDiv, foundString, type[y], sport[z]);
						LOGGER.debug("foundDiv: " + foundDiv);
		
						// Found the event
						if (foundDiv) {
							map = getMenuData(sportDiv, menuString, map);
							LOGGER.debug("Map: " + map);
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
	 * @see com.ticketadvantage.services.dao.sites.tdsports.TDSportsParser#foundSport(org.jsoup.nodes.Element, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	protected boolean foundSport(Element div, String select, String type, String sport) {
		LOGGER.info("Entering foundSport()");
		LOGGER.debug("div: " + div);
		LOGGER.debug("select: " + select);
		LOGGER.debug("type: " + type);
		LOGGER.debug("sport: " + sport);
		boolean foundDiv = false;
		
		String divData = div.html();
		LOGGER.debug("divData: " + divData);

		if (divData != null && divData.length() > 0) {
			divData = divData.trim();
			int index = divData.indexOf("\">");
			if (index != -1) {
				divData = divData.substring(index + 2).trim();
			}
		}

		// Check if we found div
		if (divData != null && divData.equals(type)) {
			foundDiv = true;
		} else if (type.startsWith("NFL - WEEK") && divData != null && divData.startsWith("NFL - WEEK")) { // HACK!!!
			foundDiv = true;
		}

		LOGGER.info("Exiting foundSport()");
		return foundDiv;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.tdsports.TDSportsParser#getMenuData(org.jsoup.nodes.Element, java.lang.String, java.util.Map)
	 */
	@Override
	protected Map<String, String> getMenuData(Element div, String element, Map<String, String> map) {
		LOGGER.info("Entering getMenuData()");
		LOGGER.debug("div: " + div);
		LOGGER.debug("element: " + element);

		final Elements inputs = div.select("input");

		if (inputs != null && inputs.size() > 0) {
			final Element input = inputs.get(0);

			if (map.containsKey("idl")) {
				final String idl = map.get("idl") + "," + input.attr("value");
				map.put("idl", idl);
			} else {
				map.put("idl", input.attr("value"));
			}
		}

		LOGGER.info("Exiting getMenuData()");
		return map;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseGames(java.lang.String, java.lang.String, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <SiteEventPackage> List<SiteEventPackage> parseGames(String xhtml, String type, Map<String, String> inputFields) throws BatchException {
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
		final Elements elements = doc.select(".panel-games div");
		if (elements != null && elements.size() > 0) {
			events = getGameData(elements, inputFields);
		}

		LOGGER.info("Exiting parseGames()");
		return (List<SiteEventPackage>)events;
	}

	/**
	 * 
	 * @param elements
	 * @param input
	 * @return
	 * @throws BatchException
	 */
	protected List<SiteEventPackage> getGameData(Elements elements, Map<String, String> input) throws BatchException {
		LOGGER.info("Entering getGameData()");
		final List<SiteEventPackage> events = new ArrayList<SiteEventPackage>();

		if (elements != null) {
			SiteTeamPackage team1 = null;
			SiteTeamPackage team2 = null;
			SiteEventPackage eventPackage = null;
			String gameDate = null;
			String gameTime = null;

			for (int x = 0; x < elements.size(); x++) {
				// Loop through the elements and then check for the dates
				final Element element = elements.get(x);
				final String className = element.attr("class");

				if (className != null && className.contains("gameDate")) {
					gameDate = element.select("div").get(0).html();

					if (gameDate != null && gameDate.length() > 0) {
						int index = gameDate.lastIndexOf("- ");

						if (index != -1) {
							gameDate = gameDate.substring(index + 2).trim();
							gameDate = gameDate.replace("</div>", "").trim();
						}
					}
				} else if (className != null && className.equals("gamebox")) {
					eventPackage = new SiteEventPackage();
					team1 = new SiteTeamPackage();
					team2 = new SiteTeamPackage();

					// Get the dates
					Elements divs = element.select(".linesTime");
					if (divs != null && divs.size() > 0) {
						gameTime = divs.get(0).attr("data-timevalue");
					}

					// Get the lines
					final Elements rows = element.select(".row");
					if (rows != null && rows.size() > 0) {
						int cnt = 0;
						for (Element row : rows) {
							if (!row.html().contains("line-divider.png") && 
								!row.html().contains("linesTime")) {
								LOGGER.debug("row: " + row);
								LOGGER.debug("cnt: " + cnt);

								if (cnt == 0) {
									setupTeamData(row, team1, input);
								} else if (cnt == 1) {
									setupTeamData(row, team2, input);
									parseDate(gameDate, gameTime, eventPackage, team1, team2);
								}

								cnt++;
							}
						}
					}

					// Set the team and packages
					eventPackage.setId(team1.getId());
					eventPackage.setSiteteamone(team1);
					eventPackage.setSiteteamtwo(team2);
					events.add(eventPackage);
				} 
			}
		}

		LOGGER.info("Exiting getGameData()");
		return events;
	}

	/**
	 * 
	 * @param row
	 * @param team
	 * @param input
	 */
	private void setupTeamData(Element row, SiteTeamPackage team, Map<String, String> input) {
		// Get rotation ID
		parseRotationid(row.select(".linesRot"), team);

		// Get Team Info
		parseTeam(row.select(".linesTeam"), team);

		// Get the spread
		parseSpread(row.select(".linesSpread"), team, input);

		// Get the total
		parseTotal(row.select(".linesTotal"), team, input);

		// Get the ml
		parseMl(row.select(".linesMl"), team, input);		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ticketadvantage.services.dao.sites.tdsports.TDSportsParser#getTeamData(
	 * org.jsoup.select.Elements,
	 * com.ticketadvantage.services.dao.sites.tdsports.TDSportsTeamPackage)
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
			for (int x = 0; (elements != null && x < elements.size()); x++) {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#getGameData(org.jsoup.
	 * select.Elements)
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
			int t = 0;
			for (int x = 0; x < elements.size(); x++) {
				// Loop through the elements and then check for the dates
				final Element element = elements.get(x);
				LOGGER.debug("Element: " + element);

				if (element != null) {
					String classInfo = element.attr("class");
					LOGGER.debug("ClassInfo: " + classInfo);

					if ((classInfo != null) && ((classInfo.length() == 0) || (classInfo.contains("dd_rowDark")))) {
						final Elements tds = element.select("td");
						if (tds != null && tds.size() == 7) {
							if (t++ == 0) {
								eventPackage = new TDSportsEventPackage();
								team1 = new TDSportsTeamPackage();
								int size = getTeamData(tds, team1);
								LOGGER.debug("size: " + size);
								eventPackage.setId(team1.getId());
							} else {
								team2 = new TDSportsTeamPackage();
								int size = getTeamData(tds, team2);
								LOGGER.debug("size: " + size);
								Date eventDate = null;
								if (size == 7 || size == 5) {
									eventDate = setupDate(DATE_FORMAT, team1.gettDate(), team2.gettDate());
								} else {
									// Oct 01<br>7:30 PM
									String dt = team1.gettDate();
									if (dt != null) {
										int index = dt.indexOf("<br>");
										if (index != -1) {
											team1.settDate(dt.substring(0, index));
											team2.settDate(dt.substring(index + "<br>".length()));
										}
									}
									eventDate = setupDate(DATE_FORMAT, team1.gettDate(), team2.gettDate());
								}
								team1.setEventdatetime(eventDate);
								team2.setEventdatetime(eventDate);
								final String dateOfEvent = team1.gettDate() + " " + team2.gettDate();
								team1.settDate(dateOfEvent);
								team2.settDate(dateOfEvent);
								eventPackage.setDateofevent(dateOfEvent);
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

		LOGGER.info("Exiting getGameData()");
		return events;
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
				pe.setGamedate(fixDate(year + " " + gamedate + " " + determineTimeZone(super.timezone),
						PENDING_DATE_FORMAT.get()));
			} catch (ParseException pe1) {
				pe.setGamedate(fixDate(year + " " + gamedate + " " + determineTimeZone(super.timezone),
						PENDING_DATE_FORMAT_2.get()));
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

	/**
	 * 
	 * @param divs
	 * @param team
	 */
	private void parseRotationid(Elements divs, SiteTeamPackage team) {
		LOGGER.info("Entering parseRotationid()");

		// Get a valid rotation ID
		if (divs != null && divs.size() > 0) {
			String rotId = divs.get(0).html().trim();
			rotId = rotId.replace("&nbsp;", "");
			team.setEventid(rotId);
			team.setId(Integer.parseInt(rotId));
		}

		LOGGER.info("Exiting parseRotationid()");
	}

	/**
	 * 
	 * @param divs
	 * @param team
	 */
	private void parseTeam(Elements divs, SiteTeamPackage team) {
		LOGGER.info("Entering parseTeam()");

		// Get a valid rotation ID
		if (divs != null && divs.size() > 0) {
			String divhtml = divs.get(0).html();

			if (divhtml != null && divhtml.length() > 0) {
				int index = divhtml.indexOf("</span>");
				if (index != -1) {
					divhtml = divhtml.substring(index + 7).trim();
					team.setTeam(divhtml);
				}
			}
		}

		LOGGER.info("Exiting parseTeam()");
	}

	/**
	 * 
	 * @param gameDate
	 * @param gameTime
	 * @param eventPackage
	 * @param team1
	 * @param team2
	 */
	private void parseDate(String gameDate, String gameTime, SiteEventPackage eventPackage, SiteTeamPackage team1, SiteTeamPackage team2) {
		LOGGER.info("Entering parseDate()");

		// Dec 15 2018 4:05:00 PM
		final Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH);
		boolean isJanuary = gameDate.startsWith("Jan");
		int year = cal.get(Calendar.YEAR);

		if (month == 11 && isJanuary) {
			year++;
		}

		final String dateTime = gameDate + " " + year + " " + gameTime + " " + super.determineTimeZone(this.timezone);
		Date dateAndTime = null;
		try {
			dateAndTime = GAME_DATE_FORMATTER.parse(dateTime);
		} catch (ParseException pe) {
			LOGGER.error(pe.getMessage(), pe);
		}
		team1.setEventdatetime(dateAndTime);
		team2.setEventdatetime(dateAndTime);
		eventPackage.setEventdatetime(dateAndTime);
		team1.setDateofevent(dateTime);
		team2.setDateofevent(dateTime);
		eventPackage.setDateofevent(dateTime);
		eventPackage.setEventdate(gameDate);
		eventPackage.setEventtime(gameTime);

		LOGGER.info("Exiting parseDate()");
	}

	/**
	 * 
	 * @param div
	 * @param team
	 * @param input
	 */
	private void parseSpread(Elements divs, SiteTeamPackage team, Map<String, String> input) {
		LOGGER.info("Entering parseSpread()");

		// 
		// amt	0
		// b		1_1960731_-1.5_-105,
		// c		1
		// idc	209189254
		// idp	2775
		// pid	6800
		// 
		// <div id="1960731_1_S" class="betting-lines-line-line div-active" onclick="addbet('1','1960731','S','LA ANGELS','-1.5','-105','MLB','5','974');">
		// 	 <a class="regular-line" href="javascript:void(0)">-1½ -105</a>
		// </div>
		// 
		// https://playitez.com/wager/betslip/getstraight.asp?pid=6800&idp=2775&idc=209189254&b=3_1960731_9_110%2C&c=1&amt=0
		// 
		// https://playitez.com/wager/betslip/getstraight.asp?pid=6800&idp=2775&idc=209189254&b=3_1960731_9_110_0_0%2C&c=1&amt=1&fp=0
		// 
		if (divs != null && divs.size() > 0) {
			final Element div = divs.get(0);
			String onclick = div.attr("onclick");
			onclick = onclick.replace("&#39;", "'");

			int index = onclick.indexOf("addbet('");
			String bvalue = "";
			if (index != -1) {
				// https://playitez.com/wager/betslip/getstraight.asp?pid=6800&idp=2775&idc=209198738&b=4_1961452_0_175_100000000_3%2C&c=1&amt=1&fp=0
				onclick = onclick.substring(index + "addbet('".length());
				index = onclick.indexOf("'");
				// 4','1961452','ML','SF GIANTS','0','175','MLB','5','930');
				bvalue = onclick.substring(0, index);
				LOGGER.debug("bvalue1: " + bvalue);
	
				index = onclick.indexOf(",'");
				onclick = onclick.substring(index + 2);
				index = onclick.indexOf("'");
				
				// ,'1961452','ML','SF GIANTS','0','175','MLB','5','930');
				bvalue = bvalue + "_" + onclick.substring(0, index);
				LOGGER.debug("bvalue2: " + bvalue);
	
				// ,'ML','SF GIANTS','0','175','MLB','5','930');
				index = onclick.indexOf(",'");
				onclick = onclick.substring(index + 2);
				index = onclick.indexOf(",'");
				onclick = onclick.substring(index + 2);
				index = onclick.indexOf(",'");
				onclick = onclick.substring(index + 2);
				LOGGER.debug("onclick: " + onclick);
				
				// ,'0','175','MLB','5','930');
				index = onclick.indexOf("'");
				bvalue = bvalue + "_" + onclick.substring(0, index);
				LOGGER.debug("bvalue3: " + bvalue);
	
				index = onclick.indexOf(",'");
				onclick = onclick.substring(index + 2);
				LOGGER.debug("onclick2: " + onclick);
	
				// ,'175','MLB','5','930');
				index = onclick.indexOf("'");
				bvalue = bvalue + "_" + onclick.substring(0, index);
				LOGGER.debug("bvalue4: " + bvalue);
			}
			input.put("b", bvalue);
			input.put("c", "1");
			input.put("amt", "0");
	
			final Elements buttons = div.select("button");
			if (buttons != null && buttons.size() > 0) {
				final Element button = buttons.get(0);
				final String spread = button.html().replace("<br>", "").replace("(", "").replace(")", "").trim();

				// First get the input field information
				team.setGameSpreadInputId(bvalue);
				team.setGameSpreadInputName("b");
				team.setGameSpreadInputValue(bvalue);
	
				// Setup spread
				team = (SiteTeamPackage)parseSpreadData(reformatValues(spread), 0, " ", null, team);
			}
		}

		LOGGER.info("Exiting parseSpread()");
	}

	/**
	 * 
	 * @param div
	 * @param team
	 * @param input
	 */
	private void parseTotal(Elements divs, SiteTeamPackage team, Map<String, String> input) {
		LOGGER.info("Entering parseTotal()");
		//
		// pid=6800
		// idp=2775
		// idc=209189254
		// b=3_1960731_9_110%2C
		// c=1
		// amt=0
		//
		// <div id="1960731_3_Un" class="betting-lines-line-line div-active" onclick="addbet('2','1961452','Ov','SF GIANTS','7.5','105','MLB','5','929');">
		//	<a class="regular-line" href="javascript:void(0)">Un 9+110</a>
		// </div>
		//
		if (divs != null && divs.size() > 0) {
			final Element div = divs.get(0);
			String onclick = div.attr("onclick");
			onclick = onclick.replace("&#39;", "'");

			boolean ov = false;
			if (onclick != null && onclick.contains("'Ov'")) {
				ov = true;
			}

			int index = onclick.indexOf("addbet('");
			String bvalue = "";
			if (index != -1) {
				onclick = onclick.substring(index + "addbet('".length());
				index = onclick.indexOf("'");
				// 2','1961452','Ov','SF GIANTS','7.5','105','MLB','5','929');
				bvalue = onclick.substring(0, index);
				LOGGER.debug("bvalue1: " + bvalue);
	
				index = onclick.indexOf(",'");
				onclick = onclick.substring(index + 2);
				index = onclick.indexOf("'");
				
				// ,'1961452','Ov','SF GIANTS','7.5','105','MLB','5','929');
				bvalue = bvalue + "_" + onclick.substring(0, index);
				LOGGER.debug("bvalue2: " + bvalue);
	
				// ,'Ov','SF GIANTS','7.5','105','MLB','5','929');
				index = onclick.indexOf(",'");
				onclick = onclick.substring(index + 2);
				index = onclick.indexOf(",'");
				onclick = onclick.substring(index + 2);
				index = onclick.indexOf(",'");
				onclick = onclick.substring(index + 2);
				LOGGER.debug("onclick: " + onclick);
				
				// ,'7.5','105','MLB','5','929');
				index = onclick.indexOf("'");
				if (ov) {
					bvalue = bvalue + "_-" + onclick.substring(0, index);
				} else {
					bvalue = bvalue + "_" + onclick.substring(0, index);
				}
				LOGGER.debug("bvalue3: " + bvalue);
	
				index = onclick.indexOf(",'");
				onclick = onclick.substring(index + 2);
				LOGGER.debug("onclick2: " + onclick);
	
				// ,'105','MLB','5','929');
				index = onclick.indexOf("'");
				bvalue = bvalue + "_" + onclick.substring(0, index);
				LOGGER.debug("bvalue4: " + bvalue);
			}
			input.put("b", bvalue);
			input.put("c", "1");
			input.put("amt", "0");
	
			final Elements buttons = div.select("button");
			if (buttons != null && buttons.size() > 0) {
				final Element button = buttons.get(0);
				final String total = button.html().replace("Ov", "o").replace("Un", "u").replace("<br>", "").replace("(", "").replace(")", "").trim();

				// First get the input field information
				team.setGameTotalInputId(bvalue);
				team.setGameTotalInputName("b");
				team.setGameTotalInputValue(bvalue);
	
				// Setup total
				team = (SiteTeamPackage)parseTotalData(reformatValues(total), 0, null, null, team);
			}
		}

		LOGGER.info("Exiting parseTotal()");
	}

	/**
	 * 
	 * @param div
	 * @param team
	 * @param input
	 */
	private void parseMl(Elements divs, SiteTeamPackage team, Map<String, String> input) {
		LOGGER.info("Entering parseMl()");
		//
		// pid=6800
		// idp=2775
		// idc=209189254
		// b=3_1960731_9_110%2C
		// c=1
		// amt=0
		//
		// <div id="1960731_3_Un" class="betting-lines-line-line div-active" onclick="addbet('3','1960731','Un','LA ANGELS','9','110','MLB','5','974');">
		//	<a class="regular-line" href="javascript:void(0)">Un 9+110</a>
		// </div>
		//
		if (divs != null && divs.size() > 0) {
			final Element div = divs.get(0);
			String onclick = div.attr("onclick");
			onclick = onclick.replace("&#39;", "'");

			int index = onclick.indexOf("addbet('");
			String bvalue = "";
			if (index != -1) {
				onclick = onclick.substring(index + "addbet('".length());
				index = onclick.indexOf("'");
				// 4','1961452','ML','SF GIANTS','0','175','MLB','5','930');
				bvalue = onclick.substring(0, index);
				LOGGER.debug("bvalue1: " + bvalue);
	
				index = onclick.indexOf(",'");
				onclick = onclick.substring(index + 2);
				index = onclick.indexOf("'");
				
				// ,'1961452','ML','SF GIANTS','0','175','MLB','5','930');
				bvalue = bvalue + "_" + onclick.substring(0, index);
				LOGGER.debug("bvalue2: " + bvalue);
	
				// ,'ML','SF GIANTS','0','175','MLB','5','930');
				index = onclick.indexOf(",'");
				onclick = onclick.substring(index + 2);
				index = onclick.indexOf(",'");
				onclick = onclick.substring(index + 2);
				index = onclick.indexOf(",'");
				onclick = onclick.substring(index + 2);
				LOGGER.debug("onclick: " + onclick);
				
				// ,'0','175','MLB','5','930');
				index = onclick.indexOf("'");
				bvalue = bvalue + "_" + onclick.substring(0, index);
				LOGGER.debug("bvalue3: " + bvalue);
	
				index = onclick.indexOf(",'");
				onclick = onclick.substring(index + 2);
				LOGGER.debug("onclick2: " + onclick);
	
				// ,'175','MLB','5','930');
				index = onclick.indexOf("'");
				bvalue = bvalue + "_" + onclick.substring(0, index);
				LOGGER.debug("bvalue4: " + bvalue);
			}
			input.put("b", bvalue);
			input.put("c", "1");
			input.put("amt", "0");

			final Elements buttons = div.select("button");
			if (buttons != null && buttons.size() > 0) {
				final Element button = buttons.get(0);
				final String ml = button.html().replace("<br>", "").replace("(", "").replace(")", "").trim();
				LOGGER.debug("ml: " + ml);
	
				// First get the input field information
				team.setGameMLInputId(bvalue);
				team.setGameMLInputName("b");
				team.addGameMLInputValue("0", bvalue);
	
				// Setup ml
				team = (SiteTeamPackage)parseMlData(reformatValues(ml), 0, team);
			}
		}

		LOGGER.info("Exiting parseMl()");
	}
	
	/**
	 * 
	 * @param label
	 * @param total
	 * @param team
	 */
	private void setupTotalData(Element label, String total, SiteTeamPackage team) {
		LOGGER.info("Entering setupTotalData()");

		// First get the input field information
		final Map<String, String> hashMap = parseInputField(label.select("input"), 0);
		if (hashMap != null && !hashMap.isEmpty()) {
			team.setGameTotalInputId(hashMap.get("id"));
			team.setGameTotalInputName(hashMap.get("name"));
			team.setGameTotalInputValue(hashMap.get("value"));
		}

		// Setup total
		team = (SiteTeamPackage)parseTotalData(reformatValues(total), 0, " ", null, team);

		LOGGER.info("Exiting setupTotalData()");
	}

	/**
	 * 
	 * @param label
	 * @param ml
	 * @param team
	 */
	private void setupMlData(Element label, String ml, SiteTeamPackage team) {
		LOGGER.info("Entering setupMlData()");

		final Map<String, String> hashMap = parseInputField(label.select("input"), 0);
		if (hashMap != null && !hashMap.isEmpty()) {
			team.setGameMLInputId(hashMap.get("id"));
			team.setGameMLInputName(hashMap.get("name"));
			team.addGameMLInputValue("0", hashMap.get("value"));
			team = (SiteTeamPackage)parseMlData(reformatValues(ml), 0, team);
		}

		LOGGER.info("Exiting setupMlData()");
	}

	/**
	 * 
	 * @param spread
	 * @param label
	 * @param team
	 */
	private void setupSpreadData(String spread, Element label, SiteTeamPackage team) {
		LOGGER.info("Entering setupSpreadData()");

		// First get the input field information
		final Map<String, String> hashMap = parseInputField(label.select("input"), 0);
		if (hashMap != null && !hashMap.isEmpty()) {
			team.setGameSpreadInputId(hashMap.get("id"));
			team.setGameSpreadInputName(hashMap.get("name"));
			team.setGameSpreadInputValue(hashMap.get("value"));
		}

		// Setup spread
		team = (SiteTeamPackage)parseSpreadData(reformatValues(spread), 0, " ", null, team);
		LOGGER.info("Exiting setupSpreadData()");
	}

	/**
	 * 
	 * @param i
	 * @return
	 */
	private String determineIndicator(Element i) {
		LOGGER.info("Entering determineIndicator()");
		String class1 = i.attr("class");

		if (class1 != null && class1.contains("minus")) {
			class1 = "-";
		} else {
			class1 = "+";
		}

		LOGGER.info("Exiting determineIndicator()");
		return class1;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseEventSelection(java.lang.String, com.ticketadvantage.services.dao.sites.SiteTeamPackage, java.lang.String)
	 */
	@Override
	public Map<String, String> parseEventSelection(String xhtml, SiteTeamPackage siteTeamPackage, String type)
			throws BatchException {
		LOGGER.info("Entering parseEventSelection()");
		final Map<String, String> map = new HashMap<String, String>();

		// Check for a wager limit and change it 
		if (xhtml.contains("but this line has just changed to:")) {
			// Line changed throw exception
			throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed and cannot complete transaction", xhtml);			
		}

		// Parse the HTML first
		final Document doc = parseXhtml(xhtml);

		final String[] types = new String[] { "hidden", "text" };
		Element form = doc.getElementById("aspnetForm");
		if (form != null) {
			// Get form action field
			map.put("action", form.attr("action"));
			getAllElementsByTypeWithCheckbox(form, "input", types, map);
		}

		// Now get the select options if there are any
		if (type.equals("spread")) {
			setupSpreadWager(doc, siteTeamPackage);
		} else if (type.equals("total")) {
			setupTotalWager(doc, siteTeamPackage);
		} else {
			setupMlWager(doc, siteTeamPackage);
		}

		// Get the wager select
		final Elements selects = doc.select(".teamsNameRotConB select");
		LOGGER.debug("selects: " + selects);
		if (selects != null && selects.size() == 3) {
			parseSelectFieldByNumBlank(selects, 1, map);
		} else if (selects != null && selects.size() == 2) {
			parseSelectFieldByNumBlank(selects, 0, map);
		}

		// Get the pitcher select (if any)
		final Elements pitchers = doc.select(".gPitch select");
		LOGGER.debug("pitchers: " + pitchers);
		if (pitchers != null && pitchers.size() == 2) {
			parseSelectFieldByNumBlank(pitchers, 1, map);
		} else if (pitchers != null && pitchers.size() == 1) {
			parseSelectFieldByNumBlank(pitchers, 0, map);
		}

		LOGGER.info("Exiting parseEventSelection()");
		return map;
	}

	/**
	 * 
	 * @param doc
	 * @param teamPackage
	 */
	protected void setupSpreadWager(Document doc, SiteTeamPackage teamPackage) {
		LOGGER.info("Entering setupSpreadWager()");

		final Map<String, String> hashMap = parseSelectField(doc.select(".teamsNameRotCon select"));
		if (hashMap != null && hashMap.size() > 0) {
			teamPackage.setGameSpreadSelectName(hashMap.get("name"));
		}

		final Elements options = doc.select(".teamsNameRotCon select option");
		if (options != null && options.size() > 0) {
			LOGGER.debug("options.size(): " + options.size());
			Map<String, SelectOptionData> optionsMap = parseSelectOptionField(options);
			for (int x = 0; x < optionsMap.size(); x++) {
				final SelectOptionData option = optionsMap.get(Integer.toString(x));
				if (option != null && option.getData() != null && option.getData().length() > 0) {
					String optionData = option.getData();
					optionData = optionData.trim();
					optionData = optionData.replaceAll("â²", "");
					optionData = optionData.replaceAll("Â", "");
					LOGGER.debug("optionData: " + optionData);
					LOGGER.debug("option.getValue(): " + option.getValue());
					teamPackage.addGameSpreadOptionValue(Integer.toString(x), option.getValue());

					// -2½ +200; Now parse the data
					teamPackage = (SiteTeamPackage)parseSpreadData(reformatValues(optionData), x, " ", null, teamPackage);
				}
			}
		} else {
			setupSpreadWagerInfo(doc, teamPackage);
		}

		LOGGER.info("Exiting setupSpreadWager()");
	}

	/**
	 * 
	 * @param doc
	 * @param siteTeamPackage
	 */
	private void setupSpreadWagerInfo(Document doc, SiteTeamPackage siteTeamPackage) {
		LOGGER.info("Entering setupSpreadWagerInfo()");

		// <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 teamsNameRotCon p5">
		//   [52] NEW YORK RANGERS
		//   <span class="pull-right lineSpan">
		//     <i class="glyphicon glyphicon-minus" aria-hidden="true"></i>
		//     5
		//     <i class="glyphicon glyphicon-minus" aria-hidden="true"></i>
		//     110
		//   </span>
		//   <input id="bp_3_1757496" name="bp_3_1757496" value="0" type="hidden">
		// </div>

		final Elements divs = doc.select(".teamsNameRotCon");
		if (divs != null) {
			for (Element divEle : divs) {
				Elements spans = divEle.select("span");
				if (spans != null && spans.size() > 0) {
					Element span = spans.get(0);
					String html = span.html();
					LOGGER.debug("html: " + html);
					Elements is = span.select("i");
					if (is != null && is.size() == 2) {
						// -1 -110
						String class1 = determineIndicator(is.get(0));
						String class2 = determineIndicator(is.get(1));

						int bindex = html.indexOf("</i>");
						if (bindex != -1) {
							html = html.substring(bindex + 4);
							int eindex = html.indexOf("<i");
							if (eindex != -1) {
								final String line = html.substring(0, eindex).trim();
								html = html.substring(eindex);
								bindex = html.indexOf("</i>");
	
								if (bindex != -1) {
									final String juice = html.substring(bindex + 4).trim();
									final String spread = class1 + line + " " + class2 + juice;
		
									setupSpreadData(spread, span, siteTeamPackage);
								}
							}
						}
					} else if (is != null && is.size() == 1) {
						// PK -110 or -3EV or -3 EV
						html = html.trim();

						if (html.startsWith("PK")) {
							// PK -110
							//     <label style="margin-bottom: 0;">
							//      PK
							//      <i class="glyphicon glyphicon-minus" aria-hidden="true"></i>
							//      110
							//      <input id="1_1751773_S_MIAMI OHIO_-3_-110_CFB_2_326" class="checkBox" name="game" type="checkbox">
							//     </label>
							int bindex = html.indexOf("</i>");

							if (bindex != -1) {
								String lineplusminus = "+";
								String line = "0";
								String juiceplusminus = determineIndicator(is.get(0));
								final String juice = html.substring(bindex + 4).trim();
								final String spread = lineplusminus + line + " " + juiceplusminus + juice;
	
								setupSpreadData(spread, span, siteTeamPackage);
							}
						} else {
							// -3 EV or
							//     <label style="margin-bottom: 0;">
							//      <i class="glyphicon glyphicon-minus" aria-hidden="true"></i>
							//      3EV
							//      <input id="1_1751773_S_MIAMI OHIO_-3_-110_CFB_2_326" class="checkBox" name="game" type="checkbox">
							//     </label>
							int bindex = html.indexOf("</i>");
							if (bindex != -1) {
								html = html.substring(bindex + 4).trim();
								html = html.replace(" EV", " +100");
								html = html.replace("EV", " +100");
								final String lineplusminus = determineIndicator(is.get(0));
								final String spread = lineplusminus + html;

								setupSpreadData(spread, span, siteTeamPackage);
							}
						}
					} else {
						// PKEV
						// <input id="5_1751768_ML_NORTHWESTERN_0_100_CFB_2_316" class="checkBox" name="game" type="checkbox">
						final String spread = "+0 +100";
						setupSpreadData(spread, span, siteTeamPackage);
					}
				}
			}
		}

		LOGGER.info("Exiting setupSpreadWagerInfo()");
	}

	/**
	 * 
	 * @param doc
	 * @param teamPackage
	 */
	protected void setupTotalWager(Document doc, SiteTeamPackage teamPackage) {
		LOGGER.info("Entering setupTotalWager()");
		
		final Map<String, String> hashMap = parseSelectField(doc.select(".teamsNameRotCon select"));
		if (hashMap != null && hashMap.size() > 0) {
			teamPackage.setGameTotalSelectName(hashMap.get("name"));
		}

		final Elements options = doc.select(".teamsNameRotCon select option");
		if (options != null && options.size() > 0) {
			LOGGER.debug("options.size(): " + options.size());
			Map<String, SelectOptionData> optionsMap = parseSelectOptionField(options);
			for (int x = 0; x < optionsMap.size(); x++) {
				final SelectOptionData option = optionsMap.get(Integer.toString(x));
				if (option != null && option.getData() != null && option.getData().length() > 0) {
					String optionData = option.getData();
					optionData = optionData.trim();
					optionData = optionData.replaceAll("â²", "");
					optionData = optionData.replaceAll("Â", "");
					optionData = optionData.replaceAll("ov", "");
					optionData = optionData.replaceAll("un", "");
					optionData = optionData.replaceAll("▲", "");
					optionData = optionData.replaceAll("▼", "");
					LOGGER.debug("optionData: " + optionData);
					LOGGER.debug("option.getValue(): " + option.getValue());
					teamPackage.addGameTotalOptionValue(Integer.toString(x), option.getValue());

					// -2½ +200; Now parse the data
					teamPackage = (SiteTeamPackage)parseTotalData(reformatValues(optionData), x, " ", null, teamPackage);
				}
			}
		} else {
			setupTotalWagerInfo(doc, teamPackage);
		}

		LOGGER.info("Exiting setupTotalWager()");
	}

	/**
	 * 
	 * @param doc
	 * @param teamPackage
	 */
	private void setupTotalWagerInfo(Document doc, SiteTeamPackage teamPackage) {
		LOGGER.info("Entering setupTotalWagerInfo()");

		// <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 teamsNameRotCon p5">
		//   [52] NEW YORK RANGERS
		//   <span class="pull-right lineSpan">
		//     u5½
		//     <i class="glyphicon glyphicon-minus" aria-hidden="true"></i>
		//     110
		//   </span>
		//   <input id="bp_3_1757496" name="bp_3_1757496" value="0" type="hidden">
		// </div>

		final Elements divs = doc.select(".teamsNameRotCon");
		if (divs != null) {
			for (Element divEle : divs) {
				Elements spans = divEle.select("span");
				if (spans != null && spans.size() > 0) {
					Element span = spans.get(0);
					String html = span.html();
					Elements is = span.select("i");
					if (is != null && is.size() == 1) {
						int index = html.indexOf("<i");
						if (index != -1) {
							String total = html.substring(0, index);
							total = total.replace("Ov", "o");
							total = total.replace("Un", "u");
							total = total.trim();
							total = total.replace(" ", "");
							String juiceindicator = determineIndicator(is.get(0));
							
							int bindex = html.indexOf("</i>");
							if (bindex != -1) {
								String juice = html.substring(bindex + 4).trim();
								total = total + " " + juiceindicator + juice;
								setupTotalData(divEle, total, teamPackage);
							}
						}
					} else {
						int index = html.indexOf("EV");
						if (index != -1) {
							String total = html.substring(0, index);
							total = total + " +100";
							setupTotalData(divEle, total, teamPackage);
						}
					}
				}
			}
		}

		LOGGER.info("Exiting setupTotalWagerInfo()");
	}

	/**
	 * 
	 * @param doc
	 * @param teamPackage
	 */
	private void setupMlWager(Document doc, SiteTeamPackage teamPackage) {
		LOGGER.info("Entering setupMlWager()");

		// <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 teamsNameRotCon p5">
		//   [452] CLE BROWNS
		//   <span class="pull-right lineSpan">
		//     <i class="glyphicon glyphicon-plus" aria-hidden="true"></i>
		//     199
		//   </span>
		//   <input id="bp_5_1751821" name="bp_5_1751821" value="0" type="hidden">
		// </div>

		final Elements divs = doc.select(".teamsNameRotCon");
		if (divs != null) {
			for (Element divEle : divs) {
				Elements spans = divEle.select("span");
				if (spans != null && spans.size() > 0) {
					Element span = spans.get(0);
					String html = span.html();
					Elements is = span.select("i");
					if (is != null && is.size() == 1) {
						String ml = null;
						String juiceindicator = determineIndicator(is.get(0));

						int bindex = html.indexOf("</i>");
						if (bindex != -1) {
							ml = html.substring(bindex + 4).trim();
							ml = juiceindicator + ml;
							setupMlData(divEle, ml, teamPackage);
						}
					} else {
						String ml = "+100";
						setupMlData(divEle, ml, teamPackage);
					}
				}
			}
		}

		LOGGER.info("Exiting setupMlWager()");
	}
}