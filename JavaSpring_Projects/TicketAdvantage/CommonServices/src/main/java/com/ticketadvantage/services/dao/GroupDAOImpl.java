/**
 * 
 */
package com.ticketadvantage.services.dao;

import java.math.BigInteger;
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
import com.ticketadvantage.services.model.Groups;
import com.ticketadvantage.services.model.User;

/**
 * @author jmiller
 *
 */
@Repository
public class GroupDAOImpl implements GroupDAO {
	private static final Logger LOGGER = Logger.getLogger(GroupDAOImpl.class);

	@PersistenceContext(name = "entityManager")
	private EntityManager entityManager;

	/**
	 * 
	 */
	public GroupDAOImpl() {
		super();
		LOGGER.debug("Entering GroupDAOImpl()");
		LOGGER.debug("Exiting GroupDAOImpl()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final ApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
		final GroupDAOImpl groupDAO = ctx.getBean(GroupDAOImpl.class);
		try {
			Groups group = groupDAO.getGroup(new Long(1));
			groupDAO.LOGGER.info("Group: " + group);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.GroupDAO#persist(com.ticketadvantage.services.model.Group)
	 */
	@Override
	@Transactional(readOnly = false)
	public Groups persist(Groups group) throws AppException {
		LOGGER.info("Entering persist()");
		group.setId(null);
		final List<Groups> listGroup = (List<Groups>) entityManager
				.createQuery("SELECT g FROM Groups g WHERE g.name = :name")
				.setParameter("name", group.getName()).getResultList();
		if ((listGroup == null) || (listGroup != null && listGroup.size() == 0)) {
			// Write out the user to the users tables
			entityManager.persist(group);
		} else if (listGroup != null && listGroup.size() > 0) {
			// We already have one, send necessary error message
			throw new AppException(500, AppErrorCodes.DUPLICATE_GROUP,  
					AppErrorMessage.DUPLICATE_GROUP);
		}

		LOGGER.info("Exiting persist()");
		return group;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.GroupDAO#update(com.ticketadvantage.services.model.Groups)
	 */
	@Override
	@Transactional(readOnly = false)
	public Groups update(Groups group) throws AppException {
		LOGGER.info("Entering update()");
		LOGGER.debug("Groups: " + group);
		Groups retValue = null;
		Groups tempGroup = entityManager.find(Groups.class, group.getId());
		if (tempGroup != null) {
			tempGroup.setName(group.getName());
//			tempGroup.setVpninfo(group.getVpninfo());
			tempGroup.setIsactive(group.getIsactive());

			// Write out the account to the account table
			retValue = entityManager.merge(tempGroup);
		} else {
			throw new AppException(500, AppErrorCodes.GROUP_NOT_FOUND,  
					AppErrorMessage.GROUP_NOT_FOUND);
		}
		LOGGER.info("Exiting update()");
		return retValue;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.GroupDAO#delete(java.lang.Long, java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = false)
	public void delete(Long userid, Long id) throws AppException {
		LOGGER.info("Entering delete()");
		User user = entityManager.find(User.class, userid);
		if (user != null) {
			final Groups group = entityManager.find(Groups.class, id);
			if (group != null) {
				final Set<Groups> groups = user.getGroups();
	
				// Now remove the group itself from user list
				if (groups != null && groups.size() > 0) {
					groups.remove(group);
				}
				user.setGroups(groups);
	
				// Commit the updated user association for group
				entityManager.merge(user);
				
				// Remove the group
				entityManager.remove(group);
			} else {
				throw new AppException(500, AppErrorCodes.GROUP_NOT_FOUND,  
						AppErrorMessage.GROUP_NOT_FOUND);
			}
		} else {
			throw new AppException(500, AppErrorCodes.USER_NOT_FOUND,  
					AppErrorMessage.USER_NOT_FOUND);
		}
		LOGGER.info("Exiting delete()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.GroupDAO#getGroup(java.lang.Long)
	 */
	@Override
	public Groups getGroup(Long id) throws AppException {
		LOGGER.info("Entering getGroup()");
		Groups retValue = null;
		try {
			LOGGER.debug("id: " + id);
			retValue = entityManager.find(Groups.class, id);
			LOGGER.debug("Group: " + retValue);
			if (retValue == null) {
				throw new AppException(500, AppErrorCodes.GROUP_NOT_FOUND,  
						AppErrorMessage.GROUP_NOT_FOUND);				
			}
		} catch (NoResultException e) {
			LOGGER.info("NoResultException id: " + id);
			throw new AppException(500, AppErrorCodes.GROUP_NOT_FOUND,  
					AppErrorMessage.GROUP_NOT_FOUND);
		}
		LOGGER.info("Exiting getGroup()");
		return retValue;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.GroupDAO#findAll()
	 */
	@Override
	@Transactional(readOnly = true)
	public Set<Groups> findAll() throws AppException {
		LOGGER.info("Entering findAll()");
		final List<Groups> retValue = entityManager.createQuery("SELECT g FROM Groups g order by id ASC").getResultList();
		final Set<Groups> groupSet = new LinkedHashSet<Groups>(retValue);
		LOGGER.info("Exiting findAll()");
		return groupSet;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.GroupDAO#getGroupsForUser(java.lang.Long)
	 */
	@Override
	public Set<Groups> getGroupsForUser(Long id) throws AppException {
		LOGGER.info("Entering getGroupsForUser()");
		final Set<Groups> groupSet = new LinkedHashSet<Groups>();

		@SuppressWarnings("unchecked")
		List<Object[]> grps = entityManager.createNativeQuery("SELECT * FROM groupsta join usersgroups on groupsta.id = usersgroups.groupsid where usersid = " + id + " order by name asc").getResultList();
		for (Object[] g : grps) {
			final Groups group = new Groups();
			final BigInteger theId = (BigInteger)g[0];
			group.setId(theId.longValue());
			group.setDatecreated((Date)g[1]);
			group.setDatemodified((Date)g[2]);
			group.setIsactive((Boolean)g[3]);
			group.setName((String)g[4]);

			Set<Accounts> accounts = new LinkedHashSet<Accounts>();
			@SuppressWarnings("unchecked")
			List<Object[]> accts = entityManager.createNativeQuery("SELECT * FROM accountsta join groupsaccounts on accountsta.id = groupsaccounts.accountsid where groupsid = " + theId + " order by name asc").getResultList();
			for (Object[] a : accts) {
				final Accounts acct = new Accounts();
				final BigInteger theaId = (BigInteger)a[0];
				acct.setId(theaId.longValue());
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
				accounts.add(acct);
			}

			group.setAccounts(accounts);
			groupSet.add(group);
		}

		LOGGER.info("Exiting getGroupsForUser()");
		return groupSet;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.GroupDAO#createGroupForUser(java.lang.Long, com.ticketadvantage.services.model.Group)
	 */
	@Override
	@Transactional(readOnly = false)
	public Groups createGroupForUser(Long id, Groups group) throws AppException {
		LOGGER.info("Entering createGroupForUser()");
		LOGGER.debug("id: " + id);
		LOGGER.debug("Group: " + group);
		Groups retValue = null;
		User tempUser = entityManager.find(User.class, id);
		if (tempUser != null) {
			Set<Groups> tempGroups = tempUser.getGroups();
			if (tempGroups != null) {
				tempGroups.add(group);
			} else {
				tempGroups = new HashSet<Groups>();
				tempGroups.add(group);
			}
			tempUser = entityManager.merge(tempUser);
			tempGroups = tempUser.getGroups();
			final Iterator<Groups> itr = tempGroups.iterator();
			while (itr != null && itr.hasNext()) {
				final Groups tempGroup = itr.next();
				if (tempGroup!= null && tempGroup.getName().equals(group.getName())) {
					retValue = tempGroup;
				}
			}
		} else {
			throw new AppException(500, AppErrorCodes.USER_NOT_FOUND,  
					AppErrorMessage.USER_NOT_FOUND);
		}
		LOGGER.info("Exiting createGroupForUser()");
		return retValue;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.GroupDAO#addAccountForGroup(java.lang.Long, java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = false)
	public void addAccountForGroup(Long group_id, Long account_id) throws AppException {
		LOGGER.info("Entering addAccountForGroup()");
		final Groups group = entityManager.find(Groups.class, group_id);
		if (group != null) {
			final Accounts account = entityManager.find(Accounts.class, account_id);
			if (account != null) {
				Set<Accounts> accounts = group.getAccounts();
				if (accounts != null && accounts.size() > 0) {
					accounts.add(account);
				} else {
					accounts = new HashSet<Accounts>();
					accounts.add(account);			
				}
				group.setAccounts(accounts);
				entityManager.merge(group);
			} else {
				throw new AppException(500, AppErrorCodes.ACCOUNT_NOT_FOUND,  
						AppErrorMessage.ACCOUNT_NOT_FOUND);
			}
		} else {
			throw new AppException(500, AppErrorCodes.GROUP_NOT_FOUND,  
					AppErrorMessage.GROUP_NOT_FOUND);
		}
		LOGGER.info("Exiting addAccountForGroup()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.GroupDAO#deleteAccountForGroup(java.lang.Long, java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = false)
	public void deleteAccountForGroup(Long group_id, Long account_id) throws AppException {
		LOGGER.info("Entering deleteAccountForGroup()");
		Groups group = entityManager.find(Groups.class, group_id);
		if (group != null) {
			Accounts account = entityManager.find(Accounts.class, account_id);
			if (account != null) {
				Set<Accounts> accounts = group.getAccounts();
				if (accounts != null && accounts.size() > 0) {
					accounts.remove(account);
				}
				group.setAccounts(accounts);
				group = entityManager.merge(group);
			} else {
				throw new AppException(500, AppErrorCodes.ACCOUNT_NOT_FOUND,  
						AppErrorMessage.ACCOUNT_NOT_FOUND);
			}
		} else {
			throw new AppException(500, AppErrorCodes.GROUP_NOT_FOUND,  
					AppErrorMessage.GROUP_NOT_FOUND);
		}
		LOGGER.info("Exiting deleteAccountForGroup()");
	}

	/**
	 * 
	 * @param entityManager
	 */
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
}