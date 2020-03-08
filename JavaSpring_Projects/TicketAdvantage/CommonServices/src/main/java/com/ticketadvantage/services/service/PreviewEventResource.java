/**
 * 
 */
package com.ticketadvantage.services.service;

import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.sites.SiteProcessor;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.PreviewInput;
import com.ticketadvantage.services.model.PreviewOutput;
import com.ticketadvantage.services.site.util.AccountSite;

/**
 * @author jmiller
 *
 */
public class PreviewEventResource implements Runnable {
	private static final Logger LOGGER = Logger.getLogger(PreviewEventResource.class);
	private PreviewInput previewInput;
	private PreviewOutput previewOutput;
	private PreviewTransactionResource previewTransactionResource;

	/**
	 * 
	 */
	public PreviewEventResource() {
		super();
		LOGGER.debug("Entering AWSPreviewEventResource()");
		LOGGER.debug("Exiting AWSPreviewEventResource()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final PreviewEventResource awsTransactionEventResource = new PreviewEventResource();
		PreviewInput previewInput = new PreviewInput();
		previewInput.setIsmobile(new Boolean(true));
		previewInput.setLineindicator("+");
		previewInput.setLinetype("spread");
		previewInput.setPassword("JY54");
		previewInput.setProxyname("None");
		previewInput.setRotationid(new Integer(355));
		previewInput.setShowrequestresponse(new Boolean(true));
		previewInput.setSitetype("TDSportsSeven");
		previewInput.setSporttype("ncaaflines");
		previewInput.setTeam1("North Texas");
		previewInput.setTeam2("SMU");
		previewInput.setTimezone("ET");
		previewInput.setUrl("http://www.fireonsports.ag");
		previewInput.setUsername("CPS221");

		awsTransactionEventResource.setPreviewInput(previewInput);
		awsTransactionEventResource.run();	
	}

	/**
	 * @return the previewInput
	 */
	public PreviewInput getPreviewInput() {
		return previewInput;
	}

	/**
	 * @param previewInput the previewInput to set
	 */
	public void setPreviewInput(PreviewInput previewInput) {
		this.previewInput = previewInput;
	}

	/**
	 * @return the previewOutput
	 */
	public PreviewOutput getPreviewOutput() {
		return previewOutput;
	}

	/**
	 * @param previewOutput the previewOutput to set
	 */
	public void setPreviewOutput(PreviewOutput previewOutput) {
		this.previewOutput = previewOutput;
	}

	/**
	 * @return the previewTransactionResource
	 */
	public PreviewTransactionResource getPreviewTransactionResource() {
		return previewTransactionResource;
	}

	/**
	 * @param previewTransactionResource the previewTransactionResource to set
	 */
	public void setPreviewTransactionResource(PreviewTransactionResource previewTransactionResource) {
		this.previewTransactionResource = previewTransactionResource;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		LOGGER.info("Entering run()");
		LOGGER.error("Preview Entering Time: " + new java.util.Date());

		try {	
			// Process the account
			previewOutput = processAccount(previewInput);

			if (previewOutput != null) {
				// Add preview output
				previewTransactionResource.addPreviewOutput(previewOutput);
			}
		} catch (Throwable t) {
			LOGGER.error("Exception in handleRequest()", t);
		}

		LOGGER.error("Preview Exiting Time: " + new java.util.Date());
		LOGGER.info("Exiting run()");
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
			previewOutput.setAccountid(previewInput.getAccountid());
			previewOutput.setAccountname(previewInput.getAccountname());
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