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

import com.ticketadvantage.services.dao.sites.tdsports.TDSportsEventPackage;
import com.ticketadvantage.services.dao.sites.tdsports.TDSportsParser;
import com.ticketadvantage.services.dao.sites.tdsports.TDSportsTeamPackage;
import com.ticketadvantage.services.errorhandling.BatchException;

/**
 * @author jmiller
 *
 */
public class BetallstarTDSportsParser extends TDSportsParser {
	private static final Logger LOGGER = Logger.getLogger(BetallstarTDSportsParser.class);
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd yyyy hh:mm a z");
	private boolean isMlb;

	/**
	 * Constructor
	 */
	public BetallstarTDSportsParser() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
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
			if (sportName.contains("MLB") || sportName.contains("mlb") || sportName.contains("BASEBALL")) {
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
		map = findMenu(doc.select("#content center table tbody tr td"), map, type, sport, "td span", "td span input");
		LOGGER.debug("MAP: " + map);

		// Remove the input fields that shouldn't be sent
		map.remove("ctl00$WagerContent$btnClear");
		map.remove("ctl00$WagerContent$btnClear_top");
		map.remove("ctl00$WagerContent$btn_Continue_top");
		map.remove("ctl00$WagerContent$btn_Continuemod");

		LOGGER.info("Exiting parseMenu()");
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

		String divData = getHtmlFromElement(div, select, 0, false);
		LOGGER.debug("divData: " + divData);

		LOGGER.debug("type: " + type);
		
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
	 * 
	 */
	@Override
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
		Element originalForm = null;
		final Element form = originalForm = doc.getElementById("aspnetForm");
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

		// abcweb.ag
		Elements elements = doc.select(".ScheduleLeagueGames tbody tr");
		if (elements != null && elements.size() > 0) {
			events = getGameData(elements);
		}

		// whq.ag
		elements = doc.select("#PageContent div center div div table tbody tr");
		if (elements != null && elements.size() > 0) {
			events = getGameData(elements);
		}

		// Get form action field
		tempFields.put("action", originalForm.attr("action"));

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
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#getFootballData(org.jsoup.select.Elements)
	 */
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
						Elements tds = element.select("td");
						if (tds.size() > 5) {
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
									if (isMlb) {
										eventDate = setupDate(DATE_FORMAT, team1.gettDate(), team2.gettDate());
									} else {
										eventDate = setupDate(DATE_FORMAT, team1.gettDate(), team1.gettTime());
									}
								} else {
									if (isMlb) {
										eventDate = setupDate(DATE_FORMAT, team1.gettDate(), team2.gettDate());
									} else {
										eventDate = setupDate(DATE_FORMAT, team1.gettDate(), team2.gettDate());
									}
								}
								team1.setEventdatetime(eventDate);
								team2.setEventdatetime(eventDate);
								String dateOfEvent = "";
										
								if (size == 7 || size == 5) {
									dateOfEvent = team1.gettDate() + " " + team2.gettDate();
								} else {
									dateOfEvent = team1.gettDate() + " " + team2.gettDate();
								}
	
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

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.tdsports.TDSportsParser#getTeamData(org.jsoup.select.Elements, com.ticketadvantage.services.dao.sites.tdsports.TDSportsTeamPackage)
	 */
	@Override
	protected int getTeamData(Elements elements, TDSportsTeamPackage team) {
		LOGGER.info("Entering getTeamData()");
		LOGGER.debug("TDSportsTeamPackage: " + team);
		int size = 0;

		LOGGER.info("elements.size(): " + elements.size());
		LOGGER.debug("Elements: " + elements);
		LOGGER.debug("isMlb: " + isMlb);
		if (isMlb) {
			parseMlb(elements, team);
			LOGGER.debug("team: " + team);
		} else {
			// Non-MLB
			if (elements != null && elements.size() == 8) {
				LOGGER.info("elements.size(): " + elements.size());
				size = 8;
				for (int x = 0;(elements != null && x < elements.size()); x++) {
					final Element td = elements.get(x);
		
					switch (x) {
						case 1:
							team = getSpecialDate(td, team);
							break;
						case 2:
							team = getEventId(td, team);
							break;
						case 3:
							team = getTeam(td, team);
							break;
						case 5:
							team = getSpread(td, team);
							break;
						case 6:
							team = getTotal(td, team);
							break;
						case 7:
							team = getMoneyLine(td, team);
							break;
						case 0:
						case 4:
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
							team = getSpecialDate(td, team);
							break;
						case 1:
							team = getEventId(td, team);
							break;
						case 2:
							team = getTeam(td, team);
							break;
						case 4:
							team = getSpread(td, team);
							break;
						case 5:
							team = getTotal(td, team);
							break;
						case 6:
							team = getMoneyLine(td, team);
							break;
						case 3:
						default:
							break;
					}
				}
			} else if (elements != null && elements.size() == 6) {
				LOGGER.info("elements.size(): " + elements.size());
				size = 6;

				for (int x = 0;(elements != null && x < elements.size()); x++) {
					final Element td = elements.get(x);
		
					switch (x) {
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
						case 0:
						default:
							break;
					}
				}
			} else if (elements != null && elements.size() == 9) {
				LOGGER.info("elements.size(): " + elements.size());
				size = 9;

				for (int x = 0;(elements != null && x < elements.size()); x++) {
					final Element td = elements.get(x);
		
					switch (x) {
						case 1:
							team = getDate(td, team);
							break;
						case 2:
							team = getEventId(td, team);
							break;
						case 3:
							team = getTeam(td, team);
							break;
						case 6:
							team = getSpread(td, team);
							break;
						case 7:
							team = getTotal(td, team);
							break;
						case 8:
							team = getMoneyLine(td, team);
							break;
						default:
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

		size = 7;
		int x = 0;
		for (int y = 0; (elements != null && y < elements.size()); y++) {
			final Element td = elements.get(y);

			switch (x) {
			case 1:
				team = getDate(td, team);
				LOGGER.debug("Team1: " + team);
				break;
			case 2:
				team = getEventId(td, team);
				LOGGER.debug("Team2: " + team);
				break;
			case 3:
				team = getTeam(td, team);
				LOGGER.debug("Team3: " + team);
				break;
			case 5:
				final String pitcher = td.html().trim();
				team.setPitcher(pitcher);
				LOGGER.debug("Team4: " + team);
				break;
			case 6:
				team = getMoneyLine(td, team);
				LOGGER.debug("Team5: " + team);
				break;
			case 7:
				team = getTotal(td, team);
				LOGGER.debug("Team6: " + team);
				break;
			case 8:
				team = getSpread(td, team);
				LOGGER.debug("Team7: " + team);
				break;
			case 0:
			case 4:
			default:
				break;
			}
			x++;
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
		team.settDate(td.html().trim());

		LOGGER.info("Exiting getDate()");
		return team;
	}

	/**
	 * 
	 * @param td
	 * @param team
	 * @return
	 */
	protected TDSportsTeamPackage getSpecialDate(Element td, TDSportsTeamPackage team) {
		LOGGER.info("Entering getSpecialDate()");
		LOGGER.debug("Element: " + td);

		final String dateNTime = td.html().trim();
		int index = dateNTime.indexOf("<br>");
		if (index != -1) {
			team.settDate(dateNTime.substring(0, index).trim());
			team.settTime(dateNTime.substring(index + 4).trim());
		}

		LOGGER.info("Exiting getSpecialDate()");
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

		final String eventId = td.html().trim();
		team.setEventid(eventId);
		team.setId(Integer.parseInt(eventId));

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
		String teamName = getHtmlFromElement(td, "table tbody tr td", 0, true);
		team.setTeam(teamName);

		LOGGER.info("Exiting getTeam()");
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

		// -1.5+140
		String spread = null;
		final Elements fonts = td.select("div font");

		if (fonts != null && fonts.size() > 0) {
			spread = fonts.get(0).html().trim();
		} else {
			final Elements divs = td.select("div");

			if (divs != null && divs.size() > 0) {
				spread = divs.get(0).html().trim();
			}
		}

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
		String total = null;
		final Elements fonts = td.select("div font");

		if (fonts != null && fonts.size() > 0) {
			total = fonts.get(0).html().trim();
		} else {
			final Elements divs = td.select("div");

			if (divs != null && divs.size() > 0) {
				total = divs.get(0).html().trim();
			}
		}

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
		String ml = null;
		final Elements fonts = td.select("div font");

		if (fonts != null && fonts.size() > 0) {
			ml = fonts.get(0).html().trim();
		} else {
			final Elements divs = td.select("div");

			if (divs != null && divs.size() > 0) {
				ml = divs.get(0).html().trim();
			}
		}

		// Parse for Money Line
		team = (TDSportsTeamPackage)parseMlData(reformatValues(ml), 0, team);

		LOGGER.info("Exiting getMoneyLine()");
		return team;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.tdsports.TDSportsParser#removeInputFields(java.util.Map)
	 */
	@Override
	protected Map<String, String> removeInputFields(Map<String, String> map) {
		LOGGER.info("Entering removeInputFields()");

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

					if (key != null && key.equals("action")) {
						if (value != null && (value.contains("Schedule") || value.contains("Continue") || value.contains("View Odds"))) {
						} else {
							deleteMap.put(key, value);
						}
					} else if (value != null && value.contains("Refresh")) {
						deleteMap.put(key, value);
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

		LOGGER.info("Exiting removeInputFields()");
		return map;
	}
}