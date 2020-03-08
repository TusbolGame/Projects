/**
 * 
 */
package com.ticketadvantage.services.dao;

import java.util.List;

import javax.persistence.EntityManager;

import com.ticketadvantage.services.errorhandling.AppException;
import com.ticketadvantage.services.model.User;

/**
 * @author jmiller
 *
 */
public interface UserDAO {

	/**
	 * 
	 * @param id
	 * @return
	 * @throws AppException
	 */
	public User find(Long id) throws AppException;

	/**
	 * 
	 * @param username
	 * @return
	 * @throws AppException
	 */
	public User findByUsername(String username) throws AppException;

	/**
	 * 
	 * @param user
	 * @return
	 * @throws AppException
	 */
	public User persist(User user) throws AppException;

	/**
	 * 
	 * @param id
	 * @param user
	 * @throws AppException
	 */
	public void update(Long id, User user) throws AppException;

	/**
	 * 
	 * @param username
	 * @param password
	 * @return
	 * @throws AppException
	 */
	public User login(String username, String password) throws AppException;

	/**
	 * 
	 * @return
	 * @throws AppException
	 */
	public List<User> findAll() throws AppException;
	
	/**
	 * 
	 * @param entityManager
	 */
	public void setEntityManager(EntityManager entityManager);
}