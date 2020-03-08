/**
 * 
 */
package com.ticketadvantage.services.dao.sites.betbuckeye;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

import com.ticketadvantage.services.dao.sites.SiteParser;
import com.ticketadvantage.services.dao.sites.SiteTeamPackage;
import com.ticketadvantage.services.email.SendText;
import com.ticketadvantage.services.email.ticketadvantage.TicketAdvantageGmailOath;
import com.ticketadvantage.services.errorhandling.AppErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.PendingEvent;

/**
 * @author jmiller
 *
 */
public class BetBuckeyeParser extends SiteParser {
	private final static Logger LOGGER = Logger.getLogger(BetBuckeyeParser.class);
	private final TicketAdvantageGmailOath TICKETADVANTAGEGMAILOATH = new TicketAdvantageGmailOath();

	// Thu 9/8  08:30PM
	private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("E MM/dd YYYY hh:mma");
	// 10/8/2017 4:05 PM EDT
	protected static final ThreadLocal<SimpleDateFormat> PENDING_DATE_FORMAT = new ThreadLocal<SimpleDateFormat>(){
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("MM/dd/yyyy hh:mm a z");
		}
	};

	/**
	 * Constructor
	 */
	public BetBuckeyeParser() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			BetBuckeyeParser betBuckeyeParser = new BetBuckeyeParser();
			betBuckeyeParser.setTimezone("ET");
			String xhtml = "<!DOCTYPE HTML PUBLIC \"-/W3C/DTD XHTML 1.0 Transitional/EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">	<html>    <HEAD>	<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">	<meta name=\"description\" content=\"Bet with Us\">        <!--<meta name=\"google-translate-customization\" content=\"850af3aab9df52c0-1111978e695f2729-g87703a3f0fd07b91-d\"></meta>-->	<link rel=\"stylesheet\" type=\"text/css\" href=\"/Qubic/images/themes/theme1/theme1.css?v=86\">    <link rel=\"stylesheet\" type=\"text/css\" href=\"/Qubic/qubic/scripts/coverflow/coverflow.css?v=86\">        	<link rel=\"stylesheet\" type=\"text/css\" href=\"/Qubic/css/commun.css?version=86\">  		<!--<link rel=\"stylesheet/less\" type=\"text/css\" href=\"http://sportswidgets.pro/app/app.less?v=2&version=86\" />-->    <link rel=\"stylesheet\" type=\"text/css\" href=\"/Qubic/qubic/scripts/tooltipster/tooltipster.css?version=86\" />	<script type=\"text/javascript\" src=\"/Qubic/qubic/scripts/fancybox/jquery-1.4.2.min.js?version=86\"></script>	<script type=\"text/javascript\" src=\"/Qubic/qubic/scripts/fancybox/jquery.mousewheel-3.0.4.pack.js?version=86\"></script>    <script type=\"text/javascript\" src=\"/Qubic/qubic/scripts/fancybox/jquery.fancybox-1.3.4.js?version=86\"></script>    <script type=\"text/javascript\" src=\"/Qubic/qubic/scripts/autosugest/autosuggest.js?version=86\"></script>    <script type=\"text/javascript\" src=\"/Qubic/qubic/scripts/autosugest/autosuggestinit.js?version=86\"></script>    <script type=\"text/javascript\" src=\"/Qubic/scripts/CommonJavaScript.js?version=86\"></script>    <script type=\"text/javascript\" src=\"/Qubic/qubic/scripts/tooltipster/jquery.tooltipster.js?version=86\"></script>    <script type=\"text/javascript\" src=\"/Qubic/qubic/scripts/ticker/facescroll.js?version=86\"></script>    <script type=\"text/javascript\" src=\"/Qubic/qubic/scripts/ticker/ticker.js?version=86\"></script>    <script type=\"text/javascript\" src=\"/Qubic/qubic/scripts/countdown/countdown.js?version=86\"></script>    <script type=\"text/javascript\" src=\"/Qubic/qubic/scripts/ticker/jquery-ui.min.js?version=86\"></script>	<link rel=\"stylesheet\" type=\"text/css\" href=\"/Qubic/qubic/scripts/fancybox/jquery.fancybox-1.3.4.css?version=86\" media=\"screen\" />	<title>Z5059 | Home</title>	<script type=\"text/javascript\">		function loadSettings(){			$.fancybox.showActivity();			$.ajax({				type	: \"GET\",				cache	: false,				url		: \"PlayerSettings.php\",				data	: \"ip=1\",				success: function(data) {					$.fancybox(data);				}			});			return false;		}		function loadNewFeatures(){			$.fancybox.showActivity();			$.ajax({				type	: \"GET\",				cache	: false,				url		: \"Newfeatures.php\",				data	: \"ip=1\",				success: function(data) {					$.fancybox(data);				}			});			return false;		}	</script>    <!--[if IE]>		<style>           	#trends .inner{			width:567px !important;			overflow:hidden !important;			zoom:1 !important;			position:relative !important;			margin-left:284px !important;			top:0px !important;			}            .tooltipster-default {				border-radius: 5px;				border: 1px solid #362D2F;				background: transparent #000;				color: #fff;				width:380px;				height:90px;				background-color:#000;				opacity:0.8;				filter:alpha(opacity=80); /* For IE8 and earlier */			}         </style>	<![endif]-->    <!--[if lte IE 6]>    <script src=\"/Qubic/qubic/scripts/pngfixie6/DD_belatedPNG_0.0.8a-min.js\"></script>    <![endif]-->		 <script type=\"text/javascript\">	$(document).ready(function() {  		var page={};         		$(function() {			new FrontPage().init();		});				$(function() {			$('.tooltip').tooltipster({				animation: 'grow',				position: 'bottom'			});			$('.limitstooltip').tooltipster({				animation: 'grow',				position: 'top'			});		});			    $('.inner2').vTicker({			speed: 500,			pause: 3000,			showItems: 3,			animation: 'fade',			mousePause: true,			height: 0,			direction: 'up'		});		page.trendDescriptions = {};		loadTrendDescriptions();			   var badBrowser = (/MSIE ((5\\.5)|6)/.test(navigator.userAgent) && navigator.platform == \"Win32\");	   if (badBrowser) {		   DD_belatedPNG.fix('img');			$('body').supersleight();	   }	   hideTickerFast()	});	</script>	<link href=\"css/mini.css?v=1\" rel=\"stylesheet\" type=\"text/css\"><script type=\"text/javascript\">			function loadTicketdata(ticketNumber, wagerNumber, gradeNumber, booleans){				$.fancybox.showActivity();				$.ajax({					type	: \"GET\",					cache	: false,					url		: \"TicketShowTicket.php\",					data	: \"ticketNumber=\"+ticketNumber+\"&wagerNumber=\"+wagerNumber+\"&gradeNumber=\"+gradeNumber+\"&booleans=\"+booleans,					success: function(data) {						$.fancybox(data);						//setTimeout(\"hideFancy()\",1500);					}				});				return false;			}		</script></HEAD><BODY onScroll=\"if (typeof balance != 'undefined' )scrollAdj(balance)\"> 		<div style=\"position:relative; top:0; padding:3px; width:987px; margin:auto auto; text-align:center;\" align=\"center\">		        <div id=\"newfeatures\" align=\"center\">New Features <a id=\"newfeaureslink\" style=\"color:#FD1342; cursor:pointer\" onclick=\"loadNewFeatures()\">(click here)</a></div>        <div id=\"ticker_system\" class=\"fbChatSidebar\">			            <div id=\"vertical-ticker\" >                <table cellpadding=\"0\" cellspacing=\"0\"><tr valign=\"top\"><td  onclick=\"showTicker();\" style=\"cursor:pointer\">                <li>              	<div class='desc' style='width:300px; height:12px;'><div style='font-size:12px;font-size-adjust: 0.5; width:100%; overflow-x:hidden; overflow-y:auto' >No New Messages</div></div>                </li>                </td></tr>                <tr><td align=\"center\" style=\" background-color:#000; position:relative; cursor:pointer; background-image:url(/qubic/scripts/countdown/img/scroll.png); background-size: 30px 8px; height:8px; background-repeat:no-repeat; text-align:center; background-position:center \" onclick=\"showTicker();\"></td></tr>                </table>            </div>			         </div>         		 		 <div id=\"google_language_drop\">		    <div id=\"google_translate_element\" class=\"-blank\">		   <select name=\"language\" onChange=\"javascript:window.location=window.location.href.split('?')[0]+'?lang=' + this.value\">		    <option value=\"English\"selected >English</option><option value=\"Spanish\" >Spanish</option><option value=\"Chinese (Simp)\" >Chinese (Simp)</option><option value=\"French\" >French</option><option value=\"Korean\" >Korean</option><option value=\"Japanese\" >Japanese</option><option value=\"Vietnamese\" >Vietnamese</option>		   </select>		   </div>		 </div>				 		 <!--myluckybuck block-->		 			 <!--end myluckybuck block-->     </div>     <!-- End TranslateThis Button -->    <script type=\"text/javascript\">	var estadoSlinkS=1;	function newFeaturesLinkS(){		if(estadoSlinkS==1){			document.getElementById(\"newfeaureslink\").style.color=\"#fff\";			estadoSlinkS=0;		}		else{			estadoSlinkS=1;			document.getElementById(\"newfeaureslink\").style.color=\"#FD1342\";		}	}	setInterval(\"newFeaturesLinkS()\",1000);    </script>            <div id=\"wrapper\" class=\"fix-wrapper\">	      	<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" >			<tr>				<td id=\"clientName\" nowrap=\"nowrap\" style=\"text-align:center\" >&nbsp;<um><strong>Account:</strong></um>&nbsp;<strong>Z5059</strong>                </td>				                    		    <td nowrap=\"nowrap\" id=\"currentBalanceField\" style=\"text-align:center;\">&nbsp;<um><strong>Current Balance:</strong></um>&nbsp; <font color=red>USD&nbsp;-7,050</font>&nbsp;								</td>				                    		    <td nowrap=\"nowrap\" id=\"totalPendingField\" style=\"text-align:center\">&nbsp;<a href=\"SpecificPlayerPendingWagers.php\"><um><strong><font color=\"white\" size=\"2px\">Total Pending:</strong></um>&nbsp; USD&nbsp;42,500.00&nbsp;</font></a>				</td>                    	    	<td nowrap=\"nowrap\" id=\"currentAvailableField\" style=\"border-left:1px solid #fff; text-align:center\">                	        		<strong>&nbsp;<um>Current Available:</um>&nbsp;</strong>                    	<span style=\"color:#00ADEE\">                        	USD&nbsp;20,450.0 &nbsp;                        </span>				</td>				    		    <td nowrap=\"nowrap\" id=\"logoutField\" style=\"text-align:center\">        			<Strong>                    	&nbsp;                    	<a href=\"LogoutSucessful.php\" style=\"color:red; font-size:16px;\">Log Out</a>                        &nbsp;                    </Strong>				</td>							</tr>		</table>		        <div class=\"pages\" style=\"margin-left:auto; margin-right:auto\">        	<table id=\"table_menu\" width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-size:14px\">            	<tr height=\"26px\" >                <!--Straight cases-->                  	                    <!--Straight activo-->					<td class=\"menuSelection\" width=\"180px\" style=\"background-image:url(images/images_topbar/main-bar-blue-1px.png); background-repeat: repeat-x; cursor:pointer\" onmouseover=\"arrow0.style.backgroundPosition='0 -52px'\" onmouseout=\"arrow0.style.backgroundPosition='0 -52px'\"><A href=\"StraightSportSelection.php\" style=\"line-height: 26px;\"><center>Straight bets</center></a></td>                    <td width=\"17px\" style=\"background-image:url(images/images_topbar/main-bar-arrows.png); background-repeat:no-repeat; background-position:0 -52px;\" id=\"arrow0\">&nbsp;</td>                                    	<!--Teaser cases-->                                         <!--Straight active && teaser over-->                    <td class=\"menuSelection\" width=\"180px\" style=\"background-image:url(images/images_topbar/main-bar-black-1px.png); background-repeat: repeat-x; cursor:pointer\" onmouseover=\"arrow1.style.backgroundPosition='0 -52px'; arrow0.style.backgroundPosition='0 -182px'\" onmouseout=\"arrow1.style.backgroundPosition='0 0'; arrow0.style.backgroundPosition='0 -52px'\" ><A href=\"TeaserTeaserSelect.php\" style=\"line-height: 26px;\"><center>Teasers</center></a></td>					<td width=\"14px\" style=\"background-image:url(images/images_topbar/main-bar-arrows.png); background-repeat:no-repeat; background-position:0 0\" id=\"arrow1\">&nbsp;&nbsp;</td>                                         <!--Parlay cases-->                                         <!--parlay no active && teaser no active-->                    <td class=\"menuSelection\" width=\"180px\" style=\"background-image:url(images/images_topbar/main-bar-black-1px.png); background-repeat: repeat-x; cursor:pointer\" onmouseover=\"arrow2.style.backgroundPosition='0 -52px'; arrow1.style.backgroundPosition='0 -26px'\" onmouseout=\"arrow2.style.backgroundPosition='0 0'; arrow1.style.backgroundPosition='0 0'\"><A href=\"StraightSportSelection.php?wagerType=Parlay\" style=\"line-height: 26px;\"><center>Parlays/RR</center></a></td>                    <td width=\"14px\" style=\"background-image:url(images/images_topbar/main-bar-arrows.png); background-repeat:no-repeat; background-position:0 0\" id=\"arrow2\">&nbsp;&nbsp;</td>					                	<!--If bets cases-->                                        <td class=\"menuSelection\" width=\"180px\" style=\"background-image:url(images/images_topbar/main-bar-black-1px.png); background-repeat: repeat-x; cursor:pointer\" onmouseover=\"arrow3.style.backgroundPosition='0 -52px'; arrow2.style.backgroundPosition='0 -26px'\" onmouseout=\"arrow3.style.backgroundPosition='0 0'; arrow2.style.backgroundPosition='0 0'\"><A href=\"IfbetsSportSelection.php\" style=\"line-height: 26px;\"><center>If Bets</center></a></td>                    					<td width=\"14px\" style=\"background-image:url(images/images_topbar/main-bar-arrows.png); background-repeat:no-repeat; background-position:0 0\" id=\"arrow3\">&nbsp;&nbsp;</td>												<!--Action reverse cases-->														 <!--if bet active action reverse over-->							 							<!--Turbo props cases-->														<td class=\"menuSelection\" id=\"navcasino\" width=\"250px\" style=\"background-image:url(images/images_topbar/main-bar-red-1px.png); background-repeat: repeat-x; cursor:pointer\" onmouseover=\"arrow5.style.backgroundPosition='0 -52px'; arrow4.style.backgroundPosition='0 -26px'\" onmouseout=\"arrow5.style.backgroundPosition='0 -78px'; arrow4.style.backgroundPosition='0 -104px'\">															  <A href=\"#\" style=\"line-height: 26px;\"><center  style=\"color:#AAAAAA;\"></center></a>							    							</td>							<!--<td width=\"14px\" style=\"background-image:url(images/images_topbar/main-bar-arrows.png); background-repeat:no-repeat; background-position:0 -78px\" id=\"arrow5\">&nbsp;&nbsp;</td>-->                            							<!--horses Cases-->														<td width=\"14px\" style=\"background-image:url(images/images_topbar/main-bar-arrows.png); background-repeat:no-repeat; background-position:0 -78px\" id=\"arrow5\">&nbsp;&nbsp;</td>														 <!--no props active && no casino active-->							<td class=\"menuSelection\" width=\"180px\" style=\"background-image:url(images/images_topbar/main-bar-black-1px.png); background-repeat: repeat-x; cursor:pointer\" onmouseover=\"arrow6.style.backgroundPosition='0 -52px'; arrow5.style.backgroundPosition='0 -130px'\" onmouseout=\"arrow6.style.backgroundPosition='0 0'; arrow5.style.backgroundPosition='0 -78px'\">							 <center style='color:#AAAAAA'></center>							</td>														<!--casino Cases-->														<td width=\"14px\" style=\"background-image:url(images/images_topbar/main-bar-arrows.png); background-repeat:no-repeat; background-position:0 0\" id=\"arrow6\">&nbsp;&nbsp;</td>							<!--<td class=\"menuSelection\" width=\"180px\" style=\"background-image:url(images/images_topbar/main-bar-black-1px.png); background-repeat: repeat-x; cursor:pointer\" onmouseover=\"arrow6.style.backgroundPosition='0 -26px'\" onmouseout=\"arrow6.style.backgroundPosition='0 0'\">-->														<td id=\"navcasino\" class=\"menuSelection\" width=\"180px\" style=\"background-image:url(images/images_topbar/main-bar-black-1px.png); background-repeat: repeat-x; cursor:pointer\" onmouseover=\"arrow6.style.backgroundPosition='0 -26px'\" onmouseout=\"arrow6.style.backgroundPosition='0 0'\">														 <A href=\"#\" style=\"line-height: 26px;\"><center  style=\"color:#AAAAAA;\"></center></a>							 							 </td>							 								                </tr>  			</table>            </div>			            <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\">                <tr>                    <td id=\"tdCategoriesContainer\">                        <div class=\"categories\" style=\"margin:3px auto 0 auto;\">                            <table width=\"928px\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-size:12px; background-image:url(images/images_topbar/options_fullbg.png); background-repeat:no-repeat;\">                                <tr>                                    <td id=\"categoriesHomeField\" style=\"text-align:center\"><a style=\"color:#fff\" href=\"StraightLoginSportSelection.php\"><center><div class=\"spriteHome\"><br></div>Home</center></a></td>                                    <td style=\"border-left:1px solid #fff;\"></td>                                    <td class=\"categoriesStandardFields\" style=\"text-align:center\" ><a style=\"color:#fff\" href=\"Scores.php\"><center><div class=\"spriteScores\"><br></div>Scores</center></a></td>                                     <td style=\"border-left:1px solid #fff;\"></td>                                    <!--<td class=\"categoriesStandardFields\" style=\"text-align:center\"><a style=\"color:#fff\" href=\"PlayerTransactions.php?transactionListDays=1\"><center><div class=\"spriteReports\"><br></div>Reports</center></a></td>-->                                    <td style=\"border-left:1px solid #fff;\"></td>									                                   		<td class=\"categoriesStandardFields\" style=\"text-align:center\"><a style=\"color:#fff\" href=\"SpecificPlayerPendingWagers.php\" ><center><div class=\"spritePending\" ><br></div><div id=\"pendingWagersNumberE\"><strong><center>7</center></strong></div><span style=\"padding:0 0 0 15px\">Pending Wagers </span></center></a></td>																			 <td style=\"border-left:1px solid #fff;\"></td>                                    <td class=\"categoriesStandardFields\" style=\"text-align:center\"><center><a style=\"color:#fff\" href=\"PlayerDailyFigure.php\"><div class=\"spriteDailyFigure\" ><br></div><span>Daily Figures</span></center></a></td>                                    <td style=\"border-left:1px solid #fff;\"></td>                                    <td class=\"categoriesStandardFields\" style=\"text-align:center\"><center><a style=\"color:#fff\" href=\"SpecificPlayerWeeklyFigure.php\"><div class=\"spriteWeeklyFigure\" ><br></div><span>Graded Wagers</span></center></a></td>                                     <td style=\"border-left:1px solid #fff;\"></td>                                    <td id=\"categoriesTransactionsField\" style=\"text-align:center\"><center><a style=\"color:#fff\" href=\"SpecificPlayerTransactions.php\"><div class=\"spriteTransaction\" ><br></div><span>Transactions</span></center></a></td>                                     <td style=\"border-left:1px solid #fff;\"></td>                                                                         <td class=\"categoriesStandardFields\" style=\"text-align:center; padding:0 0 0 0\"><center><a style=\"color:#fff\" href=\"PanelMessage.php\" ><div class=\"spriteMessages\" ><br></div><div id=\"messagesNumberE\" ><strong><center>                                     0                                    </center></strong></div><span>Messages</span></center></a></td>									 									 <td style=\"border-left:1px solid #fff;\"></td>									<td class=\"categoriesStandardFields\" style=\"text-align:center; padding:0 20px 0 0\">									    									  <center><a style=\"color:#fff\" href=\"PlayerPanic.php\"><div class=\"spriteIconBoss\" ><br></div><span>Boss</span></center></a>									  									</td>                                </tr>                            </table>                        </div>        	   		 </td>	        	</tr>                <tr>                	<td align=\"left\">                    	                        <div id=\"trends2\" class=\"container\" align=\"left\" >                         	<div id=\"scores_tool\" class=\"inner2\" style=\"height:31px!important\">                                <span class=\"trend-label2\">Result:</span>                                <span class=\"trend-label2\" style=\"position:relative; left:95px\">NEWS:</span>						      	<ul id=\"ul_scores\" class=\"trendscontent2\" style=\"zoom: 1;position:relative;top:0px\">                                	<li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/ncaa_default_logo.png' ></td><td align=center>Football            <br>0 | 1</td><td><img width=86px src='logos/ncaa_default_logo.png' ></td></tr><tr valign=top><td align=center>East Carolina score 1st</td><td></td><td align=center>Temple score 1st</td></tr></table>\">East  0 - 1 Temp</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/ncaa_default_logo.png' ></td><td align=center>Football            <br>1 | 0</td><td><img width=86px src='logos/ncaa_default_logo.png' ></td></tr><tr valign=top><td align=center>Yes, score 3 unanswered times</td><td></td><td align=center>No, score 3 unanswered times</td></tr></table>\">Yes,  1 - 0 No, </a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/ncaa_default_logo.png' ></td><td align=center>Football            <br>1 | 0</td><td><img width=86px src='logos/ncaa_default_logo.png' ></td></tr><tr valign=top><td align=center>Illinois score 1st</td><td></td><td align=center>Rutgers score 1st</td></tr></table>\">Illi  1 - 0 Rutg</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/ncaa_default_logo.png' ></td><td align=center>Football            <br>0 | 1</td><td><img width=86px src='logos/ncaa_default_logo.png' ></td></tr><tr valign=top><td align=center>Northwestern score 1st</td><td></td><td align=center>Michigan St score 1st</td></tr></table>\">Nort  0 - 1 Mich</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/ncaa_default_logo.png' ></td><td align=center>Football            <br>0 | 1</td><td><img width=86px src='logos/ncaa_default_logo.png' ></td></tr><tr valign=top><td align=center>TD 1st score</td><td></td><td align=center>Field Goal or Safety 1st score</td></tr></table>\">TD 1  0 - 1 Fiel</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/ncaa_default_logo.png' ></td><td align=center>Football            <br>1 | 0</td><td><img width=86px src='logos/ncaa_default_logo.png' ></td></tr><tr valign=top><td align=center>Oklahoma score 1st</td><td></td><td align=center>Texas score 1st</td></tr></table>\">Okla  1 - 0 Texa</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/ncaa_default_logo.png' ></td><td align=center>Football            <br>1 | 0</td><td><img width=86px src='logos/ncaa_default_logo.png' ></td></tr><tr valign=top><td align=center>TD 1st score</td><td></td><td align=center>Field Goal or Safety 1st score</td></tr></table>\">TD 1  1 - 0 Fiel</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/default.gif' ></td><td align=center>Football            <br>null | null</td><td><img width=86px src='logos/default.gif' ></td></tr><tr valign=top><td align=center>Maryland RSW</td><td></td><td align=center>Maryland RSW.</td></tr></table>\">Mary  null - null Mary</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/japanese_bl.png' ></td><td align=center>Baseball            <br>3 | 4</td><td><img width=86px src='logos/japanese_bl.png' ></td></tr><tr valign=top><td align=center>Yokohama Baystars</td><td></td><td align=center>Hanshin Tigers</td></tr></table>\">Yoko  3 - 4 Hans</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/japanese_bl.png' ></td><td align=center>Baseball            <br>1 | 8</td><td><img width=86px src='logos/japanese_bl.png' ></td></tr><tr valign=top><td align=center>Chiba Lotte Marines</td><td></td><td align=center>Rakuten Golden Eagles</td></tr></table>\">Chib  1 - 8 Raku</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/japanese_bl.png' ></td><td align=center>Baseball            <br>3 | 8</td><td><img width=86px src='logos/japanese_bl.png' ></td></tr><tr valign=top><td align=center>Seibu Lions</td><td></td><td align=center>Fukuoka S Hawks</td></tr></table>\">Seib  3 - 8 Fuku</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/korea_bl.png' ></td><td align=center>Baseball            <br>1 | 6</td><td><img width=86px src='logos/korea_bl.png' ></td></tr><tr valign=top><td align=center>Samsung Lions</td><td></td><td align=center>KT Wiz Suwon</td></tr></table>\">Sams  1 - 6 KT W</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/korea_bl.png' ></td><td align=center>Baseball            <br>3 | 1</td><td><img width=86px src='logos/korea_bl.png' ></td></tr><tr valign=top><td align=center>LG Twins</td><td></td><td align=center>Doosan Bears</td></tr></table>\">LG T  3 - 1 Doos</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/korea_bl.png' ></td><td align=center>Baseball            <br>5 | 7</td><td><img width=86px src='logos/korea_bl.png' ></td></tr><tr valign=top><td align=center>KIA Tigers</td><td></td><td align=center>SK Wyverns</td></tr></table>\">KIA   5 - 7 SK W</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/korea_bl.png' ></td><td align=center>Baseball            <br>5 | 8</td><td><img width=86px src='logos/korea_bl.png' ></td></tr><tr valign=top><td align=center>Hanwha Eagles</td><td></td><td align=center>Lotte Giants</td></tr></table>\">Hanw  5 - 8 Lott</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/korea_bl.png' ></td><td align=center>Baseball            <br>5 | 6</td><td><img width=86px src='logos/korea_bl.png' ></td></tr><tr valign=top><td align=center>Nexen Heroes</td><td></td><td align=center>NC Dinos</td></tr></table>\">Nexe  5 - 6 NC D</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/fiba_americas_logo.png' ></td><td align=center>Basketball          <br>101 | 82</td><td><img width=86px src='logos/fiba_americas_logo.png' ></td></tr><tr valign=top><td align=center>Kapfenberg Bulls</td><td></td><td align=center>Vienna Timberwolves</td></tr></table>\">Kapf  101 - 82 Vien</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/fiba_americas_logo.png' ></td><td align=center>Basketball          <br>66 | 92</td><td><img width=86px src='logos/fiba_americas_logo.png' ></td></tr><tr valign=top><td align=center>Yambol</td><td></td><td align=center>Academic</td></tr></table>\">Yamb  66 - 92 Acad</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/fiba_americas_logo.png' ></td><td align=center>Basketball          <br>78 | 88</td><td><img width=86px src='logos/fiba_americas_logo.png' ></td></tr><tr valign=top><td align=center>Spartak Pleven</td><td></td><td align=center>Lukoil Levski</td></tr></table>\">Spar  78 - 88 Luko</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/fiba_americas_logo.png' ></td><td align=center>Basketball          <br>61 | 86</td><td><img width=86px src='logos/fiba_americas_logo.png' ></td></tr><tr valign=top><td align=center>Cherno More</td><td></td><td align=center>Rilski Sportist</td></tr></table>\">Cher  61 - 86 Rils</a></li>                               	</ul>  							</div>  							<span class=\"fade fade-left2\">&nbsp;</span>						</div>                        <div id=\"trends\" align=\"left\">  							<div class=\"inner\">	 							<ul class=\"trendscontent\" style=\"zoom: 1;position:\">									 <li class=\"trend-label\">&nbsp;&nbsp;</li>                    				 <li>						            	<a href=\"#\" class=\"search_link\" name=\"\" rel=\"nofollow\">No news available...</a>						          	 </li>                                 </ul>  							</div>  							<span class=\"fade fade-left\">&nbsp;</span><span class=\"fade fade-right\">&nbsp;</span>						</div>                        <div style=\"width:100%\">                       		<div id=\"score_popup\" style=\"display:none;\">                            </div>                       </div>					   					</td>                </tr>        	</table>                      	<table cellspacing=\"0\" border=\"0\" width=\"100%\" align=\"left\">               <tr valign=\"top\">                    <td style=\" width:100%\" >                    	<div id=\"contentright\">                                                     <div class=\"postP\">                                                          <div  style=\" padding:3px; height:100%\"><div style='padding:3px' align='center'><input type=button class=button value=\"All Pending\" onClick=window.location.href=\"SpecificPlayerPendingWagers.php\" />&nbsp;<input type=button class=button value=\"Straights\" onClick=window.location.href=\"SpecificPlayerPendingWagers.php?wagertyperequest=S\" />&nbsp;<input type=button class=button value=\"Parlays\" onClick=window.location.href=\"SpecificPlayerPendingWagers.php?wagertyperequest=P\" />&nbsp;<input type=button class=button value=\"IfBets\" onClick=window.location.href=\"SpecificPlayerPendingWagers.php?wagertyperequest=I\" />&nbsp;<input type=button class=button value=\"Future/Props\" onClick=window.location.href=\"SpecificPlayerPendingProps.php\" />&nbsp;<input type=button class=button value=\"Teasers\" onClick=window.location.href=\"SpecificPlayerPendingWagers.php?wagertyperequest=T\" />&nbsp;<input type=button class=button value=\"Horses\" onClick=window.location.href=\"SpecificPlayerPendingWagers.php?wagertyperequest=G\" />&nbsp;<input type=button class=button value=\"Manual Plays\" onClick=window.location.href=\"SpecificPlayerPendingWagers.php?wagertyperequest=A\"/>&nbsp;</div><div align='center' style='background-color:#CCCCCC; padding:5px; border:1px solid #666666;width: 820px;margin-left: auto;margin-right: auto;'><div class='block-content' style='width:775px !important'><FONT style='margin-top:5px'>List of all <STRONG>Pending Wagers </STRONG>USD for&nbsp;<b>z5059</b></FONT>.</div></div><div align='center' style='background-color:#CCCCCC; padding:5px; border:1px solid #666666;'><table cellspacing=\"4\" cellpadding=\"4\" style=\"background-color:#000; color:#FFF\" ><tbody><tr><th align=\"left\">REPORT REFRESHES EVERY MINUTE</th></tr></tbody></table><form name=\"WagerListForm\" method=\"post\" action=\"PlayerWagerDetails.php\"><INPUT type=\"hidden\" id=ticketNumber name=ticketNumber value=0><INPUT type=\"hidden\" id=wagerNumber name=wagerNumber value=0><INPUT type=\"hidden\" id=gradeNumber name=gradeNumber value=0><TABLE class=listingtableQubic width=\"100%\" border=0 cellpadding=0 cellspacing=0><TH align=\"left\"  style=\"padding: 0 0 0 5px\">#</TH><TH align=\"left\" style=\"padding: 0 0 0 0px\">Accepted</TH><TH align=\"left\" style=\"padding: 0 0 0 8px\">Type</TH><TH align=\"left\">Description</TH><TH align=\"left\">Start Time (ET)</TH><TH align=\"right\">Risk</TH><TH align=\"right\"  style=\"padding: 0 5px 0 0\">To Win</TH><TR class=offering_nounQubic><TD NOWRAP>1&nbsp;&nbsp;<span class=\"grouped_elements\" onclick=\"loadTicketdata(267724001, 1, 0, true);\" style=\"background-color:#F5D247; border:1px solid #999999; cursor:pointer\">Ticket</span></TD><TD NOWRAP>10/3/2018<br>10:07 PM</TD><TD NOWRAP>&nbsp;&nbsp;Spread&nbsp;&nbsp;</TD><td style=\"text-align:left;\" NOWRAP><font style='color:#CC0000'>Football</font> #457 New York Giants +7 -110 for Game</td><TD ALIGN=LEFT NOWRAP><font size=\"2px\">10/7/2018 1:00 PM</font><br></td><TD ALIGN=RIGHT NOWRAP>&nbsp;&nbsp;<span style='color:#FF0000;'>$5,500.00</span>&nbsp;&nbsp;</TD><TD ALIGN=RIGHT NOWRAP>&nbsp;&nbsp;<span style='color:#000000;'>$5,000.00</span></TD><TR class=offering_pairQubic><TD NOWRAP>2&nbsp;&nbsp;<span class=\"grouped_elements\" onclick=\"loadTicketdata(267769399, 1, 0, true);\" style=\"background-color:#F5D247; border:1px solid #999999; cursor:pointer\">Ticket</span></TD><TD NOWRAP>10/4/2018<br>12:26 PM</TD><TD NOWRAP>&nbsp;&nbsp;Money Line&nbsp;&nbsp;</TD><td style=\"text-align:left;\" NOWRAP><font style='color:#CC0000'>Football</font> #455 Tennessee Titans -200 for Game</td><TD ALIGN=LEFT NOWRAP><font size=\"2px\">10/7/2018 1:00 PM</font><br></td><TD ALIGN=RIGHT NOWRAP>&nbsp;&nbsp;<span style='color:#FF0000;'>$10,000.00</span>&nbsp;&nbsp;</TD><TD ALIGN=RIGHT NOWRAP>&nbsp;&nbsp;<span style='color:#000000;'>$5,000.00</span></TD><TR class=offering_nounQubic><TD NOWRAP>3&nbsp;&nbsp;<span class=\"grouped_elements\" onclick=\"loadTicketdata(267769414, 1, 0, true);\" style=\"background-color:#F5D247; border:1px solid #999999; cursor:pointer\">Ticket</span></TD><TD NOWRAP>10/4/2018<br>12:26 PM</TD><TD NOWRAP>&nbsp;&nbsp;Spread&nbsp;&nbsp;</TD><td style=\"text-align:left;\" NOWRAP><font style='color:#CC0000'>Football</font> #455 Tennessee Titans -4 -110 for Game</td><TD ALIGN=LEFT NOWRAP><font size=\"2px\">10/7/2018 1:00 PM</font><br></td><TD ALIGN=RIGHT NOWRAP>&nbsp;&nbsp;<span style='color:#FF0000;'>$5,500.00</span>&nbsp;&nbsp;</TD><TD ALIGN=RIGHT NOWRAP>&nbsp;&nbsp;<span style='color:#000000;'>$5,000.00</span></TD><TR class=offering_pairQubic><TD NOWRAP>4&nbsp;&nbsp;<span class=\"grouped_elements\" onclick=\"loadTicketdata(267769429, 1, 0, true);\" style=\"background-color:#F5D247; border:1px solid #999999; cursor:pointer\">Ticket</span></TD><TD NOWRAP>10/4/2018<br>12:26 PM</TD><TD NOWRAP>&nbsp;&nbsp;Spread&nbsp;&nbsp;</TD><td style=\"text-align:left;\" NOWRAP><font style='color:#CC0000'>Football</font> #455 Tennessee Titans -3 -110 for 1st Half</td><TD ALIGN=LEFT NOWRAP><font size=\"2px\">10/7/2018 1:00 PM</font><br></td><TD ALIGN=RIGHT NOWRAP>&nbsp;&nbsp;<span style='color:#FF0000;'>$5,500.00</span>&nbsp;&nbsp;</TD><TD ALIGN=RIGHT NOWRAP>&nbsp;&nbsp;<span style='color:#000000;'>$5,000.00</span></TD><TR class=offering_nounQubic><TD NOWRAP>5&nbsp;&nbsp;<span class=\"grouped_elements\" onclick=\"loadTicketdata(267771036, 1, 0, true);\" style=\"background-color:#F5D247; border:1px solid #999999; cursor:pointer\">Ticket</span></TD><TD NOWRAP>10/4/2018<br>12:53 PM</TD><TD NOWRAP>&nbsp;&nbsp;Spread&nbsp;&nbsp;</TD><td style=\"text-align:left;\" NOWRAP><font style='color:#CC0000'>Football</font> #464 Detroit Lions +1Â½ -110 for Game</td><TD ALIGN=LEFT NOWRAP><font size=\"2px\">10/7/2018 1:00 PM</font><br></td><TD ALIGN=RIGHT NOWRAP>&nbsp;&nbsp;<span style='color:#FF0000;'>$5,500.00</span>&nbsp;&nbsp;</TD><TD ALIGN=RIGHT NOWRAP>&nbsp;&nbsp;<span style='color:#000000;'>$5,000.00</span></TD><TR class=offering_pairQubic><TD NOWRAP>6&nbsp;&nbsp;<span class=\"grouped_elements\" onclick=\"loadTicketdata(268145603, 1, 0, true);\" style=\"background-color:#F5D247; border:1px solid #999999; cursor:pointer\">Ticket</span></TD><TD NOWRAP>10/6/2018<br>12:09 PM</TD><TD NOWRAP>&nbsp;&nbsp;Spread&nbsp;&nbsp;</TD><td style=\"text-align:left;\" NOWRAP><font style='color:#CC0000'>Football</font> #469 Arizona Cardinals +3Â½ -110 for Game</td><TD ALIGN=LEFT NOWRAP><font size=\"2px\">10/7/2018 4:25 PM</font><br></td><TD ALIGN=RIGHT NOWRAP>&nbsp;&nbsp;<span style='color:#FF0000;'>$5,500.00</span>&nbsp;&nbsp;</TD><TD ALIGN=RIGHT NOWRAP>&nbsp;&nbsp;<span style='color:#000000;'>$5,000.00</span></TD><TR class=offering_nounQubic><TD NOWRAP>7&nbsp;&nbsp;<span class=\"grouped_elements\" onclick=\"loadTicketdata(268145657, 1, 0, true);\" style=\"background-color:#F5D247; border:1px solid #999999; cursor:pointer\">Ticket</span></TD><TD NOWRAP>10/6/2018<br>12:10 PM</TD><TD NOWRAP>&nbsp;&nbsp;Money Line&nbsp;&nbsp;</TD><td style=\"text-align:left;\" NOWRAP><font style='color:#CC0000'>Football</font> #469 Arizona Cardinals +160 for Game</td><TD ALIGN=LEFT NOWRAP><font size=\"2px\">10/7/2018 4:25 PM</font><br></td><TD ALIGN=RIGHT NOWRAP>&nbsp;&nbsp;<span style='color:#FF0000;'>$5,000.00</span>&nbsp;&nbsp;</TD><TD ALIGN=RIGHT NOWRAP>&nbsp;&nbsp;<span style='color:#000000;'>$8,000.00</span></TD><tr><td colspan=5>This page has <strong>7</strong> wagers</td><td><span style='color:#FF0000;'>$42,500.00</span></td><td><span style='color:#000000;'>$38,000.00</span></td></TABLE></form></div></div></div>	</div>                            </div>                        </div>                    </td>               </tr>        	</table>        </div>		        </div>		        <div id=\"new_footer\">    	<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"float:left;background-image:url(/Qubic/images/images_topbar/images_content/bg_footer.png); background-repeat:no-repeat; border-bottom:2px solid #D6D6D6;\" >        	<tr valign=\"bottom\">            	<td valign=\"top\" style=\"float:right; padding:10px 0 0 0\">                                        </td>                <td style=\" padding:2px 0 0 70px;\">				                 	<img src=\"images/images_topbar/images_content/settings_icon.png\"  style=\"cursor:pointer; \" onClick=\"javascript:loadSettings();\" class=\"settings_icon\">				                 </td>                <td  valign=\"middle\" >                	<img src=\"images/images_topbar/images_content/separador.png\" height=\"20px\" style=\"margin:0 30px 0 0\">                </td>				                	<td  style=\" padding:2px 0 0 0;\">                		<a href=\"Rules.php\"><img src=\"images/images_topbar/images_content/rules_icon.png\" class=\"rules_icon\"></a>                	</td>				                 <td  valign=\"middle\" >                	<img src=\"images/images_topbar/images_content/separador.png\" height=\"20px\" style=\"margin:0 30px 0 30px\">                </td>                <td style=\" padding:5px 0 0 0;\">                                 </td>                <td align=\"center\" style=\" padding:0 20px 0 0;\" valign=\"middle\" class=\"copyright\">                	 All Rights Reserved.<br />Copyright 2018.                </td>            </tr>        </table>         <div style=\"height:15px\">&nbsp;</div>    </div>		</body>    </html>	 ";
			Set<PendingEvent> pendingEvents = betBuckeyeParser.parsePendingBets(xhtml, "Z5059", "BetBuckeye");
			for (PendingEvent pe : pendingEvents) {
				LOGGER.error("pe: " + pe);
			}

