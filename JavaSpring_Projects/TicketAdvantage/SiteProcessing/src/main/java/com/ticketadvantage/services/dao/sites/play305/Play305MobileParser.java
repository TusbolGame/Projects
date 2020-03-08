/**
 * 
 */
package com.ticketadvantage.services.dao.sites.play305;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.select.Elements;

import com.ticketadvantage.services.dao.sites.SiteParser;
import com.ticketadvantage.services.dao.sites.SiteTeamPackage;
import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.PendingEvent;

/**
 * @author jmiller
 *
 */
public class Play305MobileParser extends SiteParser {
	private final static Logger LOGGER = Logger.getLogger(Play305MobileParser.class);
	// Thu 9/8  08:30PM
	private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss.SSS z");

	/**
	 * Constructor
	 */
	public Play305MobileParser() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			LOGGER.debug(Boolean.toString(true));
/*
			String[] NCAAF_SECOND_SPORT = new String[] { "2nd Halves" };
			String[] NCAAF_SECOND_NAME = new String[] { "2nd Half", "College Football", "College Football Extra" };
			String json = "{\"Items\": { \"2nd Halves\": { \"items\": [ { \"Sport\": \"Football\", \"SportSubType\": \"College Football\", \"IsCombined\": false, \"GroupLabel\": \"College Football\", \"PeriodNumber\": 2, \"PeriodDescription\": \"2nd Half\", \"IdSportType\": 98, \"IdType\": 1, \"IdLeague\": 46, \"LeagueName\": \"NCAA Football\", \"Rank\": 0, \"SequenceNumber\": 1042, \"BetMakerOrder\": 0, \"BetMakerExpand\": true, \"EventId\": 0, \"Time\": \"\", \"WebColumn\": 1, \"WebOrder\": 2 } ], \"IdLeague\": 46, \"Description\": \"2nd Halves\" }}}";
			MetallicaMobileParser metallicaMobileParser = new MetallicaMobileParser();
			Map<String, String> map = metallicaMobileParser.parseMenu(json, NCAAF_SECOND_NAME, NCAAF_SECOND_SPORT);
			Set<String> keys = map.keySet();
			Iterator<String> itr = keys.iterator();
			while (itr.hasNext()) {
				String key = itr.next();
				System.out.println("Key: " + key);
				System.out.println("Value: " + map.get(key));
			}
*/
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param xhtml
	 * @param accountName
	 * @param accountId
	 * @return
	 * @throws BatchException
	 */
	public Set<PendingEvent> parsePendingBets(String xhtml, String accountName, String accountId) throws BatchException {
		LOGGER.info("Entering parsePendingBets()");
		// TODO
		LOGGER.info("Exiting parsePendingBets()");
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseIndex(java.lang.String)
	 */
	@Override
	public Map<String, String> parseIndex(String xhtml) throws BatchException {
		LOGGER.info("Entering parseIndex()");
		final Map<String, String> retValue = new HashMap<String, String>();

		final JSONObject obj = new JSONObject(xhtml);
		final JSONObject credentials = obj.getJSONObject("credentials");
		retValue.put("USER", credentials.getString("USER"));
		retValue.put("PASS", credentials.getString("PASS"));
		
		LOGGER.info("Exiting parseIndex()");
		return retValue;
	}

	/**
	 * 
	 * @param json
	 * @return
	 * @throws BatchException
	 */
	public Map<String, String> parseToken(String json) throws BatchException {
		LOGGER.info("Entering parseToken()");
		final Map<String, String> retValue = new HashMap<String, String>();

		final JSONObject obj = new JSONObject(json);
		retValue.put("Token", obj.getString("Token"));

		LOGGER.info("Exiting parseToken()");
		return retValue;

	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseLogin(java.lang.String)
	 */
	@Override
	public Map<String, String> parseLogin(String xhtml) throws BatchException {
		LOGGER.info("Entering parseLogin()");
		final Map<String, String> retValue = new HashMap<String, String>();

		final JSONObject obj = new JSONObject(xhtml);
		final JSONObject accountInfo = obj.getJSONObject("accountInfo");
		retValue.put("customerID", accountInfo.getString("customerID"));
		retValue.put("Store", accountInfo.getString("Store"));
		retValue.put("AgentID", accountInfo.getString("AgentID"));

		LOGGER.info("Exiting parseLogin()");
		return retValue;
	}

	/**
	 * 
	 * @param json
	 * @return
	 * @throws BatchException
	 */
	public Map<String, String> parseAccountInfo(String json) throws BatchException {
		LOGGER.info("Entering parseAccountInfo()");
		final Map<String, String> retValue = new HashMap<String, String>();

		final JSONObject obj = new JSONObject(json);
		final JSONObject accountInfo = obj.getJSONObject("accountInfo");
		retValue.put("Store", accountInfo.getString("Store"));
		retValue.put("Office", accountInfo.getString("Office"));

		LOGGER.info("Exiting parseAccountInfo()");
		return retValue;

	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseMenu(java.lang.String, java.lang.String[], java.lang.String[])
	 */
	@Override
	public Map<String, String> parseMenu(String xhtml, String[] type, String[] sport) throws BatchException {
		LOGGER.info("Entering parseMenu()");
		LOGGER.debug("type: " + java.util.Arrays.toString(type));
		LOGGER.debug("sport: " + java.util.Arrays.toString(sport));
		final Map<String, String> labels = new HashMap<String, String>();

		// Get the document object
		final JSONObject obj = new JSONObject(xhtml);
		final JSONArray leagues = obj.getJSONArray("Leagues");
		int count = 0;

		if (leagues != null && leagues.length() > 0) {
			for (int x = 0; x < leagues.length(); x++) {
				final JSONObject league = leagues.getJSONObject(x);
				final String SportType = league.getString("SportType");
				final String SportSubType = league.getString("SportSubType");
				final String PeriodDescription = league.getString("PeriodDescription");

				int PeriodNumber = 0;
				if (!league.isNull("PeriodNumber")) {
					PeriodNumber = league.getInt("PeriodNumber");
				}

				for (int a = 0; a < sport.length; a++) {
					for (int b = 0; b < type.length; b++) {
						if (sport[a].equals(SportSubType)) {
							if (type[b].equals(PeriodDescription)) {
								labels.put("period" + count, PeriodDescription);
								labels.put("periodNumber" + count, Integer.toString(PeriodNumber));
								labels.put("sportType" + count, SportType);
								labels.put("sportSubType" + count, SportSubType);
								count++;
							}
						}
					}
				}
			}
		}

		LOGGER.info("Exiting parseMenu()");
		return labels;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseGames(java.lang.String, java.lang.String, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <SiteEventPackage> List<SiteEventPackage> parseGames(String xhtml, String type, Map<String, String> inputFields) throws BatchException {
		LOGGER.info("Entering parseGame()");
		LOGGER.debug("type: " + type);
		final List<com.ticketadvantage.services.dao.sites.SiteEventPackage> events = new ArrayList<com.ticketadvantage.services.dao.sites.SiteEventPackage>();

		// Get the document object
		final JSONObject obj = new JSONObject(xhtml);
		final JSONArray lines = obj.getJSONArray("Lines");
		
		for (int x = 0; x < lines.length(); x++) {
			final JSONObject line = lines.getJSONObject(x);
			final Play305EventPackage play305EventPackage = new Play305EventPackage();
			final Play305TeamPackage play305TeamPackage1 = new Play305TeamPackage();
			final Play305TeamPackage play305TeamPackage2 = new Play305TeamPackage();

			String FavoredTeamID = null;
			if (!line.isNull("FavoredTeamID")) {
				FavoredTeamID = line.getString("FavoredTeamID");
			}
			final String Team1ID = line.getString("Team1ID");
			final String Team2ID = line.getString("Team2ID");
			final String ShortName1 = line.getString("ShortName1");
			final String ShortName2 = line.getString("ShortName2");
			final int Team1RotNum = line.getInt("Team1RotNum");
			final int Team2RotNum = line.getInt("Team2RotNum");
			final int GameNum = line.getInt("GameNum");
			final String Status = line.getString("Status");
			play305EventPackage.setGameNum(GameNum);
			play305EventPackage.setStatus(Status);
			play305EventPackage.setFavoredTeamID(FavoredTeamID);
			play305TeamPackage1.setGameNum(GameNum);
			play305TeamPackage2.setGameNum(GameNum);
			play305TeamPackage1.setFavoredTeamID(FavoredTeamID);
			play305TeamPackage2.setFavoredTeamID(FavoredTeamID);
			play305TeamPackage1.setTeam(Team1ID);
			play305TeamPackage2.setTeam(Team2ID);
			play305TeamPackage1.setTeamshort(ShortName1);
			play305TeamPackage2.setTeamshort(ShortName2);
			play305TeamPackage1.setId(Team1RotNum);
			play305TeamPackage2.setId(Team2RotNum);

			// Spread
			getSpreadData(line, play305TeamPackage1, play305TeamPackage2);

			// Total
			getTotalData(line, play305TeamPackage1, play305TeamPackage2);

			// Money Line
			getMoneyLineData(line, play305TeamPackage1, play305TeamPackage2);

			// 2019-03-13 12:00:01.000
			Date gameDate = null;
			String gamedatetime = "";

			try {
				gamedatetime = line.getString("GameDateTime") + " " + determineTimeZone(super.timezone);
				gameDate = DATE_FORMAT.parse(gamedatetime);
			} catch (Throwable t) {
				gameDate = new Date();
			}

			play305EventPackage.setId(play305TeamPackage1.getId());
			play305EventPackage.setEventdatetime(gameDate);
			play305EventPackage.setDateofevent(gamedatetime);
			play305EventPackage.setTimeofevent(gamedatetime);
			play305EventPackage.setSpreadMax(new Integer(500));
			play305EventPackage.setTotalMax(new Integer(500));
			play305EventPackage.setMlMax(new Integer(500));
			play305TeamPackage1.setEventdatetime(gameDate);
			play305TeamPackage2.setEventdatetime(gameDate);
			play305TeamPackage1.setDateofevent(gamedatetime);
			play305TeamPackage2.setDateofevent(gamedatetime);

			play305EventPackage.setSiteteamone(play305TeamPackage1);
			play305EventPackage.setSiteteamtwo(play305TeamPackage2);
			events.add(play305EventPackage);
		}

		LOGGER.info("Exiting parseGame()");
		return (List<SiteEventPackage>)events;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseEventSelection(java.lang.String, com.ticketadvantage.services.dao.sites.SiteTeamPackage, java.lang.String)
	 */
	public Map<String, String> parseEventSelection(String xhtml, SiteTeamPackage siteTeamPackage, String type) throws BatchException {
		LOGGER.info("Entering parseEventSelection()");
		LOGGER.debug("xhtml: " + xhtml);
		final Map<String, String> map = new HashMap<String, String>();

		// Get the document object
		final JSONObject obj = new JSONObject(xhtml);
		final JSONArray lines = obj.getJSONArray("line");
		
		for (int x = 0; x < lines.length(); x++) {
			final JSONObject line = lines.getJSONObject(x);
			map.put("GameNum", Integer.toString(line.getInt("GameNum")));
			map.put("GameDateTime", line.getString("GameDateTime"));
			map.put("Status", line.getString("Status"));
			map.put("SportType", line.getString("SportType"));
			map.put("Team1ID", line.getString("Team1ID"));
			map.put("Team2ID", line.getString("Team2ID"));
			map.put("SportSubType", line.getString("SportSubType"));

			if (!line.isNull("Spread")) {
				map.put("Spread", Double.toString(line.getDouble("Spread")));
				map.put("SpreadAdj1", Double.toString(line.getDouble("SpreadAdj1")));
				map.put("SpreadAdj2", Double.toString(line.getDouble("SpreadAdj2")));
				map.put("SpreadDecimal1", Double.toString(line.getDouble("SpreadDecimal1")));
				map.put("SpreadDecimal2", Double.toString(line.getDouble("SpreadDecimal2")));
				map.put("SpreadNumerator1", Double.toString(line.getDouble("SpreadNumerator1")));
				map.put("SpreadNumerator2", Double.toString(line.getDouble("SpreadNumerator2")));
				map.put("SpreadDenominator1", Double.toString(line.getDouble("SpreadDenominator1")));
				map.put("SpreadDenominator2", Double.toString(line.getDouble("SpreadDenominator2")));
			}

			if (!line.isNull("TotalPoints")) {
				map.put("TotalPoints", Double.toString(line.getDouble("TotalPoints")));
				map.put("TtlPtsAdj1", Double.toString(line.getDouble("TtlPtsAdj1")));
				map.put("TtlPtsAdj2", Double.toString(line.getDouble("TtlPtsAdj2")));
				map.put("TtlPointsDecimal1", Double.toString(line.getDouble("TtlPointsDecimal1")));
				map.put("TtlPointsDecimal2", Double.toString(line.getDouble("TtlPointsDecimal2")));
				map.put("TtlPointsNumerator1", Double.toString(line.getDouble("TtlPointsNumerator1")));
				map.put("TtlPointsNumerator2", Double.toString(line.getDouble("TtlPointsNumerator2")));
				map.put("TtlPointsDenominator1", Double.toString(line.getDouble("TtlPointsDenominator1")));
				map.put("TtlPointsDenominator2", Double.toString(line.getDouble("TtlPointsDenominator2")));
			}

			if (!line.isNull("MoneyLine1") && !line.isNull("MoneyLine2")) {
				map.put("MoneyLine1", Double.toString(line.getDouble("MoneyLine1")));
				map.put("MoneyLine2", Double.toString(line.getDouble("MoneyLine2")));
				map.put("MoneyLineDecimal1", Double.toString(line.getDouble("MoneyLineDecimal1")));
				map.put("MoneyLineDecimal2", Double.toString(line.getDouble("MoneyLineDecimal2")));
				map.put("MoneyLineNumerator1", Double.toString(line.getDouble("MoneyLineNumerator1")));
				map.put("MoneyLineNumerator2", Double.toString(line.getDouble("MoneyLineNumerator2")));
				map.put("MoneyLineDenominator1", Double.toString(line.getDouble("MoneyLineDenominator1")));
				map.put("MoneyLineDenominator2", Double.toString(line.getDouble("MoneyLineDenominator2")));
			}

			map.put("Store", line.getString("Store"));
			map.put("CustProfile", line.getString("CustProfile"));
			map.put("PeriodNumber", Integer.toString(line.getInt("PeriodNumber")));
			map.put("PeriodDescription", line.getString("PeriodDescription"));

			if (!line.isNull("ListedPitcher1")) {
				map.put("ListedPitcher1", line.getString("ListedPitcher1"));
			}

			if (!line.isNull("ListedPitcher2")) {
				map.put("ListedPitcher2", line.getString("ListedPitcher2"));
			}

			map.put("ScheduleDate", line.getString("ScheduleDate"));
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
		LOGGER.info("Entering parseTicketTransaction()");
		String ticketNumber = null;

		// {"STATUS":{"STATE":1,"DOC":306433433}} 		
		final JSONObject obj = new JSONObject(xhtml);
		if (!obj.isNull("STATUS")) {
			final JSONObject status = obj.getJSONObject("STATUS");
			
			if (!status.isNull("DOC")) {
				int tixnum = status.getInt("DOC");
				ticketNumber = Integer.toString(tixnum);
			}
		}

		LOGGER.info("Exiting parseTicketNumber()");
		return ticketNumber;
	}

	/**
	 * 
	 * @param xhtml
	 * @return
	 */
	public Map<String, String> processLineChange(String xhtml) throws BatchException {
		LOGGER.info("Entering processLineChange()");
		final Map<String, String> map = new HashMap<String, String>();
						
//		<td align='left' style='border-top:transparent;padding:2px'>
//			<span class=thedate3 >10/25/2017 7:05 PM -  (EST)</span>
//			<br />
//			<span class=rotnumb >Denver Nuggets -2</span>
//			<span class=rotnumb > -110 for Game   </span>
//		</td>

		final JSONObject obj = new JSONObject(xhtml);
		final JSONArray Bets = obj.getJSONArray("Bets");
		for (int b = 0; b < Bets.length(); b++) {
			final JSONObject betItem = Bets.getJSONObject(b);
			final JSONObject Errors = betItem.getJSONObject("Errors");
			final JSONArray LineChanges = Errors.getJSONArray("LineChanges");

			for (int l = 0; l < LineChanges.length(); l++) {
				 // "BuyingOptions": [ 
				//		{ "Id": "5_184966698_9.5_-115_0_0_0", 
				//		  "Points": 9.5, 
				// 		  "Odds": -115, 
				//        "BuyPoints": 0 
				//		} 
				//	 ], "AutoAccepted": false, "PreviousId": "5_184966698_9.5_-110_0_0_0", "Id": "5_184966698_9.5_-115_0_0_0", "ErrorType": 1, "Description": ""
				final JSONObject lineErrors = LineChanges.getJSONObject(l);
				final JSONArray BuyingOptions = lineErrors.getJSONArray("BuyingOptions");
				for (int bo = 0; bo < BuyingOptions.length(); bo++) {
					final JSONObject buyingOption = BuyingOptions.getJSONObject(bo);
					String Id = buyingOption.getString("Id");
					Double Points = buyingOption.getDouble("Points");
					Double Odds = buyingOption.getDouble("Odds");

					map.put("Id", Id);
					String points = Points.toString();
					if (points.startsWith("-")) {
						map.put("valueindicator", "-");
						points = points.replace("-", "");
					} else {
						// Instead of + just make it empty
						map.put("valueindicator", "");
					}
					map.put("value", points);

					final Map<String, String> juice = parseJuice(Odds.toString(), null, null);
					if (juice.get("juiceindicator") == null) {
						juice.put("juiceindicator", "");
					}

					map.put("juiceindicator", juice.get("juiceindicator"));
					map.put("juice", juice.get("juice"));
				}
			}
		}

		LOGGER.info("Entering processLineChange()");
		return map;
	}

	/**
	 * 
	 * @param json
	 * @return
	 * @throws BatchException
	 */
	public void parseConfirmation(String json) throws BatchException {
		LOGGER.info("Entering parseConfirmation()");

		final JSONObject obj = new JSONObject(json);
		final String StatusDescription = obj.getString("StatusDescription");
		final int Status = obj.getInt("Status");
		
		if (!"ACCEPTED".equals(StatusDescription)) {
			LOGGER.error("StatusDescription: " + StatusDescription);
			LOGGER.error("Status: " + Status);
			throw new BatchException(BatchErrorCodes.FAILED_TO_GET_TICKET_NUMBER, BatchErrorMessage.FAILED_TO_GET_TICKET_NUMBER, "Transaction was not accepted, status code is " + Status, json);
		}

		LOGGER.info("Exiting parseConfirmation()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#getGameData(org.jsoup.select.Elements)
	 */
	@SuppressWarnings("unchecked")
	@Override	
	protected List<Play305EventPackage> getGameData(Elements elements) throws BatchException {
		LOGGER.info("Entering getGameData()");
		LOGGER.info("Exiting getGameData()");
		return null;
	}

	/**
	 * 
	 * @param line
	 * @param team1
	 * @param team2
	 */
	private void getSpreadData(JSONObject line, Play305TeamPackage team1, Play305TeamPackage team2) {
		LOGGER.info("Entering getSpreadData()");

		// Spreads
		if (!line.isNull("Spread")) {
			double spread = line.getDouble("Spread");
			double odds1 = line.getDouble("SpreadAdj1");
			double odds2 = line.getDouble("SpreadAdj2");
	
			// line
			if (spread >= 0) {
				if (team1.getFavoredTeamID().equals(team1.getTeam())) {
					team1.addGameSpreadOptionIndicator("0", "+");
					team2.addGameSpreadOptionIndicator("0", "-");
				} else {
					team1.addGameSpreadOptionIndicator("0", "-");
					team2.addGameSpreadOptionIndicator("0", "+");
				}
			} else {
				if (team1.getFavoredTeamID().equals(team1.getTeam())) {
					team1.addGameSpreadOptionIndicator("0", "-");
					team2.addGameSpreadOptionIndicator("0", "+");
				} else {
					team1.addGameSpreadOptionIndicator("0", "+");
					team2.addGameSpreadOptionIndicator("0", "-");
				}
			}
			LOGGER.debug("spread: " + spread);
			team1.addGameSpreadOptionNumber("0", Double.toString(Math.abs(spread)));
			team2.addGameSpreadOptionNumber("0", Double.toString(Math.abs(spread)));
	
			// Juice
			if (odds1 >= 0) {
				team1.addGameSpreadOptionJuiceIndicator("0", "+");
			} else {
				team1.addGameSpreadOptionJuiceIndicator("0", "-");
			}
			team1.addGameSpreadOptionJuiceNumber("0", Double.toString(Math.abs(odds1)));
	
			if (odds2 >= 0) {
				team2.addGameSpreadOptionJuiceIndicator("0", "+");
			} else {
				team2.addGameSpreadOptionJuiceIndicator("0", "-");
			}
			team2.addGameSpreadOptionJuiceNumber("0", Double.toString(Math.abs(odds2)));
	
			// Set the ID value
			team1.addGameSpreadOptionValue("0", "0");
			team2.addGameSpreadOptionValue("0", "0");
		}

		LOGGER.info("Exiting getSpreadData()");
	}

	/**
	 * 
	 * @param line
	 * @param team1
	 * @param team2
	 */
	private void getTotalData(JSONObject line, Play305TeamPackage team1, Play305TeamPackage team2) {
		LOGGER.info("Entering getTotalData()");

		// Spreads
		if (!line.isNull("TotalPoints")) {
			double total = line.getDouble("TotalPoints");
			double odds1 = line.getDouble("TtlPtsAdj1");
			double odds2 = line.getDouble("TtlPtsAdj2");
	
			team1.addGameTotalOptionIndicator("0", "o");
			team2.addGameTotalOptionIndicator("0", "u");
			team1.addGameTotalOptionNumber("0", Double.toString(total));
			team2.addGameTotalOptionNumber("0", Double.toString(total));
	
			// Juice
			if (odds1 >= 0) {
				team1.addGameTotalOptionJuiceIndicator("0", "+");
			} else {
				team1.addGameTotalOptionJuiceIndicator("0", "-");
			}
			team1.addGameTotalOptionJuiceNumber("0", Double.toString(Math.abs(odds1)));
	
			if (odds2 >= 0) {
				team2.addGameTotalOptionJuiceIndicator("0", "+");
			} else {
				team2.addGameTotalOptionJuiceIndicator("0", "-");
			}
			team2.addGameTotalOptionJuiceNumber("0", Double.toString(Math.abs(odds2)));
	
			// Set the ID value
			team1.addGameTotalOptionValue("0", "0");
			team2.addGameTotalOptionValue("0", "0");
		}

		LOGGER.info("Exiting getTotalData()");
	}

	/**
	 * 
	 * @param line
	 * @param team1
	 * @param team2
	 */
	private void getMoneyLineData(JSONObject line, Play305TeamPackage team1, Play305TeamPackage team2) {
		LOGGER.info("Entering getMoneyLineData()");

		if (!line.isNull("MoneyLine1") && !line.isNull("MoneyLine2")) {
			double ml1 = line.getDouble("MoneyLine1");
			double ml2 = line.getDouble("MoneyLine2");
	
			// ID
			team1.setGameMLInputId("0");
			team1.setGameMLInputName("0");
	
			// Juice
			if (ml1 >= 0) {
				team1.addGameMLOptionJuiceIndicator("0", "+");
			} else {
				team1.addGameMLOptionJuiceIndicator("0", "-");
			}
			team1.addGameMLOptionJuiceNumber("0", Double.toString(Math.abs(ml1)));
			team1.addGameMLInputValue("0", "0");
	
			// ID
			team2.setGameMLInputId("0");
			team2.setGameMLInputName("0");
	
			// Juice
			if (ml2 >= 0) {
				team2.addGameMLOptionJuiceIndicator("0", "+");
			} else {
				team2.addGameMLOptionJuiceIndicator("0", "-");
			}
			team2.addGameMLOptionJuiceNumber("0", Double.toString(Math.abs(ml2)));
			team2.addGameMLInputValue("0", "0");
		}

		LOGGER.info("Exiting getMoneyLineData()");
	}

	/**
	 * 
	 * @param z
	 * @param siteEventPackage
	 * @param team
	 * @param spreadMax
	 * @param mlMax
	 * @param totalMax
	 * @param events
	 */
	private void getSitePackage(int z, com.ticketadvantage.services.dao.sites.SiteEventPackage siteEventPackage, SiteTeamPackage team, Integer spreadMax, Integer mlMax, Integer totalMax, List<com.ticketadvantage.services.dao.sites.SiteEventPackage> events) {
		LOGGER.info("Entering getSitePackage()");

		if (z == 0) {
			siteEventPackage.setSiteteamone(team);
			siteEventPackage.setId(team.getId());
			siteEventPackage.setDateofevent(team.getDateofevent());
			siteEventPackage.setTimeofevent(team.getTimeofevent());
			siteEventPackage.setSpreadMax(spreadMax);
			siteEventPackage.setMlMax(mlMax);
			siteEventPackage.setTotalMax(totalMax);
		} else {
			siteEventPackage.setSiteteamtwo(team);
			events.add(siteEventPackage);
		}

		LOGGER.info("Exiting getSitePackage()");
	}
}