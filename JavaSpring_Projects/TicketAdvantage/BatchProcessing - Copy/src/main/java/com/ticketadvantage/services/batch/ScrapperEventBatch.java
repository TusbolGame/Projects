/**
 * 
 */
package com.ticketadvantage.services.batch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ticketadvantage.services.batch.processevent.GlobalSiteBatch;
import com.ticketadvantage.services.batch.processevent.UserSiteBatch;
import com.ticketadvantage.services.db.ScrapperDB;
import com.ticketadvantage.services.model.Accounts;
import com.ticketadvantage.services.model.GlobalScrapper;
import com.ticketadvantage.services.model.Scrapper;
import com.ticketadvantage.services.model.UserScrapper;

/**
 * @author jmiller
 *
 */
@WebListener
@Service
public class ScrapperEventBatch implements Runnable, ServletContextListener {
	private static final Logger LOGGER = Logger.getLogger(ScrapperEventBatch.class);
	private ScrapperEventBatch SCRAPPER_EVENT_BATCH = null;
	private volatile boolean shutdown = false;
	private final ScrapperDB SCRAPPERDB = new ScrapperDB(true);

	/**
	 * 
	 */
	public ScrapperEventBatch() {
		super();
		LOGGER.info("Entering ScrapperEventBatch()");
		LOGGER.info("Exiting ScrapperEventBatch()");
	}
	
