/**
 * 
 */
package com.ticketadvantage.services.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.sites.SiteProcessor;
import com.ticketadvantage.services.db.RecordEventDB;
import com.ticketadvantage.services.db.SiteActiveDosDB;
import com.ticketadvantage.services.db.SiteEventsDosDB;
import com.ticketadvantage.services.email.SendText;
import com.ticketadvantage.services.email.ticketadvantage.TicketAdvantageGmailOath;
import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.AccountEvent;
import com.ticketadvantage.services.model.Accounts;
import com.ticketadvantage.services.model.BaseRecordEvent;
import com.ticketadvantage.services.site.util.AccountSite;

/**
 * @author jmiller
 *
 */
public class BestPriceBuyOrderResource implements BaseBestPriceBuyOrderResource, Runnable {
	private static final Logger LOGGER = Logger.getLogger(BestPriceBuyOrderResource.class);
	private static TicketAdvantageGmailOath TICKETADVANTAGEGMAILOATH = new TicketAdvantageGmailOath();
	private BaseRecordEvent event;
	private List<Accounts> accounts;
	private String maxAmount;
	private Integer orderAmount;
	private Boolean enableretry;
	private String mobileTextNumber;
	private Boolean sendtextforaccount;
	private RecordEventDB RECORDEVENTDB;
	private SiteActiveDosDB SITEACTIVEDB;
	private SiteEventsDosDB SITEEVENTSDB;
	private Boolean wasSuccessful;
	private int successfulAttempts = 0;
	private Boolean humanspeed = new Boolean(false);

//	private BestPriceResource[] bestPriceResources;
	private List<BestPriceBuyOrderResource.ThreadResult> threadResults = new ArrayList<BestPriceBuyOrderResource.ThreadResult>();

