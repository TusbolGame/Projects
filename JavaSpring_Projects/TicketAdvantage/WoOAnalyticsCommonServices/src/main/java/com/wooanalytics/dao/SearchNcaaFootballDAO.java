/**
 * 
 */
package com.wooanalytics.dao;

import java.util.Set;

import javax.persistence.EntityManager;

import org.springframework.util.MultiValueMap;

import com.wooanalytics.model.EspnCollegeFootballGameData;
import com.wootechnologies.errorhandling.AppException;

/**
 * @author jmiller
 *
 */
public interface SearchNcaaFootballDAO {

	/**
	 * 
	 * @param espnCollegeFootballGameData
	 * @return
	 * @throws AppException
	 */
	public EspnCollegeFootballGameData persist(EspnCollegeFootballGameData espnCollegeFootballGameData) throws AppException;

	/**
	 * 
	 * @param espngameid
	 * @throws AppException
	 */
	public void delete(Integer espngameid) throws AppException;

	/**
	 * 
	 * @param params
	 * @return
	 */
	public Set<EspnCollegeFootballGameData> searchNcaaFootball(MultiValueMap<String, String> params);

	/**
	 * 
	 * @param entityManager
	 */
	public void setEntityManager(EntityManager entityManager);

	/**
	 * 
	 * @return
	 */
	public EntityManager getEntityManager();
}