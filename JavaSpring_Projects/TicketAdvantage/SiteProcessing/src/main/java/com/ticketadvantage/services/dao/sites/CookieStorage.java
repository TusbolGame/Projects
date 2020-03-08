/**
 * 
 */
package com.ticketadvantage.services.dao.sites;

import java.util.Date;

/**
 * @author jmiller
 *
 */
public class CookieStorage {
	private String cookies;
	private Date cookieTimestamp;

	/**
	 * 
	 */
	public CookieStorage() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * @return the cookies
	 */
	public String getCookies() {
		return cookies;
	}

	/**
	 * @param cookies the cookies to set
	 */
	public void setCookies(String cookies) {
		this.cookies = cookies;
	}

	/**
	 * @return the cookieTimestamp
	 */
	public Date getCookieTimestamp() {
		return cookieTimestamp;
	}

	/**
	 * @param cookieTimestamp the cookieTimestamp to set
	 */
	public void setCookieTimestamp(Date cookieTimestamp) {
		this.cookieTimestamp = cookieTimestamp;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CookieStorage [cookies=" + cookies + ", cookieTimestamp=" + cookieTimestamp + "]";
	}
}
