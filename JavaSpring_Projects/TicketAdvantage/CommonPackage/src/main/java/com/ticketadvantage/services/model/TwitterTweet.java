/**
 * 
 */
package com.ticketadvantage.services.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * @author jmiller
 *
 */
@Entity
@Table(name = "twittertweet")
@XmlRootElement(name = "twittertweet")
@XmlAccessorType(XmlAccessType.NONE)
public class TwitterTweet implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "twittertweet_generator")
	@SequenceGenerator(name="twittertweet_generator", sequenceName = "twittertweet_id_seq", allocationSize=1)
	@Column(name = "id", unique = true, nullable = false)
	@XmlElement
	private Long id;

	@Column(name = "tweetid", unique = false, nullable = false)
	@XmlElement(nillable=false)
	private Long tweetid;

	@Column(name = "tweettext", unique = false, nullable = false)
	@XmlElement(nillable=true)
	private String tweettext;

	@Column(name = "username", unique = false, nullable = false)
	@XmlElement(nillable=true)
	private String username;

	@Column(name = "screenname", unique = false, nullable = false)
	@XmlElement(nillable=true)
	private String screenname;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tweetdate", unique = false, nullable = true)
	@XmlElement
	private Date tweetdate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "datecreated", unique = false, nullable = true)
	@XmlElement
	private Date datecreated;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "datemodified", unique = false, nullable = true)
	@XmlElement
	private Date datemodified;

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
	 * @return the tweetid
	 */
	public Long getTweetid() {
		return tweetid;
	}

	/**
	 * @param tweetid the tweetid to set
	 */
	public void setTweetid(Long tweetid) {
		this.tweetid = tweetid;
	}

	/**
	 * @return the tweettext
	 */
	public String getTweettext() {
		return tweettext;
	}

	/**
	 * @param tweettext the tweettext to set
	 */
	public void setTweettext(String tweettext) {
		this.tweettext = tweettext;
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

	/**
	 * @return the datecreated
	 */
	public Date getDatecreated() {
		return datecreated;
	}

	/**
	 * @param datecreated the datecreated to set
	 */
	public void setDatecreated(Date datecreated) {
		this.datecreated = datecreated;
	}

	/**
	 * @return the datemodified
	 */
	public Date getDatemodified() {
		return datemodified;
	}

	/**
	 * @param datemodified the datemodified to set
	 */
	public void setDatemodified(Date datemodified) {
		this.datemodified = datemodified;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TwitterTweet [id=" + id + ", tweetid=" + tweetid + ", tweettext=" + tweettext + ", username=" + username
				+ ", screenname=" + screenname + ", tweetdate=" + tweetdate + ", datecreated=" + datecreated
				+ ", datemodified=" + datemodified + "]";
	}
}