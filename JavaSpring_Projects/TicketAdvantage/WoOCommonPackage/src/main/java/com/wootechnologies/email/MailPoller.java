package com.wootechnologies.email;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.sun.mail.imap.IMAPFolder;

/**
 * 
 * @author jmiller
 *
 */
public abstract class MailPoller {
	private static final Logger LOGGER = Logger.getLogger(MailPoller.class);
    private final static int SLEEP_TIME = 300000; // check mail every 5 min
    private IMAPFolder folder;
    private String name = "";
    private int previousCount = -1;
    private int diffCount = -1;
    protected Timer timer;

    /**
     * 
     * @param folder
     */
    public MailPoller(IMAPFolder folder) {
        this.folder = folder;
    }

    /**
     * 
     * @return
     */
    private boolean poll() {
    	LOGGER.info("Entering poll()");

        try {
            int newCount = folder.getMessageCount();
            if (previousCount == -1) {
            	diffCount = 0;
                previousCount = newCount;
                LOGGER.info("Exiting poll()");
                return false;
            } else {
                if (previousCount < newCount) {
                	diffCount = newCount - previousCount;
                    previousCount = newCount;
                    LOGGER.info("Exiting poll()");
                    return true;
                }
                LOGGER.info("Exiting poll()");
                return false;
            }
        } catch (Exception ex) {
            LOGGER.error("Poller Error: ", ex);
            LOGGER.info("Exiting poll()");
            return false;
        }
    }

    /**
     * 
     */
    private void periodicPoller() {
    	LOGGER.info("Entering periodicPoller()");

        TimerTask task = new TimerTask() {
        	/*
        	 * (non-Javadoc)
        	 * @see java.util.TimerTask#run()
        	 */
            @Override
            public void run() {
                if (poll())
                    onNewMessage();
            }
        };
        stop();
        timer = new Timer(name, true);
        timer.scheduleAtFixedRate(task, Calendar.getInstance().getTime(), SLEEP_TIME);

        LOGGER.info("Exiting periodicPoller()");
    }

    /**
     * 
     * @param name
     */
    public void start(String name) {
    	LOGGER.info("Entering start()");

        this.name = "MailPoller-" + name;
        Runnable r = new Runnable() {

            public void run() {
                periodicPoller();
            }
        };
        stop();
        Thread t = new Thread(r, this.name);
        t.setDaemon(true);
        t.start();

        LOGGER.info("Exiting start()");
    }

    /**
     * 
     */
    public void stop() {
    	LOGGER.info("Entering stop()");

        if (timer == null)
            return;

        timer.cancel();
        timer.purge();
        timer = null;

        LOGGER.info("Exiting stop()");
    }

    /**
     * 
     * @return
     */
    public int getDiffCount() {
    	return diffCount;
    }

    /**
     * 
     * @param folder
     */
    public void setFolder(IMAPFolder folder) {
        this.folder = folder;
    }

    /**
     * 
     */
    public abstract void onNewMessage();
}