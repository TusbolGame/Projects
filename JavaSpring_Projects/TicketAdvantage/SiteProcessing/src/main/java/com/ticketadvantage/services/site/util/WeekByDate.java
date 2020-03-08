/**
 * 
 */
package com.ticketadvantage.services.site.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ibm.icu.util.Calendar;

/**
 * @author jmiller
 *
 */
public class WeekByDate {
	private static final Logger LOGGER = Logger.getLogger(WeekByDate.class);
	private static Date StartDate2014 = null;
	private static Date StartDate2015 = null;
	private static Date StartDate2016 = null;
	private static Date StartDate2017 = null;
	private static Date StartDate2018 = null;
	private static Date StartDate2019 = null;

	static {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, 10);
		cal.set(Calendar.DAY_OF_MONTH, 6);
		cal.set(Calendar.YEAR, 2016);		
		StartDate2016 = cal.getTime();

		cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, 10);
		cal.set(Calendar.DAY_OF_MONTH, 5);
		cal.set(Calendar.YEAR, 2017);		
		StartDate2017 = cal.getTime();
		
		cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, 10);
		cal.set(Calendar.DAY_OF_MONTH, 4);
		cal.set(Calendar.YEAR, 2018);
		StartDate2018 = cal.getTime();
	}

	/**
	 * 
	 */
	public WeekByDate() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	    final Calendar endcal = Calendar.getInstance();
	    endcal.set(Calendar.MONTH, 0);
	    endcal.set(Calendar.DAY_OF_MONTH, 28);
	    endcal.set(Calendar.YEAR, 2017);
//	    LOGGER.error("endcal: " + endcal);
		final Integer week = WeekByDate.DetermineWeek(2016, endcal.getTime());
		LOGGER.error("week: " + week);
	}

	/**
	 * 
	 * @param year
	 * @param start
	 * @param end
	 * @return
	 */
	public static Map<String, Integer> DetermineWeeks(Integer year, Date start, Date end) {
		final Map<String, Integer> weeks = new HashMap<String, Integer>();

		try {
			LOGGER.error("start: " + start);
			LOGGER.error("end: " + end);

		    Instant d1i = null;
		    Instant d2i = null;

		    if (year == 2016) {
		    		final Calendar cal = Calendar.getInstance();
		    		cal.setTime(StartDate2016);
		    		d1i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    		cal.setTime(start);
		    		d2i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    } else if (year == 2017) {
		    		final Calendar cal = Calendar.getInstance();
		    		cal.setTime(StartDate2017);
		    		d1i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    		cal.setTime(start);
		    		d2i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    } else {
		    		final Calendar cal = Calendar.getInstance();
		    		cal.setTime(StartDate2018);
		    		d1i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    		cal.setTime(start);
		    		d2i = Instant.ofEpochMilli(cal.getTimeInMillis());		    	
		    }
		    final LocalDateTime sDate = LocalDateTime.ofInstant(d1i, ZoneId.systemDefault());
		    final LocalDateTime eDate = LocalDateTime.ofInstant(d2i, ZoneId.systemDefault());
		    long sdiffs = ChronoUnit.WEEKS.between(sDate, eDate);
		    LOGGER.error("sdiffs: " + sdiffs);

		    if (year == 2016) {
		    		final Calendar cal = Calendar.getInstance();
		    		cal.setTime(StartDate2016);
		    		d1i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    		cal.setTime(end);
		    		d2i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    } else if (year == 2017) {
		    		final Calendar cal = Calendar.getInstance();
		    		cal.setTime(StartDate2017);
		    		d1i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    		cal.setTime(end);
		    		d2i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    } else {
		    		final Calendar cal = Calendar.getInstance();
		    		cal.setTime(StartDate2018);
		    		d1i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    		cal.setTime(end);
		    		d2i = Instant.ofEpochMilli(cal.getTimeInMillis());		    	
		    }
		    final LocalDateTime seDate = LocalDateTime.ofInstant(d1i, ZoneId.systemDefault());
		    final LocalDateTime eeDate = LocalDateTime.ofInstant(d2i, ZoneId.systemDefault());
		    long ediffs = ChronoUnit.WEEKS.between(seDate, eeDate);
		    LOGGER.error("ediffs: " + ediffs);
		    
		    weeks.put("start", (int)sdiffs);
		    weeks.put("end", (int)ediffs);
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		return weeks;
	}

	/**
	 * 
	 * @param year
	 * @param thedate
	 * @return
	 */
	public static Integer DetermineWeek(Integer year, Date thedate) {
		int week = 0;

		try {
		    Instant d1i = null;
		    Instant d2i = null;

		    if (year == 2016) {
		    		final Calendar cal = Calendar.getInstance();
		    		cal.setTime(StartDate2016);
		    		d1i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    		cal.setTime(thedate);
		    		d2i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    } else if (year == 2017) {
		    		final Calendar cal = Calendar.getInstance();
		    		cal.setTime(StartDate2017);
		    		d1i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    		cal.setTime(thedate);
		    		d2i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    } else {
		    		final Calendar cal = Calendar.getInstance();
		    		cal.setTime(StartDate2018);
		    		d1i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    		cal.setTime(thedate);
		    		d2i = Instant.ofEpochMilli(cal.getTimeInMillis());		    	
		    }
		    final LocalDateTime sDate = LocalDateTime.ofInstant(d1i, ZoneId.systemDefault());
		    final LocalDateTime eDate = LocalDateTime.ofInstant(d2i, ZoneId.systemDefault());
		    LOGGER.debug("sDate: " + sDate);
		    LOGGER.debug("eDate: " + eDate);

		    long sdiffs = ChronoUnit.WEEKS.between(sDate, eDate);
		    LOGGER.debug("sdiffs: " + sdiffs);

		    week = (int)sdiffs;
		    week++;
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		return week;
	}
}