	/**
	 * 
	 * @param event
	 * @param accounts
	 * @param maxAmount
	 * @param enableretry
	 * @param mobileTextNumber
	 * @param orderAmount
	 * @param RECORDEVENTDB
	 * @param sendtextforaccount
	 */
	public BestPriceBuyOrderResource(BaseRecordEvent event, 
			List<Accounts> accounts,
			String maxAmount,
			Integer orderAmount,
			Boolean enableretry,
			String mobileTextNumber,
			Boolean sendtextforaccount,
			RecordEventDB RECORDEVENTDB,
			SiteActiveDosDB SITEACTIVEDB,
			SiteEventsDosDB SITEEVENTSDB,
			Boolean humanspeed) {
		super();
		LOGGER.debug("Entering BestPriceBuyOrderResource()");

		try {
			this.event = event;
			this.accounts = accounts;
			this.maxAmount = maxAmount;
			this.orderAmount = orderAmount;
			this.enableretry = enableretry;
			this.mobileTextNumber = mobileTextNumber;
			this.sendtextforaccount = sendtextforaccount;
			this.RECORDEVENTDB = RECORDEVENTDB;
			this.SITEACTIVEDB = SITEACTIVEDB;
			this.SITEEVENTSDB = SITEEVENTSDB;
			this.humanspeed = humanspeed;
			new Thread(this).start();
		} catch (Exception e) {
			LOGGER.error("Exception setting up JAXBContext and Marshaller", e);
		}

		LOGGER.debug("Exiting BestPriceBuyOrderResource()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
		cal.set(2018, Calendar.JUNE, 27, 7, 00, 00);
		LOGGER.error("Now Date: " + cal.getTime());

		final Calendar gameDate = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
		gameDate.set(2018, Calendar.JUNE, 27, 9, 5, 00);
		LOGGER.error("Game Date: " + gameDate.getTime());

		final Calendar game = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
		game.setTime(gameDate.getTime());
		game.add(Calendar.HOUR, Integer.parseInt("-" + "02"));
		game.add(Calendar.MINUTE, Integer.parseInt("-" + "00"));
		LOGGER.error("Game Date After: " + game.getTime());

		// Check if game time is within time frame
		if (cal.after(game)) {
			LOGGER.error("yes");
		}

/*
		final RecordEventDB RECORDEVENTDB = new RecordEventDB();
		final SiteActiveDosDB SITEACTIVEDB = new SiteActiveDosDB();
		final SiteEventsDosDB SITEEVENTSDB = new SiteEventsDosDB();

		final List<Accounts> accounts = new ArrayList<Accounts>();
		try {
			final Accounts account1 = RECORDEVENTDB.getAccount(new Long(7));
			final Accounts account2 = RECORDEVENTDB.getAccount(new Long(8));
			final Accounts account3 = RECORDEVENTDB.getAccount(new Long(9));
			final Accounts account4 = RECORDEVENTDB.getAccount(new Long(10));
			final Accounts account5 = RECORDEVENTDB.getAccount(new Long(11));
			final Accounts account6 = RECORDEVENTDB.getAccount(new Long(12));
			final Accounts account7 = RECORDEVENTDB.getAccount(new Long(13));
			final Accounts account8 = RECORDEVENTDB.getAccount(new Long(14));
			final Accounts account9 = RECORDEVENTDB.getAccount(new Long(15));
			accounts.add(account1);
			accounts.add(account2);
			accounts.add(account3);
			accounts.add(account4);
			accounts.add(account5);
			accounts.add(account6);
			accounts.add(account7);
			accounts.add(account8);
			accounts.add(account9);
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		final MlRecordEvent mre = new MlRecordEvent();
		mre.setAccountid(new Long(-99));
		mre.setAmount("2");
		mre.setAttempts(0);
		mre.setDatecreated(new Date());
		mre.setDatemodified(new Date());
		mre.setMlplusminusfirstone("+");
		mre.setMlinputfirstone("132");
		mre.setId(new Long(1));
		mre.setEventid(new Integer(975));
		mre.setEventid1(new Integer(975));
		mre.setEventid2(new Integer(976));
		mre.setEventname("976 LA ANGELS +132 for Game");
		mre.setEventteam1("LA ANGELS");
		mre.setEventteam2("St. Louis Cardinals");
		mre.setEventtype("ml");
		mre.setGroupid(new Long(-99));
		mre.setScrappername("Iron69-kw2202");
		mre.setSport("mlblines"); 
		mre.setUserid(new Long(6));
		mre.setWtype("1");
		mre.setRotationid(975);

		final SpreadRecordEvent sre = new SpreadRecordEvent();
		sre.setAccountid(new Long(-99));
		sre.setAmount("2");
		sre.setAttempts(0);
		sre.setDatecreated(new Date());
		sre.setDatemodified(new Date());
		sre.setSpreadplusminusfirstone("+");
		sre.setSpreadinputfirstone("1.5");
		sre.setSpreadjuiceplusminusfirstone("-");
		sre.setSpreadinputjuicefirstone("180");
		sre.setId(new Long(1));
		sre.setEventid(new Integer(975));
		sre.setEventid1(new Integer(975));
		sre.setEventid2(new Integer(976));
		sre.setEventname("#929 LA Angeles +1.5 -180 for Game");
		sre.setEventteam1("Chicago Cubs");
		sre.setEventteam2("St. Louis Cardinals");
		sre.setEventtype("spread");
		sre.setGroupid(new Long(-99));
		sre.setScrappername("Iron69-kw2202");
		sre.setSport("mlblines"); 
		sre.setUserid(new Long(6));
		sre.setWtype("1");
		sre.setRotationid(975);

		final TotalRecordEvent tre = new TotalRecordEvent();
		tre.setAccountid(new Long(-99));
		tre.setAmount("2");
		tre.setAttempts(0);
		tre.setDatecreated(new Date());
		tre.setDatemodified(new Date());
		tre.setTotalinputfirstone("10.5");
		tre.setTotaljuiceplusminusfirstone("-");
		tre.setTotalinputjuicefirstone("120");
		tre.setId(new Long(1));
		tre.setEventid(new Integer(975));
		tre.setEventid1(new Integer(975));
		tre.setEventid2(new Integer(976));
		tre.setEventname("#914 Chicago Cubs +115 for Game");
		tre.setEventteam1("Chicago Cubs");
		tre.setEventteam2("St. Louis Cardinals");
		tre.setEventtype("total");
		tre.setGroupid(new Long(-99));
		tre.setScrappername("Iron69-kw2202");
		tre.setSport("mlblines"); 
		tre.setUserid(new Long(6));
		tre.setWtype("1");
		tre.setRotationid(975);

		new BestPriceBuyOrderResource(tre, 
				accounts,
				"1",
				2,
				false,
				"",
				false,
				RECORDEVENTDB,
				SITEACTIVEDB,
				SITEEVENTSDB);

		while (true) {
			try {
				Thread.sleep(1000);
			} catch (Throwable t) {
				
			}
		}
*/
	}

	/**
	 * 
	 * @author jmiller
	 *
	 */
	private class ThreadResult {
		public Long accountEventId;
		public Boolean betPriceResoureStatus;

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "ThreadResult [accountEventId=" + accountEventId + ", betPriceResoureStatus=" + betPriceResoureStatus
					+ "]";
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.service.BaseBestPriceBuyOrderResource#getEvent()
	 */
	@Override
	public BaseRecordEvent getEvent() {
		return event;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.service.BaseBestPriceBuyOrderResource#setEvent(com.ticketadvantage.services.model.BaseRecordEvent)
	 */
	@Override
	public void setEvent(BaseRecordEvent event) {
		this.event = event;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.service.BaseBestPriceBuyOrderResource#getAccounts()
	 */
	@Override
	public List<Accounts> getAccounts() {
		return accounts;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.service.BaseBestPriceBuyOrderResource#setAccounts(java.util.Set)
	 */
	@Override
	public void setAccounts(Set<Accounts> setAccounts) {
		this.accounts = new ArrayList<Accounts>(setAccounts);
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.service.BaseBestPriceBuyOrderResource#getMaxAmount()
	 */
	@Override
	public String getMaxAmount() {
		return maxAmount;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.service.BaseBestPriceBuyOrderResource#setMaxAmount(java.lang.String)
	 */
	@Override
	public void setMaxAmount(String maxAmount) {
		this.maxAmount = maxAmount;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.service.BaseBestPriceBuyOrderResource#getEnableretry()
	 */
	@Override
	public Boolean getEnableretry() {
		return enableretry;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.service.BaseBestPriceBuyOrderResource#setEnableretry(java.lang.Boolean)
	 */
	@Override
	public void setEnableretry(Boolean enableretry) {
		this.enableretry = enableretry;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.service.BaseBestPriceBuyOrderResource#getMobileTextNumber()
	 */
	@Override
	public String getMobileTextNumber() {
		return mobileTextNumber;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.service.BaseBestPriceBuyOrderResource#setMobileTextNumber(java.lang.String)
	 */
	@Override
	public void setMobileTextNumber(String mobileTextNumber) {
		this.mobileTextNumber = mobileTextNumber;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.service.BaseBestPriceBuyOrderResource#getOrderAmount()
	 */
	@Override
	public Integer getOrderAmount() {
		return orderAmount;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.service.BaseBestPriceBuyOrderResource#setOrderAmount(java.lang.Integer)
	 */
	@Override
	public void setOrderAmount(Integer orderAmount) {
		this.orderAmount = orderAmount;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.service.BaseBestPriceBuyOrderResource#getRECORDEVENTDB()
	 */
	@Override
	public RecordEventDB getRECORDEVENTDB() {
		return RECORDEVENTDB;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.service.BaseBestPriceBuyOrderResource#setRECORDEVENTDB(com.ticketadvantage.services.db.RecordEventDB)
	 */
	@Override
	public void setRECORDEVENTDB(RecordEventDB rECORDEVENTDB) {
		RECORDEVENTDB = rECORDEVENTDB;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.service.BaseBestPriceBuyOrderResource#getSendtextforaccount()
	 */
	@Override
	public Boolean getSendtextforaccount() {
		return sendtextforaccount;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.service.BaseBestPriceBuyOrderResource#setSendtextforaccount(java.lang.Boolean)
	 */
	@Override
	public void setSendtextforaccount(Boolean sendtextforaccount) {
		this.sendtextforaccount = sendtextforaccount;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.service.BaseBestPriceBuyOrderResource#getWasSuccessful()
	 */
	@Override
	public Boolean getWasSuccessful() {
		return wasSuccessful;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.service.BaseBestPriceBuyOrderResource#setWasSuccessful(java.lang.Boolean)
	 */
	@Override
	public void setWasSuccessful(Boolean wasSuccessful) {
		this.wasSuccessful = wasSuccessful;
	}

	/**
	 * @return the humanspeed
	 */
	public Boolean getHumanspeed() {
		return humanspeed;
	}

	/**
	 * @param humanspeed the humanspeed to set
	 */
	public void setHumanspeed(Boolean humanspeed) {
		this.humanspeed = humanspeed;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.service.BaseBestPriceBuyOrderResource#bestPriceResourceDone(java.lang.Boolean, java.lang.Long)
	 */
	@Override
	public synchronized void bestPriceResourceDone(Boolean wasSuccessful, Long accountId) {
		LOGGER.info("Entering bestPriceResourceDone()");
		final BestPriceBuyOrderResource.ThreadResult threadResult = new BestPriceBuyOrderResource.ThreadResult();
		threadResult.betPriceResoureStatus = wasSuccessful;
		threadResult.accountEventId = accountId;
		LOGGER.debug("threadResult: " + threadResult);
		threadResults.add(threadResult);	
		
/*		for (BestPriceResource bpr : bestPriceResources) {
			final BestPriceBuyOrderResource.ThreadResult threadResult = new BestPriceBuyOrderResource.ThreadResult();
			threadResult.betPriceResoureStatus = wasSuccessful;
			threadResult.accountEventId = accountId;
			LOGGER.debug("threadResult: " + threadResult);
			final Accounts accts = bpr.getAccount();
			if (accts.getId() != null && accts.getId().longValue() == accountId.longValue()) {
				threadResults.add(threadResult);				
			} else {
				bpr.setDone(true);
			}
		}
*/
		LOGGER.info("Exiting bestPriceResourceDone()");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		LOGGER.info("Entering run()");
		int numAccountsNeeded = 0;

		try {
			// Check to make sure the account list is not empty
			if (accounts != null && !accounts.isEmpty()) {
				// How many accounts do we need to push in to?
				numAccountsNeeded = determineAccountsNeeded();
				LOGGER.debug("numAccountsNeeded: " + numAccountsNeeded);

				// Setup the best prices threads
				final List<BestPriceResource> allResources = setupBestPriceResources();

				// Wait for the sites to finish
				waitForSites(allResources);

				// Check best prices
				BestPriceResource[] bestPriceResources = checkBestPrices(numAccountsNeeded, allResources);

				if (bestPriceResources != null && bestPriceResources.length > 0) {
					// Now place the transaction
					for (BestPriceResource bestPriceResource : bestPriceResources) {
						LOGGER.debug("BestPriceResource: " + bestPriceResource.getAccount().getName());
						final AccountEvent accountEvent = populateAccountEvent(event, bestPriceResource.getAccount(), false);
						this.RECORDEVENTDB.setupAccountEvent(accountEvent);
						bestPriceResource.setAccountEvent(accountEvent);
						bestPriceResource.setDone(true);

//						if (this.SITEACTIVEDB.hasActiveSite(bestPriceResource.getAccount().getSitetype(), bestPriceResource.getAccount().getUsername())) {
//							this.SITEACTIVEDB.deleteSiteActive(bestPriceResource.getAccount().getSitetype(), bestPriceResource.getAccount().getUsername());
//						}
					}

					// Process until complete or run out of accounts
					processAccounts(numAccountsNeeded, bestPriceResources, allResources);
				}
			}

			if (numAccountsNeeded != 0 && numAccountsNeeded == successfulAttempts) {
				try {
					if ("spread".equals(event.getEventtype())) {
						RECORDEVENTDB.setCompleteSpreadEvent(event.getId());
					} else if ("total".equals(event.getEventtype())) {
						RECORDEVENTDB.setCompleteTotalEvent(event.getId());
					} else if ("ml".equals(event.getEventtype())) {
						RECORDEVENTDB.setCompleteMlEvent(event.getId());
					}
				} catch (Throwable t) {
					LOGGER.warn(t.getMessage(), t);
				}

				if (sendtextforaccount != null && sendtextforaccount.booleanValue()) {
					sendText("Success");
				}
			} else {
				sendText("Fail");
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);

			// Send out an error messsage
			try {
				if (numAccountsNeeded != 0 && numAccountsNeeded == successfulAttempts) {
					try {
						if ("spread".equals(event.getEventtype())) {
							RECORDEVENTDB.setCompleteSpreadEvent(event.getId());
						} else if ("total".equals(event.getEventtype())) {
							RECORDEVENTDB.setCompleteTotalEvent(event.getId());
						} else if ("ml".equals(event.getEventtype())) {
							RECORDEVENTDB.setCompleteMlEvent(event.getId());
						}
					} catch (Throwable th) {
						LOGGER.warn(th.getMessage(), th);
					}

					if (sendtextforaccount != null && sendtextforaccount.booleanValue()) {
						sendText("Success");
					}
				} else {
					sendText("Fail");
				}
			} catch (Throwable be) {
				LOGGER.error(be.getMessage(), be);
			}
		} 

		LOGGER.info("Exiting run()");
	}

	/**
	 * 
	 * @return
	 */
	protected int determineAccountsNeeded() {
		LOGGER.info("Entering determineAccountsNeeded()");
		int numAccountsNeeded = 0;

		try {
			// Setup the amounts
			LOGGER.error("maxAmount: " + maxAmount);
			Integer maxAccountAmount = Integer.parseInt(maxAmount);
			LOGGER.debug("maxAccountAmount: " + maxAccountAmount);
			LOGGER.debug("orderAmount: " + orderAmount);
	
			// How many accounts do we need to push in to?
			numAccountsNeeded = orderAmount / maxAccountAmount;
			LOGGER.debug("numAccountsNeeded: " + numAccountsNeeded);
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		LOGGER.info("Entering determineAccountsNeeded()");
		return numAccountsNeeded;
	}

	/**
	 * 
	 * @return
	 * @throws BatchException
	 */
	protected List<BestPriceResource> setupBestPriceResources() throws BatchException {
		LOGGER.info("Entering setupBestPriceResources()");
		final List<BestPriceResource> allResources = new ArrayList<BestPriceResource>();

		// Randomly redo the account list
		Collections.shuffle(accounts);
		Arrays.toString(accounts.toArray()); 

		for (Accounts account : accounts) {
			final String hourafter = account.getHourafter();
			final String minuteafter = account.getMinuteafter();
			final String hourbefore = account.getHourbefore();
			final String minutebefore = account.getMinutebefore();

			if (hourafter.equals("00") && hourbefore.equals("00")) {
				account.setShowrequestresponse(true);
				LOGGER.error("Account: " + account);
	
				final SiteProcessor siteProcessor = AccountSite.GetAccountSite(account);
				siteProcessor.setHumanspeed(humanspeed);

				// Start the best price
				final BestPriceResource bestPriceResource = new BestPriceResource(event, 
						null, 
						account, 
						event.getEventtype(), 
						30, 
						1000, 
						mobileTextNumber, 
						sendtextforaccount, 
						SITEACTIVEDB,
						SITEEVENTSDB,
						siteProcessor);
				bestPriceResource.setBestPriceBuyOrderResource(this);
				bestPriceResource.startProcessing();
				
				// Add to the list
				allResources.add(bestPriceResource);
			} else if (!hourbefore.equals("00")) {
				final String timezone = account.getTimezone();
				Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
				Calendar game = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));

				if ("ET".equals(timezone)) {
					cal = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
					game = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
				} else if ("CT".equals(timezone)) {
					cal = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
					game = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));					
				} else if ("MT".equals(timezone)) {
					cal = Calendar.getInstance(TimeZone.getTimeZone("America/Denver"));
					game = Calendar.getInstance(TimeZone.getTimeZone("America/Denver"));					
				} else if ("PT".equals(timezone)) {
					cal = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
					game = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
				}

				final Date gameDate = event.getEventdatetime();
				game.setTime(gameDate);
				game.add(Calendar.HOUR, Integer.parseInt("-" + hourbefore));
				game.add(Calendar.MINUTE, Integer.parseInt("-" + minutebefore));

				// Check if game time is within time frame
				if (cal.after(game)) {
					account.setShowrequestresponse(true);
					LOGGER.error("Account: " + account);

					final SiteProcessor siteProcessor = AccountSite.GetAccountSite(account);
					siteProcessor.setHumanspeed(humanspeed);

					// Start the best price
					final BestPriceResource bestPriceResource = new BestPriceResource(event, 
							null, 
							account, 
							event.getEventtype(), 
							30, 
							1000, 
							mobileTextNumber, 
							sendtextforaccount, 
							SITEACTIVEDB,
							SITEEVENTSDB,
							siteProcessor);
					bestPriceResource.setBestPriceBuyOrderResource(this);
					bestPriceResource.startProcessing();
					
					// Add to the list
					allResources.add(bestPriceResource);					
				}
			}
		}

		LOGGER.info("Exiting setupBestPriceResources()");
		return allResources;
	}

	/**
	 * 
	 * @param futures
	 */
	protected void waitForSites(List<BestPriceResource> allResources) {
		LOGGER.info("Entering waitForSites()");
		int waiterCounter = 60;
		boolean isDone = false;

		while (!isDone && waiterCounter != 0) {
			isDone = true;

			// Loop through the resources
			for (BestPriceResource bestPriceResource : allResources) {
				if (bestPriceResource.getIsDoneWithPrices() != null && !bestPriceResource.getIsDoneWithPrices().booleanValue()) {
					isDone = false;
				}
			}

			try {
				// Sleep for 1 second
				Thread.sleep(1000);
				waiterCounter--;
			} catch (InterruptedException ie) {
				LOGGER.warn(ie.getMessage(), ie);
			}
		}

		LOGGER.info("Exiting waitForSites()");
	}

	/**
	 * 
	 * @param numAccountsNeeded
	 * @param allThreads
	 * @return
	 */
	protected BestPriceResource[] checkBestPrices(int numAccountsNeeded, List<BestPriceResource> allResources) {
		LOGGER.info("Entering checkBestPrices()");

		// First set which sites will be tried first
		BestPriceResource[] bestPriceResources = setupBestPrices(numAccountsNeeded, allResources);

		// Remove from main list and see if there are any empties in the list
		boolean emptyResource = removeSelectedResource(bestPriceResources, allResources);

		// Redo the list if there are any empty resources
		bestPriceResources = redoListForEmptyResource(emptyResource, bestPriceResources);

		LOGGER.info("Exiting checkBestPrices()");
		return bestPriceResources;
	}

	/**
	 * 
	 * @param numAccountsNeeded
	 * @param allResources
	 * @return
	 */
	protected BestPriceResource[] setupBestPrices(int numAccountsNeeded, List<BestPriceResource> allResources) {
		LOGGER.info("Entering setupBestPrices()");
		BestPriceResource[] bestPriceResources = null;

		// Check see to see how many we need/have
		if (allResources.size() >= numAccountsNeeded) {
			bestPriceResources = new BestPriceResource[numAccountsNeeded];
		} else {
			bestPriceResources = new BestPriceResource[allResources.size()];
		}

		// Loop through all threads
		int acctcounter = 0;
		final Iterator<BestPriceResource> itr = allResources.iterator();

		while (itr.hasNext()) {
			final BestPriceResource bestPrice = itr.next();
			LOGGER.error("BestPriceResource: " + bestPrice.getAccount().getName());

			if (acctcounter < numAccountsNeeded) {
				LOGGER.debug("acctcounter: " + acctcounter);
				LOGGER.debug("numAccountsNeeded: " + numAccountsNeeded);
				LOGGER.debug("bestPriceResources.length: " + bestPriceResources.length);

				// do we have what we need yet?
				if (acctcounter < bestPriceResources.length) {
					if (bestPrice.getEventLine() != null && 
						bestPrice.getEventLine().length() > 0 && 
						bestPrice.getEventLineIndicator() != null && 
						bestPrice.getEventLineIndicator().length() > 0) {

						// Check if we already have one
						if (acctcounter > 0) {
							boolean found = false;
							for (int z = 0; z < bestPriceResources.length; z++) {
								if (bestPriceResources[z] != null && 
									bestPriceResources[z].getAccount() != null && 
									bestPriceResources[z].getAccount().getId().longValue() == bestPrice.getAccount().getId().longValue()) {
									found = true;
								}
							}

							// Did we find it already in the list, if no then set it
							if (!found) {
								bestPriceResources[acctcounter] = bestPrice;
							}
						} else {
							LOGGER.debug("bestPrice: " + bestPrice.getAccount().getName());
							bestPriceResources[acctcounter] = bestPrice;
						}

						acctcounter++;
					} else {
						try {
							// We need to store a failure here
							final AccountEvent accountEvent = populateFailedAccountEvent(event, bestPrice);
							this.RECORDEVENTDB.setupAccountEvent(accountEvent);
							if (this.SITEACTIVEDB.hasActiveSite(bestPrice.getAccount().getSitetype(), bestPrice.getAccount().getUsername())) {
								this.SITEACTIVEDB.deleteSiteActive(bestPrice.getAccount().getSitetype(), bestPrice.getAccount().getUsername());
							}
						} catch (Throwable t) {
							LOGGER.error(t.getMessage(), t);
						}

						bestPrice.setEp(null);
						bestPrice.setDone(true);
						itr.remove();
					}
				}
			} else {
				final String eventType = bestPrice.getEventType();
				final String lineIndicator = bestPrice.getEventLineIndicator();
				LOGGER.debug("lineIndicator: " + lineIndicator);

				if ("spread".equals(eventType)) {
					if (lineIndicator != null && "-".equals(lineIndicator)) {
						checkSpread(true, bestPriceResources, bestPrice);
					} else if (lineIndicator != null && lineIndicator.length() > 0) {
						checkSpread(false, bestPriceResources, bestPrice);
					}
				} else if ("total".equals(eventType)) {
					if (lineIndicator != null && "o".equals(lineIndicator)) {
						checkTotal(true, bestPriceResources, bestPrice);
					} else if (lineIndicator != null && lineIndicator.length() > 0) {
						checkTotal(false, bestPriceResources, bestPrice);
					}
				} else if ("ml".equals(eventType)) {
					if (lineIndicator != null && "-".equals(lineIndicator)) {
						checkMl(true, bestPriceResources, bestPrice);
					} else if (lineIndicator != null && lineIndicator.length() > 0) {
						checkMl(false, bestPriceResources, bestPrice);
					}
				}
			}
		}

		LOGGER.info("Exiting setupBestPrices()");
		return bestPriceResources;
	}

	/**
	 * 
	 * @param bestPriceResources
	 * @param allResources
	 */
	protected boolean removeSelectedResource(BestPriceResource[] bestPriceResources, List<BestPriceResource> allResources) {
		LOGGER.info("Entering removeSelectedResource()");
		boolean emptyResource = false;

		// Remove the one's just added from the list
		for (int x = 0; x < bestPriceResources.length; x++) {
			if (bestPriceResources[x] != null) {
				LOGGER.debug("bestPriceResources[" + x + "]: " + bestPriceResources[x].getAccount().getName());
				final BestPriceResource bestPrice = bestPriceResources[x];
				final Iterator<BestPriceResource> itr2 = allResources.iterator();
	
				// loop through list
				while (itr2.hasNext()) {
					final BestPriceResource bestPriceItr = itr2.next();
					final Accounts account = bestPrice.getAccount();
					final Accounts account2 = bestPriceItr.getAccount();
	
					// If they match remove from main resource list
					if (account.getId().longValue() == account2.getId().longValue()) {
						itr2.remove();
					}
				}
			} else {
				emptyResource = true;
			}
		}

		LOGGER.info("Exiting removeSelectedResource()");
		return emptyResource;
	}

	/**
	 * 
	 * @param emptyResource
	 * @param bestPriceResources
	 * @return
	 */
	protected BestPriceResource[] redoListForEmptyResource(boolean emptyResource, BestPriceResource[] bestPriceResources) {
		LOGGER.info("Entering redoListForEmptyResource()");

		// Check if we have an empty and if so, reshuffle list
		if (emptyResource) {
			int numbad = 0;
			for (int x = 0; x < bestPriceResources.length; x++) {
				if (bestPriceResources[x] == null) {
					numbad++;
				}
			}

			// Is the number of bed resources the same as the resource list?
			if (numbad == bestPriceResources.length) {
				bestPriceResources = null;
			} else {
				final BestPriceResource[] bestPriceResourcesTemp = new BestPriceResource[bestPriceResources.length - numbad];
				int y = 0;
				for (int x = 0; x < bestPriceResources.length; x++) {
					if (bestPriceResources[x] != null) {
						bestPriceResourcesTemp[y++] = bestPriceResources[x];
					}
				}
				bestPriceResources = bestPriceResourcesTemp;
			}
		}

		LOGGER.info("Exiting redoListForEmptyResource()");
		return bestPriceResources;
	}

	/**
	 * 
	 * @param isNegative
	 * @param bestPriceResources
	 * @param bestPrice
	 */
	protected void checkSpread(boolean isNegative, BestPriceResource[] bestPriceResources, BestPriceResource bestPrice) {
		LOGGER.info("Entering checkSpread()");
		LOGGER.debug("isNegative: " + isNegative);

		final String line = bestPrice.getEventLine();
		LOGGER.debug("line: " + line);

		if (line != null && line.length() > 0) {
			float value = 0;

			if (isNegative) {
				value = Float.parseFloat("-" + line);
			} else {
				value = Float.parseFloat(line);
			}
			LOGGER.debug("value: " + value);

			for (int x = 0; x < bestPriceResources.length; x++) {
				if (bestPriceResources[x] != null) {
					final String lineIndicatorTemp = bestPriceResources[x].getEventLineIndicator();
					final String lineTemp = bestPriceResources[x].getEventLine();
					LOGGER.debug("lineIndicatorTemp: " + lineIndicatorTemp);
					LOGGER.debug("lineTemp: " + lineTemp);
	
					if (lineIndicatorTemp != null && lineIndicatorTemp.length() > 0 &&
						lineTemp != null && lineTemp.length() > 0) {
						float valueTemp = 0;
	
						if ("-".equals(lineIndicatorTemp)) {
							valueTemp = Float.parseFloat("-" + lineTemp);
						} else {
							valueTemp = Float.parseFloat(lineTemp);
						}
						LOGGER.debug("valueTemp: " + valueTemp);
	
						if (valueTemp < value) {
							boolean found = false;
							for (int z = 0; z < bestPriceResources.length; z++) {
								LOGGER.debug("bestPriceResources[" + z + "]: " + bestPriceResources[z].getAccount().getName());
								LOGGER.debug("bestPrice: " + bestPrice.getAccount().getName());
								if (bestPriceResources[z].getAccount().getId().longValue() == bestPrice.getAccount().getId().longValue()) {
									found = true;
								}
							}
	
							if (!found) {
								bestPriceResources[x] = bestPrice;
							}
						} else if (value == valueTemp) {
							boolean isBetter = checkJuice(bestPriceResources[x], bestPrice);
							LOGGER.debug("isBetter: " + isBetter);
	
							if (isBetter) {
								boolean found = false;
								for (int z = 0; z < bestPriceResources.length; z++) {
									LOGGER.debug("bestPriceResources[" + z + "]: " + bestPriceResources[z].getAccount().getName());
									LOGGER.debug("bestPrice: " + bestPrice.getAccount().getName());
									if (bestPriceResources[z].getAccount().getId().longValue() == bestPrice.getAccount().getId().longValue()) {
										found = true;
									}
								}
	
								if (!found) {
									bestPriceResources[x] = bestPrice;
								}
							}
						}
					} else {
						boolean found = false;
						for (int z = 0; z < bestPriceResources.length; z++) {
							LOGGER.debug("bestPriceResources[" + z + "]: " + bestPriceResources[z].getAccount().getName());
							LOGGER.debug("bestPrice: " + bestPrice.getAccount().getName());
							if (bestPriceResources[z].getAccount().getId().longValue() == bestPrice.getAccount().getId().longValue()) {
								found = true;
							}
						}
	
						if (!found) {
							bestPriceResources[x] = bestPrice;
						}
					}
				}
			}
		}

		LOGGER.info("Exiting checkSpread()");
	}

	/**
	 * 
	 * @param isOver
	 * @param bestPriceResources
	 * @param bestPrice
	 */
	protected void checkTotal(boolean isOver, BestPriceResource[] bestPriceResources, BestPriceResource bestPrice) {
		LOGGER.info("Entering checkTotal()");
		LOGGER.debug("isOver: " + isOver);
		final String line = bestPrice.getEventLine();
		LOGGER.debug("line: " + line);
		
		if (line != null && line.length() > 0) {
			float value = Float.parseFloat(line);
			LOGGER.debug("value: " + value);
	
			for (int x = 0; x < bestPriceResources.length; x++) {
				if (bestPriceResources[x] != null) {
					final String tempLine = bestPriceResources[x].getEventLine();
					LOGGER.debug("tempLine: " + tempLine);
	
					if (tempLine != null && tempLine.length() > 0) {
						float valueTemp = Float.parseFloat(tempLine);
						LOGGER.debug("valueTemp: " + valueTemp);
			
						if (isOver && (value < valueTemp)) {
							boolean found = false;
							for (int z = 0; z < bestPriceResources.length; z++) {
								LOGGER.debug("bestPriceResources[" + z + "]: " + bestPriceResources[z].getAccount().getName());
								LOGGER.debug("bestPrice: " + bestPrice.getAccount().getName());
								if (bestPriceResources[z].getAccount().getId().longValue() == bestPrice.getAccount().getId().longValue()) {
									found = true;
								}
							}
	
							if (!found) {
								bestPriceResources[x] = bestPrice;
							}
						} else if (!isOver && (value > valueTemp)) {
							boolean found = false;
							for (int z = 0; z < bestPriceResources.length; z++) {
								LOGGER.debug("bestPriceResources[" + z + "]: " + bestPriceResources[z].getAccount().getName());
								LOGGER.debug("bestPrice: " + bestPrice.getAccount().getName());
								if (bestPriceResources[z].getAccount().getId().longValue() == bestPrice.getAccount().getId().longValue()) {
									found = true;
								}
							}
	
							if (!found) {
								bestPriceResources[x] = bestPrice;
							}
						} else if (value == valueTemp) {
							boolean isBetter = checkJuice(bestPriceResources[x], bestPrice);
							LOGGER.debug("isBetter: " + isBetter);
			
							if (isBetter) {
								boolean found = false;
								for (int z = 0; z < bestPriceResources.length; z++) {
									LOGGER.debug("bestPriceResources[" + z + "]: " + bestPriceResources[z].getAccount().getName());
									LOGGER.debug("bestPrice: " + bestPrice.getAccount().getName());
									if (bestPriceResources[z].getAccount().getId().longValue() == bestPrice.getAccount().getId().longValue()) {
										found = true;
									}
								}
	
								if (!found) {
									bestPriceResources[x] = bestPrice;
								}
							}
						}
					} else {
						boolean found = false;
						for (int z = 0; z < bestPriceResources.length; z++) {
							LOGGER.debug("bestPriceResources[" + z + "]: " + bestPriceResources[z].getAccount().getName());
							LOGGER.debug("bestPrice: " + bestPrice.getAccount().getName());
							if (bestPriceResources[z].getAccount().getId().longValue() == bestPrice.getAccount().getId().longValue()) {
								found = true;
							}
						}
	
						if (!found) {
							bestPriceResources[x] = bestPrice;
						}
					}
				}
			}
		}

		LOGGER.info("Exiting checkTotal()");
	}

	/**
	 * 
	 * @param isNegative
	 * @param bestPriceResources
	 * @param bestPrice
	 */
	protected void checkMl(boolean isNegative, BestPriceResource[] bestPriceResources, BestPriceResource bestPrice) {
		LOGGER.info("Entering checkMl()");
		final String line = bestPrice.getEventLine();
		LOGGER.debug("line: " + line);

		if (line != null && line.length() > 0) {
			float value = 0;

			if (isNegative) {
				value = Float.parseFloat("-" + line);
			} else {
				value = Float.parseFloat(line);
			}
			LOGGER.debug("value: " + value);

			for (int x = 0; x < bestPriceResources.length; x++) {
				if (bestPriceResources[x] != null) {
					final String lineIndicatorTemp = bestPriceResources[x].getEventLineIndicator();
					final String lineTemp = bestPriceResources[x].getEventLine();
					LOGGER.debug("lineIndicatorTemp: " + lineIndicatorTemp);
					LOGGER.debug("lineTemp: " + lineTemp);
	
					if (lineIndicatorTemp != null && lineIndicatorTemp.length() > 0 &&
						lineTemp != null && lineTemp.length() > 0) {
						float valueTemp = 0;
	
						if ("-".equals(lineIndicatorTemp)) {
							valueTemp = Float.parseFloat("-" + lineTemp);
						} else {
							valueTemp = Float.parseFloat(lineTemp);
						}
						LOGGER.debug("valueTemp: " + valueTemp);
						LOGGER.debug("bestPriceResources[" + x + "]: " + bestPriceResources[x].getAccount().getName());
						LOGGER.debug("bestPrice: " + bestPrice.getAccount().getName());
							
						if (valueTemp < value) {
							boolean found = false;
							for (int z = 0; z < bestPriceResources.length; z++) {
								LOGGER.debug("bestPriceResources[" + z + "]: " + bestPriceResources[z].getAccount().getName());
								LOGGER.debug("bestPrice: " + bestPrice.getAccount().getName());
								if (bestPriceResources[z].getAccount().getId().longValue() == bestPrice.getAccount().getId().longValue()) {
									found = true;
								}
							}
	
							if (!found) {
								bestPriceResources[x] = bestPrice;
							}
						}
					} else {
						// Means the one in the list does not have valid data so discard
						boolean found = false;
						for (int z = 0; z < bestPriceResources.length; z++) {
							LOGGER.debug("bestPriceResources[" + z + "]: " + bestPriceResources[z].getAccount().getName());
							LOGGER.debug("bestPrice: " + bestPrice.getAccount().getName());
							if (bestPriceResources[z].getAccount().getId().longValue() == bestPrice.getAccount().getId().longValue()) {
								found = true;
							}
						}
	
						if (!found) {
							bestPriceResources[x] = bestPrice;
						}
					}
				}
			}
		}

		LOGGER.info("Exiting checkMl()");
	}

	/**
	 * 
	 * @param bestPriceResource
	 * @param bestPrice
	 * @return
	 */
	protected boolean checkJuice(BestPriceResource bestPriceResource, BestPriceResource bestPrice) {
		LOGGER.info("Entering checkJuice()");
		boolean isBetter = false;
		final String juiceIndicator = bestPrice.getEventJuiceIndicator();
		final String juice = bestPrice.getEventJuice();
		final String juiceIndicatorTemp = bestPriceResource.getEventJuiceIndicator();
		final String juiceTemp = bestPriceResource.getEventJuice();

		if (juiceIndicator != null && juiceIndicator.length() > 0 &&
			juice != null && juice.length() > 0 &&
			juiceIndicatorTemp != null && juiceIndicatorTemp.length() > 0 &&
			juiceTemp != null && juiceTemp.length() > 0) {
			float valueJuice = 0;
			if ("-".equals(juiceIndicator)) {
				valueJuice = Float.parseFloat("-" + juice);
			} else {
				valueJuice = Float.parseFloat(juice);
			}
	
			float valueJuiceTemp = 0;
			if ("-".equals(juiceIndicatorTemp)) {
				valueJuiceTemp = Float.parseFloat("-" + juiceTemp);
			} else {
				valueJuiceTemp = Float.parseFloat(juiceTemp);
			}
	
			if (valueJuiceTemp < valueJuice) {
				isBetter = true;
			}
		}

		LOGGER.info("Exiting checkJuice()");
		return isBetter;
	}

	/**
	 * 
	 * @param event
	 * @param account
	 * @param enableRetry
	 * @return
	 */
	protected AccountEvent populateAccountEvent(BaseRecordEvent event, Accounts account, Boolean enableRetry) {
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
		accountEvent.setHumanspeed(this.humanspeed);

		// Check if we should retry on failed attempts
		if (enableRetry) {
			accountEvent.setAttempts(new Integer(11));
		}

		LOGGER.info("Exiting populateAccountEvent()");
		return accountEvent;
	}

	/**
	 * 
	 * @param event
	 * @param account
	 * @param enableRetry
	 * @return
	 */
	protected AccountEvent populateFailedAccountEvent(BaseRecordEvent event, BestPriceResource bestPrice) {
		LOGGER.info("Entering populateFailedAccountEvent()");
		final AccountEvent accountEvent = new AccountEvent();
		final Accounts account = bestPrice.getAccount();

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
		accountEvent.setStatus("Error");
		accountEvent.setTimezone(account.getTimezone());
		accountEvent.setType(event.getEventtype());
		accountEvent.setWagertype(event.getWtype());
		accountEvent.setUserid(event.getUserid());
		accountEvent.setAttempts(new Integer(0));
		accountEvent.setHumanspeed(this.humanspeed);

		// check if we don't have a valid event line
		if (bestPrice.getEventLine() == null || bestPrice.getEventLine().length() == 0) {
			accountEvent.setErrorexception("Cannot retrieve a line for " + bestPrice.getEventType());
			accountEvent.setErrormessage("No line for " + bestPrice.getEventType());
			accountEvent.setErrorcode(BatchErrorCodes.GAME_NOT_AVAILABLE);
		} else if (bestPrice.getEp() == null) {
			accountEvent.setErrorexception("Error getting game information for " + bestPrice.getEventType());
			accountEvent.setErrormessage(BatchErrorMessage.GAME_NOT_AVAILABLE);
			accountEvent.setErrorcode(BatchErrorCodes.GAME_NOT_AVAILABLE);			
		} else {
			accountEvent.setErrorexception("Unknown error has occured for " + bestPrice.getEventType());
			accountEvent.setErrormessage(BatchErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
			accountEvent.setErrorcode(BatchErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION);			
		}

		LOGGER.info("Exiting populateFailedAccountEvent()");
		return accountEvent;
	}

	/**
	 * 
	 * @param numAccountsNeeded
	 * @param bestPriceResources
	 * @param allResources
	 */
	protected void processAccounts(int numAccountsNeeded, BestPriceResource[] bestPriceResources, List<BestPriceResource> allResources) {
		boolean isComplete = false;
		int counter = 120;

		// Now wait for
		while (!isComplete && counter != 0) {
			try {
				boolean allSuccess = true;
				LOGGER.debug("threadResults.size(): " + threadResults.size());
				LOGGER.debug("bestPriceResources.length: " + bestPriceResources.length);

				if ((threadResults.size() == bestPriceResources.length) && 
					(threadResults.size() != 0 && bestPriceResources.length != 0)) {
					int failed = checkForFailedTransaction();
					LOGGER.error("failed: " + failed);

					if (failed > 0) {
						allSuccess = false;
					}
					LOGGER.debug("allSuccess: " + allSuccess);
					threadResults.clear();

					if (!allSuccess) {
						// How many were unsuccessful?
						bestPriceResources = checkBestPrices(failed, allResources);

						// Now place the transaction
						if (bestPriceResources == null || bestPriceResources.length == 0) {
							isComplete = true;
							counter = 0;
						} else {
							processTransaction(bestPriceResources);
							isComplete = false;
							counter = 120;
						}
					} else {
						isComplete = true;
						counter = 0;
					}
				} else {
					allSuccess = false;
					if (counter == 1) {
						int neededNum = bestPriceResources.length;
						int numProcessed = threadResults.size();
						int failed = checkForFailedTransaction();
						LOGGER.debug("failed: " + failed);

						if (failed > 0) {
							allSuccess = false;
						}
						LOGGER.debug("allSuccess: " + allSuccess);

						int newNumber = numProcessed - failed;
						threadResults.clear();

						// How many were unsuccessful?
						bestPriceResources = checkBestPrices(neededNum - newNumber, allResources);

						// Are we at the end
						if (bestPriceResources == null || bestPriceResources.length == 0) {
							isComplete = true;
							counter = 0;
						} else {
							processTransaction(bestPriceResources);
							isComplete = false;
							counter = 120;
						}
					}
				}

				// Are we all done?
				if (allSuccess) {
					isComplete = true;

					for (BestPriceResource bestPriceResource : allResources) {
						bestPriceResource.setEp(null);
						bestPriceResource.setDone(true);

//						if (this.SITEACTIVEDB.hasActiveSite(bestPriceResource.getAccount().getSitetype(), bestPriceResource.getAccount().getUsername())) {
//							this.SITEACTIVEDB.deleteSiteActive(bestPriceResource.getAccount().getSitetype(), bestPriceResource.getAccount().getUsername());
//						}
					}
				}

				// Sleep for 1 second
				Thread.sleep(1000);
			} catch (Throwable ie) {
				LOGGER.warn(ie.getMessage(), ie);
			}

			counter--;
			LOGGER.debug("counter: " + counter);
		}
	}

	/**
	 * 
	 * @return
	 * @throws BatchException
	 */
	private int checkForFailedTransaction() throws BatchException {
		LOGGER.info("Entering checkForFailedTransaction()");
		int failed = 0;

		for (int x = 0; x < threadResults.size(); x++) {
			final BestPriceBuyOrderResource.ThreadResult threadResult = threadResults.get(x);
			final AccountEvent accountEvent = this.RECORDEVENTDB.getAccountEvent(threadResult.accountEventId);
			if (!threadResult.betPriceResoureStatus) {
				failed++;
			} else if (threadResult.betPriceResoureStatus && ("Error".equals(accountEvent.getStatus()) || "In Progress".equals(accountEvent.getStatus()))) {
				failed++;
			} else if (threadResult.betPriceResoureStatus && "Complete".equals(accountEvent.getStatus())) {
				this.successfulAttempts++;
			}
		}

		LOGGER.info("Entering checkForFailedTransaction()");
		return failed;
	}

	/**
	 * 
	 * @param bestPriceResources
	 * @throws BatchException
	 */
	private void processTransaction(BestPriceResource[] bestPriceResources) throws BatchException {
		LOGGER.info("Entering processTransaction()");
		LOGGER.debug("bestPriceResources.length: " + bestPriceResources.length);

		for (BestPriceResource bestPriceResource : bestPriceResources) {
			LOGGER.debug("BestPriceResource: " + bestPriceResource);

			if (bestPriceResource != null) {
				final AccountEvent accountEvent = populateAccountEvent(event, bestPriceResource.getAccount(), false);
				this.RECORDEVENTDB.setupAccountEvent(accountEvent);
				bestPriceResource.setAccountEvent(accountEvent);
				bestPriceResource.setDone(true);
			}
		}

		LOGGER.info("Exiting processTransaction()");
	}

	/**
	 * 
	 * @param outcome
	 */
	private void sendText(String outcome) {
		LOGGER.info("Entering sendText()");

		try {
			final SendText sendText = new SendText();
			sendText.setOAUTH2_TOKEN(TICKETADVANTAGEGMAILOATH.getAccessToken());
			sendText.sendTextWithMessage(mobileTextNumber, outcome + ": " + event.getEventname());
			LOGGER.debug(mobileTextNumber + " " + outcome + ": " + event.getEventname());
		} catch (BatchException be) {
			LOGGER.warn(be.getMessage(), be);
		}

		LOGGER.info("Exiting sendText()");
	}
}