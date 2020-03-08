/**
 * 
 */
package com.ticketadvantage.services.dao.sites.tdsportseight;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.ticketadvantage.services.errorhandling.AppErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;

/**
 * @author jmiller
 *
 */
public class TDSportsEightParser extends TDSportsParser {
	private static final Logger LOGGER = Logger.getLogger(TDSportsEightParser.class);
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd hh:mm a z");
	private boolean isMlb;

	/**
	 * Constructor
	 */
	public TDSportsEightParser() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final TDSportsEightParser bet504TDSportsParser = new TDSportsEightParser();
			String xhtml = "";
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
			if (sportName.contains("MLB") || sportName.contains("mlb") || sportName.contains("BASEBALL") || sportName.contains("baseball")) {
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
		map = findMenu(doc.select("#FirstChild1 td table tbody tr"), map, type, sport, "td div", "td input");
		map = findMenu(doc.select("#FirstChild2 td table tbody tr"), map, type, sport, "td div", "td input");

		// Remove the input fields that shouldn't be sent
		map = removeInputFields(map);

		// Continue button
		// map.put("ctl00$WagerContent$btn_Continue1", "Continue");

		LOGGER.info("Exiting parseMenu()");
		return map;
	}

	protected Map<String, String> findMenu(Elements divs, Map<String, String> map, String[] type, String[] sport, String foundString, String menuString) {
		LOGGER.info("Entering findMenu()");

		for (int x = 0; (divs != null && x < divs.size()); x++) {
			final Element div = divs.get(x);
			LOGGER.debug("div: " + div);
			boolean foundDiv = false;
			for (int y = 0; y < type.length; y++) {
				for (int z = 0; z < sport.length; z++) {
					foundDiv = foundSport(div, foundString, type[y], sport[z]);
					LOGGER.debug("foundDiv: " + foundDiv);
	
					// Found the event
					if (foundDiv) {
						map = getMenuData(div, menuString, map);
						LOGGER.debug("Map: " + map);
					}
				}
			}
		}

		LOGGER.info("Exiting findMenu()");
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
		Elements elements = doc.select("#maincol center center table tbody tr");
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

		if (xhtml != null) {
			int index = xhtml.indexOf("USD</TD><TD>");
			
			if (index != -1) {
				xhtml = xhtml.substring(index + "USD</TD><TD>".length());
				index = xhtml.indexOf("</TD>");

				if (index != -1) {
					ticketNumber = xhtml.substring(0, index);
				} else {
					ticketNumber = "Failed to get ticket number";
					throw new BatchException(BatchErrorCodes.FAILED_TO_SETUP_TRANSACTION_CORRECTLY,
							BatchErrorMessage.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, ticketNumber, xhtml);
				}
			} else {
				ticketNumber = "Failed to get ticket number";
				throw new BatchException(BatchErrorCodes.FAILED_TO_SETUP_TRANSACTION_CORRECTLY,
						BatchErrorMessage.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, ticketNumber, xhtml);
			}
		} else {
			ticketNumber = "Failed to get ticket number";
			throw new BatchException(BatchErrorCodes.FAILED_TO_SETUP_TRANSACTION_CORRECTLY,
					BatchErrorMessage.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, ticketNumber, xhtml);
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
			int t = 0;
			for (int x = 0; x < elements.size(); x++) {
				// Loop through the elements and then check for the dates
				final Element element = elements.get(x);
				LOGGER.debug("Element: " + element);

				if (element != null) {
					String classInfo = element.attr("class");
					LOGGER.debug("ClassInfo: " + classInfo);

					if ((classInfo != null && classInfo.length() > 0) && 
						(classInfo.contains("TrGameOdd") || classInfo.contains("TrGameEven"))) {
						if (t == 0) {
							// Get the date
							eventPackage = new TDSportsEventPackage();
							team1 = new TDSportsTeamPackage();
							final Elements tds = element.select("td");
							
							if (tds != null && tds.size() > 0) {
								this.getDate(tds.get(1), team1);
							}
							getTeamData(element.select("td"), team1);
							eventPackage.setId(team1.getId());
							t++;
						} else {
							team2 = new TDSportsTeamPackage();
							final Elements tds = element.select("td");

							if (tds != null && tds.size() > 0) {
								this.getDate(tds.get(1), team2);
							}

							int size = getTeamData(tds, team2);
							Date eventDate = null;
							final Calendar now = Calendar.getInstance();
							int offset = now.get(Calendar.DST_OFFSET);
							String cDate = null;

							try {
								// Mar 05 7:05 PM
								cDate = team1.gettDate() + " " + team2.gettDate() + " " + timeZoneLookup(timezone, offset);
								LOGGER.debug("cDate: " + cDate);

								eventDate = DATE_FORMAT.parse(cDate);
								final Calendar newDate = Calendar.getInstance();
								offset = newDate.get(Calendar.DST_OFFSET);
								newDate.setTime(eventDate);
								newDate.set(Calendar.YEAR, now.get(Calendar.YEAR));
								eventDate = newDate.getTime();
							} catch (ParseException pe) {
								LOGGER.error("ParseExeption for " + cDate, pe);
								// Throw an exception
								throw new BatchException(
										AppErrorMessage.SITE_PARSER_EXCEPTION + " Date parsing exception for " + cDate);
							}
							team1.setEventdatetime(eventDate);
							team2.setEventdatetime(eventDate);
							final String dateOfEvent = eventDate.toString();
							LOGGER.debug("dateOfEvent: " + dateOfEvent);
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
		LOGGER.debug("EventPackage: " + eventPackage);

		// Reset
		isMlb = false;

		LOGGER.info("Exiting getGameData()");
		return events;
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

		if (elements != null && elements.size() == 7) {
			LOGGER.info("elements.size(): " + elements.size());
			size = 7;
			for (int x = 0;(elements != null && x < elements.size()); x++) {
				final Element td = elements.get(x);
	
				switch (x) {
					case 0: // Do nothing
						break;
					case 1:
						team = getEventId(td, team);
						break;
					case 2:
						team = getTeam(td, team);
						break;
					case 3:
						team = getPitcher(td, team);
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
	 * @param team
	 * @return
	 */
	protected TDSportsTeamPackage getDate(Element td, TDSportsTeamPackage team) {
		LOGGER.info("Entering getDate()");
		LOGGER.debug("Element: " + td);

		// Date String
		final String date = parseIconOut(td);
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
		LOGGER.debug("Element: " + td);

		final String eventId = td.html();
		if (eventId != null) {
			team.setEventid(eventId);
			team.setId(Integer.parseInt(eventId));
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
		LOGGER.debug("Element: " + td);

		// Team String
		String teamInfo = td.html();
		int index = teamInfo.indexOf(">");
		if (index != -1) {
			final String teamName = teamInfo.substring(index + 1).trim();
			team.setTeam(teamName);
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
	protected TDSportsTeamPackage getPitcher(Element td, TDSportsTeamPackage team) {
		LOGGER.info("Entering getPitcher()");
		LOGGER.debug("Element: " + td);

		// Pitcher
		String pitcher = td.html();
		if (pitcher != null && pitcher.length() > 0) {
			pitcher = pitcher.trim();
			team.setPitcher(pitcher);
		}

		LOGGER.info("Exiting getPitcher()");
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
		LOGGER.debug("Element: " + td);

		// First get the input field information
		final Map<String, String> hashMap = parseInputField(td.select("input"), 0);
		team.setGameSpreadInputId(hashMap.get("id"));
		team.setGameSpreadInputName(hashMap.get("name"));
		team.setGameSpreadInputValue(hashMap.get("value"));

		final String spread = parseFontOut(td);
		LOGGER.error("spread: " + spread);

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

		final String total = parseFontOut(td);

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
		final String ml = parseFontOut(td);

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
	private String parseFontOut(Element td) {
		LOGGER.info("Entering parseSpanOut()");
		LOGGER.debug("Element: " + td);
		String retValue = null;		
		String html = td.html();

		final Elements fonts = td.select("font");

		if (fonts != null && fonts.size() > 0) {
			retValue = fonts.get(0).html().trim();
		} else if (html != null && html.length() > 0) {
			int index = html.indexOf("<input");

			if (index != -1) {
				retValue = html.substring(0, index);
			} else {
				retValue = html.trim();				
			}
			LOGGER.debug("retValue: " + retValue);
		} else {
			retValue = html.trim();
		}

		LOGGER.info("Entering parseSpanOut()");
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