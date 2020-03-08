package com.ticketadvantage.services.httpclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.sites.ProxyContainer;
import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.CookieData;

/**
 *    
 * @author jmiller
 *
 */
public class HttpClientWrapper {
	private static final Logger LOGGER = org.apache.log4j.Logger.getLogger(HttpClientWrapper.class);
	protected HttpClient client;
	protected String host;
	protected String domain;
	protected String webappname = "";
	protected String username;
	protected String password;
	protected String cookies;
	protected String previousUrl = "";
	protected String previousXhtml = "";
	protected String proxyName = "None";
	protected HttpClientContext context = HttpClientContext.create();
	protected BasicCookieStore cookieStore;
	protected boolean isMobile = false;
	protected boolean showRequestResponse = false;
	protected List<CookieData> cookieMap = new ArrayList<CookieData>();

	/**
	 * 
	 */
	public HttpClientWrapper() {
		super();
	}

	/**
	 * 
	 * @param host
	 * @param username
	 * @param password
	 */
	public HttpClientWrapper(String host, String username, String password) {
		super();
		this.host = host;
		this.username = username;
		this.password = password;
		try {
			URI uri = new URI(host);
			domain = uri.getHost();
		} catch (Throwable t) {
			LOGGER.error("Error parsing host" + host, t);
		}
	}

