/**
 * 
 */
package com.wootechnologies.dataminer.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

/**
 * @author jmiller
 *
 */
public class DataMinerDB {
	private static final Logger LOGGER = Logger.getLogger(DataMinerDB.class);
	protected Connection conn = null;

	/**
	 * 
	 */
	public DataMinerDB() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * 
	 */
	public void start() {
		try {
			String url = "jdbc:postgresql://localhost:38293/wooanalytics";
//			String url = "jdbc:postgresql://35.153.142.138:38293/wooanalytics";
			String username = "wooanalytics";
			String password = "3id39d";

			conn = DriverManager.getConnection(url, username, password);
			conn.setAutoCommit(true);
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}
	}

	/**
	 * 
	 */
	public void complete() {
		try {
			if (conn != null) {
				conn.close();
				conn = null;
			}
		} catch (SQLException sqle) {
			LOGGER.error(sqle);
			sqle.printStackTrace();
		}
	}

	/**
	 * 
	 * @param year
	 * @param collegename
	 * @return
	 * @throws SQLException
	 */
	public Integer numberOfGames(Integer year, String collegename) throws SQLException {
		LOGGER.info("Entering numberOfGames()");
		Integer numWeek = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {	
			final StringBuffer sb = new StringBuffer(2000);
			sb.append("SELECT COUNT(*) AS rowcount FROM footballpointspergame WHERE year = ? AND collegename = ? AND stattype = 'all'");

			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, year);
			stmt.setString(2, collegename);

			resultSet = stmt.executeQuery();
			resultSet.next();
			numWeek = resultSet.getInt("rowcount");
		} catch (SQLException sqle) {
			LOGGER.error(sqle.getMessage(), sqle);
//			throw sqle;
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}

		LOGGER.info("Exiting numberOfGames()");
		return numWeek;
	}

	/**
	 * 
	 * @param year
	 * @param collegename
	 * @return
	 * @throws SQLException
	 */
	public Integer numberOfAwayGames(Integer year, String collegename) throws SQLException {
		LOGGER.info("Entering numberOfAwayGames()");
		Integer numWeek = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {	
			final StringBuffer sb = new StringBuffer(2000);
			sb.append("SELECT COUNT(*) AS rowcount FROM footballpointspergame WHERE year = ? AND collegename = ? AND stattype = 'away'");

			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, year);
			stmt.setString(2, collegename);

			resultSet = stmt.executeQuery();
			resultSet.next();
			numWeek = resultSet.getInt("rowcount");
		} catch (SQLException sqle) {
			LOGGER.error(sqle.getMessage(), sqle);
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}

		LOGGER.info("Exiting numberOfAwayGames()");
		return numWeek;
	}

	/**
	 * 
	 * @param year
	 * @param collegename
	 * @return
	 * @throws SQLException
	 */
	public Integer numberOfHomeGames(Integer year, String collegename) throws SQLException {
		LOGGER.info("Entering numberOfHomeGames()");
		Integer numWeek = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {	
			final StringBuffer sb = new StringBuffer(2000);
			sb.append("SELECT COUNT(*) AS rowcount FROM footballpointspergame WHERE year = ? AND collegename = ? AND stattype = 'home'");

			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, year);
			stmt.setString(2, collegename);

			resultSet = stmt.executeQuery();
			resultSet.next();
			numWeek = resultSet.getInt("rowcount");
		} catch (SQLException sqle) {
			LOGGER.error(sqle.getMessage(), sqle);
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}

		LOGGER.info("Exiting numberOfHomeGames()");
		return numWeek;
	}

	/**
	 * 
	 * @param week
	 * @param year
	 * @param collegename
	 * @return
	 * @throws SQLException
	 */
	public Integer checkForDuplicateGame(Integer week, Integer year, String collegename) throws SQLException {
		LOGGER.info("Entering checkForDuplicateGame()");
		Integer numWeek = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {	
			final StringBuffer sb = new StringBuffer(2000);
			sb.append("SELECT COUNT(*) AS rowcount FROM espnbasketballgame WHERE week = ? AND year = ? AND (awaycollegename = ? OR homecollegename = ?)");

			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, week);
			stmt.setInt(2, year);
			stmt.setString(3, collegename);
			stmt.setString(4, collegename);

			resultSet = stmt.executeQuery();
			resultSet.next();
			numWeek = resultSet.getInt("rowcount");
		} catch (SQLException sqle) {
			LOGGER.error(sqle.getMessage(), sqle);
//			throw sqle;
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}

		LOGGER.info("Exiting checkForDuplicateGame()");
		return numWeek;
	}
}