/**
 * 
 */
package com.ticketadvantage.services.batch.processevent;

import java.sql.Connection;
import java.sql.DriverManager;
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

import com.ticketadvantage.services.GlobalProperties;
import com.ticketadvantage.services.dao.AccountDAO;
import com.ticketadvantage.services.dao.AccountDAOImpl;
import com.ticketadvantage.services.dao.RecordEventDAO;
import com.ticketadvantage.services.dao.RecordEventDAOImpl;
import com.ticketadvantage.services.dao.sites.SiteProcessor;
import com.ticketadvantage.services.dao.sites.SiteWagers;
import com.ticketadvantage.services.dao.sites.betbuckeye.BetBuckeyeProcessSite;
import com.ticketadvantage.services.dao.sites.linetracker.LineTrackerProcessSite;
import com.ticketadvantage.services.dao.sites.metallica.MetallicaProcessSite;
import com.ticketadvantage.services.dao.sites.tdsports.Green444TDSportsProcessSite;
import com.ticketadvantage.services.db.EventsDB;
import com.ticketadvantage.services.db.PendingEventDB;
import com.ticketadvantage.services.db.RecordEventDB;
import com.ticketadvantage.services.errorhandling.AppErrorCodes;
import com.ticketadvantage.services.errorhandling.AppErrorMessage;
import com.ticketadvantage.services.errorhandling.AppException;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.AccountEvent;
import com.ticketadvantage.services.model.Accounts;
import com.ticketadvantage.services.model.BaseRecordEvent;
import com.ticketadvantage.services.model.EventPackage;
import com.ticketadvantage.services.model.EventsPackage;
import com.ticketadvantage.services.model.MlRecordEvent;
import com.ticketadvantage.services.model.PendingEvent;
import com.ticketadvantage.services.model.Scrapper;
import com.ticketadvantage.services.model.SpreadRecordEvent;
import com.ticketadvantage.services.model.TotalRecordEvent;
import com.ticketadvantage.services.service.AWSTransactionEventResource;
import com.ticketadvantage.services.service.TransactionEventResource;

/**
 * @author jmiller
 *
 */
public class BaseSiteBatch {
	private static final Logger LOGGER = Logger.getLogger(BaseSiteBatch.class);
	protected final PendingEventDB PENDINGEVENTDB = new PendingEventDB();
	protected final EventsDB EVENTSDB = new EventsDB();
	protected final RecordEventDB RECORDEVENTDB = new RecordEventDB();
	protected SiteProcessor siteProcessor = null;
	protected Connection conn = null;

	/**
	 * 
	 */
	public BaseSiteBatch() {
		super();
		LOGGER.info("Entering BaseSiteBatch()");
		LOGGER.info("Exiting BaseSiteBatch()");
		try {
			String url = "jdbc:postgresql://localhost:5432/ticketadvantage";
			String username = "ticketadvantage";
			String password = "ticketadvantage";

			conn = DriverManager.getConnection(url, username, password);
		} catch (SQLException sqle) {
			LOGGER.error(sqle);
		}
	}

	/**
	 * 
	 * @param siteType
	 * @param account
	 */
	protected void determinSiteProcessor(String siteType, Accounts account) throws BatchException {
		LOGGER.info("Entering determinSiteProcessor()");
		LOGGER.info("siteType: " + siteType);
		LOGGER.info("Account: " + account);

		if (siteType != null && "Metallica".equals(siteType)) { 
			siteProcessor = new MetallicaProcessSite(account.getUrl(),
				account.getUsername(), account.getPassword());
		} else if (siteType != null && "BetBuckeye".equals(siteType)) {
			siteProcessor = new BetBuckeyeProcessSite(
				account.getUrl(), account.getUsername(), account.getPassword());
		} else if (siteType != null && "LineTracker".equals(siteType)) {
			siteProcessor = new LineTrackerProcessSite(
				account.getUrl(), account.getUsername(), account.getPassword());
		} else if (siteType != null && "Green444TDSports".equals(siteType)) {
			siteProcessor = new Green444TDSportsProcessSite(
				account.getUrl(), account.getUsername(), account.getPassword());
		}

		// Setup the site processor
		siteProcessor.setTimezone(account.getTimezone());
		siteProcessor.getHttpClientWrapper().setupHttpClient(account.getProxylocation());
		siteProcessor.setProcessTransaction(false);
		siteProcessor.loginToSite(siteProcessor.getHttpClientWrapper().getUsername(),
				siteProcessor.getHttpClientWrapper().getPassword());

		LOGGER.info("Exiting determinSiteProcessor()");
	}

