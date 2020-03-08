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

import com.ticketadvantage.services.dao.UserBillingDAO;
import com.ticketadvantage.services.errorhandling.AppErrorCodes;
import com.ticketadvantage.services.errorhandling.AppErrorMessage;
import com.ticketadvantage.services.errorhandling.AppException;
import com.ticketadvantage.services.model.UserBilling;

/**
 * @author calderson
 *
 */
@Path("/userbilling")
@Service
public class UserBillingResource {
	private static final Logger LOGGER = Logger.getLogger(UserBillingResource.class);

	@Autowired
	private UserBillingDAO userBillingDAO;

	/**
	 * 
	 */
	public UserBillingResource() {
		super();
		LOGGER.debug("Entering UserBillingResource()");
		LOGGER.debug("Exiting UserBillingResource()");
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
	public Set<UserBilling> getUserBillings() throws AppException {
		LOGGER.info("Entering getUserBillings()");
		Set<UserBilling> retValue = null;
		try {
			// Find all accounts
			retValue = userBillingDAO.findAll();
		} catch (AppException ae) {
			LOGGER.error("AppException in getUserBillings()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in getUserBillings()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}
		LOGGER.debug("UserBillings: " + retValue);
		LOGGER.info("Exiting getUserBillings()");
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
	public List<UserBilling> getUserBillingsForUser(@PathParam("userid") Long userid, @QueryParam("week_start_date") String weekStartDateStr) throws AppException {
		LOGGER.info("Entering getUserBillingsForUser()");
		LOGGER.debug("userid: " + userid);
		List<UserBilling> retValue = null;
		try {
			if (StringUtils.isEmpty(weekStartDateStr)) {
				retValue = userBillingDAO.getUserBillingsForUser(userid, null);
			} else {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Date weekStartDate  = format.parse(weekStartDateStr);
				
				retValue = userBillingDAO.getUserBillingsForUser(userid, weekStartDate);
			}
			// Get all user billings for user
			
		} catch (AppException ae) {
			LOGGER.error("AppException in getUserBillingsForUser()", ae);
			throw ae;
		} catch (ParseException e) {
			LOGGER.error("ParseException in getUserBillingsForUser()", e);
			throw new AppException("Could not parse weekStartDate");
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in getUserBillingsForUser()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}
		LOGGER.debug("Accounts: " + retValue);
		LOGGER.info("Exiting getUserBillingsForUser()");
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
	public UserBilling createUserBilling(@PathParam("userid") Long userid, UserBilling userBilling) throws AppException {
		LOGGER.info("Entering createUserBilling()");
		LOGGER.debug("userid: " + userid);
		LOGGER.debug("UserBilling: " + userBilling);
		UserBilling retValue = null;
		try {
			// Create the account for the user
			retValue = userBillingDAO.persist(userBilling);
		} catch (AppException ae) {
			LOGGER.error("AppException in createUserBilling()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in createUserBilling()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}
		LOGGER.debug("Accounts: " + retValue);
		LOGGER.info("Exiting createUserBilling()");
		return retValue;
	}

	/**
	 * 
	 * @param userbilling
	 * @return
	 * @throws AppException
	 */
	@POST
	@Path("/update")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public UserBilling updateUserBilling(UserBilling userBilling) throws AppException {
		LOGGER.info("Entering updateUserBilling()");
		LOGGER.debug("UserBilling: " + userBilling);
		UserBilling retValue = null;
		try {
			// Update the account
			retValue = userBillingDAO.update(userBilling);
		} catch (AppException ae) {
			LOGGER.error("AppException in updateUserBilling()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in updateUserBilling()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}
		LOGGER.debug("Accounts: " + retValue);
		LOGGER.info("Exiting updateUserBilling()");
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
	public void deleteUserBilling(@PathParam("id") Long id) throws AppException {
		LOGGER.info("Entering deleteUserBilling()");
		LOGGER.debug("Account ID: " + id);
		try {
			// Create the account
			userBillingDAO.delete(id);
		} catch (AppException ae) {
			LOGGER.error("AppException in deleteUserBilling()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in deleteUserBilling()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}
		LOGGER.info("Exiting deleteUserBilling()");
	}
}