/**
 * 
 */
package com.ticketadvantage.services.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.TimeZone;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ticketadvantage.services.GlobalProperties;
import com.ticketadvantage.services.dao.AccountDAO;
import com.ticketadvantage.services.errorhandling.AppErrorCodes;
import com.ticketadvantage.services.errorhandling.AppErrorMessage;
import com.ticketadvantage.services.errorhandling.AppException;
import com.ticketadvantage.services.model.Accounts;
import com.ticketadvantage.services.model.ProxyLocation;
import com.ticketadvantage.services.model.SiteName;
import com.ticketadvantage.services.model.WeeklyFigures;

/**
 * @author jmiller
 *
 */
@Path("/account")
@Service
public class AccountResource {
	private static final Logger LOGGER = Logger.getLogger(AccountResource.class);

	@Autowired
	private AccountDAO accountDAO;

	/**
	 * 
	 */
	public AccountResource() {
		super();
		LOGGER.debug("Entering AccountResource()");
		LOGGER.debug("Exiting AccountResource()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * 
	 * @return
	 * @throws AppException
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Set<Accounts> getAccounts() throws AppException {
		LOGGER.info("Entering getAccounts()");
		Set<Accounts> retValue = null;

		try {
			// Find all accounts
			retValue = accountDAO.findAll();
		} catch (AppException ae) {
			LOGGER.error("AppException in getAccounts()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in getAccounts()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}

		LOGGER.debug("Accounts: " + retValue);
		LOGGER.info("Exiting getAccounts()");
		return retValue;
	}

	/**
	 * 
	 * @param userid
	 * @return
	 * @throws AppException
	 */
	@GET
	@Path("/userid/{userid}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Set<Accounts> getAccountsForUser(@PathParam("userid") Long userid) throws AppException {
		LOGGER.info("Entering getAccountsForUser()");
		LOGGER.debug("userid: " + userid);
		Set<Accounts> retValue = null;

		try {
			// Get all accounts for user
			retValue = accountDAO.getAccountsForUser(userid);
		} catch (AppException ae) {
			LOGGER.error("AppException in getAccountsForUser()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in getAccountsForUser()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}

		LOGGER.debug("Accounts: " + retValue);
		LOGGER.info("Exiting getAccountsForUser()");
		return retValue;
	}

	/**
	 * 
	 * @param userid
	 * @param account
	 * @return
	 * @throws AppException
	 */
	@POST
	@Path("/id/{userid}")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Accounts createAccountForUser(@PathParam("userid") Long userid, Accounts account) throws AppException {
		LOGGER.info("Entering createAccountForUser()");
		LOGGER.debug("userid: " + userid);
		LOGGER.debug("Account: " + account);
		Accounts retValue = null;

		try {
			// Check for which account type should be supported
			if (GlobalProperties.isStubbed()) {
				account.setSitetype("Stub");
			}

			// Create the account for the user
			retValue = accountDAO.createAccountForUser(userid, account);
		} catch (AppException ae) {
			LOGGER.error("AppException in createAccountForUser()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in createAccountForUser()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}

		LOGGER.debug("Accounts: " + retValue);
		LOGGER.info("Exiting createAccountForUser()");
		return retValue;
	}

	/**
	 * 
	 * @param account
	 * @return
	 * @throws AppException
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Accounts account(Accounts account) throws AppException {
		LOGGER.info("Entering account()");
		LOGGER.debug("Account: " + account);
		Accounts retValue = null;

		try {
			// Check for which account type should be supported
			if (GlobalProperties.isStubbed()) {
				account.setSitetype("Stub");
			} 

			// Create the account
			retValue = accountDAO.persist(account);
		} catch (AppException ae) {
			LOGGER.error("AppException in account()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in account()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}

		LOGGER.debug("Accounts: " + retValue);
		LOGGER.info("Exiting account()");
		return retValue;
	}

	/**
	 * 
	 * @param account
	 * @return
	 * @throws AppException
	 */
	@POST
	@Path("/update")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Accounts updateAccount(Accounts account) throws AppException {
		LOGGER.info("Entering updateAccount()");
		LOGGER.debug("Account: " + account);
		Accounts retValue = null;

		try {
			// Update the account
			retValue = accountDAO.update(account);
		} catch (AppException ae) {
			LOGGER.error("AppException in updateAccount()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in updateAccount()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}

		LOGGER.debug("Accounts: " + retValue);
		LOGGER.info("Exiting updateAccount()");
		return retValue;
	}

	/**
	 * 
	 * @param account
	 * @return
	 * @throws AppException
	 */
	@DELETE
	@Path("/delete/{id}")
	public void deleteAccount(@QueryParam("userid") Long userid, @PathParam("id") Long id) throws AppException {
		LOGGER.info("Entering deleteAccount()");
		LOGGER.debug("User ID: " + userid);
		LOGGER.debug("Account ID: " + id);

		try {
			// Create the account
			accountDAO.delete(userid, id);
		} catch (AppException ae) {
			LOGGER.error("AppException in deleteAccount()", ae);
			throw ae;
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in deleteAccount()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}

		LOGGER.info("Exiting deleteAccount()");
	}

	/**
	 * 
	 * @return
	 * @throws AppException
	 */
	@GET
	@Path("/proxylocations")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Set<ProxyLocation> getProxyLocations() throws AppException {
		LOGGER.info("Entering getProxyLocations()");
		Set<ProxyLocation> proxies = null;

		try {
			// Get Proxy Names
			proxies = accountDAO.getProxyNames();
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in getProxyLocations()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);

		}

		LOGGER.debug("Proxies: " + proxies);
		LOGGER.info("Exiting getProxyLocations()");
		return proxies;
	}

	/**
	 * 
	 * @return
	 * @throws AppException
	 */
	@GET
	@Path("/sitetypes")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Set<SiteName> getSiteNames() throws AppException {
		LOGGER.info("Entering getSiteNames()");
		Set<SiteName> siteNames = null;

		try {
			// Get Proxy Names
			siteNames = accountDAO.getSiteNames();
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in getProxyLocations()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,
					AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);

		}

		LOGGER.debug("SiteNames: " + siteNames);
		LOGGER.info("Exiting getSiteNames()");
		return siteNames;
	}

	/**
	 * 
	 * @param userid
	 * @return
	 * @throws AppException
	 */
	@GET
	@Path("/userid/{userid}/weeklyfigures")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Set<WeeklyFigures> getWeeklyFiguresForUser(@PathParam("userid") Long userId, 		
			@QueryParam("start_date") String startDateStr,
			@QueryParam("timezone") String timezone) throws AppException {
		LOGGER.info("Entering getWeeklyFiguresForUser()");
		LOGGER.debug("userId: " + userId);
		LOGGER.debug("start_date: " + startDateStr);
		LOGGER.debug("timezone: " + timezone);
		Set<WeeklyFigures> weeklyFigures = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd z");

		try {
			if (timezone == null || timezone.length() == 0) {
				timezone = "ET";
			}
			String tz = determineTimeZone(timezone);
			Date weekStartDate  = format.parse(startDateStr + " " + tz);
			weeklyFigures = accountDAO.getWeeklyAccountFigures(userId, weekStartDate, tz);
			WeeklyFigures summaryFigures = new WeeklyFigures();
			int index = 0;
			for (WeeklyFigures temp : weeklyFigures) {
				index++;
				summaryFigures.setMon(temp.getMon() + summaryFigures.getMon());
				summaryFigures.setTue(temp.getTue() + summaryFigures.getTue());
				summaryFigures.setWed(temp.getWed() + summaryFigures.getWed());
				summaryFigures.setThu(temp.getThu() + summaryFigures.getThu());
				summaryFigures.setFri(temp.getFri() + summaryFigures.getFri());
				summaryFigures.setSat(temp.getSat() + summaryFigures.getSat());
				summaryFigures.setSun(temp.getSun() + summaryFigures.getSun());
				summaryFigures.setWeek(temp.getWeek() + summaryFigures.getWeek());
				summaryFigures.setPending(temp.getPending() + summaryFigures.getPending());
				summaryFigures.setBalance(temp.getBalance() + summaryFigures.getBalance());
			}
			
			summaryFigures.setAccountName("TOTAL (" + index + ")");
			weeklyFigures.add(summaryFigures);
		} catch (AppException ae) {
			LOGGER.error("AppException in accountEvents()", ae);
			throw ae;
		} catch (ParseException e) {
			LOGGER.error("ParseException in accountEvents()", e);
			throw new AppException("Could not parse date");
		} catch (Throwable t) {
			LOGGER.error("Unexpected exception in accountEvents()", t);
			throw new AppException(500, AppErrorCodes.UNEXPECTED_APPLICATION_EXCEPTION,  
			AppErrorMessage.UNEXPECTED_APPLICATION_EXCEPTION);
		}
		
		LOGGER.info("Exiting getAccountEventsTotalForUser()");
		return weeklyFigures;
	}

	/**
	 * 
	 * @param timezone
	 * @return
	 */
	public static String determineTimeZone(String timezone) {
		LOGGER.info("Entering determineTimeZone()");
		LOGGER.info("timezone: " + timezone);
		String tZone = "EST";

		if (timezone.equals("ET")) {
			if (TimeZone.getTimeZone("America/New_York").inDaylightTime(new Date())) {
				tZone = "EDT";
			} else {
				tZone = "EST";
			}
		} else if (timezone.equals("CT")) {
			if (TimeZone.getTimeZone("America/Chicago").inDaylightTime(new Date())) {
				tZone = "CDT";
			} else {
				tZone = "CST";
			}
		} else if (timezone.equals("MT")) {
			if (TimeZone.getTimeZone("America/Denver").inDaylightTime(new Date())) {
				tZone = "MDT";
			} else {
				tZone = "MST";
			}
		} else if (timezone.equals("PT")) {
			if (TimeZone.getTimeZone("America/Los_Angeles").inDaylightTime(new Date())) {
				tZone = "PDT";
			} else {
				tZone = "PST";
			}
		} else if (timezone.equals("AKT")) {
			if (TimeZone.getTimeZone("America/Anchorage").inDaylightTime(new Date())) {
				tZone = "AKDT";
			} else {
				tZone = "AKST";
			}
		} else if (timezone.equals("HT")) {
			if (TimeZone.getTimeZone("America/Hawaii").inDaylightTime(new Date())) {
				tZone = "HDT";
			} else {
				tZone = "HST";
			}
		}

		LOGGER.debug("tZone: " + tZone);
		LOGGER.info("Exiting determineTimeZone()");
		return tZone;
	}
}