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
public class FoursfoldTDSportsParser extends TDSportsParser {
	private static final Logger LOGGER = Logger.getLogger(FoursfoldTDSportsParser.class);
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd yyyy hh:mm a z");

	/**
	 * Constructor
	 */
	public FoursfoldTDSportsParser() {
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
		final String[] types = new String[] { "hidden" };
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
		map = findMenu(doc.select("center table tbody tr td table tbody tr"), map, type, sport, "td div", "td input");
		map.put("ctl00$WagerContent$btn_Continue", "submit");

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
		Elements elements = doc.select(".lselected div div");
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
						(classInfo.contains("TGameOdd") || classInfo.contains("TGameEven"))) {
						if (t++ == 0) {
							try {
								eventPackage = new TDSportsEventPackage();
								team1 = new TDSportsTeamPackage();
								team2 = new TDSportsTeamPackage();
								
								getDate(element.select(".ldatetime").get(0), team1, team2);
								getTeamData(element.select(".main_section").get(0), team1, team2);
								eventPackage.setId(team1.getId());
								
								// Get date information
								Date eventDate = null;
								eventDate = setupDate(DATE_FORMAT, team1.gettDate(), team1.gettTime());
								team1.setEventdatetime(eventDate);
								team2.setEventdatetime(eventDate);
								final String dateOfEvent = team1.gettDate() + " " + team1.gettTime();
								team1.settDate(dateOfEvent);
								team2.settDate(dateOfEvent);
								eventPackage.setDateofevent(dateOfEvent);
								eventPackage.setSiteteamone(team1);
								eventPackage.setSiteteamtwo(team2);
								eventPackage.setTeamone(team1);
								eventPackage.setTeamtwo(team2);
								LOGGER.debug("EventPackage: " + eventPackage);
								events.add(eventPackage);
								t = 0;
							} catch (Throwable th) {
								LOGGER.error(t);
							}
						}
					}
				} 
			}
			LOGGER.debug("events: " + events);
		}

		LOGGER.info("Exiting getGameData()");
		return events;
	}

	/**
	 * 
	 * @param div
	 * @param team1
	 * @param team2
	 */
	protected void getDate(Element div, TDSportsTeamPackage team1, TDSportsTeamPackage team2) {
		LOGGER.info("Entering getDate()");

		// Date String
		Elements spans = div.select("span");
		if (spans != null && spans.size() == 2) {
			String tdate = spans.get(0).html().trim();
			String ttime = spans.get(1).html().trim();
			team1.settDate(tdate);
			team2.settDate(tdate);
			team1.settTime(ttime);
			team2.settTime(ttime);
		}

		LOGGER.info("Exiting getDate()");
	}

	/**
	 * 
	 * @param elements
	 * @param team1
	 * @param team2
	 * @return
	 */
	protected int getTeamData(Element div, TDSportsTeamPackage team1, TDSportsTeamPackage team2) {
		LOGGER.info("Entering getTeamData()");
		LOGGER.debug("div: " + div);
		int size = 0;

		// Rotation IDs
		getEventId(div.select(".tnumbers span"), team1, team2);

		// Teams
		getTeam(div.select(".tnames div"), team1, team2);
		
		LOGGER.error("sportType: " + sportType);
		if (sportType != null && sportType.toLowerCase().contains("mlb")) {
			try {
				// Get the moneylines
				Elements divs = div.select(".tmoneyline .tmoneylinev");
	
				// Moneylines
				if (divs != null && divs.size() > 0) {
					getMlbMoneyLine(divs.get(0), team1);
				}

				// Spreads
				if (divs != null && divs.size() > 1) {
					getMlbSpread(divs.get(1), team1);
				}

				// Totals
				getTotal(div.select(".ttotal div"), team1, team2);

				divs = div.select(".tmoneyline .tmoneylineh");
				
				// Moneylines
				if (divs != null && divs.size() > 0) {
					getMlbMoneyLine(divs.get(0), team2);
				}

				// Spreads
				if (divs != null && divs.size() > 1) {
					getMlbSpread(divs.get(1), team2);
				}
			} catch (Throwable t) {
				LOGGER.error(t.getMessage());
				t.printStackTrace();
			}
		} else {
			try {
				// Get the spreads
				getSpread(div.select(".tspread div"), team1, team2);

				// Totals
				getTotal(div.select(".ttotal div"), team1, team2);

				// Moneys
				getMoneyLine(div.select(".tmoneyline div"), team1, team2);
			} catch (Throwable t) {
				LOGGER.error(t.getMessage());
				t.printStackTrace();
			}
		}

		LOGGER.info("Exiting getTeamData()");
		return size;
	}

	/**
	 * 
	 * @param spans
	 * @param team1
	 * @param team2
	 */
	protected void getEventId(Elements spans, TDSportsTeamPackage team1, TDSportsTeamPackage team2) {
		LOGGER.info("Entering getEventId()");

		team1.setEventid(spans.get(0).html());
		team1.setId(Integer.parseInt(spans.get(0).html()));
		team2.setEventid(spans.get(1).html());
		team2.setId(Integer.parseInt(spans.get(1).html()));

		LOGGER.info("Exiting getEventId()");
	}

	/**
	 * 
	 * @param divs
	 * @param team1
	 * @param team2
	 */
	protected void getTeam(Elements divs, TDSportsTeamPackage team1, TDSportsTeamPackage team2) {
		LOGGER.info("Entering getTeam()");

		// Team String
		team1.setTeam(divs.get(0).html());
		team2.setTeam(divs.get(1).html());

		LOGGER.info("Exiting getTeam()");
	}

	/**
	 * 
	 * @param divs
	 * @param team1
	 * @param team2
	 */
	protected void getMoneyLine(Elements divs, TDSportsTeamPackage team1, TDSportsTeamPackage team2) {
		LOGGER.info("Entering getMoneyLine()");

		// First get the input field information
		Element div1 = divs.get(0);
		Element div2 = divs.get(1);

		Map<String, String> hashMap = parseInputField(div1.select("div input"), 0);
		team1.setGameMLInputId(hashMap.get("id"));
		team1.setGameMLInputName(hashMap.get("name"));
		team1.addGameMLInputValue("0", hashMap.get("value"));

		hashMap = parseInputField(div2.select("div input"), 0);
		team2.setGameMLInputId(hashMap.get("id"));
		team2.setGameMLInputName(hashMap.get("name"));
		team2.addGameMLInputValue("0", hashMap.get("value"));

		// Now parse the data
		Elements divdivs = div1.select("div");
		if (divdivs != null && divdivs.size() > 0) {
			LOGGER.error("divdivs: " + divdivs);
			String ml = divdivs.get(0).html();
			LOGGER.error("ml1: " + ml);
			if (ml != null && ml.length() > 0) {
				int index = ml.indexOf(">");
				if (index != -1) {
					// +200; Now parse the data
					ml = ml.substring(index + 1);
					ml = reformatValues(ml);
					team1 = (TDSportsTeamPackage)parseMlData(reformatValues(ml), 0, team1);
				}
			}
		}

		divdivs = div2.select("div");
		if (divdivs != null && divdivs.size() > 0) {
			LOGGER.error("divdivs: " + divdivs);
			String ml = divdivs.get(1).html();
			ml = div2.html();
			LOGGER.error("ml2: " + ml);
			if (ml != null && ml.length() > 0) {
				int index = ml.indexOf(">");
				if (index != -1) {
					// +200; Now parse the data
					ml = ml.substring(index + 1);
					ml = reformatValues(ml);
					team2 = (TDSportsTeamPackage)parseMlData(reformatValues(ml), 0, team2);
				}
			}
		}

		LOGGER.info("Exiting getMoneyLine()");
	}

	/**
	 * 
	 * @param divs
	 * @param team1
	 * @param team2
	 */
	protected void getTotal(Elements divs, TDSportsTeamPackage team1, TDSportsTeamPackage team2) {
		LOGGER.info("Entering getTotal()");

		// First get the input field information
		Element div1 = divs.get(0);
		Element div2 = divs.get(1);

		Map<String, String> hashMap = parseInputField(div1.select("input"), 0);
		team1.setGameTotalInputId(hashMap.get("id"));
		team1.setGameTotalInputName(hashMap.get("name"));
		team1.setGameTotalInputValue(hashMap.get("value"));

		hashMap = parseInputField(div2.select("input"), 0);
		team2.setGameTotalInputId(hashMap.get("id"));
		team2.setGameTotalInputName(hashMap.get("name"));
		team2.setGameTotalInputValue(hashMap.get("value"));

		// Now parse the data
		String total = div1.html();
		if (total != null && total.length() > 0) {
			int index = total.indexOf(">");
			if (index != -1) {
				// +200; Now parse the data
				total = total.substring(index + 1);
				total = reformatValues(total);
				team1 = (TDSportsTeamPackage)parseTotalData(reformatValues(total), 0, " ", null, team1);
			}
		}
		
		total = div2.html();
		if (total != null && total.length() > 0) {
			int index = total.indexOf(">");
			if (index != -1) {
				// +200; Now parse the data
				total = total.substring(index + 1);
				total = reformatValues(total);
				team2 = (TDSportsTeamPackage)parseTotalData(reformatValues(total), 0, " ", null, team2);
			}
		}

		// Parse Total Data
		team1.addGameTotalOptionValue("0", "0");
		team2.addGameTotalOptionValue("0", "0");

		LOGGER.info("Exiting getTotal()");
	}

	/**
	 * 
	 * @param divs
	 * @param team1
	 * @param team2
	 */
	protected void getSpread(Elements divs, TDSportsTeamPackage team1, TDSportsTeamPackage team2) {
		LOGGER.info("Entering getSpread()");

		// First get the input field information
		Element div1 = divs.get(0);
		Element div2 = divs.get(1);

		Map<String, String> hashMap = parseInputField(div1.select("input"), 0);
		team1.setGameSpreadInputId(hashMap.get("id"));
		team1.setGameSpreadInputName(hashMap.get("name"));
		team1.setGameSpreadInputValue(hashMap.get("value"));

		hashMap = parseInputField(div2.select("input"), 0);
		team2.setGameSpreadInputId(hashMap.get("id"));
		team2.setGameSpreadInputName(hashMap.get("name"));
		team2.setGameSpreadInputValue(hashMap.get("value"));

		// Now parse the data
		String spread = div1.html();
		if (spread != null && spread.length() > 0) {
			int index = spread.indexOf(">");
			if (index != -1) {
				// +200; Now parse the data
				spread = spread.substring(index + 1);
				spread = reformatValues(spread);
				team1 = (TDSportsTeamPackage)parseSpreadData(reformatValues(spread), 0, " ", null, team1);
			}
		}

		spread = div2.html();
		if (spread != null && spread.length() > 0) {
			int index = spread.indexOf(">");
			if (index != -1) {
				// +200; Now parse the data
				spread = spread.substring(index + 1);
				spread = reformatValues(spread);
				team2 = (TDSportsTeamPackage)parseSpreadData(reformatValues(spread), 0, " ", null, team2);
			}
		}

		// Parse Spread Data
		team1.addGameSpreadOptionValue("0", "0");
		team2.addGameSpreadOptionValue("0", "0");

		LOGGER.info("Exiting getSpread()");
	}

	/**
	 * 
	 * @param div
	 * @param team
	 */
	protected void getMlbMoneyLine(Element div, TDSportsTeamPackage team) {
		LOGGER.info("Entering getMlbMoneyLine()");

		Map<String, String> hashMap = parseInputField(div.select("input"), 0);
		team.setGameMLInputId(hashMap.get("id"));
		team.setGameMLInputName(hashMap.get("name"));
		team.addGameMLInputValue("0", hashMap.get("value"));

		// Now parse the data
		String ml = div.html();
		LOGGER.error("ml1: " + ml);
		if (ml != null && ml.length() > 0) {
			int index = ml.indexOf(">");
			if (index != -1) {
				// +200; Now parse the data
				ml = ml.substring(index + 1);
				ml = reformatValues(ml);
				team = (TDSportsTeamPackage) parseMlData(reformatValues(ml), 0, team);
			}
		}

		LOGGER.info("Exiting getMlbMoneyLine()");
	}

	/**
	 * 
	 * @param divs
	 * @param team
	 */
	protected void getMlbSpread(Element div, TDSportsTeamPackage team) {
		LOGGER.info("Entering getMlbSpread()");

		Map<String, String> hashMap = parseInputField(div.select("input"), 0);
		team.setGameSpreadInputId(hashMap.get("id"));
		team.setGameSpreadInputName(hashMap.get("name"));
		team.setGameSpreadInputValue(hashMap.get("value"));

		// Now parse the data
		String spread = div.html();
		if (spread != null && spread.length() > 0) {
			int index = spread.indexOf(">");
			if (index != -1) {
				// +200; Now parse the data
				spread = spread.substring(index + 1);
				spread = reformatValues(spread);
				team = (TDSportsTeamPackage)parseSpreadData(reformatValues(spread), 0, " ", null, team);
			}
		}

		// Parse Spread Data
		team.addGameSpreadOptionValue("0", "0");

		LOGGER.info("Exiting getMlbSpread()");
	}
}