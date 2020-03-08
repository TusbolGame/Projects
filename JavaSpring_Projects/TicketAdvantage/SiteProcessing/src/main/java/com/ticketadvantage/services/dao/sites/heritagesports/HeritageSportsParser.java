/**
 * 
 */
package com.ticketadvantage.services.dao.sites.heritagesports;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ticketadvantage.services.dao.sites.SiteParser;
import com.ticketadvantage.services.dao.sites.SiteTeamPackage;
import com.ticketadvantage.services.errorhandling.AppErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.PendingEvent;

/**
 * @author jmiller
 *
 */
public class HeritageSportsParser extends SiteParser {
	private static final Logger LOGGER = Logger.getLogger(HeritageSportsParser.class);
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("E MM/dd yyyy hh:mma Z");
	protected String inetWagerNumber;
	protected String inetSportSelection;
	private boolean isMlb;

	/**
	 * Constructor
	 */
	public HeritageSportsParser() {
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
		LOGGER.debug("xhtml: " + xhtml);
		final Map<String, String> map = new HashMap<String, String>();

		// Get the document object
		final Document doc = parseXhtml(xhtml);

		// Get hidden input fields
		final String[] types = new String[] { "text", "password", "hidden" };
		final Elements forms = doc.select("form");
		for(int x = 0; (forms != null && x < forms.size()); x++) {
			final Element form = forms.get(x);
			map.put("action", form.attr("action"));
			getAllElementsByType(form, "input", types, map);
		}

		LOGGER.info("Exiting parseIndex()");
		return map;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseLogin(java.lang.String)
	 */
	@Override
	public Map<String, String> parseLogin(String xhtml) throws BatchException {
		LOGGER.info("Entering parseLogin()");
		LOGGER.debug("xhtml: " + xhtml);
		final Map<String, String> map = new HashMap<String, String>();

		// Get the document object
		final Document doc = parseXhtml(xhtml);

		final Element iwn = doc.getElementById("inetWagerNumber");
		final Element iss = doc.getElementById("inetSportSelection");

		if (iwn != null) {
			this.inetWagerNumber = iwn.attr("value");
			map.put("inetWagerNumber", this.inetWagerNumber);
		}

		if (iss != null) {
			this.inetSportSelection = iss.attr("value");
			map.put("inetSportSelection", this.inetSportSelection);
		}

		LOGGER.info("Exiting parseLogin()");
		return map;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseMenu(java.lang.String, java.lang.String[], java.lang.String[])
	 */
	@Override
	public Map<String, String> parseMenu(String xhtml, String[] type, String[] sport) throws BatchException {
		LOGGER.info("Entering parseMenu()");
		LOGGER.info("xhtml: " + xhtml);
		LOGGER.info("type: " + java.util.Arrays.toString(type));
		LOGGER.info("sport: " + java.util.Arrays.toString(sport));
		Map<String, String> map = new HashMap<String, String>();
		
		for (String sportName : sport) {
			if (sportName.contains("MLB") || sportName.contains("mlb")) {
				isMlb = true;
			}
		}

		try {
			// Parse to get the Document
			final SAXReader reader = new SAXReader(); 
			final InputStream is = new ByteArrayInputStream(xhtml.getBytes());
			final org.dom4j.Document document = reader.read(is);
	
//			<ArrayOfLeagues cache="0" loaded_from_cache="false">
//				<League name="NFL" sport="Football" id="101" active="1">
//					<available_periods>
//						<Period number="0" name="Game"/>
//						<Period number="1" name="1st Half"/>
//					</available_periods>
//				</League>
//			</<ArrayOfLeagues>

			// Extract the root element
			@SuppressWarnings("unchecked")
			List<Node> leagues = document.selectNodes("/ArrayOfLeagues/League");

			// Loop through the leagues
			for (Node node : leagues) {
				String league = node.valueOf("@name");
				LOGGER.debug("league: " + league);
				@SuppressWarnings("unchecked")
				List<Node> periods = node.selectNodes("available_periods/Period");
				for (Node period : periods) {
					String leaguePeriod = period.valueOf("@name");
					LOGGER.debug("leaguePeriod: " + leaguePeriod);
					for (String sportName : sport) {
						for (String typeName : type) {
							if (league.contains(sportName) && leaguePeriod.contains(typeName)) {
								// Example: chk_College_Game
								map.put("chk_" + league + "_" + leaguePeriod, "on");
							}
						}
					}
				}
			}

			// Setup the other attributes
			map.put("ScheduleText1", "");
			map.put("inetWagerNumber", this.inetWagerNumber);
			map.put("inetSportSelection", this.inetSportSelection);
		} catch (DocumentException de) {
			LOGGER.error(de.getMessage(), de);
		}

		LOGGER.info("Exiting parseMenu()");
		return map;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseGames(java.lang.String, java.lang.String, java.util.Map)
	 */
	@Override
	@SuppressWarnings("hiding")
	public <SiteTeamPackage> List<SiteTeamPackage> parseGames(String xhtml, String type, Map<String, String> inputFields) throws BatchException {
		LOGGER.info("Entering parseGames()");
		LOGGER.debug("xhtml: " + xhtml);
		LOGGER.debug("type: " + type);
		LOGGER.debug("InputFields: " + inputFields);
		List<?> events = null;

		int xindex = xhtml.indexOf("<html>");
		if (xindex != -1) {
			xhtml = xhtml.substring(xindex);
		}

		// Parse to get the Document
		final Document doc = parseXhtml(xhtml);
		LOGGER.debug("Document: " + doc);

		// Get forms hidden input fields and action URL
		final Elements forms = doc.select("form");
		for (Element form : forms) {
			String name = form.attr("name");
			if (name != null && name.equals("GameSelectionForm")) {
				final String[] types = new String[] { "hidden" };
				getAllElementsByType(form, "input", types, inputFields);

				// Get form action field
				inputFields.put("action", form.attr("action"));				

				// Now get the games
				final Elements elements = form.select("div table tbody tr");
				if (elements != null && elements.size() > 0) {
					events = getGameData(elements);
				}
			}
		}

		LOGGER.info("Exiting parseGames()");
		return (List<SiteTeamPackage>)events;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseEventSelection(java.lang.String, com.ticketadvantage.services.dao.sites.SiteTeamPackage, java.lang.String)
	 */
	@Override
	public Map<String, String> parseEventSelection(String xhtml, SiteTeamPackage siteTeamPackage, String type) throws BatchException {
		LOGGER.info("Entering parseEventSelection()");
		LOGGER.debug("xhtml: " + xhtml);
		final Map<String, String> map = new HashMap<String, String>();

		int xindex = xhtml.indexOf("<html>");
		if (xindex != -1) {
			xhtml = xhtml.substring(xindex);
		}

		// Parse to get the Document
		final Document doc = parseXhtml(xhtml);

		if (xhtml.contains("THE LINE (OR PRICE) HAS CHANGED")) {
			throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed", xhtml);
		}
		
		if (xhtml.contains("is greater than your amount available")) {
			throw new BatchException(BatchErrorCodes.MAX_WAGER_AMOUNT_REACHED, BatchErrorMessage.MAX_WAGER_AMOUNT_REACHED, "The risk amount is greater than your amount available", xhtml);
		}

		// A maximum wager amount of 500.00 USD is permitted for this wager on the internet
		if (xhtml.contains("A maximum wager amount of ")) {
			int bindex = xhtml.indexOf("A maximum wager amount of ");
			int eindex = xhtml.indexOf(" is permitted for this wager on the internet.");
			String error = xhtml.substring(bindex, eindex + " is permitted for this wager on the internet.".length());
			throw new BatchException(BatchErrorCodes.MAX_WAGER_AMOUNT_REACHED, BatchErrorMessage.MAX_WAGER_AMOUNT_REACHED, error, xhtml);
		}

		// Get all of the input fields
		final Element wagerVerificationForm = doc.getElementById("WagerAmountForm");
		if (wagerVerificationForm != null) {
			final String[] types = new String[] { "hidden", "text" };
			getAllElementsByType(wagerVerificationForm, "input", types, map);

			// Get form action field
			map.put("action", wagerVerificationForm.attr("action"));
		}

		LOGGER.info("Exiting parseEventSelection()");
		return map;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseTicketNumber(java.lang.String)
	 */
	@Override
	public String parseTicketNumber(String xhtml) throws BatchException {
		LOGGER.info("Entering parseTicketNumber()");
		String ticketNumber = null;

		if (xhtml.contains("THE LINE (OR PRICE) HAS CHANGED")) {
			throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed", xhtml);
		}
		
		if (xhtml.contains("is greater than your amount available")) {
			throw new BatchException(BatchErrorCodes.MAX_WAGER_AMOUNT_REACHED, BatchErrorMessage.MAX_WAGER_AMOUNT_REACHED, "The risk amount is greater than your amount available", xhtml);
		}

		// <div class="singleTxt plain large">Ticket Number: 
		// <b>127369581</b></div>
		int bindex = xhtml.indexOf("Ticket Number:");
		if (bindex != -1) {
			xhtml = xhtml.substring(bindex + "Ticket Number:".length());
			bindex = xhtml.indexOf("</b>");
			if (bindex != -1) {
				xhtml = xhtml.substring(0, bindex);
				xhtml = xhtml.replace("<b>", "");
				ticketNumber = xhtml.trim();
			}
		}

		// Check for a valid ticket number
		if (ticketNumber == null) {
			throw new BatchException(BatchErrorCodes.FAILED_TO_GET_TICKET_NUMBER, BatchErrorMessage.FAILED_TO_GET_TICKET_NUMBER, "Failed to get ticket number", xhtml);
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
	public Map<String, String> parseWagerType(String xhtml) throws BatchException {
		LOGGER.info("Entering parseWagerType()");
		LOGGER.debug("xhtml: " + xhtml);
		final Map<String, String> map = new HashMap<String, String>();

		int xindex = xhtml.indexOf("<html>");
		if (xindex != -1) {
			xhtml = xhtml.substring(xindex);
		}

		// Parse to get the Document
		final Document doc = parseXhtml(xhtml);

		if (xhtml.contains("THE LINE (OR PRICE) HAS CHANGED")) {
			throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed", xhtml);
		}

		if (xhtml.contains("is greater than your amount available")) {
			throw new BatchException(BatchErrorCodes.MAX_WAGER_AMOUNT_REACHED, BatchErrorMessage.MAX_WAGER_AMOUNT_REACHED, "The risk amount is greater than your amount available", xhtml);
		}

		// Get all of the input fields
		final Elements elements = doc.select("form");
		for (int x = 0; (elements != null && x < elements.size()); x++) {
			Element wagerVerificationForm = elements.get(x);
			if (wagerVerificationForm != null && "WagerVerificationForm".equals(wagerVerificationForm.attr("name"))) {
				final String[] types = new String[] { "hidden", "password" };
				getAllElementsByType(wagerVerificationForm, "input", types, map);
	
				// Get form action field
				map.put("action", wagerVerificationForm.attr("action"));
			}
		}

		// Check for a wager limit and change it 
		if (xhtml.contains("A maximum wager amount of ")) {
			int index = xhtml.indexOf("A maximum wager amount of ");
			if (index != -1) {
				xhtml = xhtml.substring(index + "A maximum wager amount of ".length());
				index = xhtml.indexOf(" USD");
				if (index != -1) {
					String wagerAmount = xhtml.substring(0, index);
					if (wagerAmount != null) {
						wagerAmount = wagerAmount.replaceAll(",", "");
						wagerAmount = wagerAmount.replaceAll(" ", "");
						map.put("wageramount", wagerAmount);
					}
				}
			}
		} else if (xhtml.contains("A minimum wager amount of")) {
			int index = xhtml.indexOf("A minimum wager amount of");
			if (index != -1) {
				xhtml = xhtml.substring(index + "A minimum wager amount of".length());
				index = xhtml.indexOf("USD");
				if (index != -1) {
					String wagerAmount = xhtml.substring(0, index);
					if (wagerAmount != null) {
						wagerAmount = wagerAmount.replaceAll(",", "");
						wagerAmount = wagerAmount.replaceAll(" ", "");
						map.put("wagerminamount", wagerAmount);
					}
				}
			}
		} else {
			// First get the Risk
			String wagerInfo = getHtmlFromAllElements(doc, ".risk");
			if (wagerInfo != null && wagerInfo.length() > 0) {
				// Risking 55.00   To Win 50.00  USD
				// <div class="risk">
		        //     Risking : 
		        //     <b>110.00</b> To Win : 
		        //     <b>100.00</b> USD
		        // </div>
				
				// Risk
				int beginIndex = wagerInfo.indexOf("Risking :");
				int endIndex = wagerInfo.indexOf("To Win :");
				if (beginIndex != -1 && endIndex != -1) {
					String risk = wagerInfo.substring(beginIndex + "Risking :".length(), endIndex);
					risk = risk.replace("&nbsp;", "");
					risk = risk.replace("<b>", "");
					risk = risk.replace("</b>", "");
					risk = risk.trim();
					map.put("risk", risk);
				} else {
					// Throw Exception
					throw new BatchException(BatchErrorCodes.ERROR_PARSING_WAGER_INFO, BatchErrorMessage.ERROR_PARSING_WAGER_INFO, "Cannot find Risking " + xhtml);				
				}
				
				// Win
				beginIndex = wagerInfo.indexOf("To Win :");
				endIndex = wagerInfo.indexOf("USD");
				if (beginIndex != -1 && endIndex != -1) {
					String win = wagerInfo.substring(beginIndex + "To Win :".length(), endIndex);
					win = win.replace("&nbsp;", "");
					win = win.replace("<b>", "");
					win = win.replace("</b>", "");
					win = win.trim();
					map.put("win", win);
				} else {
					// Throw Exception
					throw new BatchException(BatchErrorCodes.ERROR_PARSING_WAGER_INFO, BatchErrorMessage.ERROR_PARSING_WAGER_INFO, "Cannot find Win " + xhtml);				
				}
			} else {
				// Throw Exception
				throw new BatchException(BatchErrorCodes.ERROR_PARSING_WAGER_INFO, BatchErrorMessage.ERROR_PARSING_WAGER_INFO, "No element found for " + xhtml);
			}
		}

		LOGGER.info("Exiting parseWagerType()");
		return map;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parsePendingBets(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Set<PendingEvent> parsePendingBets(String xhtml, String accountName, String accountId) throws BatchException {
		LOGGER.info("Entering parsePendingBets()");
		LOGGER.debug("accountName: " + accountName);
		LOGGER.debug("accountId: " + accountId);
		Set<PendingEvent> pendingEvents = new HashSet<PendingEvent>();

		// Get the document object
		final Document doc = parseXhtml(xhtml);
		final Elements trs = doc.select("#content div table tr");
		LOGGER.debug("trs: " + trs);
		if (trs != null) {
			for (Element tr : trs) {
				int x = 0;
				String className = tr.attr("class");
				if (className != null && className.contains("colRep")) {
					Elements tds = tr.select("td");
					final PendingEvent pe = new PendingEvent();
					for (Element td : tds) {
						LOGGER.debug("td: " + td);
						pe.setAccountname(accountName);
						pe.setAccountid(accountId);
		
						switch (x++) {
							case 0:
								getTicketNumber(td, pe);
								break;
							case 1:
								getDateAccepted(td, pe);
								break;
							case 2:
								getEventType(td, pe);
								break;
							case 3:
								getRisk(td, pe);
								break;
							case 4:
								getWin(td, pe);
								break;
							case 5:
								getEventInfo(td, pe);
								break;
							default:
								break;
						}
					}
					pe.setDoposturl(false);
					LOGGER.debug("PendingEvent: " + pe);
					pendingEvents.add(pe);
				}
			}	
		}
		LOGGER.debug("PendingEvents: " + pendingEvents);

		LOGGER.info("Exiting parsePendingBets()");
		return pendingEvents;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#getGameData(org.jsoup.select.Elements)
	 */
	@Override
	@SuppressWarnings("unchecked")
	protected List<HeritageSportsEventPackage> getGameData(Elements elements) throws BatchException {
		LOGGER.info("Entering getGameData()");
		HeritageSportsEventPackage eventPackage = null;

		final List<HeritageSportsEventPackage> events = new ArrayList<HeritageSportsEventPackage>();
		if (elements != null) {
			HeritageSportsTeamPackage team1 = null;
			HeritageSportsTeamPackage team2 = null;
			int t = 0;
			for (int x = 0; x < elements.size(); x++) {
				// Loop through the elements and then check for the dates
				final Element element = elements.get(x);

				if (element != null) {
					String event_id = element.attr("event_id");
					LOGGER.debug("event_id: " + event_id);

					if (event_id != null && event_id.length() > 0) {
						if (t++ == 0) {
							eventPackage = new HeritageSportsEventPackage();
							team1 = new HeritageSportsTeamPackage();

							// Get team data
							getGameTeam(element.select("td"), team1);
							if (team1.getTeam() == null || team1.getTeam().length() == 0) {
								t = 0;
							} else {
								eventPackage.setId(team1.getId());	
							}
						} else {
							team2 = new HeritageSportsTeamPackage();
							getGameTeam(element.select("td"), team2);
							
							if (team1 != null && team1.getTeam() != null &&
								team2 != null && team2.getTeam() != null) {
								Date eventDate = null;
								String cDate = team1.gettDate();
								String tDate = team2.gettDate();
								LOGGER.debug("cDate: " + cDate);
								LOGGER.debug("tDate: " + tDate);

								try {
									final Calendar now = Calendar.getInstance();
									cDate = cDate.replaceAll("<br>", "");
									cDate = cDate + " " + String.valueOf(now.get(Calendar.YEAR)) + " ";
									tDate = tDate.replace("(", "");
									tDate = tDate.replace(")", "");
									cDate = cDate + tDate;
	
									eventDate = DATE_FORMAT.parse(cDate);
								} catch (ParseException pe) {
									LOGGER.error("ParseExeption for " + cDate, pe);
									// Throw an exception
									throw new BatchException(AppErrorMessage.SITE_PARSER_EXCEPTION  + " Date parsing exception for " + cDate);
								}
								team1.setEventdatetime(eventDate);
								team2.setEventdatetime(eventDate);
								final String dateOfEvent = cDate;
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

		LOGGER.info("List<HeritageSportsEventPackage>: " + events);
		LOGGER.info("Exiting getGameData()");
		return events;
	}

	/**
	 * 
	 * @param elements
	 * @param team
	 * @return
	 * @throws BatchException
	 */
	private int getGameTeam(Elements elements, HeritageSportsTeamPackage team) throws BatchException {
		LOGGER.info("Entering getGameTeam()");
		LOGGER.debug("SiteTeamPackage: " + team);
		int size = 0;

		if (elements != null && elements.size() == 8) {
			LOGGER.info("elements.size(): " + elements.size());
			size = 8;
			for (int x = 0; (elements != null && x < elements.size()); x++) {
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
					team = getMoneyLine(td, team);
					break;
				case 5:
					team = getTotal(td, team);
					break;
				case 6:
				case 7:
				default:
					break;
				}
			}
		} else if (elements != null) {
			LOGGER.warn("Unsunpported element size: " + elements.size());
		}

		LOGGER.info("Exiting getGameTeam()");
		return size;
	}

	/**
	 * 
	 * @param td
	 * @param team
	 * @return
	 */
	private HeritageSportsTeamPackage getDate(Element td, HeritageSportsTeamPackage team) {
		LOGGER.info("Entering getDate()");

		// Date String
		String date = td.html();
		if (date != null) {
			date = date.replaceAll("&nbsp;", "");
			date = date.trim();
			if (date.contains("<div")) {
				int bindex = date.indexOf("<div");
				date = date.substring(0, bindex);
				date = date.trim();
				date = date.replaceAll("\\n", "");
				date = date.replaceAll("\\r", "");
			}
			team.settDate(date);
		}

		LOGGER.info("Exiting getDate()");
		return team;
	}

	/**
	 * 
	 * @param td
	 * @param team
	 * @return
	 */
	private HeritageSportsTeamPackage getEventId(Element td, HeritageSportsTeamPackage team) {
		LOGGER.info("Entering getEventId()");

		String eventId = getHtmlFromElement(td, "div", 0, true);
		if (eventId != null) {
			eventId = eventId.replaceAll("\\.", "");
			team.setEventid(eventId);
			team.setId(Integer.parseInt(eventId));
		} else {
			eventId = td.html();
			eventId = eventId.trim();
			eventId = eventId.replaceAll("\\.", "");
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
	private HeritageSportsTeamPackage getTeam(Element td, HeritageSportsTeamPackage team) {
		LOGGER.info("Entering getTeam()");

		// Team String
		String teamName = td.html();
		if (teamName != null) {
			if (teamName.contains("<br>")) {
				int index = teamName.indexOf("<br");
				teamName = teamName.substring(0, index);
				Elements spans = td.select("span");
				if (spans != null && spans.size() > 0) {
					Element span = spans.get(0);
					String pitcher = span.html();
					if (pitcher != null) {
						pitcher = pitcher.trim();
						team.setPitcher(pitcher);
					}
				}
			}
			teamName = teamName.replaceAll("&nbsp;", "");
			teamName = teamName.replaceAll("\\.", "");
			teamName = teamName.trim();
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
	private HeritageSportsTeamPackage getSpread(Element td, HeritageSportsTeamPackage team) throws BatchException {
		LOGGER.info("Entering getSpread()");
		
		// First try to get the max
		String title = td.attr("title");
		if (title != null && title.length() > 0) {
			title = title.replace("$", "");
			title = title.replace(",", "");
			team.setSpreadMax(Integer.parseInt(title));
		}

		// First get the input field information
		Map<String, String> hashMap = parseInputField(td.select("input"), 0);
		if (hashMap != null && hashMap.size() > 0) {
			team.setGameSpreadInputId(hashMap.get("id"));
			team.setGameSpreadInputName(hashMap.get("name"));
			team.setGameSpreadInputValue(hashMap.get("value"));
		}

		// Get the select fields
		hashMap = parseSelectField(td.select("select"));
		if (hashMap != null && hashMap.size() > 0) {
			team.setGameSpreadSelectId(hashMap.get("id"));
			team.setGameSpreadSelectName(hashMap.get("name"));
		}

		final Elements options = td.select("select option");
		if (options != null && options.size() > 0) {
			team = (HeritageSportsTeamPackage)parseSpreadSelectOption(options, team, " ", null);
		} else {
			// -2½ +200; Now parse the data
			final Elements divs = td.select("div");
			if (divs != null && divs.size() > 0) {
				final Element div = divs.get(0);
				String data = getHtmlFromLastIndex(div, ">");
				if (data != null && data.length() > 0) {
					team = (HeritageSportsTeamPackage)parseSpreadData(reformatValues(data), 0, " ", null, team);
				}
			}
		}

		LOGGER.info("Exiting getSpread()");
		return team;
	}

	/**
	 * 
	 * @param td
	 * @param team
	 * @return
	 * @throws BatchException
	 */
	private HeritageSportsTeamPackage getTotal(Element td, HeritageSportsTeamPackage team) throws BatchException {
		LOGGER.info("Entering getTotal()");
		LOGGER.debug("td: " + td);

		// First try to get the max
		String title = td.attr("title");
		if (title != null && title.length() > 0) {
			title = title.replace("$", "");
			title = title.replace(",", "");
			team.setTotalMax(Integer.parseInt(title));
		}

		// First get the input field information
		Map<String, String> hashMap = parseInputField(td.select("input"), 0);
		if (hashMap != null && hashMap.size() > 0) {
			team.setGameTotalInputId(hashMap.get("id"));
			team.setGameTotalInputName(hashMap.get("name"));
			team.setGameTotalInputValue(hashMap.get("value"));
		}

		// Get the select fields
		hashMap = parseSelectField(td.select("select"));
		if (hashMap != null && hashMap.size() > 0) {
			team.setGameTotalSelectId(hashMap.get("id"));
			team.setGameTotalSelectName(hashMap.get("name"));
		}

		final Elements options = td.select("select option");
		if (options != null && options.size() > 0) {
			team = (HeritageSportsTeamPackage)parseTotalSelectOption(options, team, " ", null);
		} else {
			final Elements divs = td.select("div");
			if (divs != null && divs.size() > 0) {
				final Element div = divs.get(0);
				final String data = getHtmlFromLastIndex(div, ">");
				if (data != null && data.length() > 0) {
					team = (HeritageSportsTeamPackage)parseTotalData(reformatValues(data), 0, " ", null, team);
				}
			}
		}

		LOGGER.info("Exiting getTotal()");
		return team;
	}

	/**
	 * 
	 * @param td
	 * @param team
	 * @return
	 */
	private HeritageSportsTeamPackage getMoneyLine(Element td, HeritageSportsTeamPackage team) {
		LOGGER.info("Entering getMoneyLine()");
		LOGGER.debug("td: " + td);

		// First try to get the max
		String title = td.attr("title");
		if (title != null && title.length() > 0) {
			title = title.replace("$", "");
			title = title.replace(",", "");
			team.setMlMax(Integer.parseInt(title));
		}

		if (isMlb) {
			// First get the input field information
			final Elements inputs = td.select("input");
			LOGGER.debug("inputs: " + inputs);

			final Map<String, String> hashMap = parseInputField(inputs, 1);
			if (hashMap != null && hashMap.size() > 0) {
				team.setGameMLInputId(hashMap.get("id"));
				team.setGameMLInputName(hashMap.get("name"));
				final LinkedHashMap<String, String> mlValue = new LinkedHashMap<String, String>();
				mlValue.put("0", hashMap.get("value"));
				team.setGameMLInputValue(mlValue);
			}			
		} else {
			// First get the input field information
			final Map<String, String> hashMap = parseInputField(td.select("input"), 0);
			if (hashMap != null && hashMap.size() > 0) {
				team.setGameMLInputId(hashMap.get("id"));
				team.setGameMLInputName(hashMap.get("name"));
				final LinkedHashMap<String, String> mlValue = new LinkedHashMap<String, String>();
				mlValue.put("0", hashMap.get("value"));
				team.setGameMLInputValue(mlValue);
			}
		}

		if (isMlb) {
			// Parse ML Data
			final Elements divs = td.select("div");
			LOGGER.error("divs.size(): " + divs.size());

			if (divs != null && divs.size() > 1) {
				final Element div = divs.get(1);
				String divhtml = div.html();
				int indexof = divhtml.indexOf("maxlength=\"7\">");
				if (indexof != -1) {
					divhtml = divhtml.substring(indexof + "maxlength=\"7\">".length());
					indexof = divhtml.indexOf("<em>");
					if (indexof != -1) {
						divhtml = divhtml.substring(0, indexof);
						LOGGER.debug("ML data: " + divhtml);
						if (divhtml != null && divhtml.length() > 0) {
							team = (HeritageSportsTeamPackage)parseMlData(reformatValues(divhtml), 0, team);
						}
					}
				}
			} else {
				if (divs != null && divs.size() > 0) {
					final Element div = divs.get(0);
					String divhtml = div.html();
					int indexof = divhtml.indexOf("maxlength=\"7\">");
					if (indexof != -1) {
						divhtml = divhtml.substring(indexof + "maxlength=\"7\">".length());
						indexof = divhtml.indexOf("<em>");
						if (indexof != -1) {
							divhtml = divhtml.substring(0, indexof);
							LOGGER.debug("ML data: " + divhtml);
							if (divhtml != null && divhtml.length() > 0) {
								team = (HeritageSportsTeamPackage)parseMlData(reformatValues(divhtml), 0, team);
							}
						}
					}
				}
			}
		} else {
			// Parse ML Data
			final Elements divs = td.select("div");
			if (divs != null && divs.size() > 0) {
				final Element div = divs.get(0);
				final String data = getHtmlFromLastIndex(div, ">");
				LOGGER.debug("ML data: " + data);
				if (data != null && data.length() > 0) {
					team = (HeritageSportsTeamPackage)parseMlData(reformatValues(data), 0, team);
				}
			}
		}

		LOGGER.info("Exiting getMoneyLine()");
		return team;
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 */
	private void getTicketNumber(Element td, PendingEvent pe) {
		LOGGER.info("Entering getTicketNumber()");

		// <a href="javascript:void(0)" style="text-decoration:none" onclick="javascript:document.forms['WagerListForm'].ticketNumber.value = 112867671; document.forms['WagerListForm'].wagerNumber.value = 1; document.forms['WagerListForm'].gradeNumber.value = 0; document.forms['WagerListForm'].submit(); return false">
		//    <font class="item">112867671-1</font>
		// </a>
		final Elements fonts = td.select("a font");
		if (fonts != null && fonts.size() > 0 ) {
			pe.setTicketnum(super.reformatValues(fonts.get(0).html()));
		}

		LOGGER.info("Exiting getTicketNumber()");
	}
	
	/**
	 * 
	 * @param td
	 * @param pe
	 */
	private void getDateAccepted(Element td, PendingEvent pe) {
		LOGGER.info("Entering getDateAccepted()");

		// <td nowrap="nowrap">6/16/2017 0:26 AM</td>
		String html = td.html();
		if (html != null && html.length() > 0) {
			LOGGER.info("html: " + html);
			pe.setDateaccepted(super.reformatValues(html + " " + super.timezone));
		}

		LOGGER.info("Exiting getDateAccepted()");
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 */
	private void getEventType(Element td, PendingEvent pe) {
		LOGGER.info("Entering getEventType()");

		// <td>Money Line</td>
		final String html = td.html();
		if (html != null && html.length() > 0) {
			LOGGER.info("html: " + html);
			if ("Money Line".equals(html)) {
				pe.setEventtype("ml");
			} else if ("Spread".equals(html)) {
				pe.setEventtype("spread");
			} else {
				pe.setEventtype("total");
			}
		}

		LOGGER.info("Exiting getEventType()");
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 */
	private void getRisk(Element td, PendingEvent pe) {
		LOGGER.info("Entering getRisk()");
		LOGGER.debug("td: " + td);

		// <td align="RIGHT">39.25</td>
		String html = td.html();
		if (html != null && html.length() > 0) {
			html = super.reformatValues(html);
			LOGGER.info("html: " + html);
			pe.setRisk(html);
		}

		LOGGER.info("Exiting getRisk()");
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 */
	private void getWin(Element td, PendingEvent pe) {
		LOGGER.info("Entering getWin()");
		LOGGER.debug("td: " + td);

		// <td align="RIGHT">  25.00</td>
		String html = td.html();
		if (html != null && html.length() > 0) {
			html = super.reformatValues(html);
			LOGGER.info("html: " + html);
			pe.setWin(html);
		}

		LOGGER.info("Exiting getWin()");
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 */
	private void getEventInfo(Element td, PendingEvent pe) {
		LOGGER.info("Entering getEventInfo()");

		// <td style="text-align:left;white-space:normal;padding-left:10px;">
		// Pregame Baseball
		// <br>
		//   957 Washington Nationals -157 for Game 
		// </td>
		String sportName = "";
		String html = td.html();
		int index = html.indexOf("<br>");
		if (index != -1) {
//			pe.setGamesport(html.substring(0, index).trim());
			sportName = html.substring(0, index).trim().replace("Pregame ", "");
			// Set the game sport
			pe.setGamesport(super.reformatValues(sportName));
			html = html.substring(index + 4);
		}

		String sportType = "";
		index = html.indexOf(" W ");
		if (index != -1) {
			sportType = "W";
			html = html.replace(" W ", "");
		}
		// Set the game type
		pe.setGametype(getGameType(sportName, sportType));

		// Get the Rotation ID
		html = getRotationId(html, pe);

		// Now parse the team
		getEventInfo(html, pe);

		// Now parse line type
		getLineType(html, pe);

		LOGGER.info("Exiting getEventInfo()");
	}

	/**
	 * 
	 * @param html
	 * @param pe
	 * @return
	 */
	private String getRotationId(String html, PendingEvent pe) {
		LOGGER.info("Entering getRotationId()");

		if (html != null && html.length() > 0) {
			int spaceIndex = html.indexOf(" ");
			if (spaceIndex != -1) {
				// Get the Rotation ID
				pe.setRotationid(super.reformatValues(html.substring(0, spaceIndex)));
				html = html.substring(spaceIndex + 1);
			}
		}

		LOGGER.info("Exiting getRotationId()");
		return html;
	}

	/**
	 * 
	 * @param html
	 * @param pe
	 */
	private void getEventInfo(String html, PendingEvent pe) {
		LOGGER.info("Entering getEventInfo()");
		LOGGER.debug("html: " + html);

		if ("spread".equals(pe.getEventtype())) {
			String juiceString = null;
			String lineString = null;
			int forIndex = html.indexOf(" for ");
			if (forIndex != -1) {
				html = html.substring(0, forIndex);
				LOGGER.debug("html: " + html);
				int juiceIndex = html.lastIndexOf(" ");
				if (juiceIndex != -1) {
					juiceString = html.substring(juiceIndex + 1);
					html = html.substring(0, juiceIndex);
					LOGGER.debug("html: " + html);
					int lineIndex = html.lastIndexOf(" ");
					if (lineIndex != -1) {
						LOGGER.debug("html: " + html);
						lineString = html.substring(lineIndex + 1);
						LOGGER.debug("lineString: " + lineString);
						html = html.substring(0, lineIndex);
						pe.setTeam(html);
					}
				}
			}

			// Now get the line Information
			if (lineString != null) {
				LOGGER.debug("lineString: " + lineString);
				getLineInformation(lineString, pe);	
			}

			// Now get the juice Information
			if (juiceString != null) {
				getJuiceInformation(juiceString, pe);
			}
		} else if ("total".equals(pe.getEventtype())) {
			int underIndex = html.indexOf("under");
			int overIndex = html.indexOf("over");

			if (underIndex != -1) {
				pe.setTeam(html.substring(0, underIndex - 1));
				pe.setLineplusminus("u");

				html = html.substring(underIndex + 6);
				LOGGER.debug("html: " + html);
				int lineIndex = html.indexOf(" ");
				if (lineIndex != -1) {
					pe.setLine(super.reformatValues(html.substring(0, lineIndex)));
					html = html.substring(lineIndex + 1);
					int endIndex = html.indexOf(" ");
					if (endIndex != -1) {
						getJuiceInformation(html.substring(0, endIndex), pe);
					}
				}
			} else if (overIndex != -1) {
				pe.setTeam(html.substring(0, overIndex - 1));
				pe.setLineplusminus("o");

				html = html.substring(overIndex + 5);
				int lineIndex = html.indexOf(" ");
				if (lineIndex != -1) {
					pe.setLine(super.reformatValues(html.substring(0, lineIndex)));
					html = html.substring(lineIndex + 1);
					int endIndex = html.indexOf(" ");
					if (endIndex != -1) {
						getJuiceInformation(html.substring(0, endIndex), pe);
					}
				}
			}
		} else if ("ml".equals(pe.getEventtype())) {
			String juiceString = null;
			int forIndex = html.indexOf(" for ");
			if (forIndex != -1) {
				html = html.substring(0, forIndex);
				int juiceIndex = html.lastIndexOf(" ");
				if (juiceIndex != -1) {
					juiceString = html.substring(juiceIndex + 1);
					html = html.substring(0, juiceIndex);
					pe.setTeam(html);
				}
			}

			// Now get the line Information
			if (juiceString != null) {
				getJuiceInformation(juiceString, pe);		
			}
		}

		LOGGER.info("Exiting getEventInfo()");
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 * @return
	 */
	private String getJuiceInformation(String html, PendingEvent pe) {
		LOGGER.info("Entering getJuiceInformation()");
		LOGGER.debug("html: " + html);

		int plusdIndex = html.indexOf("+");
		int minusIndex = html.indexOf("-");
		if (plusdIndex != -1 || minusIndex != -1) {
			pe.setJuiceplusminus(html.substring(0, 1));
			pe.setJuice(super.reformatValues(html.substring(1)));
			if ("ml".equals(pe.getEventtype())) {
				pe.setLineplusminus(html.substring(0, 1));
				pe.setLine(super.reformatValues(html.substring(1)));
			}
		} else {
			int pkIndex = html.indexOf("pk");
			int PKIndex = html.indexOf("PK");
			int evIndex = html.indexOf("ev");
			int EVIndex = html.indexOf("EV");
			if (pkIndex != -1 || PKIndex != -1 || evIndex != -1 || EVIndex != -1) {
				pe.setJuiceplusminus("+");
				pe.setJuice("100");
				if ("ml".equals(pe.getEventtype())) {
					pe.setLineplusminus("+");
					pe.setLine("100");
				}
			}
		}

		LOGGER.info("Exiting getJuiceInformation()");
		return html;
	}

	/**
	 * 
	 * @param html
	 * @param pe
	 * @return
	 */
	private String getLineType(String html, PendingEvent pe) {
		LOGGER.info("Entering getLineType()");
		LOGGER.debug("html: " + html);

		int forIndex = html.indexOf("for ");
		if (forIndex != -1) {
			html = html.substring(forIndex + 4);
			if (html != null && html.length() > 0) {
				html = super.reformatValues(html.trim());
				if ("1st Half".equals(html) || "1st 5 Innings".equals(html) || "1st Period".equals(html)) {
					pe.setLinetype("first");
				} else if ("2nd Half".equals(html) || "2nd Period".equals(html)) {
					pe.setLinetype("second");
				} else {
					pe.setLinetype("game");
				}
			} else {
				pe.setLinetype("game");
			}
		}

		LOGGER.info("Exiting getLineType()");
		return html;
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 * @return
	 */
	private String getLineInformation(String html, PendingEvent pe) {
		LOGGER.info("Entering getLineInformation()");
		LOGGER.debug("html: " + html);

		if ("spread".equals(pe.getEventtype())) {
			int plusdIndex = html.indexOf("+");
			int minusIndex = html.indexOf("-");
			if (plusdIndex != -1 || minusIndex != -1) {
				pe.setLineplusminus(html.substring(0, 1));
				String lineData = html.substring(1);
				LOGGER.debug("lineData: " + lineData + " end");
				pe.setLine(super.reformatValues(lineData));
			} else {
				int pkIndex = html.indexOf("pk");
				int PKIndex = html.indexOf("PK");
				int evIndex = html.indexOf("ev");
				int EVIndex = html.indexOf("EV");
				if (pkIndex != -1 || PKIndex != -1 || evIndex != -1 || EVIndex != -1) {
					pe.setLineplusminus("+");
					pe.setLine("100");
				}
			}	
		} else if ("total".equals(pe.getEventtype())) {
			int underIndex = html.indexOf("under");
			int overIndex = html.indexOf("over");
			if (underIndex != -1) {
				pe.setLineplusminus("u");
			} else if (overIndex != -1) {
				pe.setLineplusminus("o");
			}
			int firstSpace = html.indexOf(" ");
			if (firstSpace != -1) {
				pe.setLineplusminus(html.substring(0, 1));
				html = html.substring(1);
				html = parseSpace(html, pe);
			}
		} else if ("ml".equals(pe.getEventtype())) {
			int plusdIndex = html.indexOf("+");
			int minusIndex = html.indexOf("-");
			if (plusdIndex != -1 || minusIndex != -1) {
				pe.setLineplusminus(html.substring(0, 1));
				html = html.substring(1);
				int spaceIndex = html.indexOf(" ");
				if (spaceIndex != -1) {
					pe.setLine(super.reformatValues(html.substring(0, spaceIndex)));
					html = html.substring(spaceIndex + 1);
				}
			}
		}

		LOGGER.info("Exiting getLineInformation()");
		return html;
	}

	/**
	 * 
	 * @param html
	 * @param pe
	 * @return
	 */
	private String parseSpace(String html, PendingEvent pe) {
		LOGGER.info("Entering parseSpace()");
		LOGGER.debug("html: " + html);

		int spaceIndex = html.indexOf(" ");
		if (spaceIndex != -1) {
			pe.setLine(super.reformatValues(html.substring(0, spaceIndex)));
			html = html.substring(spaceIndex + 1);
			spaceIndex = html.indexOf(" ");
			if (spaceIndex != -1) {
				pe.setJuiceplusminus(html.substring(0, 1));
				pe.setJuice(super.reformatValues(html.substring(1, spaceIndex)));
				html = html.substring(spaceIndex + 1);
			}
		}
		
		LOGGER.info("Exiting parseSpace()");
		return html;
	}

	/**
	 * 
	 * @param sportName
	 * @param sportType
	 * @return
	 */
	private String getGameType(String sportName, String sportType) {
		LOGGER.info("Entering getGameType()");
		LOGGER.info("sportName: " + sportName);
		LOGGER.info("sportType: " + sportType);

		if ("Football".equals(sportName)) {
			if ("".equals(sportType)) {
				return "NFL";
			} else {
				return "NCAA Football";
			}			
		} else if ("Basketball".equals(sportName)) {
			if ("".equals(sportType)) {
				return "NBA";
			} else if ("W".equals(sportType)) {
				return "WNBA";
			} else {
				return "NCAA Basketball";	
			}
		} else if ("Baseball".equals(sportName)) {
			return "MLB";
		} else if ("Hockey".equals(sportName)) {
			return "NHL";
		}

		LOGGER.info("Exiting getGameType()");
		return "";
	}
}