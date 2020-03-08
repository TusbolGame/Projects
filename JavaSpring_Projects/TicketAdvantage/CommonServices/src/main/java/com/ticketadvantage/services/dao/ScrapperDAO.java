/**
 * 
 */
package com.ticketadvantage.services.dao;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import com.ticketadvantage.services.errorhandling.AppException;
import com.ticketadvantage.services.model.EmailScrapper;
import com.ticketadvantage.services.model.TwitterScrapper;
import com.ticketadvantage.services.model.WebScrapper;

/**
 * @author jmiller
 *
 */
public interface ScrapperDAO {

	/**
	 * 
	 * @param id
	 * @return
	 * @throws AppException
	 */
	public WebScrapper findWeb(Long id) throws AppException;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws AppException
	 */
	public EmailScrapper findEmail(Long id) throws AppException;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws AppException
	 */
	public TwitterScrapper findTwitter(Long id) throws AppException;

	/**
	 * 
	 * @param scrapper
	 * @return
	 * @throws AppException
	 */
	public WebScrapper persistWeb(WebScrapper scrapper) throws AppException;

	/**
	 * 
	 * @param scrapper
	 * @return
	 * @throws AppException
	 */
	public EmailScrapper persistEmail(EmailScrapper scrapper) throws AppException;

	/**
	 * 
	 * @param scrapper
	 * @return
	 * @throws AppException
	 */
	public TwitterScrapper persistTwitter(TwitterScrapper scrapper) throws AppException;

	/**
	 * 
	 * @param scrapper
	 * @return
	 * @throws AppException
	 */
	public WebScrapper mergeWeb(WebScrapper scrapper) throws AppException;

	/**
	 * 
	 * @param scrapper
	 * @return
	 * @throws AppException
	 */
	public EmailScrapper mergeEmail(EmailScrapper scrapper) throws AppException;

	/**
	 * 
	 * @param scrapper
	 * @return
	 * @throws AppException
	 */
	public TwitterScrapper mergeTwitter(TwitterScrapper scrapper) throws AppException;

	/**
	 * 
	 * @param id
	 * @param scrapper
	 * @throws AppException
	 */
	public void updateWeb(Long id, WebScrapper scrapper) throws AppException;

	/**
	 * 
	 * @param id
	 * @param scrapper
	 * @throws AppException
	 */
	public void updateEmail(Long id, EmailScrapper scrapper) throws AppException;

	/**
	 * 
	 * @param id
	 * @param scrapper
	 * @throws AppException
	 */
	public void updateTwitter(Long id, TwitterScrapper scrapper) throws AppException;

	/**
	 * 
	 * @param id
	 * @throws AppException
	 */
	public void deleteWeb(Long id) throws AppException;

	/**
	 * 
	 * @param id
	 * @throws AppException
	 */
	public void deleteEmail(Long id) throws AppException;

	/**
	 * 
	 * @param id
	 * @throws AppException
	 */
	public void deleteTwitter(Long id) throws AppException;

	/**
	 * 
	 * @return
	 * @throws AppException
	 */
	public List<WebScrapper> findAllWeb() throws AppException;

	/**
	 * 
	 * @return
	 * @throws AppException
	 */
	public List<EmailScrapper> findAllEmail() throws AppException;

	/**
	 * 
	 * @return
	 * @throws AppException
	 */
	public List<TwitterScrapper> findAllTwitter() throws AppException;

	/**
	 * 
	 * @param userid
	 * @return
	 * @throws AppException
	 */
	public Set<WebScrapper> findScrappersByUserIdWeb(Long userid) throws AppException;

	/**
	 * 
	 * @param userid
	 * @return
	 * @throws AppException
	 */
	public Set<EmailScrapper> findScrappersByUserIdEmail(Long userid) throws AppException;

	/**
	 * 
	 * @param userid
	 * @return
	 * @throws AppException
	 */
	public Set<TwitterScrapper> findScrappersByUserIdTwitter(Long userid) throws AppException;

	/**
	 * 
	 * @param id
	 * @param accountid
	 * @throws AppException
	 */
	public void addSourceAccount(Long id, Long accountid) throws AppException;

	/**
	 * 
	 * @param id
	 * @param emailaccountid
	 * @throws AppException
	 */
	public void addEmailSourceAccount(Long id, Long emailaccountid) throws AppException;

