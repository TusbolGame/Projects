/**
 * 
 */
package com.ticketadvantage.services.batch;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ticketadvantage.services.batch.processevent.EmailGlobalSiteBatch;
import com.ticketadvantage.services.batch.processevent.EmailUserSiteBatch;
import com.ticketadvantage.services.db.EmailScrapperDB;
import com.ticketadvantage.services.model.BaseScrapper;
import com.ticketadvantage.services.model.EmailAccounts;
import com.ticketadvantage.services.model.EmailGlobalScrapper;
import com.ticketadvantage.services.model.EmailScrapper;
import com.ticketadvantage.services.model.EmailUserScrapper;

/**
 * @author jmiller
 *
 */
@WebListener
@Service
public class EmailScrapperEventBatch implements Runnable, ServletContextListener {
	private static final Logger LOGGER = Logger.getLogger(EmailScrapperEventBatch.class);
	private volatile boolean shutdown = false;
	private final EmailScrapperDB EMAILSCRAPPERDB = new EmailScrapperDB();
	private static Integer SCRAPPER_SERVER;

	static {
		// Get the properties
		try {
			final Properties prop = new Properties();
			final InputStream in = new FileInputStream("/opt/tomcat/server.properties");
			prop.load(in);
			SCRAPPER_SERVER = Integer.parseInt(prop.getProperty("SCRAPPER_SERVER"));
			LOGGER.error("SCRAPPER_SERVER: " + SCRAPPER_SERVER);
			in.close();
		} catch (IOException ioe) {
			LOGGER.error(ioe.getMessage(), ioe);
			System.exit(99);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			System.exit(99);
		}
	}

	/**
	 * 
	 */
	public EmailScrapperEventBatch() {
		super();
		LOGGER.info("Entering EmailScrapperEventBatch()");
		LOGGER.info("Exiting EmailScrapperEventBatch()");
	}

