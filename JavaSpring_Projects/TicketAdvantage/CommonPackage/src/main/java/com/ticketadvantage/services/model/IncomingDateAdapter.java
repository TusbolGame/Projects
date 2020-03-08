/**
 * 
 */
package com.ticketadvantage.services.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.TimeZone;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.apache.log4j.Logger;


/**
 * @author jmiller
 *
 */
public class IncomingDateAdapter extends XmlAdapter<String, Date> {
	private static final Logger LOGGER = Logger.getLogger(IncomingDateAdapter.class);
	// 08/14/2016 07:00 pm
	private SimpleDateFormat inDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
	private SimpleDateFormat outDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a z");
	
	/**
	 * 
	 */
	public IncomingDateAdapter() {
		super();
		LOGGER.info("Entering IncomingDateAdapter()");
		LOGGER.info("Exiting IncomingDateAdapter()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			IncomingDateAdapter incomingDateAdapter = new IncomingDateAdapter();
			Date nDate = incomingDateAdapter.unmarshal("09/05/2016 07:04 pm America/Los_Angeles");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
	 */
    @Override
    public String marshal(Date d) throws Exception {
    	LOGGER.info("Entering marshal()");
    	LOGGER.debug("Date: " + d);
    	LOGGER.info("Exiting marshal()");
    	return outDateFormat.format(d);
    	
    }

    /*
     * (non-Javadoc)
     * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
     */
    @Override
    public Date unmarshal(String s) throws Exception {
    	LOGGER.info("Entering unmarshal()");
    	LOGGER.debug("String: " + s);
    	StringTokenizer st = new StringTokenizer(s, " ");
    	int count = 0;
    	String newString = "";
		while (st.hasMoreElements()) {
			String timeZone = (String)st.nextElement();
			if (++count == 4) {
				TimeZone tzInAmerica = TimeZone.getTimeZone(timeZone);
				inDateFormat.setTimeZone(tzInAmerica);
			} else {
				newString = newString + " " + timeZone;
			}
		}
		LOGGER.debug("NewDate: " + newString);
    	LOGGER.info("Exiting unmarshal()");
    	return inDateFormat.parse(newString);
    }
}