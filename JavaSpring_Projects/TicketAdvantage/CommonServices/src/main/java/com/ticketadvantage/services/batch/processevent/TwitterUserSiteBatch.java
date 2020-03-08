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
import com.ticketadvantage.services.model.TwitterAccounts;
import com.ticketadvantage.services.model.TwitterScrapper;
import com.ticketadvantage.services.model.UserScrapper;

/**
 * @author jmiller
 *
 */
@Service
public class TwitterUserSiteBatch extends TwitterBaseSiteBatch implements Runnable {
	private static final Logger LOGGER = Logger.getLogger(TwitterUserSiteBatch.class);
	private UserScrapper userScrapper;
	private volatile boolean shutdown = false;

	/**
	 * 
	 */
	public TwitterUserSiteBatch() {
		super();
		LOGGER.info("Entering TwitterUserSiteBatch()");
		LOGGER.info("Exiting TwitterUserSiteBatch()");
	}

	/**
	 * 
	 */
	public TwitterUserSiteBatch(int pullingInterval, UserScrapper userScrapper) {
		super();
		LOGGER.info("Entering TwitterUserSiteBatch()");
		super.pullingInterval = pullingInterval;
		this.userScrapper = userScrapper;
		LOGGER.info("Exiting TwitterUserSiteBatch()");
		new Thread(this).start();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * @return the userScrapper
	 */
	public UserScrapper getUserScrapper() {
		return userScrapper;
	}

	/**
	 * @param userScrapper the userScrapper to set
	 */
	public void setUserScrapper(UserScrapper userScrapper) {
		this.userScrapper = userScrapper;
		if (this.userScrapper == null) {
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
			List<BaseScrapper> baseScrappers = this.userScrapper.getUserScrappers();
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

			// Setup twitter handle
			setupTwitterHandle();

			while (!shutdown) {
				try {
					checkSite("user", baseScrappers, null);

					// Sleep for specified time
					Thread.sleep(pullingInterval);

					final Long scrapperId = userScrapper.getScrapperid();
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
				} catch (Throwable  t) {
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

		if (userScrapper != null && userScrapper.getUserScrappers() != null && userScrapper.getUserScrappers().size() > 0) {
			LOGGER.debug("userScrapper.getUserScrappers().size(): " + userScrapper.getUserScrappers().size());
			final TwitterScrapper twitterScrapper = (TwitterScrapper)userScrapper.getUserScrappers().get(0);
			final TwitterAccounts account = twitterScrapper.getSources().iterator().next();
			final String siteType = account.getSitetype();

			determineTwitterProcessor(siteType, account);
		}

		LOGGER.info("Exiting setupTwitterHandle()");
	}
}