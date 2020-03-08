/**
 * 
 */
package com.ticketadvantage.services.service;

import java.util.Set;

import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.sites.SiteProcessor;
import com.ticketadvantage.services.dao.sites.SiteTransaction;
import com.ticketadvantage.services.dao.sites.sportsinsights.SportsInsightsSite;
import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.BacksideOutput;
import com.ticketadvantage.services.model.EventPackage;
import com.ticketadvantage.services.model.MlRecordEvent;
import com.ticketadvantage.services.model.PreviewInput;
import com.ticketadvantage.services.model.SportsInsightsEvent;
import com.ticketadvantage.services.model.SpreadRecordEvent;
import com.ticketadvantage.services.model.TotalRecordEvent;
import com.ticketadvantage.services.site.util.AccountSite;

/**
 * @author jmiller
 *
 */
public class PreviewAndCompleteResource extends BasePreviewAndComplete implements Runnable {
	private static final Logger LOGGER = Logger.getLogger(PreviewAndCompleteResource.class);
	private SiteProcessor siteProcessor;

	/**
	 * 
	 */
	public PreviewAndCompleteResource() {
		super();
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

				SiteEventsDB.updateAccountEvent(accountEvent);
				if (event instanceof SpreadRecordEvent) {
					SiteEventsDB.updateSpreadEvent((SpreadRecordEvent) event);
				} else if (event instanceof TotalRecordEvent) {
					SiteEventsDB.updateTotalEvent((TotalRecordEvent) event);
				} else if (event instanceof MlRecordEvent) {
					SiteEventsDB.updateMlEvent((MlRecordEvent) event);
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
				SiteEventsDB.updateAccountEvent(accountEvent);
				if (event instanceof SpreadRecordEvent) {
					SiteEventsDB.updateSpreadEvent((SpreadRecordEvent) event);
				} else if (event instanceof TotalRecordEvent) {
					SiteEventsDB.updateTotalEvent((TotalRecordEvent) event);
				} else if (event instanceof MlRecordEvent) {
					SiteEventsDB.updateMlEvent((MlRecordEvent) event);
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

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.service.BasePreviewAndComplete#createLineWatch()
	 */
	@Override
	protected void createLineWatch() throws BatchException {
		LOGGER.info("Entering createLineWatch()");

		final SportsInsightsSite processSite = new SportsInsightsSite("https://sportsinsights.actionnetwork.com",
				"mojaxsventures@gmail.com", 
				"action1");
		final SportsInsightsEvent sie = processSite.getEventByIdAll(event.getRotationid().toString(), AccountSite.GetSportName(event.getSport()), 1);
		LOGGER.debug("SportsInsightsEvent: " + sie);

		if (sie != null) {
			if ("total".equals(eventType)) {
				final TotalRecordEvent te = (TotalRecordEvent)event;
				String team = "";
				Double value = null;

				if (te.getTotalinputfirstone() != null && te.getTotalinputfirstone().length() > 0) {
					team = te.getEventteam1();
					value = Double.valueOf(te.getTotalinputfirstone());
				} else {
					team = te.getEventteam2();
					value = Double.valueOf(te.getTotalinputsecondone());
				}
				
				// * @param gameType 0=game, 1=1h, 2=2h, 3=3h, 6=live
				// * @param lineType 1=spread, 2=ml, 3=total
				processSite.createLineWatch(sie, 0, 3, team, value, new Double(-9999));
			} else {
				final MlRecordEvent me = (MlRecordEvent)event;
				String team = "";
				Double value = null;

				if (me.getMlinputfirstone() != null && me.getMlinputfirstone().length() > 0) {
					team = me.getEventteam1();

					if ("-".equals(me.getMlplusminusfirstone())) {
						value = Double.valueOf("-" + me.getMlinputfirstone());
					} else {
						value = Double.valueOf(me.getMlinputfirstone());
					}
				} else {
					team = me.getEventteam2();

					if ("-".equals(me.getMlplusminussecondone())) {
						value = Double.valueOf("-" + me.getMlinputsecondone());
					} else {
						value = Double.valueOf(me.getMlinputsecondone());
					}
				}

				// * @param gameType 0=game, 1=1h, 2=2h, 3=3h, 6=live
				// * @param lineType 1=spread, 2=ml, 3=total
				processSite.createLineWatch(sie, 0, 2, team, new Double(-9999), value);						
			}
		}

		LOGGER.info("Exiting createLineWatch()");
	}
}