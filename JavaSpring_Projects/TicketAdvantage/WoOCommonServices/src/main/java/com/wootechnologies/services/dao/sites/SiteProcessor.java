/**
 * 
 */
package com.wootechnologies.services.dao.sites;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.wootechnologies.dao.sites.ProxyContainer;
import com.wootechnologies.errorhandling.BatchErrorCodes;
import com.wootechnologies.errorhandling.BatchErrorMessage;
import com.wootechnologies.errorhandling.BatchException;
import com.wootechnologies.httpclient.HttpClientWrapper;
import com.wootechnologies.model.AccountEvent;
import com.wootechnologies.model.BacksideOutput;
import com.wootechnologies.model.BaseRecordEvent;
import com.wootechnologies.model.EventPackage;
import com.wootechnologies.model.EventsPackage;
import com.wootechnologies.model.MlRecordEvent;
import com.wootechnologies.model.PendingEvent;
import com.wootechnologies.model.PreviewInput;
import com.wootechnologies.model.PreviewOutput;
import com.wootechnologies.model.SpreadRecordEvent;
import com.wootechnologies.model.TeamTotalRecordEvent;
import com.wootechnologies.model.TotalRecordEvent;
import com.wootechnologies.services.captcha.CaptchaWorker;
import com.wootechnologies.services.dao.sites.sportsinsights.SportsInsightsSite;

/**
 * @author jmiller 
 *
 */
public abstract class SiteProcessor {
	private static final Logger LOGGER = Logger.getLogger(SiteProcessor.class);
	protected HttpClientWrapper httpClientWrapper;
	protected SiteParser siteParser;
	public Map<String, String> MAP_DATA;
	public Map<String, String> DYNAMIC_DATA = new HashMap<String, String>();
	protected String timezone = "ET";
	protected Integer captchaRetry = new Integer(0);
	protected boolean processTransaction = true;
	protected String sportType;
	protected Integer eventId;
	protected String sportWagerType;
	protected String gamesXhtml;
	protected String proxyName;
	protected Long userid;
	protected String textNumber;
	protected Boolean humanspeed = new Boolean(false);
	protected static final Map<String, SiteProcessor> ACCOUNTSITES = new HashMap<String, SiteProcessor>();
	protected static String[][] LINE_TYPES = new String [][] { 
//		{"nfllines"}
//      {"nflfirst"},
//      {"nflsecond"},
//		{"ncaaflines"},
//      {"ncaaffirst"},
//      {"ncaafsecond"},
//		{"nbalines"}
//      {"nbafirst"},
//      {"nbasecond"},
	    {"ncaablines"}
//		{"ncaabfirst"},
//      {"ncaabsecond"},
//		{"mlblines"}
	};

	protected String[] NFL_LINES_NAME = null;
	protected String[] NFL_LINES_SPORT = null;
	protected String[] NFL_FIRST_NAME = null;
	protected String[] NFL_FIRST_SPORT = null;
	protected String[] NFL_SECOND_NAME = null;
	protected String[] NFL_SECOND_SPORT = null;
	protected String[] NCAAF_LINES_NAME = null;
	protected String[] NCAAF_LINES_SPORT = null;
	protected String[] NCAAF_FIRST_NAME = null;
	protected String[] NCAAF_FIRST_SPORT = null;
	protected String[] NCAAF_SECOND_NAME = null;
	protected String[] NCAAF_SECOND_SPORT = null;
	protected String[] NBA_LINES_NAME = null;
	protected String[] NBA_LINES_SPORT = null;
	protected String[] NBA_FIRST_NAME = null;
	protected String[] NBA_FIRST_SPORT = null;
	protected String[] NBA_SECOND_NAME = null;
	protected String[] NBA_SECOND_SPORT = null;
	protected String[] NCAAB_LINES_NAME = null;
	protected String[] NCAAB_LINES_SPORT = null;
	protected String[] NCAAB_FIRST_NAME = null;
	protected String[] NCAAB_FIRST_SPORT = null;
	protected String[] NCAAB_SECOND_NAME = null;
	protected String[] NCAAB_SECOND_SPORT = null;
	protected String[] NHL_LINES_NAME = null;
	protected String[] NHL_LINES_SPORT = null;
	protected String[] NHL_FIRST_NAME = null;
	protected String[] NHL_FIRST_SPORT = null;
	protected String[] NHL_SECOND_NAME = null;
	protected String[] NHL_SECOND_SPORT = null;
	protected String[] NHL_THIRD_NAME = null;
	protected String[] NHL_THIRD_SPORT = null;
	protected String[] WNBA_LINES_NAME = null;
	protected String[] WNBA_LINES_SPORT = null;
	protected String[] WNBA_FIRST_NAME = null;
	protected String[] WNBA_FIRST_SPORT = null;
	protected String[] WNBA_SECOND_NAME = null;
	protected String[] WNBA_SECOND_SPORT = null;
	protected String[] MLB_LINES_NAME = null;
	protected String[] MLB_LINES_SPORT = null;
	protected String[] MLB_FIRST_NAME = null;
	protected String[] MLB_FIRST_SPORT = null;
	protected String[] MLB_SECOND_NAME = null;
	protected String[] MLB_SECOND_SPORT = null;
	protected String[] MLB_THIRD_NAME = null;
	protected String[] MLB_THIRD_SPORT = null;
	protected String[] INTERNATIONAL_BASEBALL_LINES_NAME = null;
	protected String[] INTERNATIONAL_BASEBALL_LINES_SPORT = null;
	protected String[] INTERNATIONAL_BASEBALL_FIRST_NAME = null;
	protected String[] INTERNATIONAL_BASEBALL_FIRST_SPORT = null;
	protected String[] INTERNATIONAL_BASEBALL_SECOND_NAME = null;
	protected String[] INTERNATIONAL_BASEBALL_SECOND_SPORT = null;

	/**
	 * 
	 * @param accountSoftware
	 */
	public SiteProcessor(String accountSoftware) {
		super();
		ACCOUNTSITES.put(accountSoftware, this);
	}

	/**
	 * 
	 * @param accountSoftware
	 * @param host
	 * @param username
	 * @param password
	 */
	public SiteProcessor(String accountSoftware, String host, String username, String password) {
		super();
		LOGGER.info("Entering SiteProcessor()");
		LOGGER.debug("accountSoftware: " + accountSoftware);
		LOGGER.debug("host: " + host);
		LOGGER.debug("username: " + username);
		LOGGER.debug("password: " + password);

		httpClientWrapper = new HttpClientWrapper(host, username, password);
		ACCOUNTSITES.put(accountSoftware, this);

		LOGGER.info("Exiting SiteProcessor()");
	}

	/**
	 * 
	 * @param accountSoftware
	 * @param host
	 * @param username
	 * @param password
	 */
	public SiteProcessor(String accountSoftware, String host, String username, String password, boolean isMobile, boolean showRequestResponse) {
		super();
		LOGGER.info("Entering SiteProcessor()");
		LOGGER.debug("accountSoftware: " + accountSoftware);
		LOGGER.debug("host: " + host);
		LOGGER.debug("username: " + username);
		LOGGER.debug("password: " + password);
		LOGGER.debug("isMobile: " + isMobile);
		LOGGER.debug("showRequestResponse: " + showRequestResponse);

		httpClientWrapper = new HttpClientWrapper(host, username, password);
		httpClientWrapper.setMobile(isMobile);
		httpClientWrapper.setShowRequestResponse(showRequestResponse);
		ACCOUNTSITES.put(accountSoftware, this);

		LOGGER.info("Exiting SiteProcessor()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Value: " + Math.abs(-177));
	}

	/**
	 * 
	 * @param processSite
	 * @param i
	 * @throws BatchException
	 */
	public static void testSite(SiteProcessor processSite, int i, String [][] BSITES) throws BatchException {
		LOGGER.info("Entering testSite()");
		final SiteProcessorTest spt = new SiteProcessorTest();

	    processSite.httpClientWrapper.setupHttpClient(BSITES[i][6]);
	    processSite.processTransaction = false;
	    processSite.timezone = BSITES[i][7];

		// Loop through the types
		for (int l = 0; l < SiteProcessor.LINE_TYPES.length; l++) {
			int count = 0;
			try {
				String xhtml = processSite.loginToSite(processSite.httpClientWrapper.getUsername(), processSite.httpClientWrapper.getPassword());
				LOGGER.debug("xhtml: " + xhtml);

				// Step 5 - Get menu
				processSite.MAP_DATA = processSite.getMenuFromSite(SiteProcessor.LINE_TYPES[l][0], xhtml);
				LOGGER.debug("MAP_DATA: " + processSite.MAP_DATA);

				// Step 6 - Sleep for 1 second
//				processSite.sleepAsUser(1000);

				// Step 7 - Select sport type
				xhtml = processSite.selectSport(SiteProcessor.LINE_TYPES[l][0]);
				LOGGER.debug("XHTML: " + xhtml);

				// Step 8 - Parse the games from the site
				final Iterator<EventPackage> ep = processSite.parseGamesOnSite(SiteProcessor.LINE_TYPES[l][0], xhtml);
				processSite.httpClientWrapper.setWebappname("");

				while (ep != null && ep.hasNext()) {
					final EventPackage eventPackage = ep.next();
					if (count++ == 0) {
						LOGGER.warn("EventPackage: " + eventPackage);
						spt.processTests(processSite, eventPackage, SiteProcessor.LINE_TYPES[l][0],
								Integer.parseInt(BSITES[i][3]), Integer.parseInt(BSITES[i][4]),
								Integer.parseInt(BSITES[i][5]), BSITES[i][0], BSITES[i][6]);
					}
				}
			} catch (Exception e) {
				LOGGER.error("Exception: ", e);
			}
		}

		LOGGER.info("Exiting testSite()");
	}

	/**
	 * 
	 * @param processSite
	 * @param i
	 * @throws BatchException
	 */
	public static void testSite2(SiteProcessor processSite, SiteProcessor processSite2, int i, String[][] BSITES)
			throws BatchException {
		LOGGER.info("Entering testSite2()");
		final SiteProcessorTest spt = new SiteProcessorTest();

		processSite.httpClientWrapper.setupHttpClient(BSITES[i][6]);
		processSite.processTransaction = false;
		processSite.timezone = BSITES[i][7];

		processSite2.httpClientWrapper.setupHttpClient(BSITES[i][6]);
		processSite2.processTransaction = false;
		processSite2.timezone = BSITES[i][7];

		// Loop through the types
		for (int l = 0; l < SiteProcessor.LINE_TYPES.length; l++) {
			int count = 0;
			try {
				String xhtml = processSite.loginToSite(processSite.httpClientWrapper.getUsername(),
						processSite.httpClientWrapper.getPassword());
				LOGGER.debug("xhtml: " + xhtml);

				// Step 5 - Get menu
				processSite.MAP_DATA = processSite.getMenuFromSite(SiteProcessor.LINE_TYPES[l][0], xhtml);
				LOGGER.debug("MAP_DATA: " + processSite.MAP_DATA);

				// Step 6 - Sleep for 1 second
//				processSite.sleepAsUser(1000);

				// Step 7 - Select sport type
				xhtml = processSite.selectSport(SiteProcessor.LINE_TYPES[l][0]);
				LOGGER.debug("XHTML: " + xhtml);

				// Step 8 - Parse the games from the site
				final Iterator<EventPackage> ep = processSite.parseGamesOnSite(SiteProcessor.LINE_TYPES[l][0], xhtml);
				processSite.httpClientWrapper.setWebappname("");

				while (ep != null && ep.hasNext()) {
					final EventPackage eventPackage = ep.next();
					if (count++ == 0) {
						LOGGER.warn("EventPackage: " + eventPackage);
						spt.processTests(processSite2, eventPackage, SiteProcessor.LINE_TYPES[l][0],
								Integer.parseInt(BSITES[i][3]), Integer.parseInt(BSITES[i][4]),
								Integer.parseInt(BSITES[i][5]), BSITES[i][0], BSITES[i][6]);
					}
				}
			} catch (Exception e) {
				LOGGER.error("Exception: ", e);
			}
		}

		LOGGER.info("Exiting testSite2()");
	}

	/**
	 * 
	 * @param accountName
	 * @param accountId
	 * @param anythingObject
	 * @return
	 * @throws BatchException
	 */
	public Set<PendingEvent> getPendingBets(String accountName, String accountId, Object anythingObject)
			throws BatchException {
		LOGGER.info("Entering getPendingBets()");
		LOGGER.info("Exiting getPendingBets()");
		return null;
	}