/*
			String html = " Oklahoma City Thunder/Minnesota Timberwolves under 218 -110 for Game";
			PendingEvent pe = new PendingEvent();
			pe.setEventtype("total");
			betBuckeyeParser.getEventInfoForGame(html, pe);
			LOGGER.debug("pe: " + pe);
*/
//			String xhtml = "<!DOCTYPE HTML PUBLIC \"-/W3C/DTD XHTML 1.0 Transitional/EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">	<html>    <HEAD>	<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">	<meta name=\"description\" content=\"Bet with Us\">        <!--<meta name=\"google-translate-customization\" content=\"850af3aab9df52c0-1111978e695f2729-g87703a3f0fd07b91-d\"></meta>-->	<link rel=\"stylesheet\" type=\"text/css\" href=\"/Qubic/images/themes/theme1/theme1.css?v=86\">    <link rel=\"stylesheet\" type=\"text/css\" href=\"/Qubic/qubic/scripts/coverflow/coverflow.css?v=86\">        	<link rel=\"stylesheet\" type=\"text/css\" href=\"/Qubic/css/commun.css?version=86\">  		<!--<link rel=\"stylesheet/less\" type=\"text/css\" href=\"http://sportswidgets.pro/app/app.less?v=2&version=86\" />-->    <link rel=\"stylesheet\" type=\"text/css\" href=\"/Qubic/qubic/scripts/tooltipster/tooltipster.css?version=86\" />	<script type=\"text/javascript\" src=\"/Qubic/qubic/scripts/fancybox/jquery-1.4.2.min.js?version=86\"></script>	<script type=\"text/javascript\" src=\"/Qubic/qubic/scripts/fancybox/jquery.mousewheel-3.0.4.pack.js?version=86\"></script>    <script type=\"text/javascript\" src=\"/Qubic/qubic/scripts/fancybox/jquery.fancybox-1.3.4.js?version=86\"></script>    <script type=\"text/javascript\" src=\"/Qubic/qubic/scripts/autosugest/autosuggest.js?version=86\"></script>    <script type=\"text/javascript\" src=\"/Qubic/qubic/scripts/autosugest/autosuggestinit.js?version=86\"></script>    <script type=\"text/javascript\" src=\"/Qubic/scripts/CommonJavaScript.js?version=86\"></script>    <script type=\"text/javascript\" src=\"/Qubic/qubic/scripts/tooltipster/jquery.tooltipster.js?version=86\"></script>    <script type=\"text/javascript\" src=\"/Qubic/qubic/scripts/ticker/facescroll.js?version=86\"></script>    <script type=\"text/javascript\" src=\"/Qubic/qubic/scripts/ticker/ticker.js?version=86\"></script>    <script type=\"text/javascript\" src=\"/Qubic/qubic/scripts/countdown/countdown.js?version=86\"></script>    <script type=\"text/javascript\" src=\"/Qubic/qubic/scripts/ticker/jquery-ui.min.js?version=86\"></script>	<link rel=\"stylesheet\" type=\"text/css\" href=\"/Qubic/qubic/scripts/fancybox/jquery.fancybox-1.3.4.css?version=86\" media=\"screen\" />	<title>GA9002 | Home</title>	<script type=\"text/javascript\">		function loadSettings(){			$.fancybox.showActivity();			$.ajax({				type	: \"GET\",				cache	: false,				url		: \"PlayerSettings.php\",				data	: \"ip=1\",				success: function(data) {					$.fancybox(data);				}			});			return false;		}		function loadNewFeatures(){			$.fancybox.showActivity();			$.ajax({				type	: \"GET\",				cache	: false,				url		: \"Newfeatures.php\",				data	: \"ip=1\",				success: function(data) {					$.fancybox(data);				}			});			return false;		}	</script>    <!--[if IE]>		<style>           	#trends .inner{			width:567px !important;			overflow:hidden !important;			zoom:1 !important;			position:relative !important;			margin-left:284px !important;			top:0px !important;			}            .tooltipster-default {				border-radius: 5px;				border: 1px solid #362D2F;				background: transparent #000;				color: #fff;				width:380px;				height:90px;				background-color:#000;				opacity:0.8;				filter:alpha(opacity=80); /* For IE8 and earlier */			}         </style>	<![endif]-->    <!--[if lte IE 6]>    <script src=\"/Qubic/qubic/scripts/pngfixie6/DD_belatedPNG_0.0.8a-min.js\"></script>    <![endif]-->		 <script type=\"text/javascript\">	$(document).ready(function() {  		var page={};         		$(function() {			new FrontPage().init();		});				$(function() {			$('.tooltip').tooltipster({				animation: 'grow',				position: 'bottom'			});			$('.limitstooltip').tooltipster({				animation: 'grow',				position: 'top'			});		});			    $('.inner2').vTicker({			speed: 500,			pause: 3000,			showItems: 3,			animation: 'fade',			mousePause: true,			height: 0,			direction: 'up'		});		page.trendDescriptions = {};		loadTrendDescriptions();			   var badBrowser = (/MSIE ((5\\.5)|6)/.test(navigator.userAgent) && navigator.platform == \"Win32\");	   if (badBrowser) {		   DD_belatedPNG.fix('img');			$('body').supersleight();	   }	   hideTickerFast()	});	</script>	<SCRIPT ID=clientEventHandlersJS LANGUAGE=javascript>function goto(page){	window.location.href=page;}//--></SCRIPT></HEAD><BODY onScroll=\"if (typeof balance != 'undefined' )scrollAdj(balance)\"> 		<div style=\"position:relative; top:0; padding:3px; width:987px; margin:auto auto; text-align:center;\" align=\"center\">		        <div id=\"newfeatures\" align=\"center\">New Features <a id=\"newfeaureslink\" style=\"color:#FD1342; cursor:pointer\" onclick=\"loadNewFeatures()\">(click here)</a></div>        <div id=\"ticker_system\" class=\"fbChatSidebar\">			            <div id=\"vertical-ticker\" >                <table cellpadding=\"0\" cellspacing=\"0\"><tr valign=\"top\"><td  onclick=\"showTicker();\" style=\"cursor:pointer\">                <li>              	<div class='desc' style='width:300px; height:12px;'><div style='font-size:12px;font-size-adjust: 0.5; width:100%; overflow-x:hidden; overflow-y:auto' >No New Messages</div></div>                </li>                </td></tr>                <tr><td align=\"center\" style=\" background-color:#000; position:relative; cursor:pointer; background-image:url(/qubic/scripts/countdown/img/scroll.png); background-size: 30px 8px; height:8px; background-repeat:no-repeat; text-align:center; background-position:center \" onclick=\"showTicker();\"></td></tr>                </table>            </div>			         </div>         		 		 <div id=\"google_language_drop\">		    <div id=\"google_translate_element\" class=\"-blank\">		   <select name=\"language\" onChange=\"javascript:window.location=window.location.href.split('?')[0]+'?lang=' + this.value\">		    <option value=\"English\"selected >English</option><option value=\"Spanish\" >Spanish</option><option value=\"Chinese (Simp)\" >Chinese (Simp)</option><option value=\"French\" >French</option><option value=\"Korean\" >Korean</option><option value=\"Japanese\" >Japanese</option><option value=\"Vietnamese\" >Vietnamese</option>		   </select>		   </div>		 </div>				 		 <!--myluckybuck block-->		 			 <!--end myluckybuck block-->     </div>     <!-- End TranslateThis Button -->    <script type=\"text/javascript\">	var estadoSlinkS=1;	function newFeaturesLinkS(){		if(estadoSlinkS==1){			document.getElementById(\"newfeaureslink\").style.color=\"#fff\";			estadoSlinkS=0;		}		else{			estadoSlinkS=1;			document.getElementById(\"newfeaureslink\").style.color=\"#FD1342\";		}	}	setInterval(\"newFeaturesLinkS()\",1000);    </script>            <div id=\"wrapper\" class=\"fix-wrapper\">	      	<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" >			<tr>				<td id=\"clientName\" nowrap=\"nowrap\" style=\"text-align:center\" >&nbsp;<um><strong>Account:</strong></um>&nbsp;<strong>GA9002</strong>                </td>				                    		    <td nowrap=\"nowrap\" id=\"currentBalanceField\" style=\"text-align:center;\">&nbsp;<um><strong>Current Balance:</strong></um>&nbsp; <font color=green>USD&nbsp;1,475</font>&nbsp;								</td>				                    		    <td nowrap=\"nowrap\" id=\"totalPendingField\" style=\"text-align:center\">&nbsp;<a href=\"SpecificPlayerPendingWagers.php\"><um><strong><font color=\"white\" size=\"2px\">Total Pending:</strong></um>&nbsp; USD&nbsp;725.00&nbsp;</font></a>				</td>                    	    	<td nowrap=\"nowrap\" id=\"currentAvailableField\" style=\"border-left:1px solid #fff; text-align:center\">                	        		<strong>&nbsp;<um>Current Available:</um>&nbsp;</strong>                    	<span style=\"color:#00ADEE\">                        	USD&nbsp;20,750.0 &nbsp;                        </span>				</td>				    		    <td nowrap=\"nowrap\" id=\"logoutField\" style=\"text-align:center\">        			<Strong>                    	&nbsp;                    	<a href=\"LogoutSucessful.php\" style=\"color:red; font-size:16px;\">Log Out</a>                        &nbsp;                    </Strong>				</td>							</tr>		</table>		        <div class=\"pages\" style=\"margin-left:auto; margin-right:auto\">        	<table id=\"table_menu\" width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-size:14px\">            	<tr height=\"26px\" >                <!--Straight cases-->                  	                    <!--Straight activo-->					<td class=\"menuSelection\" width=\"180px\" style=\"background-image:url(images/images_topbar/main-bar-blue-1px.png); background-repeat: repeat-x; cursor:pointer\" onmouseover=\"arrow0.style.backgroundPosition='0 -52px'\" onmouseout=\"arrow0.style.backgroundPosition='0 -52px'\"><A href=\"StraightSportSelection.php\" style=\"line-height: 26px;\"><center>Straight bets</center></a></td>                    <td width=\"17px\" style=\"background-image:url(images/images_topbar/main-bar-arrows.png); background-repeat:no-repeat; background-position:0 -52px;\" id=\"arrow0\">&nbsp;</td>                                    	<!--Teaser cases-->                                         <!--Straight active && teaser over-->                    <td class=\"menuSelection\" width=\"180px\" style=\"background-image:url(images/images_topbar/main-bar-black-1px.png); background-repeat: repeat-x; cursor:pointer\" onmouseover=\"arrow1.style.backgroundPosition='0 -52px'; arrow0.style.backgroundPosition='0 -182px'\" onmouseout=\"arrow1.style.backgroundPosition='0 0'; arrow0.style.backgroundPosition='0 -52px'\" ><A href=\"TeaserTeaserSelect.php\" style=\"line-height: 26px;\"><center>Teasers</center></a></td>					<td width=\"14px\" style=\"background-image:url(images/images_topbar/main-bar-arrows.png); background-repeat:no-repeat; background-position:0 0\" id=\"arrow1\">&nbsp;&nbsp;</td>                                         <!--Parlay cases-->                                         <!--parlay no active && teaser no active-->                    <td class=\"menuSelection\" width=\"180px\" style=\"background-image:url(images/images_topbar/main-bar-black-1px.png); background-repeat: repeat-x; cursor:pointer\" onmouseover=\"arrow2.style.backgroundPosition='0 -52px'; arrow1.style.backgroundPosition='0 -26px'\" onmouseout=\"arrow2.style.backgroundPosition='0 0'; arrow1.style.backgroundPosition='0 0'\"><A href=\"StraightSportSelection.php?wagerType=Parlay\" style=\"line-height: 26px;\"><center>Parlays/RR</center></a></td>                    <td width=\"14px\" style=\"background-image:url(images/images_topbar/main-bar-arrows.png); background-repeat:no-repeat; background-position:0 0\" id=\"arrow2\">&nbsp;&nbsp;</td>					                	<!--If bets cases-->                                        <td class=\"menuSelection\" width=\"180px\" style=\"background-image:url(images/images_topbar/main-bar-black-1px.png); background-repeat: repeat-x; cursor:pointer\" onmouseover=\"arrow3.style.backgroundPosition='0 -52px'; arrow2.style.backgroundPosition='0 -26px'\" onmouseout=\"arrow3.style.backgroundPosition='0 0'; arrow2.style.backgroundPosition='0 0'\"><A href=\"IfbetsSportSelection.php\" style=\"line-height: 26px;\"><center>If Bets</center></a></td>                    					<td width=\"14px\" style=\"background-image:url(images/images_topbar/main-bar-arrows.png); background-repeat:no-repeat; background-position:0 0\" id=\"arrow3\">&nbsp;&nbsp;</td>												<!--Action reverse cases-->														 <!--if bet active action reverse over-->							 							<td class=\"menuSelection\" width=\"180px\" style=\"background-image:url(images/images_topbar/main-bar-black-1px.png); background-repeat: repeat-x; cursor:pointer\" onmouseover=\"arrow4.style.backgroundPosition='0 -156px'; arrow3.style.backgroundPosition='0 -26px'\" onmouseout=\"arrow4.style.backgroundPosition='0 -104px'; arrow3.style.backgroundPosition='0 0'\"><A href=\"StraightSportSelection.php?wagerType=Action%20Reverse\" style=\"line-height: 26px;\"><center>Action Reverse</center></a></td>							<td width=\"14px\" style=\"background-image:url(images/images_topbar/main-bar-arrows.png); background-repeat:no-repeat; background-position:0 -104px\" id=\"arrow4\">&nbsp;&nbsp;</td>							 							<!--Turbo props cases-->														<td class=\"menuSelection\" id=\"navcasino\" width=\"250px\" style=\"background-image:url(images/images_topbar/main-bar-red-1px.png); background-repeat: repeat-x; cursor:pointer\" onmouseover=\"arrow5.style.backgroundPosition='0 -52px'; arrow4.style.backgroundPosition='0 -26px'\" onmouseout=\"arrow5.style.backgroundPosition='0 -78px'; arrow4.style.backgroundPosition='0 -104px'\">															  <A href=\"#\" style=\"line-height: 26px;\"><center  style=\"color:#AAAAAA;\"></center></a>							    							</td>							<!--<td width=\"14px\" style=\"background-image:url(images/images_topbar/main-bar-arrows.png); background-repeat:no-repeat; background-position:0 -78px\" id=\"arrow5\">&nbsp;&nbsp;</td>-->                            							<!--horses Cases-->														<td width=\"14px\" style=\"background-image:url(images/images_topbar/main-bar-arrows.png); background-repeat:no-repeat; background-position:0 -78px\" id=\"arrow5\">&nbsp;&nbsp;</td>														 <!--no props active && no casino active-->							<td class=\"menuSelection\" width=\"180px\" style=\"background-image:url(images/images_topbar/main-bar-black-1px.png); background-repeat: repeat-x; cursor:pointer\" onmouseover=\"arrow6.style.backgroundPosition='0 -52px'; arrow5.style.backgroundPosition='0 -130px'\" onmouseout=\"arrow6.style.backgroundPosition='0 0'; arrow5.style.backgroundPosition='0 -78px'\">							 <center style='color:#AAAAAA'></center>							</td>														<!--casino Cases-->														<td width=\"14px\" style=\"background-image:url(images/images_topbar/main-bar-arrows.png); background-repeat:no-repeat; background-position:0 0\" id=\"arrow6\">&nbsp;&nbsp;</td>							<!--<td class=\"menuSelection\" width=\"180px\" style=\"background-image:url(images/images_topbar/main-bar-black-1px.png); background-repeat: repeat-x; cursor:pointer\" onmouseover=\"arrow6.style.backgroundPosition='0 -26px'\" onmouseout=\"arrow6.style.backgroundPosition='0 0'\">-->														<td id=\"navcasino\" class=\"menuSelection\" width=\"180px\" style=\"background-image:url(images/images_topbar/main-bar-black-1px.png); background-repeat: repeat-x; cursor:pointer\" onmouseover=\"arrow6.style.backgroundPosition='0 -26px'\" onmouseout=\"arrow6.style.backgroundPosition='0 0'\">														 <A href=\"#\" style=\"line-height: 26px;\"><center  style=\"color:#AAAAAA;\"></center></a>							 							 </td>							 								                </tr>  			</table>            </div>			            <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\">                <tr>                    <td id=\"tdCategoriesContainer\">                        <div class=\"categories\" style=\"margin:3px auto 0 auto;\">                            <table width=\"928px\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-size:12px; background-image:url(images/images_topbar/options_fullbg.png); background-repeat:no-repeat;\">                                <tr>                                    <td id=\"categoriesHomeField\" style=\"text-align:center\"><a style=\"color:#fff\" href=\"StraightLoginSportSelection.php\"><center><div class=\"spriteHome\"><br></div>Home</center></a></td>                                    <td style=\"border-left:1px solid #fff;\"></td>                                    <td class=\"categoriesStandardFields\" style=\"text-align:center\" ><a style=\"color:#fff\" href=\"Scores.php\"><center><div class=\"spriteScores\"><br></div>Scores</center></a></td>                                     <td style=\"border-left:1px solid #fff;\"></td>                                    <!--<td class=\"categoriesStandardFields\" style=\"text-align:center\"><a style=\"color:#fff\" href=\"PlayerTransactions.php?transactionListDays=1\"><center><div class=\"spriteReports\"><br></div>Reports</center></a></td>-->                                    <td style=\"border-left:1px solid #fff;\"></td>									                                   		<td class=\"categoriesStandardFields\" style=\"text-align:center\"><a style=\"color:#fff\" href=\"SpecificPlayerPendingWagers.php\" ><center><div class=\"spritePending\" ><br></div><div id=\"pendingWagersNumberE\"><strong><center>1</center></strong></div><span style=\"padding:0 0 0 15px\">Pending Wagers </span></center></a></td>																			 <td style=\"border-left:1px solid #fff;\"></td>                                    <td class=\"categoriesStandardFields\" style=\"text-align:center\"><center><a style=\"color:#fff\" href=\"PlayerDailyFigure.php\"><div class=\"spriteDailyFigure\" ><br></div><span>Daily Figures</span></center></a></td>                                    <td style=\"border-left:1px solid #fff;\"></td>                                    <td class=\"categoriesStandardFields\" style=\"text-align:center\"><center><a style=\"color:#fff\" href=\"SpecificPlayerWeeklyFigure.php\"><div class=\"spriteWeeklyFigure\" ><br></div><span>Graded Wagers</span></center></a></td>                                     <td style=\"border-left:1px solid #fff;\"></td>                                    <td id=\"categoriesTransactionsField\" style=\"text-align:center\"><center><a style=\"color:#fff\" href=\"SpecificPlayerTransactions.php\"><div class=\"spriteTransaction\" ><br></div><span>Transactions</span></center></a></td>                                     <td style=\"border-left:1px solid #fff;\"></td>                                                                         <td class=\"categoriesStandardFields\" style=\"text-align:center; padding:0 0 0 0\"><center><a style=\"color:#fff\" href=\"PanelMessage.php\" ><div class=\"spriteMessages\" ><br></div><div id=\"messagesNumberE\" ><strong><center>                                     0                                    </center></strong></div><span>Messages</span></center></a></td>									 									 <td style=\"border-left:1px solid #fff;\"></td>									<td class=\"categoriesStandardFields\" style=\"text-align:center; padding:0 20px 0 0\">									    									  <center><a style=\"color:#fff\" href=\"PlayerPanic.php\"><div class=\"spriteIconBoss\" ><br></div><span>Boss</span></center></a>									  									</td>                                </tr>                            </table>                        </div>        	   		 </td>	        	</tr>                <tr>                	<td align=\"left\">                    	                        <div id=\"trends2\" class=\"container\" align=\"left\" >                         	<div id=\"scores_tool\" class=\"inner2\" style=\"height:31px!important\">                                <span class=\"trend-label2\">Result:</span>                                <span class=\"trend-label2\" style=\"position:relative; left:95px\">NEWS:</span>						      	<ul id=\"ul_scores\" class=\"trendscontent2\" style=\"zoom: 1;position:relative;top:0px\">                                	<li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/japanese_bl.png' ></td><td align=center>Baseball            <br>4 | 5</td><td><img width=86px src='logos/japanese_bl.png' ></td></tr><tr valign=top><td align=center>Hanshin Tigers</td><td></td><td align=center>Hiroshima Carp</td></tr></table>\">Hans  4 - 5 Hiro</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/japanese_bl.png' ></td><td align=center>Baseball            <br>7 | 3</td><td><img width=86px src='logos/japanese_bl.png' ></td></tr><tr valign=top><td align=center>Seibu Lions</td><td></td><td align=center>Nippon Ham Fighters</td></tr></table>\">Seib  7 - 3 Nipp</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/japanese_bl.png' ></td><td align=center>Baseball            <br>9 | 12</td><td><img width=86px src='logos/japanese_bl.png' ></td></tr><tr valign=top><td align=center>Chunichi Dragons</td><td></td><td align=center>Yakult Swallows</td></tr></table>\">Chun  9 - 12 Yaku</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/korea_bl.png' ></td><td align=center>Baseball            <br>3 | 4</td><td><img width=86px src='logos/korea_bl.png' ></td></tr><tr valign=top><td align=center>LG Twins</td><td></td><td align=center>KT Wiz Suwon</td></tr></table>\">LG T  3 - 4 KT W</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/korea_bl.png' ></td><td align=center>Baseball            <br>7 | 3</td><td><img width=86px src='logos/korea_bl.png' ></td></tr><tr valign=top><td align=center>Nexen Heroes</td><td></td><td align=center>SK Wyverns</td></tr></table>\">Nexe  7 - 3 SK W</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/korea_bl.png' ></td><td align=center>Baseball            <br>5 | 3</td><td><img width=86px src='logos/korea_bl.png' ></td></tr><tr valign=top><td align=center>Samsung Lions</td><td></td><td align=center>NC Dinos</td></tr></table>\">Sams  5 - 3 NC D</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/korea_bl.png' ></td><td align=center>Baseball            <br>10 | 5</td><td><img width=86px src='logos/korea_bl.png' ></td></tr><tr valign=top><td align=center>Kia Tigers</td><td></td><td align=center>Doosan Bears</td></tr></table>\">Kia   10 - 5 Doos</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/korea_bl.png' ></td><td align=center>Baseball            <br>4 | 6</td><td><img width=86px src='logos/korea_bl.png' ></td></tr><tr valign=top><td align=center>Lotte Giants</td><td></td><td align=center>Hanwha Eagles</td></tr></table>\">Lott  4 - 6 Hanw</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/Maryland Terrapins.png' ></td><td align=center>Football            <br>null | null</td><td><img width=86px src='logos/Maryland Terrapins.png' ></td></tr><tr valign=top><td align=center>Maryland RSW</td><td></td><td align=center>Maryland RSW.</td></tr></table>\">Mary  null - null Mary</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/hk_Russia.png' ></td><td align=center>Hockey              <br>0 | 3</td><td><img width=86px src='logos/hk_Russia.png' ></td></tr><tr valign=top><td align=center>Slovan Bratislava</td><td></td><td align=center>Traktor Chelyabinsk</td></tr></table>\">Slov  0 - 3 Trak</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/hk_Russia.png' ></td><td align=center>Hockey              <br>1 | 3</td><td><img width=86px src='logos/hk_Russia.png' ></td></tr><tr valign=top><td align=center>CSKA Moscow</td><td></td><td align=center>Yekaterinburg</td></tr></table>\">CSKA  1 - 3 Yeka</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/hk_Russia.png' ></td><td align=center>Hockey              <br>2 | 1</td><td><img width=86px src='logos/hk_Russia.png' ></td></tr><tr valign=top><td align=center>Dinamo Riga</td><td></td><td align=center>Metallurg Magnitogorsk</td></tr></table>\">Dina  2 - 1 Meta</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/hk_Russia.png' ></td><td align=center>Hockey              <br>0 | 5</td><td><img width=86px src='logos/hk_Russia.png' ></td></tr><tr valign=top><td align=center>Sibir Novosibirsk</td><td></td><td align=center>Lokomotiv Yaroslavl</td></tr></table>\">Sibi  0 - 5 Loko</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/hk_Russia.png' ></td><td align=center>Hockey              <br>4 | 4</td><td><img width=86px src='logos/hk_Russia.png' ></td></tr><tr valign=top><td align=center>Salavat Ufa</td><td></td><td align=center>Cherepovets</td></tr></table>\">Sala  4 - 4 Cher</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/hk_Russia.png' ></td><td align=center>Hockey              <br>5 | 1</td><td><img width=86px src='logos/hk_Russia.png' ></td></tr><tr valign=top><td align=center>Avangard Omsk</td><td></td><td align=center>Dynamo Moscow</td></tr></table>\">Avan  5 - 1 Dyna</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/ATP_logo.png' ></td><td align=center>Tennis              <br>3 | 1</td><td><img width=86px src='logos/ATP_logo.png' ></td></tr><tr valign=top><td align=center>J M Del Potro</td><td></td><td align=center>J Isner</td></tr></table>\">J M   3 - 1 J Is</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/Tennis-generic-home.png' ></td><td align=center>Tennis              <br>25 | 18</td><td><img width=86px src='logos/Tennis-generic-home.png' ></td></tr><tr valign=top><td align=center>J M Del Potro Games</td><td></td><td align=center>J Isner Games</td></tr></table>\">J M   25 - 18 J Is</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/wta_logo.png' ></td><td align=center>Tennis              <br>1 | 2</td><td><img width=86px src='logos/wta_logo.png' ></td></tr><tr valign=top><td align=center>S Stephens</td><td></td><td align=center>A Sevastova</td></tr></table>\">S St  1 - 2 A Se</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/wta_logo.png' ></td><td align=center>Tennis              <br>5 | 12</td><td><img width=86px src='logos/wta_logo.png' ></td></tr><tr valign=top><td align=center>S Stephens Games</td><td></td><td align=center>A Sevastova Games</td></tr></table>\">S St  5 - 12 A Se</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/PGATour.png' ></td><td align=center>Golf                <br>0 | 0</td><td><img width=86px src='logos/PGATour.png' ></td></tr><tr valign=top><td align=center>T Woods 2018 Majors</td><td></td><td align=center>T Woods 2018 Majors.</td></tr></table>\">T Wo  0 - 0 T Wo</a></li>                               	</ul>  							</div>  							<span class=\"fade fade-left2\">&nbsp;</span>						</div>                        <div id=\"trends\" align=\"left\">  							<div class=\"inner\">	 							<ul class=\"trendscontent\" style=\"zoom: 1;position:\">									 <li class=\"trend-label\">&nbsp;&nbsp;</li>                    				 <li>						            	<a href=\"#\" class=\"search_link\" name=\"\" rel=\"nofollow\">No news available...</a>						          	 </li>                                 </ul>  							</div>  							<span class=\"fade fade-left\">&nbsp;</span><span class=\"fade fade-right\">&nbsp;</span>						</div>                        <div style=\"width:100%\">                       		<div id=\"score_popup\" style=\"display:none;\">                            </div>                       </div>					   					</td>                </tr>        	</table>                      	<table cellspacing=\"0\" border=\"0\" width=\"100%\" align=\"left\">               <tr valign=\"top\">                    <td style=\" width:100%\" >                    	<div id=\"contentright\">                                                     <div class=\"postP\">                                                          <div  style=\" padding:3px; height:100%\"><div style='background-color:#fff; padding:3px; width:86.3%; margin:auto auto; text-align:center' align='center'><div class='offering_pair'><table cellspacing='0' cellpadding='0' class='tktable' style='width:100%' ><tr><th class=sptype>Wager type : Straight Bet </th></tr></table><table cellspacing='0' cellpadding='0' class='listingtable' ><TR class='offering_noun'><TD width=100% style='border-bottom:1px solid #333333; padding:3px' ><table cellspacing='0' cellpadding='0' width=100% ><TR class='gamenumb'><TD  width=15% >MLB</td><td><table cellpadding='2' cellspacing='2' ><tr valign='mdddle'><td align='center' style='border-top:transparent;padding:2px;font-weight:bold; font-size:13px'>906</td><td align='left' style='border-top:transparent;padding:2px;font-weight:bold; font-size:13px'><img width=\"43px\" src=\"logos/Miami-Marlins.png\" class=\"logo1\" ></td><td align='left' style='border-top:transparent;padding:2px'><span class=thedate3 >9/4/2018 7:10 PM -  (EST)</span><br /><span class=rotnumb >Miami Marlins Money Line</span><span class=rotnumb > +105 for 1st 5 Innings   <br>J. Arrieta - R - Action&nbsp;&nbsp;&nbsp;T. Richards - R - Action </span></td></tr></table>  </td><td align=right><span class='gamenumb'><um>Risking</um> <span style='color:#FF0000;font-weight:bold; font-size:14px'>500.00 USD</span> <um>To Win</um> <span style='color:#000000;font-weight:bold; font-size:14px'>525.00 USD</span>  </span></TD></TR><tr><td colspan=3><TABLE cellspacing='0' cellpadding='0' class=tktable><TR ><TD width=269px style='border-top:1px solid transparent' ><a  href='StraightVerifyWager.php?delete=M2_3' class='deleteSpecial' ><img border='0' src='/Qubic/qubic/images/deleteIcon.gif'></a><span class='tits'>&nbsp;&nbsp; Internet Max Wager: 1000.00</span></TD><TD style='border-top:1px solid transparent'><span class='tits'><INPUT type='radio' onclick=javascript:goto('StraightVerifyWager.php?modify=true&radioxGame0=riskType') id='radiox' name='radioxGame0)' value='riskType' CHECKED>&nbsp;<um>Risk Amount</um>&nbsp;&nbsp;<INPUT type='radio' onclick=javascript:goto('StraightVerifyWager.php?modify=true&radioxGame0=toWinType') id='radiox' name='radioxGame0' value='toWinType'>&nbsp;<um>To Win Amount</um>&nbsp;&nbsp;&nbsp;&nbsp; : 500.0</span></TD></TR></TABLE></td></tr></TABLE></td></tr></table><div class='offering_noun' width=\"100%\" style=\"background-color:#CC0000; color:#FFFFFF;font-size:14px;padding:3px;font-weight:bold;border:1px solid #000\">One or more lines have changed</div></div><div align=\"center\" style=\"background-color:#CCCCCC; padding:5px; border:1px solid #666666;width: 835px;margin-left: auto;margin-right: auto;\"><div class=\"block-content_box_review\"><FORM name=\"WagerVerificationForm\" method=\"post\" action=\"CheckAcceptancePassword.php\"><INPUT type=\"hidden\" id=inetWagerNumber name=inetWagerNumber value=\"0.7565737737569653\"><BR><BR>Please Review Wagers Carefully!  &amp;  Re-enter Password To Confirm Wagers </FONT><br /><INPUT type=\"password\" id=password class=input_login name=password size = 8>&nbsp;&nbsp;<INPUT type=\"submit\" value=\"Submit\" id=submit1 name=submit1></FORM><script type='text/javascript'>document.getElementById('password').focus();</script><BR><FORM name=\"Cancel\" method=\"post\" action=\"Error.php?err=41\"><INPUT type=\"submit\" value=\"Cancel\" id=\"cancel\" name=\"cancel\" style=\"margin-top:5px\"></FORM></div></div></table></div>	</div>                            </div>                        </div>                    </td>               </tr>        	</table>        </div>		        </div>		        <div id=\"new_footer\">    	<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"float:left;background-image:url(/Qubic/images/images_topbar/images_content/bg_footer.png); background-repeat:no-repeat; border-bottom:2px solid #D6D6D6;\" >        	<tr valign=\"bottom\">            	<td valign=\"top\" style=\"float:right; padding:10px 0 0 0\">                                                                <img src=\"images/images_topbar/images_content/phone-symbol.png\" class=\"phone_img\">                    	<span class=\"phone_img_text\" style=\"color:#FFF\">                                        <um>Wagering Phone</um></span>                </td>                <td style=\" padding:2px 0 0 70px;\">				                 	<img src=\"images/images_topbar/images_content/settings_icon.png\"  style=\"cursor:pointer; \" onClick=\"javascript:loadSettings();\" class=\"settings_icon\">				                 </td>                <td  valign=\"middle\" >                	<img src=\"images/images_topbar/images_content/separador.png\" height=\"20px\" style=\"margin:0 30px 0 0\">                </td>				                	<td  style=\" padding:2px 0 0 0;\">                		<a href=\"Rules.php\"><img src=\"images/images_topbar/images_content/rules_icon.png\" class=\"rules_icon\"></a>                	</td>				                 <td  valign=\"middle\" >                	<img src=\"images/images_topbar/images_content/separador.png\" height=\"20px\" style=\"margin:0 30px 0 30px\">                </td>                <td style=\" padding:5px 0 0 0;\">                                 </td>                <td align=\"center\" style=\" padding:0 20px 0 0;\" valign=\"middle\" class=\"copyright\">                	 All Rights Reserved.<br />Copyright 2018.                </td>            </tr>        </table>         <div style=\"height:15px\">&nbsp;</div>    </div>		</body>    </html>	";
//			String xhtml = "<!DOCTYPE HTML PUBLIC \"-/W3C/DTD XHTML 1.0 Transitional/EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"> <html> <HEAD> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"> <meta name=\"description\" content=\"Bet with Us\"> <!--<meta name=\"google-translate-customi\n" + 
//					"2018-09-12 21:58:59 <06e3b3f6-b6d7-11e8-92f5-41bebf8f8674> ERROR HttpClientWrapper:1615 - RESPONSE: <!DOCTYPE HTML PUBLIC \"-/W3C/DTD XHTML 1.0 Transitional/EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"> <html> <HEAD> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"> <meta name=\"description\" content=\"Bet with Us\"> <!--<meta name=\"google-translate-customization\" content=\"850af3aab9df52c0-1111978e695f2729-g87703a3f0fd07b91-d\"></meta>--> <link rel=\"stylesheet\" type=\"text/css\" href=\"/Qubic/images/themes/theme1/theme1.css?v=86\"> <link rel=\"stylesheet\" type=\"text/css\" href=\"/Qubic/qubic/scripts/coverflow/coverflow.css?v=86\"> <link rel=\"stylesheet\" type=\"text/css\" href=\"/Qubic/css/commun.css?version=86\"> <!--<link rel=\"stylesheet/less\" type=\"text/css\" href=\"http://sportswidgets.pro/app/app.less?v=2&version=86\" />--> <link rel=\"stylesheet\" type=\"text/css\" href=\"/Qubic/qubic/scripts/tooltipster/tooltipster.css?version=86\" /> <script type=\"text/javascript\" src=\"/Qubic/qubic/scripts/fancybox/jquery-1.4.2.min.js?version=86\"></script> <script type=\"text/javascript\" src=\"/Qubic/qubic/scripts/fancybox/jquery.mousewheel-3.0.4.pack.js?version=86\"></script> <script type=\"text/javascript\" src=\"/Qubic/qubic/scripts/fancybox/jquery.fancybox-1.3.4.js?version=86\"></script> <script type=\"text/javascript\" src=\"/Qubic/qubic/scripts/autosugest/autosuggest.js?version=86\"></script> <script type=\"text/javascript\" src=\"/Qubic/qubic/scripts/autosugest/autosuggestinit.js?version=86\"></script> <script type=\"text/javascript\" src=\"/Qubic/scripts/CommonJavaScript.js?version=86\"></script> <script type=\"text/javascript\" src=\"/Qubic/qubic/scripts/tooltipster/jquery.tooltipster.js?version=86\"></script> <script type=\"text/javascript\" src=\"/Qubic/qubic/scripts/ticker/facescroll.js?version=86\"></script> <script type=\"text/javascript\" src=\"/Qubic/qubic/scripts/ticker/ticker.js?version=86\"></script> <script type=\"text/javascript\" src=\"/Qubic/qubic/scripts/countdown/countdown.js?version=86\"></script> <script type=\"text/javascript\" src=\"/Qubic/qubic/scripts/ticker/jquery-ui.min.js?version=86\"></script> <link rel=\"stylesheet\" type=\"text/css\" href=\"/Qubic/qubic/scripts/fancybox/jquery.fancybox-1.3.4.css?version=86\" media=\"screen\" /> <title>JNN286 | Home</title> <script type=\"text/javascript\"> function loadSettings(){ $.fancybox.showActivity(); $.ajax({ type : \"GET\", cache : false, url : \"PlayerSettings.php\", data : \"ip=1\", success: function(data) { $.fancybox(data); } }); return false; } function loadNewFeatures(){ $.fancybox.showActivity(); $.ajax({ type : \"GET\", cache : false, url : \"Newfeatures.php\", data : \"ip=1\", success: function(data) { $.fancybox(data); } }); return false; } </script> <!--[if IE]> <style> #trends .inner{ width:567px !important; overflow:hidden !important; zoom:1 !important; position:relative !important; margin-left:284px !important; top:0px !important; } .tooltipster-default { border-radius: 5px; border: 1px solid #362D2F; background: transparent #000; color: #fff; width:380px; height:90px; background-color:#000; opacity:0.8; filter:alpha(opacity=80); /* For IE8 and earlier */ } </style> <![endif]--> <!--[if lte IE 6]> <script src=\"/Qubic/qubic/scripts/pngfixie6/DD_belatedPNG_0.0.8a-min.js\"></script> <![endif]--> <script type=\"text/javascript\"> $(document).ready(function() { var page={}; $(function() { new FrontPage().init(); }); $(function() { $('.tooltip').tooltipster({ animation: 'grow', position: 'bottom' }); $('.limitstooltip').tooltipster({ animation: 'grow', position: 'top' }); }); $('.inner2').vTicker({ speed: 500, pause: 3000, showItems: 3, animation: 'fade', mousePause: true, height: 0, direction: 'up' }); page.trendDescriptions = {}; loadTrendDescriptions(); var badBrowser = (/MSIE ((5\\.5)|6)/.test(navigator.userAgent) && navigator.platform == \"Win32\"); if (badBrowser) { DD_belatedPNG.fix('img'); $('body').supersleight(); } hideTickerFast() }); </script> <SCRIPT ID=clientEventHandlersJS LANGUAGE=javascript>function goto(page){ window.location.href=page;}//--></SCRIPT></HEAD><BODY onScroll=\"if (typeof balance != 'undefined' )scrollAdj(balance)\"> <div style=\"position:relative; top:0; padding:3px; width:987px; margin:auto auto; text-align:center;\" align=\"center\"> <div id=\"newfeatures\" align=\"center\">New Features <a id=\"newfeaureslink\" style=\"color:#FD1342; cursor:pointer\" onclick=\"loadNewFeatures()\">(click here)</a></div> <div id=\"ticker_system\" class=\"fbChatSidebar\"> <div id=\"vertical-ticker\" > <table cellpadding=\"0\" cellspacing=\"0\"><tr valign=\"top\"><td onclick=\"showTicker();\" style=\"cursor:pointer\"> <li> <div class='desc' style='width:300px; height:12px;'><div style='font-size:12px;font-size-adjust: 0.5; width:100%; overflow-x:hidden; overflow-y:auto' >No New Messages</div></div> </li> </td></tr> <tr><td align=\"center\" style=\" background-color:#000; position:relative; cursor:pointer; background-image:url(/qubic/scripts/countdown/img/scroll.png); background-size: 30px 8px; height:8px; background-repeat:no-repeat; text-align:center; background-position:center \" onclick=\"showTicker();\"></td></tr> </table> </div> </div> <div id=\"google_language_drop\"> <div id=\"google_translate_element\" class=\"-blank\"> <select name=\"language\" onChange=\"javascript:window.location=window.location.href.split('?')[0]+'?lang=' + this.value\"> <option value=\"English\"selected >English</option><option value=\"Spanish\" >Spanish</option><option value=\"Chinese (Simp)\" >Chinese (Simp)</option><option value=\"French\" >French</option><option value=\"Korean\" >Korean</option><option value=\"Japanese\" >Japanese</option><option value=\"Vietnamese\" >Vietnamese</option> </select> </div> </div> <!--myluckybuck block--> <!--end myluckybuck block--> </div> <!-- End TranslateThis Button --> <script type=\"text/javascript\"> var estadoSlinkS=1; function newFeaturesLinkS(){ if(estadoSlinkS==1){ document.getElementById(\"newfeaureslink\").style.color=\"#fff\"; estadoSlinkS=0; } else{ estadoSlinkS=1; document.getElementById(\"newfeaureslink\").style.color=\"#FD1342\"; } } setInterval(\"newFeaturesLinkS()\",1000); </script> <div id=\"wrapper\" class=\"fix-wrapper\"> <table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" > <tr> <td id=\"clientName\" nowrap=\"nowrap\" style=\"text-align:center\" >&nbsp;<um><strong>Account:</strong></um>&nbsp;<strong>JNN286</strong> </td> <td nowrap=\"nowrap\" id=\"currentBalanceField\" style=\"text-align:center;\">&nbsp;<um><strong>Current Balance:</strong></um>&nbsp; <font color=green>USD&nbsp;2,560</font>&nbsp; </td> <td nowrap=\"nowrap\" id=\"totalPendingField\" style=\"text-align:center\">&nbsp;<a href=\"SpecificPlayerPendingWagers.php\"><um><strong><font color=\"white\" size=\"2px\">Total Pending:</strong></um>&nbsp; USD&nbsp;6,780.00&nbsp;</font></a> </td> <td nowrap=\"nowrap\" id=\"currentAvailableField\" style=\"border-left:1px solid #fff; text-align:center\"> <strong>&nbsp;<um>Current Available:</um>&nbsp;</strong> <span style=\"color:#00ADEE\"> USD&nbsp;20,780.0 &nbsp; </span> </td> <td nowrap=\"nowrap\" id=\"logoutField\" style=\"text-align:center\"> <Strong> &nbsp; <a href=\"LogoutSucessful.php\" style=\"color:red; font-size:16px;\">Log Out</a> &nbsp; </Strong> </td> </tr> </table> <div class=\"pages\" style=\"margin-left:auto; margin-right:auto\"> <table id=\"table_menu\" width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-size:14px\"> <tr height=\"26px\" > <!--Straight cases--> <!--Straight activo--> <td class=\"menuSelection\" width=\"180px\" style=\"background-image:url(images/images_topbar/main-bar-blue-1px.png); background-repeat: repeat-x; cursor:pointer\" onmouseover=\"arrow0.style.backgroundPosition='0 -52px'\" onmouseout=\"arrow0.style.backgroundPosition='0 -52px'\"><A href=\"StraightSportSelection.php\" style=\"line-height: 26px;\"><center>Straight bets</center></a></td> <td width=\"17px\" style=\"background-image:url(images/images_topbar/main-bar-arrows.png); background-repeat:no-repeat; background-position:0 -52px;\" id=\"arrow0\">&nbsp;</td> <!--Teaser cases--> <!--Straight active && teaser over--> <td class=\"menuSelection\" width=\"180px\" style=\"background-image:url(images/images_topbar/main-bar-black-1px.png); background-repeat: repeat-x; cursor:pointer\" onmouseover=\"arrow1.style.backgroundPosition='0 -52px'; arrow0.style.backgroundPosition='0 -182px'\" onmouseout=\"arrow1.style.backgroundPosition='0 0'; arrow0.style.backgroundPosition='0 -52px'\" ><A href=\"TeaserTeaserSelect.php\" style=\"line-height: 26px;\"><center>Teasers</center></a></td> <td width=\"14px\" style=\"background-image:url(images/images_topbar/main-bar-arrows.png); background-repeat:no-repeat; background-position:0 0\" id=\"arrow1\">&nbsp;&nbsp;</td> <!--Parlay cases--> <!--parlay no active && teaser no active--> <td class=\"menuSelection\" width=\"180px\" style=\"background-image:url(images/images_topbar/main-bar-black-1px.png); background-repeat: repeat-x; cursor:pointer\" onmouseover=\"arrow2.style.backgroundPosition='0 -52px'; arrow1.style.backgroundPosition='0 -26px'\" onmouseout=\"arrow2.style.backgroundPosition='0 0'; arrow1.style.backgroundPosition='0 0'\"><A href=\"StraightSportSelection.php?wagerType=Parlay\" style=\"line-height: 26px;\"><center>Parlays/RR</center></a></td> <td width=\"14px\" style=\"background-image:url(images/images_topbar/main-bar-arrows.png); background-repeat:no-repeat; background-position:0 0\" id=\"arrow2\">&nbsp;&nbsp;</td> <!--If bets cases--> <td class=\"menuSelection\" width=\"180px\" style=\"background-image:url(images/images_topbar/main-bar-black-1px.png); background-repeat: repeat-x; cursor:pointer\" onmouseover=\"arrow3.style.backgroundPosition='0 -52px'; arrow2.style.backgroundPosition='0 -26px'\" onmouseout=\"arrow3.style.backgroundPosition='0 0'; arrow2.style.backgroundPosition='0 0'\"><A href=\"IfbetsSportSelection.php\" style=\"line-height: 26px;\"><center>If Bets</center></a></td> <td width=\"14px\" style=\"background-image:url(images/images_topbar/main-bar-arrows.png); background-repeat:no-repeat; background-position:0 0\" id=\"arrow3\">&nbsp;&nbsp;</td> <!--Action reverse cases--> <!--if bet active action reverse over--> <td class=\"menuSelection\" width=\"180px\" style=\"background-image:url(images/images_topbar/main-bar-black-1px.png); background-repeat: repeat-x; cursor:pointer\" onmouseover=\"arrow4.style.backgroundPosition='0 -156px'; arrow3.style.backgroundPosition='0 -26px'\" onmouseout=\"arrow4.style.backgroundPosition='0 -104px'; arrow3.style.backgroundPosition='0 0'\"><A href=\"StraightSportSelection.php?wagerType=Action%20Reverse\" style=\"line-height: 26px;\"><center>Action Reverse</center></a></td> <td width=\"14px\" style=\"background-image:url(images/images_topbar/main-bar-arrows.png); background-repeat:no-repeat; background-position:0 -104px\" id=\"arrow4\">&nbsp;&nbsp;</td> <!--Turbo props cases--> <td class=\"menuSelection\" id=\"navcasino\" width=\"250px\" style=\"background-image:url(images/images_topbar/main-bar-red-1px.png); background-repeat: repeat-x; cursor:pointer\" onmouseover=\"arrow5.style.backgroundPosition='0 -52px'; arrow4.style.backgroundPosition='0 -26px'\" onmouseout=\"arrow5.style.backgroundPosition='0 -78px'; arrow4.style.backgroundPosition='0 -104px'\"> <a href=\"http://live2.betlivelines.com/sports2/session_pre.php?to=https://dimebet.ag/Qubic/livebetting2.php\"><center>Dynamic Live Betting</center></a> </td> <!--<td width=\"14px\" style=\"background-image:url(images/images_topbar/main-bar-arrows.png); background-repeat:no-repeat; background-position:0 -78px\" id=\"arrow5\">&nbsp;&nbsp;</td>--> <!--horses Cases--> <td width=\"14px\" style=\"background-image:url(images/images_topbar/main-bar-arrows.png); background-repeat:no-repeat; background-position:0 -78px\" id=\"arrow5\">&nbsp;&nbsp;</td> <!--no props active && no casino active--> <td class=\"menuSelection\" width=\"180px\" style=\"background-image:url(images/images_topbar/main-bar-black-1px.png); background-repeat: repeat-x; cursor:pointer\" onmouseover=\"arrow6.style.backgroundPosition='0 -52px'; arrow5.style.backgroundPosition='0 -130px'\" onmouseout=\"arrow6.style.backgroundPosition='0 0'; arrow5.style.backgroundPosition='0 -78px'\"> <center style='color:#AAAAAA'></center> </td> <!--casino Cases--> <td width=\"14px\" style=\"background-image:url(images/images_topbar/main-bar-arrows.png); background-repeat:no-repeat; background-position:0 0\" id=\"arrow6\">&nbsp;&nbsp;</td> <!--<td class=\"menuSelection\" width=\"180px\" style=\"background-image:url(images/images_topbar/main-bar-black-1px.png); background-repeat: repeat-x; cursor:pointer\" onmouseover=\"arrow6.style.backgroundPosition='0 -26px'\" onmouseout=\"arrow6.style.backgroundPosition='0 0'\">--> <td id=\"navcasino\" class=\"menuSelection\" width=\"180px\" style=\"background-image:url(images/images_topbar/main-bar-black-1px.png); background-repeat: repeat-x; cursor:pointer\" onmouseover=\"arrow6.style.backgroundPosition='0 -26px'\" onmouseout=\"arrow6.style.backgroundPosition='0 0'\"> <A href=\"#\" style=\"line-height: 26px;\"><center style=\"color:#AAAAAA;\"></center></a> </td> </tr> </table> </div> <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\"> <tr> <td id=\"tdCategoriesContainer\"> <div class=\"categories\" style=\"margin:3px auto 0 auto;\"> <table width=\"928px\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-size:12px; background-image:url(images/images_topbar/options_fullbg.png); background-repeat:no-repeat;\"> <tr> <td id=\"categoriesHomeField\" style=\"text-align:center\"><a style=\"color:#fff\" href=\"StraightLoginSportSelection.php\"><center><div class=\"spriteHome\"><br></div>Home</center></a></td> <td style=\"border-left:1px solid #fff;\"></td> <td class=\"categoriesStandardFields\" style=\"text-align:center\" ><a style=\"color:#fff\" href=\"Scores.php\"><center><div class=\"spriteScores\"><br></div>Scores</center></a></td> <td style=\"border-left:1px solid #fff;\"></td> <!--<td class=\"categoriesStandardFields\" style=\"text-align:center\"><a style=\"color:#fff\" href=\"PlayerTransactions.php?transactionListDays=1\"><center><div class=\"spriteReports\"><br></div>Reports</center></a></td>--> <td style=\"border-left:1px solid #fff;\"></td> <td class=\"categoriesStandardFields\" style=\"text-align:center\"><a style=\"color:#fff\" href=\"SpecificPlayerPendingWagers.php\" ><center><div class=\"spritePending\" ><br></div><div id=\"pendingWagersNumberE\"><strong><center>13</center></strong></div><span style=\"padding:0 0 0 15px\">Pending Wagers </span></center></a></td> <td style=\"border-left:1px solid #fff;\"></td> <td class=\"categoriesStandardFields\" style=\"text-align:center\"><center><a style=\"color:#fff\" href=\"PlayerDailyFigure.php\"><div class=\"spriteDailyFigure\" ><br></div><span>Daily Figures</span></center></a></td> <td style=\"border-left:1px solid #fff;\"></td> <td class=\"categoriesStandardFields\" style=\"text-align:center\"><center><a style=\"color:#fff\" href=\"SpecificPlayerWeeklyFigure.php\"><div class=\"spriteWeeklyFigure\" ><br></div><span>Graded Wagers</span></center></a></td> <td style=\"border-left:1px solid #fff;\"></td> <td id=\"categoriesTransactionsField\" style=\"text-align:center\"><center><a style=\"color:#fff\" href=\"SpecificPlayerTransactions.php\"><div class=\"spriteTransaction\" ><br></div><span>Transactions</span></center></a></td> <td style=\"border-left:1px solid #fff;\"></td> <td class=\"categoriesStandardFields\" style=\"text-align:center; padding:0 0 0 0\"><center><a style=\"color:#fff\" href=\"PanelMessage.php\" ><div class=\"spriteMessages\" ><br></div><div id=\"messagesNumberE\" ><strong><center> 0 </center></strong></div><span>Messages</span></center></a></td> <td style=\"border-left:1px solid #fff;\"></td> <td class=\"categoriesStandardFields\" style=\"text-align:center; padding:0 20px 0 0\"> <center><a style=\"color:#fff\" href=\"PlayerPanic.php\"><div class=\"spriteIconBoss\" ><br></div><span>Boss</span></center></a> </td> </tr> </table> </div> </td> </tr> <tr> <td align=\"left\"> <div id=\"trends2\" class=\"container\" align=\"left\" > <div id=\"scores_tool\" class=\"inner2\" style=\"height:31px!important\"> <span class=\"trend-label2\">Result:</span> <span class=\"trend-label2\" style=\"position:relative; left:95px\">NEWS:</span> <ul id=\"ul_scores\" class=\"trendscontent2\" style=\"zoom: 1;position:relative;top:0px\"> <li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/Los Angeles Dodgers.png' ></td><td align=center>LIVE <br>8 | 1</td><td><img width=86px src='logos/Cincinnati-Reds.png' ></td></tr><tr valign=top><td align=center>Dodgers (Live)</td><td></td><td align=center>Reds (Live)</td></tr></table>\">Dodg 8 - 1 Reds</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/Houston-Astros.png' ></td><td align=center>LIVE <br>5 | 4</td><td><img width=86px src='logos/Detroit-Tigers.png' ></td></tr><tr valign=top><td align=center>Astros (Live)</td><td></td><td align=center>Tigers (Live)</td></tr></table>\">Astr 5 - 4 Tige</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/MLB.png' ></td><td align=center>LIVE <br>0 | 1</td><td><img width=86px src='logos/MLB.png' ></td></tr><tr valign=top><td align=center>Yes Astros/Tigers score 2nd Inn</td><td></td><td align=center>No Astros/Tigers score 2nd Inn</td></tr></table>\">Yes 0 - 1 No A</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/MLB.png' ></td><td align=center>LIVE <br>1 | 0</td><td><img width=86px src='logos/MLB.png' ></td></tr><tr valign=top><td align=center>Yes Astros/Tigers score 3rd Inn</td><td></td><td align=center>No Astros/Tigers score 3rd Inn</td></tr></table>\">Yes 1 - 0 No A</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/MLB.png' ></td><td align=center>LIVE <br>1 | 0</td><td><img width=86px src='logos/MLB.png' ></td></tr><tr valign=top><td align=center>Yes Astros/Tigers score 4th Inn</td><td></td><td align=center>No Astros/Tigers score 4th Inn</td></tr></table>\">Yes 1 - 0 No A</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/MLB.png' ></td><td align=center>LIVE <br>1 | 0</td><td><img width=86px src='logos/MLB.png' ></td></tr><tr valign=top><td align=center>Yes Astros/Tigers score 5th Inn</td><td></td><td align=center>No Astros/Tigers score 5th Inn</td></tr></table>\">Yes 1 - 0 No A</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/MLB.png' ></td><td align=center>LIVE <br>0 | 1</td><td><img width=86px src='logos/MLB.png' ></td></tr><tr valign=top><td align=center>Yes Astros/Tigers score 6th Inn</td><td></td><td align=center>No Astros/Tigers score 6th Inn</td></tr></table>\">Yes 0 - 1 No A</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/MLB.png' ></td><td align=center>LIVE <br>1 | 0</td><td><img width=86px src='logos/MLB.png' ></td></tr><tr valign=top><td align=center>Yes Astros/Tigers score 7th Inn</td><td></td><td align=center>No Astros/Tigers score 7th Inn</td></tr></table>\">Yes 1 - 0 No A</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/MLB.png' ></td><td align=center>LIVE <br>0 | 1</td><td><img width=86px src='logos/MLB.png' ></td></tr><tr valign=top><td align=center>Yes Astros/Tigers score 8th Inn</td><td></td><td align=center>No Astros/Tigers score 8th Inn</td></tr></table>\">Yes 0 - 1 No A</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/Cleveland-Indians.png' ></td><td align=center>LIVE <br>1 | 3</td><td><img width=86px src='logos/Tampa-Bay-Rays.png' ></td></tr><tr valign=top><td align=center>Indians (Live)</td><td></td><td align=center>Rays (Live)</td></tr></table>\">Indi 1 - 3 Rays</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/Pittsburgh-Pirates.png' ></td><td align=center>LIVE <br>4 | 3</td><td><img width=86px src='logos/St.-Louis-Cardinals.png' ></td></tr><tr valign=top><td align=center>Pirates (Live)</td><td></td><td align=center>Cardinals (Live)</td></tr></table>\">Pira 4 - 3 Card</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/MLB.png' ></td><td align=center>LIVE <br>0 | 1</td><td><img width=86px src='logos/MLB.png' ></td></tr><tr valign=top><td align=center>Yes Braves/Giants score 2nd Inn</td><td></td><td align=center>No Braves/Giants score 2nd Inn</td></tr></table>\">Yes 0 - 1 No B</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/MLB.png' ></td><td align=center>LIVE <br>1 | 0</td><td><img width=86px src='logos/MLB.png' ></td></tr><tr valign=top><td align=center>Yes Braves/Giants score 3rd Inn</td><td></td><td align=center>No Braves/Giants score 3rd Inn</td></tr></table>\">Yes 1 - 0 No B</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/MLB.png' ></td><td align=center>LIVE <br>0 | 1</td><td><img width=86px src='logos/MLB.png' ></td></tr><tr valign=top><td align=center>Yes Braves/Giants score 4th Inn</td><td></td><td align=center>No Braves/Giants score 4th Inn</td></tr></table>\">Yes 0 - 1 No B</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/MLB.png' ></td><td align=center>LIVE <br>0 | 1</td><td><img width=86px src='logos/MLB.png' ></td></tr><tr valign=top><td align=center>Yes Braves/Giants score 5th Inn</td><td></td><td align=center>No Braves/Giants score 5th Inn</td></tr></table>\">Yes 0 - 1 No B</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/MLB.png' ></td><td align=center>LIVE <br>1 | 0</td><td><img width=86px src='logos/MLB.png' ></td></tr><tr valign=top><td align=center>Yes Braves/Giants score 6th Inn</td><td></td><td align=center>No Braves/Giants score 6th Inn</td></tr></table>\">Yes 1 - 0 No B</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/Campbell Fighting Camels.png' ></td><td align=center>Football <br>21 | 58</td><td><img width=86px src='logos/Coastal Carolina Chanticleers.png' ></td></tr><tr valign=top><td align=center>Campbell</td><td></td><td align=center>Coastal Carolina</td></tr></table>\">Camp 21 - 58 Coas</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/Maryland Terrapins.png' ></td><td align=center>Football <br>null | null</td><td><img width=86px src='logos/Maryland Terrapins.png' ></td></tr><tr valign=top><td align=center>Maryland RSW</td><td></td><td align=center>Maryland RSW.</td></tr></table>\">Mary null - null Mary</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/Los Angeles Dodgers.png' ></td><td align=center>Baseball <br>8 | 1</td><td><img width=86px src='logos/Cincinnati-Reds.png' ></td></tr><tr valign=top><td align=center>Los Angeles Dodgers</td><td></td><td align=center>Cincinnati Reds</td></tr></table>\">Los 8 - 1 Cinc</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/Pittsburgh-Pirates.png' ></td><td align=center>Baseball <br>4 | 3</td><td><img width=86px src='logos/St.-Louis-Cardinals.png' ></td></tr><tr valign=top><td align=center>Pittsburgh Pirates</td><td></td><td align=center>Saint Louis Cardinals</td></tr></table>\">Pitt 4 - 3 Sain</a></li> </ul> </div> <span class=\"fade fade-left2\">&nbsp;</span> </div> <div id=\"trends\" align=\"left\"> <div class=\"inner\"> <ul class=\"trendscontent\" style=\"zoom: 1;position:\"> <li class=\"trend-label\">&nbsp;&nbsp;</li> <li> <a href=\"#\" class=\"search_link\" name=\"\" rel=\"nofollow\">No news available...</a> </li> </ul> </div> <span class=\"fade fade-left\">&nbsp;</span><span class=\"fade fade-right\">&nbsp;</span> </div> <div style=\"width:100%\"> <div id=\"score_popup\" style=\"display:none;\"> </div> </div> </td> </tr> </table> <table cellspacing=\"0\" border=\"0\" width=\"100%\" align=\"left\"> <tr valign=\"top\"> <td style=\" width:100%\" > <div id=\"contentright\"> <div class=\"postP\"> <div style=\" padding:3px; height:100%\"><div style='background-color:#fff; padding:3px; width:86.3%; margin:auto auto; text-align:center' align='center'><div class='offering_pair'><table cellspacing='0' cellpadding='0' class='tktable' style='width:100%' ><tr><th class=sptype>Wager type : Straight Bet </th></tr></table><table cellspacing='0' cellpadding='0' class='listingtable' ><TR class='offering_noun'><TD width=100% style='border-bottom:1px solid #333333; padding:3px' ><table cellspacing='0' cellpadding='0' width=100% ><TR class='gamenumb'><TD width=15% >MLB</td><td><table cellpadding='2' cellspacing='2' ><tr valign='mdddle'><td align='center' style='border-top:transparent;padding:2px;font-weight:bold; font-size:13px'>907</td><td align='left' style='border-top:transparent;padding:2px'><img width=\"43px\" src=\"logos/Washington-Nationals.png\" class=\"logo1\" ></td><td align='left' style='border-top:transparent;padding:2px'><span class=thedate3 >9/12/2018 7:05 PM - (EST)</span><br />"
//					+ "<span class=rotnumb >Miami Marlins Money Line</span>"
//					+ "<span class=rotnumb > +105 for 1st 5 Innings   <br>J. Arrieta - R - Action&nbsp;&nbsp;&nbsp;T. Richards - R - Action </span>"
//					+ "<span class=rotnumb >Cincinnati/Miami Ohio Ov 51</span>"
//					+ "<span class=rotnumb > -110 for Game   </span>"
//					+ "<span class=rotnumb >Washington Nationals +1&#189;</span>"
//					+ "<span class=rotnumb > -185 for Game <br>S. Strasburg - R must Start&nbsp;&nbsp;A. Nola - R must Start </span>
//					+ "</td></tr></table> </td><td align=right><span class='gamenumb'><um>Risking</um> <span style='color:#FF0000;font-weight:bold; font-size:14px'>925.00 USD</span> <um>To Win</um> <span style='color:#000000;font-weight:bold; font-size:14px'>500.00 USD</span> </span></TD></TR><tr><td colspan=3><TABLE cellspacing='0' cellpadding='0' class=tktable><TR ><TD width=269px style='border-top:1px solid transparent' ><a href='StraightVerifyWager.php?delete=S1_2' class='deleteSpecial' ><img border='0' src='/Qubic/qubic/images/deleteIcon.gif'></a><span class='tits'>&nbsp;&nbsp; Internet Max Wager: 3000.00</span></TD><TD style='border-top:1px solid transparent'><span class='tits'><INPUT type='radio' onclick=javascript:goto('StraightVerifyWager.php?modify=true&radioxGame0=riskType') id='radiox' name='radioxGame0' value='riskType'>&nbsp;<um>Risk Amount</um>&nbsp;&nbsp;<INPUT type='radio' onclick=javascript:goto('StraightVerifyWager.php?modify=true&radioxGame0=toWinType') id='radiox' name='radioxGame0' value='toWinType' CHECKED>&nbsp;<um>To Win Amount</um>&nbsp;&nbsp;&nbsp;&nbsp; : 500.0</span></TD></TR></TABLE></td></tr></TABLE></td></tr></table><div class='offering_noun' width=\"100%\" style=\"background-color:#CC0000; color:#FFFFFF;font-size:14px;padding:3px;font-weight:bold;border:1px solid #000\">One or more lines have changed</div></div><div align=\"center\" style=\"background-color:#CCCCCC; padding:5px; border:1px solid #666666;width: 835px;margin-left: auto;margin-right: auto;\"><div class=\"block-content_box_review\"><FORM name=\"WagerVerificationForm\" method=\"post\" action=\"CheckAcceptancePassword.php\"><INPUT type=\"hidden\" id=inetWagerNumber name=inetWagerNumber value=\"0.7973606700850556\"><BR><BR>Please Review Wagers Carefully! &amp; Re-enter Password To Confirm Wagers </FONT><br /><INPUT type=\"password\" id=password class=input_login name=password size = 8>&nbsp;&nbsp;<INPUT type=\"submit\" value=\"Submit\" id=submit1 name=submit1></FORM><script type='text/javascript'>document.getElementById('password').focus();</script><BR><FORM name=\"Cancel\" method=\"post\" action=\"Error.php?err=41\"><INPUT type=\"submit\" value=\"Cancel\" id=\"cancel\" name=\"cancel\" style=\"margin-top:5px\"></FORM></div></div></table></div> </div> </div> </div> </td> </tr> </table> </div> </div> <div id=\"new_footer\"> <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"float:left;background-image:url(/Qubic/images/images_topbar/images_content/bg_footer.png); background-repeat:no-repeat; border-bottom:2px solid #D6D6D6;\" > <tr valign=\"bottom\"> <td valign=\"top\" style=\"float:right; padding:10px 0 0 0\"> <img src=\"images/images_topbar/images_content/phone-symbol.png\" class=\"phone_img\"> <span class=\"phone_img_text\" style=\"color:#FFF\"> <um>Wagering Phone</um></span> </td> <td style=\" padding:2px 0 0 70px;\"> <img src=\"images/images_topbar/images_content/settings_icon.png\" style=\"cursor:pointer; \" onClick=\"javascript:loadSettings();\" class=\"settings_icon\"> </td> <td valign=\"middle\" > <img src=\"images/images_topbar/images_content/separador.png\" height=\"20px\" style=\"margin:0 30px 0 0\"> </td> <td style=\" padding:2px 0 0 0;\"> <a href=\"Rules.php\"><img src=\"images/images_topbar/images_content/rules_icon.png\" class=\"rules_icon\"></a> </td> <td valign=\"middle\" > <img src=\"images/images_topbar/images_content/separador.png\" height=\"20px\" style=\"margin:0 30px 0 30px\"> </td> <td style=\" padding:5px 0 0 0;\"> </td> <td align=\"center\" style=\" padding:0 20px 0 0;\" valign=\"middle\" class=\"copyright\"> All Rights Reserved.<br />Copyright 2018. </td> </tr> </table> <div style=\"height:15px\">&nbsp;</div> </div> </body> </html> ";
//			String xhtml = "<!DOCTYPE HTML PUBLIC \"-/W3C/DTD XHTML 1.0 Transitional/EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"> <html> <HEAD> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"> <meta name=\"description\" content=\"Bet with Us\"> <!--<meta name=\"google-translate-customization\" content=\"850af3aab9df52c0-1111978e695f2729-g87703a3f0fd07b91-d\"></meta>--> <link rel=\"stylesheet\" type=\"text/css\" href=\"/Qubic/images/themes/theme1/theme1.css?v=86\"> <link rel=\"stylesheet\" type=\"text/css\" href=\"/Qubic/qubic/scripts/coverflow/coverflow.css?v=86\"> <link rel=\"stylesheet\" type=\"text/css\" href=\"/Qubic/css/commun.css?version=86\"> <!--<link rel=\"stylesheet/less\" type=\"text/css\" href=\"http://sportswidgets.pro/app/app.less?v=2&version=86\" />--> <link rel=\"stylesheet\" type=\"text/css\" href=\"/Qubic/qubic/scripts/tooltipster/tooltipster.css?version=86\" /> <script type=\"text/javascript\" src=\"/Qubic/qubic/scripts/fancybox/jquery-1.4.2.min.js?version=86\"></script> <script type=\"text/javascript\" src=\"/Qubic/qubic/scripts/fancybox/jquery.mousewheel-3.0.4.pack.js?version=86\"></script> <script type=\"text/javascript\" src=\"/Qubic/qubic/scripts/fancybox/jquery.fancybox-1.3.4.js?version=86\"></script> <script type=\"text/javascript\" src=\"/Qubic/qubic/scripts/autosugest/autosuggest.js?version=86\"></script> <script type=\"text/javascript\" src=\"/Qubic/qubic/scripts/autosugest/autosuggestinit.js?version=86\"></script> <script type=\"text/javascript\" src=\"/Qubic/scripts/CommonJavaScript.js?version=86\"></script> <script type=\"text/javascript\" src=\"/Qubic/qubic/scripts/tooltipster/jquery.tooltipster.js?version=86\"></script> <script type=\"text/javascript\" src=\"/Qubic/qubic/scripts/ticker/facescroll.js?version=86\"></script> <script type=\"text/javascript\" src=\"/Qubic/qubic/scripts/ticker/ticker.js?version=86\"></script> <script type=\"text/javascript\" src=\"/Qubic/qubic/scripts/countdown/countdown.js?version=86\"></script> <script type=\"text/javascript\" src=\"/Qubic/qubic/scripts/ticker/jquery-ui.min.js?version=86\"></script> <link rel=\"stylesheet\" type=\"text/css\" href=\"/Qubic/qubic/scripts/fancybox/jquery.fancybox-1.3.4.css?version=86\" media=\"screen\" /> <title>JNN286 | Home</title> <script type=\"text/javascript\"> function loadSettings(){ $.fancybox.showActivity(); $.ajax({ type : \"GET\", cache : false, url : \"PlayerSettings.php\", data : \"ip=1\", success: function(data) { $.fancybox(data); } }); return false; } function loadNewFeatures(){ $.fancybox.showActivity(); $.ajax({ type : \"GET\", cache : false, url : \"Newfeatures.php\", data : \"ip=1\", success: function(data) { $.fancybox(data); } }); return false; } </script> <!--[if IE]> <style> #trends .inner{ width:567px !important; overflow:hidden !important; zoom:1 !important; position:relative !important; margin-left:284px !important; top:0px !important; } .tooltipster-default { border-radius: 5px; border: 1px solid #362D2F; background: transparent #000; color: #fff; width:380px; height:90px; background-color:#000; opacity:0.8; filter:alpha(opacity=80); /* For IE8 and earlier */ } </style> <![endif]--> <!--[if lte IE 6]> <script src=\"/Qubic/qubic/scripts/pngfixie6/DD_belatedPNG_0.0.8a-min.js\"></script> <![endif]--> <script type=\"text/javascript\"> $(document).ready(function() { var page={}; $(function() { new FrontPage().init(); }); $(function() { $('.tooltip').tooltipster({ animation: 'grow', position: 'bottom' }); $('.limitstooltip').tooltipster({ animation: 'grow', position: 'top' }); }); $('.inner2').vTicker({ speed: 500, pause: 3000, showItems: 3, animation: 'fade', mousePause: true, height: 0, direction: 'up' }); page.trendDescriptions = {}; loadTrendDescriptions(); var badBrowser = (/MSIE ((5\\.5)|6)/.test(navigator.userAgent) && navigator.platform == \"Win32\"); if (badBrowser) { DD_belatedPNG.fix('img'); $('body').supersleight(); } hideTickerFast() }); </script> <SCRIPT ID=clientEventHandlersJS LANGUAGE=javascript>function goto(page){ window.location.href=page;}//--></SCRIPT></HEAD><BODY onScroll=\"if (typeof balance != 'undefined' )scrollAdj(balance)\"> <div style=\"position:relative; top:0; padding:3px; width:987px; margin:auto auto; text-align:center;\" align=\"center\"> <div id=\"newfeatures\" align=\"center\">New Features <a id=\"newfeaureslink\" style=\"color:#FD1342; cursor:pointer\" onclick=\"loadNewFeatures()\">(click here)</a></div> <div id=\"ticker_system\" class=\"fbChatSidebar\"> <div id=\"vertical-ticker\" > <table cellpadding=\"0\" cellspacing=\"0\"><tr valign=\"top\"><td onclick=\"showTicker();\" style=\"cursor:pointer\"> <li> <div class='desc' style='width:300px; height:12px;'><div style='font-size:12px;font-size-adjust: 0.5; width:100%; overflow-x:hidden; overflow-y:auto' >No New Messages</div></div> </li> </td></tr> <tr><td align=\"center\" style=\" background-color:#000; position:relative; cursor:pointer; background-image:url(/qubic/scripts/countdown/img/scroll.png); background-size: 30px 8px; height:8px; background-repeat:no-repeat; text-align:center; background-position:center \" onclick=\"showTicker();\"></td></tr> </table> </div> </div> <div id=\"google_language_drop\"> <div id=\"google_translate_element\" class=\"-blank\"> <select name=\"language\" onChange=\"javascript:window.location=window.location.href.split('?')[0]+'?lang=' + this.value\"> <option value=\"English\"selected >English</option><option value=\"Spanish\" >Spanish</option><option value=\"Chinese (Simp)\" >Chinese (Simp)</option><option value=\"French\" >French</option><option value=\"Korean\" >Korean</option><option value=\"Japanese\" >Japanese</option><option value=\"Vietnamese\" >Vietnamese</option> </select> </div> </div> <!--myluckybuck block--> <!--end myluckybuck block--> </div> <!-- End TranslateThis Button --> <script type=\"text/javascript\"> var estadoSlinkS=1; function newFeaturesLinkS(){ if(estadoSlinkS==1){ document.getElementById(\"newfeaureslink\").style.color=\"#fff\"; estadoSlinkS=0; } else{ estadoSlinkS=1; document.getElementById(\"newfeaureslink\").style.color=\"#FD1342\"; } } setInterval(\"newFeaturesLinkS()\",1000); </script> <div id=\"wrapper\" class=\"fix-wrapper\"> <table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" > <tr> <td id=\"clientName\" nowrap=\"nowrap\" style=\"text-align:center\" >&nbsp;<um><strong>Account:</strong></um>&nbsp;<strong>JNN286</strong> </td> <td nowrap=\"nowrap\" id=\"currentBalanceField\" style=\"text-align:center;\">&nbsp;<um><strong>Current Balance:</strong></um>&nbsp; <font color=green>USD&nbsp;2,560</font>&nbsp; </td> <td nowrap=\"nowrap\" id=\"totalPendingField\" style=\"text-align:center\">&nbsp;<a href=\"SpecificPlayerPendingWagers.php\"><um><strong><font color=\"white\" size=\"2px\">Total Pending:</strong></um>&nbsp; USD&nbsp;6,780.00&nbsp;</font></a> </td> <td nowrap=\"nowrap\" id=\"currentAvailableField\" style=\"border-left:1px solid #fff; text-align:center\"> <strong>&nbsp;<um>Current Available:</um>&nbsp;</strong> <span style=\"color:#00ADEE\"> USD&nbsp;20,780.0 &nbsp; </span> </td> <td nowrap=\"nowrap\" id=\"logoutField\" style=\"text-align:center\"> <Strong> &nbsp; <a href=\"LogoutSucessful.php\" style=\"color:red; font-size:16px;\">Log Out</a> &nbsp; </Strong> </td> </tr> </table> <div class=\"pages\" style=\"margin-left:auto; margin-right:auto\"> <table id=\"table_menu\" width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-size:14px\"> <tr height=\"26px\" > <!--Straight cases--> <!--Straight activo--> <td class=\"menuSelection\" width=\"180px\" style=\"background-image:url(images/images_topbar/main-bar-blue-1px.png); background-repeat: repeat-x; cursor:pointer\" onmouseover=\"arrow0.style.backgroundPosition='0 -52px'\" onmouseout=\"arrow0.style.backgroundPosition='0 -52px'\"><A href=\"StraightSportSelection.php\" style=\"line-height: 26px;\"><center>Straight bets</center></a></td> <td width=\"17px\" style=\"background-image:url(images/images_topbar/main-bar-arrows.png); background-repeat:no-repeat; background-position:0 -52px;\" id=\"arrow0\">&nbsp;</td> <!--Teaser cases--> <!--Straight active && teaser over--> <td class=\"menuSelection\" width=\"180px\" style=\"background-image:url(images/images_topbar/main-bar-black-1px.png); background-repeat: repeat-x; cursor:pointer\" onmouseover=\"arrow1.style.backgroundPosition='0 -52px'; arrow0.style.backgroundPosition='0 -182px'\" onmouseout=\"arrow1.style.backgroundPosition='0 0'; arrow0.style.backgroundPosition='0 -52px'\" ><A href=\"TeaserTeaserSelect.php\" style=\"line-height: 26px;\"><center>Teasers</center></a></td> <td width=\"14px\" style=\"background-image:url(images/images_topbar/main-bar-arrows.png); background-repeat:no-repeat; background-position:0 0\" id=\"arrow1\">&nbsp;&nbsp;</td> <!--Parlay cases--> <!--parlay no active && teaser no active--> <td class=\"menuSelection\" width=\"180px\" style=\"background-image:url(images/images_topbar/main-bar-black-1px.png); background-repeat: repeat-x; cursor:pointer\" onmouseover=\"arrow2.style.backgroundPosition='0 -52px'; arrow1.style.backgroundPosition='0 -26px'\" onmouseout=\"arrow2.style.backgroundPosition='0 0'; arrow1.style.backgroundPosition='0 0'\"><A href=\"StraightSportSelection.php?wagerType=Parlay\" style=\"line-height: 26px;\"><center>Parlays/RR</center></a></td> <td width=\"14px\" style=\"background-image:url(images/images_topbar/main-bar-arrows.png); background-repeat:no-repeat; background-position:0 0\" id=\"arrow2\">&nbsp;&nbsp;</td> <!--If bets cases--> <td class=\"menuSelection\" width=\"180px\" style=\"background-image:url(images/images_topbar/main-bar-black-1px.png); background-repeat: repeat-x; cursor:pointer\" onmouseover=\"arrow3.style.backgroundPosition='0 -52px'; arrow2.style.backgroundPosition='0 -26px'\" onmouseout=\"arrow3.style.backgroundPosition='0 0'; arrow2.style.backgroundPosition='0 0'\"><A href=\"IfbetsSportSelection.php\" style=\"line-height: 26px;\"><center>If Bets</center></a></td> <td width=\"14px\" style=\"background-image:url(images/images_topbar/main-bar-arrows.png); background-repeat:no-repeat; background-position:0 0\" id=\"arrow3\">&nbsp;&nbsp;</td> <!--Action reverse cases--> <!--if bet active action reverse over--> <td class=\"menuSelection\" width=\"180px\" style=\"background-image:url(images/images_topbar/main-bar-black-1px.png); background-repeat: repeat-x; cursor:pointer\" onmouseover=\"arrow4.style.backgroundPosition='0 -156px'; arrow3.style.backgroundPosition='0 -26px'\" onmouseout=\"arrow4.style.backgroundPosition='0 -104px'; arrow3.style.backgroundPosition='0 0'\"><A href=\"StraightSportSelection.php?wagerType=Action%20Reverse\" style=\"line-height: 26px;\"><center>Action Reverse</center></a></td> <td width=\"14px\" style=\"background-image:url(images/images_topbar/main-bar-arrows.png); background-repeat:no-repeat; background-position:0 -104px\" id=\"arrow4\">&nbsp;&nbsp;</td> <!--Turbo props cases--> <td class=\"menuSelection\" id=\"navcasino\" width=\"250px\" style=\"background-image:url(images/images_topbar/main-bar-red-1px.png); background-repeat: repeat-x; cursor:pointer\" onmouseover=\"arrow5.style.backgroundPosition='0 -52px'; arrow4.style.backgroundPosition='0 -26px'\" onmouseout=\"arrow5.style.backgroundPosition='0 -78px'; arrow4.style.backgroundPosition='0 -104px'\"> <a href=\"http://live2.betlivelines.com/sports2/session_pre.php?to=https://dimebet.ag/Qubic/livebetting2.php\"><center>Dynamic Live Betting</center></a> </td> <!--<td width=\"14px\" style=\"background-image:url(images/images_topbar/main-bar-arrows.png); background-repeat:no-repeat; background-position:0 -78px\" id=\"arrow5\">&nbsp;&nbsp;</td>--> <!--horses Cases--> <td width=\"14px\" style=\"background-image:url(images/images_topbar/main-bar-arrows.png); background-repeat:no-repeat; background-position:0 -78px\" id=\"arrow5\">&nbsp;&nbsp;</td> <!--no props active && no casino active--> <td class=\"menuSelection\" width=\"180px\" style=\"background-image:url(images/images_topbar/main-bar-black-1px.png); background-repeat: repeat-x; cursor:pointer\" onmouseover=\"arrow6.style.backgroundPosition='0 -52px'; arrow5.style.backgroundPosition='0 -130px'\" onmouseout=\"arrow6.style.backgroundPosition='0 0'; arrow5.style.backgroundPosition='0 -78px'\"> <center style='color:#AAAAAA'></center> </td> <!--casino Cases--> <td width=\"14px\" style=\"background-image:url(images/images_topbar/main-bar-arrows.png); background-repeat:no-repeat; background-position:0 0\" id=\"arrow6\">&nbsp;&nbsp;</td> <!--<td class=\"menuSelection\" width=\"180px\" style=\"background-image:url(images/images_topbar/main-bar-black-1px.png); background-repeat: repeat-x; cursor:pointer\" onmouseover=\"arrow6.style.backgroundPosition='0 -26px'\" onmouseout=\"arrow6.style.backgroundPosition='0 0'\">--> <td id=\"navcasino\" class=\"menuSelection\" width=\"180px\" style=\"background-image:url(images/images_topbar/main-bar-black-1px.png); background-repeat: repeat-x; cursor:pointer\" onmouseover=\"arrow6.style.backgroundPosition='0 -26px'\" onmouseout=\"arrow6.style.backgroundPosition='0 0'\"> <A href=\"#\" style=\"line-height: 26px;\"><center style=\"color:#AAAAAA;\"></center></a> </td> </tr> </table> </div> <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\"> <tr> <td id=\"tdCategoriesContainer\"> <div class=\"categories\" style=\"margin:3px auto 0 auto;\"> <table width=\"928px\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-size:12px; background-image:url(images/images_topbar/options_fullbg.png); background-repeat:no-repeat;\"> <tr> <td id=\"categoriesHomeField\" style=\"text-align:center\"><a style=\"color:#fff\" href=\"StraightLoginSportSelection.php\"><center><div class=\"spriteHome\"><br></div>Home</center></a></td> <td style=\"border-left:1px solid #fff;\"></td> <td class=\"categoriesStandardFields\" style=\"text-align:center\" ><a style=\"color:#fff\" href=\"Scores.php\"><center><div class=\"spriteScores\"><br></div>Scores</center></a></td> <td style=\"border-left:1px solid #fff;\"></td> <!--<td class=\"categoriesStandardFields\" style=\"text-align:center\"><a style=\"color:#fff\" href=\"PlayerTransactions.php?transactionListDays=1\"><center><div class=\"spriteReports\"><br></div>Reports</center></a></td>--> <td style=\"border-left:1px solid #fff;\"></td> <td class=\"categoriesStandardFields\" style=\"text-align:center\"><a style=\"color:#fff\" href=\"SpecificPlayerPendingWagers.php\" ><center><div class=\"spritePending\" ><br></div><div id=\"pendingWagersNumberE\"><strong><center>13</center></strong></div><span style=\"padding:0 0 0 15px\">Pending Wagers </span></center></a></td> <td style=\"border-left:1px solid #fff;\"></td> <td class=\"categoriesStandardFields\" style=\"text-align:center\"><center><a style=\"color:#fff\" href=\"PlayerDailyFigure.php\"><div class=\"spriteDailyFigure\" ><br></div><span>Daily Figures</span></center></a></td> <td style=\"border-left:1px solid #fff;\"></td> <td class=\"categoriesStandardFields\" style=\"text-align:center\"><center><a style=\"color:#fff\" href=\"SpecificPlayerWeeklyFigure.php\"><div class=\"spriteWeeklyFigure\" ><br></div><span>Graded Wagers</span></center></a></td> <td style=\"border-left:1px solid #fff;\"></td> <td id=\"categoriesTransactionsField\" style=\"text-align:center\"><center><a style=\"color:#fff\" href=\"SpecificPlayerTransactions.php\"><div class=\"spriteTransaction\" ><br></div><span>Transactions</span></center></a></td> <td style=\"border-left:1px solid #fff;\"></td> <td class=\"categoriesStandardFields\" style=\"text-align:center; padding:0 0 0 0\"><center><a style=\"color:#fff\" href=\"PanelMessage.php\" ><div class=\"spriteMessages\" ><br></div><div id=\"messagesNumberE\" ><strong><center> 0 </center></strong></div><span>Messages</span></center></a></td> <td style=\"border-left:1px solid #fff;\"></td> <td class=\"categoriesStandardFields\" style=\"text-align:center; padding:0 20px 0 0\"> <center><a style=\"color:#fff\" href=\"PlayerPanic.php\"><div class=\"spriteIconBoss\" ><br></div><span>Boss</span></center></a> </td> </tr> </table> </div> </td> </tr> <tr> <td align=\"left\"> <div id=\"trends2\" class=\"container\" align=\"left\" > <div id=\"scores_tool\" class=\"inner2\" style=\"height:31px!important\"> <span class=\"trend-label2\">Result:</span> <span class=\"trend-label2\" style=\"position:relative; left:95px\">NEWS:</span> <ul id=\"ul_scores\" class=\"trendscontent2\" style=\"zoom: 1;position:relative;top:0px\"> <li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/Los Angeles Dodgers.png' ></td><td align=center>LIVE <br>8 | 1</td><td><img width=86px src='logos/Cincinnati-Reds.png' ></td></tr><tr valign=top><td align=center>Dodgers (Live)</td><td></td><td align=center>Reds (Live)</td></tr></table>\">Dodg 8 - 1 Reds</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/Houston-Astros.png' ></td><td align=center>LIVE <br>5 | 4</td><td><img width=86px src='logos/Detroit-Tigers.png' ></td></tr><tr valign=top><td align=center>Astros (Live)</td><td></td><td align=center>Tigers (Live)</td></tr></table>\">Astr 5 - 4 Tige</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/MLB.png' ></td><td align=center>LIVE <br>0 | 1</td><td><img width=86px src='logos/MLB.png' ></td></tr><tr valign=top><td align=center>Yes Astros/Tigers score 2nd Inn</td><td></td><td align=center>No Astros/Tigers score 2nd Inn</td></tr></table>\">Yes 0 - 1 No A</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/MLB.png' ></td><td align=center>LIVE <br>1 | 0</td><td><img width=86px src='logos/MLB.png' ></td></tr><tr valign=top><td align=center>Yes Astros/Tigers score 3rd Inn</td><td></td><td align=center>No Astros/Tigers score 3rd Inn</td></tr></table>\">Yes 1 - 0 No A</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/MLB.png' ></td><td align=center>LIVE <br>1 | 0</td><td><img width=86px src='logos/MLB.png' ></td></tr><tr valign=top><td align=center>Yes Astros/Tigers score 4th Inn</td><td></td><td align=center>No Astros/Tigers score 4th Inn</td></tr></table>\">Yes 1 - 0 No A</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/MLB.png' ></td><td align=center>LIVE <br>1 | 0</td><td><img width=86px src='logos/MLB.png' ></td></tr><tr valign=top><td align=center>Yes Astros/Tigers score 5th Inn</td><td></td><td align=center>No Astros/Tigers score 5th Inn</td></tr></table>\">Yes 1 - 0 No A</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/MLB.png' ></td><td align=center>LIVE <br>0 | 1</td><td><img width=86px src='logos/MLB.png' ></td></tr><tr valign=top><td align=center>Yes Astros/Tigers score 6th Inn</td><td></td><td align=center>No Astros/Tigers score 6th Inn</td></tr></table>\">Yes 0 - 1 No A</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/MLB.png' ></td><td align=center>LIVE <br>1 | 0</td><td><img width=86px src='logos/MLB.png' ></td></tr><tr valign=top><td align=center>Yes Astros/Tigers score 7th Inn</td><td></td><td align=center>No Astros/Tigers score 7th Inn</td></tr></table>\">Yes 1 - 0 No A</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/MLB.png' ></td><td align=center>LIVE <br>0 | 1</td><td><img width=86px src='logos/MLB.png' ></td></tr><tr valign=top><td align=center>Yes Astros/Tigers score 8th Inn</td><td></td><td align=center>No Astros/Tigers score 8th Inn</td></tr></table>\">Yes 0 - 1 No A</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/Cleveland-Indians.png' ></td><td align=center>LIVE <br>1 | 3</td><td><img width=86px src='logos/Tampa-Bay-Rays.png' ></td></tr><tr valign=top><td align=center>Indians (Live)</td><td></td><td align=center>Rays (Live)</td></tr></table>\">Indi 1 - 3 Rays</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/Pittsburgh-Pirates.png' ></td><td align=center>LIVE <br>4 | 3</td><td><img width=86px src='logos/St.-Louis-Cardinals.png' ></td></tr><tr valign=top><td align=center>Pirates (Live)</td><td></td><td align=center>Cardinals (Live)</td></tr></table>\">Pira 4 - 3 Card</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/MLB.png' ></td><td align=center>LIVE <br>0 | 1</td><td><img width=86px src='logos/MLB.png' ></td></tr><tr valign=top><td align=center>Yes Braves/Giants score 2nd Inn</td><td></td><td align=center>No Braves/Giants score 2nd Inn</td></tr></table>\">Yes 0 - 1 No B</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/MLB.png' ></td><td align=center>LIVE <br>1 | 0</td><td><img width=86px src='logos/MLB.png' ></td></tr><tr valign=top><td align=center>Yes Braves/Giants score 3rd Inn</td><td></td><td align=center>No Braves/Giants score 3rd Inn</td></tr></table>\">Yes 1 - 0 No B</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/MLB.png' ></td><td align=center>LIVE <br>0 | 1</td><td><img width=86px src='logos/MLB.png' ></td></tr><tr valign=top><td align=center>Yes Braves/Giants score 4th Inn</td><td></td><td align=center>No Braves/Giants score 4th Inn</td></tr></table>\">Yes 0 - 1 No B</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/MLB.png' ></td><td align=center>LIVE <br>0 | 1</td><td><img width=86px src='logos/MLB.png' ></td></tr><tr valign=top><td align=center>Yes Braves/Giants score 5th Inn</td><td></td><td align=center>No Braves/Giants score 5th Inn</td></tr></table>\">Yes 0 - 1 No B</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/MLB.png' ></td><td align=center>LIVE <br>1 | 0</td><td><img width=86px src='logos/MLB.png' ></td></tr><tr valign=top><td align=center>Yes Braves/Giants score 6th Inn</td><td></td><td align=center>No Braves/Giants score 6th Inn</td></tr></table>\">Yes 1 - 0 No B</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/Campbell Fighting Camels.png' ></td><td align=center>Football <br>21 | 58</td><td><img width=86px src='logos/Coastal Carolina Chanticleers.png' ></td></tr><tr valign=top><td align=center>Campbell</td><td></td><td align=center>Coastal Carolina</td></tr></table>\">Camp 21 - 58 Coas</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/Maryland Terrapins.png' ></td><td align=center>Football <br>null | null</td><td><img width=86px src='logos/Maryland Terrapins.png' ></td></tr><tr valign=top><td align=center>Maryland RSW</td><td></td><td align=center>Maryland RSW.</td></tr></table>\">Mary null - null Mary</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/Los Angeles Dodgers.png' ></td><td align=center>Baseball <br>8 | 1</td><td><img width=86px src='logos/Cincinnati-Reds.png' ></td></tr><tr valign=top><td align=center>Los Angeles Dodgers</td><td></td><td align=center>Cincinnati Reds</td></tr></table>\">Los 8 - 1 Cinc</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/Pittsburgh-Pirates.png' ></td><td align=center>Baseball <br>4 | 3</td><td><img width=86px src='logos/St.-Louis-Cardinals.png' ></td></tr><tr valign=top><td align=center>Pittsburgh Pirates</td><td></td><td align=center>Saint Louis Cardinals</td></tr></table>\">Pitt 4 - 3 Sain</a></li> </ul> </div> <span class=\"fade fade-left2\">&nbsp;</span> </div> <div id=\"trends\" align=\"left\"> <div class=\"inner\"> <ul class=\"trendscontent\" style=\"zoom: 1;position:\"> <li class=\"trend-label\">&nbsp;&nbsp;</li> <li> <a href=\"#\" class=\"search_link\" name=\"\" rel=\"nofollow\">No news available...</a> </li> </ul> </div> <span class=\"fade fade-left\">&nbsp;</span><span class=\"fade fade-right\">&nbsp;</span> </div> <div style=\"width:100%\"> <div id=\"score_popup\" style=\"display:none;\"> </div> </div> </td> </tr> </table> <table cellspacing=\"0\" border=\"0\" width=\"100%\" align=\"left\"> <tr valign=\"top\"> <td style=\" width:100%\" > <div id=\"contentright\"> <div class=\"postP\"> <div style=\" padding:3px; height:100%\"><div style='background-color:#fff; padding:3px; width:86.3%; margin:auto auto; text-align:center' align='center'><div class='offering_pair'><table cellspacing='0' cellpadding='0' class='tktable' style='width:100%' ><tr><th class=sptype>Wager type : Straight Bet </th></tr></table><table cellspacing='0' cellpadding='0' class='listingtable' ><TR class='offering_noun'><TD width=100% style='border-bottom:1px solid #333333; padding:3px' ><table cellspacing='0' cellpadding='0' width=100% ><TR class='gamenumb'><TD width=15% >MLB</td><td><table cellpadding='2' cellspacing='2' ><tr valign='mdddle'><td align='center' style='border-top:transparent;padding:2px;font-weight:bold; font-size:13px'>907</td><td align='left' style='border-top:transparent;padding:2px;font-weight:bold; font-size:13px'><img width=\"43px\" src=\"logos/Washington-Nationals.png\" class=\"logo1\" ></td><td align='left' style='border-top:transparent;padding:2px'><span class=thedate3 >9/12/2018 7:05 PM - (EST)</span><br /><span class=rotnumb >Washington Nationals Money Line</span><span class=rotnumb > +105 for 1st 5 Innings <br>S. Strasburg - R - Action&nbsp;&nbsp;&nbsp;A. Nola - R - Action </span></td></tr></table> </td><td align=right><span class='gamenumb'><um>Risking</um> <span style='color:#FF0000;font-weight:bold; font-size:14px'>500.00 USD</span> <um>To Win</um> <span style='color:#000000;font-weight:bold; font-size:14px'>525.00 USD</span> </span></TD></TR><tr><td colspan=3><TABLE cellspacing='0' cellpadding='0' class=tktable><TR ><TD width=269px style='border-top:1px solid transparent' ><a href='StraightVerifyWager.php?delete=M1_2' class='deleteSpecial' ><img border='0' src='/Qubic/qubic/images/deleteIcon.gif'></a><span class='tits'>&nbsp;&nbsp; Internet Max Wager: 10000000.00</span></TD><TD style='border-top:1px solid transparent'><span class='tits'><INPUT type='radio' onclick=javascript:goto('StraightVerifyWager.php?modify=true&radioxGame0=riskType') id='radiox' name='radioxGame0)' value='riskType' CHECKED>&nbsp;<um>Risk Amount</um>&nbsp;&nbsp;<INPUT type='radio' onclick=javascript:goto('StraightVerifyWager.php?modify=true&radioxGame0=toWinType') id='radiox' name='radioxGame0' value='toWinType'>&nbsp;<um>To Win Amount</um>&nbsp;&nbsp;&nbsp;&nbsp; : 500.0</span></TD></TR></TABLE></td></tr></TABLE></td></tr></table><div class='offering_noun' width=\"100%\" style=\"background-color:#CC0000; color:#FFFFFF;font-size:14px;padding:3px;font-weight:bold;border:1px solid #000\">One or more lines have changed</div></div><div align=\"center\" style=\"background-color:#CCCCCC; padding:5px; border:1px solid #666666;width: 835px;margin-left: auto;margin-right: auto;\"><div class=\"block-content_box_review\"><FORM name=\"WagerVerificationForm\" method=\"post\" action=\"CheckAcceptancePassword.php\"><INPUT type=\"hidden\" id=inetWagerNumber name=inetWagerNumber value=\"0.7105077676899534\"><BR><BR>Please Review Wagers Carefully! &amp; Re-enter Password To Confirm Wagers </FONT><br /><INPUT type=\"password\" id=password class=input_login name=password size = 8>&nbsp;&nbsp;<INPUT type=\"submit\" value=\"Submit\" id=submit1 name=submit1></FORM><script type='text/javascript'>document.getElementById('password').focus();</script><BR><FORM name=\"Cancel\" method=\"post\" action=\"Error.php?err=41\"><INPUT type=\"submit\" value=\"Cancel\" id=\"cancel\" name=\"cancel\" style=\"margin-top:5px\"></FORM></div></div></table></div> </div> </div> </div> </td> </tr> </table> </div> </div> <div id=\"new_footer\"> <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"float:left;background-image:url(/Qubic/images/images_topbar/images_content/bg_footer.png); background-repeat:no-repeat; border-bottom:2px solid #D6D6D6;\" > <tr valign=\"bottom\"> <td valign=\"top\" style=\"float:right; padding:10px 0 0 0\"> <img src=\"images/images_topbar/images_content/phone-symbol.png\" class=\"phone_img\"> <span class=\"phone_img_text\" style=\"color:#FFF\"> <um>Wagering Phone</um></span> </td> <td style=\" padding:2px 0 0 70px;\"> <img src=\"images/images_topbar/images_content/settings_icon.png\" style=\"cursor:pointer; \" onClick=\"javascript:loadSettings();\" class=\"settings_icon\"> </td> <td valign=\"middle\" > <img src=\"images/images_topbar/images_content/separador.png\" height=\"20px\" style=\"margin:0 30px 0 0\"> </td> <td style=\" padding:2px 0 0 0;\"> <a href=\"Rules.php\"><img src=\"images/images_topbar/images_content/rules_icon.png\" class=\"rules_icon\"></a> </td> <td valign=\"middle\" > <img src=\"images/images_topbar/images_content/separador.png\" height=\"20px\" style=\"margin:0 30px 0 30px\"> </td> <td style=\" padding:5px 0 0 0;\"> </td> <td align=\"center\" style=\" padding:0 20px 0 0;\" valign=\"middle\" class=\"copyright\"> All Rights Reserved.<br />Copyright 2018. </td> </tr> </table> <div style=\"height:15px\">&nbsp;</div> </div> </body> </html>";
//			Map<String, String> linechanges = betBuckeyeParser.processLineChange(xhtml);
//			LOGGER.error("LineChanges: " + linechanges);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseIndex(java.lang.String)
	 */
	@Override
	public Map<String, String> parseIndex(String xhtml) throws BatchException {
		LOGGER.info("Entering parseIndex()");
		final Map<String, String> map = new HashMap<String, String>();

		// Get the document object
		final Document doc = parseXhtml(xhtml);

		// Get all input fields
		getAllElementsByName(doc, "input", "value", map);

		// Get form action field
		map.put("action", getElementByName(doc, "form", "action"));

		LOGGER.info("Entering parseIndex()");
		return map;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseLogin(java.lang.String)
	 */
	@Override
	public Map<String, String> parseLogin(String xhtml) throws BatchException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ticketadvantage.services.dao.sites.SiteParser#parseMenu(java.lang.
	 * String, java.lang.String[], java.lang.String[])
	 */
	@Override
	public Map<String, String> parseMenu(String xhtml, String[] type, String[] sport) throws BatchException {
		LOGGER.info("Entering parseMenu()");
		LOGGER.debug("types: " + java.util.Arrays.toString(type));
		LOGGER.debug("sport: " + java.util.Arrays.toString(sport));
		Map<String, String> map = new HashMap<String, String>();

		// Parse XHTML
		final Document doc = parseXhtml(xhtml);

		// Get hidden input fields
		String[] types = new String[] { "hidden" };
		Element form = doc.getElementById("showlinesForm");
		if (form != null) {
			// Get form action field
			map.put("action", form.attr("action"));

			getAllElementsByType(form, "input", types, map);
			map = getAllElementsByNameForClass(doc, "table tbody tr td input", "value", map, type);
		} else {
			// Throw Exception
			throw new BatchException(BatchErrorCodes.XHTML_MENU_PARSING_EXCEPTION,
				BatchErrorMessage.XHTML_MENU_PARSING_EXCEPTION, "No form with id of showlinesForm in HTML", "<![CDATA[" + xhtml + "]]>");
		}

		LOGGER.info("Exiting parseMenu()");
		return map;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseGames(java.lang.String, java.lang.String, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <BetBuckeyeEventPackage> List<BetBuckeyeEventPackage> parseGames(String xhtml, String type, Map<String, String> inputFields) throws BatchException {
		LOGGER.info("Entering parseGames()");
		LOGGER.debug("type: " + type);

		List<?> events = null;
		final Document doc = parseXhtml(xhtml);
		
		// Get hidden input fields
		final String[] types = new String[] { "hidden", "textbox" };
		final Elements forms = doc.select("form");
		for (int z = 0;(forms != null && z < forms.size()); z++) {
			Element form = forms.get(z);
			if (form != null && "GameSelectionForm".equals(form.attr("name"))) {
				// Get form action field
				inputFields.put("action", form.attr("action"));

				getAllElementsByType(form, "input", types, inputFields);
				getAllSelects(form, inputFields);
			}
		}

		final Elements elements = doc.select("#mainTab table tbody tr td");
		LOGGER.debug("# of Records: " + elements.size());

		// Check for valid size
		if (elements != null && elements.size() > 0) {
			events = getGameData(elements);
		}

		LOGGER.info("Exiting parseGames()");
		return (List<BetBuckeyeEventPackage>)events;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseEventSelection(java.lang.String, com.ticketadvantage.services.dao.sites.SiteTeamPackage, java.lang.String)
	 */
	@Override
	public Map<String, String> parseEventSelection(String xhtml, SiteTeamPackage siteTeamPackage, String type) throws BatchException {
		LOGGER.info("Entering parseEventSelection()");
		final Map<String, String> map = new HashMap<String, String>();

		// Parse the XTHML
		final Document doc = parseXhtml(xhtml);

		// Check for a wager limit and change it 
		if (xhtml.contains("A maximum wager amount of ")) {
			int index = xhtml.indexOf("A maximum wager amount of ");
			int eindex = xhtml.indexOf(" USD is permitted based on your account");
			String error = xhtml.substring(index, eindex + " USD is permitted based on your account".length());

			throw new BatchException(BatchErrorCodes.MAX_WAGER_AMOUNT_REACHED,
					BatchErrorMessage.MAX_WAGER_AMOUNT_REACHED, error, xhtml);
		}
		
		// Check for a wager limit and change it 
		if (xhtml.contains("Customer has insufficient funds")) {
			throw new BatchException(BatchErrorCodes.ACCOUNT_BALANCE_LIMIT,
					BatchErrorMessage.ACCOUNT_BALANCE_LIMIT, "Customer has insufficient funds.", xhtml);
		}

		// Check for a line change
		if (xhtml.contains("One or more lines have changed")) {
			throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "Line Change", xhtml);
		}

		// Get hidden input fields
		final String[] types = new String[] {"hidden", "password", "submit" };
		final Elements forms = doc.select("form");
		for (int z = 0;(forms != null && z < forms.size()); z++) {
			final Element form = forms.get(z);
			if (form != null && "WagerVerificationForm".equals(form.attr("name"))) {
				// Get form action field
				map.put("action", form.attr("action"));

				getAllElementsByType(form, "input", types, map);
				getAllSelects(form, map);
			}
		}

		// Check for valid size
		if (map.size() == 0) {
			// Throw Exception
			throw new BatchException(BatchErrorCodes.XHTML_WAGER_PARSING_EXCEPTION,
				BatchErrorMessage.XHTML_WAGER_PARSING_EXCEPTION, "No form with name of WagerVerificationForm in HTML", "<![CDATA[" + xhtml + "]]>");
		}

		LOGGER.info("Exiting parseEventSelection()");
		return map;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseTicketNumber(java.lang.String)
	 */
	@Override
	public String parseTicketNumber(String xhtml) throws BatchException {
		LOGGER.info("Entering parseTicketNumber()");
		String ticketNumber = "Ticket Number - ";

		// Check for a line change
		if (xhtml.contains("One or more lines have changed")) {
			throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "Line Change", xhtml);
		}

		// First check for Wager has been accepted!
		if (xhtml != null && xhtml.contains("Wager has been accepted!")) {
			// Great, wager is complete; now get the Ticket Number
			int index = xhtml.indexOf(ticketNumber);
			if (index != -1) {
				final String nxhtml = xhtml.substring(index + ticketNumber.length());
				index = nxhtml.indexOf("</span>");
				if (index != -1) {
					ticketNumber = nxhtml.substring(0, index);
				} else {
					throw new BatchException(BatchErrorCodes.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, 
						BatchErrorMessage.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, "Error retrieving ticket number", xhtml);
				}
			} else {
				throw new BatchException(BatchErrorCodes.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, 
					BatchErrorMessage.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, "Error retrieving ticket number", xhtml);
			}
		} else {
			throw new BatchException(BatchErrorCodes.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, 
				BatchErrorMessage.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, "Error retrieving ticket number", xhtml);
		}

		LOGGER.info("Exiting parseTicketNumber()");
		return ticketNumber;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parsePendingBets(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Set<PendingEvent> parsePendingBets(String xhtml, String accountName, String accountId) throws BatchException {
		LOGGER.info("Entering parsePendingBets()");
		LOGGER.debug("accountName: " + accountName);
		LOGGER.debug("accountId: " + accountId);
		Set<PendingEvent> pendingEvents = new HashSet<PendingEvent>();

		// Get the document object
		final Document doc = parseXhtml(xhtml);
		final Elements trs = doc.select(".listingtableQubic tbody tr");
		LOGGER.debug("trs: " + trs);
		if (trs != null) {
			for (Element tr : trs) {
				PendingEvent pe = null;
				int x = 0;
				String className = tr.attr("class");
				if (className != null && className.contains("offering_")) {
					try {
						pe = new PendingEvent();
						Elements tds = tr.select("td");
						if (tds != null && tds.size() > 0) {
							LOGGER.debug("tds.size(): " + tds.size());
							for (Element td : tds) {
								LOGGER.debug("td: " + td);
								pe.setAccountname(accountName);
								pe.setAccountid(accountId);
								pe.setCustomerid(accountName);
								pe.setInet(accountName);
	
								if (tds.size() == 4) {
									switch (x++) {
										case 0:
											break;
										case 1:
											getNewEventInfo(td, pe);
											break;
										case 2:
											getRisk(td, pe);
											break;
										case 3:
											getWin(td, pe);
											break;
										default:
											break;
									}
								} else {
									switch (x++) {
										case 0:
											getTicketNumber(td, pe);
											break;
										case 1:
											getDateAccepted(td, pe);
											break;
										case 2:
											getEventType(td, pe);
											break;
										case 3:
											getEventInfo(td, pe);
											break;
										case 4:
											getStartDate(td, pe);
											break;
										case 5:
											getRisk(td, pe);
											break;
										case 6:
											getWin(td, pe);
											break;
										default:
											break;
									}
								}
							}
						}
						pe.setDoposturl(true);
						LOGGER.debug("PendingEvent: " + pe);

						// "Fix" the total rotation ID
						if (pe.getEventtype() != null && pe.getEventtype().equals("total")) {
							if (pe.getLineplusminus().equals("u")) {
								String rotationid = pe.getRotationid();
								try {
									Integer rotId = Integer.parseInt(rotationid);
									rotId = rotId + 1;
									pe.setRotationid(rotId.toString());
								} catch (Throwable t) {
									LOGGER.error(t.getMessage(), t);
								}
							}
						}

						// Setup the correct rotation IDs
						if (pe.getLinetype() != null && pe.getLinetype().equals("first")) {
							String rotId = pe.getRotationid();
							pe.setRotationid("1" + rotId);
						} else if (pe.getLinetype() != null && pe.getLinetype().equals("second")) {
							String rotId = pe.getRotationid();
							pe.setRotationid("2" + rotId);
						} else if (pe.getLinetype() != null && pe.getLinetype().equals("third")) {
							String rotId = pe.getRotationid();
							pe.setRotationid("3" + rotId);							
						}

						LOGGER.debug("PEXXX: " + pe);
						pendingEvents.add(pe);
					} catch (BatchException be) {
						LOGGER.warn(be.getMessage(), be);
						if (pe != null) {
							pe.setGamesport("");
							pe.setGametype("");
						}
					} catch (Throwable t) {
						LOGGER.warn(t.getMessage(), t);
						if (pe != null) {
							pe.setGamesport("");
							pe.setGametype("");
						}

						try {
							// Get the email access token so we can update the users
							final SendText sendText = new SendText();
							sendText.setOAUTH2_TOKEN(TICKETADVANTAGEGMAILOATH.getAccessToken());
							sendText.sendTextWithMessage("9132195234@vtext.com", "Error in pendingSite " + accountName + " " + accountId + " " + tr.html());
						} catch (Throwable tt) {
							LOGGER.warn(tt.getMessage(), tt);
						}
					}
				}
			}
		}

		// Size
		LOGGER.debug("pendingEvents.size(): " + pendingEvents.size());
		LOGGER.info("Exiting parsePendingBets()");
		return pendingEvents;
	}

	/**
	 * 
	 * @param xhtml
	 * @param pe
	 */
	private void parseLoadTicketData(String xhtml, PendingEvent pe) {
		LOGGER.info("Entering parseLoadTicketData()");

		// loadTicketdata(190259950, 1, 0, true);
		String url = "/TicketShowTicket.php?_=" + (Math.floor(Math.random() * 9000000000000L) + 1000000000000L);

		if (xhtml != null) {
			int ltdIndex = xhtml.indexOf("loadTicketdata(");
			if (ltdIndex != -1) {
				xhtml = xhtml.substring(ltdIndex + 15);
				if (xhtml != null) {
					int commaIndex = xhtml.indexOf(",");
					if (commaIndex != -1) {
						String tixNum = xhtml.substring(0, commaIndex).trim();
						url += "&ticketNumber=" + tixNum;
						xhtml = xhtml.substring(commaIndex + 1);
						commaIndex = xhtml.indexOf(",");
						if (commaIndex != -1) {
							String wagerNumber = xhtml.substring(0, commaIndex).trim();
							url += "&wagerNumber=" + wagerNumber;
							xhtml = xhtml.substring(commaIndex + 1);
							commaIndex = xhtml.indexOf(",");
							if (commaIndex != -1) {
								String gradeNumber = xhtml.substring(0, commaIndex).trim();
								url += "&gradeNumber=" + gradeNumber;
								xhtml = xhtml.substring(commaIndex + 1);
								commaIndex = xhtml.indexOf(");");
								if (commaIndex != -1) {
									String booleans = xhtml.substring(0, commaIndex);
									url += "&booleans=" + booleans.trim();
									pe.setPosturl(url);
									pe.setDoposturl(true);
								}
							}
						}
					}
				}
			}
		}

		LOGGER.info("Exiting parseLoadTicketData()");
	}

	/**
	 * 
	 * @param xhtml
	 * @return
	 * @throws BatchException
	 */
	public Map<String, String> parseWagerTypes(String xhtml) throws BatchException {
		LOGGER.info("Entering parseWagerTypes()");
		final Map<String, String> map = new HashMap<String, String>();

		// Check for a line change
		if (xhtml.contains("One or more lines have changed")) {
			throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "Line Change", xhtml);
		}

		// Parse the XTHML
		final Document doc = parseXhtml(xhtml);

		// Get the information for check list
		final Elements elements = doc.select(".tits input");
		for (int y = 0; (elements != null && y < elements.size()); y++) {
			final Element element = elements.get(y);
			if (element != null) {
				final String checked = element.attr("checked");
				final String value = element.attr("value");
				if (checked != null && checked.length() == 0) {
					map.put("wagerType", value);
				}

				// Onclick value
				String getUrl = null;
				final String onclick = element.attr("onclick");
				if (onclick != null) {
					int beginIndex = onclick.indexOf("javascript:goto('");
					int endIndex = onclick.indexOf("')");
					if (beginIndex != -1 && endIndex != -1) {
						getUrl = onclick.substring(beginIndex + "javascript:goto('".length(), endIndex);
					}
				}

				// Get the url's
				if (value != null && "riskType".equals(value)) {
					map.put("riskurl", getUrl);
				} else if (value != null && "toWinType".equals(value)) {
					map.put("winurl", getUrl);
				}
			}
		}

		// Check for a wager limit and change it 
		if (xhtml.contains("A maximum wager amount of ")) {
			int index = xhtml.indexOf("A maximum wager amount of ");
			if (index != -1) {
				xhtml = xhtml.substring(index + "A maximum wager amount of ".length());
				index = xhtml.indexOf("USD is permitted based on your account and wager type");
				if (index != -1) {
					String wagerAmount = xhtml.substring(0, index);
					if (wagerAmount != null) {
						wagerAmount = wagerAmount.replaceAll(",", "");
						wagerAmount = wagerAmount.replaceAll(" ", "");
						map.put("wageramount", wagerAmount);
					}
				}
			}
		}

		// Check for valid size
		if (map.size() == 0) {
			// Throw Exception
			throw new BatchException(BatchErrorCodes.XHTML_WAGER_PARSING_EXCEPTION,
				BatchErrorMessage.XHTML_WAGER_PARSING_EXCEPTION, "Cannot find wager type information in HTML", "<![CDATA[" + xhtml + "]]>");
		}

		LOGGER.info("Exiting parseWagerTypes()");
		return map;
	}

	/**
	 * 
	 * @param xhtml
	 * @return
	 */
	public Map<String, String> processLineChange(String xhtml) throws BatchException {
		LOGGER.info("Entering processLineChange()");
		final Map<String, String> map = new HashMap<String, String>();
		String originalXhtml = xhtml;

//		<span class=thedate3 >10/25/2017 7:05 PM -  (EST)</span><br />
//		<span class=rotnumb >Denver Nuggets -2</span>
//		<span class=rotnumb > -110 for Game   </span>

//		<span class=thedate3 >9/4/2018 7:10 PM -  (EST)</span><br />
//		<span class=rotnumb >Miami Marlins Money Line</span>
//		<span class=rotnumb > +105 for 1st 5 Innings   <br>J. Arrieta - R - Action&nbsp;&nbsp;&nbsp;T. Richards - R - Action </span>

//		<span class=rotnumb >Cincinnati/Miami Ohio Ov 51</span>
//		<span class=rotnumb > -110 for Game   </span></td>

//		<span class=rotnumb >Washington Nationals +1&#189;</span>
//		<span class=rotnumb > -185 for Game <br>S. Strasburg - R must Start&nbsp;&nbsp;A. Nola - R must Start </span>

		int index = xhtml.indexOf("<span class=rotnumb >");
		if (index != -1) {
			xhtml = xhtml.substring(index + "<span class=rotnumb >".length());
			if (xhtml.contains("Money Line")) {
				index = xhtml.indexOf("<span class=rotnumb >");
				if (index != -1) {
					xhtml = xhtml.substring(index + "<span class=rotnumb >".length()).trim();
					index = xhtml.indexOf(" for ");
					xhtml = xhtml.substring(0, index);
					map.put("valueindicator", xhtml.substring(0, 1));
					map.put("value", reformatValues(xhtml.substring(1)));
					map.put("juiceindicator", xhtml.substring(0, 1));
					map.put("juice", reformatValues(xhtml.substring(1)));
				}
			} else {
				index = xhtml.indexOf("</span>");
				if (index != -1) {
					String first = xhtml.substring(0, index);
					xhtml = xhtml.substring(index + 7);
					LOGGER.error("first: " + first);
					if (first.contains(" Ov ") || first.contains(" Un ")) {
						int ov = first.indexOf(" Ov ");
						if (ov != -1) {
							first = first.substring(ov + 4);
							map.put("valueindicator", "o");
							map.put("value", reformatValues(first));

							index = xhtml.indexOf("<span class=rotnumb >");
							if (index != -1) {
								xhtml = xhtml.substring(index + "<span class=rotnumb >".length());
								LOGGER.error("xhtml: " + xhtml);
								index = xhtml.indexOf(" for ");
								if (index != -1) {
									xhtml = xhtml.substring(0, index).trim();
									map.put("juiceindicator", xhtml.substring(0, 1));
									map.put("juice", reformatValues(xhtml.substring(1)));
								}
							}
						} else {
							int un = first.indexOf(" Un ");
							if (un != -1) {
								first = first.substring(un + 4);
								map.put("valueindicator", "u");
								map.put("value", reformatValues(first));

								index = xhtml.indexOf("<span class=rotnumb >");
								if (index != -1) {
									xhtml = xhtml.substring(index + "<span class=rotnumb >".length());
									LOGGER.error("xhtml: " + xhtml);
									index = xhtml.indexOf(" for ");
									if (index != -1) {
										xhtml = xhtml.substring(0, index).trim();
										map.put("juiceindicator", xhtml.substring(0, 1));
										map.put("juice", reformatValues(xhtml.substring(1)));
									}
								}
							}
						}
					} else {
						int last = first.lastIndexOf(" ");
						if (last != -1) {
							first = first.substring(last + 1).trim();
							LOGGER.error("first2: " + first);
							if (first.startsWith("+") || first.startsWith("-")) {
								map.put("valueindicator", first.substring(0, 1));
								map.put("value", reformatValues(first.substring(1)));
							}
							index = xhtml.indexOf("<span class=rotnumb >");
							if (index != -1) {
								xhtml = xhtml.substring(index + "<span class=rotnumb >".length());
								index = xhtml.indexOf(" for ");
								xhtml = xhtml.substring(0, index).trim();
								LOGGER.error("xhtml: " + xhtml);
								map.put("juiceindicator", xhtml.substring(0, 1));
								map.put("juice", reformatValues(xhtml.substring(1)));
							}
						}
					}
				}
			}
		}

		// Juice
		if (!map.containsKey("juiceindicator")) {
			map.put("juiceindicator", map.get("valueindicator"));
			map.put("juice", super.reformatValues(map.get("value")));			
		}

		// Parse the XTHML
		final Document doc = parseXhtml(originalXhtml);

		// Parse xhtml and get action
		final Elements forms = doc.select("form");
		LOGGER.error("forms.size(): " + forms.size());
		for (Element form : forms) {
			final String name = form.attr("name");
			if ("WagerVerificationForm".equals(name)) {		
				final String action = form.attr("action");
				map.put("action", action);
				final Element inetWagerNumber = form.getElementById("inetWagerNumber");
				map.put("inetWagerNumber", inetWagerNumber.attr("value"));
			}
		}

		LOGGER.info("Entering processLineChange()");
		return map;
	}

	/**
	 * 
	 * @param xtml
	 * @return
	 */
	public String processCaptcha(String xhtml) throws BatchException {
		LOGGER.info("Entering processCaptcha()");
		String url = null;

		// Parse xhtml
		final Document doc = parseXhtml(xhtml);

		final Element element = doc.getElementById("modalcaptcha");
		if (element != null) {
			url = element.attr("href");
		}

		LOGGER.info("Exiting processCaptcha()");
		return url;
	}

	/**
	 * 
	 * @param xhtml
	 * @return
	 * @throws BatchException
	 */
	public String processTicketInfo(String xhtml) throws BatchException {
		LOGGER.info("Entering processTicketInfo()");
		String gameType = null;
		
		// Parse xhtml
		final Document doc = parseXhtml(xhtml);
		Elements trs = doc.select(".gamenumb tr");
		for (Element tr : trs) {
			String html = tr.html();
			if (html != null) {
				if (html.contains("Sport / Period:")) {
					Elements tds = tr.select("td");
					if (tds != null && tds.size() == 2) {
						Element td = tds.get(1);
						if (td != null) {
							// MLB Baseball / Game ( Atlanta Braves: 0 - Los Angeles Angels: 0 )
							gameType = td.html();
							int index = gameType.indexOf("/");
							if (index != -1) {
								gameType = gameType.substring(0, index).trim();
								gameType = gameType.replaceAll("&nbsp;", "");
							}
						}
					}
				}
			}
		}

		LOGGER.info("Exiting processTicketInfo()");
		return gameType;
	}

	/**
	 * 
	 * @param xtml
	 * @return
	 */
	public String processCheckAcceptance(String xhtml) throws BatchException {
		LOGGER.info("Entering processCheckAcceptance()");
		String url = null;
		
		// Parse xhtml
		final Document doc = parseXhtml(xhtml);

		final Elements strongs = doc.select("strong");
		for (int x = 0; x < strongs.size(); x++) {
			final Element strong = strongs.get(x);
			if (strong != null) {
				String shtml = strong.html();
				if (shtml != null) {
					shtml = shtml.trim();
					if ("Please wait while the system is processing your wager".equals(shtml)) {
						int index = xhtml.indexOf("window.location.replace(\"");
						if (index != -1) {
							xhtml = xhtml.substring(index + "window.location.replace(\"".length());
							index = xhtml.indexOf("\");");
							if (index != -1) {
								url = xhtml.substring(0, index);
							}
						}
					}
				}
			}
		}

		LOGGER.info("Exiting processCheckAcceptance()");
		return url;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#getGameData(org.jsoup.select.Elements)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected List<BetBuckeyeEventPackage> getGameData(Elements elements) throws BatchException {
		LOGGER.info("Entering getGameData()");
		final List<BetBuckeyeEventPackage> events = new ArrayList<BetBuckeyeEventPackage>();

		if (elements != null) {
			BetBuckeyeTeamPackage team1 = null;
			BetBuckeyeTeamPackage team2 = null;
			BetBuckeyeEventPackage eventPackage = null;

			for (int x = 0; x < elements.size(); x++) {
				// Loop through the elements and then check for the dates
				final Element element = elements.get(x);
				if (element != null) {
					Elements tables1 = element.select(".teams_betting_options");
					Elements tables2 = element.select(".teams_betting_options_2");
					LOGGER.debug("tables1.size(): " + tables1.size());
					LOGGER.debug("tables2.size(): " + tables2.size());

					if (tables1 != null && tables1.size() > 0 &&
						tables2 != null && tables2.size() > 0) {
						final Element table1 = tables1.get(0);
						final Element table2 = tables2.get(0);
						team1 = new BetBuckeyeTeamPackage();
						team2 = new BetBuckeyeTeamPackage();
						eventPackage = new BetBuckeyeEventPackage();

						if (table1 != null) {
							final Elements dates = table1.select(".dateLinebetting");
							if (dates != null && dates.size() > 0) {
								final Element dt = dates.get(0);
								if (dt != null) {
									String dhtml = dt.html();
									int index = dhtml.indexOf("</i>");
									if (index != -1) {
										dhtml = dhtml.substring(index + 4);
									}

									// Trim
									if (dhtml != null) {
										dhtml = dhtml.trim();
									}
									
									LOGGER.debug("Date: " + dhtml);

									String cDate = null;
									try {
										// Tue 2/13  06:30PM
										Date newDate = null;
										final Calendar now = Calendar.getInstance();
										int offset = now.get(Calendar.DST_OFFSET);
										dhtml = dhtml.replace("&nbsp;&nbsp;", "##");
										index = dhtml.indexOf("##");
										if (index != -1) {
											String firstPart = dhtml.substring(0, index);
											String secondPart = dhtml.substring(index + 2);
											cDate = firstPart + " " + String.valueOf(now.get(Calendar.YEAR)) + " " + secondPart;
											cDate += " " + timeZoneLookup(timezone, offset);
											newDate = DATE_FORMAT.parse(cDate);

											// Set date of event
											eventPackage.setDateofevent(cDate);
											eventPackage.setEventdatetime(newDate);
											team1.setEventdatetime(newDate);
											team2.setEventdatetime(newDate);
										}
									} catch (ParseException pe) {
										LOGGER.error("ParseExeption for " + cDate, pe);
										// Throw an exception
										throw new BatchException(AppErrorMessage.SITE_PARSER_EXCEPTION  + " Date parsing exception for " + cDate);
									}
								}
							}
						}

						if (table2 != null) {
							LOGGER.debug("table2.html(): " + table2.html());
							Elements divs = table2.select(".team1_name_up");
							LOGGER.debug("divs.size(): " + divs.size());
							if (divs != null && divs.size() > 0) {
								final Element div = divs.get(0);
								LOGGER.debug("div.html(): " + div.html());
								team1 = getTeam(div, team1);
							}

							divs = table2.select(".team2_name_down");
							LOGGER.debug("divs.size(): " + divs.size());
							if (divs != null && divs.size() > 0) {
								final Element div = divs.get(0);
								LOGGER.debug("div.html(): " + div.html());
								team2 = getTeam(div, team2);
							}

							final Elements trs = table2.select(".new_tb_cont tbody tr");
							if (trs != null && trs.size() > 1) {
								final Element team1tr = trs.get(0);
								final Element team2tr = trs.get(1);
								team1 = getGameTeam(team1tr.select("td"), team1);
								team2 = getGameTeam(team2tr.select("td"), team2);
							}
						}

						// Team1 and Team2
						eventPackage.setSiteteamone(team1);
						eventPackage.setSiteteamtwo(team2);
						eventPackage.setId(team1.getId());
						eventPackage.setMlMax(team1.getMlMax());
						eventPackage.setSpreadMax(team1.getSpreadMax());
						eventPackage.setTotalMax(team1.getTotalMax());
						events.add(eventPackage);
					} else {
						tables1 = element.select(".teams_betting_options_advance");
						LOGGER.debug("tables1.size(): " + tables1.size());

						if (tables1 != null && tables1.size() > 0) {
							Element table = tables1.get(0);
							Elements trs = table.select("tr");
							if (trs != null && trs.size() > 1) {
								for (int p = 0; p < trs.size(); p++) {
									final Element tr = trs.get(p);
									if (p == 0) {
										// Date row
										final Elements divs = tr.select(".opt_advance_date");
										if (divs != null && divs.size() > 0) {
											team1 = new BetBuckeyeTeamPackage();
											team2 = new BetBuckeyeTeamPackage();
											eventPackage = new BetBuckeyeEventPackage();

											final Element div = divs.get(0);
											String date = div.html().trim();
											String cDate = null;
											try {
												// Tue 2/13  06:30PM
												Date newDate = null;
												final Calendar now = Calendar.getInstance();
												int offset = now.get(Calendar.DST_OFFSET);
												date = date.replace("&nbsp;&nbsp;", "##");
												int index = date.indexOf("##");
												if (index != -1) {
													String firstPart = date.substring(0, index);
													String secondPart = date.substring(index + 2);
													cDate = firstPart + " " + String.valueOf(now.get(Calendar.YEAR)) + " " + secondPart;
													cDate += " " + timeZoneLookup(timezone, offset);
													newDate = DATE_FORMAT.parse(cDate);

													// Set date of event
													eventPackage.setDateofevent(cDate);
													eventPackage.setEventdatetime(newDate);
													team1.setEventdatetime(newDate);
													team2.setEventdatetime(newDate);
												}
											} catch (ParseException pe) {
												LOGGER.error("ParseExeption for " + cDate, pe);
												// Throw an exception
												throw new BatchException(AppErrorMessage.SITE_PARSER_EXCEPTION  + " Date parsing exception for " + cDate);
											}
										}
									} else if (p == 1) {
										// Team 1
										final Elements tds = tr.select("td");
										if (tds != null && tds.size() > 5) {
											for (int t = 0; t < tds.size(); t++) {
												final Element td = tds.get(t);
												switch (t) {
													case 0:
														team1 = getTeam(td, team1);
														break;
													case 1:
														team1 = getSpread(td, team1);
														break;
													case 2:
														team1 = getMoneyLine(td, team1);
														break;
													case 3:
														team1 = getTotal(td, team1);
														break;
													case 4:
													case 5:
													default:
														break;
												}
											}
										}
									} else {
										// Team 2
										final Elements tds = tr.select("td");
										if (tds != null && tds.size() > 5) {
											for (int t = 0; t < tds.size(); t++) {
												final Element td = tds.get(t);
												switch (t) {
													case 0:
														team2 = getTeam(td, team2);
														break;
													case 1:
														team2 = getSpread(td, team2);
														break;
													case 2:
														team2 = getMoneyLine(td, team2);
														break;
													case 3:
														team2 = getTotal(td, team2);
														break;
													case 4:
													case 5:
													default:
														break;
												}
											}
										}
										
										// Team1 and Team2
										eventPackage.setSiteteamone(team1);
										eventPackage.setSiteteamtwo(team2);
										eventPackage.setId(team1.getId());
										eventPackage.setMlMax(team1.getMlMax());
										eventPackage.setSpreadMax(team1.getSpreadMax());
										eventPackage.setTotalMax(team1.getTotalMax());
										events.add(eventPackage);
									}
								}
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
	 * @param elements
	 * @param team
	 * @return
	 * @throws BatchException
	 */
	protected BetBuckeyeTeamPackage getGameTeam(Elements elements, BetBuckeyeTeamPackage team) throws BatchException {
		LOGGER.info("Entering getGameTeam()");

		for (int x = 0; x < elements.size(); x++) {
			final Element td = elements.get(x);
			LOGGER.debug("td: " + td.html());

			switch (x) {
				case 0:
					team = getSpread(td, team);
					break;
				case 1:
					team = getMoneyLine(td, team);
					break;
				case 2:
					team = getTotal(td, team);
					break;
				case 3:
				case 4:
				default:
					break;
			}
		}

		LOGGER.info("Exiting getGameTeam()");
		return team;
	}

	/**
	 * 
	 * @param td
	 * @param team
	 * @return
	 * @throws BatchException
	 */
	private BetBuckeyeTeamPackage getTeam(Element td, BetBuckeyeTeamPackage team) throws BatchException {
		LOGGER.info("Entering getTeam()");

		// First get game rotation ID
		String gameId = getHtmlFromElement(td, "span", 0, false);
		LOGGER.debug("gameId: " + gameId);

		gameId = gameId.replaceAll("<strong>", "");
		gameId = gameId.replaceAll("</strong>", "");
		team.setEventid(gameId);
		team.setId(Integer.parseInt(gameId));
		
		// Get team name
		String teamName = getHtmlFromElement(td, "span", 1, false);
		LOGGER.debug("teamName: " + teamName);
		team.setTeam(teamName);

		LOGGER.info("Exiting getTeam()");
		return team;
	}

	/**
	 * 
	 * @param td
	 * @param team
	 * @return
	 * @throws BatchException
	 */
	private BetBuckeyeTeamPackage getSpread(Element td, BetBuckeyeTeamPackage team) throws BatchException {
		LOGGER.info("Entering getSpread()");

		// Get the input field
		Map<String, String> hashMap = parseInputField(td.select("input"), 1);
		team.setGameSpreadInputId(hashMap.get("id"));
		team.setGameSpreadInputName(hashMap.get("name"));
		String max = hashMap.get("rel");
		if (max != null && max.length() > 0) {
			int indx = max.indexOf("Max:$");
			if (indx != -1) {
				team.setSpreadMax(Integer.parseInt(max.substring(indx + 5)));
			}
		}

		// Get the select fields
		hashMap = parseSelectField(td.select("select"));
		team.setGameSpreadSelectId(hashMap.get("id"));
		team.setGameSpreadSelectName(hashMap.get("name"));

		final Elements options = td.select("select option");
		if (options != null && options.size() > 0) {
			team = (BetBuckeyeTeamPackage)parseSpreadSelectOption(options, team, " ", null);
		} else {
			// -2½  +200; Now parse the data
			String data = getHtmlFromLastIndex(td, ">");
			team = (BetBuckeyeTeamPackage)parseSpreadData(reformatValues(data), 0, " ", null, team);
		}

		LOGGER.info("Exiting getSpread()");
		return team;
	}

	/**
	 * 
	 * @param td
	 * @param team
	 * @return
	 */
	private BetBuckeyeTeamPackage getMoneyLine(Element td, BetBuckeyeTeamPackage team) {
		LOGGER.info("Entering getMoneyLine()");

		// First get the input field information
		final Map<String, String> hashMap = parseInputField(td.select("input"), 1);
		team.setGameMLInputId(hashMap.get("id"));
		team.setGameMLInputName(hashMap.get("name"));

		String max = hashMap.get("rel");
		if (max != null && max.length() > 0) {
			int indx = max.indexOf("Max:$");
			if (indx != -1) {
				team.setMlMax(Integer.parseInt(max.substring(indx + 5)));
			}
		}

		// Parse ML Data
		String data = getHtmlFromLastIndex(td, ">");
		team = (BetBuckeyeTeamPackage)parseMlData(data, 0, team);
	
		LOGGER.info("Exiting getMoneyLine()");
		return team;
	}

	/**
	 * 
	 * @param td
	 * @param team
	 * @return
	 * @throws BatchException
	 */
	private BetBuckeyeTeamPackage getTotal(Element td, BetBuckeyeTeamPackage team) throws BatchException {
		LOGGER.info("Entering getTotal()");

		// First get the input field information
		Map<String, String> hashMap = parseInputField(td.select("input"), 1);
		team.setGameTotalInputId(hashMap.get("id"));
		team.setGameTotalInputName(hashMap.get("name"));

		String max = hashMap.get("rel");
		if (max != null && max.length() > 0) {
			int indx = max.indexOf("Max:$");
			if (indx != -1) {
				team.setTotalMax(Integer.parseInt(max.substring(indx + 5)));
			}
		}

		// Get the select fields
		hashMap = parseSelectField(td.select("select"));
		team.setGameTotalSelectId(hashMap.get("id"));
		team.setGameTotalSelectName(hashMap.get("name"));

		final Elements options = td.select("select option");
		if (options != null && options.size() > 0) {
			team = (BetBuckeyeTeamPackage)parseTotalSelectOption(options, team, " ", null);
		} else {
			final String data = getHtmlFromLastIndex(td, ">");			
			team = (BetBuckeyeTeamPackage)parseTotalData(data, 0, " ", null, team);
		}

		LOGGER.info("Exiting getTotal()");
		return team;
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 */
	private void getTicketNumber(Element td, PendingEvent pe) {
		LOGGER.info("Entering getTicketNumber()");

		// <td nowrap="nowrap">1&nbsp;&nbsp;
		//   <span class="grouped_elements" onclick="loadTicketdata(190259950, 1, 0, true);" style="background-color:#F5D247; border:1px solid #999999; cursor:pointer">Ticket</span>
		// </td>

		// Parse the load ticket data
		parseLoadTicketData(td.html(), pe);

		Elements spans = td.select("span");
		if (spans != null && spans.size() > 0 ) {
			String onclick  = spans.get(0).attr("onclick");
			LOGGER.info("onclick: " + onclick);
			if (onclick != null) {
				int bIndex = onclick.indexOf("loadTicketdata(");
				int eIndex = onclick.indexOf(",");
				if (bIndex != -1 & eIndex != -1) {
					pe.setTicketnum(onclick.substring(bIndex + 15, eIndex));
				}
			}
		}
		
		LOGGER.info("Exiting getTicketNumber()");
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 */
	private void getDateAccepted(Element td, PendingEvent pe) {
		LOGGER.info("Entering getDateAccepted()");

		// <td nowrap="nowrap">5/24/2017<br>2:15 AM</td>
		String html = td.html();
		if (html != null && html.length() > 0) {
			LOGGER.info("html: " + html);
			pe.setDateaccepted(html.replaceAll("<br>", " ") + " " + determineTimeZone(super.timezone));
		}

		LOGGER.info("Exiting getDateAccepted()");
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 */
	private void getEventType(Element td, PendingEvent pe) {
		LOGGER.info("Entering getEventType()");

		// <td nowrap="nowrap">&nbsp;&nbsp;Total&nbsp;&nbsp;</td>
		String html = td.html();
		if (html != null && html.length() > 0) {
			html = html.replaceAll("&nbsp;", "");
			LOGGER.info("html: " + html);

			if ("Money Line".equals(html)) {
				pe.setEventtype("ml");
			} else if ("Spread".equals(html)) {
				pe.setEventtype("spread");
			} else if ("Total".equals(html)) {
				pe.setEventtype("total");
			} else if ("Teaser".equals(html)) {
				pe.setEventtype("teaser");
			} else {
				pe.setEventtype("notsupported");
			}
		}

		LOGGER.info("Exiting getEventType()");
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 * @throws BatchException
	 */
	private void getEventInfo(Element td, PendingEvent pe) throws BatchException {
		LOGGER.info("Entering getEventInfo()");

		// <td style="text-align:left;" nowrap="nowrap"><font style="color:#CC0000">Basketball</font> #507 Cleveland Cavaliers/Boston Celtics under 214½ -110 for Game</td>
		// <td style="text-align:left;" nowrap=""><font style="color:#CC0000">Football</font>#394 Arizona State -3½ -115 buying ½ for Game</td>
		final Elements fonts = td.select("font");
		LOGGER.info("fonts: " + fonts);
		if (fonts != null && fonts.size() > 0) {
			// Get the game sport
			final String gamesport = fonts.get(0).html().trim();
			pe.setGamesport(gamesport);
		} else {
			String html = td.html().trim();
			LOGGER.info("html: " + html);
			int spaceIndex = html.indexOf(" ");
			if (spaceIndex != -1) {
				final String gamesport = html.substring(0, spaceIndex).trim();
				pe.setGamesport(gamesport);
			}
		}

		String html = td.html();

		// Get the Rotation ID
		html = getRotationId(html, pe);

		// Now parse the team
		getEventInfoForGame(html, pe);
		
		// Now parse line type
		getLineType(html, pe);

		final String rotationId = pe.getRotationid();
		if (rotationId != null && (rotationId.length() == 1 || rotationId.length() == 2 || rotationId.length() == 3)) {
			// Now check for Team Total
			String team = pe.getTeam();
			if (team != null && team.contains("Team Total")) {
				pe.setEventtype("unsupported");
				throw new BatchException(BatchErrorCodes.UNSUPPORTED_SPORT_TYPE, BatchErrorMessage.UNSUPPORTED_SPORT_TYPE, html);				
			}
		} else if (rotationId != null && rotationId.length() >= 4) {
			pe.setEventtype("unsupported");
			throw new BatchException(BatchErrorCodes.UNSUPPORTED_SPORT_TYPE, BatchErrorMessage.UNSUPPORTED_SPORT_TYPE, html);
		} else {
			pe.setEventtype("unsupported");
			throw new BatchException(BatchErrorCodes.UNSUPPORTED_SPORT_TYPE, BatchErrorMessage.UNSUPPORTED_SPORT_TYPE, html);			
		}

		LOGGER.info("Exiting getEventInfo()");
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 * @throws BatchException
	 */
	private void getNewEventInfo(Element td, PendingEvent pe) throws BatchException {
		LOGGER.info("Entering getNewEventInfo()");

		// <td "=">
		//   <span class=" cl-show-ticket" onclick="loadTicketdata(210701549, 1, 0, true);" style="background-color:#F5D247; border:1px solid #999999; cursor:pointer">Ticket</span>
		//   <label class="cl-type-wager ">Total</label>
		//   <font style="color:#CC0000">Football</font>
		//   #253 Chicago Bears/New Orleans Saints under 48 -110 for Game
		// </td>

		String html = td.html();
		if (html != null) {
			final Elements spans = td.select("span");
			if (spans != null && spans.size() > 0) {
				final Element span = spans.get(0);
				String ticketInfo = span.attr("onclick");
				if (ticketInfo != null && ticketInfo.length() > 0) {
					int idx = ticketInfo.indexOf("loadTicketdata(");
					if (idx != -1) {
						ticketInfo = ticketInfo.substring(idx + "loadTicketdata(".length());
						idx = ticketInfo.indexOf(",");
						if (idx != -1) {
							String firstPart = ticketInfo.substring(0, idx).trim();
							ticketInfo = ticketInfo.substring(idx + 1);
							idx = ticketInfo.indexOf(",");
							if (idx != -1) {
								ticketInfo = ticketInfo.substring(0, idx).trim();
								pe.setTicketnum(firstPart + "-" + ticketInfo);
							}
						}
					}
				}
			}

			pe.setPosturl("Something here");
			final Elements labels = td.select("label");
			if (labels != null && labels.size() > 0) {
				final Element label = labels.get(0);
				String labelInfo = label.html().trim();
				labelInfo = labelInfo.replace("&nbsp;", "");

				if ("Money Line".equals(labelInfo)) {
					pe.setEventtype("ml");
				} else if ("Spread".equals(labelInfo)) {
					pe.setEventtype("spread");
				} else if ("Total".equals(labelInfo)) {
					pe.setEventtype("total");
				} else if ("Teaser".equals(labelInfo)) {
					pe.setEventtype("teaser");
				}
			}

			final Elements fonts = td.select("font");
			if (fonts != null && fonts.size() > 0) {
				final Element font = fonts.get(0);
				final String fontInfo = font.html().trim();
				pe.setGamesport(fontInfo);
			}

			// Get the Rotation ID
			html = getRotationId(html, pe);

			// Now parse the team
			getEventInfoForGame(html, pe);

			// Now parse line type
			getLineType(html, pe);

			final String rotationId = pe.getRotationid();
			if (rotationId != null && rotationId.length() == 1 || rotationId.length() == 2
					|| rotationId.length() == 3) {
				// Now check for Team Total
				String team = pe.getTeam();
				if (team != null && team.contains("Team Total")) {
					pe.setEventtype("unsupported");
					throw new BatchException(BatchErrorCodes.UNSUPPORTED_SPORT_TYPE,
							BatchErrorMessage.UNSUPPORTED_SPORT_TYPE, html);
				}
			} else if (rotationId.length() >= 4) {
				pe.setEventtype("unsupported");
				throw new BatchException(BatchErrorCodes.UNSUPPORTED_SPORT_TYPE,
						BatchErrorMessage.UNSUPPORTED_SPORT_TYPE, html);
			}
		}

		LOGGER.info("Exiting getNewEventInfo()");
	}


	/**
	 * 
	 * @param html
	 * @param pe
	 * @return
	 */
	private String getRotationId(String html, PendingEvent pe) {
		LOGGER.info("Entering getRotationId()");

		if (html != null && html.length() > 0) {
			int fontIndex = html.indexOf("</font>");
			if (fontIndex != -1) {
				html = html.substring(fontIndex + 7).trim();
				int rotationIndex = html.indexOf("#");
				int spaceIndex = html.indexOf(" ");
				if (rotationIndex != -1 && spaceIndex != -1) {
					// Get the Rotation ID
					pe.setRotationid(html.substring(rotationIndex + 1, spaceIndex));
					html = html.substring(spaceIndex + 1);
				}
			}
		}

		LOGGER.info("Exiting getRotationId()");
		return html;
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 * @return
	 */
	protected void getEventInfoForGame(String html, PendingEvent pe) throws BatchException {
		LOGGER.info("Entering getEventInfo()");
		html = html.replaceAll("Â", "");
		LOGGER.debug("html: " + html);

		if ("spread".equals(pe.getEventtype())) {
			String juiceString = null;
			String lineString = null;
			int forIndex = html.indexOf(" for ");
			if (forIndex != -1) {

				if (html.contains("Quarter")) {
					pe.setEventtype("unsupported");
					throw new BatchException(BatchErrorCodes.UNSUPPORTED_SPORT_TYPE, BatchErrorMessage.UNSUPPORTED_SPORT_TYPE, html);
				}

				html = html.substring(0, forIndex);
				LOGGER.debug("html: " + html);
				int tempIndex = html.indexOf(" buying");
				if (tempIndex != -1) {
					html = html.substring(0, tempIndex);
				}
				int juiceIndex = html.lastIndexOf(" ");
				if (juiceIndex != -1) {
					juiceString = html.substring(juiceIndex + 1);
					html = html.substring(0, juiceIndex);
					LOGGER.debug("html: " + html);
					int lineIndex = html.lastIndexOf(" ");
					if (lineIndex != -1) {
						LOGGER.debug("html: " + html);
						lineString = html.substring(lineIndex + 1);
						LOGGER.debug("lineString: " + lineString);
						html = html.substring(0, lineIndex);
						pe.setTeam(html.trim());
					}
				}
			}

			// Now get the line Information
			if (lineString != null) {
				LOGGER.debug("lineString: " + lineString);
				getLineInformation(lineString, pe);	
			}

			// Now get the juice Information
			if (juiceString != null) {
				getJuiceInformation(juiceString, pe);
			}
		} else if ("total".equals(pe.getEventtype())) {
			if (html.contains("Quarter")) {
				pe.setEventtype("unsupported");
				throw new BatchException(BatchErrorCodes.UNSUPPORTED_SPORT_TYPE, BatchErrorMessage.UNSUPPORTED_SPORT_TYPE, html);
			}

			int underIndex = html.lastIndexOf("under");
			int overIndex = html.lastIndexOf("over");
			
			LOGGER.debug("underIndex: " + underIndex);
			LOGGER.debug("overIndex: " + overIndex);
		
			if (overIndex != -1) {
				LOGGER.debug("Setting up over");
				pe.setTeam(html.substring(0, overIndex - 1));
				pe.setLineplusminus("o");

				html = html.substring(overIndex + 5);
				LOGGER.debug("html2: " + html);
				int lineIndex = html.indexOf(" ");
				if (lineIndex != -1) {
					LOGGER.debug("setting line");
					pe.setLine(super.reformatValues(html.substring(0, lineIndex)));
					html = html.substring(lineIndex + 1);
					int endIndex = html.indexOf(" ");
					if (endIndex != -1) {
						LOGGER.debug("setting juice");
						getJuiceInformation(html.substring(0, endIndex), pe);
					}
				}

			} else if (underIndex != -1) {
				pe.setTeam(html.substring(0, underIndex - 1));
				pe.setLineplusminus("u");

				html = html.substring(underIndex + 6);
				LOGGER.debug("html: " + html);
				int lineIndex = html.indexOf(" ");
				if (lineIndex != -1) {
					pe.setLine(super.reformatValues(html.substring(0, lineIndex)));
					html = html.substring(lineIndex + 1);
					int endIndex = html.indexOf(" ");
					if (endIndex != -1) {
						getJuiceInformation(html.substring(0, endIndex), pe);
					}
				}
			} else {
				underIndex = html.lastIndexOf(" u ");
				overIndex = html.lastIndexOf(" o ");
				
				LOGGER.debug("underIndex: " + underIndex);
				LOGGER.debug("overIndex: " + overIndex);
			
				if (overIndex != -1) {
					LOGGER.debug("Setting up over");
					pe.setTeam(html.substring(0, overIndex - 1));
					pe.setLineplusminus("o");

					html = html.substring(overIndex + 3);
					LOGGER.debug("html2: " + html);
					int lineIndex = html.indexOf(" ");
					if (lineIndex != -1) {
						LOGGER.debug("setting line");
						pe.setLine(super.reformatValues(html.substring(0, lineIndex)));
						html = html.substring(lineIndex + 1);
						int endIndex = html.indexOf(" ");
						if (endIndex != -1) {
							LOGGER.debug("setting juice");
							getJuiceInformation(html.substring(0, endIndex), pe);
						}
					}

				} else if (underIndex != -1) {
					pe.setTeam(html.substring(0, underIndex - 1));
					pe.setLineplusminus("u");

					html = html.substring(underIndex + 3);
					LOGGER.debug("html: " + html);
					int lineIndex = html.indexOf(" ");
					if (lineIndex != -1) {
						pe.setLine(super.reformatValues(html.substring(0, lineIndex)));
						html = html.substring(lineIndex + 1);
						int endIndex = html.indexOf(" ");
						if (endIndex != -1) {
							getJuiceInformation(html.substring(0, endIndex), pe);
						}
					}
				}
			}
		} else if ("ml".equals(pe.getEventtype())) {
			String juiceString = null;
			int forIndex = html.indexOf(" for ");
			if (forIndex != -1) {
				if (html.contains("Quarter")) {
					pe.setEventtype("unsupported");
					throw new BatchException(BatchErrorCodes.UNSUPPORTED_SPORT_TYPE, BatchErrorMessage.UNSUPPORTED_SPORT_TYPE, html);
				}

				html = html.substring(0, forIndex);
				int tempIndex = html.indexOf("buying");
				if (tempIndex != -1) {
					html = html.substring(0, tempIndex);
				}
				int juiceIndex = html.lastIndexOf(" ");
				if (juiceIndex != -1) {
					juiceString = html.substring(juiceIndex + 1);
					html = html.substring(0, juiceIndex);
					pe.setTeam(html.trim());
				}
			}

			// Now get the line Information
			if (juiceString != null) {
				getJuiceInformation(juiceString, pe);
			}
		}

		LOGGER.info("Exiting getEventInfo()");
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 * @return
	 */
	private String getJuiceInformation(String html, PendingEvent pe) {
		LOGGER.info("Entering getJuiceInformation()");
		LOGGER.debug("html: " + html);

		int plusdIndex = html.indexOf("+");
		int minusIndex = html.indexOf("-");
		if (plusdIndex != -1 || minusIndex != -1) {
			pe.setJuiceplusminus(html.substring(0, 1));
			pe.setJuice(super.reformatValues(html.substring(1)));
			if ("ml".equals(pe.getEventtype())) {
				pe.setLineplusminus(html.substring(0, 1));
				pe.setLine(super.reformatValues(html.substring(1)));
			}
		} else {
			int pkIndex = html.indexOf("pk");
			int PKIndex = html.indexOf("PK");
			int evIndex = html.indexOf("ev");
			int EVIndex = html.indexOf("EV");
			if (pkIndex != -1 || PKIndex != -1 || evIndex != -1 || EVIndex != -1) {
				pe.setJuiceplusminus("+");
				pe.setJuice("100");
				if ("ml".equals(pe.getEventtype())) {
					pe.setLineplusminus("+");
					pe.setLine("100");
				}
			}
		}

		LOGGER.info("Exiting getJuiceInformation()");
		return html;
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 * @return
	 */
	private String getLineInformation(String html, PendingEvent pe) {
		LOGGER.info("Entering getLineInformation()");
		LOGGER.debug("html: " + html);

		if ("spread".equals(pe.getEventtype())) {
			int plusdIndex = html.indexOf("+");
			int minusIndex = html.indexOf("-");
			if (plusdIndex != -1 || minusIndex != -1) {
				pe.setLineplusminus(html.substring(0, 1));
				String lineData = html.substring(1);
				LOGGER.debug("lineData: " + lineData + " end");
				pe.setLine(super.reformatValues(lineData));
			} else {
				int pkIndex = html.indexOf("pk");
				int PKIndex = html.indexOf("PK");
				int evIndex = html.indexOf("ev");
				int EVIndex = html.indexOf("EV");
				if (pkIndex != -1 || PKIndex != -1 || evIndex != -1 || EVIndex != -1) {
					pe.setLineplusminus("+");
					pe.setLine("100");
				}
			}	
		} else if ("total".equals(pe.getEventtype())) {
			int underIndex = html.indexOf("under");
			int overIndex = html.indexOf("over");
			if (underIndex != -1) {
				pe.setLineplusminus("u");
			} else if (overIndex != -1) {
				pe.setLineplusminus("o");
			} else {
				underIndex = html.indexOf(" u ");
				overIndex = html.indexOf(" o ");
				if (underIndex != -1) {
					pe.setLineplusminus("u");
					html = html.substring(underIndex + 2);
				} else if (overIndex != -1) {
					pe.setLineplusminus("o");
					html = html.substring(overIndex + 2);
				}
			}

			int firstSpace = html.indexOf(" ");
			if (firstSpace != -1) {
				pe.setLineplusminus(html.substring(0, 1));
				html = html.substring(1);
				html = parseSpace(html, pe);
			}
		} else if ("ml".equals(pe.getEventtype())) {
			int plusdIndex = html.indexOf("+");
			int minusIndex = html.indexOf("-");
			if (plusdIndex != -1 || minusIndex != -1) {
				pe.setLineplusminus(html.substring(0, 1));
				html = html.substring(1);
				int spaceIndex = html.indexOf(" ");
				if (spaceIndex != -1) {
					pe.setLine(super.reformatValues(html.substring(0, spaceIndex)));
					html = html.substring(spaceIndex + 1);
				}
			}
		}

		LOGGER.info("Exiting getLineInformation()");
		return html;
	}

	/**
	 * 
	 * @param html
	 * @param pe
	 * @return
	 */
	private String getLineType(String html, PendingEvent pe) {
		LOGGER.info("Entering getLineType()");
		LOGGER.debug("html: " + html);

		int forIndex = html.indexOf("for ");
		if (forIndex != -1) {
			html = html.substring(forIndex + 4);
			if (html != null && html.length() > 0) {
				html = html.trim();
				if ("1st Half".equals(html) || "1st 5 Innings".equals(html) || "1st Period".equals(html)) {
					pe.setLinetype("first");
				} else if ("2nd Half".equals(html) || "2nd Period".equals(html)) {
					pe.setLinetype("second");
				} else if ("3rd Half".equals(html) || "3rd Period".equals(html)) {
					pe.setLinetype("third");
				} else {
					pe.setLinetype("game");
				}
			} else {
				pe.setLinetype("game");
			}
		}

		LOGGER.info("Exiting getLineType()");
		return html;
	}

	/**
	 * 
	 * @param html
	 * @param pe
	 * @return
	 */
	private String parseSpace(String html, PendingEvent pe) {
		LOGGER.info("Entering parseSpace()");
		LOGGER.debug("html: " + html);

		int spaceIndex = html.indexOf(" ");
		if (spaceIndex != -1) {
			pe.setLine(super.reformatValues(html.substring(0, spaceIndex)));
			html = html.substring(spaceIndex + 1);
			spaceIndex = html.indexOf(" ");
			if (spaceIndex != -1) {
				pe.setJuiceplusminus(html.substring(0, 1));
				pe.setJuice(super.reformatValues(html.substring(1, spaceIndex)));
				html = html.substring(spaceIndex + 1);
			}
		}
		
		LOGGER.info("Exiting parseSpace()");
		return html;
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 */
	private void getStartDate(Element td, PendingEvent pe) {
		LOGGER.info("Entering getStartDate()");
		LOGGER.debug("td: " + td);

		// <td nowrap="" align="LEFT"><font size="2px">5/29/2017 9:05 PM</font><br></td>
		final Elements fonts = td.select("font");
		if (fonts != null && fonts.size() > 0 ) {
			String html  = fonts.get(0).html();
			if (html != null && html.length() > 0) {
				pe.setEventdate(html + " " + determineTimeZone(super.timezone));

				// Setup the date
				try {
					Date gameDate = PENDING_DATE_FORMAT.get().parse(html + " " + determineTimeZone(super.timezone));
					pe.setGamedate(gameDate);
				} catch (Throwable t) {
					LOGGER.error("gamedate: " + html + " " + determineTimeZone(super.timezone));
					LOGGER.error(t.getMessage(), t);
					pe.setGamedate(new Date());
				}
			}
		}

		LOGGER.info("Exiting getStartDate()");
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 */
	private void getRisk(Element td, PendingEvent pe) {
		LOGGER.info("Entering getRisk()");
		LOGGER.debug("td: " + td);

		// <td nowrap="nowrap" align="RIGHT">&nbsp;&nbsp;<span style="color:#FF0000;">$55.00</span>&nbsp;&nbsp;</td>
		final Elements spans = td.select("span");
		if (spans != null && spans.size() > 0 ) {
			String html  = spans.get(0).html();
			if (html != null && html.length() > 0) {
				pe.setRisk(html);
			}
		}

		LOGGER.info("Exiting getRisk()");
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 */
	private void getWin(Element td, PendingEvent pe) {
		LOGGER.info("Entering getWin()");
		LOGGER.debug("td: " + td);

		// <td nowrap="nowrap" align="RIGHT">&nbsp;&nbsp;<span style="color:#000000;">$50.00</span></td>
		Elements spans = td.select("span");
		if (spans != null && spans.size() > 0 ) {
			String html  = spans.get(0).html();
			if (html != null && html.length() > 0) {
				pe.setWin(html);
			}
		}

		LOGGER.info("Exiting getWin()");
	}
}