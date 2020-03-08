/**
 * 
 */
package com.ticketadvantage.services.dao.email.everysport247;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;

import com.ticketadvantage.services.dao.email.EmailProcessor;
import com.ticketadvantage.services.dao.sites.tdsportseleven.TDSportsElevenProcessSite;
import com.ticketadvantage.services.email.SendText;
import com.ticketadvantage.services.email.ticketadvantage.TicketAdvantageGmailOath;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.BaseScrapper;
import com.ticketadvantage.services.model.EmailEvent;
import com.ticketadvantage.services.model.PendingEvent;

/**
 * 
 * @author jmiller
 *
 */
public class EverySport247EmailProcessSite extends EmailProcessor {
	private static final Logger LOGGER = Logger.getLogger(EverySport247EmailProcessSite.class);

	/**
	 * 
	 * @param emailaddress
	 * @param password
	 */
	public EverySport247EmailProcessSite(String emailaddress, String password) {
		super("EverySport247Email", emailaddress, password);
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final EverySport247EmailProcessSite esep = new EverySport247EmailProcessSite("doesnt@matter.com", "dontcare");
			final Set<PendingEvent> pendingEvents = esep.getPendingBets("email", new EmailEvent(), null);
			if (pendingEvents != null && !pendingEvents.isEmpty()) {
				Iterator<PendingEvent> itr = pendingEvents.iterator();
				while (itr.hasNext()) {
					System.out.println("PendingEvent: " + itr.next());
				}
			}
		} catch (Throwable t) {
			
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
		// alerts@notice247.info
		//
		if (emailContainer != null && emailContainer.getFromemail() != null) {
			String from = emailContainer.getFromemail();
			String bcc = emailContainer.getBccemail();
			if (from.contains("<") && from.contains(">")) {
				int findex = from.indexOf("<");
				int bindex = from.indexOf(">");
				if (findex != -1 && bindex != -1) {
					from = from.substring(findex + 1, bindex);
					LOGGER.debug("From: " + from);
					if (from.equals("alerts@notice247.info") || from.equals("john.miller@lvairductcare.com")) {
						retValue = true;
					}
				}
			} else if (bcc.contains("<") && bcc.contains(">")) {
				int findex = bcc.indexOf("<");
				int bindex = bcc.indexOf(">");
				if (findex != -1 && bindex != -1) {
					from = from.substring(findex + 1, bindex);
					LOGGER.debug("From: " + from);
					if (from.equals("alerts@notice247.info") || from.equals("john.miller@lvairductcare.com")) {
						retValue = true;
					}
				}
			} else if (emailContainer.getFromemail().equals("alerts@notice247.info") || emailContainer.getFromemail().equals("john.miller@lvairductcare.com")) {
				retValue = true;				
			}
		}

		LOGGER.debug("retValue: " + retValue);

		LOGGER.info("Exiting determineEmailProcessor()");
		return retValue;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.email.EmailProcessor#processEmail(com.ticketadvantage.services.model.EmailEvent, java.util.List)
	 */
	@Override
	public boolean processEmail(EmailEvent emailContainer, List<BaseScrapper> baseScrappers) throws BatchException {
		LOGGER.info("Entering processEmail()");
		boolean retValue = false;

		if (determineHasPendingBets(emailContainer)) {
			retValue = true;
		}

		LOGGER.info("Entering processEmail()");
		return retValue;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.email.EmailProcessor#getPendingBets(java.lang.String, com.ticketadvantage.services.model.EmailEvent, java.util.List)
	 */
	@Override
	public Set<PendingEvent> getPendingBets(String pendingType, EmailEvent emailContainer, List<BaseScrapper> baseScrappers) throws BatchException {
		LOGGER.info("Entering getPendingBets()");
		Set<PendingEvent> pendingEvents = null;

		// Check for a valid email
		if (emailContainer != null) {
			try {
				final TDSportsElevenProcessSite processSite = new TDSportsElevenProcessSite("http://everysport247.com", "WB236",
						"zoom", false, false);
			    processSite.getHttpClientWrapper().setupHttpClient("New York");
//			    processSite.getHttpClientWrapper().setupHttpClient("None");
			    processSite.setProcessTransaction(false);
			    processSite.setTimezone("ET");
			    processSite.loginToSite("WB236", "zoom");

				pendingEvents = processSite.getPendingBets(accountname, accountid, null);
				LOGGER.error("pendingEvents: " + pendingEvents);
			} catch (Throwable t) {
				LOGGER.error(t.getMessage(), t);
			}
		}

		LOGGER.info("Exiting getPendingBets()");
		return pendingEvents;
	}

	/**
	 * 
	 * @param emailContainer
	 * @return
	 */
	private boolean determineHasPendingBets(EmailEvent emailContainer) {
		LOGGER.info("Entering determineHasPendingBets()");
		boolean retValue = false;

		try {
			final TDSportsElevenProcessSite processSite = new TDSportsElevenProcessSite("http://everysport247.com", "WB236",
					"zoom", false, false);
		    processSite.getHttpClientWrapper().setupHttpClient("New York");
		    processSite.setProcessTransaction(false);
		    processSite.setTimezone("ET");
		    processSite.loginToSite("WB236", "zoom");

			final Set<PendingEvent> pendingEvents = processSite.getPendingBets("WB236", "zoom", null);
			if (pendingEvents != null && !pendingEvents.isEmpty()) {
				final Iterator<PendingEvent> itr = pendingEvents.iterator();
				while (itr.hasNext()) {
					LOGGER.error("PendingEvent: " + itr.next());
					retValue = true;
				}
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		LOGGER.error("retValue: " + retValue);

		LOGGER.info("Exiting determineHasPendingBets()");
		return retValue;
	}
}