/**
 * 
 */
package com.ticketadvantage.services.dao.sites.generic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.sites.ProxyContainer;
import com.ticketadvantage.services.dao.sites.SiteProcessor;
import com.ticketadvantage.services.dao.sites.SiteTeamPackage;
import com.ticketadvantage.services.dao.sites.SiteTransaction;
import com.ticketadvantage.services.errorhandling.AppException;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.AccountEvent;
import com.ticketadvantage.services.model.BaseRecordEvent;
import com.ticketadvantage.services.model.EventPackage;
import com.ticketadvantage.services.model.EventsPackage;
import com.ticketadvantage.services.model.MlRecordEvent;
import com.ticketadvantage.services.model.SpreadRecordEvent;
import com.ticketadvantage.services.model.TeamPackage;
import com.ticketadvantage.services.model.TotalRecordEvent;

/**
 * @author jmiller
 *
 */
public class GenericProcessSite extends SiteProcessor {
	private static final Logger LOGGER = Logger.getLogger(GenericProcessSite.class);
	private HttpClient client = null;

	/**
	 * 
	 */
	public GenericProcessSite() {
		super("GenericSite");
	}

	protected void processTotal(TotalRecordEvent transaction, EventPackage checkPackage) throws BatchException {
		
	}
	protected void processMoneyLine(MlRecordEvent transaction, EventPackage checkPackage) throws BatchException {
		
	}
	public void processSpreadTransaction(SpreadRecordEvent event, AccountEvent ae) throws BatchException {
		
	}
	public void processTotalTransaction(TotalRecordEvent event, AccountEvent ae) throws BatchException {
		
	}
	public void processMlTransaction(MlRecordEvent event, AccountEvent ae) throws BatchException {
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * @param url
	 * @param username
	 * @param password
	 * @return
	 */
	public String checkSite(String url, String username, String password) {
		String siteType = "undefined";

		try {
			// Simple request ...
			HttpGet get = new HttpGet(url);
			try {
				URI uri = new URI(url);
			    String domain = uri.getHost();
			    LOGGER.info("Domain: " + domain);

				// Set the post header values
			    get.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			    get.addHeader("Accept-Encoding", "gzip, deflate");
			    get.addHeader("Accept-Language", "en-US,en;q=0.5");
			    get.addHeader("Connection", "keep-alive");
			    get.addHeader("Content-Type", "application/x-www-form-urlencoded");
			    get.addHeader("Host", domain);
			    get.addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:47.0) Gecko/20100101 Firefox/47.0");

			    HttpResponse response = null;
			    try {
			    		this.setupHttpClient("Dallas");
			    		response = client.execute(get);
			    } catch (ClientProtocolException cpe) {
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
	
				    				// Simple request ...
				    				get = new HttpGet(newurl);
				    				uri = new URI(newurl);
									domain = uri.getHost();
									LOGGER.info("Domain: " + domain);
	
									// Set the post header values
									get.addHeader("Accept",
											"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
									get.addHeader("Accept-Encoding", "gzip, deflate");
									get.addHeader("Accept-Language", "en-US,en;q=0.5");
									get.addHeader("Connection", "keep-alive");
									get.addHeader("Content-Type", "application/x-www-form-urlencoded");
									get.addHeader("Host", domain);
									get.addHeader("User-Agent",
											"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:47.0) Gecko/20100101 Firefox/47.0");
	
									response = client.execute(get);
				    			}
				    		}
				    	}
			    }

				final StringBuffer sb = new StringBuffer(1000);
				final BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				String line = "";
				while ((line = rd.readLine()) != null) {
					sb.append(line);
				}
				final String siteInfo = sb.toString();
				LOGGER.debug("Site: " + siteInfo);

				if (siteInfo.contains("spot47")) {
					LOGGER.debug("Found siteInfo for Spot47");
					siteType = "TDSports";
				} else if (siteInfo.contains("GSBETTING")) {
					LOGGER.debug("Found siteInfo for Gsbetting");
					siteType = "Gsbetting";
				} else if (siteInfo.contains("BetIt.info")) {
					LOGGER.debug("Found siteInfo for BetitTDSports");
					siteType = "BetitTDSports";
				} else if (siteInfo.contains("BetOnUSASports.com")) {
					LOGGER.debug("Found siteInfo for BetOnUsaSportsTDSports");
					siteType = "BetOnUsaSportsTDSports";
				} else if (siteInfo.contains("BetPanther")) {
					LOGGER.debug("Found site Info for BetPantherTDSports");
					siteType = "BetPantherTDSports";
				} else if (siteInfo.contains("action23")) {
					LOGGER.debug("Found site Info for Action23TDSports");
					siteType = "Action23TDSports";
				} else if (siteInfo.contains("504bet")) {
					LOGGER.debug("Found site Info for Bet504TDSports");
					siteType = "Bet504TDSports";
				} else if (siteInfo.contains("names777")) {
					LOGGER.debug("Found site Info for Names777TDSports");
					siteType = "Names777TDSports";
				} else if (siteInfo.contains("724sports")) {
					LOGGER.debug("Found siteInfo for SevenTwentyFourTDSports");
					siteType = "SevenTwentyFourTDSports";
				} else if (siteInfo.contains(".:Mobile Betting:.")) {
					LOGGER.debug("Found site for WagerShackMobile");
					siteType = "WagerShackMobile";
				} else if (siteInfo.contains("BETGOTHAM")) {
					LOGGER.debug("Found site for BetGotham");
					siteType = "BetGotham";
				} else if (siteInfo.contains("ABC Wagering")) {
					LOGGER.debug("Found siteInfo for AbcWageringTDSports");
					siteType = "AbcWageringTDSports";
				} else if (siteInfo.contains("ebets")) {
					LOGGER.debug("Found siteInfo for Ebets247TDSports");
					siteType = "Ebets247TDSports";
				} else if (siteInfo.contains("lvaction")) {
					LOGGER.debug("Found siteInfo for LvAction");
					siteType = "LvActionTDSports";
				} else if (siteInfo.contains("Royal Sports")) {
					LOGGER.debug("Found siteInfo for PlayRSBTDSports");
					siteType = "PlayRSBTDSports";
				} else if (siteInfo.contains("betproplus.com")) {
					LOGGER.debug("Found siteInfo for BetProPlusTDSports");
					siteType = "BetProPlusTDSports";
				} else if (siteInfo.contains("Sand Island Sports")) {
					LOGGER.debug("Found siteInfo for SandIslandSportsTDSports");
					siteType = "SandIslandSportsTDSports";
				} else if (siteInfo.contains("BetLoneStar")) {
					LOGGER.debug("Found siteInfo for BetlonestarTDSports");
					siteType = "BetlonestarTDSports";
				} else if (siteInfo.contains("member_login.aspx")) {
					LOGGER.debug("Found siteInfo for TDSportsOne");
					siteType = "TDSportsOne";
				} else if (siteInfo.contains("Bet All Star")) {
					LOGGER.debug("Found siteInfo for BetallstarTDSports");
					siteType = "BetallstarTDSports";
				} else if (siteInfo.contains("Green444")) {
					LOGGER.debug("Found siteInfo for Green444TDSports");
					siteType = "Green444TDSports";
				} else if (siteInfo.contains("4sfold")) {
					LOGGER.debug("Found siteInfo for FoursfoldTDSportsProcessSite");
					siteType = "FoursfoldTDSportsProcessSite";
				} else if (siteInfo.contains("abcweb")) {
					LOGGER.debug("Found siteInfo for AbcwebTDSports");
					siteType = "AbcwebTDSports";
				} else if (siteInfo.contains("iolcostarica")) {
					LOGGER.debug("Found siteInfo for IolSports");
					siteType = "IolSports";
				} else if (siteInfo.contains("BetGrande")) {
					LOGGER.debug("Found siteInfo for BetGrande");
					siteType = "BetgrandeTDSports";
				} else if (siteInfo.contains("Qubic")) {
					LOGGER.debug("Found siteInfo for BetTuckeye");
					siteType = "BetBuckeye";
				} else if (siteInfo.contains("linepros.com")) {
					LOGGER.debug("Found siteInfo for HeritageSports");
					siteType = "HeritageSports";
				} else if (siteInfo.contains("/flash/banner.html") ||
						siteInfo.contains("/cog/LoginVerify.Asp") ||
						siteInfo.contains("/Cog/LoginVerify.Asp") || 
						siteInfo.contains("/cog/loginverify.asp") ||
						siteInfo.contains("ctl00_MainContent") ||
						siteInfo.contains("static.cog.cr")) {
					LOGGER.debug("Found siteInfo for Metallica");
					siteType = "Metallica";
				} else if (siteInfo.contains("src=\"/Logins/") || siteInfo.contains("BWC.AG")) {
					LOGGER.debug("Found siteInfo for LineTracker");
					siteType = "LineTracker";
				} else if (siteInfo.contains("handheld mobile en-US")) {
					LOGGER.debug("Found site for Sports411Mobile");
					siteType = "Sports411Mobile";
				} else if (siteInfo.contains("sports411")) {
					LOGGER.debug("Found siteInfo for Sports411");
					siteType = "Sports411";
				} else if (siteInfo.contains("ELITE:SPORTS")) {
					LOGGER.debug("Found siteInfo for EliteSports");
					siteType = "EliteSports";
				} else if (siteInfo.contains("ibet.ag")) {
					LOGGER.debug("Found siteInfo for IbetTDSports");
					siteType = "IbetTDSports";
				} else if (siteInfo.contains("playthedog")) {
					LOGGER.debug("Found siteInfo for PlayTheDogTDSports");
					siteType = "PlayTheDogTDSports";
				} else if (siteInfo.contains("yopig")) {
					LOGGER.debug("Found siteInfo for YoPigTDSports");
					siteType = "YoPigTDSports";
				} else if (siteInfo.contains("PlayItBig.net")) {
					siteType = "TDSportsNew";
				} else if (siteInfo.contains("name=\"account\"") || siteInfo.contains("name=\"Account\"")) {
					LOGGER.debug("Found siteInfo for TDSports");
					siteType = "TDSports";
				} else if (siteInfo.contains("/Design")) {
					LOGGER.debug("Found siteInfo for AGSoftware");
					siteType = "AGSoftware";
				} else {
					LOGGER.error(siteInfo);
				}
			} catch (IOException ioe) {
				LOGGER.error(ioe.getMessage(), ioe);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			//TODO
		}

