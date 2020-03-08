/**
 * 
 */
package com.ticketadvantage.services.service;

import java.util.Iterator;
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
import com.ticketadvantage.services.dao.GroupDAO;
import com.ticketadvantage.services.dao.RecordEventDAO;
import com.ticketadvantage.services.dao.UserDAO;
import com.ticketadvantage.services.dao.sites.SiteWagers;
import com.ticketadvantage.services.errorhandling.AppErrorCodes;
import com.ticketadvantage.services.errorhandling.AppErrorMessage;
import com.ticketadvantage.services.errorhandling.AppException;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.AccountEvent;
import com.ticketadvantage.services.model.Accounts;
import com.ticketadvantage.services.model.BaseRecordEvent;
import com.ticketadvantage.services.model.EventId;
import com.ticketadvantage.services.model.Groups;
import com.ticketadvantage.services.model.MlRecordEvent;
import com.ticketadvantage.services.model.MlTransaction;
import com.ticketadvantage.services.model.SpreadRecordEvent;
import com.ticketadvantage.services.model.SpreadTransaction;
import com.ticketadvantage.services.model.TotalRecordEvent;
import com.ticketadvantage.services.model.TotalTransaction;
import com.ticketadvantage.services.model.User;

/**
 * @author jmiller
 *
 */
@Path("/recordevent")
@Service
public class RecordEventResource {
	private static final Logger LOGGER = Logger.getLogger(RecordEventResource.class);

	@Autowired
	private RecordEventDAO recordEventDAO;

	@Autowired
	private AccountDAO accountDAO;

	@Autowired
	private GroupDAO groupDAO;
	
	@Autowired
	private UserDAO userDAO;

	@PersistenceContext(name = "entityManager")
	private EntityManager entityManager;

