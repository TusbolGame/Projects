/**
 * 
 */
package com.ticketadvantage.services.dao;

import java.util.Set;

import javax.persistence.EntityManager;

import com.ticketadvantage.services.errorhandling.AppException;
import com.ticketadvantage.services.model.EmailAccounts;

/**
 * @author jmiller
 *
 */
public interface EmailAccountDAO {

	/**
	 * 
	 * @param account
	 * @return
	 * @throws AppException
	 */
	public EmailAccounts persist(EmailAccounts account) throws AppException;

	/**
	 * 
	 * @param account
	 * @return
	 * @throws AppException
	 */
	public EmailAccounts update(EmailAccounts account) throws AppException;

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
	public EmailAccounts getEmailAccount(Long id) throws AppException;

	/**
	 * 
	 * @return
	 * @throws AppException
	 */
	public Set<EmailAccounts> findAll() throws AppException;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws AppException
	 */
	public Set<EmailAccounts> getAccountsForUser(Long id) throws AppException;

	/**
	 * 
	 * @param id
	 * @param account
	 * @return
	 * @throws AppException
	 */
	public EmailAccounts createEmailAccountForUser(Long id, EmailAccounts account) throws AppException;
	
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