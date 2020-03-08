/**
 * 
 */
package com.ticketadvantage.services.dao.twitter.poissonsports;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.sites.SiteProcessor;
import com.ticketadvantage.services.dao.sites.SiteTeamPackage;
import com.ticketadvantage.services.dao.sites.SiteTransaction;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.AccountEvent;
import com.ticketadvantage.services.model.BaseRecordEvent;
import com.ticketadvantage.services.model.EventPackage;
import com.ticketadvantage.services.model.MlRecordEvent;
import com.ticketadvantage.services.model.SpreadRecordEvent;
import com.ticketadvantage.services.model.TotalRecordEvent;

/**
 * @author jmiller
 *
 */
public class PoissonSportsSite extends SiteProcessor {
	private final static Logger LOGGER = Logger.getLogger(PoissonSportsSite.class);
	private final PoissonSportsParser poissonSportsParser = new PoissonSportsParser();

	/**
	 * 
	 */
	public PoissonSportsSite(String host, String username, String password) {
		super("PoissonSportsSite", host, username, password, false, false);
		LOGGER.info("Entering PoissonSportsSite()");

		try {
			setTimezone("ET");
			getHttpClientWrapper().setupHttpClient("None");
			setProcessTransaction(false);
		} catch (Throwable t) {
			LOGGER.warn(t.getMessage(), t);
		}

		LOGGER.info("Exiting PoissonSportsSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final PoissonSportsSite sportslineSite = new PoissonSportsSite("https://t.co", null, null);
			String html = sportslineSite.getPlays("https://t.co/lHYdIR5Ezf");
			LOGGER.error("html: " + html);
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}
	}

	/**
	 * 
	 * @param url
	 * @return
	 */
	public String getPlays(String url) {
		String xhtml = null;

		try {
			xhtml = super.getSite(url);
			LOGGER.debug("xhtml: " + xhtml);
			
			if (xhtml != null) {
				int index = xhtml.indexOf("URL=");
				if (index != -1) {
					xhtml = xhtml.substring(index + 4);
					index = xhtml.indexOf("\">");
					if (index != -1) {
						String newurl = xhtml.substring(0, index);
						
						List<NameValuePair> headerValuePairs = new ArrayList<NameValuePair>();
						headerValuePairs.add(new BasicNameValuePair("Upgrade-Insecure-Requests", "1"));
						headerValuePairs.add(new BasicNameValuePair("Referer", super.httpClientWrapper.getPreviousUrl()));
						headerValuePairs.add(new BasicNameValuePair("Host", "bit.ly"));
						List<NameValuePair> retValue = super.httpClientWrapper.getSitePageNoRedirect(newurl, null, headerValuePairs);

						String location = "";
						// Get all new cookies and the XHTML for website
						if (retValue != null && !retValue.isEmpty()) {
							final Iterator<NameValuePair> itr = retValue.iterator();
							while (itr != null && itr.hasNext()) {
								final NameValuePair nvp = (NameValuePair) itr.next();
								LOGGER.info("Header Name: " + nvp.getName());
								if ("Location".equals(nvp.getName())) {
									location = nvp.getValue();
								} else if ("xhtml".equals(nvp.getName())) {
									xhtml = nvp.getValue();
								}
							}
						}

						if (location != null && location.length() > 0) {
							super.httpClientWrapper.setHost("www.bettingtalk.com");
							headerValuePairs = new ArrayList<NameValuePair>();
							headerValuePairs.add(new BasicNameValuePair("Upgrade-Insecure-Requests", "1"));
							headerValuePairs.add(new BasicNameValuePair("Referer", super.httpClientWrapper.getPreviousUrl()));
							headerValuePairs.add(new BasicNameValuePair("Host", "www.bettingtalk.com"));

							String thevalue = null;
							int pindex = location.indexOf("p=");
							if (pindex != -1) {
								thevalue = location.substring(pindex + 2);
								pindex = thevalue.indexOf("&");

								if (pindex != -1) {
									thevalue = thevalue.substring(0, pindex);
								}
							}
							retValue = super.httpClientWrapper.getSitePageNoRedirect(location, null, headerValuePairs);
							xhtml = httpClientWrapper.getCookiesAndXhtml(retValue);
							
							xhtml = poissonSportsParser.parseGames(thevalue, xhtml);
						}
						LOGGER.debug("xhtml: " + xhtml);
					}
				}
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		return xhtml;
	}

	@Override
	public SiteTransaction createSpreadTransaction(SiteTeamPackage siteTeamPackage,
			SpreadRecordEvent spreadRecordEvent, int arrayNum, float juice, AccountEvent accountEvent) throws BatchException {
		return null;
	}

	@Override
	public SiteTransaction createTotalTransaction(SiteTeamPackage siteTeamPackage, TotalRecordEvent totalRecordEvent,
			int arrayNum, float juice, AccountEvent accountEvent) throws BatchException {
		return null;
	}

	@Override
	public SiteTransaction createMoneyLineTransaction(SiteTeamPackage siteTeamPackage, MlRecordEvent mlRecordEvent,
			int arrayNum, float juice, AccountEvent accountEvent) throws BatchException {
		return null;
	}

	@Override
	protected SiteTransaction createSiteTransaction() {
		return null;
	}

	@Override
	protected String selectSport(String type) throws BatchException {
		return null;
	}

	@Override
	protected void parseEventSelection(String xhtml, SiteTransaction siteTransaction, EventPackage eventPackage,
			BaseRecordEvent event, AccountEvent ae) throws BatchException {
	}

	@Override
	protected String selectEvent(SiteTransaction siteTransaction, EventPackage eventPackage, BaseRecordEvent event,
			AccountEvent ae) throws BatchException {
		return null;
	}

	@Override
	protected String completeTransaction(SiteTransaction siteTransaction, EventPackage eventPackage,
			BaseRecordEvent event, AccountEvent ae) throws BatchException {
		return null;
	}

	@Override
	protected String parseTicketTransaction(String xhtml) throws BatchException {
		return null;
	}

	@Override
	public String loginToSite(String username, String password) throws BatchException {
		return null;
	}
}