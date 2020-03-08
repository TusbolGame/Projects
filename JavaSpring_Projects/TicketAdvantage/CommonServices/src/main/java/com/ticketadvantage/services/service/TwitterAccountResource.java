/**
 * 
 */
package com.ticketadvantage.services.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
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

import com.ticketadvantage.services.dao.TwitterAccountDAO;
import com.ticketadvantage.services.errorhandling.AppErrorCodes;
import com.ticketadvantage.services.errorhandling.AppErrorMessage;
import com.ticketadvantage.services.errorhandling.AppException;
import com.ticketadvantage.services.model.TwitterAccounts;

/**
 * @author jmiller
 *
 */
@Path("/twitteraccount")
@Service
public class TwitterAccountResource {
	private static final Logger LOGGER = Logger.getLogger(TwitterAccountResource.class);

	@Autowired
	private TwitterAccountDAO accountDAO;

	/**
	 * 
	 */
	public TwitterAccountResource() {
		super();
		LOGGER.debug("Entering TwitterAccountResource()");
		LOGGER.debug("Exiting TwitterAccountResource()");
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
	public Set<TwitterAccounts> getAccounts() throws AppException {
		LOGGER.info("Entering getAccounts()");
		Set<TwitterAccounts> retValue = null;

		try {
			// Find all accounts
			retValue = accountDAO.findAll();
		} catch (AppException ae) {
			LOGGER.error("AppException in getAccounts()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in getAccounts()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}

		LOGGER.debug("Accounts: " + retValue);
		LOGGER.info("Exiting getAccounts()");
		return retValue;
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
	public Set<TwitterAccounts> getAccountsForUser(@PathParam("userid") Long userid) throws AppException {
		LOGGER.info("Entering getAccountsForUser()");
		LOGGER.debug("userid: " + userid);
		Set<TwitterAccounts> retValue = null;

		try {
			// Get all accounts for user
			retValue = accountDAO.getAccountsForUser(userid);
		} catch (AppException ae) {
			LOGGER.error("AppException in getAccountsForUser()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in getAccountsForUser()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}

		LOGGER.debug("Accounts: " + retValue);
		LOGGER.info("Exiting getAccountsForUser()");
		return retValue;
	}

	/**
	 * 
	 * @param userid
	 * @param account
	 * @return
	 * @throws AppException
	 */
	@POST
	@Path("/id/{userid}")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public TwitterAccounts createAccountForUser(@PathParam("userid") Long userid, TwitterAccounts account) throws AppException {
		LOGGER.info("Entering createAccountForUser()");
		LOGGER.debug("userid: " + userid);
		LOGGER.debug("Account: " + account);
		TwitterAccounts retValue = null;

		try {
			// Create the account for the user
			retValue = accountDAO.createTwitterAccountForUser(userid, account);
		} catch (AppException ae) {
			LOGGER.error("AppException in createAccountForUser()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in createAccountForUser()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}

		LOGGER.debug("Accounts: " + retValue);
		LOGGER.info("Exiting createAccountForUser()");
		return retValue;
	}

	/**
	 * 
	 * @param account
	 * @return
	 * @throws AppException
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public TwitterAccounts account(TwitterAccounts account) throws AppException {
		LOGGER.info("Entering account()");
		LOGGER.debug("TwitterAccounts: " + account);
		TwitterAccounts retValue = null;

		try {
			// Create the account
			retValue = accountDAO.persist(account);
		} catch (AppException ae) {
			LOGGER.error("AppException in account()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in account()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}

		LOGGER.debug("Accounts: " + retValue);
		LOGGER.info("Exiting account()");
		return retValue;
	}

	/**
	 * 
	 * @param account
	 * @return
	 * @throws AppException
	 */
	@POST
	@Path("/update")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public TwitterAccounts updateAccount(TwitterAccounts account) throws AppException {
		LOGGER.info("Entering updateAccount()");
		LOGGER.debug("TwitterAccounts: " + account);
		TwitterAccounts retValue = null;

		try {
			// Update the account
			retValue = accountDAO.update(account);
		} catch (AppException ae) {
			LOGGER.error("AppException in updateAccount()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in updateAccount()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}

		LOGGER.debug("TwitterAccounts: " + retValue);
		LOGGER.info("Exiting updateAccount()");
		return retValue;
	}

	/**
	 * 
	 * @param account
	 * @return
	 * @throws AppException
	 */
	@DELETE
	@Path("/delete/{id}")
	public void deleteAccount(@QueryParam("userid") Long userid, @PathParam("id") Long id) throws AppException {
		LOGGER.info("Entering deleteAccount()");
		LOGGER.debug("User ID: " + userid);
		LOGGER.debug("Account ID: " + id);

		try {
			// Create the account
			accountDAO.delete(userid, id);
		} catch (AppException ae) {
			LOGGER.error("AppException in deleteAccount()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in deleteAccount()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}

		LOGGER.info("Exiting deleteAccount()");
	}
}