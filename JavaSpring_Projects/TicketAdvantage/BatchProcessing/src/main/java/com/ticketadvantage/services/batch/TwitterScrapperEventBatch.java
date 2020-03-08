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

import com.ticketadvantage.services.batch.processevent.TwitterGlobalSiteBatch;
import com.ticketadvantage.services.batch.processevent.TwitterUserSiteBatch;
import com.ticketadvantage.services.db.TwitterScrapperDB;
import com.ticketadvantage.services.model.BaseScrapper;
import com.ticketadvantage.services.model.TwitterAccounts;
import com.ticketadvantage.services.model.TwitterGlobalScrapper;
import com.ticketadvantage.services.model.TwitterScrapper;
import com.ticketadvantage.services.model.TwitterUserScrapper;

/**
 * @author jmiller
 *
 */
@WebListener
@Service
public class TwitterScrapperEventBatch implements Runnable, ServletContextListener {
	private static final Logger LOGGER = Logger.getLogger(TwitterScrapperEventBatch.class);
	private volatile boolean shutdown = false;
	private final TwitterScrapperDB TWITTERSCRAPPERDB = new TwitterScrapperDB();
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
	public TwitterScrapperEventBatch() {
		super();
		LOGGER.info("Entering TwitterScrapperEventBatch()");
		LOGGER.info("Exiting TwitterScrapperEventBatch()");
	}

	/**
	 * 
	 * @author jmiller
	 *
	 */
	public class UserContainer {
		String id;
		TwitterUserSiteBatch userSiteBatch;

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
	    TwitterGlobalSiteBatch globalSiteBatch;

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
		TwitterScrapperEventBatch scrapperEventBatch = new TwitterScrapperEventBatch();
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
				final List<TwitterScrapper> scrappers = TWITTERSCRAPPERDB.findAll();
				LOGGER.debug("Scrappers.size(): " + scrappers.size());

				// First get Scrappers in individual lists
				List<TwitterUserScrapper> userList = new ArrayList<TwitterUserScrapper>();
				userList = getUserScrapperList(scrappers, userList);
				LOGGER.debug("userList.size(): " + userList.size());

				// Now get Global lists
				List<TwitterGlobalScrapper> globalList = new ArrayList<TwitterGlobalScrapper>();
				globalList = determineGlobalList(userList, globalList);
				LOGGER.debug("globalList.size(): " + globalList.size());

				int twittersize = userList.size() + globalList.size();
				
				// We have to do in 15 minute increments 1 per minute
				int pullingInterval = (twittersize * 60000);

