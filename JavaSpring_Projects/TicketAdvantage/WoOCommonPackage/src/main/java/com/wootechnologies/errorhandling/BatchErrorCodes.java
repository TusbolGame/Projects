/**
 * 
 */
package com.wootechnologies.errorhandling;

/**
 * @author jmiller
 *
 */
public class BatchErrorCodes {
	public static final int SITE_SPREAD_TEAM_PACKAGE_ISSUE = 1;
	public static final int SITE_TOTAL_TEAM_PACKAGE_ISSUE = 2;
	public static final int SITE_ML_TEAM_PACKAGE_ISSUE = 3;
	public static final int SPREAD_NEGATIVE_TOO_LOW = 4;
	public static final int SPREAD_POSITIVE_TOO_HIGH = 5;
	public static final int TOTAL_OVER_TOO_LOW = 6;
	public static final int TOTAL_UNDER_TOO_HIGH = 7;
	public static final int ML_DOES_NOT_MATCH = 8;
	public static final int SPREAD_TRANSACTION_NOT_SETUP = 10;
	public static final int TOTAL_TRANSACTION_NOT_SETUP = 11;
	public static final int MONEYLINE_TRANSACTION_NOT_SETUP = 12;
	public static final int FAILED_TO_SETUP_TRANSACTION_CORRECTLY = 13;
	public static final int ERROR_PARSING_WAGER_INFO = 14;
	public static final int LINE_CHANGED_ERROR = 15;
	public static final int WAGER_ALREADY_PROCESSED = 16;
	public static final int MAX_WAGER_AMOUNT_REACHED = 17;
	public static final int WAGER_AMOUNT_DOES_NOT_MATCH = 18;
	public static final int FAILED_TO_GET_TICKET_NUMBER = 19;
	public static final int ACCOUNT_CANNOT_RETRIEVE_EVENTS = 20;
	public static final int ACCOUNT_EVENT_DATE_PAST = 21;
	public static final int GAME_NOT_AVAILABLE = 22;
	public static final int MIN_WAGER_AMOUNT_NOT_REACHED = 23;
	public static final int ACCOUNT_BALANCE_LIMIT = 24;
	public static final int WAGER_HAS_EXPIRED = 25;
	public static final int SITE_PROVIDER_NOT_FOUND = 30;
	public static final int SITE_PARSER_EXCEPTION = 31;
	public static final int LOGIN_EXCEPTION = 32;
	public static final int SPORT_EVENT_NOT_FOUND = 33;
	public static final int USER_ACCOUNT_EVENT_DATE_PAST = 50;
	public static final int ADD_BET_EXCEPTION = 51;
	public static final int MAX_WAGER_UNTIL_TIME = 52;
	public static final int PLAY_CANNOT_BE_MADE_UNTIL_DAY_OF_GAME = 53;
	public static final int XHTML_DOC_PARSING_EXCEPTION = 100;
	public static final int XHTML_INDEX_PARSING_EXCEPTION = 101;
	public static final int XHTML_LOGIN_PARSING_EXCEPTION = 102;
	public static final int XHTML_MENU_PARSING_EXCEPTION = 103;
	public static final int XHTML_GAMES_PARSING_EXCEPTION = 104;
	public static final int XHTML_WAGER_PARSING_EXCEPTION = 105;
	public static final int XHTML_NO_DATA_RETURNED_EXCEPTION = 106;
	public static final int UNSUPPORTED_SPORT_TYPE = 107;
	public static final int INVALID_PAGE_SEQUENCE = 150;
	public static final int CAPTCHA_PROCESSING_EXCEPTION = 200;
	public static final int FATAL_EXCEPTION = 201;
	public static final int RETRY_CAPTCHA = 202;
	public static final int DB_CONNECTION_EXCEPTION = 300;
	public static final int HTTP_NETWORK_EXCEPTION = 400;
	public static final int HTTP_BAD_GATEWAY_EXCEPTION = 502;
	public static final int ERROR_SENDING_EMAIL = 600;
	public static final int ERROR_RETRIEVING_EMAIL = 601;
	public static final int CREATE_LINE_WATCH_EXCEPTION = 602;
	public static final int TEAM_BET_ON_EXCEPTION = 700;
	public static final int UNEXPECTED_APPLICATION_EXCEPTION = 999;
	
	public static final int TEAM_TOTAL_EVENT_NOT_FOUND = 34;
}