/**
 * 
 */
package com.ticketadvantage.services.dao.sites.wagershack;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ticketadvantage.services.dao.sites.SelectOptionData;
import com.ticketadvantage.services.dao.sites.SiteEventPackage;
import com.ticketadvantage.services.dao.sites.SiteTeamPackage;
import com.ticketadvantage.services.dao.sites.tdsports.TDSportsParser;
import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.PendingEvent;

/**
 * @author jmiller
 *
 */
public class WagerShackMobileParser extends TDSportsParser {
	private static final Logger LOGGER = Logger.getLogger(WagerShackMobileParser.class);
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a z");
	// May 17 08:45 AM
	private static final SimpleDateFormat PENDING_DATE_FORMAT = new SimpleDateFormat("MMM dd hh:mm a z yyyy");

	/**
	 * Constructor
	 */
	public WagerShackMobileParser() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			WagerShackMobileParser wsmp = new WagerShackMobileParser();
			wsmp.setTimezone("ET");
			final Date gameDate = wsmp.PENDING_DATE_FORMAT.parse("May 17 07:10 PM EDT 2019");
			LOGGER.error("gameDate: " + gameDate);
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}		
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
		final Elements trs = doc.select(".inner-content div div div table tbody tr");
		LOGGER.debug("trs: " + trs);

		if (trs != null) {
			for (Element tr : trs) {
				PendingEvent pe = null;

				try {
					pe = new PendingEvent();
					Elements tds = tr.select("td");
					if (tds != null && tds.size() > 0) {
						LOGGER.debug("tds.size(): " + tds.size());
						int size = 0;

						for (Element td : tds) {
							LOGGER.debug("td: " + td);
							pe.setAccountname(accountName);
							pe.setAccountid(accountId);
							pe.setCustomerid(accountName);
							pe.setInet(accountName);

							switch (size++) {
								case 0:
									// get sport
									getSport(td, pe);
									break;
								case 1:
									// get description
									getGameInfo(td, pe);
									break;
								case 2:
									// get risk/win
									getRiskWinInfo(td, pe);
									break;
								case 3:
									// get date placed
									getDatePlaced(td, pe);
									break;
								case 4:
									// get game date/ticket #
									getGameDateAndTicketNumber(td, pe);
									pendingEvents.add(pe);
									break;
								default:
									break;
							}
						}
					}
				} catch (Throwable t) {
					LOGGER.debug(t.getMessage(), t);
				}
			}
		}

		// Size
		LOGGER.debug("pendingEvents.size(): " + pendingEvents.size());
		LOGGER.info("Exiting parsePendingBets()");
		return pendingEvents;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseIndex(java.lang.String)
	 */
	@Override
	public Map<String, String> parseIndex(String xhtml) throws BatchException {
		LOGGER.info("Entering parseIndex()");
		final Map<String, String> map = new HashMap<String, String>();

		// Get the document object
		final Document doc = parseXhtml(xhtml);

		// Get hidden input fields
		final String[] types = new String[] { "hidden", "text", "password", "submit" };
		final Elements forms = doc.select("form");
		for (int x = 0; (forms != null && x < forms.size()); x++) {
			final Element form = forms.get(x);

			// Get form action field
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
		Map<String, String> map = new HashMap<String, String>();

		// Parse the xhtml
		final Document doc = parseXhtml(xhtml);

		// Get the form information
		final Elements forms = doc.select("form");
		for (int x = 0; (forms != null && x < forms.size()); x++) {
			final Element form = forms.get(x);
			if (form != null) {
				// Get form action field
				map.put("action", form.attr("action"));
				map = getAllElementsByNameByElement(form, "input", "value", map);
			}
		}

		LOGGER.info("Exiting parseLogin()");
		return map;
	}

	/**
	 * 
	 * @param xhtml
	 * @return
	 * @throws BatchException
	 */
	public Map<String, String> parseSports(String xhtml) throws BatchException {
		LOGGER.info("Entering parseSports()");
		Map<String, String> map = new HashMap<String, String>();

		// Parse the xhtml
		final Document doc = parseXhtml(xhtml);

		// Get the form information
		final String[] types = new String[] { "hidden" };
		getAllElementsByType(doc, "input", types, map);

		LOGGER.info("Exiting parseSports()");
		return map;
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
		map = findMenu(doc.select(".panel"), map, type, sport, ".panel-heading h4 a", ".panel-body div label");
		
		LOGGER.info("Exiting parseMenu()");
		return map;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseGames(java.lang.String, java.lang.String, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <SiteEventPackage> List<SiteEventPackage> parseGames(String xhtml, String type, Map<String, String> inputFields) throws BatchException {
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
		Elements elements = doc.select(".gameOddsBox");
		if (elements != null && elements.size() > 0) {
			events = getGameData(elements, inputFields);
		}

		LOGGER.info("Exiting parseGames()");
		return (List<SiteEventPackage>)events;
	}

	/**
	 * 
	 * @param xhtml
	 * @return
	 * @throws BatchException
	 */
	public Map<String, String> parseWagerTypes(String xhtml) throws BatchException {
		LOGGER.info("Entering parseWagerTypes()");
		LOGGER.debug("xhtml: " + xhtml);
		final Map<String, String> map = new HashMap<String, String>();

		// Parse to get the Document
		final Document doc = parseXhtml(xhtml);
		
		if (xhtml.contains("alert alert-danger")) {
			String errorMessage = doc.select(".alert p").html();

			if (errorMessage != null && errorMessage.length() > 0) {
				// Check for a wager limit and change it 
				if (errorMessage.contains("minimum required")) {
					throw new BatchException(BatchErrorCodes.MIN_WAGER_AMOUNT_NOT_REACHED, BatchErrorMessage.MIN_WAGER_AMOUNT_NOT_REACHED, errorMessage, xhtml);
				}
				// Check for a wager limit and change it 
				else if (errorMessage.contains("Max Wager Online Exceeded")) {
					throw new BatchException(BatchErrorCodes.MAX_WAGER_AMOUNT_REACHED, BatchErrorMessage.MAX_WAGER_AMOUNT_REACHED, errorMessage, xhtml);
				}
				// Check for a wager limit and change it 
				else if (errorMessage.contains("line has just changed to")) {
					// Line changed throw exception
					throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, errorMessage, xhtml);			
				}
				else {
					// Line changed throw exception
					throw new BatchException(BatchErrorCodes.ERROR_PARSING_WAGER_INFO, BatchErrorMessage.ERROR_PARSING_WAGER_INFO, errorMessage, xhtml);								
				}
			} else {
				errorMessage = doc.select(".alert").html();
				
				if (errorMessage != null && errorMessage.length() > 0) {
					// Check for a wager limit and change it 
					if (errorMessage.contains("minimum required")) {
						throw new BatchException(BatchErrorCodes.MIN_WAGER_AMOUNT_NOT_REACHED, BatchErrorMessage.MIN_WAGER_AMOUNT_NOT_REACHED, errorMessage, xhtml);
					}
					// Check for a wager limit and change it 
					else if (errorMessage.contains("Max Wager Online Exceeded")) {
						throw new BatchException(BatchErrorCodes.MAX_WAGER_AMOUNT_REACHED, BatchErrorMessage.MAX_WAGER_AMOUNT_REACHED, errorMessage, xhtml);
					}
					// Check for a wager limit and change it 
					else if (errorMessage.contains("line has just changed to")) {
						// Line changed throw exception
						throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, errorMessage, xhtml);			
					}
					else if (errorMessage.contains("Balance Exceeded")) {
						// Line changed throw exception
						throw new BatchException(BatchErrorCodes.ACCOUNT_BALANCE_LIMIT, BatchErrorMessage.ACCOUNT_BALANCE_LIMIT, errorMessage, xhtml);
					}
					else {
						// Line changed throw exception
						throw new BatchException(BatchErrorCodes.ERROR_PARSING_WAGER_INFO, BatchErrorMessage.ERROR_PARSING_WAGER_INFO, errorMessage, xhtml);								
					}
				}
			}
		} else {
			final String[] types = new String[] { "hidden" };
			getAllElementsByType(doc, "input", types, map);
		}

		LOGGER.info("Exiting parseWagerTypes()");
		return map;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.tdsports.TDSportsParser#parseTicketNumber(java.lang.String)
	 */
	@Override
	public String parseTicketNumber(String xhtml) throws BatchException {
		LOGGER.info("Entering parseTicketNumber()");
		String ticketNumber = "";
		//
		final String ticketInfo = "Ticket Number:";

		if (xhtml.contains("Your bet expired or has already been submited.")) {
			throw new BatchException(BatchErrorCodes.WAGER_HAS_EXPIRED, BatchErrorMessage.WAGER_HAS_EXPIRED, "The wager has expired or already been bet", xhtml);	
		}
		
		if (xhtml.contains("The line changed for one (or more) of your selections.")) {
			LOGGER.error("WagerShack Line Change: " + xhtml);
			// Line changed throw exception
			throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed and cannot complete transaction", xhtml);	
		}

		// First check for Wager has been accepted!
		if (xhtml != null && xhtml.contains(ticketInfo)) {
			// <div id="ticketNumber">160163837</div>
			int index = xhtml.indexOf("ticketNumber'>");
			if (index != -1) {
				final String nxhtml = xhtml.substring(index + "ticketNumber'>".length());
				index = nxhtml.indexOf("</div>");
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
		} else {
			ticketNumber = "Failed to get ticket number";
			throw new BatchException(BatchErrorCodes.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, BatchErrorMessage.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, ticketNumber, xhtml);
		}

		LOGGER.info("Exiting parseTicketNumber()");
		return ticketNumber;
	}

	/**
	 * 
	 * @param elements
	 * @param input
	 * @return
	 * @throws BatchException
	 */
	protected List<SiteEventPackage> getGameData(Elements elements, Map<String, String> input) throws BatchException {
		LOGGER.info("Entering getGameData()");
		final List<SiteEventPackage> events = new ArrayList<SiteEventPackage>();

		if (elements != null) {
			SiteTeamPackage team1 = null;
			SiteTeamPackage team2 = null;
			SiteEventPackage eventPackage = null;

			for (int x = 0; x < elements.size(); x++) {
				// Loop through the elements and then check for the dates
				final Element element = elements.get(x);

				if (element != null) {
					eventPackage = new SiteEventPackage();
					team1 = new SiteTeamPackage();
					team2 = new SiteTeamPackage();

					// Get the dates
					Elements divs = element.select(".row div small");
					if (divs != null && divs.size() > 0) {
						parseDate(divs, eventPackage, team1, team2);
					}

					final Elements teamsBoxes = element.select(".teamsBox");
					if (teamsBoxes != null && teamsBoxes.size() > 0) {
						final Element teamsBox = teamsBoxes.get(0);
						// Get rotation ID and team names
						parseRotationIdTeam(teamsBox, team1, team2);
					}

					// Get the lines
					divs = element.select(".teamsodds");
					if (divs != null && divs.size() > 0) {
						int count = divs.size();
						int counter = 0;
						for (Element div : divs) {
							switch (counter++) {
								case 0: {
									String html = div.html();
									if (html != null && html.contains("_S_")) {
										final Elements inputs = div.select("input");
										int inputcounter = 0;
	
										for (Element iput : inputs) {
											switch (inputcounter++) {
												case 0:
													parseSpread(iput, team1, input);
													break;
												case 1:
													parseSpread(iput, team2, input);
													break;
											}
										}
									} else if (html != null && html.contains("_ML_")) {
										final Elements inputs = div.select("input");
										int inputcounter = 0;
	
										for (Element iput : inputs) {
											switch (inputcounter++) {
												case 0:
													parseMl(iput, team1, input);
													break;
												case 1:
													parseMl(iput, team2, input);
													break;
											}
										}
									}
									break;
								}
								case 1: {
									String html = div.html();
									if (html != null && html.contains("_ML")) {
										final Elements inputs = div.select("input");
										int inputcounter = 0;
	
										for (Element iput : inputs) {
											switch (inputcounter++) {
												case 0:
													parseMl(iput, team1, input);
													break;
												case 1:
													parseMl(iput, team2, input);
													break;
											}
										}
									} else if (html != null && html.contains("_Ov_") || html.contains("_Un_")) {
										final Elements inputs = div.select("input");
										int inputcounter = 0;

										for (Element iput : inputs) {
											switch (inputcounter++) {
												case 0:
													parseTotal(iput, team1, input);
													break;
												case 1:
													parseTotal(iput, team2, input);
													break;
											}
										}
									}

									break;
								}
								case 2: {
									String html = div.html();
									if (html != null && html.contains("_ML")) {
										final Elements inputs = div.select("input");
										int inputcounter = 0;
	
										for (Element iput : inputs) {
											switch (inputcounter++) {
												case 0:
													parseMl(iput, team1, input);
													break;
												case 1:
													parseMl(iput, team2, input);
													break;
											}
										}
									} else if (html != null && html.contains("_Ov_") || html.contains("_Un_")) {
										final Elements inputs = div.select("input");
										int inputcounter = 0;

										for (Element iput : inputs) {
											switch (inputcounter++) {
												case 0:
													parseTotal(iput, team1, input);
													break;
												case 1:
													parseTotal(iput, team2, input);
													break;
											}
										}
									}

									break;
								}
								default:
									break;
							}
						}
					}

					// Set the team and packages
					eventPackage.setId(team1.getId());
					eventPackage.setSiteteamone(team1);
					eventPackage.setSiteteamtwo(team2);
					events.add(eventPackage);
				} 
			}
		}

		LOGGER.info("Exiting getGameData()");
		return events;
	}

	/**
	 * 
	 * @param xhtml
	 * @return
	 */
	public Map<String, String> processLineChange(String xhtml) throws BatchException {
		LOGGER.info("Entering processLineChange()");
		final Map<String, String> map = new HashMap<String, String>();
		String tempXhtml = xhtml;

		try {
//			<div class='alert alert-error'>The line changed for one (or more) of your selections.</div>
//			<div class='row row_betslip row-margin'>
//			  <div class='col-lg-12'>
//			    STRAIGHT BET<br>[335] KANSAS <strong> 6 -115 </strong> <br><hr>
//			    <span>
//			      <button type='button' class='btn btn-small' onClick='remove_all()'>Cancel Bet(s)</button>
//			    </span>
//			    <span class='pull-right'>
//			      <button type='button' class='btn btn-small btn-inverse' id='pwd_button' onClick='dobetafterchanges("1984297_0_6_-115_500_0,","WHITE","0","88726")'>
//					Accept Change(s)
//				  </button>
//			    </span>
//			  </div>
//			</div>
//
//		    <div class='alert alert-error'>The line changed for one (or more) of your selections.</div>
//		    <div class='row row_betslip row-margin'>
//		      <div class='col-lg-12'>
//				STRAIGHT BET <br />[930] ATL BRAVES <strong>0 115</strong>
//		        <br />
//		        <hr />
//		        <span>
//		          <button type='button' class='btn btn-small' onclick='remove_all()'>
//		            <span>Cancel Bet(s)</span>
//		          </button>
//		        </span>
//		        <span class='pull-right'>
//		          <button type='button' class='btn btn-small btn-inverse' id='pwd_button' onclick='dobetafterchanges("1985599_5_0_115_500_0,","BLUE","0","88726")'>
//		            <span class='pull-right'>Accept Change(s)</span>
//		          </button>
//		        </span>
//			  </div>
//		    </div>
//
//			<div class='alert alert-error'>The line changed for one (or more) of your selections.</div>
//			<div class='row row_betslip row-margin'>
//				<div class='col-lg-12'>
//					STRAIGHT BET<br>[929] TOTAL (SF GIANTS vrs SEA MARINERS) <strong> -7.5 105 </strong> <br><hr>
//					<span>
//						<button type='button' class='btn btn-small' onClick='remove_all()'>Cancel Bet(s)</button>
//					</span>
//					<span class='pull-right'>
//						<button type='button' class='btn btn-small btn-inverse' id='pwd_button' onClick='dobetafterchanges("1961452_2_-7.5_105_10_0,","BLUE","0","88726")'>
//							Accept Change(s)
//						</button>
//					</span>
//				</div>
//			</div>

			int index = xhtml.indexOf("STRAIGHT BET<br>");
			if (index != -1) {
				xhtml = xhtml.substring(index + "STRAIGHT BET<br>".length());
				index = xhtml.indexOf("<strong>");
				if (index != -1) {
					xhtml = xhtml.substring(index + "<strong>".length()).trim();
					int index2 = xhtml.indexOf("</strong>");
					if (index2 != -1) {
						String line = xhtml.substring(0, index2).trim();
						if (line.startsWith("-") || !line.startsWith("0")) {
							// Spread or Total
							int space = line.lastIndexOf(" ");
							if (space != -1) {
								String ln = line.substring(0, space);
								String juice = line.substring(space + 1);
								if (ln.startsWith("-")) {
									map.put("valueindicator", ln.substring(0, 1));
									map.put("value", ln.substring(1));
								} else {
									map.put("valueindicator", "+");
									map.put("value", ln.substring(0));									
								}
								if (juice.startsWith("-")) {
									map.put("juiceindicator", juice.substring(0, 1));
									map.put("juice", juice.substring(1));
								} else {
									map.put("juiceindicator", "+");
									map.put("juice", juice.substring(0));									
								}
							}
						} else if (line.startsWith("0")) {
							int space = line.lastIndexOf(" ");
							if (space != -1) {
								String juice = line.substring(space + 1);
								if (juice.startsWith("-")) {
									map.put("valueindicator", juice.substring(0, 1));
									map.put("value", juice.substring(1));
									map.put("juiceindicator", juice.substring(0, 1));
									map.put("juice", juice.substring(1));
								} else {
									map.put("valueindicator", "+");
									map.put("value", juice.substring(0));
									map.put("juiceindicator", "+");
									map.put("juice", juice.substring(0));
								}
							}
						}

						// dobetafterchanges("1961452_2_-7.5_105_10_0,","BLUE","0","88726")

						index = xhtml.indexOf("dobetafterchanges(\"");
						if (index != -1) {
							xhtml = xhtml.substring(index + "dobetafterchanges(\"".length());
							index = xhtml.indexOf("\",\"");
							if (index != -1) {
								String newurl = xhtml.substring(0, index);
								map.put("newurl", newurl);
								xhtml = xhtml.substring(index + "\",\"".length());
								index = xhtml.indexOf("\",\"");
								if (index != -1) {
									String p = xhtml.substring(0, index);
									map.put("p", p);
									xhtml = xhtml.substring(index + "\",\"".length());
									index = xhtml.indexOf("\",\"");
									if (index != -1) {
										String wt = xhtml.substring(0, index);
										map.put("wt", wt);
										xhtml = xhtml.substring(index + "\",\"".length());
										index = xhtml.indexOf("\")");
										if (index != -1) {
											xhtml = xhtml.substring(0, index);
											map.put("idwt", xhtml);
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
			LOGGER.error("xhtml: " + xhtml);
			throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed", xhtml);
		}

		// Parse xhtml and get action
		final Element form = parseXhtml(tempXhtml).getElementById("wagerVerificationForm");
		if (form != null) {
			final String action = form.attr("action");
			map.put("action", action);
			final Element inetWagerNumber = form.getElementById("inetWagerNumber");
			map.put("inetWagerNumber", inetWagerNumber.attr("value"));
		}

		LOGGER.info("Entering processLineChange()");
		return map;
	}

	/**
	 * 
	 * @param div
	 * @param team1
	 * @param team2
	 */
	private void parseRotationIdTeam(Element div, SiteTeamPackage team1, SiteTeamPackage team2) {
		LOGGER.info("Entering parseRotationIdTeam()");

		// <div class='col-xs-12 teamsNameRot p5'>667 WESTERN ILLINOIS 
        // <span>@</span> 668 IPFW</div>
		String info = div.html().trim();
		LOGGER.debug("info: " + info);
		int index = info.indexOf("p5\">");

		if (index != -1) {
			info = info.substring(index + 4);
			index = info.indexOf("<span>");

			if (index != -1) {
				String t1 = info.substring(0, index).trim();
				int space = t1.indexOf(" ");

				if (space != -1) {
					team1.setEventid(t1.substring(0, space));
					team1.setId(Integer.parseInt(t1.substring(0, space)));
					team1.setTeam(t1.substring(space + 1));
				}

				info = info.substring(index + 6);
				index = info.indexOf("</span>");

				if (index != -1) {
					info = info.substring(index + 7).trim();
					space = info.indexOf(" ");

					if (space != -1) {
						team2.setEventid(info.substring(0, space));
						team2.setId(Integer.parseInt(info.substring(0, space)));
						team2.setTeam(info.substring(space + 1).replace("</div>", "").trim());
					}
				}
			}
		}

		LOGGER.info("Exiting parseRotationIdTeam()");
	}

	/**
	 * 
	 * @param divs
	 * @param eventPackage
	 * @param team1
	 * @param team2
	 */
	private void parseDate(Elements divs, SiteEventPackage eventPackage, SiteTeamPackage team1, SiteTeamPackage team2) {
		LOGGER.info("Entering parseDate()");

		final Element div = divs.get(0);
		String html = div.html();
		int index = html.indexOf("</i>");
		if (index != -1) {
			// 7/23/2018 4:05:00 PM
			html = html.substring(index + 4);
			index = html.indexOf("<i");
			if (index != -1) {
				final String gdate = html.substring(0, index).replace("-", "").trim();
				index = html.indexOf("</i>");
				if (index != -1) {
					final String gtime = html.substring(index + 4).trim();
					final String dateTime = gdate + " " + gtime + " " + super.determineTimeZone(this.timezone);
					Date dateAndTime = null;
					try {
						dateAndTime = DATE_FORMAT.parse(dateTime);
					} catch (ParseException pe) {
						LOGGER.error(pe.getMessage(), pe);
					}
					team1.setEventdatetime(dateAndTime);
					team2.setEventdatetime(dateAndTime);
					eventPackage.setEventdatetime(dateAndTime);
					team1.setDateofevent(dateTime);
					team2.setDateofevent(dateTime);
					eventPackage.setDateofevent(dateTime);
					eventPackage.setEventdate(gdate);
					eventPackage.setEventtime(gtime);
				}
			}
		}

		LOGGER.info("Exiting parseDate()");
	}

	/**
	 * 
	 * @param iput
	 * @param team
	 * @param input
	 */
	private void parseSpread(Element iput, SiteTeamPackage team, Map<String, String> input) {
		LOGGER.info("Entering parseSpread()");

		// 
		// 0_2143145_S_WESTERN ILLINOIS_8.5_-110_CBB_917_667
		// 
		String name = iput.attr("id");

		int index = name.indexOf("_");
		String bvalue = "";
		if (index != -1) {
			// 0_2143145_S_WESTERN ILLINOIS_8.5_-110_CBB_917_667
			bvalue = name.substring(0, index);
			LOGGER.debug("bvalue1: " + bvalue);

			name = name.substring(index + 1);
			index = name.indexOf("_");
			
			// 2143145_S_WESTERN ILLINOIS_8.5_-110_CBB_917_667
			bvalue = bvalue + "_" + name.substring(0, index);
			LOGGER.debug("bvalue2: " + bvalue);

			name = name.substring(index + 1);
			index = name.indexOf("_");
			
			// S_WESTERN ILLINOIS_8.5_-110_CBB_917_667
			name = name.substring(index + 1);
			index = name.indexOf("_");
			
			// WESTERN ILLINOIS_8.5_-110_CBB_917_667
			name = name.substring(index + 1);
			index = name.indexOf("_");

			// 8.5_-110_CBB_917_667
			String spread = name.substring(0, index);
			if (!spread.startsWith("-")) {
				spread = "+" + spread;
			}
			bvalue = bvalue + "_" + name.substring(0, index);
			LOGGER.debug("bvalue3: " + bvalue);

			name = name.substring(index + 1);
			index = name.indexOf("_");

			// -110_CBB_917_667
			String juice = name.substring(0, index);
			if (!juice.startsWith("-")) {
				juice = "+" + juice;
			}
			bvalue = bvalue + "_" + name.substring(0, index);
			LOGGER.debug("bvalue4: " + bvalue);

			name = name.substring(index + 1);
			index = name.indexOf("_");

			// CBB_917_667
			name = name.substring(index + 1);
			index = name.indexOf("_");

			// 917_667
			name = name.substring(index + 1).trim();

			// 667
			
			// First get the input field information
			team.setGameSpreadInputId(bvalue);
			team.setGameSpreadInputName("id");
			team.setGameSpreadInputValue(bvalue);

			// Setup spread
			team = (SiteTeamPackage)parseSpreadData(reformatValues(spread + " " + juice), 0, " ", null, team);
		}
		input.put("bb", bvalue);
		input.put("b", bvalue + "_0_0");
		input.put("c", "1");
		input.put("amt", "0");
		input.put("wt", "0");

		LOGGER.info("Exiting parseSpread()");
	}

	/**
	 * 
	 * @param iput
	 * @param team
	 * @param input
	 */
	private void parseTotal(Element iput, SiteTeamPackage team, Map<String, String> input) {
		LOGGER.info("Entering parseTotal()");

		// 
		// 2_2143145_Ov_WESTERN ILLINOIS_155.5_-110_CBB_917_667
		// 
		String name = iput.attr("id");
		LOGGER.debug("NAMEXXX: " + name);

		boolean ov = false;
		if (name != null && name.contains("_Ov_")) {
			ov = true;
		}

		int index = name.indexOf("_");
		String bvalue = "";
		if (index != -1) {
			// 2_2143145_Ov_WESTERN ILLINOIS_155.5_-110_CBB_917_667
			bvalue = name.substring(0, index);
			LOGGER.debug("bvalue1: " + bvalue);

			name = name.substring(index + 1);
			index = name.indexOf("_");
			
			// 2143145_Ov_WESTERN ILLINOIS_155.5_-110_CBB_917_667
			bvalue = bvalue + "_" + name.substring(0, index);
			LOGGER.debug("bvalue2: " + bvalue);

			name = name.substring(index + 1);
			index = name.indexOf("_");
			
			// Ov_WESTERN ILLINOIS_155.5_-110_CBB_917_667
			name = name.substring(index + 1);
			index = name.indexOf("_");
			
			// WESTERN ILLINOIS_155.5_-110_CBB_917_667
			name = name.substring(index + 1);
			index = name.indexOf("_");

			// 155.5_-110_CBB_917_667
			String total = name.substring(0, index);
			if (ov) {
				total = "o" + total;
			} else {
				total = "u" + total;
			}
			if (ov) {
				bvalue = bvalue + "_" + "-" + name.substring(0, index);
			} else {
				bvalue = bvalue + "_" + name.substring(0, index);
			}
			LOGGER.debug("bvalue3: " + bvalue);

			name = name.substring(index + 1);
			index = name.indexOf("_");

			// -110_CBB_917_667
			String juice = name.substring(0, index);
			if (!juice.startsWith("-")) {
				juice = "+" + juice;
			}
			bvalue = bvalue + "_" + name.substring(0, index);
			LOGGER.debug("bvalue4: " + bvalue);

			name = name.substring(index + 1);
			index = name.indexOf("_");

			// CBB_917_667
			name = name.substring(index + 1);
			index = name.indexOf("_");

			// 917_667
			name = name.substring(index + 1).trim();

			// 667
			
			// First get the input field information
			team.setGameTotalInputId(bvalue);
			team.setGameTotalInputName("id");
			team.setGameTotalInputValue(bvalue);

			// Setup total
			team = (SiteTeamPackage)parseTotalData(reformatValues(total + " " + juice), 0, null, null, team);
		}
		input.put("bb", bvalue);
		input.put("b", bvalue + "_0_0");
		input.put("c", "1");
		input.put("amt", "0");
		input.put("wt", "0");

		LOGGER.info("Exiting parseTotal()");
	}

	/**
	 * 
	 * @param iput
	 * @param team
	 * @param input
	 */
	private void parseMl(Element iput, SiteTeamPackage team, Map<String, String> input) {
		LOGGER.info("Entering parseMl()");
		// 
		// 4_2143146_ML_MONMOUTH_0_105_CBB_917_669
		// 
		String name = iput.attr("id");

		int index = name.indexOf("_");
		String bvalue = "";
		if (index != -1) {
			// 4_2143146_ML_MONMOUTH_0_105_CBB_917_669
			bvalue = name.substring(0, index);
			LOGGER.debug("bvalue1: " + bvalue);

			name = name.substring(index + 1);
			index = name.indexOf("_");
			
			// 2143146_ML_MONMOUTH_0_105_CBB_917_669
			bvalue = bvalue + "_" + name.substring(0, index);
			LOGGER.debug("bvalue2: " + bvalue);

			name = name.substring(index + 1);
			index = name.indexOf("_");
			
			// ML_MONMOUTH_0_105_CBB_917_669
			name = name.substring(index + 1);
			index = name.indexOf("_");
			
			// MONMOUTH_0_105_CBB_917_669
			name = name.substring(index + 1);
			index = name.indexOf("_");

			// 0_105_CBB_917_669
			bvalue = bvalue + "_0";
			LOGGER.debug("bvalue3: " + bvalue);

			name = name.substring(index + 1);
			index = name.indexOf("_");

			// 105_CBB_917_669
			String ml = name.substring(0, index);
			if (!ml.startsWith("-")) {
				ml = "+" + ml;
			}
			bvalue = bvalue + "_" + name.substring(0, index);
			LOGGER.debug("bvalue4: " + bvalue);

			name = name.substring(index + 1);
			index = name.indexOf("_");

			// CBB_917_669
			name = name.substring(index + 1);
			index = name.indexOf("_");

			// 917_669
			name = name.substring(index + 1).trim();
			index = name.indexOf("_");

			// 669

			// First get the input field information
			team.setGameMLInputId(bvalue);
			team.setGameMLInputName("id");
			team.addGameMLInputValue("0", bvalue);

			// Setup ml
			team = (SiteTeamPackage)parseMlData(reformatValues(ml), 0, team);
		}
		input.put("bb", bvalue);
		input.put("b", bvalue + "_0_0");
		input.put("c", "1");
		input.put("amt", "0");
		input.put("wt", "0");

		LOGGER.info("Exiting parseMl()");
	}
	
	/**
	 * 
	 * @param label
	 * @param total
	 * @param team
	 */
	private void setupTotalData(Element label, String total, SiteTeamPackage team) {
		LOGGER.info("Entering setupTotalData()");

		// First get the input field information
		final Map<String, String> hashMap = parseInputField(label.select("input"), 0);
		if (hashMap != null && !hashMap.isEmpty()) {
			team.setGameTotalInputId(hashMap.get("id"));
			team.setGameTotalInputName(hashMap.get("name"));
			team.setGameTotalInputValue(hashMap.get("value"));
		}

		// Setup total
		team = (SiteTeamPackage)parseTotalData(reformatValues(total), 0, " ", null, team);

		LOGGER.info("Exiting setupTotalData()");
	}

	/**
	 * 
	 * @param label
	 * @param ml
	 * @param team
	 */
	private void setupMlData(Element label, String ml, SiteTeamPackage team) {
		LOGGER.info("Entering setupMlData()");

		final Map<String, String> hashMap = parseInputField(label.select("input"), 0);
		if (hashMap != null && !hashMap.isEmpty()) {
			team.setGameMLInputId(hashMap.get("id"));
			team.setGameMLInputName(hashMap.get("name"));
			team.addGameMLInputValue("0", hashMap.get("value"));
			team = (SiteTeamPackage)parseMlData(reformatValues(ml), 0, team);
		}

		LOGGER.info("Exiting setupMlData()");
	}

	/**
	 * 
	 * @param spread
	 * @param label
	 * @param team
	 */
	private void setupSpreadData(String spread, Element label, SiteTeamPackage team) {
		LOGGER.info("Entering setupSpreadData()");

		// First get the input field information
		final Map<String, String> hashMap = parseInputField(label.select("input"), 0);
		if (hashMap != null && !hashMap.isEmpty()) {
			team.setGameSpreadInputId(hashMap.get("id"));
			team.setGameSpreadInputName(hashMap.get("name"));
			team.setGameSpreadInputValue(hashMap.get("value"));
		}

		// Setup spread
		team = (SiteTeamPackage)parseSpreadData(reformatValues(spread), 0, " ", null, team);
		LOGGER.info("Exiting setupSpreadData()");
	}

	/**
	 * 
	 * @param i
	 * @return
	 */
	private String determineIndicator(Element i) {
		LOGGER.info("Entering determineIndicator()");
		String class1 = i.attr("class");

		if (class1 != null && class1.contains("minus")) {
			class1 = "-";
		} else {
			class1 = "+";
		}

		LOGGER.info("Exiting determineIndicator()");
		return class1;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseEventSelection(java.lang.String, com.ticketadvantage.services.dao.sites.SiteTeamPackage, java.lang.String)
	 */
	@Override
	public Map<String, String> parseEventSelection(String xhtml, SiteTeamPackage siteTeamPackage, String type)
			throws BatchException {
		LOGGER.info("Entering parseEventSelection()");
		final Map<String, String> map = new HashMap<String, String>();

		// Check for a wager limit and change it 
		if (xhtml.contains("but this line has just changed to:")) {
			// Line changed throw exception
			throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed and cannot complete transaction", xhtml);			
		}

		// Parse the HTML first
		final Document doc = parseXhtml(xhtml);

		final String[] types = new String[] { "hidden", "text" };
		Element form = doc.getElementById("aspnetForm");
		if (form != null) {
			// Get form action field
			map.put("action", form.attr("action"));
			getAllElementsByTypeWithCheckbox(form, "input", types, map);
		}

		// Now get the select options if there are any
		if (type.equals("spread")) {
			setupSpreadWager(doc, siteTeamPackage);
		} else if (type.equals("total")) {
			setupTotalWager(doc, siteTeamPackage);
		} else {
			setupMlWager(doc, siteTeamPackage);
		}

		// Get the wager select
		final Elements selects = doc.select(".teamsNameRotConB select");
		LOGGER.debug("selects: " + selects);
		if (selects != null && selects.size() == 3) {
			parseSelectFieldByNumBlank(selects, 1, map);
		} else if (selects != null && selects.size() == 2) {
			parseSelectFieldByNumBlank(selects, 0, map);
		}

		// Get the pitcher select (if any)
		final Elements pitchers = doc.select(".gPitch select");
		LOGGER.debug("pitchers: " + pitchers);
		if (pitchers != null && pitchers.size() == 2) {
			parseSelectFieldByNumBlank(pitchers, 1, map);
		} else if (pitchers != null && pitchers.size() == 1) {
			parseSelectFieldByNumBlank(pitchers, 0, map);
		}

		LOGGER.info("Exiting parseEventSelection()");
		return map;
	}

	/**
	 * 
	 * @param doc
	 * @param teamPackage
	 */
	protected void setupSpreadWager(Document doc, SiteTeamPackage teamPackage) {
		LOGGER.info("Entering setupSpreadWager()");

		final Map<String, String> hashMap = parseSelectField(doc.select(".teamsNameRotCon select"));
		if (hashMap != null && hashMap.size() > 0) {
			teamPackage.setGameSpreadSelectName(hashMap.get("name"));
		}

		final Elements options = doc.select(".teamsNameRotCon select option");
		if (options != null && options.size() > 0) {
			LOGGER.debug("options.size(): " + options.size());
			Map<String, SelectOptionData> optionsMap = parseSelectOptionField(options);
			for (int x = 0; x < optionsMap.size(); x++) {
				final SelectOptionData option = optionsMap.get(Integer.toString(x));
				if (option != null && option.getData() != null && option.getData().length() > 0) {
					String optionData = option.getData();
					optionData = optionData.trim();
					optionData = optionData.replaceAll("â²", "");
					optionData = optionData.replaceAll("Â", "");
					LOGGER.debug("optionData: " + optionData);
					LOGGER.debug("option.getValue(): " + option.getValue());
					teamPackage.addGameSpreadOptionValue(Integer.toString(x), option.getValue());

					// -2½ +200; Now parse the data
					teamPackage = (SiteTeamPackage)parseSpreadData(reformatValues(optionData), x, " ", null, teamPackage);
				}
			}
		} else {
			setupSpreadWagerInfo(doc, teamPackage);
		}

		LOGGER.info("Exiting setupSpreadWager()");
	}

	/**
	 * 
	 * @param doc
	 * @param siteTeamPackage
	 */
	private void setupSpreadWagerInfo(Document doc, SiteTeamPackage siteTeamPackage) {
		LOGGER.info("Entering setupSpreadWagerInfo()");

		// <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 teamsNameRotCon p5">
		//   [52] NEW YORK RANGERS
		//   <span class="pull-right lineSpan">
		//     <i class="glyphicon glyphicon-minus" aria-hidden="true"></i>
		//     5
		//     <i class="glyphicon glyphicon-minus" aria-hidden="true"></i>
		//     110
		//   </span>
		//   <input id="bp_3_1757496" name="bp_3_1757496" value="0" type="hidden">
		// </div>

		final Elements divs = doc.select(".teamsNameRotCon");
		if (divs != null) {
			for (Element divEle : divs) {
				Elements spans = divEle.select("span");
				if (spans != null && spans.size() > 0) {
					Element span = spans.get(0);
					String html = span.html();
					LOGGER.debug("html: " + html);
					Elements is = span.select("i");
					if (is != null && is.size() == 2) {
						// -1 -110
						String class1 = determineIndicator(is.get(0));
						String class2 = determineIndicator(is.get(1));

						int bindex = html.indexOf("</i>");
						if (bindex != -1) {
							html = html.substring(bindex + 4);
							int eindex = html.indexOf("<i");
							if (eindex != -1) {
								final String line = html.substring(0, eindex).trim();
								html = html.substring(eindex);
								bindex = html.indexOf("</i>");
	
								if (bindex != -1) {
									final String juice = html.substring(bindex + 4).trim();
									final String spread = class1 + line + " " + class2 + juice;
		
									setupSpreadData(spread, span, siteTeamPackage);
								}
							}
						}
					} else if (is != null && is.size() == 1) {
						// PK -110 or -3EV or -3 EV
						html = html.trim();

						if (html.startsWith("PK")) {
							// PK -110
							//     <label style="margin-bottom: 0;">
							//      PK
							//      <i class="glyphicon glyphicon-minus" aria-hidden="true"></i>
							//      110
							//      <input id="1_1751773_S_MIAMI OHIO_-3_-110_CFB_2_326" class="checkBox" name="game" type="checkbox">
							//     </label>
							int bindex = html.indexOf("</i>");

							if (bindex != -1) {
								String lineplusminus = "+";
								String line = "0";
								String juiceplusminus = determineIndicator(is.get(0));
								final String juice = html.substring(bindex + 4).trim();
								final String spread = lineplusminus + line + " " + juiceplusminus + juice;
	
								setupSpreadData(spread, span, siteTeamPackage);
							}
						} else {
							// -3 EV or
							//     <label style="margin-bottom: 0;">
							//      <i class="glyphicon glyphicon-minus" aria-hidden="true"></i>
							//      3EV
							//      <input id="1_1751773_S_MIAMI OHIO_-3_-110_CFB_2_326" class="checkBox" name="game" type="checkbox">
							//     </label>
							int bindex = html.indexOf("</i>");
							if (bindex != -1) {
								html = html.substring(bindex + 4).trim();
								html = html.replace(" EV", " +100");
								html = html.replace("EV", " +100");
								final String lineplusminus = determineIndicator(is.get(0));
								final String spread = lineplusminus + html;

								setupSpreadData(spread, span, siteTeamPackage);
							}
						}
					} else {
						// PKEV
						// <input id="5_1751768_ML_NORTHWESTERN_0_100_CFB_2_316" class="checkBox" name="game" type="checkbox">
						final String spread = "+0 +100";
						setupSpreadData(spread, span, siteTeamPackage);
					}
				}
			}
		}

		LOGGER.info("Exiting setupSpreadWagerInfo()");
	}

	/**
	 * 
	 * @param doc
	 * @param teamPackage
	 */
	protected void setupTotalWager(Document doc, SiteTeamPackage teamPackage) {
		LOGGER.info("Entering setupTotalWager()");
		
		final Map<String, String> hashMap = parseSelectField(doc.select(".teamsNameRotCon select"));
		if (hashMap != null && hashMap.size() > 0) {
			teamPackage.setGameTotalSelectName(hashMap.get("name"));
		}

		final Elements options = doc.select(".teamsNameRotCon select option");
		if (options != null && options.size() > 0) {
			LOGGER.debug("options.size(): " + options.size());
			Map<String, SelectOptionData> optionsMap = parseSelectOptionField(options);
			for (int x = 0; x < optionsMap.size(); x++) {
				final SelectOptionData option = optionsMap.get(Integer.toString(x));
				if (option != null && option.getData() != null && option.getData().length() > 0) {
					String optionData = option.getData();
					optionData = optionData.trim();
					optionData = optionData.replaceAll("â²", "");
					optionData = optionData.replaceAll("Â", "");
					optionData = optionData.replaceAll("ov", "");
					optionData = optionData.replaceAll("un", "");
					optionData = optionData.replaceAll("▲", "");
					optionData = optionData.replaceAll("▼", "");
					LOGGER.debug("optionData: " + optionData);
					LOGGER.debug("option.getValue(): " + option.getValue());
					teamPackage.addGameTotalOptionValue(Integer.toString(x), option.getValue());

					// -2½ +200; Now parse the data
					teamPackage = (SiteTeamPackage)parseTotalData(reformatValues(optionData), x, " ", null, teamPackage);
				}
			}
		} else {
			setupTotalWagerInfo(doc, teamPackage);
		}

		LOGGER.info("Exiting setupTotalWager()");
	}

	/**
	 * 
	 * @param doc
	 * @param teamPackage
	 */
	private void setupTotalWagerInfo(Document doc, SiteTeamPackage teamPackage) {
		LOGGER.info("Entering setupTotalWagerInfo()");

		// <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 teamsNameRotCon p5">
		//   [52] NEW YORK RANGERS
		//   <span class="pull-right lineSpan">
		//     u5½
		//     <i class="glyphicon glyphicon-minus" aria-hidden="true"></i>
		//     110
		//   </span>
		//   <input id="bp_3_1757496" name="bp_3_1757496" value="0" type="hidden">
		// </div>

		final Elements divs = doc.select(".teamsNameRotCon");
		if (divs != null) {
			for (Element divEle : divs) {
				Elements spans = divEle.select("span");
				if (spans != null && spans.size() > 0) {
					Element span = spans.get(0);
					String html = span.html();
					Elements is = span.select("i");
					if (is != null && is.size() == 1) {
						int index = html.indexOf("<i");
						if (index != -1) {
							String total = html.substring(0, index);
							total = total.replace("Ov", "o");
							total = total.replace("Un", "u");
							total = total.trim();
							total = total.replace(" ", "");
							String juiceindicator = determineIndicator(is.get(0));
							
							int bindex = html.indexOf("</i>");
							if (bindex != -1) {
								String juice = html.substring(bindex + 4).trim();
								total = total + " " + juiceindicator + juice;
								setupTotalData(divEle, total, teamPackage);
							}
						}
					} else {
						int index = html.indexOf("EV");
						if (index != -1) {
							String total = html.substring(0, index);
							total = total + " +100";
							setupTotalData(divEle, total, teamPackage);
						}
					}
				}
			}
		}

		LOGGER.info("Exiting setupTotalWagerInfo()");
	}

	/**
	 * 
	 * @param doc
	 * @param teamPackage
	 */
	private void setupMlWager(Document doc, SiteTeamPackage teamPackage) {
		LOGGER.info("Entering setupMlWager()");

		// <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 teamsNameRotCon p5">
		//   [452] CLE BROWNS
		//   <span class="pull-right lineSpan">
		//     <i class="glyphicon glyphicon-plus" aria-hidden="true"></i>
		//     199
		//   </span>
		//   <input id="bp_5_1751821" name="bp_5_1751821" value="0" type="hidden">
		// </div>

		final Elements divs = doc.select(".teamsNameRotCon");
		if (divs != null) {
			for (Element divEle : divs) {
				Elements spans = divEle.select("span");
				if (spans != null && spans.size() > 0) {
					Element span = spans.get(0);
					String html = span.html();
					Elements is = span.select("i");
					if (is != null && is.size() == 1) {
						String ml = null;
						String juiceindicator = determineIndicator(is.get(0));

						int bindex = html.indexOf("</i>");
						if (bindex != -1) {
							ml = html.substring(bindex + 4).trim();
							ml = juiceindicator + ml;
							setupMlData(divEle, ml, teamPackage);
						}
					} else {
						String ml = "+100";
						setupMlData(divEle, ml, teamPackage);
					}
				}
			}
		}

		LOGGER.info("Exiting setupMlWager()");
	}

	/**
	 * 
	 * @param divs
	 * @param map
	 * @param type
	 * @param sport
	 * @param foundString
	 * @param menuString
	 * @return
	 */
	protected Map<String, String> findMenu(Elements divs, Map<String, String> map, String[] type, String[] sport, String foundString, String menuString) {
		LOGGER.info("Entering findMenu()");
		LOGGER.info("divs: " + divs);

		for (int x = 0; (divs != null && x < divs.size()); x++) {
			final Element div = divs.get(x);
			boolean foundDiv = false;
			for (int z = 0; z < sport.length; z++) {
				for (int y = 0; y < type.length; y++) {
					foundDiv = foundSport(div, foundString, sport[z]);
					LOGGER.debug("foundDiv: " + foundDiv);
	
					// Found the event
					if (foundDiv) {
						map = getMenuData(div, menuString, map, type[y]);
						LOGGER.debug("Map: " + map);
					}
				}
			}
		}

		LOGGER.info("Exiting findMenu()");
		return map;
	}

	/**
	 * 
	 * @param div
	 * @param select
	 * @param sport
	 * @return
	 */
	protected boolean foundSport(Element div, String select, String sport) {
		LOGGER.info("Entering foundSport()");
		LOGGER.debug("div: " + div);
		LOGGER.debug("select: " + select);
		LOGGER.debug("sport: " + sport);
		boolean foundDiv = false;

		String divData = getHtmlFromElement(div, select, 0, false);
		if (divData != null && divData.length() > 0) {
			int index = divData.indexOf("<i");
			if (index != -1) {
				divData = divData.substring(0, index);
				divData = divData.replace("&nbsp;", "");
				divData = divData.replace("*", "");
				divData = divData.trim();
			}
		}

		LOGGER.debug("divData: " + divData);

		// Check if we found div
		if (divData != null && divData.equals(sport)) {
			foundDiv = true;
		} else if (sport.startsWith("NFL - WEEK") && divData != null && divData.startsWith("NFL - WEEK")) { // HACK!!!
			foundDiv = true;
		}

		LOGGER.info("Exiting foundSport()");
		return foundDiv;
	}

	/**
	 * 
	 * @param div
	 * @param element
	 * @param map
	 * @param type
	 * @return
	 */
	protected Map<String, String> getMenuData(Element div, String element, Map<String, String> map, String type) {
		LOGGER.info("Entering getMenuData()");
		LOGGER.debug("Div: " + div);
		LOGGER.debug("element: " + element);

		final Elements labels = div.select(element);
		for (int x = 0;(labels != null && x < labels.size()); x++) {
			final Element label = labels.get(x);
			LOGGER.debug("label: " + label);
			if (label != null) {
				String html = label.html();
				LOGGER.debug("html: " + html);
				if (html != null) {
					int index = html.indexOf(">");
					if (index != -1) {
						html = html.substring(index + 1).trim();
						html = html.replace("&nbsp;", "");
						LOGGER.debug("html2: " + html);
						LOGGER.debug("type: " + type);
						if (html.equals(type)) {
							LOGGER.debug("Found menu item");
							final Elements inputs = label.select("input");
							if (inputs != null && inputs.size() > 0) {
								final Element input = inputs.get(0);
								LOGGER.debug("input: " + input);
								String chk = map.get("chk");
								if (chk != null && chk.length() > 0) {
									String value = input.attr("value");
									map.put("chk", chk + "," + value);
								} else {
									map.put("chk", input.attr("value"));	
								}
							}
						}
					}
				}
			}
		}

		LOGGER.info("Exiting getMenuData()");
		return map;
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 */
	private void getSport(Element td, PendingEvent pe) {
		LOGGER.info("Entering getSport()");

		final String sport = td.html().replace("<br>", "").trim();
		if ("NFL".equals(sport)) {
			pe.setGamesport("Football");
			pe.setGametype("NFL");
		} else if ("CFB".equals(sport)) {
			pe.setGamesport("Football");
			pe.setGametype("NCAA");
		} else if ("NBA".equals(sport)) {
			pe.setGamesport("Basketball");
			pe.setGametype("NBA");
		} else if ("CBB".equals(sport)) {
			pe.setGamesport("Basketball");
			pe.setGametype("NCAA");
		} else if ("WNBA".equals(sport)) {
			pe.setGamesport("Basketball");
			pe.setGametype("WNBA");
		} else if ("MLB".equals(sport)) {
			pe.setGamesport("Baseball");
			pe.setGametype("MLB");
		} else if ("NHL".equals(sport)) {
			pe.setGamesport("Hockey");
			pe.setGametype("NHL");								
		}

		LOGGER.info("Exiting getSport()");
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 */
	private void getRiskWinInfo(Element td, PendingEvent pe) {
		LOGGER.info("Entering getRiskWinInfo()");

		final String riskwin = td.html().replace("<br>", "").trim();
		int index = riskwin.indexOf("/");

		if (index != -1) {
			pe.setRisk(riskwin.substring(0, index).trim());
			pe.setWin(riskwin.substring(index + 1).trim());
		}

		LOGGER.info("Exiting getRiskWinInfo()");
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 */
	private void getDatePlaced(Element td, PendingEvent pe) {
		LOGGER.info("Entering getDatePlaced()");

		final String dateaccepted = td.html().replace("<br>", "").trim();
		pe.setDateaccepted(dateaccepted);

		LOGGER.info("Exiting getDatePlaced()");
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 */
	private void getGameDateAndTicketNumber(Element td, PendingEvent pe) {
		LOGGER.info("Entering getGameDateAndTicketNumber()");
		String html = td.html();		
		int index = html.indexOf("Ticket#:");

		if (index != -1) {
			int indexdos = html.indexOf("<br>");
			if (indexdos != -1) {
				pe.setTicketnum(html.substring(index + "Ticket#:".length(), indexdos).trim());

				html = html.substring(indexdos + 4);
				index = html.indexOf("<br>");
				
				if (index != -1) {
					final String timezone = determineTimeZone(super.timezone);
					final String gameDate = html.substring(0, index).trim();
					final Calendar now = Calendar.getInstance();

					try {
						LOGGER.debug("gameDate: " + gameDate);
						LOGGER.debug("timezone: " + timezone);
						final Date theGameDate = PENDING_DATE_FORMAT.parse(gameDate + " " + timezone + " " + now.get(Calendar.YEAR));
						pe.setGamedate(theGameDate);
						pe.setEventdate(gameDate + " " + timezone + " " + now.get(Calendar.YEAR));
					} catch (ParseException pee) {
						LOGGER.warn(pee.getMessage(), pee);
						pe.setGamedate(new Date());
						pe.setEventdate(gameDate + " " + timezone + " " + now.get(Calendar.YEAR));
					}
				}
			}
		}

		LOGGER.info("Exiting getGameDateAndTicketNumber()");
	}
}