	/**
	 * 
	 * @param previewInput
	 * @return
	 * @throws BatchException
	 */
	public PreviewOutput previewEvent(PreviewInput previewInput) throws BatchException {
		LOGGER.info("Entering previewEvent()");

		// Step 1 - Setup timezone
		this.timezone = previewInput.getTimezone();

		// Step 2 - Setup proxy client
		httpClientWrapper.setupHttpClient(previewInput.getProxyname());

		//
		// Call the login of the site
		//
		// Step 3 - Login
		String xhtml = loginToSite(httpClientWrapper.getUsername(), httpClientWrapper.getPassword());
		LOGGER.debug("XHTML: " + xhtml);

		// Act as a human if we should
		actasHuman();

		// Step 4a - Get event sport type
		this.siteParser.setSportType(previewInput.getSporttype());
		LOGGER.debug("SportType: " + sportType);

		// Step 4b - Add eventID
		this.eventId = previewInput.getRotationid();

		// Step 5 - Get menu
		MAP_DATA = getMenuFromSite(previewInput.getSporttype(), xhtml);
		LOGGER.debug("MAP_DATA: " + MAP_DATA);

		// Act as a human if we should
		actasHuman();

		//
		// Call the Menu selection of the site
		//
		// Step 6 - Select sport type
		xhtml = selectSport(previewInput.getSporttype());
		LOGGER.debug("XHTML: " + xhtml);

		// Step 7 - Parse the games from the site
		final Iterator<EventPackage> ep = parseGamesOnSite(previewInput.getSporttype(), xhtml);
		LOGGER.debug("ep: " + ep);

		// Act as a human if we should
		actasHuman();

		// Step 8 - Find event
		final SiteEventPackage eventPackage = (SiteEventPackage) findPreviewEvent(ep,
				previewInput.getRotationid().longValue(), 
				previewInput.getSporttype(), 
				previewInput.getTeam1(),
				previewInput.getTeam2(), 
				new Date());
		LOGGER.debug("EventPackage: " + eventPackage);

		// Step 9 - Setup the spread object
		final PreviewOutput previewData = new PreviewOutput();
		if (eventPackage != null) {
			final SiteTeamPackage siteTeam1 = eventPackage.getSiteteamone();
			final SiteTeamPackage siteTeam2 = eventPackage.getSiteteamtwo();
			LOGGER.debug("siteTeam1: " + siteTeam1);
			LOGGER.debug("siteTeam2: " + siteTeam2);

			if (siteTeam1 != null && siteTeam2 != null) {
				if ("spread".equals(previewInput.getLinetype())) {
					LinkedHashMap<String, String> spreadOptionIndicator = null;
					LinkedHashMap<String, String> spreadOptionNumber = null;
					LinkedHashMap<String, String> spreadOptionJuiceIndicator = null;
					LinkedHashMap<String, String> spreadOptionJuiceNumber = null;

					if (previewInput.getRotationid() % 2 == 0) {
						spreadOptionIndicator = siteTeam2.getGameSpreadOptionIndicator();
						spreadOptionNumber = siteTeam2.getGameSpreadOptionNumber();
						spreadOptionJuiceIndicator = siteTeam2.getGameSpreadOptionJuiceIndicator();
						spreadOptionJuiceNumber = siteTeam2.getGameSpreadOptionJuiceNumber();

						if (spreadOptionIndicator != null && !spreadOptionIndicator.isEmpty()
								&& spreadOptionNumber != null && !spreadOptionNumber.isEmpty()
								&& spreadOptionJuiceIndicator != null && !spreadOptionJuiceIndicator.isEmpty()
								&& spreadOptionJuiceNumber != null && !spreadOptionJuiceNumber.isEmpty()) {

							// int finalnumber = finalOrder(spreadOptionJuiceIndicator,
							// spreadOptionJuiceNumber);
							int finalnumber = 0;

							// Get the values
							previewData.setLineindicator(spreadOptionIndicator.get(String.valueOf(finalnumber)));
							previewData.setLine(replaceDot(spreadOptionNumber.get(String.valueOf(finalnumber)), ".0", ""));
							previewData.setJuiceindicator(spreadOptionJuiceIndicator.get(String.valueOf(finalnumber)));
							previewData.setJuice(replaceDot(spreadOptionJuiceNumber.get(String.valueOf(finalnumber)), ".0", ""));
						}
					} else {
						spreadOptionIndicator = siteTeam1.getGameSpreadOptionIndicator();
						spreadOptionNumber = siteTeam1.getGameSpreadOptionNumber();
						spreadOptionJuiceIndicator = siteTeam1.getGameSpreadOptionJuiceIndicator();
						spreadOptionJuiceNumber = siteTeam1.getGameSpreadOptionJuiceNumber();
						LOGGER.debug("spreadOptionIndicator: " + spreadOptionIndicator);
						LOGGER.debug("spreadOptionNumber: " + spreadOptionNumber);
						LOGGER.debug("spreadOptionJuiceIndicator: " + spreadOptionJuiceIndicator);
						LOGGER.debug("spreadOptionJuiceNumber: " + spreadOptionJuiceNumber);

						if (spreadOptionIndicator != null && !spreadOptionIndicator.isEmpty() && 
							spreadOptionNumber != null && !spreadOptionNumber.isEmpty() && 
							spreadOptionJuiceIndicator != null && !spreadOptionJuiceIndicator.isEmpty() && 
							spreadOptionJuiceNumber != null && !spreadOptionJuiceNumber.isEmpty()) {

							// int finalnumber = finalOrder(spreadOptionJuiceIndicator,
							// spreadOptionJuiceNumber);
							int finalnumber = 0;

							// Get the values
							previewData.setLineindicator(spreadOptionIndicator.get(String.valueOf(finalnumber)));
							previewData.setLine(replaceDot(spreadOptionNumber.get(String.valueOf(finalnumber)), ".0", ""));
							previewData.setJuiceindicator(spreadOptionJuiceIndicator.get(String.valueOf(finalnumber)));
							previewData.setJuice(replaceDot(spreadOptionJuiceNumber.get(String.valueOf(finalnumber)), ".0", ""));
						}
					}
				} else if ("total".equals(previewInput.getLinetype())) {
					LinkedHashMap<String, String> totalOptionNumber = null;
					LinkedHashMap<String, String> totalOptionJuiceIndicator = null;
					LinkedHashMap<String, String> totalOptionJuiceNumber = null;
					if ("o".equals(previewInput.getLineindicator())) {
						totalOptionNumber = siteTeam1.getGameTotalOptionNumber();
						totalOptionJuiceIndicator = siteTeam1.getGameTotalOptionJuiceIndicator();
						totalOptionJuiceNumber = siteTeam1.getGameTotalOptionJuiceNumber();
						LOGGER.debug("totalOptionNumber: " + totalOptionNumber);
						LOGGER.debug("totalOptionJuiceIndicator: " + totalOptionJuiceIndicator);
						LOGGER.debug("totalOptionJuiceNumber: " + totalOptionJuiceNumber);
						if (totalOptionNumber != null && !totalOptionNumber.isEmpty()
								&& totalOptionJuiceIndicator != null && !totalOptionJuiceIndicator.isEmpty()
								&& totalOptionJuiceNumber != null && !totalOptionJuiceNumber.isEmpty()) {

							// int finalnumber = finalOrder(totalOptionJuiceIndicator,
							// totalOptionJuiceNumber);
							int finalnumber = 0;
							LOGGER.debug("finalnumber: " + finalnumber);

							// Get the values
							LOGGER.debug("totalOptionNumber: " + totalOptionNumber.get(String.valueOf(finalnumber)));
							LOGGER.debug("totalOptionJuiceIndicator: "
									+ totalOptionJuiceIndicator.get(String.valueOf(finalnumber)));
							LOGGER.debug("totalOptionJuiceNumber: "
									+ totalOptionJuiceNumber.get(String.valueOf(finalnumber)));

							previewData.setLineindicator("o");
							previewData
									.setLine(replaceDot(totalOptionNumber.get(String.valueOf(finalnumber)), ".0", ""));
							previewData.setJuiceindicator(totalOptionJuiceIndicator.get(String.valueOf(finalnumber)));
							previewData.setJuice(
									replaceDot(totalOptionJuiceNumber.get(String.valueOf(finalnumber)), ".0", ""));
						}
					} else {
						totalOptionNumber = siteTeam2.getGameTotalOptionNumber();
						totalOptionJuiceIndicator = siteTeam2.getGameTotalOptionJuiceIndicator();
						totalOptionJuiceNumber = siteTeam2.getGameTotalOptionJuiceNumber();
						LOGGER.debug("totalOptionNumber: " + totalOptionNumber);
						LOGGER.debug("totalOptionJuiceIndicator: " + totalOptionJuiceIndicator);
						LOGGER.debug("totalOptionJuiceNumber: " + totalOptionJuiceNumber);
						if (totalOptionNumber != null && !totalOptionNumber.isEmpty()
								&& totalOptionJuiceIndicator != null && !totalOptionJuiceIndicator.isEmpty()
								&& totalOptionJuiceNumber != null && !totalOptionJuiceNumber.isEmpty()) {

							// int finalnumber = finalOrder(totalOptionJuiceIndicator,
							// totalOptionJuiceNumber);
							int finalnumber = 0;
							LOGGER.debug("finalnumber: " + finalnumber);

							// Get the values
							LOGGER.debug("totalOptionNumber: " + totalOptionNumber.get(String.valueOf(finalnumber)));
							LOGGER.debug("totalOptionJuiceIndicator: "
									+ totalOptionJuiceIndicator.get(String.valueOf(finalnumber)));
							LOGGER.debug("totalOptionJuiceNumber: "
									+ totalOptionJuiceNumber.get(String.valueOf(finalnumber)));

							previewData.setLineindicator("u");
							previewData
									.setLine(replaceDot(totalOptionNumber.get(String.valueOf(finalnumber)), ".0", ""));
							previewData.setJuiceindicator(totalOptionJuiceIndicator.get(String.valueOf(finalnumber)));
							previewData.setJuice(
									replaceDot(totalOptionJuiceNumber.get(String.valueOf(finalnumber)), ".0", ""));
						}
					}
				} else if ("ml".equals(previewInput.getLinetype())) {
					LinkedHashMap<String, String> mlOptionJuiceIndicator = null;
					LinkedHashMap<String, String> mlOptionJuiceNumber = null;
					if (previewInput.getRotationid() % 2 == 0) {
						mlOptionJuiceIndicator = siteTeam2.getGameMLOptionJuiceIndicator();
						mlOptionJuiceNumber = siteTeam2.getGameMLOptionJuiceNumber();

						if (mlOptionJuiceIndicator != null && !mlOptionJuiceIndicator.isEmpty()
								&& mlOptionJuiceNumber != null && !mlOptionJuiceNumber.isEmpty()) {

							// int finalnumber = finalOrder(mlOptionJuiceIndicator, mlOptionJuiceNumber);
							int finalnumber = 0;

							// Get the values
							previewData.setLineindicator(mlOptionJuiceIndicator.get(String.valueOf(finalnumber)));
							previewData.setLine(
									replaceDot(mlOptionJuiceNumber.get(String.valueOf(finalnumber)), ".0", ""));
							previewData.setJuiceindicator(mlOptionJuiceIndicator.get(String.valueOf(finalnumber)));
							previewData.setJuice(
									replaceDot(mlOptionJuiceNumber.get(String.valueOf(finalnumber)), ".0", ""));
						}
					} else {
						mlOptionJuiceIndicator = siteTeam1.getGameMLOptionJuiceIndicator();
						mlOptionJuiceNumber = siteTeam1.getGameMLOptionJuiceNumber();
						if (mlOptionJuiceIndicator != null && !mlOptionJuiceIndicator.isEmpty()
								&& mlOptionJuiceNumber != null && !mlOptionJuiceNumber.isEmpty()) {

							// int finalnumber = finalOrder(mlOptionJuiceIndicator, mlOptionJuiceNumber);
							int finalnumber = 0;

							// Get the values
							previewData.setLineindicator(
									mlOptionJuiceIndicator.get(String.valueOf(finalnumber)).replace("\\\\.0", ""));
							previewData.setLine(
									replaceDot(mlOptionJuiceNumber.get(String.valueOf(finalnumber)), ".0", ""));
							previewData.setJuiceindicator(
									mlOptionJuiceIndicator.get(String.valueOf(finalnumber)).replace("\\\\.0", ""));
							previewData.setJuice(
									replaceDot(mlOptionJuiceNumber.get(String.valueOf(finalnumber)), ".0", ""));
						}
					}
				} else if ("teamtotal".equals(previewInput.getLinetype())) {
					// Newly adeded code on 03-11-18
					LinkedHashMap<String, String> teamtotalOptionNumber = null;
					LinkedHashMap<String, String> teamtotalOptionJuiceIndicator = null;
					LinkedHashMap<String, String> teamtotalOptionJuiceNumber = null;
					if (previewInput.getRotationid() % 2 != 0) {
						if ("o".equals(previewInput.getLineindicator())) {
							teamtotalOptionNumber = siteTeam1.getGameTeamTotalOptionNumber();
							teamtotalOptionJuiceIndicator = siteTeam1.getGameTeamTotalOptionJuiceIndicator();
							teamtotalOptionJuiceNumber = siteTeam1.getGameTeamTotalOptionJuiceNumber();
							LOGGER.debug("teamtotalOptionNumber: " + teamtotalOptionNumber);
							LOGGER.debug("teamtotalOptionJuiceIndicator: " + teamtotalOptionJuiceIndicator);
							LOGGER.debug("teamtotalOptionJuiceNumber: " + teamtotalOptionJuiceNumber);
							if (teamtotalOptionNumber != null && !teamtotalOptionNumber.isEmpty()
									&& teamtotalOptionJuiceIndicator != null && !teamtotalOptionJuiceIndicator.isEmpty()
									&& teamtotalOptionJuiceNumber != null && !teamtotalOptionJuiceNumber.isEmpty()) {

								// int finalnumber = finalOrder(totalOptionJuiceIndicator,
								// totalOptionJuiceNumber);
								int finalnumber = 0;
								LOGGER.debug("finalnumber: " + finalnumber);

								// Get the values
								LOGGER.debug("teamtotalOptionNumber: "
										+ teamtotalOptionNumber.get(String.valueOf(finalnumber)));
								LOGGER.debug("teamtotalOptionJuiceIndicator: "
										+ teamtotalOptionJuiceIndicator.get(String.valueOf(finalnumber)));
								LOGGER.debug("teamtotalOptionJuiceNumber: "
										+ teamtotalOptionJuiceNumber.get(String.valueOf(finalnumber)));

								previewData.setLineindicator("o");
								previewData.setLine(
										replaceDot(teamtotalOptionNumber.get(String.valueOf(finalnumber)), ".0", ""));
								previewData.setJuiceindicator(
										teamtotalOptionJuiceIndicator.get(String.valueOf(finalnumber)));
								previewData.setJuice(replaceDot(
										teamtotalOptionJuiceNumber.get(String.valueOf(finalnumber)), ".0", ""));
							}
						} else {
							teamtotalOptionNumber = siteTeam1.getGameTeamTotalOptionNumberSecond();
							teamtotalOptionJuiceIndicator = siteTeam1.getGameTeamTotalOptionJuiceIndicatorSecond();
							teamtotalOptionJuiceNumber = siteTeam1.getGameTeamTotalOptionJuiceNumberSecond();
							LOGGER.debug("teamtotalOptionNumber: " + teamtotalOptionNumber);
							LOGGER.debug("teamtotalOptionJuiceIndicator: " + teamtotalOptionJuiceIndicator);
							LOGGER.debug("teamtotalOptionJuiceNumber: " + teamtotalOptionJuiceNumber);
							if (teamtotalOptionNumber != null && !teamtotalOptionNumber.isEmpty()
									&& teamtotalOptionJuiceIndicator != null && !teamtotalOptionJuiceIndicator.isEmpty()
									&& teamtotalOptionJuiceNumber != null && !teamtotalOptionJuiceNumber.isEmpty()) {

								// int finalnumber = finalOrder(totalOptionJuiceIndicator,
								// totalOptionJuiceNumber);
								int finalnumber = 0;
								LOGGER.debug("finalnumber: " + finalnumber);

								// Get the values
								LOGGER.debug("teamtotalOptionNumber: "
										+ teamtotalOptionNumber.get(String.valueOf(finalnumber)));
								LOGGER.debug("teamtotalOptionJuiceIndicator: "
										+ teamtotalOptionJuiceIndicator.get(String.valueOf(finalnumber)));
								LOGGER.debug("teamtotalOptionJuiceNumber: "
										+ teamtotalOptionJuiceNumber.get(String.valueOf(finalnumber)));

								previewData.setLineindicator("u");
								previewData.setLine(
										replaceDot(teamtotalOptionNumber.get(String.valueOf(finalnumber)), ".0", ""));
								previewData.setJuiceindicator(
										teamtotalOptionJuiceIndicator.get(String.valueOf(finalnumber)));
								previewData.setJuice(replaceDot(
										teamtotalOptionJuiceNumber.get(String.valueOf(finalnumber)), ".0", ""));
							}
						}
					}else {
						if ("o".equals(previewInput.getLineindicator())) {
							teamtotalOptionNumber = siteTeam2.getGameTeamTotalOptionNumber();
							teamtotalOptionJuiceIndicator = siteTeam2.getGameTeamTotalOptionJuiceIndicator();
							teamtotalOptionJuiceNumber = siteTeam2.getGameTeamTotalOptionJuiceNumber();
							LOGGER.debug("teamtotalOptionNumber: " + teamtotalOptionNumber);
							LOGGER.debug("teamtotalOptionJuiceIndicator: " + teamtotalOptionJuiceIndicator);
							LOGGER.debug("teamtotalOptionJuiceNumber: " + teamtotalOptionJuiceNumber);
							if (teamtotalOptionNumber != null && !teamtotalOptionNumber.isEmpty()
									&& teamtotalOptionJuiceIndicator != null && !teamtotalOptionJuiceIndicator.isEmpty()
									&& teamtotalOptionJuiceNumber != null && !teamtotalOptionJuiceNumber.isEmpty()) {

								// int finalnumber = finalOrder(totalOptionJuiceIndicator,
								// totalOptionJuiceNumber);
								int finalnumber = 0;
								LOGGER.debug("finalnumber: " + finalnumber);

								// Get the values
								LOGGER.debug("teamtotalOptionNumber: "
										+ teamtotalOptionNumber.get(String.valueOf(finalnumber)));
								LOGGER.debug("teamtotalOptionJuiceIndicator: "
										+ teamtotalOptionJuiceIndicator.get(String.valueOf(finalnumber)));
								LOGGER.debug("teamtotalOptionJuiceNumber: "
										+ teamtotalOptionJuiceNumber.get(String.valueOf(finalnumber)));

								previewData.setLineindicator("o");
								previewData.setLine(
										replaceDot(teamtotalOptionNumber.get(String.valueOf(finalnumber)), ".0", ""));
								previewData.setJuiceindicator(
										teamtotalOptionJuiceIndicator.get(String.valueOf(finalnumber)));
								previewData.setJuice(replaceDot(
										teamtotalOptionJuiceNumber.get(String.valueOf(finalnumber)), ".0", ""));
							}
						} else {
							teamtotalOptionNumber = siteTeam2.getGameTeamTotalOptionNumberSecond();
							teamtotalOptionJuiceIndicator = siteTeam2.getGameTeamTotalOptionJuiceIndicatorSecond();
							teamtotalOptionJuiceNumber = siteTeam2.getGameTeamTotalOptionJuiceNumberSecond();
							LOGGER.debug("teamtotalOptionNumber: " + teamtotalOptionNumber);
							LOGGER.debug("teamtotalOptionJuiceIndicator: " + teamtotalOptionJuiceIndicator);
							LOGGER.debug("teamtotalOptionJuiceNumber: " + teamtotalOptionJuiceNumber);
							if (teamtotalOptionNumber != null && !teamtotalOptionNumber.isEmpty()
									&& teamtotalOptionJuiceIndicator != null && !teamtotalOptionJuiceIndicator.isEmpty()
									&& teamtotalOptionJuiceNumber != null && !teamtotalOptionJuiceNumber.isEmpty()) {

								// int finalnumber = finalOrder(totalOptionJuiceIndicator,
								// totalOptionJuiceNumber);
								int finalnumber = 0;
								LOGGER.debug("finalnumber: " + finalnumber);

								// Get the values
								LOGGER.debug("teamtotalOptionNumber: "
										+ teamtotalOptionNumber.get(String.valueOf(finalnumber)));
								LOGGER.debug("teamtotalOptionJuiceIndicator: "
										+ teamtotalOptionJuiceIndicator.get(String.valueOf(finalnumber)));
								LOGGER.debug("teamtotalOptionJuiceNumber: "
										+ teamtotalOptionJuiceNumber.get(String.valueOf(finalnumber)));

								previewData.setLineindicator("u");
								previewData.setLine(
										replaceDot(teamtotalOptionNumber.get(String.valueOf(finalnumber)), ".0", ""));
								previewData.setJuiceindicator(
										teamtotalOptionJuiceIndicator.get(String.valueOf(finalnumber)));
								previewData.setJuice(replaceDot(
										teamtotalOptionJuiceNumber.get(String.valueOf(finalnumber)), ".0", ""));
							}
						}
					}
				}
			}
		}

		LOGGER.info("Exiting previewEvent()");
		return previewData;
	}

	/**
	 * 
	 * @param previewInput
	 * @return
	 * @throws BatchException
	 */
	public void setupInitial(PreviewInput previewInput) throws BatchException {
		LOGGER.info("Entering setupInitial()");

		// Step 1 - Setup timezone
		this.timezone = previewInput.getTimezone();

		// Step 2 - Setup proxy client
		httpClientWrapper.setupHttpClient(previewInput.getProxyname());

		//
		// Call the login of the site
		//
		// Step 3 - Login
		String xhtml = loginToSite(httpClientWrapper.getUsername(), httpClientWrapper.getPassword());
		LOGGER.debug("XHTML: " + xhtml);

		// Act as a human if we should
		actasHuman();

		// Step 4 - Get event sport type
		this.siteParser.setSportType(previewInput.getSporttype());
		LOGGER.debug("SportType: " + sportType);

		// Step 5 - Get menu
		MAP_DATA = getMenuFromSite(previewInput.getSporttype(), xhtml);
		LOGGER.debug("MAP_DATA: " + MAP_DATA);
		final Set<String> keys = MAP_DATA.keySet();
		for (String key : keys) {
			DYNAMIC_DATA.put(key, MAP_DATA.get(key));
		}

		// Act as a human if we should
		actasHuman();

		LOGGER.info("Exiting setupInitial()");
	}

