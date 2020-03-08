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
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.postgresql.largeobject.LargeObject;
import org.postgresql.largeobject.LargeObjectManager;

import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.AccountEvent;
import com.ticketadvantage.services.model.Accounts;
import com.ticketadvantage.services.model.MlRecordEvent;
import com.ticketadvantage.services.model.SpreadRecordEvent;
import com.ticketadvantage.services.model.TotalRecordEvent;

/**
 * @author jmiller
 *
 */
public class SiteEventsDosDB extends BaseDB {
	private static final Logger LOGGER = Logger.getLogger(SiteEventsDosDB.class);

	/**
	 * 
	 */
	public SiteEventsDosDB() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public AccountEvent getAccountEvent(int id) throws BatchException {
		LOGGER.info("Entering getAccountEvent()");
		LOGGER.info("id: "+ id);
		AccountEvent accountEvent = null;
		Connection conn = null;
		Statement stmt = null;
		ResultSet resultSet = null;

		try {
			conn = this.getNoAutoConnection();
			stmt = conn.createStatement();
			resultSet = stmt.executeQuery("select * from accountevent where id = " + id);
	
			if (resultSet.next()) {
				accountEvent = new AccountEvent();

				accountEvent.setAccountconfirmation(resultSet.getString("accountconfirmation"));
				Long accountHtml = resultSet.getLong("accounthtml");
				
				if (accountHtml != null && accountHtml != 0) {
					// Get the Large Object Manager to perform operations with
					LargeObjectManager lobj = conn.unwrap(org.postgresql.PGConnection.class).getLargeObjectAPI();
					// Open the large object for reading
					long oid = accountHtml.longValue();
					LargeObject obj = lobj.open(oid, LargeObjectManager.READ);

					// Read the data
					byte buf[] = new byte[obj.size()];
					obj.read(buf, 0, obj.size());
					// Do something with the data read here
					accountEvent.setAccounthtml(new String(buf));

					// Close the object
					obj.close();
				}

				accountEvent.setAccountid(resultSet.getLong("accountid"));
				accountEvent.setAccesstoken(resultSet.getString("accesstoken"));
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
		LOGGER.debug("id: "+ id);
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
		LOGGER.debug("id: "+ id);
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
		LOGGER.debug("id: "+ id);
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
	 * @param accountEvent
	 */
	public void updateAccountEvent(AccountEvent accountEvent) throws BatchException {
		LOGGER.info("Entering updateAccountEvent()");
		LOGGER.debug("AccountEvent: " + accountEvent);
		Connection conn = null;
		PreparedStatement update = null;

		try {
			final StringBuffer sb = new StringBuffer(1000);
			final Date dateModified = new java.util.Date();
			
			checkString(accountEvent.getAccountconfirmation(), sb, "accountconfirmation = ? ");
			checkString(accountEvent.getAccounthtml(), sb, "accounthtml = ? ");
			checkLong(accountEvent.getAccountid(), sb, "accountid = ? ");
			checkString(accountEvent.getAmount(), sb, "amount = ? ");
			checkString(accountEvent.getActualamount(), sb, "actualamount = ? ");
			checkInteger(accountEvent.getAttempts(), sb, "attempts = ? ");
			checkTimestamp(accountEvent.getAttempttime(), sb, "attempttime = ? ");
			checkInteger(accountEvent.getCurrentattempts(), sb, "currentattempts = ? ");
			checkTimestamp(dateModified, sb, "datemodified = ? ");
			checkInteger(accountEvent.getErrorcode(), sb, "errorcode = ? ");
			checkString(accountEvent.getErrorexception(), sb, "errorexception = ? ");
			checkString(accountEvent.getErrormessage(), sb, "errormessage = ? ");
			checkTimestamp(accountEvent.getEventdatetime(), sb, "eventdatetime = ? ");
			checkInteger(accountEvent.getEventid(), sb, "eventid = ? ");
			checkString(accountEvent.getEventname(), sb, "eventname = ? ");
			checkLong(accountEvent.getGroupid(), sb, "groupid = ? ");
			checkBoolean(accountEvent.getIscompleted(), sb, "iscompleted = ? ");
			checkInteger(accountEvent.getMaxspreadamount(), sb, "maxspreadamount = ? ");
			checkInteger(accountEvent.getMaxtotalamount(), sb, "maxtotalamount = ? ");
			checkInteger(accountEvent.getMaxmlamount(), sb, "maxmlamount = ? ");
			checkString(accountEvent.getMlindicator(), sb, "mlindicator = ? ");
			checkString(accountEvent.getTimezone(), sb, "timezone = ? ");
			checkFloat(accountEvent.getMl(), sb, "ml = ? ");
			checkLong(accountEvent.getMlid(), sb, "mlid = ? ");
			checkFloat(accountEvent.getMljuice(), sb, "mljuice = ? ");
			checkString(accountEvent.getName(), sb, "name = ? ");
			checkInteger(accountEvent.getOwnerpercentage(), sb, "ownerpercentage = ? ");
			checkInteger(accountEvent.getPartnerpercentage(), sb, "partnerpercentage = ? ");
			checkString(accountEvent.getProxy(), sb, "proxy = ? ");
			checkString(accountEvent.getRiskamount(), sb, "riskamount = ?");
			checkString(accountEvent.getSport(), sb, "sport = ? ");
			checkString(accountEvent.getSpreadindicator(), sb, "spreadindicator = ? ");
			checkFloat(accountEvent.getSpread(), sb, "spread = ? ");
			checkLong(accountEvent.getSpreadid(), sb, "spreadid = ? ");
			checkFloat(accountEvent.getSpreadjuice(), sb, "spreadjuice = ? ");
			checkString(accountEvent.getStatus(), sb, "status = ? ");
			checkFloat(accountEvent.getTotal(), sb, "total = ? ");
			checkLong(accountEvent.getTotalid(), sb, "totalid = ? ");
			checkString(accountEvent.getTotalindicator(), sb, "totalindicator = ? ");
			checkFloat(accountEvent.getTotaljuice(), sb, "totaljuice = ? ");
			checkString(accountEvent.getTowinamount(), sb, "towinamount = ? ");
			checkString(accountEvent.getType(), sb, "type = ? ");
			checkLong(accountEvent.getUserid(), sb, "userid = ? ");
			checkString(accountEvent.getWagertype(), sb, "wagertype = ? ");
			
			// Prepare the statement
			String sqlData = "UPDATE accountevent SET " + sb.toString() + " WHERE id = ?"; 
			LOGGER.debug("SQL: " + sqlData);
			conn = this.getNoAutoConnection();
			update = conn.prepareStatement(sqlData);

			// Setup the data
			int x = 0;
			x = setupString(update, x, accountEvent.getAccountconfirmation());
			if (accountEvent.getAccounthtml() != null) {
					// Get the Large Object Manager to perform operations with
					final LargeObjectManager lobj = conn.unwrap(org.postgresql.PGConnection.class).getLargeObjectAPI();

					// Create a new large object
					long oid = lobj.createLO(LargeObjectManager.READ | LargeObjectManager.WRITE);

					// Open the large object for writing
					final LargeObject obj = lobj.open(oid, LargeObjectManager.WRITE);
					obj.write(accountEvent.getAccounthtml().getBytes());					
					obj.close();

					update.setLong(++x, oid);
//					update.setClob(++x, lob.clob(accountEvent.getAccounthtml()));
			}

			x = setupLong(update, x, accountEvent.getAccountid());
			x = setupString(update, x, accountEvent.getAmount());
			x = setupString(update, x, accountEvent.getActualamount());
			x = setupInteger(update, x, accountEvent.getAttempts());
			x = setupTimestamp(update, x, accountEvent.getAttempttime());
			x = setupInteger(update, x, accountEvent.getCurrentattempts());
			x = setupTimestamp(update, x, dateModified);
			x = setupInteger(update, x, accountEvent.getErrorcode());
			x = setupString(update, x, accountEvent.getErrorexception());
			x = setupString(update, x, accountEvent.getErrormessage());
			x = setupTimestamp(update, x, accountEvent.getEventdatetime());
			x = setupInteger(update, x, accountEvent.getEventid());
			x = setupString(update, x, accountEvent.getEventname());
			x = setupLong(update, x, accountEvent.getGroupid());
			x = setupBoolean(update, x, accountEvent.getIscompleted());
			x = setupInteger(update, x, accountEvent.getMaxspreadamount());
			x = setupInteger(update, x, accountEvent.getMaxtotalamount());
			x = setupInteger(update, x, accountEvent.getMaxmlamount());
			x = setupString(update, x, accountEvent.getMlindicator());
			x = setupString(update, x, accountEvent.getTimezone());
			x = setupFloat(update, x, accountEvent.getMl());
			x = setupLong(update, x, accountEvent.getMlid());
			x = setupFloat(update, x, accountEvent.getMljuice());
			x = setupString(update, x, accountEvent.getName());
			x = setupInteger(update, x, accountEvent.getOwnerpercentage());
			x = setupInteger(update, x, accountEvent.getPartnerpercentage());
			x = setupString(update, x, accountEvent.getProxy());
			x = setupString(update, x, accountEvent.getRiskamount());
			x = setupString(update, x, accountEvent.getSport());
			x = setupString(update, x, accountEvent.getSpreadindicator());
			x = setupFloat(update, x, accountEvent.getSpread());
			x = setupLong(update, x, accountEvent.getSpreadid());
			x = setupFloat(update, x, accountEvent.getSpreadjuice());
			x = setupString(update, x, accountEvent.getStatus());
			x = setupFloat(update, x, accountEvent.getTotal());
			x = setupLong(update, x, accountEvent.getTotalid());
			x = setupString(update, x, accountEvent.getTotalindicator());
			x = setupFloat(update, x, accountEvent.getTotaljuice());
			x = setupString(update, x, accountEvent.getTowinamount());
			x = setupString(update, x, accountEvent.getType());
			x = setupLong(update, x, accountEvent.getUserid());
			x = setupString(update, x, accountEvent.getWagertype());
			x = setupLong(update, x, accountEvent.getId());

			// Run the update query
			update.executeUpdate();
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
				closeStreams(conn, update, null);
			} catch (Throwable t) {
				LOGGER.error(t.getMessage(), t);
			}
		}

		LOGGER.info("Exiting updateAccountEvent()");
	}

	/**
	 * 
	 * @param spreadRecordEvent
	 * @throws SQLException
	 */
	public void updateSpreadEvent(SpreadRecordEvent spreadRecordEvent) throws BatchException {
		LOGGER.info("Entering updateSpreadEvent()");
		LOGGER.debug("SpreadRecordEvent: " + spreadRecordEvent);
		Connection conn = null;
		PreparedStatement update = null;

		try {
			final StringBuffer sb = new StringBuffer(1000);
			final Date dateModified = new Date();

			// Base event info
			checkString(spreadRecordEvent.getEventname(), sb, "eventname = ? ");
			checkString(spreadRecordEvent.getEventtype(), sb, "eventtype = ? ");
			checkString(spreadRecordEvent.getSport(), sb, "sport = ? ");
			checkLong(spreadRecordEvent.getUserid(), sb, "userid = ? ");
			checkLong(spreadRecordEvent.getAccountid(), sb, "accountid = ? ");
			checkLong(spreadRecordEvent.getGroupid(), sb, "groupid = ? ");
			checkInteger(spreadRecordEvent.getEventid(), sb, "eventid = ? ");
			checkInteger(spreadRecordEvent.getEventid1(), sb, "eventid1 = ? ");
			checkInteger(spreadRecordEvent.getEventid2(), sb, "eventid2 = ? ");
			checkString(spreadRecordEvent.getEventteam1(), sb, "eventteam1 = ? ");
			checkString(spreadRecordEvent.getEventteam2(), sb, "eventteam2 = ? ");
			checkString(spreadRecordEvent.getAmount(), sb, "amount = ? ");
			checkString(spreadRecordEvent.getWtype(), sb, "wtype = ? ");
			checkBoolean(spreadRecordEvent.getIscompleted(), sb, "iscompleted = ? ");
			checkInteger(spreadRecordEvent.getAttempts(), sb, "attempts = ? ");
			checkInteger(spreadRecordEvent.getCurrentattempts(), sb, "currentattempts = ? ");
			checkTimestamp(spreadRecordEvent.getAttempttime(), sb, "attempttime = ? ");
			checkTimestamp(spreadRecordEvent.getEventdatetime(), sb, "eventdatetime = ? ");
			checkTimestamp(spreadRecordEvent.getDatentime(), sb, "datentime = ? ");
			checkTimestamp(dateModified, sb, "datemodified = ? ");
			checkString(spreadRecordEvent.getScrappername(), sb, "scrappername = ? ");
			checkString(spreadRecordEvent.getActiontype(), sb, "actiontype = ? ");
			checkString(spreadRecordEvent.getTextnumber(), sb, "textnumber = ? ");
			
			// Spread
			checkString(spreadRecordEvent.getSpreadinputfirstone(), sb, "spreadinputfirstone = ? ");
			checkString(spreadRecordEvent.getSpreadplusminusfirstone(), sb, "spreadplusminusfirstone = ? ");
			checkString(spreadRecordEvent.getSpreadinputjuicefirstone(), sb, "spreadinputjuicefirstone = ? ");
			checkString(spreadRecordEvent.getSpreadjuiceplusminusfirstone(), sb, "spreadjuiceplusminusfirstone = ? ");
			checkString(spreadRecordEvent.getSpreadinputfirsttwo(), sb, "spreadinputfirsttwo = ? ");
			checkString(spreadRecordEvent.getSpreadplusminusfirsttwo(), sb, "spreadplusminusfirsttwo = ? ");
			checkString(spreadRecordEvent.getSpreadinputjuicefirsttwo(), sb, "spreadinputjuicefirsttwo = ? ");
			checkString(spreadRecordEvent.getSpreadjuiceplusminusfirsttwo(), sb, "spreadjuiceplusminusfirsttwo = ? ");
			checkString(spreadRecordEvent.getSpreadinputsecondone(), sb, "spreadinputsecondone = ? ");
			checkString(spreadRecordEvent.getSpreadplusminussecondone(), sb, "spreadplusminussecondone = ? ");
			checkString(spreadRecordEvent.getSpreadinputjuicesecondone(), sb, "spreadinputjuicesecondone = ? ");
			checkString(spreadRecordEvent.getSpreadjuiceplusminussecondone(), sb, "spreadjuiceplusminussecondone = ? ");
			checkString(spreadRecordEvent.getSpreadinputsecondtwo(), sb, "spreadinputsecondtwo = ? ");
			checkString(spreadRecordEvent.getSpreadplusminussecondtwo(), sb, "spreadplusminussecondtwo = ? ");
			checkString(spreadRecordEvent.getSpreadinputjuicesecondtwo(), sb, "spreadinputjuicesecondtwo = ? ");
			checkString(spreadRecordEvent.getSpreadjuiceplusminussecondtwo(), sb, "spreadjuiceplusminussecondtwo = ? ");

			// Prepare the statement
			String sqlData = "UPDATE spreadrecordevents SET " + sb.toString() + " WHERE id = ?"; 
			LOGGER.debug("SQL: " + sqlData);
			conn = this.getConnection();
			update = conn.prepareStatement(sqlData);

			// Common
			int x = 0;
			x = setupString(update, x, spreadRecordEvent.getEventname());
			x = setupString(update, x, spreadRecordEvent.getEventtype());
			x = setupString(update, x, spreadRecordEvent.getSport());
			x = setupLong(update, x, spreadRecordEvent.getUserid());
			x = setupLong(update, x, spreadRecordEvent.getAccountid());
			x = setupLong(update, x, spreadRecordEvent.getGroupid());
			x = setupInteger(update, x, spreadRecordEvent.getEventid());
			x = setupInteger(update, x, spreadRecordEvent.getEventid1());
			x = setupInteger(update, x, spreadRecordEvent.getEventid2());
			x = setupString(update, x, spreadRecordEvent.getEventteam1());
			x = setupString(update, x, spreadRecordEvent.getEventteam2());
			x = setupString(update, x, spreadRecordEvent.getAmount());
			x = setupString(update, x, spreadRecordEvent.getWtype());
			x = setupBoolean(update, x, spreadRecordEvent.getIscompleted());
			x = setupInteger(update, x, spreadRecordEvent.getAttempts());
			x = setupInteger(update, x, spreadRecordEvent.getCurrentattempts());
			x = setupTimestamp(update, x, spreadRecordEvent.getAttempttime());
			x = setupTimestamp(update, x, spreadRecordEvent.getEventdatetime());
			x = setupTimestamp(update, x, spreadRecordEvent.getDatentime());
			x = setupTimestamp(update, x, dateModified);
			x = setupString(update, x, spreadRecordEvent.getScrappername());
			x = setupString(update, x, spreadRecordEvent.getActiontype());
			x = setupString(update, x, spreadRecordEvent.getTextnumber());

			// Spread only
			x = setupString(update, x, spreadRecordEvent.getSpreadinputfirstone());
			x = setupString(update, x, spreadRecordEvent.getSpreadplusminusfirstone());
			x = setupString(update, x, spreadRecordEvent.getSpreadinputjuicefirstone());
			x = setupString(update, x, spreadRecordEvent.getSpreadjuiceplusminusfirstone());
			x = setupString(update, x, spreadRecordEvent.getSpreadinputfirsttwo());
			x = setupString(update, x, spreadRecordEvent.getSpreadplusminusfirsttwo());
			x = setupString(update, x, spreadRecordEvent.getSpreadinputjuicefirsttwo());
			x = setupString(update, x, spreadRecordEvent.getSpreadjuiceplusminusfirsttwo());
			x = setupString(update, x, spreadRecordEvent.getSpreadinputsecondone());
			x = setupString(update, x, spreadRecordEvent.getSpreadplusminussecondone());
			x = setupString(update, x, spreadRecordEvent.getSpreadinputjuicesecondone());
			x = setupString(update, x, spreadRecordEvent.getSpreadjuiceplusminussecondone());
			x = setupString(update, x, spreadRecordEvent.getSpreadinputsecondtwo());
			x = setupString(update, x, spreadRecordEvent.getSpreadplusminussecondtwo());
			x = setupString(update, x, spreadRecordEvent.getSpreadinputjuicesecondtwo());
			x = setupString(update, x, spreadRecordEvent.getSpreadjuiceplusminussecondtwo());
			x = setupLong(update, x, spreadRecordEvent.getId());

			// Updates SQL
			update.executeUpdate();
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
				closeStreams(conn, update, null);
			} catch (Throwable t) {
				LOGGER.error(t.getMessage(), t);
			}
		}

		LOGGER.info("Exiting updateSpreadEvent()");
	}

	/**
	 * 
	 * @param totalRecordEvent
	 * @throws SQLException
	 */
	public void updateTotalEvent(TotalRecordEvent totalRecordEvent) throws BatchException {
		LOGGER.info("Entering updateTotalEvent()");
		LOGGER.debug("TotalRecordEvent: " + totalRecordEvent);
		Connection conn = null;
		PreparedStatement update = null;

		try {
			final StringBuffer sb = new StringBuffer(1000);
			final Date dateModified = new Date();

			// Base event info
			checkString(totalRecordEvent.getEventname(), sb, "eventname = ? ");
			checkString(totalRecordEvent.getEventtype(), sb, "eventtype = ? ");
			checkString(totalRecordEvent.getSport(), sb, "sport = ? ");
			checkLong(totalRecordEvent.getUserid(), sb, "userid = ? ");
			checkLong(totalRecordEvent.getAccountid(), sb, "accountid = ? ");
			checkLong(totalRecordEvent.getGroupid(), sb, "groupid = ? ");
			checkInteger(totalRecordEvent.getEventid(), sb, "eventid = ? ");
			checkInteger(totalRecordEvent.getEventid1(), sb, "eventid1 = ? ");
			checkInteger(totalRecordEvent.getEventid2(), sb, "eventid2 = ? ");
			checkString(totalRecordEvent.getEventteam1(), sb, "eventteam1 = ? ");
			checkString(totalRecordEvent.getEventteam2(), sb, "eventteam2 = ? ");
			checkString(totalRecordEvent.getAmount(), sb, "amount = ? ");
			checkString(totalRecordEvent.getWtype(), sb, "wtype = ? ");
			checkBoolean(totalRecordEvent.getIscompleted(), sb, "iscompleted = ? ");
			checkInteger(totalRecordEvent.getAttempts(), sb, "attempts = ? ");
			checkInteger(totalRecordEvent.getCurrentattempts(), sb, "currentattempts = ? ");
			checkTimestamp(totalRecordEvent.getAttempttime(), sb, "attempttime = ? ");
			checkTimestamp(totalRecordEvent.getEventdatetime(), sb, "eventdatetime = ? ");
			checkTimestamp(totalRecordEvent.getDatentime(), sb, "datentime = ? ");
			checkTimestamp(dateModified, sb, "datemodified = ? ");			
			checkString(totalRecordEvent.getScrappername(), sb, "scrappername = ? ");
			checkString(totalRecordEvent.getActiontype(), sb, "actiontype = ? ");
			checkString(totalRecordEvent.getTextnumber(), sb, "textnumber = ? ");

			// Total
			checkString(totalRecordEvent.getTotalinputfirstone(), sb, "totalinputfirstone = ? ");
			checkString(totalRecordEvent.getTotaljuiceplusminusfirstone(), sb, "totaljuiceplusminusfirstone = ? ");
			checkString(totalRecordEvent.getTotalinputjuicefirstone(), sb, "totalinputjuicefirstone = ? ");
			checkString(totalRecordEvent.getTotalinputfirsttwo(), sb, "totalinputfirsttwo = ? ");
			checkString(totalRecordEvent.getTotaljuiceplusminusfirsttwo(), sb, "totaljuiceplusminusfirsttwo = ? ");
			checkString(totalRecordEvent.getTotalinputjuicefirsttwo(), sb, "totalinputjuicefirsttwo = ? ");
			checkString(totalRecordEvent.getTotalinputsecondone(), sb, "totalinputsecondone = ? ");
			checkString(totalRecordEvent.getTotaljuiceplusminussecondone(), sb, "totaljuiceplusminussecondone = ? ");
			checkString(totalRecordEvent.getTotalinputjuicesecondone(), sb, "totalinputjuicesecondone = ? ");
			checkString(totalRecordEvent.getTotalinputsecondtwo(), sb, "totalinputsecondtwo = ? ");
			checkString(totalRecordEvent.getTotaljuiceplusminussecondtwo(), sb, "totaljuiceplusminussecondtwo = ? ");
			checkString(totalRecordEvent.getTotalinputjuicesecondtwo(), sb, "totalinputjuicesecondtwo = ? ");

			// Prepare the statement
			String sqlData = "UPDATE totalrecordevents SET " + sb.toString() + " WHERE id = ?"; 
			LOGGER.debug("SQL: " + sqlData);
			conn = this.getConnection();
			update = conn.prepareStatement(sqlData);

			// Common
			int x = 0;
			x = setupString(update, x, totalRecordEvent.getEventname());
			x = setupString(update, x, totalRecordEvent.getEventtype());
			x = setupString(update, x, totalRecordEvent.getSport());
			x = setupLong(update, x, totalRecordEvent.getUserid());
			x = setupLong(update, x, totalRecordEvent.getAccountid());
			x = setupLong(update, x, totalRecordEvent.getGroupid());
			x = setupInteger(update, x, totalRecordEvent.getEventid());
			x = setupInteger(update, x, totalRecordEvent.getEventid1());
			x = setupInteger(update, x, totalRecordEvent.getEventid2());
			x = setupString(update, x, totalRecordEvent.getEventteam1());
			x = setupString(update, x, totalRecordEvent.getEventteam2());
			x = setupString(update, x, totalRecordEvent.getAmount());
			x = setupString(update, x, totalRecordEvent.getWtype());
			x = setupBoolean(update, x, totalRecordEvent.getIscompleted());
			x = setupInteger(update, x, totalRecordEvent.getAttempts());
			x = setupInteger(update, x, totalRecordEvent.getCurrentattempts());
			x = setupTimestamp(update, x, totalRecordEvent.getAttempttime());
			x = setupTimestamp(update, x, totalRecordEvent.getEventdatetime());
			x = setupTimestamp(update, x, totalRecordEvent.getDatentime());
			x = setupTimestamp(update, x, dateModified);
			x = setupString(update, x, totalRecordEvent.getScrappername());
			x = setupString(update, x, totalRecordEvent.getActiontype());
			x = setupString(update, x, totalRecordEvent.getTextnumber());

			// Total only
			x = setupString(update, x, totalRecordEvent.getTotalinputfirstone());
			x = setupString(update, x, totalRecordEvent.getTotaljuiceplusminusfirstone());
			x = setupString(update, x, totalRecordEvent.getTotalinputjuicefirstone());
			x = setupString(update, x, totalRecordEvent.getTotalinputfirsttwo());
			x = setupString(update, x, totalRecordEvent.getTotaljuiceplusminusfirsttwo());
			x = setupString(update, x, totalRecordEvent.getTotalinputjuicefirsttwo());
			x = setupString(update, x, totalRecordEvent.getTotalinputsecondone());
			x = setupString(update, x, totalRecordEvent.getTotaljuiceplusminussecondone());
			x = setupString(update, x, totalRecordEvent.getTotalinputjuicesecondone());
			x = setupString(update, x, totalRecordEvent.getTotalinputsecondtwo());
			x = setupString(update, x, totalRecordEvent.getTotaljuiceplusminussecondtwo());
			x = setupString(update, x, totalRecordEvent.getTotalinputjuicesecondtwo());
			x = setupLong(update, x, totalRecordEvent.getId());

			// Update SQL
			update.executeUpdate();
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
				closeStreams(conn, update, null);
			} catch (Throwable t) {
				LOGGER.error(t.getMessage(), t);
			}
		}

		LOGGER.info("Exiting updateTotalEvent()");
	}
	
