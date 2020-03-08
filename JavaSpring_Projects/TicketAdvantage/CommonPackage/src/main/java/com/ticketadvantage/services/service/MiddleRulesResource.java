/**
 * 
 */
package com.ticketadvantage.services.service;

import java.util.Set;

import com.ticketadvantage.services.db.RecordEventDB;
import com.ticketadvantage.services.model.Accounts;

/**
 * @author jmiller
 *
 */
public interface MiddleRulesResource {

	/**
	 * 
	 * @param id
	 */
	public void setEventId(Long id);

	/**
	 * 
	 * @param type
	 */
	public void setEventType(String type);

	/**
	 * 
	 * @param accounts
	 */
	public void setMiddleAccounts(Set<Accounts> accounts);

	/**
	 * 
	 * @param RECORDEVENTDB
	 */
	public void setRecordEventDB(RecordEventDB RECORDEVENTDB);
	
	/**
	 * 
	 */
	public void startMiddle();
}