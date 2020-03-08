package com.ticketadvantage.services.dao;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ticketadvantage.services.errorhandling.AppErrorCodes;
import com.ticketadvantage.services.errorhandling.AppErrorMessage;
import com.ticketadvantage.services.errorhandling.AppException;
import com.ticketadvantage.services.model.AccountEventFinal;
import com.ticketadvantage.services.model.User;


@Repository
public class AccountEventFinalDAOImpl implements AccountEventFinalDAO {

	private static final Logger LOGGER = Logger.getLogger(AccountEventFinalDAOImpl.class);

	@PersistenceContext(name = "entityManager")
	private EntityManager entityManager;
	
	public AccountEventFinalDAOImpl() {
		super();
		LOGGER.debug("Entering AccountEventFinalDAOImpl()");
		LOGGER.debug("Exiting AccountEventFinalDAOImpl()");
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.UserBillingDAO#persist(com.ticketadvantage.services.model.UserBilling)
	 */
	@Override
	@Transactional(readOnly = false)
	@SuppressWarnings("unchecked")
	public AccountEventFinal persist(AccountEventFinal accountEventFinal) throws AppException {
		LOGGER.info("Entering persist()");
		LOGGER.debug("AccountEventFinal: " + accountEventFinal);
		accountEventFinal.setId(null);

		// Check if user billing info already exists for the given week
		final List<AccountEventFinal> listAccountEventFinals = (List<AccountEventFinal>) entityManager
				.createQuery("SELECT a FROM AccountEventFinal a WHERE a.accounteventid = :accounteventid")
				.setParameter("accounteventid", accountEventFinal.getAccounteventid()).getResultList();
		if ((listAccountEventFinals == null) || (listAccountEventFinals != null && listAccountEventFinals.size() == 0)) {
			// Write out the account to the account table
			entityManager.persist(accountEventFinal);
		}

		LOGGER.info("Exiting persist()");
		return accountEventFinal;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.UserBillingDAO#update(com.ticketadvantage.services.model.UserBilling)
	 */
	@Override
	@Transactional(readOnly = false)
	public AccountEventFinal update(AccountEventFinal accountEventFinal) throws AppException {
		LOGGER.info("Entering update()");
		LOGGER.debug("AccountEventFinal: " + accountEventFinal);
		AccountEventFinal retValue = null;

		AccountEventFinal tempAccountEventFinal = entityManager.find(AccountEventFinal.class, accountEventFinal.getId());
		if (tempAccountEventFinal != null) {
			tempAccountEventFinal.setAccounteventid(accountEventFinal.getAccounteventid());
			tempAccountEventFinal.setOutcomewin(accountEventFinal.getOutcomewin());
			tempAccountEventFinal.setRotation1(accountEventFinal.getRotation1());
			tempAccountEventFinal.setRotation2(accountEventFinal.getRotation2());
			tempAccountEventFinal.setRotation1team(accountEventFinal.getRotation1team());
			tempAccountEventFinal.setRotation2team(accountEventFinal.getRotation2team());
			tempAccountEventFinal.setRotation1score(accountEventFinal.getRotation1score());
			tempAccountEventFinal.setRotation2score(accountEventFinal.getRotation2score());
			
			tempAccountEventFinal.setSpreadindicator(accountEventFinal.getSpreadindicator());
			tempAccountEventFinal.setSpreadnumber(accountEventFinal.getSpreadnumber());
			
			tempAccountEventFinal.setSpreadjuiceindicator(accountEventFinal.getSpreadjuiceindicator());
			tempAccountEventFinal.setSpreadjuicenumber(accountEventFinal.getSpreadjuicenumber());
			
			tempAccountEventFinal.setTotalindicator(accountEventFinal.getTotalindicator());
			tempAccountEventFinal.setTotalnumber(accountEventFinal.getTotalnumber());
			
			tempAccountEventFinal.setTotaljuiceindicator(accountEventFinal.getTotaljuiceindicator());
			tempAccountEventFinal.setTotaljuicenumber(accountEventFinal.getTotaljuicenumber());		
			
			retValue = entityManager.merge(tempAccountEventFinal);
		} else {
			throw new AppException(500, AppErrorCodes.ACCOUNT_EVENT_FINAL_NOT_FOUND,  
					AppErrorMessage.ACCOUNT_EVENT_FINAL_NOT_FOUND);
		}
		LOGGER.info("Exiting update()");
		return retValue;
	}


	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.UserBillingDAO#delete(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = false)
	public void delete(Long id) throws AppException {
		LOGGER.info("Entering delete()");
		
		final AccountEventFinal accountEventFinal = entityManager.find(AccountEventFinal.class, id);
		if (accountEventFinal != null) {
			// Remove the account
			entityManager.remove(accountEventFinal);
		} else {
			throw new AppException(500, AppErrorCodes.ACCOUNT_EVENT_FINAL_NOT_FOUND,  
				AppErrorMessage.ACCOUNT_EVENT_FINAL_NOT_FOUND);
		}
		
		LOGGER.info("Exiting delete()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.AccountEventFinalDAO#getAccountEventFinal(java.lang.Long)
	 */
	@Override
	public AccountEventFinal getAccountEventFinal(Long id) throws AppException {
		LOGGER.info("Entering getAcountEventFinal()");
		LOGGER.debug("id: " + id);
		AccountEventFinal retValue = null;

		try {
			retValue = entityManager.find(AccountEventFinal.class, id);
			LOGGER.debug("AccountEventFinal: " + retValue);
			if (retValue == null) {
				// Should have one by this account id
				throw new AppException(500, AppErrorCodes.ACCOUNT_EVENT_FINAL_NOT_FOUND,  
						AppErrorMessage.ACCOUNT_EVENT_FINAL_NOT_FOUND);
			}
		} catch (NoResultException e) {
			LOGGER.info("NoResultException id: " + id);
			throw new AppException(500, AppErrorCodes.ACCOUNT_EVENT_FINAL_NOT_FOUND,  
					AppErrorMessage.ACCOUNT_EVENT_FINAL_NOT_FOUND);
		}

		LOGGER.info("Exiting getAccountEventFinal()");
		return retValue;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.AccountEventFinalDAO#getAccountEventFinalByAccountEventId(java.lang.Long)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public AccountEventFinal getAccountEventFinalByAccountEventId(Long id) throws AppException {
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
	 * @see com.ticketadvantage.services.dao.AccountEventFinalDAO#findAll()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Set<AccountEventFinal> findAll() throws AppException {
		LOGGER.info("Entering findAll()");

		final List<AccountEventFinal> retValue = entityManager.createQuery("SELECT aev FROM AccountEventFinal aev").getResultList();
		final Set<AccountEventFinal> accountEventFinalSet = new LinkedHashSet<AccountEventFinal>(retValue);

		LOGGER.info("Exiting findAll()");
		return accountEventFinalSet;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.AccountDAO#setEntityManager(javax.persistence.EntityManager)
	 */
	@Override
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.AccountEventFinalDAO#getUserAccountEventFinals(java.lang.Long, java.util.Date)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<AccountEventFinal> getUserAccountEventFinals(Long userid, Date weekStartDate) throws AppException {
		List<AccountEventFinal> accountEventFinals = null;
		User tempUser = entityManager.find(User.class, userid);
		if (tempUser != null) {
			if (weekStartDate == null) {
				// --- TODO implement
				//accountEventFinals = tempUser.getAccountEventFinals();
			} else {
				accountEventFinals = entityManager.createQuery("SELECT aev FROM AccountEventFinal aev " 
						+ " JOIN AccountEvent ae on ae.id = aev.accounteventid "
						+ " JOIN User u on u.id = ae.userid "
						+ " WHERE u.userid = :userid AND ae.weekstartdate = :week_start_date")
						.setParameter("userid", userid)
						.setParameter("week_start_date", weekStartDate)
						.getResultList();
			}
		}
		return accountEventFinals;
	}

}