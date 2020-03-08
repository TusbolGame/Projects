/**
 * 
 */
package com.ticketadvantage.services.dao.sites;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;


/**
 * @author jmiller
 *
 */
public class SiteContainer {
	private static final Logger LOGGER = Logger.getLogger(SiteContainer.class);
	private String sitename;
	private static Set<SiteContainer> sites;

	static {
		// Get the properties
		try {
			final Properties prop = new Properties();
			final InputStream in = SiteContainer.class.getClassLoader().getResourceAsStream("ticketadvantage.properties");
			if (in == null) {
				throw new Exception("Error loading proxy.properties!");
			}
	        prop.load(in);
	        in.close();

	        // Get the proxy information
	        sites = getPropertyList(prop, "siteprovider.name");
		} catch (Exception e) {
			LOGGER.error("Exception getting proxy information", e);
			System.exit(99);
		}
	}

	/**
	 * 
	 */
	public SiteContainer() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LOGGER.info("SiteNames: " + SiteContainer.getSiteNames());
	}

	/**
	 * @return the sitename
	 */
	public String getSitename() {
		return sitename;
	}

	/**
	 * @param sitename the sitename to set
	 */
	public void setSitename(String sitename) {
		this.sitename = sitename;
	}

	/**
	 * 
	 * @return
	 */
	public static String[] getSiteNames() {
		LOGGER.info("Entering getSiteNames()");
		final String[] siteNames = new String[sites.size()];

		final Iterator<SiteContainer> itr = sites.iterator();
		int x = 0;
		while (itr.hasNext()) {
			siteNames[x++] = itr.next().getSitename();
		}

		LOGGER.debug("SiteNames: " + siteNames);
		LOGGER.info("Exiting getSiteNames()");
		return siteNames;
	}

	/**
	 * 
	 * @param siteName
	 * @return
	 */
	public static SiteContainer getSiteByName(String siteName) {
		LOGGER.info("Entering getSiteByName()");
		SiteContainer siteContainer = null;
		final Iterator<SiteContainer> itr = sites.iterator();

		while (itr.hasNext()) {
			siteContainer = itr.next();
			if (siteName != null && siteName.equals(siteContainer.getSitename())) {
				break;
			} else {
				siteContainer = null;
			}
		}

		LOGGER.debug("SiteContainer: " + siteContainer);
		LOGGER.info("Exiting getSiteByName()");
		return siteContainer;
	}

	/**
	 * @return the sites
	 */
	public static Set<SiteContainer> getSites() {
		return sites;
	}

	/**
	 * 
	 * @param properties
	 * @param name
	 * @return
	 */
	public static Set<SiteContainer> getPropertyList(Properties properties, String name) 
	{
		LOGGER.info("Entering getPropertyList()");
	    Set<SiteContainer> result = new HashSet<SiteContainer>();
	    for (Map.Entry<Object, Object> entry : properties.entrySet())
	    {
	        if (((String)entry.getKey()).matches("^" + Pattern.quote(name) + "\\.\\d+$"))
	        {
		        	LOGGER.debug("Key: " + (String)entry.getKey());
		        final SiteContainer siteContainer = new SiteContainer();
		        siteContainer.setSitename((String) entry.getValue());
		        LOGGER.debug("SiteContainer: " + siteContainer);

	            result.add(siteContainer);
	        }
	    }

	    LOGGER.info("result: " + result);
	    LOGGER.info("Exiting getPropertyList()");
	    return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SiteContainer [sitename=" + sitename + "]";
	}
}
