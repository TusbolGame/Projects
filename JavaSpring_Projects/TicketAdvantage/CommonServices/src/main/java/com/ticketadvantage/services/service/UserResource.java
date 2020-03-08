/**
 * 
 */
package com.ticketadvantage.services.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ticketadvantage.services.dao.UserDAO;
import com.ticketadvantage.services.errorhandling.AppErrorCodes;
import com.ticketadvantage.services.errorhandling.AppErrorMessage;
import com.ticketadvantage.services.errorhandling.AppException;
import com.ticketadvantage.services.model.User;

/**
 * @author jmiller
 *
 */
@Path("/user")
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Service
public class UserResource {
	private static final Logger LOGGER = Logger.getLogger(UserResource.class);

	@Autowired
	private UserDAO userDAO;

	/**
	 * 
	 */
	public UserResource() {
		super();
		LOGGER.debug("Entering UserResource()");
		LOGGER.debug("Exiting UserResource()");
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
	 */
	@GET
	@Path("/id/{userid}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public User getUser(@PathParam("userid") Long userid) {
		LOGGER.info("Entering getUser()");
		LOGGER.debug("userid: " + userid);
		User retValue = null;

		try {
			// Find the user
			retValue = userDAO.find(userid);
		} catch (AppException ae) {
			LOGGER.error("AppException in getUser()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in getUser()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}
		LOGGER.debug("User: " + retValue);
		LOGGER.info("Exiting getUser()");
		return retValue;
	}

	/**
	 * 
	 * @param user
	 * @return
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public User user(User user) throws AppException {
		LOGGER.info("Entering user()");
		LOGGER.debug("User: " + user);
		User retValue = null;
		
		try {
			// Create the user
			retValue = userDAO.persist(user);
		} catch (AppException ae) {
			LOGGER.error("AppException in user()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in user()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}
		LOGGER.debug("User: " + retValue);
		LOGGER.info("Exiting user()");
		return retValue;
	}

	/**
	 * 
	 * @param userid
	 * @param user
	 */
	@PUT
	@Path("/id/{userid}")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public void updateUser(@PathParam("userid") Long userid, User user) {
		LOGGER.info("Entering updateUser()");
		LOGGER.debug("userid: " + userid);
		LOGGER.debug("User: " + user);
		try {
			// Update the user
			userDAO.update(userid, user);
		} catch (AppException ae) {
			LOGGER.error("AppException in updateUser()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in updateUser()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}
		LOGGER.info("Exiting updateUser()");
	}
}