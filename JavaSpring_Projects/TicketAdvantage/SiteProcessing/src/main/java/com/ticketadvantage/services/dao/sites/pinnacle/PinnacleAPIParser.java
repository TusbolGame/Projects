/**
 * 
 */
package com.ticketadvantage.services.dao.sites.pinnacle;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.select.Elements;

import com.ticketadvantage.services.dao.sites.SiteEventPackage;
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
public class PinnacleAPIParser extends SiteParser {
	private final static Logger LOGGER = Logger.getLogger(PinnacleAPIParser.class);

	/**
	 * Constructor
	 */
	public PinnacleAPIParser() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// Encode 
//			String asB64 = Base64.getEncoder().encodeToString("58061:jacob11".getBytes("utf-8"));
//			System.out.println(asB64); // Output will be: c29tZSBzdHJpbmc=
			final String json = "{\"sportId\":3,\"last\":534792012,\"leagues\":[{\"id\":246,\"events\":[{\"id\":871501173,\"periods\":[{\"lineId\":534792012,\"number\":0,\"cutoff\":\"2018-07-18T00:15:00Z\",\"maxSpread\":2000.0,\"maxMoneyline\":3000.0,\"maxTotal\":2000.0,\"maxTeamTotal\":1000.0,\"status\":1,\"spreads\":[{\"hdp\":-1.5,\"home\":175.0,\"away\":-195.0},{\"altLineId\":6550208034,\"hdp\":-2.5,\"home\":267.0,\"away\":-325.0},{\"altLineId\":6550208036,\"hdp\":-2.0,\"home\":236.0,\"away\":-278.0},{\"altLineId\":6550208038,\"hdp\":-1.0,\"home\":124.0,\"away\":-138.0}],\"moneyline\":{\"home\":-121.0,\"away\":112.0},\"totals\":[{\"points\":7.0,\"over\":-117.0,\"under\":106.0},{\"altLineId\":6550208035,\"points\":6.0,\"over\":-189.0,\"under\":167.0},{\"altLineId\":6550208037,\"points\":6.5,\"over\":-153.0,\"under\":137.0},{\"altLineId\":6550208039,\"points\":7.5,\"over\":113.0,\"under\":-127.0},{\"altLineId\":6550208041,\"points\":8.0,\"over\":134.0,\"under\":-151.0}],\"teamTotal\":{\"home\":{\"points\":3.5,\"over\":-111.0,\"under\":-109.0},\"away\":{\"points\":3.5,\"over\":-101.0,\"under\":-120.0}}}]}]}]}";
			final PinnacleAPIParser pap = new PinnacleAPIParser();
			PinnacleAPIEventPackage siteEventPackage = new PinnacleAPIEventPackage();
			PinnacleAPITeamPackage pinnacleAPITeamPackage1 = new PinnacleAPITeamPackage();
			PinnacleAPITeamPackage pinnacleAPITeamPackage2 = new PinnacleAPITeamPackage();
			siteEventPackage.setSiteteamone(pinnacleAPITeamPackage1);
			siteEventPackage.setSiteteamtwo(pinnacleAPITeamPackage2);
			siteEventPackage = pap.processOdds(json, "mlblines", siteEventPackage);
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
		LOGGER.info("Exiting parseIndex()");
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseLogin(java.lang.String)
	 */
	@Override
	public Map<String, String> parseLogin(String xhtml) throws BatchException {
		LOGGER.info("Entering parseLogin()");
		LOGGER.info("Exiting parseLogin()");
		return null;
	}

	/**
	 * 
	 * @param json
	 * @param sport
	 * @return
	 * @throws BatchException
	 */
	public Integer parseSport(String json, String[] sport) throws BatchException {
		LOGGER.info("Entering parseSport()");
		LOGGER.debug("sport: " + java.util.Arrays.toString(sport));
		Integer id = null;

		// Get the document object
		final JSONObject obj = new JSONObject(json);
		final JSONArray sports = obj.getJSONArray("sports");
		if (sports != null && sports.length() > 0) {
			for (int x = 0; x < sports.length(); x++) {
				final JSONObject sportsObject = (JSONObject)sports.get(x);
				final String sportName = sportsObject.getString("name");
				
				if (sportName != null && sportName.equals(sport[0])) {
					id = sportsObject.getInt("id");	
				}
			}
		}

		LOGGER.info("Entering parseSport()");
		return id;
	}

