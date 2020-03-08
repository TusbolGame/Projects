/**
 * 
 */
package com.ticketadvantage.services.dao;

import java.util.List;

import javax.persistence.EntityManager;

import com.ticketadvantage.services.errorhandling.AppException;
import com.ticketadvantage.services.model.PendingEvent;

/**
 * @author jmiller
 *
 */
public interface PendingEventsDAO {

	/**
	 * 
	 * @param id
	 * @return
	 * @throws AppException
	 */
	public PendingEvent find(Long id) throws AppException;

	/**
	 * 
	 * @param pendingEvent
	 * @return
	 * @throws AppException
	 */
	public PendingEvent persist(PendingEvent pendingEvent) throws AppException;

	/**
	 * 
	 * @param id
	 * @param pendingEvent
	 * @throws AppException
	 */
	public void update(Long id, PendingEvent pendingEvent) throws AppException;

	/**
	 * 
	 * @param id
	 * @throws AppException
	 */
	public void delete(Long id) throws AppException;

	/**
	 * 
	 * @return
	 * @throws AppException
	 */
	public List<PendingEvent> findAll() throws AppException;
	
	/**
	 * 
	 * @param userid
	 * @return
	 * @throws AppException
	 */
	public List<PendingEvent> findPendingEventsByUserId(Long userid) throws AppException;

	/**
	 * 
	 * @param userid
	 * @param pendingType
	 * @return
	 * @throws AppException
	 */
	public List<PendingEvent> findPendingEventsByUserIdByType(Long userid, String pendingType) throws AppException;

	/**
	 * 
	 * @param entityManager
	 */
	public void setEntityManager(EntityManager entityManager);
}