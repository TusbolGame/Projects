/**
 * 
 */
package com.ticketadvantage.services.batch.processevent;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.email.EmailProcessor;
import com.ticketadvantage.services.dao.email.drh.DrhProcessSite;
import com.ticketadvantage.services.dao.email.everysport247.EverySport247EmailProcessSite;
import com.ticketadvantage.services.dao.email.grupoantares.GrupoantaresEmailProcessSite;
import com.ticketadvantage.services.dao.email.krackwins.KrackWinsProcessSite;
import com.ticketadvantage.services.dao.email.linetracker.LinetrackerEmailProcessSite;
import com.ticketadvantage.services.dao.email.metallica.MetallicaEmailProcessSite;
import com.ticketadvantage.services.dao.email.procap.ProCapSportsProcessSite;
import com.ticketadvantage.services.dao.email.sportsbettingonline.SportsBettingOnlineEmailProcessSite;
import com.ticketadvantage.services.dao.email.sportsinsights.SportsInsightsEmailProcessSite;
import com.ticketadvantage.services.dao.email.sportsowlpredictions.SportsOwlPredictionsEmailProcessSite;
import com.ticketadvantage.services.dao.email.themachine.TheMachineEmailProcessSite;
import com.ticketadvantage.services.db.EmailEventDB;
import com.ticketadvantage.services.email.AccessTokenFromRefreshToken;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.Accounts;
import com.ticketadvantage.services.model.BaseRecordEvent;
import com.ticketadvantage.services.model.BaseScrapper;
import com.ticketadvantage.services.model.EmailAccounts;
import com.ticketadvantage.services.model.EmailEvent;
import com.ticketadvantage.services.model.EmailScrapper;
import com.ticketadvantage.services.model.PendingEvent;

/**
 * @author jmiller
 *
 */
public abstract class EmailBaseSiteBatch extends BaseSiteBatch {
	private static final Logger LOGGER = Logger.getLogger(EmailBaseSiteBatch.class);
	protected final EmailEventDB EMAILEVENTDB = new EmailEventDB();
	protected final List<EmailProcessor> emailProcessors = new ArrayList<EmailProcessor>();
	protected String siteType;
	protected String inet;
	protected String timezone;
	protected List<BaseScrapper> baseScrappers;

