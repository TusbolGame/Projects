/**
 * 
 */
package com.ticketadvantage.services.dao.sites.heritagesports;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ticketadvantage.services.errorhandling.AppErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;

/**
 * @author jmiller
 *
 */
public class GsbettingParser extends HeritageSportsParser {
	private static final Logger LOGGER = Logger.getLogger(GsbettingParser.class);
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("E MM/dd yyyy hh:mma Z");

	/**
	 * Constructor
	 */
	public GsbettingParser() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
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
		final Map<String, String> map = new HashMap<String, String>();
		int xindex = xhtml.indexOf("<html>");
		if (xindex != -1) {
			xhtml = xhtml.substring(xindex);
		}

		// Get the document object
		final Document doc = parseXhtml(xhtml);
		final Elements trs = doc.select(".sportsTable tbody tr td table tbody tr");
		
		for (Element tr : trs) {
			final Elements tds = tr.select("td");
			if (tds != null && tds.size() > 1) {
				final Element td1 = tds.get(0);
				final Element td2 = tds.get(1);
				String league = null;
				String typeName = null;
				
				final Elements input1s = td1.select("input");
				if (input1s != null && input1s.size() > 0) {
					final Element input = input1s.get(0);
					league = input.attr("name");
				}

				if (league != null && league.equals(sport[0])) {
					final Elements divs = td2.select("font div");
					if (divs != null && divs.size() > 0) {
						final Element div = divs.get(0);
						final Elements inputs = div.select("input");
						final Elements fonts = div.select("font");
						
						if (inputs != null && inputs.size() > 0 && fonts != null) {
							int size = inputs.size();
							for (int x = 0; x < size; x++) {
								typeName = inputs.get(x).attr("name");
								final Element font = fonts.get(x);
								String fhtml = font.html();
	
								if (fhtml != null) {
									fhtml = fhtml.trim();
									if (fhtml.equals(type[0])) {
										final Element inetWagerNumber = doc.getElementById("inetWagerNumber");
										final Element inetSportSelection = doc.getElementById("inetSportSelection");
										map.put("inetWagerNumber", inetWagerNumber.attr("value"));
										String inetSportSelectionValue = inetSportSelection.attr("value");
										if (inetSportSelectionValue != null && inetSportSelectionValue.length() > 0
												&& !"undefined".equals(inetSportSelectionValue)) {
											map.put("inetSportSelection", inetSportSelectionValue);
										} else {
											map.put("inetSportSelection", "");
										}
										map.put(league, "on");
										map.put(typeName, "on");
										map.put("submit1", "Continue");
									}
								}
							}
						}						
					}
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
				final Elements elements = form.select("table tbody tr");
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
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#getGameData(org.jsoup.select.Elements)
	 */
	@Override
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
					String bgcolor = element.attr("bgcolor");
					LOGGER.debug("bgcolor: " + bgcolor);

					if (bgcolor != null && bgcolor.length() > 0) {
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
		int bindex = xhtml.indexOf("Ticket Number - ");
		if (bindex != -1) {
			xhtml = xhtml.substring(bindex + "Ticket Number - ".length());
			bindex = xhtml.indexOf("</FONT>");
			if (bindex != -1) {
				xhtml = xhtml.substring(0, bindex);
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
		} else if (elements != null && elements.size() == 9) {
			LOGGER.info("elements.size(): " + elements.size());
			size = 9;
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
				case 4:
					team = getSpread(td, team);
					break;
				case 5:
					team = getMoneyLine(td, team);
					break;
				case 6:
					team = getTotal(td, team);
					break;
				case 3:
				case 7:
				case 8:
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
		String date = getHtmlFromElement(td, "font", 0, true);
		LOGGER.debug("date: " + date);
		if (date != null) {
			date = date.replaceAll("\\.", "");
			team.settDate(date);
		} else {
			date = td.html();
			date = date.trim();
			date = date.replaceAll("\\.", "");
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

		String eventId = getHtmlFromElement(td, "font", 0, true);
		LOGGER.debug("eventId: " + eventId);
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
		String teamName = getHtmlFromElement(td, "b", 0, true);
		LOGGER.debug("teamName: " + teamName);
		if (teamName != null) {
			teamName = teamName.replaceAll("\\.", "");
			team.setTeam(teamName);
		} else {
			teamName = td.html();
			teamName = teamName.replaceAll("\\.", "");
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
			LOGGER.debug("title: " + title);
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
			final Elements bs = td.select("font font b");
			if (bs != null && bs.size() > 0) {
				final Element b = bs.get(0);
				String data = getHtmlFromLastIndex(b, ">");
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
			final Elements bs = td.select("font font b");
			if (bs != null && bs.size() > 0) {
				final Element b = bs.get(0);
				String data = getHtmlFromLastIndex(b, ">");
				if (data != null && data.length() > 0) {
					data = data.replace("Over ", "o");
					data = data.replace("Under ", "u");
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

		// First get the input field information
		final Map<String, String> hashMap = parseInputField(td.select("input"), 0);
		if (hashMap != null && hashMap.size() > 0) {
			team.setGameMLInputId(hashMap.get("id"));
			team.setGameMLInputName(hashMap.get("name"));
			LinkedHashMap<String, String> mlValue = new LinkedHashMap<String, String>();
			mlValue.put("0", hashMap.get("value"));
			team.setGameMLInputValue(mlValue);
		}

		// Parse ML Data
		final Elements bs = td.select("font font b");
		if (bs != null && bs.size() > 0) {
			final Element b = bs.get(0);
			String data = getHtmlFromLastIndex(b, ">");
			if (data != null && data.length() > 0) {
				team = (HeritageSportsTeamPackage)parseMlData(reformatValues(data), 0, team);
			}
		}

		LOGGER.info("Exiting getMoneyLine()");
		return team;
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
			String wagerInfo = getHtmlFromAllElements(doc, ".PeriodDescription");
			if (wagerInfo != null && wagerInfo.length() > 0) {
				// Risking 55.00   To Win 50.00  USD
				// <div class="risk">
		        //     Risking : 
		        //     <b>110.00</b> To Win : 
		        //     <b>100.00</b> USD
		        // </div>
				// <font class='PeriodDescription'> 
				// <br />  Wager type : Straight Bet(s)
				// <br />
				// <br />  Select #1  : NCAA Basketball
				// <br />               Washington  3/1/2018 11:00 PM - (EST)  
				// <br />               Spread -3ݠ-113 for Game   
				// <br />               Risking 113   To Win 100  USD  </font>
				// </pre>
				int beginIndex = wagerInfo.indexOf("Risking ");
				int endIndex = wagerInfo.indexOf("To Win ");
				if (beginIndex != -1 && endIndex != -1) {
					String risk = wagerInfo.substring(beginIndex + "Risking ".length(), endIndex);
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
				beginIndex = wagerInfo.indexOf("To Win ");
				endIndex = wagerInfo.indexOf("USD");
				if (beginIndex != -1 && endIndex != -1) {
					String win = wagerInfo.substring(beginIndex + "To Win ".length(), endIndex);
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
}