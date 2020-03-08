package com.ticketadvantage.services.dao.email.drh;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;

import com.ticketadvantage.services.dao.email.EmailProcessor;
import com.ticketadvantage.services.dao.sites.sportsinsights.SportsInsightsSite;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.BaseScrapper;
import com.ticketadvantage.services.model.EmailEvent;
import com.ticketadvantage.services.model.EventsPackage;
import com.ticketadvantage.services.model.PendingEvent;

public class DrhProcessSite extends EmailProcessor {
	private static final Logger LOGGER = Logger.getLogger(DrhProcessSite.class);
	private static final SportsInsightsSite SportsInsightSite = new SportsInsightsSite("https://sportsinsights.actionnetwork.com", "mojaxsventures@gmail.com", "action1");
	private static final DrhParser DRHP = new DrhParser();
	private static EventsPackage EventsPackage;

	static {
		try {
			EventsPackage = SportsInsightSite.getNextDaySport("mlblines");
			DRHP.setEventsPackage(EventsPackage);
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}
	}

	/**
	 * 
	 * @param emailaddress
	 * @param password
	 */
	public DrhProcessSite(String emailaddress, String password) {
		super("Drh", emailaddress, password);
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final DrhProcessSite drhProcessSite = new DrhProcessSite("ticketadvantage@gmail.com", "ticket123");
			final EmailEvent emailContainer = new EmailEvent();
			emailContainer.setBccemail("");
			emailContainer.setBodyhtml("<div dir=\"ltr\">4 plays starting in 2-3 minutes</div>");
			emailContainer.setBodytext("4 plays starting in 2-3 minutes");
			emailContainer.setDatecreated(new Date());
			emailContainer.setDatereceived(new Date());
			emailContainer.setDatesent(new Date());
			emailContainer.setEmailname("null");
			emailContainer.setFromemail("Dr H <drh@drhpicks.com>");
			emailContainer.setInet("null");
			emailContainer.setMessagenum(499);
			emailContainer.setSubject("Release warning");
			emailContainer.setToemail("J D <drh@drhpicks.com>");
			boolean found = drhProcessSite.determineEmailProcessor(emailContainer, null);
			LOGGER.debug("found: " + found);

/*			final Set<PendingEvent> pendingEvents = drhProcessSite.getPendingBets("user", emailContainer, null);

			if (pendingEvents != null && !pendingEvents.isEmpty()) {
				final Iterator<PendingEvent> itr = pendingEvents.iterator();
				while (itr != null && itr.hasNext()) {
					final PendingEvent pendingEvent = itr.next();
					System.out.println("PendingEvent: " + pendingEvent);
				}
			}
*/
		} catch (BatchException be) {
			be.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.email.EmailProcessor#determineEmailProcessor(com.ticketadvantage.services.model.EmailEvent, java.util.List)
	 */
	@Override
	public boolean determineEmailProcessor(EmailEvent emailContainer, List<BaseScrapper> baseScrappers) throws BatchException {
		LOGGER.info("Entering determineEmailProcessor()");
		boolean retValue = false;

		//
		// Dr H <drh@drhpicks.com>
		//
		LOGGER.debug("EmailContainer: " + emailContainer);
		if (emailContainer != null && emailContainer.getFromemail() != null) {
			String from = emailContainer.getFromemail();
			LOGGER.debug("from: " + from);

			if (from.contains("<") && from.contains(">")) {
				int findex = from.indexOf("<");
				int bindex = from.indexOf(">");
				if (findex != -1 && bindex != -1) {
					from = from.substring(findex + 1, bindex);
					LOGGER.debug("From: " + from);
					
					// Dr H <drh@drhpicks.com>
					if (from.equals("drh@drhpicks.com") || from.equals("john.miller@lasdigitaltech.com")) {
						LOGGER.debug("Found!");
						retValue = true;
					}
				}
			} else if (from.equals("drh@drhpicks.com") || from.equals("john.miller@lasdigitaltech.com")) {
				LOGGER.debug("Found!");
				retValue = true;				
			}
		}

		LOGGER.info("Exiting determineEmailProcessor()");
		return retValue;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.email.EmailProcessor#processEmail(com.ticketadvantage.services.model.EmailEvent, java.util.List)
	 */
	@Override
	public synchronized boolean processEmail(EmailEvent emailContainer, List<BaseScrapper> baseScrappers) throws BatchException {
		LOGGER.info("Entering processEmail()");
		LOGGER.info("Entering processEmail()");
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.email.EmailProcessor#getPendingBets(java.lang.String, com.ticketadvantage.services.model.EmailEvent, java.util.List)
	 */
	@Override
	public synchronized Set<PendingEvent> getPendingBets(String pendingType, EmailEvent emailContainer, List<BaseScrapper> baseScrappers) throws BatchException {
		LOGGER.info("Entering getPendingBets()");
		LOGGER.error("pendingType: " + pendingType);
		LOGGER.error("EmailEvent: " + emailContainer);

		String bodyText = emailContainer.getBodytext();
		if (bodyText != null) {
			bodyText = bodyText.replace("#", "");
		} else {
			bodyText = emailContainer.getBodyhtml();
			if (bodyText != null && bodyText.length() > 0) {
				bodyText = Jsoup.parse(bodyText).text().trim();
			}
		}
		LOGGER.debug("bodyText: " + bodyText);

		// Get the Users Data
		final Set<PendingEvent> pendingEvents = DRHP.parsePendingBets(bodyText, accountname, accountid);
		if (pendingEvents != null && pendingEvents.size() > 0) {
			for (PendingEvent pe : pendingEvents) {
				LOGGER.debug("PE: " + pe);
			}
		}

		// Send to only those who want the text
		if (baseScrappers != null && baseScrappers.size() > 0) {
			for (BaseScrapper scrapper : baseScrappers) {
				LOGGER.error("scrapper: " + scrapper);
				if (scrapper.getSendtextforaccount()) {
					final String phoneNumber = scrapper.getMobiletext();
					if (phoneNumber != null && phoneNumber.length() > 0) {
						super.sendText(phoneNumber, bodyText);
					}
				}
			}
		}

		LOGGER.info("Exiting getPendingBets()");
		return pendingEvents;
	}
}