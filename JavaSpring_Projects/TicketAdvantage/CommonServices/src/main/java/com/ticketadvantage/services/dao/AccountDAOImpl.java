
/**
 * 
 */
package com.ticketadvantage.services.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
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
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ticketadvantage.services.dao.sites.ProxyContainer;
import com.ticketadvantage.services.dao.sites.SiteContainer;
import com.ticketadvantage.services.dao.sites.generic.GenericProcessSite;
import com.ticketadvantage.services.errorhandling.AppErrorCodes;
import com.ticketadvantage.services.errorhandling.AppErrorMessage;
import com.ticketadvantage.services.errorhandling.AppException;
import com.ticketadvantage.services.model.Accounts;
import com.ticketadvantage.services.model.Groups;
import com.ticketadvantage.services.model.ProxyLocation;
import com.ticketadvantage.services.model.SiteName;
import com.ticketadvantage.services.model.User;
import com.ticketadvantage.services.model.WeeklyFigures;

/**
 * @author jmiller
 *
 */
@Repository
public class AccountDAOImpl implements AccountDAO {
	private static final Logger LOGGER = Logger.getLogger(AccountDAOImpl.class);

	@PersistenceContext(name = "entityManager")
	private EntityManager entityManager;

	/**
	 * 
	 */
	public AccountDAOImpl() {
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
	 * 
	 * @see com.ticketadvantage.services.dao.AccountDAO#setEntityManager(javax.
	 * persistence.EntityManager)
	 */
	@Override
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ticketadvantage.services.dao.AccountDAO#getEntityManager()
	 */
	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ticketadvantage.services.dao.AccountDAO#persist(com.ticketadvantage.
	 * services.model.Account)
	 */
	@Override
	@Transactional(readOnly = false)
	public Accounts persist(Accounts account) throws AppException {
		LOGGER.info("Entering persist()");
		LOGGER.debug("Accounts: " + account);

		// Set the ID to null
		account.setId(null);

		// Check if account name is already taken
		@SuppressWarnings("unchecked")
		final List<Accounts> listAccount = (List<Accounts>) entityManager
				.createQuery("SELECT a FROM Accounts a WHERE a.name = :name").setParameter("name", account.getName())
				.getResultList();
		if ((listAccount == null) || (listAccount != null && listAccount.size() == 0)) {
			// Write out the account to the account table
			entityManager.persist(account);
		} else if (listAccount != null && listAccount.size() > 0) {
			// We already have one, send necessary error message
			throw new AppException(500, AppErrorCodes.DUPLICATE_ACCOUNT, AppErrorMessage.DUPLICATE_ACCOUNT);
		}

		LOGGER.info("Exiting persist()");
		return account;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ticketadvantage.services.dao.AccountDAO#update(com.ticketadvantage.
	 * services.model.Accounts)
	 */
	@Override
	@Transactional(readOnly = false)
	public Accounts update(Accounts account) throws AppException {
		LOGGER.info("Entering update()");
		LOGGER.debug("Accounts: " + account);
		Accounts retValue = null;

		Accounts tempAccount = entityManager.find(Accounts.class, account.getId());
		if (tempAccount != null) {
			tempAccount.setIsactive(account.getIsactive());
			tempAccount.setName(account.getName());
			tempAccount.setPassword(account.getPassword());
			tempAccount.setUrl(account.getUrl());
			tempAccount.setUsername(account.getUsername());
			tempAccount.setSpreadlimitamount(account.getSpreadlimitamount());
			tempAccount.setTotallimitamount(account.getTotallimitamount());
			tempAccount.setMllimitamount(account.getMllimitamount());
			tempAccount.setTimezone(account.getTimezone());
			tempAccount.setOwnerpercentage(account.getOwnerpercentage());
			tempAccount.setPartnerpercentage(account.getPartnerpercentage());
			tempAccount.setProxylocation(account.getProxylocation());
			tempAccount.setIsmobile(account.getIsmobile());
			tempAccount.setIscomplexcaptcha(account.getIscomplexcaptcha());
			tempAccount.setShowrequestresponse(account.getShowrequestresponse());
			tempAccount.setSitetype(account.getSitetype());
			tempAccount.setHourbefore(account.getHourbefore());
			tempAccount.setMinutebefore(account.getMinutebefore());
			tempAccount.setHourafter(account.getHourafter());
			tempAccount.setMinuteafter(account.getMinuteafter());

			// Write out the account to the account table
			retValue = entityManager.merge(tempAccount);
		} else {
			throw new AppException(500, AppErrorCodes.ACCOUNT_NOT_FOUND, AppErrorMessage.ACCOUNT_NOT_FOUND);
		}
		LOGGER.info("Exiting update()");
		return retValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ticketadvantage.services.dao.AccountDAO#delete(java.lang.Long,
	 * java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = false)
	public void delete(Long userid, Long id) throws AppException {
		LOGGER.info("Entering delete()");
		User user = entityManager.find(User.class, userid);
		if (user != null) {
			final Accounts account = entityManager.find(Accounts.class, id);
			if (account != null) {
				final Set<Accounts> accounts = user.getAccounts();
				if (accounts != null && accounts.size() > 0) {
					accounts.remove(account);
				}
				user.setAccounts(accounts);

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
				throw new AppException(500, AppErrorCodes.ACCOUNT_NOT_FOUND, AppErrorMessage.ACCOUNT_NOT_FOUND);
			}
		} else {
			throw new AppException(500, AppErrorCodes.USER_NOT_FOUND, AppErrorMessage.USER_NOT_FOUND);
		}
		LOGGER.info("Exiting delete()");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ticketadvantage.services.dao.AccountDAO#getAccount(java.lang.Long)
	 */
	@Override
	public Accounts getAccount(Long id) throws AppException {
		LOGGER.info("Entering getAccount()");
		LOGGER.debug("id: " + id);
		Accounts retValue = null;
		try {
			retValue = entityManager.find(Accounts.class, id);
			LOGGER.debug("Account: " + retValue);
			if (retValue == null) {
				// Should have one by this account id
				throw new AppException(500, AppErrorCodes.ACCOUNT_NOT_FOUND, AppErrorMessage.ACCOUNT_NOT_FOUND);
			}
		} catch (NoResultException e) {
			LOGGER.info("NoResultException id: " + id);
			throw new AppException(500, AppErrorCodes.ACCOUNT_NOT_FOUND, AppErrorMessage.ACCOUNT_NOT_FOUND);
		}
		LOGGER.info("Exiting getAccount()");
		return retValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ticketadvantage.services.dao.AccountDAO#findAll()
	 */
	@Override
	public Set<Accounts> findAll() throws AppException {
		LOGGER.info("Entering findAll()");

		@SuppressWarnings("unchecked")
		final List<Accounts> retValue = entityManager.createQuery("SELECT a FROM Accounts a").getResultList();
		
		// Compare the dates to order them appropriately
		Collections.sort(retValue, new Comparator<Accounts>() {
			public int compare(Accounts o1, Accounts o2) {
				if (o1.getName() == null || o2.getName() == null)
					return 0;
				return o1.getName().compareTo(o2.getName());
			}
		});

		// Make the order correct
		final Set<Accounts> accountSet = new LinkedHashSet<Accounts>(retValue);

		LOGGER.info("Exiting findAll()");
		return accountSet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ticketadvantage.services.dao.AccountDAO#getAccountsForUser(java.lang.
	 * Long)
	 */
	@Override
	public Set<Accounts> getAccountsForUser(Long id) throws AppException {
		final Set<Accounts> accountSet = new LinkedHashSet<Accounts>();

		@SuppressWarnings("unchecked")
		List<Object[]> accts = entityManager.createNativeQuery("SELECT * FROM accountsta join usersaccounts on accountsta.id = usersaccounts.accountsid where usersid = " + id + " order by name asc").getResultList();
		for (Object[] a : accts) {
			final Accounts acct = new Accounts();
			final BigInteger theId = (BigInteger)a[0];
			acct.setId(theId.longValue());
			acct.setDatecreated((Date)a[1]);
			acct.setDatemodified((Date)a[2]);
			acct.setIsactive((Boolean)a[3]);
			acct.setSpreadlimitamount((Integer)a[4]);
			acct.setTotallimitamount((Integer)a[5]);
			acct.setMllimitamount((Integer)a[6]);
			acct.setName((String)a[7]);
			acct.setTimezone((String)a[8]);
			acct.setOwnerpercentage((Integer)a[9]);
			acct.setPartnerpercentage((Integer)a[10]);
			acct.setPassword((String)a[11]);
			acct.setProxylocation((String)a[12]);
			acct.setSitetype((String)a[13]);
			acct.setUrl((String)a[14]);
			acct.setUsername((String)a[15]);
			acct.setIsmobile((Boolean)a[16]);
			acct.setShowrequestresponse((Boolean)a[17]);
			acct.setHourbefore((String)a[18]);
			acct.setMinutebefore((String)a[19]);
			acct.setHourafter((String)a[20]);
			acct.setMinuteafter((String)a[21]);
			acct.setIscomplexcaptcha((Boolean)a[22]);

			accountSet.add(acct);
		}

		return accountSet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ticketadvantage.services.dao.AccountDAO#createAccountForUser(java.
	 * lang.Long, com.ticketadvantage.services.model.Account)
	 */
	@Override
	@Transactional(readOnly = false)
	public Accounts createAccountForUser(Long id, Accounts account) throws AppException {
		LOGGER.info("Entering createAccountForUser()");
		LOGGER.debug("id: " + id);
		LOGGER.debug("Accounts: " + account);
		Accounts retValue = null;

		User tempUser = entityManager.find(User.class, id);
		// User found?
		if (tempUser != null) {
			// First get all the accounts for this user
			Set<Accounts> tempAccounts = tempUser.getAccounts();
			tempAccounts.add(account);
			tempUser.setAccounts(tempAccounts);

			// Add account to user
			tempUser = entityManager.merge(tempUser);
			tempAccounts = tempUser.getAccounts();
			final Iterator<Accounts> itr = tempAccounts.iterator();

			// Find the account we just added
			while (itr != null && itr.hasNext()) {
				final Accounts tempAccount = itr.next();
				if (tempAccount != null) {
					if (tempAccount.getName().equals(account.getName())) {
						retValue = tempAccount;
					}
				}
			}
		}

		LOGGER.info("Exiting createAccountForUser()");
		return retValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ticketadvantage.services.dao.AccountDAO#checkSite(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public String checkSite(String url, String username, String password) throws AppException {
		LOGGER.info("Entering checkSite()");
		String retValue = "";
		LOGGER.debug("url: " + url);
		LOGGER.debug("username: " + username);
		LOGGER.debug("password: " + password);

		// Check if this site is supported
		final GenericProcessSite gpSite = new GenericProcessSite();
		retValue = gpSite.checkSite(url, username, password);
		LOGGER.debug("SiteValue: " + retValue);

		LOGGER.info("Exiting checkSite()");
		return retValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ticketadvantage.services.dao.AccountDAO#getProxyNames()
	 */
	@Override
	public Set<ProxyLocation> getProxyNames() {
		LOGGER.info("Entering getProxyNames()");
		final String[] proxyLocations = ProxyContainer.getProxyNames();
		final List<ProxyLocation> proxies = new ArrayList<ProxyLocation>();

		for (int x = 0; (proxyLocations != null && x < proxyLocations.length); x++) {
			final ProxyLocation pl = new ProxyLocation();
			pl.setName(proxyLocations[x]);
			proxies.add(pl);
		}

		// Compare the dates to order them appropriately
		Collections.sort(proxies, new Comparator<ProxyLocation>() {
			public int compare(ProxyLocation o1, ProxyLocation o2) {
				if (o1.getName() == null || o2.getName() == null)
					return 0;
				return o1.getName().compareTo(o2.getName());
			}
		});

		// Make the order correct
		final Set<ProxyLocation> setProxies = new LinkedHashSet<ProxyLocation>(proxies);

		LOGGER.info("Exiting getProxyNames()");
		return setProxies;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.AccountDAO#getSiteNames()
	 */
	@Override
	public Set<SiteName> getSiteNames() {
		LOGGER.info("Entering getSiteNames()");
		final String[] siteNames = SiteContainer.getSiteNames();
		final List<SiteName> sites = new ArrayList<SiteName>();
		for (int x = 0; (siteNames != null && x < siteNames.length); x++) {
			final SiteName pl = new SiteName();
			pl.setName(siteNames[x]);
			sites.add(pl);
		}

		// Compare the dates to order them appropriately
		Collections.sort(sites, new Comparator<SiteName>() {
			public int compare(SiteName o1, SiteName o2) {
				if (o1.getName() == null || o2.getName() == null)
					return 0;
				return o1.getName().compareTo(o2.getName());
			}
		});

		// Make the order correct
		final Set<SiteName> setSites = new LinkedHashSet<SiteName>(sites);

		LOGGER.info("Exiting getSiteNames()");
		return setSites;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ticketadvantage.services.dao.AccountDAO#getWeeklyAccountFigures(java.
	 * lang.Long, java.util.Date, java.lang.String)
	 */
	@Override
	public Set<WeeklyFigures> getWeeklyAccountFigures(Long userId, Date weekStartDate, String timezone)
			throws AppException {
		LOGGER.info("Entering getWeeklyAccountFigures()");
		LOGGER.debug("  userId: " + userId);
		LOGGER.debug("  weekStartDate: " + weekStartDate);
		LOGGER.debug("  timezone: " + timezone);

		final Calendar c = Calendar.getInstance();
		c.setTime(weekStartDate);
		c.add(Calendar.DATE, 7);

		final Date weekEndDate = c.getTime();
		LOGGER.debug("  weekEndDate: " + weekEndDate);

		final String sql = new StringBuilder()
				.append("select u.id as userId, a.id as accountId, a.name as accountName, a.password as accountPassword, 0 as carry,\n")
				.append("	sum(case when date_part('dow', ae.eventdatetime at time zone '" + timezone
						+ "') = 1 then\n")
				.append("		case when ae.eventresult = 'WIN' then ae.eventresultamount\n")
				.append("			when ae.eventresult = 'LOSS' then ae.eventresultamount\n")
				.append("			else 0\n").append("		end\n").append("	else 0 end) as mon,\n")
				.append("	sum(case when date_part('dow', ae.eventdatetime at time zone '" + timezone
						+ "') = 2 then\n")
				.append("			case when ae.eventresult = 'WIN' then ae.eventresultamount\n")
				.append("			 	when ae.eventresult = 'LOSS' then ae.eventresultamount\n")
				.append("			 	else 0\n").append("			end\n").append("		else 0 end) as tue,\n")
				.append("	sum(case when date_part('dow', ae.eventdatetime at time zone '" + timezone
						+ "') = 3 then\n")
				.append("			case when ae.eventresult = 'WIN' then ae.eventresultamount\n")
				.append("	    		  when ae.eventresult = 'LOSS' then ae.eventresultamount\n")
				.append("	    		  else 0\n").append("	    	end\n").append("	    else 0 end) as wed,\n")
				.append("	sum(case when date_part('dow', ae.eventdatetime at time zone '" + timezone
						+ "') = 4 then\n")
				.append("	    	case when ae.eventresult = 'WIN' then ae.eventresultamount\n")
				.append("	    		  when ae.eventresult = 'LOSS' then ae.eventresultamount\n")
				.append("	    		  else 0\n").append("	    	end\n").append("	    else 0 end) as thu,\n")
				.append("	sum(case when date_part('dow', ae.eventdatetime at time zone '" + timezone
						+ "') = 5 then\n")
				.append("	    	case when ae.eventresult = 'WIN' then ae.eventresultamount\n")
				.append("	    		  when ae.eventresult = 'LOSS' then ae.eventresultamount\n")
				.append("	    		  else 0\n").append("	    	end\n").append("	    else 0 end) as fri,\n")
				.append("	sum(case when date_part('dow', ae.eventdatetime at time zone '" + timezone
						+ "') = 6 then\n")
				.append("	    	case when ae.eventresult = 'WIN' then ae.eventresultamount\n")
				.append("	    		  when ae.eventresult = 'LOSS' then ae.eventresultamount\n")
				.append("	    		  else 0\n").append("	    	end\n").append("	    else 0 end) as sat,\n")
				.append("	sum(case when date_part('dow', ae.eventdatetime at time zone '" + timezone
						+ "') = 0 then\n")
				.append("	    	case when ae.eventresult = 'WIN' then ae.eventresultamount\n")
				.append("	    		  when ae.eventresult = 'LOSS' then ae.eventresultamount\n")
				.append("	    		  else 0\n").append("	    	end\n").append("	    else 0 end) as sun,\n")
				.append("	sum(case when ae.eventresult = 'WIN' then ae.eventresultamount \n")
				.append("	    	  when ae.eventresult = 'LOSS' then ae.eventresultamount\n")
				.append("	    	  else 0\n").append("	    end) as week,\n")
				.append("	sum(case when (ae.eventresult = '' OR ae.eventresult IS NULL) AND ae.actualamount != '' AND ae.iscompleted = 't' then to_number(ae.actualamount, '999999.99') else 0 end) as pending,\n")
				.append("	0 as balance\n").append("	from usersta u \n")
				.append("   join usersaccounts ua on ua.usersid = u.id \n")
				.append("	join accountsta a on a.id = ua.accountsid \n")
				.append("   join accountevent ae on ae.accountid = a.id \n").append("   where u.id = :user_id")
				.append("	and ae.eventdatetime between :week_start_date and :week_end_date")
				.append("	group by u.id, a.id, ae.name \n").append("	order by ae.name \n").toString();

		LOGGER.debug("weeklysql: " + sql);
		LOGGER.debug("userId: " + userId);
		LOGGER.debug("weekStartDate: " + weekStartDate);
		LOGGER.debug("weekEndDate: " + weekEndDate);

		Query query = entityManager.createNativeQuery(sql, WeeklyFigures.class).setParameter("user_id", userId)
				.setParameter("week_start_date", weekStartDate).setParameter("week_end_date", weekEndDate);
		@SuppressWarnings("unchecked")
		List<WeeklyFigures> retValue = query.getResultList();
		
		if (retValue != null && retValue.size() > 0) {
			for (WeeklyFigures wf : retValue) {
				LOGGER.debug("WeeklyFigures: " + wf);
			}
		}
		final Set<WeeklyFigures> weeklyFiguresSet = new LinkedHashSet<WeeklyFigures>(retValue);

		LOGGER.info("Exiting getWeeklyAccountFigures()");
		return weeklyFiguresSet;
	}
}