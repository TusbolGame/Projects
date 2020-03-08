/**
 * 
 */
package com.ticketadvantage.services.service;

import java.util.Set;

import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.sites.SiteProcessor;
import com.ticketadvantage.services.dao.sites.SiteTransaction;
import com.ticketadvantage.services.db.SiteActiveDosDB;
import com.ticketadvantage.services.db.SiteEventsDosDB;
import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.AccountEvent;
import com.ticketadvantage.services.model.Accounts;
import com.ticketadvantage.services.model.BacksideOutput;
import com.ticketadvantage.services.model.BaseRecordEvent;
import com.ticketadvantage.services.model.EventPackage;
import com.ticketadvantage.services.model.MlRecordEvent;
import com.ticketadvantage.services.model.PreviewInput;
import com.ticketadvantage.services.model.SpreadRecordEvent;
import com.ticketadvantage.services.model.TotalRecordEvent;

/**
 * @author jmiller
 *
 */
public class BestPriceResource extends BaseBestPrice implements Runnable {
	private static final Logger LOGGER = Logger.getLogger(BestPriceResource.class);
	private SiteProcessor siteProcessor;

	/**
	 * 
	 * @param event
	 * @param accountEvent
	 * @param account
	 * @param eventType
	 * @param counter
	 * @param sleepTime
	 * @param mobileTextNumber
	 * @param sendtextforaccount
	 * @param SITESACTIVEDB
	 * @param SITESEVENTSDB
	 * @param siteProcessor
	 */
	public BestPriceResource(BaseRecordEvent event,
			AccountEvent accountEvent, 
			Accounts account, 
			String eventType, 
			Integer counter, 
			Integer sleepTime, 
			String mobileTextNumber, 
			Boolean sendtextforaccount,
			SiteActiveDosDB SITEACTIVEDB,
			SiteEventsDosDB SITEEVENTSDB,
			SiteProcessor siteProcessor) {
		super(event, accountEvent, account, eventType, counter, sleepTime, mobileTextNumber, sendtextforaccount, SITEACTIVEDB, SITEEVENTSDB);
		this.siteProcessor = siteProcessor;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.service.BasePreviewAndComplete#startProcessing()
	 */
	@Override
	public void startProcessing() {
		new Thread(this).start();
	}

	/**
	 * @return the siteProcessor
	 */
	public SiteProcessor getSiteProcessor() {
		return siteProcessor;
	}

	/**
	 * @param siteProcessor the siteProcessor to set
	 */
	public void setSiteProcessor(SiteProcessor siteProcessor) {
		this.siteProcessor = siteProcessor;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.service.BasePreviewAndComplete#setupInitialSite(com.ticketadvantage.services.model.PreviewInput)
	 */
	@Override
	protected void setupInitialSite(PreviewInput previewInput) throws BatchException {
		LOGGER.info("Entering setupInitialSite()");

		// Login and get to menu page
		siteProcessor.setupInitial(previewInput);

		LOGGER.info("Exiting setupInitialSite()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.service.BasePreviewAndComplete#setupSiteData()
	 */
	@Override
	protected void setupSiteData() throws BatchException {
		LOGGER.info("Entering setupSiteData()");

		siteProcessor.setUserid(event.getUserid());
		siteProcessor.MAP_DATA.clear();
		final Set<String> keys = siteProcessor.DYNAMIC_DATA.keySet();
		for (String key : keys) {
			siteProcessor.MAP_DATA.put(key, siteProcessor.DYNAMIC_DATA.get(key));
		}

		LOGGER.info("Exiting setupSiteData()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.service.BasePreviewAndComplete#checkGame(com.ticketadvantage.services.model.PreviewInput)
	 */
	@Override
	protected BacksideOutput checkGame(PreviewInput previewInput) throws BatchException {
		LOGGER.info("Entering checkGame()");

		final BacksideOutput previewOutput = siteProcessor.checkGame(previewInput);
		
		LOGGER.info("Exiting checkGame()");
		return previewOutput;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		LOGGER.info("Entering run()");

		try {
			processEvent();
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		LOGGER.info("Exiting run()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.service.BasePreviewAndComplete#processAccount(com.ticketadvantage.services.model.EventPackage)
	 */
	@Override
	protected boolean processAccount(EventPackage ep) {
		LOGGER.info("Entering processAccount()");
		boolean wasSuccessful = false;

		try {
			if (event instanceof SpreadRecordEvent) {
				// Setup site transaction
				final SiteTransaction siteTransaction = siteProcessor.getSpreadTransaction((SpreadRecordEvent)event, ep, accountEvent);

				// Finish the transaction
				siteProcessor.finishTransaction(siteTransaction, ep, event, accountEvent);
			} else if (event instanceof TotalRecordEvent) {
				// Setup site transaction
				final SiteTransaction siteTransaction = siteProcessor.getTotalTransaction((TotalRecordEvent)event, ep, accountEvent);
		
				// Finish the transaction
				siteProcessor.finishTransaction(siteTransaction, ep, event, accountEvent);
			} else if (event instanceof MlRecordEvent) {
				// Setup site transaction
				final SiteTransaction siteTransaction = siteProcessor.getMlTransaction((MlRecordEvent)event, ep, accountEvent);

				// Finish the transaction
				siteProcessor.finishTransaction(siteTransaction, ep, event, accountEvent);
			}

			wasSuccessful = true;
		} catch (BatchException be) {
			LOGGER.error("BatchException for Event: " + event + " and account: " + account + " Account Event: " + accountEvent,
					be);
			accountEvent.setErrorcode(be.getErrorcode());
			accountEvent.setAccounthtml(be.getHtml());
			accountEvent.setStatus("Error");

			// Check to make sure size is not more than 4000 characters
			if (be.getErrormessage() != null && be.getErrormessage().length() > 4000) {
				String tempErrorMessage = be.getErrormessage();
				tempErrorMessage = tempErrorMessage.substring(0, 4000);
				accountEvent.setErrormessage(tempErrorMessage);
			} else {
				accountEvent.setErrormessage(be.getErrormessage());
			}

			// Check to make sure size is not more than 4000 characters
			if (be.getMessage() != null && be.getMessage().length() > 4000) {
				String tempMessage = be.getMessage();
				tempMessage = tempMessage.substring(0, 4000);
				accountEvent.setErrorexception(tempMessage);
			} else {
				accountEvent.setErrorexception(be.getMessage());
			}

			// Update the account event
			try {
				LOGGER.debug("AccountEvent update: " + accountEvent);
				LOGGER.debug("BaseRecordEvent update: " + event);

				// Setup the extra data
				// setupEventData(ae);
				accountEvent.setEventid(event.getRotationid());

				SITEEVENTSDB.updateAccountEvent(accountEvent);
				if (event instanceof SpreadRecordEvent) {
					SITEEVENTSDB.updateSpreadEvent((SpreadRecordEvent) event);
				} else if (event instanceof TotalRecordEvent) {
					SITEEVENTSDB.updateTotalEvent((TotalRecordEvent) event);
				} else if (event instanceof MlRecordEvent) {
					SITEEVENTSDB.updateMlEvent((MlRecordEvent) event);
				}
			} catch (Throwable t) {
				LOGGER.error(
						"BatchException for Event: " + event + " and account: " + account + " Account Event: " + accountEvent, t);
			}
		} catch (Throwable t) {
			LOGGER.error("BatchException for Event: " + event + " and account: " + account + " Account Event: " + accountEvent,
					t);
			accountEvent.setErrorcode(BatchErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION);
			accountEvent.setErrormessage(BatchErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
			accountEvent.setErrorexception(t.getMessage());
			accountEvent.setStatus("Error");

			// Update the account event
			try {
				LOGGER.debug("AccountEvent update: " + accountEvent);
				LOGGER.debug("BaseRecordEvent update: " + event);

				accountEvent.setEventid(event.getRotationid());
				SITEEVENTSDB.updateAccountEvent(accountEvent);

				if (event instanceof SpreadRecordEvent) {
					SITEEVENTSDB.updateSpreadEvent((SpreadRecordEvent) event);
				} else if (event instanceof TotalRecordEvent) {
					SITEEVENTSDB.updateTotalEvent((TotalRecordEvent) event);
				} else if (event instanceof MlRecordEvent) {
					SITEEVENTSDB.updateMlEvent((MlRecordEvent) event);
				}
			} catch (Throwable th) {
				LOGGER.error(
						"BatchException for Event: " + event + " and account: " + account + " Account Event: " + accountEvent,
						th);
			}
		}

		LOGGER.info("Exiting processAccount()");
		return wasSuccessful;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BestPriceResource [siteProcessor=" + siteProcessor + "] " + super.toString();
	}
}