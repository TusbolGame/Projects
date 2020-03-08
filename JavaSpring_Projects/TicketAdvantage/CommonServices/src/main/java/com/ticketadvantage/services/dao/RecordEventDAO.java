/**
 * 
 */
package com.ticketadvantage.services.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import com.ticketadvantage.services.errorhandling.AppException;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.AccountEvent;
import com.ticketadvantage.services.model.BaseRecordEvent;
import com.ticketadvantage.services.model.MlRecordEvent;
import com.ticketadvantage.services.model.SpreadRecordEvent;
import com.ticketadvantage.services.model.TotalRecordEvent;

/**
 * @author jmiller
 *
 */
public interface RecordEventDAO {

	/**
	 * 
	 * @param recordEvent
	 * @return
	 * @throws AppException
	 */
	public Long setSpreadEvent(SpreadRecordEvent recordEvent) throws AppException;

	/**
	 * 
	 * @param recordEvent
	 * @return
	 * @throws AppException
	 */
	public Long setTotalEvent(TotalRecordEvent recordEvent) throws AppException;

	/**
	 * 
	 * @param recordEvent
	 * @return
	 * @throws AppException
	 */
	public Long setMlEvent(MlRecordEvent recordEvent) throws AppException;

	/**
	 * 
	 * @return
	 * @throws BatchException
	 */
	public List<SpreadRecordEvent> getUnProcessedSpreadEvents() throws BatchException;

	/**
	 * 
	 * @return
	 * @throws BatchException
	 */
	public List<TotalRecordEvent> getUnProcessedTotalEvents() throws BatchException;

	/**
	 * 
	 * @return
	 * @throws BatchException
	 */
	public List<MlRecordEvent> getUnProcessedMlEvents() throws BatchException;

	/**
	 * 
	 * @throws AppException
	 */
	public void processEvents() throws AppException;

	/**
	 * 
	 * @param recordEvent
	 * @throws BatchException
	 */
	public void updateSpreadEvent(SpreadRecordEvent recordEvent) throws BatchException;

	/**
	 * 
	 * @param recordEvent
	 * @throws BatchException
	 */
	public void updateTotalEvent(TotalRecordEvent recordEvent) throws BatchException;
	
	/**
	 * 
	 * @param recordEvent
	 * @throws BatchException
	 */
	public void updateMlEvent(MlRecordEvent recordEvent) throws BatchException;

	/**
	 * 
	 * @param accountEvent
	 * @throws AppException
	 */
	public void setupAccountEvent(AccountEvent accountEvent) throws AppException;

	/**
	 * 
	 * @param eventId
	 * @return
	 * @throws BatchException
	 */
	public List<AccountEvent> getAccountEvents(Long eventId) throws BatchException;

	/**
	 * 
	 * @param accountEventId
	 * @return
	 * @throws BatchException
	 */
	public AccountEvent getAccountEvent(Long accountEventId) throws BatchException;

	/**
	 * 
	 * @param spreadid
	 * @return
	 * @throws BatchException
	 */
	public List<AccountEvent> getSpreadActiveAccountEvents(Long spreadid) throws BatchException;
	
	/**
	 * 
	 * @param totalid
	 * @return
	 * @throws BatchException
	 */
	public List<AccountEvent> getTotalActiveAccountEvents(Long totalid) throws BatchException;
	
	/**
	 * 
	 * @param mlid
	 * @return
	 * @throws BatchException
	 */
	public List<AccountEvent> getMlActiveAccountEvents(Long mlid) throws BatchException;
	
	/**
	 * 
	 * @param startDate
	 * @param endDate
	 * @return 
	 * @throws AppException
	 */
	public Integer getActiveAccountEvents(Date startDate, Date endDate) throws AppException;
	/**
	 * 
	 * @param accountEvent
	 * @throws BatchException
	 */
	public void updateAccountEvent(AccountEvent accountEvent) throws BatchException;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws AppException
	 */
	public BaseRecordEvent getEvent(Long id) throws AppException;	

	/**
	 * 
	 * @param id
	 * @return
	 * @throws AppException
	 */
	public BaseRecordEvent getSpreadEvent(Long id) throws AppException;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws AppException
	 */
	public BaseRecordEvent getTotalEvent(Long id) throws AppException;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws AppException
	 */
	public BaseRecordEvent getMlEvent(Long id) throws AppException;

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

	/**
	 * 
	 * @param recordEvent
	 * @return
	 * @throws AppException
	 */
	public boolean checkDuplicateSpreadEvent(SpreadRecordEvent recordEvent) throws AppException;

	/**
	 * 
	 * @param recordEvent
	 * @return
	 * @throws AppException
	 */
	public boolean checkDuplicateTotalEvent(TotalRecordEvent recordEvent) throws AppException;

	/**
	 * 
	 * @param recordEvent
	 * @return
	 * @throws AppException
	 */
	public boolean checkDuplicateMlEvent(MlRecordEvent recordEvent) throws AppException;
}