/**
 * 
 */
package com.wootechnologies.dataminer.db.ncaaf;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.wootechnologies.dataminer.db.DataMinerDB;
import com.wootechnologies.dataminer.model.Efficiencies;
import com.wootechnologies.dataminer.model.FloatData;
import com.wootechnologies.dataminer.model.XandYObject;
import com.wootechnologies.errorhandling.BatchException;
import com.wootechnologies.services.dao.sites.espn.EspnCollegeFootballGameData;

/**
 * @author jmiller
 *
 */
public class NcaafDataMinerDB extends DataMinerDB {
	private static final Logger LOGGER = Logger.getLogger(NcaafDataMinerDB.class);

	/**
	 * 
	 */
	public NcaafDataMinerDB() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * 
	 * @param gameData
	 * @throws SQLException
	 */
	public void persistEspnNcaafGameData(EspnCollegeFootballGameData gameData) throws SQLException {
		LOGGER.info("Entering persistEspnNcaafGameData()");
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {
			stmt = conn.prepareStatement("SELECT * FROM espnfootballgame where week = ? AND year = ? AND awaycollegename = ? AND homecollegename = ?");
			stmt.setLong(1, gameData.getWeek());
			stmt.setLong(2, gameData.getYear());
			stmt.setString(3, gameData.getAwaycollegename());
			stmt.setString(4, gameData.getHomecollegename());
			resultSet = stmt.executeQuery();

			if (resultSet.next()) {
				LOGGER.error("Duplicate game being recorded: " + gameData);
				resultSet.close();
				stmt.close();
				resultSet = null;
				stmt = null;
				return;
			} else {
				resultSet.close();
				stmt.close();				
			}

			final StringBuffer sb = new StringBuffer(2000);
			sb.append("INSERT into espnfootballgame (");
			sb.append("espngameid, ");
			sb.append("week, ");
			sb.append("hour , ");
			sb.append("minute, ");
			sb.append("ampm, ");
			sb.append("timezone, ");
			sb.append("month, ");
			sb.append("day, ");
			sb.append("year, ");
			sb.append("gamedate, ");
			sb.append("tv, ");
			sb.append("city, ");
			sb.append("state, ");
			sb.append("zipcode, ");
			sb.append("eventlocation, ");
			sb.append("isconferencegame, ");
			sb.append("neutralsite, ");
			sb.append("linefavorite, ");
			sb.append("lineindicator, ");
			sb.append("linevalue, ");
			sb.append("line, ");
			sb.append("total, ");
			sb.append("attendance, ");
			sb.append("awaywin, ");
			sb.append("homewin, ");
			sb.append("awaycollegename, ");
			sb.append("homecollegename, ");
			sb.append("awaymascotname, ");
			sb.append("homemascotname, ");
			sb.append("awayshortname, ");
			sb.append("homeshortname, ");
			sb.append("awayranking, ");
			sb.append("homeranking, ");
			sb.append("awaywins, ");
			sb.append("homewins, ");
			sb.append("awaylosses, ");
			sb.append("homelosses, ");
			sb.append("awayfirstquarterscore, ");
			sb.append("homefirstquarterscore, ");
			sb.append("awaysecondquarterscore, ");
			sb.append("homesecondquarterscore, ");
			sb.append("awayfirsthalfscore, ");
			sb.append("homefirsthalfscore, ");
			sb.append("awaythirdquarterscore, ");
			sb.append("homethirdquarterscore, ");
			sb.append("awayfourthquarterscore, ");
			sb.append("homefourthquarterscore, ");
			sb.append("awaysecondhalfscore, ");
			sb.append("homesecondhalfscore, ");
			sb.append("awayotonescore, ");
			sb.append("homeotonescore, ");
			sb.append("awayottwoscore, ");
			sb.append("homeottwoscore, ");
			sb.append("awayotthreescore, ");
			sb.append("homeotthreescore, ");
			sb.append("awayotfourscore, ");
			sb.append("homeotfourscore, ");
			sb.append("awayotfivescore, ");
			sb.append("homeotfivescore, ");
			sb.append("awayotsixscore, ");
			sb.append("homeotsixscore, ");
			sb.append("awaysecondhalfotscore, ");
			sb.append("homesecondhalfotscore, ");
			sb.append("awayfinalscore, ");
			sb.append("homefinalscore, ");
			sb.append("awayfirstdowns, ");
			sb.append("homefirstdowns, ");
			sb.append("awaythirdefficiencymade, ");
			sb.append("homethirdefficiencymade, ");
			sb.append("awaythirdefficiencyattempts, ");
			sb.append("homethirdefficiencyattempts, ");
			sb.append("awayfourthefficiencymade, ");
			sb.append("homefourthefficiencymade, ");
			sb.append("awayfourthefficiencyattempts, ");
			sb.append("homefourthefficiencyattempts, ");
			sb.append("awaytotalyards, ");
			sb.append("hometotalyards, ");
			sb.append("awaypassingyards, ");
			sb.append("homepassingyards, ");
			sb.append("awaypasscomp, ");
			sb.append("homepasscomp, ");
			sb.append("awaypassattempts, ");
			sb.append("homepassattempts, ");
			sb.append("awayyardsperpass, ");
			sb.append("homeyardsperpass, ");
			sb.append("awayrushingyards, ");
			sb.append("homerushingyards, ");
			sb.append("awayrushingattempts, ");
			sb.append("homerushingattempts, ");
			sb.append("awayyardsperrush, ");
			sb.append("homeyardsperrush, ");
			sb.append("awaypenalties, ");
			sb.append("homepenalties, ");
			sb.append("awaypenaltyyards, ");
			sb.append("homepenaltyyards, ");
			sb.append("awayturnovers, ");
			sb.append("hometurnovers, ");
			sb.append("awayfumbleslost, ");
			sb.append("homefumbleslost, ");
			sb.append("awayinterceptions, ");
			sb.append("homeinterceptions, ");
			sb.append("awaypossessionminutes, ");
			sb.append("homepossessionminutes, ");
			sb.append("awaypossessionseconds, ");
			sb.append("homepossessionseconds, ");
			sb.append("awaysagrinrating, ");
			sb.append("homesagrinrating, ");
			sb.append("awaymasseyrating, ");
			sb.append("homemasseyrating, ");
			sb.append("awaymasseyranking, ");
			sb.append("homemasseyranking, ");
			sb.append("awaysos, ");
			sb.append("homesos, ");
			sb.append("awayisfbs, ");
			sb.append("homeisfbs, ");
			sb.append("datecreated, ");
			sb.append("datemodified) ");
			sb.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,");
			sb.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,");
			sb.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,");
			sb.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,");
			sb.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,");
			sb.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,");
			sb.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,");
			sb.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,");
			sb.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,");
			sb.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,");
			sb.append(" ?, ?, ?, ?, ?, ?, ?, ?,");
			sb.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?)");

			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, gameData.getEspngameid());
			stmt.setInt(2, gameData.getWeek());
			stmt.setInt(3, gameData.getHour());
			stmt.setInt(4, gameData.getMinute());
			stmt.setString(5, gameData.getAmpm());
			stmt.setString(6, gameData.getTimezone());
			stmt.setInt(7, gameData.getMonth());
			stmt.setInt(8, gameData.getDay());
			stmt.setInt(9, gameData.getYear());
			final Date gameDate = new Date();
			if (gameData.getGamedate() != null) {
				stmt.setTimestamp(10, new java.sql.Timestamp(gameData.getGamedate().getTime()));
			} else {
				stmt.setTimestamp(10, new java.sql.Timestamp(gameDate.getTime()));
			}
			stmt.setString(11, gameData.getTv());
			stmt.setString(12, gameData.getCity());
			stmt.setString(13, gameData.getState());
			stmt.setInt(14, gameData.getZipcode());
			stmt.setString(15, gameData.getEventlocation());
			stmt.setBoolean(16, gameData.getIsconferencegame());
			stmt.setBoolean(17, gameData.getNeutralsite());
			stmt.setString(18, gameData.getLinefavorite());
			stmt.setString(19, gameData.getLineindicator());
			stmt.setFloat(20, gameData.getLinevalue());
			stmt.setFloat(21, gameData.getLine());
			stmt.setFloat(22, gameData.getTotal());
			stmt.setInt(23, gameData.getAttendance());
			stmt.setBoolean(24, gameData.getAwaywin());
			stmt.setBoolean(25, gameData.getHomewin());
			stmt.setString(26, gameData.getAwaycollegename());
			stmt.setString(27, gameData.getHomecollegename());
			stmt.setString(28, gameData.getAwaymascotname());
			stmt.setString(29, gameData.getHomemascotname());
			stmt.setString(30, gameData.getAwayshortname());
			stmt.setString(31, gameData.getHomeshortname());
			stmt.setInt(32, gameData.getAwayranking());
			stmt.setInt(33, gameData.getHomeranking());
			stmt.setInt(34, gameData.getAwaywins());
			stmt.setInt(35, gameData.getHomewins());
			stmt.setInt(36, gameData.getAwaylosses());
			stmt.setInt(37, gameData.getHomelosses());
			stmt.setInt(38, gameData.getAwayfirstquarterscore());
			stmt.setInt(39, gameData.getHomefirstquarterscore());
			stmt.setInt(40, gameData.getAwaysecondquarterscore());
			stmt.setInt(41, gameData.getHomesecondquarterscore());
			stmt.setInt(42, gameData.getAwayfirsthalfscore());
			stmt.setInt(43, gameData.getHomefirsthalfscore());
			stmt.setInt(44, gameData.getAwaythirdquarterscore());
			stmt.setInt(45, gameData.getHomethirdquarterscore());
			stmt.setInt(46, gameData.getAwayfourthquarterscore());
			stmt.setInt(47, gameData.getHomefourthquarterscore());
			stmt.setInt(48, gameData.getAwaysecondhalfscore());
			stmt.setInt(49, gameData.getHomesecondhalfscore());
			stmt.setInt(50, gameData.getAwayotonescore());
			stmt.setInt(51, gameData.getHomeotonescore());
			stmt.setInt(52, gameData.getAwayottwoscore());
			stmt.setInt(53, gameData.getHomeottwoscore());
			stmt.setInt(54, gameData.getAwayotthreescore());
			stmt.setInt(55, gameData.getHomeotthreescore());
			stmt.setInt(56, gameData.getAwayotfourscore());
			stmt.setInt(57, gameData.getHomeotfourscore());
			stmt.setInt(58, gameData.getAwayotfivescore());
			stmt.setInt(59, gameData.getHomeotfivescore());
			stmt.setInt(60, gameData.getAwayotsixscore());
			stmt.setInt(61, gameData.getHomeotsixscore());
			stmt.setInt(62, gameData.getAwaysecondhalfotscore());
			stmt.setInt(63, gameData.getHomesecondhalfotscore());
			stmt.setInt(64, gameData.getAwayfinalscore());
			stmt.setInt(65, gameData.getHomefinalscore());
			stmt.setInt(66, gameData.getAwayfirstdowns());
			stmt.setInt(67, gameData.getHomefirstdowns());
			stmt.setInt(68, gameData.getAwaythirdefficiencymade());
			stmt.setInt(69, gameData.getHomethirdefficiencymade());
			stmt.setInt(70, gameData.getAwaythirdefficiencyattempts());
			stmt.setInt(71, gameData.getHomethirdefficiencyattempts());
			stmt.setInt(72, gameData.getAwayfourthefficiencymade());
			stmt.setInt(73, gameData.getHomefourthefficiencymade());
			stmt.setInt(74, gameData.getAwayfourthefficiencyattempts());
			stmt.setInt(75, gameData.getHomefourthefficiencyattempts());
			stmt.setInt(76, gameData.getAwaytotalyards());
			stmt.setInt(77, gameData.getHometotalyards());
			stmt.setInt(78, gameData.getAwaypassingyards());
			stmt.setInt(79, gameData.getHomepassingyards());
			stmt.setInt(80, gameData.getAwaypasscomp());
			stmt.setInt(81, gameData.getHomepasscomp());
			stmt.setInt(82, gameData.getAwaypassattempts());
			stmt.setInt(83, gameData.getHomepassattempts());
			stmt.setFloat(84, gameData.getAwayyardsperpass());
			stmt.setFloat(85, gameData.getHomeyardsperpass());
			stmt.setInt(86, gameData.getAwayrushingyards());
			stmt.setInt(87, gameData.getHomerushingyards());
			stmt.setInt(88, gameData.getAwayrushingattempts());
			stmt.setInt(89, gameData.getHomerushingattempts());
			stmt.setFloat(90, gameData.getAwayyardsperrush());
			stmt.setFloat(91, gameData.getHomeyardsperrush());
			stmt.setInt(92, gameData.getAwaypenalties());
			stmt.setInt(93, gameData.getHomepenalties());
			stmt.setInt(94, gameData.getAwaypenaltyyards());
			stmt.setInt(95, gameData.getHomepenaltyyards());
			stmt.setInt(96, gameData.getAwayturnovers());
			stmt.setInt(97, gameData.getHometurnovers());
			stmt.setInt(98, gameData.getAwayfumbleslost());
			stmt.setInt(99, gameData.getHomefumbleslost());
			stmt.setInt(100, gameData.getAwayinterceptions());
			stmt.setInt(101, gameData.getHomeinterceptions());
			stmt.setInt(102, gameData.getAwaypossessionminutes());
			stmt.setInt(103, gameData.getHomepossessionminutes());
			stmt.setInt(104, gameData.getAwaypossessionseconds());
			stmt.setInt(105, gameData.getHomepossessionseconds());
			stmt.setFloat(106, gameData.getAwaysagrinrating());
			stmt.setFloat(107, gameData.getHomesagrinrating());
			stmt.setFloat(108, gameData.getAwaymasseyrating());
			stmt.setFloat(109, gameData.getHomemasseyrating());
			stmt.setInt(110, gameData.getAwaymasseyranking());
			stmt.setInt(111, gameData.getHomemasseyranking());
			stmt.setFloat(112, gameData.getAwaysos());
			stmt.setFloat(113, gameData.getHomesos());
			stmt.setBoolean(114, gameData.getAwayisfbs());
			stmt.setBoolean(115, gameData.getHomeisfbs());
			stmt.setTimestamp(116, new java.sql.Timestamp(gameDate.getTime()));
			stmt.setTimestamp(117, new java.sql.Timestamp(gameDate.getTime()));