	/**
	 * 
	 * @param mlRecordEvent
	 * @throws SQLException
	 */
	public void updateMlEvent(MlRecordEvent mlRecordEvent) throws BatchException {
		LOGGER.info("Entering updateMlEvent()");
		LOGGER.debug("MlRecordEvent: " + mlRecordEvent);
		Connection conn = null;
		PreparedStatement update = null;

		try {
			StringBuffer sb = new StringBuffer(1000);
			final Date dateModified = new Date();

			// Base event info
			checkString(mlRecordEvent.getEventname(), sb, "eventname = ? ");
			checkString(mlRecordEvent.getEventtype(), sb, "eventtype = ? ");
			checkString(mlRecordEvent.getSport(), sb, "sport = ? ");
			checkLong(mlRecordEvent.getUserid(), sb, "userid = ? ");
			checkLong(mlRecordEvent.getAccountid(), sb, "accountid = ? ");
			checkLong(mlRecordEvent.getGroupid(), sb, "groupid = ? ");
			checkInteger(mlRecordEvent.getEventid(), sb, "eventid = ? ");
			checkInteger(mlRecordEvent.getEventid1(), sb, "eventid1 = ? ");
			checkInteger(mlRecordEvent.getEventid2(), sb, "eventid2 = ? ");
			checkString(mlRecordEvent.getEventteam1(), sb, "eventteam1 = ? ");
			checkString(mlRecordEvent.getEventteam2(), sb, "eventteam2 = ? ");
			checkString(mlRecordEvent.getAmount(), sb, "amount = ? ");
			checkString(mlRecordEvent.getWtype(), sb, "wtype = ? ");
			checkBoolean(mlRecordEvent.getIscompleted(), sb, "iscompleted = ? ");
			checkInteger(mlRecordEvent.getAttempts(), sb, "attempts = ? ");
			checkInteger(mlRecordEvent.getCurrentattempts(), sb, "currentattempts = ? ");
			checkTimestamp(mlRecordEvent.getAttempttime(), sb, "attempttime = ? ");
			checkTimestamp(mlRecordEvent.getEventdatetime(), sb, "eventdatetime = ? ");
			checkTimestamp(mlRecordEvent.getDatentime(), sb, "datentime = ? ");
			checkTimestamp(dateModified, sb, "datemodified = ? ");			
			checkString(mlRecordEvent.getScrappername(), sb, "scrappername = ? ");
			checkString(mlRecordEvent.getActiontype(), sb, "actiontype = ? ");
			checkString(mlRecordEvent.getTextnumber(), sb, "textnumber = ? ");

			// Money line
			checkString(mlRecordEvent.getMlinputfirstone(), sb, "mlinputfirstone = ? ");
			checkString(mlRecordEvent.getMlplusminusfirstone(), sb, "mlplusminusfirstone = ? ");
			checkString(mlRecordEvent.getMlinputfirsttwo(), sb, "mlinputfirsttwo = ? ");
			checkString(mlRecordEvent.getMlplusminusfirsttwo(), sb, "mlplusminusfirsttwo = ? ");
			checkString(mlRecordEvent.getMlinputsecondone(), sb, "mlinputsecondone = ? ");
			checkString(mlRecordEvent.getMlplusminussecondone(), sb, "mlplusminussecondone = ? ");
			checkString(mlRecordEvent.getMlinputsecondtwo(), sb, "mlinputsecondtwo = ? ");
			checkString(mlRecordEvent.getMlplusminussecondtwo(), sb, "mlplusminussecondtwo = ? ");

			// Prepare the statement
			String sqlData = "UPDATE mlrecordevents SET " + sb.toString() + " WHERE id = ?"; 
			LOGGER.debug("SQL: " + sqlData);
			conn = this.getConnection();
			update = conn.prepareStatement(sqlData);

			// Common
			int x = 0;
			x = setupString(update, x, mlRecordEvent.getEventname());
			x = setupString(update, x, mlRecordEvent.getEventtype());
			x = setupString(update, x, mlRecordEvent.getSport());
			x = setupLong(update, x, mlRecordEvent.getUserid());
			x = setupLong(update, x, mlRecordEvent.getAccountid());
			x = setupLong(update, x, mlRecordEvent.getGroupid());
			x = setupInteger(update, x, mlRecordEvent.getEventid());
			x = setupInteger(update, x, mlRecordEvent.getEventid1());
			x = setupInteger(update, x, mlRecordEvent.getEventid2());
			x = setupString(update, x, mlRecordEvent.getEventteam1());
			x = setupString(update, x, mlRecordEvent.getEventteam2());
			x = setupString(update, x, mlRecordEvent.getAmount());
			x = setupString(update, x, mlRecordEvent.getWtype());
			x = setupBoolean(update, x, mlRecordEvent.getIscompleted());
			x = setupInteger(update, x, mlRecordEvent.getAttempts());
			x = setupInteger(update, x, mlRecordEvent.getCurrentattempts());
			x = setupTimestamp(update, x, mlRecordEvent.getAttempttime());
			x = setupTimestamp(update, x, mlRecordEvent.getEventdatetime());
			x = setupTimestamp(update, x, mlRecordEvent.getDatentime());
			x = setupTimestamp(update, x, dateModified);
			x = setupString(update, x, mlRecordEvent.getScrappername());
			x = setupString(update, x, mlRecordEvent.getActiontype());
			x = setupString(update, x, mlRecordEvent.getTextnumber());

			// ML only
			x = setupString(update, x, mlRecordEvent.getMlinputfirstone());
			x = setupString(update, x, mlRecordEvent.getMlplusminusfirstone());
			x = setupString(update, x, mlRecordEvent.getMlinputfirsttwo());
			x = setupString(update, x, mlRecordEvent.getMlplusminusfirsttwo());
			x = setupString(update, x, mlRecordEvent.getMlinputsecondone());
			x = setupString(update, x, mlRecordEvent.getMlplusminussecondone());
			x = setupString(update, x, mlRecordEvent.getMlinputsecondtwo());
			x = setupString(update, x, mlRecordEvent.getMlplusminussecondtwo());
			x = setupLong(update, x, mlRecordEvent.getId());

			// Update SQL
			update.executeUpdate();
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
				closeStreams(conn, update, null);
			} catch (Throwable t) {
				LOGGER.error(t.getMessage(), t);
			}
		}

