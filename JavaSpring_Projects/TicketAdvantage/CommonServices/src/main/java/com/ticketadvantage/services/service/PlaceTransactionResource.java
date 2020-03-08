/**
 * 
 */
package com.ticketadvantage.services.service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ticketadvantage.services.GlobalProperties;
import com.ticketadvantage.services.dao.AccountDAO;
import com.ticketadvantage.services.dao.EventsDAO;
import com.ticketadvantage.services.dao.GroupDAO;
import com.ticketadvantage.services.dao.RecordEventDAO;
import com.ticketadvantage.services.dao.UserDAO;
import com.ticketadvantage.services.errorhandling.AppErrorCodes;
import com.ticketadvantage.services.errorhandling.AppErrorMessage;
import com.ticketadvantage.services.errorhandling.AppException;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.AccountEvent;
import com.ticketadvantage.services.model.Accounts;
import com.ticketadvantage.services.model.BaseRecordEvent;
import com.ticketadvantage.services.model.EventData;
import com.ticketadvantage.services.model.EventId;
import com.ticketadvantage.services.model.EventPackage;
import com.ticketadvantage.services.model.Groups;
import com.ticketadvantage.services.model.MlRecordEvent;
import com.ticketadvantage.services.model.PlaceTransaction;
import com.ticketadvantage.services.model.SpreadRecordEvent;
import com.ticketadvantage.services.model.TotalRecordEvent;
import com.ticketadvantage.services.model.User;

/**
 * @author jmiller
 *
 */
@Path("/placetransaction")
@Service
public class PlaceTransactionResource {
	private static final Logger LOGGER = Logger.getLogger(PlaceTransactionResource.class);

	@Autowired
	private RecordEventDAO recordEventDAO;

	@Autowired
	private AccountDAO accountDAO;

	@Autowired
	private GroupDAO groupDAO;

	@Autowired
	private EventsDAO eventsDAO;

	@Autowired
	private UserDAO userDAO;

	@PersistenceContext(name = "entityManager")
	private EntityManager entityManager;

