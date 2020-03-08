/**
 * 
 */
package com.ticketadvantage.services.dao.sites.linetracker;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Set;
import java.util.TimeZone;

import org.apache.http.client.utils.HttpClientUtils;
import org.apache.log4j.Logger;

import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.PendingEvent;

/**
 * @author jmiller
 *
 */
public class SkullBetProcessSite extends LineTrackerProcessSite {
	private static final Logger LOGGER = Logger.getLogger(SkullBetProcessSite.class);
	private static final String PENDING_BETS = "PendingsWagers.aspx";
	private boolean resetConnection;
	private static int HOUR_TO_CHECK = 19;
	private boolean done;

	/**
	 * 
	 * @param host
	 * @param username
	 * @param password
	 */
	public SkullBetProcessSite(String host, String username, String password, boolean isMobile, boolean showRequestResponse) {
		super("SkullBet", host, username, password, isMobile, showRequestResponse);
		LOGGER.info("Entering SkullBetProcessSite()");

		// Menu items
		NFL_LINES_NAME = new String[] { "NFL" };
		NFL_LINES_SPORT = new String[] { "Football" };
		NFL_FIRST_NAME = new String[] { "NFL 1st Half" };
		NFL_FIRST_SPORT = new String[] { "Football" };
		NFL_SECOND_NAME = new String[] { "NFL 2nd Half" };
		NFL_SECOND_SPORT = new String[] { "Football" };
		NCAAF_LINES_NAME = new String[] { "NCAA" };
		NCAAF_LINES_SPORT = new String[] { "Football" };
		NCAAF_FIRST_NAME = new String[] { "NCAA 1st Half" };
		NCAAF_FIRST_SPORT = new String[] { "Football" };
		NCAAF_SECOND_NAME = new String[] { "NCAA 2nd Half" };
		NCAAF_SECOND_SPORT = new String[] { "Football" };
		// NBA_LINES_NAME = new String[] { "NBA", "NBA - Preseason" };
		NBA_LINES_NAME = new String[] { "NBA" };
		NBA_LINES_SPORT = new String[] { "Basketball" };
		NBA_FIRST_NAME = new String[] { "NBA 1st Half" };
		NBA_FIRST_SPORT = new String[] { "Basketball" };
		NBA_SECOND_NAME = new String[] { "NBA 2nd Half" };
		NBA_SECOND_SPORT = new String[] { "Basketball" };
		NCAAB_LINES_NAME = new String[] { "NCAA", "NCAA - Added / Extra" };
		NCAAB_LINES_SPORT = new String[] { "Basketball" };
		NCAAB_FIRST_NAME = new String[] { "NCAA 1st Half", "NCAA - Added / Extra 1st Half" };
		NCAAB_FIRST_SPORT = new String[] { "Basketball" };
		NCAAB_SECOND_NAME = new String[] { "NCAA 2nd Half", "NCAA - Added / Extra 2nd Half" };
		NCAAB_SECOND_SPORT = new String[] { "Basketball" };
		NHL_LINES_NAME = new String[] { "NHL", "NHL - Added / Extra" };
		NHL_LINES_SPORT = new String[] { "Hockey" };
		NHL_FIRST_NAME = new String[] { "NHL 1st Half", "NHL - Added / Extra 1st Half" };
		NHL_FIRST_SPORT = new String[] { "Hockey" };
		NHL_SECOND_NAME = new String[] { "NHL 2nd Half", "NHL - Added / Extra 2nd Half" };
		NHL_SECOND_SPORT = new String[] { "Hockey" };
		WNBA_LINES_NAME = new String[] { "WNBA" };
		WNBA_LINES_SPORT = new String[] { "Basketball" };
		WNBA_FIRST_NAME = new String[] { "WNBA 1st Half" };
		WNBA_FIRST_SPORT = new String[] { "Basketball" };
		WNBA_SECOND_NAME = new String[] { "WNBA 2nd Half" };
		WNBA_SECOND_SPORT = new String[] { "Basketball" };
		MLB_LINES_NAME = new String[] { "MLB" };
		MLB_LINES_SPORT = new String[] { "Baseball" };
		MLB_FIRST_NAME = new String[] { "MLB First 5 Innings" };
		MLB_FIRST_SPORT = new String[] { "Baseball" };
		MLB_SECOND_NAME = new String[] { "MLB 2nd Half" };
		MLB_SECOND_SPORT = new String[] { "Baseball" };
		MLB_SECOND_NAME = new String[] { "MLB 3rd Half" };
		MLB_SECOND_SPORT = new String[] { "Baseball" };

		LOGGER.info("Exiting SkullBetProcessSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final String[][] LSITES = new String [][] {
				{ "http://skullbet.com", "SK702", "v9746", "0", "0", "0", "Canada", "ET" }
			};

			// Loop through the sites
			for (int i = 0; i < LSITES.length; i++) {
				final SkullBetProcessSite processSite = new SkullBetProcessSite(LSITES[i][0], LSITES[i][1], LSITES[i][2], false, true);

				// Now call the test suite
				// LineTrackerProcessSite.testSite(processSite, i, LSITES);
			    processSite.httpClientWrapper.setupHttpClient(LSITES[i][6]);
			    processSite.processTransaction = false;
			    processSite.timezone = LSITES[i][7];
				String xhtml = processSite.loginToSite(LSITES[i][1], LSITES[i][2]);
				
				final Set<PendingEvent> pendingEvents = processSite.getPendingBets(LSITES[0][0], LSITES[0][1], new Object());
				if (pendingEvents != null && pendingEvents.size() > 0) {
					final Iterator<PendingEvent> itr = pendingEvents.iterator();
					if (itr != null) {
						while (itr.hasNext()) {
							final PendingEvent pe = itr.next();
							LOGGER.debug("PendingEvent: " + pe);
						}
					}
				}
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}
	}

