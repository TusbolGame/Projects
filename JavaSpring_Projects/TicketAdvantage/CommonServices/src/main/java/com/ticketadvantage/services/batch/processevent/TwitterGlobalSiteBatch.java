/**
 * 
 */
package com.ticketadvantage.services.batch.processevent;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.BaseScrapper;
import com.ticketadvantage.services.model.GlobalScrapper;
import com.ticketadvantage.services.model.TwitterAccounts;
import com.ticketadvantage.services.model.TwitterScrapper;

/**
 * @author jmiller
 *
 */
@Service
public class TwitterGlobalSiteBatch extends TwitterBaseSiteBatch implements Runnable {
	private static final Logger LOGGER = Logger.getLogger(TwitterGlobalSiteBatch.class);
	private GlobalScrapper globalScrapper;
	private volatile boolean shutdown = false;

	/**
	 * 
	 */
	public TwitterGlobalSiteBatch() {
		super();
		LOGGER.info("Entering TwitterGlobalSiteBatch()");
		LOGGER.info("Exiting TwitterGlobalSiteBatch()");
	}

	/**
	 * 
	 */
	public TwitterGlobalSiteBatch(int pullingInterval, GlobalScrapper globalScrapper) {
		super();
		LOGGER.info("Entering TwitterGlobalSiteBatch()");
		super.pullingInterval = pullingInterval;
		this.globalScrapper = globalScrapper;
		LOGGER.info("Exiting TwitterGlobalSiteBatch()");
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
				LOGGER.debug("TwitterScrapper: " + b.getScrappername());
			}

			// Compare the dates to order them appropriately
			Collections.sort(baseScrappers, new Comparator<BaseScrapper>() {
				public int compare(BaseScrapper o1, BaseScrapper o2) {
					if (o1.getUserid() == null || o2.getUserid() == null)
						return 0;
					return o1.getUserid().compareTo(o2.getUserid());
				}
			});

			// Setup Twitter Handle
			setupTwitterHandle();

			while (!shutdown) {
				try {
					checkSite("global", baseScrappers, null);

					// Sleep for specified time
					Thread.sleep(pullingInterval);

					final Long scrapperId = globalScrapper.getScrapperid();
					LOGGER.debug("scrapperId: " + scrapperId);

					if (scrapperId != null) {
						final List<TwitterScrapper> wscrapper = TWITTERSCRAPPERDB.findById(scrapperId);

						if (wscrapper != null && !wscrapper.isEmpty()) {
							final TwitterScrapper websc = wscrapper.get(0);
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
	private void setupTwitterHandle() throws BatchException {
		LOGGER.info("Entering setupTwitterHandle()");

		if (globalScrapper != null && globalScrapper.getUserScrappers() != null && globalScrapper.getUserScrappers().size() > 0) {
			LOGGER.debug("globalScrapper.getUserScrappers().size(): " + globalScrapper.getUserScrappers().size());
			final TwitterScrapper twitterScrapper = (TwitterScrapper)globalScrapper.getUserScrappers().get(0);
			final TwitterAccounts account = twitterScrapper.getSources().iterator().next();
			final String siteType = account.getSitetype();

			determineTwitterProcessor(siteType, account);
		}

		LOGGER.info("Exiting setupTwitterHandle()");
	}
}