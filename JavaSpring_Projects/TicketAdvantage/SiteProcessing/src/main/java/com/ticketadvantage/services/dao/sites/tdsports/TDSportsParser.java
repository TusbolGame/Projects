/**
 * 
 */
package com.ticketadvantage.services.dao.sites.tdsports;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ibm.icu.util.Calendar;
import com.ticketadvantage.services.dao.sites.SelectOptionData;
import com.ticketadvantage.services.dao.sites.SiteParser;
import com.ticketadvantage.services.dao.sites.SiteTeamPackage;
import com.ticketadvantage.services.email.AccessTokenFromRefreshToken;
import com.ticketadvantage.services.email.SendText;
import com.ticketadvantage.services.errorhandling.AppErrorCodes;
import com.ticketadvantage.services.errorhandling.AppErrorMessage;
import com.ticketadvantage.services.errorhandling.AppException;
import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.PendingEvent;

/**
 * @author jmiller
 *
 */
public class TDSportsParser extends SiteParser {
	protected static final Logger LOGGER = Logger.getLogger(TDSportsParser.class);
	protected static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd yyyy hh:mm a z");
	protected static final ThreadLocal<SimpleDateFormat> PENDING_DATE_FORMAT = new ThreadLocal<SimpleDateFormat>(){
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy MMM dd hh:mm a z");
		}
	};

	protected static final ThreadLocal<SimpleDateFormat> PENDING_DATE_FORMAT_2 = new ThreadLocal<SimpleDateFormat>(){
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy MMM ddhh:mm a z");
		}
	};

	protected static final String[] WNBA_TEAMS = {
		"DREAM",
		"SKY",
		"SUN",
		"FEVER",
		"LIBERTY",
		"MYSTICS",
		"WINGS",
		"SPARKS",
		"LYNX",
		"MERCURY",
		"STARS",
		"STORM",
		"ACES"
	};
	protected boolean isMlb;

	/**
	 * Constructor
	 */
	public TDSportsParser() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// FireOnSports
//			final String xhtml = "<center><table width=\"100%\" cellspacing=\"1\" cellpadding=\"1\" border=\"0\"><tbody><tr class=\"TrGameOdd\"><td align=\"center\">Ticket#:139659800<br>Oct 07 12:16 AM<br></td><td valign=\"center\">INTERNET / -1</td><td valign=\"center\">Oct 07 12:01 AM </td><td><br>CFB<br></td><td align=\"center\">STRAIGHT BET<br>[981] CHICAGO CUBS +1½-175<br>( K HENDRICKS -R / S STRASBURG -R )<br></td><td><br>550 / 500<br></td></tr></tbody></table></center>";
//			final String xhtml = "<center><table width=\"100%\" cellspacing=\"1\" cellpadding=\"1\" border=\"0\"><tbody><tr class=\"TrGameOdd\"><td align=\"center\">Ticket#:139659800<br>Oct 07 12:16 AM<br></td><td valign=\"center\">INTERNET / -1</td><td valign=\"center\">Oct 07 12:01 AM </td><td><br>CFB<br></td><td align=\"center\">STRAIGHT BET<br>[2310] TOTAL u21½-110<br>(2H BOISE STATE vrs 2H BYU)<br></td><td><br>550 / 500<br></td></tr></tbody></table></center>";
//			final String xhtml = "<center><table width=\"100%\" cellspacing=\"1\" cellpadding=\"1\" border=\"0\"><tbody><tr class=\"TrGameOdd\"><td align=\"center\">Ticket#:139659800<br>Oct 07 12:16 AM<br></td><td valign=\"center\">INTERNET / -1</td><td valign=\"center\">Oct 07 12:01 AM </td><td><br>NHL<br></td><td align=\"center\">STRAIGHT BET<br>[72] ARIZONA COYOTES -183<br></td><td><br>550 / 500<br></td></tr></tbody></table></center>";

			// LvAction
//			final String xhtml = "<center><table width=\"100%\" cellspacing=\"1\" cellpadding=\"1\" border=\"0\"><tbody><tr class=\"TrGameOdd\"><td align=\"center\">Ticket#:139659800<br>Oct 07 12:16 AM<br></td><td valign=\"center\">INTERNET / -1</td><td valign=\"center\">Oct 07 12:01 AM </td><td><br>CFB<br></td><td align=\"center\">STRAIGHT BET<br>[981] CHICAGO CUBS +1½-175<br>( K HENDRICKS -R / S STRASBURG -R ) (NL - Divisional Playoffs - Game #1)<br></td><td><br>550 / 500<br></td></tr></tbody></table></center>";
//			final String xhtml = "<center><table width=\"100%\" cellspacing=\"1\" cellpadding=\"1\" border=\"0\"><tbody><tr class=\"TrGameOdd\"><td align=\"center\">Ticket#:139659800<br>Oct 07 12:16 AM<br></td><td valign=\"center\">INTERNET / -1</td><td valign=\"center\">Oct 07 12:01 AM </td><td><br>CFB<br></td><td align=\"center\">STRAIGHT BET<br>[1309] TOTAL o23½-105<br>(1H BOISE STATE vrs 1H BYU)<br></td><td><br>550 / 500<br></td></tr></tbody></table></center>";
//			final String xhtml = "<center><table width=\"100%\" cellspacing=\"1\" cellpadding=\"1\" border=\"0\"><tbody><tr class=\"TrGameOdd\"><td align=\"center\">Ticket#:139659800<br>Oct 07 12:16 AM<br></td><td valign=\"center\">INTERNET / -1</td><td valign=\"center\">Oct 07 12:01 AM </td><td><br>CFB<br></td><td align=\"center\">STRAIGHT BET<br>[981] CHICAGO CUBS +130<br>( ACTION ) (NL - Divisional Playoffs - Game #1)<br></td><td><br>550 / 500<br></td></tr></tbody></table></center>";

			// BetBigCity
//			final String xhtml = "<table id=\"CreateWagerTable\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody><tr class=\"TrGameOdd\"><td width=\"20%\" align=\"center\">Ticket Number: 88014656<br>Oct 0703:30 PM</td><td>Internet / -1</td><td>Oct 0403:16 PM </td><td>CFB<br></td><td>STRAIGHT BET<br>[348] PURDUE -3½-110 (ESPN-2)<br></td><td class=\"sum\">1100</td><td class=\"sum1\">1000<br></td></tr></tbody></table>";
//			final String xhtml = "<table id=\"CreateWagerTable\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody><tr class=\"TrGameOdd\"><td width=\"20%\" align=\"center\">Ticket Number: 88014656<br>Oct 0703:30 PM</td><td>Internet / -1</td><td>Oct 0403:16 PM </td><td>CFB<br></td><td>STRAIGHT BET<br>[377] WASHINGTON STATE o60-110 (FOX)<br></td><td class=\"sum\">1100</td><td class=\"sum1\">1000<br></td></tr></tbody></table>";
//			final String xhtml = "<table id=\"CreateWagerTable\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody><tr class=\"TrGameOdd\"><td width=\"20%\" align=\"center\">Ticket Number: 88014656<br>Oct 0703:30 PM</td><td>Internet / -1</td><td>Oct 0403:16 PM </td><td>CFB<br></td><td>STRAIGHT BET<br>[348] PURDUE +120 (ESPN-2)<br></td><td class=\"sum\">1100</td><td class=\"sum1\">1000<br></td></tr></tbody></table>";

			// GotoHCC
//			final String xhtml = "<center><table width=\"100%\" cellspacing=\"1\" cellpadding=\"1\" border=\"0\"><tbody><tr class=\"TrGameOdd\"><td align=\"center\">Confirmation #:58181335<br>Oct 06 07:32 PM<br></td><td valign=\"center\">INTERNET / -1</td><td valign=\"center\">Oct 06 06:47 PM </td><td><br>MLB<br><br></td><td>STRAIGHT BET<br>[981] CUBS (CHICAGO) +1½-175<br>( K HENDRICKS-R / S STRASBURG -R ) (TV: TBS)<br></td><td><br>875 / 500<br></td></tr></tbody></table></center>";
//			final String xhtml = "<center><table width=\"100%\" cellspacing=\"1\" cellpadding=\"1\" border=\"0\"><tbody><tr class=\"TrGameOdd\"><td align=\"center\">Confirmation #:58181335<br>Oct 06 07:32 PM<br></td><td valign=\"center\">INTERNET / -1</td><td valign=\"center\">Oct 06 06:47 PM </td><td><br>MLB<br><br></td><td>STRAIGHT BET<br>[1309] 1H BOISE STATE o23½-105<br></td><td><br>875 / 500<br></td></tr></tbody></table></center>";
//			final String xhtml = "<center><table width=\"100%\" cellspacing=\"1\" cellpadding=\"1\" border=\"0\"><tbody><tr class=\"TrGameOdd\"><td align=\"center\">Confirmation #:58181335<br>Oct 06 07:32 PM<br></td><td valign=\"center\">INTERNET / -1</td><td valign=\"center\">Oct 06 06:47 PM </td><td><br>MLB<br><br></td><td>STRAIGHT BET<br>[981] CUBS (CHICAGO) +135<br>( ACTION ) (TV: TBS)<br></td><td><br>875 / 500<br></td></tr></tbody></table></center>";
			
			// YoPig
//			final String xhtml = "<center><table width=\"100%\" cellspacing=\"1\" cellpadding=\"1\" border=\"0\"><tbody><tr class=\"TrGameOdd\"><td align=\"center\">Ticket#:225858515<br>Oct 06 07:32 PM<br></td><td valign=\"center\">INTERNET / -1</td><td valign=\"center\">Oct 06 06:47 PM </td><td><br>CFB<br><br></td><td>STRAIGHT BET<br>[2310] 2H BYU +3-105<br></td><td><br>875 / 500<br></td><td><br>1050 / 1000<br></td></tr></tbody></table></center>";
//			final String xhtml = "<center><table width=\"100%\" cellspacing=\"1\" cellpadding=\"1\" border=\"0\"><tbody><tr class=\"TrGameOdd\"><td align=\"center\">Ticket#:225858515<br>Oct 06 07:32 PM<br></td><td valign=\"center\">INTERNET / -1</td><td valign=\"center\">Oct 06 06:47 PM </td><td><br>CFB<br><br></td><td>STRAIGHT BET<br>[310] TOTAL u47-110<br>(BOISE STATE vrs BYU)<br></td><td><br>1050 / 1000<br></td></tr></tbody></table></center>";
//			final String xhtml = "<center><table width=\"100%\" cellspacing=\"1\" cellpadding=\"1\" border=\"0\"><tbody><tr class=\"TrGameOdd\"><td align=\"center\">Ticket#:225858515<br>Oct 06 07:32 PM<br></td><td valign=\"center\">INTERNET / -1</td><td valign=\"center\">Oct 06 06:47 PM </td><td><br>CFB<br><br></td><td>STRAIGHT BET<br>[395] LSU +135<br></td><td><br>1050 / 1000<br></td></tr></tbody></table></center>";

