/**
 * 
 */
package com.ticketadvantage.services.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ticketadvantage.services.dao.AccountDAO;
import com.ticketadvantage.services.dao.EventsDAO;
import com.ticketadvantage.services.dao.GroupDAO;
import com.ticketadvantage.services.errorhandling.AppErrorCodes;
import com.ticketadvantage.services.errorhandling.AppErrorMessage;
import com.ticketadvantage.services.errorhandling.AppException;
import com.ticketadvantage.services.model.Accounts;
import com.ticketadvantage.services.model.EventPackage;
import com.ticketadvantage.services.model.Groups;
import com.ticketadvantage.services.model.PreviewInput;
import com.ticketadvantage.services.model.PreviewOutput;
import com.ticketadvantage.services.model.PreviewRequest;
import com.ticketadvantage.services.model.PreviewResponse;

/**
 * @author jmiller
 *
 */
@Path("/previewtransaction")
@Service
public class PreviewTransactionResource {
	private static final Logger LOGGER = Logger.getLogger(PreviewTransactionResource.class);

	@Autowired
	private AccountDAO accountDAO;

	@Autowired
	private GroupDAO groupDAO;

	@Autowired
	private EventsDAO eventsDAO;

	@PersistenceContext(name = "entityManager")
	private EntityManager entityManager;

	private Set<PreviewOutput> previewOutputData;

	/**
	 * 
	 */
	public PreviewTransactionResource() {
		super();
		LOGGER.debug("Entering PreviewTransactionResource()");
		LOGGER.debug("Exiting PreviewTransactionResource()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * 
	 * @param previewOutput
	 */
	public void addPreviewOutput(PreviewOutput previewOutput) {
		previewOutputData.add(previewOutput);
	}

	/**
	 * 
	 * @param previewRequest
	 * @return
	 * @throws AppException
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Set<PreviewResponse> previewTransaction(PreviewRequest previewRequest) throws AppException {
		LOGGER.info("Entering previewTransaction()");
		LOGGER.debug("PreviewRequest: " + previewRequest);
		previewOutputData = new HashSet<PreviewOutput>();
		final Set<PreviewResponse> previewData = new HashSet<PreviewResponse>();

		try {
			// First, get the event data
			final EventPackage ep = eventsDAO.getEvent(previewRequest.getRotationid().longValue());
			if (ep == null) {
				LOGGER.error("Rotaion ID not found");
				throw new AppException(500, AppErrorCodes.EVENTS_EXCEPTION,  
						AppErrorMessage.EVENTS_EXCEPTION);
			}

			// Setup the teams
			Set<Long> accounts = null;
			final Set<Long> groups = previewRequest.getGroupids();
			if (groups != null && groups.size() > 0) {
				accounts = new HashSet<Long>();
				for (Long gid : groups) {
					final Groups group = groupDAO.getGroup(gid);
					final Set<Accounts> groupAccounts = group.getAccounts();
					for (Accounts account : groupAccounts) {
						accounts.add(account.getId());
					}
				}
			} else {
				accounts = previewRequest.getAccountids();
			}

			// Make sure there is at least one
			if (accounts != null && accounts.size() > 0) {
				final ExecutorService executor = Executors.newFixedThreadPool(accounts.size());
				final List<Future<?>> futures = new ArrayList<Future<?>>();
				for (Long accountId : accounts) {
					// Get the account information
					final Accounts account = accountDAO.getAccount(accountId);

					// Setup the preview input
					final PreviewInput previewInput = new PreviewInput();
					previewInput.setRotationid(setupRotationid(previewRequest.getSporttype(), previewRequest.getRotationid()));
					previewInput.setSporttype(previewRequest.getSporttype());
					previewInput.setLineindicator(previewRequest.getLineindicator());
					previewInput.setLinetype(previewRequest.getLinetype());					
					previewInput.setTeam1(ep.getTeamone().getTeam());
					previewInput.setTeam2(ep.getTeamtwo().getTeam());
					previewInput.setAccountid(account.getId());
					previewInput.setAccountname(account.getName());
					previewInput.setIsmobile(account.getIsmobile());
					previewInput.setPassword(account.getPassword());
					previewInput.setProxyname(account.getProxylocation());
					previewInput.setShowrequestresponse(account.getShowrequestresponse());
					previewInput.setSitetype(account.getSitetype());
					previewInput.setTimezone(account.getTimezone());
					previewInput.setUsername(account.getUsername());
					previewInput.setUrl(account.getUrl());
					LOGGER.debug("PreviewInput: " + previewInput);

//					if (GlobalProperties.isLocal()) {
					if (true) {
						final PreviewEventResource previewEventResource = new PreviewEventResource();
						previewEventResource.setPreviewInput(previewInput);
						previewEventResource.setPreviewTransactionResource(this);
						final Runnable worker = previewEventResource;
						final Future<?> f = executor.submit(worker);
						futures.add(f);
					} else {
						final AWSPreviewEventResource awsPreviewEventResource = new AWSPreviewEventResource();
						awsPreviewEventResource.setPreviewInput(previewInput);
						final Runnable worker = awsPreviewEventResource;
						final Future<?> f = executor.submit(worker);
						futures.add(f);
					}
				}

				// A) Await all runnables to be done (blocking)
				for (Future<?> future : futures)
					future.get(); // get will block until the future is done

				// B) Check if all runnables are done (non-blocking)
				boolean allDone = true;
				for (Future<?> future : futures) {
					allDone &= future.isDone(); // check if future is done
					LOGGER.debug("allDone: " + allDone);
				}

				// Shut down the executor
				executor.shutdown();

				if (!previewOutputData.isEmpty()) {
					for (PreviewOutput previewOutput : previewOutputData) {
						final PreviewResponse previewResponse = new PreviewResponse();
						previewResponse.setAccountid(previewOutput.getAccountid());
						previewResponse.setAccountname(previewOutput.getAccountname());
						previewResponse.setAmount(previewRequest.getAmount());
						previewResponse.setJuice(previewOutput.getJuice());
						previewResponse.setJuiceindicator(previewOutput.getJuiceindicator());
						previewResponse.setLine(previewOutput.getLine());
						previewResponse.setLineindicator(previewOutput.getLineindicator());
						previewResponse.setLinetype(previewRequest.getLinetype());
						previewResponse.setRotationid(previewRequest.getRotationid());
						previewResponse.setSporttype(previewRequest.getSporttype());
						previewData.add(previewResponse);
					}
				}
			}
		} catch (AppException ae) {
			LOGGER.error("AppException in recordSpreadEvent()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in recordSpreadEvent()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}

		LOGGER.info("Exiting previewTransaction()");
		return previewData;
	}

	/**
	 * 
	 * @param rotationid
	 * @return
	 */
	private static Integer setupRotationid(String sportType, Integer rotationid) {
		Integer rotid = new Integer(0);
		if (rotationid != null) {
			if (sportType.contains("lines")) {
				rotid = rotationid;
			} else if (sportType.contains("first")) {
				rotid = 1000 + rotationid;
			} else if (sportType.contains("second")) {
				rotid = 2000 + rotationid;
			} else if (sportType.contains("third")) {
				rotid = 3000 + rotationid;
			}
		}

		return rotid;
	}
}