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

import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.PendingEvent;

/**
 * @author jmiller
 *
 */
public class PendingEventDB extends BaseDB {
	private static final Logger LOGGER = Logger.getLogger(PendingEventDB.class);

	/**
	 * 
	 */
	public PendingEventDB() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * 
	 * @param pendingEvent
	 * @return
	 * @throws BatchException
	 */
	public PendingEvent persist(PendingEvent pendingEvent) throws BatchException {
		LOGGER.info("Entering persist()");
		LOGGER.debug("PendingEvent: " + pendingEvent);
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {
			StringBuffer sb = new StringBuffer(200);
			sb.append("SELECT pe.* FROM pendingevents pe WHERE pe.ticketnum = ? ")
					.append("AND pe.userid = ? ")
					.append("AND pe.pendingtype = ? ")
					.append("AND pe.eventtype = ? ")
					.append("AND pe.gametype = ? ")
					.append("AND pe.team = ? ")
					.append("AND pe.linetype = ?");
			conn = this.getConnection();
			stmt = conn.prepareStatement(sb.toString());
			stmt.setString(1, pendingEvent.getTicketnum());
			stmt.setLong(2, pendingEvent.getUserid());
			stmt.setString(3, pendingEvent.getPendingtype());
			stmt.setString(4, pendingEvent.getEventtype());
			stmt.setString(5, pendingEvent.getGametype());
			stmt.setString(6, pendingEvent.getTeam());
			stmt.setString(7, pendingEvent.getLinetype());
			resultSet = stmt.executeQuery();
			
			List<PendingEvent> listPendingEvent = new ArrayList<>();
			PendingEvent pEvent = null;
			while (resultSet.next()) {
				pEvent = new PendingEvent();
				pEvent.setAccountid(resultSet.getString("accountid"));
				pEvent.setAccountname(resultSet.getString("accountname"));
				pEvent.setDatecreated(resultSet.getTimestamp("datecreated"));
				pEvent.setDatemodified(resultSet.getTimestamp("datemodified"));
				pEvent.setEventdate(resultSet.getString("eventdate"));
				pEvent.setEventtype(resultSet.getString("eventtype"));
				pEvent.setGamedate(resultSet.getTimestamp("gamedate"));
				pEvent.setGamesport(resultSet.getString("gamesport"));
				pEvent.setGametype(resultSet.getString("gametype"));
				pEvent.setId(resultSet.getLong("id"));
				pEvent.setJuice(resultSet.getString("juice"));
				pEvent.setJuiceplusminus(resultSet.getString("juiceplusminus"));
				pEvent.setLine(resultSet.getString("line"));
				pEvent.setLineplusminus(resultSet.getString("lineplusminus"));
				pEvent.setLinetype(resultSet.getString("linetype"));
				pEvent.setPendingtype(resultSet.getString("pendingtype"));
				pEvent.setRisk(resultSet.getString("risk"));
				pEvent.setRotationid(resultSet.getString("rotationid"));
				pEvent.setTeam(resultSet.getString("team"));
				pEvent.setTicketnum(resultSet.getString("ticketnum"));
				pEvent.setUserid(resultSet.getLong("userid"));
				pEvent.setWin(resultSet.getString("win"));
				pEvent.setCustomerid(resultSet.getString("customerid"));
				pEvent.setInet(resultSet.getString("inet"));
				pEvent.setPitcher1(resultSet.getString("pitcher1"));
				pEvent.setPitcher2(resultSet.getString("pitcher2"));
				pEvent.setListedpitchers(resultSet.getBoolean("listedpitchers"));
				pEvent.setTransactiontype(resultSet.getString("transactiontype"));
				pEvent.setParlaynumber(resultSet.getLong("parlaynumber"));
				pEvent.setAmountrisk(resultSet.getString("amountrisk"));
				pEvent.setAmountwin(resultSet.getString("amountwin"));
				pEvent.setParlayseqnum(resultSet.getInt("parlayseqnum"));
				pEvent.setNumunits(resultSet.getFloat("numunits"));
				pEvent.setIslean(resultSet.getBoolean("islean"));
				listPendingEvent.add(pEvent);
			}
			closeStreams(stmt, resultSet);

			if ((listPendingEvent == null) || (listPendingEvent != null && listPendingEvent.size() == 0)) {
				LOGGER.debug("Persisting PendingEvent");
				// Write out the user to the users tables
				pendingEvent.setId(null);
				sb = new StringBuffer(200);
				sb.append("INSERT into pendingevents (accountid, accountname, datecreated, datemodified, eventdate, eventtype, gamesport, ")
						.append("gametype, juice, juiceplusminus, line, lineplusminus, linetype, pendingtype, risk, rotationid, team, ticketnum, userid, win, doposturl, posturl, dateaccepted, customerid, inet, gamedate, ")
						.append("pitcher1, pitcher2, listedpitchers, transactiontype, parlaynumber, amountrisk, amountwin, parlayseqnum, numunits, islean) ")
						.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
	
				stmt = conn.prepareStatement(sb.toString(), Statement.RETURN_GENERATED_KEYS);
				stmt.setString(1, pendingEvent.getAccountid());
				stmt.setString(2, pendingEvent.getAccountname());
				stmt.setTimestamp(3, new java.sql.Timestamp(new Date().getTime()));
				stmt.setTimestamp(4, new java.sql.Timestamp(new Date().getTime()));
				stmt.setString(5, pendingEvent.getEventdate());
				stmt.setString(6, pendingEvent.getEventtype());
				stmt.setString(7, pendingEvent.getGamesport());
				stmt.setString(8, pendingEvent.getGametype());
				stmt.setString(9, pendingEvent.getJuice());
				stmt.setString(10, pendingEvent.getJuiceplusminus());
				stmt.setString(11, pendingEvent.getLine());
				stmt.setString(12, pendingEvent.getLineplusminus());
				stmt.setString(13, pendingEvent.getLinetype());
				stmt.setString(14, pendingEvent.getPendingtype());
				stmt.setString(15, pendingEvent.getRisk());
				stmt.setString(16, pendingEvent.getRotationid());
				stmt.setString(17, pendingEvent.getTeam());
				stmt.setString(18, pendingEvent.getTicketnum());
				stmt.setLong(19, pendingEvent.getUserid());
				stmt.setString(20, pendingEvent.getWin());
				stmt.setBoolean(21, pendingEvent.getDoposturl());
				stmt.setString(22, pendingEvent.getPosturl());
				stmt.setString(23, pendingEvent.getDateaccepted());
				stmt.setString(24, pendingEvent.getCustomerid());
				stmt.setString(25, pendingEvent.getInet());
				if (pendingEvent.getGamedate() != null) {
					stmt.setTimestamp(26, new java.sql.Timestamp(pendingEvent.getGamedate().getTime()));
				} else {
					Date gameDate = new Date();
					stmt.setTimestamp(26, new java.sql.Timestamp(gameDate.getTime()));
				}
				stmt.setString(27, pendingEvent.getPitcher1());
				stmt.setString(28, pendingEvent.getPitcher2());
				stmt.setBoolean(29, pendingEvent.getListedpitchers());
				stmt.setString(30, pendingEvent.getTransactiontype());
				stmt.setLong(31, pendingEvent.getParlaynumber());
				stmt.setString(32, pendingEvent.getAmountrisk());
				stmt.setString(33, pendingEvent.getAmountwin());
				stmt.setInt(34, pendingEvent.getParlayseqnum());
				stmt.setFloat(35, pendingEvent.getNumunits());
				stmt.setBoolean(36, pendingEvent.getIslean());

				stmt.executeUpdate();
			    resultSet = stmt.getGeneratedKeys();  
			    resultSet.next();
			    long primaryKey = resultSet.getLong(1);
			    pendingEvent.setId(primaryKey);
	
			    closeStreams(stmt, resultSet);
			} else if (listPendingEvent != null && listPendingEvent.size() > 0) {
				LOGGER.debug("Already a pending event");
				pendingEvent = listPendingEvent.get(0);
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

		LOGGER.info("Exiting persist()");
		return pendingEvent;
	}

	/**
	 * 
	 * @param pendingEvent
	 * @return
	 * @throws BatchException
	 */
	public PendingEvent persistParlay(PendingEvent pendingEvent) throws BatchException {
		LOGGER.info("Entering persist()");
		LOGGER.debug("PendingEvent: " + pendingEvent);
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {
			StringBuffer sb = new StringBuffer(200);
			sb.append("SELECT pe.* FROM pendingevents pe WHERE pe.ticketnum = ? ")
					.append("AND pe.userid = ? ")
					.append("AND pe.pendingtype = ? ")
					.append("AND pe.eventtype = ? ")
					.append("AND pe.gametype = ? ")
					.append("AND pe.team = ? ")
					.append("AND pe.linetype = ?")
					.append("AND pe.parlayseqnum = ?")
					.append("AND pe.line = ?")
					.append("AND pe.lineplusminus = ?")
					.append("AND pe.juice = ?")
					.append("AND pe.juiceplusminus = ?");

			conn = this.getConnection();
			stmt = conn.prepareStatement(sb.toString());
			stmt.setString(1, pendingEvent.getTicketnum());
			stmt.setLong(2, pendingEvent.getUserid());
			stmt.setString(3, pendingEvent.getPendingtype());
			stmt.setString(4, pendingEvent.getEventtype());
			stmt.setString(5, pendingEvent.getGametype());
			stmt.setString(6, pendingEvent.getTeam());
			stmt.setString(7, pendingEvent.getLinetype());
			stmt.setInt(8, pendingEvent.getParlayseqnum());
			stmt.setString(9, pendingEvent.getLine());
			stmt.setString(10, pendingEvent.getLineplusminus());
			stmt.setString(11, pendingEvent.getJuice());
			stmt.setString(12, pendingEvent.getJuiceplusminus());
			resultSet = stmt.executeQuery();
			
			List<PendingEvent> listPendingEvent = new ArrayList<>();
			PendingEvent pEvent = null;
			while (resultSet.next()) {
				pEvent = new PendingEvent();
				pEvent.setAccountid(resultSet.getString("accountid"));
				pEvent.setAccountname(resultSet.getString("accountname"));
				pEvent.setDatecreated(resultSet.getTimestamp("datecreated"));
				pEvent.setDatemodified(resultSet.getTimestamp("datemodified"));
				pEvent.setEventdate(resultSet.getString("eventdate"));
				pEvent.setEventtype(resultSet.getString("eventtype"));
				pEvent.setGamedate(resultSet.getTimestamp("gamedate"));
				pEvent.setGamesport(resultSet.getString("gamesport"));
				pEvent.setGametype(resultSet.getString("gametype"));
				pEvent.setId(resultSet.getLong("id"));
				pEvent.setJuice(resultSet.getString("juice"));
				pEvent.setJuiceplusminus(resultSet.getString("juiceplusminus"));
				pEvent.setLine(resultSet.getString("line"));
				pEvent.setLineplusminus(resultSet.getString("lineplusminus"));
				pEvent.setLinetype(resultSet.getString("linetype"));
				pEvent.setPendingtype(resultSet.getString("pendingtype"));
				pEvent.setRisk(resultSet.getString("risk"));
				pEvent.setRotationid(resultSet.getString("rotationid"));
				pEvent.setTeam(resultSet.getString("team"));
				pEvent.setTicketnum(resultSet.getString("ticketnum"));
				pEvent.setUserid(resultSet.getLong("userid"));
				pEvent.setWin(resultSet.getString("win"));
				pEvent.setCustomerid(resultSet.getString("customerid"));
				pEvent.setInet(resultSet.getString("inet"));
				pEvent.setPitcher1(resultSet.getString("pitcher1"));
				pEvent.setPitcher2(resultSet.getString("pitcher2"));
				pEvent.setListedpitchers(resultSet.getBoolean("listedpitchers"));
				pEvent.setTransactiontype(resultSet.getString("transactiontype"));
				pEvent.setParlaynumber(resultSet.getLong("parlaynumber"));
				pEvent.setAmountrisk(resultSet.getString("amountrisk"));
				pEvent.setAmountwin(resultSet.getString("amountwin"));
				pEvent.setParlayseqnum(resultSet.getInt("parlayseqnum"));
				pEvent.setNumunits(resultSet.getFloat("numunits"));
				pEvent.setIslean(resultSet.getBoolean("islean"));
				listPendingEvent.add(pEvent);
			}
			closeStreams(stmt, resultSet);

			if ((listPendingEvent == null) || (listPendingEvent != null && listPendingEvent.size() == 0)) {
				LOGGER.debug("Persisting PendingEvent");
				// Write out the user to the users tables
				pendingEvent.setId(null);
				sb = new StringBuffer(200);
				sb.append("INSERT into pendingevents (accountid, accountname, datecreated, datemodified, eventdate, eventtype, gamesport, ")
						.append("gametype, juice, juiceplusminus, line, lineplusminus, linetype, pendingtype, risk, rotationid, team, ticketnum, userid, win, doposturl, posturl, dateaccepted, customerid, inet, gamedate, ")
						.append("pitcher1, pitcher2, listedpitchers, transactiontype, parlaynumber, amountrisk, amountwin, parlayseqnum, numunits, islean) ")
						.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
	
				stmt = conn.prepareStatement(sb.toString(), Statement.RETURN_GENERATED_KEYS);
				stmt.setString(1, pendingEvent.getAccountid());
				stmt.setString(2, pendingEvent.getAccountname());
				stmt.setTimestamp(3, new java.sql.Timestamp(new Date().getTime()));
				stmt.setTimestamp(4, new java.sql.Timestamp(new Date().getTime()));
				stmt.setString(5, pendingEvent.getEventdate());
				stmt.setString(6, pendingEvent.getEventtype());
				stmt.setString(7, pendingEvent.getGamesport());
				stmt.setString(8, pendingEvent.getGametype());
				stmt.setString(9, pendingEvent.getJuice());
				stmt.setString(10, pendingEvent.getJuiceplusminus());
				stmt.setString(11, pendingEvent.getLine());
				stmt.setString(12, pendingEvent.getLineplusminus());
				stmt.setString(13, pendingEvent.getLinetype());
				stmt.setString(14, pendingEvent.getPendingtype());
				stmt.setString(15, pendingEvent.getRisk());
				stmt.setString(16, pendingEvent.getRotationid());
				stmt.setString(17, pendingEvent.getTeam());
				stmt.setString(18, pendingEvent.getTicketnum());
				stmt.setLong(19, pendingEvent.getUserid());
				stmt.setString(20, pendingEvent.getWin());
				stmt.setBoolean(21, pendingEvent.getDoposturl());
				stmt.setString(22, pendingEvent.getPosturl());
				stmt.setString(23, pendingEvent.getDateaccepted());
				stmt.setString(24, pendingEvent.getCustomerid());
				stmt.setString(25, pendingEvent.getInet());
				if (pendingEvent.getGamedate() != null) {
					stmt.setTimestamp(26, new java.sql.Timestamp(pendingEvent.getGamedate().getTime()));
				} else {
					Date gameDate = new Date();
					stmt.setTimestamp(26, new java.sql.Timestamp(gameDate.getTime()));
				}
				stmt.setString(27, pendingEvent.getPitcher1());
				stmt.setString(28, pendingEvent.getPitcher2());
				stmt.setBoolean(29, pendingEvent.getListedpitchers());
				stmt.setString(30, pendingEvent.getTransactiontype());
				stmt.setLong(31, pendingEvent.getParlaynumber());
				stmt.setString(32, pendingEvent.getAmountrisk());
				stmt.setString(33, pendingEvent.getAmountwin());
				stmt.setInt(34, pendingEvent.getParlayseqnum());
				stmt.setFloat(35, pendingEvent.getNumunits());
				stmt.setBoolean(36, pendingEvent.getIslean());

				stmt.executeUpdate();
			    resultSet = stmt.getGeneratedKeys();  
			    resultSet.next();
			    long primaryKey = resultSet.getLong(1);
			    pendingEvent.setId(primaryKey);
	
			    closeStreams(stmt, resultSet);
			} else if (listPendingEvent != null && listPendingEvent.size() > 0) {
				LOGGER.debug("Already a pending event");
				pendingEvent = listPendingEvent.get(0);
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

		LOGGER.info("Exiting persist()");
		return pendingEvent;
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
			String sqlPendingEvent = "SELECT pe.id FROM pendingevents pe WHERE pe.id = ?";
			conn = this.getConnection();
			stmt = conn.prepareStatement(sqlPendingEvent);
			stmt.setLong(1, id);
			resultSet = stmt.executeQuery();
	
			// Loop through the results
			Long pid = null;
			while (resultSet.next()) {
				pid = resultSet.getLong(1);
			}
			closeStreams(stmt, resultSet);

			if (pid != null) {
				sqlPendingEvent = "DELETE FROM pendingevents where id = ?";
				stmt = conn.prepareStatement(sqlPendingEvent);
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
	 * @param pendingType
	 * @return
	 * @throws BatchException
	 */
	public List<PendingEvent> findPendingEventsByUserIdByType(Long userid, String pendingType) throws BatchException {
		LOGGER.info("Entering findPendingEventsByUserIdByType()");
		final List<PendingEvent> pendingEvents = new ArrayList<PendingEvent>();
		Connection conn = null;
		PendingEvent pendingEvent = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {
			final String sqlDestination = "SELECT pe.* FROM pendingevents pe WHERE pe.userid = ? AND pe.pendingtype = ?";
			
			conn = this.getConnection();
			stmt = conn.prepareStatement(sqlDestination);
			stmt.setLong(1, userid);
			stmt.setString(2, pendingType);
			resultSet = stmt.executeQuery();

			// Loop through the results
			while (resultSet.next()) {
				pendingEvent = new PendingEvent();
				pendingEvent.setAccountid(resultSet.getString("accountid"));
				pendingEvent.setAccountname(resultSet.getString("accountname"));
				pendingEvent.setDatecreated(resultSet.getTimestamp("datecreated"));
				pendingEvent.setDatemodified(resultSet.getTimestamp("datemodified"));
				pendingEvent.setEventdate(resultSet.getString("eventdate"));
				pendingEvent.setEventtype(resultSet.getString("eventtype"));
				pendingEvent.setGamedate(resultSet.getTimestamp("gamedate"));
				pendingEvent.setGamesport(resultSet.getString("gamesport"));
				pendingEvent.setGametype(resultSet.getString("gametype"));
				pendingEvent.setId(resultSet.getLong("id"));
				pendingEvent.setJuice(resultSet.getString("juice"));
				pendingEvent.setJuiceplusminus(resultSet.getString("juiceplusminus"));
				pendingEvent.setLine(resultSet.getString("line"));
				pendingEvent.setLineplusminus(resultSet.getString("lineplusminus"));
				pendingEvent.setLinetype(resultSet.getString("linetype"));
				pendingEvent.setPendingtype(resultSet.getString("pendingtype"));
				pendingEvent.setRisk(resultSet.getString("risk"));
				pendingEvent.setRotationid(resultSet.getString("rotationid"));
				pendingEvent.setTeam(resultSet.getString("team"));
				pendingEvent.setTicketnum(resultSet.getString("ticketnum"));
				pendingEvent.setUserid(resultSet.getLong("userid"));
				pendingEvent.setWin(resultSet.getString("win"));
				pendingEvent.setCustomerid(resultSet.getString("customerid"));
				pendingEvent.setInet(resultSet.getString("inet"));
				pendingEvent.setPitcher1(resultSet.getString("pitcher1"));
				pendingEvent.setPitcher2(resultSet.getString("pitcher2"));
				pendingEvent.setListedpitchers(resultSet.getBoolean("listedpitchers"));
				pendingEvent.setTransactiontype(resultSet.getString("transactiontype"));
				pendingEvent.setParlaynumber(resultSet.getLong("parlaynumber"));
				pendingEvent.setAmountrisk(resultSet.getString("amountrisk"));
				pendingEvent.setAmountwin(resultSet.getString("amountwin"));
				pendingEvent.setParlayseqnum(resultSet.getInt("parlayseqnum"));
				pendingEvent.setNumunits(resultSet.getFloat("numunits"));
				pendingEvent.setIslean(resultSet.getBoolean("islean"));

				pendingEvents.add(pendingEvent);
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

		LOGGER.info("Exiting findPendingEventsByUserIdByType()");
		return pendingEvents;
	}

	/**
	 * 
	 * @param userid
	 * @param ticketnum
	 * @return
	 * @throws BatchException
	 */
	public PendingEvent findPendingEventsByUserIdByTicketnumber(Long userid, String ticketnum) throws BatchException {
		LOGGER.info("Entering findPendingEventsByUserIdByTicketnumber()");
		LOGGER.debug("userid: " + userid);
		LOGGER.debug("ticketnum: " + ticketnum);
		PendingEvent pendingEvent = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {
			final String sqlDestination = "SELECT pe.* FROM pendingevents pe WHERE pe.userid = ? AND pe.ticketnum = ?";
			
			conn = this.getConnection();
			stmt = conn.prepareStatement(sqlDestination);
			stmt.setLong(1, userid);
			stmt.setString(2, ticketnum);
			resultSet = stmt.executeQuery();

			// Loop through the results
			while (resultSet.next()) {
				pendingEvent = new PendingEvent();
				pendingEvent.setAccountid(resultSet.getString("accountid"));
				pendingEvent.setAccountname(resultSet.getString("accountname"));
				pendingEvent.setDatecreated(resultSet.getTimestamp("datecreated"));
				pendingEvent.setDatemodified(resultSet.getTimestamp("datemodified"));
				pendingEvent.setEventdate(resultSet.getString("eventdate"));
				pendingEvent.setEventtype(resultSet.getString("eventtype"));
				pendingEvent.setGamedate(resultSet.getTimestamp("gamedate"));
				pendingEvent.setGamesport(resultSet.getString("gamesport"));
				pendingEvent.setGametype(resultSet.getString("gametype"));
				pendingEvent.setId(resultSet.getLong("id"));
				pendingEvent.setJuice(resultSet.getString("juice"));
				pendingEvent.setJuiceplusminus(resultSet.getString("juiceplusminus"));
				pendingEvent.setLine(resultSet.getString("line"));
				pendingEvent.setLineplusminus(resultSet.getString("lineplusminus"));
				pendingEvent.setLinetype(resultSet.getString("linetype"));
				pendingEvent.setPendingtype(resultSet.getString("pendingtype"));
				pendingEvent.setRisk(resultSet.getString("risk"));
				pendingEvent.setRotationid(resultSet.getString("rotationid"));
				pendingEvent.setTeam(resultSet.getString("team"));
				pendingEvent.setTicketnum(resultSet.getString("ticketnum"));
				pendingEvent.setUserid(resultSet.getLong("userid"));
				pendingEvent.setWin(resultSet.getString("win"));
				pendingEvent.setCustomerid(resultSet.getString("customerid"));
				pendingEvent.setInet(resultSet.getString("inet"));
				pendingEvent.setPitcher1(resultSet.getString("pitcher1"));
				pendingEvent.setPitcher2(resultSet.getString("pitcher2"));
				pendingEvent.setListedpitchers(resultSet.getBoolean("listedpitchers"));
				pendingEvent.setTransactiontype(resultSet.getString("transactiontype"));
				pendingEvent.setParlaynumber(resultSet.getLong("parlaynumber"));
				pendingEvent.setAmountrisk(resultSet.getString("amountrisk"));
				pendingEvent.setAmountwin(resultSet.getString("amountwin"));
				pendingEvent.setParlayseqnum(resultSet.getInt("parlayseqnum"));
				pendingEvent.setNumunits(resultSet.getFloat("numunits"));
				pendingEvent.setIslean(resultSet.getBoolean("islean"));
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

		LOGGER.info("Exiting findPendingEventsByUserIdByTicketnumber()");
		return pendingEvent;
	}
}