/**
 * 
 */
package com.ticketadvantage.services.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.SpreadRecordEvent;

/**
 * @author jmiller
 *
 */
public class SiteActiveDosDB extends BaseDB {
	private static final Logger LOGGER = Logger.getLogger(SiteActiveDosDB.class);

	/**
	 * 
	 */
	public SiteActiveDosDB() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			SimpleDateFormat PENDING_DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
			Date gameDate = PENDING_DATE_FORMAT.parse("10/14/2017 7:30 PM" + " " + "EDT");
			SpreadRecordEvent recordEvent = new SpreadRecordEvent();
			recordEvent.setEventdatetime(gameDate);
			recordEvent.setRotationid(191);
			recordEvent.setSport("ncaaflines");
			recordEvent.setUserid(new Long(5));
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	/**
	 * 
	 * @param sitetype
	 * @param username
	 * @throws BatchException
	 */
	public void persistSiteActive(String sitetype, String username) throws SQLException {
		LOGGER.info("Entering persistSiteActive()");
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {
			final StringBuffer sb = new StringBuffer(200);
			sb.append("INSERT into siteactive (sitetype, username) VALUES (?, ?)");

			conn = this.getConnection();
			stmt = conn.prepareStatement(sb.toString());
			stmt.setString(1, sitetype);
			stmt.setString(2, username);
			
			stmt.executeUpdate();
		} catch (SQLException sqle) {
			LOGGER.error(sqle);
			throw sqle;
		} finally {
			closeStreams(conn, stmt, resultSet);
		}

		LOGGER.info("Exiting persistSiteActive()");
	}

	/**
	 * 
	 * @param recordEvent
	 * @return
	 * @throws SQLException
	 */
	public boolean hasActiveSite(String sitetype, String username) throws SQLException {
		LOGGER.info("Entering hasActiveSite()");
		boolean retValue = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {
			final StringBuffer sb = new StringBuffer(200);
			sb.append("SELECT sa.sitetype, sa.username FROM siteactive sa ")
					.append("WHERE (sa.sitetype = ? OR sa.username = ?) ");

			conn = super.getConnection();
			stmt = conn.prepareStatement(sb.toString());
			stmt.setString(1, sitetype);
			stmt.setString(2, username);
			resultSet = stmt.executeQuery();

			if (resultSet.next()) {
				LOGGER.error("Duplicate is true");
				retValue = true;
			}
		} catch (SQLException sqle) {
			LOGGER.error(sqle);
			throw sqle;
		} finally {
			closeStreams(conn, stmt, resultSet);
		}
		
		LOGGER.info("Exiting hasActiveSite()");
		return retValue;
	}

	/**
	 * 
	 * @param sitetype
	 * @param username
	 * @throws BatchException
	 */
	public void deleteSiteActive(String sitetype, String username) throws SQLException {
		LOGGER.info("Entering deleteSiteActive()");
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {
			final StringBuffer sb = new StringBuffer(200);
			sb.append("DELETE from siteactive where sitetype = ? AND username = ?");

			conn = super.getConnection();
			stmt = conn.prepareStatement(sb.toString());
			stmt.setString(1, sitetype);
			stmt.setString(2, username);

			stmt.executeUpdate();
		} catch (SQLException sqle) {
			LOGGER.error(sqle);
			throw sqle;
		} finally {
			closeStreams(conn, stmt, resultSet);
		}

		LOGGER.info("Exiting deleteSiteActive()");
	}
}