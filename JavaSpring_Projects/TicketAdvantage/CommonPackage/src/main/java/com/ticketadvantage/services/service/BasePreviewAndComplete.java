/**
 * 
 */
package com.ticketadvantage.services.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.ticketadvantage.services.db.SiteActiveDB;
import com.ticketadvantage.services.db.SiteEventsDB;
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
public abstract class BasePreviewAndComplete {
	private static final Logger LOGGER = Logger.getLogger(BasePreviewAndComplete.class);
	protected final SiteEventsDB SiteEventsDB = new SiteEventsDB();
	protected final SiteActiveDB SiteActiveDB = new SiteActiveDB();
	protected BaseRecordEvent event;
	protected AccountEvent accountEvent;
	protected Accounts account;
	protected String eventType;
	protected Integer counter;
	protected Integer sleepTime;
	protected String eventLineIndicator = null;
	protected String eventLine = null;
	protected String eventJuiceIndicator = null;
	protected String eventJuice = null;

	/**
	 * 
	 */
	public BasePreviewAndComplete() {
		super();
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
	 * @throws BatchException
	 */
	protected abstract void createLineWatch() throws BatchException;

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
			// Get the DB connections
			SiteEventsDB.start();
			SiteActiveDB.start();

			// check for certain software packages
			checkSoftwarePackagesActive();		
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		try {
			// Setup PreviewInput
			final PreviewInput previewInput = setupPreview();

			// Login and get to menu page
			setupInitialSite(previewInput);

			EventPackage ep = null;
			boolean done = false;

			while (!done && (counter.intValue() > 0)) {
				// Setup the site data
				setupSiteData();

				BacksideOutput previewOutput = null;
				try {
					previewOutput = checkGame(previewInput);
					LOGGER.error("previewOutput: " + previewOutput);
				} catch (Throwable t) {
					LOGGER.error(t.getMessage(), t);
				}

				// Preview output
				if (previewOutput != null) {
					if ("total".equals(eventType)) {
						ep = processTotal(previewOutput);
					} else if ("ml".equals(eventType)) {
						ep = processMl(previewOutput);
					}

					// Are we done?
					if (ep != null) {
						done = true;
					}
				}

				try {
					// Sleep for defined seconds
					Thread.sleep(sleepTime.intValue());
					counter--;
				} catch (InterruptedException ie) {
					LOGGER.warn(ie.getMessage(), ie);
				}
			}

			LOGGER.error("EventPackage: " + ep);
			if (ep != null) {
				// Process the transactions
				if (processAccount(ep)) {
					LOGGER.error("ProcessAccount success!");
	
					// Successful transaction placed; update
					updateAccountInformation(event, accountEvent);
	
					if (event.getTextnumber() != null && event.getTextnumber().length() > 0) {
						sendText("Success");
					}
				} else {
					if (event.getTextnumber() != null && event.getTextnumber().length() > 0) {
						sendText("Fail");
					}
				}

				LOGGER.debug("AccountEvent update: " + accountEvent);
				LOGGER.debug("BaseRecordEvent update: " + event);

				// Update the site event
				updateSiteEvent();
			} else {
				// Create line watch
				createLineWatch();
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
			cleanUpSiteEvent();
		}

		LOGGER.info("Exiting processEvent()");
	}

	/**
	 * 
	 */
	private void cleanUpSiteEvent() {
		LOGGER.info("Entering cleanUpSiteEvent()");
		SiteEventsDB.complete();

		if (account != null) {
			try {
				SiteActiveDB.deleteSiteActive(account.getSitetype(), account.getUsername());
				SiteActiveDB.complete();
			} catch (Throwable th) {
				LOGGER.error(th.getMessage(), th);
			}	
		} else {
			SiteActiveDB.complete();				
		}

		LOGGER.info("Exiting cleanUpSiteEvent()");
	}

	/**
	 * 
	 * @throws SQLException
	 */
	private void updateSiteEvent() throws SQLException {
		LOGGER.info("Entering updateSiteEvent()");

		// Update the events
		SiteEventsDB.updateAccountEvent(accountEvent);

		// Now update the record
		if (event instanceof SpreadRecordEvent) {
			SiteEventsDB.updateSpreadEvent((SpreadRecordEvent) event);
		} else if (event instanceof TotalRecordEvent) {
			SiteEventsDB.updateTotalEvent((TotalRecordEvent) event);
		} else if (event instanceof MlRecordEvent) {
			SiteEventsDB.updateMlEvent((MlRecordEvent) event);
		}

		SiteEventsDB.complete();
		if ("AGSoftware".equals(account.getSitetype()) || "LineTracker".equals(account.getSitetype())) {
			try {
				SiteActiveDB.deleteSiteActive(account.getSitetype(), account.getUsername());
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
			LOGGER.error(event.getTextnumber() + " " + outcome + ": " + accountEvent.getName() + "-" + event.getEventname());
		} catch (BatchException be) {
			LOGGER.error(be.getMessage(), be);
		}

		LOGGER.info("Exiting sendText()");
	}

	/**
	 * 
	 * @param previewOutput
	 * @return
	 */
	private EventPackage processTotal(BacksideOutput previewOutput) {
		LOGGER.info("Entering processTotal()");
		EventPackage ep = null;
		final String line = previewOutput.getLine();
		final String juiceIndicator = previewOutput.getJuiceindicator();
		final String juice = previewOutput.getJuice();

		double tline = Double.parseDouble(line);
		double tline2 = Double.parseDouble(eventLine);
		LOGGER.error("tline: " + tline);
		LOGGER.error("tline2: " + tline2);
		if ("o".equals(eventLineIndicator)) {
			if (tline <= tline2) {
				double eventJuiceNumber = 0;
				if ("+".equals(eventJuiceIndicator)) {
					eventJuiceNumber = Double.parseDouble(eventJuice);
				} else {
					eventJuiceNumber = Double.parseDouble(eventJuiceIndicator + eventJuice);
				}
				if (eventJuiceNumber == 100) {
					eventJuiceNumber = new Double(0);
				}
				LOGGER.error("eventJuiceNumber: " + eventJuiceNumber);

				double juiceNumber = 0;
				if ("+".equals(juiceIndicator)) {
					juiceNumber = Double.parseDouble(juice);
				} else {
					juiceNumber = Double.parseDouble(juiceIndicator + juice);
				}
				if (juiceNumber == 100) {
					juiceNumber = new Double(0);
				}
				LOGGER.error("juiceNumber: " + juiceNumber);

				if (juiceNumber >= eventJuiceNumber) {
					LOGGER.error("Setting EP");
					ep = previewOutput.getEp();
				}
			}
		} else {
			if (tline >= tline2) {
				double eventJuiceNumber = 0;
				if ("+".equals(eventJuiceIndicator)) {
					eventJuiceNumber = Double.parseDouble(eventJuice);
				} else {
					eventJuiceNumber = Double.parseDouble(eventJuiceIndicator + eventJuice);
				}
				if (eventJuiceNumber == 100) {
					eventJuiceNumber = new Double(0);
				}
				LOGGER.error("eventJuiceNumber: " + eventJuiceNumber);

				double juiceNumber = 0;
				if ("+".equals(juiceIndicator)) {
					juiceNumber = Double.parseDouble(juice);
				} else {
					juiceNumber = Double.parseDouble(juiceIndicator + juice);
				}
				if (juiceNumber == 100) {
					juiceNumber = new Double(0);
				}
				LOGGER.error("juiceNumber: " + juiceNumber);

				if (juiceNumber >= eventJuiceNumber) {
					LOGGER.error("Setting EP");
					ep = previewOutput.getEp();
				}
			}
		}

		LOGGER.info("Exiting processTotal()");
		return ep;
	}

	/**
	 * 
	 * @param previewOutput
	 * @return
	 */
	private EventPackage processMl(BacksideOutput previewOutput) {
		LOGGER.info("Entering processMl()");
		EventPackage ep = null;
		final String lineIndicator = previewOutput.getLineindicator();
		final String line = previewOutput.getLine();
		double mline = 0;

		if ("+".equals(lineIndicator)) {
			mline = Double.parseDouble(line);
		} else {
			mline = Double.parseDouble(lineIndicator + line);
		}
		if (mline == 100) {
			mline = new Double(0);
		}
		LOGGER.error("mline: " + mline);
		
		double mline2 = 0;
		if ("+".equals(eventLineIndicator)) {
			mline2 = Double.parseDouble(eventLine);
		} else {
			mline2 = Double.parseDouble(eventLineIndicator + eventLine);
		}
		if (mline2 == 100) {
			mline2 = new Double(0);
		}
		LOGGER.error("mline2: " + mline2);

		if (mline >= mline2) {
			LOGGER.error("Setting EP");
			ep = previewOutput.getEp();
		}

		LOGGER.info("Entering processMl()");
		return ep;
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
		previewInput.setAccountid(accountEvent.getAccountid());
		previewInput.setSporttype("mlblines");

		if (eventType != null && "total".equals(eventType)) {
			previewInput.setLinetype("total");
			final TotalRecordEvent totalEvent = (TotalRecordEvent) event;
			eventLine = totalEvent.getTotalinputfirstone();

			if (eventLine != null && eventLine.length() > 0) {
				previewInput.setLineindicator("o");
				eventLineIndicator = "o";
				eventJuiceIndicator = totalEvent.getTotaljuiceplusminusfirstone();
				eventJuice = totalEvent.getTotalinputjuicefirstone();
			} else {
				eventLine = totalEvent.getTotalinputsecondone();
				if (eventLine != null && eventLine.length() > 0) {
					previewInput.setLineindicator("u");
					eventLineIndicator = "u";
					eventJuiceIndicator = totalEvent.getTotaljuiceplusminussecondone();
					eventJuice = totalEvent.getTotalinputjuicesecondone();
				}
			}
			previewInput.setRotationid(totalEvent.getRotationid());
			previewInput.setTeam1(totalEvent.getEventteam1());
			previewInput.setTeam2(totalEvent.getEventteam2());
		} else if (eventType != null && "ml".equals(eventType)) {
			previewInput.setLinetype("ml");
			final MlRecordEvent mlEvent = (MlRecordEvent) event;
			eventJuiceIndicator = eventLineIndicator = mlEvent.getMlplusminusfirstone();

			if (eventLineIndicator != null && eventLineIndicator.length() > 0) {
				previewInput.setLineindicator(eventLineIndicator);
				eventJuice = eventLine = mlEvent.getMlinputfirstone();
			} else {
				eventJuiceIndicator = eventLineIndicator = mlEvent.getMlplusminussecondone();
				if (eventLineIndicator != null && eventLineIndicator.length() > 0) {
					previewInput.setLineindicator(eventLineIndicator);
					eventJuice = eventLine = mlEvent.getMlinputsecondone();
				}
			}
			previewInput.setRotationid(mlEvent.getRotationid());
			previewInput.setTeam1(mlEvent.getEventteam1());
			previewInput.setTeam2(mlEvent.getEventteam2());
		}

		LOGGER.error("previewInput: " + previewInput);
		LOGGER.error("eventLineIndicator: " + eventLineIndicator);
		LOGGER.error("eventLine: " + eventLine);
		LOGGER.error("eventJuiceIndicator: " + eventJuiceIndicator);
		LOGGER.error("eventJuice: " + eventJuice);

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
				if (SiteActiveDB.hasActiveSite(account.getSitetype(), account.getUsername())) {
					LOGGER.error("is active");
					boolean done = false;
					int count = 0;

					// Loop for one minute max
					while (!done && count < 120) {
						try {
							// Try every 1/2 second
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
			SiteEventsDB.updateAccountEvent(ae);

			// Now check if all the other accounts have been updated and are
			// successful
			if (event instanceof SpreadRecordEvent) {
				final List<AccountEvent> accountEvents = SiteEventsDB.getSpreadActiveAccountEvents(event.getId());
				if (accountEvents == null || accountEvents.isEmpty()) {
					event.setIscompleted(true);
					SiteEventsDB.updateSpreadEvent((SpreadRecordEvent) event);
				}
			} else if (event instanceof TotalRecordEvent) {
				final List<AccountEvent> accountEvents = SiteEventsDB.getTotalActiveAccountEvents(event.getId());
				if (accountEvents == null || accountEvents.isEmpty()) {
					event.setIscompleted(true);
					SiteEventsDB.updateTotalEvent((TotalRecordEvent) event);
				}
			} else if (event instanceof MlRecordEvent) {
				final List<AccountEvent> accountEvents = SiteEventsDB.getMlActiveAccountEvents(event.getId());
				if (accountEvents == null || accountEvents.isEmpty()) {
					event.setIscompleted(true);
					SiteEventsDB.updateMlEvent((MlRecordEvent) event);
				}
			}
		} catch (Exception e) {
			throw new BatchException(BatchErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,
					BatchErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION, e.getMessage());
		}

		LOGGER.info("Exiting updateAccountInformation()");
	}
}