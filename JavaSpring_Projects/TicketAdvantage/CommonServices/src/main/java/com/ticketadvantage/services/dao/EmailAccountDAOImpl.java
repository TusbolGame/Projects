
/**
 * 
 */
package com.ticketadvantage.services.dao;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
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
import com.ticketadvantage.services.model.EmailAccounts;
import com.ticketadvantage.services.model.Groups;
import com.ticketadvantage.services.model.User;

/**
 * @author jmiller
 *
 */
@Repository
public class EmailAccountDAOImpl implements EmailAccountDAO {
	private static final Logger LOGGER = Logger.getLogger(EmailAccountDAOImpl.class);

	@PersistenceContext(name = "entityManager")
	private EntityManager entityManager;

	/**
	 * 
	 */
	public EmailAccountDAOImpl() {
		super();
		LOGGER.debug("Entering EmailAccountDAOImpl()");
		LOGGER.debug("Exiting EmailAccountDAOImpl()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final ApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
		final EmailAccountDAOImpl accountDAO = ctx.getBean(EmailAccountDAOImpl.class);
		try {
			EmailAccounts account = accountDAO.getEmailAccount(new Long(1));
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
	public EmailAccounts persist(EmailAccounts account) throws AppException {
		LOGGER.info("Entering persist()");
		LOGGER.debug("EmailAccounts: " + account);
		account.setId(null);

		// Check if account name is already taken
		final List<EmailAccounts> listAccount = (List<EmailAccounts>) entityManager
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
	public EmailAccounts update(EmailAccounts account) throws AppException {
		LOGGER.info("Entering update()");
		LOGGER.debug("EmailAccounts: " + account);
		EmailAccounts retValue = null;

		final EmailAccounts tempAccount = entityManager.find(EmailAccounts.class, account.getId());
		if (tempAccount != null) {
			tempAccount.setAccountid(account.getAccountid());
			tempAccount.setAddress(account.getAddress());
			tempAccount.setAuthenticationtype(account.getAuthenticationtype());
			tempAccount.setClientid(account.getClientid());
			tempAccount.setClientsecret(account.getClientsecret());
			tempAccount.setEmailtype(account.getEmailtype());
			tempAccount.setGranttype(account.getGranttype());
			tempAccount.setHost(account.getHost());
			tempAccount.setInet(account.getInet());
			tempAccount.setIsactive(account.getIsactive());
			tempAccount.setName(account.getName());
			tempAccount.setPassword(account.getPassword());
			tempAccount.setPort(account.getPort());
			tempAccount.setProvider(account.getProvider());
			tempAccount.setRefreshtoken(account.getRefreshtoken());
			tempAccount.setSitetype(account.getSitetype());
			tempAccount.setTimezone(account.getTimezone());
			tempAccount.setTls(account.getTls());

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
			final EmailAccounts account = entityManager.find(EmailAccounts.class, id);
			if (account != null) {
				final Set<EmailAccounts> accounts = user.getEmailaccounts();
				if (accounts != null && accounts.size() > 0) {
					accounts.remove(account);
				}
				user.setEmailaccounts(accounts);

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
	public EmailAccounts getEmailAccount(Long id) throws AppException {
		LOGGER.info("Entering getEmailAccount()");
		LOGGER.debug("id: " + id);
		EmailAccounts retValue = null;

		try {
			retValue = entityManager.find(EmailAccounts.class, id);
			LOGGER.debug("EmailAccounts: " + retValue);
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

		LOGGER.info("Exiting getEmailAccount()");
		return retValue;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.AccountDAO#findAll()
	 */
	@Override
	public Set<EmailAccounts> findAll() throws AppException {
		LOGGER.info("Entering findAll()");

		@SuppressWarnings("unchecked")
		final List<EmailAccounts> retValue = entityManager.createQuery("SELECT a FROM EmailAccounts a").getResultList();

		// Compare the dates to order them appropriately
		Collections.sort(retValue, new Comparator<EmailAccounts>() {
			public int compare(EmailAccounts o1, EmailAccounts o2) {
				if (o1.getName() == null || o2.getName() == null)
					return 0;
				return o1.getName().compareTo(o2.getName());
			}
		});

		// Make the order correct
		final Set<EmailAccounts> accountSet = new LinkedHashSet<EmailAccounts>(retValue);

		LOGGER.info("Exiting findAll()");
		return accountSet;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.AccountDAO#getAccountsForUser(java.lang.Long)
	 */
	@Override
	public Set<EmailAccounts> getAccountsForUser(Long id) throws AppException {
		final Set<EmailAccounts> accountSet = new LinkedHashSet<EmailAccounts>();

		@SuppressWarnings("unchecked")
		List<Object[]> accts = entityManager.createNativeQuery("SELECT * FROM emailaccountsta join usersemailaccounts on emailaccountsta.id = usersemailaccounts.emailaccountsid where usersid = " + id + " order by name asc").getResultList();
		for (Object[] a : accts) {
			final EmailAccounts acct = new EmailAccounts();
			final BigInteger theId = (BigInteger)a[0];
			acct.setId(theId.longValue());
			acct.setName((String)a[1]);
			acct.setInet((String)a[2]);
			acct.setAddress((String)a[3]);
			acct.setPassword((String)a[4]);
			acct.setHost((String)a[5]);
			acct.setPort((String)a[6]);
			acct.setTls((Boolean)a[7]);
			acct.setTimezone((String)a[8]);
			acct.setSitetype((String)a[9]);
			acct.setProvider((String)a[10]);
			acct.setEmailtype((String)a[11]);
			acct.setAuthenticationtype((String)a[12]);
			acct.setClientid((String)a[13]);
			acct.setClientsecret((String)a[14]);
			acct.setRefreshtoken((String)a[15]);
			acct.setGranttype((String)a[16]);
			acct.setIsactive((Boolean)a[17]);
			acct.setDatecreated((Date)a[18]);
			acct.setDatemodified((Date)a[19]);
			acct.setAccountid((String)a[20]);

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
	public EmailAccounts createEmailAccountForUser(Long id, EmailAccounts account) throws AppException {
		LOGGER.info("Entering createEmailAccountForUser()");
		LOGGER.debug("id: " + id);
		LOGGER.debug("EmailAccounts: " + account);
		EmailAccounts retValue = null;

		User tempUser = entityManager.find(User.class, id);
		// User found?
		if (tempUser != null) {
			// First get all the accounts for this user
			Set<EmailAccounts> tempAccounts = tempUser.getEmailaccounts();
			tempAccounts.add(account);
			tempUser.setEmailaccounts(tempAccounts);

			// Add account to user
			tempUser = entityManager.merge(tempUser);
			tempAccounts = tempUser.getEmailaccounts();
			final Iterator<EmailAccounts> itr = tempAccounts.iterator();

			// Find the account we just added
			while (itr != null && itr.hasNext()) {
				final EmailAccounts tempAccount = itr.next();
				if (tempAccount != null) {
					if (tempAccount.getName().equals(account.getName())) {
						retValue = tempAccount;
					}
				}
			}
		}

		LOGGER.info("Exiting createEmailAccountForUser()");
		return retValue;
	}
}