/*			
			final TDSportsParser tsp = new TDSportsParser();
			final Set<PendingEvent> pending = tsp.parsePendingBets(xhtml, "test1", "test2");
			final Iterator<PendingEvent> itrs = pending.iterator();
			while (itrs.hasNext()) {
				PendingEvent pe = itrs.next();
				System.out.println("PendingEvent: " + pe);
			}
*/
			try {
				//Date gDate = fixDate("Oct 10 07:05 PM" + " " + determineTimeZone("ET"), PENDING_DATE_FORMAT);
				final TDSportsParser tsp = new TDSportsParser();
//				String xhtml = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\" ><head id=\"ctl00_Head\"><title>	888east.com</title><link href=\"../css/888east.css\" rel=\"stylesheet\" type=\"text/css\" /><link href=\"../App_Themes/888east/default.css\" type=\"text/css\" rel=\"stylesheet\" /><link href=\"../App_Themes/888east/images/calendar/Calendar.css\" type=\"text/css\" rel=\"stylesheet\" /></head><center><body leftmargin=\"0\" topmargin=\"0\" marginwidth=\"0\" marginheight=\"0\"><form name=\"aspnetForm\" method=\"post\" action=\"ChangeWager.aspx?WT=0\" onkeypress=\"javascript:return WebForm_FireDefaultButton(event, 'ctl00_WagerContent_btn_Continue1')\" id=\"aspnetForm\"><div><input type=\"hidden\" name=\"__EVENTTARGET\" id=\"__EVENTTARGET\" value=\"\" /><input type=\"hidden\" name=\"__EVENTARGUMENT\" id=\"__EVENTARGUMENT\" value=\"\" /><input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"/wEPDwUKLTMxNjc3NTM3NQ8WAh4IV2FnZXJYbWwFrg08eG1sIEN1cnJlbmN5Q29kZT0iVVNEIiBQaXRjaGVyRGVmYXVsdD0iMCIgSWRVc2VyPSIwIiBGcmVlUGxheUF2YWlsYWJsZT0iMC4wMCIgSWRQcm9maWxlPSI3OTUiIElkUHJvZmlsZUxpbWl0cz0iMTEyNiIgSWRQbGF5ZXI9Ijc1NzQ4IiBMYXN0SWRXYWdlcj0iMjI3NTkxMzUiIElkQ2FsbD0iMzM2MzQ0ODkiIFdhZ2VyVHJhY2tlcj0iMTU4OTEwODMiIElmQmV0QXNrQW1vdW50PSJGYWxzZSIgQ29uZmlybT0iVHJ1ZSIgRXJyb3JDb2RlPSIwIiBFcnJvck1zZ0tleT0iIiBFcnJvck1zZ1BhcmFtcz0iIiBFcnJvck1zZz0iIiBVc2VGcmVlUGxheUF2YWlsYWJsZT0iRmFsc2UiIFNlY3VyaXR5Q29kZT0iQ0FDOUJBRUUxQUUxRTU3NTM2NDVEOTUyQTlEMjkzNkUiIElmQmV0Umlzaz0iMCIgSWZCZXRXaW49IjAiPjx3YWdlciBXYWdlclR5cGVEZXNjPSJTVFJBSUdIVCBCRVQiIFdhZ2VyVHlwZURlc2NFbmc9IlNUUkFJR0hUIEJFVCIgV2FnZXJUeXBlPSIwIiBUaWNrZXROdW1iZXI9IiIgVGVhc2VyQ2FuQnV5SGFsZj0iMCIgVGVhc2VyQ2FuQnV5T25lPSIwIiBUZWFzZXJQb2ludHNQdXJjaGFzZWQ9IjAiIEZpbGxJZFdhZ2VyPSItMSIgQW1vdW50PSI1MDAuMCIgUmlzaz0iNTUwLjAwIiBXaW49IjUwMC4wMCIgSURXVD0iMzI2MDciIFJpc2tXaW49IjEiIFJvdW5kUm9iaW5Db21iaW5hdGlvbnM9IjAiIENvbXBhY3RDb21iaW5hdGlvbnM9IiI+PGRldGFpbCBJc01MaW5lPSJGYWxzZSIgVGVhbURlc2NyaXB0aW9uPSJbOTU1XSBUT1RBTCAoV0FTIE5BVElPTkFMUyB2cnMgU1RMIENBUkRJTkFMUykiIFRlYW1EZXNjcmlwdGlvbkVuZz0iWzk1NV0gVE9UQUwgKFdBUyBOQVRJT05BTFMgdnJzIFNUTCBDQVJESU5BTFMpIiBWZXJzdXNUZWFtRGVzY3JpcHRpb249Ils5NTZdIFRPVEFMIChXQVMgTkFUSU9OQUxTIHZycyBTVEwgQ0FSRElOQUxTKSIgTGluZURlc2NyaXB0aW9uPSJvOS0xMTAiIFByZXZpb3VzTGluZURlc2NyaXB0aW9uPSJvOS0xMDUiIERlc2NyaXB0aW9uPSJbOTU1XSBUT1RBTCBvOS0xMTAgKFdBUyBOQVRJT05BTFMgdnJzIFNUTCBDQVJESU5BTFMpIiBEZXNjcmlwdGlvbkVuZz0iWzk1NV0gVE9UQUwgbzktMTEwIChXQVMgTkFUSU9OQUxTIHZycyBTVEwgQ0FSRElOQUxTKSIgSWRTcG9ydD0iTUxCIiBJZEdhbWVUeXBlPSIxIiBQYXJlbnRHYW1lPSIxNzgxMTk4IiBGYW1pbHlHYW1lPSIxNzgxMTk4IiBJZEV2ZW50PSIwIiBFdmVudERlc2NyaXB0aW9uPSIiIFBlcmlvZD0iMCIgQ2FuQ2hvb3NlUGl0Y2hlcj0iRmFsc2UiIE1hcmtGb3JEZWxldGlvbj0iRmFsc2UiIFBvaW50c1B1cmNoYXNlZEp1aWNlPSIwIiBPcmlnaW5hbE9kZHM9Ii0xMTAiIE9yaWdpbmFsUG9pbnRzPSItOSIgVmlzaXRvclBpdGNoZXI9IkcgR09OWkFMRVogLSBMIiBIb21lUGl0Y2hlcj0iSiBHQU5UIC1SIiBHYW1lRGF0ZVRpbWU9IjA4LTE0LTIwMTggMjA6MTU6MDAiIEdhbWVEYXRlPSJBdWcgMTQiIEdhbWVUaW1lPSI4OjE1IFBNIiBJc0Zhdm9yaXRlPSJUcnVlIiBJc0xpbmVGcm9tQWdlbnQ9IkZhbHNlIiBJZEdhbWU9IjE3ODExOTgiIFBsYXk9IjIiIFBvaW50cz0iLTkiIE9kZHM9Ii0xMTAiIFBpdGNoZXI9IjMiIElzT25PcGVuQmV0cz0iRmFsc2UiIFBvaW50c1B1cmNoYXNlZD0iMCIgSXNTcHJlYWQ9IkZhbHNlIiBJc1RvdGFsPSJUcnVlIiBJc0tleT0iRmFsc2UiIENoYXJ0Umlzaz0iMCIgQ2hhcnRXaW49IjAiIC8+PC93YWdlcj48L3htbD4WAmYPZBYCAgMPZBYCAgEPZBYKAgEPDxYCHgRUZXh0BQQ1MDAgZGQCAw8PFgIfAQUGMSw0MjkgZGQCBQ8PFgIfAQUGNiw1NzEgZGQCBw8PFgIfAQUBMGRkAgkPDxYCHwEFAjAgZGRkAcMKRhuvZrca8ayLoc0/SuroRFU=\" /></div><script type=\"text/javascript\">//<![CDATA[var theForm = document.forms['aspnetForm'];if (!theForm) {    theForm = document.aspnetForm;}function __doPostBack(eventTarget, eventArgument) {    if (!theForm.onsubmit || (theForm.onsubmit() != false)) {        theForm.__EVENTTARGET.value = eventTarget;        theForm.__EVENTARGUMENT.value = eventArgument;        theForm.submit();    }}//]]></script><script src=\"/WebResource.axd?d=IO2c2kLxgBd07gM-G3Sz838cp4F8sCMC-bQrqA-i8NI5gm-_uoqTarWjj_DgMcA8wfWibl1awvelqAeQ4M28fjSNIXo1&amp;t=636271419501517547\" type=\"text/javascript\"></script><div>	<input type=\"hidden\" name=\"__VIEWSTATEGENERATOR\" id=\"__VIEWSTATEGENERATOR\" value=\"C24B2D1F\" />	<input type=\"hidden\" name=\"__EVENTVALIDATION\" id=\"__EVENTVALIDATION\" value=\"/wEWAwL6/IrcBQLqlbKLCQL4396aAjstU+XTFT3/DGvDg8xJJjkBAG4s\" /></div><!--<center><div style=\"background-color:#609a34; color:#FFF; font-family:Verdana, Geneva, sans-serif; size:12px; height:20px;\"><strong>Important Announcement:</strong> In the event that Super Bowl XLVIII's date and/or venue is changed due to severe weather conditions, all existing open bets on the game will stand.</div></center> --><table id=\"Wager\" width=\"960\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">	<tr>		<td colspan=\"2\">			<img src=\"../../App_Themes/888east/images/after_login/after_login_02.png\" width=\"960\" height=\"26\" alt=\"\"></td>	</tr>	<tr>		<td colspan=\"2\">			<img src=\"../../App_Themes/888east/images/after_login/after_login_05.png\" width=\"960\" height=\"85\" alt=\"\"></td>	</tr>	<tr>		<td colspan=\"2\">			<img src=\"../../App_Themes/888east/images/after_login/after_login_07.png\" width=\"960\" height=\"1\" alt=\"\"></td>	</tr>	<tr>		<td width=\"740\">			<img src=\"../../App_Themes/888east/images/after_login/after_login_08.png\" width=\"740\" height=\"254\" alt=\"\"></td>		<td width=\"220\" background=\"../../App_Themes/888east/images/after_login/after_login_09.png\" valign=\"top\" align=\"left\">		<div style=\"padding-left:150px; padding-top:10px;\">        	<a href=\"/Logout.aspx\"><img src=\"../../App_Themes/888east/images/after_login/logout_button.png\" alt=\"Logout\"></a>		</div>				<div style=\"padding-left:113px; padding-top:15px;\">        	<span id=\"ctl00_ctl02_lblCurrentBalance\" style=\"font-family:Helvetica, Arial, sans-serif; font-size:12px; text-align:right; font-weight:bold; color:#000;\">500 </span>		</div>		<div style=\"padding-left:113px; padding-top:15px;\">        	<span id=\"ctl00_ctl02_lblRealAvailBalance\" style=\"font-family:Helvetica, Arial, sans-serif; font-size:12px; text-align:right; font-weight:bold; color:#000;\">1,429 </span>		</div>		<div style=\"padding-left:113px; padding-top:15px;\">        	<span id=\"ctl00_ctl02_lblAmountAtRisk\" style=\"font-family:Helvetica, Arial, sans-serif; font-size:12px; text-align:right; font-weight:bold; color:#000;\">6,571 </span>		</div>		<div style=\"padding-left:113px; padding-top:15px;\">        	<span id=\"ctl00_ctl02_lblBonusPoints\" style=\"font-family:Helvetica, Arial, sans-serif; font-size:12px; text-align:right; font-weight:bold; color:#000;\">0</span>		</div>		<div style=\"padding-left:113px; padding-top:15px;\">        	<span id=\"ctl00_ctl02_lblFreePlay\" style=\"font-family:Helvetica, Arial, sans-serif; font-size:12px; text-align:right; font-weight:bold; color:#000;\">0 </span>		</div>    		<div style=\"padding-left:10px; padding-top:30px;\">        	<a href=\"History.aspx\"><img src=\"../../App_Themes/888east/images/after_login/history_button.png\" alt=\"History\"></a>	  	</div>				</td>	</tr>	<tr>		<td colspan=\"2\">			<img src=\"../../App_Themes/888east/images/after_login/after_login_10.png\" alt=\"\" width=\"960\" height=\"30\" border=\"0\" usemap=\"#Map\"></td>	</tr>	<tr>		<td colspan=\"2\"><img src=\"../../App_Themes/888east/images/after_login/after_login_11.png\" alt=\"\" width=\"960\" height=\"30\" border=\"0\" usemap=\"#Map2\">		</td>	</tr>	<tr>		<td colspan=\"2\" background=\"../../App_Themes/888east/images/after_login/after_login_12.png\">     <CENTER  ><TABLE WIDTH=\"98%\" ALIGN=\"center\" CLASS=\"WagerContainer\"><TR><TD ALIGN=\"center\"><h3><span>The line changed for one (or more) of your selections.</span></h3><br><TABLE BORDER=\"0\" CLASS=\"WagerTable\" WIDTH=\"100%\" cellspacing=\"2\" cellpading=\"1\"><TR><TD WIDTH=\"80\"><B><span>WagerType:</span></B></TD><TD><B><span>Date:</span></B></TD><TD><B><span>Current Line:</span></B></TD><TD><B><span>Previous Line:</span></B></TD><TD><B><span>Risking</span> / <span>To Win</span></B></TD></TR><TR><TD>STRAIGHT BET</TD><TD>Aug 14</TD><TD>MLB [955] TOTAL (WAS NATIONALS vrs STL CARDINALS) <font class=\"LineChange\">o9-110</font>               [G GONZALEZ - L/J GANT -R]            </TD><TD>o9-105</TD><TD>550.00 USD / 500.00 USD</TD></TR></TABLE><TABLE BORDER=\"0\" WIDTH=\"100%\" ALIGN=\"center\"><TR><TD ALIGN=\"center\"><TABLE ALIGN=\"center\" class=\"WagerTable2\"><TR><TD><span>Please enter your password and click the button to confirm your wager(s).</span></TD></TR><TR><TD ALIGN=\"CENTER\"><INPUT TYPE=\"Password\" NAME=\"password\" ID=\"password\" SIZE=\"25\"><INPUT type=\"hidden\" NAME=\"RMV_0\" ID=\"RMV_0\" value=\"\"></TD></TR><TR><TD ALIGN=\"CENTER\"><input type=\"submit\" name=\"ctl00$WagerContent$btn_Continue1\" value=\"Accept Changes\" id=\"ctl00_WagerContent_btn_Continue1\" /><input type=\"submit\" name=\"ctl00$WagerContent$can_1\" value=\"Cancel Wager\" id=\"ctl00_WagerContent_can_1\" /></TD></TR></TABLE><BR></TD></TR></TABLE></TD></TR></TABLE></CENTER><SCRIPT language=\"JavaScript\"  >      var wamt; wamt = document.getElementById(\"password\"); if (wamt != null) wamt.focus();    </SCRIPT>    </td>	</tr>	<tr>		<td colspan=\"2\">			<img src=\"../../App_Themes/888east/images/after_login/after_login_15.png\" alt=\"\" width=\"960\" height=\"69\" border=\"0\" usemap=\"#Map3\"></td>	</tr>	<tr>		<td>			<img src=\"../../App_Themes/888east/images/after_login/spacer.gif\" width=\"740\" height=\"1\" alt=\"\"></td>		<td>			<img src=\"../../App_Themes/888east/images/after_login/spacer.gif\" width=\"220\" height=\"1\" alt=\"\"></td>	</tr></table><map name=\"Map\">  <area shape=\"rect\" coords=\"8,0,88,28\" href=\"welcome.aspx\">  <area shape=\"rect\" coords=\"90,3,195,27\" href=\"CreateSports.aspx?WT=0\">  <area shape=\"rect\" coords=\"198,4,259,30\" href=\"CreateSports.aspx?WT=1\">  <area shape=\"rect\" coords=\"261,4,323,31\" href=\"Chooseteaser.aspx?WT=2\">  <area shape=\"rect\" coords=\"326,4,431,37\" href=\"CreateSports.aspx?WT=5\">  <area shape=\"rect\" coords=\"434,6,537,37\" href=\"CreateSports.aspx?WT=3\">  <area shape=\"rect\" coords=\"540,3,644,40\" href=\"CreateSports.aspx?WT=7\">  <area shape=\"rect\" coords=\"645,5,752,45\" href=\"CreateSports.aspx?WT=4\">  <area shape=\"rect\" coords=\"753,4,868,27\" href=\"CreateSports.aspx?WT=8\">  <area shape=\"rect\" coords=\"870,4,959,38\" href=\"fillchoose.aspx\"></map><map name=\"Map2\">  <area shape=\"rect\" coords=\"664,1,745,32\" href=\"OpenBets.aspx\">  <area shape=\"rect\" coords=\"747,3,829,25\" href=\"../racing/Racing.aspx?ws=0\">  <area shape=\"rect\" coords=\"833,3,907,25\" href=\"Bridge.aspx?to=casino\"></map><map name=\"Map3\">  <area shape=\"rect\" coords=\"406,14,446,36\" href=\"Rules.aspx\">  <area shape=\"rect\" coords=\"451,15,513,38\" href=\"#\">  <area shape=\"rect\" coords=\"521,14,550,37\" href=\"#\"></map></form></body></center></html>";
				String xhtml = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">	<html xmlns=\"http://www.w3.org/1999/xhtml\" >	<head id=\"ctl00_Head\"><title>	All Sports Wagering, Horse Racing and Full Casino Online</title>	<base target=\"_parent\" />	<meta http-equiv=\"X-UA-Compatible\" content=\"IE=Edge\" /><link rel=\"stylesheet\" href=\"/content/css/global.css\" type=\"text/css\" />	<script type=\"text/javascript\">	function getElements(simbol, firstchild, secondchild) {	var sign = document.getElementById(simbol);	var x = document.getElementById(firstchild);	var y = document.getElementById(secondchild);	if (sign.innerHTML == \"+\") {	sign.innerHTML = \"-\";	x.style.display = \"\";	y.style.display = \"\";	}	else if (sign.innerHTML == \"-\") {	sign.innerHTML = \"+\";	x.style.display = \"none\";	y.style.display = \"none\";	}	}	function CasinoGameOpen(gameid,value2){	var targeturl=\"http://casino.cdntools.info/aspNET/Launch/Enter.aspx?login=CATJ776&password=PINK&casinogameid=\" + gameid + \"&AccountID=1&Lang=en%>\" newwin=window.open(\"\",\"casinogamingflashwindow\",\"noscrollbars\")	if (document.all){	newwin.moveTo(0,0)	newwin.resizeTo(screen.width,screen.height)	} newwin.location=targeturl;	newwin.focus();	} </script> <!-- Add jQuery library -->	<script type=\"text/javascript\" src=\"/Scripts/jquery-latest.min.js\"></script>	<script src=\"/Scripts/jquery.cookie.js\"></script>	<script type=\"text/javascript\">	if($.cookie(\"backgroundImg\")) {	$('body').css('background-image', 'url(\"' + $.cookie(\"backgroundImg\") + '\")');	var pathvalues=$.cookie(\"backgroundImg\").split('/');	cssfile = pathvalues[3].replace(\".jpg\",\".css\");	$('head').append('<link rel=\"stylesheet\" href=\"/content/css/' + cssfile + '\" type=\"text/css\" />');	}	else{	$.ajax({	type	: \"GET\",	cache	: false,	url	: \"/content/getSettings.asp\",	data	: \"IdPlayer=\"+832331+\"&IdSiteType=1\" ,	success: function(data) {	var arr = data.split('|');	if(arr.length > 1){	$.cookie(\"backgroundImg\",arr[0], {expires: 365, path: '/'}); var pathvalues=$.cookie(\"backgroundImg\").split('/');	cssfile = pathvalues[3].replace(\".jpg\",\".css\");	$('head').append('<link rel=\"stylesheet\" href=\"/content/css/' + cssfile + '\" type=\"text/css\" />');	}	else{	$('head').append('<link rel=\"stylesheet\" href=\"/content/css/1.css\" type=\"text/css\" />');	}	}	});	};	if($.cookie(\"theme\")) { $('head').append('<link rel=\"stylesheet\" href=\"' + $.cookie(\"theme\") + '\" type=\"text/css\" />');	}	else{	$.ajax({	type	: \"GET\",	cache	: false,	url	: \"/content/getSettings.asp\",	data	: \"IdPlayer=\"+832331+\"&IdSiteType=1\" ,	success: function(data) {	var arr = data.split('|');	if(arr.length > 1 && arr[1] != \"\"){ $.cookie(\"theme\",arr[1], {expires: 365, path: '/'});	$('head').append('<link rel=\"stylesheet\" href=\"' + $.cookie(\"theme\") + '\" type=\"text/css\" />');	}	else{ $.cookie(\"theme\",\"/content/css/theme_blue.css\", {expires: 365, path: '/'});	$('head').append('<link rel=\"stylesheet\" href=\"' + $.cookie(\"theme\") + '\" type=\"text/css\" />'); }	}	});	};	if(!$.cookie(\"lan\")){	$.ajax({	type	: \"GET\",	cache	: false,	url	: \"/content/getSettings.asp\",	data	: \"IdPlayer=\"+832331+\"&IdSiteType=1\" ,	success: function(data) {	var arr = data.split('|');	if(arr.length > 0 && arr[2] != \"\")	$.cookie(\"lan\",arr[2], {expires: 365, path: '/'});	else	$.cookie(\"lan\",\"en-US\", {expires: 365, path: '/'}); var url=window.location.href;	if(url.indexOf(\"?\") > -1)	window.location.replace(url	+ \"&lan=\" + arr[2]);	else window.location.replace(url	+ \"?lan=\" + arr[2]);	}	});	}; $(document).ready(function() {	$(\".various\").fancybox({	maxWidth	: 630,	maxHeight	: 450,	fitToView	: false,	width	: '80%',	height	: '50%',	autoSize	: false,	closeClick	: false,	openEffect	: 'none',	closeEffect	: 'none'	});	$(\"img\").error(function(){	$(this).hide();	});	$(\"[id^=teamlogo]\").each(function() {	value = $(this).attr(\"class\").replace(/ /g, '-').toLowerCase();	value = value.replace(/&/g, '-');	value = value.replace(/\\(.*?\\)/g, '');	if(value.substr(value.length - 1) == '-'){ value = value.substr(0, value.length-1)	}	value = value.replace(/1h-/g, '');	value = value.replace(/2h-/g, '');	value = value.replace(/1q-/g, '');	value = value.replace(/2q-/g, '');	value = value.replace(/3q-/g, '');	value = value.replace(/4q-/g, '');	value = value.replace(/49ers/g, 'a49ers');	value = value.replace(/-rsw/g, '');	$(this).attr(\"class\", value);	$(this).addClass( \"teamlogo\" )	});	$('.dd_item').each(function(){	$(this).hover(	function(){	$(this).find('.dd_overlay').show();	}, function(){	$(this).find('.dd_overlay').hide();	}	);	});	$(\".tablegames\").fadeIn();	$('.dd_CasinoMenu a').click(function(){	$(this).siblings().removeClass(\"active\"); $(this).addClass(\"active\");	$(\".dd_item\").hide();	$(\".\" + $(this).attr(\"cat\")).fadeIn();	return false;	});	});	function loadIframe(iframeName, url) {	var $iframe = $('#' + iframeName);	if ( $iframe.length ) {	$iframe.attr('src',url); return false;	}	return true;	}	</script>	<link href=\"/content/css/teamlogos.css\" type=\"text/css\" rel=\"stylesheet\" /> <!-- Add mousewheel plugin (this is optional) -->	<script type=\"text/javascript\" src=\"/Scripts/fancybox/lib/jquery.mousewheel-3.0.6.pack.js\"></script> <!-- Add fancyBox -->	<link rel=\"stylesheet\" href=\"/Scripts/fancybox/source/jquery.fancybox.css?v=2.1.5\" type=\"text/css\" media=\"screen\" />	<script type=\"text/javascript\" src=\"/Scripts/fancybox/source/jquery.fancybox.pack.js?v=2.1.5\"></script>	<!-- Optionally add helpers - button, thumbnail and/or media -->	<link rel=\"stylesheet\" href=\"/Scripts/fancybox/source/helpers/jquery.fancybox-buttons.css?v=1.0.5\" type=\"text/css\" media=\"screen\" />	<script type=\"text/javascript\" src=\"/Scripts/fancybox/source/helpers/jquery.fancybox-buttons.js?v=1.0.5\"></script>	<script type=\"text/javascript\" src=\"/Scripts/fancybox/source/helpers/jquery.fancybox-media.js?v=1.0.6\"></script>	<link rel=\"stylesheet\" href=\"/Scripts/fancybox/source/helpers/jquery.fancybox-thumbs.css?v=1.0.7\" type=\"text/css\" media=\"screen\" />	<script type=\"text/javascript\" src=\"/Scripts/fancybox/source/helpers/jquery.fancybox-thumbs.js?v=1.0.7\"></script>	<!---WebSiteBanners-->	<script type=\"text/javascript\" src=\"../content/webSiteBanners/js/simple.carousel.js\"></script>	<script type=\"text/javascript\" src=\"../content/webSiteBanners/slider-initialization.js\"></script>	<script type=\"text/javascript\">	var sTimeOut = setTimeout(function () {	jQuery(document).ready(function() {	// sliderWSB	$(\"ul.slider\").simplecarousel({	width:920,	height:120, auto: 4000,	fade: 400,	pagination: false	});	});	}, 1000);	</script>	<style>	.slider {	margin:0;	padding:0;	list-style:none;	width:920px;	height:120px;	overflow:hidden;	} .slider li {	text-align:left;	display:block;	width:920px;	height:120px;	}	.carousel-pagination li {	display:none;	width:10px;	height:10px;	margin-right:5px;	cursor:pointer; float:left;	background:#333;	}	</style>	<!---WebSiteBanners-->	<link href=\"../App_Themes/betcatalinasports/default.css\" type=\"text/css\" rel=\"stylesheet\" /><link href=\"../App_Themes/betcatalinasports/images/calendar/Calendar.css\" type=\"text/css\" rel=\"stylesheet\" /><link href=\"../App_Themes/betcatalinasports/SpryTabbedPanels.css\" type=\"text/css\" rel=\"stylesheet\" /></head>	<body>	<input type=\"hidden\" name=\"sliderUser\" id=\"sliderUser\" value=\"832331\" />	<input type=\"hidden\" name=\"sliderSiteTypeId\" id=\"sliderSiteTypeId\" value=\"3\" />	<input type=\"hidden\" name=\"sliderBetSystem\" id=\"sliderBetSystem\" value=\"4\" /	<a id=\"Content_Top\"></a>	<form name=\"aspnetForm\" method=\"post\" action=\"ChangeWager.aspx?WT=0\" onkeypress=\"javascript:return WebForm_FireDefaultButton(event, 'ctl00_WagerContent_btn_Continue1')\" id=\"aspnetForm\"><div><input type=\"hidden\" name=\"__EVENTTARGET\" id=\"__EVENTTARGET\" value=\"\" /><input type=\"hidden\" name=\"__EVENTARGUMENT\" id=\"__EVENTARGUMENT\" value=\"\" /><input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"/wEPDwUKLTMxNjc3NTM3NQ8WAh4IV2FnZXJYbWwFqQw8eG1sIEN1cnJlbmN5Q29kZT0iVVNEIiBQaXRjaGVyRGVmYXVsdD0iMCIgSWRVc2VyPSIwIiBGcmVlUGxheUF2YWlsYWJsZT0iMC4wMCIgSWRQcm9maWxlPSIzMjQiIElkUHJvZmlsZUxpbWl0cz0iMjM3ODUiIElkUGxheWVyPSI4MzIzMzEiIExhc3RJZFdhZ2VyPSIyMDk1NDAyMjgiIElkQ2FsbD0iMzQ4MzU4MDMyIiBXYWdlclRyYWNrZXI9IjE0MzA3Nzk2NCIgSWZCZXRBc2tBbW91bnQ9IlRydWUiIENvbmZpcm09IlRydWUiIEVycm9yQ29kZT0iMCIgRXJyb3JNc2dLZXk9IiIgRXJyb3JNc2dQYXJhbXM9IiIgRXJyb3JNc2c9IiIgVXNlRnJlZVBsYXlBdmFpbGFibGU9IkZhbHNlIiBTZWN1cml0eUNvZGU9IjU1RENCRDAyMTdBRDlCNUM1RUQ5NEU3MDc0MEQ0NTM0IiBJZkJldFJpc2s9IjAiIElmQmV0V2luPSIwIj48d2FnZXIgV2FnZXJUeXBlRGVzYz0iU1RSQUlHSFQgQkVUIiBXYWdlclR5cGVEZXNjRW5nPSJTVFJBSUdIVCBCRVQiIFdhZ2VyVHlwZT0iMCIgVGlja2V0TnVtYmVyPSIiIFRlYXNlckNhbkJ1eUhhbGY9IjAiIFRlYXNlckNhbkJ1eU9uZT0iMCIgVGVhc2VyUG9pbnRzUHVyY2hhc2VkPSIwIiBGaWxsSWRXYWdlcj0iLTEiIEFtb3VudD0iNTAwIiBSaXNrPSI1MDAuMDAiIFdpbj0iNjEwLjAwIiBJRFdUPSIxMTcwMCIgUmlza1dpbj0iMiIgUm91bmRSb2JpbkNvbWJpbmF0aW9ucz0iMCIgQ29tcGFjdENvbWJpbmF0aW9ucz0iIj48ZGV0YWlsIElzTUxpbmU9IlRydWUiIFRlYW1EZXNjcmlwdGlvbj0iWzE5MDVdIDFIIENJTiBSRURTIiBUZWFtRGVzY3JpcHRpb25Fbmc9IlsxOTA1XSAxSCBDSU4gUkVEUyIgVmVyc3VzVGVhbURlc2NyaXB0aW9uPSJbMTkwNl0gMUggU1RMIENBUkRJTkFMUyIgTGluZURlc2NyaXB0aW9uPSIrMTIyIiBQcmV2aW91c0xpbmVEZXNjcmlwdGlvbj0iKzEyNiIgRGVzY3JpcHRpb249IlsxOTA1XSAxSCBDSU4gUkVEUyArMTIyIiBEZXNjcmlwdGlvbkVuZz0iWzE5MDVdIDFIIENJTiBSRURTICsxMjIiIElkU3BvcnQ9Ik1MQiIgSWRHYW1lVHlwZT0iMTIiIFBhcmVudEdhbWU9IjI4MDQwODEiIEZhbWlseUdhbWU9IjI4MDQwODEiIElkRXZlbnQ9IjAiIEV2ZW50RGVzY3JpcHRpb249IiIgUGVyaW9kPSIxIiBDYW5DaG9vc2VQaXRjaGVyPSJGYWxzZSIgTWFya0ZvckRlbGV0aW9uPSJGYWxzZSIgUG9pbnRzUHVyY2hhc2VkSnVpY2U9IjAiIE9yaWdpbmFsT2Rkcz0iMTIyIiBPcmlnaW5hbFBvaW50cz0iMCIgVmlzaXRvclBpdGNoZXI9IkEuIERFU0NMQUZBTkkgLVIiIEhvbWVQaXRjaGVyPSJMLiBXRUFWRVIgLVIiIEdhbWVEYXRlVGltZT0iMDktMDItMjAxOCAxNDoxNTowMCIgR2FtZURhdGU9IlNlcCAwMiIgR2FtZVRpbWU9IjI6MTUgUE0iIElzRmF2b3JpdGU9IkZhbHNlIiBJc0xpbmVGcm9tQWdlbnQ9IkZhbHNlIiBJZEdhbWU9IjI4MDQwOTYiIFBsYXk9IjQiIFBvaW50cz0iMCIgT2Rkcz0iMTIyIiBQaXRjaGVyPSIzIiBJc09uT3BlbkJldHM9IkZhbHNlIiBQb2ludHNQdXJjaGFzZWQ9IjAiIElzU3ByZWFkPSJGYWxzZSIgSXNUb3RhbD0iRmFsc2UiIElzS2V5PSJGYWxzZSIgQ2hhcnRSaXNrPSIwIiBDaGFydFdpbj0iMCIgLz48L3dhZ2VyPjwveG1sPhYCZg9kFgRmD2QWBgIEDxUCB0NBVEo3NzYEUElOS2QCBg8VAQY4MzIzMzFkAgoPFQIGODMyMzMxBjgzMjMzMWQCAQ9kFgICCA9kFggCBQ8PFgIeBFRleHQFBy00LDIwMCBkZAIJDw8WAh8BBQYyLDI1MCBkZAINDw8WAh8BBQcxMyw1NTAgZGQCEQ8PFgIfAQUCMCBkZGSP3mMc4k5Yf+9wiueBDVVoBu47FA==\" /></div><script type=\"text/javascript\">//<![CDATA[var theForm = document.forms['aspnetForm'];if (!theForm) { theForm = document.aspnetForm;}function __doPostBack(eventTarget, eventArgument) { if (!theForm.onsubmit || (theForm.onsubmit() != false)) { theForm.__EVENTTARGET.value = eventTarget; theForm.__EVENTARGUMENT.value = eventArgument; theForm.submit(); }}//]]></script><script src=\"/WebResource.axd?d=AuxhVh8N7FOyovMBWaqD0YfpTbgQztryxWXJbFYftJWyur55uNE1CPUxLpoidnbSMpqh6_HWVgLUHSrNxI3r3x0qFlU1&amp;t=636284489597151108\" type=\"text/javascript\"></script><div>	<input type=\"hidden\" name=\"__VIEWSTATEGENERATOR\" id=\"__VIEWSTATEGENERATOR\" value=\"C24B2D1F\" />	<input type=\"hidden\" name=\"__EVENTVALIDATION\" id=\"__EVENTVALIDATION\" value=\"/wEWAwL9n8O0BgLqlbKLCQL4396aAsIRGFJqeFhh+Flo6z+dibuWHZns\" /></div>	<div class=\"dd_topbar\">	<a class=\"dd_logo\"><img src=\"/Master/betcatalinasports/graphics/WagerLogo.png\" /></a>	<div class=\"dd_right\"> <a class=\"various fancybox.iframe\" href=\"/content/themes.html?IdPlayer=832331\"><img src=\"/Master/betcatalinasports/images/settings.jpg\" />	<span id=\"ctl00_Settings\">Settings</span>	</a>	<a href=\"javascript:window.print()\"><img src=\"/Master/betcatalinasports/images/print_icon.jpg\" /> <span id=\"ctl00_Print\">Print</span>	</a>	<a href=\"../../Logout.aspx\"><img src=\"/Master/betcatalinasports/images/logout_icon.jpg\" /> <span id=\"ctl00_Logout\">Logout</span>	</a>	</div>	<div class=\"dd_center\">	<a href=\"../../wager/CreateSports.aspx?wt=0\"><span id=\"ctl00_Sports\">Sports</span></a>	<a href=\"../../racing/RacingHorsesLoader.aspx?ws=0\"><span id=\"ctl00_Horses\">Horses</span></a>	</div>	</div>	<!-- BALANCE INFO -->	<div class=\"dd_secondbar_container\">	<div class=\"dd_secondbar\">	<!--<a class=\"dd_btn\" href=\"http://www.1betvegas.com/football-sheet.pdf\"><span id=\"ctl00_AccountFigures2_DailySheetTop\">Daily Sheet</span></a>-->	<div class=\"dd_balance\">	<p><span id=\"ctl00_AccountFigures2_BalanceTop\">Balance</span>: <strong><span id=\"ctl00_AccountFigures2_lblCurrentBalance\">-4,200 </span></strong></p>	<p><span id=\"ctl00_AccountFigures2_PendingTop\">Pending</span>: <strong><span id=\"ctl00_AccountFigures2_lblAmountAtRisk\">2,250 </span></strong></p>	<p><span id=\"ctl00_AccountFigures2_AvailableTop\">Available</span>: <strong><span id=\"ctl00_AccountFigures2_lblRealAvailBalance\">13,550 </span></strong></p>	<p><span id=\"ctl00_AccountFigures2_LocalizedLabel12\">Free Plays</span>: <strong><span id=\"ctl00_AccountFigures2_lblFreePlay\">0 </span></strong></p>	</div>	</div> </div>	<div class=\"dd_menu\">	<ul class=\"dd_mainmenu\">	<li><a href=\"../../wager/Welcome.aspx\"><span id=\"ctl00_Account\">Account</span></a></li>	<li><a href=\"../../wager/SportsAllRules.aspx\"><span id=\"ctl00_Rules\">Rules</span></a></li>	<li><a href=\"../../wager/OpenBets.aspx\"><span id=\"ctl00_OpenBets\">Open Bets</span></a></li>	<li><a href=\"../../wager/History.aspx\"><span id=\"ctl00_History\">History</span></a></li> <li><a href=\"../../wager/SportsSchedules.aspx\"><span id=\"ctl00_Schedules\">Schedules</span></a></li>	<li><a href=\"../../wager/Matchups.aspx\"><span id=\"ctl00_Matchups\">Matchups</span></a></li>	<li><a href=\"../../wager/TimeChanges.aspx\"><span id=\"ctl00_TimeChanges\">Time Changes</span></a></li>	<li><a href=\"../../wager/Scores.aspx\"><span id=\"ctl00_Scores\">Scores</span></a></li>	<li><a href=\"../../wager/TVListings.aspx\"><span id=\"ctl00_TVSchedules\">TV Schedules</span></a></li>	<li><a href=\"../../wager/Injuries.aspx\"><span id=\"ctl00_Injuries\">Injuries</span></a></li>	</ul>	<ul class=\"dd_secondmenu\">	<li><a href=\"../../wager/CreateSports.aspx?WT=0\"><span id=\"ctl00_Straight\">Straight</span></a></li>	<li><a href=\"../../wager/CreateSports.aspx?WT=1\"><span id=\"ctl00_Parlay\">Parlay</span></a></li>	<li><a href=\"../../wager/ChooseTeaser.aspx?WT=2\"><span id=\"ctl00_Teaser\">Teaser</span></a></li>	<li><a href=\"../../wager/CreateSports.aspx?WT=3\"><span id=\"ctl00_IfWinOnly\">If Win Only</span></a></li>	<li><a href=\"../../wager/CreateSports.aspx?WT=7\"><span id=\"ctl00_IfwinOrTie\">If Win or Tie</span></a></li>	<li><a href=\"../../wager/CreateSports.aspx?WT=8\"><span id=\"ctl00_ActionReverse\">Action Reverse</span></a></li>	<li><a href=\"../../wager/CreateSports.aspx?WT=4\"><span id=\"ctl00_WinReverse\">Win Reverse</span></a></li>	<li><a href=\"../../wager/CreateSports.aspx?WT=5\"><span id=\"ctl00_RoundRobin\">Round Robin</span></a></li>	<li><a href=\"../../wager/FillChoose.aspx\"><span id=\"ctl00_FillOpen\">Fill Open</span></a></li>	</ul>	</div>	<div class=\"dd_wrapper\">	<div class=\"bContainer\" btype=\"1\" style=\"position: relative; width: 920px; margin:0 auto\">	<ul class=\"slider\">	<li></li>	</ul>	</div>	<div class=\"dd_content\" ><TABLE class=\"dd_tableSpacing\"><TR><TD ALIGN=\"center\"><h3><span>The line changed for one (or more) of your selections.</span></h3><br><TABLE><TR class=\"dd_title2\"><TD WIDTH=\"80\"><B><span>WagerType:</span></B></TD><TD><B><span>Date:</span></B></TD><TD><B><span>Current Line:</span></B></TD><TD><B><span>Previous Line:</span></B></TD><TD><B><span>Risking</span> / <span>To Win</span></B></TD></TR><TR class=\"dd_rowDark\"><TD>STRAIGHT BET</TD><TD>Sep 02</TD><TD>MLB [1905] 1H CIN REDS <font class=\"LineChange\">+122</font> [A. DESCLAFANI -R/L. WEAVER -R] </TD><TD>+126</TD><TD>500.00 USD / 610.00 USD</TD></TR></TABLE><TABLE BORDER=\"0\" WIDTH=\"100%\" ALIGN=\"center\"><TR><TD ALIGN=\"center\"><TABLE ALIGN=\"center\" class=\"WagerTable2\"><TR><TD><span>Please enter your password and click the button to confirm your wager(s).</span></TD></TR><TR><TD ALIGN=\"CENTER\"><script type=\"text/javascript\"> function UpperPass(e){ var txt = document.getElementById('password').value; document.getElementById('password').value = txt.toUpperCase(); } </script><INPUT TYPE=\"Password\" NAME=\"password\" ID=\"password\" SIZE=\"25\" onkeyup=\"javascript:UpperPass(event);\"><INPUT type=\"hidden\" NAME=\"RMV_0\" ID=\"RMV_0\" value=\"\"></TD></TR><TR><TD ALIGN=\"CENTER\"><input type=\"submit\" name=\"ctl00$WagerContent$btn_Continue1\" value=\"Accept Changes\" id=\"ctl00_WagerContent_btn_Continue1\" /><input type=\"submit\" name=\"ctl00$WagerContent$can_1\" value=\"Cancel Wager\" id=\"ctl00_WagerContent_can_1\" /></TD></TR></TABLE><BR></TD></TR></TABLE></TD></TR></TABLE></div><SCRIPT language=\"JavaScript\" > var wamt; wamt = document.getElementById(\"password\"); if (wamt != null) wamt.focus(); </SCRIPT> </div>	</form>	</body>	</html>";
				final Map<String, String> map = tsp.processLineChange(xhtml);
				LOGGER.error("map: " + map);
			} catch (Throwable pee) {
				LOGGER.error(pee.getMessage(), pee);
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	/**
	 * 
	 * @param xhtml
	 * @param accountName
	 * @param accountId
	 * @return
	 * @throws BatchException
	 */
	public Set<PendingEvent> parsePendingBets(String xhtml, String accountName, String accountId) throws BatchException {
		LOGGER.info("Entering parsePendingBets()");
		LOGGER.debug("accountName: " + accountName);
		LOGGER.debug("accountId: " + accountId);
		Set<PendingEvent> pendingEvents = new HashSet<PendingEvent>();
		
		if (xhtml != null) {
			xhtml = xhtml.replace("Â", "");
		}

		// Get the document object
		final Document doc = parseXhtml(xhtml);
		Elements trs = doc.select("center table tbody tr");
		if (trs != null && trs.size() > 0) {
			// Do nothing
		} else {
			trs = doc.select(".content_container table tbody tr");
			if (trs != null && trs.size() > 0) {
				// use this one
			} else {
				trs = doc.select("#CreateWagerTable tr");
				if (trs != null && trs.size() > 0) {
					
				} else {
					trs = doc.select("#CreateWagerTable table tr");
					
					if (trs != null && trs.size() > 0) {
						
					} else {
						trs = doc.select("#GenericContent table tr");
						if (trs != null && trs.size() > 0) {
							
						} else {
							trs = doc.select("#content div table tr");
						}
					}
				}
			}
		}

		LOGGER.debug("trs: " + trs);
		if (trs != null) {
			for (Element tr : trs) {
				PendingEvent pe = null;
				int x = 0;
				final String className = tr.attr("class");
				if (className != null && className.contains("TrGame")) {
					try {
						final Elements tds = tr.select("td");
						pe = new PendingEvent();
						pe.setDatecreated(new Date());
						pe.setDatemodified(new Date());
						pe.setCustomerid(accountName);
						pe.setInet(accountName);
						pe.setAccountname(accountName);
						pe.setAccountid(accountId);
						for (Element td : tds) {
							LOGGER.debug("td: " + td);
							LOGGER.debug("tds.size(): " + tds.size());
							if (tds.size() == 6) {
								switch (x++) {
									case 0:
										getTicketNumberAndDate(td, pe);
										break;
									case 1:
										// do nothing
										break;
									case 2:
										pe.setDateaccepted(td.html().trim());
										break;
									case 3:
										getEventSportAndType(td, pe);
										break;
									case 4:
									{
										Pattern p = Pattern.compile("<br>");
										Matcher m = p.matcher(td.html());
										int i = 0;
										while (m.find()) {
										    i++;
										}
										if (i == 3) {
											getGameInfo(td, pe);	
										} else {
											getGameInfo2(td, pe);
										}
										break;
									}
									case 5:
										getRiskWin(td, pe);
										break;
									default:
										break;
								}
							} else if (tds.size() == 7) {
								switch (x++) {
									case 0:
										getTicketNumberAndDate(td, pe);
										break;
									case 1:
										// do nothing
										break;
									case 2:
										pe.setDateaccepted(td.html().trim());
										break;
									case 3:
										getEventSportAndType(td, pe);
										break;
									case 4:
									{
										Pattern p = Pattern.compile("<br>");
										Matcher m = p.matcher(td.html());
										int i = 0;
										while (m.find()) {
										    i++;
										}
										if (i == 3) {
											getGameInfo(td, pe);	
										} else {
											getGameInfo2(td, pe);
										}
										break;
									}
									case 5:
										String thtml = td.html();
										if (thtml != null) {
											thtml = thtml.replace("<br>", "").trim();
											pe.setRisk(thtml);
										}
										break;
									case 6:
										String whtml = td.html();
										if (whtml != null) {
											whtml = whtml.replace("<br>", "").trim();
											pe.setWin(whtml);
										}
										break;
									default:
										break;
								}							
							}
						}
						pe.setDoposturl(false);

/*
						// Quick check if WNBA
						if ("NBA".equals(pe.getGametype())) {
							String peTeam = pe.getTeam();
							if (peTeam != null) {
								for (String wnbaTeam : WNBA_TEAMS) {
									if (peTeam.contains(wnbaTeam)) {
										pe.setGametype("WNBA");
										break;
									}
								}
							}
						}
*/
						pendingEvents.add(pe);
					} catch (BatchException be) {
						if (pe != null) {
							pe.setGamesport("");
							pe.setGametype("");
						}
					} catch (Throwable t) {
						LOGGER.error(t.getMessage(), t);
						if (pe != null) {
							pe.setGamesport("");
							pe.setGametype("");
						}
						// Get the email access token so we can update the users
						String accessToken = "";
						try {
							String clientid = "529498092660-bcn5kjtr7iqr1k7fasv28ld16hgdi8p0.apps.googleusercontent.com";
							String clientsecret = "o4VwTH0ykC3qjyeMlI7FdlaM";						
							String refreshtoken = "1/CA1PWWUpXS6Y4N0jEGtK6KnR5fCCSD4mvg2gO-ERKC4";
							String granttype = "refresh_token";
							final AccessTokenFromRefreshToken accessTokenFromRefreshToken = new AccessTokenFromRefreshToken(clientid, clientsecret, refreshtoken, granttype);
							accessToken = accessTokenFromRefreshToken.getAccessToken();
							final SendText sendText = new SendText();
							sendText.setOAUTH2_TOKEN(accessToken);
							sendText.sendTextWithMessage("9132195234@vtext.com", "Error in pendingSite " + accountName + " " + accountId + " " + tr.html());
						} catch (Throwable tt) {
							LOGGER.error(tt.getMessage(), tt);
						}
					}
				}
			}	
		}

		LOGGER.info("Exiting parsePendingBets()");
		return pendingEvents;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseIndex(java.lang.String)
	 */
	@Override
	public Map<String, String> parseIndex(String xhtml) throws BatchException {
		LOGGER.info("Entering parseIndex()");
		final Map<String, String> map = new HashMap<String, String>();

		// Get the document object
		final Document doc = parseXhtml(xhtml);

		// Get hidden input fields
		final String[] types = new String[] { "hidden", "text", "password", "submit" };
		final Elements forms = doc.select("form");
		if (forms != null && forms.size() > 0) {
			for (Element form : forms) {
				// Get form action field
				map.put("action", form.attr("action"));
				getAllElementsByType(form, "input", types, map);
			}
		}

		// Check if we need to send upper case password
		if (!xhtml.contains("toLowerCase") && xhtml.contains("toUpperCase")) {
			map.put("toUpperCase", "true");
		}

		LOGGER.info("Exiting parseIndex()");
		return map;
	}

	/**
	 * 
	 * @param xhtml
	 * @return
	 */
	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseLogin(java.lang.String)
	 */
	@Override
	public Map<String, String> parseLogin(String xhtml) throws BatchException {
		LOGGER.info("Entering parseLogin()");
		Map<String, String> map = new HashMap<String, String>();

		// Parse the xhtml
		final Document doc = parseXhtml(xhtml);

		// Get the form information
		final Elements forms = doc.select("form");
		for (int x = 0; (forms != null && x < forms.size()); x++) {
			final Element form = forms.get(x);
			if (form != null) {
				// Get form action field
				map.put("action", form.attr("action"));
				map = getAllElementsByNameByElement(form, "input", "value", map);
			}
		}

		LOGGER.info("Exiting parseLogin()");
		return map;
	}

	/**
	 * 
	 * @param xhtml
	 * @param type
	 * @param sport
	 * @return
	 * @throws BatchException
	 */
	@Override
	public Map<String, String> parseMenu(String xhtml, String[] type, String[] sport) throws BatchException {
		LOGGER.info("Entering parseMenu()");
		LOGGER.info("type: " + java.util.Arrays.toString(type));
		LOGGER.info("sport: " + java.util.Arrays.toString(sport));
		Map<String, String> map = new HashMap<String, String>();

		if (sport != null && sport.length > 0) {
			for (String sportName : sport) {
				if (sportName.contains("MLB") || sportName.contains("mlb") || sportName.contains("BASEBALL")) {
					isMlb = true;
				}
			}
		}
		LOGGER.debug("isMlb: " + isMlb);

		// Parse to get the Document
		final Document doc = parseXhtml(xhtml);

		final String[] types = new String[] { "hidden", "submit" };
		final Elements forms = doc.select("form");
		for (int x = 0; (forms != null) && (x < forms.size()); x++) {
			final Element form = forms.get(0);
			if (form != null) {
				// Get form action field
				map.put("action", form.attr("action"));

				getAllElementsByType(form, "input", types, map);
			}			
		}
		
		if (xhtml.contains("Your bet expired or has already been submited.")) {
			throw new BatchException(BatchErrorCodes.WAGER_HAS_EXPIRED, BatchErrorMessage.WAGER_HAS_EXPIRED, "The wager has expired or already been bet", xhtml);	
		}

		// Find the different menu types
		map = findMenu(doc.select(".leaguesblock div div"), map, type, sport, "a", "input");
		map = findMenu(doc.select(".TableLeagues tbody tr td div"), map, type, sport, "span", "input");
		map = findMenu(doc.select("#TableLeague div div div"), map, type, sport, "span", "input");
		map = findMenu(doc.select("center table tbody tr td table tbody tr"), map, type, sport, "td span strong", "td input");
		map = findMenu(doc.select("#content center div div"), map, type, sport, "div b", "div input");
		map = findMenu(doc.select("center table tbody tr td center table tbody tr td table tbody tr"), map, type, sport, "td span b", "td input");
		map = findMenu2(doc.select("form div div table tbody tr td table tbody tr"), map, type, sport, "td a", "td input", 0);
		map = findMenu2(doc.select(".dd_content table tbody tr td table tbody tr"), map, type, sport, "td a", "td input", 0); // Betcatalinasports
		map = findMenu(doc.select("#content center div div div div"), map, type, sport, "div b", "div input"); // wager47

		// Remove the input fields that shouldn't be sent
		map = removeInputFields(map);

		LOGGER.info("Exiting parseMenu()");
		return map;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseGames(java.lang.String, java.lang.String, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <TDSportsEventPackage> List<TDSportsEventPackage> parseGames(String xhtml, String type, Map<String, String> inputFields) throws BatchException {
		LOGGER.info("Entering parseGames()");
		LOGGER.debug("xhtml: " + xhtml);
		LOGGER.debug("type: " + type);
		LOGGER.debug("InputFields: " + inputFields);
		List<?> events = null;
		
		// Parse to get the Document
		final Document doc = parseXhtml(xhtml);
		Map<String, String> tempFields = new HashMap<String, String>();

		final String[] types = new String[] { "hidden", "submit" };
		final Elements forms = doc.select("form");
		for (int x = 0; (forms != null) && (x < forms.size()); x++) {
			final Element form = forms.get(0);
			if (form != null) {
				// Get form action field
				tempFields.put("action", form.attr("action"));

				// Get all input elements with hidden and submit types
				getAllElementsByType(form, "input", types, tempFields);
				
				Elements elements = form.select("input");
				for (int y = 0; (elements != null) && (y < elements.size()); y++) {
					final Element input = elements.get(y);
					String nameValue = input.attr("name");
					String typeValue = input.attr("type");
					if (nameValue != null && typeValue != null && "checkbox".equals(typeValue)) {
						nameValue = nameValue.trim();
						if (nameValue.contains("linesel_")) {
							tempFields.put(nameValue, "on");
						}
					}
				}
			}
		}

		if (xhtml.contains("Your bet expired or has already been submited.")) {
			throw new BatchException(BatchErrorCodes.WAGER_HAS_EXPIRED, BatchErrorMessage.WAGER_HAS_EXPIRED, "The wager has expired or already been bet", xhtml);	
		}

		// abcgrand.ag
		Elements elements = doc.select("#OddsTables div table tbody tr");
		if ((events == null || events.size() == 0) && elements != null && elements.size() > 0) {
			events = getGameData(elements);
		}

		// bigmansports.ag
		elements = doc.select("#games_supercontainer table tbody tr");
		if ((events == null || events.size() == 0) && elements != null && elements.size() > 0) {
			events = getGameData(elements);
		}

		// Betwestcoast.com
		elements = doc.select("center table tbody tr td center table tbody tr");
		if ((events == null || events.size() == 0) && elements != null && elements.size() > 0) {
			events = getGameData(elements);
		}

		elements = doc.select(".tformat table tbody tr");
		if ((events == null || events.size() == 0) && elements != null && elements.size() > 0) {
			events = getGameData(elements);
		}

		elements = doc.select(".line_table tbody tr");
		if ((events == null || events.size() == 0) && elements != null && elements.size() > 0) {
			events = getGameData(elements);
		}

		// www.1betvegas.com and www.spot47.com
		elements = doc.select("form div div table tbody tr");
		if ((events == null || events.size() == 0) && elements != null && elements.size() > 0) {
			events = getGameData(elements);
		}

		// betcatalinasports and names777
		elements = doc.select(".dd_content table tr");
		if ((events == null || events.size() == 0) && elements != null && elements.size() > 0) {
			events = getGameData(elements);
		}

		// wager47
		elements = doc.select("#content center div div div");
		if ((events == null || events.size() == 0) && elements != null && elements.size() > 0) {
			events = getGameData(elements);
		}

		// Remove the input fields that shouldn't be sent
		tempFields = removeInputFields(tempFields);
		if (tempFields != null && !tempFields.isEmpty()) {
			final Set<Entry<String, String>> indexs = tempFields.entrySet();
			if (indexs != null && !indexs.isEmpty()) {
				final Iterator<Entry<String, String>> itr = indexs.iterator();
				while (itr != null && itr.hasNext()) {
					final Entry<String, String> values = itr.next();
					String key = values.getKey();
					String value = values.getValue();
					LOGGER.info("KEY: " + key);
					LOGGER.info("VALUE: " + value);

					if (key != null && value != null && value.length() > 0) {
						inputFields.put(key, value);
					}
				}
			}
		}

		LOGGER.info("Exiting parseGames()");
		return (List<TDSportsEventPackage>)events;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseEventSelection(java.lang.String, com.ticketadvantage.services.dao.sites.SiteTeamPackage, java.lang.String)
	 */
	@Override
	public Map<String, String> parseEventSelection(String xhtml, SiteTeamPackage siteTeamPackage, String type) throws BatchException {
		LOGGER.info("Entering parseEventSelection()");
		Map<String, String> map = new HashMap<String, String>();
		
		// Parse the HTML first
		final Document doc = parseXhtml(xhtml);

		final String[] types = new String[] { "hidden", "checkbox", "text", "submit" };
		Element form = doc.getElementById("aspnetForm");
		if (form != null) {
			// Get form action field
			map.put("action", form.attr("action"));
			getAllElementsByTypeWithCheckbox(form, "input", types, map);
		}

		// Now get the select options if there are any
		if (type.equals("spread")) {
			setupSpreadWager(doc, (TDSportsTeamPackage) siteTeamPackage);
		} else if (type.equals("total")) {
			setupTotalWager(doc, (TDSportsTeamPackage) siteTeamPackage);
		}

		// Check for a wager limit and change it 
		if (xhtml.contains("Min Wager Online not reached. Your Current Wager Limit is ")) {
			int index = xhtml.indexOf("Min Wager Online not reached. Your Current Wager Limit is ");
			if (index != -1) {
				xhtml = xhtml.substring(index + "Min Wager Online not reached. Your Current Wager Limit is ".length());
				index = xhtml.indexOf(" USD.");
				if (index != -1) {
					String wagerAmount = xhtml.substring(0, index);
					if (wagerAmount != null) {
						wagerAmount = wagerAmount.replaceAll(",", "");
						wagerAmount = wagerAmount.replaceAll(" ", "");
						map.put("wagerminamount", wagerAmount);
					}
				}
			}
		}

		// Check for a wager limit and change it  
		if (xhtml.contains("Exceeded. Limit For This Game is ")) {
			int index = xhtml.indexOf("Exceeded. Limit For This Game is ");
			if (index != -1) {
				xhtml = xhtml.substring(index + "Exceeded. Limit For This Game is ".length());
				index = xhtml.indexOf(" USD");
				if (index != -1) {
					String wagerAmount = xhtml.substring(0, index);
					if (wagerAmount != null) {
						wagerAmount = wagerAmount.replaceAll(",", "");
						wagerAmount = wagerAmount.replaceAll(" ", "");
						map.put("wageramount", wagerAmount);
						if (xhtml.contains("Go Back")) {
							map.put("goback", "true");
						}
					}
				}
			}
		}

		// Check for account balance limit
		if (xhtml.contains("be accepted because Balance Exceeded")) {
			map.put("wagerbalanceexceeded", "true");	
		}

		if (xhtml.contains("Your bet expired or has already been submited.")) {
			throw new BatchException(BatchErrorCodes.WAGER_HAS_EXPIRED, BatchErrorMessage.WAGER_HAS_EXPIRED, "The wager has expired or already been bet", xhtml);	
		}

		if (xhtml.contains("The line changed for one (or more) of your selections.")) {
			// Line changed throw exception
			throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed and cannot complete transaction", xhtml);	
		}

		// Remove the input fields that shouldn't be sent
		map = removeInputFields(map);

		LOGGER.info("Exiting parseEventSelection()");
		return map;
	}

	/**
	 * 
	 * @param xhtml
	 * @return
	 * @throws BatchException
	 */
	public Map<String, String> parseConfirmWager(String xhtml) throws BatchException {
		LOGGER.info("Entering parseConfirmWager()");
		Map<String, String> map = new HashMap<String, String>();
		
		// Parse the HTML first
		final Document doc = parseXhtml(xhtml);

		final String[] types = new String[] { "hidden", "submit", "password", "Password" };
		Element form = doc.getElementById("aspnetForm");
		if (form != null) {
			// Get form action field
			map.put("action", form.attr("action"));
			getAllElementsByType(form, "input", types, map);
		}

		// Check for a wager limit and change it 
		if (xhtml.contains("Max Wager Online Exceeded. Your Current Wager Limit is ")) {
			int index = xhtml.indexOf("Max Wager Online Exceeded. Your Current Wager Limit is ");
			if (index != -1) {
				xhtml = xhtml.substring(index + "Max Wager Online Exceeded. Your Current Wager Limit is ".length());
				index = xhtml.indexOf(" USD.");
				if (index != -1) {
					String wagerAmount = xhtml.substring(0, index);
					if (wagerAmount != null) {
						wagerAmount = wagerAmount.replaceAll(",", "");
						wagerAmount = wagerAmount.replaceAll(" ", "");
						map.put("wageramount", wagerAmount);
						if (xhtml.contains("Go Back")) {
							map.put("goback", "true");
						}
					}
				}
			}
		}

		// Check for a wager limit and change it 
		if (xhtml.contains("Min Wager Online not reached. Your Current Wager Limit is ")) {
			int index = xhtml.indexOf("Min Wager Online not reached. Your Current Wager Limit is ");
			if (index != -1) {
				xhtml = xhtml.substring(index + "Min Wager Online not reached. Your Current Wager Limit is ".length());
				index = xhtml.indexOf(" USD.");
				if (index != -1) {
					String wagerAmount = xhtml.substring(0, index);
					if (wagerAmount != null) {
						wagerAmount = wagerAmount.replaceAll(",", "");
						wagerAmount = wagerAmount.replaceAll(" ", "");
						map.put("wagerminamount", wagerAmount);
					}
				}
			}
		}

		// Check for a wager limit and change it  
		if (xhtml.contains("Exceeded. Limit For This Game is ")) {
			int index = xhtml.indexOf("Exceeded. Limit For This Game is ");
			if (index != -1) {
				xhtml = xhtml.substring(index + "Exceeded. Limit For This Game is ".length());
				index = xhtml.indexOf(" USD");
				if (index != -1) {
					String wagerAmount = xhtml.substring(0, index);
					if (wagerAmount != null) {
						wagerAmount = wagerAmount.replaceAll(",", "");
						wagerAmount = wagerAmount.replaceAll(" ", "");
						map.put("wageramount", wagerAmount);
						if (xhtml.contains("Go Back")) {
							map.put("goback", "true");
						}
					}
				}
			}
		}

		// Check for account balance limit
		if (xhtml.contains("be accepted because Balance Exceeded")) {
			LOGGER.debug("ACCOUNT BALANCE EXCEEDED");
			map.put("wagerbalanceexceeded", "true");	
		}
		
		
		if (xhtml.contains("Your bet expired or has already been")) {
			throw new BatchException(BatchErrorCodes.WAGER_HAS_EXPIRED, BatchErrorMessage.WAGER_HAS_EXPIRED, "The wager has expired or already been bet", xhtml);	
		}

		if (xhtml.contains("The line changed for one (or more) of your selections.")) {
			// Line changed throw exception
			throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed and cannot complete transaction", xhtml);	
		}

		// Check if we need to send upper case password
		if (xhtml.contains("toUpperCase")) {
			map.put("toUpperCase", "true");
		}

		// Remove the input fields that shouldn't be sent
		map = removeInputFields(map);

		LOGGER.info("Exiting parseConfirmWager()");
		return map;		
	}

	/**
	 * 
	 * @param xhtml
	 * @return
	 * @throws BatchException
	 */
	public Map<String, String> processLineChange(String xhtml) throws BatchException {
		LOGGER.info("Entering processLineChange()");
		Map<String, String> map = new HashMap<String, String>();
		
		// Parse the HTML first
		final Document doc = parseXhtml(xhtml);

		final String[] types = new String[] { "hidden", "submit", "password", "Password" };
		final Element form = doc.getElementById("aspnetForm");
		if (form != null) {
			// Get form action field
			map.put("action", form.attr("action"));
			getAllElementsByType(form, "input", types, map);
		}

		// Parse out the changes
		final Elements elements = doc.select(".WagerTable tr");
		int x = 0;
		for (Element element : elements) {
			if (x++ == 1) {
                // <td>STRAIGHT BET</td>
                // <td>Aug 14</td>
                // <td>MLB [955] TOTAL (WAS NATIONALS vrs STL CARDINALS) <font class="LineChange">o9-110</font> [G GONZALEZ -L/J GANT -R]</td>
                // <td>o9-105</td>
                // <td>550.00 USD / 500.00 USD</td>

				final Elements tds = element.select("td");
				int y = 0;
				for (Element td : tds) {
					switch (y++) {
						case 2:
							final Elements fonts = td.select("font");
							if (fonts != null && fonts.size() > 0) {
								final Element font = fonts.get(0);
								String newLine = font.html().toLowerCase();
								newLine = super.reformatValues(newLine);
								LOGGER.debug("newLine: " + newLine);

								if (newLine.startsWith("o") || newLine.startsWith("u")) {
									parseTotal(newLine, "newvalindicator", "newval", "newjuiceindicator", "newjuice", map);
								} else {
									// Check for money first
									if ((newLine.startsWith("ev") || newLine.startsWith("pk")) && (newLine.length() == 2)) {
										// We have moneyline
										map.put("newvalindicator", "+");
										map.put("newval", "100");
										map.put("newjuiceindicator", "+");
										map.put("newjuice", "100");
									} else if ((newLine.startsWith("+") || newLine.startsWith("-")) && (newLine.length() == 4) && (!newLine.endsWith("ev") && !newLine.endsWith("pk"))) {
										// We have moneyline
										map.put("newvalindicator", newLine.substring(0, 1));
										map.put("newval", newLine.substring(1));
										map.put("newjuiceindicator", newLine.substring(0, 1));
										map.put("newjuice", newLine.substring(1));
									} else {
										parseSpread(newLine, "newvalindicator", "newval", "newjuiceindicator", "newjuice", map);
									}
								}
							}
							break;
						case 0:
						case 1:
						case 3:
						case 4:
							break;
					}
				}
			}
		}

		LOGGER.info("Exiting processLineChange()");
		return map;
	}

	/**
	 * 
	 * @param newLine
	 * @param valindicatorname
	 * @param valname
	 * @param juiceindicatorname
	 * @param juicename
	 * @param map
	 */
	private void parseTotal(String newLine, String valindicatorname, String valname, String juiceindicatorname, String juicename, Map<String, String> map) {
		LOGGER.info("Entering parseTotal()");

		int minusindex = newLine.indexOf("-");
		if (minusindex != -1) {
			final Map<String, String> vals = parseTotal(newLine.substring(0 , minusindex), -1);
			if (vals != null && !vals.isEmpty()) {
				map.put(valindicatorname, vals.get("valindicator"));
				map.put(valname, vals.get("val"));
			}

			newLine = newLine.substring(minusindex);
			LOGGER.debug("newLine: " + newLine);
			final Map<String, String> juices = parseJuice(newLine, null, null);
			if (juices != null && !juices.isEmpty()) {
				map.put(juiceindicatorname, juices.get("juiceindicator"));
				map.put(juicename, juices.get("juice"));
			}
		} else {
			int plusindex = newLine.indexOf("+");
			if (plusindex != -1) {
				final Map<String, String> vals = parseTotal(newLine.substring(0 , plusindex), -1);
				if (vals != null && !vals.isEmpty()) {
					map.put(valindicatorname, vals.get("valindicator"));
					map.put(valname, vals.get("val"));
				}

				newLine = newLine.substring(minusindex);
				LOGGER.debug("newLine: " + newLine);
				final Map<String, String> juices = parseJuice(newLine, null, null);
				if (juices != null && !juices.isEmpty()) {
					map.put(juiceindicatorname, juices.get("juiceindicator"));
					map.put(juicename, juices.get("juice"));
				}
			} else {
				newLine = newLine.toLowerCase();
				int evindex = newLine.indexOf("ev");
				if (evindex != -1) {
					final Map<String, String> vals = parseTotal(newLine.substring(0 , evindex), -1);
					if (vals != null && !vals.isEmpty()) {
						map.put(valindicatorname, vals.get("valindicator"));
						map.put(valname, vals.get("val"));
					}

					map.put(juiceindicatorname, "+");
					map.put(juicename, "100");
				}
			}
		}

		LOGGER.info("Exiting parseTotal()");
	}

	/**
	 * 
	 * @param newLine
	 * @param valindicatorname
	 * @param valname
	 * @param juiceindicatorname
	 * @param juicename
	 * @param map
	 */
	private void parseSpread(String newLine, String valindicatorname, String valname, String juiceindicatorname, String juicename, Map<String, String> map) {
		LOGGER.info("Entering parseSpread()");

		// Spread
		if (newLine.startsWith("ev") || newLine.startsWith("pk")) {
			map.put(valindicatorname, "+");
			map.put(valname, "100");

			// Now get the Juice
			newLine = newLine.substring(2);
			if (newLine.startsWith("ev") || newLine.startsWith("pk")) {
				map.put(juiceindicatorname, "+");
				map.put(juicename, "100");
			} else if (newLine.startsWith("+") || newLine.startsWith("-")) {
				map.put(juiceindicatorname, newLine.substring(0, 1));
				map.put(juicename, newLine.substring(1));
			}
		} else {
			map.put(valindicatorname, newLine.substring(0, 1));
			newLine = newLine.substring(1);

			int eindex = newLine.lastIndexOf("EV");
			int pindex = newLine.lastIndexOf("PK");
			int plindex = newLine.lastIndexOf("+");
			int mindex = newLine.lastIndexOf("-");
			LOGGER.debug("eindex: " + eindex);
			LOGGER.debug("pindex: " + pindex);
			LOGGER.debug("plindex: " + plindex);
			LOGGER.debug("mindex: " + mindex);

			// Now get the line value and juice
			if (eindex != -1) {
				map.put(valname, newLine.substring(0, eindex));
				map.put(juiceindicatorname, "+");
				map.put(juicename, "100");
			} else if (pindex != -1) {
				map.put(valname, newLine.substring(0, pindex));
				map.put(juiceindicatorname, "+");
				map.put(juicename, "100");
			} else if (plindex != -1) {
				map.put(valname, newLine.substring(0, pindex));
				map.put(juiceindicatorname, "+");
				map.put(juicename, newLine.substring(plindex + 1));
			} else if (mindex != -1) {
				map.put(valname, newLine.substring(0, mindex));
				map.put(juiceindicatorname, "-");
				map.put(juicename, newLine.substring(mindex + 1));					
			}
		}

		LOGGER.info("Exiting parseSpread()");
	}

	/**
	 * 
	 * @param xhtml
	 * @return
	 * @throws BatchException
	 */
	public Map<String, String> parseWagerTypes(String xhtml) throws BatchException {
		LOGGER.info("Entering parseWagerTypes()");
		LOGGER.debug("xhtml: " + xhtml);
		final Map<String, String> wagers = new HashMap<String, String>();

		// Parse to get the Document
		final Document doc = parseXhtml(xhtml);

		
		if (xhtml.contains("Your bet expired or has already been")) {
			throw new BatchException(BatchErrorCodes.WAGER_HAS_EXPIRED, BatchErrorMessage.WAGER_HAS_EXPIRED, "The wager has expired or already been bet", xhtml);	
		}

		if (xhtml.contains("The line changed for one (or more) of your selections.")) {
			// Line changed throw exception
			throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed and cannot complete transaction", xhtml);	
		}

		// First get the Risk
		String risk = getHtmlFromAllElements(doc, ".offering_pair td a font b");
		if (risk != null && risk.length() > 0) {
			risk = risk.substring(1);
			wagers.put("risk", risk);
		} else {
			// Throw Exception
			throw new BatchException(BatchErrorCodes.ERROR_PARSING_WAGER_INFO, BatchErrorMessage.ERROR_PARSING_WAGER_INFO, "No element found for " + xhtml);
		}

		// Now get the Win
		String win = getHtmlFromAllElements(doc, ".offering_pair td a font b");
		if (win != null && win.length() > 0) {
			win = win.substring(1);
			wagers.put("win", win);
		} else {
			// Throw Exception
			throw new BatchException(BatchErrorCodes.ERROR_PARSING_WAGER_INFO, BatchErrorMessage.ERROR_PARSING_WAGER_INFO, "No element found for " + xhtml);
		}

		LOGGER.info("Exiting parseWagerTypes()");
		return wagers;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseTicketNumber(java.lang.String)
	 */
	@Override
	public String parseTicketNumber(String xhtml) throws BatchException {
		LOGGER.info("Entering parseTicketNumber()");
		String ticketNumber = "Ticket Number - ";
		final String ticketInfo = "\"ticket\">";

		if (xhtml.contains("Your bet expired or has already been submited.")) {
			throw new BatchException(BatchErrorCodes.WAGER_HAS_EXPIRED, BatchErrorMessage.WAGER_HAS_EXPIRED, "The wager has expired or already been bet", xhtml);	
		}
		
		if (xhtml.contains("The line changed for one (or more) of your selections.")) {
			// Line changed throw exception
			throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed and cannot complete transaction", xhtml);	
		}

		// First check for Wager has been accepted!
		if (xhtml != null && xhtml.contains(ticketInfo)) {
			// Great, wager is complete; now get the Ticket Number
			int index = xhtml.indexOf(ticketInfo);
			if (index != -1) {
				final String nxhtml = xhtml.substring(index + ticketInfo.length());
				index = nxhtml.indexOf("</td>");
				if (index != -1) {
					ticketNumber = nxhtml.substring(0, index);
				} else {
					ticketNumber = "Failed to get ticket number";
					throw new BatchException(BatchErrorCodes.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, BatchErrorMessage.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, ticketNumber, xhtml);
				}
			} else {
				ticketNumber = "Failed to get ticket number";
				throw new BatchException(BatchErrorCodes.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, BatchErrorMessage.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, ticketNumber, xhtml);
			}
		} else {
			String spanTicket = "<span>Ticket#:</span>";
			int index = xhtml.indexOf(spanTicket);
			if (index != -1) {
				xhtml = xhtml.substring(index + "<span>Ticket#:</span>".length());
				index = xhtml.indexOf("</TR></TABLE>");
				if (index != -1) {
					xhtml = xhtml.substring(0, index);
					index = xhtml.indexOf("USD</TD>");
					if (index != -1) {
						xhtml = xhtml.substring(index + "USD</TD>".length());
						xhtml = xhtml.replaceAll("<TD>", "");
						xhtml = xhtml.replaceAll("</TD>", "");
						ticketNumber = ticketNumber + xhtml;
					} else {
						ticketNumber = "Failed to get ticket number";
						throw new BatchException(BatchErrorCodes.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, BatchErrorMessage.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, ticketNumber, xhtml);
					}
				} else {
					ticketNumber = "Failed to get ticket number";
					throw new BatchException(BatchErrorCodes.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, BatchErrorMessage.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, ticketNumber, xhtml);
				}
			} else {
				ticketNumber = "Failed to get ticket number";
				throw new BatchException(BatchErrorCodes.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, BatchErrorMessage.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, ticketNumber, xhtml);
			}
		}

		LOGGER.info("Exiting parseTicketNumber()");
		return ticketNumber;
	}

	/**
	 * 
	 * @param xhtml
	 * @return
	 */
	public String parseSchedule(String xhtml) {
		LOGGER.info("Entering parseSchedule()");
		LOGGER.debug("xhtml: " + xhtml);

		//window.location= 'http://wager.abcgrand.ag/wager/CreateWager.aspx?WT=0&lg=32&sel=1_2292840_-3.5_-110'}
		String newUrl = "";
		int index = xhtml.indexOf("window.location=");
		if (index != -1) {
			xhtml = xhtml.substring(index + "window.location=".length());
			index = xhtml.indexOf("'");
			if (index != -1) {
				int endIndex = xhtml.indexOf("'}");
				if (endIndex != -1) {
					newUrl = xhtml.substring(index + 1, endIndex);
				}
			}
		} else {
			index = xhtml.indexOf("Location: ");
			if (index != -1) {
				newUrl = xhtml.substring(index + "Location: ".length() + 1);
			}
		}

		LOGGER.info("Exiting parseSchedule()");
		return newUrl;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#getGameData(org.jsoup.select.Elements)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected List<TDSportsEventPackage> getGameData(Elements elements) throws BatchException {
		LOGGER.info("Entering getGameData()");

		TDSportsEventPackage eventPackage = null;
		final List<TDSportsEventPackage> events = new ArrayList<TDSportsEventPackage>();
		if (elements != null) {
			TDSportsTeamPackage team1 = null;
			TDSportsTeamPackage team2 = null;
			int t = 0;
			for (int x = 0; x < elements.size(); x++) {
				// Loop through the elements and then check for the dates
				final Element element = elements.get(x);
				LOGGER.debug("Element: " + element);

				if (element != null) {
					String classInfo = element.attr("class");
					LOGGER.debug("ClassInfo: " + classInfo);

					if ((classInfo != null && classInfo.length() > 0) && 
						(("trgameodd".equalsIgnoreCase(classInfo) || "trgameeven".equalsIgnoreCase(classInfo)) ||
						 (classInfo.contains("gamerow")) ||
						 (classInfo.contains("odd") || classInfo.contains("even")))) {
						if (t++ == 0) {
							eventPackage = new TDSportsEventPackage();
							team1 = new TDSportsTeamPackage();
							getTeamData(element.select("td"), team1);
							eventPackage.setId(team1.getId());
						} else {
							team2 = new TDSportsTeamPackage();
							int size = getTeamData(element.select("td"), team2);
							Date eventDate = null;
							if (size == 7 || size == 5) {
								eventDate = setupDate(DATE_FORMAT, team1.gettDate(), team2.gettDate());
							} else {
								// Oct 01<br>7:30 PM
								String dt = team1.gettDate();
								if (dt != null) {
									int index = dt.indexOf("<br>");
									if (index != -1) {
										team1.settDate(dt.substring(0, index));
										team2.settDate(dt.substring(index + "<br>".length()));
									}
								}
								eventDate = setupDate(DATE_FORMAT, team1.gettDate(), team2.gettDate());
							}
							team1.setEventdatetime(eventDate);
							team2.setEventdatetime(eventDate);
							final String dateOfEvent = team1.gettDate() + " " + team2.gettDate();
							team1.settDate(dateOfEvent);
							team2.settDate(dateOfEvent);
							eventPackage.setDateofevent(dateOfEvent);
							eventPackage.setSiteteamone(team1);
							eventPackage.setSiteteamtwo(team2);
							eventPackage.setTeamone(team1);
							eventPackage.setTeamtwo(team2);
							events.add(eventPackage);
							t = 0;
						}
					} else if ((classInfo != null && classInfo.length() > 0) && (!classInfo.contains("GameHeader") && !classInfo.contains("GameBanner"))) {
						Elements tds = element.select("td");
						if (tds != null && tds.size() == 7) {
							if (t++ == 0) {
								eventPackage = new TDSportsEventPackage();
								team1 = new TDSportsTeamPackage();
								getTeamData(element.select("td"), team1);
								eventPackage.setId(team1.getId());
							} else {
								team2 = new TDSportsTeamPackage();
								int size = getTeamData(element.select("td"), team2);
								Date eventDate = null;
								if (size == 7 || size == 5) {
									eventDate = setupDate(DATE_FORMAT, team1.gettDate(), team2.gettDate());
								} else {
									// Oct 01<br>7:30 PM
									String dt = team1.gettDate();
									if (dt != null) {
										int index = dt.indexOf("<br>");
										if (index != -1) {
											team1.settDate(dt.substring(0, index));
											team2.settDate(dt.substring(index + "<br>".length()));
										}
									}
									eventDate = setupDate(DATE_FORMAT, team1.gettDate(), team2.gettDate());
								}
								team1.setEventdatetime(eventDate);
								team2.setEventdatetime(eventDate);
								final String dateOfEvent = team1.gettDate() + " " + team2.gettDate();
								team1.settDate(dateOfEvent);
								team2.settDate(dateOfEvent);
								eventPackage.setDateofevent(dateOfEvent);
								eventPackage.setSiteteamone(team1);
								eventPackage.setSiteteamtwo(team2);
								eventPackage.setTeamone(team1);
								eventPackage.setTeamtwo(team2);
								events.add(eventPackage);
								t = 0;
							}							
						}
					}
				} 
			}
		}
		
		for (TDSportsEventPackage tep : events) {
			LOGGER.debug("TDSportsEventPackage: " + tep);
		}

		LOGGER.info("Exiting getGameData()");
		return events;
	}

	/**
	 * 
	 * @param element
	 * @param team
	 * @return
	 */
	protected int getTeamData(Elements elements, TDSportsTeamPackage team) {
		LOGGER.info("Entering getTeamData()");
		LOGGER.debug("TDSportsTeamPackage: " + team);
		int size = 0;

		if (elements != null && elements.size() == 7) {
			LOGGER.info("elements.size(): " + elements.size());
			size = 7;
			for (int x = 0;(elements != null && x < elements.size()); x++) {
				final Element td = elements.get(x);
	
				switch (x) {
					case 0: // Do nothing
						break;
					case 1:
						team = getDate(td, team);
						break;
					case 2:
						team = getEventId(td, team);
						break;
					case 3:
						team = getTeam(td, team);
						break;
					case 4:
						team = getSpread(td, team);
						break;
					case 5:
						team = getTotal(td, team);
						break;
					case 6:
						team = getMoneyLine(td, team);
						break;
				}
			}
		} else if (elements != null && elements.size() == 9) {
			LOGGER.info("elements.size(): " + elements.size());
			size = 9;
			for (int x = 0; (elements != null && x < elements.size()); x++) {
				final Element td = elements.get(x);

				switch (x) {
				case 0: // Do nothing
				case 4:
				case 5:
				case 9:
					break;
				case 1:
					team = getDate(td, team);
					break;
				case 2:
					team = getEventId(td, team);
					break;
				case 3:
					team = getTeam(td, team);
					break;
				case 6:
					team = getSpread(td, team);
					break;
				case 7:
					team = getTotal(td, team);
					break;
				case 8:
					team = getMoneyLine(td, team);
					break;
				}
			}
		} else if (elements != null && elements.size() == 6) {
			LOGGER.info("elements.size(): " + elements.size());
			size = 6;
			for (int x = 0;(elements != null && x < elements.size()); x++) {
				final Element td = elements.get(x);
	
				switch (x) {
					case 0: // Do nothing
						break;
					case 1:
						team = getEventId(td, team);
						break;
					case 2:
						team = getTeam(td, team);
						break;
					case 3:
						team = getSpread(td, team);
						break;
					case 4:
						team = getTotal(td, team);
						break;
					case 5:
						team = getMoneyLine(td, team);
						break;
				}
			}
		} else if (elements != null && elements.size() == 8) {
			LOGGER.info("elements.size(): " + elements.size());
			size = 8;
			for (int x = 0;(elements != null && x < elements.size()); x++) {
				final Element td = elements.get(x);
	
				switch (x) {
					case 0: // Do nothing
					case 3:
						break;
					case 1:
						team = getDate(td, team);
						break;
					case 2:
						team = getEventId(td, team);
						break;
					case 4:
						team = getTeam(td, team);
						break;
					case 5:
						team = getSpread(td, team);
						break;
					case 6:
						team = getTotal(td, team);
						break;
					case 7:
						team = getMoneyLine(td, team);
						break;
				}
			}
		} else if (elements != null) {
			LOGGER.info("elements.size(): " + elements.size());
		}

		LOGGER.info("Exiting getTeamData()");
		return size;
	}

	/**
	 * 
	 * @param td
	 * @param team
	 * @return
	 */
	protected TDSportsTeamPackage getDate(Element td, TDSportsTeamPackage team) {
		LOGGER.info("Entering getDate()");
		LOGGER.debug("Element: " + td);

		// Date String
		final String date = getHtmlFromElement(td, "font", 0, true);
		team.settDate(date);

		LOGGER.info("Exiting getDate()");
		return team;
	}

	/**
	 * 
	 * @param td
	 * @param team
	 * @return
	 */
	protected TDSportsTeamPackage getEventId(Element td, TDSportsTeamPackage team) {
		LOGGER.info("Entering getEventId()");
		LOGGER.debug("Element: " + td);

		final String eventId = getHtmlFromElement(td, "font", 0, true);
		team.setEventid(eventId);
		team.setId(Integer.parseInt(eventId));

		LOGGER.info("Exiting getEventId()");
		return team;
	}

	/**
	 * 
	 * @param td
	 * @param team
	 * @return
	 */
	protected TDSportsTeamPackage getTeam(Element td, TDSportsTeamPackage team) {
		LOGGER.info("Entering getTeam()");
		LOGGER.debug("Element: " + td);

		// Team String
		String teamName = getHtmlFromElement(td, "font", 0, false);
		if (teamName == null || teamName.length() == 0) {
			teamName = getHtmlFromElement(td, "table tbody tr td", 1, true);
		}
		team.setTeam(teamName);

		LOGGER.info("Exiting getTeam()");
		return team;
	}

	/**
	 * 
	 * @param td
	 * @param team
	 * @return
	 */
	protected TDSportsTeamPackage getSpread(Element td, TDSportsTeamPackage team) {
		LOGGER.info("Entering getSpread()");
		LOGGER.debug("Element: " + td);

		// First get the input field information
		final Map<String, String> hashMap = parseInputField(td.select("input"), 0);
		team.setGameSpreadInputId(hashMap.get("id"));
		team.setGameSpreadInputName(hashMap.get("name"));
		team.setGameSpreadInputValue(hashMap.get("value"));

		String spread = getHtmlFromElement(td, "font", 0, false);
		if (spread == null || spread.length() == 0) {
			spread = getHtmlFromElement(td, "div", 0, true);
			
			// -2½+200; Now parse the data
			spread = parseHtmlBefore(reformatValues(spread), "<input");
		}

		// Setup spread
		team.addGameSpreadOptionValue("0", "0");
		team = (TDSportsTeamPackage)parseSpreadData(reformatValues(spread), 0, " ", null, team);

		LOGGER.info("Exiting getSpread()");
		return team;
	}

	/**
	 * 
	 * @param td
	 * @param team
	 * @return
	 */
	protected TDSportsTeamPackage getTotal(Element td, TDSportsTeamPackage team) {
		LOGGER.info("Entering getTotal()");
		LOGGER.debug("Element: " + td);

		// First get the input field information
		final Map<String, String> hashMap = parseInputField(td.select("input"), 0);
		team.setGameTotalInputId(hashMap.get("id"));
		team.setGameTotalInputName(hashMap.get("name"));
		team.setGameTotalInputValue(hashMap.get("value"));

		// o40½-110; Now parse the data
		String total = getHtmlFromElement(td, "font", 0, false);
		if (total == null || total.length() == 0) {
			total = getHtmlFromElement(td, "div", 0, true);

			// -2½+200; Now parse the data
			total = parseHtmlBefore(reformatValues(total), "<input");
		}

		// Parse Total Data
		team.addGameTotalOptionValue("0", "0");
		team = (TDSportsTeamPackage)parseTotalData(reformatValues(total), 0, " ", null, team);

		LOGGER.info("Exiting getTotal()");
		return team;
	}

	/**
	 * 
	 * @param td
	 * @param team
	 * @return
	 */
	protected TDSportsTeamPackage getMoneyLine(Element td, TDSportsTeamPackage team) {
		LOGGER.info("Entering getMoneyLine()");
		LOGGER.debug("Element: " + td);
		
		// First get the input field information
		final Map<String, String> hashMap = parseInputField(td.select("input"), 0);
		team.setGameMLInputId(hashMap.get("id"));
		team.setGameMLInputName(hashMap.get("name"));
		team.addGameMLInputValue("0", hashMap.get("value"));

		// -110; Now parse the data
		String ml = getHtmlFromElement(td, "font", 0, false);
		if (ml == null || ml.length() == 0) {
			ml = getHtmlFromElement(td, "div", 0, true);
				
			// -2½+200; Now parse the data
			ml = parseHtmlBefore(reformatValues(ml), "<input");				
		}

		// Parse for Money Line
		team = (TDSportsTeamPackage)parseMlData(reformatValues(ml), 0, team);

		LOGGER.info("Exiting getMoneyLine()");
		return team;
	}

	/**
	 * 
	 * @param divs
	 * @param map
	 * @param type
	 * @param sport
	 * @param foundString
	 * @param menuString
	 * @return
	 */
	protected Map<String, String> findMenu(Elements divs, Map<String, String> map, String[] type, String[] sport, String foundString, String menuString) {
		LOGGER.info("Entering findMenu()");

		for (int x = 0; (divs != null && x < divs.size()); x++) {
			final Element div = divs.get(x);
			LOGGER.debug("div: " + div);
			boolean foundDiv = false;

			for (int y = 0; y < type.length; y++) {
				for (int z = 0; z < sport.length; z++) {
					foundDiv = foundSport(div, foundString, type[y], sport[z]);
					LOGGER.debug("foundDiv: " + foundDiv);
	
					// Found the event
					if (foundDiv) {
						map = getMenuData(div, menuString, map);
						LOGGER.debug("Map: " + map);
					}
				}
			}
		}

		LOGGER.info("Exiting findMenu()");
		return map;
	}

	/**
	 * 
	 * @param divs
	 * @param map
	 * @param type
	 * @param sport
	 * @param foundString
	 * @param menuString
	 * @param position
	 * @return
	 */
	protected Map<String, String> findMenu2(Elements divs, Map<String, String> map, String[] type, String[] sport, String foundString, String menuString, int position) {
		LOGGER.info("Entering findMenu2()");

		for (int x = 0; (divs != null && x < divs.size()); x++) {
			final Element div = divs.get(x);
			boolean foundDiv = false;
			for (int y = 0; y < type.length; y++) {
				for (int z = 0; z < sport.length; z++) {
					foundDiv = foundSport2(div, foundString, type[y], sport[z], position);
					LOGGER.debug("foundDiv: " + foundDiv);
	
					// Found the event
					if (foundDiv) {
						map = getMenuData(div, menuString, map);
						LOGGER.debug("Map: " + map);
					}
				}
			}
		}

		LOGGER.info("Exiting findMenu2()");
		return map;
	}

	/**
	 * 
	 * @param div
	 * @param select
	 * @param sport
	 * @return
	 */
	protected boolean foundSport(Element div, String select, String type, String sport) {
		LOGGER.info("Entering foundSport()");
		LOGGER.debug("div: " + div);
		LOGGER.debug("select: " + select);
		LOGGER.debug("type: " + type);
		LOGGER.debug("sport: " + sport);
		boolean foundDiv = false;

		String divData = getHtmlFromElement(div, select, 0, false);
		LOGGER.debug("divData: " + divData);

		// Check if we found div
		if (divData != null && divData.equals(type)) {
			foundDiv = true;
		} else if (type.startsWith("NFL - WEEK") && divData != null && divData.startsWith("NFL - WEEK")) { // HACK!!!
			foundDiv = true;
		}

		LOGGER.info("Exiting foundSport()");
		return foundDiv;
	}

	/**
	 * 
	 * @param div
	 * @param select
	 * @param type
	 * @param sport
	 * @param position
	 * @return
	 */
	protected boolean foundSport2(Element div, String select, String type, String sport, int position) {
		LOGGER.info("Entering foundSport2()");
		LOGGER.debug("div: " + div);
		LOGGER.debug("select: " + select);
		LOGGER.debug("type: " + type);
		LOGGER.debug("sport: " + sport);
		boolean foundDiv = false;

		final Elements divs = div.select(select);
		for(int x=0; (divs != null && x < divs.size()); x++) {
			final Element ele = divs.get(x);
			if (ele != null) {
				String divData = ele.html();
				if (divData != null && divData.length() > 0) {
					divData = divData.replaceAll("&nbsp;", "");
					divData = divData.trim();					
					LOGGER.debug("divData2: " + divData);

					// Check if we found div
					if (divData != null && divData.equals(type)) {
						foundDiv = true;
						return foundDiv;
					} else if (type.startsWith("NFL - WEEK") && divData != null && divData.startsWith("NFL - WEEK")) { // HACK!!!
						foundDiv = true;
						return foundDiv;
					}
				}
			}
		}

		LOGGER.info("Exiting foundSport2()");
		return foundDiv;
	}

	/**
	 * 
	 * @param div
	 * @param element
	 * @param map
	 * @return
	 */
	protected Map<String, String> getMenuData(Element div, String element, Map<String, String> map) {
		LOGGER.info("Entering getMenuData()");
		LOGGER.debug("Div: " + div);
		LOGGER.debug("element: " + element);

		final Elements inputs = div.select(element);
		if (inputs != null && inputs.size() > 0) {
			final Element input = inputs.get(0);
			LOGGER.debug("input: " + input);

			if (input != null) {
				map.put(input.attr("name"), input.attr("value"));
			}
		}

		LOGGER.info("Exiting getMenuData()");
		return map;
	}

	/**
	 * 
	 * @param doc
	 * @param teamPackage
	 */
	protected void setupSpreadWager(Document doc, TDSportsTeamPackage teamPackage) {
		LOGGER.info("Entering setupSpreadWager()");

		final Map<String, String> hashMap = parseSelectField(doc.select("table tbody tr td select"));
		teamPackage.setGameSpreadSelectName(hashMap.get("name"));

		final Elements options = doc.select("table tbody tr td select option");
		if (options != null && options.size() > 0) {
			Map<String, SelectOptionData> optionsMap = parseSelectOptionField(options);
			for (int x = 0; x < optionsMap.size(); x++) {
				final SelectOptionData option = optionsMap.get(Integer.toString(x));
				if (option != null && option.getData() != null && option.getData().length() > 0) {
					String optionData = option.getData();
					optionData = optionData.trim();
					if (!"Buy no points".equals(optionData)) {
						int index = optionData.indexOf("for ");
						if (index != -1) {
							teamPackage.addGameSpreadOptionValue(Integer.toString(x), option.getValue());
							optionData = optionData.substring(index + "for ".length());

							// -2½ +200; Now parse the data
							teamPackage = (TDSportsTeamPackage)parseSpreadData(reformatValues(optionData), x, " ", null, teamPackage);
						}
					}
				} else {
					// Throw an exception
					throw new AppException(500, AppErrorCodes.SITE_PARSER_EXCEPTION,  
							AppErrorMessage.SITE_PARSER_EXCEPTION  + " Options are empty");					
				}
			}
		}

		LOGGER.info("Exiting setupSpreadWager()");
	}

	/**
	 * 
	 * @param doc
	 * @param teamPackage
	 */
	protected void setupTotalWager(Document doc, TDSportsTeamPackage teamPackage) {
		LOGGER.info("Entering setupTotalWager()");
		
		final Map<String, String> hashMap = parseSelectField(doc.select("table tbody tr td select"));
		teamPackage.setGameTotalSelectName(hashMap.get("name"));

		final Elements options = doc.select("table tbody tr td select option");
		if (options != null && options.size() > 0) {
			Map<String, SelectOptionData> optionsMap = parseSelectOptionField(options);
			for (int x = 0; x < optionsMap.size(); x++) {
				final SelectOptionData option = optionsMap.get(Integer.toString(x));
				if (option != null && option.getData() != null && option.getData().length() > 0) {
					String optionData = option.getData();
					optionData = optionData.trim();
					if (!"Buy no points".equals(optionData)) {
						int index = optionData.indexOf("for ");
						if (index != -1) {
							teamPackage.addGameTotalOptionValue(Integer.toString(x), option.getValue());
							optionData = optionData.substring(index + "for ".length());

							// -2½ +200; Now parse the data
							teamPackage = (TDSportsTeamPackage)parseTotalData(reformatValues(optionData), x, " ", null, teamPackage);
						}
					}
				} else {
					// Throw an exception
					throw new AppException(500, AppErrorCodes.SITE_PARSER_EXCEPTION,  
							AppErrorMessage.SITE_PARSER_EXCEPTION  + " Options are empty");					
				}
			}
		}

		LOGGER.info("Exiting setupTotalWager()");
	}
	
	/**
	 * 
	 * @param map
	 * @return
	 */
	protected Map<String, String> removeInputFields(Map<String, String> map) {
		// Check for a valid map
		final Map<String, String> deleteMap = new HashMap<String, String>();
		if (map != null && !map.isEmpty()) {
			final Set<Entry<String, String>> indexs = map.entrySet();
			if (indexs != null && !indexs.isEmpty()) {
				final Iterator<Entry<String, String>> itr = indexs.iterator();
				int counter = 0;
				while (itr != null && itr.hasNext()) {
					final Entry<String, String> values = itr.next();
					String key = values.getKey();
					String value = values.getValue();
					LOGGER.info("KEY: " + key);
					LOGGER.info("VALUE: " + value);

					if (key != null) {
						if (value != null && value.contains("Continue") || value.contains("View Odds")) {
							if (counter++ > 0) {
							} else {
								deleteMap.put(key, value);
							}
						} else if (value != null && value.contains("Refesh") || value.contains("Refresh") || value.contains("Clear") || value.contains("clear") || value.contains("Cancel") || value.contains("cancel")) {
						} else {
							deleteMap.put(key, value);
						}
					}
				}
			}
		}

		if (deleteMap != null && !deleteMap.isEmpty()) {
			final Set<Entry<String, String>> indexs = deleteMap.entrySet();
			if (indexs != null && !indexs.isEmpty()) {
				final Iterator<Entry<String, String>> itr = indexs.iterator();
				while (itr != null && itr.hasNext()) {
					final Entry<String, String> values = itr.next();
					String key = values.getKey();
					map.remove(key);
				}
			}
		}	

		return deleteMap;
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 */
	protected void getTicketNumberAndDate(Element td, PendingEvent pe) {
		LOGGER.info("Entering getTicketNumberAndDate()");

		// Ticket#: 31478859
		// <br>
		// Aug 26 08:00 PM
		// <br>
		String html = td.html();
		LOGGER.debug("html: " + html);
		if (html != null) {
			int index = html.indexOf("Ticket#:");
			if (index != -1) {
				int brindex = html.indexOf("<br>");
				if (brindex != -1) {
					pe.setTicketnum(html.substring(index + 9, brindex).trim());
					
					html = html.substring(brindex + 4);
					html = html.replace("<br>", "").trim();
					setGameDate(html, pe);
				}
			} else {
				index = html.indexOf("Ticket Number:");
				int brindex = html.indexOf("<br>");
				if (index != -1 && brindex != -1) {
					
					pe.setTicketnum(html.substring(index + "Ticket Number:".length(), brindex).trim());
					
					html = html.substring(brindex + 4);
					html = html.replace("<br>", "").trim();
					setGameDate(html, pe);
				} else {
					index = html.indexOf("Ticket #:");
					brindex = html.indexOf("<br>");
					if (index != -1 && brindex != -1) {
						pe.setTicketnum(html.substring(index + "Ticket #:".length(), brindex).trim());

						html = html.substring(brindex + 4);
						html = html.replace("<br>", "").trim();
						setGameDate(html, pe);
					} else {
						index = html.indexOf("Confirmation #:");
						brindex = html.indexOf("<br>");
						if (index != -1 && brindex != -1) {
							pe.setTicketnum(html.substring(index + "Confirmation #:".length(), brindex).trim());

							html = html.substring(brindex + 4);
							html = html.replace("<br>", "").trim();
							setGameDate(html, pe);
						}
					}
				}
			}
		}
		
		LOGGER.info("Exiting getTicketNumberAndDate()");
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 */
	protected void getEventSportAndType(Element td, PendingEvent pe) {
		LOGGER.info("Entering getEventSportAndType()");

		// <br>
		// NFL
		// <br>

		String html = td.html().trim();
		LOGGER.debug("html: " + html);
		if (html != null) {
			html = html.replace("<br>", "");
			int index = html.indexOf(" ");
			if (index != -1) {
				html = html.substring(0, index);
			}
			LOGGER.debug("html2: " + html);
			if ("NFL".equals(html)) {
				pe.setGamesport("Football");
				pe.setGametype(html);
			} else if ("CFB".equals(html)) {
				pe.setGamesport("Football");
				pe.setGametype("NCAA");				
			} else if ("NBA".equals(html)) {
				pe.setGamesport("Basketball");
				pe.setGametype(html);
			} else if ("CBB".equals(html)) {
				pe.setGamesport("Basketball");
				pe.setGametype("NCAA");
			} else if ("WNBA".equals(html)) {
				pe.setGamesport("Basketball");
				pe.setGametype(html);
			} else if ("NHL".equals(html)) {
				pe.setGamesport("Hockey");
				pe.setGametype(html);
			} else if ("MLB".equals(html)) {
				pe.setGamesport("Baseball");
				pe.setGametype(html);				
			} else if ("MU".equals(html)) {
				pe.setGamesport("Golf");
				pe.setGametype(html);
			}
		}

		LOGGER.info("Exiting getEventSportAndType()");
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 * @throws BatchException
	 */
	protected void getGameInfo(Element td, PendingEvent pe) throws BatchException {
		LOGGER.info("Entering getGameInfo()");
		LOGGER.debug("td: " + td);
		String html = td.html().trim();
		LOGGER.debug("html: " + html);

		//
		// Spread
		//
		// STRAIGHT BET
		// <br>
		// [270] NO SAINTS (PRESEASON) -2-105
		// <br>

		// 
		// Total
		//
		// STRAIGHT BET
		// <br>
		// [931] TOTAL o9EV (COL ROCKIES vrs KC ROYALS)
		// <br>
		// ( JON GRAY / DANNY DUFFY )
		// <br>

		//
		// Total
		//
		// STRAIGHT BET
		// <br>
		// [293] TOTAL o62-120 (B+½)
		// <br>
		// (HAWAII vrs MASSACHUSETTS)
		// <br>

		//
		// MoneyLine
		//
		// STRAIGHT BET
		// <br>
		// [932] KC ROYALS -122
		// <br>
		// ( JON GRAY / DANNY DUFFY )
		// <br>

		int index = html.indexOf("<br>");
		if (index != -1) {
			html = html.substring(index + 4);
			LOGGER.debug("html: " + html);

			if (html.contains("TEASER")) {
				pe.setEventtype("teaser");
				return;
			}

			// First get the rotation ID
			int bindex = html.indexOf("[");
			int eindex = html.indexOf("]");
			if (bindex != -1 && eindex != -1) {
				String rotationId = html.substring(bindex + 1, eindex);
				LOGGER.debug("rotationID: " + rotationId);
				pe.setRotationid(rotationId);

				// Check what type it is
				if (rotationId.length() == 1 || rotationId.length() == 2 || rotationId.length() == 3 || rotationId.length() == 6) {
					pe.setLinetype("game");
				} else if (rotationId.startsWith("1") && rotationId.length() == 4) {
					pe.setLinetype("first");
				} else if (rotationId.startsWith("2") && rotationId.length() == 4) {
					pe.setLinetype("second");
				} else if (rotationId.startsWith("3") && rotationId.length() == 4) {
					// Determine if this is a Quarter, Period or something else
					if (html.contains("1Q")) {
						pe.setEventtype("unsupported");
						throw new BatchException(BatchErrorCodes.UNSUPPORTED_SPORT_TYPE, BatchErrorMessage.UNSUPPORTED_SPORT_TYPE, html);
					} else {
						pe.setLinetype("third");
					}
				} else if (rotationId.length() >= 4) {
					pe.setEventtype("unsupported");
					throw new BatchException(BatchErrorCodes.UNSUPPORTED_SPORT_TYPE, BatchErrorMessage.UNSUPPORTED_SPORT_TYPE, html);
				}

				html = html.substring(eindex + 2); // Skip to next thing
				LOGGER.debug("html: " + html);
				bindex = html.indexOf("TOTAL ");
				if (bindex != -1) {
					getTotal(html, bindex, pe);
				} else {
					bindex = html.indexOf(" u");
					if (bindex != -1) {
						String tempHtml = html;
						getSpreadTotalMoneyline(html, pe);
						index = tempHtml.indexOf("<br>");
						if (index != -1) {
							tempHtml = tempHtml.substring(index + 4);
							tempHtml = tempHtml.replace("<br>", "").replace("(", "").replace(")", "").trim();
							LOGGER.debug("tempHtml: " + tempHtml);
							pe.setPitcher(tempHtml);
						}
					} else {
						bindex = html.indexOf(" o");
						if (bindex != -1) {
							String tempHtml = html;
							getSpreadTotalMoneyline(html, pe);
							index = tempHtml.indexOf("<br>");
							if (index != -1) {
								tempHtml = tempHtml.substring(index + 4);
								tempHtml = tempHtml.replace("<br>", "").replace("(", "").replace(")", "").trim();
								LOGGER.debug("tempHtml: " + tempHtml);
								pe.setPitcher(tempHtml);
							}
						} else {
							getSpreadMoneyline(html, pe);
						}
					}
				}
			}
		}

		LOGGER.info("Exiting getGameInfo()");
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 */
	protected void getGameInfo2(Element td, PendingEvent pe) throws BatchException {
		LOGGER.info("Entering getGameInfo2()");
		LOGGER.debug("td: " + td);
		LOGGER.debug("PendingEvent: " + pe);
		String html = td.html().trim();
		LOGGER.debug("html: " + html);

		//
		// Spread
		//
		// STRAIGHT BET
		// <br>
		// [270] NO SAINTS (PRESEASON) -2-105
		// <br>

		// STRAIGHT BET
		// <br>
		// [474] TEXANS (HOU) u45-110 (SUNDAY NIGHT FOOTBALL (NBC))
		// <br>

		// 
		// Total
		//
		// STRAIGHT BET
		// <br>
		// [931] TOTAL o9EV (COL ROCKIES vrs KC ROYALS)
		// <br>
		// ( JON GRAY / DANNY DUFFY )
		// <br>

		//
		// Total
		//
		// STRAIGHT BET
		// <br>
		// [293] TOTAL o62-120 (B+½)
		// <br>
		// (HAWAII vrs MASSACHUSETTS)
		// <br>

		//
		// MoneyLine
		//
		// STRAIGHT BET
		// <br>
		// [932] KC ROYALS -122
		// <br>
		// ( JON GRAY / DANNY DUFFY )
		// <br>

		int index = html.indexOf("<br>");
		if (index != -1) {
			html = html.substring(index + 4);
			// First get the rotation ID
			int bindex = html.indexOf("[");
			int eindex = html.indexOf("]");
			if (bindex != -1 && eindex != -1) {
				String rotationId = html.substring(bindex + 1, eindex);
				LOGGER.debug("rotationID: " + rotationId);
				pe.setRotationid(rotationId);

				// Check what type it is
				if (rotationId.length() == 1 || rotationId.length() == 2 || rotationId.length() == 3) {
					pe.setLinetype("game");
				} else if (rotationId.startsWith("1") && rotationId.length() == 4) {
					pe.setLinetype("first");
				} else if (rotationId.startsWith("2") && rotationId.length() == 4) {
					pe.setLinetype("second");
				} else if (rotationId.startsWith("3") && rotationId.length() == 4) {
					// Determine if this is a Quarter, Period or something else
					if (html.contains("1Q")) {
						pe.setEventtype("unsupported");
						throw new BatchException(BatchErrorCodes.UNSUPPORTED_SPORT_TYPE, BatchErrorMessage.UNSUPPORTED_SPORT_TYPE, html);
					} else {
						pe.setLinetype("third");
					}
				} else if (rotationId.length() >= 4) {
					pe.setEventtype("unsupported");
					throw new BatchException(BatchErrorCodes.UNSUPPORTED_SPORT_TYPE, BatchErrorMessage.UNSUPPORTED_SPORT_TYPE, html);
				}

				html = html.substring(eindex + 2); // Skip to next thing
				LOGGER.debug("html: " + html);
				getSpreadTotalMoneyline(html, pe);
			}
		}

		LOGGER.info("Exiting getGameInfo2()");
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 */
	protected void getRiskWin(Element td, PendingEvent pe) {
		LOGGER.info("Entering getRiskWin()");
		String html = td.html().trim();

		if (html != null) {
			html = html.replace("<br>", "");
			
			int index = html.indexOf("/");
			if (index != -1) {
				pe.setRisk(html.substring(0, index).trim());
				pe.setWin(html.substring(index + 1).trim());
			}
		}

		LOGGER.info("Exiting getRiskWin()");
	}

	/**
	 * 
	 * @param html
	 * @param pe
	 */
	protected void getSpreadMoneyline(String html, PendingEvent pe) {
		LOGGER.info("Entering getSpreadMoneyline()");
		LOGGER.debug("html: " + html);

		// Now determine spread or ML
		html = html.trim();
		int index = html.indexOf("<br>");

		if (index != -1) {
			pe.setPitcher(html.substring(index + 4).replace("<br>", "").replace("(", "").replace(")", "").trim());
			String tempHtml = html.substring(0, index);
			LOGGER.debug("tempHtml: " + tempHtml);

			if (tempHtml != null) {
				tempHtml = tempHtml.trim();

				// check if we have ()
				int bindex = tempHtml.indexOf("(");
				int eindex = tempHtml.indexOf(")");
				if (bindex != -1 && eindex != -1) {
					tempHtml = tempHtml.substring(0, bindex).trim();
					LOGGER.debug("tempHtml: " + tempHtml);
				}

				index = tempHtml.lastIndexOf(" ");

				if (index != -1) {
					tempHtml = tempHtml.substring(index + 1);

					// Kansas State o62-120 (B+½)
					if (tempHtml != null && tempHtml.startsWith("(")) {
						tempHtml = html.substring(0, index);
						LOGGER.debug("tempHtml: " + tempHtml);
						index = tempHtml.lastIndexOf(" ");

						if (index != -1) {
							// Get the team while we are here
							pe.setTeam(tempHtml.substring(0, index));
							tempHtml = tempHtml.substring(index + 1);
							LOGGER.debug("tempHtml: " + tempHtml);
		
							// Now determine spread or ML
							determineSpreadMoney(tempHtml, pe);
						}
					} else {
						LOGGER.debug("tempHtml: " + tempHtml);
						if (tempHtml != null) {
							tempHtml = tempHtml.replace("<br>", "").trim();
		
							// Get the team while we are here
							pe.setTeam(html.substring(0, index).trim());
			
							// Now determine spread or ML
							determineSpreadMoney(tempHtml.trim(), pe);
						}
					}
				}
			}
		}

		LOGGER.info("Exiting getSpreadMoneyline()");
	}

	/**
	 * 
	 * @param html
	 * @param pe
	 */
	protected void getSpreadTotalMoneyline(String html, PendingEvent pe) {
		LOGGER.info("Entering getSpreadTotalMoneyline()");
		LOGGER.debug("html: " + html);
		String team = "";

		final StringTokenizer st = new StringTokenizer(html);
		while (st.hasMoreElements()) {
			String word = (String)st.nextElement();
			determineSpreadTotalMoney(word, pe);
			LOGGER.debug("PendingEvent: " + pe);
			if (pe.getEventtype() == null || pe.getEventtype().length() == 0) {
				team += (word + " ");
			} else {
				pe.setTeam(team.trim());
				break;
			}
		}

		LOGGER.info("Exiting getSpreadTotalMoneyline()");
	}

	/**
	 * 
	 * @param html
	 * @param pe
	 */
	private void determineSpreadTotalMoney(String html, PendingEvent pe) {
		LOGGER.info("Entering determineSpreadTotalMoney()");
		LOGGER.debug("html: " + html);

		//
		// Spread Examples
		//
		// -1-120
		// +1+120
		// -1+120
		// +1-120
		// PK-120
		// PK+120
		// EV-120
		// EV+120
		// -1EV
		// +1EV
		// -1PK
		// +1PK
		// PKEV
		// EVPK
		// PKPK
		// EVEV

		//
		// Moneyline Examples
		//
		// +120
		// -233
		// EV
		// PK

		// Remove breaks
		html = html.replace("<br>", "");
		LOGGER.debug("html: " + html);

		// Check for money first
		if ((html.startsWith("EV") || html.startsWith("PK")) && (html.length() == 2)) {
			// We have moneyline
			pe.setEventtype("ml");
			pe.setLineplusminus("+");
			pe.setLine("100");
			pe.setJuiceplusminus("+");
			pe.setJuice("100");
		} else if ((html.startsWith("+") || html.startsWith("-")) && (html.length() == 4) && (!html.endsWith("EV") && !html.endsWith("PK"))) {
			// We have moneyline
			pe.setEventtype("ml");
			pe.setLineplusminus(html.substring(0, 1));
			pe.setLine(super.reformatValues(html.substring(1)));
			pe.setJuiceplusminus(html.substring(0, 1));
			pe.setJuice(super.reformatValues(html.substring(1)));
		} else if (html.startsWith("o") || html.startsWith("u")) {
			// We have moneyline
			pe.setEventtype("total");
			pe.setLineplusminus(html.substring(0, 1));
			html = html.substring(1);

			int eindex = html.lastIndexOf("EV");
			int pindex = html.lastIndexOf("PK");
			int plindex = html.lastIndexOf("+");
			int mindex = html.lastIndexOf("-");
			LOGGER.debug("eindex: " + eindex);
			LOGGER.debug("pindex: " + pindex);
			LOGGER.debug("plindex: " + plindex);
			LOGGER.debug("mindex: " + mindex);

			// Now get the line value and juice
			if (eindex != -1) {
				pe.setLine(super.reformatValues(html.substring(0, eindex)));
				pe.setJuiceplusminus("+");
				pe.setJuice("100");
			} else if (pindex != -1) {
				pe.setLine(super.reformatValues(html.substring(0, pindex)));
				pe.setJuiceplusminus("+");
				pe.setJuice("100");
			} else if (plindex != -1) {
				pe.setLine(super.reformatValues(html.substring(0, plindex)));
				pe.setJuiceplusminus("+");
				pe.setJuice(super.reformatValues(html.substring(plindex + 1)));
			} else if (mindex != -1) {
				pe.setLine(super.reformatValues(html.substring(0, mindex)));
				pe.setJuiceplusminus("-");
				pe.setJuice(super.reformatValues(html.substring(mindex + 1)));					
			}
		} else {
			if (html.startsWith("EV") || html.startsWith("PK")) {
				// Spread
				pe.setEventtype("spread");
				pe.setLineplusminus("+");
				pe.setLine("100");
				// Now get the Juice
				html = html.substring(2);
				if (html.startsWith("EV") || html.startsWith("PK")) {
					pe.setJuiceplusminus("+");
					pe.setJuice("100");
				} else if (html.startsWith("+") || html.startsWith("-")) {
					pe.setJuiceplusminus(html.substring(0, 1));
					pe.setJuice(super.reformatValues(html.substring(1)));
				}
			} else if (html.startsWith("+") || html.startsWith("-")) {
				// Spread
				pe.setEventtype("spread");
				pe.setLineplusminus(html.substring(0, 1));
				html = html.substring(1);

				int eindex = html.lastIndexOf("EV");
				int pindex = html.lastIndexOf("PK");
				int plindex = html.lastIndexOf("+");
				int mindex = html.lastIndexOf("-");
				LOGGER.debug("eindex: " + eindex);
				LOGGER.debug("pindex: " + pindex);
				LOGGER.debug("plindex: " + plindex);
				LOGGER.debug("mindex: " + mindex);

				// Now get the line value and juice
				if (eindex != -1) {
					pe.setLine(super.reformatValues(html.substring(0, eindex)));
					pe.setJuiceplusminus("+");
					pe.setJuice("100");
				} else if (pindex != -1) {
					pe.setLine(super.reformatValues(html.substring(0, pindex)));
					pe.setJuiceplusminus("+");
					pe.setJuice("100");
				} else if (plindex != -1) {
					pe.setLine(super.reformatValues(html.substring(0, plindex)));
					pe.setJuiceplusminus("+");
					pe.setJuice(super.reformatValues(html.substring(plindex + 1)));
				} else if (mindex != -1) {
					pe.setLine(super.reformatValues(html.substring(0, mindex)));
					pe.setJuiceplusminus("-");
					pe.setJuice(super.reformatValues(html.substring(mindex + 1)));					
				}
			}
		}

		LOGGER.info("Exiting determineSpreadTotalMoney()");
	}

	/**
	 * 
	 * @param html
	 * @param pe
	 */
	protected void determineSpreadMoney(String html, PendingEvent pe) {
		LOGGER.info("Entering determineSpreadMoney()");
		LOGGER.debug("html: " + html);

		//
		// Spread Examples
		//
		// -1-120
		// +1+120
		// -1+120
		// +1-120
		// PK-120
		// PK+120
		// EV-120
		// EV+120
		// -1EV
		// +1EV
		// -1PK
		// +1PK
		// PKEV
		// EVPK
		// PKPK
		// EVEV

		//
		// Moneyline Examples
		//
		// +120
		// -233
		// EV
		// PK

		// Check for money first
		if ((html.startsWith("EV") || html.startsWith("PK")) && (html.length() == 2)) {
			// We have moneyline
			pe.setEventtype("ml");
			pe.setLineplusminus("+");
			pe.setLine("100");
			pe.setJuiceplusminus("+");
			pe.setJuice("100");
		} else if ((html.startsWith("+") || html.startsWith("-")) && (html.length() == 4) && (!html.endsWith("EV") && !html.endsWith("PK"))) {
			// We have moneyline
			pe.setEventtype("ml");
			pe.setLineplusminus(html.substring(0, 1));
			pe.setLine(super.reformatValues(html.substring(1)));
			pe.setJuiceplusminus(html.substring(0, 1));
			pe.setJuice(super.reformatValues(html.substring(1)));
		} else {
			// Spread
			pe.setEventtype("spread");
			if (html.startsWith("EV") || html.startsWith("PK")) {
				pe.setLineplusminus("+");
				pe.setLine("100");
				// Now get the Juice
				html = html.substring(2);
				if (html.startsWith("EV") || html.startsWith("PK")) {
					pe.setJuiceplusminus("+");
					pe.setJuice("100");
				} else if (html.startsWith("+") || html.startsWith("-")) {
					pe.setJuiceplusminus(html.substring(0, 1));
					pe.setJuice(super.reformatValues(html.substring(1)));
				}
			} else {
				pe.setLineplusminus(html.substring(0, 1));
				html = html.substring(1);

				int eindex = html.lastIndexOf("EV");
				int pindex = html.lastIndexOf("PK");
				int plindex = html.lastIndexOf("+");
				int mindex = html.lastIndexOf("-");
				LOGGER.debug("eindex: " + eindex);
				LOGGER.debug("pindex: " + pindex);
				LOGGER.debug("plindex: " + plindex);
				LOGGER.debug("mindex: " + mindex);

				// Now get the line value and juice
				if (eindex != -1) {
					pe.setLine(super.reformatValues(html.substring(0, eindex)));
					pe.setJuiceplusminus("+");
					pe.setJuice("100");
				} else if (pindex != -1) {
					pe.setLine(super.reformatValues(html.substring(0, pindex)));
					pe.setJuiceplusminus("+");
					pe.setJuice("100");
				} else if (plindex != -1) {
					pe.setLine(super.reformatValues(html.substring(0, plindex)));
					pe.setJuiceplusminus("+");
					pe.setJuice(super.reformatValues(html.substring(plindex + 1)));
				} else if (mindex != -1) {
					pe.setLine(super.reformatValues(html.substring(0, mindex)));
					pe.setJuiceplusminus("-");
					pe.setJuice(super.reformatValues(html.substring(mindex + 1)));					
				}
			}
		}

		LOGGER.info("Exiting determineSpreadMoney()");
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 */
	protected void getTotal(String html, int bindex, PendingEvent pe) {
		LOGGER.info("Entering getTotal()");
		html = html.substring(bindex + 6);
		LOGGER.debug("html: " + html);

		// Then we know we have a total
		pe.setEventtype("total");
		int eindex = html.indexOf("<br>");
		if (eindex != -1) {
			String lines = html.substring(0, eindex).trim();
			LOGGER.debug("lines: " + lines);

			if (lines != null) {
				lines = lines.trim();
				// u9EV or o42½-110 or
				pe.setLineplusminus(lines.substring(0, 1));
				lines = lines.substring(1);
				int tindex = lines.indexOf("(");
				int evindex = -1;
				int pkindex = -1;
				
				if (tindex != -1) {
					String xlines = lines.substring(0, tindex);
					evindex = xlines.indexOf("EV");
					pkindex = xlines.indexOf("PK");
					LOGGER.debug("evindex: " + evindex);
					LOGGER.debug("pkindex: " + pkindex);
				} else {
					evindex = lines.indexOf("EV");
					pkindex = lines.indexOf("PK");
					LOGGER.debug("evindex2: " + evindex);
					LOGGER.debug("pkindex2: " + pkindex);
				}

				if (evindex != -1 || pkindex != -1) {
					if (evindex != -1) {
						LOGGER.debug("linesieke: " + lines);
						pe.setLine(reformatValues(lines.substring(0, evindex)));
						pe.setJuiceplusminus("+");
						pe.setJuice("100");
						lines = lines.substring(evindex + 2);

						if (lines != null) {
							lines = lines.trim();
							final String gameType = pe.getGametype();

							if ("MLB".equals(gameType)) {								
								// o4½-110 (1H HOU ASTROS vrs 1H TEX RANGERS)
								int paindex = lines.indexOf("(");
								if (paindex != -1) {
									String tempLines = lines.substring(paindex);
									tempLines = tempLines.replace("(", "");
									tempLines = tempLines.replace(")", "");
									tempLines = tempLines.trim();

									pe.setTeam(tempLines);
								}
							}
						}
					} else {
						LOGGER.debug("lines: " + lines);
						pe.setLine(reformatValues(lines.substring(0, pkindex)));
						pe.setJuiceplusminus("+");
						pe.setJuice("100");
					}
				} else {
					int mindex = lines.indexOf("-");
					int pindex = lines.indexOf("+");

					if (mindex != -1 || pindex != -1) {
						final String gameType = pe.getGametype();

						if (mindex != -1) {
							LOGGER.debug("lineskdkf: " + lines.substring(0, mindex));
							pe.setLine(reformatValues(lines.substring(0, mindex).trim()));
							pe.setJuiceplusminus("-");

							if ("MLB".equals(gameType)) {								
								// o4½-110 (1H HOU ASTROS vrs 1H TEX RANGERS)
								lines = lines.substring(mindex + 1);
								LOGGER.debug("linesxxx: " + lines);
								int paindex = lines.indexOf("(");

								if (paindex != -1) {
									String tempLines = lines.substring(0, paindex).trim();
									LOGGER.debug("templinesxxx: " + lines);
									pe.setJuice(reformatValues(tempLines));
									tempLines = lines.substring(paindex);
									tempLines = tempLines.replace("(", "");
									tempLines = tempLines.replace(")", "");
									tempLines = tempLines.trim();
									LOGGER.debug("templinesyyy: " + lines);

									pe.setTeam(tempLines);
								}
							} else {
								pe.setJuice(reformatValues(lines.substring(mindex + 1)));
							}
						} else {
							pe.setLine(reformatValues(lines.substring(0, pindex).trim()));
							pe.setJuiceplusminus("+");

							if ("MLB".equals(gameType)) {								
								// o4½-110 (1H HOU ASTROS vrs 1H TEX RANGERS)
								lines = lines.substring(pindex + 1);
								LOGGER.debug("linesayy: " + lines);
								int paindex = lines.indexOf("(");

								if (paindex != -1) {
									String tempLines = lines.substring(0, paindex).trim();
									LOGGER.debug("teampLines: " + tempLines);
									LOGGER.debug("templinesayy: " + lines);

									pe.setJuice(reformatValues(tempLines));
									tempLines = lines.substring(paindex);
									tempLines = tempLines.replace("(", "");
									tempLines = tempLines.replace(")", "");
									tempLines = tempLines.trim();

									pe.setTeam(tempLines);
								}
							} else {
								pe.setJuice(reformatValues(lines.substring(pindex + 1)));
							}
						}
					} 
				}	
			}

			html = html.substring(eindex + 4);
			LOGGER.debug("html: " + html);
			final String gameType = pe.getGametype();
			LOGGER.debug("gameType: " + gameType);

			if ("MLB".equals(gameType)) {
				if (html != null) {
					html = html.replace("<br>", " ");
					html = html.replace("(", "");
					html = html.replace(")", "");
					pe.setPitcher(html.trim());
				}
			} else {
				if (html != null) {
					html = html.replace("<br>", " ");
					html = html.replace("(", "");
					html = html.replace(")", "");
					pe.setTeam(html.trim());
				}
			}
		}

		LOGGER.info("Exiting getTotal()");
	}

	/**
	 * 
	 * @param html
	 * @param bindex
	 * @param pe
	 */
	protected void getTotalNoBreak(String html, int bindex, PendingEvent pe) {
		LOGGER.info("Entering getTotalNoBreak()");
		html = html.substring(bindex + 6);
		LOGGER.debug("html: " + html);

		// Then we know we have a total
		pe.setEventtype("total");
		html = html.trim();
		LOGGER.debug("html: " + html);
		if (html != null) {
			html = html.trim();
			int eindex = html.lastIndexOf("(");
			if (eindex != -1) {
				String lines = html.substring(0, eindex);

				// u9EV or o42½-110 or
				pe.setLineplusminus(lines.substring(0, 1));
				lines = lines.substring(1);
				int evindex = lines.indexOf("EV");
				int pkindex = lines.indexOf("PK");
	
				if (evindex != -1 || pkindex != -1) {
					if (evindex != -1) {
						LOGGER.debug("lines: " + lines);
						pe.setLine(reformatValues(lines.substring(0, evindex)));
						pe.setJuiceplusminus("+");
						pe.setJuice("100");
					} else {
						LOGGER.debug("lines: " + lines);
						pe.setLine(reformatValues(lines.substring(0, pkindex)));
						pe.setJuiceplusminus("+");
						pe.setJuice("100");
					}
				} else {
					int mindex = lines.indexOf("-");
					int pindex = lines.indexOf("+");
					if (mindex != -1 || pindex != -1) {
						if (mindex != -1) {
							pe.setLine(reformatValues(lines.substring(0, mindex)));
							pe.setJuiceplusminus("-");
							pe.setJuice(reformatValues(lines.substring(mindex + 1)));
						} else {
							pe.setLine(reformatValues(lines.substring(0, pindex)));
							pe.setJuiceplusminus("-");
							pe.setJuice(reformatValues(lines.substring(pindex + 1)));
						}
					}
				}
				
				html = html.substring(eindex);
				LOGGER.debug("html: " + html);
				String gameType = pe.getGametype();
				LOGGER.debug("gameType: " + gameType);
				if ("MLB".equals(gameType)) {
					if (html != null) {
						html = html.replace("<br>", " ");
						html = html.replace("(", "");
						html = html.replace(")", "");
						pe.setTeam(html.trim());
						pe.setPitcher(html.trim());
					}
				} else {
					if (html != null) {
						html = html.replace("<br>", " ");
						html = html.replace("(", "");
						html = html.replace(")", "");
						pe.setTeam(html.trim());
					}
				}
			}
		}

		LOGGER.info("Exiting getTotalNoBreak()");
	}

	/**
	 * 
	 * @param gamedate
	 * @param pe
	 */
	protected void setGameDate(String gamedate, PendingEvent pe) {
		LOGGER.info("Entering setGameDate()");
		LOGGER.info("gamedate: " + gamedate);

		try {
			Calendar now = Calendar.getInstance();
			int year = now.get(Calendar.YEAR);

			pe.setEventdate(year + " " + gamedate + " " + determineTimeZone(super.timezone));
			try {
				pe.setGamedate(fixDate(year + " " + gamedate + " " + determineTimeZone(super.timezone), PENDING_DATE_FORMAT.get()));
			} catch (ParseException pe1) {
				pe.setGamedate(fixDate(year + " " + gamedate + " " + determineTimeZone(super.timezone), PENDING_DATE_FORMAT_2.get()));
			}
		} catch (ParseException pee) {
			LOGGER.error("gamedate: " + gamedate + " " + determineTimeZone(super.timezone));
			LOGGER.error(pee.getMessage(), pee);
			pe.setGamedate(new Date());
		} catch (NumberFormatException nfe) {
			LOGGER.error("gamedate: " + gamedate + " " + determineTimeZone(super.timezone));
			LOGGER.error(nfe.getMessage(), nfe);
			pe.setGamedate(new Date());
		}

		LOGGER.info("Exiting setGameDate()");
	}
}