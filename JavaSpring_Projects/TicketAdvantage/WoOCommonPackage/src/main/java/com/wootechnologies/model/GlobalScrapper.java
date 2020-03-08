package com.wootechnologies.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author jmiller
 *
 */
public class GlobalScrapper {
	private Long scrapperid;
	private String id;
	private Long userid;
    private final List<BaseScrapper> userScrappers = new ArrayList<BaseScrapper>();

    /**
     * 
     */
	public GlobalScrapper() {
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
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
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
		return "GlobalScrapper [scrapperid=" + scrapperid + ", id=" + id + ", userid=" + userid + ", userScrappers="
				+ userScrappers + "]";
	}
}