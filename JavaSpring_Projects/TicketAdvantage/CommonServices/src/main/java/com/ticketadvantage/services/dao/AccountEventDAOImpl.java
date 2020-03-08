package com.ticketadvantage.services.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.ticketadvantage.services.errorhandling.AppException;
import com.ticketadvantage.services.model.AccountEvent;

/**
 * 
 * @author jmiller
 *
 */
@Repository
public class AccountEventDAOImpl implements AccountEventDAO {
	private static final Logger LOGGER = Logger.getLogger(AccountEventDAOImpl.class);

	@PersistenceContext(name = "entityManager")
	private EntityManager entityManager;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * 
	 */
	public AccountEventDAOImpl() {
		super();
		LOGGER.debug("Entering AccountEventDAOImpl()");
		LOGGER.debug("Exiting AccountEventDAOImpl()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.AccountEventDAO#getAccountEvents(java.lang.Long, java.util.Date, java.util.Date)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<AccountEvent> getAccountEvents(Long userid, Date startDate, Date endDate) throws AppException {
		LOGGER.info("Entering getAccoutnEvents()");
		List<AccountEvent> accountEvents = new ArrayList<AccountEvent>();

		try {
			accountEvents = entityManager.createQuery("SELECT ae FROM AccountEvent ae WHERE ae.userid = :userid").setParameter("userid", userid).getResultList();
			LOGGER.debug("AccountEvents: " + accountEvents);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		LOGGER.info("Exiting getAccoutnEvents()");
		return accountEvents;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.AccountEventDAO#getAccountEventsAmount(java.lang.Long, java.util.Date, java.util.Date)
	 */
	@Override
	public BigDecimal getAccountEventsAmount(Long userid, Date startDate, Date endDate) throws AppException {
		LOGGER.info("Entering getAccountEventsAmount()");
		BigDecimal accountEventAmount = new BigDecimal(0);

		try {
			Object result = (entityManager.createQuery("SELECT sum(case when eventresult = 'win' then eventresultamount " +
	           		 "when eventresult = 'loss' then (-1 * eventresultamount) " +
	           		 "end) FROM AccountEvent ae WHERE ae.userid = :userid AND ae.eventdatetime > :start_date AND ae.eventdatetime <= :end_date"))
						.setParameter("userid", userid)
						.setParameter("start_date", startDate)
						.setParameter("end_date", endDate).getSingleResult();
			if (result == null) {
				accountEventAmount = new BigDecimal(0);
			} else {
				accountEventAmount = new BigDecimal((String)result.toString());
			}
			
			LOGGER.debug("AccountEventsAmount: " + accountEventAmount);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		LOGGER.info("Exiting getAccountEventsAmount()");
		return accountEventAmount;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.AccountEventDAO#getAccountEventsCount(java.lang.Long, java.util.Date, java.util.Date)
	 */
	@Override
	public Long getAccountEventsCount(Long userid, Date startDate, Date endDate) throws AppException {
		LOGGER.info("Entering getAccountEventsCount()");
		Long accountEventCount = new Long(0);

		try {
			accountEventCount = Long.parseLong(entityManager.createQuery("SELECT count(ae) FROM AccountEvent ae WHERE ae.userid = :userid AND ae.eventdatetime > :start_date AND ae.eventdatetime <= :end_date")
					.setParameter("userid", userid)
					.setParameter("start_date", startDate)
					.setParameter("end_date", endDate).getSingleResult().toString());
			LOGGER.debug("AccountEventsCount: " + accountEventCount);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		LOGGER.info("Exiting getAccountEventsCount()");
		return accountEventCount;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.AccountEventDAO#getOpenAccountEventsForWeek()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<AccountEvent> getOpenAccountEventsForWeek() throws AppException {
		LOGGER.info("Entering getOpenAccountEventsForWeek()");
		List<AccountEvent> accountEvents = null;

		try {
			final TimeZone timeZone = TimeZone.getTimeZone("America/Los_Angeles");
			final Calendar weekDate = Calendar.getInstance(timeZone);
			weekDate.set(Calendar.HOUR_OF_DAY, 0);
			weekDate.set(Calendar.MINUTE, 0);
			weekDate.set(Calendar.SECOND, 0);
			weekDate.set(Calendar.MILLISECOND, 0);
			weekDate.add(Calendar.DAY_OF_MONTH, -7);
			final Date sevenDaysAgo = weekDate.getTime();
 
			accountEvents = entityManager.createQuery("SELECT ae FROM AccountEvent ae WHERE ae.datecreated > :datecreated AND status='Complete' AND (eventresult IS NULL OR eventresult='')").setParameter("datecreated", sevenDaysAgo).getResultList();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		LOGGER.info("Exiting getOpenAccountEventsForWeek()");
		return accountEvents;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.AccountEventDAO#getErrorAccountEventsForWeek()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<AccountEvent> getErrorAccountEventsForWeek() throws AppException {
		LOGGER.info("Entering getErrorAccountEventsForWeek()");
		List<AccountEvent> accountEvents = null;

		try {
			final Calendar weekDate = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
			weekDate.set(Calendar.HOUR_OF_DAY, 0);
			weekDate.set(Calendar.MINUTE, 0);
			weekDate.set(Calendar.SECOND, 0);
			weekDate.set(Calendar.MILLISECOND, 0);
			weekDate.add(Calendar.DAY_OF_MONTH, -7);
			final Date sevenDaysAgo = weekDate.getTime();

			final Calendar now = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
			now.set(Calendar.HOUR_OF_DAY, 0);
			now.set(Calendar.MINUTE, 0);
			now.set(Calendar.SECOND, 0);
			now.set(Calendar.MILLISECOND, 0);
			final Date nowDate  = now.getTime();

			accountEvents = entityManager.createQuery("SELECT ae FROM AccountEvent ae WHERE ae.datecreated > :datecreated AND ae.eventdatetime > :eventdatetime AND ae.status != 'Complete' AND ae.attempts = 11").setParameter("datecreated", sevenDaysAgo).setParameter("eventdatetime", nowDate).getResultList();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		LOGGER.info("Exiting getErrorAccountEventsForWeek()");
		return accountEvents;
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
