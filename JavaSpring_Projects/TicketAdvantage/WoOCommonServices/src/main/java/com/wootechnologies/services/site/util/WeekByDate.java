/**
 * 
 */
package com.wootechnologies.services.site.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * @author jmiller
 *
 */
public class WeekByDate {
	private static final Logger LOGGER = Logger.getLogger(WeekByDate.class);
	private static Date FootballStartDate2014 = null;
	private static Date FootballStartDate2015 = null;
	private static Date FootballStartDate2016 = null;
	private static Date FootballStartDate2017 = null;
	private static Date FootballStartDate2018 = null;
	private static Date FootballStartDate2019 = null;
	private static Date BasketballStartDate2014 = null;
	private static Date BasketballStartDate2015 = null;
	private static Date BasketballStartDate2016 = null;
	private static Date BasketballStartDate2017 = null;
	private static Date BasketballStartDate2018 = null;
	private static Date BasketballStartDate2019 = null;

	static {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, 7);
		cal.set(Calendar.DAY_OF_MONTH, 27);
		cal.set(Calendar.YEAR, 2014);		
		FootballStartDate2014 = cal.getTime();

		cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, 8);
		cal.set(Calendar.DAY_OF_MONTH, 3);
		cal.set(Calendar.YEAR, 2015);		
		FootballStartDate2015 = cal.getTime();

		cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, 7);
		cal.set(Calendar.DAY_OF_MONTH, 26);
		cal.set(Calendar.YEAR, 2016);		
		FootballStartDate2016 = cal.getTime();

		cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, 7);
		cal.set(Calendar.DAY_OF_MONTH, 26);
		cal.set(Calendar.YEAR, 2017);		
		FootballStartDate2017 = cal.getTime();

		cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, 7);
		cal.set(Calendar.DAY_OF_MONTH, 25);
		cal.set(Calendar.YEAR, 2018);		
		FootballStartDate2018 = cal.getTime();

		cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, 7);
		cal.set(Calendar.DAY_OF_MONTH, 24);
		cal.set(Calendar.YEAR, 2019);		
		BasketballStartDate2019 = cal.getTime();

		cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, 10);
		cal.set(Calendar.DAY_OF_MONTH, 5);
		cal.set(Calendar.YEAR, 2017);		
		BasketballStartDate2017 = cal.getTime();
		
		cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, 10);
		cal.set(Calendar.DAY_OF_MONTH, 4);
		cal.set(Calendar.YEAR, 2018);
		BasketballStartDate2018 = cal.getTime();
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
	public static Map<String, Integer> FootballDetermineWeeks(Integer year, Date start, Date end) {
		final Map<String, Integer> weeks = new HashMap<String, Integer>();

		try {
			LOGGER.error("start: " + start);
			LOGGER.error("end: " + end);

		    Instant d1i = null;
		    Instant d2i = null;

		    if (year == 2014) {
		    		final Calendar cal = Calendar.getInstance();
		    		cal.setTime(FootballStartDate2014);
		    		d1i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    		cal.setTime(start);
		    		d2i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    } else if (year == 2015) {
		    		final Calendar cal = Calendar.getInstance();
		    		cal.setTime(FootballStartDate2015);
		    		d1i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    		cal.setTime(start);
		    		d2i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    } else if (year == 2016) {
		    		final Calendar cal = Calendar.getInstance();
		    		cal.setTime(FootballStartDate2016);
		    		d1i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    		cal.setTime(start);
		    		d2i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    } else if (year == 2017) {
		    		final Calendar cal = Calendar.getInstance();
		    		cal.setTime(FootballStartDate2017);
		    		d1i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    		cal.setTime(start);
		    		d2i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    } else if (year == 2018) {
		    		final Calendar cal = Calendar.getInstance();
		    		cal.setTime(FootballStartDate2018);
		    		d1i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    		cal.setTime(start);
		    		d2i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    } else if (year == 2019) {
		    		final Calendar cal = Calendar.getInstance();
		    		cal.setTime(FootballStartDate2019);
		    		d1i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    		cal.setTime(start);
		    		d2i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    }
		    final LocalDateTime sDate = LocalDateTime.ofInstant(d1i, ZoneId.systemDefault());
		    final LocalDateTime eDate = LocalDateTime.ofInstant(d2i, ZoneId.systemDefault());
		    long sdiffs = ChronoUnit.WEEKS.between(sDate, eDate);
		    LOGGER.error("sdiffs: " + sdiffs);

		    if (year == 2014) {
		    		final Calendar cal = Calendar.getInstance();
		    		cal.setTime(FootballStartDate2014);
		    		d1i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    		cal.setTime(end);
		    		d2i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    } else if (year == 2015) {
		    		final Calendar cal = Calendar.getInstance();
		    		cal.setTime(FootballStartDate2015);
		    		d1i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    		cal.setTime(end);
		    		d2i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    } else if (year == 2016) {
		    		final Calendar cal = Calendar.getInstance();
		    		cal.setTime(FootballStartDate2016);
		    		d1i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    		cal.setTime(end);
		    		d2i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    } else if (year == 2017) {
		    		final Calendar cal = Calendar.getInstance();
		    		cal.setTime(FootballStartDate2017);
		    		d1i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    		cal.setTime(end);
		    		d2i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    } else if (year == 2018) {
		    		final Calendar cal = Calendar.getInstance();
		    		cal.setTime(FootballStartDate2018);
		    		d1i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    		cal.setTime(end);
		    		d2i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    } else if (year == 2019) {
		    		final Calendar cal = Calendar.getInstance();
		    		cal.setTime(FootballStartDate2019);
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
		    		cal.setTime(BasketballStartDate2016);
		    		d1i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    		cal.setTime(start);
		    		d2i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    } else if (year == 2017) {
		    		final Calendar cal = Calendar.getInstance();
		    		cal.setTime(BasketballStartDate2017);
		    		d1i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    		cal.setTime(start);
		    		d2i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    } else {
		    		final Calendar cal = Calendar.getInstance();
		    		cal.setTime(BasketballStartDate2018);
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
		    		cal.setTime(BasketballStartDate2016);
		    		d1i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    		cal.setTime(end);
		    		d2i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    } else if (year == 2017) {
		    		final Calendar cal = Calendar.getInstance();
		    		cal.setTime(BasketballStartDate2017);
		    		d1i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    		cal.setTime(end);
		    		d2i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    } else {
		    		final Calendar cal = Calendar.getInstance();
		    		cal.setTime(BasketballStartDate2018);
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
	public static Integer FootballDetermineWeek(Integer year, Date thedate) {
		int week = 0;

		try {
		    Instant d1i = null;
		    Instant d2i = null;

		    if (year == 2014) {
		    		final Calendar cal = Calendar.getInstance();
		    		cal.setTime(FootballStartDate2014);
		    		d1i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    		cal.setTime(thedate);
		    		d2i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    } else if (year == 2015) {
		    		final Calendar cal = Calendar.getInstance();
		    		cal.setTime(FootballStartDate2015);
		    		d1i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    		cal.setTime(thedate);
		    		d2i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    } else if (year == 2016) {
		    		final Calendar cal = Calendar.getInstance();
		    		cal.setTime(FootballStartDate2016);
		    		d1i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    		cal.setTime(thedate);
		    		d2i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    } else if (year == 2017) {
		    		final Calendar cal = Calendar.getInstance();
		    		cal.setTime(FootballStartDate2017);
		    		d1i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    		cal.setTime(thedate);
		    		d2i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    } else if (year == 2018) {
		    		final Calendar cal = Calendar.getInstance();
		    		cal.setTime(FootballStartDate2018);
		    		d1i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    		cal.setTime(thedate);
		    		d2i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    } else if (year == 2019) {
		    		final Calendar cal = Calendar.getInstance();
		    		cal.setTime(FootballStartDate2019);
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
		    		cal.setTime(BasketballStartDate2016);
		    		d1i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    		cal.setTime(thedate);
		    		d2i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    } else if (year == 2017) {
		    		final Calendar cal = Calendar.getInstance();
		    		cal.setTime(BasketballStartDate2017);
		    		d1i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    		cal.setTime(thedate);
		    		d2i = Instant.ofEpochMilli(cal.getTimeInMillis());
		    } else {
		    		final Calendar cal = Calendar.getInstance();
		    		cal.setTime(BasketballStartDate2018);
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