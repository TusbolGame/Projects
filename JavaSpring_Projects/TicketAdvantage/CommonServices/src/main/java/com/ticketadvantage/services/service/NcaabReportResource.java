/**
 * 
 */
package com.ticketadvantage.services.service;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ticketadvantage.services.dao.ReportsDAO;
import com.ticketadvantage.services.errorhandling.AppErrorCodes;
import com.ticketadvantage.services.errorhandling.AppErrorMessage;
import com.ticketadvantage.services.errorhandling.AppException;

/**
 * @author jmiller
 *
 */
@Path("/ncaabreport")
@Service
public class NcaabReportResource {
	private static final Logger LOGGER = Logger.getLogger(NcaabReportResource.class);

	@Autowired
	private ReportsDAO reportsDAO;

	/**
	 * 
	 */
	public NcaabReportResource() {
		super();
		LOGGER.debug("Entering NcaabReportResource()");
		LOGGER.debug("Exiting NcaabReportResource()");
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
	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response ncaabReport(@Context HttpServletResponse response) throws AppException {
		LOGGER.info("Entering ncaabReport()");
		StreamingOutput output = null;

		try {
			// Find all data based on query
			output = reportsDAO.createNcaabReport();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-disposition", "attachment; filename=\"" + "HoopsData" + ".xlsx\"");
		} catch (AppException ae) {
			LOGGER.error("AppException in ncaabReport()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in ncaabReport()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}
		
		LOGGER.info("Exiting ncaabReport()");
		return Response.ok(output).header("content-disposition","attachment; filename = HoopsData.xlsx").header("content-type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet").build();
	}
}