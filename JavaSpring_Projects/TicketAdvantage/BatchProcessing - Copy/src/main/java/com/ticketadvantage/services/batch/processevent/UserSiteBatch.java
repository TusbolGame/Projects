/**
 * 
 */
package com.ticketadvantage.services.batch.processevent;

import java.sql.SQLException;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.Accounts;
import com.ticketadvantage.services.model.PendingEvent;
import com.ticketadvantage.services.model.Scrapper;
import com.ticketadvantage.services.model.UserScrapper;

/**
 * @author jmiller
 *
 */
@Service
public class UserSiteBatch extends BaseSiteBatch implements Runnable {
	private static final Logger LOGGER = Logger.getLogger(UserSiteBatch.class);
	private UserScrapper userScrapper;
	private volatile boolean shutdown = false;

	/**
	 * 
	 */
	public UserSiteBatch(UserScrapper userScrapper) {
		super();
		LOGGER.info("Entering UserSiteBatch()");
		LOGGER.info("Exiting UserSiteBatch()");
		this.userScrapper = userScrapper;
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
			PENDINGEVENTDB.setConn(this.conn);
			EVENTSDB.setConn(this.conn);
			RECORDEVENTDB.setConn(this.conn);
			this.conn.setAutoCommit(false);
			setupSiteAndLogin();
	
			while (!shutdown) {
				try {
					checkUserSite();
	
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					LOGGER.error("InterruptedException");
					// good practice
					Thread.currentThread().interrupt();
					return;
				} catch (Throwable  t) {
					LOGGER.error("Throwable in thread", t);
				} finally {
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
			LOGGER.error(t);
		} finally {
			PENDINGEVENTDB.complete();
			EVENTSDB.complete();
			RECORDEVENTDB.complete();			
		}
	}

	/**
	 * 
	 * @throws BatchException
	 */
	public void checkUserSite() throws BatchException  {
		LOGGER.info("Entering checkUserSite()");

		try {
			// Process the pending bet
			final Set<PendingEvent> sitePendingEvents = siteProcessor.getPendingBets(siteProcessor.getHttpClientWrapper().getHost(), 
					siteProcessor.getHttpClientWrapper().getUsername());

			if (sitePendingEvents != null && !sitePendingEvents.isEmpty()) {
				for (Scrapper scrapper : this.userScrapper.getUserScrappers()) {
					// Check if we need to place a transaction
					checkSitePendingEvent("user", sitePendingEvents, scrapper);
	
					// Delete pending events
					deleteRemovedPendingEvents("user", sitePendingEvents, scrapper);
				}
				// Commit
				conn.commit();
			}
		} catch (BatchException be) {
			LOGGER.error("Batch Exception", be);
			throw be;
		} catch (Throwable t) {
			LOGGER.error("Throwable checkGlobalSite()", t);
			try {
				// Commit
				this.conn.commit();
			} catch (SQLException sqle) {
				LOGGER.error(sqle);
			}
		}

		LOGGER.info("Exiting checkUserSite()");
	}
	
	/**
	 * 
	 * @throws BatchException
	 */
	private void setupSiteAndLogin() throws BatchException {
		LOGGER.info("Entering setupSiteAndLogin()");

		if (userScrapper != null && userScrapper.getUserScrappers() != null && userScrapper.getUserScrappers().size() > 0) {
			LOGGER.debug("userScrapper.getUserScrappers().size(): " + userScrapper.getUserScrappers().size());
			final Accounts account = userScrapper.getUserScrappers().get(0).getSources().iterator().next();
			final String siteType = account.getSitetype();
			determinSiteProcessor(siteType, account);
		}

		LOGGER.info("Exiting setupSiteAndLogin()");
	}
}