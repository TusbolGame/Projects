/**
 * 
 */
package com.ticketadvantage.services.batch;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ticketadvantage.services.batch.processevent.BaseSiteBatch;
import com.ticketadvantage.services.dao.email.drh.DrhParser;
import com.ticketadvantage.services.dao.sites.SiteProcessor;
import com.ticketadvantage.services.dao.sites.linetracker.SkullBetProcessSite;
import com.ticketadvantage.services.dao.sites.sportsinsights.SportsInsightsSite;
import com.ticketadvantage.services.db.WebScrapperDB;
import com.ticketadvantage.services.email.SendText;
import com.ticketadvantage.services.email.ticketadvantage.TicketAdvantageGmailOath;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.Accounts;
import com.ticketadvantage.services.model.BaseRecordEvent;
import com.ticketadvantage.services.model.BaseScrapper;
import com.ticketadvantage.services.model.EventPackage;
import com.ticketadvantage.services.model.EventsPackage;
import com.ticketadvantage.services.model.GlobalScrapper;
import com.ticketadvantage.services.model.PendingEvent;
import com.ticketadvantage.services.model.WebScrapper;
import com.ticketadvantage.services.service.WoOMiddleRulesResource;
import com.ticketadvantage.services.transactions.RecordTransaction;

/**
 * @author jmiller
 *
 */
@WebListener
@Service
public class DrHWebEventBatch extends BaseSiteBatch implements Runnable, ServletContextListener {
	private static final Logger LOGGER = Logger.getLogger(DrHWebEventBatch.class);
	private static final TicketAdvantageGmailOath TicketAdvantageGmailOath = new TicketAdvantageGmailOath();
	private static final SportsInsightsSite SportsInsightSite = new SportsInsightsSite("https://sportsinsights.actionnetwork.com", "mojaxsventures@gmail.com", "Jaxson17");
	private static final DrhParser DHP = new DrhParser();
	private static Integer SCRAPPER_SERVER;
	private static EventsPackage eventsPackage;
	private volatile boolean shutdown = false;
	private final WebScrapperDB WEBSCRAPPERDB = new WebScrapperDB();
	private RecordTransaction RECORDTRANSACTION;
	private final List<WebScrapper> scrappers = new ArrayList<WebScrapper>();
	private SiteProcessor siteProcessor;


