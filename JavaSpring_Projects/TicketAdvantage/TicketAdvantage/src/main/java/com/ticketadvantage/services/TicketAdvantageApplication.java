package com.ticketadvantage.services;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

public class TicketAdvantageApplication extends ResourceConfig {
	private static Logger theLogger =
			 Logger.getLogger(TicketAdvantageApplication.class.getName());
	
	public TicketAdvantageApplication() {
        register(new TicketAdvantgeBinder());
        packages(true, "com.mypackage.rest");
 
        // Enable Tracing support.
       property(ServerProperties.TRACING, "ALL");
        theLogger.setLevel(Level.FINEST);
//        registerInstances(new LoggingFilter(theLogger, true));
	}
}
