/**
 * 
 */
package com.wootechnologies.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jmiller
 *
 */
public class UserScrapper {
	private Long scrapperid;
	private Long userid;
    private final List<BaseScrapper> userScrappers = new ArrayList<BaseScrapper>();

	/**
	 * 
	 */
	public UserScrapper() {
		super();
	}

	/**
	 * @return the scrapperid
	 */
	public Long getScrapperid() {
		return scrapperid;
	}

	/**
	 * @param scrapperid the scrapperid to set
	 */
	public void setScrapperid(Long scrapperid) {
		this.scrapperid = scrapperid;
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
	public List<BaseScrapper> getUserScrappers() {
		return userScrappers;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UserScrapper [scrapperid=" + scrapperid + ", userid=" + userid + ", userScrappers=" + userScrappers
				+ "]";
	}
}