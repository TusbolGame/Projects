/**
 * 
 */
package com.ticketadvantage.services.dao;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import com.ticketadvantage.services.errorhandling.AppException;
import com.ticketadvantage.services.model.AccountEventFinal;

/**
 * @author calderson
 *
 */
public interface AccountEventFinalDAO {

	/**
	 * 
	 * @param accountEventFinal
	 * @return
	 * @throws AppException
	 */
	public AccountEventFinal persist(AccountEventFinal accountEventFinal) throws AppException;

	/**
	 * 
	 * @param accountEventFinal
	 * @return
	 * @throws AppException
	 */
	public AccountEventFinal update(AccountEventFinal accountEventFinal) throws AppException;

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
	public AccountEventFinal getAccountEventFinal(Long id) throws AppException;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws AppException
	 */
	public AccountEventFinal getAccountEventFinalByAccountEventId(Long id) throws AppException;

	/**
	 * 
	 * @return
	 * @throws AppException
	 */
	public Set<AccountEventFinal> findAll() throws AppException;

	/**
	 * 
	 * @param userid
	 * @param weekStartDate
	 * @return
	 * @throws AppException
	 */
	public List<AccountEventFinal> getUserAccountEventFinals(Long userid, Date weekStartDate) throws AppException;

	/**
	 * 
	 * @param entityManager
	 */
	public void setEntityManager(EntityManager entityManager);

}