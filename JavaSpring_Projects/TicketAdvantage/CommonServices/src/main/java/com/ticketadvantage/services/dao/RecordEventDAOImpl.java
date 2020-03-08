/**
 * 
 */
package com.ticketadvantage.services.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ticketadvantage.services.errorhandling.AppException;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.AccountEvent;
import com.ticketadvantage.services.model.BaseRecordEvent;
import com.ticketadvantage.services.model.MlRecordEvent;
import com.ticketadvantage.services.model.PendingEvent;
import com.ticketadvantage.services.model.RecordEvent;
import com.ticketadvantage.services.model.SpreadRecordEvent;
import com.ticketadvantage.services.model.TotalRecordEvent;

/**
 * @author jmiller
 *
 */
@Repository
public class RecordEventDAOImpl implements RecordEventDAO {
	private static final Logger LOGGER = Logger.getLogger(RecordEventDAOImpl.class);

	@PersistenceContext(name = "entityManager")
	private EntityManager entityManager;

	/**
	 * 
	 */
	public RecordEventDAOImpl() {
		super();
		LOGGER.debug("Entering AccountDAOImpl()");
		LOGGER.debug("Exiting AccountDAOImpl()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.RecordEventDAO#setSpreadEvent(com.ticketadvantage.services.model.SpreadRecordEvent)
	 */
	@Override
	@Transactional(readOnly = false)
	public Long setSpreadEvent(SpreadRecordEvent recordEvent) throws AppException {
		LOGGER.info("Entering setSpreadEvent()");
		LOGGER.info("SpreadRecordEvent: " + recordEvent);
		recordEvent.setId(null);
		entityManager.persist(recordEvent);
		entityManager.flush();
		LOGGER.info("Exiting setSpreadEvent()");
		return recordEvent.getId();
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.RecordEventDAO#setTotalEvent(com.ticketadvantage.services.model.TotalRecordEvent)
	 */
	@Override
	@Transactional(readOnly = false)
	public Long setTotalEvent(TotalRecordEvent recordEvent) throws AppException {
		LOGGER.info("Entering setTotalEvent()");
		LOGGER.info("TotalRecordEvent: " + recordEvent);
		recordEvent.setId(null);
		entityManager.persist(recordEvent);
		entityManager.flush();
		LOGGER.info("Exiting setTotalEvent()");
		return recordEvent.getId();
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.RecordEventDAO#setMlEvent(com.ticketadvantage.services.model.MlRecordEvent)
	 */
	@Override
	@Transactional(readOnly = false)
	public Long setMlEvent(MlRecordEvent recordEvent) throws AppException {
		LOGGER.info("Entering setTotalEvent()");
		LOGGER.info("MLRecordEvent: " + recordEvent);
		recordEvent.setId(null);
		entityManager.persist(recordEvent);
		entityManager.flush();
		LOGGER.info("Exiting setTotalEvent()");
		return recordEvent.getId();
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.RecordEventDAO#getUnProcessedSpreadEvents()
	 */
	@Override
	@Transactional(readOnly = true)
	public List<SpreadRecordEvent> getUnProcessedSpreadEvents() throws AppException {
		LOGGER.info("Entering getUnProcessedSpreadEvents()");

		@SuppressWarnings("unchecked")
		List<SpreadRecordEvent> retValue = entityManager.createQuery("SELECT r FROM SpreadRecordEvent r WHERE iscompleted = false").getResultList();

		LOGGER.info("Exiting getUnProcessedSpreadEvents()");
		return retValue;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.RecordEventDAO#getUnProcessedTotalEvents()
	 */
	@Override
	@Transactional(readOnly = true)
	public List<TotalRecordEvent> getUnProcessedTotalEvents() throws AppException {
		LOGGER.info("Entering getUnProcessedTotalEvents()");

		@SuppressWarnings("unchecked")
		List<TotalRecordEvent> retValue = entityManager.createQuery("SELECT r FROM TotalRecordEvent r WHERE iscompleted = false").getResultList();

		LOGGER.info("Exiting getUnProcessedTotalEvents()");
		return retValue;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.RecordEventDAO#getUnProcessedMlEvents()
	 */
	@Override
	@Transactional(readOnly = true)
	public List<MlRecordEvent> getUnProcessedMlEvents() throws AppException {
		LOGGER.info("Entering getUnProcessedMlEvents()");
		
		@SuppressWarnings("unchecked")
		List<MlRecordEvent> retValue = entityManager.createQuery("SELECT r FROM MlRecordEvent r WHERE iscompleted = false").getResultList();

		LOGGER.info("Exiting getUnProcessedMlEvents()");
		return retValue;		
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.RecordEventDAO#processEvents()
	 */
	@Override
	@Transactional(readOnly = true)
	public void processEvents() throws AppException {
		LOGGER.info("Entering processEvents()");

		@SuppressWarnings("unchecked")
		List<RecordEvent> retValue = entityManager.createQuery("SELECT r FROM RecordEvent r").getResultList();
		for (int x = 0; (retValue != null && x < retValue.size()); x++) {
			RecordEvent event = retValue.get(x);
			LOGGER.debug("RecordEvent: " + event);
		}

		LOGGER.info("Exiting processEvents()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.RecordEventDAO#updateSpreadEvent(com.ticketadvantage.services.model.SpreadRecordEvent)
	 */
	@Override
	@Transactional(readOnly = false)
	public void updateSpreadEvent(SpreadRecordEvent recordEvent) throws BatchException {
		LOGGER.info("Entering updateSpreadEvent()");
		LOGGER.debug("SpreadRecordEvent: " + recordEvent);

		entityManager.merge(recordEvent);

		LOGGER.info("Exiting updateSpreadEvent()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.RecordEventDAO#updateTotalEvent(com.ticketadvantage.services.model.TotalRecordEvent)
	 */
	@Override
	@Transactional(readOnly = false)
	public void updateTotalEvent(TotalRecordEvent recordEvent) throws BatchException {
		LOGGER.info("Entering updateTotalEvent()");
		LOGGER.debug("TotalRecordEvent: " + recordEvent);

		entityManager.merge(recordEvent);

		LOGGER.info("Exiting updateTotalEvent()");		
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.RecordEventDAO#updateMlEvent(com.ticketadvantage.services.model.MlRecordEvent)
	 */
	@Override
	@Transactional(readOnly = false)
	public void updateMlEvent(MlRecordEvent recordEvent) throws BatchException {
		LOGGER.info("Entering updateMlEvent()");
		LOGGER.debug("MlRecordEvent: " + recordEvent);

		entityManager.merge(recordEvent);

		LOGGER.info("Exiting updateMlEvent()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.RecordEventDAO#setupAccountEvent(com.ticketadvantage.services.model.AccountEvent)
	 */
	@Override
	@Transactional(readOnly = false)
	public void setupAccountEvent(AccountEvent accountEvent) throws AppException {
		LOGGER.info("Entering setupAccountEvent()");

		entityManager.persist(accountEvent);

		LOGGER.info("Exiting setupAccountEvent()");		
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.RecordEventDAO#getAccountEvents(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")
	public List<AccountEvent> getAccountEvents(Long eventId) throws BatchException {
		LOGGER.info("Entering getAccountEvents()");
		List<AccountEvent> retValue = null;

		try {
			retValue = entityManager.createQuery("SELECT ae FROM AccountEvent ae WHERE ae.eventid = :eventid ORDER BY ASC ae.name").setParameter("eventid", eventId).getResultList();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		LOGGER.info("Exiting getAccountEvents()");
		return retValue;		
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.RecordEventDAO#getSpreadActiveAccountEvents(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")
	public List<AccountEvent> getSpreadActiveAccountEvents(Long spreadid) throws BatchException {
		LOGGER.info("Entering getSpreadActiveAccountEvents()");
		List<AccountEvent> retValue = null;

		try {
			retValue = entityManager.createQuery("SELECT ae FROM AccountEvent ae WHERE ae.spreadid = :spreadid AND iscompleted = false").setParameter("spreadid", spreadid).getResultList();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		LOGGER.info("Exiting getSpreadActiveAccountEvents()");
		return retValue;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.RecordEventDAO#getTotalActiveAccountEvents(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")
	public List<AccountEvent> getTotalActiveAccountEvents(Long totalid) throws BatchException {
		LOGGER.info("Entering getTotalActiveAccountEvents()");
		List<AccountEvent> retValue = null;

		try {
			retValue = entityManager.createQuery("SELECT ae FROM AccountEvent ae WHERE ae.totalid = :totalid AND iscompleted = false").setParameter("totalid", totalid).getResultList();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		LOGGER.info("Exiting getTotalActiveAccountEvents()");
		return retValue;		
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.RecordEventDAO#getMlActiveAccountEvents(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")
	public List<AccountEvent> getMlActiveAccountEvents(Long mlid) throws BatchException {
		LOGGER.info("Entering getMlActiveAccountEvents()");
		List<AccountEvent> retValue = null;

		try {
			retValue = entityManager.createQuery("SELECT ae FROM AccountEvent ae WHERE ae.mlid = :mlid AND iscompleted = false").setParameter("mlid", mlid).getResultList();
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}

		LOGGER.info("Exiting getMlActiveAccountEvents()");
		return retValue;		
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.RecordEventDAO#updateAccountEvent(com.ticketadvantage.services.model.AccountEvent)
	 */
	@Override
	@Transactional(readOnly = false)
	public void updateAccountEvent(AccountEvent accountEvent) throws BatchException {
		LOGGER.info("Entering updateAccountEvent()");
		LOGGER.debug("AccountEvent: " + accountEvent);

		entityManager.merge(accountEvent);

		LOGGER.info("Exiting updateAccountEvent()");
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.RecordEventDAO#getEvent(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = true)
	synchronized public BaseRecordEvent getEvent(Long id) throws AppException {
		LOGGER.info("Entering getEvent()");
		LOGGER.debug("ID: " + id);

		final SpreadRecordEvent spreadRecordEvent = entityManager.find(SpreadRecordEvent.class, id);
		if (spreadRecordEvent != null) {
			LOGGER.debug("spreadRecordEvent: " + spreadRecordEvent);
			return spreadRecordEvent;
		}
		final TotalRecordEvent totalRecordEvent = entityManager.find(TotalRecordEvent.class, id);
		if (totalRecordEvent != null) {
			LOGGER.debug("totalRecordEvent: " + totalRecordEvent);
			return totalRecordEvent;
		}
		final MlRecordEvent mlRecordEvent = entityManager.find(MlRecordEvent.class, id);
		if (mlRecordEvent != null) {
			LOGGER.debug("mlRecordEvent: " + mlRecordEvent);
			return mlRecordEvent;
		}

		LOGGER.info("Exiting getEvent()");
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.RecordEventDAO#getSpreadEvent(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = true)
	public BaseRecordEvent getSpreadEvent(Long id) throws AppException {
		LOGGER.info("Entering getSpreadEvent()");
		LOGGER.debug("ID: " + id);
		
		final SpreadRecordEvent spreadRecordEvent = entityManager.find(SpreadRecordEvent.class, id);
			
		LOGGER.debug("SpreadRecordEvent: " + spreadRecordEvent);
		LOGGER.info("Exiting getSpreadEvent()");
		return spreadRecordEvent;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.RecordEventDAO#getTotalEvent(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = true)
	public BaseRecordEvent getTotalEvent(Long id) throws AppException {
		LOGGER.info("Entering getTotalEvent()");
		LOGGER.debug("ID: " + id);
		
		final TotalRecordEvent totalRecordEvent = entityManager.find(TotalRecordEvent.class, id);
			
		LOGGER.debug("TotalRecordEvent: " + totalRecordEvent);
		LOGGER.info("Exiting getTotalEvent()");
		return totalRecordEvent;		
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.RecordEventDAO#getMlEvent(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = true)
	public BaseRecordEvent getMlEvent(Long id) throws AppException {
		LOGGER.info("Entering getMlEvent()");
		LOGGER.debug("ID: " + id);
		
		final MlRecordEvent mlRecordEvent = entityManager.find(MlRecordEvent.class, id);
			
		LOGGER.debug("MlRecordEvent: " + mlRecordEvent);
		LOGGER.info("Exiting getMlEvent()");
		return mlRecordEvent;		
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.RecordEventDAO#setEntityManager(javax.persistence.EntityManager)
	 */
	@Override
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.AccountDAO#getEntityManager()
	 */
	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.RecordEventDAO#checkDuplicateSpreadEvent(com.ticketadvantage.services.model.SpreadRecordEvent)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public boolean checkDuplicateSpreadEvent(SpreadRecordEvent recordEvent) throws AppException {
		final List<PendingEvent> listPendingEvent = (List<PendingEvent>) entityManager
				.createQuery("SELECT sre FROM SpreadRecordEvent sre "
						+ "WHERE sre.accountid = :accountid "
						+ "AND sre.eventdatetime = :eventdatetime "
						+ "AND sre.eventid = :eventid "
						+ "AND sre.eventname = :eventname "
						+ "AND sre.eventtype = :eventtype "
						+ "AND sre.groupid = :groupid "
						+ "AND sre.sport = :sport "
						+ "AND sre.userid = :userid ")
				.setParameter("accountid", recordEvent.getAccountid())
				.setParameter("eventdatetime", recordEvent.getEventdatetime())
				.setParameter("eventid", recordEvent.getEventid())
				.setParameter("eventname", recordEvent.getEventname())
				.setParameter("eventtype", recordEvent.getEventtype())
				.setParameter("groupid", recordEvent.getGroupid())
				.setParameter("sport", recordEvent.getSport())
				.setParameter("userid", recordEvent.getUserid())
				.getResultList();

		if (listPendingEvent != null && !listPendingEvent.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.RecordEventDAO#checkDuplicateTotalEvent(com.ticketadvantage.services.model.TotalRecordEvent)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public boolean checkDuplicateTotalEvent(TotalRecordEvent recordEvent) throws AppException {
		final List<PendingEvent> listPendingEvent = (List<PendingEvent>) entityManager
				.createQuery("SELECT sre FROM TotalRecordEvent sre "
						+ "WHERE sre.accountid = :accountid "
						+ "AND sre.eventdatetime = :eventdatetime "
						+ "AND sre.eventid = :eventid "
						+ "AND sre.eventname = :eventname "
						+ "AND sre.eventtype = :eventtype "
						+ "AND sre.groupid = :groupid "
						+ "AND sre.sport = :sport "
						+ "AND sre.userid = :userid ")
				.setParameter("accountid", recordEvent.getAccountid())
				.setParameter("eventdatetime", recordEvent.getEventdatetime())
				.setParameter("eventid", recordEvent.getEventid())
				.setParameter("eventname", recordEvent.getEventname())
				.setParameter("eventtype", recordEvent.getEventtype())
				.setParameter("groupid", recordEvent.getGroupid())
				.setParameter("sport", recordEvent.getSport())
				.setParameter("userid", recordEvent.getUserid())
				.getResultList();

		if (listPendingEvent != null && !listPendingEvent.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.RecordEventDAO#checkDuplicateMlEvent(com.ticketadvantage.services.model.MlRecordEvent)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public boolean checkDuplicateMlEvent(MlRecordEvent recordEvent) throws AppException {
		final List<PendingEvent> listPendingEvent = (List<PendingEvent>) entityManager
				.createQuery("SELECT sre FROM MlRecordEvent sre "
						+ "WHERE sre.accountid = :accountid "
						+ "AND sre.eventdatetime = :eventdatetime "
						+ "AND sre.eventid = :eventid "
						+ "AND sre.eventname = :eventname "
						+ "AND sre.eventtype = :eventtype "
						+ "AND sre.groupid = :groupid "
						+ "AND sre.sport = :sport "
						+ "AND sre.userid = :userid ")
				.setParameter("accountid", recordEvent.getAccountid())
				.setParameter("eventdatetime", recordEvent.getEventdatetime())
				.setParameter("eventid", recordEvent.getEventid())
				.setParameter("eventname", recordEvent.getEventname())
				.setParameter("eventtype", recordEvent.getEventtype())
				.setParameter("groupid", recordEvent.getGroupid())
				.setParameter("sport", recordEvent.getSport())
				.setParameter("userid", recordEvent.getUserid())
				.getResultList();

		if (listPendingEvent != null && !listPendingEvent.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.RecordEventDAO#getActiveAccountEvents(java.util.Date, java.util.Date)
	 */
	@Override
	public Integer getActiveAccountEvents(Date startDate, Date endDate) throws AppException {
		return 4;
	}

	@Override
	public AccountEvent getAccountEvent(Long accountEventId) throws BatchException {
		AccountEvent accountEvent = null;

		try {
			List<AccountEvent> accountEvents = entityManager.createQuery("SELECT ae FROM AccountEvent ae WHERE ae.id = :id").setParameter("id", accountEventId).getResultList();
			LOGGER.debug("AccountEvents: " + accountEvents);
			
			if (accountEvents != null && accountEvents.size() > 0) {
				accountEvent = accountEvents.get(0);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		return accountEvent;
	}
}