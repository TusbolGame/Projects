/**
 * 
 */
package com.wooanalytics.dao;

import java.io.ByteArrayOutputStream;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.wooanalytics.dao.ncaab.WoONcaaBasketballReport;
import com.wooanalytics.dao.sites.kenpom.KenPomData;
import com.wooanalytics.model.ncaab.SpreadLastThree;
import com.wootechnologies.errorhandling.AppException;

/**
 * @author jmiller
 *
 */
@Repository
public class ReportsDAOImpl implements ReportsDAO {
	private final static Logger LOGGER = Logger.getLogger(ReportsDAOImpl.class);
	private WoONcaaBasketballReport woONcaaBasketballReport = new WoONcaaBasketballReport();

	@PersistenceContext(name = "entityManager")
	private EntityManager entityManager;

	/**
	 * 
	 */
	public ReportsDAOImpl() {
		super();
		LOGGER.debug("Entering ReportsDAOImpl()");
		LOGGER.debug("Exiting ReportsDAOImpl()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LOGGER.debug("Entering main()");

		try {
			final ReportsDAOImpl reportsDAOImpl = new ReportsDAOImpl();
			try {
				boolean isLoaded = false;
				while (!isLoaded) {
					isLoaded = reportsDAOImpl.woONcaaBasketballReport.getREPORT_CACHE().isLoaded();
					Thread.sleep(10000);
				}
				List<KenPomData> kmpd = reportsDAOImpl.woONcaaBasketballReport.getREPORT_CACHE().getKps().getNcaabScheduleLastThreeDays();
				LOGGER.debug("kmpd.size(): " + kmpd.size());
				reportsDAOImpl.woONcaaBasketballReport.getREPORT_CACHE().setLkpd(kmpd);
			} catch (InterruptedException ie) {
				LOGGER.error(ie.getMessage(), ie);
			}

			final List<SpreadLastThree> spreadLastThreeList = reportsDAOImpl.woONcaaBasketballReport.getNextDaySpreads();
			for (SpreadLastThree spreadLastThree : spreadLastThreeList) {
				System.out.println("SpreadLastThree: " + spreadLastThree);
			}
		} catch (Throwable t) {
			LOGGER.error("Exception in main()");
			LOGGER.error(t.getMessage(), t);
		}


/*
		try {
			final ReportsDAOImpl reportsDAOImpl = new ReportsDAOImpl();
			try {
				boolean isLoaded = false;
				while (!isLoaded) {
					isLoaded = reportsDAOImpl.woOzNcaaBasketballReport.getREPORT_CACHE().isLoaded();
					Thread.sleep(10000);
				}
				List<KenPomData> kmpd =reportsDAOImpl.woOzNcaaBasketballReport.getREPORT_CACHE().getKps().getNcaabScheduleLastThreeDays();
				LOGGER.debug("kmpd.size(): " + kmpd.size());
				reportsDAOImpl.woOzNcaaBasketballReport.getREPORT_CACHE().setLkpd(kmpd);
			} catch (InterruptedException ie) {
				LOGGER.error(ie.getMessage(), ie);
			}

			final HSSFWorkbook workbook = reportsDAOImpl.woOzNcaaBasketballReport.generateReportWorkbook();

			// Write the output to a file
			final FileOutputStream fileOut = new FileOutputStream("Hoops Sheet.xls");
			workbook.write(fileOut);
			workbook.close();
			fileOut.close();
		} catch (Throwable t) {
			LOGGER.error("Exception in main()");
			LOGGER.error(t.getMessage(), t);
		}
*/

		LOGGER.debug("Exiting main()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.ReportsDAO#createNcaabReport()
	 */
	@Override
	public ByteArrayOutputStream createNcaabReport() throws AppException {
		LOGGER.debug("Entering createNcaabReport()");

		try {
			boolean isLoaded = false;
			while (!isLoaded) {
				isLoaded = woONcaaBasketballReport.getREPORT_CACHE().isLoaded();
				Thread.sleep(10000);
			}
		} catch (InterruptedException ie) {
			LOGGER.error(ie.getMessage(), ie);
		}

		// create the report and stream it back
		final ByteArrayOutputStream stream = woONcaaBasketballReport.createNcaaReport();

		LOGGER.debug("Exiting createNcaabReport()");
		return stream;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.ReportsDAO#createNewNcaabReport()
	 */
	@Override
	public ByteArrayOutputStream createNewNcaabReport() throws AppException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.ReportsDAO#getSpreadsForNextDay()
	 */
	@Override
	public List<SpreadLastThree> getSpreadsForNextDay() throws AppException {
		LOGGER.debug("Entering getSpreadsForNextDay()");

		try {
			boolean isLoaded = false;
			while (!isLoaded) {
				isLoaded = woONcaaBasketballReport.getREPORT_CACHE().isLoaded();
				Thread.sleep(10000);
			}
		} catch (InterruptedException ie) {
			LOGGER.error(ie.getMessage(), ie);
		}

		// Get the next day spreads
		final List<SpreadLastThree> spreadLastThreeList = woONcaaBasketballReport.getNextDaySpreads();

		LOGGER.debug("Exiting getSpreadsForNextDay()");
		return spreadLastThreeList;
	}
}