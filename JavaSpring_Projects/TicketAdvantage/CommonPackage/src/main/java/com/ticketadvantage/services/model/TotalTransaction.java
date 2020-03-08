/**
 * 
 */
package com.ticketadvantage.services.model;

import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author jmiller
 *
 */
@XmlRootElement(name = "totaltransaction")
@XmlAccessorType(XmlAccessType.NONE)
public class TotalTransaction {
	@XmlElement
	private TotalRecordEvent totalRecordEvent;
	
	@XmlElement
	private Set<Accounts> accounts;
	
	@XmlElement
	private Set<Groups> groups;

	/**
	 * 
	 */
	public TotalTransaction() {
		super();
	}

	/**
	 * @return the totalRecordEvent
	 */
	public TotalRecordEvent getTotalRecordEvent() {
		return totalRecordEvent;
	}

	/**
	 * @param totalRecordEvent the totalRecordEvent to set
	 */
	public void setTotalRecordEvent(TotalRecordEvent totalRecordEvent) {
		this.totalRecordEvent = totalRecordEvent;
	}

	/**
	 * @return the accounts
	 */
	public Set<Accounts> getAccounts() {
		return accounts;
	}

	/**
	 * @param accounts the accounts to set
	 */
	public void setAccounts(Set<Accounts> accounts) {
		this.accounts = accounts;
	}

	/**
	 * @return the groups
	 */
	public Set<Groups> getGroups() {
		return groups;
	}

	/**
	 * @param groups the groups to set
	 */
	public void setGroups(Set<Groups> groups) {
		this.groups = groups;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TotalTransaction [totalRecordEvent=" + totalRecordEvent + ", accounts=" + accounts + ", groups="
				+ groups + "]";
	}
}
