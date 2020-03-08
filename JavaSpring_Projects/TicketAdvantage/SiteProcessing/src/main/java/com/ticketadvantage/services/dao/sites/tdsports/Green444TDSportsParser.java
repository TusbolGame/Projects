/**
 * 
 */
package com.ticketadvantage.services.dao.sites.tdsports;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.PendingEvent;

/**
 * @author jmiller
 *
 */
public class Green444TDSportsParser extends TDSportsParser {
	private static final Logger LOGGER = Logger.getLogger(Green444TDSportsParser.class);
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd hh:mm a");
	private static final SimpleDateFormat TO_DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy hh:mm a z");

	/**
	 * Constructor
	 */
	public Green444TDSportsParser() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parsePendingBets(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Set<PendingEvent> parsePendingBets(String xhtml, String accountName, String accountId) throws BatchException {
		LOGGER.debug("accountName: " + accountName);
		LOGGER.debug("accountId: " + accountId);
		Set<PendingEvent> pendingEvents = new HashSet<PendingEvent>();

		// Get the document object
		final Document doc = parseXhtml(xhtml);
		final Elements divs = doc.select(".Table_Principal tbody tr td table tbody tr td div");
		if (divs != null) {
			final Iterator<Element> itr = divs.iterator();
			while (itr != null && itr.hasNext()) {
				final Element div = itr.next();
				final Elements as = div.select("h3 a");
				final Elements tbodys = div.select("div div table tbody");
				if (as != null && tbodys != null) {
					Iterator<Element> itrAs = as.iterator();
					Iterator<Element> itrTbodys = tbodys.iterator();
					while (itrAs.hasNext() && itrTbodys.hasNext()) {
						PendingEvent pe = new PendingEvent();
						pe.setAccountname(accountName);
						pe.setAccountid(accountId);

						Element a = itrAs.next();
						Element tbody = itrTbodys.next();
						String nHtml = a.html();
						String tHtml = tbody.html();
						LOGGER.debug("nHtml: " + nHtml);
						if (nHtml != null && nHtml.contains("STRAIGHT") || (tHtml != null && tHtml.contains("STRAIGHT"))) {
							// Get Rotation
							String htmlData = parseRotation(pe, a);
							LOGGER.debug("htmlData: " + htmlData);
							int bindx = htmlData.indexOf("(B+");
							if (bindx != -1) {
								String firstHalf = htmlData.substring(0, bindx);
								htmlData = htmlData.substring(bindx + "(B+".length());
								int eindx = htmlData.indexOf(")");
								if (eindx != -1) {
									htmlData = htmlData.substring(eindx + 1);
									htmlData = firstHalf + htmlData;
								}
							}

							// Get type lines, first, second
							String type = parseType(pe.getRotationid());
							pe.setLinetype(type);
							htmlData = htmlData.trim();
			
							// first check for total
							if (htmlData.startsWith("TOTAL")) {
								processTotal(pe, htmlData);
							} else if (htmlData.startsWith("1H")) {
								int index = htmlData.indexOf("1H");
								if (index != -1) {
									htmlData = htmlData.substring(index + "1H".length());
									htmlData = htmlData.trim();
			
									htmlData = getTeam(pe, htmlData);
									String indicator = "";
									if (htmlData.startsWith("+")) {
										indicator = "+";
									} else {
										indicator = "-";
									}
			
									// Now get the game type
									htmlData = htmlData.substring(1);
									boolean isMoneyLine = determineEventType(htmlData);
									if (isMoneyLine) {
										pe.setEventtype("ml");
										htmlData = processMoneyLine(pe, indicator, htmlData);
									} else {
										pe.setEventtype("spread");
										htmlData = processSpread(pe, indicator, htmlData);
									}
			
									// Get the game type
									// processGameType(pe, htmlData);
								}
							} else if (htmlData.startsWith("2H")) {
								int index = htmlData.indexOf("2H");
								if (index != -1) {
									htmlData = htmlData.substring(index + "2H".length());
									htmlData = htmlData.trim();
									
									htmlData = getTeam(pe, htmlData);
									String indicator = "";
									if (htmlData.startsWith("+")) {
										indicator = "+";
									} else {
										indicator = "-";
									}
			
									// Now get the game type
									htmlData = htmlData.substring(1);
									LOGGER.debug("htmlData: " + htmlData);
									boolean isMoneyLine = determineEventType(htmlData);
									if (isMoneyLine) {
										pe.setEventtype("ml");
										htmlData = processMoneyLine(pe, indicator, htmlData);
									} else {
										pe.setEventtype("spread");
										htmlData = processSpread(pe, indicator, htmlData);
									}
			
									// Get the game type
									// processGameType(pe, htmlData);
								}
							} else {
								htmlData = getTeam(pe, htmlData);
								String indicator = "";
								if (htmlData.startsWith("+")) {
									indicator = "+";
								} else {
									indicator = "-";
								}
			
								LOGGER.debug("HTMLDATA: " + htmlData);
								
								// Now get the game type
								htmlData = htmlData.substring(1);
								boolean isMoneyLine = determineEventType(htmlData);
								if (isMoneyLine) {
									pe.setEventtype("ml");
									htmlData = processMoneyLine(pe, indicator, htmlData);
								} else {
									pe.setEventtype("spread");
									htmlData = processSpread(pe, indicator, htmlData);
								}
			
								// Get the game type
								// processGameType(pe, htmlData);
							}
			
							LOGGER.debug("tbody: " + tbody);
							Elements trs = tbody.select("tr");
							if (trs != null) {
								for (int u = 0; u < trs.size(); u++) {
									Element tr = trs.get(u);
									LOGGER.debug("tr: " + tr);
									switch (u) {
										case 0:
										case 3:
										case 4:
											break;
										case 1:
											parseScrappingDate(pe, tr);
											break;
										case 2:
											parseScrappingTicket(pe, tr);
											break;
										case 5:
											parseScrappingSport(pe, tr);
											break;
										case 6:
											parseScrappingRiskWin(pe, tr);
											break;
										default:
											break;
									}
								}
							}

							// Add event
							LOGGER.debug("PE: " + pe);
							pendingEvents.add(pe);
						}
					}
				}
			}
		}

		LOGGER.info("Exiting parsePendingBets()");
		return pendingEvents;
	}

	/**
	 * 
	 * @param pe
	 * @param tr
	 */
	private void parseScrappingDate(PendingEvent pe, Element tr) {
		Elements tds = tr.select("td");
		for (int x = 0; (tds != null && x < tds.size()); x++) {
			Element td = tds.get(x);
			switch (x) {
				case 0:
					break;
				case 1: {
					// Mar 26 05:05 PM
					String dt = td.html();
					dt = dt.replaceAll("<br>", "");
					dt = dt.trim();
					boolean dayLight = TimeZone.getDefault().inDaylightTime(new Date());
					if (dayLight) {
						dt = dt + " EDT";
					} else {
						dt = dt + " EST";
					}
					Calendar cal = Calendar.getInstance();
					try {
						Date nDate = DATE_FORMAT.parse(dt);
						cal.setTime(nDate);
						cal.set(Calendar.YEAR, 2017);
						pe.setEventdate(TO_DATE_FORMAT.format(cal.getTime()));
					} catch (ParseException pee) {
						LOGGER.error(pee);
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param pe
	 * @param tr
	 */
	private void parseScrappingTicket(PendingEvent pe, Element tr) {
		Elements tds = tr.select("td");
		for (int x = 0; (tds != null && x < tds.size()); x++) {
			Element td = tds.get(x);
			switch (x) {
				case 0:
					break;
				case 1: {
					// Mar 26 05:05 PM
					String dt = td.html();
					dt = dt.replaceAll("<br>", "");
					dt = dt.trim();
					pe.setTicketnum(dt);
				}
			}
		}		
	}

	/**
	 * 
	 * @param pe
	 * @param tr
	 */
	private void parseScrappingSport(PendingEvent pe, Element tr) {
		Elements tds = tr.select("td");
		for (int x = 0; (tds != null && x < tds.size()); x++) {
			Element td = tds.get(x);
			switch (x) {
				case 0:
					break;
				case 1: {
					// Mar 26 05:05 PM
					String htmlData = td.html();
					htmlData = htmlData.replaceAll("<br>", "");
					htmlData = htmlData.trim();

					if ("CBB".equals(htmlData)) {
						pe.setGametype("NCAA Basketball");
						pe.setGamesport("Basketball");
					} else if ("NBA".equals(htmlData)) {
						pe.setGametype("NBA");
						pe.setGamesport("Basketball");
					} else if ("NFL".equals(htmlData)) {
						pe.setGametype("NFL");
						pe.setGamesport("Football");
					} else if ("NFL".equals(htmlData)) {
						pe.setGametype("CFB");
						pe.setGamesport("Football");
					} else if ("WNBA".equals(htmlData)) {
						pe.setGametype("WNBA");
						pe.setGamesport("Basketball");
					} else if ("NHL".equals(htmlData)) {
						pe.setGametype("NHL");
						pe.setGamesport("Hockey");
					}
				}
			}
		}		
	}

	/**
	 * 
	 * @param pe
	 * @param tr
	 */
	private void parseScrappingRiskWin(PendingEvent pe, Element tr) {
		Elements tds = tr.select("td");
		for (int x = 0; (tds != null && x < tds.size()); x++) {
			Element td = tds.get(x);
			switch (x) {
				case 0:
					break;
				case 1: {
					// Mar 26 05:05 PM
					String htmlData = td.html();
					htmlData = htmlData.replaceAll("<br>", "");
					htmlData = htmlData.trim();
					
					int index = htmlData.indexOf("/");
					if (index != -1) {
						String risk = htmlData.substring(0, index);
						risk = risk.trim();
						pe.setRisk(risk);
						String win = htmlData.substring(index + 1);
						win = win.trim();
						pe.setWin(win);
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param pe
	 * @param indicator
	 * @param htmlData
	 * @return
	 */
	private String processMoneyLine(PendingEvent pe, String indicator, String htmlData) {
		LOGGER.info("Entering processMoneyLine()");

		// ie: -105
		pe.setJuiceplusminus(indicator);
		int index = htmlData.indexOf("<br>");
		if (index != -1) {
			String juice = htmlData.substring(0, index);
			pe.setJuice(reformatValues(juice));
			htmlData = htmlData.substring(index + "<br>".length());
		}

		LOGGER.info("Exiting processMoneyLine()");
		return htmlData;
	}

	/**
	 * 
	 * @param pe
	 * @param indicator
	 * @param htmlData
	 * @return
	 */
	private String processSpread(PendingEvent pe, String indicator, String htmlData) {
		LOGGER.info("Entering processSpread()");

		// ie: +3-105
		pe.setLineplusminus(indicator);
		int index = htmlData.indexOf("+");
		if (index != -1) {
			String line = htmlData.substring(0, index);
			pe.setLine(reformatValues(line));
			htmlData = htmlData.substring(index + "+".length());
			pe.setJuiceplusminus("+");
			index = htmlData.indexOf("<br>");
			if (index != -1) {
				String juice = htmlData.substring(0, index);
				pe.setJuice(reformatValues(juice));
				htmlData = htmlData.substring(index + "<br>".length());
			}
		} else {
			index = htmlData.indexOf("-");
			if (index != -1) {
				String line = htmlData.substring(0, index);
				pe.setLine(reformatValues(line));
				htmlData = htmlData.substring(index + "-".length());
				pe.setJuiceplusminus("-");
				index = htmlData.indexOf("<br>");
				if (index != -1) {
					String juice = htmlData.substring(0, index);
					pe.setJuice(reformatValues(juice));
					htmlData = htmlData.substring(index + "<br>".length());
				}
			}
		}

		LOGGER.info("Exiting processSpread()");
		return htmlData;
	}

	/**
	 * 
	 * @param htmlData
	 * @return
	 */
	private boolean determineEventType(String htmlData) {
		LOGGER.info("Entering determineEventType()");
		boolean isMoneyLine = true;

		int index = htmlData.indexOf("+");
		if (index != -1) {
			isMoneyLine = false;
		} else {
			index = htmlData.indexOf("-");
			if (index != -1) {
				isMoneyLine = false;
			}
		}

		LOGGER.info("Exiting determineEventType()");
		return isMoneyLine;
	}

	/**
	 * 
	 * @param pe
	 * @param htmlData
	 * @return
	 */
	private String getTeam(PendingEvent pe, String htmlData) {
		LOGGER.info("Entering getTeam()");
		
		int index = htmlData.indexOf("+");
		if (index != -1) {
			String team = htmlData.substring(0, index);
			team = team.trim();
			pe.setTeam(team);
			htmlData = htmlData.substring(index);
		} else {
			index = htmlData.indexOf("-");
			if (index != -1) {
				String team = htmlData.substring(0, index);
				team = team.trim();
				pe.setTeam(team);
				htmlData = htmlData.substring(index);
			}
		}
		htmlData = super.reformatValues(htmlData);

		LOGGER.info("Exiting getTeam()");
		return htmlData;
	}

	/**
	 * 
	 * @param pe
	 * @param htmlData
	 */
	private void processTotal(PendingEvent pe, String htmlData) {
		LOGGER.info("Entering processTotal()");

		pe.setEventtype("total");
		int index = htmlData.indexOf("TOTAL");
		if (index != -1) {
			htmlData = htmlData.substring(index + "TOTAL".length());
			htmlData = htmlData.trim();
			// Now get over/under o40½-110
			String indicator = htmlData.substring(0, 1);
			pe.setLineplusminus(indicator);
			htmlData = htmlData.substring(1);
			index = htmlData.indexOf("-");
			if (index != -1) {
				String number = htmlData.substring(0, index);
				pe.setLine(reformatValues(number));
				pe.setJuiceplusminus("-");
			} else {
				index = htmlData.indexOf("+");
				if (index != -1) {
					String number = htmlData.substring(0, index);
					pe.setLine(reformatValues(number));
					pe.setJuiceplusminus("+");
				}
			}
			if (index != -1) {
				htmlData = htmlData.substring(index + 1);
				index = htmlData.indexOf("<br>");
				if (index != -1) {
					String juice = htmlData.substring(0, index);
					juice = juice.trim();
					pe.setJuice(reformatValues(juice));
					htmlData = htmlData.substring(index + "<br>".length());
					htmlData = htmlData.trim();
					index = htmlData.indexOf("<br>");
					if (index != -1) {
						String team = htmlData.substring(0, index);
						pe.setTeam(team);
						htmlData = htmlData.substring(index + "<br>".length());
						htmlData = htmlData.trim();
						pe.setGametype(reformatValues(htmlData));
					}
				}
			}
		}
		LOGGER.info("Exiting processTotal()");
	}

	/**
	 * 
	 * @param pe
	 * @param div
	 * @return
	 */
	private String parseRotation(PendingEvent pe, Element a) {
		LOGGER.info("Entering parseRotation()");

		String aData = a.html();
		if (aData != null) {
			int beginIndex = aData.indexOf("[");
			int endIndex = aData.indexOf("]");
			if (beginIndex != -1 && endIndex != -1) {
				String rotationId = aData.substring(beginIndex + 1, endIndex);
				LOGGER.info("rotationId: " + rotationId);
				pe.setRotationid(reformatValues(rotationId));

				// Now move on
				aData = aData.substring(endIndex + 1);
			}
		}

		LOGGER.info("Exiting parseRotation()");
		return aData;
	}

	/**
	 * 
	 * @param htmlData
	 * @return
	 */
	private String parseType(String htmlData) {
		LOGGER.info("Entering parseType()");
		String type = "game";

		if (htmlData != null) {
			if (htmlData.length() == 4 && htmlData.startsWith("1")) {
				type = "first";
			} else if (htmlData.length() == 4 && htmlData.startsWith("2")) {
				type = "second";
			}
		}
		
		LOGGER.info("Exiting parseType()");
		return type;
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
		map = findMenu(doc.select("center table tr td table tbody tr"), map, type, sport, "td span b", "td input");
		LOGGER.debug("MAP: " + map);

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
		Elements elements = doc.select("#content center table tbody tr");
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
						(classInfo.contains("TrGameOdd") || classInfo.contains("TrGameEven"))) {
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

		LOGGER.info("Exiting getGameData()");
		return events;
	}
}