/**
 * 
 */
package com.ticketadvantage.services.service;

import java.util.Date;

import org.apache.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.ticketadvantage.services.dao.sites.SiteProcessor;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.PreviewInput;
import com.ticketadvantage.services.model.PreviewOutput;
import com.ticketadvantage.services.site.util.AccountSite;

/**
 * @author jmiller
 *
 */
public class LambdaPreviewEvent implements RequestHandler<PreviewInput, PreviewOutput> {
	private static final Logger LOGGER = Logger.getLogger(LambdaPreviewEvent.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * 
	 * @param input
	 * @param context
	 * @return
	 */
	public PreviewOutput handleRequest(PreviewInput input, Context context) {
		LOGGER.info("Entering handleRequest()");
		LOGGER.error("PreviewInput: " + input);
		LOGGER.debug("Context: " + context);
		LOGGER.error("Entering Date/Time: " + new Date());
		PreviewOutput output = null;

		try {	
			// Process the account
			output = processAccount(input);
		} catch (Throwable t) {
			LOGGER.error("Exception in handleRequest()", t);
		}

		LOGGER.error("Exiting Date/Time: " + new Date());
		LOGGER.info("Exiting handleRequest()");
		return output;
	}

	/**
	 * 
	 * @param previewInput
	 * @return
	 */
	private PreviewOutput processAccount(PreviewInput previewInput) {
		LOGGER.info("Entering processAccount()");
		LOGGER.debug("PreviewInput: " + previewInput);
		PreviewOutput previewOutput = null;

		try {
			final SiteProcessor processSite = AccountSite.GetAccountSite(previewInput);
			LOGGER.debug("ProcessSite: " + processSite);

			previewOutput = processSite.previewEvent(previewInput);
			LOGGER.error("PreviewOutput: " + previewOutput);
		} catch (BatchException be) {
			LOGGER.error(be.getMessage(), be);
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		LOGGER.info("Exiting processAccount()");
		return previewOutput;
	}
}