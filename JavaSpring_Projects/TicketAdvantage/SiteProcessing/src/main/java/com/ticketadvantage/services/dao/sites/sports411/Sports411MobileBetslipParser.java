/**
 * 
 */
package com.ticketadvantage.services.dao.sites.sports411;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ticketadvantage.services.dao.sites.SiteParser;
import com.ticketadvantage.services.dao.sites.SiteTeamPackage;
import com.ticketadvantage.services.errorhandling.AppErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;

/**
 * @author jmiller
 *
 */
public class Sports411MobileBetslipParser extends SiteParser {
	protected static final Logger LOGGER = Logger.getLogger(Sports411MobileBetslipParser.class);
	protected static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd HH:mm:ss z");

	/**
	 * Constructor
	 */
	public Sports411MobileBetslipParser() {
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
		final Map<String, String> map = new HashMap<String, String>();

		// Get the document object
		final Document doc = parseXhtml(xhtml);

		// Get hidden input fields
		final String[] types = new String[] { "text", "password", "image" };
		final Elements forms = doc.select("form");
		for(int x = 0; (forms != null && x < forms.size()); x++) {
			final Element form = forms.get(x);
			String action = form.attr("action");
			if (action != null && action.equals("/loginvalidate.aspx")) {
				// Get form action field
				map.put("action", form.attr("action"));
				getAllElementsByType(form, "input", types, map);
			}
		}
		map.put("account", "");

		LOGGER.info("Exiting parseIndex()");
		return map;
	}

