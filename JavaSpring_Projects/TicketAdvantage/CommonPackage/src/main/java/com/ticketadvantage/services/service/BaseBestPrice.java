/**
 * 
 */
package com.ticketadvantage.services.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.ticketadvantage.services.db.SiteActiveDosDB;
import com.ticketadvantage.services.db.SiteEventsDosDB;
import com.ticketadvantage.services.email.SendText;
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
public abstract class BaseBestPrice {
	private static final Logger LOGGER = Logger.getLogger(BaseBestPrice.class);
	protected SiteActiveDosDB SITEACTIVEDB;
	protected SiteEventsDosDB SITEEVENTSDB;
	protected BaseRecordEvent event;
	protected AccountEvent accountEvent;
	protected Accounts account;
	protected String eventType;
	protected Integer counter;
	protected Integer sleepTime;
	protected String mobileTextNumber;
	protected Boolean sendtextforaccount;
	protected String eventLineIndicator;
	protected String eventLine;
	protected String eventJuiceIndicator;
	protected String eventJuice;
	protected Boolean done = new Boolean(false);
	protected Boolean isDoneWithPrices = new Boolean(false);
	protected EventPackage ep;
	protected BaseBestPriceBuyOrderResource bestPriceBuyOrderResource;

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
	 */
	public BaseBestPrice(BaseRecordEvent event,
			AccountEvent accountEvent, 
			Accounts account, 
			String eventType, 
			Integer counter, 
			Integer sleepTime,
			String mobileTextNumber,
			Boolean sendtextforaccount,
			SiteActiveDosDB SITEACTIVEDB,
			SiteEventsDosDB SITEEVENTSDB) {
		super();
		this.event = event;
		this.accountEvent = accountEvent;
		this.account = account;
		this.eventType = eventType;
		this.counter = counter;
		this.sleepTime = sleepTime;
		this.mobileTextNumber = mobileTextNumber;
		this.sendtextforaccount = sendtextforaccount;
		this.SITEACTIVEDB = SITEACTIVEDB;
		this.SITEEVENTSDB = SITEEVENTSDB;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * 
	 */
	public abstract void startProcessing();

	/**
	 * @return the event
	 */
	public BaseRecordEvent getEvent() {
		return event;
	}

	/**
	 * @param event the event to set
	 */
	public void setEvent(BaseRecordEvent event) {
		this.event = event;
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

	/**
	 * @return the account
	 */
	public Accounts getAccount() {
		return account;
	}

	/**
	 * @param account the account to set
	 */
	public void setAccount(Accounts account) {
		this.account = account;
	}

	/**
	 * @return the eventType
	 */
	public String getEventType() {
		return eventType;
	}

	/**
	 * @param eventType the eventType to set
	 */
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	/**
	 * @return the counter
	 */
	public Integer getCounter() {
		return counter;
	}

	/**
	 * @param counter the counter to set
	 */
	public void setCounter(Integer counter) {
		this.counter = counter;
	}

	/**
	 * @return the sleepTime
	 */
	public Integer getSleepTime() {
		return sleepTime;
	}

	/**
	 * @param sleepTime the sleepTime to set
	 */
	public void setSleepTime(Integer sleepTime) {
		this.sleepTime = sleepTime;
	}

	/**
	 * @return the mobileTextNumber
	 */
	public String getMobileTextNumber() {
		return mobileTextNumber;
	}

	/**
	 * @param mobileTextNumber the mobileTextNumber to set
	 */
	public void setMobileTextNumber(String mobileTextNumber) {
		this.mobileTextNumber = mobileTextNumber;
	}

	/**
	 * @return the sendtextforaccount
	 */
	public Boolean getSendtextforaccount() {
		return sendtextforaccount;
	}

	/**
	 * @param sendtextforaccount the sendtextforaccount to set
	 */
	public void setSendtextforaccount(Boolean sendtextforaccount) {
		this.sendtextforaccount = sendtextforaccount;
	}

	/**
	 * @return the eventLineIndicator
	 */
	public String getEventLineIndicator() {
		return eventLineIndicator;
	}

	/**
	 * @param eventLineIndicator the eventLineIndicator to set
	 */
	public void setEventLineIndicator(String eventLineIndicator) {
		this.eventLineIndicator = eventLineIndicator;
	}

	/**
	 * @return the eventLine
	 */
	public String getEventLine() {
		return eventLine;
	}

	/**
	 * @param eventLine the eventLine to set
	 */
	public void setEventLine(String eventLine) {
		this.eventLine = eventLine;
	}

	/**
	 * @return the eventJuiceIndicator
	 */
	public String getEventJuiceIndicator() {
		return eventJuiceIndicator;
	}

	/**
	 * @param eventJuiceIndicator the eventJuiceIndicator to set
	 */
	public void setEventJuiceIndicator(String eventJuiceIndicator) {
		this.eventJuiceIndicator = eventJuiceIndicator;
	}

	/**
	 * @return the eventJuice
	 */
	public String getEventJuice() {
		return eventJuice;
	}

	/**
	 * @param eventJuice the eventJuice to set
	 */
	public void setEventJuice(String eventJuice) {
		this.eventJuice = eventJuice;
	}

	/**
	 * @return the done
	 */
	public Boolean getDone() {
		return done;
	}

	/**
	 * @param done the done to set
	 */
	public void setDone(Boolean done) {
		this.done = done;
	}

	/**
	 * @return the isDoneWithPrices
	 */
	public Boolean getIsDoneWithPrices() {
		return isDoneWithPrices;
	}

	/**
	 * @param isDoneWithPrices the isDoneWithPrices to set
	 */
	public void setIsDoneWithPrices(Boolean isDoneWithPrices) {
		this.isDoneWithPrices = isDoneWithPrices;
	}

	/**
	 * @return the ep
	 */
	public EventPackage getEp() {
		return ep;
	}

	/**
	 * @param ep the ep to set
	 */
	public void setEp(EventPackage ep) {
		this.ep = ep;
	}

	/**
	 * @return the bestPriceBuyOrderResource
	 */
	public BaseBestPriceBuyOrderResource getBestPriceBuyOrderResource() {
		return bestPriceBuyOrderResource;
	}

	/**
	 * @param bestPriceBuyOrderResource the bestPriceBuyOrderResource to set
	 */
	public void setBestPriceBuyOrderResource(BaseBestPriceBuyOrderResource bestPriceBuyOrderResource) {
		this.bestPriceBuyOrderResource = bestPriceBuyOrderResource;
	}

	/**
	 * @return the sITESACTIVEDB
	 */
	public SiteActiveDosDB getSITEACTIVEDB() {
		return SITEACTIVEDB;
	}

	/**
	 * @param sITESACTIVEDB the sITESACTIVEDB to set
	 */
	public void setSITEACTIVEDB(SiteActiveDosDB sITEACTIVEDB) {
		SITEACTIVEDB = sITEACTIVEDB;
	}

	/**
	 * @return the sITESEVENTSDB
	 */
	public SiteEventsDosDB getSITEEVENTSDB() {
		return SITEEVENTSDB;
	}

	/**
	 * @param sITESEVENTSDB the sITESEVENTSDB to set
	 */
	public void setSITEEVENTSDB(SiteEventsDosDB sITEEVENTSDB) {
		SITEEVENTSDB = sITEEVENTSDB;
	}

	/**
	 * 
	 * @param previewInput
	 * @throws BatchException
	 */
	protected abstract void setupInitialSite(PreviewInput previewInput) throws BatchException;

	/**
	 * 
	 * @throws BatchException
	 */
	protected abstract void setupSiteData() throws BatchException;

	/**
	 * 
	 * @param previewInput
	 * @return
	 * @throws BatchException
	 */
	protected abstract BacksideOutput checkGame(PreviewInput previewInput) throws BatchException;

	/**
	 * 
	 * @param ep
	 * @return
	 */
	protected abstract boolean processAccount(EventPackage ep);

	/**
	 * 
	 */
	protected void processEvent() {
		LOGGER.info("Entering processEvent()");

		try {
			// check for certain software packages
			// checkSoftwarePackagesActive();		
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		try {
			try {
				// Setup PreviewInput
				final PreviewInput previewInput = setupPreview();

				// Login and get to menu page
				setupInitialSite(previewInput);

				// Setup the site data
				setupSiteData();

				// Get the game information
				final BacksideOutput previewOutput = checkGame(previewInput);
				previewOutput.setAccountid(account.getId());
				previewOutput.setAccountname(account.getName());
				LOGGER.error("previewOutput: " + previewOutput);

				this.eventLineIndicator = previewOutput.getLineindicator();
				this.eventLine = previewOutput.getLine();
				this.eventJuiceIndicator = previewOutput.getJuiceindicator();
				this.eventJuice = previewOutput.getJuice();
				ep = previewOutput.getEp();

				isDoneWithPrices = true;
			} catch (Throwable t) {
				LOGGER.error(t.getMessage(), t);
				isDoneWithPrices = true;
			}

			int timer = 300;

			// Wait until we get the go ahead
			while (!done && timer != 0) {
				try {
					// Sleep for defined seconds
					Thread.sleep(sleepTime.intValue());
					counter--;
					timer--;
				} catch (InterruptedException ie) {
					LOGGER.warn(ie.getMessage(), ie);
				}
			}

			LOGGER.debug("EventPackage: " + ep);
			if (ep != null) {
				// Process the transactions
				boolean wasSuccessful = false;

				if (processAccount(ep)) {
					LOGGER.error(account.getName() + " ProcessAccount success!");
	
					// Successful transaction placed; update
					updateAccountInformation(event, accountEvent);

					// Check if this account result should be sent
					if (this.sendtextforaccount != null && this.sendtextforaccount.booleanValue() && mobileTextNumber != null && mobileTextNumber.length() > 0) {
						sendText("Success");
					}

					wasSuccessful = true;
				} else {
					LOGGER.error(account.getName() + " ProcessAccount failed!");
					// Check if this account result should be sent
					if (this.sendtextforaccount != null && this.sendtextforaccount.booleanValue() && mobileTextNumber != null && mobileTextNumber.length() > 0) {
						sendText("Fail");
					}
				}
				LOGGER.debug("AccountEvent update: " + accountEvent);
				LOGGER.debug("BaseRecordEvent update: " + event);

				// Update the site event
				updateSiteEvent();

				// Now send necessary info back to main processor
				bestPriceBuyOrderResource.bestPriceResourceDone(wasSuccessful, accountEvent.getId());

				cleanUpSiteEvent();
			} else {
				cleanUpSiteEvent();
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
			bestPriceBuyOrderResource.bestPriceResourceDone(false, accountEvent.getId());
			cleanUpSiteEvent();
		}

		LOGGER.info("Exiting processEvent()");
	}

	/**
	 * 
	 */
	private void cleanUpSiteEvent() {
		LOGGER.info("Entering cleanUpSiteEvent()");

		if (account != null) {
			// check for certain software packages
			if ("AGSoftware".equals(account.getSitetype()) || "LineTracker".equals(account.getSitetype())) {
				try {
					SITEACTIVEDB.deleteSiteActive(account.getSitetype(), account.getUsername());
				} catch (Throwable th) {
					LOGGER.error(th.getMessage(), th);
				}
			}
		}

		LOGGER.info("Exiting cleanUpSiteEvent()");
	}

	/**
	 * 
	 * @throws SQLException
	 */
	private void updateSiteEvent() throws BatchException {
		LOGGER.info("Entering updateSiteEvent()");

		// Update the events
		SITEEVENTSDB.updateAccountEvent(accountEvent);

		// Now update the record
		if (event instanceof SpreadRecordEvent) {
			SITEEVENTSDB.updateSpreadEvent((SpreadRecordEvent) event);
		} else if (event instanceof TotalRecordEvent) {
			SITEEVENTSDB.updateTotalEvent((TotalRecordEvent) event);
		} else if (event instanceof MlRecordEvent) {
			SITEEVENTSDB.updateMlEvent((MlRecordEvent) event);
		}

		if ("AGSoftware".equals(account.getSitetype()) || "LineTracker".equals(account.getSitetype())) {
			try {
				SITEACTIVEDB.deleteSiteActive(account.getSitetype(), account.getUsername());
			} catch (Throwable th) {
				LOGGER.error(th.getMessage(), th);
			}
		}

		LOGGER.info("Exiting updateSiteEvent()");
	}

	/**
	 * 
	 * @param outcome
	 */
	private void sendText(String outcome) {
		LOGGER.info("Entering sendText()");

		try {
			final SendText sendText = new SendText();
			sendText.setOAUTH2_TOKEN(accountEvent.getAccesstoken());
			sendText.sendTextWithMessage(event.getTextnumber(), outcome + ": " + accountEvent.getName() + "-" + event.getEventname());
			LOGGER.debug(event.getTextnumber() + " " + outcome + ": " + accountEvent.getName() + "-" + event.getEventname());
		} catch (BatchException be) {
			LOGGER.error(be.getMessage(), be);
		}

		LOGGER.info("Exiting sendText()");
	}

	/**
	 * 
	 * @return
	 */
	private PreviewInput setupPreview() {
		LOGGER.info("Entering setupPreview()");
		final PreviewInput previewInput = new PreviewInput();
		previewInput.setIsmobile(account.getIsmobile());
		previewInput.setPassword(account.getPassword());
		previewInput.setProxyname(account.getProxylocation());
		previewInput.setShowrequestresponse(account.getShowrequestresponse());
		previewInput.setSitetype(account.getSitetype());
		previewInput.setTimezone(account.getTimezone());
		previewInput.setUrl(account.getUrl());
		previewInput.setUsername(account.getUsername());
		previewInput.setAccountid(account.getId());
		previewInput.setSporttype(event.getSport());

		if ("spread".equals(eventType)) {
			previewInput.setLinetype("spread");
			final SpreadRecordEvent spreadEvent = (SpreadRecordEvent) event;
			eventLine = spreadEvent.getSpreadinputfirstone();

			if (eventLine != null && eventLine.length() > 0) {
				eventLineIndicator = spreadEvent.getSpreadplusminusfirstone();
				previewInput.setLineindicator(eventLineIndicator);
			} else {
				eventLineIndicator = spreadEvent.getSpreadplusminussecondone();
				previewInput.setLineindicator(eventLineIndicator);
			}

			previewInput.setRotationid(spreadEvent.getRotationid());
			previewInput.setTeam1(spreadEvent.getEventteam1());
			previewInput.setTeam2(spreadEvent.getEventteam2());
		} else if ("total".equals(eventType)) {
			previewInput.setLinetype("total");
			final TotalRecordEvent totalEvent = (TotalRecordEvent) event;
			eventLine = totalEvent.getTotalinputfirstone();

			if (eventLine != null && eventLine.length() > 0) {
				previewInput.setLineindicator("o");
			} else {
				eventLine = totalEvent.getTotalinputsecondone();
				if (eventLine != null && eventLine.length() > 0) {
					previewInput.setLineindicator("u");
				}
			}

			previewInput.setRotationid(totalEvent.getRotationid());
			previewInput.setTeam1(totalEvent.getEventteam1());
			previewInput.setTeam2(totalEvent.getEventteam2());
		} else if ("ml".equals(eventType)) {
			previewInput.setLinetype("ml");
			final MlRecordEvent mlEvent = (MlRecordEvent) event;
			eventJuiceIndicator = eventLineIndicator = mlEvent.getMlplusminusfirstone();

			if (eventLineIndicator != null && eventLineIndicator.length() > 0) {
				previewInput.setLineindicator(eventLineIndicator);
			} else {
				eventJuiceIndicator = eventLineIndicator = mlEvent.getMlplusminussecondone();
				previewInput.setLineindicator(eventLineIndicator);
			}

			previewInput.setRotationid(mlEvent.getRotationid());
			previewInput.setTeam1(mlEvent.getEventteam1());
			previewInput.setTeam2(mlEvent.getEventteam2());
		}

		LOGGER.debug("previewInput: " + previewInput);
		LOGGER.info("Exiting setupPreview()");
		return previewInput;
	}

	/**
	 * 
	 */
	private void checkSoftwarePackagesActive() {
		try {
			// check for certain software packages
			if ("AGSoftware".equals(account.getSitetype()) || "LineTracker".equals(account.getSitetype())) {
				LOGGER.error("SiteType2: " + account.getSitetype());
				LOGGER.error("UserName2: " + account.getUsername());

				if (SITEACTIVEDB.hasActiveSite(account.getSitetype(), account.getUsername())) {
					LOGGER.error("is active");
					boolean done = false;
					int count = 0;

					// Loop for one minute max
					while (!done && count < 120) {
						try {
							// Try every 1/2 second
							Thread.sleep(500);
							if (!SITEACTIVEDB.hasActiveSite(account.getSitetype(), account.getUsername())) {
								LOGGER.error("not active");
								done = true;
								SITEACTIVEDB.persistSiteActive(account.getSitetype(), account.getUsername());
							} else {
								LOGGER.error("is active");
							}
							count++;
						} catch (InterruptedException ie) {
							done = true;
							SITEACTIVEDB.persistSiteActive(account.getSitetype(), account.getUsername());
						}
					}
				} else {
					LOGGER.error("not active");
					SITEACTIVEDB.persistSiteActive(account.getSitetype(), account.getUsername());
				}
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}		
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
			SITEEVENTSDB.updateAccountEvent(ae);

			// Now check if all the other accounts have been updated and are
			// successful
			if (event instanceof SpreadRecordEvent) {
				final List<AccountEvent> accountEvents = SITEEVENTSDB.getSpreadActiveAccountEvents(event.getId());
				if (accountEvents == null || accountEvents.isEmpty()) {
					event.setIscompleted(true);
					SITEEVENTSDB.updateSpreadEvent((SpreadRecordEvent) event);
				}
			} else if (event instanceof TotalRecordEvent) {
				final List<AccountEvent> accountEvents = SITEEVENTSDB.getTotalActiveAccountEvents(event.getId());
				if (accountEvents == null || accountEvents.isEmpty()) {
					event.setIscompleted(true);
					SITEEVENTSDB.updateTotalEvent((TotalRecordEvent) event);
				}
			} else if (event instanceof MlRecordEvent) {
				final List<AccountEvent> accountEvents = SITEEVENTSDB.getMlActiveAccountEvents(event.getId());
				if (accountEvents == null || accountEvents.isEmpty()) {
					event.setIscompleted(true);
					SITEEVENTSDB.updateMlEvent((MlRecordEvent) event);
				}
			}
		} catch (Throwable t) {
			throw new BatchException(BatchErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,
					BatchErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION, t.getMessage());
		}

		LOGGER.info("Exiting updateAccountInformation()");
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BaseBestPrice [SITEACTIVEDB=" + SITEACTIVEDB + ", SITEEVENTSDB=" + SITEEVENTSDB + ", event=" + event
				+ ", accountEvent=" + accountEvent + ", account=" + account + ", eventType=" + eventType + ", counter="
				+ counter + ", sleepTime=" + sleepTime + ", mobileTextNumber=" + mobileTextNumber
				+ ", sendtextforaccount=" + sendtextforaccount + ", eventLineIndicator=" + eventLineIndicator
				+ ", eventLine=" + eventLine + ", eventJuiceIndicator=" + eventJuiceIndicator + ", eventJuice="
				+ eventJuice + ", done=" + done + ", isDoneWithPrices=" + isDoneWithPrices + ", ep=" + ep
				+ ", bestPriceBuyOrderResource=" + bestPriceBuyOrderResource + "]";
	}
}