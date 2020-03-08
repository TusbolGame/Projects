/**
 * 
 */
package com.ticketadvantage.services.dao.sites.agsoftware;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ticketadvantage.services.dao.sites.SiteEventPackage;
import com.ticketadvantage.services.dao.sites.SiteParser;
import com.ticketadvantage.services.dao.sites.SiteTeamPackage;
import com.ticketadvantage.services.errorhandling.AppErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.PendingEvent;

/**
 * @author jmiller
 *
 */
public class AGSoftwareParser extends SiteParser {
	private static final Logger LOGGER = Logger.getLogger(AGSoftwareParser.class);
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("E MM/dd yyyy hh:mma Z");

	/**
	 * Constructor
	 */
	public AGSoftwareParser() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			String xhtml = "<?xml version=\"1.0\" charset=\"UTF-8\" ?><!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\">	<HEAD>		<meta http-equiv=\"Content-Type\" content=\"Type=text/html; charset=iso-8859-1\">		<script type=\"text/javascript\" language=\"JavaScript\">			<!--						//-->		</script>				<meta name=\"Template\" content=\"t:www.vegasbettingworld.com;10.0.69.20;/SbVerifyFixedWager.asp\"/>		<script src=\"/javascript/site_js.js\" type=\"text/javascript\"></script>		<script src=\"/Design/vegasbettingworld_com/site_settings.js?v=10.0045\" type=\"text/javascript\"></script>				<script src=\"/javascript/prototype.js\" type=\"text/javascript\"></script>		<script src=\"/javascript/effects.js\" type=\"text/javascript\"></script>		<script src=\"/javascript/window.js\" type=\"text/javascript\"></script>		<script src=\"/javascript/window_effects.js\" type=\"text/javascript\"></script>		<script src=\"/javascript/scriptaculous.js\" type=\"text/javascript\"></script>				<script src='/javascript/class.horinaja.scriptaculous.js' type='text/javascript'></script>			<link href='/Design/vegasbettingworld_com/Default.css?v=10.0045' rel='stylesheet' type='text/css'></link>		<link href='/Design/Reporting.css?v=10.0045' rel='stylesheet' type='text/css'></link>		<link href='/Design/vegasbettingworld_com/Reporting.css?v=10.0045' rel='stylesheet' type='text/css'></link>	<script type='text/javascript'>var canDelete = false;</script><script src=\"/javascript/menu_en.js?v=10.0045\" type=\"text/javascript\"></script>		<link rel=\"stylesheet\" href=\"/Design/season.css?v=10.0045\" type=\"text/css\"  media=\"screen\"/>		<link rel=\"stylesheet\" href=\"/Design/horinaja.css\" type=\"text/css\" media=\"screen\" />			<script type=\"text/javascript\" async=\"async\" src=\"/__zenedge/assets/hic.js?v=1529343809\"></script><script type=\"text/javascript\">(function(){ if (typeof(___zen) === \"undefined\") {setTimeout(arguments.callee, 50); return; }___zen.hic(\"__ZEHIC1688\", 1532355632, 1, 0, 60); })()</script></head>	<body>		<div id=\"wrapper\">			<div id=\"navigation1\">			<ul id=\"MenuUserList\">							<li id=\"link_home\"><a href=\"/WagerMenu.asp\">Home</a></li>			<li>&nbsp;</li>			<li><a href=\"LogoutSucessful.asp\">Logout</a></li>				<li><a href=\"https://www.wager-info.com\" target=\"_blank\" class=\"TopMenu\">Sports Rules</a></li>				<li>&nbsp;</li>								<li>&nbsp;</li>			</ul>		</div>	<div id=\"header\">		<div class=\"LogoCustomerbk\">			<div class=\"logoDiv\" style=\"width:40%;height:95px; position: absolute; z-index:-1;\" onClick=\"document.location.href='/'\" onMouseOver=\"this.style.cursor = 'pointer'\" ></div>						<div class=\"custinfo\">Logged in as: yl661<br/>Current Balance: 0.00<br/>Available: 8,260.00<br/>Total Pending: 1,740.00&nbsp;USD<br/>Non-Posted Casino: 0.00<br/> 		</div>	</div>	</div> <!--end header-->	<div id=\"navigation2\">	<ul id=\"MenuWager\">				  <li><a href=\"/WagerMenu.asp\" onMouseover=\"dropdownmenu(this, event, menu1, '155px')\" onMouseout=\"delayhidemenu()\" class=\"LktTitle\">Wagering Menu </a></li>				  <li><a href=\"/WagerMenu.asp?accountmenu=1\" onMouseover=\"dropdownmenu(this, event, menu2, '140px')\" onMouseout=\"delayhidemenu()\" class=\"LktTitle\">My Account</a></li>							  <li><a href=\"/Wagermenu.asp?casino=2\">Casino</a></li>			  <li id=\"link_webReport\"> <a href=\"_reportWebProblem.asp\">Report Web Problem</a></li></ul></div>		<div></div>		<div id=\"content\"><div style='clear:both'></div><TABLE id=\"TicketTable\" style=\"text-align:center;margin:auto\"><TR><TD align=\"center\"><FONT><BR/><span style='color: #000131'>Current Wager type:</span> Single Straight Bet / Buy Points<BR/><BR>Sport: MLB Baseball  <BR>  Selection  : New York Yankees/Tampa Bay Rays  7/23/2018 7:10 PM - (EST)  <BR/>               Total Points UNDER 7&#189; -120 for Game   <BR/>               LUIS SEVERINO-R must Start&nbsp;&nbsp;&nbsp;HUNTER WOOD-R must Start  <BR/>Amount     : Risking 600.00   To Win 500.00  USD  </FONT></TR></TD></TABLE>		<FORM name=\"wagerVerificationForm\" id=\"wagerVerificationForm\" method=\"post\" action=\"CheckAcceptancePassword.asp\">			<INPUT type=\"hidden\" id=inetWagerNumber name=inetWagerNumber value=\"0.6135203171913873\"><BR/>			<P class=\"errorMessage\">			ATTENTION!!!  THE LINE (OR PRICE) HAS CHANGED......Please review the new line carefully and if you want this changed bet, 			 			then re-enter password To Confirm Wager! 						 Otherwise press \"Cancel\".			</p> 			<BR/>			<BR/>			<EM><STRONG>Please do not hit the \"back\" button because the bet in progress was cancelled due to the line change.<BR/><BR/><STRONG></EM>			<BR/>			<BR/><INPUT type=\"button\" value=\"Cancel\" id=\"cancel\" name=\"cancel\" onClick=\"javascript:document.CancelForm.submit()\">&nbsp;<INPUT type=\"password\" id=password name=password class='narrowInputBox'><a href=\"javascript:void(0)\" onClick=\"javascript:document.forms['wagerVerificationForm'].submit()\" class='narrowButtonsAnchor' alt=\"Continue\" id=\"continueBtn\"><div class='styleButton narrowButtonsDiv'>Continue</div></a><br/>		</FORM>		<BR/>		<FORM name=\"CancelForm\" id=\"CancelForm\" method=\"post\" action=\"WagerCancelled.asp\">		</FORM></div><div id=footer></div></div><script type=\"text/javascript\" async=\"async\" src=\"/__zenedge/assets/f.js?v=1529343809\"></script><script>(function () { var v = 1532355632 * 3.1415926535898; v = Math.floor(v); document.cookie = \"__zjc9749=\"+v+\"; expires=Mon, 23 Jul 2018 14:21:45 UTC; path=/\"; })()</script></body></HTML>";
			AGSoftwareParser asp = new AGSoftwareParser();
			Map<String, String> map = asp.processLineChange(xhtml);
			LOGGER.debug("Map: " + map);
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseIndex(java.lang.String)
	 */
	@Override
	public Map<String, String> parseIndex(String xhtml) throws BatchException {
		LOGGER.info("Entering parseIndex()");
		LOGGER.debug("xhtml: " + xhtml);
		final Map<String, String> retValue = new HashMap<String, String>();

		// Get the document object
		final Document doc = parseXhtml(xhtml);
		
		// Get all input fields
		getAllElementsByName(doc, "input", "value", retValue);
		
		// Get form action field
		retValue.put("action", getElementByName(doc, "form", "action"));

		LOGGER.info("Exiting parseIndex()");
		return retValue;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseLogin(java.lang.String)
	 */
	@Override
	public Map<String, String> parseLogin(String xhtml) throws BatchException {
		LOGGER.info("Entering parseLogin()");
		LOGGER.debug("xhtml: " + xhtml);
		final Map<String, String> map = new HashMap<String, String>();

		// Get the document object
		final Document doc = parseXhtml(xhtml);

		//<script src="/Design/betting4entertainment_com/site_settings.js?v=10.0036" type="text/javascript"></script>
		Elements scripts = doc.select("script");
		for (int x = 0; (scripts != null && x < scripts.size()); x++) {
			Element script = scripts.get(x);
			if (script != null) {
				String src = script.attr("src");
				if (src != null && src.length() > 0) {
					if (src.contains("site_settings.js")) {
						map.put("src", src);
					}
				}
			}
		}

		// Now check for the straightSelectionFrm form
		final String[] types = new String[] { "hidden" };
		Element form = doc.getElementById("straightSelectionFrm");
		if (form != null) {
			// Get form action field
			map.put("action", form.attr("action"));

			getAllElementsByType(form, "input", types, map);
		}

		
		// Now check for the straightSelectionFrm form
		form = doc.getElementById("frmContinue");
		if (form != null) {
			// Get form action field
			map.put("action", form.attr("action"));

			getAllElementsByType(form, "input", types, map);
		}
		
		LOGGER.info("Exiting parseLogin()");
		return map;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseMenu(java.lang.String, java.lang.String[], java.lang.String[])
	 */
	@Override
	public Map<String, String> parseMenu(String xhtml, String[] type, String[] sport) throws BatchException {
		LOGGER.info("Entering parseMenu()");
		LOGGER.info("xhtml: " + xhtml);
		LOGGER.info("type: " + java.util.Arrays.toString(type));
		LOGGER.info("sport: " + java.util.Arrays.toString(sport));
		Map<String, String> map = new HashMap<String, String>();

		// Parse to get the Document
		final Document doc = parseXhtml(xhtml);
		LOGGER.debug("Document: " + doc);

		// Get forms hidden input fields and action URL
		final Element sportSelectionForm = doc.getElementById("sportSelectionFrm");
		if (sportSelectionForm != null) {
			final String[] types = new String[] { "hidden" };
			getAllElementsByType(sportSelectionForm, "input", types, map);

			// Get form action field
			map.put("action", sportSelectionForm.attr("action"));
		}

		// Find the different menu types
		map = findMenu(doc.select(".sportSelectionSubSport"), map, type, sport, "a", "div div");

		LOGGER.info("Exiting parseMenu()");
		return map;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseGames(java.lang.String, java.lang.String, java.util.Map)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <SiteEventPackage> List<SiteEventPackage> parseGames(String xhtml, String type, Map<String, String> inputFields) throws BatchException {
		LOGGER.info("Entering parseGames()");
		LOGGER.debug("xhtml: " + xhtml);
		LOGGER.debug("type: " + type);
		LOGGER.debug("InputFields: " + inputFields);
		
		List<?> events = null;
		// Parse to get the Document
		final Document doc = parseXhtml(xhtml);
		LOGGER.debug("Document: " + doc);

		// Get forms hidden input fields and action URL
		final Element gameSelectionForm = doc.getElementById("gameSelectionForm");
		if (gameSelectionForm != null) {
			final String[] types = new String[] { "hidden" };
			getAllElementsByType(gameSelectionForm, "input", types, inputFields);

			// Get form action field
			inputFields.put("action", gameSelectionForm.attr("action"));
		}

		// Now get the games
		final Elements elements = doc.select(".LineOfferingTable tbody tr");
		if (elements != null && elements.size() > 0) {
			events = getGameData(elements);
		}

		LOGGER.info("Exiting parseGames()");
		return (List<SiteEventPackage>)events;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseEventSelection(java.lang.String, com.ticketadvantage.services.dao.sites.SiteTeamPackage, java.lang.String)
	 */
	@Override
	public Map<String, String> parseEventSelection(String xhtml, SiteTeamPackage siteTeamPackage, String type) throws BatchException {
		LOGGER.info("Entering parseEventSelection()");
		LOGGER.debug("xhtml: " + xhtml);
		final Map<String, String> map = new HashMap<String, String>();

		// Parse to get the Document
		final Document doc = parseXhtml(xhtml);

		if (xhtml.contains("THE LINE (OR PRICE) HAS CHANGED")) {
			throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed", xhtml);
		}
		
		if (xhtml.contains("is greater than your amount available")) {
			throw new BatchException(BatchErrorCodes.MAX_WAGER_AMOUNT_REACHED, BatchErrorMessage.MAX_WAGER_AMOUNT_REACHED, "The risk amount is greater than your amount available", xhtml);
		}

		if (xhtml.contains("captcha below")) {
			final Elements imgs = doc.select("form img");

			if (imgs != null && imgs.size() > 0) {
				String captchaImage = imgs.get(0).attr("src").replace("data:image/png;base64,", "");
				LOGGER.error("captchaImage: " + captchaImage);
				map.put("CaptchaImage", captchaImage);
				map.put("code", "");
				map.put("action", "&#x2F;__zenedge&#x2F;c?src=%2FBbVerifyWager.asp");
				return map;
			} else {
				throw new BatchException(BatchErrorCodes.CAPTCHA_PROCESSING_EXCEPTION, BatchErrorMessage.CAPTCHA_PROCESSING_EXCEPTION, "Captcha on account", xhtml);
			}
		}

		// Get all of the input fields
		final Element wagerVerificationForm = doc.getElementById("wagerVerificationForm");
		if (wagerVerificationForm != null) {
			final String[] types = new String[] { "hidden", "text" };
			getAllElementsByType(wagerVerificationForm, "input", types, map);

			// Get form action field
			map.put("action", wagerVerificationForm.attr("action"));
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
		String ticketNumber = null;

		// Parse to get the Document
		final Document doc = parseXhtml(xhtml);
		
		if (xhtml.contains("THE LINE (OR PRICE) HAS CHANGED")) {
			throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed", xhtml);
		}
		
		if (xhtml.contains("is greater than your amount available")) {
			throw new BatchException(BatchErrorCodes.MAX_WAGER_AMOUNT_REACHED, BatchErrorMessage.MAX_WAGER_AMOUNT_REACHED, "The risk amount is greater than your amount available", xhtml);
		}

		final Element content = doc.getElementById("content");
		if (content != null) {
			final Elements elements = content.select("font");
			for (int x = 0; x < elements.size(); x++) {
				final Element font = elements.get(x);
				if (font != null) {
					String html = font.html();
					if (html != null && html.contains("Ticket Number - ")) {
						ticketNumber = html;
					}
				}
			}
		}

		// Check for a valid ticket number
		if (ticketNumber == null) {
			throw new BatchException(BatchErrorCodes.FAILED_TO_GET_TICKET_NUMBER, BatchErrorMessage.FAILED_TO_GET_TICKET_NUMBER, "Failed to get ticket number", xhtml);
		}

		LOGGER.info("Exiting parseTicketNumber()");
		return ticketNumber;
	}

	/**
	 * 
	 * @param xhtml
	 * @return
	 * @throws BatchException
	 */
	public Map<String, String> parseCaptcha(String xhtml) throws BatchException {
		final Map<String, String> map = new HashMap<String, String>();

		if (xhtml.contains("captcha below")) {
			// Parse to get the Document
			final Document doc = parseXhtml(xhtml);
			final Elements imgs = doc.select("form img");

			if (imgs != null && imgs.size() > 0) {
				String captchaImage = imgs.get(0).attr("src").replace("data:image/png;base64,", "");
				LOGGER.error("captchaImage: " + captchaImage);
				map.put("CaptchaImage", captchaImage);
				map.put("code", "");
				map.put("action", "&#x2F;__zenedge&#x2F;c?src=%2FBbVerifyWager.asp");
				return map;
			} else {
				throw new BatchException(BatchErrorCodes.CAPTCHA_PROCESSING_EXCEPTION, BatchErrorMessage.CAPTCHA_PROCESSING_EXCEPTION, "Captcha on account", xhtml);
			}
		}

		return map;
	}

	/**
	 * 
	 * @param xhtml
	 * @return
	 * @throws BatchException
	 */
	public Map<String, String> parseWagerType(String xhtml) throws BatchException {
		LOGGER.info("Entering parseWagerType()");
		LOGGER.debug("xhtml: " + xhtml);
		final Map<String, String> map = new HashMap<String, String>();

		// Parse to get the Document
		final Document doc = parseXhtml(xhtml);

		if (xhtml.contains("THE LINE (OR PRICE) HAS CHANGED")) {
			throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed", xhtml);
		}

		if (xhtml.contains("is greater than your amount available")) {
			throw new BatchException(BatchErrorCodes.MAX_WAGER_AMOUNT_REACHED, BatchErrorMessage.MAX_WAGER_AMOUNT_REACHED, "The risk amount is greater than your amount available", xhtml);
		}

		// Get all of the input fields
		final Elements elements = doc.select("form");
		for (int x = 0; (elements != null && x < elements.size()); x++) {
			Element wagerVerificationForm = elements.get(x);
			if (wagerVerificationForm != null && "WagerVerificationForm".equals(wagerVerificationForm.attr("name"))) {
				final String[] types = new String[] { "hidden", "password" };
				getAllElementsByType(wagerVerificationForm, "input", types, map);
	
				// Get form action field
				map.put("action", wagerVerificationForm.attr("action"));
			}
		}

		// Check for a wager limit and change it 
		if (xhtml.contains("A maximum wager amount of ")) {
			int index = xhtml.indexOf("A maximum wager amount of ");
			if (index != -1) {
				xhtml = xhtml.substring(index + "A maximum wager amount of ".length());
				index = xhtml.indexOf(" USD");
				if (index != -1) {
					String wagerAmount = xhtml.substring(0, index);
					if (wagerAmount != null) {
						wagerAmount = wagerAmount.replaceAll(",", "");
						wagerAmount = wagerAmount.replaceAll(" ", "");
						map.put("wageramount", wagerAmount);
					}
				}
			}
		} else if (xhtml.contains("A minimum wager amount of")) {
			int index = xhtml.indexOf("A minimum wager amount of");
			if (index != -1) {
				xhtml = xhtml.substring(index + "A minimum wager amount of".length());
				index = xhtml.indexOf("USD");
				if (index != -1) {
					String wagerAmount = xhtml.substring(0, index);
					if (wagerAmount != null) {
						wagerAmount = wagerAmount.replaceAll(",", "");
						wagerAmount = wagerAmount.replaceAll(" ", "");
						map.put("wagerminamount", wagerAmount);
					}
				}
			}
		} else {
			// First get the Risk
			String wagerInfo = getHtmlFromAllElements(doc, "#TicketTable tbody tr td font");
			if (wagerInfo != null && wagerInfo.length() > 0) {
				// Risking 55.00   To Win 50.00  USD
	
				// Risk
				int beginIndex = wagerInfo.indexOf("Risking");
				int endIndex = wagerInfo.indexOf("To Win");
				if (beginIndex != -1 && endIndex != -1) {
					String risk = wagerInfo.substring(beginIndex + "Risking".length(), endIndex);
					risk = risk.replaceAll("&nbsp;", "");
					risk = risk.trim();
					map.put("risk", risk);
				} else {
					// Throw Exception
					throw new BatchException(BatchErrorCodes.ERROR_PARSING_WAGER_INFO, BatchErrorMessage.ERROR_PARSING_WAGER_INFO, "Cannot find Risk", xhtml);				
				}
				
				// Win
				beginIndex = wagerInfo.indexOf("To Win");
				endIndex = wagerInfo.indexOf("USD");
				if (beginIndex != -1 && endIndex != -1) {
					String win = wagerInfo.substring(beginIndex + "To Win".length(), endIndex);
					win = win.replaceAll("&nbsp;", "");
					win = win.trim();
					map.put("win", win);
				} else {
					// Throw Exception
					throw new BatchException(BatchErrorCodes.ERROR_PARSING_WAGER_INFO, BatchErrorMessage.ERROR_PARSING_WAGER_INFO, "Cannot find Win", xhtml);				
				}
			} else {
				// Throw Exception
				throw new BatchException(BatchErrorCodes.ERROR_PARSING_WAGER_INFO, BatchErrorMessage.ERROR_PARSING_WAGER_INFO, "No element found", xhtml);
			}
		}

		LOGGER.info("Exiting parseWagerType()");
		return map;
	}

	/**
	 * 
	 * @param xhtml
	 * @param cookies
	 * @return
	 */
	public String parseCookies(String xhtml, String cookies) {
		LOGGER.info("Entering parseCookies()");
		LOGGER.debug("Cookies: " + cookies);

		//createCookie("btn_admin","/AgLogin.asp");
		int bIndex = xhtml.indexOf("createCookie(");
		int eIndex = xhtml.indexOf(");");

		while (bIndex != -1 && eIndex != -1) {
			String tempString = xhtml.substring(bIndex + "createCookie(".length(), eIndex);
			tempString = tempString.replaceAll("\"", "");
			tempString = tempString.replaceAll(",", "=") + ";";
			cookies += tempString;
			xhtml = xhtml.substring(eIndex + ");".length());
			if (xhtml != null && xhtml.length() > 0) {
				bIndex = xhtml.indexOf("createCookie(");
				eIndex = xhtml.indexOf(");");
			} else {
				bIndex = -1;
				eIndex = -1;
			}
		}

		LOGGER.info("Exiting parseCookies()");
		return cookies;
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
		final Elements trs = doc.select("#content div table tr");
		LOGGER.debug("trs: " + trs);
		if (trs != null) {
			for (Element tr : trs) {
				int x = 0;
				String className = tr.attr("class");
				if (className != null && className.contains("colRep")) {
					Elements tds = tr.select("td");
					final PendingEvent pe = new PendingEvent();
					for (Element td : tds) {
						LOGGER.debug("td: " + td);
						pe.setAccountname(accountName);
						pe.setAccountid(accountId);
						pe.setCustomerid(accountId);
						pe.setInet(accountName);

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
								getRisk(td, pe);
								break;
							case 4:
								getWin(td, pe);
								break;
							case 5:
								getEventInfo(td, pe);
								break;
							default:
								break;
						}
					}
					pe.setDoposturl(false);
					LOGGER.debug("PendingEvent: " + pe);
					pendingEvents.add(pe);
				}
			}	
		}
		LOGGER.debug("PendingEvents: " + pendingEvents);

		LOGGER.info("Exiting parsePendingBets()");
		return pendingEvents;
	}

	/**
	 * 
	 * @param xhtml
	 * @return
	 */
	public Map<String, String> processLineChange(String xhtml) throws BatchException {
		LOGGER.info("Entering processLineChange()");
		final Map<String, String> map = new HashMap<String, String>();
		String tempXhtml = xhtml;

		try {
			LOGGER.debug("xhtml: " + xhtml);
			int index = xhtml.indexOf("Selection  :");
			if (index != -1) {
				xhtml = xhtml.substring(index + "Selection  :".length());
				LOGGER.debug("new xhtml: " + xhtml);

		        // Spread -2.5 -124 for Game<br />
		        // Total Points UNDER 197 -116 for Game<br />
				// Money Line -119 for Game<br />
		
				index = xhtml.indexOf("Spread");
				if (index != -1) {
					xhtml = xhtml.substring(index + "Spread".length());
					index = xhtml.indexOf("for");
					if (index != -1) {
						xhtml = xhtml.substring(0, index).trim();
						index = xhtml.indexOf(" ");
						if (index != -1) {
							final Map<String, String> spread = parseSpread(xhtml.substring(0, index), -1);
							final Map<String, String> spreadJuice = parseJuice(xhtml.substring(index + 1), null, null);
							map.put("valueindicator", spread.get("valindicator"));
							map.put("value", super.reformatValues(spread.get("val")));
							map.put("juiceindicator", spreadJuice.get("juiceindicator"));
							map.put("juice", super.reformatValues(spreadJuice.get("juice")));
						}
					}
				} else {
					index = xhtml.indexOf("Total Points");
					if (index != -1) {
						xhtml = xhtml.substring(index + "Total Points".length());
						index = xhtml.indexOf("for");
						if (index != -1) {
							xhtml = xhtml.substring(0, index).trim();
							xhtml = xhtml.replace("OVER ", "o");
							xhtml = xhtml.replace("UNDER ", "u");
							index = xhtml.indexOf(" ");
							if (index != -1) {
								final Map<String, String> total = parseTotal(xhtml.substring(0, index), -1);
								final Map<String, String> totalJuice = parseJuice(xhtml.substring(index + 1), null, null);
								LOGGER.debug("totalJuice: " + totalJuice);
								map.put("valueindicator", total.get("valindicator"));
								map.put("value", super.reformatValues(total.get("val")));
								map.put("juiceindicator", totalJuice.get("juiceindicator"));
								map.put("juice", super.reformatValues(totalJuice.get("juice")));
							}
						}
					} else {
						index = xhtml.indexOf("Money Line");
						if (index != -1) {
							xhtml = xhtml.substring(index + "Money Line".length());
							index = xhtml.indexOf("for");
							if (index != -1) {
								xhtml = xhtml.substring(0, index).trim();
								LOGGER.debug("ChangeLine XHTML: " + xhtml);
								final Map<String, String> mlJuice = parseJuice(xhtml, null, null);
								map.put("juiceindicator", mlJuice.get("juiceindicator"));
								map.put("juice", super.reformatValues(mlJuice.get("juice")));
							}
						}
					}
				}
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
			LOGGER.error("xhtml: " + xhtml);
			throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed", xhtml);
		}

		// Parse xhtml and get action
		final Element form = parseXhtml(tempXhtml).getElementById("wagerVerificationForm");
		if (form != null) {
			final String action = form.attr("action");
			map.put("action", action);
			final Element inetWagerNumber = form.getElementById("inetWagerNumber");
			map.put("inetWagerNumber", inetWagerNumber.attr("value"));
		}

		LOGGER.info("Entering processLineChange()");
		return map;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#getGameData(org.jsoup.select.Elements)
	 */
	@Override
	protected List<SiteEventPackage> getGameData(Elements elements) throws BatchException {
		LOGGER.info("Entering getGameData()");
		LOGGER.debug("Elements: " + elements);

		SiteEventPackage eventPackage = null;
		final List<SiteEventPackage> events = new ArrayList<SiteEventPackage>();
		if (elements != null) {
			AGSoftwareTeamPackage team1 = null;
			AGSoftwareTeamPackage team2 = null;
			int t = 0;
			for (int x = 0; x < elements.size(); x++) {
				// Loop through the elements and then check for the dates
				final Element element = elements.get(x);
				LOGGER.debug("Element: " + element);

				if (element != null) {
					String classInfo = element.attr("class");
					LOGGER.debug("ClassInfo: " + classInfo);

					if ((classInfo != null && classInfo.length() > 0) &&  
						 classInfo.contains("trlabel")) {
						final Elements tds = element.select("td");
						if (tds != null && tds.size() > 1) {
							if (t++ == 0) {
								eventPackage = new SiteEventPackage();
								team1 = new AGSoftwareTeamPackage();
								getGameTeam(tds, team1);
								if (team1.getTeam() == null || team1.getTeam().length() == 0) {
									t = 0;
								} else {
									eventPackage.setId(team1.getId());	
								}
							} else {
								team2 = new AGSoftwareTeamPackage();
								getGameTeam(tds, team2);
								
								if (team1 != null && team1.getTeam() != null &&
									team2 != null && team2.getTeam() != null) {
									Date eventDate = null;
									String cDate = team1.gettDate();
									LOGGER.debug("Team1: " + team1);
									LOGGER.debug("cDate: " + cDate);
		
									try {
										final Calendar now = Calendar.getInstance();
										int offset = now.get(Calendar.DST_OFFSET);
										cDate = cDate.replaceAll("<br>", " " + String.valueOf(now.get(Calendar.YEAR)) + " ");
										cDate += " " + timeZoneLookup(timezone, offset);
		
										eventDate = DATE_FORMAT.parse(cDate);
									} catch (ParseException pe) {
										LOGGER.error("ParseExeption for " + cDate, pe);
										// Throw an exception
										throw new BatchException(AppErrorMessage.SITE_PARSER_EXCEPTION  + " Date parsing exception for " + cDate);
									}
									team1.setEventdatetime(eventDate);
									team2.setEventdatetime(eventDate);
									final String dateOfEvent = cDate;
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
		}

		LOGGER.info("List<AgSoftwareEventPackage>: " + events);
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
	private int getGameTeam(Elements elements, AGSoftwareTeamPackage team) throws BatchException {
		LOGGER.info("Entering getGameTeam()");
		LOGGER.debug("AgSoftwareTeamPackage: " + team);
		int size = 0;

		if (elements != null && elements.size() == 9) {
			LOGGER.info("elements.size(): " + elements.size());
			size = 9;
			for (int x = 0;(elements != null && x < elements.size()); x++) {
				final Element td = elements.get(x);
	
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
					case 3:
						team = getSpread(td, team);
						break;
					case 4:
						team = getMoneyLine(td, team);
						break;
					case 5:
						team = getTotal(td, team);
						break;
					case 6:
					case 7:
					case 8:
					default:
						break;
				}
			}
		} else if (elements != null && elements.size() == 8) {
				LOGGER.info("elements.size(): " + elements.size());
				size = 8;
				for (int x = 0;(elements != null && x < elements.size()); x++) {
					final Element td = elements.get(x);
		
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
						case 3:
							team = getSpread(td, team);
							break;
						case 4:
							team = getMoneyLine(td, team);
							break;
						case 5:
							team = getTotal(td, team);
							break;
						case 6:
						case 7:
						default:
							break;
					}
				}
		} else if (elements != null && elements.size() == 7) {
			LOGGER.info("elements.size(): " + elements.size());
			size = 7;
			for (int x = 0;(elements != null && x < elements.size()); x++) {
				final Element td = elements.get(x);
	
				switch (x) {
					case 0:
						team = getEventId(td, team);
						break;
					case 1:
						team = getTeam(td, team);
						break;
					case 2:
						team = getSpread(td, team);
						break;
					case 3:
						team = getMoneyLine(td, team);
						break;
					case 4:
						team = getTotal(td, team);
						break;
					case 5:
					case 6:
					default:
						break;
				}
			}
		} else if (elements != null) {
			LOGGER.info("elements.size(): " + elements.size());
		}

		LOGGER.info("Exiting getGameTeam()");
		return size;
	}

	/**
	 * 
	 * @param td
	 * @param team
	 * @return
	 */
	private AGSoftwareTeamPackage getDate(Element td, AGSoftwareTeamPackage team) {
		LOGGER.info("Entering getDate()");
		LOGGER.debug("Element: " + td);

		// Date String
		String date = td.html();
		if (date != null) {
			date = date.replaceAll("&nbsp;", "");
			date = date.trim();
			if (date.contains("<div")) {
				int bindex = date.indexOf("<div");
				date = date.substring(0, bindex);
				date = date.trim();
				date = date.replaceAll("\\n", "");
				date = date.replaceAll("\\r", "");
			}
			team.settDate(date);
		}

		LOGGER.info("Exiting getDate()");
		return team;
	}

	/**
	 * 
	 * @param td
	 * @param team
	 * @return
	 */
	private AGSoftwareTeamPackage getEventId(Element td, AGSoftwareTeamPackage team) {
		LOGGER.info("Entering getEventId()");
		LOGGER.debug("Element: " + td);

		String eventId = getHtmlFromElement(td, "div", 0, true);
		if (eventId != null) {
			eventId = eventId.replaceAll("\\.", "");
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
	private AGSoftwareTeamPackage getTeam(Element td, AGSoftwareTeamPackage team) {
		LOGGER.info("Entering getTeam()");
		LOGGER.debug("Element: " + td);

		// Team String
		String teamName = td.html();
		if (teamName != null) {
			if (teamName.contains("<br>")) {
				int index = teamName.indexOf("<br");
				teamName = teamName.substring(0, index);
			}
			teamName = teamName.replaceAll("&nbsp;", "");
			teamName = teamName.replaceAll("\\.", "");
			teamName = teamName.trim();
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
	private AGSoftwareTeamPackage getSpread(Element td, AGSoftwareTeamPackage team) throws BatchException {
		LOGGER.info("Entering getSpread()");
		LOGGER.debug("Element: " + td);

		// First get the input field information
		Map<String, String> hashMap = parseInputField(td.select("input"), 0);
		team.setGameSpreadInputId(hashMap.get("id"));
		team.setGameSpreadInputName(hashMap.get("name"));
		team.setGameSpreadInputValue(hashMap.get("value"));

		// Get the select fields
		hashMap = parseSelectField(td.select("select"));
		team.setGameSpreadSelectId(hashMap.get("id"));
		team.setGameSpreadSelectName(hashMap.get("name"));

		final Elements options = td.select("select option");
		if (options != null && options.size() > 0) {
			team = (AGSoftwareTeamPackage)parseSpreadSelectOption(options, team, " ", null);
		} else {
			// -2½  +200; Now parse the data
			String data = getHtmlFromLastIndex(td, ">");
			team = (AGSoftwareTeamPackage)parseSpreadData(reformatValues(data), 0, " ", null, team);
		}

		LOGGER.info("Exiting getSpread()");
		return team;
	}

	/**
	 * 
	 * @param td
	 * @param team
	 * @return
	 * @throws BatchException
	 */
	private AGSoftwareTeamPackage getTotal(Element td, AGSoftwareTeamPackage team) throws BatchException {
		LOGGER.info("Entering getTotal()");
		LOGGER.debug("Element: " + td);

		// First get the input field information
		Map<String, String> hashMap = parseInputField(td.select("input"), 0);
		team.setGameTotalInputId(hashMap.get("id"));
		team.setGameTotalInputName(hashMap.get("name"));
		team.setGameTotalInputValue(hashMap.get("value"));

		// Get the select fields
		hashMap = parseSelectField(td.select("select"));
		team.setGameTotalSelectId(hashMap.get("id"));
		team.setGameTotalSelectName(hashMap.get("name"));

		final Elements options = td.select("select option");
		if (options != null && options.size() > 0) {
			team = (AGSoftwareTeamPackage)parseTotalSelectOption(options, team, " ", null);
		} else {
			final String data = getHtmlFromLastIndex(td, ">");			
			team = (AGSoftwareTeamPackage)parseTotalData(reformatValues(data), 0, " ", null, team);
		}

		LOGGER.info("Exiting getTotal()");
		return team;
	}

	/**
	 * 
	 * @param td
	 * @param team
	 * @return
	 */
	private AGSoftwareTeamPackage getMoneyLine(Element td, AGSoftwareTeamPackage team) {
		LOGGER.info("Entering getMoneyLine()");
		LOGGER.debug("Element: " + td);
		
		// First get the input field information
		final Map<String, String> hashMap = parseInputField(td.select("input"), 0);
		team.setGameMLInputId(hashMap.get("id"));
		team.setGameMLInputName(hashMap.get("name"));
		LinkedHashMap<String, String> mlValue = new LinkedHashMap<String, String>();
		mlValue.put("0", hashMap.get("value"));
		team.setGameMLInputValue(mlValue);

		// Parse ML Data
		String data = getHtmlFromLastIndex(td, ">");
		team = (AGSoftwareTeamPackage)parseMlData(reformatValues(data), 0, team);

		LOGGER.info("Exiting getMoneyLine()");
		return team;
	}

	/**
	 * 
	 * @param divs
	 * @param map
	 * @param type
	 * @param sport
	 * @param foundString
	 * @param menuString
	 * @return
	 */
	private Map<String, String> findMenu(Elements divs, Map<String, String> map, String[] type, String sport[], String foundString, String menuString) {
		LOGGER.info("Entering findMenu()");

		for (int x = 0; (divs != null && x < divs.size()); x++) {
			final Element div = divs.get(x);
			boolean foundDiv = false;
			for (int y = 0; y < type.length; y++) {
				for (int fk = 0; fk < sport.length; fk++) {
					foundDiv = foundSport(div, foundString, sport[fk]);
					LOGGER.debug("foundDiv: " + foundDiv);
	
					// Found the event
					if (foundDiv) {
						Map<String, String> hashMap = parseInputField(div.select(foundString + " input"), 0);
						map.put(hashMap.get("name"), "on");
	
						final Elements elements = div.select(foundString);
						for (int z = 0; z < elements.size(); z++) {
							Element a = elements.get(z);
							if (a != null) {
								String onclick = a.attr("onclick");
								if (onclick != null && onclick.contains("toggleCheckboxes") && !onclick.contains("typediv_prop")) {
									// i.e.: toggleCheckboxes('Basketball','NCAA','typediv_NCAA');
									int index = onclick.indexOf("'");
									if (index != -1) {
										onclick = onclick.substring(index + "'".length());
										int eindex = onclick.indexOf("'");
										if (eindex != -1) {
//											map.put("sportType", onclick.substring(0, eindex));
											onclick = onclick.substring(eindex + "'".length());
											index = onclick.indexOf("'");
											if (index != -1) {
												onclick = onclick.substring(index + "'".length());
												eindex = onclick.indexOf("'");
												if (eindex != -1) {
//													map.put("sportSubType", onclick.substring(0, eindex));
												}
											}
										}
									}
								}
							}
						}
	
						map = getMenuData(div, menuString, type[y], map);
						LOGGER.debug("Map: " + map);
					}
				}
			}
		}

		LOGGER.info("Exiting findMenu()");
		return map;
	}

	/**
	 * 
	 * @param div
	 * @param select
	 * @param sport
	 * @return
	 */
	private boolean foundSport(Element div, String select, String sport) {
		LOGGER.info("Entering foundSport()");
		LOGGER.debug("select: " + select);
		LOGGER.debug("sport: " + sport);
		boolean foundDiv = false;

		String divData = getHtmlFromElement(div, select, 0, false);
		LOGGER.debug("divData: " + divData);

		// Check if we found div
		if (divData != null) {
			int index = divData.lastIndexOf(">");
			if (index != -1) {
				if (!divData.contains("Hockey_NCAA")) {
					LOGGER.debug("foundSportDivData: " + divData);
					divData = divData.substring(index + ">".length());
					LOGGER.debug("sportData: " + divData);
					if (divData != null && divData.equals(sport)) {	
						foundDiv = true;	
					}
				}
			}
		}

		LOGGER.info("Exiting foundSport()");
		return foundDiv;
	}

	/**
	 * 
	 * @param div
	 * @param element
	 * @param map
	 * @return
	 */
	private Map<String, String> getMenuData(Element div, String element, String name, Map<String, String> map) {
		LOGGER.info("Entering getMenuData()");
		LOGGER.debug("Div: " + div);
		LOGGER.debug("element: " + element);

		Elements divdiv = div.select(element);
		for (int x = 0; (divdiv != null && x < divdiv.size()); x++) {
			Element sdiv = divdiv.get(x);
			if (sdiv != null) {
				Elements as = sdiv.select("a");
				for (int y = 0; (as != null && y < as.size()); y++) {
					Element a = as.get(y);
					if (a != null) {
						String menuName = a.html();
						menuName = menuName.replaceAll("&nbsp;", "");
						menuName = menuName.trim();
						if (menuName.equals(name)) {
							Map<String, String> hashMap = parseInputField(sdiv.select("input"), 0);
							map.put(hashMap.get("name"), "on");
						}
					}
				}				
			}
		}

		LOGGER.info("Exiting getMenuData()");
		return map;
	}
	
	/**
	 * 
	 * @param td
	 * @param pe
	 */
	private void getTicketNumber(Element td, PendingEvent pe) {
		LOGGER.info("Entering getTicketNumber()");

		// <a href="javascript:void(0)" style="text-decoration:none" onclick="javascript:document.forms['WagerListForm'].ticketNumber.value = 112867671; document.forms['WagerListForm'].wagerNumber.value = 1; document.forms['WagerListForm'].gradeNumber.value = 0; document.forms['WagerListForm'].submit(); return false">
		//    <font class="item">112867671-1</font>
		// </a>
		final Elements fonts = td.select("a font");
		if (fonts != null && fonts.size() > 0 ) {
			pe.setTicketnum(super.reformatValues(fonts.get(0).html()));
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

		// <td nowrap="nowrap">6/16/2017 0:26 AM</td>
		String html = td.html();
		if (html != null && html.length() > 0) {
			LOGGER.info("html: " + html);
			pe.setDateaccepted(super.reformatValues(html + " " + super.timezone));
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

		// <td>Money Line</td>
		final String html = td.html();
		if (html != null && html.length() > 0) {
			LOGGER.info("html: " + html);
			if ("Money Line".equals(html)) {
				pe.setEventtype("ml");
			} else if ("Spread".equals(html)) {
				pe.setEventtype("spread");
			} else {
				pe.setEventtype("total");
			}
		}

		LOGGER.info("Exiting getEventType()");
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 */
	private void getRisk(Element td, PendingEvent pe) {
		LOGGER.info("Entering getRisk()");
		LOGGER.debug("td: " + td);

		// <td align="RIGHT">39.25</td>
		String html = td.html();
		if (html != null && html.length() > 0) {
			html = super.reformatValues(html);
			LOGGER.info("html: " + html);
			pe.setRisk(html);
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

		// <td align="RIGHT">  25.00</td>
		String html = td.html();
		if (html != null && html.length() > 0) {
			html = super.reformatValues(html);
			LOGGER.info("html: " + html);
			pe.setWin(html);
		}

		LOGGER.info("Exiting getWin()");
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 */
	private void getEventInfo(Element td, PendingEvent pe) {
		LOGGER.info("Entering getEventInfo()");

		// <td style="text-align:left;white-space:normal;padding-left:10px;">
		// Pregame Baseball
		// <br>
		//   957 Washington Nationals -157 for Game 
		// </td>
		String sportName = "";
		String html = td.html();
		int index = html.indexOf("<br>");
		if (index != -1) {
//			pe.setGamesport(html.substring(0, index).trim());
			sportName = html.substring(0, index).trim().replace("Pregame ", "").replace("&nbsp;", "");
			// Set the game sport
			LOGGER.error("sportName: " + sportName);
			pe.setGamesport(sportName);
			html = html.substring(index + 4);
		}

		String sportType = "";
		index = html.indexOf(" W ");
		if (index != -1) {
			sportType = "W";
			html = html.replace(" W ", "");
		}
		// Set the game type
		pe.setGametype(getGameType(sportName, sportType));

		// Get the Rotation ID
		html = getRotationId(html, pe);

		// Now parse the team
		getEventInfo(html, pe);

		// Now parse line type
		getLineType(html, pe);

		LOGGER.info("Exiting getEventInfo()");
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
			int spaceIndex = html.indexOf(" ");
			if (spaceIndex != -1) {
				// Get the Rotation ID
				pe.setRotationid(super.reformatValues(html.substring(0, spaceIndex)));
				html = html.substring(spaceIndex + 1);
			}
		}

		LOGGER.info("Exiting getRotationId()");
		return html;
	}

	/**
	 * 
	 * @param html
	 * @param pe
	 */
	private void getEventInfo(String html, PendingEvent pe) {
		LOGGER.info("Entering getEventInfo()");
		LOGGER.debug("html: " + html);

		if ("spread".equals(pe.getEventtype())) {
			String juiceString = null;
			String lineString = null;
			int forIndex = html.indexOf(" for ");
			if (forIndex != -1) {
				html = html.substring(0, forIndex);
				LOGGER.debug("html: " + html);
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
						pe.setTeam(html);
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
			int underIndex = html.indexOf("under");
			int overIndex = html.indexOf("over");

			if (underIndex != -1) {
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
			} else if (overIndex != -1) {
				pe.setTeam(html.substring(0, overIndex - 1));
				pe.setLineplusminus("o");

				html = html.substring(overIndex + 5);
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
		} else if ("ml".equals(pe.getEventtype())) {
			String juiceString = null;
			int forIndex = html.indexOf(" for ");
			if (forIndex != -1) {
				html = html.substring(0, forIndex);
				int juiceIndex = html.lastIndexOf(" ");
				if (juiceIndex != -1) {
					juiceString = html.substring(juiceIndex + 1);
					html = html.substring(0, juiceIndex);
					pe.setTeam(html);
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
				html = super.reformatValues(html.trim());
				if ("1st Half".equals(html) || "1st 5 Innings".equals(html) || "1st Period".equals(html)) {
					pe.setLinetype("first");
				} else if ("2nd Half".equals(html) || "2nd Period".equals(html)) {
					pe.setLinetype("second");
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
	 * @param sportName
	 * @param sportType
	 * @return
	 */
	private String getGameType(String sportName, String sportType) {
		LOGGER.info("Entering getGameType()");
		LOGGER.info("sportName: " + sportName);
		LOGGER.info("sportType: " + sportType);

		if ("Football".equals(sportName)) {
			if ("".equals(sportType)) {
				return "NFL";
			} else {
				return "NCAA Football";
			}			
		} else if ("Basketball".equals(sportName)) {
			if ("".equals(sportType)) {
				return "NBA";
			} else if ("W".equals(sportType)) {
				return "WNBA";
			} else {
				return "NCAA Basketball";	
			}
		} else if ("Baseball".equals(sportName)) {
			return "MLB";
		} else if ("Hockey".equals(sportName)) {
			return "NHL";
		}

		LOGGER.info("Exiting getGameType()");
		return "";
	}
}