/**
 * 
 */
package com.ticketadvantage.services.dao.sites.usatoday;

import java.util.List;

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
public class UsaTodaySite extends Updatable<EventPackage> {
	private final static Logger LOGGER = Logger.getLogger(UsaTodaySite.class);
	private final UsaTodayParser utp = new UsaTodayParser();

	/**
	 * 
	 */
	public UsaTodaySite() {
		super("UsaTodaySite", "https://www.usatoday.com/", null, null, false, false);
		LOGGER.info("Entering UsaTodaySite()");

		try {
			setTimezone("ET");
			getHttpClientWrapper().setupHttpClient("None");
			setProcessTransaction(false);
		} catch (Throwable t) {
			LOGGER.warn(t.getMessage(), t);
		}

		LOGGER.info("Exiting UsaTodaySite()");
	}

	/**
	 * 
	 */
	public UsaTodaySite(String host, String username, String password) {
		super("UsaTodaySite", host, username, password, false, false);
		LOGGER.info("Entering UsaTodaySite()");
		
		try {
			setTimezone("ET");
			getHttpClientWrapper().setupHttpClient("None");
			setProcessTransaction(false);
		} catch (Throwable t) {
			LOGGER.warn(t.getMessage(), t);
		}

		LOGGER.info("Exiting UsaTodaySite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final UsaTodaySite processSite = new UsaTodaySite("https://www.usatoday.com/",
					"", 
					"");
		    processSite.httpClientWrapper.setupHttpClient("None");
		    processSite.getSagarinNcaafRatings();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	/**
	 * 
	 * @return
	 * @throws BatchException
	 */
	public List<SagarinData> getSagarinRatings() throws BatchException {
		String xhtml = super.getSite("https://www.usatoday.com/sports/ncaab/sagarin");
		LOGGER.debug("xhtml: " + xhtml);
		
		List<SagarinData> sdList = utp.parsSagarinRatings(xhtml);
		for (SagarinData sd : sdList) {
			LOGGER.debug("SagarinData: " + sd);
		}

		return sdList;
	}

	/**
	 * 
	 * @return
	 * @throws BatchException
	 */
	public List<SagarinNcaafData> getSagarinNcaafRatings() throws BatchException {
		String xhtml = super.getSite("https://www.usatoday.com/sports/ncaaf/sagarin");
		LOGGER.debug("xhtml: " + xhtml);
		
		List<SagarinNcaafData> sdList = utp.parseNcaafSagarinRatings(xhtml);
		for (SagarinNcaafData sd : sdList) {
			LOGGER.debug("SagarinData: " + sd);
		}

		return sdList;
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