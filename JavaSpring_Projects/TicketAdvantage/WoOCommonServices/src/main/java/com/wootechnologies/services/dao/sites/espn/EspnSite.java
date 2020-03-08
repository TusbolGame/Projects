/**
 * 
 */
package com.wootechnologies.services.dao.sites.espn;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.wootechnologies.errorhandling.BatchException;
import com.wootechnologies.model.AccountEvent;
import com.wootechnologies.model.BaseRecordEvent;
import com.wootechnologies.model.EventPackage;
import com.wootechnologies.model.EventsPackage;
import com.wootechnologies.model.MlRecordEvent;
import com.wootechnologies.model.SpreadRecordEvent;
import com.wootechnologies.model.TeamPackage;
import com.wootechnologies.model.TotalRecordEvent;
import com.wootechnologies.services.dao.sites.SiteProcessor;
import com.wootechnologies.services.dao.sites.SiteTeamPackage;
import com.wootechnologies.services.dao.sites.SiteTransaction;


/**
 * @author jmiller
 *
 */
public class EspnSite extends SiteProcessor {
	private final static Logger LOGGER = Logger.getLogger(EspnSite.class);
	private final EspnParser esp = new EspnParser();

	/**
	 * 
	 */
	public EspnSite() {
		super("EspnSite", "http://www.espn.com", null, null, false, false);
		LOGGER.info("Entering EspnSite()");

		try {
			setTimezone("ET");
			getHttpClientWrapper().setupHttpClient("None");
			setProcessTransaction(false);
		} catch (Throwable t) {
			LOGGER.warn(t.getMessage(), t);
		}

		LOGGER.info("Exiting EspnSite()");
	}

