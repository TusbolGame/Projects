package com.ticketadvantage.services.linemovement;

import java.util.Date;

/**
 * 
 * @author jmiller
 *
 */
public class LineMovementLineData {
	private Long id;
	private String text;
	private String username;
	private String screenname;
	private Date tweetdate;

	/**
	 * 
	 */
	public LineMovementLineData() {
		super();
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
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
	 * @return the tweetdate
	 */
	public Date getTweetdate() {
		return tweetdate;
	}

	/**
	 * @param tweetdate the tweetdate to set
	 */
	public void setTweetdate(Date tweetdate) {
		this.tweetdate = tweetdate;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TwitterData [id=" + id + ", text=" + text + ", username=" + username + ", screenname=" + screenname
				+ ", tweetdate=" + tweetdate + "]";
	}
}
