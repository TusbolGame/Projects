/**
 * 
 */
package com.ticketadvantage.services.service;

import java.util.List;

import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.AccountDAO;
import com.ticketadvantage.services.dao.RecordEventDAO;
import com.ticketadvantage.services.dao.sites.SiteProcessor;
import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.AccountEvent;
import com.ticketadvantage.services.model.Accounts;
import com.ticketadvantage.services.model.BaseRecordEvent;
import com.ticketadvantage.services.model.MlRecordEvent;
import com.ticketadvantage.services.model.SpreadRecordEvent;
import com.ticketadvantage.services.model.TotalRecordEvent;
import com.ticketadvantage.services.site.util.AccountSite;
import com.ticketadvantage.services.telegram.TelegramBotSender;

/**
 * @author jmiller
 *
 */
public class TransactionEventResource implements Runnable {
	private static final Logger LOGGER = Logger.getLogger(TransactionEventResource.class);
	private RecordEventDAO recordEventDAO;
	private AccountDAO accountDAO;
	private AccountEvent accountEvent;
	private boolean sendTextOnFailure = true;
	private boolean isRetry = false;

	/**
	 * 
	 */
	public TransactionEventResource() {
		super();
		LOGGER.debug("Entering TransactionEventResource()");
		LOGGER.debug("Exiting TransactionEventResource()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * @return the sendTextOnFailure
	 */
	public boolean isSendTextOnFailure() {
		return sendTextOnFailure;
	}

	/**
	 * @param sendTextOnFailure the sendTextOnFailure to set
	 */
	public void setSendTextOnFailure(boolean sendTextOnFailure) {
		this.sendTextOnFailure = sendTextOnFailure;
	}

	/**
	 * @return the isRetry
	 */
	public boolean isRetry() {
		return isRetry;
	}

	/**
	 * @param isRetry the isRetry to set
	 */
	public void setRetry(boolean isRetry) {
		this.isRetry = isRetry;
	}

	/**
	 * @return the recordEventDAO
	 */
	public RecordEventDAO getRecordEventDAO() {
		return recordEventDAO;
	}

	/**
	 * @param recordEventDAO the recordEventDAO to set
	 */
	public void setRecordEventDAO(RecordEventDAO recordEventDAO) {
		this.recordEventDAO = recordEventDAO;
	}

	/**
	 * @return the accountDAO
	 */
	public AccountDAO getAccountDAO() {
		return accountDAO;
	}

	/**
	 * @param accountDAO the accountDAO to set
	 */
	public void setAccountDAO(AccountDAO accountDAO) {
		this.accountDAO = accountDAO;
	}

	/**
	 * @return the accountEvent
	 */
	public AccountEvent getAccountEvent() {
		return accountEvent;
	}

	/**
	 * @param accountEvent the accountEvent to set
	 */
	public void setAccountEvent(AccountEvent accountEvent) {
		this.accountEvent = accountEvent;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		LOGGER.info("Entering run()");

		try {
			// Process the event
			doProcess(accountEvent);
		} catch (BatchException be) {
			LOGGER.error(be.getMessage() + accountEvent, be);
		} catch (Throwable t) {
			LOGGER.error(t.getMessage() + accountEvent, t);
		}

		LOGGER.info("Exiting run()");
	}

	/**
	 * 
	 * @param event
	 * @throws BatchException
	 */
	private void doProcess(AccountEvent accountEvent) throws BatchException {
		LOGGER.info("Entering doProcess()");
		LOGGER.debug("AccountEvent: " + accountEvent);
		BaseRecordEvent event = null;

		// Begin the transaction
		if (accountEvent.getSpreadid() != null && accountEvent.getSpreadid().longValue() != 0) {
			LOGGER.debug("accountEvent.getSpreadid(): " + accountEvent.getSpreadid());
			event = recordEventDAO.getSpreadEvent(accountEvent.getSpreadid());
		} else if (accountEvent.getTotalid() != null && accountEvent.getTotalid().longValue() != 0) {
			LOGGER.debug("accountEvent.getTotalid(): " + accountEvent.getTotalid());
			event = recordEventDAO.getTotalEvent(accountEvent.getTotalid());
		} else if (accountEvent.getMlid() != null && accountEvent.getMlid().longValue() != 0) {
			LOGGER.debug("accountEvent.getMlid(): " + accountEvent.getMlid());
			event = recordEventDAO.getMlEvent(accountEvent.getMlid());
		}
		LOGGER.debug("Event: " + event);

		final Accounts account = accountDAO.getAccount(accountEvent.getAccountid());
		LOGGER.debug("Account: " + account);

		if (account.getIscomplexcaptcha()) {
			account.setShowrequestresponse(false);
		}

		// 
		// Was the transaction successful?
		// 
		boolean isSuccessful = processAccount(event, account, accountEvent);

		if (isSuccessful) {
			// Set as complete
			accountEvent.setIscompleted(true);
			accountEvent.setStatus("Complete");

			// Successful transaction placed; update
			updateAccountInformation(event, accountEvent);

			// 
			// Send a text
			// 
				if (event.getTextnumber() != null && event.getTextnumber().length() > 0) {
					try {
//						TelegramBotSender.sendToTelegram(event.getTextnumber(), "Success: " + accountEvent.getName() + "-" + event.getEventname());
						LOGGER.error(event.getTextnumber() + " Success: " + accountEvent.getName() + "-" + event.getEventname());
					} catch (Exception be) {
						LOGGER.error(be.getMessage(), be);
					}
				}
		} else {
			if (sendTextOnFailure && event.getTextnumber() != null && event.getTextnumber().length() > 0) {
				try {
					TelegramBotSender.sendToTelegram(event.getTextnumber(), " Fail: " + accountEvent.getName() + "-" + event.getEventname());
					LOGGER.error(event.getTextnumber() + " Fail: " + accountEvent.getName() + "-" + event.getEventname());
				} catch (Exception be) {
					LOGGER.error(be.getMessage(), be);
				}
			}
		}

		recordEventDAO.updateAccountEvent(accountEvent);
		if (event instanceof SpreadRecordEvent) {
			recordEventDAO.updateSpreadEvent((SpreadRecordEvent) event);
		} else if (event instanceof TotalRecordEvent) {
			recordEventDAO.updateTotalEvent((TotalRecordEvent) event);
		} else if (event instanceof MlRecordEvent) {
			recordEventDAO.updateMlEvent((MlRecordEvent) event);
		}

		LOGGER.info("Exiting doProcess()");
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
			if (accountEvent.getHumanspeed() != null) {
				processSite.setHumanspeed(accountEvent.getHumanspeed());
			}
			LOGGER.debug("ProcessSite: " + processSite);

			// Don't write to log file for retry logic
			if (isRetry) {
				processSite.getHttpClientWrapper().setShowRequestResponse(false);
			}

			if (event instanceof SpreadRecordEvent) {
				processSite.processSpreadTransaction((SpreadRecordEvent) event, ae);
			} else if (event instanceof TotalRecordEvent) {
				processSite.processTotalTransaction((TotalRecordEvent) event, ae);
			} else if (event instanceof MlRecordEvent) {
				processSite.processMlTransaction((MlRecordEvent) event, ae);
			}

			wasSuccessful = true;
		} catch (BatchException be) {
			LOGGER.error("BatchException for Event: " + event + " and account: "
					+ account + " Account Event: " + ae, be);
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
				ae.setEventid(event.getRotationid());
				recordEventDAO.updateAccountEvent(ae);

				if (event instanceof SpreadRecordEvent) {
					recordEventDAO.updateSpreadEvent((SpreadRecordEvent)event);
				} else if (event instanceof TotalRecordEvent) {
					recordEventDAO.updateTotalEvent((TotalRecordEvent)event);
				} else if (event instanceof MlRecordEvent) {
					recordEventDAO.updateMlEvent((MlRecordEvent)event);
				}
			} catch (Throwable t) {
				LOGGER.error("BatchException for Event: " + event + " and account: "
						+ account + " Account Event: " + ae, t);				
			}
		} catch (Throwable t) {
			LOGGER.error("BatchException for Event: " + event + " and account: "
					+ account + " Account Event: " + ae, t);
			ae.setErrorcode(BatchErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION);
			ae.setErrormessage(BatchErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
			ae.setErrorexception(t.getMessage());
			ae.setStatus("Error");

			// Update the account event
			try {
				ae.setEventid(event.getRotationid());
				recordEventDAO.updateAccountEvent(ae);

				if (event instanceof SpreadRecordEvent) {
					recordEventDAO.updateSpreadEvent((SpreadRecordEvent)event);
				} else if (event instanceof TotalRecordEvent) {
					recordEventDAO.updateTotalEvent((TotalRecordEvent)event);
				} else if (event instanceof MlRecordEvent) {
					recordEventDAO.updateMlEvent((MlRecordEvent)event);
				}
			} catch (Throwable th) {
				LOGGER.error("BatchException for Event: " + event + " and account: "
						+ account + " Account Event: " + ae, th);				
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

		// Set to complete
		ae.setIscompleted(true);
		ae.setStatus("Complete");

		// Update the account event
		recordEventDAO.updateAccountEvent(ae);
		
		// Now check if all the other accounts have been updated and are successful
		if (event instanceof SpreadRecordEvent) {
			final List<AccountEvent> accountEvents = recordEventDAO.getSpreadActiveAccountEvents(event.getId());
			if (accountEvents == null || accountEvents.isEmpty()) {
				event.setIscompleted(true);
				recordEventDAO.updateSpreadEvent((SpreadRecordEvent) event);
			}
		} else if (event instanceof TotalRecordEvent) {
			final List<AccountEvent> accountEvents = recordEventDAO.getTotalActiveAccountEvents(event.getId());
			if (accountEvents == null || accountEvents.isEmpty()) {
				event.setIscompleted(true);
				recordEventDAO.updateTotalEvent((TotalRecordEvent) event);
			}
		} else if (event instanceof MlRecordEvent) {
			final List<AccountEvent> accountEvents = recordEventDAO.getMlActiveAccountEvents(event.getId());
			if (accountEvents == null || accountEvents.isEmpty()) {
				event.setIscompleted(true);
				recordEventDAO.updateMlEvent((MlRecordEvent) event);
			}
		}

		LOGGER.info("Exiting updateAccountInformation()");
	}
}