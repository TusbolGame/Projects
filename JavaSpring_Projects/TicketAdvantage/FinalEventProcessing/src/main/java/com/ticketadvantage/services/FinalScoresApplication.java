package com.ticketadvantage.services;

import java.util.logging.Logger;

import org.glassfish.jersey.server.ResourceConfig;

public class FinalScoresApplication extends ResourceConfig {
	private static Logger theLogger =
			 Logger.getLogger(FinalScoresApplication.class.getName());

	/**
	 * 
	 */
	public FinalScoresApplication() {
        register(new FinalScoresBinder());
        packages(true, "com.mypackage.rest");
 
        // Enable Tracing support.
 //       property(ServerProperties.TRACING, "ALL");
 //       theLogger.setLevel(Level.FINEST);
 //       registerInstances(new LoggingFilter(theLogger, true));

//		final ScrapperEventBatch scrapperEventBatch = new ScrapperEventBatch();
//		new Thread(scrapperEventBatch).start();
	}
}