	/**
	 * 
	 * @param previewInput
	 * @return
	 * @throws BatchException
	 */
	public BacksideOutput checkGame(PreviewInput previewInput) throws BatchException {
		LOGGER.info("Entering checkGame()");

		//
		// Call the Menu selection of the site
		//
		// Step 6 - Select sport type
		String xhtml = selectSport(previewInput.getSporttype());
		LOGGER.debug("XHTML: " + xhtml);

		// Step 7 - Parse the games from the site
		final Iterator<EventPackage> ep = parseGamesOnSite(previewInput.getSporttype(), xhtml);
		LOGGER.debug("ep: " + ep);

		// Act as a human if we should
		actasHuman();

		// Step 8 - Find event
		final SiteEventPackage eventPackage = (SiteEventPackage) findPreviewEvent(ep,
				previewInput.getRotationid().longValue(), previewInput.getSporttype(), previewInput.getTeam1(),
				previewInput.getTeam2(), new Date());
		LOGGER.debug("EventPackage: " + eventPackage);

		// Step 9 - Setup the spread object
		final BacksideOutput previewData = new BacksideOutput();
		if (eventPackage != null) {
			final SiteTeamPackage siteTeam1 = eventPackage.getSiteteamone();
			final SiteTeamPackage siteTeam2 = eventPackage.getSiteteamtwo();
			LOGGER.debug("siteTeam1: " + siteTeam1);
			LOGGER.debug("siteTeam2: " + siteTeam2);

			if (siteTeam1 != null && siteTeam2 != null) {
				if ("spread".equals(previewInput.getLinetype())) {
					LinkedHashMap<String, String> spreadOptionIndicator = null;
					LinkedHashMap<String, String> spreadOptionNumber = null;
					LinkedHashMap<String, String> spreadOptionJuiceIndicator = null;
					LinkedHashMap<String, String> spreadOptionJuiceNumber = null;
					if (previewInput.getRotationid() % 2 == 0) {
						spreadOptionIndicator = siteTeam2.getGameSpreadOptionIndicator();
						spreadOptionNumber = siteTeam2.getGameSpreadOptionNumber();
						spreadOptionJuiceIndicator = siteTeam2.getGameSpreadOptionJuiceIndicator();
						spreadOptionJuiceNumber = siteTeam2.getGameSpreadOptionJuiceNumber();
						if (spreadOptionIndicator != null && !spreadOptionIndicator.isEmpty()
								&& spreadOptionNumber != null && !spreadOptionNumber.isEmpty()
								&& spreadOptionJuiceIndicator != null && !spreadOptionJuiceIndicator.isEmpty()
								&& spreadOptionJuiceNumber != null && !spreadOptionJuiceNumber.isEmpty()) {
							// Set the EventPackage
							previewData.setEp(eventPackage);
							int finalnumber = finalOrder(spreadOptionJuiceIndicator, spreadOptionJuiceNumber);

							// Get the values
							previewData.setLineindicator(spreadOptionIndicator.get(String.valueOf(finalnumber)));
							previewData
									.setLine(replaceDot(spreadOptionNumber.get(String.valueOf(finalnumber)), ".0", ""));
							previewData.setJuiceindicator(spreadOptionJuiceIndicator.get(String.valueOf(finalnumber)));
							previewData.setJuice(
									replaceDot(spreadOptionJuiceNumber.get(String.valueOf(finalnumber)), ".0", ""));
						}
					} else {
						spreadOptionIndicator = siteTeam1.getGameSpreadOptionIndicator();
						spreadOptionNumber = siteTeam1.getGameSpreadOptionNumber();
						spreadOptionJuiceIndicator = siteTeam1.getGameSpreadOptionJuiceIndicator();
						spreadOptionJuiceNumber = siteTeam1.getGameSpreadOptionJuiceNumber();
						if (spreadOptionIndicator != null && !spreadOptionIndicator.isEmpty()
								&& spreadOptionNumber != null && !spreadOptionNumber.isEmpty()
								&& spreadOptionJuiceIndicator != null && !spreadOptionJuiceIndicator.isEmpty()
								&& spreadOptionJuiceNumber != null && !spreadOptionJuiceNumber.isEmpty()) {
							// Set the EventPackage
							previewData.setEp(eventPackage);
							int finalnumber = finalOrder(spreadOptionJuiceIndicator, spreadOptionJuiceNumber);

							// Get the values
							previewData.setLineindicator(spreadOptionIndicator.get(String.valueOf(finalnumber)));
							previewData
									.setLine(replaceDot(spreadOptionNumber.get(String.valueOf(finalnumber)), ".0", ""));
							previewData.setJuiceindicator(spreadOptionJuiceIndicator.get(String.valueOf(finalnumber)));
							previewData.setJuice(
									replaceDot(spreadOptionJuiceNumber.get(String.valueOf(finalnumber)), ".0", ""));
						}
					}
				} else if ("total".equals(previewInput.getLinetype())) {
					LinkedHashMap<String, String> totalOptionNumber = null;
					LinkedHashMap<String, String> totalOptionJuiceIndicator = null;
					LinkedHashMap<String, String> totalOptionJuiceNumber = null;
					if ("o".equals(previewInput.getLineindicator())) {
						totalOptionNumber = siteTeam1.getGameTotalOptionNumber();
						totalOptionJuiceIndicator = siteTeam1.getGameTotalOptionJuiceIndicator();
						totalOptionJuiceNumber = siteTeam1.getGameTotalOptionJuiceNumber();
						LOGGER.debug("totalOptionNumber: " + totalOptionNumber);
						LOGGER.debug("totalOptionJuiceIndicator: " + totalOptionJuiceIndicator);
						LOGGER.debug("totalOptionJuiceNumber: " + totalOptionJuiceNumber);
						if (totalOptionNumber != null && !totalOptionNumber.isEmpty()
								&& totalOptionJuiceIndicator != null && !totalOptionJuiceIndicator.isEmpty()
								&& totalOptionJuiceNumber != null && !totalOptionJuiceNumber.isEmpty()) {
							// Set the EventPackage
							previewData.setEp(eventPackage);

							int finalnumber = finalOrder(totalOptionJuiceIndicator, totalOptionJuiceNumber);
							LOGGER.debug("finalnumber: " + finalnumber);

							// Get the values
							LOGGER.debug("totalOptionNumber: " + totalOptionNumber.get(String.valueOf(finalnumber)));
							LOGGER.debug("totalOptionJuiceIndicator: "
									+ totalOptionJuiceIndicator.get(String.valueOf(finalnumber)));
							LOGGER.debug("totalOptionJuiceNumber: "
									+ totalOptionJuiceNumber.get(String.valueOf(finalnumber)));

							previewData.setLineindicator("o");
							previewData
									.setLine(replaceDot(totalOptionNumber.get(String.valueOf(finalnumber)), ".0", ""));
							previewData.setJuiceindicator(totalOptionJuiceIndicator.get(String.valueOf(finalnumber)));
							previewData.setJuice(
									replaceDot(totalOptionJuiceNumber.get(String.valueOf(finalnumber)), ".0", ""));
						}
					} else {
						totalOptionNumber = siteTeam2.getGameTotalOptionNumber();
						totalOptionJuiceIndicator = siteTeam2.getGameTotalOptionJuiceIndicator();
						totalOptionJuiceNumber = siteTeam2.getGameTotalOptionJuiceNumber();
						LOGGER.debug("totalOptionNumber: " + totalOptionNumber);
						LOGGER.debug("totalOptionJuiceIndicator: " + totalOptionJuiceIndicator);
						LOGGER.debug("totalOptionJuiceNumber: " + totalOptionJuiceNumber);
						if (totalOptionNumber != null && !totalOptionNumber.isEmpty()
								&& totalOptionJuiceIndicator != null && !totalOptionJuiceIndicator.isEmpty()
								&& totalOptionJuiceNumber != null && !totalOptionJuiceNumber.isEmpty()) {
							// Set the EventPackage
							previewData.setEp(eventPackage);

							int finalnumber = finalOrder(totalOptionJuiceIndicator, totalOptionJuiceNumber);
							LOGGER.debug("finalnumber: " + finalnumber);

							// Get the values
							LOGGER.debug("totalOptionNumber: " + totalOptionNumber.get(String.valueOf(finalnumber)));
							LOGGER.debug("totalOptionJuiceIndicator: "
									+ totalOptionJuiceIndicator.get(String.valueOf(finalnumber)));
							LOGGER.debug("totalOptionJuiceNumber: "
									+ totalOptionJuiceNumber.get(String.valueOf(finalnumber)));

							previewData.setLineindicator("u");
							previewData
									.setLine(replaceDot(totalOptionNumber.get(String.valueOf(finalnumber)), ".0", ""));
							previewData.setJuiceindicator(totalOptionJuiceIndicator.get(String.valueOf(finalnumber)));
							previewData.setJuice(
									replaceDot(totalOptionJuiceNumber.get(String.valueOf(finalnumber)), ".0", ""));
						}
					}
				} else if ("ml".equals(previewInput.getLinetype())) {
					LinkedHashMap<String, String> mlOptionJuiceIndicator = null;
					LinkedHashMap<String, String> mlOptionJuiceNumber = null;
					if (previewInput.getRotationid() % 2 == 0) {
						mlOptionJuiceIndicator = siteTeam2.getGameMLOptionJuiceIndicator();
						mlOptionJuiceNumber = siteTeam2.getGameMLOptionJuiceNumber();
						if (mlOptionJuiceIndicator != null && !mlOptionJuiceIndicator.isEmpty()
								&& mlOptionJuiceNumber != null && !mlOptionJuiceNumber.isEmpty()) {
							// Set the EventPackage
							previewData.setEp(eventPackage);

							int finalnumber = finalOrder(mlOptionJuiceIndicator, mlOptionJuiceNumber);

							// Get the values
							previewData.setLineindicator(mlOptionJuiceIndicator.get(String.valueOf(finalnumber)));
							previewData.setLine(
									replaceDot(mlOptionJuiceNumber.get(String.valueOf(finalnumber)), ".0", ""));
							previewData.setJuiceindicator(mlOptionJuiceIndicator.get(String.valueOf(finalnumber)));
							previewData.setJuice(
									replaceDot(mlOptionJuiceNumber.get(String.valueOf(finalnumber)), ".0", ""));
						}
					} else {
						mlOptionJuiceIndicator = siteTeam1.getGameMLOptionJuiceIndicator();
						mlOptionJuiceNumber = siteTeam1.getGameMLOptionJuiceNumber();
						if (mlOptionJuiceIndicator != null && !mlOptionJuiceIndicator.isEmpty()
								&& mlOptionJuiceNumber != null && !mlOptionJuiceNumber.isEmpty()) {
							// Set the EventPackage
							previewData.setEp(eventPackage);

							int finalnumber = finalOrder(mlOptionJuiceIndicator, mlOptionJuiceNumber);

							// Get the values
							previewData.setLineindicator(
									mlOptionJuiceIndicator.get(String.valueOf(finalnumber)).replace("\\\\.0", ""));
							previewData.setLine(
									replaceDot(mlOptionJuiceNumber.get(String.valueOf(finalnumber)), ".0", ""));
							previewData.setJuiceindicator(
									mlOptionJuiceIndicator.get(String.valueOf(finalnumber)).replace("\\\\.0", ""));
							previewData.setJuice(
									replaceDot(mlOptionJuiceNumber.get(String.valueOf(finalnumber)), ".0", ""));
						}
					}
				}
			}
		}

		LOGGER.info("Exiting checkGame()");
		return previewData;
	}

	/**
	 * 
	 * @param event
	 * @param ae
	 * @throws BatchException
	 */
	public void processSpreadTransaction(SpreadRecordEvent event, AccountEvent ae) throws BatchException {
		LOGGER.info("Entering processSpreadTransaction()");
		LOGGER.debug("SpreadRecordEvent: " + event);
		LOGGER.debug("AccountEvent: " + ae);
		userid = event.getUserid();
		textNumber = event.getTextnumber();

		final EventPackage eventPackage = setupTransaction(event, ae);
		LOGGER.debug("EventPackage" + eventPackage);

		if (eventPackage != null) {
			// Step 10 - Setup site transaction
			final SiteTransaction siteTransaction = getSpreadTransaction(event, eventPackage, ae);

			// Finish the transaction
			finishTransaction(siteTransaction, eventPackage, event, ae);
		}

		LOGGER.info("Exiting processSpreadTransaction()");
	}

	/**
	 * 
	 * @param event
	 * @param ae
	 * @throws BatchException
	 */
	public void processTotalTransaction(TotalRecordEvent event, AccountEvent ae) throws BatchException {
		LOGGER.info("Entering processTotalTransaction()");
		LOGGER.debug("TotalRecordEvent: " + event);
		LOGGER.debug("AccountEvent: " + ae);
		userid = event.getUserid();
		textNumber = event.getTextnumber();

		final EventPackage eventPackage = setupTransaction(event, ae);
		LOGGER.debug("EventPackage" + eventPackage);

		if (eventPackage != null) {
			// Step 10 - Setup site transaction
			final SiteTransaction siteTransaction = getTotalTransaction(event, eventPackage, ae);

			// Finish the transaction
			finishTransaction(siteTransaction, eventPackage, event, ae);
		}

		LOGGER.info("Exiting processTotalTransaction()");
	}

	/**
	 * 
	 * @param event
	 * @param ae
	 * @throws BatchException
	 */
	public void processMlTransaction(MlRecordEvent event, AccountEvent ae) throws BatchException {
		LOGGER.info("Entering processMlTransaction()");
		LOGGER.debug("MlRecordEvent: " + event);
		LOGGER.debug("AccountEvent: " + ae);
		userid = event.getUserid();
		textNumber = event.getTextnumber();

		final EventPackage eventPackage = setupTransaction(event, ae);
		LOGGER.debug("EventPackage" + eventPackage);

		if (eventPackage != null) {
			// Step 10 - Setup site transaction
			final SiteTransaction siteTransaction = getMlTransaction(event, eventPackage, ae);

			// Finish the transaction
			finishTransaction(siteTransaction, eventPackage, event, ae);
		}

		LOGGER.info("Exiting processMlTransaction()");
	}

	/**
	 * 
	 * @return
	 */
	public static Map<String, SiteProcessor> getAccountSites() {
		LOGGER.info("Entering getAccountSites()");
		LOGGER.info("Exiting getAccountSites()");
		return ACCOUNTSITES;
	}

	/**
	 * 
	 * @param username
	 * @param password
	 * @return
	 * @throws BatchException
	 */
	public abstract String loginToSite(String username, String password) throws BatchException;

	/**
	 * 
	 * @param pe
	 * @throws BatchException
	 */
	public void doProcessPendingEvent(PendingEvent pe) throws BatchException {
		// Do nothing
	}

	/**
	 * 
	 * @param type
	 * @return
	 * @throws BatchException
	 */
	protected abstract String selectSport(String type) throws BatchException;

	/**
	 * 
	 * @param xhtml
	 * @param siteTransaction
	 * @param eventPackage
	 * @param event
	 * @param ae
	 * @throws BatchException
	 */
	protected abstract void parseEventSelection(String xhtml, SiteTransaction siteTransaction,
			EventPackage eventPackage, BaseRecordEvent event, AccountEvent ae) throws BatchException;

	/**
	 * 
	 * @return
	 */
	protected abstract SiteTransaction createSiteTransaction();

	/**
	 * 
	 * @param siteTransaction
	 * @param eventPackage
	 * @param event
	 * @param ae
	 * @throws BatchException
	 */
	protected abstract String selectEvent(SiteTransaction siteTransaction, EventPackage eventPackage,
			BaseRecordEvent event, AccountEvent ae) throws BatchException;

	/**
	 * 
	 * @param siteTransaction
	 * @param eventPackage
	 * @param event
	 * @param ae
	 * @return
	 * @throws BatchException
	 */
	protected abstract String completeTransaction(SiteTransaction siteTransaction, EventPackage eventPackage,
			BaseRecordEvent event, AccountEvent ae) throws BatchException;

	/**
	 * 
	 * @param xhtml
	 * @return
	 * @throws BatchException
	 */
	protected abstract String parseTicketTransaction(String xhtml) throws BatchException;

	/**
	 * 
	 * @param event
	 * @param ae
	 * @return
	 * @throws BatchException
	 */
	public EventPackage setupTransaction(BaseRecordEvent event, AccountEvent ae) throws BatchException {
		LOGGER.info("Entering setupTransaction()");
		EventPackage eventPackage = null;
		String xhtml = "";

		try {
			// Step 1 - Setup timezone
			timezone = ae.getTimezone();

			// Step 2 - Setup proxy client
			this.proxyName = ae.getProxy();
			httpClientWrapper.setupHttpClient(proxyName);

			// Hack for Elite Sports
			sportWagerType = event.getSport();

			//
			// Call the login of the site
			//
			// Step 3 - Login
			xhtml = loginToSite(httpClientWrapper.getUsername(), httpClientWrapper.getPassword());
			LOGGER.debug("XHTML: " + xhtml);

			// Act as a human if we should
			actasHuman();

			// Step 4a - Get event sport type
			sportType = event.getSport();
			this.siteParser.setSportType(sportType);
			LOGGER.debug("SportType: " + sportType);

			// Step 4b - Get the event ID
			eventId = event.getEventid();

			// Step 5 - Get menu
			MAP_DATA = getMenuFromSite(sportType, xhtml);
			LOGGER.debug("MAP_DATA: " + MAP_DATA);

			// Act as a human if we should
			actasHuman();

			//
			// Call the Menu selection of the site
			//
			// Step 7 - Select sport type
			xhtml = selectSport(sportType);
			LOGGER.debug("XHTML: " + xhtml);

			// Step 8 - Parse the games from the site
			final Iterator<EventPackage> ep = parseGamesOnSite(sportType, xhtml);
			LOGGER.debug("ep: " + ep);

			// Act as a human if we should
			actasHuman();

			// Step 9 - Find event
			eventPackage = findEvent(ep, event);
			if (eventPackage == null) {
				throw new BatchException(BatchErrorCodes.GAME_NOT_AVAILABLE, BatchErrorMessage.GAME_NOT_AVAILABLE,
						"Game cannot be found on site for " + event.getEventname(), xhtml);
			}
		} catch (BatchException be) {
			LOGGER.error(be.getMessage(), be);
			if (be.getErrorcode() == BatchErrorCodes.HTTP_BAD_GATEWAY_EXCEPTION) {
				final String[] proxyLocations = ProxyContainer.getProxyNames();
				for (int x = 0; (proxyLocations != null && x < proxyLocations.length); x++) {
					String proxyName = proxyLocations[x];
					if (proxyName != null && !proxyName.equals("None") && !proxyName.equals(ae.getProxy())) {
						ae.setProxy(proxyName);
						break;
					}
				}
				final String type = event.getEventtype();
				HttpClientUtils.closeQuietly(this.httpClientWrapper.getClient());
				this.httpClientWrapper.setupHttpClient(proxyName);
				this.loginToSite(this.httpClientWrapper.getUsername(), this.httpClientWrapper.getPassword());
				if ("spread".equals(type)) {
					processSpreadTransaction((SpreadRecordEvent) event, ae);
				} else if ("total".equals(type)) {
					processTotalTransaction((TotalRecordEvent) event, ae);
				} else if ("ml".equals(type)) {
					processMlTransaction((MlRecordEvent) event, ae);
				}
			} else {
				if (be.getHtml() == null || (be.getHtml() != null && be.getHtml().length() == 0)) {
					be.setHtml(xhtml);
					ae.setAccounthtml(xhtml);
				}
				throw be;
			}
		}

		LOGGER.info("Exiting setupTransaction()");
		return eventPackage;
	}

	/**
	 * 
	 * @param siteTransaction
	 * @param eventPackage
	 * @param event
	 * @param ae
	 * @throws BatchException
	 */
	public void finishTransaction(SiteTransaction siteTransaction, EventPackage eventPackage, BaseRecordEvent event,
			AccountEvent ae) throws BatchException {
		LOGGER.info("Entering finishTransaction()");
		String xhtml = "";

		try {
			// Set the text number
			this.textNumber = event.getTextnumber();

			final String ticketNumber = processBet(siteTransaction, eventPackage, event, ae);
			LOGGER.debug("ticketNumber: " + ticketNumber);

			// Set the account data
			ae.setAccountconfirmation(ticketNumber);
			ae.setAccounthtml("<![CDATA[" + xhtml + "]]>");

			// Setup the risk and win amounts
			setupRiskWinAmounts(event, ae);
		} catch (BatchException be) {
			LOGGER.error(be.getMessage(), be);

			if (be.getErrorcode() == BatchErrorCodes.HTTP_BAD_GATEWAY_EXCEPTION) {
				final String[] proxyLocations = ProxyContainer.getProxyNames();
				for (int x = 0; (proxyLocations != null && x < proxyLocations.length); x++) {
					String proxyName = proxyLocations[x];
					if (proxyName != null && !proxyName.equals("None") && !proxyName.equals(ae.getProxy())) {
						ae.setProxy(proxyName);
						break;
					}
				}
				final String type = event.getEventtype();
				if ("spread".equals(type)) {
					processSpreadTransaction((SpreadRecordEvent) event, ae);
				} else if ("total".equals(type)) {
					processTotalTransaction((TotalRecordEvent) event, ae);
				} else if ("ml".equals(type)) {
					processMlTransaction((MlRecordEvent) event, ae);
				}
			} else {
				if (be.getHtml() == null || (be.getHtml() != null && be.getHtml().length() == 0)) {
					be.setHtml(xhtml);
					ae.setAccounthtml(xhtml);
				}
				throw be;
			}
		}

		LOGGER.info("Exiting finishTransaction()");
	}

	/**
	 * 
	 * @param siteTransaction
	 * @param eventPackage
	 * @param event
	 * @param ae
	 * @return
	 * @throws BatchException
	 */
	private String processBet(SiteTransaction siteTransaction, EventPackage eventPackage, BaseRecordEvent event,
			AccountEvent ae) throws BatchException {
		LOGGER.info("Entering processBet()");
		String ticketNumber = null;
		String xhtml = null;

		try {
			// Act as a human if we should
			actasHuman();

			//
			// Call the Game selection
			//
			// Step 12 - Select event and parse wager info
			//
			xhtml = selectEvent(siteTransaction, eventPackage, event, ae);
			LOGGER.debug("xhtml: " + xhtml);

			//
			// Step 13 - Parse the event selection
			//
			parseEventSelection(xhtml, siteTransaction, eventPackage, event, ae);

			// Act as a human if we should
			actasHuman();

			//
			// Call the Complete Wager
			//
			// Step 14 - Complete transaction
			//
			xhtml = completeTransaction(siteTransaction, eventPackage, event, ae);

			//
			// Step 15 - Parse the ticket #
			//
			ticketNumber = parseTicketTransaction(xhtml);
		} catch (BatchException b) {
			if (b.getErrorcode() == BatchErrorCodes.RETRY_CAPTCHA) {
				if (captchaRetry++ < 100) {
					ticketNumber = processBet(siteTransaction, eventPackage, event, ae);
				} else {
					throw new BatchException(BatchErrorCodes.CAPTCHA_PROCESSING_EXCEPTION,
							BatchErrorMessage.CAPTCHA_PROCESSING_EXCEPTION, "Problem getting captcha text", xhtml);
				}
			} else {
				throw b;
			}
		}

		LOGGER.info("Exiting processBet()");
		return ticketNumber;
	}

