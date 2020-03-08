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

import com.ticketadvantage.services.batch.processevent.PinnyWebGlobalSiteBatch;
import com.ticketadvantage.services.batch.processevent.PinnyWebUserSiteBatch;
import com.ticketadvantage.services.db.WebScrapperDB;
import com.ticketadvantage.services.model.Accounts;
import com.ticketadvantage.services.model.BaseScrapper;
import com.ticketadvantage.services.model.GlobalScrapper;
import com.ticketadvantage.services.model.UserScrapper;
import com.ticketadvantage.services.model.WebGlobalScrapper;
import com.ticketadvantage.services.model.WebScrapper;
import com.ticketadvantage.services.model.WebUserScrapper;

/**
 * @author jmiller
 *
 */
@WebListener
@Service
public class PinnyWebScrapperEventBatch implements Runnable, ServletContextListener {
	private static final Logger LOGGER = Logger.getLogger(PinnyWebScrapperEventBatch.class);
	private volatile boolean shutdown = false;
	private final WebScrapperDB WEBSCRAPPERDB = new WebScrapperDB();
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
	public PinnyWebScrapperEventBatch() {
		super();
		LOGGER.info("Entering PinnyWebScrapperEventBatch()");
		LOGGER.info("Exiting PinnyWebScrapperEventBatch()");
	}
	