	/**
	 * 
	 * @author jmiller
	 *
	 */
	public class UserContainer {
		String id;
	    EmailUserSiteBatch userSiteBatch;

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "UserContainer [id=" + id + ", userSiteBatch=" + userSiteBatch + "]";
		}
	}

	/**
	 * 
	 * @author jmiller
	 *
	 */
	public class GlobalContainer {
		String id;
	    EmailGlobalSiteBatch globalSiteBatch;

	    /* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "GlobalContainer [id=" + id + ", globalSiteBatch=" + globalSiteBatch + "]";
		}
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		EmailScrapperEventBatch scrapperEventBatch = new EmailScrapperEventBatch();
		scrapperEventBatch.run();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
    @Override
    public void contextInitialized(ServletContextEvent event) {
    	LOGGER.error("Entering contextInitialized()");
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
		List<UserContainer> uc = new ArrayList<UserContainer>();
		List<GlobalContainer> gc = new ArrayList<GlobalContainer>();

		while (!shutdown) {
			try {
				final List<EmailScrapper> scrappers = EMAILSCRAPPERDB.findAll();
				LOGGER.debug("Scrappers.size(): " + scrappers.size());

				// First get Scrappers in individual lists
				List<EmailUserScrapper> userList = new ArrayList<EmailUserScrapper>();
				userList = getUserScrapperList(scrappers, userList);
				LOGGER.debug("userList.size(): " + userList.size());

				// Now get Global lists
				List<EmailGlobalScrapper> globalList = new ArrayList<EmailGlobalScrapper>();
				globalList = determineGlobalList(userList, globalList);
				LOGGER.debug("globalList.size(): " + globalList.size());

				// User Scrapper List
				for (EmailUserScrapper us : userList) {
					LOGGER.debug("UserScrapper: " + us);
					if (us.getUserScrappers() != null && !us.getUserScrappers().isEmpty()) {
						LOGGER.debug("NOT us.getUserScrappers().isEmpty()");
						if (uc.isEmpty()) {
							LOGGER.debug("uc is empty");
							final EmailUserSiteBatch usb = new EmailUserSiteBatch(us);
							final UserContainer userContainer = new UserContainer();
							userContainer.id = us.getUserid() + us.getAddress() + us.getPassword() + us.getHost();
							userContainer.userSiteBatch = usb;
							LOGGER.debug("UserContainer: " + userContainer);
							uc.add(userContainer);
						} else {
							boolean found = false;
							for (UserContainer userContainer : uc) {
								String uId = us.getUserid() + us.getAddress() + us.getPassword() + us.getHost();
								if (uId.equals(userContainer.id)) {
									userContainer.userSiteBatch.setUserScrapper(us);
									LOGGER.debug("UserContainer found");
									found = true;
									break;
								}
							}
							if (!found) {
								final EmailUserSiteBatch usb = new EmailUserSiteBatch(us);
								final UserContainer userContainer = new UserContainer();
								userContainer.id = us.getUserid() + us.getAddress() + us.getPassword() + us.getHost();
								userContainer.userSiteBatch = usb;
								LOGGER.debug("UserContainer2: " + userContainer);
								uc.add(userContainer);
							}
						}
					} else {
						UserContainer removeContainer = null;
						for (UserContainer userContainer : uc) {
							String uId = us.getUserid() + us.getAddress() + us.getPassword() + us.getHost();
							if (uId.equals(userContainer.id)) {
								userContainer.userSiteBatch.setUserScrapper(null);
								userContainer.userSiteBatch = null;
								removeContainer = userContainer;
								break;
							}
						}
						if (removeContainer != null) {
							LOGGER.debug("UserContainer removed");
							uc.remove(removeContainer);
						}
					}
				}

				// Global Scrapper List
				for (EmailGlobalScrapper gs : globalList) {
					LOGGER.debug("GlobalScrapper: " + gs);
					if (gs.getUserScrappers() != null && !gs.getUserScrappers().isEmpty()) {
						if (gc.isEmpty()) {
							final EmailGlobalSiteBatch gsb = new EmailGlobalSiteBatch(gs);
							final GlobalContainer globalContainer = new GlobalContainer();
							globalContainer.id = gs.getId();
							globalContainer.globalSiteBatch = gsb;
							gc.add(globalContainer);
						} else {
							boolean found = false;
							for (GlobalContainer globalContainer : gc) {
								if (gs.getId().equals(globalContainer.id)) {
									globalContainer.globalSiteBatch.setGlobalScrapper(gs);
									found = true;
									break;
								}
							}
							if (!found) {
								final EmailGlobalSiteBatch gsb = new EmailGlobalSiteBatch(gs);
								final GlobalContainer globalContainer = new GlobalContainer();
								globalContainer.id = gs.getId();
								globalContainer.globalSiteBatch = gsb;
								gc.add(globalContainer);
							}
						}
					} else {
						GlobalContainer removeContainer = null;
						for (GlobalContainer globalContainer : gc) {
							if (gs.getId().equals(globalContainer.id)) {
								globalContainer.globalSiteBatch.setGlobalScrapper(null);
								globalContainer.globalSiteBatch = null;
								removeContainer = globalContainer;
								break;
							}
						}

						if (removeContainer != null) {
							gc.remove(removeContainer);
						}
					}
				}

				Thread.sleep(30000);
			} catch (InterruptedException e) {
				LOGGER.error("InterruptedException");
				// good practice
				Thread.currentThread().interrupt();
				return;
			} catch (Throwable  t) {
				LOGGER.error("Throwable in thread", t);
			}
		}
	}

	/**
	 * 
	 * @param scrappers
	 * @param userScrappers
	 */
	private List<EmailUserScrapper> getUserScrapperList(List<EmailScrapper> scrappers, List<EmailUserScrapper> userScrappers) {
		LOGGER.info("Entering getUserScrapperList()");
		final Map<String, EmailUserScrapper> scrapperMap = new HashMap<String, EmailUserScrapper>();

		// First get Scrappers in individual lists
		for (EmailScrapper scrapper : scrappers) {
			LOGGER.debug("Scrapper: " + scrapper);
			LOGGER.debug("scrapper.getOnoff(): " + scrapper.getOnoff());

			// Is the scrapper on?
			if (scrapper.getOnoff() && (scrapper.getServernumber().intValue() == SCRAPPER_SERVER.intValue())) {
				String address = "";
				String password = "";
				String host = "";
				for (EmailAccounts account : scrapper.getSources()) {
					if (account.getAddress() != null && account.getHost() != null) {
						address += account.getAddress();
						password += account.getPassword();
						host += account.getHost();
					}
				}

				LOGGER.debug("scrapper.getUserid(): " + scrapper.getUserid());
				LOGGER.debug("address: " + address);
				LOGGER.debug("password: " + password);
				LOGGER.debug("host: " + host);

				// See if a UserScrapper is already created
				if (scrapperMap.containsKey(scrapper.getUserid() + address + password + host)) {
					final EmailUserScrapper uScrapper = scrapperMap.get(scrapper.getUserid() + address + password + host);
					uScrapper.setAddress(address);
					uScrapper.setPassword(password);
					uScrapper.setHost(host);
					uScrapper.getUserScrappers().add(scrapper);
				} else {
					final EmailUserScrapper uScrapper = new EmailUserScrapper();
					uScrapper.setUserid(scrapper.getUserid());
					uScrapper.setAddress(address);
					uScrapper.setPassword(password);
					uScrapper.setHost(host);
					uScrapper.getUserScrappers().add(scrapper);
					scrapperMap.put(scrapper.getUserid() + address + password + host, uScrapper);
				}
			}
		}

		// Put it in the List format
		for(Map.Entry<String, EmailUserScrapper> entry : scrapperMap.entrySet()) {
			userScrappers.add(entry.getValue());
		}

		LOGGER.info("Exiting getUserScrapperList()");
		return userScrappers;
	}

	/**
	 * 
	 * @param userScrappers
	 * @param globalList
	 * @return
	 */
	private List<EmailGlobalScrapper> determineGlobalList(List<EmailUserScrapper> userScrappers, List<EmailGlobalScrapper> globalList) {
		LOGGER.info("Entering determineGlobalList()");
		final Map<String, EmailGlobalScrapper> globalScrapperMap = new HashMap<String, EmailGlobalScrapper>();
		final Map<BaseScrapper, List<BaseScrapper>> tempRemoveList = new HashMap<BaseScrapper, List<BaseScrapper>>();

		// First get Scrappers in individual lists
		for (EmailUserScrapper userScrapper : userScrappers) {
			LOGGER.debug("UserScrapper: " + userScrapper);
			for (Iterator<BaseScrapper> iterator = userScrapper.getUserScrappers().iterator(); iterator.hasNext();) {
				EmailScrapper scrapper = (EmailScrapper)iterator.next();
				if (scrapper.getOnoff() && (scrapper.getServernumber().intValue() == SCRAPPER_SERVER.intValue())) {
					for (EmailAccounts acct : scrapper.getSources()) {
						// First get Scrappers in individual lists
						for (EmailUserScrapper tempUserScrapper : userScrappers) {
							for (Iterator<BaseScrapper> iterator2 = tempUserScrapper.getUserScrappers().iterator(); iterator2.hasNext();) {
								EmailScrapper tempScrapper = (EmailScrapper)iterator2.next();
								if (scrapper.getId().longValue() != tempScrapper.getId().longValue()) {
									for (EmailAccounts tempAcct : tempScrapper.getSources()) {
										if (acct.getAddress().contains(tempAcct.getAddress()) &&
											acct.getPassword().contains(tempAcct.getPassword()) &&
											acct.getHost().equals(tempAcct.getHost())) {
											// See if a UserScrapper is already created
											if (globalScrapperMap.containsKey(acct.getAddress()+acct.getHost())) {
												final EmailGlobalScrapper gScrapper = globalScrapperMap.get(acct.getAddress()+acct.getHost());
												LOGGER.debug("GlobalScrapper: " + gScrapper);
												boolean sfound = false;
												boolean tfound = false;
												for (BaseScrapper sScrapper : gScrapper.getUserScrappers()) {
													if (sScrapper.getId().longValue() == scrapper.getId().longValue()) {
														sfound = true;
													} else if (sScrapper.getId().longValue() == tempScrapper.getId().longValue()) {
														tfound = true;
													}
												}
												if (!sfound) {
													gScrapper.getUserScrappers().add(scrapper);
													tempRemoveList.put(scrapper, userScrapper.getUserScrappers());
													// userScrapper.userScrappers.remove(scrapper);
												}
												if (!tfound) {
													gScrapper.getUserScrappers().add(tempScrapper);
													tempRemoveList.put(tempScrapper, tempUserScrapper.getUserScrappers());
													// tempUserScrapper.userScrappers.remove(tempScrapper);
												}
											} else {
												final EmailGlobalScrapper gScrapper = new EmailGlobalScrapper();
												gScrapper.setId(acct.getAddress()+acct.getHost());
												gScrapper.getUserScrappers().add(scrapper);
												gScrapper.getUserScrappers().add(tempScrapper);
												tempRemoveList.put(scrapper, userScrapper.getUserScrappers());
												tempRemoveList.put(tempScrapper, tempUserScrapper.getUserScrappers());
												// userScrapper.userScrappers.remove(scrapper);
												// tempUserScrapper.userScrappers.remove(tempScrapper);
												globalScrapperMap.put(gScrapper.getId(), gScrapper);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		// Put it in the List format
		for (Map.Entry<BaseScrapper, List<BaseScrapper>> entry : tempRemoveList.entrySet()) {
			entry.getValue().remove(entry.getKey());
		}

		// Put it in the List format
		for (Map.Entry<String, EmailGlobalScrapper> entry : globalScrapperMap.entrySet()) {
			globalList.add(entry.getValue());
		}

		LOGGER.info("Exiting determineGlobalList()");
		return globalList;
	}
}