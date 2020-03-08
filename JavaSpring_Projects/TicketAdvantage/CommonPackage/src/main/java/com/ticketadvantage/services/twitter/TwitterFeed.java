/**
 * 
 */
package com.ticketadvantage.services.twitter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.ticketadvantage.services.model.TwitterTweet;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

/**
 * @author jmiller
 *
 */
public class TwitterFeed {
	private static final Logger LOGGER = org.apache.log4j.Logger.getLogger(TwitterFeed.class);
	private static final Twitter twitter = TwitterFactory.getSingleton();

	/**
	 * 
	 */
	public TwitterFeed() {
		super();

		final AccessToken accessTokenObject = new AccessToken("1085056545799299072-XQmWJduQfa2yAXbu0sksnldV6fSFTH", "FDodqf9oC235S7DKKT1JMs7CU10K2mnsuxX51agDAnKJB");
		twitter.setOAuthConsumer("EJNSIhUggC7Chrmjg8o4rzwJC", "EJ9VSfoYHhykfzHjYfYfz0qHMxf1IMWSpsX3xwCXvGvRyKdfW7");
		twitter.setOAuthAccessToken(accessTokenObject);
	}

	/**
	 * 
	 * @param accessToken
	 * @param tokenSecret
	 * @param consumerKey
	 * @param consumerSecret
	 */
	public TwitterFeed(String accessToken, String tokenSecret, String consumerKey, String consumerSecret) {
		super();

		final AccessToken accessTokenObject = new AccessToken("1085056545799299072-XQmWJduQfa2yAXbu0sksnldV6fSFTH", "FDodqf9oC235S7DKKT1JMs7CU10K2mnsuxX51agDAnKJB");
		twitter.setOAuthConsumer("EJNSIhUggC7Chrmjg8o4rzwJC", "EJ9VSfoYHhykfzHjYfYfz0qHMxf1IMWSpsX3xwCXvGvRyKdfW7");
		twitter.setOAuthAccessToken(accessTokenObject);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final TwitterFeed twitterFeed = new TwitterFeed("accessToken", "tokenSecret", "consumerKey", "consumerSecret");
//		final List<TwitterTweet> twitterDatas = twitterFeed.getUserTweets("@jerryxpicks");
		final List<TwitterTweet> twitterDatas = twitterFeed.getUserTweets("@3MW_CBB");
//		final List<TwitterTweet> twitterDatas = twitterFeed.getUserTweets("@OddsWizards");

		for (TwitterTweet td : twitterDatas) {
			LOGGER.error("TwitterData: " + td);
		}
	}

	/**
	 * 
	 * @param screenName
	 * @return
	 */
	public List<TwitterTweet> getUserTweets(String screenName) {
		final List<TwitterTweet> twitterDataList = new ArrayList<TwitterTweet>();

		try {
		    final List<Status> statuses = twitter.getUserTimeline(screenName);
		    LOGGER.debug("statuses: " + statuses);

		    for (Status status : statuses) {
		    		LOGGER.debug("Status: " + status);

		    		final TwitterTweet twitterData = new TwitterTweet();
		    		twitterData.setTweetid(status.getId());
		    		twitterData.setScreenname(screenName);
		    		twitterData.setTweettext(status.getText());
		    		twitterData.setUsername(status.getUser().getName());
		    		twitterData.setTweetdate(status.getCreatedAt());
		    		twitterData.setDatecreated(new Date());
		    		twitterData.setDatemodified(new Date());

		    		twitterDataList.add(twitterData);
		    }
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		return twitterDataList;
	}
}