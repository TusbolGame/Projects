/**
 * 
 */
package com.ticketadvantage.services.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ticketadvantage.services.dao.AccountEventDAO;
import com.ticketadvantage.services.errorhandling.AppErrorCodes;
import com.ticketadvantage.services.errorhandling.AppErrorMessage;
import com.ticketadvantage.services.errorhandling.AppException;

/**
 * @author calderson
 *
 */
@Path("/accountevent")
@Service
public class AccountEventResource {
	private static final Logger LOGGER = Logger.getLogger(AccountEventResource.class);

	@Autowired
	private AccountEventDAO accountEventDAO;

	/**
	 * 
	 */
	public AccountEventResource() {
		super();
		LOGGER.debug("Entering AccountEventResource()");
		LOGGER.debug("Exiting AccountEventResource()");
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
	@Path("/userid/{userid}/count")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public String getAccountEventsCountForUser(@PathParam("userid") Long userid, 		
			@QueryParam("start_date") String startDateStr,
			@QueryParam("end_date") String endDateStr) throws AppException {
		LOGGER.info("Entering getAccountEventsCountForUser()");
		LOGGER.debug("userId: " + userid);
		
		Long count = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date startDate  = format.parse(startDateStr);
			Date endDate 	= format.parse(endDateStr);
	
			count = accountEventDAO.getAccountEventsCount(userid, startDate, endDate);
		
		} catch (AppException ae) {
			LOGGER.error("AppException in accountEvents()", ae);
			throw ae;
		} catch (ParseException e) {
			LOGGER.error("ParseException in accountEvents()", e);
			throw new AppException("Could not parse date");
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in accountEvents()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
			AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}
		
		LOGGER.info("Exiting getAccountEventsCountForUser()");
		return count.toString();
	}
	
	/**
	 * 
	 * @param userid
	 * @return
	 * @throws AppException
	 */
	@GET
	@Path("/userid/{userid}/amount")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public String getAccountEventsAmountForUser(@PathParam("userid") Long userid, 		
			@QueryParam("start_date") String startDateStr,
			@QueryParam("end_date") String endDateStr) throws AppException {
		LOGGER.info("Entering getAccountEventsTotalForUser()");
		LOGGER.debug("userId: " + userid);
		
		BigDecimal total = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
		Date startDate  = format.parse(startDateStr);
		Date endDate 	= format.parse(endDateStr);

		total = accountEventDAO.getAccountEventsAmount(userid, startDate, endDate);
		
		} catch (AppException ae) {
			LOGGER.error("AppException in accountEvents()", ae);
			throw ae;
		} catch (ParseException e) {
			LOGGER.error("ParseException in accountEvents()", e);
			throw new AppException("Could not parse date");
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in accountEvents()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
			AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}
		
		LOGGER.info("Exiting getAccountEventsTotalForUser()");
		return total.toString();
	}
}