/**
 * 
 */
package com.ticketadvantage.services.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ticketadvantage.services.db.ParlayRecordEventDB;
import com.ticketadvantage.services.db.RecordEventDB;
import com.ticketadvantage.services.email.SendText;
import com.ticketadvantage.services.email.ticketadvantage.TicketAdvantageGmailOath;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.AccountEvent;
import com.ticketadvantage.services.model.Accounts;
import com.ticketadvantage.services.model.BaseRecordEvent;
import com.ticketadvantage.services.model.MlRecordEvent;
import com.ticketadvantage.services.model.SpreadRecordEvent;
import com.ticketadvantage.services.model.TotalRecordEvent;

/**
 * @author jmiller
 *
 */
public class ProcessParlayResource implements BaseBestPriceBuyOrderResource, Runnable {
	private static final Logger LOGGER = Logger.getLogger(ProcessParlayResource.class);
	private static TicketAdvantageGmailOath TICKETADVANTAGEGMAILOATH = new TicketAdvantageGmailOath();
	private BaseRecordEvent event;
	private List<Accounts> accounts;
	private String maxAmount;
	private Boolean enableretry;
	private String mobileTextNumber;
	private Integer orderAmount;
	private RecordEventDB RECORDEVENTDB;
	private ParlayRecordEventDB PARLAYRECORDEVENTDB;
	private Boolean sendtextforaccount;
	private Boolean wasSuccessful;
	private int successfulAttempts = 0;
	private List<ProcessParlayResource.ThreadResult> threadResults = new ArrayList<ProcessParlayResource.ThreadResult>();

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
	public ProcessParlayResource(BaseRecordEvent event, 
			List<Accounts> accounts,
			String maxAmount,
			Boolean enableretry,
			String mobileTextNumber,
			Integer orderAmount,	
			RecordEventDB RECORDEVENTDB, 
			ParlayRecordEventDB PARLAYRECORDEVENTDB, 
			Boolean sendtextforaccount) {
		super();
		LOGGER.debug("Entering BestPriceBuyOrderResource()");

		try {
			this.event = event;
			this.accounts = accounts;
			this.maxAmount = maxAmount;
			this.enableretry = enableretry;
			this.mobileTextNumber = mobileTextNumber;
			this.orderAmount = orderAmount;
			this.RECORDEVENTDB = RECORDEVENTDB;
			this.PARLAYRECORDEVENTDB = PARLAYRECORDEVENTDB;
			this.sendtextforaccount = sendtextforaccount;

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
		final RecordEventDB RECORDEVENTDB = new RecordEventDB();
		final List<Accounts> accounts = new ArrayList<Accounts>();

		try {
			final Accounts account1 = RECORDEVENTDB.getAccount(new Long(2));
			final Accounts account2 = RECORDEVENTDB.getAccount(new Long(4));
			final Accounts account3 = RECORDEVENTDB.getAccount(new Long(5));
			final Accounts account4 = RECORDEVENTDB.getAccount(new Long(6));
			accounts.add(account1);
			accounts.add(account2);
			accounts.add(account3);
			accounts.add(account4);
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		final MlRecordEvent mre = new MlRecordEvent();
		mre.setAccountid(new Long(-99));
		mre.setAmount("2");
		mre.setAttempts(0);
		mre.setDatecreated(new Date());
		mre.setDatemodified(new Date());
		mre.setMlplusminusfirstone("-");
		mre.setMlinputfirstone("110");
		mre.setId(new Long(1));
		mre.setEventid(new Integer(913));
		mre.setEventid1(new Integer(913));
		mre.setEventid2(new Integer(914));
		mre.setEventname("#914 Chicago Cubs +115 for Game");
		mre.setEventteam1("Chicago Cubs");
		mre.setEventteam2("St. Louis Cardinals");
		mre.setEventtype("ml");
		mre.setGroupid(new Long(-99));
		mre.setScrappername("Iron69-kw2202");
		mre.setSport("mlblines"); 
		mre.setUserid(new Long(6));
		mre.setWtype("1");
		mre.setRotationid(913);

		final SpreadRecordEvent sre = new SpreadRecordEvent();
		sre.setAccountid(new Long(-99));
		sre.setAmount("2");
		sre.setAttempts(0);
		sre.setDatecreated(new Date());
		sre.setDatemodified(new Date());
		sre.setSpreadplusminusfirstone("+");
		sre.setSpreadinputfirstone("1.5");
		sre.setSpreadjuiceplusminusfirstone("-");
		sre.setSpreadinputjuicefirstone("200");
		sre.setId(new Long(1));
		sre.setEventid(new Integer(913));
		sre.setEventid1(new Integer(913));
		sre.setEventid2(new Integer(914));
		sre.setEventname("#914 Chicago Cubs +115 for Game");
		sre.setEventteam1("Chicago Cubs");
		sre.setEventteam2("St. Louis Cardinals");
		sre.setEventtype("spread");
		sre.setGroupid(new Long(-99));
		sre.setScrappername("Iron69-kw2202");
		sre.setSport("mlblines"); 
		sre.setUserid(new Long(6));
		sre.setWtype("1");
		sre.setRotationid(913);

		final TotalRecordEvent tre = new TotalRecordEvent();
		tre.setAccountid(new Long(-99));
		tre.setAmount("2");
		tre.setAttempts(0);
		tre.setDatecreated(new Date());
		tre.setDatemodified(new Date());
		tre.setTotalinputfirstone("8.5");
		tre.setTotaljuiceplusminusfirstone("-");
		tre.setTotalinputjuicefirstone("110");
		tre.setId(new Long(1));
		tre.setEventid(new Integer(913));
		tre.setEventid1(new Integer(913));
		tre.setEventid2(new Integer(914));
		tre.setEventname("#914 Chicago Cubs +115 for Game");
		tre.setEventteam1("Chicago Cubs");
		tre.setEventteam2("St. Louis Cardinals");
		tre.setEventtype("total");
		tre.setGroupid(new Long(-99));
		tre.setScrappername("Iron69-kw2202");
		tre.setSport("mlblines"); 
		tre.setUserid(new Long(6));
		tre.setWtype("1");
		tre.setRotationid(913);

		new ProcessParlayResource(mre, 
				accounts,
				"1",
				false,
				"",
				2,	
				RECORDEVENTDB,
				null,
				false);
		
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (Throwable t) {
				
			}
		}
	}

	/**
	 * 
	 * @author jmiller
	 *
	 */
	private class ThreadResult {
		public Long accountEventId;
		public Boolean betPriceResoureStatus;
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

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.service.BaseBestPriceBuyOrderResource#bestPriceResourceDone(java.lang.Boolean, java.lang.Long)
	 */
	@Override
	public void bestPriceResourceDone(Boolean wasSuccessful, Long accountId) {
		final ProcessParlayResource.ThreadResult threadResult = new ProcessParlayResource.ThreadResult();
		threadResult.betPriceResoureStatus = wasSuccessful;
		threadResult.accountEventId = accountId;
		threadResults.add(threadResult);
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
				final BestPriceResource[] bestPriceResources = checkBestPrices(numAccountsNeeded, allResources);

				// Now place the transaction
				for (BestPriceResource bestPriceResource : bestPriceResources) {
					LOGGER.debug("BestPriceResource: " + bestPriceResource.getAccount().getName());
					final AccountEvent accountEvent = populateAccountEvent(event, bestPriceResource.getAccount(), false);
					this.RECORDEVENTDB.setupAccountEvent(accountEvent);
					bestPriceResource.setAccountEvent(accountEvent);
					bestPriceResource.setDone(true);
				}

				// Process until complete or run out of accounts
				processAccounts(numAccountsNeeded, bestPriceResources, allResources);
			}

			if (numAccountsNeeded != 0 && numAccountsNeeded == successfulAttempts) {
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
			account.setShowrequestresponse(false);
			LOGGER.error("Account: " + account);
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
		BestPriceResource[] bestPriceResources = null;
		if (allResources.size() >= numAccountsNeeded) {
			bestPriceResources = new BestPriceResource[numAccountsNeeded];
		} else {
			bestPriceResources = new BestPriceResource[allResources.size()];
		}
		int acctcounter = 0;

		// Loop through all threads
		for (BestPriceResource bestPrice : allResources) {
			LOGGER.error("BestPriceResource: " + bestPrice.getAccount().getName());

			// Check which event type
			if ("spread".equals(bestPrice.getEventType())) {
				if (acctcounter <= numAccountsNeeded) {
					if (acctcounter <= (bestPriceResources.length-1)) {
						bestPriceResources[acctcounter] = bestPrice;
					}
				} else {
					final String lineIndicator = bestPrice.getEventLineIndicator();
					LOGGER.debug("lineIndicator: " + lineIndicator);

					if (lineIndicator != null && "-".equals(lineIndicator)) {
						checkSpread(true, bestPriceResources, bestPrice);
					} else {
						checkSpread(false, bestPriceResources, bestPrice);
					}
				}
			} else if ("total".equals(bestPrice.getEventType())) {
				if (acctcounter <= numAccountsNeeded) {
					if (acctcounter <= (bestPriceResources.length-1)) {
						bestPriceResources[acctcounter] = bestPrice;
					}
				} else {
					final String lineIndicator = bestPrice.getEventLineIndicator();
					LOGGER.debug("lineIndicator: " + lineIndicator);

					if (lineIndicator != null && "o".equals(lineIndicator)) {
						checkTotal(true, bestPriceResources, bestPrice);
					} else {
						checkTotal(false, bestPriceResources, bestPrice);
					}
				}
			} else if ("ml".equals(bestPrice.getEventType())) {
				if (acctcounter <= numAccountsNeeded) {
					if (acctcounter <= (bestPriceResources.length-1)) {
						bestPriceResources[acctcounter] = bestPrice;
					}
				} else {
					final String lineIndicator = bestPrice.getEventLineIndicator();
					LOGGER.debug("lineIndicator: " + lineIndicator);

					if (lineIndicator != null && "-".equals(lineIndicator)) {
						checkMl(true, bestPriceResources, bestPrice);
					} else {
						checkMl(false, bestPriceResources, bestPrice);
					}
				}
			}

			acctcounter++;
		}

		// Remove the one's just added from the list
		for (int x = 0; x < bestPriceResources.length; x++) {
			final BestPriceResource bestPrice = bestPriceResources[x];
			final Iterator<BestPriceResource> itr = allResources.iterator();
			while (itr.hasNext()) {
				final BestPriceResource bestPriceItr = itr.next();
				final Accounts account = bestPrice.getAccount();
				final Accounts account2 = bestPriceItr.getAccount();

				if (account.getId().longValue() == account2.getId().longValue()) {
					itr.remove();
				}
			}
		}

		LOGGER.info("Exiting checkBestPrices()");
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
						bestPriceResources[x] = bestPrice;
					} else if (value == valueTemp) {
						boolean isBetter = checkJuice(bestPriceResources[x], bestPrice);
						LOGGER.debug("isBetter: " + isBetter);

						if (isBetter) {
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
				final String tempLine = bestPriceResources[x].getEventLine();
				LOGGER.debug("tempLine: " + tempLine);

				if (tempLine != null && tempLine.length() > 0) {
					float valueTemp = Float.parseFloat(tempLine);
					LOGGER.debug("valueTemp: " + valueTemp);
		
					if (isOver && (value < valueTemp)) {
						bestPriceResources[x] = bestPrice;
					} else if (!isOver && (value > valueTemp)) {
						bestPriceResources[x] = bestPrice;
					} else if (value == valueTemp) {
						boolean isBetter = checkJuice(bestPriceResources[x], bestPrice);
						LOGGER.debug("isBetter: " + isBetter);
		
						if (isBetter) {
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
						bestPriceResources[x] = bestPrice;
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

		// Check if we should retry on failed attempts
		if (enableRetry) {
			accountEvent.setAttempts(new Integer(11));
		}

		LOGGER.info("Exiting populateAccountEvent()");
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
		int counter = 90;

		// Now wait for
		while (!isComplete && counter != 0) {
			try {
				boolean allSuccess = true;
				LOGGER.debug("threadResults.size(): " + threadResults.size());
				LOGGER.debug("bestPriceResources.length: " + bestPriceResources.length);

				if (threadResults.size() == bestPriceResources.length && 
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
						if (bestPriceResources.length == 0) {
							isComplete = true;
							counter = 0;
						} else {
							processTransaction(bestPriceResources);
							isComplete = false;
							counter = 90;
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
						if (bestPriceResources.length == 0) {
							isComplete = true;
							counter = 0;
						} else {
							processTransaction(bestPriceResources);
							isComplete = false;
							counter = 90;
						}
					}
				}

				// Are we all done?
				if (allSuccess) {
					isComplete = true;
					for (BestPriceResource bestPriceResource : allResources) {
						bestPriceResource.setEp(null);
						bestPriceResource.setDone(true);
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
			final ProcessParlayResource.ThreadResult threadResult = threadResults.get(x);
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
			final AccountEvent accountEvent = populateAccountEvent(event, bestPriceResource.getAccount(), false);
			this.RECORDEVENTDB.setupAccountEvent(accountEvent);
			bestPriceResource.setAccountEvent(accountEvent);
			bestPriceResource.setDone(true);
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