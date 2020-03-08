/**
 * 
 */
package com.ticketadvantage.services.dao;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ticketadvantage.services.GlobalProperties;
import com.ticketadvantage.services.dao.sites.sportsinsights.SportsInsightsSite;
import com.ticketadvantage.services.dao.sites.stub.StubProcessSite;
import com.ticketadvantage.services.dao.sites.vegasinsider.VegasInsiderProcessSite;
import com.ticketadvantage.services.errorhandling.AppErrorCodes;
import com.ticketadvantage.services.errorhandling.AppErrorMessage;
import com.ticketadvantage.services.errorhandling.AppException;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.AccountEvent;
import com.ticketadvantage.services.model.AccountEventFinal;
import com.ticketadvantage.services.model.ClosingLine;
import com.ticketadvantage.services.model.CommittedEvent;
import com.ticketadvantage.services.model.CommittedEvents;
import com.ticketadvantage.services.model.EventPackage;
import com.ticketadvantage.services.model.EventsPackage;
import com.ticketadvantage.services.model.MlRecordEvent;
import com.ticketadvantage.services.model.SpreadRecordEvent;
import com.ticketadvantage.services.model.TotalRecordEvent;

/**
 * @author jmiller
 *
 */
@Repository
public class EventsDAOImpl implements EventsDAO {
	private final static Logger LOGGER = Logger.getLogger(EventsDAOImpl.class);
	private final static DateFormat DF = new SimpleDateFormat("MM/dd/yyyy h:mm a z");

	@PersistenceContext(name = "entityManager")
	private EntityManager entityManager;

	private static Set<EventPackage> AllGames;
	private SportsInsightsSite sportsInsightSite;
	private StubProcessSite stubProcessSite;

