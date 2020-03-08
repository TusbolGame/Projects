/**
 * 
 */
package com.ticketadvantage.services.util;

/**
 * @author jmiller
 *
 */
public class ServerInfo {

	/**
	 * 
	 */
	public ServerInfo() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * 
	 * @return
	 */
	public static String getIp() {
		return "54.87.137.30";
//		return "54.210.204.148";
	}

	/**
	 * 
	 * @return
	 */
	public static String getHost() {
		return "ec2-54-87-137-30.compute-1.amazonaws.com";
//		return "ec2-54-210-204-148.compute-1.amazonaws.com";
	}

	/**
	 * 
	 * @return
	 */
	public static String getAppPort() {
		return "8080";
	}

	/**
	 * 
	 * @return
	 */
	public static String getDbPort() {
		return "38293";
	}
}