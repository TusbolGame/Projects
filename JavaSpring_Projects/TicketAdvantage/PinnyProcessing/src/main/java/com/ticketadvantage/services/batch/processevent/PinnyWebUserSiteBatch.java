/**
 * 
 */
package com.ticketadvantage.services.batch.processevent;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ticketadvantage.services.dao.sites.pinnacleagent.PinnacleAgentProcessSite;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.Accounts;
import com.ticketadvantage.services.model.BaseScrapper;
import com.ticketadvantage.services.model.PendingEvent;
import com.ticketadvantage.services.model.UserScrapper;
import com.ticketadvantage.services.model.WebScrapper;

/**
 * @author jmiller
 *
 */
@Service
public class PinnyWebUserSiteBatch extends WebBaseSiteBatch implements Runnable {
	private static final Logger LOGGER = Logger.getLogger(PinnyWebUserSiteBatch.class);
	private UserScrapper userScrapper;
	private volatile boolean shutdown;
	private Long userId;

	protected static String[][] XPIKE_WOO_PLAYERS = new String[][] { };
	protected static String[][] XPIKE1_WOO_PLAYERS = new String[][] { };
	protected static String[][] XPIKE4_WOO_PLAYERS = new String[][] { };
	protected static String[][] XPIKE5_WOO_PLAYERS = new String[][] { };
	protected static String[][] XPIKE6_WOO_PLAYERS = new String[][] { };

	protected static String[][] XPIKE_INDI_PLAYERS = new String[][] {
		{"52026", "0", "500"},
		{"52065", "0", "500"}
	};
	protected static String[][] XPIKE1_INDI_PLAYERS = new String[][] {
		{"58009", "1000", "500"}
	};
	protected static String[][] XPIKE4_INDI_PLAYERS = new String[][] {
		{"58026", "0", "500"},
		{"58058", "0", "500"}
	};
	protected static String[][] XPIKE5_INDI_PLAYERS = new String[][] { };
	protected static String[][] XPIKE6_INDI_PLAYERS = new String[][] { };

	/**
	 * 
	 */
	public PinnyWebUserSiteBatch() {
		super();
		LOGGER.info("Entering WebUserSiteBatch()");
		LOGGER.info("Exiting WebUserSiteBatch()");
	}

