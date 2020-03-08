/**
 * 
 */
package com.wooanalytics.dao;

import java.io.ByteArrayOutputStream;
import java.util.List;

import com.wooanalytics.model.ncaab.SpreadLastThree;
import com.wootechnologies.errorhandling.AppException;

/**
 * @author jmiller
 *
 */
public interface ReportsDAO {

	/**
	 * 
	 * @return
	 * @throws AppException
	 */
	public ByteArrayOutputStream createNcaabReport() throws AppException;

	/**
	 * 
	 * @return
	 * @throws AppException
	 */
	public ByteArrayOutputStream createNewNcaabReport() throws AppException;

	/**
	 * 
	 * @return
	 * @throws AppException
	 */
	public List<SpreadLastThree> getSpreadsForNextDay() throws AppException;
}