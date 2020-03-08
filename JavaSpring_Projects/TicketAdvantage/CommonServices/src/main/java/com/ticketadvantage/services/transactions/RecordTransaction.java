/**
 * 
 */
package com.ticketadvantage.services.transactions;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.ticketadvantage.services.GlobalProperties;
import com.ticketadvantage.services.dao.AccountDAO;
import com.ticketadvantage.services.dao.RecordEventDAO;
import com.ticketadvantage.services.dao.RecordEventDAOImpl;
import com.ticketadvantage.services.dao.sites.SiteWagers;
import com.ticketadvantage.services.db.RecordEventDB;
import com.ticketadvantage.services.errorhandling.AppErrorCodes;
import com.ticketadvantage.services.errorhandling.AppErrorMessage;
import com.ticketadvantage.services.errorhandling.AppException;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.AccountEvent;
import com.ticketadvantage.services.model.Accounts;
import com.ticketadvantage.services.model.BaseRecordEvent;
import com.ticketadvantage.services.model.BaseScrapper;
import com.ticketadvantage.services.model.EventPackage;
import com.ticketadvantage.services.model.MlRecordEvent;
import com.ticketadvantage.services.model.SpreadRecordEvent;
import com.ticketadvantage.services.model.TotalRecordEvent;
import com.ticketadvantage.services.service.AWSBuyOrderEventResource;
import com.ticketadvantage.services.service.AWSTransactionEventResource;
import com.ticketadvantage.services.service.TransactionEventResource;

/**
 * @author jmiller
 *
 */
public class RecordTransaction {
	private static final Logger LOGGER = Logger.getLogger(RecordTransaction.class);
	private RecordEventDB RECORDEVENTDB;
	
	@Autowired
	private AccountDAO accountDAO;

