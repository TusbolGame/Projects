/**
 * 
 */
package com.ticketadvantage.services.db;

import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.sites.sportsinsights.SportsInsightsSite;
import com.ticketadvantage.services.model.EventPackage;
import com.ticketadvantage.services.model.EventsPackage;

/**
 * @author jmiller
 *
 */
public class EventsDB extends BaseDB {
	private static final Logger LOGGER = Logger.getLogger(EventsDB.class);
	private static EventsPackage AllGames;
	private SportsInsightsSite sportsInsightSite = new SportsInsightsSite("https://account.sportsinsights.com", "mojaxsventures@gmail.com", "Jaxson17");

	/**
	 * 
	 */
	public EventsDB() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * 
	 * @param searchString
	 * @return
	 */
	public EventsPackage findEvents(String searchString) {
		LOGGER.info("Entering findEvents()");
		LOGGER.debug("searchString: " + searchString);
		final EventsPackage ep = new EventsPackage();
		
		// Get all events
		AllGames = sportsInsightSite.getAllEvents();

		final Set<EventPackage> games = AllGames.getEvents();
		final Iterator<EventPackage> itr = games.iterator();
		while (itr != null && itr.hasNext()) {
			final EventPackage eventPackage = itr.next();
			LOGGER.debug("EventPackage: " + eventPackage);

			// Check if string is in any of the packages
			if (eventPackage.search().contains(searchString)) {
				ep.addEvent(eventPackage);
			}
		}

		LOGGER.debug("EventsPackage: " + ep);
		LOGGER.info("Exiting findEvents()");
		return ep;
	}
}