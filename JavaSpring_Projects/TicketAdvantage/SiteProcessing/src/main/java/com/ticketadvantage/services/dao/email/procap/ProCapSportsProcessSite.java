package com.ticketadvantage.services.dao.email.procap;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;

import com.ticketadvantage.services.dao.email.EmailProcessor;
import com.ticketadvantage.services.email.SendText;
import com.ticketadvantage.services.email.ticketadvantage.TicketAdvantageGmailOath;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.BaseScrapper;
import com.ticketadvantage.services.model.EmailEvent;
import com.ticketadvantage.services.model.PendingEvent;

public class ProCapSportsProcessSite extends EmailProcessor {
	private static final Logger LOGGER = Logger.getLogger(ProCapSportsProcessSite.class);
	private static final ProCapSportsParser PP = new ProCapSportsParser();

	/**
	 * 
	 * @param emailaddress
	 * @param password
	 */
	public ProCapSportsProcessSite(String emailaddress, String password) {
		super("ProCapSports", emailaddress, password);
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final ProCapSportsProcessSite proCapSportsProcessSite = new ProCapSportsProcessSite("procapsports@gmail.com", "ticket123");
			final EmailEvent emailContainer = new EmailEvent();
			emailContainer.setBccemail("");
			emailContainer.setBodyhtml("");
			emailContainer.setBodytext("La $alle/Fordham und3r 140.5");
			emailContainer.setDatecreated(new Date());
			emailContainer.setDatereceived(new Date());
			emailContainer.setDatesent(new Date());
			emailContainer.setEmailname("Procap");
			emailContainer.setFromemail("procapsports@gmail.com");
			emailContainer.setInet("Procap");
			emailContainer.setMessagenum(1);
			emailContainer.setSubject("La $alle/Fordham und3r 140.5");
			emailContainer.setToemail("ticketadvantage@gmail.com");
			final Set<PendingEvent> pendingEvents = proCapSportsProcessSite.getPendingBets("user", emailContainer, null);

			if (pendingEvents != null && !pendingEvents.isEmpty()) {
				final Iterator<PendingEvent> itr = pendingEvents.iterator();
				while (itr != null && itr.hasNext()) {
					final PendingEvent pendingEvent = itr.next();
					System.out.println("PendingEvent: " + pendingEvent);
				}
			}
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
		// Procap Sports <procapsports@gmail.com>
		//
		if (emailContainer != null && emailContainer.getFromemail() != null) {
			String from = emailContainer.getFromemail();
			if (from.contains("<") && from.contains(">")) {
				int findex = from.indexOf("<");
				int bindex = from.indexOf(">");
				if (findex != -1 && bindex != -1) {
					from = from.substring(findex + 1, bindex);
					LOGGER.debug("From: " + from);
					if (from.equals("procapsports@gmail.com") || from.equals("john.miller@wootechnologies.tech")) {
						LOGGER.debug("Found!");
						retValue = true;
					}
				}
			} else if (emailContainer.getFromemail().equals("procapsports@gmail.com") || emailContainer.getFromemail().equals("john.miller@wootechnologies.tech")) {
				LOGGER.debug("Found!");
				retValue = true;				
			}
		}

		// Send text
		if (retValue) {
			try {
				for (BaseScrapper scrapper : baseScrappers) {
					if (accountname != null && accountid != null && (scrapper.getScrappername().toLowerCase().contains(accountname.toLowerCase()) || scrapper.getScrappername().toLowerCase().contains(accountid.toLowerCase())) && scrapper.getSendtextforaccount()) {
						TicketAdvantageGmailOath TicketAdvantageGmailOath = new TicketAdvantageGmailOath();
						final String accessToken = TicketAdvantageGmailOath.getAccessToken();
						final SendText sendText = new SendText();
						sendText.setOAUTH2_TOKEN(accessToken);
						String bodyText = emailContainer.getBodytext();
						if (bodyText == null || bodyText.length() == 0) {
							bodyText = emailContainer.getBodyhtml();
							bodyText = Jsoup.parse(bodyText).text();
						}
						sendText.sendTextWithMessage(scrapper.getMobiletext(), bodyText);
					}
				}
			} catch (Throwable t) {
				LOGGER.error(t.getMessage(), t);
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
	public boolean processEmail(EmailEvent emailContainer, List<BaseScrapper> baseScrappers) throws BatchException {
		LOGGER.info("Entering processEmail()");
		boolean retValue = false;

		if (determineHasPendingBets(emailContainer)) {
			LOGGER.error("Setting retValue to true");
			retValue = true;
		}

		LOGGER.debug("retValue: " + retValue);
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
		LOGGER.error("pendingType: " + pendingType);
		LOGGER.error("EmailEvent: " + emailContainer);
		Set<PendingEvent> pendingEvents = null;

		if (emailContainer != null) {
			final String bodyText = emailContainer.getBodytext();
			if (bodyText != null && bodyText.length() > 0) {
				pendingEvents = PP.parsePendingBets(bodyText, accountname, accountid);
			} else {
				String bodyHtml = emailContainer.getBodyhtml();
				if (bodyHtml != null && bodyHtml.length() > 0) {
					bodyHtml = Jsoup.parse(bodyHtml).text().trim();
					pendingEvents = PP.parsePendingBets(bodyHtml, accountname, accountid);
				}
			}

			if (pendingEvents != null && !pendingEvents.isEmpty()) {
				// Get the game details
				getGameDetails(pendingEvents);
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
		String bodyText = null;
		String subject = null;
		LOGGER.error("emailContainer.getBodytext(): " + emailContainer.getBodytext() + "END");
		LOGGER.error("emailContainer.getSubject(): " + emailContainer.getSubject() + "END");
		LOGGER.error("emailContainer.getBodyhtml(): " + emailContainer.getBodyhtml() + "END");

		// Check if we want to process this action
		if (emailContainer.getBodytext() != null && emailContainer.getBodytext().length() > 0 &&
			emailContainer.getSubject() != null && emailContainer.getSubject().length() > 0) {
			bodyText = emailContainer.getBodytext().trim();
			subject = emailContainer.getSubject().trim();

			// Check if they match
			if (bodyText.equals(subject)) {
				LOGGER.error("bodyText is equal to subject");
				retValue = true;
			}
		} else if (emailContainer.getBodyhtml() != null && emailContainer.getBodyhtml().length() > 0 &&
			emailContainer.getSubject() != null && emailContainer.getSubject().length() > 0) {
			String html = emailContainer.getBodyhtml();
			bodyText = Jsoup.parse(html).text().trim();
			subject = emailContainer.getSubject().trim();

			// Check if they match
			if (bodyText.equals(subject)) {
				LOGGER.error("bodyText/html is equal to subject");
				retValue = true;
			}
		}

		LOGGER.info("Entering determineHasPendingBets()");
		return retValue;
	}
}