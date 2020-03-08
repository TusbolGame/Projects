/**
 * 
 */
package com.wootechnologies.model;

/**
 * @author jmiller
 *
 */
public class WebUserScrapper extends UserScrapper {
	private String accountids;
	private String accounturls;

	/**
	 * 
	 */
	public WebUserScrapper() {
		super();
	}

	/**
	 * @return the accountids
	 */
	public String getAccountids() {
		return accountids;
	}

	/**
	 * @param accountids the accountids to set
	 */
	public void setAccountids(String accountids) {
		this.accountids = accountids;
	}

	/**
	 * @return the accounturls
	 */
	public String getAccounturls() {
		return accounturls;
	}

	/**
	 * @param accounturls the accounturls to set
	 */
	public void setAccounturls(String accounturls) {
		this.accounturls = accounturls;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "WebUserScrapper [accountids=" + accountids + ", accounturls=" + accounturls + "] " + super.toString();
	}
}