	/**
	 * 
	 * @param pendingType
	 * @param siteListItr
	 * @param userScrapper
	 * @param entityManager
	 */
	protected void checkSitePendingEvent(String pendingType, Set<PendingEvent> sitePendingEvents, Scrapper scrapper) {
		LOGGER.info("Entering checkSitePendingEvent()");

		// Now loop through the site pending events and
		// compare to users pending events
		for (PendingEvent sitePendingEvent : sitePendingEvents) {
			boolean found = false;
			PendingEvent copySitePendingEvent = new PendingEvent();
			copySitePendingEvent = copyPendingEvent(sitePendingEvent, copySitePendingEvent);

			// Set attributes on event
			copySitePendingEvent.setUserid(scrapper.getUserid());
			copySitePendingEvent.setDatecreated(new Date());
			copySitePendingEvent.setDatemodified(new Date());
			copySitePendingEvent.setPendingtype(pendingType);

			// Get the users pending events from DB
			try {
				final List<PendingEvent> usersPendingEvents = PENDINGEVENTDB.findPendingEventsByUserIdByType(scrapper.getUserid(), pendingType);
				LOGGER.debug("usersPendingEvents: " + usersPendingEvents);
	
				if (usersPendingEvents != null && !usersPendingEvents.isEmpty()) {
					// Now loop through the users pending events
					for (int j = 0; j < usersPendingEvents.size(); j++) {
						final PendingEvent userPendingEvent = usersPendingEvents.get(j);
						if (copySitePendingEvent.getTicketnum().equals(userPendingEvent.getTicketnum()) &&
							copySitePendingEvent.getUserid().equals(userPendingEvent.getUserid()) &&
							copySitePendingEvent.getPendingtype().equals(userPendingEvent.getPendingtype()) &&
							copySitePendingEvent.getEventtype().equals(userPendingEvent.getEventtype()) &&
							copySitePendingEvent.getGametype().equals(userPendingEvent.getGametype()) &&
							copySitePendingEvent.getTeam().equals(userPendingEvent.getTeam()) &&
							copySitePendingEvent.getLinetype().equals(userPendingEvent.getLinetype())) {
							// This means we don't have an update
							found = true;
						}
					}
				} else {
					found = false;
				}
			} catch (Exception e) {
				LOGGER.error(e);
			}
			LOGGER.debug("found: " + found);

			try {
				if (!found) {
					// Check for a post call if need be
					if (copySitePendingEvent.getDoposturl()) {
						// Write this event to the DB for this user
						siteProcessor.doProcessPendingEvent(copySitePendingEvent);						
					}

					copySitePendingEvent = PENDINGEVENTDB.persist(copySitePendingEvent);
					LOGGER.error("Persisted event: " + copySitePendingEvent);
					conn.commit();
				}
			} catch (Throwable t) {
				LOGGER.error(t);
			}
			LOGGER.debug("copySitePendingEvent: " + copySitePendingEvent);										

			// If we determine that this is new, create the transaction
			if (!found) {
				LOGGER.error("PendingEvent not found: " + copySitePendingEvent);
				placeTransactions(scrapper, copySitePendingEvent);
			}
		}

		LOGGER.info("Exiting checkSitePendingEvent()");
	}

