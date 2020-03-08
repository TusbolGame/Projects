package com.ticketadvantage.services.dao.sites;

import java.util.Date;

import org.apache.log4j.Logger;

import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.AccountEvent;
import com.ticketadvantage.services.model.EventPackage;
import com.ticketadvantage.services.model.SpreadRecordEvent;
import com.ticketadvantage.services.model.TotalRecordEvent;

public class SiteProcessorTest {
	private static final Logger LOGGER = Logger.getLogger(SiteProcessorTest.class);

	/**
	 * 
	 */
	public SiteProcessorTest() {
		super();
	}

	/**
	 * 
	 * @param siteProcessor
	 * @param eventPackage
	 * @param sport
	 * @param amountLimit
	 * @param name
	 * @param proxy
	 */
	public void processTests(SiteProcessor siteProcessor, EventPackage eventPackage, String sport, Integer spreadAmountLimit, Integer totalAmountLimit, Integer mlAmountLimit, String name, String proxy) throws BatchException {
		LOGGER.info("Entering processTests()");
		
		testSpread(siteProcessor, eventPackage, sport, spreadAmountLimit, name, proxy);
//		testTotal(siteProcessor, eventPackage, sport, totalAmountLimit, name, proxy);

		LOGGER.info("Exiting processTests()");
	}

	/**
	 * 
	 * @param siteProcessor
	 * @param eventPackage
	 * @param sport
	 * @param amountLimit
	 * @param name
	 * @param proxy
	 * @throws BatchException
	 */
	protected void testSpread(SiteProcessor siteProcessor, EventPackage eventPackage, String sport, Integer amountLimit, String name, String proxy) throws BatchException {
		LOGGER.info("Entering testSpread()");
		LOGGER.warn("siteProcessor: " + siteProcessor);
		LOGGER.warn("eventPackage: " + eventPackage);
		LOGGER.warn("sport: " + sport);
		LOGGER.warn("amountLimit: " + amountLimit);
		LOGGER.warn("name: " + name);
		LOGGER.warn("proxy: " + proxy);
		
		SpreadRecordEvent event = new SpreadRecordEvent();
		event.setAccountid(new Long(1));
		event.setAmount("1000");
		event.setAttempts(0);
		event.setCurrentattempts(0);
		event.setDatecreated(new Date());
		event.setDatemodified(new Date());
//		event.setDatentime(eventPackage.getDateofevent()  + " " + eventPackage.getTimeofevent());
//		event.setEventdatetime(eventPackage.);
		event.setEventid(eventPackage.getId());
		final SiteTeamPackage team1 = ((SiteEventPackage)eventPackage).getSiteteamone();
		final SiteTeamPackage team2 = ((SiteEventPackage)eventPackage).getSiteteamtwo();

		event.setEventid1(team1.getId());
		event.setEventid2(team2.getId());
		event.setEventname("Test");
		event.setEventteam1(team1.getTeam());
		event.setEventteam2(team2.getTeam());
		event.setEventtype("spread");
		event.setIscompleted(false);
		event.setSport(sport);
		event.setWtype("2");

		event.setSpreadplusminusfirstone(team1.getGameSpreadOptionIndicator().get("0"));
		event.setSpreadinputfirstone(team1.getGameSpreadOptionNumber().get("0"));
		event.setSpreadinputjuicefirstone(team1.getGameSpreadOptionJuiceNumber().get("0"));
		event.setSpreadjuiceplusminusfirstone(team1.getGameSpreadOptionJuiceIndicator().get("0"));

		final AccountEvent accountEvent = new AccountEvent();
		accountEvent.setSpreadid(event.getId());
		accountEvent.setAccountid(new Long(1));
		accountEvent.setAttempts(event.getAttempts());
		accountEvent.setGroupid(event.getGroupid());
		accountEvent.setEventname(event.getEventname());
		accountEvent.setEventdatetime(event.getEventdatetime());
		accountEvent.setMaxspreadamount(amountLimit);
		accountEvent.setName(name);
		accountEvent.setOwnerpercentage(100);
		accountEvent.setPartnerpercentage(0);
		accountEvent.setProxy(proxy);
		accountEvent.setSport(event.getSport());
		accountEvent.setType(event.getEventtype());
		accountEvent.setWagertype(event.getWtype());
		accountEvent.setUserid(event.getUserid());
		
		siteProcessor.processSpreadTransaction(event, accountEvent);
		
		LOGGER.info("Exiting testSpread()");
	}

	/**
	 * 
	 * @param siteProcessor
	 * @param eventPackage
	 * @param sport
	 * @param amountLimit
	 * @param name
	 * @param proxy
	 * @throws BatchException
	 */
	protected void testTotal(SiteProcessor siteProcessor, EventPackage eventPackage, String sport, Integer amountLimit, String name, String proxy) throws BatchException {
		LOGGER.info("Entering testTotal()");
		LOGGER.warn("siteProcessor: " + siteProcessor);
		LOGGER.warn("eventPackage: " + eventPackage);
		LOGGER.warn("sport: " + sport);
		LOGGER.warn("amountLimit: " + amountLimit);
		LOGGER.warn("name: " + name);
		LOGGER.warn("proxy: " + proxy);
		
		TotalRecordEvent event = new TotalRecordEvent();
		event.setAccountid(new Long(1));
		event.setAmount("1000");
		event.setAttempts(0);
		event.setCurrentattempts(0);
		event.setDatecreated(new Date());
		event.setDatemodified(new Date());
//		event.setDatentime(eventPackage.getDateofevent()  + " " + eventPackage.getTimeofevent());
//		event.setEventdatetime(eventPackage.);
		event.setEventid(eventPackage.getId());
		final SiteTeamPackage team1 = ((SiteEventPackage)eventPackage).getSiteteamone();
		final SiteTeamPackage team2 = ((SiteEventPackage)eventPackage).getSiteteamtwo();

		event.setEventid1(team1.getId());
		event.setEventid2(team2.getId());
		event.setEventname("Test");
		event.setEventteam1(team1.getTeam());
		event.setEventteam2(team2.getTeam());
		event.setEventtype("spread");
		event.setIscompleted(false);
		event.setSport(sport);
		event.setWtype("2");

		event.setTotalinputfirstone(team1.getGameTotalOptionNumber().get("0"));
		event.setTotalinputjuicefirstone(team1.getGameTotalOptionJuiceNumber().get("0"));
		event.setTotaljuiceplusminusfirstone(team1.getGameTotalOptionJuiceIndicator().get("0"));

		final AccountEvent accountEvent = new AccountEvent();
		accountEvent.setSpreadid(event.getId());
		accountEvent.setAccountid(new Long(1));
		accountEvent.setAttempts(event.getAttempts());
		accountEvent.setGroupid(event.getGroupid());
		accountEvent.setEventname(event.getEventname());
		accountEvent.setEventdatetime(event.getEventdatetime());
		accountEvent.setMaxtotalamount(amountLimit);
		accountEvent.setName(name);
		accountEvent.setOwnerpercentage(100);
		accountEvent.setPartnerpercentage(0);
		accountEvent.setProxy(proxy);
		accountEvent.setSport(event.getSport());
		accountEvent.setType(event.getEventtype());
		accountEvent.setWagertype(event.getWtype());
		accountEvent.setUserid(event.getUserid());

		siteProcessor.processTotalTransaction(event, accountEvent);

		LOGGER.info("Exiting testTotal()");
	}
}