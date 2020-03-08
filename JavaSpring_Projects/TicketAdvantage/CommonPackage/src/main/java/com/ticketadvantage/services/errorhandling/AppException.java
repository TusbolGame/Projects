package com.ticketadvantage.services.errorhandling;

import javax.ws.rs.WebApplicationException;

/**
 * Class to map application related exceptions
 * 
 * @author jmiller
 *
 */
public class AppException extends WebApplicationException {

	private static final long serialVersionUID = -8999932578270387947L;
	
	/** 
	 * contains redundantly the HTTP status of the response sent back to the client in case of error, so that
	 * the developer does not have to look into the response headers. If null a default 
	 */
	private int status;
	
	/** application specific error code */
	private int code; 
		
	/** link documenting the exception */	
	private String link;
	
	/** detailed error description for developers*/
	private String developerMessage;	

	/**
	 * 
	 * @param message
	 */
	public AppException(String message) {
		super(message);
	}

	/**
	 * 
	 * @param status
	 * @param code
	 * @param message
	 * @param developerMessage
	 * @param link
	 */
	public AppException(int status, int code, String message,
			String developerMessage, String link) {
		super(message);
		this.status = status;
		this.code = code;
		this.developerMessage = developerMessage;
		this.link = link;
	}

	/**
	 * 
	 * @param status
	 * @param code
	 * @param message
	 */
	public AppException(int status, int code, String message) {
		super(message);
		this.status = status;
		this.code = status;
		this.developerMessage = message;
		this.link = "No Link";
	}

	public AppException() { }

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDeveloperMessage() {
		return developerMessage;
	}

	public void setDeveloperMessage(String developerMessage) {
		this.developerMessage = developerMessage;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}					
}