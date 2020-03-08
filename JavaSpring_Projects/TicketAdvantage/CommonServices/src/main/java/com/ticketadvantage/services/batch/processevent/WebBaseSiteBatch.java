/**
 * 
 */
package com.ticketadvantage.services.batch.processevent;

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.sites.SiteProcessor;
import com.ticketadvantage.services.dao.sites.agsoftware.AGSoftwareProcessSite;
import com.ticketadvantage.services.dao.sites.betbuckeye.BetBuckeyeProcessSite;
import com.ticketadvantage.services.dao.sites.heritagesports.GsbettingProcessSite;
import com.ticketadvantage.services.dao.sites.iol.IolSportsProcessSite;
import com.ticketadvantage.services.dao.sites.linetracker.LineTrackerProcessSite;
import com.ticketadvantage.services.dao.sites.linetracker.SkullBetProcessSite;
import com.ticketadvantage.services.dao.sites.metallica.MetallicaProcessSite;
import com.ticketadvantage.services.dao.sites.tangiers.TangiersProcessSite;
import com.ticketadvantage.services.dao.sites.tdsports.AbcWageringTDSportsProcessSite;
import com.ticketadvantage.services.dao.sites.tdsports.Action23TDSportsProcessSite;
import com.ticketadvantage.services.dao.sites.tdsports.BetBigCityTDSportsProcessSite;
import com.ticketadvantage.services.dao.sites.tdsports.BetProPlusTDSportsProcessSite;
import com.ticketadvantage.services.dao.sites.tdsports.BetallstarTDSportsProcessSite;
import com.ticketadvantage.services.dao.sites.tdsports.BetgrandeTDSportsProcessSite;
import com.ticketadvantage.services.dao.sites.tdsports.FalconTDSportsProcessSite;
import com.ticketadvantage.services.dao.sites.tdsports.FireOnSportsTDSportsProcessSite;
import com.ticketadvantage.services.dao.sites.tdsports.GotoHccTDSportsProcessSite;
import com.ticketadvantage.services.dao.sites.tdsports.Green444TDSportsProcessSite;
import com.ticketadvantage.services.dao.sites.tdsports.LvActionTDSportsProcessSite;
import com.ticketadvantage.services.dao.sites.tdsports.MyWagerLiveTDSportsProcessSite;
import com.ticketadvantage.services.dao.sites.tdsports.PlaySports365TDSportsProcessSite;
import com.ticketadvantage.services.dao.sites.tdsports.PlayWpsTDSportsProcessSite;
import com.ticketadvantage.services.dao.sites.tdsports.SbPlayTDSportsProcessSite;
import com.ticketadvantage.services.dao.sites.tdsports.TDSportsProcessSite;
import com.ticketadvantage.services.dao.sites.tdsports.YoPigTDSportsProcessSite;
import com.ticketadvantage.services.dao.sites.tdsportseight.TDSportsEightProcessSite;
import com.ticketadvantage.services.dao.sites.tdsportseleven.TDSportsElevenProcessSite;
import com.ticketadvantage.services.dao.sites.tdsportsfive.TDSportsFiveProcessSite;
import com.ticketadvantage.services.dao.sites.tdsportsfour.AbcwebTDSportsProcessSite;
import com.ticketadvantage.services.dao.sites.tdsportsnine.TDSportsNineProcessSite;
import com.ticketadvantage.services.dao.sites.tdsportsone.TDSportsOneProcessSite;
import com.ticketadvantage.services.dao.sites.tdsportsseven.TDSportsSevenProcessSite;
import com.ticketadvantage.services.dao.sites.tdsportsten.TDSportsTenProcessSite;
import com.ticketadvantage.services.dao.sites.tdsportsthree.TDSportsThreeProcessSite;
import com.ticketadvantage.services.dao.sites.tdsportstwelve.TDSportsTwelveProcessSite;
import com.ticketadvantage.services.dao.sites.tdsportstwo.TDSportsTwoProcessSite;
import com.ticketadvantage.services.dao.sites.wagershack.WagerShackMobileProcessSite;
import com.ticketadvantage.services.dao.sites.wagerus.WagerusProcessSite;
import com.ticketadvantage.services.db.WebScrapperDB;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.Accounts;
import com.ticketadvantage.services.model.BaseRecordEvent;
import com.ticketadvantage.services.model.BaseScrapper;
import com.ticketadvantage.services.model.GlobalScrapper;
import com.ticketadvantage.services.model.PendingEvent;
import com.ticketadvantage.services.model.WebScrapper;

