/**
 * 
 */
package com.thewizard.app.web.restapi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.wooanalytics.dao.ReportsDAO;
import com.wootechnologies.errorhandling.AppErrorCodes;
import com.wootechnologies.errorhandling.AppErrorMessage;
import com.wootechnologies.errorhandling.AppException;

/**
 * @author jmiller
 *
 */
@RequestMapping(path="/restapi")
@RestController
@Service
public class NcaabReportController {
	private static final Logger LOGGER = Logger.getLogger(NcaabReportController.class);

	@Autowired
	private ReportsDAO reportsDAO;

	/**
	 * 
	 */
	public NcaabReportController() {
		super();
		LOGGER.debug("Entering NcaabReportController()");
		LOGGER.debug("Exiting NcaabReportController()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * 
	 * @param response
	 * @return
	 * @throws AppException
	 */
	@GetMapping(path="/ncaabreport")
	public ResponseEntity<StreamingResponseBody> ncaabReport(HttpServletResponse response) throws AppException {
		LOGGER.info("Entering ncaabReport()");
		ByteArrayOutputStream output = null;
		StreamingResponseBody stream = null;

		try {
			// Find all data based on query
			output = reportsDAO.createNcaabReport();

	    		final byte[] bytes = output.toByteArray();
	    		stream = out -> {
	    			out.write(bytes);
	        };
		} catch (AppException ae) {
			LOGGER.error("AppException in ncaabReport()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in ncaabReport()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}

		final HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("content-disposition","attachment; filename = HoopsData.xlsx");
		responseHeaders.set("content-type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

		LOGGER.info("Exiting ncaabReport()");
        return new ResponseEntity<StreamingResponseBody>(stream, responseHeaders, HttpStatus.OK);
//		return Response.ok(output).header("content-disposition","attachment; filename = HoopsData.xlsx").header("content-type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet").build();
	}
}