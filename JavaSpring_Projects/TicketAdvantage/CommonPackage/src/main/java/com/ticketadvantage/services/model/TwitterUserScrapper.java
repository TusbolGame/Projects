/**
 * 
 */
package com.ticketadvantage.services.model;

/**
 * @author jmiller
 *
 */
public class TwitterUserScrapper extends UserScrapper {
	private String screenname;
	private String handleid;

	/**
	 * 
	 */
	public TwitterUserScrapper() {
		super();
	}

	/**
	 * @return the screenname
	 */
	public String getScreenname() {
		return screenname;
	}

	/**
	 * @param screenname the screenname to set
	 */
	public void setScreenname(String screenname) {
		this.screenname = screenname;
	}

	/**
	 * @return the handleid
	 */
	public String getHandleid() {
		return handleid;
	}

	/**
	 * @param handleid the handleid to set
	 */
	public void setHandleid(String handleid) {
		this.handleid = handleid;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TwitterUserScrapper [screenname=" + screenname + ", handleid=" + handleid + "]";
	}
}