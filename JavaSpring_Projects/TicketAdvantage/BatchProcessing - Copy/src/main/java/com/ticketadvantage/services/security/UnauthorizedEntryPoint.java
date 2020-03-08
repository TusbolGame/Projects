/**
 * 
 */
package com.ticketadvantage.services.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.ticketadvantage.services.errorhandling.AppErrorCodes;
import com.ticketadvantage.services.errorhandling.AppErrorMessage;
import com.ticketadvantage.services.errorhandling.ErrorMessage;

/**
 * @author jmiller
 *
 */
public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {
	private static final Logger LOGGER = Logger.getLogger(UnauthorizedEntryPoint.class);

	/**
	 * 
	 */
	public UnauthorizedEntryPoint() {
		super();
		LOGGER.info("Entering UnauthorizedEntryPoint()");
		LOGGER.info("Exiting UnauthorizedEntryPoint()");
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.web.AuthenticationEntryPoint#commence(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.core.AuthenticationException)
	 */
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
			throws IOException, ServletException {
		LOGGER.info("Entering commence()");
		ErrorMessage errorMessage = new ErrorMessage();
		errorMessage.setCode(AppErrorCodes.UNAUTHORIZED_ACCESS);
		errorMessage.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		errorMessage.setMessage(AppErrorMessage.UNAUTHORIZED_ACCESS);
		StringWriter errorStackTrace = new StringWriter();
		authException.printStackTrace(new PrintWriter(errorStackTrace));
		errorMessage.setDeveloperMessage(errorStackTrace.toString());
		LOGGER.debug("AuthenticationException: ", authException);
		String json = "{\"status\":" + errorMessage.getStatus() + ",\"code\":" + errorMessage.getCode() + 
				",\"message\":\"" + errorMessage.getMessage() + "\",\"link\":\"\"" +  
				",\"developerMessage\":\"" + errorMessage.getDeveloperMessage() + "\"}";
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
	    LOGGER.info("Exiting commence()");
	    response.getWriter().write(json);
//		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Authentication token was either missing or invalid.");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}
}
