package com.wootechnologies.errorhandling;

/**
 * 
 * @author jmiller
 *
 */
public class BatchErrorMessage {
	public final static String SITE_SPREAD_TEAM_PACKAGE_ISSUE = "Incomplete Spread SiteTeamPackage";
	public final static String SITE_TOTAL_TEAM_PACKAGE_ISSUE = "Incomplete Total SiteTeamPackage";
	public final static String SITE_ML_TEAM_PACKAGE_ISSUE = "Incomplete Money Line SiteTeamPackage";
	public final static String SPREAD_NEGATIVE_TOO_LOW = "Spread is lower than site spread";
	public final static String SPREAD_POSITIVE_TOO_HIGH = "Spread is higher than site spread";
	public final static String TOTAL_OVER_TOO_LOW = "Total over is lower than site over";
	public final static String TOTAL_UNDER_TOO_HIGH = "Total under is higher than site under";
	public final static String ML_DOES_NOT_MATCH = "Moneyline does not match";
	public final static String SPREAD_TRANSACTION_NOT_SETUP = "Spread transaction not setup correctly";
	public final static String TOTAL_TRANSACTION_NOT_SETUP = "Total transaction not setup correctly";
	public final static String MONEYLINE_TRANSACTION_NOT_SETUP = "Moneyline transaction not setup correctly";
	public final static String FAILED_TO_SETUP_TRANSACTION_CORRECTLY = "Failed to get transaction number";
	public final static String ERROR_PARSING_WAGER_INFO = "Error parsing wager information";
	public final static String LINE_CHANGED_ERROR = "Line has changed";
	public final static String WAGER_ALREADY_PROCESSED = "Wager has already been processed";
	public final static String MAX_WAGER_AMOUNT_REACHED = "Maximum wager amount has already been reached";
	public final static String WAGER_AMOUNT_DOES_NOT_MATCH = "Wager amount is not the same from site amount";
	public final static String FAILED_TO_GET_TICKET_NUMBER = "Failed to get ticket number from site";
	public final static String ACCOUNT_CANNOT_RETRIEVE_EVENTS = "Cannot retrieve events for account";
	public final static String ACCOUNT_EVENT_DATE_PAST = "The event date/time has passed";
	public final static String GAME_NOT_AVAILABLE = "Cannot find the game for this transaction";
	public final static String MIN_WAGER_AMOUNT_NOT_REACHED = "Minimum wager amount has NOT been reached";
	public final static String ACCOUNT_BALANCE_LIMIT = "This transaction can't be accepted because account balance has been exceeded";
	public final static String WAGER_HAS_EXPIRED = "Your bet expired or has already been submited. Please check your open bets or redo your wager.";
	public final static String SITE_PROVIDER_NOT_FOUND = "Site provider not found";
	public final static String SITE_PARSER_EXCEPTION = "Exception parsing site";
	public final static String LOGIN_EXCEPTION = "Problem logging into site";
	public final static String SPORT_EVENT_NOT_FOUND = "Sport event not found";
	public final static String USER_ACCOUNT_EVENT_DATE_PAST = "The user defined wager event date/time has passed";
	public final static String ADD_BET_EXCEPTION = "Could not add bet";
	public final static String MAX_WAGER_UNTIL_TIME = "Maximum wager until specified time";
	public final static String PLAY_CANNOT_BE_MADE_UNTIL_DAY_OF_GAME = "Cannot make play until the day of the game";
	public final static String XHTML_DOC_PARSING_EXCEPTION = "XHTML cannot be parsed into a Document object";
	public final static String XHTML_INDEX_PARSING_EXCEPTION = "Error parsing index page";
	public final static String XHTML_LOGIN_PARSING_EXCEPTION = "Error parsing login page";
	public final static String XHTML_MENU_PARSING_EXCEPTION = "Error parsing menu page";
	public final static String XHTML_GAMES_PARSING_EXCEPTION = "Error parsing games page";
	public final static String XHTML_WAGER_PARSING_EXCEPTION = "Error parsing wager page";
	public final static String XHTML_NO_DATA_RETURNED_EXCEPTION = "Site returned no data after request";
	public final static String UNSUPPORTED_SPORT_TYPE = "Unsupported sport type";
	public final static String INVALID_PAGE_SEQUENCE = "Invalid Page Sequence on site";
	public final static String CAPTCHA_PROCESSING_EXCEPTION = "Captcha processing failed";
	public final static String FATAL_EXCEPTION = "Fatal Exception";
	public final static String RETRY_CAPTCHA = "Retry Captcha";
	public final static String DB_CONNECTION_EXCEPTION = "Database Connection Exception";
	public final static String HTTP_NETWORK_EXCEPTION = "Network Exception";
	public final static String HTTP_BAD_GATEWAY_EXCEPTION = "502 Status Code BAD GATEWAY";
	public final static String ERROR_SENDING_EMAIL = "Error sending email";
	public final static String ERROR_RETRIEVING_EMAIL = "Error retrieving email";
	public final static String CREATE_LINE_WATCH_EXCEPTION = "Error creating a line watch";
	public final static String TEAM_BET_ON_EXCEPTION = "Cannot find team bet on";
	public final static String UNEXPECTED_APPLICATION_EXCEPTION = "Unexpected application exception";
	
	public final static String TEAM_TOTAL_EVENT_NOT_FOUND = "Team Total event not present";
	
}