		return siteType;
	}

	/**
	 * 
	 * @param proxyName
	 * @throws BatchException
	 */
	public void setupHttpClient(String proxyName) throws BatchException {
		LOGGER.info("Entering setupHttpClient()");
		LOGGER.info("ProxyName: " + proxyName);

		int CONNECTION_TIMEOUT_MS = 45 * 1000; // Timeout in millis.
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
	 * @param transaction
	 */
	public void processSpreadTransaction(SpreadRecordEvent transaction) throws BatchException {
		
	}

	/**
	 * 
	 * @return
	 * @throws AppException
	 */
	public EventsPackage getAllEvents() throws AppException {
		return null;
	}

	/**
	 * 
	 * @param transaction
	 * @param checkPackage
	 * @throws BatchException
	 */
	protected void processSpread(SpreadRecordEvent transaction, EventPackage checkPackage) throws BatchException {
		
	}
	
	/**
	 * 
	 * @param transaction
	 * @param checkPackage
	 * @throws AppException
	 */
	protected void processTotal(EventPackage transaction, EventPackage checkPackage) throws AppException {
		
	}

	/**
	 * 
	 * @param transaction
	 * @param checkPackage
	 * @throws AppException
	 */
	protected void processMoneyLine(EventPackage transaction, EventPackage checkPackage) throws AppException {
		
	}

	/**
	 * 
	 * @param spread
	 * @param juice
	 * @param aspTeam
	 * @return
	 * @throws BatchException
	 */
	protected SiteTransaction determineNegativeSpread(SpreadRecordEvent spreadRecordEvent, float spread, float juice, TeamPackage teamPackage) throws BatchException {
		return null;
	}
	
	/**
	 * 
	 * @param spread
	 * @param juice
	 * @param aspTeam
	 * @return
	 * @throws BatchException
	 */
	protected SiteTransaction determinePositiveSpread(SpreadRecordEvent spreadRecordEvent, float spread, float juice, TeamPackage aspTeam) throws BatchException {
		return null;
	}

	/**
	 * 
	 * @param spread
	 * @param juice
	 * @param aspTeam
	 * @return
	 * @throws BatchException
	 */
	protected SiteTransaction determineEqualSpread(SpreadRecordEvent spreadRecordEvent, float spread, float juice, TeamPackage aspTeam) throws BatchException {
		return null;
	}

	/**
	 * 
	 * @param type
	 * @return
	 * @throws BatchException
	 */
	protected EventsPackage retrievePackage(String type) throws BatchException {
		return null;
	}
	
	/**
	 * 
	 * @param spreadType
	 * @param eventPackage
	 * @return
	 * @throws BatchException
	 */
	protected SiteTeamPackage setupTeam(int spreadType, EventPackage eventPackage) throws BatchException {
		return null;
	}

	@Override
	public SiteTransaction createSpreadTransaction(SiteTeamPackage siteTeamPackage,
			SpreadRecordEvent spreadRecordEvent, int arrayNum, float juice, AccountEvent accountEvent) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SiteTransaction createTotalTransaction(SiteTeamPackage siteTeamPackage, TotalRecordEvent totalRecordEvent,
			int arrayNum, float juice, AccountEvent accountEvent) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SiteTransaction createMoneyLineTransaction(SiteTeamPackage siteTeamPackage, MlRecordEvent mlRecordEvent,
			int arrayNum, float juice, AccountEvent accountEvent) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected SiteTransaction createSiteTransaction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String loginToSite(String username, String password) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String selectSport(String type) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void parseEventSelection(String xhtml, SiteTransaction siteTransaction, EventPackage eventPackage,
			BaseRecordEvent event, AccountEvent ae) throws BatchException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String selectEvent(SiteTransaction siteTransaction, EventPackage eventPackage, BaseRecordEvent event,
			AccountEvent ae) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String completeTransaction(SiteTransaction siteTransaction, EventPackage eventPackage,
			BaseRecordEvent event, AccountEvent ae) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String parseTicketTransaction(String xhtml) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}
}