		LOGGER.info("Exiting updateMlEvent()");
	}

	/**
	 * 
	 * @param spreadid
	 * @return
	 * @throws SQLException
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
	 * @throws SQLException
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
	 * @param value
	 * @param sb
	 * @param sqlInfo
	 */
	private void checkString(String value, StringBuffer sb, String sqlInfo) {
		if (value != null) {
			setupData(sb, sqlInfo);
		}
	}

	/**
	 * 
	 * @param value
	 * @param sb
	 * @param sqlInfo
	 */
	private void checkLong(Long value, StringBuffer sb, String sqlInfo) {
		if (value != null) {
			setupData(sb, sqlInfo);
		}
	}

	/**
	 * 
	 * @param value
	 * @param sb
	 * @param sqlInfo
	 */
	private void checkFloat(Float value, StringBuffer sb, String sqlInfo) {
		if (value != null) {
			setupData(sb, sqlInfo);
		}
	}

	/**
	 * 
	 * @param value
	 * @param sb
	 * @param sqlInfo
	 */
	private void checkInteger(Integer value, StringBuffer sb, String sqlInfo) {
		if (value != null) {
			setupData(sb, sqlInfo);
		}
	}

	/**
	 * 
	 * @param value
	 * @param sb
	 * @param sqlInfo
	 */
	private void checkTimestamp(Date value, StringBuffer sb, String sqlInfo) {
		if (value != null) {
			setupData(sb, sqlInfo);
		}
	}

	/**
	 * 
	 * @param value
	 * @param sb
	 * @param sqlInfo
	 */
	private void checkBoolean(Boolean value, StringBuffer sb, String sqlInfo) {
		if (value != null) {
			setupData(sb, sqlInfo);
		}
	}

	/**
	 * 
	 * @param update
	 * @param x
	 * @param value
	 * @return
	 * @throws SQLException
	 */
	private int setupString(PreparedStatement update, int x, String value)  throws SQLException {
		if (value != null) {
			update.setString(++x, value);
		}
		return x;
	}

	/**
	 * 
	 * @param update
	 * @param x
	 * @param value
	 * @return
	 * @throws SQLException
	 */
	private int setupLong(PreparedStatement update, int x, Long value)  throws SQLException {
		if (value != null) {
			update.setLong(++x, value);
		}
		return x;
	}

	/**
	 * 
	 * @param update
	 * @param x
	 * @param value
	 * @return
	 * @throws SQLException
	 */
	private int setupFloat(PreparedStatement update, int x, Float value)  throws SQLException {
		if (value != null) {
			update.setFloat(++x, value);
		}
		return x;
	}

	/**
	 * 
	 * @param update
	 * @param x
	 * @param value
	 * @return
	 * @throws SQLException
	 */
	private int setupInteger(PreparedStatement update, int x, Integer value)  throws SQLException {
		if (value != null) {
			update.setInt(++x, value);
		}
		return x;
	}

	/**
	 * 
	 * @param update
	 * @param x
	 * @param value
	 * @return
	 * @throws SQLException
	 */
	private int setupTimestamp(PreparedStatement update, int x, Date value)  throws SQLException {
		if (value != null) {
			update.setTimestamp(++x, new java.sql.Timestamp(value.getTime()));
		}
		return x;
	}

	/**
	 * 
	 * @param update
	 * @param x
	 * @param value
	 * @return
	 * @throws SQLException
	 */
	private int setupBoolean(PreparedStatement update, int x, Boolean value)  throws SQLException {
		if (value != null) {
			update.setBoolean(++x, value);
		}
		return x;
	}

	/**
	 * 
	 * @param sb
	 * @param sqlInfo
	 */
	private void setupData(StringBuffer sb, String sqlInfo) {
		if (sb.length() == 0) {
			sb.append(sqlInfo);
		} else {
			sb.append(", " + sqlInfo);
		}
	}
}