	/**
	 * 
	 */
	public RecordEventResource() {
		super();
		LOGGER.debug("Entering RecordEventResource()");
		LOGGER.debug("Exiting RecordEventResource()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * 
	 * @param spreadTransaction
	 * @return
	 * @throws AppException
	 */
	@Path("/spread")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public EventId recordSpreadEvent(SpreadTransaction spreadTransaction) throws AppException {
		LOGGER.info("Entering recordSpreadEvent()");
		LOGGER.debug("SpreadTransaction: " + spreadTransaction);
		EventId eventId = null;

		try {
			final SpreadRecordEvent spreadRecordEvent = spreadTransaction.getSpreadRecordEvent();
			final User user = userDAO.find(spreadRecordEvent.getUserid());
			if (user.getMobilenumber() != null && user.getMobilenumber().length() > 0) {
				spreadRecordEvent.setTextnumber(user.getMobilenumber());
			}

			// Setup the datetime for now
			spreadRecordEvent.setAttempttime(spreadRecordEvent.getDatentime());
			final String event_name = SiteWagers.setupSpreadEventName(spreadRecordEvent);
			spreadRecordEvent.setEventname(event_name);
			
			// Setup the rotation ID
			if (spreadRecordEvent.getSpreadinputfirstone() != null && spreadRecordEvent.getSpreadinputfirstone().length() > 0) {
				spreadRecordEvent.setRotationid(spreadRecordEvent.getEventid1());
			} else {
				spreadRecordEvent.setRotationid(spreadRecordEvent.getEventid2());
			}
			
			// Set the spread event
			final Long id = recordEventDAO.setSpreadEvent(spreadRecordEvent);
			LOGGER.debug("ID: " + id);
			eventId = new EventId();
			eventId.setId(id);
			spreadRecordEvent.setId(id);
			setupAccountEvents(spreadRecordEvent, spreadTransaction.getAccounts(), spreadTransaction.getGroups());
			
			// Check if these need to be run right away
			if (spreadRecordEvent.getAttempts().intValue() == 0) {
				setupImmediateEvents(spreadRecordEvent);
			}
		} catch (AppException ae) {
			LOGGER.error("AppException in recordSpreadEvent()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in recordSpreadEvent()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}

		LOGGER.info("Exiting recordSpreadEvent()");
		return eventId;
	}

	/**
	 * 
	 * @param totalTransaction
	 * @return
	 * @throws AppException
	 */
	@Path("/total")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public EventId recordTotalEvent(TotalTransaction totalTransaction) throws AppException {
		LOGGER.info("Entering recordTotalEvent()");
		LOGGER.debug("TotalTransaction: " + totalTransaction);
		EventId eventId = null;

		try {
			TotalRecordEvent totalRecordEvent = totalTransaction.getTotalRecordEvent();
			final User user = userDAO.find(totalRecordEvent.getUserid());
			if (user.getMobilenumber() != null && user.getMobilenumber().length() > 0) {
				totalRecordEvent.setTextnumber(user.getMobilenumber());
			}

			// Setup the datetime for now
			totalRecordEvent.setAttempttime(totalRecordEvent.getDatentime());
			String event_name = SiteWagers.setupTotalEventName(totalRecordEvent);
			totalRecordEvent.setEventname(event_name);
			
			// Setup the rotation ID
			if (totalRecordEvent.getTotalinputfirstone() != null && totalRecordEvent.getTotalinputfirstone().length() > 0) {
				totalRecordEvent.setRotationid(totalRecordEvent.getEventid1());
			} else {
				totalRecordEvent.setRotationid(totalRecordEvent.getEventid2());
			}

			// Set the total event
			final Long id = recordEventDAO.setTotalEvent(totalRecordEvent);
			LOGGER.debug("ID: " + id);
			eventId = new EventId();
			eventId.setId(id);

			totalRecordEvent.setId(id);
			setupAccountEvents(totalRecordEvent, totalTransaction.getAccounts(), totalTransaction.getGroups());

			// Check if these need to be run right away
			if (totalRecordEvent.getAttempts().intValue() == 0) {
				setupImmediateEvents(totalRecordEvent);
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
		return eventId;
	}

	/**
	 * 
	 * @param mlRecordEvent
	 * @return
	 * @throws AppException
	 */
	@Path("/ml")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public EventId recordMlEvent(MlTransaction mlTransaction) throws AppException {
		LOGGER.info("Entering recordMlEvent()");
		LOGGER.debug("MlTransaction: " + mlTransaction);
		EventId eventId = null;

		try {
			MlRecordEvent mlRecordEvent = mlTransaction.getMlRecordEvent();
			final User user = userDAO.find(mlRecordEvent.getUserid());
			if (user.getMobilenumber() != null && user.getMobilenumber().length() > 0) {
				mlRecordEvent.setTextnumber(user.getMobilenumber());				
			}

			// Setup the datetime for now
			mlRecordEvent.setAttempttime(mlRecordEvent.getDatentime());
			String event_name = SiteWagers.setupMlEventName(mlRecordEvent);
			mlRecordEvent.setEventname(event_name);

			// Setup the rotation ID
			if (mlRecordEvent.getMlinputfirstone() != null && mlRecordEvent.getMlinputfirstone().length() > 0) {
				mlRecordEvent.setRotationid(mlRecordEvent.getEventid1());
			} else {
				mlRecordEvent.setRotationid(mlRecordEvent.getEventid2());
			}

			// Set the ml event
			final Long id = recordEventDAO.setMlEvent(mlRecordEvent);
			LOGGER.debug("ID: " + id);
			eventId = new EventId();
			eventId.setId(id);

			mlRecordEvent.setId(id);
			setupAccountEvents(mlRecordEvent, mlTransaction.getAccounts(), mlTransaction.getGroups());

			// Check if these need to be run right away
			if (mlRecordEvent.getAttempts().intValue() == 0) {
				setupImmediateEvents(mlRecordEvent);
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
		return eventId;
	}

	/**
	 * 
	 * @param event
	 */
	private void setupAccountEvents(BaseRecordEvent event, Set<Accounts> accounts, Set<Groups> groups) {
		LOGGER.info("Entering setupAccountEvents()");
		LOGGER.debug("Event: " + event);

		if (accounts != null && accounts.size() > 0) {
			setupAccounts(event, accounts);
		} else if (groups != null && groups.size() > 0) {
			for (Groups grp : groups) {
				final Groups group = groupDAO.getGroup(grp.getId());
				setupAccountsForGroup(event, group);
			}
		} else if (event.getAccountid() != null && event.getAccountid().longValue() == 0) {
			final Set<Accounts> accts = accountDAO.getAccountsForUser(event.getUserid());
			setupAccounts(event, accts);
		} else if (event.getGroupid() != null && event.getGroupid().longValue() == 0) {
			final Set<Groups> grps = groupDAO.getGroupsForUser(event.getUserid());
			// Check to make sure the group list is not empty
			if (grps != null && !grps.isEmpty()) {
				final Iterator<Groups> itr = grps.iterator();
				while (itr.hasNext()) {
					final Groups group = itr.next();
					setupAccountsForGroup(event, group);
				}
			}
		}

		LOGGER.info("Exiting setupAccountEvents()");
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
	 * @param group
	 */
	private void setupAccountsForGroup(BaseRecordEvent event, Groups group) {
		LOGGER.info("Entering setupAccountsForGroup()");
		LOGGER.debug("BaseRecordEvent: " + event);
		LOGGER.debug("Group: " + group);

		if (group != null) {
			final Set<Accounts> accounts = group.getAccounts();
			setupAccounts(event, accounts);
		}
		LOGGER.info("Exiting setupAccountsForGroup()");
	}

	/**
	 * 
	 * @param event
	 * @param accounts
	 */
	private void setupAccounts(BaseRecordEvent event, Set<Accounts> accounts) {
		LOGGER.info("Entering setupAccountsForGroup()");
		LOGGER.debug("BaseRecordEvent: " + event);
		LOGGER.debug("Accounts: " + accounts);

		// Check to make sure the account list is not empty
		if (accounts != null && !accounts.isEmpty()) {
			final Iterator<Accounts> itr = accounts.iterator();
			while (itr.hasNext()) {
				final Accounts account = itr.next();
				if (account != null) {
					final Accounts acct = accountDAO.getAccount(account.getId());

					// Populate events
					final AccountEvent accountEvent = populateAccountEvent(event, acct);

					// Setup the account event
					recordEventDAO.setupAccountEvent(accountEvent);
				}
			}
		}
		LOGGER.info("Exiting setupAccountsForGroup()");
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
		accountEvent.setIscomplexcaptcha(account.getIscomplexcaptcha());

		LOGGER.info("Exiting populateAccountEvent()");
		return accountEvent;
	}
}