	/**
	 * 
	 * @param json
	 * @param sport
	 * @return
	 * @throws BatchException
	 */
	public Map<Integer, Integer> parseLeague(String json, String[] sport) throws BatchException {
		LOGGER.info("Entering parseLeague()");
		LOGGER.debug("sport: " + java.util.Arrays.toString(sport));
		final Map<Integer, Integer> leagues = new HashMap<Integer, Integer>();

		// Get the document object
		final JSONObject obj = new JSONObject(json);
		final JSONArray sports = obj.getJSONArray("leagues");
		if (sports != null && sports.length() > 0) {
			for (int x = 0; x < sports.length(); x++) {
				final JSONObject sportsObject = (JSONObject)sports.get(x);
				final String leagueName = sportsObject.getString("name");
				
				for (int y = 1; y < sport.length; y++) {
					if (leagueName != null && leagueName.equals(sport[y])) {
						final Integer id = sportsObject.getInt("id");
						leagues.put(id, id);
					}
				}
			}
		}

		LOGGER.info("Entering parseLeague()");
		return leagues;
	}

	/**
	 * 
	 * @param json
	 * @param types
	 * @return
	 * @throws BatchException
	 */
	public Integer parsePeriod(String json, String[] types) throws BatchException {
		LOGGER.info("Entering parsePeriod()");
		LOGGER.debug("types: " + java.util.Arrays.toString(types));
		Integer id = null;

		// Get the document object
		final JSONObject obj = new JSONObject(json);
		final JSONArray periods = obj.getJSONArray("periods");
		if (periods != null && periods.length() > 0) {
			for (int x = 0; x < periods.length(); x++) {
				final JSONObject sportsObject = (JSONObject)periods.get(x);
				final String sportName = sportsObject.getString("description");

				if (sportName != null && sportName.equals(types[0])) {
					id = sportsObject.getInt("number");	
				}
			}
		}

		LOGGER.info("Entering parsePeriod()");
		return id;
	}

