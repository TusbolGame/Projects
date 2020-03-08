/**
 * 
 */
package com.ticketadvantage.services.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.zone.ZoneRules;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.MlRecordEvent;
import com.ticketadvantage.services.model.TotalRecordEvent;

/**
 * @author jmiller
 *
 */
public class RecordEventLookupDB extends BaseDB {
	private static final Logger LOGGER = Logger.getLogger(RecordEventLookupDB.class);

	/**
	 * 
	 */
	public RecordEventLookupDB() {
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
	 * @param gameDate
	 * @param sport
	 * @param userid
	 * @return
	 * @throws SQLException
	 */
	public TotalRecordEvent getTotalEventByRotationId(Integer rotationId, Date gameDate, String sport, Long userid) throws BatchException {
		LOGGER.info("Entering getTotalEventByRotationId()");
		TotalRecordEvent totalRecordEvent = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {
			final String dateCheck = getDateQuery(gameDate);
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
	 * @param gameDate
	 * @param sport
	 * @param userid
	 * @return
	 * @throws SQLException
	 */
	public MlRecordEvent getMlEventByRotationId(Integer rotationId, Date gameDate, String sport, Long userid) throws BatchException {
		LOGGER.info("Entering getMlEventByRotationId()");
		MlRecordEvent mlRecordEvent = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {
			final String dateCheck = getDateQuery(gameDate);
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
	 */
	public String getDateQuery(Date gameDate) {
		LOGGER.info("Entering getDateQuery()");
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
}