	/**
	 * 
	 * @param recordEventDb
	 */
	public RecordTransaction(RecordEventDB recordEventDb) {
		super();
		RECORDEVENTDB = recordEventDb;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * 
	 * @param lineType
	 * @param gameSport
	 * @param gameType
	 * @param rotation
	 * @param team
	 * @param lineplusminus
	 * @param linedata
	 * @param juiceplusminus
	 * @param juicedata
	 * @param eventdate
	 * @param gamedate
	 * @param customerId
	 * @param scrapper
	 * @param ep
	 */
	public void recordSpreadEvent(String lineType, 
			String gameSport, 
			String gameType, 
			String rotation, 
			String team,
			String lineplusminus, 
			String linedata, 
			String juiceplusminus, 
			String juicedata, 
			String eventdate, 
			Date gamedate,
			String spreadLineAdjustment,
			String spreadJuiceAdjustment,
			String spreadJuiceIndicator,
			String spreadJuice,
			String customerId, 
			String scrapperName,
			String mobileText,
			String playType,
			Long userId,
			String spreadMaxAmount,
			Boolean fullFill,
			Boolean checkDupGame,
			Boolean playOtherSide,
			EventPackage ep,
			Boolean sendtextforaccount) {
		LOGGER.debug("Entering recordSpreadEvent()");
		LOGGER.debug("lineType: " + lineType);
		LOGGER.debug("gameSport: " + gameSport);
		LOGGER.debug("gameType: " + gameType);
		LOGGER.debug("rotation: " + rotation);
		LOGGER.debug("team: " + team);
		LOGGER.debug("lineplusminus: " + lineplusminus);
		LOGGER.debug("linedata: " + linedata);
		LOGGER.debug("juiceplusminus: " + juiceplusminus);
		LOGGER.debug("juicedata: " + juicedata);
		LOGGER.debug("eventdate: " + eventdate); 
		LOGGER.debug("gamedate: " + gamedate);
		LOGGER.debug("spreadLineAdjustment: " + spreadLineAdjustment);
		LOGGER.debug("spreadJuiceAdjustment: " + spreadJuiceAdjustment);
		LOGGER.debug("spreadJuiceIndicator: " + spreadJuiceIndicator);
		LOGGER.debug("spreadJuice: " + spreadJuice);
		LOGGER.debug("customerId: " + customerId);
		LOGGER.debug("scrapperName: " + scrapperName);
		LOGGER.debug("mobileText: " + mobileText);
		LOGGER.debug("playType: " + playType);
		LOGGER.debug("userId: " + userId);
		LOGGER.debug("spreadMaxAmount: " + spreadMaxAmount);
		LOGGER.debug("fullFill: " + fullFill);
		LOGGER.debug("checkDupGame: " + checkDupGame);
		LOGGER.debug("playOtherSide: " + playOtherSide);
		
		try {
			final SpreadRecordEvent spreadRecordEvent = new SpreadRecordEvent();
			String rotationId = setupBaseEvent(spreadRecordEvent, team, lineType, gameSport, gameType, rotation, scrapperName, playType, mobileText, customerId);
			spreadRecordEvent.setEventdatetime(gamedate);
			spreadRecordEvent.setEventtype("spread");
			spreadRecordEvent.setUserid(userId);
			spreadRecordEvent.setAmount(spreadMaxAmount);

			if (ep != null) {
				Integer eoneId = ep.getTeamone().getId();
				Integer etwoId = ep.getTeamtwo().getId();
				Integer rotId = Integer.parseInt(rotationId);
				LOGGER.debug("eoneId: " + eoneId);
				LOGGER.debug("etwoId: " + etwoId);
				LOGGER.debug("rotId: " + rotId);

				if (eoneId.intValue() == rotId.intValue() || etwoId.intValue() == rotId.intValue()) {
					spreadRecordEvent.setRotationid(rotId);
					spreadRecordEvent.setEventid(eoneId);
					spreadRecordEvent.setEventid1(eoneId);
					spreadRecordEvent.setEventid2(etwoId);
					spreadRecordEvent.setEventteam1(ep.getTeamone().getTeam());
					spreadRecordEvent.setEventteam2(ep.getTeamtwo().getTeam());
					spreadRecordEvent.setEventdatetime(ep.getEventdatetime());
					
					// Setup the spread
					// Setup the spread
					setupSpread(lineplusminus, 
							linedata, 
							juiceplusminus, 
							juicedata, 
							rotId, 
							eoneId, 
							etwoId,
							spreadLineAdjustment,
							spreadJuiceAdjustment,
							spreadJuiceIndicator,
							spreadJuice,
							spreadRecordEvent);
				}
			} else {
				Integer rotId = Integer.parseInt(rotationId);
				Integer eoneId = new Integer(0);
				Integer etwoId = new Integer(0);
				String teamOne = "";
				String teamTwo = "";

				if ((rotId.intValue() & 1) == 0) {
					// even...
					eoneId = rotId.intValue() - 1;
					etwoId = rotId.intValue();
					teamOne = teamTwo = team;
				} else {
					// odd...
					eoneId = rotId.intValue();
					etwoId = rotId.intValue() + 1;
					teamOne = teamTwo = team;
				}

				spreadRecordEvent.setRotationid(rotId);
				spreadRecordEvent.setEventid(eoneId);
				spreadRecordEvent.setEventid1(eoneId);
				spreadRecordEvent.setEventid2(etwoId);
				spreadRecordEvent.setEventteam1(teamOne);
				spreadRecordEvent.setEventteam2(teamTwo);

				// Setup the spread
				setupSpread(lineplusminus, 
						linedata, 
						juiceplusminus, 
						juicedata, 
						rotId, 
						eoneId, 
						etwoId,
						spreadLineAdjustment,
						spreadJuiceAdjustment,
						spreadJuiceIndicator,
						spreadJuice,
						spreadRecordEvent);
			}

			// Setup the datetime for now
			spreadRecordEvent.setAttempttime(new Date());
			String event_name = SiteWagers.setupSpreadEventName(spreadRecordEvent);
			spreadRecordEvent.setEventname(event_name);

			// Set the spread event
			if (checkDupGame.booleanValue() && !RECORDEVENTDB.checkDuplicateSpreadEvent(spreadRecordEvent)) {
				LOGGER.debug("SpreadRecordEvent: " + spreadRecordEvent);
				Long id = RECORDEVENTDB.setSpreadEvent(spreadRecordEvent);
				LOGGER.debug("ID: " + id);
				spreadRecordEvent.setId(id);

				// Are we doing "buy" orders?
				LOGGER.error("fullFill: " + fullFill);
				if (fullFill.booleanValue()) {
//					setupBuyOrder(spreadRecordEvent, scrapper);
				} else {
//					setupAccounts(spreadRecordEvent, accounts, enableRetry);
					
					// Check if these need to be run right away
					if (spreadRecordEvent.getAttempts().intValue() == 0) {
						setupImmediateEvents(spreadRecordEvent);
					}
				}
			} else if (playOtherSide.booleanValue() && !RECORDEVENTDB.checkDuplicateReverseSpreadEvent(spreadRecordEvent)) {
				LOGGER.debug("SpreadRecordEvent: " + spreadRecordEvent);
				Long id = RECORDEVENTDB.setSpreadEvent(spreadRecordEvent);
				LOGGER.debug("ID: " + id);
				spreadRecordEvent.setId(id);

				// Are we doing "buy" orders?
				LOGGER.error("fullFill: " + fullFill);
				if (fullFill.booleanValue()) {
//					setupBuyOrder(spreadRecordEvent, scrapper);
				} else {
//					setupAccountEvents(spreadRecordEvent, scrapper);

					// Check if these need to be run right away
					if (spreadRecordEvent.getAttempts().intValue() == 0) {
						setupImmediateEvents(spreadRecordEvent);
					}
				}
			} else {
				LOGGER.warn("Duplicate event being processed");
			}
		} catch (AppException ae) {
			LOGGER.error("AppException in recordSpreadEvent()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in recordSpreadEvent()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}

		LOGGER.debug("Exiting recordSpreadEvent()");
	}

	/**
	 * 
	 * @param lineType
	 * @param gameSport
	 * @param gameType
	 * @param rotation
	 * @param teamOne
	 * @param teamTwo
	 * @param lineplusminus
	 * @param linedata
	 * @param juiceplusminus
	 * @param juicedata
	 * @param eventDate
	 * @param gamedate
	 * @param totalLineAdjustment
	 * @param totalJuiceIndicator
	 * @param totalJuice
	 * @param totalJuiceAdjustment
	 * @param customerId
	 * @param scrapperName
	 * @param mobileText
	 * @param playType
	 * @param userId
	 * @param amount
	 * @param maxAmount
	 * @param fullFill
	 * @param checkDupGame
	 * @param playOtherSide
	 * @param enableRetry
	 * @param orderAmount
	 * @param accounts
	 * @param ep
	 * @param immediate
	 * @param sendtextforaccount
	 * @return
	 */
	public Long recordTotalEvent(String lineType, 
			String gameSport,
			String gameType, 
			String rotation, 
			String teamOne, 
			String teamTwo, 
			String lineplusminus, 
			String linedata,
			String juiceplusminus,
			String juicedata,
			String eventDate,
			Date gamedate,
			String totalLineAdjustment,
			String totalJuiceIndicator,
			String totalJuice,
			String totalJuiceAdjustment,
			String customerId, 
			String scrapperName,
			String mobileText,
			String playType,
			Long userId,
			String amount,
			String maxAmount,
			Boolean fullFill,
			Boolean checkDupGame,
			Boolean playOtherSide,
			Boolean enableRetry,
			Integer orderAmount,
			Set<Accounts> accounts,
			EventPackage ep,
			Boolean immediate,
			Boolean sendtextforaccount,
			Boolean humanspeed) {
		LOGGER.info("Entering recordTotalEvent()");
		LOGGER.error("lineType: " + lineType);
		LOGGER.error("gameSport: " + gameSport);
		LOGGER.error("gameType: " + gameType);
		LOGGER.error("rotation: " + rotation);
		LOGGER.error("teamOne: " + teamOne);
		LOGGER.error("teamTwo: " + teamTwo);
		LOGGER.error("lineplusminus: " + lineplusminus);
		LOGGER.error("linedata: " + linedata);
		LOGGER.error("juiceplusminus: " + lineplusminus);
		LOGGER.error("juicedata: " + linedata);
		LOGGER.error("eventDate: " + eventDate);
		LOGGER.error("gamedate: " + gamedate);
		LOGGER.error("totalLineAdjustment: " + totalLineAdjustment);
		LOGGER.error("totalJuiceIndicator: " + totalJuiceIndicator);
		LOGGER.error("totalJuice: " + totalJuice);
		LOGGER.error("totalJuiceAdjustment: " + totalJuiceAdjustment);
		LOGGER.error("customerId: " + customerId);
		LOGGER.error("scrapperName: " + scrapperName);
		LOGGER.error("mobileText: " + mobileText);
		LOGGER.error("playType: " + playType);
		LOGGER.error("userId: " + userId);
		LOGGER.error("amount: " + amount);
		LOGGER.error("maxAmount: " + maxAmount);
		LOGGER.error("fullFill: " + fullFill);
		LOGGER.error("checkDupGame: " + checkDupGame);
		LOGGER.error("playOtherSide: " + playOtherSide);
		LOGGER.error("enableRetry: " + enableRetry);
		LOGGER.error("orderAmount: " + orderAmount);
		Long id = null;

		try {
			final TotalRecordEvent totalRecordEvent = new TotalRecordEvent();
			String rotationId = setupBaseEvent(totalRecordEvent, 
					"", 
					lineType, 
					gameSport, 
					gameType, 
					rotation, 
					scrapperName, 
					playType, 
					mobileText, 
					customerId);
			totalRecordEvent.setEventdatetime(gamedate);
			totalRecordEvent.setEventtype("total");
			totalRecordEvent.setUserid(userId);
			final Double nAmount = Double.parseDouble(amount);
			final Double nMaxAmount = Double.parseDouble(maxAmount);
			if (nAmount.doubleValue() < nMaxAmount.doubleValue()) {
				totalRecordEvent.setAmount(amount);
			} else {
				totalRecordEvent.setAmount(maxAmount);
			}

			if (ep != null) {
				Integer eoneId = ep.getTeamone().getId();
				Integer etwoId = ep.getTeamtwo().getId();
				Integer rotId = Integer.parseInt(rotationId);
				LOGGER.debug("eoneId: " + eoneId);
				LOGGER.debug("etwoId: " + etwoId);
				LOGGER.debug("rotId: " + rotId);
	
				if (eoneId.intValue() == rotId.intValue() || etwoId.intValue() == rotId.intValue()) {
					totalRecordEvent.setRotationid(rotId);
					totalRecordEvent.setEventid(eoneId);
					totalRecordEvent.setEventid1(eoneId);
					totalRecordEvent.setEventid2(etwoId);
					totalRecordEvent.setEventteam1(ep.getTeamone().getTeam());
					totalRecordEvent.setEventteam2(ep.getTeamtwo().getTeam());
					totalRecordEvent.setEventdatetime(ep.getEventdatetime());

					// Setup the total record
					setupTotal(lineplusminus, 
							linedata, 
							juiceplusminus, 
							juicedata, 
							totalLineAdjustment,
							totalJuiceIndicator,
							totalJuice,
							totalJuiceAdjustment,
							totalRecordEvent);
				}
			} else {
				Integer rotId = Integer.parseInt(rotationId);
				int eoneId = 0;
				int etwoId = 0;
				if ((rotId.intValue() & 1) == 0) {
					// even...
					eoneId = rotId.intValue() - 1;
					etwoId = rotId.intValue();
				} else {
					// odd...
					eoneId = rotId.intValue();
					etwoId = rotId.intValue() + 1;
				}
	
				totalRecordEvent.setRotationid(rotId);
				totalRecordEvent.setEventid(eoneId);
				totalRecordEvent.setEventid1(eoneId);
				totalRecordEvent.setEventid2(etwoId);
				totalRecordEvent.setEventteam1(teamOne);
				totalRecordEvent.setEventteam2(teamTwo);

				// Setup the total record
				setupTotal(lineplusminus, 
						linedata, 
						juiceplusminus, 
						juicedata, 
						totalLineAdjustment,
						totalJuiceIndicator,
						totalJuice,
						totalJuiceAdjustment,
						totalRecordEvent);
			}

			// Setup the datetime for now
			totalRecordEvent.setAttempttime(new Date());
			String event_name = SiteWagers.setupTotalEventName(totalRecordEvent);
			totalRecordEvent.setEventname(event_name);

			// Set the total event
			if (checkDupGame != null && checkDupGame.booleanValue() && !RECORDEVENTDB.checkDuplicateTotalEvent(totalRecordEvent)) {
				// create the Total event in the DB
				id = createTotalEvent(totalRecordEvent, 
						fullFill, 
						accounts, 
						maxAmount, 
						enableRetry, 
						mobileText,
						orderAmount,
						immediate,
						sendtextforaccount,
						humanspeed);
			} else if (playOtherSide != null && playOtherSide.booleanValue() && !RECORDEVENTDB.checkDuplicateReverseTotalEvent(totalRecordEvent)) {
				// create the Total event in the DB
				id = createTotalEvent(totalRecordEvent, 
						fullFill, 
						accounts, 
						maxAmount, 
						enableRetry, 
						mobileText,
						orderAmount,
						immediate,
						sendtextforaccount,
						humanspeed);
			} else {
				// create the Total event in the DB
				id = createTotalEvent(totalRecordEvent, 
						fullFill, 
						accounts, 
						maxAmount, 
						enableRetry, 
						mobileText,
						orderAmount,
						immediate,
						sendtextforaccount,
						humanspeed);
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}

		LOGGER.info("Exiting recordTotalEvent()");
		return id;
	}

	/**
	 * 
	 * @param lineType
	 * @param gameSport
	 * @param gameType
	 * @param rotation
	 * @param teamOne
	 * @param teamTwo
	 * @param lineplusminus
	 * @param linedata
	 * @param juiceplusminus
	 * @param juicedata
	 * @param eventDate
	 * @param gamedate
	 * @param mlLineAdjustment
	 * @param mlIndicator
	 * @param mlLine
	 * @param customerId
	 * @param scrapperName
	 * @param mobileText
	 * @param playType
	 * @param userId
	 * @param amount
	 * @param maxAmount
	 * @param fullFill
	 * @param checkDupGame
	 * @param playOtherSide
	 * @param enableRetry
	 * @param orderAmount
	 * @param accounts
	 * @param ep
	 * @param immediate
	 * @param sendtextforaccount
	 * @return
	 */
	public Long recordMlEvent(String lineType,
			String gameSport,
			String gameType, 
			String rotation, 
			String teamOne,
			String teamTwo,
			String lineplusminus, 
			String linedata,
			String juiceplusminus,
			String juicedata, 
			String eventDate,
			Date gamedate,
			String mlLineAdjustment,
			String mlIndicator,
			String mlLine,
			String customerId, 
			String scrapperName,
			String mobileText,
			String playType,
			Long userId,
			String amount,
			String maxAmount,
			Boolean fullFill,
			Boolean checkDupGame,
			Boolean playOtherSide,
			Boolean enableRetry,
			Integer orderAmount,
			Set<Accounts> accounts,
			EventPackage ep,
			Boolean immediate,
			Boolean sendtextforaccount,
			Boolean humanspeed) {
		LOGGER.info("Entering recordMlEvent()");
		LOGGER.error("lineType: " + lineType);
		LOGGER.error("gameSport: " + gameSport);
		LOGGER.error("gameType: " + gameType);
		LOGGER.error("rotation: " + rotation);
		LOGGER.error("teamOne: " + teamOne);
		LOGGER.error("teamTwo: " + teamTwo);
		LOGGER.error("lineplusminus: " + lineplusminus);
		LOGGER.error("linedata: " + linedata);
		LOGGER.error("juiceplusminus: " + lineplusminus);
		LOGGER.error("juicedata: " + linedata);
		LOGGER.error("eventDate: " + eventDate);
		LOGGER.error("gamedate: " + gamedate);
		LOGGER.error("mlLineAdjustment: " + mlLineAdjustment);
		LOGGER.error("mlIndicator: " + mlIndicator);
		LOGGER.error("mlLine: " + mlLine);
		LOGGER.error("customerId: " + customerId);
		LOGGER.error("scrapperName: " + scrapperName);
		LOGGER.error("mobileText: " + mobileText);
		LOGGER.error("playType: " + playType);
		LOGGER.error("userId: " + userId);
		LOGGER.error("amount: " + amount);
		LOGGER.error("maxAmount: " + maxAmount);
		LOGGER.error("fullFill: " + fullFill);
		LOGGER.error("checkDupGame: " + checkDupGame);
		LOGGER.error("playOtherSide: " + playOtherSide);
		LOGGER.error("enableRetry: " + enableRetry);
		LOGGER.error("orderAmount: " + orderAmount);
		Long id = null;

		try {
			final MlRecordEvent mlRecordEvent = new MlRecordEvent();
			String rotationId = setupBaseEvent(mlRecordEvent, 
					"", 
					lineType, 
					gameSport, 
					gameType, 
					rotation, 
					scrapperName, 
					playType, 
					mobileText,
					customerId);
			mlRecordEvent.setEventdatetime(gamedate);
			mlRecordEvent.setEventtype("ml");
			mlRecordEvent.setUserid(userId);

			final Double nAmount = Double.parseDouble(amount);
			final Double nMaxAmount = Double.parseDouble(maxAmount);
			if (nAmount.doubleValue() < nMaxAmount.doubleValue()) {
				mlRecordEvent.setAmount(amount);
			} else {
				mlRecordEvent.setAmount(maxAmount);
			}

			if (ep != null) {
				// Get all events for event type
				Integer eoneId = ep.getTeamone().getId();
				Integer etwoId = ep.getTeamtwo().getId();
				Integer rotId = Integer.parseInt(rotationId);
				LOGGER.debug("eoneId: " + eoneId);
				LOGGER.debug("etwoId: " + etwoId);
				LOGGER.debug("rotId: " + rotId);
	
				if (eoneId.intValue() == rotId.intValue() || etwoId.intValue() == rotId.intValue()) {
					mlRecordEvent.setRotationid(rotId);
					mlRecordEvent.setEventid(eoneId);
					mlRecordEvent.setEventid1(eoneId);
					mlRecordEvent.setEventid2(etwoId);
					mlRecordEvent.setEventteam1(ep.getTeamone().getTeam());
					mlRecordEvent.setEventteam2(ep.getTeamtwo().getTeam());
					mlRecordEvent.setEventdatetime(ep.getEventdatetime());

					// Setup money line
					setupMoneyLine(juiceplusminus, 
							juicedata, 
							rotId, 
							eoneId, 
							etwoId, 
							mlIndicator,
							mlLine,
							mlLineAdjustment,
							mlRecordEvent);
				}
			} else {
				Integer rotId = Integer.parseInt(rotationId);
				int eoneId = 0;
				int etwoId = 0;
				if ((rotId.intValue() & 1) == 0) {
					// even...
					eoneId = rotId.intValue() - 1;
					etwoId = rotId.intValue();
				} else {
					// odd...
					eoneId = rotId.intValue();
					etwoId = rotId.intValue() + 1;
				}
				LOGGER.debug("eoneId: " + eoneId);
				LOGGER.debug("etwoId: " + etwoId);
				LOGGER.debug("rotId: " + rotId);

				mlRecordEvent.setRotationid(rotId);
				mlRecordEvent.setEventid(eoneId);
				mlRecordEvent.setEventid1(eoneId);
				mlRecordEvent.setEventid2(etwoId);
				mlRecordEvent.setEventteam1(teamOne);
				mlRecordEvent.setEventteam2(teamTwo);

				// Setup money line
				setupMoneyLine(juiceplusminus, 
						juicedata, 
						rotId, 
						eoneId, 
						etwoId, 
						mlIndicator,
						mlLine,
						mlLineAdjustment,
						mlRecordEvent);
			}

			// Setup the datetime for now
			mlRecordEvent.setAttempttime(new Date());
			String event_name = SiteWagers.setupMlEventName(mlRecordEvent);
			mlRecordEvent.setEventname(event_name);

			// Set the ml event
			if (checkDupGame != null && checkDupGame.booleanValue() && !RECORDEVENTDB.checkDuplicateMlEvent(mlRecordEvent)) {
				// create the ML event in the DB
				id = createMlEvent(mlRecordEvent, 
						fullFill, 
						accounts, 
						maxAmount, 
						enableRetry, 
						mobileText,
						orderAmount,
						immediate,
						sendtextforaccount,
						humanspeed);
			} else if (playOtherSide != null && playOtherSide.booleanValue() && !RECORDEVENTDB.checkDuplicateReverseMlEvent(mlRecordEvent)) {
				// create the ML event in the DB
				id = createMlEvent(mlRecordEvent, 
						fullFill, 
						accounts, 
						maxAmount, 
						enableRetry, 
						mobileText,
						orderAmount,
						immediate,
						sendtextforaccount,
						humanspeed);
			} else {
				// create the ML event in the DB
				id = createMlEvent(mlRecordEvent, 
						fullFill, 
						accounts, 
						maxAmount, 
						enableRetry, 
						mobileText,
						orderAmount,
						immediate,
						sendtextforaccount,
						humanspeed);
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}

		LOGGER.info("Exiting recordMlEvent()");
		return id;
	}

	/**
	 * 
	 * @param totalRecordEvent
	 * @param fullFill
	 * @param accounts
	 * @param maxAmount
	 * @param enableRetry
	 * @param mobileText
	 * @param orderAmount
	 * @param immediate
	 * @param sendtextforaccount
	 * @return
	 * @throws BatchException
	 * @throws SQLException
	 */
	protected Long createTotalEvent(TotalRecordEvent totalRecordEvent, 
			Boolean fullFill, 
			Set<Accounts> accounts, 
			String maxAmount, 
			Boolean enableRetry, 
			String mobileText,
			Integer orderAmount,
			Boolean immediate,
			Boolean sendtextforaccount,
			Boolean humanspeed) throws BatchException, SQLException {
		LOGGER.info("Entering createTotalEvent()");
		LOGGER.debug("TotalRecordEvent: " + totalRecordEvent);
		Long id = RECORDEVENTDB.setTotalEvent(totalRecordEvent);
		LOGGER.debug("ID: " + id);
		totalRecordEvent.setId(id);

		// Are we doing "buy" orders?
		if (fullFill != null && fullFill.booleanValue()) {
			this.setupBuyOrderAccounts(totalRecordEvent, 
					accounts, 
					maxAmount, 
					enableRetry, 
					mobileText, 
					orderAmount,
					sendtextforaccount,
					humanspeed);
		} else {
			setupAccounts(totalRecordEvent, accounts, enableRetry);

			// Check if these need to be run right away
			if (totalRecordEvent.getAttempts().intValue() == 0 && immediate.booleanValue()) {
				setupImmediateEvents(totalRecordEvent);
			}
		}

		LOGGER.info("Exiting createTotalEvent()");
		return id;
	}

	/**
	 * 
	 * @param mlRecordEvent
	 * @param fullFill
	 * @param accounts
	 * @param maxAmount
	 * @param enableRetry
	 * @param mobileText
	 * @param orderAmount
	 * @param immediate
	 * @param sendtextforaccount
	 * @return
	 * @throws BatchException
	 * @throws SQLException
	 */
	protected Long createMlEvent(MlRecordEvent mlRecordEvent, 
			Boolean fullFill, 
			Set<Accounts> accounts, 
			String maxAmount, 
			Boolean enableRetry, 
			String mobileText,
			Integer orderAmount,
			Boolean immediate,
			Boolean sendtextforaccount,
			Boolean humanspeed) throws BatchException, SQLException {
		LOGGER.info("Entering createMlEvent()");
		LOGGER.debug("MlRecordEvent: " + mlRecordEvent);
		Long id = RECORDEVENTDB.setMlEvent(mlRecordEvent);
		LOGGER.debug("ID: " + id);
		mlRecordEvent.setId(id);

		// Are we doing "buy" orders?
		LOGGER.error("fullFill: " + fullFill);
		if (fullFill != null && fullFill.booleanValue()) {
			this.setupBuyOrderAccounts(mlRecordEvent, 
					accounts, 
					maxAmount, 
					enableRetry, 
					mobileText, 
					orderAmount, 
					sendtextforaccount,
					humanspeed);
		} else {
			setupAccounts(mlRecordEvent, accounts, enableRetry);

			// Check if these need to be run right away
			if (mlRecordEvent.getAttempts().intValue() == 0 && immediate.booleanValue()) {
				setupImmediateEvents(mlRecordEvent);
			}
		}

		LOGGER.info("Exiting createMlEvent()");
		return id;
	}

	/**
	 * 
	 * @param event
	 * @param accounts
	 * @param enableRetry
	 */
	protected void setupAccounts(BaseRecordEvent event, Set<Accounts> accounts, Boolean enableRetry) {
		LOGGER.info("Entering setupAccounts()");
		LOGGER.debug("BaseRecordEvent: " + event);
		LOGGER.debug("Accounts: " + accounts);
		LOGGER.debug("enableRetry: " + enableRetry);

		// Check to make sure the account list is not empty
		LOGGER.error("accounts: " + accounts);
		if (accounts != null && !accounts.isEmpty()) {
			LOGGER.error("accounts.size(): " + accounts.size());
			final Iterator<Accounts> itr = accounts.iterator();

			while (itr.hasNext()) {
				final Accounts account = itr.next();
				if (account != null) {
					// Populate events
					final AccountEvent accountEvent = populateAccountEvent(event, account, enableRetry);

					try {
						// Setup the account event
						RECORDEVENTDB.setupAccountEvent(accountEvent);
					} catch (Exception e) {
						LOGGER.error(e);
					}
				}
			}
		}

		LOGGER.info("Exiting setupAccounts()");
	}

	/**
	 * 
	 * @param event
	 * @param accounts
	 * @param maxAmount
	 * @param enableRetry
	 * @param mobileText
	 * @param orderAmount
	 */
	protected void setupBuyOrderAccounts(BaseRecordEvent event, 
			Set<Accounts> accounts, 
			String maxAmount,
			Boolean enableRetry,
			String mobileText,
			Integer orderAmount,
			Boolean sendtextforaccount,
			Boolean humanspeed) {
		LOGGER.info("Entering setupBuyOrderAccounts()");
		LOGGER.debug("BaseRecordEvent: " + event);
		LOGGER.debug("Accounts: " + accounts);

		try {
			final AWSBuyOrderEventResource awsBuyOrderEventResource = new AWSBuyOrderEventResource(event, 
					accounts, 
					maxAmount,
					enableRetry,
					mobileText,
					orderAmount,
					RECORDEVENTDB, 
					sendtextforaccount,
					humanspeed);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		LOGGER.info("Exiting setupBuyOrderAccounts()");
	}

	/**
	 * 
	 * @param event
	 */
	protected void setupImmediateEvents(BaseRecordEvent event) throws SQLException {
		LOGGER.info("Entering setupImmediateEvents()");
		LOGGER.debug("Event: " + event);
		
		// First check to see if it's an account or group
		try {
			List<AccountEvent> accountEvents = null;
			if ("spread".equals(event.getEventtype())) {
				accountEvents = RECORDEVENTDB.getSpreadActiveAccountEvents(event.getId());
			} else if ("total".equals(event.getEventtype())) {
				accountEvents = RECORDEVENTDB.getTotalActiveAccountEvents(event.getId());
			} else if ("ml".equals(event.getEventtype())) {
				accountEvents = RECORDEVENTDB.getMlActiveAccountEvents(event.getId());
			}
			LOGGER.debug("AccountEvents: " + accountEvents);

			// Make sure there is at least one
			if (accountEvents != null && accountEvents.size() > 0) {
				final ExecutorService executor = Executors.newFixedThreadPool(accountEvents.size());
				List<Future<?>> futures = new ArrayList<Future<?>>();
				for (int i = 0; i < accountEvents.size(); i++) {
					final AccountEvent ae = accountEvents.get(i);
					if (GlobalProperties.isLocal() || ae.getIscomplexcaptcha()) {
						final TransactionEventResource transactionEventResource = new TransactionEventResource();
						LOGGER.debug("AccountEvent: " + ae);

						transactionEventResource.setAccountEvent(ae);
						final EntityManager entityManager2 = Persistence.createEntityManagerFactory("entityManager").createEntityManager();
						accountDAO.setEntityManager(entityManager2);						
						final RecordEventDAO recordEventDAO = new RecordEventDAOImpl();
						recordEventDAO.setEntityManager(entityManager2);
						transactionEventResource.setAccountDAO(accountDAO);
						transactionEventResource.setRecordEventDAO(recordEventDAO);
						LOGGER.debug("TransactionEventResource: " + transactionEventResource);
						final Runnable worker = transactionEventResource;
						Future<?> f = executor.submit(worker);
						futures.add(f);
						LOGGER.debug("Called executor");
					} else {
						final AWSTransactionEventResource awsTransactionEventResource = new AWSTransactionEventResource();
						LOGGER.debug("AccountEvent: " + ae);

						awsTransactionEventResource.setAccountEventId(ae.getId());
						final Runnable worker = awsTransactionEventResource;
						// executor.execute(worker);

						executor.submit(worker);
					}
				}

				// A) Await all runnables to be done (blocking)
				for(Future<?> future : futures)
				    future.get(); // get will block until the future is done

				// B) Check if all runnables are done (non-blocking)
				boolean allDone = true;
				for(Future<?> future : futures){
				    allDone &= future.isDone(); // check if future is done
				    LOGGER.debug("allDone: " + allDone);
				}

				// Shut down the executor
				executor.shutdown();
			}
		} catch (Exception e) {
			LOGGER.error("Cannot get account event for " + event.getId(), e);
		}

		LOGGER.info("Exiting setupImmediateEvents()");
	}

	/**
	 * 
	 * @param event
	 * @param account
	 * @param enableRetry
	 * @return
	 */
	protected AccountEvent populateAccountEvent(BaseRecordEvent event, Accounts account, Boolean enableRetry) {
		LOGGER.info("Entering populateAccountEvent()");

		final AccountEvent accountEvent = new AccountEvent();
		if ("spread".equals(event.getEventtype())) {
			accountEvent.setSpreadid(event.getId());
			accountEvent.setMaxspreadamount(account.getSpreadlimitamount());
		} else if ("total".equals(event.getEventtype())) {
			accountEvent.setTotalid(event.getId());
			accountEvent.setMaxtotalamount(account.getTotallimitamount());
		} else if ("ml".equals(event.getEventtype())) {
			accountEvent.setMlid(event.getId());
			accountEvent.setMaxmlamount(account.getMllimitamount());
		}
		accountEvent.setAccountid(account.getId());
		accountEvent.setAttempts(event.getAttempts());
		accountEvent.setGroupid(event.getGroupid());
		accountEvent.setEventname(event.getEventname());
		accountEvent.setEventdatetime(event.getEventdatetime());
		accountEvent.setName(account.getName());
		accountEvent.setOwnerpercentage(account.getOwnerpercentage());
		accountEvent.setPartnerpercentage(account.getPartnerpercentage());
		accountEvent.setProxy(account.getProxylocation());
		accountEvent.setSport(event.getSport());
		accountEvent.setStatus("In Progress");
		accountEvent.setTimezone(account.getTimezone());
		accountEvent.setType(event.getEventtype());
		accountEvent.setWagertype(event.getWtype());
		accountEvent.setUserid(event.getUserid());
		accountEvent.setIscomplexcaptcha(account.getIscomplexcaptcha());

		// Check if we should retry on failed attempts
		if (enableRetry) {
			accountEvent.setAttempts(new Integer(11));
		}

		LOGGER.info("Exiting populateAccountEvent()");
		return accountEvent;
	}

	/**
	 * 
	 * @param gamesport
	 * @param gametype
	 * @param eventtype
	 * @param linetype
	 * @param scrapper
	 * @return
	 */
	protected boolean isSportAvailable(String gamesport, String gametype, String eventtype, String linetype, BaseScrapper scrapper) {
		LOGGER.info("Entering isSportAvailable()");
		LOGGER.debug("gamesport: " + gamesport);
		LOGGER.debug("gametype: " + gametype);
		LOGGER.debug("eventtype: " + eventtype);
		LOGGER.debug("linetype: " + linetype);
		boolean retValue = false;

		if (eventtype == null || eventtype.equals("teaser")) {
			return retValue;
		}

		if (gamesport != null) {
			gamesport = gamesport.toLowerCase();
			if (gamesport.contains("football")) {
				if (gametype != null) {
					gametype = gametype.toLowerCase();
					if (gametype.contains("nfl")) {
						LOGGER.debug("gametype: NFL");
						if (eventtype.equals("spread")) {
							if (linetype.equals("game")) {
								if (scrapper.getGameonoff().booleanValue() && scrapper.getNflspreadonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("first")) {
								if (scrapper.getFirstonoff().booleanValue() && scrapper.getNflspreadonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("second")) {
								if (scrapper.getSecondonoff().booleanValue() && scrapper.getNflspreadonoff().booleanValue()) {
									retValue = true;
								}
							}
						} else if (eventtype.equals("total")) {
							if (linetype.equals("game")) {
								if (scrapper.getGameonoff().booleanValue() && scrapper.getNfltotalonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("first")) {
								if (scrapper.getFirstonoff().booleanValue() && scrapper.getNfltotalonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("second")) {
								if (scrapper.getSecondonoff().booleanValue() && scrapper.getNfltotalonoff().booleanValue()) {
									retValue = true;
								}
							}							
						} else if (eventtype.equals("ml")) {
							if (linetype.equals("game")) {
								if (scrapper.getGameonoff().booleanValue() && scrapper.getNflmlonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("first")) {
								if (scrapper.getFirstonoff().booleanValue() && scrapper.getNflmlonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("second")) {
								if (scrapper.getSecondonoff().booleanValue() && scrapper.getNflmlonoff().booleanValue()) {
									retValue = true;
								}
							}							
						}
					} else if (gametype.contains("ncaa") || gametype.contains("college football")) { // NCAA FOOTBALL
						if (eventtype.equals("spread")) {
							if (linetype.equals("game")) {
								if (scrapper.getGameonoff().booleanValue() && scrapper.getNcaafspreadonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("first")) {
								if (scrapper.getFirstonoff().booleanValue() && scrapper.getNcaafspreadonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("second")) {
								if (scrapper.getSecondonoff().booleanValue() && scrapper.getNcaafspreadonoff().booleanValue()) {
									retValue = true;
								}
							}
						} else if (eventtype.equals("total")) {
							if (linetype.equals("game")) {
								if (scrapper.getGameonoff().booleanValue() && scrapper.getNcaaftotalonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("first")) {
								if (scrapper.getFirstonoff().booleanValue() && scrapper.getNcaaftotalonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("second")) {
								if (scrapper.getSecondonoff().booleanValue() && scrapper.getNcaaftotalonoff().booleanValue()) {
									retValue = true;
								}
							}							
						} else if (eventtype.equals("ml")) {
							if (linetype.equals("game")) {
								if (scrapper.getGameonoff().booleanValue() && scrapper.getNcaafmlonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("first")) {
								if (scrapper.getFirstonoff().booleanValue() && scrapper.getNcaafmlonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("second")) {
								if (scrapper.getSecondonoff().booleanValue() && scrapper.getNcaafmlonoff().booleanValue()) {
									retValue = true;
								}
							}							
						}
					}
				}
			} else if (gamesport.contains("basketball")) {
				if (gametype != null) {
					gametype = gametype.toLowerCase();
					if (gametype.contains("wnba")) { // WNBA
						if (eventtype.equals("spread")) {
							if (linetype.equals("game")) {
								if (scrapper.getGameonoff().booleanValue() && scrapper.getWnbaspreadonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("first")) {
								if (scrapper.getFirstonoff().booleanValue() && scrapper.getWnbaspreadonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("second")) {
								if (scrapper.getSecondonoff().booleanValue() && scrapper.getWnbaspreadonoff().booleanValue()) {
									retValue = true;
								}
							}
						} else if (eventtype.equals("total")) {
							if (linetype.equals("game")) {
								if (scrapper.getGameonoff().booleanValue() && scrapper.getWnbatotalonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("first")) {
								if (scrapper.getFirstonoff().booleanValue() && scrapper.getWnbatotalonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("second")) {
								if (scrapper.getSecondonoff().booleanValue() && scrapper.getWnbatotalonoff().booleanValue()) {
									retValue = true;
								}
							}							
						} else if (eventtype.equals("ml")) {
							if (linetype.equals("game")) {
								if (scrapper.getGameonoff().booleanValue() && scrapper.getWnbamlonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("first")) {
								if (scrapper.getFirstonoff().booleanValue() && scrapper.getWnbamlonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("second")) {
								if (scrapper.getSecondonoff().booleanValue() && scrapper.getWnbamlonoff().booleanValue()) {
									retValue = true;
								}
							}
						}
					} else if (gametype.contains("nba")) { // NBA
						if (eventtype.equals("spread")) {
							if (linetype.equals("game")) {
								if (scrapper.getGameonoff().booleanValue()
										&& scrapper.getNbaspreadonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("first")) {
								if (scrapper.getFirstonoff().booleanValue()
										&& scrapper.getNbaspreadonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("second")) {
								if (scrapper.getSecondonoff().booleanValue()
										&& scrapper.getNbaspreadonoff().booleanValue()) {
									retValue = true;
								}
							}
						} else if (eventtype.equals("total")) {
							if (linetype.equals("game")) {
								if (scrapper.getGameonoff().booleanValue()
										&& scrapper.getNbatotalonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("first")) {
								if (scrapper.getFirstonoff().booleanValue()
										&& scrapper.getNbatotalonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("second")) {
								if (scrapper.getSecondonoff().booleanValue()
										&& scrapper.getNbatotalonoff().booleanValue()) {
									retValue = true;
								}
							}
						} else if (eventtype.equals("ml")) {
							if (linetype.equals("game")) {
								if (scrapper.getGameonoff().booleanValue() && scrapper.getNbamlonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("first")) {
								if (scrapper.getFirstonoff().booleanValue()
										&& scrapper.getNbamlonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("second")) {
								if (scrapper.getSecondonoff().booleanValue()
										&& scrapper.getNbamlonoff().booleanValue()) {
									retValue = true;
								}
							}
						}
					} else if (gametype.contains("ncaa")  || gametype.contains("college basketball")) { // NCAA BASKETBALL
						if (eventtype.equals("spread")) {
							if (linetype.equals("game")) {
								if (scrapper.getGameonoff().booleanValue() && scrapper.getNcaabspreadonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("first")) {
								if (scrapper.getFirstonoff().booleanValue() && scrapper.getNcaabspreadonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("second")) {
								if (scrapper.getSecondonoff().booleanValue() && scrapper.getNcaabspreadonoff().booleanValue()) {
									retValue = true;
								}
							}
						} else if (eventtype.equals("total")) {
							if (linetype.equals("game")) {
								if (scrapper.getGameonoff().booleanValue() && scrapper.getNcaabtotalonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("first")) {
								if (scrapper.getFirstonoff().booleanValue() && scrapper.getNcaabtotalonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("second")) {
								if (scrapper.getSecondonoff().booleanValue() && scrapper.getNcaabtotalonoff().booleanValue()) {
									retValue = true;
								}
							}							
						} else if (eventtype.equals("ml")) {
							if (linetype.equals("game")) {
								if (scrapper.getGameonoff().booleanValue() && scrapper.getNcaabmlonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("first")) {
								if (scrapper.getFirstonoff().booleanValue() && scrapper.getNcaabmlonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("second")) {
								if (scrapper.getSecondonoff().booleanValue() && scrapper.getNcaabmlonoff().booleanValue()) {
									retValue = true;
								}
							}							
						}
					}
				}				
			} else if (gamesport.contains("baseball")) {
				if (gametype != null) {
					gametype = gametype.toLowerCase();
					if (gametype.contains("mlb")) { // MLB
						if (eventtype.equals("spread")) {
							if (linetype.equals("game")) {
								if (scrapper.getGameonoff().booleanValue() && scrapper.getMlbspreadonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("first")) {
								if (scrapper.getFirstonoff().booleanValue() && scrapper.getMlbspreadonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("second")) {
								if (scrapper.getSecondonoff().booleanValue() && scrapper.getMlbspreadonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("third")) {
								if (scrapper.getThirdonoff().booleanValue() && scrapper.getMlbspreadonoff().booleanValue()) {
									retValue = true;
								}
							}
						} else if (eventtype.equals("total")) {
							if (linetype.equals("game")) {
								if (scrapper.getGameonoff().booleanValue() && scrapper.getMlbtotalonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("first")) {
								if (scrapper.getFirstonoff().booleanValue() && scrapper.getMlbtotalonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("second")) {
								if (scrapper.getSecondonoff().booleanValue() && scrapper.getMlbtotalonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("third")) {
								if (scrapper.getThirdonoff().booleanValue() && scrapper.getMlbtotalonoff().booleanValue()) {
									retValue = true;
								}
							}							
						} else if (eventtype.equals("ml")) {
							if (linetype.equals("game")) {
								if (scrapper.getGameonoff().booleanValue() && scrapper.getMlbmlonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("first")) {
								if (scrapper.getFirstonoff().booleanValue() && scrapper.getMlbmlonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("second")) {
								if (scrapper.getSecondonoff().booleanValue() && scrapper.getMlbmlonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("third")) {
								if (scrapper.getThirdonoff().booleanValue() && scrapper.getMlbmlonoff().booleanValue()) {
									retValue = true;
								}
							}
						}
					} else if (gametype.contains("international")) { // MLB
						if (eventtype.equals("spread")) {
							if (linetype.equals("game")) {
								if (scrapper.getGameonoff().booleanValue() && scrapper.getInternationalbaseballspreadonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("first")) {
								if (scrapper.getFirstonoff().booleanValue() && scrapper.getInternationalbaseballspreadonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("second")) {
								if (scrapper.getSecondonoff().booleanValue() && scrapper.getInternationalbaseballspreadonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("third")) {
								if (scrapper.getThirdonoff().booleanValue() && scrapper.getInternationalbaseballspreadonoff().booleanValue()) {
									retValue = true;
								}
							}
						} else if (eventtype.equals("total")) {
							if (linetype.equals("game")) {
								if (scrapper.getGameonoff().booleanValue() && scrapper.getInternationalbaseballtotalonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("first")) {
								if (scrapper.getFirstonoff().booleanValue() && scrapper.getInternationalbaseballtotalonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("second")) {
								if (scrapper.getSecondonoff().booleanValue() && scrapper.getInternationalbaseballtotalonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("third")) {
								if (scrapper.getThirdonoff().booleanValue() && scrapper.getInternationalbaseballtotalonoff().booleanValue()) {
									retValue = true;
								}
							}							
						} else if (eventtype.equals("ml")) {
							if (linetype.equals("game")) {
								if (scrapper.getGameonoff().booleanValue() && scrapper.getInternationalbaseballmlonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("first")) {
								if (scrapper.getFirstonoff().booleanValue() && scrapper.getInternationalbaseballmlonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("second")) {
								if (scrapper.getSecondonoff().booleanValue() && scrapper.getInternationalbaseballmlonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("third")) {
								if (scrapper.getThirdonoff().booleanValue() && scrapper.getInternationalbaseballmlonoff().booleanValue()) {
									retValue = true;
								}
							}
						}
					}
				}
			} else if (gamesport.contains("hockey")) {
				if (gametype != null) {
					gametype = gametype.toLowerCase();
					if (gametype.contains("nhl")) { // NHL
						if (eventtype.equals("spread")) {
							if (linetype.equals("game")) {
								if (scrapper.getGameonoff().booleanValue() && scrapper.getNhlspreadonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("first")) {
								if (scrapper.getFirstonoff().booleanValue() && scrapper.getNhlspreadonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("second")) {
								if (scrapper.getSecondonoff().booleanValue() && scrapper.getNhlspreadonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("third")) {
								if (scrapper.getThirdonoff().booleanValue() && scrapper.getNhlspreadonoff().booleanValue()) {
									retValue = true;
								}
							}
						} else if (eventtype.equals("total")) {
							if (linetype.equals("game")) {
								if (scrapper.getGameonoff().booleanValue() && scrapper.getNhltotalonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("first")) {
								if (scrapper.getFirstonoff().booleanValue() && scrapper.getNhltotalonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("second")) {
								if (scrapper.getSecondonoff().booleanValue() && scrapper.getNhltotalonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("third")) {
								if (scrapper.getThirdonoff().booleanValue() && scrapper.getNhltotalonoff().booleanValue()) {
									retValue = true;
								}
							}							
						} else if (eventtype.equals("ml")) {
							if (linetype.equals("game")) {
								if (scrapper.getGameonoff().booleanValue() && scrapper.getNhlmlonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("first")) {
								if (scrapper.getFirstonoff().booleanValue() && scrapper.getNhlmlonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("second")) {
								if (scrapper.getSecondonoff().booleanValue() && scrapper.getNhlmlonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("third")) {
								if (scrapper.getThirdonoff().booleanValue() && scrapper.getNhlmlonoff().booleanValue()) {
									retValue = true;
								}
							}
						}
					}
				}
			}
		}

		LOGGER.info("Exiting isSportAvailable()");
		return retValue;
	}
	
	/**
	 * 
	 * @param lineplusminus
	 * @param linedata
	 * @param juiceplusminus
	 * @param juicedata
	 * @param rotId
	 * @param eoneId
	 * @param etwoId
	 * @param spreadLineAdjustment
	 * @param spreadJuiceAdjustment
	 * @param spreadJuiceIndicator
	 * @param spreadJuice
	 * @param spreadRecordEvent
	 */
	protected void setupSpread(String lineplusminus, 
			String linedata, 
			String juiceplusminus, 
			String juicedata, 
			Integer rotId, 
			Integer eoneId, 
			Integer etwoId, 
			String spreadLineAdjustment,
			String spreadJuiceAdjustment,
			String spreadJuiceIndicator,
			String spreadJuice,
			SpreadRecordEvent spreadRecordEvent) {
		LOGGER.info("Entering setupSpread()");

		String pm = lineplusminus;
		String line = linedata;
		Double sline = null;
		LOGGER.debug("Line: " + line);
		LOGGER.debug("PM: " + pm);

		if ("+".equals(pm)) {
			sline = Double.parseDouble(line);
		} else {
			sline = Double.parseDouble(pm + line);
		}
		double sAdjustment = Double.parseDouble(spreadLineAdjustment);

		if (sline == 100) {
			sline = new Double(0);
		}
		LOGGER.debug("sline: " + sline);
		sline = sline - sAdjustment;
		String spreadValue = "";
		String plmn = "+";
		if (sline < 0) {
			plmn = "-";
			spreadValue = Double.toString(sline);
			spreadValue = spreadValue.substring(1);
		} else {
			spreadValue = Double.toString(sline);
		}
		LOGGER.debug("plmn: " + plmn);
		LOGGER.debug("spreadAmount: " + spreadValue);

		// Now get either a specific juice or an adjustment
		String spreadJuiceplmn = null;
		String spreadJuiceValue = null;
		if (spreadJuice != null && spreadJuice.length() > 0) {
			spreadJuiceplmn = spreadJuiceIndicator;
			spreadJuiceValue = spreadJuice;

			LOGGER.debug("spreadJuiceplmn: " + spreadJuiceplmn);
			LOGGER.debug("spreadJuiceValue: " + spreadJuiceValue);
		} else {
			String spreadJuicepm = juiceplusminus;
			String spreadJuiceData = juicedata;
			Double spreadJuiceNumber = null;
			if ("+".equals(spreadJuicepm)) {
				spreadJuiceNumber = Double.parseDouble(spreadJuiceData);
			} else {
				spreadJuiceNumber = Double.parseDouble(spreadJuicepm + spreadJuiceData);
			}
			double spreadJuiceAdjustmentData = Double.parseDouble(spreadJuiceAdjustment);
			LOGGER.debug("spreadJuiceAdjustmentData: " + spreadJuiceAdjustmentData);
			spreadJuiceNumber = spreadJuiceNumber - spreadJuiceAdjustmentData;
			// Check if we crossed over
			if (spreadJuiceNumber >= 0 && spreadJuiceNumber < 100) {
				// This means we had a + juice now need to convert
				// to negative
				double offset = 100 - spreadJuiceNumber;
				spreadJuiceNumber = -100 - offset;
			}
			spreadJuiceValue = "";
			spreadJuiceplmn = "+";
			if (spreadJuiceNumber < 0) {
				spreadJuiceplmn = "-";
				spreadJuiceValue = Double.toString(spreadJuiceNumber);
				spreadJuiceValue = spreadJuiceValue.substring(1);
			} else {
				spreadJuiceValue = Double.toString(spreadJuiceNumber);
				spreadRecordEvent.setWtype("1"); // Default To Risk
			}
			LOGGER.debug("spreadJuiceplmn: " + spreadJuiceplmn);
			LOGGER.debug("spreadJuiceValue: " + spreadJuiceValue);
		}

		if (eoneId.intValue() == rotId.intValue()) {
			spreadRecordEvent.setSpreadplusminusfirstone(plmn);
			spreadRecordEvent.setSpreadinputfirstone(spreadValue);
			spreadRecordEvent.setSpreadjuiceplusminusfirstone(spreadJuiceplmn);
			spreadRecordEvent.setSpreadinputjuicefirstone(spreadJuiceValue);
		} else if (etwoId.intValue() == rotId.intValue()) {
			spreadRecordEvent.setSpreadplusminussecondone(plmn);
			spreadRecordEvent.setSpreadinputsecondone(spreadValue);
			spreadRecordEvent.setSpreadjuiceplusminussecondone(spreadJuiceplmn);
			spreadRecordEvent.setSpreadinputjuicesecondone(spreadJuiceValue);
		}

		LOGGER.info("Exiting setupSpread()");
	}

	/**
	 * 
	 * @param lineplusminus
	 * @param linedata
	 * @param juiceplusminus
	 * @param juicedata
	 * @param totalLineAdjustment
	 * @param totalJuiceIndicator
	 * @param totalJuice
	 * @param totalJuiceAdjustment
	 * @param totalRecordEvent
	 */
	protected void setupTotal(String lineplusminus, 
			String linedata, 
			String juiceplusminus, 
			String juicedata, 
			String totalLineAdjustment,
			String totalJuiceIndicator,
			String totalJuice,
			String totalJuiceAdjustment,
			TotalRecordEvent totalRecordEvent) {
		LOGGER.info("Entering setupTotal()");

		try {
			final String ou = lineplusminus;
			String line = linedata;
			LOGGER.debug("ou: " + ou);
			LOGGER.debug("line: " + line);
	
			// o75½ u75½
			Double sline = Double.parseDouble(line);
			double tAdjustment = Double.parseDouble(totalLineAdjustment);
			if ("o".equals(ou)) {
				sline = sline + tAdjustment;
			} else {
				sline = sline - tAdjustment;
			}
			LOGGER.debug("sline: " + sline);
			String totalValue = Double.toString(sline);
			LOGGER.debug("totalValue: " + totalValue);
	
			// Get the Juice Adjustment
			String totalJuiceplmn = null;
			String totalJuiceValue = null;
			if (totalJuice != null && totalJuice.length() > 0) {
				totalJuiceplmn = totalJuiceIndicator;
				totalJuiceValue = totalJuice;
	
				LOGGER.debug("totalJuiceplmn: " + totalJuiceplmn);
				LOGGER.debug("totalJuiceValue: " + totalJuiceValue);
			} else {
				String totalJuicepm = juiceplusminus;
				String totalJuiceval = juicedata;
				Double totalJuiceNumber = null;
				if ("+".equals(totalJuicepm)) {
					totalJuiceNumber = Double.parseDouble(totalJuiceval);
				} else {
					totalJuiceNumber = Double.parseDouble(totalJuicepm + totalJuiceval);
				}
				double totalJuiceAdjustmentNumber = Double.parseDouble(totalJuiceAdjustment);
				LOGGER.debug("totalJuiceAdjustmentNumber: " + totalJuiceAdjustmentNumber);
				totalJuiceNumber = totalJuiceNumber - totalJuiceAdjustmentNumber;
				// Check if we crossed over
				if (totalJuiceNumber >= 0 && totalJuiceNumber < 100) {
					// This means we had a + juice now need to convert to
					// negative
					double offset = 100 - totalJuiceNumber;
					totalJuiceNumber = -100 - offset;
				}
	
				totalJuiceplmn = "+";
				totalJuiceValue = "";
				if (totalJuiceNumber < 0) {
					totalJuiceplmn = "-";
					totalJuiceValue = Double.toString(totalJuiceNumber);
					totalJuiceValue = totalJuiceValue.substring(1);
				} else {
					totalJuiceValue = Double.toString(totalJuiceNumber);
					totalRecordEvent.setWtype("1"); // Default To Risk
				}
				LOGGER.debug("totalJuiceplmn: " + totalJuiceplmn);
				LOGGER.debug("totalJuiceValue: " + totalJuiceValue);
			}
	
			if ("o".equals(ou)) {
				// This implies Over
				totalRecordEvent.setTotalinputfirstone(totalValue);
				totalRecordEvent.setTotaljuiceplusminusfirstone(totalJuiceplmn);
				totalRecordEvent.setTotalinputjuicefirstone(totalJuiceValue);
			} else if ("u".equals(ou)) {
				// This implies Under
				totalRecordEvent.setTotalinputsecondone(totalValue);
				totalRecordEvent.setTotaljuiceplusminussecondone(totalJuiceplmn);
				totalRecordEvent.setTotalinputjuicesecondone(totalJuiceValue);
			}
		} catch (Throwable t) {
			LOGGER.error("lineplusminus: " + lineplusminus);
			LOGGER.error("linedata: " + linedata);
			LOGGER.error("juiceplusminus: " + juiceplusminus);
			LOGGER.error("juicedata: " + juicedata);
			LOGGER.error("totalRecordEvent: " + totalRecordEvent);
			LOGGER.error(t.getMessage(), t);
		}

		LOGGER.info("Exiting setupTotal()");
	}

	/**
	 * 
	 * @param juiceplusminus
	 * @param juicedata
	 * @param rotId
	 * @param eoneId
	 * @param etwoId
	 * @param mlIndicator
	 * @param mlLine
	 * @param mlLineAdjustment
	 * @param mlRecordEvent
	 */
	protected void setupMoneyLine(String juiceplusminus, 
			String juicedata, 
			Integer rotId, 
			Integer eoneId, 
			Integer etwoId, 
			String mlIndicator,
			String mlLine,
			String mlLineAdjustment,
			MlRecordEvent mlRecordEvent) {
		LOGGER.info("Entering setupMoneyLine()");

		String pm = null;
		String line = null;
		if (mlLine != null && mlLine.length() > 0) {
			pm = mlIndicator;
			line = mlLine;
		} else {
			pm = juiceplusminus;
			line = juicedata;
			double mlValue = Double.parseDouble(pm + line);
			double smlValue = Double.parseDouble(mlLineAdjustment);
			mlValue = mlValue - smlValue;
			// Check if we crossed over
			if (mlValue >= 0 && mlValue < 100) {
				// This means we had a + juice now need to convert to
				// negative
				double offset = 100 - mlValue;
				mlValue = -100 - offset;
			}

			if (mlValue < 0) {
				pm = "-";
				mlValue = Math.abs(mlValue);
				int mlv = (int) mlValue;
				line = String.valueOf(mlv);
			} else {
				pm = "+";
				int mlv = (int) mlValue;
				line = String.valueOf(mlv);
				mlRecordEvent.setWtype("1"); // Default To Risk
			}
		}

		if (eoneId.intValue() == rotId.intValue()) {
			// This implies Over
			mlRecordEvent.setMlplusminusfirstone(pm);
			mlRecordEvent.setMlinputfirstone(line);
		} else if (etwoId.intValue() == rotId.intValue()) {
			// This implies Under
			mlRecordEvent.setMlplusminussecondone(pm);
			mlRecordEvent.setMlinputsecondone(line);
		}

		LOGGER.info("Exiting setupMoneyLine()");
	}
	
	/**
	 * 
	 * @param baseRecordEvent
	 * @param eventName
	 * @param lineType
	 * @param gameSport
	 * @param gameType
	 * @param rotationId
	 * @param scrappername
	 * @param actiontype
	 * @param textnumber
	 * @param customerId
	 * @return
	 */
	protected String setupBaseEvent(BaseRecordEvent baseRecordEvent, 
			String eventName, 
			String lineType, 
			String gameSport, 
			String gameType, 
			String rotationId, 
			String scrappername, 
			String actiontype, 
			String textnumber, 
			String customerId) {
		LOGGER.debug("Entering setupBaseEvent()");
		baseRecordEvent.setEventname(eventName);
		baseRecordEvent.setWtype("2"); // Default To Win
		baseRecordEvent.setAttempts(0);
		baseRecordEvent.setDatentime(new Date());
		baseRecordEvent.setActiontype(actiontype);
		
		if (scrappername != null && scrappername.length() > 0 && scrappername.contains("PinnacleAgent")) {
			baseRecordEvent.setScrappername(scrappername + " " +  customerId);
		} else {
			baseRecordEvent.setScrappername(scrappername);			
		}
		baseRecordEvent.setTextnumber(textnumber);

		LOGGER.debug("lineType: " + lineType);
		LOGGER.debug("gameType: " + gameType);
		LOGGER.debug("rotationId: " + rotationId);

		if (gameType != null && gameType.length() > 0) {
			if (gameType.contains("NFL")) {
				if ("game".equals(lineType)) {
					baseRecordEvent.setSport("nfllines");
				} else if ("first".equals(lineType)) {
					baseRecordEvent.setSport("nflfirst");
					if (rotationId.length() != 4 && !rotationId.startsWith("1")) {
						rotationId = "1" + rotationId;
					}
				} else if ("second".equals(lineType)) {
					baseRecordEvent.setSport("nflsecond");
					if (rotationId.length() != 4 && !rotationId.startsWith("2")) {
						rotationId = "2" + rotationId;
					}
				}
			} else if (gameType.contains("WNBA")) {
				if ("game".equals(lineType)) {
					baseRecordEvent.setSport("wnbalines");
				} else if ("first".equals(lineType)) {
					baseRecordEvent.setSport("wnbafirst");
					if (rotationId.length() != 4 && !rotationId.startsWith("1")) {
						rotationId = "1" + rotationId;
					}
				} else if ("second".equals(lineType)) {
					baseRecordEvent.setSport("wnbasecond");
					if (rotationId.length() != 4 && !rotationId.startsWith("2")) {
						rotationId = "2" + rotationId;
					}
				}
			} else if (gameType.contains("NBA")) {
				if ("game".equals(lineType)) {
					baseRecordEvent.setSport("nbalines");
				} else if ("first".equals(lineType)) {
					baseRecordEvent.setSport("nbafirst");
					if (rotationId.length() != 4 && !rotationId.startsWith("1")) {
						rotationId = "1" + rotationId;
					}
				} else if ("second".equals(lineType)) {
					baseRecordEvent.setSport("nbasecond");
					if (rotationId.length() != 4 && !rotationId.startsWith("2")) {
						rotationId = "2" + rotationId;
					}
				}
			} else if (gameType.contains("NCAAB") || (gameType + " " + gameSport).contains("NCAAB") ||
					   gameType.contains("NCAA Basketball") || (gameType + " " + gameSport).contains("NCAA Basketball") ||
					   gameType.contains("College Basketball") || (gameType + " " + gameSport).contains("College Basketball") ||
					   gameType.contains("NCAA Added Basketball") || (gameType + " " + gameSport).contains("NCAA Added Basketball")) {
				if ("game".equals(lineType)) {
					baseRecordEvent.setSport("ncaablines");
				} else if ("first".equals(lineType)) {
					baseRecordEvent.setSport("ncaabfirst");
					if (rotationId.length() != 4 && !rotationId.startsWith("1")) {
						rotationId = "1" + rotationId;
					}
				} else if ("second".equals(lineType)) {
					baseRecordEvent.setSport("ncaabsecond");
					if (rotationId.length() != 4 && !rotationId.startsWith("2")) {
						rotationId = "2" + rotationId;
					}
				}
			} else if (gameType.contains("NCAAF") || (gameType + " " + gameSport).contains("NCAAF") || 
					   gameType.contains("NCAA Football") || (gameType + " " + gameSport).contains("NCAA Football") || 
					   gameType.contains("College Football") || (gameType + " " + gameSport).contains("College Football") || 
					   gameType.contains("NCAA Added Football") || (gameType + " " + gameSport).contains("NCAA Added Football")) {
				if ("game".equals(lineType)) {
					baseRecordEvent.setSport("ncaaflines");
				} else if ("first".equals(lineType)) {
					baseRecordEvent.setSport("ncaaffirst");
					if (rotationId.length() != 4 && !rotationId.startsWith("1")) {
						rotationId = "1" + rotationId;
					}
				} else if ("second".equals(lineType)) {
					baseRecordEvent.setSport("ncaafsecond");
					if (rotationId.length() != 4 && !rotationId.startsWith("2")) {
						rotationId = "2" + rotationId;
					}
				}
			} else if (gameType.contains("NHL")) {
				if ("game".equals(lineType)) {
					baseRecordEvent.setSport("nhllines");
				} else if ("first".equals(lineType)) {
					baseRecordEvent.setSport("nhlfirst");
					if (rotationId.length() != 4 && !rotationId.startsWith("1")) {
						rotationId = "1" + rotationId;
					}
				} else if ("second".equals(lineType)) {
					baseRecordEvent.setSport("nhlsecond");
					if (rotationId.length() != 4 && !rotationId.startsWith("2")) {
						rotationId = "2" + rotationId;
					}
				} else if ("third".equals(lineType)) {
					baseRecordEvent.setSport("nhlthird");
					if (rotationId.length() != 4 && !rotationId.startsWith("3")) {
						rotationId = "3" + rotationId;
					}
				}
			} else if (gameType.contains("MLB")) {
				if ("game".equals(lineType)) {
					baseRecordEvent.setSport("mlblines");
				} else if ("first".equals(lineType)) {
					baseRecordEvent.setSport("mlbfirst");
					if (rotationId.length() != 4 && !rotationId.startsWith("1")) {
						rotationId = "1" + rotationId;
					}
				} else if ("second".equals(lineType)) {
					baseRecordEvent.setSport("mlbsecond");
					if (rotationId.length() != 4 && !rotationId.startsWith("2")) {
						rotationId = "2" + rotationId;
					}
				} else if ("third".equals(lineType)) {
					baseRecordEvent.setSport("mlbthird");
					if (rotationId.length() != 4 && !rotationId.startsWith("3")) {
						rotationId = "3" + rotationId;
					}
				}
			} else if (gameType.contains("International Baseball")) {
				if ("game".equals(lineType)) {
					baseRecordEvent.setSport("internationalbaseballlines");
				} else if ("first".equals(lineType)) {
					baseRecordEvent.setSport("internationalbaseballfirst");
					if (rotationId.length() != 6 && !rotationId.startsWith("1")) {
						rotationId = "1" + rotationId;
					}
				} else if ("second".equals(lineType)) {
					baseRecordEvent.setSport("internationalbaseballsecond");
					if (rotationId.length() != 6 && !rotationId.startsWith("2")) {
						rotationId = "2" + rotationId;
					}
				} else if ("third".equals(lineType)) {
					baseRecordEvent.setSport("internationalbaseballthrid");
					if (rotationId.length() != 6 && !rotationId.startsWith("3")) {
						rotationId = "3" + rotationId;
					}
				}
			}
		}

		LOGGER.debug("Exiting setupBaseEvent()");
		return rotationId;
	}
}