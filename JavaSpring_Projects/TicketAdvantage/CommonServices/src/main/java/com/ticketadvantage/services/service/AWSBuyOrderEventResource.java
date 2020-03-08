/**
 * 
 */
package com.ticketadvantage.services.service;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;
import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.springframework.beans.factory.annotation.Autowired;

import com.ticketadvantage.services.dao.AccountDAO;
import com.ticketadvantage.services.dao.RecordEventDAO;
import com.ticketadvantage.services.dao.RecordEventDAOImpl;
import com.ticketadvantage.services.dao.sites.SiteProcessor;
import com.ticketadvantage.services.db.RecordEventDB;
import com.ticketadvantage.services.email.ticketadvantage.TicketAdvantageGmailOath;
import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.AccountEvent;
import com.ticketadvantage.services.model.Accounts;
import com.ticketadvantage.services.model.BaseRecordEvent;
import com.ticketadvantage.services.model.MlRecordEvent;
import com.ticketadvantage.services.model.ProcessSiteInput;
import com.ticketadvantage.services.model.ProcessSiteOutput;
import com.ticketadvantage.services.model.SpreadRecordEvent;
import com.ticketadvantage.services.model.TotalRecordEvent;
import com.ticketadvantage.services.site.util.AccountSite;
import com.ticketadvantage.services.telegram.TelegramBotSender;

/**
 * @author jmiller
 *
 */
public class AWSBuyOrderEventResource implements Runnable {
	private static final Logger LOGGER = Logger.getLogger(AWSBuyOrderEventResource.class);
	private static TicketAdvantageGmailOath TICKETADVANTAGEGMAILOATH = new TicketAdvantageGmailOath();
	private ClientConfig config = null;
	private Client client = null;
	private WebTarget target = null;
	private JAXBContext jc = null;
	private Marshaller marshaller = null;
	private BaseRecordEvent event;
	private Set<Accounts> accounts;
	private String maxAmount;
	private Boolean enableretry;
	private String mobileTextNumber;
	private Integer orderAmount;
	private RecordEventDB RECORDEVENTDB;
	private Boolean sendtextforaccount;
	private Boolean humanspeed = new Boolean(false);