	/**
	 * 
	 * @param ep
	 * @param rotationId
	 * @param sport
	 * @param team1
	 * @param team2
	 * @param eventdatetime
	 * @return
	 * @throws BatchException
	 */
	public EventPackage findPreviewEvent(Iterator<EventPackage> ep, Long rotationId, String sport, String team1,
			String team2, Date eventdatetime) throws BatchException {
		LOGGER.info("Entering findPreviewEvent()");
		LOGGER.debug("rotationId: " + rotationId);
		SiteEventPackage eventPackage = null;
		boolean found = false;

		// Process the transaction
		if (ep != null) {
			while (ep.hasNext() && !found) {
				eventPackage = (SiteEventPackage) ep.next();
				LOGGER.debug("EventPackage: " + eventPackage);
				final SiteTeamPackage teampackage1 = eventPackage.getSiteteamone();
				final SiteTeamPackage teampackage2 = eventPackage.getSiteteamtwo();
				LOGGER.debug("teampackage1: " + teampackage1);
				LOGGER.debug("teampackage2: " + teampackage2);

				if (rotationId != null && teampackage1 != null && teampackage2 != null && teampackage1.getId() != null
						&& teampackage2.getId() != null) {
					final Integer teamid1 = teampackage1.getId();
					final Integer teamid2 = teampackage2.getId();
					final Integer teamid11H = teamid1 + 1000;
					final Integer teamid12H = teamid1 + 2000;
					final Integer teamid13H = teamid1 + 3000;
					final Integer teamid21H = teamid2 + 1000;
					final Integer teamid22H = teamid2 + 2000;
					final Integer teamid23H = teamid2 + 3000;

					LOGGER.debug("Event ID: " + rotationId.intValue());
					LOGGER.debug("CheckPackage ID: " + eventPackage.getId().intValue());
					LOGGER.debug("teamid1: " + teamid1);
					LOGGER.debug("teamid2: " + teamid2);

					// Check if the games match
					if (rotationId.intValue() == teamid1.intValue() || rotationId.intValue() == teamid2.intValue()
							|| rotationId.intValue() == teamid11H.intValue()
							|| rotationId.intValue() == teamid12H.intValue()
							|| rotationId.intValue() == teamid13H.intValue()
							|| rotationId.intValue() == teamid21H.intValue()
							|| rotationId.intValue() == teamid22H.intValue()
							|| rotationId.intValue() == teamid23H.intValue()) {
						found = true;
						LOGGER.debug("FOUND EVENT PACKAGE: " + eventPackage);
						LOGGER.info("Exiting findPreviewEvent()");
						return eventPackage;
					}
				} else {
					final SportsInsightsSite sportsInsightSite = new SportsInsightsSite(
							"https://sportsinsights.actionnetwork.com", "mojaxsventures@gmail.com", "action1");
					final EventsPackage eventsPackage = sportsInsightSite.getSport(sport);
					final Set<EventPackage> eventPackages = eventsPackage.getEvents();
					final Iterator<EventPackage> itr = eventPackages.iterator();
					while (itr.hasNext()) {
						final EventPackage ePackage = itr.next();
						if (rotationId.intValue() == ePackage.getId().intValue()) {
							LOGGER.debug("ePackage: " + ePackage);
							team1 = ePackage.getTeamone().getTeam();
							team2 = ePackage.getTeamtwo().getTeam();
						}
					}

					LOGGER.debug("event.getEventteam1(): " + team1);
					LOGGER.debug("eventPackage.getTeamone(): " + eventPackage.getTeamone());
					LOGGER.debug("event.getEventteam2(): " + team2);
					LOGGER.debug("eventPackage.getTeamtwo(): " + eventPackage.getTeamtwo());

					if (team1 != null && eventPackage.getTeamone() != null && team2 != null
							&& eventPackage.getTeamtwo() != null) {
						String eventTeamOne = eventPackage.getTeamone().getTeam();
						String eventTeamTwo = eventPackage.getTeamtwo().getTeam();
						if (eventTeamOne != null) {
							if (eventTeamOne.contains("CS Northridge")) {
								eventTeamOne = "CS Northridge";
							} else if (eventTeamOne.contains("CS Fullerton")) {
								eventTeamOne = "CS Fullerton";
							} else if (eventTeamOne.contains("E Tenn St")) {
								eventTeamOne = "E Tennessee St";
							} else if (eventTeamOne.contains("UNC Wilmington")) {
								eventTeamOne = "NC Wilmington";
							} else {
								eventTeamOne = eventTeamOne.replaceAll(" St", " State");
								eventTeamOne = eventTeamOne.replaceAll("S ", "South ");
								eventTeamOne = eventTeamOne.replaceAll("N ", "North ");
								eventTeamOne = eventTeamOne.replaceAll("E ", "East ");
								eventTeamOne = eventTeamOne.replaceAll("W ", "West ");
							}
							eventTeamOne = eventTeamOne.trim();
							eventPackage.getTeamone().setTeam(eventTeamOne);
						}
						if (eventTeamTwo != null) {
							if (eventTeamTwo.contains("CS Northridge")) {
								eventTeamTwo = "CS Northridge";
							} else if (eventTeamTwo.contains("CS Fullerton")) {
								eventTeamTwo = "CS Fullerton";
							} else if (eventTeamTwo.contains("E Tenn St")) {
								eventTeamTwo = "E Tennessee St";
							} else if (eventTeamTwo.contains("UNC Wilmington")) {
								eventTeamTwo = "NC Wilmington";
							} else {
								eventTeamTwo = eventTeamTwo.replaceAll(" St", " State");
								eventTeamTwo = eventTeamTwo.replaceAll("S ", "South ");
								eventTeamTwo = eventTeamTwo.replaceAll("N ", "North ");
								eventTeamTwo = eventTeamTwo.replaceAll("E ", "East ");
								eventTeamTwo = eventTeamTwo.replaceAll("W ", "West ");
							}
							eventTeamTwo = eventTeamTwo.trim();
							eventPackage.getTeamtwo().setTeam(eventTeamTwo);
						}
						LOGGER.debug("event.getEventteam1(): " + team1);
						LOGGER.debug("eventTeamOne: " + eventTeamOne);
						LOGGER.debug("event.getEventteam2(): " + team2);
						LOGGER.debug("eventTeamTwo: " + eventTeamTwo);

						if ((team1.equals(eventTeamOne) && team2.equals(eventTeamTwo))
								|| ((team1 + " 1H").equals(eventPackage.getTeamone().getTeam())
										&& (team2 + " 1H").equals(eventPackage.getTeamtwo().getTeam()))
								|| ((team1 + " 2H").equals(eventPackage.getTeamone().getTeam())
										&& (team2 + " 2H").equals(eventPackage.getTeamtwo().getTeam()))
								|| ((team1 + " 3H").equals(eventPackage.getTeamone().getTeam())
										&& (team2 + " 3H").equals(eventPackage.getTeamtwo().getTeam()))
								|| (("1H " + team1).equals(eventPackage.getTeamone().getTeam())
										&& ("1H " + team2).equals(eventPackage.getTeamtwo().getTeam()))
								|| (("2H " + team1).equals(eventPackage.getTeamone().getTeam())
										&& ("2H " + team2).equals(eventPackage.getTeamtwo().getTeam()))
								|| (("3H " + team1).equals(eventPackage.getTeamone().getTeam())
										&& ("3H " + team2).equals(eventPackage.getTeamtwo().getTeam()))) {
							found = true;
							String timeZoneName = null;
							if ("ET".equals(this.timezone)) {
								timeZoneName = "America/New_York";
							} else if ("CT".equals(this.timezone)) {
								timeZoneName = "America/Chicago";
							} else if ("MT".equals(this.timezone)) {
								timeZoneName = "America/Denver";
							} else if ("PT".equals(this.timezone)) {
								timeZoneName = "America/Los_Angeles";
							} else {
								timeZoneName = "America/New_York";
							}
							final TimeZone zone = TimeZone.getTimeZone(timeZoneName);
							final Calendar eventCalendar = Calendar.getInstance();
							final Calendar eventPackageCalendar = Calendar.getInstance();
							eventCalendar.setTimeZone(zone);
							eventCalendar.setTime(eventdatetime);
							eventPackageCalendar.setTimeZone(zone);
							eventPackageCalendar.setTime(eventPackage.getEventdatetime());
							LOGGER.debug("event.getEventdatetime(): " + eventdatetime);
							LOGGER.debug("eventPackage.getEventdatetime(): " + eventPackage.getEventdatetime());
							LOGGER.debug("eventCalendar.get(Calendar.MONTH): " + eventCalendar.get(Calendar.MONTH));
							LOGGER.debug("eventPackageCalendar.get(Calendar.MONTH): "
									+ eventPackageCalendar.get(Calendar.MONTH));
							LOGGER.debug("eventCalendar.get(Calendar.DAY_OF_MONTH): "
									+ eventCalendar.get(Calendar.DAY_OF_MONTH));
							LOGGER.debug("eventPackageCalendar.get(Calendar.DAY_OF_MONTH): "
									+ eventPackageCalendar.get(Calendar.DAY_OF_MONTH));
							LOGGER.debug("eventCalendar.get(Calendar.YEAR): " + eventCalendar.get(Calendar.YEAR));
							LOGGER.debug("eventPackageCalendar.get(Calendar.YEAR): "
									+ eventPackageCalendar.get(Calendar.YEAR));

							if (eventCalendar.get(Calendar.MONTH) == eventPackageCalendar.get(Calendar.MONTH)
									&& eventCalendar.get(Calendar.DAY_OF_MONTH) == eventPackageCalendar
											.get(Calendar.DAY_OF_MONTH)
									&& eventCalendar.get(Calendar.YEAR) == eventPackageCalendar.get(Calendar.YEAR)) {
								LOGGER.debug("FOUND EVENT PACKAGE: " + eventPackage);
								LOGGER.info("Exiting findPreviewEvent()");
								return eventPackage;
							}
						}
					}
				}
			}
		} else {
			throw new BatchException(BatchErrorCodes.ACCOUNT_CANNOT_RETRIEVE_EVENTS,
					BatchErrorMessage.ACCOUNT_CANNOT_RETRIEVE_EVENTS, "Cannot retrieve events");
		}

		if (!found) {
			eventPackage = null;
		}

		LOGGER.debug("FINAL EVENT PACKAGE: " + eventPackage);
		LOGGER.info("Exiting findPreviewEvent()");
		return eventPackage;
	}

	/**
	 * 
	 * @param event
	 * @param ae
	 */
	private void setupRiskWinAmounts(BaseRecordEvent event, AccountEvent ae) {
		LOGGER.info("Entering setupRiskWinAmounts()");
		float juice = 0;

		// Get the spread transaction
		if (event instanceof SpreadRecordEvent) {
			juice = ae.getSpreadjuice();
		} else if (event instanceof TotalRecordEvent) {
			juice = ae.getTotaljuice();
		} else if (event instanceof MlRecordEvent) {
			juice = ae.getMljuice();
		}

		boolean isNegative = false;

		float factor = 1;
		if (juice < 0) {
			factor = 100 / juice;
			isNegative = true;
		} else {
			factor = juice / 100;
		}

		Double theAmount = new Double(ae.getActualamount());
		if (isNegative) {
			// Setup the data on the backend
			final Double risk = theAmount / factor;
			Double riskAmount = round(risk.doubleValue(), 2);
			Double rAmount = Math.abs(riskAmount);
			ae.setRiskamount(rAmount.toString());
			ae.setTowinamount(theAmount.toString());
		} else {
			// Setup the data on the backend
			final Double win = theAmount * factor;
			Double winAmount = round(win.doubleValue(), 2);
			ae.setRiskamount(theAmount.toString());
			ae.setTowinamount(winAmount.toString());
		}

		LOGGER.info("Exiting setupRiskWinAmounts()");
	}

	/**
	 * 
	 * @param ep
	 * @param event
	 * @return
	 * @throws BatchException
	 */
	protected EventPackage findEvent(Iterator<EventPackage> ep, BaseRecordEvent event) throws BatchException {
		LOGGER.info("Entering findEvent()");
		LOGGER.debug("Event: " + event);
		EventPackage eventPackage = null;

		// Process the transaction
		if (ep != null) {
			LOGGER.debug("ep: " + ep.hasNext());
			while (ep.hasNext()) {
				eventPackage = ep.next();
				LOGGER.debug("EventPackage: " + eventPackage);
				if (event.getEventid() != null && eventPackage.getId() != null) {
					final int eventId = event.getEventid().intValue();
					final int eventPackageId = eventPackage.getId().intValue();
//					669 - Game
//					669 - 1H or 2H
//					1669 - 1H
//					2669 - 2H
//					5669 - 3Q
					final int firstHalfId = eventId + 1000;
					final int secondHalfId = eventId + 2000;
					final int thirdHalfId = eventId + 3000;
					final int firstHalfBack = eventId - 1000;
					final int secondHalfBack = eventId - 2000;
					final int thirdHalfBack = eventId - 3000;

					LOGGER.debug("Event ID: " + eventId);
					LOGGER.debug("CheckPackage ID: " + eventPackageId);
					LOGGER.debug("firstHalfId ID: " + firstHalfId);
					LOGGER.debug("secondHalfId ID: " + secondHalfId);
					LOGGER.debug("thirdHalfId ID: " + thirdHalfId);
					LOGGER.debug("firstHalfBack ID: " + firstHalfBack);
					LOGGER.debug("secondHalfBack ID: " + secondHalfBack);
					LOGGER.debug("thirdHalfBack ID: " + thirdHalfBack);
					final String sport = event.getSport();

					// Check if the games match
					if ((sport.contains("lines") || sport.contains("first") || sport.contains("second"))
							&& (eventPackageId < 3000) && (eventId == eventPackageId || firstHalfId == eventPackageId
									|| secondHalfId == eventPackageId)) {
						LOGGER.debug("FOUND EVENT PACKAGE: " + eventPackage);
						LOGGER.info("Exiting findEvent()");
						return eventPackage;
					} else if ((sport.contains("lines") || sport.contains("first") || sport.contains("second"))
							&& (eventPackageId < 3000) && (eventId == eventPackageId || firstHalfBack == eventPackageId
									|| secondHalfBack == eventPackageId)) {
						LOGGER.debug("FOUND EVENT PACKAGE: " + eventPackage);
						LOGGER.info("Exiting findEvent()");
						return eventPackage;
					} else if (sport.contains("third") && (eventPackageId < 4000) && (eventId == eventPackageId
							|| thirdHalfId == eventPackageId || thirdHalfBack == eventPackageId)) {
						LOGGER.debug("FOUND EVENT PACKAGE: " + eventPackage);
						LOGGER.info("Exiting findEvent()");
						return eventPackage;
					} else if ((sport.contains("lines") || sport.contains("first") || sport.contains("second")) && 
							   (eventPackageId >= 300000 && eventPackageId < 400000) && 
							   (eventId == eventPackageId || firstHalfBack == eventPackageId || secondHalfBack == eventPackageId)) {
						LOGGER.debug("FOUND EVENT PACKAGE: " + eventPackage);
						LOGGER.info("Exiting findEvent()");
						return eventPackage;
					}
				} else if (eventPackage.getId() != null && (eventPackage.getId().intValue() < 4000)) {
					final SportsInsightsSite sportsInsightSite = new SportsInsightsSite(
							"https://sportsinsights.actionnetwork.com", "mojaxsventures@gmail.com", "action1");
					final EventsPackage eventsPackage = sportsInsightSite.getSport(event.getSport());
					final Set<EventPackage> eventPackages = eventsPackage.getEvents();
					final Iterator<EventPackage> itr = eventPackages.iterator();
					while (itr.hasNext()) {
						final EventPackage ePackage = itr.next();
						if (event.getEventid().intValue() == ePackage.getId().intValue()) {
							LOGGER.debug("ePackage: " + ePackage);
							event.setEventteam1(ePackage.getTeamone().getTeam());
							event.setEventteam2(ePackage.getTeamtwo().getTeam());
						}
					}

					LOGGER.debug("event.getEventteam1(): " + event.getEventteam1());
					LOGGER.debug("eventPackage.getTeamone(): " + eventPackage.getTeamone());
					LOGGER.debug("event.getEventteam2(): " + event.getEventteam2());
					LOGGER.debug("eventPackage.getTeamtwo(): " + eventPackage.getTeamtwo());

					if (event.getEventteam1() != null && eventPackage.getTeamone() != null
							&& event.getEventteam2() != null && eventPackage.getTeamtwo() != null) {
						String eventTeamOne = eventPackage.getTeamone().getTeam();
						String eventTeamTwo = eventPackage.getTeamtwo().getTeam();
						if (eventTeamOne != null) {
							if (eventTeamOne.contains("CS Northridge")) {
								eventTeamOne = "CS Northridge";
							} else if (eventTeamOne.contains("CS Fullerton")) {
								eventTeamOne = "CS Fullerton";
							} else if (eventTeamOne.contains("E Tenn St")) {
								eventTeamOne = "E Tennessee St";
							} else if (eventTeamOne.contains("UNC Wilmington")) {
								eventTeamOne = "NC Wilmington";
							} else {
								eventTeamOne = eventTeamOne.replaceAll(" St", " State");
								eventTeamOne = eventTeamOne.replaceAll("S ", "South ");
								eventTeamOne = eventTeamOne.replaceAll("N ", "North ");
								eventTeamOne = eventTeamOne.replaceAll("E ", "East ");
								eventTeamOne = eventTeamOne.replaceAll("W ", "West ");
							}
							eventTeamOne = eventTeamOne.trim();
							eventPackage.getTeamone().setTeam(eventTeamOne);
						}
						if (eventTeamTwo != null) {
							if (eventTeamTwo.contains("CS Northridge")) {
								eventTeamTwo = "CS Northridge";
							} else if (eventTeamTwo.contains("CS Fullerton")) {
								eventTeamTwo = "CS Fullerton";
							} else if (eventTeamTwo.contains("E Tenn St")) {
								eventTeamTwo = "E Tennessee St";
							} else if (eventTeamTwo.contains("UNC Wilmington")) {
								eventTeamTwo = "NC Wilmington";
							} else {
								eventTeamTwo = eventTeamTwo.replaceAll(" St", " State");
								eventTeamTwo = eventTeamTwo.replaceAll("S ", "South ");
								eventTeamTwo = eventTeamTwo.replaceAll("N ", "North ");
								eventTeamTwo = eventTeamTwo.replaceAll("E ", "East ");
								eventTeamTwo = eventTeamTwo.replaceAll("W ", "West ");
							}
							eventTeamTwo = eventTeamTwo.trim();
							eventPackage.getTeamtwo().setTeam(eventTeamTwo);
						}
						LOGGER.debug("event.getEventteam1(): " + event.getEventteam1());
						LOGGER.debug("eventTeamOne: " + eventTeamOne);
						LOGGER.debug("event.getEventteam2(): " + event.getEventteam2());
						LOGGER.debug("eventTeamTwo: " + eventTeamTwo);

						if ((event.getEventteam1().equals(eventTeamOne) && event.getEventteam2().equals(eventTeamTwo))
								|| ((event.getEventteam1() + " 1H").equals(eventPackage.getTeamone().getTeam())
										&& (event.getEventteam2() + " 1H").equals(eventPackage.getTeamtwo().getTeam()))
								|| ((event.getEventteam1() + " 2H").equals(eventPackage.getTeamone().getTeam())
										&& (event.getEventteam2() + " 2H").equals(eventPackage.getTeamtwo().getTeam()))
								|| ((event.getEventteam1() + " 3H").equals(eventPackage.getTeamone().getTeam())
										&& (event.getEventteam2() + " 3H").equals(eventPackage.getTeamtwo().getTeam()))
								|| (("1H " + event.getEventteam1()).equals(eventPackage.getTeamone().getTeam())
										&& ("1H " + event.getEventteam2()).equals(eventPackage.getTeamtwo().getTeam()))
								|| (("2H " + event.getEventteam1()).equals(eventPackage.getTeamone().getTeam())
										&& ("2H " + event.getEventteam2()).equals(eventPackage.getTeamtwo().getTeam()))
								|| (("3H " + event.getEventteam1()).equals(eventPackage.getTeamone().getTeam())
										&& ("3H " + event.getEventteam2()).equals(eventPackage.getTeamtwo().getTeam()))
								|| ((event.getEventteam1() + " 1P").equals(eventPackage.getTeamone().getTeam())
										&& (event.getEventteam2() + " 1P").equals(eventPackage.getTeamtwo().getTeam()))
								|| ((event.getEventteam1() + " 2P").equals(eventPackage.getTeamone().getTeam())
										&& (event.getEventteam2() + " 2P").equals(eventPackage.getTeamtwo().getTeam()))
								|| ((event.getEventteam1() + " 3P").equals(eventPackage.getTeamone().getTeam())
										&& (event.getEventteam2() + " 3P").equals(eventPackage.getTeamtwo().getTeam()))
								|| (("1P " + event.getEventteam1()).equals(eventPackage.getTeamone().getTeam())
										&& ("1P " + event.getEventteam2()).equals(eventPackage.getTeamtwo().getTeam()))
								|| (("2P " + event.getEventteam1()).equals(eventPackage.getTeamone().getTeam())
										&& ("2P " + event.getEventteam2()).equals(eventPackage.getTeamtwo().getTeam()))
								|| (("3P " + event.getEventteam1()).equals(eventPackage.getTeamone().getTeam())
										&& ("3P " + event.getEventteam2())
												.equals(eventPackage.getTeamtwo().getTeam()))) {
							String timeZoneName = null;
							if ("ET".equals(this.timezone)) {
								timeZoneName = "America/New_York";
							} else if ("CT".equals(this.timezone)) {
								timeZoneName = "America/Chicago";
							} else if ("MT".equals(this.timezone)) {
								timeZoneName = "America/Denver";
							} else if ("PT".equals(this.timezone)) {
								timeZoneName = "America/Los_Angeles";
							} else {
								timeZoneName = "America/New_York";
							}
							final TimeZone zone = TimeZone.getTimeZone(timeZoneName);
							final Calendar eventCalendar = Calendar.getInstance();
							final Calendar eventPackageCalendar = Calendar.getInstance();
							eventCalendar.setTimeZone(zone);
							eventCalendar.setTime(event.getEventdatetime());
							eventPackageCalendar.setTimeZone(zone);
							eventPackageCalendar.setTime(eventPackage.getEventdatetime());
							LOGGER.debug("event.getEventdatetime(): " + event.getEventdatetime());
							LOGGER.debug("eventPackage.getEventdatetime(): " + eventPackage.getEventdatetime());
							LOGGER.debug("eventCalendar.get(Calendar.MONTH): " + eventCalendar.get(Calendar.MONTH));
							LOGGER.debug("eventPackageCalendar.get(Calendar.MONTH): "
									+ eventPackageCalendar.get(Calendar.MONTH));
							LOGGER.debug("eventCalendar.get(Calendar.DAY_OF_MONTH): "
									+ eventCalendar.get(Calendar.DAY_OF_MONTH));
							LOGGER.debug("eventPackageCalendar.get(Calendar.DAY_OF_MONTH): "
									+ eventPackageCalendar.get(Calendar.DAY_OF_MONTH));
							LOGGER.debug("eventCalendar.get(Calendar.YEAR): " + eventCalendar.get(Calendar.YEAR));
							LOGGER.debug("eventPackageCalendar.get(Calendar.YEAR): "
									+ eventPackageCalendar.get(Calendar.YEAR));

							if (eventCalendar.get(Calendar.MONTH) == eventPackageCalendar.get(Calendar.MONTH)
									&& eventCalendar.get(Calendar.DAY_OF_MONTH) == eventPackageCalendar
											.get(Calendar.DAY_OF_MONTH)
									&& eventCalendar.get(Calendar.YEAR) == eventPackageCalendar.get(Calendar.YEAR)) {
								LOGGER.debug("event: " + event);
								LOGGER.debug("FOUND EVENT PACKAGE: " + eventPackage);
								LOGGER.info("Exiting findEvent()");
								return eventPackage;
							}
						}
					}
				}
			}
		} else {
			throw new BatchException(BatchErrorCodes.ACCOUNT_CANNOT_RETRIEVE_EVENTS,
					BatchErrorMessage.ACCOUNT_CANNOT_RETRIEVE_EVENTS,
					"Cannot retrieve events for Event: " + event.getEventname());
		}

		LOGGER.debug("FINAL EVENT PACKAGE: " + null);
		LOGGER.info("Exiting findEvent()");
		return null;
	}

