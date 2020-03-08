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
import com.ticketadvantage.services.model.ParlayAccountEvent;
import com.ticketadvantage.services.model.ParlayRecordEvent;
import com.ticketadvantage.services.model.SpreadRecordEvent;
import com.ticketadvantage.services.model.TotalRecordEvent;

/**
 * @author jmiller
 *
 */
public class ParlayRecordEventDB extends BaseDB {
	private static final Logger LOGGER = Logger.getLogger(ParlayRecordEventDB.class);

	/**
	 * 
	 */
	public ParlayRecordEventDB() {
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
	 * @param recordEvent
	 * @return
	 * @throws SQLException
	 */
	public boolean checkDuplicateParlayEvent(SpreadRecordEvent recordEvent) throws BatchException {
		LOGGER.info("Entering checkDuplicateParlayEvent()");
		boolean retValue = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {
			final String dateCheck = getDateQuery(recordEvent);
			LOGGER.error("dateCheck: " + dateCheck);

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

			if (resultSet.next()) {
				LOGGER.error("Duplicate is true");
				retValue = true;
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

		LOGGER.info("Exiting checkDuplicateParlayEvent()");
		return retValue;
	}

	/**
	 * 
	 * @param recordEvent
	 * @return
	 * @throws SQLException
	 */
	public boolean checkDuplicateReverseParlayEvent(SpreadRecordEvent recordEvent) throws BatchException {
		LOGGER.info("Entering checkDuplicateReverseParlayEvent()");
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
				} else if (secondone != null && secondone.length() > 0 && 
						eventsecondone != null && eventsecondone.length() > 0) {
					retValue = true;
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
		
		LOGGER.info("Exiting checkDuplicateReverseParlayEvent()");
		return retValue;
	}

	/**
	 * 
	 * @param recordEvent
	 * @return
	 * @throws BatchException
	 */
	public Long setParlayRecordEvent(ParlayRecordEvent recordEvent) throws BatchException {
		LOGGER.info("Entering setParlayRecordEvent()");
		LOGGER.info("ParlayRecordEvent: " + recordEvent);
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {
			recordEvent.setId(null);
			StringBuffer sb = new StringBuffer(200);
			sb.append("INSERT into parlayrecordevent (userid, parlaytype, totalrisk, totalwin, description, scrappername, ")
					.append("actiontype, textnumber, wtype, iscompleted, attempts, attempttime, currentattempts, riskamount, ")
					.append("winamount, datecreated, datemodified) ")
					.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
	
			conn = this.getConnection();
			stmt = conn.prepareStatement(sb.toString(), Statement.RETURN_GENERATED_KEYS);
			stmt.setLong(1, checkLong(recordEvent.getUserid()));
			stmt.setString(2, checkString(recordEvent.getParlaytype()));
			stmt.setFloat(3, checkFloat(recordEvent.getTotalrisk()));
			stmt.setFloat(4, checkFloat(recordEvent.getTotalwin()));
			stmt.setString(5, checkString(recordEvent.getDescription()));
			stmt.setString(6, checkString(recordEvent.getScrappername()));
			stmt.setString(7, checkString(recordEvent.getActiontype()));
			stmt.setString(8, checkString(recordEvent.getTextnumber()));
			stmt.setString(9, checkString(recordEvent.getWtype()));
			stmt.setBoolean(10, checkBoolean(recordEvent.getIscompleted()));
			stmt.setInt(11, checkInteger(recordEvent.getAttempts()));
			stmt.setTimestamp(12, new java.sql.Timestamp(checkTimestamp(recordEvent.getAttempttime()).getTime()));
			stmt.setInt(13, checkInteger(recordEvent.getCurrentattempts()));
			stmt.setFloat(14, checkFloat(recordEvent.getRiskamount()));
			stmt.setFloat(15, checkFloat(recordEvent.getWinamount()));
			stmt.setTimestamp(16, new java.sql.Timestamp(new Date().getTime()));
			stmt.setTimestamp(17, new java.sql.Timestamp(new Date().getTime()));
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

		LOGGER.info("Exiting setParlayRecordEvent()");
		return recordEvent.getId();
	}

	/**
	 * 
	 * @param id
	 * @return
	 * @throws BatchException
	 */
	public ParlayRecordEvent getParlayRecordEvent(Long id) throws BatchException {
		LOGGER.info("Entering getParlayRecordEvent()");
		LOGGER.info("id: " + id);
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		ParlayRecordEvent parlayRecordEvent = null;

		try {
			StringBuffer sb = new StringBuffer(200);
			sb.append("SELECT * from parlayrecordevent where id = ?");

			conn = this.getConnection();
			stmt = conn.prepareStatement(sb.toString());
			stmt.setLong(1, id);
			resultSet = stmt.executeQuery();
			
			while (resultSet.next()) {
				parlayRecordEvent = new ParlayRecordEvent();
				parlayRecordEvent.setId(resultSet.getLong("id"));
				parlayRecordEvent.setUserid(resultSet.getLong("userid"));
				parlayRecordEvent.setParlaytype(resultSet.getString("parlaytype"));
				parlayRecordEvent.setTotalrisk(resultSet.getFloat("totalrisk"));
				parlayRecordEvent.setTotalwin(resultSet.getFloat("totalwin"));
				parlayRecordEvent.setDescription(resultSet.getString("description"));
				parlayRecordEvent.setScrappername(resultSet.getString("scrappername"));
				parlayRecordEvent.setActiontype(resultSet.getString("actiontype"));
				parlayRecordEvent.setTextnumber(resultSet.getString("textnumber"));
				parlayRecordEvent.setWtype(resultSet.getString("wtype"));
				parlayRecordEvent.setIscompleted(resultSet.getBoolean("iscompleted"));
				parlayRecordEvent.setAttempts(resultSet.getInt("attempts"));
				parlayRecordEvent.setAttempttime(resultSet.getDate("attempttime"));
				parlayRecordEvent.setCurrentattempts(resultSet.getInt("currentattempts"));
				parlayRecordEvent.setRiskamount(resultSet.getFloat("riskamount"));
				parlayRecordEvent.setWinamount(resultSet.getFloat("winamount"));
				parlayRecordEvent.setEventresult(resultSet.getString("eventresult"));
				parlayRecordEvent.setEventresultamount(resultSet.getFloat("eventresultamount"));
				parlayRecordEvent.setDatecreated(resultSet.getDate("datecreated"));
				parlayRecordEvent.setDatemodified(resultSet.getDate("datemodified"));
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

		LOGGER.info("Exiting getParlayRecordEvent()");
		return parlayRecordEvent;
	}

	/**
	 * 
	 * @param id
	 * @throws BatchException
	 */
	public void setCompleteParlayRecordEvent(Long id) throws BatchException {
		LOGGER.info("Entering setCompleteParlayRecordEvent()");
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {
			final StringBuffer sb = new StringBuffer(200);
			sb.append("UPDATE parlayrecordevent SET iscompleted = ? WHERE id = ?");

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

		LOGGER.info("Exiting setCompleteParlayRecordEvent()");
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
			conn = this.getConnection();
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
	 * @param accountEvent
	 * @throws BatchException
	 */
	public void setupParlayAccountEvent(ParlayAccountEvent accountEvent) throws BatchException {
		LOGGER.info("Entering setupParlayAccountEvent()");
		LOGGER.debug("ParlayAccountEvent: " + accountEvent);
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {
			final StringBuffer sb = new StringBuffer(1000);
			final Date dateModified = new java.util.Date();
			sb.append("INSERT into parlayaccountevent (userid, parlayid, eventid, eventname, accountid, accountname, ")
					.append("groupid, sport, wagertype, amount, actualamount, maxspreadamount, maxtotalamount, maxmlamount, ")
					.append("timezone, ownerpercentage, partnerpercentage, ")
					.append("spreadindicator, spreaddata, spread, spreadjuiceindicator, spreadjuicedata, spreadjuice, ")
					.append("totalindicator, totaldata, total, totaljuiceindicator, totaljuicedata, totaljuice, ")
					.append("mlindicator, mldata, ml, iscompleted, ")
					.append("proxy, attempts, status, riskamount, towinamount, accesstoken, eventdatetime, ")
					.append("datecreated, datemodified, type, parlaytype) ")
					.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ")
					.append("?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

			conn = this.getConnection();
			stmt = conn.prepareStatement(sb.toString(), Statement.RETURN_GENERATED_KEYS);
			stmt.setLong(1, checkLong(accountEvent.getUserid()));
			stmt.setLong(2, checkLong(accountEvent.getParlayid()));
			stmt.setInt(3, checkInteger(accountEvent.getEventid()));
			stmt.setString(4, checkString(accountEvent.getEventname()));
			stmt.setLong(5, checkLong(accountEvent.getAccountid()));
			stmt.setString(6, checkString(accountEvent.getAccountname()));
			stmt.setLong(7, checkLong(accountEvent.getGroupid()));
			stmt.setString(8, checkString(accountEvent.getSport()));
			stmt.setString(9, checkString(accountEvent.getWagertype()));
			stmt.setFloat(10, checkFloat(accountEvent.getAmount()));
			stmt.setFloat(11, checkFloat(accountEvent.getActualamount()));			
			stmt.setFloat(12, checkFloat(accountEvent.getMaxspreadamount()));
			stmt.setFloat(13, checkFloat(accountEvent.getMaxtotalamount()));
			stmt.setFloat(14, checkFloat(accountEvent.getMaxmlamount()));
			stmt.setString(15, checkString(accountEvent.getTimezone()));
			stmt.setInt(16, checkInteger(accountEvent.getOwnerpercentage()));
			stmt.setInt(17, checkInteger(accountEvent.getPartnerpercentage()));
			stmt.setString(18, checkString(accountEvent.getSpreadindicator()));
			stmt.setString(19, checkString(accountEvent.getSpreaddata()));
			stmt.setFloat(20, checkFloat(accountEvent.getSpread()));
			stmt.setString(21, checkString(accountEvent.getSpreadjuiceindicator()));
			stmt.setString(22, checkString(accountEvent.getSpreadjuicedata()));
			stmt.setFloat(23, checkFloat(accountEvent.getSpreadjuice()));
			stmt.setString(24, checkString(accountEvent.getTotalindicator()));
			stmt.setString(25, checkString(accountEvent.getTotaldata()));
			stmt.setFloat(26, checkFloat(accountEvent.getTotal()));
			stmt.setString(27, checkString(accountEvent.getTotaljuiceindicator()));
			stmt.setString(28, checkString(accountEvent.getTotaljuicedata()));
			stmt.setFloat(29, checkFloat(accountEvent.getTotaljuice()));
			stmt.setString(30, checkString(accountEvent.getMlindicator()));
			stmt.setString(31, checkString(accountEvent.getMldata()));
			stmt.setFloat(32, checkFloat(accountEvent.getMl()));
			stmt.setBoolean(33, checkBoolean(accountEvent.getIscompleted()));
			stmt.setString(34, checkString(accountEvent.getProxy()));
			stmt.setInt(35, checkInteger(accountEvent.getAttempts()));
			stmt.setString(36, checkString(accountEvent.getStatus()));
			stmt.setFloat(37, checkFloat(accountEvent.getRiskamount()));
			stmt.setFloat(38, checkFloat(accountEvent.getTowinamount()));
			stmt.setString(39, checkString(accountEvent.getAccesstoken()));
			stmt.setTimestamp(40, new java.sql.Timestamp(checkTimestamp(accountEvent.getEventdatetime()).getTime()));
			stmt.setTimestamp(41, new java.sql.Timestamp(checkTimestamp(dateModified).getTime()));
			stmt.setTimestamp(42, new java.sql.Timestamp(checkTimestamp(dateModified).getTime()));
			stmt.setString(43, checkString(accountEvent.getType()));
			stmt.setString(44, checkString(accountEvent.getParlaytype()));

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
				closeStreams(conn, stmt, resultSet);
			} catch (Throwable t) {
				LOGGER.error(t.getMessage(), t);
			}
		}

		LOGGER.info("Exiting setupParlayAccountEvent()");		
	}

	/**
	 * 
	 * @param accountEventId
	 * @return
	 * @throws BatchException
	 */
	public ParlayAccountEvent getParlyAccountEvent(Long accountEventId) throws BatchException {
		LOGGER.info("Entering getParlyAccountEvent()");
		LOGGER.debug("accountEventId: " + accountEventId);
		ParlayAccountEvent accountEvent = null;
		Connection conn = null;
		Statement stmt = null;
		ResultSet resultSet = null;

		try {
			conn = this.getConnection();
			stmt = conn.createStatement();
			resultSet = stmt.executeQuery("SELECT * FROM parlayaccountevent where id = " + accountEventId);

			while (resultSet.next()) {
				accountEvent = new ParlayAccountEvent();

				accountEvent.setAccountconfirmation(resultSet.getString("accountconfirmation"));
				accountEvent.setAccesstoken(resultSet.getString("accesstoken"));
				accountEvent.setAccounthtml(resultSet.getString("accounthtml"));
				accountEvent.setAccountid(resultSet.getLong("accountid"));
				accountEvent.setAccountname(resultSet.getString("accountnamename"));
				accountEvent.setAmount(resultSet.getFloat("amount"));
				accountEvent.setActualamount(resultSet.getFloat("actualamount"));
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
				accountEvent.setMaxspreadamount(resultSet.getFloat("maxspreadamount"));
				accountEvent.setMaxtotalamount(resultSet.getFloat("maxtotalamount"));
				accountEvent.setMaxmlamount(resultSet.getFloat("maxmlamount"));
				accountEvent.setMlindicator(resultSet.getString("mlindicator"));
				accountEvent.setTimezone(resultSet.getString("timezone"));
				accountEvent.setMl(resultSet.getFloat("ml"));
				accountEvent.setOwnerpercentage(resultSet.getInt("ownerpercentage"));
				accountEvent.setPartnerpercentage(resultSet.getInt("partnerpercentage"));
				accountEvent.setProxy(resultSet.getString("proxy"));
				accountEvent.setSport(resultSet.getString("sport"));
				accountEvent.setSpreadindicator(resultSet.getString("spreadindicator"));
				accountEvent.setSpread(resultSet.getFloat("spread"));
				accountEvent.setSpreadjuice(resultSet.getFloat("spreadjuice"));
				accountEvent.setStatus(resultSet.getString("status"));
				accountEvent.setTotal(resultSet.getFloat("total"));
				accountEvent.setTotalindicator(resultSet.getString("totalindicator"));
				accountEvent.setTotaljuice(resultSet.getFloat("totaljuice"));
				accountEvent.setType(resultSet.getString("type"));
				accountEvent.setUserid(resultSet.getLong("userid"));
				accountEvent.setWagertype(resultSet.getString("wagertype"));
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

		LOGGER.info("Exiting getParlyAccountEvent()");
		return accountEvent;
	}

	/**
	 * 
	 * @param accountEventId
	 * @return
	 * @throws BatchException
	 */
	public List<ParlayAccountEvent> getParlyAccountEventsByParlayId(Long parlayId) throws BatchException {
		LOGGER.info("Entering getParlyAccountEventsByParlayId()");
		LOGGER.debug("parlayId: " + parlayId);
		final List<ParlayAccountEvent> accountEvents = new ArrayList<ParlayAccountEvent>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet resultSet = null;

		try {
			conn = this.getConnection();
			stmt = conn.createStatement();
			resultSet = stmt.executeQuery("SELECT * FROM parlayaccountevent where parlayid = " + parlayId);

			while (resultSet.next()) {
				final ParlayAccountEvent accountEvent = new ParlayAccountEvent();
				accountEvent.setAccountconfirmation(resultSet.getString("accountconfirmation"));
				accountEvent.setAccesstoken(resultSet.getString("accesstoken"));
				accountEvent.setAccounthtml(resultSet.getString("accounthtml"));
				accountEvent.setAccountid(resultSet.getLong("accountid"));
				accountEvent.setAccountname(resultSet.getString("accountnamename"));
				accountEvent.setAmount(resultSet.getFloat("amount"));
				accountEvent.setActualamount(resultSet.getFloat("actualamount"));
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
				accountEvent.setMaxspreadamount(resultSet.getFloat("maxspreadamount"));
				accountEvent.setMaxtotalamount(resultSet.getFloat("maxtotalamount"));
				accountEvent.setMaxmlamount(resultSet.getFloat("maxmlamount"));
				accountEvent.setMlindicator(resultSet.getString("mlindicator"));
				accountEvent.setTimezone(resultSet.getString("timezone"));
				accountEvent.setMl(resultSet.getFloat("ml"));
				accountEvent.setOwnerpercentage(resultSet.getInt("ownerpercentage"));
				accountEvent.setPartnerpercentage(resultSet.getInt("partnerpercentage"));
				accountEvent.setParlayid(resultSet.getLong("parlayid"));
				accountEvent.setParlayid(resultSet.getLong("parlaytype"));
				accountEvent.setParlaytype(resultSet.getString("parlaytype"));
				accountEvent.setProxy(resultSet.getString("proxy"));
				accountEvent.setSport(resultSet.getString("sport"));
				accountEvent.setSpreadindicator(resultSet.getString("spreadindicator"));
				accountEvent.setSpread(resultSet.getFloat("spread"));
				accountEvent.setSpreadjuice(resultSet.getFloat("spreadjuice"));
				accountEvent.setStatus(resultSet.getString("status"));
				accountEvent.setTotal(resultSet.getFloat("total"));
				accountEvent.setTotalindicator(resultSet.getString("totalindicator"));
				accountEvent.setTotaljuice(resultSet.getFloat("totaljuice"));
				accountEvent.setType(resultSet.getString("type"));
				accountEvent.setUserid(resultSet.getLong("userid"));
				accountEvent.setWagertype(resultSet.getString("wagertype"));
				accountEvents.add(accountEvent);
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

		LOGGER.info("Exiting getParlyAccountEventsByParlayId()");
		return accountEvents;
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