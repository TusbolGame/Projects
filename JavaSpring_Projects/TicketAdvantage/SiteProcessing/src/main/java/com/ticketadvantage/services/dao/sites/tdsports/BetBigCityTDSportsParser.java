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
import java.util.Set;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.PendingEvent;

/**
 * @author jmiller
 *
 */
public class BetBigCityTDSportsParser extends TDSportsParser {
	protected static final Logger LOGGER = Logger.getLogger(BetBigCityTDSportsParser.class);
	protected static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd yyyy hh:mm a z");
	protected static final String[] WNBA_TEAMS = {
		"DREAM",
		"SKY",
		"SUN",
		"FEVER",
		"LIBERTY",
		"MYSTICS",
		"WINGS",
		"SPARKS",
		"LYNX",
		"MERCURY",
		"STARS",
		"STORM",
	};

	/**
	 * Constructor
	 */
	public BetBigCityTDSportsParser() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// BetBigCity
			

//			final String xhtml = "<table id=\"CreateWagerTable\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody><tr class=\"TrGameOdd\"><td width=\"20%\" align=\"center\">Ticket Number: 88014656<br>Oct 0703:30 PM</td><td>Internet / -1</td><td>Oct 0403:16 PM </td><td>CFB<br></td><td>STRAIGHT BET<br>[473] CHIEFS (KC) -2½-110 (SUNDAY NIGHT FOOTBALL (NBC))<br></td><td class=\"sum\">1100</td><td class=\"sum1\">1000<br></td></tr></tbody></table>";
//			final String xhtml = "<table id=\"CreateWagerTable\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody><tr class=\"TrGameOdd\"><td width=\"20%\" align=\"center\">Ticket Number: 88014656<br>Oct 0703:30 PM</td><td>Internet / -1</td><td>Oct 0403:16 PM </td><td>CFB<br></td><td>STRAIGHT BET<br>[348] PURDUE -3½-110 (ESPN-2)<br></td><td class=\"sum\">1100</td><td class=\"sum1\">1000<br></td></tr></tbody></table>";
//			final String xhtml = "<table id=\"CreateWagerTable\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody><tr class=\"TrGameOdd\"><td width=\"20%\" align=\"center\">Ticket Number: 88014656<br>Oct 0703:30 PM</td><td>Internet / -1</td><td>Oct 0403:16 PM </td><td>NFL<br></td><td>STRAIGHT BET<br>[474] TEXANS (HOU) u45-110 (SUNDAY NIGHT FOOTBALL (NBC))<br></td><td class=\"sum\">1100</td><td class=\"sum1\">1000<br></td></tr></tbody></table>";
//			final String xhtml = "<table id=\"CreateWagerTable\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody><tr class=\"TrGameOdd\"><td width=\"20%\" align=\"center\">Ticket Number: 88014656<br>Oct 0703:30 PM</td><td>Internet / -1</td><td>Oct 0403:16 PM </td><td>CFB<br></td><td>STRAIGHT BET<br>[377] WASHINGTON STATE o60-110 (FOX)<br></td><td class=\"sum\">1100</td><td class=\"sum1\">1000<br></td></tr></tbody></table>";

			final String xhtml = "<table id=\"CreateWagerTable\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody><tr class=\"TrGameOdd\"><td width=\"20%\" align=\"center\">Ticket Number: 88284284<br>Oct 0807:39 PM</td><td>Internet / -1</td><td>Oct 0807:04 PM </td><td>MLB<br></td><td>STRAIGHT BET<br>[473] CHIEFS (KC) -130 (SUNDAY NIGHT FOOTBALL (NBC))<br></td><td class=\"sum\">1100</td><td class=\"sum1\">1000<br></td></tr></tbody></table>";
//			final String xhtml = "<table id=\"CreateWagerTable\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody><tr class=\"TrGameOdd\"><td width=\"20%\" align=\"center\">Ticket Number: 88284284<br>Oct 0807:39 PM</td><td>Internet / -1</td><td>Oct 0807:04 PM </td><td>MLB<br></td><td>STRAIGHT BET<br>[953] INDIANS (CLE) -110<br>( ACTION ) (GAME #3 (INDIANS LEAD SERIES 2-0) FOX)<br></td><td class=\"sum\">1100</td><td class=\"sum1\">1000<br></td></tr></tbody></table>";

			final BetBigCityTDSportsParser tsp = new BetBigCityTDSportsParser();
			tsp.timezone = "ET";
			final Set<PendingEvent> pending = tsp.parsePendingBets(xhtml, "test1", "test2");
			final Iterator<PendingEvent> itrs = pending.iterator();
			while (itrs.hasNext()) {
				PendingEvent pe = itrs.next();
				System.out.println("PendingEvent: " + pe);
			}
		} catch (Throwable t) {
			t.printStackTrace();
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

		if (sport != null && sport.length > 0) {
			for (String sportName : sport) {
				LOGGER.debug("sportName: " + sportName);
				if (sportName.contains("MLB") || sportName.contains("BASEBALL")) {
					isMlb = true;
					LOGGER.error("setting isMlb to true");
				}
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
		map = findMenu(doc.select(".leagueevent"), map, type, sport, "a", "input");

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
		Elements elements = doc.select("#OddsTables div table tbody tr");
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
		String ticketNumber = "Ticket Number - ";
		final String ticketInfo = " USD</TD><TD>";

		if (xhtml.contains("Your bet expired or has already been submited.")) {
			throw new BatchException(BatchErrorCodes.WAGER_HAS_EXPIRED, BatchErrorMessage.WAGER_HAS_EXPIRED, "The wager has expired or already been bet", xhtml);	
		}
		
		if (xhtml.contains("line changed for one (or more) of your selections.")) {
			// Line changed throw exception
			throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed and cannot complete transaction", xhtml);	
		}

		// First check for Wager has been accepted!
		if (xhtml != null && xhtml.contains(ticketInfo)) {
			// Great, wager is complete; now get the Ticket Number
			int index = xhtml.indexOf(ticketInfo);
			if (index != -1) {
				final String nxhtml = xhtml.substring(index + ticketInfo.length());
				index = nxhtml.indexOf("</TD>");
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
					String classInfo = element.attr("class");
					LOGGER.debug("ClassInfo: " + classInfo);

					if ((classInfo != null && classInfo.length() > 0) && 
						(classInfo.contains("trgameodd") || classInfo.contains("trgameeven"))) {
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
		int size = 0;

		LOGGER.error("isMlb: " + isMlb);
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

		if (elements != null && elements.size() == 8) {
			LOGGER.info("elements.size(): " + elements.size());
			size = 8;
			for (int x = 0;(elements != null && x < elements.size()); x++) {
				final Element td = elements.get(x);
	
				switch (x) {
					case 0: // Do nothing
					case 4:
						break;
					case 1:
						team = getDate(td, team);
						break;
					case 2:
						team = getEventId(td, team);
						break;
					case 3:
						team = getTeam(td, team);
						break;
					case 5:
						team = getMoneyLine(td, team);
						break;
					case 6:
						team = getTotal(td, team);
						break;
					case 7:
						team = getSpread(td, team);
						break;
				}
			}
		}

		LOGGER.info("Exiting parseMlb()");
		return size;
	}
}