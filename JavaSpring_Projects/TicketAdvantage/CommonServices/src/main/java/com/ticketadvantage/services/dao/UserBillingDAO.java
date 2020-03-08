/**
 * 
 */
package com.ticketadvantage.services.dao;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import com.ticketadvantage.services.errorhandling.AppException;
import com.ticketadvantage.services.model.UserBilling;

/**
 * @author calderson
 *
 */
public interface UserBillingDAO {

	/**
	 * 
	 * @param userBilling
	 * @return
	 * @throws AppException
	 */
	public UserBilling persist(UserBilling userBilling) throws AppException;

	/**
	 * 
	 * @param userBilling
	 * @return
	 * @throws AppException
	 */
	public UserBilling update(UserBilling userBilling) throws AppException;

	/**
	 * 
	 * @param id
	 * @throws AppException
	 */
	public void delete(Long id) throws AppException;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws AppException
	 */
	public UserBilling getUserBilling(Long id) throws AppException;

	/**
	 * 
	 * @return
	 * @throws AppException
	 */
	public Set<UserBilling> findAll() throws AppException;

	/**
	 * 
	 * @param userid
	 * @param weekStartDate
	 * @return
	 * @throws AppException
	 */
	public List<UserBilling> getUserBillingsForUser(Long userid, Date weekStartDate) throws AppException;

	/**
	 * 
	 * @param entityManager
	 */
	public void setEntityManager(EntityManager entityManager);
}