/**
 * @author jmiller
 *
 */
public class WebBaseSiteBatch extends BaseSiteBatch {
	private static final Logger LOGGER = Logger.getLogger(WebBaseSiteBatch.class);
	protected SiteProcessor siteProcessor = null;
	protected final WebScrapperDB WEBSCRAPPERDB = new WebScrapperDB();

	/**
	 * 
	 */
	public WebBaseSiteBatch() {
		super();
		LOGGER.info("Entering WebBaseSiteBatch()");
		LOGGER.info("Exiting WebBaseSiteBatch()");
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
	 * @param siteType
	 * @param account
	 */
	protected void determinSiteProcessor(String siteType, Accounts account) throws BatchException {
		LOGGER.info("Entering determinSiteProcessor()");
		LOGGER.debug("siteType: " + siteType);
		LOGGER.debug("Account: " + account);

		if (siteType != null && "Metallica".equals(siteType)) {
			siteProcessor = new MetallicaProcessSite(account.getUrl(), account.getUsername(), account.getPassword(),
					account.getIsmobile(), account.getShowrequestresponse());
		} else if (siteType != null && "BetBuckeye".equals(siteType)) {
			siteProcessor = new BetBuckeyeProcessSite(account.getUrl(), account.getUsername(), account.getPassword(),
					account.getIsmobile(), account.getShowrequestresponse());
		} else if (siteType != null && "SkullBet".equals(siteType)) {
			siteProcessor = new SkullBetProcessSite(account.getUrl(), account.getUsername(), account.getPassword(),
					account.getIsmobile(), account.getShowrequestresponse());
		} else if (siteType != null && "LineTracker".equals(siteType)) {
			siteProcessor = new LineTrackerProcessSite(account.getUrl(), account.getUsername(), account.getPassword(),
					account.getIsmobile(), account.getShowrequestresponse());
		} else if (siteType != null && "AGSoftware".equals(siteType)) {
			siteProcessor = new AGSoftwareProcessSite(account.getUrl(), account.getUsername(), account.getPassword(),
					account.getIsmobile(), account.getShowrequestresponse());
		} else if (siteType != null && "Green444TDSports".equals(siteType)) {
			siteProcessor = new Green444TDSportsProcessSite(account.getUrl(), account.getUsername(),
					account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if (siteType != null && "BetBigCityTDSports".equals(siteType)) {
			siteProcessor = new BetBigCityTDSportsProcessSite(account.getUrl(), account.getUsername(),
					account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if (siteType != null && "SbPlayTDSports".equals(siteType)) {
			siteProcessor = new SbPlayTDSportsProcessSite(account.getUrl(), account.getUsername(),
					account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if (siteType != null && "PlayWpsTDSports".equals(siteType)) {
			siteProcessor = new PlayWpsTDSportsProcessSite(account.getUrl(), account.getUsername(),
					account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if (siteType != null && "PlaySports365TDSports".equals(siteType)) {
			siteProcessor = new PlaySports365TDSportsProcessSite(account.getUrl(), account.getUsername(),
					account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if (siteType != null && "GotoHccTDSports".equals(siteType)) {
			siteProcessor = new GotoHccTDSportsProcessSite(account.getUrl(), account.getUsername(),
					account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if (siteType != null && "LvActionTDSports".equals(siteType)) {
			siteProcessor = new LvActionTDSportsProcessSite(account.getUrl(), account.getUsername(),
					account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if (siteType != null && "FireOnSportsTDSports".equals(siteType)) {
			siteProcessor = new FireOnSportsTDSportsProcessSite(account.getUrl(), account.getUsername(),
					account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if (siteType != null && "TDSportsOne".equals(siteType)) {
			siteProcessor = new TDSportsOneProcessSite(account.getUrl(), account.getUsername(), account.getPassword(),
					account.getIsmobile(), account.getShowrequestresponse());
		} else if (siteType != null && "TDSportsSeven".equals(siteType)) {
			siteProcessor = new TDSportsSevenProcessSite(account.getUrl(), account.getUsername(), account.getPassword(),
					account.getIsmobile(), account.getShowrequestresponse());
		} else if (siteType != null && "YoPigTDSports".equals(siteType)) {
			siteProcessor = new YoPigTDSportsProcessSite(account.getUrl(), account.getUsername(), account.getPassword(),
					account.getIsmobile(), account.getShowrequestresponse());
		} else if (siteType != null && "BetgrandeTDSports".equals(siteType)) {
			siteProcessor = new BetgrandeTDSportsProcessSite(account.getUrl(), account.getUsername(),
					account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if (siteType != null && "AbcWageringTDSports".equals(siteType)) {
			siteProcessor = new AbcWageringTDSportsProcessSite(account.getUrl(), account.getUsername(),
					account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if (siteType != null && "IolSports".equals(siteType)) {
			siteProcessor = new IolSportsProcessSite(account.getUrl(), account.getUsername(), account.getPassword(),
					account.getIsmobile(), account.getShowrequestresponse());
		} else if (siteType != null && "TDSports".equals(siteType)) {
			siteProcessor = new TDSportsProcessSite(account.getUrl(), account.getUsername(), account.getPassword(),
					account.getIsmobile(), account.getShowrequestresponse());
		} else if (siteType != null && "TDSportsFive".equals(siteType)) {
			siteProcessor = new TDSportsFiveProcessSite(account.getUrl(), account.getUsername(), account.getPassword(),
					account.getIsmobile(), account.getShowrequestresponse());
		} else if (siteType != null && "TDSportsThree".equals(siteType)) {
			siteProcessor = new TDSportsThreeProcessSite(account.getUrl(), account.getUsername(),
					account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if (siteType != null && "TDSportsNine".equals(siteType)) {
			siteProcessor = new TDSportsNineProcessSite(account.getUrl(), account.getUsername(),
					account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if (siteType != null && "TDSportsEleven".equals(siteType)) {
			siteProcessor = new TDSportsElevenProcessSite(account.getUrl(), account.getUsername(),
					account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if (siteType != null && "TDSportsTwo".equals(siteType)) {
			siteProcessor = new TDSportsTwoProcessSite(account.getUrl(), account.getUsername(),
					account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if (siteType != null && "TDSportsTen".equals(siteType)) {
			siteProcessor = new TDSportsTenProcessSite(account.getUrl(), account.getUsername(),
					account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if (siteType != null && "BetallstarTDSports".equals(siteType)) {
			siteProcessor = new BetallstarTDSportsProcessSite(account.getUrl(), account.getUsername(),
					account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if (siteType != null && "MyWagerLiveTDSports".equals(siteType)) {
			siteProcessor = new MyWagerLiveTDSportsProcessSite(account.getUrl(), account.getUsername(),
					account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if (siteType != null && "Gsbetting".equals(siteType)) {
			siteProcessor = new GsbettingProcessSite(account.getUrl(), account.getUsername(), account.getPassword(),
					account.getIsmobile(), account.getShowrequestresponse());
		} else if (siteType != null && "TDSportsEight".equals(siteType)) {
			siteProcessor = new TDSportsEightProcessSite(account.getUrl(), account.getUsername(), account.getPassword(),
					account.getIsmobile(), account.getShowrequestresponse());
		} else if (siteType != null && "AbcwebTDSports".equals(siteType)) {
			siteProcessor = new AbcwebTDSportsProcessSite(account.getUrl(), account.getUsername(), account.getPassword(),
					account.getIsmobile(), account.getShowrequestresponse());
		} else if (siteType != null && "Wagerus".equals(siteType)) {
			siteProcessor = new WagerusProcessSite(account.getUrl(), account.getUsername(), account.getPassword(),
					account.getIsmobile(), account.getShowrequestresponse());
		} else if (siteType != null && "Tangiers".equals(siteType)) {
			siteProcessor = new TangiersProcessSite(account.getUrl(), account.getUsername(), account.getPassword(),
					account.getIsmobile(), account.getShowrequestresponse());
		} else if (siteType != null && "WagerShackMobile".equals(siteType)) {
			siteProcessor = new WagerShackMobileProcessSite(account.getUrl(), account.getUsername(), account.getPassword(),
					account.getIsmobile(), account.getShowrequestresponse());
		} else if (siteType != null && "Action23TDSports".equals(siteType)) {
			siteProcessor = new Action23TDSportsProcessSite(account.getUrl(), account.getUsername(), account.getPassword(),
					account.getIsmobile(), account.getShowrequestresponse());
		} else if (siteType != null && "FalconTDSports".equals(siteType)) {
			siteProcessor = new FalconTDSportsProcessSite(account.getUrl(), account.getUsername(), account.getPassword(),
					account.getIsmobile(), account.getShowrequestresponse());
		} else if (siteType != null && "BetProPlusTDSports".equals(siteType)) {
			siteProcessor = new BetProPlusTDSportsProcessSite(account.getUrl(), account.getUsername(), account.getPassword(),
					account.getIsmobile(), account.getShowrequestresponse());
		} else if (siteType != null && "TDSportsTwelve".equals(siteType)) {
			siteProcessor = new TDSportsTwelveProcessSite(account.getUrl(), account.getUsername(), account.getPassword(),
					account.getIsmobile(), account.getShowrequestresponse());
		}

		if (siteProcessor != null) {
			siteProcessor.setTimezone(account.getTimezone());
			siteProcessor.getHttpClientWrapper().setupHttpClient(account.getProxylocation());
			siteProcessor.setProcessTransaction(false);
			siteProcessor.loginToSite(siteProcessor.getHttpClientWrapper().getUsername(),
					siteProcessor.getHttpClientWrapper().getPassword());
		}

		LOGGER.info("Exiting determinSiteProcessor()");
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
	 * @see com.ticketadvantage.services.batch.processevent.BaseSiteBatch#setupParlayAccount(com.ticketadvantage.services.model.ParlayRecordEvent, com.ticketadvantage.services.model.BaseScrapper)
	 */
	@Override
	protected Accounts setupParlayAccount(BaseScrapper scrapper) {
		LOGGER.info("Entering setupParlayAccount()");
		LOGGER.debug("BaseScrapper: " + scrapper);
		Accounts destination = null;

		// Setup all the accounts
		final Set<Accounts> destinations = ((WebScrapper)scrapper).getDestinations();
		for (Accounts account : destinations) {
			LOGGER.debug("SiteType: " + account.getSitetype());
			if ("MetallicaMobile".equals(account.getSitetype())) {
				destination = account;
				break;
			}
		}

		if (destination == null) {
			// Setup all the accounts
			final Set<Accounts> orderDestinations = ((WebScrapper)scrapper).getOrderdestinations();
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
		setupBuyOrderAccounts(event, copyAccounts(((WebScrapper)scrapper).getOrderdestinations()), scrapper, numunits, islean);

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
		setupBestPriceBuyOrderAccounts(event, copyAccounts(((WebScrapper)scrapper).getOrderdestinations()), scrapper, numunits, islean);

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
		return ((WebScrapper)scrapper).getSources();
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