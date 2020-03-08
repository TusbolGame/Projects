package com.ticketadvantage.services.model;

public class TwitterGlobalScrapper extends GlobalScrapper {
	private TwitterAccounts source;

    /**
     * 
     */
	public TwitterGlobalScrapper() {
		super();
	}

	/**
	 * @return the source
	 */
	public TwitterAccounts getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(TwitterAccounts source) {
		this.source = source;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TwitterGlobalScrapper [source=" + source + "] " + super.toString();
	}
}