/**
 * 
 */
package com.ticketadvantage.services.dao.sites.sportsinsights;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.sites.SiteTeamPackage;
import com.ticketadvantage.services.dao.sites.SiteTransaction;
import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.AccountEvent;
import com.ticketadvantage.services.model.BaseRecordEvent;
import com.ticketadvantage.services.model.ClosingLine;
import com.ticketadvantage.services.model.EventPackage;
import com.ticketadvantage.services.model.EventsPackage;
import com.ticketadvantage.services.model.MlRecordEvent;
import com.ticketadvantage.services.model.SportsInsightsEvent;
import com.ticketadvantage.services.model.SpreadRecordEvent;
import com.ticketadvantage.services.model.TeamPackage;
import com.ticketadvantage.services.model.TotalRecordEvent;

/**
 * @author jmiller
 *
 */
public class SportsInsightsSite extends Updatable<EventPackage> {
	private final static Logger LOGGER = Logger.getLogger(SportsInsightsSite.class);
	private final SportsInsightsJSONParser sip = new SportsInsightsJSONParser();
	private final TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	private static final String SETUP_URL = "/api/event-model/";
	private static final String USERID = "email";
	private static final String PASSID = "password";
	private static volatile List<EventPackage> EVENT_PACKAGES;
	private String token;

