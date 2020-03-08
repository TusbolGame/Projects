/**
 * 
 */
package com.wootechnologies.dataminer.db.ncaab;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.wootechnologies.dataminer.db.DataMinerDB;
import com.wootechnologies.dataminer.model.XandYObject;
import com.wootechnologies.services.dao.sites.espn.EspnCollegeBasketballGameData;

/**
 * @author jmiller
 *
 */
public class NcaabDataMinerDB extends DataMinerDB {
	private static final Logger LOGGER = Logger.getLogger(NcaabDataMinerDB.class);

	/**
	 * 
	 */
	public NcaabDataMinerDB() {
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
	public void persistEspnNcaabGameData(EspnCollegeBasketballGameData gameData) throws SQLException {
		LOGGER.info("Entering persistEspnNcaabGameData()");
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {	
			final StringBuffer sb = new StringBuffer(2000);
			sb.append("INSERT into espnbasketballgame (");
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
			sb.append("awaysecondhalfotscore, ");
			sb.append("homesecondhalfotscore, ");
			sb.append("awayfinalscore, ");
			sb.append("homefinalscore, ");
			sb.append("awayfgmade, ");
			sb.append("homefgmade, ");
			sb.append("awayfgattempt, ");
			sb.append("homefgattempt, ");
			sb.append("awayfgpercentage, ");
			sb.append("homefgpercentage, ");
			sb.append("away3ptfgmade, ");
			sb.append("home3ptfgmade, ");
			sb.append("away3ptfgattempt, ");
			sb.append("home3ptfgattempt, ");
			sb.append("away3ptfgpercentage, ");
			sb.append("home3ptfgpercentage, ");
			sb.append("awayftmade, ");
			sb.append("homeftmade, ");
			sb.append("awayftattempt, ");
			sb.append("homeftattempt, ");
			sb.append("awayftpercentage, ");
			sb.append("homeftpercentage, ");
			sb.append("awaytotalrebounds, ");
			sb.append("hometotalrebounds, ");
			sb.append("awayoffrebounds, ");
			sb.append("homeoffrebounds, ");
			sb.append("awaydefrebounds, ");
			sb.append("homedefrebounds, ");
			sb.append("awayassists, ");
			sb.append("homeassists, ");
			sb.append("awaysteals, ");
			sb.append("homesteals, ");
			sb.append("awayblocks, ");
			sb.append("homeblocks, ");
			sb.append("awaytotalturnovers, ");
			sb.append("hometotalturnovers, ");
			sb.append("awaypersonalfouls, ");
			sb.append("homepersonalfouls, ");
			sb.append("awaytechnicalfouls, ");
			sb.append("hometechnicalfouls, ");
			sb.append("awaysagrinrating, ");
			sb.append("homesagrinrating, ");
			sb.append("awaymasseyrating, ");
			sb.append("homemasseyrating, ");
			sb.append("awaysos, ");
			sb.append("homesos, ");
			sb.append("ref1, ");
			sb.append("ref2, ");
			sb.append("ref3, ");
			sb.append("seasonyear, ");
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
			sb.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

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
			stmt.setInt(60, gameData.getAwaysecondhalfotscore());
			stmt.setInt(61, gameData.getHomesecondhalfotscore());
			stmt.setInt(62, gameData.getAwayfinalscore());
			stmt.setInt(63, gameData.getHomefinalscore());
			stmt.setInt(64, gameData.getAwayfgmade());
			stmt.setInt(65, gameData.getHomefgmade());
			stmt.setInt(66, gameData.getAwayfgattempt());
			stmt.setInt(67, gameData.getHomefgattempt());
			stmt.setFloat(68, gameData.getAwayfgpercentage());
			stmt.setFloat(69, gameData.getHomefgpercentage());
			stmt.setInt(70, gameData.getAway3ptfgmade());
			stmt.setInt(71, gameData.getHome3ptfgmade());
			stmt.setInt(72, gameData.getAway3ptfgattempt());
			stmt.setInt(73, gameData.getHome3ptfgattempt());
			stmt.setFloat(74, gameData.getAway3ptfgpercentage());
			stmt.setFloat(75, gameData.getHome3ptfgpercentage());
			stmt.setInt(76, gameData.getAwayftmade());
			stmt.setInt(77, gameData.getHomeftmade());
			stmt.setInt(78, gameData.getAwayftattempt());
			stmt.setInt(79, gameData.getHomeftattempt());
			stmt.setFloat(80, gameData.getAwayftpercentage());
			stmt.setFloat(81, gameData.getHomeftpercentage());
			stmt.setInt(82, gameData.getAwaytotalrebounds());
			stmt.setInt(83, gameData.getHometotalrebounds());
			stmt.setInt(84, gameData.getAwayoffrebounds());
			stmt.setInt(85, gameData.getHomeoffrebounds());
			stmt.setInt(86, gameData.getAwaydefrebounds());
			stmt.setInt(87, gameData.getHomedefrebounds());
			stmt.setInt(88, gameData.getAwayassists());
			stmt.setInt(89, gameData.getHomeassists());
			stmt.setInt(90, gameData.getAwaysteals());
			stmt.setInt(91, gameData.getHomesteals());
			stmt.setInt(92, gameData.getAwayblocks());
			stmt.setInt(93, gameData.getHomeblocks());
			stmt.setInt(94, gameData.getAwaytotalturnovers());
			stmt.setInt(95, gameData.getHometotalturnovers());
			stmt.setInt(96, gameData.getAwaypersonalfouls());
			stmt.setInt(97, gameData.getHomepersonalfouls());
			stmt.setInt(98, gameData.getAwaytechnicalfouls());
			stmt.setInt(99, gameData.getHometechnicalfouls());
			stmt.setFloat(100, gameData.getAwaysagrinrating());
			stmt.setFloat(101, gameData.getHomesagrinrating());
			stmt.setFloat(102, gameData.getAwaymasseyrating());
			stmt.setFloat(103, gameData.getHomemasseyrating());
			stmt.setFloat(104, gameData.getAwaysos());
			stmt.setFloat(105, gameData.getHomesos());
			stmt.setString(106, gameData.getRef1());
			stmt.setString(107, gameData.getRef2());
			stmt.setString(108, gameData.getRef3());
			stmt.setInt(109, gameData.getSeasonyear());
			stmt.setTimestamp(110, new java.sql.Timestamp(gameDate.getTime()));
			stmt.setTimestamp(111, new java.sql.Timestamp(gameDate.getTime()));

			stmt.executeUpdate();
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

		LOGGER.info("Exiting persistEspnNcaabGameData()");
	}

	/**
	 * 
	 * @param year
	 * @return
	 * @throws SQLException
	 */
	public List<EspnCollegeBasketballGameData> getEspnNcaabGameData(Date start, Date end) throws SQLException {
		LOGGER.info("Entering getEspnNcaabGameData()");
		final List<EspnCollegeBasketballGameData> games = new ArrayList<EspnCollegeBasketballGameData>();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {
			final Calendar startd = Calendar.getInstance();
			startd.setTime(start);
			final Calendar endd = Calendar.getInstance();
			endd.setTime(end);

			stmt = conn.prepareStatement("SELECT "
							+ "espngameid," + 
							"  week," + 
							"  hour," + 
							"  minute," + 
							"  ampm," + 
							"  timezone," + 
							"  month," + 
							"  day," + 
							"  year," + 
							"  gamedate," + 
							"  tv," + 
							"  city," + 
							"  state," + 
							"  zipcode," + 
							"  eventlocation," + 
							"  isconferencegame," + 
							"  neutralsite," + 
							"  linefavorite," + 
							"  lineindicator," + 
							"  linevalue," + 
							"  line," + 
							"  total," + 
							"  attendance," + 
							"  awaywin," + 
							"  homewin," + 
							"  awaycollegename," + 
							"  homecollegename," + 
							"  awaymascotname," + 
							"  homemascotname," + 
							"  awayshortname," + 
							"  homeshortname," + 
							"  awayranking," + 
							"  homeranking," + 
							"  awaywins," + 
							"  homewins," + 
							"  awaylosses," + 
							"  homelosses," + 
							"  awayfirstquarterscore," + 
							"  homefirstquarterscore," + 
							"  awaysecondquarterscore," + 
							"  homesecondquarterscore," + 
							"  awayfirsthalfscore," + 
							"  homefirsthalfscore," + 
							"  awaythirdquarterscore," + 
							"  homethirdquarterscore," + 
							"  awayfourthquarterscore," + 
							"  homefourthquarterscore," + 
							"  awaysecondhalfscore," + 
							"  homesecondhalfscore," + 
							"  awayotonescore," + 
							"  homeotonescore," + 
							"  awayottwoscore," + 
							"  homeottwoscore," + 
							"  awayotthreescore," + 
							"  homeotthreescore," + 
							"  awayotfourscore," + 
							"  homeotfourscore," + 
							"  awayotfivescore," + 
							"  homeotfivescore," + 
							"  awaysecondhalfotscore," + 
							"  homesecondhalfotscore," + 
							"  awayfinalscore," + 
							"  homefinalscore," + 
							"  awayfgmade," + 
							"  homefgmade," + 
							"  awayfgattempt," + 
							"  homefgattempt," + 
							"  awayfgpercentage," + 
							"  homefgpercentage," + 
							"  away3ptfgmade," + 
							"  home3ptfgmade," + 
							"  away3ptfgattempt," + 
							"  home3ptfgattempt," + 
							"  away3ptfgpercentage," + 
							"  home3ptfgpercentage," + 
							"  awayftmade," + 
							"  homeftmade," + 
							"  awayftattempt," + 
							"  homeftattempt," + 
							"  awayftpercentage," + 
							"  homeftpercentage," + 
							"  awaytotalrebounds," + 
							"  hometotalrebounds," + 
							"  awayoffrebounds," + 
							"  homeoffrebounds," + 
							"  awaydefrebounds," + 
							"  homedefrebounds," + 
							"  awayassists," + 
							"  homeassists," + 
							"  awaysteals," + 
							"  homesteals," + 
							"  awayblocks," + 
							"  homeblocks," + 
							"  awaytotalturnovers," + 
							"  hometotalturnovers," + 
							"  awaypersonalfouls," + 
							"  homepersonalfouls," + 
							"  awaytechnicalfouls," + 
							"  hometechnicalfouls," + 
							"  awaysagrinrating," + 
							"  homesagrinrating," + 
							"  awaymasseyrating," + 
							"  homemasseyrating," + 
							"  awaysos," + 
							"  homesos," + 
							"  ref1," + 
							"  ref2," + 
							"  ref3," + 
							"  seasonyear"
					+ " FROM espnbasketballgame WHERE date between '" + 
					startd.get(Calendar.YEAR) + "-" + (startd.get(Calendar.MONTH) + 1) + "-" + startd.get(Calendar.DAY_OF_MONTH) + "' and '" +
					endd.get(Calendar.YEAR) + "-" + (endd.get(Calendar.MONTH) + 1) + "-" + endd.get(Calendar.DAY_OF_MONTH) + "' ORDER BY date DESC");
			resultSet = stmt.executeQuery();

			while (resultSet.next()) {
				final EspnCollegeBasketballGameData game = new EspnCollegeBasketballGameData();
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
				game.setAwaysecondhalfotscore(resultSet.getInt(60));
				game.setHomesecondhalfotscore(resultSet.getInt(61));
				game.setAwayfinalscore(resultSet.getInt(62));
				game.setHomefinalscore(resultSet.getInt(63));
				game.setAwayfgmade(resultSet.getInt(64));
				game.setHomefgmade(resultSet.getInt(65));
				game.setAwayfgattempt(resultSet.getInt(66));
				game.setHomefgattempt(resultSet.getInt(67));
				game.setAwayfgpercentage(resultSet.getFloat(68));
				game.setHomefgpercentage(resultSet.getFloat(69));
				game.setAway3ptfgmade(resultSet.getInt(70));
				game.setHome3ptfgmade(resultSet.getInt(71));
				game.setAway3ptfgattempt(resultSet.getInt(72));
				game.setHome3ptfgattempt(resultSet.getInt(73));
				game.setAway3ptfgpercentage(resultSet.getFloat(74));
				game.setHome3ptfgpercentage(resultSet.getFloat(75));
				game.setAwayftmade(resultSet.getInt(76));
				game.setHomeftmade(resultSet.getInt(77));
				game.setAwayftattempt(resultSet.getInt(78));
				game.setHomeftattempt(resultSet.getInt(79));
				game.setAwayftpercentage(resultSet.getFloat(80));
				game.setHomeftpercentage(resultSet.getFloat(81));
				game.setAwaytotalrebounds(resultSet.getInt(82));
				game.setHometotalrebounds(resultSet.getInt(83));
				game.setAwayoffrebounds(resultSet.getInt(84));
				game.setHomeoffrebounds(resultSet.getInt(85));
				game.setAwaydefrebounds(resultSet.getInt(86));
				game.setHomedefrebounds(resultSet.getInt(87));
				game.setAwayassists(resultSet.getInt(88));
				game.setHomeassists(resultSet.getInt(89));
				game.setAwaysteals(resultSet.getInt(90));
				game.setHomesteals(resultSet.getInt(91));
				game.setAwayblocks(resultSet.getInt(92));
				game.setHomeblocks(resultSet.getInt(93));
				game.setAwaytotalturnovers(resultSet.getInt(94));
				game.setHometotalturnovers(resultSet.getInt(95));
				game.setAwaypersonalfouls(resultSet.getInt(96));
				game.setHomepersonalfouls(resultSet.getInt(97));
				game.setAwaytechnicalfouls(resultSet.getInt(98));
				game.setHometechnicalfouls(resultSet.getInt(99));
				game.setAwaysagrinrating(resultSet.getFloat(100));
				game.setHomesagrinrating(resultSet.getFloat(101));
				game.setAwaymasseyrating(resultSet.getFloat(102));
				game.setHomemasseyrating(resultSet.getFloat(103));
				game.setAwaysos(resultSet.getFloat(104));
				game.setHomesos(resultSet.getFloat(105));
				game.setRef1(resultSet.getString(106));
				game.setRef2(resultSet.getString(107));
				game.setRef3(resultSet.getString(108));
				game.setSeasonyear(resultSet.getInt(109));
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

		LOGGER.info("Exiting getEspnNcaabGameData()");
		return games;
	}

	/**
	 * 
	 * @param seasonyear
	 * @param team
	 * @return
	 * @throws SQLException
	 */
	public List<EspnCollegeBasketballGameData> getEspnNcaabGameDataBySeasonYear(Integer seasonyear, String team) throws SQLException {
		LOGGER.info("Entering getEspnNcaabGameDataBySeasonYear()");
		final List<EspnCollegeBasketballGameData> games = new ArrayList<EspnCollegeBasketballGameData>();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {
			stmt = conn.prepareStatement("SELECT "
							+ "espngameid," + 
							"  week," + 
							"  hour," + 
							"  minute," + 
							"  ampm," + 
							"  timezone," + 
							"  month," + 
							"  day," + 
							"  year," + 
							"  gamedate," + 
							"  tv," + 
							"  city," + 
							"  state," + 
							"  zipcode," + 
							"  eventlocation," + 
							"  isconferencegame," + 
							"  neutralsite," + 
							"  linefavorite," + 
							"  lineindicator," + 
							"  linevalue," + 
							"  line," + 
							"  total," + 
							"  attendance," + 
							"  awaywin," + 
							"  homewin," + 
							"  awaycollegename," + 
							"  homecollegename," + 
							"  awaymascotname," + 
							"  homemascotname," + 
							"  awayshortname," + 
							"  homeshortname," + 
							"  awayranking," + 
							"  homeranking," + 
							"  awaywins," + 
							"  homewins," + 
							"  awaylosses," + 
							"  homelosses," + 
							"  awayfirstquarterscore," + 
							"  homefirstquarterscore," + 
							"  awaysecondquarterscore," + 
							"  homesecondquarterscore," + 
							"  awayfirsthalfscore," + 
							"  homefirsthalfscore," + 
							"  awaythirdquarterscore," + 
							"  homethirdquarterscore," + 
							"  awayfourthquarterscore," + 
							"  homefourthquarterscore," + 
							"  awaysecondhalfscore," + 
							"  homesecondhalfscore," + 
							"  awayotonescore," + 
							"  homeotonescore," + 
							"  awayottwoscore," + 
							"  homeottwoscore," + 
							"  awayotthreescore," + 
							"  homeotthreescore," + 
							"  awayotfourscore," + 
							"  homeotfourscore," + 
							"  awayotfivescore," + 
							"  homeotfivescore," + 
							"  awaysecondhalfotscore," + 
							"  homesecondhalfotscore," + 
							"  awayfinalscore," + 
							"  homefinalscore," + 
							"  awayfgmade," + 
							"  homefgmade," + 
							"  awayfgattempt," + 
							"  homefgattempt," + 
							"  awayfgpercentage," + 
							"  homefgpercentage," + 
							"  away3ptfgmade," + 
							"  home3ptfgmade," + 
							"  away3ptfgattempt," + 
							"  home3ptfgattempt," + 
							"  away3ptfgpercentage," + 
							"  home3ptfgpercentage," + 
							"  awayftmade," + 
							"  homeftmade," + 
							"  awayftattempt," + 
							"  homeftattempt," + 
							"  awayftpercentage," + 
							"  homeftpercentage," + 
							"  awaytotalrebounds," + 
							"  hometotalrebounds," + 
							"  awayoffrebounds," + 
							"  homeoffrebounds," + 
							"  awaydefrebounds," + 
							"  homedefrebounds," + 
							"  awayassists," + 
							"  homeassists," + 
							"  awaysteals," + 
							"  homesteals," + 
							"  awayblocks," + 
							"  homeblocks," + 
							"  awaytotalturnovers," + 
							"  hometotalturnovers," + 
							"  awaypersonalfouls," + 
							"  homepersonalfouls," + 
							"  awaytechnicalfouls," + 
							"  hometechnicalfouls," + 
							"  awaysagrinrating," + 
							"  homesagrinrating," + 
							"  awaymasseyrating," + 
							"  homemasseyrating," + 
							"  awaysos," + 
							"  homesos," + 
							"  ref1," + 
							"  ref2," + 
							"  ref3," + 
							"  seasonyear"
					+ " FROM espnbasketballgame WHERE seasonyear = ? AND (awaycollegename = ? OR homecollegename = ?)  ORDER BY date DESC");
			stmt.setInt(1, seasonyear);
			stmt.setString(2, team);
			stmt.setString(3, team);
			resultSet = stmt.executeQuery();

			while (resultSet.next()) {
				final EspnCollegeBasketballGameData game = new EspnCollegeBasketballGameData();
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
				game.setAwaysecondhalfotscore(resultSet.getInt(60));
				game.setHomesecondhalfotscore(resultSet.getInt(61));
				game.setAwayfinalscore(resultSet.getInt(62));
				game.setHomefinalscore(resultSet.getInt(63));
				game.setAwayfgmade(resultSet.getInt(64));
				game.setHomefgmade(resultSet.getInt(65));
				game.setAwayfgattempt(resultSet.getInt(66));
				game.setHomefgattempt(resultSet.getInt(67));
				game.setAwayfgpercentage(resultSet.getFloat(68));
				game.setHomefgpercentage(resultSet.getFloat(69));
				game.setAway3ptfgmade(resultSet.getInt(70));
				game.setHome3ptfgmade(resultSet.getInt(71));
				game.setAway3ptfgattempt(resultSet.getInt(72));
				game.setHome3ptfgattempt(resultSet.getInt(73));
				game.setAway3ptfgpercentage(resultSet.getFloat(74));
				game.setHome3ptfgpercentage(resultSet.getFloat(75));
				game.setAwayftmade(resultSet.getInt(76));
				game.setHomeftmade(resultSet.getInt(77));
				game.setAwayftattempt(resultSet.getInt(78));
				game.setHomeftattempt(resultSet.getInt(79));
				game.setAwayftpercentage(resultSet.getFloat(80));
				game.setHomeftpercentage(resultSet.getFloat(81));
				game.setAwaytotalrebounds(resultSet.getInt(82));
				game.setHometotalrebounds(resultSet.getInt(83));
				game.setAwayoffrebounds(resultSet.getInt(84));
				game.setHomeoffrebounds(resultSet.getInt(85));
				game.setAwaydefrebounds(resultSet.getInt(86));
				game.setHomedefrebounds(resultSet.getInt(87));
				game.setAwayassists(resultSet.getInt(88));
				game.setHomeassists(resultSet.getInt(89));
				game.setAwaysteals(resultSet.getInt(90));
				game.setHomesteals(resultSet.getInt(91));
				game.setAwayblocks(resultSet.getInt(92));
				game.setHomeblocks(resultSet.getInt(93));
				game.setAwaytotalturnovers(resultSet.getInt(94));
				game.setHometotalturnovers(resultSet.getInt(95));
				game.setAwaypersonalfouls(resultSet.getInt(96));
				game.setHomepersonalfouls(resultSet.getInt(97));
				game.setAwaytechnicalfouls(resultSet.getInt(98));
				game.setHometechnicalfouls(resultSet.getInt(99));
				game.setAwaysagrinrating(resultSet.getFloat(100));
				game.setHomesagrinrating(resultSet.getFloat(101));
				game.setAwaymasseyrating(resultSet.getFloat(102));
				game.setHomemasseyrating(resultSet.getFloat(103));
				game.setAwaysos(resultSet.getFloat(104));
				game.setHomesos(resultSet.getFloat(105));
				game.setRef1(resultSet.getString(106));
				game.setRef2(resultSet.getString(107));
				game.setRef3(resultSet.getString(108));
				game.setSeasonyear(resultSet.getInt(109));
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

		LOGGER.info("Exiting getEspnNcaabGameDataBySeasonYear()");
		return games;
	}

	/**
	 * 
	 * @param team
	 * @param start
	 * @param end
	 * @return
	 * @throws SQLException
	 */
	public List<EspnCollegeBasketballGameData> getEspnNcaabGameDataByDateTeam(Integer seasonyear, String team, Date start, Date end) throws SQLException {
		LOGGER.info("Entering getEspnNcaabGameDataByDateTeam()");
		LOGGER.debug("start: " + start);
		LOGGER.debug("end: " + end);
		final List<EspnCollegeBasketballGameData> games = new ArrayList<EspnCollegeBasketballGameData>();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {
			final Calendar startd = Calendar.getInstance();
			startd.setTime(start);
			final Calendar endd = Calendar.getInstance();
			endd.setTime(end);

			String sql = "SELECT "
					+ "espngameid," + 
					"  week," + 
					"  hour," + 
					"  minute," + 
					"  ampm," + 
					"  timezone," + 
					"  month," + 
					"  day," + 
					"  year," + 
					"  gamedate," + 
					"  tv," + 
					"  city," + 
					"  state," + 
					"  zipcode," + 
					"  eventlocation," + 
					"  isconferencegame," + 
					"  neutralsite," + 
					"  linefavorite," + 
					"  lineindicator," + 
					"  linevalue," + 
					"  line," + 
					"  total," + 
					"  attendance," + 
					"  awaywin," + 
					"  homewin," + 
					"  awaycollegename," + 
					"  homecollegename," + 
					"  awaymascotname," + 
					"  homemascotname," + 
					"  awayshortname," + 
					"  homeshortname," + 
					"  awayranking," + 
					"  homeranking," + 
					"  awaywins," + 
					"  homewins," + 
					"  awaylosses," + 
					"  homelosses," + 
					"  awayfirstquarterscore," + 
					"  homefirstquarterscore," + 
					"  awaysecondquarterscore," + 
					"  homesecondquarterscore," + 
					"  awayfirsthalfscore," + 
					"  homefirsthalfscore," + 
					"  awaythirdquarterscore," + 
					"  homethirdquarterscore," + 
					"  awayfourthquarterscore," + 
					"  homefourthquarterscore," + 
					"  awaysecondhalfscore," + 
					"  homesecondhalfscore," + 
					"  awayotonescore," + 
					"  homeotonescore," + 
					"  awayottwoscore," + 
					"  homeottwoscore," + 
					"  awayotthreescore," + 
					"  homeotthreescore," + 
					"  awayotfourscore," + 
					"  homeotfourscore," + 
					"  awayotfivescore," + 
					"  homeotfivescore," + 
					"  awaysecondhalfotscore," + 
					"  homesecondhalfotscore," + 
					"  awayfinalscore," + 
					"  homefinalscore," + 
					"  awayfgmade," + 
					"  homefgmade," + 
					"  awayfgattempt," + 
					"  homefgattempt," + 
					"  awayfgpercentage," + 
					"  homefgpercentage," + 
					"  away3ptfgmade," + 
					"  home3ptfgmade," + 
					"  away3ptfgattempt," + 
					"  home3ptfgattempt," + 
					"  away3ptfgpercentage," + 
					"  home3ptfgpercentage," + 
					"  awayftmade," + 
					"  homeftmade," + 
					"  awayftattempt," + 
					"  homeftattempt," + 
					"  awayftpercentage," + 
					"  homeftpercentage," + 
					"  awaytotalrebounds," + 
					"  hometotalrebounds," + 
					"  awayoffrebounds," + 
					"  homeoffrebounds," + 
					"  awaydefrebounds," + 
					"  homedefrebounds," + 
					"  awayassists," + 
					"  homeassists," + 
					"  awaysteals," + 
					"  homesteals," + 
					"  awayblocks," + 
					"  homeblocks," + 
					"  awaytotalturnovers," + 
					"  hometotalturnovers," + 
					"  awaypersonalfouls," + 
					"  homepersonalfouls," + 
					"  awaytechnicalfouls," + 
					"  hometechnicalfouls," + 
					"  awaysagrinrating," + 
					"  homesagrinrating," + 
					"  awaymasseyrating," + 
					"  homemasseyrating," + 
					"  awaysos," + 
					"  homesos," + 
					"  ref1," + 
					"  ref2," + 
					"  ref3," + 
					"  seasonyear"
			+ " FROM espnbasketballgame WHERE gamedate between '" + 
			startd.get(Calendar.YEAR) + "-" + (startd.get(Calendar.MONTH) + 1) + "-" + startd.get(Calendar.DAY_OF_MONTH) + "' AND '" +
			endd.get(Calendar.YEAR) + "-" + (endd.get(Calendar.MONTH) + 1) + "-" + endd.get(Calendar.DAY_OF_MONTH) + 
			"' AND seasonyear = ? AND (awaycollegename = ? OR homecollegename = ?)  ORDER BY gamedate DESC";
			LOGGER.debug("sql: " + sql);

			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, seasonyear);
			stmt.setString(2, team);
			stmt.setString(3, team);
			resultSet = stmt.executeQuery();

			while (resultSet.next()) {
				final EspnCollegeBasketballGameData game = new EspnCollegeBasketballGameData();
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
				game.setAwaysecondhalfotscore(resultSet.getInt(60));
				game.setHomesecondhalfotscore(resultSet.getInt(61));
				game.setAwayfinalscore(resultSet.getInt(62));
				game.setHomefinalscore(resultSet.getInt(63));
				game.setAwayfgmade(resultSet.getInt(64));
				game.setHomefgmade(resultSet.getInt(65));
				game.setAwayfgattempt(resultSet.getInt(66));
				game.setHomefgattempt(resultSet.getInt(67));
				game.setAwayfgpercentage(resultSet.getFloat(68));
				game.setHomefgpercentage(resultSet.getFloat(69));
				game.setAway3ptfgmade(resultSet.getInt(70));
				game.setHome3ptfgmade(resultSet.getInt(71));
				game.setAway3ptfgattempt(resultSet.getInt(72));
				game.setHome3ptfgattempt(resultSet.getInt(73));
				game.setAway3ptfgpercentage(resultSet.getFloat(74));
				game.setHome3ptfgpercentage(resultSet.getFloat(75));
				game.setAwayftmade(resultSet.getInt(76));
				game.setHomeftmade(resultSet.getInt(77));
				game.setAwayftattempt(resultSet.getInt(78));
				game.setHomeftattempt(resultSet.getInt(79));
				game.setAwayftpercentage(resultSet.getFloat(80));
				game.setHomeftpercentage(resultSet.getFloat(81));
				game.setAwaytotalrebounds(resultSet.getInt(82));
				game.setHometotalrebounds(resultSet.getInt(83));
				game.setAwayoffrebounds(resultSet.getInt(84));
				game.setHomeoffrebounds(resultSet.getInt(85));
				game.setAwaydefrebounds(resultSet.getInt(86));
				game.setHomedefrebounds(resultSet.getInt(87));
				game.setAwayassists(resultSet.getInt(88));
				game.setHomeassists(resultSet.getInt(89));
				game.setAwaysteals(resultSet.getInt(90));
				game.setHomesteals(resultSet.getInt(91));
				game.setAwayblocks(resultSet.getInt(92));
				game.setHomeblocks(resultSet.getInt(93));
				game.setAwaytotalturnovers(resultSet.getInt(94));
				game.setHometotalturnovers(resultSet.getInt(95));
				game.setAwaypersonalfouls(resultSet.getInt(96));
				game.setHomepersonalfouls(resultSet.getInt(97));
				game.setAwaytechnicalfouls(resultSet.getInt(98));
				game.setHometechnicalfouls(resultSet.getInt(99));
				game.setAwaysagrinrating(resultSet.getFloat(100));
				game.setHomesagrinrating(resultSet.getFloat(101));
				game.setAwaymasseyrating(resultSet.getFloat(102));
				game.setHomemasseyrating(resultSet.getFloat(103));
				game.setAwaysos(resultSet.getFloat(104));
				game.setHomesos(resultSet.getFloat(105));
				game.setRef1(resultSet.getString(106));
				game.setRef2(resultSet.getString(107));
				game.setRef3(resultSet.getString(108));
				game.setSeasonyear(resultSet.getInt(109));
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

		LOGGER.info("Exiting getEspnNcaabGameDataByDateTeam()");
		return games;
	}

	/**
	 * 
	 * @param year
	 * @return
	 * @throws SQLException
	 */
	public List<EspnCollegeBasketballGameData> getEspnCollegeBasketballGameDataByTeam(Integer seasonyear, String team) throws SQLException {
		LOGGER.info("Entering getEspnCollegeBasketballGameDataByTeam()");
		final List<EspnCollegeBasketballGameData> games = new ArrayList<EspnCollegeBasketballGameData>();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {
			stmt = conn.prepareStatement("SELECT "
					+ "espngameid," + 
					"  week," + 
					"  hour," + 
					"  minute," + 
					"  ampm," + 
					"  timezone," + 
					"  month," + 
					"  day," + 
					"  year," + 
					"  gamedate," + 
					"  tv," + 
					"  city," + 
					"  state," + 
					"  zipcode," + 
					"  eventlocation," + 
					"  isconferencegame," + 
					"  neutralsite," + 
					"  linefavorite," + 
					"  lineindicator," + 
					"  linevalue," + 
					"  line," + 
					"  total," + 
					"  attendance," + 
					"  awaywin," + 
					"  homewin," + 
					"  awaycollegename," + 
					"  homecollegename," + 
					"  awaymascotname," + 
					"  homemascotname," + 
					"  awayshortname," + 
					"  homeshortname," + 
					"  awayranking," + 
					"  homeranking," + 
					"  awaywins," + 
					"  homewins," + 
					"  awaylosses," + 
					"  homelosses," + 
					"  awayfirstquarterscore," + 
					"  homefirstquarterscore," + 
					"  awaysecondquarterscore," + 
					"  homesecondquarterscore," + 
					"  awayfirsthalfscore," + 
					"  homefirsthalfscore," + 
					"  awaythirdquarterscore," + 
					"  homethirdquarterscore," + 
					"  awayfourthquarterscore," + 
					"  homefourthquarterscore," + 
					"  awaysecondhalfscore," + 
					"  homesecondhalfscore," + 
					"  awayotonescore," + 
					"  homeotonescore," + 
					"  awayottwoscore," + 
					"  homeottwoscore," + 
					"  awayotthreescore," + 
					"  homeotthreescore," + 
					"  awayotfourscore," + 
					"  homeotfourscore," + 
					"  awayotfivescore," + 
					"  homeotfivescore," + 
					"  awaysecondhalfotscore," + 
					"  homesecondhalfotscore," + 
					"  awayfinalscore," + 
					"  homefinalscore," + 
					"  awayfgmade," + 
					"  homefgmade," + 
					"  awayfgattempt," + 
					"  homefgattempt," + 
					"  awayfgpercentage," + 
					"  homefgpercentage," + 
					"  away3ptfgmade," + 
					"  home3ptfgmade," + 
					"  away3ptfgattempt," + 
					"  home3ptfgattempt," + 
					"  away3ptfgpercentage," + 
					"  home3ptfgpercentage," + 
					"  awayftmade," + 
					"  homeftmade," + 
					"  awayftattempt," + 
					"  homeftattempt," + 
					"  awayftpercentage," + 
					"  homeftpercentage," + 
					"  awaytotalrebounds," + 
					"  hometotalrebounds," + 
					"  awayoffrebounds," + 
					"  homeoffrebounds," + 
					"  awaydefrebounds," + 
					"  homedefrebounds," + 
					"  awayassists," + 
					"  homeassists," + 
					"  awaysteals," + 
					"  homesteals," + 
					"  awayblocks," + 
					"  homeblocks," + 
					"  awaytotalturnovers," + 
					"  hometotalturnovers," + 
					"  awaypersonalfouls," + 
					"  homepersonalfouls," + 
					"  awaytechnicalfouls," + 
					"  hometechnicalfouls," + 
					"  awaysagrinrating," + 
					"  homesagrinrating," + 
					"  awaymasseyrating," + 
					"  homemasseyrating," + 
					"  awaysos," + 
					"  homesos," + 
					"  ref1," + 
					"  ref2," + 
					"  ref3," + 
					"  seasonyear"
					+ " FROM espnbasketballgame WHERE seasonyear = ? AND (awaycollegename = ? OR homecollegename = ?) ORDER BY date DESC");
			stmt.setInt(1, seasonyear);
			stmt.setString(2, team);
			stmt.setString(3, team);
			resultSet = stmt.executeQuery();

			while (resultSet.next()) {
				final EspnCollegeBasketballGameData game = new EspnCollegeBasketballGameData();
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
				game.setAwaysecondhalfotscore(resultSet.getInt(60));
				game.setHomesecondhalfotscore(resultSet.getInt(61));
				game.setAwayfinalscore(resultSet.getInt(62));
				game.setHomefinalscore(resultSet.getInt(63));
				game.setAwayfgmade(resultSet.getInt(64));
				game.setHomefgmade(resultSet.getInt(65));
				game.setAwayfgattempt(resultSet.getInt(66));
				game.setHomefgattempt(resultSet.getInt(67));
				game.setAwayfgpercentage(resultSet.getFloat(68));
				game.setHomefgpercentage(resultSet.getFloat(69));
				game.setAway3ptfgmade(resultSet.getInt(70));
				game.setHome3ptfgmade(resultSet.getInt(71));
				game.setAway3ptfgattempt(resultSet.getInt(72));
				game.setHome3ptfgattempt(resultSet.getInt(73));
				game.setAway3ptfgpercentage(resultSet.getFloat(74));
				game.setHome3ptfgpercentage(resultSet.getFloat(75));
				game.setAwayftmade(resultSet.getInt(76));
				game.setHomeftmade(resultSet.getInt(77));
				game.setAwayftattempt(resultSet.getInt(78));
				game.setHomeftattempt(resultSet.getInt(79));
				game.setAwayftpercentage(resultSet.getFloat(80));
				game.setHomeftpercentage(resultSet.getFloat(81));
				game.setAwaytotalrebounds(resultSet.getInt(82));
				game.setHometotalrebounds(resultSet.getInt(83));
				game.setAwayoffrebounds(resultSet.getInt(84));
				game.setHomeoffrebounds(resultSet.getInt(85));
				game.setAwaydefrebounds(resultSet.getInt(86));
				game.setHomedefrebounds(resultSet.getInt(87));
				game.setAwayassists(resultSet.getInt(88));
				game.setHomeassists(resultSet.getInt(89));
				game.setAwaysteals(resultSet.getInt(90));
				game.setHomesteals(resultSet.getInt(91));
				game.setAwayblocks(resultSet.getInt(92));
				game.setHomeblocks(resultSet.getInt(93));
				game.setAwaytotalturnovers(resultSet.getInt(94));
				game.setHometotalturnovers(resultSet.getInt(95));
				game.setAwaypersonalfouls(resultSet.getInt(96));
				game.setHomepersonalfouls(resultSet.getInt(97));
				game.setAwaytechnicalfouls(resultSet.getInt(98));
				game.setHometechnicalfouls(resultSet.getInt(99));
				game.setAwaysagrinrating(resultSet.getFloat(100));
				game.setHomesagrinrating(resultSet.getFloat(101));
				game.setAwaymasseyrating(resultSet.getFloat(102));
				game.setHomemasseyrating(resultSet.getFloat(103));
				game.setAwaysos(resultSet.getFloat(104));
				game.setHomesos(resultSet.getFloat(105));
				game.setRef1(resultSet.getString(106));
				game.setRef2(resultSet.getString(107));
				game.setRef3(resultSet.getString(108));
				game.setSeasonyear(resultSet.getInt(109));
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

		LOGGER.info("Exiting getEspnCollegeBasketballGameDataByTeam()");
		return games;
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
}