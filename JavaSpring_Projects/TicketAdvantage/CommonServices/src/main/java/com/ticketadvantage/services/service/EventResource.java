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
import com.ticketadvantage.services.model.EventPackage;

/**
 * @author jmiller
 *
 */
@Path("/event")
@Service
public class EventResource {
	private static final Logger LOGGER = Logger.getLogger(EventResource.class);

	@Autowired
	private EventsDAO eventsDAO;

	/**
	 * 
	 */
	public EventResource() {
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
	public EventPackage event(@QueryParam("eventid") Long eventid) throws AppException {
		LOGGER.info("Entering event()");
		LOGGER.debug("eventid: " + eventid);
		EventPackage eventPackage = null;
		try {
			// Get all events for event type
			eventPackage = eventsDAO.getEvent(eventid);
		} catch (AppException ae) {
			LOGGER.error("AppException in event()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in event()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}
		LOGGER.debug("EventPackage: " + eventPackage);
		LOGGER.info("Exiting event()");
		return eventPackage;
	}
}