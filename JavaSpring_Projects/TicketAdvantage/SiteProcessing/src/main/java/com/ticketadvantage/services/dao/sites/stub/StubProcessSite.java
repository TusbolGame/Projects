/**
 * 
 */
package com.ticketadvantage.services.dao.sites.stub;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.http.client.HttpClient;
import org.apache.log4j.Logger;


import com.ticketadvantage.services.dao.sites.SiteProcessor;
import com.ticketadvantage.services.dao.sites.ProxyContainer;
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
public class StubProcessSite extends SiteProcessor {
	private final static Logger LOGGER = Logger.getLogger(StubProcessSite.class);

	// Sunday, August 28 3:25p CT
	private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");

	/**
	 * 
	 * @param host
	 * @param username
	 * @param password
	 */
	public StubProcessSite(String host, String username, String password) {
		super("Stub", host, username, password);
		LOGGER.info("Entering StubProcessSite()");
		LOGGER.info("Exiting StubProcessSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final StubProcessSite processSite = new StubProcessSite("http://www.lucktrain.com", "kwd2202", "jh");
		String[] labels = new String[2];
		labels[0] = "NFL - Preseason";
		labels[1] = "NFL - Week";

		try {
			EventsPackage eventsPackage = processSite.getNFL();
//			EventsPackage eventsPackage = processSite.getNFL("nfllines");
			LOGGER.info("EventsPackage: " + eventsPackage);
		} catch (Throwable t) {
			LOGGER.info("Throwable: ", t);
		}
	}

	/**
	 * 
	 * @return
	 */
	public EventsPackage getAllEvents() {
		LOGGER.info("Entering getAllEvents()");

		final EventsPackage esp = new EventsPackage();
		final Set<EventPackage> nfl = getNFL().getEvents();
		Iterator<EventPackage> itr = nfl.iterator();
		while (itr.hasNext()) {
			esp.addEvent(itr.next());
		}
		final Set<EventPackage> nflfirst = getNFLFirst().getEvents();
		itr = nflfirst.iterator();
		while (itr.hasNext()) {
			esp.addEvent(itr.next());
		}

		LOGGER.info("EventsPackage: " + esp);
		LOGGER.info("Exiting getAllEvents()");
		return esp;
	}

	/**
	 * 
	 * @return
	 */
	public EventsPackage getAllGameEvents() {
		EventsPackage esp = new EventsPackage();
		Set<EventPackage> nfl = getNFLEvents().getEvents();
		Iterator<EventPackage> itr = nfl.iterator();
		while (itr.hasNext()) {
			esp.addEvent(itr.next());
		}
		Set<EventPackage> nflfirst = getNFLFirstEvents().getEvents();
		itr = nflfirst.iterator();
		while (itr.hasNext()) {
			esp.addEvent(itr.next());
		}

		Set<EventPackage> ncaaf = getNCAAFEvents().getEvents();
		itr = ncaaf.iterator();
		while (itr.hasNext()) {
			esp.addEvent(itr.next());
		}

		return esp;
	}

	/**
	 * 
	 * @param eventid
	 * @return
	 */
	public EventPackage getEvent(Long eventid) {
		LOGGER.info("Entering getEvent()");
		LOGGER.debug("eventid: " + eventid);
		Iterator<EventPackage> itr = getAllEvents().getEvents().iterator();
		EventPackage retValue = null;
		while (itr.hasNext()) {
			EventPackage ep = itr.next();
			LOGGER.debug("EventPackage: " + ep);
			if (eventid == ep.getId().longValue()) {
				retValue = ep;
				break;
			}
		}
		LOGGER.info("Exiting getEvent()");
		return retValue;
	}

	/**
	 * 
	 * @param eventid
	 * @return
	 */
	public EventPackage getGameEvent(Long eventid) {
		LOGGER.info("Entering getGameEvent()");
		LOGGER.debug("eventid: " + eventid);
		Iterator<EventPackage> itr = getAllGameEvents().getEvents().iterator();
		EventPackage retValue = null;
		while (itr.hasNext()) {
			EventPackage ep = itr.next();
			LOGGER.debug("EventPackage: " + ep);
			if (eventid == ep.getId().longValue()) {
				retValue = ep;
				break;
			}
		}
		LOGGER.info("Exiting getGameEvent()");
		return retValue;
	}

	/**
	 * 
	 * @param sportType
	 * @return
	 * @throws BatchException
	 */
	public EventsPackage getNFL(String sportType) throws BatchException {
		LOGGER.info("Entering getNFL()");
		LOGGER.info("sportType: " + sportType);
		EventsPackage esp = null;

		if ("nfllines".equals(sportType)) {
			esp = getNFL();
		} else if ("nflfirst".equals(sportType)) {
			esp = getNFL();
		} else if ("nflsecond".equals(sportType)) {
			esp = getNFL();
		}

		LOGGER.debug("EventsPackage: " + esp);
		LOGGER.info("Exiting getNFL()");
		return esp;
	}

	/**
	 * 
	 * @param sportType
	 * @return
	 * @throws BatchException
	 */
	public EventsPackage getNCAAF(String sportType) throws BatchException {
		LOGGER.info("Entering getNCAAF()");

		EventsPackage esp = getNCAAF();

		LOGGER.debug("EventsPackage: " + esp);
		LOGGER.info("Exiting getNCAAF()");
		return esp;		
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#fullyAuthenticate(java.lang.String, java.lang.String)
	 */
	@Override
	public String loginToSite(String username, String password) throws BatchException {
		LOGGER.info("Entering fullyAuthenticate()");
		LOGGER.debug("username: " + username);
		LOGGER.debug("password: " + password);
		String xhtml = "";

		LOGGER.info("Exiting fullyAuthenticate()");
		return xhtml;
	}

	/**
	 * 
	 * @param type
	 * @return
	 * @throws BatchException
	 */
	protected EventsPackage retrievePackage(String type) throws BatchException {
		LOGGER.info("Entering retrievePackage()");
		LOGGER.debug("type: " + type);
		EventsPackage esp = null;
		if ("nfllines".equals(type)) {
			esp = getNFL();
		} else if ("nflfirst".equals(type)) {
			esp = getNFL();
		} else if ("nflsecond".equals(type)) {
			esp = getNFL();
		}

		LOGGER.info("Exiting retrievePackage()");
		return esp;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.ProcessSite#createSpreadTransaction(com.ticketadvantage.services.dao.sites.SiteTeamPackage, com.ticketadvantage.services.model.SpreadRecordEvent, int, float, com.ticketadvantage.services.model.AccountEvent)
	 */
	@Override
	public SiteTransaction createSpreadTransaction(SiteTeamPackage siteTeamPackage, SpreadRecordEvent spreadRecordEvent, int arrayNum, float juice, AccountEvent accountEvent) throws BatchException {
		LOGGER.info("Entering createSpreadTransaction()");
		final StubTeamPackage stubTeamPackage = (StubTeamPackage)siteTeamPackage;
		LOGGER.debug("SiteTeamPackage: " + siteTeamPackage);
		LOGGER.debug("SpreadRecordEvent: " + spreadRecordEvent);
		LOGGER.debug("StubTeamPackage: " + stubTeamPackage);

		final StubTransaction stubTransaction = new StubTransaction();
		stubTransaction.setOptionValue(stubTeamPackage.getGameSpreadOptionValue().get(Integer.toString(arrayNum)));
		stubTransaction.setSelectId(stubTeamPackage.getGameSpreadSelectId());
		stubTransaction.setInputId(stubTeamPackage.getGameSpreadInputId());

		// Set the amount
		final Double newAmount = amountToApply(juice, spreadRecordEvent.getWtype(), spreadRecordEvent.getAmount(), accountEvent.getMaxspreadamount(), accountEvent);
		stubTransaction.setAmount(Double.toString(newAmount));

		LOGGER.info("StubTransaction: " + stubTransaction);
		LOGGER.info("Exiting createSpreadTransaction()");
		return stubTransaction;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.ProcessSite#createTotalTransaction(com.ticketadvantage.services.dao.sites.SiteTeamPackage, com.ticketadvantage.services.model.TotalRecordEvent, int, float, com.ticketadvantage.services.model.AccountEvent)
	 */
	@Override
	public SiteTransaction createTotalTransaction(SiteTeamPackage siteTeamPackage, TotalRecordEvent totalRecordEvent, int arrayNum, float juice, AccountEvent accountEvent) throws BatchException {
		LOGGER.info("Entering createTotalTransaction()");
		final StubTeamPackage stubTeamPackage = (StubTeamPackage)siteTeamPackage;
		LOGGER.debug("SiteTeamPackage: " + siteTeamPackage);
		LOGGER.debug("TotalRecordEvent: " + totalRecordEvent);
		LOGGER.debug("StubTeamPackage: " + stubTeamPackage);

		final StubTransaction stubTransaction = new StubTransaction();
		stubTransaction.setOptionValue(stubTeamPackage.getGameSpreadOptionValue().get(Integer.toString(arrayNum)));
		stubTransaction.setSelectId(stubTeamPackage.getGameSpreadSelectId());
		stubTransaction.setInputId(stubTeamPackage.getGameSpreadInputId());

		// Set the amount
		final Double newAmount = amountToApply(juice, totalRecordEvent.getWtype(), totalRecordEvent.getAmount(), accountEvent.getMaxtotalamount(), accountEvent);
		stubTransaction.setAmount(Double.toString(newAmount));

		LOGGER.info("StubTransaction: " + stubTransaction);
		LOGGER.info("Exiting createTotalTransaction()");
		return stubTransaction;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.ProcessSite#createMoneyLineTransaction(com.ticketadvantage.services.dao.sites.SiteTeamPackage, com.ticketadvantage.services.model.MlRecordEvent, int, float, com.ticketadvantage.services.model.AccountEvent)
	 */
	@Override
	public SiteTransaction createMoneyLineTransaction(SiteTeamPackage siteTeamPackage, MlRecordEvent mlRecordEvent, int arrayNum, float juice, AccountEvent accountEvent) throws BatchException {
		LOGGER.info("Entering createTotalTransaction()");
		final StubTeamPackage stubTeamPackage = (StubTeamPackage)siteTeamPackage;
		LOGGER.debug("SiteTeamPackage: " + siteTeamPackage);
		LOGGER.debug("MlRecordEvent: " + mlRecordEvent);
		LOGGER.debug("StubTeamPackage: " + stubTeamPackage);

		final StubTransaction stubTransaction = new StubTransaction();
		stubTransaction.setOptionValue(stubTeamPackage.getGameSpreadOptionValue().get(Integer.toString(arrayNum)));
		stubTransaction.setSelectId(stubTeamPackage.getGameSpreadSelectId());
		stubTransaction.setInputId(stubTeamPackage.getGameSpreadInputId());

		// Set the amount
		final Double newAmount = amountToApply(juice, mlRecordEvent.getWtype(), mlRecordEvent.getAmount(), accountEvent.getMaxmlamount(), accountEvent);
		stubTransaction.setAmount(Double.toString(newAmount));

		LOGGER.info("StubTransaction: " + stubTransaction);
		LOGGER.info("Exiting createTotalTransaction()");
		return stubTransaction;
	}

	/**
	 * 
	 * @param sportType
	 * @return
	 * @throws BatchException
	 */
	public EventsPackage getNFLEvents(String sportType) throws BatchException {
		LOGGER.info("Entering getNFLEvents()");
		LOGGER.info("sportType: " + sportType);
		EventsPackage esp = null;

		if ("nfllines".equals(sportType)) {
			esp = getNFLEvents();
		} else if ("nflfirst".equals(sportType)) {
			esp = getNFLEvents();
		} else if ("nflsecond".equals(sportType)) {
			esp = getNFLEvents();
		}

		LOGGER.debug("EventsPackage: " + esp);
		LOGGER.info("Exiting getNFLEvents()");
		return esp;
	}

	/**
	 * 
	 * @param sportType
	 * @return
	 * @throws BatchException
	 */
	public EventsPackage getNCAAFEvents(String sportType) throws BatchException {
		LOGGER.info("Entering getNCAAFEvents()");
		LOGGER.info("sportType: " + sportType);
		EventsPackage esp = null;

		if ("ncaaflines".equals(sportType)) {
			esp = getNFLEvents();
		} else if ("ncaaffirst".equals(sportType)) {
			esp = getNFLEvents();
		} else if ("ncaafsecond".equals(sportType)) {
			esp = getNFLEvents();
		}

		LOGGER.debug("EventsPackage: " + esp);
		LOGGER.info("Exiting getNCAAFEvents()");
		return esp;
	}

	/**
	 * 
	 * @return
	 */
	public EventsPackage getNFL() 
	{
		LOGGER.info("Entering getNFL()");
		EventsPackage eps = new EventsPackage();

		// Get the properties
		Properties properties = null;
		try {
			properties = new Properties();
			final InputStream in = ProxyContainer.class.getResourceAsStream("/nfllines.properties");
			properties.load(in);
	        in.close();
		} catch (Exception e) {
			LOGGER.error("Exception getting proxy information", e);
		}

	    for (Map.Entry<Object, Object> entry : properties.entrySet())
	    {
	    	String key = (String)entry.getKey();
	    	if (key.contains("nfl.time")) {
	        	String nameKey =  (String)entry.getKey();
	        	int index = nameKey.lastIndexOf(".");
	        	String numValue = nameKey.substring(index + 1);
	        	LOGGER.info("numValue: " + numValue);

		        final StubEventPackage stubEventPackage = new StubEventPackage();
		        final StubTeamPackage team1 = new StubTeamPackage();
		        final StubTeamPackage team2 = new StubTeamPackage();
		        stubEventPackage.setDateofevent(properties.getProperty("nfl.date." + numValue));
		        stubEventPackage.setTimeofevent(properties.getProperty("nfl.time." + numValue));
		        stubEventPackage.setId(Integer.parseInt(properties.getProperty("nfl.team1.id." + numValue)));

		    	team1.setGameSpreadInputId("TS");
		    	team1.setGameSpreadInputName("S1");
		    	team1.setGameSpreadSelectId("SS1");
		    	team1.setGameSpreadSelectName("SS1");
		    	team1.addGameSpreadOptionValue("0", "0");
		    	team1.addGameSpreadOptionValue("1", "1");
		    	team1.addGameSpreadOptionValue("2", "2");
		    	team1.setGameMLInputId("M1");
		    	team1.setGameMLInputName("M1");
		    	team1.addGameMLInputValue("0", "0");
		    	team1.setGameTotalInputId("T1");
		    	team1.setGameTotalInputName("T2");
		    	team1.addGameTotalOptionValue("0", "0");
		    	team1.addGameTotalOptionValue("1", "1");
		    	team1.addGameTotalOptionValue("2", "2");
		    	team1.setGameTotalSelectId("TS1");
		    	team1.setGameTotalSelectName("TS1");
		    	team1.addGameSpreadOptionIndicator("0", properties.getProperty("nfl.team1.spreadind1." + numValue));
		    	team1.addGameSpreadOptionIndicator("1", properties.getProperty("nfl.team1.spreadind2." + numValue));
		    	team1.addGameSpreadOptionIndicator("2", properties.getProperty("nfl.team1.spreadind3." + numValue));
		    	team1.addGameSpreadOptionNumber("0", properties.getProperty("nfl.team1.spread1." + numValue));
		    	team1.addGameSpreadOptionNumber("1", properties.getProperty("nfl.team1.spread2." + numValue));
		    	team1.addGameSpreadOptionNumber("2", properties.getProperty("nfl.team1.spread3." + numValue));
		    	team1.addGameSpreadOptionJuiceIndicator("0", properties.getProperty("nfl.team1.juiceind1." + numValue));
		    	team1.addGameSpreadOptionJuiceIndicator("1", properties.getProperty("nfl.team1.juiceind2." + numValue));
		    	team1.addGameSpreadOptionJuiceIndicator("2", properties.getProperty("nfl.team1.juiceind3." + numValue));
		    	team1.addGameSpreadOptionJuiceNumber("0", properties.getProperty("nfl.team1.juice1." + numValue));
		    	team1.addGameSpreadOptionJuiceNumber("1", properties.getProperty("nfl.team1.juice2." + numValue));
		    	team1.addGameSpreadOptionJuiceNumber("2", properties.getProperty("nfl.team1.juice3." + numValue));
		    	team1.addGameTotalOptionIndicator("0", properties.getProperty("nfl.team1.ouind1." + numValue));
		    	team1.addGameTotalOptionIndicator("1", properties.getProperty("nfl.team1.ouind2." + numValue));
		    	team1.addGameTotalOptionIndicator("2", properties.getProperty("nfl.team1.ouind3." + numValue));
		    	team1.addGameTotalOptionNumber("0", properties.getProperty("nfl.team1.ou1." + numValue));
		    	team1.addGameTotalOptionNumber("1", properties.getProperty("nfl.team1.ou2." + numValue));
		    	team1.addGameTotalOptionNumber("2", properties.getProperty("nfl.team1.ou3." + numValue));
		    	team1.addGameTotalOptionJuiceIndicator("0", properties.getProperty("nfl.team1.oujuiceind1." + numValue));
		    	team1.addGameTotalOptionJuiceIndicator("1", properties.getProperty("nfl.team1.oujuiceind2." + numValue));
		    	team1.addGameTotalOptionJuiceIndicator("2", properties.getProperty("nfl.team1.oujuiceind3." + numValue));
		    	team1.addGameTotalOptionJuiceNumber("0", properties.getProperty("nfl.team1.oujuice1." + numValue));
		    	team1.addGameTotalOptionJuiceNumber("1", properties.getProperty("nfl.team1.oujuice2." + numValue));
		    	team1.addGameTotalOptionJuiceNumber("2", properties.getProperty("nfl.team1.oujuice3." + numValue));
		    	team1.addGameMLOptionJuiceIndicator("0", properties.getProperty("nfl.team1.mlind." + numValue));
		    	team1.addGameMLOptionJuiceNumber("0", properties.getProperty("nfl.team1.ml." + numValue));
		    	try {
		    		final Date nDate = dateFormat.parse(properties.getProperty("nfl.date." + numValue) + "/2016 " + properties.getProperty("nfl.time." + numValue));
		    		team1.setEventdatetime(nDate);
		    	} catch (ParseException pe) {
		    		LOGGER.error("Parser Exception", pe);
		    	}
		    	team1.setTimeofevent(properties.getProperty("nfl.time." + numValue));
		    	team1.setEventid(properties.getProperty("nfl.team1.id." + numValue));
		    	team1.setTeam(properties.getProperty("nfl.team1.name." + numValue));
		    	team1.setId(Integer.parseInt(properties.getProperty("nfl.team1.id." + numValue)));

		    	team2.setGameSpreadInputId("TS");
		    	team2.setGameSpreadInputName("S1");
		    	team2.setGameSpreadSelectId("SS1");
		    	team2.setGameSpreadSelectName("SS1");
		    	team2.addGameSpreadOptionValue("0", "0");
		    	team2.addGameSpreadOptionValue("1", "1");
		    	team2.addGameSpreadOptionValue("2", "2");
		    	team2.setGameMLInputId("M1");
		    	team2.setGameMLInputName("M1");
		    	team2.addGameMLInputValue("0", "0");
		    	team2.setGameTotalInputId("T1");
		    	team2.setGameTotalInputName("T2");
		    	team2.addGameTotalOptionValue("0", "0");
		    	team2.addGameTotalOptionValue("1", "1");
		    	team2.addGameTotalOptionValue("2", "2");
		    	team2.setGameTotalSelectId("TS1");
		    	team2.setGameTotalSelectName("TS1");
		    	team2.addGameSpreadOptionIndicator("0", properties.getProperty("nfl.team2.spreadind1." + numValue));
		    	team2.addGameSpreadOptionIndicator("1", properties.getProperty("nfl.team2.spreadind2." + numValue));
		    	team2.addGameSpreadOptionIndicator("2", properties.getProperty("nfl.team2.spreadind3." + numValue));
		    	team2.addGameSpreadOptionNumber("0", properties.getProperty("nfl.team2.spread1." + numValue));
		    	team2.addGameSpreadOptionNumber("1", properties.getProperty("nfl.team2.spread2." + numValue));
		    	team2.addGameSpreadOptionNumber("2", properties.getProperty("nfl.team2.spread3." + numValue));
		    	team2.addGameSpreadOptionJuiceIndicator("0", properties.getProperty("nfl.team2.juiceind1." + numValue));
		    	team2.addGameSpreadOptionJuiceIndicator("1", properties.getProperty("nfl.team2.juiceind2." + numValue));
		    	team2.addGameSpreadOptionJuiceIndicator("2", properties.getProperty("nfl.team2.juiceind3." + numValue));
		    	team2.addGameSpreadOptionJuiceNumber("0", properties.getProperty("nfl.team2.juice1." + numValue));
		    	team2.addGameSpreadOptionJuiceNumber("1", properties.getProperty("nfl.team2.juice2." + numValue));
		    	team2.addGameSpreadOptionJuiceNumber("2", properties.getProperty("nfl.team2.juice3." + numValue));
		    	team2.addGameTotalOptionIndicator("0", properties.getProperty("nfl.team2.ouind1." + numValue));
		    	team2.addGameTotalOptionIndicator("1", properties.getProperty("nfl.team2.ouind2." + numValue));
		    	team2.addGameTotalOptionIndicator("2", properties.getProperty("nfl.team2.ouind3." + numValue));
		    	team2.addGameTotalOptionNumber("0", properties.getProperty("nfl.team2.ou1." + numValue));
		    	team2.addGameTotalOptionNumber("1", properties.getProperty("nfl.team2.ou2." + numValue));
		    	team2.addGameTotalOptionNumber("2", properties.getProperty("nfl.team2.ou3." + numValue));
		    	team2.addGameTotalOptionJuiceIndicator("0", properties.getProperty("nfl.team2.oujuiceind1." + numValue));
		    	team2.addGameTotalOptionJuiceIndicator("1", properties.getProperty("nfl.team2.oujuiceind2." + numValue));
		    	team2.addGameTotalOptionJuiceIndicator("2", properties.getProperty("nfl.team2.oujuiceind3." + numValue));
		    	team2.addGameTotalOptionJuiceNumber("0", properties.getProperty("nfl.team2.oujuice1." + numValue));
		    	team2.addGameTotalOptionJuiceNumber("1", properties.getProperty("nfl.team2.oujuice2." + numValue));
		    	team2.addGameTotalOptionJuiceNumber("2", properties.getProperty("nfl.team2.oujuice3." + numValue));
		    	team2.addGameMLOptionJuiceIndicator("0", properties.getProperty("nfl.team2.mlind." + numValue));
		    	team2.addGameMLOptionJuiceNumber("0", properties.getProperty("nfl.team2.ml." + numValue));
		    	try {
		    		final Date nDate = dateFormat.parse(properties.getProperty("nfl.date." + numValue) + "/2016 " + properties.getProperty("nfl.time." + numValue));
		    		team2.setEventdatetime(nDate);
		    	} catch (ParseException pe) {
		    		LOGGER.error("Parser Exception", pe);
		    	}
		    	team2.setTimeofevent(properties.getProperty("nfl.time." + numValue));
		    	team2.setEventid(properties.getProperty("nfl.team2.id." + numValue));
		    	team2.setTeam(properties.getProperty("nfl.team2.name." + numValue));
		    	team2.setId(Integer.parseInt(properties.getProperty("nfl.team2.id." + numValue)));

		    	stubEventPackage.setSiteteamone(team1);
		    	stubEventPackage.setSiteteamtwo(team2);
		    	stubEventPackage.setTeamone(team1);
		    	stubEventPackage.setTeamtwo(team2);

		    	LOGGER.info("stubEventPackage: " + stubEventPackage);
		    	eps.addEvent(stubEventPackage);
	        }
	    }
	    LOGGER.info("result: " + eps);
	    LOGGER.info("Exiting getNFL()");
	    return eps;
	}

	/**
	 * 
	 * @return
	 */
	public EventsPackage getNFLEvents() 
	{
		LOGGER.info("Entering getNFLEvents()");
		EventsPackage eps = new EventsPackage();

		// Get the properties
		Properties properties = null;
		try {
			properties = new Properties();
			final InputStream in = ProxyContainer.class.getResourceAsStream("/nfllines.properties");
			properties.load(in);
	        in.close();
		} catch (Exception e) {
			LOGGER.error("Exception getting proxy information", e);
		}

	    for (Map.Entry<Object, Object> entry : properties.entrySet())
	    {
	    	String key = (String)entry.getKey();
	    	if (key.contains("nfl.time")) {
	        	String nameKey =  (String)entry.getKey();
	        	int index = nameKey.lastIndexOf(".");
	        	String numValue = nameKey.substring(index + 1);
	        	LOGGER.info("numValue: " + numValue);

		        final EventPackage stubEventPackage = new EventPackage();
		        final TeamPackage team1 = new TeamPackage();
		        final TeamPackage team2 = new TeamPackage();
		        stubEventPackage.setDateofevent(properties.getProperty("nfl.date." + numValue));
		        stubEventPackage.setTimeofevent(properties.getProperty("nfl.time." + numValue));
		        stubEventPackage.setId(Integer.parseInt(properties.getProperty("nfl.team1.id." + numValue)));

		    	try {
		    		final Date nDate = dateFormat.parse(properties.getProperty("nfl.date." + numValue) + "/2016 " + properties.getProperty("nfl.time." + numValue));
		    		team1.setEventdatetime(nDate);
		    	} catch (ParseException pe) {
		    		LOGGER.error("Parser Exception", pe);
		    	}
		    	team1.setTimeofevent(properties.getProperty("nfl.time." + numValue));
		    	team1.setEventid(properties.getProperty("nfl.team1.id." + numValue));
		    	team1.setTeam(properties.getProperty("nfl.team1.name." + numValue));
		    	team1.setId(Integer.parseInt(properties.getProperty("nfl.team1.id." + numValue)));

		    	try {
		    		final Date nDate = dateFormat.parse(properties.getProperty("nfl.date." + numValue) + "/2016 " + properties.getProperty("nfl.time." + numValue));
		    		team2.setEventdatetime(nDate);
		    	} catch (ParseException pe) {
		    		LOGGER.error("Parser Exception", pe);
		    	}
		    	team2.setTimeofevent(properties.getProperty("nfl.time." + numValue));
		    	team2.setEventid(properties.getProperty("nfl.team2.id." + numValue));
		    	team2.setTeam(properties.getProperty("nfl.team2.name." + numValue));
		    	team2.setId(Integer.parseInt(properties.getProperty("nfl.team2.id." + numValue)));

		    	stubEventPackage.setTeamone(team1);
		    	stubEventPackage.setTeamtwo(team2);

		    	LOGGER.info("stubEventPackage: " + stubEventPackage);
		    	eps.addEvent(stubEventPackage);
	        }
	    }
	    LOGGER.info("result: " + eps);
	    LOGGER.info("Exiting getNFL()");
	    return eps;
	}

	/**
	 * 
	 * @return
	 */
	public EventsPackage getNFLFirst()
	{
		LOGGER.info("Entering getNFLFirst()");
		EventsPackage eps = new EventsPackage();

		// Get the properties
		Properties properties = null;
		try {
			properties = new Properties();
			final InputStream in = ProxyContainer.class.getResourceAsStream("/nflfirst.properties");
			properties.load(in);
	        in.close();
		} catch (Exception e) {
			LOGGER.error("Exception getting proxy information", e);
		}

	    for (Map.Entry<Object, Object> entry : properties.entrySet())
	    {
	    	String key = (String)entry.getKey();
	    	if (key.contains("nfl.time")) {
	        	String nameKey =  (String)entry.getKey();
	        	int index = nameKey.lastIndexOf(".");
	        	String numValue = nameKey.substring(index + 1);
	        	LOGGER.info("numValue: " + numValue);

		        final StubEventPackage stubEventPackage = new StubEventPackage();
		        final StubTeamPackage team1 = new StubTeamPackage();
		        final StubTeamPackage team2 = new StubTeamPackage();
		        stubEventPackage.setDateofevent(properties.getProperty("nfl.date." + numValue));
		        stubEventPackage.setTimeofevent(properties.getProperty("nfl.time." + numValue));
		        stubEventPackage.setId(Integer.parseInt(properties.getProperty("nfl.team1.id." + numValue)));

		    	team1.setGameSpreadInputId("TS");
		    	team1.setGameSpreadInputName("S1");
		    	team1.setGameSpreadSelectId("SS1");
		    	team1.setGameSpreadSelectName("SS1");
		    	team1.addGameSpreadOptionValue("0", "0");
		    	team1.addGameSpreadOptionValue("1", "1");
		    	team1.addGameSpreadOptionValue("2", "2");
		    	team1.setGameMLInputId("M1");
		    	team1.setGameMLInputName("M1");
		    	team1.addGameMLInputValue("0", "0");
		    	team1.setGameTotalInputId("T1");
		    	team1.setGameTotalInputName("T2");
		    	team1.addGameTotalOptionValue("0", "0");
		    	team1.addGameTotalOptionValue("1", "1");
		    	team1.addGameTotalOptionValue("2", "2");
		    	team1.setGameTotalSelectId("TS1");
		    	team1.setGameTotalSelectName("TS1");
		    	team1.addGameSpreadOptionIndicator("0", properties.getProperty("nfl.team1.spreadind1." + numValue));
		    	team1.addGameSpreadOptionIndicator("1", properties.getProperty("nfl.team1.spreadind2." + numValue));
		    	team1.addGameSpreadOptionIndicator("2", properties.getProperty("nfl.team1.spreadind3." + numValue));
		    	team1.addGameSpreadOptionNumber("0", properties.getProperty("nfl.team1.spread1." + numValue));
		    	team1.addGameSpreadOptionNumber("1", properties.getProperty("nfl.team1.spread2." + numValue));
		    	team1.addGameSpreadOptionNumber("2", properties.getProperty("nfl.team1.spread3." + numValue));
		    	team1.addGameSpreadOptionJuiceIndicator("0", properties.getProperty("nfl.team1.juiceind1." + numValue));
		    	team1.addGameSpreadOptionJuiceIndicator("1", properties.getProperty("nfl.team1.juiceind2." + numValue));
		    	team1.addGameSpreadOptionJuiceIndicator("2", properties.getProperty("nfl.team1.juiceind3." + numValue));
		    	team1.addGameSpreadOptionJuiceNumber("0", properties.getProperty("nfl.team1.juice1." + numValue));
		    	team1.addGameSpreadOptionJuiceNumber("1", properties.getProperty("nfl.team1.juice2." + numValue));
		    	team1.addGameSpreadOptionJuiceNumber("2", properties.getProperty("nfl.team1.juice3." + numValue));
		    	team1.addGameTotalOptionIndicator("0", properties.getProperty("nfl.team1.ouind1." + numValue));
		    	team1.addGameTotalOptionIndicator("1", properties.getProperty("nfl.team1.ouind2." + numValue));
		    	team1.addGameTotalOptionIndicator("2", properties.getProperty("nfl.team1.ouind3." + numValue));
		    	team1.addGameTotalOptionNumber("0", properties.getProperty("nfl.team1.ou1." + numValue));
		    	team1.addGameTotalOptionNumber("1", properties.getProperty("nfl.team1.ou2." + numValue));
		    	team1.addGameTotalOptionNumber("2", properties.getProperty("nfl.team1.ou3." + numValue));
		    	team1.addGameTotalOptionJuiceIndicator("0", properties.getProperty("nfl.team1.oujuiceind1." + numValue));
		    	team1.addGameTotalOptionJuiceIndicator("1", properties.getProperty("nfl.team1.oujuiceind2." + numValue));
		    	team1.addGameTotalOptionJuiceIndicator("2", properties.getProperty("nfl.team1.oujuiceind3." + numValue));
		    	team1.addGameTotalOptionJuiceNumber("0", properties.getProperty("nfl.team1.oujuice1." + numValue));
		    	team1.addGameTotalOptionJuiceNumber("1", properties.getProperty("nfl.team1.oujuice2." + numValue));
		    	team1.addGameTotalOptionJuiceNumber("2", properties.getProperty("nfl.team1.oujuice3." + numValue));
		    	team1.addGameMLOptionJuiceIndicator("0", properties.getProperty("nfl.team1.mlind." + numValue));
		    	team1.addGameMLOptionJuiceNumber("0", properties.getProperty("nfl.team1.ml." + numValue));
		    	try {
		    		final Date nDate = dateFormat.parse(properties.getProperty("nfl.date." + numValue) + "/2016 " + properties.getProperty("nfl.time." + numValue));
		    		team1.setEventdatetime(nDate);
		    	} catch (ParseException pe) {
		    		LOGGER.error("Parser Exception", pe);
		    	}
		    	team1.setTimeofevent(properties.getProperty("nfl.time." + numValue));
		    	team1.setEventid(properties.getProperty("nfl.team1.id." + numValue));
		    	team1.setTeam(properties.getProperty("nfl.team1.name." + numValue));
		    	team1.setId(Integer.parseInt(properties.getProperty("nfl.team1.id." + numValue)));

		    	team2.setGameSpreadInputId("TS");
		    	team2.setGameSpreadInputName("S1");
		    	team2.setGameSpreadSelectId("SS1");
		    	team2.setGameSpreadSelectName("SS1");
		    	team2.addGameSpreadOptionValue("0", "0");
		    	team2.addGameSpreadOptionValue("1", "1");
		    	team2.addGameSpreadOptionValue("2", "2");
		    	team2.setGameMLInputId("M1");
		    	team2.setGameMLInputName("M1");
		    	team2.addGameMLInputValue("0", "0");
		    	team2.setGameTotalInputId("T1");
		    	team2.setGameTotalInputName("T2");
		    	team2.addGameTotalOptionValue("0", "0");
		    	team2.addGameTotalOptionValue("1", "1");
		    	team2.addGameTotalOptionValue("2", "2");
		    	team2.setGameTotalSelectId("TS1");
		    	team2.setGameTotalSelectName("TS1");
		    	team2.addGameSpreadOptionIndicator("0", properties.getProperty("nfl.team2.spreadind1." + numValue));
		    	team2.addGameSpreadOptionIndicator("1", properties.getProperty("nfl.team2.spreadind2." + numValue));
		    	team2.addGameSpreadOptionIndicator("2", properties.getProperty("nfl.team2.spreadind3." + numValue));
		    	team2.addGameSpreadOptionNumber("0", properties.getProperty("nfl.team2.spread1." + numValue));
		    	team2.addGameSpreadOptionNumber("1", properties.getProperty("nfl.team2.spread2." + numValue));
		    	team2.addGameSpreadOptionNumber("2", properties.getProperty("nfl.team2.spread3." + numValue));
		    	team2.addGameSpreadOptionJuiceIndicator("0", properties.getProperty("nfl.team2.juiceind1." + numValue));
		    	team2.addGameSpreadOptionJuiceIndicator("1", properties.getProperty("nfl.team2.juiceind2." + numValue));
		    	team2.addGameSpreadOptionJuiceIndicator("2", properties.getProperty("nfl.team2.juiceind3." + numValue));
		    	team2.addGameSpreadOptionJuiceNumber("0", properties.getProperty("nfl.team2.juice1." + numValue));
		    	team2.addGameSpreadOptionJuiceNumber("1", properties.getProperty("nfl.team2.juice2." + numValue));
		    	team2.addGameSpreadOptionJuiceNumber("2", properties.getProperty("nfl.team2.juice3." + numValue));
		    	team2.addGameTotalOptionIndicator("0", properties.getProperty("nfl.team2.ouind1." + numValue));
		    	team2.addGameTotalOptionIndicator("1", properties.getProperty("nfl.team2.ouind2." + numValue));
		    	team2.addGameTotalOptionIndicator("2", properties.getProperty("nfl.team2.ouind3." + numValue));
		    	team2.addGameTotalOptionNumber("0", properties.getProperty("nfl.team2.ou1." + numValue));
		    	team2.addGameTotalOptionNumber("1", properties.getProperty("nfl.team2.ou2." + numValue));
		    	team2.addGameTotalOptionNumber("2", properties.getProperty("nfl.team2.ou3." + numValue));
		    	team2.addGameTotalOptionJuiceIndicator("0", properties.getProperty("nfl.team2.oujuiceind1." + numValue));
		    	team2.addGameTotalOptionJuiceIndicator("1", properties.getProperty("nfl.team2.oujuiceind2." + numValue));
		    	team2.addGameTotalOptionJuiceIndicator("2", properties.getProperty("nfl.team2.oujuiceind3." + numValue));
		    	team2.addGameTotalOptionJuiceNumber("0", properties.getProperty("nfl.team2.oujuice1." + numValue));
		    	team2.addGameTotalOptionJuiceNumber("1", properties.getProperty("nfl.team2.oujuice2." + numValue));
		    	team2.addGameTotalOptionJuiceNumber("2", properties.getProperty("nfl.team2.oujuice3." + numValue));
		    	team2.addGameMLOptionJuiceIndicator("0", properties.getProperty("nfl.team2.mlind." + numValue));
		    	team2.addGameMLOptionJuiceNumber("0", properties.getProperty("nfl.team2.ml." + numValue));
		    	try {
		    		final Date nDate = dateFormat.parse(properties.getProperty("nfl.date." + numValue) + "/2016 " + properties.getProperty("nfl.time." + numValue));
		    		team2.setEventdatetime(nDate);
		    	} catch (ParseException pe) {
		    		LOGGER.error("Parser Exception", pe);
		    	}
		    	team2.setTimeofevent(properties.getProperty("nfl.time." + numValue));
		    	team2.setEventid(properties.getProperty("nfl.team2.id." + numValue));
		    	team2.setTeam(properties.getProperty("nfl.team2.name." + numValue));
		    	team2.setId(Integer.parseInt(properties.getProperty("nfl.team2.id." + numValue)));

		    	stubEventPackage.setSiteteamone(team1);
		    	stubEventPackage.setSiteteamtwo(team2);
		    	stubEventPackage.setTeamone(team1);
		    	stubEventPackage.setTeamtwo(team2);


		    	LOGGER.info("stubEventPackage: " + stubEventPackage);
		    	eps.addEvent(stubEventPackage);
	        }
	    }
	    LOGGER.info("result: " + eps);
	    LOGGER.info("Exiting getNFLFirst()");
	    return eps;
	}

	/**
	 * 
	 * @return
	 */
	public EventsPackage getNFLFirstEvents()
	{
		LOGGER.info("Entering getNFLFirstEvents()");
		EventsPackage eps = new EventsPackage();

		// Get the properties
		Properties properties = null;
		try {
			properties = new Properties();
			final InputStream in = ProxyContainer.class.getResourceAsStream("/nfllines.properties");
			properties.load(in);
	        in.close();
		} catch (Exception e) {
			LOGGER.error("Exception getting proxy information", e);
		}

	    for (Map.Entry<Object, Object> entry : properties.entrySet())
	    {
	    	String key = (String)entry.getKey();
	    	if (key.contains("nfl.time")) {
	        	String nameKey =  (String)entry.getKey();
	        	int index = nameKey.lastIndexOf(".");
	        	String numValue = nameKey.substring(index + 1);
	        	LOGGER.info("numValue: " + numValue);

		        final EventPackage stubEventPackage = new EventPackage();
		        final TeamPackage team1 = new TeamPackage();
		        final TeamPackage team2 = new TeamPackage();
		        stubEventPackage.setDateofevent(properties.getProperty("nfl.date." + numValue));
		        stubEventPackage.setTimeofevent(properties.getProperty("nfl.time." + numValue));
		        stubEventPackage.setId(Integer.parseInt(properties.getProperty("nfl.team1.id." + numValue)));

		    	try {
		    		final Date nDate = dateFormat.parse(properties.getProperty("nfl.date." + numValue) + "/2016 " + properties.getProperty("nfl.time." + numValue));
		    		team1.setEventdatetime(nDate);
		    	} catch (ParseException pe) {
		    		LOGGER.error("Parser Exception", pe);
		    	}
		    	team1.setTimeofevent(properties.getProperty("nfl.time." + numValue));
		    	team1.setEventid(properties.getProperty("nfl.team1.id." + numValue));
		    	team1.setTeam(properties.getProperty("nfl.team1.name." + numValue));
		    	team1.setId(Integer.parseInt(properties.getProperty("nfl.team1.id." + numValue)));

		    	try {
		    		final Date nDate = dateFormat.parse(properties.getProperty("nfl.date." + numValue) + "/2016 " + properties.getProperty("nfl.time." + numValue));
		    		team2.setEventdatetime(nDate);
		    	} catch (ParseException pe) {
		    		LOGGER.error("Parser Exception", pe);
		    	}
		    	team2.setTimeofevent(properties.getProperty("nfl.time." + numValue));
		    	team2.setEventid(properties.getProperty("nfl.team2.id." + numValue));
		    	team2.setTeam(properties.getProperty("nfl.team2.name." + numValue));
		    	team2.setId(Integer.parseInt(properties.getProperty("nfl.team2.id." + numValue)));

		    	stubEventPackage.setTeamone(team1);
		    	stubEventPackage.setTeamtwo(team2);

		    	LOGGER.info("stubEventPackage: " + stubEventPackage);
		    	eps.addEvent(stubEventPackage);
	        }
	    }
	    LOGGER.info("result: " + eps);
	    LOGGER.info("Exiting getNFLFirstEvents()");
	    return eps;
	}

	/**
	 * 
	 * @return
	 */
	public EventsPackage getNCAAF()
	{
		LOGGER.info("Entering getNCAAF()");
		EventsPackage eps = new EventsPackage();

		// Get the properties
		Properties properties = null;
		try {
			properties = new Properties();
			final InputStream in = ProxyContainer.class.getResourceAsStream("/ncaaflines.properties");
			properties.load(in);
	        in.close();
		} catch (Exception e) {
			LOGGER.error("Exception getting proxy information", e);
		}

	    for (Map.Entry<Object, Object> entry : properties.entrySet())
	    {
	    	String key = (String)entry.getKey();
	    	if (key.contains("nfl.time")) {
	        	String nameKey =  (String)entry.getKey();
	        	int index = nameKey.lastIndexOf(".");
	        	String numValue = nameKey.substring(index + 1);
	        	LOGGER.info("numValue: " + numValue);

		        final StubEventPackage stubEventPackage = new StubEventPackage();
		        final StubTeamPackage team1 = new StubTeamPackage();
		        final StubTeamPackage team2 = new StubTeamPackage();
		        stubEventPackage.setDateofevent(properties.getProperty("nfl.date." + numValue));
		        stubEventPackage.setTimeofevent(properties.getProperty("nfl.time." + numValue));
		        stubEventPackage.setId(Integer.parseInt(properties.getProperty("nfl.team1.id." + numValue)));

		    	team1.setGameSpreadInputId("TS");
		    	team1.setGameSpreadInputName("S1");
		    	team1.setGameSpreadSelectId("SS1");
		    	team1.setGameSpreadSelectName("SS1");
		    	team1.addGameSpreadOptionValue("0", "0");
		    	team1.addGameSpreadOptionValue("1", "1");
		    	team1.addGameSpreadOptionValue("2", "2");
		    	team1.setGameMLInputId("M1");
		    	team1.setGameMLInputName("M1");
		    	team1.addGameMLInputValue("0", "0");
		    	team1.setGameTotalInputId("T1");
		    	team1.setGameTotalInputName("T2");
		    	team1.addGameTotalOptionValue("0", "0");
		    	team1.addGameTotalOptionValue("1", "1");
		    	team1.addGameTotalOptionValue("2", "2");
		    	team1.setGameTotalSelectId("TS1");
		    	team1.setGameTotalSelectName("TS1");
		    	team1.addGameSpreadOptionIndicator("0", properties.getProperty("nfl.team1.spreadind1." + numValue));
		    	team1.addGameSpreadOptionIndicator("1", properties.getProperty("nfl.team1.spreadind2." + numValue));
		    	team1.addGameSpreadOptionIndicator("2", properties.getProperty("nfl.team1.spreadind3." + numValue));
		    	team1.addGameSpreadOptionNumber("0", properties.getProperty("nfl.team1.spread1." + numValue));
		    	team1.addGameSpreadOptionNumber("1", properties.getProperty("nfl.team1.spread2." + numValue));
		    	team1.addGameSpreadOptionNumber("2", properties.getProperty("nfl.team1.spread3." + numValue));
		    	team1.addGameSpreadOptionJuiceIndicator("0", properties.getProperty("nfl.team1.juiceind1." + numValue));
		    	team1.addGameSpreadOptionJuiceIndicator("1", properties.getProperty("nfl.team1.juiceind2." + numValue));
		    	team1.addGameSpreadOptionJuiceIndicator("2", properties.getProperty("nfl.team1.juiceind3." + numValue));
		    	team1.addGameSpreadOptionJuiceNumber("0", properties.getProperty("nfl.team1.juice1." + numValue));
		    	team1.addGameSpreadOptionJuiceNumber("1", properties.getProperty("nfl.team1.juice2." + numValue));
		    	team1.addGameSpreadOptionJuiceNumber("2", properties.getProperty("nfl.team1.juice3." + numValue));
		    	team1.addGameTotalOptionIndicator("0", properties.getProperty("nfl.team1.ouind1." + numValue));
		    	team1.addGameTotalOptionIndicator("1", properties.getProperty("nfl.team1.ouind2." + numValue));
		    	team1.addGameTotalOptionIndicator("2", properties.getProperty("nfl.team1.ouind3." + numValue));
		    	team1.addGameTotalOptionNumber("0", properties.getProperty("nfl.team1.ou1." + numValue));
		    	team1.addGameTotalOptionNumber("1", properties.getProperty("nfl.team1.ou2." + numValue));
		    	team1.addGameTotalOptionNumber("2", properties.getProperty("nfl.team1.ou3." + numValue));
		    	team1.addGameTotalOptionJuiceIndicator("0", properties.getProperty("nfl.team1.oujuiceind1." + numValue));
		    	team1.addGameTotalOptionJuiceIndicator("1", properties.getProperty("nfl.team1.oujuiceind2." + numValue));
		    	team1.addGameTotalOptionJuiceIndicator("2", properties.getProperty("nfl.team1.oujuiceind3." + numValue));
		    	team1.addGameTotalOptionJuiceNumber("0", properties.getProperty("nfl.team1.oujuice1." + numValue));
		    	team1.addGameTotalOptionJuiceNumber("1", properties.getProperty("nfl.team1.oujuice2." + numValue));
		    	team1.addGameTotalOptionJuiceNumber("2", properties.getProperty("nfl.team1.oujuice3." + numValue));
		    	team1.addGameMLOptionJuiceIndicator("0", properties.getProperty("nfl.team1.mlind." + numValue));
		    	team1.addGameMLOptionJuiceNumber("0", properties.getProperty("nfl.team1.ml." + numValue));
		    	try {
		    		final Date nDate = dateFormat.parse(properties.getProperty("nfl.date." + numValue) + "/2016 " + properties.getProperty("nfl.time." + numValue));
		    		team1.setEventdatetime(nDate);
		    	} catch (ParseException pe) {
		    		LOGGER.error("Parser Exception", pe);
		    	}
		    	team1.setTimeofevent(properties.getProperty("nfl.time." + numValue));
		    	team1.setEventid(properties.getProperty("nfl.team1.id." + numValue));
		    	team1.setTeam(properties.getProperty("nfl.team1.name." + numValue));
		    	team1.setId(Integer.parseInt(properties.getProperty("nfl.team1.id." + numValue)));

		    	team2.setGameSpreadInputId("TS");
		    	team2.setGameSpreadInputName("S1");
		    	team2.setGameSpreadSelectId("SS1");
		    	team2.setGameSpreadSelectName("SS1");
		    	team2.addGameSpreadOptionValue("0", "0");
		    	team2.addGameSpreadOptionValue("1", "1");
		    	team2.addGameSpreadOptionValue("2", "2");
		    	team2.setGameMLInputId("M1");
		    	team2.setGameMLInputName("M1");
		    	team2.addGameMLInputValue("0", "0");
		    	team2.setGameTotalInputId("T1");
		    	team2.setGameTotalInputName("T2");
		    	team2.addGameTotalOptionValue("0", "0");
		    	team2.addGameTotalOptionValue("1", "1");
		    	team2.addGameTotalOptionValue("2", "2");
		    	team2.setGameTotalSelectId("TS1");
		    	team2.setGameTotalSelectName("TS1");
		    	team2.addGameSpreadOptionIndicator("0", properties.getProperty("nfl.team2.spreadind1." + numValue));
		    	team2.addGameSpreadOptionIndicator("1", properties.getProperty("nfl.team2.spreadind2." + numValue));
		    	team2.addGameSpreadOptionIndicator("2", properties.getProperty("nfl.team2.spreadind3." + numValue));
		    	team2.addGameSpreadOptionNumber("0", properties.getProperty("nfl.team2.spread1." + numValue));
		    	team2.addGameSpreadOptionNumber("1", properties.getProperty("nfl.team2.spread2." + numValue));
		    	team2.addGameSpreadOptionNumber("2", properties.getProperty("nfl.team2.spread3." + numValue));
		    	team2.addGameSpreadOptionJuiceIndicator("0", properties.getProperty("nfl.team2.juiceind1." + numValue));
		    	team2.addGameSpreadOptionJuiceIndicator("1", properties.getProperty("nfl.team2.juiceind2." + numValue));
		    	team2.addGameSpreadOptionJuiceIndicator("2", properties.getProperty("nfl.team2.juiceind3." + numValue));
		    	team2.addGameSpreadOptionJuiceNumber("0", properties.getProperty("nfl.team2.juice1." + numValue));
		    	team2.addGameSpreadOptionJuiceNumber("1", properties.getProperty("nfl.team2.juice2." + numValue));
		    	team2.addGameSpreadOptionJuiceNumber("2", properties.getProperty("nfl.team2.juice3." + numValue));
		    	team2.addGameTotalOptionIndicator("0", properties.getProperty("nfl.team2.ouind1." + numValue));
		    	team2.addGameTotalOptionIndicator("1", properties.getProperty("nfl.team2.ouind2." + numValue));
		    	team2.addGameTotalOptionIndicator("2", properties.getProperty("nfl.team2.ouind3." + numValue));
		    	team2.addGameTotalOptionNumber("0", properties.getProperty("nfl.team2.ou1." + numValue));
		    	team2.addGameTotalOptionNumber("1", properties.getProperty("nfl.team2.ou2." + numValue));
		    	team2.addGameTotalOptionNumber("2", properties.getProperty("nfl.team2.ou3." + numValue));
		    	team2.addGameTotalOptionJuiceIndicator("0", properties.getProperty("nfl.team2.oujuiceind1." + numValue));
		    	team2.addGameTotalOptionJuiceIndicator("1", properties.getProperty("nfl.team2.oujuiceind2." + numValue));
		    	team2.addGameTotalOptionJuiceIndicator("2", properties.getProperty("nfl.team2.oujuiceind3." + numValue));
		    	team2.addGameTotalOptionJuiceNumber("0", properties.getProperty("nfl.team2.oujuice1." + numValue));
		    	team2.addGameTotalOptionJuiceNumber("1", properties.getProperty("nfl.team2.oujuice2." + numValue));
		    	team2.addGameTotalOptionJuiceNumber("2", properties.getProperty("nfl.team2.oujuice3." + numValue));
		    	team2.addGameMLOptionJuiceIndicator("0", properties.getProperty("nfl.team2.mlind." + numValue));
		    	team2.addGameMLOptionJuiceNumber("0", properties.getProperty("nfl.team2.ml." + numValue));
		    	try {
		    		final Date nDate = dateFormat.parse(properties.getProperty("nfl.date." + numValue) + "/2016 " + properties.getProperty("nfl.time." + numValue));
		    		team2.setEventdatetime(nDate);
		    	} catch (ParseException pe) {
		    		LOGGER.error("Parser Exception", pe);
		    	}
		    	team2.setTimeofevent(properties.getProperty("nfl.time." + numValue));
		    	team2.setEventid(properties.getProperty("nfl.team2.id." + numValue));
		    	team2.setTeam(properties.getProperty("nfl.team2.name." + numValue));
		    	team2.setId(Integer.parseInt(properties.getProperty("nfl.team2.id." + numValue)));

		    	stubEventPackage.setSiteteamone(team1);
		    	stubEventPackage.setSiteteamtwo(team2);
		    	stubEventPackage.setTeamone(team1);
		    	stubEventPackage.setTeamtwo(team2);

		    	LOGGER.info("stubEventPackage: " + stubEventPackage);
		    	eps.addEvent(stubEventPackage);
	        }
	    }
	    LOGGER.info("result: " + eps);
	    LOGGER.info("Exiting getNCAAF()");
	    return eps;
	}
	
	/**
	 * 
	 * @return
	 */
	public EventsPackage getNCAAFEvents()
	{
		LOGGER.info("Entering getNCAAEvents()");
		EventsPackage eps = new EventsPackage();

		// Get the properties
		Properties properties = null;
		try {
			properties = new Properties();
			final InputStream in = ProxyContainer.class.getResourceAsStream("/nfllines.properties");
			properties.load(in);
	        in.close();
		} catch (Exception e) {
			LOGGER.error("Exception getting proxy information", e);
		}

	    for (Map.Entry<Object, Object> entry : properties.entrySet())
	    {
	    	String key = (String)entry.getKey();
	    	if (key.contains("nfl.time")) {
	        	String nameKey =  (String)entry.getKey();
	        	int index = nameKey.lastIndexOf(".");
	        	String numValue = nameKey.substring(index + 1);
	        	LOGGER.info("numValue: " + numValue);

		        final EventPackage stubEventPackage = new EventPackage();
		        final TeamPackage team1 = new TeamPackage();
		        final TeamPackage team2 = new TeamPackage();
		        stubEventPackage.setDateofevent(properties.getProperty("nfl.date." + numValue));
		        stubEventPackage.setTimeofevent(properties.getProperty("nfl.time." + numValue));
		        stubEventPackage.setId(Integer.parseInt(properties.getProperty("nfl.team1.id." + numValue)));

		    	try {
		    		final Date nDate = dateFormat.parse(properties.getProperty("nfl.date." + numValue) + "/2016 " + properties.getProperty("nfl.time." + numValue));
		    		team1.setEventdatetime(nDate);
		    	} catch (ParseException pe) {
		    		LOGGER.error("Parser Exception", pe);
		    	}
		    	team1.setTimeofevent(properties.getProperty("nfl.time." + numValue));
		    	team1.setEventid(properties.getProperty("nfl.team1.id." + numValue));
		    	team1.setTeam(properties.getProperty("nfl.team1.name." + numValue));
		    	team1.setId(Integer.parseInt(properties.getProperty("nfl.team1.id." + numValue)));
		    	stubEventPackage.setId(Integer.parseInt(properties.getProperty("nfl.team1.id." + numValue)));

		    	try {
		    		final Date nDate = dateFormat.parse(properties.getProperty("nfl.date." + numValue) + "/2016 " + properties.getProperty("nfl.time." + numValue));
		    		team2.setEventdatetime(nDate);
		    	} catch (ParseException pe) {
		    		LOGGER.error("Parser Exception", pe);
		    	}
		    	team2.setTimeofevent(properties.getProperty("nfl.time." + numValue));
		    	team2.setEventid(properties.getProperty("nfl.team2.id." + numValue));
		    	team2.setTeam(properties.getProperty("nfl.team2.name." + numValue));
		    	team2.setId(Integer.parseInt(properties.getProperty("nfl.team2.id." + numValue)));

		    	stubEventPackage.setTeamone(team1);
		    	stubEventPackage.setTeamtwo(team2);

		    	LOGGER.info("stubEventPackage: " + stubEventPackage);
		    	eps.addEvent(stubEventPackage);
	        }
	    }
	    LOGGER.info("result: " + eps);
	    LOGGER.info("Exiting getNCAAEvents()");
	    return eps;
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
}