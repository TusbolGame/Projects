/**
 * 
 */
package com.ticketadvantage.services.dao.sites.tdsportsone;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ibm.icu.util.Calendar;
import com.ticketadvantage.services.dao.sites.tdsports.TDSportsEventPackage;
import com.ticketadvantage.services.dao.sites.tdsports.TDSportsParser;
import com.ticketadvantage.services.dao.sites.tdsports.TDSportsTeamPackage;
import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.PendingEvent;

/**
 * @author jmiller
 *
 */
public class TDSportsOneParser extends TDSportsParser {
	private static final Logger LOGGER = Logger.getLogger(TDSportsOneParser.class);
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd yyyy hh:mm a z");
	private boolean isMlb;

	/**
	 * Constructor
	 */
	public TDSportsOneParser() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.tdsports.TDSportsParser#parsePendingBets(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Set<PendingEvent> parsePendingBets(String xhtml, String accountName, String accountId) throws BatchException {
		LOGGER.info("Entering parsePendingBets()");
		LOGGER.debug("accountName: " + accountName);
		LOGGER.debug("accountId: " + accountId);
		Set<PendingEvent> pendingEvents = new HashSet<PendingEvent>();
		
		if (xhtml != null) {
			xhtml = xhtml.replace("Â", "");
		}

		// Get the document object
		final Document doc = parseXhtml(xhtml);
		Elements trs = doc.select("#content table tr");

