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
@XmlRootElement(name = "mltransaction")
@XmlAccessorType(XmlAccessType.NONE)
public class MlTransaction {
	@XmlElement
	private MlRecordEvent mlRecordEvent;
	
	@XmlElement
	private Set<Accounts> accounts;
	
	@XmlElement
	private Set<Groups> groups;

	/**
	 * 
	 */
	public MlTransaction() {
		super();
	}

	/**
	 * @return the mlRecordEvent
	 */
	public MlRecordEvent getMlRecordEvent() {
		return mlRecordEvent;
	}

	/**
	 * @param mlRecordEvent the mlRecordEvent to set
	 */
	public void setMlRecordEvent(MlRecordEvent mlRecordEvent) {
		this.mlRecordEvent = mlRecordEvent;
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
		return "MlTransaction [mlRecordEvent=" + mlRecordEvent + ", accounts=" + accounts + ", groups=" + groups + "]";
	}
}
