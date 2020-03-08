/**
 * 
 */
package com.wootechnologies.services.site.util;

import org.apache.log4j.Logger;

import com.wootechnologies.errorhandling.BatchException;
import com.wootechnologies.model.Accounts;
import com.wootechnologies.model.PreviewInput;
import com.wootechnologies.services.dao.sites.SiteProcessor;

/**
 * @author jmiller
 *
 */
public class AccountSite {
	private static final Logger LOGGER = Logger.getLogger(AccountSite.class);

	/**
	 * 
	 */
	public AccountSite() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * 
	 * @param account
	 * @return
	 * @throws BatchException
	 */
	public static SiteProcessor GetAccountSite(Accounts account) throws BatchException {
		LOGGER.info("Entering GetAccountSite()");
		SiteProcessor processSite = null;


		LOGGER.info("Exiting GetAccountSite()");
		return processSite;
	}

	/**
	 * 
	 * @param previewInput
	 * @return
	 * @throws BatchException
	 */
	public static SiteProcessor GetAccountSite(PreviewInput previewInput) throws BatchException {
		LOGGER.info("Entering GetAccountSite()");
		SiteProcessor processSite = null;

		LOGGER.info("Exiting GetAccountSite()");
		return processSite;
	}

	/**
	 * 
	 * @param sportType
	 * @return
	 */
	public static String GetSportName(String sportType) {
		LOGGER.info("Entering GetSportName()");
		String sportName = "";

		if (sportType.contains("nfl")) {
			sportName = "NFL";
		} else if (sportType.contains("ncaaf")) {
			sportName = "NCAAF";
		} else if (sportType.contains("wnba")) {
			sportName = "WNBA";
		} else if (sportType.contains("nba")) {
			sportName = "NBA";
		} else if (sportType.contains("ncaab")) {
			sportName = "NCAAB";
		} else if (sportType.contains("nhl")) {
			sportName = "NHL";
		} else if (sportType.contains("mlb")) {
			sportName = "MLB";
		}

		LOGGER.info("Exiting GetSportName()");
		return sportName;
	}
}