package com.ticketadvantage.services.batch;

import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.ticketadvantage.services.db.UpdateEventDB;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.SpreadRecordEvent;

public class UpdateGameInformationBatch implements Runnable, ServletContextListener {
	private static final Logger LOGGER = Logger.getLogger(UpdateGameInformationBatch.class);
	protected UpdateEventDB UPDATEEVENTDB = new UpdateEventDB();
	private boolean shutdown = false;

	/**
	 * 
	 */
	public UpdateGameInformationBatch() {
		super();
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		UpdateGameInformationBatch updateGameInformation = new UpdateGameInformationBatch();
		updateGameInformation.processUpdateGameInfo(updateGameInformation);
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
    @Override
    public void contextInitialized(ServletContextEvent event) {
    	LOGGER.error("Entering contextInitialized()");
//		new Thread(this).start();
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

		while (!shutdown) {
			try {

				Thread.sleep(30000);
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
	}

	/**
	 * 
	 * @param updateGameInformation
	 */
	public void processUpdateGameInfo(UpdateGameInformationBatch updateGameInformation) {
		try {
			List<SpreadRecordEvent> spreadEvents = UPDATEEVENTDB.getAllSpreadEventsForWeek();
			for (SpreadRecordEvent sre : spreadEvents) {
				LOGGER.debug("SpreadRecordEvent: " + sre);
			}
		} catch (BatchException be) {
			LOGGER.error(be.getMessage(), be);
		}
	}
}