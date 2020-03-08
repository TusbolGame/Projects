/**
 * 
 */
package com.ticketadvantage.services.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ticketadvantage.services.dao.AccountDAO;
import com.ticketadvantage.services.dao.EventsDAO;
import com.ticketadvantage.services.dao.GroupDAO;
import com.ticketadvantage.services.dao.RecordEventDAO;
import com.ticketadvantage.services.dao.ReportsDAO;
import com.ticketadvantage.services.errorhandling.AppErrorCodes;
import com.ticketadvantage.services.errorhandling.AppErrorMessage;
import com.ticketadvantage.services.errorhandling.AppException;
import com.ticketadvantage.services.model.SpreadLastThree;

/**
 * @author jmiller
 *
 */
@Path("/playncaablastthree")
@Service
public class PlayNcaabLastThreeResource {
	private static final Logger LOGGER = Logger.getLogger(PlayNcaabLastThreeResource.class);

	@Autowired
	private ReportsDAO reportsDAO;

	@Autowired
	private RecordEventDAO recordEventDAO;

	@Autowired
	private AccountDAO accountDAO;

	@Autowired
	private GroupDAO groupDAO;

	@Autowired
	private EventsDAO eventsDAO;

	@PersistenceContext(name = "entityManager")
	private EntityManager entityManager;

	/**
	 * 
	 */
	public PlayNcaabLastThreeResource() {
		super();
		LOGGER.debug("Entering PlayNcaabLastThreeResource()");
		LOGGER.debug("Exiting PlayNcaabLastThreeResource()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * @return the reportsDAO
	 */
	public ReportsDAO getReportsDAO() {
		return reportsDAO;
	}

	/**
	 * @param reportsDAO the reportsDAO to set
	 */
	public void setReportsDAO(ReportsDAO reportsDAO) {
		this.reportsDAO = reportsDAO;
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
	 * 
	 * @param response
	 * @return
	 * @throws AppException
	 */
	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public void playLastThree(@Context HttpServletResponse response) throws AppException {
		LOGGER.info("Entering playLastThree()");

		try {
			// Find all data based on query
			final List<SpreadLastThree> spreadLastThreeList = reportsDAO.getSpreadsForNextDay();
			LOGGER.error("Got next days listing");
			for (SpreadLastThree sp : spreadLastThreeList) {
				LOGGER.error("SpreadLastThree: " + sp);
			}
			final PlaceSpreadLastThreeResource placeSpreadLastThreeResource = 
					new PlaceSpreadLastThreeResource(recordEventDAO, accountDAO, groupDAO, eventsDAO, entityManager, spreadLastThreeList);
		} catch (AppException ae) {
			LOGGER.error("AppException in playLastThree()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in playLastThree()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}
		
		LOGGER.info("Exiting playLastThree()");
	}
}