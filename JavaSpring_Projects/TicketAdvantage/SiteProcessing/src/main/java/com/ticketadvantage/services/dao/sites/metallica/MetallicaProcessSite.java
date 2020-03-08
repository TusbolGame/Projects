/**
 * 
 */
package com.ticketadvantage.services.dao.sites.metallica;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.sites.SiteEventPackage;
import com.ticketadvantage.services.dao.sites.SiteProcessor;
import com.ticketadvantage.services.dao.sites.SiteTransaction;
import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.AccountEvent;
import com.ticketadvantage.services.model.BaseRecordEvent;
import com.ticketadvantage.services.model.EventPackage;
import com.ticketadvantage.services.model.MlRecordEvent;
import com.ticketadvantage.services.model.PendingEvent;
import com.ticketadvantage.services.model.SpreadRecordEvent;
import com.ticketadvantage.services.model.TotalRecordEvent;

/**
 * @author jmiller
 *
 */
public class MetallicaProcessSite extends SiteProcessor {
	private static final Logger LOGGER = Logger.getLogger(MetallicaMobileProcessSite.class);
	private static final String MOBILE_HOST = "api.cog.cr";
	private static final String MOBILE_HOST_URL = "https://api.cog.cr";
	private final MetallicaParser MP = new MetallicaParser();
	private String VERSION = "1.3.28";
	private String website = null;
	private String authorizationToken = null;
	private String optionValue = null;
	private boolean resetConnection = false;
	private int rerunCount = 0;

