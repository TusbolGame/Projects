/**
 * 
 */
package com.ticketadvantage.services.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.apache.log4j.Logger;

import com.ticketadvantage.services.db.manager.DataSourceAutoManager;
import com.ticketadvantage.services.db.manager.DataSourceNoAutoManager;

/**
 * @author jmiller
 *
 */
public class BaseDB {
	private static final Logger LOGGER = Logger.getLogger(BaseDB.class);

	/**
	 * 
	 */
	public BaseDB() {
		super();
	}

	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	protected Connection getConnection() throws SQLException {
		return DataSourceAutoManager.getConnection();
	}

	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	protected Connection getNoAutoConnection() throws SQLException {
		return DataSourceNoAutoManager.getConnection();
	}

	/**
	 * 
	 * @param statement
	 * @param resultSet
	 * @throws SQLException
	 */
	protected void closeStreams(Statement statement, ResultSet resultSet) throws SQLException {
		LOGGER.info("Entering closeStreams()");

		if (resultSet != null) {
			resultSet.close();
			resultSet = null;
		}
		if (statement != null) {
			statement.close();
			statement = null;
		}

		LOGGER.info("Exiting closeStreams()");
	}

	/**
	 * 
	 * @param connection
	 * @param statement
	 * @param resultSet
	 * @throws SQLException
	 */
	protected void closeStreams(Connection connection, Statement statement, ResultSet resultSet) throws SQLException {
		LOGGER.info("Entering closeStreams()");

		if (resultSet != null) {
			resultSet.close();
			resultSet = null;
		}
		if (statement != null) {
			statement.close();
			statement = null;
		}
		if (connection != null) {
			connection.close();
			connection = null;
		}

		LOGGER.info("Exiting closeStreams()");
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	protected String checkString(String value) {
		if (value == null) {
			value = "";
		}
		return value;
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	protected Long checkLong(Long value) {
		if (value == null) {
			value = new Long(0);
		}
		return value;
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	protected Integer checkInteger(Integer value) {
		if (value == null) {
			value = new Integer(0);
		}
		return value;
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	protected Float checkFloat(Float value) {
		if (value == null) {
			value = new Float(0);
		}
		return value;
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	protected Date checkTimestamp(Date value) {
		if (value == null) {
			value = new Date();
		}
		return value;
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	protected Boolean checkBoolean(Boolean value) {
		if (value == null) {
			value = new Boolean(false);
		}
		return value;
	}
}