	/**
	 * 
	 * @param id
	 * @param twitteraccountid
	 * @throws AppException
	 */
	public void addTwitterSourceAccount(Long id, Long twitteraccountid) throws AppException;

	/**
	 * 
	 * @param id
	 * @param accountid
	 * @throws AppException
	 */
	public void addDestinationAccount(Long id, Long accountid) throws AppException;

	/**
	 * 
	 * @param id
	 * @param emailaccountid
	 * @throws AppException
	 */
	public void addEmailDestinationAccount(Long id, Long emailaccountid) throws AppException;

	/**
	 * 
	 * @param id
	 * @param twitteraccountid
	 * @throws AppException
	 */
	public void addTwitterDestinationAccount(Long id, Long twitteraccountid) throws AppException;

	/**
	 * 
	 * @param id
	 * @param accountid
	 * @throws AppException
	 */
	public void addMiddleAccount(Long id, Long accountid) throws AppException;

	/**
	 * 
	 * @param id
	 * @param emailaccountid
	 * @throws AppException
	 */
	public void addEmailMiddleAccount(Long id, Long emailaccountid) throws AppException;

	/**
	 * 
	 * @param id
	 * @param twitteraccountid
	 * @throws AppException
	 */
	public void addTwitterMiddleAccount(Long id, Long twitteraccountid) throws AppException;

	/**
	 * 
	 * @param id
	 * @param accountid
	 * @throws AppException
	 */
	public void addOrderAccount(Long id, Long accountid) throws AppException;

	/**
	 * 
	 * @param id
	 * @param emailaccountid
	 * @throws AppException
	 */
	public void addEmailOrderAccount(Long id, Long emailaccountid) throws AppException;

	/**
	 * 
	 * @param id
	 * @param twitteraccountid
	 * @throws AppException
	 */
	public void addTwitterOrderAccount(Long id, Long twitteraccountid) throws AppException;

	/**
	 * 
	 * @param id
	 * @param accountid
	 * @throws AppException
	 */
	public void deleteSourceAccount(Long id, Long accountid) throws AppException;

	/**
	 * 
	 * @param id
	 * @param emailaccountid
	 * @throws AppException
	 */
	public void deleteEmailSourceAccount(Long id, Long emailaccountid) throws AppException;

	/**
	 * 
	 * @param id
	 * @param twitteraccountid
	 * @throws AppException
	 */
	public void deleteTwitterSourceAccount(Long id, Long twitteraccountid) throws AppException;

	/**
	 * 
	 * @param id
	 * @param accountid
	 * @throws AppException
	 */
	public void deleteDestinationAccount(Long id, Long accountid) throws AppException;

	/**
	 * 
	 * @param id
	 * @param accountid
	 * @throws AppException
	 */
	public void deleteEmailDestinationAccount(Long id, Long accountid) throws AppException;

	/**
	 * 
	 * @param id
	 * @param accountid
	 * @throws AppException
	 */
	public void deleteTwitterDestinationAccount(Long id, Long accountid) throws AppException;

	/**
	 * 
	 * @param id
	 * @param accountid
	 * @throws AppException
	 */
	public void deleteMiddleAccount(Long id, Long accountid) throws AppException;

	/**
	 * 
	 * @param id
	 * @param accountid
	 * @throws AppException
	 */
	public void deleteEmailMiddleAccount(Long id, Long accountid) throws AppException;

	/**
	 * 
	 * @param id
	 * @param accountid
	 * @throws AppException
	 */
	public void deleteTwitterMiddleAccount(Long id, Long accountid) throws AppException;

	/**
	 * 
	 * @param id
	 * @param accountid
	 * @throws AppException
	 */
	public void deleteOrderAccount(Long id, Long accountid) throws AppException;

	/**
	 * 
	 * @param id
	 * @param accountid
	 * @throws AppException
	 */
	public void deleteEmailOrderAccount(Long id, Long accountid) throws AppException;

	/**
	 * 
	 * @param id
	 * @param accountid
	 * @throws AppException
	 */
	public void deleteTwitterOrderAccount(Long id, Long accountid) throws AppException;

	/**
	 * 
	 * @param entityManager
	 */
	public void setEntityManager(EntityManager entityManager);
}