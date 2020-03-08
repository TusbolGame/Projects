package com.ticketadvantage.services.model;

import java.util.ArrayList;
import java.util.List;

public class GlobalScrapper {
	private String id;
	private Accounts source;
    private final List<Scrapper> userScrappers = new ArrayList<Scrapper>();

    /**
     * 
     */
	public GlobalScrapper() {
		super();
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
	 * @return the source
	 */
	public Accounts getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(Accounts source) {
		this.source = source;
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
		return "GlobalScrapper [id=" + id + ", source=" + source + ", userScrappers=" + userScrappers + "]";
	}
}