	public class UserContainer {
		Long id;
	    UserSiteBatch userSiteBatch;

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
	    GlobalSiteBatch globalSiteBatch;

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
		ScrapperEventBatch scrapperEventBatch = new ScrapperEventBatch();
		scrapperEventBatch.run();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
    @Override
    public void contextInitialized(ServletContextEvent event) {
    	LOGGER.error("Entering contextInitialized()");
		if (SCRAPPER_EVENT_BATCH == null) {
			SCRAPPER_EVENT_BATCH = new ScrapperEventBatch();
			new Thread(SCRAPPER_EVENT_BATCH).start();
		}
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
    	this.SCRAPPERDB.complete();
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
				final List<Scrapper> scrappers = SCRAPPERDB.findAll();
				LOGGER.debug("Scrappers.size(): " + scrappers.size());

				// First get Scrappers in individual lists
				List<UserScrapper> userList = new ArrayList<UserScrapper>();
				userList = getUserScrapperList(scrappers, userList);

				// Now get Global lists
				List<GlobalScrapper> globalList = new ArrayList<GlobalScrapper>();
				globalList = determineGlobalList(userList, globalList);

				// User Scrapper List
				for (UserScrapper us : userList) {
					LOGGER.debug("UserScrapper: " + us);
					if (us.getUserScrappers() != null && !us.getUserScrappers().isEmpty()) {
						LOGGER.debug("NOT us.getUserScrappers().isEmpty()");
						if (uc.isEmpty()) {
							LOGGER.debug("uc is empty");
							UserSiteBatch usb = new UserSiteBatch(us);
							UserContainer userContainer = new UserContainer();
							userContainer.id = us.getUserid();
							userContainer.userSiteBatch = usb;
							LOGGER.debug("UserContainer: " + userContainer);
							uc.add(userContainer);
						} else {
							boolean found = false;
							for (UserContainer userContainer : uc) {
								if (us.getUserid().longValue() == userContainer.id) {
									userContainer.userSiteBatch.setUserScrapper(us);
									LOGGER.debug("UserContainer found");
									found = true;
									break;
								}
							}
							if (!found) {
								UserSiteBatch usb = new UserSiteBatch(us);
								UserContainer userContainer = new UserContainer();
								userContainer.id = us.getUserid();
								userContainer.userSiteBatch = usb;
								LOGGER.debug("UserContainer2: " + userContainer);
								uc.add(userContainer);
							}
						}
					} else {
						UserContainer removeContainer = null;
						for (UserContainer userContainer : uc) {
							if (us.getUserid().longValue() == userContainer.id) {
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
				for (GlobalScrapper gs : globalList) {
					LOGGER.debug("GlobalScrapper: " + gs);
					if (gs.getUserScrappers() != null && !gs.getUserScrappers().isEmpty()) {
						if (gc.isEmpty()) {
							GlobalSiteBatch gsb = new GlobalSiteBatch(gs);
							GlobalContainer globalContainer = new GlobalContainer();
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
								GlobalSiteBatch gsb = new GlobalSiteBatch(gs);
								GlobalContainer globalContainer = new GlobalContainer();
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

				Thread.sleep(10000);
			} catch (InterruptedException e) {
				LOGGER.error("InterruptedException");
				// good practice
				Thread.currentThread().interrupt();
				return;
			} catch (Throwable  t) {
				LOGGER.error("Throwable in thread", t);
			} finally {
				
			}
		}
		this.SCRAPPERDB.complete();
	}

	/**
	 * 
	 * @param scrappers
	 * @param userScrappers
	 */
	private List<UserScrapper> getUserScrapperList(List<Scrapper> scrappers, List<UserScrapper> userScrappers) {
		LOGGER.info("Entering getUserScrapperList()");
		final Map<Long, UserScrapper> scrapperMap = new HashMap<Long, UserScrapper>();

		// First get Scrappers in individual lists
		for (Scrapper scrapper : scrappers) {
			LOGGER.debug("Scrapper: " + scrapper);
			LOGGER.debug("scrapper.getOnoff(): " + scrapper.getOnoff());
			// Is the scrapper on?
			if (scrapper.getOnoff()) {
				// See if a UserScrapper is already created
				if (scrapperMap.containsKey(scrapper.getUserid())) {
					UserScrapper uScrapper = scrapperMap.get(scrapper.getUserid());
					uScrapper.getUserScrappers().add(scrapper);
				} else {
					final UserScrapper uScrapper = new UserScrapper();
					uScrapper.setUserid(scrapper.getUserid());
					uScrapper.getUserScrappers().add(scrapper);
					scrapperMap.put(scrapper.getUserid(), uScrapper);
				}
			}
		}

		// Put it in the List format
		for(Map.Entry<Long, UserScrapper> entry : scrapperMap.entrySet()) {
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
	private List<GlobalScrapper> determineGlobalList(List<UserScrapper> userScrappers, List<GlobalScrapper> globalList) {
		LOGGER.info("Entering determineGlobalList()");
		final Map<String, GlobalScrapper> globalScrapperMap = new HashMap<String, GlobalScrapper>();
		final Map<Scrapper, List<Scrapper>> tempRemoveList = new HashMap<Scrapper, List<Scrapper>>();

		// First get Scrappers in individual lists
		for (UserScrapper userScrapper : userScrappers) {
			for (Iterator<Scrapper> iterator = userScrapper.getUserScrappers().iterator(); iterator.hasNext();) {
				Scrapper scrapper = iterator.next();
				for (Accounts acct : scrapper.getSources()) {
					// First get Scrappers in individual lists
					for (UserScrapper tempUserScrapper : userScrappers) {
						for (Iterator<Scrapper> iterator2 = tempUserScrapper.getUserScrappers().iterator(); iterator2.hasNext();) {
							Scrapper tempScrapper = iterator2.next();
							if (scrapper.getId().longValue() != tempScrapper.getId().longValue()) {
								for (Accounts tempAcct : tempScrapper.getSources()) {
									if (acct.getUrl().contains(tempAcct.getUrl()) && 
										acct.getUsername().equals(tempAcct.getUsername())) {
										// See if a UserScrapper is already created
										if (globalScrapperMap.containsKey(acct.getUrl()+acct.getUsername())) {
											GlobalScrapper gScrapper = globalScrapperMap.get(acct.getUrl()+acct.getUsername());
											LOGGER.debug("GlobalScrapper: " + gScrapper);
											boolean sfound = false;
											boolean tfound = false;
											for (Scrapper sScrapper : gScrapper.getUserScrappers()) {
												if (sScrapper.getId().longValue() != scrapper.getId().longValue()) {
													sfound = true;
												} else if (sScrapper.getId().longValue() != tempScrapper.getId().longValue()) {
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
											final GlobalScrapper gScrapper = new GlobalScrapper();
											gScrapper.setId(acct.getUrl()+acct.getUsername());
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
		
		// Put it in the List format
		for(Map.Entry<Scrapper, List<Scrapper>> entry : tempRemoveList.entrySet()) {
			entry.getValue().remove(entry.getKey());
		}

		// Put it in the List format
		for(Map.Entry<String, GlobalScrapper> entry : globalScrapperMap.entrySet()) {
			globalList.add(entry.getValue());
		}

		LOGGER.info("Exiting determineGlobalList()");
		return globalList;
	}
}