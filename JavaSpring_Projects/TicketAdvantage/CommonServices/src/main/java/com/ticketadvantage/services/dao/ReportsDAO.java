/**
 * 
 */
package com.ticketadvantage.services.dao;

import java.util.List;

import javax.ws.rs.core.StreamingOutput;

import com.ticketadvantage.services.errorhandling.AppException;
import com.ticketadvantage.services.model.SpreadLastThree;

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
	public StreamingOutput createNcaabReport() throws AppException;

	/**
	 * 
	 * @return
	 * @throws AppException
	 */
	public StreamingOutput createNewNcaabReport() throws AppException;

	/**
	 * 
	 * @return
	 * @throws AppException
	 */
	public List<SpreadLastThree> getSpreadsForNextDay() throws AppException;
}