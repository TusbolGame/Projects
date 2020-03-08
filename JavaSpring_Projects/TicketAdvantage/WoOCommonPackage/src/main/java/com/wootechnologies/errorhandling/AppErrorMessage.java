package com.wootechnologies.errorhandling;

/**
 * 
 * @author jmiller
 *
 */
public class AppErrorMessage {
	public final static String DUPLICATE_USERNAME = "A user with this username is already taken";
	public final static String LOGIN_FAILED = "username/password incorrect";
	public final static String DUPLICATE_ACCOUNT = "An account with this name is already taken";
	public final static String ACCOUNT_NOT_FOUND = "Account was not found for this ID";
	public final static String DUPLICATE_GROUP = "A group with this name is already taken";
	public final static String GROUP_NOT_FOUND = "Group was not found for this ID";
	public final static String ACCOUNT_TYPE_NOT_SUPPORTED = "The account site URL provided is not supported by the platform at this time";
	public final static String UNAUTHORIZED_ACCESS = "Unauthorized: Authentication token was either missing or invalid";
	public final static String USER_NOT_FOUND = "User was not found for this ID";
	public final static String POST_SITE_EXCEPTION = "Exception retrieving data from site";
	public final static String GET_SITE_EXCEPTION = "Exception retrieving data from site";
	public final static String SITE_PARSER_EXCEPTION = "Exception parsing site";
	public final static String TRANSACTION_EXCEPTION = "Unable to determine type of transaction";
	public final static String EVENTS_EXCEPTION = "Exception retriving events";
	public final static String SCRAPPER_NOT_FOUND = "Scrapper was not found for this ID";
	public final static String UNEXPECTED_APPLICATION_EXCEPTION = "Unexpected application exception";
	public final static String DUPLICATE_USER_BILLING = "Billing information for this user and week already exists.";
	public final static String USER_BILLING_NOT_FOUND = "Billing information for user was not found";
	public final static String DUPLICATE_ACCOUNT_EVENT_FINAL = "Event final for this acount event already exists";
	public final static String ACCOUNT_EVENT_FINAL_NOT_FOUND = "Event final for account event was not found";
}