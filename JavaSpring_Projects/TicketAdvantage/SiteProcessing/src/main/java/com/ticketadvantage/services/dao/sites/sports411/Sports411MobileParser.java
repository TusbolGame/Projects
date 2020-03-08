/**
 * 
 */
package com.ticketadvantage.services.dao.sites.sports411;

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

import com.ticketadvantage.services.dao.sites.SelectOptionData;
import com.ticketadvantage.services.dao.sites.SiteParser;
import com.ticketadvantage.services.dao.sites.SiteTeamPackage;
import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;

/**
 * @author jmiller
 *
 */
public class Sports411MobileParser extends SiteParser {
	protected static final Logger LOGGER = Logger.getLogger(Sports411MobileParser.class);
	protected static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd yyyy hh:mm a z");

	/**
	 * Constructor
	 */
	public Sports411MobileParser() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final Sports411MobileParser smp = new Sports411MobileParser();
			final String xhtml = "";
			final Map<String, String> inputFields = new HashMap<String, String>();
			List<Sports411EventPackage> sep = smp.parseGames(xhtml, "ncaabsecond", inputFields);
			if (sep != null && sep.size() > 0) {
				LOGGER.error("sep: " + sep);
			}
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
		final Map<String, String> map = new HashMap<String, String>();

		// Get the document object
		final Document doc = parseXhtml(xhtml);

		// Get hidden input fields
		final String[] types = new String[] { "text", "password", "image" };
		final Elements forms = doc.select("form");
		for(int x = 0; (forms != null && x < forms.size()); x++) {
			final Element form = forms.get(x);
			String action = form.attr("action");
			if (action != null && action.equals("/loginvalidate.aspx")) {
				// Get form action field
				map.put("action", form.attr("action"));
				getAllElementsByType(form, "input", types, map);
			}
		}
		map.put("account", "");

