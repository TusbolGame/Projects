/**
 * 
 */
package com.ticketadvantage.services.service;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;
import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;

import com.ticketadvantage.services.dao.AccountDAO;
import com.ticketadvantage.services.dao.EventsDAO;
import com.ticketadvantage.services.dao.GroupDAO;
import com.ticketadvantage.services.dao.RecordEventDAO;
import com.ticketadvantage.services.errorhandling.AppErrorCodes;
import com.ticketadvantage.services.errorhandling.AppErrorMessage;
import com.ticketadvantage.services.errorhandling.AppException;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.AccountEvent;
import com.ticketadvantage.services.model.Accounts;
import com.ticketadvantage.services.model.BaseRecordEvent;
import com.ticketadvantage.services.model.EventId;
import com.ticketadvantage.services.model.EventPackage;
import com.ticketadvantage.services.model.EventsPackage;
import com.ticketadvantage.services.model.Groups;
import com.ticketadvantage.services.model.ProcessSiteInput;
import com.ticketadvantage.services.model.ProcessSiteOutput;
import com.ticketadvantage.services.model.SpreadLastThree;
import com.ticketadvantage.services.model.SpreadRecordEvent;

/**
 * @author jmiller
 *
 */
public class PlaceSpreadLastThreeResource implements Runnable {
	private static final Logger LOGGER = Logger.getLogger(PlaceSpreadLastThreeResource.class);

	private ClientConfig config = null;
	private Client client = null;
	private WebTarget target = null;
	private JAXBContext jc = null;
	private Marshaller marshaller = null;

	private RecordEventDAO recordEventDAO;
	private AccountDAO accountDAO;
	private GroupDAO groupDAO;
	private EventsDAO eventsDAO;
	private EntityManager entityManager;
	List<SpreadLastThree> spreadLastThreeList;

