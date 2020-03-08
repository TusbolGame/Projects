/**
 * 
 */
package com.ticketadvantage.services.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
@Path("/login")
@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Service
public class LoginResource {
	private static final Logger LOGGER = Logger.getLogger(LoginResource.class);

	@Autowired
	private UserDAO userDAO;

	/**
	 * 
	 */
	public LoginResource() {
		super();
		LOGGER.debug("Entering LoginResource()");
		LOGGER.debug("Exiting LoginResource()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * 
	 * @param user
	 * @return
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public User login(User user) throws AppException {
		LOGGER.info("Entering login()");
		LOGGER.debug("User: " + user);
		User retValue = null;

		try {
			retValue = userDAO.login(user.getUsername(), user.getPassword());
		} catch (AppException ae) {
			LOGGER.error("AppException in login()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in login()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}
		LOGGER.debug("User: " + retValue);
		LOGGER.info("Exiting login()");
		return retValue;
	}
}