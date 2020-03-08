/**
 * 
 */
package com.ticketadvantage.services.batch;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ticketadvantage.services.dao.AccountDAO;
import com.ticketadvantage.services.dao.AccountEventDAO;
import com.ticketadvantage.services.dao.AccountEventDAOImpl;
import com.ticketadvantage.services.dao.AccountEventFinalDAO;
import com.ticketadvantage.services.dao.AccountEventFinalDAOImpl;
import com.ticketadvantage.services.dao.EventsDAO;
import com.ticketadvantage.services.dao.EventsDAOImpl;
import com.ticketadvantage.services.dao.RecordEventDAO;
import com.ticketadvantage.services.dao.RecordEventDAOImpl;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.AccountEvent;
import com.ticketadvantage.services.service.TransactionEventResource;

/**
 * @author jmiller
 *
 */
@WebListener
@Service
public class RealTimeMoneyBatch implements Runnable, ServletContextListener {
	private static final Logger LOGGER = Logger.getLogger(RealTimeMoneyBatch.class);
	private volatile boolean shutdown = false;
	private EventsDAO eventDAO;
	private AccountEventDAO accountEventDAO;
	private AccountEventFinalDAO accountEventFinalDAO;
	private RecordEventDAO recordEventDAO;
	private final EntityManager entityManager = Persistence.createEntityManagerFactory("entityManager").createEntityManager();

	@Autowired
	private AccountDAO accountDAO;
	
