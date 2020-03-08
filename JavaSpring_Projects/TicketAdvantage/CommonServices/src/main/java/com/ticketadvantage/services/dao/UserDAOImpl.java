/**
 * 
 */
package com.ticketadvantage.services.dao;

import java.util.Calendar;
import java.util.List;

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
import com.ticketadvantage.services.model.User;

/**
 * @author jmiller
 *
 */
@Repository
public class UserDAOImpl implements UserDAO {
	private static final Logger LOGGER = Logger.getLogger(UserDAOImpl.class);

	@PersistenceContext(name = "entityManager")
	private EntityManager entityManager;

	/**
	 * 
	 */
	public UserDAOImpl() {
		super();
		LOGGER.debug("Entering UserDAOImpl()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final ApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
		final UserDAOImpl userDAO = ctx.getBean(UserDAOImpl.class);
		try {
			final User user = userDAO.login("user", "password");
			LOGGER.info("User: " + user);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.UserDAO#find(java.lang.Long)
	 */
	@Transactional(readOnly=true)
	public User find(Long id) throws AppException {
		LOGGER.info("Entering find()");
		LOGGER.debug("id: " + id);
		User tempUser = entityManager.find(User.class, id);
		LOGGER.debug("User: " + tempUser);
		LOGGER.info("Exiting find()");
		return tempUser;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.UserDAO#findByUsername(java.lang.String)
	 */
	@Override
	@Transactional(readOnly=true)
	public User findByUsername(String username) throws AppException {
		LOGGER.info("Entering findByUsername()");
		User retValue = null;
		try {
			LOGGER.debug("login username: " + username);
			final List<User> listUser = entityManager
					.createQuery(
							"SELECT u FROM User u WHERE u.username = :username AND isactive = true")
					.setParameter("username", username).getResultList();
			int x = 0;

			// Should only be one!!!
			for (x = 0; (listUser != null) && x < listUser.size(); x++) {
				retValue = listUser.get(x++);
			}

			LOGGER.debug("User: " + retValue);
			if (retValue == null) {
				// We already have one, send necessary error message
				throw new AppException(401, AppErrorCodes.LOGIN_FAILED,  
						AppErrorMessage.LOGIN_FAILED);
			}
		} catch (NoResultException e) {
			LOGGER.info("NoResultException username: " + username);
			throw new AppException(401, AppErrorCodes.LOGIN_FAILED,  
					AppErrorMessage.LOGIN_FAILED);
		}
		LOGGER.info("Exiting findByUsername()");
		return retValue;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.user.dao.UserDAO#persist(com.ticketadvantage.services.user.model.User)
	 */
	@Override
	@Transactional(readOnly = false)
	public User persist(User user) throws AppException {
		LOGGER.info("Entering persist()");
		LOGGER.debug("User: " + user);
		user.setId(null);
		// Check if username is already taken
		final List<User> listUser = (List<User>) entityManager
				.createQuery("SELECT u FROM User u WHERE u.username = :username")
				.setParameter("username", user.getUsername()).getResultList();
		if ((listUser == null) || (listUser != null && listUser.size() == 0)) {
			// Write out the user to the users tables
			entityManager.persist(user);
		} else if (listUser != null && listUser.size() > 0) {
			// We already have one, send necessary error message
			throw new AppException(401, AppErrorCodes.DUPLICATE_USERNAME,  
					AppErrorMessage.DUPLICATE_USERNAME);
		}
		LOGGER.info("Exiting persist()");
		return user;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.UserDAO#update(java.lang.Long, com.ticketadvantage.services.model.User)
	 */
	@Override
	@Transactional(readOnly = false)
	public void update(Long id, User user) throws AppException {
		LOGGER.info("Entering update()");
		LOGGER.debug("id: " + id);
		LOGGER.debug("User: " + user);
		User tempUser = entityManager.find(User.class, id);
		tempUser = copyUser(user, tempUser);
		LOGGER.debug("User: " + tempUser);
		entityManager.merge(tempUser);
		LOGGER.info("Exiting update()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.UserDAO#login(java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(readOnly=true)
	public User login(String username, String password) throws AppException {
		LOGGER.info("Entering login()");
		User retValue = null;
		try {
			LOGGER.debug("login username: " + username + " password: " + password);
			final List<User> listUser = entityManager
					.createQuery(
							"SELECT u FROM User u WHERE u.username = :username AND u.password = :password AND isactive = true")
					.setParameter("username", username).setParameter("password", password).getResultList();
			int x = 0;
			// Should only be one!!!
			for (x = 0; (listUser != null) && x < listUser.size(); x++) {
				retValue = listUser.get(x++);
			}
			LOGGER.debug("User: " + retValue);
			if (retValue == null) {
				// We already have one, send necessary error message
				throw new AppException(401, AppErrorCodes.LOGIN_FAILED,  
						AppErrorMessage.LOGIN_FAILED);
			}
		} catch (NoResultException e) {
			LOGGER.info("NoResultException username: " + username + " password: " + password);
			throw new AppException(401, AppErrorCodes.LOGIN_FAILED,  
					AppErrorMessage.LOGIN_FAILED);
		}
		LOGGER.info("Exiting login()");
		return retValue;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.user.dao.UserDAO#findAll()
	 */
	public List<User> findAll() throws AppException {
		LOGGER.info("Entering findAll()");
		final List<User> retValue = entityManager.createQuery("SELECT u FROM User u").getResultList();
		LOGGER.info("Exiting findAll()");
		return retValue;
	}

	/**
	 * 
	 * @param user1
	 * @param user2
	 * @return
	 */
	private User copyUser(User user1, User user2) {
		user2.setAccounts(user1.getAccounts());
		user2.setDatecreated(user1.getDatecreated());
		user2.setDatemodified(Calendar.getInstance().getTime());
		user2.setEmail(user1.getEmail());
		user2.setIsactive(user1.getIsactive());
		user2.setMobilenumber(user1.getMobilenumber());
		user2.setPassword(user1.getPassword());
		user2.setUsername(user1.getUsername());
		return user2;
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
