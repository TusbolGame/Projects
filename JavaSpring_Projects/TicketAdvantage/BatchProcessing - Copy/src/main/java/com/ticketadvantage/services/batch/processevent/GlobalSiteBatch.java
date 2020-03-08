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
import com.ticketadvantage.services.model.GlobalScrapper;
import com.ticketadvantage.services.model.PendingEvent;
import com.ticketadvantage.services.model.Scrapper;

/**
 * @author jmiller
 *
 */
@Service
public class GlobalSiteBatch extends BaseSiteBatch implements Runnable {
	private static final Logger LOGGER = Logger.getLogger(GlobalSiteBatch.class);
	private GlobalScrapper globalScrapper;
	private volatile boolean shutdown = false;

	/**
	 * 
	 */
	public GlobalSiteBatch(GlobalScrapper globalScrapper) {
		super();
		LOGGER.info("Entering GlobalSiteBatch()");
		LOGGER.info("Exiting GlobalSiteBatch()");
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
			PENDINGEVENTDB.setConn(this.conn);
			EVENTSDB.setConn(this.conn);
			RECORDEVENTDB.setConn(this.conn);
			this.conn.setAutoCommit(false);
			setupSiteAndLogin();
	
			while (!shutdown) {
				try {
					checkGlobalSite();
	
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
	private void setupSiteAndLogin() throws BatchException {
		LOGGER.info("Entering setupSiteAndLogin()");

		if (globalScrapper != null && globalScrapper.getUserScrappers() != null && globalScrapper.getUserScrappers().size() > 0) {
			LOGGER.debug("globalScrapper.getUserScrappers().size(): " + globalScrapper.getUserScrappers().size());
			final Accounts account = globalScrapper.getUserScrappers().get(0).getSources().iterator().next();
			final String siteType = account.getSitetype();
			determinSiteProcessor(siteType, account);
		}

		LOGGER.info("Exiting setupSiteAndLogin()");
	}

	/**
	 * 
	 */
	public void checkGlobalSite() {
		LOGGER.info("Entering checkGlobalSite()");

		try {
			// Process the pending bet
			LOGGER.debug("siteProcessor: " + siteProcessor);
			final Set<PendingEvent> sitePendingEvents = siteProcessor.getPendingBets(siteProcessor.getHttpClientWrapper().getHost(), 
					siteProcessor.getHttpClientWrapper().getUsername());

			if (sitePendingEvents != null && !sitePendingEvents.isEmpty()) {
				LOGGER.debug("sitePendingEvents: " + sitePendingEvents);
				for (Scrapper scrapper : this.globalScrapper.getUserScrappers()) {
					LOGGER.debug("Scrapper: " + scrapper);

					// Check if we need to place a transaction
					checkSitePendingEvent("global", sitePendingEvents, scrapper);

					// Delete pending events
					deleteRemovedPendingEvents("global", sitePendingEvents, scrapper);

					// Commit
					this.conn.commit();
				}
			}
		} catch (Throwable t) {
			LOGGER.error("Throwable checkGlobalSite()", t);
			try {
				// Commit
				this.conn.commit();
			} catch (SQLException sqle) {
				LOGGER.error(sqle);
			}
		}

		LOGGER.info("Exiting checkGlobalSite()");
	}
}