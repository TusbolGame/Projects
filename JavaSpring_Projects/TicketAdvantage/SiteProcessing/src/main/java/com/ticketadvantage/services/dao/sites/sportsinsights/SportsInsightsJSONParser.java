/**
 * 
 */
package com.ticketadvantage.services.dao.sites.sportsinsights;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.select.Elements;

import com.ticketadvantage.services.dao.sites.SiteParser;
import com.ticketadvantage.services.dao.sites.SiteTeamPackage;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.ClosingLine;
import com.ticketadvantage.services.model.EventPackage;
import com.ticketadvantage.services.model.SportsInsightsEvent;
import com.ticketadvantage.services.model.SportsInsightsValues;
import com.ticketadvantage.services.model.TeamPackage;

/**
 * @author jmiller
 *
 */
public class SportsInsightsJSONParser extends SiteParser {
	private static final Logger LOGGER = Logger.getLogger(SportsInsightsJSONParser.class);
	private static final Map<String, String> NFLMapping = new HashMap<String, String>();
	private static final Map<String, String> MLBMapping = new HashMap<String, String>();
	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

	static {
		NFLMapping.put("ATL", "Atlanta Falcons");
		NFLMapping.put("ARI", "Arizona Cardinals");
		NFLMapping.put("BAL", "Baltimore Ravens");
		NFLMapping.put("BUF", "Buffalo Bills");
		NFLMapping.put("CAR", "Carolina Panthers");
		NFLMapping.put("CHI", "Chicago Bears");
		NFLMapping.put("CIN", "Cincinnati Bengals");
		NFLMapping.put("CLE", "Cleveland Browns");
		NFLMapping.put("DAL", "Dallas Cowboys");
		NFLMapping.put("DEN", "Denver Broncos");
		NFLMapping.put("DET", "Detroit Lions");
		NFLMapping.put("GB", "Green Bay Packers");
		NFLMapping.put("HOU", "Houston Texans");
		NFLMapping.put("IND", "Indianapolis Colts");
		NFLMapping.put("JAX", "Jacksonville Jaguars");
		NFLMapping.put("KC", "Kansas City Chiefs");
		NFLMapping.put("LA", "Los Angeles Rams");
		NFLMapping.put("MIA", "Miami Dolphins");
		NFLMapping.put("MIN", "Minnesota Vikings");
		NFLMapping.put("NE", "New England Patriots");
		NFLMapping.put("NO", "New Orleans Saints");
		NFLMapping.put("NYG", "New York Giants");
		NFLMapping.put("NYJ", "New York Jets");
		NFLMapping.put("OAK", "Oakland Raiders");
		NFLMapping.put("PHI", "Philadelphia Eagles");
		NFLMapping.put("PIT", "Pittsburgh Steelers"); 
		NFLMapping.put("SD", "San Diego Chargers");
		NFLMapping.put("SF", "San Francisco 49ers");
		NFLMapping.put("SEA", "Seattle Seahawks");
		NFLMapping.put("TB", "Tampa Bay Buccaneers");
		NFLMapping.put("TEN", "Tennessee Titans");
		NFLMapping.put("WAS", "Washington Redskins");

		MLBMapping.put("ATL", "Atlanta Braves");
		MLBMapping.put("ARI", "Arizona Diamondbacks");
		MLBMapping.put("BAL", "Baltimore Orioles");
		MLBMapping.put("BOS", "Boston Red Sox");
		MLBMapping.put("CHC", "Chicago Cubs");
		MLBMapping.put("CHW", "Chicago White Sox");
		MLBMapping.put("CIN", "Cincinnati Reds");
		MLBMapping.put("CLE", "Cleveland Indians");
		MLBMapping.put("COL", "Colorado Rockies");
		MLBMapping.put("DET", "Detroit Tigers");
		MLBMapping.put("HOU", "Houston Astros");
		MLBMapping.put("JAX", "Jacksonville Jaguars");
		MLBMapping.put("KC", "Kansas City Royals");
		MLBMapping.put("LAA", "Los Angeles Angels");
		MLBMapping.put("LAD", "Los Angeles Dodgers");
		MLBMapping.put("MIA", "Miami Marlins");
		MLBMapping.put("MIL", "Milwaukee Brewers");
		MLBMapping.put("MIN", "Minnesota Twins");
		MLBMapping.put("NYM", "New York Mets");
		MLBMapping.put("NYY", "New York Yankees");
		MLBMapping.put("OAK", "Oakland Athletics");
		MLBMapping.put("PHI", "Philadelphia Phillies");
		MLBMapping.put("PIT", "Pittsburgh Pirates"); 
		MLBMapping.put("SD", "San Diego Padres");
		MLBMapping.put("SEA", "Seattle Mariners");
		MLBMapping.put("SF", "San Francisco Giants");
		MLBMapping.put("STL", "Saint Louis Cardinals");
		MLBMapping.put("TB", "Tampa Bay Rays");
		MLBMapping.put("TEN", "Tennessee Titans");
		MLBMapping.put("TEX", "Texas Rangers");
		MLBMapping.put("TOR", "Toronto Blue Jays");
		MLBMapping.put("WAS", "Washington Nationals");
	}