	/**
	 * 
	 * @param json
	 * @return
	 * @throws BatchException
	 */
	public Integer parseFixture(String json, String[] types) throws BatchException {
		LOGGER.info("Entering parsePeriod()");
		Integer id = null;

		// Get the document object
		final JSONObject obj = new JSONObject(json);
		final JSONArray periods = obj.getJSONArray("periods");
		if (periods != null && periods.length() > 0) {
			for (int x = 0; x < periods.length(); x++) {
				final JSONObject sportsObject = (JSONObject)periods.get(x);
				final String sportName = sportsObject.getString("name");

				if (sportName != null && sportName.equals(types[0])) {
					id = sportsObject.getInt("id");	
				}
			}
		}

		LOGGER.info("Entering parsePeriod()");
		return id;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseMenu(java.lang.String, java.lang.String[], java.lang.String[])
	 */
	@Override
	public Map<String, String> parseMenu(String xhtml, String[] type, String[] sport) throws BatchException {
		LOGGER.info("Entering parseMenu()");
		LOGGER.info("Exiting parseMenu()");
		return null;
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

//		    "sportId": 3, 
//		    "last": 161401620,
//		    "league": [
//		        {
//		            "id": 246,
//		            "name": "MLB",
// 		            "events": [
//					{
//        				"id": 867831952,
//        				"starts": "2018-07-10T02:10:00Z",
//        				"home": "San Diego Padres",
//        				"away": "Los Angeles Dodgers",
//        				"rotNum": "907",
//        				"liveStatus": 2,
//        				"homePitcher": "L. Perdomo",
//        				"awayPitcher": "C. Kershaw",
//        				"status": "O",
//        				"parlayRestriction": 2,
//        				"altTeaser": false,
//        				"resultingUnit": "Regular"
//					}
		
//				}

		// Get the document object
		final JSONObject obj = new JSONObject(xhtml);
		final JSONArray leagues = obj.getJSONArray("league");
		if (leagues != null && leagues.length() > 0) {
			for (int x = 0; x < leagues.length(); x++) {
				final JSONObject league = (JSONObject)leagues.get(x);
				final JSONArray leagueevents = league.getJSONArray("events");
				if (leagueevents != null && leagueevents.length() > 0) {
					for (int y = 0; y < leagueevents.length(); y++) {
						final JSONObject event = (JSONObject)leagueevents.get(y);
						String starts = event.getString("starts");
						
						if (starts != null && starts.length() > 0) {
							SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ssz" );
					        
					        // this is zero time so we need to add that TZ indicator for 
					        if (starts.endsWith( "Z" )) {
					            starts = starts.substring(0, starts.length() - 1) + "GMT-00:00";
					        } else {
					            int inset = 6;
					        
					            String s0 = starts.substring(0, starts.length() - inset);
					            String s1 = starts.substring(starts.length() - inset, starts.length());

					            starts = s0 + "GMT" + s1;
					        }

					        try {
				        			final TimeZone zone = TimeZone.getTimeZone("America/Los_Angeles");
					        		final Date startDate = df.parse(starts);
					        		final Calendar gameDay = Calendar.getInstance();
					        		gameDay.setTimeZone(zone);
					        		gameDay.setTime(startDate);

					        		final Calendar today = Calendar.getInstance();
					        		today.setTimeZone(zone);
					        		today.set(Calendar.HOUR_OF_DAY, 0);
					        		today.set(Calendar.MINUTE, 0);
					        		today.set(Calendar.SECOND, 0);
					        		today.set(Calendar.MILLISECOND, 0);
					        		today.set(Calendar.AM_PM, Calendar.AM);

					        		if (gameDay.getTime().after(today.getTime())) {
					        			LOGGER.debug("AFTER!!!");
						        		LOGGER.debug("gameDate: " + gameDay.getTime());
						        		LOGGER.debug("today: " + today.getTime());

//				        				"id": 867831952,
//				        				"starts": "2018-07-10T02:10:00Z",
//				        				"home": "San Diego Padres",
//				        				"away": "Los Angeles Dodgers",
//				        				"rotNum": "907",
//				        				"liveStatus": 2,
//				        				"homePitcher": "L. Perdomo",
//				        				"awayPitcher": "C. Kershaw",
//				        				"status": "O",
//				        				"parlayRestriction": 2,
//				        				"altTeaser": false,
//				        				"resultingUnit": "Regular"

						        		final Integer rotNum = new Integer(event.getInt("rotNum"));
						        		
						        		if (rotNum.toString().length() == 3) {
							        		final PinnacleAPIEventPackage siteEventPackage = new PinnacleAPIEventPackage();
							        		siteEventPackage.setEventid(event.getInt("id"));
										siteEventPackage.setId(rotNum);
										siteEventPackage.setEventdatetime(gameDay.getTime());
										siteEventPackage.setEventdate(gameDay.getTime().toString());
										siteEventPackage.setEventtime(gameDay.getTime().toString());
										
										// Team 1
										final PinnacleAPITeamPackage team1 = new PinnacleAPITeamPackage();
										team1.setId(rotNum);
										team1.setEventdatetime(gameDay.getTime());
										team1.setDateofevent(gameDay.getTime().toString());
										team1.setEventid(rotNum.toString());
										team1.setTeam(event.getString("away"));
										if (!event.isNull("awayPitcher")) {
											team1.setPitcher(event.getString("awayPitcher"));
										}
										// Team 2
										final PinnacleAPITeamPackage team2 = new PinnacleAPITeamPackage();
										team2.setId(rotNum + 1);
										team2.setEventdatetime(gameDay.getTime());
										team2.setDateofevent(gameDay.getTime().toString());
										team2.setEventid(rotNum.toString());
										team2.setTeam(event.getString("home"));
										if (!event.isNull("homePitcher")) {
											team1.setPitcher(event.getString("homePitcher"));
										}

										siteEventPackage.setSiteteamone(team1);
										siteEventPackage.setSiteteamtwo(team2);

										LOGGER.debug("siteEventPackage: " + siteEventPackage);
										events.add(siteEventPackage);
						        		}
					        		}
					        } catch (ParseException pe) {
					        		LOGGER.error(pe.getMessage(), pe);
					        }
						}
					}
				}
			}
		}

		LOGGER.info("Exiting parseGame()");
		return (List<SiteEventPackage>)events;
	}

	/**
	 * 
	 * @param json
	 * @param type
	 * @param siteEventPackage
	 * @return
	 * @throws BatchException
	 */
	public PinnacleAPIEventPackage processOdds(String json, String type, PinnacleAPIEventPackage siteEventPackage) throws BatchException {
		LOGGER.info("Entering processOdds()");

		// Get the document object
		final JSONObject obj = new JSONObject(json);
		final JSONArray leagues = obj.getJSONArray("leagues");
		if (leagues != null && leagues.length() > 0) {
			
			for (int x = 0; x < leagues.length(); x++) {
				final JSONObject league = (JSONObject)leagues.get(x);
				final JSONArray leagueevents = league.getJSONArray("events");

				if (leagueevents != null && leagueevents.length() > 0) {
					for (int y = 0; y < leagueevents.length(); y++) {
						final JSONObject event = (JSONObject)leagueevents.get(y);
						final JSONArray periods = event.getJSONArray("periods");

						for (int z = 0; z < periods.length(); z++) {
							final JSONObject period = (JSONObject)periods.get(z);
							int number = period.getInt("number");

							if (number == 0 && type.contains("lines") || 
								number == 1 && type.contains("first") ||
								number == 2 && type.contains("second") ||
								number == 3 && type.contains("third")) {
								final PinnacleAPITeamPackage awayTeam = (PinnacleAPITeamPackage)siteEventPackage.getSiteteamone();
								final PinnacleAPITeamPackage homeTeam = (PinnacleAPITeamPackage)siteEventPackage.getSiteteamtwo();
								LOGGER.debug("awayTeam: " + awayTeam);
								LOGGER.debug("homeTeam: " + homeTeam);
	
								final Integer lineId = period.getInt("lineId");
								siteEventPackage.setLineId(lineId);
								awayTeam.setLineId(period.getInt("lineId"));
								homeTeam.setLineId(period.getInt("lineId"));
								awayTeam.setNumber(period.getInt("number"));
								homeTeam.setNumber(period.getInt("number"));
	
								if (!period.isNull("status")) {
									awayTeam.setStatus(period.getInt("status"));
									homeTeam.setStatus(period.getInt("status"));
								}
	
								if (!period.isNull("maxMoneyline")) {
									final Integer maxMl = period.getInt("maxMoneyline");
									siteEventPackage.setMlMax(maxMl);
								}
								if (!period.isNull("maxSpread")) {
									final Integer maxSpread = period.getInt("maxSpread");
									siteEventPackage.setSpreadMax(maxSpread);
								}
								if (!period.isNull("maxTotal")) {
									final Integer maxTotal = period.getInt("maxTotal");
									siteEventPackage.setTotalMax(maxTotal);
								}
								
								//newly added condition to set maxTeamTotal 12-11-18
								if (!period.isNull("maxTeamTotal")) {
									final Integer maxTeamTotal = period.getInt("maxTeamTotal");
									siteEventPackage.setTeamTotalMax(maxTeamTotal);
								}
								// Get the spread data
								if (!period.isNull("spreads")) {
									final JSONArray spreads = period.getJSONArray("spreads");
									getSpreadData(lineId, spreads, awayTeam, homeTeam);
								}
	
								// Get the total data
								if (!period.isNull("moneyline")) {
									final JSONObject ml = period.getJSONObject("moneyline");
									getMoneyLineData(lineId, ml, awayTeam, homeTeam);
								}
	
								// Get the total data
								if (!period.isNull("totals")) {
									final JSONArray totals = period.getJSONArray("totals");
									getTotalData(lineId, totals, awayTeam, homeTeam);
								}
								
								if (!period.isNull("teamTotal")) {
									final JSONObject teamTotals = period.getJSONObject("teamTotal");
									getTeamTotalData(lineId, teamTotals, awayTeam, homeTeam);
								}
							}
						}
					}
				}
			}
		}

		LOGGER.info("Exiting processOdds()");
		return siteEventPackage;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseEventSelection(java.lang.String, com.ticketadvantage.services.dao.sites.SiteTeamPackage, java.lang.String)
	 */
	public Map<String, String> parseEventSelection(String xhtml, SiteTeamPackage siteTeamPackage, String type) throws BatchException {
		LOGGER.info("Entering parseEventSelection()");
		LOGGER.info("Exiting parseEventSelection()");
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseTicketNumber(java.lang.String)
	 */
	@Override
	public String parseTicketNumber(String xhtml) throws BatchException {
		LOGGER.info("Entering parseTicketTransaction()");
		String ticketNumber = null;

		final JSONObject obj = new JSONObject(xhtml);
		if (!obj.isNull("status")) {
			final String status = obj.getString("status");
			if (status != null && status.endsWith("ACCEPTED")) {
				final JSONObject straightBet = obj.getJSONObject("straightBet");
				final Integer betId = straightBet.getInt("betId");
				ticketNumber = betId.toString();
				LOGGER.debug("ticketNumber: " + ticketNumber);
			} else if (status != null && status.contains("PROCESSED_WITH_ERROR")) {
				final String errorCode = obj.getString("errorCode");
				if (errorCode != null && errorCode.contains("LINE_CHANGED")) {
					LOGGER.error("LINE CHANGE");
					throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed", xhtml);
				} else {
					throw new BatchException(BatchErrorCodes.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, BatchErrorMessage.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, "No Ticket Number", xhtml);
				}
			} else {
				throw new BatchException(BatchErrorCodes.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, BatchErrorMessage.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, "No Ticket Number", xhtml);
			}
		} else {
			throw new BatchException(BatchErrorCodes.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, BatchErrorMessage.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, "No Ticket Number", xhtml);
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
		LOGGER.info("Entering processLineChange()");
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#getGameData(org.jsoup.select.Elements)
	 */
	@SuppressWarnings("unchecked")
	@Override	
	protected List<SiteEventPackage> getGameData(Elements elements) throws BatchException {
		LOGGER.info("Entering getGameData()");
		LOGGER.info("Exiting getGameData()");
		return null;
	}

	/**
	 * 
	 * @param lineId
	 * @param spreads
	 * @param team1
	 * @param team2
	 */
	private void getSpreadData(Integer lineId, JSONArray spreads, SiteTeamPackage awayteam, SiteTeamPackage hometeam) {
		LOGGER.info("Entering getSpreadData()");

		// Spreads
		for (int x = 0; x < spreads.length(); x++) {
			final JSONObject spread = (JSONObject)spreads.get(x);
			final Double hdp = spread.getDouble("hdp");
			final Double away = spread.getDouble("away");
			final Double home = spread.getDouble("home");
			LOGGER.debug("hdp: " + hdp);
			LOGGER.debug("home: " + home);
			LOGGER.debug("away: " + away);

			// line
			if (hdp >= 0) {
				awayteam.addGameSpreadOptionIndicator(Integer.toString(x), "-");
				hometeam.addGameSpreadOptionIndicator(Integer.toString(x), "+");
			} else {
				hometeam.addGameSpreadOptionIndicator(Integer.toString(x), "-");
				awayteam.addGameSpreadOptionIndicator(Integer.toString(x), "+");
			}
			
			awayteam.addGameSpreadOptionNumber(Integer.toString(x), Double.toString(Math.abs(hdp)));
			hometeam.addGameSpreadOptionNumber(Integer.toString(x), Double.toString(Math.abs(hdp)));

			// Juice
			if (away >= 0) {
				awayteam.addGameSpreadOptionJuiceIndicator(Integer.toString(x), "+");
			} else {
				awayteam.addGameSpreadOptionJuiceIndicator(Integer.toString(x), "-");
			}
			awayteam.addGameSpreadOptionJuiceNumber(Integer.toString(x), Double.toString(Math.abs(away)));

			if (home >= 0) {
				hometeam.addGameSpreadOptionJuiceIndicator(Integer.toString(x), "+");
			} else {
				hometeam.addGameSpreadOptionJuiceIndicator(Integer.toString(x), "-");
			}
			hometeam.addGameSpreadOptionJuiceNumber(Integer.toString(x), Double.toString(Math.abs(home)));

			// Default the option value
			String optionValue = lineId.toString();

			// Set the ID value
			if (!spread.isNull("altLineId")) {
				final BigInteger altLineId = spread.getBigInteger("altLineId");
				LOGGER.error("altLineId: " + altLineId);
				optionValue = altLineId.toString();
			}

			// Set the ID value
			awayteam.addGameSpreadOptionValue(Integer.toString(x), optionValue);
			hometeam.addGameSpreadOptionValue(Integer.toString(x), optionValue);				
		}

		LOGGER.info("Exiting getSpreadData()");
	}
	
	/**
	 * 
	 * @param lineId
	 * @param teamTotal
	 * @param awayteam
	 * @param hometeam
	 * newly added method on 12-11-18
	 */
	private void getTeamTotalData(Integer lineId, JSONObject teamTotal, SiteTeamPackage awayteam, SiteTeamPackage hometeam) {
		LOGGER.info("Entering getTeamTotalData()");

		// Team Totals
			JSONObject team1 = (JSONObject)teamTotal.get("away");
			JSONObject team2 = (JSONObject)teamTotal.get("home");
			
			//away
			final Double points = team1.getDouble("points");
			final Double over = team1.getDouble("over");
			final Double under = team1.getDouble("under");
			
			//home
			final Double homepoints = team2.getDouble("points");
			final Double homeover = team2.getDouble("over");
			final Double homeunder = team2.getDouble("under");
			
			LOGGER.debug("awaypoints: " + points);
			LOGGER.debug("awayover: " + over);
			LOGGER.debug("awayunder: " + under);
			
			LOGGER.debug("homepoints: " + homepoints);
			LOGGER.debug("homeawayover: " + homeover);
			LOGGER.debug("homeawayunder: " + homeunder);
			
		setTeamTotalData(lineId,teamTotal,points,over,under,awayteam);
		setTeamTotalData(lineId,teamTotal,homepoints,homeover,homeunder,hometeam);
		LOGGER.info("Exiting getTeamTotalData()");
	}

	//newly added method on 13-11-18
	private void setTeamTotalData(Integer lineId, JSONObject teamTotal, Double points, Double over, Double under, SiteTeamPackage team){
		// team totals
		team.addGameTeamTotalOptionIndicator("0", "o"); 
		team.addGameTeamTotalOptionIndicatorSecond("0", "u");
		
		team.addGameTeamTotalOptionNumber("0", Double.toString(Math.abs(points)));
		team.addGameTeamTotalOptionNumberSecond("0", Double.toString(Math.abs(points)));

		// Juice
		if (over >= 0) {
			team.addGameTeamTotalOptionJuiceIndicator("0", "+");
		} else {
			team.addGameTeamTotalOptionJuiceIndicator("0", "-");
		}
		team.addGameTeamTotalOptionJuiceNumber("0", Double.toString(Math.abs(over)));

		// Juice
		if (under >= 0) {
			team.addGameTeamTotalOptionJuiceIndicatorSecond("0", "+");
		} else {
			team.addGameTeamTotalOptionJuiceIndicatorSecond("0", "-");
		}
		team.addGameTeamTotalOptionJuiceNumberSecond("0", Double.toString(Math.abs(under)));

		// Default the option value
		String optionValue = lineId.toString();

		// Set the ID value
		if (!teamTotal.isNull("altLineId")) {
			BigInteger altLineId = teamTotal.getBigInteger("altLineId");
			LOGGER.error("altLineId: " + altLineId);
			optionValue = altLineId.toString();
		}
		team.addGameTeamTotalOptionValue("0", optionValue);
		team.addGameTeamTotalOptionValueSecond("0", optionValue);				
	}
	
	/**
	 * 
	 * @param lineId
	 * @param ml
	 * @param awayteam
	 * @param hometeam
	 */
	private void getMoneyLineData(Integer lineId, JSONObject ml, SiteTeamPackage awayteam, SiteTeamPackage hometeam) {
		LOGGER.info("Entering getMoneyLineData()");

		// Default the option value
		String optionValue = lineId.toString();

		// Set the ID value
		if (!ml.isNull("altLineId")) {
			final BigInteger altLineId = ml.getBigInteger("altLineId");
			LOGGER.error("altLineId: " + altLineId);
			optionValue = altLineId.toString();
		}

		// Set the ID value
		awayteam.setGameMLInputId(optionValue);
		awayteam.setGameMLInputName(optionValue);
		hometeam.setGameMLInputId(optionValue);
		hometeam.setGameMLInputName(optionValue);

		final Double away = ml.getDouble("away");
		final Double home = ml.getDouble("home");
		LOGGER.debug("awayml: " + away);
		LOGGER.debug("homeml: " + home);

		// Juice
		if (away >= 0) {
			awayteam.addGameMLOptionJuiceIndicator("0", "+");
		} else {
			awayteam.addGameMLOptionJuiceIndicator("0", "-");
		}
		awayteam.addGameMLOptionJuiceNumber("0", Double.toString(Math.abs(away)));
		awayteam.addGameMLInputValue("0", optionValue);

		// Juice
		if (home >= 0) {
			hometeam.addGameMLOptionJuiceIndicator("0", "+");
		} else {
			hometeam.addGameMLOptionJuiceIndicator("0", "-");
		}
		hometeam.addGameMLOptionJuiceNumber("0", Double.toString(Math.abs(home)));
		hometeam.addGameMLInputValue("0", optionValue);

		LOGGER.info("Exiting getMoneyLineData()");
	}

	/**
	 * 
	 * @param lineId
	 * @param totals
	 * @param awayteam
	 * @param hometeam
	 */
	private void getTotalData(Integer lineId, JSONArray totals, SiteTeamPackage awayteam, SiteTeamPackage hometeam) {
		LOGGER.info("Entering getTotalData()");

		// Totals
		for (int x = 0; x < totals.length(); x++) {
			final JSONObject total = (JSONObject)totals.get(x);
			final Double points = total.getDouble("points");
			final Double over = total.getDouble("over");
			final Double under = total.getDouble("under");

			LOGGER.debug("points: " + points);
			LOGGER.debug("over: " + over);
			LOGGER.debug("under: " + under);

			// totals
			awayteam.addGameTotalOptionIndicator(Integer.toString(x), "o");
			hometeam.addGameTotalOptionIndicator(Integer.toString(x), "u");
			
			awayteam.addGameTotalOptionNumber(Integer.toString(x), Double.toString(Math.abs(points)));
			hometeam.addGameTotalOptionNumber(Integer.toString(x), Double.toString(Math.abs(points)));
	
			// Juice
			if (over >= 0) {
				awayteam.addGameTotalOptionJuiceIndicator(Integer.toString(x), "+");
			} else {
				awayteam.addGameTotalOptionJuiceIndicator(Integer.toString(x), "-");
			}
			awayteam.addGameTotalOptionJuiceNumber(Integer.toString(x), Double.toString(Math.abs(over)));

			// Juice
			if (under >= 0) {
				hometeam.addGameTotalOptionJuiceIndicator(Integer.toString(x), "+");
			} else {
				hometeam.addGameTotalOptionJuiceIndicator(Integer.toString(x), "-");
			}
			hometeam.addGameTotalOptionJuiceNumber(Integer.toString(x), Double.toString(Math.abs(under)));

			// Default the option value
			String optionValue = lineId.toString();

			// Set the ID value
			if (!total.isNull("altLineId")) {
				BigInteger altLineId = total.getBigInteger("altLineId");
				LOGGER.error("altLineId: " + altLineId);
				optionValue = altLineId.toString();
			}

			awayteam.addGameTotalOptionValue(Integer.toString(x), optionValue);
			hometeam.addGameTotalOptionValue(Integer.toString(x), optionValue);				
		}

		LOGGER.info("Exiting getTotalData()");
	}
}