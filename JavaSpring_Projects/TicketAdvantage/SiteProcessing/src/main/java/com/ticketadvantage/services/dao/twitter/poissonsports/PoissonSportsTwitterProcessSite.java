/**
 * 
 */
package com.ticketadvantage.services.dao.twitter.poissonsports;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.twitter.TwitterProcessor;
import com.ticketadvantage.services.email.SendText;
import com.ticketadvantage.services.email.ticketadvantage.TicketAdvantageGmailOath;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.BaseScrapper;
import com.ticketadvantage.services.model.PendingEvent;
import com.ticketadvantage.services.model.TwitterTweet;
import com.ticketadvantage.services.twitter.TwitterFeed;

/**
 * 
 * @author jmiller
 *
 */
public class PoissonSportsTwitterProcessSite extends TwitterProcessor {
	private static final Logger LOGGER = Logger.getLogger(PoissonSportsTwitterProcessSite.class);
	private static final PoissonSportsTwitterParser PP = new PoissonSportsTwitterParser();
	private static final PoissonSportsSite PoissonSportsSite = new PoissonSportsSite("https://t.co", null, null);
	
	/**
	 * 
	 */
	public PoissonSportsTwitterProcessSite(String inet, String customerid, String screenname, String handleid) {
		super("PoissonSportsTwitter", inet, customerid, screenname, handleid);
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final PoissonSportsTwitterProcessSite proCapSportsProcessSite = new PoissonSportsTwitterProcessSite("WizardsOfOdds", "@PoissonSports", "@PoissonSports", "@PoissonSports");
			final TwitterFeed twitterFeed = new TwitterFeed();
			final List<TwitterTweet> twitterTweets = twitterFeed.getUserTweets(proCapSportsProcessSite.getScreenname());
			LOGGER.error("TwitterProcessor: " + proCapSportsProcessSite.getScreenname());
			final Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
			LOGGER.error("cal.getTime(): " + cal.getTime());
			cal.set(Calendar.HOUR, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			cal.set(Calendar.AM_PM, Calendar.AM);

			for (TwitterTweet tt : twitterTweets) {
				LOGGER.error("TwitterTweet: " + tt);
				LOGGER.error("tt.getTweetdate(): " + tt.getTweetdate());
				LOGGER.error("cal.getTime(): " + cal.getTime());

				if (tt.getTweetdate().after(cal.getTime())) {
					LOGGER.error("after");
				}
			}
/*		
			final TwitterTweet twitterContainer = new TwitterTweet();
			twitterContainer.setDatecreated(new Date());
			twitterContainer.setTweetid(new Long(2));
			twitterContainer.setTweetdate(new Date());
			twitterContainer.setScreenname("@PoissonSports");
			twitterContainer.setUsername("@PoissonSports");
			twitterContainer.setTweettext("CBB TOTALS 01/26\n" + 
					"\n" + 
					"https://t.co/clRtYwzf2g");
*/
/*			twitterContainer.setTweettext("CBB TOTALS 01/20\r\n" + 
					"\r\n" + 
					"823 Duquesne/George Washington UNDER 137.0\r\n" + 
					"819 Providence/Marquette OVER 138.5\r\n" + 
					"817 Florida State/Boston College UNDER 148.0\r\n" + 
					"\r\n" + 
					"Odds from Bookmaker");
*/
/*
			if (proCapSportsProcessSite.determineTwitterProcessor(twitterContainer, null)) {
				final Set<PendingEvent> pendingEvents = proCapSportsProcessSite.getPendingBets("user", twitterContainer, null);
	
				if (pendingEvents != null && !pendingEvents.isEmpty()) {
					final Iterator<PendingEvent> itr = pendingEvents.iterator();
	
					while (itr != null && itr.hasNext()) {
						final PendingEvent pendingEvent = itr.next();
						LOGGER.error("PendingEvent: " + pendingEvent);
					}
				}
			}
*/
		} catch (Throwable be) {
			LOGGER.error(be.getMessage(), be);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.twitter.TwitterProcessor#determineTwitterProcessor(com.ticketadvantage.services.model.TwitterTweet, java.util.List)
	 */
	@Override
	public boolean determineTwitterProcessor(TwitterTweet twitterContainer, List<BaseScrapper> baseScrappers) throws BatchException {
		LOGGER.info("Entering determineEmailProcessor()");
		boolean retValue = false;
		
		//
		// @PoissonSports
		//
		if (twitterContainer != null && twitterContainer.getScreenname() != null) {
			final String screen = twitterContainer.getScreenname();
			
			if (screen.equals("@PoissonSports")) {
				retValue = true;
			}

			// bit.ly/2FBoLzI
			if (twitterContainer.getTweettext().contains("t.co") || twitterContainer.getTweettext().contains("bit.ly")) {
				final String twittertext = twitterContainer.getTweettext();		
				final String[] tokens = twittertext.split("\\n");

				// Loop through the lines
				for (String line: tokens) {
					if (line.contains("http")) {
						final String ttext = PoissonSportsSite.getPlays(line.trim());
						twitterContainer.setTweettext(ttext);
					}
				}
			}
		}

		// Send text
		if (retValue) {
			try {
				if (baseScrappers != null) {
					for (BaseScrapper scrapper : baseScrappers) {
						if (accountname != null && accountid != null && (scrapper.getScrappername().toLowerCase().contains(accountname.toLowerCase()) || scrapper.getScrappername().toLowerCase().contains(accountid.toLowerCase())) && scrapper.getSendtextforaccount()) {
							final TicketAdvantageGmailOath TicketAdvantageGmailOath = new TicketAdvantageGmailOath();
							final String accessToken = TicketAdvantageGmailOath.getAccessToken();
							final SendText sendText = new SendText();
							sendText.setOAUTH2_TOKEN(accessToken);
	
							final String bodyText = twitterContainer.getTweettext();
							sendText.sendTextWithMessage("9132195234@vtext.com", bodyText);
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
	 * @see com.ticketadvantage.services.dao.twitter.TwitterProcessor#processTweet(com.ticketadvantage.services.model.TwitterTweet, java.util.List)
	 */
	@Override
	public boolean processTweet(TwitterTweet twitterContainer, List<BaseScrapper> baseScrappers) throws BatchException {
		LOGGER.info("Entering processEmail()");
		boolean retValue = false;

		if (determineHasPendingBets(twitterContainer)) {
			retValue = true;
		}

		LOGGER.info("Entering processEmail()");
		return retValue;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.twitter.TwitterProcessor#getPendingBets(java.lang.String, com.ticketadvantage.services.model.TwitterTweet, java.util.List)
	 */
	@Override
	public Set<PendingEvent> getPendingBets(String pendingType, 
			TwitterTweet twitterContainer,
			List<BaseScrapper> baseScrappers) throws BatchException {
		LOGGER.info("Entering getPendingBets()");
		Set<PendingEvent> pendingEvents = null;

		// Check for a valid email
		if (twitterContainer != null) {
			String tweetText = twitterContainer.getTweettext();
			LOGGER.error("tweetText: " + tweetText);

			if (tweetText != null && tweetText.length() > 0) {
				LOGGER.debug("accountname: " + accountname);
				LOGGER.debug("accountid: " + accountid);
				pendingEvents = PP.parsePendingBets(tweetText, accountname, accountid);
			}

			if (pendingEvents != null && !pendingEvents.isEmpty()) {
				final Iterator<PendingEvent> itr = pendingEvents.iterator();

				// Loop through all the games
				while (itr.hasNext()) {
					final PendingEvent pendingEvent = itr.next();

					// Get the game details
					boolean found = getGameDetails(pendingEvent);

					if (!found) {
						try {
							if (baseScrappers != null) {
								for (BaseScrapper scrapper : baseScrappers) {
									if (accountname != null && accountid != null && 
										(scrapper.getScrappername().toLowerCase().contains(accountname.toLowerCase()) || scrapper.getScrappername().toLowerCase().contains(accountid.toLowerCase())) && 
										scrapper.getSendtextforaccount()) {
										final TicketAdvantageGmailOath TicketAdvantageGmailOath = new TicketAdvantageGmailOath();
										final String accessToken = TicketAdvantageGmailOath.getAccessToken();
										final SendText sendText = new SendText();
										sendText.setOAUTH2_TOKEN(accessToken);
										sendText.sendTextWithMessage(scrapper.getMobiletext(), "NOT FOUND: " + pendingEvent.getTeam());
									}
								}
							}
						} catch (Throwable t) {
							LOGGER.error(t.getMessage(), t);
						}
					}
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
	private boolean determineHasPendingBets(TwitterTweet twitterContainer) {
		LOGGER.info("Entering determineHasPendingBets()");
		boolean retValue = false;

		// Check if we want to process this action
		if (twitterContainer.getTweettext() != null && twitterContainer.getTweettext().length() > 0) {
			String text = twitterContainer.getTweettext();
			LOGGER.debug("text: " + text);
			retValue = PP.isPendingBet(text, accountname, accountid);
		}

		LOGGER.info("Exiting determineHasPendingBets()");
		return retValue;
	}
}