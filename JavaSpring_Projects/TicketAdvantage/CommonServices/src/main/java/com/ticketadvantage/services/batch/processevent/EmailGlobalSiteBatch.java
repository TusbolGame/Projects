/**
 * 
 */
package com.ticketadvantage.services.batch.processevent;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ticketadvantage.services.email.MessageChangedListenerIMAP;
import com.ticketadvantage.services.email.MessageCountListenerIMAP;
import com.ticketadvantage.services.email.RetrieveEmailImpl;
import com.ticketadvantage.services.model.BaseScrapper;
import com.ticketadvantage.services.model.EmailAccounts;
import com.ticketadvantage.services.model.EmailScrapper;
import com.ticketadvantage.services.model.GlobalScrapper;

/**
 * @author jmiller
 *
 */
@Service
public class EmailGlobalSiteBatch extends EmailBaseSiteBatch implements Runnable {
	private static final Logger LOGGER = Logger.getLogger(EmailGlobalSiteBatch.class);
	private GlobalScrapper globalScrapper;
	private volatile boolean shutdown = false;

	/**
	 * 
	 */
	
	public EmailGlobalSiteBatch() {
		super();
		LOGGER.info("Entering EmailGlobalSiteBatch()");
		LOGGER.info("Exiting EmailGlobalSiteBatch()");
	}

	/**
	 * 
	 */
	public EmailGlobalSiteBatch(GlobalScrapper globalScrapper) {
		super();
		LOGGER.info("Entering EmailGlobalSiteBatch()");
		LOGGER.info("Exiting EmailGlobalSiteBatch()");
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
	@Override
	public GlobalScrapper getGlobalScrapper() {
		return globalScrapper;
	}

	/**
	 * @param globalScrapper the globalScrapper to set
	 */
	@Override
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
		RetrieveEmailImpl retrieveEmailImpl = null;
		EmailAccounts account = null;

		try {
			if (globalScrapper != null && globalScrapper.getUserScrappers() != null && globalScrapper.getUserScrappers().size() > 0) {
				LOGGER.debug("globalScrapper.getUserScrappers().size(): " + globalScrapper.getUserScrappers().size());

				super.baseScrappers = globalScrapper.getUserScrappers();
				super.siteType = "global";
				final EmailScrapper emailScrapper = (EmailScrapper)globalScrapper.getUserScrappers().get(0);
				account = emailScrapper.getSources().iterator().next();
				LOGGER.debug("EmailAccount: " + account);

				final List<BaseScrapper> baseScrappers = (List<BaseScrapper>)globalScrapper.getUserScrappers();
				LOGGER.debug("Scrappers.size(): " + baseScrappers.size());

				// Compare the dates to order them appropriately
				Collections.sort(baseScrappers, new Comparator<BaseScrapper>() {
					public int compare(BaseScrapper o1, BaseScrapper o2) {
						if (o1.getUserid() == null || o2.getUserid() == null)
							return 0;
						return o1.getUserid().compareTo(o2.getUserid());
					}
				});

				for (BaseScrapper scrapper : baseScrappers) {
					final EmailScrapper eScrapper = (EmailScrapper)scrapper;
					LOGGER.debug("EmailScrapper: " + eScrapper);
					final Set<EmailAccounts> emailAccounts = eScrapper.getSources();
					for (EmailAccounts emailAccount : emailAccounts) {
						// Setup all site processors
						determinSiteProcessor(emailAccount.getSitetype(), emailAccount);
					}
				}

				// Active?
				if (account.getIsactive()) {
					if ("googlemail".equals(account.getProvider())) {
						if ("imap".equals(account.getEmailtype())) {
							retrieveEmailImpl = new RetrieveEmailImpl(account.getAddress(), account.getHost(), Integer.parseInt(account.getPort()), account.getTls());
							if ("oauth2".equals(account.getAuthenticationtype())) {
								String accessToken = getAccessToken(account);
								if (accessToken != null) {
									retrieveEmailImpl.setToken(account.getAddress(), accessToken);
								} else {
									// Try one more time
									accessToken = getAccessToken(account);
									retrieveEmailImpl.setToken(account.getAddress(), accessToken);
								}
							} else {
								retrieveEmailImpl.setCredentials(account.getAddress(), account.getPassword());
							}
	
							// Inet setup
							this.setInet(account.getInet());
							this.setTimezone(account.getTimezone());
	
							// Setup the listener
							retrieveEmailImpl.setMessageCounterListerer(new MessageCountListenerIMAP(this));
							retrieveEmailImpl.setMessageChangedListerer(new MessageChangedListenerIMAP(this));
	
							// Thead to start the email
							Thread t = new Thread(retrieveEmailImpl);
							t.setName("JPM-" + retrieveEmailImpl.getAccountName());
							t.start();
						}
					}
				} else {
					shutdown = true;
				}
			}

			while (!shutdown) {
				try {
					Thread.sleep(1000000000);
				} catch (InterruptedException e) {
					LOGGER.error("InterruptedException");
					// good practice
					Thread.currentThread().interrupt();
					return;
				} catch (Throwable  t) {
					LOGGER.error("Throwable in thread", t);
				}
			}
		} catch (Throwable t) {
			if (account != null) {
				LOGGER.error(account);
			}
			LOGGER.error(t.getMessage(), t);
		}
	}
}