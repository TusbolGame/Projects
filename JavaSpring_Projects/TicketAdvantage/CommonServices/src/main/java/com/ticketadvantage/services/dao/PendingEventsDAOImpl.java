/**
 * 
 */
package com.ticketadvantage.services.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ticketadvantage.services.errorhandling.AppErrorCodes;
import com.ticketadvantage.services.errorhandling.AppErrorMessage;
import com.ticketadvantage.services.errorhandling.AppException;
import com.ticketadvantage.services.model.PendingEvent;

/**
 * @author jmiller
 *
 */
@Repository
public class PendingEventsDAOImpl implements PendingEventsDAO {
	private static final Logger LOGGER = Logger.getLogger(PendingEventsDAOImpl.class);

	@PersistenceContext(name = "entityManager")
	private EntityManager entityManager;

	/**
	 * 
	 */
	public PendingEventsDAOImpl() {
		super();
		LOGGER.debug("Entering PendingEventsDAOImpl()");
		LOGGER.debug("Exiting PendingEventsDAOImpl()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.PendingEventsDAO#find(java.lang.Long)
	 */
	@Transactional(readOnly=true)
	public PendingEvent find(Long id) throws AppException {
		LOGGER.info("Entering find()");
		LOGGER.debug("id: " + id);
		final PendingEvent pendingEvent = entityManager.find(PendingEvent.class, id);
		LOGGER.debug("PendingEvent: " + pendingEvent);
		LOGGER.info("Exiting find()");
		return pendingEvent;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.PendingEventsDAO#persist(com.ticketadvantage.services.model.PendingEvent)
	 */
	@Override
	@Transactional(readOnly = false)
	public PendingEvent persist(PendingEvent pendingEvent) throws AppException {
		LOGGER.info("Entering persist()");
		LOGGER.debug("PendingEvent: " + pendingEvent);

		final List<PendingEvent> listPendingEvent = (List<PendingEvent>) entityManager
				.createQuery("SELECT pe FROM PendingEvent pe WHERE pe.ticketnum = :ticketnum "
						+ "AND pe.userid = :userid "
						+ "AND pe.pendingtype = :pendingtype "
						+ "AND pe.eventtype = :eventtype "
						+ "AND pe.gametype = :gametype "
						+ "AND pe.team = :team "
						+ "AND pe.linetype = :linetype")
				.setParameter("ticketnum", pendingEvent.getTicketnum())
				.setParameter("userid", pendingEvent.getUserid())
				.setParameter("pendingtype", pendingEvent.getPendingtype())
				.setParameter("eventtype", pendingEvent.getEventtype())
				.setParameter("gametype", pendingEvent.getGametype())
				.setParameter("team", pendingEvent.getTeam())
				.setParameter("linetype", pendingEvent.getLinetype())
				.getResultList();

		if ((listPendingEvent == null) || (listPendingEvent != null && listPendingEvent.size() == 0)) {
			LOGGER.debug("Persisting PendingEvent");
			// Write out the user to the users tables
			pendingEvent.setId(null);
			entityManager.persist(pendingEvent);
		} else if (listPendingEvent != null && listPendingEvent.size() > 0) {
			LOGGER.debug("Already a pending event");
			pendingEvent = listPendingEvent.get(0);
		}
		LOGGER.info("Exiting persist()");
		return pendingEvent;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.PendingEventsDAO#update(java.lang.Long, com.ticketadvantage.services.model.PendingEvent)
	 */
	@Override
	@Transactional(readOnly = false)
	public void update(Long id, PendingEvent pendingEvent) throws AppException {
		LOGGER.info("Entering update()");
		LOGGER.debug("id: " + id);
		LOGGER.debug("PendingEvent: " + pendingEvent);
		PendingEvent tempPendingEvent = entityManager.find(PendingEvent.class, id);
		tempPendingEvent = copyPendingEvent(pendingEvent, tempPendingEvent);
		LOGGER.debug("PendingEvent: " + tempPendingEvent);
		entityManager.merge(tempPendingEvent);
		LOGGER.info("Exiting update()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.PendingEventsDAO#delete(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = false)
	public void delete(Long id) throws AppException {
		LOGGER.info("Entering delete()");
		PendingEvent pendingEvent = entityManager.find(PendingEvent.class, id);
		if (pendingEvent != null) {				
			// Remove the group
			entityManager.remove(pendingEvent);
		} else {
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}
		LOGGER.info("Exiting delete()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.PendingEventsDAO#findAll()
	 */
	public List<PendingEvent> findAll() throws AppException {
		LOGGER.info("Entering findAll()");
		final List<PendingEvent> retValue = entityManager.createQuery("SELECT pe FROM PendingEvent pe").getResultList();
		LOGGER.info("Exiting findAll()");
		return retValue;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.PendingEventsDAO#findPendingEventsByUserId(java.lang.Long)
	 */
	public List<PendingEvent> findPendingEventsByUserId(Long userid) throws AppException {
		LOGGER.info("Entering findPendingEventsByUserId()");
		final List<PendingEvent> listPendingEvent = (List<PendingEvent>) entityManager
				.createQuery("SELECT pe FROM PendingEvent pe WHERE pe.userid = :userid")
				.setParameter("userid", userid)
				.getResultList();
		LOGGER.info("Exiting findPendingEventsByUserId()");
		return listPendingEvent;		
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.PendingEventsDAO#findPendingEventsByUserIdByType(java.lang.Long, java.lang.String)
	 */
	public List<PendingEvent> findPendingEventsByUserIdByType(Long userid, String pendingType) throws AppException {
		LOGGER.info("Entering findPendingEventsByUserIdByType()");
		final List<PendingEvent> listPendingEvent = (List<PendingEvent>) entityManager
				.createQuery("SELECT pe FROM PendingEvent pe WHERE pe.userid = :userid AND pe.pendingtype = :pendingtype")
				.setParameter("userid", userid)
				.setParameter("pendingtype", pendingType)
				.getResultList();
		LOGGER.info("Exiting findPendingEventsByUserIdByType()");
		return listPendingEvent;		
	}

	/**
	 * 
	 * @param pendingEvent1
	 * @param pendingEvent2
	 * @return
	 */
	private PendingEvent copyPendingEvent(PendingEvent pendingEvent1, PendingEvent pendingEvent2) {
		pendingEvent2.setAccountid(pendingEvent1.getAccountid());
		pendingEvent2.setAccountname(pendingEvent1.getAccountname());
		pendingEvent2.setDatecreated(pendingEvent1.getDatecreated());
		pendingEvent2.setDatemodified(pendingEvent1.getDatemodified());
		pendingEvent2.setEventdate(pendingEvent1.getEventdate());
		pendingEvent2.setEventtype(pendingEvent1.getEventtype());
		pendingEvent2.setGamesport(pendingEvent1.getGamesport());
		pendingEvent2.setGametype(pendingEvent1.getGametype());
		pendingEvent2.setJuice(pendingEvent1.getJuice());
		pendingEvent2.setJuiceplusminus(pendingEvent1.getJuiceplusminus());
		pendingEvent2.setLine(pendingEvent1.getLine());
		pendingEvent2.setLineplusminus(pendingEvent1.getLineplusminus());
		pendingEvent2.setLinetype(pendingEvent1.getLinetype());
		pendingEvent2.setRisk(pendingEvent1.getRisk());
		pendingEvent2.setRotationid(pendingEvent1.getRotationid());
		pendingEvent2.setTeam(pendingEvent1.getTeam());
		pendingEvent2.setTicketnum(pendingEvent1.getTicketnum());
		pendingEvent2.setEventtype(pendingEvent1.getEventtype());
		pendingEvent2.setWin(pendingEvent1.getWin());

		return pendingEvent2;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.RecordEventDAO#setEntityManager(javax.persistence.EntityManager)
	 */
	@Override
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
}