	/**
	 * 
	 */
	public RealTimeMoneyBatch() {
		super();
		LOGGER.info("Entering RetryBatch()");
		LOGGER.info("Exiting RetryBatch()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LOGGER.info("Entering main()");

		try {
			final RealTimeMoneyBatch retryBatch = new RealTimeMoneyBatch();
			// finalScoresBatch.run();
	
			retryBatch.eventDAO = new EventsDAOImpl();
			retryBatch.accountEventDAO = new AccountEventDAOImpl();
			retryBatch.recordEventDAO = new RecordEventDAOImpl();
			retryBatch.eventDAO.setEntityManager(retryBatch.entityManager);
			retryBatch.accountEventDAO.setEntityManager(retryBatch.entityManager);
			retryBatch.recordEventDAO.setEntityManager(retryBatch.entityManager);
			
			retryBatch.entityManager.getTransaction().begin();	
			retryBatch.proccessRetryTransactions();
			retryBatch.entityManager.getTransaction().commit();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		LOGGER.info("Exiting main()");
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
    @Override
    public void contextInitialized(ServletContextEvent event) {
    	LOGGER.error("Entering contextInitialized()");
		new Thread(this).start();
		LOGGER.error("Exiting contextInitialized()");
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextDestroyed(ServletContextEvent event) {
    	LOGGER.error("Entering contextDestroyed()");
    	this.shutdown = true;
    	LOGGER.error("Exiting contextDestroyed()");
    }

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		LOGGER.info("Entering run()");

		eventDAO = new EventsDAOImpl(); 
		accountEventDAO = new AccountEventDAOImpl();
		accountEventFinalDAO = new AccountEventFinalDAOImpl();
		recordEventDAO = new RecordEventDAOImpl();
		eventDAO.setEntityManager(entityManager);
		accountEventDAO.setEntityManager(entityManager);
		accountEventFinalDAO.setEntityManager(entityManager);
		recordEventDAO.setEntityManager(entityManager);

		while (!shutdown) {
			try {
				entityManager.getTransaction().begin();

				// Process retry transaction
				proccessRetryTransactions();

				entityManager.getTransaction().commit();

				// Check every 1 minute
				Thread.sleep(60000);
			} catch (Exception e) {
				LOGGER.error("Exception in thread", e);
			}
		}
	}

	/**
	 * 
	 */
	public void proccessRetryTransactions() {
		LOGGER.info("Entering proccessRetryTransactions()");

		try {
			// Process events
			doProcess();
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		LOGGER.info("Exiting proccessRetryTransactions()");
	}

	/**
	 * 
	 * @throws BatchException
	 */
	private void doProcess() throws BatchException {
		LOGGER.info("Entering doProcess()");

		// List of error'd out account events
		final List<AccountEvent> accountEvents = accountEventDAO.getErrorAccountEventsForWeek();
		LOGGER.debug("accountEvents: " + accountEvents);

		// Go through all account events to see if they are complete
		if (accountEvents != null && accountEvents.size() > 0) {
			try {
				LOGGER.debug("accountEvents.size(): " + accountEvents.size());

				accountDAO.setEntityManager(entityManager);

				final RecordEventDAO recordEventDAO = new RecordEventDAOImpl();
				recordEventDAO.setEntityManager(entityManager);

				// Loop through all the account events
				for (AccountEvent accountEvent : accountEvents) {
					LOGGER.debug("accountEvent.getSport(): " + accountEvent.getSport());
					LOGGER.debug("accountEvent.getEventid(): " + accountEvent.getEventid());

					if (shouldRetry(accountEvent)) {
						final TransactionEventResource transactionEventResource = new TransactionEventResource();
						transactionEventResource.setAccountEvent(accountEvent);

						// Current time
						final Date now = Calendar.getInstance(TimeZone.getTimeZone("America/New_York")).getTime();
						accountEvent.setAttempttime(now);

						// Current attempts
						final Integer currentAttempts = accountEvent.getCurrentattempts();
						if (currentAttempts != null) {
							int attempts = currentAttempts.intValue();
							accountEvent.setCurrentattempts(++attempts);
						} else {
							accountEvent.setCurrentattempts(1);
						}

						LOGGER.debug("AccountEvent: " + accountEvent);
						recordEventDAO.updateAccountEvent(accountEvent);

						// Setup the transaction
						transactionEventResource.setAccountDAO(accountDAO);
						transactionEventResource.setRecordEventDAO(recordEventDAO);
						transactionEventResource.setSendTextOnFailure(false);
						transactionEventResource.setRetry(true);
						transactionEventResource.run();
					}
				}
			} catch (Throwable t) {
				LOGGER.error(t.getMessage(), t);
			}
		}

		LOGGER.info("Exiting doProcess()");
	}

	/**
	 * 
	 * @param accountEvent
	 * @return
	 */
	private boolean shouldRetry(AccountEvent accountEvent) {
		LOGGER.info("Entering shouldRetry()");
		boolean retValue = false;
		Date attemptedTime = accountEvent.getAttempttime();
		Integer attempts = accountEvent.getCurrentattempts();
		LOGGER.debug("attemptedTime: " + attemptedTime);
		LOGGER.debug("attempts: " + attempts);

		if (attempts != null) {
			if (attempts.intValue() < 3) {
				// 
				// Day 1
				//
				if (attempts < 2) {
					retValue = true;
				} else if (attempts == 2) {
					final Calendar now = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
					final Calendar attemptDate = Calendar.getInstance();
					attemptDate.setTime(attemptedTime);
					attemptDate.set(Calendar.HOUR_OF_DAY, (attemptDate.get(Calendar.HOUR_OF_DAY) + 4));
					if (now.getTime().after(attemptDate.getTime())) {
						retValue = true;
					}
				}
			} else if (attempts.intValue() >= 3 && attempts.intValue() < 5) {
				// 
				// Day 2
				//
				if (attempts == 3) {
					final Calendar now = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));					
					final Calendar sixam = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
					sixam.set(Calendar.HOUR_OF_DAY, 6);
					sixam.set(Calendar.MINUTE, 0);
					sixam.set(Calendar.SECOND, 0);
					sixam.set(Calendar.MILLISECOND, 0);

					// Is it after 6 am ET?
					if (now.getTime().after(sixam.getTime())) {
						final Calendar attemptDate = Calendar.getInstance();
						attemptDate.setTime(attemptedTime);
						
						final Calendar yesterday = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
						yesterday.set(Calendar.DAY_OF_MONTH, -1);

						// First check yesterday and then same day in case was done overnight
						if (checkYesterday(yesterday, attemptDate)) {
							retValue = true;
						} else {
							if (checkSameDay(now, attemptDate)) {
								retValue = true;
							}
						}
					}
				} else if (attempts == 4) {
					final Calendar now = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
					final Calendar twelvepm = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
					twelvepm.set(Calendar.HOUR_OF_DAY, 15);
					twelvepm.set(Calendar.MINUTE, 0);
					twelvepm.set(Calendar.SECOND, 0);
					twelvepm.set(Calendar.MILLISECOND, 0);

					// Is it after 3 pm ET?
					if (now.getTime().after(twelvepm.getTime())) {
						final Calendar attemptDate = Calendar.getInstance();
						attemptDate.setTime(attemptedTime);

						// Check if its the same day as last attempted time
						if (checkSameDay(now, attemptDate)) {
							retValue = true;
						}
					}
				}
			} else if (attempts.intValue() >= 5 && attempts.intValue() < 7) {
				// 
				// Day 3
				//
				if (attempts == 5) {
					final Calendar now = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
					final Calendar eightam = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
					eightam.set(Calendar.HOUR_OF_DAY, 8);
					eightam.set(Calendar.MINUTE, 0);
					eightam.set(Calendar.SECOND, 0);
					eightam.set(Calendar.MILLISECOND, 0);

					// Is it after 8 am ET?
					if (now.getTime().after(eightam.getTime())) {
						final Calendar attemptDate = Calendar.getInstance();
						attemptDate.setTime(attemptedTime);

						final Calendar yesterday = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
						yesterday.set(Calendar.DAY_OF_MONTH, -1);

						// First check yesterday and then same day in case was done overnight
						if (checkYesterday(yesterday, attemptDate)) {
							retValue = true;
						}
					}
				} else if (attempts == 6) {
					final Calendar now = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
					final Calendar eightteenpm = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
					eightteenpm.set(Calendar.HOUR_OF_DAY, 18);
					eightteenpm.set(Calendar.MINUTE, 0);
					eightteenpm.set(Calendar.SECOND, 0);
					eightteenpm.set(Calendar.MILLISECOND, 0);

					// Is it after 6 pm ET?
					if (now.getTime().after(eightteenpm.getTime())) {
						final Calendar attemptDate = Calendar.getInstance();
						attemptDate.setTime(attemptedTime);

						// Check if its the same day as last attempted time
						if (checkSameDay(now, attemptDate)) {
							retValue = true;
						}
					}
				}
			} else if (attempts.intValue() >= 7 && attempts.intValue() < 9) {
				// 
				// Day 4
				//
				if (attempts == 7) {
					final Calendar now = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
					final Calendar eightam = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
					eightam.set(Calendar.HOUR_OF_DAY, 8);
					eightam.set(Calendar.MINUTE, 0);
					eightam.set(Calendar.SECOND, 0);
					eightam.set(Calendar.MILLISECOND, 0);

					// Is it after 8 am ET?
					if (now.getTime().after(eightam.getTime())) {
						final Calendar attemptDate = Calendar.getInstance();
						attemptDate.setTime(attemptedTime);

						final Calendar yesterday = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
						yesterday.set(Calendar.DAY_OF_MONTH, -1);

						// First check yesterday and then same day in case was done overnight
						if (checkYesterday(yesterday, attemptDate)) {
							retValue = true;
						}
					}
				} else if (attempts == 8) {
					final Calendar now = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
					final Calendar eightteenpm = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
					eightteenpm.set(Calendar.HOUR_OF_DAY, 18);
					eightteenpm.set(Calendar.MINUTE, 0);
					eightteenpm.set(Calendar.SECOND, 0);
					eightteenpm.set(Calendar.MILLISECOND, 0);

					// Is it after 6 pm ET?
					if (now.getTime().after(eightteenpm.getTime())) {
						final Calendar attemptDate = Calendar.getInstance();
						attemptDate.setTime(attemptedTime);

						// Check if its the same day as last attempted time
						if (checkSameDay(now, attemptDate)) {
							retValue = true;
						}
					}
				}				
			} else if (attempts.intValue() == 9) {
				// 
				// Day 5
				//
				final Calendar now = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
				final Calendar eightam = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
				eightam.set(Calendar.HOUR_OF_DAY, 11);
				eightam.set(Calendar.MINUTE, 30);
				eightam.set(Calendar.SECOND, 0);
				eightam.set(Calendar.MILLISECOND, 0);

				// Is it after 11:30 am ET?
				if (now.getTime().after(eightam.getTime())) {
					final Calendar attemptDate = Calendar.getInstance();
					attemptDate.setTime(attemptedTime);

					final Calendar yesterday = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
					yesterday.set(Calendar.DAY_OF_MONTH, -1);

					// First check yesterday and then same day in case was done
					// overnight
					if (checkYesterday(yesterday, attemptDate)) {
						retValue = true;
					}
				}
			} else if (attempts.intValue() == 10) {
				// 
				// Day 6
				//
				final Calendar now = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
				final Calendar eightam = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
				eightam.set(Calendar.HOUR_OF_DAY, 11);
				eightam.set(Calendar.MINUTE, 30);
				eightam.set(Calendar.SECOND, 0);
				eightam.set(Calendar.MILLISECOND, 0);

				// Is it after 11:30 am ET?
				if (now.getTime().after(eightam.getTime())) {
					final Calendar attemptDate = Calendar.getInstance();
					attemptDate.setTime(attemptedTime);

					final Calendar yesterday = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
					yesterday.set(Calendar.DAY_OF_MONTH, -1);

					// First check yesterday and then same day in case was done
					// overnight
					if (checkYesterday(yesterday, attemptDate)) {
						retValue = true;
					}
				}
			} else {
				retValue = false;
			}
		}

		LOGGER.info("Exiting shouldRetry()");
		return retValue;
	}

	/**
	 * 
	 * @param now
	 * @param attemptDate
	 * @return
	 */
	private boolean checkYesterday(Calendar yesterday, Calendar attemptDate) {
		LOGGER.info("Entering checkYesterday()");
		boolean retValue = false;

		int yesterdayday = yesterday.get(Calendar.DAY_OF_MONTH);
		int yesterdaymonth = yesterday.get(Calendar.MONTH);
		int yesterdayyear = yesterday.get(Calendar.YEAR);
		int attemptday = attemptDate.get(Calendar.DAY_OF_MONTH);
		int attemptmonth = attemptDate.get(Calendar.MONTH);
		int attemptyear = attemptDate.get(Calendar.YEAR);

		if (yesterdaymonth == attemptmonth &&
			yesterdayday == attemptday &&
			yesterdayyear == attemptyear) {
			retValue = true;
		}

		LOGGER.info("Exiting checkYesterday()");
		return retValue;
	}

	/**
	 * 
	 * @param now
	 * @param attemptDate
	 * @return
	 */
	private boolean checkSameDay(Calendar now, Calendar attemptDate) {
		LOGGER.info("Entering checkSameDay()");
		boolean retValue = false;

		int nowday = now.get(Calendar.DAY_OF_MONTH);
		int nowmonth = now.get(Calendar.MONTH);
		int nowyear = now.get(Calendar.YEAR);
		int attemptday = attemptDate.get(Calendar.DAY_OF_MONTH);
		int attemptmonth = attemptDate.get(Calendar.MONTH);
		int attemptyear = attemptDate.get(Calendar.YEAR);

		if (nowmonth == attemptmonth &&
			nowday == attemptday &&
			nowyear == attemptyear) {
			retValue = true;
		}

		LOGGER.info("Exiting checkSameDay()");
		return retValue;
	}
}