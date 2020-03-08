/**
 * 
 */
package com.ticketadvantage.services.dao.sites.sportsline;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.sites.SiteProcessor;
import com.ticketadvantage.services.dao.sites.SiteTeamPackage;
import com.ticketadvantage.services.dao.sites.SiteTransaction;
import com.ticketadvantage.services.db.manager.DataSourceAutoManager;
import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.AccountEvent;
import com.ticketadvantage.services.model.BaseRecordEvent;
import com.ticketadvantage.services.model.EventPackage;
import com.ticketadvantage.services.model.EventsPackage;
import com.ticketadvantage.services.model.MlRecordEvent;
import com.ticketadvantage.services.model.SpreadRecordEvent;
import com.ticketadvantage.services.model.TeamPackage;
import com.ticketadvantage.services.model.TotalRecordEvent;

/**
 * @author jmiller
 *
 */
public class SportslineSite extends SiteProcessor {
	private final static Logger LOGGER = Logger.getLogger(SportslineSite.class);
	private final SportslineParser esp = new SportslineParser();

	/**
	 * 
	 */
	public SportslineSite(String host, String username, String password) {
		super("EspnSite", "http://www.espn.com", username, password, false, false);
		LOGGER.info("Entering EspnSite()");

		try {
			setTimezone("ET");
			getHttpClientWrapper().setupHttpClient("None");
			setProcessTransaction(false);
		} catch (Throwable t) {
			LOGGER.warn(t.getMessage(), t);
		}

		LOGGER.info("Exiting EspnSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final SportslineSite sportslineSite = new SportslineSite(null, null, null);
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}
	}


	@Override
	public SiteTransaction createSpreadTransaction(SiteTeamPackage siteTeamPackage,
			SpreadRecordEvent spreadRecordEvent, int arrayNum, float juice, AccountEvent accountEvent) throws BatchException {
		return null;
	}

	@Override
	public SiteTransaction createTotalTransaction(SiteTeamPackage siteTeamPackage, TotalRecordEvent totalRecordEvent,
			int arrayNum, float juice, AccountEvent accountEvent) throws BatchException {
		return null;
	}

	@Override
	public SiteTransaction createMoneyLineTransaction(SiteTeamPackage siteTeamPackage, MlRecordEvent mlRecordEvent,
			int arrayNum, float juice, AccountEvent accountEvent) throws BatchException {
		return null;
	}

	@Override
	protected SiteTransaction createSiteTransaction() {
		return null;
	}

	@Override
	protected String selectSport(String type) throws BatchException {
		return null;
	}

	@Override
	protected void parseEventSelection(String xhtml, SiteTransaction siteTransaction, EventPackage eventPackage,
			BaseRecordEvent event, AccountEvent ae) throws BatchException {
	}

	@Override
	protected String selectEvent(SiteTransaction siteTransaction, EventPackage eventPackage, BaseRecordEvent event,
			AccountEvent ae) throws BatchException {
		return null;
	}

	@Override
	protected String completeTransaction(SiteTransaction siteTransaction, EventPackage eventPackage,
			BaseRecordEvent event, AccountEvent ae) throws BatchException {
		return null;
	}

	@Override
	protected String parseTicketTransaction(String xhtml) throws BatchException {
		return null;
	}

	@Override
	public String loginToSite(String username, String password) throws BatchException {
		return null;
	}
}