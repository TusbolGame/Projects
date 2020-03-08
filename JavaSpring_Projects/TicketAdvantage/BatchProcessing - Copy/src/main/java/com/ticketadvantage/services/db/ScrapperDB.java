/**
 * 
 */
package com.ticketadvantage.services.db;

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
import com.ticketadvantage.services.model.Scrapper;

/**
 * @author jmiller
 *
 */
public class ScrapperDB extends BaseDB {
	private static final Logger LOGGER = Logger.getLogger(ScrapperDB.class);

	/**
	 * 
	 * @param startConnection
	 */
	public ScrapperDB(boolean startConnection) {
		super(startConnection);
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
	public List<Scrapper> findAll() throws BatchException {
		LOGGER.info("Entering findAll()");
		List<Scrapper> scrappers = new ArrayList<Scrapper>();
		Scrapper scrapper = null;
		Statement stmt = null;
		ResultSet resultSet = null;

		try {
			stmt = conn.createStatement();
			StringBuffer sb = new StringBuffer(100);
			resultSet = stmt.executeQuery(sb.append("SELECT s.id, s.mllineadjustment, s.mlmaxamount, s.onoff, s.spreadjuice, ")
					.append("s.spreadjuiceindicator, s.spreadlineadjustment, s.spreadmaxamount, ")
					.append("s.totaljuice, s.totaljuiceindicator, s.totallineadjustment, s.totalmaxamount, ")
					.append("s.userid FROM Scrapper s").toString());
	
			while (resultSet.next()) {
				scrapper = new Scrapper();

				scrapper.setId(resultSet.getLong("id"));
				scrapper.setMllineadjustment(resultSet.getString("mllineadjustment"));
				scrapper.setMlmaxamount(resultSet.getString("mlmaxamount"));
				scrapper.setOnoff(resultSet.getBoolean("onoff"));
				scrapper.setSpreadjuice(resultSet.getString("spreadjuice"));
				scrapper.setSpreadjuiceindicator(resultSet.getString("spreadjuiceindicator"));
				scrapper.setSpreadlineadjustment(resultSet.getString("spreadlineadjustment"));
				scrapper.setSpreadmaxamount(resultSet.getString("spreadmaxamount"));
				scrapper.setTotaljuice(resultSet.getString("totaljuice"));
				scrapper.setTotaljuiceindicator(resultSet.getString("totaljuiceindicator"));
				scrapper.setTotallineadjustment(resultSet.getString("totallineadjustment"));
				scrapper.setTotalmaxamount(resultSet.getString("totalmaxamount"));
				scrapper.setUserid(resultSet.getLong("userid"));

				final String sqlDestination = "select accountsta.id, accountsta.name, accountsta.username, accountsta.password, accountsta.spreadlimitamount, accountsta.mllimitamount, accountsta.totallimitamount, accountsta.url, accountsta.proxylocation, accountsta.isactive, accountsta.sitetype, accountsta.timezone FROM accountsta INNER JOIN scrapperdestinations ON accountsta.id = scrapperdestinations.accountsid where scrapperdestinations.scrapperid = ?";
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

					setAccountsDestination.add(account);
				}
				scrapper.setDestinations(setAccountsDestination);
				if (resultSetDestination != null) {
					resultSetDestination.close();
				}
				if (stmtDestinations != null) {
					stmtDestinations.close();
				}

				final String sqlSource = "select accountsta.id, accountsta.name, accountsta.username, accountsta.password, accountsta.spreadlimitamount, accountsta.mllimitamount, accountsta.totallimitamount, accountsta.url, accountsta.proxylocation, accountsta.isactive, accountsta.sitetype, accountsta.timezone FROM accountsta INNER JOIN scrappersources ON accountsta.id = scrappersources.accountsid where scrappersources.scrapperid = ?";
				final PreparedStatement stmtSources = conn.prepareStatement(sqlSource);
				stmtSources.setLong(1, scrapper.getId());
				final ResultSet resultSetSource = stmtSources.executeQuery();
				final Set<Accounts> setAccountsSources = new HashSet<Accounts>();
				account = null;
				while (resultSetSource.next()) {
					account = new Accounts();

					account.setId(resultSetSource.getLong("id"));
					account.setName(resultSetSource.getString("name"));
					account.setUsername(resultSetSource.getString("username"));
					account.setPassword(resultSetSource.getString("password"));
					account.setSpreadlimitamount(resultSetSource.getInt("spreadlimitamount"));
					account.setMllimitamount(resultSetSource.getInt("mllimitamount"));
					account.setTotallimitamount(resultSetSource.getInt("totallimitamount"));
					account.setUrl(resultSetSource.getString("url"));
					account.setProxylocation(resultSetSource.getString("proxylocation"));
					account.setIsactive(resultSetSource.getBoolean("isactive"));
					account.setSitetype(resultSetSource.getString("sitetype"));
					account.setTimezone(resultSetDestination.getString("timezone"));

					setAccountsSources.add(account);
				}
				scrapper.setSources(setAccountsSources);
				if (resultSetSource != null) {
					resultSetSource.close();
				}
				if (stmtSources != null) {
					stmtSources.close();
				}

				// Add the Scrapper to the list
				scrappers.add(scrapper);
			}
		} catch (SQLException sqle) {
			try {
				closeStreams(stmt, resultSet);
			} catch (Exception e) {
				LOGGER.error(e);
				throw new BatchException(BatchErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
						BatchErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION, "SQL Exception");
			}			
		} finally {
			try {
				closeStreams(stmt, resultSet);
			} catch (Exception e) {
				LOGGER.error(e);
			}
		}

		LOGGER.info("Exiting findAll()");
		return scrappers;
	}
}