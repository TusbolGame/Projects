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
public class ProxyContainer {
	private static final Logger LOGGER = Logger.getLogger(ProxyContainer.class);
	private String proxyname;
	private String hostname;
	private String port1;
	private String port2;
	private String username;
	private String password;
	private static Set<ProxyContainer> proxies;
	
	static {
		// Get the properties
		try {
			final Properties prop = new Properties();
			final InputStream in = ProxyContainer.class.getClassLoader().getResourceAsStream("proxy.properties");
			if (in == null) {
				throw new Exception("Error loading proxy.properties!");
			}
	        prop.load(in);
	        in.close();

	        // Get the proxy information
	        proxies = getPropertyList(prop, "proxy.name");
		} catch (Exception e) {
			LOGGER.error("Exception getting proxy information", e);
			System.exit(99);
		}
	}

	/**
	 * 
	 */
	public ProxyContainer() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final ProxyContainer proxyContainer = new ProxyContainer();
		LOGGER.info("ProxyName: " + proxyContainer.getProxyname());
	}

	/**
	 * @return the proxyname
	 */
	public String getProxyname() {
		return proxyname;
	}

	/**
	 * @param proxyname the proxyname to set
	 */
	public void setProxyname(String proxyname) {
		this.proxyname = proxyname;
	}

	/**
	 * @return the hostname
	 */
	public String getHostname() {
		return hostname;
	}

	/**
	 * @param hostname the hostname to set
	 */
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	/**
	 * @return the port1
	 */
	public String getPort1() {
		return port1;
	}

	/**
	 * @param port1 the port1 to set
	 */
	public void setPort1(String port1) {
		this.port1 = port1;
	}

	/**
	 * @return the port2
	 */
	public String getPort2() {
		return port2;
	}

	/**
	 * @param port2 the port2 to set
	 */
	public void setPort2(String port2) {
		this.port2 = port2;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 
	 * @return
	 */
	public static String[] getProxyNames() {
		LOGGER.info("Entering getProxyNames()");
		String[] proxyNames = new String[proxies.size()];
		Iterator<ProxyContainer> itr = proxies.iterator();
		int x = 0;
		while (itr.hasNext()) {
			proxyNames[x++] = itr.next().getProxyname();
		}
		LOGGER.debug("ProxyNames: " + proxyNames);
		LOGGER.info("Exiting getProxyNames()");
		return proxyNames;
	}

	/**
	 * 
	 * @param proxyName
	 * @return
	 */
	public static ProxyContainer getProxyByName(String proxyName) {
		LOGGER.info("Entering getProxyByName()");
		ProxyContainer proxyContainer = null;
		Iterator<ProxyContainer> itr = proxies.iterator();
		while (itr.hasNext()) {
			proxyContainer = itr.next();
			if (proxyName != null && proxyName.equals(proxyContainer.getProxyname())) {
				break;
			} else {
				proxyContainer = null;
			}
		}
		LOGGER.debug("ProxyContainer: " + proxyContainer);
		LOGGER.info("Exiting getProxyByName()");
		return proxyContainer;
	}

	/**
	 * @return the proxies
	 */
	public static Set<ProxyContainer> getProxies() {
		return proxies;
	}

	/**
	 * 
	 * @param properties
	 * @param name
	 * @return
	 */
	public static Set<ProxyContainer> getPropertyList(Properties properties, String name) 
	{
		LOGGER.info("Entering getPropertyList()");
	    Set<ProxyContainer> result = new HashSet<ProxyContainer>();
	    for (Map.Entry<Object, Object> entry : properties.entrySet())
	    {
	        if (((String)entry.getKey()).matches("^" + Pattern.quote(name) + "\\.\\d+$"))
	        {
	        	LOGGER.debug("Key: " + (String)entry.getKey());
	        	String nameKey =  (String)entry.getKey();
	        	int index = nameKey.lastIndexOf(".");
	        	String numValue = nameKey.substring(index + 1);
	        	String hostName = "proxy.hostname." + numValue;
	        	String port1 = "proxy.port1." + numValue; 
	        	String port2 = "proxy.port2." + numValue;
	        	String username = "proxy.username." + numValue;
	        	String password = "proxy.password." + numValue;

		        final ProxyContainer proxyContainer = new ProxyContainer();
		        proxyContainer.setProxyname((String) entry.getValue());
		        proxyContainer.setHostname(properties.getProperty(hostName));
		        proxyContainer.setPort1(properties.getProperty(port1));
		        proxyContainer.setPort2(properties.getProperty(port2));
		        proxyContainer.setUsername(properties.getProperty(username));
		        proxyContainer.setPassword(properties.getProperty(password));
		        LOGGER.debug("ProxyContainer: " + proxyContainer);

	            result.add(proxyContainer);
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
		return "ProxyContainer [proxyname=" + proxyname + ", hostname=" + hostname + ", port1=" + port1 + ", port2="
				+ port2 + ", username=" + username + ", password=" + password + "]";
	}
}