	/**
	 * 
	 */
	public EmailBaseSiteBatch() {
		super();
		LOGGER.info("Entering EmailBaseSiteBatch()");
		LOGGER.info("Exiting EmailBaseSiteBatch()");
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * @return the inet
	 */
	public String getInet() {
		return inet;
	}

	/**
	 * @param inet the inet to set
	 */
	public void setInet(String inet) {
		this.inet = inet;
	}

	/**
	 * @return the timezone
	 */
	public String getTimezone() {
		return timezone;
	}

	/**
	 * @param timezone the timezone to set
	 */
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	/**
	 * 
	 * @param emailContainer
	 */
	public synchronized void processEmail(EmailEvent emailContainer) {
		LOGGER.info("Entering processEmail()");

		try {
			for (EmailProcessor emailProcessor : this.emailProcessors) {
				try {
					if (emailProcessor.determineEmailProcessor(emailContainer, baseScrappers)) {
						LOGGER.error("emailProcessor: " + emailProcessor);
	
						// Process the email
						if (emailProcessor.processEmail(emailContainer, baseScrappers)) {
							LOGGER.debug("processEmail is true");
							LOGGER.debug("Calling getPendingBets()");
							final Set<PendingEvent> pendingEvents = emailProcessor.getPendingBets(this.siteType, emailContainer, baseScrappers);
							if (pendingEvents != null && pendingEvents.size() > 0) {
								for (PendingEvent pe : pendingEvents) {
									LOGGER.debug("PE2: " + pe);
								}
							}

							checkSite(pendingEvents);
	
							// Persist the email
							EMAILEVENTDB.persist(emailContainer);
						}
					}
				} catch (Throwable t) {
					LOGGER.error(t.getMessage(), t);
				}
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		LOGGER.info("Exiting processEmail()");
	}

	/**
	 * 
	 * @param sitePendingEvents
	 * @throws BatchException
	 */
	public void checkSite(Set<PendingEvent> sitePendingEvents) throws BatchException {
		LOGGER.info("Entering checkSite()");

		try {
			if (sitePendingEvents != null && !sitePendingEvents.isEmpty()) {
				LOGGER.debug("sitePendingEvents: " + sitePendingEvents);

				for (BaseScrapper scrapper : baseScrappers) {
					LOGGER.debug("Scrapper: " + scrapper);

					if (scrapper.getOnoff()) {
						for (PendingEvent sitePendingEvent : sitePendingEvents) {
							LOGGER.debug("PendingEvent: " + sitePendingEvent);

							final EmailScrapper emailScrapper = (EmailScrapper)scrapper;
							if (emailScrapper.getSources() != null && !emailScrapper.getSources().isEmpty()) {
								final EmailAccounts eAccount = emailScrapper.getSources().iterator().next();
								LOGGER.debug("EmailAccount: " + eAccount);

								if (eAccount.getInet().equals(sitePendingEvent.getInet()) &&
									eAccount.getAccountid().equals(sitePendingEvent.getCustomerid())) {
									// Check if we need to place a transaction
									checkSitePendingEvent(siteType, sitePendingEvents, scrapper);
								}
							}
						}
					}
				}
			}
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
	protected void determinSiteProcessor(String siteType, EmailAccounts account) throws BatchException {
		LOGGER.info("Entering determinSiteProcessor()");
		LOGGER.debug("siteType: " + siteType);
		LOGGER.debug("EmailAccount: " + account);

		if (siteType != null && "Drh".equals(siteType)) {
			final DrhProcessSite drhProcessSite = new DrhProcessSite(account.getAddress(), account.getPassword());
			drhProcessSite.setAccountname(account.getInet());
			drhProcessSite.setAccountid(account.getAccountid());
			drhProcessSite.setTimezone(account.getTimezone());
			drhProcessSite.setProcessTransaction(true);

			emailProcessors.add(drhProcessSite);
		} else if (siteType != null && "ProCapSports".equals(siteType)) {
			final ProCapSportsProcessSite proCapSportsProcessSite = new ProCapSportsProcessSite(account.getAddress(), account.getPassword());
			proCapSportsProcessSite.setAccountname(account.getInet());
			proCapSportsProcessSite.setAccountid(account.getAccountid());
			proCapSportsProcessSite.setTimezone(account.getTimezone());
			proCapSportsProcessSite.setProcessTransaction(true);

			emailProcessors.add(proCapSportsProcessSite);
		} else if (siteType != null && "KrackWins".equals(siteType)) {
			final KrackWinsProcessSite krackWinsProcessSite = new KrackWinsProcessSite(account.getAddress(), account.getPassword());
			krackWinsProcessSite.setAccountname(account.getInet());
			krackWinsProcessSite.setAccountid(account.getAccountid());
			krackWinsProcessSite.setTimezone(account.getTimezone());
			krackWinsProcessSite.setProcessTransaction(true);

			emailProcessors.add(krackWinsProcessSite);
		} else if (siteType != null && "MetallicaEmail".equals(siteType)) {
			final MetallicaEmailProcessSite metallicaEmailProcessSite = new MetallicaEmailProcessSite(account.getAddress(), account.getPassword());
			metallicaEmailProcessSite.setAccountname(account.getInet());
			metallicaEmailProcessSite.setAccountid(account.getAccountid());
			metallicaEmailProcessSite.setTimezone(account.getTimezone());
			metallicaEmailProcessSite.setProcessTransaction(true);

			emailProcessors.add(metallicaEmailProcessSite);
		} else if (siteType != null && "LinetrackerEmail".equals(siteType)) {
			final LinetrackerEmailProcessSite linetrackerEmailProcessSite = new LinetrackerEmailProcessSite(account.getAddress(), account.getPassword());
			linetrackerEmailProcessSite.setAccountname(account.getInet());
			linetrackerEmailProcessSite.setAccountid(account.getAccountid());
			linetrackerEmailProcessSite.setTimezone(account.getTimezone());
			linetrackerEmailProcessSite.setProcessTransaction(true);

			emailProcessors.add(linetrackerEmailProcessSite);
		} else if (siteType != null && "SportsBettingOnlineEmail".equals(siteType)) {
			final SportsBettingOnlineEmailProcessSite sportsBettingOnlineEmailProcessSite = new SportsBettingOnlineEmailProcessSite(account.getAddress(), account.getPassword());
			sportsBettingOnlineEmailProcessSite.setAccountname(account.getInet());
			sportsBettingOnlineEmailProcessSite.setAccountid(account.getAccountid());
			sportsBettingOnlineEmailProcessSite.setTimezone(account.getTimezone());
			sportsBettingOnlineEmailProcessSite.setProcessTransaction(true);

			emailProcessors.add(sportsBettingOnlineEmailProcessSite);
		} else if (siteType != null && "EverySport247Email".equals(siteType)) {
			final EverySport247EmailProcessSite everySport247EmailProcessSite = new EverySport247EmailProcessSite(account.getAddress(), account.getPassword());
			everySport247EmailProcessSite.setAccountname(account.getInet());
			everySport247EmailProcessSite.setAccountid(account.getAccountid());
			everySport247EmailProcessSite.setTimezone(account.getTimezone());
			everySport247EmailProcessSite.setProcessTransaction(true);

			emailProcessors.add(everySport247EmailProcessSite);
		} else if (siteType != null && "SportsOwlPredictionsEmail".equals(siteType)) {
			final SportsOwlPredictionsEmailProcessSite sportsOwlPredictionsEmail = new SportsOwlPredictionsEmailProcessSite(account.getAddress(), account.getPassword());
			sportsOwlPredictionsEmail.setAccountname(account.getInet());
			sportsOwlPredictionsEmail.setAccountid(account.getAccountid());
			sportsOwlPredictionsEmail.setTimezone(account.getTimezone());
			sportsOwlPredictionsEmail.setProcessTransaction(true);

			emailProcessors.add(sportsOwlPredictionsEmail);
		} else if (siteType != null && "TheMachineEmail".equals(siteType)) {
			final TheMachineEmailProcessSite theMachineEmailProcessSite = new TheMachineEmailProcessSite(account.getAddress(), account.getPassword());
			theMachineEmailProcessSite.setAccountname(account.getInet());
			theMachineEmailProcessSite.setAccountid(account.getAccountid());
			theMachineEmailProcessSite.setTimezone(account.getTimezone());
			theMachineEmailProcessSite.setProcessTransaction(true);

			emailProcessors.add(theMachineEmailProcessSite);
		} else if (siteType != null && "SportsInsightsEmail".equals(siteType)) {
			final SportsInsightsEmailProcessSite sportsInsightsEmailProcessSite = new SportsInsightsEmailProcessSite(account.getAddress(), account.getPassword());
			sportsInsightsEmailProcessSite.setAccountname(account.getInet());
			sportsInsightsEmailProcessSite.setAccountid(account.getAccountid());
			sportsInsightsEmailProcessSite.setTimezone(account.getTimezone());
			sportsInsightsEmailProcessSite.setProcessTransaction(true);

			emailProcessors.add(sportsInsightsEmailProcessSite);
		} else if (siteType != null && "GrupoantaresEmail".equals(siteType)) {
			final GrupoantaresEmailProcessSite grupoantaresEmailProcessSite = new GrupoantaresEmailProcessSite(account.getAddress(), account.getPassword());
			grupoantaresEmailProcessSite.setAccountname(account.getInet());
			grupoantaresEmailProcessSite.setAccountid(account.getAccountid());
			grupoantaresEmailProcessSite.setTimezone(account.getTimezone());
			grupoantaresEmailProcessSite.setProcessTransaction(true);

			emailProcessors.add(grupoantaresEmailProcessSite);
		}

		LOGGER.info("Exiting determinSiteProcessor()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.batch.processevent.BaseSiteBatch#deleteRemovedPendingEvents(java.lang.String, java.util.Set, com.ticketadvantage.services.model.BaseScrapper)
	 */
	@Override
	protected void deleteRemovedPendingEvents(String pendingType, Set<PendingEvent> sitePendingEvents, BaseScrapper scrapper) throws BatchException {
		LOGGER.info("Entering deleteRemovedPendingEvents()");
		LOGGER.info("Exiting deleteRemovedPendingEvents()");
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
			LOGGER.error(t);
		}

		LOGGER.info("Exiting persistPendingEvent()");
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
		setupAccounts(event, copyAccounts(((EmailScrapper)scrapper).getDestinations()), scrapper.getEnableretry(), scrapper.getSendtextforaccount(), scrapper.getHumanspeed());

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
		final Set<Accounts> emailAccounts = copyAccounts(((EmailScrapper)scrapper).getEmailorderdestinations());
		setupBuyOrderAccounts(event, emailAccounts, scrapper, numunits, islean);

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
		final Set<Accounts> emailAccounts = copyAccounts(((EmailScrapper)scrapper).getEmailorderdestinations());
		setupBestPriceBuyOrderAccounts(event, emailAccounts, scrapper, numunits, islean);

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
	 * 
	 * @param account
	 * @return
	 */
	protected synchronized String getAccessToken(EmailAccounts account) {
		LOGGER.info("Entering getAccessToken()");
		String accessToken = null;

		try {
			LOGGER.debug("account.getName(): " + account.getName());
			LOGGER.debug("account.getClientid(): " + account.getClientid());
			LOGGER.debug("account.getClientsecret(): " + account.getClientsecret());
			LOGGER.debug("account.getRefreshtoken(): " + account.getRefreshtoken());
			LOGGER.debug("account.getGranttype(): " + account.getGranttype());
			final AccessTokenFromRefreshToken accessTokenFromRefreshToken = new AccessTokenFromRefreshToken(account.getClientid(), account.getClientsecret(), account.getRefreshtoken(), account.getGranttype());
			accessToken = accessTokenFromRefreshToken.getAccessToken();
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		LOGGER.info("Entering getAccessToken()");
		return accessToken;
	}
}