/**
 * 
 */
package com.wootechnologies.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.apache.log4j.Logger;


/**
 * @author jmiller
 *
 */
public class EventDateAdapter extends XmlAdapter<String, Date> {
	private static final Logger LOGGER = Logger.getLogger(EventDateAdapter.class);
	// 08/14 07:00 pm PST
	private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a z");
	
	/**
	 * 
	 */
	public EventDateAdapter() {
		super();
		LOGGER.info("Entering EventDateAdapter()");
		LOGGER.info("Exiting EventDateAdapter()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		EventDateAdapter eda = new EventDateAdapter();
		try {
			Date nDate = eda.unmarshal("1/01/2017 7:00 PM PT");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 
	 */
    @Override
    public String marshal(Date d) throws Exception {
    	LOGGER.info("Entering marshal()");
    	LOGGER.debug("Date: " + d);
    	LOGGER.info("Exiting marshal()");
    	return dateFormat.format(d);
    	
    }
 
    /*
     * 
     */
    @Override
    public Date unmarshal(String s) throws Exception {
    	LOGGER.info("Entering unmarshal()");
    	Date newDate = null;
    	LOGGER.debug("String: " + s);
    	if (s != null && s.length() > 0) {
    		int index = s.indexOf(" ");
    		String nDate = s.substring(0, index);
    		final Calendar now = Calendar.getInstance();
    		int offset = now.get(Calendar.DST_OFFSET);
    		nDate = nDate +  s.substring(index);
    		if (offset != 0) {
    			nDate = nDate.replace("ET", "EDT");
    			nDate = nDate.replace("CT", "CDT");
    			nDate = nDate.replace("MT", "MDT");
    			nDate = nDate.replace("PT", "PDT");
    		} else {
    			nDate = nDate.replace("ET", "EST");
    			nDate = nDate.replace("CT", "CST");
    			nDate = nDate.replace("MT", "MST");
    			nDate = nDate.replace("PT", "PST");
    		}
    		LOGGER.debug("nDate: " + nDate);
    		newDate = dateFormat.parse(nDate);
    		LOGGER.debug("Date: " + newDate);
    	}
    	LOGGER.info("Exiting unmarshal()");
    	return newDate;
    }
}