	/**
	 * 
	 * @param client
	 * @param host
	 */
	public HttpClientWrapper(HttpClient client, String host) {
		super();
		this.client = client;
		this.host = host;
		try {
			URI uri = new URI(host);
			domain = uri.getHost();
		} catch (Throwable t) {
			LOGGER.error("Error parsing host" + host, t);
		}
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final HttpClientWrapper client = new HttpClientWrapper("http://www.windycity.ws", "username", "password");
			String action = client.setupAction("./WagerTicket.aspx", null);
			LOGGER.error("action: " + action);
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}
	}

	/**
	 * @return the client
	 */
	public HttpClient getClient() {
		return client;
	}

	/**
	 * @param client the client to set
	 */
	public void setClient(HttpClient client) {
		this.client = client;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * @param domain the domain to set
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}

	/**
	 * @return the webappname
	 */
	public String getWebappname() {
		return webappname;
	}

	/**
	 * @param webappname the webappname to set
	 */
	public void setWebappname(String webappname) {
		this.webappname = webappname;
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
	 * @return the cookies
	 */
	public String getCookies() {
		return cookies;
	}

	/**
	 * @param cookies the cookies to set
	 */
	public void setCookies(String cookies) {
		this.cookies = cookies;
	}

	/**
	 * @return the previousUrl
	 */
	public String getPreviousUrl() {
		return previousUrl;
	}

	/**
	 * @param previousUrl the previousUrl to set
	 */
	public void setPreviousUrl(String previousUrl) {
		this.previousUrl = previousUrl;
	}

	/**
	 * @return the previousXhtml
	 */
	public String getPreviousXhtml() {
		return previousXhtml;
	}

	/**
	 * @param previousXhtml the previousXhtml to set
	 */
	public void setPreviousXhtml(String previousXhtml) {
		this.previousXhtml = previousXhtml;
	}

	/**
	 * @return the proxyName
	 */
	public String getProxyName() {
		return proxyName;
	}

	/**
	 * @param proxyName the proxyName to set
	 */
	public void setProxyName(String proxyName) {
		this.proxyName = proxyName;
	}

	/**
	 * @return the cookieStore
	 */
	public BasicCookieStore getCookieStore() {
		return cookieStore;
	}

	/**
	 * @param cookieStore the cookieStore to set
	 */
	public void setCookieStore(BasicCookieStore cookieStore) {
		this.cookieStore = cookieStore;
	}

	/**
	 * @return the isMobile
	 */
	public boolean isMobile() {
		return isMobile;
	}

	/**
	 * @param isMobile the isMobile to set
	 */
	public void setMobile(boolean isMobile) {
		this.isMobile = isMobile;
	}

	/**
	 * @return the showRequestResponse
	 */
	public boolean isShowRequestResponse() {
		return showRequestResponse;
	}

	/**
	 * @param showRequestResponse the showRequestResponse to set
	 */
	public void setShowRequestResponse(boolean showRequestResponse) {
		this.showRequestResponse = showRequestResponse;
	}

	/**
	 * 
	 * @param action
	 * @return
	 */
	public String populateUrl(String action) {
		return this.getHost() + "/" + this.getWebappname() + "/" + action;
	}

	/**
	 * @return the cookieMap
	 */
	public List<CookieData> getCookieMap() {
		return cookieMap;
	}

	/**
	 * @param cookieMap the cookieMap to set
	 */
	public void setCookieMap(List<CookieData> cookieMap) {
		this.cookieMap = cookieMap;
	}

	/**
	 * @return the context
	 */
	public HttpClientContext getContext() {
		return context;
	}

	/**
	 * @param context the context to set
	 */
	public void setContext(HttpClientContext context) {
		this.context = context;
	}

	/**
	 * 
	 * @param retValue
	 * @param siteHost
	 * @param headerValuePairs
	 * @param urlSection
	 * @param prefix
	 * @param locationName
	 * @param xhtml
	 * @return
	 * @throws BatchException
	 */
	public List<NameValuePair> checkForRedirectWithLocation(List<NameValuePair> retValue, String siteHost,
			List<NameValuePair> headerValuePairs, String urlSection, String prefix, String locationName, String xhtml) throws BatchException {
		LOGGER.info("Entering checkForRedirectWithLocation()");
		LOGGER.debug("siteHost: " + siteHost);
		LOGGER.debug("urlSection: " + urlSection);
		LOGGER.debug("prefix: " + prefix);
		LOGGER.debug("locationName: " + locationName);

		// Check for redirect
		String redirect = getRedirectLocation(retValue);
		LOGGER.debug("Location: " + redirect);
		if (redirect != null && redirect.contains(locationName)) {
			LOGGER.error("CHECK LOCATION NAME: " + redirect);
			LOGGER.error("xhtml: " + xhtml);
		}
		if (redirect != null && redirect.contains("../")) {
			redirect = redirect.replaceAll("../", "/");
		}

		// Make sure there is a valid redirect URL
		if (redirect != null && redirect.length() > 0) {
			// Make sure full url is setup
			if (!redirect.contains("://")) {
				if (!redirect.contains(prefix)) {
					redirect = prefix + redirect;
				}

				// Check for a valid url section
				if (urlSection != null && urlSection.length() > 0) {
					String upperUrl = urlSection.toUpperCase();
					String lowerUrl = urlSection.toLowerCase();
					if (redirect.startsWith(upperUrl) || redirect.startsWith(lowerUrl)) {
						int index = redirect.indexOf(upperUrl);
						if (index != -1) {
							redirect = redirect.substring(index + upperUrl.length());
						} else {
							index = redirect.indexOf(lowerUrl);
							if (index != -1) {
								redirect = redirect.substring(index + lowerUrl.length());
							}
						}
					}
				} else if (!redirect.startsWith("/")) {
					redirect = "/" + redirect;
				}
				redirect = siteHost + redirect;
			}

			// Call the page
			retValue = getSitePage(redirect, cookies, headerValuePairs);
			String tempxhtml = getCookiesAndXhtml(retValue);
			LOGGER.debug("XHTML: " + tempxhtml);
			retValue = checkForRedirectWithLocation(retValue, siteHost, headerValuePairs, urlSection, prefix, locationName, tempxhtml);
		}

		LOGGER.info("Exiting checkForRedirectWithLocation()");
		return retValue;
	}

	/**
	 * 
	 * @param retValue
	 * @param siteHost
	 * @param headerValuePairs
	 * @param urlSection
	 * @param prefix
	 * @param locationName
	 * @param xhtml
	 * @return
	 * @throws BatchException
	 */
	public List<NameValuePair> checkForRedirectWithLocationNoBr(List<NameValuePair> retValue, String siteHost,
			List<NameValuePair> headerValuePairs, String urlSection, String prefix, String locationName, String xhtml) throws BatchException {
		LOGGER.info("Entering checkForRedirectWithLocationNoBr()");
		LOGGER.debug("siteHost: " + siteHost);
		LOGGER.debug("urlSection: " + urlSection);
		LOGGER.debug("prefix: " + prefix);
		LOGGER.debug("locationName: " + locationName);

		// Check for redirect
		String redirect = getRedirectLocation(retValue);
		LOGGER.debug("Location: " + redirect);
		if (redirect != null && redirect.contains(locationName)) {
			LOGGER.error("CHECK LOCATION NAME: " + redirect);
			LOGGER.error("xhtml: " + xhtml);
		}
		if (redirect != null && redirect.contains("../")) {
			redirect = redirect.replaceAll("../", "/");
		}

		// Make sure there is a valid redirect URL
		if (redirect != null && redirect.length() > 0) {
			// Make sure full url is setup
			if (!redirect.contains("://")) {
				if (!redirect.contains(prefix)) {
					redirect = prefix + redirect;
				}

				// Check for a valid url section
				if (urlSection != null && urlSection.length() > 0) {
					String upperUrl = urlSection.toUpperCase();
					String lowerUrl = urlSection.toLowerCase();
					if (redirect.startsWith(upperUrl) || redirect.startsWith(lowerUrl)) {
						int index = redirect.indexOf(upperUrl);
						if (index != -1) {
							redirect = redirect.substring(index + upperUrl.length());
						} else {
							index = redirect.indexOf(lowerUrl);
							if (index != -1) {
								redirect = redirect.substring(index + lowerUrl.length());
							}
						}
					}
				} else if (!redirect.startsWith("/")) {
					redirect = "/" + redirect;
				}

				redirect = siteHost + redirect;
			}

			// Call the page
			retValue = getSitePageNoBr(redirect, cookies, headerValuePairs);
			String tempxhtml = getCookiesAndXhtml(retValue);
			LOGGER.debug("XHTML: " + tempxhtml);
			retValue = checkForRedirectWithLocationNoBr(retValue, siteHost, headerValuePairs, urlSection, prefix, locationName, tempxhtml);
		}

		LOGGER.info("Exiting checkForRedirectWithLocationNoBr()");
		return retValue;
	}

	/**
	 * 
	 * @param retValue
	 * @param siteHost
	 * @param headerValuePairs
	 * @param urlSection
	 * @param prefix
	 * @return
	 * @throws BatchException
	 */
	public List<NameValuePair> checkForRedirect(List<NameValuePair> retValue, String siteHost,
			List<NameValuePair> headerValuePairs, String urlSection, String prefix) throws BatchException {
		LOGGER.info("Entering checkForRedirect()");
		LOGGER.debug("siteHost: " + siteHost);
		LOGGER.debug("urlSection: " + urlSection);
		LOGGER.debug("prefix: " + prefix);

		// Check for redirect
		String redirect = getRedirectLocation(retValue);
		LOGGER.debug("Location: " + redirect);

		if (redirect != null && redirect.contains("../")) {
			redirect = redirect.replace("../", "/");
		}

		// Make sure there is a valid redirect URL
		if (redirect != null && redirect.length() > 0) {
			// Make sure full url is setup
			if (!redirect.contains("://")) {
				if (prefix != null && !prefix.contains("null") && !redirect.contains(prefix)) {
					redirect = prefix + redirect;
				}

				// Check for a valid url section
				if (urlSection != null && urlSection.length() > 0) {
					String upperUrl = urlSection.toUpperCase();
					String lowerUrl = urlSection.toLowerCase();
					if (redirect.startsWith(upperUrl) || redirect.startsWith(lowerUrl)) {
						int index = redirect.indexOf(upperUrl);
						if (index != -1) {
							redirect = redirect.substring(index + upperUrl.length());
						} else {
							index = redirect.indexOf(lowerUrl);
							if (index != -1) {
								redirect = redirect.substring(index + lowerUrl.length());
							}
						}
					}
				} else if (!redirect.startsWith("/")) {
					redirect = "/" + redirect;
				}

				redirect = siteHost + redirect;
			}

			// Call the page
			retValue = getSitePage(redirect, cookies, headerValuePairs);
			String xhtml = getCookiesAndXhtml(retValue);
			LOGGER.debug("XHTML: " + xhtml);
			retValue = checkForRedirect(retValue, siteHost, headerValuePairs, urlSection, prefix);
		}

		LOGGER.info("Exiting checkForRedirect()");
		return retValue;
	}

	/**
	 * 
	 * @param retValue
	 * @param siteHost
	 * @param headerValuePairs
	 * @param urlSection
	 * @param prefix
	 * @return
	 * @throws BatchException
	 */
	public List<NameValuePair> checkForRedirectNoBr(List<NameValuePair> retValue, String siteHost,
			List<NameValuePair> headerValuePairs, String urlSection, String prefix) throws BatchException {
		LOGGER.info("Entering checkForRedirectNoBr()");
		LOGGER.debug("siteHost: " + siteHost);
		LOGGER.debug("urlSection: " + urlSection);
		LOGGER.debug("prefix: " + prefix);

		// Check for redirect
		String redirect = getRedirectLocation(retValue);
		LOGGER.debug("Location: " + redirect);

		if (redirect != null && redirect.contains("../")) {
			redirect = redirect.replace("../", "/");
		}

		// Make sure there is a valid redirect URL
		if (redirect != null && redirect.length() > 0) {
			// Make sure full url is setup
			if (!redirect.contains("://")) {
				if (prefix != null && !prefix.contains("null") && !redirect.contains(prefix)) {
					redirect = prefix + redirect;
				}

				// Check for a valid url section
				if (urlSection != null && urlSection.length() > 0) {
					String upperUrl = urlSection.toUpperCase();
					String lowerUrl = urlSection.toLowerCase();
					if (redirect.startsWith(upperUrl) || redirect.startsWith(lowerUrl)) {
						int index = redirect.indexOf(upperUrl);
						if (index != -1) {
							redirect = redirect.substring(index + upperUrl.length());
						} else {
							index = redirect.indexOf(lowerUrl);
							if (index != -1) {
								redirect = redirect.substring(index + lowerUrl.length());
							}
						}
					}
				} else if (!redirect.startsWith("/")) {
					redirect = "/" + redirect;
				}

				redirect = siteHost + redirect;
			}

			// Call the page
			retValue = getSitePageNoBr(redirect, cookies, headerValuePairs);
			String xhtml = getCookiesAndXhtml(retValue);
			LOGGER.debug("XHTML: " + xhtml);
			retValue = checkForRedirectNoBr(retValue, siteHost, headerValuePairs, urlSection, prefix);
		}

		LOGGER.info("Exiting checkForRedirectNoBr()");
		return retValue;
	}

	/**
	 * 
	 * @param retValue
	 * @return
	 */
	public String getRedirectLocation(List<NameValuePair> retValue) {
		LOGGER.info("Entering getRedirectLocation()");
		String redirect = null;
		
		// Check for value data
		if (retValue != null && !retValue.isEmpty()) {
			final Iterator<NameValuePair> itr = retValue.iterator();
			while (itr != null && itr.hasNext()) {
				final NameValuePair nvp = itr.next();
				String name = nvp.getName();
				String value = nvp.getValue();
				if ("Location".equals(name)) {
					redirect = value;
				}
			}
		}

		LOGGER.debug("Location: " + redirect);
		LOGGER.info("Exiting getRedirectLocation()");
		return redirect;
	}

	/**
	 * 
	 * @param retValue
	 * @return
	 */
	public String getCookiesAndXhtml(List<NameValuePair> retValue) {
		LOGGER.info("Entering getCookiesAndXhtml()");
		String xhtml = "";

		// Get all new cookies and the XHTML for website
		if (retValue != null && !retValue.isEmpty()) {
			Iterator<NameValuePair> itr = retValue.iterator();
			while (itr != null && itr.hasNext()) {
				NameValuePair nvp = (NameValuePair) itr.next();
				LOGGER.info("Header Name: " + nvp.getName());
				if ("Set-Cookie".equals(nvp.getName())) {
					cookies += nvp.getValue();
				} else if ("xhtml".equals(nvp.getName())) {
					xhtml = nvp.getValue();
				} else if ("json".equals(nvp.getName())) {
					xhtml = nvp.getValue();
				}
			}
		}

		LOGGER.info("Exiting getCookiesAndXhtml()");
		return xhtml;
	}

	/**
	 * 
	 * @param retValue
	 * @return
	 */
	public String getCookiesAndJSON(List<NameValuePair> retValue) {
		LOGGER.info("Entering getCookiesAndJSON()");
		String xhtml = "";

		// Get all new cookies and the XHTML for website
		if (retValue != null && !retValue.isEmpty()) {
			Iterator<NameValuePair> itr = retValue.iterator();
			while (itr != null && itr.hasNext()) {
				NameValuePair nvp = (NameValuePair) itr.next();
				LOGGER.info("Header Name: " + nvp.getName());
				if ("Set-Cookie".equals(nvp.getName())) {
					cookies += nvp.getValue();
				} else if ("json".equals(nvp.getName())) {
					xhtml = nvp.getValue();
				}
			}
		}
		LOGGER.info("Exiting getCookiesAndJSON()");
		return xhtml;
	}

	/**
	 * 
	 * @param url
	 * @return
	 */
	public String determineWebappName(String url) {
		LOGGER.info("Entering determineWebappName()");
		LOGGER.debug("url: " + url);
		String urlProtocol = null;
		String urlHost = null;
		String urlWebapp = null;
		String tempUrl = url;

		if (tempUrl != null && tempUrl.contains("://")) {
			// Now parse out the url
			int bIndex = tempUrl.indexOf("://");
			if (bIndex != -1) {
				urlProtocol = tempUrl.substring(0, bIndex);
				tempUrl = tempUrl.substring(bIndex + "://".length());
				tempUrl = tempUrl.replaceAll("//", "/");
				bIndex = tempUrl.indexOf("/");
				if (bIndex != -1) {
					urlHost = tempUrl.substring(0, bIndex);
					domain = urlHost;
					host = urlProtocol + "://" + urlHost;
					tempUrl = tempUrl.substring(bIndex + "/".length());
					
					LOGGER.debug("urlHost: " + urlHost);
					LOGGER.debug("domain: " + domain);
					LOGGER.debug("host: " + host);
					LOGGER.debug("tempUrl: " + tempUrl);

					bIndex = tempUrl.indexOf("/");
					if (bIndex != -1) {
						urlWebapp = tempUrl.substring(0, bIndex);
					}
				}
			}
		} else {
			if (tempUrl.contains("/")) {
				int bIndex = tempUrl.indexOf("/");
				if (bIndex != -1) {
					String tempurl = tempUrl.substring(bIndex + "/".length());
					int eIndex = tempurl.indexOf("/");
					if (eIndex != -1) {
						urlWebapp = tempurl.substring(0, eIndex);
					}
				}
			}
			url = host + tempUrl;
		}

		webappname = urlWebapp;
		LOGGER.debug("webappname: " + webappname);

		// Check for correct webappname
		int index = url.lastIndexOf("/");
		if (index != -1) {
			tempUrl = url.substring(index + 1);
			LOGGER.debug("tempUrl: " + tempUrl);
			
			if (webappname == null) {
				webappname = "";
			}

			String constructUrl = populateUrl(tempUrl);
			LOGGER.debug("url: " + url);
			LOGGER.debug("constructUrl: " + constructUrl);
			if (!url.equals(constructUrl)) {
				// now get everything in-between
				int index2 = url.indexOf(urlWebapp + "/");
				if (index2 != -1) {
					String fullWebapp = url.substring(index2 + (urlWebapp + "/").length(), index);
					fullWebapp = urlWebapp + "/" + fullWebapp;
					LOGGER.debug("FULLWEBAPP: " + fullWebapp);
					urlWebapp = fullWebapp;
				}
			}
		} 

		webappname = urlWebapp;
		LOGGER.debug("urlWebapp: " + webappname);
		LOGGER.info("Exiting determineWebappName()");
		return urlWebapp;
	}

	/**
	 * 
	 * @param proxyName
	 * @throws BatchException
	 */
	public void setupHttpClient(String proxyName) throws BatchException {
		LOGGER.info("Entering setupHttpClient()");
		LOGGER.info("ProxyName: " + proxyName);

		int CONNECTION_TIMEOUT_MS = 75 * 1000; // Timeout in millis.
		final RequestConfig requestConfig = RequestConfig.custom()
		    .setConnectionRequestTimeout(CONNECTION_TIMEOUT_MS)
		    .setConnectTimeout(CONNECTION_TIMEOUT_MS)
		    .setSocketTimeout(CONNECTION_TIMEOUT_MS)
		    .build();

		if (proxyName != null && proxyName.length() > 0 && !"None".equals(proxyName)) {
			this.proxyName = proxyName;
			final ProxyContainer proxyContainer = ProxyContainer.getProxyByName(proxyName);
			LOGGER.debug("ProxyContainer: " + proxyContainer);
			if (proxyContainer != null) {
				final HttpHost proxy = new HttpHost(proxyContainer.getHostname(), Integer.parseInt(proxyContainer.getPort1()));
				final Credentials credentials = new UsernamePasswordCredentials(proxyContainer.getUsername(), proxyContainer.getPassword());
				final AuthScope authScope = new AuthScope(proxyContainer.getHostname(), Integer.parseInt(proxyContainer.getPort1()));
				final CredentialsProvider credsProvider = new BasicCredentialsProvider();
				credsProvider.setCredentials(authScope, credentials);

				// Initialize the client
				client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).setProxy(proxy).setDefaultCredentialsProvider(credsProvider).setMaxConnPerRoute(1).setMaxConnTotal(1).build();
			} else {
				throw new BatchException("Unable to obtain a valid ProxyContainer");
			}
		} else {
			proxyName = "None";
			client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).setMaxConnPerRoute(1).setMaxConnTotal(1).build();
		}

		LOGGER.info("Exiting setupHttpClient()");
	}

	/**
	 * 
	 * @param value
	 * @param webappName
	 * @return
	 * @throws BatchException
	 */
	public String setupAction(String value, String webappName) throws BatchException {
		LOGGER.info("Entering setupAction()");
		LOGGER.debug("value: " + value);
		LOGGER.debug("webappName: " + webappName);
		String actionLogin = null;

		try {
			URL url = null;
			if (value.contains("://")) {
				url = new URL(value);
				actionLogin = value;
			} else {
				if (value.startsWith("./")) {
					LOGGER.debug("webappName: " + webappName);
					LOGGER.debug("webappname: " + webappname);

					if (webappName != null && webappName.length() > 0) {
						url = new URL(host + "/" + webappName + "/" + value.substring(2));
						actionLogin = host + "/" + webappName + "/" + value.substring(2);
						LOGGER.debug("url: " + url);
					} else if (webappname != null && webappname.length() > 0) {
						url = new URL(host + "/" + webappname + "/" + value.substring(2));
						actionLogin = host + "/" + webappname + "/" + value.substring(2);
						LOGGER.debug("url: " + url);
					} else if (webappname != null && webappname.length() == 0) {
						url = new URL(host + "/" + value.substring(2));
						actionLogin = host + "/" + value.substring(2);
						LOGGER.debug("url: " + url);
					}
				} else if (value.contains("/")) {
					url = new URL(host + value);
					actionLogin = host + value;
				} else {
					if (value.contains("Welcome")) {
						url = new URL(host + value);
						actionLogin = host + value;
					} else {
						if (webappName != null && webappName.length() > 0) {
							url = new URL(host + "/" + webappName + "/" + value);
							actionLogin = host + "/" + webappName + "/" + value;
						} else if (webappname != null && webappname.length() == 0) {
							url = new URL(host + "/" + value);
							actionLogin = host + "/" + value;							
						} else {
							url = new URL(host + "/" + value);
							actionLogin = host + "/" + value;
						}
					}
				}
			}

			String proto = url.getProtocol();
			String hhost = url.getHost();
			domain = hhost;
			host = proto + "://" + hhost;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new BatchException(BatchErrorCodes.SITE_PARSER_EXCEPTION, BatchErrorMessage.SITE_PARSER_EXCEPTION,
					e.getMessage());
		}

		LOGGER.info("Exiting setupAction()");
		return actionLogin;
	}

	/**
	 * 
	 * @param url
	 * @param postValuePairs
	 * @param headerValuePairs
	 * @return
	 * @throws BatchException
	 */
	public List<NameValuePair> authenticateSite(String url, 
			List<NameValuePair> postValuePairs,
			List<NameValuePair> headerValuePairs) throws BatchException {
		LOGGER.info("Entering authenticateSite()");
		LOGGER.debug("URL: " + url);

		// Just call the postSitePage for athentication
		List<NameValuePair> retValue = postSitePage(url, 
				null,
				postValuePairs, 
				headerValuePairs);

		LOGGER.info("Exiting authenticateSite()");
		return retValue;
	}

	/**
	 * 
	 * @param url
	 * @param postValuePairs
	 * @param headerValuePairs
	 * @return
	 * @throws BatchException
	 */
	public List<NameValuePair> authenticateSiteNoBr(String url, 
			List<NameValuePair> postValuePairs,
			List<NameValuePair> headerValuePairs) throws BatchException {
		LOGGER.info("Entering authenticateSiteNoBr()");
		LOGGER.debug("URL: " + url);

		// Just call the postSitePage for athentication
		List<NameValuePair> retValue = postSitePageNoBr(url, 
				null,
				postValuePairs, 
				headerValuePairs);

		LOGGER.info("Exiting authenticateSiteNoBr()");
		return retValue;
	}

	/**
	 * 
	 * @param url
	 * @param postValuePairs
	 * @param headerValuePairs
	 * @return
	 * @throws BatchException
	 */
	public List<NameValuePair> authenticateSiteNoRedirect(String url,
			String cookieValue,
			List<NameValuePair> postValuePairs,
			List<NameValuePair> headerValuePairs) throws BatchException {
		LOGGER.info("Entering authenticateSiteNoRedirect()");
		LOGGER.debug("URL: " + url);
		LOGGER.debug("PostValuePairs: " + postValuePairs);
		LOGGER.debug("HeaderValuePairs: " + headerValuePairs);
		List<NameValuePair> retValue = null;

		// Check for a valid URL
		checkForValidUrl(url);

		if (showRequestResponse) {
			LOGGER.error("HTTP POST REQUEST: " + url);
		}

		// HTTP Post
		final RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT).setRedirectsEnabled(false).build(); //disable redirect
		final HttpPost post = new HttpPost(url);
		post.setConfig(requestConfig); //pass the request config to request
		setupHttpBaseHeaderInfo(post, cookieValue, headerValuePairs, postValuePairs, false);

		// Make the request
		HttpResponse response = null;
		try {
			response = handleRequest(post, url);

			// Handle the response
			retValue = handleResponse(response, url, "xhtml");
			previousUrl = url;
		} catch (ClientProtocolException cpe) {
			retValue = handleCirularRedirect(cpe, url, post, null, headerValuePairs);
		} catch (BatchException be) {
			throw be;
		} finally {
			post.releaseConnection();
		}

		LOGGER.info("Exiting authenticateSiteNoRedirect()");
		return retValue;
	}

	/**
	 * 
	 * @param url
	 * @param cookieValue
	 * @param postValuePairs
	 * @param headerValuePairs
	 * @return
	 * @throws BatchException
	 */
	public List<NameValuePair> postSitePage(String url, 
			String cookieValue,
			List<NameValuePair> postValuePairs, 
			List<NameValuePair> headerValuePairs) throws BatchException {
		LOGGER.info("Entering postSitePage()");
		LOGGER.debug("URL: " + url);
		LOGGER.debug("CookieValue: " + cookieValue);
		List<NameValuePair> retValue = null;

		// Check for a valid URL
		checkForValidUrl(url);

		if (showRequestResponse) {
			LOGGER.error("HTTP POST REQUEST: " + url);
		}

		// HTTP Post
		final HttpPost post = new HttpPost(url);
		setupHttpBaseHeaderInfo(post, cookieValue, headerValuePairs, postValuePairs, false);

		// Make the request
		HttpResponse response = null;
		try {
			response = handleRequest(post, url);

			// Handle the response
			retValue = handleResponse(response, url, "xhtml");
			previousUrl = url;
		} catch (ClientProtocolException cpe) {
			retValue = handleCirularRedirect(cpe, url, post, null, headerValuePairs);
		} catch (BatchException be) {
			throw be;
		} finally {
			post.releaseConnection();
		}

		LOGGER.info("Exiting postSitePage()");
		return retValue;
	}

	/**
	 * 
	 * @param url
	 * @param cookieValue
	 * @param postValuePairs
	 * @param headerValuePairs
	 * @return
	 * @throws BatchException
	 */
	public List<NameValuePair> postSitePageAccessAll(String url, 
			String cookieValue,
			List<NameValuePair> headerValuePairs,
			List<NameValuePair> postValuePairs) throws BatchException {
		LOGGER.info("Entering postSitePageAccessAll()");
		LOGGER.debug("URL: " + url);
		LOGGER.debug("CookieValue: " + cookieValue);
		List<NameValuePair> retValue = null;

		// Check for a valid URL
		checkForValidUrl(url);

		if (showRequestResponse) {
			LOGGER.error("HTTP POST REQUEST: " + url);
		}

		// HTTP Post
		final HttpPost post = new HttpPost(url);
		setupHttpBaseHeaderInfo(post, cookieValue, headerValuePairs, postValuePairs, true);

		// Make the request
		HttpResponse response = null;
		try {
			response = handleRequest(post, url);

			// Handle the response
			retValue = handleResponse(response, url, "xhtml");
			previousUrl = url;
		} catch (ClientProtocolException cpe) {
			retValue = handleCirularRedirect(cpe, url, post, null, headerValuePairs);
		} catch (BatchException be) {
			throw be;
		} finally {
			post.releaseConnection();
		}

		LOGGER.info("Exiting postSitePageAccessAll()");
		return retValue;
	}

	/**
	 * 
	 * @param url
	 * @param cookieValue
	 * @param postValuePairs
	 * @param headerValuePairs
	 * @return
	 * @throws BatchException
	 */
	public List<NameValuePair> postSitePageNoHeader(String url, 
			List<NameValuePair> postValuePairs, 
			List<NameValuePair> headerValuePairs) throws BatchException {
		LOGGER.info("Entering postSitePage()");
		LOGGER.debug("URL: " + url);
		List<NameValuePair> retValue = null;

		// Check for a valid URL
		checkForValidUrl(url);

		if (showRequestResponse) {
			LOGGER.error("HTTP POST REQUEST: " + url);
		}

		// HTTP Post
		final HttpPost post = new HttpPost(url);

		// Setup header attributes
		if (headerValuePairs != null && !headerValuePairs.isEmpty()) {
			final Iterator<NameValuePair> itr = headerValuePairs.iterator();
			while (itr != null && itr.hasNext()) {
				final NameValuePair nvp = itr.next();
				String name = nvp.getName();
				String value = nvp.getValue();

				// Set the get header values
				post.addHeader(name, value);
			}
		}

		// Check for valid post parameter values
		if (postValuePairs != null && postValuePairs.size() > 0) {
			// Set the post parameters
			for (NameValuePair postValuePair: postValuePairs) {
				if (showRequestResponse) {
					LOGGER.error(postValuePair.getName() + ": " + postValuePair.getValue());
				}
			}

			try {
				post.setEntity(new UrlEncodedFormEntity(postValuePairs));
			} catch (UnsupportedEncodingException uee) {
				LOGGER.error(uee.getMessage(), uee);
			}
		}

		// Make the request
		HttpResponse response = null;
		try {
			response = handleRequest(post, url);

			// Handle the response
			retValue = handleResponse(response, url, "xhtml");
			previousUrl = url;
		} catch (ClientProtocolException cpe) {
			retValue = handleCirularRedirect(cpe, url, post, null, headerValuePairs);
		} catch (BatchException be) {
			throw be;
		} finally {
			post.releaseConnection();
		}

		LOGGER.info("Exiting postSitePage()");
		return retValue;
	}

	/**
	 * 
	 * @param url
	 * @param cookieValue
	 * @param postValuePairs
	 * @param headerValuePairs
	 * @return
	 * @throws BatchException
	 */
	public List<NameValuePair> postSitePageNoBr(String url, 
			String cookieValue,
			List<NameValuePair> postValuePairs, 
			List<NameValuePair> headerValuePairs) throws BatchException {
		LOGGER.info("Entering postSitePageNoBr()");
		LOGGER.debug("URL: " + url);
		LOGGER.debug("CookieValue: " + cookieValue);
		List<NameValuePair> retValue = null;

		// Check for a valid URL
		checkForValidUrl(url);

		if (showRequestResponse) {
			LOGGER.error("HTTP POST REQUEST: " + url);
		}

		// HTTP Post
		final HttpPost post = new HttpPost(url);
		setupHttpBaseHeaderInfoNoBr(post, cookieValue, headerValuePairs, postValuePairs, false);

		// Make the request
		HttpResponse response = null;
		try {
			response = handleRequest(post, url);

			// Handle the response
			retValue = handleResponse(response, url, "xhtml");
			previousUrl = url;
		} catch (ClientProtocolException cpe) {
			retValue = handleCirularRedirect(cpe, url, post, null, headerValuePairs);
		} catch (BatchException be) {
			throw be;
		} finally {
			post.releaseConnection();
		}

		LOGGER.info("Exiting postSitePageNoBr()");
		return retValue;
	}

	/**
	 * 
	 * @param url
	 * @param cookieValue
	 * @param jsonString
	 * @param headerValuePairs
	 * @return
	 * @throws BatchException
	 */
	public List<NameValuePair> postJSONSite(String url, 
			String cookieValue,
			String jsonString, 
			List<NameValuePair> headerValuePairs) throws BatchException {
		LOGGER.info("Entering postJSONSite()");
		LOGGER.debug("URL: " + url);
		LOGGER.debug("CookieValue: " + cookieValue);
		LOGGER.debug("jsonString: " + jsonString);
		List<NameValuePair> retValue = null;

		// Check for a valid URL
		checkForValidUrl(url);

		if (showRequestResponse) {
			LOGGER.error("HTTP POST JSON REQUEST: " + url);
		}

		// HTTP Post
		final HttpPost post = new HttpPost(url);
		setupJSONHttpBaseHeaderInfo(post, cookieValue, headerValuePairs, jsonString, true);

		// Make the request
		HttpResponse response = null;
		try {
			response = handleRequest(post, url);

			// Handle the response
			retValue = handleResponse(response, url, "json");
			previousUrl = url;
		} catch (ClientProtocolException cpe) {
			retValue = handleCirularRedirect(cpe, url, post, null, headerValuePairs);
		} catch (BatchException be) {
			throw be;
		} finally {
			post.releaseConnection();
		}

		LOGGER.info("Exiting postJSONSite()");
		return retValue;
	}

	/**
	 * 
	 * @param url
	 * @param cookieValue
	 * @param postValuePairs
	 * @param headerValuePairs
	 * @return
	 * @throws BatchException
	 */
	public List<NameValuePair> postSitePageNoRedirect(String url, 
			String cookieValue,
			List<NameValuePair> postValuePairs, 
			List<NameValuePair> headerValuePairs) throws BatchException {
		LOGGER.info("Entering postSitePageNoRedirect()");
		LOGGER.debug("URL: " + url);
		LOGGER.debug("CookieValue: " + cookieValue);
		LOGGER.debug("PostValuePairs: " + postValuePairs);
		List<NameValuePair> retValue = null;
			
		// Check for a valid URL
		checkForValidUrl(url);

		if (showRequestResponse) {
			LOGGER.error("HTTP POST REQUEST: " + url);
		}

		// HTTP Post
		final RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT).setRedirectsEnabled(false).build(); //disable redirect
		final HttpPost post = new HttpPost(url);
		post.setConfig(requestConfig); //pass the request config to request

		// Set the get header values
		post.addHeader("Accept", "text/html,*/*;q=0.01");
		post.addHeader("Accept-Encoding", "gzip, deflate, br");
		post.addHeader("Accept-Language", "en-US,en;q=0.5");
		post.addHeader("Connection", "keep-alive");
		if (!proxyName.equals("None")) {
			post.addHeader("Proxy-Authorization", "Basic dGlja2V0YWR2YW50YWdlOnBhc3QzeXdpbnRlcg==");
		}

		// Check if this is the mobile view
		// post.addHeader("User-Agent", "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; Trident/6.0)");
		post.addHeader("User-Agent", "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; Trident/6.0)");

		// Should we show what we are sending?
		if (showRequestResponse) {
			LOGGER.error("Accept: text/html, */*; q=0.01");
			LOGGER.error("Accept-Encoding: gzip, deflate");
			LOGGER.error("Accept-Language: en-US,en;q=0.8");
			LOGGER.error("Connection: keep-alive");
			if (!proxyName.equals("None")) {
				LOGGER.error("Proxy-Authorization: Basic dGlja2V0YWR2YW50YWdlOnBhc3QzeXdpbnRlcg==");
			}

			LOGGER.error("User-Agent: Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; Trident/6.0)");
		}

		// Setup the remaining header data
		boolean hostfound = setupPairValues(post, 
				cookieValue, 
				headerValuePairs, 
				postValuePairs,
				null);

		if (!hostfound) {
			post.addHeader("Host", domain);
			if (showRequestResponse) {
				LOGGER.error("Host: " + domain);
			}
		}

		// Make the request
		HttpResponse response = null;
		try {
			response = handleRequest(post, url);

			// Handle the response
			retValue = handleResponse(response, url, "xhtml");
			previousUrl = url;
		} catch (ClientProtocolException cpe) {
			retValue = handleCirularRedirect(cpe, url, post, null, headerValuePairs);
		} catch (BatchException be) {
			throw be;
		} finally {
			post.releaseConnection();
		}

		LOGGER.info("Exiting postSitePageNoRedirect()");
		return retValue;
	}

	/**
	 * 
	 * @param url
	 * @param cookieValue
	 * @param jsonString
	 * @param headerValuePairs
	 * @return
	 * @throws BatchException
	 */
	public List<NameValuePair> postJSONNoRedirectSite(String url, 
			String cookieValue,
			String jsonString, 
			List<NameValuePair> headerValuePairs) throws BatchException {
		LOGGER.info("Entering postJSONNoRedirectSite()");
		LOGGER.debug("URL: " + url);
		LOGGER.debug("CookieValue: " + cookieValue);
		LOGGER.debug("jsonString: " + jsonString);
		List<NameValuePair> retValue = null;

		// Check for a valid URL
		checkForValidUrl(url);

		if (showRequestResponse) {
			LOGGER.error("HTTP POST REQUEST: " + url);
		}

		// HTTP Post
		final RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT).setRedirectsEnabled(false).build(); //disable redirect
		final HttpPost post = new HttpPost(url);
		post.setConfig(requestConfig); //pass the request config to request

		// Set the get header values
		post.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		post.addHeader("Accept-Encoding", "gzip, deflate, br");
		post.addHeader("Accept-Language", "en-US,en;q=0.5");
		post.addHeader("Connection", "keep-alive");
		post.addHeader("Content-Type", "application/x-www-form-urlencoded");
		if (!proxyName.equals("None")) {
			post.addHeader("Proxy-Authorization", "Basic dGlja2V0YWR2YW50YWdlOnBhc3QzeXdpbnRlcg==");
		}

		// Check if this is the mobile view
		post.addHeader("User-Agent", "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; Trident/6.0)");

		// Should we show what we are sending?
		if (showRequestResponse) {
			LOGGER.error("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			LOGGER.error("Accept-Encoding: gzip, deflate");
			LOGGER.error("Accept-Language: en-US,en;q=0.8");
			LOGGER.error("Connection: keep-alive");
			LOGGER.error("Content-Type: application/x-www-form-urlencoded");
			if (!proxyName.equals("None")) {
				LOGGER.error("Proxy-Authorization: Basic dGlja2V0YWR2YW50YWdlOnBhc3QzeXdpbnRlcg==");
			}

			LOGGER.error("User-Agent: Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; Trident/6.0)");
		}

		// Setup the remaining header data
		boolean hostfound = setupPairValues(post, 
				cookieValue, 
				headerValuePairs, 
				null,
				jsonString);

		if (!hostfound) {
			post.addHeader("Host", domain);
			if (showRequestResponse) {
				LOGGER.error("Host: " + domain);
			}
		}

		// Make the request
		HttpResponse response = null;
		try {
			response = handleRequest(post, url);

			// Handle the response
			retValue = handleResponse(response, url, "json");
			previousUrl = url;
		} catch (ClientProtocolException cpe) {
			retValue = handleCirularRedirect(cpe, url, post, null, headerValuePairs);
		} catch (BatchException be) {
			throw be;
		} finally {
			post.releaseConnection();
		}

		LOGGER.info("Exiting postJSONNoRedirectSite()");
		return retValue;
	}

	/**
	 * 
	 * @param url
	 * @param cookieValue
	 * @param headerValuePairs
	 * @return
	 * @throws BatchException
	 */
	public List<NameValuePair> getSitePage(String url, 
			String cookieValue,
			List<NameValuePair> headerValuePairs) throws BatchException {
		LOGGER.info("Entering getSitePage()");
		LOGGER.debug("URL: " + url);
		LOGGER.debug("cookieValue: " + cookieValue);
		List<NameValuePair> retValue = null;

		// Check for a valid URL
		checkForValidUrl(url);

		if (showRequestResponse) {
			LOGGER.error("HTTP GET REQUEST: " + url);
		}

		// HTTP Get
		final HttpGet get = new HttpGet(url);
		setupHttpBaseHeaderInfo(get, cookieValue, headerValuePairs, null, false);

		// Make the request
		HttpResponse response = null;
		try {
			response = handleRequest(get, url);

			// Handle the response
			retValue = handleResponse(response, url, "xhtml");
			previousUrl = url;
		} catch (ClientProtocolException cpe) {
			retValue = handleCirularRedirect(cpe, url, get, null, headerValuePairs);
		} catch (BatchException be) {
			throw be;
		} finally {
			get.releaseConnection();
		}

		LOGGER.info("Exiting getSitePage()");
		return retValue;
	}

	/**
	 * 
	 * @param url
	 * @param cookieValue
	 * @param headerValuePairs
	 * @return
	 * @throws BatchException
	 */
	public List<NameValuePair> getSitePageNoBr(String url, 
			String cookieValue,
			List<NameValuePair> headerValuePairs) throws BatchException {
		LOGGER.info("Entering getSitePageNoBr()");
		LOGGER.debug("URL: " + url);
		LOGGER.debug("cookieValue: " + cookieValue);
		List<NameValuePair> retValue = null;

		// Check for a valid URL
		checkForValidUrl(url);

		if (showRequestResponse) {
			LOGGER.error("HTTP GET REQUEST: " + url);
		}

		// HTTP Get
		final HttpGet get = new HttpGet(url);
		setupHttpBaseHeaderInfoNoBr(get, cookieValue, headerValuePairs, null, false);

		// Make the request
		HttpResponse response = null;
		try {
			response = handleRequest(get, url);
			
			// Handle the response
			retValue = handleResponse(response, url, "xhtml");
			previousUrl = url;
		} catch (ClientProtocolException cpe) {
			retValue = handleCirularRedirect(cpe, url, get, null, headerValuePairs);
		} catch (BatchException be) {
			throw be;
		} finally {
			get.releaseConnection();
		}

		LOGGER.info("Exiting getSitePageNoBr()");
		return retValue;
	}

	/**
	 * 
	 * @param url
	 * @param cookieValue
	 * @param headerValuePairs
	 * @return
	 * @throws BatchException
	 */
	public List<NameValuePair> getJSONSite(String url, 
			String cookieValue,
			List<NameValuePair> headerValuePairs) throws BatchException {
		LOGGER.info("Entering getJSONSite()");
		LOGGER.debug("URL: " + url);
		LOGGER.debug("cookieValue: " + cookieValue);
		List<NameValuePair> retValue = null;

		// Check for a valid URL
		checkForValidUrl(url);

		if (showRequestResponse) {
			LOGGER.error("HTTP GET JSON REQUEST: " + url);
		}

		// HTTP Get
		final HttpGet get = new HttpGet(url);
		setupJSONHttpBaseHeaderInfo(get, null, headerValuePairs, null, false);

		// Make the request
		HttpResponse response = null;
		try {
			response = handleRequest(get, url);

			// Handle the response
			retValue = handleResponse(response, url, "json");
			previousUrl = url;
		} catch (ClientProtocolException cpe) {
			retValue = handleCirularRedirect(cpe, url, get, null, headerValuePairs);
		} catch (BatchException be) {
			throw be;
		} finally {
			get.releaseConnection();
		}

		LOGGER.info("Exiting getJSONSite()");
		return retValue;
	}
	
	/**
	 * 
	 * @param url
	 * @param cookieValue
	 * @param headerValuePairs
	 * @return
	 * @throws BatchException
	 */
	public List<NameValuePair> getJSSite(String url, 
			String cookieValue,
			List<NameValuePair> headerValuePairs) throws BatchException {
		LOGGER.info("Entering getJSSite()");
		LOGGER.debug("URL: " + url);
		LOGGER.debug("cookieValue: " + cookieValue);
		List<NameValuePair> retValue = null;

		// Check for a valid URL
		checkForValidUrl(url);

		if (showRequestResponse) {
			LOGGER.error("HTTP GET JS REQUEST: " + url);
		}

		// HTTP Get
		final HttpGet get = new HttpGet(url);
		setupHttpBaseHeaderInfo(get, null, headerValuePairs, null, true);

		// Make the request
		HttpResponse response = null;
		try {
			response = handleRequest(get, url);
			
			// Handle the response
			retValue = handleResponse(response, url, "json");
			previousUrl = url;
		} catch (ClientProtocolException cpe) {
			retValue = handleCirularRedirect(cpe, url, get, null, headerValuePairs);
		} catch (BatchException be) {
			throw be;
		} finally {
			get.releaseConnection();
		}

		LOGGER.info("Exiting getJSSite()");
		return retValue;
	}

	/**
	 * 
	 * @param url
	 * @param cookieValue
	 * @param headerValuePairs
	 * @return
	 * @throws BatchException
	 */
	public List<NameValuePair> getSitePageNoRedirect(String url, 
			String cookieValue,
			List<NameValuePair> headerValuePairs) throws BatchException {
		LOGGER.info("Entering getSitePageNoRedirect()");
		LOGGER.debug("URL: " + url);
		LOGGER.debug("cookieValue: " + cookieValue);
		List<NameValuePair> retValue = null;

		// Check for a valid URL
		checkForValidUrl(url);

		if (showRequestResponse) {
			LOGGER.error("HTTP GET REQUEST: " + url);
		}

		// HTTP Get
		final HttpGet get = new HttpGet(url);
		final RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT).setRedirectsEnabled(false).build(); // disable redirect
		get.setConfig(requestConfig); // pass the request config to request
		setupHttpBaseHeaderInfo(get, cookieValue, headerValuePairs, null, false);

		// Make the request
		HttpResponse response = null;
		try {
			response = handleRequest(get, url);

			// Handle the response
			retValue = handleResponse(response, url, "xhtml");
			previousUrl = url;
		} catch (ClientProtocolException cpe) {
			retValue = handleCirularRedirect(cpe, url, get, null, headerValuePairs);
		} catch (BatchException be) {
			throw be;
		} finally {
			get.releaseConnection();
		}

		LOGGER.info("Exiting getSitePageNoRedirect()");
		return retValue;
	}

	/**
	 * 
	 * @param httpRequestBase
	 * @param cookieValue
	 * @param headerValuePairs
	 * @param postValuePairs
	 * @param acceptAll
	 * @return
	 */
	protected void setupHttpBaseHeaderInfo(HttpRequestBase httpRequestBase, 
			String cookieValue, 
			List<NameValuePair> headerValuePairs, 
			List<NameValuePair> postValuePairs,
			boolean acceptAll) {
		LOGGER.info("Entering setupHttpBaseHeaderInfo()");

		// Set the get header values
		if (acceptAll) {
			httpRequestBase.addHeader("Accept", "*/*");
		} else {
			httpRequestBase.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		}
		httpRequestBase.addHeader("Accept-Encoding", "gzip, deflate, br");
		httpRequestBase.addHeader("Accept-Language", "en-US,en;q=0.5");
		httpRequestBase.addHeader("Connection", "keep-alive");
		if (!proxyName.equals("None")) {
			httpRequestBase.addHeader("Proxy-Authorization", "Basic dGlja2V0YWR2YW50YWdlOnBhc3QzeXdpbnRlcg==");
		}

		// Check if this is the mobile view
		if (isMobile) {
			httpRequestBase.addHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 7_0_2 like Mac OS X) AppleWebKit/537.51.1 (KHTML, like Gecko) Version/7.0 Mobile/11A501 Safari/9537.53");			
		} else {
//			httpRequestBase.addHeader("User-Agent", "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; Trident/6.0)");
			httpRequestBase.addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.13; rv:50.0) Gecko/20100101 Firefox/50.0");
		}

		// Should we show what we are sending?
		if (showRequestResponse) {
			if (acceptAll) {
				LOGGER.error("Accept: */*");
			} else {
				LOGGER.error("Accept: text/html,application/xhtml+xml,application/xml,*/*");				
			}
			LOGGER.error("Accept-Encoding: gzip, deflate");
			LOGGER.error("Accept-Language: en-US,en;q=0.8");
			LOGGER.error("Connection: keep-alive");
			if (!proxyName.equals("None")) {
				LOGGER.error("Proxy-Authorization: Basic dGlja2V0YWR2YW50YWdlOnBhc3QzeXdpbnRlcg==");
			}
			if (isMobile) {
				LOGGER.error("User-Agent: Mozilla/5.0 (iPhone; CPU iPhone OS 7_0_2 like Mac OS X) AppleWebKit/537.51.1 (KHTML, like Gecko) Version/7.0 Mobile/11A501 Safari/9537.53");			
			} else {
				LOGGER.error("User-Agent: Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; Trident/6.0)");
				// LOGGER.error("User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:47.0) Gecko/20100101 Firefox/47.0");
			}
		}

		// Setup the remaining header data
		boolean hostfound = setupPairValues(httpRequestBase, 
				cookieValue, 
				headerValuePairs, 
				postValuePairs,
				null);

		if (!hostfound) {
			httpRequestBase.addHeader("Host", domain);
			if (showRequestResponse) {
				LOGGER.error("Host: " + domain);
			}
		}

		LOGGER.info("Exiting setupHttpBaseHeaderInfo()");
	}

	/**
	 * 
	 **/
	protected void setupHttpBaseHeaderInfoNoBr(HttpRequestBase httpRequestBase, 
			String cookieValue, 
			List<NameValuePair> headerValuePairs, 
			List<NameValuePair> postValuePairs,
			boolean acceptAll) {
		LOGGER.info("Entering setupHttpBaseHeaderInfoNoBr()");

		// Set the get header values
		if (acceptAll) {
			httpRequestBase.addHeader("Accept", "*/*");
		} else {
			httpRequestBase.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		}
		httpRequestBase.addHeader("Accept-Encoding", "deflate");
		httpRequestBase.addHeader("Accept-Language", "en-US,en;q=0.5");
		httpRequestBase.addHeader("Connection", "keep-alive");
		if (!proxyName.equals("None")) {
			httpRequestBase.addHeader("Proxy-Authorization", "Basic dGlja2V0YWR2YW50YWdlOnBhc3QzeXdpbnRlcg==");
		}

		// Check if this is the mobile view
		if (isMobile) {
			httpRequestBase.addHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 7_0_2 like Mac OS X) AppleWebKit/537.51.1 (KHTML, like Gecko) Version/7.0 Mobile/11A501 Safari/9537.53");			
		} else {
			// httpRequestBase.addHeader("User-Agent", "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; Trident/6.0)");
			httpRequestBase.addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.13; rv:50.0) Gecko/20100101 Firefox/50.0");
		}

		// Should we show what we are sending?
		if (showRequestResponse) {
			if (acceptAll) {
				LOGGER.error("Accept: */*");
			} else {
				LOGGER.error("Accept: text/html,application/xhtml+xml,application/xml,*/*");				
			}
			LOGGER.error("Accept-Encoding: gzip, deflate");
			LOGGER.error("Accept-Language: en-US,en;q=0.8");
			LOGGER.error("Connection: keep-alive");
			if (!proxyName.equals("None")) {
				LOGGER.error("Proxy-Authorization: Basic dGlja2V0YWR2YW50YWdlOnBhc3QzeXdpbnRlcg==");
			}
			if (isMobile) {
				LOGGER.error("User-Agent: Mozilla/5.0 (iPhone; CPU iPhone OS 7_0_2 like Mac OS X) AppleWebKit/537.51.1 (KHTML, like Gecko) Version/7.0 Mobile/11A501 Safari/9537.53");			
			} else {
				LOGGER.error("User-Agent: Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; Trident/6.0)");
				// LOGGER.error("User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:47.0) Gecko/20100101 Firefox/47.0");
			}
		}

		// Setup the remaining header data
		boolean hostfound = setupPairValues(httpRequestBase, 
				cookieValue, 
				headerValuePairs, 
				postValuePairs,
				null);

		if (!hostfound) {
			httpRequestBase.addHeader("Host", domain);
			if (showRequestResponse) {
				LOGGER.error("Host: " + domain);
			}
		}

		LOGGER.info("Exiting setupHttpBaseHeaderInfoNoBr()");
	}

	/**
	 * 
	 * @param httpRequestBase
	 * @param cookieValue
	 * @param headerValuePairs
	 * @param jsonString
	 * @return
	 */
	protected void setupJSONHttpBaseHeaderInfo(HttpRequestBase httpRequestBase, 
			String cookieValue, 
			List<NameValuePair> headerValuePairs, 
			String jsonString,
			boolean includeContentType) {
		LOGGER.info("Entering setupJSONHttpBaseHeaderInfo()");

		// Set the HTTP header values
		httpRequestBase.addHeader("Accept", "application/json, text/plain, */*");
//		httpRequestBase.addHeader("Accept-Encoding", "gzip, deflate, br");
		httpRequestBase.addHeader("Accept-Language", "en-US,en;q=0.5");
		httpRequestBase.addHeader("Connection", "keep-alive");
		// Check to include Content Type or not
		if (includeContentType) {
			httpRequestBase.addHeader("Content-Type", "application/json;charset=UTF-8");
		}
		if (!proxyName.equals("None")) {
			httpRequestBase.addHeader("Proxy-Authorization", "Basic dGlja2V0YWR2YW50YWdlOnBhc3QzeXdpbnRlcg==");
		}

		// Check if this is the mobile view
		if (isMobile) {
			httpRequestBase.addHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 7_0_2 like Mac OS X) AppleWebKit/537.51.1 (KHTML, like Gecko) Version/7.0 Mobile/11A501 Safari/9537.53");			
		} else {
			httpRequestBase.addHeader("User-Agent", "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; Trident/6.0)");
//			httpRequestBase.addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:47.0) Gecko/20100101 Firefox/47.0");
		}

		// Should we show what we are sending?
		if (showRequestResponse) {
			LOGGER.error("Accept: application/json, text/plain, */*");
			LOGGER.error("Accept-Encoding: gzip, deflate, br");
			LOGGER.error("Accept-Language: en-US,en;q=0.5");
			LOGGER.error("Connection: keep-alive");
			if (includeContentType) {
				LOGGER.error("Content-Type: application/json;charset=UTF-8");
			}

			if (!proxyName.equals("None")) {
				LOGGER.error("Proxy-Authorization: Basic dGlja2V0YWR2YW50YWdlOnBhc3QzeXdpbnRlcg==");
			}

			if (isMobile) {
				LOGGER.error("User-Agent: Mozilla/5.0 (iPhone; CPU iPhone OS 7_0_2 like Mac OS X) AppleWebKit/537.51.1 (KHTML, like Gecko) Version/7.0 Mobile/11A501 Safari/9537.53");			
			} else {
				LOGGER.error("User-Agent: Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; Trident/6.0)");
				// LOGGER.error("User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:47.0) Gecko/20100101 Firefox/47.0");
			}
		}

		// Setup the remaining header data
		boolean hostfound = setupPairValues(httpRequestBase, 
				cookieValue, 
				headerValuePairs, 
				null, 
				jsonString);

		if (!hostfound) {
			httpRequestBase.addHeader("Host", domain);
			LOGGER.warn("Host: " + domain);
		}

		LOGGER.info("Exiting setupJSONHttpBaseHeaderInfo()");
	}

	/**
	 * 
	 * @param httpRequestBase
	 * @param cookieValue
	 * @param headerValuePairs
	 * @param postValuePairs
	 * @param jsonString
	 */
	protected boolean setupPairValues(HttpRequestBase httpRequestBase, 
			String cookieValue, 
			List<NameValuePair> headerValuePairs, 
			List<NameValuePair> postValuePairs,
			String jsonString) {
		LOGGER.info("Entering setupPairValues()");
		boolean hostfound = false;

		// Setup cookies if we need to
		if (cookieValue != null && cookieValue.length() > 0) {
			httpRequestBase.addHeader("Cookie", cookieValue);
			if (showRequestResponse) {
				LOGGER.error("cookieValue: " + cookieValue);
			}
		} else if (cookies != null && cookies.length() > 0) {
			httpRequestBase.addHeader("Cookie", cookies);
			if (showRequestResponse) {
				LOGGER.error("cookies: " + cookies);
			}
		}

		// Setup header attributes
		if (headerValuePairs != null && !headerValuePairs.isEmpty()) {
			final Iterator<NameValuePair> itr = headerValuePairs.iterator();
			while (itr != null && itr.hasNext()) {
				final NameValuePair nvp = itr.next();
				String name = nvp.getName();
				String value = nvp.getValue();

				if (name != null && name.equalsIgnoreCase("host")) {
					hostfound = true;
					httpRequestBase.addHeader(name, value);
					if (showRequestResponse) {
						LOGGER.error(name + ": " + value);
					}
				} else {
					httpRequestBase.addHeader(name, value);
					if (showRequestResponse) {
						LOGGER.error(name + ": " + value);
					}
				}

			}
		}

		// Check for valid post parameter values
		if (postValuePairs != null && postValuePairs.size() > 0) {
			// Set the post parameters
			for (NameValuePair postValuePair: postValuePairs) {
				if (showRequestResponse) {
					LOGGER.error(postValuePair.getName() + ": " + postValuePair.getValue());
				}
			}

			try {
				((HttpPost)httpRequestBase).setEntity(new UrlEncodedFormEntity(postValuePairs));
			} catch (UnsupportedEncodingException uee) {
				LOGGER.error(uee.getMessage(), uee);
			}
		}

		// Setup a JSON string if necessary
		if (jsonString != null && jsonString.length() > 0) {
			if (showRequestResponse) {
				LOGGER.error(jsonString);
			}

			try {
				((HttpPost)httpRequestBase).setEntity(new StringEntity(jsonString));
			} catch (UnsupportedEncodingException uee) {
				LOGGER.error(uee.getMessage(), uee);
			}
		}

		LOGGER.info("Exiting setupPairValues()");
		return hostfound;
	}

	/**
	 * 
	 * @param httpRequestBase
	 * @param url
	 * @return
	 * @throws BatchException
	 * @throws ClientProtocolException
	 */
	protected HttpResponse handleRequest(HttpRequestBase httpRequestBase, String url) throws BatchException, ClientProtocolException {
		LOGGER.info("Entering handleRequest()");
		HttpResponse response = null;
		HttpContext localContext = null;

		if (cookieStore != null) {
			localContext = new BasicHttpContext();
			LOGGER.debug("cookieStore: " + cookieStore);
			localContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
		}
		
		// Make the request and handle any exceptions
		try {
			if (cookieStore != null) {
				response = client.execute(httpRequestBase, localContext);
			} else {
				response = client.execute(httpRequestBase, context);
			}
		} catch (SocketTimeoutException ste) {
			LOGGER.error(ste.getMessage(), ste);
//			response = setupRetry(httpRequestBase, localContext);
			throw new BatchException(BatchErrorCodes.HTTP_NETWORK_EXCEPTION, BatchErrorMessage.HTTP_NETWORK_EXCEPTION, "Socket Timeout Exception");
		} catch (ClientProtocolException cpe) {
			LOGGER.error(cpe.getMessage(), cpe);
			throw cpe;
		} catch (IOException ioe) {
			LOGGER.error(ioe.getMessage(), ioe);
			throw new BatchException(BatchErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION, BatchErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION, ioe.getMessage());
		}

		LOGGER.info("Exiting handleRequest()");
		return response;
	}

	/**
	 * 
	 * @param response
	 * @param url
	 * @param type
	 * @return
	 * @throws BatchException
	 */
	protected List<NameValuePair> handleResponse(HttpResponse response, String url, String type) throws BatchException {
		LOGGER.info("Entering handleResponse()");
		final List<NameValuePair> retValue = new ArrayList<NameValuePair>(2);

		// First check for 502 status
		if (response != null) {
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_BAD_GATEWAY) {
				// We have a BAD GATEWAY
				throw new BatchException(BatchErrorCodes.HTTP_BAD_GATEWAY_EXCEPTION, BatchErrorMessage.HTTP_BAD_GATEWAY_EXCEPTION, "Error with URL: " + url);
			}
	
			// Get the Location redirect
			final Header[] headers = response.getAllHeaders();
			this.cookieMap.clear();
			for (Header header : headers) {
				if ("Set-Cookie".equals(header.getName())) {
					final String cookieValue = header.getValue();
					final CookieData cookieData = new CookieData();
					final StringTokenizer st = new StringTokenizer(cookieValue, ";");
					while (st.hasMoreElements()) {
						final String element = (String)st.nextElement();
						LOGGER.debug(element);
						
						if (element != null && (element.contains("domain") || element.contains("Domain"))) {
							int index = element.indexOf("domain=");
							if (index != -1) {
								cookieData.setDomain(element.substring(index + "domain=".length()));
							} else {
								index = element.indexOf("Domain=");
								if (index != -1) {
									cookieData.setDomain(element.substring(index + "Domain=".length()));
								}
							}
						} else if (element != null && element.contains("expires")) {
							int index = element.indexOf("expires=");
							if (index != -1) {
								cookieData.setExpires(element.substring(index + "expires=".length()));
							}
						} else if (element != null && element.contains("path")) {
							int index = element.indexOf("path=");
							if (index != -1) {
								cookieData.setExpires(element.substring(index + "path=".length()));
							}
						} else {
							int index = element.indexOf("=");
							if (index != -1) {
								cookieData.setName(element.substring(0, index));
								cookieData.setValue(element.substring(index + 1));
							}
						}
					}
	
					this.cookieMap.add(cookieData);
				} else {
					retValue.add(new BasicNameValuePair(header.getName(), header.getValue()));
				}
			}
	
			// Check for 204 status
			if (this.showRequestResponse) {
				LOGGER.error("statusCode: " + statusCode);
			} else {
				LOGGER.debug("statusCode: " + statusCode);
			}
	
			if (statusCode != HttpStatus.SC_NO_CONTENT && statusCode != HttpStatus.SC_MOVED_TEMPORARILY && statusCode != HttpStatus.SC_NOT_MODIFIED && statusCode != HttpStatus.SC_TEMPORARY_REDIRECT) {
				String data = readInputStream(response);
				if (this.showRequestResponse) {
					LOGGER.error("RESPONSE: " + data);
					System.out.println("Response: "+data);
				}
				previousXhtml = data;
		
				if (data == null || data.length() == 0) {
					throw new BatchException(BatchErrorCodes.XHTML_NO_DATA_RETURNED_EXCEPTION, BatchErrorMessage.XHTML_NO_DATA_RETURNED_EXCEPTION, "There was no data returned for the url of " + url);
				} else {
					if (data != null && data.contains("Invalid page sequence")) {
						throw new BatchException(BatchErrorCodes.INVALID_PAGE_SEQUENCE, BatchErrorMessage.INVALID_PAGE_SEQUENCE, "There was a Invalid Pag Sequence for the url of " + url, data);
					}
				}
	
				retValue.add(new BasicNameValuePair(type, data));
			} else if (statusCode == HttpStatus.SC_MOVED_TEMPORARILY || statusCode == HttpStatus.SC_TEMPORARY_REDIRECT) {
				EntityUtils.consumeQuietly(response.getEntity());
			}
		}

		LOGGER.info("Exiting handleResponse()");
		return retValue;
	}

	/**
	 * 
	 * @param httpRequestBase
	 * @param localContext
	 * @return
	 * @throws BatchException
	 */
	private HttpResponse setupRetry(HttpRequestBase httpRequestBase, HttpContext localContext) throws BatchException {
		LOGGER.info("Entering setupRetry()");
		HttpResponse response = null;

		try {
			LOGGER.info("cookieStore: " + cookieStore);
			if (cookieStore != null) {
				response = client.execute(httpRequestBase, localContext);
			} else {
				response = client.execute(httpRequestBase);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new BatchException(BatchErrorCodes.HTTP_NETWORK_EXCEPTION, BatchErrorMessage.HTTP_NETWORK_EXCEPTION, e.getMessage());
		}

		LOGGER.info("Exiting setupRetry()");
		return response;
	}

	/**
	 * 
	 * @param cpe
	 * @param url
	 * @param httpRequestBase
	 * @param cookieValue
	 * @param headerValuePairs
	 * @return
	 * @throws BatchException
	 */
	private List<NameValuePair> handleCirularRedirect(ClientProtocolException cpe, 
			String url, 
			HttpRequestBase httpRequestBase, 
			String cookieValue, 
			List<NameValuePair> headerValuePairs) throws BatchException {
		LOGGER.info("Entering handleCirularRedirect()");
		List<NameValuePair> retValue = null;

    	String message = cpe.getCause().getMessage();
    	LOGGER.error("Message: " + message);

    	if (message != null) {
    		int index = message.indexOf("Circular redirect to '");
    		if (index != -1) {
    			message = message.substring(index + "Circular redirect to '".length());
    			index = message.indexOf("'");
    			if (index != -1) {
    				String newurl = message.substring(0, index);
    				LOGGER.error("newurl: " + newurl);

    				// Check to make sure it is a Get
    				if (httpRequestBase instanceof HttpGet) {
	    				// Simple request ...
	    				HttpGet get = new HttpGet(newurl);
	    				try {
		    				URI uri = new URI(newurl);
		    				host = newurl;
							domain = uri.getHost();
							LOGGER.info("Domain: " + domain);
	    				} catch (Exception e) {
	    					LOGGER.error("Exception with URI", e);
	    				}
	
	    				// Make the request
	    				HttpResponse response = null;
	    				try {
	    					response = handleRequest(get, newurl);
	    				} catch (Exception e) {
	    					LOGGER.error(e.getMessage(), e);
	    					throw new BatchException(BatchErrorCodes.HTTP_NETWORK_EXCEPTION, BatchErrorMessage.HTTP_NETWORK_EXCEPTION, e.getMessage());
	    				}

	    				// Handle the response
	    				retValue = handleResponse(response, url, "xhtml");
	    				previousUrl = url;
    				}
    			}
    		}
    	}

    	LOGGER.info("Exiting handleCirularRedirect()");
    	return retValue;
	}

	/**
	 * 
	 * @param response
	 * @return
	 * @throws BatchException
	 */
	private String readInputStream(HttpResponse response) throws BatchException {
		LOGGER.info("Entering readInputStream()");

		final StringBuffer sb = new StringBuffer(1000);
		try {
			final BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), Charset.forName("ISO-8859-1")));
			String line = "";
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}

			// Close reader
			if (rd != null) {
				rd.close();
			}
		} catch (IOException ioe) {
			LOGGER.error(ioe.getMessage(), ioe);
			throw new BatchException(BatchErrorCodes.HTTP_NETWORK_EXCEPTION, BatchErrorMessage.HTTP_NETWORK_EXCEPTION, ioe.getMessage());
		}

		LOGGER.info("Exiting readInputStream()");
		return sb.toString();
	}

	/**
	 * 
	 * @param url
	 * @throws BatchException
	 */
	private void checkForValidUrl(String url) throws BatchException {
		if (url == null) {
			LOGGER.error("URL: " + url);
			LOGGER.error("PREVIOUS URL: " + previousUrl);
			LOGGER.error("PREVIOUSXHTML: " + previousXhtml);
			throw new BatchException(BatchErrorCodes.FATAL_EXCEPTION, BatchErrorMessage.FATAL_EXCEPTION, "URL: " + url + " PREVIOUSURL: " + previousUrl, previousXhtml);
		}
	}
}