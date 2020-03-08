/**
 * 
 */
package com.ticketadvantage.services.dao;

import java.util.Set;

import javax.persistence.EntityManager;

import com.ticketadvantage.services.errorhandling.AppException;
import com.ticketadvantage.services.model.Groups;

/**
 * @author jmiller
 *
 */
public interface GroupDAO {

	/**
	 * 
	 * @param group
	 * @return
	 * @throws AppException
	 */
	public Groups persist(Groups group) throws AppException;

	/**
	 * 
	 * @param group
	 * @return
	 * @throws AppException
	 */
	public Groups update(Groups group) throws AppException;

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
	public Groups getGroup(Long id) throws AppException;

	/**
	 * 
	 * @return
	 * @throws AppException
	 */
	public Set<Groups> findAll() throws AppException;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws AppException
	 */
	public Set<Groups> getGroupsForUser(Long id) throws AppException;

	/**
	 * 
	 * @param id
	 * @param group
	 * @return
	 * @throws AppException
	 */
	public Groups createGroupForUser(Long id, Groups group) throws AppException;
	
	/**
	 * 
	 * @param group_id
	 * @param account_id
	 * @throws AppException
	 */
	public void addAccountForGroup(Long group_id, Long account_id) throws AppException;
	
	/**
	 * 
	 * @param group_id
	 * @param account_id
	 * @throws AppException
	 */
	public void deleteAccountForGroup(Long group_id, Long account_id) throws AppException;

	/**
	 * 
	 * @param entityManager
	 */
	public void setEntityManager(EntityManager entityManager);
}