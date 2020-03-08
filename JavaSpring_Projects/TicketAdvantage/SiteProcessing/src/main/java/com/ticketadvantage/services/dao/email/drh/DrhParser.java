package com.ticketadvantage.services.dao.email.drh;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.email.EmailParser;
import com.ticketadvantage.services.dao.sites.sportsinsights.SportsInsightsSite;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.EventContainer;
import com.ticketadvantage.services.model.EventsPackage;
import com.ticketadvantage.services.model.PendingEvent;
import com.ticketadvantage.services.util.DrHBullshit;

public class DrhParser extends EmailParser {
	private static final Logger LOGGER = Logger.getLogger(DrhParser.class);
	private static DrHBullshit DRHBULLSHIT = new DrHBullshit();
	private EventsPackage eventsPackage;

	/**
	 * 
	 */
	public DrhParser() {
		super();
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final SportsInsightsSite sportsInsightSite = new SportsInsightsSite("https://sportsinsights.actionnetwork.com", "mojaxsventures@gmail.com", "action1");
			final EventsPackage eventsPackage = sportsInsightSite.getNextDaySport("mlblines");
//			final EventsPackage eventsPackage = sportsInsightSite.getDayOfSport("mlblines");

			final DrhParser drhParser = new DrhParser();
			drhParser.setEventsPackage(eventsPackage);

//			final Set<PendingEvent> pendingEvents = proCapSportsParser.parsePendingBets("369 Toledo/Nevada over 65", "procap","1");
//			final Set<PendingEvent> pendingEvents = proCapSportsParser.parsePendingBets("Bee-Y-U/Mi$si$sippi $t ov3r Forty-6.5", "procap","1");
//			final Set<PendingEvent> pendingEvents = drhParser.parsePendingBets("MIK0lAS minus one thirty versus DAVI3S", "drh","1");
//			final Set<PendingEvent> pendingEvents = drhParser.parsePendingBets("Ma41E +140 vs Chatw00d", "drh","1");
//			final Set<PendingEvent> pendingEvents = drhParser.parsePendingBets("Zimerman/Sheidz un 9.5 -110\n", "drh","1");
//			final Set<PendingEvent> pendingEvents = drhParser.parsePendingBets("Estr4adaa/More greater than ten minus one oh five", "drh", "1");
//			final Set<PendingEvent> pendingEvents = drhParser.parsePendingBets("G'onzalesz plus 130 v Linn", "drh", "1");
//			final Set<PendingEvent> pendingEvents = drhParser.parsePendingBets("Right/Montgomerie below 9.5 -105", "drh", "1");
//			final Set<PendingEvent> pendingEvents = drhParser.parsePendingBets("Fires/Lo-pez less than 9 minus 120", "drh", "1");
//			final Set<PendingEvent> pendingEvents = drhParser.parsePendingBets("G4RC14 -1O2 against H4M.ELS", "drh", "1");
//			final Set<PendingEvent> pendingEvents = drhParser.parsePendingBets("ROWARK -142 vs Harv3y", "drh", "1");
//			final Set<PendingEvent> pendingEvents = drhParser.parsePendingBets("Kuechell -137 vs Odori22i", "drh", "1");
//			final Set<PendingEvent> pendingEvents = drhParser.parsePendingBets("Skowgulnd +115 vs H3rn4nd3z", "drh", "1");
//			final Set<PendingEvent> pendingEvents = drhParser.parsePendingBets("Derg0m minus one forty versus T3h3r4n", "drh", "1");
//			final Set<PendingEvent> pendingEvents = drhParser.parsePendingBets("H0LLLADN/R1CHardZ over 8.5 -105", "drh", "1");
//			final Set<PendingEvent> pendingEvents = drhParser.parsePendingBets("SD P4DR3S +180", "drh", "1");
//			final Set<PendingEvent> pendingEvents = drhParser.parsePendingBets("Davrishh -124 v 4nd3rs0n", "drh", "1");
//			final Set<PendingEvent> pendingEvents = drhParser.parsePendingBets("Baylee/Matrin3z ovr 8 +100", "drh", "1");
//			final Set<PendingEvent> pendingEvents = drhParser.parsePendingBets("Ham3l/N0RR15 und 9 -110", "drh", "1");
//			final Set<PendingEvent> pendingEvents = drhParser.parsePendingBets("911 Rosss +130", "drh", "1");
//			final Set<PendingEvent> pendingEvents = drhParser.parsePendingBets("973 Rosss +130", "drh", "1");
//			final Set<PendingEvent> pendingEvents = drhParser.parsePendingBets("New-combe +140 against Synndergard", "drh", "1");
//			final Set<PendingEvent> pendingEvents = drhParser.parsePendingBets("Garvemann +130 v Pomrenanz", "drh", "1");
//			final Set<PendingEvent> pendingEvents = drhParser.parsePendingBets("Stratonn plus 1 forty v. Rayy", "drh", "1");
//			final Set<PendingEvent> pendingEvents = drhParser.parsePendingBets("Gasman/Boid above 9 -105", "drh", "1");
//			final Set<PendingEvent> pendingEvents = drhParser.parsePendingBets("Malhe|Daveiz higher then 8.5 -105", "drh", "1");
//			final Set<PendingEvent> pendingEvents = drhParser.parsePendingBets("H4PPY -180 vs K3N3Dy", "drh", "1");
//			final Set<PendingEvent> pendingEvents = drhParser.parsePendingBets("N1N3-7EVEN-4OUR Cay-hill below 9 minus 100", "drh", "1");
//			final Set<PendingEvent> pendingEvents = drhParser.parsePendingBets("#N1N3-2WO-N1N3 OVER 8.5 -115", "drh", "1");
			final Set<PendingEvent> pendingEvents = drhParser.parsePendingBets("H4PPY -180 vs K3N3Dy\n ", "drh", "1");

			if (pendingEvents != null && !pendingEvents.isEmpty()) {
				final Iterator<PendingEvent> itr = pendingEvents.iterator();
				while (itr != null && itr.hasNext()) {
					final PendingEvent pendingEvent = itr.next();
					LOGGER.error("PendingEvent: " + pendingEvent);
				}
			}
		} catch (BatchException be) {
			be.printStackTrace();
		}
	}

	/**
	 * @return the eventsPackage
	 */
	public EventsPackage getEventsPackage() {
		return eventsPackage;
	}

	/**
	 * @param eventsPackage the eventsPackage to set
	 */
	public void setEventsPackage(EventsPackage eventsPackage) {
		this.eventsPackage = eventsPackage;
	}


	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.email.EmailParser#parsePendingBets(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Set<PendingEvent> parsePendingBets(String body, String accountName, String accountId) throws BatchException {
		LOGGER.info("Entering parsePendingBets()");
		LOGGER.debug("body: " + body);
		LOGGER.debug("accountName: " + accountName);
		LOGGER.debug("accountId: " + accountId);
		final Set<PendingEvent> pendingEvents = new HashSet<PendingEvent>();
		final BufferedReader bufferedReader = new BufferedReader(new StringReader(body));
		final Stream<String> lines = bufferedReader.lines();

		lines.forEach(line -> {
			LOGGER.error("line: " + line);
			if (line != null && line.length() > 0) {
				line = line.trim();
	
				// Check for a game type
				EventContainer eventContainer = DRHBULLSHIT.parseTokens(line);
				if (eventContainer != null) {
					eventContainer = DRHBULLSHIT.determineEvent(eventsPackage, eventContainer, line);
					LOGGER.debug("eventContainer: " + eventContainer);
	
					final PendingEvent pendingEvent = new PendingEvent();
					pendingEvent.setAccountname(accountName);
					pendingEvent.setAccountid(accountId);
					pendingEvent.setCustomerid(accountId);
					pendingEvent.setInet(accountId);
					final Calendar now = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
	
					pendingEvent.setDateaccepted(now.getTime().toString());
					pendingEvent.setDatecreated(now.getTime());
					pendingEvent.setDatemodified(now.getTime());
					pendingEvent.setEventdate(eventContainer.getEventdate());
					pendingEvent.setEventtype(eventContainer.getEventtype());
					pendingEvent.setGamedate(eventContainer.getGamedate());
					pendingEvent.setGamesport(eventContainer.getGamesport());
					pendingEvent.setGametype(eventContainer.getGametype());
					pendingEvent.setJuice(eventContainer.getJuice());
					pendingEvent.setJuiceplusminus(eventContainer.getJuiceplusminus());
					pendingEvent.setLine(eventContainer.getLine());
					pendingEvent.setLineplusminus(eventContainer.getLineplusminus());
					pendingEvent.setLinetype(eventContainer.getLinetype());
					pendingEvent.setPitcher(eventContainer.getPitcher());
					pendingEvent.setRotationid(eventContainer.getRotationid());
					pendingEvent.setTeam(eventContainer.getTeam());
					pendingEvent.setTicketnum(String.valueOf(now.getTimeInMillis()));
					pendingEvent.setRisk("0");
					pendingEvent.setWin("0");
					pendingEvent.setDoposturl(false);
					pendingEvent.setPosturl("");
					pendingEvents.add(pendingEvent);
				}
			}
		});

		LOGGER.info("Exiting parsePendingBets()");
		return pendingEvents;
	}
}