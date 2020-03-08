/**
 * 
 */
package com.ticketadvantage.services.batch.processevent;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.ticketadvantage.services.dao.AccountDAO;
import com.ticketadvantage.services.dao.sites.SiteWagers;
import com.ticketadvantage.services.dao.sites.sportsinsights.SportsInsightsSite;
import com.ticketadvantage.services.db.ParlayRecordEventDB;
import com.ticketadvantage.services.db.PendingEventDB;
import com.ticketadvantage.services.db.RecordEventDB;
import com.ticketadvantage.services.db.SiteActiveDosDB;
import com.ticketadvantage.services.db.SiteEventsDosDB;
import com.ticketadvantage.services.email.ticketadvantage.TicketAdvantageGmailOath;
import com.ticketadvantage.services.errorhandling.AppErrorCodes;
import com.ticketadvantage.services.errorhandling.AppErrorMessage;
import com.ticketadvantage.services.errorhandling.AppException;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.AccountEvent;
import com.ticketadvantage.services.model.Accounts;
import com.ticketadvantage.services.model.BaseRecordEvent;
import com.ticketadvantage.services.model.BaseScrapper;
import com.ticketadvantage.services.model.EmailScrapper;
import com.ticketadvantage.services.model.EventPackage;
import com.ticketadvantage.services.model.GlobalScrapper;
import com.ticketadvantage.services.model.MlRecordEvent;
import com.ticketadvantage.services.model.ParlayAccountEvent;
import com.ticketadvantage.services.model.ParlayRecordEvent;
import com.ticketadvantage.services.model.PendingEvent;
import com.ticketadvantage.services.model.SpreadRecordEvent;
import com.ticketadvantage.services.model.TotalRecordEvent;
import com.ticketadvantage.services.model.WebScrapper;
import com.ticketadvantage.services.service.AWSBuyOrderEventResource;
import com.ticketadvantage.services.service.BestPriceBuyOrderResource;
import com.ticketadvantage.services.service.WoOMiddleRulesResource;
import com.ticketadvantage.services.telegram.TelegramBotSender;



/**
 * @author jmiller
 *
 */
public abstract class BaseSiteBatch {
	private static final Logger LOGGER = Logger.getLogger(BaseSiteBatch.class);
	protected static final SportsInsightsSite SportsInsightSite = new SportsInsightsSite("https://sportsinsights.actionnetwork.com", "mojaxsventures@gmail.com", "action1");
	protected final PendingEventDB PENDINGEVENTDB = new PendingEventDB();
	protected final RecordEventDB RECORDEVENTDB = new RecordEventDB();
	protected final ParlayRecordEventDB PARLAYRECORDEVENTDB = new ParlayRecordEventDB();
	protected final SiteEventsDosDB SITEEVENTSDB = new SiteEventsDosDB();
	protected final SiteActiveDosDB SITEACTIVEDB = new SiteActiveDosDB();
	protected ArrayList<ArrayList<PendingEvent>> listOLists = new ArrayList<ArrayList<PendingEvent>>();
	
	protected AccountDAO accountDAO;
	
	@Autowired
	public final void setAccountDAO(AccountDAO accountDAO) {
		this.accountDAO = accountDAO;
	}
	
