
/**
 * 
 */
package com.wooanalytics.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import com.wooanalytics.model.EspnCollegeFootballGameData;
import com.wootechnologies.errorhandling.AppErrorCodes;
import com.wootechnologies.errorhandling.AppErrorMessage;
import com.wootechnologies.errorhandling.AppException;

import lombok.Data;

/**
 * @author jmiller
 *
 */
@Data
@Repository
public class SearchNcaaFootballDAOImpl implements SearchNcaaFootballDAO {
	final static Logger LOGGER = LoggerFactory.getLogger(SearchNcaaFootballDAOImpl.class);

	@PersistenceContext(name = "entityManager")
	private EntityManager entityManager;

	/**
	 * 
	 */
	public SearchNcaaFootballDAOImpl() {
		super();
		LOGGER.debug("Entering SearchNcaaFootballDAOImpl()");
		LOGGER.debug("Exiting SearchNcaaFootballDAOImpl()");
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
	 * @see com.wooanalytics.dao.SearchNcaaFootballDAO#persist(com.wooanalytics.model.EspnCollegeFootballGameData)
	 */
	@Override
	@Transactional(readOnly = false)
	public EspnCollegeFootballGameData persist(EspnCollegeFootballGameData espnCollegeFootballGameData) throws AppException {
		LOGGER.info("Entering persist()");
		LOGGER.debug("EspnCollegeFootballGameData: " + espnCollegeFootballGameData);

		// Check if account name is already taken
		@SuppressWarnings("unchecked")
		final List<EspnCollegeFootballGameData> listGames = (List<EspnCollegeFootballGameData>) entityManager
				.createQuery("SELECT ecfgd FROM EspnCollegeFootballGameData ecfgd WHERE ecfgd.espngameid = :espngameid").setParameter("espngameid", espnCollegeFootballGameData.getEspngameid())
				.getResultList();
		if ((listGames == null) || (listGames != null && listGames.size() == 0)) {
			// Write out the account to the account table
			entityManager.persist(espnCollegeFootballGameData);
		} else if (listGames != null && listGames.size() > 0) {
			// We already have one, send necessary error message
			throw new AppException(500, AppErrorCodes.DUPLICATE_ACCOUNT, AppErrorMessage.DUPLICATE_ACCOUNT);
		}

		LOGGER.info("Exiting persist()");
		return espnCollegeFootballGameData;
	}

	/*
	 * (non-Javadoc)
	 * @see com.wooanalytics.dao.SearchNcaaFootballDAO#delete(java.lang.Integer)
	 */
	@Override
	@Transactional(readOnly = false)
	public void delete(Integer espngameid) throws AppException {
		LOGGER.info("Entering delete()");

		final EspnCollegeFootballGameData espnCollegeFootballGameData = entityManager.find(EspnCollegeFootballGameData.class, espngameid);

		// Remove the account
		entityManager.remove(espnCollegeFootballGameData);

		LOGGER.info("Exiting delete()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.wooanalytics.dao.SearchNcaaFootballDAO#searchNcaaFootball(org.springframework.util.MultiValueMap)
	 */
	@Override
	public Set<EspnCollegeFootballGameData> searchNcaaFootball(MultiValueMap<String, String> params) {
		final StringBuffer sb = new StringBuffer(500);
//		final List<Accounts> listAccount = (List<Accounts>) entityManager.createQuery("SELECT a FROM Accounts a WHERE a.name = :name").setParameter("name", account.getName()).getResultList();
		sb.append("SELECT e FROM EspnCollegeFootballGameData e WHERE ");
		boolean queryset = false;
		if (params.containsKey("searchterm") && params.get("searchterm") != null && params.get("searchterm").size() > 0) {
			if (params.containsKey("searchfields") && params.get("searchfields") != null && params.get("searchfields").size() > 0) {
				final String searchterm = params.get("searchterm").get(0);
				final List<String> searchfields = params.get("searchfields");
				int x = 0;

				sb.append("(");
				for (String searchfield : searchfields) {
					if (x++ == 0) {
						sb.append(searchfield +" = '").append(searchterm).append("' ");
					} else {
						sb.append("OR " + searchfield + " = '").append(searchterm).append("' ");
					}
				}
				sb.append(") ");
				queryset = true;				
			}
		}
		if (params.containsKey("week") && params.get("week") != null && params.get("week").size() > 0) {
			final Integer week = Integer.parseInt(params.get("week").get(0));
			if (queryset) {
				sb.append("AND week = ").append(week).append(" ");
			} else {
				sb.append(" week = ").append(week).append(" ");
			}
			queryset = true;
		}
		if (params.containsKey("from") && params.get("from") != null && params.get("from").size() > 0) {
		}
		if (params.containsKey("to") && params.get("to") != null && params.get("to").size() > 0) {
		}
		if (params.containsKey("year") && params.get("year") != null && params.get("year").size() > 0) {
			final List<String> years = params.get("year");
			String yearsin = "";
			int x = 0;
			for (String year : years) {
				if (x++ == 0) {
					yearsin = year.toString();
				} else {
					yearsin += ", " + year.toString();
				}
			}
			if (queryset) {
				sb.append("AND year IN (").append(yearsin).append(") ");
			} else {
				sb.append(" year IN (").append(yearsin).append(") ");
			}
			queryset = true;
		}

		LOGGER.debug("query: " + sb.toString());

		@SuppressWarnings("unchecked")
		final List<EspnCollegeFootballGameData> list = entityManager.createQuery(sb.toString()).getResultList();
		final Set<EspnCollegeFootballGameData> retValue = new HashSet<EspnCollegeFootballGameData>(list);

		return retValue;
	}
}