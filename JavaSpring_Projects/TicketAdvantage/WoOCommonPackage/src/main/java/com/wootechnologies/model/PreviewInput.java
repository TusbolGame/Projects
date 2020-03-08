/**
 * 
 */
package com.wootechnologies.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author jmiller
 *
 */
@XmlRootElement()
@XmlAccessorType(XmlAccessType.NONE)
public class PreviewInput implements Serializable {
	private static final long serialVersionUID = 3156058159245563904L;

	@XmlElement
	private Integer rotationid;

	@XmlElement
	private String linetype;

	@XmlElement
	private String lineindicator;

	@XmlElement
	private String sporttype;

	@XmlElement
	private String team1;

	@XmlElement
	private String team2;

	@XmlElement
	private Long accountid;

	@XmlElement
	private String accountname;

	@XmlElement
	private String sitetype;

	@XmlElement
	private String url;

	@XmlElement
	private Boolean ismobile;

	@XmlElement
	private Boolean showrequestresponse;

	@XmlElement
	private String username;
	
	@XmlElement
	private String password;

	@XmlElement
	private String timezone;

	@XmlElement
	private String proxyname;

	/**
	 * 
	 */
	public PreviewInput() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * @return the rotationid
	 */
	public Integer getRotationid() {
		return rotationid;
	}

	/**
	 * @param rotationid the rotationid to set
	 */
	public void setRotationid(Integer rotationid) {
		this.rotationid = rotationid;
	}

	/**
	 * @return the linetype
	 */
	public String getLinetype() {
		return linetype;
	}

	/**
	 * @param linetype the linetype to set
	 */
	public void setLinetype(String linetype) {
		this.linetype = linetype;
	}

	/**
	 * @return the lineindicator
	 */
	public String getLineindicator() {
		return lineindicator;
	}

	/**
	 * @param lineindicator the lineindicator to set
	 */
	public void setLineindicator(String lineindicator) {
		this.lineindicator = lineindicator;
	}

	/**
	 * @return the sporttype
	 */
	public String getSporttype() {
		return sporttype;
	}

	/**
	 * @param sporttype the sporttype to set
	 */
	public void setSporttype(String sporttype) {
		this.sporttype = sporttype;
	}

	/**
	 * @return the team1
	 */
	public String getTeam1() {
		return team1;
	}

	/**
	 * @param team1 the team1 to set
	 */
	public void setTeam1(String team1) {
		this.team1 = team1;
	}

	/**
	 * @return the team2
	 */
	public String getTeam2() {
		return team2;
	}

	/**
	 * @param team2 the team2 to set
	 */
	public void setTeam2(String team2) {
		this.team2 = team2;
	}

	/**
	 * @return the accountid
	 */
	public Long getAccountid() {
		return accountid;
	}

	/**
	 * @param accountid the accountid to set
	 */
	public void setAccountid(Long accountid) {
		this.accountid = accountid;
	}

	/**
	 * @return the accountname
	 */
	public String getAccountname() {
		return accountname;
	}

	/**
	 * @param accountname the accountname to set
	 */
	public void setAccountname(String accountname) {
		this.accountname = accountname;
	}

	/**
	 * @return the sitetype
	 */
	public String getSitetype() {
		return sitetype;
	}

	/**
	 * @param sitetype the sitetype to set
	 */
	public void setSitetype(String sitetype) {
		this.sitetype = sitetype;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the ismobile
	 */
	public Boolean getIsmobile() {
		return ismobile;
	}

	/**
	 * @param ismobile the ismobile to set
	 */
	public void setIsmobile(Boolean ismobile) {
		this.ismobile = ismobile;
	}

	/**
	 * @return the showrequestresponse
	 */
	public Boolean getShowrequestresponse() {
		return showrequestresponse;
	}

	/**
	 * @param showrequestresponse the showrequestresponse to set
	 */
	public void setShowrequestresponse(Boolean showrequestresponse) {
		this.showrequestresponse = showrequestresponse;
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
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the timezone
	 */
	public String getTimezone() {
		return timezone;
	}

	/**
	 * @param timezone the timezone to set
	 */
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	/**
	 * @return the proxyname
	 */
	public String getProxyname() {
		return proxyname;
	}

	/**
	 * @param proxyname the proxyname to set
	 */
	public void setProxyname(String proxyname) {
		this.proxyname = proxyname;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PreviewInput [rotationid=" + rotationid + ", linetype=" + linetype + ", lineindicator=" + lineindicator
				+ ", sporttype=" + sporttype + ", team1=" + team1 + ", team2=" + team2 + ", accountid=" + accountid
				+ ", accountname=" + accountname + ", sitetype=" + sitetype + ", url=" + url + ", ismobile=" + ismobile
				+ ", showrequestresponse=" + showrequestresponse + ", username=" + username + ", password=" + password
				+ ", timezone=" + timezone + ", proxyname=" + proxyname + "]";
	}
}