	/**
	 * 
	 * @param sportType
	 * @param xhtml
	 * @return
	 * @throws BatchException
	 */
	protected Iterator<EventPackage> parseGamesOnSite(String sportType, String xhtml) throws BatchException {
		LOGGER.info("Entering parseGamesOnSite()");
		LOGGER.debug("sportType: " + sportType);
		gamesXhtml = xhtml;
		final EventsPackage esp = new EventsPackage();
		Iterator<EventPackage> ep = null;

		// Clear the MAP
		if (MAP_DATA != null) {
			MAP_DATA.clear();
		}

		// Parse the games
		final List<SiteEventPackage> eventsPackage = siteParser.parseGames(xhtml, sportType, MAP_DATA);
		if (eventsPackage != null) {
			for (int x = 0; x < eventsPackage.size(); x++) {
				final SiteEventPackage aep = eventsPackage.get(x);
				LOGGER.debug("aep: " + aep);
				esp.addEvent(aep);
			}
		} else {
			LOGGER.error(sportType + " null eventsPackage");
			// Throw Exception
			throw new BatchException(BatchErrorCodes.XHTML_GAMES_PARSING_EXCEPTION,
					BatchErrorMessage.XHTML_GAMES_PARSING_EXCEPTION, "No games available", "<![CDATA[" + xhtml + "]]>");
		}

		// Check for valid objects
		final Set<EventPackage> events = esp.getEvents();
		if (events != null && !events.isEmpty()) {
			ep = events.iterator();
		}
		LOGGER.debug("EP: " + ep);

		LOGGER.info("Exiting parseGamesOnSite()");
		return ep;
	}

	/**
	 * 
	 * @param event
	 * @param eventPackge
	 * @param ae
	 * @return
	 * @throws BatchException
	 */
	public SiteTransaction getSpreadTransaction(SpreadRecordEvent event, EventPackage eventPackge, AccountEvent ae)
			throws BatchException {
		LOGGER.info("Entering getSpreadTransaction()");

		// First check for which team/transaction
		final SiteTransaction siteTransaction = determineSpreadTransactionData(event, eventPackge, ae);
		LOGGER.debug("SiteTransaction: " + siteTransaction);

		if (siteTransaction == null) {
			throw new BatchException("Transaction not setup successfully");
		}

		LOGGER.info("Exiting getSpreadTransaction()");
		return siteTransaction;
	}

	/**
	 * 
	 * @param event
	 * @param eventPackge
	 * @param ae
	 * @return
	 * @throws BatchException
	 */
	public SiteTransaction getTotalTransaction(TotalRecordEvent event, EventPackage eventPackge, AccountEvent ae)
			throws BatchException {
		LOGGER.info("Entering getTotalTransaction()");

		// First check for which team/transaction
		final SiteTransaction siteTransaction = determineTotalTransactionData(event, eventPackge, ae);
		LOGGER.debug("SiteTransaction: " + siteTransaction);
		if (siteTransaction == null) {
			throw new BatchException("Transaction not setup successfully");
		}

		LOGGER.info("Exiting getTotalTransaction()");
		return siteTransaction;
	}

	/**
	 * 
	 * @param event
	 * @param eventPackge
	 * @param ae
	 * @return
	 * @throws BatchException
	 */
	public SiteTransaction getMlTransaction(MlRecordEvent event, EventPackage eventPackge, AccountEvent ae)
			throws BatchException {
		LOGGER.info("Entering getMlTransaction()");

		// First check for which team/transaction
		final SiteTransaction siteTransaction = determineMlTransactionData(event, eventPackge, ae);
		LOGGER.debug("SiteTransaction: " + siteTransaction);
		if (siteTransaction == null) {
			throw new BatchException("Transaction not setup successfully");
		}

		LOGGER.info("Exiting getMlTransaction()");
		return siteTransaction;
	}

	/**
	 * 
	 * @param event
	 * @param eventPackge
	 * @param ae
	 * @return
	 * @throws BatchException newly added on 31-10-18
	 */
	public SiteTransaction getTeamTotalTransaction(TeamTotalRecordEvent event, EventPackage eventPackge,
			AccountEvent ae) throws BatchException {
		LOGGER.info("Entering getTeamTotalTransaction()");

		// First check for which team/transaction
		final SiteTransaction siteTransaction = determineTeamTotalTransactionData(event, eventPackge, ae);
		LOGGER.debug("SiteTransaction: " + siteTransaction);
		if (siteTransaction == null) {
			throw new BatchException("Transaction not setup successfully");
		}

		LOGGER.info("Exiting getTeamTotalTransaction()");
		return siteTransaction;
	}

	/**
	 * 
	 * @param teamTotalRecordEvent
	 * @param eventPackage
	 * @param accountEvent
	 * @return
	 * @throws BatchException newly added on 31-10-18
	 */
	protected SiteTransaction determineTeamTotalTransactionData(TeamTotalRecordEvent teamTotalRecordEvent,
			EventPackage eventPackage, AccountEvent accountEvent) throws BatchException {
		LOGGER.info("Entering determineTeamTotalTransactionData()");
		LOGGER.debug("TeamTotalRecordEvent: " + teamTotalRecordEvent);
		LOGGER.debug("EventPackage: " + eventPackage);
		LOGGER.debug("AccountEvent: " + accountEvent);

		final SiteWagers siteWagers = new SiteWagers();
		siteWagers.setProcessSite(this);
		siteWagers.setAccountEvent(accountEvent);
		SiteTransaction siteTransaction = null;

		try {
			siteTransaction = siteWagers.setupTeamTotals(teamTotalRecordEvent, eventPackage);
		} catch (BatchException be) {
			be.setHtml(gamesXhtml);
			accountEvent.setAccounthtml(gamesXhtml);
			throw be;
		}

		LOGGER.info("Exiting determineTeamTotalTransactionData()");
		return siteTransaction;
	}

	/**
	 * 
	 * @param siteTeamPackage
	 * @param teamTotalRecordEvent
	 * @param arrayNum
	 * @param juice
	 * @param accountEvent
	 * @return
	 * @throws BatchException newly added on 31-10-18
	 */
	public SiteTransaction createTeamTotalTransaction(SiteTeamPackage siteTeamPackage,
			TeamTotalRecordEvent teamTotalRecordEvent, int arrayNum, float juice, AccountEvent accountEvent)
			throws BatchException {
		LOGGER.info("Entering createTeamTotalTransaction()");
		LOGGER.debug("TeamTotalRecordEvent: " + teamTotalRecordEvent);
		LOGGER.debug("SiteTeamPackage: " + siteTeamPackage);
		LOGGER.debug("arrayNum: " + arrayNum);
		LOGGER.debug("juice: " + juice);
		LOGGER.debug("AccountEvent: " + accountEvent);

		final SiteTransaction siteTransaction = createSiteTransaction();
		final String overunder = teamTotalRecordEvent.getTeamtotaloverunder();

		if (overunder.equals("o")) {
			siteTransaction
					.setOptionValue(siteTeamPackage.getGameTeamTotalOptionValue().get(Integer.toString(arrayNum)));
			siteTransaction.setSelectName(siteTeamPackage.getGameTeamTotalSelectName());
			siteTransaction.setInputName(siteTeamPackage.getGameTeamTotalInputName());
			siteTransaction.setSelectId(siteTeamPackage.getGameTeamTotalSelectId());
			siteTransaction.setInputId(siteTeamPackage.getGameTeamTotalInputId());
			siteTransaction.setInputValue(siteTeamPackage.getGameTeamTotalInputValue());
		} else if (overunder.equals("u")) {
			siteTransaction.setOptionValue(
					siteTeamPackage.getGameTeamTotalOptionValueSecond().get(Integer.toString(arrayNum)));
			siteTransaction.setSelectName(siteTeamPackage.getGameTeamTotalSelectNameSecond());
			siteTransaction.setInputName(siteTeamPackage.getGameTeamTotalInputNameSecond());
			siteTransaction.setSelectId(siteTeamPackage.getGameTeamTotalSelectIdSecond());
			siteTransaction.setInputId(siteTeamPackage.getGameTeamTotalInputIdSecond());
			siteTransaction.setInputValue(siteTeamPackage.getGameTeamTotalInputValueSecond());
		}
		// Set the amount
		final Double newAmount = amountToApply(juice, teamTotalRecordEvent.getWtype(), teamTotalRecordEvent.getAmount(),
				accountEvent.getMaxteamtotalamount(), accountEvent);
		LOGGER.debug("newAmount: " + newAmount);
		siteTransaction.setAmount(Double.toString(newAmount));
		accountEvent.setAmount(teamTotalRecordEvent.getAmount());
		accountEvent.setActualamount(Double.toString(newAmount));

		LOGGER.info("Exiting createTeamTotalTransaction()");
		return siteTransaction;
	}

	/**
	 * 
	 * @param sportType
	 * @param xhtml
	 * @return
	 * @throws BatchException
	 */
	protected Map<String, String> getMenuFromSite(String sportType, String xhtml) throws BatchException {
		LOGGER.info("Entering getMenuFromSite()");
		LOGGER.debug("sportType: " + sportType);
		LOGGER.debug("xhtml: " + xhtml);
		String labels[] = null;
		String sport[] = null;

		if (sportType != null && sportType.length() > 0) {
			if ("nfllines".equals(sportType)) {
				labels = NFL_LINES_NAME;
				sport = NFL_LINES_SPORT;
			} else if ("nflfirst".equals(sportType)) {
				labels = NFL_FIRST_NAME;
				sport = NFL_FIRST_SPORT;
			} else if ("nflsecond".equals(sportType)) {
				labels = NFL_SECOND_NAME;
				sport = NFL_SECOND_SPORT;
			} else if ("ncaaflines".equals(sportType)) {
				labels = NCAAF_LINES_NAME;
				sport = NCAAF_LINES_SPORT;
			} else if ("ncaaffirst".equals(sportType)) {
				labels = NCAAF_FIRST_NAME;
				sport = NCAAF_FIRST_SPORT;
			} else if ("ncaafsecond".equals(sportType)) {
				labels = NCAAF_SECOND_NAME;
				sport = NCAAF_SECOND_SPORT;
			} else if ("nbalines".equals(sportType)) {
				labels = NBA_LINES_NAME;
				sport = NBA_LINES_SPORT;
			} else if ("nbafirst".equals(sportType)) {
				labels = NBA_FIRST_NAME;
				sport = NBA_FIRST_SPORT;
			} else if ("nbasecond".equals(sportType)) {
				labels = NBA_SECOND_NAME;
				sport = NBA_SECOND_SPORT;
			} else if ("ncaablines".equals(sportType)) {
				labels = NCAAB_LINES_NAME;
				sport = NCAAB_LINES_SPORT;
			} else if ("ncaabfirst".equals(sportType)) {
				labels = NCAAB_FIRST_NAME;
				sport = NCAAB_FIRST_SPORT;
			} else if ("ncaabsecond".equals(sportType)) {
				labels = NCAAB_SECOND_NAME;
				sport = NCAAB_SECOND_SPORT;
			} else if ("nhllines".equals(sportType)) {
				labels = NHL_LINES_NAME;
				sport = NHL_LINES_SPORT;
			} else if ("nhlfirst".equals(sportType)) {
				labels = NHL_FIRST_NAME;
				sport = NHL_FIRST_SPORT;
			} else if ("nhlsecond".equals(sportType)) {
				labels = NHL_SECOND_NAME;
				sport = NHL_SECOND_SPORT;
			} else if ("nhlthird".equals(sportType)) {
				labels = NHL_THIRD_NAME;
				sport = NHL_THIRD_SPORT;
			} else if ("wnbalines".equals(sportType)) {
				labels = WNBA_LINES_NAME;
				sport = WNBA_LINES_SPORT;
			} else if ("wnbafirst".equals(sportType)) {
				labels = WNBA_FIRST_NAME;
				sport = WNBA_FIRST_SPORT;
			} else if ("wnbasecond".equals(sportType)) {
				labels = WNBA_SECOND_NAME;
				sport = WNBA_SECOND_SPORT;
			} else if ("mlblines".equals(sportType)) {
				labels = MLB_LINES_NAME;
				sport = MLB_LINES_SPORT;
			} else if ("mlbfirst".equals(sportType)) {
				labels = MLB_FIRST_NAME;
				sport = MLB_FIRST_SPORT;
			} else if ("mlbsecond".equals(sportType)) {
				labels = MLB_SECOND_NAME;
				sport = MLB_SECOND_SPORT;
			} else if ("mlbthird".equals(sportType)) {
				labels = MLB_THIRD_NAME;
				sport = MLB_THIRD_SPORT;
			} else if ("internationalbaseballlines".equals(sportType)) {
				labels = INTERNATIONAL_BASEBALL_LINES_NAME;
				sport = INTERNATIONAL_BASEBALL_LINES_SPORT;
			} else if ("internationalbaseballfirst".equals(sportType)) {
				labels = INTERNATIONAL_BASEBALL_FIRST_NAME;
				sport = INTERNATIONAL_BASEBALL_FIRST_SPORT;
			} else if ("internationalbaseballsecond".equals(sportType)) {
				labels = INTERNATIONAL_BASEBALL_SECOND_NAME;
				sport = INTERNATIONAL_BASEBALL_SECOND_SPORT;
			} else {
				throw new BatchException("Unsupported sportType");
			}
		}

		// Parse the menu to get correct sport
		MAP_DATA.clear();
		MAP_DATA = siteParser.parseMenu(xhtml, labels, sport);

		LOGGER.info("Exiting getMenuFromSite()");
		return MAP_DATA;
	}

