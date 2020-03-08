/**
 * 
 */
package com.ticketadvantage.services.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.TwitterTweet;

/**
 * @author jmiller
 *
 */
public class TwitterTweetDB extends BaseDB {
	private static final Logger LOGGER = Logger.getLogger(TwitterTweetDB.class);

	/**
	 * 
	 */
	public TwitterTweetDB() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * 
	 * @param emailEvent
	 * @return
	 * @throws BatchException
	 */
	public void persist(TwitterTweet twitterTweet) throws BatchException {
		LOGGER.info("Entering persist()");
		LOGGER.debug("TwitterTweet: " + twitterTweet);
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;		

		try {
			StringBuffer sb = new StringBuffer(200);
			// Write out the user to the users tables
			twitterTweet.setId(null);
			sb.append("INSERT into twittertweet (tweetid, tweettext, username, screenname, tweetdate, datecreated, datemodified) ")
					.append("VALUES (?, ?, ?, ?, ?, ?, ?)");

			conn = this.getConnection();
			stmt = conn.prepareStatement(sb.toString(), Statement.RETURN_GENERATED_KEYS);
			stmt.setLong(1, twitterTweet.getTweetid());
			stmt.setString(2, twitterTweet.getTweettext());
			stmt.setString(3, twitterTweet.getUsername());
			stmt.setString(4, twitterTweet.getScreenname());
			stmt.setTimestamp(5, new java.sql.Timestamp(twitterTweet.getTweetdate().getTime()));
			stmt.setTimestamp(6, new java.sql.Timestamp(twitterTweet.getDatecreated().getTime()));
			stmt.setTimestamp(7, new java.sql.Timestamp(twitterTweet.getDatemodified().getTime()));

			stmt.executeUpdate();
			resultSet = stmt.getGeneratedKeys();
			resultSet.next();
		} catch (SQLException sqle) {
			LOGGER.error(sqle);

			if (sqle.getMessage().contains("An I/O error occurred while sending to the backend")) {
				throw new BatchException(BatchErrorCodes.DB_CONNECTION_EXCEPTION,
						BatchErrorMessage.DB_CONNECTION_EXCEPTION, sqle.getMessage());
			} else {
				throw new BatchException(BatchErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,
						BatchErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION, sqle.getMessage());
			}
		} finally {
			try {
				closeStreams(conn, stmt, resultSet);
			} catch (Throwable t) {
				LOGGER.error(t.getMessage(), t);
			}
		}

		LOGGER.info("Exiting persist()");
	}

	/**
	 * 
	 * @param id
	 * @throws BatchException
	 */
	public void delete(Long id) throws BatchException {
		LOGGER.info("Entering delete()");
		LOGGER.debug("id: " + id);
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {
			String sqlEmailEvent = "SELECT tt.id FROM twittertweet tt WHERE tt.id = ?";
			conn = this.getConnection();
			stmt = conn.prepareStatement(sqlEmailEvent);
			stmt.setLong(1, id);
			resultSet = stmt.executeQuery();
	
			// Loop through the results
			Long pid = null;
			while (resultSet.next()) {
				pid = resultSet.getLong(1);
			}

		    if (resultSet != null) {
		    		resultSet.close();
			}
		    if (stmt != null) {
				stmt.close();
			}

			if (pid != null) {
				sqlEmailEvent = "DELETE FROM twittertweet where id = ?";
				stmt = conn.prepareStatement(sqlEmailEvent);
				stmt.setLong(1, pid);
				stmt.executeUpdate();
			    if (stmt != null) {
					stmt.close();
				}
			} else {
				throw new BatchException(BatchErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
						BatchErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION, "Exception with delete");
			}
		} catch (SQLException sqle) {
			LOGGER.error(sqle);

			if (sqle.getMessage().contains("An I/O error occurred while sending to the backend")) {
				throw new BatchException(BatchErrorCodes.DB_CONNECTION_EXCEPTION,
						BatchErrorMessage.DB_CONNECTION_EXCEPTION, sqle.getMessage());
			} else {
				throw new BatchException(BatchErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,
						BatchErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION, sqle.getMessage());
			}
		} finally {
			try {
				closeStreams(conn, stmt, resultSet);
			} catch (Throwable t) {
				LOGGER.error(t.getMessage(), t);
			}
		}

		LOGGER.info("Exiting delete()");
	}

	/**
	 * 
	 * @param tweetid
	 * @return
	 * @throws BatchException
	 */
	public TwitterTweet findTwitterTweetByTweetId(Long tweetid) throws BatchException {
		LOGGER.info("Entering findTwitterTweetByTweetId()");
		TwitterTweet twitterTweet = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {
			final String sqlDestination = "SELECT * FROM twittertweet tt WHERE tt.tweetid = ?";
			conn = this.getConnection();
			stmt = conn.prepareStatement(sqlDestination);
			stmt.setLong(1, tweetid);
			resultSet = stmt.executeQuery();

			// Loop through the results
			while (resultSet.next()) {
				twitterTweet = new TwitterTweet();
				twitterTweet.setId(resultSet.getLong("id"));
				twitterTweet.setTweetid(resultSet.getLong("tweetid"));
				twitterTweet.setScreenname(resultSet.getString("screenname"));
				twitterTweet.setTweetdate(resultSet.getDate("tweetdate"));
			}
		} catch (SQLException sqle) {
			LOGGER.error(sqle);

			if (sqle.getMessage().contains("An I/O error occurred while sending to the backend")) {
				throw new BatchException(BatchErrorCodes.DB_CONNECTION_EXCEPTION,
						BatchErrorMessage.DB_CONNECTION_EXCEPTION, sqle.getMessage());
			} else {
				throw new BatchException(BatchErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,
						BatchErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION, sqle.getMessage());
			}
		} finally {
			try {
				closeStreams(conn, stmt, resultSet);
			} catch (Throwable t) {
				LOGGER.error(t.getMessage(), t);
			}
		}

		LOGGER.info("Exiting findTwitterTweetByTweetId()");
		return twitterTweet;
	}
}