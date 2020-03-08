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

import com.ticketadvantage.services.dao.GroupDAO;
import com.ticketadvantage.services.errorhandling.AppErrorCodes;
import com.ticketadvantage.services.errorhandling.AppErrorMessage;
import com.ticketadvantage.services.errorhandling.AppException;
import com.ticketadvantage.services.model.Groups;

/**
 * @author jmiller
 *
 */
@Path("/group")
@Service
public class GroupResource {
	private static final Logger LOGGER = Logger.getLogger(GroupResource.class);

	@Autowired
	private GroupDAO groupDAO;

	/**
	 * 
	 */
	public GroupResource() {
		super();
		LOGGER.debug("Entering GroupResource()");
		LOGGER.debug("Exiting GroupResource()");
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
	public Set<Groups> getGroups() throws AppException {
		LOGGER.info("Entering getGroups()");
		Set<Groups> retValue = null;
		try {
			// Find all groups
			retValue = groupDAO.findAll();
		} catch (AppException ae) {
			LOGGER.error("AppException in getGroups()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in getGroups()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}
		LOGGER.debug("Groups: " + retValue);
		LOGGER.info("Exiting getGroups()");
		return retValue;
	}

	/**
	 * 
	 * @param id
	 * @return
	 * @throws AppException
	 */
	@GET 
	@Path("/id/{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Groups getGroup(@PathParam("id") Long id) throws AppException {
		LOGGER.info("Entering getGroup()");
		Groups retValue = null;
		try {
			// Find all groups
			retValue = groupDAO.getGroup(id);
		} catch (AppException ae) {
			LOGGER.error("AppException in getGroup()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in getGroup()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}
		LOGGER.debug("Group: " + retValue);
		LOGGER.info("Exiting getGroup()");
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
	public Set<Groups> getGroupsForUser(@PathParam("userid") Long userid) throws AppException {
		LOGGER.info("Entering getGroupsForUser()");
		LOGGER.debug("userid: " + userid);
		Set<Groups> retValue = null;

		try {
			// Get all groups for user
			retValue = groupDAO.getGroupsForUser(userid);
		} catch (AppException ae) {
			LOGGER.error("AppException in getGroupsForUser()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in getGroupsForUser()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}

		LOGGER.debug("Groups: " + retValue);
		LOGGER.info("Exiting getGroupsForUser()");
		return retValue;
	}

	/**
	 * 
	 * @param userid
	 * @param group
	 * @return
	 * @throws AppException
	 */
	@POST 
	@Path("/id/{userid}")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Groups createGroupForUser(@PathParam("userid") Long userid, Groups groups) throws AppException {
		LOGGER.info("Entering createGroupForUser()");
		LOGGER.debug("userid: " + userid);
		LOGGER.debug("Group: " + groups);
		Groups retValue = null;
		try {
			// Create the group for the user
			retValue = groupDAO.createGroupForUser(userid, groups);
		} catch (AppException ae) {
			LOGGER.error("AppException in createGroupForUser()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in createGroupForUser()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}
		LOGGER.debug("Groups: " + retValue);
		LOGGER.info("Exiting createGroupForUser()");
		return retValue;
	}

	/**
	 * 
	 * @param group
	 * @return
	 * @throws AppException
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Groups group(Groups group) throws AppException {
		LOGGER.info("Entering group()");
		LOGGER.debug("Group: " + group);
		Groups retValue = null;

		try {
			// Create the account
			retValue = groupDAO.persist(group);
		} catch (AppException ae) {
			LOGGER.error("AppException in group()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in group()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}
		LOGGER.debug("Groups: " + retValue);
		LOGGER.info("Exiting group()");
		return retValue;
	}

	/**
	 * 
	 * @param group
	 * @return
	 * @throws AppException
	 */
	@POST
	@Path("/update")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Groups updateGroup(Groups group) throws AppException {
		LOGGER.info("Entering updateGroup()");
		LOGGER.debug("Group: " + group);
		Groups retValue = null;

		try {
			// Update the account
			retValue = groupDAO.update(group);
		} catch (AppException ae) {
			LOGGER.error("AppException in updateGroup()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in updateGroup()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}
		LOGGER.debug("Groups: " + retValue);
		LOGGER.info("Exiting updateGroup()");
		return retValue;
	}

	/**
	 * 
	 * @param userid
	 * @param id
	 * @throws AppException
	 */
	@DELETE
	@Path("/delete/{id}")
	public void deleteGroup(@QueryParam("userid") Long userid, @PathParam("id") Long id) throws AppException {
		LOGGER.info("Entering deleteGroup()");
		LOGGER.info("User ID: " + userid);
		LOGGER.info("Group ID: " + id);

		try {
			// Create the account
			groupDAO.delete(userid, id);
		} catch (AppException ae) {
			LOGGER.error("AppException in deleteGroup()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in deleteGroup()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}
		LOGGER.info("Exiting deleteGroup()");
	}
}