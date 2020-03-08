/**
 * 
 */
package com.ticketadvantage.services.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ticketadvantage.services.dao.EventsDAO;
import com.ticketadvantage.services.errorhandling.AppErrorCodes;
import com.ticketadvantage.services.errorhandling.AppErrorMessage;
import com.ticketadvantage.services.errorhandling.AppException;
import com.ticketadvantage.services.model.EventsPackage;

/**
 * @author jmiller
 *
 */
@Path("/events")
@Service
public class EventsResource {
	private static final Logger LOGGER = Logger.getLogger(EventsResource.class);

	@Autowired
	private EventsDAO eventsDAO;

	/**
	 * 
	 */
	public EventsResource() {
		super();
		LOGGER.debug("Entering EventsResource()");
		LOGGER.debug("Exiting EventsResource()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * 
	 * @param eventType
	 * @return
	 * @throws AppException
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public EventsPackage events(@QueryParam("eventtype") String eventType) throws AppException {
		LOGGER.info("Entering events()");
		LOGGER.debug("eventtype: " + eventType);
		EventsPackage eventsPackage = null;
		try {
			// Get all events for event type
			eventsPackage = eventsDAO.getEvents(eventType);
		} catch (AppException ae) {
			LOGGER.error("AppException in events()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in getUser()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}
		LOGGER.debug("eventsPackage: " + eventsPackage);
		LOGGER.info("Exiting events()");
		return eventsPackage;
	}
}