		LOGGER.debug("trs: " + trs);
		if (trs != null) {
			PendingEvent pe = null;
			for (Element tr : trs) {
				String trhtml = tr.html();

				if (trhtml.contains("TDTicketNumber")) {
					pe = new PendingEvent();
					pe.setDatecreated(new Date());
					pe.setDatemodified(new Date());
					pe.setCustomerid(accountName);
					pe.setInet(accountName);
					pe.setAccountname(accountName);
					pe.setAccountid(accountId);
					pe.setPosturl("Something here");
					pe.setDoposturl(true);

					final Elements trstix = tr.select(".TDTicketNumber");
					if (trstix != null) {
						pe.setTicketnum(trstix.get(0).html().replace("STRAIGHT BET - Ticket#:", "").replaceAll("&nbsp;", "").trim());
					}
				} else if (trhtml.contains("TDPlacedDate")) {
					final Elements trdivs = tr.select(".TDPlacedDate div");
					if (trdivs != null) {
						pe.setDateaccepted(trdivs.get(0).html().replace("Placed:", "").trim());
					}
				} else if (trhtml.contains("TDBetDetail")) {
					final Elements trdivs = tr.select(".TDBetDetail");
					if (trdivs != null) {
						String trdata = trdivs.get(0).html();
						trdata = trdata.replaceAll("Game Date:", "").trim();
						int index = trdata.indexOf("<br>");

						if (index != -1) {
							String gamedate = trdata.substring(0, index);

							try {
								Calendar now = Calendar.getInstance();
								int year = now.get(Calendar.YEAR);
								pe.setEventdate(year + " " + gamedate + " " + determineTimeZone(super.timezone));

								try {
									pe.setGamedate(fixDate(year + " " + gamedate + " " + determineTimeZone(super.timezone), PENDING_DATE_FORMAT.get()));
								} catch (ParseException pe1) {
									pe.setGamedate(fixDate(year + " " + gamedate + " " + determineTimeZone(super.timezone), PENDING_DATE_FORMAT_2.get()));
								}
							} catch (ParseException pee) {
								LOGGER.error("gamedate: " + gamedate + " " + determineTimeZone(super.timezone));
								LOGGER.error(pee.getMessage(), pee);
								pe.setGamedate(new Date());
							} catch (NumberFormatException nfe) {
								LOGGER.error("gamedate: " + gamedate + " " + determineTimeZone(super.timezone));
								LOGGER.error(nfe.getMessage(), nfe);
								pe.setGamedate(new Date());
							}

							try {
								// [715] TEXAS TECH -3½-110
								this.getGameInfo(trdata, pe);
							} catch (Throwable t) {
								LOGGER.warn(t.getMessage(), t);
							}

							// Add PE
							pendingEvents.add(pe);
						}
					}
				} else if (trhtml.contains("PostWager-amount")) {
					final Elements tds = tr.select("td");
					if (tds != null) {
						String amounts = tds.get(0).html().replace("<br>", "").trim();
						int index = amounts.indexOf("To Win");

						if (index != -1) {
							pe.setRisk(amounts.substring(0, index).trim());
							pe.setWin(amounts.substring(index + "To Win".length()).trim());
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
	 * @param td
	 * @param pe
	 * @throws BatchException
	 */
	protected void getGameInfo(String html, PendingEvent pe) throws BatchException {
		LOGGER.info("Entering getGameInfo()");
		LOGGER.debug("html: " + html);

		//
		// Spread
		//
		// STRAIGHT BET
		// <br>
		// [270] NO SAINTS (PRESEASON) -2-105
		// <br>

		// 
		// Total
		//
		// STRAIGHT BET
		// <br>
		// [931] TOTAL o9EV (COL ROCKIES vrs KC ROYALS)
		// <br>
		// ( JON GRAY / DANNY DUFFY )
		// <br>

		//
		// Total
		//
		// STRAIGHT BET
		// <br>
		// [293] TOTAL o62-120 (B+½)
		// <br>
		// (HAWAII vrs MASSACHUSETTS)
		// <br>

		//
		// MoneyLine
		//
		// STRAIGHT BET
		// <br>
		// [932] KC ROYALS -122
		// <br>
		// ( JON GRAY / DANNY DUFFY )
		// <br>

		int index = html.indexOf("<br>");
		if (index != -1) {
			html = html.substring(index + 4);
			LOGGER.debug("html: " + html);

			if (html.contains("TEASER")) {
				pe.setEventtype("teaser");
				return;
			}

			// First get the rotation ID
			int bindex = html.indexOf("[");
			int eindex = html.indexOf("]");
			if (bindex != -1 && eindex != -1) {
				String rotationId = html.substring(bindex + 1, eindex);
				LOGGER.debug("rotationID: " + rotationId);
				pe.setRotationid(rotationId);

				// Check what type it is
				if (rotationId.length() == 1 || rotationId.length() == 2 || rotationId.length() == 3 || rotationId.length() == 6) {
					pe.setLinetype("game");
				} else if (rotationId.startsWith("1") && rotationId.length() == 4) {
					pe.setLinetype("first");
				} else if (rotationId.startsWith("2") && rotationId.length() == 4) {
					pe.setLinetype("second");
				} else if (rotationId.startsWith("3") && rotationId.length() == 4) {
					// Determine if this is a Quarter, Period or something else
					if (html.contains("1Q")) {
						pe.setEventtype("unsupported");
						throw new BatchException(BatchErrorCodes.UNSUPPORTED_SPORT_TYPE, BatchErrorMessage.UNSUPPORTED_SPORT_TYPE, html);
					} else {
						pe.setLinetype("third");
					}
				} else if (rotationId.length() >= 4) {
					pe.setEventtype("unsupported");
					throw new BatchException(BatchErrorCodes.UNSUPPORTED_SPORT_TYPE, BatchErrorMessage.UNSUPPORTED_SPORT_TYPE, html);
				}

				html = html.substring(eindex + 2); // Skip to next thing
				LOGGER.debug("html: " + html);
				bindex = html.indexOf("TOTAL ");
				if (bindex != -1) {
					getTotal(html, bindex, pe);
				} else {
					bindex = html.indexOf(" u");
					if (bindex != -1) {
						String tempHtml = html;
						getSpreadTotalMoneyline(html, pe);
						index = tempHtml.indexOf("<br>");
						if (index != -1) {
							tempHtml = tempHtml.substring(index + 4);
							tempHtml = tempHtml.replace("<br>", "").replace("(", "").replace(")", "").trim();
							LOGGER.debug("tempHtml: " + tempHtml);
							pe.setPitcher(tempHtml);
						}
					} else {
						bindex = html.indexOf(" o");
						if (bindex != -1) {
							String tempHtml = html;
							getSpreadTotalMoneyline(html, pe);
							index = tempHtml.indexOf("<br>");
							if (index != -1) {
								tempHtml = tempHtml.substring(index + 4);
								tempHtml = tempHtml.replace("<br>", "").replace("(", "").replace(")", "").trim();
								LOGGER.debug("tempHtml: " + tempHtml);
								pe.setPitcher(tempHtml);
							}
						} else {
							getSpreadMoneyline(html, pe);
						}
					}
				}
			}
		}

		LOGGER.info("Exiting getGameInfo()");
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
			if (sportName.contains("MLB")) {
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
		map = findMenu(doc.select("div div div div label div"), map, type, sport, "span", "input");

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
		Elements elements = doc.select(".container div div center table tbody tr");
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