	/**
	 * 
	 */
	public PlaceSpreadLastThreeResource(RecordEventDAO recordEventDAO,
			AccountDAO accountDAO, GroupDAO groupDAO, EventsDAO eventsDAO,
			EntityManager entityManager, List<SpreadLastThree> spreadLastThreeList) {
		super();
		LOGGER.debug("Entering PlaceSpreadLastThreeResource()");
		this.recordEventDAO = recordEventDAO;
		this.accountDAO = accountDAO;
		this.groupDAO = groupDAO;
		this.eventsDAO = eventsDAO;
		this.entityManager = entityManager;
		this.spreadLastThreeList = spreadLastThreeList;
		LOGGER.debug("Exiting PlaceSpreadLastThreeResource()");

		new Thread(this).start();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
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
	 * @return the groupDAO
	 */
	public GroupDAO getGroupDAO() {
		return groupDAO;
	}

	/**
	 * @param groupDAO the groupDAO to set
	 */
	public void setGroupDAO(GroupDAO groupDAO) {
		this.groupDAO = groupDAO;
	}

	/**
	 * @return the eventsDAO
	 */
	public EventsDAO getEventsDAO() {
		return eventsDAO;
	}

	/**
	 * @param eventsDAO the eventsDAO to set
	 */
	public void setEventsDAO(EventsDAO eventsDAO) {
		this.eventsDAO = eventsDAO;
	}

	/**
	 * @return the entityManager
	 */
	public EntityManager getEntityManager() {
		return entityManager;
	}

	/**
	 * @param entityManager the entityManager to set
	 */
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/**
	 * @return the spreadLastThreeList
	 */
	public List<SpreadLastThree> getSpreadLastThreeList() {
		return spreadLastThreeList;
	}

	/**
	 * @param spreadLastThreeList the spreadLastThreeList to set
	 */
	public void setSpreadLastThreeList(List<SpreadLastThree> spreadLastThreeList) {
		this.spreadLastThreeList = spreadLastThreeList;
	}

	/**
	 * 
	 * @param placeTransaction
	 * @param ep
	 * @return
	 * @throws AppException
	 */
	public EventId setupSpreadEvent(SpreadLastThree spreadLastThree, EventPackage ep) throws AppException {
		LOGGER.info("Entering setupSpreadEvent()");
		LOGGER.debug("SpreadLastThree: " + spreadLastThree);
		EventId eventId = null;

		try {
			final SpreadRecordEvent spreadRecordEvent = new SpreadRecordEvent();
			Float kpspread = spreadLastThree.getKenpomspread();
			Float homespread = spreadLastThree.getHomespread();
			Long rotationid = null;
			String lineindicator = "";
			String line = "";

			// Home dog?
			if (kpspread.floatValue() >= 0) {
				// Bet home team? 
				if (homespread.floatValue() >= kpspread.floatValue()) {
					// Bet visitor team
					spreadRecordEvent.setSpreadplusminusfirstone("-");
					String kspread = kpspread.toString().replace("-", "");
					spreadRecordEvent.setSpreadinputfirstone(kspread);
					spreadRecordEvent.setSpreadjuiceplusminusfirstone("-");
					spreadRecordEvent.setSpreadinputjuicefirstone("110");
					lineindicator = "-";
					line = kspread;
					final Integer rotid = ep.getTeamone().getId();
					rotationid = rotid.longValue();
				} else {
					// Nope, worse spread, bet home
					spreadRecordEvent.setSpreadplusminussecondone("+");
					String kspread = kpspread.toString().replace("-", "");
					spreadRecordEvent.setSpreadinputsecondone(kspread);
					spreadRecordEvent.setSpreadjuiceplusminussecondone("-");
					spreadRecordEvent.setSpreadinputjuicesecondone("110");
					lineindicator = "+";
					line = kspread;
					final Integer rotid = ep.getTeamtwo().getId();
					rotationid = rotid.longValue();
				}
			} else {
				// Home favorite
				if (homespread.floatValue() >= kpspread.floatValue()) {
					// Nope, worse spread, bet visitor
					spreadRecordEvent.setSpreadplusminusfirstone("+");
					String kspread = kpspread.toString().replace("-", "");
					spreadRecordEvent.setSpreadinputfirstone(kspread);
					spreadRecordEvent.setSpreadjuiceplusminusfirstone("-");
					spreadRecordEvent.setSpreadinputjuicefirstone("110");
					lineindicator = "+";
					line = kspread;
					final Integer rotid = ep.getTeamone().getId();
					rotationid = rotid.longValue();
				} else {
					// Bet home team
					spreadRecordEvent.setSpreadplusminussecondone("-");
					String kspread = kpspread.toString().replace("-", "");
					spreadRecordEvent.setSpreadinputsecondone(kspread);
					spreadRecordEvent.setSpreadjuiceplusminussecondone("-");
					spreadRecordEvent.setSpreadinputjuicesecondone("110");
					lineindicator = "-";
					line = kspread;
					final Integer rotid = ep.getTeamtwo().getId();
					rotationid = rotid.longValue();
				}				
			}
			LOGGER.debug("lineindicator: " + lineindicator);
			LOGGER.debug("line: " + line);

			setupBaseEvent("100", rotationid, lineindicator, line, spreadRecordEvent, ep);
			LOGGER.debug("SpreadRecordEvent: " + spreadRecordEvent);

			// Set the spread event
			final Long id = recordEventDAO.setSpreadEvent(spreadRecordEvent);
			LOGGER.debug("ID: " + id);
			eventId = new EventId();
			eventId.setId(id);
			spreadRecordEvent.setId(id);

			// Setup the account events
			List<AccountEvent> accountEvents = setupAccountEvents(true, rotationid, lineindicator, line, spreadRecordEvent, new Long(4));

			// Check if these need to be run right away
			if (spreadRecordEvent.getAttempts().intValue() == 0) {
				setupBuyOrder(accountEvents, spreadRecordEvent, Double.parseDouble("100"), Double.parseDouble("100"));
			}
		} catch (AppException ae) {
			LOGGER.error("AppException in recordSpreadEvent()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in recordSpreadEvent()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}

		LOGGER.info("Exiting placeTransaction()");
		return eventId;
	}

	/**
	 * 
	 * @param placeTransaction
	 * @param baseRecordEvent
	 * @param ep
	 * @throws AppException
	 */
	public void setupBaseEvent(String amount, Long rotationId, String lineindicator, String line, BaseRecordEvent baseRecordEvent, EventPackage ep) throws AppException {
		LOGGER.info("Entering setupBaseEvent()");

		// Setup the data
		baseRecordEvent.setAccountid(new Long(-99));
		baseRecordEvent.setAmount(amount);
		baseRecordEvent.setAttempts(0);
		baseRecordEvent.setDatecreated(new Date());
		baseRecordEvent.setDatemodified(new Date());
		baseRecordEvent.setDatentime(new Date());
		baseRecordEvent.setEventdatetime(ep.getEventdatetime());

		// Rotation ID
		if (rotationId % 2 == 0) {
			baseRecordEvent.setEventid2(rotationId.intValue());
			baseRecordEvent.setEventid1(rotationId.intValue() - 1);
			baseRecordEvent.setEventid(rotationId.intValue() - 1);
			baseRecordEvent.setEventname(getGameType("ncaablines") + " #" + 
					rotationId + " " + ep.getTeamtwo().getTeam() + " " + 
					lineindicator + line + " " + 
					"-110" + " for " + 
					getBetType("ncaablines"));
		} else {
			baseRecordEvent.setEventid2(rotationId.intValue() + 1);
			baseRecordEvent.setEventid1(rotationId.intValue());
			baseRecordEvent.setEventid(rotationId.intValue());
			baseRecordEvent.setEventname(getGameType("ncaablines") + " #" + 
					rotationId + " " + ep.getTeamone().getTeam() + " " + 
					lineindicator + line + " " + 
					"-110" + " for " + 
					getBetType("ncaablines"));
		}
		LOGGER.debug("EventName: " + baseRecordEvent.getEventname());
		baseRecordEvent.setEventtype("spread");
		baseRecordEvent.setEventteam1(ep.getTeamone().getTeam());
		baseRecordEvent.setEventteam2(ep.getTeamtwo().getTeam());
		baseRecordEvent.setGroupid(new Long(-99));
		baseRecordEvent.setRotationid(rotationId.intValue());
		baseRecordEvent.setScrappername("WoO");
		baseRecordEvent.setSport("ncaablines");
		baseRecordEvent.setUserid(new Long(1));
		baseRecordEvent.setWtype("2");

		LOGGER.info("Exiting setupBaseEvent()");
	}

	/**
	 * 
	 * @param buyOrder
	 * @param rotationId
	 * @param lineindicator
	 * @param line
	 * @param event
	 * @param groupid
	 * @return
	 */
	private List<AccountEvent> setupAccountEvents(Boolean buyOrder, Long rotationId, String lineindicator, String line, BaseRecordEvent event, Long groupid) {
		LOGGER.info("Entering setupAccountEvents()");
		LOGGER.debug("Event: " + event);
		final List<AccountEvent> accountEvents = new ArrayList<AccountEvent>();

		// Check for groups first
		final Set<Accounts> accounts = setupAccountsForGroup(groupid);
		if (accounts != null && !accounts.isEmpty()) {
			for (Accounts account : accounts) {
				// Populate events
				final AccountEvent accountEvent = populateAccountEvent(buyOrder, rotationId, lineindicator, line, event, account);
				accountEvents.add(accountEvent);
			}
		}

		LOGGER.info("Exiting setupAccountEvents()");
		return accountEvents;
	}

	/**
	 * 
	 * @param eventDatas
	 * @return
	 */
	private Set<Accounts> setupAccountsForGroup(Long groupid) {
		LOGGER.info("Entering setupAccountsForGroup()");
		Set<Accounts> accounts = null;

		if (groupid != null && groupid.longValue() != -99 && groupid.longValue() != 0) {
			final Groups group = groupDAO.getGroup(groupid.longValue());
			accounts = group.getAccounts();
		}

		LOGGER.info("Exiting setupAccountsForGroup()");
		return accounts;
	}

	/**
	 * 
	 * @param buyOrder
	 * @param rotationid
	 * @param event
	 * @param eventData
	 * @param account
	 * @return
	 */
	private AccountEvent populateAccountEvent(Boolean buyOrder, Long rotationid, String lineindicator, String line, BaseRecordEvent event, Accounts account) {
		LOGGER.info("Entering populateAccountEvent()");

		final AccountEvent accountEvent = new AccountEvent();
		if ("spread".equals(event.getEventtype())) {
			accountEvent.setSpreadid(event.getId());
			accountEvent.setMaxspreadamount(account.getSpreadlimitamount());
			accountEvent.setSpreadindicator(lineindicator);
			if ("-".equals(lineindicator)) {
				accountEvent.setSpread(Float.parseFloat("-" + Float.parseFloat(line)));
			} else {
				line = line.replace("+", "");
				accountEvent.setSpread(Float.parseFloat(line));
			}
			accountEvent.setSpreadjuice(Float.parseFloat("-110"));
		}

		accountEvent.setAccountid(account.getId());
		accountEvent.setAttempts(event.getAttempts());
		if (!buyOrder) {
			accountEvent.setAmount("100");
		}
		accountEvent.setGroupid(event.getGroupid());
		accountEvent.setEventdatetime(event.getEventdatetime());
		accountEvent.setEventid(rotationid.intValue());
		accountEvent.setEventname(event.getEventname());
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

		LOGGER.info("Exiting populateAccountEvent()");
		return accountEvent;
	}

	/**
	 * 
	 * @param event
	 * @param orderAmount
	 * @param maxAmount
	 */
	private void setupBuyOrder(List<AccountEvent> accountEvents, BaseRecordEvent event, Double orderAmount, Double maxAmount) {
		LOGGER.info("Entering setupBuyOrder()");
		LOGGER.debug("Event: " + event);
		
		// First check to see if it's an account or group
		try {
			// Make sure there is at least one
			if (accountEvents != null && accountEvents.size() > 0) {
				for (AccountEvent ae : accountEvents) {
					LOGGER.debug("ae: " + ae);
				}
				LOGGER.debug("orderAmount: " + orderAmount);
				LOGGER.debug("maxAmount: " + maxAmount);
//				final BuyOrderResource buyOrderResource = new BuyOrderResource(accountEvents, orderAmount, maxAmount);
				placeBuyOrder(accountEvents, orderAmount, maxAmount);
			}
		} catch (Exception be) {
			LOGGER.error("Cannot get account event for " + event.getId(), be);
		}

		LOGGER.info("Exiting setupBuyOrder()");
	}

	/**
	 * 
	 * @param sportType
	 * @return
	 */
	private static String getGameType(String sportType) {
		String gType = "";

		if ("nfllines".equals(sportType) || "nflfirst".equals(sportType) ||
				"nflsecond".equals(sportType)) {
			gType = "NFL";
		} else if ("ncaaflines".equals(sportType) || "ncaaffirst".equals(sportType) ||
				"ncaafsecond".equals(sportType)) {
			gType = "NCAAF";
		} else if ("nbalines".equals(sportType) || "nbafirst".equals(sportType) ||
				"nbasecond".equals(sportType)) {
			gType = "NBA";
		} else if ("ncaablines".equals(sportType) || "ncaabfirst".equals(sportType) ||
				"ncaabsecond".equals(sportType)) {
			gType = "NCAAB";
		} else if ("nhllines".equals(sportType) || "nchlfirst".equals(sportType) ||
				"nhlsecond".equals(sportType)) {
			gType = "NHL";
		} else if ("wnbalines".equals(sportType) || "wnbafirst".equals(sportType) ||
				"wnbasecond".equals(sportType)) {
			gType = "WNBA";
		}

		return gType;
	}

	/**
	 * 
	 * @param sportType
	 * @return
	 */
	private static String getBetType(String sportType) {
		String bType = "";

		if (sportType != null) {
			if (sportType.contains("lines")) {
				bType = "Game";
			} else if (sportType.contains("first")) {
				bType = "1H";
			} else if (sportType.contains("second")) {
				bType = "2H";
			} else if (sportType.contains("third")) {
				bType = "3H";
			}
		}

		return bType;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		LOGGER.error("Entering run()");

		try {
			final EventsPackage eventsPackage = eventsDAO.getSport("ncaab");

			// First, get the event data
			for (SpreadLastThree spreadLastThree : spreadLastThreeList) {
				EventPackage ep = null;
				for (EventPackage eventPackage : eventsPackage.getEvents()) {
					final String team1 = eventPackage.getTeamone().getTeam();
					final String roadteam = spreadLastThree.getRoadteam();
					if (roadteam.equals(team1)) {
						ep = eventPackage;
						LOGGER.debug("ep: " + ep);
						break;
					}
				}

				if (ep != null) {
					// Spread
					setupSpreadEvent(spreadLastThree, ep);
				} else {
					LOGGER.error("ep: " + ep);
				}
			}
		} catch (BatchException be) {
			LOGGER.error(be.getMessage(), be);
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		LOGGER.error("Exiting run()");
	}

	/**
	 * 
	 * @param accountEvents
	 * @param orderAmount
	 * @param maxAmount
	 */
	private void placeBuyOrder(List<AccountEvent> accountEvents, Double orderAmount, Double maxAmount) {
		try {
			// Check to make sure the account list is not empty
			if (accountEvents != null && !accountEvents.isEmpty()) {
				// Setup the amounts
				// How many accounts do we need to push in to?
				int numAccountsNeeded = orderAmount.intValue() / maxAmount.intValue();
				LOGGER.debug("numAccountsNeeded: " + numAccountsNeeded);

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

						while (itr.hasNext() && !accountDone) {
							countItr++;
							final AccountEvent accountEvent = itr.next();

							LOGGER.debug("Random #: " + n);
							// We have a match
							if (countItr == n) {
								try {
									// Setup the account event
									recordEventDAO.setupAccountEvent(accountEvent);

									// Was the transaction successful?
									boolean wasSuccessful = callAWS(accountEvent.getId());
									LOGGER.error("wasSuccessful: " + wasSuccessful);

									if (wasSuccessful) {
										acctsSuccess.add(accountEvent.getId());

										// Check if we are all done
										if (acctsSuccess.size() == numAccountsNeeded) {
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
					final AccountEvent ae = recordEventDAO.getAccountEvent(accountEventId);
					
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
					final AccountEvent ae = recordEventDAO.getAccountEvent(accountEventId);
					
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