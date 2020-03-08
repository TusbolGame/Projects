/**
 * 
 */
package com.ticketadvantage.services.email.ticketadvantage;

import org.apache.log4j.Logger;

import com.ticketadvantage.services.email.AccessTokenFromRefreshToken;

/**
 * @author jmiller
 *
 */
public class TicketAdvantageGmailOath {
	private static final Logger LOGGER = Logger.getLogger(TicketAdvantageGmailOath.class);
	private String clientid = "529498092660-bcn5kjtr7iqr1k7fasv28ld16hgdi8p0.apps.googleusercontent.com";
	private String clientsecret = "o4VwTH0ykC3qjyeMlI7FdlaM";
	private String refreshtoken = "1/CA1PWWUpXS6Y4N0jEGtK6KnR5fCCSD4mvg2gO-ERKC4";
	private String granttype = "refresh_token";
	// String granttype = "authorization_code";

	/**
	 * 
	 */
	public TicketAdvantageGmailOath() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TicketAdvantageGmailOath ticketAdvantageGmailOath = new TicketAdvantageGmailOath();
		LOGGER.error(ticketAdvantageGmailOath.getAccessToken());
	}

	/**
	 * @return the clientid
	 */
	public String getClientid() {
		return clientid;
	}

	/**
	 * @param clientid the clientid to set
	 */
	public void setClientid(String clientid) {
		this.clientid = clientid;
	}

	/**
	 * @return the clientsecret
	 */
	public String getClientsecret() {
		return clientsecret;
	}

	/**
	 * @param clientsecret the clientsecret to set
	 */
	public void setClientsecret(String clientsecret) {
		this.clientsecret = clientsecret;
	}

	/**
	 * @return the refreshtoken
	 */
	public String getRefreshtoken() {
		return refreshtoken;
	}

	/**
	 * @param refreshtoken the refreshtoken to set
	 */
	public void setRefreshtoken(String refreshtoken) {
		this.refreshtoken = refreshtoken;
	}

	/**
	 * @return the granttype
	 */
	public String getGranttype() {
		return granttype;
	}

	/**
	 * @param granttype the granttype to set
	 */
	public void setGranttype(String granttype) {
		this.granttype = granttype;
	}

	/**
	 * 
	 * @return
	 */
	public String getAccessToken() {
		LOGGER.info("Entering getAccessToken()");
		String accessToken = "";
		// Get the email access token so we can update the users

		try {
			final AccessTokenFromRefreshToken accessTokenFromRefreshToken = new AccessTokenFromRefreshToken(clientid,
					clientsecret, 
					refreshtoken, 
					granttype);
			accessToken = accessTokenFromRefreshToken.getAccessToken();
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		LOGGER.info("Exiting getAccessToken()");
		return accessToken;
	}
}
