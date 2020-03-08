/**
 * 
 */
package com.wootechnologies.services.dao.sites;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.wootechnologies.errorhandling.BatchErrorCodes;
import com.wootechnologies.errorhandling.BatchErrorMessage;
import com.wootechnologies.errorhandling.BatchException;
import com.wootechnologies.model.AccountEvent;
import com.wootechnologies.model.BaseRecordEvent;
import com.wootechnologies.model.EventPackage;
import com.wootechnologies.model.MlRecordEvent;
import com.wootechnologies.model.SpreadRecordEvent;
import com.wootechnologies.model.TeamTotalRecordEvent;
import com.wootechnologies.model.TotalRecordEvent;

/**
 * @author jmiller
 *
 */
public class SiteWagers {
	private final static Logger LOGGER = Logger.getLogger(SiteWagers.class);
	private float spreadOne = -9999;
	private float spreadTwo = -9999;
	private float spreadJuiceOne = -9999;
	private float spreadJuiceTwo = -9999;
	private float overUnderOne = -9999;
	private float overUnderTwo = -9999;
	private float overUnderJuiceOne = -9999;
	private float overUnderJuiceTwo = -9999;
	private float moneyLineJuiceOne = -9999;
	private float moneyLineJuiceTwo = -9999;
	private AccountEvent accountEvent;
	private SiteProcessor processSite;

	/**
	 * 
	 */
	public SiteWagers() {
		super();
	}

