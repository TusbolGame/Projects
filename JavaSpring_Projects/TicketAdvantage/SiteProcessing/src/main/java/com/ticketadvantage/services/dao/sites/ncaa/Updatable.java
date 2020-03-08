package com.ticketadvantage.services.dao.sites.ncaa;

import java.util.List;
import java.util.concurrent.Semaphore;

import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.sites.SiteProcessor;

public abstract class Updatable<T> extends SiteProcessor {
    protected static final Logger log = Logger.getLogger(Updatable.class);
	protected volatile long lastRefreshed = 0;
    private final int REFRESH_FREQUENCY_MILLISECONDS = 14400000; // 4 hours
    private Thread updateThread;
    private final Semaphore updateInProgress = new Semaphore(1);

    /**
     * 
     * @param siteType
     * @param host
     * @param username
     * @param password
     */
    public Updatable(String siteType, String host, String username, String password) {
    	super(siteType, host, username, password);
    }

    /**
     * 
     * @param siteType
     * @param host
     * @param username
     * @param password
     * @param isMobile
     * @param showRequestResponse
     */
    public Updatable(String siteType, String host, String username, String password, boolean isMobile, boolean showRequestResponse) {
    	super(siteType, host, username, password, isMobile, showRequestResponse);
    }

    /**
     * 
     */
    public void forceRefresh()
    {
        try
        {
            updateInProgress.acquire();
        }
        catch (InterruptedException e)
        {
            log.warn("forceRefresh Interrupted");
        }

        try
        {
            loadAllData();
        }
        catch (Exception e)
        {
            log.error("Exception while updating data from DB", e);
        }
        finally
            {
            updateInProgress.release();
        }

    }

    /**
     * 
     */
    protected void checkRefresh()
    {
        if (lastRefreshed + REFRESH_FREQUENCY_MILLISECONDS < System.currentTimeMillis()) {
            startUpdateThread();
        }
    }

    /**
     * 
     */
    private void startUpdateThread()
    {
        if (updateInProgress.tryAcquire())
        {
            updateThread = new Thread(new Runnable()
            {
                public void run()
                {
                    try
                    {
                        loadAllData();
                    }
                    catch (Exception e)
                    {
                        log.error("Exception while updating data from site", e);
                    }
                    finally
                    {
                        updateInProgress.release();
                    }
                }
            });

            updateThread.start();
        }
    }

    /**
     * implement this function to load the data from DB
     *
     * @return
     */
    protected abstract List<T> loadFromSite();

    /**
     * Implement this function to hotswap the data in memory after it was loaded from DB
     *
     * @param data
     */
    protected abstract void updateData(List<T> data);

    /**
     * 
     */
    private void loadAllData()
    {
        List<T> l = loadFromSite();
        updateData(l);
        lastRefreshed = System.currentTimeMillis();
    }

    /**
     * 
     */
    public void invalidateCache()
    {
    	lastRefreshed = 0;
    }
}
