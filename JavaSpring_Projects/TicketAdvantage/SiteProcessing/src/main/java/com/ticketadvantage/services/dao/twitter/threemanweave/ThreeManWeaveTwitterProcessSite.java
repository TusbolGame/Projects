/**
 * 
 */
package com.ticketadvantage.services.dao.twitter.threemanweave;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.twitter.TwitterProcessor;
import com.ticketadvantage.services.email.SendText;
import com.ticketadvantage.services.email.ticketadvantage.TicketAdvantageGmailOath;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.BaseScrapper;
import com.ticketadvantage.services.model.PendingEvent;
import com.ticketadvantage.services.model.TwitterTweet;

/**
 * 
 * @author jmiller
 *
 */
public class ThreeManWeaveTwitterProcessSite extends TwitterProcessor {
	private static final Logger LOGGER = Logger.getLogger(ThreeManWeaveTwitterProcessSite.class);
	private static final ThreeManWeaveTwitterParser PP = new ThreeManWeaveTwitterParser();

	/**
	 * 
	 * @param inet
	 * @param customerid
	 * @param screenname
	 * @param handleid
	 */
	public ThreeManWeaveTwitterProcessSite(String inet, String customerid, String screenname, String handleid) {
		super("ThreeManWeaveTwitter", inet, customerid, screenname, handleid);
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final ThreeManWeaveTwitterProcessSite proCapSportsProcessSite = new ThreeManWeaveTwitterProcessSite("inet", "customerid", "screenname", "handleid");
			final TwitterTweet twitterContainer = new TwitterTweet();
			twitterContainer.setDatecreated(new Date());
			twitterContainer.setTweetid(new Long(2));
			twitterContainer.setTweetdate(new Date());
			twitterContainer.setScreenname("@3MW_CBB");
			twitterContainer.setUsername("@3MW_CBB");
			twitterContainer.setTweettext("It's been a favorite's paradise the last 10 days (which spells bad news for us)... Faves were 91-75 (55%) this past weekend, an extremely high clip....\n" + 
					"\n" + 
					"0-2 for us last night (150-112-7 YTD):\n" + 
					"\n" + 
					"UNCA +4.5\n" + 
					"SJSU +25.5\n" + 
					"\n" + 
					"6 replies 0 retweets 14 likes\n" + 
					"");

			final Set<PendingEvent> pendingEvents = proCapSportsProcessSite.getPendingBets("user", twitterContainer, null);

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
	 * @see com.ticketadvantage.services.dao.twitter.TwitterProcessor#determineTwitterProcessor(com.ticketadvantage.services.model.TwitterTweet, java.util.List)
	 */
	@Override
	public boolean determineTwitterProcessor(TwitterTweet twitterContainer, List<BaseScrapper> baseScrappers) throws BatchException {
		LOGGER.info("Entering determineEmailProcessor()");
		boolean retValue = false;
		
		//
		// @3MW_CBB
		//
		if (twitterContainer != null && twitterContainer.getScreenname() != null) {
			final String screen = twitterContainer.getScreenname();
			
			if (screen.equals("@3MW_CBB")) {
				retValue = true;
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
			if (twitterContainer.getTweettext() != null && twitterContainer.getTweettext().length() > 0) {
				pendingEvents = PP.parsePendingBets(twitterContainer.getTweettext(), accountname, accountid);
			}

			if (pendingEvents != null && !pendingEvents.isEmpty()) {
				final Iterator<PendingEvent> itr = pendingEvents.iterator();

				// Loop through all the games
				while (itr.hasNext()) {
					final PendingEvent pendingEvent = itr.next();
					LOGGER.debug("PendingEvent: " + pendingEvent);

					// Get the game details
					boolean found = getGameDetails(pendingEvent);

					if (!found) {
						try {
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