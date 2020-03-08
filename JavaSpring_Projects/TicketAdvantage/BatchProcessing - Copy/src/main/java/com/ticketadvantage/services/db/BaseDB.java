/**
 * 
 */
package com.ticketadvantage.services.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

/**
 * @author jmiller
 *
 */
public class BaseDB {
	private static final Logger LOGGER = Logger.getLogger(BaseDB.class);
	protected Connection conn = null;

	/**
	 * 
	 */
	public BaseDB() {
		super();
	}

	/**
	 * 
	 * @param startConnection
	 */
	public BaseDB(boolean startConnection) {
		super();
		if (startConnection) {
			start();
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
	 */
	public void start() {
		LOGGER.info("Entering start()");

		try {
			String url = "jdbc:postgresql://localhost:5432/ticketadvantage";
			String username = "ticketadvantage";
			String password = "ticketadvantage";

			conn = DriverManager.getConnection(url, username, password);
		} catch (SQLException sqle) {
			LOGGER.error(sqle);
		}

		LOGGER.info("Exiting start()");
	}

	/**
	 * 
	 */
	public void complete() {
		LOGGER.info("Entering complete()");

		try {
			if (conn != null) {
				conn.close();
				conn = null;
			}
		} catch (SQLException sqle) {
			LOGGER.error(sqle);
		}

		LOGGER.info("Exiting complete()");
	}

	/**
	 * 
	 * @param statement
	 * @param resultSet
	 * @throws SQLException
	 */
	protected void closeStreams(Statement statement, ResultSet resultSet) throws SQLException {
		if (statement != null) {
			statement.close();
			statement = null;
		}
		if (resultSet != null) {
			resultSet.close();
			resultSet = null;
		}
	}
}