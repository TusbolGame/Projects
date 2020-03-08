package com.wootechnologies.model;

public class WebGlobalScrapper extends GlobalScrapper {
	private Accounts source;

    /**
     * 
     */
	public WebGlobalScrapper() {
		super();
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "WebGlobalScrapper [source=" + source + "] " + super.toString();
	}
}