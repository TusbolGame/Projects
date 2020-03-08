/**
 * 
 */
package com.ticketadvantage.services.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import com.ticketadvantage.services.errorhandling.AppException;
import com.ticketadvantage.services.model.AccountEvent;

/**
 * @author calderson
 *
 */
public interface AccountEventDAO {
		
	/**
	 * 
	 * @param startDate
	 * @param endDate
	 * @param countOnly
	 * @return 
	 * @throws AppException
	 */
	public List<AccountEvent> getAccountEvents(Long userid, Date startDate, Date endDate) throws AppException;
	
	/**
	 * 
	 * @param startDate
	 * @param endDate
	 * @param countOnly
	 * @return 
	 * @throws AppException
	 */
	public Long getAccountEventsCount(Long userid, Date startDate, Date endDate) throws AppException;
	
	/**
	 * 
	 * @param startDate
	 * @param endDate
	 * @param countOnly
	 * @return 
	 * @throws AppException
	 */
	public BigDecimal getAccountEventsAmount(Long userid, Date startDate, Date endDate) throws AppException;
	
	/**
	 * 
	 * @param entityManager
	 */
	public void setEntityManager(EntityManager entityManager);
	
	/**
	 * 
	 * @return
	 * @throws AppException
	 */
	public List<AccountEvent> getOpenAccountEventsForWeek() throws AppException;

	/**
	 * 
	 * @return
	 * @throws AppException
	 */
	public List<AccountEvent> getErrorAccountEventsForWeek() throws AppException;
}