/**
 * 
 */
package com.ticketadvantage.services.service;

import java.util.List;
import java.util.Set;

import com.ticketadvantage.services.db.RecordEventDB;
import com.ticketadvantage.services.model.Accounts;
import com.ticketadvantage.services.model.BaseRecordEvent;

/**
 * @author jmiller
 *
 */
public interface BaseBestPriceBuyOrderResource {

	/**
	 * @return the event
	 */
	public BaseRecordEvent getEvent();

	/**
	 * @param event
	 *            the event to set
	 */
	public void setEvent(BaseRecordEvent event);

	/**
	 * @return the accounts
	 */
	public List<Accounts> getAccounts();

	/**
	 * @param accounts
	 *            the accounts to set
	 */
	public void setAccounts(Set<Accounts> setAccounts);

	/**
	 * @return the maxAmount
	 */
	public String getMaxAmount();

	/**
	 * @param maxAmount the maxAmount to set
	 */
	public void setMaxAmount(String maxAmount);

	/**
	 * @return the enableretry
	 */
	public Boolean getEnableretry();

	/**
	 * @param enableretry the enableretry to set
	 */
	public void setEnableretry(Boolean enableretry);

	/**
	 * @return the mobileTextNumber
	 */
	public String getMobileTextNumber();

	/**
	 * @param mobileTextNumber the mobileTextNumber to set
	 */
	public void setMobileTextNumber(String mobileTextNumber);

	/**
	 * @return the orderAmount
	 */
	public Integer getOrderAmount();

	/**
	 * @param orderAmount the orderAmount to set
	 */
	public void setOrderAmount(Integer orderAmount);

	/**
	 * @return the rECORDEVENTDB
	 */
	public RecordEventDB getRECORDEVENTDB();

	/**
	 * @param rECORDEVENTDB
	 *            the rECORDEVENTDB to set
	 */
	public void setRECORDEVENTDB(RecordEventDB rECORDEVENTDB);

	/**
	 * @return the sendtextforaccount
	 */
	public Boolean getSendtextforaccount();

	/**
	 * @param sendtextforaccount the sendtextforaccount to set
	 */
	public void setSendtextforaccount(Boolean sendtextforaccount);

	/**
	 * @return the wasSuccessful
	 */
	public Boolean getWasSuccessful();

	/**
	 * @param wasSuccessful the wasSuccessful to set
	 */
	public void setWasSuccessful(Boolean wasSuccessful);

	/**
	 * 
	 * @param wasSuccessful
	 * @param accountId
	 */
	public void bestPriceResourceDone(Boolean wasSuccessful, Long accountId);
}