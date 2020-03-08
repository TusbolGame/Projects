/**
 * 
 */
package com.wootechnologies.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * @author jmiller
 *
 */
@Entity
@Table (name="groupaccounts")
@XmlRootElement()
@XmlAccessorType(XmlAccessType.NONE)
public class GroupAccount implements Serializable {
	private static final long serialVersionUID = 1L;

    @Id
	@Column(name = "groupid", unique = false, nullable = false)
	@XmlElement
	private Long groupid;

	@Column(name = "accountid", unique = false, nullable = false)
	@XmlElement
	private Long accountid;

	/**
	 * 
	 */
	public GroupAccount() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * @return the groupid
	 */
	public Long getGroupid() {
		return groupid;
	}

	/**
	 * @param groupid the groupid to set
	 */
	public void setGroupid(Long groupid) {
		this.groupid = groupid;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GroupAccount [groupid=" + groupid + ", accountid=" + accountid + "]";
	}
}