/**
 * 
 */
package com.wooanalytics.service;

import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.wooanalytics.dao.SearchNcaaFootballDAO;
import com.wooanalytics.model.EspnCollegeFootballGameData;
import com.wootechnologies.errorhandling.AppErrorCodes;
import com.wootechnologies.errorhandling.AppErrorMessage;
import com.wootechnologies.errorhandling.AppException;

/**
 * @author jmiller
 *
 */
@RequestMapping(path="/search")
@RestController
@Service
public class SearchRestController {
	private static final Logger LOGGER = Logger.getLogger(SearchRestController.class);

	@Autowired
	private SearchNcaaFootballDAO searchNcaaFootballDAO;

	/**
	 * 
	 */
	public SearchRestController() {
		super();
		LOGGER.debug("Entering SearchRestController()");
		LOGGER.debug("Exiting SearchRestController()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * 
	 * @param searchterm
	 * @param week
	 * @param from
	 * @param to
	 * @param yearmap
	 * @param bettype
	 * @param options
	 * @param directsql
	 * @return
	 * @throws AppException
	 */
	@GetMapping(path="/ncaafootball", produces={MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	public Set<EspnCollegeFootballGameData> searchNcaaFootball(MultiValueMap<String, String> params) throws AppException {
		LOGGER.info("Entering searchNcaaFootball()");
		Set<EspnCollegeFootballGameData> retValue = null;

		try {
			// Find all accounts
			retValue = searchNcaaFootballDAO.searchNcaaFootball(params);
		} catch (AppException ae) {
			LOGGER.error("AppException in searchNcaaFootball()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in searchNcaaFootball()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}

		LOGGER.debug("EspnCollegeFootballGameData: " + retValue);
		LOGGER.info("Exiting searchNcaaFootball()");
		return retValue;
	}
}