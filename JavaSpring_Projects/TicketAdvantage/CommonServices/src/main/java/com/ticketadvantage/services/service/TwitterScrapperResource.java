/**
 * 
 */
package com.ticketadvantage.services.service;

import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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

import com.ticketadvantage.services.dao.ScrapperDAO;
import com.ticketadvantage.services.errorhandling.AppErrorCodes;
import com.ticketadvantage.services.errorhandling.AppErrorMessage;
import com.ticketadvantage.services.errorhandling.AppException;
import com.ticketadvantage.services.model.PendingEvent;
import com.ticketadvantage.services.model.TwitterScrapper;

/**
 * @author jmiller
 *
 */
@Path("/twitterscrapper")
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Service
public class TwitterScrapperResource {
	private static final Logger LOGGER = Logger.getLogger(TwitterScrapperResource.class);

	@Autowired
	private ScrapperDAO scrapperDAO;

	/**
	 * 
	 */
	public TwitterScrapperResource() {
		super();
		LOGGER.debug("Entering TwitterScrapperResource()");
		LOGGER.debug("Exiting TwitterScrapperResource()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
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
	public TwitterScrapper getScrapper(@PathParam("id") Long id) throws AppException {
		LOGGER.info("Entering getScrapper()");
		TwitterScrapper retValue = null;

		try {
			// Find all groups
			retValue = scrapperDAO.findTwitter(id);
		} catch (AppException ae) {
			LOGGER.error("AppException in getScrapper()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in getScrapper()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}

		LOGGER.debug("TwitterScrapper: " + retValue);
		LOGGER.info("Exiting getScrapper()");
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
	public Set<TwitterScrapper> getScrappersForUser(@PathParam("userid") Long userid) throws AppException {
		LOGGER.info("Entering getScrappersForUser()");
		LOGGER.debug("userid: " + userid);
		Set<TwitterScrapper> retValue = null;

		try {
			// Get all groups for user
			retValue = scrapperDAO.findScrappersByUserIdTwitter(userid);
		} catch (AppException ae) {
			LOGGER.error("AppException in getScrappersForUser()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in getScrappersForUser()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}

		LOGGER.debug("TwitterScrapper: " + retValue);
		LOGGER.info("Exiting getScrappersForUser()");
		return retValue;
	}

	/**
	 * 
	 * @param userid
	 * @return
	 */
	@GET
	@Path("/events/{userid}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Set<PendingEvent> getEventsById(@PathParam("userid") Long userid) {
		LOGGER.info("Entering getEventsById()");
		LOGGER.debug("userid: " + userid);
		Set<PendingEvent> retValue = null;

		try {
		} catch (AppException ae) {
			LOGGER.error("AppException in getEventsById()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in getEventsById()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}

		LOGGER.debug("PendingEvents: " + retValue);
		LOGGER.info("Exiting getEventsById()");
		return retValue;
	}

	/**
	 * 
	 * @param userid
	 * @param scrapper
	 * @return
	 * @throws AppException
	 */
	@POST 
	@Path("/add")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public TwitterScrapper addScrapperForUser(TwitterScrapper scrapper) throws AppException {
		LOGGER.info("Entering addScrapperForUser()");
		LOGGER.debug("TwitterScrapper: " + scrapper);
		TwitterScrapper retValue = null;

		try {
			retValue = scrapperDAO.persistTwitter(scrapper);
		} catch (AppException ae) {
			LOGGER.error("AppException in addScrapperForUser()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in addScrapperForUser()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}

		LOGGER.debug("TwitterScrapper: " + retValue);
		LOGGER.info("Exiting addScrapperForUser()");
		return retValue;
	}

	/**
	 * 
	 * @param scrapper
	 * @return
	 * @throws AppException
	 */
	@POST
	@Path("/update")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public TwitterScrapper updateScrapper(TwitterScrapper scrapper) throws AppException {
		LOGGER.info("Entering updateScrapper()");
		LOGGER.debug("Scrapper: " + scrapper);
		TwitterScrapper retValue = null;

		try {
			// Update the scrapper
			TwitterScrapper tempScrapper = scrapperDAO.findTwitter(scrapper.getId());
			if (tempScrapper != null) {
				tempScrapper.setScrappername(scrapper.getScrappername());
				tempScrapper.setMllineadjustment(scrapper.getMllineadjustment());
				tempScrapper.setMlmaxamount(scrapper.getMlmaxamount());
				tempScrapper.setOnoff(scrapper.getOnoff());
				tempScrapper.setSpreadjuiceindicator(scrapper.getSpreadjuiceindicator());
				tempScrapper.setSpreadjuice(scrapper.getSpreadjuice());
				tempScrapper.setSpreadlineadjustment(scrapper.getSpreadlineadjustment());
				tempScrapper.setSpreadmaxamount(scrapper.getSpreadmaxamount());
				tempScrapper.setTotaljuiceindicator(scrapper.getTotaljuiceindicator());
				tempScrapper.setTotaljuice(scrapper.getTotaljuice());
				tempScrapper.setTotallineadjustment(scrapper.getTotallineadjustment());
				tempScrapper.setTotalmaxamount(scrapper.getTotalmaxamount());
				tempScrapper.setPullinginterval(scrapper.getPullinginterval());
				tempScrapper.setTelegramnumber(scrapper.getTelegramnumber());
				tempScrapper.setServernumber(scrapper.getServernumber());
				tempScrapper.setEnableretry(scrapper.getEnableretry());
				tempScrapper.setFullfill(scrapper.getFullfill());
				tempScrapper.setOrderamount(scrapper.getOrderamount());
				tempScrapper.setSendtextforaccount(scrapper.getSendtextforaccount());
				tempScrapper.setSendtextforgame(scrapper.getSendtextforgame());

				tempScrapper.setMlindicator(scrapper.getMlindicator());
				tempScrapper.setMlline(scrapper.getMlline());
				tempScrapper.setMllineadjustment(scrapper.getMllineadjustment());
				tempScrapper.setMlmaxamount(scrapper.getMlmaxamount());
				tempScrapper.setOnoff(scrapper.getOnoff());
				tempScrapper.setPullinginterval(scrapper.getPullinginterval());
				tempScrapper.setScrappername(scrapper.getScrappername());
				tempScrapper.setSpreadjuice(scrapper.getSpreadjuice());
				tempScrapper.setSpreadjuiceindicator(scrapper.getSpreadjuiceindicator());
				tempScrapper.setSpreadjuiceadjustment(scrapper.getSpreadjuiceadjustment());
				tempScrapper.setSpreadlineadjustment(scrapper.getSpreadlineadjustment());
				tempScrapper.setSpreadmaxamount(scrapper.getSpreadmaxamount());
				tempScrapper.setTelegramnumber(scrapper.getTelegramnumber());
				tempScrapper.setTotaljuice(scrapper.getTotaljuice());
				tempScrapper.setTotaljuiceindicator(scrapper.getTotaljuiceindicator());
				tempScrapper.setTotaljuiceadjustment(scrapper.getTotaljuiceadjustment());
				tempScrapper.setTotallineadjustment(scrapper.getTotallineadjustment());
				tempScrapper.setTotalmaxamount(scrapper.getTotalmaxamount());
				tempScrapper.setUserid(scrapper.getUserid());

				tempScrapper.setMobiletext(scrapper.getMobiletext());
				tempScrapper.setMiddlerules(scrapper.getMiddlerules());
				tempScrapper.setCheckdupgame(scrapper.getCheckdupgame());
				tempScrapper.setPlayotherside(scrapper.getPlayotherside());
				tempScrapper.setBestprice(scrapper.getBestprice());
				tempScrapper.setFirstonoff(scrapper.getFirstonoff());
				tempScrapper.setGameonoff(scrapper.getGameonoff());
				tempScrapper.setSecondonoff(scrapper.getSecondonoff());
				tempScrapper.setThirdonoff(scrapper.getThirdonoff());
				tempScrapper.setNflmlonoff(scrapper.getNflmlonoff());
				tempScrapper.setNflspreadonoff(scrapper.getNflspreadonoff());
				tempScrapper.setNfltotalonoff(scrapper.getNfltotalonoff());
				tempScrapper.setNcaafmlonoff(scrapper.getNcaafmlonoff());
				tempScrapper.setNcaafspreadonoff(scrapper.getNcaafspreadonoff());
				tempScrapper.setNcaaftotalonoff(scrapper.getNcaaftotalonoff());
				tempScrapper.setNbamlonoff(scrapper.getNbamlonoff());
				tempScrapper.setNbaspreadonoff(scrapper.getNbaspreadonoff());
				tempScrapper.setNbatotalonoff(scrapper.getNbatotalonoff());
				tempScrapper.setNcaabmlonoff(scrapper.getNcaabmlonoff());
				tempScrapper.setNcaabspreadonoff(scrapper.getNcaabspreadonoff());
				tempScrapper.setNcaabtotalonoff(scrapper.getNcaabtotalonoff());
				tempScrapper.setWnbamlonoff(scrapper.getWnbamlonoff());
				tempScrapper.setWnbaspreadonoff(scrapper.getWnbaspreadonoff());
				tempScrapper.setWnbatotalonoff(scrapper.getWnbatotalonoff());
				tempScrapper.setNhlmlonoff(scrapper.getNhlmlonoff());
				tempScrapper.setNhlspreadonoff(scrapper.getNhlspreadonoff());
				tempScrapper.setNhltotalonoff(scrapper.getNhltotalonoff());
				tempScrapper.setMlbmlonoff(scrapper.getMlbmlonoff());
				tempScrapper.setMlbspreadonoff(scrapper.getMlbspreadonoff());
				tempScrapper.setMlbtotalonoff(scrapper.getMlbtotalonoff());
				tempScrapper.setInternationalbaseballspreadonoff(scrapper.getInternationalbaseballspreadonoff());
				tempScrapper.setInternationalbaseballtotalonoff(scrapper.getInternationalbaseballtotalonoff());
				tempScrapper.setInternationalbaseballmlonoff(scrapper.getInternationalbaseballmlonoff());
				tempScrapper.setKeynumber(scrapper.getKeynumber());
				tempScrapper.setHumanspeed(scrapper.getHumanspeed());
				tempScrapper.setUnitsenabled(scrapper.getUnitsenabled());
				tempScrapper.setSpreadunit(scrapper.getSpreadunit());
				tempScrapper.setTotalunit(scrapper.getTotalunit());
				tempScrapper.setMlunit(scrapper.getMlunit());
				tempScrapper.setLeanssenabled(scrapper.getLeanssenabled());
				tempScrapper.setSpreadlean(scrapper.getSpreadlean());
				tempScrapper.setTotallean(scrapper.getTotallean());
				tempScrapper.setMllean(scrapper.getMllean());

				retValue = scrapperDAO.mergeTwitter(tempScrapper);
			} else {
				scrapperDAO.persistTwitter(scrapper);
			}
		} catch (AppException ae) {
			LOGGER.error("AppException in updateScrapper()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in updateScrapper()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}

		LOGGER.debug("Scrapper: " + retValue);
		LOGGER.info("Exiting updateScrapper()");
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
	public void deleteScrapper(@PathParam("id") Long id) throws AppException {
		LOGGER.info("Entering deleteScrapper()");
		LOGGER.info("id: " + id);

		try {
			// Delete the Scrapper
			scrapperDAO.deleteTwitter(id);
		} catch (AppException ae) {
			LOGGER.error("AppException in deleteScrapper()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in deleteScrapper()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}

		LOGGER.info("Exiting deleteScrapper()");
	}

	/**
	 * 
	 * @param id
	 * @param accountid
	 * @throws AppException
	 */
	@Path("/addsource/{id}/{accountid}")
	@PUT
	public void addSourceAccountForScrapper(@PathParam("id")Long id, @PathParam("accountid") Long accountid) throws AppException {
		LOGGER.info("Entering addSourceAccountForScrapper()");

		try {
			scrapperDAO.addTwitterSourceAccount(id, accountid);
		} catch (AppException ae) {
			LOGGER.error("AppException in addSourceAccountForScrapper()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in addSourceAccountForScrapper()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}

		LOGGER.info("Exiting addSourceAccountForScrapper()");
	}

	/**
	 * 
	 * @param id
	 * @param accountid
	 * @throws AppException
	 */
	@Path("/adddestination/{id}/{accountid}")
	@PUT
	public void addDestinationAccountForScrapper(@PathParam("id")Long id, @PathParam("accountid") Long accountid) throws AppException {
		LOGGER.info("Entering addDestinationAccountForScrapper()");

		try {
			scrapperDAO.addTwitterDestinationAccount(id, accountid);
		} catch (AppException ae) {
			LOGGER.error("AppException in addDestinationAccountForScrapper()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in addDestinationAccountForScrapper()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}

		LOGGER.info("Exiting addDestinationAccountForScrapper()");
	}

	/**
	 * 
	 * @param id
	 * @param accountid
	 * @throws AppException
	 */
	@Path("/addmiddle/{id}/{accountid}")
	@PUT
	public void addMiddleAccountForScrapper(@PathParam("id")Long id, @PathParam("accountid") Long accountid) throws AppException {
		LOGGER.info("Entering addMiddleAccountForScrapper()");

		try {
			scrapperDAO.addTwitterMiddleAccount(id, accountid);
		} catch (AppException ae) {
			LOGGER.error("AppException in addMiddleAccountForScrapper()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in addMiddleAccountForScrapper()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}

		LOGGER.info("Exiting addMiddleAccountForScrapper()");
	}

	/**
	 * 
	 * @param id
	 * @param accountid
	 * @throws AppException
	 */
	@Path("/addorder/{id}/{accountid}")
	@PUT
	public void addOrderAccountForScrapper(@PathParam("id")Long id, @PathParam("accountid") Long accountid) throws AppException {
		LOGGER.info("Entering addOrderAccountForScrapper()");

		try {
			scrapperDAO.addTwitterOrderAccount(id, accountid);
		} catch (AppException ae) {
			LOGGER.error("AppException in addOrderAccountForScrapper()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in addOrderAccountForScrapper()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}

		LOGGER.info("Exiting addOrderAccountForScrapper()");
	}

	/**
	 * 
	 * @param id
	 * @param accountid
	 * @throws AppException
	 */
	@Path("/deletesource/{id}/{accountid}")
	@DELETE
	public void deleteSourceAccountForScrapper(@PathParam("id")Long id, @PathParam("accountid") Long accountid) throws AppException {
		LOGGER.info("Entering deleteSourceAccountForScrapper()");

		try {
			scrapperDAO.deleteTwitterSourceAccount(id, accountid);
		} catch (AppException ae) {
			LOGGER.error("AppException in deleteSourceAccountForScrapper()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in deleteSourceAccountForScrapper()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}

		LOGGER.info("Exiting deleteSourceAccountForScrapper()");
	}

	/**
	 * 
	 * @param id
	 * @param accountid
	 * @throws AppException
	 */
	@Path("/deletedestination/{id}/{accountid}")
	@DELETE
	public void deleteDestinationAccountForScrapper(@PathParam("id")Long id, @PathParam("accountid") Long accountid) throws AppException {
		LOGGER.info("Entering deleteDestinationAccountForScrapper()");

		try {
			scrapperDAO.deleteTwitterDestinationAccount(id, accountid);
		} catch (AppException ae) {
			LOGGER.error("AppException in deleteDestinationAccountForScrapper()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in deleteDestinationAccountForScrapper()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}

		LOGGER.info("Exiting deleteDestinationAccountForScrapper()");
	}

	/**
	 * 
	 * @param id
	 * @param accountid
	 * @throws AppException
	 */
	@Path("/deletemiddle/{id}/{accountid}")
	@DELETE
	public void deleteMiddleAccountForScrapper(@PathParam("id")Long id, @PathParam("accountid") Long accountid) throws AppException {
		LOGGER.info("Entering deleteMiddleAccountForScrapper()");

		try {
			scrapperDAO.deleteTwitterMiddleAccount(id, accountid);
		} catch (AppException ae) {
			LOGGER.error("AppException in deleteMiddleAccountForScrapper()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in deleteMiddleAccountForScrapper()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}

		LOGGER.info("Exiting deleteMiddleAccountForScrapper()");
	}

	/**
	 * 
	 * @param id
	 * @param accountid
	 * @throws AppException
	 */
	@Path("/deleteorder/{id}/{accountid}")
	@DELETE
	public void deleteOrderAccountForScrapper(@PathParam("id")Long id, @PathParam("accountid") Long accountid) throws AppException {
		LOGGER.info("Entering deleteOrderAccountForScrapper()");

		try {
			scrapperDAO.deleteTwitterOrderAccount(id, accountid);
		} catch (AppException ae) {
			LOGGER.error("AppException in deleteOrderAccountForScrapper()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in deleteOrderAccountForScrapper()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}

		LOGGER.info("Exiting deleteOrderAccountForScrapper()");
	}
}