	public class UserContainer {
		String id;
	    PinnyWebUserSiteBatch userSiteBatch;

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "UserContainer [id=" + id + ", userSiteBatch=" + userSiteBatch + "]";
		}
	}

	public class GlobalContainer {
		String id;
	    PinnyWebGlobalSiteBatch globalSiteBatch;

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
		PinnyWebScrapperEventBatch scrapperEventBatch = new PinnyWebScrapperEventBatch();
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
				final List<WebScrapper> scrappers = WEBSCRAPPERDB.findAll();
				LOGGER.debug("Scrappers.size(): " + scrappers.size());

				// First get Scrappers in individual lists
				List<WebUserScrapper> userList = new ArrayList<WebUserScrapper>();
				userList = getUserScrapperList(scrappers, userList);
				LOGGER.debug("userList.size(): " + userList.size());

				// Now get Global lists
				List<WebGlobalScrapper> globalList = new ArrayList<WebGlobalScrapper>();
				globalList = determineGlobalList(userList, globalList);
				LOGGER.debug("globalList.size(): " + globalList.size());

				// User Scrapper List
				for (WebUserScrapper us : userList) {
					LOGGER.debug("UserScrapper: " + us);
					if (us.getUserScrappers() != null && !us.getUserScrappers().isEmpty()) {
						LOGGER.debug("NOT us.getUserScrappers().isEmpty()");
						if (uc.isEmpty()) {
							LOGGER.debug("uc is empty");
							final PinnyWebUserSiteBatch usb = new PinnyWebUserSiteBatch(us);
							usb.setUserId(us.getUserid());

							final UserContainer userContainer = new UserContainer();
							userContainer.id = us.getUserid() + us.getAccountids() + us.getAccounturls();
							userContainer.userSiteBatch = usb;
							LOGGER.debug("UserContainer: " + userContainer);
							uc.add(userContainer);
						} else {
							boolean found = false;
							for (UserContainer userContainer : uc) {
								String uId = us.getUserid() + us.getAccountids() + us.getAccounturls();
								if (uId.equals(userContainer.id)) {
									userContainer.userSiteBatch.setUserScrapper(us);
									LOGGER.debug("UserContainer found");
									found = true;
									break;
								}
							}
							if (!found) {
								final PinnyWebUserSiteBatch usb = new PinnyWebUserSiteBatch(us);
								usb.setUserId(us.getUserid());

								final UserContainer userContainer = new UserContainer();
								userContainer.id = us.getUserid() + us.getAccountids() + us.getAccounturls();
								userContainer.userSiteBatch = usb;
								LOGGER.debug("UserContainer2: " + userContainer);
								uc.add(userContainer);
							}
						}
					} else {
						UserContainer removeContainer = null;
						for (UserContainer userContainer : uc) {
							String uId = us.getUserid() + us.getAccountids() + us.getAccounturls();
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
				for (WebGlobalScrapper gs : globalList) {
					LOGGER.debug("GlobalScrapper: " + gs);
					if (gs.getUserScrappers() != null && !gs.getUserScrappers().isEmpty()) {
						if (gc.isEmpty()) {
							final PinnyWebGlobalSiteBatch gsb = new PinnyWebGlobalSiteBatch(gs);
							gsb.setUserId(gs.getUserid());

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
								final PinnyWebGlobalSiteBatch gsb = new PinnyWebGlobalSiteBatch(gs);
								gsb.setUserId(gs.getUserid());

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
	private List<WebUserScrapper> getUserScrapperList(List<WebScrapper> scrappers, List<WebUserScrapper> userScrappers) {
		LOGGER.info("Entering getUserScrapperList()");
		final Map<String, WebUserScrapper> scrapperMap = new HashMap<String, WebUserScrapper>();

		// First get Scrappers in individual lists
		for (WebScrapper scrapper : scrappers) {
			LOGGER.debug("Scrapper: " + scrapper);
			LOGGER.debug("scrapper.getOnoff(): " + scrapper.getOnoff());
			// Is the scrapper on?
			if (scrapper.getOnoff() && (scrapper.getServernumber().intValue() == SCRAPPER_SERVER.intValue())) {
				LOGGER.debug("WebScrapper: " + scrapper);
				String accountids = "";
				String accounturls = "";
				for (Accounts account : scrapper.getSources()) {
					if (account.getUsername() != null && account.getUrl() != null) {
						accountids += account.getUsername();
						accounturls += account.getUrl();
					}
				}

				// See if a UserScrapper is already created
				if (scrapperMap.containsKey(scrapper.getUserid() + accountids + accounturls)) {
					final WebUserScrapper uScrapper = scrapperMap.get(scrapper.getUserid() + accountids + accounturls);
					uScrapper.setScrapperid(scrapper.getId());
					uScrapper.setUserid(scrapper.getUserid());
					uScrapper.setAccountids(accountids);
					uScrapper.setAccounturls(accounturls);
					uScrapper.getUserScrappers().add(scrapper);
				} else {
					final WebUserScrapper uScrapper = new WebUserScrapper();
					uScrapper.setScrapperid(scrapper.getId());
					uScrapper.setUserid(scrapper.getUserid());
					uScrapper.setAccountids(accountids);
					uScrapper.setAccounturls(accounturls);
					uScrapper.getUserScrappers().add(scrapper);
					scrapperMap.put(scrapper.getUserid() + accountids + accounturls, uScrapper);
				}
			}
		}

		// Put it in the List format
		for(Map.Entry<String, WebUserScrapper> entry : scrapperMap.entrySet()) {
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
	private List<WebGlobalScrapper> determineGlobalList(List<WebUserScrapper> userScrappers, List<WebGlobalScrapper> globalList) {
		LOGGER.info("Entering determineGlobalList()");
		final Map<String, WebGlobalScrapper> globalScrapperMap = new HashMap<String, WebGlobalScrapper>();
		final Map<BaseScrapper, List<BaseScrapper>> tempRemoveList = new HashMap<BaseScrapper, List<BaseScrapper>>();

		// First get Scrappers in individual lists
		for (UserScrapper userScrapper : userScrappers) {
			LOGGER.debug("UserScrapper: " + userScrapper);
			for (Iterator<BaseScrapper> iterator = userScrapper.getUserScrappers().iterator(); iterator.hasNext();) {
				final WebScrapper scrapper = (WebScrapper)iterator.next();
				if (scrapper.getOnoff() && (scrapper.getServernumber().intValue() == SCRAPPER_SERVER.intValue())) {
					LOGGER.debug("WebScrapper: " + scrapper);
					for (Accounts acct : scrapper.getSources()) {
						// First get Scrappers in individual lists
						for (WebUserScrapper tempUserScrapper : userScrappers) {
							for (Iterator<BaseScrapper> iterator2 = tempUserScrapper.getUserScrappers().iterator(); iterator2.hasNext();) {
								final WebScrapper tempScrapper = (WebScrapper)iterator2.next();
								if (scrapper.getId().longValue() != tempScrapper.getId().longValue()) {
									for (Accounts tempAcct : tempScrapper.getSources()) {
										if (acct.getUrl().contains(tempAcct.getUrl()) && 
											acct.getUsername().equals(tempAcct.getUsername())) {
											// See if a UserScrapper is already created
											if (globalScrapperMap.containsKey(acct.getUrl()+acct.getUsername())) {
												final GlobalScrapper gScrapper = globalScrapperMap.get(acct.getUrl()+acct.getUsername());
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
												final WebGlobalScrapper gScrapper = new WebGlobalScrapper();
												gScrapper.setScrapperid(scrapper.getId());
												gScrapper.setId(acct.getUrl()+acct.getUsername());
												gScrapper.setUserid(scrapper.getUserid());
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
		for(Map.Entry<BaseScrapper, List<BaseScrapper>> entry : tempRemoveList.entrySet()) {
			entry.getValue().remove(entry.getKey());
		}

		// Put it in the List format
		for(Map.Entry<String, WebGlobalScrapper> entry : globalScrapperMap.entrySet()) {
			globalList.add(entry.getValue());
		}

		LOGGER.info("Exiting determineGlobalList()");
		return globalList;
	}
}