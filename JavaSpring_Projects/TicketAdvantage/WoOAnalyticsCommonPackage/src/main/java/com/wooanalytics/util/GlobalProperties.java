/**
 * 
 */
package com.wooanalytics.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;


/**
 * @author jmiller
 *
 */
public class GlobalProperties {
	private static final Logger LOGGER = Logger.getLogger(GlobalProperties.class);
	private static Properties NFLLINESPROPS;
	private static Properties NFLFIRSTPROPS;
	private static Properties NFLSECONDPROPS;
	private static Properties NCAAFLINESPROPS;
	private static Properties NCAAFFIRSTPROPS;
	private static Properties NCAAFSECONDPROPS;
	private static boolean isStubbed;
	private static boolean isLocal;
	private static final List<String> SITEPROVIDERS = new ArrayList<String>();

	static {
		// Get the properties
		try {
			NFLLINESPROPS = new Properties();
			InputStream in = GlobalProperties.class.getClassLoader().getResourceAsStream("nfllines.properties");
			if (in == null) {
				throw new Exception("Error loading nfllines.properties!");
			}
	        NFLLINESPROPS.load(in);
	        in.close();

			NFLFIRSTPROPS = new Properties();
	        in = GlobalProperties.class.getClassLoader().getResourceAsStream("nflfirst.properties");
			if (in == null) {
				throw new Exception("Error loading nflfirst.properties!");
			}
	        NFLFIRSTPROPS.load(in);
	        in.close();

			NFLSECONDPROPS = new Properties();
	        in = GlobalProperties.class.getClassLoader().getResourceAsStream("nflsecond.properties");
			if (in == null) {
				throw new Exception("Error loading nflsecond.properties!");
			}
	        NFLSECONDPROPS.load(in);
	        in.close();

	        NCAAFLINESPROPS = new Properties();
	        in = GlobalProperties.class.getClassLoader().getResourceAsStream("ncaaflines.properties");
			if (in == null) {
				throw new Exception("Error loading ncaaflines.properties!");
			}
	        NCAAFLINESPROPS.load(in);
	        in.close();

	        NCAAFFIRSTPROPS = new Properties();
	        in = GlobalProperties.class.getClassLoader().getResourceAsStream("ncaaffirst.properties");
			if (in == null) {
				throw new Exception("Error loading ncaaffirst.properties!");
			}
	        NCAAFFIRSTPROPS.load(in);
	        in.close();

	        NCAAFSECONDPROPS = new Properties();
	        in = GlobalProperties.class.getClassLoader().getResourceAsStream("ncaafsecond.properties");
			if (in == null) {
				throw new Exception("Error loading ncaafsecond.properties!");
			}
	        NCAAFSECONDPROPS.load(in);
	        in.close();

	        Properties tixAdvantage = new Properties();
	        in = GlobalProperties.class.getClassLoader().getResourceAsStream("ticketadvantage.properties");
			if (in == null) {
				throw new Exception("Error loading ticketadvantage.properties!");
			}
	        tixAdvantage.load(in);
	        isStubbed = new Boolean(tixAdvantage.getProperty("isstubbed"));
	        isLocal = new Boolean(tixAdvantage.getProperty("islocal"));
	        in.close();

	        // Loop through the list
	        for (Map.Entry<Object, Object> entry : tixAdvantage.entrySet())
		    {
		    	String key = (String)entry.getKey();
		    	if (key.contains("siteprovider.name")) {
		        	String nameKey =  (String)entry.getKey();
		        	int index = nameKey.lastIndexOf(".");
		        	String numValue = nameKey.substring(index + 1);
		        	LOGGER.info("numValue: " + numValue);

		        	SITEPROVIDERS.add(tixAdvantage.getProperty("siteprovider.name." + numValue));
		    	}
		    }
		} catch (Exception e) {
			LOGGER.error("Exception getting proxy information", e);
		}
	}

	/**
	 * 
	 */
	public GlobalProperties() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * 
	 * @return
	 */
	public static Properties getNFLlines() {
		return NFLLINESPROPS;
	}

	/**
	 * 
	 * @return
	 */
	public static Properties getNFLfirst() {
		return NFLFIRSTPROPS;
	}

	/**
	 * 
	 * @return
	 */
	public static Properties getNFLsecond() {
		return NFLSECONDPROPS;
	}

	/**
	 * 
	 * @return
	 */
	public static Properties getNCAAFlines() {
		return NCAAFLINESPROPS;
	}

	/**
	 * 
	 * @return
	 */
	public static Properties getNCAAFfirst() {
		return NCAAFFIRSTPROPS;
	}

	/**
	 * 
	 * @return
	 */
	public static Properties getNCAAFsecond() {
		return NCAAFSECONDPROPS;
	}

	/**
	 * 
	 * @return
	 */
	public static boolean isStubbed() {
		return isStubbed;
	}

	/**
	 * 
	 * @return
	 */
	public static boolean isLocal() {
		return isLocal;
	}

	/**
	 * 
	 * @return
	 */
	public static String[] getSiteproviders() {
		final String list2[] = new String[SITEPROVIDERS.size()];
		return SITEPROVIDERS.toArray(list2);
	}
}