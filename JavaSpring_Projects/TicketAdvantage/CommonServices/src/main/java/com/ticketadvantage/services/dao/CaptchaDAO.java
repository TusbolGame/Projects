/**
 * 
 */
package com.ticketadvantage.services.dao;

import javax.persistence.EntityManager;

import com.ticketadvantage.services.errorhandling.AppException;
import com.ticketadvantage.services.model.Captcha;

/**
 * @author jmiller
 *
 */
public interface CaptchaDAO {

	/**
	 * 
	 * @param captchaInput
	 * @return
	 * @throws AppException
	 */
	public Captcha setupCaptcha(Captcha captchaInput) throws AppException;

	/**
	 * 
	 * @param id
	 * @param textdata
	 * @throws AppException
	 */
	public void updateCaptcha(Long id, String textdata) throws AppException;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws AppException
	 */
	public Captcha getCaptcha(Long id) throws AppException;

	/**
	 * 
	 * @param entityManager
	 */
	public void setEntityManager(EntityManager entityManager);
}