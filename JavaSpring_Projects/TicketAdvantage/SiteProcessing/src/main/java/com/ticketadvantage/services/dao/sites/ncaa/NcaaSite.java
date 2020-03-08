/**
 * 
 */
package com.ticketadvantage.services.dao.sites.ncaa;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.sites.SiteTeamPackage;
import com.ticketadvantage.services.dao.sites.SiteTransaction;
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
public class NcaaSite extends Updatable<EventPackage> {
	private final static Logger LOGGER = Logger.getLogger(NcaaSite.class);
	private final NcaaParser ncaap = new NcaaParser();

	/**
	 * 
	 */
	public NcaaSite(String host, String username, String password) {
		super("NcaaSite", host, username, password, false, false);
		LOGGER.info("Entering NcaaSite()");
		LOGGER.info("Exiting NcaaSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// http://www.ncaa.com/scoreboard/basketball-men/d1/2017/12/18
			final NcaaSite ns = new NcaaSite("http://www.ncaa.com", "", "");
			ns.getHttpClientWrapper().setupHttpClient("None");
			final List<NcaaData> nd = ns.getNcaabScheduleNextTwoDays();
	
			for (NcaaData n : nd) {
				LOGGER.debug("NcaaData: " + n);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	/**
	 * 
	 * @return
	 */
	public List<NcaaData> getNcaabScheduleNextTwoDays() {
		final List<NcaaData> nd = new ArrayList<NcaaData>();

		try {
			// Get time
/*			final Calendar yesterday = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
			yesterday.add(Calendar.DATE, -1); // number of days to subtract
			int monthMinusOne = yesterday.get(Calendar.MONTH) + 1;			
			int dayMinusOne = yesterday.get(Calendar.DAY_OF_MONTH);
			int yearMinusOne = yesterday.get(Calendar.YEAR);

			String xhtml = super.getSite("http://www.ncaa.com/scoreboard/basketball-men/d1/" + yearMinusOne + "/" + monthMinusOne + "/" + dayMinusOne);
			ncaap.parseNcaabSchedule(yearMinusOne + "/" + monthMinusOne + "/" + dayMinusOne, nd, xhtml);
*/
			// Get time
			final Calendar date = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
			int month = date.get(Calendar.MONTH) + 1;
			int day = date.get(Calendar.DAY_OF_MONTH);
			int year = date.get(Calendar.YEAR);

			String xhtml = super.getSite("http://www.ncaa.com/scoreboard/basketball-men/d1/" + year + "/" + month + "/" + day);
			ncaap.parseNcaabSchedule(year + "/" + month + "/" + day, nd, xhtml);

			final Calendar datePlusOne = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
			datePlusOne.add(Calendar.DATE, 1); // number of days to add
			int monthPlusOne = datePlusOne.get(Calendar.MONTH) + 1;
			int dayPlusOne = datePlusOne.get(Calendar.DAY_OF_MONTH);
			int yearPlusOne = datePlusOne.get(Calendar.YEAR);

			xhtml = super.getSite("http://www.ncaa.com/scoreboard/basketball-men/d1/" + yearPlusOne + "/" + monthPlusOne + "/" + dayPlusOne);
			ncaap.parseNcaabSchedule(yearPlusOne + "/" + monthPlusOne + "/" + dayPlusOne, nd, xhtml);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		return nd;
	}

	/**
	 * 
	 * @param proxy
	 */
	public void setProxy(String proxy) {
		LOGGER.info("Entering setProxy()");

		try {
			this.httpClientWrapper.setupHttpClient(proxy);
		} catch (BatchException be) {
			LOGGER.error(be.getMessage(), be);
		}

		LOGGER.info("Exiting setProxy()");
	}

	/**
	 * 
	 * @param spread
	 * @param juice
	 * @param aspTeam
	 * @return
	 * @throws BatchException
	 */
	protected SiteTransaction determineNegativeSpread(SpreadRecordEvent spreadRecordEvent, float spread, float juice, TeamPackage teamPackage) throws BatchException {
		return null;
	}
	
	/**
	 * 
	 * @param spread
	 * @param juice
	 * @param aspTeam
	 * @return
	 * @throws BatchException
	 */
	protected SiteTransaction determinePositiveSpread(SpreadRecordEvent spreadRecordEvent, float spread, float juice, TeamPackage aspTeam) throws BatchException {
		return null;
	}

	/**
	 * 
	 * @param spread
	 * @param juice
	 * @param aspTeam
	 * @return
	 * @throws BatchException
	 */
	protected SiteTransaction determineEqualSpread(SpreadRecordEvent spreadRecordEvent, float spread, float juice, TeamPackage aspTeam) throws BatchException {
		return null;
	}

	/**
	 * 
	 * @param type
	 * @return
	 * @throws BatchException
	 */
	protected EventsPackage retrievePackage(String type) throws BatchException {
		return null;
	}
	
	/**
	 * 
	 * @param spreadType
	 * @param eventPackage
	 * @return
	 * @throws BatchException
	 */
	protected SiteTeamPackage setupTeam(int spreadType, EventPackage eventPackage) throws BatchException {
		return null;
	}

	@Override
	public SiteTransaction createSpreadTransaction(SiteTeamPackage siteTeamPackage,
			SpreadRecordEvent spreadRecordEvent, int arrayNum, float juice, AccountEvent accountEvent) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SiteTransaction createTotalTransaction(SiteTeamPackage siteTeamPackage, TotalRecordEvent totalRecordEvent,
			int arrayNum, float juice, AccountEvent accountEvent) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SiteTransaction createMoneyLineTransaction(SiteTeamPackage siteTeamPackage, MlRecordEvent mlRecordEvent,
			int arrayNum, float juice, AccountEvent accountEvent) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected SiteTransaction createSiteTransaction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String selectSport(String type) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void parseEventSelection(String xhtml, SiteTransaction siteTransaction, EventPackage eventPackage,
			BaseRecordEvent event, AccountEvent ae) throws BatchException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String selectEvent(SiteTransaction siteTransaction, EventPackage eventPackage, BaseRecordEvent event,
			AccountEvent ae) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String completeTransaction(SiteTransaction siteTransaction, EventPackage eventPackage,
			BaseRecordEvent event, AccountEvent ae) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String parseTicketTransaction(String xhtml) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<EventPackage> loadFromSite() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void updateData(List<EventPackage> data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String loginToSite(String username, String password) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}
}