	/**
	 * 
	 */
	public PinnyWebUserSiteBatch(UserScrapper userScrapper) {
		super();
		LOGGER.info("Entering WebUserSiteBatch()");
		LOGGER.info("Exiting WebUserSiteBatch()");
		this.userScrapper = userScrapper;
		new Thread(this).start();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Get time
		final Calendar date = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
		int hour = date.get(Calendar.HOUR_OF_DAY);
		System.out.println("hour: " + hour);
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

	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * 
	 * @param siteType
	 * @param account
	 */
	protected void determinSiteProcessor(String siteType, Accounts account) throws BatchException {
		LOGGER.info("Entering determinSiteProcessor()");
		LOGGER.info("siteType: " + siteType);
		LOGGER.info("Account: " + account);

		if (siteType != null && "PinnacleAgent".equals(siteType)) { 
			siteProcessor = new PinnacleAgentProcessSite(account.getUrl(),
				account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		}

		if (siteProcessor != null) {
			siteProcessor.setUserid(userId);
			siteProcessor.setTimezone(account.getTimezone());
			siteProcessor.getHttpClientWrapper().setupHttpClient(account.getProxylocation());
			siteProcessor.setProcessTransaction(false);
			siteProcessor.loginToSite(siteProcessor.getHttpClientWrapper().getUsername(), siteProcessor.getHttpClientWrapper().getPassword());
		}

		LOGGER.info("Exiting determinSiteProcessor()");
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		LOGGER.info("Entering run()");

		try {
			setupSiteAndLogin();
			int pullingInterval = Integer.parseInt(userScrapper.getUserScrappers().get(0).getPullinginterval()) * 1000;
	
			while (!shutdown) {
				try {
					checkSite("user", this.userScrapper.getUserScrappers(), this.userScrapper.getUserid());

					// Sleep for pulling time
					Thread.sleep(pullingInterval);

					final Long scrapperId = userScrapper.getScrapperid();
					LOGGER.debug("scrapperId: " + scrapperId);
					if (scrapperId != null) {
						final List<WebScrapper> wscrapper = WEBSCRAPPERDB.findById(scrapperId);
						if (wscrapper != null && !wscrapper.isEmpty()) {
							shutdown = true;
							for (WebScrapper scrapper : wscrapper) {
								final Boolean onoff = scrapper.getOnoff();
								if (onoff != null && onoff.booleanValue()) {
									shutdown = false;
								}
							}
						}
					}
				} catch (BatchException be) {
					LOGGER.error(be.getErrormessage(), be);
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

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.batch.processevent.BaseSiteBatch#checkSitePendingEvent(java.lang.String, java.util.Set, com.ticketadvantage.services.model.BaseScrapper)
	 */
	@Override
	protected void checkSitePendingEvent(String pendingType, Set<PendingEvent> sitePendingEvents, BaseScrapper scrapper) {
		LOGGER.info("Entering checkSitePendingEvent()");

		// Now loop through the site pending events and
		// compare to users pending events
		if (sitePendingEvents != null) {
			for (PendingEvent sitePendingEvent : sitePendingEvents) {
				LOGGER.debug("CheckSitePendingEvent: " + sitePendingEvent);

				boolean found = false;
				if (scrapper.getUserid() != null && scrapper.getUserid().longValue() == 1) {
					// Add a pending event; make sure it's from a good source
					for (int x = 0; x < XPIKE_WOO_PLAYERS.length; x++) {
						if (sitePendingEvent.getCustomerid().equals(XPIKE_WOO_PLAYERS[x][0])) {
							Double win = Double.valueOf(sitePendingEvent.getWin().replace("$", "").replaceAll(",", ""));
							Double wooWin = Double.valueOf(XPIKE_WOO_PLAYERS[x][1]);
							if (win >= wooWin) {
								found = true;
							}
						}			
					}
					for (int x = 0; x < XPIKE1_WOO_PLAYERS.length; x++) {
						if (sitePendingEvent.getCustomerid().equals(XPIKE_WOO_PLAYERS[x][0])) {
							Double win = Double.valueOf(sitePendingEvent.getWin().replace("$", "").replaceAll(",", ""));
							Double wooWin = Double.valueOf(XPIKE_WOO_PLAYERS[x][1]);
							if (win >= wooWin) {
								found = true;
							}
						}			
					}
					for (int x = 0; x < XPIKE4_WOO_PLAYERS.length; x++) {
						if (sitePendingEvent.getCustomerid().equals(XPIKE_WOO_PLAYERS[x][0])) {
							Double win = Double.valueOf(sitePendingEvent.getWin().replace("$", "").replaceAll(",", ""));
							Double wooWin = Double.valueOf(XPIKE_WOO_PLAYERS[x][1]);
							if (win >= wooWin) {
								found = true;
							}
						}			
					}
					for (int x = 0; x < XPIKE5_WOO_PLAYERS.length; x++) {
						if (sitePendingEvent.getCustomerid().equals(XPIKE_WOO_PLAYERS[x][0])) {
							Double win = Double.valueOf(sitePendingEvent.getWin().replace("$", "").replaceAll(",", ""));
							Double wooWin = Double.valueOf(XPIKE_WOO_PLAYERS[x][1]);
							if (win >= wooWin) {
								found = true;
							}
						}			
					}
					for (int x = 0; x < XPIKE6_WOO_PLAYERS.length; x++) {
						if (sitePendingEvent.getCustomerid().equals(XPIKE_WOO_PLAYERS[x][0])) {
							Double win = Double.valueOf(sitePendingEvent.getWin().replace("$", "").replaceAll(",", ""));
							Double wooWin = Double.valueOf(XPIKE_WOO_PLAYERS[x][1]);
							if (win >= wooWin) {
								found = true;
							}
						}			
					}
				} else {
					// Add a pending event; make sure it's from a good source
					for (int x = 0; x < XPIKE_INDI_PLAYERS.length; x++) {
						if (sitePendingEvent.getCustomerid().equals(XPIKE_INDI_PLAYERS[x][0])) {
							Double win = Double.valueOf(sitePendingEvent.getWin().replace("$", "").replaceAll(",", ""));
							Double indiWin = Double.valueOf(XPIKE_INDI_PLAYERS[x][1]);
							if (win >= indiWin) {
								found = true;
							}
						}			
					}
					for (int x = 0; x < XPIKE1_INDI_PLAYERS.length; x++) {
						if (sitePendingEvent.getCustomerid().equals(XPIKE1_INDI_PLAYERS[x][0])) {
							Double win = Double.valueOf(sitePendingEvent.getWin().replace("$", "").replaceAll(",", ""));
							Double indiWin = Double.valueOf(XPIKE1_INDI_PLAYERS[x][1]);
							if (win >= indiWin) {
								found = true;
							}
						}			
					}
					for (int x = 0; x < XPIKE4_INDI_PLAYERS.length; x++) {
						if (sitePendingEvent.getCustomerid().equals(XPIKE4_INDI_PLAYERS[x][0])) {
							Double win = Double.valueOf(sitePendingEvent.getWin().replace("$", "").replaceAll(",", ""));
							Double indiWin = Double.valueOf(XPIKE4_INDI_PLAYERS[x][1]);
							if (win >= indiWin) {
								found = true;
							}
						}			
					}
					for (int x = 0; x < XPIKE5_INDI_PLAYERS.length; x++) {
						if (sitePendingEvent.getCustomerid().equals(XPIKE5_INDI_PLAYERS[x][0])) {
							Double win = Double.valueOf(sitePendingEvent.getWin().replace("$", "").replaceAll(",", ""));
							Double indiWin = Double.valueOf(XPIKE5_INDI_PLAYERS[x][1]);
							if (win >= indiWin) {
								found = true;
							}
						}			
					}
					for (int x = 0; x < XPIKE6_INDI_PLAYERS.length; x++) {
						if (sitePendingEvent.getCustomerid().equals(XPIKE6_INDI_PLAYERS[x][0])) {
							Double win = Double.valueOf(sitePendingEvent.getWin().replace("$", "").replaceAll(",", ""));
							Double indiWin = Double.valueOf(XPIKE6_INDI_PLAYERS[x][1]);
							if (win >= indiWin) {
								found = true;
							}
						}			
					}
				}

				LOGGER.debug("found: " + found);
				LOGGER.debug("UserID: " + scrapper.getUserid().longValue() + " CustomerID: " + sitePendingEvent.getCustomerid());

				if (found) {
					// Check for sites that need to do an extra call
					if (sitePendingEvent.getDoposturl()) {
						try {
							final PendingEvent pe = PENDINGEVENTDB.findPendingEventsByUserIdByTicketnumber(scrapper.getUserid(), sitePendingEvent.getTicketnum());
							if (pe != null) {
								sitePendingEvent.setGametype(pe.getGametype());
							} else {
								// Write this event to the DB for this user
								doProcessPendingEvent(sitePendingEvent);
							}
						} catch (Throwable t) {
							LOGGER.error(t.getMessage(), t);
						}
					}
	
					// Check if the scrapper should process this sport
					if (isSportAvailable(sitePendingEvent.getGamesport(), sitePendingEvent.getGametype(), sitePendingEvent.getEventtype(), sitePendingEvent.getLinetype(), scrapper)) {
						PendingEvent copySitePendingEvent = new PendingEvent();
						copySitePendingEvent = copyPendingEvent(sitePendingEvent, copySitePendingEvent);
						// Set attributes on event
						copySitePendingEvent.setUserid(scrapper.getUserid());
						copySitePendingEvent.setDatecreated(new Date());
						copySitePendingEvent.setDatemodified(new Date());
						copySitePendingEvent.setPendingtype(pendingType);
	
						// Check for pending event update
						boolean foundPending = checkForPendingUpdate(pendingType, scrapper, copySitePendingEvent);
	
						// Persist pending event
						persistPendingEvent(foundPending, sitePendingEvent, copySitePendingEvent);
	
						// If we determine that this is new, create the transaction
						if (!foundPending) {
							setupScrapperAmount(scrapper, copySitePendingEvent);

							LOGGER.debug("PendingEvent not found: " + copySitePendingEvent);
							checkSourceSiteAmount(copySitePendingEvent, scrapper);
						}
					}
				}
			}
		}

		LOGGER.info("Exiting checkSitePendingEvent()");
	}

	/**
	 * 
	 * @throws BatchException
	 */
	private void setupSiteAndLogin() throws BatchException {
		LOGGER.info("Entering setupSiteAndLogin()");

		if (userScrapper != null && userScrapper.getUserScrappers() != null && userScrapper.getUserScrappers().size() > 0) {
			LOGGER.debug("userScrapper.getUserScrappers().size(): " + userScrapper.getUserScrappers().size());
			
			WebScrapper webScrapper = null;
			final List<BaseScrapper> scrappers = userScrapper.getUserScrappers();
			for (BaseScrapper scrapper : scrappers) {
				if (scrapper.getOnoff().booleanValue()) {
					webScrapper = (WebScrapper)scrapper;
				}
			}

			if (webScrapper != null) {
				final Accounts account = webScrapper.getSources().iterator().next();
				final String siteType = account.getSitetype();
				determinSiteProcessor(siteType, account);
			}
		}

		LOGGER.info("Exiting setupSiteAndLogin()");
	}

	/**
	 * 
	 * @param scrapper
	 * @param sitePendingEvent
	 */
	private void setupScrapperAmount(BaseScrapper scrapper, PendingEvent sitePendingEvent) {
		LOGGER.info("Entering setupScrapperAmount()");

		try {
			if (scrapper.getUserid() != null && scrapper.getUserid().longValue() == 1) {
				// Add a pending event; make sure it's from a good source
				for (int x = 0; x < XPIKE_WOO_PLAYERS.length; x++) {
					if (sitePendingEvent.getCustomerid().equals(XPIKE_WOO_PLAYERS[x][0])) {
						scrapper.setOrderamount(Integer.parseInt(XPIKE_WOO_PLAYERS[x][2]));
					}			
				}
				for (int x = 0; x < XPIKE1_WOO_PLAYERS.length; x++) {
					if (sitePendingEvent.getCustomerid().equals(XPIKE1_WOO_PLAYERS[x][0])) {
						scrapper.setOrderamount(Integer.parseInt(XPIKE1_WOO_PLAYERS[x][2]));
					}			
				}
				for (int x = 0; x < XPIKE4_WOO_PLAYERS.length; x++) {
					if (sitePendingEvent.getCustomerid().equals(XPIKE4_WOO_PLAYERS[x][0])) {
						scrapper.setOrderamount(Integer.parseInt(XPIKE4_WOO_PLAYERS[x][2]));
					}			
				}
				for (int x = 0; x < XPIKE5_WOO_PLAYERS.length; x++) {
					if (sitePendingEvent.getCustomerid().equals(XPIKE5_WOO_PLAYERS[x][0])) {
						scrapper.setOrderamount(Integer.parseInt(XPIKE5_WOO_PLAYERS[x][2]));
					}			
				}
				for (int x = 0; x < XPIKE6_WOO_PLAYERS.length; x++) {
					if (sitePendingEvent.getCustomerid().equals(XPIKE6_WOO_PLAYERS[x][0])) {
						scrapper.setOrderamount(Integer.parseInt(XPIKE6_WOO_PLAYERS[x][2]));
					}			
				}
			} else {
				// Add a pending event; make sure it's from a good source
				for (int x = 0; x < XPIKE_INDI_PLAYERS.length; x++) {
					if (sitePendingEvent.getCustomerid().equals(XPIKE_INDI_PLAYERS[x][0])) {
						scrapper.setOrderamount(Integer.parseInt(XPIKE_INDI_PLAYERS[x][2]));
					}			
				}
				for (int x = 0; x < XPIKE1_INDI_PLAYERS.length; x++) {
					if (sitePendingEvent.getCustomerid().equals(XPIKE1_INDI_PLAYERS[x][0])) {
						scrapper.setOrderamount(Integer.parseInt(XPIKE1_INDI_PLAYERS[x][2]));
					}			
				}
				for (int x = 0; x < XPIKE4_INDI_PLAYERS.length; x++) {
					if (sitePendingEvent.getCustomerid().equals(XPIKE4_INDI_PLAYERS[x][0])) {
						scrapper.setOrderamount(Integer.parseInt(XPIKE4_INDI_PLAYERS[x][2]));
					}			
				}
				for (int x = 0; x < XPIKE5_INDI_PLAYERS.length; x++) {
					if (sitePendingEvent.getCustomerid().equals(XPIKE5_INDI_PLAYERS[x][0])) {
						scrapper.setOrderamount(Integer.parseInt(XPIKE5_INDI_PLAYERS[x][2]));
					}			
				}
				for (int x = 0; x < XPIKE6_INDI_PLAYERS.length; x++) {
					if (sitePendingEvent.getCustomerid().equals(XPIKE6_INDI_PLAYERS[x][0])) {
						scrapper.setOrderamount(Integer.parseInt(XPIKE6_INDI_PLAYERS[x][2]));
					}			
				}
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		LOGGER.info("Exiting setupScrapperAmount()");
	}
}