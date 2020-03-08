/**
 * 
 */
package com.ticketadvantage.services.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Path;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ticketadvantage.services.dao.AccountDAO;
import com.ticketadvantage.services.dao.RecordEventDAO;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.AccountEvent;
import com.ticketadvantage.services.model.BaseRecordEvent;
import com.ticketadvantage.services.model.MlRecordEvent;
import com.ticketadvantage.services.model.SpreadRecordEvent;
import com.ticketadvantage.services.model.TotalRecordEvent;

/**
 * @author jmiller
 *
 */
@Path("/process")
@Service
public class ProcessEventsResource {
	private static final Logger LOGGER = Logger.getLogger(ProcessEventsResource.class);

	@Autowired
	private RecordEventDAO recordEventDAO;

	@Autowired
	private AccountDAO accountDAO;

	@PersistenceContext(name = "entityManager")
	private EntityManager entityManager;

	/**
	 * 
	 */
	public ProcessEventsResource() {
		super();
		LOGGER.debug("Entering ProcessEventsResource()");
		LOGGER.debug("Exiting ProcessEventsResource()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

/*
	@GET
	@Path("/startprocess")
	public void startProccessEvents() {
		LOGGER.info("Entering startProccessEvents()");
//		new Thread(new ProcessEventsBatch()).start();
		LOGGER.info("Ending startProccessEvents()");
	}

	@GET
	@Path("/processevents")
	public void proccessEvents() {
		LOGGER.info("Entering proccessEvents()");
		List<SpreadRecordEvent> spreadEvents = null;
		List<TotalRecordEvent> totalEvents = null;
		List<MlRecordEvent> mlEvents = null;

		try {
			// Spread events
			spreadEvents = recordEventDAO.getUnProcessedSpreadEvents();
			if (spreadEvents != null && spreadEvents.size() > 0) {
				for (int x = 0; x < spreadEvents.size(); x++) {
					final BaseRecordEvent event = spreadEvents.get(x);

					// Process the event
					doProcess(event);
				}
			}
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in proccessEvents() for SpreadEvent: " + spreadEvents, t);
		}
		try {
			// Total events
			totalEvents = recordEventDAO.getUnProcessedTotalEvents();
			if (totalEvents != null && totalEvents.size() > 0) {
				for (int x = 0; x < totalEvents.size(); x++) {
					final BaseRecordEvent event = totalEvents.get(x);

					// Process the event
					doProcess(event);
				}
			}
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in proccessEvents() for TotalEvent: " + totalEvents, t);
		}
		try {
			// ML events
			mlEvents = recordEventDAO.getUnProcessedMlEvents();
			if (mlEvents != null && mlEvents.size() > 0) {
				for (int x = 0; x < mlEvents.size(); x++) {
					final BaseRecordEvent event = mlEvents.get(x);

					// Process the event
					doProcess(event);
				}
			}
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in proccessEvents() for MlEvents: " + mlEvents, t);
		}
		LOGGER.info("Exiting proccessEvents()");
	}
*/
	
	/**
	 * 
	 * @param event
	 * @throws BatchException
	 */
	private void doProcess(BaseRecordEvent event) throws BatchException {
		LOGGER.info("Entering doProcess()");
		LOGGER.debug("RecordEvent: " + event);
		final int attempts = event.getAttempts().intValue();
		final Date attemptDate = event.getAttempttime();

		// First check to see if we should even try
		if (attempts > 0 && attemptDate.before(new Date())) {
			// This means we should batch this; Should be 1 to 5 times a day
			List<AccountEvent> accountEvents = getAccountEvents(event);
			if (accountEvents != null && accountEvents.size() > 0) {
				final ExecutorService executor = Executors.newFixedThreadPool(accountEvents.size());
				for (int x = 0; x < accountEvents.size(); x++) {
					// Now check for date
					final AccountEvent accountEvent = accountEvents.get(x);
					final TransactionEventResource transactionEventResource = new TransactionEventResource();
					transactionEventResource.setAccountEvent(accountEvent);
					transactionEventResource.setAccountDAO(accountDAO);
					transactionEventResource.setRecordEventDAO(recordEventDAO);

					final Runnable worker = transactionEventResource;
					executor.execute(worker);
				}
				executor.shutdown();

				// Wait until all threads are finished
				while (!executor.isTerminated()) {
				}
			}

			// Get the new date for it to run
			final Date newDate = determineNextAttempt(attempts);
			event.setAttempttime(newDate);
		}

		LOGGER.info("Exiting doProcess()");
	}

	/**
	 * 
	 * @param event
	 * @return
	 * @throws BatchException
	 */
	private List<AccountEvent> getAccountEvents(BaseRecordEvent event) throws BatchException {
		LOGGER.info("Exiting getAccountEvents()");
		List<AccountEvent> accountEvents = null;

		if (event instanceof SpreadRecordEvent) {
			accountEvents = recordEventDAO.getSpreadActiveAccountEvents(event.getId());
		} else if (event instanceof TotalRecordEvent) {
			accountEvents = recordEventDAO.getTotalActiveAccountEvents(event.getId());
		} else if (event instanceof MlRecordEvent) {
			accountEvents = recordEventDAO.getMlActiveAccountEvents(event.getId());
		}

		LOGGER.debug("AccountEvents: " + accountEvents);
		LOGGER.info("Exiting getAccountEvents()");
		return accountEvents;
	}

	/**
	 * 
	 * @param attempts
	 * @return
	 */
	private Date determineNextAttempt(int attempts) {
		LOGGER.info("Entering determineNextAttempt()");
		Date retValue = null;
		Calendar cal = Calendar.getInstance(); // creates calendar
		cal.setTime(new Date()); // sets calendar time/date
		switch (attempts) {
			case 1:
				cal.add(Calendar.HOUR_OF_DAY, 24); // 24 hours
				retValue = cal.getTime();
				break;
			case 2:
				cal.add(Calendar.HOUR_OF_DAY, 12); // 12 hours
				retValue = cal.getTime();
				break;
			case 3:
				cal.add(Calendar.HOUR_OF_DAY, 8); // 8 hours
				retValue = cal.getTime();
				break;
			case 4:
				cal.add(Calendar.HOUR_OF_DAY, 6); // 6 hours
				retValue = cal.getTime();
				break;
			case 5:
				cal.add(Calendar.HOUR_OF_DAY, 5); // 5 hours
				retValue = cal.getTime();
				break;
		}
		LOGGER.debug("NewDate: " + retValue);
		LOGGER.info("Exiting determineNextAttempt()");
		return retValue;
	}
}