	/**
	 * 
	 * @param siteTeamPackage
	 * @param spreadRecordEvent
	 * @param arrayNum
	 * @param juice
	 * @return
	 * @throws BatchException
	 */
	public SiteTransaction createSpreadTransaction(SiteTeamPackage siteTeamPackage, SpreadRecordEvent spreadRecordEvent,
			int arrayNum, float juice, AccountEvent accountEvent) throws BatchException {
		LOGGER.info("Entering createSpreadTransaction()");
		LOGGER.debug("SpreadRecordEvent: " + spreadRecordEvent);
		LOGGER.debug("SiteTeamPackage: " + siteTeamPackage);
		LOGGER.debug("arrayNum: " + arrayNum);
		LOGGER.debug("juice: " + juice);
		LOGGER.debug("AccountEvent: " + accountEvent);

		final SiteTransaction siteTransaction = createSiteTransaction();
		siteTransaction.setOptionValue(siteTeamPackage.getGameSpreadOptionValue().get(Integer.toString(arrayNum)));
		siteTransaction.setSelectName(siteTeamPackage.getGameSpreadSelectName());
		siteTransaction.setInputName(siteTeamPackage.getGameSpreadInputName());
		siteTransaction.setSelectId(siteTeamPackage.getGameSpreadSelectId());
		siteTransaction.setInputId(siteTeamPackage.getGameSpreadInputId());
		siteTransaction.setInputValue(siteTeamPackage.getGameSpreadInputValue());

		// Set the amount
		final Double newAmount = amountToApply(juice, spreadRecordEvent.getWtype(), spreadRecordEvent.getAmount(),
				accountEvent.getMaxspreadamount(), accountEvent);
		LOGGER.debug("newAmount: " + newAmount);
		siteTransaction.setAmount(Double.toString(newAmount));
		accountEvent.setAmount(spreadRecordEvent.getAmount());
		accountEvent.setActualamount(Double.toString(newAmount));

		LOGGER.info("Exiting createSpreadTransaction()");
		return siteTransaction;
	}

	/**
	 * 
	 * @param siteTeamPackage
	 * @param totalRecordEvent
	 * @param arrayNum
	 * @param juice
	 * @param accountEvent
	 * @return
	 * @throws BatchException
	 */
	public SiteTransaction createTotalTransaction(SiteTeamPackage siteTeamPackage, TotalRecordEvent totalRecordEvent,
			int arrayNum, float juice, AccountEvent accountEvent) throws BatchException {
		LOGGER.info("Entering createTotalTransaction()");
		LOGGER.debug("TotalRecordEvent: " + totalRecordEvent);
		LOGGER.debug("SiteTeamPackage: " + siteTeamPackage);
		LOGGER.debug("arrayNum: " + arrayNum);
		LOGGER.debug("juice: " + juice);
		LOGGER.debug("AccountEvent: " + accountEvent);

		final SiteTransaction siteTransaction = createSiteTransaction();
		siteTransaction.setOptionValue(siteTeamPackage.getGameTotalOptionValue().get(Integer.toString(arrayNum)));
		siteTransaction.setSelectName(siteTeamPackage.getGameTotalSelectName());
		siteTransaction.setInputName(siteTeamPackage.getGameTotalInputName());
		siteTransaction.setSelectId(siteTeamPackage.getGameTotalSelectId());
		siteTransaction.setInputId(siteTeamPackage.getGameTotalInputId());
		siteTransaction.setInputValue(siteTeamPackage.getGameTotalInputValue());

		// Set the amount
		final Double newAmount = amountToApply(juice, totalRecordEvent.getWtype(), totalRecordEvent.getAmount(),
				accountEvent.getMaxtotalamount(), accountEvent);
		LOGGER.debug("newAmount: " + newAmount);
		siteTransaction.setAmount(Double.toString(newAmount));
		accountEvent.setAmount(totalRecordEvent.getAmount());
		accountEvent.setActualamount(Double.toString(newAmount));

		LOGGER.info("Exiting createTotalTransaction()");
		return siteTransaction;
	}

	/**
	 * 
	 * @param siteTeamPackage
	 * @param mlRecordEvent
	 * @param arrayNum
	 * @param juice
	 * @param accountEvent
	 * @return
	 * @throws BatchException
	 */
	public SiteTransaction createMoneyLineTransaction(SiteTeamPackage siteTeamPackage, MlRecordEvent mlRecordEvent,
			int arrayNum, float juice, AccountEvent accountEvent) throws BatchException {
		LOGGER.info("Entering createMoneyLineTransaction()");
		LOGGER.debug("MlRecordEvent: " + mlRecordEvent);
		LOGGER.debug("SiteTeamPackage: " + siteTeamPackage);
		LOGGER.debug("arrayNum: " + arrayNum);
		LOGGER.debug("juice: " + juice);
		LOGGER.debug("AccountEvent: " + accountEvent);

		final SiteTransaction siteTransaction = createSiteTransaction();
		siteTransaction.setOptionValue(siteTeamPackage.getGameMLInputValue().get(Integer.toString(arrayNum)));
		siteTransaction.setInputName(siteTeamPackage.getGameMLInputName());
		siteTransaction.setInputId(siteTeamPackage.getGameMLInputId());
		siteTransaction.setInputValue(siteTeamPackage.getGameMLInputValue().get(Integer.toString(arrayNum)));

		// Set the amount
		final Double newAmount = amountToApply(juice, mlRecordEvent.getWtype(), mlRecordEvent.getAmount(),
				accountEvent.getMaxmlamount(), accountEvent);
		LOGGER.debug("newAmount: " + newAmount);
		siteTransaction.setAmount(Double.toString(newAmount));
		accountEvent.setAmount(mlRecordEvent.getAmount());
		accountEvent.setActualamount(Double.toString(newAmount));

		LOGGER.info("Exiting createMoneyLineTransaction()");
		return siteTransaction;
	}

	/**
	 * 
	 * @param spreadRecordEvent
	 * @param eventPackage
	 * @param accountEvent
	 * @return
	 * @throws BatchException
	 */
	protected SiteTransaction determineSpreadTransactionData(SpreadRecordEvent spreadRecordEvent,
			EventPackage eventPackage, AccountEvent accountEvent) throws BatchException {
		LOGGER.info("Entering determineSpreadTransactionData()");
		LOGGER.debug("SpreadRecordEvent: " + spreadRecordEvent);
		LOGGER.debug("EventPackage: " + eventPackage);
		LOGGER.debug("AccountEvent: " + accountEvent);

		final SiteWagers siteWagers = new SiteWagers();
		siteWagers.setProcessSite(this);
		siteWagers.setAccountEvent(accountEvent);
		SiteTransaction siteTransaction = null;
		try {
			siteTransaction = siteWagers.setupSpreads(spreadRecordEvent, eventPackage);
		} catch (BatchException be) {
			be.setHtml(gamesXhtml);
			accountEvent.setAccounthtml(gamesXhtml);
			throw be;
		}

		LOGGER.info("Exiting determineSpreadTransactionData()");
		return siteTransaction;
	}

	/**
	 * 
	 * @param totalRecordEvent
	 * @param eventPackage
	 * @param accountEvent
	 * @return
	 * @throws BatchException
	 */
	protected SiteTransaction determineTotalTransactionData(TotalRecordEvent totalRecordEvent,
			EventPackage eventPackage, AccountEvent accountEvent) throws BatchException {
		LOGGER.info("Entering determineTotalTransactionData()");
		LOGGER.debug("TotalRecordEvent: " + totalRecordEvent);
		LOGGER.debug("EventPackage: " + eventPackage);
		LOGGER.debug("AccountEvent: " + accountEvent);

		final SiteWagers siteWagers = new SiteWagers();
		siteWagers.setProcessSite(this);
		siteWagers.setAccountEvent(accountEvent);
		SiteTransaction siteTransaction = null;

		try {
			siteTransaction = siteWagers.setupTotals(totalRecordEvent, eventPackage);
		} catch (BatchException be) {
			be.setHtml(gamesXhtml);
			accountEvent.setAccounthtml(gamesXhtml);
			throw be;
		}

		LOGGER.info("Exiting determineTotalTransactionData()");
		return siteTransaction;
	}

	/**
	 * 
	 * @param mlRecordEvent
	 * @param eventPackage
	 * @param accountEvent
	 * @return
	 * @throws BatchException
	 */
	protected SiteTransaction determineMlTransactionData(MlRecordEvent mlRecordEvent, EventPackage eventPackage,
			AccountEvent accountEvent) throws BatchException {
		LOGGER.info("Entering determineMlTransactionData()");
		LOGGER.debug("MlRecordEvent: " + mlRecordEvent);
		LOGGER.debug("EventPackage: " + eventPackage);
		LOGGER.debug("AccountEvent: " + accountEvent);

		final SiteWagers siteWagers = new SiteWagers();
		siteWagers.setProcessSite(this);
		siteWagers.setAccountEvent(accountEvent);
		SiteTransaction siteTransaction = null;
		try {
			siteTransaction = siteWagers.setupMoneyLine(mlRecordEvent, eventPackage);
		} catch (BatchException be) {
			be.setHtml(gamesXhtml);
			accountEvent.setAccounthtml(gamesXhtml);
			throw be;
		}

		LOGGER.info("Exiting determineMlTransactionData()");
		return siteTransaction;
	}

	/**
	 * 
	 * @param juice
	 * @param wagerType
	 * @param amount
	 * @param maxWagerAmount
	 * @param accountEvent
	 * @return
	 */
	protected Double amountToApply(float juice, String wagerType, String amount, Integer maxWagerAmount,
			AccountEvent accountEvent) {
		LOGGER.info("Entering amountToApply()");
		LOGGER.debug("juice: " + juice);
		LOGGER.debug("wagerType: " + wagerType);
		LOGGER.debug("amount: " + amount);
		LOGGER.debug("maxWagerAmount: " + maxWagerAmount);

		// Set the default to the win or risk amount
		Double wagerAmount = Double.valueOf(amount);
		Double maximumWagerAmount = new Double(0);
		boolean isNegative = false;

		float factor = 1;
		if (juice < 0) {
			factor = 100 / juice;
			isNegative = true;
		} else {
			factor = juice / 100;
		}

		// Setup the maximum amount
		if (maxWagerAmount != null && maxWagerAmount.intValue() != 0) {
			maximumWagerAmount = new Double(maxWagerAmount);
		}

		Double theAmount = null;
		if (isNegative) {
			// Check on maximum amount
			if (wagerAmount > maximumWagerAmount) {
				wagerAmount = maximumWagerAmount;
			}

			theAmount = wagerAmount;

			// Setup the data on the backend
			final Double risk = wagerAmount / factor;
			Double riskAmount = round(risk.doubleValue(), 2);
			Double rAmount = Math.abs(riskAmount);
			accountEvent.setRiskamount(rAmount.toString());
			accountEvent.setTowinamount(wagerAmount.toString());
		} else {
			// Check on maximum amount
			if (wagerAmount > maximumWagerAmount) {
				wagerAmount = maximumWagerAmount;
			}

			// Setup the data on the backend
			final Double win = wagerAmount * factor;
			Double winAmount = round(win.doubleValue(), 2);
			theAmount = winAmount;
			accountEvent.setRiskamount(wagerAmount.toString());
			accountEvent.setTowinamount(winAmount.toString());
		}

		/*
		 * // Check on what type of wager if (wagerType != null && wagerType.length() >
		 * 0) { // Risk? if (("1".equals(wagerType) && isNegative)) { // Change the
		 * amount wagerAmount = wagerAmount * factor; }
		 * 
		 * // Check on maximum amount if (wagerAmount > maximumWagerAmount) {
		 * wagerAmount = maximumWagerAmount; }
		 * 
		 * // Setup the data on the backend final Double risk = wagerAmount / factor;
		 * Double riskAmount = round(risk.doubleValue(), 2);
		 * accountEvent.setRiskamount(riskAmount.toString());
		 * accountEvent.setTowinamount(wagerAmount.toString()); }
		 */
		// Now round the amount to 2 digit precision
		double amountApplied = round(theAmount.doubleValue(), 2);

		LOGGER.info("Exiting amountToApply()");
		return amountApplied;
	}

	/**
	 * 
	 * @param value
	 * @param places
	 * @return
	 */
	protected static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	/**
	 * 
	 * @param nameValuePairs
	 * @param map
	 * @param setOn
	 * @return
	 * @throws BatchException
	 */
	protected String setupNameValues(List<NameValuePair> nameValuePairs, Map<String, String> map, boolean setOn)
			throws BatchException {
		LOGGER.info("Entering setupMenuNameValues()");
		String actionLogin = null;

		// Check for a valid map
		if (map != null && !map.isEmpty()) {
			final Set<Entry<String, String>> indexs = map.entrySet();
			if (indexs != null && !indexs.isEmpty()) {
				final Iterator<Entry<String, String>> itr = indexs.iterator();
				while (itr != null && itr.hasNext()) {
					final Entry<String, String> values = itr.next();
					String key = values.getKey();
					String value = values.getValue();
					LOGGER.debug("KEY: " + key);
					LOGGER.debug("VALUE: " + value);

					if ("action".equals(key)) {
						actionLogin = setupAction(value, null);
					} else if (value != null && value.length() > 0) {
						nameValuePairs.add(new BasicNameValuePair(key, value));
					} else if (setOn) {
						nameValuePairs.add(new BasicNameValuePair(key, "on"));
					}
				}
			}
		}

		LOGGER.info("Exiting setupMenuNameValues()");
		return actionLogin;
	}

	/**
	 * 
	 * @param nameValuePairs
	 * @param map
	 * @param setOn
	 * @param webappName
	 * @return
	 * @throws BatchException
	 */
	protected String setupNameValuesWithName(List<NameValuePair> nameValuePairs, Map<String, String> map, boolean setOn,
			String webappName) throws BatchException {
		LOGGER.info("Entering setupNameValuesWithName()");
		String actionLogin = null;

		// Check for a valid map
		if (map != null && !map.isEmpty()) {
			final Set<Entry<String, String>> indexs = map.entrySet();
			if (indexs != null && !indexs.isEmpty()) {
				final Iterator<Entry<String, String>> itr = indexs.iterator();
				while (itr != null && itr.hasNext()) {
					final Entry<String, String> values = itr.next();
					String key = values.getKey();
					String value = values.getValue();
					LOGGER.debug("KEY: " + key);
					LOGGER.debug("VALUE: " + value);

					if ("action".equals(key)) {
						actionLogin = setupAction(value, webappName);
					} else if (value != null && value.length() > 0) {
						nameValuePairs.add(new BasicNameValuePair(key, value));
					} else if (setOn) {
						nameValuePairs.add(new BasicNameValuePair(key, "on"));
					}
				}
			}
		}

		LOGGER.info("Exiting setupNameValuesWithName()");
		return actionLogin;
	}

	/**
	 * 
	 * @param nameValuePairs
	 * @param map
	 * @param webappName
	 * @return
	 * @throws BatchException
	 */
	protected String setupNameValuesEmpty(List<NameValuePair> nameValuePairs, Map<String, String> map,
			String webappName) throws BatchException {
		LOGGER.info("Entering setupNameValuesEmpty()");
		LOGGER.debug("WebappName: " + webappName);
		String actionLogin = null;

		// Check for a valid map
		if (map != null && !map.isEmpty()) {
			final Set<Entry<String, String>> indexs = map.entrySet();
			if (indexs != null && !indexs.isEmpty()) {
				final Iterator<Entry<String, String>> itr = indexs.iterator();
				while (itr != null && itr.hasNext()) {
					final Entry<String, String> values = itr.next();
					String key = values.getKey();
					String value = values.getValue();
					LOGGER.debug("KEY: " + key);
					LOGGER.debug("VALUE: " + value);

					if ("action".equals(key)) {
						actionLogin = setupAction(value, webappName);
					} else if (value != null && value.length() > 0 && key != null && key.length() > 0) {
						nameValuePairs.add(new BasicNameValuePair(key, value));
					} else if (key != null && key.length() > 0) {
						nameValuePairs.add(new BasicNameValuePair(key, ""));
					}
				}
			}
		}

		LOGGER.info("Exiting setupNameValuesEmpty()");
		return actionLogin;
	}

	/**
	 * 
	 * @param nameValuePairs
	 * @param map
	 * @param siteTransaction
	 * @return
	 * @throws BatchException
	 */
	protected String setupWagerInfo(List<NameValuePair> nameValuePairs, Map<String, String> map,
			SiteTransaction siteTransaction) throws BatchException {
		LOGGER.info("Entering setupWagerInfo()");
		String actionLogin = null;

		// Check for a valid map
		if (map != null && !map.isEmpty()) {
			final Set<Entry<String, String>> indexs = map.entrySet();
			if (indexs != null && !indexs.isEmpty()) {
				final Iterator<Entry<String, String>> itr = indexs.iterator();
				while (itr != null && itr.hasNext()) {
					final Entry<String, String> values = itr.next();
					String key = values.getKey();
					String value = values.getValue();
					LOGGER.info("KEY: " + key);
					LOGGER.info("VALUE: " + value);

					// Setup data
					if ("action".equals(key)) {
						actionLogin = setupAction(value, null);
					} else if (value != null && value.length() > 0) {
						if (key.equals(siteTransaction.getInputName())) {
							nameValuePairs.add(new BasicNameValuePair(key, siteTransaction.getAmount()));
						} else if (key.equals(siteTransaction.getSelectName())) {
							nameValuePairs.add(new BasicNameValuePair(key, siteTransaction.getOptionValue()));
						} else {
							nameValuePairs.add(new BasicNameValuePair(key, value));
						}
					} else {
						if (key.equals(siteTransaction.getInputName())) {
							nameValuePairs.add(new BasicNameValuePair(key, siteTransaction.getAmount()));
						} else if (key.equals(siteTransaction.getSelectName())) {
							nameValuePairs.add(new BasicNameValuePair(key, siteTransaction.getOptionValue()));
						} else {
							nameValuePairs.add(new BasicNameValuePair(key, ""));
						}
					}
				}
			}
		}

		LOGGER.info("Exiting setupWagerInfo()");
		return actionLogin;
	}

	/**
	 * 
	 * @param xhtml
	 * @param captchaInfo
	 * @return
	 */
	protected boolean checkForCaptcha(String xhtml, String captchaInfo) {
		LOGGER.info("Entering checkForCaptcha()");
		boolean foundCaptcha = false;

		if (xhtml != null && xhtml.length() > 0 && xhtml.contains(captchaInfo)) {
			foundCaptcha = true;
		}

		LOGGER.info("Exiting checkForCaptcha()");
		return foundCaptcha;
	}

	/**
	 * 
	 * @param xhtmlData
	 * @param postUrl
	 * @param postName
	 * @param headerValuePairs
	 * @param postValuePairs
	 * @param verifyUrl
	 * @param captchaUrl
	 * @return
	 * @throws BatchException
	 */
	protected String processCaptcha(String xhtmlData, String postUrl, String postName,
			List<NameValuePair> headerValuePairs, List<NameValuePair> postValuePairs, String verifyUrl,
			String captchaUrl) throws BatchException {
		LOGGER.info("Entering processCaptcha()");

		final CaptchaWorker captchaWorker = new CaptchaWorker(this.httpClientWrapper);
		final String xhtml = captchaWorker.processCaptcha(xhtmlData, postUrl, postName, headerValuePairs,
				postValuePairs, verifyUrl, captchaUrl, this.httpClientWrapper.getPreviousUrl());
		LOGGER.debug("XHTML: " + xhtml);

		LOGGER.info("Entering processCaptcha()");
		return xhtml;
	}

