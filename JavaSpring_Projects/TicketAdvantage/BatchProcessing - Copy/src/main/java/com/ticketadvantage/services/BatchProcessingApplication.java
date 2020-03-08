package com.ticketadvantage.services;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

import com.ticketadvantage.services.batch.ScrapperEventBatch;

public class BatchProcessingApplication extends ResourceConfig {
	private static Logger theLogger =
			 Logger.getLogger(BatchProcessingApplication.class.getName());

	/**
	 * 
	 */
	public BatchProcessingApplication() {
        register(new BatchProcessingBinder());
        packages(true, "com.mypackage.rest");
 
        // Enable Tracing support.
 //       property(ServerProperties.TRACING, "ALL");
 //       theLogger.setLevel(Level.FINEST);
 //       registerInstances(new LoggingFilter(theLogger, true));

//		final ScrapperEventBatch scrapperEventBatch = new ScrapperEventBatch();
//		new Thread(scrapperEventBatch).start();
	}
}