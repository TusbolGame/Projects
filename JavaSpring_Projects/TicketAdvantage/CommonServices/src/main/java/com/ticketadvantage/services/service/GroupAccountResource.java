/**
 * 
 */
package com.ticketadvantage.services.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ticketadvantage.services.dao.GroupDAO;
import com.ticketadvantage.services.errorhandling.AppErrorCodes;
import com.ticketadvantage.services.errorhandling.AppErrorMessage;
import com.ticketadvantage.services.errorhandling.AppException;
import com.ticketadvantage.services.model.GroupAccount;

/**
 * @author jmiller
 *
 */
@Path("/groupaccount")
@Service
public class GroupAccountResource {
	private static final Logger LOGGER = Logger.getLogger(GroupAccountResource.class);

	@Autowired
	private GroupDAO groupDAO;

	/**
	 * 
	 */
	public GroupAccountResource() {
		super();
		LOGGER.debug("Entering GroupAccountResource()");
		LOGGER.debug("Exiting GroupAccountResource()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * 
	 * @param groupAccount
	 * @throws AppException
	 */
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public void addAccountForGroup(GroupAccount groupAccount) throws AppException {
		LOGGER.info("Entering addAccountForGroup()");
		LOGGER.debug("GroupAccount: " + groupAccount);
		try {
			// Add the account to the group
			groupDAO.addAccountForGroup(groupAccount.getGroupid(), groupAccount.getAccountid());
		} catch (AppException ae) {
			LOGGER.error("AppException in getUser()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in getUser()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}
		LOGGER.info("Exiting addAccountForGroup()");
	}

	/**
	 * 
	 * @param groupAccount
	 * @throws AppException
	 */
	@DELETE
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public void deleteAccountForGroup(GroupAccount groupAccount) throws AppException {
		LOGGER.info("Entering deleteAccountForGroup()");
		LOGGER.debug("GroupAccount: " + groupAccount);
		try {
			// Delete the account to the group
			groupDAO.deleteAccountForGroup(groupAccount.getGroupid(), groupAccount.getAccountid());
		} catch (AppException ae) {
			LOGGER.error("AppException in getUser()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in getUser()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}
		LOGGER.info("Exiting deleteAccountForGroup()");
	}
}