	/**
	 * 
	 * @param pendingType
	 * @param sitePendingEvents
	 * @param scrapper
	 */
	protected void deleteRemovedPendingEvents(String pendingType, Set<PendingEvent> sitePendingEvents, Scrapper scrapper) throws BatchException {
		// Get the pending events
		final List<PendingEvent> usersPendingEvents = PENDINGEVENTDB
				.findPendingEventsByUserIdByType(scrapper.getUserid(), pendingType);
		LOGGER.debug("usersPendingEvents: " + usersPendingEvents);

		// Now loop through the users pending events
		for (PendingEvent userPendingEvent : usersPendingEvents) {
			boolean found = false;

			// Now loop through the site pending events and compare to users pending events
			for (PendingEvent sitePendingDeleteEvent : sitePendingEvents) {
				PendingEvent copySitePendingDeleteEvent = new PendingEvent();
				copySitePendingDeleteEvent = this.copyPendingEvent(sitePendingDeleteEvent, copySitePendingDeleteEvent);
				copySitePendingDeleteEvent.setUserid(scrapper.getUserid());
				copySitePendingDeleteEvent.setPendingtype(pendingType);
				LOGGER.debug("copySitePendingDeleteEvent: " + copySitePendingDeleteEvent);
				LOGGER.debug("userPendingEvent: " + userPendingEvent);
				if (copySitePendingDeleteEvent.getTicketnum().equals(userPendingEvent.getTicketnum()) &&
					copySitePendingDeleteEvent.getUserid().equals(userPendingEvent.getUserid()) &&
					copySitePendingDeleteEvent.getPendingtype().equals(userPendingEvent.getPendingtype()) &&
					copySitePendingDeleteEvent.getEventtype().equals(userPendingEvent.getEventtype()) &&
					copySitePendingDeleteEvent.getGametype().equals(userPendingEvent.getGametype()) &&
					copySitePendingDeleteEvent.getTeam().equals(userPendingEvent.getTeam()) &&
					copySitePendingDeleteEvent.getLinetype().equals(userPendingEvent.getLinetype())) {
					// This means we don't have an update
					found = true;
				}
			}
			LOGGER.debug("found: " + found);

			// Check if we need to delete the event
			if (!found) {
				try {
					// Loop through the sources to see if it fits
					for (Accounts acct : scrapper.getSources()) {
						LOGGER.debug("userPendingEvent: " + userPendingEvent);
						LOGGER.debug("acct: " + acct);

						LOGGER.debug("userPendingEvent.getAccountname(): " + userPendingEvent.getAccountname() + " end");
						LOGGER.debug("acct.getUrl(): " + acct.getUrl() + " end");
						LOGGER.debug("userPendingEvent.getAccountid(): " + userPendingEvent.getAccountid() + " end"); 
						LOGGER.debug("acct.getUsername(): " + acct.getUsername() + " end"); 
						LOGGER.debug("userPendingEvent.getUserid(): " + userPendingEvent.getUserid() + " end");
						LOGGER.debug("scrapper.getUserid(): " + scrapper.getUserid() + " end");

						if (userPendingEvent != null && 
							(userPendingEvent.getAccountname() != null && acct.getUrl().contains(userPendingEvent.getAccountname())) &&
							(userPendingEvent.getAccountid() != null && userPendingEvent.getAccountid().equals(acct.getUsername())) && 
							(userPendingEvent.getUserid() != null && userPendingEvent.getUserid().equals(scrapper.getUserid()))) {
							// Delete this event for this user/acct
							LOGGER.debug("Deleting pending event!");
							PENDINGEVENTDB.delete(userPendingEvent.getId());
							conn.commit();
						}
					}
				} catch (Throwable t) {
					LOGGER.error(t);
				}
			}
		}
	}

	/**
	 * 
	 * @param scrapper
	 * @param pendingEvent
	 */
	protected void placeTransactions(Scrapper scrapper, PendingEvent pendingEvent) {
		LOGGER.info("Entering placeTransactions()");
		final String type = pendingEvent.getEventtype();

		try {
			if ("spread".equals(type)) {
				recordSpreadEvent(pendingEvent, scrapper);
			} else if ("total".equals(type)) {
				recordTotalEvent(pendingEvent, scrapper);
			} else if ("ml".equals(type)) {
				recordMlEvent(pendingEvent, scrapper);
			}
		} catch (Throwable t) {
			LOGGER.error(t);
		} finally {
		}
		LOGGER.info("Exiting placeTransactions()");
	} 

	/**
	 * 
	 * @param baseRecordEvent
	 * @param pendingEvent
	 * @return
	 */
	private String setupBaseEvent(BaseRecordEvent baseRecordEvent, PendingEvent pendingEvent) {
		LOGGER.debug("Entering setupBaseEvent()");
		baseRecordEvent.setEventname(pendingEvent.getTeam());
		baseRecordEvent.setWtype("2"); // Default To Win
		baseRecordEvent.setAttempts(0);
		baseRecordEvent.setDatentime(new Date());

		String lineType = pendingEvent.getLinetype();
		String gameType = pendingEvent.getGametype();
		String rotationId = pendingEvent.getRotationid();
		LOGGER.debug("lineType: " + lineType);
		LOGGER.debug("gameType: " + gameType);
		LOGGER.debug("rotationId: " + rotationId);

		if ("NFL".equals(gameType)) {
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
		} else if ("NBA".equals(gameType)) {
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
		} else if ("NCAA Basketball".equals(gameType)) {
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
		} else if ("NCAA Football".equals(gameType)) {
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
		} else if ("WNBA".equals(gameType)) {
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
		} else if ("NHL".equals(gameType)) {
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
		} else if ("MLB".equals(gameType)) {
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
		}

		LOGGER.debug("Exiting setupBaseEvent()");
		return rotationId;
	}