	/**
	 * Constructor
	 */
	public SportsInsightsJSONParser() {
		super();
		LOGGER.info("Entering SportsInsightsParser()");
		LOGGER.info("Exiting SportsInsightsParser()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SimpleDateFormat sdfmt1 = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
		try {
			String dateElement = "2/09";
			String timeElement = "10:00 pm";
			final Calendar now = Calendar.getInstance();
			dateElement += "/" + String.valueOf(now.get(Calendar.YEAR));
			String dtDate = dateElement + " " + timeElement;
			LOGGER.error("dtDate: " + dtDate);
			Date dDate = sdfmt1.parse(dtDate);
		} catch (ParseException pe) {
			LOGGER.error("Error parsing date", pe);
		}
	}

	/**
	 * 
	 * @param json
	 * @return
	 */
	public String getUserId(String json) {
		LOGGER.info("Entering getUserId()");
		String userId = null;

		try {
			if (json != null) {
				int index = json.indexOf("userId:");
				if (index != -1) {
					json = json.substring(index + "userId:".length());
					index = json.indexOf(",firstName");
					if (index != -1) {
						userId = json.substring(0, index);
						LOGGER.debug("userId: " + userId);
					}
				}
			}
		} catch (Throwable e) {
			LOGGER.error(e.getMessage(), e);
		}

		LOGGER.info("Exiting getUserId()");
		return userId;
	}

	/**
	 * 
	 * @param json
	 * @return
	 */
	public List<EventPackage> getAllEvents(String json) {
		LOGGER.info("Entering getAllEvents()");
		List<EventPackage> eventPackageList = null;

		try {
			if (json != null) {
				final JSONObject obj = new JSONObject(json);
				eventPackageList = processGames(obj.getJSONArray("Events"));
			}
		} catch (Throwable e) {
			LOGGER.error(e.getMessage(), e);
		}

		LOGGER.info("Exiting getAllEvents()");
		return eventPackageList;
	}

	/**
	 * 
	 * @param json
	 * @param ep
	 */
	public void parseMlbScore(String json, EventPackage ep) {
		LOGGER.info("Entering parseMlbScore()");

		try {
			if (json != null) {
				final JSONObject obj = new JSONObject(json);
				final JSONObject scoreModel = obj.getJSONObject("ScoreModel");
				final JSONArray eventScores = scoreModel.getJSONArray("EventScores");

				if (eventScores != null && eventScores.length() > 0) {
					boolean is1hdone = false;

					for (int x = 0; x < eventScores.length(); x++) {
						final JSONObject eventScore = (JSONObject)eventScores.get(x);
						final String ptime = eventScore.getString("PeriodTime");
						final String pshort = eventScore.getString("PeriodShort");

						if (ptime != null && ptime.equals("TOP") && pshort != null && pshort.equals("6")) {
							is1hdone = true;
						} else if (is1hdone && ptime != null && ptime.equals("BOT") && pshort != null && pshort.equals("5")) {
							ep.setFirstIsFinal(true);
							ep.getTeamone().setFirstIsFinal(true);
							ep.getTeamtwo().setFirstIsFinal(true);
							ep.getTeamone().setFirstScore(eventScore.getInt("VisitorScore"));
							ep.getTeamtwo().setFirstScore(eventScore.getInt("HomeScore"));
							is1hdone = false;
						}
					}
				}
			}
		} catch (Throwable e) {
			LOGGER.error(e.getMessage(), e);
		}

		LOGGER.info("Exiting parseMlbScore()");
	}

	/**
	 * 
	 * @param json
	 * @return
	 */
	public ClosingLine parseClosingLine(String json) {
		LOGGER.info("Entering parseClosingLine()");
		ClosingLine closingLine = null;

		try {
			if (json != null) {
				JSONArray closingLines = new JSONArray(json);
				if (closingLines != null && closingLines.length() > 0) {
					
					// "LineId":827845295,
					// "Line":-7.5,
					// "Money1":-104.0000,
					// "Money2":-112.0000,
					// "Money3":0.0000,
					// "HomeTeamFavored":true,
					// "CreatedDate":"2017-10-13T15:10:04.197",
					// "LinePercent1":43,
					// "LinePercent2":57,
					// "LinePercent3":0,
					// "LinePercentMoney1":0,
					// "LinePercentMoney2":0,
					// "Sportsbook":"Pinnacle",
					// "SignalType":0,
					// "IsSteamMove":false,
					// "IsRLMMove":false,
					// "LineType":1
					
					final JSONObject obj = (JSONObject)closingLines.get(0);
					closingLine = new ClosingLine();
					closingLine.setCreatedDate(obj.getString("CreatedDate"));
					closingLine.setHomeTeamFavored(obj.getBoolean("HomeTeamFavored"));
					closingLine.setLine(obj.getDouble("Line"));
					closingLine.setLinePercent1(obj.getInt("LinePercent1"));
					closingLine.setLinePercent2(obj.getInt("LinePercent2"));
					closingLine.setLinePercent3(obj.getInt("LinePercent3"));
					closingLine.setLinePercentMoney1(obj.getInt("LinePercentMoney1"));
					closingLine.setLinePercentMoney2(obj.getInt("LinePercentMoney2"));
					closingLine.setLineType(obj.getInt("LineType"));
					closingLine.setMoney1(obj.getDouble("Money1"));
					closingLine.setMoney2(obj.getDouble("Money2"));
					closingLine.setMoney3(obj.getDouble("Money3"));
					closingLine.setSignalType(obj.getInt("SignalType"));
					closingLine.setSportsBook(obj.getString("Sportsbook"));
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception processing events", e);
		}

		LOGGER.info("Exiting parseClosingLine()");
		return closingLine;
	}

	/**
	 * 
	 * @param divs
	 * @param eventType
	 * @return
	 * @throws ParseException
	 */
	private List<EventPackage> processGames(JSONArray events) throws ParseException {
		LOGGER.info("Entering processSport()");
		final List<EventPackage> eventList = new ArrayList<EventPackage>();

		// Loop through everything
		for (int x = 0;events != null && x < events.length(); x++) {
			final JSONObject event = events.getJSONObject(x);

			final TeamPackage team1 = new TeamPackage();
			final TeamPackage team2 = new TeamPackage();

			final EventPackage eventPackage = new EventPackage();
			eventPackage.setSporttype(event.getString("SportName"));

			// 1 - NOT Started
			// 2 - ??
			// 3 - Active
			// 4 - FINAL
			// int periodState = event.getInt("PeriodState");
			eventPackage.setSportsInsightsEventId(event.getInt("EventId"));
			eventPackage.setPeriodType(event.getString("PeriodTime"));
			eventPackage.setPeriodNumber(event.getString("PeriodShort"));
			eventPackage.setId(event.getInt("VisitorNSS"));

			team1.setTeam(event.getString("VisitorTeam"));
			team1.setEventid(Integer.toString(event.getInt("VisitorNSS")));
			team1.setId(event.getInt("VisitorNSS"));
			team1.setScore(event.getInt("VisitorScore"));
			String tPitcher = event.getString("VisitorPitcher");
			if (tPitcher != null && tPitcher.length() > 0) {
				int index = tPitcher.indexOf(" ");
				if (index != -1) {
					tPitcher = tPitcher.substring(index + 1);
					index = tPitcher.indexOf("(L)");
					if (index != -1) {
						tPitcher = tPitcher.substring(0, index);
						tPitcher = tPitcher.trim();
					}
				}
			}
			team1.setPitcher(tPitcher);
			team1.setTeamshort(event.getString("VisitorTeamShort"));

			team2.setTeam(event.getString("HomeTeam"));
			team2.setEventid(Integer.toString(event.getInt("HomeNSS")));
			team2.setId(event.getInt("HomeNSS"));
			team2.setScore(event.getInt("HomeScore"));
			tPitcher = event.getString("HomePitcher");
			if (tPitcher != null && tPitcher.length() > 0) {
				int index = tPitcher.indexOf(" ");
				if (index != -1) {
					tPitcher = tPitcher.substring(index + 1);
					index = tPitcher.indexOf("(L)");
					if (index != -1) {
						tPitcher = tPitcher.substring(0, index);
						tPitcher = tPitcher.trim();
					}
				}
			}
			team2.setPitcher(tPitcher);
			team2.setTeamshort(event.getString("HomeTeamShort"));

			try {
				Date dateEvent = null;
				final TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
				final Calendar gameDate = Calendar.getInstance(timeZone);
				SDF.setTimeZone(timeZone);

				final String eventDate = event.getString("EventDate");
				if (eventDate != null && eventDate.length() > 0) {
					dateEvent = SDF.parse(eventDate);
					gameDate.setTime(dateEvent);
				} else {
					dateEvent = gameDate.getTime();
				}

				String dateString = null;
				String timeString = null;

				int month = gameDate.get(Calendar.MONTH) + 1;
				int day = gameDate.get(Calendar.DAY_OF_MONTH);
				int year = gameDate.get(Calendar.YEAR);
				int hour = gameDate.get(Calendar.HOUR_OF_DAY);
				int minute = gameDate.get(Calendar.MINUTE);

				dateString = month + "/";
				if (day < 10) {
					dateString += "0" + day + "/";
				} else {
					dateString += day + "/";
				}
				dateString += year;

				String ampm = "AM";
				if (hour > 12) {
					hour = hour - 12;
					ampm = "PM";
				}
				if (hour < 10) {
					timeString = "0" + hour + ":";
				} else {
					timeString = hour + ":";
				}
				if (minute < 10) {
					timeString += "0" + minute + " " + ampm;
				} else {
					timeString += minute + " " + ampm;
				}

//				LOGGER.debug("Team: " + event.getString("VisitorTeam"));
//				LOGGER.debug("eventDate: " + eventDate);
//				LOGGER.debug("dateEvent: " + dateEvent);
//				LOGGER.debug("dateElement: " + dateString);
//				LOGGER.debug("timeElement: " + timeString);
				team1.setEventdatetime(dateEvent);
				team1.setDateofevent(dateString);
				team1.setTimeofevent(timeString);
				team2.setEventdatetime(dateEvent);
				team2.setDateofevent(dateString);
				team2.setTimeofevent(timeString);
				eventPackage.setTeamone(team1);
				eventPackage.setTeamtwo(team2);
				eventPackage.setDateofevent(dateString);
				eventPackage.setTimeofevent(timeString);
				eventPackage.setEventdatetime(dateEvent);
			} catch (ParseException pe) {
				LOGGER.error(pe.getMessage(), pe);
			} catch (Throwable t) {
				LOGGER.error(t.getMessage(), t);
			}

			eventList.add(eventPackage);
		}

		return eventList;		
	}

	/**
	 * 
	 * @param json
	 * @return
	 * @throws ParseException
	 */
	public List<SportsInsightsEvent> processGamesAll(String json) throws ParseException {
		LOGGER.info("Entering processGamesAll()");
		final List<SportsInsightsEvent> eventList = new ArrayList<SportsInsightsEvent>();
		JSONArray events = null;

		if (json != null) {
			final JSONObject obj = new JSONObject(json);
			events = obj.getJSONArray("Events");
		} else {
			return null;
		}

		// Loop through everything
		for (int x = 0; events != null && x < events.length(); x++) {
			final JSONObject event = events.getJSONObject(x);
			final SportsInsightsEvent sie = new SportsInsightsEvent();

			sie.EventId = event.getInt("EventId");
			sie.SportId = event.getInt("SportId");
			sie.HomeTeam = event.getString("HomeTeam");
			sie.VisitorTeam = event.getString("VisitorTeam");
			sie.HomeTeamShort = event.getString("HomeTeamShort");
			sie.VisitorTeamShort = event.getString("VisitorTeamShort");
			sie.TotalBets = event.getInt("TotalBets");
			sie.EventDate = event.getString("EventDate");
			sie.EventDateShort = event.getString("EventDateShort");
			sie.HomeNSS = event.getInt("HomeNSS");
			sie.VisitorNSS = event.getInt("VisitorNSS");
			sie.SportName = event.getString("SportName");
			sie.HomePitcher = event.getString("HomePitcher");
			sie.VisitorPitcher = event.getString("VisitorPitcher");
			sie.GameId = event.getInt("GameId");
			sie.VisitorScore = event.getInt("VisitorScore");
			sie.HomeScore = event.getInt("HomeScore");
			sie.PeriodState = event.getInt("PeriodState");
			sie.PeriodTime = event.getString("PeriodTime");
			sie.PeriodShort = event.getString("PeriodShort");
			final JSONObject visitorValues = event.getJSONObject("VisitorValues");
			final SportsInsightsValues vValues = new SportsInsightsValues();
			vValues.Overall = visitorValues.getInt("Overall");
			vValues.Contrarian = visitorValues.getInt("Contrarian");
			vValues.Steam = visitorValues.getInt("Steam");
			vValues.ReverseLine = visitorValues.getInt("ReverseLine");
			vValues.Official = visitorValues.getInt("Official");
			sie.VisitorValues = vValues;
			final JSONObject homeValues = event.getJSONObject("HomeValues");
			final SportsInsightsValues hValues = new SportsInsightsValues();
			hValues.Overall = homeValues.getInt("Overall");
			hValues.Contrarian = homeValues.getInt("Contrarian");
			hValues.Steam = homeValues.getInt("Steam");
			hValues.ReverseLine = homeValues.getInt("ReverseLine");
			hValues.Official = homeValues.getInt("Official");
			sie.HomeValues = hValues;
			sie.Value1 = event.getInt("Value1");
			sie.Value2 = event.getInt("Value2");
			sie.Group1 = event.getString("Group1");
			sie.Group2 = event.getString("Group2");
			sie.MyGame = event.getBoolean("MyGame");
			sie.NeutralSite = event.getBoolean("NeutralSite");
			sie.VisitorROTNumber = event.getString("VisitorROTNumber");
			sie.HomeROTNumber = event.getString("HomeROTNumber");
			sie.GameCode = event.getString("GameCode");
			sie.LeagueId = event.getInt("LeagueId");
			sie.HalftimeModifiedDate = event.getString("HalftimeModifiedDate");

			eventList.add(sie);
		}

		LOGGER.info("Exiting processGamesAll()");
		return eventList;
	}

	/**
	 * 
	 * @param json
	 * @param ep
	 * @return
	 */
	public EventPackage parseScores(String linetype, String json, EventPackage ep) {
		LOGGER.info("Entering parseScores()");

		try {
			if (json != null) {
				final JSONObject obj = new JSONObject(json);
				final JSONObject scoreModel = obj.getJSONObject("ScoreModel");
				if (scoreModel != null) {
					final JSONArray eventScores = scoreModel.getJSONArray("EventScores");
					for (int y = 0; y < eventScores.length(); y++) {
						final JSONObject eventScore = eventScores.getJSONObject(y);
						Integer VisitorScore = eventScore.getInt("VisitorScore");
						Integer HomeScore = eventScore.getInt("HomeScore");
						String PeriodDescription = eventScore.getString("PeriodDescription");
					
						if (!linetype.startsWith("mlb") || !linetype.startsWith("nhl")) {
							if (PeriodDescription != null && PeriodDescription.equals("FINAL")) {
								ep.setGameIsFinal(true);
								ep.setFirstIsFinal(true);
								ep.setSecondIsFinal(true);
								ep.setThirdIsFinal(true);
								ep.setLiveIsFinal(true);
								ep.getTeamone().setGameScore(VisitorScore);
								ep.getTeamtwo().setGameScore(HomeScore);
								ep.getTeamone().setGameIsFinal(true);
								ep.getTeamtwo().setGameIsFinal(true);
								ep.getTeamone().setFirstIsFinal(true);
								ep.getTeamtwo().setFirstIsFinal(true);
								ep.getTeamone().setSecondIsFinal(true);
								ep.getTeamtwo().setSecondIsFinal(true);
								ep.getTeamone().setLiveIsFinal(true);
								ep.getTeamtwo().setLiveIsFinal(true);
							} else if (PeriodDescription != null && PeriodDescription.equals("Half")) {
								ep.setFirstIsFinal(true);
								ep.getTeamone().setFirstScore(VisitorScore);
								ep.getTeamtwo().setFirstScore(HomeScore);
								ep.getTeamone().setFirstIsFinal(true);
								ep.getTeamtwo().setFirstIsFinal(true);
							}
						} else if (linetype.startsWith("mlb")) {
							if (PeriodDescription != null && PeriodDescription.equals("FINAL")) {
								ep.setGameIsFinal(true);
								ep.setFirstIsFinal(true);
								ep.setSecondIsFinal(true);
								ep.setThirdIsFinal(false);
								ep.setLiveIsFinal(true);
								ep.getTeamone().setGameScore(VisitorScore);
								ep.getTeamtwo().setGameScore(HomeScore);
								ep.getTeamone().setGameIsFinal(true);
								ep.getTeamtwo().setGameIsFinal(true);
								ep.getTeamone().setFirstIsFinal(true);
								ep.getTeamtwo().setFirstIsFinal(true);
								ep.getTeamone().setSecondIsFinal(true);
								ep.getTeamtwo().setSecondIsFinal(true);
								ep.getTeamone().setLiveIsFinal(true);
								ep.getTeamtwo().setLiveIsFinal(true);
							} else if (PeriodDescription != null && PeriodDescription.equals("BOT 5")) {
								ep.setFirstIsFinal(true);
								ep.getTeamone().setFirstIsFinal(true);
								ep.getTeamtwo().setFirstIsFinal(true);
								ep.getTeamone().setFirstScore(VisitorScore);
								ep.getTeamtwo().setFirstScore(HomeScore);							
							}
						} else if (linetype.startsWith("nhl")) {
							if (PeriodDescription != null && PeriodDescription.equals("FINAL")) {
								ep.setGameIsFinal(true);
								ep.setFirstIsFinal(true);
								ep.setSecondIsFinal(true);
								ep.setThirdIsFinal(true);
								ep.setLiveIsFinal(true);
								ep.getTeamone().setGameScore(VisitorScore);
								ep.getTeamtwo().setGameScore(HomeScore);
								ep.getTeamone().setGameIsFinal(true);
								ep.getTeamtwo().setGameIsFinal(true);
								ep.getTeamone().setFirstIsFinal(true);
								ep.getTeamtwo().setFirstIsFinal(true);
								ep.getTeamone().setSecondIsFinal(true);
								ep.getTeamtwo().setSecondIsFinal(true);
								ep.getTeamone().setThirdIsFinal(true);
								ep.getTeamtwo().setThirdIsFinal(true);
								ep.getTeamone().setLiveIsFinal(true);
								ep.getTeamtwo().setLiveIsFinal(true);
							} else if (PeriodDescription != null && PeriodDescription.equals("1st Half")) {
								// TODO: Need to figure out NHL
							}
						}
					}

					// Now calculate 2nd halfs/ 2,3 periods
					if (!linetype.startsWith("nhl")) {
						if (ep.getGameIsFinal()) {
							Integer fscore1 = ep.getTeamone().getFirstScore();
							Integer gscore1 = ep.getTeamone().getGameScore();
							Integer fscore2 = ep.getTeamtwo().getFirstScore();
							Integer gscore2 = ep.getTeamtwo().getGameScore();

							if (fscore1 != null && gscore1 != null && fscore2 != null && gscore2 != null) {
								int v1 = ep.getTeamone().getFirstScore().intValue();
								int v2 = ep.getTeamone().getGameScore().intValue();
								int h1 = ep.getTeamtwo().getFirstScore().intValue();
								int h2 = ep.getTeamtwo().getGameScore().intValue();
								ep.getTeamone().setSecondScore(v2 - v1);
								ep.getTeamtwo().setSecondScore(h2 - h1);
							}
						}
					} else if (linetype.startsWith("nhl")) {
						// TODO
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception processing events", e);
		}

		LOGGER.info("Exiting parseScores()");
		return ep;
	}

	@Override
	public Map<String, String> parseIndex(String xhtml) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseLogin(java.lang.String)
	 */
	@Override
	public Map<String, String> parseLogin(String xhtml) throws BatchException {
		final Map<String, String> loginInfo = new HashMap<String, String>();

		if (xhtml != null) {
			final JSONObject obj = new JSONObject(xhtml);
			String token = obj.getString("token");
			loginInfo.put("token", token);
		}

		return loginInfo;
	}

	@Override
	public Map<String, String> parseMenu(String xhtml, String[] type, String[] sport) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> parseGames(String xhtml, String type, Map<String, String> inputFields) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> parseEventSelection(String xhtml, SiteTeamPackage siteTeamPackage, String type)
			throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String parseTicketNumber(String xhtml) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected <T> List<T> getGameData(Elements elements) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}	
}