				// User Scrapper List
				for (TwitterUserScrapper us : userList) {
					LOGGER.debug("UserScrapper: " + us);
					if (us.getUserScrappers() != null && !us.getUserScrappers().isEmpty()) {
						LOGGER.debug("NOT us.getUserScrappers().isEmpty()");
						if (uc.isEmpty()) {
							LOGGER.debug("uc is empty");
							final TwitterUserSiteBatch usb = new TwitterUserSiteBatch(pullingInterval, us);
							final UserContainer userContainer = new UserContainer();
							userContainer.id = us.getUserid() + us.getScreenname() + us.getHandleid();
							userContainer.userSiteBatch = usb;
							LOGGER.debug("UserContainer: " + userContainer);
							uc.add(userContainer);
						} else {
							boolean found = false;
							for (UserContainer userContainer : uc) {
								String uId = us.getUserid() + us.getScreenname() + us.getHandleid();
								if (uId.equals(userContainer.id)) {
									userContainer.userSiteBatch.setUserScrapper(us);
									LOGGER.debug("UserContainer found");
									found = true;
									break;
								}
							}
							if (!found) {
								final TwitterUserSiteBatch usb = new TwitterUserSiteBatch(pullingInterval, us);
								final UserContainer userContainer = new UserContainer();
								userContainer.id = us.getUserid() + us.getScreenname() + us.getHandleid();
								userContainer.userSiteBatch = usb;
								LOGGER.debug("UserContainer2: " + userContainer);
								uc.add(userContainer);
							}
						}
					} else {
						UserContainer removeContainer = null;
						for (UserContainer userContainer : uc) {
							String uId = us.getUserid() + us.getScreenname() + us.getHandleid();
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
				for (TwitterGlobalScrapper gs : globalList) {
					LOGGER.debug("GlobalScrapper: " + gs);

					if (gs.getUserScrappers() != null && !gs.getUserScrappers().isEmpty()) {
						if (gc.isEmpty()) {
							final TwitterGlobalSiteBatch gsb = new TwitterGlobalSiteBatch(pullingInterval, gs);
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
								final TwitterGlobalSiteBatch gsb = new TwitterGlobalSiteBatch(pullingInterval, gs);
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
	private List<TwitterUserScrapper> getUserScrapperList(List<TwitterScrapper> scrappers, List<TwitterUserScrapper> userScrappers) {
		LOGGER.info("Entering getUserScrapperList()");
		final Map<String, TwitterUserScrapper> scrapperMap = new HashMap<String, TwitterUserScrapper>();

		// First get Scrappers in individual lists
		for (TwitterScrapper scrapper : scrappers) {
			LOGGER.debug("Scrapper: " + scrapper);
			LOGGER.debug("scrapper.getOnoff(): " + scrapper.getOnoff());

			// Is the scrapper on?
			if (scrapper.getOnoff().booleanValue() && (scrapper.getServernumber().intValue() == SCRAPPER_SERVER.intValue())) {
				String screenname = "";
				String handleid = "";

				for (TwitterAccounts account : scrapper.getSources()) {
					if (account.getScreenname() != null && account.getHandleid() != null) {
						screenname += account.getScreenname();
						handleid += account.getHandleid();
					}
				}

				LOGGER.debug("scrapper.getUserid(): " + scrapper.getUserid());
				LOGGER.debug("screenname: " + screenname);
				LOGGER.debug("handleid: " + handleid);

				// See if a UserScrapper is already created
				if (scrapperMap.containsKey(scrapper.getUserid() + screenname + handleid)) {
					final TwitterUserScrapper uScrapper = scrapperMap.get(scrapper.getUserid() + screenname + handleid);
					uScrapper.setScreenname(screenname);
					uScrapper.setHandleid(handleid);
					uScrapper.getUserScrappers().add(scrapper);
				} else {
					final TwitterUserScrapper uScrapper = new TwitterUserScrapper();
					uScrapper.setUserid(scrapper.getUserid());
					uScrapper.setScreenname(screenname);
					uScrapper.setHandleid(handleid);
					uScrapper.getUserScrappers().add(scrapper);
					scrapperMap.put(scrapper.getUserid() + screenname + handleid, uScrapper);
				}
			}
		}

		// Put it in the List format
		for(Map.Entry<String, TwitterUserScrapper> entry : scrapperMap.entrySet()) {
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
	private List<TwitterGlobalScrapper> determineGlobalList(List<TwitterUserScrapper> userScrappers, List<TwitterGlobalScrapper> globalList) {
		LOGGER.info("Entering determineGlobalList()");
		final Map<String, TwitterGlobalScrapper> globalScrapperMap = new HashMap<String, TwitterGlobalScrapper>();
		final Map<BaseScrapper, List<BaseScrapper>> tempRemoveList = new HashMap<BaseScrapper, List<BaseScrapper>>();

		// First get Scrappers in individual lists
		for (TwitterUserScrapper userScrapper : userScrappers) {
			LOGGER.debug("UserScrapper: " + userScrapper);
			for (Iterator<BaseScrapper> iterator = userScrapper.getUserScrappers().iterator(); iterator.hasNext();) {
				TwitterScrapper scrapper = (TwitterScrapper)iterator.next();

				if (scrapper.getOnoff().booleanValue() && (scrapper.getServernumber().intValue() == SCRAPPER_SERVER.intValue())) {
					for (TwitterAccounts acct : scrapper.getSources()) {
						// First get Scrappers in individual lists
						for (TwitterUserScrapper tempUserScrapper : userScrappers) {
							for (Iterator<BaseScrapper> iterator2 = tempUserScrapper.getUserScrappers().iterator(); iterator2.hasNext();) {
								TwitterScrapper tempScrapper = (TwitterScrapper)iterator2.next();
								if (scrapper.getId().longValue() != tempScrapper.getId().longValue()) {
									for (TwitterAccounts tempAcct : tempScrapper.getSources()) {
										if (acct.getScreenname().contains(tempAcct.getScreenname()) &&
											acct.getHandleid().contains(tempAcct.getHandleid())) {
											// See if a UserScrapper is already created
											if (globalScrapperMap.containsKey(acct.getScreenname() + acct.getHandleid())) {
												final TwitterGlobalScrapper gScrapper = globalScrapperMap.get(acct.getScreenname() + acct.getHandleid());
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
												final TwitterGlobalScrapper gScrapper = new TwitterGlobalScrapper();
												gScrapper.setId(acct.getScreenname() + acct.getHandleid());
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
		for (Map.Entry<String, TwitterGlobalScrapper> entry : globalScrapperMap.entrySet()) {
			globalList.add(entry.getValue());
		}

		LOGGER.info("Exiting determineGlobalList()");
		return globalList;
	}
}