	/**
	 * 
	 */
	public PlaceTransactionResource() {
		super();
		LOGGER.debug("Entering PlaceTransactionResource()");
		LOGGER.debug("Exiting PlaceTransactionResource()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * 
	 * @param placeTransaction
	 * @return
	 * @throws AppException
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public EventId placeTransaction(PlaceTransaction placeTransaction) throws AppException {
		LOGGER.info("Entering placeTransaction()");
		LOGGER.debug("PlaceTransaction: " + placeTransaction);
		EventId eventId = null;

		try {
			// First, get the event data
			final EventPackage ep = eventsDAO.getEvent(placeTransaction.getRotationid());
			if (ep == null) {
				LOGGER.error("Rotaion ID not found");
				throw new AppException(500, AppErrorCodes.EVENTS_EXCEPTION,  
						AppErrorMessage.EVENTS_EXCEPTION);
			}

			// Spread
			if ("spread".equals(placeTransaction.getLinetype())) {
				eventId = setupSpreadEvent(placeTransaction, ep);
			// Total
			} else if ("total".equals(placeTransaction.getLinetype())) {
				eventId = setupTotalEvent(placeTransaction, ep);
			// Money Line
			} else if ("ml".equals(placeTransaction.getLinetype())) {
				eventId = setupMlEvent(placeTransaction, ep);
			}
		} catch (AppException ae) {
			LOGGER.error("AppException in recordSpreadEvent()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in recordSpreadEvent()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}

		LOGGER.info("Exiting placeTransaction()");
		return eventId;
	}

	/**
	 * 
	 * @param placeTransaction
	 * @param ep
	 * @return
	 * @throws AppException
	 */
	public EventId setupSpreadEvent(PlaceTransaction placeTransaction, EventPackage ep) throws AppException {
		LOGGER.info("Entering setupSpreadEvent()");
		LOGGER.debug("PlaceTransaction: " + placeTransaction);
		EventId eventId = null;

		try {
			final Set<EventData> eventDatas = placeTransaction.getEventdatas();
			final SpreadRecordEvent spreadRecordEvent = new SpreadRecordEvent();
			setupBaseEvent(placeTransaction, spreadRecordEvent, ep);

			// Rotation ID
			final Long rotationId = setupRotationid(placeTransaction.getSport(), placeTransaction.getRotationid());
			if (rotationId % 2 == 0) {
				spreadRecordEvent.setSpreadplusminussecondone(placeTransaction.getLineindicator());
				spreadRecordEvent.setSpreadinputsecondone(placeTransaction.getLine());
				spreadRecordEvent.setSpreadjuiceplusminussecondone(placeTransaction.getJuiceindicator());
				spreadRecordEvent.setSpreadinputjuicesecondone(placeTransaction.getJuice());
			} else {
				spreadRecordEvent.setSpreadplusminusfirstone(placeTransaction.getLineindicator());
				spreadRecordEvent.setSpreadinputfirstone(placeTransaction.getLine());
				spreadRecordEvent.setSpreadjuiceplusminusfirstone(placeTransaction.getJuiceindicator());
				spreadRecordEvent.setSpreadinputjuicefirstone(placeTransaction.getJuice());
			}

			final User user = userDAO.find(spreadRecordEvent.getUserid());
			if (user.getMobilenumber() != null && user.getMobilenumber().length() > 0) {
				spreadRecordEvent.setTextnumber(user.getMobilenumber());
			}

			// Set the spread event
			final Long id = recordEventDAO.setSpreadEvent(spreadRecordEvent);
			LOGGER.debug("ID: " + id);
			eventId = new EventId();
			eventId.setId(id);
			spreadRecordEvent.setId(id);

			// Setup the account events
			setupAccountEvents(placeTransaction.getBuyorder(), rotationId, spreadRecordEvent, eventDatas);

			// Check if these need to be run right away
			if (spreadRecordEvent.getAttempts().intValue() == 0) {
				if (placeTransaction.getBuyorder()) {
					setupBuyOrder(spreadRecordEvent, Double.parseDouble(placeTransaction.getAmount()), Double.parseDouble(placeTransaction.getBuyamount()));					
				} else {
					setupImmediateEvents(spreadRecordEvent);
				}
			}
		} catch (AppException ae) {
			LOGGER.error("AppException in recordSpreadEvent()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in recordSpreadEvent()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}

		LOGGER.info("Exiting placeTransaction()");
		return eventId;
	}

	/**
	 * 
	 * @param placeTransaction
	 * @param ep
	 * @return
	 * @throws AppException
	 */
	public EventId setupTotalEvent(PlaceTransaction placeTransaction, EventPackage ep) throws AppException {
		LOGGER.info("Entering setupTotalEvent()");
		LOGGER.debug("PlaceTransaction: " + placeTransaction);
		EventId eventId = null;

		try {
			final Set<EventData> eventDatas = placeTransaction.getEventdatas();
			final TotalRecordEvent totalRecordEvent = new TotalRecordEvent();
			setupBaseEvent(placeTransaction, totalRecordEvent, ep);

			final String overUnder = placeTransaction.getLineindicator();
			if ("o".equals(overUnder)) {
				totalRecordEvent.setTotalinputfirstone(placeTransaction.getLine());
				totalRecordEvent.setTotaljuiceplusminusfirstone(placeTransaction.getJuiceindicator());
				totalRecordEvent.setTotalinputjuicefirstone(placeTransaction.getJuice());
			} else {
				totalRecordEvent.setTotalinputsecondone(placeTransaction.getLine());
				totalRecordEvent.setTotaljuiceplusminussecondone(placeTransaction.getJuiceindicator());
				totalRecordEvent.setTotalinputjuicesecondone(placeTransaction.getJuice());
			}

			final User user = userDAO.find(totalRecordEvent.getUserid());
			if (user.getMobilenumber() != null && user.getMobilenumber().length() > 0) {
				totalRecordEvent.setTextnumber(user.getMobilenumber());
			}

			// Set the total event
			final Long id = recordEventDAO.setTotalEvent(totalRecordEvent);
			LOGGER.debug("ID: " + id);
			eventId = new EventId();
			eventId.setId(id);
			totalRecordEvent.setId(id);
			
			// Setup the account events
			final Long rotationId = setupRotationid(placeTransaction.getSport(), placeTransaction.getRotationid());
			setupAccountEvents(placeTransaction.getBuyorder(), rotationId, totalRecordEvent, eventDatas);

			// Check if these need to be run right away
			if (totalRecordEvent.getAttempts().intValue() == 0) {
				if (placeTransaction.getBuyorder()) {
					setupBuyOrder(totalRecordEvent, Double.parseDouble(placeTransaction.getAmount()), Double.parseDouble(placeTransaction.getBuyamount()));					
				} else {
					setupImmediateEvents(totalRecordEvent);
				}
			}
		} catch (AppException ae) {
			LOGGER.error("AppException in setupTotalEvent()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in setupTotalEvent()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}

		LOGGER.info("Exiting setupTotalEvent()");
		return eventId;
	}

	/**
	 * 
	 * @param placeTransaction
	 * @param ep
	 * @return
	 * @throws AppException
	 */
	public EventId setupMlEvent(PlaceTransaction placeTransaction, EventPackage ep) throws AppException {
		LOGGER.info("Entering setupMlEvent()");
		LOGGER.debug("PlaceTransaction: " + placeTransaction);
		EventId eventId = null;

		try {
			final Set<EventData> eventDatas = placeTransaction.getEventdatas();
			final MlRecordEvent mlRecordEvent = new MlRecordEvent();
			setupBaseEvent(placeTransaction, mlRecordEvent, ep);
			
			// Rotation ID
			final Long rotationId = setupRotationid(placeTransaction.getSport(), placeTransaction.getRotationid());
			if (rotationId % 2 == 0) {
				mlRecordEvent.setMlplusminussecondone(placeTransaction.getJuiceindicator());
				mlRecordEvent.setMlinputsecondone(placeTransaction.getJuice());
			} else {
				mlRecordEvent.setMlplusminusfirstone(placeTransaction.getJuiceindicator());
				mlRecordEvent.setMlinputfirstone(placeTransaction.getJuice());
			}

			final User user = userDAO.find(mlRecordEvent.getUserid());
			if (user.getMobilenumber() != null && user.getMobilenumber().length() > 0) {
				mlRecordEvent.setTextnumber(user.getMobilenumber());				
			}

			// Set the ml event
			final Long id = recordEventDAO.setMlEvent(mlRecordEvent);
			LOGGER.debug("ID: " + id);
			eventId = new EventId();
			eventId.setId(id);
			mlRecordEvent.setId(id);

			// Setup the account events
			setupAccountEvents(placeTransaction.getBuyorder(), rotationId, mlRecordEvent, eventDatas);

			// Check if these need to be run right away
			if (mlRecordEvent.getAttempts().intValue() == 0) {
				if (placeTransaction.getBuyorder()) {
					setupBuyOrder(mlRecordEvent, Double.parseDouble(placeTransaction.getAmount()), Double.parseDouble(placeTransaction.getBuyamount()));					
				} else {
					setupImmediateEvents(mlRecordEvent);
				}
			}
		} catch (AppException ae) {
			LOGGER.error("AppException in setupMlEvent()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in setupMlEvent()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}

		LOGGER.info("Exiting setupMlEvent()");
		return eventId;
	}

	/**
	 * 
	 * @param placeTransaction
	 * @param baseRecordEvent
	 * @param ep
	 * @throws AppException
	 */
	public void setupBaseEvent(PlaceTransaction placeTransaction, BaseRecordEvent baseRecordEvent, EventPackage ep) throws AppException {
		LOGGER.info("Entering setupBaseEvent()");

		// Setup the data
		baseRecordEvent.setAccountid(new Long(-99));
		baseRecordEvent.setAmount(placeTransaction.getAmount());
		baseRecordEvent.setAttempts(0);
		baseRecordEvent.setDatecreated(new Date());
		baseRecordEvent.setDatemodified(new Date());
		baseRecordEvent.setDatentime(new Date());
		baseRecordEvent.setEventdatetime(ep.getEventdatetime());

		// Rotation ID
		final Long rotationId = setupRotationid(placeTransaction.getSport(), placeTransaction.getRotationid());
		if (rotationId % 2 == 0) {
			baseRecordEvent.setEventid2(rotationId.intValue());
			baseRecordEvent.setEventid1(rotationId.intValue() - 1);
			baseRecordEvent.setEventid(rotationId.intValue() - 1);
			baseRecordEvent.setEventname(getGameType(placeTransaction.getSport()) + " #" + 
					rotationId + " " + ep.getTeamtwo().getTeam() + " " + 
					placeTransaction.getLineindicator() + placeTransaction.getLine() + " " + 
					placeTransaction.getJuiceindicator() + placeTransaction.getJuice() + " for " + 
					getBetType(placeTransaction.getSport()));
		} else {
			baseRecordEvent.setEventid2(rotationId.intValue() + 1);
			baseRecordEvent.setEventid1(rotationId.intValue());
			baseRecordEvent.setEventid(rotationId.intValue());
			baseRecordEvent.setEventname(getGameType(placeTransaction.getSport()) + " #" + 
					rotationId + " " + ep.getTeamone().getTeam() + " " + 
					placeTransaction.getLineindicator() + placeTransaction.getLine() + " " + 
					placeTransaction.getJuiceindicator() + placeTransaction.getJuice() + " for " + 
					getBetType(placeTransaction.getSport()));
		}
		LOGGER.debug("EventName: " + baseRecordEvent.getEventname());
		baseRecordEvent.setEventtype(placeTransaction.getLinetype());
		baseRecordEvent.setEventteam1(ep.getTeamone().getTeam());
		baseRecordEvent.setEventteam2(ep.getTeamtwo().getTeam());
		baseRecordEvent.setGroupid(new Long(-99));
		baseRecordEvent.setRotationid(rotationId.intValue());
		baseRecordEvent.setScrappername("UI");
		baseRecordEvent.setSport(placeTransaction.getSport());
		baseRecordEvent.setUserid(placeTransaction.getUserid());
		baseRecordEvent.setWtype(placeTransaction.getRiskwin());

		LOGGER.info("Exiting setupBaseEvent()");
	}

	/**
	 * 
	 * @param buyOrder
	 * @param event
	 * @param eventDatas
	 */
	private void setupAccountEvents(Boolean buyOrder, Long rotationId, BaseRecordEvent event, Set<EventData> eventDatas) {
		LOGGER.info("Entering setupAccountEvents()");
		LOGGER.debug("Event: " + event);

		// Check for groups first
		final Set<Accounts> accounts = setupAccountsForGroup(eventDatas);
		if (accounts != null && !accounts.isEmpty()) {
			int count = 0;
			for (EventData eventData : eventDatas) {
				int acount = 0;
				for (Accounts account : accounts) {
					if (count == acount++) {
						// Populate events
						final AccountEvent accountEvent = populateAccountEvent(buyOrder, rotationId, event, eventData, account);

						// Setup the account event
						recordEventDAO.setupAccountEvent(accountEvent);
					}
				}
				count++;
			}
		} else {
			for (EventData eventData : eventDatas) {
				// Get account
				final Long accountid = eventData.getAccountid();
				final Accounts acct = accountDAO.getAccount(accountid.longValue());

				// Populate events
				final AccountEvent accountEvent = populateAccountEvent(buyOrder, rotationId, event, eventData, acct);

				// Setup the account event
				recordEventDAO.setupAccountEvent(accountEvent);
			}			
		}

		LOGGER.info("Exiting setupAccountEvents()");
	}

	/**
	 * 
	 * @param eventDatas
	 * @return
	 */
	private Set<Accounts> setupAccountsForGroup(Set<EventData> eventDatas) {
		LOGGER.info("Entering setupAccountsForGroup()");
		Set<Accounts> accounts = null;

		if (eventDatas != null && !eventDatas.isEmpty()) {
			final EventData ed = eventDatas.iterator().next();
			final Long groupid = ed.getGroupid();
			if (groupid != null && groupid.longValue() != -99 && groupid.longValue() != 0) {
				final Groups group = groupDAO.getGroup(groupid.longValue());
				accounts = group.getAccounts();
			}
		}

		LOGGER.info("Exiting setupAccountsForGroup()");
		return accounts;
	}

	/**
	 * 
	 * @param buyOrder
	 * @param rotationid
	 * @param event
	 * @param eventData
	 * @param account
	 * @return
	 */
	private AccountEvent populateAccountEvent(Boolean buyOrder, Long rotationid, BaseRecordEvent event, EventData eventData, Accounts account) {
		LOGGER.info("Entering populateAccountEvent()");

		final AccountEvent accountEvent = new AccountEvent();
		if ("spread".equals(event.getEventtype())) {
			accountEvent.setSpreadid(event.getId());
			accountEvent.setMaxspreadamount(account.getSpreadlimitamount());
			accountEvent.setSpreadindicator(eventData.getLineindicator());
			if ("-".equals(eventData.getLineindicator())) {
				accountEvent.setSpread(Float.parseFloat("-" + Float.parseFloat(eventData.getLine())));
			} else {
				accountEvent.setSpread(Float.parseFloat(eventData.getLine()));
			}
			accountEvent.setSpreadjuice(Float.parseFloat(eventData.getJuiceindicator() + eventData.getJuice()));
		} else if ("total".equals(event.getEventtype())) {
			accountEvent.setTotalid(event.getId());
			accountEvent.setMaxtotalamount(account.getTotallimitamount());
			accountEvent.setTotalindicator(eventData.getLineindicator());
			accountEvent.setTotal(Float.parseFloat(eventData.getLine()));
			accountEvent.setTotaljuice(Float.parseFloat(eventData.getJuiceindicator() + eventData.getJuice()));
		} else if ("ml".equals(event.getEventtype())) {
			accountEvent.setMlid(event.getId());
			accountEvent.setMaxmlamount(account.getMllimitamount());
			accountEvent.setMlindicator(eventData.getLineindicator());
			if ("-".equals(eventData.getLineindicator())) {
				accountEvent.setMl(Float.parseFloat("-" + Float.parseFloat(eventData.getLine())));
			} else {
				accountEvent.setMl(Float.parseFloat(eventData.getLine()));
			}

			if (eventData.getJuice() == null || eventData.getJuice().length() == 0) {
				accountEvent.setMljuice(Float.parseFloat(eventData.getLineindicator() + eventData.getLine()));
			} else {
				accountEvent.setMljuice(Float.parseFloat(eventData.getJuiceindicator() + eventData.getJuice()));
			}
		}
		accountEvent.setAccountid(account.getId());
		accountEvent.setAttempts(event.getAttempts());
		if (!buyOrder) {
			accountEvent.setAmount(eventData.getAmount());
		}
		accountEvent.setGroupid(event.getGroupid());
		accountEvent.setEventdatetime(event.getEventdatetime());
		accountEvent.setEventid(rotationid.intValue());
		accountEvent.setEventname(event.getEventname());
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
	 * @param event
	 */
	private void setupImmediateEvents(BaseRecordEvent event) {
		LOGGER.info("Entering setupImmediateEvents()");
		LOGGER.debug("Event: " + event);
		
		// First check to see if it's an account or group
		try {
			List<AccountEvent> accountEvents = null;
			if ("spread".equals(event.getEventtype())) {
				accountEvents = recordEventDAO.getSpreadActiveAccountEvents(event.getId());
			} else if ("total".equals(event.getEventtype())) {
				accountEvents = recordEventDAO.getTotalActiveAccountEvents(event.getId());
			} else if ("ml".equals(event.getEventtype())) {
				accountEvents = recordEventDAO.getMlActiveAccountEvents(event.getId());
			}

			// Make sure there is at least one
			if (accountEvents != null && accountEvents.size() > 0) {
				final ExecutorService executor = Executors.newFixedThreadPool(accountEvents.size());
				for (int i = 0; i < accountEvents.size(); i++) {
					final AccountEvent ae = accountEvents.get(i);
					if (GlobalProperties.isLocal() || ae.getIscomplexcaptcha()) {
						final TransactionEventResource transactionEventResource = new TransactionEventResource();

						transactionEventResource.setAccountEvent(ae);
						transactionEventResource.setAccountDAO(accountDAO);
						transactionEventResource.setRecordEventDAO(recordEventDAO);
						final Runnable worker = transactionEventResource;
						executor.execute(worker);
					} else {
						final AWSTransactionEventResource awsTransactionEventResource = new AWSTransactionEventResource();

						awsTransactionEventResource.setAccountEventId(ae.getId());
						final Runnable worker = awsTransactionEventResource;
						executor.execute(worker);
					}
				}
				executor.shutdown();

				// Wait until all threads are finished
				while (!executor.isTerminated()) {
				}
			}
		} catch (BatchException be) {
			LOGGER.error("Cannot get account event for " + event.getId(), be);
		}

		LOGGER.info("Exiting setupImmediateEvents()");
	}

	/**
	 * 
	 * @param event
	 * @param orderAmount
	 * @param maxAmount
	 */
	private void setupBuyOrder(BaseRecordEvent event, Double orderAmount, Double maxAmount) {
		LOGGER.info("Entering setupBuyOrder()");
		LOGGER.debug("Event: " + event);
		
		// First check to see if it's an account or group
		try {
			List<AccountEvent> accountEvents = null;
			if ("spread".equals(event.getEventtype())) {
				accountEvents = recordEventDAO.getSpreadActiveAccountEvents(event.getId());
			} else if ("total".equals(event.getEventtype())) {
				accountEvents = recordEventDAO.getTotalActiveAccountEvents(event.getId());
			} else if ("ml".equals(event.getEventtype())) {
				accountEvents = recordEventDAO.getMlActiveAccountEvents(event.getId());
			}

			// Make sure there is at least one
			if (accountEvents != null && accountEvents.size() > 0) {
				final BuyOrderResource buyOrderResource = new BuyOrderResource(accountEvents, orderAmount, maxAmount);
			}
		} catch (BatchException be) {
			LOGGER.error("Cannot get account event for " + event.getId(), be);
		}

		LOGGER.info("Exiting setupBuyOrder()");
	}

	/**
	 * 
	 * @param sportType
	 * @return
	 */
	private static String getGameType(String sportType) {
		String gType = "";

		if ("nfllines".equals(sportType) || "nflfirst".equals(sportType) ||
				"nflsecond".equals(sportType)) {
			gType = "NFL";
		} else if ("ncaaflines".equals(sportType) || "ncaaffirst".equals(sportType) ||
				"ncaafsecond".equals(sportType)) {
			gType = "NCAAF";
		} else if ("nbalines".equals(sportType) || "nbafirst".equals(sportType) ||
				"nbasecond".equals(sportType)) {
			gType = "NBA";
		} else if ("ncaablines".equals(sportType) || "ncaabfirst".equals(sportType) ||
				"ncaabsecond".equals(sportType)) {
			gType = "NCAAB";
		} else if ("nhllines".equals(sportType) || "nchlfirst".equals(sportType) ||
				"nhlsecond".equals(sportType)) {
			gType = "NHL";
		} else if ("wnbalines".equals(sportType) || "wnbafirst".equals(sportType) ||
				"wnbasecond".equals(sportType)) {
			gType = "WNBA";
		}

		return gType;
	}

	/**
	 * 
	 * @param sportType
	 * @return
	 */
	private static String getBetType(String sportType) {
		String bType = "";

		if (sportType != null) {
			if (sportType.contains("lines")) {
				bType = "Game";
			} else if (sportType.contains("first")) {
				bType = "1H";
			} else if (sportType.contains("second")) {
				bType = "2H";
			} else if (sportType.contains("third")) {
				bType = "3H";
			}
		}

		return bType;
	}

	/**
	 * 
	 * @param sportType
	 * @param rotationid
	 * @return
	 */
	private static Long setupRotationid(String sportType, Long rotationid) {
		Long rotid = new Long(0);

		if (rotationid != null) {
			if (sportType.contains("lines")) {
				rotid = rotationid;
			} else if (sportType.contains("first")) {
				rotid = 1000 + rotationid;
			} else if (sportType.contains("second")) {
				rotid = 2000 + rotationid;
			} else if (sportType.contains("third")) {
				rotid = 3000 + rotationid;
			}
		}

		return rotid;
	}
}