/**
 * 
 */
package com.ticketadvantage.services.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ticketadvantage.services.dao.AccountEventFinalDAO;
import com.ticketadvantage.services.errorhandling.AppErrorCodes;
import com.ticketadvantage.services.errorhandling.AppErrorMessage;
import com.ticketadvantage.services.errorhandling.AppException;
import com.ticketadvantage.services.model.AccountEventFinal;

/**
 * @author calderson
 *
 */
@Path("/accounteventfinal")
@Service
public class AccountEventFinalResource {
	private static final Logger LOGGER = Logger.getLogger(AccountEventFinalResource.class);

	@Autowired
	private AccountEventFinalDAO accountEventFinalDAO;

	/**
	 * 
	 */
	public AccountEventFinalResource() {
		super();
		LOGGER.debug("Entering AccountEventFinalResource()");
		LOGGER.debug("Exiting AccountEventFinalResource()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * 
	 * @return
	 * @throws AppException
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Set<AccountEventFinal> getAccountEventFinals() throws AppException {
		LOGGER.info("Entering getAccountEventFinals()");
		Set<AccountEventFinal> retValue = null;
		try {
			// Find all accounts
			retValue = accountEventFinalDAO.findAll();
		} catch (AppException ae) {
			LOGGER.error("AppException in getAccountEventFinals()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in getAccountEventFinals()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}
		LOGGER.debug("AccountEventFinals: " + retValue);
		LOGGER.info("Exiting getAccountEventsFinals()");
		return retValue;
	}

	/**
	 * 
	 * @param userid
	 * @param weekstartdate
	 * @return
	 * @throws AppException
	 */
	@GET
	@Path("/userid/{userid}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<AccountEventFinal> getAccountEventFinalsForUser(@PathParam("userid") Long userid, @QueryParam("week_start_date") String weekStartDateStr) throws AppException {
		LOGGER.info("Entering getAccountEventFinalsForUser()");
		LOGGER.debug("userid: " + userid);
		List<AccountEventFinal> retValue = null;
		try {
			if (StringUtils.isEmpty(weekStartDateStr)) {
				retValue = accountEventFinalDAO.getUserAccountEventFinals(userid, null);
			} else {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Date weekStartDate  = format.parse(weekStartDateStr);
				
				retValue = accountEventFinalDAO.getUserAccountEventFinals(userid, weekStartDate);
			}
			// Get all user billings for user
			
		} catch (AppException ae) {
			LOGGER.error("AppException in getAccountEventFinalsForUser()", ae);
			throw ae;
		} catch (ParseException e) {
			LOGGER.error("ParseException in getAccountEventFinalsForUser()", e);
			throw new AppException("Could not parse weekStartDate");
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in getAccountEventFinalsForUser()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}
		LOGGER.debug("Accounts: " + retValue);
		LOGGER.info("Exiting getAccountEventFinalsForUser()");
		return retValue;
	}

	/**
	 * 
	 * @return
	 * @throws AppException
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public AccountEventFinal createAccountEventFinal(AccountEventFinal accountEventFinal) throws AppException {
		LOGGER.info("Entering createAccountEventFinal()");
		LOGGER.debug("AccountEventFinal: " + accountEventFinal);
		AccountEventFinal retValue = null;
		try {
			// Create the account for the user
			retValue = accountEventFinalDAO.persist(accountEventFinal);
		} catch (AppException ae) {
			LOGGER.error("AppException in createAccountEventFinal()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in createAccountEventFinal()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}
		LOGGER.debug("Accounts: " + retValue);
		LOGGER.info("Exiting createAccountEventFinal()");
		return retValue;
	}

	/**
	 * 
	 * @param accountEventFinal
	 * @return
	 * @throws AppException
	 */
	@POST
	@Path("/update")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public AccountEventFinal updateAccountEventFinal(AccountEventFinal accountEventFinal) throws AppException {
		LOGGER.info("Entering updateAccountEventFinal()");
		LOGGER.debug("AccountEventFinal: " + accountEventFinal);
		AccountEventFinal retValue = null;
		try {
			// Update the account
			retValue = accountEventFinalDAO.update(accountEventFinal);
		} catch (AppException ae) {
			LOGGER.error("AppException in updateAccountEventFinal()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in updateAccountEventFinal()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}
		LOGGER.debug("AccountEventFinal: " + retValue);
		LOGGER.info("Exiting updateAccountEventFinal()");
		return retValue;
	}

	/**
	 * 
	 * @param id
	 * @return
	 * @throws AppException
	 */
	@DELETE
	@Path("/delete/{id}")
	public void deleteAccountEventFinal(@PathParam("id") Long id) throws AppException {
		LOGGER.info("Entering deleteAccountEventFinal()");
		LOGGER.debug("Account ID: " + id);
		try {
			// Create the account
			accountEventFinalDAO.delete(id);
		} catch (AppException ae) {
			LOGGER.error("AppException in deleteAccountEventFinal()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in deleteAccountEventFinal()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}
		LOGGER.info("Exiting deleteAccountEventFinal()");
	}
}