	/**
	 * 
	 * @param pendingEvent
	 */
	private void recordSpreadEvent(PendingEvent pendingEvent, Scrapper scrapper) {
		LOGGER.debug("Entering recordSpreadEvent()");

		try {
			SpreadRecordEvent spreadRecordEvent = new SpreadRecordEvent();
			String lineType = pendingEvent.getLinetype();
			String gameType = pendingEvent.getGametype();
			LOGGER.debug("lineType: " + lineType);
			LOGGER.debug("gameType: " + gameType);
			String rotationId = setupBaseEvent(spreadRecordEvent, pendingEvent);
			spreadRecordEvent.setEventtype("spread");
			spreadRecordEvent.setUserid(scrapper.getUserid());
			spreadRecordEvent.setAmount(scrapper.getSpreadmaxamount());

			// Get all events for event type
			LOGGER.debug("rotationId: " + rotationId);
			EventsPackage eventsPackage = EVENTSDB.findEvents(rotationId);
			LOGGER.debug("eventsPackage: " + eventsPackage);
			if (eventsPackage != null && eventsPackage.getEvents() != null) {
				Set<EventPackage> events = eventsPackage.getEvents();
				Iterator<EventPackage> itr = events.iterator();
				while (itr.hasNext()) {
					final EventPackage ep = itr.next();
					Integer eoneId = ep.getTeamone().getId();
					Integer etwoId = ep.getTeamtwo().getId();
					Integer rotId = Integer.parseInt(rotationId);
					LOGGER.debug("eoneId: " + eoneId);
					LOGGER.debug("etwoId: " + etwoId);
					LOGGER.debug("rotId: " + rotId);

					if (eoneId.intValue() == rotId.intValue() || etwoId.intValue() == rotId.intValue()) {
						spreadRecordEvent.setEventid(eoneId);
						spreadRecordEvent.setEventid1(eoneId);
						spreadRecordEvent.setEventid2(etwoId);
						spreadRecordEvent.setEventteam1(ep.getTeamone().getTeam());
						spreadRecordEvent.setEventteam2(ep.getTeamtwo().getTeam());
						spreadRecordEvent.setEventdatetime(ep.getEventdatetime());
	
						String pm = pendingEvent.getLineplusminus();
						String line = pendingEvent.getLine();
						Double sline = null;
						LOGGER.debug("Line: " + line);
						LOGGER.debug("PM: " + pm);
	
						if ("+".equals(pm)) {
							sline = Double.parseDouble(line);
						} else {
							sline = Double.parseDouble(pm + line);
						}
						int sAdjustment = Integer.parseInt(scrapper.getSpreadlineadjustment());
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
	
						if (eoneId.intValue() == rotId.intValue()) {						
							spreadRecordEvent.setSpreadplusminusfirstone(plmn);
							spreadRecordEvent.setSpreadinputfirstone(spreadValue);
							spreadRecordEvent.setSpreadjuiceplusminusfirstone(scrapper.getSpreadjuiceindicator());						
							spreadRecordEvent.setSpreadinputjuicefirstone(scrapper.getSpreadjuice());
						} else if (etwoId.intValue() == rotId.intValue()) {
							spreadRecordEvent.setSpreadplusminussecondone(plmn);
							spreadRecordEvent.setSpreadinputsecondone(spreadValue);
							spreadRecordEvent.setSpreadjuiceplusminussecondone(scrapper.getSpreadjuiceindicator());						
							spreadRecordEvent.setSpreadinputjuicesecondone(scrapper.getSpreadjuice());
						}
					}
				}
			} else {
				LOGGER.debug("Not found!");
			}

			// Setup the datetime for now
			spreadRecordEvent.setAttempttime(spreadRecordEvent.getDatentime());
			String event_name = SiteWagers.setupSpreadEventName(spreadRecordEvent);
			spreadRecordEvent.setEventname(event_name);

			// Set the spread event
			if (!RECORDEVENTDB.checkDuplicateSpreadEvent(spreadRecordEvent)) {
				Long id = RECORDEVENTDB.setSpreadEvent(spreadRecordEvent);
				LOGGER.debug("ID: " + id);
				spreadRecordEvent.setId(id);
				conn.commit();
				setupAccountEvents(spreadRecordEvent, scrapper);
				
				// Check if these need to be run right away
				if (spreadRecordEvent.getAttempts().intValue() == 0) {
					setupImmediateEvents(spreadRecordEvent);
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
	 * @param pendingEvent
	 * @param scrapper
	 * @throws AppException
	 */
	public void recordTotalEvent(PendingEvent pendingEvent, Scrapper scrapper) throws AppException {
		LOGGER.info("Entering recordTotalEvent()");

		try {
			TotalRecordEvent totalRecordEvent = new TotalRecordEvent();
			String lineType = pendingEvent.getLinetype();
			String gameType = pendingEvent.getGametype();
			LOGGER.debug("lineType: " + lineType);
			LOGGER.debug("gameType: " + gameType);
			String rotationId = setupBaseEvent(totalRecordEvent, pendingEvent);
			totalRecordEvent.setEventtype("total");
			totalRecordEvent.setUserid(scrapper.getUserid());
			totalRecordEvent.setAmount(scrapper.getTotalmaxamount());

			// Get all events for event type
			EventsPackage eventsPackage = EVENTSDB.findEvents(rotationId);
			if (eventsPackage != null && eventsPackage.getEvents() != null) {
				LOGGER.debug("Events not null");
				Set<EventPackage> events = eventsPackage.getEvents();
				Iterator<EventPackage> itr = events.iterator();
				while (itr.hasNext()) {
					final EventPackage ep = itr.next();
					Integer eoneId = ep.getTeamone().getId();
					Integer etwoId = ep.getTeamtwo().getId();
					Integer rotId = Integer.parseInt(rotationId);
					LOGGER.debug("eoneId: " + eoneId);
					LOGGER.debug("etwoId: " + etwoId);
					LOGGER.debug("rotId: " + rotId);

					if (eoneId.intValue() == rotId.intValue() || etwoId.intValue() == rotId.intValue()) {
						final String ou = pendingEvent.getLineplusminus();
						String line = pendingEvent.getLine();
						LOGGER.debug("ou: " + ou);
						LOGGER.debug("line: " + line);

						// o75½ u75½
						Double sline = Double.parseDouble(line);
						int tAdjustment = Integer.parseInt(scrapper.getTotallineadjustment());
						if ("o".equals(ou)) {
							sline = sline + tAdjustment;
						} else {
							sline = sline - tAdjustment;
						}
						LOGGER.debug("sline: " + sline);
						String totalValue = Double.toString(sline);
						LOGGER.debug("totalValue: " + totalValue);
						totalRecordEvent.setEventid(eoneId);
						totalRecordEvent.setEventid1(eoneId);
						totalRecordEvent.setEventid2(etwoId);
						totalRecordEvent.setEventteam1(ep.getTeamone().getTeam());
						totalRecordEvent.setEventteam2(ep.getTeamtwo().getTeam());
						totalRecordEvent.setEventdatetime(ep.getEventdatetime());
	
						if ("o".equals(ou)) {
							// This implies Over
							totalRecordEvent.setTotalinputfirstone(totalValue);
							totalRecordEvent.setTotaljuiceplusminusfirstone(scrapper.getTotaljuiceindicator());
							totalRecordEvent.setTotalinputjuicefirstone(scrapper.getTotaljuice());							
						} else if ("u".equals(ou)) {
							// This implies Under
							totalRecordEvent.setTotalinputsecondone(totalValue);
							totalRecordEvent.setTotaljuiceplusminussecondone(scrapper.getTotaljuiceindicator());
							totalRecordEvent.setTotalinputjuicesecondone(scrapper.getTotaljuice());							
						}
					}
				}
			}

			// Setup the datetime for now
			totalRecordEvent.setAttempttime(totalRecordEvent.getDatentime());
			String event_name = SiteWagers.setupTotalEventName(totalRecordEvent);
			totalRecordEvent.setEventname(event_name);

			// Set the total event
			if (!RECORDEVENTDB.checkDuplicateTotalEvent(totalRecordEvent)) {
				Long id = RECORDEVENTDB.setTotalEvent(totalRecordEvent);
				LOGGER.debug("ID: " + id);
				totalRecordEvent.setId(id);
				conn.commit();
				setupAccountEvents(totalRecordEvent, scrapper);

				// Check if these need to be run right away
				if (totalRecordEvent.getAttempts().intValue() == 0) {
					setupImmediateEvents(totalRecordEvent);
				}
			} else {
				LOGGER.warn("Duplicate event being processed");
			}
		} catch (AppException ae) {
			LOGGER.error("AppException in recordTotalEvent()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in recordTotalEvent()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}

		LOGGER.info("Exiting recordTotalEvent()");
	}

	/**
	 * 
	 * @param pendingEvent
	 * @return
	 * @throws AppException
	 */
	public void recordMlEvent(PendingEvent pendingEvent, Scrapper scrapper) throws AppException {
		LOGGER.info("Entering recordMlEvent()");

		try {
			final MlRecordEvent mlRecordEvent = new MlRecordEvent();
			final String lineType = pendingEvent.getLinetype();
			final String gameType = pendingEvent.getGametype();
			LOGGER.debug("lineType: " + lineType);
			LOGGER.debug("gameType: " + gameType);

			final String rotationId = setupBaseEvent(mlRecordEvent, pendingEvent);
			mlRecordEvent.setEventtype("ml");
			mlRecordEvent.setUserid(scrapper.getUserid());
			mlRecordEvent.setAmount(scrapper.getMlmaxamount());

			// Get all events for event type
			EventsPackage eventsPackage = EVENTSDB.findEvents(rotationId);
			if (eventsPackage != null && eventsPackage.getEvents() != null) {
				Set<EventPackage> events = eventsPackage.getEvents();
				Iterator<EventPackage> itr = events.iterator();
				while (itr.hasNext()) {
					final EventPackage ep = itr.next();
					Integer eoneId = ep.getTeamone().getId();
					Integer etwoId = ep.getTeamtwo().getId();
					Integer rotId = Integer.parseInt(rotationId);
					LOGGER.debug("eoneId: " + eoneId);
					LOGGER.debug("etwoId: " + etwoId);
					LOGGER.debug("rotId: " + rotId);

					if (eoneId.intValue() == rotId.intValue() || etwoId.intValue() == rotId.intValue()) {
						String pm = pendingEvent.getJuiceplusminus();
						String line = pendingEvent.getJuice();
						double mlValue = Double.parseDouble(pm + line);
						double smlValue = Double.parseDouble(scrapper.getMllineadjustment());
						mlValue = mlValue - smlValue;
						
						if (mlValue < 0) {
							pm = "-";
							mlValue = Math.abs(mlValue);
							int mlv = (int)mlValue;
							line = String.valueOf(mlv);
						} else {
							pm = "+";
							int mlv = (int)mlValue;
							line = String.valueOf(mlv);							
						}

						mlRecordEvent.setEventid(eoneId);
						mlRecordEvent.setEventid1(eoneId);
						mlRecordEvent.setEventid2(etwoId);
						mlRecordEvent.setEventteam1(ep.getTeamone().getTeam());
						mlRecordEvent.setEventteam2(ep.getTeamtwo().getTeam());
						LOGGER.debug("ep.getEventdatetime(): " + ep.getEventdatetime());
						mlRecordEvent.setEventdatetime(ep.getEventdatetime());
	
						if (eoneId.intValue() == rotId.intValue()) {
							// This implies Over
							mlRecordEvent.setMlplusminusfirstone(pm);
							mlRecordEvent.setMlinputfirstone(line);
						} else if (etwoId.intValue() == rotId.intValue()) {
							// This implies Under
							mlRecordEvent.setMlplusminussecondone(pm);
							mlRecordEvent.setMlinputsecondone(line);
						}
					}
				}
			}

			// Setup the datetime for now
			mlRecordEvent.setAttempttime(mlRecordEvent.getDatentime());
			String event_name = SiteWagers.setupMlEventName(mlRecordEvent);
			mlRecordEvent.setEventname(event_name);

			// Set the ml event
			if (!RECORDEVENTDB.checkDuplicateMlEvent(mlRecordEvent)) {
				Long id = RECORDEVENTDB.setMlEvent(mlRecordEvent);
				LOGGER.debug("ID: " + id);
				mlRecordEvent.setId(id);
				conn.commit();
				setupAccountEvents(mlRecordEvent, scrapper);

				// Check if these need to be run right away
				if (mlRecordEvent.getAttempts().intValue() == 0) {
					setupImmediateEvents(mlRecordEvent);
				}
			} else {
				LOGGER.warn("Duplicate event being processed");
			}
		} catch (AppException ae) {
			LOGGER.error("AppException in recordMlEvent()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in recordMlEvent()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}

		LOGGER.info("Exiting recordMlEvent()");
	}

	/**
	 * 
	 * @param event
	 */
	private void setupAccountEvents(BaseRecordEvent event, Scrapper scrapper) {
		LOGGER.info("Entering setupAccountEvents()");
		LOGGER.debug("Event: " + event);

		// Setup all the accounts
		setupAccounts(event, scrapper.getDestinations());

		LOGGER.info("Exiting setupAccountEvents()");
	}

	/**
	 * 
	 * @param event
	 */
	private void setupImmediateEvents(BaseRecordEvent event) throws SQLException {
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
			conn.commit();

			// Make sure there is at least one
			if (accountEvents != null && accountEvents.size() > 0) {
				final ExecutorService executor = Executors.newFixedThreadPool(accountEvents.size());
				List<Future<?>> futures = new ArrayList<Future<?>>();
				for (int i = 0; i < accountEvents.size(); i++) {
					if (GlobalProperties.isLocal()) {
						final TransactionEventResource transactionEventResource = new TransactionEventResource();
						final AccountEvent ae = accountEvents.get(i);
						LOGGER.debug("AccountEvent: " + ae);

						transactionEventResource.setAccountEvent(ae);
						final EntityManager entityManager2 = Persistence.createEntityManagerFactory("entityManager").createEntityManager();
						final AccountDAO accountDAO = new AccountDAOImpl();
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
						final AccountEvent ae = accountEvents.get(i);
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

//				// Wait until all threads are finished
//				while (!executor.isTerminated()) {				
//				}

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
	 * @param accounts
	 */
	private void setupAccounts(BaseRecordEvent event, Set<Accounts> accounts) {
		LOGGER.info("Entering setupAccounts()");
		LOGGER.debug("BaseRecordEvent: " + event);
		LOGGER.debug("Accounts: " + accounts);

		// Check to make sure the account list is not empty
		if (accounts != null && !accounts.isEmpty()) {
			final Iterator<Accounts> itr = accounts.iterator();
			while (itr.hasNext()) {
				final Accounts account = itr.next();
				if (account != null) {
					// Populate events
					final AccountEvent accountEvent = populateAccountEvent(event, account);

					// Setup the account event
					try {
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
	 * @param account
	 * @return
	 */
	private AccountEvent populateAccountEvent(BaseRecordEvent event, Accounts account) {
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

		LOGGER.info("Exiting populateAccountEvent()");
		return accountEvent;
	}

	/**
	 * 
	 * @param pendingEvent1
	 * @param pendingEvent2
	 * @return
	 */
	private PendingEvent copyPendingEvent(PendingEvent pendingEvent1, PendingEvent pendingEvent2) {
		pendingEvent2.setAccountid(pendingEvent1.getAccountid());
		pendingEvent2.setAccountname(pendingEvent1.getAccountname());
		pendingEvent2.setDatecreated(pendingEvent1.getDatecreated());
		pendingEvent2.setDatemodified(pendingEvent1.getDatemodified());
		pendingEvent2.setEventdate(pendingEvent1.getEventdate());
		pendingEvent2.setEventtype(pendingEvent1.getEventtype());
		pendingEvent2.setGamesport(pendingEvent1.getGamesport());
		pendingEvent2.setGametype(pendingEvent1.getGametype());
		pendingEvent2.setId(pendingEvent1.getId());
		pendingEvent2.setJuice(pendingEvent1.getJuice());
		pendingEvent2.setJuiceplusminus(pendingEvent1.getJuiceplusminus());
		pendingEvent2.setLine(pendingEvent1.getLine());
		pendingEvent2.setLineplusminus(pendingEvent1.getLineplusminus());
		pendingEvent2.setLinetype(pendingEvent1.getLinetype());
		pendingEvent2.setRisk(pendingEvent1.getRisk());
		pendingEvent2.setRotationid(pendingEvent1.getRotationid());
		pendingEvent2.setTeam(pendingEvent1.getTeam());
		pendingEvent2.setTicketnum(pendingEvent1.getTicketnum());
		pendingEvent2.setEventtype(pendingEvent1.getEventtype());
		pendingEvent2.setWin(pendingEvent1.getWin());

		return pendingEvent2;
	}
}