			stmt.executeUpdate();
		} catch (SQLException sqle) {
			LOGGER.error("gameData: " + gameData);
			LOGGER.error(sqle);
			throw sqle;
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (resultSet != null) {
				resultSet.close();
			}
		}

		LOGGER.info("Exiting persistEspnNcaafGameData()");
	}

	/**
	 * 
	 * @param team
	 * @param week
	 * @param year
	 * @return
	 * @throws SQLException
	 */
	public List<EspnCollegeFootballGameData> getEspnNcaafGameDataByYearByTeam(String team, Integer year) throws SQLException {
		LOGGER.info("Entering getEspnNcaafGameDataByYearByTeam()");
		LOGGER.debug("year: " + year);
		final List<EspnCollegeFootballGameData> games = new ArrayList<EspnCollegeFootballGameData>();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {
			stmt = conn.prepareStatement("SELECT * FROM espnfootballgame where year = ? AND (awaycollegename = ? OR homecollegename = ?) ORDER BY week DESC");
			stmt.setLong(1, year);
			stmt.setString(2, team);
			stmt.setString(3, team);
			resultSet = stmt.executeQuery();

			while (resultSet.next()) {
				final EspnCollegeFootballGameData game = populateData(resultSet);
				games.add(game);
			}
		} catch (SQLException sqle) {
			LOGGER.error(sqle);
			throw sqle;
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (resultSet != null) {
				resultSet.close();
			}
		}

		LOGGER.info("Exiting getEspnNcaafGameDataByYearByTeam()");
		return games;
	}

	/**
	 * 
	 * @param team
	 * @param week
	 * @param year
	 * @return
	 * @throws SQLException
	 */
	public List<EspnCollegeFootballGameData> getEspnNcaafGameDataByWeekByTeam(String team, Integer week, Integer year) throws SQLException {
		LOGGER.info("Entering getEspnNcaafGameDataByWeekByTeam()");
		LOGGER.debug("week: " + week);
		LOGGER.debug("year: " + year);
		final List<EspnCollegeFootballGameData> games = new ArrayList<EspnCollegeFootballGameData>();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {
			stmt = conn.prepareStatement("SELECT * FROM espnfootballgame where week < ? AND year = ? AND (awaycollegename = ? OR homecollegename = ?) ORDER BY week DESC");
			stmt.setLong(1, week);
			stmt.setLong(2, year);
			stmt.setString(3, team);
			stmt.setString(4, team);
			resultSet = stmt.executeQuery();

			while (resultSet.next()) {
				final EspnCollegeFootballGameData game = populateData(resultSet);
				games.add(game);
			}
		} catch (SQLException sqle) {
			LOGGER.error(sqle);
			throw sqle;
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (resultSet != null) {
				resultSet.close();
			}
		}

		LOGGER.info("Exiting getEspnNcaafGameDataByWeekByTeam()");
		return games;
	}

	/**
	 * 
	 * @param year
	 * @return
	 * @throws SQLException
	 */
	public List<EspnCollegeFootballGameData> getEspnNcaafGameDataByYearDesc(Integer year) throws SQLException {
		LOGGER.info("Entering getEspnNcaafGameDataByYearDesc()");
		LOGGER.debug("year: " + year);
		final List<EspnCollegeFootballGameData> games = new ArrayList<EspnCollegeFootballGameData>();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {
			stmt = conn.prepareStatement("SELECT * FROM espnfootballgame where year = ? ORDER BY week DESC");
			stmt.setLong(1, year);
			resultSet = stmt.executeQuery();

			while (resultSet.next()) {
				final EspnCollegeFootballGameData game = populateData(resultSet);
				games.add(game);
			}
		} catch (SQLException sqle) {
			LOGGER.error(sqle);
			throw sqle;
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (resultSet != null) {
				resultSet.close();
			}
		}

		LOGGER.info("Exiting getEspnNcaafGameDataByYearDesc()");
		return games;
	}

	/**
	 * 
	 * @param year
	 * @return
	 * @throws SQLException
	 */
	public List<EspnCollegeFootballGameData> getEspnNcaafGameDataByWeekYearDesc(Integer week, Integer year) throws SQLException {
		LOGGER.info("Entering getEspnNcaafGameDataByWeekYearDesc()");
		LOGGER.debug("year: " + year);
		final List<EspnCollegeFootballGameData> games = new ArrayList<EspnCollegeFootballGameData>();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {
			stmt = conn.prepareStatement("SELECT * FROM espnfootballgame where week < ? AND year = ? ORDER BY week DESC");
			stmt.setInt(1, week.intValue());
			stmt.setLong(2, year);
			resultSet = stmt.executeQuery();

			while (resultSet.next()) {
				final EspnCollegeFootballGameData game = populateData(resultSet);
				games.add(game);
			}
		} catch (SQLException sqle) {
			LOGGER.error(sqle);
			throw sqle;
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (resultSet != null) {
				resultSet.close();
			}
		}

		LOGGER.info("Exiting getEspnNcaafGameDataByWeekYearDesc()");
		return games;
	}

	/**
	 * 
	 * @param week
	 * @param year
	 * @return
	 * @throws SQLException
	 */
	public List<EspnCollegeFootballGameData> getEspnNcaafGameDataByWeek(Integer week, Integer year) throws SQLException {
		LOGGER.info("Entering getEspnNcaafGameDataByWeek()");
		LOGGER.debug("week: " + week);
		LOGGER.debug("year: " + year);
		final List<EspnCollegeFootballGameData> games = new ArrayList<EspnCollegeFootballGameData>();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {
			stmt = conn.prepareStatement("SELECT * FROM espnfootballgame where week < ? and year = ?");
			stmt.setLong(1, week);
			stmt.setLong(2, year);
			resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				final EspnCollegeFootballGameData game = populateData(resultSet);
				games.add(game);
			}
		} catch (SQLException sqle) {
			LOGGER.error(sqle);
			throw sqle;
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (resultSet != null) {
				resultSet.close();
			}
		}

		LOGGER.info("Exiting getEspnNcaafGameDataByWeek()");
		return games;
	}

	/**
	 * 
	 * @param week
	 * @param year
	 * @param home
	 * @return
	 * @throws SQLException
	 */
	public List<EspnCollegeFootballGameData> getEspnNcaafGameDataByWeek(Integer week, Integer year, boolean home) throws SQLException {
		LOGGER.info("Entering getEspnNcaafGameDataByWeek()");
		final List<EspnCollegeFootballGameData> games = new ArrayList<EspnCollegeFootballGameData>();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {
			if (home) {
				stmt = conn.prepareStatement("SELECT * FROM espnfootballgame where week = ? and year = ? and stattype='home'");
			} else {
				stmt = conn.prepareStatement("SELECT * FROM espnfootballgame where week = ? and year = ? and stattype='away'");
			}
			stmt.setLong(1, week);
			stmt.setLong(2, year);
			resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				final EspnCollegeFootballGameData game = populateData(resultSet);
				games.add(game);
			}
		} catch (SQLException sqle) {
			LOGGER.error(sqle);
			throw sqle;
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (resultSet != null) {
				resultSet.close();
			}
		}

		LOGGER.info("Exiting getEspnNcaafGameDataByWeek()");
		return games;
	}

	/**
	 * 
	 * @param week
	 * @param year
	 * @return
	 * @throws SQLException
	 */
	public List<EspnCollegeFootballGameData> getAllEspnNcaafGameDataFromWeek(Integer week, Integer year) throws SQLException {
		LOGGER.info("Entering getAllEspnNcaafGameDataFromWeek()");
		LOGGER.debug("week: " + week);
		LOGGER.debug("year: " + year);
		final List<EspnCollegeFootballGameData> games = new ArrayList<EspnCollegeFootballGameData>();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {
			stmt = conn.prepareStatement("SELECT * FROM espnfootballgame where week <= ? and year = ? ORDER BY WEEK DESC");
			stmt.setLong(1, week);
			stmt.setLong(2, year);
			resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				final EspnCollegeFootballGameData game = populateData(resultSet);
				games.add(game);
			}
		} catch (SQLException sqle) {
			LOGGER.error(sqle);
			throw sqle;
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (resultSet != null) {
				resultSet.close();
			}
		}

		LOGGER.info("Exiting getAllEspnNcaafGameDataFromWeek()");
		return games;
	}

	/**
	 * 
	 * @param week
	 * @param year
	 * @param team
	 * @return
	 * @throws SQLException
	 */
	public boolean checkHasGamesFromWeek(Integer week, Integer year, String team) throws SQLException {
		LOGGER.info("Entering checkHasGamesFromWeek()");
		LOGGER.debug("week: " + week);
		LOGGER.debug("year: " + year);
		LOGGER.debug("team: " + team);
		boolean hasGames = false;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {
			stmt = conn.prepareStatement("SELECT * FROM espnfootballgame where week <= ? and year = ? and (awaycollegename = ? OR homecollegename = ?) ORDER BY WEEK DESC");
			stmt.setLong(1, week);
			stmt.setLong(2, year);
			stmt.setString(3, team);
			stmt.setString(4, team);
			resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				hasGames = true;
			}
		} catch (SQLException sqle) {
			LOGGER.error(sqle);
			throw sqle;
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (resultSet != null) {
				resultSet.close();
			}
		}

		LOGGER.info("Exiting checkHasGamesFromWeek()");
		return hasGames;
	}

	/**
	 * 
	 * @param week
	 * @param year
	 * @param stattype
	 * @param collegename
	 * @param firstdownspergame
	 * @param oppfirstdownspergame
	 * @param firstdownspergametotal
	 * @param oppfirstdownspergametotal
	 * @param numgames
	 * @throws SQLException
	 */
	public void persistFootballfirstdownspergame(Integer week, 
			Integer year, 
			String stattype, 
			String collegename, 
			Float firstdownspergame, 
			Float oppfirstdownspergame,
			Float firstdownspergametotal,
			Float oppfirstdownspergametotal,
			Integer numgames) throws SQLException {
		LOGGER.info("Entering persistFootballfirstdownspergame()");
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {
			final StringBuffer sbpre = new StringBuffer(2000);
			sbpre.append("SELECT numgames from footballfirstdownspergame where week = ? AND year = ? AND collegename = ?");

			stmt = conn.prepareStatement(sbpre.toString());
			stmt.setInt(1, week);
			stmt.setInt(2, year);
			stmt.setString(3, collegename);

			resultSet = stmt.executeQuery();
			int counter = 0;
			while (resultSet.next()) {
				counter++;
			}
			if (counter > 2) {
				resultSet.close();
				stmt.close();
				LOGGER.error("DUPLICATE game week: " + week + " year: " + year + " collegename: " + collegename);
				return;
			} else {
				resultSet.close();
				stmt.close();
			}

			final StringBuffer sb = new StringBuffer(2000);
			sb.append("INSERT into footballfirstdownspergame (");
			sb.append("week, ");
			sb.append("year, ");
			sb.append("stattype, ");
			sb.append("collegename, ");
			sb.append("firstdownspergame, ");
			sb.append("oppfirstdownspergame, ");
			sb.append("firstdownspergametotal, ");
			sb.append("oppfirstdownspergametotal, ");
			sb.append("numgames) ");
			sb.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, week);
			stmt.setInt(2, year);
			stmt.setString(3, stattype);
			stmt.setString(4, collegename);
			stmt.setFloat(5, firstdownspergame);
			stmt.setFloat(6, oppfirstdownspergame);
			stmt.setFloat(7, firstdownspergametotal);
			stmt.setFloat(8, oppfirstdownspergametotal);
			stmt.setFloat(9, numgames);

			stmt.executeUpdate();
		} catch (SQLException sqle) {
			String error = sqle.getMessage();
			if (error != null && error.contains("duplicate key value violates")) {
				LOGGER.error(sqle.getMessage(), sqle);
			} else {
				LOGGER.error(sqle.getMessage(), sqle);
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}

		LOGGER.info("Exiting persistFootballfirstdownspergame()");
	}

	/**
	 * 
	 * @param year
	 * @param stattype
	 * @param collegename
	 * @return
	 * @throws BatchException
	 * @throws SQLException
	 */
	public FloatData getFootballfirstdownspergame(Integer year, String stattype, String collegename) throws BatchException, SQLException {
		LOGGER.info("Entering getFootballfirstdownspergame()");
		final FloatData floatData = new FloatData();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {	
			final StringBuffer sb = new StringBuffer(2000);
			sb.append("SELECT firstdownspergame, oppfirstdownspergame, firstdownspergametotal, oppfirstdownspergametotal, numgames from footballfirstdownspergame where year = ? AND collegename = ? AND stattype = ? ORDER by week DESC");

			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, year);
			stmt.setString(2, collegename);
			stmt.setString(3, stattype);

			resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				floatData.setFloatdata(resultSet.getFloat(1));
				floatData.setOppfloatdata(resultSet.getFloat(2));
				floatData.setFloatdatatotal(resultSet.getFloat(3));
				floatData.setOppfloatdatatotal(resultSet.getFloat(4));
				floatData.setNumgames(resultSet.getInt(5));
			}
		} catch (SQLException sqle) {
			LOGGER.error(sqle.getMessage(), sqle);
			throw sqle;
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}

		LOGGER.info("Exiting getFootballfirstdownspergame()");
		return floatData;
	}

	/**
	 * 
	 * @param week
	 * @param year
	 * @param stattype
	 * @param collegename
	 * @return
	 * @throws BatchException
	 * @throws SQLException
	 */
	public FloatData getFootballfirstdownspergameForWeek(Integer week, Integer year, String stattype, String collegename) throws BatchException, SQLException {
		LOGGER.info("Entering getFootballfirstdownspergameForWeek()");
		final FloatData floatData = new FloatData();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {	
			final StringBuffer sb = new StringBuffer(2000);
			sb.append("SELECT firstdownspergame, oppfirstdownspergame, firstdownspergametotal, oppfirstdownspergametotal, numgames from footballfirstdownspergame where week < ? AND year = ? AND collegename = ? AND stattype =? ORDER by week DESC");

			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, week);
			stmt.setInt(2, year);
			stmt.setString(3, collegename);
			stmt.setString(4, stattype);

			resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				floatData.setFloatdata(resultSet.getFloat(1));
				floatData.setOppfloatdata(resultSet.getFloat(2));
				floatData.setFloatdatatotal(resultSet.getFloat(3));
				floatData.setOppfloatdatatotal(resultSet.getFloat(4));
				floatData.setNumgames(resultSet.getInt(5));
			}
		} catch (SQLException sqle) {
			LOGGER.error(sqle.getMessage(), sqle);
			throw sqle;
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}

		LOGGER.info("Exiting getFootballfirstdownspergameForWeek()");
		return floatData;
	}

	/**
	 * 
	 * @param week
	 * @param year
	 * @param stattype
	 * @param collegename
	 * @param fourthdowneffpergame
	 * @param oppfourthdowneffpergame
	 * @param fourthdowneffpergametotal
	 * @param oppfourthdowneffpergametotal
	 * @param numgames
	 * @param made
	 * @param attempts
	 * @param oppmade
	 * @param oppattempts
	 * @param madetotal
	 * @param attemptstotal
	 * @param oppmadetotal
	 * @param oppattemptstotal
	 * @throws SQLException
	 */
	public void persistFootballfourthdowneffpergame(Integer week, 
			Integer year, 
			String stattype, 
			String collegename, 
			Float fourthdowneffpergame, 
			Float oppfourthdowneffpergame,
			Float fourthdowneffpergametotal, 
			Float oppfourthdowneffpergametotal,
			Integer numgames, 
			Float made, 
			Float attempts, 
			Float oppmade, 
			Float oppattempts, 
			Float madetotal, 
			Float attemptstotal, 
			Float oppmadetotal, 
			Float oppattemptstotal) throws SQLException {
		LOGGER.info("Entering persistFootballfourthdowneffpergame()");
		PreparedStatement stmt = null;

		try {	
			final StringBuffer sb = new StringBuffer(2000);
			sb.append("INSERT into footballfourthdowneffpergame (");
			sb.append("week, ");
			sb.append("year, ");
			sb.append("stattype, ");
			sb.append("collegename, ");
			sb.append("fourthdowneffpergame, ");
			sb.append("oppfourthdowneffpergame, ");
			sb.append("fourthdowneffpergametotal, ");
			sb.append("oppfourthdowneffpergametotal, ");
			sb.append("numgames, ");
			sb.append("made, ");
			sb.append("attempts, ");
			sb.append("oppmade, ");
			sb.append("oppattempts, ");
			sb.append("madetotal, ");
			sb.append("attemptstotal, ");
			sb.append("oppmadetotal, ");
			sb.append("oppattemptstotal) ");
			sb.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, week);
			stmt.setInt(2, year);
			stmt.setString(3, stattype);
			stmt.setString(4, collegename);
			stmt.setFloat(5, fourthdowneffpergame);
			stmt.setFloat(6, oppfourthdowneffpergame);
			stmt.setFloat(7, fourthdowneffpergametotal);
			stmt.setFloat(8, oppfourthdowneffpergametotal);
			stmt.setInt(9, numgames);
			stmt.setFloat(10, made);
			stmt.setFloat(11, attempts);
			stmt.setFloat(12, oppmade);
			stmt.setFloat(13, oppattempts);
			stmt.setFloat(14, madetotal);
			stmt.setFloat(15, attemptstotal);
			stmt.setFloat(16, oppmadetotal);
			stmt.setFloat(17, oppattemptstotal);

			stmt.executeUpdate();
		} catch (SQLException sqle) {
			LOGGER.error(sqle.getMessage(), sqle);
//			throw sqle;
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}

		LOGGER.info("Exiting persistFootballfourthdowneffpergame()");
	}

	/**
	 * 
	 * @param year
	 * @param stattype
	 * @param collegename
	 * @return
	 * @throws BatchException
	 * @throws SQLException
	 */
	public Efficiencies getFootballfourthdowneffpergame(Integer year, String stattype, String collegename) throws BatchException, SQLException {
		LOGGER.info("Entering getFootballfourthdowneffpergame()");
		final Efficiencies floatData = new Efficiencies();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {	
			final StringBuffer sb = new StringBuffer(2000);
			sb.append("SELECT fourthdowneffpergame, oppfourthdowneffpergame, fourthdowneffpergametotal, oppfourthdowneffpergametotal, numgames, "
					+ "made, attempts, oppmade, oppattempts, madetotal, attemptstotal, oppmadetotal, oppattemptstotal from footballfourthdowneffpergame where year = ? AND collegename = ? AND stattype = ? ORDER BY week DESC");

			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, year);
			stmt.setString(2, collegename);
			stmt.setString(3, stattype);

			resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				floatData.setFloatdata(resultSet.getFloat(1));
				floatData.setOppfloatdata(resultSet.getFloat(2));
				floatData.setFloatdatatotal(resultSet.getFloat(3));
				floatData.setOppfloatdatatotal(resultSet.getFloat(4));
				floatData.setNumgames(resultSet.getInt(5));
				floatData.setMade(resultSet.getFloat(6));
				floatData.setAttempts(resultSet.getFloat(7));
				floatData.setOppmade(resultSet.getFloat(8));
				floatData.setOppattempts(resultSet.getFloat(9));
				floatData.setMadetotal(resultSet.getFloat(10));
				floatData.setAttemptstotal(resultSet.getFloat(11));
				floatData.setOppmadetotal(resultSet.getFloat(12));
				floatData.setOppattemptstotal(resultSet.getFloat(13));
			}
		} catch (SQLException sqle) {
			LOGGER.error(sqle.getMessage(), sqle);
			// throw sqle;
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}

		LOGGER.info("Exiting getFootballfourthdowneffpergame()");
		return floatData;
	}

	/**
	 * 
	 * @param week
	 * @param year
	 * @param stattype
	 * @param collegename
	 * @return
	 * @throws BatchException
	 * @throws SQLException
	 */
	public Efficiencies getFootballfourthdowneffpergameForWeek(Integer week, Integer year, String stattype, String collegename) throws BatchException, SQLException {
		LOGGER.info("Entering getFootballfourthdowneffpergameForWeek()");
		final Efficiencies floatData = new Efficiencies();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {	
			final StringBuffer sb = new StringBuffer(2000);
			sb.append("SELECT fourthdowneffpergame, oppfourthdowneffpergame, fourthdowneffpergametotal, oppfourthdowneffpergametotal, numgames, "
					+ "made, attempts, oppmade, oppattempts, madetotal, attemptstotal, oppmadetotal, oppattemptstotal from footballfourthdowneffpergame where week < ? AND year = ? AND collegename = ? AND stattype = ? ORDER BY week DESC");

			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, week);
			stmt.setInt(2, year);
			stmt.setString(3, collegename);
			stmt.setString(4, stattype);

			resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				floatData.setFloatdata(resultSet.getFloat(1));
				floatData.setOppfloatdata(resultSet.getFloat(2));
				floatData.setFloatdatatotal(resultSet.getFloat(3));
				floatData.setOppfloatdatatotal(resultSet.getFloat(4));
				floatData.setNumgames(resultSet.getInt(5));
				floatData.setMade(resultSet.getFloat(6));
				floatData.setAttempts(resultSet.getFloat(7));
				floatData.setOppmade(resultSet.getFloat(8));
				floatData.setOppattempts(resultSet.getFloat(9));
				floatData.setMadetotal(resultSet.getFloat(10));
				floatData.setAttemptstotal(resultSet.getFloat(11));
				floatData.setOppmadetotal(resultSet.getFloat(12));
				floatData.setOppattemptstotal(resultSet.getFloat(13));
			}
		} catch (SQLException sqle) {
			LOGGER.error(sqle.getMessage(), sqle);
			// throw sqle;
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}

		LOGGER.info("Exiting getFootballfourthdowneffpergameForWeek()");
		return floatData;
	}

	/**
	 * 
	 * @param week
	 * @param year
	 * @param stattype
	 * @param collegename
	 * @param passattemptspergame
	 * @param opppassattemptspergame
	 * @param passattemptspergametotal
	 * @param opppassattemptspergametotal
	 * @param numgames
	 * @throws SQLException
	 */
	public void persistFootballpassattemptspergame(Integer week, 
			Integer year, 
			String stattype, 
			String collegename, 
			Float passattemptspergame, 
			Float opppassattemptspergame,
			Float passattemptspergametotal,
			Float opppassattemptspergametotal,
			Integer numgames) throws SQLException {
		LOGGER.info("Entering persistFootballpassattemptspergame()");
		PreparedStatement stmt = null;

		try {	
			final StringBuffer sb = new StringBuffer(2000);
			sb.append("INSERT into footballpassattemptspergame (");
			sb.append("week, ");
			sb.append("year, ");
			sb.append("stattype, ");
			sb.append("collegename, ");
			sb.append("passattemptspergame, ");
			sb.append("opppassattemptspergame, ");
			sb.append("passattemptspergametotal, ");
			sb.append("opppassattemptspergametotal, ");
			sb.append("numgames) ");
			sb.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, week);
			stmt.setInt(2, year);
			stmt.setString(3, stattype);
			stmt.setString(4, collegename);
			stmt.setFloat(5, passattemptspergame);
			stmt.setFloat(6, opppassattemptspergame);
			stmt.setFloat(7, passattemptspergametotal);
			stmt.setFloat(8, opppassattemptspergametotal);
			stmt.setInt(9, numgames);

			stmt.executeUpdate();
		} catch (SQLException sqle) {
			LOGGER.error(sqle.getMessage(), sqle);
			// throw sqle;
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}

		LOGGER.info("Exiting persistFootballpassattemptspergame()");
	}

	/**
	 * 
	 * @param year
	 * @param stattype
	 * @param collegename
	 * @return
	 * @throws BatchException
	 * @throws SQLException
	 */
	public FloatData getFootballpassattemptspergame(Integer year, String stattype, String collegename) throws BatchException, SQLException {
		LOGGER.info("Entering getFootballpassattemptspergame()");
		final FloatData floatData = new FloatData();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {	
			final StringBuffer sb = new StringBuffer(2000);
			sb.append("SELECT passattemptspergame, opppassattemptspergame, passattemptspergametotal, opppassattemptspergametotal, numgames from footballpassattemptspergame where year = ? AND collegename = ? AND stattype = ? ORDER BY week DESC");

			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, year);
			stmt.setString(2, collegename);
			stmt.setString(3, stattype);

			resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				floatData.setFloatdata(resultSet.getFloat(1));
				floatData.setOppfloatdata(resultSet.getFloat(2));
				floatData.setFloatdatatotal(resultSet.getFloat(3));
				floatData.setOppfloatdatatotal(resultSet.getFloat(4));
				floatData.setNumgames(resultSet.getInt(5));
			}
		} catch (SQLException sqle) {
			LOGGER.error(sqle.getMessage(), sqle);
			throw sqle;
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}

		LOGGER.info("Exiting getFootballpassattemptspergame()");
		return floatData;
	}

	/**
	 * 
	 * @param week
	 * @param year
	 * @param stattype
	 * @param collegename
	 * @return
	 * @throws BatchException
	 * @throws SQLException
	 */
	public FloatData getFootballpassattemptspergameForWeek(Integer week, Integer year, String stattype, String collegename) throws BatchException, SQLException {
		LOGGER.info("Entering getFootballpassattemptspergameForWeek()");
		final FloatData floatData = new FloatData();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {	
			final StringBuffer sb = new StringBuffer(2000);
			sb.append("SELECT passattemptspergame, opppassattemptspergame, passattemptspergametotal, opppassattemptspergametotal, numgames from footballpassattemptspergame where week < ? AND year = ? AND collegename = ? AND stattype =? ORDER BY week DESC");

			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, week);
			stmt.setInt(2, year);
			stmt.setString(3, collegename);
			stmt.setString(4, stattype);

			resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				floatData.setFloatdata(resultSet.getFloat(1));
				floatData.setOppfloatdata(resultSet.getFloat(2));
				floatData.setFloatdatatotal(resultSet.getFloat(3));
				floatData.setOppfloatdatatotal(resultSet.getFloat(4));
				floatData.setNumgames(resultSet.getInt(5));
			}
		} catch (SQLException sqle) {
			LOGGER.error(sqle.getMessage(), sqle);
			throw sqle;
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}

		LOGGER.info("Exiting getFootballpassattemptspergameForWeek()");
		return floatData;
	}

	/**
	 * 
	 * @param week
	 * @param year
	 * @param stattype
	 * @param collegename
	 * @param passcompletionspergame
	 * @param opppasscompletionspergame
	 * @param passcompletionspergametotal
	 * @param opppasscompletionspergametotal
	 * @param numgames
	 * @throws SQLException
	 */
	public void persistFootballpasscompletionspergame(Integer week, 
			Integer year, 
			String stattype, 
			String collegename, 
			Float passcompletionspergame, 
			Float opppasscompletionspergame, 
			Float passcompletionspergametotal, 
			Float opppasscompletionspergametotal, 
			Integer numgames) throws SQLException {
		LOGGER.info("Entering persistFootballpasscompletionspergame()");
		PreparedStatement stmt = null;

		try {	
			final StringBuffer sb = new StringBuffer(2000);
			sb.append("INSERT into footballpasscompletionspergame (");
			sb.append("week, ");
			sb.append("year, ");
			sb.append("stattype, ");
			sb.append("collegename, ");
			sb.append("passcompletionspergame, ");
			sb.append("opppasscompletionspergame, ");
			sb.append("passcompletionspergametotal, ");
			sb.append("opppasscompletionspergametotal, ");
			sb.append("numgames) ");
			sb.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, week);
			stmt.setInt(2, year);
			stmt.setString(3, stattype);
			stmt.setString(4, collegename);
			stmt.setFloat(5, passcompletionspergame);
			stmt.setFloat(6, opppasscompletionspergame);
			stmt.setFloat(7, passcompletionspergametotal);
			stmt.setFloat(8, opppasscompletionspergametotal);
			stmt.setInt(9, numgames);

			stmt.executeUpdate();
		} catch (SQLException sqle) {
			LOGGER.error(sqle.getMessage(), sqle);
			// throw sqle;
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}

		LOGGER.info("Exiting persistFootballpasscompletionspergame()");
	}

	/**
	 * 
	 * @param year
	 * @param stattype
	 * @param collegename
	 * @return
	 * @throws BatchException
	 * @throws SQLException
	 */
	public FloatData getFootballpasscompletionspergame(Integer year, String stattype, String collegename) throws BatchException, SQLException {
		LOGGER.info("Entering getFootballpasscompletionspergame()");
		final FloatData floatData = new FloatData();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {	
			final StringBuffer sb = new StringBuffer(2000);
			sb.append("SELECT passcompletionspergame, opppasscompletionspergame, passcompletionspergametotal, opppasscompletionspergametotal, numgames from footballpasscompletionspergame where year = ? AND collegename = ? AND stattype = ? ORDER BY week DESC");

			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, year);
			stmt.setString(2, collegename);
			stmt.setString(3, stattype);

			resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				floatData.setFloatdata(resultSet.getFloat(1));
				floatData.setOppfloatdata(resultSet.getFloat(2));
				floatData.setFloatdatatotal(resultSet.getFloat(3));
				floatData.setOppfloatdatatotal(resultSet.getFloat(4));
				floatData.setNumgames(resultSet.getInt(5));
			}
		} catch (SQLException sqle) {
			LOGGER.error(sqle.getMessage(), sqle);
			throw sqle;
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}

		LOGGER.info("Exiting getFootballpasscompletionspergame()");
		return floatData;
	}

	/**
	 * 
	 * @param week
	 * @param year
	 * @param stattype
	 * @param collegename
	 * @return
	 * @throws BatchException
	 * @throws SQLException
	 */
	public FloatData getFootballpasscompletionspergameForWeek(Integer week, Integer year, String stattype, String collegename) throws BatchException, SQLException {
		LOGGER.info("Entering getFootballpasscompletionspergameForWeek()");
		final FloatData floatData = new FloatData();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {	
			final StringBuffer sb = new StringBuffer(2000);
			sb.append("SELECT passcompletionspergame, opppasscompletionspergame, passcompletionspergametotal, opppasscompletionspergametotal, numgames from footballpasscompletionspergame where week < ? AND year = ? AND collegename = ? AND stattype = ? ORDER BY week DESC");

			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, week);
			stmt.setInt(2, year);
			stmt.setString(3, collegename);
			stmt.setString(4, stattype);

			resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				floatData.setFloatdata(resultSet.getFloat(1));
				floatData.setOppfloatdata(resultSet.getFloat(2));
				floatData.setFloatdatatotal(resultSet.getFloat(3));
				floatData.setOppfloatdatatotal(resultSet.getFloat(4));
				floatData.setNumgames(resultSet.getInt(5));
			}
		} catch (SQLException sqle) {
			LOGGER.error(sqle.getMessage(), sqle);
			throw sqle;
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}

		LOGGER.info("Exiting getFootballpasscompletionspergameForWeek()");
		return floatData;
	}

	/**
	 * 
	 * @param week
	 * @param year
	 * @param stattype
	 * @param collegename
	 * @param passyardspergame
	 * @param opppassyardspergame
	 * @param passyardspergametotal
	 * @param opppassyardspergametotal
	 * @param numgames
	 * @throws SQLException
	 */
	public void persistFootballpassyardspergame(Integer week, 
			Integer year, 
			String stattype, 
			String collegename, 
			Float passyardspergame, 
			Float opppassyardspergame, 
			Float passyardspergametotal, 
			Float opppassyardspergametotal, 
			Integer numgames) throws SQLException {
		LOGGER.info("Entering persistFootballpassyardspergame()");
		PreparedStatement stmt = null;

		try {	
			final StringBuffer sb = new StringBuffer(2000);
			sb.append("INSERT into footballpassyardspergame (");
			sb.append("week, ");
			sb.append("year, ");
			sb.append("stattype, ");
			sb.append("collegename, ");
			sb.append("passyardspergame, ");
			sb.append("opppassyardspergame, ");
			sb.append("passyardspergametotal, ");
			sb.append("opppassyardspergametotal, ");
			sb.append("numgames) ");
			sb.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, week);
			stmt.setInt(2, year);
			stmt.setString(3, stattype);
			stmt.setString(4, collegename);
			stmt.setFloat(5, passyardspergame);
			stmt.setFloat(6, opppassyardspergame);
			stmt.setFloat(7, passyardspergametotal);
			stmt.setFloat(8, opppassyardspergametotal);
			stmt.setInt(9, numgames);

			stmt.executeUpdate();
		} catch (SQLException sqle) {
			LOGGER.error(sqle.getMessage(), sqle);
			// throw sqle;
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}

		LOGGER.info("Exiting persistFootballpassyardspergame()");
	}

	/**
	 * 
	 * @param year
	 * @param stattype
	 * @param collegename
	 * @return
	 * @throws BatchException
	 * @throws SQLException
	 */
	public FloatData getFootballpassyardspergame(Integer year, String stattype, String collegename) throws BatchException, SQLException {
		LOGGER.info("Entering getFootballpassyardspergame()");
		final FloatData floatData = new FloatData();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {	
			final StringBuffer sb = new StringBuffer(2000);
			sb.append("SELECT passyardspergame, opppassyardspergame, passyardspergametotal, opppassyardspergametotal, numgames from footballpassyardspergame where year = ? AND collegename = ? AND stattype = ? ORDER BY week DESC");

			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, year);
			stmt.setString(2, collegename);
			stmt.setString(3, stattype);

			resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				floatData.setFloatdata(resultSet.getFloat(1));
				floatData.setOppfloatdata(resultSet.getFloat(2));
				floatData.setFloatdatatotal(resultSet.getFloat(3));
				floatData.setOppfloatdatatotal(resultSet.getFloat(4));
				floatData.setNumgames(resultSet.getInt(5));
			}
		} catch (SQLException sqle) {
			LOGGER.error(sqle.getMessage(), sqle);
			throw sqle;
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}

		LOGGER.info("Exiting getFootballpassyardspergame()");
		return floatData;
	}

	/**
	 * 
	 * @param week
	 * @param year
	 * @param stattype
	 * @param collegename
	 * @return
	 * @throws BatchException
	 * @throws SQLException
	 */
	public FloatData getFootballpassyardspergameForWeek(Integer week, Integer year, String stattype, String collegename) throws BatchException, SQLException {
		LOGGER.info("Entering getFootballpassyardspergameForWeek()");
		final FloatData floatData = new FloatData();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {	
			final StringBuffer sb = new StringBuffer(2000);
			sb.append("SELECT passyardspergame, opppassyardspergame, passyardspergametotal, opppassyardspergametotal, numgames from footballpassyardspergame where week < ? AND year = ? AND collegename = ? AND stattype = ? ORDER BY week DESC");

			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, week);
			stmt.setInt(2, year);
			stmt.setString(3, collegename);
			stmt.setString(4, stattype);

			resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				floatData.setFloatdata(resultSet.getFloat(1));
				floatData.setOppfloatdata(resultSet.getFloat(2));
				floatData.setFloatdatatotal(resultSet.getFloat(3));
				floatData.setOppfloatdatatotal(resultSet.getFloat(4));
				floatData.setNumgames(resultSet.getInt(5));
			}
		} catch (SQLException sqle) {
			LOGGER.error(sqle.getMessage(), sqle);
			throw sqle;
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}

		LOGGER.info("Exiting getFootballpassyardspergameForWeek()");
		return floatData;
	}

	/**
	 * 
	 * @param week
	 * @param year
	 * @param stattype
	 * @param collegename
	 * @param pointspergame
	 * @param opppointspergame
	 * @param pointspergametotal
	 * @param opppointspergametotal
	 * @param numgames
	 * @throws SQLException
	 */
	public void persistFootballpointspergame(Integer week, 
			Integer year, 
			String stattype, 
			String collegename, 
			Float pointspergame, 
			Float opppointspergame, 
			Float pointspergametotal, 
			Float opppointspergametotal, 
			Integer numgames) throws SQLException {
		LOGGER.info("Entering persistFootballpointspergame()");
		PreparedStatement stmt = null;

		try {	
			final StringBuffer sb = new StringBuffer(2000);
			sb.append("INSERT into footballpointspergame (");
			sb.append("week, ");
			sb.append("year, ");
			sb.append("stattype, ");
			sb.append("collegename, ");
			sb.append("pointspergame, ");
			sb.append("opppointspergame, ");
			sb.append("pointspergametotal, ");
			sb.append("opppointspergametotal, ");
			sb.append("numgames) ");
			sb.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, week);
			stmt.setInt(2, year);
			stmt.setString(3, stattype);
			stmt.setString(4, collegename);
			stmt.setFloat(5, pointspergame);
			stmt.setFloat(6, opppointspergame);
			stmt.setFloat(7, pointspergametotal);
			stmt.setFloat(8, opppointspergametotal);
			stmt.setInt(9, numgames);

			stmt.executeUpdate();
		} catch (SQLException sqle) {
			LOGGER.error(sqle.getMessage(), sqle);
			// throw sqle;
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}

		LOGGER.info("Exiting persistFootballpointspergame()");
	}

	/**
	 * 
	 * @param year
	 * @param stattype
	 * @param collegename
	 * @return
	 * @throws BatchException
	 * @throws SQLException
	 */
	public FloatData getFootballpointspergame(Integer year, String stattype, String collegename) throws BatchException, SQLException {
		LOGGER.info("Entering getFootballpointspergame()");
		final FloatData floatData = new FloatData();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {	
			final StringBuffer sb = new StringBuffer(2000);
			sb.append("SELECT pointspergame, opppointspergame, pointspergametotal, opppointspergametotal, numgames from footballpointspergame where year = ? AND collegename = ? AND stattype = ? ORDER BY week DESC");

			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, year);
			stmt.setString(2, collegename);
			stmt.setString(3, stattype);

			resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				floatData.setFloatdata(resultSet.getFloat(1));
				floatData.setOppfloatdata(resultSet.getFloat(2));
				floatData.setFloatdatatotal(resultSet.getFloat(3));
				floatData.setOppfloatdatatotal(resultSet.getFloat(4));
				floatData.setNumgames(resultSet.getInt(5));
			}
		} catch (SQLException sqle) {
			LOGGER.error(sqle.getMessage(), sqle);
			throw sqle;
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}

		LOGGER.info("Exiting getFootballpointspergame()");
		return floatData;
	}

	/**
	 * 
	 * @param week
	 * @param year
	 * @param stattype
	 * @param collegename
	 * @return
	 * @throws BatchException
	 * @throws SQLException
	 */
	public FloatData getFootballpointspergameForWeek(Integer week, Integer year, String stattype, String collegename) throws BatchException, SQLException {
		LOGGER.info("Entering getFootballpointspergameForWeek()");
		final FloatData floatData = new FloatData();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {	
			final StringBuffer sb = new StringBuffer(2000);
			sb.append("SELECT pointspergame, opppointspergame, pointspergametotal, opppointspergametotal, numgames from footballpointspergame where week < ? AND year = ? AND collegename = ? AND stattype = ? ORDER BY week DESC");

			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, week);
			stmt.setInt(2, year);
			stmt.setString(3, collegename);
			stmt.setString(4, stattype);

			resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				floatData.setFloatdata(resultSet.getFloat(1));
				floatData.setOppfloatdata(resultSet.getFloat(2));
				floatData.setFloatdatatotal(resultSet.getFloat(3));
				floatData.setOppfloatdatatotal(resultSet.getFloat(4));
				floatData.setNumgames(resultSet.getInt(5));
			}
		} catch (SQLException sqle) {
			LOGGER.error(sqle.getMessage(), sqle);
			throw sqle;
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}

		LOGGER.info("Exiting getFootballpointspergameForWeek()");
		return floatData;
	}

	/**
	 * 
	 * @param week
	 * @param year
	 * @param stattype
	 * @param collegename
	 * @param possessiontimepergame
	 * @param opppossessiontimepergame
	 * @param possessiontimepergametotal
	 * @param opppossessiontimepergametotal
	 * @param numgames
	 * @throws SQLException
	 */
	public void persistFootballpossessiontimepergame(Integer week, 
			Integer year, 
			String stattype, 
			String collegename, 
			Float possessiontimepergame, 
			Float opppossessiontimepergame, 
			Float possessiontimepergametotal, 
			Float opppossessiontimepergametotal, 
			Integer numgames) throws SQLException {
		LOGGER.info("Entering persistFootballpossessiontimepergame()");
		PreparedStatement stmt = null;

		try {	
			final StringBuffer sb = new StringBuffer(2000);
			sb.append("INSERT into footballpossessiontimepergame (");
			sb.append("week, ");
			sb.append("year, ");
			sb.append("stattype, ");
			sb.append("collegename, ");
			sb.append("possessiontimepergame, ");
			sb.append("opppossessiontimepergame, ");
			sb.append("possessiontimepergametotal, ");
			sb.append("opppossessiontimepergametotal, ");
			sb.append("numgames) ");
			sb.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, week);
			stmt.setInt(2, year);
			stmt.setString(3, stattype);
			stmt.setString(4, collegename);
			stmt.setFloat(5, possessiontimepergame);
			stmt.setFloat(6, opppossessiontimepergame);
			stmt.setFloat(7, possessiontimepergametotal);
			stmt.setFloat(8, opppossessiontimepergametotal);
			stmt.setInt(9, numgames);

			stmt.executeUpdate();
		} catch (SQLException sqle) {
			LOGGER.error(sqle.getMessage(), sqle);
			// throw sqle;
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}

		LOGGER.info("Exiting persistFootballpossessiontimepergame()");
	}

	/**
	 * 
	 * @param year
	 * @param stattype
	 * @param collegename
	 * @return
	 * @throws BatchException
	 * @throws SQLException
	 */
	public FloatData getFootballpossessiontimepergame(Integer year, String stattype, String collegename) throws BatchException, SQLException {
		LOGGER.info("Entering getFootballpossessiontimepergame()");
		final FloatData floatData = new FloatData();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {	
			final StringBuffer sb = new StringBuffer(2000);
			stmt = conn.prepareStatement(sb.toString());
			sb.append("SELECT possessiontimepergame, opppossessiontimepergame, possessiontimepergametotal, opppossessiontimepergametotal, numgames from footballpossessiontimepergame where year = ? AND collegename = ? AND stattype = ? ORDER BY week DESC");

			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, year);
			stmt.setString(2, collegename);
			stmt.setString(3, stattype);

			resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				floatData.setFloatdata(resultSet.getFloat(1));
				floatData.setOppfloatdata(resultSet.getFloat(2));
				floatData.setFloatdatatotal(resultSet.getFloat(3));
				floatData.setOppfloatdatatotal(resultSet.getFloat(4));
				floatData.setNumgames(resultSet.getInt(5));
			}
		} catch (SQLException sqle) {
			LOGGER.error(sqle.getMessage(), sqle);
			throw sqle;
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}

		LOGGER.info("Exiting getFootballpossessiontimepergame()");
		return floatData;
	}

	/**
	 * 
	 * @param week
	 * @param year
	 * @param stattype
	 * @param collegename
	 * @return
	 * @throws BatchException
	 * @throws SQLException
	 */
	public FloatData getFootballpossessiontimepergameForWeek(Integer week, Integer year, String stattype, String collegename) throws BatchException, SQLException {
		LOGGER.info("Entering getFootballpossessiontimepergameForWeek()");
		final FloatData floatData = new FloatData();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {	
			final StringBuffer sb = new StringBuffer(2000);
			stmt = conn.prepareStatement(sb.toString());
			sb.append("SELECT possessiontimepergame, opppossessiontimepergame, possessiontimepergametotal, opppossessiontimepergametotal, numgames from footballpossessiontimepergame where week <= ? AND year = ? AND collegename = ? AND stattype = ? ORDER BY week DESC");

			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, week);
			stmt.setInt(2, year);
			stmt.setString(3, collegename);
			stmt.setString(4, stattype);

			resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				floatData.setFloatdata(resultSet.getFloat(1));
				floatData.setOppfloatdata(resultSet.getFloat(2));
				floatData.setFloatdatatotal(resultSet.getFloat(3));
				floatData.setOppfloatdatatotal(resultSet.getFloat(4));
				floatData.setNumgames(resultSet.getInt(5));
			}
		} catch (SQLException sqle) {
			LOGGER.error(sqle.getMessage(), sqle);
			throw sqle;
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}

		LOGGER.info("Exiting getFootballpossessiontimepergameForWeek()");
		return floatData;
	}

	/**
	 * 
	 * @param week
	 * @param year
	 * @param stattype
	 * @param collegename
	 * @param rushingattemptspergame
	 * @param opprushingattemptspergame
	 * @param rushingattemptspergametotal
	 * @param opprushingattemptspergametotal
	 * @param numgames
	 * @throws SQLException
	 */
	public void persistFootballrushingattemptspergame(Integer week, 
			Integer year, 
			String stattype, 
			String collegename, 
			Float rushingattemptspergame, 
			Float opprushingattemptspergame, 
			Float rushingattemptspergametotal, 
			Float opprushingattemptspergametotal, 
			Integer numgames) throws SQLException {
		LOGGER.info("Entering persistFootballrushingattemptspergame()");
		PreparedStatement stmt = null;

		try {	
			final StringBuffer sb = new StringBuffer(2000);
			sb.append("INSERT into footballrushingattemptspergame (");
			sb.append("week, ");
			sb.append("year, ");
			sb.append("stattype, ");
			sb.append("collegename, ");
			sb.append("rushingattemptspergame, ");
			sb.append("opprushingattemptspergame, ");
			sb.append("rushingattemptspergametotal, ");
			sb.append("opprushingattemptspergametotal, ");
			sb.append("numgames) ");
			sb.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, week);
			stmt.setInt(2, year);
			stmt.setString(3, stattype);
			stmt.setString(4, collegename);
			stmt.setFloat(5, rushingattemptspergame);
			stmt.setFloat(6, opprushingattemptspergame);
			stmt.setFloat(7, rushingattemptspergametotal);
			stmt.setFloat(8, opprushingattemptspergametotal);
			stmt.setInt(9, numgames);

			stmt.executeUpdate();
		} catch (SQLException sqle) {
			LOGGER.error(sqle.getMessage(), sqle);
			// throw sqle;
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}

		LOGGER.info("Exiting persistFootballrushingattemptspergame()");
	}

	/**
	 * 
	 * @param week
	 * @param year
	 * @param stattype
	 * @param collegename
	 * @return
	 * @throws BatchException
	 * @throws SQLException
	 */
	public FloatData getFootballrushingattemptspergame(Integer week, Integer year, String stattype, String collegename) throws BatchException, SQLException {
		LOGGER.info("Entering getFootballrushingattemptspergame()");
		final FloatData floatData = new FloatData();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {	
			final StringBuffer sb = new StringBuffer(2000);
			sb.append("SELECT rushingattemptspergame, opprushingattemptspergame, rushingattemptspergametotal, opprushingattemptspergametotal, numgames from footballrushingattemptspergame where year = ? AND collegename = ? AND stattype = ? ORDER BY week DESC");

			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, year);
			stmt.setString(2, collegename);
			stmt.setString(3, stattype);

			resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				floatData.setFloatdata(resultSet.getFloat(1));
				floatData.setOppfloatdata(resultSet.getFloat(2));
				floatData.setFloatdatatotal(resultSet.getFloat(3));
				floatData.setOppfloatdatatotal(resultSet.getFloat(4));
				floatData.setNumgames(resultSet.getInt(5));
			}
		} catch (SQLException sqle) {
			LOGGER.error(sqle.getMessage(), sqle);
			throw sqle;
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}

		LOGGER.info("Exiting getFootballrushingattemptspergame()");
		return floatData;
	}

	/**
	 * 
	 * @param week
	 * @param year
	 * @param stattype
	 * @param collegename
	 * @return
	 * @throws BatchException
	 * @throws SQLException
	 */
	public FloatData getFootballrushingattemptspergameForWeek(Integer week, Integer year, String stattype, String collegename) throws BatchException, SQLException {
		LOGGER.info("Entering getFootballrushingattemptspergameForWeek()");
		final FloatData floatData = new FloatData();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {	
			final StringBuffer sb = new StringBuffer(2000);
			sb.append("SELECT rushingattemptspergame, opprushingattemptspergame, rushingattemptspergametotal, opprushingattemptspergametotal, numgames from footballrushingattemptspergame where week < ? AND year = ? AND collegename = ? AND stattype = ? ORDER BY week DESC");

			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, week);
			stmt.setInt(2, year);
			stmt.setString(3, collegename);
			stmt.setString(4, stattype);

			resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				floatData.setFloatdata(resultSet.getFloat(1));
				floatData.setOppfloatdata(resultSet.getFloat(2));
				floatData.setFloatdatatotal(resultSet.getFloat(3));
				floatData.setOppfloatdatatotal(resultSet.getFloat(4));
				floatData.setNumgames(resultSet.getInt(5));
			}
		} catch (SQLException sqle) {
			LOGGER.error(sqle.getMessage(), sqle);
			throw sqle;
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}

		LOGGER.info("Exiting getFootballrushingattemptspergameForWeek()");
		return floatData;
	}

	/**
	 * 
	 * @param week
	 * @param year
	 * @param stattype
	 * @param collegename
	 * @param rushingyardspergame
	 * @param opprushingyardspergame
	 * @param rushingyardspergametotal
	 * @param opprushingyardspergametotal
	 * @param numgames
	 * @throws SQLException
	 */
	public void persistFootballrushingyardspergame(Integer week, 
			Integer year, 
			String stattype, 
			String collegename, 
			Float rushingyardspergame, 
			Float opprushingyardspergame, 
			Float rushingyardspergametotal, 
			Float opprushingyardspergametotal, 
			Integer numgames) throws SQLException {
		LOGGER.info("Entering persistFootballrushingyardspergame()");
		PreparedStatement stmt = null;

		try {	
			final StringBuffer sb = new StringBuffer(2000);
			sb.append("INSERT into footballrushingyardspergame (");
			sb.append("week, ");
			sb.append("year, ");
			sb.append("stattype, ");
			sb.append("collegename, ");
			sb.append("rushingyardspergame, ");
			sb.append("opprushingyardspergame, ");
			sb.append("rushingyardspergametotal, ");
			sb.append("opprushingyardspergametotal, ");
			sb.append("numgames) ");
			sb.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, week);
			stmt.setInt(2, year);
			stmt.setString(3, stattype);
			stmt.setString(4, collegename);
			stmt.setFloat(5, rushingyardspergame);
			stmt.setFloat(6, opprushingyardspergame);
			stmt.setFloat(7, rushingyardspergame);
			stmt.setFloat(8, opprushingyardspergame);
			stmt.setInt(9, numgames);

			stmt.executeUpdate();
		} catch (SQLException sqle) {
			LOGGER.error(sqle.getMessage(), sqle);
			// throw sqle;
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}

		LOGGER.info("Exiting persistFootballrushingyardspergame()");
	}

	/**
	 * 
	 * @param week
	 * @param year
	 * @param stattype
	 * @param collegename
	 * @return
	 * @throws BatchException
	 * @throws SQLException
	 */
	public FloatData getFootballrushingyardspergame(Integer week, Integer year, String stattype, String collegename) throws BatchException, SQLException {
		LOGGER.info("Entering getFootballrushingyardspergame()");
		final FloatData floatData = new FloatData();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {	
			final StringBuffer sb = new StringBuffer(2000);
			sb.append("SELECT rushingyardspergame, opprushingyardspergame, rushingyardspergametotal, opprushingyardspergametotal, numgames from footballrushingyardspergame where year = ? AND collegename = ? AND stattype = ? ORDER BY week DESC");

			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, year);
			stmt.setString(2, collegename);
			stmt.setString(3, stattype);

			resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				floatData.setFloatdata(resultSet.getFloat(1));
				floatData.setOppfloatdata(resultSet.getFloat(2));
				floatData.setFloatdatatotal(resultSet.getFloat(3));
				floatData.setOppfloatdatatotal(resultSet.getFloat(4));
				floatData.setNumgames(resultSet.getInt(5));
			}
		} catch (SQLException sqle) {
			LOGGER.error(sqle.getMessage(), sqle);
			throw sqle;
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}

		LOGGER.info("Exiting getFootballrushingyardspergame()");
		return floatData;
	}

	/**
	 * 
	 * @param week
	 * @param year
	 * @param stattype
	 * @param collegename
	 * @return
	 * @throws BatchException
	 * @throws SQLException
	 */
	public FloatData getFootballrushingyardspergameForWeek(Integer week, Integer year, String stattype, String collegename) throws BatchException, SQLException {
		LOGGER.info("Entering getFootballrushingyardspergameForWeek()");
		final FloatData floatData = new FloatData();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {	
			final StringBuffer sb = new StringBuffer(2000);
			sb.append("SELECT rushingyardspergame, opprushingyardspergame, rushingyardspergametotal, opprushingyardspergametotal, numgames from footballrushingyardspergame where week < ? AND year = ? AND collegename = ? AND stattype = ? ORDER BY week DESC");

			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, week);
			stmt.setInt(2, year);
			stmt.setString(3, collegename);
			stmt.setString(4, stattype);

			resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				floatData.setFloatdata(resultSet.getFloat(1));
				floatData.setOppfloatdata(resultSet.getFloat(2));
				floatData.setFloatdatatotal(resultSet.getFloat(3));
				floatData.setOppfloatdatatotal(resultSet.getFloat(4));
				floatData.setNumgames(resultSet.getInt(5));
			}
		} catch (SQLException sqle) {
			LOGGER.error(sqle.getMessage(), sqle);
			throw sqle;
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}

		LOGGER.info("Exiting getFootballrushingyardspergameForWeek()");
		return floatData;
	}

	/**
	 * 
	 * @param week
	 * @param year
	 * @param stattype
	 * @param collegename
	 * @param thirddowneffpergame
	 * @param oppthirddowneffpergame
	 * @param thirddowneffpergametotal
	 * @param oppthirddowneffpergametotal
	 * @param numgames
	 * @param made
	 * @param attempts
	 * @param oppmade
	 * @param oppattempts
	 * @param madetotal
	 * @param attemptstotal
	 * @param oppmadetotal
	 * @param oppattemptstotal
	 * @throws SQLException
	 */
	public void persistFootballthirddowneffpergame(Integer week, 
			Integer year, 
			String stattype, 
			String collegename, 
			Float thirddowneffpergame, 
			Float oppthirddowneffpergame, 
			Float thirddowneffpergametotal, 
			Float oppthirddowneffpergametotal, 
			Integer numgames, 
			Float made, 
			Float attempts, 
			Float oppmade, 
			Float oppattempts, 
			Float madetotal, 
			Float attemptstotal, 
			Float oppmadetotal, 
			Float oppattemptstotal) throws SQLException {
		LOGGER.info("Entering persistFootballthirddowneffpergame()");
		PreparedStatement stmt = null;

		try {	
			final StringBuffer sb = new StringBuffer(2000);
			sb.append("INSERT into footballthirddowneffpergame (");
			sb.append("week, ");
			sb.append("year, ");
			sb.append("stattype, ");
			sb.append("collegename, ");
			sb.append("thirddowneffpergame, ");
			sb.append("oppthirddowneffpergame, ");
			sb.append("thirddowneffpergametotal, ");
			sb.append("oppthirddowneffpergametotal, ");
			sb.append("numgames, ");
			sb.append("made, ");
			sb.append("attempts, ");
			sb.append("oppmade, ");
			sb.append("oppattempts, ");
			sb.append("madetotal, ");
			sb.append("attemptstotal, ");
			sb.append("oppmadetotal, ");
			sb.append("oppattemptstotal) ");
			sb.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, week);
			stmt.setInt(2, year);
			stmt.setString(3, stattype);
			stmt.setString(4, collegename);
			stmt.setFloat(5, thirddowneffpergame);
			stmt.setFloat(6, oppthirddowneffpergame);
			stmt.setFloat(7, thirddowneffpergametotal);
			stmt.setFloat(8, oppthirddowneffpergametotal);
			stmt.setInt(9, numgames);
			stmt.setFloat(10, made);
			stmt.setFloat(11, attempts);
			stmt.setFloat(12, oppmade);
			stmt.setFloat(13, oppattempts);
			stmt.setFloat(14, madetotal);
			stmt.setFloat(15, attemptstotal);
			stmt.setFloat(16, oppmadetotal);
			stmt.setFloat(17, oppattemptstotal);

			stmt.executeUpdate();
		} catch (SQLException sqle) {
			LOGGER.error(sqle.getMessage(), sqle);
			// throw sqle;
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}

		LOGGER.info("Exiting persistFootballthirddowneffpergame()");
	}

	/**
	 * 
	 * @param week
	 * @param year
	 * @param stattype
	 * @param collegename
	 * @return
	 * @throws BatchException
	 * @throws SQLException
	 */
	public Efficiencies getFootballthirddowneffpergame(Integer week, Integer year, String stattype, String collegename) throws BatchException, SQLException {
		LOGGER.info("Entering getFootballthirddowneffpergame()");
		final Efficiencies floatData = new Efficiencies();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {	
			final StringBuffer sb = new StringBuffer(2000);
			sb.append("SELECT thirddowneffpergame, oppthirddowneffpergame, thirddowneffpergametotal, oppthirddowneffpergametotal, numgames, " + 
					"made, attempts, oppmade, oppattempts, madetotal, attemptstotal, oppmadetotal, oppattemptstotal from footballthirddowneffpergame where year = ? AND collegename = ? AND stattype = ? ORDER BY week DESC");

			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, year);
			stmt.setString(2, collegename);
			stmt.setString(3, stattype);

			resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				floatData.setFloatdata(resultSet.getFloat(1));
				floatData.setOppfloatdata(resultSet.getFloat(2));
				floatData.setFloatdatatotal(resultSet.getFloat(3));
				floatData.setOppfloatdatatotal(resultSet.getFloat(4));
				floatData.setNumgames(resultSet.getInt(5));
				floatData.setMade(resultSet.getFloat(6));
				floatData.setAttempts(resultSet.getFloat(7));
				floatData.setOppmade(resultSet.getFloat(8));
				floatData.setOppattempts(resultSet.getFloat(9));
				floatData.setMadetotal(resultSet.getFloat(10));
				floatData.setAttemptstotal(resultSet.getFloat(11));
				floatData.setOppmadetotal(resultSet.getFloat(12));
				floatData.setOppattemptstotal(resultSet.getFloat(13));
			}
		} catch (SQLException sqle) {
			LOGGER.error(sqle.getMessage(), sqle);
			throw sqle;
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}

		LOGGER.info("Exiting getFootballthirddowneffpergame()");
		return floatData;
	}

	/**
	 * 
	 * @param week
	 * @param year
	 * @param stattype
	 * @param collegename
	 * @return
	 * @throws BatchException
	 * @throws SQLException
	 */
	public Efficiencies getFootballthirddowneffpergameForWeek(Integer week, Integer year, String stattype, String collegename) throws BatchException, SQLException {
		LOGGER.info("Entering getFootballthirddowneffpergameForWeek()");
		final Efficiencies floatData = new Efficiencies();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {	
			final StringBuffer sb = new StringBuffer(2000);
			sb.append("SELECT thirddowneffpergame, oppthirddowneffpergame, thirddowneffpergametotal, oppthirddowneffpergametotal, numgames, " + 
					"made, attempts, oppmade, oppattempts, madetotal, attemptstotal, oppmadetotal, oppattemptstotal from footballthirddowneffpergame where week < ? AND year = ? AND collegename = ? AND stattype = ? ORDER BY week DESC");

			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, week);
			stmt.setInt(2, year);
			stmt.setString(3, collegename);
			stmt.setString(4, stattype);

			resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				floatData.setFloatdata(resultSet.getFloat(1));
				floatData.setOppfloatdata(resultSet.getFloat(2));
				floatData.setFloatdatatotal(resultSet.getFloat(3));
				floatData.setOppfloatdatatotal(resultSet.getFloat(4));
				floatData.setNumgames(resultSet.getInt(5));
				floatData.setMade(resultSet.getFloat(6));
				floatData.setAttempts(resultSet.getFloat(7));
				floatData.setOppmade(resultSet.getFloat(8));
				floatData.setOppattempts(resultSet.getFloat(9));
				floatData.setMadetotal(resultSet.getFloat(10));
				floatData.setAttemptstotal(resultSet.getFloat(11));
				floatData.setOppmadetotal(resultSet.getFloat(12));
				floatData.setOppattemptstotal(resultSet.getFloat(13));
			}
		} catch (SQLException sqle) {
			LOGGER.error(sqle.getMessage(), sqle);
			throw sqle;
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}

		LOGGER.info("Exiting getFootballthirddowneffpergameForWeek()");
		return floatData;
	}

	/**
	 * 
	 * @param week
	 * @param year
	 * @param stattype
	 * @param collegename
	 * @param totalyardspergame
	 * @param opptotalyardspergame
	 * @param totalyardspergametotal
	 * @param opptotalyardspergametotal
	 * @param numgames
	 * @throws SQLException
	 */
	public void persistFootballtotalyardspergame(Integer week, 
			Integer year, 
			String stattype, 
			String collegename, 
			Float totalyardspergame, 
			Float opptotalyardspergame, 
			Float totalyardspergametotal, 
			Float opptotalyardspergametotal, 
			Integer numgames) throws SQLException {
		LOGGER.info("Entering persistFootballtotalyardspergame()");
		PreparedStatement stmt = null;

		try {	
			final StringBuffer sb = new StringBuffer(2000);
			sb.append("INSERT into footballtotalyardspergame (");
			sb.append("week, ");
			sb.append("year, ");
			sb.append("stattype, ");
			sb.append("collegename, ");
			sb.append("totalyardspergame, ");
			sb.append("opptotalyardspergame, ");
			sb.append("totalyardspergametotal, ");
			sb.append("opptotalyardspergametotal, ");
			sb.append("numgames) ");
			sb.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, week);
			stmt.setInt(2, year);
			stmt.setString(3, stattype);
			stmt.setString(4, collegename);
			stmt.setFloat(5, totalyardspergame);
			stmt.setFloat(6, opptotalyardspergame);
			stmt.setFloat(7, totalyardspergametotal);
			stmt.setFloat(8, opptotalyardspergametotal);
			stmt.setInt(9, numgames);

			stmt.executeUpdate();
		} catch (SQLException sqle) {
			LOGGER.error(sqle.getMessage(), sqle);
			// throw sqle;
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}

		LOGGER.info("Exiting persistFootballtotalyardspergame()");
	}

	/**
	 * 
	 * @param week
	 * @param year
	 * @param stattype
	 * @param collegename
	 * @return
	 * @throws BatchException
	 * @throws SQLException
	 */
	public FloatData getFootballtotalyardspergame(Integer week, Integer year, String stattype, String collegename) throws BatchException, SQLException {
		LOGGER.info("Entering getFootballtotalyardspergame()");
		final FloatData floatData = new FloatData();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {	
			final StringBuffer sb = new StringBuffer(2000);
			sb.append("SELECT totalyardspergame, opptotalyardspergame, totalyardspergametotal, opptotalyardspergametotal, numgames from footballtotalyardspergame where year = ? AND collegename = ? AND stattype = ? ORDER BY week DESC");

			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, year);
			stmt.setString(2, collegename);
			stmt.setString(3, stattype);

			resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				floatData.setFloatdata(resultSet.getFloat(1));
				floatData.setOppfloatdata(resultSet.getFloat(2));
				floatData.setFloatdatatotal(resultSet.getFloat(3));
				floatData.setOppfloatdatatotal(resultSet.getFloat(4));
				floatData.setNumgames(resultSet.getInt(5));
			}
		} catch (SQLException sqle) {
			LOGGER.error(sqle.getMessage(), sqle);
			throw sqle;
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}

		LOGGER.info("Exiting getFootballtotalyardspergame()");
		return floatData;
	}

	/**
	 * 
	 * @param week
	 * @param year
	 * @param stattype
	 * @param collegename
	 * @return
	 * @throws BatchException
	 * @throws SQLException
	 */
	public FloatData getFootballtotalyardspergameForWeek(Integer week, Integer year, String stattype, String collegename) throws BatchException, SQLException {
		LOGGER.info("Entering getFootballtotalyardspergameForWeek()");
		final FloatData floatData = new FloatData();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {	
			final StringBuffer sb = new StringBuffer(2000);
			sb.append("SELECT totalyardspergame, opptotalyardspergame, totalyardspergametotal, opptotalyardspergametotal, numgames from footballtotalyardspergame where week < ? AND year = ? AND collegename = ? AND stattype = ? ORDER BY week DESC");

			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, week);
			stmt.setInt(2, year);
			stmt.setString(3, collegename);
			stmt.setString(4, stattype);

			resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				floatData.setFloatdata(resultSet.getFloat(1));
				floatData.setOppfloatdata(resultSet.getFloat(2));
				floatData.setFloatdatatotal(resultSet.getFloat(3));
				floatData.setOppfloatdatatotal(resultSet.getFloat(4));
				floatData.setNumgames(resultSet.getInt(5));
			}
		} catch (SQLException sqle) {
			LOGGER.error(sqle.getMessage(), sqle);
			throw sqle;
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}

		LOGGER.info("Exiting getFootballtotalyardspergameForWeek()");
		return floatData;
	}

	/**
	 * 
	 * @param week
	 * @param year
	 * @param stattype
	 * @param collegename
	 * @param turnoverspergame
	 * @param oppturnoverspergame
	 * @param turnoverspergametotal
	 * @param oppturnoverspergametotal
	 * @param numgames
	 * @throws SQLException
	 */
	public void persistFootballturnoverspergame(Integer week, 
			Integer year, 
			String stattype, 
			String collegename, 
			Float turnoverspergame, 
			Float oppturnoverspergame, 
			Float turnoverspergametotal, 
			Float oppturnoverspergametotal, 
			Integer numgames) throws SQLException {
		LOGGER.info("Entering persistFootballturnoverspergame()");
		PreparedStatement stmt = null;

		try {	
			final StringBuffer sb = new StringBuffer(2000);
			sb.append("INSERT into footballturnoverspergame (");
			sb.append("week, ");
			sb.append("year, ");
			sb.append("stattype, ");
			sb.append("collegename, ");
			sb.append("turnoverspergame, ");
			sb.append("oppturnoverspergame, ");
			sb.append("turnoverspergametotal, ");
			sb.append("oppturnoverspergametotal, ");
			sb.append("numgames) ");
			sb.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, week);
			stmt.setInt(2, year);
			stmt.setString(3, stattype);
			stmt.setString(4, collegename);
			stmt.setFloat(5, turnoverspergame);
			stmt.setFloat(6, oppturnoverspergame);
			stmt.setFloat(7, turnoverspergametotal);
			stmt.setFloat(8, oppturnoverspergametotal);
			stmt.setInt(9, year);
			
			stmt.executeUpdate();
		} catch (SQLException sqle) {
			LOGGER.error(sqle.getMessage(), sqle);
			// throw sqle;
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}

		LOGGER.info("Exiting persistFootballturnoverspergame()");
	}

	/**
	 * 
	 * @param year
	 * @param stattype
	 * @param collegename
	 * @return
	 * @throws BatchException
	 * @throws SQLException
	 */
	public FloatData getFootballturnoverspergame(Integer year, String stattype, String collegename) throws BatchException, SQLException {
		LOGGER.info("Entering getFootballturnoverspergame()");
		final FloatData floatData = new FloatData();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {	
			final StringBuffer sb = new StringBuffer(2000);
			sb.append("SELECT turnoverspergame, oppturnoverspergame, turnoverspergametotal, oppturnoverspergametotal, numgames from footballturnoverspergame where year = ? AND collegename = ? AND stattype = ? ORDER BY week DESC");

			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, year);
			stmt.setString(2, collegename);
			stmt.setString(3, stattype);

			resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				floatData.setFloatdata(resultSet.getFloat(1));
				floatData.setOppfloatdata(resultSet.getFloat(2));
				floatData.setFloatdatatotal(resultSet.getFloat(3));
				floatData.setOppfloatdatatotal(resultSet.getFloat(4));
				floatData.setNumgames(resultSet.getInt(5));
			}
		} catch (SQLException sqle) {
			LOGGER.error(sqle.getMessage(), sqle);
			throw sqle;
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}

		LOGGER.info("Exiting getFootballturnoverspergame()");
		return floatData;
	}

	/**
	 * 
	 * @param week
	 * @param year
	 * @param stattype
	 * @param collegename
	 * @return
	 * @throws BatchException
	 * @throws SQLException
	 */
	public FloatData getFootballturnoverspergameForWeek(Integer week, Integer year, String stattype, String collegename) throws BatchException, SQLException {
		LOGGER.info("Entering getFootballturnoverspergameForWeek()");
		final FloatData floatData = new FloatData();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {	
			final StringBuffer sb = new StringBuffer(2000);
			sb.append("SELECT turnoverspergame, oppturnoverspergame, turnoverspergametotal, oppturnoverspergametotal, numgames from footballturnoverspergame where week < ? AND year = ? AND collegename = ? AND stattype = ? ORDER BY week DESC");

			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, week);
			stmt.setInt(2, year);
			stmt.setString(3, collegename);
			stmt.setString(4, stattype);

			resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				floatData.setFloatdata(resultSet.getFloat(1));
				floatData.setOppfloatdata(resultSet.getFloat(2));
				floatData.setFloatdatatotal(resultSet.getFloat(3));
				floatData.setOppfloatdatatotal(resultSet.getFloat(4));
				floatData.setNumgames(resultSet.getInt(5));
			}
		} catch (SQLException sqle) {
			LOGGER.error(sqle.getMessage(), sqle);
			throw sqle;
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}

		LOGGER.info("Exiting getFootballturnoverspergameForWeek()");
		return floatData;
	}

	/**
	 * 
	 * @param week
	 * @param year
	 * @param stattype
	 * @param collegename
	 * @param yardsperpass
	 * @param oppyardsperpass
	 * @param yardsperpasstotal
	 * @param oppyardsperpasstotal
	 * @param numgames
	 * @throws SQLException
	 */
	public void persistFootballyardsperpass(Integer week, 
			Integer year, 
			String stattype, 
			String collegename, 
			Float yardsperpass, 
			Float oppyardsperpass, 
			Float yardsperpasstotal, 
			Float oppyardsperpasstotal, 
			Integer numgames) throws SQLException {
		LOGGER.info("Entering persistFootballyardsperpass()");
		PreparedStatement stmt = null;

		try {	
			final StringBuffer sb = new StringBuffer(2000);
			sb.append("INSERT into footballyardsperpass (");
			sb.append("week, ");
			sb.append("year, ");
			sb.append("stattype, ");
			sb.append("collegename, ");
			sb.append("yardsperpass, ");
			sb.append("oppyardsperpass, ");
			sb.append("yardsperpasstotal, ");
			sb.append("oppyardsperpasstotal, ");
			sb.append("numgames) ");
			sb.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, week);
			stmt.setInt(2, year);
			stmt.setString(3, stattype);
			stmt.setString(4, collegename);
			stmt.setFloat(5, yardsperpass);
			stmt.setFloat(6, oppyardsperpass);
			stmt.setFloat(7, yardsperpasstotal);
			stmt.setFloat(8, oppyardsperpasstotal);
			stmt.setInt(9, numgames);

			stmt.executeUpdate();
		} catch (SQLException sqle) {
			LOGGER.error(sqle.getMessage(), sqle);
			// throw sqle;
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}

		LOGGER.info("Exiting persistFootballyardsperpass()");
	}

	/**
	 * 
	 * @param year
	 * @param stattype
	 * @param collegename
	 * @return
	 * @throws BatchException
	 * @throws SQLException
	 */
	public FloatData getFootballyardsperpass(Integer year, String stattype, String collegename) throws BatchException, SQLException {
		LOGGER.info("Entering getFootballyardsperpass()");
		final FloatData floatData = new FloatData();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {	
			final StringBuffer sb = new StringBuffer(2000);
			sb.append("SELECT yardsperpass, oppyardsperpass, yardsperpasstotal, oppyardsperpasstotal, numgames from footballyardsperpass where year = ? AND collegename = ? AND stattype = ? ORDER BY week DESC");

			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, year);
			stmt.setString(2, collegename);
			stmt.setString(3, stattype);

			resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				floatData.setFloatdata(resultSet.getFloat(1));
				floatData.setOppfloatdata(resultSet.getFloat(2));
				floatData.setFloatdatatotal(resultSet.getFloat(3));
				floatData.setOppfloatdatatotal(resultSet.getFloat(4));
				floatData.setNumgames(resultSet.getInt(5));
			}
		} catch (SQLException sqle) {
			LOGGER.error(sqle.getMessage(), sqle);
			throw sqle;
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}

		LOGGER.info("Exiting getFootballyardsperpass()");
		return floatData;
	}

	/**
	 * 
	 * @param week
	 * @param year
	 * @param stattype
	 * @param collegename
	 * @return
	 * @throws BatchException
	 * @throws SQLException
	 */
	public FloatData getFootballyardsperpassForWeek(Integer week, Integer year, String stattype, String collegename) throws BatchException, SQLException {
		LOGGER.info("Entering getFootballyardsperpassForWeek()");
		final FloatData floatData = new FloatData();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {	
			final StringBuffer sb = new StringBuffer(2000);
			sb.append("SELECT yardsperpass, oppyardsperpass, yardsperpasstotal, oppyardsperpasstotal, numgames from footballyardsperpass where week < ? AND year = ? AND collegename = ? AND stattype = ? ORDER BY week DESC");

			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, week);
			stmt.setInt(2, year);
			stmt.setString(3, collegename);
			stmt.setString(4, stattype);

			resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				floatData.setFloatdata(resultSet.getFloat(1));
				floatData.setOppfloatdata(resultSet.getFloat(2));
				floatData.setFloatdatatotal(resultSet.getFloat(3));
				floatData.setOppfloatdatatotal(resultSet.getFloat(4));
				floatData.setNumgames(resultSet.getInt(5));
			}
		} catch (SQLException sqle) {
			LOGGER.error(sqle.getMessage(), sqle);
			throw sqle;
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}

		LOGGER.info("Exiting getFootballyardsperpassForWeek()");
		return floatData;
	}

	/**
	 * 
	 * @param week
	 * @param year
	 * @param stattype
	 * @param collegename
	 * @param yardsperrush
	 * @param oppyardsperrush
	 * @param yardsperrushtotal
	 * @param oppyardsperrushtotal
	 * @param numgames
	 * @throws SQLException
	 */
	public void persistFootballyardsperrush(Integer week, 
			Integer year, 
			String stattype, 
			String collegename, 
			Float yardsperrush, 
			Float oppyardsperrush, 
			Float yardsperrushtotal, 
			Float oppyardsperrushtotal, 
			Integer numgames) throws SQLException {
		LOGGER.info("Entering persistFootballyardsperrush()");
		PreparedStatement stmt = null;

		try {	
			final StringBuffer sb = new StringBuffer(2000);
			sb.append("INSERT into footballyardsperrush (");
			sb.append("week, ");
			sb.append("year, ");
			sb.append("stattype, ");
			sb.append("collegename, ");
			sb.append("yardsperrush, ");
			sb.append("oppyardsperrush, ");
			sb.append("yardsperrushtotal, ");
			sb.append("oppyardsperrushtotal, ");
			sb.append("numgames) ");
			sb.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, week);
			stmt.setInt(2, year);
			stmt.setString(3, stattype);
			stmt.setString(4, collegename);
			stmt.setFloat(5, yardsperrush);
			stmt.setFloat(6, oppyardsperrush);
			stmt.setFloat(7, yardsperrushtotal);
			stmt.setFloat(8, oppyardsperrushtotal);
			stmt.setInt(9, numgames);

			stmt.executeUpdate();
		} catch (SQLException sqle) {
			LOGGER.error(sqle.getMessage(), sqle);
			// throw sqle;
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}

		LOGGER.info("Exiting persistFootballyardsperrush()");
	}

	/**
	 * 
	 * @param year
	 * @param stattype
	 * @param collegename
	 * @return
	 * @throws BatchException
	 * @throws SQLException
	 */
	public FloatData getFootballyardsperrush(Integer year, String stattype, String collegename) throws BatchException, SQLException {
		LOGGER.info("Entering getFootballyardsperrush()");
		final FloatData floatData = new FloatData();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {	
			final StringBuffer sb = new StringBuffer(2000);
			sb.append("SELECT yardsperrush, oppyardsperrush, yardsperrushtotal, oppyardsperrushtotal, numgames from footballyardsperrush where year = ? AND collegename = ? AND stattype = ? ORDER BY week DESC");

			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, year);
			stmt.setString(2, collegename);
			stmt.setString(3, stattype);

			resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				floatData.setFloatdata(resultSet.getFloat(1));
				floatData.setOppfloatdata(resultSet.getFloat(2));
				floatData.setFloatdatatotal(resultSet.getFloat(3));
				floatData.setOppfloatdatatotal(resultSet.getFloat(4));
				floatData.setNumgames(resultSet.getInt(5));
			}
		} catch (SQLException sqle) {
			LOGGER.error(sqle.getMessage(), sqle);
			throw sqle;
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}

		LOGGER.info("Exiting getFootballyardsperrush()");
		return floatData;
	}

	/**
	 * 
	 * @param week
	 * @param year
	 * @param stattype
	 * @param collegename
	 * @return
	 * @throws BatchException
	 * @throws SQLException
	 */
	public FloatData getFootballyardsperrushForWeek(Integer week, Integer year, String stattype, String collegename) throws BatchException, SQLException {
		LOGGER.info("Entering getFootballyardsperrushForWeek()");
		final FloatData floatData = new FloatData();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {	
			final StringBuffer sb = new StringBuffer(2000);
			sb.append("SELECT yardsperrush, oppyardsperrush, yardsperrushtotal, oppyardsperrushtotal, numgames from footballyardsperrush where week < ? AND year = ? AND collegename = ? AND stattype = ? ORDER BY week DESC");

			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, week);
			stmt.setInt(2, year);
			stmt.setString(3, collegename); 
			stmt.setString(4, stattype);

			resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				floatData.setFloatdata(resultSet.getFloat(1));
				floatData.setOppfloatdata(resultSet.getFloat(2));
				floatData.setFloatdatatotal(resultSet.getFloat(3));
				floatData.setOppfloatdatatotal(resultSet.getFloat(4));
				floatData.setNumgames(resultSet.getInt(5));
			}
		} catch (SQLException sqle) {
			LOGGER.error(sqle.getMessage(), sqle);
			throw sqle;
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}

		LOGGER.info("Exiting getFootballyardsperrushForWeek()");
		return floatData;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param fromdate
	 * @param todate
	 * @return
	 */
	public List<XandYObject> plotXandYDelta(String x, String y, Date fromdate, Date todate) throws SQLException {
		LOGGER.info("Entering plotXandYDelta()");
		final List<XandYObject> xyObjects = new ArrayList<XandYObject>();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		boolean isxfloat = false;
		boolean isyfloat = false;
		String awayx = "";
		String homex = "";
		String awayy = "";
		String homey = "";

		try {
			if (x.contains("percentage")) {
				isxfloat = true;
			}
			if (y.contains("percentage")) {
				isyfloat = true;
			}

			awayx = "away" + x;
			homex = "home" + x;
			awayy = "away" + y;
			homey = "home" + y;

			final StringBuffer sb = new StringBuffer(200);
			sb.append("SELECT week, year, linefavorite, line, awaycollegename, homecollegename, awayshortname, homeshortname, awayfinalscore, homefinalscore, ");
			sb.append(awayx).append(", ");
			sb.append(homex).append(", ");
			sb.append(awayy).append(", ");
			sb.append(homey);
			// 2018-01-01
			sb.append(" FROM espnbasketballgame WHERE date BETWEEN ? AND ?");
		
			stmt = conn.prepareStatement(sb.toString());
			stmt.setTimestamp(1, new java.sql.Timestamp(fromdate.getTime()));
			stmt.setTimestamp(2, new java.sql.Timestamp(todate.getTime()));
			resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				final XandYObject game = new XandYObject();
				game.setWeek(resultSet.getInt("week"));
				game.setYear(resultSet.getInt("year"));
				game.setLinefavorite(resultSet.getString("linefavorite"));
				game.setLine(resultSet.getFloat("line"));
				game.setGame(resultSet.getString("awaycollegename") + " - " + resultSet.getString("homecollegename"));
				
				final Integer awayscore = resultSet.getInt("awayfinalscore");
				final Integer homescore = resultSet.getInt("homefinalscore");
				game.setAwayscore(awayscore);
				game.setHomescore(homescore);
				int scoredelta = homescore - awayscore;
				final String awayshortname = resultSet.getString("awayshortname");
//				final String homeshortname = resultSet.getString("homeshortname");
				float line = Math.abs(game.getLine());
				game.setLine(line);

				if (game.getLinefavorite().equals(awayshortname)) {
					scoredelta = awayscore - homescore;
				} else {
					scoredelta = homescore - awayscore;
				}

				if (isxfloat) {
					final Float awayfloatx = resultSet.getFloat(awayx);
					final Float homefloatx = resultSet.getFloat(homex);

					if (game.getLinefavorite().equals(awayshortname)) {
						game.setScoredelta(scoredelta);
						game.setX(Float.toString(awayfloatx - homefloatx));
					} else {
						game.setScoredelta(scoredelta);
						game.setX(Float.toString(homefloatx - awayfloatx));
					}
				} else {
					final Integer awayfloatx = resultSet.getInt(awayx);
					final Integer homefloatx = resultSet.getInt(homex);

					if (game.getLinefavorite().equals(awayshortname)) {
						game.setScoredelta(scoredelta);
						game.setX(Integer.toString(awayfloatx - homefloatx));
					} else {
						game.setScoredelta(scoredelta);
						game.setX(Integer.toString(homefloatx - awayfloatx));
					}					
				}

				if (isyfloat) {
					final Float awayfloaty = resultSet.getFloat(awayy);
					final Float homefloaty = resultSet.getFloat(homey);

					if (game.getLinefavorite().equals(awayshortname)) {
						game.setScoredelta(scoredelta);
						game.setY(Float.toString(awayfloaty - homefloaty));
					} else {
						game.setScoredelta(scoredelta);
						game.setY(Float.toString(homefloaty - awayfloaty));
					}
				} else {
					final Integer awayfloaty = resultSet.getInt(awayy);
					final Integer homefloaty = resultSet.getInt(homey);

					if (game.getLinefavorite().equals(awayshortname)) {
						game.setScoredelta(scoredelta);
						game.setY(Integer.toString(awayfloaty - homefloaty));
					} else {
						game.setScoredelta(scoredelta);
						game.setY(Integer.toString(homefloaty - awayfloaty));
					}					
				}

				LOGGER.debug("game: " + game);
				xyObjects.add(game);
			}
		} catch (SQLException sqle) {
			LOGGER.error(sqle);
			throw sqle;
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (resultSet != null) {
				resultSet.close();
			}
		}

		LOGGER.info("Exiting plotXandYDelta()");
		return xyObjects;
	}

	/**
	 * 
	 * @param resultSet
	 * @return
	 * @throws SQLException
	 */
	private EspnCollegeFootballGameData populateData(ResultSet resultSet) throws SQLException {
		final EspnCollegeFootballGameData game = new EspnCollegeFootballGameData();

		game.setEspngameid(resultSet.getInt(1));
		game.setWeek(resultSet.getInt(2));
		game.setHour(resultSet.getInt(3));
		game.setMinute(resultSet.getInt(4));
		game.setAmpm(resultSet.getString(5));
		game.setTimezone(resultSet.getString(6));
		game.setMonth(resultSet.getInt(7));
		game.setDay(resultSet.getInt(8));
		game.setYear(resultSet.getInt(9));
		game.setGamedate(resultSet.getTimestamp(10));
		game.setTv(resultSet.getString(11));
		game.setCity(resultSet.getString(12));
		game.setState(resultSet.getString(13));
		game.setZipcode(resultSet.getInt(14));
		game.setEventlocation(resultSet.getString(15));
		game.setIsconferencegame(resultSet.getBoolean(16));
		game.setNeutralsite(resultSet.getBoolean(17));
		game.setLinefavorite(resultSet.getString(18));
		game.setLineindicator(resultSet.getString(19));
		game.setLinevalue(resultSet.getFloat(20));
		game.setLine(resultSet.getFloat(21));
		game.setTotal(resultSet.getFloat(22));
		game.setAttendance(resultSet.getInt(23));
		game.setAwaywin(resultSet.getBoolean(24));
		game.setHomewin(resultSet.getBoolean(25));
		game.setAwaycollegename(resultSet.getString(26));
		game.setHomecollegename(resultSet.getString(27));
		game.setAwayteam(game.getAwaycollegename());
		game.setHometeam(game.getHomecollegename());
		game.setAwaymascotname(resultSet.getString(28));
		game.setHomemascotname(resultSet.getString(29));
		game.setAwayshortname(resultSet.getString(30));
		game.setHomeshortname(resultSet.getString(31));
		game.setAwayranking(resultSet.getInt(32));
		game.setHomeranking(resultSet.getInt(33));
		game.setAwaywins(resultSet.getInt(34));
		game.setHomewins(resultSet.getInt(35));
		game.setAwaylosses(resultSet.getInt(36));
		game.setHomelosses(resultSet.getInt(37));
		game.setAwayfirstquarterscore(resultSet.getInt(38));
		game.setHomefirstquarterscore(resultSet.getInt(39));
		game.setAwaysecondquarterscore(resultSet.getInt(40));
		game.setHomesecondquarterscore(resultSet.getInt(41));
		game.setAwayfirsthalfscore(resultSet.getInt(42));
		game.setHomefirsthalfscore(resultSet.getInt(43));
		game.setAwaythirdquarterscore(resultSet.getInt(44));
		game.setHomethirdquarterscore(resultSet.getInt(45));
		game.setAwayfourthquarterscore(resultSet.getInt(46));
		game.setHomefourthquarterscore(resultSet.getInt(47));
		game.setAwaysecondhalfscore(resultSet.getInt(48));
		game.setHomesecondhalfscore(resultSet.getInt(49));
		game.setAwayotonescore(resultSet.getInt(50));
		game.setHomeotonescore(resultSet.getInt(51));
		game.setAwayottwoscore(resultSet.getInt(52));
		game.setHomeottwoscore(resultSet.getInt(53));
		game.setAwayotthreescore(resultSet.getInt(54));
		game.setHomeotthreescore(resultSet.getInt(55));
		game.setAwayotfourscore(resultSet.getInt(56));
		game.setHomeotfourscore(resultSet.getInt(57));
		game.setAwayotfivescore(resultSet.getInt(58));
		game.setHomeotfivescore(resultSet.getInt(59));
		game.setAwayotsixscore(resultSet.getInt(60));
		game.setHomeotsixscore(resultSet.getInt(61));
		game.setAwaysecondhalfotscore(resultSet.getInt(62));
		game.setHomesecondhalfotscore(resultSet.getInt(63));
		game.setAwayfinalscore(resultSet.getInt(64));
		game.setHomefinalscore(resultSet.getInt(65));
		game.setAwayfirstdowns(resultSet.getInt(66));
		game.setHomefirstdowns(resultSet.getInt(67));
		game.setAwaythirdefficiencymade(resultSet.getInt(68));
		game.setHomethirdefficiencymade(resultSet.getInt(69));
		game.setAwaythirdefficiencyattempts(resultSet.getInt(70));
		game.setHomethirdefficiencyattempts(resultSet.getInt(71));
		game.setAwayfourthefficiencymade(resultSet.getInt(72));
		game.setHomefourthefficiencymade(resultSet.getInt(73));
		game.setAwayfourthefficiencyattempts(resultSet.getInt(74));
		game.setHomefourthefficiencyattempts(resultSet.getInt(75));
		game.setAwaytotalyards(resultSet.getInt(76));
		game.setHometotalyards(resultSet.getInt(77));
		game.setAwaypassingyards(resultSet.getInt(78));
		game.setHomepassingyards(resultSet.getInt(79));
		game.setAwaypasscomp(resultSet.getInt(80));
		game.setHomepasscomp(resultSet.getInt(81));
		game.setAwaypassattempts(resultSet.getInt(82));
		game.setHomepassattempts(resultSet.getInt(83));
		game.setAwayyardsperpass(resultSet.getFloat(84));
		game.setHomeyardsperpass(resultSet.getFloat(85));
		game.setAwayrushingyards(resultSet.getInt(86));
		game.setHomerushingyards(resultSet.getInt(87));
		game.setAwayrushingattempts(resultSet.getInt(88));
		game.setHomerushingattempts(resultSet.getInt(89));
		game.setAwayyardsperrush(resultSet.getFloat(90));
		game.setHomeyardsperrush(resultSet.getFloat(91));
		game.setAwaypenalties(resultSet.getInt(92));
		game.setHomepenalties(resultSet.getInt(93));
		game.setAwaypenaltyyards(resultSet.getInt(94));
		game.setHomepenaltyyards(resultSet.getInt(95));
		game.setAwayturnovers(resultSet.getInt(96));
		game.setHometurnovers(resultSet.getInt(97));
		game.setAwayfumbleslost(resultSet.getInt(98));
		game.setHomefumbleslost(resultSet.getInt(99));
		game.setAwayinterceptions(resultSet.getInt(100));
		game.setHomeinterceptions(resultSet.getInt(101));
		game.setAwaypossessionminutes(resultSet.getInt(102));
		game.setHomepossessionminutes(resultSet.getInt(103));
		game.setAwaypossessionseconds(resultSet.getInt(104));
		game.setHomepossessionseconds(resultSet.getInt(105));
		game.setAwaysagrinrating(resultSet.getFloat(106));
		game.setHomesagrinrating(resultSet.getFloat(107));
		game.setAwaymasseyrating(resultSet.getFloat(108));
		game.setHomemasseyrating(resultSet.getFloat(109));
		game.setAwaysos(resultSet.getFloat(110));
		game.setHomesos(resultSet.getFloat(111));
		game.setAwayisfbs(resultSet.getBoolean(112));
		game.setHomeisfbs(resultSet.getBoolean(113));
		game.setAwaymasseyranking(resultSet.getInt(116));
		game.setHomemasseyranking(resultSet.getInt(117));
		game.setAwaymasseyranking(resultSet.getInt(116));
		game.setHomemasseyranking(resultSet.getInt(117));

		return game;
	}
}