	/**
	 * 
	 * @param headerValuePairs
	 * @param verifyUrl
	 * @param captchaUrl
	 * @param referer
	 * @param number
	 * @return
	 * @throws BatchException
	 */
	protected String getCaptcha(List<NameValuePair> headerValuePairs, String verifyUrl, String captchaUrl,
			String referer, String number) throws BatchException {
		LOGGER.info("Entering getCaptcha()");

		final CaptchaWorker captchaWorker = new CaptchaWorker(this.httpClientWrapper);
		final String xhtml = captchaWorker.getCaptcha(headerValuePairs, verifyUrl, captchaUrl, referer, number);
		LOGGER.debug("XHTML: " + xhtml);

		LOGGER.info("Entering getCaptcha()");
		return xhtml;

	}

	/**
	 * 
	 * @param withContentType
	 * @return
	 */
	protected List<NameValuePair> setupHeader(boolean withContentType) {
		LOGGER.info("Entering setupHeader()");

		final List<NameValuePair> headerValuePairs = new ArrayList<NameValuePair>(1);
		if (withContentType) {
			headerValuePairs.add(new BasicNameValuePair("Content-Type", "application/x-www-form-urlencoded"));
		}
		headerValuePairs.add(new BasicNameValuePair("Upgrade-Insecure-Requests", "1"));
		headerValuePairs.add(new BasicNameValuePair("Referer", this.httpClientWrapper.getPreviousUrl()));

		LOGGER.info("Exiting setupHeader()");
		return headerValuePairs;
	}

	/**
	 * 
	 * @param retValue
	 * @param redirectName
	 * @return
	 */
	protected boolean isRedirect(List<NameValuePair> retValue, String redirectName) {
		LOGGER.info("Entering isRedirect()");
		LOGGER.debug("redirectName: " + redirectName);
		boolean redirect = false;

		// Check for value data
		if (retValue != null && !retValue.isEmpty()) {
			final Iterator<NameValuePair> itr = retValue.iterator();
			while (itr != null && itr.hasNext()) {
				final NameValuePair nvp = itr.next();
				String name = nvp.getName();
				String value = nvp.getValue();
				if ("Location".equals(name) && value != null && value.length() > 0) {
					if (value.contains(redirectName)) {
						redirect = true;
					}
				}
			}
		}

		LOGGER.info("Entering isRedirect()");
		return redirect;
	}

	/**
	 * 
	 * @param actionLogin
	 * @param postValuePairs
	 * @return
	 * @throws BatchException
	 */
	protected String authenticate(String actionLogin, List<NameValuePair> postValuePairs) throws BatchException {
		LOGGER.info("Entering authenticate()");
		LOGGER.debug("actionLogin: " + actionLogin);
		LOGGER.debug("postValuePairs: " + postValuePairs);

		// Call the login
		List<NameValuePair> retValue = httpClientWrapper.authenticateSite(actionLogin, postValuePairs,
				setupHeader(true));
		String xhtml = getCookiesAndXhtml(retValue);
		LOGGER.debug("XHTML: " + xhtml);

		retValue = httpClientWrapper.checkForRedirect(retValue, httpClientWrapper.getHost(), setupHeader(false), null,
				httpClientWrapper.getWebappname() + "/");
		xhtml = httpClientWrapper.getCookiesAndXhtml(retValue);

		LOGGER.info("Exiting authenticate()");
		return xhtml;
	}

	/**
	 * 
	 * @param actionLogin
	 * @param postValuePairs
	 * @return
	 * @throws BatchException
	 */
	protected String authenticateNoBr(String actionLogin, List<NameValuePair> postValuePairs) throws BatchException {
		LOGGER.info("Entering authenticate()");
		LOGGER.debug("actionLogin: " + actionLogin);
		LOGGER.debug("postValuePairs: " + postValuePairs);

		// Call the login
		List<NameValuePair> retValue = httpClientWrapper.authenticateSiteNoBr(actionLogin, postValuePairs, setupHeader(true));
		String xhtml = getCookiesAndXhtml(retValue);
		LOGGER.debug("XHTML: " + xhtml);

		retValue = httpClientWrapper.checkForRedirectNoBr(retValue, httpClientWrapper.getHost(), setupHeader(false), null, httpClientWrapper.getWebappname() + "/");
		xhtml = httpClientWrapper.getCookiesAndXhtml(retValue);

		LOGGER.info("Exiting authenticate()");
		return xhtml;
	}

	/**
	 * 
	 * @param actionLogin
	 * @param postValuePairs
	 * @return
	 * @throws BatchException
	 */
	protected String authenticateMobile(String actionLogin, List<NameValuePair> postValuePairs) throws BatchException {
		LOGGER.info("Entering authenticateMobile()");
		LOGGER.debug("actionLogin: " + actionLogin);
		LOGGER.debug("postValuePairs: " + postValuePairs);

		// Call the login
		List<NameValuePair> retValue = httpClientWrapper.authenticateSite(actionLogin, postValuePairs,
				setupHeader(true));
		String xhtml = getCookiesAndXhtml(retValue);
		LOGGER.debug("XHTML: " + xhtml);

		retValue = httpClientWrapper.checkForRedirect(retValue, httpClientWrapper.getHost(), setupHeader(false), null,
				httpClientWrapper.getWebappname() + "/");
		xhtml = httpClientWrapper.getCookiesAndXhtml(retValue);

		LOGGER.info("Exiting authenticateMobile()");
		return xhtml;
	}

	/**
	 * 
	 * @param actionLogin
	 * @param cookieValue
	 * @param postValuePairs
	 * @return
	 * @throws BatchException
	 */
	protected List<NameValuePair> authenticateSiteNoRedirect(String actionLogin, String cookieValue,
			List<NameValuePair> postValuePairs) throws BatchException {
		LOGGER.info("Entering authenticateSiteNoRedirect()");
		LOGGER.debug("actionLogin: " + actionLogin);
		LOGGER.debug("postValuePairs: " + postValuePairs);

		// Call the login
		final List<NameValuePair> retValue = httpClientWrapper.authenticateSiteNoRedirect(actionLogin, cookieValue,
				postValuePairs, setupHeader(true));

		LOGGER.info("Exiting authenticateSiteNoRedirect()");
		return retValue;
	}

	/**
	 * 
	 * @param actionName
	 * @param postNameValuePairs
	 * @return
	 * @throws BatchException
	 */
	protected String postSite(String actionName, List<NameValuePair> postNameValuePairs) throws BatchException {
		LOGGER.info("Entering postSite()");
		LOGGER.debug("actionName: " + actionName);
		LOGGER.debug("postNameValuePairs: " + postNameValuePairs);

		// Send HTTP POST
		List<NameValuePair> retValue = httpClientWrapper.postSitePage(actionName, null, postNameValuePairs,
				setupHeader(true));
		String xhtml = getCookiesAndXhtml(retValue);
		LOGGER.debug("XHTML: " + xhtml);

		// Get webapp name
		String webappName = httpClientWrapper.getWebappname();
		if (webappName == null) {
			webappName = "";
		}

		// Check for redirect
		retValue = httpClientWrapper.checkForRedirect(retValue, httpClientWrapper.getHost(), setupHeader(false), null,
				webappName + "/");
		xhtml = httpClientWrapper.getCookiesAndXhtml(retValue);

		LOGGER.info("Exiting postSite()");
		return xhtml;
	}

	/**
	 * 
	 * @param actionName
	 * @param postNameValuePairs
	 * @return
	 * @throws BatchException
	 */
	protected String postSiteNoBr(String actionName, List<NameValuePair> postNameValuePairs) throws BatchException {
		LOGGER.info("Entering postSiteNoBr()");
		LOGGER.debug("actionName: " + actionName);
		LOGGER.debug("postNameValuePairs: " + postNameValuePairs);

		// Send HTTP POST
		List<NameValuePair> retValue = httpClientWrapper.postSitePageNoBr(actionName, null, postNameValuePairs,
				setupHeader(true));
		String xhtml = getCookiesAndXhtml(retValue);
		LOGGER.debug("XHTML: " + xhtml);

		// Get webapp name
		String webappName = httpClientWrapper.getWebappname();
		if (webappName == null) {
			webappName = "";
		}

		// Check for redirect
		retValue = httpClientWrapper.checkForRedirectNoBr(retValue, httpClientWrapper.getHost(), setupHeader(false), null,
				webappName + "/");
		xhtml = httpClientWrapper.getCookiesAndXhtml(retValue);

		LOGGER.info("Exiting postSiteNoBr()");
		return xhtml;
	}

	/**
	 * 
	 * @param actionName
	 * @param postNameValuePairs
	 * @return
	 * @throws BatchException
	 */
	protected String postMobileSite(String actionName, List<NameValuePair> postNameValuePairs) throws BatchException {
		LOGGER.info("Entering postMobileSite()");
		LOGGER.debug("actionName: " + actionName);
		LOGGER.debug("postNameValuePairs: " + postNameValuePairs);

		// Send HTTP POST
		List<NameValuePair> retValue = httpClientWrapper.postSitePage(actionName, null, postNameValuePairs,
				setupHeader(true));
		String xhtml = getCookiesAndXhtml(retValue);
		LOGGER.debug("XHTML: " + xhtml);

		// Get webapp name
		String webappName = httpClientWrapper.getWebappname();
		if (webappName == null) {
			webappName = "";
		}

		// Check for redirect
		retValue = httpClientWrapper.checkForRedirect(retValue, httpClientWrapper.getHost(), setupHeader(false), null,
				webappName + "/");
		xhtml = httpClientWrapper.getCookiesAndXhtml(retValue);

		LOGGER.info("Exiting postMobileSite()");
		return xhtml;
	}

	/**
	 * 
	 * @param actionName
	 * @param postNameValuePairs
	 * @return
	 * @throws BatchException
	 */
	protected String postSiteWithCheck(String actionName, List<NameValuePair> postNameValuePairs, String locationName)
			throws BatchException {
		LOGGER.info("Entering postSiteWithCheck()");
		LOGGER.debug("actionName: " + actionName);
		LOGGER.debug("postNameValuePairs: " + postNameValuePairs);
		LOGGER.debug("locationName: " + locationName);

		// Send HTTP POST
		List<NameValuePair> retValue = httpClientWrapper.postSitePage(actionName, null, postNameValuePairs,
				setupHeader(true));
		String xhtml = getCookiesAndXhtml(retValue);
		LOGGER.debug("XHTML: " + xhtml);

		// Get webapp name
		String webappName = httpClientWrapper.getWebappname();
		if (webappName == null) {
			webappName = "";
		}

		// Check for redirect
		retValue = httpClientWrapper.checkForRedirectWithLocation(retValue, httpClientWrapper.getHost(),
				setupHeader(false), null, webappName + "/", locationName, xhtml);
		xhtml = httpClientWrapper.getCookiesAndXhtml(retValue);

		LOGGER.info("Exiting postSiteWithCheck()");
		return xhtml;
	}

	/**
	 * 
	 * @param actionName
	 * @param postNameValuePairs
	 * @return
	 * @throws BatchException
	 */
	protected String postSiteWithCheckNoBr(String actionName, List<NameValuePair> postNameValuePairs, String locationName)
			throws BatchException {
		LOGGER.info("Entering postSiteWithCheckNoBr()");
		LOGGER.debug("actionName: " + actionName);
		LOGGER.debug("postNameValuePairs: " + postNameValuePairs);
		LOGGER.debug("locationName: " + locationName);

		// Send HTTP POST
		List<NameValuePair> retValue = httpClientWrapper.postSitePageNoBr(actionName, null, postNameValuePairs,
				setupHeader(true));
		String xhtml = getCookiesAndXhtml(retValue);
		LOGGER.debug("XHTML: " + xhtml);

		// Get webapp name
		String webappName = httpClientWrapper.getWebappname();
		if (webappName == null) {
			webappName = "";
		}

		// Check for redirect
		retValue = httpClientWrapper.checkForRedirectWithLocationNoBr(retValue, httpClientWrapper.getHost(),
				setupHeader(false), null, webappName + "/", locationName, xhtml);
		xhtml = httpClientWrapper.getCookiesAndXhtml(retValue);

		LOGGER.info("Exiting postSiteWithCheckNoBr()");
		return xhtml;
	}

	/**
	 * 
	 * @param actionName
	 * @param postNameValuePairs
	 * @param locationName
	 * @return
	 * @throws BatchException
	 */
	protected String postMobileSiteWithCheck(String actionName, List<NameValuePair> postNameValuePairs,
			String locationName) throws BatchException {
		LOGGER.info("Entering postMobileSiteWithCheck()");
		LOGGER.debug("actionName: " + actionName);
		LOGGER.debug("postNameValuePairs: " + postNameValuePairs);
		LOGGER.debug("locationName: " + locationName);

		// Send HTTP POST
		List<NameValuePair> retValue = httpClientWrapper.postSitePage(actionName, null, postNameValuePairs,
				setupHeader(true));
		String xhtml = getCookiesAndXhtml(retValue);
		LOGGER.debug("XHTML: " + xhtml);

		// Get webapp name
		String webappName = httpClientWrapper.getWebappname();
		if (webappName == null) {
			webappName = "";
		}

		// Check for redirect
		retValue = httpClientWrapper.checkForRedirectWithLocation(retValue, httpClientWrapper.getHost(),
				setupHeader(false), null, webappName + "/", locationName, xhtml);
		xhtml = httpClientWrapper.getCookiesAndXhtml(retValue);

		LOGGER.info("Exiting postMobileSiteWithCheck()");
		return xhtml;
	}

	/**
	 * 
	 * @param actionName
	 * @return
	 * @throws BatchException
	 */
	protected String getSite(String actionName) throws BatchException {
		LOGGER.info("Entering getSite()");
		LOGGER.debug("actionName: " + actionName);
		String xhtml = null;

		try {
			List<NameValuePair> retValue = httpClientWrapper.getSitePage(actionName, null, setupHeader(false));
			xhtml = httpClientWrapper.getCookiesAndXhtml(retValue);
	
			// Get webapp name
			String webappName = httpClientWrapper.getWebappname();
			if (webappName == null) {
				webappName = "";
			}
	
			// Check for redirect
			retValue = httpClientWrapper.checkForRedirect(retValue, httpClientWrapper.getHost(), setupHeader(false), null, webappName + "/");
			xhtml = httpClientWrapper.getCookiesAndXhtml(retValue);
		} catch (BatchException be) {
			throw be;
		}

		LOGGER.info("Exiting getSite()");
		return xhtml;
	}

	/**
	 * 
	 * @param actionName
	 * @return
	 * @throws BatchException
	 */
	protected String getSiteForEspn(String actionName) throws BatchException {
		LOGGER.info("Entering getSiteForEspn()");
		LOGGER.debug("actionName: " + actionName);
		String xhtml = null;

		try {
			List<NameValuePair> retValue = httpClientWrapper.getSitePageForEspn(actionName, null, setupHeader(false));
			xhtml = httpClientWrapper.getCookiesAndXhtml(retValue);
	
			// Get webapp name
			String webappName = httpClientWrapper.getWebappname();
			if (webappName == null) {
				webappName = "";
			}
	
			// Check for redirect
			retValue = httpClientWrapper.checkForRedirect(retValue, httpClientWrapper.getHost(), setupHeader(false), null, webappName + "/");
			xhtml = httpClientWrapper.getCookiesAndXhtml(retValue);
		} catch (BatchException be) {
			throw be;
		}

		LOGGER.info("Exiting getSiteForEspn()");
		return xhtml;
	}

	/**
	 * 
	 * @param actionName
	 * @return
	 * @throws BatchException
	 */
	protected String getSiteNoBr(String actionName) throws BatchException {
		LOGGER.info("Entering getSiteNoBr()");
		LOGGER.debug("actionName: " + actionName);

		List<NameValuePair> retValue = httpClientWrapper.getSitePageNoBr(actionName, null, setupHeader(false));
		String xhtml = httpClientWrapper.getCookiesAndXhtml(retValue);

		// Get webapp name
		String webappName = httpClientWrapper.getWebappname();
		if (webappName == null) {
			webappName = "";
		}

		// Check for redirect
		retValue = httpClientWrapper.checkForRedirectNoBr(retValue, httpClientWrapper.getHost(), setupHeader(false), null,
				webappName + "/");
		xhtml = httpClientWrapper.getCookiesAndXhtml(retValue);

		LOGGER.info("Exiting getSiteNoBr()");
		return xhtml;
	}

	/**
	 * 
	 * @param actionName
	 * @return
	 * @throws BatchException
	 */
	protected String getMobileSite(String actionName) throws BatchException {
		LOGGER.info("Entering getMobileSite()");
		LOGGER.debug("actionName: " + actionName);

		List<NameValuePair> retValue = httpClientWrapper.getSitePage(actionName, null, setupHeader(false));
		String xhtml = httpClientWrapper.getCookiesAndXhtml(retValue);
		// LOGGER.debug("XHTML: " + xhtml);

		// Get webapp name
		String webappName = httpClientWrapper.getWebappname();
		if (webappName == null) {
			webappName = "";
		}

		// Check for redirect
		retValue = httpClientWrapper.checkForRedirect(retValue, httpClientWrapper.getHost(), setupHeader(false), null,
				webappName + "/");
		xhtml = httpClientWrapper.getCookiesAndXhtml(retValue);

		LOGGER.info("Exiting getMobileSite()");
		return xhtml;
	}

	/**
	 * 
	 * @param sre
	 * @param ae
	 * @param lineChanges
	 * @return
	 */
	protected boolean determineSpreadLineChange(SpreadRecordEvent sre, AccountEvent ae,
			Map<String, String> lineChanges) {
		LOGGER.info("Entering determineSpreadLineChange()");
		boolean retValue = false;

		// Check which spread it is
		if (sre.getSpreadplusminusfirstone() != null && sre.getSpreadplusminusfirstone().length() > 0
				&& sre.getSpreadinputfirstone() != null && sre.getSpreadinputfirstone().length() > 0) {
			final float spread = Float.parseFloat(sre.getSpreadplusminusfirstone() + sre.getSpreadinputfirstone());
			final float spreadJuice = Float
					.parseFloat(sre.getSpreadjuiceplusminusfirstone() + sre.getSpreadinputjuicefirstone());
			final float spreadChange = Float.parseFloat(lineChanges.get("valueindicator") + lineChanges.get("value"));
			final float spreadJuiceChange = Float
					.parseFloat(lineChanges.get("juiceindicator") + lineChanges.get("juice"));

			retValue = spreadCheck(spread, spreadJuice, spreadChange, spreadJuiceChange);

			if (retValue) {
				ae.setSpread(spreadChange);
				ae.setSpreadindicator(lineChanges.get("valueindicator"));
				ae.setSpreadjuice(spreadJuiceChange);
			}
		} else if (sre.getSpreadplusminussecondone() != null && sre.getSpreadplusminussecondone().length() > 0
				&& sre.getSpreadinputsecondone() != null && sre.getSpreadinputsecondone().length() > 0) {
			final float spread = Float.parseFloat(sre.getSpreadplusminussecondone() + sre.getSpreadinputsecondone());
			final float spreadJuice = Float
					.parseFloat(sre.getSpreadjuiceplusminussecondone() + sre.getSpreadinputjuicesecondone());
			final float spreadChange = Float.parseFloat(lineChanges.get("valueindicator") + lineChanges.get("value"));
			final float spreadJuiceChange = Float
					.parseFloat(lineChanges.get("juiceindicator") + lineChanges.get("juice"));

			retValue = spreadCheck(spread, spreadJuice, spreadChange, spreadJuiceChange);

			if (retValue) {
				ae.setSpread(spreadChange);
				ae.setSpreadindicator(lineChanges.get("valueindicator"));
				ae.setSpreadjuice(spreadJuiceChange);
			}
		}

		LOGGER.info("Exiting determineSpreadLineChange()");
		return retValue;
	}

