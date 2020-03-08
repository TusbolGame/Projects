/**
 * 
 */
package com.wootechnologies.model;

import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author jmiller
 *
 */
@XmlRootElement(name = "spreadtransaction")
@XmlAccessorType(XmlAccessType.NONE)
public class SpreadTransaction {
	@XmlElement
	private SpreadRecordEvent spreadRecordEvent;
	
	@XmlElement
	private Set<Accounts> accounts;
	
	@XmlElement
	private Set<Groups> groups;

	/**
	 * 
	 */
	public SpreadTransaction() {
	}

	/**
	 * @return the spreadRecordEvent
	 */
	public SpreadRecordEvent getSpreadRecordEvent() {
		return spreadRecordEvent;
	}

	/**
	 * @param spreadRecordEvent the spreadRecordEvent to set
	 */
	public void setSpreadRecordEvent(SpreadRecordEvent spreadRecordEvent) {
		this.spreadRecordEvent = spreadRecordEvent;
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
		return "SpreadTransaction [spreadRecordEvent=" + spreadRecordEvent + ", accounts=" + accounts + ", groups="
				+ groups + "]";
	}
}