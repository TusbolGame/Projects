/**
 * 
 */
package com.ticketadvantage.services.dao;

import java.util.Date;
import java.util.Set;

import javax.persistence.EntityManager;

import com.ticketadvantage.services.errorhandling.AppException;
import com.ticketadvantage.services.model.Accounts;
import com.ticketadvantage.services.model.ProxyLocation;
import com.ticketadvantage.services.model.SiteName;
import com.ticketadvantage.services.model.WeeklyFigures;

/**
 * @author jmiller
 *
 */
public interface AccountDAO {

	/**
	 * 
	 * @param account
	 * @return
	 * @throws AppException
	 */
	public Accounts persist(Accounts account) throws AppException;

	/**
	 * 
	 * @param account
	 * @return
	 * @throws AppException
	 */
	public Accounts update(Accounts account) throws AppException;

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
	public Accounts getAccount(Long id) throws AppException;

	/**
	 * 
	 * @return
	 * @throws AppException
	 */
	public Set<Accounts> findAll() throws AppException;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws AppException
	 */
	public Set<Accounts> getAccountsForUser(Long id) throws AppException;

	/**
	 * 
	 * @param id
	 * @param account
	 * @return
	 * @throws AppException
	 */
	public Accounts createAccountForUser(Long id, Accounts account) throws AppException;

	/**
	 * 
	 * @param url
	 * @param username
	 * @param password
	 * @return
	 * @throws AppException
	 */
	public String checkSite(String url, String username, String password) throws AppException;

	/**
	 * 
	 * @param userId
	 * @param weekStartDate
	 * @param timezone
	 * @return
	 * @throws AppException
	 */
	public Set<WeeklyFigures> getWeeklyAccountFigures(Long userId, Date weekStartDate, String timezone) throws AppException;

	/**
	 * 
	 * @return
	 */
	public Set<ProxyLocation> getProxyNames();

	/**
	 * 
	 * @return
	 */
	public Set<SiteName> getSiteNames();

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