	/**
	 * 
	 * @param tre
	 * @param ae
	 * @param lineChanges
	 * @return
	 */
	protected boolean determineTotalLineChange(TotalRecordEvent tre, AccountEvent ae, Map<String, String> lineChanges) {
		LOGGER.info("Entering determineTotalLineChange()");
		boolean retValue = false;

		if (tre.getTotalinputfirstone() != null && tre.getTotalinputfirstone().length() > 0) {
			final float total = Float.parseFloat(tre.getTotalinputfirstone());
			final float totalJuice = Float
					.parseFloat(tre.getTotaljuiceplusminusfirstone() + tre.getTotalinputjuicefirstone());
			final float totalChange = Float.parseFloat(lineChanges.get("value"));
			final float totalJuiceChange = Float
					.parseFloat(lineChanges.get("juiceindicator") + lineChanges.get("juice"));

			retValue = totalOverCheck(total, totalJuice, totalChange, totalJuiceChange);

			if (retValue) {
				ae.setTotal(totalChange);
				ae.setTotalindicator(lineChanges.get("valueindicator"));
				ae.setTotaljuice(totalJuiceChange);
			}
		} else if (tre.getTotalinputsecondone() != null && tre.getTotalinputsecondone().length() > 0) {
			final float total = Float.parseFloat(tre.getTotalinputsecondone());
			final float totalJuice = Float
					.parseFloat(tre.getTotaljuiceplusminussecondone() + tre.getTotalinputjuicesecondone());
			final float totalChange = Float.parseFloat(lineChanges.get("value"));
			final float totalJuiceChange = Float
					.parseFloat(lineChanges.get("juiceindicator") + lineChanges.get("juice"));

			retValue = totalUnderCheck(total, totalJuice, totalChange, totalJuiceChange);

			if (retValue) {
				ae.setTotal(totalChange);
				ae.setTotalindicator(lineChanges.get("valueindicator"));
				ae.setTotaljuice(totalJuiceChange);
			}
		}

		LOGGER.info("Exiting determineTotalLineChange()");
		return retValue;
	}

	/**
	 * 
	 * @param mre
	 * @param ae
	 * @param lineChanges
	 * @return
	 */
	protected boolean determineMlLineChange(MlRecordEvent mre, AccountEvent ae, Map<String, String> lineChanges) {
		LOGGER.info("Entering determineMlLineChange()");
		boolean retValue = false;

		if (mre.getMlinputfirstone() != null && mre.getMlinputfirstone().length() > 0) {
			final float mlJuice = Float.parseFloat(mre.getMlplusminusfirstone() + mre.getMlinputfirstone());
			final float mlJuiceChange = Float.parseFloat(lineChanges.get("juiceindicator") + lineChanges.get("juice"));

			retValue = mlCheck(mlJuice, mlJuiceChange);

			if (retValue) {
				ae.setMlindicator(lineChanges.get("valueindicator"));
				ae.setMl(mlJuiceChange);
			}
		} else if (mre.getMlinputsecondone() != null && mre.getMlinputsecondone().length() > 0) {
			final float mlJuice = Float.parseFloat(mre.getMlplusminussecondone() + mre.getMlinputsecondone());
			final float mlJuiceChange = Float.parseFloat(lineChanges.get("juiceindicator") + lineChanges.get("juice"));

			retValue = mlCheck(mlJuice, mlJuiceChange);

			if (retValue) {
				ae.setMlindicator(lineChanges.get("valueindicator"));
				ae.setMl(mlJuiceChange);
			}
		}

		LOGGER.info("Exiting determineTotalLineChange()");
		return retValue;
	}

	/**
	 * 
	 * @param spread
	 * @param spreadJuice
	 * @param spreadChange
	 * @param spreadJuiceChange
	 * @return
	 */
	protected boolean spreadCheck(float spread, float spreadJuice, float spreadChange, float spreadJuiceChange) {
		LOGGER.info("Entering spreadOneCheck()");
		LOGGER.debug("spread: " + spread);
		LOGGER.debug("spreadJuice: " + spreadJuice);
		LOGGER.debug("spreadChange: " + spreadChange);
		LOGGER.debug("spreadJuiceChange: " + spreadJuiceChange);
		boolean retValue = false;

		// Spread less than new spread
		if (spread <= spreadChange) {
			if (spreadJuice <= spreadJuiceChange) {
				retValue = true;
			}
		}

		LOGGER.info("Exiting spreadOneCheck()");
		return retValue;
	}

	/**
	 * 
	 * @param total
	 * @param totalJuice
	 * @param totalChange
	 * @param totalJuiceChange
	 * @return
	 */
	protected boolean totalOverCheck(float total, float totalJuice, float totalChange, float totalJuiceChange) {
		LOGGER.info("Entering totalOverCheck()");
		LOGGER.debug("total: " + total);
		LOGGER.debug("totalJuice: " + totalJuice);
		LOGGER.debug("totalChange: " + totalChange);
		LOGGER.debug("totalJuiceChange: " + totalJuiceChange);
		boolean retValue = false;

		// Spread less than new spread
		if (total >= totalChange) {
			if (totalJuice <= totalJuiceChange) {
				retValue = true;
			}
		}

		LOGGER.info("Exiting totalOverCheck()");
		return retValue;
	}

	/**
	 * 
	 * @param total
	 * @param totalJuice
	 * @param totalChange
	 * @param totalJuiceChange
	 * @return
	 */
	protected boolean totalUnderCheck(float total, float totalJuice, float totalChange, float totalJuiceChange) {
		LOGGER.info("Entering totalUnderCheck()");
		LOGGER.debug("total: " + total);
		LOGGER.debug("totalJuice: " + totalJuice);
		LOGGER.debug("totalChange: " + totalChange);
		LOGGER.debug("totalJuiceChange: " + totalJuiceChange);
		boolean retValue = false;

		// Spread less than new spread
		if (total <= totalChange) {
			if (totalJuice <= totalJuiceChange) {
				retValue = true;
			}
		}

		LOGGER.info("Exiting totalUnderCheck()");
		return retValue;
	}

	/**
	 * 
	 * @param mlJuice
	 * @param mlJuiceChange
	 * @return
	 */
	protected boolean mlCheck(float mlJuice, float mlJuiceChange) {
		LOGGER.info("Entering mlCheck()");
		LOGGER.debug("mlJuice: " + mlJuice);
		LOGGER.debug("mlJuiceChange: " + mlJuiceChange);
		boolean retValue = false;

		// Spread less than new spread
		if (mlJuice <= mlJuiceChange) {
			retValue = true;
		}

		LOGGER.info("Exiting mlCheck()");
		return retValue;
	}

	/**
	 * 
	 * @param actionName
	 * @param locationName
	 * @return
	 * @throws BatchException
	 */
	protected String getSiteWithCheck(String actionName, String locationName) throws BatchException {
		LOGGER.info("Entering getSiteWithCheck()");
		LOGGER.debug("actionName: " + actionName);

		List<NameValuePair> retValue = httpClientWrapper.getSitePage(actionName, null, setupHeader(false));
		String xhtml = httpClientWrapper.getCookiesAndXhtml(retValue);
		LOGGER.debug("XHTML: " + xhtml);

		// Get webapp name
		String webappName = httpClientWrapper.getWebappname();
		if (webappName == null) {
			webappName = "";
		}

		// Check for redirect
		retValue = httpClientWrapper.checkForRedirectWithLocation(retValue, httpClientWrapper.getHost(),
				setupHeader(false), null, webappName + "/", locationName, xhtml);
		xhtml = httpClientWrapper.getCookiesAndXhtml(retValue);

		LOGGER.info("Exiting getSiteWithCheck()");
		return xhtml;
	}

	/**
	 * 
	 * @param actionName
	 * @param locationName
	 * @return
	 * @throws BatchException
	 */
	protected String getSiteWithCheckNoBr(String actionName, String locationName) throws BatchException {
		LOGGER.info("Entering getSiteWithCheckNoBr()");
		LOGGER.debug("actionName: " + actionName);

		List<NameValuePair> retValue = httpClientWrapper.getSitePageNoBr(actionName, null, setupHeader(false));
		String xhtml = httpClientWrapper.getCookiesAndXhtml(retValue);
		LOGGER.debug("XHTML: " + xhtml);

		// Get webapp name
		String webappName = httpClientWrapper.getWebappname();
		if (webappName == null) {
			webappName = "";
		}

		// Check for redirect
		retValue = httpClientWrapper.checkForRedirectWithLocation(retValue, httpClientWrapper.getHost(),
				setupHeader(false), null, webappName + "/", locationName, xhtml);
		xhtml = httpClientWrapper.getCookiesAndXhtml(retValue);

		LOGGER.info("Exiting getSiteWithCheckNoBr()");
		return xhtml;
	}

	/**
	 * 
	 * @param client
	 * @param url
	 * @param host
	 * @param cookieValue
	 * @param postValuePairs
	 * @param headerValuePairs
	 * @return
	 * @throws BatchException
	 */
	protected List<NameValuePair> postSitePage(HttpClient client, String url, String host, String cookieValue,
			List<NameValuePair> postValuePairs, List<NameValuePair> headerValuePairs) throws BatchException {
		return this.httpClientWrapper.postSitePage(url, cookieValue, postValuePairs, headerValuePairs);
	}

	/**
	 * 
	 * @param client
	 * @param url
	 * @param host
	 * @param cookieValue
	 * @param postValuePairs
	 * @param headerValuePairs
	 * @return
	 * @throws BatchException
	 */
	protected List<NameValuePair> postMobileSitePage(HttpClient client, String url, String host, String cookieValue,
			List<NameValuePair> postValuePairs, List<NameValuePair> headerValuePairs) throws BatchException {
		return this.httpClientWrapper.postSitePage(url, cookieValue, postValuePairs, headerValuePairs);
	}

	/**
	 * 
	 * @param client
	 * @param url
	 * @param host
	 * @param cookieValue
	 * @param headerValuePairs
	 * @return
	 * @throws BatchException
	 */
	protected List<NameValuePair> getSitePage(HttpClient client, String url, String host, String cookieValue,
			List<NameValuePair> headerValuePairs) throws BatchException {
		this.httpClientWrapper.setHost(host);
		return this.httpClientWrapper.getSitePage(url, cookieValue, headerValuePairs);
	}

	/**
	 * 
	 * @param client
	 * @param url
	 * @param host
	 * @param cookieValue
	 * @param headerValuePairs
	 * @return
	 * @throws BatchException
	 */
	protected List<NameValuePair> getMobileSitePage(HttpClient client, String url, String host, String cookieValue,
			List<NameValuePair> headerValuePairs) throws BatchException {
		return this.httpClientWrapper.getSitePage(url, cookieValue, headerValuePairs);
	}

	/**
	 * 
	 * @param client
	 * @param url
	 * @param host
	 * @param userLabel
	 * @param passLabel
	 * @param username
	 * @param password
	 * @return
	 * @exception BatchException
	 */
	protected List<NameValuePair> authenticateSite(HttpClient client, String url, String host,
			List<NameValuePair> postValuePairs, List<NameValuePair> headerValuePairs) throws BatchException {
		return this.httpClientWrapper.authenticateSite(url, postValuePairs, headerValuePairs);
	}

	/**
	 * 
	 * @param client
	 * @param url
	 * @param host
	 * @param postValuePairs
	 * @param headerValuePairs
	 * @return
	 * @throws BatchException
	 */
	protected List<NameValuePair> authenticateMobileSite(HttpClient client, String url, String host,
			List<NameValuePair> postValuePairs, List<NameValuePair> headerValuePairs) throws BatchException {
		return this.httpClientWrapper.authenticateSite(url, postValuePairs, headerValuePairs);
	}

	/**
	 * 
	 * @param url
	 * @param jsonString
	 * @param headerValuePairs
	 * @return
	 * @throws BatchException
	 */
	protected String postJSONSite(String url, String jsonString, List<NameValuePair> headerValuePairs)
			throws BatchException {
		final List<NameValuePair> retValue = this.httpClientWrapper.postJSONSite(url, null, jsonString,
				headerValuePairs);
		String json = httpClientWrapper.getCookiesAndXhtml(retValue);
		LOGGER.debug("JSON: " + json);

		return json;
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
	protected List<NameValuePair> postSitePageNoRedirect(String url, String cookieValue,
			List<NameValuePair> postValuePairs, List<NameValuePair> headerValuePairs) throws BatchException {
		final List<NameValuePair> retValue = this.httpClientWrapper.postSitePageNoRedirect(url, null, postValuePairs,
				headerValuePairs);

		return retValue;
	}

	/**
	 * 
	 * @param url
	 * @param jsonString
	 * @param headerValuePairs
	 * @return
	 * @throws BatchException
	 */
	protected List<NameValuePair> postJSONNoRedirectSite(String url, String jsonString,
			List<NameValuePair> headerValuePairs) throws BatchException {
		final List<NameValuePair> retValue = this.httpClientWrapper.postJSONNoRedirectSite(url, null, jsonString,
				headerValuePairs);

		return retValue;
	}

	/**
	 * 
	 * @param url
	 * @param headerValuePairs
	 * @return
	 * @throws BatchException
	 */
	protected String getJSONSite(String url, List<NameValuePair> headerValuePairs) throws BatchException {
		final List<NameValuePair> retValue = this.httpClientWrapper.getJSONSite(url, null, headerValuePairs);
		String json = httpClientWrapper.getCookiesAndXhtml(retValue);
		LOGGER.debug("JSON: " + json);

		return json;
	}

	/**
	 * 
	 * @param url
	 * @param headerValuePairs
	 * @return
	 * @throws BatchException
	 */
	protected String getJSSite(String url, List<NameValuePair> headerValuePairs) throws BatchException {
		final List<NameValuePair> retValue = this.httpClientWrapper.getJSSite(url, null, headerValuePairs);
		String js = httpClientWrapper.getCookiesAndXhtml(retValue);
		LOGGER.debug("JS: " + js);

		return js;
	}

	/**
	 * 
	 * @param url
	 * @param headerValuePairs
	 * @return
	 * @throws BatchException
	 */
	protected String getJSSiteNoBr(String url, List<NameValuePair> headerValuePairs) throws BatchException {
		final List<NameValuePair> retValue = this.httpClientWrapper.getSitePageNoBr(url, null, headerValuePairs);
		String js = httpClientWrapper.getCookiesAndXhtml(retValue);
		LOGGER.debug("JS: " + js);

		return js;
	}

	/**
	 * 
	 * @param retValue
	 * @return
	 */
	protected String getCookiesAndXhtml(List<NameValuePair> retValue) {
		return this.httpClientWrapper.getCookiesAndXhtml(retValue);
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
	protected List<NameValuePair> checkForRedirect(List<NameValuePair> retValue, String siteHost,
			List<NameValuePair> headerValuePairs, String urlSection, String prefix) throws BatchException {
		return this.httpClientWrapper.checkForRedirect(retValue, siteHost, headerValuePairs, urlSection, prefix);
	}

	/**
	 * 
	 * @param url
	 * @return
	 */
	protected String determineWebappName(String url) {
		return this.httpClientWrapper.determineWebappName(url);
	}

	/**
	 * 
	 * @param value
	 * @param webappName
	 * @return
	 * @throws BatchException
	 */
	protected String setupAction(String value, String webappName) throws BatchException {
		return this.httpClientWrapper.setupAction(value, webappName);
	}

	/**
	 * 
	 * @param action
	 * @return
	 */
	protected String populateUrl(String action) {
		return this.httpClientWrapper.populateUrl(action);
	}

	/**
	 * 
	 * @param duration
	 */
	protected void sleepAsUser(int duration) {
		try {
			// Sleep to make it more human like
			Thread.sleep(duration);
		} catch (InterruptedException ie) {
			LOGGER.error(ie);
		}
	}

	/**
	 * 
	 * @param juiceIndicator
	 * @param juiceNumber
	 * @return
	 */
	private int finalOrder(LinkedHashMap<String, String> juiceIndicator, LinkedHashMap<String, String> juiceNumber) {
		LOGGER.info("Entering finalOrder()");
		int finalnumber = 0;
		int counter = 0;
		double previousJuice = 0;
		double juice = 0;

		for (String jNumber : juiceNumber.keySet()) {
			String jindicator = juiceIndicator.get(jNumber);
			if (jindicator.equals("+")) {
				jindicator = "";
			}
			if (counter == 0) {
				previousJuice = juice = Double.parseDouble(jindicator + juiceNumber.get(jNumber));
			} else {
				previousJuice = juice;
				juice = Double.parseDouble(jindicator + juiceNumber.get(jNumber));
			}

			LOGGER.debug("juice: " + juice);
			LOGGER.debug("previousJuice: " + previousJuice);
			if (juice >= previousJuice) {
				finalnumber = counter;
			}
			counter++;
		}

		LOGGER.info("Exiting finalOrder()");
		return finalnumber;
	}

	/**
	 * 
	 * @param data
	 * @param whattoreplace
	 * @param toreplace
	 * @return
	 */
	private String replaceDot(String data, String whattoreplace, String toreplace) {
		LOGGER.info("Entering replaceDot()");

		final String replacement = Matcher.quoteReplacement(toreplace);
		final String searchString = Pattern.quote(whattoreplace);
		final String str = data.replaceAll(searchString, replacement);

		LOGGER.info("Exiting replaceDot()");
		return str;
	}

	/**
	 * 
	 */
	private void actasHuman() {
		// Act as a human if we should
		if (humanspeed != null && humanspeed.booleanValue()) {
			this.sleepAsUser(2000);
		}		
	}

	/**
	 * @return the httpClientWrapper
	 */
	public HttpClientWrapper getHttpClientWrapper() {
		return httpClientWrapper;
	}

	/**
	 * @param httpClientWrapper the httpClientWrapper to set
	 */
	public void setHttpClientWrapper(HttpClientWrapper httpClientWrapper) {
		this.httpClientWrapper = httpClientWrapper;
	}

	/**
	 * @return the timezone
	 */
	public String getTimezone() {
		return timezone;
	}

	/**
	 * @param timezone the timezone to set
	 */
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	/**
	 * @return the processTransaction
	 */
	public boolean isProcessTransaction() {
		return processTransaction;
	}

	/**
	 * @param processTransaction the processTransaction to set
	 */
	public void setProcessTransaction(boolean processTransaction) {
		this.processTransaction = processTransaction;
	}

	/**
	 * @return the userid
	 */
	public Long getUserid() {
		return userid;
	}

	/**
	 * @param userid the userid to set
	 */
	public void setUserid(Long userid) {
		this.userid = userid;
	}

	/**
	 * @return the humanspeed
	 */
	public Boolean getHumanspeed() {
		return humanspeed;
	}

	/**
	 * @param humanspeed the humanspeed to set
	 */
	public void setHumanspeed(Boolean humanspeed) {
		this.humanspeed = humanspeed;
	}
}