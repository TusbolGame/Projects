/**
 * 
 */
package com.ticketadvantage.services.batch.processevent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.email.EmailProcessor;
import com.ticketadvantage.services.dao.twitter.TwitterProcessor;
import com.ticketadvantage.services.dao.twitter.jerryspicks.JerrysPicksTwitterProcessSite;
import com.ticketadvantage.services.dao.twitter.poissonsports.PoissonSportsTwitterProcessSite;
import com.ticketadvantage.services.dao.twitter.threemanweave.ThreeManWeaveTwitterProcessSite;
import com.ticketadvantage.services.db.TwitterScrapperDB;
import com.ticketadvantage.services.db.TwitterTweetDB;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.Accounts;
import com.ticketadvantage.services.model.BaseRecordEvent;
import com.ticketadvantage.services.model.BaseScrapper;
import com.ticketadvantage.services.model.GlobalScrapper;
import com.ticketadvantage.services.model.PendingEvent;
import com.ticketadvantage.services.model.TwitterAccounts;
import com.ticketadvantage.services.model.TwitterScrapper;
import com.ticketadvantage.services.model.TwitterTweet;
import com.ticketadvantage.services.twitter.TwitterFeed;

/**
 * @author jmiller
 *
 */
public class TwitterBaseSiteBatch extends BaseSiteBatch {
	private static final Logger LOGGER = Logger.getLogger(TwitterBaseSiteBatch.class);
	protected static final TwitterFeed twitterFeed = new TwitterFeed();
	protected static final TwitterScrapperDB TWITTERSCRAPPERDB = new TwitterScrapperDB();
	protected static final TwitterTweetDB TWITTERTWEETDB = new TwitterTweetDB();
	protected final List<EmailProcessor> emailProcessors = new ArrayList<EmailProcessor>();
	protected TwitterProcessor twitterProcessor;
	
	protected int pullingInterval;

