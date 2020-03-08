/**
 * 
 */
package com.ticketadvantage.services.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.sites.sportsinsights.SportsInsightsSite;
import com.ticketadvantage.services.db.RecordEventDB;
import com.ticketadvantage.services.model.AccountEvent;
import com.ticketadvantage.services.model.Accounts;
import com.ticketadvantage.services.model.EventPackage;
import com.ticketadvantage.services.model.MlRecordEvent;
import com.ticketadvantage.services.model.TotalRecordEvent;
import com.ticketadvantage.services.site.util.AccountSite;
import com.ticketadvantage.services.transactions.RecordTransaction;

/**
 * @author jmiller
 *
 */
public class WoOMiddleRulesResource implements MiddleRulesResource, Runnable {
	private static final Logger LOGGER = Logger.getLogger(WoOMiddleRulesResource.class);
	private RecordTransaction RECORDTRANSACTION;
	private Long id;
	private String type;
	private Set<Accounts> accounts;
	private RecordEventDB RECORDEVENTDB;
	private EventPackage ep;
	private String mobileText;
	private Long userid;
	private String maxAmount;
	private Boolean sendtextforaccount;
	private Boolean humanspeed = new Boolean(false);

	/**
	 * 
	 */
	public WoOMiddleRulesResource() {
		super();
	}