	/**
	 * 
	 * @param processSite
	 */
	public SiteWagers(SiteProcessor processSite) {
		super();
		this.processSite = processSite;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * @return the accountEvent
	 */
	public AccountEvent getAccountEvent() {
		return accountEvent;
	}

	/**
	 * @param accountEvent the accountEvent to set
	 */
	public void setAccountEvent(AccountEvent accountEvent) {
		this.accountEvent = accountEvent;
	}

	/**
	 * @return the processSite
	 */
	public SiteProcessor getProcessSite() {
		return processSite;
	}

	/**
	 * @param processSite the processSite to set
	 */
	public void setProcessSite(SiteProcessor processSite) {
		this.processSite = processSite;
	}

	/**
	 * 
	 * @param spreadRecordEvent
	 * @return
	 * @throws BatchException
	 */
	public static String setupSpreadEventName(SpreadRecordEvent spreadRecordEvent) throws BatchException {
		LOGGER.info("Entering setupEventName()");
		LOGGER.debug("SpreadRecordEvent: " + spreadRecordEvent);
		String eventName = "";

		final int spreadType = determineSpreadData(spreadRecordEvent);
		LOGGER.debug("spreadType: " + spreadType);

		if (spreadType == 1 || spreadType == 2) {
			// Football #312 Kansas -2½ -110 for Game
			final String gType = getGameType(spreadRecordEvent);
			final String bType = getBetType(spreadRecordEvent);
			eventName = gType + " #" + spreadRecordEvent.getEventid1() + 
					" " + spreadRecordEvent.getEventteam1() + " " +
					spreadRecordEvent.getSpreadplusminusfirstone() + spreadRecordEvent.getSpreadinputfirstone() + " " +
					spreadRecordEvent.getSpreadjuiceplusminusfirstone() + spreadRecordEvent.getSpreadinputjuicefirstone();
			if (spreadType == 2) {				
				eventName += " and " + spreadRecordEvent.getSpreadplusminusfirsttwo() + spreadRecordEvent.getSpreadinputfirsttwo() + " " +
				spreadRecordEvent.getSpreadjuiceplusminusfirsttwo() + spreadRecordEvent.getSpreadinputjuicefirsttwo();
			}
			eventName += " for " + bType;
		} else {
			final String gType = getGameType(spreadRecordEvent);
			final String bType = getBetType(spreadRecordEvent);
			eventName = gType + " #" + spreadRecordEvent.getEventid2() + 
					" " + spreadRecordEvent.getEventteam2() + " " +
					spreadRecordEvent.getSpreadplusminussecondone() + spreadRecordEvent.getSpreadinputsecondone() + " " +
					spreadRecordEvent.getSpreadjuiceplusminussecondone() + spreadRecordEvent.getSpreadinputjuicesecondone();
			if (spreadType == 4) {				
				eventName += " and " + spreadRecordEvent.getSpreadplusminussecondtwo() + spreadRecordEvent.getSpreadinputsecondtwo() + " " +
				spreadRecordEvent.getSpreadjuiceplusminussecondtwo() + spreadRecordEvent.getSpreadinputjuicesecondtwo();
			}
			eventName += " for " + bType;
		}

		LOGGER.info("Exiting setupEventName()");
		return eventName;
	}

	/**
	 * 
	 * @param totalRecordEvent
	 * @return
	 * @throws BatchException
	 */
	public static String setupTotalEventName(TotalRecordEvent totalRecordEvent) throws BatchException {
		LOGGER.info("Entering setupTotalEventName()");
		LOGGER.debug("TotalRecordEvent: " + totalRecordEvent);
		String eventName = "";

		final int totalType = determineTotalData(totalRecordEvent);
		LOGGER.debug("totalType: " + totalType);

		// Check if over or under
		if (totalType == 1 || totalType == 2) {
			// Football #312 Kansas -2½ -110 for Game
			String gType = getGameType(totalRecordEvent);
			String bType = getBetType(totalRecordEvent);
			eventName = gType + " #" + totalRecordEvent.getEventid1() + 
					" " + totalRecordEvent.getEventteam1() + " " +
					"o" + totalRecordEvent.getTotalinputfirstone() + " " +
					totalRecordEvent.getTotaljuiceplusminusfirstone() + totalRecordEvent.getTotalinputjuicefirstone();
			if (totalType == 2) {				
				eventName += " and " + "o" + totalRecordEvent.getTotalinputfirsttwo() + " " +
					totalRecordEvent.getTotaljuiceplusminusfirsttwo() + totalRecordEvent.getTotalinputjuicefirsttwo();
			}
			eventName += " for " + bType;
		} else {
			String gType = getGameType(totalRecordEvent);
			String bType = getBetType(totalRecordEvent);
			eventName = gType + " #" + totalRecordEvent.getEventid2() + 
					" " + totalRecordEvent.getEventteam2() + " " +
					"u" + totalRecordEvent.getTotalinputsecondone() + " " +
					totalRecordEvent.getTotaljuiceplusminussecondone() + totalRecordEvent.getTotalinputjuicesecondone();
			if (totalType == 4) {				
				eventName += " and " + "u" + totalRecordEvent.getTotalinputsecondtwo() + " " +
						totalRecordEvent.getTotaljuiceplusminussecondtwo() + totalRecordEvent.getTotalinputjuicesecondtwo();
			}
			eventName += " for " + bType;
		}

		LOGGER.info("Exiting setupTotalEventName()");
		return eventName;
	}

	/**
	 * 
	 * @param mlRecordEvent
	 * @return
	 * @throws BatchException
	 */
	public static String setupMlEventName(MlRecordEvent mlRecordEvent) throws BatchException {
		LOGGER.info("Entering setupMlEventName()");
		LOGGER.debug("MlRecordEvent: " + mlRecordEvent);
		String eventName = "";
		
		final int mlType = determineMoneyLineData(mlRecordEvent);
		LOGGER.debug("mlType: " + mlType);

		// Check if over or under
		if (mlType == 1 || mlType == 2) {
			final String gType = getGameType(mlRecordEvent);
			final String bType = getBetType(mlRecordEvent);
			eventName = gType + " #" + mlRecordEvent.getEventid1() + 
					" " + mlRecordEvent.getEventteam1() + " " +
					mlRecordEvent.getMlplusminusfirstone() + mlRecordEvent.getMlinputfirstone();
			if (mlType == 2) {				
				eventName += " and " + mlRecordEvent.getMlplusminusfirsttwo() + mlRecordEvent.getMlinputfirsttwo();
			}
			eventName += " for " + bType;
		} else {
			final String gType = getGameType(mlRecordEvent);
			final String bType = getBetType(mlRecordEvent);
			eventName = gType + " #" + mlRecordEvent.getEventid2() + 
					" " + mlRecordEvent.getEventteam2() + " " +
					mlRecordEvent.getMlplusminussecondone() + mlRecordEvent.getMlinputsecondone();
			if (mlType == 2) {				
				eventName += " and " + mlRecordEvent.getMlplusminussecondtwo() + mlRecordEvent.getMlinputsecondtwo();
			}
			eventName += " for " + bType;
		}

		LOGGER.info("Entering setupMlEventName()");
		return eventName;
	}

	/**
	 * 
	 * @param spreadRecordEvent
	 * @param eventPackage
	 * @return
	 * @throws BatchException
	 */
	public SiteTransaction setupSpreads(SpreadRecordEvent spreadRecordEvent, EventPackage eventPackage) throws BatchException {
		LOGGER.info("Entering setupSpreads()");
		LOGGER.debug("SpreadRecordEvent: " + spreadRecordEvent);
		LOGGER.debug("EventPackage: " + eventPackage);
		SiteTransaction siteTransaction = null;
		int spreadType = 1;

		// Check which type of bet just occurred
		if (this.accountEvent != null && 
			this.accountEvent.getSpreadjuice() != null &&
			this.accountEvent.getSpreadjuice().floatValue() != 0) {
			if (this.accountEvent.getEventid() % 2 == 0) {
				spreadType = 3;
			}
			spreadOne = this.accountEvent.getSpread();
			spreadJuiceOne = this.accountEvent.getSpreadjuice();
		} else {
			// Setup spread information
			spreadType = determineSpreadData(spreadRecordEvent);
			LOGGER.debug("spreadType: " + spreadType);
			
			// Setup the event ID
			if (spreadType == 1 || spreadType == 2) {
				accountEvent.setEventid(spreadRecordEvent.getEventid1());
			} else {
				accountEvent.setEventid(spreadRecordEvent.getEventid2());
			}

			// Populate the spread data
			populateSpreadData(spreadType, spreadRecordEvent);
		}

		final SiteTeamPackage siteTeamPackage = setupTeam(spreadType, eventPackage);
		LOGGER.debug("SiteTeamPackage: " + siteTeamPackage);

		float[] siteSpreads = null;
		float[] siteJuice = null;

		// Get site spreads and spread juices
		if (siteTeamPackage != null && siteTeamPackage.getGameSpreadOptionNumber() != null
				&& siteTeamPackage.getGameSpreadOptionNumber().size() > 0
				&& siteTeamPackage.getGameSpreadOptionJuiceNumber() != null
				&& siteTeamPackage.getGameSpreadOptionJuiceNumber().size() > 0) {
			siteSpreads = getSiteSpreads(siteTeamPackage);
			siteJuice = getSiteSpreadJuice(siteTeamPackage);
		} else {
			throw new BatchException(BatchErrorCodes.SITE_SPREAD_TEAM_PACKAGE_ISSUE,
					BatchErrorMessage.SITE_SPREAD_TEAM_PACKAGE_ISSUE, "There are no site spread values for this game",
					siteTeamPackage.toString());
		}

		// Now check spreads
		if (spreadOne == -9999) {
			// This means we don't have valid data
			new BatchException("Invalid spreadOne: " + spreadOne);
		} else if (spreadOne < 0) { // -
			// if we are negative, anything lower than the value is good
			siteTransaction = spreadNegative(spreadRecordEvent, siteTeamPackage, siteSpreads, siteJuice);
		} else if (spreadOne >= 0) { // Pickem or +
			siteTransaction = spreadPositive(spreadRecordEvent, siteTeamPackage, siteSpreads, siteJuice);
		}

		LOGGER.info("Exiting setupSpreads()");
		return siteTransaction;
	}

	/**
	 * 
	 * @param spreadRecordEvent
	 * @return
	 * @throws BatchException
	 */
	public Map<String, String> determineSpread(SpreadRecordEvent spreadRecordEvent) throws BatchException {
		LOGGER.info("Entering determineSpread()");
		LOGGER.debug("SpreadRecordEvent: " + spreadRecordEvent);
		final Map<String, String> map = new HashMap<String, String>();

		// Setup spread information
		int spreadType = determineSpreadData(spreadRecordEvent);
		LOGGER.debug("spreadType: " + spreadType);

		// Populate the spread data
		populateSpreadMapData(spreadType, spreadRecordEvent, map);

		LOGGER.info("Exiting determineSpread()");
		return map;
	}

	/**
	 * 
	 * @param totalRecordEvent
	 * @param eventPackage
	 * @return
	 * @throws BatchException
	 */
	public SiteTransaction setupTotals(TotalRecordEvent totalRecordEvent, EventPackage eventPackage) throws BatchException {
		LOGGER.info("Entering setupTotals()");
		LOGGER.debug("TotalRecordEvent: " + totalRecordEvent);
		LOGGER.debug("EventPackage: " + eventPackage);
		SiteTransaction siteTransaction = null;
		int totalType = 1;

		// Check which type of bet just occurred
		if (this.accountEvent != null &&
			this.accountEvent.getTotaljuice() != null &&
			this.accountEvent.getTotaljuice().floatValue() != 0) {
			if ("u".equals(this.accountEvent.getTotalindicator())) {
				totalType = 3;
				overUnderTwo = this.accountEvent.getTotal();
				overUnderJuiceTwo = this.accountEvent.getTotaljuice();
			} else {
				overUnderOne = this.accountEvent.getTotal();
				overUnderJuiceOne = this.accountEvent.getTotaljuice();				
			}
		} else {
			totalType = determineTotalData(totalRecordEvent);
			LOGGER.debug("totalType: " + totalType);
	
			if (totalType == 1 || totalType == 2) {
				accountEvent.setEventid(totalRecordEvent.getEventid1());
			} else {
				accountEvent.setEventid(totalRecordEvent.getEventid2());
			}
	
			// Populate the total data
			populateTotalData(totalType, totalRecordEvent);
		}

		final SiteTeamPackage siteTeamPackage = setupTeam(totalType, eventPackage);
		LOGGER.debug("SiteTeamPackage: " + siteTeamPackage);

		float[] siteTotals = null;
		float[] siteTotalJuice = null;

		// Get site totals and total juices
		if (siteTeamPackage != null && siteTeamPackage.getGameTotalOptionNumber() != null && siteTeamPackage.getGameTotalOptionNumber().size() > 0 &&
			siteTeamPackage.getGameTotalOptionJuiceNumber() != null && siteTeamPackage.getGameTotalOptionJuiceNumber().size() > 0) {
			siteTotals = getSiteTotals(siteTeamPackage);
			siteTotalJuice = getSiteTotalJuice(siteTeamPackage);
		} else {
			throw new BatchException(BatchErrorCodes.SITE_TOTAL_TEAM_PACKAGE_ISSUE, BatchErrorMessage.SITE_TOTAL_TEAM_PACKAGE_ISSUE, "There are no site total values for this game", siteTeamPackage.toString());
		}

		if (overUnderOne == -9999) {
			// This means we don't have valid data
			new BatchException("Invalid totals overUnderOne: " + overUnderOne);
		}

		// Check for over vs. under
		if (totalType == 1 || totalType == 2) { // Over
			accountEvent.setEventid(totalRecordEvent.getEventid1());
			siteTransaction = determineOver(totalRecordEvent, siteTeamPackage, overUnderOne, overUnderJuiceOne, siteTotals, siteTotalJuice);
			
			// Now check for over two
			if (siteTransaction == null && overUnderTwo != -9999) {
				// We have both
				siteTransaction = determineOver(totalRecordEvent, siteTeamPackage, overUnderTwo, overUnderJuiceTwo, siteTotals, siteTotalJuice);
				if (siteTransaction == null) {
					// Total over for 2 also too low
					accountEvent.setTotalindicator("o");
					accountEvent.setTotal(overUnderTwo);
					accountEvent.setTotaljuice(overUnderJuiceTwo);
					throw new BatchException(BatchErrorCodes.TOTAL_OVER_TOO_LOW, BatchErrorMessage.TOTAL_OVER_TOO_LOW, "Total over two value is " + setupTotalValue("o", overUnderTwo) + " and Site Totals are " + setupSiteTotalValue("o", siteTotals) + " Total Juice two value is " + setupJuiceValue(overUnderJuiceTwo) + " Total Site Juices are " + setupSiteJuiceValue(siteTotalJuice));
				}
				accountEvent.setTotalindicator("o");
			} else if (siteTransaction != null) {
				accountEvent.setTotalindicator("o");
			} else {
				// Total over for 1 is too low
				accountEvent.setTotalindicator("o");
				accountEvent.setTotal(overUnderOne);
				accountEvent.setTotaljuice(overUnderJuiceOne);
				throw new BatchException(BatchErrorCodes.TOTAL_OVER_TOO_LOW, BatchErrorMessage.TOTAL_OVER_TOO_LOW, "Total over one value is " + setupTotalValue("o", overUnderOne) + " and Site Totals are " + setupSiteTotalValue("o", siteTotals) + " Total Juice one value is " + setupJuiceValue(overUnderJuiceOne )+ " Total Site Juices are " + setupSiteJuiceValue(siteTotalJuice));				
			}
		} else { // Under
			accountEvent.setEventid(totalRecordEvent.getEventid2());
			siteTransaction = determineUnder(totalRecordEvent, siteTeamPackage, overUnderOne, overUnderJuiceOne, siteTotals, siteTotalJuice);

			// Now check for under two
			if (siteTransaction == null && overUnderTwo != -9999) {
				// We have both
				siteTransaction = determineUnder(totalRecordEvent, siteTeamPackage, overUnderTwo, overUnderJuiceTwo, siteTotals, siteTotalJuice);
				if (siteTransaction == null) {
					// Total over for 2 also too low
					accountEvent.setTotalindicator("u");
					accountEvent.setTotal(overUnderTwo);
					accountEvent.setTotaljuice(overUnderJuiceTwo);
					throw new BatchException(BatchErrorCodes.TOTAL_UNDER_TOO_HIGH, BatchErrorMessage.TOTAL_UNDER_TOO_HIGH, "Total under two value is " + setupTotalValue("u", overUnderTwo) + " and Site Totals are " + setupSiteTotalValue("u", siteTotals) + " Total Juice two value is " + setupJuiceValue(overUnderJuiceTwo) + " Total Site Juices are " + setupSiteJuiceValue(siteTotalJuice));
				}
				accountEvent.setTotalindicator("u");
			} else if (siteTransaction != null) {
				accountEvent.setTotalindicator("u");
			} else {
				// Total over for 1 is too low
				accountEvent.setTotalindicator("u");
				accountEvent.setTotal(overUnderOne);
				accountEvent.setTotaljuice(overUnderJuiceOne);
				throw new BatchException(BatchErrorCodes.TOTAL_UNDER_TOO_HIGH, BatchErrorMessage.TOTAL_UNDER_TOO_HIGH, "Total under one value is " + setupTotalValue("u", overUnderOne) + " and Site Totals are " + setupSiteTotalValue("u", siteTotals) + " Total Juice one value is " + setupJuiceValue(overUnderJuiceOne) + " Total Site Juices are " + setupSiteJuiceValue(siteTotalJuice));				
			}
		}

		LOGGER.info("Exiting setupTotals()");
		return siteTransaction;
	}

	/**
	 * 
	 * @param totalRecordEvent
	 * @return
	 * @throws BatchException
	 */
	public Map<String, String> determineTotal(TotalRecordEvent totalRecordEvent) throws BatchException {
		LOGGER.info("Entering determineTotal()");
		LOGGER.debug("TotalRecordEvent: " + totalRecordEvent);
		final Map<String, String> map = new HashMap<String, String>();

		int totalType = determineTotalData(totalRecordEvent);
		LOGGER.debug("totalType: " + totalType);

		// Populate the total data
		populateTotalMapData(totalType, totalRecordEvent, map);

		LOGGER.info("Exiting determineTotal()");
		return map;
	}

	/**
	 * 
	 * @param mlRecordEvent
	 * @param eventPackage
	 * @return
	 * @throws BatchException
	 */
	public SiteTransaction setupMoneyLine(MlRecordEvent mlRecordEvent, EventPackage eventPackage) throws BatchException {
		LOGGER.info("Entering setupMoneyLine()");
		LOGGER.debug("MlRecordEvent: " + mlRecordEvent);
		LOGGER.debug("EventPackage: " + eventPackage);
		SiteTransaction siteTransaction = null;
		int mlType = 1;

		// Check which type of bet just occurred
		// Check which type of bet just occurred
		if (this.accountEvent != null &&
			this.accountEvent.getMljuice() != null &&
			this.accountEvent.getMljuice().floatValue() != 0) {
			if (this.accountEvent.getEventid() % 2 == 0) {
				mlType = 3;
			}
			moneyLineJuiceOne = this.accountEvent.getMljuice();
		} else {
			mlType = determineMoneyLineData(mlRecordEvent);
			LOGGER.debug("mlType: " + mlType);

			if (mlType == 1 || mlType == 2) {
				accountEvent.setEventid(mlRecordEvent.getEventid1());
			} else {
				accountEvent.setEventid(mlRecordEvent.getEventid2());
			}

			// Populate the total data		
			populateMoneyLineData(mlType, mlRecordEvent);
		}

		final SiteTeamPackage siteTeamPackage = setupTeam(mlType, eventPackage);
		LOGGER.debug("SiteTeamPackage: " + siteTeamPackage);
		
		float[] siteMlJuice = null;
		if (siteTeamPackage != null && siteTeamPackage.getGameMLOptionJuiceNumber() != null && siteTeamPackage.getGameMLOptionJuiceNumber().size() > 0) {
			siteMlJuice = getSiteMlJuice(siteTeamPackage);
		} else {
			throw new BatchException(BatchErrorCodes.SITE_ML_TEAM_PACKAGE_ISSUE, BatchErrorMessage.SITE_ML_TEAM_PACKAGE_ISSUE, "There are no site moneyline values for this game", siteTeamPackage.toString());
		}

		if (moneyLineJuiceOne == -9999) {
			// This means we don't have valid data
			new BatchException("Invalid money line moneyLineJuiceOne: " + moneyLineJuiceOne);
		} else {
			siteTransaction = moneyLineMoreThan(mlRecordEvent, siteTeamPackage, moneyLineJuiceOne, siteMlJuice);

			// Check if it was setup correctly
			if (siteTransaction != null) {
				if (moneyLineJuiceOne > 0) {
					accountEvent.setMlindicator("+");
				} else {
					accountEvent.setMlindicator("");
				}
			} else if (moneyLineJuiceTwo != -9999 && siteTransaction == null) { // Check for the second if first not matching
				// Try the second one
				siteTransaction = moneyLineMoreThan(mlRecordEvent, siteTeamPackage, moneyLineJuiceTwo, siteMlJuice);
				if (siteTransaction != null) {
					if (moneyLineJuiceTwo > 0) {
						accountEvent.setMlindicator("+");
					} else {
						accountEvent.setMlindicator("");
					}
				} else {
					if (moneyLineJuiceTwo > 0) {
						accountEvent.setMlindicator("+");
						accountEvent.setMljuice(moneyLineJuiceTwo);
					} else {
						accountEvent.setMlindicator("");
						accountEvent.setMljuice(moneyLineJuiceTwo);
					}
					throw new BatchException(BatchErrorCodes.ML_DOES_NOT_MATCH, BatchErrorMessage.ML_DOES_NOT_MATCH, "Moneyline two value is " + setupJuiceValue(moneyLineJuiceTwo) + " and Site Juices are " + setupSiteJuiceValue(siteMlJuice));
				}
			} else {
				if (moneyLineJuiceTwo > 0) {
					accountEvent.setMlindicator("+");
					accountEvent.setMljuice(moneyLineJuiceTwo);
				} else {
					accountEvent.setMlindicator("");
					accountEvent.setMljuice(moneyLineJuiceTwo);
				}
				throw new BatchException(BatchErrorCodes.ML_DOES_NOT_MATCH, BatchErrorMessage.ML_DOES_NOT_MATCH, "Moneyline value is " + setupJuiceValue(moneyLineJuiceOne) + " and Site Juices are " + setupSiteJuiceValue(siteMlJuice));				
			}
		}

		LOGGER.info("Exiting setupMoneyLine()");
		return siteTransaction;
	}

	/**
	 * 
	 * @param mlRecordEvent
	 * @return
	 * @throws BatchException
	 */
	public Map<String, String> determineMoneyLine(MlRecordEvent mlRecordEvent) throws BatchException {
		LOGGER.info("Entering determineMoneyLine()");
		LOGGER.debug("MlRecordEvent: " + mlRecordEvent);
		final Map<String, String> map = new HashMap<String, String>();

		// Determine money line
		int mlType = determineMoneyLineData(mlRecordEvent);
		LOGGER.debug("mlType: " + mlType);

		// Populate the total data
		populateMoneyLineMapData(mlType, mlRecordEvent, map);

		LOGGER.info("Exiting setupMoneyLine()");
		return map;
	}

	/**
	 * 
	 * @param sre
	 * @return
	 * @throws BatchException
	 */
	public static int determineSpreadData(SpreadRecordEvent sre) throws BatchException {
		LOGGER.info("Entering determineSpreadData()");
		LOGGER.debug("SpreadRecordEvent: " + sre);

		// 1 = Spread on first only
		// 2 = Spread on first/second both
		// 3 = Spread on second only
		// 4 = Spread on second/second both
		int retValue = 1;
		if (sre.getSpreadinputfirstone() != null && sre.getSpreadinputfirstone().length() > 0) {
			if (sre.getSpreadinputfirsttwo() != null && sre.getSpreadinputfirsttwo().length() > 0) {
				// We have two spreads
				if (sre.getSpreadinputjuicefirsttwo() != null && sre.getSpreadinputjuicefirsttwo().length() > 0) {
					retValue = 2;
				} else {
					// we have a problem
					throw new BatchException("No Juice value for Spread first two");
				}
			} else {
				// We just have the first spread value
				if (sre.getSpreadinputjuicefirstone() != null && sre.getSpreadinputjuicefirstone().length() > 0) {
					retValue = 1;
				} else {
					// we have a problem and we need to write out the error
					throw new BatchException("No Juice value for Spread first one");
				}
			}
		} else if (sre.getSpreadinputsecondone() != null && sre.getSpreadinputsecondone().length() > 0) {
			if (sre.getSpreadinputsecondtwo() != null && sre.getSpreadinputsecondtwo().length() > 0) {
				if (sre.getSpreadinputjuicesecondtwo() != null && sre.getSpreadinputjuicesecondtwo().length() > 0) {
					retValue = 4;
				} else {
					throw new BatchException("No Juice value for Spread second two");
				}
			} else {
				if (sre.getSpreadinputjuicesecondone() != null && sre.getSpreadinputjuicesecondone().length() > 0) {
					retValue = 3;
				} else {
					throw new BatchException("No Juice value for Spread second one");
				}
			}
		} else {
			throw new BatchException("No spread for either one or two");
		}

		LOGGER.info("Exiting determineSpreadData()");
		return retValue;
	}

	/**
	 * 
	 * @param tre
	 * @return
	 * @throws BatchException
	 */
	public static int determineTotalData(TotalRecordEvent tre) throws BatchException {
		LOGGER.info("Entering determineTotalData()");
		LOGGER.debug("TotalRecordEvent: " + tre);

		// 1 = Total on first only
		// 2 = Total on first/second both
		// 3 = Total on second only
		// 4 = Total on second/second both
		int retValue = 1;
		if (tre.getTotalinputfirstone() != null && tre.getTotalinputfirstone().length() > 0) {
			if (tre.getTotalinputfirsttwo() != null && tre.getTotalinputfirsttwo().length() > 0) {
				// We have two spreads
				if (tre.getTotalinputjuicefirsttwo() != null && tre.getTotalinputjuicefirsttwo().length() > 0) {
					retValue = 2;
				} else {
					// we have a problem
					throw new BatchException("No Juice value for Total first two");
				}
			} else {
				// We just have the first spread value
				if (tre.getTotalinputjuicefirstone() != null && tre.getTotalinputjuicefirstone().length() > 0) {
					retValue = 1;
				} else {
					// we have a problem and we need to write out the error
					throw new BatchException("No Juice value for Total first one");
				}
			}
		} else if (tre.getTotalinputsecondone() != null && tre.getTotalinputsecondone().length() > 0) {
			if (tre.getTotalinputsecondtwo() != null && tre.getTotalinputsecondtwo().length() > 0) {
				if (tre.getTotalinputjuicesecondtwo() != null && tre.getTotalinputjuicesecondtwo().length() > 0) {
					retValue = 4;
				} else {
					throw new BatchException("No Juice value for Total second two");
				}
			} else {
				if (tre.getTotalinputjuicesecondone() != null && tre.getTotalinputjuicesecondone().length() > 0) {
					retValue = 3;
				} else {
					throw new BatchException("No Juice value for Total second one");
				}
			}
		} else {
			throw new BatchException("No Total for either one or two");
		}

		LOGGER.info("Exiting determineTotalData()");
		return retValue;
	}

	/**
	 * 
	 * @param mlRecordEvent
	 * @return
	 * @throws BatchException
	 */
	public static int determineMoneyLineData(MlRecordEvent mlRecordEvent) throws BatchException {
		LOGGER.info("Entering determineMoneyLineData()");
		LOGGER.debug("MlRecordEvent: " + mlRecordEvent);

		// 1 = MoneyLine on first only
		// 2 = MoneyLine on first/second both
		// 3 = MoneyLine on second only
		// 4 = MoneyLine on second/second both
		int retValue = 1;
		if (mlRecordEvent.getMlinputfirstone() != null && mlRecordEvent.getMlinputfirstone().length() > 0) {
			if (mlRecordEvent.getMlinputfirsttwo() != null && mlRecordEvent.getMlinputfirsttwo().length() > 0) {
				retValue = 2;
			} else {
				retValue = 1;
			}
		} else if (mlRecordEvent.getMlinputsecondone() != null && mlRecordEvent.getMlinputsecondone().length() > 0) {
			if (mlRecordEvent.getMlinputsecondtwo() != null && mlRecordEvent.getMlinputsecondtwo().length() > 0) {
				retValue = 4;
			} else {
				retValue = 3;
			}
		} else {
			throw new BatchException("No MoneyLine for either one or two");
		}

		LOGGER.info("Exiting determineMoneyLineData()");
		return retValue;
	}
	
	
	/**
	 * 
	 * @param teamTotalRecordEvent
	 * @param eventPackage
	 * @return
	 * @throws BatchException
	 * newly added method on 31-10-18
	 */
	public SiteTransaction setupTeamTotals(TeamTotalRecordEvent teamTotalRecordEvent, EventPackage eventPackage) throws BatchException {
		LOGGER.info("Entering setupTotals()");
		LOGGER.debug("TotalRecordEvent: " + teamTotalRecordEvent);
		LOGGER.debug("EventPackage: " + eventPackage);
		SiteTransaction siteTransaction = null;
		int totalType = 1;

		// Check which type of bet just occurred
		if (this.accountEvent != null &&
			this.accountEvent.getTeamtotaljuice() != null &&
			this.accountEvent.getTeamtotaljuice().floatValue() != 0) {
			if ("u".equals(this.accountEvent.getTeamtotalindicator())) {
				totalType = 3;
				overUnderTwo = this.accountEvent.getTeamtotal();
				overUnderJuiceTwo = this.accountEvent.getTeamtotaljuice();
			} else {
				overUnderOne = this.accountEvent.getTeamtotal();
				overUnderJuiceOne = this.accountEvent.getTeamtotaljuice();				
			}
		} else {
			/////////***
			totalType = determineTeamTotalData(teamTotalRecordEvent);
			LOGGER.debug("totalType: " + totalType);
	
			if (totalType == 1 || totalType == 2) {
				accountEvent.setEventid(teamTotalRecordEvent.getEventid1());
			} else {
				accountEvent.setEventid(teamTotalRecordEvent.getEventid2());
			}
	
			// Populate the total data
			populateTeamTotalData(totalType, teamTotalRecordEvent);
		}

		final SiteTeamPackage siteTeamPackage = setupTeam(totalType, eventPackage);
		LOGGER.debug("SiteTeamPackage: " + siteTeamPackage);

		float[] siteTotals = null;
		float[] siteTotalJuice = null;

		// Get site totals and total juices
		if (siteTeamPackage != null && siteTeamPackage.getGameTeamTotalOptionNumber() != null && siteTeamPackage.getGameTeamTotalOptionNumber().size() > 0 &&
			siteTeamPackage.getGameTeamTotalOptionJuiceNumber() != null && siteTeamPackage.getGameTeamTotalOptionJuiceNumber().size() > 0) {
			siteTotals = getSiteTotals(siteTeamPackage);
			siteTotalJuice = getSiteTotalJuice(siteTeamPackage);
		} else {
			throw new BatchException(BatchErrorCodes.TEAM_TOTAL_EVENT_NOT_FOUND, BatchErrorMessage.TEAM_TOTAL_EVENT_NOT_FOUND, "There are no team total values present for this game", siteTeamPackage.toString());
		}

		if (overUnderOne == -9999) {
			// This means we don't have valid data
			new BatchException("Invalid totals overUnderOne: " + overUnderOne);
		}

		// Check for over vs. under
		if (totalType == 1 || totalType == 2) { // Over
			accountEvent.setEventid(teamTotalRecordEvent.getEventid1());
			siteTransaction = determineOver(teamTotalRecordEvent, siteTeamPackage, overUnderOne, overUnderJuiceOne, siteTotals, siteTotalJuice);
			
			// Now check for over two
			if (siteTransaction == null && overUnderTwo != -9999) {
				// We have both
				siteTransaction = determineOver(teamTotalRecordEvent, siteTeamPackage, overUnderTwo, overUnderJuiceTwo, siteTotals, siteTotalJuice);
				if (siteTransaction == null) {
					// Total over for 2 also too low
					accountEvent.setTeamtotalindicator("o");
					accountEvent.setTeamtotal(overUnderTwo);
					accountEvent.setTeamtotaljuice(overUnderJuiceTwo);
					throw new BatchException(BatchErrorCodes.TOTAL_OVER_TOO_LOW, BatchErrorMessage.TOTAL_OVER_TOO_LOW, "Total over two value is " + setupTotalValue("o", overUnderTwo) + " and Site Totals are " + setupSiteTotalValue("o", siteTotals) + " Total Juice two value is " + setupJuiceValue(overUnderJuiceTwo) + " Total Site Juices are " + setupSiteJuiceValue(siteTotalJuice));
				}
				accountEvent.setTeamtotalindicator("o");
			} else if (siteTransaction != null) {
				accountEvent.setTeamtotalindicator("o");
			} else {
				// Total over for 1 is too low
				accountEvent.setTeamtotalindicator("o");
				accountEvent.setTeamtotal(overUnderOne);
				accountEvent.setTeamtotaljuice(overUnderJuiceOne);
				throw new BatchException(BatchErrorCodes.TOTAL_OVER_TOO_LOW, BatchErrorMessage.TOTAL_OVER_TOO_LOW, "Total over one value is " + setupTotalValue("o", overUnderOne) + " and Site Totals are " + setupSiteTotalValue("o", siteTotals) + " Total Juice one value is " + setupJuiceValue(overUnderJuiceOne )+ " Total Site Juices are " + setupSiteJuiceValue(siteTotalJuice));				
			}
		} else { // Under
			accountEvent.setEventid(teamTotalRecordEvent.getEventid2());
			siteTransaction = determineUnder(teamTotalRecordEvent, siteTeamPackage, overUnderOne, overUnderJuiceOne, siteTotals, siteTotalJuice);

			// Now check for under two
			if (siteTransaction == null && overUnderTwo != -9999) {
				// We have both
				siteTransaction = determineUnder(teamTotalRecordEvent, siteTeamPackage, overUnderTwo, overUnderJuiceTwo, siteTotals, siteTotalJuice);
				if (siteTransaction == null) {
					// Total over for 2 also too low
					accountEvent.setTeamtotalindicator("u");
					accountEvent.setTeamtotal(overUnderTwo);
					accountEvent.setTeamtotaljuice(overUnderJuiceTwo);
					throw new BatchException(BatchErrorCodes.TOTAL_UNDER_TOO_HIGH, BatchErrorMessage.TOTAL_UNDER_TOO_HIGH, "Total under two value is " + setupTotalValue("u", overUnderTwo) + " and Site Totals are " + setupSiteTotalValue("u", siteTotals) + " Total Juice two value is " + setupJuiceValue(overUnderJuiceTwo) + " Total Site Juices are " + setupSiteJuiceValue(siteTotalJuice));
				}
				accountEvent.setTeamtotalindicator("u");
			} else if (siteTransaction != null) {
				accountEvent.setTeamtotalindicator("u");
			} else {
				// Total over for 1 is too low
				accountEvent.setTeamtotalindicator("u");
				accountEvent.setTeamtotal(overUnderOne);
				accountEvent.setTeamtotaljuice(overUnderJuiceOne);
				throw new BatchException(BatchErrorCodes.TOTAL_UNDER_TOO_HIGH, BatchErrorMessage.TOTAL_UNDER_TOO_HIGH, "Total under one value is " + setupTotalValue("u", overUnderOne) + " and Site Totals are " + setupSiteTotalValue("u", siteTotals) + " Total Juice one value is " + setupJuiceValue(overUnderJuiceOne) + " Total Site Juices are " + setupSiteJuiceValue(siteTotalJuice));				
			}
		}

		LOGGER.info("Exiting setupTotals()");
		return siteTransaction;
	}
	
	/**
	 * 
	 * @param tre
	 * @return
	 * @throws BatchException
	 * newly added method on 31-10-18
	 */
	public static int determineTeamTotalData(TeamTotalRecordEvent tre) throws BatchException {
		LOGGER.info("Entering determineTotalData()");
		LOGGER.debug("TotalRecordEvent: " + tre);

		// 1 = Total on first only
		// 2 = Total on first/second both
		// 3 = Total on second only
		// 4 = Total on second/second both
		int retValue = 1;
		if (tre.getTeamTotalinputfirstone() != null && tre.getTeamTotalinputfirstone().length() > 0) {
			if (tre.getTeamTotalinputfirsttwo() != null && tre.getTeamTotalinputfirsttwo().length() > 0) {
				// We have two team total
				if (tre.getTeamTotalinputjuicefirsttwo() != null && tre.getTeamTotalinputjuicefirsttwo().length() > 0) {
					retValue = 2;
				} else {
					// we have a problem
					throw new BatchException("No Juice value for Team Total first two");
				}
			} else {
				// We just have the first team total value
				if (tre.getTeamTotalinputjuicefirstone() != null && tre.getTeamTotalinputjuicefirstone().length() > 0) {
					retValue = 1;
				} else {
					// we have a problem and we need to write out the error
					throw new BatchException("No Juice value for Team Total first one");
				}
			}
		} else if (tre.getTeamTotalinputsecondone() != null && tre.getTeamTotalinputsecondone().length() > 0) {
			if (tre.getTeamTotalinputsecondtwo() != null && tre.getTeamTotalinputsecondtwo().length() > 0) {
				if (tre.getTeamTotalinputjuicesecondtwo() != null && tre.getTeamTotalinputjuicesecondtwo().length() > 0) {
					retValue = 4;
				} else {
					throw new BatchException("No Juice value for Team Total second two");
				}
			} else {
				if (tre.getTeamTotalinputjuicesecondone() != null && tre.getTeamTotalinputjuicesecondone().length() > 0) {
					retValue = 3;
				} else {
					throw new BatchException("No Juice value for Team Total second one");
				}
			}
		} else {
			throw new BatchException("No Team Total for either one or two");
		}

		LOGGER.info("Exiting determineTeamTeamTotalData()");
		return retValue;
	}	
	
	
	/**
	 * 
	 * @param totalType
	 * @param totalRecordEvent
	 * @throws BatchException
	 * newly added method on 31-10-18
	 */
	private void populateTeamTotalData(int totalType, TeamTotalRecordEvent teamTotalRecordEvent) throws BatchException {
		LOGGER.info("Entering populateTeamTotalData()");

		switch (totalType) {
			// Over
			case 1:
				overUnderOne = Float.parseFloat(teamTotalRecordEvent.getTeamTotalinputfirstone());
				overUnderJuiceOne = parseFloatData(teamTotalRecordEvent.getTeamTotaljuiceplusminusfirstone(), teamTotalRecordEvent.getTeamTotalinputjuicefirstone());
				break;
			// Over
			case 2:
				overUnderOne = Float.parseFloat(teamTotalRecordEvent.getTeamTotalinputfirstone());
				overUnderTwo = Float.parseFloat(teamTotalRecordEvent.getTeamTotalinputfirsttwo());
				overUnderJuiceOne = parseFloatData(teamTotalRecordEvent.getTeamTotaljuiceplusminusfirstone(), teamTotalRecordEvent.getTeamTotalinputjuicefirstone());
				overUnderJuiceTwo = parseFloatData(teamTotalRecordEvent.getTeamTotaljuiceplusminusfirsttwo(), teamTotalRecordEvent.getTeamTotalinputjuicefirsttwo());
				break;
			// Under
			case 3:
				overUnderOne = Float.parseFloat(teamTotalRecordEvent.getTeamTotalinputsecondone());
				overUnderJuiceOne = parseFloatData(teamTotalRecordEvent.getTeamTotaljuiceplusminussecondone(), teamTotalRecordEvent.getTeamTotalinputjuicesecondone());
				break;
			// Under
			case 4:
				overUnderOne = Float.parseFloat(teamTotalRecordEvent.getTeamTotalinputsecondone());
				overUnderTwo = Float.parseFloat(teamTotalRecordEvent.getTeamTotalinputsecondtwo());
				overUnderJuiceOne = parseFloatData(teamTotalRecordEvent.getTeamTotaljuiceplusminussecondone(), teamTotalRecordEvent.getTeamTotalinputjuicesecondone());
				overUnderJuiceTwo = parseFloatData(teamTotalRecordEvent.getTeamTotaljuiceplusminussecondtwo(), teamTotalRecordEvent.getTeamTotalinputjuicesecondtwo());
				break;
			default:
				throw new BatchException("Cannot determine team total numbers");
		}

		LOGGER.info("Exiting populateTeamTeamTotalData()");
	}
	
	/**
	 * 
	 * @param teamTotalRecordEvent
	 * @param siteTeamPackage
	 * @param totalOverUnder
	 * @param totalOverUnderJuice
	 * @param siteTotals
	 * @param siteTotalJuices
	 * @return
	 * @throws BatchException
	 * newly added on 31-10-18
	 */
	protected SiteTransaction determineUnder(TeamTotalRecordEvent teamTotalRecordEvent, SiteTeamPackage siteTeamPackage, float totalOverUnder, float totalOverUnderJuice, float[] siteTotals, float[] siteTotalJuices) throws BatchException {
		LOGGER.info("Entering determineUnder()");
		LOGGER.debug("TeamTotalRecordEvent: " + teamTotalRecordEvent);
		LOGGER.debug("SiteTeamPackage: " + siteTeamPackage);
		LOGGER.debug("totalOverUnder: " + totalOverUnder);
		LOGGER.debug("totalOverUnderJuice: " + totalOverUnderJuice);
		LOGGER.debug("siteTotals: " + siteTotals);
		LOGGER.debug("siteTotalJuices: " + siteTotalJuices);
		SiteTransaction siteTransaction = null;

		// Loop through site spreads
		for (int x = 0; x < siteTotals.length; x++) {
			LOGGER.debug("totalOverUnder: " + totalOverUnder);
			LOGGER.debug("siteTotals[x]: " + siteTotals[x]);
			if (totalOverUnder <= siteTotals[x]) {
				// Now check the juice to make sure it is correct
				LOGGER.debug("totalOverUnderJuice: " + totalOverUnderJuice);
				LOGGER.debug("siteTotalJuices[x]: " + siteTotalJuices[x]);
				if (totalOverUnderJuice <= siteTotalJuices[x]) {
					siteTransaction = processSite.createTeamTotalTransaction(siteTeamPackage, teamTotalRecordEvent, x, siteTotalJuices[x], accountEvent);
					accountEvent.setTeamtotal(siteTotals[x]);
					accountEvent.setTeamtotaljuice(siteTotalJuices[x]);
					
					if (siteTotalJuices[x] < 0) {
						// To Win
						siteTransaction.setRiskorwin(1);
					} else {
						// Risk
						siteTransaction.setRiskorwin(2);
					}
					break;
				}
			}
		}

		LOGGER.info("Exiting determineUnder()");
		return siteTransaction;
	}

	/**
	 * 
	 * @param spreadRecordEvent
	 * @param siteTeamPackage
	 * @param siteSpreads
	 * @param siteJuice
	 * @return
	 * @throws BatchException
	 */
	protected SiteTransaction spreadNegative(SpreadRecordEvent spreadRecordEvent, SiteTeamPackage siteTeamPackage, float[] siteSpreads, float[] siteJuice) throws BatchException {
		LOGGER.info("Entering spreadNegative()");
		LOGGER.debug("SpreadRecordEvent: " + spreadRecordEvent);
		LOGGER.debug("SiteTeamPackage: " + siteTeamPackage);
		LOGGER.debug("siteSpreads: " + siteSpreads);
		LOGGER.debug("siteJuice: " + siteJuice);

		// if we are negative, anything lower than the value is good
		SiteTransaction siteTransaction = determineNegativeSpread(spreadRecordEvent, siteTeamPackage, spreadOne, spreadJuiceOne, siteSpreads, siteJuice);
		if (siteTransaction != null) {
			if (spreadOne > 0) {
				accountEvent.setSpreadindicator("+");
			} else {
				accountEvent.setSpreadindicator("-");
			}
		} else {
			// Try the second spread if there is one
			if (spreadTwo != -9999) {
				if (spreadTwo < 0) {
					siteTransaction = determineNegativeSpread(spreadRecordEvent, siteTeamPackage, spreadTwo, spreadJuiceTwo, siteSpreads, siteJuice);
					if (siteTransaction == null) {
						throw new BatchException(BatchErrorCodes.SPREAD_NEGATIVE_TOO_LOW, BatchErrorMessage.SPREAD_NEGATIVE_TOO_LOW, "Spread two value is " + setupSpreadValue(spreadTwo) + " and Site Spreads are " + setupSiteSpreadValue(siteSpreads) + " Juice two value is " + setupJuiceValue(spreadJuiceTwo) + " Site Juices are " + setupSiteJuiceValue(siteJuice));
					}
				} else if (spreadTwo >= 0) {
					siteTransaction = determinePositiveSpread(spreadRecordEvent, siteTeamPackage, spreadTwo, spreadJuiceTwo, siteSpreads, siteJuice);
					if (siteTransaction == null) {
						throw new BatchException(BatchErrorCodes.SPREAD_POSITIVE_TOO_HIGH, BatchErrorMessage.SPREAD_POSITIVE_TOO_HIGH, "Spread two value is " + setupSpreadValue(spreadTwo) + " and Site Spreads are " + setupSiteSpreadValue(siteSpreads) + " Juice two value is " + setupJuiceValue(spreadJuiceTwo) + " Site Juices are " + setupSiteJuiceValue(siteJuice));
					}
				}

				// If spread is positive
				if (spreadTwo > 0) {
					accountEvent.setSpreadindicator("+");
				} else {
					accountEvent.setSpreadindicator("-");
				}
			} else {
				// Spread is less than site spread
				if (spreadOne > 0) {
					accountEvent.setSpreadindicator("+");
					accountEvent.setSpread(spreadOne);
				} else {
					accountEvent.setSpreadindicator("-");
					accountEvent.setSpread(spreadOne);
				}
				accountEvent.setSpreadjuice(spreadJuiceOne);
				throw new BatchException(BatchErrorCodes.SPREAD_NEGATIVE_TOO_LOW, BatchErrorMessage.SPREAD_NEGATIVE_TOO_LOW, "Spread value is " + setupSpreadValue(spreadOne) + " and Site Spreads are " + setupSiteSpreadValue(siteSpreads) + " Juice value is " + setupJuiceValue(spreadJuiceOne) + " Site Juices are " + setupSiteJuiceValue(siteJuice));
			}
		}

		LOGGER.info("Exiting spreadNegative()");
		return siteTransaction;
	}

	/**
	 * 
	 * @param spreadRecordEvent
	 * @param siteTeamPackage
	 * @param siteSpreads
	 * @param siteJuice
	 * @return
	 * @throws BatchException
	 */
	protected SiteTransaction spreadPositive(SpreadRecordEvent spreadRecordEvent, SiteTeamPackage siteTeamPackage, float[] siteSpreads, float[] siteJuice) throws BatchException {
		LOGGER.info("Entering spreadPositive()");
		LOGGER.debug("SpreadRecordEvent: " + spreadRecordEvent);
		LOGGER.debug("SiteTeamPackage: " + siteTeamPackage);

		// if we are negative, anything lower than the value is good
		SiteTransaction transaction = determinePositiveSpread(spreadRecordEvent, siteTeamPackage, spreadOne, spreadJuiceOne, siteSpreads, siteJuice);
		if (transaction != null) {
			if (spreadOne > 0) {
				accountEvent.setSpreadindicator("+");
			} else {
				accountEvent.setSpreadindicator("-");
			}
		} else {
			// Try the second spread if there is one
			if (spreadTwo != -9999) {
				if (spreadTwo <= 0) {
					transaction = determinePositiveSpread(spreadRecordEvent, siteTeamPackage, spreadTwo, spreadJuiceTwo, siteSpreads, siteJuice);
					if (transaction == null) {
						// Spread is greater than site spread
						if (spreadTwo > 0) {
							accountEvent.setSpreadindicator("+");
							accountEvent.setSpread(spreadTwo);
						} else {
							accountEvent.setSpreadindicator("-");
							accountEvent.setSpread(spreadTwo);
						}
						accountEvent.setSpreadjuice(spreadJuiceTwo);
						throw new BatchException(BatchErrorCodes.SPREAD_POSITIVE_TOO_HIGH, BatchErrorMessage.SPREAD_POSITIVE_TOO_HIGH, "Spread two value is " + setupSpreadValue(spreadTwo) + " and Site Spreads are " + setupSiteSpreadValue(siteSpreads) + " Juice two value is " + setupJuiceValue(spreadJuiceTwo) + " Site Juices are " + setupSiteJuiceValue(siteJuice));
					}
				} else if (spreadTwo > 0) {
					transaction = determineNegativeSpread(spreadRecordEvent, siteTeamPackage, spreadTwo, spreadJuiceTwo, siteSpreads, siteJuice);
					if (transaction == null) {
						// Spread is less than site spread
						if (spreadTwo > 0) {
							accountEvent.setSpreadindicator("+");
							accountEvent.setSpread(spreadTwo);
						} else {
							accountEvent.setSpreadindicator("-");
							accountEvent.setSpread(spreadTwo);
						}
						accountEvent.setSpreadjuice(spreadJuiceTwo);
						throw new BatchException(BatchErrorCodes.SPREAD_NEGATIVE_TOO_LOW, BatchErrorMessage.SPREAD_NEGATIVE_TOO_LOW, "Spread two value is " + setupSpreadValue(spreadTwo) + " and Site Spreads are " + setupSiteSpreadValue(siteSpreads) + " Juice two value is " + setupJuiceValue(spreadJuiceTwo) + " Site Juices are " + setupSiteJuiceValue(siteJuice));
					}
				}

				// If spread is positive
				if (spreadTwo > 0) {
					accountEvent.setSpreadindicator("+");
				} else {
					accountEvent.setSpreadindicator("-");
				}
			} else {
				// Spread is greater than site spread
				if (spreadOne > 0) {
					accountEvent.setSpreadindicator("+");
					accountEvent.setSpread(spreadOne);
				} else {
					accountEvent.setSpreadindicator("-");
					accountEvent.setSpread(spreadOne);
				}
				accountEvent.setSpreadjuice(spreadJuiceOne);
				throw new BatchException(BatchErrorCodes.SPREAD_POSITIVE_TOO_HIGH, BatchErrorMessage.SPREAD_POSITIVE_TOO_HIGH, "Spread value is " + setupSpreadValue(spreadOne) + " and Site Spreads are " + setupSiteSpreadValue(siteSpreads) + " Juice value is " + setupJuiceValue(spreadJuiceOne) + " Site Juices are " + setupSiteJuiceValue(siteJuice));
			}
		}

		LOGGER.info("Exiting spreadPositive()");
		return transaction;
	}

	/**
	 * 
	 * @param spreadRecordEvent
	 * @param siteTeamPackage
	 * @param spread
	 * @param juice
	 * @param siteSpreads
	 * @param siteJuice
	 * @return
	 * @throws BatchException
	 */
	protected SiteTransaction determineNegativeSpread(SpreadRecordEvent spreadRecordEvent, SiteTeamPackage siteTeamPackage, float spread, float juice, float[] siteSpreads, float[] siteJuice) throws BatchException {
		LOGGER.info("Entering determineNegativeSpread()");
		LOGGER.debug("SpreadRecordEvent: " + spreadRecordEvent);
		LOGGER.debug("SiteTeamPackage: " + siteTeamPackage);
		LOGGER.debug("spread: " + spread);
		LOGGER.debug("juice: " + juice);
		LOGGER.debug("siteSpreads: " + siteSpreads);
		LOGGER.debug("siteJuice: " + siteJuice);
		SiteTransaction siteTransaction = null;

		// Loop through site spreads
		for (int x = 0; (siteSpreads != null && x < siteSpreads.length); x++) {
			LOGGER.debug("spread: " + spread);
			LOGGER.debug("siteSpreads[x]: " + siteSpreads[x]);

			if (spread <= siteSpreads[x]) {
				// Now check the juice to make sure it is correct
				LOGGER.debug("juice: " + juice);
				LOGGER.debug("siteJuice[x]: " + siteJuice[x]);

				if (juice <= siteJuice[x]) {
					siteTransaction = processSite.createSpreadTransaction(siteTeamPackage, spreadRecordEvent, x, siteJuice[x], accountEvent);
					accountEvent.setSpread(siteSpreads[x]);
					accountEvent.setSpreadjuice(siteJuice[x]);
					
					if (siteJuice[x] < 0) {
						// To Win
						siteTransaction.setRiskorwin(1);
					} else {
						// Risk
						siteTransaction.setRiskorwin(2);
					}
					break;
				}
			}
		}

		LOGGER.info("Exiting determineNegativeSpread()");
		return siteTransaction;
	}

	/**
	 * 
	 * @param spreadRecordEvent
	 * @param siteTeamPackage
	 * @param spread
	 * @param juice
	 * @param siteSpreads
	 * @param siteJuice
	 * @return
	 * @throws BatchException
	 */
	protected SiteTransaction determinePositiveSpread(SpreadRecordEvent spreadRecordEvent, SiteTeamPackage siteTeamPackage, float spread, float juice, float[] siteSpreads, float[] siteJuice) throws BatchException {
		LOGGER.info("Entering determinePositiveSpread()");
		LOGGER.debug("SpreadRecordEvent: " + spreadRecordEvent);
		LOGGER.debug("SiteTeamPackage: " + siteTeamPackage);
		LOGGER.debug("spread: " + spread);
		LOGGER.debug("juice: " + juice);
		LOGGER.debug("siteSpreads: " + siteSpreads);
		LOGGER.debug("siteJuice: " + siteJuice);
		SiteTransaction siteTransaction = null;

		// Loop through site spreads
		for (int x = 0; (siteSpreads != null && x < siteSpreads.length); x++) {
			LOGGER.debug("spread: " + spread);
			LOGGER.debug("siteSpreads[x]: " + siteSpreads[x]);
			if (spread <= siteSpreads[x]) {
				// Now check the juice to make sure it is correct
				LOGGER.debug("juice: " + juice);
				LOGGER.debug("siteJuice[x]: " + siteJuice[x]);
				if (juice <= siteJuice[x]) {
					siteTransaction = processSite.createSpreadTransaction(siteTeamPackage, spreadRecordEvent, x, siteJuice[x], accountEvent);
					accountEvent.setSpread(siteSpreads[x]);
					accountEvent.setSpreadjuice(siteJuice[x]);

					if (siteJuice[x] < 0) {
						// To Win
						siteTransaction.setRiskorwin(1);
					} else {
						// Risk
						siteTransaction.setRiskorwin(2);
					}
					break;
				}
			}
		}

		LOGGER.info("Exiting determinePositiveSpread()");
		return siteTransaction;
	}

	/**
	 * 
	 * @param totalRecordEvent
	 * @param siteTeamPackage
	 * @param totalOverUnder
	 * @param totalOverUnderJuice
	 * @param siteTotals
	 * @param siteTotalJuices
	 * @return
	 * @throws BatchException
	 */
	protected SiteTransaction determineOver(TotalRecordEvent totalRecordEvent, SiteTeamPackage siteTeamPackage, float totalOverUnder, float totalOverUnderJuice, float[] siteTotals, float[] siteTotalJuices) throws BatchException {
		LOGGER.info("Entering determineOver()");
		LOGGER.debug("TotalRecordEvent: " + totalRecordEvent);
		LOGGER.debug("SiteTeamPackage: " + siteTeamPackage);
		LOGGER.debug("totalOverUnder: " + totalOverUnder);
		LOGGER.debug("totalOverUnderJuice: " + totalOverUnderJuice);
		LOGGER.debug("siteTotals: " + siteTotals);
		LOGGER.debug("siteTotalJuices: " + siteTotalJuices);
		SiteTransaction siteTransaction = null;

		// Loop through site totals
		for (int x = 0; x < siteTotals.length; x++) {
			LOGGER.debug("totalOverUnder: " + totalOverUnder);
			LOGGER.debug("siteTotals[x]: " + siteTotals[x]);
			if (totalOverUnder >= siteTotals[x]) {
				// Now check the juice to make sure it is correct
				LOGGER.debug("totalOverUnderJuice: " + totalOverUnderJuice);
				LOGGER.debug("siteTotalJuices[x]: " + siteTotalJuices[x]);
				if (totalOverUnderJuice <= siteTotalJuices[x]) {
					siteTransaction = processSite.createTotalTransaction(siteTeamPackage, totalRecordEvent, x, siteTotalJuices[x], accountEvent);
					accountEvent.setTotal(siteTotals[x]);
					accountEvent.setTotaljuice(siteTotalJuices[x]);

					if (siteTotalJuices[x] < 0) {
						// To Win
						siteTransaction.setRiskorwin(1);
					} else {
						// Risk
						siteTransaction.setRiskorwin(2);
					}
					break;
				}
			}
		}

		LOGGER.info("Exiting determineOver()");
		return siteTransaction;
	}

	/**
	 * 
	 * @param teamTotalRecordEvent
	 * @param siteTeamPackage
	 * @param totalOverUnder
	 * @param totalOverUnderJuice
	 * @param siteTotals
	 * @param siteTotalJuices
	 * @return
	 * @throws BatchException
	 * newly added method on 31-10-18
	 */
	protected SiteTransaction determineOver(TeamTotalRecordEvent teamTotalRecordEvent, SiteTeamPackage siteTeamPackage, float teamTotalOverUnder, float teamTotalOverUnderJuice, float[] siteTotals, float[] siteTotalJuices) throws BatchException {
		LOGGER.info("Entering determineOver()");
		LOGGER.debug("TeamTotalRecordEvent: " + teamTotalRecordEvent);
		LOGGER.debug("SiteTeamPackage: " + siteTeamPackage);
		LOGGER.debug("teamTotalOverUnder: " + teamTotalOverUnder);
		LOGGER.debug("teamTotalOverUnderJuice: " + teamTotalOverUnderJuice);
		LOGGER.debug("siteTotals: " + siteTotals);
		LOGGER.debug("siteTotalJuices: " + siteTotalJuices);
		SiteTransaction siteTransaction = null;

		// Loop through site totals
		for (int x = 0; x < siteTotals.length; x++) {
			LOGGER.debug("teamTotalOverUnder: " + teamTotalOverUnder);
			LOGGER.debug("siteTotals[x]: " + siteTotals[x]);
			if (teamTotalOverUnder >= siteTotals[x]) {
				// Now check the juice to make sure it is correct
				LOGGER.debug("totalOverUnderJuice: " + teamTotalOverUnderJuice);
				LOGGER.debug("siteTotalJuices[x]: " + siteTotalJuices[x]);
				if (teamTotalOverUnderJuice <= siteTotalJuices[x]) {
					siteTransaction = processSite.createTeamTotalTransaction(siteTeamPackage, teamTotalRecordEvent, x, siteTotalJuices[x], accountEvent);
					accountEvent.setTeamtotal(siteTotals[x]);
					accountEvent.setTeamtotaljuice(siteTotalJuices[x]);

					if (siteTotalJuices[x] < 0) {
						// To Win
						siteTransaction.setRiskorwin(1);
					} else {
						// Risk
						siteTransaction.setRiskorwin(2);
					}
					break;
				}
			}
		}

		LOGGER.info("Exiting determineOver()");
		return siteTransaction;
	}
	
	/**
	 * 
	 * @param totalRecordEvent
	 * @param siteTeamPackage
	 * @param totalOverUnder
	 * @param totalOverUnderJuice
	 * @param siteTotals
	 * @param siteTotalJuices
	 * @return
	 * @throws BatchException
	 */
	protected SiteTransaction determineUnder(TotalRecordEvent totalRecordEvent, SiteTeamPackage siteTeamPackage, float totalOverUnder, float totalOverUnderJuice, float[] siteTotals, float[] siteTotalJuices) throws BatchException {
		LOGGER.info("Entering determineUnder()");
		LOGGER.debug("TotalRecordEvent: " + totalRecordEvent);
		LOGGER.debug("SiteTeamPackage: " + siteTeamPackage);
		LOGGER.debug("totalOverUnder: " + totalOverUnder);
		LOGGER.debug("totalOverUnderJuice: " + totalOverUnderJuice);
		LOGGER.debug("siteTotals: " + siteTotals);
		LOGGER.debug("siteTotalJuices: " + siteTotalJuices);
		SiteTransaction siteTransaction = null;

		// Loop through site spreads
		for (int x = 0; x < siteTotals.length; x++) {
			LOGGER.debug("totalOverUnder: " + totalOverUnder);
			LOGGER.debug("siteTotals[x]: " + siteTotals[x]);
			if (totalOverUnder <= siteTotals[x]) {
				// Now check the juice to make sure it is correct
				LOGGER.debug("totalOverUnderJuice: " + totalOverUnderJuice);
				LOGGER.debug("siteTotalJuices[x]: " + siteTotalJuices[x]);
				if (totalOverUnderJuice <= siteTotalJuices[x]) {
					siteTransaction = processSite.createTotalTransaction(siteTeamPackage, totalRecordEvent, x, siteTotalJuices[x], accountEvent);
					accountEvent.setTotal(siteTotals[x]);
					accountEvent.setTotaljuice(siteTotalJuices[x]);
					
					if (siteTotalJuices[x] < 0) {
						// To Win
						siteTransaction.setRiskorwin(1);
					} else {
						// Risk
						siteTransaction.setRiskorwin(2);
					}
					break;
				}
			}
		}

		LOGGER.info("Exiting determineUnder()");
		return siteTransaction;
	}

	/**
	 * 
	 * @param mlRecordEvent
	 * @param siteTeamPackage
	 * @param moneyLine
	 * @param siteMlJuice
	 * @return
	 * @throws BatchException
	 */
	protected SiteTransaction moneyLineMoreThan(MlRecordEvent mlRecordEvent, SiteTeamPackage siteTeamPackage, float moneyLine, float[] siteMlJuice) throws BatchException {
		LOGGER.info("Entering moneyLineMoreThan()");
		LOGGER.debug("MlRecordEvent: " + mlRecordEvent);
		LOGGER.debug("SiteTeamPackage: " + siteTeamPackage);
		LOGGER.debug("moneyLine: " + moneyLine);
		LOGGER.debug("siteMlJuice: " + siteMlJuice);
		SiteTransaction siteTransaction = null;

		// Loop through site money lines
		for (int x = 0; (siteMlJuice != null && x < siteMlJuice.length); x++) {
			// Now check the juice to make sure it is correct
			LOGGER.debug("moneyLine: " + moneyLine);
			LOGGER.debug("siteMlJuice[x]: " + siteMlJuice[x]);
			if (moneyLine <= siteMlJuice[x]) {
				siteTransaction = processSite.createMoneyLineTransaction(siteTeamPackage, mlRecordEvent, x, siteMlJuice[x], accountEvent);
				accountEvent.setMljuice(siteMlJuice[x]);

				if (siteMlJuice[x] < 0) {
					// To Win
					siteTransaction.setRiskorwin(1);
				} else {
					// Risk
					siteTransaction.setRiskorwin(2);
				}
				break;
			}
		}

		LOGGER.debug("SiteTransaction: " + siteTransaction);
		LOGGER.info("Exiting moneyLineMoreThan()");
		return siteTransaction;
	}

	/**
	 * 
	 * @param spreadType
	 * @param spreadRecordEvent
	 * @throws BatchException
	 */
	private void populateSpreadData(int spreadType, SpreadRecordEvent spreadRecordEvent) throws BatchException {
		LOGGER.info("Entering populateSpreadData()");
		switch (spreadType) {
			// Team 1
			case 1:
				spreadOne = parseFloatData(spreadRecordEvent.getSpreadplusminusfirstone(), spreadRecordEvent.getSpreadinputfirstone());
				spreadJuiceOne = parseFloatData(spreadRecordEvent.getSpreadjuiceplusminusfirstone(), spreadRecordEvent.getSpreadinputjuicefirstone());
				break;
			// Team 1
			case 2:
				spreadOne = parseFloatData(spreadRecordEvent.getSpreadplusminusfirstone(), spreadRecordEvent.getSpreadinputfirstone());
				spreadTwo = parseFloatData(spreadRecordEvent.getSpreadplusminusfirsttwo(), spreadRecordEvent.getSpreadinputfirsttwo());
				spreadJuiceOne = parseFloatData(spreadRecordEvent.getSpreadjuiceplusminusfirstone(), spreadRecordEvent.getSpreadinputjuicefirstone());
				spreadJuiceTwo = parseFloatData(spreadRecordEvent.getSpreadjuiceplusminusfirsttwo(), spreadRecordEvent.getSpreadinputjuicefirsttwo());
				break;
			// Team 2
			case 3:
				spreadOne = parseFloatData(spreadRecordEvent.getSpreadplusminussecondone(), spreadRecordEvent.getSpreadinputsecondone());
				spreadJuiceOne = parseFloatData(spreadRecordEvent.getSpreadjuiceplusminussecondone(), spreadRecordEvent.getSpreadinputjuicesecondone());
				break;
			// Team 2
			case 4:
				spreadOne = parseFloatData(spreadRecordEvent.getSpreadplusminussecondone(), spreadRecordEvent.getSpreadinputsecondone());
				spreadTwo = parseFloatData(spreadRecordEvent.getSpreadplusminussecondtwo(), spreadRecordEvent.getSpreadinputsecondtwo());
				spreadJuiceOne = parseFloatData(spreadRecordEvent.getSpreadjuiceplusminussecondone(), spreadRecordEvent.getSpreadinputjuicesecondone());
				spreadJuiceTwo = parseFloatData(spreadRecordEvent.getSpreadjuiceplusminussecondtwo(), spreadRecordEvent.getSpreadinputjuicesecondtwo());
				break;
			default:
				throw new BatchException("Cannot determine spread numbers");
		}
		LOGGER.info("Exiting populateSpreadData()");
	}

	/**
	 * 
	 * @param spreadType
	 * @param spreadRecordEvent
	 * @param map
	 * @throws BatchException
	 */
	private void populateSpreadMapData(int spreadType, SpreadRecordEvent spreadRecordEvent, Map<String, String> map) throws BatchException {
		LOGGER.info("Entering populateSpreadMapData()");
		switch (spreadType) {
			// Team 1
			case 1:
				map.put("valindicator", spreadRecordEvent.getSpreadplusminusfirstone());
				map.put("val", spreadRecordEvent.getSpreadinputfirstone());
				map.put("juiceindicator", spreadRecordEvent.getSpreadjuiceplusminusfirstone());
				map.put("juice", spreadRecordEvent.getSpreadinputjuicefirstone());
				break;
			// Team 1
			case 2:
				map.put("valindicator", spreadRecordEvent.getSpreadplusminusfirstone());
				map.put("val", spreadRecordEvent.getSpreadinputfirstone());
				map.put("juiceindicator", spreadRecordEvent.getSpreadjuiceplusminusfirstone());
				map.put("juice", spreadRecordEvent.getSpreadinputjuicefirstone());
				break;
			// Team 2
			case 3:
				map.put("valindicator", spreadRecordEvent.getSpreadplusminussecondone());
				map.put("val", spreadRecordEvent.getSpreadinputsecondone());
				map.put("juiceindicator", spreadRecordEvent.getSpreadjuiceplusminussecondone());
				map.put("juice", spreadRecordEvent.getSpreadinputjuicesecondone());
				break;
			// Team 2
			case 4:
				map.put("valindicator", spreadRecordEvent.getSpreadplusminussecondone());
				map.put("val", spreadRecordEvent.getSpreadinputsecondone());
				map.put("juiceindicator", spreadRecordEvent.getSpreadjuiceplusminussecondone());
				map.put("juice", spreadRecordEvent.getSpreadinputjuicesecondone());
				break;
			default:
				throw new BatchException("Cannot determine spread numbers");
		}

		LOGGER.info("Exiting populateSpreadMapData()");
	}

	/**
	 * 
	 * @param totalType
	 * @param totalRecordEvent
	 * @throws BatchException
	 */
	private void populateTotalData(int totalType, TotalRecordEvent totalRecordEvent) throws BatchException {
		LOGGER.info("Entering populateTotalData()");

		switch (totalType) {
			// Over
			case 1:
				overUnderOne = Float.parseFloat(totalRecordEvent.getTotalinputfirstone());
				overUnderJuiceOne = parseFloatData(totalRecordEvent.getTotaljuiceplusminusfirstone(), totalRecordEvent.getTotalinputjuicefirstone());
				break;
			// Over
			case 2:
				overUnderOne = Float.parseFloat(totalRecordEvent.getTotalinputfirstone());
				overUnderTwo = Float.parseFloat(totalRecordEvent.getTotalinputfirsttwo());
				overUnderJuiceOne = parseFloatData(totalRecordEvent.getTotaljuiceplusminusfirstone(), totalRecordEvent.getTotalinputjuicefirstone());
				overUnderJuiceTwo = parseFloatData(totalRecordEvent.getTotaljuiceplusminusfirsttwo(), totalRecordEvent.getTotalinputjuicefirsttwo());
				break;
			// Under
			case 3:
				overUnderOne = Float.parseFloat(totalRecordEvent.getTotalinputsecondone());
				overUnderJuiceOne = parseFloatData(totalRecordEvent.getTotaljuiceplusminussecondone(), totalRecordEvent.getTotalinputjuicesecondone());
				break;
			// Under
			case 4:
				overUnderOne = Float.parseFloat(totalRecordEvent.getTotalinputsecondone());
				overUnderTwo = Float.parseFloat(totalRecordEvent.getTotalinputsecondtwo());
				overUnderJuiceOne = parseFloatData(totalRecordEvent.getTotaljuiceplusminussecondone(), totalRecordEvent.getTotalinputjuicesecondone());
				overUnderJuiceTwo = parseFloatData(totalRecordEvent.getTotaljuiceplusminussecondtwo(), totalRecordEvent.getTotalinputjuicesecondtwo());
				break;
			default:
				throw new BatchException("Cannot determine total numbers");
		}

		LOGGER.info("Exiting populateTotalData()");
	}

	/**
	 * 
	 * @param totalType
	 * @param totalRecordEvent
	 * @param map
	 * @throws BatchException
	 */
	private void populateTotalMapData(int totalType, TotalRecordEvent totalRecordEvent, Map<String, String> map) throws BatchException {
		LOGGER.info("Entering populateTotalMapData()");

		switch (totalType) {
			// Over
			case 1:
				map.put("valindicator", "o");
				map.put("val", totalRecordEvent.getTotalinputfirstone());
				map.put("juiceindicator", totalRecordEvent.getTotaljuiceplusminusfirstone());
				map.put("juice", totalRecordEvent.getTotalinputjuicefirstone());
				break;
			// Over
			case 2:
				map.put("valindicator", "o");
				map.put("val", totalRecordEvent.getTotalinputfirstone());
				map.put("juiceindicator", totalRecordEvent.getTotaljuiceplusminusfirstone());
				map.put("juice", totalRecordEvent.getTotalinputjuicefirstone());
				break;
			// Under
			case 3:
				map.put("valindicator", "u");
				map.put("val", totalRecordEvent.getTotalinputsecondone());
				map.put("juiceindicator", totalRecordEvent.getTotaljuiceplusminussecondone());
				map.put("juice", totalRecordEvent.getTotalinputjuicesecondone());
				break;
			// Under
			case 4:
				map.put("valindicator", "u");
				map.put("val", totalRecordEvent.getTotalinputsecondone());
				map.put("juiceindicator", totalRecordEvent.getTotaljuiceplusminussecondone());
				map.put("juice", totalRecordEvent.getTotalinputjuicesecondone());
				break;
			default:
				throw new BatchException("Cannot determine total numbers");
		}

		LOGGER.info("Exiting populateTotalMapData()");
	}

	/**
	 * 
	 * @param mlType
	 * @param mlRecordEvent
	 * @throws BatchException
	 */
	private void populateMoneyLineData(int mlType, MlRecordEvent mlRecordEvent) throws BatchException {
		LOGGER.info("Entering populateMoneyLineData()");

		switch (mlType) {
			// Team 1
			case 1:
				moneyLineJuiceOne = parseFloatData(mlRecordEvent.getMlplusminusfirstone(), mlRecordEvent.getMlinputfirstone());
				break;
			// Team 1
			case 2:
				moneyLineJuiceOne = parseFloatData(mlRecordEvent.getMlplusminusfirstone(), mlRecordEvent.getMlinputfirstone());
				moneyLineJuiceTwo = parseFloatData(mlRecordEvent.getMlplusminusfirsttwo(), mlRecordEvent.getMlinputfirsttwo());
				break;
			// Team 2
			case 3:
				moneyLineJuiceOne = parseFloatData(mlRecordEvent.getMlplusminussecondone(), mlRecordEvent.getMlinputsecondone());
				break;
			// Under
			case 4:
				moneyLineJuiceOne = parseFloatData(mlRecordEvent.getMlplusminussecondone(), mlRecordEvent.getMlinputsecondone());
				moneyLineJuiceTwo = parseFloatData(mlRecordEvent.getMlplusminussecondtwo(), mlRecordEvent.getMlinputsecondtwo());
				break;
			default:
				throw new BatchException("Cannot determine money line numbers");
		}

		LOGGER.info("Exiting populateMoneyLineData()");
	}

	/**
	 * 
	 * @param mlType
	 * @param mlRecordEvent
	 * @param map
	 * @throws BatchException
	 */
	private void populateMoneyLineMapData(int mlType, MlRecordEvent mlRecordEvent, Map<String, String> map) throws BatchException {
		LOGGER.info("Entering populateMoneyLineMapData()");

		switch (mlType) {
			// Team 1
			case 1:
				map.put("valindicator", mlRecordEvent.getMlplusminusfirstone());
				map.put("val", mlRecordEvent.getMlinputfirstone());
				map.put("juiceindicator", mlRecordEvent.getMlplusminusfirstone());
				map.put("juice", mlRecordEvent.getMlinputfirstone());
				break;
			// Team 1
			case 2:
				map.put("valindicator", mlRecordEvent.getMlplusminusfirstone());
				map.put("val", mlRecordEvent.getMlinputfirstone());
				map.put("juiceindicator", mlRecordEvent.getMlplusminusfirstone());
				map.put("juice", mlRecordEvent.getMlinputfirstone());
				break;
			// Team 2
			case 3:
				map.put("valindicator", mlRecordEvent.getMlplusminussecondone());
				map.put("val", mlRecordEvent.getMlinputsecondone());
				map.put("juiceindicator", mlRecordEvent.getMlplusminussecondone());
				map.put("juice", mlRecordEvent.getMlinputsecondone());
				break;
			// Under
			case 4:
				map.put("valindicator", mlRecordEvent.getMlplusminussecondone());
				map.put("val", mlRecordEvent.getMlinputsecondone());
				map.put("juiceindicator", mlRecordEvent.getMlplusminussecondone());
				map.put("juice", mlRecordEvent.getMlinputsecondone());
				break;
			default:
				throw new BatchException("Cannot determine money line numbers");
		}

		LOGGER.info("Exiting populateMoneyLineData()");
	}

	/**
	 * 
	 * @param wagerType
	 * @param eventPackage
	 * @return
	 * @throws BatchException
	 */
	public static SiteTeamPackage setupTeam(int wagerType, EventPackage eventPackage) throws BatchException {
		LOGGER.info("Entering setupTeam()");
		LOGGER.debug("wagerType: " + wagerType);
		LOGGER.debug("EventPackage: " + eventPackage);

		SiteTeamPackage siteTeamPackage = null;
		switch (wagerType) {
			// Team 1
			case 1:
			case 2:
				siteTeamPackage = ((SiteEventPackage)eventPackage).getSiteteamone();
				break;
			// Team 2
			case 3:
			case 4:
				siteTeamPackage = ((SiteEventPackage)eventPackage).getSiteteamtwo();
				break;
			default:
				throw new BatchException("Cannot determine wager teams");
		}
		LOGGER.info("Exiting setupTeam()");
		return siteTeamPackage;
	}

	/**
	 * 
	 * @param indicator
	 * @param value
	 * @return
	 */
	private float parseFloatData(String indicator, String value) {
		LOGGER.info("Entering parseFloatData()");
		LOGGER.debug("indicator: " + indicator);
		LOGGER.debug("value: " + value);

		float retValue = -9999;
		if (indicator != null && indicator.length() > 0 && value != null && value.length() > 0) {
			retValue = Float.parseFloat(indicator + value);
		}
		LOGGER.debug("Float value: " + retValue);
		LOGGER.info("Exiting parseFloatData()");
		return retValue;
	}

	/**
	 * 
	 * @param siteTeamPackage
	 * @return
	 */
	private float[] getSiteSpreads(SiteTeamPackage siteTeamPackage) {
		LOGGER.info("Entering getSiteSpreads()");
		LOGGER.debug("SiteTeamPackage: " + siteTeamPackage);
		final float siteSpreads[] = new float[siteTeamPackage.getGameSpreadOptionNumber().size()];
		for (int x = 0; x < siteTeamPackage.getGameSpreadOptionNumber().size(); x++) {
			final String gsoi = siteTeamPackage.getGameSpreadOptionIndicator().get(Integer.toString(x));
			final String gson = siteTeamPackage.getGameSpreadOptionNumber().get(Integer.toString(x));
			if (gsoi != null && gson != null) {
				float fvalue = parseFloatData(gsoi, gson);
				siteSpreads[x] = fvalue;
			}
		}
		LOGGER.info("Exiting getSiteSpreads()");
		return siteSpreads;
	}

	/**
	 * 
	 * @param siteTeamPackage
	 * @return
	 */
	private float[] getSiteTotals(SiteTeamPackage siteTeamPackage) {
		LOGGER.info("Entering getSiteTotals()");
		LOGGER.debug("SiteTeamPackage: " + siteTeamPackage);
		final float siteTotals[] = new float[siteTeamPackage.getGameTotalOptionNumber().size()];
		LOGGER.debug("siteTotals size: " + siteTotals.length);
		for (int x = 0; x < siteTeamPackage.getGameTotalOptionNumber().size(); x++) {
			final String gvalue = siteTeamPackage.getGameTotalOptionNumber().get(Integer.toString(x));
			if (gvalue != null) {
				float fvalue = Float.parseFloat(gvalue);
				LOGGER.debug("fvalue: " + fvalue);
				siteTotals[x] = fvalue;
			}
		}
		LOGGER.info("Exiting getSiteTotals()");
		return siteTotals;
	}

	/**
	 * 
	 * @param siteTeamPackage
	 * @return
	 */
	private float[] getSiteSpreadJuice(SiteTeamPackage siteTeamPackage) {
		LOGGER.info("Entering getSiteSpreadJuice()");
		LOGGER.debug("SiteTeamPackage: " + siteTeamPackage);
		final float[] siteJuice = new float[siteTeamPackage.getGameSpreadOptionJuiceNumber().size()];
		for (int x = 0; x < siteTeamPackage.getGameSpreadOptionJuiceNumber().size(); x++) {
			final String gsoji = siteTeamPackage.getGameSpreadOptionJuiceIndicator().get(Integer.toString(x));
			final String gsojn = siteTeamPackage.getGameSpreadOptionJuiceNumber().get(Integer.toString(x));
			if (gsoji != null && gsojn != null) {
				float fvalue = parseFloatData(gsoji, gsojn);
				siteJuice[x] = fvalue;
			}
		}
		LOGGER.info("Exiting getSiteSpreadJuice()");
		return siteJuice;
	}

	/**
	 * 
	 * @param siteTeamPackage
	 * @return
	 */
	private float[] getSiteTotalJuice(SiteTeamPackage siteTeamPackage) {
		LOGGER.info("Entering getSiteTotalJuice()");
		LOGGER.debug("SiteTeamPackage: " + siteTeamPackage);
		final float[] siteJuice = new float[siteTeamPackage.getGameTotalOptionJuiceNumber().size()];
		LOGGER.debug("siteTotalJuice size: " + siteTeamPackage.getGameTotalOptionJuiceNumber().size());
		for (int x = 0; x < siteTeamPackage.getGameTotalOptionJuiceNumber().size(); x++) {
			final String gtoji = siteTeamPackage.getGameTotalOptionJuiceIndicator().get(Integer.toString(x)); 
			final String gtojn = siteTeamPackage.getGameTotalOptionJuiceNumber().get(Integer.toString(x));
			if (gtoji != null && gtojn != null) {
				float fvalue = parseFloatData(gtoji, gtojn);
				LOGGER.debug("fvalue: " + fvalue);
				siteJuice[x] = fvalue;
			}
		}
		LOGGER.info("Exiting getSiteTotalJuice()");
		return siteJuice;
	}

	/**
	 * 
	 * @param siteTeamPackage
	 * @return
	 */
	private float[] getSiteMlJuice(SiteTeamPackage siteTeamPackage) {
		LOGGER.info("Entering getSiteMlJuice()");
		LOGGER.debug("SiteTeamPackage: " + siteTeamPackage);
		final float[] siteJuice = new float[siteTeamPackage.getGameMLOptionJuiceNumber().size()];
		for (int x = 0; x < siteTeamPackage.getGameMLOptionJuiceNumber().size(); x++) {
			final String gmji = siteTeamPackage.getGameMLOptionJuiceIndicator().get(Integer.toString(x));
			final String gmjn = siteTeamPackage.getGameMLOptionJuiceNumber().get(Integer.toString(x));
			if (gmji != null && gmjn != null) {
				float fvalue = parseFloatData(gmji, gmjn);
				siteJuice[x] = fvalue;
			}
		}
		LOGGER.info("Exiting getSiteMlJuice()");
		return siteJuice;
	}

	/**
	 * 
	 * @param spreadRecordEvent
	 * @return
	 */
	private static String getGameType(BaseRecordEvent baseRecordEvent) {
		String gType = "";
		if ("nfllines".equals(baseRecordEvent.getSport()) || "nflfirst".equals(baseRecordEvent.getSport()) ||
				"nflsecond".equals(baseRecordEvent.getSport())) {
			gType = "NFL";
		} else if ("ncaaflines".equals(baseRecordEvent.getSport()) || "ncaaffirst".equals(baseRecordEvent.getSport()) ||
				"ncaafsecond".equals(baseRecordEvent.getSport())) {
			gType = "NCAAF";
		} else if ("nbalines".equals(baseRecordEvent.getSport()) || "nbafirst".equals(baseRecordEvent.getSport()) ||
				"nbasecond".equals(baseRecordEvent.getSport())) {
			gType = "NBA";
		} else if ("ncaablines".equals(baseRecordEvent.getSport()) || "ncaabfirst".equals(baseRecordEvent.getSport()) ||
				"ncaabsecond".equals(baseRecordEvent.getSport())) {
			gType = "NCAAB";
		} else if ("nhllines".equals(baseRecordEvent.getSport()) || "nchlfirst".equals(baseRecordEvent.getSport()) ||
				"nhlsecond".equals(baseRecordEvent.getSport())) {
			gType = "NHL";
		} else if ("wnbalines".equals(baseRecordEvent.getSport()) || "wnbafirst".equals(baseRecordEvent.getSport()) ||
				"wnbasecond".equals(baseRecordEvent.getSport())) {
			gType = "WNBA";
		}
		return gType;
	}

	/**
	 * 
	 * @param spreadvalue
	 * @return
	 */
	private String setupSpreadValue(float spreadvalue) {
		LOGGER.info("Entering setupSpreadValue()");
		LOGGER.debug("spreadvalue: " + spreadvalue);

		String retValue = null;
		if (spreadvalue < 0) {
			retValue = Float.toString(spreadvalue);
		} else if (spreadvalue > 0) {
			retValue = "+" + Float.toString(spreadvalue);
		} else {
			retValue = "pk";
		}

		LOGGER.info("Exiting setupSpreadValue()");
		return retValue;
	}

	/**
	 * 
	 * @param sitespreads
	 * @return
	 */
	private String setupSiteSpreadValue(float[] sitespreads) {
		LOGGER.info("Entering setupSiteSpreadValue()");

		String retValue = "(";
		for (int x = 0; x < sitespreads.length; x++) {
			if (x == (sitespreads.length - 1)) {
				retValue += setupSpreadValue(sitespreads[x]);
			} else {
				retValue += setupSpreadValue(sitespreads[x]) + ", ";
			}
		}
		retValue += ")";
		
		LOGGER.info("Entering setupSiteSpreadValue()");
		return retValue;
	}

	/**
	 * 
	 * @param indicator
	 * @param totalvalue
	 * @return
	 */
	private String setupTotalValue(String indicator, float totalvalue) {
		LOGGER.info("Entering setupTotalValue()");
		LOGGER.debug("indicator: " + indicator);
		LOGGER.debug("totalvalue: " + totalvalue);

		final String retValue = indicator + Float.toString(totalvalue);

		LOGGER.info("Exiting setupTotalValue()");
		return retValue;
	}

	/**
	 * 
	 * @param indicator
	 * @param sitetotals
	 * @return
	 */
	private String setupSiteTotalValue(String indicator, float[] sitetotals) {
		LOGGER.info("Entering setupSiteTotalValue()");

		String retValue = "(";
		for (int x = 0; x < sitetotals.length; x++) {
			if (x == (sitetotals.length - 1)) {
				retValue += setupTotalValue(indicator, sitetotals[x]);
			} else {
				retValue += setupTotalValue(indicator, sitetotals[x]) + ", ";
			}
		}
		retValue += ")";

		LOGGER.info("Entering setupSiteTotalValue()");
		return retValue;
	}

	/**
	 * 
	 * @param juicevalue
	 * @return
	 */
	private String setupJuiceValue(float juicevalue) {
		LOGGER.info("Entering setupJuiceValue()");
		LOGGER.debug("juicevalue: " + juicevalue);

		String retValue = null;
		if (juicevalue < 0) {
			retValue = Float.toString(juicevalue);
		} else if (juicevalue >= 0) {
			retValue = "+" + Float.toString(juicevalue);
		}

		LOGGER.info("Exiting setupJuiceValue()");
		return retValue;
	}

	/**
	 * 
	 * @param sitespreadjuices
	 * @return
	 */
	private String setupSiteJuiceValue(float[] sitejuices) {
		LOGGER.info("Entering setupSiteJuiceValue()");

		String retValue = "(";
		for (int x = 0; x < sitejuices.length; x++) {
			if (x == (sitejuices.length - 1)) {
				retValue += setupJuiceValue(sitejuices[x]);
			} else {
				retValue += setupJuiceValue(sitejuices[x]) + ", ";
			}
		}
		retValue += ")";

		LOGGER.info("Exiting setupSiteJuiceValue()");
		return retValue;
	}

	/**
	 * 
	 * @param spreadRecordEvent
	 * @return
	 */
	private static String getBetType(BaseRecordEvent baseRecordEvent) {
		String bType = "";
		if (baseRecordEvent.getSport() != null) {
			if (baseRecordEvent.getSport().contains("lines")) {
				bType = "Game";
			} else if (baseRecordEvent.getSport().contains("first")) {
				bType = "1H";
			} else if (baseRecordEvent.getSport().contains("second")) {
				bType = "2H";
			}
		}

		return bType;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SiteWagers [spreadOne=" + spreadOne + ", spreadTwo=" + spreadTwo + ", spreadJuiceOne=" + spreadJuiceOne
				+ ", spreadJuiceTwo=" + spreadJuiceTwo + ", overUnderOne=" + overUnderOne + ", overUnderTwo="
				+ overUnderTwo + ", overUnderJuiceOne=" + overUnderJuiceOne + ", overUnderJuiceTwo=" + overUnderJuiceTwo
				+ ", moneyLineJuiceOne=" + moneyLineJuiceOne + ", moneyLineJuiceTwo=" + moneyLineJuiceTwo
				+ ", processSite=" + processSite + "]";
	}
}