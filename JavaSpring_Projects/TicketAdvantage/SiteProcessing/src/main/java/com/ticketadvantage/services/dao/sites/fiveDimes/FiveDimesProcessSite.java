/**
 * 
 */
package com.ticketadvantage.services.dao.sites.fiveDimes;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.ticketadvantage.services.captcha.CaptchaWorker;
import com.ticketadvantage.services.captcha.GetCaptchaText;
import com.ticketadvantage.services.dao.sites.SiteProcessor;
import com.ticketadvantage.services.dao.sites.SiteTransaction;
import com.ticketadvantage.services.dao.sites.sportsinsights.SportsInsightsSite;
import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.AccountEvent;
import com.ticketadvantage.services.model.BaseRecordEvent;
import com.ticketadvantage.services.model.EventPackage;
import com.ticketadvantage.services.model.MlRecordEvent;
import com.ticketadvantage.services.model.PendingEvent;
import com.ticketadvantage.services.model.PreviewInput;
import com.ticketadvantage.services.model.PreviewOutput;
import com.ticketadvantage.services.model.SpreadRecordEvent;
import com.ticketadvantage.services.model.TeamTotalRecordEvent;
import com.ticketadvantage.services.model.TotalRecordEvent;

/**
 * @author jmiller
 *
 */
public class FiveDimesProcessSite extends SiteProcessor {
	private static final Logger LOGGER = Logger.getLogger(FiveDimesProcessSite.class);
	private static final String PENDING_BETS = "SpecificPlayerPendingWagers.php";
	private final FiveDimesParser BBP = new FiveDimesParser();
	private int rerunCount = 0;
	private boolean resetConnection = false;

