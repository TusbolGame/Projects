/**
 * 
 */
package com.ticketadvantage.services.dao;

import java.util.Date;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.ws.rs.core.StreamingOutput;

import com.ticketadvantage.services.errorhandling.AppException;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.ClosingLine;
import com.ticketadvantage.services.model.CommittedEvents;
import com.ticketadvantage.services.model.EventPackage;
import com.ticketadvantage.services.model.EventsPackage;

/**
 * @author jmiller
 *
 */
public interface EventsDAO {

	/**
	 * 
	 * @return
	 * @throws AppException
	 */
	public Set<EventPackage> getAllEvents() throws AppException;

	/**
	 * 
	 * @param eventType
	 * @return
	 * @throws AppException
	 */
	public EventsPackage getEvents(String eventType) throws AppException;
	
	/**
	 * 
	 * @param eventid
	 * @return
	 * @throws AppException
	 */
	public EventPackage getEvent(Long eventid) throws AppException;
	
	/**
	 * 
	 * @param searchString
	 * @return
	 * @throws AppException
	 */
	public EventsPackage findEvents(String searchString) throws AppException;

	/**
	 * 
	 * @param userid
	 * @return
	 * @throws AppException
	 */
	public CommittedEvents getAllEventsByUser(Long userid) throws AppException;

	/**
	 * 
	 * @param userid
	 * @param fromdate
	 * @param todate
	 * @param groupid
	 * @param accountid
	 * @param timeZone
	 * @return
	 * @throws AppException
	 */
	public CommittedEvents getEventsByUserByFilter(Long userid, Date fromdate, Date todate, Long groupid, Long accountid, String timeZone) throws AppException;

	/**
	 * 
	 * @param userid
	 * @param fromdate
	 * @param todate
	 * @param groupid
	 * @param accountid
	 * @param timeZone
	 * @return
	 * @throws AppException
	 */
	public StreamingOutput createSpreadSheetByUserByFilter(Long userid, Date fromdate, Date todate, Long groupid, Long accountid, String timeZone, boolean all) throws AppException;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws AppException
	 */
	public CommittedEvents getSpreadEventById(Long id) throws AppException;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws AppException
	 */
	public CommittedEvents getTotalEventById(Long id) throws AppException;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws AppException
	 */
	public CommittedEvents getMlEventById(Long id) throws AppException;
	
	/**
	 * 
	 * @param entityManager
	 */
	public void setEntityManager(EntityManager entityManager);

	/**
	 * 
	 * @return
	 * @throws AppException
	 */
	public Set<EventPackage> getAllEventsNoCache() throws AppException;
	
	/**
	 * 
	 * @param linetype
	 * @param sportsInsightsEventId
	 * @param ep
	 * @return
	 * @throws AppException
	 */
	public EventPackage getScores(String linetype, Integer sportsInsightsEventId, EventPackage ep) throws BatchException;
	
	/**
	 * 
	 * @param sportsInsightEventId
	 * @param lineType
	 * @param gameType
	 * @param sportId
	 * @return
	 * @throws AppException
	 */
	public ClosingLine getClosingLine(Integer sportsInsightsEventId, Integer lineType, Integer gameType, Integer sportId) throws BatchException;

	/**
	 * 
	 * @param sportType
	 * @return
	 * @throws BatchException
	 */
	public EventsPackage getSport(String sportType) throws BatchException;
}