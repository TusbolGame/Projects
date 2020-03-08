/**
 * 
 */
package com.ticketadvantage.services.email.shootfromanywhere;

import org.apache.log4j.Logger;

import com.ticketadvantage.services.email.AccessTokenFromRefreshToken;

/**
 * @author jmiller
 *
 */
public class ShootFromAnywhereGmailOath {
	private static final Logger LOGGER = Logger.getLogger(ShootFromAnywhereGmailOath.class);
	String clientid = "439144314873-61i70o8fo7qrjikv9tae33msfo1cdpjq.apps.googleusercontent.com";
	String clientsecret = "XLYVqvLcab38BTXoBkPQsI4L";						
	String refreshtoken = "1/Fbd9Cr__juoc3GhZjp5JHkbuSjA4N_IthZcqumozftk";
	String granttype = "refresh_token";
	// String granttype = "authorization_code";

	/**
	 * 
	 */
	public ShootFromAnywhereGmailOath() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
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