	/**
	 * 
	 * @param host
	 * @param username
	 * @param password
	 * @param isMobile
	 * @param showRequestResponse
	 */
	public MetallicaProcessSite(String host, String username, String password, boolean isMobile, boolean showRequestResponse) {
		super("Metallica", host, username, password, isMobile, showRequestResponse);
		LOGGER.info("Entering MetallicaProcessSite()");

		try {
			int bindex = host.indexOf("m.");
			int eindex = host.lastIndexOf(".");
			if (bindex != -1 && eindex != -1) {
				website = host.substring(bindex + "m.".length(), eindex);
			} else {
				website = host;
			}
		} catch (Throwable t) {
			LOGGER.error(t);
			website = host;
		}

		// Set the parser
		this.siteParser = MP;

		// Setup the menu items
//		NFL_LINES_NAME = new String[] { "Game", "NFL - Pre Season", "NFL" };
//		NFL_SECOND_NAME = new String[] { "2nd Half", "NFL 2nd Half", "NFL - Pre Season 2nd Half" };
//		NCAAF_LINES_NAME = new String[] { "Game", "NCAAF - Week 1", "College Football" };
		NFL_LINES_SPORT = new String[] { "Football", "NFL" };
		NFL_LINES_NAME = new String[] { "Game", "NFL" };
		NFL_FIRST_SPORT = new String[] { "Football", "NFL" };
		NFL_FIRST_NAME = new String[] { "1st Half", "" };
		NFL_SECOND_SPORT = new String[] { "2nd Halves", "2nd Halves" };
		NFL_SECOND_NAME = new String[] { "2nd Half", "NFL", "NFL - 2nd Half" };
		NCAAF_LINES_SPORT = new String[] { "Football", "NCAA Football" };
		// NCAAF_LINES_NAME = new String[] { "Game", "College Football", "College Football Extra" };
		NCAAF_LINES_NAME = new String[] { "Game", "College - Bowls", "College - Playoffs" };
		NCAAF_FIRST_SPORT = new String[] { "Football", "NCAA Football" };
		// NCAAF_FIRST_NAME = new String[] { "1st Half", "College Football", "College Football Extra 1st Half" };
		NCAAF_FIRST_NAME = new String[] { "1st Half", "College - Bowls", "College - Playoffs" };
		NCAAF_SECOND_SPORT = new String[] { "2nd Halves", "2nd Halves" };
		// NCAAF_SECOND_NAME = new String[] { "2nd Half", "College Football", "College Football Extra" };
		NCAAF_SECOND_NAME = new String[] { "2nd Half", "College - Bowls", "College - Playoffs" };
		//NBA_LINES_NAME = new String[] { "Game", "NBA - Preseason", "NBA - Summer League & Big 3" };
		NBA_LINES_SPORT = new String[] { "Basketball", "NBA" };
		NBA_LINES_NAME = new String[] { "Game", "NBA" };
		NBA_FIRST_SPORT = new String[] { "Basketball", "NBA" };
		NBA_FIRST_NAME = new String[] { "1st Half", "" };
		NBA_SECOND_SPORT = new String[] { "2nd Halves", "2nd Halves" };
		NBA_SECOND_NAME = new String[] { "2nd Half", "NBA" };
		NCAAB_LINES_SPORT = new String[] { "Basketball", "NCAA Basketball" };
		NCAAB_LINES_NAME = new String[] { "Game", "NCAA - Minor Tournaments", "NCAA - March Madness" };
		NCAAB_FIRST_SPORT = new String[] { "Basketball", "NCAA Basketball" };
//		NCAAB_FIRST_NAME = new String[] { "1st Half", "NCAA Basketball", "NCAA Basketball Extra 1st Half", "Added Tournaments - 1st Half" };
		NCAAB_FIRST_NAME = new String[] { "1st Half", "NCAA - Minor Tournaments", "NCAA - March Madness" };
		NCAAB_SECOND_SPORT = new String[] { "2nd Halves", "2nd Halves" };
//		NCAAB_SECOND_NAME = new String[] { "2nd Half", "NCAA Basketball", "NCAA Basketball Extra", "Added Tournaments - 2nd Half", "Conference Tournaments - 2nd Half" };
		NCAAB_SECOND_NAME = new String[] { "2nd Half", "NCAA - Minor Tournaments", "NCAA - March Madness" };
		NHL_LINES_SPORT = new String[] { "Hockey", "NHL" };
		NHL_LINES_NAME = new String[] { "Game", "NHL - Conference Finals"};
		NHL_FIRST_SPORT = new String[] { "Hockey", "NHL" };
		NHL_FIRST_NAME = new String[] { "1st Period", "NHL - Conference Finals" };
		NHL_SECOND_NAME = new String[] { "NHL Hockey - 2nd Period", "NHL Hockey - Extra - 2nd Period", "NHL Hockey Added - 2nd Period" };
		NHL_SECOND_SPORT = new String[] { "NHL", "2nd Halves (click below)" };
		WNBA_LINES_NAME = new String[] { "Game", "WNBA" };
		WNBA_LINES_SPORT = new String[] { "Basketball", "WNBA" };
		WNBA_FIRST_NAME = new String[] { "1st Half", "" };
		WNBA_FIRST_SPORT = new String[] { "Basketball", "WNBA" };
		WNBA_SECOND_NAME = new String[] { "2nd Half", "WNBA" };
		WNBA_SECOND_SPORT = new String[] { "2nd Halves", "2nd Halves" };
		MLB_LINES_NAME = new String[] { "Game", "MLB - Playoffs" };
		MLB_LINES_SPORT = new String[] { "Baseball", "MLB" };
		MLB_FIRST_NAME = new String[] { "1st 5 Innings", "" };
		MLB_FIRST_SPORT = new String[] { "Baseball", "MLB" };
		MLB_SECOND_NAME = new String[] { "2nd Half", "MLB" };
		MLB_SECOND_SPORT = new String[] { "2nd Halves", "2nd Halves" };		
		INTERNATIONAL_BASEBALL_LINES_SPORT = new String[] { "Baseball", "International Baseball" };
		INTERNATIONAL_BASEBALL_LINES_NAME = new String[] { "Game", "Japan Baseball", "Korean Baseball" };
		INTERNATIONAL_BASEBALL_FIRST_SPORT = new String[] { "Baseball", "International Baseball" };
		INTERNATIONAL_BASEBALL_FIRST_NAME = new String[] { "1st 5 Innings", "Japan Baseball", "Korean Baseball" };
		INTERNATIONAL_BASEBALL_SECOND_SPORT = new String[] { "2nd Halves", "2nd Halves" };
		INTERNATIONAL_BASEBALL_SECOND_NAME = new String[] { "2nd Half", "International Baseball" };

		LOGGER.info("Exiting MetallicaProcessSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final String[][] MSITES = new String[][] {
				// {"http://www.Betwindycity.com", "w102", "Phillies","500", "500", "500", "Baltimore", "PT"},
				// { "http://www.Sunwager.com", "4488", "John", "300", "300", "300", "Asheville", "ET" },
				// {"http://www.greenisle.club", "Go508", "Iowa", "500","250", "300", "Dallas", "ET" },
				// {"http://www.Scotch88.com", "kwd2202", "jh", "500", "500", "500", "Dallas", "CT"}
//				{"http://www.madmax.ag", "820", "black2020", "500", "500", "500", "Dallas", "CT"}
//				{"http://www.madmax.ag", "834", "sky1212", "500", "500", "500", "Dallas", "CT"}
//				{"http://www.BetWC.ag", "Wc24006", "Demo", "500", "500", "500", "Dallas", "CT"}
// 				{"http://m.iron69.com", "kwd2202", "jh", "500", "500", "500", "Dallas", "CT"}
//				{"http://www.betwc.ag", "MO1", "111", "0", "0", "0", "Chicago", "CT"}
//				{"https://duck77.com", "AA022", "lq48", "0", "0", "0", "New York", "ET"}
//				{"https://globalsides.com", "kent35", "kent35", "0", "0", "0", "Dallas", "ET"}
//				{"https://iron69.com", "kwd2202", "kh", "0", "0", "0", "None", "PT"}
//				{"https://iron69.com", "kwd2202", "we", "0", "0", "0", "None", "PT"}
//				{"https://betwindycity.com", "bwc101", "wb32", "0", "0", "0", "None", "PT"}
//				{"https://betwindycity.com", "bwc116", "lava456", "0", "0", "0", "None", "PT"}
				{"https://sunwager.com", "PT586", "rowlands2", "0", "0", "0", "None", "PT"}
			};

			final MetallicaProcessSite processSite = new MetallicaProcessSite(MSITES[0][0], MSITES[0][1],
					MSITES[0][2], true, true);
	
		    processSite.httpClientWrapper.setupHttpClient(MSITES[0][6]);
		    processSite.processTransaction = false;
		    processSite.timezone = MSITES[0][7];
			processSite.loginToSite(MSITES[0][1], MSITES[0][2]);
			
			final Set<PendingEvent> pendingEvents = processSite.getPendingBets(MSITES[0][0], MSITES[0][1], new Object());
			if (pendingEvents != null && pendingEvents.size() > 0) {
				final Iterator<PendingEvent> itr = pendingEvents.iterator();
				if (itr != null) {
					while (itr.hasNext()) {
						final PendingEvent pe = itr.next();
						System.out.println("PendingEvent: " + pe);
					}
				}
			}

/*
			final PreviewInput previewInput = new PreviewInput();
			previewInput.setLinetype("spread");
			previewInput.setLineindicator("+");
			previewInput.setRotationid(new Integer(520));
			previewInput.setSporttype("ncaabfirst");
			previewInput.setProxyname("Los Angeles");
			previewInput.setTimezone("PT");

			final PreviewOutput previewData = processSite.previewEvent(previewInput);
			LOGGER.error("PreviewData: " + previewData);

			final String[][] MSITES = new String[][] {
					// {"http://www.Betwindycity.com", "w102", "Phillies","500", "500", "500", "Baltimore", "PT"},
					// { "http://www.Sunwager.com", "4488", "John", "300", "300", "300", "Asheville", "ET" },
					// {"http://www.greenisle.club", "Go508", "Iowa", "500","250", "300", "Dallas", "ET" },
					// {"http://www.Scotch88.com", "kwd2202", "jh", "500", "500", "500", "Dallas", "CT"}
//					{"http://www.madmax.ag", "820", "black2020", "500", "500", "500", "Dallas", "CT"}
//					{"http://www.madmax.ag", "834", "sky1212", "500", "500", "500", "Dallas", "CT"}
				// {"http://www.BetWC.ag", "Wc24006", "Demo", "500", "500", "500", "Dallas", "CT"}
				{"http://m.iron69.com", "kwd2202", "jh", "500", "500", "500", "Dallas", "CT"}
			};

			final MetallicaMobileProcessSite processSite = new MetallicaMobileProcessSite(MSITES[0][0], MSITES[0][1],
					MSITES[0][2]);
		    processSite.httpClientWrapper.setupHttpClient(MSITES[0][6]);
		    processSite.processTransaction = false;
		    processSite.timezone = MSITES[0][7];
			processSite.loginToSite("kwd2202", "jh");


			// Loop through the sites
			for (int i = 0; i < MSITES.length; i++) {
				final MetallicaMobileProcessSite processSite = new MetallicaMobileProcessSite(MSITES[i][0], MSITES[i][1],
						MSITES[i][2]);
				final MetallicaMobileProcessSite processSite2 = new MetallicaMobileProcessSite(MSITES[i][0], MSITES[i][1],
						MSITES[i][2]);

				    processSite.httpClientWrapper.setupHttpClient(MSITES[0][6]);
				    processSite.processTransaction = false;
				    processSite.timezone = MSITES[0][7];

				    processSite2.httpClientWrapper.setupHttpClient(MSITES[0][6]);
				    processSite2.processTransaction = false;
				    processSite2.timezone = MSITES[0][7];

					// Now call the test suite
				    MetallicaMobileProcessSite.testSite2(processSite, processSite2, i, MSITES);

				// Now call the test suite
//				MetallicaProcessSite.testSite(processSite, i, MSITES);
//			    processSite.httpClientWrapper.setupHttpClient(MSITES[i][6]);
//			    processSite.processTransaction = false;
//			    processSite.timezone = MSITES[i][7];
//				processSite.loginToSite(MSITES[i][1], MSITES[i][2]);
//				Set<PendingEvent> pendingEvents = processSite.getPendingBets(MSITES[i][0], MSITES[i][1]);
//				Iterator<PendingEvent> itr = pendingEvents.iterator();
//				while (itr.hasNext()) {
//					PendingEvent pe = itr.next();
//				}
			}
 */
		} catch (Throwable t) {
			LOGGER.error("Throwable Exception", t);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#getPendingBets(java.lang.String, java.lang.String, java.lang.Object)
	 */
	@Override
	public Set<PendingEvent> getPendingBets(String accountName, String accountId, Object anythingObject) throws BatchException {
		LOGGER.info("Entering getPendingBets()");
		LOGGER.debug("accountName: " + accountName);
		LOGGER.debug("accountId: " + accountId);
		Set<PendingEvent> pendingWagers = null;

		try {
			// Get time
			final Calendar date = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
			int hour = date.get(Calendar.HOUR_OF_DAY);

			// Check if we are between 2 am CT and 6 am CT
			if (hour < 3 || hour > 5) {
				if (this.resetConnection) {
					LOGGER.error("Resetting the connection for " + accountName + " " + accountId);
					HttpClientUtils.closeQuietly(this.httpClientWrapper.getClient());
					this.httpClientWrapper.setupHttpClient(proxyName);
					this.loginToSite(this.httpClientWrapper.getUsername(), this.httpClientWrapper.getPassword());
					this.resetConnection = false;
				}
			} else {
				this.resetConnection = true;
			}

			// Get the pending bets data
			final List<NameValuePair> headerValuePairs = new ArrayList<NameValuePair>(4);
			headerValuePairs.add(new BasicNameValuePair("Host", super.httpClientWrapper.getDomain()));
			headerValuePairs.add(new BasicNameValuePair("Referer", this.httpClientWrapper.getHost() + "/cog/index.html"));
			headerValuePairs.add(new BasicNameValuePair("Authorization", "Bearer " + authorizationToken));
/*
			final String json = "{\"Items\":[{\"Timezone\":\"ET\",\"Description\":\"2 Team - Action Reverse\",\"Wagers\":[{\"idCustomer\":377179,\"Type\"" + 
					":\"Action Reverse\",\"TicketNumber\":223783584,\"TotalRisk\":1600.0,\"TotalWin\":2000.0,\"Description\":\"2 Team" + 
					" - Action Reverse\",\"TeaserName\":\"\",\"PlacedDate\":\"2018-04-30T19:46:59.123\",\"AllowDelete\":false,\"FifteenMinsPost\"" + 
					":false,\"ContinueOnPush\":true,\"Details\":[{\"Points\":0.0,\"OddsFormatted\":\"-160\",\"Description\":\"San Francisco" + 
					" Giants\",\"AmountRisk\":1600.0,\"AmountToWin\":1000.0,\"EventdDate\":\"2018-04-30T22:15:00\",\"Outcome\":\"Pending\"" + 
					",\"Rotation\":\"912\",\"WagerType\":\"M\",\"PointsOU\":\"\\u0000\",\"Header\":\"San Diego Padres (10-19) @ San Francisco" + 
					" Giants (14-14)\",\"SportType\":\"Baseball\",\"SportSubTypeWeb\":\"MLB\",\"IdLeague\":5,\"LeagueTypeName\":\"Game\"" + 
					",\"LeagueName\":\"MLB\",\"ContestDesc\":\"\",\"RegulationNote\":\"\",\"PeriodDescription\":\"Game\",\"Pitcher1Required\"" + 
					":true,\"Pitcher2Required\":true,\"ListedPitcher1\":\"E Lauer\",\"ListedPitcher2\":\"J Samardzija\",\"PitcherAdjust\"" + 
					":0,\"AdjustableOddsFlag\":false,\"IsFreePlay\":false,\"OutcomeCode\":\"P\",\"WinnerId\":\"\",\"Team1\":\"San Diego Padres\"" + 
					",\"Team1Logo\":102,\"Team1Mascot\":\"Padres\",\"Team2\":\"San Francisco Giants\",\"Team2Logo\":104,\"Team2Mascot\"" + 
					":\"Giants\",\"Team1Score\":0,\"Team2Score\":0},{\"Points\":7.5,\"OddsFormatted\":\"+100\",\"Description\":\"San Diego" + 
					" Padres/San Francisco Giants\",\"AmountRisk\":1000.0,\"AmountToWin\":1000.0,\"EventdDate\":\"2018-04-30T22:15" + 
					":00\",\"Outcome\":\"Pending\",\"Rotation\":\"911/912\",\"WagerType\":\"L\",\"PointsOU\":\"U\",\"Header\":\"San Diego Padres" + 
					" (10-19) @ San Francisco Giants (14-14)\",\"SportType\":\"Baseball\",\"SportSubTypeWeb\":\"MLB\",\"IdLeague\":5" + 
					",\"LeagueTypeName\":\"Game\",\"LeagueName\":\"MLB\",\"ContestDesc\":\"\",\"RegulationNote\":\"\",\"PeriodDescription\"" + 
					":\"Game\",\"Pitcher1Required\":true,\"Pitcher2Required\":true,\"ListedPitcher1\":\"E Lauer\",\"ListedPitcher2\":\"J" + 
					" Samardzija\",\"PitcherAdjust\":0,\"AdjustableOddsFlag\":false,\"IsFreePlay\":false,\"OutcomeCode\":\"P\",\"WinnerId\"" + 
					":\"\",\"Team1\":\"San Diego Padres\",\"Team1Logo\":102,\"Team1Mascot\":\"Padres\",\"Team2\":\"San Francisco Giants\"" + 
					",\"Team2Logo\":104,\"Team2Mascot\":\"Giants\",\"Team1Score\":0,\"Team2Score\":0}],\"Outcome\":\"Pending\",\"WagerNumber\"" + 
					":1,\"IsFreePlay\":false}]},{\"Timezone\":\"ET\",\"Description\":\"2 Team - Action Reverse\",\"Wagers\":[{\"idCustomer\"" + 
					":377179,\"Type\":\"Action Reverse\",\"TicketNumber\":223783584,\"TotalRisk\":1600.0,\"TotalWin\":2000.0,\"Description\"" + 
					":\"2 Team - Action Reverse\",\"TeaserName\":\"\",\"PlacedDate\":\"2018-04-30T19:46:59.123\",\"AllowDelete\":false" + 
					",\"FifteenMinsPost\":false,\"ContinueOnPush\":true,\"Details\":[{\"Points\":7.5,\"OddsFormatted\":\"+100\",\"Description\"" + 
					":\"San Diego Padres/San Francisco Giants\",\"AmountRisk\":1000.0,\"AmountToWin\":1000.0,\"EventdDate\":\"2018-04-30T22" + 
					":15:00\",\"Outcome\":\"Pending\",\"Rotation\":\"911/912\",\"WagerType\":\"L\",\"PointsOU\":\"U\",\"Header\":\"San Diego Padres" + 
					" (10-19) @ San Francisco Giants (14-14)\",\"SportType\":\"Baseball\",\"SportSubTypeWeb\":\"MLB\",\"IdLeague\":5" + 
					",\"LeagueTypeName\":\"Game\",\"LeagueName\":\"MLB\",\"ContestDesc\":\"\",\"RegulationNote\":\"\",\"PeriodDescription\"" + 
					":\"Game\",\"Pitcher1Required\":true,\"Pitcher2Required\":true,\"ListedPitcher1\":\"E Lauer\",\"ListedPitcher2\":\"J" + 
					" Samardzija\",\"PitcherAdjust\":0,\"AdjustableOddsFlag\":false,\"IsFreePlay\":false,\"OutcomeCode\":\"P\",\"WinnerId\"" + 
					":\"\",\"Team1\":\"San Diego Padres\",\"Team1Logo\":102,\"Team1Mascot\":\"Padres\",\"Team2\":\"San Francisco Giants\"" + 
					",\"Team2Logo\":104,\"Team2Mascot\":\"Giants\",\"Team1Score\":0,\"Team2Score\":0},{\"Points\":0.0,\"OddsFormatted\"" + 
					":\"-160\",\"Description\":\"San Francisco Giants\",\"AmountRisk\":1600.0,\"AmountToWin\":1000.0,\"EventdDate\":\"2018-04-30T22" + 
					":15:00\",\"Outcome\":\"Pending\",\"Rotation\":\"912\",\"WagerType\":\"M\",\"PointsOU\":\"\\u0000\",\"Header\":\"San Diego" + 
					" Padres (10-19) @ San Francisco Giants (14-14)\",\"SportType\":\"Baseball\",\"SportSubTypeWeb\":\"MLB\",\"IdLeague\"" + 
					":5,\"LeagueTypeName\":\"Game\",\"LeagueName\":\"MLB\",\"ContestDesc\":\"\",\"RegulationNote\":\"\",\"PeriodDescription\"" + 
					":\"Game\",\"Pitcher1Required\":true,\"Pitcher2Required\":true,\"ListedPitcher1\":\"E Lauer\",\"ListedPitcher2\":\"J" + 
					" Samardzija\",\"PitcherAdjust\":0,\"AdjustableOddsFlag\":false,\"IsFreePlay\":false,\"OutcomeCode\":\"P\",\"WinnerId\"" + 
					":\"\",\"Team1\":\"San Diego Padres\",\"Team1Logo\":102,\"Team1Mascot\":\"Padres\",\"Team2\":\"San Francisco Giants\"" + 
					",\"Team2Logo\":104,\"Team2Mascot\":\"Giants\",\"Team1Score\":0,\"Team2Score\":0}],\"Outcome\":\"Pending\",\"WagerNumber\"" + 
					":2,\"IsFreePlay\":false}]},{\"Timezone\":\"ET\",\"Description\":\"2 Team - Parlay\",\"Wagers\":[{\"idCustomer\":377179" + 
					",\"Type\":\"Parlay\",\"TicketNumber\":223783517,\"TotalRisk\":2000.0,\"TotalWin\":4500.0,\"Description\":\"2 Team" + 
					" - Parlay\",\"TeaserName\":\"\",\"PlacedDate\":\"2018-04-30T19:46:36.497\",\"AllowDelete\":false,\"FifteenMinsPost\"" + 
					":false,\"ContinueOnPush\":false,\"Details\":[{\"Points\":0.0,\"OddsFormatted\":\"-160\",\"Description\":\"San Francisco" + 
					" Giants\",\"AmountRisk\":0.0,\"AmountToWin\":0.0,\"EventdDate\":\"2018-04-30T22:15:00\",\"Outcome\":\"Pending\",\"Rotation\"" + 
					":\"912\",\"WagerType\":\"M\",\"PointsOU\":\"\\u0000\",\"Header\":\"San Diego Padres (10-19) @ San Francisco Giants" + 
					" (14-14)\",\"SportType\":\"Baseball\",\"SportSubTypeWeb\":\"MLB\",\"IdLeague\":5,\"LeagueTypeName\":\"Game\",\"LeagueName\"" + 
					":\"MLB\",\"ContestDesc\":\"\",\"RegulationNote\":\"\",\"PeriodDescription\":\"Game\",\"Pitcher1Required\":true,\"Pitcher2Required\"" + 
					":true,\"ListedPitcher1\":\"E Lauer\",\"ListedPitcher2\":\"J Samardzija\",\"PitcherAdjust\":0,\"AdjustableOddsFlag\"" + 
					":false,\"IsFreePlay\":false,\"OutcomeCode\":\"P\",\"WinnerId\":\"\",\"Team1\":\"San Diego Padres\",\"Team1Logo\":102" + 
					",\"Team1Mascot\":\"Padres\",\"Team2\":\"San Francisco Giants\",\"Team2Logo\":104,\"Team2Mascot\":\"Giants\",\"Team1Score\"" + 
					":0,\"Team2Score\":0},{\"Points\":7.5,\"OddsFormatted\":\"+100\",\"Description\":\"San Diego Padres/San Francisco" + 
					" Giants\",\"AmountRisk\":0.0,\"AmountToWin\":0.0,\"EventdDate\":\"2018-04-30T22:15:00\",\"Outcome\":\"Pending\",\"Rotation\"" + 
					":\"911/912\",\"WagerType\":\"L\",\"PointsOU\":\"U\",\"Header\":\"San Diego Padres (10-19) @ San Francisco Giants " + 
					"(14-14)\",\"SportType\":\"Baseball\",\"SportSubTypeWeb\":\"MLB\",\"IdLeague\":5,\"LeagueTypeName\":\"Game\",\"LeagueName\"" + 
					":\"MLB\",\"ContestDesc\":\"\",\"RegulationNote\":\"\",\"PeriodDescription\":\"Game\",\"Pitcher1Required\":true,\"Pitcher2Required\"" + 
					":true,\"ListedPitcher1\":\"E Lauer\",\"ListedPitcher2\":\"J Samardzija\",\"PitcherAdjust\":0,\"AdjustableOddsFlag\"" + 
					":false,\"IsFreePlay\":false,\"OutcomeCode\":\"P\",\"WinnerId\":\"\",\"Team1\":\"San Diego Padres\",\"Team1Logo\":102" + 
					",\"Team1Mascot\":\"Padres\",\"Team2\":\"San Francisco Giants\",\"Team2Logo\":104,\"Team2Mascot\":\"Giants\",\"Team1Score\"" + 
					":0,\"Team2Score\":0}],\"Outcome\":\"Pending\",\"WagerNumber\":1,\"IsFreePlay\":false}]},{\"Timezone\":\"ET\",\"Description\"" + 
					":\"3 Team - Parlay\",\"Wagers\":[{\"idCustomer\":377179,\"Type\":\"Parlay\",\"TicketNumber\":223783679,\"TotalRisk\"" + 
					":1000.0,\"TotalWin\":4161.76,\"Description\":\"3 Team - Parlay\",\"TeaserName\":\"\",\"PlacedDate\":\"2018-04-30T19" + 
					":47:40.007\",\"AllowDelete\":false,\"FifteenMinsPost\":false,\"ContinueOnPush\":false,\"Details\":[{\"Points\":0" + 
					".0,\"OddsFormatted\":\"-160\",\"Description\":\"San Francisco Giants\",\"AmountRisk\":0.0,\"AmountToWin\":0.0,\"EventdDate\"" + 
					":\"2018-04-30T22:15:00\",\"Outcome\":\"Pending\",\"Rotation\":\"912\",\"WagerType\":\"M\",\"PointsOU\":\"\\u0000\",\"Header\"" + 
					":\"San Diego Padres (10-19) @ San Francisco Giants (14-14)\",\"SportType\":\"Baseball\",\"SportSubTypeWeb\":\"MLB\"" + 
					",\"IdLeague\":5,\"LeagueTypeName\":\"Game\",\"LeagueName\":\"MLB\",\"ContestDesc\":\"\",\"RegulationNote\":\"\",\"PeriodDescription\"" + 
					":\"Game\",\"Pitcher1Required\":true,\"Pitcher2Required\":true,\"ListedPitcher1\":\"E Lauer\",\"ListedPitcher2\":\"J" + 
					" Samardzija\",\"PitcherAdjust\":0,\"AdjustableOddsFlag\":false,\"IsFreePlay\":false,\"OutcomeCode\":\"P\",\"WinnerId\"" + 
					":\"\",\"Team1\":\"San Diego Padres\",\"Team1Logo\":102,\"Team1Mascot\":\"Padres\",\"Team2\":\"San Francisco Giants\"" + 
					",\"Team2Logo\":104,\"Team2Mascot\":\"Giants\",\"Team1Score\":0,\"Team2Score\":0},{\"Points\":7.5,\"OddsFormatted\"" + 
					":\"+100\",\"Description\":\"San Diego Padres/San Francisco Giants\",\"AmountRisk\":0.0,\"AmountToWin\":0.0,\"EventdDate\"" + 
					":\"2018-04-30T22:15:00\",\"Outcome\":\"Pending\",\"Rotation\":\"911/912\",\"WagerType\":\"L\",\"PointsOU\":\"U\",\"Header\"" + 
					":\"San Diego Padres (10-19) @ San Francisco Giants (14-14)\",\"SportType\":\"Baseball\",\"SportSubTypeWeb\":\"MLB\"" + 
					",\"IdLeague\":5,\"LeagueTypeName\":\"Game\",\"LeagueName\":\"MLB\",\"ContestDesc\":\"\",\"RegulationNote\":\"\",\"PeriodDescription\"" + 
					":\"Game\",\"Pitcher1Required\":true,\"Pitcher2Required\":true,\"ListedPitcher1\":\"E Lauer\",\"ListedPitcher2\":\"J" + 
					" Samardzija\",\"PitcherAdjust\":0,\"AdjustableOddsFlag\":false,\"IsFreePlay\":false,\"OutcomeCode\":\"P\",\"WinnerId\"" + 
					":\"\",\"Team1\":\"San Diego Padres\",\"Team1Logo\":102,\"Team1Mascot\":\"Padres\",\"Team2\":\"San Francisco Giants\"" + 
					",\"Team2Logo\":104,\"Team2Mascot\":\"Giants\",\"Team1Score\":0,\"Team2Score\":0},{\"Points\":0.0,\"OddsFormatted\"" + 
					":\"-170\",\"Description\":\"Houston Astros\",\"AmountRisk\":0.0,\"AmountToWin\":0.0,\"EventdDate\":\"2018-04-30T20" + 
					":10:00\",\"Outcome\":\"Win\",\"Rotation\":\"920\",\"WagerType\":\"M\",\"PointsOU\":\"\\u0000\",\"Header\":\"New York Yankees" + 
					" (18-9) @ Houston Astros (19-10)\",\"SportType\":\"Baseball\",\"SportSubTypeWeb\":\"MLB\",\"IdLeague\":5,\"LeagueTypeName\"" + 
					":\"Game\",\"LeagueName\":\"MLB\",\"ContestDesc\":\"\",\"RegulationNote\":\"\",\"PeriodDescription\":\"Game\",\"Pitcher1Required\"" + 
					":true,\"Pitcher2Required\":true,\"ListedPitcher1\":\"S Gray\",\"ListedPitcher2\":\"C Morton\",\"PitcherAdjust\":0" + 
					",\"AdjustableOddsFlag\":false,\"IsFreePlay\":false,\"OutcomeCode\":\"W\",\"WinnerId\":\"Houston Astros\",\"Team1\"" + 
					":\"New York Yankees\",\"Team1Logo\":98,\"Team1Mascot\":\"Yankees\",\"Team2\":\"Houston Astros\",\"Team2Logo\":91,\"Team2Mascot\"" + 
					":\"Astros\",\"Team1Score\":1,\"Team2Score\":2}],\"Outcome\":\"Pending\",\"WagerNumber\":1,\"IsFreePlay\":false}]}]" + 
					",\"Page\":0,\"PageSize\":500,\"TotalItemCount\":0}";

			if (pendingWagers == null) {
				pendingWagers = new HashSet<PendingEvent>();
			}
			final Set<PendingEvent> tempPending = MP.parsePendingBets(json, accountName, accountId);
			if (tempPending != null && !tempPending.isEmpty()) {
				for (PendingEvent pe : tempPending) {
					LOGGER.error("PendingEvent: " + pe);
					pendingWagers.add(pe);
				}
			}
*/

			final String json = getJSONSite(super.httpClientWrapper.getHost() + "/" + super.httpClientWrapper.getWebappname() + "/api/customer/pendingfilteredcount/", headerValuePairs);
			LOGGER.debug("json: " + json);
			final List<Integer> pendingList = MP.parsePendingBetsInitial(json);
			LOGGER.debug("pendingList: " + pendingList);
			for (Integer id : pendingList) {
				if (pendingWagers == null) {
					pendingWagers = new HashSet<PendingEvent>();
				}
				final String jsonpending = getJSONSite(super.httpClientWrapper.getHost() + "/" + super.httpClientWrapper.getWebappname() + "/api/customer/pendingfiltered/" + id, headerValuePairs);
				LOGGER.debug("jsonpending: " + jsonpending);
				final Set<PendingEvent> tempPending = MP.parsePendingBets(jsonpending, accountName, accountId);
				if (tempPending != null && !tempPending.isEmpty()) {
					for (PendingEvent pe : tempPending) {
						pendingWagers.add(pe);
					}
				}
			}
		} catch (BatchException be) {
			// Site returned no data after request
			if (be.getErrormessage().contains("Site returned no data after request")) {
				LOGGER.warn(be.getBexception(), be);
				HttpClientUtils.closeQuietly(this.httpClientWrapper.getClient());
				this.httpClientWrapper.setupHttpClient(proxyName);
				this.loginToSite(this.httpClientWrapper.getUsername(), this.httpClientWrapper.getPassword());
				String xhtml = "";
				if (!xhtml.contains("<iframe src=\"/Logins\"")) {
					pendingWagers = MP.parsePendingBets(xhtml, accountName, accountId);
				}
			}
		}

		LOGGER.info("Exiting getPendingBets()");
		return pendingWagers;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#loginToSite(java.lang.String, java.lang.String)
	 */
	@Override
	public String loginToSite(String username, String password) throws BatchException {
		LOGGER.info("Entering loginToSite()");
		LOGGER.debug("username: " + username);
		LOGGER.debug("password: " + password);

		// Setup the timezone
		MP.setTimezone(timezone);
		
		// Get the site
		List<NameValuePair> headerValuePairs = new ArrayList<NameValuePair>(3);
		final String originalHost = this.httpClientWrapper.getHost();

		// HTTP Header Post
		super.httpClientWrapper.setWebappname("player-api");
		headerValuePairs = new ArrayList<NameValuePair>(3);
		headerValuePairs.add(new BasicNameValuePair("Host", super.httpClientWrapper.getDomain()));
		headerValuePairs.add(new BasicNameValuePair("Referer", this.httpClientWrapper.getHost() + "/"));
		headerValuePairs.add(new BasicNameValuePair("Upgrade-Insecure-Requests", "1"));

		// Setup login POST JSON
		final List<NameValuePair> postNameValuePairs = new ArrayList<NameValuePair>(1);
		postNameValuePairs.add(new BasicNameValuePair("customerid", username));
		postNameValuePairs.add(new BasicNameValuePair("password", password));
		postNameValuePairs.add(new BasicNameValuePair("submit", "Submit"));

		// Login
		List<NameValuePair> retValue = null;
		try {
			retValue = postSitePageNoRedirect(super.httpClientWrapper.getHost() + "/" + super.httpClientWrapper.getWebappname() + "/identity/CustomerLoginRedir", null, postNameValuePairs, headerValuePairs);postSitePageNoRedirect(super.httpClientWrapper.getHost() + "/" + super.httpClientWrapper.getWebappname() + "/identity/CustomerLoginRedir", null, postNameValuePairs, headerValuePairs);
		} catch (Throwable t) {
			LOGGER.warn(t.getMessage(), t);
		}
//		retValue = postSitePageNoRedirect(super.httpClientWrapper.getHost() + "/" + super.httpClientWrapper.getWebappname() + "/identity/CustomerLoginRedir", null, postNameValuePairs, headerValuePairs);postSitePageNoRedirect(super.httpClientWrapper.getHost() + "/" + super.httpClientWrapper.getWebappname() + "/identity/CustomerLoginRedir", null, postNameValuePairs, headerValuePairs);

		String location = "";
		String xhtml = "";
				
		// Get all new cookies and the XHTML for website
		if (retValue != null && !retValue.isEmpty()) {
			final Iterator<NameValuePair> itr = retValue.iterator();
			while (itr != null && itr.hasNext()) {
				final NameValuePair nvp = (NameValuePair) itr.next();
				LOGGER.info("Header Name: " + nvp.getName());
				if ("Location".equals(nvp.getName())) {
					location = nvp.getValue();
				} else if ("json".equals(nvp.getName())) {
					xhtml = nvp.getValue();
				}
			}
		}

		String token = "";
		if (location != null && location.length() > 0) {
			// http://www.betwc.ag/cog/index.html#/redir?t=6B256405-46DB-46A0-92CB-E8A162C1C7D7
			int index = location.indexOf("redir?t=");

			if (index != -1) {
				token = location.substring(index + "redir?t=".length());
			}
		}
		LOGGER.debug("LoCaTiOn: " + location);
		LOGGER.debug("token: " + token);

		headerValuePairs = new ArrayList<NameValuePair>(3);
		String hostName = this.httpClientWrapper.getHost().replace("http://", "").replace("https://", "");
		headerValuePairs.add(new BasicNameValuePair("Host", hostName));
		headerValuePairs.add(new BasicNameValuePair("Referer", this.httpClientWrapper.getHost() + "/"));
		headerValuePairs.add(new BasicNameValuePair("If-Modified-Since", "Fri, 23 Mar 2018 16:34:55 GMT"));
		headerValuePairs.add(new BasicNameValuePair("If-None-Match", "W/\"5ab52caf-37f\""));
		headerValuePairs.add(new BasicNameValuePair("Upgrade-Insecure-Requests", "1"));
		headerValuePairs.add(new BasicNameValuePair("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.13; rv:50.0) Gecko/20100101 Firefox/50.0"));

		// Call the redirct
		retValue = super.httpClientWrapper.getSitePageNoRedirect(location, null, headerValuePairs);
		xhtml = "";

		// Get all new cookies and the XHTML for website
		if (retValue != null && !retValue.isEmpty()) {
			final Iterator<NameValuePair> itr = retValue.iterator();
			while (itr != null && itr.hasNext()) {
				final NameValuePair nvp = (NameValuePair) itr.next();
				LOGGER.info("Header Name: " + nvp.getName());
				if ("Location".equals(nvp.getName())) {
					location = nvp.getValue();
					// Call the redirct
					retValue = super.httpClientWrapper.getSitePageNoRedirect(location, null, headerValuePairs);
					xhtml = "";

					// Get all new cookies and the XHTML for website
					if (retValue != null && !retValue.isEmpty()) {
						final Iterator<NameValuePair> itr2 = retValue.iterator();
						while (itr2 != null && itr2.hasNext()) {
							final NameValuePair nvp2 = (NameValuePair) itr2.next();
							LOGGER.info("Header Name: " + nvp2.getName());
							if ("Location".equals(nvp2.getName())) {
								location = nvp2.getValue();
							} else if ("xhtml".equals(nvp2.getName())) {
								xhtml = nvp2.getValue();
								LOGGER.debug("xhtml: " + xhtml);
							}
						}
					}
				} else if ("xhtml".equals(nvp.getName())) {
					xhtml = nvp.getValue();
					LOGGER.debug("xhtml: " + xhtml);
				}
			}
		}

		if (xhtml != null) {
			int index = xhtml.indexOf("https://cdn.cog.cr/scripts/p_app_");
			if (index != -1) {
				xhtml = xhtml.substring(index + "https://cdn.cog.cr/scripts/p_app_".length());
				LOGGER.debug("Script Name: " + xhtml);
				index = xhtml.indexOf(".js");
				if (index != -1) {
					this.httpClientWrapper.setHost("cdn.cog.cr");
					xhtml = xhtml.substring(0, index);
					// this.httpClientWrapper.setHost("https://static.cog.cr");
					// static.cog.cr
					// HTTP Header Post
					headerValuePairs = new ArrayList<NameValuePair>(3);
					headerValuePairs.add(new BasicNameValuePair("Host", "cdn.cog.cr"));
					headerValuePairs.add(new BasicNameValuePair("Referer", originalHost + "/cog/index.html"));
					xhtml = getJSSiteNoBr("https://cdn.cog.cr/scripts/p_app_" + xhtml + ".js", headerValuePairs);
					LOGGER.debug("Script Name2: " + xhtml);
					if (xhtml != null && xhtml.length() > 0) {
						index = xhtml.indexOf("JS_VERSION:\"");
						if (index != -1) {
							xhtml = xhtml.substring(index + "JS_VERSION:\"".length());
							LOGGER.debug("JS_VERSION: " + xhtml);
							index = xhtml.indexOf("\",");
							if (index != -1) {
								xhtml = xhtml.substring(0, index);
								LOGGER.debug("VERSION: " + xhtml);
								if (xhtml != null && xhtml.length() > 0) {
									VERSION = xhtml;
								}
							}
						}
					}
				}
			}
		}

		this.httpClientWrapper.setHost(originalHost);
		StringBuffer postObj = new StringBuffer(200);
		postObj.append("{\"token\":\"").append(token);
		postObj.append("\",\"version\":\"").append(VERSION);
		postObj.append("\"}");

		headerValuePairs = new ArrayList<NameValuePair>(3);
		headerValuePairs.add(new BasicNameValuePair("Host", this.httpClientWrapper.getDomain()));
		headerValuePairs.add(new BasicNameValuePair("Referer", this.httpClientWrapper.getHost() + "/cog/index.html"));
		String json = postJSONSite(super.httpClientWrapper.getHost() + "/" + super.httpClientWrapper.getWebappname() + "/identity/customerLoginFromToken", postObj.toString(), headerValuePairs);
		MAP_DATA = MP.parseLogin(json);

		// Now pass in auth token
		authorizationToken = MAP_DATA.get("AccessToken");
		headerValuePairs.add(new BasicNameValuePair("Authorization", "Bearer " + authorizationToken));

		try {
			// Get the menu
			json = getJSONSite(super.httpClientWrapper.getHost() + "/" + super.httpClientWrapper.getWebappname() + "/api/wager/sportsavailablebyplayeronleague/false", headerValuePairs);
		} catch (Throwable t) {
			LOGGER.warn(t.getMessage(), t);
		}

		LOGGER.info("Exiting loginToSite()");
		return json;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#selectSport(java.lang.String)
	 */
	@Override
	protected String selectSport(String type) throws BatchException {
		LOGGER.info("Entering selectSport()");
		LOGGER.debug("type: " + type);

		// HTTP Header Post
		final List<NameValuePair> headerValuePairs = new ArrayList<NameValuePair>(4);
		headerValuePairs.add(new BasicNameValuePair("Host", MOBILE_HOST));
		headerValuePairs.add(new BasicNameValuePair("Origin", this.httpClientWrapper.getHost()));
		headerValuePairs.add(new BasicNameValuePair("Referer", this.httpClientWrapper.getHost()));
		headerValuePairs.add(new BasicNameValuePair("Authorization", "Bearer " + authorizationToken));

		// Setup schedules POST JSON
		final StringBuffer postObj = new StringBuffer(100);
		postObj.append("[");
		Set<Entry<String, String>> entries = MAP_DATA.entrySet();
		Iterator<Entry<String, String>> itr = entries.iterator();
		int size = entries.size();
		int counter = 0;
		while (itr.hasNext()) {
			counter++;
			Entry<String, String> entry = itr.next();
			String key = entry.getKey();
			if (key.startsWith("sport")) {
				if (size == counter) {
					postObj.append(entry.getValue());
				} else {
					postObj.append(entry.getValue() + ",");
				}
			}
		}
		postObj.append("]");

		// HTTP Post
		final String json = postJSONSite(MOBILE_HOST_URL + "/api/wager/schedules/S/0", postObj.toString(), headerValuePairs);

		LOGGER.info("Exiting selectSport()");
		return json;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#createSiteTransaction()
	 */
	@Override
	protected SiteTransaction createSiteTransaction() {
		LOGGER.info("Entering createSiteTransaction()");

		final MetallicaTransaction metallicaTransaction = new MetallicaTransaction();

		LOGGER.info("Exiting createSiteTransaction()");
		return metallicaTransaction;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#selectEvent(com.ticketadvantage.services.dao.sites.SiteTransaction, com.ticketadvantage.services.model.EventPackage, com.ticketadvantage.services.model.BaseRecordEvent, com.ticketadvantage.services.model.AccountEvent)
	 */
	@Override
	protected String selectEvent(SiteTransaction siteTransaction, EventPackage eventPackage, BaseRecordEvent event, AccountEvent ae) throws BatchException {
		LOGGER.info("Entering selectEvent()");
		LOGGER.debug("SiteTransaction: " + siteTransaction);
		LOGGER.debug("EventPackage: " + eventPackage);
		LOGGER.debug("BaseRecordEvent: " + event);
		LOGGER.debug("AccountEvent: " + ae);
		
		final SiteEventPackage siteEventPackage = (SiteEventPackage)eventPackage;
		String siteAmount = siteTransaction.getAmount();
		LOGGER.error("siteAmount: " + siteAmount);

		// Get the spread transaction
		if (event instanceof SpreadRecordEvent) {
			Double mAmount = Double.parseDouble(siteAmount);
			LOGGER.error("mAmount: " + mAmount);
			if (mAmount != null) {
				if (siteTransaction.getRiskorwin().intValue() == 1) {
					if (mAmount.doubleValue() > siteEventPackage.getSpreadMax().intValue()) {
						siteAmount = siteEventPackage.getSpreadMax().toString();
					}
				} else {
					float spreadJuice = ae.getSpreadjuice();
					LOGGER.error("spreadJuice: " + spreadJuice);
					Double risk = mAmount * (100 / spreadJuice);
					risk = round(risk.doubleValue(), 2);
					LOGGER.error("Risk: " + risk);
					LOGGER.error("siteEventPackage.getSpreadMax().intValue(): " + siteEventPackage.getSpreadMax().intValue());
					if (risk.doubleValue() > siteEventPackage.getSpreadMax().intValue()) {
						siteAmount = siteEventPackage.getSpreadMax().toString();
					} else {
						siteAmount = risk.toString();
					}
				}
			}

			if (siteTransaction.getOptionValue() != null) {
				optionValue = siteTransaction.getOptionValue();
			}
		} else if (event instanceof TotalRecordEvent) {
			Double mAmount = Double.parseDouble(siteAmount);
			LOGGER.error("mAmount: " + mAmount);
			if (mAmount != null) {
				if (siteTransaction.getRiskorwin().intValue() == 1) {
					if (mAmount.doubleValue() > siteEventPackage.getTotalMax().intValue()) {
						siteAmount = siteEventPackage.getTotalMax().toString();
					}
				} else {
					float totalJuice = ae.getTotaljuice();
					LOGGER.error("totalJuice: " + totalJuice);
					Double risk = mAmount * (100 / totalJuice);
					risk = round(risk.doubleValue(), 2);
					LOGGER.error("Risk: " + risk);
					LOGGER.error("siteEventPackage.getTotalMax().intValue(): " + siteEventPackage.getTotalMax().intValue());
					if (risk.doubleValue() > siteEventPackage.getTotalMax().intValue()) {
						siteAmount = siteEventPackage.getTotalMax().toString();
					} else {
						siteAmount = risk.toString();
					}
				}
			}

			if (siteTransaction.getOptionValue() != null) {
				optionValue = siteTransaction.getOptionValue();
			}
		} else if (event instanceof MlRecordEvent) {
			Double mAmount = Double.parseDouble(siteAmount);
			LOGGER.error("mAmount: " + mAmount);
			if (mAmount != null) {
				if (siteTransaction.getRiskorwin().intValue() == 1) {
					if (mAmount.doubleValue() > siteEventPackage.getTotalMax().intValue()) {
						siteAmount = siteEventPackage.getMlMax().toString();
					}
				} else {
					float mlJuice = ae.getMljuice();
					LOGGER.error("mlJuice: " + mlJuice);
					Double risk = mAmount * (100 / mlJuice);
					risk = round(risk.doubleValue(), 2);
					LOGGER.error("Risk: " + risk);
					LOGGER.error("siteEventPackage.getMlMax().intValue(): " + siteEventPackage.getMlMax().intValue());
					if (risk.doubleValue() > siteEventPackage.getMlMax().intValue()) {
						siteAmount = siteEventPackage.getMlMax().toString();
					} else {
						siteAmount = risk.toString();
					}
				}
			}
			optionValue = siteTransaction.getInputValue();
		}
		siteTransaction.setAmount(siteAmount);
		ae.setActualamount(siteAmount);
	
		// HTTP Post
		String json = addBet(siteAmount);

		if (json != null && json.contains("Your maximum wager is")) {
			// Parse the event selection
			try {
				LOGGER.error("Max wager time limit");
				MP.parseEventSelection(json, null, null);
			} catch (BatchException be) {
				if (be.getErrorcode() == BatchErrorCodes.MAX_WAGER_UNTIL_TIME) {
					String amnt = be.getBexception().replace(",", "");
					siteTransaction.setAmount(amnt);
					ae.setActualamount(amnt);

					// HTTP Post
					json = addBet(amnt);

					// Determine the type
					if ("spread".equals(ae.getType())) {
						float dFactor = determineFactor(ae.getSpreadjuice().floatValue());
						determineRiskWin(amnt, dFactor, ae);
					} else if ("total".equals(ae.getType())) {
						float dFactor = determineFactor(ae.getTotaljuice().floatValue());
						determineRiskWin(amnt, dFactor, ae);
					} else if ("ml".equals(ae.getType())) {
						float dFactor = determineFactor(ae.getMljuice().floatValue());
						determineRiskWin(amnt, dFactor, ae);
					}
				}
			}
		}

		LOGGER.info("Exiting selectEvent()");
		return json;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#parseEventSelection(java.lang.String, com.ticketadvantage.services.dao.sites.SiteTransaction, com.ticketadvantage.services.model.EventPackage, com.ticketadvantage.services.model.BaseRecordEvent, com.ticketadvantage.services.model.AccountEvent)
	 */
	@Override
	protected void parseEventSelection(String xhtml, SiteTransaction siteTransaction, EventPackage eventPackage, BaseRecordEvent event, AccountEvent ae) throws BatchException {
		LOGGER.info("Entering parseEventSelection()");

		Map<String, String> wagers = null;
		try {
			wagers = MP.parseEventSelection(xhtml, null, null);
			LOGGER.debug("wagers: " + wagers);
		} catch (BatchException be) {
			if (be.getErrorcode() == BatchErrorCodes.LINE_CHANGED_ERROR) {
				xhtml = processLineChange("AddBet", xhtml, event, ae);
				wagers = MP.parseEventSelection(xhtml, null, null);
			} else {
				throw be;
			}
		}

		// Check for a wager limit and rerun
		if (wagers.containsKey("wageramount")) {
			// Only call the select event once
			if (rerunCount++ == 0) {
				siteTransaction.setAmount(wagers.get("wageramount"));
				ae.setAmount(wagers.get("wageramount"));
				ae.setActualamount(wagers.get("wageramount"));
				xhtml = selectEvent(siteTransaction, eventPackage, event, ae);

				wagers = MP.parseEventSelection(xhtml, null, null);
				LOGGER.debug("wagers: " + wagers);

				// Check for a wager limit and rerun
				if (wagers.containsKey("wageramount")) {
					throw new BatchException(BatchErrorCodes.MAX_WAGER_AMOUNT_REACHED, BatchErrorMessage.MAX_WAGER_AMOUNT_REACHED, "The maximum wager amount has been reached " + siteTransaction.getAmount(), xhtml);
				} else if (wagers.containsKey("wageraccountexceeded")) {
					throw new BatchException(BatchErrorCodes.MAX_WAGER_AMOUNT_REACHED, BatchErrorMessage.MAX_WAGER_AMOUNT_REACHED, "The total risk amount for your selected wagers is greater than your amount available of " + wagers.get("wageraccountexceeded"), xhtml);
				}
			} else {
				throw new BatchException(BatchErrorCodes.MAX_WAGER_AMOUNT_REACHED, BatchErrorMessage.MAX_WAGER_AMOUNT_REACHED, "The maximum wager amount has been reached " + siteTransaction.getAmount(), xhtml);
			}
		} else if (wagers.containsKey("wageraccountexceeded")) {
			throw new BatchException(BatchErrorCodes.MAX_WAGER_AMOUNT_REACHED, BatchErrorMessage.MAX_WAGER_AMOUNT_REACHED, "The total risk amount for your selected wagers is greater than your amount available of " + wagers.get("wageraccountexceeded"), xhtml);
		}
		
		String delay = wagers.get("DelayUntilUTC");
		if (delay != null && delay.length() > 0) {
			final DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_DATE_TIME;
		    final TemporalAccessor accessor = timeFormatter.parse(delay);	
		    final Date date = Date.from(Instant.from(accessor));
		    final Calendar delayDate = Calendar.getInstance();
		    delayDate.setTime(date);
		    final Calendar now = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		    long duration = delayDate.getTime().getTime() - now.getTime().getTime();
		    
		    try {
		    	if (duration > 0) {
		    		Thread.sleep(duration);
		    	}
			} catch (InterruptedException e) {
				LOGGER.error("InterruptedException");
				// good practice
				Thread.currentThread().interrupt();
			}
		}

		// Now set the MAP_DATA to what was parsed
		MAP_DATA = wagers;

		LOGGER.info("Exiting parseEventSelection()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#completeTransaction(com.ticketadvantage.services.dao.sites.SiteTransaction, com.ticketadvantage.services.model.EventPackage, com.ticketadvantage.services.model.BaseRecordEvent, com.ticketadvantage.services.model.AccountEvent)
	 */
	@Override
	protected String completeTransaction(SiteTransaction siteTransaction, EventPackage eventPackage, BaseRecordEvent event, AccountEvent ae) throws BatchException {
		LOGGER.info("Entering completeTransaction()");
		String json = null;

		// Actually process the transaction?
		if (processTransaction) {
			// Save bet
			json = saveBet(siteTransaction.getAmount());
			
			try {
				MP.parseTicketNumber(json);
			} catch (BatchException be) {
				if (be.getErrorcode() == BatchErrorCodes.LINE_CHANGED_ERROR) {
					json = processLineChange("SaveBet", json, event, ae);
				} else {
					throw be;
				}
			}
		}

		LOGGER.info("Exiting completeTransaction()");
		return json;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#parseTicketTransaction(java.lang.String)
	 */
	@Override
	protected String parseTicketTransaction(String xhtml) throws BatchException {
		LOGGER.info("Entering parseTicketNumber()");		
		String ticketNumber = null;

		// Actually process the transaction?
		if (processTransaction) {
			ticketNumber = MP.parseTicketNumber(xhtml);
			LOGGER.debug("ticketNumber: " + ticketNumber);
	
			// HTTP Header Post
			final List<NameValuePair> headerValuePairs = new ArrayList<NameValuePair>(4);
			headerValuePairs.add(new BasicNameValuePair("Host", MOBILE_HOST));
			headerValuePairs.add(new BasicNameValuePair("Origin", this.httpClientWrapper.getHost()));
			headerValuePairs.add(new BasicNameValuePair("Referer", this.httpClientWrapper.getHost()));
			headerValuePairs.add(new BasicNameValuePair("Authorization", "Bearer " + authorizationToken));

			// Setup SaveBet POST JSON
			final StringBuffer postObj = new StringBuffer(50);
			postObj.append("{\"TicketNumber\":").append(ticketNumber);
			postObj.append("}");

			// HTTP Post
			final String json = postJSONSite(MOBILE_HOST_URL + "/api/wager/confirmBet", postObj.toString(), headerValuePairs);

			// Validate confirmation
			MP.parseConfirmation(json);
		}

		LOGGER.info("Exiting parseTicketNumber()");
		return ticketNumber;		
	}
	
	/**
	 * 
	 * @param juice
	 * @return
	 */
	private float determineFactor(float juice) {
		float factor = 1;
		if (juice < 0) {
			factor = 100/juice;
		} else {
			factor = juice/100;
		}
		return factor;
	}

	/**
	 * 
	 * @param amnt
	 * @param dFactor
	 * @param ae
	 */
	private void determineRiskWin(String amnt, float dFactor, AccountEvent ae) {
		LOGGER.info("Entering determineRiskWin()");
		final Double damount = new Double(amnt);

		if (dFactor >= 1) {
			final Double data = damount.doubleValue() * dFactor;
			ae.setRiskamount(amnt);
			ae.setTowinamount(data.toString());
		} else {
			final Double data = damount.doubleValue()/dFactor;
			ae.setTowinamount(amnt);
			ae.setRiskamount(data.toString());
		}

		LOGGER.info("Exiting determineRiskWin()");
	}

	/**
	 * 
	 * @param siteAmount
	 * @return
	 * @throws BatchException
	 */
	private String addBet(String siteAmount) throws BatchException {
		LOGGER.info("Entering addBet()");

		// HTTP POST Options
		final List<NameValuePair> headerValuePairs = new ArrayList<NameValuePair>(4);
		headerValuePairs.add(new BasicNameValuePair("Host", MOBILE_HOST));
		headerValuePairs.add(new BasicNameValuePair("Origin", this.httpClientWrapper.getHost()));
		headerValuePairs.add(new BasicNameValuePair("Referer", this.httpClientWrapper.getHost()));
		headerValuePairs.add(new BasicNameValuePair("Authorization", "Bearer " + authorizationToken));

		// Setup AddBet POST JSON
		StringBuffer postObj = new StringBuffer(200);
		postObj.append("[{\"BetType\":\"S\",\"TotalPicks\":1,\"IdTeaser\":0,\"IsFreePlay\":false,\"Amount\":")
				.append(siteAmount);
		postObj.append(",\"RoundRobinOptions\":[],\"Wagers\":[{\"Id\":\"").append(optionValue);
		postObj.append(
				"\",\"PitcherVisitor\":false,\"PitcherHome\":false}],\"AmountCalculation\":\"A\",\"ContinueOnPush\":true}]");

		// HTTP Post
		String json = postJSONSite(MOBILE_HOST_URL + "/api/wager/AddBet", postObj.toString(), headerValuePairs);
		
		LOGGER.info("Exiting addBet()");
		return json;
	}

	/**
	 * 
	 * @param siteAmount
	 * @return
	 * @throws BatchException
	 */
	private String saveBet(String siteAmount) throws BatchException {
		LOGGER.info("Entering saveBet()");

		// HTTP Header Post
		final List<NameValuePair> headerValuePairs = new ArrayList<NameValuePair>(4);
		headerValuePairs.add(new BasicNameValuePair("Host", MOBILE_HOST));
		headerValuePairs.add(new BasicNameValuePair("Origin", this.httpClientWrapper.getHost()));
		headerValuePairs.add(new BasicNameValuePair("Referer", this.httpClientWrapper.getHost()));
		headerValuePairs.add(new BasicNameValuePair("Authorization", "Bearer " + authorizationToken));

		// Setup SaveBet POST JSON
		final StringBuffer postObj = new StringBuffer(200);
		postObj.append(
				"{\"Details\":[{\"BetType\":\"S\",\"TotalPicks\":1,\"IdTeaser\":0,\"IsFreePlay\":false,\"Amount\":")
				.append(siteAmount);
		postObj.append(",\"RoundRobinOptions\":[],\"Wagers\":[{\"Id\":\"").append(optionValue);
		postObj.append(
				"\",\"PitcherVisitor\":false,\"PitcherHome\":false}],\"AmountCalculation\":\"A\",\"ContinueOnPush\":true}],\"DelayUntilSignature\":\"")
				.append(MAP_DATA.get("DelayUntilSignature"));
		postObj.append("\",\"DelayUntilUTC\":\"").append(MAP_DATA.get("DelayUntilUTC"));
		postObj.append("\"}");

		// HTTP Post
		String json = postJSONSite(MOBILE_HOST_URL + "/api/wager/SaveBet", postObj.toString(), headerValuePairs);
		
		LOGGER.info("Exiting saveBet()");
		return json;
	}

	/**
	 * 
	 * @param addSave
	 * @param xhtml
	 * @param event
	 * @param ae
	 * @return
	 * @throws BatchException
	 */
	private String processLineChange(String addSave, String xhtml, BaseRecordEvent event, AccountEvent ae) throws BatchException {
		LOGGER.info("Entering processLineChange()");

		final Map<String, String> lineChanges = MP.processLineChange(xhtml);
		if (lineChanges != null && !lineChanges.isEmpty()) {
			optionValue = lineChanges.get("Id");
			if (event instanceof SpreadRecordEvent) {
				final SpreadRecordEvent sre = (SpreadRecordEvent)event;
				if (determineSpreadLineChange(sre, ae, lineChanges)) {
					if (addSave.equals("AddBet")) {
						xhtml = addBet(ae.getActualamount());
					} else {
						xhtml = saveBet(ae.getActualamount());
					}
				} else {
					throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed", xhtml);
				}
			} else if (event instanceof TotalRecordEvent) {
				final TotalRecordEvent tre = (TotalRecordEvent)event;

				// Set the over/under
				if (tre.getTotalinputfirstone() != null && tre.getTotalinputfirstone().length() > 0) {
					lineChanges.put("valueindicator", "o");
				} else {
					lineChanges.put("valueindicator", "u");
				}

				if (determineTotalLineChange(tre, ae, lineChanges)) {
					if (addSave.equals("AddBet")) {
						xhtml = addBet(ae.getActualamount());
					} else {
						xhtml = saveBet(ae.getActualamount());
					}
				} else {
					throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed", xhtml);
				}
			} else if (event instanceof MlRecordEvent) {
				final MlRecordEvent mre = (MlRecordEvent)event;
				
				if (determineMlLineChange(mre, ae, lineChanges)) {
					if (addSave.equals("AddBet")) {
						xhtml = addBet(ae.getActualamount());
					} else {
						xhtml = saveBet(ae.getActualamount());
					}
				} else {
					throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed", xhtml);
				}
			}
		}

		LOGGER.info("Exiting processLineChange()");
		return xhtml;
	}
}