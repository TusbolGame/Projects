/**
 * 
 */
package com.ticketadvantage.services.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.SpreadRecordEvent;
import com.ticketadvantage.services.util.ServerInfo;

/**
 * @author jmiller
 *
 */
public class SiteActiveDB {
	private static final Logger LOGGER = Logger.getLogger(SiteActiveDB.class);
	private Connection conn = null;

	/**
	 * 
	 */
	public SiteActiveDB() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			SiteActiveDB recordEventDB = new SiteActiveDB();
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
	 * @return the conn
	 */
	public Connection getConn() {
		return conn;
	}

	/**
	 * @param conn the conn to set
	 */
	public void setConn(Connection conn) {
		this.conn = conn;
	}

	/**
	 * 
	 * @param sitetype
	 * @param username
	 * @throws BatchException
	 */
	public void persistSiteActive(String sitetype, String username) throws SQLException {
		LOGGER.info("Entering persistSiteActive()");
		PreparedStatement stmt = null;

		try {
			final StringBuffer sb = new StringBuffer(200);
			sb.append("INSERT into siteactive (sitetype, username) VALUES (?, ?)");

			stmt = conn.prepareStatement(sb.toString());
			stmt.setString(1, sitetype);
			stmt.setString(2, username);

			stmt.executeUpdate();
		} catch (SQLException sqle) {
			LOGGER.error(sqle);
			throw sqle;
		} finally {
			if (stmt != null) {
				stmt.close();
			}

			// Finally, commit the transaction.
			conn.commit();
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
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {
			final StringBuffer sb = new StringBuffer(200);
			sb.append("SELECT sa.sitetype, sa.username FROM siteactive sa ")
					.append("WHERE (sa.sitetype = ? AND sa.username = ?) ");

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
			if (stmt != null) {
				stmt.close();
			}
			if (resultSet != null) {
				resultSet.close();
			}

			// Finally, commit the transaction.
			conn.commit();
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
		PreparedStatement stmt = null;

		try {
			final StringBuffer sb = new StringBuffer(200);
			sb.append("DELETE from siteactive where sitetype = ? AND username = ?");

			stmt = conn.prepareStatement(sb.toString());
			stmt.setString(1, sitetype);
			stmt.setString(2, username);

			stmt.executeUpdate();
		} catch (SQLException sqle) {
			LOGGER.error(sqle);
			throw sqle;
		} finally {
			if (stmt != null) {
				stmt.close();
			}

			// Finally, commit the transaction.
			conn.commit();
		}

		LOGGER.info("Exiting deleteSiteActive()");
	}
}