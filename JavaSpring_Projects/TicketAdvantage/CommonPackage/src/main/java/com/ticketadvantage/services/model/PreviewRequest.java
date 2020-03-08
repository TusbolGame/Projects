/**
 * 
 */
package com.ticketadvantage.services.model;

import java.io.Serializable;
import java.util.Set;

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
public class PreviewRequest implements Serializable {
	private static final long serialVersionUID = 3156058159245563904L;

	@XmlElement
	private Long userid;

	@XmlElement
	private Integer rotationid;

	@XmlElement
	private String linetype;

	@XmlElement
	private String lineindicator;

	@XmlElement
	private String sporttype;

	@XmlElement
	private String amount;

	@XmlElement
	private Set<Long> accountids;

	@XmlElement
	private Set<Long> groupids;

	/**
	 * 
	 */
	public PreviewRequest() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
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
	 * @return the amount
	 */
	public String getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(String amount) {
		this.amount = amount;
	}

	/**
	 * @return the accountids
	 */
	public Set<Long> getAccountids() {
		return accountids;
	}

	/**
	 * @param accountids the accountids to set
	 */
	public void setAccountids(Set<Long> accountids) {
		this.accountids = accountids;
	}

	/**
	 * @return the groupids
	 */
	public Set<Long> getGroupids() {
		return groupids;
	}

	/**
	 * @param groupids the groupids to set
	 */
	public void setGroupids(Set<Long> groupids) {
		this.groupids = groupids;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PreviewRequest [userid=" + userid + ", rotationid=" + rotationid + ", linetype=" + linetype
				+ ", lineindicator=" + lineindicator + ", sporttype=" + sporttype + ", amount=" + amount
				+ ", accountids=" + accountids + ", groupids=" + groupids + "]";
	}
}