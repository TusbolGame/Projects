/**
 * 
 */
package com.ticketadvantage.services.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ticketadvantage.services.dao.EventsDAO;
import com.ticketadvantage.services.errorhandling.AppErrorCodes;
import com.ticketadvantage.services.errorhandling.AppErrorMessage;
import com.ticketadvantage.services.errorhandling.AppException;
import com.ticketadvantage.services.model.CommittedEvents;

/**
 * @author jmiller
 *
 */
@Path("/committedevents")
@Service
public class CommittedEventsResource {
	private static final Logger LOGGER = Logger.getLogger(CommittedEventsResource.class);
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM-dd-yyyy");

	@Autowired
	private EventsDAO eventsDAO;

	/**
	 * 
	 */
	public CommittedEventsResource() {
		super();
		LOGGER.debug("Entering CommittedEventsResource()");
		LOGGER.debug("Exiting CommittedEventsResource()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * 
	 * @param userid
	 * @return
	 * @throws AppException
	 */
	@GET
	@Path("/userid/{userid}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public CommittedEvents getAllEventsByUser(@PathParam("userid") Long userid) throws AppException {
		LOGGER.info("Entering getAllEventsByUser()");
		LOGGER.debug("userid: " + userid);
		CommittedEvents retValue = null;
		try {
			// Find all accounts
			retValue = eventsDAO.getAllEventsByUser(userid);
		} catch (AppException ae) {
			LOGGER.error("AppException in getAllEventsByUser()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in getAllEventsByUser()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}
		LOGGER.debug("CommittedEvents: " + retValue);
		LOGGER.info("Exiting getAllEventsByUser()");
		return retValue;
	}

	/**
	 * 
	 * @param userid
	 * @param fromdate
	 * @param todate
	 * @param groupid
	 * @param accountid
	 * @return
	 * @throws AppException
	 */
	@GET
	@Path("/userbyfilter")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public CommittedEvents getEventsByUserByFilter(@QueryParam("userid") Long userid, @QueryParam("fromdate") String fromdate, @QueryParam("todate") String todate, @QueryParam("groupid") Long groupid, @QueryParam("accountid") Long accountid) throws AppException {
		LOGGER.info("Entering getEventsByUserByFilter()");
		LOGGER.debug("userid: " + userid);
		LOGGER.debug("fromdate: " + fromdate);
		LOGGER.debug("todate: " + todate);
		LOGGER.debug("groupid: " + groupid);
		LOGGER.debug("accountid: " + accountid);
		CommittedEvents retValue = null;

		try {
			String timeZone = "ET";
			int index = fromdate.indexOf(" ");
			if (index != -1) {
				timeZone = fromdate.substring(index + 1).trim();
			}

			TimeZone zone = TimeZone.getTimeZone("America/New_York");
			DATE_FORMAT.setTimeZone(zone);

			if ("CT".equals(timeZone)) {
				zone = TimeZone.getTimeZone("America/Chicago");
				DATE_FORMAT.setTimeZone(zone);
			} else if ("MT".equals(timeZone)) {
				zone = TimeZone.getTimeZone("America/Denver");
				DATE_FORMAT.setTimeZone(zone);
			} else if ("PT".equals(timeZone)) {
				zone = TimeZone.getTimeZone("America/Los_Angeles");
				DATE_FORMAT.setTimeZone(zone);
			}

			// Now convert the date from mm-dd-yyyy format
			final Date fromDate = DATE_FORMAT.parse(fromdate);
			final Calendar c = Calendar.getInstance();
			c.setTime(DATE_FORMAT.parse(todate));
			c.add(Calendar.DATE, 1);  // number of days to add
			final Date toDate = c.getTime();

			// Find all accounts
			retValue = eventsDAO.getEventsByUserByFilter(userid, fromDate, toDate, groupid, accountid, timeZone);
		} catch (AppException ae) {
			LOGGER.error("AppException in getEventsByUserByFilter()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in getEventsByUserByFilter()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}
		LOGGER.debug("CommittedEvents: " + retValue);

		LOGGER.info("Exiting getEventsByUserByFilter()");
		return retValue;
	}

	/**
	 * 
	 * @param userid
	 * @param fromdate
	 * @param todate
	 * @param groupid
	 * @param accountid
	 * @param response
	 * @return
	 * @throws AppException
	 */
	@GET
	@Path("/excelfilter")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getEventsByUserExcel(@QueryParam("userid") Long userid, @QueryParam("fromdate") String fromdate, @QueryParam("todate") String todate, @QueryParam("groupid") Long groupid, @QueryParam("accountid") Long accountid, @Context HttpServletResponse response) throws AppException {
		LOGGER.info("Entering getEventsByUserExcel()");
		LOGGER.debug("userid: " + userid);
		LOGGER.debug("fromdate: " + fromdate);
		LOGGER.debug("todate: " + todate);
		LOGGER.debug("groupid: " + groupid);
		LOGGER.debug("accountid: " + accountid);
		StreamingOutput output = null;

		try {
			String timeZone = "ET";
			int index = fromdate.indexOf(" ");
			if (index != -1) {
				timeZone = fromdate.substring(index + 1).trim();
			}

			if ("ET".equals(timeZone)) {
				TimeZone zone = TimeZone.getTimeZone("America/New_York");
				DATE_FORMAT.setTimeZone(zone);
			} else if ("CT".equals(timeZone)) {
				TimeZone zone = TimeZone.getTimeZone("America/Chicago");
				DATE_FORMAT.setTimeZone(zone);
			} else if ("MT".equals(timeZone)) {
				TimeZone zone = TimeZone.getTimeZone("America/Denver");
				DATE_FORMAT.setTimeZone(zone);
			} else if ("PT".equals(timeZone)) {
				TimeZone zone = TimeZone.getTimeZone("America/Los_Angeles");
				DATE_FORMAT.setTimeZone(zone);
			} else {
				TimeZone zone = TimeZone.getTimeZone("America/New_York");
				DATE_FORMAT.setTimeZone(zone);				
			}

			// Now convert the date from mm-dd-yyyy format
			final Date fromDate = DATE_FORMAT.parse(fromdate);
			final Calendar c = Calendar.getInstance();
			c.setTime(DATE_FORMAT.parse(todate));
			c.add(Calendar.DATE, 1);  // number of days to add
			final Date toDate = c.getTime();

			// Find all data based on query
			output = eventsDAO.createSpreadSheetByUserByFilter(userid, fromDate, toDate, groupid, accountid, timeZone, false);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-disposition", "attachment; filename=\"" + "games" + ".xlsx\"");
//			outStream.close();
		} catch (AppException ae) {
			LOGGER.error("AppException in getEventsByUserByFilter()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in getEventsByUserByFilter()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}
		
		LOGGER.info("Exiting getEventsByUserExcel()");
		return Response.ok(output).header("content-disposition","attachment; filename = export.xlsx").header("content-type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet").build();
	}

	@GET
	@Path("/excelfilterall")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getAllEventsByUserExcel(@QueryParam("userid") Long userid, @QueryParam("fromdate") String fromdate, @QueryParam("todate") String todate, @QueryParam("groupid") Long groupid, @QueryParam("accountid") Long accountid, @Context HttpServletResponse response) throws AppException {
		LOGGER.info("Entering getAllEventsByUserExcel()");
		LOGGER.debug("userid: " + userid);
		LOGGER.debug("fromdate: " + fromdate);
		LOGGER.debug("todate: " + todate);
		LOGGER.debug("groupid: " + groupid);
		LOGGER.debug("accountid: " + accountid);
		StreamingOutput output = null;

		try {
			String timeZone = "ET";
			int index = fromdate.indexOf(" ");
			if (index != -1) {
				timeZone = fromdate.substring(index + 1).trim();
			}

			if ("ET".equals(timeZone)) {
				TimeZone zone = TimeZone.getTimeZone("America/New_York");
				DATE_FORMAT.setTimeZone(zone);
			} else if ("CT".equals(timeZone)) {
				TimeZone zone = TimeZone.getTimeZone("America/Chicago");
				DATE_FORMAT.setTimeZone(zone);
			} else if ("MT".equals(timeZone)) {
				TimeZone zone = TimeZone.getTimeZone("America/Denver");
				DATE_FORMAT.setTimeZone(zone);
			} else if ("PT".equals(timeZone)) {
				TimeZone zone = TimeZone.getTimeZone("America/Los_Angeles");
				DATE_FORMAT.setTimeZone(zone);
			} else {
				TimeZone zone = TimeZone.getTimeZone("America/New_York");
				DATE_FORMAT.setTimeZone(zone);
			}

			// Now convert the date from mm-dd-yyyy format
			final Date fromDate = DATE_FORMAT.parse(fromdate);
			final Calendar c = Calendar.getInstance();
			c.setTime(DATE_FORMAT.parse(todate));
			c.add(Calendar.DATE, 1);  // number of days to add
			final Date toDate = c.getTime();

			// Find all data based on query
			output = eventsDAO.createSpreadSheetByUserByFilter(userid, fromDate, toDate, groupid, accountid, timeZone, true);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-disposition", "attachment; filename=\"" + "games" + ".xlsx\"");
		} catch (AppException ae) {
			LOGGER.error("AppException in getEventsByUserByFilter()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in getEventsByUserByFilter()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}
		
		LOGGER.info("Exiting getAllEventsByUserExcel()");
		return Response.ok(output).header("content-disposition","attachment; filename = export.xlsx").header("content-type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet").build();
	}

	/**
	 * 
	 * @param id
	 * @return
	 * @throws AppException
	 */
	@GET
	@Path("/spreadevent/{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public CommittedEvents getSpreadEventById(@PathParam("id") Long id) throws AppException {
		LOGGER.info("Entering getSpreadEventById()");
		LOGGER.debug("id: " + id);
		CommittedEvents retValue = null;

		try {
			// Find all accounts
			retValue = eventsDAO.getSpreadEventById(id);
		} catch (AppException ae) {
			LOGGER.error("AppException in getSpreadEventById()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in getSpreadEventById()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}

		LOGGER.debug("CommittedEvents: " + retValue);
		LOGGER.info("Exiting getSpreadEventById()");
		return retValue;
	}

	/**
	 * 
	 * @param id
	 * @return
	 * @throws AppException
	 */
	@GET
	@Path("/totalevent/{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public CommittedEvents getTotalEventById(@PathParam("id") Long id) throws AppException {
		LOGGER.info("Entering getTotalEventById()");
		LOGGER.debug("id: " + id);
		CommittedEvents retValue = null;

		try {
			// Find all accounts
			retValue = eventsDAO.getTotalEventById(id);
		} catch (AppException ae) {
			LOGGER.error("AppException in getTotalEventById()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in getTotalEventById()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}

		LOGGER.debug("CommittedEvents: " + retValue);
		LOGGER.info("Exiting getTotalEventById()");
		return retValue;
	}

	/**
	 * 
	 * @param id
	 * @return
	 * @throws AppException
	 */
	@GET
	@Path("/mlevent/{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public CommittedEvents getMlEventById(@PathParam("id") Long id) throws AppException {
		LOGGER.info("Entering getMlEventById()");
		LOGGER.debug("id: " + id);
		CommittedEvents retValue = null;

		try {
			// Find all accounts
			retValue = eventsDAO.getMlEventById(id);
		} catch (AppException ae) {
			LOGGER.error("AppException in getMlEventById()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in getMlEventById()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}

		LOGGER.debug("CommittedEvents: " + retValue);
		LOGGER.info("Exiting getMlEventById()");
		return retValue;
	}
}