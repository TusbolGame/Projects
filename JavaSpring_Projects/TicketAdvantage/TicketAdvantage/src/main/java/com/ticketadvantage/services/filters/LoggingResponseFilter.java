package com.ticketadvantage.services.filters;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;


@Provider
public class LoggingResponseFilter
		implements ContainerResponseFilter {

	private static final Logger logger = Logger.getLogger(LoggingResponseFilter.class);

	public void filter(ContainerRequestContext requestContext,
			ContainerResponseContext responseContext) throws IOException {
		final String method = requestContext.getMethod();

//		logger.debug("Requesting " + method + " for path " + requestContext.getUriInfo().getPath());
		final Object entity = responseContext.getEntity();
		if (entity != null) {
//			logger.debug("Response " + new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(entity));
		}
		
	}
}