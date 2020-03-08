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

import com.ticketadvantage.services.errorhandling.BatchException;

/**
 * @author jmiller
 *
 */
public class PlayTheDogTDSportsParser extends TDSportsParser {
	private static final Logger LOGGER = Logger.getLogger(PlayTheDogTDSportsParser.class);
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd yyyy hh:mm a z");

	/**
	 * Constructor
	 */
	public PlayTheDogTDSportsParser() {
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
		map = findMenu(doc.select("#content center div div"), map, type, sport, "div b", "div input");

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

		// playthedog.net
		Elements elements = doc.select(".ScheduleGameRow div");
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
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#getFootballData(org.jsoup.select.Elements)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected List<TDSportsEventPackage> getGameData(Elements elements) throws BatchException {
		LOGGER.info("Entering getGameData()");
		TDSportsEventPackage eventPackage = null;
		final List<TDSportsEventPackage> events = new ArrayList<TDSportsEventPackage>();

		if (elements != null) {
			for (int x = 0;(elements != null && x < elements.size()); x++) {
				final Element div = elements.get(x);
				LOGGER.debug("div: " + div);
				Elements divs = div.select("div");
				TDSportsTeamPackage team1 = null;
				TDSportsTeamPackage team2 = null;
				LOGGER.info("divs.size(): " + divs.size());

				if (divs != null && divs.size() == 7) {
					LOGGER.info("divs.size(): " + divs.size());
					for (int i = 0;(divs != null && i < divs.size()); i++) {
						final Element row = divs.get(i);
	
						LOGGER.debug("i: " + i);
						switch (i) {
							case 0:
								eventPackage = new TDSportsEventPackage();
								team1 = new TDSportsTeamPackage();
								team2 = new TDSportsTeamPackage();
								break;
							case 1:
								getDatesForPlayTheDog(row, team1, team2);
								break;
							case 2:
								getEventIdForPlayTheDog(row, team1, team2);
								break;
							case 3:
								getTeamForPlayTheDog(row, team1, team2);
								break;
							case 4:
								getSpreadForPlayTheDog(row, team1, team2);
								break;
							case 5:
								getTotalForPlayTheDog(row, team1, team2);
								break;
							case 6:
								getMlForPlayTheDog(row, team1, team2, eventPackage, events);
								eventPackage.setId(team1.getId());
								Date eventDate = null;
								LOGGER.debug("Team1: " + team1);
								LOGGER.debug("Team2: " + team2);
								eventDate = setupDate(DATE_FORMAT, team1.gettDate(), team2.gettDate());
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
								break;
						}
					}
				} else if (divs != null && divs.size() == 8) {
					LOGGER.info("divs.size(): " + divs.size());
					for (int i = 0;(divs != null && i < divs.size()); i++) {
						final Element row = divs.get(i);
						LOGGER.debug("i: " + i);
						switch (i) {
							case 0:
							case 1:
								eventPackage = new TDSportsEventPackage();
								team1 = new TDSportsTeamPackage();
								team2 = new TDSportsTeamPackage();
								break;
							case 2:
								getDatesForPlayTheDog(row, team1, team2);
								break;
							case 3:
								getEventIdForPlayTheDog(row, team1, team2);
								break;
							case 4:
								getTeamForPlayTheDog(row, team1, team2);
								break;
							case 5:
								getSpreadForPlayTheDog(row, team1, team2);
								break;
							case 6:
								getTotalForPlayTheDog(row, team1, team2);
								break;
							case 7:
								getMlForPlayTheDog(row, team1, team2, eventPackage, events);
								eventPackage.setId(team1.getId());
								Date eventDate = null;
								LOGGER.debug("Team1: " + team1);
								LOGGER.debug("Team2: " + team2);
								eventDate = setupDate(DATE_FORMAT, team1.gettDate(), team2.gettDate());
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
								break;
						}
					}
				} else if (divs != null && divs.size() == 9) {
					LOGGER.info("divs.size(): " + divs.size());
					for (int i = 0;(divs != null && i < divs.size()); i++) {
						final Element row = divs.get(i);
	
						LOGGER.debug("i: " + i);
						switch (i) {
							case 0:
							case 1:
							case 2:
								eventPackage = new TDSportsEventPackage();
								team1 = new TDSportsTeamPackage();
								team2 = new TDSportsTeamPackage();
								break;
							case 3:
								getDatesForPlayTheDog(row, team1, team2);
								break;
							case 4:
								getEventIdForPlayTheDog(row, team1, team2);
								break;
							case 5:
								getTeamForPlayTheDog(row, team1, team2);
								break;
							case 6:
								getSpreadForPlayTheDog(row, team1, team2);
								break;
							case 7:
								getTotalForPlayTheDog(row, team1, team2);
								break;
							case 8:
								getMlForPlayTheDog(row, team1, team2, eventPackage, events);
								eventPackage.setId(team1.getId());
								Date eventDate = null;
								LOGGER.debug("Team1: " + team1);
								LOGGER.debug("Team2: " + team2);
								eventDate = setupDate(DATE_FORMAT, team1.gettDate(), team2.gettDate());
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
								break;
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
	 * @param row
	 * @param team1
	 * @param team2
	 */
	private void getDatesForPlayTheDog(Element row, TDSportsTeamPackage team1, TDSportsTeamPackage team2) {
		LOGGER.info("Entering getDatesForPlayTheDog()");

		String html = row.html();
		LOGGER.debug("HTML: " + html);
		if (html != null) {
			int index = html.indexOf("<br>");
			if (index != -1) {
				String dt1 = html.substring(0, index);
				String dt2 = html.substring(index + "<br>".length());
				team1.settDate(dt1.trim());
				team2.settDate(dt2.trim());
			}
		}

		LOGGER.info("Exiting getDatesForPlayTheDog()");
	}

	/**
	 * 
	 * @param row
	 * @param team1
	 * @param team2
	 */
	private void getEventIdForPlayTheDog(Element row, TDSportsTeamPackage team1, TDSportsTeamPackage team2) {
		LOGGER.info("Entering getEventIdForPlayTheDog()");

		String html = row.html();
		LOGGER.debug("HTML: " + html);
		if (html != null) {
			int index = html.indexOf("<br>");
			if (index != -1) {
				String dt1 = html.substring(0, index);
				String dt2 = html.substring(index + "<br>".length());
				team1.setEventid(dt1.trim());
				team1.setId(Integer.parseInt(dt1.trim()));
				team2.setEventid(dt2.trim());
				team2.setId(Integer.parseInt(dt2.trim()));
			}
		}
		
		LOGGER.info("Exiting getEventIdForPlayTheDog()");
	}

	/**
	 * 
	 * @param row
	 * @param team1
	 * @param team2
	 */
	private void getTeamForPlayTheDog(Element row, TDSportsTeamPackage team1, TDSportsTeamPackage team2) {
		LOGGER.info("Entering getTeamForPlayTheDog()");

		String html = row.html();
		LOGGER.debug("HTML: " + html);
		if (html != null) {
			int index = html.indexOf("<br>");
			if (index != -1) {
				String dt1 = html.substring(0, index);
				String dt2 = html.substring(index + "<br>".length());
				team1.setTeam(dt1.trim());
				team2.setTeam(dt2.trim());
			}
		}
		
		LOGGER.info("Exiting getTeamForPlayTheDog()");
	}

	/**
	 * 
	 * @param row
	 * @param team1
	 * @param team2
	 */
	private void getSpreadForPlayTheDog(Element row, TDSportsTeamPackage team1, TDSportsTeamPackage team2) {
		LOGGER.info("Entering getSpreadForPlayTheDog()");

		// First get the input field information
		Map<String, String> hashMap = parseInputField(row.select("input"), 0);
		team1.setGameSpreadInputId(hashMap.get("id"));
		team1.setGameSpreadInputName(hashMap.get("name"));
		team1.setGameSpreadInputValue(hashMap.get("value"));
	
		// First get the input field information
		hashMap = parseInputField(row.select("input"), 1);
		team2.setGameSpreadInputId(hashMap.get("id"));
		team2.setGameSpreadInputName(hashMap.get("name"));
		team2.setGameSpreadInputValue(hashMap.get("value"));
	
		String html = row.html();
		LOGGER.debug("HTML: " + html);
		if (html != null) {
			int index = html.indexOf("<br>");
			if (index != -1) {
				String dt1 = html.substring(0, index);
				String dt2 = html.substring(index + "<br>".length());
	
				// -2½+200; Now parse the data
				String spread = parseHtmlBefore(reformatValues(dt1.trim()), "<input");
				team1.addGameSpreadOptionValue("0", "0");
				team1 = (TDSportsTeamPackage)parseSpreadData(reformatValues(spread), 0, " ", null, team1);
	
				spread = parseHtmlBefore(reformatValues(dt2.trim()), "<input");
				team2.addGameSpreadOptionValue("0", "0");
				team2 = (TDSportsTeamPackage)parseSpreadData(reformatValues(spread), 0, " ", null, team2);
			}
		}
		
		LOGGER.info("Exiting getSpreadForPlayTheDog()");
	}

	/**
	 * 
	 * @param row
	 * @param team1
	 * @param team2
	 */
	private void getTotalForPlayTheDog(Element row, TDSportsTeamPackage team1, TDSportsTeamPackage team2) {
		LOGGER.info("Entering getTotalForPlayTheDog()");

		// First get the input field information
		Map<String, String> hashMap = parseInputField(row.select("input"), 0);
		team1.setGameTotalInputId(hashMap.get("id"));
		team1.setGameTotalInputName(hashMap.get("name"));
		team1.setGameTotalInputValue(hashMap.get("value"));
	
		hashMap = parseInputField(row.select("input"), 1);
		team2.setGameTotalInputId(hashMap.get("id"));
		team2.setGameTotalInputName(hashMap.get("name"));
		team2.setGameTotalInputValue(hashMap.get("value"));
	
		String html = row.html();
		LOGGER.debug("HTML: " + html);
		if (html != null) {
			int index = html.indexOf("<br>");
			if (index != -1) {
				String dt1 = html.substring(0, index);
				String dt2 = html.substring(index + "<br>".length());
	
				// o40½-110; Now parse the data
				String total = parseHtmlBefore(reformatValues(dt1.trim()), "<input");
				team1.addGameSpreadOptionValue("0", "0");
				team1 = (TDSportsTeamPackage)parseTotalData(reformatValues(total), 0, " ", null, team1);
	
				total = parseHtmlBefore(reformatValues(dt2.trim()), "<input");
				team2.addGameSpreadOptionValue("0", "0");
				team2 = (TDSportsTeamPackage)parseTotalData(reformatValues(total), 0, " ", null, team2);
			}
		}
		
		LOGGER.info("Exiting getTotalForPlayTheDog()");
	}

	/**
	 * 
	 * @param row
	 * @param team1
	 * @param team2
	 * @param eventPackage
	 * @param events
	 * @throws BatchException
	 */
	private void getMlForPlayTheDog(Element row, TDSportsTeamPackage team1, TDSportsTeamPackage team2, TDSportsEventPackage eventPackage, List<TDSportsEventPackage> events) throws BatchException {
		LOGGER.info("Entering getMlForPlayTheDog()");

		// First get the input field information
		Map<String, String> hashMap = parseInputField(row.select("input"), 0);
		team1.setGameMLInputId(hashMap.get("id"));
		team1.setGameMLInputName(hashMap.get("name"));
		team1.addGameMLInputValue("0", hashMap.get("value"));
	
		hashMap = parseInputField(row.select("input"), 1);
		team2.setGameMLInputId(hashMap.get("id"));
		team2.setGameMLInputName(hashMap.get("name"));
		team2.addGameMLInputValue("0", hashMap.get("value"));
	
		String html = row.html();
		LOGGER.debug("HTML: " + html);
		if (html != null) {
			int index = html.indexOf("<br>");
			if (index != -1) {
				String dt1 = html.substring(0, index);
				String dt2 = html.substring(index + "<br>".length());
	
				// -110; Now parse the data
				String ml = parseHtmlBefore(reformatValues(dt1.trim()), "<input");
				team1.addGameSpreadOptionValue("0", "0");
				team1 = (TDSportsTeamPackage)parseMlData(reformatValues(ml), 0, team1);
	
				ml = parseHtmlBefore(reformatValues(dt2.trim()), "<input");
				team2.addGameSpreadOptionValue("0", "0");
				team2 = (TDSportsTeamPackage)parseMlData(reformatValues(ml), 0, team2);
			}
		}
		
		LOGGER.info("Exiting getMlForPlayTheDog()");
	}
}