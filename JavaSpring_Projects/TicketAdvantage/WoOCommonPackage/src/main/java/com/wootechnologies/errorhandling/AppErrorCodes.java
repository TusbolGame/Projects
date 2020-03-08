/**
 * 
 */
package com.wootechnologies.errorhandling;

/**
 * @author jmiller
 *
 */
public class AppErrorCodes {
	public static final int DUPLICATE_USERNAME = 30;
	public static final int LOGIN_FAILED = 31;
	public static final int DUPLICATE_ACCOUNT = 32;
	public static final int ACCOUNT_NOT_FOUND = 33;
	public static final int DUPLICATE_GROUP = 34;
	public static final int GROUP_NOT_FOUND = 35;
	public static final int ACCOUNT_TYPE_NOT_SUPPORTED = 36;
	public static final int UNAUTHORIZED_ACCESS = 37;
	public static final int USER_NOT_FOUND = 38;
	public static final int POST_SITE_EXCEPTION = 39;
	public static final int GET_SITE_EXCEPTION = 40;
	public static final int SITE_PARSER_EXCEPTION = 41;
	public static final int TRANSACTION_EXCEPTION = 42;
	public static final int EVENTS_EXCEPTION = 42;
	public static final int SCRAPPER_NOT_FOUND = 35;
	public static final int DUPLICATE_USER_BILLING = 44;
	public static final int USER_BILLING_NOT_FOUND = 45;
	public static final int DUPLICATE_ACCOUNT_EVENT_FINAL = 46;
	public static final int ACCOUNT_EVENT_FINAL_NOT_FOUND = 47;

	public static final int UNEXPECTED_APPLICATION_EXCEPTION = 999;
}