/**
 * 
 */
package com.ticketadvantage.services.dao.email.metallica;

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
import com.ticketadvantage.services.model.EmailAccounts;
import com.ticketadvantage.services.model.EmailEvent;
import com.ticketadvantage.services.model.EmailScrapper;
import com.ticketadvantage.services.model.PendingEvent;

/**
 * 
 * @author jmiller
 *
 */
public class MetallicaEmailProcessSite extends EmailProcessor {
	private static final Logger LOGGER = Logger.getLogger(MetallicaEmailProcessSite.class);
	private static final MetallicaEmailParser PP = new MetallicaEmailParser();

	/**
	 * 
	 */
	public MetallicaEmailProcessSite(String emailaddress, String password) {
		super("MetallicaEmail", emailaddress, password);
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final MetallicaEmailProcessSite proCapSportsProcessSite = new MetallicaEmailProcessSite("ticketadvantage@gmail.com", "ticket123");
			proCapSportsProcessSite.setAccountname("690sports");
			proCapSportsProcessSite.setAccountid("POP402");
			final EmailEvent emailContainer = new EmailEvent();
			emailContainer.setBccemail("");
			emailContainer.setBodyhtml("");
			emailContainer.setBodytext("\t\t\t\t\tCustomer Id:	POP402\r\n\t\t\t\t\t\r\n" + 
					"\t\t\t\t\tInet:	690sports\r\n\t\t\t\t\t\r\n" + 
					"\t\t\t\t\tTicket #:	261912428\r\n\t\t\t\t\t\r\n" + 
					"\t\t\t\t\tAccepted:	Mar, 01 12:07 AM\r\n\t\t\t\t\t\r\n" + 
					"\t\t\t\t\tRisk/Win:	$330.00 to win $300.00\r\n\t\t\t\t\t\r\n" + 
					"\t\t\t\t\tScheduled:	Feb, 28 11:15 PM\r\n\t\t\t\t\t\r\n" + 
					"\t\t\t\t\tSport:	MLB - Exhibition\r\n\t\t\t\t\t\r\n" + 
					"\t\t\t\t\tNotes:	COGAPI, Placed this bet\r\n\t\t\t\t\t\r\n" + 
					"\t\t\t\t\tDescription	#936 - Texas Rangers ML (-115)");
			emailContainer.setDatecreated(new Date());
			emailContainer.setDatereceived(new Date());
			emailContainer.setDatesent(new Date());
			emailContainer.setEmailname("MetallicaEmail");
			emailContainer.setFromemail("mailservice@snotification.com");
			emailContainer.setInet("MetallicaEmail");
			emailContainer.setMessagenum(1);
			emailContainer.setSubject("BetTicker: VIP Customer Notification");
			emailContainer.setToemail("ticketadvantage@gmail.com");
			final Set<PendingEvent> pendingEvents = proCapSportsProcessSite.getPendingBets("user", emailContainer, null);

			if (pendingEvents != null && !pendingEvents.isEmpty()) {
				final Iterator<PendingEvent> itr = pendingEvents.iterator();

				while (itr != null && itr.hasNext()) {
					final PendingEvent pendingEvent = itr.next();
					LOGGER.error("PendingEvent: " + pendingEvent);
				}
			}
		} catch (BatchException be) {
			LOGGER.error(be.getMessage(), be);
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
		// Mail Service <cogservice@cog.cr>
		//
		if (emailContainer != null && emailContainer.getFromemail() != null) {
			String from = emailContainer.getFromemail();
			if (from.contains("<") && from.contains(">")) {
				int findex = from.indexOf("<");
				int bindex = from.indexOf(">");
				if (findex != -1 && bindex != -1) {
					from = from.substring(findex + 1, bindex);
					LOGGER.debug("From: " + from);
					if (from.equals("mailservice@snotification.com") || from.equals("cogservice@cog.cr") || from.equals("john.miller@wootechnologies.tech")) {
						retValue = true;
					}
				}
			} else if (emailContainer.getFromemail().equals("mailservice@snotification.com") || emailContainer.getFromemail().equals("cogservice@cog.cr") || emailContainer.getFromemail().equals("john.miller@wootechnologies.tech")) {
				retValue = true;				
			}
		}

		// Send text
		if (retValue) {
			try {
				for (BaseScrapper scrapper : baseScrappers) {
					if (scrapper instanceof EmailScrapper) {
						final EmailScrapper emailscrapper = (EmailScrapper)scrapper;
						final Set<EmailAccounts> sources = emailscrapper.getSources();

						if (sources != null && sources.size() > 0) {
							final EmailAccounts emailaccount = sources.iterator().next();
							if (accountname != null && 
								accountid != null && 
								accountname.equals(emailaccount.getInet()) && 
								accountid.equals(emailaccount.getAccountid()) && 
								(scrapper.getScrappername().toLowerCase().contains(accountname.toLowerCase()) || scrapper.getScrappername().toLowerCase().contains(accountid.toLowerCase())) && 
								scrapper.getSendtextforaccount()) {
								final TicketAdvantageGmailOath TicketAdvantageGmailOath = new TicketAdvantageGmailOath();
								final String accessToken = TicketAdvantageGmailOath.getAccessToken();
								final SendText sendText = new SendText();
								sendText.setOAUTH2_TOKEN(accessToken);
								String bodyText = emailContainer.getBodytext();
								if (bodyText == null || bodyText.length() == 0) {
									bodyText = emailContainer.getBodyhtml();
									bodyText = Jsoup.parse(bodyText).text();
								}

//								sendText.sendTextWithMessage(scrapper.getMobiletext(), bodyText);
							}
						}
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
			if (emailContainer.getBodytext() != null && emailContainer.getBodytext().length() > 0) {
				LOGGER.error("bodytext: " + emailContainer.getBodytext());
				LOGGER.error("text: " + accountname);
				LOGGER.error("accountname: " + accountid);
				pendingEvents = PP.parsePendingBets(emailContainer.getBodytext(), accountname, accountid);
				LOGGER.error("pendingEvents: " + pendingEvents);
				if (pendingEvents != null && !pendingEvents.isEmpty()) {
					final String inet = pendingEvents.iterator().next().getInet();
					LOGGER.error("inet: " + inet);
					emailContainer.setInet(inet);
				}
			} else if (emailContainer.getBodyhtml() != null && emailContainer.getBodyhtml().length() > 0) {
				final String html = emailContainer.getBodyhtml();
				final String text = Jsoup.parse(html).text();
				LOGGER.error("bodytext: " + text);
				LOGGER.error("text: " + accountname);
				LOGGER.error("accountname: " + accountid);
				pendingEvents = PP.parsePendingBets(text, accountname, accountid);
				LOGGER.error("pendingEvents: " + pendingEvents);
				if (pendingEvents != null && !pendingEvents.isEmpty()) {
					final String inet = pendingEvents.iterator().next().getInet();
					LOGGER.error("inet: " + inet);
					emailContainer.setInet(inet);
				}
			} else {
				LOGGER.error("bodytext: " + emailContainer.getSubject());
				LOGGER.error("text: " + accountname);
				LOGGER.error("accountname: " + accountid);
				pendingEvents = PP.parsePendingBets(emailContainer.getSubject(), accountname, accountid);
				LOGGER.error("pendingEvents: " + pendingEvents);
				if (pendingEvents != null && !pendingEvents.isEmpty()) {
					final String inet = pendingEvents.iterator().next().getInet();
					LOGGER.error("inet: " + inet);
					emailContainer.setInet(inet);
				}
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

		// Check if we want to process this action
		if (emailContainer.getBodytext() != null && emailContainer.getBodytext().length() > 0) {
			String text = emailContainer.getBodytext();
			retValue = PP.isPendingBet(text, accountname, accountid);
			LOGGER.error("retValue: " + retValue);
			LOGGER.error("text: " + text);
			LOGGER.error("accountname: " + accountname);
			LOGGER.error("accountid: " + accountid);
		} else if (emailContainer.getBodyhtml() != null && emailContainer.getBodyhtml().length() > 0) {
			String html = emailContainer.getBodyhtml();
			String text = Jsoup.parse(html).text();
			LOGGER.debug("text: " + text);
			retValue = PP.isPendingBet(text, accountname, accountid);
			LOGGER.error("retValue: " + retValue);
			LOGGER.error("text: " + text);
			LOGGER.error("accountname: " + accountname);
			LOGGER.error("accountid: " + accountid);
		} else {
			String subject = emailContainer.getSubject();
			LOGGER.debug("subject: " + subject);
			retValue = PP.isPendingBet(subject, accountname, accountid);
			LOGGER.error("retValue: " + retValue);
			LOGGER.error("text: " + subject);
			LOGGER.error("accountname: " + accountname);
			LOGGER.error("accountid: " + accountid);
		}

		LOGGER.info("Exiting determineHasPendingBets()");
		return retValue;
	}
}