	/**
	 * 
	 * @param host
	 * @param username
	 * @param password
	 */
	public EspnSite(String host, String username, String password) {
		super("EspnSite", "http://www.espn.com", username, password, false, false);
		LOGGER.info("Entering EspnSite()");

		try {
			setTimezone("ET");
			getHttpClientWrapper().setupHttpClient("None");
			setProcessTransaction(false);
		} catch (Throwable t) {
			LOGGER.warn(t.getMessage(), t);
		}

		LOGGER.info("Exiting EspnSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final EspnSite espnSite = new EspnSite();
//			String html = espnSite.getSiteForEspn("http://www.espn.com/mens-college-basketball/matchup?gameId=401083106");
//			LOGGER.error("html: " + html);

//			final List<EspnFootballPowerIndex> powerIndexes = espnSite.getNcaafPowerIndexes();
//			for (EspnFootballPowerIndex efpi : powerIndexes) {
//				LOGGER.error("EspnFootballPowerIndex: " + efpi);
//			}

			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.MONTH, 10);
			cal.set(Calendar.DAY_OF_MONTH, 6);
			cal.set(Calendar.YEAR, 2018);
			Date startDate = cal.getTime();
			cal.set(Calendar.MONTH, 10);
			cal.set(Calendar.DAY_OF_MONTH, 6);
			cal.set(Calendar.YEAR, 2018);
			Date endDate = cal.getTime();

//			final List<EspnCollegeBasketballGameData> ncaabGames = espnSite.getNcaabTeamData(startDate, endDate);
//			for (EspnCollegeBasketballGameData game : ncaabGames) {
//				LOGGER.error("game: " + game);
//			}

//			final EspnFootballGameData egame = espnSite.getNcaafGameTeamData("http://cdn.espn.com/core/college-football/game/_/gameId/401012880?xhr=1&render=true&device=desktop&country=us&lang=en&region=us&site=espn&edition-host=espn.com&site-type=full");
//			espnSite.getNcaafGameMatchupTeamData("http://cdn.espn.com/core/college-football/matchup?gameId=401012880&xhr=1&render=true&device=desktop&country=us&lang=en&region=us&site=espn&edition-host=espn.com&site-type=full", egame);
//			LOGGER.error("EspnFootballGameData: " + egame);
			
			
			final List<EspnCollegeFootballGameData> games = espnSite.getNcaafTeamData(2018, 1, 1, false);
			LOGGER.error("games: " + games);

//			EspnFootballGameData efgd = espnSite.getNcaafGameTeamData();
//			EspnBasketballGameData efgd = espnSite.getNcaabGameTeamData();
//			final EspnFootballGame espnGame = espnSite.getNcaafGame("http://www.espn.com/college-football/playbyplay?gameId=400934493");
//			LOGGER.error("espnGame: " + espnGame);
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}
	}

	/**
	 * 
	 */
	public void getWnbaBasketball() {
		final EspnSite eSite = new EspnSite(null, null, null);
		final List<EspnBasketballGame> games = new ArrayList<EspnBasketballGame>();
		Integer year = 2018;
		Integer startMonth = 5;
		Integer endMonth = 6;
		Integer startDay = 18;
		Integer endDay = 11;

		for (int x = startMonth; x <= endMonth; x++) {
			for (int y = 1; y <= 31; y++) {
				if (x == startMonth && y >= startDay) {
					String month = Integer.toString(x);
					if (x < 10) {
						month = "0" + x;
					}
					String day = Integer.toString(y);
					if (y < 10) {
						day = "0" + y;
					}
	
					final String theDate = year + month + day;
					final List<EspnBasketballGame> gamesToo = eSite.getWnbaSeasonDataObjects(theDate);
 					for (EspnBasketballGame game : gamesToo) {
 						games.add(game);
 					}
				} else if (x == endMonth && y <= endDay) {
					String month = Integer.toString(x);
					if (x < 10) {
						month = "0" + x;
					}
					String day = Integer.toString(y);
					if (y < 10) {
						day = "0" + y;
					}

					final String theDate = year + month + day;
 					final List<EspnBasketballGame> gamesToo = eSite.getWnbaSeasonDataObjects(theDate);
 					for (EspnBasketballGame game : gamesToo) {
 						games.add(game);
 					}
				}
			}
		}

		// WNBA store the data
		// eSite.storeBasketballData(games);
	}

	/**
	 * 
	 * @param year
	 * @param startWeek
	 * @param endWeek
	 * @param includebowls
	 * @return
	 */
	public List<EspnCollegeFootballGameData> getNcaafTeamData(Integer year, Integer startWeek, Integer endWeek, Boolean includebowls) {
		final List<EspnCollegeFootballGameData> teamData = new ArrayList<EspnCollegeFootballGameData>();

		try {
			for (int week = startWeek;  week <= endWeek; week++) {
				LOGGER.error("Week: " + week);
				
				// Get 1-A (FBS) first
				// https://www.espn.com/college-football/scoreboard/_/group/80/year/2019/seasontype/2/week/1
				String url = "http://www.espn.com/college-football/scoreboard/_/group/80/year/" + year + "/seasontype/2/week/" + week;
				getNcaafWeekGames(week, url, teamData);

				// Get 1-AA (FCS) second
				url = "http://www.espn.com/college-football/scoreboard/_/group/81/year/" + year + "/seasontype/2/week/" + week;
				getNcaafWeekGames(week, url, teamData);
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		return teamData;
	}

	/**
	 * 
	 * @return
	 */
	public List<String> getNcaafFCSTeamData() {
		final List<String> fcsTeams = new ArrayList<String>();

		try {
			Integer year = 0;
			for (int y = 0; y < 2; y++) {
				if (y == 0) {
					year = 2017;
				} else {
					year = 2018;
				}
				for (int x = 1; x < 15; x++) {
					// Get 1-AA (FCS) second
					String url = "http://www.espn.com/college-football/scoreboard/_/group/81/year/" + year + "/seasontype/2/week/" + x;
					final List<EspnCollegeFootballGameData> teamData = new ArrayList<EspnCollegeFootballGameData>();
					getNcaafWeekGames(x, url, teamData);
					for (EspnCollegeFootballGameData ef : teamData) {
						if (!ef.getAwayisfbs()) {
							boolean didnotfind = false;
							for (String nn : fcsTeams) {
								if (nn.equals(ef.getAwaycollegename())) {
									didnotfind = true;
								}
							}
							if (!didnotfind) {
								fcsTeams.add(ef.getAwaycollegename());
							}
						}
						if (!ef.getHomeisfbs()) {
							boolean didnotfind = false;
							for (String nn : fcsTeams) {
								if (nn.equals(ef.getHomecollegename())) {
									didnotfind = true;
								}
							}
							if (!didnotfind) {
								fcsTeams.add(ef.getHomecollegename());
							}
						}
					}
				}
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		return fcsTeams;
	}

	/**
	 * 
	 * @param week
	 * @param url
	 * @param teamData
	 * @throws BatchException
	 */
	private void getNcaafWeekGames(Integer week, String url, List<EspnCollegeFootballGameData> teamData) throws BatchException {
		String xhtml = super.getSiteForEspn(url);
		LOGGER.debug("xhtml: " + xhtml);
		final List<String> games = esp.parseNccafGamesForDay(xhtml);

		if (games != null && games.size() > 0) {
			for (String gameId : games) {
				try {
					LOGGER.debug("gameId: " + gameId);
					// http://secure.espn.com/core/college-football/game/_/gameId/401013357?xhr=1&render=true&device=desktop&country=us&lang=en&region=us&site=espn&edition-host=espn.com&site-type=full
					// https://secure.espn.com/core/college-football/matchup?gameId=401013357&xhr=1&render=true&device=desktop&country=us&lang=en&region=us&site=espn&edition-host=espn.com&site-type=full
					final EspnCollegeFootballGameData gameData = getNcaafGameTeamData("http://secure.espn.com/core/college-football/game/_/gameId/" + gameId + "?xhr=1&render=true&device=desktop&country=us&lang=en&region=us&site=espn&edition-host=espn.com&site-type=full");
					getNcaafGameMatchupTeamData("http://secure.espn.com/core/college-football/matchup?gameId=" + gameId + "&xhr=1&render=true&device=desktop&country=us&lang=en&region=us&site=espn&edition-host=espn.com&site-type=full", gameData);
					LOGGER.debug("gameData: " + gameData);

					if (gameData != null) {
						gameData.setEspngameid(Integer.parseInt(gameId));

						// Check if we have early games for week 1
						if (week == 1) {
							final Integer day = gameData.getDay();
							final Integer month = gameData.getMonth();
							LOGGER.debug("day: " + day);
							LOGGER.debug("month: " + month);
							LOGGER.debug("week: " + week);
							if (day < 30 && month == 8) {
								gameData.setWeek(0);
							} else {
								gameData.setWeek(week);		
							}
						} else {
							gameData.setWeek(week);
						}

						if ((gameData.getAwaycollegename() != null && gameData.getAwaycollegename().length() > 0) ||
							(gameData.getHomecollegename() != null && gameData.getHomecollegename().length() > 0)) {
							teamData.add(gameData);
							LOGGER.debug("EspnFootballGameData: " + gameData);
						}
					}
				} catch (Throwable t) {
					LOGGER.error(t.getMessage(), t);
				}
			}
		}
	}

	/**
	 * 
	 * @param year
	 * @param startWeek
	 * @param endWeek
	 * @param includebowls
	 * @return
	 */
	public List<EspnFootballGameData> getNcaafTeamDataForBets(Integer year, Integer startWeek, Integer endWeek, Boolean includebowls) {
		final List<EspnFootballGameData> teamData = new ArrayList<EspnFootballGameData>();

		try {
			for (int week = startWeek;  week <= endWeek; week++) {
				LOGGER.error("Week: " + week);
				final String url = "http://www.espn.com/college-football/scoreboard/_/group/80/year/" + year + "/seasontype/2/week/" + week;
				LOGGER.debug("url: " + url);

				String xhtml = super.getSite(url);
				final List<String> games = esp.parseNccafGamesForDay(xhtml);

				if (games != null && games.size() > 0) {
					for (String href : games) {
						try {
							LOGGER.debug("url: " + href);
							xhtml = super.getSite("http://www.espn.com" + href);
							
							Integer gameId = null;
							int index = href.indexOf("gameId=");
							if (index != -1) {
								final String gId = href.substring(index + "gameId=".length());
								LOGGER.debug("gId: " + gId);
								gameId = Integer.parseInt(gId);
							}
							final EspnCollegeFootballGameData gameData = getNcaafGameTeamDataForBets("http://www.espn.com" + href);
							if (gameData != null) {
								gameData.setEspngameid(gameId);
								gameData.setWeek(week);
								LOGGER.debug("EspnFootballGameData: " + gameData);
								teamData.add(gameData);
							}
						} catch (Throwable t) {
							LOGGER.error(t.getMessage(), t);
						}
					}
				}
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		return teamData;
	}

	/**
	 * 
	 * @return
	 */
	public List<EspnFootballPowerIndex> getNcaafPowerIndexes() {
		LOGGER.info("Entering getNcaafPowerIndexes()");
		List<EspnFootballPowerIndex> powerIndexes = null;

		try {
			final String xhtml = super.getSite("http://www.espn.com/college-football/statistics/teamratings");
			powerIndexes = esp.parseNcaafPowerIndexes(xhtml);
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		LOGGER.info("Entering getNcaafPowerIndexes()");
		return powerIndexes;
	}

	/**
	 * 
	 * @return
	 */
	public List<EspnFootballTeamEfficiencies> getNcaafTeamEfficiencies() {
		LOGGER.info("Entering getNcaafTeamEfficiencies()");
		List<EspnFootballTeamEfficiencies> teamEfficiencies = null;

		try {
			final String xhtml = super.getSite("http://www.espn.com/college-football/statistics/teamratings/_/tab/efficiency");
			teamEfficiencies = esp.parseNcaafTeamEfficiencies(xhtml);
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		LOGGER.info("Entering getNcaafTeamEfficiencies()");
		return teamEfficiencies;
	}

	/**
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<EspnCollegeBasketballGameData> getNcaabTeamData(Date startDate, Date endDate) {
		List<EspnCollegeBasketballGameData> teamData = new ArrayList<EspnCollegeBasketballGameData>();

		try {
			final LocalDate start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			final LocalDate end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			int week = 1;

			for (LocalDate date = start; date.isBefore(end) || date.isEqual(end); date = date.plusDays(1)) {
				String monthString = "";
				String dayString = "";
				int year = date.getYear();
				int month = date.getMonthValue();
				int day = date.getDayOfMonth();
				if (month < 10) {
					monthString = "0" + month;
				} else {
					monthString = Integer.toString(month);
				}
				if (day < 10) {
					dayString = "0" + day;
				} else {
					dayString = Integer.toString(day);
				}

				final String url = "http://www.espn.com/mens-college-basketball/scoreboard/_/group/50/date/" + year + monthString + dayString;
				LOGGER.error("url: " + url);

				String xhtml = getSiteForEspn(url);
				LOGGER.debug("xhtml: " + xhtml);
				final List<String> games = esp.parseNccabGamesForDay(xhtml);

				if (games != null && games.size() > 0) {
					LOGGER.error("games.size(): " + games.size());

					for (String href : games) {
						try {
							href = href.replace("game?gameId", "matchup?gameId");
							LOGGER.debug("href: " + href);

							if (href != null && !href.contains("\",\"video\":{\"gameId") && !href.contains("\",\"uid\":\"s:")) {
								xhtml = super.getSiteForEspn("http://www.espn.com" + href);
								
								Integer gameId = null;
								int index = href.indexOf("gameId=");
								if (index != -1) {
									final String gId = href.substring(index + "gameId=".length());
									LOGGER.debug("gId: " + gId);
									gameId = Integer.parseInt(gId);
								}
								final EspnCollegeBasketballGameData gameData = getNcaabGameTeamData("http://www.espn.com" + href);
								if (gameData != null) {
									gameData.setEspngameid(gameId);
									gameData.setWeek(week);
									LOGGER.debug("EspnBasketballGameData: " + gameData);

									if (year == gameData.getYear().intValue()) {
										teamData.add(gameData);
									}
								}
							}
						} catch (BatchException be) {
							if (be.getErrormessage() != null && (be.getErrormessage().equals("Network Exception") || 
									be.getErrormessage().equals("502 Status Code BAD GATEWAY"))) {
								teamData = getNcaabTeamData(startDate, endDate);
							} else {
								throw be;
							}
						} catch (Throwable t) {
							LOGGER.error(t.getMessage(), t);
						}
					}
				}

				int dayOfWeek = date.getDayOfWeek().getValue();
				if (dayOfWeek == 1) {
					week++;
				}
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		return teamData;
	}

	/**
	 * 
	 * @param url
	 * @return
	 */
	public EspnCollegeFootballGameData getNcaafGameTeamData(String url) {
		LOGGER.info("Entering getNcaafGameTeamData()");
		EspnCollegeFootballGameData espnGame = null;

		try {
			List<NameValuePair> headerValuePairs = new ArrayList<NameValuePair>(3);
			headerValuePairs.add(new BasicNameValuePair("Host", "cdn.espn.com"));
			headerValuePairs.add(new BasicNameValuePair("If-None-Match", "W/\"86b7305088e0022196f58874f7f0f053c51a4ee2\""));
			final String xhtml = super.getJSONSite(url, headerValuePairs);
			LOGGER.debug("xhtml: " + xhtml);

			espnGame = esp.parseNcaafGameTeamData(xhtml);
			LOGGER.debug("EspnFootballGameData: " + espnGame);
		} catch (Throwable t) {
			LOGGER.debug(t.getMessage(), t);
		}

		LOGGER.info("Entering getNcaafGameTeamData()");
		return espnGame;
	}

	/**
	 * 
	 * @param url
	 * @return
	 */
	public void getNcaafGameMatchupTeamData(String url, EspnCollegeFootballGameData espnGame) {
		LOGGER.info("Entering getNcaafGameMatchupTeamData()");

		try {
			final List<NameValuePair> headerValuePairs = new ArrayList<NameValuePair>(3);
			headerValuePairs.add(new BasicNameValuePair("Host", "cdn.espn.com"));
			headerValuePairs.add(new BasicNameValuePair("Origin", "http://www.espn.com"));
			headerValuePairs.add(new BasicNameValuePair("Refer", "http://www.espn.com/college-football/scoreboard/_/group/80/year/2018/seasontype/2/week/1"));			
			headerValuePairs.add(new BasicNameValuePair("If-None-Match", "W/\"63c400f81c204702f143806b94225e64d686778a\""));			
			final String xhtml = super.getJSONSite(url, headerValuePairs);
			LOGGER.debug("xhtml: " + xhtml);

			esp.parseNcaafGameMatchupTeamData(xhtml, espnGame);
			LOGGER.debug("EspnFootballGameData: " + espnGame);
		} catch (Throwable t) {
			LOGGER.debug(t.getMessage(), t);
		}

		LOGGER.info("Entering getNcaafGameMatchupTeamData()");
	}

	/**
	 * 
	 * @param url
	 * @return
	 */
	public EspnCollegeFootballGameData getNcaafGameTeamDataForBets(String url) {
		LOGGER.info("Entering getNcaafGameTeamDataForBets()");
		EspnCollegeFootballGameData espnGame = null;

		try {
			LOGGER.debug("url: " + url);
			final String xhtml = super.getSite(url);
			LOGGER.debug("xhtml: " + xhtml);

			espnGame = esp.parseNcaafTeamDataForBets(xhtml);
			LOGGER.debug("EspnCollegeFootballGameData: " + espnGame);
		} catch (Throwable t) {
			LOGGER.debug(t.getMessage(), t);
		}

		LOGGER.info("Entering getNcaafGameTeamDataForBets()");
		return espnGame;
	}

	/**
	 * 
	 * @param url
	 * @return
	 */
	public EspnCollegeBasketballGameData getNcaabGameTeamData(String url) {
		LOGGER.info("Entering getNcaabGameTeamData()");
		EspnCollegeBasketballGameData espnGame = null;

		try {
			url = url.trim();
			LOGGER.debug("url: " + url);
			final String xhtml = super.getSiteForEspn(url);
			LOGGER.debug("xhtml: " + xhtml);

			espnGame = esp.parseNcaabTeamData(xhtml);
			LOGGER.debug("EspnBasketballGameData: " + espnGame);
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		LOGGER.info("Entering getNcaabGameTeamData()");
		return espnGame;
	}

	/**
	 * 
	 * @param year
	 * @param week
	 * @return
	 */
	public List<EspnFootballGame> getNcaafSeasonDataObjects(String year, String week) {
		LOGGER.info("Entering getNcaafSeasonDataObjects()");
		List<EspnFootballGame> espnGames = new ArrayList<EspnFootballGame>();

		try {
			String xhtml = super.getSite("http://www.espn.com/college-football/scoreboard/_/year/" + year + "/seasontype/2/week/" + week);	
			LOGGER.debug("xhtml: " + xhtml);

			if (xhtml != null && xhtml.length() > 0) {
				final List<String> urls = esp.parseFootballSchedulePlayByPlay(xhtml);
				if (urls.size() > 0) {
					for (String url : urls) {
						final EspnFootballGame espnGame = getNcaafGame(url);
						espnGames.add(espnGame);
					}
				}
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		LOGGER.info("Entering getNcaafSeasonDataObjects()");
		return espnGames;
	}

	/**
	 * 
	 * @param url
	 * @return
	 * @throws BatchException
	 */
	private EspnFootballGame getNcaafGame(String url) throws BatchException {
		LOGGER.info("Entering getNcaafGame()");
		final EspnFootballGame espnGame = new EspnFootballGame();

		String xhtml = super.getSite(url);
		LOGGER.debug("xhtml: " + xhtml);

		Integer gameId = null;
		if (url != null && url.length() > 0) {
			int index = url.indexOf("gameId=");
			if (index != -1) {
				gameId = Integer.parseInt(url.substring(index + 7));
			}
		}

		final List<EspnFootballDrive> pbps = esp.parseNcaafDrives("", xhtml);
		if (pbps != null && !pbps.isEmpty()) {
			espnGame.setGameid(gameId);

			for (EspnFootballDrive pbp : pbps) {
				LOGGER.debug("EspnFootballDrive: " + pbp);
				espnGame.addDrive(pbp);
			}
		}

		LOGGER.info("Exiting getNcaafGame()");
		return espnGame;
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public List<EspnBasketballGame> getWnbaSeasonDataObjects(String date) {
		LOGGER.info("Entering getWnbaSeasonDataObjects()");
		List<EspnBasketballGame> espnGames = new ArrayList<EspnBasketballGame>();

		try {
			String xhtml = super.getSite("http://www.espn.com/wnba/scoreboard/_/date/" + date);	
			LOGGER.debug("xhtml: " + xhtml);

			if (xhtml != null && xhtml.length() > 0) {
				final List<String> urls = esp.parseBasketballSchedulePlayByPlay(xhtml);
				if (urls.size() > 0) {
					for (String url : urls) {
						xhtml = super.getSite(url);
						LOGGER.debug("xhtml: " + xhtml);
						
						if (url != null && url.length() > 0) {
							int index = url.indexOf("gameId=");
							if (index != -1) {
								final Integer gameId = Integer.parseInt(url.substring(index + 7));
								final List<EspnBasketballPlayByPlay> pbps = esp.parseWnbaPlayByPlay(gameId, date, xhtml);
								if (pbps != null && !pbps.isEmpty()) {
									final EspnBasketballGame espnGame = new EspnBasketballGame();
									espnGame.setGameid(gameId);
									espnGame.setGamedate(date);
									espnGame.setAwayteam(pbps.get(0).getAwayteam());
									espnGame.setHometeam(pbps.get(0).getHometeam());

									for (EspnBasketballPlayByPlay pbp : pbps) {
										LOGGER.debug("EspnPlayByPlay: " + pbp);
										espnGame.addPlay(pbp);
									}

									espnGames.add(espnGame);
								}
							}
						}
					}
				}
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		LOGGER.info("Entering getWnbaSeasonDataObjects()");
		return espnGames;
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public String getWnbaSeasonData(String date) {
		LOGGER.info("Entering getWnbaSeasonData()");
		String json = null;

		try {
			String xhtml = super.getSite("http://www.espn.com/wnba/scoreboard/_/date/" + date);	
			LOGGER.debug("xhtml: " + xhtml);

			if (xhtml != null && xhtml.length() > 0) {
				final List<String> urls = esp.parseBasketballSchedulePlayByPlay(xhtml);
				if (urls.size() > 0) {
					for (String url : urls) {
						xhtml = super.getSite(url);
						LOGGER.debug("xhtml: " + xhtml);
						
						if (url != null && url.length() > 0) {
							int index = url.indexOf("gameId=");
							if (index != -1) {
								final Integer gameId = Integer.parseInt(url.substring(index + 7));
								final List<EspnBasketballPlayByPlay> pbps = esp.parseWnbaPlayByPlay(gameId, date, xhtml);
								if (pbps != null && !pbps.isEmpty()) {
									json = "{game:{" +
											"\"gameid\":" + gameId + "," +
											"\"gamedate\":\"" + date + "\"," +
											"\"awayteam\":\"" + pbps.get(0).getAwayteam() + "\"," +
											"\"hometeam\":\"" + pbps.get(0).getHometeam() + "\"," +
											"\"plays\":[";

									int count = 0;
									for (EspnBasketballPlayByPlay pbp : pbps) {
										LOGGER.debug("EspnPlayByPlay: " + pbp);
										if (count == 0) {
											json += ("{" + pbp.toJSON() + "}");
										} else {
											json += (",{" + pbp.toJSON() + "}");
										}
										count++;
									}

									json += "],";
									json += "},";
									json += "}";
								}
							}
						}
					}
				}
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		LOGGER.info("Entering getWnbaSeasonData()");
		return json;
	}

	/**
	 * 
	 * @return
	 */
	public List<EspnBasketBallTeamData> getNcaamBasketballTeams() {
		List<EspnBasketBallTeamData> dc = null;

		try {
			// http://www.espn.com/mens-college-basketball/teams
			String xhtml = super.getSite("http://www.espn.com/mens-college-basketball/teams");
			LOGGER.debug("xhtml: " + xhtml);

			dc = esp.parseBasketbBallTeams(xhtml);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dc;
	}

	/**
	 * 
	 * @return
	 */
	public List<EspnBasketBallRosterData> getNcaamBasketballRoster(String url) {
		List<EspnBasketBallRosterData> dc = null;

		try {
			// http://www.espn.com/mens-college-basketball/teams
			String xhtml = super.getSite("http://www.espn.com/" + url);
			LOGGER.debug("xhtml: " + xhtml);

			dc = esp.parseBasketbBallTeamRoster(xhtml);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dc;
	}

	/**
	 * 
	 * @return
	 */
	public List<EspnData> getStrengthOfSchedule(String url) {
		List<EspnData> dc = null;

		try {
			String xhtml = super.getSite(url);
			LOGGER.debug("xhtml: " + xhtml);

			dc = esp.parseStrengthOfSchedule(xhtml);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dc;
	}

	/**
	 * 
	 * @param proxy
	 */
	public void setProxy(String proxy) {
		LOGGER.info("Entering setProxy()");

		try {
			this.httpClientWrapper.setupHttpClient(proxy);
		} catch (BatchException be) {
			LOGGER.error(be.getMessage(), be);
		}

		LOGGER.info("Exiting setProxy()");
	}

	/**
	 * 
	 * @param spread
	 * @param juice
	 * @param aspTeam
	 * @return
	 * @throws BatchException
	 */
	protected SiteTransaction determineNegativeSpread(SpreadRecordEvent spreadRecordEvent, float spread, float juice, TeamPackage teamPackage) throws BatchException {
		return null;
	}
	
	/**
	 * 
	 * @param spread
	 * @param juice
	 * @param aspTeam
	 * @return
	 * @throws BatchException
	 */
	protected SiteTransaction determinePositiveSpread(SpreadRecordEvent spreadRecordEvent, float spread, float juice, TeamPackage aspTeam) throws BatchException {
		return null;
	}

	/**
	 * 
	 * @param spread
	 * @param juice
	 * @param aspTeam
	 * @return
	 * @throws BatchException
	 */
	protected SiteTransaction determineEqualSpread(SpreadRecordEvent spreadRecordEvent, float spread, float juice, TeamPackage aspTeam) throws BatchException {
		return null;
	}

	/**
	 * 
	 * @param type
	 * @return
	 * @throws BatchException
	 */
	protected EventsPackage retrievePackage(String type) throws BatchException {
		return null;
	}
	
	/**
	 * 
	 * @param spreadType
	 * @param eventPackage
	 * @return
	 * @throws BatchException
	 */
	protected SiteTeamPackage setupTeam(int spreadType, EventPackage eventPackage) throws BatchException {
		return null;
	}

	@Override
	public SiteTransaction createSpreadTransaction(SiteTeamPackage siteTeamPackage,
			SpreadRecordEvent spreadRecordEvent, int arrayNum, float juice, AccountEvent accountEvent) throws BatchException {
		return null;
	}

	@Override
	public SiteTransaction createTotalTransaction(SiteTeamPackage siteTeamPackage, TotalRecordEvent totalRecordEvent,
			int arrayNum, float juice, AccountEvent accountEvent) throws BatchException {
		return null;
	}

	@Override
	public SiteTransaction createMoneyLineTransaction(SiteTeamPackage siteTeamPackage, MlRecordEvent mlRecordEvent,
			int arrayNum, float juice, AccountEvent accountEvent) throws BatchException {
		return null;
	}

	@Override
	protected SiteTransaction createSiteTransaction() {
		return null;
	}

	@Override
	protected String selectSport(String type) throws BatchException {
		return null;
	}

	@Override
	protected void parseEventSelection(String xhtml, SiteTransaction siteTransaction, EventPackage eventPackage,
			BaseRecordEvent event, AccountEvent ae) throws BatchException {
	}

	@Override
	protected String selectEvent(SiteTransaction siteTransaction, EventPackage eventPackage, BaseRecordEvent event,
			AccountEvent ae) throws BatchException {
		return null;
	}

	@Override
	protected String completeTransaction(SiteTransaction siteTransaction, EventPackage eventPackage,
			BaseRecordEvent event, AccountEvent ae) throws BatchException {
		return null;
	}

	@Override
	protected String parseTicketTransaction(String xhtml) throws BatchException {
		return null;
	}

	@Override
	public String loginToSite(String username, String password) throws BatchException {
		return null;
	}
}