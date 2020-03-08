/**
 * 
 */
package com.ticketadvantage.services.service;

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

import com.ticketadvantage.services.dao.EmailAccountDAO;
import com.ticketadvantage.services.errorhandling.AppErrorCodes;
import com.ticketadvantage.services.errorhandling.AppErrorMessage;
import com.ticketadvantage.services.errorhandling.AppException;
import com.ticketadvantage.services.model.EmailAccounts;

/**
 * @author jmiller
 *
 */
@Path("/emailaccount")
@Service
public class EmailAccountResource {
	private static final Logger LOGGER = Logger.getLogger(EmailAccountResource.class);

	@Autowired
	private EmailAccountDAO accountDAO;

	/**
	 * 
	 */
	public EmailAccountResource() {
		super();
		LOGGER.debug("Entering EmailAccountResource()");
		LOGGER.debug("Exiting EmailAccountResource()");
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
	public Set<EmailAccounts> getAccounts() throws AppException {
		LOGGER.info("Entering getAccounts()");
		Set<EmailAccounts> retValue = null;
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
	public Set<EmailAccounts> getAccountsForUser(@PathParam("userid") Long userid) throws AppException {
		LOGGER.info("Entering getAccountsForUser()");
		LOGGER.debug("userid: " + userid);
		Set<EmailAccounts> retValue = null;
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
	public EmailAccounts createAccountForUser(@PathParam("userid") Long userid, EmailAccounts account) throws AppException {
		LOGGER.info("Entering createAccountForUser()");
		LOGGER.debug("userid: " + userid);
		LOGGER.debug("Account: " + account);
		EmailAccounts retValue = null;

		try {
			// Create the account for the user
			retValue = accountDAO.createEmailAccountForUser(userid, account);
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
	public EmailAccounts account(EmailAccounts account) throws AppException {
		LOGGER.info("Entering account()");
		LOGGER.debug("EmailAccounts: " + account);
		EmailAccounts retValue = null;

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
	public EmailAccounts updateAccount(EmailAccounts account) throws AppException {
		LOGGER.info("Entering updateAccount()");
		LOGGER.debug("EmailAccounts: " + account);
		EmailAccounts retValue = null;

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

		LOGGER.debug("EmailAccounts: " + retValue);
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