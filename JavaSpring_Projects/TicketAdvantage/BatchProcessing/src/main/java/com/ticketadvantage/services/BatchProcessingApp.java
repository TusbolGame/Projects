package com.ticketadvantage.services;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

public class BatchProcessingApp extends Application {

	public BatchProcessingApp() {
		super();
	}

	@Override
    public Set<Class<?>> getClasses() {
        return new HashSet<Class<?>>() {{

            // Add LoggingFilter.
//            add(LoggingFilter.class);
        }};
    }
}