	/**
	 * 
	 * @param xhtml
	 * @return
	 */
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
	 * @param actionName
	 * @return
	 * @throws BatchException
	 */
	public Map<String, String> parseLogin2(String xhtml, String actionName) throws BatchException {
		LOGGER.info("Entering parseLogin2()");
		final Map<String, String> map = new HashMap<String, String>();

		// Get the document object
		final Document doc = parseXhtml(xhtml);

		// Get hidden input fields
		final String[] types = new String[] { "hidden", "text" };
		final Elements forms = doc.select("form");
		for(int x = 0; (forms != null && x < forms.size()); x++) {
			final Element form = forms.get(x);
			String action = form.attr("action");
			if (action != null && action.equals(actionName)) {
				// Get form action field
				map.put("action", form.attr("action"));
				getAllElementsByType(form, "input", types, map);
			}
		}

		LOGGER.info("Exiting parseLogin2()");
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
		final Map<String, String> map = new HashMap<String, String>();

		// Get the document object
		xhtml = xhtml.replace("\\\"", "\"").replace("\"d\":\"", "\"d\":").replace("\"}\"}", "\"}}");
		LOGGER.debug("xhtml: " + xhtml);

		final JSONObject obj = new JSONObject(xhtml);
		final JSONObject d = obj.getJSONObject("d");
		final JSONObject activeleague = d.getJSONObject("ActiveLeagues");
		final JSONObject data = activeleague.getJSONObject("Data");
		final JSONObject leaguex = data.getJSONObject("Leagues");
		final JSONArray indexes = leaguex.getJSONArray("index");

		if (indexes != null && !indexes.isNull(0)) {
			for (int x = 0; x < indexes.length(); x++) {
				final JSONObject league = indexes.getJSONObject(x);
				final String value = league.getString("value");

				for (String sports : sport) {
					if (sports.equals(value)) {
						final JSONArray leaguess = league.getJSONArray("league");

						if (leaguess != null && !leaguess.isNull(0)) {
							for (int y = 0; y < leaguess.length(); y++) {
								final JSONObject leaguesss = leaguess.getJSONObject(y);
								final String desc = leaguesss.getString("desc");

								for (String types : type) {
									if (types.equals(desc)) {
										final String id = leaguesss.getString("id");
										map.put("id", id);
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
	public <Sports411EventPackage> List<Sports411EventPackage> parseGames(String xhtml, String type, Map<String, String> inputFields) throws BatchException {
		LOGGER.info("Entering parseGames()");
		LOGGER.debug("xhtml: " + xhtml);
		LOGGER.debug("type: " + type);
		LOGGER.debug("InputFields: " + inputFields);
		final List<com.ticketadvantage.services.dao.sites.sports411.Sports411EventPackage> events = new ArrayList<com.ticketadvantage.services.dao.sites.sports411.Sports411EventPackage>();

		xhtml = xhtml.replace("\\\"", "\"").replace("\"d\":\"", "\"d\":").replace("\"}\"}", "\"}}");
		LOGGER.debug("xhtml: " + xhtml);

		// Get the document object
		final JSONObject obj = new JSONObject(xhtml);
		final JSONObject dd = obj.getJSONObject("d");
		final JSONObject schedule = dd.getJSONObject("Schedule");
		final JSONObject data = schedule.getJSONObject("Data");
		final JSONObject leaguex = data.getJSONObject("Leagues");
		final JSONArray league = leaguex.getJSONArray("League");

		if (league != null && !league.isNull(0)) {
			for (int x = 0; x < league.length(); x++) {
				final JSONObject leaguep = league.getJSONObject(x);
				final String leagueDescription = leaguep.getString("Description");				
				final JSONArray dategroups = leaguep.getJSONArray("dateGroup");

				if (dategroups != null && !dategroups.isNull(0)) {
					for (int d = 0; d < dategroups.length(); d++) {
						final JSONObject dategroup = dategroups.getJSONObject(d);
						final JSONArray games = dategroup.getJSONArray("game");

						if (games != null && !games.isNull(0)) {
							for (int e = 0; e < games.length(); e++) {
								final JSONObject game = games.getJSONObject(e);
								final com.ticketadvantage.services.dao.sites.sports411.Sports411EventPackage eventPackage = new com.ticketadvantage.services.dao.sites.sports411.Sports411EventPackage();
								final Sports411TeamPackage team1 = new Sports411TeamPackage();
								final Sports411TeamPackage team2 = new Sports411TeamPackage();
								final Calendar now = Calendar.getInstance();
								int offset = now.get(Calendar.DST_OFFSET);
								String gmdt = game.getString("gmdt");
								String gmtm = game.getString("gmtm");

								String cDate = "";
								cDate = gmdt + " " + gmtm + " " + timeZoneLookup(timezone, offset);
								team1.settDate(cDate);
								team2.settDate(cDate);

								eventPackage.setDateofevent(gmdt);
								eventPackage.setTimeofevent(gmtm);
								eventPackage.setEventdate(gmtm + " " + gmtm);

								team1.setLeagueDesc(leagueDescription);
								team2.setLeagueDesc(leagueDescription);
								eventPackage.setLeagueDesc(leagueDescription);

								Date eventDate = null;
								try {
									eventDate = DATE_FORMAT.parse(cDate);
									LOGGER.debug("eventDate: " + eventDate);
								} catch (Throwable t) {
									LOGGER.error(t);
									Calendar cal = Calendar.getInstance();
									cal.setTime(new Date());
									cal.add(Calendar.DATE, 1); // minus number would decrement the days
									eventDate = cal.getTime();
								}
								eventPackage.setEventdatetime(eventDate);
								team1.setEventdatetime(eventDate);
								team2.setEventdatetime(eventDate);

								// Derivatives
								if (!game.isNull("Derivatives") && game.has("Derivatives")) {
									final JSONObject derivative = game.getJSONObject("Derivatives");

									if (!derivative.isNull("line") && derivative.has("line")) {
										final JSONArray derivatives = derivative.getJSONArray("line");
										eventPackage.setDerivatives(derivatives);
									}
								}

								// idspt : "MLB"
								final String idspt = game.getString("idspt");
								team1.setIdspt(idspt);
								team2.setIdspt(idspt);
								eventPackage.setIdspt(idspt);

								if (idspt.equals("MLB")) {
									team1.setCategoryDesc("BASEBALL");
									team2.setCategoryDesc("BASEBALL");
									eventPackage.setCategoryDesc("BASEBALL");
								} else if (idspt.equals("NFL")) {
									team1.setCategoryDesc("FOOTBALL");
									team2.setCategoryDesc("FOOTBALL");
									eventPackage.setCategoryDesc("FOOTBALL");
								} else if (idspt.equals("CFB")) {
									team1.setCategoryDesc("FOOTBALL");
									team2.setCategoryDesc("FOOTBALL");
									eventPackage.setCategoryDesc("FOOTBALL");
								} else if (idspt.equals("NBA")) {
									team1.setCategoryDesc("BASKETBALL");
									team2.setCategoryDesc("BASKETBALL");
									eventPackage.setCategoryDesc("BASKETBALL");
								} else if (idspt.equals("CBB")) {
									team1.setCategoryDesc("BASKETBALL");
									team2.setCategoryDesc("BASKETBALL");
									eventPackage.setCategoryDesc("BASKETBALL");
								} else if (idspt.equals("NHL")) {
									team1.setCategoryDesc("HOCKEY");
									team2.setCategoryDesc("HOCKEY");
									eventPackage.setCategoryDesc("HOCKEY");
								}

								// descgmtyp : "5k-3k-3k USD"
								// Correlated 5k-3k-3k USD
								String descgmtyp = game.getString("descgmtyp");
								if (descgmtyp != null && descgmtyp.length() > 0) {
									descgmtyp = descgmtyp.replace("Correlated", "").trim();
									descgmtyp = descgmtyp.replace("correlated", "").trim();
									int index = descgmtyp.indexOf("-");

									if (index != -1) {
										if (idspt != null && idspt.equals("MLB")) {
											final String mlMax = descgmtyp.substring(0, index).replace("k", "000").replace("K", "000").trim();
											eventPackage.setMlMax(Integer.parseInt(mlMax));
											team1.setMlMax(Integer.parseInt(mlMax));
											team2.setMlMax(Integer.parseInt(mlMax));
										} else {
											final String spreadMax = descgmtyp.substring(0, index).replace("k", "000").replace("K", "000").trim();
											eventPackage.setSpreadMax(Integer.parseInt(spreadMax));
											team1.setSpreadMax(Integer.parseInt(spreadMax));
											team2.setSpreadMax(Integer.parseInt(spreadMax));
										}

										descgmtyp = descgmtyp.substring(index + 1);
										index = descgmtyp.indexOf("-");
										if (index != -1) {
											final String totalMax = descgmtyp.substring(0, index).replace("k", "000").replace("K", "000").trim();
											eventPackage.setTotalMax(Integer.parseInt(totalMax));
											team1.setTotalMax(Integer.parseInt(totalMax));
											team2.setTotalMax(Integer.parseInt(totalMax));

											descgmtyp = descgmtyp.substring(index + 1).replace(" USD", "").replace("k", "000").replace("K", "000").trim();
											if (idspt != null && idspt.equals("MLB")) {
												final String spreadMax = descgmtyp;
												eventPackage.setSpreadMax(Integer.parseInt(spreadMax));
												team1.setSpreadMax(Integer.parseInt(spreadMax));
												team2.setSpreadMax(Integer.parseInt(spreadMax));
											} else {
												final String mlMax = descgmtyp;
												eventPackage.setMlMax(Integer.parseInt(mlMax));
												team1.setMlMax(Integer.parseInt(mlMax));
												team2.setMlMax(Integer.parseInt(mlMax));
											}
										}
									}
								}

								// famGame : "4623046"
								final String famGame = game.getString("famGame");
								final String idgp = game.getString("idgp");
								team1.setParentIdg(idgp);
								team2.setParentIdg(idgp);
								eventPackage.setParentIdg(idgp);

								// gpd : "Game"
								final String gpd = game.getString("gpd");
								final String gdesc = game.getString("gdesc");
								eventPackage.setGpd(gdesc);

								// Rotation #s
								// vnum : "951"
								// hnum : "952"
								final String vnum = game.getString("vnum");
								final String hnum = game.getString("hnum");
								team1.setId(Integer.parseInt(vnum));
								team1.setEventid(vnum);
								team2.setId(Integer.parseInt(hnum));
								team2.setEventid(hnum);
								eventPackage.setId(team1.getId());

								// Get pitchers if this is baseball
								// vpt : "F Valdez - L"
								// hpt : "L Lynn - R"
								if (!game.isNull("vpt") && !game.isNull("hpt")) {
									final String vpt = game.getString("vpt");
									team1.setPitcher(vpt);
									final String hpt = game.getString("hpt");
									team2.setPitcher(hpt);
								}

								// vtm : "Houston Astros"
								// htm : "Texas Rangers"
								if (!game.isNull("vtm") && !game.isNull("htm")) {
									final String vtm = game.getString("vtm");
									team1.setTeam(vtm);
									final String htm = game.getString("htm");
									team2.setTeam(htm);
								}

								// idlg : "5"
								final String idlg = game.getString("idlg");
								team1.setIdlg(idlg);
								team2.setIdlg(idlg);
								eventPackage.setIdlg(idlg);

								// idgm : "4623046"
								final String idgm = game.getString("idgm");
								team1.setBetId(idgm + "_" + idlg + "_");
								team2.setBetId(idgm + "_" + idlg + "_");
								eventPackage.setBetId(idgm + "_" + idlg + "_");
								team1.setIdGame(idgm);
								team2.setIdGame(idgm);
								eventPackage.setIdGame(idgm);
								team1.setIdCombinationGame(idgm);
								team2.setIdCombinationGame(idgm);
								eventPackage.setIdCombinationGame(idgm);
								
								// idgmtyp : "82"
								final String idgmtyp = game.getString("idgmtyp");
								team1.setGameTypeID(idgmtyp);
								team2.setGameTypeID(idgmtyp);
								eventPackage.setGameTypeID(idgmtyp);

								final JSONArray lines = game.getJSONArray("line");
								if (!lines.isNull(0)) {
									for (int i = 0; i < lines.length(); i++) {
										final JSONObject line = lines.getJSONObject(i);
										final String vsprdoddst = line.getString("vsprdoddst");
										final String vsprdt = line.getString("vsprdt");
										getSpreadData(idgm, i, vsprdt, vsprdoddst, team1);

										final String ovoddst = line.getString("ovoddst");
										final String ovt = line.getString("ovt");
										getTotalData(idgm, i, "o", ovt, ovoddst, team1);

										final String vsddsh = line.getString("voddsh");
										getMoneyLineData(idgm, i, vsddsh, team1);

										final String hsprdoddst = line.getString("hsprdoddst");
										final String hsprdt = line.getString("hsprdt");
										getSpreadData(idgm, i, hsprdt, hsprdoddst, team2);

										final String unoddst = line.getString("unoddst");
										final String unt = line.getString("unt");
										getTotalData(idgm, i, "u", unt, unoddst, team2);

										final String hoddsh = line.getString("hoddsh");
										getMoneyLineData(idgm, i, hoddsh, team2);
									}
								}

								eventPackage.setSiteteamone(team1);
								eventPackage.setSiteteamtwo(team2);
								eventPackage.setTeamone(team1);
								eventPackage.setTeamtwo(team2);
								eventPackage.setId(team1.getId());
								events.add(eventPackage);
							}
						}
					}
				}
			}
		}

		LOGGER.info("Exiting parseGames()");
		return (List<Sports411EventPackage>)events;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseEventSelection(java.lang.String, com.ticketadvantage.services.dao.sites.SiteTeamPackage, java.lang.String)
	 */
	@Override
	public Map<String, String> parseEventSelection(String xhtml, SiteTeamPackage siteTeamPackage, String type) throws BatchException {
		LOGGER.info("Entering parseEventSelection()");
		Map<String, String> map = new HashMap<String, String>();
		
		// Parse the HTML first
		final Document doc = parseXhtml(xhtml);

		final String[] types = new String[] { "hidden", "text", "submit", "number" };
		Element form = doc.getElementById("aspnetForm");
		if (form != null) {
			// Get form action field
			map.put("action", form.attr("action"));
			getAllElementsByTypeWithCheckbox(form, "input", types, map);
		}

		// Now get the select options if there are any
		if (type.equals("spread")) {
//			setupSpreadWager(doc, (Sports411TeamPackage) siteTeamPackage);
		} else if (type.equals("total")) {
//			setupTotalWager(doc, (Sports411TeamPackage) siteTeamPackage);
		}

		// Get the wager select
		final Elements selects = doc.select(".amounts select");
		LOGGER.debug("selects: " + selects);
		if (selects != null && selects.size() == 2) {
			parseSelectFieldByNumBlank(selects, 1, map);
		} else if (selects != null && selects.size() == 1) {
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

		// Check for a wager limit and change it 
		if (xhtml.contains("but this line has just changed to:")) {
			// Line changed throw exception
			throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed and cannot complete transaction", xhtml);			
		}
			
		// Check for a wager limit and change it 
		if (xhtml.contains("be accepted because Balance Exceeded")) {
			map.put("wagerbalanceexceeded", "true");
			return map;
		}

		// Remove the input fields that shouldn't be sent
//		map = removeInputFields(map);

		LOGGER.info("Exiting parseEventSelection()");
		return map;
	}

	/**
	 * 
	 * @param xhtml
	 * @param type
	 * @return
	 * @throws BatchException
	 */
	public Map<String, String> parseConfirmWager(String xhtml) throws BatchException {
		LOGGER.info("Entering parseConfirmWager()");
		Map<String, String> map = new HashMap<String, String>();

		// Parse the HTML first
		final Document doc = parseXhtml(xhtml);

		final String[] types = new String[] { "hidden", "submit", "password", "Password" };
		Element form = doc.getElementById("aspnetForm");
		if (form != null) {
			// Get form action field
			map.put("action", form.attr("action"));
			getAllElementsByType(form, "input", types, map);
		}

		// Check for a wager limit and change it 
		if (xhtml.contains("Exceeded. Limit For This Game is ")) {
			int index = xhtml.indexOf("Exceeded. Limit For This Game is ");
			if (index != -1) {
				xhtml = xhtml.substring(index + "Exceeded. Limit For This Game is ".length());
				index = xhtml.indexOf(" USD.");
				if (index != -1) {
					String wagerAmount = xhtml.substring(0, index);
					if (wagerAmount != null) {
						wagerAmount = wagerAmount.replaceAll(",", "");
						wagerAmount = wagerAmount.replaceAll(" ", "");
						map.put("wageramount", wagerAmount);
						return map;
					}
				}
			}
		}
		
		if (xhtml.contains("Max Wager Online Exceeded. Your Current Wager Limit is ")) {
			int index = xhtml.indexOf("Max Wager Online Exceeded. Your Current Wager Limit is ");
			if (index != -1) {
				xhtml = xhtml.substring(index + "Max Wager Online Exceeded. Your Current Wager Limit is ".length());
				index = xhtml.indexOf(" USD.");
				if (index != -1) {
					String wagerAmount = xhtml.substring(0, index);
					if (wagerAmount != null) {
						wagerAmount = wagerAmount.replaceAll(",", "");
						wagerAmount = wagerAmount.replaceAll(" ", "");
						map.put("wageramount", wagerAmount);
						return map;
					}
				}
			}
		}

		// Check for a wager limit and change it 
		if (xhtml.contains("Either `Risk` or `Win` Amount is below the ")) {
			int index = xhtml.indexOf("Either `Risk` or `Win` Amount is below the ");
			if (index != -1) {
				xhtml = xhtml.substring(index + "Either `Risk` or `Win` Amount is below the ".length());
				index = xhtml.indexOf(" USD");
				if (index != -1) {
					String wagerAmount = xhtml.substring(0, index);
					if (wagerAmount != null) {
						wagerAmount = wagerAmount.replaceAll(",", "");
						wagerAmount = wagerAmount.replaceAll(" ", "");
						map.put("wagerminamount", wagerAmount);
						return map;
					}
				}
			}
		}

		// Check for a wager limit and change it 
		if (xhtml.contains("but this line has just changed to:")) {
			// Line changed throw exception
			throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed and cannot complete transaction", xhtml);			
		}

		// Check for a wager limit and change it 
		if (xhtml.contains("be accepted because Balance Exceeded")) {
			map.put("wagerbalanceexceeded", "true");
			return map;
		}

		// Confirm the amounts
		Elements spans = doc.select(".evenRow td table tr td span");
		if (spans != null && spans.size() > 0) {
			Element risk = spans.get(2);
			map.put("risk", risk.html());
			Element win = spans.get(3);
			map.put("win", win.html());
		}

		map.remove("ctl00$Feedback1$ctl00$feedbackBtnSubmit"); 
		map.remove("ctl00$mainContent$btnContinueBottom"); 
		map.remove("ctl00$TimeZoneDesktop$ctl00$btnSetTimeZone");  

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

		// Check for a wager limit and change it 
		if (xhtml.contains("but this line has just changed to:")) {
			// Line changed throw exception
			throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed and cannot complete transaction", xhtml);			
		}

		// Check for a wager limit and change it 
		if (xhtml.contains("be accepted because Balance Exceeded")) {
			wagers.put("wagerbalanceexceeded", "true");
			return wagers;
		}

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

		// Check for a wager limit and change it 
		if (xhtml.contains("but this line has just changed to:")) {
			// Line changed throw exception
			throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed and cannot complete transaction", xhtml);			
		}

		// Parse to get the Document
		final Document doc = parseXhtml(xhtml);
		Elements spans = doc.select(".ticket");
		if (spans != null && spans.size() > 0) {
			Element span = spans.get(0);
			String tk = span.html();
			if (tk != null && tk.length() > 0) {
				ticketNumber = tk.trim();
			} else {
				throw new BatchException(BatchErrorCodes.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, BatchErrorMessage.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, tk, xhtml);
			}
		} else {
			throw new BatchException(BatchErrorCodes.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, BatchErrorMessage.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, ticketNumber, xhtml);
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
	protected List<Sports411EventPackage> getGameData(Elements elements) throws BatchException {
		LOGGER.info("Entering getGameData()");
		LOGGER.info("Exiting getGameData()");
		return null;
	}

	/**
	 * 
	 * @param span
	 * @param team
	 * @return
	 */
	protected Sports411EventPackage getMaxValues(Element span, Sports411EventPackage event) {
		LOGGER.info("Entering getMaxValues()");
		LOGGER.debug("span: " + span);

		// Setup max values
		String html = span.html();
		if (html != null && html.length() > 0) {
			int index = html.indexOf(" USD");
			if (index != -1) {
				html = html.substring(0, index);
				html = html.replaceAll("Correlated ", "");
				html = html.replaceAll("K", "000");
				html = html.replaceAll("k", "000");
				index = html.indexOf("-");
				LOGGER.debug("html: " + html);
				if (index != -1) {
					// 3k-3k-5k
					event.setSpreadMax(Integer.parseInt(html.substring(0, index)));
					html = html.substring(index + 1);
					LOGGER.debug("html: " + html);
					index = html.indexOf("-");
					if (index != -1) {
						event.setMlMax(Integer.parseInt(html.substring(0, index)));
						event.setTotalMax(Integer.parseInt(html.substring(index + 1)));
					}
				} else {
					final Integer maxAmount = Integer.parseInt(html);
					event.setSpreadMax(maxAmount);
					event.setTotalMax(maxAmount);
					event.setMlMax(maxAmount);
				}
			}
		}

		LOGGER.info("Exiting getMaxValues()");
		return event;
	}

	/**
	 * 
	 * @param lineItem
	 * @param team
	 */
	private void getSpreadData(String id, int num, String line, String odds, SiteTeamPackage team) {
		LOGGER.info("Entering getSpreadData()");

		team.setGameSpreadInputId(id);
		team.setGameSpreadInputName(id);
		team.setGameSpreadInputValue(id);

		// line
		if (line != null && line.startsWith("-")) {
			team.addGameSpreadOptionIndicator(Integer.toString(num), "-");
			team.addGameSpreadOptionNumber(Integer.toString(num), line.substring(1));
		} else {
			team.addGameSpreadOptionIndicator(Integer.toString(num), "+");
			team.addGameSpreadOptionNumber(Integer.toString(num), line);
		}
		

		// odds
		if (odds != null && odds.startsWith("-")) {
			team.addGameSpreadOptionJuiceIndicator(Integer.toString(num), "-");
			team.addGameSpreadOptionJuiceNumber(Integer.toString(num), odds.substring(1));
		} else {
			team.addGameSpreadOptionJuiceIndicator(Integer.toString(num), "+");
			team.addGameSpreadOptionJuiceNumber(Integer.toString(num), odds);
		}

		// Set the ID value
		team.addGameSpreadOptionValue(Integer.toString(num), id);

		LOGGER.info("Exiting getSpreadData()");
	}

	/**
	 * 
	 * @param id
	 * @param num
	 * @param odds
	 * @param team
	 */
	private void getMoneyLineData(String id, int num, String odds, SiteTeamPackage team) {
		LOGGER.info("Entering getMoneyLineData()");

		// ID
		team.setGameMLInputId(id);
		team.setGameMLInputName(id);

		// Juice
		if (odds != null && odds.startsWith("+")) {
			team.addGameMLOptionJuiceIndicator(Integer.toString(num), "+");
		} else {
			team.addGameMLOptionJuiceIndicator(Integer.toString(num), "-");
		}

		if (odds != null && odds.length() > 1) {
			team.addGameMLOptionJuiceNumber(Integer.toString(num), odds.substring(1));
		}
		team.addGameMLInputValue(Integer.toString(num), id);

		LOGGER.info("Exiting getMoneyLineData()");
	}

	/**
	 * 
	 * @param z
	 * @param lineItem
	 * @param team
	 */
	private void getTotalData(String id, int num, String ou, String line, String odds, SiteTeamPackage team) {
		LOGGER.info("Entering getTotalData()");

		team.setGameTotalInputId(id);
		team.setGameTotalInputName(id);
		team.setGameTotalInputValue(id);

		// line
		if (ou != null && ou.startsWith("o")) {
			team.addGameTotalOptionIndicator(Integer.toString(num), "o");
		} else {
			team.addGameTotalOptionIndicator(Integer.toString(num), "u");
		}
		team.addGameTotalOptionNumber(Integer.toString(num), line);

		// odds
		if (odds != null && odds.startsWith("-")) {
			team.addGameTotalOptionJuiceIndicator(Integer.toString(num), "-");
			team.addGameTotalOptionJuiceNumber(Integer.toString(num), odds.substring(1));
		} else {
			team.addGameTotalOptionJuiceIndicator(Integer.toString(num), "+");
			team.addGameTotalOptionJuiceNumber(Integer.toString(num), odds);
		}

		// Set the ID value
		team.addGameTotalOptionValue(Integer.toString(num), id);

		LOGGER.info("Exiting getTotalData()");
	}
}