	/**
	 * 
	 */
	public TwitterBaseSiteBatch() {
		super();
		LOGGER.info("Entering TwitterBaseSiteBatch()");
		LOGGER.info("Exiting TwitterBaseSiteBatch()");
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * 
	 * @param siteType
	 * @param baseScrappers
	 * @param anythingObject
	 * @throws BatchException
	 */
	public void checkSite(String siteType, List<BaseScrapper> baseScrappers, Object anythingObject) throws BatchException {
		LOGGER.info("Entering checkSite()");
		final Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.AM_PM, Calendar.AM);

		try {
			if (twitterProcessor != null) {
				final List<TwitterTweet> twitterTweets = twitterFeed.getUserTweets(twitterProcessor.getScreenname());
				LOGGER.debug("TwitterProcessor: " + twitterProcessor.getScreenname());

				for (TwitterTweet tt : twitterTweets) {
					LOGGER.debug("TwitterTweet: " + tt);

					if (tt.getTweetdate().after(cal.getTime())) {
						final TwitterTweet twt = TWITTERTWEETDB.findTwitterTweetByTweetId(tt.getTweetid());
	
						if (twt == null) {
							TWITTERTWEETDB.persist(tt);
	
							if (twitterProcessor.determineTwitterProcessor(tt, baseScrappers)) {
								// Process the Tweet
								if (twitterProcessor.processTweet(tt, baseScrappers)) {
									LOGGER.debug("siteType: " + siteType);
									LOGGER.debug("TwitterTweet: " + tt);
		
									// Process the pending bet
									final Set<PendingEvent> sitePendingEvents = twitterProcessor.getPendingBets(
											siteType,
											tt,
											baseScrappers);
									LOGGER.debug("sitePendingEvents: " + sitePendingEvents);
						
									if (sitePendingEvents != null && !sitePendingEvents.isEmpty()) {
										LOGGER.debug("sitePendingEvents: " + sitePendingEvents);
		
										for (BaseScrapper scrapper : baseScrappers) {
											LOGGER.debug("Scrapper: " + scrapper);
						
											// Check if we need to place a transaction
											checkSitePendingEvent(siteType, sitePendingEvents, scrapper);
										}
									}
								}
							}
						}
					}
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
	 * @param siteType
	 * @param account
	 */
	protected void determineTwitterProcessor(String siteType, TwitterAccounts account) throws BatchException {
		LOGGER.info("Entering determineTwitterProcessor()");
		LOGGER.debug("siteType: " + siteType);
		LOGGER.debug("Account: " + account);

		if (siteType != null && "ThreeManWeaveTwitter".equals(siteType)) {
			twitterProcessor = new ThreeManWeaveTwitterProcessSite(account.getInet(), account.getAccountid(), account.getScreenname(), account.getHandleid());
		} else if (siteType != null && "JerryPicksTwitter".equals(siteType)) {
			twitterProcessor = new JerrysPicksTwitterProcessSite(account.getInet(), account.getAccountid(), account.getScreenname(), account.getHandleid());
		} else if (siteType != null && "PoissonSportsTwitter".equals(siteType)) {
			twitterProcessor = new PoissonSportsTwitterProcessSite(account.getInet(), account.getAccountid(), account.getScreenname(), account.getHandleid());
		}

		if (twitterProcessor != null) {
			twitterProcessor.setProcessTransaction(false);
		}

		LOGGER.info("Exiting determineTwitterProcessor()");
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
				if (copySitePendingEvent != null && 
					copySitePendingEvent.getTransactiontype() != null && 
					(copySitePendingEvent.getTransactiontype().equals("Parlay") || 
					 copySitePendingEvent.getTransactiontype().equals("Action Reverse"))) {
					copySitePendingEvent = PENDINGEVENTDB.persistParlay(copySitePendingEvent);
				} else {
					copySitePendingEvent = PENDINGEVENTDB.persist(copySitePendingEvent);
				}
			}
		} catch (Throwable t) {
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
			twitterProcessor.doProcessPendingEvent(pendingEvent);
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
		setupAccounts(event, ((TwitterScrapper)scrapper).getDestinations(), scrapper.getEnableretry(), scrapper.getSendtextforaccount(), scrapper.getHumanspeed());

		LOGGER.info("Exiting setupAccountEvents()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.batch.processevent.BaseSiteBatch#setupParlayAccount(com.ticketadvantage.services.model.ParlayRecordEvent, com.ticketadvantage.services.model.BaseScrapper)
	 */
	@Override
	protected Accounts setupParlayAccount(BaseScrapper scrapper) {
		LOGGER.info("Entering setupParlayAccount()");
		LOGGER.debug("BaseScrapper: " + scrapper);
		Accounts destination = null;

		// Setup all the accounts
		final Set<Accounts> destinations = ((TwitterScrapper)scrapper).getDestinations();
		for (Accounts account : destinations) {
			LOGGER.debug("SiteType: " + account.getSitetype());
			if ("MetallicaMobile".equals(account.getSitetype())) {
				destination = account;
				break;
			}
		}

		if (destination == null) {
			// Setup all the accounts
			final Set<Accounts> orderDestinations = ((TwitterScrapper)scrapper).getTwitterorderdestinations();
			for (Accounts account : orderDestinations) {
				LOGGER.debug("SiteType: " + account.getSitetype());
				if ("MetallicaMobile".equals(account.getSitetype())) {
					destination = account;
					break;
				}
			}			
		}

		LOGGER.info("Exiting setupParlayAccount()");
		return destination;
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
		setupBuyOrderAccounts(event, copyAccounts(((TwitterScrapper)scrapper).getTwitterorderdestinations()), scrapper, numunits, islean);

		LOGGER.info("Exiting setupBuyOrder()");		
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.batch.processevent.BaseSiteBatch#setupBestPriceBuyOrder(com.ticketadvantage.services.model.BaseRecordEvent, com.ticketadvantage.services.model.BaseScrapper, java.lang.Float, java.lang.Boolean)
	 */
	@Override
	protected void setupBestPriceBuyOrder(BaseRecordEvent event, 
			BaseScrapper scrapper, 
			Float numunits, 
			Boolean islean) {
		LOGGER.info("Entering setupBestPriceBuyOrder()");
		LOGGER.debug("Event: " + event);

		// Setup all the accounts
		setupBestPriceBuyOrderAccounts(event, copyAccounts(((TwitterScrapper)scrapper).getTwitterorderdestinations()), scrapper, numunits, islean);

		LOGGER.info("Exiting setupBestPriceBuyOrder()");		
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.batch.processevent.BaseSiteBatch#getScrapperSources(com.ticketadvantage.services.model.BaseScrapper)
	 */
	@Override
	protected Set<Accounts> getScrapperSources(BaseScrapper scrapper) {
		LOGGER.info("Entering getScrapperSources()");
		LOGGER.info("Exiting getScrapperSources()");
		return null;
	}

	/**
	 * @return the pullingInterval
	 */
	public int getPullingInterval() {
		return pullingInterval;
	}

	/**
	 * @param pullingInterval the pullingInterval to set
	 */
	public void setPullingInterval(int pullingInterval) {
		this.pullingInterval = pullingInterval;
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