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
public class Sports411Parser extends SiteParser {
	protected static final Logger LOGGER = Logger.getLogger(Sports411Parser.class);
	protected static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd yyyy hh:mm a z");

	/**
	 * Constructor
	 */
	public Sports411Parser() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
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
			if (action != null && action.equals(actionName)) {
				// Get form action field
				map.put("action", form.attr("action"));
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
		map.put("ctl00$Feedback1$ctl00$txtComments", "");
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
		Elements elements = doc.select("#printLines section table tbody tr");
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

		final String[] types = new String[] { "hidden", "text", "submit" };
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
		Elements selects = doc.select("td table tbody tr td select");
		LOGGER.debug("selects: " + selects);
		LOGGER.debug("selects.size(): " + selects.size());
		if (selects != null && selects.size() == 2) {
			parseSelectFieldByNum(selects, 1, map);
		} else if (selects != null && selects.size() == 1) {
			parseSelectFieldByNum(selects, 0, map);
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

		// Remove the input fields that shouldn't be sent
		map = removeInputFields(map);

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
		Elements spans = doc.select(".evenRow td span");
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

			for (int x = 0; x < elements.size(); x++) {
				// Loop through the elements and then check for the dates
				final Element element = elements.get(x);
				LOGGER.debug("Element: " + element);

				if (element != null) {
					String classInfo = element.attr("class");
					LOGGER.debug("ClassInfo: " + classInfo);

					if ((classInfo != null && classInfo.length() > 0) && 
						("banner".equalsIgnoreCase(classInfo))) {
						Elements date = element.select("td span");
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
						("oddRow".equals(classInfo) || "evenRow".equals(classInfo))) {
						String gTime = "";
						Elements left = element.select(".leftColumn");
						if (left != null && left.size() > 0) {
							Element tdTime = left.get(0);
							LOGGER.debug("tdTime: " + tdTime);
							Elements smalls = tdTime.select("small");
							if (smalls != null && smalls.size() > 0) {
								Element small = smalls.get(0);
								gTime = small.html();
								if (gTime != null) {
									gTime = gTime.trim();
									gTime = gTime.replaceAll("Â", "");
									gTime = gTime.replaceAll("&nbsp;", "");

								}
							}
						}
						Elements right = element.select(".rightColumn");
						if (right != null && right.size() > 0) {
							eventPackage = new Sports411EventPackage();
							Element tdGame = right.get(0);
							LOGGER.debug("tdGame: " + tdGame);
							Elements trs = tdGame.select("table tbody tr");
							for (int i = 0; (trs != null && i < trs.size()); i++) {
								Element tr = trs.get(i);
								if (i == 0) {
									team1 = new Sports411TeamPackage();
									getTeamData(tr.select("td"), team1, true);
									LOGGER.debug("team1: " + team1);
									eventPackage.setId(team1.getId());
								} else {
									team2 = new Sports411TeamPackage();
									getTeamData(tr.select("td"), team2, false);
									LOGGER.debug("team2: " + team2);
									LOGGER.error("gameDate: " + gameDate);
									LOGGER.error("gTime: " + gTime);
									Date eventDate = null;
									try {
										eventDate = setupDate(DATE_FORMAT, gameDate, gTime);
										LOGGER.debug("eventDate: " + eventDate);
									} catch (Throwable t) {
										LOGGER.error(t);
										Calendar cal = Calendar.getInstance();
								        cal.setTime(new Date());
								        cal.add(Calendar.DATE, 1); //minus number would decrement the days
								        eventDate = cal.getTime();
									}
									team1.setEventdatetime(eventDate);
									team2.setEventdatetime(eventDate);
									String cDate = "";
									final Calendar now = Calendar.getInstance();
									int offset = now.get(Calendar.DST_OFFSET);
									cDate = gameDate + " " + String.valueOf(now.get(Calendar.YEAR)) + " " + gTime;
									cDate += " " + timeZoneLookup(timezone, offset);
									team1.settDate(cDate);
									team2.settDate(cDate);
									LOGGER.debug("setEventdatetime: " + team1.getDateofevent());
									eventPackage.setDateofevent(gameDate);
									eventPackage.setTimeofevent(gTime);
									eventPackage.setEventdatetime(eventDate);
									eventPackage.setDateofevent(cDate);
									eventPackage.setSiteteamone(team1);
									eventPackage.setSiteteamtwo(team2);
									eventPackage.setTeamone(team1);
									eventPackage.setTeamtwo(team2);
									eventPackage.setSpreadMax(team1.getSpreadMax());
									eventPackage.setTotalMax(team1.getTotalMax());
									eventPackage.setMlMax(team1.getMlMax());
									LOGGER.debug("EventPackage: " + eventPackage);
									events.add(eventPackage);
								}
							}
						}
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
	 * @param elements
	 * @param team
	 * @param isFirst
	 * @return
	 */
	protected int getTeamData(Elements elements, Sports411TeamPackage team, boolean isFirst) {
		LOGGER.info("Entering getTeamData()");
		LOGGER.debug("Sports411TeamPackage: " + team);
		int size = 0;

		for (int x = 0; (elements != null && x < elements.size()); x++) {
			final Element td = elements.get(x);

			switch (x) {
			case 0:
				if (td.html().contains("gPitch")) {
					String htmlData = td.html();
					LOGGER.debug("htmlData: " + htmlData);
					if (htmlData.contains("labelT")) {
						int spanIndex = htmlData.indexOf("</span>");
						if (spanIndex != -1) {
							htmlData = htmlData.substring(spanIndex + 7);
							LOGGER.debug("htmlData: " + htmlData);
							spanIndex = htmlData.indexOf("<span");
							if (spanIndex != -1) {
								htmlData = htmlData.substring(0, spanIndex);
								LOGGER.debug("htmlData: " + htmlData);
								htmlData = htmlData.trim();
								htmlData = htmlData.replaceAll("Â", "");
								htmlData = htmlData.replaceAll("&nbsp;", "");
								team.setTeam(htmlData);
							}
						}
					} else {
						LOGGER.debug("htmlData: " + htmlData);
						int spanIndex = htmlData.indexOf("<span");
						if (spanIndex != -1) {
							htmlData = htmlData.substring(0, spanIndex);
							LOGGER.debug("htmlData: " + htmlData);
							htmlData = htmlData.trim();
							htmlData = htmlData.replaceAll("Â", "");
							htmlData = htmlData.replaceAll("&nbsp;", "");
							team.setTeam(htmlData);
						}						
					}
				} else {
					Elements spans = td.select("span");
					if (spans != null && spans.size() > 0) {
						team = getEventId(spans.get(0), team);
						team = getTeam(spans.get(1), team);
					}
				}
				break;
			case 1:
				Elements labels = td.select("label");
				if (labels != null && labels.size() > 0) {
					team = getSpread(labels.get(0), team);
				}
				break;
			case 2:
				labels = td.select("label");
				if (labels != null && labels.size() > 0) {
					team = getTotal(labels.get(0), team);
				}
				break;
			case 3:
				labels = td.select("label");
				if (labels != null && labels.size() > 0) {
					team = getMoneyLine(labels.get(0), team);
				}
				break;
			case 4:
				if (isFirst) {
					labels = td.select("div span");
					if (labels != null && labels.size() > 0) {
						team = getMaxValues(labels.get(0), team);
					}
				}
				break;
			default:				
				break;
			}
		}

		LOGGER.info("Exiting getTeamData()");
		return size;
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
	protected Sports411TeamPackage getSpread(Element td, Sports411TeamPackage team) {
		LOGGER.info("Entering getSpread()");
		LOGGER.debug("Element: " + td);

		// First get the input field information
		final Map<String, String> hashMap = parseInputField(td.select("input"), 0);
		team.setGameSpreadInputId(hashMap.get("id"));
		team.setGameSpreadInputName(hashMap.get("name"));
		team.setGameSpreadInputValue(hashMap.get("value"));

		// Setup spread
		String spread = getHtmlFromElement(td, "span", 0, false);
		spread = spread.replaceAll("Â", "");
		spread = spread.replaceAll("&nbsp;", "");
		team.addGameSpreadOptionValue("0", "0");
		team = (Sports411TeamPackage)parseSpreadData(reformatValues(spread), 0, " ", null, team);

		LOGGER.info("Exiting getSpread()");
		return team;
	}

	/**
	 * 
	 * @param td
	 * @param team
	 * @return
	 */
	protected Sports411TeamPackage getTotal(Element td, Sports411TeamPackage team) {
		LOGGER.info("Entering getTotal()");
		LOGGER.debug("Element: " + td);

		// First get the input field information
		final Map<String, String> hashMap = parseInputField(td.select("input"), 0);
		team.setGameTotalInputId(hashMap.get("id"));
		team.setGameTotalInputName(hashMap.get("name"));
		team.setGameTotalInputValue(hashMap.get("value"));

		// o40½ -110; Now parse the data
		Elements spans = td.select("span");
		String totalString = "";
		if (spans != null && spans.size() > 0) {
			Element span = spans.get(0);
			String overUnder = span.attr("class");
			if (overUnder != null && "over".equals(overUnder)) {
				overUnder = "o";
			} else {
				overUnder = "u";
			}
			totalString = span.html();
			if (totalString != null && totalString.length() > 0) {
				int index = totalString.indexOf("::before");
				if (index != -1) {
					totalString = totalString.substring(index + "::before".length());
					totalString = reformatValues(totalString);
				}
			}
			totalString = overUnder + totalString;
		}
		totalString = totalString.replaceAll("Â", "");
		totalString = totalString.replaceAll("&nbsp;", "");
		team = (Sports411TeamPackage)parseTotalData(reformatValues(totalString), 0, " ", null, team);

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
		team.setGameMLInputId(hashMap.get("id"));
		team.setGameMLInputName(hashMap.get("name"));
		team.addGameMLInputValue("0", hashMap.get("value"));

		// Setup ml
		String ml = getHtmlFromElement(td, "span", 0, false);
		team.addGameSpreadOptionValue("0", "0");
		ml = ml.replaceAll("Â", "");
		ml = ml.replaceAll("&nbsp;", "");		
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
	protected Sports411TeamPackage getMaxValues(Element span, Sports411TeamPackage team) {
		LOGGER.info("Entering getMaxValues()");
		LOGGER.debug("span: " + span);

		// Setup max values
		String html = span.html();
		if (html != null && html.length() > 0) {
			int index = html.indexOf(" USD");
			if (index != -1) {
				html = html.substring(0, index);
				html = html.replaceAll("K", "000");
				html = html.replaceAll("k", "000");
				index = html.indexOf("-");
				if (index != -1) {
					// 3k-3k-5k
					team.setSpreadMax(Integer.parseInt(html.substring(0, index)));
					html = html.substring(index + 1);
					index = html.indexOf("-");
					if (index != -1) {
						team.setTotalMax(Integer.parseInt(html.substring(0, index)));
						team.setMlMax(Integer.parseInt(html.substring(index + 1)));
					}
				} else {
					final Integer maxAmount = Integer.parseInt(html);
					team.setSpreadMax(maxAmount);
					team.setTotalMax(maxAmount);
					team.setMlMax(maxAmount);
				}
			}
		}

		LOGGER.info("Exiting getMaxValues()");
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
				map.put(input.attr("name"), input.attr("value"));
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