		LOGGER.info("Exiting parseIndex()");
		return map;
	}

	/**
	 * 
	 * @param xhtml
	 * @return
	 */
	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseLogin(java.lang.String)
	 */
	@Override
	public Map<String, String> parseLogin(String xhtml) throws BatchException {
		LOGGER.info("Entering parseLogin()");
		Map<String, String> map = new HashMap<String, String>();

		// Parse the xhtml
		final Document doc = parseXhtml(xhtml);

		// Get the form information
		final Elements forms = doc.select("form");
		for (int x = 0; (forms != null && x < forms.size()); x++) {
			final Element form = forms.get(x);
			if (form != null) {
				// Get form action field
				map.put("action", form.attr("action"));
				map = getAllElementsByNameByElement(form, "input", "value", map);
			}
		}

		LOGGER.info("Exiting parseLogin()");
		return map;
	}

	/**
	 * 
	 * @param xhtml
	 * @param actionName
	 * @return
	 * @throws BatchException
	 */
	public Map<String, String> parseLogin2(String xhtml, String actionName) throws BatchException {
		LOGGER.info("Entering parseLogin2()");
		final Map<String, String> map = new HashMap<String, String>();

		// Get the document object
		final Document doc = parseXhtml(xhtml);

		// Get hidden input fields
		final String[] types = new String[] { "hidden", "text" };
		final Elements forms = doc.select("form");
		for(int x = 0; (forms != null && x < forms.size()); x++) {
			final Element form = forms.get(x);
			String action = form.attr("action");
			if (action != null && action.contains(actionName)) {
				// Get form action field
				map.put("action", action);
				getAllElementsByType(form, "input", types, map);
			}
		}

		LOGGER.info("Exiting parseLogin2()");
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
		map = findMenu(doc.select("#frameLines div div div section div ul li"), map, type, sport, "a", "input");

		// Remove the input fields that shouldn't be sent
		map = removeInputFields(map);
		map.remove("ctl00$Feedback2$ctl00$feedbackBtnSubmit3");
		map.put("ctl00$Feedback1$ctl00$txtComments", "");
		map.put("ctl00$TimeZoneDesktop$ctl00$ddlTZone", "NA");
		map.put("ctl00$mainContent$btnContinueTop", "Continue");

		LOGGER.info("Exiting parseMenu()");
		return map;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseGames(java.lang.String, java.lang.String, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <Sports411EventPackage> List<Sports411EventPackage> parseGames(String xhtml, String type, Map<String, String> inputFields) throws BatchException {
		LOGGER.info("Entering parseGames()");
		LOGGER.debug("xhtml: " + xhtml);
		LOGGER.debug("type: " + type);
		LOGGER.debug("InputFields: " + inputFields);
		List<?> events = null;
		
		// Parse to get the Document
		final Document doc = parseXhtml(xhtml);
		Map<String, String> tempFields = new HashMap<String, String>();

		if (xhtml.contains("No props available at this time")) {
			// Throw Exception
			throw new BatchException(BatchErrorCodes.GAME_NOT_AVAILABLE, BatchErrorMessage.GAME_NOT_AVAILABLE, "No props available at this time" + xhtml);
		}

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

		// abcgrand.ag
		Elements elements = doc.select("#printLines section div div");
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
		inputFields.remove("ctl00$Feedback2$ctl00$feedbackBtnSubmit3");
		inputFields.remove("ctl00$Feedback2$ctl00$txtAccount");
		inputFields.put("ctl00$Feedback1$ctl00$txtComments", "");
		inputFields.put("SelectedGame", "");
		inputFields.put("removeclick", "");
		inputFields.put("__EVENTTARGET", "");
		inputFields.put("__EVENTARGUMENT", "");
		LOGGER.info("Exiting parseGames()");
		return (List<Sports411EventPackage>)events;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseEventSelection(java.lang.String, com.ticketadvantage.services.dao.sites.SiteTeamPackage, java.lang.String)
	 */
	@Override
	public Map<String, String> parseEventSelection(String xhtml, SiteTeamPackage siteTeamPackage, String type) throws BatchException {
		LOGGER.info("Entering parseEventSelection()");
		Map<String, String> map = new HashMap<String, String>();
		
		// Parse the HTML first
		final Document doc = parseXhtml(xhtml);

		final String[] types = new String[] { "hidden", "text", "submit", "number" };
		Element form = doc.getElementById("aspnetForm");
		if (form != null) {
			// Get form action field
			map.put("action", form.attr("action"));
			getAllElementsByTypeWithCheckbox(form, "input", types, map);
		}

		// Now get the select options if there are any
		if (type.equals("spread")) {
			setupSpreadWager(doc, (Sports411TeamPackage) siteTeamPackage);
		} else if (type.equals("total")) {
			setupTotalWager(doc, (Sports411TeamPackage) siteTeamPackage);
		}

		// Get the wager select
		final Elements selects = doc.select(".amounts select");
		LOGGER.debug("selects: " + selects);
		if (selects != null && selects.size() == 2) {
			parseSelectFieldByNumBlank(selects, 1, map);
		} else if (selects != null && selects.size() == 1) {
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

		// Check for a wager limit and change it 
		if (xhtml.contains("but this line has just changed to:")) {
			// Line changed throw exception
			throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed and cannot complete transaction", xhtml);			
		}
			
		// Check for a wager limit and change it 
		if (xhtml.contains("be accepted because Balance Exceeded")) {
			map.put("wagerbalanceexceeded", "true");
			return map;
		}

		// Remove the input fields that shouldn't be sent
		map = removeInputFields(map);
		map.remove("ctl00$Feedback2$ctl00$feedbackBtnSubmit3");
		map.remove("ctl00$Feedback2$ctl00$txtAccount");

		LOGGER.info("Exiting parseEventSelection()");
		return map;
	}

	/**
	 * 
	 * @param xhtml
	 * @param type
	 * @return
	 * @throws BatchException
	 */
	public Map<String, String> parseConfirmWager(String xhtml) throws BatchException {
		LOGGER.info("Entering parseConfirmWager()");
		Map<String, String> map = new HashMap<String, String>();

		// Parse the HTML first
		final Document doc = parseXhtml(xhtml);

		final String[] types = new String[] { "hidden", "submit", "password", "Password" };
		Element form = doc.getElementById("aspnetForm");
		if (form != null) {
			// Get form action field
			map.put("action", form.attr("action"));
			getAllElementsByType(form, "input", types, map);
		}

		// Check for a wager limit and change it 
		if (xhtml.contains("Exceeded. Limit For This Game is ")) {
			int index = xhtml.indexOf("Exceeded. Limit For This Game is ");
			if (index != -1) {
				xhtml = xhtml.substring(index + "Exceeded. Limit For This Game is ".length());
				index = xhtml.indexOf(" USD.");
				if (index != -1) {
					String wagerAmount = xhtml.substring(0, index);
					if (wagerAmount != null) {
						wagerAmount = wagerAmount.replaceAll(",", "");
						wagerAmount = wagerAmount.replaceAll(" ", "");
						map.put("wageramount", wagerAmount);
						return map;
					}
				}
			}
		}
		
		if (xhtml.contains("Max Wager Online Exceeded. Your Current Wager Limit is ")) {
			int index = xhtml.indexOf("Max Wager Online Exceeded. Your Current Wager Limit is ");
			if (index != -1) {
				xhtml = xhtml.substring(index + "Max Wager Online Exceeded. Your Current Wager Limit is ".length());
				index = xhtml.indexOf(" USD.");
				if (index != -1) {
					String wagerAmount = xhtml.substring(0, index);
					if (wagerAmount != null) {
						wagerAmount = wagerAmount.replaceAll(",", "");
						wagerAmount = wagerAmount.replaceAll(" ", "");
						map.put("wageramount", wagerAmount);
						return map;
					}
				}
			}
		}

		// Check for a wager limit and change it 
		if (xhtml.contains("Either `Risk` or `Win` Amount is below the ")) {
			int index = xhtml.indexOf("Either `Risk` or `Win` Amount is below the ");
			if (index != -1) {
				xhtml = xhtml.substring(index + "Either `Risk` or `Win` Amount is below the ".length());
				index = xhtml.indexOf(" USD");
				if (index != -1) {
					String wagerAmount = xhtml.substring(0, index);
					if (wagerAmount != null) {
						wagerAmount = wagerAmount.replaceAll(",", "");
						wagerAmount = wagerAmount.replaceAll(" ", "");
						map.put("wagerminamount", wagerAmount);
						return map;
					}
				}
			}
		}

		// Check for a wager limit and change it 
		if (xhtml.contains("but this line has just changed to:")) {
			// Line changed throw exception
			throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed and cannot complete transaction", xhtml);			
		}

		// Check for a wager limit and change it 
		if (xhtml.contains("be accepted because Balance Exceeded")) {
			map.put("wagerbalanceexceeded", "true");
			return map;
		}

		// Confirm the amounts
		Elements spans = doc.select(".evenRow td table tr td span");
		if (spans != null && spans.size() > 0) {
			Element risk = spans.get(2);
			map.put("risk", risk.html());
			Element win = spans.get(3);
			map.put("win", win.html());
		}

		map.remove("ctl00$Feedback2$ctl00$txtAccount");
		map.remove("ctl00$Feedback2$ctl00$feedbackBtnSubmit3");
		map.remove("ctl00$Feedback1$ctl00$feedbackBtnSubmit"); 
		map.remove("ctl00$mainContent$btnContinueBottom"); 
		map.remove("ctl00$TimeZoneDesktop$ctl00$btnSetTimeZone");

		LOGGER.info("Exiting parseConfirmWager()");
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
		final Map<String, String> wagers = new HashMap<String, String>();

		// Parse to get the Document
		final Document doc = parseXhtml(xhtml);

		// Check for a wager limit and change it 
		if (xhtml.contains("but this line has just changed to:")) {
			// Line changed throw exception
			throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed and cannot complete transaction", xhtml);			
		}

		// Check for a wager limit and change it 
		if (xhtml.contains("be accepted because Balance Exceeded")) {
			wagers.put("wagerbalanceexceeded", "true");
			return wagers;
		}

		// First get the Risk
		String risk = getHtmlFromAllElements(doc, ".offering_pair td a font b");
		if (risk != null && risk.length() > 0) {
			risk = risk.substring(1);
			wagers.put("risk", risk);
		} else {
			// Throw Exception
			throw new BatchException(BatchErrorCodes.ERROR_PARSING_WAGER_INFO, BatchErrorMessage.ERROR_PARSING_WAGER_INFO, "No element found for " + xhtml);
		}

		// Now get the Win
		String win = getHtmlFromAllElements(doc, ".offering_pair td a font b");
		if (win != null && win.length() > 0) {
			win = win.substring(1);
			wagers.put("win", win);
		} else {
			// Throw Exception
			throw new BatchException(BatchErrorCodes.ERROR_PARSING_WAGER_INFO, BatchErrorMessage.ERROR_PARSING_WAGER_INFO, "No element found for " + xhtml);
		}

		LOGGER.info("Exiting parseWagerTypes()");
		return wagers;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseTicketNumber(java.lang.String)
	 */
	@Override
	public String parseTicketNumber(String xhtml) throws BatchException {
		LOGGER.info("Entering parseTicketNumber()");
		String ticketNumber = "";

		// Check for a wager limit and change it 
		if (xhtml.contains("but this line has just changed to:")) {
			// Line changed throw exception
			throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed and cannot complete transaction", xhtml);			
		}

		// Parse to get the Document
		final Document doc = parseXhtml(xhtml);
		Elements spans = doc.select(".ticket");
		if (spans != null && spans.size() > 0) {
			Element span = spans.get(0);
			String tk = span.html();
			if (tk != null && tk.length() > 0) {
				ticketNumber = tk.trim();
			} else {
				throw new BatchException(BatchErrorCodes.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, BatchErrorMessage.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, tk, xhtml);
			}
		} else {
			throw new BatchException(BatchErrorCodes.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, BatchErrorMessage.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, ticketNumber, xhtml);
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
	public Map<String, String> parseLineChange(String xhtml) throws BatchException {
		LOGGER.info("Entering processLineChange()");
		final Map<String, String> map = new HashMap<String, String>();
		String originalXhtml = xhtml;

//		<div class="line changed">
//			<span class="previousLine">(!) You had selected:-1-110</span>
//			<span class="newLine">, but this line has just changed to:-1-113Â </span>
//		</div>

//		<nav class="sysNavFoot">	
//			<div class="buttonStrip">	
//				<input id="cancelBtn" class="button" type="button" value="Cancel Bet" onclick="location.href='SelectLeagues.aspx?wagertype=0'"/>	
//				<input type="submit" name="ctl00$mainContent$btnContinueBottom" value="Confirm your bet" id="ctl00_mainContent_btnContinueBottom" tabindex="1" class="grad_FC0" />	
//			</div>	
//		</nav>

		int index = xhtml.indexOf("but this line has just changed to:");
		if (index != -1) {
			xhtml = xhtml.substring(index + "but this line has just changed to:".length());
			index = xhtml.indexOf("</span>");
			xhtml = xhtml.substring(0, index).toLowerCase();

			if (xhtml.startsWith("+") || xhtml.startsWith("-")) {
				map.put("valueindicator", xhtml.substring(0, 1));
				xhtml = xhtml.substring(1);
				index = xhtml.indexOf("-");
				if (index != -1) {
					final String value = xhtml.substring(0, index);
					map.put("value", reformatValues(value));
					xhtml = xhtml.substring(index + 1);

					if (xhtml.startsWith("+") || xhtml.startsWith("-")) {
						map.put("juiceindicator", xhtml.substring(0, 1));
						map.put("juice", reformatValues(xhtml.substring(1)));
					}
				} else {
					index = xhtml.indexOf("+");
					if (index != -1) {
						final String value = xhtml.substring(0, index);
						map.put("value", reformatValues(value));
						xhtml = xhtml.substring(index + 1);

						if (xhtml.startsWith("+") || xhtml.startsWith("-")) {
							map.put("juiceindicator", xhtml.substring(0, 1));
							map.put("juice", reformatValues(xhtml.substring(1)));
						}
					} else {
						index = xhtml.indexOf("ev");
						if (index != -1) {
							final String value = xhtml.substring(0, index);
							map.put("value", reformatValues(value));
							
							map.put("juiceindicator", "+");
							map.put("juice", "100");
						} else {
							
						}
					}
				}
			} else if (xhtml.startsWith("pk")) {
				
			}
			map.put("valueindicator", xhtml.substring(0, 1));
			map.put("value", reformatValues(xhtml.substring(1)));
			map.put("juiceindicator", xhtml.substring(0, 1));
			map.put("juice", reformatValues(xhtml.substring(1)));
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
	 * @param xhtml
	 * @return
	 */
	public String parseSchedule(String xhtml) {
		LOGGER.info("Entering parseSchedule()");
		LOGGER.debug("xhtml: " + xhtml);

		//window.location= 'http://wager.abcgrand.ag/wager/CreateWager.aspx?WT=0&lg=32&sel=1_2292840_-3.5_-110'}
		String newUrl = "";
		int index = xhtml.indexOf("window.location=");
		if (index != -1) {
			xhtml = xhtml.substring(index + "window.location=".length());
			index = xhtml.indexOf("'");
			if (index != -1) {
				int endIndex = xhtml.indexOf("'}");
				if (endIndex != -1) {
					newUrl = xhtml.substring(index + 1, endIndex);
				}
			}
		} else {
			index = xhtml.indexOf("Location: ");
			if (index != -1) {
				newUrl = xhtml.substring(index + "Location: ".length() + 1);
			}
		}

		LOGGER.info("Exiting parseSchedule()");
		return newUrl;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#getFootballData(org.jsoup.select.Elements)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected List<Sports411EventPackage> getGameData(Elements elements) throws BatchException {
		LOGGER.info("Entering getGameData()");
		Sports411EventPackage eventPackage = null;
		final List<Sports411EventPackage> events = new ArrayList<Sports411EventPackage>();

		if (elements != null) {
			Sports411TeamPackage team1 = null;
			Sports411TeamPackage team2 = null;
			String gameDate = "";
			final Calendar now = Calendar.getInstance();
			int offset = now.get(Calendar.DST_OFFSET);

			for (int x = 0; x < elements.size(); x++) {
				// Loop through the elements and then check for the dates
				final Element element = elements.get(x);
				LOGGER.debug("Element: " + element);

				if (element != null) {
					String classInfo = element.attr("class");
					LOGGER.debug("ClassInfo: " + classInfo);

					if ((classInfo != null && classInfo.length() > 0) && 
						("banner".equalsIgnoreCase(classInfo))) {
						Elements date = element.select("span");
						if (date != null && date.size() > 0) {
							Element dt = date.get(0);
							String gDate = dt.html();
							if (gDate != null) {
								int idx = gDate.indexOf(" - ");
								if (idx != -1) {
									gDate = gDate.substring(idx + " - ".length());
									gDate = gDate.trim();
									gDate = gDate.replaceAll("Â", "");
									gameDate = gDate = gDate.replaceAll("&nbsp;", "");
								} else {
									idx = gDate.indexOf("- ");
									if (idx != -1) {
										gDate = gDate.substring(idx + "- ".length());
										gDate = gDate.trim();
										gDate = gDate.replaceAll("Â", "");
										gDate = gDate.replaceAll("&nbsp;", "");
										gameDate = gDate = gDate.replaceAll("&nbsp;", "");
									}
								}
							}
						}
						LOGGER.debug("gameDate: " + gameDate);
					} else if ((classInfo != null && classInfo.length() > 0) && 
						("game".equals(classInfo))) {
						eventPackage = new Sports411EventPackage();
						team1 = new Sports411TeamPackage();
						team2 = new Sports411TeamPackage();
						String gameTime = "";
						Elements left = element.select(".gameTime");
						if (left != null && left.size() > 0) {
							final Element tdTime = left.get(0);
							LOGGER.debug("tdTime: " + tdTime);
							final Elements smalls = tdTime.select("small");
							if (smalls != null && smalls.size() > 0) {
								final Element small = smalls.get(0);
								gameTime = small.html();
								if (gameTime != null) {
									gameTime = gameTime.trim();
									gameTime = gameTime.replaceAll("Â", "");
									gameTime = gameTime.replaceAll("&nbsp;", "");
									eventPackage.setDateofevent(gameDate);
									eventPackage.setTimeofevent(gameTime);
									eventPackage.setEventdate(gameDate + " " + gameTime);
									Date eventDate = null;
									try {
										eventDate = setupDate(DATE_FORMAT, gameDate, gameTime);
										LOGGER.debug("eventDate: " + eventDate);
									} catch (Throwable t) {
										LOGGER.error(t);
										Calendar cal = Calendar.getInstance();
								        cal.setTime(new Date());
								        cal.add(Calendar.DATE, 1); //minus number would decrement the days
								        eventDate = cal.getTime();
									}
									eventPackage.setEventdatetime(eventDate);
									team1.setEventdatetime(eventDate);
									team2.setEventdatetime(eventDate);
									String cDate = "";
									cDate = gameDate + " " + String.valueOf(now.get(Calendar.YEAR)) + " " + gameTime;
									cDate += " " + timeZoneLookup(timezone, offset);
									team1.settDate(cDate);
									team2.settDate(cDate);
								}
							}

							// Get the MAX values
							final Elements spans = tdTime.select("span");
							if (spans != null && spans.size() > 0) {
								Element span = spans.get(0);
								eventPackage = getMaxValues(span, eventPackage);
							}
						}

						final Elements gameDetails = element.select(".gameDetail");
						if (gameDetails != null && gameDetails.size() > 0) {
							final Element gameDetail = gameDetails.get(0);
							team1 = getTeamInfo(gameDetail.select(".visitor"), team1);
							team2 = getTeamInfo(gameDetail.select(".home"), team2);

							final Elements labelTs = gameDetail.select(".labelT");
							final Elements uls = gameDetail.select("ul");
							if (labelTs != null && labelTs.size() > 0 && 
								uls != null && uls.size() > 0) {
								int counter = 0;
								for (Element labelT : labelTs) {
									String label = labelT.html();
									if (label != null) {
										label = label.trim();
										label = label.replaceAll("Â", "");
										label = label.replaceAll("&nbsp;", "");
										final Element ul = uls.get(counter);
										final Elements lis = ul.select("li");
										int licounter = 0;
										for (Element li : lis) {
											if (label.equals("Spread")) {
												if (licounter == 0) {
													team1 = getSpread(li.select(".pickInput").first(), team1);
												} else {
													team2 = getSpread(li.select(".pickInput").first(), team2);
												}
											}
											if (label.equals("Total")) {
												if (licounter == 0) {
													team1 = getTotal(li.select(".pickInput").first(), team1);
												} else {
													team2 = getTotal(li.select(".pickInput").first(), team2);
												}
											}
											if (label.equals("Odds")) {
												if (licounter == 0) {
													team1 = getMoneyLine(li.select(".pickInput").first(), team1);
												} else {
													team2 = getMoneyLine(li.select(".pickInput").first(), team2);
												}
											}
											licounter++;
										}
									}
									counter++;
								}
							}
						}
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

		LOGGER.debug("EventPackages: " + events);
		LOGGER.info("Exiting getGameData()");
		return events;
	}

	/**
	 * 
	 * @param td
	 * @param team
	 * @return
	 */
	protected Sports411TeamPackage getEventId(Element td, Sports411TeamPackage team) {
		LOGGER.info("Entering getEventId()");
		LOGGER.debug("Element: " + td);

		String eventId = td.html();
		if (eventId != null && eventId.length() > 0) {
			eventId = eventId.trim();
			eventId = eventId.replaceAll("Â", "");
			eventId = eventId.replaceAll("&nbsp;", "");
			team.setEventid(eventId);
			team.setId(Integer.parseInt(eventId));
		}

		LOGGER.info("Exiting getEventId()");
		return team;
	}

	/**
	 * 
	 * @param teams
	 * @param teamPackage
	 * @return
	 */
	protected Sports411TeamPackage getTeamInfo(Elements teams, Sports411TeamPackage teamPackage) {
		LOGGER.info("Entering getTeamInfo()");

		if (teams != null && teams.size() > 0) {
			final Element team = teams.get(0);
			final Elements rotations = team.select(".rotation");
			if (rotations != null && rotations.size()> 0) {
				teamPackage = getEventId(rotations.get(0), teamPackage);
			}
			teamPackage = getTeam(team, teamPackage);
		}

		LOGGER.info("Exiting getTeamInfo()");
		return teamPackage;
	}

	/**
	 * 
	 * @param td
	 * @param team
	 * @return
	 */
	protected Sports411TeamPackage getTeam(Element td, Sports411TeamPackage team) {
		LOGGER.info("Entering getTeam()");
		LOGGER.debug("Element: " + td);

		// Team String
		String teamName = td.html();
		if (teamName != null && teamName.length() > 0) {
			teamName = teamName.trim();
			teamName = teamName.replaceAll("Â", "");
			teamName = teamName.replaceAll("&nbsp;", "");

			if (teamName.contains("gPitch")) {
				int index = teamName.indexOf("</span>");
				if (index != -1) {
					teamName = teamName.substring(index + 7);
					index = teamName.indexOf("<span");
					if (index != -1) {
						team.setTeam(teamName.substring(0, index));
					}
				}
			} else {	
				int index = teamName.indexOf("</span>");
				if (index != -1) {
					team.setTeam(teamName.substring(index + 7));				
				}
			}
		}

		LOGGER.info("Exiting getTeam()");
		return team;
	}

	/**
	 * 
	 * @param span
	 * @param team
	 * @return
	 */
	protected Sports411TeamPackage getSpread(Element span, Sports411TeamPackage team) {
		LOGGER.info("Entering getSpread()");
		LOGGER.debug("Element: " + span);

		// First get the input field information
		final Map<String, String> hashMap = parseInputField(span.select("input"), 0);
		team.setGameSpreadInputId(hashMap.get("name"));
		team.setGameSpreadInputName(hashMap.get("name"));
		team.setGameSpreadInputValue(hashMap.get("value"));

		// Setup spread
		String spread = getHtmlFromElement(span, "strong", 0, false);
		spread = spread.replaceAll("Â", "");
		spread = spread.replaceAll("&nbsp;", "");
		team.addGameSpreadOptionValue("0", "0");
		team = (Sports411TeamPackage)parseSpreadData(reformatValues(spread), 0, " ", null, team);

		LOGGER.info("Exiting getSpread()");
		return team;
	}

	/**
	 * 
	 * @param span
	 * @param team
	 * @return
	 */
	protected Sports411TeamPackage getTotal(Element span, Sports411TeamPackage team) {
		LOGGER.info("Entering getTotal()");
		LOGGER.debug("Element: " + span);

		// First get the input field information
		final Map<String, String> hashMap = parseInputField(span.select("input"), 0);
		team.setGameTotalInputId(hashMap.get("name"));
		team.setGameTotalInputName(hashMap.get("name"));
		team.setGameTotalInputValue(hashMap.get("value"));

		// o40½-110; Now parse the data
		String overUnder = span.attr("class");
		if (overUnder != null && overUnder.contains("over")) {
			overUnder = "o";
		} else {
			overUnder = "u";
		}
		String totalString = getHtmlFromElement(span, "strong", 0, false);
		totalString = overUnder + totalString;
		totalString = totalString.replaceAll("Â", "");
		totalString = totalString.replaceAll("&nbsp;", "");
		team = (Sports411TeamPackage)parseTotalData(reformatValues(totalString), 0, null, null, team);

		LOGGER.info("Exiting getTotal()");
		return team;
	}

	/**
	 * 
	 * @param td
	 * @param team
	 * @return
	 */
	protected Sports411TeamPackage getMoneyLine(Element td, Sports411TeamPackage team) {
		LOGGER.info("Entering getMoneyLine()");
		LOGGER.debug("Element: " + td);
		
		// First get the input field information
		final Map<String, String> hashMap = parseInputField(td.select("input"), 0);
		team.setGameMLInputId(hashMap.get("name"));
		team.setGameMLInputName(hashMap.get("name"));
		team.addGameMLInputValue("0", hashMap.get("value"));

		// Setup ml
		String ml = getHtmlFromElement(td, "strong", 0, false);
		team = (Sports411TeamPackage)parseMlData(reformatValues(ml), 0, team);

		LOGGER.info("Exiting getMoneyLine()");
		return team;
	}

	/**
	 * 
	 * @param span
	 * @param team
	 * @return
	 */
	protected Sports411EventPackage getMaxValues(Element span, Sports411EventPackage event) {
		LOGGER.info("Entering getMaxValues()");
		LOGGER.debug("span: " + span);

		// Setup max values
		String html = span.html();
		if (html != null && html.length() > 0) {
			int index = html.indexOf(" USD");
			if (index != -1) {
				html = html.substring(0, index);
				html = html.replaceAll("Correlated ", "");
				html = html.replaceAll("K", "000");
				html = html.replaceAll("k", "000");
				index = html.indexOf("-");
				LOGGER.debug("html: " + html);
				if (index != -1) {
					// 3k-3k-5k
					event.setSpreadMax(Integer.parseInt(html.substring(0, index)));
					html = html.substring(index + 1);
					LOGGER.debug("html: " + html);
					index = html.indexOf("-");
					if (index != -1) {
						event.setTotalMax(Integer.parseInt(html.substring(0, index)));
						event.setMlMax(Integer.parseInt(html.substring(index + 1)));
					}
				} else {
					final Integer maxAmount = Integer.parseInt(html);
					event.setSpreadMax(maxAmount);
					event.setTotalMax(maxAmount);
					event.setMlMax(maxAmount);
				}
			}
		}

		LOGGER.info("Exiting getMaxValues()");
		return event;
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
	protected Map<String, String> findMenu(Elements divs, Map<String, String> map, String[] type, String[] sport, String foundString, String menuString) {
		LOGGER.info("Entering findMenu()");
		LOGGER.info("divs: " + divs);

		for (int x = 0; (divs != null && x < divs.size()); x++) {
			final Element div = divs.get(x);
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

		String divData = getHtmlFromElement(div, select, 0, false);
		LOGGER.debug("divData: " + divData);

		// Check if we found div
		if (divData != null && divData.equals(type)) {
			foundDiv = true;
		} else if (type.startsWith("NFL - WEEK") && divData != null && divData.startsWith("NFL - WEEK")) { // HACK!!!
			foundDiv = true;
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
	protected Map<String, String> getMenuData(Element div, String element, Map<String, String> map) {
		LOGGER.info("Entering getMenuData()");
		LOGGER.debug("Div: " + div);
		LOGGER.debug("element: " + element);
		final Elements inputs = div.select(element);

		if (inputs != null && inputs.size() > 0) {
			final Element input = inputs.get(0);
			LOGGER.debug("input: " + input);

			if (input != null) {
				final String value = map.get(input.attr("name"));

				if (value != null && value.length() > 0) {
					map.put(input.attr("name"), (input.attr("value") + "," + value));
				} else {
					map.put(input.attr("name"), input.attr("value"));
				}
			}
		}

		LOGGER.info("Exiting getMenuData()");
		return map;
	}

	/**
	 * 
	 * @param doc
	 * @param teamPackage
	 */
	protected void setupSpreadWager(Document doc, Sports411TeamPackage teamPackage) {
		LOGGER.info("Entering setupSpreadWager()");

		final Map<String, String> hashMap = parseSelectField(doc.select(".wager_game_pts_ddl"));
		if (hashMap.size() > 0) {
			teamPackage.setGameSpreadSelectName(hashMap.get("name"));
		}

		final Elements options = doc.select(".wager_game_pts_ddl option");
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
					teamPackage = (Sports411TeamPackage)parseSpreadData(reformatValues(optionData), x, " ", null, teamPackage);
				}
			}
		}

		LOGGER.info("Exiting setupSpreadWager()");
	}

	/**
	 * 
	 * @param doc
	 * @param teamPackage
	 */
	protected void setupTotalWager(Document doc, Sports411TeamPackage teamPackage) {
		LOGGER.info("Entering setupTotalWager()");
		
		final Map<String, String> hashMap = parseSelectField(doc.select(".wager_game_pts_ddl"));
		if (hashMap.size() > 0) {
			teamPackage.setGameTotalSelectName(hashMap.get("name"));
		}

		final Elements options = doc.select(".wager_game_pts_ddl option");
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
					teamPackage = (Sports411TeamPackage)parseTotalData(reformatValues(optionData), x, " ", null, teamPackage);
				}
			}
		}

		LOGGER.info("Exiting setupTotalWager()");
	}
	
	/**
	 * 
	 * @param map
	 * @return
	 */
	protected Map<String, String> removeInputFields(Map<String, String> map) {
		// Check for a valid map
		final Map<String, String> deleteMap = new HashMap<String, String>();
		if (map != null && !map.isEmpty()) {
			final Set<Entry<String, String>> indexs = map.entrySet();
			if (indexs != null && !indexs.isEmpty()) {
				final Iterator<Entry<String, String>> itr = indexs.iterator();
				int counter = 0;
				while (itr != null && itr.hasNext()) {
					final Entry<String, String> values = itr.next();
					String key = values.getKey();
					String value = values.getValue();
					LOGGER.info("KEY: " + key);
					LOGGER.info("VALUE: " + value);

					if (key != null) {
						if (value != null && value.contains("Continue") || value.contains("View Odds")) {
							if (counter++ > 0) {
							} else {
								deleteMap.put(key, value);
							}
						} else if (value != null && value.contains("Refesh") || value.contains("Refresh") || value.contains("Clear") || value.contains("Print") || value.contains("Cancel") || value.contains("cancel")) {
						} else if (key != null && key.contains("Feedback1") || key.contains("Refresh") || key.contains("Continue")) {
						} else {
							deleteMap.put(key, value);
						}
					}
				}
			}
		}

		if (deleteMap != null && !deleteMap.isEmpty()) {
			final Set<Entry<String, String>> indexs = deleteMap.entrySet();
			if (indexs != null && !indexs.isEmpty()) {
				final Iterator<Entry<String, String>> itr = indexs.iterator();
				while (itr != null && itr.hasNext()) {
					final Entry<String, String> values = itr.next();
					String key = values.getKey();
					map.remove(key);
				}
			}
		}	

		return deleteMap;
	}
}