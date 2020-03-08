/**
 * 
 */
package com.ticketadvantage.services.dao.sites.pinnacle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.ticketadvantage.services.dao.sites.SiteEventPackage;
import com.ticketadvantage.services.dao.sites.SiteParser;
import com.ticketadvantage.services.dao.sites.SiteTeamPackage;
import com.ticketadvantage.services.dao.sites.sports411.Sports411TeamPackage;
import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;

/**
 * @author jmiller
 *
 */
public class PinnacleParser extends SiteParser {
	protected static final Logger LOGGER = Logger.getLogger(PinnacleParser.class);
	protected static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd yyyy hh:mm a z");

	/**
	 * Constructor
	 */
	public PinnacleParser() {
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
		LOGGER.info("Exiting parseIndex()");
		return null;
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
		LOGGER.info("Exiting parseLogin()");
		return null;
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
		LOGGER.error("xhtml: " + xhtml);
		LOGGER.info("type: " + java.util.Arrays.toString(type));
		LOGGER.info("sport: " + java.util.Arrays.toString(sport));
		Map<String, String> map = new HashMap<String, String>();

		// Put in type and sport
		map.put("type", java.util.Arrays.toString(type));
		map.put("sport", java.util.Arrays.toString(sport));

		// Parse to get the Document
		final Document doc = parseXhtml(xhtml);
		final Elements menus = doc.select(".wm1s li div a");
		for (int x = 0; x < menus.size(); x++) {
			final Element menu = menus.get(x);
			for (int y = 0; y < type.length; y++) {
				final String mhtml = menu.html();
				LOGGER.error("mhtml: " + mhtml);
				if (mhtml != null && type[y].equals(mhtml.trim())) {
					String href = menu.attr("href");
					// <a href="/Members/Lineoffering.asp?display=DynamicLines&amp;sportType=Basketball&amp;sportSubType=NCAA&amp;descr=1">NCAA</a>
					href = href.replace("amp;", "");
					href += "&maincontent&LangExt=egl";
					LOGGER.error("href: " + href);
					map.put("url", href);
				}
			}
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
	public <Sports411EventPackage> List<Sports411EventPackage> parseGames(String xhtml, String type, Map<String, String> inputFields) throws BatchException {
		LOGGER.info("Entering parseGames()");
		LOGGER.debug("xhtml: " + xhtml);
		LOGGER.debug("type: " + type);
		LOGGER.debug("InputFields: " + inputFields);
		List<?> events = null;
		
		// Parse to get the Document
		final Document doc = parseXhtml(xhtml);
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
	protected List<SiteEventPackage> getGameData(Elements elements) throws BatchException {
		LOGGER.info("Entering getGameData()");
		SiteEventPackage eventPackage = null;

		final List<SiteEventPackage> events = new ArrayList<SiteEventPackage>();

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
	protected int getTeamData(Elements elements, SiteTeamPackage team, boolean isFirst) {
		LOGGER.info("Entering getTeamData()");
		LOGGER.debug("Sports411TeamPackage: " + team);

		int size = 0;

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