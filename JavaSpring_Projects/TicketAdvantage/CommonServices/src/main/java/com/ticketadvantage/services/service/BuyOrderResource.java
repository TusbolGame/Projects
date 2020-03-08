/**
 * 
 */
package com.ticketadvantage.services.service;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
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
import com.ticketadvantage.services.db.RecordEventDB;
import com.ticketadvantage.services.model.AccountEvent;
import com.ticketadvantage.services.model.ProcessSiteInput;
import com.ticketadvantage.services.model.ProcessSiteOutput;

/**
 * @author jmiller
 *
 */
public class BuyOrderResource implements Runnable {
	private static final Logger LOGGER = Logger.getLogger(BuyOrderResource.class);
	private ClientConfig config = null;
	private Client client = null;
	private WebTarget target = null;
	private JAXBContext jc = null;
	private Marshaller marshaller = null;

	private List<AccountEvent> accountEvents;
	private Double orderAmount;
	private Double maxAmount;
	private RecordEventDB RECORDEVENTDB;
	
	@Autowired
	private AccountDAO accountDAO;

	/**
	 * 
	 */
	public BuyOrderResource() {
		super();
	}

	/**
	 * 
	 * @param accountEvents
	 * @param orderAmount
	 * @param maxAmount
	 */
	public BuyOrderResource(List<AccountEvent> accountEvents, Double orderAmount, Double maxAmount) {
		super();
		LOGGER.debug("Entering AWSTransactionEventResource()");

		try {
			this.accountEvents = accountEvents;
			this.orderAmount = orderAmount;
			this.maxAmount = maxAmount;
			this.RECORDEVENTDB = new RecordEventDB();

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
			if (accountEvents != null && !accountEvents.isEmpty()) {
				final List<Long> acctsSuccess = new ArrayList<Long>();
				Random rand = new Random();
				int n = rand.nextInt(accountEvents.size()) + 1;
				boolean isComplete = false;

				// Have we went through all of the accounts or fullfilled the buy order
				while (!isComplete && accountEvents != null) {
					LOGGER.debug("accountEvents.size(): " + accountEvents.size());

					// Are we all used up on the accounts?
					if (accountEvents.size() > 0) {
						int countItr = 0;
						AccountEvent accountEventUsed = null;
						final Iterator<AccountEvent> itr = accountEvents.iterator();
						boolean accountDone = false;
						double runningnumber = 0.0;
						final ExecutorService executor = Executors.newFixedThreadPool(accountEvents.size());
						List<Future<?>> futures = new ArrayList<Future<?>>();

						while (itr.hasNext() && !accountDone) {
							countItr++;
							final AccountEvent accountEvent = itr.next();

							LOGGER.debug("Random #: " + n);
							// We have a match
							if (countItr == n) {
								try {
									// Setup the account event
									RECORDEVENTDB.setupAccountEvent(accountEvent);

									final TransactionEventResource transactionEventResource = new TransactionEventResource();
									LOGGER.debug("AccountEvent: " + accountEvent);

									transactionEventResource.setAccountEvent(accountEvent);
									final EntityManager entityManager2 = Persistence.createEntityManagerFactory("entityManager").createEntityManager();
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
									boolean wasSuccessful = false;

									if (acctevent.getStatus() != null && acctevent.getStatus().equals("Complete")) {
										wasSuccessful = true;
									}

									// Was the transaction successful?
//									boolean wasSuccessful = callAWS(accountEvent.getId());
//									LOGGER.error("wasSuccessful: " + wasSuccessful);

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
									accountEventUsed = accountEvent;
								} catch (Exception e) {
									LOGGER.error(e.getMessage(), e);
								}
							}
						}

						if (!isComplete) {
							accountEvents.remove(accountEventUsed);
							// Remove from the list one's used
							if (accountEvents != null && accountEvents.size() > 0) {
								rand = new Random();
								n = rand.nextInt(accountEvents.size()) + 1;
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
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		LOGGER.info("Exiting run()");
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
}