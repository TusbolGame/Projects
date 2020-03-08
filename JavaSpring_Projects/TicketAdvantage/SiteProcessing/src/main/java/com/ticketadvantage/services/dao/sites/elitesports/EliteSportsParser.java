/**
 * 
 */
package com.ticketadvantage.services.dao.sites.elitesports;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ticketadvantage.services.dao.sites.SelectOptionData;
import com.ticketadvantage.services.dao.sites.SiteParser;
import com.ticketadvantage.services.dao.sites.SiteTeamPackage;
import com.ticketadvantage.services.errorhandling.AppErrorCodes;
import com.ticketadvantage.services.errorhandling.AppErrorMessage;
import com.ticketadvantage.services.errorhandling.AppException;
import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;

/**
 * @author jmiller
 *
 */
public class EliteSportsParser extends SiteParser {
	private static final Logger LOGGER = Logger.getLogger(EliteSportsParser.class);
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("E MM/dd yyyy hh:mma Z");

	/**
	 * Constructor
	 */
	public EliteSportsParser() {
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
		final Map<String, String> retValue = new HashMap<String, String>();

		// Get the document object
		final Document doc = parseXhtml(xhtml);
		
		// Get all input fields
		getAllElementsByName(doc, "input", "value", retValue);
		
		// Get form action field
		retValue.put("action", getElementByName(doc, "form", "action"));

		LOGGER.info("Exiting parseIndex()");
		return retValue;
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

		// Now check for the straightSelectionFrm form
		String[] types = new String[] { "hidden" };
		Elements forms = doc.select("form");
		for (int x = 0; (forms != null && x < forms.size()); x++) {
			Element form = forms.get(x);
			if (form != null && "WagerMenu.asp".equals(form.attr("action"))) {
				// Get form action field
				map.put("action", form.attr("action"));	
				getAllElementsByType(form, "input", types, map);
			} else if (form != null && "betTypeForm".equals(form.attr("name"))) {
				// Get form action field
				map.put("action", form.attr("action"));	
				getAllElementsByType(form, "input", types, map);				
			} else if (form != null && "SportSelectionForm".equals(form.attr("name"))) {
				// Get form action field
				map.put("action", form.attr("action"));	
				getAllElementsByType(form, "input", types, map);				
			}			
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

		// Parse to get the Document
		final Document doc = parseXhtml(xhtml);
		LOGGER.debug("Document: " + doc);

		// Now check for the straightSelectionFrm form
		final String[] types = new String[] { "hidden" };
		final Elements forms = doc.select("form");
		for (int x = 0; (forms != null && x < forms.size()); x++) {
			final Element form = forms.get(x);
			if (form != null && "SportSelectionForm".equals(form.attr("name"))) {
				// Get form action field
				map.put("action", form.attr("action"));	
				getAllElementsByType(form, "input", types, map);				
			}			
		}

		// Setup the data
		if (map.containsKey("sportType")) {
			map.put("sportType", sport[0]);
		}
		if (map.containsKey("sportSubType")) {
			map.put("sportSubType", type[0]);
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
	public <TDSportsEventPackage> List<TDSportsEventPackage> parseGames(String xhtml, String type, Map<String, String> inputFields) throws BatchException {
		LOGGER.info("Entering parseGames()");
		LOGGER.debug("xhtml: " + xhtml);
		LOGGER.debug("type: " + type);
		LOGGER.debug("InputFields: " + inputFields);
		
		List<?> events = null;
		// Parse to get the Document
		final Document doc = parseXhtml(xhtml);
		LOGGER.debug("Document: " + doc);

		String divid = null;
		final Elements divs = doc.select("div");
		for (Element div : divs) {
			final String id = div.attr("id");
			if (id != null && id.contains("gamePeriodDiv")) {
				final String html = div.html().trim();
				if (type.contains("lines")) {
					if (html.contains("<th></th>Game")) {
						divid = id;
						break;
					}
				} else if (type.contains("first")) {
					if (html.contains("<th></th>1st 5 Innings") || html.contains("<th></th>1st Half") || html.contains("<th></th>1st Period")) {
						divid = id;
						break;
					}
				} else if (type.contains("second")) {
					if (html.contains("<th></th>Last 4 Innings") || html.contains("<th></th>2nd Half") || html.contains("<th></th>2nd Period")) {
						divid = id;
						break;
					}
				} else if (type.contains("Third")) {
					if (html.contains("<th></th>3rd Period")) {
						divid = id;
						break;
					}
				}
			}
		}

/*
		String divName = null;
		final Elements inputs = doc.select("input");
		for (int x = 0; (inputs != null && x < inputs.size()); x++) {
			Element input = inputs.get(x);
			if (type.equals("nfllines") || type.equals("ncaaflines") || type.equals("nbalines") || type.equals("ncaablines") || type.equals("nhllines") || type.equals("wnbalines") || type.equals("mlblines")) {
				String value = input.attr("value");
				if (value != null && "Game".equals(value)) {
					String onclick = input.attr("onclick");
					if (onclick != null && onclick.contains("gamePeriodDiv")) {
						int index = onclick.indexOf("'gamePeriodDiv");
						if (index != -1) {
							onclick = onclick.substring(index + "'gamePeriodDiv".length());
							int endIndex = onclick.indexOf("'");
							if (index != -1) {
								divName = "gamePeriodDiv" + onclick.substring(0, endIndex);
							}
						}
					}
				}
			} else if (type.equals("nflfirst") || type.equals("ncaaffirst") || type.equals("nbafirst") || type.equals("ncaabfirst") || type.equals("nhlfirst") || type.equals("wnbafirst") || type.equals("mlbfirst")) {
				String value = input.attr("value");
				if (value != null && "1st Half".equals(value)) {
					String onclick = input.attr("onclick");
					if (onclick != null && onclick.contains("gamePeriodDiv")) {
						int index = onclick.indexOf("'gamePeriodDiv");
						if (index != -1) {
							onclick = onclick.substring(index + "'gamePeriodDiv".length());
							int endIndex = onclick.indexOf("'");
							if (index != -1) {
								divName = "gamePeriodDiv" + onclick.substring(0, endIndex);
							}
						}
					}
				}				
			} else if (type.equals("nflsecond") || type.equals("ncaafsecond") || type.equals("nbasecond") || type.equals("ncaabsecond") || type.equals("nhlsecond") || type.equals("wnbasecond") || type.equals("mlbsecond")) {
				String value = input.attr("value");
				if (value != null && "2nd Half".equals(value)) {
					String onclick = input.attr("onclick");
					if (onclick != null && onclick.contains("gamePeriodDiv")) {
						int index = onclick.indexOf("'gamePeriodDiv");
						if (index != -1) {
							onclick = onclick.substring(index + "'gamePeriodDiv".length());
							int endIndex = onclick.indexOf("'");
							if (index != -1) {
								divName = "gamePeriodDiv" + onclick.substring(0, endIndex);
							}
						}
					}
				}			
			}
		}
*/

		LOGGER.debug("divid: " + divid);
		if (divid != null) {
			final Element div = doc.getElementById(divid);
			final Elements trs = div.select("table tbody tr");
			if (trs != null && trs.size() > 0) {
				events = getGameData(trs);
			}				
		}

		// Get forms hidden input fields and action URL
		final String[] types = new String[] { "hidden" };
		final Elements forms = doc.select("form");
		for (int x = 0; (forms != null && x < forms.size()); x++) {
			final Element form = forms.get(x);
			if (form != null && "GameSelectionForm".equals(form.attr("name"))) {
				// Get form action field
				inputFields.put("action", form.attr("action"));	
				getAllElementsByType(form, "input", types, inputFields);				
			}			
		}

		LOGGER.info("Exiting parseGames()");
		return (List<TDSportsEventPackage>)events;
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

		// Parse to get the Document
		final Document doc = parseXhtml(xhtml);

		// Get all of the input fields
		final String[] types = new String[] { "hidden", "radio", "text", "submit" };
		final Elements forms = doc.select("form");
		for (int x = 0; (forms != null && x < forms.size()); x++) {
			final Element form = forms.get(x);
			if (form != null && "WagerAmountForm".equals(form.attr("name"))) {
				// Get form action field
				map.put("action", form.attr("action"));	
				getAllElementsByType(form, "input", types, map);				
			}			
		}

		// Now get the select options if there are any
		if (type.equals("spread")) {
			setupSpreadWager(doc, map, (EliteSportsTeamPackage) siteTeamPackage);
		} else if (type.equals("total")) {
			if (xhtml.contains("OVER")) {
				setupTotalWager(doc, "o", (EliteSportsTeamPackage) siteTeamPackage);
			} else {
				setupTotalWager(doc, "u", (EliteSportsTeamPackage) siteTeamPackage);
			}
		}

		// Check for a wager limit and change it 
		if (xhtml.contains("A maximum wager amount of ")) {
			int index = xhtml.indexOf("A maximum wager amount of ");
			if (index != -1) {
				xhtml = xhtml.substring(index + "A maximum wager amount of ".length());
				index = xhtml.indexOf(" USD.");
				if (index != -1) {
					String wagerAmount = xhtml.substring(0, index);
					if (wagerAmount != null) {
						wagerAmount = wagerAmount.replaceAll(",", "");
						wagerAmount = wagerAmount.replaceAll(" ", "");
						map.put("wageramount", wagerAmount);
					}
				}
			}
		}

		// Check for a wager limit and change it 
		if (xhtml.contains("A minimum wager amount of ")) {
			int index = xhtml.indexOf("A minimum wager amount of ");
			if (index != -1) {
				xhtml = xhtml.substring(index + "A minimum wager amount of ".length());
				index = xhtml.indexOf(" USD");
				if (index != -1) {
					String wagerAmount = xhtml.substring(0, index);
					if (wagerAmount != null) {
						wagerAmount = wagerAmount.replaceAll(",", "");
						wagerAmount = wagerAmount.replaceAll(" ", "");
						map.put("wagerminamount", wagerAmount);
					}
				}
			}
		}

		// Check for a wager limit and change it 
		if (xhtml.contains("Exceeded. Limit For This Game is ")) {
			int index = xhtml.indexOf("Exceeded. Limit For This Game is ");
			if (index != -1) {
				xhtml = xhtml.substring(index + "Exceeded. Limit For This Game is ".length());
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
		}

		LOGGER.info("Exiting parseEventSelection()");
		return map;
	}

	/**
	 * 
	 * @param xhtml
	 * @return
	 * @throws BatchException
	 */
	public Map<String, String> parseConfirmWager(String xhtml) throws BatchException {
		LOGGER.info("Entering parseConfirmWager()");
		LOGGER.debug("xhtml: " + xhtml);
		Map<String, String> map = new HashMap<String, String>();

		// Parse the HTML first
		final Document doc = parseXhtml(xhtml);

		final String[] types = new String[] { "hidden", "submit", "password", "Password" };
		final Elements forms = doc.select("form");
		for (int x = 0; (forms != null && x < forms.size()); x++) {
			final Element form = forms.get(x);
			if (form != null && "WagerVerificationForm".equals(form.attr("name"))) {
				// Get form action field
				map.put("action", form.attr("action"));	
				getAllElementsByType(form, "input", types, map);				
			}			
		}

		// Check if we need to send upper case password
		if (xhtml.contains("toUpperCase")) {
			map.put("toUpperCase", "true");
		}

		// Max wager amount
		String tempXhtml = xhtml;
		if (xhtml.contains("A maximum wager amount of ")) {
			int index = xhtml.indexOf("A maximum wager amount of ");
			if (index != -1) {
				xhtml = xhtml.substring(index);
				index = xhtml.indexOf("account.    </div>");
				if (index != -1) {
					xhtml = xhtml.substring(0, index);
					xhtml += " account.";
					throw new BatchException(BatchErrorCodes.MAX_WAGER_AMOUNT_REACHED, BatchErrorMessage.MAX_WAGER_AMOUNT_REACHED, xhtml, tempXhtml);
				} else {
					throw new BatchException(BatchErrorCodes.MAX_WAGER_AMOUNT_REACHED, BatchErrorMessage.MAX_WAGER_AMOUNT_REACHED, "MAX WAGER AMOUNT REACHED", tempXhtml);
				}
			}
		}

		// Check for a wager limit and change it 
		if (xhtml.contains("is greater than your amount available of ")) {
			int index = xhtml.indexOf("is greater than your amount available of ");
			if (index != -1) {
				xhtml = xhtml.substring(index + "is greater than your amount available of ".length());
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
		}

		// Check for a wager limit and change it 
		if (xhtml.contains("A minimumwager amount of ")) {
			int index = xhtml.indexOf("A minimumwager amount of ");
			if (index != -1) {
				xhtml = xhtml.substring(index + "A minimumwager amount of ".length());
				index = xhtml.indexOf(" USD");
				if (index != -1) {
					String wagerAmount = xhtml.substring(0, index);
					if (wagerAmount != null) {
						wagerAmount = wagerAmount.replaceAll(",", "");
						wagerAmount = wagerAmount.replaceAll(" ", "");
						map.put("wagerminamount", wagerAmount);
					}
				}
			}
		}

		// Check for a wager limit and change it 
		if (xhtml.contains("Amount In Side Exceeded. Limit For This Game is ")) {
			int index = xhtml.indexOf("Amount In Side Exceeded. Limit For This Game is ");
			if (index != -1) {
				xhtml = xhtml.substring(index + "Amount In Side Exceeded. Limit For This Game is ".length());
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
		}

		// Remove the input fields that shouldn't be sent
		map = removeInputFields(map);

		LOGGER.info("Exiting parseConfirmWager()");
		return map;		
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
		final Map<String, String> wagers = new HashMap<String, String>();

		// Parse to get the Document
		final Document doc = parseXhtml(xhtml);
			
		// First get the Risk
		String risk = getHtmlFromAllElements(doc, ".offering_pair td a font b");
		if (risk != null && risk.length() > 0) {
			risk = risk.substring(1);
			wagers.put("risk", risk);
		} else {
			// Throw Exception
			throw new BatchException(BatchErrorCodes.ERROR_PARSING_WAGER_INFO, BatchErrorMessage.ERROR_PARSING_WAGER_INFO, "No element found for " + xhtml);
		}

		// Now get the Win
		String win = getHtmlFromAllElements(doc, ".offering_pair td a font b");
		if (win != null && win.length() > 0) {
			win = win.substring(1);
			wagers.put("win", win);
		} else {
			// Throw Exception
			throw new BatchException(BatchErrorCodes.ERROR_PARSING_WAGER_INFO, BatchErrorMessage.ERROR_PARSING_WAGER_INFO, "No element found for " + xhtml);
		}

		LOGGER.info("Exiting parseWagerTypes()");
		return wagers;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseTicketNumber(java.lang.String)
	 */
	@Override
	public String parseTicketNumber(String xhtml) throws BatchException {
		LOGGER.info("Entering parseTicketNumber()");
		String ticketNumber = "";
		final String ticketInfo = "Ticket Number:";
		LOGGER.debug("xhtml: " + xhtml);

		// First check for Wager has been accepted!
		if (xhtml != null && xhtml.contains(ticketInfo)) {
			// Great, wager is complete; now get the Ticket Number
			int index = xhtml.indexOf(ticketInfo);
			LOGGER.debug("index: " + index);

			if (index != -1) {
				String nxhtml = xhtml.substring(index + ticketInfo.length()).trim();
				LOGGER.debug("nxhtml: " + nxhtml);

				index = nxhtml.indexOf("bold\">");
				LOGGER.debug("index: " + index);

				if (index != -1) {
					nxhtml = nxhtml.substring(index + "bold\">".length());
					index = nxhtml.indexOf("</span>");
					if (index != -1) {
						ticketNumber = nxhtml.substring(0, index).trim();
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
		}

		LOGGER.info("Exiting parseTicketNumber()");
		return ticketNumber;
	}

	/**
	 * 
	 * @param doc
	 * @param teamPackage
	 */
	private void setupSpreadWager(Document doc, Map<String, String> map, EliteSportsTeamPackage teamPackage) {
		LOGGER.info("Entering setupSpreadWager()");

		parseSelectFieldWithMap(doc.select("form select"), map);
		teamPackage.setGameSpreadSelectName(map.get("name"));

		final Elements options = doc.select("form select option");
		if (options != null && options.size() > 0) {
			Map<String, SelectOptionData> optionsMap = parseSelectOptionField(options);
			for (int x = 0; x < optionsMap.size(); x++) {
				final SelectOptionData option = optionsMap.get(Integer.toString(x));
				if (option != null && option.getData() != null && option.getData().length() > 0) {
					String optionData = option.getData();
					optionData = optionData.trim();
					if (!"Buy no points".equals(optionData)) {
						String pointTo = "point to ";
						int index = optionData.indexOf("point to ");
						if (index == -1) {
							index = optionData.indexOf("points to ");
							pointTo = "points to";
						}
						String spreadOption = null;
						if (index != -1) {
							int fIndex = optionData.indexOf("for");
							if (fIndex != -1) {
								String firstPart = optionData.substring(index + pointTo.length(), fIndex);
								firstPart = firstPart.trim();
								String secondPart = optionData.substring(fIndex + "for".length());
								secondPart = secondPart.trim();
								spreadOption = firstPart + " " + secondPart;
							}
						}
						LOGGER.debug("spreadOption: " + spreadOption);
						teamPackage = (EliteSportsTeamPackage)parseSpreadData(reformatValues(spreadOption), x, " ", null, teamPackage);
					}

					// Setup the option value
					teamPackage.addGameSpreadOptionValue(Integer.toString(x), option.getValue());
				} else {
					// Throw an exception
					throw new AppException(500, AppErrorCodes.SITE_PARSER_EXCEPTION,  
							AppErrorMessage.SITE_PARSER_EXCEPTION  + " Options are empty");					
				}
			}
		}

		LOGGER.info("Exiting setupSpreadWager()");
	}

	/**
	 * 
	 * @param doc
	 * @param teamPackage
	 */
	private void setupTotalWager(Document doc, String indicator, EliteSportsTeamPackage teamPackage) {
		LOGGER.info("Entering setupTotalWager()");
		
		Map<String, String> tempMap = parseSelectField(doc.select("form select"));
		teamPackage.setGameTotalSelectName(tempMap.get("name"));

		final Elements options = doc.select("form select option");
		if (options != null && options.size() > 0) {
			Map<String, SelectOptionData> optionsMap = parseSelectOptionField(options);
			for (int x = 0; x < optionsMap.size(); x++) {
				final SelectOptionData option = optionsMap.get(Integer.toString(x));
				if (option != null && option.getData() != null && option.getData().length() > 0) {
					String optionData = option.getData();
					optionData = optionData.trim();
					if (!"Buy no points".equals(optionData)) {
						String pointTo = "point to ";
						int index = optionData.indexOf("point to ");
						if (index == -1) {
							index = optionData.indexOf("points to ");
							pointTo = "points to";
						}
						String totalOption = null;
						if (index != -1) {
							int fIndex = optionData.indexOf("for");
							if (fIndex != -1) {
								String firstPart = optionData.substring(index + pointTo.length(), fIndex);
								firstPart = indicator + firstPart.trim();
								String secondPart = optionData.substring(fIndex + "for".length());
								secondPart = secondPart.trim();
								totalOption = firstPart + " " + secondPart;
							}
						}
						LOGGER.debug("TotalOption: " + totalOption);

						// Buy 1½ points to  206  for -140
						teamPackage = (EliteSportsTeamPackage)parseTotalData(reformatValues(totalOption), x, " ", null, teamPackage);
					}
					
					// Setup the option value
					teamPackage.addGameTotalOptionValue(Integer.toString(x), option.getValue());
				} else {
					// Throw an exception
					throw new AppException(500, AppErrorCodes.SITE_PARSER_EXCEPTION,  
							AppErrorMessage.SITE_PARSER_EXCEPTION  + " Options are empty");					
				}
			}
		}

		LOGGER.debug("TeamPackage: " + teamPackage);
		LOGGER.info("Exiting setupTotalWager()");
	}

	/**
	 * 
	 * @param map
	 * @return
	 */
	private Map<String, String> removeInputFields(Map<String, String> map) {
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

					if (key != null) {
						if (value != null && value.contains("Continue") || value.contains("View Odds")) {
							if (counter++ > 0) {
							} else {
								deleteMap.put(key, value);
							}
						} else if (value != null && value.contains("Refesh") || value.contains("Refresh") || value.contains("Clear") || value.contains("clear") || value.contains("Cancel") || value.contains("cancel")) {
						} else {
							deleteMap.put(key, value);
						}
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

		return deleteMap;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#getGameData(org.jsoup.select.Elements)
	 */
	@Override
	protected List<EliteSportsEventPackage> getGameData(Elements elements) throws BatchException {
		LOGGER.info("Entering getGameData()");
		LOGGER.debug("Elements: " + elements);

		EliteSportsEventPackage eventPackage = null;
		final List<EliteSportsEventPackage> events = new ArrayList<EliteSportsEventPackage>();
		if (elements != null) {
			EliteSportsTeamPackage team1 = null;
			EliteSportsTeamPackage team2 = null;
			int t = 0;
			for (int x = 0; x < elements.size(); x++) {
				// Loop through the elements and then check for the dates
				final Element tr = elements.get(x);
				LOGGER.debug("tr: " + tr);

				if (tr != null) {
					Elements tds = tr.select("td font");
					if (tds != null && tds.size() == 8) {
						if (t++ == 0) {
							eventPackage = new EliteSportsEventPackage();
							team1 = new EliteSportsTeamPackage();
							getGameTeam(tr.select("td font"), team1);
							if (team1.getTeam() == null || team1.getTeam().length() == 0) {
								t = 0;
							} else {
								eventPackage.setId(team1.getId());	
							}
						} else {
							team2 = new EliteSportsTeamPackage();
							getGameTeam(tr.select("td font"), team2);
							
							if (team1 != null && team1.getTeam() != null &&
								team2 != null && team2.getTeam() != null) {
								Date eventDate = null;
								String cDate = team1.gettDate();
								LOGGER.debug("Team1: " + team1);
								LOGGER.debug("cDate: " + cDate);
	
								try {
									final Calendar now = Calendar.getInstance();
									int offset = now.get(Calendar.DST_OFFSET);
									cDate = team1.gettDate() + " " + String.valueOf(now.get(Calendar.YEAR)) + " " + team2.gettDate();
									cDate = cDate.replaceAll("\\(", "");
									cDate = cDate.replaceAll("\\)", "");
//									cDate += " " + timeZoneLookup(timezone, offset);
	
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

		LOGGER.info("List<EliteSportsEventPackage>: " + events);
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
	private int getGameTeam(Elements elements, EliteSportsTeamPackage team) throws BatchException {
		LOGGER.info("Entering getGameTeam()");
		LOGGER.debug("EliteSportsTeamPackage: " + team);
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
			LOGGER.info("elements.size(): " + elements.size());
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
	private EliteSportsTeamPackage getDate(Element td, EliteSportsTeamPackage team) {
		LOGGER.info("Entering getDate()");
		LOGGER.debug("Element: " + td);

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
	private EliteSportsTeamPackage getEventId(Element td, EliteSportsTeamPackage team) {
		LOGGER.info("Entering getEventId()");
		LOGGER.debug("Element: " + td);

		String eventId = getHtmlFromElement(td, "div", 0, true);
		if (eventId != null) {
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
	private EliteSportsTeamPackage getTeam(Element td, EliteSportsTeamPackage team) {
		LOGGER.info("Entering getTeam()");
		LOGGER.debug("Element: " + td);

		// Team String
		String teamName = td.html();
		LOGGER.debug("teamName: " + teamName);
		if (teamName != null) {
			// <span class="por-trail-blazers teamlogo"></span>
			int index = teamName.indexOf("</span>");
			if (index != -1) {
				teamName = teamName.substring(index + "</span>".length());
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
	private EliteSportsTeamPackage getSpread(Element td, EliteSportsTeamPackage team) throws BatchException {
		LOGGER.info("Entering getSpread()");
		LOGGER.debug("Element: " + td);

		// First get the input field information
		Map<String, String> hashMap = parseInputField(td.select("input"), 0);
		team.setGameSpreadInputId(hashMap.get("id"));
		team.setGameSpreadInputName(hashMap.get("name"));
		team.setGameSpreadInputValue(hashMap.get("value"));

		// Get the select fields
		hashMap = parseSelectField(td.select("select"));
		team.setGameSpreadSelectId(hashMap.get("id"));
		team.setGameSpreadSelectName(hashMap.get("name"));

		final Elements options = td.select("select option");
		if (options != null && options.size() > 0) {
			team = (EliteSportsTeamPackage)parseSpreadSelectOption(options, team, " ", null);
		} else {
			// -2½  +200; Now parse the data
			String data = getHtmlFromLastIndex(td, ">");
			team = (EliteSportsTeamPackage)parseSpreadData(reformatValues(data), 0, " ", null, team);
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
	private EliteSportsTeamPackage getTotal(Element td, EliteSportsTeamPackage team) throws BatchException {
		LOGGER.info("Entering getTotal()");
		LOGGER.debug("Element: " + td);

		// First get the input field information
		Map<String, String> hashMap = parseInputField(td.select("input"), 0);
		team.setGameTotalInputId(hashMap.get("id"));
		team.setGameTotalInputName(hashMap.get("name"));
		team.setGameTotalInputValue(hashMap.get("value"));

		// Get the select fields
		hashMap = parseSelectField(td.select("select"));
		team.setGameTotalSelectId(hashMap.get("id"));
		team.setGameTotalSelectName(hashMap.get("name"));

		final Elements options = td.select("select option");
		if (options != null && options.size() > 0) {
			team = (EliteSportsTeamPackage)parseTotalSelectOption(options, team, " ", null);
		} else {
			String data = getHtmlFromLastIndex(td, ">");
			LOGGER.debug("data: " + data);
			// Over 206½  -110
			int fIndex = data.indexOf(" ");
			if (fIndex != -1) {
				String ou = data.substring(0, fIndex);
				LOGGER.debug("ou: " + ou);
				if (ou.equalsIgnoreCase("over")) {
					team.addGameTotalOptionIndicator(Integer.toString(0), "o");		
				} else if (ou.equalsIgnoreCase("under")) {
					team.addGameTotalOptionIndicator(Integer.toString(0), "u");
				}
				data = data.substring(fIndex + " ".length());
				LOGGER.debug("data: " + data);
				fIndex = data.indexOf(" ");
				if (fIndex != -1) {
					String val = data.substring(0, fIndex);
					val = val.replaceAll("\u00BD", ".5");
					val = val.replaceAll("&nbsp;", "");
					LOGGER.debug("val: " + val);
					team.addGameTotalOptionNumber(Integer.toString(0), val);		
				}
				data = data.substring(fIndex + " ".length());
				LOGGER.debug("data: " + data);
				if (data != null) {
					data = data.trim();
					fIndex = data.indexOf("-");
					if (fIndex != -1) {
						LOGGER.debug("data: " + data);
						LOGGER.debug("fIndex: " + fIndex);
						team.addGameTotalOptionJuiceIndicator(Integer.toString(0), "-");
						team.addGameTotalOptionJuiceNumber(Integer.toString(0), data.substring(fIndex + 1));
					} else {
						fIndex = data.indexOf("+");
						if (fIndex != -1) {
							LOGGER.debug("data: " + data);
							LOGGER.debug("fIndex: " + fIndex);
							team.addGameTotalOptionJuiceIndicator(Integer.toString(0), "+");
							team.addGameTotalOptionJuiceNumber(Integer.toString(0), data.substring(fIndex + 1));							
						}
					}
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
	private EliteSportsTeamPackage getMoneyLine(Element td, EliteSportsTeamPackage team) {
		LOGGER.info("Entering getMoneyLine()");
		LOGGER.debug("Element: " + td);
		
		// First get the input field information
		final Map<String, String> hashMap = parseInputField(td.select("input"), 0);
		team.setGameMLInputId(hashMap.get("id"));
		team.setGameMLInputName(hashMap.get("name"));
		LinkedHashMap<String, String> mlValue = new LinkedHashMap<String, String>();
		mlValue.put("0", hashMap.get("value"));
		team.setGameMLInputValue(mlValue);

		// Parse ML Data
		String data = getHtmlFromLastIndex(td, ">");
		team = (EliteSportsTeamPackage)parseMlData(reformatValues(data), 0, team);

		LOGGER.info("Exiting getMoneyLine()");
		return team;
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
	private Map<String, String> findMenu(Elements divs, Map<String, String> map, String[] type, String sport, String foundString, String menuString) {
		LOGGER.info("Entering findMenu()");

		for (int x = 0; (divs != null && x < divs.size()); x++) {
			final Element div = divs.get(x);
			boolean foundDiv = false;
			for (int y = 0; y < type.length; y++) {
				foundDiv = foundSport(div, foundString, sport);
				LOGGER.debug("foundDiv: " + foundDiv);

				// Found the event
				if (foundDiv) {
					Map<String, String> hashMap = parseInputField(div.select(foundString + " input"), 0);
					map.put(hashMap.get("name"), "on");

					final Elements elements = div.select(foundString);
					for (int z = 0; z < elements.size(); z++) {
						Element a = elements.get(z);
						if (a != null) {
							String onclick = a.attr("onclick");
							if (onclick != null && onclick.contains("toggleCheckboxes")) {
								// i.e.: toggleCheckboxes('Basketball','NCAA','typediv_NCAA');
								int index = onclick.indexOf("'");
								if (index != -1) {
									onclick = onclick.substring(index + "'".length());
									int eindex = onclick.indexOf("'");
									if (eindex != -1) {
										map.put("sportType", onclick.substring(0, eindex));
										onclick = onclick.substring(eindex + "'".length());
										index = onclick.indexOf("'");
										if (index != -1) {
											onclick = onclick.substring(index + "'".length());
											eindex = onclick.indexOf("'");
											if (eindex != -1) {
												map.put("sportSubType", onclick.substring(0, eindex));
											}
										}
									}
								}
							}
						}
					}

					map = getMenuData(div, menuString, type[y], map);
					LOGGER.debug("Map: " + map);
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
	private boolean foundSport(Element div, String select, String sport) {
		LOGGER.info("Entering foundSport()");
		LOGGER.debug("select: " + select);
		LOGGER.debug("sport: " + sport);
		boolean foundDiv = false;

		String divData = getHtmlFromElement(div, select, 0, false);
		LOGGER.debug("divData: " + divData);

		// Check if we found div
		if (divData != null) {
			int index = divData.lastIndexOf(">");
			if (index != -1) {
				divData = divData.substring(index + ">".length());
				if (divData != null && divData.equals(sport)) {
					foundDiv = true;					
				}
			}
		}

		LOGGER.info("Exiting foundSport()");
		return foundDiv;
	}

	/**
	 * 
	 * @param div
	 * @param element
	 * @param map
	 * @return
	 */
	private Map<String, String> getMenuData(Element div, String element, String name, Map<String, String> map) {
		LOGGER.info("Entering getMenuData()");
		LOGGER.debug("Div: " + div);
		LOGGER.debug("element: " + element);

		Elements divdiv = div.select(element);
		for (int x = 0; (divdiv != null && x < divdiv.size()); x++) {
			Element sdiv = divdiv.get(x);
			if (sdiv != null) {
				Elements as = sdiv.select("a");
				for (int y = 0; (as != null && y < as.size()); y++) {
					Element a = as.get(y);
					if (a != null) {
						String menuName = a.html();
						menuName = menuName.replaceAll("&nbsp;", "");
						menuName = menuName.trim();
						if (menuName.equals(name)) {
							Map<String, String> hashMap = parseInputField(sdiv.select("input"), 0);
							map.put(hashMap.get("name"), "on");
						}
					}
				}				
			}
		}

		LOGGER.info("Exiting getMenuData()");
		return map;
	}
}