/**
 * 
 */
package com.ticketadvantage.services.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.EmailEvent;

/**
 * @author jmiller
 *
 */
public class EmailEventDB extends BaseDB {
	private static final Logger LOGGER = Logger.getLogger(EmailEventDB.class);

	/**
	 * 
	 */
	public EmailEventDB() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * 
	 * @param emailEvent
	 * @return
	 * @throws BatchException
	 */
	public EmailEvent persist(EmailEvent emailEvent) throws BatchException {
		LOGGER.info("Entering persist()");
		LOGGER.debug("EmailEvent: " + emailEvent);
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;		

		try {
			StringBuffer sb = new StringBuffer(200);
			// Write out the user to the users tables
			emailEvent.setId(null);
			sb.append("INSERT into emailevent (messagenum, fromemail, toemail, ccemail, bccemail, subject, ")
					.append("bodytext, bodyhtml, emailname, inet, datesent, datereceived, datecreated, datemodified) ")
					.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

			conn = this.getConnection();
			stmt = conn.prepareStatement(sb.toString(), Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, emailEvent.getMessagenum());
			stmt.setString(2, emailEvent.getFromemail());
			stmt.setString(3, emailEvent.getToemail());
			stmt.setString(4, emailEvent.getCcemail());
			stmt.setString(5, emailEvent.getBccemail());
			stmt.setString(6, emailEvent.getSubject());
			String bodyText = emailEvent.getBodytext();
			if (bodyText != null && bodyText.length() > 4000) {
				bodyText = bodyText.substring(0, 4000);
			}
			String bodyHtml = emailEvent.getBodyhtml();
			if (bodyHtml != null && bodyHtml.length() > 4000) {
				bodyHtml = bodyHtml.substring(0, 4000);
			}
			stmt.setString(7, bodyText);
			stmt.setString(8, bodyHtml);
			stmt.setString(9, emailEvent.getEmailname());
			stmt.setString(10, emailEvent.getInet());
			stmt.setTimestamp(11, new java.sql.Timestamp(emailEvent.getDatesent().getTime()));
			stmt.setTimestamp(12, new java.sql.Timestamp(emailEvent.getDatereceived().getTime()));
			stmt.setTimestamp(13, new java.sql.Timestamp(emailEvent.getDatecreated().getTime()));
			stmt.setTimestamp(14, new java.sql.Timestamp(emailEvent.getDatemodified().getTime()));

			stmt.executeUpdate();
			resultSet = stmt.getGeneratedKeys();
			resultSet.next();
			long primaryKey = resultSet.getLong(1);
			emailEvent.setId(primaryKey);
		} catch (SQLException sqle) {
			LOGGER.error(sqle);

			if (sqle.getMessage().contains("An I/O error occurred while sending to the backend")) {
				throw new BatchException(BatchErrorCodes.DB_CONNECTION_EXCEPTION,
						BatchErrorMessage.DB_CONNECTION_EXCEPTION, sqle.getMessage());
			} else {
				throw new BatchException(BatchErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,
						BatchErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION, sqle.getMessage());
			}
		} finally {
			try {
				closeStreams(conn, stmt, resultSet);
			} catch (Throwable t) {
				LOGGER.error(t.getMessage(), t);
			}
		}

		LOGGER.info("Exiting persist()");
		return emailEvent;
	}

	/**
	 * 
	 * @param id
	 * @throws BatchException
	 */
	public void delete(Long id) throws BatchException {
		LOGGER.info("Entering delete()");
		LOGGER.debug("id: " + id);
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {
			String sqlEmailEvent = "SELECT ee.id FROM emailevent ee WHERE ee.id = ?";
			conn = this.getConnection();
			stmt = conn.prepareStatement(sqlEmailEvent);
			stmt.setLong(1, id);
			resultSet = stmt.executeQuery();
	
			// Loop through the results
			Long pid = null;
			while (resultSet.next()) {
				pid = resultSet.getLong(1);
			}

		    if (resultSet != null) {
		    		resultSet.close();
			}
		    if (stmt != null) {
				stmt.close();
			}

			if (pid != null) {
				sqlEmailEvent = "DELETE FROM emailevent where id = ?";
				stmt = conn.prepareStatement(sqlEmailEvent);
				stmt.setLong(1, pid);
				stmt.executeUpdate();
			    if (stmt != null) {
					stmt.close();
				}
			} else {
				throw new BatchException(BatchErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
						BatchErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION, "Exception with delete");
			}
		} catch (SQLException sqle) {
			LOGGER.error(sqle);

			if (sqle.getMessage().contains("An I/O error occurred while sending to the backend")) {
				throw new BatchException(BatchErrorCodes.DB_CONNECTION_EXCEPTION,
						BatchErrorMessage.DB_CONNECTION_EXCEPTION, sqle.getMessage());
			} else {
				throw new BatchException(BatchErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,
						BatchErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION, sqle.getMessage());
			}
		} finally {
			try {
				closeStreams(conn, stmt, resultSet);
			} catch (Throwable t) {
				LOGGER.error(t.getMessage(), t);
			}
		}

		LOGGER.info("Exiting delete()");
	}

	/**
	 * 
	 * @param userid
	 * @return
	 * @throws BatchException
	 */
	public List<EmailEvent> findEmailEventsByUserId(Long userid) throws BatchException {
		LOGGER.info("Entering findEmailEventsByUserIdByType()");
		final List<EmailEvent> emailEvents = new ArrayList<EmailEvent>();
		EmailEvent eEvent = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {
			final String sqlDestination = "SELECT * FROM emailevent ee WHERE ee.userid = ?";
			conn = this.getConnection();
			stmt = conn.prepareStatement(sqlDestination);
			stmt.setLong(1, userid);
			resultSet = stmt.executeQuery();

			// Loop through the results
			while (resultSet.next()) {
				eEvent = new EmailEvent();
				eEvent.setId(resultSet.getLong("id"));
				eEvent.setMessagenum(resultSet.getInt("messagenum"));
				eEvent.setFromemail(resultSet.getString("fromemail"));
				eEvent.setToemail(resultSet.getString("toemail"));
				eEvent.setCcemail(resultSet.getString("ccemail"));
				eEvent.setBccemail(resultSet.getString("bccemail"));
				eEvent.setSubject(resultSet.getString("subject"));
				eEvent.setBodytext(resultSet.getString("bodytext"));
				eEvent.setBodyhtml(resultSet.getString("bodyhtml"));
				eEvent.setEmailname(resultSet.getString("emailname"));
				eEvent.setInet(resultSet.getString("inet"));
				eEvent.setDatesent(resultSet.getTimestamp("datesent"));
				eEvent.setDatereceived(resultSet.getTimestamp("datereceived"));
				eEvent.setDatecreated(resultSet.getTimestamp("datecreated"));
				eEvent.setDatemodified(resultSet.getTimestamp("datemodified"));
				emailEvents.add(eEvent);
			}
		} catch (SQLException sqle) {
			LOGGER.error(sqle);

			if (sqle.getMessage().contains("An I/O error occurred while sending to the backend")) {
				throw new BatchException(BatchErrorCodes.DB_CONNECTION_EXCEPTION,
						BatchErrorMessage.DB_CONNECTION_EXCEPTION, sqle.getMessage());
			} else {
				throw new BatchException(BatchErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,
						BatchErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION, sqle.getMessage());
			}
		} finally {
			try {
				closeStreams(conn, stmt, resultSet);
			} catch (Throwable t) {
				LOGGER.error(t.getMessage(), t);
			}
		}

		LOGGER.info("Exiting findEmailEventsByUserIdByType()");
		return emailEvents;
	}
}