	@Autowired
	private AccountDAO accountDAO;
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
	 * @param humanspeed
	 */
	public AWSBuyOrderEventResource(BaseRecordEvent event, 
			Set<Accounts> accounts,
			String maxAmount,
			Boolean enableretry,
			String mobileTextNumber,
			Integer orderAmount,	
			RecordEventDB RECORDEVENTDB,
			Boolean sendtextforaccount,
			Boolean humanspeed) {
		super();
		LOGGER.debug("Entering AWSTransactionEventResource()");

		try {
			this.event = event;
			this.accounts = accounts;
			this.maxAmount = maxAmount;
			this.enableretry = enableretry;
			this.mobileTextNumber = mobileTextNumber;
			this.orderAmount = orderAmount;
			this.RECORDEVENTDB = RECORDEVENTDB;
			this.sendtextforaccount = sendtextforaccount;
			this.humanspeed = humanspeed;

			new Thread(this).start();
		} catch (Exception e) {
			LOGGER.error("Exception setting up JAXBContext and Marshaller", e);
		}

		LOGGER.debug("Exiting AWSTransactionEventResource()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * @return the event
	 */
	public BaseRecordEvent getEvent() {
		return event;
	}

	/**
	 * @param event
	 *            the event to set
	 */
	public void setEvent(BaseRecordEvent event) {
		this.event = event;
	}

	/**
	 * @return the accounts
	 */
	public Set<Accounts> getAccounts() {
		return accounts;
	}

	/**
	 * @param accounts
	 *            the accounts to set
	 */
	public void setAccounts(Set<Accounts> accounts) {
		this.accounts = accounts;
	}

	/**
	 * @return the maxAmount
	 */
	public String getMaxAmount() {
		return maxAmount;
	}

	/**
	 * @param maxAmount the maxAmount to set
	 */
	public void setMaxAmount(String maxAmount) {
		this.maxAmount = maxAmount;
	}

	/**
	 * @return the enableretry
	 */
	public Boolean getEnableretry() {
		return enableretry;
	}

	/**
	 * @param enableretry the enableretry to set
	 */
	public void setEnableretry(Boolean enableretry) {
		this.enableretry = enableretry;
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
	 * @return the orderAmount
	 */
	public Integer getOrderAmount() {
		return orderAmount;
	}

	/**
	 * @param orderAmount the orderAmount to set
	 */
	public void setOrderAmount(Integer orderAmount) {
		this.orderAmount = orderAmount;
	}

	/**
	 * @return the rECORDEVENTDB
	 */
	public RecordEventDB getRECORDEVENTDB() {
		return RECORDEVENTDB;
	}

	/**
	 * @param rECORDEVENTDB
	 *            the rECORDEVENTDB to set
	 */
	public void setRECORDEVENTDB(RecordEventDB rECORDEVENTDB) {
		RECORDEVENTDB = rECORDEVENTDB;
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
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		LOGGER.info("Entering run()");

		try {
			// Check to make sure the account list is not empty
			if (accounts != null && !accounts.isEmpty()) {
				// Setup the amounts
				LOGGER.debug("orderAmount: " + orderAmount);

				final List<Long> acctsSuccess = new ArrayList<Long>();
				Random rand = new Random();
				int n = rand.nextInt(accounts.size()) + 1;
				boolean isComplete = false;
				double runningnumber = 0.0;
				final ExecutorService executor = Executors.newFixedThreadPool(accounts.size());
				List<Future<?>> futures = new ArrayList<Future<?>>();

				// Have we went through all of the accounts or full-filled the buy order
				while (!isComplete && accounts != null) {
					LOGGER.debug("accounts.size(): " + accounts.size());

					// Are we all used up on the accounts?
					if (accounts.size() > 0) {
						int countItr = 0;
						Accounts accountUsed = null;
						final Iterator<Accounts> itr = accounts.iterator();
						boolean accountDone = false;

						while (itr.hasNext() && !accountDone) {
							countItr++;
							final Accounts account = itr.next();

							LOGGER.debug("Random #: " + n);
							// We have a match
							if (countItr == n) {
								try {
									// Populate events
									LOGGER.debug("AccountName: " + account.getName());
									LOGGER.debug("enableretry: " + enableretry);
									final AccountEvent accountEvent = populateAccountEvent(event, 
											account,
											enableretry,
											humanspeed);

									// Setup the account event
									RECORDEVENTDB.setupAccountEvent(accountEvent);

									// Was the transaction successful?
									boolean wasSuccessful = false;
/*
									if (account.getIscomplexcaptcha()) {
										wasSuccessful = processAccount(event, account, accountEvent);
									} else {
										wasSuccessful = callAWS(accountEvent.getId());
									}
*/
									final TransactionEventResource transactionEventResource = new TransactionEventResource();
									LOGGER.debug("AccountEvent: " + accountEvent);

									transactionEventResource.setAccountEvent(accountEvent);
									final EntityManager entityManager2 = Persistence.createEntityManagerFactory("entityManager").createEntityManager();
									LOGGER.error("Account DAO Impl Bean Creating");
									accountDAO.setEntityManager(entityManager2);						
									final RecordEventDAO recordEventDAO = new RecordEventDAOImpl();
									recordEventDAO.setEntityManager(entityManager2);
									transactionEventResource.setAccountDAO(accountDAO);
									transactionEventResource.setRecordEventDAO(recordEventDAO);
									LOGGER.debug("TransactionEventResource: " + transactionEventResource);

									final Runnable worker = transactionEventResource;
									Future<?> f = executor.submit(worker);
									futures.add(f);
									LOGGER.debug("Called executor");

									// A) Await all runnables to be done (blocking)
									for(Future<?> future : futures)
									    future.get(); // get will block until the future is done

									// B) Check if all runnables are done (non-blocking)
									boolean allDone = true;
									for(Future<?> future : futures){
									    allDone &= future.isDone(); // check if future is done
									    LOGGER.debug("allDone: " + allDone);
									}

									// Shut down the executor
									executor.shutdown();

									final AccountEvent acctevent = RECORDEVENTDB.getAccountEvent(accountEvent.getId());

									if (acctevent.getStatus() != null && acctevent.getStatus().equals("Complete")) {
										wasSuccessful = true;
									}

									LOGGER.error("wasSuccessful: " + wasSuccessful);

									if (wasSuccessful) {
										try {
											Double risk = Double.parseDouble(acctevent.getRiskamount());
											runningnumber += risk.doubleValue();
										} catch (Throwable t) {
											LOGGER.error(t.getMessage(), t);
										}

										acctsSuccess.add(accountEvent.getId());

										// Check if we are all done
										if (runningnumber >= orderAmount.doubleValue()) {
											LOGGER.error("Buy order has been completed");
											isComplete = true;
										}
									}

									accountDone = true;
									accountUsed = account;
								} catch (Throwable e) {
									LOGGER.error(e.getMessage(), e);
								}
							}
						}

						if (!isComplete) {
							accounts.remove(accountUsed);
							// Remove from the list one's used
							if (accounts != null && accounts.size() > 0) {
								rand = new Random();
								n = rand.nextInt(accounts.size()) + 1;
							} else {
								rand = new Random();
								n = 1;
							}
						}
					} else {
						isComplete = true;
						LOGGER.error("Account list is 0");
					}
				}

				// Did we fullfill the order? No, send a message
				if (runningnumber < orderAmount.doubleValue()) {
					// Send out an error messsage
					try {
						if (mobileTextNumber != null) {
							TelegramBotSender.sendToTelegram(mobileTextNumber, "Fail: " + event.getEventname());
							LOGGER.error(mobileTextNumber + " Fail: " + event.getEventname());
						}
					} catch (Exception be) {
						LOGGER.error(be.getMessage(), be);
					}
				} else {
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
				}
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);

			// Send out an error messsage
			try {
				LOGGER.error("mobileTextNumber: " + mobileTextNumber);
				if (mobileTextNumber != null) {
					TelegramBotSender.sendToTelegram(mobileTextNumber, "Fail: " + event.getEventname());
					LOGGER.error(mobileTextNumber + " Fail: " + event.getEventname());
				}
			} catch (Exception be) {
				LOGGER.error(be.getMessage(), be);
			}
		}

		LOGGER.info("Exiting run()");
	}

	/**
	 * 
	 * @param event
	 * @param account
	 * @param enableRetry
	 * @param humanspeed
	 * @return
	 */
	protected AccountEvent populateAccountEvent(BaseRecordEvent event, 
			Accounts account, 
			Boolean enableRetry,
			Boolean humanspeed) {
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
		accountEvent.setIscomplexcaptcha(account.getIscomplexcaptcha());
		accountEvent.setHumanspeed(humanspeed);

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
		} catch (Throwable t) {
			LOGGER.error("BatchException for Event: " + event + " and account: "
					+ account + " Account Event: " + ae, t);
			ae.setErrorcode(BatchErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION);
			ae.setErrormessage(BatchErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
			ae.setErrorexception(t.getMessage());
			ae.setStatus("Error");
		}

		LOGGER.info("Exiting processAccount()");
		return wasSuccessful;
	}

	/**
	 * 
	 * @param accountEventId
	 * @return
	 */
	protected boolean callAWS(Long accountEventId) {
		LOGGER.info("Entering callAWS()");
		LOGGER.error("accountEventId: " + accountEventId);
		boolean success = false;

		try {
			jc = JAXBContext.newInstance(ProcessSiteInput.class);
			marshaller = jc.createMarshaller();
			marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);

			config = new ClientConfig();
			config = config.property(ClientProperties.CONNECT_TIMEOUT, 30000);
			config = config.property(ClientProperties.READ_TIMEOUT, 60000);

			client = ClientBuilder.newClient(config);
//			target = client.target(UriBuilder.fromUri("https://gr9gn86vp0.execute-api.sa-east-1.amazonaws.com/prod").build());
			target = client.target(UriBuilder.fromUri("https://n3kwusybo1.execute-api.us-east-1.amazonaws.com/prod").build());

			final ProcessSiteInput processSiteInput = new ProcessSiteInput();
			processSiteInput.setInputvalue(accountEventId.intValue());
			LOGGER.error("ProcessSiteInput: " + processSiteInput);

			final StringWriter sw = new StringWriter();
			marshaller.marshal(processSiteInput, sw);
			final Builder request = target.request(MediaType.APPLICATION_JSON);

			// overridden timeout value for this request
			request.property(ClientProperties.CONNECT_TIMEOUT, 45000);
			request.property(ClientProperties.READ_TIMEOUT, 45000);

			// Call the service asynchronously
			LOGGER.error("AWS Entering Time: " + new java.util.Date());
			final Response jsonResponse = request.header("Content-Type", MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.post(Entity.entity(sw.toString(), MediaType.APPLICATION_JSON), Response.class);

			// Valid response?
			LOGGER.debug("jsonResponse.getStatus(): " + jsonResponse.getStatus());
			if (jsonResponse.getStatus() == 200) { 
				final ProcessSiteOutput retValue = jsonResponse.readEntity(ProcessSiteOutput.class);
				LOGGER.error("AWS Exiting Time: " + new java.util.Date());
				LOGGER.error("ProcessSiteOutput: " + retValue);
	
				if (retValue != null && retValue.getOutputvalue() != null && retValue.getOutputvalue().intValue() == 0) {
					LOGGER.error("Successful!");
					success = true;
				}
			} else if (jsonResponse.getStatus() == 504) {
				int count = 6;

				// Loop for a minute waiting for completion
				while (count != 0) {
					// We have a timeout situation on the Gateway API not the lambda function so we need to poll
					final AccountEvent ae = RECORDEVENTDB.getAccountEvent(accountEventId);
					
					if (ae.getStatus().equals("Complete")) {
						success = true;
						count = 0;						
					} else if (ae.getStatus().equals("Error")) {
						success = false;
						count = 0;
					} else {
						count--;
						Thread.sleep(10000);						
					}
				}
			} else {
				int count = 6;

				// Loop for a minute waiting for completion
				while (count != 0) {
					// We have a timeout situation on the Gateway API not the lambda function so we need to poll
					final AccountEvent ae = RECORDEVENTDB.getAccountEvent(accountEventId);
					
					if (ae.getStatus().equals("Complete")) {
						success = true;
						count = 0;						
					} else if (ae.getStatus().equals("Error")) {
						success = false;
						count = 0;
					} else {
						count--;
						Thread.sleep(10000);						
					}
				}				
			}
		} catch (Throwable t) {
			LOGGER.error("Throwable for AccountEventId " + accountEventId, t);
		}

		LOGGER.info("Exiting callAWS()");
		return success;
	}

	/**
	 * 
	 * @return
	 */
	private String getAccessToken() {
		LOGGER.info("Entering getAccessToken()");
		final String accessToken = TICKETADVANTAGEGMAILOATH.getAccessToken();
		LOGGER.info("Exiting getAccessToken()");
		return accessToken;
	}
}