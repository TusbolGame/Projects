/**
 * 
 */
package com.ticketadvantage.services.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.ticketadvantage.services.dao.sites.SiteProcessor;
import com.ticketadvantage.services.dao.sites.sportsinsights.SportsInsightsSite;
import com.ticketadvantage.services.db.SiteActiveDB;
import com.ticketadvantage.services.db.TicketAdvantageDB;
import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.AccountEvent;
import com.ticketadvantage.services.model.Accounts;
import com.ticketadvantage.services.model.BaseRecordEvent;
import com.ticketadvantage.services.model.EventPackage;
import com.ticketadvantage.services.model.EventsPackage;
import com.ticketadvantage.services.model.MlRecordEvent;
import com.ticketadvantage.services.model.SpreadRecordEvent;
import com.ticketadvantage.services.model.TotalRecordEvent;
import com.ticketadvantage.services.site.util.AccountSite;
import com.ticketadvantage.services.telegram.TelegramBotSender;
import com.ticketadvantage.services.util.ServerInfo;

/**
 * @author jmiller
 *
 */
public class LambdaTransactionEvent implements RequestHandler<Integer, Integer> {
	private static final Logger LOGGER = Logger.getLogger(LambdaTransactionEvent.class);
	private static TicketAdvantageDB TicketAdvantageDB = new TicketAdvantageDB();
	private static SiteActiveDB SiteActiveDB = new SiteActiveDB();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final LambdaTransactionEvent lambdaTransactionEvent = new LambdaTransactionEvent();
		final Integer retValue = lambdaTransactionEvent.handleRequest(new Integer(491526), null);
		LOGGER.debug("retValue: " + retValue);
	}

	/**
	 * 
	 * @param eventId
	 * @param context
	 * @return
	 */
	public Integer handleRequest(Integer eventId, Context context) {
		LOGGER.info("Entering handleRequest()");
		LOGGER.error("eventId: " + eventId);
		LOGGER.debug("Context: " + context);
		LOGGER.error("Entering Date/Time: " + new Date());
		BaseRecordEvent event = null;
		AccountEvent accountEvent = null;
		Accounts account = null;
		Connection conn = null;
		boolean success = false;
		Integer retValue = new Integer(1);

		try {
			// Start a connection
			// org.postgresql.util.PSQLException: FATAL: sorry, too many clients already
			try {
				conn = getDbConnection();
			} catch (Throwable t) {
				LOGGER.error(t.getMessage(), t);

				if (t.getMessage().contains("too many clients already")) {
					// Sleep for 20 seconds
					Thread.sleep(20000);
					conn = getDbConnection();
				}
			}

			accountEvent = TicketAdvantageDB.getAccountEvent(eventId);
			LOGGER.error("AccountEvent: " + accountEvent);

			if (accountEvent != null) {
				account = TicketAdvantageDB.getAccount(accountEvent.getAccountid());
				LOGGER.error("Account Name: " + account.getName());
				LOGGER.debug("Account: " + account);
				LOGGER.error("SiteType: " + account.getSitetype());
				LOGGER.error("UserName: " + account.getUsername());

				// check for certain software packages
				if ("AGSoftware".equals(account.getSitetype()) || 
					"LineTracker".equals(account.getSitetype()) || 
					"Sports411Mobile".equals(account.getSitetype())) {
					LOGGER.error("SiteType2: " + account.getSitetype());
					LOGGER.error("UserName2: " + account.getUsername());

					if (SiteActiveDB.hasActiveSite(account.getSitetype(), account.getUsername())) {
						LOGGER.error("is active");
						boolean done = false;
						int count = 0;
						
						// Loop for one minute max
						while (!done && count < 580) {
							try {
								// Try ever 1/2 second
								Thread.sleep(500);
								if (!SiteActiveDB.hasActiveSite(account.getSitetype(), account.getUsername())) {
									LOGGER.error("not active");
									done = true;
									SiteActiveDB.persistSiteActive(account.getSitetype(), account.getUsername());
								} else {
									LOGGER.error("is active");
								}
								count++;
							} catch (InterruptedException ie) {
								done = true;
								SiteActiveDB.persistSiteActive(account.getSitetype(), account.getUsername());
							}
						}
					} else {
						LOGGER.error("not active");
						SiteActiveDB.persistSiteActive(account.getSitetype(), account.getUsername());
					}
				}
	
				// Check for spread vs. total vs. moneyline
				if (accountEvent.getSpreadid() != null && accountEvent.getSpreadid() != 0) {
					event = TicketAdvantageDB.getSpreadEvent(accountEvent.getSpreadid());
				} else if (accountEvent.getTotalid() != null && accountEvent.getTotalid() != 0) {
					event = TicketAdvantageDB.getTotalEvent(accountEvent.getTotalid());
				} else if (accountEvent.getMlid() != null && accountEvent.getMlid() != 0) {
					event = TicketAdvantageDB.getMlEvent(accountEvent.getMlid());
				}
				LOGGER.debug("BaseRecordEvent: " + event);
	
				// Process the account
				success = processAccount(event, account, accountEvent);

				if (success) {
					LOGGER.error("ProcessAccount success!");
					retValue = new Integer(0);

					// Successful transaction placed; update
					updateAccountInformation(event, accountEvent);

					if (accountEvent.getAccesstoken() != null && accountEvent.getAccesstoken().length() > 0 && 
						event.getTextnumber() != null && event.getTextnumber().length() > 0) {
						try {
//								final SendText sendText = new SendText();
//								sendText.setOAUTH2_TOKEN(accessToken);								
//								sendText.sendTextWithMessage(event.getTextnumber(), "Success: " + accountEvent.getName() + "-" + event.getEventname());
							TelegramBotSender.sendToTelegram(event.getTextnumber(),
									"Success: " + accountEvent.getName() + "-" + event.getEventname());
							LOGGER.error(event.getTextnumber() + " Success: " + accountEvent.getName() + "-"
									+ event.getEventname());
						} catch (Throwable be) {
							LOGGER.error(be.getMessage(), be);
						}
					}
				} else {	
					if (accountEvent.getAccesstoken() != null && accountEvent.getAccesstoken().length() > 0 && 
						event.getTextnumber() != null && event.getTextnumber().length() > 0) {
						try {
//								final SendText sendText = new SendText();
//								sendText.setOAUTH2_TOKEN(accessToken);
//								sendText.sendTextWithMessage(event.getTextnumber(), "Fail: " + accountEvent.getName() + "-" + event.getEventname());
							TelegramBotSender.sendToTelegram(event.getTextnumber(),
									"Fail: " + accountEvent.getName() + "-" + event.getEventname());
							LOGGER.error(event.getTextnumber() + " Fail: " + accountEvent.getName() + "-"
									+ event.getEventname());
						} catch (Throwable be) {
							LOGGER.error(be.getMessage(), be);
						}
					}
				}
				LOGGER.debug("AccountEvent update: " + accountEvent);
				LOGGER.debug("BaseRecordEvent update: " + event);
	
				// Update the events
				TicketAdvantageDB.updateAccountEvent(accountEvent);
	
				// Now update the record
				if (event instanceof SpreadRecordEvent) {
					TicketAdvantageDB.updateSpreadEvent((SpreadRecordEvent) event);
				} else if (event instanceof TotalRecordEvent) {
					TicketAdvantageDB.updateTotalEvent((TotalRecordEvent) event);
				} else if (event instanceof MlRecordEvent) {
					TicketAdvantageDB.updateMlEvent((MlRecordEvent) event);
				}

				if ("AGSoftware".equals(account.getSitetype()) || 
					"LineTracker".equals(account.getSitetype()) || 
					"Sports411Mobile".equals(account.getSitetype())) {
					try {
						SiteActiveDB.deleteSiteActive(account.getSitetype(), account.getUsername());
					} catch (Throwable th) {
						LOGGER.error(th.getMessage(), th);
					}
				}
			} else {
				// TODO
//					final SendText sendText = new SendText();
//					sendText.sendTextWithMessage(event.getTextnumber(), "Fail: " + accountEvent.getName() + "-" + event.getEventname());
//					LOGGER.error(event.getTextnumber() + " Fail: " + accountEvent.getName() + "-" + event.getEventname());
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);

			if (account != null) {
				try {
					SiteActiveDB.deleteSiteActive(account.getSitetype(), account.getUsername());
				} catch (Throwable th) {
					LOGGER.error(th.getMessage(), th);
				}	
			} else {				
			}
		} finally {
			try {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException sqle) {
				LOGGER.error(sqle.getMessage(), sqle);
			}
		}

		LOGGER.error("Exiting Date/Time: " + new Date());
		LOGGER.info("Exiting doProcess()");
		return retValue;
	}

	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	private Connection getDbConnection() throws SQLException {
		Connection conn = null;

		// org.postgresql.util.PSQLException: FATAL: sorry, too many clients already
		String url = "jdbc:postgresql://" + ServerInfo.getHost() + ":" + ServerInfo.getDbPort() + "/tadatabase";
		String username = "lambdauser";
		String password = "3id39d";
		conn = DriverManager.getConnection(url, username, password);

		// All LargeObject API calls must be within a transaction block
		conn.setAutoCommit(false);
		TicketAdvantageDB.setConn(conn);
		SiteActiveDB.setConn(conn);

		return conn;
	}

	/**
	 * 
	 * @param event
	 * @param account
	 * @param ae
	 * @return
	 */
	private boolean processAccount(BaseRecordEvent event, Accounts account, AccountEvent ae) {
		LOGGER.info("Entering processAccount()");
		LOGGER.debug("Event: " + event);
		LOGGER.debug("Account: " + account);
		LOGGER.debug("AccountEvent: " + ae);
		boolean wasSuccessful = false;

		try {
			final SiteProcessor processSite = AccountSite.GetAccountSite(account);
			processSite.setHumanspeed(ae.getHumanspeed());
			LOGGER.debug("ProcessSite: " + processSite);

			if (event instanceof SpreadRecordEvent) {
				processSite.processSpreadTransaction((SpreadRecordEvent) event, ae);
			} else if (event instanceof TotalRecordEvent) {
				processSite.processTotalTransaction((TotalRecordEvent) event, ae);
			} else if (event instanceof MlRecordEvent) {
				processSite.processMlTransaction((MlRecordEvent) event, ae);
			}

			// Setup the extra data
			// setupEventData(ae);

			wasSuccessful = true;
		} catch (BatchException be) {
			LOGGER.error("BatchException for Event: " + event + " and account: " + account + " Account Event: " + ae,
					be);
			ae.setErrorcode(be.getErrorcode());
			ae.setAccounthtml(be.getHtml());
			ae.setStatus("Error");

			// Check to make sure size is not more than 4000 characters
			if (be.getErrormessage() != null && be.getErrormessage().length() > 4000) {
				String tempErrorMessage = be.getErrormessage();
				tempErrorMessage = tempErrorMessage.substring(0, 4000);
				ae.setErrormessage(tempErrorMessage);
			} else {
				ae.setErrormessage(be.getErrormessage());
			}

			// Check to make sure size is not more than 4000 characters
			if (be.getMessage() != null && be.getMessage().length() > 4000) {
				String tempMessage = be.getMessage();
				tempMessage = tempMessage.substring(0, 4000);
				ae.setErrorexception(tempMessage);
			} else {
				ae.setErrorexception(be.getMessage());
			}

			// Update the account event
			try {
				LOGGER.debug("AccountEvent update: " + ae);
				LOGGER.debug("BaseRecordEvent update: " + event);

				// Setup the extra data
				// setupEventData(ae);
				ae.setEventid(event.getRotationid());

				TicketAdvantageDB.updateAccountEvent(ae);
				if (event instanceof SpreadRecordEvent) {
					TicketAdvantageDB.updateSpreadEvent((SpreadRecordEvent) event);
				} else if (event instanceof TotalRecordEvent) {
					TicketAdvantageDB.updateTotalEvent((TotalRecordEvent) event);
				} else if (event instanceof MlRecordEvent) {
					TicketAdvantageDB.updateMlEvent((MlRecordEvent) event);
				}
			} catch (Throwable t) {
				LOGGER.error(
						"BatchException for Event: " + event + " and account: " + account + " Account Event: " + ae, t);
			}
		} catch (Throwable t) {
			LOGGER.error("BatchException for Event: " + event + " and account: " + account + " Account Event: " + ae,
					t);
			ae.setErrorcode(BatchErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION);
			ae.setErrormessage(BatchErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
			ae.setErrorexception(t.getMessage());
			ae.setStatus("Error");

			// Update the account event
			try {
				LOGGER.debug("AccountEvent update: " + ae);
				LOGGER.debug("BaseRecordEvent update: " + event);

				// Setup the extra data
				// setupEventData(ae);

				ae.setEventid(event.getRotationid());
				TicketAdvantageDB.updateAccountEvent(ae);
				if (event instanceof SpreadRecordEvent) {
					TicketAdvantageDB.updateSpreadEvent((SpreadRecordEvent) event);
				} else if (event instanceof TotalRecordEvent) {
					TicketAdvantageDB.updateTotalEvent((TotalRecordEvent) event);
				} else if (event instanceof MlRecordEvent) {
					TicketAdvantageDB.updateMlEvent((MlRecordEvent) event);
				}
			} catch (Throwable th) {
				LOGGER.error(
						"BatchException for Event: " + event + " and account: " + account + " Account Event: " + ae,
						th);
			}
		}

		LOGGER.info("Exiting processAccount()");
		return wasSuccessful;
	}

	/**
	 * 
	 * @param event
	 * @param ae
	 * @throws BatchException
	 */
	private void updateAccountInformation(BaseRecordEvent event, AccountEvent ae) throws BatchException {
		LOGGER.info("Entering updateAccountInformation()");
		LOGGER.debug("Event: " + event);
		LOGGER.debug("AccountEvent: " + ae);

		try {
			// Set as complete
			ae.setIscompleted(true);
			ae.setStatus("Complete");

			// Update the account event
			TicketAdvantageDB.updateAccountEvent(ae);

			// Now check if all the other accounts have been updated and are
			// successful
			if (event instanceof SpreadRecordEvent) {
				final List<AccountEvent> accountEvents = TicketAdvantageDB.getSpreadActiveAccountEvents(event.getId());
				if (accountEvents == null || accountEvents.isEmpty()) {
					event.setIscompleted(true);
					TicketAdvantageDB.updateSpreadEvent((SpreadRecordEvent) event);
				}
			} else if (event instanceof TotalRecordEvent) {
				final List<AccountEvent> accountEvents = TicketAdvantageDB.getTotalActiveAccountEvents(event.getId());
				if (accountEvents == null || accountEvents.isEmpty()) {
					event.setIscompleted(true);
					TicketAdvantageDB.updateTotalEvent((TotalRecordEvent) event);
				}
			} else if (event instanceof MlRecordEvent) {
				final List<AccountEvent> accountEvents = TicketAdvantageDB.getMlActiveAccountEvents(event.getId());
				if (accountEvents == null || accountEvents.isEmpty()) {
					event.setIscompleted(true);
					TicketAdvantageDB.updateMlEvent((MlRecordEvent) event);
				}
			}
		} catch (Exception e) {
			throw new BatchException(BatchErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,
					BatchErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION, e.getMessage());
		}

		LOGGER.info("Exiting updateAccountInformation()");
	}

	/**
	 * 
	 * @param ae
	 */
	void setupEventData(AccountEvent ae) {
		try {
			final SportsInsightsSite sportsInsightSite = new SportsInsightsSite("https://sportsinsights.actionnetwork.com", "mojaxsventures@gmail.com", "action1");
			final EventsPackage ep = sportsInsightSite.getSport(ae.getSport());
			if (ep != null && ep.getEvents() != null && !ep.getEvents().isEmpty()) {
				final Iterator<EventPackage> itr = ep.getEvents().iterator();
				boolean found = false;
				while (itr.hasNext() && !found) {
					final EventPackage eventPackage = itr.next();
	
					// Make sure valid
					if (eventPackage.getTeamone() != null && eventPackage.getTeamone().getId() != null &&
						eventPackage.getTeamtwo() != null && eventPackage.getTeamtwo().getId() != null) {
						final int idOne = eventPackage.getTeamone().getId();
						final int firstHalfIdOne = eventPackage.getTeamone().getId() + 1000;
						final int secondHalfIdOne = eventPackage.getTeamone().getId() + 2000;
						final int idTwo = eventPackage.getTeamtwo().getId();
						final int firstHalfIdTwo = eventPackage.getTeamtwo().getId() + 1000;
						final int secondHalfIdTwo = eventPackage.getTeamtwo().getId() + 2000;
	
						if (idOne == ae.getEventid().intValue() || 
							idTwo == ae.getEventid().intValue()) {
							// Found it
							ae.setEventdatetime(eventPackage.getEventdatetime());
							found = true;
						} else if (firstHalfIdOne == ae.getEventid().intValue() ||
								firstHalfIdTwo == ae.getEventid().intValue()) {
							// Found it
							ae.setEventdatetime(eventPackage.getEventdatetime());
							found = true;
						} else if (secondHalfIdOne == ae.getEventid().intValue() ||
								secondHalfIdTwo == ae.getEventid().intValue()) {
							// Found it
							ae.setEventdatetime(eventPackage.getEventdatetime());
							found = true;
						}
					}
				}
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}
	}
}