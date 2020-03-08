/**
 * 
 */
package com.ticketadvantage.services.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.Accounts;
import com.ticketadvantage.services.model.TwitterAccounts;
import com.ticketadvantage.services.model.TwitterScrapper;

/**
 * @author jmiller
 *
 */
public class TwitterScrapperDB extends BaseDB {
	private static final Logger LOGGER = Logger.getLogger(TwitterScrapperDB.class);

	/**
	 * 
	 * @param startConnection
	 */
	public TwitterScrapperDB() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * 
	 * @return
	 * @throws BatchException
	 */
	public List<TwitterScrapper> findAll() throws BatchException {
		LOGGER.info("Entering findAll()");
		List<TwitterScrapper> scrappers = new ArrayList<TwitterScrapper>();
		TwitterScrapper scrapper = null;
		Connection conn = null;
		Statement stmt = null;
		ResultSet resultSet = null;

		try {
			conn = this.getConnection();
			stmt = conn.createStatement();
			StringBuffer sb = new StringBuffer(100);
			resultSet = stmt.executeQuery(sb.append("SELECT * FROM twitterscrapper s").toString());
	
			while (resultSet.next()) {
				LOGGER.debug("Found scrapper item");
				scrapper = new TwitterScrapper();
				scrapper.setDatecreated(resultSet.getDate("datecreated"));
				scrapper.setDatemodified(resultSet.getDate("datemodified"));
				scrapper.setId(resultSet.getLong("id"));
				scrapper.setMlindicator(resultSet.getString("mlindicator"));
				scrapper.setMlline(resultSet.getString("mlline"));
				scrapper.setMllineadjustment(resultSet.getString("mllineadjustment"));
				scrapper.setMlmaxamount(resultSet.getString("mlmaxamount"));
				scrapper.setOnoff(resultSet.getBoolean("onoff"));
				scrapper.setPullinginterval(resultSet.getString("pullinginterval"));
				scrapper.setScrappername(resultSet.getString("scrappername"));
				scrapper.setSpreadjuice(resultSet.getString("spreadjuice"));
				scrapper.setSpreadjuiceindicator(resultSet.getString("spreadjuiceindicator"));
				scrapper.setSpreadjuiceadjustment(resultSet.getString("spreadjuiceadjustment"));
				scrapper.setSpreadlineadjustment(resultSet.getString("spreadlineadjustment"));
				scrapper.setSpreadmaxamount(resultSet.getString("spreadmaxamount"));				
				scrapper.setTelegramnumber(resultSet.getString("telegramnumber"));
				scrapper.setTotaljuice(resultSet.getString("totaljuice"));
				scrapper.setTotaljuiceindicator(resultSet.getString("totaljuiceindicator"));
				scrapper.setTotaljuiceadjustment(resultSet.getString("totaljuiceadjustment"));
				scrapper.setTotallineadjustment(resultSet.getString("totallineadjustment"));
				scrapper.setTotalmaxamount(resultSet.getString("totalmaxamount"));
				scrapper.setUserid(resultSet.getLong("userid"));
				scrapper.setServernumber(resultSet.getInt("servernumber"));
				scrapper.setEnableretry(resultSet.getBoolean("enableretry"));
				scrapper.setFullfill(resultSet.getBoolean("fullfill"));
				scrapper.setOrderamount(resultSet.getInt("orderamount"));
				scrapper.setSendtextforaccount(resultSet.getBoolean("sendtextforaccount"));
				scrapper.setSendtextforgame(resultSet.getBoolean("sendtextforgame"));
				scrapper.setMlsourceamount(resultSet.getFloat("mlsourceamount"));
				scrapper.setSpreadsourceamount(resultSet.getFloat("spreadsourceamount"));
				scrapper.setTotalsourceamount(resultSet.getFloat("totalsourceamount"));

				scrapper.setMiddlerules(resultSet.getBoolean("middlerules"));
				scrapper.setCheckdupgame(resultSet.getBoolean("checkdupgame"));
				scrapper.setPlayotherside(resultSet.getBoolean("playotherside"));
				scrapper.setBestprice(resultSet.getBoolean("bestprice"));
				scrapper.setMobiletext(resultSet.getString("mobiletext"));
				scrapper.setFirstonoff(resultSet.getBoolean("firstonoff"));
				scrapper.setGameonoff(resultSet.getBoolean("gameonoff"));
				scrapper.setSecondonoff(resultSet.getBoolean("secondonoff"));
				scrapper.setThirdonoff(resultSet.getBoolean("thirdonoff"));
				scrapper.setNflmlonoff(resultSet.getBoolean("nflmlonoff"));
				scrapper.setNflspreadonoff(resultSet.getBoolean("nflspreadonoff"));
				scrapper.setNfltotalonoff(resultSet.getBoolean("nfltotalonoff"));
				scrapper.setNcaafmlonoff(resultSet.getBoolean("ncaafmlonoff"));
				scrapper.setNcaafspreadonoff(resultSet.getBoolean("ncaafspreadonoff"));
				scrapper.setNcaaftotalonoff(resultSet.getBoolean("ncaaftotalonoff"));
				scrapper.setNbamlonoff(resultSet.getBoolean("nbamlonoff"));
				scrapper.setNbaspreadonoff(resultSet.getBoolean("nbaspreadonoff"));
				scrapper.setNbatotalonoff(resultSet.getBoolean("nbatotalonoff"));
				scrapper.setNcaabmlonoff(resultSet.getBoolean("ncaabmlonoff"));
				scrapper.setNcaabspreadonoff(resultSet.getBoolean("ncaabspreadonoff"));
				scrapper.setNcaabtotalonoff(resultSet.getBoolean("ncaabtotalonoff"));
				scrapper.setWnbamlonoff(resultSet.getBoolean("wnbamlonoff"));
				scrapper.setWnbaspreadonoff(resultSet.getBoolean("wnbaspreadonoff"));
				scrapper.setWnbatotalonoff(resultSet.getBoolean("wnbatotalonoff"));
				scrapper.setNhlmlonoff(resultSet.getBoolean("nhlmlonoff"));
				scrapper.setNhlspreadonoff(resultSet.getBoolean("nhlspreadonoff"));
				scrapper.setNhltotalonoff(resultSet.getBoolean("nhltotalonoff"));
				scrapper.setMlbmlonoff(resultSet.getBoolean("mlbmlonoff"));
				scrapper.setMlbspreadonoff(resultSet.getBoolean("mlbspreadonoff"));
				scrapper.setMlbtotalonoff(resultSet.getBoolean("mlbtotalonoff"));
				scrapper.setInternationalbaseballspreadonoff(resultSet.getBoolean("internationalbaseballspreadonoff"));
				scrapper.setInternationalbaseballtotalonoff(resultSet.getBoolean("internationalbaseballtotalonoff"));
				scrapper.setInternationalbaseballmlonoff(resultSet.getBoolean("internationalbaseballmlonoff"));
				scrapper.setKeynumber(resultSet.getBoolean("keynumber"));
				scrapper.setHumanspeed(resultSet.getBoolean("humanspeed"));
				scrapper.setUnitsenabled(resultSet.getBoolean("unitsenabled"));
				scrapper.setLeanssenabled(resultSet.getBoolean("leanssenabled"));
				scrapper.setSpreadunit(resultSet.getDouble("spreadunit"));
				scrapper.setTotalunit(resultSet.getDouble("totalunit"));
				scrapper.setMlunit(resultSet.getDouble("mlunit"));
				scrapper.setSpreadlean(resultSet.getDouble("spreadlean"));
				scrapper.setTotallean(resultSet.getDouble("totallean"));
				scrapper.setMllean(resultSet.getDouble("mllean"));

				final String sqlSource = "select twitteraccountsta.id, twitteraccountsta.name, twitteraccountsta.inet, "
						+ "twitteraccountsta.screenname, twitteraccountsta.handleid, twitteraccountsta.sitetype, "
						+ "twitteraccountsta.isactive, twitteraccountsta.accountid FROM twitteraccountsta INNER JOIN twitterscrappersources ON twitteraccountsta.id = twitterscrappersources.twitteraccountsid where twitterscrappersources.twitterid = ?";
				final PreparedStatement stmtSources = conn.prepareStatement(sqlSource);
				stmtSources.setLong(1, scrapper.getId());
				final ResultSet resultSetSource = stmtSources.executeQuery();

				final Set<TwitterAccounts> setAccountsSources = new HashSet<TwitterAccounts>();
				while (resultSetSource.next()) {
					final TwitterAccounts twitterAccount = new TwitterAccounts();
					twitterAccount.setId(resultSetSource.getLong("id"));
					twitterAccount.setName(resultSetSource.getString("name"));
					twitterAccount.setInet(resultSetSource.getString("inet"));
					twitterAccount.setScreenname(resultSetSource.getString("screenname"));
					twitterAccount.setHandleid(resultSetSource.getString("handleid"));
					twitterAccount.setIsactive(resultSetSource.getBoolean("isactive"));
					twitterAccount.setAccountid(resultSetSource.getString("accountid"));
					twitterAccount.setSitetype(resultSetSource.getString("sitetype"));

					setAccountsSources.add(twitterAccount);
				}
				scrapper.setSources(setAccountsSources);

				if (resultSetSource != null) {
					resultSetSource.close();
				}
				if (stmtSources != null) {
					stmtSources.close();
				}

				final String sqlDestination = "select accountsta.id, accountsta.name, accountsta.username, accountsta.password, accountsta.spreadlimitamount, accountsta.mllimitamount, accountsta.totallimitamount, accountsta.url, accountsta.proxylocation, accountsta.isactive, accountsta.sitetype, accountsta.timezone, accountsta.ownerpercentage, accountsta.partnerpercentage, accountsta.hourbefore, accountsta.minutebefore, accountsta.hourafter, accountsta.minuteafter FROM accountsta INNER JOIN twitterscrapperdestinations ON accountsta.id = twitterscrapperdestinations.accountsid where twitterscrapperdestinations.twitterid = ?";
				final PreparedStatement stmtDestinations = conn.prepareStatement(sqlDestination);
				stmtDestinations.setLong(1, scrapper.getId());
				final ResultSet resultSetDestination = stmtDestinations.executeQuery();
				final Set<Accounts> setAccountsDestination = new HashSet<Accounts>();

				Accounts account = null;
				while (resultSetDestination.next()) {
					account = new Accounts();
					account.setId(resultSetDestination.getLong("id"));
					account.setName(resultSetDestination.getString("name"));
					account.setUsername(resultSetDestination.getString("username"));
					account.setPassword(resultSetDestination.getString("password"));
					account.setSpreadlimitamount(resultSetDestination.getInt("spreadlimitamount"));
					account.setMllimitamount(resultSetDestination.getInt("mllimitamount"));
					account.setTotallimitamount(resultSetDestination.getInt("totallimitamount"));
					account.setUrl(resultSetDestination.getString("url"));
					account.setProxylocation(resultSetDestination.getString("proxylocation"));
					account.setIsactive(resultSetDestination.getBoolean("isactive"));
					account.setSitetype(resultSetDestination.getString("sitetype"));
					account.setTimezone(resultSetDestination.getString("timezone"));
					account.setOwnerpercentage(resultSetDestination.getInt("ownerpercentage"));
					account.setPartnerpercentage(resultSetDestination.getInt("partnerpercentage"));
					account.setHourbefore(resultSetDestination.getString("hourbefore"));
					account.setMinutebefore(resultSetDestination.getString("minutebefore"));
					account.setHourafter(resultSetDestination.getString("hourafter"));
					account.setMinuteafter(resultSetDestination.getString("minuteafter"));

					setAccountsDestination.add(account);
				}
				scrapper.setDestinations(setAccountsDestination);

				if (resultSetDestination != null) {
					resultSetDestination.close();
				}
				if (stmtDestinations != null) {
					stmtDestinations.close();
				}

				final String sqlMiddle = "select accountsta.id, accountsta.name, accountsta.username, accountsta.password, accountsta.spreadlimitamount, accountsta.mllimitamount, accountsta.totallimitamount, accountsta.url, accountsta.proxylocation, accountsta.isactive, accountsta.sitetype, accountsta.timezone, accountsta.ownerpercentage, accountsta.partnerpercentage, accountsta.hourbefore, accountsta.minutebefore, accountsta.hourafter, accountsta.minuteafter FROM accountsta INNER JOIN twittermiddledestinations ON accountsta.id = twittermiddledestinations.accountsid where twittermiddledestinations.twitterid = ?";
				final PreparedStatement stmtMiddle = conn.prepareStatement(sqlMiddle);
				stmtMiddle.setLong(1, scrapper.getId());
				final ResultSet resultSetMiddle = stmtMiddle.executeQuery();
				final Set<Accounts> setAccountsMiddles = new HashSet<Accounts>();
				account = null;
				while (resultSetMiddle.next()) {
					account = new Accounts();
					account.setId(resultSetMiddle.getLong("id"));
					account.setName(resultSetMiddle.getString("name"));
					account.setUsername(resultSetMiddle.getString("username"));
					account.setPassword(resultSetMiddle.getString("password"));
					account.setSpreadlimitamount(resultSetMiddle.getInt("spreadlimitamount"));
					account.setMllimitamount(resultSetMiddle.getInt("mllimitamount"));
					account.setTotallimitamount(resultSetMiddle.getInt("totallimitamount"));
					account.setUrl(resultSetMiddle.getString("url"));
					account.setProxylocation(resultSetMiddle.getString("proxylocation"));
					account.setIsactive(resultSetMiddle.getBoolean("isactive"));
					account.setSitetype(resultSetMiddle.getString("sitetype"));
					account.setTimezone(resultSetMiddle.getString("timezone"));
					account.setOwnerpercentage(resultSetMiddle.getInt("ownerpercentage"));
					account.setPartnerpercentage(resultSetMiddle.getInt("partnerpercentage"));
					account.setHourbefore(resultSetMiddle.getString("hourbefore"));
					account.setMinutebefore(resultSetMiddle.getString("minutebefore"));
					account.setHourafter(resultSetMiddle.getString("hourafter"));
					account.setMinuteafter(resultSetMiddle.getString("minuteafter"));

					setAccountsMiddles.add(account);
				}
				scrapper.setTwittermiddledestinations(setAccountsMiddles);

				if (resultSetMiddle != null) {
					resultSetMiddle.close();
				}
				if (stmtMiddle != null) {
					stmtMiddle.close();
				}

				final String sqlOrder = "select accountsta.id, accountsta.name, accountsta.username, accountsta.password, accountsta.spreadlimitamount, accountsta.mllimitamount, accountsta.totallimitamount, accountsta.url, accountsta.proxylocation, accountsta.isactive, accountsta.sitetype, accountsta.timezone, accountsta.ownerpercentage, accountsta.partnerpercentage, accountsta.hourbefore, accountsta.minutebefore, accountsta.hourafter, accountsta.minuteafter FROM accountsta INNER JOIN twitterorderdestinations ON accountsta.id = twitterorderdestinations.accountsid where twitterorderdestinations.twitterid = ?";
				final PreparedStatement stmtOrder = conn.prepareStatement(sqlOrder);
				stmtOrder.setLong(1, scrapper.getId());
				final ResultSet resultSetOrder = stmtOrder.executeQuery();
				final Set<Accounts> setAccountsOrder = new HashSet<Accounts>();
				account = null;
				while (resultSetOrder.next()) {
					account = new Accounts();
					account.setId(resultSetOrder.getLong("id"));
					account.setName(resultSetOrder.getString("name"));
					account.setUsername(resultSetOrder.getString("username"));
					account.setPassword(resultSetOrder.getString("password"));
					account.setSpreadlimitamount(resultSetOrder.getInt("spreadlimitamount"));
					account.setMllimitamount(resultSetOrder.getInt("mllimitamount"));
					account.setTotallimitamount(resultSetOrder.getInt("totallimitamount"));
					account.setUrl(resultSetOrder.getString("url"));
					account.setProxylocation(resultSetOrder.getString("proxylocation"));
					account.setIsactive(resultSetOrder.getBoolean("isactive"));
					account.setSitetype(resultSetOrder.getString("sitetype"));
					account.setTimezone(resultSetOrder.getString("timezone"));
					account.setOwnerpercentage(resultSetOrder.getInt("ownerpercentage"));
					account.setPartnerpercentage(resultSetOrder.getInt("partnerpercentage"));
					account.setHourbefore(resultSetOrder.getString("hourbefore"));
					account.setMinutebefore(resultSetOrder.getString("minutebefore"));
					account.setHourafter(resultSetOrder.getString("hourafter"));
					account.setMinuteafter(resultSetOrder.getString("minuteafter"));

					setAccountsOrder.add(account);
				}
				scrapper.setTwitterorderdestinations(setAccountsOrder);
				if (resultSetOrder != null) {
					resultSetOrder.close();
				}
				if (stmtOrder != null) {
					stmtOrder.close();
				}

				LOGGER.debug("setAccountsSources.size(): " + setAccountsSources.size());

				// Make sure that destination and size have at least 1
				if (setAccountsSources.size() > 0) {
					// Add the Scrapper to the list
					scrappers.add(scrapper);
				}
			}
		} catch (SQLException sqle) {
			LOGGER.error(sqle.getMessage(), sqle);

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

		LOGGER.info("Exiting findAll()");
		return scrappers;
	}

	/**
	 * 
	 * @param id
	 * @return
	 * @throws BatchException
	 */
	public List<TwitterScrapper> findById(Long id) throws BatchException {
		LOGGER.info("Entering findById()");
		List<TwitterScrapper> scrappers = new ArrayList<TwitterScrapper>();
		TwitterScrapper scrapper = null;
		Connection conn = null;
		Statement stmt = null;
		ResultSet resultSet = null;

		try {
			conn = this.getConnection();
			stmt = conn.createStatement();
			StringBuffer sb = new StringBuffer(100);
			resultSet = stmt.executeQuery(sb.append("SELECT * FROM twitterscrapper s where id = " + id.toString()).toString());
	
			while (resultSet.next()) {
				LOGGER.debug("Found scrapper item");
				scrapper = new TwitterScrapper();
				scrapper.setDatecreated(resultSet.getDate("datecreated"));
				scrapper.setDatemodified(resultSet.getDate("datemodified"));
				scrapper.setId(resultSet.getLong("id"));
				scrapper.setMlindicator(resultSet.getString("mlindicator"));
				scrapper.setMlline(resultSet.getString("mlline"));
				scrapper.setMllineadjustment(resultSet.getString("mllineadjustment"));
				scrapper.setMlmaxamount(resultSet.getString("mlmaxamount"));
				scrapper.setOnoff(resultSet.getBoolean("onoff"));
				scrapper.setPullinginterval(resultSet.getString("pullinginterval"));
				scrapper.setScrappername(resultSet.getString("scrappername"));
				scrapper.setSpreadjuice(resultSet.getString("spreadjuice"));
				scrapper.setSpreadjuiceindicator(resultSet.getString("spreadjuiceindicator"));
				scrapper.setSpreadjuiceadjustment(resultSet.getString("spreadjuiceadjustment"));
				scrapper.setSpreadlineadjustment(resultSet.getString("spreadlineadjustment"));
				scrapper.setSpreadmaxamount(resultSet.getString("spreadmaxamount"));				
				scrapper.setTelegramnumber(resultSet.getString("telegramnumber"));
				scrapper.setTotaljuice(resultSet.getString("totaljuice"));
				scrapper.setTotaljuiceindicator(resultSet.getString("totaljuiceindicator"));
				scrapper.setTotaljuiceadjustment(resultSet.getString("totaljuiceadjustment"));
				scrapper.setTotallineadjustment(resultSet.getString("totallineadjustment"));
				scrapper.setTotalmaxamount(resultSet.getString("totalmaxamount"));
				scrapper.setUserid(resultSet.getLong("userid"));
				scrapper.setServernumber(resultSet.getInt("servernumber"));
				scrapper.setEnableretry(resultSet.getBoolean("enableretry"));
				scrapper.setFullfill(resultSet.getBoolean("fullfill"));
				scrapper.setOrderamount(resultSet.getInt("orderamount"));
				scrapper.setSendtextforaccount(resultSet.getBoolean("sendtextforaccount"));
				scrapper.setSendtextforgame(resultSet.getBoolean("sendtextforgame"));
				scrapper.setMlsourceamount(resultSet.getFloat("mlsourceamount"));
				scrapper.setSpreadsourceamount(resultSet.getFloat("spreadsourceamount"));
				scrapper.setTotalsourceamount(resultSet.getFloat("totalsourceamount"));

				scrapper.setMiddlerules(resultSet.getBoolean("middlerules"));
				scrapper.setCheckdupgame(resultSet.getBoolean("checkdupgame"));
				scrapper.setPlayotherside(resultSet.getBoolean("playotherside"));
				scrapper.setBestprice(resultSet.getBoolean("bestprice"));
				scrapper.setMobiletext(resultSet.getString("mobiletext"));
				scrapper.setFirstonoff(resultSet.getBoolean("firstonoff"));
				scrapper.setGameonoff(resultSet.getBoolean("gameonoff"));
				scrapper.setSecondonoff(resultSet.getBoolean("secondonoff"));
				scrapper.setThirdonoff(resultSet.getBoolean("thirdonoff"));
				scrapper.setNflmlonoff(resultSet.getBoolean("nflmlonoff"));
				scrapper.setNflspreadonoff(resultSet.getBoolean("nflspreadonoff"));
				scrapper.setNfltotalonoff(resultSet.getBoolean("nfltotalonoff"));
				scrapper.setNcaafmlonoff(resultSet.getBoolean("ncaafmlonoff"));
				scrapper.setNcaafspreadonoff(resultSet.getBoolean("ncaafspreadonoff"));
				scrapper.setNcaaftotalonoff(resultSet.getBoolean("ncaaftotalonoff"));
				scrapper.setNbamlonoff(resultSet.getBoolean("nbamlonoff"));
				scrapper.setNbaspreadonoff(resultSet.getBoolean("nbaspreadonoff"));
				scrapper.setNbatotalonoff(resultSet.getBoolean("nbatotalonoff"));
				scrapper.setNcaabmlonoff(resultSet.getBoolean("ncaabmlonoff"));
				scrapper.setNcaabspreadonoff(resultSet.getBoolean("ncaabspreadonoff"));
				scrapper.setNcaabtotalonoff(resultSet.getBoolean("ncaabtotalonoff"));
				scrapper.setWnbamlonoff(resultSet.getBoolean("wnbamlonoff"));
				scrapper.setWnbaspreadonoff(resultSet.getBoolean("wnbaspreadonoff"));
				scrapper.setWnbatotalonoff(resultSet.getBoolean("wnbatotalonoff"));
				scrapper.setNhlmlonoff(resultSet.getBoolean("nhlmlonoff"));
				scrapper.setNhlspreadonoff(resultSet.getBoolean("nhlspreadonoff"));
				scrapper.setNhltotalonoff(resultSet.getBoolean("nhltotalonoff"));
				scrapper.setMlbmlonoff(resultSet.getBoolean("mlbmlonoff"));
				scrapper.setMlbspreadonoff(resultSet.getBoolean("mlbspreadonoff"));
				scrapper.setMlbtotalonoff(resultSet.getBoolean("mlbtotalonoff"));
				scrapper.setInternationalbaseballspreadonoff(resultSet.getBoolean("internationalbaseballspreadonoff"));
				scrapper.setInternationalbaseballtotalonoff(resultSet.getBoolean("internationalbaseballtotalonoff"));
				scrapper.setInternationalbaseballmlonoff(resultSet.getBoolean("internationalbaseballmlonoff"));
				scrapper.setKeynumber(resultSet.getBoolean("keynumber"));
				scrapper.setHumanspeed(resultSet.getBoolean("humanspeed"));
				scrapper.setUnitsenabled(resultSet.getBoolean("unitsenabled"));
				scrapper.setSpreadunit(resultSet.getDouble("spreadunit"));
				scrapper.setTotalunit(resultSet.getDouble("totalunit"));
				scrapper.setMlunit(resultSet.getDouble("mlunit"));
				scrapper.setLeanssenabled(resultSet.getBoolean("leanssenabled"));
				scrapper.setSpreadlean(resultSet.getDouble("spreadlean"));
				scrapper.setTotallean(resultSet.getDouble("totallean"));
				scrapper.setMllean(resultSet.getDouble("mllean"));

				final String sqlSource = "select twitteraccountsta.id, twitteraccountsta.name, twitteraccountsta.inet, " + 
						"twitteraccountsta.screenname, twitteraccountsta.handleid, twitteraccountsta.sitetype, " + 
						"twitteraccountsta.isactive, twitteraccountsta.accountid FROM twitteraccountsta INNER JOIN twitterscrappersources ON twitteraccountsta.id = twitterscrappersources.twitteraccountsid where twitterscrappersources.twitterid = ?";
				final PreparedStatement stmtSources = conn.prepareStatement(sqlSource);
				stmtSources.setLong(1, scrapper.getId());
				final ResultSet resultSetSource = stmtSources.executeQuery();

				final Set<TwitterAccounts> setAccountsSources = new HashSet<TwitterAccounts>();
				while (resultSetSource.next()) {
					final TwitterAccounts twitterAccount = new TwitterAccounts();
					twitterAccount.setId(resultSetSource.getLong("id"));
					twitterAccount.setName(resultSetSource.getString("name"));
					twitterAccount.setInet(resultSetSource.getString("inet"));
					twitterAccount.setScreenname(resultSetSource.getString("screenname"));
					twitterAccount.setHandleid(resultSetSource.getString("handleid"));
					twitterAccount.setIsactive(resultSetSource.getBoolean("isactive"));
					twitterAccount.setAccountid(resultSetSource.getString("accountid"));
					twitterAccount.setSitetype(resultSetSource.getString("sitetype"));

					setAccountsSources.add(twitterAccount);
				}
				scrapper.setSources(setAccountsSources);

				if (resultSetSource != null) {
					resultSetSource.close();
				}
				if (stmtSources != null) {
					stmtSources.close();
				}

				final String sqlDestination = "select accountsta.id, accountsta.name, accountsta.username, accountsta.password, accountsta.spreadlimitamount, accountsta.mllimitamount, accountsta.totallimitamount, accountsta.url, accountsta.proxylocation, accountsta.isactive, accountsta.sitetype, accountsta.timezone, accountsta.ownerpercentage, accountsta.partnerpercentage, accountsta.hourbefore, accountsta.minutebefore, accountsta.hourafter, accountsta.minuteafter FROM accountsta INNER JOIN twitterscrapperdestinations ON accountsta.id = twitterscrapperdestinations.accountsid where twitterscrapperdestinations.twitterid = ?";
				final PreparedStatement stmtDestinations = conn.prepareStatement(sqlDestination);
				stmtDestinations.setLong(1, scrapper.getId());
				final ResultSet resultSetDestination = stmtDestinations.executeQuery();
				final Set<Accounts> setAccountsDestination = new HashSet<Accounts>();
				Accounts account = null;
				while (resultSetDestination.next()) {
					account = new Accounts();
					account.setId(resultSetDestination.getLong("id"));
					account.setName(resultSetDestination.getString("name"));
					account.setUsername(resultSetDestination.getString("username"));
					account.setPassword(resultSetDestination.getString("password"));
					account.setSpreadlimitamount(resultSetDestination.getInt("spreadlimitamount"));
					account.setMllimitamount(resultSetDestination.getInt("mllimitamount"));
					account.setTotallimitamount(resultSetDestination.getInt("totallimitamount"));
					account.setUrl(resultSetDestination.getString("url"));
					account.setProxylocation(resultSetDestination.getString("proxylocation"));
					account.setIsactive(resultSetDestination.getBoolean("isactive"));
					account.setSitetype(resultSetDestination.getString("sitetype"));
					account.setTimezone(resultSetDestination.getString("timezone"));
					account.setHourbefore(resultSetDestination.getString("hourbefore"));
					account.setMinutebefore(resultSetDestination.getString("minutebefore"));
					account.setHourafter(resultSetDestination.getString("hourafter"));
					account.setMinuteafter(resultSetDestination.getString("minuteafter"));

					setAccountsDestination.add(account);
				}
				scrapper.setDestinations(setAccountsDestination);

				if (resultSetDestination != null) {
					resultSetDestination.close();
				}
				if (stmtDestinations != null) {
					stmtDestinations.close();
				}

				final String sqlMiddle = "select accountsta.id, accountsta.name, accountsta.username, accountsta.password, accountsta.spreadlimitamount, accountsta.mllimitamount, accountsta.totallimitamount, accountsta.url, accountsta.proxylocation, accountsta.isactive, accountsta.sitetype, accountsta.timezone, accountsta.ownerpercentage, accountsta.partnerpercentage, accountsta.hourbefore, accountsta.minutebefore, accountsta.hourafter, accountsta.minuteafter FROM accountsta INNER JOIN twittermiddledestinations ON accountsta.id = twittermiddledestinations.accountsid where twittermiddledestinations.twitterid = ?";
				final PreparedStatement stmtMiddle = conn.prepareStatement(sqlMiddle);
				stmtMiddle.setLong(1, scrapper.getId());
				final ResultSet resultSetMiddle = stmtMiddle.executeQuery();
				final Set<Accounts> setAccountsMiddles = new HashSet<Accounts>();
				account = null;
				while (resultSetMiddle.next()) {
					account = new Accounts();
					account.setId(resultSetMiddle.getLong("id"));
					account.setName(resultSetMiddle.getString("name"));
					account.setUsername(resultSetMiddle.getString("username"));
					account.setPassword(resultSetMiddle.getString("password"));
					account.setSpreadlimitamount(resultSetMiddle.getInt("spreadlimitamount"));
					account.setMllimitamount(resultSetMiddle.getInt("mllimitamount"));
					account.setTotallimitamount(resultSetMiddle.getInt("totallimitamount"));
					account.setUrl(resultSetMiddle.getString("url"));
					account.setProxylocation(resultSetMiddle.getString("proxylocation"));
					account.setIsactive(resultSetMiddle.getBoolean("isactive"));
					account.setSitetype(resultSetMiddle.getString("sitetype"));
					account.setTimezone(resultSetMiddle.getString("timezone"));
					account.setHourbefore(resultSetMiddle.getString("hourbefore"));
					account.setMinutebefore(resultSetMiddle.getString("minutebefore"));
					account.setHourafter(resultSetMiddle.getString("hourafter"));
					account.setMinuteafter(resultSetMiddle.getString("minuteafter"));

					setAccountsMiddles.add(account);
				}
				scrapper.setTwittermiddledestinations(setAccountsMiddles);

				if (resultSetMiddle != null) {
					resultSetMiddle.close();
				}
				if (stmtMiddle != null) {
					stmtMiddle.close();
				}

				final String sqlOrder = "select accountsta.id, accountsta.name, accountsta.username, accountsta.password, accountsta.spreadlimitamount, accountsta.mllimitamount, accountsta.totallimitamount, accountsta.url, accountsta.proxylocation, accountsta.isactive, accountsta.sitetype, accountsta.timezone, accountsta.ownerpercentage, accountsta.partnerpercentage, accountsta.hourbefore, accountsta.minutebefore, accountsta.hourafter, accountsta.minuteafter FROM accountsta INNER JOIN twitterorderdestinations ON accountsta.id = twitterorderdestinations.accountsid where twitterorderdestinations.twitterid = ?";
				final PreparedStatement stmtOrder = conn.prepareStatement(sqlOrder);
				stmtOrder.setLong(1, scrapper.getId());
				final ResultSet resultSetOrder = stmtOrder.executeQuery();
				final Set<Accounts> setAccountsOrder = new HashSet<Accounts>();
				account = null;
				while (resultSetOrder.next()) {
					account = new Accounts();
					account.setId(resultSetOrder.getLong("id"));
					account.setName(resultSetOrder.getString("name"));
					account.setUsername(resultSetOrder.getString("username"));
					account.setPassword(resultSetOrder.getString("password"));
					account.setSpreadlimitamount(resultSetOrder.getInt("spreadlimitamount"));
					account.setMllimitamount(resultSetOrder.getInt("mllimitamount"));
					account.setTotallimitamount(resultSetOrder.getInt("totallimitamount"));
					account.setUrl(resultSetOrder.getString("url"));
					account.setProxylocation(resultSetOrder.getString("proxylocation"));
					account.setIsactive(resultSetOrder.getBoolean("isactive"));
					account.setSitetype(resultSetOrder.getString("sitetype"));
					account.setTimezone(resultSetOrder.getString("timezone"));
					account.setOwnerpercentage(resultSetOrder.getInt("ownerpercentage"));
					account.setPartnerpercentage(resultSetOrder.getInt("partnerpercentage"));
					account.setHourbefore(resultSetOrder.getString("hourbefore"));
					account.setMinutebefore(resultSetOrder.getString("minutebefore"));
					account.setHourafter(resultSetOrder.getString("hourafter"));
					account.setMinuteafter(resultSetOrder.getString("minuteafter"));

					setAccountsOrder.add(account);
				}
				scrapper.setTwitterorderdestinations(setAccountsOrder);

				if (resultSetOrder != null) {
					resultSetOrder.close();
				}
				if (stmtOrder != null) {
					stmtOrder.close();
				}

				LOGGER.debug("setAccountsSources.size(): " + setAccountsSources.size());

				// Make sure that destination and size have at least 1
				if (setAccountsSources.size() > 0) {
					// Add the Scrapper to the list
					scrappers.add(scrapper);
				}
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

		LOGGER.info("Exiting findById()");
		return scrappers;
	}
}