	static {
		// Get the properties
		try {
			final Properties prop = new Properties();
			final InputStream in = new FileInputStream("/opt/tomcat/server.properties");
			prop.load(in);
			SCRAPPER_SERVER = Integer.parseInt(prop.getProperty("SCRAPPER_SERVER"));
			LOGGER.error("SCRAPPER_SERVER: " + SCRAPPER_SERVER);
			in.close();

			eventsPackage = SportsInsightSite.getNextDaySport("mlblines");
//			eventsPackage = SportsInsightSite.getDayOfSport("mlblines");
			DHP.setEventsPackage(eventsPackage);
		} catch (IOException ioe) {
			LOGGER.error(ioe.getMessage(), ioe);
		} catch (Throwable e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	/**
	 * 
	 */
	public DrHWebEventBatch() {
		super();
		LOGGER.info("Entering FastEmailEventBatch()");
		LOGGER.info("Exiting FastEmailEventBatch()");
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		final EventPackage ep = SportsInsightSite.getEventById("925", "MLB");
		LOGGER.error("EventPackage: " + ep);

		// scrapperEventBatch.run();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
    @Override
    public void contextInitialized(ServletContextEvent event) {
    		LOGGER.error("Entering contextInitialized()");
    		RECORDTRANSACTION = new RecordTransaction(RECORDEVENTDB);
		new Thread(this).start();
		LOGGER.error("Exiting contextInitialized()");
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextDestroyed(ServletContextEvent event) {
	    	LOGGER.error("Entering contextDestroyed()");

	    	this.shutdown = true;

	    	LOGGER.error("Exiting contextDestroyed()");
    }

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		LOGGER.info("Entering run()");
		List<WebScrapper> tempScrappers = null;
		int intervalcheck = 0;

		try {
			tempScrappers = WEBSCRAPPERDB.findAll();
			for (WebScrapper wscrapper : tempScrappers) {
				String name = wscrapper.getScrappername();
				if ("DrH".equals(name)) {
					scrappers.add(wscrapper);
				}
			}
			LOGGER.error("Scrappers.size(): " + scrappers.size());

			// Compare the dates to order them appropriately
			Collections.sort(scrappers, new Comparator<WebScrapper>() {
				public int compare(WebScrapper o1, WebScrapper o2) {
					if (o1.getUserid() == null || o2.getUserid() == null)
						return 0;
					return o1.getUserid().compareTo(o2.getUserid());
				}
			});

			final WebScrapper webscrapper = scrappers.get(0);
			intervalcheck = Integer.parseInt(webscrapper.getPullinginterval());
			final Accounts siteAccount = scrappers.get(0).getSources().iterator().next();
			final String siteType = siteAccount.getSitetype();
			if (siteType != null && "SkullBet".equals(siteType)) {
				siteProcessor = new SkullBetProcessSite(siteAccount.getUrl(), 
					siteAccount.getUsername(), 
					siteAccount.getPassword(), 
					siteAccount.getIsmobile(), 
					siteAccount.getShowrequestresponse());
				
				if (siteProcessor != null) {
					siteProcessor.setTimezone(siteAccount.getTimezone());
					siteProcessor.getHttpClientWrapper().setupHttpClient(siteAccount.getProxylocation());
					siteProcessor.setProcessTransaction(false);
					siteProcessor.loginToSite(siteProcessor.getHttpClientWrapper().getUsername(), siteProcessor.getHttpClientWrapper().getPassword());
				}
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		try {
			int counter = 0;
			while (!shutdown) {
				try {
					checkSite("global", scrappers, null);

					// Sleep for a while
					Thread.sleep(intervalcheck * 1000);
				} catch (InterruptedException e) {
					LOGGER.error("InterruptedException");
					// good practice
					Thread.currentThread().interrupt();
					return;
				} catch (Throwable th) {
					LOGGER.error("Throwable in thread", th);
				}

				if (counter++ == 30) {
					// Check every 30 minutes
					scrappers.clear();
					tempScrappers = WEBSCRAPPERDB.findAll();
					for (WebScrapper wscrapper : tempScrappers) {
						String name = wscrapper.getScrappername();
						if ("DrH".equals(name)) {
							scrappers.add(wscrapper);
						}
					}

					// Compare the dates to order them appropriately
					Collections.sort(scrappers, new Comparator<WebScrapper>() {
						public int compare(WebScrapper o1, WebScrapper o2) {
							if (o1.getUserid() == null || o2.getUserid() == null)
								return 0;
							return o1.getUserid().compareTo(o2.getUserid());
						}
					});

					counter = 0;
				}
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}
	}

	/**
	 * 
	 * @param siteType
	 * @param baseScrappers
	 * @param anythingObject
	 * @throws BatchException
	 */
	public void checkSite(String siteType, List<WebScrapper> baseScrappers, Object anythingObject) throws BatchException {
		LOGGER.info("Entering checkSite()");

		try {
			if (siteProcessor != null) {
				// Process the pending bet
				final Set<PendingEvent> sitePendingEvents = siteProcessor.getPendingBets(siteProcessor.getHttpClientWrapper().getHost(), 
						siteProcessor.getHttpClientWrapper().getUsername(), anythingObject);
				LOGGER.debug("sitePendingEvents: " + sitePendingEvents);
	
				if (sitePendingEvents != null && !sitePendingEvents.isEmpty()) {
					LOGGER.debug("sitePendingEvents: " + sitePendingEvents);
					for (BaseScrapper scrapper : baseScrappers) {
						LOGGER.debug("Scrapper: " + scrapper);
	
						// Check if we need to place a transaction
						checkSitePendingEvent(siteType, sitePendingEvents, scrapper);
	
						// Delete pending events
						deleteRemovedPendingEvents(siteType, sitePendingEvents, scrapper);
					}
				} else if (sitePendingEvents != null && sitePendingEvents.isEmpty()) {
					// Still need to check for complete transactions
					for (BaseScrapper scrapper : baseScrappers) {
						LOGGER.debug("Scrapper: " + scrapper);
	
						// Delete pending events
						deleteRemovedPendingEvents(siteType, sitePendingEvents, scrapper);
					}
				} else {
					// if it's NULL then don't do anything
				}
			}
		} catch (BatchException be) {
			throw be;
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		LOGGER.info("Exiting checkSite()");
	}

	/**
	 * 
	 * @param scrapper
	 * @param pendingEvent
	 * @param eventPackage
	 */
	protected void placeSiteTransactions(BaseScrapper scrapper, PendingEvent pendingEvent, EventPackage eventPackage) {
		LOGGER.info("Entering placeTransactions()");
		LOGGER.error("PendingEvent: " + pendingEvent);

		try {
			final String type = pendingEvent.getEventtype();
			final WebScrapper wscrapper = (WebScrapper)scrapper;
			LOGGER.debug("wscrapper: " + wscrapper);

			Set<Accounts> destinations = null;
			if (wscrapper.getFullfill() != null && wscrapper.getFullfill().booleanValue()) {
				destinations = wscrapper.getOrderdestinations();
			} else {
				destinations = wscrapper.getDestinations();
			}

			// Send a message for WoO
			if (wscrapper.getUserid().longValue() == 1) {
				try {
					final SendText sendText = new SendText();
					sendText.setOAUTH2_TOKEN(TicketAdvantageGmailOath.getAccessToken());
					sendText.sendTextWithMessage(wscrapper.getMobiletext(), "DrH " + pendingEvent.getRotationid() + " " + pendingEvent.getEventtype() + " " + pendingEvent.getTeam() + " " + pendingEvent.getLineplusminus() + pendingEvent.getLine() + " " + pendingEvent.getJuiceplusminus() + pendingEvent.getJuice());
				} catch (Exception be) {
					LOGGER.error(be.getMessage(), be);
				}
			}

			// If we are middling, then execute middle rules
			String playType = "Standard";
			if (wscrapper.getMiddlerules().booleanValue()) {
				playType = "Middle";
			}

			String team1 = "";
			String team2 = "";
			final EventPackage evp = SportsInsightSite.getEventById(pendingEvent.getRotationid(), "MLB");
			LOGGER.error("EventPackage: " + evp);

			if (evp != null) {
				team1 = evp.getTeamone().getTeam();
				team2 = evp.getTeamtwo().getTeam();
			}

			if ("spread".equals(type)) {
				// RECORDTRANSACTION
			} else if ("total".equals(type)) {
				final Long totalId = RECORDTRANSACTION.recordTotalEvent(pendingEvent.getLinetype(),
					pendingEvent.getGamesport(), pendingEvent.getGametype(),
					pendingEvent.getRotationid(), team1, team2,
					pendingEvent.getLineplusminus(), pendingEvent.getLine(),
					pendingEvent.getJuiceplusminus(), pendingEvent.getJuice(),
					pendingEvent.getEventdate(), pendingEvent.getGamedate(),
					wscrapper.getTotallineadjustment(), wscrapper.getTotaljuiceindicator(),
					wscrapper.getTotaljuice(), wscrapper.getTotaljuiceadjustment(),
					pendingEvent.getCustomerid(), wscrapper.getScrappername(),
					wscrapper.getMobiletext(), playType, wscrapper.getUserid(),
					wscrapper.getTotalmaxamount(), wscrapper.getTotalmaxamount(), wscrapper.getFullfill(),
					wscrapper.getCheckdupgame(), wscrapper.getPlayotherside(),
					wscrapper.getEnableretry(), wscrapper.getOrderamount(), destinations, null, true, wscrapper.getSendtextforaccount(),
					wscrapper.getHumanspeed());

				// If we are middling, then execute middle rules
				if (wscrapper.getMiddlerules().booleanValue()) {
					new WoOMiddleRulesResource(
						totalId,
						"total",
						wscrapper.getMiddledestinations(),
						RECORDEVENTDB,
						evp,
						wscrapper.getMobiletext(),
						wscrapper.getUserid(),
						wscrapper.getTotalmaxamount());
				}
			} else if ("ml".equals(type)) {
				final Long mlId = RECORDTRANSACTION.recordMlEvent(pendingEvent.getLinetype(),
					pendingEvent.getGamesport(), pendingEvent.getGametype(),
					pendingEvent.getRotationid(), team1, team2,
					pendingEvent.getLineplusminus(), pendingEvent.getLine(),
					pendingEvent.getJuiceplusminus(), pendingEvent.getJuice(),
					pendingEvent.getEventdate(), pendingEvent.getGamedate(),
					wscrapper.getMllineadjustment(), wscrapper.getMlindicator(),
					wscrapper.getMlline(), pendingEvent.getCustomerid(),
					wscrapper.getScrappername(), wscrapper.getMobiletext(), playType,
					wscrapper.getUserid(), wscrapper.getMlmaxamount(), wscrapper.getMlmaxamount(), wscrapper.getFullfill(),
					wscrapper.getCheckdupgame(), wscrapper.getPlayotherside(),
					wscrapper.getEnableretry(), wscrapper.getOrderamount(), destinations, null, true, wscrapper.getSendtextforaccount(),
					wscrapper.getHumanspeed());

				// If we are middling, then execute middle rules
				if (wscrapper.getMiddlerules().booleanValue()) {
					new WoOMiddleRulesResource(
						mlId,
						"ml",
						wscrapper.getMiddledestinations(),
						RECORDEVENTDB,
						evp,
						wscrapper.getMobiletext(),
						wscrapper.getUserid(),
						wscrapper.getMlmaxamount());
				}
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		LOGGER.info("Exiting placeTransactions()");
	} 

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.batch.processevent.BaseSiteBatch#persistPendingEvent(boolean, com.ticketadvantage.services.model.PendingEvent, com.ticketadvantage.services.model.PendingEvent)
	 */
	@Override
	protected void persistPendingEvent(boolean found, PendingEvent sitePendingEvent, PendingEvent copySitePendingEvent) {
		LOGGER.info("Entering persistPendingEvent()");

		try {
			if (!found) {
				LOGGER.debug("copySitePendingEvent: " + copySitePendingEvent);
				LOGGER.debug("copySitePendingEvent.getDoposturl(): " + copySitePendingEvent.getDoposturl());
				copySitePendingEvent = PENDINGEVENTDB.persist(copySitePendingEvent);
				LOGGER.debug("Persisted event: " + copySitePendingEvent);
			}
		} catch (Throwable t) {
			t.printStackTrace();
			LOGGER.error(t.getMessage(), t);
		}

		LOGGER.info("Exiting persistPendingEvent()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.batch.processevent.BaseSiteBatch#doProcessPendingEvent(com.ticketadvantage.services.model.PendingEvent)
	 */
	@Override
	protected void doProcessPendingEvent(PendingEvent pendingEvent) {
		try {
			// Write this event to the DB for this user
			siteProcessor.doProcessPendingEvent(pendingEvent);
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.batch.processevent.BaseSiteBatch#setupAccountEvents(com.ticketadvantage.services.model.BaseRecordEvent, com.ticketadvantage.services.model.BaseScrapper)
	 */
	@Override
	protected void setupAccountEvents(BaseRecordEvent event, BaseScrapper scrapper) {
		LOGGER.info("Entering setupAccountEvents()");
		LOGGER.debug("Event: " + event);

		// Setup all the accounts
		setupAccounts(event, ((WebScrapper)scrapper).getDestinations(), scrapper.getEnableretry(), scrapper.getSendtextforaccount(), scrapper.getHumanspeed());

		LOGGER.info("Exiting setupAccountEvents()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.batch.processevent.BaseSiteBatch#setupBuyOrder(com.ticketadvantage.services.model.BaseRecordEvent, com.ticketadvantage.services.model.BaseScrapper, java.lang.Float, java.lang.Boolean)
	 */
	@Override
	protected void setupBuyOrder(BaseRecordEvent event, BaseScrapper scrapper, Float numunits, Boolean islean) {
		LOGGER.info("Entering setupBuyOrder()");
		LOGGER.debug("Event: " + event);

		// Setup all the accounts
		setupBuyOrderAccounts(event, copyAccounts(((WebScrapper)scrapper).getOrderdestinations()), scrapper, numunits, islean);

		LOGGER.info("Exiting setupBuyOrder()");		
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.batch.processevent.BaseSiteBatch#getScrapperSources(com.ticketadvantage.services.model.BaseScrapper)
	 */
	@Override
	protected Set<Accounts> getScrapperSources(BaseScrapper scrapper) {
		LOGGER.info("Entering getScrapperSources()");
		LOGGER.info("Exiting getScrapperSources()");
		return ((WebScrapper)scrapper).getSources();
	}

	@Override
	protected void setupBestPriceBuyOrder(BaseRecordEvent event, BaseScrapper scrapper, Float numunits,
			Boolean islean) {
		// TODO Auto-generated method stub		
	}

	@Override
	protected GlobalScrapper getGlobalScrapper() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void setGlobalScrapper(GlobalScrapper globalScrapper) {
		// TODO Auto-generated method stub
		
	}
}