	/**
	 * 
	 */
	public EventsDAOImpl() {
		super();
		LOGGER.debug("Entering EventsDAOImpl()");

		sportsInsightSite = new SportsInsightsSite("https://sportsinsights.actionnetwork.com", "mojaxsventures@gmail.com",
				"action1");
		stubProcessSite = new StubProcessSite("http://stub.com", "john", "jeremy");

		LOGGER.debug("Exiting EventsDAOImpl()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			Integer ownerPercentage = new Integer(75);
			Float riskAmnt = Float.parseFloat("220");
			float divAmount = ownerPercentage/100.0f;
			System.out.println("divAmount: " + divAmount);
			String eventAmount = df.format(riskAmnt * divAmount);
			System.out.println("eventAmount1: " + eventAmount);
			eventAmount = eventAmount.replace(",", "");
			System.out.println("eventAmount: " + eventAmount);

			Float ea = Float.parseFloat(eventAmount);
			System.out.println("ea: " + ea);
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ticketadvantage.services.dao.EventsDAO#getAllEvents()
	 */
	@Override
	public Set<EventPackage> getAllEvents() throws AppException {
		LOGGER.info("Entering getAllEvents()");
		Set<EventPackage> ep = null;

		AllGames = ep = sportsInsightSite.getAllEvents();

		LOGGER.debug("EventsPackage: " + ep);
		LOGGER.info("Exiting getAllEvents()");
		return ep;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ticketadvantage.services.dao.EventsDAO#getEvents(java.lang.String)
	 */
	@Override
	public EventsPackage getEvents(String eventType) throws AppException {
		LOGGER.info("Entering getEvents()");
		LOGGER.debug("eventType: " + eventType);
		EventsPackage ep = null;

		// Check for event type
		try {
			ep = sportsInsightSite.getSport(eventType);
			if (ep == null || ep.getEvents() == null || ep.getEvents().isEmpty()) {
				sportsInsightSite.getAllEvents();
				ep = sportsInsightSite.getSport(eventType);
			}
		} catch (BatchException be) {
			LOGGER.error("Exception getting event data", be);
			throw new AppException(500, AppErrorCodes.EVENTS_EXCEPTION, AppErrorMessage.EVENTS_EXCEPTION);
		}
		LOGGER.debug("EventsPackage: " + ep);
		LOGGER.info("Exiting getEvents()");
		return ep;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ticketadvantage.services.dao.EventsDAO#getEvent(java.lang.Long)
	 */
	@Override
	public EventPackage getEvent(Long eventid) throws AppException {
		LOGGER.info("Entering getEvent()");
		LOGGER.debug("Eventid: " + eventid);
		EventPackage ep = null;

		// Stub or regular?
		if (GlobalProperties.isStubbed()) {
			ep = stubProcessSite.getGameEvent(eventid);
		} else {
			try {
				// Get all games
				EventsPackage eventsPackage = sportsInsightSite.getAllSports();
				for (EventPackage eventPackage : eventsPackage.getEvents()) {
					// Check game IDs
					LOGGER.debug("Event Team One ID: " + eventPackage.getTeamone().getId());
					LOGGER.debug("Event Team Two ID: " + eventPackage.getTeamtwo().getId());
					if (eventid.longValue() == eventPackage.getTeamone().getId().longValue() || 
						eventid.longValue() == eventPackage.getTeamtwo().getId().longValue()) {
						LOGGER.debug("Found eventID: " + eventid);
						ep = eventPackage;
						break;
					}
				}

				if (ep == null) {
					VegasInsiderProcessSite vips = new VegasInsiderProcessSite();
					ep = vips.getNcaabGameByRotationID(eventid.intValue());
				}
			} catch (BatchException be) {
				LOGGER.error(be);
			}
		}
		LOGGER.debug("EventPackage: " + ep);
		LOGGER.info("Exiting getEvent()");
		return ep;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ticketadvantage.services.dao.EventsDAO#findEvents(java.lang.String)
	 */
	@Override
	public EventsPackage findEvents(String searchString) throws AppException {
		LOGGER.info("Entering findEvents()");
		LOGGER.debug("searchString: " + searchString);
		final EventsPackage ep = new EventsPackage();

		try {
			EventsPackage eventsPackage = sportsInsightSite.getAllSports();
			for (EventPackage eventPackage : eventsPackage.getEvents()) {
				LOGGER.debug("EventPackage: " + eventPackage);

				// Check if string is in any of the packages
				if (eventPackage.search().contains(searchString)) {
					ep.addEvent(eventPackage);
				}
			}
		} catch (BatchException be) {
			LOGGER.debug(be);
		}

		LOGGER.debug("EventsPackage: " + ep);
		LOGGER.info("Exiting findEvents()");
		return ep;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ticketadvantage.services.dao.EventsDAO#getAllEventsByUser(java.lang.
	 * Long)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public CommittedEvents getAllEventsByUser(Long userid) throws AppException {
		LOGGER.info("Entering getAllEventsByUser()");
		LOGGER.debug("Userid: " + userid);
		final CommittedEvents committedEvents = new CommittedEvents();
		final List<CommittedEvent> events = new ArrayList<CommittedEvent>();
		try {
			final List<SpreadRecordEvent> retValue = entityManager
					.createQuery("SELECT sre FROM SpreadRecordEvent sre WHERE sre.userid = :userid")
					.setParameter("userid", userid).getResultList();
			LOGGER.debug("SpreadRecordEvents: " + retValue);
			if (retValue != null && !retValue.isEmpty()) {
				final Iterator<SpreadRecordEvent> itr = retValue.iterator();
				while (itr.hasNext()) {
					final CommittedEvent committedEvent = populateSpreadEvent(itr.next());
					events.add(committedEvent);
				}
			}

			final List<TotalRecordEvent> totalRecords = entityManager
					.createQuery("SELECT tre FROM TotalRecordEvent tre WHERE tre.userid = :userid")
					.setParameter("userid", userid).getResultList();
			LOGGER.debug("TotalRecordEvent: " + totalRecords);
			if (totalRecords != null && !totalRecords.isEmpty()) {
				final Iterator<TotalRecordEvent> itr = totalRecords.iterator();
				while (itr.hasNext()) {
					final CommittedEvent committedEvent = populateTotalEvent(itr.next());
					events.add(committedEvent);
				}
			}

			final List<MlRecordEvent> mlRecordEvent = entityManager
					.createQuery("SELECT mle FROM MlRecordEvent mle WHERE mle.userid = :userid")
					.setParameter("userid", userid).getResultList();
			LOGGER.debug("MlRecordEvent: " + mlRecordEvent);
			if (mlRecordEvent != null && !mlRecordEvent.isEmpty()) {
				final Iterator<MlRecordEvent> itr = mlRecordEvent.iterator();
				while (itr.hasNext()) {
					final CommittedEvent committedEvent = populateMlEvent(itr.next());
					events.add(committedEvent);
				}
			}

			// Compare the dates to order them appropriately
			Collections.sort(events, new Comparator<CommittedEvent>() {
				public int compare(CommittedEvent o1, CommittedEvent o2) {
					if (o1.getDatecreated() == null || o2.getDatecreated() == null)
						return 0;
					return o1.getDatecreated().compareTo(o2.getDatecreated());
				}
			});

			// Make the order correct
			final Set<CommittedEvent> cEvents = new LinkedHashSet<CommittedEvent>(events);
			committedEvents.setCommittedevents(cEvents);
		} catch (Exception e) {
			LOGGER.error("Exception", e);
		}
		LOGGER.info("Exiting getAllEventsByUser()");
		return committedEvents;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ticketadvantage.services.dao.EventsDAO#getEventsByUserByFilter(java.
	 * lang.Long, java.util.Date, java.util.Date, java.lang.Long,
	 * java.lang.Long, java.lang.String)
	 */
	@Override
	@Transactional(readOnly = true)
	public CommittedEvents getEventsByUserByFilter(Long userid, 
			Date fromdate, 
			Date todate, 
			Long groupid,
			Long accountid, 
			String timeZone) throws AppException {
		LOGGER.info("Entering getEventsByUserByFilter()");
		LOGGER.debug("userid: " + userid);
		LOGGER.debug("fromdate: " + fromdate);
		LOGGER.debug("todate: " + todate);
		LOGGER.debug("groupid: " + groupid);
		LOGGER.debug("accountid: " + accountid);
		final CommittedEvents committedEvents = new CommittedEvents();
		List<CommittedEvent> events = new ArrayList<CommittedEvent>();

		try {
			boolean groupOnly = false;
			boolean accountOnly = false;
			if (groupid.longValue() == 0 && accountid.longValue() == 0) {
				events = getDateCommittedEvents(userid, fromdate, todate, events);
			} else if (groupid.longValue() > 0) {
				groupOnly = true;
				events = getGroupCommittedEvents(userid, fromdate, todate, groupid, events);
			} else if (accountid.longValue() > 0) {
				accountOnly = true;
				events = getAccountCommittedEvents(userid, fromdate, todate, accountid, events);
			}

			// Compare the dates to order them appropriately
			Collections.sort(events, new Comparator<CommittedEvent>() {
				public int compare(CommittedEvent o1, CommittedEvent o2) {
					if (o1.getDatecreated() == null || o2.getDatecreated() == null)
						return 0;
					return o1.getDatecreated().compareTo(o2.getDatecreated());
				}
			});

			// Make the order correct
			final Set<CommittedEvent> cEvents = new LinkedHashSet<CommittedEvent>(events);
			committedEvents.setCommittedevents(cEvents);
			if (groupOnly) {
				committedEvents.setGroupid(groupid);
				committedEvents.setAccountid(new Long(0));
			} else if (accountOnly) {
				committedEvents.setAccountid(accountid);
				committedEvents.setGroupid(new Long(0));
			}
		} catch (Exception e) {
			LOGGER.error("Exception", e);
		}
		LOGGER.info("Exiting getEventsByUserByFilter()");
		return committedEvents;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.EventsDAO#createSpreadSheetByUserByFilter(java.lang.Long, java.util.Date, java.util.Date, java.lang.Long, java.lang.Long, java.lang.String, boolean)
	 */
	@Override
	public StreamingOutput createSpreadSheetByUserByFilter(Long userid, Date fromdate, Date todate, Long groupid,
			Long accountid, String timeZone, boolean all) throws AppException {
		LOGGER.debug("userid: " + userid);
		LOGGER.debug("fromdate: " + fromdate);
		LOGGER.debug("todate: " + todate);
		LOGGER.debug("groupid: " + groupid);
		LOGGER.debug("accountid: " + accountid);
		final CommittedEvents committedEvents = new CommittedEvents();
		List<CommittedEvent> events = new ArrayList<CommittedEvent>();
		StreamingOutput stream = null;

		final HSSFWorkbook workbook = new HSSFWorkbook();
		try {
			boolean groupOnly = false;
			boolean accountOnly = false;
			if (groupid.longValue() == 0 && accountid.longValue() == 0) {
				events = getDateCommittedEvents(userid, fromdate, todate, events);
				LOGGER.debug("events: " + events);
			} else if (groupid.longValue() > 0) {
				groupOnly = true;
				events = getGroupCommittedEvents(userid, fromdate, todate, groupid, events);
			} else if (accountid.longValue() > 0) {
				accountOnly = true;
				events = getAccountCommittedEvents(userid, fromdate, todate, accountid, events);
			}

			// Compare the dates to order them appropriately
			Collections.sort(events, new Comparator<CommittedEvent>() {
				public int compare(CommittedEvent o1, CommittedEvent o2) {
					if (o1.getEventdatetime() == null || o2.getEventdatetime() == null)
						return 0;
					return o1.getEventdatetime().compareTo(o2.getEventdatetime());
				}
			});
			
			HSSFCellStyle style1 = null;
			HSSFCellStyle style2 = null;
			HSSFCellStyle win1 = null;
			HSSFCellStyle loss1 = null;
			HSSFCellStyle push1 = null;
			HSSFCellStyle notplayed1 = null;
			HSSFCellStyle win2 = null;
			HSSFCellStyle loss2 = null;
			HSSFCellStyle push2 = null;
			HSSFCellStyle notplayed2 = null;

			// Create the worksheet
			try {
				short format = workbook.createDataFormat().getFormat("##%");

				win1 = workbook.createCellStyle();
				win1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				win1.setFillForegroundColor(HSSFColor.GREEN.index);

				loss1 = workbook.createCellStyle();
				loss1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				loss1.setFillForegroundColor(HSSFColor.RED.index);

				push1 = workbook.createCellStyle();
				push1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				push1.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);

				notplayed1 = workbook.createCellStyle();

				win2 = workbook.createCellStyle();
				win2.setDataFormat(format);
				win2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				win2.setFillForegroundColor(HSSFColor.GREEN.index);

				loss2 = workbook.createCellStyle();
				loss2.setDataFormat(format);
				loss2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				loss2.setFillForegroundColor(HSSFColor.RED.index);

				push2 = workbook.createCellStyle();
				push2.setDataFormat(format);
				push2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				push2.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
				
				notplayed2 = workbook.createCellStyle();
				notplayed2.setDataFormat(format);
			} catch (java.lang.IllegalArgumentException iae) {
				LOGGER.error(iae);
			}

			// Make the order correct
			final Set<CommittedEvent> cEvents = new LinkedHashSet<CommittedEvent>(events);
			committedEvents.setCommittedevents(cEvents);
			if (groupOnly || !accountOnly) {
				committedEvents.setGroupid(groupid);
				committedEvents.setAccountid(new Long(0));
				HSSFSheet sheet = null;
				int ceCount = 0;
				final Set<CommittedEvent> commitEvents = committedEvents.getCommittedevents();
				final Iterator<CommittedEvent> itr = commitEvents.iterator();
				
				try {
					sheet = workbook.createSheet("Transactions");
				} catch (java.lang.IllegalArgumentException iae) {
					LOGGER.error(iae);
				}

				int aeCount = 0;
				// setupXlsHeader(sheet.createRow((short)aeCount++));
				setupXlsHeaderSuccess(workbook, sheet.createRow((short) aeCount++));
				while (itr != null && itr.hasNext()) {
					final CommittedEvent committedEvent = itr.next();
					LOGGER.debug("committedEvent.getEventname(): " + committedEvent.getEventname());
					LOGGER.debug("ceCount: " + ceCount);

					// For every game
					++ceCount;

					// Now loop through all of the AccountEvents
					final Iterator<AccountEvent> itrAe = committedEvent.getAccountevents().iterator();

					// Go through AccountEvents
					while (itrAe != null && itrAe.hasNext()) {
						final AccountEvent ae = itrAe.next();
						style1 = notplayed1;
						style2 = notplayed2;									

						if (ae.getStatus().equals("Complete")) {
							if (ae.getEventresult() != null) {
								if (ae.getEventresult().equals("WIN")) {
									style1 = win1;
									style2 = win2;
								} else if (ae.getEventresult().equals("LOSS")) {
									style1 = loss1;
									style2 = loss2;
								} else if (ae.getEventresult().equals("PUSH")) {
									style1 = push1;
									style2 = push2;
								}
							}
							setupXlsSuccessData(workbook, sheet, sheet.createRow((short) aeCount++), committedEvent, ae, ceCount, timeZone, style1, style2);
//							setupXlsData(sheet.createRow((short) aeCount++), committedEvent, ae, ceCount, timeZone);
						} else if (all) {
							setupXlsSuccessData(workbook, sheet, sheet.createRow((short) aeCount++), committedEvent, ae, ceCount, timeZone, style1, style2);
						}
					}
				}
			} else if (accountOnly) {
				committedEvents.setAccountid(accountid);
				committedEvents.setGroupid(new Long(0));

				HSSFSheet sheet = null;
				int aeCount = 0;
				final Set<CommittedEvent> commitEvents = committedEvents.getCommittedevents();
				final Iterator<CommittedEvent> itr = commitEvents.iterator();
				while (itr != null && itr.hasNext()) {
					final CommittedEvent committedEvent = itr.next();
					final Set<AccountEvent> accountEvents = committedEvent.getAccountevents();
					final Iterator<AccountEvent> itrAe = accountEvents.iterator();
					while (itrAe != null && itrAe.hasNext()) {
						final AccountEvent ae = itrAe.next();
						style1 = notplayed1;
						style2 = notplayed2;									
						
						if (ae.getEventresult() != null) {
							if (ae.getEventresult().equals("WIN")) {
								style1 = win1;
								style2 = win2;
							} else if (ae.getEventresult().equals("LOSS")) {
								style1 = loss1;
								style2 = loss2;
							} else if (ae.getEventresult().equals("PUSH")) {
								style1 = push1;
								style2 = push2;
							}
						}

						if (aeCount++ == 0) {
							sheet = workbook.createSheet(ae.getName());
							// setupAccountXlsHeader(sheet.createRow((short) 0));
							setupXlsHeaderSuccess(workbook, sheet.createRow((short) aeCount++));
							setupXlsSuccessData(workbook, sheet, sheet.createRow((short) aeCount++), committedEvent, ae, 0, timeZone, style1, style2);
							// setupAccountXlsData(sheet.createRow((short) aeCount), committedEvent, ae);
						} else {
							setupXlsHeaderSuccess(workbook, sheet.createRow((short) aeCount++));
							setupXlsSuccessData(workbook, sheet, sheet.createRow((short) aeCount++), committedEvent, ae, 0, timeZone, style1, style2);
							// setupAccountXlsData(sheet.createRow((short) aeCount), committedEvent, ae);
						}
					}
				}
			}

			stream = new StreamingOutput() {
				public void write(OutputStream output) throws IOException, WebApplicationException {
					try {
						workbook.write(output);
					} catch (Exception e) {
						throw new WebApplicationException(e);
					}
				}
			};
		} catch (Exception e) {
			LOGGER.error(e);
		}

		return stream;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ticketadvantage.services.dao.EventsDAO#getSpreadEventById(java.lang.
	 * Long)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public CommittedEvents getSpreadEventById(Long id) throws AppException {
		LOGGER.info("Entering getSpreadEventById()");
		LOGGER.debug("id: " + id);
		final CommittedEvents committedEvents = new CommittedEvents();
		final List<CommittedEvent> events = new ArrayList<CommittedEvent>();
		try {
			final List<SpreadRecordEvent> retValue = entityManager
					.createQuery("SELECT sre FROM SpreadRecordEvent sre WHERE sre.id = :id").setParameter("id", id)
					.getResultList();
			LOGGER.debug("SpreadRecordEvents: " + retValue);
			if (retValue != null && !retValue.isEmpty()) {
				final Iterator<SpreadRecordEvent> itr = retValue.iterator();
				while (itr.hasNext()) {
					final CommittedEvent committedEvent = populateSpreadEvent(itr.next());
					events.add(committedEvent);
				}
			}

			// Compare the dates to order them appropriately
			Collections.sort(events, new Comparator<CommittedEvent>() {
				public int compare(CommittedEvent o1, CommittedEvent o2) {
					if (o1.getDatecreated() == null || o2.getDatecreated() == null)
						return 0;
					return o1.getDatecreated().compareTo(o2.getDatecreated());
				}
			});

			// Make the order correct
			final Set<CommittedEvent> cEvents = new LinkedHashSet<CommittedEvent>(events);
			committedEvents.setCommittedevents(cEvents);
		} catch (Exception e) {
			LOGGER.error("Exception", e);
		}
		LOGGER.info("Exiting getSpreadEventById()");
		return committedEvents;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ticketadvantage.services.dao.EventsDAO#getTotalEventById(java.lang.
	 * Long)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public CommittedEvents getTotalEventById(Long id) throws AppException {
		LOGGER.info("Entering getTotalEventById()");
		LOGGER.debug("id: " + id);
		final CommittedEvents committedEvents = new CommittedEvents();
		final List<CommittedEvent> events = new ArrayList<CommittedEvent>();

		try {
			final List<TotalRecordEvent> totalRecords = entityManager
					.createQuery("SELECT tre FROM TotalRecordEvent tre WHERE tre.id = :id").setParameter("id", id)
					.getResultList();
			LOGGER.debug("TotalRecordEvent: " + totalRecords);
			if (totalRecords != null && !totalRecords.isEmpty()) {
				final Iterator<TotalRecordEvent> itr = totalRecords.iterator();
				while (itr.hasNext()) {
					final CommittedEvent committedEvent = populateTotalEvent(itr.next());
					events.add(committedEvent);
				}
			}

			// Compare the dates to order them appropriately
			Collections.sort(events, new Comparator<CommittedEvent>() {
				public int compare(CommittedEvent o1, CommittedEvent o2) {
					if (o1.getDatecreated() == null || o2.getDatecreated() == null)
						return 0;
					return o1.getDatecreated().compareTo(o2.getDatecreated());
				}
			});

			// Make the order correct
			final Set<CommittedEvent> cEvents = new LinkedHashSet<CommittedEvent>(events);
			committedEvents.setCommittedevents(cEvents);
		} catch (Exception e) {
			LOGGER.error("Exception", e);
		}
		LOGGER.info("Exiting getTotalEventById()");
		return committedEvents;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ticketadvantage.services.dao.EventsDAO#getMlEventById(java.lang.Long)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public CommittedEvents getMlEventById(Long id) throws AppException {
		LOGGER.info("Entering getMlEventById()");
		LOGGER.debug("id: " + id);
		final CommittedEvents committedEvents = new CommittedEvents();
		final List<CommittedEvent> events = new ArrayList<CommittedEvent>();
		try {
			final List<MlRecordEvent> mlRecordEvent = entityManager
					.createQuery("SELECT mle FROM MlRecordEvent mle WHERE mle.id = :id").setParameter("id", id)
					.getResultList();
			LOGGER.debug("MlRecordEvent: " + mlRecordEvent);
			if (mlRecordEvent != null && !mlRecordEvent.isEmpty()) {
				final Iterator<MlRecordEvent> itr = mlRecordEvent.iterator();
				while (itr.hasNext()) {
					final CommittedEvent committedEvent = populateMlEvent(itr.next());
					events.add(committedEvent);
				}
			}

			// Compare the dates to order them appropriately
			Collections.sort(events, new Comparator<CommittedEvent>() {
				public int compare(CommittedEvent o1, CommittedEvent o2) {
					if (o1.getDatecreated() == null || o2.getDatecreated() == null)
						return 0;
					return o1.getDatecreated().compareTo(o2.getDatecreated());
				}
			});

			// Make the order correct
			final Set<CommittedEvent> cEvents = new LinkedHashSet<CommittedEvent>(events);
			committedEvents.setCommittedevents(cEvents);
		} catch (Exception e) {
			LOGGER.error("Exception", e);
		}
		LOGGER.info("Exiting getMlEventById()");
		return committedEvents;
	}

	/**
	 * 
	 * @param id
	 * @return
	 * @throws AppException
	 */
	@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")
	private List<AccountEvent> getSpreadAccountEvents(Long id) throws AppException {
		LOGGER.info("Entering getAccountEvents()");
		LOGGER.debug("id: " + id);
		List<AccountEvent> retValue = null;

		try {
			retValue = entityManager
					.createQuery("SELECT ae FROM AccountEvent ae WHERE ae.spreadid = :spreadid ORDER BY name ASC")
					.setParameter("spreadid", id).getResultList();
			for (int x = 0; (retValue != null && x < retValue.size()); x++) {
				AccountEvent accountEvent = retValue.get(x);
//				String aHtml = accountEvent.getAccounthtml();
//				aHtml = "<![CDATA[" + aHtml + "]]>";
				accountEvent.setAccounthtml("");
				Float juiceValue = accountEvent.getSpreadjuice();
				determineAmounts(juiceValue, accountEvent);
				String ticketNum = accountEvent.getAccountconfirmation();
				if (ticketNum != null) {
					ticketNum = ticketNum.replace("Ticket Number - ", "");
					ticketNum = ticketNum.replace("<TD id=\"sbo_ticket_num\">", "");
					accountEvent.setAccountconfirmation(ticketNum);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Exception getting account events: " + e.getMessage(), e);
			throw new AppException("Exception getting account events");
		}

		LOGGER.debug("AccountEvents: " + retValue);
		LOGGER.info("Exiting getAccountEvents()");
		return retValue;
	}

	/**
	 * 
	 * @param id
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	private List<AccountEvent> getTotalAccountEvents(Long id) throws AppException {
		LOGGER.info("Entering getAccountEvents()");
		LOGGER.debug("id: " + id);
		List<AccountEvent> retValue = null;
		try {
			retValue = entityManager
					.createQuery("SELECT ae FROM AccountEvent ae WHERE ae.totalid = :totalid ORDER BY name ASC")
					.setParameter("totalid", id).getResultList();
			for (int x = 0; (retValue != null && x < retValue.size()); x++) {
				AccountEvent accountEvent = retValue.get(x);
//				String aHtml = accountEvent.getAccounthtml();
//				aHtml = "<![CDATA[" + aHtml + "]]>";
				accountEvent.setAccounthtml("");
				Float juiceValue = accountEvent.getTotaljuice();
				determineAmounts(juiceValue, accountEvent);
				String ticketNum = accountEvent.getAccountconfirmation();
				if (ticketNum != null) {
					ticketNum = ticketNum.replace("Ticket Number - ", "");
					ticketNum = ticketNum.replace("<TD id=\"sbo_ticket_num\">", "");
					accountEvent.setAccountconfirmation(ticketNum);
				}
			}
		} catch (Exception e) {
			LOGGER.error(e);
			throw new AppException("Exception getting account events");
		}
		LOGGER.debug("AccountEvents: " + retValue);
		LOGGER.info("Exiting getAccountEvents()");
		return retValue;
	}

	/**
	 * 
	 * @param id
	 * @return
	 * @throws BatchException
	 */
	@SuppressWarnings("unchecked")
	private List<AccountEvent> getMlAccountEvents(Long id) throws AppException {
		LOGGER.info("Entering getAccountEvents()");
		LOGGER.debug("id: " + id);
		List<AccountEvent> retValue = null;
		try {
			retValue = entityManager
					.createQuery("SELECT ae FROM AccountEvent ae WHERE ae.mlid = :mlid ORDER BY name ASC")
					.setParameter("mlid", id).getResultList();
			for (int x = 0; (retValue != null && x < retValue.size()); x++) {
				AccountEvent accountEvent = retValue.get(x);
//				String aHtml = accountEvent.getAccounthtml();
//				aHtml = "<![CDATA[" + aHtml + "]]>";
				accountEvent.setAccounthtml("");

				// First determine risk and win
				Float juiceValue = accountEvent.getMljuice();
				determineAmounts(juiceValue, accountEvent);

				// Message ticket number to only show ticket #
				String ticketNum = accountEvent.getAccountconfirmation();
				if (ticketNum != null) {
					ticketNum = ticketNum.replace("Ticket Number - ", "");
					ticketNum = ticketNum.replace("<TD id=\"sbo_ticket_num\">", "");
					accountEvent.setAccountconfirmation(ticketNum);
				}
			}
		} catch (Exception e) {
			LOGGER.error(e);
			throw new AppException("Exception getting account events");
		}
		LOGGER.debug("AccountEvents: " + retValue);
		LOGGER.info("Exiting getAccountEvents()");
		return retValue;
	}

	/**
	 * 
	 * @param sre
	 * @return
	 * @throws AppException
	 */
	private CommittedEvent populateSpreadEvent(SpreadRecordEvent sre) throws AppException {
		LOGGER.debug("Entering populateSpreadEvent()");

		// Populate committed event
		final CommittedEvent committedEvent = populateCommittedSpreadEvent(sre);

		// Now get the account events
		final List<AccountEvent> aes = getSpreadAccountEvents(sre.getId());
		final Set<AccountEvent> accountevents = new LinkedHashSet<AccountEvent>(aes);
		LOGGER.debug("AccountEvents: " + accountevents);
		committedEvent.setAccountevents(accountevents);

		LOGGER.debug("CommittedEvent: " + committedEvent);
		LOGGER.debug("Exiting populateSpreadEvent()");
		return committedEvent;
	}

	/**
	 * 
	 * @param sre
	 * @return
	 */
	private CommittedEvent populateCommittedSpreadEvent(SpreadRecordEvent sre) {
		LOGGER.debug("Entering populateCommittedSpreadEvent()");

		final CommittedEvent committedEvent = new CommittedEvent();
		committedEvent.setAccountid(sre.getAccountid());
		committedEvent.setAmount(sre.getAmount());
		committedEvent.setAttempts(sre.getAttempts());
		committedEvent.setDatecreated(sre.getDatecreated());
		committedEvent.setDatemodified(sre.getDatemodified());
		committedEvent.setDatentime(sre.getDatentime());
		committedEvent.setEventdatetime(sre.getEventdatetime());
		committedEvent.setEventid(sre.getEventid());
		committedEvent.setEventname(sre.getEventname());
		committedEvent.setEventteam1(sre.getEventteam1());
		committedEvent.setEventteam2(sre.getEventteam2());
		committedEvent.setEventtype(sre.getEventtype());
		committedEvent.setGroupid(sre.getGroupid());
		committedEvent.setId(sre.getId());
		committedEvent.setIscompleted(sre.getIscompleted());
		committedEvent.setSport(sre.getSport());
		committedEvent.setSpreadinputfirstone(sre.getSpreadinputfirstone());
		committedEvent.setSpreadinputfirsttwo(sre.getSpreadinputfirsttwo());
		committedEvent.setSpreadinputjuicefirstone(sre.getSpreadinputjuicefirstone());
		committedEvent.setSpreadinputjuicefirsttwo(sre.getSpreadinputjuicefirsttwo());
		committedEvent.setSpreadinputjuicesecondone(sre.getSpreadinputjuicesecondone());
		committedEvent.setSpreadinputjuicesecondtwo(sre.getSpreadinputjuicesecondtwo());
		committedEvent.setSpreadinputsecondone(sre.getSpreadinputsecondone());
		committedEvent.setSpreadinputsecondtwo(sre.getSpreadinputsecondtwo());
		committedEvent.setSpreadjuiceplusminusfirstone(sre.getSpreadjuiceplusminusfirstone());
		committedEvent.setSpreadjuiceplusminusfirsttwo(sre.getSpreadjuiceplusminusfirsttwo());
		committedEvent.setSpreadjuiceplusminussecondone(sre.getSpreadjuiceplusminussecondone());
		committedEvent.setSpreadjuiceplusminussecondtwo(sre.getSpreadjuiceplusminussecondtwo());
		committedEvent.setSpreadplusminusfirstone(sre.getSpreadplusminusfirstone());
		committedEvent.setSpreadplusminusfirsttwo(sre.getSpreadplusminusfirsttwo());
		committedEvent.setSpreadplusminussecondone(sre.getSpreadplusminussecondone());
		committedEvent.setSpreadplusminussecondtwo(sre.getSpreadplusminussecondtwo());
		committedEvent.setUserid(sre.getUserid());
		committedEvent.setWtype(sre.getWtype());
		if (sre.getScrappername() == null || sre.getScrappername().length() == 0) {
			committedEvent.setScrappername("");
		} else {
			committedEvent.setScrappername(sre.getScrappername());
		}
		if (sre.getActiontype() == null || sre.getActiontype().length() == 0) {
			committedEvent.setActiontype("");
		} else {
			committedEvent.setActiontype(sre.getActiontype());
		}
		committedEvent.setTextnumber(sre.getTextnumber());

		LOGGER.debug("Exiting populateCommittedSpreadEvent()");
		return committedEvent;
	}

	/**
	 * 
	 * @param tre
	 * @return
	 * @throws AppException
	 */
	private CommittedEvent populateTotalEvent(TotalRecordEvent tre) throws AppException {
		LOGGER.debug("Entering populateTotalEvent()");

		// Populate committed event
		final CommittedEvent committedEvent = populateCommittedTotalEvent(tre);

		// Now get the account events
		final List<AccountEvent> aes = getTotalAccountEvents(tre.getId());
		final Set<AccountEvent> accountevents = new LinkedHashSet<AccountEvent>(aes);
		LOGGER.debug("AccountEvents: " + accountevents);
		committedEvent.setAccountevents(accountevents);

		LOGGER.debug("CommittedEvent: " + committedEvent);
		LOGGER.debug("Exiting populateTotalEvent()");
		return committedEvent;
	}

	/**
	 * 
	 * @param tre
	 * @return
	 */
	private CommittedEvent populateCommittedTotalEvent(TotalRecordEvent tre) {
		LOGGER.debug("Entering populateCommittedTotalEvent()");

		final CommittedEvent committedEvent = new CommittedEvent();
		committedEvent.setAccountid(tre.getAccountid());
		committedEvent.setAmount(tre.getAmount());
		committedEvent.setAttempts(tre.getAttempts());
		committedEvent.setDatecreated(tre.getDatecreated());
		committedEvent.setDatemodified(tre.getDatemodified());
		committedEvent.setDatentime(tre.getDatentime());
		committedEvent.setEventdatetime(tre.getEventdatetime());
		committedEvent.setEventid(tre.getEventid());
		committedEvent.setEventname(tre.getEventname());
		committedEvent.setEventteam1(tre.getEventteam1());
		committedEvent.setEventteam2(tre.getEventteam2());
		committedEvent.setEventtype(tre.getEventtype());
		committedEvent.setGroupid(tre.getGroupid());
		committedEvent.setId(tre.getId());
		committedEvent.setIscompleted(tre.getIscompleted());
		committedEvent.setSport(tre.getSport());
		committedEvent.setTotalinputfirstone(tre.getTotalinputfirstone());
		committedEvent.setTotalinputfirsttwo(tre.getTotalinputfirsttwo());
		committedEvent.setTotalinputjuicefirstone(tre.getTotalinputjuicefirstone());
		committedEvent.setTotalinputjuicefirsttwo(tre.getTotalinputjuicefirsttwo());
		committedEvent.setTotalinputjuicesecondone(tre.getTotalinputjuicesecondone());
		committedEvent.setTotalinputjuicesecondtwo(tre.getTotalinputjuicesecondtwo());
		committedEvent.setTotalinputsecondone(tre.getTotalinputsecondone());
		committedEvent.setTotalinputsecondtwo(tre.getTotalinputsecondtwo());
		committedEvent.setTotaljuiceplusminusfirstone(tre.getTotaljuiceplusminusfirstone());
		committedEvent.setTotaljuiceplusminusfirsttwo(tre.getTotaljuiceplusminusfirsttwo());
		committedEvent.setTotaljuiceplusminussecondone(tre.getTotaljuiceplusminussecondone());
		committedEvent.setTotaljuiceplusminussecondtwo(tre.getTotaljuiceplusminussecondtwo());
		committedEvent.setUserid(tre.getUserid());
		committedEvent.setWtype(tre.getWtype());
		if (tre.getScrappername() == null || tre.getScrappername().length() == 0) {
			committedEvent.setScrappername("");
		} else {
			committedEvent.setScrappername(tre.getScrappername());
		}
		if (tre.getActiontype() == null || tre.getActiontype().length() == 0) {
			committedEvent.setActiontype("");
		} else {
			committedEvent.setActiontype(tre.getActiontype());
		}
		committedEvent.setTextnumber(tre.getTextnumber());

		LOGGER.debug("Exiting populateCommittedTotalEvent()");
		return committedEvent;
	}

	/**
	 * 
	 * @param mle
	 * @return
	 * @throws AppException
	 */
	private CommittedEvent populateMlEvent(MlRecordEvent mle) throws AppException {
		LOGGER.debug("Entering populateMlEvent()");

		// Get the CommittedEvents
		final CommittedEvent committedEvent = populateCommittedMlEvent(mle);

		// Now get the account events
		final List<AccountEvent> aes = getMlAccountEvents(mle.getId());
		final Set<AccountEvent> accountevents = new LinkedHashSet<AccountEvent>(aes);
		LOGGER.debug("AccountEvents: " + accountevents);
		committedEvent.setAccountevents(accountevents);

		LOGGER.debug("CommittedEvent: " + committedEvent);
		LOGGER.debug("Exiting populateMlEvent()");
		return committedEvent;
	}

	/**
	 * 
	 * @param mle
	 * @return
	 */
	private CommittedEvent populateCommittedMlEvent(MlRecordEvent mle) {
		LOGGER.debug("Entering populateCommittedMlEvent()");

		final CommittedEvent committedEvent = new CommittedEvent();
		committedEvent.setAccountid(mle.getAccountid());
		committedEvent.setAmount(mle.getAmount());
		committedEvent.setAttempts(mle.getAttempts());
		committedEvent.setDatecreated(mle.getDatecreated());
		committedEvent.setDatemodified(mle.getDatemodified());
		committedEvent.setDatentime(mle.getDatentime());
		committedEvent.setEventdatetime(mle.getEventdatetime());
		committedEvent.setEventid(mle.getEventid());
		committedEvent.setEventname(mle.getEventname());
		committedEvent.setEventteam1(mle.getEventteam1());
		committedEvent.setEventteam2(mle.getEventteam2());
		committedEvent.setEventtype(mle.getEventtype());
		committedEvent.setGroupid(mle.getGroupid());
		committedEvent.setId(mle.getId());
		committedEvent.setIscompleted(mle.getIscompleted());
		committedEvent.setSport(mle.getSport());
		committedEvent.setMlinputfirstone(mle.getMlinputfirstone());
		committedEvent.setMlinputfirsttwo(mle.getMlinputfirsttwo());
		committedEvent.setMlinputsecondone(mle.getMlinputsecondone());
		committedEvent.setMlinputsecondtwo(mle.getMlinputsecondtwo());
		committedEvent.setMlplusminusfirstone(mle.getMlplusminusfirstone());
		committedEvent.setMlplusminusfirsttwo(mle.getMlplusminusfirsttwo());
		committedEvent.setMlplusminussecondone(mle.getMlplusminussecondone());
		committedEvent.setMlplusminussecondtwo(mle.getMlplusminussecondtwo());
		committedEvent.setUserid(mle.getUserid());
		committedEvent.setWtype(mle.getWtype());
		if (mle.getScrappername() == null || mle.getScrappername().length() == 0) {
			committedEvent.setScrappername("");
		} else {
			committedEvent.setScrappername(mle.getScrappername());
		}
		if (mle.getActiontype() == null || mle.getActiontype().length() == 0) {
			committedEvent.setActiontype("");
		} else {
			committedEvent.setActiontype(mle.getActiontype());
		}
		committedEvent.setTextnumber(mle.getTextnumber());

		LOGGER.debug("Exiting populateCommittedMlEvent()");
		return committedEvent;
	}

	/**
	 * 
	 * @param userid
	 * @param fromdate
	 * @param todate
	 * @param events
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	private List<CommittedEvent> getDateCommittedEvents(Long userid, 
			Date fromdate, 
			Date todate,
			List<CommittedEvent> events) {
		LOGGER.debug("Entering getDateCommittedEvents()");

		List<SpreadRecordEvent> retValue = entityManager
				.createQuery(
						"SELECT sre FROM SpreadRecordEvent sre WHERE sre.userid = :userid AND sre.datecreated >= :fromdate AND sre.datecreated <= :todate")
				.setParameter("userid", userid).setParameter("fromdate", fromdate).setParameter("todate", todate)
				.getResultList();

		LOGGER.debug("SpreadRecordEvents: " + retValue);
		if (retValue != null && !retValue.isEmpty()) {
			final Iterator<SpreadRecordEvent> itr = retValue.iterator();
			while (itr.hasNext()) {
				final CommittedEvent committedEvent = populateSpreadEvent(itr.next());

				events.add(committedEvent);
			}
		}

		final List<TotalRecordEvent> totalRecords = entityManager
				.createQuery(
						"SELECT tre FROM TotalRecordEvent tre WHERE tre.userid = :userid AND tre.datecreated >= :fromdate AND tre.datecreated <= :todate")
				.setParameter("userid", userid).setParameter("fromdate", fromdate).setParameter("todate", todate)
				.getResultList();

		LOGGER.debug("TotalRecordEvent: " + totalRecords);
		if (totalRecords != null && !totalRecords.isEmpty()) {
			final Iterator<TotalRecordEvent> itr = totalRecords.iterator();
			while (itr.hasNext()) {
				final CommittedEvent committedEvent = populateTotalEvent(itr.next());
				events.add(committedEvent);
			}
		}

		final List<MlRecordEvent> mlRecordEvent = entityManager
				.createQuery(
						"SELECT mle FROM MlRecordEvent mle WHERE mle.userid = :userid AND mle.datecreated >= :fromdate AND mle.datecreated <= :todate")
				.setParameter("userid", userid).setParameter("fromdate", fromdate).setParameter("todate", todate)
				.getResultList();

		LOGGER.debug("MlRecordEvent: " + mlRecordEvent);
		if (mlRecordEvent != null && !mlRecordEvent.isEmpty()) {
			final Iterator<MlRecordEvent> itr = mlRecordEvent.iterator();
			while (itr.hasNext()) {
				final CommittedEvent committedEvent = populateMlEvent(itr.next());
				events.add(committedEvent);
			}
		}

		LOGGER.debug("Exiting getDateCommittedEvents()");
		return events;
	}

	/**
	 * 
	 * @param userid
	 * @param fromdate
	 * @param todate
	 * @param groupid
	 * @param events
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<CommittedEvent> getGroupCommittedEvents(Long userid, Date fromdate, Date todate, Long groupid,
			List<CommittedEvent> events) {
		LOGGER.debug("Entering getGroupCommittedEvents()");

		final List<SpreadRecordEvent> retValue = entityManager
				.createQuery(
						"SELECT sre FROM SpreadRecordEvent sre WHERE sre.userid = :userid AND sre.datecreated >= :fromdate AND sre.datecreated <= :todate AND sre.groupid = :groupid")
				.setParameter("userid", userid).setParameter("fromdate", fromdate).setParameter("todate", todate)
				.setParameter("groupid", groupid).getResultList();

		LOGGER.debug("SpreadRecordEvents: " + retValue);
		if (retValue != null && !retValue.isEmpty()) {
			final Iterator<SpreadRecordEvent> itr = retValue.iterator();
			while (itr.hasNext()) {
				final CommittedEvent committedEvent = populateSpreadEvent(itr.next());
				committedEvent.setAccountid(new Long(0));
				committedEvent.setGroupid(groupid);
				events.add(committedEvent);
			}
		}

		final List<TotalRecordEvent> totalRecords = entityManager
				.createQuery(
						"SELECT tre FROM TotalRecordEvent tre WHERE tre.userid = :userid AND tre.datecreated >= :fromdate AND tre.datecreated <= :todate AND tre.groupid = :groupid")
				.setParameter("userid", userid).setParameter("fromdate", fromdate).setParameter("todate", todate)
				.setParameter("groupid", groupid).getResultList();

		LOGGER.debug("TotalRecordEvent: " + totalRecords);
		if (totalRecords != null && !totalRecords.isEmpty()) {
			final Iterator<TotalRecordEvent> itr = totalRecords.iterator();
			while (itr.hasNext()) {
				final CommittedEvent committedEvent = populateTotalEvent(itr.next());
				committedEvent.setAccountid(new Long(0));
				committedEvent.setGroupid(groupid);
				events.add(committedEvent);
			}
		}

		final List<MlRecordEvent> mlRecords = entityManager
				.createQuery(
						"SELECT mre FROM MlRecordEvent mre WHERE mre.userid = :userid AND mre.datecreated >= :fromdate AND mre.datecreated <= :todate AND mre.groupid = :groupid")
				.setParameter("userid", userid).setParameter("fromdate", fromdate).setParameter("todate", todate)
				.setParameter("groupid", groupid).getResultList();

		LOGGER.debug("MlRecordEvent: " + mlRecords);
		if (mlRecords != null && !mlRecords.isEmpty()) {
			final Iterator<MlRecordEvent> itr = mlRecords.iterator();
			while (itr.hasNext()) {
				final CommittedEvent committedEvent = populateMlEvent(itr.next());
				committedEvent.setAccountid(new Long(0));
				committedEvent.setGroupid(groupid);
				events.add(committedEvent);
			}
		}

		LOGGER.debug("Exiting getGroupCommittedEvents()");
		return events;
	}

	/**
	 * 
	 * @param userid
	 * @param fromdate
	 * @param todate
	 * @param accountid
	 * @param events
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<CommittedEvent> getAccountCommittedEvents(Long userid, Date fromdate, Date todate, Long accountid,
			List<CommittedEvent> events) {
		LOGGER.debug("Entering getAccountCommittedEvents()");

		final List<AccountEvent> accountList = entityManager
				.createQuery(
						"SELECT ae FROM AccountEvent ae WHERE ae.userid = :userid AND ae.datecreated >= :fromdate AND ae.datecreated <= :todate AND ae.accountid = :accountid")
				.setParameter("userid", userid).setParameter("fromdate", fromdate).setParameter("todate", todate)
				.setParameter("accountid", accountid).getResultList();

		// Now get all SpreadEvents from the account list
		for (int x = 0; (accountList != null && x < accountList.size()); x++) {
			LOGGER.debug("AccountList size: " + accountList.size());
			final AccountEvent accountEvent = accountList.get(x);
			final Long sid = accountEvent.getSpreadid();
			LOGGER.debug("SpreadID: " + sid);

			final List<SpreadRecordEvent> retValue = entityManager
					.createQuery("SELECT sre FROM SpreadRecordEvent sre WHERE sre.userid = :userid AND sre.id = :id")
					.setParameter("userid", userid).setParameter("id", sid).getResultList();

			for (int z = 0; (retValue != null && z < retValue.size()); z++) {
				SpreadRecordEvent sre = retValue.get(z);
				final CommittedEvent committedEvent = populateCommittedSpreadEvent(sre);
				if (accountEvent.getIscompleted()) {
					committedEvent.setIscompleted(true);
				}
				final Set<AccountEvent> accountevents = new LinkedHashSet<AccountEvent>();
				accountevents.add(accountEvent);
				committedEvent.setAccountid(accountid);
				committedEvent.setGroupid(new Long(0));
				committedEvent.setAccountevents(accountevents);
				events.add(committedEvent);
			}

			final Long tid = accountEvent.getTotalid();
			LOGGER.debug("TotalID: " + tid);
			final List<TotalRecordEvent> totalRecords = entityManager
					.createQuery("SELECT tre FROM TotalRecordEvent tre WHERE tre.userid = :userid AND tre.id = :id")
					.setParameter("userid", userid).setParameter("id", tid).getResultList();

			for (int z = 0; (totalRecords != null && z < totalRecords.size()); z++) {
				TotalRecordEvent tre = totalRecords.get(z);
				final CommittedEvent committedEvent = populateCommittedTotalEvent(tre);
				if (accountEvent.getIscompleted()) {
					committedEvent.setIscompleted(true);
				}
				final Set<AccountEvent> accountevents = new LinkedHashSet<AccountEvent>();
				accountevents.add(accountEvent);
				committedEvent.setAccountid(accountid);
				committedEvent.setGroupid(new Long(0));
				committedEvent.setAccountevents(accountevents);
				events.add(committedEvent);
			}

			final Long mid = accountEvent.getMlid();
			LOGGER.debug("MoneylineID: " + mid);
			final List<MlRecordEvent> mlRecords = entityManager
					.createQuery("SELECT mre FROM MlRecordEvent mre WHERE mre.userid = :userid AND mre.id = :id")
					.setParameter("userid", userid).setParameter("id", mid).getResultList();

			for (int z = 0; (mlRecords != null && z < mlRecords.size()); z++) {
				MlRecordEvent mre = mlRecords.get(z);
				final CommittedEvent committedEvent = populateCommittedMlEvent(mre);
				if (accountEvent.getIscompleted()) {
					committedEvent.setIscompleted(true);
				}
				final Set<AccountEvent> accountevents = new LinkedHashSet<AccountEvent>();
				accountevents.add(accountEvent);
				committedEvent.setAccountid(accountid);
				committedEvent.setGroupid(new Long(0));
				committedEvent.setAccountevents(accountevents);
				events.add(committedEvent);
			}
		}

		LOGGER.debug("Exiting getAccountCommittedEvents()");
		return events;
	}

	/**
	 * 
	 * @param rowhead
	 */
	private void setupXlsHeader(HSSFRow rowhead) {
		LOGGER.debug("Entering setupXlsHeader()");

		// Row
		rowhead.createCell(3).setCellValue("Game Date");
		rowhead.createCell(0).setCellValue("Game Rotation #");
		rowhead.createCell(1).setCellValue("Game Description");
		rowhead.createCell(2).setCellValue("Account Name");
		rowhead.createCell(3).setCellValue("Date Accepted");
		rowhead.createCell(4).setCellValue("Type");
		rowhead.createCell(5).setCellValue("Status");
		rowhead.createCell(6).setCellValue("Attempted To Win");
		rowhead.createCell(7).setCellValue("Actual Risk");
		rowhead.createCell(8).setCellValue("Actual To Win");
		rowhead.createCell(9).setCellValue("Game Time");
		rowhead.createCell(10).setCellValue("Game Line");
		rowhead.createCell(11).setCellValue("Game Juice");
		rowhead.createCell(12).setCellValue("Owner %");
		rowhead.createCell(13).setCellValue("Partner %");
		rowhead.createCell(14).setCellValue("Message");
		rowhead.createCell(15).setCellValue("Ticket Number");

		LOGGER.debug("Exiting setupXlsHeader()");
	}

	/**
	 * 
	 * @param workbook
	 * @param rowhead
	 */
	private void setupXlsHeaderSuccess(HSSFWorkbook workbook, HSSFRow rowhead) {
		LOGGER.debug("Entering setupXlsHeaderSuccess()");

		final HSSFCellStyle style = workbook.createCellStyle();
		style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		// Row
		HSSFCell column0 = rowhead.createCell(0);
		column0.setCellStyle(style);
		column0.setCellValue("Game Date");

		HSSFCell column1 = rowhead.createCell(1);
		column1.setCellStyle(style);
		column1.setCellValue("Rotation #");
		
		HSSFCell column2 = rowhead.createCell(2);
		column2.setCellStyle(style);
		column2.setCellValue("Description");
		
		HSSFCell column3 = rowhead.createCell(3);
		column3.setCellStyle(style);
		column3.setCellValue("Account Name");
		
		HSSFCell column4 = rowhead.createCell(4);
		column4.setCellStyle(style);
		column4.setCellValue("Accepted Date");

		HSSFCell column5 = rowhead.createCell(5);
		column5.setCellStyle(style);
		column5.setCellValue("Type");
		
		HSSFCell column6 = rowhead.createCell(6);
		column6.setCellStyle(style);
		column6.setCellValue("Risk");
		
		HSSFCell column7 = rowhead.createCell(7);
		column7.setCellStyle(style);
		column7.setCellValue("Win");
		
		HSSFCell column8 = rowhead.createCell(8);
		column8.setCellStyle(style);
		column8.setCellValue("Owner % Risk");
		
		HSSFCell column9 = rowhead.createCell(9);
		column9.setCellStyle(style);
		column9.setCellValue("Owner % Win");
		
		HSSFCell column10 = rowhead.createCell(10);
		column10.setCellStyle(style);
		column10.setCellValue("Line");
		
		HSSFCell column11 = rowhead.createCell(11);
		column11.setCellStyle(style);
		column11.setCellValue("Juice");
		
		HSSFCell column12 = rowhead.createCell(12);
		column12.setCellStyle(style);
		column12.setCellValue("Owner %");
		
		HSSFCell column13 = rowhead.createCell(13);
		column13.setCellStyle(style);
		column13.setCellValue("Partner %");
		
		HSSFCell column14 = rowhead.createCell(14);
		column14.setCellStyle(style);
		column14.setCellValue("Ticket #");
		
		HSSFCell column15 = rowhead.createCell(15);
		column15.setCellStyle(style);
		column15.setCellValue("Outcome");
		
		HSSFCell column16 = rowhead.createCell(16);
		column16.setCellStyle(style);
		column16.setCellValue("Owner % Outcome");
		
		HSSFCell column17 = rowhead.createCell(17);
		column17.setCellStyle(style);
		column17.setCellValue("Source Name");
		
		HSSFCell column18 = rowhead.createCell(18);
		column18.setCellStyle(style);
		column18.setCellValue("Closing Line");
		
		HSSFCell column19 = rowhead.createCell(19);
		column19.setCellStyle(style);
		column19.setCellValue("Closing Juice");
		
		HSSFCell column20 = rowhead.createCell(20);
		column20.setCellStyle(style);
		column20.setCellValue("Delta");
		
		HSSFCell column21 = rowhead.createCell(21);
		column21.setCellStyle(style);
		column21.setCellValue("Bookmaker");

		LOGGER.debug("Exiting setupXlsHeaderSuccess()");
	}

	/**
	 * 
	 * @param row
	 * @param committedEvent
	 * @param ae
	 * @param ceCount
	 * @param timeZone
	 */
	private void setupXlsSuccessData(HSSFWorkbook workbook, HSSFSheet sheet, HSSFRow row, CommittedEvent committedEvent, AccountEvent ae, int ceCount, String timeZone, HSSFCellStyle style, HSSFCellStyle style2) {
		LOGGER.debug("Entering setupXlsSuccessData()");

		try {
			TimeZone zone = TimeZone.getTimeZone("America/New_York");
			DF.setTimeZone(zone);

			if ("ET".equals(timeZone)) {
				zone = TimeZone.getTimeZone("America/New_York");
				DF.setTimeZone(zone);
			} else if ("CT".equals(timeZone)) {
				zone = TimeZone.getTimeZone("America/Chicago");
				DF.setTimeZone(zone);
			} else if ("MT".equals(timeZone)) {
				zone = TimeZone.getTimeZone("America/Denver");
				DF.setTimeZone(zone);
			} else if ("PT".equals(timeZone)) {
				zone = TimeZone.getTimeZone("America/Los_Angeles");
				DF.setTimeZone(zone);
			}

			final HSSFCell column0 = row.createCell(0);
			column0.setCellStyle(style);
			column0.setCellType(CellType.STRING);
			column0.setCellValue(DF.format(ae.getEventdatetime()));
			
			final HSSFCell column1 = row.createCell(1);
			column1.setCellStyle(style);
			column1.setCellType(CellType.NUMERIC);
			column1.setCellValue(ae.getEventid());

			String sportName = getSportName(committedEvent.getSport());
			String rotationId = ae.getEventid().toString();
			String team = "";
			if (ae.getEventid() % 2 == 0) {
				team = committedEvent.getEventteam2();
			} else {
				team = committedEvent.getEventteam1();
			}
			String line = "";
			String juice = "";

			HSSFCell column2 = row.createCell(2);
			column2.setCellType(CellType.STRING);
			column2.setCellStyle(style);
			if (committedEvent.getEventtype().equals("spread")) {
				line = ae.getSpreadindicator() + ae.getSpread();
				juice = ae.getSpreadjuice().toString();
				final StringBuffer sb = new StringBuffer(100);
				sb.append(sportName).
				append(" #").
				append(rotationId).
				append(" ").
				append(team).
				append(" ").
				append(line).
				append(" ").
				append(juice);

				column2.setCellValue(sb.toString());
			} else if (committedEvent.getEventtype().equals("total")) {
				line = ae.getTotalindicator() + ae.getTotal();
				juice = ae.getTotaljuice().toString();
				final StringBuffer sb = new StringBuffer(100);
				sb.append(sportName).
				append(" #").
				append(rotationId).
				append(" ").
				append(team).
				append(" ").
				append(line).
				append(" ").
				append(juice);

				column2.setCellValue(sb.toString());
			} else if (committedEvent.getEventtype().equals("ml")) {
				juice = line = ae.getMljuice().toString();
				final StringBuffer sb = new StringBuffer(100);
				sb.append(sportName).
				append(" #").
				append(rotationId).
				append(" ").
				append(team).
				append(" ").
				append(line);

				column2.setCellValue(sb.toString());
			}

			HSSFCell column3 = row.createCell(3);
			column3.setCellStyle(style);
			column3.setCellType(CellType.STRING);
			column3.setCellValue(ae.getName());

			HSSFCell column4 = row.createCell(4);
			column4.setCellStyle(style);
			column4.setCellType(CellType.STRING);
			column4.setCellValue(DF.format(ae.getDatecreated()));

			HSSFCell column5 = row.createCell(5);
			column5.setCellStyle(style);
			column5.setCellType(CellType.STRING);
			column5.setCellValue(committedEvent.getEventtype().toUpperCase());

			HSSFCell column6 = row.createCell(6);
			column6.setCellStyle(style);
			column6.setCellType(CellType.NUMERIC);
			try {
				column6.setCellValue(Float.parseFloat(ae.getRiskamount()));
			} catch (Throwable t) {
				column6.setCellValue(new Float(0.00));
				LOGGER.error(t.getMessage(), t);
			}
			
			HSSFCell column7 = row.createCell(7);
			column7.setCellStyle(style);
			column7.setCellType(CellType.NUMERIC);
			try {
				column7.setCellValue(Float.parseFloat(ae.getTowinamount()));
			} catch (Throwable t) {
				column7.setCellValue(new Float(0.00));
				LOGGER.error(t.getMessage(), t);
			}

			final Integer ownerPercentage = ae.getOwnerpercentage();
			final Integer partnerPercentage = ae.getPartnerpercentage();

			HSSFCell column8 = row.createCell(8);
			column8.setCellStyle(style);
			column8.setCellType(CellType.NUMERIC);
			try {
				final DecimalFormat df = new DecimalFormat();
				df.setMaximumFractionDigits(2); 
				Float riskAmnt = Float.parseFloat(ae.getRiskamount());
				String eventAmount = df.format(riskAmnt * (ownerPercentage/100.0f));
				eventAmount = eventAmount.replace(",", "");

				column8.setCellValue(Float.parseFloat(eventAmount));
			} catch (Throwable t) {
				column8.setCellValue(new Float(0.00));
				LOGGER.error(t.getMessage(), t);
			}
			
			HSSFCell column9 = row.createCell(9);
			column9.setCellStyle(style);
			column9.setCellType(CellType.NUMERIC);
			try {
				final DecimalFormat df = new DecimalFormat();
				df.setMaximumFractionDigits(2); 
				Float winAmnt = Float.parseFloat(ae.getTowinamount());
				String eventAmount = df.format(winAmnt * (ownerPercentage/100.0f));
				eventAmount = eventAmount.replace(",", "");

				column9.setCellValue(Float.parseFloat(eventAmount));
			} catch (Throwable t) {
				column9.setCellValue(new Float(0.00));
				LOGGER.error(t.getMessage(), t);
			}

			// Setup Line and Juice
			HSSFCell column10 = row.createCell(10);
			column10.setCellStyle(style);
			column10.setCellType(CellType.STRING);
			column10.setCellValue(line.toString());

			HSSFCell column11 = row.createCell(11);
			column11.setCellStyle(style);
			column11.setCellType(CellType.STRING);
			column11.setCellValue(juice.toString());

			try {
				final HSSFCell column12 = row.createCell(12);
				column12.setCellStyle(style2);
	
				final DecimalFormat df = new DecimalFormat();
				df.setMaximumFractionDigits(2);
				final String ownerPer = df.format(ownerPercentage/100.0f);
				column12.setCellValue(Float.parseFloat(ownerPer));
			} catch (Throwable t) {
				LOGGER.error(t.getMessage(), t);
			}

			try {
				final HSSFCell column13 = row.createCell(13);
				column13.setCellStyle(style2);

				final DecimalFormat df = new DecimalFormat();
				df.setMaximumFractionDigits(2);
				final String partnerPer = df.format(partnerPercentage/100.0f);
				column13.setCellValue(Float.parseFloat(partnerPer));
			} catch (Throwable t) {
				LOGGER.error(t.getMessage(), t);
			}

			HSSFCell column14 = row.createCell(14);
			column14.setCellStyle(style);
			column14.setCellType(CellType.NUMERIC);
			try {
				column14.setCellValue(Integer.parseInt(ae.getAccountconfirmation()));
			} catch (Throwable t) {
				column14.setCellValue(ae.getAccountconfirmation());
			}

			HSSFCell column15 = row.createCell(15);
			column15.setCellStyle(style);
			column15.setCellType(CellType.NUMERIC);
			if (ae.getEventresultamount() != null ) {
				column15.setCellValue(ae.getEventresultamount());
			} else {
				column15.setCellValue("");
			}

			HSSFCell column16 = row.createCell(16);
			column16.setCellStyle(style);
			column16.setCellType(CellType.NUMERIC);
			if (ae.getEventresultamount() != null ) {
				try {
					final DecimalFormat df = new DecimalFormat();
					df.setMaximumFractionDigits(2);
					String eventAmount = df.format(ae.getEventresultamount() * (ownerPercentage/100.0f));
					eventAmount = eventAmount.replace(",", "");
	
					column16.setCellValue(Float.parseFloat(eventAmount));
				} catch (Throwable t) {
					column16.setCellValue(new Float(0.00));
					LOGGER.error(t.getMessage(), t);
				}
			} else {
				column16.setCellValue("");
			}

			HSSFCell column17 = row.createCell(17);
			column17.setCellStyle(style);
			column17.setCellType(CellType.STRING);
			column17.setCellValue(committedEvent.getScrappername());

			final AccountEventFinal aef = getAccountEventFinalByAccountEventId(ae.getId());
			if (aef != null) {
				String cLine = "";
				String cJuice = "";

				if (committedEvent.getEventtype().equals("spread")) {
					if (aef != null && aef.getSpreadnumber() != null) {
						cLine = aef.getSpreadindicator() + aef.getSpreadnumber().toString();
					}
					if (aef != null && aef.getSpreadjuicenumber() != null) {
						cJuice = aef.getSpreadjuiceindicator() + aef.getSpreadjuicenumber().toString();
					}
				} else if (committedEvent.getEventtype().equals("total")) {
					if (aef != null && aef.getTotalnumber() != null) {
						cLine = aef.getTotalindicator() + aef.getTotalnumber().toString();
					}
					if (aef != null && aef.getTotaljuicenumber() != null) {
						cJuice = aef.getTotaljuiceindicator() + aef.getTotaljuicenumber().toString();
					}
				} else if (committedEvent.getEventtype().equals("ml")) {
					if (aef != null && aef.getMlnumber() != null) {
						cJuice = cLine = aef.getMlindicator() + aef.getMlnumber().toString();
					}
				}

				HSSFCell column18 = row.createCell(18);
				column18.setCellStyle(style);
				column18.setCellType(CellType.STRING);
				column18.setCellValue(cLine);

				HSSFCell column19 = row.createCell(19);
				column19.setCellStyle(style);
				column19.setCellType(CellType.STRING);
				column19.setCellValue(cJuice);

				// TODO
				HSSFCell column20 = row.createCell(20);
				column20.setCellStyle(style);
				column20.setCellType(CellType.STRING);
				column20.setCellValue("");

				HSSFCell column21 = row.createCell(21);
				column21.setCellStyle(style);
				column21.setCellType(CellType.STRING);
				column21.setCellValue("Pinnacle");			
			}
		} catch (Throwable t) {
			LOGGER.error("Throwable exception with getting XLS data", t);
		}

		LOGGER.debug("Exiting setupXlsSuccessData()");
	}

	/**
	 * 
	 * @param sport
	 * @return
	 */
	private String getSportName(String sport) {
		LOGGER.debug("Entering getSportName()");
		String sportName = null;

		if (sport.contains("nfl")) {
			sportName = "NFL";
		} else if (sport.contains("ncaaf")) {
			sportName = "NCAAF";
		} else if (sport.contains("wnba")) {
			sportName = "WNBA";
		} else if (sport.contains("nba")) {
			sportName = "NBA";
		} else if (sport.contains("ncaab")) {
			sportName = "NCAAB";
		} else if (sport.contains("nhl")) {
			sportName = "NHL";
		} else if (sport.contains("mlb")) {
			sportName = "MLB";
		}

		LOGGER.debug("Exiting getSportName()");
		return sportName;
	}

	/**
	 * 
	 * @param row
	 * @param committedEvent
	 * @param ae
	 * @param ceCount
	 * @param timeZone
	 */
	private void setupXlsData(HSSFRow row, CommittedEvent committedEvent, AccountEvent ae, int ceCount,
			String timeZone) {
		LOGGER.debug("Entering setupXlsData()");

		try {
			row.createCell(0).setCellValue(ceCount);
			row.createCell(1).setCellValue(committedEvent.getEventname());
			row.createCell(2).setCellValue(ae.getName());
			if ("ET".equals(timeZone)) {
				TimeZone zone = TimeZone.getTimeZone("America/New_York");
				DF.setTimeZone(zone);
			} else if ("CT".equals(timeZone)) {
				TimeZone zone = TimeZone.getTimeZone("America/Chicago");
				DF.setTimeZone(zone);
			} else if ("MT".equals(timeZone)) {
				TimeZone zone = TimeZone.getTimeZone("America/Denver");
				DF.setTimeZone(zone);
			} else if ("PT".equals(timeZone)) {
				TimeZone zone = TimeZone.getTimeZone("America/Los_Angeles");
				DF.setTimeZone(zone);
			}
			row.createCell(3).setCellValue(DF.format(ae.getDatecreated()));
			row.createCell(4).setCellValue(committedEvent.getEventtype());
			row.createCell(5).setCellValue(ae.getIscompleted() ? "Success" : "Fail");

			if (committedEvent.getAmount() != null && committedEvent.getAmount().length() > 0) {
				row.createCell(6).setCellValue("$" + committedEvent.getAmount());
			}

			String gameLine = "";
			String gameJuice = "";
			Float juiceValue = null;
			if (ae.getSpreadindicator() != null && ae.getSpread() != null && ae.getSpreadjuice() != null) {
				gameLine = ae.getSpreadindicator() + ae.getSpread();
				gameJuice = ae.getSpreadjuice().toString();
				juiceValue = ae.getSpreadjuice();
			} else if (ae.getTotalindicator() != null && ae.getTotal() != null && ae.getTotaljuice() != null) {
				gameLine = ae.getTotalindicator() + ae.getTotal();
				gameJuice = ae.getTotaljuice().toString();
				juiceValue = ae.getTotaljuice();
			} else if (ae.getMlindicator() != null && ae.getMl() != null && ae.getMljuice() != null) {
				gameLine = ae.getMlindicator() + ae.getMl();
				gameJuice = ae.getMljuice().toString();
				juiceValue = ae.getMljuice();
			}
			if (gameJuice.length() > 0 && !gameJuice.contains("-")) {
				gameJuice = "+" + gameJuice;
			}

			// Setup Risk and Win
			row.createCell(7).setCellValue("$" + ae.getRiskamount()); // Risk
			row.createCell(8).setCellValue("$" + ae.getTowinamount()); // To Win

			// Event Date
			row.createCell(9).setCellValue(DF.format(committedEvent.getEventdatetime()));
			row.createCell(10).setCellValue(gameLine);
			row.createCell(11).setCellValue(gameJuice);

			if (ae.getActualamount() != null) {
				Double amount = new Double(ae.getActualamount());
				if (ae.getOwnerpercentage() != null) {
					Double opercent = new Double(ae.getOwnerpercentage() / 100);
					Double oamount = BigDecimal.valueOf(opercent * amount).setScale(3, RoundingMode.HALF_UP)
							.doubleValue();
					row.createCell(12).setCellValue("$" + oamount);
				} else {
					row.createCell(12).setCellValue("NA");
				}
				if (ae.getPartnerpercentage() != null) {
					Double ppercent = new Double(ae.getPartnerpercentage() / 100);
					Double pamount = BigDecimal.valueOf(ppercent * amount).setScale(3, RoundingMode.HALF_UP)
							.doubleValue();

					row.createCell(13).setCellValue("$" + pamount);
				} else {
					row.createCell(13).setCellValue("NA");
				}
			} else {
				row.createCell(12).setCellValue("NA");
				row.createCell(13).setCellValue("NA");
			}

			String message = " ";
			if (ae.getErrorexception() != null && ae.getErrorexception().length() > 0) {
				message = ae.getErrormessage() + "; " + ae.getErrorexception();
			}
			row.createCell(14).setCellValue(message);
			row.createCell(15).setCellValue(ae.getAccountconfirmation());
		} catch (Throwable t) {
			LOGGER.error("Throwable exception with getting XLS data", t);
		}

		LOGGER.debug("Exiting setupXlsData()");
	}

	/**
	 * 
	 * @param rowhead
	 */
	private void setupAccountXlsHeader(HSSFRow rowhead) {
		LOGGER.debug("Entering setupAccountXlsHeader()");

		rowhead.createCell(0).setCellValue("Date Accepted");
		rowhead.createCell(1).setCellValue("Type");
		rowhead.createCell(2).setCellValue("Status");
		rowhead.createCell(3).setCellValue("Game Description");
		rowhead.createCell(4).setCellValue("Game Wager");
		rowhead.createCell(5).setCellValue("Game Time");
		rowhead.createCell(6).setCellValue("Game Transaction");
		rowhead.createCell(7).setCellValue("Owner %");
		rowhead.createCell(8).setCellValue("Partner %");
		rowhead.createCell(9).setCellValue("Ticket Number");
		rowhead.createCell(10).setCellValue("Actual Bet Amount");

		LOGGER.debug("Exiting setupAccountXlsHeader()");
	}

	/**
	 * 
	 * @param row
	 * @param committedEvent
	 * @param ae
	 */
	private void setupAccountXlsData(HSSFRow row, CommittedEvent committedEvent, AccountEvent ae) {
		LOGGER.debug("Entering setupAccountXlsData()");

		row.createCell(0).setCellValue(committedEvent.getDatecreated());
		row.createCell(1).setCellValue(committedEvent.getEventtype());
		row.createCell(2).setCellValue(committedEvent.getIscompleted() ? "Success" : "Fail");
		row.createCell(3).setCellValue(committedEvent.getEventname());
		row.createCell(4).setCellValue(committedEvent.getAmount());
		row.createCell(5).setCellValue(committedEvent.getEventdatetime());
		String gameTransaction = " ";
		if (ae.getErrorexception() != null && ae.getErrorexception().length() > 0) {
			gameTransaction = ae.getErrorexception();
		} else {
			if (ae.getSpreadindicator() != null && ae.getSpread() != null && ae.getSpreadjuice() != null) {
				gameTransaction = ae.getSpreadindicator() + ae.getSpread() + ' ' + ae.getSpreadjuice();
			} else if (ae.getTotalindicator() != null && ae.getTotal() != null && ae.getTotaljuice() != null) {
				gameTransaction = ae.getTotalindicator() + ae.getTotal() + ' ' + ae.getTotaljuice();
			} else if (ae.getMlindicator() != null && ae.getMl() != null && ae.getMljuice() != null) {
				gameTransaction = ae.getMlindicator() + ae.getMl() + ' ' + ae.getMljuice();
			}
		}
		row.createCell(6).setCellValue(gameTransaction);
		row.createCell(7).setCellValue("%" + ae.getOwnerpercentage());
		row.createCell(8).setCellValue("%" + ae.getPartnerpercentage());
		String information = " ";
		if (ae.getErrormessage() != null) {
			information = ae.getErrormessage();
		} else {
			if (ae.getAccountconfirmation() != null) {
				information = ae.getAccountconfirmation();
			}
		}
		row.createCell(9).setCellValue(information);
		row.createCell(10).setCellValue(ae.getActualamount());

		LOGGER.debug("Exiting setupAccountXlsData()");
	}

	/**
	 * 
	 * @param value
	 * @param places
	 * @return
	 */
	private static double round(double value, int places) {
		LOGGER.info("Entering round()");
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);

		LOGGER.info("Exiting round()");
		return bd.doubleValue();
	}

	/**
	 * 
	 * @param wagerType
	 * @param juiceValue
	 * @param ae
	 * @return
	 */
	private void determineAmounts(Float juiceValue, AccountEvent ae) {
		LOGGER.info("Entering determineAmounts()");
		String wagerType = ae.getWagertype();

		if (wagerType != null && "1".equals(wagerType) && juiceValue != null) { // RISK
			float factor = 1;
			if (juiceValue.floatValue() < 0) {
				factor = 100 / juiceValue.floatValue();
			} else {
				factor = juiceValue.floatValue() / 100;
			}
			if (ae.getActualamount() != null && ae.getIscompleted()) {
				Double riskAmount = Double.parseDouble(ae.getActualamount());
				Double theAmount = riskAmount.doubleValue() * factor;
				// Now round the amount to 2 digit precision
				double amountApplied = round(theAmount.doubleValue(), 2);
				if (amountApplied < 0) {
					amountApplied = Math.abs(amountApplied);
				}
				Double finalAmount = new Double(amountApplied);
				ae.setRiskamount(ae.getActualamount());
				ae.setTowinamount(finalAmount.toString());
			} else {
				ae.setRiskamount("0");
				ae.setTowinamount("0");
			}
		} else if (wagerType != null && "2".equals(wagerType) && juiceValue != null) { // TO
																						// WIN
			float factor = 1;
			if (juiceValue.floatValue() < 0) {
				factor = 100 / juiceValue.floatValue();
			} else {
				factor = juiceValue.floatValue() / 100;
			}

			if (ae.getActualamount() != null && ae.getIscompleted()) {
				Double winAmount = Double.parseDouble(ae.getActualamount());
				Double theAmount = winAmount.doubleValue() / factor;
				// Now round the amount to 2 digit precision
				double amountApplied = round(theAmount.doubleValue(), 2);
				if (amountApplied < 0) {
					amountApplied = Math.abs(amountApplied);
				}
				Double finalAmount = new Double(amountApplied);
				ae.setRiskamount(finalAmount.toString()); // Risk
				ae.setTowinamount(ae.getActualamount()); // To Win
			} else {
				ae.setRiskamount("0");
				ae.setTowinamount("0");
			}
		} else {
			ae.setRiskamount("0");
			ae.setTowinamount("0");
		}

		LOGGER.info("Exiting determineAmounts()");
	}

	/*
	 * 
	 */
	@Override
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ticketadvantage.services.dao.EventsDAO#getAllEventsNoCache()
	 */
	@Override
	public Set<EventPackage> getAllEventsNoCache() throws AppException {
		LOGGER.info("Entering getAllEventsNoCache()");
		Set<EventPackage> ep = null;

		AllGames = ep = sportsInsightSite.getAllEventsNoCache();

		LOGGER.info("Exiting getAllEventsNoCache()");
		return ep;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ticketadvantage.services.dao.EventsDAO#getScores(java.lang.String,
	 * java.lang.Integer, com.ticketadvantage.services.model.EventPackage)
	 */
	@Override
	public EventPackage getScores(String linetype, Integer sportsInsightsEventId, EventPackage ep)
			throws BatchException {
		LOGGER.info("Entering getScores()");

		// Get the scores
		ep = sportsInsightSite.getScores(linetype, sportsInsightsEventId, ep);

		LOGGER.info("Exiting getScores()");
		return ep;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ticketadvantage.services.dao.EventsDAO#getAllEvents()
	 */
	@Override
	public ClosingLine getClosingLine(Integer sportsInsightsEventId, Integer lineType, Integer gameType,
			Integer sportId) throws BatchException {
		LOGGER.info("Entering getClosingLine()");
		ClosingLine cl = null;

		// Check for stub turned on or not
		try {
			cl = sportsInsightSite.getClosingLine(sportsInsightsEventId, lineType, gameType, sportId);
		} catch (BatchException e) {
			throw new BatchException("Batch Exception getting closing lines");
		}

		LOGGER.info("Exiting getClosingLine()");
		return cl;
	}

	/**
	 * 
	 * @param id
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	protected AccountEventFinal getAccountEventFinalByAccountEventId(Long id) throws AppException {
		LOGGER.info("Entering getAccountEventFinalByAccountEventId()");
		LOGGER.debug("id: " + id);
		AccountEventFinal retValue = null;

		try {
			final List<AccountEventFinal> aefList = 
					entityManager.createQuery("SELECT aev FROM AccountEventFinal aev WHERE aev.accounteventid = :accounteventid").setParameter("accounteventid", id).getResultList();

			if (aefList != null && aefList.size() > 0) {
				retValue = aefList.get(0);
			}
		} catch (Exception e) {
			LOGGER.info(e.getMessage(), e);
		}

		LOGGER.info("Exiting getAccountEventFinalByAccountEventId()");
		return retValue;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.EventsDAO#getSport(java.lang.String)
	 */
	@Override
	public EventsPackage getSport(String sportType) throws BatchException {
		LOGGER.info("Entering getSport()");
		EventsPackage eventsPackage = null;

		// Check for stub turned on or not
		try {
			eventsPackage = sportsInsightSite.getSport(sportType);
		} catch (BatchException e) {
			throw new BatchException("Batch Exception getting EventsPackage");
		}

		LOGGER.info("Exiting getSport()");
		return eventsPackage;
	}
}