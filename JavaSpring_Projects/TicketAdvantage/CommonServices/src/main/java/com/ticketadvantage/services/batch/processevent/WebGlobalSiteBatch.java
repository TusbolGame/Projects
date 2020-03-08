/**
 * 
 */
package com.ticketadvantage.services.batch.processevent;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.Accounts;
import com.ticketadvantage.services.model.BaseScrapper;
import com.ticketadvantage.services.model.GlobalScrapper;
import com.ticketadvantage.services.model.WebScrapper;

/**
 * @author jmiller
 *
 */
@Service
public class WebGlobalSiteBatch extends WebBaseSiteBatch implements Runnable {
	private static final Logger LOGGER = Logger.getLogger(WebGlobalSiteBatch.class);
	private GlobalScrapper globalScrapper;
	private volatile boolean shutdown = false;

	/**
	 * 
	 */
	public WebGlobalSiteBatch() {
		super();
		LOGGER.info("Entering WebGlobalSiteBatch()");
		LOGGER.info("Exiting WebGlobalSiteBatch()");
	}

	/**
	 * 
	 */
	public WebGlobalSiteBatch(GlobalScrapper globalScrapper) {
		super();
		LOGGER.info("Entering WebGlobalSiteBatch()");
		LOGGER.info("Exiting WebGlobalSiteBatch()");
		this.globalScrapper = globalScrapper;
		new Thread(this).start();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * @return the globalScrapper
	 */
	public GlobalScrapper getGlobalScrapper() {
		return globalScrapper;
	}

	/**
	 * @param globalScrapper the globalScrapper to set
	 */
	public void setGlobalScrapper(GlobalScrapper globalScrapper) {
		this.globalScrapper = globalScrapper;
		if (this.globalScrapper == null) {
			shutdown = true;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		LOGGER.info("Entering run()");

		try {
			List<BaseScrapper> baseScrappers = this.globalScrapper.getUserScrappers();
			for (BaseScrapper b : baseScrappers) {
				LOGGER.debug("WebScrapper: " + b.getScrappername());
			}

			// Compare the dates to order them appropriately
			Collections.sort(baseScrappers, new Comparator<BaseScrapper>() {
				public int compare(BaseScrapper o1, BaseScrapper o2) {
					if (o1.getUserid() == null || o2.getUserid() == null)
						return 0;
					return o1.getUserid().compareTo(o2.getUserid());
				}
			});

			setupSiteAndLogin();
			int pullingInterval = Integer.parseInt(baseScrappers.get(0).getPullinginterval()) * 1000;
	
			while (!shutdown) {
				try {
					checkSite("global", baseScrappers, null);
	
					// Get time
					final Calendar date = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
					int hour = date.get(Calendar.HOUR_OF_DAY);

					// Check if we are between 2 am CT and 6 am CT
					if (hour < 3 || hour > 5) {
						Thread.sleep(pullingInterval);
					} else {
						// Sleep for an hour
						Thread.sleep(60 * 60 * 1000);
					}

					final Long scrapperId = globalScrapper.getScrapperid();
					LOGGER.debug("scrapperId: " + scrapperId);
					if (scrapperId != null) {
						final List<WebScrapper> wscrapper = WEBSCRAPPERDB.findById(scrapperId);
						if (wscrapper != null && !wscrapper.isEmpty()) {
							final WebScrapper websc = wscrapper.get(0);
							final Boolean onoff = websc.getOnoff();
							if (onoff != null && !onoff.booleanValue()) {
								shutdown = true;
							}
						}
					}
				} catch (BatchException be) {
					String errormessage = be.getErrormessage();
					if (errormessage != null &&errormessage.contains("Connection refused")) {
						LOGGER.error("Connection refused");
					} else {
						LOGGER.error(be.getErrormessage(), be);
					}
				} catch (InterruptedException e) {
					LOGGER.error("InterruptedException");
					// good practice
					Thread.currentThread().interrupt();
					return;
				} catch (Throwable t) {
					LOGGER.error(t.getMessage(), t);
				}
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}
	}

	/**
	 * 
	 * @throws BatchException
	 */
	private void setupSiteAndLogin() throws BatchException {
		LOGGER.info("Entering setupSiteAndLogin()");

		if (globalScrapper != null && globalScrapper.getUserScrappers() != null && globalScrapper.getUserScrappers().size() > 0) {
			LOGGER.debug("globalScrapper.getUserScrappers().size(): " + globalScrapper.getUserScrappers().size());
			final WebScrapper webScrapper = (WebScrapper)globalScrapper.getUserScrappers().get(0);
			final Accounts account = webScrapper.getSources().iterator().next();
			final String siteType = account.getSitetype();

			determinSiteProcessor(siteType, account);
		}

		LOGGER.info("Exiting setupSiteAndLogin()");
	}
}