	/**
	 * @return the done
	 */
	public boolean isDone() {
		return done;
	}

	/**
	 * @param done the done to set
	 */
	public void setDone(boolean done) {
		this.done = done;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#getPendingBets(java.lang.String, java.lang.String, java.lang.Object)
	 */
	@Override
	public Set<PendingEvent> getPendingBets(String accountName, String accountId, Object anythingObject) throws BatchException {
		LOGGER.info("Entering getPendingBets()");
		LOGGER.debug("accountName: " + accountName);
		LOGGER.debug("accountId: " + accountId);
		Set<PendingEvent> pendingWagers = null;

		try {
			// Get time
			final Calendar date = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
			int hour = date.get(Calendar.HOUR_OF_DAY);
			int minute = date.get(Calendar.MINUTE);
			LOGGER.debug("hour: " + hour);
			LOGGER.debug("minute: " + minute);

			// Check if it is 7pm PT
			if (hour == HOUR_TO_CHECK || hour == 18 || hour == 17 || hour == 6) {
				if (this.resetConnection) {
					LOGGER.error("Resetting the connection for " + accountName + " " + accountId);
					HttpClientUtils.closeQuietly(this.httpClientWrapper.getClient());
					this.httpClientWrapper.setupHttpClient(proxyName);
					this.loginToSite(this.httpClientWrapper.getUsername(), this.httpClientWrapper.getPassword());
					this.resetConnection = false;
				}
			} else {
				this.resetConnection = true;
			}

			// Get the pending bets data
			if ((hour == HOUR_TO_CHECK || hour == 18 || hour == 17 || hour == 6)  && (minute == 2 || minute == 4 || minute == 6 || minute == 8 || minute == 10 || minute == 12 || (minute >= 14 && minute < 45)) && !done) {
				String xhtml = getSite(super.populateUrl(PENDING_BETS));
				LOGGER.debug("xhtml: " + xhtml);
	
				if (xhtml != null && xhtml.contains("TablePendingWagers")) {
					if (!xhtml.contains("<iframe src=\"/Logins\"")) {
						pendingWagers = LTP.parsePendingBets(xhtml, accountName, accountId);
					}
				} else if (xhtml != null && !xhtml.contains("TablePendingWagers")) {
					// Do nothing
					LOGGER.error("No Open Bets");
					if (!xhtml.contains("<iframe src=\"/Logins\"")) {
						// LOGGER.error("xhtml: " + xhtml);
						// LOGGER.error("previousXhtml: " + previousXhtml);
						HttpClientUtils.closeQuietly(this.httpClientWrapper.getClient());
						this.httpClientWrapper.setupHttpClient(proxyName);
						this.loginToSite(this.httpClientWrapper.getUsername(), this.httpClientWrapper.getPassword());
						xhtml = getSite(super.populateUrl(PENDING_BETS));
						if (!xhtml.contains("<iframe src=\"/Logins\"")) {
							pendingWagers = LTP.parsePendingBets(xhtml, accountName, accountId);
						}
					}
				} else {
					// LOGGER.error("No pending wagers found for " + accountName + " " + accountId);
					HttpClientUtils.closeQuietly(this.httpClientWrapper.getClient());
					this.httpClientWrapper.setupHttpClient(proxyName);
					this.loginToSite(this.httpClientWrapper.getUsername(), this.httpClientWrapper.getPassword());
					xhtml = getSite(super.populateUrl(PENDING_BETS));
					if (!xhtml.contains("<iframe src=\"/Logins\"")) {
						pendingWagers = LTP.parsePendingBets(xhtml, accountName, accountId);
					}
				}
			}
		} catch (BatchException be) {
			// Site returned no data after request
			if (be.getErrormessage().contains("Site returned no data after request")) {
				LOGGER.error("LineTracker BatchException: " + be.getBexception());
				HttpClientUtils.closeQuietly(this.httpClientWrapper.getClient());
				this.httpClientWrapper.setupHttpClient(proxyName);
				this.loginToSite(this.httpClientWrapper.getUsername(), this.httpClientWrapper.getPassword());
				String xhtml = getSite(super.populateUrl(PENDING_BETS));
				if (!xhtml.contains("<iframe src=\"/Logins\"")) {
					pendingWagers = LTP.parsePendingBets(xhtml, accountName, accountId);
				}
			}
		}

		LOGGER.info("Exiting getPendingBets()");
		return pendingWagers;
	}
}