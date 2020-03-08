
/**
 * 
 */
package com.ticketadvantage.services.dao;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ticketadvantage.services.errorhandling.AppErrorCodes;
import com.ticketadvantage.services.errorhandling.AppErrorMessage;
import com.ticketadvantage.services.errorhandling.AppException;
import com.ticketadvantage.services.model.Accounts;
import com.ticketadvantage.services.model.Groups;
import com.ticketadvantage.services.model.TwitterAccounts;
import com.ticketadvantage.services.model.User;

/**
 * @author jmiller
 *
 */
@Repository
public class TwitterAccountDAOImpl implements TwitterAccountDAO {
	private static final Logger LOGGER = Logger.getLogger(TwitterAccountDAOImpl.class);

	@PersistenceContext(name = "entityManager")
	private EntityManager entityManager;

	/**
	 * 
	 */
	public TwitterAccountDAOImpl() {
		super();
		LOGGER.debug("Entering TwitterAccountDAOImpl()");
		LOGGER.debug("Exiting TwitterAccountDAOImpl()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final ApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
		final TwitterAccountDAOImpl accountDAO = ctx.getBean(TwitterAccountDAOImpl.class);
		try {
			TwitterAccounts account = accountDAO.getTwitterAccount(new Long(1));
			accountDAO.LOGGER.info("Account: " + account);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	 * @see com.ticketadvantage.services.dao.AccountDAO#getEntityManager()
	 */
	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	@Transactional(readOnly = false)
	public TwitterAccounts persist(TwitterAccounts account) throws AppException {
		LOGGER.info("Entering persist()");
		LOGGER.debug("TwitterAccounts: " + account);
		account.setId(null);

		// Check if account name is already taken
		final List<TwitterAccounts> listAccount = (List<TwitterAccounts>) entityManager
				.createQuery("SELECT a FROM Accounts a WHERE a.name = :name")
				.setParameter("name", account.getName()).getResultList();
		if ((listAccount == null) || (listAccount != null && listAccount.size() == 0)) {
			// Write out the account to the account table
			entityManager.persist(account);
		} else if (listAccount != null && listAccount.size() > 0) {
			// We already have one, send necessary error message
			throw new AppException(500, AppErrorCodes.DUPLICATE_ACCOUNT,  
					AppErrorMessage.DUPLICATE_ACCOUNT);
		}

		LOGGER.info("Exiting persist()");
		return account;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.AccountDAO#update(com.ticketadvantage.services.model.Accounts)
	 */
	@Override
	@Transactional(readOnly = false)
	public TwitterAccounts update(TwitterAccounts account) throws AppException {
		LOGGER.info("Entering update()");
		LOGGER.debug("TwitterAccounts: " + account);
		TwitterAccounts retValue = null;

		final TwitterAccounts tempAccount = entityManager.find(TwitterAccounts.class, account.getId());
		if (tempAccount != null) {
			tempAccount.setAccountid(account.getAccountid());
			tempAccount.setInet(account.getInet());
			tempAccount.setIsactive(account.getIsactive());
			tempAccount.setName(account.getName());
			tempAccount.setSitetype(account.getSitetype());
			tempAccount.setScreenname(account.getScreenname());
			tempAccount.setHandleid(account.getHandleid());

			// Write out the account to the account table
			retValue = entityManager.merge(tempAccount);
		} else {
			throw new AppException(500, AppErrorCodes.ACCOUNT_NOT_FOUND,  
					AppErrorMessage.ACCOUNT_NOT_FOUND);
		}

		LOGGER.info("Exiting update()");
		return retValue;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.AccountDAO#delete(java.lang.Long, java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = false)
	public void delete(Long userid, Long id) throws AppException {
		LOGGER.info("Entering delete()");
		User user = entityManager.find(User.class, userid);

		if (user != null) {
			final TwitterAccounts account = entityManager.find(TwitterAccounts.class, id);
			if (account != null) {
				final Set<TwitterAccounts> accounts = user.getTwitteraccounts();
				if (accounts != null && accounts.size() > 0) {
					accounts.remove(account);
				}
				user.setTwitteraccounts(accounts);

				final Set<Groups> groups = user.getGroups();
				if (groups != null && groups.size() > 0) {
					Iterator<Groups> grps = groups.iterator();
					while (grps.hasNext()) {
						final Groups group = grps.next();
						final Set<Accounts> accts = group.getAccounts();
						if (accts != null && accts.size() > 0) {
							accts.remove(account);
						}
						// Commit the group
						group.setAccounts(accts);
						entityManager.merge(group);
					}
				}

				// Commit user info
				user = entityManager.merge(user);

				// Remove the account
				entityManager.remove(account);
			} else {
				throw new AppException(500, AppErrorCodes.ACCOUNT_NOT_FOUND,  
					AppErrorMessage.ACCOUNT_NOT_FOUND);
			}
		} else {
			throw new AppException(500, AppErrorCodes.USER_NOT_FOUND,  
				AppErrorMessage.USER_NOT_FOUND);
		}

		LOGGER.info("Exiting delete()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.AccountDAO#getAccount(java.lang.Long)
	 */
	@Override
	public TwitterAccounts getTwitterAccount(Long id) throws AppException {
		LOGGER.info("Entering getTwitterAccount()");
		LOGGER.debug("id: " + id);
		TwitterAccounts retValue = null;

		try {
			retValue = entityManager.find(TwitterAccounts.class, id);
			LOGGER.debug("TwitterAccounts: " + retValue);
			if (retValue == null) {
				// Should have one by this account id
				throw new AppException(500, AppErrorCodes.ACCOUNT_NOT_FOUND,  
						AppErrorMessage.ACCOUNT_NOT_FOUND);
			}
		} catch (NoResultException e) {
			LOGGER.info("NoResultException id: " + id);
			throw new AppException(500, AppErrorCodes.ACCOUNT_NOT_FOUND,  
					AppErrorMessage.ACCOUNT_NOT_FOUND);
		}

		LOGGER.info("Exiting getTwitterAccount()");
		return retValue;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.AccountDAO#findAll()
	 */
	@Override
	public Set<TwitterAccounts> findAll() throws AppException {
		LOGGER.info("Entering findAll()");

		@SuppressWarnings("unchecked")
		final List<TwitterAccounts> retValue = entityManager.createQuery("SELECT a FROM TwitterAccounts a").getResultList();

		// Compare the dates to order them appropriately
		Collections.sort(retValue, new Comparator<TwitterAccounts>() {
			public int compare(TwitterAccounts o1, TwitterAccounts o2) {
				if (o1.getName() == null || o2.getName() == null)
					return 0;
				return o1.getName().compareTo(o2.getName());
			}
		});

		// Make the order correct
		final Set<TwitterAccounts> accountSet = new LinkedHashSet<TwitterAccounts>(retValue);

		LOGGER.info("Exiting findAll()");
		return accountSet;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.AccountDAO#getAccountsForUser(java.lang.Long)
	 */
	@Override
	public Set<TwitterAccounts> getAccountsForUser(Long id) throws AppException {
		final Set<TwitterAccounts> accountSet = new LinkedHashSet<TwitterAccounts>();

		@SuppressWarnings("unchecked")
		List<Object[]> accts = entityManager.createNativeQuery("SELECT * FROM twitteraccountsta join userstwitteraccounts on twitteraccountsta.id = userstwitteraccounts.twitteraccountsid where usersid = " + id + " order by name asc").getResultList();
		for (Object[] a : accts) {
			final TwitterAccounts acct = new TwitterAccounts();
			final BigInteger theId = (BigInteger)a[0];
			acct.setId(theId.longValue());
			acct.setName((String)a[1]);
			acct.setInet((String)a[2]);
			acct.setAccountid((String)a[3]);
			acct.setScreenname((String)a[4]);
			acct.setHandleid((String)a[5]);
			acct.setSitetype((String)a[6]);
			acct.setIsactive((Boolean)a[7]);
			acct.setDatecreated((Date)a[8]);
			acct.setDatemodified((Date)a[9]);

			accountSet.add(acct);
		}

		return accountSet;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.AccountDAO#createAccountForUser(java.lang.Long, com.ticketadvantage.services.model.Account)
	 */
	@Override
	@Transactional(readOnly = false)
	public TwitterAccounts createTwitterAccountForUser(Long id, TwitterAccounts account) throws AppException {
		LOGGER.info("Entering createTwitterAccountForUser()");
		LOGGER.debug("id: " + id);
		LOGGER.debug("TwitterAccounts: " + account);
		TwitterAccounts retValue = null;

		User tempUser = entityManager.find(User.class, id);
		// User found?
		if (tempUser != null) {
			// First get all the accounts for this user
			Set<TwitterAccounts> tempAccounts = tempUser.getTwitteraccounts();
			tempAccounts.add(account);
			tempUser.setTwitteraccounts(tempAccounts);

			// Add account to user
			tempUser = entityManager.merge(tempUser);
			tempAccounts = tempUser.getTwitteraccounts();
			final Iterator<TwitterAccounts> itr = tempAccounts.iterator();

			// Find the account we just added
			while (itr != null && itr.hasNext()) {
				final TwitterAccounts tempAccount = itr.next();
				if (tempAccount != null) {
					if (tempAccount.getName().equals(account.getName())) {
						retValue = tempAccount;
					}
				}
			}
		}

		LOGGER.info("Exiting createTwitterAccountForUser()");
		return retValue;
	}
}