	/**
	 * 
	 */
	public SportsInsightsSite(String host, String username, String password) {
		super("SportsInsightsSite", host, username, password, false, false);
		LOGGER.info("Entering SportsInsightsSite()");
		LOGGER.info("Exiting SportsInsightsSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final SportsInsightsSite processSite = new SportsInsightsSite("https://sportsinsights.actionnetwork.com/",
					"mojaxsventures@gmail.com", 
					"action1");
//			final EventsPackage eventsPackage = processSite.getNextDaySport("mlblines");
//			if (eventsPackage != null) {
//				for (EventPackage ep : eventsPackage.getEvents()) {
//					LOGGER.error("EventPackage: " + ep);
//				}
//			}

			// Setup HTTP Client with proxy if there is one
			processSite.setProxy("None");

			processSite.loginToSite("mojaxsventures@gmail.com", "action1");

//			final EventPackage ep = processSite.getEventByIdNoSport("764");
//			LOGGER.error("EventPackage: " + ep);
			
//			final EventPackage ep = processSite.getEventById("701", "NCAAB");
//			LOGGER.error("ep: " + ep);


			EventsPackage esps = processSite.getAllSports();
			Set<EventPackage> eps = esps.getEvents();
			for (EventPackage ep : eps) {
				LOGGER.debug("ep.getId(): " + ep.getId());

				if (ep.getTeamone().getId().intValue() == 312 || ep.getTeamtwo().getId().intValue() == 312) {
					ep = processSite.getScores("mlblines", ep.getSportsInsightsEventId(), ep);
					LOGGER.debug("Event: " + ep);
				}
			}

//			final EventPackage ep = processSite.getEventById("952", "MLB");
//			LOGGER.error("EP: " + ep);

			// processSite.getAllEvents();
//			EventsPackage ep = processSite.getNextDaySport("mlblines");
//			LOGGER.debug("MLB EventsPackage: " + ep);
/*		
			final SportsInsightsEvent sie = processSite.getEventByIdAll("909", "MLB", 1);
			LOGGER.debug("SportsInsightsEvent: " + sie);
			if (sie != null) {
				// * @param lineType 1=spread, 2=ml, 3=total
				// * @param gameType 0=game, 1=1h, 2=2h, 3=3h, 6=live
				// * @param sportId 1=NFL, 2=NBA, 3=MLB, 8=WNBA, 11=NCAAF
				processSite.createLineWatch(sie, 0, 2, "Kansas City Royals", -9999, 205);
			}
*/
//			EventsPackage ep = processSite.getAllEvents();
//			EventsPackage ep = processSite.getSport("ncaaflines");
//			LOGGER.debug("MLB EventsPackage: " + ep);
		} catch (Throwable t) {
			LOGGER.error("Exception", t);
		}
	}

	/**
	 * 
	 * @param proxy
	 */
	public void setProxy(String proxy) {
		LOGGER.info("Entering setProxy()");

		try {
			this.httpClientWrapper.setupHttpClient(proxy);
		} catch (BatchException be) {
			LOGGER.error(be.getMessage(), be);
		}

		LOGGER.info("Exiting setProxy()");
	}

	/**
	 * 
	 * @param rotationId
	 * @return
	 */
	public EventPackage getEventByIdNoSport(String rotationId) {
		LOGGER.info("Entering getEventById()");

		try {
			// Setup HTTP Client with proxy if there is one
			this.httpClientWrapper.setupHttpClient("None");

			// Authenticate first
			loginToSite(this.httpClientWrapper.getUsername(), this.httpClientWrapper.getPassword());

			List<NameValuePair> retValue = httpClientWrapper.getSitePage("https://sportsinsights.actionnetwork.com/live-odds", "AN_SESSION_TOKEN_V1=" + token, setupHeader(false));
			String json = httpClientWrapper.getCookiesAndXhtml(retValue);
			LOGGER.debug("json: " + json);
			String userId = sip.getUserId(json);

			int idx = userId.indexOf(",actionId");
			if (idx != -1) {
				userId = userId.substring(0, idx);
			}
			LOGGER.debug("userId: " + userId);

			retValue = httpClientWrapper.getJSONSite("https://sportsinsights.actionnetwork.com/" + SETUP_URL + userId, "AN_SESSION_TOKEN_V1=" + token, setupHeader(false));
			json = httpClientWrapper.getCookiesAndXhtml(retValue);
			LOGGER.debug("json: " + json);
			final List<EventPackage> ep = sip.getAllEvents(json);

			// Game first
			if (ep != null) {
				for (EventPackage e : ep) {
					LOGGER.debug("e: " + e);
	
					if (e.getTeamone() != null && e.getTeamtwo() != null && compareTodayOn(e.getEventdatetime())) {
						final String eventId1 = e.getTeamone().getEventid();
						final String eventId2 = e.getTeamtwo().getEventid();
						LOGGER.debug("eventId1: " + eventId1);
						LOGGER.debug("eventId2: " + eventId2);
						LOGGER.debug("rotationId: " + rotationId);
	
						if (eventId1.equals(rotationId) || eventId2.equals(rotationId)) {
							return e;
						}
					}
				}
	
				// Check 1H
				for (EventPackage e : ep) {
					LOGGER.debug("e: " + e);
	
					if (e.getTeamone() != null && e.getTeamtwo() != null && compareTodayOn(e.getEventdatetime())) {
						final String eventId1 = e.getTeamone().getEventid();
						final String eventId2 = e.getTeamtwo().getEventid();
						LOGGER.debug("eventId1: " + eventId1);
						LOGGER.debug("eventId2: " + eventId2);
						LOGGER.debug("rotationId: " + rotationId);
	
						final String eventId1h1 = "1" + eventId1;
						final String eventId2h1 = "1" + eventId2;
						if (eventId1h1.equals(rotationId) || eventId2h1.equals(rotationId)) {
							return e;
						}
					}
				}
	
				// Check 2H
				for (EventPackage e : ep) {
					LOGGER.debug("e: " + e);
	
					if (e.getTeamone() != null && e.getTeamtwo() != null && compareTodayOn(e.getEventdatetime())) {
						final String eventId1 = e.getTeamone().getEventid();
						final String eventId2 = e.getTeamtwo().getEventid();
						LOGGER.debug("eventId1: " + eventId1);
						LOGGER.debug("eventId2: " + eventId2);
						LOGGER.debug("rotationId: " + rotationId);
	
						final String eventId1h2 = "2" + eventId1;
						final String eventId2h2 = "2" + eventId2;
						if (eventId1h2.equals(rotationId) || eventId2h2.equals(rotationId)) {
							return e;
						}
					}
				}
	
				// Check 3P
				for (EventPackage e : ep) {
					LOGGER.debug("e: " + e);
	
					if (e.getTeamone() != null && e.getTeamtwo() != null && compareTodayOn(e.getEventdatetime())) {
						final String eventId1 = e.getTeamone().getEventid();
						final String eventId2 = e.getTeamtwo().getEventid();
						LOGGER.debug("eventId1: " + eventId1);
						LOGGER.debug("eventId2: " + eventId2);
						LOGGER.debug("rotationId: " + rotationId);
	
						final String eventId1h3 = "3" + eventId1;
						final String eventId2h3 = "3" + eventId2;
						if (eventId1h3.equals(rotationId) || eventId2h3.equals(rotationId)) {
							return e;
						}
					}
				}
			}
		} catch (BatchException be) {
			LOGGER.error(be.getMessage(), be);
		}

		LOGGER.info("Exiting getEventById()");
		return null;
	}

	/**
	 * 
	 * @param rotationId
	 * @param gameSport
	 * @return
	 */
	public EventPackage getEventById(String rotationId, String gameSport) {
		LOGGER.info("Entering getEventById()");

		try {
			// Setup HTTP Client with proxy if there is one
			this.httpClientWrapper.setupHttpClient("None");

			// Authenticate first
			loginToSite(this.httpClientWrapper.getUsername(), this.httpClientWrapper.getPassword());

			List<NameValuePair> retValue = httpClientWrapper.getSitePage("https://sportsinsights.actionnetwork.com/live-odds", "AN_SESSION_TOKEN_V1=" + token, setupHeader(false));
			String json = httpClientWrapper.getCookiesAndXhtml(retValue);
			LOGGER.debug("json: " + json);
			String userId = sip.getUserId(json);

			int idx = userId.indexOf(",actionId");
			if (idx != -1) {
				userId = userId.substring(0, idx);
			}
			LOGGER.debug("userId: " + userId);

			retValue = httpClientWrapper.getJSONSite("https://sportsinsights.actionnetwork.com/" + SETUP_URL + userId, "AN_SESSION_TOKEN_V1=" + token, setupHeader(false));
			json = httpClientWrapper.getCookiesAndXhtml(retValue);
			LOGGER.debug("json: " + json);
			final List<EventPackage> ep = sip.getAllEvents(json);

			for (EventPackage e : ep) {
				LOGGER.debug("e: " + e);

				if (e.getTeamone() != null && e.getTeamtwo() != null && compareTodayOn(e.getEventdatetime())) {
					final String eventId1 = e.getTeamone().getEventid();
					final String eventId2 = e.getTeamtwo().getEventid();
					LOGGER.debug("eventId1: " + eventId1);
					LOGGER.debug("eventId2: " + eventId2);
					LOGGER.debug("rotationId: " + rotationId);

					if (eventId1.equals(rotationId) || eventId2.equals(rotationId)) {
						if (gameSport != null && gameSport.equals(e.getSporttype())) {
							return e;							
						}
					} else {
						String eventId1h1 = "1" + eventId1;
						String eventId2h1 = "1" + eventId2;
						if (eventId1h1.equals(rotationId) || eventId2h1.equals(rotationId)) {
							if (gameSport != null && gameSport.equals(e.getSporttype())) {
								return e;							
							}
						} else {
							String eventId1h2 = "2" + eventId1;
							String eventId2h2 = "2" + eventId2;
							if (eventId1h2.equals(rotationId) || eventId2h2.equals(rotationId)) {
								if (gameSport != null && gameSport.equals(e.getSporttype())) {
									return e;							
								}
							} else {
								String eventId1h3 = "3" + eventId1;
								String eventId2h3 = "3" + eventId2;
								if (eventId1h3.equals(rotationId) || eventId2h3.equals(rotationId)) {
									if (gameSport != null && gameSport.equals(e.getSporttype())) {
										return e;							
									}
								}
							}
						}
					}
				}
			}
		} catch (BatchException be) {
			LOGGER.error(be.getMessage(), be);
		}

		LOGGER.info("Exiting getEventById()");
		return null;
	}

	/**
	 * 
	 * @param rotationId
	 * @param gameSport
	 * @return
	 */
	public SportsInsightsEvent getEventByIdAll(String rotationId, String gameSport, Integer day) {
		LOGGER.info("Entering getEventByIdAll()");

		try {
			// Setup HTTP Client with proxy if there is one
			this.httpClientWrapper.setupHttpClient("None");

			// Authenticate first
			loginToSite(this.httpClientWrapper.getUsername(), this.httpClientWrapper.getPassword());

			List<NameValuePair> retValue = httpClientWrapper.getSitePage("https://sportsinsights.actionnetwork.com/live-odds", "AN_SESSION_TOKEN_V1=" + token, setupHeader(false));
			String json = httpClientWrapper.getCookiesAndXhtml(retValue);
			LOGGER.debug("json: " + json);
			String userId = sip.getUserId(json);

			int idx = userId.indexOf(",actionId");
			if (idx != -1) {
				userId = userId.substring(0, idx);
			}
			LOGGER.debug("userId: " + userId);

			retValue = httpClientWrapper.getJSONSite("https://sportsinsights.actionnetwork.com/" + SETUP_URL + userId, "AN_SESSION_TOKEN_V1=" + token, setupHeader(false));
			json = httpClientWrapper.getCookiesAndXhtml(retValue);
			LOGGER.debug("json: " + json);
			final List<SportsInsightsEvent> sies = sip.processGamesAll(json);

			final Integer rotId = Integer.parseInt(rotationId);
			LOGGER.debug("rotId: " + rotId);

			for (SportsInsightsEvent sie : sies) {
				final Integer visitorNSS = sie.VisitorNSS;
				final Integer homeNSS = sie.HomeNSS;

				if (visitorNSS != null && homeNSS != null) {
					LOGGER.debug("visitorNSS: " + visitorNSS);
					LOGGER.debug("homeNSS: " + homeNSS);
					if (visitorNSS.intValue() == rotId.intValue() || homeNSS.intValue() == rotId.intValue()) {
						LOGGER.debug("sie.SportName: " + sie.SportName);
						if (gameSport != null && gameSport.equals(sie.SportName) && compareDay(day, sie.EventDate)) {
							return sie;
						}
					} else {
						String eventId1h1 = "1" + visitorNSS;
						String eventId2h1 = "1" + homeNSS;
						if (eventId1h1.equals(rotationId) || eventId2h1.equals(rotationId)) {
							if (gameSport != null && gameSport.equals(sie.SportName) && compareDay(day, sie.EventDate)) {
								return sie;			
							}
						} else {
							String eventId1h2 = "2" + visitorNSS;
							String eventId2h2 = "2" + homeNSS;
							if (eventId1h2.equals(rotationId) || eventId2h2.equals(rotationId)) {
								if (gameSport != null && gameSport.equals(sie.SportName) && compareDay(day, sie.EventDate)) {
									return sie;			
								}
							} else {
								String eventId1h3 = "3" + visitorNSS;
								String eventId2h3 = "3" + homeNSS;
								if (eventId1h3.equals(rotationId) || eventId2h3.equals(rotationId)) {
									if (gameSport != null && gameSport.equals(sie.SportName) && compareDay(day, sie.EventDate)) {
										return sie;			
									}
								}
							}
						}
					}
				}
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		LOGGER.info("Exiting getEventByIdAll()");
		return null;
	}

	/**
	 * 
	 * @param rotationId
	 * @return
	 */
	public EventPackage getEventByIdNoGameSport(String rotationId) {
		LOGGER.info("Entering getEventByIdNoGameSport()");

		try {
			// Setup HTTP Client with proxy if there is one
			this.httpClientWrapper.setupHttpClient("None");

			// Authenticate first
			loginToSite(this.httpClientWrapper.getUsername(), this.httpClientWrapper.getPassword());

			List<NameValuePair> retValue = httpClientWrapper.getSitePage("https://sportsinsights.actionnetwork.com/live-odds", "AN_SESSION_TOKEN_V1=" + token, setupHeader(false));
			String json = httpClientWrapper.getCookiesAndXhtml(retValue);
			LOGGER.debug("json: " + json);
			String userId = sip.getUserId(json);

			int idx = userId.indexOf(",actionId");
			if (idx != -1) {
				userId = userId.substring(0, idx);
			}
			LOGGER.debug("userId: " + userId);

			retValue = httpClientWrapper.getJSONSite("https://sportsinsights.actionnetwork.com/" + SETUP_URL + userId, "AN_SESSION_TOKEN_V1=" + token, setupHeader(false));
			json = httpClientWrapper.getCookiesAndXhtml(retValue);
			LOGGER.debug("json: " + json);
			final List<EventPackage> ep = sip.getAllEvents(json);

			for (EventPackage e : ep) {
				if (e.getTeamone() != null && e.getTeamtwo() != null) {
					final String eventId1 = e.getTeamone().getEventid();
					final String eventId2 = e.getTeamtwo().getEventid();
					if (eventId1.equals(rotationId) || eventId2.equals(rotationId)) {
						return e;
					}
				}
			}
		} catch (BatchException be) {
			LOGGER.error(be.getMessage(), be);
		}

		LOGGER.info("Exiting getEventByIdNoGameSport()");
		return null;
	}

	/**
	 * 
	 * @param rotationId
	 * @return
	 */
	public EventPackage getEventByIdAllNoGameSport(String rotationId) {
		LOGGER.info("Entering getEventByIdAllNoGameSport()");

		try {
			final EventsPackage eventsPackage = this.getAllSports();
			for (EventPackage e : eventsPackage.getEvents()) {
				if (e.getTeamone() != null && e.getTeamtwo() != null) {
					final String eventId1 = e.getTeamone().getEventid();
					final String eventId2 = e.getTeamtwo().getEventid();
					if (eventId1.equals(rotationId) || eventId2.equals(rotationId)) {
						return e;
					}
				}
			}
		} catch (BatchException be) {
			LOGGER.error(be.getMessage(), be);
		}

		LOGGER.info("Exiting getEventByIdAllNoGameSport()");
		return null;
	}

	/**
	 * 
	 * @param gameSport
	 * @return
	 */
	protected String getGameSport(String sport) {
		LOGGER.info("Exiting getGameSport()");
		String gameSport = null;

		if ("NFL".equals(sport)) {
			gameSport = "Football";
		} else if ("NCAAF".equals(sport)) {
			gameSport = "Football";
		} else if ("NBA".equals(sport)) {
			gameSport = "Basketball";
		} else if ("NCAAB".equals(sport)) {
			gameSport = "Basketball";
		} else if ("WNBA".equals(sport)) {
			gameSport = "Basketball";
		} else if ("NHL".equals(sport)) {
			gameSport = "Hockey";
		} else if ("MLB".equals(sport)) {
			gameSport = "Baseball";
		}

		LOGGER.info("Exiting getGameSport()");
		return gameSport;
	}

	/**
	 * 
	 * @return
	 */
	public Set<EventPackage> forceGetAllEvents() {
		LOGGER.info("Entering forceGetAllEvents()");
		Set<EventPackage> eps = null;

		try {
			List<NameValuePair> retValue = httpClientWrapper.getSitePage("https://sportsinsights.actionnetwork.com/live-odds", "AN_SESSION_TOKEN_V1=" + token, setupHeader(false));
			String json = httpClientWrapper.getCookiesAndXhtml(retValue);
			LOGGER.debug("json: " + json);
			String userId = sip.getUserId(json);

			int idx = userId.indexOf(",actionId");
			if (idx != -1) {
				userId = userId.substring(0, idx);
			}
			LOGGER.debug("userId: " + userId);

			retValue = httpClientWrapper.getJSONSite("https://sportsinsights.actionnetwork.com/" + SETUP_URL + userId, "AN_SESSION_TOKEN_V1=" + token, setupHeader(false));
			json = httpClientWrapper.getCookiesAndXhtml(retValue);
			LOGGER.debug("json: " + json);
			final List<EventPackage> ep = sip.getAllEvents(json);
			eps = new LinkedHashSet<EventPackage>(ep);
		} catch (BatchException be) {
			LOGGER.error(be.getMessage(), be);
		}

		LOGGER.info("Exiting forceGetAllEvents()");
		return eps;
	}

	/**
	 * 
	 * @return
	 */
	public Set<EventPackage> forceGetAllTodayOnEvents() {
		LOGGER.info("Entering forceGetAllTodayOnEvents()");
		Set<EventPackage> eps = null;

		try {
			List<NameValuePair> retValue = httpClientWrapper.getSitePage("https://sportsinsights.actionnetwork.com/live-odds", "AN_SESSION_TOKEN_V1=" + token, setupHeader(false));
			String json = httpClientWrapper.getCookiesAndXhtml(retValue);
			LOGGER.debug("json: " + json);
			String userId = sip.getUserId(json);

			int idx = userId.indexOf(",actionId");
			if (idx != -1) {
				userId = userId.substring(0, idx);
			}
			LOGGER.debug("userId: " + userId);

			retValue = httpClientWrapper.getJSONSite("https://sportsinsights.actionnetwork.com/" + SETUP_URL + userId, "AN_SESSION_TOKEN_V1=" + token, setupHeader(false));
			json = httpClientWrapper.getCookiesAndXhtml(retValue);
			LOGGER.debug("json: " + json);
			final List<EventPackage> eplist = sip.getAllEvents(json);

/*
			if (eplist != null && eplist.size() > 0) {
				for (EventPackage ep : eplist) {
					final Integer siei = ep.getSportsInsightsEventId();
					final String sportType = ep.getSporttype();
					if (sportType != null && "mlb".equals(sportType.toLowerCase())) {
						final List<NameValuePair> mlbevent = httpClientWrapper.getJSONSite("https://sportsinsights.actionnetwork.com/api/events/" + siei + "/boxscore/", null, setupHeader(false));
						final String jsonMlb = httpClientWrapper.getCookiesAndXhtml(mlbevent);
						sip.parseMlbScore(jsonMlb, ep);
					}
				}
			}
*/
			final List<EventPackage> eventList = new ArrayList<EventPackage>();

			final TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
			final Calendar today = Calendar.getInstance(timeZone);
			today.add(Calendar.DAY_OF_MONTH, -1);
			today.set(Calendar.HOUR_OF_DAY, 0);
			today.set(Calendar.MINUTE, 0);
			today.set(Calendar.SECOND, 0);
			today.set(Calendar.MILLISECOND, 0);
			LOGGER.debug("today: " + today.getTime());

			for (EventPackage ep : eplist) {
				if (today.getTime().equals(ep.getEventdatetime())
					|| ep.getEventdatetime().after(today.getTime())) {
					eventList.add(ep);
				}
			}

			eps = new LinkedHashSet<EventPackage>(eventList);
		} catch (BatchException be) {
			LOGGER.error(be.getMessage(), be);
		}

		LOGGER.info("Exiting forceGetAllTodayOnEvents()");
		return eps;
	}

	/**
	 * 
	 * @return
	 */
	public Set<EventPackage> getAllEvents() {
		LOGGER.info("Entering getAllEvents()");
		Set<EventPackage> eps = null;

		try {
			invalidateCache();
			checkRefresh();

			if (EVENT_PACKAGES == null || EVENT_PACKAGES.size() == 0) {
				EVENT_PACKAGES = loadFromSite();
				if (EVENT_PACKAGES == null || EVENT_PACKAGES.size() == 0) {
					EVENT_PACKAGES = loadFromSite();
				}
			}
			eps = new LinkedHashSet<EventPackage>(EVENT_PACKAGES);
		} catch (Exception e) {
			LOGGER.error("Exception", e);
		}
//		LOGGER.debug("EventPackages: " + eps);

		LOGGER.info("Exiting getAllEvents()");
		return eps;
	}

	/**
	 * 
	 * @param sportType
	 * @return
	 * @throws BatchException
	 */
	public EventsPackage getSport(String sportType) throws BatchException {
		LOGGER.info("Entering getSport()");
		LOGGER.debug("sportType: " + sportType);
		Set<EventPackage> eventPackages = null;
		final List<EventPackage> eventList = new ArrayList<EventPackage>();

		// Make sure cache is refreshed
		if (EVENT_PACKAGES == null) {
			EVENT_PACKAGES = loadFromSite();	
		}
		final TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
		final Calendar today = Calendar.getInstance(timeZone);
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.set(Calendar.MILLISECOND, 0);
		LOGGER.debug("today: " + today.getTime());

		if (sportType.contains("nfl")) {
			for (EventPackage ep : EVENT_PACKAGES) {
				if ("NFL".equals(ep.getSporttype()) && ep.getEventdatetime() != null && (today.getTime().equals(ep.getEventdatetime()) || ep.getEventdatetime().after(today.getTime()))) {
					final EventPackage eventPackage = processSport(ep, sportType);
					eventList.add(eventPackage);
				}
			}
		} else if (sportType.contains("ncaaf")) {
			for (EventPackage ep : EVENT_PACKAGES) {
				if ("NCAAF".equals(ep.getSporttype()) && ep.getEventdatetime() != null && (today.getTime().equals(ep.getEventdatetime()) || ep.getEventdatetime().after(today.getTime()))) {
					final EventPackage eventPackage = processSport(ep, sportType);
					eventList.add(eventPackage);
				}
			}
		} else if (sportType.contains("wnba")) {
			for (EventPackage ep : EVENT_PACKAGES) {
				if ("WNBA".equals(ep.getSporttype()) && ep.getEventdatetime() != null && (today.getTime().equals(ep.getEventdatetime()) || ep.getEventdatetime().after(today.getTime()))) {
					final EventPackage eventPackage = processSport(ep, sportType);
					eventList.add(eventPackage);
				}
			}
		} else if (sportType.contains("nba")) {
			for (EventPackage ep : EVENT_PACKAGES) {
				if ("NBA".equals(ep.getSporttype()) && ep.getEventdatetime() != null && (today.getTime().equals(ep.getEventdatetime()) || ep.getEventdatetime().after(today.getTime()))) {
					final EventPackage eventPackage = processSport(ep, sportType);
					eventList.add(eventPackage);
				}
			}
		} else if (sportType.contains("ncaab")) {
			for (EventPackage ep : EVENT_PACKAGES) {
				if ("NCAAB".equals(ep.getSporttype()) && ep.getEventdatetime() != null && (today.getTime().equals(ep.getEventdatetime()) || ep.getEventdatetime().after(today.getTime()))) {
					final EventPackage eventPackage = processSport(ep, sportType);
					eventList.add(eventPackage);
				}
			}
		} else if (sportType.contains("nhl")) {
			for (EventPackage ep : EVENT_PACKAGES) {
				if ("NHL".equals(ep.getSporttype()) && ep.getEventdatetime() != null && (today.getTime().equals(ep.getEventdatetime()) || ep.getEventdatetime().after(today.getTime()))) {
					final EventPackage eventPackage = processSport(ep, sportType);
					eventList.add(eventPackage);
				}
			}
		} else if (sportType.contains("mlb")) {
			for (EventPackage ep : EVENT_PACKAGES) {
				if ("MLB".equals(ep.getSporttype()) && ep.getEventdatetime() != null && (today.getTime().equals(ep.getEventdatetime()) || ep.getEventdatetime().after(today.getTime()))) {
					final EventPackage eventPackage = processSport(ep, sportType);
					eventList.add(eventPackage);
				}
			}
		}
		eventPackages = new LinkedHashSet<EventPackage>(eventList);
		LOGGER.debug("EventPackages: " + eventPackages);
		final EventsPackage eps = new EventsPackage();
		eps.addEvents(eventPackages);

		LOGGER.info("Exiting getSport()");
		return eps;
	}

	/**
	 * 
	 * @param sportType
	 * @return
	 * @throws BatchException
	 */
	public EventsPackage getNextDaySport(String sportType) throws BatchException {
		LOGGER.info("Entering getNextDaySport()");
		LOGGER.debug("sportType: " + sportType);
		Set<EventPackage> eventPackages = null;
		final List<EventPackage> eventList = new ArrayList<EventPackage>();

		if (EVENT_PACKAGES == null) {
			EVENT_PACKAGES = loadFromSite();	
		}

		if (sportType.contains("nfl")) {
			for (EventPackage ep : EVENT_PACKAGES) {
				if ("NFL".equals(ep.getSporttype()) && ep.getEventdatetime() != null && compareDates(ep.getEventdatetime())) {
					final EventPackage eventPackage = processSport(ep, sportType);
					eventList.add(eventPackage);
				}
			}
		} else if (sportType.contains("ncaaf")) {
			for (EventPackage ep : EVENT_PACKAGES) {
				if ("NCAAF".equals(ep.getSporttype()) && ep.getEventdatetime() != null && compareDates(ep.getEventdatetime())) {
					final EventPackage eventPackage = processSport(ep, sportType);
					eventList.add(eventPackage);
				}
			}
		} else if (sportType.contains("wnba")) {
			for (EventPackage ep : EVENT_PACKAGES) {
				if ("WNBA".equals(ep.getSporttype()) && ep.getEventdatetime() != null && compareDates(ep.getEventdatetime())) {
					final EventPackage eventPackage = processSport(ep, sportType);
					eventList.add(eventPackage);
				}
			}
		} else if (sportType.contains("nba")) {
			for (EventPackage ep : EVENT_PACKAGES) {
				if ("NBA".equals(ep.getSporttype()) && ep.getEventdatetime() != null && compareDates(ep.getEventdatetime())) {
					final EventPackage eventPackage = processSport(ep, sportType);
					eventList.add(eventPackage);
				}
			}
		} else if (sportType.contains("ncaab")) {
			for (EventPackage ep : EVENT_PACKAGES) {
				if ("NCAAB".equals(ep.getSporttype()) && ep.getEventdatetime() != null && compareDates(ep.getEventdatetime())) {
					final EventPackage eventPackage = processSport(ep, sportType);
					eventList.add(eventPackage);
				}
			}
		} else if (sportType.contains("nhl")) {
			for (EventPackage ep : EVENT_PACKAGES) {
				if ("NHL".equals(ep.getSporttype()) && ep.getEventdatetime() != null && compareDates(ep.getEventdatetime())) {
					final EventPackage eventPackage = processSport(ep, sportType);
					eventList.add(eventPackage);
				}
			}
		} else if (sportType.contains("mlb")) {
			for (EventPackage ep : EVENT_PACKAGES) {
				if ("MLB".equals(ep.getSporttype()) && ep.getEventdatetime() != null && compareDates(ep.getEventdatetime())) {
					final EventPackage eventPackage = processSport(ep, sportType);
					eventList.add(eventPackage);
				}
			}
		}
		eventPackages = new LinkedHashSet<EventPackage>(eventList);
		LOGGER.debug("EventPackages: " + eventPackages);
		final EventsPackage eps = new EventsPackage();
		eps.addEvents(eventPackages);

		LOGGER.info("Exiting getNextDaySport()");
		return eps;
	}

	/**
	 * 
	 * @param sportType
	 * @return
	 * @throws BatchException
	 */
	public EventsPackage getDayOfSport(String sportType) throws BatchException {
		LOGGER.info("Entering getDayOfSport()");
		LOGGER.debug("sportType: " + sportType);
		Set<EventPackage> eventPackages = null;
		final List<EventPackage> eventList = new ArrayList<EventPackage>();

		if (EVENT_PACKAGES == null) {
			EVENT_PACKAGES = loadFromSite();	
		}

		if (sportType.contains("nfl")) {
			for (EventPackage ep : EVENT_PACKAGES) {
				if ("NFL".equals(ep.getSporttype()) && ep.getEventdatetime() != null && compareDayOfDates(ep.getEventdatetime())) {
					final EventPackage eventPackage = processSport(ep, sportType);
					eventList.add(eventPackage);
				}
			}
		} else if (sportType.contains("ncaaf")) {
			for (EventPackage ep : EVENT_PACKAGES) {
				if ("NCAAF".equals(ep.getSporttype()) && ep.getEventdatetime() != null && compareDayOfDates(ep.getEventdatetime())) {
					final EventPackage eventPackage = processSport(ep, sportType);
					eventList.add(eventPackage);
				}
			}
		} else if (sportType.contains("wnba")) {
			for (EventPackage ep : EVENT_PACKAGES) {
				if ("WNBA".equals(ep.getSporttype()) && ep.getEventdatetime() != null && compareDayOfDates(ep.getEventdatetime())) {
					final EventPackage eventPackage = processSport(ep, sportType);
					eventList.add(eventPackage);
				}
			}
		} else if (sportType.contains("nba")) {
			for (EventPackage ep : EVENT_PACKAGES) {
				if ("NBA".equals(ep.getSporttype()) && ep.getEventdatetime() != null && compareDayOfDates(ep.getEventdatetime())) {
					final EventPackage eventPackage = processSport(ep, sportType);
					eventList.add(eventPackage);
				}
			}
		} else if (sportType.contains("ncaab")) {
			for (EventPackage ep : EVENT_PACKAGES) {
				if ("NCAAB".equals(ep.getSporttype()) && ep.getEventdatetime() != null && compareDayOfDates(ep.getEventdatetime())) {
					final EventPackage eventPackage = processSport(ep, sportType);
					eventList.add(eventPackage);
				}
			}
		} else if (sportType.contains("nhl")) {
			for (EventPackage ep : EVENT_PACKAGES) {
				if ("NHL".equals(ep.getSporttype()) && ep.getEventdatetime() != null && compareDayOfDates(ep.getEventdatetime())) {
					final EventPackage eventPackage = processSport(ep, sportType);
					eventList.add(eventPackage);
				}
			}
		} else if (sportType.contains("mlb")) {
			for (EventPackage ep : EVENT_PACKAGES) {
				if ("MLB".equals(ep.getSporttype()) && ep.getEventdatetime() != null && compareDayOfDates(ep.getEventdatetime())) {
					final EventPackage eventPackage = processSport(ep, sportType);
					eventList.add(eventPackage);
				}
			}
		}
		eventPackages = new LinkedHashSet<EventPackage>(eventList);
		LOGGER.debug("EventPackages: " + eventPackages);
		final EventsPackage eps = new EventsPackage();
		eps.addEvents(eventPackages);

		LOGGER.info("Exiting getDayOfSport()");
		return eps;
	}

	/**
	 * 
	 * @param sie
	 * @param gameType
	 * @param lineType
	 * @param selectedTeam
	 * @param line
	 * @param juice
	 * @throws BatchException
	 */
	public void createLineWatch(SportsInsightsEvent sie, 
			Integer gameType, 
			Integer lineType,
			String selectedTeam,
			Double line,
			Double juice) throws BatchException {
		LOGGER.info("Entering createLineWatch()");

		try {
			// Setup HTTP Client with proxy if there is one
			this.httpClientWrapper.setupHttpClient("None");
	
			// Authenticate first
			loginToSite(this.httpClientWrapper.getUsername(), this.httpClientWrapper.getPassword());

			/*
			 * FIX URL
			 */
			final List<NameValuePair> headerValuePairs = new ArrayList<NameValuePair>(3);
			headerValuePairs.add(new BasicNameValuePair("Host", "account.sportsinsights.com"));
			headerValuePairs.add(new BasicNameValuePair("Referer", "https://sportsinsights.actionnetwork.com/live-odds/"));

			final StringBuffer json = new StringBuffer(1000);
			json.append("{\"LineType\":" + lineType + ",\"GameType\":" + gameType + ",\"SelectedTeam\":\"" + selectedTeam + "\",\"Team1BestLine\":\"\",\"Team2BestLine\":\"\",");
			if (juice != null && juice.intValue() != -9999) {
				json.append("\"Juice\"" + ":" + juice);
			}
			if (line != null && line.intValue() != -9999) {
				json.append("\"Spread\"" + ":" + line);
			}
			json.append(",\"LineWatcherEventModel\":{\"EventId\":" + sie.EventId + ",\"SportId\":" + sie.SportId + ",\"HomeTeam\":\"" + sie.HomeTeam + "\",\"VisitorTeam\"" + 
					":\"" + sie.VisitorTeam + "\",\"HomeTeamShort\":\"" + sie.HomeTeamShort + "\",\"VisitorTeamShort\":\"" + sie.VisitorTeamShort + "\",\"TotalBets\":" + sie.TotalBets + ",\"EventDate\":\"" + sie.EventDate + 
					"\",\"EventDateShort\":\"" + sie.EventDateShort + "\",\"HomeNSS\":" + sie.HomeNSS + ",\"VisitorNSS\":" + sie.VisitorNSS + ",\"SportName\":\"" + sie.SportName + "\",\"HomePitcher\"" + 
					":\"" + sie.HomePitcher + "\",\"VisitorPitcher\":\"" + sie.VisitorPitcher + "\",\"GameId\":" + sie.GameId + ",\"VisitorScore\":" + sie.VisitorScore + ",\"HomeScore\":" + sie.HomeScore + ",\"PeriodState\":" + sie.PeriodState + ",\"PeriodTime\"" + 
					":\"" + sie.PeriodTime + "\",\"PeriodShort\":\"" + sie.PeriodShort + "\",\"VisitorValues\":{\"Overall\":" + sie.VisitorValues.Overall + ",\"Contrarian\":" + sie.VisitorValues.Contrarian + ",\"Steam\":" + sie.VisitorValues.Steam + ",\"ReverseLine\":" + sie.VisitorValues.ReverseLine + ",\"Official\"" + 
					":" + sie.VisitorValues.Official + "},\"HomeValues\":{\"Overall\":" + sie.HomeValues.Overall + ",\"Contrarian\":" + sie.HomeValues.Contrarian + ",\"Steam\":" + sie.HomeValues.Steam + ",\"ReverseLine\":" + sie.HomeValues.ReverseLine + ",\"Official\":" + sie.HomeValues.Official + "},\"Value1\":" + sie.Value1 + ",\"Value2\"" + 
					":" + sie.Value2 + ",\"Group1\":\"" + sie.Group1 + "\",\"Group2\":\"" + sie.Group2 + "\",\"MyGame\":" + sie.MyGame + ",\"NeutralSite\":" + sie.NeutralSite + ",\"VisitorROTNumber\":\"" + sie.VisitorROTNumber + "\",\"HomeROTNumber\"" + 
					":\"" + sie.HomeROTNumber + "\",\"GameCode\":\"" + sie.GameCode + "\",\"LeagueId\":" + sie.LeagueId + ",\"HalftimeModifiedDate\":\"" + sie.HalftimeModifiedDate + "\",\"SystemEventId\":0,\"Line\"" + 
					":\"\"}}");
			postJSONSite("https://sportsinsights.actionnetwork.com/api/linewatcherpicker/save", json.toString(), headerValuePairs);
		} catch (Throwable t) {
			throw new BatchException(BatchErrorCodes.CREATE_LINE_WATCH_EXCEPTION,
					BatchErrorMessage.CREATE_LINE_WATCH_EXCEPTION, 
					"Exception creating line watch for Team: " + selectedTeam + " Line: " + line + " Juice: " + juice);
		}

		LOGGER.info("Exiting createLineWatch()");
	}

	/**
	 * 
	 * @param eventdatetime
	 * @return
	 */
	private boolean compareDates(Date eventdatetime) {
		final TimeZone ptTimezone = TimeZone.getTimeZone("America/Los_Angeles");
		final Calendar tomorrow = Calendar.getInstance(ptTimezone);
		final Calendar eventDate = Calendar.getInstance(ptTimezone);

		// add one day to the date/calendar
		tomorrow.add(Calendar.DAY_OF_YEAR, 1);
		tomorrow.set(Calendar.HOUR_OF_DAY, 0);
		tomorrow.set(Calendar.MINUTE, 0);
		tomorrow.set(Calendar.SECOND, 0);
		tomorrow.set(Calendar.MILLISECOND, 0);
		eventDate.setTime(eventdatetime);

		if (tomorrow.get(Calendar.DAY_OF_MONTH) == eventDate.get(Calendar.DAY_OF_MONTH) &&
			tomorrow.get(Calendar.MONTH) == eventDate.get(Calendar.MONTH) && 
			tomorrow.get(Calendar.YEAR) == eventDate.get(Calendar.YEAR)) {
			return true;
		}

		LOGGER.debug("tomorrow: " + tomorrow.getTime());
		return false;
	}

	/**
	 * 
	 * @param eventdatetime
	 * @return
	 */
	private boolean compareDayOfDates(Date eventdatetime) {
		final Calendar tomorrow = Calendar.getInstance(timeZone);
		// add one day to the date/calendar
		tomorrow.set(Calendar.HOUR_OF_DAY, 0);
		tomorrow.set(Calendar.MINUTE, 0);
		tomorrow.set(Calendar.SECOND, 0);
		tomorrow.set(Calendar.MILLISECOND, 0);
		final Calendar eventDate = Calendar.getInstance(timeZone);
		eventDate.setTime(eventdatetime);
		if (tomorrow.get(Calendar.DAY_OF_MONTH) == eventDate.get(Calendar.DAY_OF_MONTH) &&
			tomorrow.get(Calendar.MONTH) == eventDate.get(Calendar.MONTH) && 
			tomorrow.get(Calendar.YEAR) == eventDate.get(Calendar.YEAR)) {
			return true;
		}

		LOGGER.debug("tomorrow: " + tomorrow.getTime());
		return false;
	}

	/**
	 * 
	 * @param eventdatetime
	 * @return
	 */
	private boolean compareDay(Integer day, String eventdatetime) {
		LOGGER.info("Entering compareDay()");
		// -1 yesterday
		//  0 today
		//  1 tomorrow
		//  2 anytime
		final Calendar checkdate = Calendar.getInstance(timeZone);

		// add one day to the date/calendar
		if (day != null && day.intValue() != 2) {
			Date dateEvent = null;
			final TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
			final Calendar gameDate = Calendar.getInstance(timeZone);
			SDF.setTimeZone(timeZone);

			if (eventdatetime != null && eventdatetime.length() > 0) {
				try {
					dateEvent = SDF.parse(eventdatetime);
					gameDate.setTime(dateEvent);
				} catch (ParseException pe) {
					LOGGER.info(pe.getMessage(), pe);
				}
			} else {
				dateEvent = gameDate.getTime();
			}

			checkdate.add(Calendar.DAY_OF_YEAR, day.intValue());
			checkdate.set(Calendar.HOUR_OF_DAY, 0);
			checkdate.set(Calendar.MINUTE, 0);
			checkdate.set(Calendar.SECOND, 0);
			checkdate.set(Calendar.MILLISECOND, 0);
			final Calendar eventDate = Calendar.getInstance(timeZone);
			eventDate.setTime(dateEvent);
			if (checkdate.get(Calendar.DAY_OF_MONTH) == eventDate.get(Calendar.DAY_OF_MONTH) &&
				checkdate.get(Calendar.MONTH) == eventDate.get(Calendar.MONTH) && 
				checkdate.get(Calendar.YEAR) == eventDate.get(Calendar.YEAR)) {
				LOGGER.info("Exiting compareDay()");
				return true;
			}
		} else {
			LOGGER.info("Exiting compareDay()");
			return true;
		}

		LOGGER.info("Exiting compareDay()");
		return false;
	}

	/**
	 * 
	 * @param eventdatetime
	 * @return
	 */
	private boolean compareTodayOn(Date eventdatetime) {
		LOGGER.info("Entering compareTodayOn()");
		LOGGER.debug("eventdatetime: " + eventdatetime);

		final Calendar checkdate = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
		LOGGER.debug("checkdate: " + checkdate.getTime());

		final Calendar eventdate = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
		eventdate.setTime(eventdatetime);
		LOGGER.debug("eventdate: " + eventdate.getTime());

		if (eventdate.getTime().after(checkdate.getTime())) {
			LOGGER.debug("Exiting compareTodayOn()");
			return true;
		}

		LOGGER.info("Exiting compareTodayOn()");
		return false;
	}

	/**
	 * 
	 * @return
	 * @throws BatchException
	 */
	public EventsPackage getAllSports() throws BatchException {
		LOGGER.info("Entering getAllSports()");
		Set<EventPackage> eventPackages = null;
		final List<EventPackage> eventList = new ArrayList<EventPackage>();

		// Make sure cache is refreshed
		if (EVENT_PACKAGES == null || EVENT_PACKAGES.size() == 0) {
			EVENT_PACKAGES = loadFromSite();
		}
		final TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
		final Calendar today = Calendar.getInstance(timeZone);
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.set(Calendar.MILLISECOND, 0);
		LOGGER.debug("today: " + today.getTime());

		for (EventPackage ep : EVENT_PACKAGES) {
			if (today.getTime().equals(ep.getEventdatetime())
				|| ep.getEventdatetime().after(today.getTime())) {
				final EventPackage eventPackage = processSport(ep, "first");
				eventList.add(eventPackage);
			}
		}

		for (EventPackage ep : EVENT_PACKAGES) {
			if (today.getTime().equals(ep.getEventdatetime())
				|| ep.getEventdatetime().after(today.getTime())) {
				final EventPackage eventPackage = processSport(ep, "second");
				eventList.add(eventPackage);
			}
		}

		for (EventPackage ep : EVENT_PACKAGES) {
			if (today.getTime().equals(ep.getEventdatetime())
				|| ep.getEventdatetime().after(today.getTime())) {
				final EventPackage eventPackage = processSport(ep, "game");
				eventList.add(eventPackage);
			}
		}

		eventPackages = new LinkedHashSet<EventPackage>(eventList);
		LOGGER.debug("EventPackages: " + eventPackages);
		final EventsPackage eps = new EventsPackage();
		eps.addEvents(eventPackages);

		LOGGER.info("Exiting getAllSports()");
		return eps;
	}

	/**
	 * 
	 * @return
	 * @throws BatchException
	 */
	public EventsPackage getAllSportsGame() throws BatchException {
		LOGGER.info("Entering getAllSportsGame()");
		Set<EventPackage> eventPackages = null;
		final List<EventPackage> eventList = new ArrayList<EventPackage>();

		// Make sure cache is refreshed
		if (EVENT_PACKAGES == null || EVENT_PACKAGES.size() == 0) {
			EVENT_PACKAGES = loadFromSite();
		}
		final TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
		final Calendar today = Calendar.getInstance(timeZone);
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.set(Calendar.MILLISECOND, 0);
		LOGGER.debug("today: " + today.getTime());

		for (EventPackage ep : EVENT_PACKAGES) {
			if (ep.getEventdatetime().equals(today.getTime()) || ep.getEventdatetime().after(today.getTime())) {
				final EventPackage eventPackage = processSport(ep, "game");
				eventList.add(eventPackage);
			}
		}

		eventPackages = new LinkedHashSet<EventPackage>(eventList);
		LOGGER.debug("EventPackages: " + eventPackages);
		final EventsPackage eps = new EventsPackage();
		eps.addEvents(eventPackages);

		LOGGER.info("Exiting getAllSportsGame()");
		return eps;
	}

	/**
	 * 
	 * @return
	 * @throws BatchException
	 */
	public EventsPackage forceGetAllSports() throws BatchException {
		LOGGER.info("Entering forceGetAllSports()");
		Set<EventPackage> eventPackages = null;
		final List<EventPackage> eventList = new ArrayList<EventPackage>();

		// Make sure cache is refreshed
		invalidateCache();
		checkRefresh();

		if (EVENT_PACKAGES == null || EVENT_PACKAGES.size() == 0) {
			EVENT_PACKAGES = loadFromSite();
			if (EVENT_PACKAGES == null || EVENT_PACKAGES.size() == 0) {
				EVENT_PACKAGES = loadFromSite();
			}
		}

		final TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
		final Calendar today = Calendar.getInstance(timeZone);
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.set(Calendar.MILLISECOND, 0);
		LOGGER.debug("today: " + today.getTime());

		for (EventPackage ep : EVENT_PACKAGES) {
			if (today.getTime().equals(ep.getEventdatetime())
				|| ep.getEventdatetime().after(today.getTime())) {
				final EventPackage eventPackage = processSport(ep, "first");
				eventList.add(eventPackage);
			}
		}

		for (EventPackage ep : EVENT_PACKAGES) {
			if (today.getTime().equals(ep.getEventdatetime())
				|| ep.getEventdatetime().after(today.getTime())) {
				final EventPackage eventPackage = processSport(ep, "second");
				eventList.add(eventPackage);
			}
		}

		for (EventPackage ep : EVENT_PACKAGES) {
			if (today.getTime().equals(ep.getEventdatetime())
				|| ep.getEventdatetime().after(today.getTime())) {
				final EventPackage eventPackage = processSport(ep, "game");
				eventList.add(eventPackage);
			}
		}

		eventPackages = new LinkedHashSet<EventPackage>(eventList);
		LOGGER.debug("EventPackages: " + eventPackages);
		final EventsPackage eps = new EventsPackage();
		eps.addEvents(eventPackages);

		LOGGER.info("Exiting forceGetAllSports()");
		return eps;
	}

	/**
	 * 
	 * @param sportsInsightsEventId
	 * @param lineType 1=spread, 2=ml, 3=total
	 * @param gameType 0=game, 1=1h, 2=2h, 3=3h, 6=live
	 * @param sportId 1=NFL, 2=NBA, 3=MLB, 8=WNBA, 11=NCAAF
	 * @return
	 * @exception
	 */
	public ClosingLine getClosingLine(Integer sportsInsightsEventId, Integer lineType, Integer gameType, Integer sportId) throws BatchException {
		LOGGER.info("Entering getClosingLine()");
		ClosingLine closingLine = null;

		try {
			String url = "https://sportsinsights.actionnetwork.com/api/lines/" + sportsInsightsEventId + "/history/?sportsbookId=2&lineType=" + lineType.toString() + "&gameType=" + gameType.toString() + "&sportId=" + sportId.toString();
			LOGGER.info("url: " + url);
			List<NameValuePair> retValue = httpClientWrapper.getJSONSite(url, "AN_SESSION_TOKEN_V1=" + token, setupHeader(false));
			String json = httpClientWrapper.getCookiesAndXhtml(retValue);
			LOGGER.info("json: " + json);
			closingLine = sip.parseClosingLine(json);
		} catch (BatchException be) {
			LOGGER.error(be);
			throw be;
		}

		LOGGER.info("Entering getClosingLine()");
		return closingLine;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#loginToSite(java.lang.String, java.lang.String)
	 */
	@Override
	public String loginToSite(String username, String password) throws BatchException {
		LOGGER.info("Entering loginToSite()");
		String xhtml = "";

		// HTTP Header Options
		List<NameValuePair> headerValuePairs = new ArrayList<NameValuePair>(1);
		headerValuePairs.add(new BasicNameValuePair("Host", "sportsinsights.actionnetwork.com"));
		headerValuePairs.add(new BasicNameValuePair("Referer", this.httpClientWrapper.getHost() + "login"));
		headerValuePairs.add(new BasicNameValuePair("Authorization", "basic bW9qYXhzdmVudHVyZXNAZ21haWwuY29tOmFjdGlvbjE="));

		String data1 = "{}";
		List<NameValuePair> siteLogin = this.httpClientWrapper.postJSONSite("https://api.actionnetwork.com/web/v1/user/loginnew", null, data1, headerValuePairs);

		for (NameValuePair nvp : siteLogin) {
			LOGGER.debug("Nvp: " + nvp);

			if (nvp.getName().equals("json")) {
				MAP_DATA = sip.parseLogin(nvp.getValue());
				token = MAP_DATA.get("token");
			}
		}

		headerValuePairs = new ArrayList<NameValuePair>(1);
		headerValuePairs.add(new BasicNameValuePair("Host", "sportsinsights.actionnetwork.com"));
		headerValuePairs.add(new BasicNameValuePair("Referer", "https://www.actionnetwork.com/login?src_brand=sportsinsights&redirecturl=sportsinsights.actionnetwork.com/dashboard"));
		headerValuePairs.add(new BasicNameValuePair("User-Agent", "ActionWeb"));

		List<NameValuePair> validate = this.httpClientWrapper.getJSONSite("https://www.actionnetwork.com/validate?AN_SESSION_TOKEN_V1=" + token, "", headerValuePairs);
		for (NameValuePair nvp : validate) {
			LOGGER.debug("Nvp: " + nvp);
		}

		LOGGER.info("Exiting loginToSite()");
		return xhtml;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.sportsinsights.Updatable#loadFromSite()
	 */
	@Override
    protected List<EventPackage> loadFromSite() {
		List<EventPackage> ep = null;
		try {
			// Setup HTTP Client with proxy if there is one
			this.httpClientWrapper.setupHttpClient("Chicago");

			String json = commonSetup();
			LOGGER.debug("json: " + json);
			ep = sip.getAllEvents(json);
		} catch (Exception e) {
			LOGGER.error(e);
		}
//		LOGGER.debug("ep: " + ep);
		return ep;
    }

    /**
     * Implement this function to hotswap the data in memory after it was loaded from site
     *
     * @param data
     */
	@Override
    protected void updateData(List<EventPackage> data) {
		EVENT_PACKAGES = data;
    }

	/**
	 * 
	 * @param source
	 * @param sportType
	 * @return
	 */
	private EventPackage processSport(EventPackage source, String sportType) {
		LOGGER.info("Entering getSport()");

		final EventPackage eventPackage = new EventPackage();
		eventPackage.setSportsInsightsEventId(source.getSportsInsightsEventId());
		eventPackage.setDateofevent(source.getDateofevent());
		eventPackage.setEventdatetime(source.getEventdatetime());
		eventPackage.setEventtype(sportType);
		eventPackage.setPeriodNumber(source.getPeriodNumber());
		eventPackage.setPeriodType(source.getPeriodType());
		eventPackage.setTimeofevent(source.getTimeofevent());
		eventPackage.setSporttype(source.getSporttype());

		// Teams
		final TeamPackage sourceTeam1 = source.getTeamone();
		final TeamPackage sourceTeam2 = source.getTeamtwo();
		final TeamPackage team1 = new TeamPackage();
		final TeamPackage team2 = new TeamPackage();

		team1.setDateofevent(sourceTeam1.getDateofevent());
		team1.setEventdatetime(sourceTeam1.getEventdatetime());
		team1.setEventid(sourceTeam1.getEventid());
		team1.setId(sourceTeam1.getId());
		team1.setScore(sourceTeam1.getScore());
		team1.setTeam(sourceTeam1.getTeam());
		team1.setTimeofevent(sourceTeam1.getTimeofevent());
		team1.setPitcher(sourceTeam1.getPitcher());
		team1.setTeamshort(sourceTeam1.getTeamshort());

		team2.setDateofevent(sourceTeam2.getDateofevent());
		team2.setEventdatetime(sourceTeam2.getEventdatetime());
		team2.setEventid(sourceTeam2.getEventid());
		team2.setId(sourceTeam2.getId());
		team2.setScore(sourceTeam2.getScore());
		team2.setTeam(sourceTeam2.getTeam());
		team2.setTimeofevent(sourceTeam2.getTimeofevent());
		team2.setPitcher(sourceTeam2.getPitcher());
		team2.setTeamshort(sourceTeam2.getTeamshort());

		if (sportType.contains("first")) {
			eventPackage.setLinetype("first");
			eventPackage.setId(1000 + source.getId());
			team1.setEventid("1" + sourceTeam1.getEventid());
			team1.setId(1000 + sourceTeam1.getId());
			team2.setEventid("1" + sourceTeam2.getEventid());
			team2.setId(1000 + sourceTeam2.getId());
		} else if (sportType.contains("second")) {
			eventPackage.setLinetype("second");
			eventPackage.setId(2000 + source.getId());
			team1.setEventid("2" + sourceTeam1.getEventid());
			team1.setId(2000 + sourceTeam1.getId());
			team2.setEventid("2" + sourceTeam2.getEventid());
			team2.setId(2000 + sourceTeam2.getId());
		} else if (sportType.contains("third")) {
			eventPackage.setLinetype("thrid");
			eventPackage.setId(3000 + source.getId());
			team1.setEventid("3" + sourceTeam1.getEventid());
			team1.setId(3000 + sourceTeam1.getId());
			team2.setEventid("3" + sourceTeam2.getEventid());
			team2.setId(3000 + sourceTeam2.getId());
		} else {
			eventPackage.setLinetype("game");
			eventPackage.setId(source.getId());
			team1.setEventid(sourceTeam1.getEventid());
			team1.setId(sourceTeam1.getId());
			team2.setEventid(sourceTeam2.getEventid());
			team2.setId(sourceTeam2.getId());
		}
		eventPackage.setId(sourceTeam1.getId());
		eventPackage.setTeamone(team1);
		eventPackage.setTeamtwo(team2);

		LOGGER.info("Exiting getSport()");
		return eventPackage;
	}

	/**
	 * 
	 * @return
	 */
	private String commonSetup() throws BatchException {
		LOGGER.info("Entering commonSetup()");

		// Setup HTTP Client with proxy if there is one
		this.httpClientWrapper.setupHttpClient("None");

		// Authenticate first
		loginToSite(this.httpClientWrapper.getUsername(), this.httpClientWrapper.getPassword());

		List<NameValuePair> retValue = httpClientWrapper.getSitePage("https://sportsinsights.actionnetwork.com/live-odds", "AN_SESSION_TOKEN_V1=" + token, setupHeader(false));
		String json = httpClientWrapper.getCookiesAndXhtml(retValue);
		LOGGER.debug("json: " + json);
		String userId = sip.getUserId(json);

		int idx = userId.indexOf(",actionId");
		if (idx != -1) {
			userId = userId.substring(0, idx);
		}
		LOGGER.debug("userId: " + userId);

		retValue = httpClientWrapper.getJSONSite("https://sportsinsights.actionnetwork.com/" + SETUP_URL + userId, "AN_SESSION_TOKEN_V1=" + token, setupHeader(false));
		json = httpClientWrapper.getCookiesAndXhtml(retValue);
		LOGGER.debug("json: " + json);
		final List<EventPackage> ep = sip.getAllEvents(json);

		for (NameValuePair nvp : retValue) {
			LOGGER.debug("Nvp: " + nvp);
		}
		json = httpClientWrapper.getCookiesAndXhtml(retValue);

		LOGGER.debug("JSON: " + json);
		LOGGER.info("Exiting commonSetup()");
		return json;
	}

	/**
	 * 
	 * @return
	 */
	public Set<EventPackage> getAllEventsNoCache() {
		LOGGER.info("Entering getAllEventsNoCache()");
		Set<EventPackage> eps = null;

		try {
			invalidateCache();
			checkRefresh();

			if (EVENT_PACKAGES == null || EVENT_PACKAGES.size() == 0) {
				EVENT_PACKAGES = loadFromSite();
				if (EVENT_PACKAGES == null || EVENT_PACKAGES.size() == 0) {
					EVENT_PACKAGES = loadFromSite();
				}
			}
			eps = new LinkedHashSet<EventPackage>(EVENT_PACKAGES);
		} catch (Exception e) {
			LOGGER.error("Exception", e);
		}

		LOGGER.info("Exiting getAllEvents()");
		return eps;
	}
	
	/**
	 * 
	 * @param linetype
	 * @param sportsInsightsEventId
	 * @param ep
	 * @return
	 * @throws BatchException
	 */
	public EventPackage getScores(String linetype, Integer sportsInsightsEventId, EventPackage ep) throws BatchException {
		LOGGER.info("Entering getScores()");

		try {
			// Setup HTTP Client with proxy if there is one
			this.httpClientWrapper.setupHttpClient("None");
	
			// Authenticate first
			loginToSite(this.httpClientWrapper.getUsername(), this.httpClientWrapper.getPassword());

			// https://sportsinsights.actionnetwork.com/api/events/3538950/boxscore/
			List<NameValuePair> retValue = httpClientWrapper.getJSONSite("https://sportsinsights.actionnetwork.com/api/events/" + sportsInsightsEventId + "/boxscore/", null, setupHeader(false));
			String json = httpClientWrapper.getCookiesAndXhtml(retValue);
			ep = sip.parseScores(linetype, json, ep);
			final String sportType = ep.getSporttype();
			if (sportType != null && "mlb".equals(sportType.toLowerCase())) {
				sip.parseMlbScore(json, ep);
			}
		} catch (BatchException be) {
			LOGGER.error(be);
			throw be;
		}

		LOGGER.info("Entering getScores()");
		return ep;
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