	/**
	 * 
	 * @param host
	 * @param username
	 * @param password
	 */
	public FiveDimesProcessSite(String host, String username, String password, boolean isMobile, boolean showRequestResponse) {
		super("FiveDimes", host, username, password, isMobile, showRequestResponse);
		LOGGER.info("Entering FiveDimesProcessSite()");

		// Setup the parser
		this.siteParser = BBP;

		// Setup the menu items
		NFL_LINES_NAME = new String[] { "Football_NFL" };
		NFL_FIRST_SPORT = new String[] { "Football" };
		NFL_FIRST_NAME = new String[] { "Football_NFL" };		
		NFL_SECOND_SPORT = new String[] { "Football" };
		NFL_SECOND_NAME = new String[] { "Football_NFL" };
		NCAAF_LINES_SPORT = new String[] { "Football" };
		NCAAF_LINES_NAME = new String[] { "Football_College" };
		NCAAF_FIRST_SPORT = new String[] { "Football" };
		NCAAF_FIRST_NAME = new String[] { "Football_College" };
		NCAAF_SECOND_SPORT = new String[] { "Football" };
		NCAAF_SECOND_NAME = new String[] { "Football_College" };
		NBA_LINES_SPORT = new String[] { "Basketball" };
		NBA_LINES_NAME = new String[] { "Basketball_NBA", "Basketball_NBA Preseason" };
		NBA_FIRST_SPORT = new String[] { "Basketball" };
		NBA_FIRST_NAME = new String[] { "Basketball_NBA", "Basketball_NBA Preseason" };
		NBA_SECOND_SPORT = new String[] { "Basketball" };
		NBA_SECOND_NAME = new String[] { "Basketball_NBA", "Basketball_NBA Preseason" };
		NCAAB_LINES_SPORT = new String[] { "Basketball" };
		NCAAB_LINES_NAME = new String[] { "Basketball_College" }; 
		NCAAB_FIRST_SPORT = new String[] { "Basketball" };
		NCAAB_FIRST_NAME = new String[] { "Basketball_College" };
		NCAAB_SECOND_SPORT = new String[] { "Basketball" };
		NCAAB_SECOND_NAME = new String[] { "Basketball_College" };
		NHL_LINES_SPORT = new String[] { "Hockey" };
		NHL_LINES_NAME = new String[] { "Hockey_NHL" }; 
		NHL_FIRST_SPORT = new String[] { "Hockey" };		
		NHL_FIRST_NAME = new String[] { "Hockey_NHL" };
		NHL_SECOND_SPORT = new String[] { "Hockey" };
		NHL_SECOND_NAME = new String[] { "Hockey_NHL" };
		WNBA_LINES_SPORT = new String[] { "Basketball" };
		WNBA_LINES_NAME = new String[] { "Basketball_WNBA" };
		WNBA_FIRST_SPORT = new String[] { "Basketball" };
		WNBA_FIRST_NAME = new String[] { "Basketball_WNBA" };
		WNBA_SECOND_SPORT = new String[] { "Basketball" };
		WNBA_SECOND_NAME = new String[] { "Basketball_WNBA" };
		MLB_LINES_SPORT = new String[] { "Baseball" };
		MLB_LINES_NAME = new String[] { "Baseball_MLB" };
		MLB_FIRST_SPORT = new String[] { "Baseball" };
		MLB_FIRST_NAME = new String[] { "Baseball_MLB" };
		MLB_SECOND_SPORT = new String[] { "Baseball" };
		MLB_SECOND_NAME = new String[] { "Baseball_MLB" };

		LOGGER.info("Exiting FiveDimesProcessSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			String [][] BSITES = new String [][] { 
//				{ "http://fiveDimessports.com", "zt8219", "red3", "2000", "1000", "1000", "Chicago", "ET"}
//			 	{ "http://www.betbck.com", "A5313", "utah", "500", "500", "500", "Phoenix", "ET"}
//		        { "http://www.Tobysports.net", "Orc101", "pgf", "500", "500", "500", "Asheville", "ET"}
//			 	{ "http://allweeksports.com", "ja005", "Water5", "500", "500", "500", "Phoenix", "ET"}
//				{ "http://www.fiveDimessports.com", "spl301", "cramsey", "0", "0", "0", "Phoenix", "ET"}
//				{ "http://789sports.com", "SPL312", "OWEN", "0", "0", "0", "New York City", "ET"}
//				{ "http://aceshigh.ag", "CLD5001", "c", "0", "0", "0", "New York City", "ET"}
//				{ "http://solidgoldwagering.com", "BM008", "Nephew", "0", "0", "0", "Chicago", "ET"}
//				{ "http://dimebet.ag", "JNN211", "rhino44", "0", "0", "0", "Chicago", "ET"}
//				{ "http://aceshigh.ag", "JNN219", "tunes2", "0", "0", "0", "Asheville", "ET"}
//				{ "https://ttdsportsbook.net", "309b", "3387", "0", "0", "0", "Los Angeles", "ET"}
//				{ "https://beatpat.com", "Z5059", "4444", "0", "0", "0", "New York", "ET"}
//				{ "https://betvegas365.com", "GA9002", "gold", "0", "0", "0", "New York", "ET"}
//				{ "https://ttdsportsbook.net", "Ttd10001", "l338", "0", "0", "0", "Los Angeles", "ET"}
//				{ "https://dimebet.ag", "jnn286", "jake73", "0", "0", "0", "None", "ET"}
				{ "https://www.5dimes.eu/", "5d2440502", "Randy123!", "0", "0", "0", "None", "ET"}
			};

			// Loop through the sites
			//for (int i = 0; i < BSITES.length; i++) {
			    final FiveDimesProcessSite processSite = new FiveDimesProcessSite(BSITES[0][0], BSITES[0][1], BSITES[0][2], false, true);
			    processSite.httpClientWrapper.setupHttpClient(BSITES[0][6]);
			    processSite.processTransaction = false;
			    processSite.timezone = BSITES[0][7];
				String xhtml = processSite.loginToSite(BSITES[0][1], BSITES[0][2]);

/*				final PreviewInput previewInput = new PreviewInput();
				previewInput.setLinetype("spread");
				previewInput.setLineindicator("+");
				previewInput.setRotationid(new Integer(309));
				previewInput.setSporttype("ncaaflines");
				previewInput.setProxyname("None");
				previewInput.setTimezone("ET");

				final PreviewOutput previewData = processSite.previewEvent(previewInput);
				LOGGER.error("PreviewData: " + previewData);*/

				/**
				 ************************************************************************
				 *---------------------Football College----------------------------------
				 ************************************************************************
				 */

				Map<String, String> map = processSite.getMenuFromSite("ncaablines", xhtml);
				LOGGER.debug("map: " + map);

				xhtml = processSite.selectSport("ncaablines");
				System.out.println("xhtml: "+xhtml);
				LOGGER.debug("XHTML: " + xhtml);

				final Iterator<EventPackage> ep = processSite.parseGamesOnSite("ncaablines", xhtml);
					
				final SpreadRecordEvent mre = new SpreadRecordEvent();
				mre.setSpreadjuiceplusminusfirstone("-");
				mre.setSpreadinputjuicefirstone("110");
				mre.setSpreadplusminusfirstone("-");
				mre.setSpreadinputfirstone("5");
				mre.setId(new Long(725));
				mre.setEventname("#725 Ball State -110");
				mre.setEventtype("spread");
				mre.setSport("ncaablines"); 
				mre.setUserid(new Long(6));
				mre.setEventid(new Integer(725));
				mre.setEventid1(new Integer(725));
				mre.setEventid2(new Integer(726));
				mre.setRotationid(new Integer(725));
				mre.setWtype("2");
				mre.setAmount("60");
				
				/*final MlRecordEvent mre = new MlRecordEvent();
				mre.setMlplusminusfirstone("-");
				mre.setMlinputfirstone("500");
				mre.setId(new Long(305));
				mre.setEventname("#305 Ball State -110");
				mre.setEventtype("ml");
				mre.setSport("nfllines");
				mre.setUserid(new Long(6));
				mre.setEventid(new Integer(305));
				mre.setEventid1(new Integer(305));
				mre.setEventid2(new Integer(306));
				mre.setRotationid(new Integer(305));
				mre.setWtype("2");
				mre.setAmount("50");
				*/
				
/*				final TotalRecordEvent mre = new TotalRecordEvent();
				mre.setTotaljuiceplusminusfirstone("-");
				mre.setTotalinputjuicefirstone("666");
				mre.setTotaljuiceplusminusfirstone("-");
				mre.setTotalinputfirstone("300");
				mre.setId(new Long(309));
				mre.setEventname("#309 Ohio -110");
				mre.setEventtype("total");
				mre.setSport("nfllines");
				mre.setUserid(new Long(6));
				mre.setEventid(new Integer(309));
				mre.setEventid1(new Integer(309));
				mre.setEventid2(new Integer(310));
				mre.setRotationid(new Integer(309));
				mre.setWtype("2");
			
				TeamTotalRecordEvent mre = new TeamTotalRecordEvent();
				mre.setAmount("50");
*/
				
/*				TeamTotalRecordEvent mre = new TeamTotalRecordEvent();
				mre.setTeamTotaljuiceplusminusfirstone("-");
				mre.setTeamTotalinputjuicefirstone("2");
				mre.setTeamTotaljuiceplusminusfirstone("-");
				mre.setTeamTotalinputfirstone("500");
				mre.setId(new Long(725));
				mre.setEventname("#309 Ohio -110");
				mre.setEventtype("teamtotal");
				mre.setSport("ncaablines");
				mre.setUserid(new Long(6));
				mre.setEventid(new Integer(725));
				mre.setEventid1(new Integer(725));
				mre.setEventid2(new Integer(726));
				mre.setRotationid(new Integer(725));
				mre.setWtype("2");
				mre.setAmount("50");
				mre.setTeamtotaloverunder("o");
*/

				// Step 9 - Find event
				EventPackage eventPackage = processSite.findEvent(ep, mre);
				if (eventPackage == null) {
					throw new BatchException(BatchErrorCodes.GAME_NOT_AVAILABLE, BatchErrorMessage.GAME_NOT_AVAILABLE, "Game cannot be found on site for " + mre.getEventname(), xhtml);
				}
							
				SiteTransaction siteTransaction = new SiteTransaction();
				AccountEvent ae = new AccountEvent();
				ae.setMaxmlamount(new Integer(500));
				ae.setMaxspreadamount(new Integer(500));
				ae.setMaxtotalamount(new Integer(500));
				ae.setMaxteamtotalamount(new Integer(500));

				xhtml = processSite.selectEvent(siteTransaction, eventPackage, mre, ae);
				LOGGER.debug("xhtml: " + xhtml);
		
					// Step 13 - Parse the event selection
				processSite.parseEventSelection(xhtml, siteTransaction, eventPackage, mre, ae);
	
				/**
				 ************************************************************************
				 *---------------------Football College code end here----------------------------------
				 ************************************************************************
				 */
				
				/**
				 ************************************************************************
				 *---------------------Football NFL----------------------------------
				 ************************************************************************
				 */
/*			    Map<String, String> map = processSite.getMenuFromSite("nflfirst", xhtml);
			    LOGGER.debug("map: " + map);

				xhtml = processSite.selectSport("nflfirst");
				LOGGER.debug("XHTML: " + xhtml);

				final Iterator<EventPackage> ep = processSite.parseGamesOnSite("tblFootballNFLGame", xhtml);

				final MlRecordEvent mre = new MlRecordEvent();
				mre.setMlplusminusfirstone("-");
				mre.setMlinputfirstone("135");
				mre.setId(new Long(277));
				mre.setEventname("#277 San Francisco 49ers -135");
				mre.setEventtype("ml");
				mre.setSport("nflfirst"); 
				mre.setUserid(new Long(6));
				mre.setEventid(new Integer(277));
				mre.setEventid1(new Integer(277));
				mre.setEventid2(new Integer(278));
				mre.setRotationid(new Integer(277));
				mre.setWtype("2");
				mre.setAmount("50");
	
				// Step 9 - Find event
				EventPackage eventPackage = processSite.findEvent(ep, mre);
				if (eventPackage == null) {
					throw new BatchException(BatchErrorCodes.GAME_NOT_AVAILABLE, BatchErrorMessage.GAME_NOT_AVAILABLE, "Game cannot be found on site for " + mre.getEventname(), xhtml);
				}

				SiteTransaction siteTransaction = new SiteTransaction();
				AccountEvent ae = new AccountEvent();
				ae.setMaxmlamount(new Integer(500));
				xhtml = processSite.selectEvent(siteTransaction, eventPackage, mre, ae);
				LOGGER.debug("xhtml: " + xhtml);
		
				// Step 13 - Parse the event selection
				processSite.parseEventSelection(xhtml, siteTransaction, eventPackage, mre, ae);*/
				
				/**
				 ************************************************************************
				 *---------------------Football NFL Code end here----------------------------------
				 ************************************************************************
				 */
				
				
				
				
				
				
				
				
				
				////////////Old Commented Code//////////////////
				
//			    String xhtml = "<!DOCTYPE HTML PUBLIC \"-/W3C/DTD XHTML 1.0 Transitional/EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">	<html>    <HEAD>	<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">	<meta name=\"description\" content=\"Bet with Us\">        <!--<meta name=\"google-translate-customization\" content=\"850af3aab9df52c0-1111978e695f2729-g87703a3f0fd07b91-d\"></meta>-->	<link rel=\"stylesheet\" type=\"text/css\" href=\"/Qubic/images/themes/theme2/theme2.css?v=86\">    <link rel=\"stylesheet\" type=\"text/css\" href=\"/Qubic/qubic/scripts/coverflow/coverflow.css?v=86\">        	<link rel=\"stylesheet\" type=\"text/css\" href=\"/Qubic/css/commun.css?version=86\">  		<!--<link rel=\"stylesheet/less\" type=\"text/css\" href=\"http://sportswidgets.pro/app/app.less?v=2&version=86\" />-->    <link rel=\"stylesheet\" type=\"text/css\" href=\"/Qubic/qubic/scripts/tooltipster/tooltipster.css?version=86\" />	<script type=\"text/javascript\" src=\"/Qubic/qubic/scripts/fancybox/jquery-1.4.2.min.js?version=86\"></script>	<script type=\"text/javascript\" src=\"/Qubic/qubic/scripts/fancybox/jquery.mousewheel-3.0.4.pack.js?version=86\"></script>    <script type=\"text/javascript\" src=\"/Qubic/qubic/scripts/fancybox/jquery.fancybox-1.3.4.js?version=86\"></script>    <script type=\"text/javascript\" src=\"/Qubic/qubic/scripts/autosugest/autosuggest.js?version=86\"></script>    <script type=\"text/javascript\" src=\"/Qubic/qubic/scripts/autosugest/autosuggestinit.js?version=86\"></script>    <script type=\"text/javascript\" src=\"/Qubic/scripts/CommonJavaScript.js?version=86\"></script>    <script type=\"text/javascript\" src=\"/Qubic/qubic/scripts/tooltipster/jquery.tooltipster.js?version=86\"></script>    <script type=\"text/javascript\" src=\"/Qubic/qubic/scripts/ticker/facescroll.js?version=86\"></script>    <script type=\"text/javascript\" src=\"/Qubic/qubic/scripts/ticker/ticker.js?version=86\"></script>    <script type=\"text/javascript\" src=\"/Qubic/qubic/scripts/countdown/countdown.js?version=86\"></script>    <script type=\"text/javascript\" src=\"/Qubic/qubic/scripts/ticker/jquery-ui.min.js?version=86\"></script>	<link rel=\"stylesheet\" type=\"text/css\" href=\"/Qubic/qubic/scripts/fancybox/jquery.fancybox-1.3.4.css?version=86\" media=\"screen\" />	<title>GA977 | Home</title>	<script type=\"text/javascript\">		function loadSettings(){			$.fancybox.showActivity();			$.ajax({				type	: \"GET\",				cache	: false,				url		: \"PlayerSettings.php\",				data	: \"ip=1\",				success: function(data) {					$.fancybox(data);				}			});			return false;		}		function loadNewFeatures(){			$.fancybox.showActivity();			$.ajax({				type	: \"GET\",				cache	: false,				url		: \"Newfeatures.php\",				data	: \"ip=1\",				success: function(data) {					$.fancybox(data);				}			});			return false;		}	</script>    <!--[if IE]>		<style>           	#trends .inner{			width:567px !important;			overflow:hidden !important;			zoom:1 !important;			position:relative !important;			margin-left:284px !important;			top:0px !important;			}            .tooltipster-default {				border-radius: 5px;				border: 1px solid #362D2F;				background: transparent #000;				color: #fff;				width:380px;				height:90px;				background-color:#000;				opacity:0.8;				filter:alpha(opacity=80); /* For IE8 and earlier */			}         </style>	<![endif]-->    <!--[if lte IE 6]>    <script src=\"/Qubic/qubic/scripts/pngfixie6/DD_belatedPNG_0.0.8a-min.js\"></script>    <![endif]-->		 <script type=\"text/javascript\">	$(document).ready(function() {  		var page={};         		$(function() {			new FrontPage().init();		});				$(function() {			$('.tooltip').tooltipster({				animation: 'grow',				position: 'bottom'			});			$('.limitstooltip').tooltipster({				animation: 'grow',				position: 'top'			});		});			    $('.inner2').vTicker({			speed: 500,			pause: 3000,			showItems: 3,			animation: 'fade',			mousePause: true,			height: 0,			direction: 'up'		});		page.trendDescriptions = {};		loadTrendDescriptions();			   var badBrowser = (/MSIE ((5\\.5)|6)/.test(navigator.userAgent) && navigator.platform == \"Win32\");	   if (badBrowser) {		   DD_belatedPNG.fix('img');			$('body').supersleight();	   }	   hideTickerFast()	});	</script>	<SCRIPT ID=clientEventHandlersJS LANGUAGE=javascript>function goto(page){	window.location.href=page;}//--></SCRIPT></HEAD><BODY onScroll=\"if (typeof balance != 'undefined' )scrollAdj(balance)\"> 		<div style=\"position:relative; top:0; padding:3px; width:987px; margin:auto auto; text-align:center;\" align=\"center\">		        <div id=\"newfeatures\" align=\"center\">New Features <a id=\"newfeaureslink\" style=\"color:#FD1342; cursor:pointer\" onclick=\"loadNewFeatures()\">(click here)</a></div>        <div id=\"ticker_system\" class=\"fbChatSidebar\">			            <div id=\"vertical-ticker\" >                <table cellpadding=\"0\" cellspacing=\"0\"><tr valign=\"top\"><td  onclick=\"showTicker();\" style=\"cursor:pointer\">                <li>              	<div class='desc' style='width:300px; height:12px;'><div style='font-size:12px;font-size-adjust: 0.5; width:100%; overflow-x:hidden; overflow-y:auto' >No New Messages</div></div>                </li>                </td></tr>                <tr><td align=\"center\" style=\" background-color:#000; position:relative; cursor:pointer; background-image:url(/qubic/scripts/countdown/img/scroll.png); background-size: 30px 8px; height:8px; background-repeat:no-repeat; text-align:center; background-position:center \" onclick=\"showTicker();\"></td></tr>                </table>            </div>			         </div>         		 		 <div id=\"google_language_drop\">		    <div id=\"google_translate_element\" class=\"-blank\">		   <select name=\"language\" onChange=\"javascript:window.location=window.location.href.split('?')[0]+'?lang=' + this.value\">		    <option value=\"English\"selected >English</option><option value=\"Spanish\" >Spanish</option><option value=\"Chinese (Simp)\" >Chinese (Simp)</option><option value=\"French\" >French</option><option value=\"Korean\" >Korean</option><option value=\"Japanese\" >Japanese</option><option value=\"Vietnamese\" >Vietnamese</option>		   </select>		   </div>		 </div>				 		 <!--myluckybuck block-->		 			 <!--end myluckybuck block-->     </div>     <!-- End TranslateThis Button -->    <script type=\"text/javascript\">	var estadoSlinkS=1;	function newFeaturesLinkS(){		if(estadoSlinkS==1){			document.getElementById(\"newfeaureslink\").style.color=\"#fff\";			estadoSlinkS=0;		}		else{			estadoSlinkS=1;			document.getElementById(\"newfeaureslink\").style.color=\"#FD1342\";		}	}	setInterval(\"newFeaturesLinkS()\",1000);    </script>            <div id=\"wrapper\" class=\"fix-wrapper\">	      	<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" >			<tr>				<td id=\"clientName\" nowrap=\"nowrap\" style=\"text-align:center\" >&nbsp;<um><strong>Account:</strong></um>&nbsp;<strong>GA977</strong>                </td>				                    		    <td nowrap=\"nowrap\" id=\"currentBalanceField\" style=\"text-align:center;\">&nbsp;<um><strong>Current Balance:</strong></um>&nbsp; <font color=red>USD&nbsp;-80</font>&nbsp;								</td>				                    		    <td nowrap=\"nowrap\" id=\"totalPendingField\" style=\"text-align:center\">&nbsp;<a href=\"SpecificPlayerPendingWagers.php\"><um><strong><font color=\"white\" size=\"2px\">Total Pending:</strong></um>&nbsp; USD&nbsp;3,830.00&nbsp;</font></a>				</td>                    	    	<td nowrap=\"nowrap\" id=\"currentAvailableField\" style=\"border-left:1px solid #fff; text-align:center\">                	        		<strong>&nbsp;<um>Current Available:</um>&nbsp;</strong>                    	<span style=\"color:#00ADEE\">                        	USD&nbsp;16,090.0 &nbsp;                        </span>				</td>				    		    <td nowrap=\"nowrap\" id=\"logoutField\" style=\"text-align:center\">        			<Strong>                    	&nbsp;                    	<a href=\"LogoutSucessful.php\" style=\"color:red; font-size:16px;\">Log Out</a>                        &nbsp;                    </Strong>				</td>							</tr>		</table>		        <div class=\"pages\" style=\"margin-left:auto; margin-right:auto\">        	<table id=\"table_menu\" width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-size:14px\">            	<tr height=\"26px\" >                <!--Straight cases-->                  	                    <!--Straight activo-->					<td class=\"menuSelection\" width=\"180px\" style=\"background-image:url(images/images_topbar/main-bar-blue-1px.png); background-repeat: repeat-x; cursor:pointer\" onmouseover=\"arrow0.style.backgroundPosition='0 -52px'\" onmouseout=\"arrow0.style.backgroundPosition='0 -52px'\"><A href=\"StraightSportSelection.php\" style=\"line-height: 26px;\"><center>Straight bets</center></a></td>                    <td width=\"17px\" style=\"background-image:url(images/images_topbar/main-bar-arrows.png); background-repeat:no-repeat; background-position:0 -52px;\" id=\"arrow0\">&nbsp;</td>                                    	<!--Teaser cases-->                                         <!--Straight active && teaser over-->                    <td class=\"menuSelection\" width=\"180px\" style=\"background-image:url(images/images_topbar/main-bar-black-1px.png); background-repeat: repeat-x; cursor:pointer\" onmouseover=\"arrow1.style.backgroundPosition='0 -52px'; arrow0.style.backgroundPosition='0 -182px'\" onmouseout=\"arrow1.style.backgroundPosition='0 0'; arrow0.style.backgroundPosition='0 -52px'\" ><A href=\"TeaserTeaserSelect.php\" style=\"line-height: 26px;\"><center>Teasers</center></a></td>					<td width=\"14px\" style=\"background-image:url(images/images_topbar/main-bar-arrows.png); background-repeat:no-repeat; background-position:0 0\" id=\"arrow1\">&nbsp;&nbsp;</td>                                         <!--Parlay cases-->                                         <!--parlay no active && teaser no active-->                    <td class=\"menuSelection\" width=\"180px\" style=\"background-image:url(images/images_topbar/main-bar-black-1px.png); background-repeat: repeat-x; cursor:pointer\" onmouseover=\"arrow2.style.backgroundPosition='0 -52px'; arrow1.style.backgroundPosition='0 -26px'\" onmouseout=\"arrow2.style.backgroundPosition='0 0'; arrow1.style.backgroundPosition='0 0'\"><A href=\"StraightSportSelection.php?wagerType=Parlay\" style=\"line-height: 26px;\"><center>Parlays/RR</center></a></td>                    <td width=\"14px\" style=\"background-image:url(images/images_topbar/main-bar-arrows.png); background-repeat:no-repeat; background-position:0 0\" id=\"arrow2\">&nbsp;&nbsp;</td>					                	<!--If bets cases-->                                        <td class=\"menuSelection\" width=\"180px\" style=\"background-image:url(images/images_topbar/main-bar-black-1px.png); background-repeat: repeat-x; cursor:pointer\" onmouseover=\"arrow3.style.backgroundPosition='0 -52px'; arrow2.style.backgroundPosition='0 -26px'\" onmouseout=\"arrow3.style.backgroundPosition='0 0'; arrow2.style.backgroundPosition='0 0'\"><A href=\"IfbetsSportSelection.php\" style=\"line-height: 26px;\"><center>If Bets</center></a></td>                    					<td width=\"14px\" style=\"background-image:url(images/images_topbar/main-bar-arrows.png); background-repeat:no-repeat; background-position:0 0\" id=\"arrow3\">&nbsp;&nbsp;</td>												<!--Action reverse cases-->														 <!--if bet active action reverse over-->							 							<td class=\"menuSelection\" width=\"180px\" style=\"background-image:url(images/images_topbar/main-bar-black-1px.png); background-repeat: repeat-x; cursor:pointer\" onmouseover=\"arrow4.style.backgroundPosition='0 -156px'; arrow3.style.backgroundPosition='0 -26px'\" onmouseout=\"arrow4.style.backgroundPosition='0 -104px'; arrow3.style.backgroundPosition='0 0'\"><A href=\"StraightSportSelection.php?wagerType=Action%20Reverse\" style=\"line-height: 26px;\"><center>Action Reverse</center></a></td>							<td width=\"14px\" style=\"background-image:url(images/images_topbar/main-bar-arrows.png); background-repeat:no-repeat; background-position:0 -104px\" id=\"arrow4\">&nbsp;&nbsp;</td>							 							<!--Turbo props cases-->														<td class=\"menuSelection\" id=\"navcasino\" width=\"250px\" style=\"background-image:url(images/images_topbar/main-bar-red-1px.png); background-repeat: repeat-x; cursor:pointer\" onmouseover=\"arrow5.style.backgroundPosition='0 -52px'; arrow4.style.backgroundPosition='0 -26px'\" onmouseout=\"arrow5.style.backgroundPosition='0 -78px'; arrow4.style.backgroundPosition='0 -104px'\">															  <A href=\"#\" style=\"line-height: 26px;\"><center  style=\"color:#AAAAAA;\"></center></a>							    							</td>							<!--<td width=\"14px\" style=\"background-image:url(images/images_topbar/main-bar-arrows.png); background-repeat:no-repeat; background-position:0 -78px\" id=\"arrow5\">&nbsp;&nbsp;</td>-->                            							<!--horses Cases-->														<td width=\"14px\" style=\"background-image:url(images/images_topbar/main-bar-arrows.png); background-repeat:no-repeat; background-position:0 -78px\" id=\"arrow5\">&nbsp;&nbsp;</td>														 <!--no props active && no casino active-->							<td class=\"menuSelection\" width=\"180px\" style=\"background-image:url(images/images_topbar/main-bar-black-1px.png); background-repeat: repeat-x; cursor:pointer\" onmouseover=\"arrow6.style.backgroundPosition='0 -52px'; arrow5.style.backgroundPosition='0 -130px'\" onmouseout=\"arrow6.style.backgroundPosition='0 0'; arrow5.style.backgroundPosition='0 -78px'\">							 <center style='color:#AAAAAA'></center>							</td>														<!--casino Cases-->														<td width=\"14px\" style=\"background-image:url(images/images_topbar/main-bar-arrows.png); background-repeat:no-repeat; background-position:0 0\" id=\"arrow6\">&nbsp;&nbsp;</td>							<!--<td class=\"menuSelection\" width=\"180px\" style=\"background-image:url(images/images_topbar/main-bar-black-1px.png); background-repeat: repeat-x; cursor:pointer\" onmouseover=\"arrow6.style.backgroundPosition='0 -26px'\" onmouseout=\"arrow6.style.backgroundPosition='0 0'\">-->														<td id=\"navcasino\" class=\"menuSelection\" width=\"180px\" style=\"background-image:url(images/images_topbar/main-bar-black-1px.png); background-repeat: repeat-x; cursor:pointer\" onmouseover=\"arrow6.style.backgroundPosition='0 -26px'\" onmouseout=\"arrow6.style.backgroundPosition='0 0'\">														 <A href=\"#\" style=\"line-height: 26px;\"><center  style=\"color:#AAAAAA;\"></center></a>							 							 </td>							 								                </tr>  			</table>            </div>			            <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\">                <tr>                    <td id=\"tdCategoriesContainer\">                        <div class=\"categories\" style=\"margin:3px auto 0 auto;\">                            <table width=\"928px\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-size:12px; background-image:url(images/images_topbar/options_fullbg.png); background-repeat:no-repeat;\">                                <tr>                                    <td id=\"categoriesHomeField\" style=\"text-align:center\"><a style=\"color:#fff\" href=\"StraightLoginSportSelection.php\"><center><div class=\"spriteHome\"><br></div>Home</center></a></td>                                    <td style=\"border-left:1px solid #fff;\"></td>                                    <td class=\"categoriesStandardFields\" style=\"text-align:center\" ><a style=\"color:#fff\" href=\"Scores.php\"><center><div class=\"spriteScores\"><br></div>Scores</center></a></td>                                     <td style=\"border-left:1px solid #fff;\"></td>                                    <!--<td class=\"categoriesStandardFields\" style=\"text-align:center\"><a style=\"color:#fff\" href=\"PlayerTransactions.php?transactionListDays=1\"><center><div class=\"spriteReports\"><br></div>Reports</center></a></td>-->                                    <td style=\"border-left:1px solid #fff;\"></td>									                                   		<td class=\"categoriesStandardFields\" style=\"text-align:center\"><a style=\"color:#fff\" href=\"SpecificPlayerPendingWagers.php\" ><center><div class=\"spritePending\" ><br></div><div id=\"pendingWagersNumberE\"><strong><center>12</center></strong></div><span style=\"padding:0 0 0 15px\">Pending Wagers </span></center></a></td>																			 <td style=\"border-left:1px solid #fff;\"></td>                                    <td class=\"categoriesStandardFields\" style=\"text-align:center\"><center><a style=\"color:#fff\" href=\"PlayerDailyFigure.php\"><div class=\"spriteDailyFigure\" ><br></div><span>Daily Figures</span></center></a></td>                                    <td style=\"border-left:1px solid #fff;\"></td>                                    <td class=\"categoriesStandardFields\" style=\"text-align:center\"><center><a style=\"color:#fff\" href=\"SpecificPlayerWeeklyFigure.php\"><div class=\"spriteWeeklyFigure\" ><br></div><span>Graded Wagers</span></center></a></td>                                     <td style=\"border-left:1px solid #fff;\"></td>                                    <td id=\"categoriesTransactionsField\" style=\"text-align:center\"><center><a style=\"color:#fff\" href=\"SpecificPlayerTransactions.php\"><div class=\"spriteTransaction\" ><br></div><span>Transactions</span></center></a></td>                                     <td style=\"border-left:1px solid #fff;\"></td>                                                                         <td class=\"categoriesStandardFields\" style=\"text-align:center; padding:0 0 0 0\"><center><a style=\"color:#fff\" href=\"PanelMessage.php\" ><div class=\"spriteMessages\" ><br></div><div id=\"messagesNumberE\" ><strong><center>                                     0                                    </center></strong></div><span>Messages</span></center></a></td>									 									 <td style=\"border-left:1px solid #fff;\"></td>									<td class=\"categoriesStandardFields\" style=\"text-align:center; padding:0 20px 0 0\">									    									  <center><a style=\"color:#fff\" href=\"PlayerPanic.php\"><div class=\"spriteIconBoss\" ><br></div><span>Boss</span></center></a>									  									</td>                                </tr>                            </table>                        </div>        	   		 </td>	        	</tr>                <tr>                	<td align=\"left\">                    	                        <div id=\"trends2\" class=\"container\" align=\"left\" >                         	<div id=\"scores_tool\" class=\"inner2\" style=\"height:31px!important\">                                <span class=\"trend-label2\">Result:</span>                                <span class=\"trend-label2\" style=\"position:relative; left:95px\">NEWS:</span>						      	<ul id=\"ul_scores\" class=\"trendscontent2\" style=\"zoom: 1;position:relative;top:0px\">                                	<li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/Maryland Terrapins.png' ></td><td align=center>Football            <br>null | null</td><td><img width=86px src='logos/Maryland Terrapins.png' ></td></tr><tr valign=top><td align=center>Maryland RSW</td><td></td><td align=center>Maryland RSW.</td></tr></table>\">Mary  null - null Mary</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/japanese_bl.png' ></td><td align=center>Baseball            <br>13 | 3</td><td><img width=86px src='logos/japanese_bl.png' ></td></tr><tr valign=top><td align=center>Hanshin Tigers</td><td></td><td align=center>Hiroshima Carp</td></tr></table>\">Hans  13 - 3 Hiro</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/japanese_bl.png' ></td><td align=center>Baseball            <br>9 | 3</td><td><img width=86px src='logos/japanese_bl.png' ></td></tr><tr valign=top><td align=center>Chunichi Dragons</td><td></td><td align=center>Yakult Swallows</td></tr></table>\">Chun  9 - 3 Yaku</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/japanese_bl.png' ></td><td align=center>Baseball            <br>2 | 1</td><td><img width=86px src='logos/japanese_bl.png' ></td></tr><tr valign=top><td align=center>Fukuoka S Hawks</td><td></td><td align=center>Chiba Lotte Marines</td></tr></table>\">Fuku  2 - 1 Chib</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/korea_bl.png' ></td><td align=center>Baseball            <br>2 | 7</td><td><img width=86px src='logos/korea_bl.png' ></td></tr><tr valign=top><td align=center>Nexen Heroes</td><td></td><td align=center>Kia Tigers</td></tr></table>\">Nexe  2 - 7 Kia </a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/korea_bl.png' ></td><td align=center>Baseball            <br>0 | 10</td><td><img width=86px src='logos/korea_bl.png' ></td></tr><tr valign=top><td align=center>SK Wyverns</td><td></td><td align=center>Lotte Giants</td></tr></table>\">SK W  0 - 10 Lott</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/korea_bl.png' ></td><td align=center>Baseball            <br>3 | 6</td><td><img width=86px src='logos/korea_bl.png' ></td></tr><tr valign=top><td align=center>NC Dinos</td><td></td><td align=center>LG Twins</td></tr></table>\">NC D  3 - 6 LG T</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/korea_bl.png' ></td><td align=center>Baseball            <br>6 | 2</td><td><img width=86px src='logos/korea_bl.png' ></td></tr><tr valign=top><td align=center>Doosan Bears</td><td></td><td align=center>Samsung Lions</td></tr></table>\">Doos  6 - 2 Sams</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/korea_bl.png' ></td><td align=center>Baseball            <br>9 | 2</td><td><img width=86px src='logos/korea_bl.png' ></td></tr><tr valign=top><td align=center>Hanwha Eagles</td><td></td><td align=center>KT Wiz Suwon</td></tr></table>\">Hanw  9 - 2 KT W</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/hk_Switzerland.png' ></td><td align=center>Hockey              <br>2 | 6</td><td><img width=86px src='logos/hk_Czech Republic.png' ></td></tr><tr valign=top><td align=center>Neman Grodno</td><td></td><td align=center>Kometa Brno</td></tr></table>\">Nema  2 - 6 Kome</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/hk_Sweden.png' ></td><td align=center>Hockey              <br>2 | 6</td><td><img width=86px src='logos/hk_Switzerland.png' ></td></tr><tr valign=top><td align=center>Malmo</td><td></td><td align=center>Yunost Minsk</td></tr></table>\">Malm  2 - 6 Yuno</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/hk_Austria.png' ></td><td align=center>Hockey              <br>1 | 1</td><td><img width=86px src='logos/hk_Finland.png' ></td></tr><tr valign=top><td align=center>Bolzano</td><td></td><td align=center>IFK Helsinky</td></tr></table>\">Bolz  1 - 1 IFK </a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/hk_Switzerland.png' ></td><td align=center>Hockey              <br>3 | 5</td><td><img width=86px src='logos/hk_Finland.png' ></td></tr><tr valign=top><td align=center>Munchen</td><td></td><td align=center>TPS Turku</td></tr></table>\">Munc  3 - 5 TPS </a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/hk_Switzerland.png' ></td><td align=center>Hockey              <br>2 | 4</td><td><img width=86px src='logos/hk_Sweden.png' ></td></tr><tr valign=top><td align=center>Zurich</td><td></td><td align=center>Frolunda</td></tr></table>\">Zuri  2 - 4 Frol</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/hk_Switzerland.png' ></td><td align=center>Hockey              <br>2 | 6</td><td><img width=86px src='logos/hk_Sweden.png' ></td></tr><tr valign=top><td align=center>Tychy</td><td></td><td align=center>Skelleftea</td></tr></table>\">Tych  2 - 6 Skel</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/hk_Austria.png' ></td><td align=center>Hockey              <br>6 | 1</td><td><img width=86px src='logos/hk_Danmark.png' ></td></tr><tr valign=top><td align=center>Vienna Capitals</td><td></td><td align=center>Aalborg</td></tr></table>\">Vien  6 - 1 Aalb</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/hk_Switzerland.png' ></td><td align=center>Hockey              <br>2 | 4</td><td><img width=86px src='logos/hk_Austria.png' ></td></tr><tr valign=top><td align=center>Cardiff</td><td></td><td align=center>Salzburg</td></tr></table>\">Card  2 - 4 Salz</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/hk_Sweden.png' ></td><td align=center>Hockey              <br>3 | 4</td><td><img width=86px src='logos/hk_Switzerland.png' ></td></tr><tr valign=top><td align=center>Vaxjo</td><td></td><td align=center>Bern</td></tr></table>\">Vaxj  3 - 4 Bern</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/hk_Russia.png' ></td><td align=center>Hockey              <br>2 | 2</td><td><img width=86px src='logos/hk_Russia.png' ></td></tr><tr valign=top><td align=center>Slovan Bratislava</td><td></td><td align=center>Yekaterinburg</td></tr></table>\">Slov  2 - 2 Yeka</a></li><li ><a style=\"cursor:pointer\" class=\"search_link2 tooltip\" rel=\"<table cellpadding=4 cellspacing=4 width=250px><tr valign=top><td><img width=86px src='logos/hk_Russia.png' ></td><td align=center>Hockey              <br>2 | 1</td><td><img width=86px src='logos/hk_Russia.png' ></td></tr><tr valign=top><td align=center>CSKA Moscow</td><td></td><td align=center>Metallurg Magnitogorsk</td></tr></table>\">CSKA  2 - 1 Meta</a></li>                               	</ul>  							</div>  							<span class=\"fade fade-left2\">&nbsp;</span>						</div>                        <div id=\"trends\" align=\"left\">  							<div class=\"inner\">	 							<ul class=\"trendscontent\" style=\"zoom: 1;position:\">									 <li class=\"trend-label\">&nbsp;&nbsp;</li>                    				 <li>						            	<a href=\"#\" class=\"search_link\" name=\"\" rel=\"nofollow\">No news available...</a>						          	 </li>                                 </ul>  							</div>  							<span class=\"fade fade-left\">&nbsp;</span><span class=\"fade fade-right\">&nbsp;</span>						</div>                        <div style=\"width:100%\">                       		<div id=\"score_popup\" style=\"display:none;\">                            </div>                       </div>					   					</td>                </tr>        	</table>                      	<table cellspacing=\"0\" border=\"0\" width=\"100%\" align=\"left\">               <tr valign=\"top\">                    <td style=\" width:100%\" >                    	<div id=\"contentright\">                                                     <div class=\"postP\">                                                          <div  style=\" padding:3px; height:100%\"><div style='background-color:#fff; padding:3px; width:86.3%; margin:auto auto; text-align:center' align='center'><div class='offering_pair'><table cellspacing='0' cellpadding='0' class='tktable' style='width:100%' ><tr><th class=sptype>Wager type : Straight Bet </th></tr></table><table cellspacing='0' cellpadding='0' class='listingtable' ><TR class='offering_noun'><TD width=100% style='border-bottom:1px solid #333333; padding:3px' ><table cellspacing='0' cellpadding='0' width=100% ><TR class='gamenumb'><TD  width=15% >College</td><td><table cellpadding='2' cellspacing='2' ><tr valign='mdddle'><td align='center' style='border-top:transparent;padding:2px;font-weight:bold; font-size:13px'>&nbsp;&nbsp;&nbsp;</td><td align='left' style='border-top:transparent;padding:2px'><table cellpadding=\"0\" cellspacing=\"0\"><tr><td style=\"border-top:transparent;\"><img width=\"43px\" src=\"logos/Cincinnati Bearcats.png\" class=\"logo1\" ></td><td style=\"border-top:transparent;\"><img width=\"43px\" src=\"logos/Miami RedHawks.png\" class=\"logo1\" ></td></tr></table></td><td align='left' style='border-top:transparent;padding:2px'><span class=thedate3 >9/8/2018 8:00 PM -  (EST)</span><br /><span class=rotnumb >Cincinnati/Miami Ohio Ov 51</span><span class=rotnumb > -110 for Game   </span></td></tr></table>  </td><td align=right><span class='gamenumb'><um>Risking</um> <span style='color:#FF0000;font-weight:bold; font-size:14px'>330.00 USD</span> <um>To Win</um> <span style='color:#000000;font-weight:bold; font-size:14px'>300.00 USD</span>  </span></TD></TR><tr><td colspan=3><TABLE cellspacing='0' cellpadding='0' class=tktable><TR ><TD width=269px style='border-top:1px solid transparent' ><a  href='StraightVerifyWager.php?delete=L1_38' class='deleteSpecial' ><img border='0' src='/Qubic/qubic/images/deleteIcon.gif'></a><span class='tits'>&nbsp;&nbsp; Internet Max Wager: 500.00</span></TD><TD style='border-top:1px solid transparent'><span class='tits'><INPUT type='radio' onclick=javascript:goto('StraightVerifyWager.php?modify=true&radioxGame0=riskType') id='radiox' name='radioxGame0' value='riskType'>&nbsp;<um>Risk Amount</um>&nbsp;&nbsp;<INPUT type='radio' onclick=javascript:goto('StraightVerifyWager.php?modify=true&radioxGame0=toWinType') id='radiox' name='radioxGame0' value='toWinType' CHECKED>&nbsp;<um>To Win Amount</um>&nbsp;&nbsp;&nbsp;&nbsp; : 300</span></TD></TR></TABLE></td></tr></TABLE></td></tr></table><div class='offering_noun' width=\"100%\" style=\"background-color:#CC0000; color:#FFFFFF;font-size:14px;padding:3px;font-weight:bold;border:1px solid #000\">One or more lines have changed</div></div><div align=\"center\" style=\"background-color:#CCCCCC; padding:5px; border:1px solid #666666;width: 835px;margin-left: auto;margin-right: auto;\"><div class=\"block-content_box_review\"><FORM name=\"WagerVerificationForm\" method=\"post\" action=\"CheckAcceptancePassword.php\"><INPUT type=\"hidden\" id=inetWagerNumber name=inetWagerNumber value=\"0.9093566998322341\"><BR><BR>Please Review Wagers Carefully!  &amp;  Re-enter Password To Confirm Wagers </FONT><br /><INPUT type=\"password\" id=password class=input_login name=password size = 8>&nbsp;&nbsp;<INPUT type=\"submit\" value=\"Submit\" id=submit1 name=submit1></FORM><script type='text/javascript'>document.getElementById('password').focus();</script><BR><FORM name=\"Cancel\" method=\"post\" action=\"Error.php?err=41\"><INPUT type=\"submit\" value=\"Cancel\" id=\"cancel\" name=\"cancel\" style=\"margin-top:5px\"></FORM></div></div></table></div>	</div>                            </div>                        </div>                    </td>               </tr>        	</table>        </div>		        </div>		        <div id=\"new_footer\">    	<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"float:left;background-image:url(/Qubic/images/images_topbar/images_content/bg_footer.png); background-repeat:no-repeat; border-bottom:2px solid #D6D6D6;\" >        	<tr valign=\"bottom\">            	<td valign=\"top\" style=\"float:right; padding:10px 0 0 0\">                                                                <img src=\"images/images_topbar/images_content/phone-symbol.png\" class=\"phone_img\">                    	<span class=\"phone_img_text\" style=\"color:#FFF\">                                        <um>Wagering Phone</um><br>#1-877-347-0213      </span>                </td>                <td style=\" padding:2px 0 0 70px;\">				                 	<img src=\"images/images_topbar/images_content/settings_icon.png\"  style=\"cursor:pointer; \" onClick=\"javascript:loadSettings();\" class=\"settings_icon\">				                 </td>                <td  valign=\"middle\" >                	<img src=\"images/images_topbar/images_content/separador.png\" height=\"20px\" style=\"margin:0 30px 0 0\">                </td>				                	<td  style=\" padding:2px 0 0 0;\">                		<a href=\"Rules.php\"><img src=\"images/images_topbar/images_content/rules_icon.png\" class=\"rules_icon\"></a>                	</td>				                 <td  valign=\"middle\" >                	<img src=\"images/images_topbar/images_content/separador.png\" height=\"20px\" style=\"margin:0 30px 0 30px\">                </td>                <td style=\" padding:5px 0 0 0;\">                 				               		<a href=\"Contactus.php\"><img src=\"images/images_topbar/images_content/contact_icon.png\"  class=\"contact_icon\"></a>               	                </td>                <td align=\"center\" style=\" padding:0 20px 0 0;\" valign=\"middle\" class=\"copyright\">                	 All Rights Reserved.<br />Copyright 2018.                </td>            </tr>        </table>         <div style=\"height:15px\">&nbsp;</div>    </div>		</body>    </html>";
/*		    	Map<String, String> lines = processSite.BBP.processLineChange(xhtml);
			    LOGGER.error("lines: " + lines);
			    TotalRecordEvent tre = new TotalRecordEvent();
				tre.setTotalinputfirstone("50.0");
				tre.setTotaljuiceplusminusfirstone("-"); 
				tre.setTotalinputjuicefirstone("125");
			    processSite.determineTotalLineChange(tre, null, lines);
*/
/*
			    processSite.httpClientWrapper.setupHttpClient(BSITES[i][6]);
			    processSite.processTransaction = false;
			    processSite.timezone = BSITES[i][7];
				String xhtml = processSite.loginToSite(BSITES[i][1], BSITES[i][2]);

			    Map<String, String> map = processSite.getMenuFromSite("mlblines", xhtml);
			    LOGGER.debug("map: " + map);

				xhtml = processSite.selectSport("mlblines");
				LOGGER.debug("XHTML: " + xhtml);

				final Iterator<EventPackage> ep = processSite.parseGamesOnSite("mlblines", xhtml);
				
				while (ep.hasNext()) {
					EventPackage eventPackage = ep.next();
					LOGGER.debug("EventPackage: " + eventPackage);
				}
*/
/*
			    processSite.httpClientWrapper.setupHttpClient(BSITES[i][6]);
			    processSite.processTransaction = false;
			    processSite.timezone = BSITES[i][7];
				String xhtml = processSite.loginToSite(BSITES[i][1], BSITES[i][2]);

				final PreviewInput previewInput = new PreviewInput();
				previewInput.setLinetype("total");
				previewInput.setLineindicator("o");
				previewInput.setRotationid(new Integer(969));
				previewInput.setSporttype("mlblines");
				previewInput.setProxyname("New York");
				previewInput.setTimezone("ET");

				final PreviewOutput previewData = processSite.previewEvent(previewInput);
				LOGGER.error("PreviewData: " + previewData);
*/
/*
				final Set<PendingEvent> pendingEvents = processSite.getPendingBets(BSITES[i][0], BSITES[i][1], null);
				System.out.println("pendingEvents.size(): " + pendingEvents.size());
				final Iterator<PendingEvent> itr = pendingEvents.iterator();
				while (itr.hasNext()) {
					final PendingEvent pe = itr.next();
					LOGGER.error("PendingEventXXX: " + pe);
					if (pe.getDoposturl()) {
						processSite.doProcessPendingEvent(pe);
					}
				}
*/
			//}
		} catch (Throwable t) {
			LOGGER.error("Throwable Exception", t);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#getPendingBets(java.lang.String, java.lang.String, java.lang.Object)
	 */
	@Override
	public Set<PendingEvent> getPendingBets(String accountName, String accountId, Object anythingObject) throws BatchException {
		LOGGER.info("Entering getPendingBets()");
		LOGGER.debug("accountName: " + accountName);
		LOGGER.debug("accountId: " + accountId);
		Set<PendingEvent> pendingWagers = null;
		
		// Get time
		final Calendar date = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
		int hour = date.get(Calendar.HOUR_OF_DAY);

		// Check if we are between 6 am CT and 11 pm CT
		if (hour < 23 && hour > 5) {
			if (this.resetConnection) {
				LOGGER.error("Resetting the connection for " + accountName + " " + accountId);
				HttpClientUtils.closeQuietly(this.httpClientWrapper.getClient());
				this.httpClientWrapper.setupHttpClient(proxyName);
				this.loginToSite(this.httpClientWrapper.getUsername(), this.httpClientWrapper.getPassword());
				this.resetConnection = false;
			}
		} else {
			this.resetConnection = true;
		}

		try {
			// Get the pending bets data
			String xhtml = getSite(super.populateUrl(PENDING_BETS));
			LOGGER.debug("xhtml: " + xhtml);

			// Check for a captcha
			if (BBP.checkCaptcha(xhtml, "captcha", "modalcaptcha")) {
				processCaptcha(xhtml, true);

				xhtml = getSite(super.populateUrl(PENDING_BETS));
				LOGGER.debug("xhtml: " + xhtml);
			}

			if (xhtml != null && xhtml.contains("Pending Wagers")) {
				pendingWagers = BBP.parsePendingBets(xhtml, accountName, accountId);
			} else if (xhtml != null && xhtml.contains("No pending wagers found")) {
				// Do nothing
				LOGGER.debug("No Open Bets");
				pendingWagers = new HashSet<PendingEvent>();
			} else {
				LOGGER.error("No pending wagers found for " + accountName + " " + accountId);
				if (xhtml != null && xhtml.contains("loginform")) {
					this.loginToSite(this.httpClientWrapper.getUsername(), this.httpClientWrapper.getPassword());
					xhtml = getSite(super.populateUrl(PENDING_BETS));
					pendingWagers = BBP.parsePendingBets(xhtml, accountName, accountId);
				}

				if (pendingWagers != null && pendingWagers.isEmpty()) {
					pendingWagers = null;
				}
			}
		} catch (BatchException be) {
			if (be.getErrormessage().contains("BAD GATEWAY")) {
				LOGGER.error("Resetting the connection for " + accountName + " " + accountId);
				HttpClientUtils.closeQuietly(this.httpClientWrapper.getClient());
				this.httpClientWrapper.setupHttpClient(proxyName);
				this.loginToSite(this.httpClientWrapper.getUsername(), this.httpClientWrapper.getPassword());
				String xhtml = getSite(super.populateUrl(PENDING_BETS));
				pendingWagers = BBP.parsePendingBets(xhtml, accountName, accountId);
			}

			if (pendingWagers != null && pendingWagers.isEmpty()) {
				pendingWagers = null;
			}
		}

		LOGGER.info("Exiting getPendingBets()");
		return pendingWagers;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#doProcessPendingEvent(com.ticketadvantage.services.model.PendingEvent)
	 */
	@Override
	public void doProcessPendingEvent(PendingEvent pe) throws BatchException {
		LOGGER.info("Entering doProcessPendingEvent()");

		try {
			if (pe.getPosturl().equals("Something here")) {
				final SportsInsightsSite processSite = new SportsInsightsSite("https://sportsinsights.actionnetwork.com",
						"mojaxsventures@gmail.com", "action1");
				final EventPackage ep = processSite.getEventById(pe.getRotationid(), pe.getGamesport());

				if (ep != null) {
					// Get game type
					pe.setGametype(ep.getSporttype());
					pe.setGamedate(ep.getEventdatetime());
					pe.setEventdate(ep.getDateofevent() + " " + ep.getTimeofevent());
				}				
			} else {
				String xhtml = getSite(populateUrl(pe.getPosturl()));
				LOGGER.debug("XHTML: " + xhtml);
	
				// Get game type
				String gameType = BBP.processTicketInfo(xhtml);
				pe.setGametype(gameType);
			}
		} catch (Throwable t) {
			LOGGER.error("PendingEvent: " + pe);
			LOGGER.error(t.getMessage(), t);
		}

		LOGGER.info("Exiting doProcessPendingEvent()");
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
		BBP.setTimezone(super.timezone);

		// Get index page
		String xhtml = getSite(httpClientWrapper.getHost());
		LOGGER.debug("XHTML: " + xhtml);

		// Parse home page
		MAP_DATA = BBP.parseIndex(xhtml);
		this.httpClientWrapper.setWebappname(determineWebappName(MAP_DATA.get("action")));

		// Customer ID
		if (MAP_DATA.containsKey("customerID")) {
			MAP_DATA.put("customerID", username);	
		} else if (MAP_DATA.containsKey("CustomerID")) {
			MAP_DATA.put("CustomerID", username);
		} else if (MAP_DATA.containsKey("Customerid")) {
			MAP_DATA.put("Customerid", username);
		}

		// Password
		if (MAP_DATA.containsKey("password")) {
			MAP_DATA.put("password", password);
		} else if (MAP_DATA.containsKey("Password")) {
			MAP_DATA.put("Password", password);
		}

		// Goto
		if (MAP_DATA.containsKey("goto")) {
			MAP_DATA.put("goto", "S");
		} else if (MAP_DATA.containsKey("Goto")) {
			MAP_DATA.put("Goto", "S");
		} else if (MAP_DATA.containsKey("GoTo")) {
			MAP_DATA.put("GoTo", "S");
		}
		
		String ioBB="0400R9HVeoYv1gsNf94lis1ztjbaCjtwdIzBYiWpmjG01orwbJh8UxNrEK14rzTOrTUJWgGaTLZtl2bXnfra5uHnDwI+Y73H2L2OdUsprl5Sq8nl3OCiMt8Ua81mEx5T4nMebTYB1KlkYda93ElhrQEAQAz1gslwtNv6MLejutG4hxK7avbbBujD3ygO+Uqpxb++sZBfgyIR+XyE6hsi11FUlhkwfCqvl92YChvQ5GutOWzve+8P12kkDUeSkz80AG0yHSZAf9LhiHq1DOkZ9ybRxHxfYV3WeA0UWmgkuX2XzwM1yN2dvZSeFG4d+pwMWuE5Hg58zz2F7zmo8Jk/JATNMZVPfVKB9sZJKbNfIMCtRg51syaJc0slM63r3outOgUonQ6LgRQcIPKIfQ+IGAhcOSt4Ph3Yj718X6t2xsBNqMBA7uS6dlzrD+udjcKV+pZNo2Ji61Ck5rdiHsRUrA6QS4uoKmLN7sWcP2Mr+/EBlpYwZ8YN8wmMvarl1uapdapMyRXCTlNZlTaeHkDCNxUwlLmZnC9N+MV7hZEdr7sAeq299dtMxey6qIz3vhZjySChDX8icOWPZcwZEc35aCNHHTQif/Jx02a+stVoIB8126q9arikGqm6MDntEcVmW1kM3pv3lBngSakZMHwqr5fdmAob0ORrrTlsoZTHMTQKud1kweUabENr/qjGKr+my2FZKjb/hgYI8w7sTU9/joi9e1h0TIw5qQXOmssqTe7WzQg67PDmZdnwAtyLKVLF/4cvsBx6w1tx0mMIHbmSmbjrEjrZ99hgK8uhEMCy0s3hRLlX3uHnT/mMqyp/6MAGBzULS3iG0t1s5leHZLzv7FVPWb1quKQaqbowy/ZGi/OZc1PWD1Ek94E2JxvvIApWVhTfoGVtYyeLHLRhEJESxSJLMa+Vx63F6egNooPmHSsxiCQQwLLSzeFEufTxOH0WU0cMwkGouP5PPyvQ78T0AGn/17sCyzUxPaqct7hGJwg9yd1l3zEA4KQ4ZY9H0hVKpR9oXl1+meSMyUSTNoh+6DybbJUqq/sNnUdQGx0bcfp/3OaF6H4pTAs8F8Su9hi33Gs4J86rW8bYI5+eSV/hdTCQ52Hk3C7XHo8gNzDXDkaW//gEMv5tsr1ng02cD/qjZPRxU7cmEOu0IghWGrecBg4p0KAjMOE+2ZXixXFK/BI6Jc3XRBuNE0bsW+UropOB0vdPFxa8Swx13b4lEesxs9YZpfLKopC71TAMuG/RPRi4K8tLBxDXtKHAP3F5JrEJLmSQRBQYQDurupTNjlyBCK5/xHA+k9XRlIy/oFeCnfm5mYP9Vl9cn/JYT6i1tQKx2bAG5uR0j3gm35JH1m8q/ZBPAQwDkKx3H7kFliy11neb+tQx/yv01xPTxC5TINup9O2qSfWHgu4dWAZinW1kS6LZSaPXP1JfyVuwKElmku/BJdU=";
		String ioBB2="04009RGBO2YE2LoHcCiyOFFxUKmACfkkA+SW7TfUHMAsnWp4EnXgDMFkWQ1/3iWKzXO2NtoKO3B0jMFiJamaMbTWivBsmHxTE2sQrXivNM6tNQlaAZpMtm2XZmO7P/zAA1mnx0vwmCHSP5Td0HwZFZsoI7u4fdGNP6Eciz/8E93rWp9tXM0h5j9EGyx/7EP7F6pNMg1EJAZttft4Xa0s3p8L9oiu6TAsPc3Unx/4t4Snaubk0B48ux+V4QZkghiV83r4spqfERVXY+y8VDdLGBfK9PH/LgnAtMTg2IfHpjJ8LmcXSJNqh+A8WZaHbSQQp9XbVXzq4ED4BymalMq2FdW8qkpkktyRyzPK6jB+ZmMavO7St0oGtrebhtwLFyUpHCDqam2siK+vP6Pr+PglytfMfwdOFszUl+aEYyzRP0TT2V5WC4Z0KPmWF4I/kVVuCPBkEcbSiHXpftPGcJpgXrX3OJrLKk3u1s0IOuzw5mXZ8AILob1lxjsp4X6mAJ3ZTHu2T0YAhnMKCEdadQeFciREm2oKi4DpJzqRDi8R6wVNV9r6qoxEqm2gQ71/Y9e8eiB47caT1pbMLWpGZE2amc9bFkjXzk77ILZ/Izg/443RtWyZ54b6OXcD9E8xAXTS4l7DUuS6AmuzoafeGrpO8GgDsow7nq/p/iHjOcSUrO+l4WWQfu013hqflQtuHmc6Pe7cNepEKIoJSuwSQy3zP1/E74hPJxX5Cmud4bkIibh3yGqbPrXOHMUHVzLKr7C9adXF/xXxz5iHJ2Ep2YEIihfM3LbeJmhTi+sjMgnCg+m9FzQlxGvfby+vvmIRvvbN1W/hBeiOThsX3U883Am5YVeVH3CEpLbakpDPisAa8OmFaFl85x1FdaJD0nBiemwGqQt8b3WsYoJ77B+Nc557XJoPFyQZ+QQTVZZaRW2VNSUjHuZ8YD1Y+HjfzXVLKa5eUqvJ5dzgojLfFGv3oU6k968Na1C/0PQwVK9AfF9hXdZ4DRSWGrAW95l2cA==";
		
		// ioBB
		if (MAP_DATA.containsKey("ioBB2")) {
			MAP_DATA.put("ioBB2", ioBB2);
		} else if (MAP_DATA.containsKey("IOBB2")) {
			MAP_DATA.put("IOBB2", ioBB2);
		}

		// ioBB
		if (MAP_DATA.containsKey("ioBB")) {
			MAP_DATA.put("ioBB", ioBB);
		} else if (MAP_DATA.containsKey("IOBB")) {
			MAP_DATA.put("IOBB", ioBB);
		}

		LOGGER.debug("MAP_DATA: " + MAP_DATA);

		List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
		String actionLogin = setupNameValuesEmpty(postValuePairs, MAP_DATA, httpClientWrapper.getWebappname());
		LOGGER.debug("ActionLogin: " + actionLogin);

		// Call the login
		xhtml = authenticate(actionLogin, postValuePairs);
		LOGGER.debug("XHTML: " + xhtml);

		//////////////////
		// call for sendcond validation
		MAP_DATA = BBP.parseIndex(xhtml);
		List<NameValuePair> postValuePairs1 = new ArrayList<NameValuePair>(1);
		String actionLogin1 = setupNameValuesEmpty(postValuePairs1, MAP_DATA, httpClientWrapper.getWebappname());
		LOGGER.debug("ActionLogin: " + actionLogin1);

		// Call the auth
		xhtml = authenticate(actionLogin1, postValuePairs1);
		LOGGER.debug("XHTML1: " + xhtml);

		///////// parse auth html
		MAP_DATA = BBP.parseIndex(xhtml);
		List<NameValuePair> postValuePairs2 = new ArrayList<NameValuePair>(1);
		String actionLogin2 = setupNameValuesEmpty(postValuePairs2, MAP_DATA, httpClientWrapper.getWebappname());
		LOGGER.debug("ActionLogin: " + actionLogin2);

		// call got get list of spots
		xhtml = postSite(actionLogin2, postValuePairs2);
		LOGGER.debug("XHTML2: " + xhtml);

		///////// parse auth html
		// MAP_DATA = BBP.parseIndex(xhtml);
		// List<NameValuePair> postValuePairs3 = new ArrayList<NameValuePair>(1);
		// String actionLogin3 = setupNameValuesEmpty(postValuePairs3, MAP_DATA,
		// httpClientWrapper.getWebappname());
		// LOGGER.debug("ActionLogin: " + actionLogin3);
		//
		// // call got get list of spots
		// xhtml = postSite(actionLogin3, postValuePairs3);
		// LOGGER.debug("XHTML2: " + xhtml);

		httpClientWrapper.setWebappname("");

		MAP_DATA.clear();
		MAP_DATA.put("gotFlash", "");
		MAP_DATA.put("action", "WagerMenu.asp");

		postValuePairs2 = new ArrayList<NameValuePair>(1);
		actionLogin2 = setupNameValuesEmpty(postValuePairs2, MAP_DATA, httpClientWrapper.getWebappname());
		LOGGER.debug("ActionLogin: " + actionLogin2);

		// call got get list of spots
		xhtml = postSite(actionLogin2, postValuePairs2);

		LOGGER.info("Exiting loginToSite()");
		return xhtml;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#selectSport(java.lang.String)
	 */
	@Override
	protected String selectSport(String type) throws BatchException {
		LOGGER.info("Entering selectSport()");
		LOGGER.debug("type: " + type);

		// Process the setup page
		List<NameValuePair> postNameValuePairs = new ArrayList<NameValuePair>(1);
		String actionName = setupNameValuesEmpty(postNameValuePairs, MAP_DATA, httpClientWrapper.getWebappname());
		LOGGER.debug("actionName: " + actionName);

		// Post to select sport type
		String xhtml = postSite(actionName, postNameValuePairs);
		LOGGER.debug("XHTML: " + xhtml);
		
		// Check for a captcha
		if (BBP.checkCaptcha(xhtml, "captcha", "modalcaptcha")) {
			processCaptcha(xhtml, true);
		}

		LOGGER.info("Exiting selectSport()");
		return xhtml;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#createSiteTransaction()
	 */
	@Override
	protected SiteTransaction createSiteTransaction() {
		LOGGER.info("Entering createSiteTransaction()");

		final SiteTransaction siteTransaction = new SiteTransaction();

		LOGGER.info("Exiting createSiteTransaction()");
		return siteTransaction;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#selectEvent(com.ticketadvantage.services.dao.sites.SiteTransaction, com.ticketadvantage.services.model.EventPackage, com.ticketadvantage.services.model.BaseRecordEvent, com.ticketadvantage.services.model.AccountEvent)
	 */
	@Override
	protected String selectEvent(SiteTransaction siteTransaction, EventPackage eventPackage, BaseRecordEvent event, AccountEvent ae) throws BatchException {
		LOGGER.info("Entering selectEvent()");
		String siteAmount = siteTransaction.getAmount();

		if (event instanceof SpreadRecordEvent) {
			siteTransaction = getSpreadTransaction((SpreadRecordEvent)event, eventPackage, ae);
			siteAmount = siteTransaction.getAmount();
			LOGGER.debug("siteAmount: " + siteAmount);
			Double sAmount = Double.parseDouble(siteAmount);
			if (sAmount != null) {
				FiveDimesEventPackage fiveDimesEventPackage = (FiveDimesEventPackage)eventPackage;
				LOGGER.debug("fiveDimesEventPackage: " + fiveDimesEventPackage);
				
				if (siteTransaction.getRiskorwin() != null && siteTransaction.getRiskorwin().intValue() == 1) {
					if (fiveDimesEventPackage != null && fiveDimesEventPackage.getSpreadMax() != null) {
						if (sAmount.doubleValue() > fiveDimesEventPackage.getSpreadMax().intValue()) {
							siteAmount = fiveDimesEventPackage.getSpreadMax().toString();
						}
					}
				} else {
					Double wagerAmount = Double.valueOf(siteAmount);
					float spreadJuice = ae.getSpreadjuice();
					Double risk = wagerAmount * (100 / spreadJuice);
					risk = round(risk.doubleValue(), 2);
					if (fiveDimesEventPackage != null && fiveDimesEventPackage.getSpreadMax() != null
							&& (risk.doubleValue() > fiveDimesEventPackage.getSpreadMax().intValue())) {
						LOGGER.error("Risk: " + risk);
						LOGGER.error("fiveDimesEventPackage.getSpreadMax().intValue(): "
								+ fiveDimesEventPackage.getSpreadMax().intValue());
						siteAmount = fiveDimesEventPackage.getSpreadMax().toString();
					} else {
						LOGGER.error("Risk: " + risk);
						siteAmount = risk.toString();
					}
				}
			}
		} else if (event instanceof TotalRecordEvent) {
			siteTransaction = getTotalTransaction((TotalRecordEvent)event, eventPackage, ae);
			siteAmount = siteTransaction.getAmount();
			LOGGER.debug("siteAmount: " + siteAmount);
			Double tAmount = Double.parseDouble(siteAmount);
			if (tAmount != null) {
				FiveDimesEventPackage fiveDimesEventPackage = (FiveDimesEventPackage)eventPackage;
				LOGGER.debug("fiveDimesEventPackage: " + fiveDimesEventPackage);
				
				if (siteTransaction.getRiskorwin() != null && siteTransaction.getRiskorwin().intValue() == 1) {
					if (fiveDimesEventPackage != null && fiveDimesEventPackage.getTotalMax() != null) {
						if (tAmount.doubleValue() > fiveDimesEventPackage.getTotalMax().intValue()) {
							siteAmount = fiveDimesEventPackage.getTotalMax().toString();
						}
					}
				} else {
					Double wagerAmount = Double.valueOf(siteAmount);
					float totalJuice = ae.getTotaljuice();
					Double risk = wagerAmount * (100 / totalJuice);
					risk = round(risk.doubleValue(), 2);
					if (fiveDimesEventPackage != null && fiveDimesEventPackage.getTotalMax() != null
							&& (risk.doubleValue() > fiveDimesEventPackage.getTotalMax().intValue())) {
						LOGGER.error("Risk: " + risk);
						LOGGER.error("fiveDimesEventPackage.getTotalMax().intValue(): "
								+ fiveDimesEventPackage.getTotalMax().intValue());
						siteAmount = fiveDimesEventPackage.getTotalMax().toString();
					} else {
						LOGGER.error("Risk: " + risk);
						siteAmount = risk.toString();
					}
				}
			}
		} else if (event instanceof MlRecordEvent) {
			siteTransaction = getMlTransaction((MlRecordEvent)event, eventPackage, ae);
			siteAmount = siteTransaction.getAmount();
			LOGGER.debug("siteAmount: " + siteAmount);
			Double mAmount = Double.parseDouble(siteAmount);
			if (mAmount != null) {
				FiveDimesEventPackage fiveDimesEventPackage = (FiveDimesEventPackage)eventPackage;
				LOGGER.debug("fiveDimesEventPackage: " + fiveDimesEventPackage);
				
				if (siteTransaction.getRiskorwin() != null && siteTransaction.getRiskorwin().intValue() == 1) {
					if (fiveDimesEventPackage != null && fiveDimesEventPackage.getMlMax() != null) {
						if (mAmount.doubleValue() > fiveDimesEventPackage.getMlMax().intValue()) {
							siteAmount = fiveDimesEventPackage.getMlMax().toString();
						}
					}
				} else {
					Double wagerAmount = Double.valueOf(siteAmount);
					float mlJuice = ae.getMljuice();
					Double risk = wagerAmount * (100 / mlJuice);
					risk = round(risk.doubleValue(), 2);
					if (fiveDimesEventPackage != null && fiveDimesEventPackage.getMlMax() != null
							&& (risk.doubleValue() > fiveDimesEventPackage.getMlMax().intValue())) {
						LOGGER.error("Risk: " + risk);
						LOGGER.error("fiveDimesEventPackage.getMlMax().intValue(): "
								+ fiveDimesEventPackage.getMlMax().intValue());
						siteAmount = fiveDimesEventPackage.getMlMax().toString();
					} else {
						if (fiveDimesEventPackage != null) {
							LOGGER.error("fiveDimesEventPackage.getMlMax(): "
									+ fiveDimesEventPackage.getMlMax());							
						}
						LOGGER.error("Risk: " + risk);
						siteAmount = risk.toString();
					}
				}
			}
		} else if (event instanceof TeamTotalRecordEvent) {
			siteTransaction = getTeamTotalTransaction((TeamTotalRecordEvent) event, eventPackage, ae);
			siteAmount = siteTransaction.getAmount();
			LOGGER.debug("siteAmount: " + siteAmount);
			Double tAmount = Double.parseDouble(siteAmount);
			if (tAmount != null) {
				FiveDimesEventPackage fiveDimesEventPackage = (FiveDimesEventPackage) eventPackage;
				LOGGER.debug("fiveDimesEventPackage: " + fiveDimesEventPackage);

				if (siteTransaction.getRiskorwin() != null && siteTransaction.getRiskorwin().intValue() == 1) {
					if (fiveDimesEventPackage != null && fiveDimesEventPackage.getTeamTotalMax() != null) {
						if (tAmount.doubleValue() > fiveDimesEventPackage.getTeamTotalMax().intValue()) {
							siteAmount = fiveDimesEventPackage.getTeamTotalMax().toString();
						}
					}
				} else {
					Double wagerAmount = Double.valueOf(siteAmount);
					float totalJuice = ae.getTotaljuice();
					Double risk = wagerAmount * (100 / totalJuice);
					risk = round(risk.doubleValue(), 2);
					if (fiveDimesEventPackage != null && fiveDimesEventPackage.getTeamTotalMax() != null
							&& (risk.doubleValue() > fiveDimesEventPackage.getTeamTotalMax().intValue())) {
						LOGGER.error("Risk: " + risk);
						LOGGER.error("fiveDimesEventPackage.getFirstTeamTotalMax().intValue(): "
								+ fiveDimesEventPackage.getTeamTotalMax().intValue());
						siteAmount = fiveDimesEventPackage.getTeamTotalMax().toString();
					} else {
						LOGGER.error("Risk: " + risk);
						siteAmount = risk.toString();
					}
				}
			}
		}

		// Setup the post parameters
		List<NameValuePair> postNameValuePairs = new ArrayList<NameValuePair>(1);
		MAP_DATA.put(siteTransaction.getInputName(), siteAmount);
		//MAP_DATA.put("S1_0", "55");
		ae.setActualamount(siteAmount);

		// Now check if selection needs to be sent as well
		if (siteTransaction.getSelectName() != null && siteTransaction.getOptionValue() != null) {
			MAP_DATA.put(siteTransaction.getSelectName(), siteTransaction.getOptionValue());
		}

		// Setup post parameters and get action URL
		final String actionName = setupNameValuesEmpty(postNameValuePairs, MAP_DATA, httpClientWrapper.getWebappname());
		LOGGER.debug("actionName: " + actionName);

		// Setup the wager
		String xhtml = postSite(actionName, postNameValuePairs);
		LOGGER.debug("XHTML: " + xhtml);

		// Check for a captcha
		if (BBP.checkCaptcha(xhtml, "captcha", "modalcaptcha")) {
			processCaptcha(xhtml, true);
		}

		LOGGER.info("Exiting selectEvent()");
		return xhtml;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#parseEventSelection(java.lang.String, com.ticketadvantage.services.dao.sites.SiteTransaction, com.ticketadvantage.services.model.EventPackage, com.ticketadvantage.services.model.BaseRecordEvent, com.ticketadvantage.services.model.AccountEvent)
	 */
	@Override
	protected void parseEventSelection(String xhtml, SiteTransaction siteTransaction, EventPackage eventPackage, BaseRecordEvent event, AccountEvent ae) throws BatchException {
		LOGGER.info("Entering parseEventSelection()");

		// Parse the event selection
		Map<String, String> wagers = null;
		try {
			wagers = BBP.parseWagerTypes(xhtml);
			LOGGER.debug("wagers: " + wagers);
		} catch (BatchException be) {
			if (be.getErrorcode() == BatchErrorCodes.LINE_CHANGED_ERROR) {
				xhtml = processLineChange(xhtml, event, ae);
				wagers = BBP.parseWagerTypes(xhtml);
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

				try {
					wagers = BBP.parseWagerTypes(xhtml);
					LOGGER.debug("wagers: " + wagers);
				} catch (BatchException be) {
					if (be.getErrorcode() == BatchErrorCodes.LINE_CHANGED_ERROR) {
						xhtml = processLineChange(xhtml, event, ae);
						wagers = BBP.parseWagerTypes(xhtml);
					} else {
						throw be;
					}
				}
				
				// Check for a wager limit and rerun
				if (wagers.containsKey("wageramount")) {
					throw new BatchException(BatchErrorCodes.MAX_WAGER_AMOUNT_REACHED, BatchErrorMessage.MAX_WAGER_AMOUNT_REACHED, "The maximum wager amount has been reached " + siteTransaction.getAmount());
				}
			} else {
				throw new BatchException(BatchErrorCodes.MAX_WAGER_AMOUNT_REACHED, BatchErrorMessage.MAX_WAGER_AMOUNT_REACHED, "The maximum wager amount has been reached " + siteTransaction.getAmount());
			}
		}
		MAP_DATA = wagers;

		if (wagers != null) {
			LOGGER.debug("wagerType: " + wagers);
			final String wagerType = wagers.get("wagerType");

			// Check for risk or to win
			if ("riskType".equals(wagerType)) {
				if (!"1".equals(event.getWtype())) {
					// Send win url
					xhtml = getSite(populateUrl(wagers.get("winurl")));
					LOGGER.debug("XHTML: " + xhtml);
				}
			} else if ("toWinType".equals(wagerType)) {
				if ("1".equals(event.getWtype())) {
					// Send risk url
					xhtml = getSite(populateUrl(wagers.get("riskurl")));
					LOGGER.debug("XHTML: " + xhtml);
				}				
			}
		}

		// Parse the wager information
		try {
			MAP_DATA = BBP.parseEventSelection(xhtml, null, null);
		} catch (BatchException be) {
			if (be.getErrorcode() == BatchErrorCodes.LINE_CHANGED_ERROR) {
				xhtml = processLineChange(xhtml, event, ae);
				MAP_DATA = BBP.parseEventSelection(xhtml, null, null);
			} else {
				throw be;
			}
		}
		
		LOGGER.info("Exiting parseEventSelection()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#completeTransaction(com.ticketadvantage.services.dao.sites.SiteTransaction, com.ticketadvantage.services.model.EventPackage, com.ticketadvantage.services.model.BaseRecordEvent, com.ticketadvantage.services.model.AccountEvent)
	 */
	@Override
	protected String completeTransaction(SiteTransaction siteTransaction, EventPackage eventPackage, BaseRecordEvent event, AccountEvent ae) throws BatchException {
		LOGGER.info("Entering completeTransaction()");
		String xhtml = null;

		try {
			// Check the acceptance
			if (processTransaction) {
				// Setup the post parameters
				List<NameValuePair> postNameValuePairs = new ArrayList<NameValuePair>(1);
				MAP_DATA.put("password", httpClientWrapper.getPassword());
				final String actionName = setupNameValuesWithName(postNameValuePairs, MAP_DATA, false, httpClientWrapper.getWebappname());
				LOGGER.debug("actionName: " + actionName);
				httpClientWrapper.setPreviousUrl(populateUrl("StraightVerifyWager.php"));

				// Send the wager confirmation
				xhtml = postSiteWithCheck(actionName, postNameValuePairs, "PlayerSbVerifyFixedWager");
				LOGGER.debug("XHTML: " + xhtml);

				// Check for line change
				if (xhtml.contains("One or more lines have changed")) {
					xhtml = processLineChange(xhtml, event, ae);
				}

				// Check for delayed timer scenario
				String wagerUrl = BBP.processCheckAcceptance(xhtml);
				if (wagerUrl != null) {
					// Sleep for 10 seconds
					sleepAsUser(10000);
					xhtml = getSiteWithCheck(populateUrl(wagerUrl), "PlayerSbVerifyFixedWager");
					LOGGER.debug("XHTML: " + xhtml);

					// Check for line change
					if (xhtml.contains("One or more lines have changed")) {
						xhtml = processLineChange(xhtml, event, ae);
					}
				}
			}
		} catch (BatchException be) {
			LOGGER.error("Exception completing transaction for account event " + ae + " event " + event, be);
			throw be;
		}

		LOGGER.info("Exiting completeTransaction()");		
		return xhtml;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#parseTicketTransaction(java.lang.String)
	 */
	@Override
	protected String parseTicketTransaction(String xhtml) throws BatchException {
		LOGGER.info("Entering parseTicketNumber()");
		
		final String ticketNumber = BBP.parseTicketNumber(xhtml);
		LOGGER.debug("ticketNumber: " + ticketNumber);

		LOGGER.info("Exiting parseTicketNumber()");
		return ticketNumber;		
	}

	/**
	 * 
	 * @param xhtmlData
	 * @throws BatchException
	 */
	private void processCaptcha(String xhtmlData, boolean anotherAttempt) throws BatchException {
		String purl = BBP.processCaptcha(xhtmlData);

		// Get the base captcha
		httpClientWrapper.getSitePage(populateUrl(purl), null, setupHeader(false));

		try {
			final CaptchaWorker captchaWorker = new CaptchaWorker(super.getHttpClientWrapper());
			final ExecutorService executor = Executors.newFixedThreadPool(5);
			Set<Future<Map<String, String>>> set = new HashSet<Future<Map<String, String>>>();
			
			// Call the base captcha
			String getCaptchaString = populateUrl("getcaptcha.asp?" + Math.random());
			getSite(getCaptchaString);
			
			for (int i = 1; i <= 5; i++) {
				GetCaptchaText gct = new GetCaptchaText(getCaptchaString, populateUrl("captcha.asp?captchaID=" + String.valueOf(i)), getCaptchaString, String.valueOf(i), captchaWorker);
				Callable<Map<String, String>> callable = gct;
				Future<Map<String, String>> future = executor.submit(callable);
				set.add(future);
			}
		
			String imgText1 = "";
			String imgText2 = "";
			String imgText3 = "";
			String imgText4 = "";
			String imgText5 = "";
			boolean error = false;

			for (Future<Map<String, String>> future : set) {
				Map<String, String>  map = future.get();
				Set<Entry<String, String>> entry = map.entrySet();
				Iterator <Entry<String, String>> itr = entry.iterator();
				while (itr.hasNext()) {
					Entry<String, String> maps = itr.next();
					String key = maps.getKey();
					if ("1".equals(key)) {
						imgText1 = maps.getValue();
						if (imgText1 != null && imgText1.length() != 1) {
							error = true;
						} else if (imgText1 != null && imgText1.length() == 1){
							try {
								Integer.parseInt(imgText1);
							} catch (NumberFormatException nfe) {
								error = true;
							}
						}
					} else if ("2".equals(key)) {
						imgText2 = maps.getValue();
						if (imgText2 != null && imgText2.length() != 1) {
							error = true;
						} else if (imgText2 != null && imgText2.length() == 1){
							try {
								Integer.parseInt(imgText2);
							} catch (NumberFormatException nfe) {
								error = true;
							}
						}
					} else if ("3".equals(key)) {
						imgText3 = maps.getValue();
						if (imgText3 != null && imgText3.length() != 1) {
							error = true;
						} else if (imgText3 != null && imgText3.length() == 1){
							try {
								Integer.parseInt(imgText3);
							} catch (NumberFormatException nfe) {
								error = true;
							}
						}
					} else if ("4".equals(key)) {
						imgText4 = maps.getValue();
						if (imgText4 != null && imgText4.length() != 1) {
							error = true;
						} else if (imgText4 != null && imgText4.length() == 1){
							try {
								Integer.parseInt(imgText4);
							} catch (NumberFormatException nfe) {
								error = true;
							}
						}
					} else if ("5".equals(key)) {
						imgText5 = maps.getValue();
						if (imgText5 != null && imgText5.length() != 1) {
							error = true;
						} else if (imgText5 != null && imgText5.length() == 1){
							try {
								Integer.parseInt(imgText5);
							} catch (NumberFormatException nfe) {
								error = true;
							}
						}
					}
				}
			}
			
			executor.shutdown();

			// Wait until all threads are finished
			while (!executor.isTerminated()) {
			}

			if (error) {
				if (anotherAttempt) {
					processCaptcha(xhtmlData, false);
				}
			} else {
				final List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
				postValuePairs.add(new BasicNameValuePair("captcha_text", imgText1+imgText2+imgText3+imgText4+imgText5));

				// Process the captcha image
				httpClientWrapper.postSitePage(populateUrl("process_captcha.php"), null, postValuePairs, setupHeader(true));
			}
		} catch (Exception e) {
			LOGGER.error(e);
		}
	}
	
	/**
	 * 
	 * @param xhtml
	 * @param event
	 * @param ae
	 * @return
	 * @throws BatchException
	 */
	private String processLineChange(String xhtml, BaseRecordEvent event, AccountEvent ae) throws BatchException {
		LOGGER.info("Entering processLineChange()");

		final Map<String, String> lineChanges = BBP.processLineChange(xhtml);
		if (lineChanges != null && !lineChanges.isEmpty()) {
			if (event instanceof SpreadRecordEvent) {
				final SpreadRecordEvent sre = (SpreadRecordEvent)event;
				if (determineSpreadLineChange(sre, ae, lineChanges)) {
					// setup the data
					List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
					MAP_DATA.put("password", this.httpClientWrapper.getPassword());
					String actionLogin = setupNameValuesEmpty(postValuePairs, lineChanges, httpClientWrapper.getWebappname());
					LOGGER.debug("ActionLogin: " + actionLogin);

					// Post to site page
					xhtml = postSite(actionLogin, postValuePairs);							
				} else {
					throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed", xhtml);
				}
			} else if (event instanceof TotalRecordEvent) {
				final TotalRecordEvent tre = (TotalRecordEvent)event;
				if (determineTotalLineChange(tre, ae, lineChanges)) {
					// setup the data
					List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
					MAP_DATA.put("password", this.httpClientWrapper.getPassword());
					String actionLogin = setupNameValuesEmpty(postValuePairs, lineChanges, httpClientWrapper.getWebappname());
					LOGGER.debug("ActionLogin: " + actionLogin);

					// Post to site page
					xhtml = postSite(actionLogin, postValuePairs);
				} else {
					throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed", xhtml);
				}
			} else if (event instanceof MlRecordEvent) {
				final MlRecordEvent mre = (MlRecordEvent)event;
				
				if (determineMlLineChange(mre, ae, lineChanges)) {
					// setup the data
					List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
					MAP_DATA.put("password", this.httpClientWrapper.getPassword());
					String actionLogin = setupNameValuesEmpty(postValuePairs, lineChanges, httpClientWrapper.getWebappname());
					LOGGER.debug("ActionLogin: " + actionLogin);

					// Post to site page
					xhtml = postSite(actionLogin, postValuePairs);
				} else {
					throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed", xhtml);
				}
			}
		}

		LOGGER.info("Exiting processLineChange()");
		return xhtml;
	}
}
