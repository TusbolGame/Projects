/**
 * 
 */
package com.ticketadvantage.services.errorhandling;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;



/**
 * @author jmiller
 *
 */
@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {
	private final Logger log = Logger.getLogger(GenericExceptionMapper.class);
	
	public static final int GENERIC_APP_ERROR_CODE = 5001;		
	public static final String BLOG_POST_URL = "http://www.ticketadvantage.com";

	/**
	 * 
	 */
	public GenericExceptionMapper() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see javax.ws.rs.ext.ExceptionMapper#toResponse(java.lang.Throwable)
	 */
	@Override
	public Response toResponse(Throwable exception) {
		log.debug("Entering toResponse()");
		ErrorMessage errorMessage = new ErrorMessage();
		errorMessage.setCode(GENERIC_APP_ERROR_CODE);
		setHttpStatus(exception, errorMessage);
		errorMessage.setMessage(exception.getMessage());
		StringWriter errorStackTrace = new StringWriter();
		exception.printStackTrace(new PrintWriter(errorStackTrace));
		errorMessage.setDeveloperMessage(errorStackTrace.toString());
		errorMessage.setLink(BLOG_POST_URL);
		log.debug("Exception: ", exception);
		log.debug("Exiting toResponse()");

		return Response.status(errorMessage.getStatus())
				.entity(errorMessage)
				.type(MediaType.APPLICATION_JSON)
				.build();
	}

	/**
	 * 
	 * @param ex
	 * @param errorMessage
	 */
	private void setHttpStatus(Throwable ex, ErrorMessage errorMessage) {
		if (ex instanceof AppException) {
			errorMessage.setStatus(((AppException) ex).getStatus());
			errorMessage.setCode(((AppException) ex).getCode());
		} else if (ex instanceof WebApplicationException) {
			errorMessage.setStatus(((WebApplicationException)ex).getResponse().getStatus());
		} else {
			errorMessage.setStatus(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()); //defaults to internal server error 500
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}
