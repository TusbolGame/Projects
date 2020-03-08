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

import com.ticketadvantage.services.dao.CaptchaDAO;
import com.ticketadvantage.services.errorhandling.AppErrorCodes;
import com.ticketadvantage.services.errorhandling.AppErrorMessage;
import com.ticketadvantage.services.errorhandling.AppException;
import com.ticketadvantage.services.model.Captcha;

/**
 * @author jmiller
 *
 */
@Path("/captcha")
@Service
public class CaptchaResource {
	private static final Logger LOGGER = Logger.getLogger(CaptchaResource.class);

	@Autowired
	private CaptchaDAO captchaDAO;

	/**
	 * 
	 */
	public CaptchaResource() {
		super();
		LOGGER.debug("Entering CaptchaResource()");
		LOGGER.debug("Exiting CaptchaResource()");
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
	@POST 
	@Path("/setupcatpcha")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Captcha setupCaptcha(Captcha captchaInput) throws AppException {
		LOGGER.info("Entering setupCaptcha()");
		Captcha retValue = null;

		try {
			// Find all groups
			retValue = captchaDAO.setupCaptcha(captchaInput);
		} catch (AppException ae) {
			LOGGER.error("AppException in setupCaptcha()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in setupCaptcha()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}

		LOGGER.debug("Captcha: " + retValue);
		LOGGER.info("Exiting setupCaptcha()");
		return retValue;
	}

	/**
	 * 
	 * @param id
	 * @return
	 * @throws AppException
	 */
	@GET 
	@Path("/getcaptcha/id/{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Captcha getCaptcha(@PathParam("id") Long id) throws AppException {
		LOGGER.info("Entering getCaptcha()");
		Captcha retValue = null;

		try {
			// Find all groups
			retValue = captchaDAO.getCaptcha(id);
		} catch (AppException ae) {
			LOGGER.error("AppException in getCaptcha()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in getCaptcha()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}

		LOGGER.debug("Captcha: " + retValue);
		LOGGER.info("Exiting getCaptcha()");
		return retValue;
	}

	/**
	 * 
	 * @param group
	 * @return
	 * @throws AppException
	 */
	@PUT
	@Path("/updatecaptcha/{id}/{textdata}")
	public void updateCaptcha(@PathParam("id") Long id, @PathParam("textdata") String textdata) throws AppException {
		LOGGER.info("Entering updateCaptcha()");
		LOGGER.debug("id: " + id);
		LOGGER.debug("textdata: " + textdata);

		try {
			// Update the account
			captchaDAO.updateCaptcha(id, textdata);
		} catch (AppException ae) {
			LOGGER.error("AppException in updateCaptcha()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in updateCaptcha()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}

		LOGGER.info("Exiting updateCaptcha()");
	}
}