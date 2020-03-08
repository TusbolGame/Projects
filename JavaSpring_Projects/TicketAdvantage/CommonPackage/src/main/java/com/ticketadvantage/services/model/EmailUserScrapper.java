/**
 * 
 */
package com.ticketadvantage.services.model;

/**
 * @author jmiller
 *
 */
public class EmailUserScrapper extends UserScrapper {
	private String address;
	private String password;
	private String host;

	/**
	 * 
	 */
	public EmailUserScrapper() {
		super();
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EmailUserScrapper [address=" + address + ", password=" + password + ", host=" + host + "] " + super.toString();
	}
}