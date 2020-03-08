/**
 * 
 */
package com.ticketadvantage.services.dao;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ticketadvantage.services.errorhandling.AppErrorCodes;
import com.ticketadvantage.services.errorhandling.AppErrorMessage;
import com.ticketadvantage.services.errorhandling.AppException;
import com.ticketadvantage.services.model.Accounts;
import com.ticketadvantage.services.model.EmailAccounts;
import com.ticketadvantage.services.model.EmailScrapper;
import com.ticketadvantage.services.model.TwitterAccounts;
import com.ticketadvantage.services.model.TwitterScrapper;
import com.ticketadvantage.services.model.WebScrapper;

/**
 * @author jmiller
 *
 */
@Repository
public class ScrapperDAOImpl implements ScrapperDAO {
	private static final Logger LOGGER = Logger.getLogger(ScrapperDAOImpl.class);

	@PersistenceContext(name = "entityManager")
	private EntityManager entityManager;

	/**
	 * 
	 */
	public ScrapperDAOImpl() {
		super();
		LOGGER.debug("Entering ScrapperDAOImpl()");
		LOGGER.debug("Exiting ScrapperDAOImpl()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.ScrapperDAO#findWeb(java.lang.Long)
	 */
	@Transactional(readOnly=true)
	public WebScrapper findWeb(Long id) throws AppException {
		LOGGER.info("Entering findWeb()");
		LOGGER.debug("id: " + id);

		final WebScrapper scrapper = entityManager.find(WebScrapper.class, id);

		LOGGER.debug("Scrapper: " + scrapper);
		LOGGER.info("Exiting findWeb()");
		return scrapper;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.ScrapperDAO#findEmail(java.lang.Long)
	 */
	@Transactional(readOnly=true)
	public EmailScrapper findEmail(Long id) throws AppException {
		LOGGER.info("Entering findEmail()");
		LOGGER.debug("id: " + id);

		final EmailScrapper scrapper = entityManager.find(EmailScrapper.class, id);

		LOGGER.debug("Scrapper: " + scrapper);
		LOGGER.info("Exiting findEmail()");
		return scrapper;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.ScrapperDAO#persistWeb(com.ticketadvantage.services.model.WebScrapper)
	 */
	@Override
	@Transactional(readOnly = false)
	public WebScrapper persistWeb(WebScrapper scrapper) throws AppException {
		LOGGER.info("Entering persistWeb()");
		LOGGER.debug("Scrapper: " + scrapper);

		@SuppressWarnings("unchecked")
		final List<WebScrapper> listScrapper = (List<WebScrapper>) entityManager
				.createQuery("SELECT s FROM WebScrapper s "
						+ "WHERE s.scrappername = :scrappername AND s.userid = :userid")
				.setParameter("scrappername", scrapper.getScrappername())
				.setParameter("userid", scrapper.getUserid())
				.getResultList();

		if ((listScrapper == null) || (listScrapper != null && listScrapper.size() == 0)) {
			LOGGER.debug("Persisting WebScrapper");
			// Write out the user to the users tables
			scrapper.setId(null);
			entityManager.persist(scrapper);
		} else if (listScrapper != null && listScrapper.size() > 0) {
			LOGGER.debug("Already a WebScrapper");
			scrapper = listScrapper.get(0);
		}

		LOGGER.info("Exiting persistWeb()");
		return scrapper;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.ScrapperDAO#persistEmail(com.ticketadvantage.services.model.EmailScrapper)
	 */
	@Override
	@Transactional(readOnly = false)
	public EmailScrapper persistEmail(EmailScrapper scrapper) throws AppException {
		LOGGER.info("Entering persistEmail()");
		LOGGER.debug("Scrapper: " + scrapper);

		@SuppressWarnings("unchecked")
		final List<EmailScrapper> listScrapper = (List<EmailScrapper>) entityManager
				.createQuery("SELECT s FROM EmailScrapper s "
						+ "WHERE s.scrappername = :scrappername AND s.userid = :userid")
				.setParameter("scrappername", scrapper.getScrappername())
				.setParameter("userid", scrapper.getUserid())
				.getResultList();

		if ((listScrapper == null) || (listScrapper != null && listScrapper.size() == 0)) {
			LOGGER.debug("Persisting EmailScrapper");
			// Write out the user to the users tables
			scrapper.setId(null);
			entityManager.persist(scrapper);
		} else if (listScrapper != null && listScrapper.size() > 0) {
			LOGGER.debug("Already a EmailScrapper");
			scrapper = listScrapper.get(0);
		}
		LOGGER.info("Exiting persistEmail()");
		return scrapper;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.ScrapperDAO#mergeWeb(com.ticketadvantage.services.model.WebScrapper)
	 */
	@Override
	@Transactional(readOnly = false)
	public WebScrapper mergeWeb(WebScrapper scrapper) throws AppException {
		LOGGER.info("Entering mergeWeb()");
		LOGGER.debug("WebScrapper: " + scrapper);

		entityManager.merge(scrapper);

		LOGGER.info("Exiting mergeWeb()");
		return scrapper;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.ScrapperDAO#mergeEmail(com.ticketadvantage.services.model.EmailScrapper)
	 */
	@Override
	@Transactional(readOnly = false)
	public EmailScrapper mergeEmail(EmailScrapper scrapper) throws AppException {
		LOGGER.info("Entering mergeEmail()");
		LOGGER.debug("EmailScrapper: " + scrapper);

		entityManager.merge(scrapper);

		LOGGER.info("Exiting mergeEmail()");
		return scrapper;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.ScrapperDAO#updateWeb(java.lang.Long, com.ticketadvantage.services.model.WebScrapper)
	 */
	@Override
	@Transactional(readOnly = false)
	public void updateWeb(Long id, WebScrapper scrapper) throws AppException {
		LOGGER.info("Entering updateWeb()");
		LOGGER.debug("id: " + id);
		LOGGER.debug("WebScrapper: " + scrapper);

		WebScrapper tempScrapper = entityManager.find(WebScrapper.class, id);
//		tempScrapper = copyPendingEvent(scrapper, tempScrapper);
		LOGGER.debug("WebScrapper: " + tempScrapper);
		entityManager.merge(tempScrapper);

		LOGGER.info("Exiting updateWeb()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.ScrapperDAO#updateEmail(java.lang.Long, com.ticketadvantage.services.model.EmailScrapper)
	 */
	@Override
	@Transactional(readOnly = false)
	public void updateEmail(Long id, EmailScrapper scrapper) throws AppException {
		LOGGER.info("Entering updateEmail()");
		LOGGER.debug("id: " + id);
		LOGGER.debug("Scrapper: " + scrapper);

		EmailScrapper tempScrapper = entityManager.find(EmailScrapper.class, id);
//		tempScrapper = copyPendingEvent(scrapper, tempScrapper);
		LOGGER.debug("Scrapper: " + tempScrapper);

		entityManager.merge(tempScrapper);

		LOGGER.info("Exiting updateEmail()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.ScrapperDAO#deleteWeb(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = false)
	public void deleteWeb(Long id) throws AppException {
		LOGGER.info("Entering deleteWeb()");

		final WebScrapper scrapper = entityManager.find(WebScrapper.class, id);
		if (scrapper != null) {				
			// Remove the Scrapper
			entityManager.remove(scrapper);
		} else {
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}

		LOGGER.info("Exiting deleteWeb()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.ScrapperDAO#deleteEmail(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = false)
	public void deleteEmail(Long id) throws AppException {
		LOGGER.info("Entering deleteEmail()");

		final EmailScrapper scrapper = entityManager.find(EmailScrapper.class, id);
		if (scrapper != null) {				
			// Remove the Scrapper
			entityManager.remove(scrapper);
		} else {
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}

		LOGGER.info("Exiting deleteEmail()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.ScrapperDAO#findAllWeb()
	 */
	@Override
	@Transactional(readOnly = true)
	public List<WebScrapper> findAllWeb() throws AppException {
		LOGGER.info("Entering findAllWeb()");

		@SuppressWarnings("unchecked")
		final List<WebScrapper> retValue = entityManager.createQuery("SELECT s FROM WebScrapper s").getResultList();

		// Compare the dates to order them appropriately
		Collections.sort(retValue, new Comparator<WebScrapper>() {
			public int compare(WebScrapper o1, WebScrapper o2) {
				if (o1.getScrappername() == null || o2.getScrappername() == null)
					return 0;
				return o1.getScrappername().compareTo(o2.getScrappername());
			}
		});

		LOGGER.info("Exiting findAllWeb()");
		return retValue;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.ScrapperDAO#findAllEmail()
	 */
	@Override
	@Transactional(readOnly = true)
	public List<EmailScrapper> findAllEmail() throws AppException {
		LOGGER.info("Entering findAll()");

		@SuppressWarnings("unchecked")
		final List<EmailScrapper> retValue = entityManager.createQuery("SELECT s FROM EmailScrapper s").getResultList();

		// Compare the dates to order them appropriately
		Collections.sort(retValue, new Comparator<EmailScrapper>() {
			public int compare(EmailScrapper o1, EmailScrapper o2) {
				if (o1.getScrappername() == null || o2.getScrappername() == null)
					return 0;
				return o1.getScrappername().compareTo(o2.getScrappername());
			}
		});

		LOGGER.info("Exiting findAll()");
		return retValue;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.ScrapperDAO#findScrappersByUserIdWeb(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = true)
	public Set<WebScrapper> findScrappersByUserIdWeb(Long userid) throws AppException {
		LOGGER.info("Entering findScrappersByUserIdWeb()");

		@SuppressWarnings("unchecked")
		final List<WebScrapper> listScrapper = (List<WebScrapper>) entityManager
				.createQuery("SELECT s FROM WebScrapper s WHERE s.userid = :userid")
				.setParameter("userid", userid)
				.getResultList();
		
		// Compare the dates to order them appropriately
		Collections.sort(listScrapper, new Comparator<WebScrapper>() {
			public int compare(WebScrapper o1, WebScrapper o2) {
				if (o1.getScrappername() == null || o2.getScrappername() == null)
					return 0;
				return o1.getScrappername().compareTo(o2.getScrappername());
			}
		});

		final Set<WebScrapper> scrapperSet = new LinkedHashSet<WebScrapper>(listScrapper);

		LOGGER.info("Exiting findScrappersByUserIdWeb()");
		return scrapperSet;		
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.ScrapperDAO#findScrappersByUserIdEmail(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = true)
	public Set<EmailScrapper> findScrappersByUserIdEmail(Long userid) throws AppException {
		LOGGER.info("Entering findScrappersByUserIdEmail()");

		@SuppressWarnings("unchecked")
		final List<EmailScrapper> listScrapper = (List<EmailScrapper>) entityManager
				.createQuery("SELECT s FROM EmailScrapper s WHERE s.userid = :userid")
				.setParameter("userid", userid)
				.getResultList();

		// Compare the dates to order them appropriately
		Collections.sort(listScrapper, new Comparator<EmailScrapper>() {
			public int compare(EmailScrapper o1, EmailScrapper o2) {
				if (o1.getScrappername() == null || o2.getScrappername() == null)
					return 0;
				return o1.getScrappername().compareTo(o2.getScrappername());
			}
		});

		final Set<EmailScrapper> scrapperSet = new LinkedHashSet<EmailScrapper>(listScrapper);

		LOGGER.info("Exiting findScrappersByUserIdEmail()");
		return scrapperSet;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.ScrapperDAO#addSourceAccount(java.lang.Long, java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = false)
	public void addSourceAccount(Long id, Long accountid) {
		LOGGER.info("Entering addSourceAccount()");

		final WebScrapper scrapper = findWeb(id);
		if (scrapper != null) {
			final Accounts account = entityManager.find(Accounts.class, accountid);
			if (account != null) {
				Set<Accounts> accounts = scrapper.getSources();
				if (accounts != null && accounts.size() > 0) {
					accounts.add(account);
				} else {
					accounts = new HashSet<Accounts>();
					accounts.add(account);			
				}
				scrapper.setSources(accounts);
				entityManager.merge(scrapper);
			} else {
				throw new AppException(500, AppErrorCodes.ACCOUNT_NOT_FOUND,  
						AppErrorMessage.ACCOUNT_NOT_FOUND);
			}
		} else {
			throw new AppException(500, AppErrorCodes.USER_NOT_FOUND,  
					AppErrorMessage.USER_NOT_FOUND);
		}

		LOGGER.info("Exiting addSourceAccount()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.ScrapperDAO#addEmailSourceAccount(java.lang.Long, java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = false)
	public void addEmailSourceAccount(Long id, Long emailaccountid) throws AppException {
		LOGGER.info("Entering addSourceAccount()");

		final EmailScrapper scrapper = findEmail(id);
		if (scrapper != null) {
			final EmailAccounts account = entityManager.find(EmailAccounts.class, emailaccountid);
			if (account != null) {
				Set<EmailAccounts> accounts = scrapper.getSources();
				if (accounts != null && accounts.size() > 0) {
					accounts.add(account);
				} else {
					accounts = new HashSet<EmailAccounts>();
					accounts.add(account);			
				}
				scrapper.setSources(accounts);
				entityManager.merge(scrapper);
			} else {
				throw new AppException(500, AppErrorCodes.ACCOUNT_NOT_FOUND,  
						AppErrorMessage.ACCOUNT_NOT_FOUND);
			}
		} else {
			throw new AppException(500, AppErrorCodes.USER_NOT_FOUND,  
					AppErrorMessage.USER_NOT_FOUND);
		}

		LOGGER.info("Exiting addSourceAccount()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.ScrapperDAO#addDestinationAccount(java.lang.Long, java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = false)
	public void addDestinationAccount(Long id, Long accountid) {
		LOGGER.info("Entering addDestinationAccount()");

		final WebScrapper scrapper = findWeb(id);
		if (scrapper != null) {
			final Accounts account = entityManager.find(Accounts.class, accountid);
			if (account != null) {
				Set<Accounts> accounts = scrapper.getDestinations();
				if (accounts != null && accounts.size() > 0) {
					accounts.add(account);
				} else {
					accounts = new HashSet<Accounts>();
					accounts.add(account);			
				}
				scrapper.setDestinations(accounts);
				entityManager.merge(scrapper);
			} else {
				throw new AppException(500, AppErrorCodes.ACCOUNT_NOT_FOUND,  
						AppErrorMessage.ACCOUNT_NOT_FOUND);
			}
		} else {
			throw new AppException(500, AppErrorCodes.USER_NOT_FOUND,  
					AppErrorMessage.USER_NOT_FOUND);
		}

		LOGGER.info("Exiting addDestinationAccount()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.ScrapperDAO#addEmailDestinationAccount(java.lang.Long, java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = false)
	public void addEmailDestinationAccount(Long id, Long accountid) {
		LOGGER.info("Entering addEmailDestinationAccount()");

		final EmailScrapper scrapper = findEmail(id);
		if (scrapper != null) {
			final Accounts account = entityManager.find(Accounts.class, accountid);
			if (account != null) {
				Set<Accounts> accounts = scrapper.getDestinations();
				if (accounts != null && accounts.size() > 0) {
					accounts.add(account);
				} else {
					accounts = new HashSet<Accounts>();
					accounts.add(account);			
				}
				scrapper.setDestinations(accounts);
				entityManager.merge(scrapper);
			} else {
				throw new AppException(500, AppErrorCodes.ACCOUNT_NOT_FOUND,  
						AppErrorMessage.ACCOUNT_NOT_FOUND);
			}
		} else {
			throw new AppException(500, AppErrorCodes.USER_NOT_FOUND,  
					AppErrorMessage.USER_NOT_FOUND);
		}

		LOGGER.info("Exiting addEmailDestinationAccount()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.ScrapperDAO#addMiddleAccount(java.lang.Long, java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = false)
	public void addMiddleAccount(Long id, Long accountid) throws AppException {
		LOGGER.info("Entering addMiddleAccount()");

		final WebScrapper scrapper = findWeb(id);
		if (scrapper != null) {
			final Accounts account = entityManager.find(Accounts.class, accountid);
			if (account != null) {
				Set<Accounts> accounts = scrapper.getMiddledestinations();
				if (accounts != null && accounts.size() > 0) {
					accounts.add(account);
				} else {
					accounts = new HashSet<Accounts>();
					accounts.add(account);			
				}
				scrapper.setMiddledestinations(accounts);
				entityManager.merge(scrapper);
			} else {
				throw new AppException(500, AppErrorCodes.ACCOUNT_NOT_FOUND,  
						AppErrorMessage.ACCOUNT_NOT_FOUND);
			}
		} else {
			throw new AppException(500, AppErrorCodes.USER_NOT_FOUND,  
					AppErrorMessage.USER_NOT_FOUND);
		}

		LOGGER.info("Exiting addMiddleAccount()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.ScrapperDAO#addEmailMiddleAccount(java.lang.Long, java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = false)
	public void addEmailMiddleAccount(Long id, Long accountid) throws AppException {
		LOGGER.info("Entering addEmailMiddleAccount()");

		final EmailScrapper scrapper = findEmail(id);
		if (scrapper != null) {
			final Accounts account = entityManager.find(Accounts.class, accountid);
			if (account != null) {
				Set<Accounts> accounts = scrapper.getEmailmiddledestinations();
				if (accounts != null && accounts.size() > 0) {
					accounts.add(account);
				} else {
					accounts = new HashSet<Accounts>();
					accounts.add(account);			
				}
				scrapper.setEmailmiddledestinations(accounts);
				entityManager.merge(scrapper);
			} else {
				throw new AppException(500, AppErrorCodes.ACCOUNT_NOT_FOUND,  
						AppErrorMessage.ACCOUNT_NOT_FOUND);
			}
		} else {
			throw new AppException(500, AppErrorCodes.USER_NOT_FOUND,  
					AppErrorMessage.USER_NOT_FOUND);
		}

		LOGGER.info("Exiting addEmailMiddleAccount()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.ScrapperDAO#addOrderAccount(java.lang.Long, java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = false)
	public void addOrderAccount(Long id, Long accountid) throws AppException {
		LOGGER.info("Entering addOrderAccount()");

		final WebScrapper scrapper = findWeb(id);
		if (scrapper != null) {
			final Accounts account = entityManager.find(Accounts.class, accountid);
			if (account != null) {
				Set<Accounts> accounts = scrapper.getOrderdestinations();
				if (accounts != null && accounts.size() > 0) {
					accounts.add(account);
				} else {
					accounts = new HashSet<Accounts>();
					accounts.add(account);			
				}
				scrapper.setOrderdestinations(accounts);
				entityManager.merge(scrapper);
			} else {
				throw new AppException(500, AppErrorCodes.ACCOUNT_NOT_FOUND,  
						AppErrorMessage.ACCOUNT_NOT_FOUND);
			}
		} else {
			throw new AppException(500, AppErrorCodes.USER_NOT_FOUND,  
					AppErrorMessage.USER_NOT_FOUND);
		}

		LOGGER.info("Exiting addOrderAccount()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.ScrapperDAO#addEmailOrderAccount(java.lang.Long, java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = false)
	public void addEmailOrderAccount(Long id, Long accountid) throws AppException {
		LOGGER.info("Entering addEmailOrderAccount()");

		final EmailScrapper scrapper = findEmail(id);
		if (scrapper != null) {
			final Accounts account = entityManager.find(Accounts.class, accountid);
			if (account != null) {
				Set<Accounts> accounts = scrapper.getEmailorderdestinations();
				if (accounts != null && accounts.size() > 0) {
					accounts.add(account);
				} else {
					accounts = new HashSet<Accounts>();
					accounts.add(account);			
				}
				scrapper.setEmailorderdestinations(accounts);
				entityManager.merge(scrapper);
			} else {
				throw new AppException(500, AppErrorCodes.ACCOUNT_NOT_FOUND,  
						AppErrorMessage.ACCOUNT_NOT_FOUND);
			}
		} else {
			throw new AppException(500, AppErrorCodes.USER_NOT_FOUND,  
					AppErrorMessage.USER_NOT_FOUND);
		}

		LOGGER.info("Exiting addEmailOrderAccount()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.ScrapperDAO#deleteSourceAccount(java.lang.Long, java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = false)
	public void deleteSourceAccount(Long id, Long accountid) throws AppException {
		LOGGER.info("Entering deleteSourceAccount()");

		WebScrapper scrapper = entityManager.find(WebScrapper.class, id);
		if (scrapper != null) {
			Accounts account = entityManager.find(Accounts.class, accountid);
			if (account != null) {
				Set<Accounts> accounts = scrapper.getSources();
				if (accounts != null && accounts.size() > 0) {
					accounts.remove(account);
				}
				scrapper.setSources(accounts);
				scrapper = entityManager.merge(scrapper);
			} else {
				throw new AppException(500, AppErrorCodes.ACCOUNT_NOT_FOUND,  
						AppErrorMessage.ACCOUNT_NOT_FOUND);
			}
		} else {
			throw new AppException(500, AppErrorCodes.SCRAPPER_NOT_FOUND,  
					AppErrorMessage.SCRAPPER_NOT_FOUND);
		}

		LOGGER.info("Exiting deleteSourceAccount()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.ScrapperDAO#deleteEmailSourceAccount(java.lang.Long, java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = false)
	public void deleteEmailSourceAccount(Long id, Long emailaccountid) throws AppException {
		LOGGER.info("Entering deleteEmailSourceAccount()");

		EmailScrapper scrapper = entityManager.find(EmailScrapper.class, id);
		if (scrapper != null) {
			EmailAccounts account = entityManager.find(EmailAccounts.class, emailaccountid);
			if (account != null) {
				Set<EmailAccounts> accounts = scrapper.getSources();
				if (accounts != null && accounts.size() > 0) {
					accounts.remove(account);
				}
				scrapper.setSources(accounts);
				scrapper = entityManager.merge(scrapper);
			} else {
				throw new AppException(500, AppErrorCodes.ACCOUNT_NOT_FOUND,  
						AppErrorMessage.ACCOUNT_NOT_FOUND);
			}
		} else {
			throw new AppException(500, AppErrorCodes.SCRAPPER_NOT_FOUND,  
					AppErrorMessage.SCRAPPER_NOT_FOUND);
		}

		LOGGER.info("Exiting deleteEmailSourceAccount()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.ScrapperDAO#deleteDestinationAccount(java.lang.Long, java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = false)
	public void deleteDestinationAccount(Long id, Long accountid) throws AppException {
		LOGGER.info("Entering deleteDestinationAccount()");

		WebScrapper scrapper = entityManager.find(WebScrapper.class, id);
		if (scrapper != null) {
			Accounts account = entityManager.find(Accounts.class, accountid);
			if (account != null) {
				Set<Accounts> accounts = scrapper.getDestinations();
				if (accounts != null && accounts.size() > 0) {
					accounts.remove(account);
				}
				scrapper.setDestinations(accounts);
				scrapper = entityManager.merge(scrapper);
			} else {
				throw new AppException(500, AppErrorCodes.ACCOUNT_NOT_FOUND,  
						AppErrorMessage.ACCOUNT_NOT_FOUND);
			}
		} else {
			throw new AppException(500, AppErrorCodes.SCRAPPER_NOT_FOUND,  
					AppErrorMessage.SCRAPPER_NOT_FOUND);
		}

		LOGGER.info("Exiting deleteDestinationAccount()");		
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.ScrapperDAO#deleteEmailDestinationAccount(java.lang.Long, java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = false)
	public void deleteEmailDestinationAccount(Long id, Long accountid) throws AppException {
		LOGGER.info("Entering deleteEmailDestinationAccount()");

		EmailScrapper scrapper = entityManager.find(EmailScrapper.class, id);
		if (scrapper != null) {
			Accounts account = entityManager.find(Accounts.class, accountid);
			if (account != null) {
				Set<Accounts> accounts = scrapper.getDestinations();
				if (accounts != null && accounts.size() > 0) {
					accounts.remove(account);
				}
				scrapper.setDestinations(accounts);
				scrapper = entityManager.merge(scrapper);
			} else {
				throw new AppException(500, AppErrorCodes.ACCOUNT_NOT_FOUND,  
						AppErrorMessage.ACCOUNT_NOT_FOUND);
			}
		} else {
			throw new AppException(500, AppErrorCodes.SCRAPPER_NOT_FOUND,  
					AppErrorMessage.SCRAPPER_NOT_FOUND);
		}

		LOGGER.info("Exiting deleteEmailDestinationAccount()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.ScrapperDAO#deleteMiddleAccount(java.lang.Long, java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = false)
	public void deleteMiddleAccount(Long id, Long accountid) throws AppException {
		LOGGER.info("Entering deleteMiddleAccount()");

		WebScrapper scrapper = entityManager.find(WebScrapper.class, id);
		if (scrapper != null) {
			Accounts account = entityManager.find(Accounts.class, accountid);
			if (account != null) {
				Set<Accounts> accounts = scrapper.getMiddledestinations();
				if (accounts != null && accounts.size() > 0) {
					accounts.remove(account);
				}
				scrapper.setMiddledestinations(accounts);
				scrapper = entityManager.merge(scrapper);
			} else {
				throw new AppException(500, AppErrorCodes.ACCOUNT_NOT_FOUND,  
						AppErrorMessage.ACCOUNT_NOT_FOUND);
			}
		} else {
			throw new AppException(500, AppErrorCodes.SCRAPPER_NOT_FOUND,  
					AppErrorMessage.SCRAPPER_NOT_FOUND);
		}

		LOGGER.info("Exiting deleteMiddleAccount()");		
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.ScrapperDAO#deleteEmailMiddleAccount(java.lang.Long, java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = false)
	public void deleteEmailMiddleAccount(Long id, Long accountid) throws AppException {
		LOGGER.info("Entering deleteEmailMiddleAccount()");

		EmailScrapper scrapper = entityManager.find(EmailScrapper.class, id);
		if (scrapper != null) {
			Accounts account = entityManager.find(Accounts.class, accountid);
			if (account != null) {
				Set<Accounts> accounts = scrapper.getEmailmiddledestinations();
				if (accounts != null && accounts.size() > 0) {
					accounts.remove(account);
				}
				scrapper.setEmailmiddledestinations(accounts);
				scrapper = entityManager.merge(scrapper);
			} else {
				throw new AppException(500, AppErrorCodes.ACCOUNT_NOT_FOUND,  
						AppErrorMessage.ACCOUNT_NOT_FOUND);
			}
		} else {
			throw new AppException(500, AppErrorCodes.SCRAPPER_NOT_FOUND,  
					AppErrorMessage.SCRAPPER_NOT_FOUND);
		}

		LOGGER.info("Exiting deleteEmailMiddleAccount()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.ScrapperDAO#deleteOrderAccount(java.lang.Long, java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = false)
	public void deleteOrderAccount(Long id, Long accountid) throws AppException {
		LOGGER.info("Entering deleteOrderAccount()");

		WebScrapper scrapper = entityManager.find(WebScrapper.class, id);
		if (scrapper != null) {
			Accounts account = entityManager.find(Accounts.class, accountid);
			if (account != null) {
				Set<Accounts> accounts = scrapper.getOrderdestinations();
				if (accounts != null && accounts.size() > 0) {
					accounts.remove(account);
				}
				scrapper.setOrderdestinations(accounts);
				scrapper = entityManager.merge(scrapper);
			} else {
				throw new AppException(500, AppErrorCodes.ACCOUNT_NOT_FOUND,  
						AppErrorMessage.ACCOUNT_NOT_FOUND);
			}
		} else {
			throw new AppException(500, AppErrorCodes.SCRAPPER_NOT_FOUND,  
					AppErrorMessage.SCRAPPER_NOT_FOUND);
		}

		LOGGER.info("Exiting deleteOrderAccount()");		
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.ScrapperDAO#deleteEmailOrderAccount(java.lang.Long, java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = false)
	public void deleteEmailOrderAccount(Long id, Long accountid) throws AppException {
		LOGGER.info("Entering deleteEmailOrderAccount()");

		EmailScrapper scrapper = entityManager.find(EmailScrapper.class, id);
		if (scrapper != null) {
			Accounts account = entityManager.find(Accounts.class, accountid);
			if (account != null) {
				Set<Accounts> accounts = scrapper.getEmailorderdestinations();
				if (accounts != null && accounts.size() > 0) {
					accounts.remove(account);
				}
				scrapper.setEmailorderdestinations(accounts);
				scrapper = entityManager.merge(scrapper);
			} else {
				throw new AppException(500, AppErrorCodes.ACCOUNT_NOT_FOUND,  
						AppErrorMessage.ACCOUNT_NOT_FOUND);
			}
		} else {
			throw new AppException(500, AppErrorCodes.SCRAPPER_NOT_FOUND,  
					AppErrorMessage.SCRAPPER_NOT_FOUND);
		}

		LOGGER.info("Exiting deleteEmailOrderAccount()");		
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.RecordEventDAO#setEntityManager(javax.persistence.EntityManager)
	 */
	@Override
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.ScrapperDAO#findTwitter(java.lang.Long)
	 */
	@Override
	public TwitterScrapper findTwitter(Long id) throws AppException {
		LOGGER.info("Entering findTwitter()");
		LOGGER.debug("id: " + id);

		final TwitterScrapper scrapper = entityManager.find(TwitterScrapper.class, id);

		LOGGER.debug("Scrapper: " + scrapper);
		LOGGER.info("Exiting findTwitter()");
		return scrapper;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.ScrapperDAO#persistTwitter(com.ticketadvantage.services.model.TwitterScrapper)
	 */
	@Override
	@Transactional(readOnly = false)
	public TwitterScrapper persistTwitter(TwitterScrapper scrapper) throws AppException {
		LOGGER.info("Entering persistTwitter()");
		LOGGER.debug("Scrapper: " + scrapper);

		@SuppressWarnings("unchecked")
		final List<TwitterScrapper> listScrapper = (List<TwitterScrapper>) entityManager
				.createQuery("SELECT s FROM TwitterScrapper s "
						+ "WHERE s.scrappername = :scrappername AND s.userid = :userid")
				.setParameter("scrappername", scrapper.getScrappername())
				.setParameter("userid", scrapper.getUserid())
				.getResultList();

		if ((listScrapper == null) || (listScrapper != null && listScrapper.size() == 0)) {
			LOGGER.debug("Persisting TwitterScrapper");
			// Write out the user to the users tables
			scrapper.setId(null);
			entityManager.persist(scrapper);
		} else if (listScrapper != null && listScrapper.size() > 0) {
			LOGGER.debug("Already a TwitterScrapper");
			scrapper = listScrapper.get(0);
		}

		LOGGER.info("Exiting persistTwitter()");
		return scrapper;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.ScrapperDAO#mergeTwitter(com.ticketadvantage.services.model.TwitterScrapper)
	 */
	@Override
	@Transactional(readOnly = false)
	public TwitterScrapper mergeTwitter(TwitterScrapper scrapper) throws AppException {
		LOGGER.info("Entering mergeTwitter()");
		LOGGER.debug("TwitterScrapper: " + scrapper);

		entityManager.merge(scrapper);

		LOGGER.info("Exiting mergeTwitter()");
		return scrapper;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.ScrapperDAO#updateTwitter(java.lang.Long, com.ticketadvantage.services.model.TwitterScrapper)
	 */
	@Override
	@Transactional(readOnly = false)
	public void updateTwitter(Long id, TwitterScrapper scrapper) throws AppException {
		LOGGER.info("Entering updateTwitter()");
		LOGGER.debug("id: " + id);
		LOGGER.debug("Scrapper: " + scrapper);

		TwitterScrapper tempScrapper = entityManager.find(TwitterScrapper.class, id);
		LOGGER.debug("Scrapper: " + tempScrapper);

		entityManager.merge(tempScrapper);

		LOGGER.info("Exiting updateTwitter()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.ScrapperDAO#deleteTwitter(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = false)
	public void deleteTwitter(Long id) throws AppException {
		LOGGER.info("Entering deleteTwitter()");

		final TwitterScrapper scrapper = entityManager.find(TwitterScrapper.class, id);
		if (scrapper != null) {				
			// Remove the Scrapper
			entityManager.remove(scrapper);
		} else {
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}

		LOGGER.info("Exiting deleteTwitter()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.ScrapperDAO#findAllTwitter()
	 */
	@Override
	public List<TwitterScrapper> findAllTwitter() throws AppException {
		LOGGER.info("Entering findAll()");

		@SuppressWarnings("unchecked")
		final List<TwitterScrapper> retValue = entityManager.createQuery("SELECT s FROM TwitterScrapper s").getResultList();

		// Compare the dates to order them appropriately
		Collections.sort(retValue, new Comparator<TwitterScrapper>() {
			public int compare(TwitterScrapper o1, TwitterScrapper o2) {
				if (o1.getScrappername() == null || o2.getScrappername() == null)
					return 0;
				return o1.getScrappername().compareTo(o2.getScrappername());
			}
		});

		LOGGER.info("Exiting findAll()");
		return retValue;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.ScrapperDAO#findScrappersByUserIdTwitter(java.lang.Long)
	 */
	@Override
	public Set<TwitterScrapper> findScrappersByUserIdTwitter(Long userid) throws AppException {
		LOGGER.info("Entering findScrappersByUserIdTwitter()");

		@SuppressWarnings("unchecked")
		final List<TwitterScrapper> listScrapper = (List<TwitterScrapper>) entityManager
				.createQuery("SELECT s FROM TwitterScrapper s WHERE s.userid = :userid")
				.setParameter("userid", userid)
				.getResultList();
		
		// Compare the dates to order them appropriately
		Collections.sort(listScrapper, new Comparator<TwitterScrapper>() {
			public int compare(TwitterScrapper o1, TwitterScrapper o2) {
				if (o1.getScrappername() == null || o2.getScrappername() == null)
					return 0;
				return o1.getScrappername().compareTo(o2.getScrappername());
			}
		});

		final Set<TwitterScrapper> scrapperSet = new LinkedHashSet<TwitterScrapper>(listScrapper);

		LOGGER.info("Exiting findScrappersByUserIdTwitter()");
		return scrapperSet;	
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.ScrapperDAO#addTwitterSourceAccount(java.lang.Long, java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = false)
	public void addTwitterSourceAccount(Long id, Long twitteraccountid) throws AppException {
		LOGGER.info("Entering addTwitterSourceAccount()");

		final TwitterScrapper scrapper = findTwitter(id);
		if (scrapper != null) {
			final TwitterAccounts account = entityManager.find(TwitterAccounts.class, twitteraccountid);
			if (account != null) {
				Set<TwitterAccounts> accounts = scrapper.getSources();

				if (accounts != null && accounts.size() > 0) {
					accounts.add(account);
				} else {
					accounts = new HashSet<TwitterAccounts>();
					accounts.add(account);			
				}

				scrapper.setSources(accounts);
				entityManager.merge(scrapper);
			} else {
				throw new AppException(500, AppErrorCodes.ACCOUNT_NOT_FOUND,  
						AppErrorMessage.ACCOUNT_NOT_FOUND);
			}
		} else {
			throw new AppException(500, AppErrorCodes.USER_NOT_FOUND,  
					AppErrorMessage.USER_NOT_FOUND);
		}

		LOGGER.info("Exiting addTwitterSourceAccount()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.ScrapperDAO#addTwitterDestinationAccount(java.lang.Long, java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = false)
	public void addTwitterDestinationAccount(Long id, Long twitteraccountid) throws AppException {
		LOGGER.info("Entering addTwitterDestinationAccount()");

		final TwitterScrapper scrapper = findTwitter(id);
		if (scrapper != null) {
			final Accounts account = entityManager.find(Accounts.class, twitteraccountid);
			if (account != null) {
				Set<Accounts> accounts = scrapper.getDestinations();
				if (accounts != null && accounts.size() > 0) {
					accounts.add(account);
				} else {
					accounts = new HashSet<Accounts>();
					accounts.add(account);			
				}
				scrapper.setDestinations(accounts);
				entityManager.merge(scrapper);
			} else {
				throw new AppException(500, AppErrorCodes.ACCOUNT_NOT_FOUND,  
						AppErrorMessage.ACCOUNT_NOT_FOUND);
			}
		} else {
			throw new AppException(500, AppErrorCodes.USER_NOT_FOUND,  
					AppErrorMessage.USER_NOT_FOUND);
		}

		LOGGER.info("Exiting addTwitterDestinationAccount()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.ScrapperDAO#addTwitterMiddleAccount(java.lang.Long, java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = false)
	public void addTwitterMiddleAccount(Long id, Long twitteraccountid) throws AppException {
		LOGGER.info("Entering addTwitterMiddleAccount()");

		final TwitterScrapper scrapper = findTwitter(id);
		if (scrapper != null) {
			final Accounts account = entityManager.find(Accounts.class, twitteraccountid);
			if (account != null) {
				Set<Accounts> accounts = scrapper.getTwittermiddledestinations();
				if (accounts != null && accounts.size() > 0) {
					accounts.add(account);
				} else {
					accounts = new HashSet<Accounts>();
					accounts.add(account);			
				}

				scrapper.setTwittermiddledestinations(accounts);
				entityManager.merge(scrapper);
			} else {
				throw new AppException(500, AppErrorCodes.ACCOUNT_NOT_FOUND,  
						AppErrorMessage.ACCOUNT_NOT_FOUND);
			}
		} else {
			throw new AppException(500, AppErrorCodes.USER_NOT_FOUND,  
					AppErrorMessage.USER_NOT_FOUND);
		}

		LOGGER.info("Exiting addTwitterMiddleAccount()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.ScrapperDAO#addTwitterOrderAccount(java.lang.Long, java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = false)
	public void addTwitterOrderAccount(Long id, Long twitteraccountid) throws AppException {
		LOGGER.info("Entering addTwitterOrderAccount()");

		final TwitterScrapper scrapper = findTwitter(id);
		if (scrapper != null) {
			final Accounts account = entityManager.find(Accounts.class, twitteraccountid);
			if (account != null) {
				Set<Accounts> accounts = scrapper.getTwitterorderdestinations();
				if (accounts != null && accounts.size() > 0) {
					accounts.add(account);
				} else {
					accounts = new HashSet<Accounts>();
					accounts.add(account);			
				}
				scrapper.setTwitterorderdestinations(accounts);
				entityManager.merge(scrapper);
			} else {
				throw new AppException(500, AppErrorCodes.ACCOUNT_NOT_FOUND,  
						AppErrorMessage.ACCOUNT_NOT_FOUND);
			}
		} else {
			throw new AppException(500, AppErrorCodes.USER_NOT_FOUND,  
					AppErrorMessage.USER_NOT_FOUND);
		}

		LOGGER.info("Exiting addTwitterOrderAccount()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.ScrapperDAO#deleteTwitterSourceAccount(java.lang.Long, java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = false)
	public void deleteTwitterSourceAccount(Long id, Long twitteraccountid) throws AppException {
		LOGGER.info("Entering deleteTwitterSourceAccount()");

		final TwitterScrapper scrapper = findTwitter(id);
		if (scrapper != null) {
			final TwitterAccounts account = entityManager.find(TwitterAccounts.class, twitteraccountid);
			if (account != null) {
				Set<TwitterAccounts> accounts = scrapper.getSources();
				if (accounts != null && accounts.size() > 0) {
					accounts.add(account);
				} else {
					accounts = new HashSet<TwitterAccounts>();
					accounts.add(account);			
				}
				scrapper.setSources(accounts);
				entityManager.merge(scrapper);
			} else {
				throw new AppException(500, AppErrorCodes.ACCOUNT_NOT_FOUND,  
						AppErrorMessage.ACCOUNT_NOT_FOUND);
			}
		} else {
			throw new AppException(500, AppErrorCodes.USER_NOT_FOUND,  
					AppErrorMessage.USER_NOT_FOUND);
		}

		LOGGER.info("Exiting deleteTwitterSourceAccount()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.ScrapperDAO#deleteTwitterDestinationAccount(java.lang.Long, java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = false)
	public void deleteTwitterDestinationAccount(Long id, Long accountid) throws AppException {
		LOGGER.info("Entering deleteEmailDestinationAccount()");

		TwitterScrapper scrapper = entityManager.find(TwitterScrapper.class, id);
		if (scrapper != null) {
			Accounts account = entityManager.find(Accounts.class, accountid);
			if (account != null) {
				Set<Accounts> accounts = scrapper.getDestinations();
				if (accounts != null && accounts.size() > 0) {
					accounts.remove(account);
				}
				scrapper.setDestinations(accounts);
				scrapper = entityManager.merge(scrapper);
			} else {
				throw new AppException(500, AppErrorCodes.ACCOUNT_NOT_FOUND,  
						AppErrorMessage.ACCOUNT_NOT_FOUND);
			}
		} else {
			throw new AppException(500, AppErrorCodes.SCRAPPER_NOT_FOUND,  
					AppErrorMessage.SCRAPPER_NOT_FOUND);
		}

		LOGGER.info("Exiting deleteEmailDestinationAccount()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.ScrapperDAO#deleteTwitterMiddleAccount(java.lang.Long, java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = false)
	public void deleteTwitterMiddleAccount(Long id, Long accountid) throws AppException {
		LOGGER.info("Entering deleteTwitterMiddleAccount()");

		TwitterScrapper scrapper = entityManager.find(TwitterScrapper.class, id);
		if (scrapper != null) {
			Accounts account = entityManager.find(Accounts.class, accountid);
			if (account != null) {
				Set<Accounts> accounts = scrapper.getTwittermiddledestinations();
				if (accounts != null && accounts.size() > 0) {
					accounts.remove(account);
				}
				scrapper.setTwittermiddledestinations(accounts);
				scrapper = entityManager.merge(scrapper);
			} else {
				throw new AppException(500, AppErrorCodes.ACCOUNT_NOT_FOUND,  
						AppErrorMessage.ACCOUNT_NOT_FOUND);
			}
		} else {
			throw new AppException(500, AppErrorCodes.SCRAPPER_NOT_FOUND,  
					AppErrorMessage.SCRAPPER_NOT_FOUND);
		}

		LOGGER.info("Exiting deleteTwitterMiddleAccount()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.ScrapperDAO#deleteTwitterOrderAccount(java.lang.Long, java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = false)
	public void deleteTwitterOrderAccount(Long id, Long accountid) throws AppException {
		LOGGER.info("Entering deleteTwitterOrderAccount()");

		TwitterScrapper scrapper = entityManager.find(TwitterScrapper.class, id);
		if (scrapper != null) {
			Accounts account = entityManager.find(Accounts.class, accountid);

			if (account != null) {
				Set<Accounts> accounts = scrapper.getTwitterorderdestinations();
				if (accounts != null && accounts.size() > 0) {
					accounts.remove(account);
				}

				scrapper.setTwitterorderdestinations(accounts);
				scrapper = entityManager.merge(scrapper);
			} else {
				throw new AppException(500, AppErrorCodes.ACCOUNT_NOT_FOUND,  
						AppErrorMessage.ACCOUNT_NOT_FOUND);
			}
		} else {
			throw new AppException(500, AppErrorCodes.SCRAPPER_NOT_FOUND,  
					AppErrorMessage.SCRAPPER_NOT_FOUND);
		}

		LOGGER.info("Exiting deleteTwitterOrderAccount()");	
	}
}