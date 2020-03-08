/**
 * 
 */
package com.ticketadvantage.services.dao.sites.tdsports;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;

/**
 * @author jmiller
 *
 */
public class PlayRSBTDSportsParser extends TDSportsParser {
	private static final Logger LOGGER = Logger.getLogger(PlayRSBTDSportsParser.class);
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd yyyy hh:mm a z");
	private boolean isMlb;

	/**
	 * Constructor
	 */
	public PlayRSBTDSportsParser() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.tdsports.TDSportsParser#parseMenu(java.lang.String, java.lang.String[], java.lang.String[])
	 */
	@Override
	public Map<String, String> parseMenu(String xhtml, String[] type, String[] sport) throws BatchException {
		LOGGER.info("Entering parseMenu()");
		LOGGER.info("type: " + java.util.Arrays.toString(type));
		LOGGER.info("sport: " + java.util.Arrays.toString(sport));
		Map<String, String> map = new HashMap<String, String>();

		for (String sportName : sport) {
			if (sportName.contains("BASEBALL") || sportName.contains("MLB")) {
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
		map = findMenu(doc.select(".container div div ul li div"), map, type, sport, "a font", "input");

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
		Elements elements = doc.select(".linesTable tbody tr");
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
		final String ticketInfo = " USD</TD><TD class=\"text-center\">";

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
				index = nxhtml.indexOf("</TD></TR>");
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
			int t = 0;
			for (int x = 0; x < elements.size(); x++) {
				// Loop through the elements and then check for the dates
				final Element element = elements.get(x);
				LOGGER.debug("Element: " + element);

				if (element != null) {
					final Elements tds = element.select("td");
					if (tds != null && tds.size() == 6) {
						if (t++ == 0) {
							eventPackage = new TDSportsEventPackage();
							team1 = new TDSportsTeamPackage();
							getTeamData(element.select("td"), team1);
							eventPackage.setId(team1.getId());
						} else {
							team2 = new TDSportsTeamPackage();
							int size = getTeamData(element.select("td"), team2);
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
		LOGGER.debug("TDSportsTeamPackage: " + team);
		int size = 0;

		if (isMlb) {
			parseMlb(elements, team);
		} else {
			if (elements != null && elements.size() == 6) {
				LOGGER.info("elements.size(): " + elements.size());
				size = 6;
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
							team = getTotal(td, team);
							break;
						case 5:
							team = getMoneyLine(td, team);
							break;
					}
				}
			} else if (elements != null) {
				LOGGER.info("elements.size(): " + elements.size());
			}
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

		if (elements != null && elements.size() == 6) {
			LOGGER.info("elements.size(): " + elements.size());
			size = 6;
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
						team = getMoneyLine(td, team);
						break;
					case 4:
						team = getTotal(td, team);
						break;
					case 5:
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
	 * @see com.ticketadvantage.services.dao.sites.tdsports.TDSportsParser#getSpread(org.jsoup.nodes.Element, com.ticketadvantage.services.dao.sites.tdsports.TDSportsTeamPackage)
	 */
	@Override
	protected TDSportsTeamPackage getSpread(Element td, TDSportsTeamPackage team) {
		LOGGER.info("Entering getSpread()");
		LOGGER.debug("Element: " + td);

		// First get the input field information
		final Map<String, String> hashMap = parseInputField(td.select("input"), 0);
		team.setGameSpreadInputId(hashMap.get("id"));
		team.setGameSpreadInputName(hashMap.get("name"));
		team.setGameSpreadInputValue(hashMap.get("value"));

		String spread = getHtmlFromElement(td, "font", 0, false);
		if (spread == null || spread.length() == 0) {
			spread = getHtmlFromElement(td, "div", 0, true);
			
			// -2½+200; Now parse the data
			spread = parseHtmlAfter(reformatValues(spread), ">");
		}

		// Setup spread
		team.addGameSpreadOptionValue("0", "0");
		team = (TDSportsTeamPackage)parseSpreadData(reformatValues(spread), 0, " ", null, team);

		LOGGER.info("Exiting getSpread()");
		return team;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.tdsports.TDSportsParser#getTotal(org.jsoup.nodes.Element, com.ticketadvantage.services.dao.sites.tdsports.TDSportsTeamPackage)
	 */
	@Override
	protected TDSportsTeamPackage getTotal(Element td, TDSportsTeamPackage team) {
		LOGGER.info("Entering getTotal()");
		LOGGER.debug("Element: " + td);

		// First get the input field information
		final Map<String, String> hashMap = parseInputField(td.select("input"), 0);
		team.setGameTotalInputId(hashMap.get("id"));
		team.setGameTotalInputName(hashMap.get("name"));
		team.setGameTotalInputValue(hashMap.get("value"));

		// o40½-110; Now parse the data
		String total = getHtmlFromElement(td, "font", 0, false);
		if (total == null || total.length() == 0) {
			total = getHtmlFromElement(td, "div", 0, true);

			// -2½+200; Now parse the data
			total = parseHtmlAfter(reformatValues(total), ">");
		}

		// Parse Total Data
		team.addGameTotalOptionValue("0", "0");
		team = (TDSportsTeamPackage)parseTotalData(reformatValues(total), 0, " ", null, team);

		LOGGER.info("Exiting getTotal()");
		return team;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.tdsports.TDSportsParser#getMoneyLine(org.jsoup.nodes.Element, com.ticketadvantage.services.dao.sites.tdsports.TDSportsTeamPackage)
	 */
	@Override
	protected TDSportsTeamPackage getMoneyLine(Element td, TDSportsTeamPackage team) {
		LOGGER.info("Entering getMoneyLine()");
		LOGGER.debug("Element: " + td);
		
		// First get the input field information
		final Map<String, String> hashMap = parseInputField(td.select("input"), 0);
		team.setGameMLInputId(hashMap.get("id"));
		team.setGameMLInputName(hashMap.get("name"));
		team.addGameMLInputValue("0", hashMap.get("value"));

		// -110; Now parse the data
		String ml = getHtmlFromElement(td, "font", 0, false);
		if (ml == null || ml.length() == 0) {
			ml = getHtmlFromElement(td, "div", 0, true);
				
			// -2½+200; Now parse the data
			ml = parseHtmlAfter(reformatValues(ml), ">");				
		}

		// Parse for Money Line
		team = (TDSportsTeamPackage)parseMlData(reformatValues(ml), 0, team);

		LOGGER.info("Exiting getMoneyLine()");
		return team;
	}
}