package com.ticketadvantage.services.errorhandling;

import java.io.Serializable;

public class BatchException extends Exception implements Serializable {
	private static final long serialVersionUID = 1L;

	/** application specific error code */
	private int errorcode = BatchErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION;
	private String errormessage = BatchErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION;
	private String html = "";
	private String bexception = "";

	/**
	 * 
	 */
	public BatchException() {
		super();
	}

	/**
	 * 
	 * @param exception
	 */
	public BatchException(String exception) {
		super(exception);
	}

	/**
	 * 
	 * @param code
	 * @param message
	 * @param exception
	 */
	public BatchException(int code, String message, String exception) {
		super(exception);
		this.errorcode = code;
		this.errormessage = message;
		this.bexception = exception;
	}

	/**
	 * 
	 * @param code
	 * @param message
	 * @param exception
	 * @param html
	 */
	public BatchException(int code, String message, String exception, String html) {
		super(exception);
		this.errorcode = code;
		this.errormessage = message;
		this.bexception = exception;
		if (html != null) {
			this.html = html;
		}
	}

	/**
	 * @return the errorcode
	 */
	public int getErrorcode() {
		return errorcode;
	}

	/**
	 * @param errorcode the errorcode to set
	 */
	public void setErrorcode(int errorcode) {
		this.errorcode = errorcode;
	}

	/**
	 * @return the errormessage
	 */
	public String getErrormessage() {
		return errormessage;
	}

	/**
	 * @param errormessage the errormessage to set
	 */
	public void setErrormessage(String errormessage) {
		this.errormessage = errormessage;
	}

	/**
	 * @return the bexception
	 */
	public String getBexception() {
		return bexception;
	}

	/**
	 * @param bexception the bexception to set
	 */
	public void setBexception(String bexception) {
		this.bexception = bexception;
	}

	/**
	 * @return the html
	 */
	public String getHtml() {
		return html;
	}

	/**
	 * @param html the html to set
	 */
	public void setHtml(String html) {
		this.html = html;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BatchException [errorcode=" + errorcode + ", errormessage=" + errormessage + ", html=" + html + "] " + super.toString();
	}
}