	/**
	 * 
	 */
	public BaseSiteBatch() {
		super();
		LOGGER.info("Entering BaseSiteBatch()");
		LOGGER.info("Exiting BaseSiteBatch()");
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		final String timezone = "ET";
		Calendar start = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
		Calendar end = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
		Calendar now = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));

		if ("ET".equals(timezone)) {
			start = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
			end = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
			now = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
		} else if ("CT".equals(timezone)) {
			start = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
			end = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
			now = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
		} else if ("MT".equals(timezone)) {
			start = Calendar.getInstance(TimeZone.getTimeZone("America/Denver"));
			end = Calendar.getInstance(TimeZone.getTimeZone("America/Denver"));
			now = Calendar.getInstance(TimeZone.getTimeZone("America/Denver"));
		} else if ("PT".equals(timezone)) {
			start = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
			end = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
			now = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
		}

		start.set(Calendar.HOUR_OF_DAY, Integer.parseInt("01"));
		start.set(Calendar.MINUTE, Integer.parseInt("00"));
		start.set(Calendar.SECOND, 0);
		end.set(Calendar.HOUR_OF_DAY, Integer.parseInt("22"));
		end.set(Calendar.MINUTE, Integer.parseInt("00"));
		end.set(Calendar.SECOND, 0);

		LOGGER.error("now: " + now.getTime());
		LOGGER.error("start: " + start.getTime());
		LOGGER.error("end: " + end.getTime());

		// Check if game time is within time frame
		if (now.after(start) && now.before(end)) {
			LOGGER.error("It is after!");
		}
	}
	
	abstract protected GlobalScrapper getGlobalScrapper();
	
	abstract protected void setGlobalScrapper(GlobalScrapper globalScrapper);

	/**
	 * 
	 * @param event
	 * @param scrapper
	 */
	abstract protected void setupAccountEvents(BaseRecordEvent event, BaseScrapper scrapper);

	/**
	 * 
	 * @param event
	 * @param scrapper
	 * @param numunits
	 * @param islean
	 */
	abstract protected void setupBuyOrder(BaseRecordEvent event, BaseScrapper scrapper, Float numunits, Boolean islean);

	/**
	 * 
	 * @param event
	 * @param scrapper
	 */
	abstract protected void setupBestPriceBuyOrder(BaseRecordEvent event, BaseScrapper scrapper, Float numunits, Boolean islean);

	/**
	 * 
	 * @param scrapper
	 * @return
	 */
	abstract protected Set<Accounts> getScrapperSources(BaseScrapper scrapper);

	/**
	 * 
	 * @param found
	 * @param sitePendingEvent
	 * @param copySitePendingEvent
	 */
	abstract protected void persistPendingEvent(boolean found, PendingEvent sitePendingEvent, PendingEvent copySitePendingEvent);

	/**
	 * 
	 * @param pendingEvent
	 */
	protected void doProcessPendingEvent(PendingEvent pendingEvent) {
		// do nothing
	}

	/**
	 * 
	 * @param scrapper
	 * @return
	 */
	protected Accounts setupParlayAccount(BaseScrapper scrapper) {
		// do nothing
		return null;
	}

	/**
	 * 
	 * @param pendingType
	 * @param sitePendingEvents
	 * @param scrapper
	 */
	protected void checkSitePendingEvent(String pendingType, Set<PendingEvent> sitePendingEvents, BaseScrapper scrapper) {
		LOGGER.info("Entering checkSitePendingEvent()");

		// Now loop through the site pending events and
		// compare to users pending events
		if (sitePendingEvents != null) {
			for (PendingEvent sitePendingEvent : sitePendingEvents) {
				// Check for sites that need to do an extra call
				if (sitePendingEvent.getDoposturl()) {
					try {
						final PendingEvent pe = PENDINGEVENTDB.findPendingEventsByUserIdByTicketnumber(scrapper.getUserid(), sitePendingEvent.getTicketnum());
						if (pe != null) {
							sitePendingEvent.setGametype(pe.getGametype());
						} else {
							// Write this event to the DB for this user
							doProcessPendingEvent(sitePendingEvent);
						}
					} catch (Throwable t) {
						LOGGER.error(t.getMessage(), t);
					}
				}

				if ("Action Reverse".equals(sitePendingEvent.getTransactiontype()) || "Parlay".equals(sitePendingEvent.getTransactiontype())) {
					LOGGER.debug("Process scrapper: " + scrapper);
					PendingEvent copySitePendingEvent = new PendingEvent();
					copySitePendingEvent = copyPendingEvent(sitePendingEvent, copySitePendingEvent);
					// Set attributes on event
					copySitePendingEvent.setUserid(scrapper.getUserid());
					copySitePendingEvent.setDatecreated(new Date());
					copySitePendingEvent.setDatemodified(new Date());
					copySitePendingEvent.setPendingtype(pendingType);

					// Check for pending event update
					boolean found = checkForPendingUpdate(pendingType, scrapper, copySitePendingEvent);
					LOGGER.debug("found: " + found);

					// Persist pending event
					persistPendingEvent(found, sitePendingEvent, copySitePendingEvent);
					LOGGER.debug("copySitePendingEvent: " + copySitePendingEvent);

					// If we determine that this is new, create the transaction
					if (!found) {
						if (listOLists.size() > 0) {
							boolean didfind = false;
							for (int x = 0; x < this.listOLists.size(); x++) {
								final List<PendingEvent> parLayPendingList = this.listOLists.get(x);
								if (parLayPendingList.size() > 0) {
									for (int y = 0; y < parLayPendingList.size(); y++) {
										final PendingEvent pendingEvent = parLayPendingList.get(y);
										if (pendingEvent.getParlaynumber().longValue() == copySitePendingEvent.getParlaynumber().longValue()) {
											LOGGER.debug("addToList: " + pendingEvent.getParlaynumber());
											didfind = true;
											LOGGER.error("parLayPendingList " + copySitePendingEvent.getTransactiontype() + " " + copySitePendingEvent.getTicketnum() + " " + copySitePendingEvent.getTeam() + " " + copySitePendingEvent.getParlaynumber() + " " + copySitePendingEvent.getParlayseqnum());
											parLayPendingList.add(copySitePendingEvent);
											break;
										}
									}
								} else {
									LOGGER.error(copySitePendingEvent.getTransactiontype() + " " + copySitePendingEvent.getTicketnum() + " " + copySitePendingEvent.getTeam() + " " + copySitePendingEvent.getParlaynumber() + " " + copySitePendingEvent.getParlayseqnum());
									final ArrayList<PendingEvent> startList = new ArrayList<PendingEvent>();
									startList.add(copySitePendingEvent);
									listOLists.add(startList);									
								}
							}

							if (!didfind) {
								LOGGER.debug("adding to parlay list");
								LOGGER.error(copySitePendingEvent.getTransactiontype() + " " + copySitePendingEvent.getTicketnum() + " " + copySitePendingEvent.getTeam() + " " + copySitePendingEvent.getParlaynumber() + " " + copySitePendingEvent.getParlayseqnum());
								final ArrayList<PendingEvent> startList = new ArrayList<PendingEvent>();
								startList.add(copySitePendingEvent);
								listOLists.add(startList);								
							}
						} else {
							LOGGER.debug("adding to parlay list");
							LOGGER.error(copySitePendingEvent.getTransactiontype() + " " + copySitePendingEvent.getTicketnum() + " " + copySitePendingEvent.getTeam() + " " + copySitePendingEvent.getParlaynumber() + " " + copySitePendingEvent.getParlayseqnum());
							final ArrayList<PendingEvent> startList = new ArrayList<PendingEvent>();							
							startList.add(copySitePendingEvent);
							listOLists.add(startList);
						}
					}
				} else {
					if (isSportAvailable(sitePendingEvent.getGamesport(), sitePendingEvent.getGametype(), sitePendingEvent.getEventtype(), sitePendingEvent.getLinetype(), scrapper)) {
						LOGGER.debug("Process scrapper: " + scrapper);
						PendingEvent copySitePendingEvent = new PendingEvent();
						copySitePendingEvent = copyPendingEvent(sitePendingEvent, copySitePendingEvent);

						// Set attributes on event
						copySitePendingEvent.setUserid(scrapper.getUserid());
						copySitePendingEvent.setDatecreated(new Date());
						copySitePendingEvent.setDatemodified(new Date());
						copySitePendingEvent.setPendingtype(pendingType);
	
						// Check for pending event update
						boolean found = checkForPendingUpdate(pendingType, scrapper, copySitePendingEvent);
						LOGGER.debug("found: " + found);
	
						// Persist pending event
						persistPendingEvent(found, sitePendingEvent, copySitePendingEvent);
						LOGGER.debug("copySitePendingEvent: " + copySitePendingEvent);
	
						// If we determine that this is new, create the transaction
						if (!found) {
							LOGGER.debug("PendingEvent not found: " + copySitePendingEvent);
							checkSourceSiteAmount(copySitePendingEvent, scrapper);

							// Send text if we need to
							if (scrapper.getSendtextforaccount() != null && 
								scrapper.getSendtextforaccount().booleanValue() && 
								scrapper.getMobiletext() != null && 
								scrapper.getMobiletext().length() > 0) {
								try {
									String theText = copySitePendingEvent.getRotationid() + " " + 
											copySitePendingEvent.getEventtype() + " " +
											copySitePendingEvent.getTeam() + " " + 
											copySitePendingEvent.getLineplusminus() + copySitePendingEvent.getLine() + " " +
											copySitePendingEvent.getJuiceplusminus() + copySitePendingEvent.getJuice();
									TelegramBotSender.sendToTelegram(scrapper.getMobiletext(), theText);
								} catch (Throwable be) {
									LOGGER.error(be.getMessage(), be);
								}
							}
						}
					}
				}
			}

			if (listOLists.size() > 0) {
				for (int x = 0; x < this.listOLists.size(); x++) {
					final List<PendingEvent> parLayPendingList = this.listOLists.get(x);
					if (parLayPendingList.size() > 0) {
						// Check for parlays
						final Long parlayId = setupParlay(parLayPendingList, scrapper);
						LOGGER.error("parlayId: " + parlayId);
					}
				}
			} else {
				LOGGER.debug("listOLists is 0");
			}

			listOLists.clear();
		}

		LOGGER.info("Exiting checkSitePendingEvent()");
	}

	/**
	 * 
	 * @param pendingType
	 * @param scrapper
	 * @param copySitePendingEvent
	 * @return
	 */
	protected boolean checkForPendingUpdate(String pendingType, BaseScrapper scrapper, PendingEvent copySitePendingEvent) {
		LOGGER.info("Entering checkForPendingUpdate()");
		boolean found = false;

		// Get the users pending events from DB
		try {
			final List<PendingEvent> usersPendingEvents = PENDINGEVENTDB
					.findPendingEventsByUserIdByType(scrapper.getUserid(), pendingType);
			LOGGER.debug("usersPendingEvents: " + usersPendingEvents);

			if (usersPendingEvents != null && !usersPendingEvents.isEmpty()) {
				// Now loop through the users pending events
				for (int j = 0; j < usersPendingEvents.size(); j++) {
					final PendingEvent userPendingEvent = usersPendingEvents.get(j);
					LOGGER.debug("userPendingEvent: " + userPendingEvent);
					LOGGER.debug("copySitePendingEvent: " + copySitePendingEvent);

					if (copySitePendingEvent.getGametype() != null) {
						if (("Action Reverse".equals(copySitePendingEvent.getTransactiontype()) || "Parlay".equals(copySitePendingEvent.getTransactiontype())) &&
							("Action Reverse".equals(userPendingEvent.getTransactiontype()) || "Parlay".equals(userPendingEvent.getTransactiontype()))) {
							if (copySitePendingEvent.getParlayseqnum() != null && 
								copySitePendingEvent.getParlayseqnum().equals(userPendingEvent.getParlayseqnum()) &&
								copySitePendingEvent.getTicketnum() != null && 
								copySitePendingEvent.getTicketnum().equals(userPendingEvent.getTicketnum()) && 
								copySitePendingEvent.getUserid() != null && 
								copySitePendingEvent.getUserid().equals(userPendingEvent.getUserid()) && 
								copySitePendingEvent.getPendingtype() != null && 
								copySitePendingEvent.getPendingtype().equals(userPendingEvent.getPendingtype()) && 
								copySitePendingEvent.getEventtype() != null && 
								copySitePendingEvent.getEventtype().equals(userPendingEvent.getEventtype()) && 
								copySitePendingEvent.getGametype() != null && 
								copySitePendingEvent.getGametype().equals(userPendingEvent.getGametype()) && 
								copySitePendingEvent.getTeam() != null && 
								copySitePendingEvent.getTeam().equals(userPendingEvent.getTeam()) && 
								copySitePendingEvent.getLine() != null && 
								copySitePendingEvent.getLine().equals(userPendingEvent.getLine()) && 
								copySitePendingEvent.getLineplusminus() != null && 
								copySitePendingEvent.getLineplusminus().equals(userPendingEvent.getLineplusminus()) && 
								copySitePendingEvent.getJuice() != null && 
								copySitePendingEvent.getJuice().equals(userPendingEvent.getJuice()) && 
								copySitePendingEvent.getJuiceplusminus() != null && 
								copySitePendingEvent.getJuiceplusminus().equals(userPendingEvent.getJuiceplusminus()) && 
								copySitePendingEvent.getLinetype() != null && 
								copySitePendingEvent.getLinetype().equals(userPendingEvent.getLinetype())) {
								// This means we don't have an update
								found = true;
							}
						} else if (copySitePendingEvent.getTicketnum() != null && 
							copySitePendingEvent.getTicketnum().equals(userPendingEvent.getTicketnum()) && 
							copySitePendingEvent.getUserid() != null && 
							copySitePendingEvent.getUserid().equals(userPendingEvent.getUserid()) && 
							copySitePendingEvent.getPendingtype() != null && 
							copySitePendingEvent.getPendingtype().equals(userPendingEvent.getPendingtype()) && 
							copySitePendingEvent.getEventtype() != null && 
							copySitePendingEvent.getEventtype().equals(userPendingEvent.getEventtype()) && 
							copySitePendingEvent.getGametype() != null && 
							copySitePendingEvent.getGametype().equals(userPendingEvent.getGametype()) && 
							copySitePendingEvent.getTeam() != null && 
							copySitePendingEvent.getTeam().equals(userPendingEvent.getTeam()) && 
							copySitePendingEvent.getLinetype() != null && 
							copySitePendingEvent.getLinetype().equals(userPendingEvent.getLinetype())) {
							// This means we don't have an update
							found = true;
						}
					} else {
						if (("Action Reverse".equals(copySitePendingEvent.getTransactiontype()) || "Parlay".equals(copySitePendingEvent.getTransactiontype())) &&
							("Action Reverse".equals(userPendingEvent.getTransactiontype()) || "Parlay".equals(userPendingEvent.getTransactiontype()))) {
							if (copySitePendingEvent.getParlayseqnum() != null && 
								copySitePendingEvent.getParlayseqnum().equals(userPendingEvent.getParlayseqnum()) && 
								copySitePendingEvent.getTicketnum() != null && 
								copySitePendingEvent.getTicketnum().equals(userPendingEvent.getTicketnum()) && 
								copySitePendingEvent.getUserid() != null && 
								copySitePendingEvent.getUserid().equals(userPendingEvent.getUserid()) && 
								copySitePendingEvent.getPendingtype() != null && 
								copySitePendingEvent.getPendingtype().equals(userPendingEvent.getPendingtype()) && 
								copySitePendingEvent.getEventtype() != null && 
								copySitePendingEvent.getEventtype().equals(userPendingEvent.getEventtype()) && 
								copySitePendingEvent.getTeam() != null && 
								copySitePendingEvent.getTeam().equals(userPendingEvent.getTeam()) && 
								copySitePendingEvent.getLine() != null && 
								copySitePendingEvent.getLine().equals(userPendingEvent.getLine()) && 
								copySitePendingEvent.getLineplusminus() != null && 
								copySitePendingEvent.getLineplusminus().equals(userPendingEvent.getLineplusminus()) && 
								copySitePendingEvent.getJuice() != null && 
								copySitePendingEvent.getJuice().equals(userPendingEvent.getJuice()) && 
								copySitePendingEvent.getJuiceplusminus() != null && 
								copySitePendingEvent.getJuiceplusminus().equals(userPendingEvent.getJuiceplusminus()) && 
								copySitePendingEvent.getLinetype() != null && 
								copySitePendingEvent.getLinetype().equals(userPendingEvent.getLinetype())) {
								// This means we don't have an update
								found = true;
							}
						} else if (copySitePendingEvent.getTicketnum() != null && 
							copySitePendingEvent.getTicketnum().equals(userPendingEvent.getTicketnum()) && 
							copySitePendingEvent.getUserid() != null && 
							copySitePendingEvent.getUserid().equals(userPendingEvent.getUserid()) && 
							copySitePendingEvent.getPendingtype() != null && 
							copySitePendingEvent.getPendingtype().equals(userPendingEvent.getPendingtype()) && 
							copySitePendingEvent.getEventtype() != null && 
							copySitePendingEvent.getEventtype().equals(userPendingEvent.getEventtype()) && 
							copySitePendingEvent.getTeam() != null && 
							copySitePendingEvent.getTeam().equals(userPendingEvent.getTeam()) && 
							copySitePendingEvent.getLinetype() != null && 
							copySitePendingEvent.getLinetype().equals(userPendingEvent.getLinetype())) {
							// This means we don't have an update
							found = true;
						}
					}
				}
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		LOGGER.info("Exiting checkForPendingUpdate()");
		return found;
	}

	/**
	 * 
	 * @param parLayPendingList
	 * @param scrapper
	 * @return
	 */
	protected Long setupParlay(List<PendingEvent> parLayPendingList, BaseScrapper scrapper) {
		LOGGER.info("Entering setupParlay()");
		Long parlayId = null;

		try {
			if (parLayPendingList.size() > 0) {
				final ParlayRecordEvent recordEvent = new ParlayRecordEvent();
				String parlayType = null;

				// Which type is it?
				if ("Action Reverse".equals(parLayPendingList.get(0).getTransactiontype())) {
					recordEvent.setActiontype("Action Reverse");
					recordEvent.setParlaytype("Action Reverse");
					recordEvent.setDescription(parLayPendingList.size() + "-Team Action Reverse");
					parlayType = "Action Reverse";
				} else if ("Parlay".equals(parLayPendingList.get(0).getTransactiontype())) {
					recordEvent.setActiontype("Parlay");
					recordEvent.setParlaytype("Parlay");
					recordEvent.setDescription(parLayPendingList.size() + "-Team Parlay");
					parlayType = "Parlay";
				}

				recordEvent.setAttempts(new Integer(0));
				recordEvent.setCurrentattempts(new Integer(0));
				recordEvent.setDatecreated(new Date());
				recordEvent.setDatemodified(new Date());
				recordEvent.setIscompleted(false);
				recordEvent.setRiskamount(new Float(50.00));
				recordEvent.setScrappername(scrapper.getScrappername());
				recordEvent.setTextnumber(scrapper.getMobiletext());
				recordEvent.setTotalrisk(new Float(50.00));
				recordEvent.setTotalwin(new Float(0.00));
				recordEvent.setUserid(scrapper.getUserid());
				recordEvent.setWinamount(new Float(0.00));
				recordEvent.setWtype("1");
				parlayId = PARLAYRECORDEVENTDB.setParlayRecordEvent(recordEvent);

				// Loop through the list
				for (PendingEvent ppe : parLayPendingList) {
					final ParlayAccountEvent parlayAccountEvent = new ParlayAccountEvent();

					// Send a text?
					if (scrapper.getSendtextforaccount().booleanValue()) {
						parlayAccountEvent.setAccesstoken("Yes");
					}

					final Accounts destination = setupParlayAccount(scrapper);
					if (destination != null) {
						parlayAccountEvent.setAccountid(destination.getId());
						parlayAccountEvent.setAccountname(destination.getName());
						parlayAccountEvent.setTimezone(destination.getTimezone());
						parlayAccountEvent.setAmount(new Float(500.00));
						parlayAccountEvent.setAttempts(new Integer(0));
						parlayAccountEvent.setCurrentattempts(new Integer(0));
						parlayAccountEvent.setDatecreated(new Date());
						parlayAccountEvent.setDatemodified(new Date());
						parlayAccountEvent.setEventdatetime(ppe.getGamedate());
						parlayAccountEvent.setEventid(Integer.parseInt(ppe.getRotationid()));
						parlayAccountEvent.setEventname(ppe.getTeam());
						parlayAccountEvent.setGroupid(new Long(-9999));
						parlayAccountEvent.setIscompleted(false);
						parlayAccountEvent.setMaxmlamount(new Float(scrapper.getMlmaxamount()));
						parlayAccountEvent.setMaxspreadamount(new Float(scrapper.getSpreadmaxamount()));
						parlayAccountEvent.setMaxtotalamount(new Float(scrapper.getTotalmaxamount()));
						parlayAccountEvent.setOwnerpercentage(destination.getOwnerpercentage());
						parlayAccountEvent.setParlayid(parlayId);
						parlayAccountEvent.setParlaytype(parlayType);
						parlayAccountEvent.setPartnerpercentage(destination.getPartnerpercentage());
						parlayAccountEvent.setProxy(destination.getProxylocation());
						parlayAccountEvent.setRiskamount(new Float(500.00));
						parlayAccountEvent.setStatus("In Progress");
						parlayAccountEvent.setUserid(scrapper.getUserid());
						parlayAccountEvent.setTowinamount(new Float(500.00));
						parlayAccountEvent.setWagertype("1");
	
						if ("spread".equals(ppe.getEventtype())) {
							parlayAccountEvent.setType("spread");
							parlayAccountEvent.setSpreadindicator(ppe.getLineplusminus());
							parlayAccountEvent.setSpreaddata(ppe.getLine());
							if ("+".equals(ppe.getLineplusminus())) {
								parlayAccountEvent.setSpread(new Float(ppe.getLine()));
							} else {
								parlayAccountEvent.setSpread(new Float("-" + ppe.getLine()));	
							}
	
							// Juice
							parlayAccountEvent.setSpreadjuiceindicator(ppe.getJuiceplusminus());
							parlayAccountEvent.setSpreadjuicedata(ppe.getJuice());
							if ("+".equals(ppe.getJuiceplusminus())) {
								parlayAccountEvent.setSpreadjuice(new Float(ppe.getJuice()));
							} else {
								parlayAccountEvent.setSpreadjuice(new Float("-" + ppe.getJuice()));	
							}
						} else if ("total".equals(ppe.getEventtype())) {
							parlayAccountEvent.setType("total");
							parlayAccountEvent.setTotalindicator(ppe.getLineplusminus());
							parlayAccountEvent.setTotaldata(ppe.getLine());
							parlayAccountEvent.setTotal(new Float(ppe.getLine()));
	
							// Juice
							parlayAccountEvent.setTotaljuiceindicator(ppe.getJuiceplusminus());
							parlayAccountEvent.setTotaljuicedata(ppe.getJuice());
							if ("+".equals(ppe.getJuiceplusminus())) {
								parlayAccountEvent.setTotaljuice(new Float(ppe.getJuice()));
							} else {
								parlayAccountEvent.setTotaljuice(new Float("-" + ppe.getJuice()));	
							}
						} else if ("ml".equals(ppe.getEventtype())) {
							parlayAccountEvent.setType("ml");
							// Juice
							parlayAccountEvent.setTotaljuiceindicator(ppe.getJuiceplusminus());
							parlayAccountEvent.setTotaljuicedata(ppe.getJuice());
							if ("+".equals(ppe.getJuiceplusminus())) {
								parlayAccountEvent.setTotaljuice(new Float(ppe.getJuice()));
							} else {
								parlayAccountEvent.setTotaljuice(new Float("-" + ppe.getJuice()));	
							}							
						}

						// Setup sport and rotation ID
						setupParlaySportAndRotation(ppe.getGamesport(), 
								ppe.getGametype(), 
								ppe.getLinetype(), 
								ppe.getRotationid(), 
								parlayAccountEvent);

						// Write to the DB
						PARLAYRECORDEVENTDB.setupParlayAccountEvent(parlayAccountEvent);
					}
				}


				// Send a text for now
				if (parlayId != null && parlayId.longValue() > 0 && 
					scrapper.getMobiletext() != null && scrapper.getMobiletext().length() > 0) {
				}
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		LOGGER.info("Exiting setupParlay()");
		return parlayId;
	}

	/**
	 * 
	 * @param pendingType
	 * @param sitePendingEvents
	 * @param scrapper
	 */
	protected void deleteRemovedPendingEvents(String pendingType, Set<PendingEvent> sitePendingEvents, BaseScrapper scrapper) throws BatchException {
		// Get the pending events
		final List<PendingEvent> usersPendingEvents = PENDINGEVENTDB
				.findPendingEventsByUserIdByType(scrapper.getUserid(), pendingType);
		LOGGER.debug("usersPendingEvents: " + usersPendingEvents);

		if (usersPendingEvents != null) {
			// Now loop through the users pending events
			for (PendingEvent userPendingEvent : usersPendingEvents) {
				boolean found = false;
	
				if (sitePendingEvents != null) {
					found = findPendingEvent(pendingType,sitePendingEvents, userPendingEvent, scrapper);
				}
				LOGGER.debug("found: " + found);
	
				// Check if we need to delete the event
				if (!found) {
					try {
						Set<Accounts> accounts = getScrapperSources(scrapper);

						// Loop through the sources to see if it fits
						for (Accounts acct : accounts) {
							LOGGER.debug("userPendingEvent: " + userPendingEvent);
							LOGGER.debug("acct: " + acct);
							LOGGER.debug("userPendingEvent.getAccountname(): " + userPendingEvent.getAccountname() + " end");
							LOGGER.debug("acct.getUrl(): " + acct.getUrl() + " end");
							LOGGER.debug("userPendingEvent.getAccountid(): " + userPendingEvent.getAccountid() + " end"); 
							LOGGER.debug("acct.getUsername(): " + acct.getUsername() + " end"); 
							LOGGER.debug("userPendingEvent.getUserid(): " + userPendingEvent.getUserid() + " end");
							LOGGER.debug("scrapper.getUserid(): " + scrapper.getUserid() + " end");
	
							if (userPendingEvent != null && 
								(userPendingEvent.getAccountname() != null && acct.getUrl().contains(userPendingEvent.getAccountname())) &&
								(userPendingEvent.getAccountid() != null && userPendingEvent.getAccountid().equals(acct.getUsername())) && 
								(userPendingEvent.getUserid() != null && userPendingEvent.getUserid().equals(scrapper.getUserid()))) {
								// Delete this event for this user/acct
								LOGGER.debug("Deleting pending event!");
								PENDINGEVENTDB.delete(userPendingEvent.getId());
							}
						}
					} catch (Throwable t) {
						LOGGER.error(t);
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param pendingType
	 * @param sitePendingEvents
	 * @param userPendingEvent
	 * @param scrapper
	 * @return
	 */
	protected boolean findPendingEvent(String pendingType, Set<PendingEvent> sitePendingEvents, PendingEvent userPendingEvent, BaseScrapper scrapper) {
		LOGGER.info("Entering findPendingEvent()");		
		boolean found = false;

		// Now loop through the site pending events and compare to users pending events
		for (PendingEvent sitePendingDeleteEvent : sitePendingEvents) {
			PendingEvent copySitePendingDeleteEvent = new PendingEvent();
			copySitePendingDeleteEvent = this.copyPendingEvent(sitePendingDeleteEvent, copySitePendingDeleteEvent);
			copySitePendingDeleteEvent.setUserid(scrapper.getUserid());
			copySitePendingDeleteEvent.setPendingtype(pendingType);

			LOGGER.debug("copySitePendingDeleteEvent: " + copySitePendingDeleteEvent);
			LOGGER.debug("userPendingEvent: " + userPendingEvent);
			if (copySitePendingDeleteEvent.getGametype() != null) {
				if (copySitePendingDeleteEvent.getTicketnum() != null && copySitePendingDeleteEvent.getTicketnum().equals(userPendingEvent.getTicketnum()) &&
					copySitePendingDeleteEvent.getUserid() != null && copySitePendingDeleteEvent.getUserid().equals(userPendingEvent.getUserid()) &&
					copySitePendingDeleteEvent.getPendingtype() != null && copySitePendingDeleteEvent.getPendingtype().equals(userPendingEvent.getPendingtype()) &&
					copySitePendingDeleteEvent.getEventtype() != null && copySitePendingDeleteEvent.getEventtype().equals(userPendingEvent.getEventtype()) &&
					copySitePendingDeleteEvent.getGametype() != null && copySitePendingDeleteEvent.getGametype().equals(userPendingEvent.getGametype()) &&
					copySitePendingDeleteEvent.getTeam() != null && copySitePendingDeleteEvent.getTeam().equals(userPendingEvent.getTeam()) &&
					copySitePendingDeleteEvent.getLinetype() != null && copySitePendingDeleteEvent.getLinetype().equals(userPendingEvent.getLinetype())) {
					// This means we don't have an update
					found = true;
				}
			} else {
				if (copySitePendingDeleteEvent.getTicketnum() != null && copySitePendingDeleteEvent.getTicketnum().equals(userPendingEvent.getTicketnum()) &&
					copySitePendingDeleteEvent.getUserid() != null && copySitePendingDeleteEvent.getUserid().equals(userPendingEvent.getUserid()) &&
					copySitePendingDeleteEvent.getPendingtype() != null && copySitePendingDeleteEvent.getPendingtype().equals(userPendingEvent.getPendingtype()) &&
					copySitePendingDeleteEvent.getEventtype() != null && copySitePendingDeleteEvent.getEventtype().equals(userPendingEvent.getEventtype()) &&
					copySitePendingDeleteEvent.getTeam() != null && copySitePendingDeleteEvent.getTeam().equals(userPendingEvent.getTeam()) &&
					copySitePendingDeleteEvent.getLinetype() != null && copySitePendingDeleteEvent.getLinetype().equals(userPendingEvent.getLinetype())) {
					// This means we don't have an update
					found = true;
				}					
			}
		}

		LOGGER.info("Exiting findPendingEvent()");
		return found;
	}

	/**
	 * 
	 * @param scrapper
	 * @param pendingEvent
	 * @param eventPackage
	 */
	protected void placeSiteTransactions(BaseScrapper scrapper, PendingEvent pendingEvent, EventPackage eventPackage) {
		LOGGER.info("Entering placeTransactions()");
		final String type = pendingEvent.getEventtype();

		try {
			// If we are middling, then execute middle rules
			String playType = "Standard";
			if (scrapper.getMiddlerules().booleanValue()) {
				playType = "Middle";
			}

			if ("spread".equals(type)) {
				recordSpreadEvent(pendingEvent.getLinetype(), 
						pendingEvent.getGamesport(), 
						pendingEvent.getGametype(),
						pendingEvent.getRotationid(), 
						pendingEvent.getTeam(), 
						pendingEvent.getLineplusminus(),
						pendingEvent.getLine(), 
						pendingEvent.getJuiceplusminus(), 
						pendingEvent.getJuice(),
						pendingEvent.getEventdate(), 
						pendingEvent.getGamedate(), 
						pendingEvent.getCustomerid(), 
						pendingEvent.getNumunits(),
						pendingEvent.getIslean(),
						scrapper, 
						eventPackage);
			} else if ("total".equals(type)) {
				final Long totalId = recordTotalEvent(pendingEvent.getLinetype(), 
						pendingEvent.getGamesport(), 
						pendingEvent.getGametype(),
						pendingEvent.getRotationid(), 
						pendingEvent.getTeam(), 
						pendingEvent.getLineplusminus(),
						pendingEvent.getLine(), 
						pendingEvent.getJuiceplusminus(), 
						pendingEvent.getJuice(),
						pendingEvent.getEventdate(), 
						pendingEvent.getGamedate(), 
						pendingEvent.getCustomerid(), 
						pendingEvent.getNumunits(),
						pendingEvent.getIslean(),
						scrapper, 
						eventPackage, 
						playType);

				// If we are middling, then execute middle rules
				if (scrapper.getMiddlerules().booleanValue()) {
					Set<Accounts> middleDestinations = null;
					if (scrapper instanceof WebScrapper) {
						middleDestinations = ((WebScrapper) scrapper).getMiddledestinations();
					} else {
						middleDestinations = ((EmailScrapper) scrapper).getEmailmiddledestinations();
					}

					final EventPackage evp = SportsInsightSite.getEventById(pendingEvent.getRotationid(), "MLB");
					LOGGER.error("EventPackage: " + evp);

					// Kick off the middle
					new WoOMiddleRulesResource(
						totalId,
						"total",
						middleDestinations,
						RECORDEVENTDB,
						evp,
						scrapper.getMobiletext(),
						scrapper.getUserid(),
						scrapper.getTotalmaxamount());
				}
			} else if ("ml".equals(type)) {
				final Long mlId = recordMlEvent(pendingEvent.getLinetype(), 
						pendingEvent.getGamesport(), 
						pendingEvent.getGametype(),
						pendingEvent.getRotationid(), 
						pendingEvent.getTeam(), 
						pendingEvent.getLineplusminus(),
						pendingEvent.getLine(), 
						pendingEvent.getJuiceplusminus(), 
						pendingEvent.getJuice(),
						pendingEvent.getEventdate(), 
						pendingEvent.getGamedate(), 
						pendingEvent.getCustomerid(), 
						pendingEvent.getNumunits(),
						pendingEvent.getIslean(),
						scrapper, 
						eventPackage, 
						playType);

				// If we are middling, then execute middle rules
				if (scrapper.getMiddlerules().booleanValue()) {
					Set<Accounts> middleDestinations = null;
					if (scrapper instanceof WebScrapper) {
						middleDestinations = ((WebScrapper) scrapper).getMiddledestinations();
					} else {
						middleDestinations = ((EmailScrapper) scrapper).getEmailmiddledestinations();
					}

					final EventPackage evp = SportsInsightSite.getEventById(pendingEvent.getRotationid(), "MLB");
					LOGGER.error("EventPackage: " + evp);

					// Kick off the middle
					new WoOMiddleRulesResource(
						mlId,
						"ml",
						middleDestinations,
						RECORDEVENTDB,
						evp,
						scrapper.getMobiletext(),
						scrapper.getUserid(),
						scrapper.getMlmaxamount());
				}
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		LOGGER.info("Exiting placeTransactions()");
	} 

	/**
	 * 
	 * @param scrapper
	 * @param pendingEvent
	 * @param eventPackage
	 */
	protected void placeParlaySiteTransactions(BaseScrapper scrapper, Long parlayId, EventPackage eventPackage) {
		LOGGER.info("Entering placeTransactions()");

		try {
			// If we are middling, then execute middle rules
			final String playType = "Parlay";
			final ParlayRecordEvent parlayRecordEvent = PARLAYRECORDEVENTDB.getParlayRecordEvent(parlayId);
			final List<ParlayAccountEvent> accountEvents = PARLAYRECORDEVENTDB.getParlyAccountEventsByParlayId(parlayId);
			if (accountEvents != null && accountEvents.size() > 0) {
				final Map<String, String> sports = new HashMap<String, String>();
				for (ParlayAccountEvent pae : accountEvents) {
					// Figure out which game types we are dealing with
					sports.put(pae.getSport(), pae.getSport());
				}
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		LOGGER.info("Exiting placeTransactions()");
	}

	/**
	 * 
	 * @param baseRecordEvent
	 * @param eventName
	 * @param lineType
	 * @param gameSport
	 * @param gameType
	 * @param rotationId
	 * @param scrappername
	 * @param actiontype
	 * @param textnumber
	 * @param customerId
	 * @return
	 */
	protected String setupBaseEvent(BaseRecordEvent baseRecordEvent, 
			String eventName, 
			String lineType, 
			String gameSport, 
			String gameType, 
			String rotationId, 
			String scrappername, 
			String actiontype, 
			String textnumber, 
			String customerId) {
		LOGGER.debug("Entering setupBaseEvent()");
		baseRecordEvent.setEventname(eventName);
		baseRecordEvent.setWtype("2"); // Default To Win
		baseRecordEvent.setAttempts(0);
		baseRecordEvent.setDatentime(new Date());
		baseRecordEvent.setActiontype(actiontype);
		
		if (scrappername != null && scrappername.length() > 0 && scrappername.contains("PinnacleAgent")) {
			baseRecordEvent.setScrappername(scrappername + " " +  customerId);
		} else {
			baseRecordEvent.setScrappername(scrappername);			
		}
		baseRecordEvent.setTextnumber(textnumber);

		LOGGER.debug("lineType: " + lineType);
		LOGGER.debug("gameType: " + gameType);
		LOGGER.debug("rotationId: " + rotationId);

		if (gameType.contains("NFL")) {
			if ("game".equals(lineType)) {
				baseRecordEvent.setSport("nfllines");
			} else if ("first".equals(lineType)) {
				baseRecordEvent.setSport("nflfirst");
				if (rotationId.length() != 4 && !rotationId.startsWith("1")) {
					rotationId = "1" + rotationId;
				}
			} else if ("second".equals(lineType)) {
				baseRecordEvent.setSport("nflsecond");
				if (rotationId.length() != 4 && !rotationId.startsWith("2")) {
					rotationId = "2" + rotationId;
				}
			}
		} else if (gameType.contains("WNBA")) {
			if ("game".equals(lineType)) {
				baseRecordEvent.setSport("wnbalines");
			} else if ("first".equals(lineType)) {
				baseRecordEvent.setSport("wnbafirst");
				if (rotationId.length() != 4 && !rotationId.startsWith("1")) {
					rotationId = "1" + rotationId;
				}
			} else if ("second".equals(lineType)) {
				baseRecordEvent.setSport("wnbasecond");
				if (rotationId.length() != 4 && !rotationId.startsWith("2")) {
					rotationId = "2" + rotationId;
				}
			}
		} else if (gameType.contains("NBA")) {
			if ("game".equals(lineType)) {
				baseRecordEvent.setSport("nbalines");
			} else if ("first".equals(lineType)) {
				baseRecordEvent.setSport("nbafirst");
				if (rotationId.length() != 4 && !rotationId.startsWith("1")) {
					rotationId = "1" + rotationId;
				}
			} else if ("second".equals(lineType)) {
				baseRecordEvent.setSport("nbasecond");
				if (rotationId.length() != 4 && !rotationId.startsWith("2")) {
					rotationId = "2" + rotationId;
				}
			}
		} else if (gameType.contains("NCAAB") || (gameType + " " + gameSport).contains("NCAAB") ||
				   gameType.contains("NCAA Basketball") || (gameType + " " + gameSport).contains("NCAA Basketball") ||
				   gameType.contains("College Basketball") || (gameType + " " + gameSport).contains("College Basketball") ||
				   gameType.contains("NCAA Added Basketball") || (gameType + " " + gameSport).contains("NCAA Added Basketball")) {
			if ("game".equals(lineType)) {
				baseRecordEvent.setSport("ncaablines");
			} else if ("first".equals(lineType)) {
				baseRecordEvent.setSport("ncaabfirst");
				if (rotationId.length() != 4 && !rotationId.startsWith("1")) {
					rotationId = "1" + rotationId;
				}
			} else if ("second".equals(lineType)) {
				baseRecordEvent.setSport("ncaabsecond");
				if (rotationId.length() != 4 && !rotationId.startsWith("2")) {
					rotationId = "2" + rotationId;
				}
			}
		} else if (gameType.contains("NCAAF") || (gameType + " " + gameSport).contains("NCAAF") || 
				   gameType.contains("NCAA Football") || (gameType + " " + gameSport).contains("NCAA Football") || 
				   gameType.contains("College Football") || (gameType + " " + gameSport).contains("College Football") || 
				   gameType.contains("NCAA Added Football") || (gameType + " " + gameSport).contains("NCAA Added Football")) {
			if ("game".equals(lineType)) {
				baseRecordEvent.setSport("ncaaflines");
			} else if ("first".equals(lineType)) {
				baseRecordEvent.setSport("ncaaffirst");
				if (rotationId.length() != 4 && !rotationId.startsWith("1")) {
					rotationId = "1" + rotationId;
				}
			} else if ("second".equals(lineType)) {
				baseRecordEvent.setSport("ncaafsecond");
				if (rotationId.length() != 4 && !rotationId.startsWith("2")) {
					rotationId = "2" + rotationId;
				}
			}
		} else if (gameType.contains("NHL")) {
			if ("game".equals(lineType)) {
				baseRecordEvent.setSport("nhllines");
			} else if ("first".equals(lineType)) {
				baseRecordEvent.setSport("nhlfirst");
				if (rotationId.length() != 4 && !rotationId.startsWith("1")) {
					rotationId = "1" + rotationId;
				}
			} else if ("second".equals(lineType)) {
				baseRecordEvent.setSport("nhlsecond");
				if (rotationId.length() != 4 && !rotationId.startsWith("2")) {
					rotationId = "2" + rotationId;
				}
			} else if ("third".equals(lineType)) {
				baseRecordEvent.setSport("nhlthird");
				if (rotationId.length() != 4 && !rotationId.startsWith("3")) {
					rotationId = "3" + rotationId;
				}
			}
		} else if (gameType.contains("MLB")) {
			if ("game".equals(lineType)) {
				baseRecordEvent.setSport("mlblines");
			} else if ("first".equals(lineType)) {
				baseRecordEvent.setSport("mlbfirst");
				if (rotationId.length() != 4 && !rotationId.startsWith("1")) {
					rotationId = "1" + rotationId;
				}
			} else if ("second".equals(lineType)) {
				baseRecordEvent.setSport("mlbsecond");
				if (rotationId.length() != 4 && !rotationId.startsWith("2")) {
					rotationId = "2" + rotationId;
				}
			} else if ("third".equals(lineType)) {
				baseRecordEvent.setSport("mlbthird");
				if (rotationId.length() != 4 && !rotationId.startsWith("3")) {
					rotationId = "3" + rotationId;
				}
			}
		} else if (gameType.contains("International Baseball")) {
			if ("game".equals(lineType)) {
				baseRecordEvent.setSport("internationalbaseballlines");
			} else if ("first".equals(lineType)) {
				baseRecordEvent.setSport("internationalbaseballfirst");
				if (rotationId.length() != 6 && !rotationId.startsWith("1")) {
					rotationId = "1" + rotationId;
				}
			} else if ("second".equals(lineType)) {
				baseRecordEvent.setSport("internationalbaseballsecond");
				if (rotationId.length() != 6 && !rotationId.startsWith("2")) {
					rotationId = "2" + rotationId;
				}
			} else if ("third".equals(lineType)) {
				baseRecordEvent.setSport("internationalbaseballthrid");
				if (rotationId.length() != 6 && !rotationId.startsWith("3")) {
					rotationId = "3" + rotationId;
				}
			}
		}

		LOGGER.debug("Exiting setupBaseEvent()");
		return rotationId;
	}

	/**
	 * 
	 * @param lineType
	 * @param gameSport
	 * @param gameType
	 * @param rotation
	 * @param team
	 * @param lineplusminus
	 * @param linedata
	 * @param juiceplusminus
	 * @param juicedata
	 * @param eventdate
	 * @param gamedate
	 * @param customerId
	 * @param numunits
	 * @param islean
	 * @param scrapper
	 * @param ep
	 * @return
	 */
	protected Long recordSpreadEvent(String lineType, 
			String gameSport, 
			String gameType, 
			String rotation, 
			String team,
			String lineplusminus, 
			String linedata, 
			String juiceplusminus, 
			String juicedata, 
			String eventdate, 
			Date gamedate,
			String customerId, 
			Float numunits,
			Boolean islean,
			BaseScrapper scrapper, 
			EventPackage ep) {
		LOGGER.debug("Entering recordSpreadEvent()");
		LOGGER.debug("lineType: " + lineType);
		LOGGER.debug("gameSport: " + gameSport);
		LOGGER.debug("gameType: " + gameType);
		LOGGER.debug("rotation: " + rotation);
		LOGGER.debug("team: " + team);
		LOGGER.debug("lineplusminus: " + lineplusminus);
		LOGGER.debug("linedata: " + linedata);
		LOGGER.debug("juiceplusminus: " + juiceplusminus);
		LOGGER.debug("juicedata: " + juicedata);
		LOGGER.debug("numunits: " + numunits);
		LOGGER.debug("islean: " + islean);
		Long id = null;

		try {
			final SpreadRecordEvent spreadRecordEvent = new SpreadRecordEvent();
			String rotationId = setupBaseEvent(spreadRecordEvent, team, lineType, gameSport, gameType, rotation, scrapper.getScrappername(), "Standard", scrapper.getMobiletext(), customerId);
			spreadRecordEvent.setEventdatetime(gamedate);
			spreadRecordEvent.setEventtype("spread");
			spreadRecordEvent.setUserid(scrapper.getUserid());

			String amount = scrapper.getSpreadmaxamount();
			if (scrapper.getLeanssenabled() != null &&
				scrapper.getLeanssenabled().booleanValue() &&
				islean != null &&
				islean.booleanValue()) {
				amount = Integer.toString(scrapper.getSpreadlean().intValue());
			} else if (scrapper.getUnitsenabled() != null &&
				scrapper.getUnitsenabled().booleanValue() &&
				numunits != null) {
				final Float units = scrapper.getSpreadunit().floatValue() * numunits;
				amount = Integer.toString(units.intValue());
			}
			spreadRecordEvent.setAmount(amount);

			if (ep != null) {
				Integer eoneId = ep.getTeamone().getId();
				Integer etwoId = ep.getTeamtwo().getId();
				Integer rotId = Integer.parseInt(rotationId);
				LOGGER.debug("eoneId: " + eoneId);
				LOGGER.debug("etwoId: " + etwoId);
				LOGGER.debug("rotId: " + rotId);

				if (eoneId.intValue() == rotId.intValue() || etwoId.intValue() == rotId.intValue()) {
					spreadRecordEvent.setRotationid(rotId);
					spreadRecordEvent.setEventid(eoneId);
					spreadRecordEvent.setEventid1(eoneId);
					spreadRecordEvent.setEventid2(etwoId);
					spreadRecordEvent.setEventteam1(ep.getTeamone().getTeam());
					spreadRecordEvent.setEventteam2(ep.getTeamtwo().getTeam());
					spreadRecordEvent.setEventdatetime(ep.getEventdatetime());
					
					// Setup the spread
					setupSpread(lineplusminus, 
							linedata, 
							juiceplusminus, 
							juicedata, 
							rotId, 
							eoneId, 
							etwoId, 
							scrapper, 
							spreadRecordEvent);
				}
			} else {
				Integer rotId = Integer.parseInt(rotationId);
				Integer eoneId = new Integer(0);
				Integer etwoId = new Integer(0);
				String teamOne = "";
				String teamTwo = "";

				if ((rotId.intValue() & 1) == 0) {
					// even...
					eoneId = rotId.intValue() - 1;
					etwoId = rotId.intValue();
					teamOne = teamTwo = team;
				} else {
					// odd...
					eoneId = rotId.intValue();
					etwoId = rotId.intValue() + 1;
					teamOne = teamTwo = team;
				}

				spreadRecordEvent.setRotationid(rotId);
				spreadRecordEvent.setEventid(eoneId);
				spreadRecordEvent.setEventid1(eoneId);
				spreadRecordEvent.setEventid2(etwoId);
				spreadRecordEvent.setEventteam1(teamOne);
				spreadRecordEvent.setEventteam2(teamTwo);

				// Setup the spread
				setupSpread(lineplusminus, 
						linedata, 
						juiceplusminus, 
						juicedata, 
						rotId, 
						eoneId, 
						etwoId, 
						scrapper, 
						spreadRecordEvent);
			}

			// Setup the datetime for now
			spreadRecordEvent.setAttempttime(new Date());
			String event_name = SiteWagers.setupSpreadEventName(spreadRecordEvent);
			spreadRecordEvent.setEventname(event_name);

			LOGGER.error("scrapper.getCheckdupgame(): " + scrapper.getCheckdupgame().booleanValue());
			LOGGER.error("scrapper.getPlayotherside(): " + scrapper.getPlayotherside().booleanValue());

			// Set the spread event
			if (scrapper.getCheckdupgame().booleanValue() && !RECORDEVENTDB.checkDuplicateSpreadEvent(spreadRecordEvent)) {
				LOGGER.debug("SpreadRecordEvent: " + spreadRecordEvent);
				id = RECORDEVENTDB.setSpreadEvent(spreadRecordEvent);
				LOGGER.debug("ID: " + id);
				spreadRecordEvent.setId(id);

				// Are we doing "buy" orders?
				LOGGER.debug("scrapper.getFullfill(): " + scrapper.getFullfill());
				LOGGER.debug("scrapper.getBestprice(): " + scrapper.getBestprice());
				if (scrapper.getFullfill() && scrapper.getBestprice()) {
					setupBestPriceBuyOrder(spreadRecordEvent, scrapper, numunits, islean);
				} else if (scrapper.getFullfill()) {
					setupBuyOrder(spreadRecordEvent, scrapper, numunits, islean);
				} else {
					setupAccountEvents(spreadRecordEvent, scrapper);
					
					// Check if these need to be run right away
					if (spreadRecordEvent.getAttempts().intValue() == 0) {
						setupImmediateEvents(spreadRecordEvent);
					}
				}
			} else if (scrapper.getPlayotherside().booleanValue() && !RECORDEVENTDB.checkDuplicateReverseSpreadEvent(spreadRecordEvent)) {
				LOGGER.debug("SpreadRecordEvent: " + spreadRecordEvent);
				id = RECORDEVENTDB.setSpreadEvent(spreadRecordEvent);
				LOGGER.debug("ID: " + id);
				spreadRecordEvent.setId(id);

				// Are we doing "buy" orders?
				LOGGER.debug("scrapper.getFullfill(): " + scrapper.getFullfill());
				if (scrapper.getFullfill() && scrapper.getBestprice()) {
					setupBestPriceBuyOrder(spreadRecordEvent, scrapper, numunits, islean);
				} else if (scrapper.getFullfill()) {
					setupBuyOrder(spreadRecordEvent, scrapper, numunits, islean);
				} else {
					setupAccountEvents(spreadRecordEvent, scrapper);

					// Check if these need to be run right away
					if (spreadRecordEvent.getAttempts().intValue() == 0) {
						setupImmediateEvents(spreadRecordEvent);
					}
				}
			} else if ((scrapper.getCheckdupgame() != null && !scrapper.getCheckdupgame().booleanValue()) && (scrapper.getPlayotherside() != null && !scrapper.getPlayotherside().booleanValue())) {
				LOGGER.debug("SpreadRecordEvent: " + spreadRecordEvent);
				id = RECORDEVENTDB.setSpreadEvent(spreadRecordEvent);
				LOGGER.debug("ID: " + id);
				spreadRecordEvent.setId(id);

				// Are we doing "buy" orders?
				LOGGER.debug("scrapper.getFullfill(): " + scrapper.getFullfill());
				if (scrapper.getFullfill() && scrapper.getBestprice()) {
					setupBestPriceBuyOrder(spreadRecordEvent, scrapper, numunits, islean);
				} else if (scrapper.getFullfill()) {
					setupBuyOrder(spreadRecordEvent, scrapper, numunits, islean);
				} else {
					setupAccountEvents(spreadRecordEvent, scrapper);

					// Check if these need to be run right away
					if (spreadRecordEvent.getAttempts().intValue() == 0) {
						setupImmediateEvents(spreadRecordEvent);
					}
				}
			} else {
				LOGGER.error("SpreadRecordEvent: " + spreadRecordEvent);
				LOGGER.warn("Duplicate event being processed");
			}
		} catch (AppException ae) {
			LOGGER.error("AppException in recordSpreadEvent()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in recordSpreadEvent()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}

		LOGGER.debug("Exiting recordSpreadEvent()");
		return id;
	}

	/**
	 * 
	 * @param lineType
	 * @param gameSport
	 * @param gameType
	 * @param rotation
	 * @param team
	 * @param lineplusminus
	 * @param linedata
	 * @param juiceplusminus
	 * @param juicedata
	 * @param eventDate
	 * @param gamedate
	 * @param customerId
	 * @param numunits
	 * @param islean
	 * @param scrapper
	 * @param ep
	 * @param actionType
	 * @return
	 */
	protected Long recordTotalEvent(String lineType, 
			String gameSport,
			String gameType, 
			String rotation, 
			String team, 
			String lineplusminus, 
			String linedata,
			String juiceplusminus,
			String juicedata,
			String eventDate,
			Date gamedate,
			String customerId,
			Float numunits,
			Boolean islean,
			BaseScrapper scrapper,
			EventPackage ep,
			String actionType) {
		LOGGER.info("Entering recordTotalEvent()");
		LOGGER.debug("lineType: " + lineType);
		LOGGER.debug("gameType: " + gameType);
		LOGGER.debug("team: " + team);
		LOGGER.debug("lineplusminus: " + lineplusminus);
		LOGGER.debug("linedata: " + linedata);
		LOGGER.debug("juiceplusminus: " + lineplusminus);
		LOGGER.debug("juicedata: " + linedata);
		LOGGER.debug("eventDate: " + eventDate);
		Long id = null;

		try {
			final TotalRecordEvent totalRecordEvent = new TotalRecordEvent();
			String rotationId = setupBaseEvent(totalRecordEvent, team, lineType, gameSport, gameType, rotation, scrapper.getScrappername(), actionType, scrapper.getMobiletext(), customerId);
			totalRecordEvent.setEventdatetime(gamedate);
			totalRecordEvent.setEventtype("total");
			totalRecordEvent.setUserid(scrapper.getUserid());

			String amount = scrapper.getTotalmaxamount();
			if (scrapper.getLeanssenabled() != null &&
				scrapper.getLeanssenabled().booleanValue() &&
				islean != null &&
				islean.booleanValue()) {
				amount = Integer.toString(scrapper.getTotallean().intValue());
			} else if (scrapper.getUnitsenabled() != null &&
					scrapper.getUnitsenabled().booleanValue() &&
					numunits != null) {
				final Float units = scrapper.getTotalunit().floatValue() * numunits;
				amount = Integer.toString(units.intValue());
			}
			totalRecordEvent.setAmount(amount);

			if (ep != null) {
				Integer eoneId = ep.getTeamone().getId();
				Integer etwoId = ep.getTeamtwo().getId();
				Integer rotId = Integer.parseInt(rotationId);
				LOGGER.debug("eoneId: " + eoneId);
				LOGGER.debug("etwoId: " + etwoId);
				LOGGER.debug("rotId: " + rotId);
	
				if (eoneId.intValue() == rotId.intValue() || etwoId.intValue() == rotId.intValue()) {
					totalRecordEvent.setRotationid(rotId);
					totalRecordEvent.setEventid(eoneId);
					totalRecordEvent.setEventid1(eoneId);
					totalRecordEvent.setEventid2(etwoId);
					totalRecordEvent.setEventteam1(ep.getTeamone().getTeam());
					totalRecordEvent.setEventteam2(ep.getTeamtwo().getTeam());
					totalRecordEvent.setEventdatetime(ep.getEventdatetime());

					// Setup the total record
					setupTotal(lineplusminus, linedata, juiceplusminus, juicedata, scrapper, totalRecordEvent);
				}
			} else {
				Integer rotId = Integer.parseInt(rotationId);
				int eoneId = 0;
				int etwoId = 0;
				String teamOne = "";
				String teamTwo = "";
				if ((rotId.intValue() & 1) == 0) {
					// even...
					eoneId = rotId.intValue() - 1;
					etwoId = rotId.intValue();
					teamOne = teamTwo = team;
				} else {
					// odd...
					eoneId = rotId.intValue();
					etwoId = rotId.intValue() + 1;
					teamOne = teamTwo = team;
				}
	
				totalRecordEvent.setRotationid(rotId);
				totalRecordEvent.setEventid(eoneId);
				totalRecordEvent.setEventid1(eoneId);
				totalRecordEvent.setEventid2(etwoId);
				totalRecordEvent.setEventteam1(teamOne);
				totalRecordEvent.setEventteam2(teamTwo);

				// Setup the total record
				setupTotal(lineplusminus, linedata, juiceplusminus, juicedata, scrapper, totalRecordEvent);
			}

			// Setup the datetime for now
			totalRecordEvent.setAttempttime(new Date());
			String event_name = SiteWagers.setupTotalEventName(totalRecordEvent);
			totalRecordEvent.setEventname(event_name);

			// Set the total event
			if (scrapper.getCheckdupgame().booleanValue() && !RECORDEVENTDB.checkDuplicateTotalEvent(totalRecordEvent)) {
				LOGGER.debug("TotalRecordEvent: " + totalRecordEvent);
				id = RECORDEVENTDB.setTotalEvent(totalRecordEvent);
				LOGGER.debug("ID: " + id);
				totalRecordEvent.setId(id);

				// Are we doing "buy" orders?
				LOGGER.debug("scrapper.getFullfill(): " + scrapper.getFullfill());
				if (scrapper.getFullfill() && scrapper.getBestprice()) {
					setupBestPriceBuyOrder(totalRecordEvent, scrapper, numunits, islean);
				} else if (scrapper.getFullfill()) {
					setupBuyOrder(totalRecordEvent, scrapper, numunits, islean);
				} else {
					setupAccountEvents(totalRecordEvent, scrapper);
	
					// Check if these need to be run right away
					if (totalRecordEvent.getAttempts().intValue() == 0) {
						setupImmediateEvents(totalRecordEvent);
					}
				}
			} else if (scrapper.getPlayotherside().booleanValue() && !RECORDEVENTDB.checkDuplicateReverseTotalEvent(totalRecordEvent)) {
				LOGGER.debug("TotalRecordEvent: " + totalRecordEvent);
				id = RECORDEVENTDB.setTotalEvent(totalRecordEvent);
				LOGGER.debug("ID: " + id);
				totalRecordEvent.setId(id);

				// Are we doing "buy" orders?
				LOGGER.debug("scrapper.getFullfill(): " + scrapper.getFullfill());
				if (scrapper.getFullfill() && scrapper.getBestprice()) {
					setupBestPriceBuyOrder(totalRecordEvent, scrapper, numunits, islean);
				} else if (scrapper.getFullfill()) {
					setupBuyOrder(totalRecordEvent, scrapper, numunits, islean);
				} else {
					setupAccountEvents(totalRecordEvent, scrapper);
	
					// Check if these need to be run right away
					if (totalRecordEvent.getAttempts().intValue() == 0) {
						setupImmediateEvents(totalRecordEvent);
					}
				}
			} else if ((scrapper.getCheckdupgame() != null && !scrapper.getCheckdupgame().booleanValue()) && (scrapper.getPlayotherside() != null && !scrapper.getPlayotherside().booleanValue())) {
				LOGGER.debug("TotalRecordEvent: " + totalRecordEvent);
				id = RECORDEVENTDB.setTotalEvent(totalRecordEvent);
				LOGGER.debug("ID: " + id);
				totalRecordEvent.setId(id);

				// Are we doing "buy" orders?
				LOGGER.debug("scrapper.getFullfill(): " + scrapper.getFullfill());
				if (scrapper.getFullfill() && scrapper.getBestprice()) {
					setupBestPriceBuyOrder(totalRecordEvent, scrapper, numunits, islean);
				} else if (scrapper.getFullfill()) {
					setupBuyOrder(totalRecordEvent, scrapper, numunits, islean);
				} else {
					setupAccountEvents(totalRecordEvent, scrapper);
	
					// Check if these need to be run right away
					if (totalRecordEvent.getAttempts().intValue() == 0) {
						setupImmediateEvents(totalRecordEvent);
					}
				}
			} else {
				LOGGER.warn("Duplicate event being processed");
			}
		} catch (AppException ae) {
			LOGGER.error(ae.getMessage(), ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}

		LOGGER.info("Exiting recordTotalEvent()");
		return id;
	}

	/**
	 * 
	 * @param lineType
	 * @param gameSport
	 * @param gameType
	 * @param rotation
	 * @param team
	 * @param lineplusminus
	 * @param linedata
	 * @param juiceplusminus
	 * @param juicedata
	 * @param eventDate
	 * @param gamedate
	 * @param customerId
	 * @param numunits
	 * @param islean
	 * @param scrapper
	 * @param ep
	 * @param actionType
	 * @return
	 */
	protected Long recordMlEvent(String lineType,
			String gameSport,
			String gameType, 
			String rotation, 
			String team, 
			String lineplusminus, 
			String linedata,
			String juiceplusminus,
			String juicedata, 
			String eventDate,
			Date gamedate,
			String customerId,
			Float numunits,
			Boolean islean,
			BaseScrapper scrapper,
			EventPackage ep,
			String actionType) {
		LOGGER.info("Entering recordMlEvent()");
		LOGGER.debug("lineType: " + lineType);
		LOGGER.debug("gameType: " + gameType);
		LOGGER.debug("team: " + team);
		LOGGER.debug("lineplusminus: " + lineplusminus);
		LOGGER.debug("linedata: " + linedata);
		LOGGER.debug("juiceplusminus: " + lineplusminus);
		LOGGER.debug("juicedata: " + linedata);
		Long id = null;

		try {
			final MlRecordEvent mlRecordEvent = new MlRecordEvent();
			String rotationId = setupBaseEvent(mlRecordEvent, team, lineType, gameSport, gameType, rotation, scrapper.getScrappername(), actionType, scrapper.getMobiletext(), customerId);
			mlRecordEvent.setEventdatetime(gamedate);
			mlRecordEvent.setEventtype("ml");
			mlRecordEvent.setUserid(scrapper.getUserid());

			String amount = scrapper.getMlmaxamount();
			if (scrapper.getLeanssenabled() != null &&
					scrapper.getLeanssenabled().booleanValue() &&
					islean != null &&
					islean.booleanValue()) {
				amount = Integer.toString(scrapper.getMllean().intValue());
			} else if (scrapper.getUnitsenabled() != null &&
					scrapper.getUnitsenabled().booleanValue() &&
					numunits != null) {
				final Float units = scrapper.getMlunit().floatValue() * numunits;
				amount = Integer.toString(units.intValue());
			}
			mlRecordEvent.setAmount(amount);

			if (ep != null) {
				// Get all events for event type
				Integer eoneId = ep.getTeamone().getId();
				Integer etwoId = ep.getTeamtwo().getId();
				Integer rotId = Integer.parseInt(rotationId);
				LOGGER.debug("eoneId: " + eoneId);
				LOGGER.debug("etwoId: " + etwoId);
				LOGGER.debug("rotId: " + rotId);
	
				if (eoneId.intValue() == rotId.intValue() || etwoId.intValue() == rotId.intValue()) {
					mlRecordEvent.setRotationid(rotId);
					mlRecordEvent.setEventid(eoneId);
					mlRecordEvent.setEventid1(eoneId);
					mlRecordEvent.setEventid2(etwoId);
					mlRecordEvent.setEventteam1(ep.getTeamone().getTeam());
					mlRecordEvent.setEventteam2(ep.getTeamtwo().getTeam());
					mlRecordEvent.setEventdatetime(ep.getEventdatetime());

					// Setup money line
					setupMoneyLine(juiceplusminus, juicedata, rotId, eoneId, etwoId, scrapper, mlRecordEvent);
				}
			} else {
				Integer rotId = Integer.parseInt(rotationId);
				int eoneId = 0;
				int etwoId = 0;
				String teamOne = "";
				String teamTwo = "";
				if ((rotId.intValue() & 1) == 0) {
					// even...
					eoneId = rotId.intValue() - 1;
					etwoId = rotId.intValue();
					teamTwo = team;
				} else {
					// odd...
					eoneId = rotId.intValue();
					etwoId = rotId.intValue() + 1;
					teamOne = team;
				}
				LOGGER.debug("eoneId: " + eoneId);
				LOGGER.debug("etwoId: " + etwoId);
				LOGGER.debug("rotId: " + rotId);

				mlRecordEvent.setRotationid(rotId);
				mlRecordEvent.setEventid(eoneId);
				mlRecordEvent.setEventid1(eoneId);
				mlRecordEvent.setEventid2(etwoId);
				mlRecordEvent.setEventteam1(teamOne);
				mlRecordEvent.setEventteam2(teamTwo);

				// Setup money line
				setupMoneyLine(juiceplusminus, juicedata, rotId, eoneId, etwoId, scrapper, mlRecordEvent);
			}

			// Setup the datetime for now
			mlRecordEvent.setAttempttime(new Date());
			String event_name = SiteWagers.setupMlEventName(mlRecordEvent);
			mlRecordEvent.setEventname(event_name);

			// Set the ml event
			if (scrapper.getCheckdupgame().booleanValue() && !RECORDEVENTDB.checkDuplicateMlEvent(mlRecordEvent)) {
				LOGGER.debug("MlRecordEvent: " + mlRecordEvent);
				id = RECORDEVENTDB.setMlEvent(mlRecordEvent);
				LOGGER.debug("ID: " + id);
				mlRecordEvent.setId(id);

				// Are we doing "buy" orders?
				LOGGER.debug("scrapper.getFullfill(): " + scrapper.getFullfill());
				if (scrapper.getFullfill() && scrapper.getBestprice()) {
					setupBestPriceBuyOrder(mlRecordEvent, scrapper, numunits, islean);
				} else if (scrapper.getFullfill()) {
					setupBuyOrder(mlRecordEvent, scrapper, numunits, islean);
				} else {
					setupAccountEvents(mlRecordEvent, scrapper);

					// Check if these need to be run right away
					if (mlRecordEvent.getAttempts().intValue() == 0) {
						setupImmediateEvents(mlRecordEvent);
					}
				}
			} else if (scrapper.getPlayotherside().booleanValue() && !RECORDEVENTDB.checkDuplicateReverseMlEvent(mlRecordEvent)) {
				LOGGER.debug("MlRecordEvent: " + mlRecordEvent);
				id = RECORDEVENTDB.setMlEvent(mlRecordEvent);
				LOGGER.debug("ID: " + id);
				mlRecordEvent.setId(id);

				// Are we doing "buy" orders?
				LOGGER.debug("scrapper.getFullfill(): " + scrapper.getFullfill());
				if (scrapper.getFullfill() && scrapper.getBestprice()) {
					setupBestPriceBuyOrder(mlRecordEvent, scrapper, numunits, islean);
				} else if (scrapper.getFullfill()) {
					setupBuyOrder(mlRecordEvent, scrapper, numunits, islean);
				} else {
					setupAccountEvents(mlRecordEvent, scrapper);

					// Check if these need to be run right away
					if (mlRecordEvent.getAttempts().intValue() == 0) {
						setupImmediateEvents(mlRecordEvent);
					}
				}
			} else if ((scrapper.getCheckdupgame() != null && !scrapper.getCheckdupgame().booleanValue()) && (scrapper.getPlayotherside() != null && !scrapper.getPlayotherside().booleanValue())) {
				LOGGER.debug("MlRecordEvent: " + mlRecordEvent);
				id = RECORDEVENTDB.setMlEvent(mlRecordEvent);
				LOGGER.debug("ID: " + id);
				mlRecordEvent.setId(id);

				// Are we doing "buy" orders?
				LOGGER.debug("scrapper.getFullfill(): " + scrapper.getFullfill());
				if (scrapper.getFullfill() && scrapper.getBestprice()) {
					setupBestPriceBuyOrder(mlRecordEvent, scrapper, numunits, islean);
				} else if (scrapper.getFullfill()) {
					setupBuyOrder(mlRecordEvent, scrapper, numunits, islean);
				} else {
					setupAccountEvents(mlRecordEvent, scrapper);

					// Check if these need to be run right away
					if (mlRecordEvent.getAttempts().intValue() == 0) {
						setupImmediateEvents(mlRecordEvent);
					}
				}
			} else {
				LOGGER.warn("Duplicate event being processed");
			}
		} catch (AppException ae) {
			LOGGER.warn("AppException in recordMlEvent()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in recordMlEvent()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}

		LOGGER.info("Exiting recordMlEvent()");
		return id;
	}

	/**
	 * 
	 * @param event
	 * @param accounts
	 * @param enableRetry
	 * @param sendtextforaccount
	 * @param humanspeed
	 */
	protected void setupAccounts(BaseRecordEvent event, Set<Accounts> accounts, Boolean enableRetry, Boolean sendtextforaccount, Boolean humanspeed) {
		LOGGER.info("Entering setupAccounts()");
		LOGGER.debug("BaseRecordEvent: " + event);
		LOGGER.debug("Accounts: " + accounts);
		LOGGER.debug("enableRetry: " + enableRetry);

		// Check to make sure the account list is not empty
		if (accounts != null && !accounts.isEmpty()) {
			final Iterator<Accounts> itr = accounts.iterator();

			while (itr.hasNext()) {
				final Accounts account = itr.next();
				if (account != null) {
					final String hourafter = account.getHourafter();
					final String minuteafter = account.getMinuteafter();
					final String hourbefore = account.getHourbefore();
					final String minutebefore = account.getMinutebefore();

					if (hourafter.equals("00") && hourbefore.equals("00")) {
						// Populate events
						final AccountEvent accountEvent = populateAccountEvent(event, account, enableRetry, humanspeed);

						try {
							if (sendtextforaccount.booleanValue()) {
								accountEvent.setAccesstoken(getAccessToken());
							}

							// Setup the account event
							RECORDEVENTDB.setupAccountEvent(accountEvent);
						} catch (Exception e) {
							LOGGER.error(e);
						}
					} else if (!hourbefore.equals("00") && !hourafter.equals("00")) {
						final String timezone = account.getTimezone();
						Calendar start = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
						Calendar end = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
						Calendar now = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));

						if ("ET".equals(timezone)) {
							start = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
							end = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
							now = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
						} else if ("CT".equals(timezone)) {
							start = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
							end = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
							now = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
						} else if ("MT".equals(timezone)) {
							start = Calendar.getInstance(TimeZone.getTimeZone("America/Denver"));
							end = Calendar.getInstance(TimeZone.getTimeZone("America/Denver"));
							now = Calendar.getInstance(TimeZone.getTimeZone("America/Denver"));
						} else if ("PT".equals(timezone)) {
							start = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
							end = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
							now = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
						}

						start.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourafter));
						start.set(Calendar.MINUTE, Integer.parseInt(minuteafter));
						start.set(Calendar.SECOND, 0);
						end.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourbefore));
						end.set(Calendar.MINUTE, Integer.parseInt(minutebefore));
						end.set(Calendar.SECOND, 0);

						LOGGER.error("now: " + now.getTime());
						LOGGER.error("start: " + start.getTime());
						LOGGER.error("end: " + end.getTime());

						// Check if game time is within time frame
						if (now.after(start) && now.before(end)) {
							// Populate events
							final AccountEvent accountEvent = populateAccountEvent(event, account, enableRetry, humanspeed);

							try {
								if (sendtextforaccount.booleanValue()) {
									accountEvent.setAccesstoken(getAccessToken());
								}

								// Setup the account event
								RECORDEVENTDB.setupAccountEvent(accountEvent);
							} catch (Exception e) {
								LOGGER.error(e);
							}
						}
					} else if (!hourbefore.equals("00")) {
						final String timezone = account.getTimezone();
						Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
						Calendar game = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));

						if ("ET".equals(timezone)) {
							cal = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
							game = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
						} else if ("CT".equals(timezone)) {
							cal = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
							game = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));					
						} else if ("MT".equals(timezone)) {
							cal = Calendar.getInstance(TimeZone.getTimeZone("America/Denver"));
							game = Calendar.getInstance(TimeZone.getTimeZone("America/Denver"));					
						} else if ("PT".equals(timezone)) {
							cal = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
							game = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
						}

						final Date gameDate = event.getEventdatetime();
						game.setTime(gameDate);
						game.add(Calendar.HOUR, Integer.parseInt("-" + hourbefore));
						game.add(Calendar.MINUTE, Integer.parseInt("-" + minutebefore));

						// Check if game time is within time frame
						if (cal.after(game)) {
							// Populate events
							final AccountEvent accountEvent = populateAccountEvent(event, account, enableRetry, humanspeed);

							try {
								if (sendtextforaccount.booleanValue()) {
									accountEvent.setAccesstoken(getAccessToken());
								}

								// Setup the account event
								RECORDEVENTDB.setupAccountEvent(accountEvent);
							} catch (Exception e) {
								LOGGER.error(e);
							}
						}
					} else if (!hourafter.equals("00")) {
						final String timezone = account.getTimezone();
						Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
						Calendar now = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));

						if ("ET".equals(timezone)) {
							cal = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
							now = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
						} else if ("CT".equals(timezone)) {
							cal = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
							now = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
						} else if ("MT".equals(timezone)) {
							cal = Calendar.getInstance(TimeZone.getTimeZone("America/Denver"));
							now = Calendar.getInstance(TimeZone.getTimeZone("America/Denver"));
						} else if ("PT".equals(timezone)) {
							cal = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
							now = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
						}

						cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourafter));
						cal.set(Calendar.MINUTE, Integer.parseInt(minuteafter));
						cal.set(Calendar.SECOND, 0);

						LOGGER.error("now: " + now.getTime());
						LOGGER.error("cal: " + cal.getTime());

						// Check if game time is within time frame
						if (now.after(cal)) {
							// Populate events
							final AccountEvent accountEvent = populateAccountEvent(event, account, enableRetry, humanspeed);

							try {
								if (sendtextforaccount.booleanValue()) {
									accountEvent.setAccesstoken(getAccessToken());
								}

								// Setup the account event
								RECORDEVENTDB.setupAccountEvent(accountEvent);
							} catch (Exception e) {
								LOGGER.error(e);
							}
						}						
					}
				}
			}
		}

		LOGGER.info("Exiting setupAccounts()");
	}

	/**
	 * 
	 * @param event
	 * @param accounts
	 * @param scrapper
	 * @param numunits
	 * @param islean
	 */
	protected void setupBuyOrderAccounts(BaseRecordEvent event, 
			Set<Accounts> accounts, 
			BaseScrapper scrapper,
			Float numunits, 
			Boolean islean) {
		LOGGER.info("Entering setupBuyOrderAccounts()");
		LOGGER.debug("BaseRecordEvent: " + event);
		LOGGER.debug("Accounts: " + accounts);
		LOGGER.debug("BaseScrapper: " + scrapper);

		try {
			final Set<Accounts> accountlist = new HashSet<Accounts>();
			boolean leansenabled = false;
			boolean unitsenabled = false;
			Integer orderAmount = scrapper.getOrderamount();
			if (scrapper.getLeanssenabled() != null &&
				scrapper.getLeanssenabled().booleanValue() &&
				islean != null &&
				islean.booleanValue()) {
				leansenabled = true;
			} else if (scrapper.getUnitsenabled() != null &&
				scrapper.getUnitsenabled().booleanValue() &&
				numunits != null) {
				unitsenabled = true;
			}

			String maxAmount = "0";
			if ("spread".equals(event.getEventtype())) {
				maxAmount = scrapper.getSpreadmaxamount();

				if (leansenabled && islean != null && islean.booleanValue()) {
					orderAmount = scrapper.getSpreadlean().intValue();
					maxAmount = Integer.toString(scrapper.getSpreadlean().intValue());
				} else if (unitsenabled) {
					final Float units = scrapper.getSpreadunit().floatValue() * numunits;
					orderAmount = units.intValue();
					maxAmount = Integer.toString(units.intValue());
				}
			} else if ("total".equals(event.getEventtype())) {
				maxAmount = scrapper.getTotalmaxamount();

				if (leansenabled && islean != null && islean.booleanValue()) {
					orderAmount = scrapper.getTotallean().intValue();
					maxAmount = Integer.toString(scrapper.getTotallean().intValue());
				} else if (unitsenabled) {
					final Float units = scrapper.getTotalunit().floatValue() * numunits;
					orderAmount = units.intValue();
					maxAmount = Integer.toString(units.intValue());
				}
			} else if ("ml".equals(event.getEventtype())) {
				maxAmount = scrapper.getMlmaxamount();

				if (leansenabled) {
					orderAmount = scrapper.getMllean().intValue();
					maxAmount = Integer.toString(scrapper.getMllean().intValue());
				} else if (unitsenabled) {
					final Float units = scrapper.getMlunit().floatValue() * numunits;
					orderAmount = units.intValue();
					maxAmount = Integer.toString(units.intValue());
				}
			}

			// Check to make sure the account list is not empty
			if (accounts != null && !accounts.isEmpty()) {
				final Iterator<Accounts> itr = accounts.iterator();

				while (itr.hasNext()) {
					final Accounts account = itr.next();
					if (account != null) {
						final String hourafter = account.getHourafter();
						final String minuteafter = account.getMinuteafter();
						final String hourbefore = account.getHourbefore();
						final String minutebefore = account.getMinutebefore();

						if (hourafter.equals("00") && hourbefore.equals("00")) {
							accountlist.add(account);
						} else if (!hourbefore.equals("00") && !hourafter.equals("00")) {
							final String timezone = account.getTimezone();
							Calendar start = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
							Calendar end = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
							Calendar now = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));

							if ("ET".equals(timezone)) {
								start = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
								end = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
								now = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
							} else if ("CT".equals(timezone)) {
								start = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
								end = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
								now = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
							} else if ("MT".equals(timezone)) {
								start = Calendar.getInstance(TimeZone.getTimeZone("America/Denver"));
								end = Calendar.getInstance(TimeZone.getTimeZone("America/Denver"));
								now = Calendar.getInstance(TimeZone.getTimeZone("America/Denver"));
							} else if ("PT".equals(timezone)) {
								start = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
								end = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
								now = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
							}

							start.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourafter));
							start.set(Calendar.MINUTE, Integer.parseInt(minuteafter));
							start.set(Calendar.SECOND, 0);
							end.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourbefore));
							end.set(Calendar.MINUTE, Integer.parseInt(minutebefore));
							end.set(Calendar.SECOND, 0);

							LOGGER.error("now: " + now.getTime());
							LOGGER.error("start: " + start.getTime());
							LOGGER.error("end: " + end.getTime());

							// Check if game time is within time frame
							if (now.after(start) && now.before(end)) {
								accountlist.add(account);
							}
						} else if (!hourbefore.equals("00")) {
							final String timezone = account.getTimezone();
							Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
							Calendar game = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));

							if ("ET".equals(timezone)) {
								cal = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
								game = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
							} else if ("CT".equals(timezone)) {
								cal = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
								game = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));					
							} else if ("MT".equals(timezone)) {
								cal = Calendar.getInstance(TimeZone.getTimeZone("America/Denver"));
								game = Calendar.getInstance(TimeZone.getTimeZone("America/Denver"));					
							} else if ("PT".equals(timezone)) {
								cal = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
								game = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
							}

							final Date gameDate = event.getEventdatetime();
							game.setTime(gameDate);
							game.add(Calendar.HOUR, Integer.parseInt("-" + hourbefore));
							game.add(Calendar.MINUTE, Integer.parseInt("-" + minutebefore));

							// Check if game time is within time frame
							if (cal.after(game)) {
								accountlist.add(account);
							}
						} else if (!hourafter.equals("00")) {
							final String timezone = account.getTimezone();
							Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
							Calendar now = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));

							if ("ET".equals(timezone)) {
								cal = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
								now = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
							} else if ("CT".equals(timezone)) {
								cal = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
								now = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
							} else if ("MT".equals(timezone)) {
								cal = Calendar.getInstance(TimeZone.getTimeZone("America/Denver"));
								now = Calendar.getInstance(TimeZone.getTimeZone("America/Denver"));
							} else if ("PT".equals(timezone)) {
								cal = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
								now = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
							}

							cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourafter));
							cal.set(Calendar.MINUTE, Integer.parseInt(minuteafter));
							cal.set(Calendar.SECOND, 0);

							LOGGER.error("now: " + now.getTime());
							LOGGER.error("cal: " + cal.getTime());

							// Check if game time is within time frame
							if (now.after(cal)) {
								accountlist.add(account);
							}						
						}
					}
				}
			}

			new AWSBuyOrderEventResource(event, 
					accountlist, 
					maxAmount,
					scrapper.getEnableretry(),
					scrapper.getMobiletext(),
					orderAmount,
					RECORDEVENTDB,
					scrapper.getSendtextforaccount(),
					scrapper.getHumanspeed());
		} catch (Throwable e) {
			LOGGER.error(e.getMessage(), e);
		}

		LOGGER.info("Exiting setupBuyOrderAccounts()");
	}

	/**
	 * 
	 * @param event
	 * @param accounts
	 * @param scrapper
	 */
	protected void setupBestPriceBuyOrderAccounts(BaseRecordEvent event, 
			Set<Accounts> accounts, 
			BaseScrapper scrapper,
			Float numunits, 
			Boolean islean) {
		LOGGER.info("Entering setupBestPriceBuyOrderAccounts()");
		LOGGER.debug("BaseRecordEvent: " + event);
		LOGGER.debug("Accounts: " + accounts);
		LOGGER.debug("BaseScrapper: " + scrapper);

		try {
			final Set<Accounts> accountlist = new HashSet<Accounts>();
			boolean leansenabled = false;
			boolean unitsenabled = false;
			Integer orderAmount = scrapper.getOrderamount();
			if (scrapper.getLeanssenabled() != null &&
				scrapper.getLeanssenabled().booleanValue() &&
				islean != null &&
				islean.booleanValue()) {
				leansenabled = true;
			} else if (scrapper.getUnitsenabled() != null &&
				scrapper.getUnitsenabled().booleanValue() &&
				numunits != null) {
				unitsenabled = true;
			}

			String maxAmount = "0";
			if ("spread".equals(event.getEventtype())) {
				maxAmount = scrapper.getSpreadmaxamount();

				if (leansenabled && islean != null && islean.booleanValue()) {
					orderAmount = scrapper.getSpreadlean().intValue();
					maxAmount = Integer.toString(scrapper.getSpreadlean().intValue());
				} else if (unitsenabled) {
					final Float units = scrapper.getSpreadunit().floatValue() * numunits;
					orderAmount = units.intValue();
					maxAmount = Integer.toString(units.intValue());
				}
			} else if ("total".equals(event.getEventtype())) {
				maxAmount = scrapper.getTotalmaxamount();

				if (leansenabled && islean != null && islean.booleanValue()) {
					orderAmount = scrapper.getTotallean().intValue();
					maxAmount = Integer.toString(scrapper.getTotallean().intValue());
				} else if (unitsenabled) {
					final Float units = scrapper.getTotalunit().floatValue() * numunits;
					orderAmount = units.intValue();
					maxAmount = Integer.toString(units.intValue());
				}
			} else if ("ml".equals(event.getEventtype())) {
				maxAmount = scrapper.getMlmaxamount();

				if (leansenabled) {
					orderAmount = scrapper.getMllean().intValue();
					maxAmount = Integer.toString(scrapper.getMllean().intValue());
				} else if (unitsenabled) {
					final Float units = scrapper.getMlunit().floatValue() * numunits;
					orderAmount = units.intValue();
					maxAmount = Integer.toString(units.intValue());
				}
			}

			// Check to make sure the account list is not empty
			if (accounts != null && !accounts.isEmpty()) {
				final Iterator<Accounts> itr = accounts.iterator();

				while (itr.hasNext()) {
					final Accounts account = itr.next();
					if (account != null) {
						final String hourafter = account.getHourafter();
						final String minuteafter = account.getMinuteafter();
						final String hourbefore = account.getHourbefore();
						final String minutebefore = account.getMinutebefore();

						if (hourafter.equals("00") && hourbefore.equals("00")) {
							accountlist.add(account);
						} else if (!hourbefore.equals("00") && !hourafter.equals("00")) {
							final String timezone = account.getTimezone();
							Calendar start = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
							Calendar end = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
							Calendar now = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));

							if ("ET".equals(timezone)) {
								start = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
								end = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
								now = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
							} else if ("CT".equals(timezone)) {
								start = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
								end = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
								now = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
							} else if ("MT".equals(timezone)) {
								start = Calendar.getInstance(TimeZone.getTimeZone("America/Denver"));
								end = Calendar.getInstance(TimeZone.getTimeZone("America/Denver"));
								now = Calendar.getInstance(TimeZone.getTimeZone("America/Denver"));
							} else if ("PT".equals(timezone)) {
								start = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
								end = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
								now = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
							}

							start.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourafter));
							start.set(Calendar.MINUTE, Integer.parseInt(minuteafter));
							start.set(Calendar.SECOND, 0);
							end.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourbefore));
							end.set(Calendar.MINUTE, Integer.parseInt(minutebefore));
							end.set(Calendar.SECOND, 0);

							LOGGER.error("now: " + now.getTime());
							LOGGER.error("start: " + start.getTime());
							LOGGER.error("end: " + end.getTime());

							// Check if game time is within time frame
							if (now.after(start) && now.before(end)) {
								accountlist.add(account);
							}
						} else if (!hourbefore.equals("00")) {
							final String timezone = account.getTimezone();
							Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
							Calendar game = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));

							if ("ET".equals(timezone)) {
								cal = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
								game = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
							} else if ("CT".equals(timezone)) {
								cal = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
								game = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));					
							} else if ("MT".equals(timezone)) {
								cal = Calendar.getInstance(TimeZone.getTimeZone("America/Denver"));
								game = Calendar.getInstance(TimeZone.getTimeZone("America/Denver"));					
							} else if ("PT".equals(timezone)) {
								cal = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
								game = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
							}

							final Date gameDate = event.getEventdatetime();
							game.setTime(gameDate);
							game.add(Calendar.HOUR, Integer.parseInt("-" + hourbefore));
							game.add(Calendar.MINUTE, Integer.parseInt("-" + minutebefore));

							// Check if game time is within time frame
							if (cal.after(game)) {
								accountlist.add(account);
							}
						} else if (!hourafter.equals("00")) {
							final String timezone = account.getTimezone();
							Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
							Calendar now = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));

							if ("ET".equals(timezone)) {
								cal = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
								now = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
							} else if ("CT".equals(timezone)) {
								cal = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
								now = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
							} else if ("MT".equals(timezone)) {
								cal = Calendar.getInstance(TimeZone.getTimeZone("America/Denver"));
								now = Calendar.getInstance(TimeZone.getTimeZone("America/Denver"));
							} else if ("PT".equals(timezone)) {
								cal = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
								now = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
							}

							cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourafter));
							cal.set(Calendar.MINUTE, Integer.parseInt(minuteafter));
							cal.set(Calendar.SECOND, 0);

							LOGGER.error("now: " + now.getTime());
							LOGGER.error("cal: " + cal.getTime());

							// Check if game time is within time frame
							if (now.after(cal)) {
								accountlist.add(account);
							}						
						}
					}
				}
			}

			final List<Accounts> newaccountlist = new ArrayList<Accounts>(accountlist);
			new BestPriceBuyOrderResource(event, 
					newaccountlist,
					maxAmount,
					orderAmount,
					scrapper.getEnableretry(),
					scrapper.getMobiletext(),
					scrapper.getSendtextforaccount(),
					RECORDEVENTDB,
					this.SITEACTIVEDB,
					this.SITEEVENTSDB,
					scrapper.getHumanspeed());
		} catch (Throwable e) {
			LOGGER.error(e.getMessage(), e);
		}

		LOGGER.info("Exiting setupBestPriceBuyOrderAccounts()");
	}

	/**
	 * 
	 * @param event
	 */
	protected void setupImmediateEvents(BaseRecordEvent event) throws SQLException {
		LOGGER.info("Entering setupImmediateEvents()");
		LOGGER.debug("Event: " + event);
		
		// First check to see if it's an account or group
//		try {
//			List<AccountEvent> accountEvents = null;
//			if ("spread".equals(event.getEventtype())) {
//				accountEvents = RECORDEVENTDB.getSpreadActiveAccountEvents(event.getId());
//			} else if ("total".equals(event.getEventtype())) {
//				accountEvents = RECORDEVENTDB.getTotalActiveAccountEvents(event.getId());
//			} else if ("ml".equals(event.getEventtype())) {
//				accountEvents = RECORDEVENTDB.getMlActiveAccountEvents(event.getId());
//			}
//			LOGGER.debug("AccountEvents: " + accountEvents);
//
//			// Make sure there is at least one
//			if (accountEvents != null && accountEvents.size() > 0) {
//				final ExecutorService executor = Executors.newFixedThreadPool(accountEvents.size());
//				List<Future<?>> futures = new ArrayList<Future<?>>();
//
//				for (int i = 0; i < accountEvents.size(); i++) {
//					final AccountEvent ae = accountEvents.get(i);
//					if (GlobalProperties.isLocal() || ae.getIscomplexcaptcha()) {
//						final TransactionEventResource transactionEventResource = new TransactionEventResource();
//						LOGGER.debug("AccountEvent: " + ae);
//
//						transactionEventResource.setAccountEvent(ae);
//						final EntityManager entityManager2 = Persistence.createEntityManagerFactory("entityManager").createEntityManager();
//						accountDAO.setEntityManager(entityManager2);						
//						final RecordEventDAO recordEventDAO = new RecordEventDAOImpl();
//						recordEventDAO.setEntityManager(entityManager2);
//						transactionEventResource.setAccountDAO(accountDAO);
//						transactionEventResource.setRecordEventDAO(recordEventDAO);
//						LOGGER.debug("TransactionEventResource: " + transactionEventResource);
//						final Runnable worker = transactionEventResource;
//						Future<?> f = executor.submit(worker);
//						futures.add(f);
//						LOGGER.debug("Called executor");
//					} else {
//						final AWSTransactionEventResource awsTransactionEventResource = new AWSTransactionEventResource();
//						LOGGER.debug("AccountEvent: " + ae);
//
//						awsTransactionEventResource.setAccountEventId(ae.getId());
//						final Runnable worker = awsTransactionEventResource;
//						// executor.execute(worker);
//
//						executor.submit(worker);
//					}
//				}
//
//				// A) Await all runnables to be done (blocking)
//				for(Future<?> future : futures)
//				    future.get(); // get will block until the future is done
//
//				// B) Check if all runnables are done (non-blocking)
//				boolean allDone = true;
//				for(Future<?> future : futures){
//				    allDone &= future.isDone(); // check if future is done
//				    LOGGER.debug("allDone: " + allDone);
//				}
//
//				// Shut down the executor
//				executor.shutdown();
//			}
//		} catch (Exception e) {
//			LOGGER.error("Cannot get account event for " + event.getId(), e);
//		}

		LOGGER.info("Exiting setupImmediateEvents()");
	}

	/**
	 * 
	 * @param event
	 * @param account
	 * @param enableRetry
	 * @param humanspeed
	 * @return
	 */
	protected AccountEvent populateAccountEvent(BaseRecordEvent event, Accounts account, Boolean enableRetry, Boolean humanspeed) {
		LOGGER.info("Entering populateAccountEvent()");

		final AccountEvent accountEvent = new AccountEvent();
		if ("spread".equals(event.getEventtype())) {
			accountEvent.setSpreadid(event.getId());
			accountEvent.setMaxspreadamount(account.getSpreadlimitamount());
		} else if ("total".equals(event.getEventtype())) {
			accountEvent.setTotalid(event.getId());
			accountEvent.setMaxtotalamount(account.getTotallimitamount());
		} else if ("ml".equals(event.getEventtype())) {
			accountEvent.setMlid(event.getId());
			accountEvent.setMaxmlamount(account.getMllimitamount());
		}
		accountEvent.setAccountid(account.getId());
		accountEvent.setAttempts(event.getAttempts());
		accountEvent.setGroupid(event.getGroupid());
		accountEvent.setEventname(event.getEventname());
		accountEvent.setEventdatetime(event.getEventdatetime());
		accountEvent.setName(account.getName());
		accountEvent.setOwnerpercentage(account.getOwnerpercentage());
		accountEvent.setPartnerpercentage(account.getPartnerpercentage());
		accountEvent.setProxy(account.getProxylocation());
		accountEvent.setSport(event.getSport());
		accountEvent.setStatus("In Progress");
		accountEvent.setTimezone(account.getTimezone());
		accountEvent.setType(event.getEventtype());
		accountEvent.setWagertype(event.getWtype());
		accountEvent.setUserid(event.getUserid());
		accountEvent.setHumanspeed(humanspeed);

		// Check if we should retry on failed attempts
		if (enableRetry) {
			accountEvent.setAttempts(new Integer(11));
		}

		LOGGER.info("Exiting populateAccountEvent()");
		return accountEvent;
	}

	/**
	 * 
	 * @param pendingEvent1
	 * @param pendingEvent2
	 * @return
	 */
	protected PendingEvent copyPendingEvent(PendingEvent pendingEvent1, PendingEvent pendingEvent2) {
		LOGGER.debug("Entering copyPendingEvent()");

		pendingEvent2.setAccountid(pendingEvent1.getAccountid());
		pendingEvent2.setAccountname(pendingEvent1.getAccountname());
		pendingEvent2.setCustomerid(pendingEvent1.getCustomerid());
		pendingEvent2.setDateaccepted(pendingEvent1.getDateaccepted());
		pendingEvent2.setDatecreated(pendingEvent1.getDatecreated());
		pendingEvent2.setDatemodified(pendingEvent1.getDatemodified());
		pendingEvent2.setDoposturl(pendingEvent1.getDoposturl());
		pendingEvent2.setEventdate(pendingEvent1.getEventdate());
		pendingEvent2.setEventtype(pendingEvent1.getEventtype());
		pendingEvent2.setGamesport(pendingEvent1.getGamesport());
		pendingEvent2.setGametype(pendingEvent1.getGametype());
		pendingEvent2.setGamedate(pendingEvent1.getGamedate());
		pendingEvent2.setId(pendingEvent1.getId());
		pendingEvent2.setInet(pendingEvent1.getInet());
		pendingEvent2.setJuice(pendingEvent1.getJuice());
		pendingEvent2.setJuiceplusminus(pendingEvent1.getJuiceplusminus());
		pendingEvent2.setLine(pendingEvent1.getLine());
		pendingEvent2.setLineplusminus(pendingEvent1.getLineplusminus());
		pendingEvent2.setLinetype(pendingEvent1.getLinetype());
		pendingEvent2.setPosturl(pendingEvent1.getPosturl());
		pendingEvent2.setRisk(pendingEvent1.getRisk());
		pendingEvent2.setRotationid(pendingEvent1.getRotationid());
		pendingEvent2.setTeam(pendingEvent1.getTeam());
		pendingEvent2.setTicketnum(pendingEvent1.getTicketnum());
		pendingEvent2.setWin(pendingEvent1.getWin());
		pendingEvent2.setPitcher1(pendingEvent1.getPitcher1());
		pendingEvent2.setPitcher2(pendingEvent1.getPitcher2());
		pendingEvent2.setListedpitchers(pendingEvent1.getListedpitchers());
		pendingEvent2.setTransactiontype(pendingEvent1.getTransactiontype());
		pendingEvent2.setParlaynumber(pendingEvent1.getParlaynumber());
		pendingEvent2.setAmountrisk(pendingEvent1.getAmountrisk());
		pendingEvent2.setAmountwin(pendingEvent1.getAmountwin());
		pendingEvent2.setParlayseqnum(pendingEvent1.getParlayseqnum());
		pendingEvent2.setNumunits(pendingEvent1.getNumunits());
		pendingEvent2.setIslean(pendingEvent1.getIslean());

		LOGGER.debug("Exiting copyPendingEvent()");
		return pendingEvent2;
	}

	/**
	 * 
	 * @param gamesport
	 * @param gametype
	 * @param eventtype
	 * @param linetype
	 * @param scrapper
	 * @return
	 */
	protected boolean isSportAvailable(String gamesport, String gametype, String eventtype, String linetype, BaseScrapper scrapper) {
		LOGGER.info("Entering isSportAvailable()");
		LOGGER.debug("gamesport: " + gamesport);
		LOGGER.debug("gametype: " + gametype);
		LOGGER.debug("eventtype: " + eventtype);
		LOGGER.debug("linetype: " + linetype);
		LOGGER.debug("scrapper: " + scrapper);
		boolean retValue = false;
		
		if (eventtype == null || eventtype.equals("teaser")) {
			return retValue;
		}

		if (gamesport != null) {
			gamesport = gamesport.toLowerCase();
			if (gamesport.contains("football")) {
				if (gametype != null && linetype != null) {
					gametype = gametype.toLowerCase();
					if (gametype.contains("nfl")) {
						LOGGER.debug("gametype: NFL");
						if (eventtype.equals("spread")) {
							if (linetype.equals("game")) {
								if (scrapper.getGameonoff().booleanValue() && scrapper.getNflspreadonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("first")) {
								if (scrapper.getFirstonoff().booleanValue() && scrapper.getNflspreadonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("second")) {
								if (scrapper.getSecondonoff().booleanValue() && scrapper.getNflspreadonoff().booleanValue()) {
									retValue = true;
								}
							}
						} else if (eventtype.equals("total")) {
							if (linetype.equals("game")) {
								if (scrapper.getGameonoff().booleanValue() && scrapper.getNfltotalonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("first")) {
								if (scrapper.getFirstonoff().booleanValue() && scrapper.getNfltotalonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("second")) {
								if (scrapper.getSecondonoff().booleanValue() && scrapper.getNfltotalonoff().booleanValue()) {
									retValue = true;
								}
							}							
						} else if (eventtype.equals("ml")) {
							if (linetype.equals("game")) {
								if (scrapper.getGameonoff().booleanValue() && scrapper.getNflmlonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("first")) {
								if (scrapper.getFirstonoff().booleanValue() && scrapper.getNflmlonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("second")) {
								if (scrapper.getSecondonoff().booleanValue() && scrapper.getNflmlonoff().booleanValue()) {
									retValue = true;
								}
							}							
						}
					} else if (gametype.contains("ncaa") || gametype.contains("college football")) { // NCAA FOOTBALL
						if (eventtype.equals("spread")) {
							if (linetype.equals("game")) {
								if (scrapper.getGameonoff().booleanValue() && scrapper.getNcaafspreadonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("first")) {
								if (scrapper.getFirstonoff().booleanValue() && scrapper.getNcaafspreadonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("second")) {
								if (scrapper.getSecondonoff().booleanValue() && scrapper.getNcaafspreadonoff().booleanValue()) {
									retValue = true;
								}
							}
						} else if (eventtype.equals("total")) {
							if (linetype.equals("game")) {
								if (scrapper.getGameonoff().booleanValue() && scrapper.getNcaaftotalonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("first")) {
								if (scrapper.getFirstonoff().booleanValue() && scrapper.getNcaaftotalonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("second")) {
								if (scrapper.getSecondonoff().booleanValue() && scrapper.getNcaaftotalonoff().booleanValue()) {
									retValue = true;
								}
							}							
						} else if (eventtype.equals("ml")) {
							if (linetype.equals("game")) {
								if (scrapper.getGameonoff().booleanValue() && scrapper.getNcaafmlonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("first")) {
								if (scrapper.getFirstonoff().booleanValue() && scrapper.getNcaafmlonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("second")) {
								if (scrapper.getSecondonoff().booleanValue() && scrapper.getNcaafmlonoff().booleanValue()) {
									retValue = true;
								}
							}							
						}
					}
				}
			} else if (gamesport.contains("basketball")) {
				if (gametype != null && linetype != null) {
					gametype = gametype.toLowerCase();
					if (gametype.contains("wnba")) { // WNBA
						if (eventtype.equals("spread")) {
							if (linetype.equals("game")) {
								if (scrapper.getGameonoff().booleanValue() && scrapper.getWnbaspreadonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("first")) {
								if (scrapper.getFirstonoff().booleanValue() && scrapper.getWnbaspreadonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("second")) {
								if (scrapper.getSecondonoff().booleanValue() && scrapper.getWnbaspreadonoff().booleanValue()) {
									retValue = true;
								}
							}
						} else if (eventtype.equals("total")) {
							if (linetype.equals("game")) {
								if (scrapper.getGameonoff().booleanValue() && scrapper.getWnbatotalonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("first")) {
								if (scrapper.getFirstonoff().booleanValue() && scrapper.getWnbatotalonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("second")) {
								if (scrapper.getSecondonoff().booleanValue() && scrapper.getWnbatotalonoff().booleanValue()) {
									retValue = true;
								}
							}							
						} else if (eventtype.equals("ml")) {
							if (linetype.equals("game")) {
								if (scrapper.getGameonoff().booleanValue() && scrapper.getWnbamlonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("first")) {
								if (scrapper.getFirstonoff().booleanValue() && scrapper.getWnbamlonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("second")) {
								if (scrapper.getSecondonoff().booleanValue() && scrapper.getWnbamlonoff().booleanValue()) {
									retValue = true;
								}
							}
						}
					} else if (gametype.contains("nba")) { // NBA
						if (eventtype.equals("spread")) {
							if (linetype.equals("game")) {
								if (scrapper.getGameonoff().booleanValue()
										&& scrapper.getNbaspreadonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("first")) {
								if (scrapper.getFirstonoff().booleanValue()
										&& scrapper.getNbaspreadonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("second")) {
								if (scrapper.getSecondonoff().booleanValue()
										&& scrapper.getNbaspreadonoff().booleanValue()) {
									retValue = true;
								}
							}
						} else if (eventtype.equals("total")) {
							if (linetype.equals("game")) {
								if (scrapper.getGameonoff().booleanValue()
										&& scrapper.getNbatotalonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("first")) {
								if (scrapper.getFirstonoff().booleanValue()
										&& scrapper.getNbatotalonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("second")) {
								if (scrapper.getSecondonoff().booleanValue()
										&& scrapper.getNbatotalonoff().booleanValue()) {
									retValue = true;
								}
							}
						} else if (eventtype.equals("ml")) {
							if (linetype.equals("game")) {
								if (scrapper.getGameonoff().booleanValue() && scrapper.getNbamlonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("first")) {
								if (scrapper.getFirstonoff().booleanValue()
										&& scrapper.getNbamlonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("second")) {
								if (scrapper.getSecondonoff().booleanValue()
										&& scrapper.getNbamlonoff().booleanValue()) {
									retValue = true;
								}
							}
						}
					} else if (gametype.contains("ncaa")  || gametype.contains("college basketball")) { // NCAA BASKETBALL
						if (eventtype.equals("spread")) {
							if (linetype.equals("game")) {
								if (scrapper.getGameonoff().booleanValue() && scrapper.getNcaabspreadonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("first")) {
								if (scrapper.getFirstonoff().booleanValue() && scrapper.getNcaabspreadonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("second")) {
								if (scrapper.getSecondonoff().booleanValue() && scrapper.getNcaabspreadonoff().booleanValue()) {
									retValue = true;
								}
							}
						} else if (eventtype.equals("total")) {
							if (linetype.equals("game")) {
								if (scrapper.getGameonoff().booleanValue() && scrapper.getNcaabtotalonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("first")) {
								if (scrapper.getFirstonoff().booleanValue() && scrapper.getNcaabtotalonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("second")) {
								if (scrapper.getSecondonoff().booleanValue() && scrapper.getNcaabtotalonoff().booleanValue()) {
									retValue = true;
								}
							}							
						} else if (eventtype.equals("ml")) {
							if (linetype.equals("game")) {
								if (scrapper.getGameonoff().booleanValue() && scrapper.getNcaabmlonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("first")) {
								if (scrapper.getFirstonoff().booleanValue() && scrapper.getNcaabmlonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("second")) {
								if (scrapper.getSecondonoff().booleanValue() && scrapper.getNcaabmlonoff().booleanValue()) {
									retValue = true;
								}
							}							
						}
					}
				}				
			} else if (gamesport.contains("baseball")) {
				if (gametype != null && linetype != null) {
					gametype = gametype.toLowerCase();
					
					if (gametype.contains("mlb")) { // MLB
						if (eventtype.equals("spread")) {
							if (linetype.equals("game")) {
								if (scrapper.getGameonoff().booleanValue() && scrapper.getMlbspreadonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("first")) {
								if (scrapper.getFirstonoff().booleanValue() && scrapper.getMlbspreadonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("second")) {
								if (scrapper.getSecondonoff().booleanValue() && scrapper.getMlbspreadonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("third")) {
								if (scrapper.getThirdonoff().booleanValue() && scrapper.getMlbspreadonoff().booleanValue()) {
									retValue = true;
								}
							}
						} else if (eventtype.equals("total")) {
							if (linetype.equals("game")) {
								if (scrapper.getGameonoff().booleanValue() && scrapper.getMlbtotalonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("first")) {
								if (scrapper.getFirstonoff().booleanValue() && scrapper.getMlbtotalonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("second")) {
								if (scrapper.getSecondonoff().booleanValue() && scrapper.getMlbtotalonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("third")) {
								if (scrapper.getThirdonoff().booleanValue() && scrapper.getMlbtotalonoff().booleanValue()) {
									retValue = true;
								}
							}							
						} else if (eventtype.equals("ml")) {
							if (linetype.equals("game")) {
								if (scrapper.getGameonoff().booleanValue() && scrapper.getMlbmlonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("first")) {
								if (scrapper.getFirstonoff().booleanValue() && scrapper.getMlbmlonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("second")) {
								if (scrapper.getSecondonoff().booleanValue() && scrapper.getMlbmlonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("third")) {
								if (scrapper.getThirdonoff().booleanValue() && scrapper.getMlbmlonoff().booleanValue()) {
									retValue = true;
								}
							}
						}
					} else if (gametype.contains("international")) { // MLB
						if (eventtype.equals("spread")) {
							if (linetype.equals("game")) {
								if (scrapper.getGameonoff().booleanValue() && scrapper.getInternationalbaseballspreadonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("first")) {
								if (scrapper.getFirstonoff().booleanValue() && scrapper.getInternationalbaseballspreadonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("second")) {
								if (scrapper.getSecondonoff().booleanValue() && scrapper.getInternationalbaseballspreadonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("third")) {
								if (scrapper.getThirdonoff().booleanValue() && scrapper.getInternationalbaseballspreadonoff().booleanValue()) {
									retValue = true;
								}
							}
						} else if (eventtype.equals("total")) {
							if (linetype.equals("game")) {
								if (scrapper.getGameonoff().booleanValue() && scrapper.getInternationalbaseballtotalonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("first")) {
								if (scrapper.getFirstonoff().booleanValue() && scrapper.getInternationalbaseballtotalonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("second")) {
								if (scrapper.getSecondonoff().booleanValue() && scrapper.getInternationalbaseballtotalonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("third")) {
								if (scrapper.getThirdonoff().booleanValue() && scrapper.getInternationalbaseballtotalonoff().booleanValue()) {
									retValue = true;
								}
							}							
						} else if (eventtype.equals("ml")) {
							if (linetype.equals("game")) {
								if (scrapper.getGameonoff().booleanValue() && scrapper.getInternationalbaseballmlonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("first")) {
								if (scrapper.getFirstonoff().booleanValue() && scrapper.getInternationalbaseballmlonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("second")) {
								if (scrapper.getSecondonoff().booleanValue() && scrapper.getInternationalbaseballmlonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("third")) {
								if (scrapper.getThirdonoff().booleanValue() && scrapper.getInternationalbaseballmlonoff().booleanValue()) {
									retValue = true;
								}
							}
						}
					}
				}
			} else if (gamesport.contains("hockey")) {
				if (gametype != null && linetype != null) {
					gametype = gametype.toLowerCase();
					if (gametype.contains("nhl")) { // NHL
						if (eventtype.equals("spread")) {
							if (linetype.equals("game")) {
								if (scrapper.getGameonoff().booleanValue() && scrapper.getNhlspreadonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("first")) {
								if (scrapper.getFirstonoff().booleanValue() && scrapper.getNhlspreadonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("second")) {
								if (scrapper.getSecondonoff().booleanValue() && scrapper.getNhlspreadonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("third")) {
								if (scrapper.getThirdonoff().booleanValue() && scrapper.getNhlspreadonoff().booleanValue()) {
									retValue = true;
								}
							}
						} else if (eventtype.equals("total")) {
							if (linetype.equals("game")) {
								if (scrapper.getGameonoff().booleanValue() && scrapper.getNhltotalonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("first")) {
								if (scrapper.getFirstonoff().booleanValue() && scrapper.getNhltotalonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("second")) {
								if (scrapper.getSecondonoff().booleanValue() && scrapper.getNhltotalonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("third")) {
								if (scrapper.getThirdonoff().booleanValue() && scrapper.getNhltotalonoff().booleanValue()) {
									retValue = true;
								}
							}							
						} else if (eventtype.equals("ml")) {
							if (linetype.equals("game")) {
								if (scrapper.getGameonoff().booleanValue() && scrapper.getNhlmlonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("first")) {
								if (scrapper.getFirstonoff().booleanValue() && scrapper.getNhlmlonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("second")) {
								if (scrapper.getSecondonoff().booleanValue() && scrapper.getNhlmlonoff().booleanValue()) {
									retValue = true;
								}
							} else if (linetype.equals("third")) {
								if (scrapper.getThirdonoff().booleanValue() && scrapper.getNhlmlonoff().booleanValue()) {
									retValue = true;
								}
							}
						}
					}
				}
			}
		}

		LOGGER.info("Exiting isSportAvailable()");
		return retValue;
	}

	/**
	 * 
	 * @param lineplusminus
	 * @param linedata
	 * @param juiceplusminus
	 * @param juicedata
	 * @param rotId
	 * @param eoneId
	 * @param etwoId
	 * @param scrapper
	 * @param spreadRecordEvent
	 */
	protected void setupSpread(String lineplusminus, 
			String linedata, 
			String juiceplusminus, 
			String juicedata, 
			Integer rotId, 
			Integer eoneId, 
			Integer etwoId, 	
			BaseScrapper scrapper, 
			SpreadRecordEvent spreadRecordEvent) {
		LOGGER.info("Entering setupSpread()");

		String pm = lineplusminus;
		String line = linedata;
		Double sline = null;
		LOGGER.debug("Line: " + line);
		LOGGER.debug("PM: " + pm);

		if ("+".equals(pm)) {
			sline = Double.parseDouble(line);
		} else {
			sline = Double.parseDouble(pm + line);
		}
		double sAdjustment = Double.parseDouble(scrapper.getSpreadlineadjustment());

		if (sline == 100) {
			sline = new Double(0);
		}
		LOGGER.debug("sline: " + sline);
		sline = sline - sAdjustment;
		String spreadValue = "";
		String plmn = "+";
		if (sline < 0) {
			plmn = "-";
			spreadValue = Double.toString(sline);
			spreadValue = spreadValue.substring(1);
		} else {
			spreadValue = Double.toString(sline);
		}
		LOGGER.debug("plmn: " + plmn);
		LOGGER.debug("spreadAmount: " + spreadValue);

		boolean isKeynumber = false;
		// Check for key numbers
		if (scrapper.getKeynumber().booleanValue() && 
			(spreadRecordEvent.getSport().contains("nfl") || spreadRecordEvent.getSport().contains("ncaaf"))) {
			if (sline == 6.5) {
				sline = 7.0;
				isKeynumber = true;
			} else if (sline == -7.5) {
				sline = -7.0;
				isKeynumber = true;
			} else if (sline == 2.5) {
				sline = 3.0;
				isKeynumber = true;
			} else if (sline == -3.5) {
				sline = -3.0;
				isKeynumber = true;
			} else if (sline == 13.5) {
				sline = 14.0;
				isKeynumber = true;
			} else if (sline == -14.5) {
				sline = -14.0;
				isKeynumber = true;
			} else if (sline == 20.5) {
				sline = 21.0;
				isKeynumber = true;
			} else if (sline == -21.5) {
				sline = -21.0;
				isKeynumber = true;
			}

			if (isKeynumber) {
				if (sline < 0) {
					plmn = "-";
					spreadValue = Double.toString(sline);
					spreadValue = spreadValue.substring(1);
				} else {
					spreadValue = Double.toString(sline);
				}
				LOGGER.debug("plmn: " + plmn);
				LOGGER.debug("spreadAmount: " + spreadValue);
			}
		}

		// Now get either a specific juice or an adjustment
		String spreadJuiceplmn = null;
		String spreadJuiceValue = null;
		if (scrapper.getSpreadjuice() != null && scrapper.getSpreadjuice().length() > 0) {
			spreadJuiceplmn = scrapper.getSpreadjuiceindicator();
			spreadJuiceValue = scrapper.getSpreadjuice();

			LOGGER.debug("spreadJuiceplmn: " + spreadJuiceplmn);
			LOGGER.debug("spreadJuiceValue: " + spreadJuiceValue);
		} else {
			String spreadJuicepm = juiceplusminus;
			String spreadJuice = juicedata;
			Double spreadJuiceNumber = null;
			if ("+".equals(spreadJuicepm)) {
				spreadJuiceNumber = Double.parseDouble(spreadJuice);
			} else {
				spreadJuiceNumber = Double.parseDouble(spreadJuicepm + spreadJuice);
			}
			double spreadJuiceAdjustment = Double.parseDouble(scrapper.getSpreadjuiceadjustment());
			LOGGER.debug("spreadJuiceAdjustment: " + spreadJuiceAdjustment);
			spreadJuiceNumber = spreadJuiceNumber - spreadJuiceAdjustment;

			// Check if we crossed over
			if (spreadJuiceNumber >= 0 && spreadJuiceNumber < 100) {
				// This means we had a + juice now need to convert
				// to negative
				double offset = 100 - spreadJuiceNumber;
				spreadJuiceNumber = -100 - offset;
			}

			// If it's a key number bump up the juice
			if (isKeynumber) {
				spreadJuiceNumber = -130.0;
			}

			spreadJuiceValue = "";
			spreadJuiceplmn = "+";
			if (spreadJuiceNumber < 0) {
				spreadJuiceplmn = "-";
				spreadJuiceValue = Double.toString(spreadJuiceNumber);
				spreadJuiceValue = spreadJuiceValue.substring(1);
			} else {
				spreadJuiceValue = Double.toString(spreadJuiceNumber);
				spreadRecordEvent.setWtype("1"); // Default To Risk
			}
			LOGGER.debug("spreadJuiceplmn: " + spreadJuiceplmn);
			LOGGER.debug("spreadJuiceValue: " + spreadJuiceValue);
		}

		// MLB check
		if ("mlblines".equals(spreadRecordEvent.getSport())) {
			if ("-".equals(plmn)) {
				try {
					final Float mlbspread = Float.valueOf(plmn + spreadValue);
					if (mlbspread < -1.5) {
						spreadValue = "1.5";
					}
				} catch (Throwable t) {
					LOGGER.warn(t.getMessage(), t);
				}
			}
		}

		if (eoneId.intValue() == rotId.intValue()) {
			spreadRecordEvent.setSpreadplusminusfirstone(plmn);
			spreadRecordEvent.setSpreadinputfirstone(spreadValue);
			spreadRecordEvent.setSpreadjuiceplusminusfirstone(spreadJuiceplmn);
			spreadRecordEvent.setSpreadinputjuicefirstone(spreadJuiceValue);
		} else if (etwoId.intValue() == rotId.intValue()) {
			spreadRecordEvent.setSpreadplusminussecondone(plmn);
			spreadRecordEvent.setSpreadinputsecondone(spreadValue);
			spreadRecordEvent.setSpreadjuiceplusminussecondone(spreadJuiceplmn);
			spreadRecordEvent.setSpreadinputjuicesecondone(spreadJuiceValue);
		}

		LOGGER.info("Exiting setupSpread()");
	}

	/**
	 * 
	 * @param lineplusminus
	 * @param linedata
	 * @param juiceplusminus
	 * @param juicedata
	 * @param scrapper
	 * @param totalRecordEvent
	 */
	protected void setupTotal(String lineplusminus, String linedata, String juiceplusminus, String juicedata, BaseScrapper scrapper, TotalRecordEvent totalRecordEvent) {
		LOGGER.info("Entering setupTotal()");

		try {
			final String ou = lineplusminus;
			String line = linedata;
			LOGGER.debug("ou: " + ou);
			LOGGER.debug("line: " + line);
	
			// o75½ u75½
			Double sline = Double.parseDouble(line);
			double tAdjustment = Double.parseDouble(scrapper.getTotallineadjustment());
			if ("o".equals(ou)) {
				sline = sline + tAdjustment;
			} else {
				sline = sline - tAdjustment;
			}
			LOGGER.debug("sline: " + sline);
			String totalValue = Double.toString(sline);
			LOGGER.debug("totalValue: " + totalValue);
	
			// Get the Juice Adjustment
			String totalJuiceplmn = null;
			String totalJuiceValue = null;
			if (scrapper.getTotaljuice() != null && scrapper.getTotaljuice().length() > 0) {
				totalJuiceplmn = scrapper.getTotaljuiceindicator();
				totalJuiceValue = scrapper.getTotaljuice();

				LOGGER.debug("totalJuiceplmn: " + totalJuiceplmn);
				LOGGER.debug("totalJuiceValue: " + totalJuiceValue);
			} else {
				String totalJuicepm = juiceplusminus;
				String totalJuice = juicedata;
				Double totalJuiceNumber = null;
				if ("+".equals(totalJuicepm)) {
					totalJuiceNumber = Double.parseDouble(totalJuice);
				} else {
					totalJuiceNumber = Double.parseDouble(totalJuicepm + totalJuice);
				}
				double totalJuiceAdjustment = Double.parseDouble(scrapper.getTotaljuiceadjustment());
				LOGGER.debug("totalJuiceAdjustment: " + totalJuiceAdjustment);
				totalJuiceNumber = totalJuiceNumber - totalJuiceAdjustment;
				// Check if we crossed over
				if (totalJuiceNumber >= 0 && totalJuiceNumber < 100) {
					// This means we had a + juice now need to convert to
					// negative
					double offset = 100 - totalJuiceNumber;
					totalJuiceNumber = -100 - offset;
				}

				totalJuiceplmn = "+";
				totalJuiceValue = "";
				if (totalJuiceNumber < 0) {
					totalJuiceplmn = "-";
					totalJuiceValue = Double.toString(totalJuiceNumber);
					totalJuiceValue = totalJuiceValue.substring(1);
				} else {
					totalJuiceValue = Double.toString(totalJuiceNumber);
					totalRecordEvent.setWtype("1"); // Default To Risk
				}
				LOGGER.debug("totalJuiceplmn: " + totalJuiceplmn);
				LOGGER.debug("totalJuiceValue: " + totalJuiceValue);
			}

			if ("o".equals(ou)) {
				// This implies Over
				totalRecordEvent.setTotalinputfirstone(totalValue);
				totalRecordEvent.setTotaljuiceplusminusfirstone(totalJuiceplmn);
				totalRecordEvent.setTotalinputjuicefirstone(totalJuiceValue);
			} else if ("u".equals(ou)) {
				// This implies Under
				totalRecordEvent.setTotalinputsecondone(totalValue);
				totalRecordEvent.setTotaljuiceplusminussecondone(totalJuiceplmn);
				totalRecordEvent.setTotalinputjuicesecondone(totalJuiceValue);
			}
		} catch (Throwable t) {
			LOGGER.error("lineplusminus: " + lineplusminus);
			LOGGER.error("linedata: " + linedata);
			LOGGER.error("juiceplusminus: " + juiceplusminus);
			LOGGER.error("juicedata: " + juicedata);
			LOGGER.error("scrapper: " + scrapper);
			LOGGER.error("totalRecordEvent: " + totalRecordEvent);
			LOGGER.error(t.getMessage(), t);
		}

		LOGGER.info("Exiting setupTotal()");
	}

	/**
	 * 
	 * @param juiceplusminus
	 * @param juicedata
	 * @param rotId
	 * @param eoneId
	 * @param etwoId
	 * @param scrapper
	 * @param mlRecordEvent
	 */
	protected void setupMoneyLine(String juiceplusminus, String juicedata, Integer rotId, Integer eoneId, Integer etwoId, BaseScrapper scrapper, MlRecordEvent mlRecordEvent) {
		LOGGER.info("Entering setupMoneyLine()");

		String pm = null;
		String line = null;
		if (scrapper.getMlline() != null && scrapper.getMlline().length() > 0) {
			pm = scrapper.getMlindicator();
			line = scrapper.getMlline();
		} else {
			pm = juiceplusminus;
			line = juicedata;
			double mlValue = Double.parseDouble(pm + line);
			double smlValue = Double.parseDouble(scrapper.getMllineadjustment());
			mlValue = mlValue - smlValue;
			// Check if we crossed over
			if (mlValue >= 0 && mlValue < 100) {
				// This means we had a + juice now need to convert to
				// negative
				double offset = 100 - mlValue;
				mlValue = -100 - offset;
			}

			if (mlValue < 0) {
				pm = "-";
				mlValue = Math.abs(mlValue);
				int mlv = (int) mlValue;
				line = String.valueOf(mlv);
			} else {
				pm = "+";
				int mlv = (int) mlValue;
				line = String.valueOf(mlv);
				mlRecordEvent.setWtype("1"); // Default To Risk
			}
		}

		if (eoneId.intValue() == rotId.intValue()) {
			// This implies Over
			mlRecordEvent.setMlplusminusfirstone(pm);
			mlRecordEvent.setMlinputfirstone(line);
		} else if (etwoId.intValue() == rotId.intValue()) {
			// This implies Under
			mlRecordEvent.setMlplusminussecondone(pm);
			mlRecordEvent.setMlinputsecondone(line);
		}

		LOGGER.info("Exiting setupMoneyLine()");
	}

	/**
	 * 
	 * @param source
	 * @return
	 */
	protected Set<Accounts> copyAccounts(Set<Accounts> source) {
		final Set<Accounts> destination = new HashSet<Accounts>();

		for (Accounts account : source) {
			destination.add(account);
		}

		return destination;
	}

	/**
	 * 
	 * @return
	 */
	private String getAccessToken() {
		LOGGER.info("Entering getAccessToken()");
		String accessToken = "";

		try {
			final TicketAdvantageGmailOath ticketAdvantageGmailOath = new TicketAdvantageGmailOath();
			accessToken = ticketAdvantageGmailOath.getAccessToken();
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		LOGGER.info("Exiting getAccessToken()");
		return accessToken;
	}

	/**
	 * 
	 * @param gameSport
	 * @param gameType
	 * @param lineType
	 * @param rotationId
	 * @param parlayAccountEvent
	 */
	private void setupParlaySportAndRotation(String gameSport, String gameType, String lineType, String rotationId, ParlayAccountEvent parlayAccountEvent) {
		LOGGER.info("Entering setupParlaySportAndRotation()");

		if (gameType.contains("NFL")) {
			if ("game".equals(lineType)) {
				parlayAccountEvent.setSport("nfllines");
			} else if ("first".equals(lineType)) {
				parlayAccountEvent.setSport("nflfirst");
				if (rotationId.length() != 4 && !rotationId.startsWith("1")) {
					rotationId = "1" + rotationId;
				}
			} else if ("second".equals(lineType)) {
				parlayAccountEvent.setSport("nflsecond");
				if (rotationId.length() != 4 && !rotationId.startsWith("2")) {
					rotationId = "2" + rotationId;
				}
			}
		} else if (gameType.contains("WNBA")) {
			if ("game".equals(lineType)) {
				parlayAccountEvent.setSport("wnbalines");
			} else if ("first".equals(lineType)) {
				parlayAccountEvent.setSport("wnbafirst");
				if (rotationId.length() != 4 && !rotationId.startsWith("1")) {
					rotationId = "1" + rotationId;
				}
			} else if ("second".equals(lineType)) {
				parlayAccountEvent.setSport("wnbasecond");
				if (rotationId.length() != 4 && !rotationId.startsWith("2")) {
					rotationId = "2" + rotationId;
				}
			}
		} else if (gameType.contains("NBA")) {
			if ("game".equals(lineType)) {
				parlayAccountEvent.setSport("nbalines");
			} else if ("first".equals(lineType)) {
				parlayAccountEvent.setSport("nbafirst");
				if (rotationId.length() != 4 && !rotationId.startsWith("1")) {
					rotationId = "1" + rotationId;
				}
			} else if ("second".equals(lineType)) {
				parlayAccountEvent.setSport("nbasecond");
				if (rotationId.length() != 4 && !rotationId.startsWith("2")) {
					rotationId = "2" + rotationId;
				}
			}
		} else if (gameType.contains("NCAAB") || (gameType + " " + gameSport).contains("NCAAB") ||
				   gameType.contains("NCAA Basketball") || (gameType + " " + gameSport).contains("NCAA Basketball") ||
				   gameType.contains("College Basketball") || (gameType + " " + gameSport).contains("College Basketball") ||
				   gameType.contains("NCAA Added Basketball") || (gameType + " " + gameSport).contains("NCAA Added Basketball")) {
			if ("game".equals(lineType)) {
				parlayAccountEvent.setSport("ncaablines");
			} else if ("first".equals(lineType)) {
				parlayAccountEvent.setSport("ncaabfirst");
				if (rotationId.length() != 4 && !rotationId.startsWith("1")) {
					rotationId = "1" + rotationId;
				}
			} else if ("second".equals(lineType)) {
				parlayAccountEvent.setSport("ncaabsecond");
				if (rotationId.length() != 4 && !rotationId.startsWith("2")) {
					rotationId = "2" + rotationId;
				}
			}
		} else if (gameType.contains("NCAAF") || (gameType + " " + gameSport).contains("NCAAF") || 
				   gameType.contains("NCAA Football") || (gameType + " " + gameSport).contains("NCAA Football") || 
				   gameType.contains("College Football") || (gameType + " " + gameSport).contains("College Football") || 
				   gameType.contains("NCAA Added Football") || (gameType + " " + gameSport).contains("NCAA Added Football")) {
			if ("game".equals(lineType)) {
				parlayAccountEvent.setSport("ncaaflines");
			} else if ("first".equals(lineType)) {
				parlayAccountEvent.setSport("ncaaffirst");
				if (rotationId.length() != 4 && !rotationId.startsWith("1")) {
					rotationId = "1" + rotationId;
				}
			} else if ("second".equals(lineType)) {
				parlayAccountEvent.setSport("ncaafsecond");
				if (rotationId.length() != 4 && !rotationId.startsWith("2")) {
					rotationId = "2" + rotationId;
				}
			}
		} else if (gameType.contains("NHL")) {
			if ("game".equals(lineType)) {
				parlayAccountEvent.setSport("nhllines");
			} else if ("first".equals(lineType)) {
				parlayAccountEvent.setSport("nhlfirst");
				if (rotationId.length() != 4 && !rotationId.startsWith("1")) {
					rotationId = "1" + rotationId;
				}
			} else if ("second".equals(lineType)) {
				parlayAccountEvent.setSport("nhlsecond");
				if (rotationId.length() != 4 && !rotationId.startsWith("2")) {
					rotationId = "2" + rotationId;
				}
			} else if ("third".equals(lineType)) {
				parlayAccountEvent.setSport("nhlthird");
				if (rotationId.length() != 4 && !rotationId.startsWith("3")) {
					rotationId = "3" + rotationId;
				}
			}
		} else if (gameType.contains("MLB")) {
			if ("game".equals(lineType)) {
				parlayAccountEvent.setSport("mlblines");
			} else if ("first".equals(lineType)) {
				parlayAccountEvent.setSport("mlbfirst");
				if (rotationId.length() != 4 && !rotationId.startsWith("1")) {
					rotationId = "1" + rotationId;
				}
			} else if ("second".equals(lineType)) {
				parlayAccountEvent.setSport("mlbsecond");
				if (rotationId.length() != 4 && !rotationId.startsWith("2")) {
					rotationId = "2" + rotationId;
				}
			} else if ("third".equals(lineType)) {
				parlayAccountEvent.setSport("mlbthird");
				if (rotationId.length() != 4 && !rotationId.startsWith("3")) {
					rotationId = "3" + rotationId;
				}
			}
		} else if (gameType.contains("International Baseball")) {
			if ("game".equals(lineType)) {
				parlayAccountEvent.setSport("internationalbaseballlines");
			} else if ("first".equals(lineType)) {
				parlayAccountEvent.setSport("internationalbaseballfirst");
				if (rotationId.length() != 6 && !rotationId.startsWith("1")) {
					rotationId = "1" + rotationId;
				}
			} else if ("second".equals(lineType)) {
				parlayAccountEvent.setSport("internationalbaseballsecond");
				if (rotationId.length() != 6 && !rotationId.startsWith("2")) {
					rotationId = "2" + rotationId;
				}
			} else if ("third".equals(lineType)) {
				parlayAccountEvent.setSport("internationalbaseballthrid");
				if (rotationId.length() != 6 && !rotationId.startsWith("3")) {
					rotationId = "3" + rotationId;
				}
			}
		}

		LOGGER.info("Exiting setupParlaySportAndRotation()");
	}

	/**
	 * 
	 * @param copySitePendingEvent
	 * @param scrapper
	 */
	protected void checkSourceSiteAmount(PendingEvent copySitePendingEvent, BaseScrapper scrapper) {
		LOGGER.info("Entering checkSourceSiteAmount()");
		String stringWin = copySitePendingEvent.getWin();
		String stringRisk = copySitePendingEvent.getRisk();

		try {
			if (stringWin != null && stringWin.length() > 0 &&
				stringRisk != null && stringRisk.length() > 0) {
	
				stringWin = stringWin.replace(",", "").replace("$", "").trim();
				stringRisk = stringRisk.replace(",", "").replace("$", "").trim();
				final Float siteWin = Float.valueOf(stringWin);
				final Float siteRisk = Float.valueOf(stringRisk);
				
				final Float spreadSource = scrapper.getSpreadsourceamount();
				final Float totalSource = scrapper.getTotalsourceamount();
				final Float mlSource = scrapper.getMlsourceamount();
				final String eventType = copySitePendingEvent.getEventtype();
	
				if (siteWin.floatValue() < siteRisk.floatValue()) {
					if ("spread".equals(eventType)) {
						if (siteWin.floatValue() >= spreadSource.floatValue()) {
							placeSiteTransactions(scrapper, copySitePendingEvent, null);
						}
					} else if ("total".equals(eventType)) {
						if (siteWin.floatValue() >= totalSource.floatValue()) {
							placeSiteTransactions(scrapper, copySitePendingEvent, null);
						}
					} else if ("ml".equals(eventType)) {
						if (siteWin.floatValue() >= mlSource.floatValue()) {
							placeSiteTransactions(scrapper, copySitePendingEvent, null);
						}										
					}
				} else {
					if ("spread".equals(eventType)) {
						if (siteRisk.floatValue() >= spreadSource.floatValue()) {
							placeSiteTransactions(scrapper, copySitePendingEvent, null);
						}
					} else if ("total".equals(eventType)) {
						if (siteRisk.floatValue() >= totalSource.floatValue()) {
							placeSiteTransactions(scrapper, copySitePendingEvent, null);
						}
					} else if ("ml".equals(eventType)) {
						if (siteRisk.floatValue() >= mlSource.floatValue()) {
							placeSiteTransactions(scrapper, copySitePendingEvent, null);
						}
					}
				}
			} else {
				placeSiteTransactions(scrapper, copySitePendingEvent, null);	
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		LOGGER.info("Exiting checkSourceSiteAmount()");
	}
}