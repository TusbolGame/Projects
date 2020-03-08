package com.ticketadvantage.services.dao;

import java.util.Date;
import java.util.Iterator;
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
import com.ticketadvantage.services.model.Accounts;
import com.ticketadvantage.services.model.Groups;
import com.ticketadvantage.services.model.User;
import com.ticketadvantage.services.model.UserBilling;

@Repository
public class UserBillingDAOImpl implements UserBillingDAO {

	private static final Logger LOGGER = Logger.getLogger(UserBillingDAOImpl.class);

	@PersistenceContext(name = "entityManager")
	private EntityManager entityManager;
	
	public UserBillingDAOImpl() {
		super();
		LOGGER.debug("Entering UserBillingDAOImpl()");
		LOGGER.debug("Exiting UserBillingDAOImpl()");
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.UserBillingDAO#persist(com.ticketadvantage.services.model.UserBilling)
	 */
	@Override
	@Transactional(readOnly = false)
	public UserBilling persist(UserBilling userBilling) throws AppException {
		LOGGER.info("Entering persist()");
		LOGGER.debug("UserBilling: " + userBilling);
		userBilling.setId(null);

		// Check if user billing info already exists for the given week
		final List<UserBilling> listUserBillings = (List<UserBilling>) entityManager
				.createQuery("SELECT a FROM UserBilling a WHERE a.userid = :userid AND a.weekstartdate = :weekstartdate")
				.setParameter("userid", userBilling.getUserid())
				.setParameter("weekstartdate", userBilling.getWeekstartdate()).getResultList();
		if ((listUserBillings == null) || (listUserBillings != null && listUserBillings.size() == 0)) {
			// Write out the account to the account table
			entityManager.persist(userBilling);
		} else {
			// We already have one, send necessary error message
			throw new AppException(500, AppErrorCodes.DUPLICATE_USER_BILLING,  
					AppErrorMessage.DUPLICATE_USER_BILLING);
		}
		LOGGER.info("Exiting persist()");
		return userBilling;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.UserBillingDAO#update(com.ticketadvantage.services.model.UserBilling)
	 */
	@Override
	@Transactional(readOnly = false)
	public UserBilling update(UserBilling userBilling) throws AppException {
		LOGGER.info("Entering update()");
		LOGGER.debug("UserBilling: " + userBilling);
		UserBilling retValue = null;

		UserBilling tempUserBilling = entityManager.find(UserBilling.class, userBilling.getId());
		if (tempUserBilling != null) {
			tempUserBilling.setAccountrate(userBilling.getAccountrate());
			tempUserBilling.setIspaid(userBilling.getIspaid());
			tempUserBilling.setNumberofaccounts(userBilling.getNumberofaccounts());
			tempUserBilling.setUserid(userBilling.getUserid());
			tempUserBilling.setWeeklybalance(userBilling.getWeeklybalance());
			tempUserBilling.setWeekstartdate(userBilling.getWeekstartdate());			// Write out the account to the account table
			
			retValue = entityManager.merge(tempUserBilling);
		} else {
			throw new AppException(500, AppErrorCodes.USER_BILLING_NOT_FOUND,  
					AppErrorMessage.USER_BILLING_NOT_FOUND);
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
		
		final UserBilling userBilling = entityManager.find(UserBilling.class, id);
		if (userBilling != null) {
			// Remove the account
			entityManager.remove(userBilling);
		} else {
			throw new AppException(500, AppErrorCodes.USER_BILLING_NOT_FOUND,  
				AppErrorMessage.USER_BILLING_NOT_FOUND);
		}
		
		LOGGER.info("Exiting delete()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.AccountDAO#getUserBilling(java.lang.Long)
	 */
	@Override
	public UserBilling getUserBilling(Long id) throws AppException {
		LOGGER.info("Entering getUserBilling()");
		LOGGER.debug("id: " + id);
		UserBilling retValue = null;
		try {
			retValue = entityManager.find(UserBilling.class, id);
			LOGGER.debug("UserBilling: " + retValue);
			if (retValue == null) {
				// Should have one by this account id
				throw new AppException(500, AppErrorCodes.USER_BILLING_NOT_FOUND,  
						AppErrorMessage.USER_BILLING_NOT_FOUND);
			}
		} catch (NoResultException e) {
			LOGGER.info("NoResultException id: " + id);
			throw new AppException(500, AppErrorCodes.USER_BILLING_NOT_FOUND,  
					AppErrorMessage.USER_BILLING_NOT_FOUND);
		}
		LOGGER.info("Exiting getUserBilling()");
		return retValue;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.UserBillingDAO#findAll()
	 */
	@Override
	public Set<UserBilling> findAll() throws AppException {
		LOGGER.info("Entering findAll()");
		final List<UserBilling> retValue = entityManager.createQuery("SELECT ub FROM UserBilling ub").getResultList();
		final Set<UserBilling> userBillingSet = new LinkedHashSet<UserBilling>(retValue);
		LOGGER.info("Exiting findAll()");
		return userBillingSet;
	}

	@Override
	public List<UserBilling> getUserBillingsForUser(Long userid, Date weekStartDate) throws AppException {
		List<UserBilling> userBillings = null;
		User tempUser = entityManager.find(User.class, userid);
		if (tempUser != null) {
			if (weekStartDate == null) {
				
//				userBillings = tempUser.getUserBillings();
			} else {
				userBillings = entityManager.createQuery("SELECT ub FROM UserBilling ub WHERE ub.userid = :userid AND ub.weekstartdate = :week_start_date")
						.setParameter("userid", userid)
						.setParameter("week_start_date", weekStartDate)
						.getResultList();
			}
		}
		return userBillings;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.AccountDAO#setEntityManager(javax.persistence.EntityManager)
	 */
	@Override
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

}
