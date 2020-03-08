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
public class IbetTDSportsParser extends TDSportsParser {
	private static final Logger LOGGER = Logger.getLogger(IbetTDSportsParser.class);
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd yyyy hh:mm a z");

	/**
	 * Constructor
	 */
	public IbetTDSportsParser() {
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
		map = findMenu(doc.select("#TableLeague div div table tbody tr td table tbody tr td div div"), map, type, sport, "a", "div input");

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

		// www.ibet.ag
		Elements elements = doc.select(".ScheduleLeagueGames div div div");
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
		for (int x = 0;(elements != null && x < elements.size()); x++) {
			// Loop through the elements and then check for the dates
			final Element element = elements.get(x);
			LOGGER.debug("Element: " + element);

			if (element != null) {
				String classInfo = element.attr("class");
				LOGGER.debug("ClassInfo: " + classInfo);

				if ((classInfo != null && classInfo.length() > 0) && (classInfo.contains("animated"))) {
					eventPackage = new TDSportsEventPackage();
					getTeamDataForIbet(element, eventPackage);
					events.add(eventPackage);
				}
			}
		}

		LOGGER.info("Exiting getGameData()");
		return events;
	}

	/**
	 * 
	 * @param div
	 * @param eventPackage
	 */
	private void getTeamDataForIbet(Element div, TDSportsEventPackage eventPackage) throws BatchException {
		LOGGER.info("Entering getTeamDataForIbet()");

		TDSportsTeamPackage team1 = new TDSportsTeamPackage();
		TDSportsTeamPackage team2 = new TDSportsTeamPackage();

		// First get the date
		Elements dates = div.select(".header-tool control span");
		for (int x = 0; x < dates.size(); x++) {
			final Element span = dates.get(x);
			String date = span.html();
			date = determineDateString(date);
			// Setup the date
			Date eventDate = setupGameDate(DATE_FORMAT, date);
			team1.setEventdatetime(eventDate);
			team2.setEventdatetime(eventDate);
			team1.settDate(date);
			team2.settDate(date);

			eventPackage.setDateofevent(date);
		}

		// Now get team data
		Elements teams = div.select(".row-line");
		for (int x = 0; (teams != null && x < teams.size()); x++) {
			Element teamRow = teams.get(x);
			if (x == 0) {
				team1 = getIbetTeamInfo(teamRow, team1);
				eventPackage.setId(team1.getId());
			} else if (x == 1) {
				team2 = getIbetTeamInfo(teamRow, team2);
			}
		}

		// Setup the teams on the package
		eventPackage.setSiteteamone(team1);
		eventPackage.setSiteteamtwo(team2);
		eventPackage.setTeamone(team1);
		eventPackage.setTeamtwo(team2);

		// Now get team2 info
		LOGGER.info("Exiting getTeamDataForIbet()");
	}

	/**
	 * 
	 * @param teamRow
	 * @param team
	 * @return
	 */
	private TDSportsTeamPackage getIbetTeamInfo(Element teamRow, TDSportsTeamPackage team) {
		LOGGER.info("Entering getIbetTeamInfo()");
		LOGGER.debug("Element: " + teamRow);
		LOGGER.debug("TDSportsTeamPackage: " + team);

		// Get Rotation
		Elements spans = teamRow.select(".col-md-4 .rot-team .text-black");
		if (spans != null) {
			team.setEventid(spans.get(0).html());
			team.setId(Integer.parseInt(spans.get(0).html()));
		}
		
		// Get Team
		spans = teamRow.select(".col-md-4 .team-info .team .text-black");
		if (spans != null) {
			team.setTeam(spans.get(0).html());
		}

		// Get Spread, Total, ML
		final Elements divs = teamRow.select(".col-md-8 .col-md-3");
		for (int i = 0; (divs != null && i < divs.size()); i++) {
			if (i == 0) {
				// Spread
				team = getIbetSpread(divs.get(i), team);
			} else if (i == 1) {
				// Total
				team = getIbetTotal(divs.get(i), team);
			} else if (i == 2) {
				// ML
				team = getIbetMoneyLine(divs.get(i), team);
			}
		}

		LOGGER.debug("TDSportsTeamPackage: " + team);
		LOGGER.info("Exiting getIbetTeamInfo()");
		return team;
	}

	/**
	 * 
	 * @param div
	 * @param team
	 * @return
	 */
	private TDSportsTeamPackage getIbetSpread(Element div, TDSportsTeamPackage team) {
		LOGGER.info("Entering getIbetSpread()");
		LOGGER.debug("div: " + div);

		// First get the input field information
		final Map<String, String> hashMap = parseInputField(div.select("input"), 0);
		team.setGameSpreadInputId(hashMap.get("id"));
		team.setGameSpreadInputName(hashMap.get("name"));
		team.setGameSpreadInputValue(hashMap.get("value"));

		// -2½+200; Now parse the data
		String spread = getHtmlFromElement(div, "span", 0, false);

		// Setup spread
		team.addGameSpreadOptionValue("0", "0");
		team = (TDSportsTeamPackage)parseSpreadData(reformatValues(spread), 0, " ", null, team);

		LOGGER.info("Exiting getIbetSpread()");
		return team;
	}

	/**
	 * 
	 * @param div
	 * @param team
	 * @return
	 */
	private TDSportsTeamPackage getIbetTotal(Element div, TDSportsTeamPackage team) {
		LOGGER.info("Entering getIbetTotal()");
		LOGGER.debug("div: " + div);

		// First get the input field information
		final Map<String, String> hashMap = parseInputField(div.select("input"), 0);
		team.setGameTotalInputId(hashMap.get("id"));
		team.setGameTotalInputName(hashMap.get("name"));
		team.setGameTotalInputValue(hashMap.get("value"));

		// o40½-110; Now parse the data
		String total = getHtmlFromElement(div, "span", 0, false);

		// Parse Total Data
		team.addGameTotalOptionValue("0", "0");
		team = (TDSportsTeamPackage)parseTotalData(reformatValues(total), 0, " ", null, team);

		LOGGER.info("Exiting getIbetTotal()");
		return team;
	}

	/**
	 * 
	 * @param div
	 * @param team
	 * @return
	 */
	private TDSportsTeamPackage getIbetMoneyLine(Element div, TDSportsTeamPackage team) {
		LOGGER.info("Entering getIbetMoneyLine()");
		LOGGER.debug("Element: " + div);
		
		// First get the input field information
		final Map<String, String> hashMap = parseInputField(div.select("input"), 0);
		team.setGameMLInputId(hashMap.get("id"));
		team.setGameMLInputName(hashMap.get("name"));
		team.addGameMLInputValue("0", hashMap.get("value"));

		// -110; Now parse the data
		String ml = getHtmlFromElement(div, "span", 0, false);

		// Parse for Money Line
		team = (TDSportsTeamPackage)parseMlData(reformatValues(ml), 0, team);

		LOGGER.info("Exiting getIbetMoneyLine()");
		return team;
	}
}