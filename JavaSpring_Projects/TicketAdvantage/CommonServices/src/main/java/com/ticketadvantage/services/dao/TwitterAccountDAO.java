/**
 * 
 */
package com.ticketadvantage.services.dao;

import java.util.Set;

import javax.persistence.EntityManager;

import com.ticketadvantage.services.errorhandling.AppException;
import com.ticketadvantage.services.model.TwitterAccounts;

/**
 * @author jmiller
 *
 */
public interface TwitterAccountDAO {

	/**
	 * 
	 * @param account
	 * @return
	 * @throws AppException
	 */
	public TwitterAccounts persist(TwitterAccounts account) throws AppException;

	/**
	 * 
	 * @param account
	 * @return
	 * @throws AppException
	 */
	public TwitterAccounts update(TwitterAccounts account) throws AppException;

	/**
	 * 
	 * @param userid
	 * @param id
	 * @throws AppException
	 */
	public void delete(Long userid, Long id) throws AppException;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws AppException
	 */
	public TwitterAccounts getTwitterAccount(Long id) throws AppException;

	/**
	 * 
	 * @return
	 * @throws AppException
	 */
	public Set<TwitterAccounts> findAll() throws AppException;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws AppException
	 */
	public Set<TwitterAccounts> getAccountsForUser(Long id) throws AppException;

	/**
	 * 
	 * @param id
	 * @param account
	 * @return
	 * @throws AppException
	 */
	public TwitterAccounts createTwitterAccountForUser(Long id, TwitterAccounts account) throws AppException;
	
	/**
	 * 
	 * @param entityManager
	 */
	public void setEntityManager(EntityManager entityManager);

	/**
	 * 
	 * @return
	 */
	public EntityManager getEntityManager();
}