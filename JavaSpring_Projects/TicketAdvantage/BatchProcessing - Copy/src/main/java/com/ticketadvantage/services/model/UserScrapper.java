/**
 * 
 */
package com.ticketadvantage.services.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jmiller
 *
 */
public class UserScrapper {
	private Long userid;
    private final List<Scrapper> userScrappers = new ArrayList<Scrapper>();

	/**
	 * 
	 */
	public UserScrapper() {
		super();
	}

	/**
	 * @return the userid
	 */
	public Long getUserid() {
		return userid;
	}

	/**
	 * @param userid the userid to set
	 */
	public void setUserid(Long userid) {
		this.userid = userid;
	}

	/**
	 * @return the userScrappers
	 */
	public List<Scrapper> getUserScrappers() {
		return userScrappers;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UserScrapper [userid=" + userid + ", userScrappers=" + userScrappers + "]";
	}
}