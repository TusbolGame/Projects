/**
 * 
 */
package com.ticketadvantage.services.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.zone.ZoneRules;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.AccountEvent;
import com.ticketadvantage.services.model.Accounts;
import com.ticketadvantage.services.model.BaseRecordEvent;
import com.ticketadvantage.services.model.MlRecordEvent;
import com.ticketadvantage.services.model.SpreadRecordEvent;
import com.ticketadvantage.services.model.TotalRecordEvent;

/**
 * @author jmiller
 *
 */
public class RecordEventDB extends BaseDB {
	private static final Logger LOGGER = Logger.getLogger(RecordEventDB.class);

	/**
	 * 
	 */
	public RecordEventDB() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final ZonedDateTime now = ZonedDateTime.now(ZoneId.of("America/Los_Angeles"));
			final ZoneId z = now.getZone();
			final ZoneRules zoneRules = z.getRules();
			final Boolean isDst = zoneRules.isDaylightSavings(now.toInstant());
			System.out.println("isDst: " + isDst);

/*
			RecordEventDB recordEventDB = new RecordEventDB();
			recordEventDB.start();
			SimpleDateFormat PENDING_DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
			Date gameDate = PENDING_DATE_FORMAT.parse("10/14/2017 7:30 PM" + " " + "EDT");
			SpreadRecordEvent recordEvent = new SpreadRecordEvent();
			recordEvent.setEventdatetime(gameDate);
			recordEvent.setRotationid(191);
			recordEvent.setSport("ncaaflines");
			recordEvent.setUserid(new Long(5));

			recordEventDB.checkDuplicateSpreadEvent(recordEvent);
*/
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	/**
	 * 
	 * @param rotationId
	 * @param gameDate1
	 * @param gameDate2
	 * @param sport
	 * @param userid
	 * @return
	 * @throws SQLException
	 */
	public TotalRecordEvent getTotalEventByRotationId(Integer rotationId, Date gameDate1, Date gameDate2, String sport, Long userid) throws BatchException {
		LOGGER.info("Entering getTotalEventByRotationId()");
		TotalRecordEvent totalRecordEvent = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {
			final String dateCheck = getDateQuery(gameDate1, gameDate2);
			final StringBuffer sb = new StringBuffer(200);
			sb.append("SELECT tre.id FROM totalrecordevents tre ")
				.append("WHERE (tre.rotationid = ? OR tre.rotationid = ?) ")
				.append("AND tre.sport = ? ")
				.append("AND tre.userid = ? ")
				.append(dateCheck);

			Integer rotationId1 = rotationId;
			Integer rotationId2 = null;

			// Check for even or odd
			if (rotationId1 % 2 == 0) {
				rotationId2 = rotationId1 - 1;
			} else {
				rotationId2 = rotationId1 + 1;
			}

			conn = this.getConnection();
			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, rotationId1);
			stmt.setInt(2, rotationId2);
			stmt.setString(3, sport);
			stmt.setLong(4, userid);
			resultSet = stmt.executeQuery();
	
			if (resultSet.next()) {
				totalRecordEvent = new TotalRecordEvent();

				// Common
				totalRecordEvent.setId(resultSet.getLong("id"));
				totalRecordEvent.setEventname(resultSet.getString("eventname"));
				totalRecordEvent.setEventtype(resultSet.getString("eventtype"));
				totalRecordEvent.setSport(resultSet.getString("sport"));
				totalRecordEvent.setUserid(resultSet.getLong("userid"));
				totalRecordEvent.setAccountid(resultSet.getLong("accountid"));
				totalRecordEvent.setGroupid(resultSet.getLong("groupid"));
				totalRecordEvent.setEventid(resultSet.getInt("eventid"));
				totalRecordEvent.setEventid1(resultSet.getInt("eventid1"));
				totalRecordEvent.setEventid2(resultSet.getInt("eventid2"));
				totalRecordEvent.setEventteam1(resultSet.getString("eventteam1"));
				totalRecordEvent.setEventteam2(resultSet.getString("eventteam2"));
				totalRecordEvent.setAmount(resultSet.getString("amount"));
				totalRecordEvent.setWtype(resultSet.getString("wtype"));
				totalRecordEvent.setIscompleted(resultSet.getBoolean("iscompleted"));
				totalRecordEvent.setAttempts(resultSet.getInt("attempts"));
				totalRecordEvent.setCurrentattempts(resultSet.getInt("currentattempts"));
				totalRecordEvent.setAttempttime(resultSet.getTimestamp("attempttime"));
				totalRecordEvent.setEventdatetime(resultSet.getTimestamp("eventdatetime"));
				totalRecordEvent.setDatentime(resultSet.getTimestamp("datentime"));
				totalRecordEvent.setDatecreated(resultSet.getTimestamp("datecreated"));
				totalRecordEvent.setDatemodified(resultSet.getTimestamp("datemodified"));
				totalRecordEvent.setScrappername(resultSet.getString("scrappername"));
				totalRecordEvent.setActiontype(resultSet.getString("actiontype"));
				totalRecordEvent.setTextnumber(resultSet.getString("textnumber"));
				totalRecordEvent.setRotationid(resultSet.getInt("rotationid"));

				// Total only
				totalRecordEvent.setTotalinputfirstone(resultSet.getString("totalinputfirstone"));
				totalRecordEvent.setTotaljuiceplusminusfirstone(resultSet.getString("totaljuiceplusminusfirstone"));
				totalRecordEvent.setTotalinputjuicefirstone(resultSet.getString("totalinputjuicefirstone"));
				totalRecordEvent.setTotalinputfirsttwo(resultSet.getString("totalinputfirsttwo"));
				totalRecordEvent.setTotaljuiceplusminusfirsttwo(resultSet.getString("totaljuiceplusminusfirsttwo"));
				totalRecordEvent.setTotalinputjuicefirsttwo(resultSet.getString("totalinputjuicefirsttwo"));
				totalRecordEvent.setTotalinputsecondone(resultSet.getString("totalinputsecondone"));
				totalRecordEvent.setTotaljuiceplusminussecondone(resultSet.getString("totaljuiceplusminussecondone"));
				totalRecordEvent.setTotalinputjuicesecondone(resultSet.getString("totalinputjuicesecondone"));
				totalRecordEvent.setTotalinputsecondtwo(resultSet.getString("totalinputsecondtwo"));
				totalRecordEvent.setTotaljuiceplusminussecondtwo(resultSet.getString("totaljuiceplusminussecondtwo"));
				totalRecordEvent.setTotalinputjuicesecondtwo(resultSet.getString("totalinputjuicesecondtwo"));
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

		LOGGER.info("Exiting getTotalEventByRotationId()");
		return totalRecordEvent;
	}

	/**
	 * 
	 * @param rotationId
	 * @param gameDate1
	 * @param gameDate2
	 * @param sport
	 * @param userid
	 * @return
	 * @throws SQLException
	 */
	public MlRecordEvent getMlEventByRotationId(Integer rotationId, Date gameDate1, Date gameDate2, String sport, Long userid) throws BatchException {
		LOGGER.info("Entering getMlEventByRotationId()");
		MlRecordEvent mlRecordEvent = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {
			final String dateCheck = getDateQuery(gameDate1, gameDate2);
			final StringBuffer sb = new StringBuffer(200);
			sb.append("SELECT tre.id FROM totalrecordevents tre ")
				.append("WHERE (tre.rotationid = ? OR tre.rotationid = ?) ")
				.append("AND tre.sport = ? ")
				.append("AND tre.userid = ? ")
				.append(dateCheck);

			Integer rotationId1 = rotationId;
			Integer rotationId2 = null;

			// Check for even or odd
			if (rotationId1 % 2 == 0) {
				rotationId2 = rotationId1 - 1;
			} else {
				rotationId2 = rotationId1 + 1;
			}

			conn = this.getConnection();
			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, rotationId1);
			stmt.setInt(2, rotationId2);
			stmt.setString(3, sport);
			stmt.setLong(4, userid);
			resultSet = stmt.executeQuery();
	
			if (resultSet.next()) {
				mlRecordEvent = new MlRecordEvent();

				// Common
				mlRecordEvent.setId(resultSet.getLong("id"));
				mlRecordEvent.setEventname(resultSet.getString("eventname"));
				mlRecordEvent.setEventtype(resultSet.getString("eventtype"));
				mlRecordEvent.setSport(resultSet.getString("sport"));
				mlRecordEvent.setUserid(resultSet.getLong("userid"));
				mlRecordEvent.setAccountid(resultSet.getLong("accountid"));
				mlRecordEvent.setGroupid(resultSet.getLong("groupid"));
				mlRecordEvent.setEventid(resultSet.getInt("eventid"));
				mlRecordEvent.setEventid1(resultSet.getInt("eventid1"));
				mlRecordEvent.setEventid2(resultSet.getInt("eventid2"));
				mlRecordEvent.setEventteam1(resultSet.getString("eventteam1"));
				mlRecordEvent.setEventteam2(resultSet.getString("eventteam2"));
				mlRecordEvent.setAmount(resultSet.getString("amount"));
				mlRecordEvent.setWtype(resultSet.getString("wtype"));
				mlRecordEvent.setIscompleted(resultSet.getBoolean("iscompleted"));
				mlRecordEvent.setAttempts(resultSet.getInt("attempts"));
				mlRecordEvent.setCurrentattempts(resultSet.getInt("currentattempts"));
				mlRecordEvent.setAttempttime(resultSet.getTimestamp("attempttime"));
				mlRecordEvent.setEventdatetime(resultSet.getTimestamp("eventdatetime"));
				mlRecordEvent.setDatentime(resultSet.getTimestamp("datentime"));
				mlRecordEvent.setDatecreated(resultSet.getTimestamp("datecreated"));
				mlRecordEvent.setDatemodified(resultSet.getTimestamp("datemodified"));
				mlRecordEvent.setScrappername(resultSet.getString("scrappername"));
				mlRecordEvent.setActiontype(resultSet.getString("actiontype"));
				mlRecordEvent.setTextnumber(resultSet.getString("textnumber"));
				mlRecordEvent.setRotationid(resultSet.getInt("rotationid"));

				// ML only
				mlRecordEvent.setMlinputfirstone(resultSet.getString("mlinputfirstone"));
				mlRecordEvent.setMlplusminusfirstone(resultSet.getString("mlplusminusfirstone"));
				mlRecordEvent.setMlinputfirsttwo(resultSet.getString("mlinputfirsttwo"));
				mlRecordEvent.setMlplusminusfirsttwo(resultSet.getString("mlplusminusfirsttwo"));
				mlRecordEvent.setMlinputsecondone(resultSet.getString("mlinputsecondone"));
				mlRecordEvent.setMlplusminussecondone(resultSet.getString("mlplusminussecondone"));
				mlRecordEvent.setMlinputsecondtwo(resultSet.getString("mlinputsecondtwo"));
				mlRecordEvent.setMlplusminussecondtwo(resultSet.getString("mlplusminussecondtwo"));
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

		LOGGER.info("Exiting getMlEventByRotationId()");
		return mlRecordEvent;
	}

	/**
	 * 
	 * @param recordEvent
	 * @return
	 * @throws SQLException
	 */
	public boolean checkDuplicateSpreadEvent(SpreadRecordEvent recordEvent) throws BatchException {
		LOGGER.info("Entering checkDuplicateSpreadEvent()");
		boolean retValue = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {
			final String dateCheck = getDateQuery(recordEvent);
			LOGGER.debug("dateCheck: " + dateCheck);

			final StringBuffer sb = new StringBuffer(200);
			sb.append("SELECT sre.id FROM spreadrecordevents sre ")
					.append("WHERE (sre.rotationid = ? OR sre.rotationid = ?) ")
					.append("AND sre.sport = ? ")
					.append("AND sre.userid = ? ")
					.append(dateCheck);

			Integer rotationId1 = recordEvent.getRotationid();
			Integer rotationId2 = null;

			// Check for even or odd
			if (rotationId1 % 2 == 0) {
				rotationId2 = rotationId1 - 1;
			} else {
				rotationId2 = rotationId1 + 1;
			}

			conn = this.getConnection();
			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, rotationId1);
			stmt.setInt(2, rotationId2);
			stmt.setString(3, recordEvent.getSport());
			stmt.setLong(4, recordEvent.getUserid());
			resultSet = stmt.executeQuery();
			final Long userid = recordEvent.getUserid();
			
			if (resultSet.next()) {
				if (userid != null && userid.longValue() == 18) {
					LOGGER.error("rotationId1: " + rotationId1);
					LOGGER.error("rotationId2: " + rotationId2);
					LOGGER.error("sport: " + recordEvent.getSport());
					LOGGER.error("userid: " + userid);
					LOGGER.error("dateCheck: " + dateCheck);
					LOGGER.error("Duplicate is TRUE");
				}
				LOGGER.debug("Duplicate is true");
				retValue = true;
			} else {
				if (userid != null && userid.longValue() == 18) {
					LOGGER.error("rotationId1: " + rotationId1);
					LOGGER.error("rotationId2: " + rotationId2);
					LOGGER.error("sport: " + recordEvent.getSport());
					LOGGER.error("userid: " + userid);
					LOGGER.error("dateCheck: " + dateCheck);
					LOGGER.error("Duplicate is FALSE");
				}
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
		
		LOGGER.info("Exiting checkDuplicateSpreadEvent()");
		return retValue;
	}

	/**
	 * 
	 * @param recordEvent
	 * @return
	 * @throws SQLException
	 */
	public boolean checkDuplicateReverseSpreadEvent(SpreadRecordEvent recordEvent) throws BatchException {
		LOGGER.info("Entering checkDuplicateReverseSpreadEvent()");
		boolean retValue = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {
			final String dateCheck = getDateQuery(recordEvent);
			LOGGER.debug("dateCheck: " + dateCheck);

			final StringBuffer sb = new StringBuffer(200);
			sb.append("SELECT sre.id, sre.spreadinputfirstone, sre.spreadinputsecondone FROM spreadrecordevents sre ")
					.append("WHERE (sre.rotationid = ? OR sre.rotationid = ?) ")
					.append("AND sre.sport = ? ")
					.append("AND sre.userid = ? ")
					.append(dateCheck);

			final Integer rotationId1 = recordEvent.getRotationid();
			Integer rotationId2 = null;

			// Check for even or odd
			if (rotationId1 % 2 == 0) {
				rotationId2 = rotationId1 - 1;
			} else {
				rotationId2 = rotationId1 + 1;
			}

			conn = this.getConnection();
			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, rotationId1);
			stmt.setInt(2, rotationId2);
			stmt.setString(3, recordEvent.getSport());
			stmt.setLong(4, recordEvent.getUserid());
			resultSet = stmt.executeQuery();

			while (resultSet.next()) {
				final String firstone = resultSet.getString("spreadinputfirstone");
				final String secondone = resultSet.getString("spreadinputsecondone");
				final String eventfirstone = recordEvent.getSpreadinputfirstone();
				final String eventsecondone = recordEvent.getSpreadinputsecondone();

				if (firstone != null && firstone.length() > 0 &&
					eventfirstone != null && eventfirstone.length() > 0) {
					retValue = true;
					final Long userid = recordEvent.getUserid();

					if (userid != null && userid.longValue() == 18) {
						LOGGER.error("rotationId1: " + rotationId1);
						LOGGER.error("rotationId2: " + rotationId2);
						LOGGER.error("sport: " + recordEvent.getSport());
						LOGGER.error("userid: " + userid);
						LOGGER.error("dateCheck: " + dateCheck);
						LOGGER.error("firstone: " + firstone);
						LOGGER.error("secondone: " + secondone);
						LOGGER.error("eventfirstone: " + eventfirstone);
						LOGGER.error("eventsecondone: " + eventsecondone);
						LOGGER.error("Duplicate is TRUE");
					}
				} else if (secondone != null && secondone.length() > 0 && 
						eventsecondone != null && eventsecondone.length() > 0) {
					retValue = true;
					final Long userid = recordEvent.getUserid();

					if (userid != null && userid.longValue() == 18) {
						LOGGER.error("rotationId1: " + rotationId1);
						LOGGER.error("rotationId2: " + rotationId2);
						LOGGER.error("sport: " + recordEvent.getSport());
						LOGGER.error("userid: " + userid);
						LOGGER.error("dateCheck: " + dateCheck);
						LOGGER.error("firstone: " + firstone);
						LOGGER.error("secondone: " + secondone);
						LOGGER.error("eventfirstone: " + eventfirstone);
						LOGGER.error("eventsecondone: " + eventsecondone);
						LOGGER.error("Duplicate is TRUE");
					}
				} else {
					final Long userid = recordEvent.getUserid();

					if (userid != null && userid.longValue() == 18) {
						LOGGER.error("rotationId1: " + rotationId1);
						LOGGER.error("rotationId2: " + rotationId2);
						LOGGER.error("sport: " + recordEvent.getSport());
						LOGGER.error("userid: " + userid);
						LOGGER.error("dateCheck: " + dateCheck);
						LOGGER.error("firstone: " + firstone);
						LOGGER.error("secondone: " + secondone);
						LOGGER.error("eventfirstone: " + eventfirstone);
						LOGGER.error("eventsecondone: " + eventsecondone);
						LOGGER.error("Duplicate is FALSE");
					}
				}
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
		
		LOGGER.info("Exiting checkDuplicateReverseSpreadEvent()");
		return retValue;
	}

	/**
	 * 
	 * @param recordEvent
	 * @return
	 * @throws BatchException
	 */
	public boolean checkDuplicateTotalEvent(TotalRecordEvent recordEvent) throws BatchException {
		LOGGER.info("Entering checkDuplicateTotalEvent()");
		boolean retValue = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {
			final String dateCheck = getDateQuery(recordEvent);

			final StringBuffer sb = new StringBuffer(200);
			sb.append("SELECT tre.id FROM totalrecordevents tre ")
				.append("WHERE (tre.rotationid = ? OR tre.rotationid = ?) ")
				.append("AND tre.sport = ? ")
				.append("AND tre.userid = ? ")
				.append(dateCheck);

			Integer rotationId1 = recordEvent.getRotationid();
			Integer rotationId2 = null;

			// Check for even or odd
			if (rotationId1 % 2 == 0) {
				rotationId2 = rotationId1 - 1;
			} else {
				rotationId2 = rotationId1 + 1;
			}

			conn = this.getConnection();
			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, rotationId1);
			stmt.setInt(2, rotationId2);
			stmt.setString(3, recordEvent.getSport());
			stmt.setLong(4, recordEvent.getUserid());
			resultSet = stmt.executeQuery();

			while (resultSet.next()) {
				retValue = true;
			}
			
			if (retValue) {
				final Long userid = recordEvent.getUserid();

				if (userid != null && userid.longValue() == 18) {
					LOGGER.error("rotationId1: " + rotationId1);
					LOGGER.error("rotationId2: " + rotationId2);
					LOGGER.error("sport: " + recordEvent.getSport());
					LOGGER.error("userid: " + userid);
					LOGGER.error("dateCheck: " + dateCheck);
					LOGGER.error("Duplicate is TRUE");
				}
			} else {
				final Long userid = recordEvent.getUserid();

				if (userid != null && userid.longValue() == 18) {
					LOGGER.error("rotationId1: " + rotationId1);
					LOGGER.error("rotationId2: " + rotationId2);
					LOGGER.error("sport: " + recordEvent.getSport());
					LOGGER.error("userid: " + userid);
					LOGGER.error("dateCheck: " + dateCheck);
					LOGGER.error("Duplicate is FALSE");
				}
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
		
		LOGGER.info("Exiting checkDuplicateTotalEvent()");
		return retValue;
	}

	/**
	 * 
	 * @param recordEvent
	 * @return
	 * @throws BatchException
	 */
	public boolean checkDuplicateReverseTotalEvent(TotalRecordEvent recordEvent) throws BatchException {
		LOGGER.info("Entering checkDuplicateReverseTotalEvent()");
		boolean retValue = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {
			final String dateCheck = getDateQuery(recordEvent);

			final StringBuffer sb = new StringBuffer(200);
			sb.append("SELECT tre.totalinputfirstone, tre.totalinputsecondone FROM totalrecordevents tre ")
			.append("WHERE (tre.rotationid = ? OR tre.rotationid = ?) ")
			.append("AND tre.sport = ? ")
			.append("AND tre.userid = ? ")
			.append(dateCheck);

			Integer rotationId1 = recordEvent.getRotationid();
			Integer rotationId2 = null;

			// Check for even or odd
			if (rotationId1 % 2 == 0) {
				rotationId2 = rotationId1 - 1;
			} else {
				rotationId2 = rotationId1 + 1;
			}

			conn = this.getConnection();
			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, rotationId1);
			stmt.setInt(2, rotationId2);
			stmt.setString(3, recordEvent.getSport());
			stmt.setLong(4, recordEvent.getUserid());
			resultSet = stmt.executeQuery();

			while (resultSet.next()) {
				final String firstone = resultSet.getString("totalinputfirstone");
				final String secondone = resultSet.getString("totalinputsecondone");
				final String eventfirstone = recordEvent.getTotalinputfirstone();
				final String eventsecondone = recordEvent.getTotalinputsecondone();
				
				if (firstone != null && firstone.length() > 0 &&
					eventfirstone != null && eventfirstone.length() > 0) {
					retValue = true;
					
					final Long userid = recordEvent.getUserid();

					if (userid != null && userid.longValue() == 18) {
						LOGGER.error("rotationId1: " + rotationId1);
						LOGGER.error("rotationId2: " + rotationId2);
						LOGGER.error("sport: " + recordEvent.getSport());
						LOGGER.error("userid: " + userid);
						LOGGER.error("dateCheck: " + dateCheck);
						LOGGER.error("firstone: " + firstone);
						LOGGER.error("secondone: " + secondone);
						LOGGER.error("eventfirstone: " + eventfirstone);
						LOGGER.error("eventsecondone: " + eventsecondone);
						LOGGER.error("Duplicate is TRUE");
					}
				} else if (secondone != null && secondone.length() > 0 && 
						eventsecondone != null && eventsecondone.length() > 0) {
					retValue = true;
					
					final Long userid = recordEvent.getUserid();

					if (userid != null && userid.longValue() == 18) {
						LOGGER.error("rotationId1: " + rotationId1);
						LOGGER.error("rotationId2: " + rotationId2);
						LOGGER.error("sport: " + recordEvent.getSport());
						LOGGER.error("userid: " + userid);
						LOGGER.error("dateCheck: " + dateCheck);
						LOGGER.error("firstone: " + firstone);
						LOGGER.error("secondone: " + secondone);
						LOGGER.error("eventfirstone: " + eventfirstone);
						LOGGER.error("eventsecondone: " + eventsecondone);
						LOGGER.error("Duplicate is TRUE");
					}
				} else {
					final Long userid = recordEvent.getUserid();

					if (userid != null && userid.longValue() == 18) {
						LOGGER.error("rotationId1: " + rotationId1);
						LOGGER.error("rotationId2: " + rotationId2);
						LOGGER.error("sport: " + recordEvent.getSport());
						LOGGER.error("userid: " + userid);
						LOGGER.error("dateCheck: " + dateCheck);
						LOGGER.error("firstone: " + firstone);
						LOGGER.error("secondone: " + secondone);
						LOGGER.error("eventfirstone: " + eventfirstone);
						LOGGER.error("eventsecondone: " + eventsecondone);
						LOGGER.error("Duplicate is FALSE");
					}
				}
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

		LOGGER.info("Exiting checkDuplicateReverseTotalEvent()");
		return retValue;
	}

	/**
	 * 
	 * @param recordEvent
	 * @return
	 * @throws BatchException
	 */
	public boolean checkDuplicateMlEvent(MlRecordEvent recordEvent) throws BatchException {
		LOGGER.info("Entering checkDuplicateMlEvent()");
		boolean retValue = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {
			final String dateCheck = getDateQuery(recordEvent);

			final StringBuffer sb = new StringBuffer(200);
			sb.append("SELECT mre.id FROM mlrecordevents mre ")
				.append("WHERE (mre.rotationid = ? OR mre.rotationid = ?) ")
				.append("AND mre.sport = ? ")
				.append("AND mre.userid = ? ")
				.append(dateCheck);

			Integer rotationId1 = recordEvent.getRotationid();
			Integer rotationId2 = null;

			// Check for even or odd
			if (rotationId1 % 2 == 0) {
				rotationId2 = rotationId1 - 1;
			} else {
				rotationId2 = rotationId1 + 1;
			}

			conn = this.getConnection();
			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, rotationId1);
			stmt.setInt(2, rotationId2);
			stmt.setString(3, recordEvent.getSport());
			stmt.setLong(4, recordEvent.getUserid());
			resultSet = stmt.executeQuery();
			
			if (resultSet.next()) {
				retValue = true;
				
				final Long userid = recordEvent.getUserid();

				if (userid != null && userid.longValue() == 18) {
					LOGGER.error("rotationId1: " + rotationId1);
					LOGGER.error("rotationId2: " + rotationId2);
					LOGGER.error("sport: " + recordEvent.getSport());
					LOGGER.error("userid: " + userid);
					LOGGER.error("dateCheck: " + dateCheck);
					LOGGER.error("Duplicate is TRUE");
				}
			} else {
				final Long userid = recordEvent.getUserid();

				if (userid != null && userid.longValue() == 18) {
					LOGGER.error("rotationId1: " + rotationId1);
					LOGGER.error("rotationId2: " + rotationId2);
					LOGGER.error("sport: " + recordEvent.getSport());
					LOGGER.error("userid: " + userid);
					LOGGER.error("dateCheck: " + dateCheck);
					LOGGER.error("Duplicate is FALSE");
				}
			}

			closeStreams(stmt, resultSet);
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
		
		LOGGER.info("Exiting checkDuplicateMlEvent()");
		return retValue;
	}

	/**
	 * 
	 * @param recordEvent
	 * @return
	 * @throws BatchException
	 */
	public boolean checkDuplicateReverseMlEvent(MlRecordEvent recordEvent) throws BatchException {
		LOGGER.info("Entering checkDuplicateReverseMlEvent()");
		boolean retValue = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {
			final String dateCheck = getDateQuery(recordEvent);

			final StringBuffer sb = new StringBuffer(200);
			sb.append("SELECT mre.id, mre.mlinputfirstone, mre.mlinputsecondone FROM mlrecordevents mre ")
				.append("WHERE (mre.rotationid = ? OR mre.rotationid = ?) ")
				.append("AND mre.sport = ? ")
				.append("AND mre.userid = ? ")
				.append(dateCheck);

			Integer rotationId1 = recordEvent.getRotationid();
			Integer rotationId2 = null;

			// Check for even or odd
			if (rotationId1 % 2 == 0) {
				rotationId2 = rotationId1 - 1;
			} else {
				rotationId2 = rotationId1 + 1;
			}

			conn = this.getConnection();
			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, rotationId1);
			stmt.setInt(2, rotationId2);
			stmt.setString(3, recordEvent.getSport());
			stmt.setLong(4, recordEvent.getUserid());
			resultSet = stmt.executeQuery();
			
			while (resultSet.next()) {
				final String firstone = resultSet.getString("mlinputfirstone");
				final String secondone = resultSet.getString("mlinputsecondone");
				final String eventfirstone = recordEvent.getMlinputfirstone();
				final String eventsecondone = recordEvent.getMlinputsecondone();
				
				if (firstone != null && firstone.length() > 0 &&
					eventfirstone != null && eventfirstone.length() > 0) {
					retValue = true;
					
					final Long userid = recordEvent.getUserid();

					if (userid != null && userid.longValue() == 18) {
						LOGGER.error("rotationId1: " + rotationId1);
						LOGGER.error("rotationId2: " + rotationId2);
						LOGGER.error("sport: " + recordEvent.getSport());
						LOGGER.error("userid: " + userid);
						LOGGER.error("dateCheck: " + dateCheck);
						LOGGER.error("firstone: " + firstone);
						LOGGER.error("secondone: " + secondone);
						LOGGER.error("eventfirstone: " + eventfirstone);
						LOGGER.error("eventsecondone: " + eventsecondone);
						LOGGER.error("Duplicate is TRUE");
					}
				} else if (secondone != null && secondone.length() > 0 && 
						eventsecondone != null && eventsecondone.length() > 0) {
					retValue = true;
					
					final Long userid = recordEvent.getUserid();

					if (userid != null && userid.longValue() == 18) {
						LOGGER.error("rotationId1: " + rotationId1);
						LOGGER.error("rotationId2: " + rotationId2);
						LOGGER.error("sport: " + recordEvent.getSport());
						LOGGER.error("userid: " + userid);
						LOGGER.error("dateCheck: " + dateCheck);
						LOGGER.error("firstone: " + firstone);
						LOGGER.error("secondone: " + secondone);
						LOGGER.error("eventfirstone: " + eventfirstone);
						LOGGER.error("eventsecondone: " + eventsecondone);
						LOGGER.error("Duplicate is TRUE");
					}
				} else {
					final Long userid = recordEvent.getUserid();

					if (userid != null && userid.longValue() == 18) {
						LOGGER.error("rotationId1: " + rotationId1);
						LOGGER.error("rotationId2: " + rotationId2);
						LOGGER.error("sport: " + recordEvent.getSport());
						LOGGER.error("userid: " + userid);
						LOGGER.error("dateCheck: " + dateCheck);
						LOGGER.error("firstone: " + firstone);
						LOGGER.error("secondone: " + secondone);
						LOGGER.error("eventfirstone: " + eventfirstone);
						LOGGER.error("eventsecondone: " + eventsecondone);
						LOGGER.error("Duplicate is FALSE");
					}
				}
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

		LOGGER.info("Exiting checkDuplicateReverseMlEvent()");
		return retValue;
	}

	/**
	 * 
	 * @param recordEvent
	 * @return
	 * @throws BatchException
	 */
	public Long setSpreadEvent(SpreadRecordEvent recordEvent) throws BatchException {
		LOGGER.info("Entering setSpreadEvent()");
		LOGGER.info("SpreadRecordEvent: " + recordEvent);
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		
		try {
			recordEvent.setId(null);
			StringBuffer sb = new StringBuffer(200);
			sb.append("INSERT into spreadrecordevents (accountid, amount, attempts, attempttime, ")
					.append("currentattempts, datecreated, datemodified, datentime, eventdatetime, ")
					.append("eventid, eventid1, eventid2, eventname, eventteam1, eventteam2, ")
					.append("eventtype, groupid, iscompleted, sport, userid, wtype, ")
					.append("spreadinputfirstone, spreadinputfirsttwo, ")
					.append("spreadinputjuicefirstone, spreadinputjuicefirsttwo, ")
					.append("spreadinputjuicesecondone, spreadinputjuicesecondtwo, ")
					.append("spreadinputsecondone, spreadinputsecondtwo, ")
					.append("spreadjuiceplusminusfirstone, spreadjuiceplusminusfirsttwo, ")
					.append("spreadjuiceplusminussecondone, spreadjuiceplusminussecondtwo, ")
					.append("spreadplusminusfirstone, spreadplusminusfirsttwo, ")
					.append("spreadplusminussecondone, spreadplusminussecondtwo, scrappername, actiontype, textnumber, rotationid) ")
					.append("VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ")
					.append("?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
	
			conn = this.getConnection();
			stmt = conn.prepareStatement(sb.toString(), Statement.RETURN_GENERATED_KEYS);
			stmt.setLong(1, checkLong(recordEvent.getAccountid()));
			stmt.setString(2, checkString(recordEvent.getAmount()));
			stmt.setInt(3, checkInteger(recordEvent.getAttempts()));
			stmt.setTimestamp(4, new java.sql.Timestamp(checkTimestamp(recordEvent.getAttempttime()).getTime()));
			stmt.setInt(5, checkInteger(recordEvent.getCurrentattempts()));
			stmt.setTimestamp(6, new java.sql.Timestamp(new Date().getTime()));
			stmt.setTimestamp(7, new java.sql.Timestamp(new Date().getTime()));
			stmt.setTimestamp(8, new java.sql.Timestamp(checkTimestamp(recordEvent.getDatentime()).getTime()));
			stmt.setTimestamp(9, new java.sql.Timestamp(checkTimestamp(recordEvent.getEventdatetime()).getTime()));
			stmt.setInt(10, checkInteger(recordEvent.getEventid()));
			stmt.setInt(11, checkInteger(recordEvent.getEventid1()));
			stmt.setInt(12, checkInteger(recordEvent.getEventid2()));
			stmt.setString(13, checkString(recordEvent.getEventname()));
			stmt.setString(14, checkString(recordEvent.getEventteam1()));
			stmt.setString(15, checkString(recordEvent.getEventteam2()));
			stmt.setString(16, checkString(recordEvent.getEventtype()));
			stmt.setLong(17, checkLong(recordEvent.getGroupid()));
			stmt.setBoolean(18, checkBoolean(recordEvent.getIscompleted()));
			stmt.setString(19, checkString(recordEvent.getSport()));
			stmt.setLong(20, checkLong(recordEvent.getUserid()));
			stmt.setString(21, checkString(recordEvent.getWtype()));
			
			stmt.setString(22, checkString(recordEvent.getSpreadinputfirstone()));
			stmt.setString(23, checkString(recordEvent.getSpreadinputfirsttwo()));
			stmt.setString(24, checkString(recordEvent.getSpreadinputjuicefirstone()));
			stmt.setString(25, checkString(recordEvent.getSpreadinputjuicefirsttwo()));
			stmt.setString(26, checkString(recordEvent.getSpreadinputjuicesecondone()));
			stmt.setString(27, checkString(recordEvent.getSpreadinputjuicesecondtwo()));
			stmt.setString(28, checkString(recordEvent.getSpreadinputsecondone()));
			stmt.setString(29, checkString(recordEvent.getSpreadinputsecondtwo()));
			stmt.setString(30, checkString(recordEvent.getSpreadjuiceplusminusfirstone()));
			stmt.setString(31, checkString(recordEvent.getSpreadjuiceplusminusfirsttwo()));
			stmt.setString(32, checkString(recordEvent.getSpreadjuiceplusminussecondone()));
			stmt.setString(33, checkString(recordEvent.getSpreadjuiceplusminussecondtwo()));
			stmt.setString(34, checkString(recordEvent.getSpreadplusminusfirstone()));
			stmt.setString(35, checkString(recordEvent.getSpreadplusminusfirsttwo()));
			stmt.setString(36, checkString(recordEvent.getSpreadplusminussecondone()));
			stmt.setString(37, checkString(recordEvent.getSpreadplusminussecondtwo()));
			stmt.setString(38, checkString(recordEvent.getScrappername()));
			stmt.setString(39, checkString(recordEvent.getActiontype()));
			stmt.setString(40, checkString(recordEvent.getTextnumber()));
			stmt.setInt(41, checkInteger(recordEvent.getRotationid()));
	
			stmt.executeUpdate();  
		    resultSet = stmt.getGeneratedKeys();  
		    resultSet.next();
		    long primaryKey = resultSet.getLong(1);
		    recordEvent.setId(primaryKey);
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

		LOGGER.info("Exiting setSpreadEvent()");
		return recordEvent.getId();
	}

	/**
	 * 
	 * @param recordEvent
	 * @return
	 * @throws BatchException
	 */
	public Long setTotalEvent(TotalRecordEvent recordEvent) throws BatchException {
		LOGGER.info("Entering setTotalEvent()");
		LOGGER.info("TotalRecordEvent: " + recordEvent);
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {
			recordEvent.setId(null);
			StringBuffer sb = new StringBuffer(200);
			sb.append("INSERT into totalrecordevents (accountid, amount, attempts, attempttime, ")
					.append("currentattempts, datecreated, datemodified, datentime, eventdatetime, ")
					.append("eventid, eventid1, eventid2, eventname, eventteam1, eventteam2, ")
					.append("eventtype, groupid, iscompleted, sport, userid, wtype, ")
					.append("totalinputfirstone, totalinputfirsttwo, ")
					.append("totalinputjuicefirstone, totalinputjuicefirsttwo, ")
					.append("totalinputjuicesecondone, totalinputjuicesecondtwo, ")
					.append("totalinputsecondone, totalinputsecondtwo, ")
					.append("totaljuiceplusminusfirstone, totaljuiceplusminusfirsttwo, ")
					.append("totaljuiceplusminussecondone, totaljuiceplusminussecondtwo, scrappername, actiontype, textnumber, rotationid) ")
					.append("VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ")
					.append("?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

			conn = this.getConnection();
			stmt = conn.prepareStatement(sb.toString(), Statement.RETURN_GENERATED_KEYS);
			stmt.setLong(1, checkLong(recordEvent.getAccountid()));
			stmt.setString(2, checkString(recordEvent.getAmount()));
			stmt.setInt(3, checkInteger(recordEvent.getAttempts()));
			stmt.setTimestamp(4, new java.sql.Timestamp(checkTimestamp(recordEvent.getAttempttime()).getTime()));
			stmt.setInt(5, checkInteger(recordEvent.getCurrentattempts()));
			stmt.setTimestamp(6, new java.sql.Timestamp(new Date().getTime()));
			stmt.setTimestamp(7, new java.sql.Timestamp(new Date().getTime()));
			stmt.setTimestamp(8, new java.sql.Timestamp(checkTimestamp(recordEvent.getDatentime()).getTime()));
			stmt.setTimestamp(9, new java.sql.Timestamp(checkTimestamp(recordEvent.getEventdatetime()).getTime()));
			stmt.setInt(10, checkInteger(recordEvent.getEventid()));
			stmt.setInt(11, checkInteger(recordEvent.getEventid1()));
			stmt.setInt(12, checkInteger(recordEvent.getEventid2()));
			stmt.setString(13, checkString(recordEvent.getEventname()));
			stmt.setString(14, checkString(recordEvent.getEventteam1()));
			stmt.setString(15, checkString(recordEvent.getEventteam2()));
			stmt.setString(16, checkString(recordEvent.getEventtype()));
			stmt.setLong(17, checkLong(recordEvent.getGroupid()));
			stmt.setBoolean(18, checkBoolean(recordEvent.getIscompleted()));
			stmt.setString(19, checkString(recordEvent.getSport()));
			stmt.setLong(20, checkLong(recordEvent.getUserid()));
			stmt.setString(21, checkString(recordEvent.getWtype()));
			
			stmt.setString(22, checkString(recordEvent.getTotalinputfirstone()));
			stmt.setString(23, checkString(recordEvent.getTotalinputfirsttwo()));
			stmt.setString(24, checkString(recordEvent.getTotalinputjuicefirstone()));
			stmt.setString(25, checkString(recordEvent.getTotalinputjuicefirsttwo()));
			stmt.setString(26, checkString(recordEvent.getTotalinputjuicesecondone()));
			stmt.setString(27, checkString(recordEvent.getTotalinputjuicesecondtwo()));
			stmt.setString(28, checkString(recordEvent.getTotalinputsecondone()));
			stmt.setString(29, checkString(recordEvent.getTotalinputsecondtwo()));
			stmt.setString(30, checkString(recordEvent.getTotaljuiceplusminusfirstone()));
			stmt.setString(31, checkString(recordEvent.getTotaljuiceplusminusfirsttwo()));
			stmt.setString(32, checkString(recordEvent.getTotaljuiceplusminussecondone()));
			stmt.setString(33, checkString(recordEvent.getTotaljuiceplusminussecondtwo()));
			stmt.setString(34, checkString(recordEvent.getScrappername()));
			stmt.setString(35, checkString(recordEvent.getActiontype()));
			stmt.setString(36, checkString(recordEvent.getTextnumber()));
			stmt.setInt(37, checkInteger(recordEvent.getRotationid()));

			stmt.executeUpdate();  
		    resultSet = stmt.getGeneratedKeys();  
		    resultSet.next();
		    long primaryKey = resultSet.getLong(1);
		    recordEvent.setId(primaryKey);
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

		LOGGER.info("Exiting setTotalEvent()");
		return recordEvent.getId();
	}

	/**
	 * 
	 * @param recordEvent
	 * @return
	 * @throws BatchException
	 */
	public Long setMlEvent(MlRecordEvent recordEvent) throws BatchException {
		LOGGER.info("Entering setMlEvent()");
		LOGGER.info("MlRecordEvent: " + recordEvent);
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {
			recordEvent.setId(null);
			StringBuffer sb = new StringBuffer(200);
			sb.append("INSERT into mlrecordevents (accountid, amount, attempts, attempttime, ")
					.append("currentattempts, datecreated, datemodified, datentime, eventdatetime, ")
					.append("eventid, eventid1, eventid2, eventname, eventteam1, eventteam2, ")
					.append("eventtype, groupid, iscompleted, sport, userid, wtype, ")
					.append("mlinputfirstone, mlinputfirsttwo, ").append("mlinputsecondone, mlinputsecondtwo, ")
					.append("mlplusminusfirstone, mlplusminusfirsttwo, ")
					.append("mlplusminussecondone, mlplusminussecondtwo, scrappername, actiontype, textnumber, rotationid) ")
					.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ")
					.append("?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

			conn = this.getConnection();
			stmt = conn.prepareStatement(sb.toString(), Statement.RETURN_GENERATED_KEYS);
			stmt.setLong(1, checkLong(recordEvent.getAccountid()));
			stmt.setString(2, checkString(recordEvent.getAmount()));
			stmt.setInt(3, checkInteger(recordEvent.getAttempts()));
			stmt.setTimestamp(4, new java.sql.Timestamp(checkTimestamp(recordEvent.getAttempttime()).getTime()));
			stmt.setInt(5, checkInteger(recordEvent.getCurrentattempts()));
			stmt.setTimestamp(6, new java.sql.Timestamp(new Date().getTime()));
			stmt.setTimestamp(7, new java.sql.Timestamp(new Date().getTime()));
			stmt.setTimestamp(8, new java.sql.Timestamp(checkTimestamp(recordEvent.getDatentime()).getTime()));
			stmt.setTimestamp(9, new java.sql.Timestamp(checkTimestamp(recordEvent.getEventdatetime()).getTime()));
			stmt.setInt(10, checkInteger(recordEvent.getEventid()));
			stmt.setInt(11, checkInteger(recordEvent.getEventid1()));
			stmt.setInt(12, checkInteger(recordEvent.getEventid2()));
			stmt.setString(13, checkString(recordEvent.getEventname()));
			stmt.setString(14, checkString(recordEvent.getEventteam1()));
			stmt.setString(15, checkString(recordEvent.getEventteam2()));
			stmt.setString(16, checkString(recordEvent.getEventtype()));
			stmt.setLong(17, checkLong(recordEvent.getGroupid()));
			stmt.setBoolean(18, checkBoolean(recordEvent.getIscompleted()));
			stmt.setString(19, checkString(recordEvent.getSport()));
			stmt.setLong(20, checkLong(recordEvent.getUserid()));
			stmt.setString(21, checkString(recordEvent.getWtype()));

			stmt.setString(22, checkString(recordEvent.getMlinputfirstone()));
			stmt.setString(23, checkString(recordEvent.getMlinputfirsttwo()));
			stmt.setString(24, checkString(recordEvent.getMlinputsecondone()));
			stmt.setString(25, checkString(recordEvent.getMlinputsecondtwo()));
			stmt.setString(26, checkString(recordEvent.getMlplusminusfirstone()));
			stmt.setString(27, checkString(recordEvent.getMlplusminusfirsttwo()));
			stmt.setString(28, checkString(recordEvent.getMlplusminussecondone()));
			stmt.setString(29, checkString(recordEvent.getMlplusminussecondtwo()));
			stmt.setString(30, checkString(recordEvent.getScrappername()));
			stmt.setString(31, checkString(recordEvent.getActiontype()));
			stmt.setString(32, checkString(recordEvent.getTextnumber()));
			stmt.setInt(33, checkInteger(recordEvent.getRotationid()));

			stmt.executeUpdate();
			resultSet = stmt.getGeneratedKeys();
			resultSet.next();
			long primaryKey = resultSet.getLong(1);
			recordEvent.setId(primaryKey);
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

		LOGGER.info("Exiting setMlEvent()");
		return recordEvent.getId();
	}

	/**
	 * 
	 * @param id
	 * @throws BatchException
	 */
	public void setCompleteSpreadEvent(Long id) throws BatchException {
		LOGGER.info("Entering setCompleteSpreadEvent()");
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {
			final StringBuffer sb = new StringBuffer(200);
			sb.append("UPDATE spreadrecordevents SET iscompleted = ? WHERE id = ?");

			conn = this.getConnection();
			stmt = conn.prepareStatement(sb.toString());
			stmt.setBoolean(1, true);
			stmt.setLong(2, id);
			stmt.executeUpdate();
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

		LOGGER.info("Exiting setCompleteSpreadEvent()");
	}

	/**
	 * 
	 * @param id
	 * @throws BatchException
	 */
	public void setCompleteTotalEvent(Long id) throws BatchException {
		LOGGER.info("Entering setCompleteTotalEvent()");
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {
			final StringBuffer sb = new StringBuffer(200);
			sb.append("UPDATE totalrecordevents SET iscompleted = ? WHERE id = ?");

			conn = this.getConnection();
			stmt = conn.prepareStatement(sb.toString());
			stmt.setBoolean(1, true);
			stmt.setLong(2, id);
			stmt.executeUpdate();
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

		LOGGER.info("Exiting setCompleteTotalEvent()");
	}

	/**
	 * 
	 * @param id
	 * @throws BatchException
	 */
	public void setCompleteMlEvent(Long id) throws BatchException {
		LOGGER.info("Entering setCompleteMlEvent()");
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {
			final StringBuffer sb = new StringBuffer(200);
			sb.append("UPDATE mlrecordevents SET iscompleted = ? WHERE id = ?");

			conn = this.getConnection();
			stmt = conn.prepareStatement(sb.toString());
			stmt.setBoolean(1, true);
			stmt.setLong(2, id);
			stmt.executeUpdate();
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

		LOGGER.info("Exiting setCompleteMlEvent()");
	}

	/**
	 * 
	 * @param spreadid
	 * @return
	 * @throws BatchException
	 */
	public List<AccountEvent> getSpreadActiveAccountEvents(Long spreadid) throws BatchException {
		LOGGER.info("Entering getSpreadActiveAccountEvents()");
		LOGGER.debug("spreadid: " + spreadid);
		final List<AccountEvent> accountEventList = new ArrayList<AccountEvent>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet resultSet = null;

		try {
			conn = this.getNoAutoConnection();
			stmt = conn.createStatement();
			resultSet = stmt.executeQuery(
					"SELECT * FROM accountevent where spreadid = " + spreadid + " AND iscompleted = false");

			// Loop through all the results
			while (resultSet.next()) {
				final AccountEvent accountEvent = new AccountEvent();
				accountEvent.setAccountconfirmation(resultSet.getString("accountconfirmation"));
				accountEvent.setAccesstoken(resultSet.getString("accesstoken"));
				accountEvent.setAccounthtml(resultSet.getString("accounthtml"));
				accountEvent.setAccountid(resultSet.getLong("accountid"));
				accountEvent.setAmount(resultSet.getString("amount"));
				accountEvent.setActualamount(resultSet.getString("actualamount"));
				accountEvent.setAttempts(resultSet.getInt("attempts"));
				accountEvent.setAttempttime(resultSet.getTimestamp("attempttime"));
				accountEvent.setCurrentattempts(resultSet.getInt("currentattempts"));
				accountEvent.setErrorcode(resultSet.getInt("errorcode"));
				accountEvent.setErrorexception(resultSet.getString("errorexception"));
				accountEvent.setErrormessage(resultSet.getString("errormessage"));
				accountEvent.setEventdatetime(resultSet.getTimestamp("eventdatetime"));
				accountEvent.setEventid(resultSet.getInt("eventid"));
				accountEvent.setEventname(resultSet.getString("eventname"));
				accountEvent.setGroupid(resultSet.getLong("groupid"));
				accountEvent.setId(resultSet.getLong("id"));
				accountEvent.setIscompleted(resultSet.getBoolean("iscompleted"));
				accountEvent.setMaxspreadamount(resultSet.getInt("maxspreadamount"));
				accountEvent.setMaxtotalamount(resultSet.getInt("maxtotalamount"));
				accountEvent.setMaxmlamount(resultSet.getInt("maxmlamount"));
				accountEvent.setMlindicator(resultSet.getString("mlindicator"));
				accountEvent.setTimezone(resultSet.getString("timezone"));
				accountEvent.setMl(resultSet.getFloat("ml"));
				accountEvent.setMlid(resultSet.getLong("mlid"));
				accountEvent.setMljuice(resultSet.getFloat("mljuice"));
				accountEvent.setName(resultSet.getString("name"));
				accountEvent.setOwnerpercentage(resultSet.getInt("ownerpercentage"));
				accountEvent.setPartnerpercentage(resultSet.getInt("partnerpercentage"));
				accountEvent.setProxy(resultSet.getString("proxy"));
				accountEvent.setSport(resultSet.getString("sport"));
				accountEvent.setSpreadindicator(resultSet.getString("spreadindicator"));
				accountEvent.setSpread(resultSet.getFloat("spread"));
				accountEvent.setSpreadid(resultSet.getLong("spreadid"));
				accountEvent.setSpreadjuice(resultSet.getFloat("spreadjuice"));
				accountEvent.setStatus(resultSet.getString("status"));
				accountEvent.setTotal(resultSet.getFloat("total"));
				accountEvent.setTotalid(resultSet.getLong("totalid"));
				accountEvent.setTotalindicator(resultSet.getString("totalindicator"));
				accountEvent.setTotaljuice(resultSet.getFloat("totaljuice"));
				accountEvent.setType(resultSet.getString("type"));
				accountEvent.setUserid(resultSet.getLong("userid"));
				accountEvent.setWagertype(resultSet.getString("wagertype"));
				accountEvent.setRiskamount(resultSet.getString("riskamount"));
				accountEvent.setTowinamount(resultSet.getString("towinamount"));
				accountEvent.setIscomplexcaptcha(resultSet.getBoolean("iscomplexcaptcha"));
				accountEvent.setHumanspeed(resultSet.getBoolean("humanspeed"));

				accountEventList.add(accountEvent);
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
				conn.commit();
				closeStreams(conn, stmt, resultSet);
			} catch (Throwable t) {
				LOGGER.error(t.getMessage(), t);
			}
		}

		LOGGER.info("Exiting getSpreadActiveAccountEvents()");
		return accountEventList;
	}

	/**
	 * 
	 * @param totalid
	 * @return
	 * @throws BatchException
	 */
	public List<AccountEvent> getTotalActiveAccountEvents(Long totalid) throws BatchException {
		LOGGER.info("Entering getTotalActiveAccountEvents()");
		LOGGER.debug("totalid: " + totalid);
		List<AccountEvent> accountEventList = new ArrayList<AccountEvent>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet resultSet = null;

		try {
			conn = this.getNoAutoConnection();
			stmt = conn.createStatement();
			resultSet = stmt.executeQuery("SELECT * FROM accountevent where totalid = " + totalid + " AND iscompleted = false");

			while (resultSet.next()) {
				final AccountEvent accountEvent = new AccountEvent();
				accountEvent.setAccountconfirmation(resultSet.getString("accountconfirmation"));
				accountEvent.setAccesstoken(resultSet.getString("accesstoken"));
				accountEvent.setAccounthtml(resultSet.getString("accounthtml"));
				accountEvent.setAccountid(resultSet.getLong("accountid"));
				accountEvent.setAmount(resultSet.getString("amount"));
				accountEvent.setActualamount(resultSet.getString("actualamount"));
				accountEvent.setAttempts(resultSet.getInt("attempts"));
				accountEvent.setAttempttime(resultSet.getTimestamp("attempttime"));
				accountEvent.setCurrentattempts(resultSet.getInt("currentattempts"));
				accountEvent.setErrorcode(resultSet.getInt("errorcode"));
				accountEvent.setErrorexception(resultSet.getString("errorexception"));
				accountEvent.setErrormessage(resultSet.getString("errormessage"));
				accountEvent.setEventdatetime(resultSet.getTimestamp("eventdatetime"));
				accountEvent.setEventid(resultSet.getInt("eventid"));
				accountEvent.setEventname(resultSet.getString("eventname"));
				accountEvent.setGroupid(resultSet.getLong("groupid"));
				accountEvent.setId(resultSet.getLong("id"));
				accountEvent.setIscompleted(resultSet.getBoolean("iscompleted"));
				accountEvent.setMaxspreadamount(resultSet.getInt("maxspreadamount"));
				accountEvent.setMaxtotalamount(resultSet.getInt("maxtotalamount"));
				accountEvent.setMaxmlamount(resultSet.getInt("maxmlamount"));
				accountEvent.setMlindicator(resultSet.getString("mlindicator"));
				accountEvent.setTimezone(resultSet.getString("timezone"));
				accountEvent.setMl(resultSet.getFloat("ml"));
				accountEvent.setMlid(resultSet.getLong("mlid"));
				accountEvent.setMljuice(resultSet.getFloat("mljuice"));
				accountEvent.setName(resultSet.getString("name"));
				accountEvent.setOwnerpercentage(resultSet.getInt("ownerpercentage"));
				accountEvent.setPartnerpercentage(resultSet.getInt("partnerpercentage"));
				accountEvent.setProxy(resultSet.getString("proxy"));
				accountEvent.setSport(resultSet.getString("sport"));
				accountEvent.setSpreadindicator(resultSet.getString("spreadindicator"));
				accountEvent.setSpread(resultSet.getFloat("spread"));
				accountEvent.setSpreadid(resultSet.getLong("spreadid"));
				accountEvent.setSpreadjuice(resultSet.getFloat("spreadjuice"));
				accountEvent.setStatus(resultSet.getString("status"));
				accountEvent.setTotal(resultSet.getFloat("total"));
				accountEvent.setTotalid(resultSet.getLong("totalid"));
				accountEvent.setTotalindicator(resultSet.getString("totalindicator"));
				accountEvent.setTotaljuice(resultSet.getFloat("totaljuice"));
				accountEvent.setType(resultSet.getString("type"));
				accountEvent.setUserid(resultSet.getLong("userid"));
				accountEvent.setWagertype(resultSet.getString("wagertype"));
				accountEvent.setRiskamount(resultSet.getString("riskamount"));
				accountEvent.setTowinamount(resultSet.getString("towinamount"));
				accountEvent.setIscomplexcaptcha(resultSet.getBoolean("iscomplexcaptcha"));
				accountEvent.setHumanspeed(resultSet.getBoolean("humanspeed"));

				accountEventList.add(accountEvent);
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
				conn.commit();
				closeStreams(conn, stmt, resultSet);
			} catch (Throwable t) {
				LOGGER.error(t.getMessage(), t);
			}
		}

		LOGGER.info("Exiting getTotalActiveAccountEvents()");
		return accountEventList;
	}

	/**
	 * 
	 * @param totalid
	 * @return
	 * @throws BatchException
	 */
	public List<AccountEvent> getTotalAccountEvents(Long totalid) throws BatchException {
		LOGGER.info("Entering getTotalAccountEvents()");
		LOGGER.debug("totalid: " + totalid);
		final List<AccountEvent> accountEventList = new ArrayList<AccountEvent>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet resultSet = null;

		try {
			conn = this.getNoAutoConnection();
			stmt = conn.createStatement();
			resultSet = stmt.executeQuery("SELECT * FROM accountevent where totalid = " + totalid);

			while (resultSet.next()) {
				final AccountEvent accountEvent = new AccountEvent();
				accountEvent.setAccountconfirmation(resultSet.getString("accountconfirmation"));
				accountEvent.setAccesstoken(resultSet.getString("accesstoken"));
				accountEvent.setAccounthtml(resultSet.getString("accounthtml"));
				accountEvent.setAccountid(resultSet.getLong("accountid"));
				accountEvent.setAmount(resultSet.getString("amount"));
				accountEvent.setActualamount(resultSet.getString("actualamount"));
				accountEvent.setAttempts(resultSet.getInt("attempts"));
				accountEvent.setAttempttime(resultSet.getTimestamp("attempttime"));
				accountEvent.setCurrentattempts(resultSet.getInt("currentattempts"));
				accountEvent.setErrorcode(resultSet.getInt("errorcode"));
				accountEvent.setErrorexception(resultSet.getString("errorexception"));
				accountEvent.setErrormessage(resultSet.getString("errormessage"));
				accountEvent.setEventdatetime(resultSet.getTimestamp("eventdatetime"));
				accountEvent.setEventid(resultSet.getInt("eventid"));
				accountEvent.setEventname(resultSet.getString("eventname"));
				accountEvent.setGroupid(resultSet.getLong("groupid"));
				accountEvent.setId(resultSet.getLong("id"));
				accountEvent.setIscompleted(resultSet.getBoolean("iscompleted"));
				accountEvent.setMaxspreadamount(resultSet.getInt("maxspreadamount"));
				accountEvent.setMaxtotalamount(resultSet.getInt("maxtotalamount"));
				accountEvent.setMaxmlamount(resultSet.getInt("maxmlamount"));
				accountEvent.setMlindicator(resultSet.getString("mlindicator"));
				accountEvent.setTimezone(resultSet.getString("timezone"));
				accountEvent.setMl(resultSet.getFloat("ml"));
				accountEvent.setMlid(resultSet.getLong("mlid"));
				accountEvent.setMljuice(resultSet.getFloat("mljuice"));
				accountEvent.setName(resultSet.getString("name"));
				accountEvent.setOwnerpercentage(resultSet.getInt("ownerpercentage"));
				accountEvent.setPartnerpercentage(resultSet.getInt("partnerpercentage"));
				accountEvent.setProxy(resultSet.getString("proxy"));
				accountEvent.setSport(resultSet.getString("sport"));
				accountEvent.setSpreadindicator(resultSet.getString("spreadindicator"));
				accountEvent.setSpread(resultSet.getFloat("spread"));
				accountEvent.setSpreadid(resultSet.getLong("spreadid"));
				accountEvent.setSpreadjuice(resultSet.getFloat("spreadjuice"));
				accountEvent.setStatus(resultSet.getString("status"));
				accountEvent.setTotal(resultSet.getFloat("total"));
				accountEvent.setTotalid(resultSet.getLong("totalid"));
				accountEvent.setTotalindicator(resultSet.getString("totalindicator"));
				accountEvent.setTotaljuice(resultSet.getFloat("totaljuice"));
				accountEvent.setType(resultSet.getString("type"));
				accountEvent.setUserid(resultSet.getLong("userid"));
				accountEvent.setWagertype(resultSet.getString("wagertype"));
				accountEvent.setRiskamount(resultSet.getString("riskamount"));
				accountEvent.setTowinamount(resultSet.getString("towinamount"));
				accountEvent.setIscomplexcaptcha(resultSet.getBoolean("iscomplexcaptcha"));
				accountEvent.setHumanspeed(resultSet.getBoolean("humanspeed"));

				accountEventList.add(accountEvent);
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
				conn.commit();
				closeStreams(conn, stmt, resultSet);
			} catch (Throwable t) {
				LOGGER.error(t.getMessage(), t);
			}
		}

		LOGGER.info("Exiting getTotalAccountEvents()");
		return accountEventList;
	}

	/**
	 * 
	 * @param mlid
	 * @return
	 * @throws SQLException
	 */
	public List<AccountEvent> getMlActiveAccountEvents(Long mlid) throws BatchException {
		LOGGER.info("Entering getMlActiveAccountEvents()");
		LOGGER.debug("mlid: " + mlid);
		final List<AccountEvent> accountEventList = new ArrayList<AccountEvent>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet resultSet = null;

		try {
			conn = this.getNoAutoConnection();
			stmt = conn.createStatement();
			resultSet = stmt.executeQuery("SELECT * FROM accountevent where mlid = " + mlid + " AND iscompleted = false");

			while (resultSet.next()) {
				final AccountEvent accountEvent = new AccountEvent();

				accountEvent.setAccountconfirmation(resultSet.getString("accountconfirmation"));
				accountEvent.setAccesstoken(resultSet.getString("accesstoken"));
				accountEvent.setAccounthtml(resultSet.getString("accounthtml"));
				accountEvent.setAccountid(resultSet.getLong("accountid"));
				accountEvent.setAmount(resultSet.getString("amount"));
				accountEvent.setActualamount(resultSet.getString("actualamount"));
				accountEvent.setAttempts(resultSet.getInt("attempts"));
				accountEvent.setAttempttime(resultSet.getTimestamp("attempttime"));
				accountEvent.setCurrentattempts(resultSet.getInt("currentattempts"));
				accountEvent.setErrorcode(resultSet.getInt("errorcode"));
				accountEvent.setErrorexception(resultSet.getString("errorexception"));
				accountEvent.setErrormessage(resultSet.getString("errormessage"));
				accountEvent.setEventdatetime(resultSet.getTimestamp("eventdatetime"));
				accountEvent.setEventid(resultSet.getInt("eventid"));
				accountEvent.setEventname(resultSet.getString("eventname"));
				accountEvent.setGroupid(resultSet.getLong("groupid"));
				accountEvent.setId(resultSet.getLong("id"));
				accountEvent.setIscompleted(resultSet.getBoolean("iscompleted"));
				accountEvent.setMaxspreadamount(resultSet.getInt("maxspreadamount"));
				accountEvent.setMaxtotalamount(resultSet.getInt("maxtotalamount"));
				accountEvent.setMaxmlamount(resultSet.getInt("maxmlamount"));
				accountEvent.setMlindicator(resultSet.getString("mlindicator"));
				accountEvent.setTimezone(resultSet.getString("timezone"));
				accountEvent.setMl(resultSet.getFloat("ml"));
				accountEvent.setMlid(resultSet.getLong("mlid"));
				accountEvent.setMljuice(resultSet.getFloat("mljuice"));
				accountEvent.setName(resultSet.getString("name"));
				accountEvent.setOwnerpercentage(resultSet.getInt("ownerpercentage"));
				accountEvent.setPartnerpercentage(resultSet.getInt("partnerpercentage"));
				accountEvent.setProxy(resultSet.getString("proxy"));
				accountEvent.setSport(resultSet.getString("sport"));
				accountEvent.setSpreadindicator(resultSet.getString("spreadindicator"));
				accountEvent.setSpread(resultSet.getFloat("spread"));
				accountEvent.setSpreadid(resultSet.getLong("spreadid"));
				accountEvent.setSpreadjuice(resultSet.getFloat("spreadjuice"));
				accountEvent.setStatus(resultSet.getString("status"));
				accountEvent.setTotal(resultSet.getFloat("total"));
				accountEvent.setTotalid(resultSet.getLong("totalid"));
				accountEvent.setTotalindicator(resultSet.getString("totalindicator"));
				accountEvent.setTotaljuice(resultSet.getFloat("totaljuice"));
				accountEvent.setType(resultSet.getString("type"));
				accountEvent.setUserid(resultSet.getLong("userid"));
				accountEvent.setWagertype(resultSet.getString("wagertype"));
				accountEvent.setRiskamount(resultSet.getString("riskamount"));
				accountEvent.setTowinamount(resultSet.getString("towinamount"));
				accountEvent.setIscomplexcaptcha(resultSet.getBoolean("iscomplexcaptcha"));
				accountEvent.setHumanspeed(resultSet.getBoolean("humanspeed"));

				accountEventList.add(accountEvent);
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
				conn.commit();
				closeStreams(conn, stmt, resultSet);
			} catch (Throwable t) {
				LOGGER.error(t.getMessage(), t);
			}
		}

		LOGGER.info("Exiting getMlActiveAccountEvents()");
		return accountEventList;
	}

	/**
	 * 
	 * @param mlid
	 * @return
	 * @throws SQLException
	 */
	public List<AccountEvent> getMlAccountEvents(Long mlid) throws BatchException {
		LOGGER.info("Entering getMlAccountEvents()");
		LOGGER.debug("mlid: " + mlid);
		final List<AccountEvent> accountEventList = new ArrayList<AccountEvent>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet resultSet = null;

		try {
			conn = this.getNoAutoConnection();
			stmt = conn.createStatement();
			resultSet = stmt.executeQuery("SELECT * FROM accountevent where mlid = " + mlid);

			while (resultSet.next()) {
				final AccountEvent accountEvent = new AccountEvent();

				accountEvent.setAccountconfirmation(resultSet.getString("accountconfirmation"));
				accountEvent.setAccesstoken(resultSet.getString("accesstoken"));
				accountEvent.setAccounthtml(resultSet.getString("accounthtml"));
				accountEvent.setAccountid(resultSet.getLong("accountid"));
				accountEvent.setAmount(resultSet.getString("amount"));
				accountEvent.setActualamount(resultSet.getString("actualamount"));
				accountEvent.setAttempts(resultSet.getInt("attempts"));
				accountEvent.setAttempttime(resultSet.getTimestamp("attempttime"));
				accountEvent.setCurrentattempts(resultSet.getInt("currentattempts"));
				accountEvent.setErrorcode(resultSet.getInt("errorcode"));
				accountEvent.setErrorexception(resultSet.getString("errorexception"));
				accountEvent.setErrormessage(resultSet.getString("errormessage"));
				accountEvent.setEventdatetime(resultSet.getTimestamp("eventdatetime"));
				accountEvent.setEventid(resultSet.getInt("eventid"));
				accountEvent.setEventname(resultSet.getString("eventname"));
				accountEvent.setGroupid(resultSet.getLong("groupid"));
				accountEvent.setId(resultSet.getLong("id"));
				accountEvent.setIscompleted(resultSet.getBoolean("iscompleted"));
				accountEvent.setMaxspreadamount(resultSet.getInt("maxspreadamount"));
				accountEvent.setMaxtotalamount(resultSet.getInt("maxtotalamount"));
				accountEvent.setMaxmlamount(resultSet.getInt("maxmlamount"));
				accountEvent.setMlindicator(resultSet.getString("mlindicator"));
				accountEvent.setTimezone(resultSet.getString("timezone"));
				accountEvent.setMl(resultSet.getFloat("ml"));
				accountEvent.setMlid(resultSet.getLong("mlid"));
				accountEvent.setMljuice(resultSet.getFloat("mljuice"));
				accountEvent.setName(resultSet.getString("name"));
				accountEvent.setOwnerpercentage(resultSet.getInt("ownerpercentage"));
				accountEvent.setPartnerpercentage(resultSet.getInt("partnerpercentage"));
				accountEvent.setProxy(resultSet.getString("proxy"));
				accountEvent.setSport(resultSet.getString("sport"));
				accountEvent.setSpreadindicator(resultSet.getString("spreadindicator"));
				accountEvent.setSpread(resultSet.getFloat("spread"));
				accountEvent.setSpreadid(resultSet.getLong("spreadid"));
				accountEvent.setSpreadjuice(resultSet.getFloat("spreadjuice"));
				accountEvent.setStatus(resultSet.getString("status"));
				accountEvent.setTotal(resultSet.getFloat("total"));
				accountEvent.setTotalid(resultSet.getLong("totalid"));
				accountEvent.setTotalindicator(resultSet.getString("totalindicator"));
				accountEvent.setTotaljuice(resultSet.getFloat("totaljuice"));
				accountEvent.setType(resultSet.getString("type"));
				accountEvent.setUserid(resultSet.getLong("userid"));
				accountEvent.setWagertype(resultSet.getString("wagertype"));
				accountEvent.setRiskamount(resultSet.getString("riskamount"));
				accountEvent.setTowinamount(resultSet.getString("towinamount"));
				accountEvent.setIscomplexcaptcha(resultSet.getBoolean("iscomplexcaptcha"));
				accountEvent.setHumanspeed(resultSet.getBoolean("humanspeed"));

				accountEventList.add(accountEvent);
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
				conn.commit();
				closeStreams(conn, stmt, resultSet);
			} catch (Throwable t) {
				LOGGER.error(t.getMessage(), t);
			}
		}

		LOGGER.info("Exiting getMlAccountEvents()");
		return accountEventList;
	}

	/**
	 * 
	 * @param accountEvent
	 * @throws BatchException
	 */
	public void setupAccountEvent(AccountEvent accountEvent) throws BatchException {
		LOGGER.info("Entering setupAccountEvent()");
		LOGGER.debug("AccountEvent: " + accountEvent);
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {
			final StringBuffer sb = new StringBuffer(1000);
			final Date dateModified = new java.util.Date();
			sb.append("INSERT into accountevent (accountid, amount, actualamount, attempts, ")
					.append("datecreated, datemodified, eventdatetime, ")
					.append("eventid, eventname, ")
					.append("groupid, iscompleted, ")
					.append("maxspreadamount, maxtotalamount, maxmlamount, ")
					.append("mlindicator, timezone, ml, mlid, mljuice, ")
					.append("name, ownerpercentage, partnerpercentage, ")
					.append("proxy, sport, ")
					.append("spreadindicator, spread, spreadid, spreadjuice, status, ")
					.append("total, totalid, totalindicator, totaljuice, ")
					.append("type, userid, wagertype, accesstoken, errorcode, errorexception, errormessage, iscomplexcaptcha, humanspeed) ")
					.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ")
					.append("?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

			conn = this.getNoAutoConnection();
			stmt = conn.prepareStatement(sb.toString(), Statement.RETURN_GENERATED_KEYS);
			stmt.setLong(1, checkLong(accountEvent.getAccountid()));
			stmt.setString(2, checkString(accountEvent.getAmount()));
			stmt.setString(3, checkString(accountEvent.getActualamount()));
			stmt.setInt(4, checkInteger(accountEvent.getAttempts()));
			stmt.setTimestamp(5, new java.sql.Timestamp(checkTimestamp(dateModified).getTime()));
			stmt.setTimestamp(6, new java.sql.Timestamp(checkTimestamp(dateModified).getTime()));
			stmt.setTimestamp(7, new java.sql.Timestamp(checkTimestamp(accountEvent.getEventdatetime()).getTime()));
			stmt.setInt(8, checkInteger(accountEvent.getEventid()));
			stmt.setString(9, checkString(accountEvent.getEventname()));
			stmt.setLong(10, checkLong(accountEvent.getGroupid()));
			stmt.setBoolean(11, checkBoolean(accountEvent.getIscompleted()));
			stmt.setInt(12, checkInteger(accountEvent.getMaxspreadamount()));
			stmt.setInt(13, checkInteger(accountEvent.getMaxtotalamount()));
			stmt.setInt(14, checkInteger(accountEvent.getMaxmlamount()));
			stmt.setString(15, checkString(accountEvent.getMlindicator()));
			stmt.setString(16, checkString(accountEvent.getTimezone()));
			stmt.setFloat(17, checkFloat(accountEvent.getMl()));
			stmt.setLong(18, checkLong(accountEvent.getMlid()));
			stmt.setFloat(19, checkFloat(accountEvent.getMljuice()));
			stmt.setString(20, checkString(accountEvent.getName()));
			stmt.setInt(21, checkInteger(accountEvent.getOwnerpercentage()));
			stmt.setInt(22, checkInteger(accountEvent.getPartnerpercentage()));
			stmt.setString(23, checkString(accountEvent.getProxy()));
			stmt.setString(24, checkString(accountEvent.getSport()));
			stmt.setString(25, checkString(accountEvent.getSpreadindicator()));
			stmt.setFloat(26, checkFloat(accountEvent.getSpread()));
			stmt.setLong(27, checkLong(accountEvent.getSpreadid()));
			stmt.setFloat(28, checkFloat(accountEvent.getSpreadjuice()));
			stmt.setString(29, checkString(accountEvent.getStatus()));
			stmt.setFloat(30, checkFloat(accountEvent.getTotal()));
			stmt.setFloat(31, checkLong(accountEvent.getTotalid()));
			stmt.setString(32, checkString(accountEvent.getTotalindicator()));
			stmt.setFloat(33, checkFloat(accountEvent.getTotaljuice()));
			stmt.setString(34, checkString(accountEvent.getType()));
			stmt.setLong(35, checkLong(accountEvent.getUserid()));
			stmt.setString(36, checkString(accountEvent.getWagertype()));
			stmt.setString(37, checkString(accountEvent.getAccesstoken()));
			stmt.setInt(38, checkInteger(accountEvent.getErrorcode()));
			stmt.setString(39, checkString(accountEvent.getErrorexception()));
			stmt.setString(40, checkString(accountEvent.getErrormessage()));
			stmt.setBoolean(41, checkBoolean(accountEvent.getIscomplexcaptcha()));
			stmt.setBoolean(42, checkBoolean(accountEvent.getHumanspeed()));

			// Run the update query
			stmt.executeUpdate();
			resultSet = stmt.getGeneratedKeys();
			resultSet.next();
			long primaryKey = resultSet.getLong(1);
			accountEvent.setId(primaryKey);
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
				conn.commit();
				closeStreams(conn, stmt, resultSet);
			} catch (Throwable t) {
				LOGGER.error(t.getMessage(), t);
			}
		}

		LOGGER.info("Exiting setupAccountEvent()");		
	}

	/**
	 * 
	 * @param accountEventId
	 * @return
	 * @throws BatchException
	 */
	public AccountEvent getAccountEvent(Long accountEventId) throws BatchException {
		LOGGER.info("Entering getAccountEvent()");
		LOGGER.debug("accountEventId: " + accountEventId);
		AccountEvent accountEvent = null;
		Connection conn = null;
		Statement stmt = null;
		ResultSet resultSet = null;

		try {
			conn = this.getNoAutoConnection();
			stmt = conn.createStatement();
			resultSet = stmt.executeQuery("SELECT * FROM accountevent where id = " + accountEventId);

			while (resultSet.next()) {
				accountEvent = new AccountEvent();

				accountEvent.setAccountconfirmation(resultSet.getString("accountconfirmation"));
				accountEvent.setAccesstoken(resultSet.getString("accesstoken"));
				accountEvent.setAccounthtml(resultSet.getString("accounthtml"));
				accountEvent.setAccountid(resultSet.getLong("accountid"));
				accountEvent.setAmount(resultSet.getString("amount"));
				accountEvent.setActualamount(resultSet.getString("actualamount"));
				accountEvent.setAttempts(resultSet.getInt("attempts"));
				accountEvent.setAttempttime(resultSet.getTimestamp("attempttime"));
				accountEvent.setCurrentattempts(resultSet.getInt("currentattempts"));
				accountEvent.setErrorcode(resultSet.getInt("errorcode"));
				accountEvent.setErrorexception(resultSet.getString("errorexception"));
				accountEvent.setErrormessage(resultSet.getString("errormessage"));
				accountEvent.setEventdatetime(resultSet.getTimestamp("eventdatetime"));
				accountEvent.setEventid(resultSet.getInt("eventid"));
				accountEvent.setEventname(resultSet.getString("eventname"));
				accountEvent.setGroupid(resultSet.getLong("groupid"));
				accountEvent.setId(resultSet.getLong("id"));
				accountEvent.setIscompleted(resultSet.getBoolean("iscompleted"));
				accountEvent.setMaxspreadamount(resultSet.getInt("maxspreadamount"));
				accountEvent.setMaxtotalamount(resultSet.getInt("maxtotalamount"));
				accountEvent.setMaxmlamount(resultSet.getInt("maxmlamount"));
				accountEvent.setMlindicator(resultSet.getString("mlindicator"));
				accountEvent.setTimezone(resultSet.getString("timezone"));
				accountEvent.setMl(resultSet.getFloat("ml"));
				accountEvent.setMlid(resultSet.getLong("mlid"));
				accountEvent.setMljuice(resultSet.getFloat("mljuice"));
				accountEvent.setName(resultSet.getString("name"));
				accountEvent.setOwnerpercentage(resultSet.getInt("ownerpercentage"));
				accountEvent.setPartnerpercentage(resultSet.getInt("partnerpercentage"));
				accountEvent.setProxy(resultSet.getString("proxy"));
				accountEvent.setSport(resultSet.getString("sport"));
				accountEvent.setSpreadindicator(resultSet.getString("spreadindicator"));
				accountEvent.setSpread(resultSet.getFloat("spread"));
				accountEvent.setSpreadid(resultSet.getLong("spreadid"));
				accountEvent.setSpreadjuice(resultSet.getFloat("spreadjuice"));
				accountEvent.setStatus(resultSet.getString("status"));
				accountEvent.setTotal(resultSet.getFloat("total"));
				accountEvent.setTotalid(resultSet.getLong("totalid"));
				accountEvent.setTotalindicator(resultSet.getString("totalindicator"));
				accountEvent.setTotaljuice(resultSet.getFloat("totaljuice"));
				accountEvent.setType(resultSet.getString("type"));
				accountEvent.setUserid(resultSet.getLong("userid"));
				accountEvent.setWagertype(resultSet.getString("wagertype"));
				accountEvent.setIscomplexcaptcha(resultSet.getBoolean("iscomplexcaptcha"));
				accountEvent.setHumanspeed(resultSet.getBoolean("humanspeed"));
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
				conn.commit();
				closeStreams(conn, stmt, resultSet);
			} catch (Throwable t) {
				LOGGER.error(t.getMessage(), t);
			}
		}

		LOGGER.info("Exiting getAccountEvent()");
		return accountEvent;
	}

	/**
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public SpreadRecordEvent getSpreadEvent(Long id) throws BatchException {
		LOGGER.info("Entering getSpreadEvent()");
		LOGGER.info("id: "+ id);
		SpreadRecordEvent spreadRecordEvent = null;
		Connection conn = null;
		Statement stmt = null;
		ResultSet resultSet = null;

		try {
			conn = this.getConnection();
			stmt = conn.createStatement();
			resultSet = stmt.executeQuery("select * from spreadrecordevents where id = " + id);
	
			if (resultSet.next()) {
				spreadRecordEvent = new SpreadRecordEvent();

				// Common
				spreadRecordEvent.setId(resultSet.getLong("id"));
				spreadRecordEvent.setEventname(resultSet.getString("eventname"));
				spreadRecordEvent.setEventtype(resultSet.getString("eventtype"));
				spreadRecordEvent.setSport(resultSet.getString("sport"));
				spreadRecordEvent.setUserid(resultSet.getLong("userid"));
				spreadRecordEvent.setAccountid(resultSet.getLong("accountid"));
				spreadRecordEvent.setGroupid(resultSet.getLong("groupid"));
				spreadRecordEvent.setEventid(resultSet.getInt("eventid"));
				spreadRecordEvent.setEventid1(resultSet.getInt("eventid1"));
				spreadRecordEvent.setEventid2(resultSet.getInt("eventid2"));
				spreadRecordEvent.setEventteam1(resultSet.getString("eventteam1"));
				spreadRecordEvent.setEventteam2(resultSet.getString("eventteam2"));
				spreadRecordEvent.setAmount(resultSet.getString("amount"));
				spreadRecordEvent.setWtype(resultSet.getString("wtype"));
				spreadRecordEvent.setIscompleted(resultSet.getBoolean("iscompleted"));
				spreadRecordEvent.setAttempts(resultSet.getInt("attempts"));
				spreadRecordEvent.setCurrentattempts(resultSet.getInt("currentattempts"));
				spreadRecordEvent.setAttempttime(resultSet.getDate("attempttime"));
				spreadRecordEvent.setEventdatetime(resultSet.getTimestamp("eventdatetime"));
				spreadRecordEvent.setDatentime(resultSet.getTimestamp("datentime"));
				spreadRecordEvent.setDatecreated(resultSet.getTimestamp("datecreated"));
				spreadRecordEvent.setDatemodified(resultSet.getTimestamp("datemodified"));
				spreadRecordEvent.setScrappername(resultSet.getString("scrappername"));
				spreadRecordEvent.setActiontype(resultSet.getString("actiontype"));
				spreadRecordEvent.setTextnumber(resultSet.getString("textnumber"));
				spreadRecordEvent.setRotationid(resultSet.getInt("rotationid"));

				// Spread only
				spreadRecordEvent.setSpreadinputfirstone(resultSet.getString("spreadinputfirstone"));
				spreadRecordEvent.setSpreadplusminusfirstone(resultSet.getString("spreadplusminusfirstone"));
				spreadRecordEvent.setSpreadinputjuicefirstone(resultSet.getString("spreadinputjuicefirstone"));
				spreadRecordEvent.setSpreadjuiceplusminusfirstone(resultSet.getString("spreadjuiceplusminusfirstone"));
				spreadRecordEvent.setSpreadinputfirsttwo(resultSet.getString("spreadinputfirsttwo"));
				spreadRecordEvent.setSpreadplusminusfirsttwo(resultSet.getString("spreadplusminusfirsttwo"));
				spreadRecordEvent.setSpreadinputjuicefirsttwo(resultSet.getString("spreadinputjuicefirsttwo"));
				spreadRecordEvent.setSpreadjuiceplusminusfirsttwo(resultSet.getString("spreadjuiceplusminusfirsttwo"));
				spreadRecordEvent.setSpreadinputsecondone(resultSet.getString("spreadinputsecondone"));
				spreadRecordEvent.setSpreadplusminussecondone(resultSet.getString("spreadplusminussecondone"));
				spreadRecordEvent.setSpreadinputjuicesecondone(resultSet.getString("spreadinputjuicesecondone"));
				spreadRecordEvent.setSpreadjuiceplusminussecondone(resultSet.getString("spreadjuiceplusminussecondone"));
				spreadRecordEvent.setSpreadinputsecondtwo(resultSet.getString("spreadinputsecondtwo"));
				spreadRecordEvent.setSpreadplusminussecondtwo(resultSet.getString("spreadplusminussecondtwo"));
				spreadRecordEvent.setSpreadinputjuicesecondtwo(resultSet.getString("spreadinputjuicesecondtwo"));
				spreadRecordEvent.setSpreadjuiceplusminussecondtwo(resultSet.getString("spreadjuiceplusminussecondtwo"));
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

		LOGGER.info("Entering getSpreadEvent()");
		return spreadRecordEvent;
	}

	/**
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public TotalRecordEvent getTotalEvent(Long id) throws BatchException {
		LOGGER.info("Entering getTotalEvent()");
		LOGGER.info("id: "+ id);
		TotalRecordEvent totalRecordEvent = null;
		Connection conn = null;
		Statement stmt = null;
		ResultSet resultSet = null;

		try {
			conn = this.getConnection();
			stmt = conn.createStatement();
			resultSet = stmt.executeQuery("select * from totalrecordevents where id = " + id);
	
			if (resultSet.next()) {
				totalRecordEvent = new TotalRecordEvent();

				// Common
				totalRecordEvent.setId(resultSet.getLong("id"));
				totalRecordEvent.setEventname(resultSet.getString("eventname"));
				totalRecordEvent.setEventtype(resultSet.getString("eventtype"));
				totalRecordEvent.setSport(resultSet.getString("sport"));
				totalRecordEvent.setUserid(resultSet.getLong("userid"));
				totalRecordEvent.setAccountid(resultSet.getLong("accountid"));
				totalRecordEvent.setGroupid(resultSet.getLong("groupid"));
				totalRecordEvent.setEventid(resultSet.getInt("eventid"));
				totalRecordEvent.setEventid1(resultSet.getInt("eventid1"));
				totalRecordEvent.setEventid2(resultSet.getInt("eventid2"));
				totalRecordEvent.setEventteam1(resultSet.getString("eventteam1"));
				totalRecordEvent.setEventteam2(resultSet.getString("eventteam2"));
				totalRecordEvent.setAmount(resultSet.getString("amount"));
				totalRecordEvent.setWtype(resultSet.getString("wtype"));
				totalRecordEvent.setIscompleted(resultSet.getBoolean("iscompleted"));
				totalRecordEvent.setAttempts(resultSet.getInt("attempts"));
				totalRecordEvent.setCurrentattempts(resultSet.getInt("currentattempts"));
				totalRecordEvent.setAttempttime(resultSet.getTimestamp("attempttime"));
				totalRecordEvent.setEventdatetime(resultSet.getTimestamp("eventdatetime"));
				totalRecordEvent.setDatentime(resultSet.getTimestamp("datentime"));
				totalRecordEvent.setDatecreated(resultSet.getTimestamp("datecreated"));
				totalRecordEvent.setDatemodified(resultSet.getTimestamp("datemodified"));
				totalRecordEvent.setScrappername(resultSet.getString("scrappername"));
				totalRecordEvent.setActiontype(resultSet.getString("actiontype"));
				totalRecordEvent.setTextnumber(resultSet.getString("textnumber"));
				totalRecordEvent.setRotationid(resultSet.getInt("rotationid"));

				// Total only
				totalRecordEvent.setTotalinputfirstone(resultSet.getString("totalinputfirstone"));
				totalRecordEvent.setTotaljuiceplusminusfirstone(resultSet.getString("totaljuiceplusminusfirstone"));
				totalRecordEvent.setTotalinputjuicefirstone(resultSet.getString("totalinputjuicefirstone"));
				totalRecordEvent.setTotalinputfirsttwo(resultSet.getString("totalinputfirsttwo"));
				totalRecordEvent.setTotaljuiceplusminusfirsttwo(resultSet.getString("totaljuiceplusminusfirsttwo"));
				totalRecordEvent.setTotalinputjuicefirsttwo(resultSet.getString("totalinputjuicefirsttwo"));
				totalRecordEvent.setTotalinputsecondone(resultSet.getString("totalinputsecondone"));
				totalRecordEvent.setTotaljuiceplusminussecondone(resultSet.getString("totaljuiceplusminussecondone"));
				totalRecordEvent.setTotalinputjuicesecondone(resultSet.getString("totalinputjuicesecondone"));
				totalRecordEvent.setTotalinputsecondtwo(resultSet.getString("totalinputsecondtwo"));
				totalRecordEvent.setTotaljuiceplusminussecondtwo(resultSet.getString("totaljuiceplusminussecondtwo"));
				totalRecordEvent.setTotalinputjuicesecondtwo(resultSet.getString("totalinputjuicesecondtwo"));
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

		LOGGER.info("Exiting getTotalEvent()");
		return totalRecordEvent;
	}

	/**
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public MlRecordEvent getMlEvent(Long id) throws BatchException {
		LOGGER.info("Entering getMlEvent()");
		LOGGER.info("id: "+ id);
		MlRecordEvent mlRecordEvent = null;
		Connection conn = null;
		Statement stmt = null;
		ResultSet resultSet = null;

		try {
			conn = this.getConnection();
			stmt = conn.createStatement();
			resultSet = stmt.executeQuery("select * from mlrecordevents where id = " + id);
	
			if (resultSet.next()) {
				mlRecordEvent = new MlRecordEvent();

				// Common
				mlRecordEvent.setId(resultSet.getLong("id"));
				mlRecordEvent.setEventname(resultSet.getString("eventname"));
				mlRecordEvent.setEventtype(resultSet.getString("eventtype"));
				mlRecordEvent.setSport(resultSet.getString("sport"));
				mlRecordEvent.setUserid(resultSet.getLong("userid"));
				mlRecordEvent.setAccountid(resultSet.getLong("accountid"));
				mlRecordEvent.setGroupid(resultSet.getLong("groupid"));
				mlRecordEvent.setEventid(resultSet.getInt("eventid"));
				mlRecordEvent.setEventid1(resultSet.getInt("eventid1"));
				mlRecordEvent.setEventid2(resultSet.getInt("eventid2"));
				mlRecordEvent.setEventteam1(resultSet.getString("eventteam1"));
				mlRecordEvent.setEventteam2(resultSet.getString("eventteam2"));
				mlRecordEvent.setAmount(resultSet.getString("amount"));
				mlRecordEvent.setWtype(resultSet.getString("wtype"));
				mlRecordEvent.setIscompleted(resultSet.getBoolean("iscompleted"));
				mlRecordEvent.setAttempts(resultSet.getInt("attempts"));
				mlRecordEvent.setCurrentattempts(resultSet.getInt("currentattempts"));
				mlRecordEvent.setAttempttime(resultSet.getTimestamp("attempttime"));
				mlRecordEvent.setEventdatetime(resultSet.getTimestamp("eventdatetime"));
				mlRecordEvent.setDatentime(resultSet.getTimestamp("datentime"));
				mlRecordEvent.setDatecreated(resultSet.getTimestamp("datecreated"));
				mlRecordEvent.setDatemodified(resultSet.getTimestamp("datemodified"));
				mlRecordEvent.setScrappername(resultSet.getString("scrappername"));
				mlRecordEvent.setActiontype(resultSet.getString("actiontype"));
				mlRecordEvent.setTextnumber(resultSet.getString("textnumber"));
				mlRecordEvent.setRotationid(resultSet.getInt("rotationid"));

				// ML only
				mlRecordEvent.setMlinputfirstone(resultSet.getString("mlinputfirstone"));
				mlRecordEvent.setMlplusminusfirstone(resultSet.getString("mlplusminusfirstone"));
				mlRecordEvent.setMlinputfirsttwo(resultSet.getString("mlinputfirsttwo"));
				mlRecordEvent.setMlplusminusfirsttwo(resultSet.getString("mlplusminusfirsttwo"));
				mlRecordEvent.setMlinputsecondone(resultSet.getString("mlinputsecondone"));
				mlRecordEvent.setMlplusminussecondone(resultSet.getString("mlplusminussecondone"));
				mlRecordEvent.setMlinputsecondtwo(resultSet.getString("mlinputsecondtwo"));
				mlRecordEvent.setMlplusminussecondtwo(resultSet.getString("mlplusminussecondtwo"));
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

		LOGGER.info("Exiting getMlEvent()");
		return mlRecordEvent;
	}

	/**
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public Accounts getAccount(Long id) throws BatchException {
		LOGGER.info("Entering getAccount()");
		LOGGER.info("id: "+ id);
		Accounts account = null;
		Connection conn = null;
		Statement stmt = null;
		ResultSet resultSet = null;

		try {
			conn = this.getConnection();
			stmt = conn.createStatement();
			resultSet = stmt.executeQuery("select * from accountsta where id = " + id);
	
			if (resultSet.next()) {
				account = new Accounts();

				account.setId(resultSet.getLong("id"));
				account.setName(resultSet.getString("name"));
				account.setUsername(resultSet.getString("username"));
				account.setPassword(resultSet.getString("password"));
				account.setSpreadlimitamount(resultSet.getInt("spreadlimitamount"));
				account.setMllimitamount(resultSet.getInt("mllimitamount"));
				account.setTotallimitamount(resultSet.getInt("totallimitamount"));
				account.setUrl(resultSet.getString("url"));
				account.setTimezone(resultSet.getString("timezone"));
				account.setOwnerpercentage(resultSet.getInt("ownerpercentage"));
				account.setPartnerpercentage(resultSet.getInt("partnerpercentage"));
				account.setProxylocation(resultSet.getString("proxylocation"));
				account.setIsactive(resultSet.getBoolean("isactive"));
				account.setDatecreated(resultSet.getTimestamp("datecreated"));
				account.setDatemodified(resultSet.getTimestamp("datemodified"));
				account.setSitetype(resultSet.getString("sitetype"));
				account.setIsmobile(resultSet.getBoolean("ismobile"));
				account.setIscomplexcaptcha(resultSet.getBoolean("iscomplexcaptcha"));
				account.setShowrequestresponse(resultSet.getBoolean("showrequestresponse"));
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

		LOGGER.info("Exiting getAccount()");
		return account;
	}

	/**
	 * 
	 * @param recordEvent
	 * @return
	 */
	public String getDateQuery(BaseRecordEvent recordEvent) {
		LOGGER.info("Entering getDateQuery()");
		final Date gameDate = recordEvent.getEventdatetime();
		LOGGER.debug("gameDate: " + gameDate);

		
		final Calendar gDate = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
		gDate.setTime(gameDate);
		int imonth = gDate.get(Calendar.MONTH) + 1;
		int iday = gDate.get(Calendar.DAY_OF_MONTH);
		int iyear = gDate.get(Calendar.YEAR);
		
		final ZonedDateTime now = ZonedDateTime.now(ZoneId.of("America/Los_Angeles"));
		final ZoneId z = now.getZone();
		final ZoneRules zoneRules = z.getRules();
		final Boolean isDst = zoneRules.isDaylightSavings(now.toInstant());
		
		String PT = "PST";
		if (isDst.booleanValue()) {
			PT = "PDT";
		}
		final String dateCheck = " AND eventdatetime between ('" + iyear + "/" + imonth + "/" + iday + " 00:00:00'::timestamp AT TIME ZONE '" + PT + "') and ('" + iyear + "/" + imonth + "/" + iday + " 23:59:59'::timestamp AT TIME ZONE '" + PT + "')";

//				+ "('2018-03-23 23:59:59'::timestamp AT TIME ZONE 'PDT')"
//		final String dateCheck = "AND eventdatetime between '" + imonth + "/" + iday + "/" + iyear + " 00:00:00 " + PT + "' and '" + emonth + "/" + eday + "/" + eyear + " 23:59:59 " + PT + "'";

		LOGGER.debug("dateCheck: " + dateCheck);
		LOGGER.info("Exiting getDateQuery()");
		return dateCheck;
	}

	/**
	 * 
	 * @param recordEvent
	 * @return
	 */
	public String getDateQuery(Date gameDate1, Date gameDate2) {
		LOGGER.info("Entering getDateQuery()");
		LOGGER.debug("gameDate1: " + gameDate1);
		LOGGER.debug("gameDate2: " + gameDate2);

		final Calendar gDate = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
		gDate.setTime(gameDate1);
		int imonth = gDate.get(Calendar.MONTH) + 1;
		int iday = gDate.get(Calendar.DAY_OF_MONTH);
		int iyear = gDate.get(Calendar.YEAR);
		
		final Calendar gDate2 = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
		gDate2.setTime(gameDate2);
		int imonth2 = gDate2.get(Calendar.MONTH) + 1;
		int iday2 = gDate2.get(Calendar.DAY_OF_MONTH);
		int iyear2 = gDate2.get(Calendar.YEAR);

		final ZonedDateTime now = ZonedDateTime.now(ZoneId.of("America/Los_Angeles"));
		final ZoneId z = now.getZone();
		final ZoneRules zoneRules = z.getRules();
		final Boolean isDst = zoneRules.isDaylightSavings(now.toInstant());
		
		String PT = "PST";
		if (isDst.booleanValue()) {
			PT = "PDT";
		}
		final String dateCheck = " AND eventdatetime between ('" + iyear + "/" + imonth + "/" + iday + " 00:00:00'::timestamp AT TIME ZONE '" + PT + "') and ('" + iyear2 + "/" + imonth2 + "/" + iday2 + " 23:59:59'::timestamp AT TIME ZONE '" + PT + "')";

//				+ "('2018-03-23 23:59:59'::timestamp AT TIME ZONE 'PDT')"
//		final String dateCheck = "AND eventdatetime between '" + imonth + "/" + iday + "/" + iyear + " 00:00:00 " + PT + "' and '" + emonth + "/" + eday + "/" + eyear + " 23:59:59 " + PT + "'";

		LOGGER.debug("dateCheck: " + dateCheck);
		LOGGER.info("Exiting getDateQuery()");
		return dateCheck;
	}
}