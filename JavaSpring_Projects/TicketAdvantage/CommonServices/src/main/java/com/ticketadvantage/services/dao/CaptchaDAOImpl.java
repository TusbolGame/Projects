/**
 * 
 */
package com.ticketadvantage.services.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ticketadvantage.services.errorhandling.AppErrorCodes;
import com.ticketadvantage.services.errorhandling.AppErrorMessage;
import com.ticketadvantage.services.errorhandling.AppException;
import com.ticketadvantage.services.model.Captcha;

/**
 * @author jmiller
 *
 */
@Repository
public class CaptchaDAOImpl implements CaptchaDAO {
	private static final Logger LOGGER = Logger.getLogger(CaptchaDAOImpl.class);

	@PersistenceContext(name = "entityManager")
	private EntityManager entityManager;

	/**
	 * 
	 */
	public CaptchaDAOImpl() {
		super();
		LOGGER.debug("Entering CaptchaDAOImpl()");
		LOGGER.debug("Exiting CaptchaDAOImpl()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.CaptchaDAO#setupCaptcha(com.ticketadvantage.services.model.Captcha)
	 */
	@Override
	@Transactional(readOnly = false)
	public Captcha setupCaptcha(Captcha captchaInput) throws AppException {
		LOGGER.info("Entering persist()");
		captchaInput.setId(null);

		// Write out the user to the users tables
		entityManager.persist(captchaInput);

		LOGGER.info("Exiting persist()");
		return captchaInput;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.CaptchaDAO#updateCaptcha(java.lang.Long, java.lang.String)
	 */
	@Override
	@Transactional(readOnly = false)
	public void updateCaptcha(Long id, String textdata) throws AppException {
		LOGGER.info("Entering update()");
		final Captcha tempCaptcha = entityManager.find(Captcha.class, id);

		if (tempCaptcha != null) {
			tempCaptcha.setTextdata(textdata);

			// Write out the account to the account table
			entityManager.merge(tempCaptcha);
		} else {
			throw new AppException(500, AppErrorCodes.GROUP_NOT_FOUND,  
					AppErrorMessage.GROUP_NOT_FOUND);
		}

		LOGGER.info("Exiting update()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.CaptchaDAO#getCaptcha(java.lang.Long)
	 */
	@Override
	public Captcha getCaptcha(Long id) throws AppException {
		LOGGER.info("Entering getCaptcha()");
		Captcha retValue = null;

		try {
			LOGGER.debug("id: " + id);
			retValue = entityManager.find(Captcha.class, id);
			LOGGER.debug("Captcha: " + retValue);
			if (retValue == null) {
				throw new AppException(500, AppErrorCodes.GROUP_NOT_FOUND,  
						AppErrorMessage.GROUP_NOT_FOUND);				
			}
		} catch (NoResultException e) {
			LOGGER.info("NoResultException id: " + id);
			throw new AppException(500, AppErrorCodes.GROUP_NOT_FOUND,  
					AppErrorMessage.GROUP_NOT_FOUND);
		}

		LOGGER.info("Exiting getCaptcha()");
		return retValue;
	}

	/**
	 * 
	 * @param entityManager
	 */
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
}