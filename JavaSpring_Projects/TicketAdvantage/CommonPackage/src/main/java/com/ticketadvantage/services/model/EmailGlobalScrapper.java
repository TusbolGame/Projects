package com.ticketadvantage.services.model;

public class EmailGlobalScrapper extends GlobalScrapper {
	private EmailAccounts source;

    /**
     * 
     */
	public EmailGlobalScrapper() {
		super();
	}

	/**
	 * @return the source
	 */
	public EmailAccounts getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(EmailAccounts source) {
		this.source = source;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EmailGlobalScrapper [source=" + source + "] " + super.toString();
	}
}