	/**
	 * 
	 * @param RECORDTRANSACTION
	 * @param id
	 * @param type
	 * @param accounts
	 * @param RECORDEVENTDB
	 * @param ep
	 * @param mobileText
	 * @param userid
	 * @param maxAmount
	 */
	public WoOMiddleRulesResource(Long id, 
			String type, 
			Set<Accounts> accounts,
			RecordEventDB RECORDEVENTDB,
			EventPackage ep,
			String mobileText,
			Long userid,
			String maxAmount) {
		super();
		this.id = id;
		this.type = type;
		this.accounts = accounts;
		this.RECORDEVENTDB = RECORDEVENTDB;
		this.ep = ep;
		this.mobileText = mobileText;
		this.userid = userid;
		this.maxAmount = maxAmount;
		this.RECORDTRANSACTION = new RecordTransaction(RECORDEVENTDB);
		new Thread(this).start();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final SportsInsightsSite SportsInsightSite = new SportsInsightsSite("https://sportsinsights.actionnetwork.com", "mojaxsventures@gmail.com", "action1");
		final RecordEventDB RECORDEVENTDB = new RecordEventDB();
		final Set<Accounts> accounts = new HashSet<Accounts>();
		final Accounts account1 = new Accounts();
		account1.setDatecreated(new Date());
		account1.setId(new Long(36));
		account1.setIsactive(true);
		account1.setIsmobile(true);
		account1.setMllimitamount(500);
		account1.setName("Sports411");
		account1.setOwnerpercentage(75);
		account1.setPartnerpercentage(25);
		account1.setPassword("lenny");
		account1.setProxylocation("Los Angeles");
		account1.setShowrequestresponse(true);
		account1.setSitetype("Sports411Mobile");
		account1.setSpreadlimitamount(500);
		account1.setTimezone("PT");
		account1.setTotallimitamount(500);
		account1.setUrl("http://be.sports411.ag");
		account1.setUsername("9461");
		accounts.add(account1);

		final Accounts account2 = new Accounts();
		account2.setDatecreated(new Date());
		account2.setId(new Long(35));
		account2.setIsactive(true);
		account2.setIsmobile(false);
		account2.setMllimitamount(500);
		account2.setName("Globalsides");
		account2.setOwnerpercentage(100);
		account2.setPartnerpercentage(0);
		account2.setPassword("kent35");
		account2.setProxylocation("Dallas");
		account2.setShowrequestresponse(true);
		account2.setSitetype("MetallicaMobile");
		account2.setSpreadlimitamount(500);
		account2.setTimezone("ET");
		account2.setTotallimitamount(500);
		account2.setUrl("http://m.globalsides.com");
		account2.setUsername("kent35");
		accounts.add(account2);
		
		final EventPackage ep = SportsInsightSite.getEventById("902", "MLB");
		LOGGER.error("EP: " + ep);

		new WoOMiddleRulesResource(new Long(65),
				"ml",
				accounts,
				RECORDEVENTDB,
				ep,
				"9132195234@vtext.com",
				new Long(6),
				"2000");

		boolean done = false;
		while (!done) {
			try {
				Thread.sleep(300000);
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.service.MiddleRulesResource#setEventId(java.lang.Long)
	 */
	@Override
	public void setEventId(Long id) {
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.service.MiddleRulesResource#setEventType(java.lang.String)
	 */
	@Override
	public void setEventType(String type) {
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.service.MiddleRulesResource#setMiddleAccounts(java.util.Set)
	 */
	@Override
	public void setMiddleAccounts(Set<Accounts> accounts) {
		this.accounts = accounts;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.service.MiddleRulesResource#setRecordEventDB(com.ticketadvantage.services.db.RecordEventDB)
	 */
	@Override
	public void setRecordEventDB(RecordEventDB RECORDEVENTDB) {
		this.RECORDEVENTDB = RECORDEVENTDB;
		RECORDTRANSACTION = new RecordTransaction(RECORDEVENTDB);
	}

	/**
	 * @return the ep
	 */
	public EventPackage getEp() {
		return ep;
	}

	/**
	 * @param ep the ep to set
	 */
	public void setEp(EventPackage ep) {
		this.ep = ep;
	}

	/**
	 * @return the mobileText
	 */
	public String getMobileText() {
		return mobileText;
	}

	/**
	 * @param mobileText the mobileText to set
	 */
	public void setMobileText(String mobileText) {
		this.mobileText = mobileText;
	}

	/**
	 * @return the userid
	 */
	public Long getUserid() {
		return userid;
	}

	/**
	 * @param userid the userid to set
	 */
	public void setUserid(Long userid) {
		this.userid = userid;
	}

	/**
	 * @return the maxAmount
	 */
	public String getMaxAmount() {
		return maxAmount;
	}

	/**
	 * @param maxAmount the maxAmount to set
	 */
	public void setMaxAmount(String maxAmount) {
		this.maxAmount = maxAmount;
	}

	/**
	 * @return the sendtextforaccount
	 */
	public Boolean getSendtextforaccount() {
		return sendtextforaccount;
	}

	/**
	 * @param sendtextforaccount the sendtextforaccount to set
	 */
	public void setSendtextforaccount(Boolean sendtextforaccount) {
		this.sendtextforaccount = sendtextforaccount;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.service.MiddleRulesResource#startMiddle()
	 */
	@Override
	public void startMiddle() {
		new Thread(this).start();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		LOGGER.info("Entering run()");
		boolean complete = false;

		while (!complete) {
			try {
				if ("spread".equals(type)) {
//					final SpreadRecordEvent sre = this.RECORDEVENTDB.getSpreadEvent(id);
				} else if ("total".equals(type)) {
					final TotalRecordEvent tre = this.RECORDEVENTDB.getTotalEvent(id);
					final List<Boolean> accountComplete = new ArrayList<Boolean>();
					final List<Double> riskWin = new ArrayList<Double>();
					List<AccountEvent> totalAccountEvents = this.RECORDEVENTDB.getTotalAccountEvents(id);
					double totalAmount = 0;
					boolean done = false;
					int counter = 60;
					LOGGER.error("TotalRecordEvent: " + tre);

					while (!done && (counter > 0)) {						
						// Clear the lists first
						accountComplete.clear();
						riskWin.clear();

						// Get any amounts that are available
						totalAmount = getAmounts(totalAccountEvents, accountComplete, riskWin, totalAmount);

						// Check if we are all done
						done = checkIsComplete(accountComplete);
						LOGGER.debug("done: " + done);

						// Sleep for 1 second
						Thread.sleep(1000);
						counter--;
						LOGGER.error("counter: " + counter);

						// Grab the latest
						totalAccountEvents = this.RECORDEVENTDB.getTotalAccountEvents(id);
					}
					LOGGER.debug("totalAmount: " + totalAmount);

					if (done && totalAmount > 0) {
						// Now get the opposite side total line
						final Map<String, String> values = determineReviseTotal(tre);
						LOGGER.error("rotationId: " + values.get("rotationid"));
						LOGGER.error("indicator: " + values.get("indicator"));
						LOGGER.error("line: " + values.get("line"));
						LOGGER.error("juiceindicator: " + values.get("juiceindicator"));
						LOGGER.error("juice: " + values.get("juice"));
	
						final Map<String, String> totalValues = determineTotal(values.get("indicator"), totalAccountEvents);
						LOGGER.error("indicator: " + totalValues.get("indicator"));
						LOGGER.error("line: " + totalValues.get("line"));
	
						double juiceNumber = determineJuice(values.get("juiceindicator"), values.get("juice"));
						LOGGER.error("juiceNumber: " + juiceNumber);
						juiceNumber = juiceNumber - 5; // Default for now
	
						final Map<String, String> juiceValues = setupJuice(juiceNumber);
						LOGGER.error("juiceindictor: " + juiceValues.get("juiceindicator"));
						LOGGER.error("juice: " + juiceValues.get("juice"));
	
						// Get the accounts
						final Set<Accounts> newAccounts = getTotalAccounts(totalAmount);
	
						final Long totalId = RECORDTRANSACTION.recordTotalEvent(
								"game",
								"Baseball", 
								"MLB",
								values.get("rotationid"), 
								ep.getTeamone().getTeam(),
								ep.getTeamtwo().getTeam(),
								totalValues.get("indicator"), 
								totalValues.get("line"),
								juiceValues.get("juiceindicator"), 
								juiceValues.get("juice"),
								ep.getDateofevent() + " " + ep.getTimeofevent(), 
								ep.getEventdatetime(),
								"0", 
								"",
								"",
								"0",
								"DrH",
								"DrH", 
								mobileText, 
								"Middle",
								userid, 
								maxAmount,
								maxAmount, 
								new Boolean(false),
								new Boolean(false), 
								new Boolean(true),
								new Boolean(false), 
								0, 
								newAccounts, 
								null, 
								false,
								sendtextforaccount,
								humanspeed);
						LOGGER.error("totalId: " + totalId);
	
						// Now launch to go check
						final TotalRecordEvent totalEvent = RECORDEVENTDB.getTotalEvent(totalId);
						final List<AccountEvent> accountEvents = RECORDEVENTDB.getTotalAccountEvents(totalId);
						LOGGER.error("accountEvents.size(): " + accountEvents.size());
	
						for (AccountEvent ae : accountEvents) {
							for (Accounts account : accounts) {
								if (ae.getAccountid().longValue() == account.getId().longValue()) {
									final PreviewAndCompleteResource previewAndCompleteResource = new PreviewAndCompleteResource();
									previewAndCompleteResource.setAccountEvent(ae);
									previewAndCompleteResource.setAccount(account);
									previewAndCompleteResource.setEvent(totalEvent);
									previewAndCompleteResource.setEventType("total");
									previewAndCompleteResource.setSiteProcessor(AccountSite.GetAccountSite(account));
									previewAndCompleteResource.setCounter(new Integer(180));
									previewAndCompleteResource.setSleepTime(new Integer(10000));
									previewAndCompleteResource.startProcessing();
								}
							}
						}
					}

					// All done
					complete = true;
				} else if ("ml".equals(type)) {
					final MlRecordEvent mre = this.RECORDEVENTDB.getMlEvent(id);
					LOGGER.error("MlRecordEvent: " + mre);

					// Check for a valid ML
					if (checkForValidMl(mre)) {
						final List<Boolean> accountComplete = new ArrayList<Boolean>();
						final List<Double> riskWin = new ArrayList<Double>();
						List<AccountEvent> mlAccountEvents = this.RECORDEVENTDB.getMlAccountEvents(id);
						double mlAmount = 0;
						boolean done = false;
						int counter = 80;

						while (!done && (counter > 0)) {
							mlAmount = 0;

							// Clear the lists first
							accountComplete.clear();
							riskWin.clear();
	
							// Get any amounts that are availble
							mlAmount = getAmounts(mlAccountEvents, accountComplete, riskWin, mlAmount);
							LOGGER.error("mlAmount: " + mlAmount);
	
							// Check if we are all done
							done = checkIsComplete(accountComplete);
	
							// Sleep for 1 second
							Thread.sleep(1000);
							counter--;
							LOGGER.debug("counter: " + counter);
	
							// Grab the latest
							mlAccountEvents = this.RECORDEVENTDB.getMlAccountEvents(id);
						}
						LOGGER.error("mlAmount: " + mlAmount);
	
						if (done && mlAmount > 0) {
							// Now get the opposite side money line
							final Map<String, String> values = determineReviseLine(mre);
							LOGGER.error("rotationId: " + values.get("rotationid"));
							LOGGER.error("indicator: " + values.get("indicator"));
							LOGGER.error("line: " + values.get("line"));
		
							// Determine juice information
							float mline = determineJuice(values.get("indicator"), values.get("line"));
							LOGGER.error("mline: " + mline);
		
							// Determine new ML value
							final Map<String, String> mlValues = determineMl(mlAccountEvents);
							LOGGER.error("indicator: " + mlValues.get("indicator"));
							LOGGER.error("line: " + mlValues.get("line"));
		
							// Determine the accounts to use
							final Set<Accounts> newAccounts = getMlAccounts(mlAmount);
		
							// Record the reverse transaction
							final Long mlId = RECORDTRANSACTION.recordMlEvent(
									"game",
									"Baseball", 
									"MLB",
									values.get("rotationid"), 
									ep.getTeamone().getTeam(),
									ep.getTeamtwo().getTeam(),
									mlValues.get("indicator"), 
									mlValues.get("line"),
									mlValues.get("indicator"), 
									mlValues.get("line"),
									ep.getDateofevent() + " " + ep.getTimeofevent(), 
									ep.getEventdatetime(),
									"0", 
									"",
									"", 
									"DrH",
									"DrH", 
									mobileText, 
									"Middle",
									userid, 
									maxAmount,
									maxAmount, 
									new Boolean(false),
									new Boolean(false), 
									new Boolean(true),
									new Boolean(false), 
									0, 
									newAccounts, 
									null,
									false,
									sendtextforaccount,
									humanspeed);
							LOGGER.error("mlId: " + mlId);
		
							// Now launch to go check
							final MlRecordEvent mlEvent = RECORDEVENTDB.getMlEvent(mlId);
							final List<AccountEvent> accountEvents = RECORDEVENTDB.getMlAccountEvents(mlId);
							LOGGER.error("accountEvents.size(): " + accountEvents.size());
		
							for (AccountEvent ae : accountEvents) {
								for (Accounts account : accounts) {
									if (ae.getAccountid().longValue() == account.getId().longValue()) {
										final PreviewAndCompleteResource previewAndCompleteResource = new PreviewAndCompleteResource();
										previewAndCompleteResource.setAccountEvent(ae);
										previewAndCompleteResource.setAccount(account);
										previewAndCompleteResource.setEvent(mlEvent);
										previewAndCompleteResource.setEventType("ml");
										previewAndCompleteResource.setSiteProcessor(AccountSite.GetAccountSite(account));
										previewAndCompleteResource.setCounter(new Integer(180));
										previewAndCompleteResource.setSleepTime(new Integer(10000));
										previewAndCompleteResource.startProcessing();
									}
								}
							}
						}
					}

					// All done
					complete = true;
				}
			} catch (Throwable t) {
				LOGGER.error(t.getMessage(), t);
				complete = true;
			}
		}

		LOGGER.info("Exiting run()");
	}

	/**
	 * 
	 * @param mre
	 * @return
	 */
	private boolean checkForValidMl(MlRecordEvent mre) {
		LOGGER.info("Entering checkForValidMl()");
		boolean isvalid = false;

		// check for a ML that is greater than -250
		if (mre.getMlinputfirstone() != null && mre.getMlinputfirstone().length() > 0 &&
			mre.getMlplusminusfirstone() != null && mre.getMlplusminusfirstone().length() > 0) {
			double ml = Double.parseDouble(mre.getMlplusminusfirstone() + mre.getMlinputfirstone());
			if (ml > -250) {
				isvalid = true;
			}
		} else if (mre.getMlinputsecondone() != null && mre.getMlinputsecondone().length() > 0 &&
			mre.getMlplusminussecondone() != null && mre.getMlplusminussecondone().length() > 0) {
			double ml = Double.parseDouble(mre.getMlplusminussecondone() + mre.getMlinputsecondone());
			if (ml > -250) {
				isvalid = true;
			}
		}

		LOGGER.info("Entering checkForValidMl()");
		return isvalid;
	}

	/**
	 * 
	 * @param totalAmount
	 * @return
	 */
	private Set<Accounts> getTotalAccounts(double totalAmount) {
		LOGGER.info("Entering getTotalAccounts()");
		final Set<Accounts> newAccounts = new HashSet<Accounts>();

		// First we want to setup 50%
		double neededAmount = totalAmount * 0.5;
		if (neededAmount > 0) {
			double newTotal = 0;
			for (Accounts account : accounts) {
				if (newTotal <= neededAmount) {
					double totallimit = account.getTotallimitamount().doubleValue();
					if ((newTotal + totallimit) <= neededAmount) {
						newTotal += totallimit;
						newAccounts.add(account);
					} else {
						// what amount do we need?
						final Double newLimit = neededAmount - newTotal;
						newTotal += newLimit;
						account.setTotallimitamount(newLimit.intValue());
						newAccounts.add(account);
					}
				}
			}
			LOGGER.error("newTotal: " + newTotal);
		}

		LOGGER.info("Exiting getTotalAccounts()");
		return newAccounts;
	}

	/**
	 * 
	 * @param mlAmount
	 * @return
	 */
	private Set<Accounts> getMlAccounts(double mlAmount) {
		LOGGER.info("Entering getMlAccounts()");
		final Set<Accounts> newAccounts = new HashSet<Accounts>();

		// First we want to setup 50%
		double neededAmount = mlAmount * 0.5;
		if (neededAmount > 0) {
			double newTotal = 0;
			for (Accounts account : accounts) {
				if (newTotal <= neededAmount) {
					double mllimit = account.getMllimitamount().doubleValue();
					if ((newTotal + mllimit) <= neededAmount) {
						newTotal += mllimit;
						newAccounts.add(account);
					} else {
						// what amount do we need?
						final Double newLimit = neededAmount - newTotal;
						newTotal += newLimit;
						account.setMllimitamount(newLimit.intValue());
						newAccounts.add(account);
					}
				}
			}
			LOGGER.error("newTotal: " + newTotal);
		}

		LOGGER.info("Exiting getMlAccounts()");
		return newAccounts;
	}

	/**
	 * 
	 * @param indicator
	 * @param line
	 * @return
	 */
	private Map<String, String> determineTotal(String indicator, List<AccountEvent> totalAccountEvents) {
		LOGGER.info("Entering determineTotal()");
		final Map<String, String> value = new HashMap<String, String>();

		float theTotal = 0;
		for (AccountEvent ae : totalAccountEvents) {
			if ("Complete".equals(ae.getStatus())) {
				final Float totalValue = ae.getTotal();
				if (theTotal == 0) {
					theTotal = totalValue;
				} else {
					if ("u".equals(indicator)) {
						if (totalValue <= theTotal) {
							theTotal = totalValue;
						}
					} else {
						if (totalValue >= theTotal) {
							theTotal = totalValue;
						}
					}
				}
			}
		}

		float offset = (float)0.5;
		if ("u".equals(indicator)) {
			theTotal = theTotal + offset; // Default for now
			value.put("indicator", "u");
			value.put("line", Float.toString(theTotal));
		} else {
			theTotal = theTotal - offset; // Default for now
			value.put("indicator", "o");
			value.put("line", Double.toString(theTotal));
		}

		LOGGER.info("Exiting determineTotal()");
		return value;
	}

	/**
	 * 
	 * @param mlAccountEvents
	 * @return
	 */
	private Map<String, String> determineMl(List<AccountEvent> mlAccountEvents) {
		LOGGER.info("Entering determineMl()");
		final Map<String, String> value = new HashMap<String, String>();
		String mlValue = "";

		// +140
		// -150
		float theJuice = 0;
		for (AccountEvent ae : mlAccountEvents) {
			if ("Complete".equals(ae.getStatus())) {
				final Float mljuice = ae.getMljuice();
				if (theJuice == 0) {
					theJuice = mljuice;
				} else {
					if (mljuice <= theJuice) {
						theJuice = mljuice;
					}
				}
			}
		}

		// Subtract 5 from it
		theJuice = theJuice - 5;

		if (theJuice < 0) {
			mlValue = Float.toString(theJuice);
			mlValue = mlValue.substring(1);
			value.put("indicator", "+");
			value.put("line", mlValue);
		} else {
			if (theJuice < 100) {
				float tempJuice = theJuice - 100;
				tempJuice = tempJuice - 100;
				if (tempJuice < 0) {
					mlValue = Float.toString(tempJuice);
					mlValue = mlValue.substring(1);
					value.put("indicator", "-");
					value.put("line", mlValue);
				} else {
					mlValue = Float.toString(tempJuice);
					value.put("indicator", "-");
					value.put("line", mlValue);
				}
			} else {
				mlValue = Float.toString(theJuice);
				value.put("indicator", "-");
				value.put("line", mlValue);
			}
		}

		LOGGER.info("Exiting determineMl()");
		return value;
	}

	/**
	 * 
	 * @param juiceIndicator
	 * @param juice
	 * @return
	 */
	private float determineJuice(String juiceIndicator, String juice) {
		LOGGER.info("Entering determineJuice()");
		float juiceNumber = 0;

		if ("+".equals(juiceIndicator)) {
			juiceNumber = Float.parseFloat(juice);
		} else {
			juiceNumber = Float.parseFloat(juiceIndicator + juice);
		}

		if (juiceNumber == 100) {
			juiceNumber = new Float(0);
		}

		LOGGER.info("Exiting determineJuice()");
		return juiceNumber;
	}
	
	/**
	 * 
	 * @param mre
	 * @return
	 */
	private Map<String, String> determineReviseTotal(TotalRecordEvent tre) {
		LOGGER.info("Entering determineReviseTotal()");
		final Map<String, String> value = new HashMap<String, String>();
		Integer rotationId = tre.getRotationid();
		LOGGER.error("rotationId: " + rotationId);

		if (tre.getTotalinputjuicesecondone() != null && tre.getTotalinputjuicesecondone().length() > 0) {
			if ((rotationId.intValue() & 1) == 0) {
				// even...
				rotationId = rotationId - 1;
			}
			value.put("rotationid", Integer.toString(rotationId));
			value.put("indicator", "o");
			value.put("line", tre.getTotalinputsecondone());
			value.put("juiceindicator", tre.getTotaljuiceplusminussecondone());
			value.put("juice", tre.getTotalinputjuicesecondone());
		} else {
			if ((rotationId.intValue() & 1) == 0) {
				// even...
			} else {
				rotationId = rotationId.intValue() + 1;	
			}
			value.put("rotationid", Integer.toString(rotationId));
			value.put("indicator", "u");
			value.put("line", tre.getTotalinputfirstone());
			value.put("juiceindicator", tre.getTotaljuiceplusminusfirstone());
			value.put("juice", tre.getTotalinputjuicefirstone());
		}

		LOGGER.info("Exiting determineReviseTotal()");
		return value;
	}


	/**
	 * 
	 * @param mre
	 * @return
	 */
	private Map<String, String> determineReviseLine(MlRecordEvent mre) {
		LOGGER.info("Entering determineReviseLine()");
		LOGGER.debug("MlRecordEvent: " + mre);
		final Map<String, String> value = new HashMap<String, String>();
		Integer rotationId = mre.getRotationid();

		// Check for team1 or team2
		if ((rotationId.intValue() & 1) == 0) {
			// even...
			rotationId = rotationId.intValue() - 1;
			value.put("rotationid", Integer.toString(rotationId));
			value.put("indicator", mre.getMlplusminussecondone());
			value.put("line", mre.getMlinputsecondone());
		} else {
			// odd...
			rotationId = rotationId.intValue() + 1;
			value.put("rotationid", Integer.toString(rotationId));
			value.put("indicator", mre.getMlplusminusfirstone());
			value.put("line", mre.getMlinputfirstone());
		}

		LOGGER.info("Exiting determineReviseLine()");
		return value;
	}

	/**
	 * 
	 * @param juiceNumber
	 * @return
	 */
	private Map<String, String> setupJuice(double juiceNumber) {
		LOGGER.info("Entering setupJuice()");
		final Map<String, String> value = new HashMap<String, String>();
		String juiceValue = "";

		if (juiceNumber < 0) {
			value.put("juiceindicator","-");
			juiceValue = Double.toString(juiceNumber);
			juiceValue = juiceValue.substring(1);
			value.put("juice", juiceValue);
		} else {
			value.put("juiceindicator","+");
			juiceValue = Double.toString(juiceNumber);
			value.put("juice", juiceValue);
		}

		LOGGER.info("Exiting setupJuice()");
		return value;
	}

	/**
	 * 
	 * @param accountEvents
	 * @param accountComplete
	 * @param riskWin
	 * @param amount
	 * @return
	 */
	private double getAmounts(List<AccountEvent> accountEvents, List<Boolean> accountComplete, List<Double> riskWin, double amount) {
		LOGGER.info("Entering getAmounts()");

		for (AccountEvent ae : accountEvents) {
			if (ae.getStatus().equals("Complete") || ae.getStatus().equals("Error")) {
				LOGGER.error("AccountEvent: " + ae);
				accountComplete.add(true);
			} else {
				accountComplete.add(false);
			}

			// Get the amount risked/to win
			amount = determineAmount(ae, riskWin, amount);
		}

		LOGGER.info("Exiting getAmounts()");
		return amount;
	}

	/**
	 * 
	 * @param accountComplete
	 * @return
	 */
	private boolean checkIsComplete(List<Boolean> accountComplete) {
		LOGGER.info("Entering checkIsComplete()");
		boolean done = true;

		for (Boolean completed : accountComplete) {
			if (!completed.booleanValue()) {
				done = false;
			}
		}

		LOGGER.info("Exiting checkIsComplete()");
		return done;
	}

	/**
	 * 
	 * @param ae
	 * @param riskWin
	 * @param amount
	 * @return
	 */
	private double determineAmount(AccountEvent ae, List<Double> riskWin, double amount) {
		LOGGER.info("Entering determineAmount()");

		// Check for complete status
		if (ae.getStatus() != null && ae.getStatus().equals("Complete")) {
			final String risk = ae.getRiskamount();
			final String win = ae.getTowinamount();
			LOGGER.error("risk: " + risk);
			LOGGER.error("win: " + win);

			if (risk != null && risk.length() > 0 &&
				win != null && win.length() > 0) {
				Double riskAmount = Double.parseDouble(risk);
				Double winAmount = Double.parseDouble(win);
				LOGGER.error("riskAmount: " + riskAmount);
				LOGGER.error("winAmount: " + winAmount);

				if (riskAmount < winAmount) {
					riskWin.add(riskAmount);
					LOGGER.error("riskAmount: " + riskAmount);
					amount += riskAmount;
				} else {
					riskWin.add(winAmount);
					LOGGER.error("winAmount: " + winAmount);
					amount += winAmount;
				}
			} else {
				// Now what?
			}

			LOGGER.error("amount: " + amount);
		}

		LOGGER.info("Exiting determineAmount()");
		return amount;
	}
}