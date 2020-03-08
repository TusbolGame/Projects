/**
 * 
 */
package com.ticketadvantage.services.dao.sites.linetracker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ticketadvantage.services.dao.sites.SiteParser;
import com.ticketadvantage.services.dao.sites.SiteTeamPackage;
import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.PendingEvent;

/**
 * @author jmiller
 *
 */
public class LineTrackerParser extends SiteParser {
	private final static Logger LOGGER = Logger.getLogger(LineTrackerParser.class);
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy hh:mm a z");

	/**
	 * Constructor
	 */
	public LineTrackerParser() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			LineTrackerParser lineTrackerParser = new LineTrackerParser();
			//String xhtml = "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head>    <meta name=\"google-translate-customization\" content=\"3233c073ab0f4e8f-812f5fbec3c21e36-gd944dbbbb163dd6b-17\" />    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />    <meta http-equiv=\"Content-Script-Type\" content=\"text/javascript\" />    <meta http-equiv=\"Content-Style-Type\" content=\"text/css\" />    <title>MSB247</title>    <link rel=\"shortcut icon\" href=\"/images/ikon_MSB247.ico\" />    <meta name=\"keywords\" lang=\"en\" content=\"Title\" />    <meta name=\"description\" lang=\"en\" content=\"Title\" />    <meta name=\"language\" content=\"en\" />    <meta http-equiv=\"imagetoolbar\" content=\"no\" />        <style type=\"text/css\">        @import \"/Themes/Theme021/Styles/AllAjax.css?v=2\";        @import \"/Themes/Theme021/Styles/ClientStyle.css?v=2\";        @import \"/Themes/Theme021/Styles/AjaxClientStyle.css?v=2\";        @import \"/Themes/Theme021/Styles/Style.css?v=2\";                                @import \"/Themes/Theme021/Styles/StyleTabs.css?v=2\";        @import \"/Themes/Theme021/Styles/SessionTimeoutPopup.css?v=2\";        @import \"/App_Themes/WtSoftware/ComboBox.WTSoftware.css?v=2\";        @import \"/App_Themes/WtSoftware/TreeView.WTSoftware.css?v=2\";        @import \"/App_Themes/WtSoftware/Menu.WTSoftware.css?v=2\";    </style>    <link href=\"Styles/jquery.cluetip.css\" rel=\"stylesheet\" type=\"text/css\" />    <script type=\"text/javascript\" src=\"/Scripts/jquery/jquery-1.9.1.min.js\"></script>        <script>if(this != top){  top.document.location.href = this.document.location.href;}</script></head><body onload=\"mp_onload();\">    <form name=\"Form1\" method=\"post\" action=\"./StraightBet.aspx\" id=\"Form1\" style=\"min-height: 100%; height: 100% !important\"><div><input type=\"hidden\" name=\"__EVENTTARGET\" id=\"__EVENTTARGET\" value=\"\" /><input type=\"hidden\" name=\"__EVENTARGUMENT\" id=\"__EVENTARGUMENT\" value=\"\" /><input type=\"hidden\" name=\"__VIEWSTATEFIELDCOUNT\" id=\"__VIEWSTATEFIELDCOUNT\" value=\"7\" /><input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"/wEPDwUKLTIzMzUyODMzMw8WFB4RSWRMYXN0U2hhZGVDaGFuZ2UoKVlTeXN0ZW0uSW50NjQsIG1zY29ybGliLCBWZXJzaW9uPTQuMC4wLjAsIEN1bHR1cmU9bmV1dHJhbCwgUHVibGljS2V5VG9rZW49Yjc3YTVjNTYxOTM0ZTA4OQYzMjM4MzIeCUlzQ29udGVzdGQeA0NXUwUkZmMzOTU0YWItOTNjZS00M2FlLWI4ODktNGZkNjhmMWRhZmE1HgpQZXJpb2RUeXBlBQMwLDEeC2lzVGVhbVRvdGFsaB4LVGVhbXNTZWFyY2hlHhFQcm9jY2Vzc2luZ05leHRVcGgeCV9mdWxsUGF0aGUeEElkTGFzdEdhbWVDaGFuZ2UoKwQKMTAzMjUxMzQzNR4MSWRTcG9ydExpbmVzBQUxMCwxMBYCZg9kFgICAQ9kFhICAQ8PFgIeCEltYWdlVXJsBRh+L0ltYWdlcy9sb2dvX21zYjI0Ny5w\" /><input type=\"hidden\" name=\"__VIEWSTATE1\" id=\"__VIEWSTATE1\" value=\"bmdkZAIDDw8WAh4EVGV4dAUFTEI5MDhkZAIEDxYCHglpbm5lcmh0bWwFCFVzZXIgSUQ6ZAIFD2QWFAIBDxYCHwsFLTxmb250IGNsYXNzPVN1bW1hcnlBY2NvdW50SGVhZGVyPkxCOTA4PC9mb250PmQCAw8WAh8MBRFDdXJyZW50IEJhbGFuY2U6IGQCBQ8WAh8LBTA8Zm9udCBjbGFzcz1TdW1tYXJ5TmVnYXRpdmVOdW1iZXI+LTI4MC4wMDwvZm9udD5kAgcPFgIeB1Zpc2libGVoFgICAQ8WAh8MBQlGUkVFIFBMQVlkAgkPFgIfDAULQXZhaWxhYmxlOiBkAgsPFgIfCwVJPGZvbnQgaWQ9J2xibEF2YWlsYWJsZUFtb3VudCcgY2xhc3M9U3VtbWFyeVBvc2l0aXZlTnVtYmVyPjksNTU1LjAwPC9mb250PmQCDQ8WAh8NaBYEAgEPFgIfDAUJQmFsYW5jZTogZAIDDxYCHwsF\" /><input type=\"hidden\" name=\"__VIEWSTATE2\" id=\"__VIEWSTATE2\" value=\"LTxmb250IGNsYXNzPVN1bW1hcnlQb3NpdGl2ZU51bWJlcj4wLjAwPC9mb250PmQCDw8WAh8MBQlQZW5kaW5nOiBkAhEPFgIfCwVFPGZvbnQgaWQ9J2xibFBlbmRpbmdBbW91bnQnIGNsYXNzPVN1bW1hcnlQb3NpdGl2ZU51bWJlcj4xNjUuMDA8L2ZvbnQ+ZAITDxYCHw1oFgQCAQ8WAh8MBQlQZW5kaW5nOiBkAgMPFgIfCwUtPGZvbnQgY2xhc3M9U3VtbWFyeVBvc2l0aXZlTnVtYmVyPjAuMDA8L2ZvbnQ+ZAIGDxYCHgtfIUl0ZW1Db3VudAIHFg5mD2QWAmYPFQYMZmlyc3QgYWN0aXZlDWJ0blNwb3J0c01lbnUXU3RyYWlnaHRCZXRNZW51RXgyLmFzcHgAAAZTcG9ydHNkAgEPZBYCZg8VBgEgDWJ0bkNhc2lub01lbnUYLi4vQ2xpZW50L0Nhc2lub0FTVC5hc3B4AAZf\" /><input type=\"hidden\" name=\"__VIEWSTATE3\" id=\"__VIEWSTATE3\" value=\"YmxhbmsGQ2FzaW5vZAICD2QWAmYPFQYBIA1idG5Ib3JzZXNNZW51GS4uL0NsaWVudC9Ib3JzZVdhZ2VyLmFzcHgAAAZIb3JzZXNkAgMPZBYCZg8VBgEgDmJ0bkFjY291bnRNZW51EERhaWx5RmlndXJlLmFzcHgAAAdBY2NvdW50ZAIED2QWAmYPFQYBIA5idG5QZW5kaW5nTWVudRNQZW5kaW5nc1dhZ2Vycy5hc3B4AAAHUGVuZGluZ2QCBQ9kFgJmDxUGASANYnRuU2NvcmVzTWVudTFodHRwOi8vc2NvcmVzLnNwb3J0c29wdGlvbnMuY29tL3Njb3Jlcy90b2RheS5odG1sAAZfYmxhbmsGU2NvcmVzZAIGD2QWAmYPFQYBIAxidG5SdWxlc01lbnUULi4vQ2xpZW50L1J1bGVzLmFzcHgAAAVSdWxlc2QCBw8WAh4FY2xhc3MFBWxhc3QgZAIID2QWAgIDD2QWAgIBDxYCHw4C\" /><input type=\"hidden\" name=\"__VIEWSTATE4\" id=\"__VIEWSTATE4\" value=\"BBYIZg9kFgJmDxUGB2N1cnJlbnQDcmVkF1N0cmFpZ2h0QmV0TWVudUV4Mi5hc3B4AAlTdHJhaWdodHMAZAIBD2QWAmYPFQYAABVQYXJsYXlCZXRNZW51RXgyLmFzcHgAB1BhcmxheXMAZAICD2QWAmYPFQYAABVUZWFzZXJCZXRNZW51RXgyLmFzcHgAB1RlYXNlcnMAZAIDD2QWAmYPFQYAABhJZkJldFJldmVyc2VNZW51RXgyLmFzcHgAEklmIEJldHMgLyBSZXZlcnNlcwlJbnZpc2libGVkAgoPZBYCAgMPFgIfDGVkAgwPZBYCAgEPZBYUAgEPFgIfCwUtPGZvbnQgY2xhc3M9U3VtbWFyeUFjY291bnRIZWFkZXI+TEI5MDg8L2ZvbnQ+ZAIDDxYCHwwFEUN1cnJlbnQgQmFsYW5jZTogZAIFDxYCHwsFMDxmb250IGNsYXNzPVN1bW1hcnlOZWdhdGl2ZU51bWJlcj4tMjgw\" /><input type=\"hidden\" name=\"__VIEWSTATE5\" id=\"__VIEWSTATE5\" value=\"LjAwPC9mb250PmQCBw8WAh8NaBYCAgEPFgIfDAUJRlJFRSBQTEFZZAIJDxYCHwwFC0F2YWlsYWJsZTogZAILDxYCHwsFSTxmb250IGlkPSdsYmxBdmFpbGFibGVBbW91bnQnIGNsYXNzPVN1bW1hcnlQb3NpdGl2ZU51bWJlcj45LDU1NS4wMDwvZm9udD5kAg0PFgIfDWgWBAIBDxYCHwwFCUJhbGFuY2U6IGQCAw8WAh8LBS08Zm9udCBjbGFzcz1TdW1tYXJ5UG9zaXRpdmVOdW1iZXI+MC4wMDwvZm9udD5kAg8PFgIfDAUJUGVuZGluZzogZAIRDxYCHwsFRTxmb250IGlkPSdsYmxQZW5kaW5nQW1vdW50JyBjbGFzcz1TdW1tYXJ5UG9zaXRpdmVOdW1iZXI+MTY1LjAwPC9mb250PmQCEw8WAh8NaBYEAgEPFgIfDAUJUGVuZGluZzogZAIDDxYCHwsFLTxmb250IGNsYXNz\" /><input type=\"hidden\" name=\"__VIEWSTATE6\" id=\"__VIEWSTATE6\" value=\"PVN1bW1hcnlQb3NpdGl2ZU51bWJlcj4wLjAwPC9mb250PmQYAQUeX19Db250cm9sc1JlcXVpcmVQb3N0QmFja0tleV9fFgUFFWN0bDAwJGNwaFdvcmtBcmVhJExheQUVY3RsMDAkY3BoV29ya0FyZWEkTGF5BRZjdGwwMCRjcGhXb3JrQXJlYSRUYWtlBRZjdGwwMCRjcGhXb3JrQXJlYSRUYWtlBRxjdGwwMCRjcGhXb3JrQXJlYSRCYXNlQW1vdW50unCr+Vj+4UF4LqwwD7qqCWmR7MQ=\" /></div><script type=\"text/javascript\">//<![CDATA[var theForm = document.forms['Form1'];if (!theForm) {    theForm = document.Form1;}function __doPostBack(eventTarget, eventArgument) {    if (!theForm.onsubmit || (theForm.onsubmit() != false)) {        theForm.__EVENTTARGET.value = eventTarget;        theForm.__EVENTARGUMENT.value = eventArgument;        theForm.submit();    }}//]]></script><script src=\"/WebResource.axd?d=Jb6kFaBFm7finf4QmAO8yFE6cC1fENxmf0aRhO2X0KK1MA0km4ExxopQJ0Skdaui19k5MhD4mGFeqKazEFKVjio58XE1&amp;t=635875293181218729\" type=\"text/javascript\"></script><script src=\"/ScriptResource.axd?d=Q1A8Qlz73lgJrOxSZ835LbeaaNyF0-ZUTZ1vRmsk7EgiRfBOBB9mUgl41mTN3zyvUogtmTFB8QBh19trlmhMbfJXDmwz-pqi6kkcsanHE6NcEXrkKyldrrTy3u9KrJk_twBEILD5rYJazip9TOf7ns_Z6tmbIPLu0DSmGlvErlAulses0&amp;t=ffffffffeea0dba9\" type=\"text/javascript\"></script><script type=\"text/javascript\">//<![CDATA[if (typeof(Sys) === 'undefined') throw new Error('ASP.NET Ajax client-side framework failed to load.');//]]></script><script src=\"/ScriptResource.axd?d=Yg3CyeT5W5dN3B81YusGU4ziDIY_Gme-bdTeIm_fRJ4ccBlvAxl16-cVvfsosWQ8S_5B8IZhu1ThkrPatj8WQUdeypYXElOfcnbLDsLPqHQ_a0z1_QPm9dtCQsAqwEL9L21NSZQUEd0SI9WbNDje7-FJS6eRnpqYkbZkI1ldGrpK86JP0&amp;t=ffffffffeea0dba9\" type=\"text/javascript\"></script><script type=\"text/javascript\">//<![CDATA[Type.registerNamespace('WTSoftwareWeb');WTSoftwareWeb.SessionTimeoutWS=function() {WTSoftwareWeb.SessionTimeoutWS.initializeBase(this);this._timeout = 0;this._userContext = null;this._succeeded = null;this._failed = null;}WTSoftwareWeb.SessionTimeoutWS.prototype={_get_path:function() { var p = this.get_path(); if (p) return p; else return WTSoftwareWeb.SessionTimeoutWS._staticInstance.get_path();},GetSessionTimeout:function(succeededCallback, failedCallback, userContext) {/// <param name=\"succeededCallback\" type=\"Function\" optional=\"true\" mayBeNull=\"true\"></param>/// <param name=\"failedCallback\" type=\"Function\" optional=\"true\" mayBeNull=\"true\"></param>/// <param name=\"userContext\" optional=\"true\" mayBeNull=\"true\"></param>return this._invoke(this._get_path(), 'GetSessionTimeout',false,{},succeededCallback,failedCallback,userContext); },RefreshSession:function(succeededCallback, failedCallback, userContext) {/// <param name=\"succeededCallback\" type=\"Function\" optional=\"true\" mayBeNull=\"true\"></param>/// <param name=\"failedCallback\" type=\"Function\" optional=\"true\" mayBeNull=\"true\"></param>/// <param name=\"userContext\" optional=\"true\" mayBeNull=\"true\"></param>return this._invoke(this._get_path(), 'RefreshSession',false,{},succeededCallback,failedCallback,userContext); }}WTSoftwareWeb.SessionTimeoutWS.registerClass('WTSoftwareWeb.SessionTimeoutWS',Sys.Net.WebServiceProxy);WTSoftwareWeb.SessionTimeoutWS._staticInstance = new WTSoftwareWeb.SessionTimeoutWS();WTSoftwareWeb.SessionTimeoutWS.set_path = function(value) {WTSoftwareWeb.SessionTimeoutWS._staticInstance.set_path(value); }WTSoftwareWeb.SessionTimeoutWS.get_path = function() { /// <value type=\"String\" mayBeNull=\"true\">The service url.</value>return WTSoftwareWeb.SessionTimeoutWS._staticInstance.get_path();}WTSoftwareWeb.SessionTimeoutWS.set_timeout = function(value) {WTSoftwareWeb.SessionTimeoutWS._staticInstance.set_timeout(value); }WTSoftwareWeb.SessionTimeoutWS.get_timeout = function() { /// <value type=\"Number\">The service timeout.</value>return WTSoftwareWeb.SessionTimeoutWS._staticInstance.get_timeout(); }WTSoftwareWeb.SessionTimeoutWS.set_defaultUserContext = function(value) { WTSoftwareWeb.SessionTimeoutWS._staticInstance.set_defaultUserContext(value); }WTSoftwareWeb.SessionTimeoutWS.get_defaultUserContext = function() { /// <value mayBeNull=\"true\">The service default user context.</value>return WTSoftwareWeb.SessionTimeoutWS._staticInstance.get_defaultUserContext(); }WTSoftwareWeb.SessionTimeoutWS.set_defaultSucceededCallback = function(value) {  WTSoftwareWeb.SessionTimeoutWS._staticInstance.set_defaultSucceededCallback(value); }WTSoftwareWeb.SessionTimeoutWS.get_defaultSucceededCallback = function() { /// <value type=\"Function\" mayBeNull=\"true\">The service default succeeded callback.</value>return WTSoftwareWeb.SessionTimeoutWS._staticInstance.get_defaultSucceededCallback(); }WTSoftwareWeb.SessionTimeoutWS.set_defaultFailedCallback = function(value) { WTSoftwareWeb.SessionTimeoutWS._staticInstance.set_defaultFailedCallback(value); }WTSoftwareWeb.SessionTimeoutWS.get_defaultFailedCallback = function() { /// <value type=\"Function\" mayBeNull=\"true\">The service default failed callback.</value>return WTSoftwareWeb.SessionTimeoutWS._staticInstance.get_defaultFailedCallback(); }WTSoftwareWeb.SessionTimeoutWS.set_enableJsonp = function(value) { WTSoftwareWeb.SessionTimeoutWS._staticInstance.set_enableJsonp(value); }WTSoftwareWeb.SessionTimeoutWS.get_enableJsonp = function() { /// <value type=\"Boolean\">Specifies whether the service supports JSONP for cross domain calling.</value>return WTSoftwareWeb.SessionTimeoutWS._staticInstance.get_enableJsonp(); }WTSoftwareWeb.SessionTimeoutWS.set_jsonpCallbackParameter = function(value) { WTSoftwareWeb.SessionTimeoutWS._staticInstance.set_jsonpCallbackParameter(value); }WTSoftwareWeb.SessionTimeoutWS.get_jsonpCallbackParameter = function() { /// <value type=\"String\">Specifies the parameter name that contains the callback function name for a JSONP request.</value>return WTSoftwareWeb.SessionTimeoutWS._staticInstance.get_jsonpCallbackParameter(); }WTSoftwareWeb.SessionTimeoutWS.set_path(\"/Services/SessionTimeoutWS.asmx\");WTSoftwareWeb.SessionTimeoutWS.GetSessionTimeout= function(onSuccess,onFailed,userContext) {/// <param name=\"succeededCallback\" type=\"Function\" optional=\"true\" mayBeNull=\"true\"></param>/// <param name=\"failedCallback\" type=\"Function\" optional=\"true\" mayBeNull=\"true\"></param>/// <param name=\"userContext\" optional=\"true\" mayBeNull=\"true\"></param>WTSoftwareWeb.SessionTimeoutWS._staticInstance.GetSessionTimeout(onSuccess,onFailed,userContext); }WTSoftwareWeb.SessionTimeoutWS.RefreshSession= function(onSuccess,onFailed,userContext) {/// <param name=\"succeededCallback\" type=\"Function\" optional=\"true\" mayBeNull=\"true\"></param>/// <param name=\"failedCallback\" type=\"Function\" optional=\"true\" mayBeNull=\"true\"></param>/// <param name=\"userContext\" optional=\"true\" mayBeNull=\"true\"></param>WTSoftwareWeb.SessionTimeoutWS._staticInstance.RefreshSession(onSuccess,onFailed,userContext); }var gtc = Sys.Net.WebServiceProxy._generateTypedConstructor;Type.registerNamespace('WTSoftwareWeb.Helpers.Entities');if (typeof(WTSoftwareWeb.Helpers.Entities.AjaxResultEntity) === 'undefined') {WTSoftwareWeb.Helpers.Entities.AjaxResultEntity=gtc(\"WTSoftwareWeb.Helpers.Entities.AjaxResultEntity\");WTSoftwareWeb.Helpers.Entities.AjaxResultEntity.registerClass('WTSoftwareWeb.Helpers.Entities.AjaxResultEntity');}//]]></script><div>	<input type=\"hidden\" name=\"__VIEWSTATEGENERATOR\" id=\"__VIEWSTATEGENERATOR\" value=\"518652EE\" />	<input type=\"hidden\" name=\"__PREVIOUSPAGE\" id=\"__PREVIOUSPAGE\" value=\"QqTsUtowXELBvKaVOigEbDIEwZ9Nr7nVSBEVwYELQ-fQYNYVLUTTBOlITDceONAF-C8BrYVLfSXJU4vNr530H4_3q_8kyd2X5tFyyxvjBPsnZurb0\" />	<input type=\"hidden\" name=\"__EVENTVALIDATION\" id=\"__EVENTVALIDATION\" value=\"/wEdAFco9fC7ul80Rf8mF1SX3SRYn1RcjtHAj66REed34CUNnsaLLJQKqpDkcgZwXU0uzVQR8bsmBVMgKg+JzwLlVOyd9e2hhEqHd00/lZns9Yg2+BqQcf4LX1C6Xt607UI9BpnSq7mmpngUSBF+gJx2K3Q4/adpv4eF8okDL930hGhz3JCvYoVqkjEtIckuWC/5ayyO+Su6BmWeGwqxaRXnuoz7OF03sgXiu5b0u3GlZwj/7Ev92f5Z3cIpibQtjCoiqYRdVY3WgAGJRaDCihwd9IuJDNZDKbmlWsSqUPpszQ6nHPncRS0pVBotugEdJPvbjdaEF7f8/FVMPBlCipaxlq5dTtfKFYU0MlLVZKPKWOw6tH5rt1XwxjLNipG+Q4MTNu0QOlLI/RRCkX3lO39DnOTD33Euj8uqF1vO0KB0Vs2yZe+O0KyBQcmvdGZ3b7BNW68RJtUUKIT/5q3QDFfTwKFGDsqTXzhZo1paLCw9A9382n+NLsMQyzKk2jBtHrHVA+cP3e3kbCnywglyUlA3HT7QkMcKQHBGIVAyM/UdWG6b9yQu0eXllT5jK+RedJllKsHc0BfHvmfLvta84msBqWuMqlMXQag7Jm7IU3WM2Kg9MOndV1WTUaxC10AJPUjgIsnCBcS7TtNtxzdXVlFMDmKbI9SnvvSaNxZYLE6Uu5526IRinIi6vxL1o5HJnh2/cYpn+4L7K0Xajze/wzOqUri+vXlT9bSl3UuDGrrpVxn2Tse9abx26iFZRD/FpgONkF95MelXFVaTyekwbfhspLMIDXM9JR+RR/8ZJxs1kdQWTYXmSCJnzP1MmKa/Wp8LWq75qtG38qpj3yeF5+shznrAAofk5VPeRxBOS3fbIVjNIf+rjuZzYM5AdX8qVrdwbZ2N6Nj1ETUvbmthWN2VjE0Is345+R0ffpl8JCBwABjk7xBRkGfg8U29603SS4u2NtostQgqfrrMNpkHWLkyd0D5XJedvvnSq5nd5fsr2XbkxEBLR5tYbE/+4F7soE7fXRPInh8jHN6W6uydAyZQDajm8MRerZp4BXytwUZMtmPLq784QdrMbBjlVyZ6zZdqvPxP0vQ9jR1xQ/Y+PxCenw5VsuWakkIvGBuwk1gS6nvGwczekU4uLSgfpR9nABs41AkgY3Szw5J0Y9e4qwRPHsYEqsFP06SEXxLS4PGbwu9CCYKCU2Rir3GGNuNBUThG6tWIeUueEIX5IeBJao0M2iKc5o5ie7ejVOi0c5yrnF6SQZjguJt9Id8qAJxz7QvFHBN4WaBG7Nh7sAgofTF+6/hUbWXiXuRyDvWF05AQtzUrS/jpL3seQ/wQnoFkVGbJjCCcS52Jeb8NDYjTU3Ey/nCqPYgfNL0aOIDF1l7CxsEmvBi9Tms5KqQmx3t82/E1/ZWa04Nm84ZNeTqUadtSpITBPqg1mP3sANwyzgKX68hUVDgMHp8XAFs8xwYoF70nFWHdLq/mf7I1h3BFCZ4CExTG4edgvI4+0ZmSs8qJMQH1zTPb+gKvIdKE9TguTjfIM6Ux253SfP6pTTAOl9dafHJK4amjVt2OCfR2jnjjhOn2SrBKpdnLpiIuVyfBOVdPhtY0jS0L/SYNyPB/air4J+FidXkb0t05O2PeEA4rrkvG4pzzW9DVgFX/YqUhhmrtrg9IQnM6iLwGuR3/zVYC4Asz3BEDegDBAfk9mQgk7GZLr2A9n4DLKPbhfTVxXvqo1W4sYwrXMYrkXtGmhsrxuWh5L5nzLde3x7I/yxe/TwXdTjAqR/3yZpo2PSdCM9i8HqhePe8DZ33I4j+e/1lDv+Drg51ZUVXc1ldOJI/lCbuLNuJ+MrDJV0orf5SLnU0lJuXU3/jq2lmKYCB7Uu8GDROgMCu+hw==\" /></div>    <script type=\"text/javascript\">//<![CDATA[Sys.WebForms.PageRequestManager._initialize('ctl00$ScriptManager1', 'Form1', [], [], [], 90, 'ctl00');//]]></script>		    <div class=\"main\">        <div class=\"header\">            <div class=\"logo_container\">								                <a class=\"logo\" href=\"#\">                                        <img id=\"mainLogoImage\" src=\"../Images/logo_msb247.png\" style=\"border-width:0px;\" />                </a>                            </div>						<div>				<div class=\"logoff\">                                        <input type=\"button\" name=\"ctl00$Button1\" value=\"Log Off\" onclick=\"javascript:WebForm_DoPostBackWithOptions(new WebForm_PostBackOptions(&quot;ctl00$Button1&quot;, &quot;&quot;, false, &quot;&quot;, &quot;../LogOff.aspx&quot;, false, true))\" id=\"Button1\" />                                        <p>                        <font>                            <span id=\"userName\" class=\"userName\">LB908</span>                        </font>                    </p>                    <span id=\"LabelUserID\">User ID:</span>                </div>			</div>						<div class=\"SummaryContainer\"><!--PLEASE, DON'T CHANGE ID PROPERTY OF HTML ELEMENTS       CAN YOU COMMENT OR DELETE SOMEBODY TAG HTML, BUT NOT CHANGE THE ID PROPERTY --><table class=\"SummaryAccountHeaderClient\" cellpadding=\"0\" cellspacing =\"0\">    <tr>                                                                                    <td class=\"tdSummaryAccountHeaderClient\">            <span id=\"LabelCustomerBalance\">Current Balance: </span><a href=\"/Client/DailyFigure.aspx\"><font class=SummaryNegativeNumber>-280.00</font></a>        </td>                                                                        </tr>									<tr>		<td class=\"tdSummaryAccountHeaderClient\">            <span id=\"LabelAvailable\">Available: </span><a href=\"/client/Account.aspx\"><font id='lblAvailableAmount' class=SummaryPositiveNumber>9,555.00</font></a>        </td>																							</tr>									<tr>		<td class=\"tdSummaryAccountHeaderClient\">            <span id=\"LabelPending\">Pending: </span><a href=\"../Client/PendingsWagers.aspx\"><font id='lblPendingAmount' class=SummaryPositiveNumber>165.00</font></a>        </td>													</tr>																</table></div>			            <div style=\"position:absolute;top:136px;right:6px;width:167px;z-index:999;display:none\">		<div id=\"google_translate_element\"></div><script type=\"text/javascript\">		                                             function googleTranslateElementInit() {		                                                 new google.translate.TranslateElement({ pageLanguage: 'en', includedLanguages: 'en,es,hi,it,ko,ru,vi,zh-CN,zh-TW', layout: google.translate.TranslateElement.InlineLayout.SIMPLE, autoDisplay: false }, 'google_translate_element');		                                             }</script><script type=\"text/javascript\" src=\"//translate.google.com/translate_a/element.js?cb=googleTranslateElementInit\"></script>                 </div>	    <div class=\"menu\">                <div>                    <div class=\"main_menu\">                        <ul>                                                                <li class='first active' id='btnSportsMenu'><a href='StraightBetMenuEx2.aspx'                                        onclick='' target=''>                                        Sports</a> <i></i></li>                                                                    <li class=' ' id='btnCasinoMenu'><a href='../Client/CasinoAST.aspx'                                        onclick='' target='_blank'>                                        Casino</a> <i></i></li>                                                                    <li class=' ' id='btnHorsesMenu'><a href='../Client/HorseWager.aspx'                                        onclick='' target=''>                                        Horses</a> <i></i></li>                                                                    <li class=' ' id='btnAccountMenu'><a href='DailyFigure.aspx'                                        onclick='' target=''>                                        Account</a> <i></i></li>                                                                    <li class=' ' id='btnPendingMenu'><a href='PendingsWagers.aspx'                                        onclick='' target=''>                                        Pending</a> <i></i></li>                                                                    <li class=' ' id='btnScoresMenu'><a href='http://scores.sportsoptions.com/scores/today.html'                                        onclick='' target='_blank'>                                        Scores</a> <i></i></li>                                                                    <li class=' ' id='btnRulesMenu'><a href='../Client/Rules.aspx'                                        onclick='' target=''>                                        Rules</a> <i></i></li>                                                            														                        </ul>                    </div>                </div>                           <div id=\"subMenuContainer\">        <div class=\"sportsSubmenu\">                        <div class=\"bg1\">                <div class=\"bg2\">                    <ul>                                                                                     <li id='current'><a class='red' href='StraightBetMenuEx2.aspx' onclick=''>                                        Straights</a> <i class=''></i></li>                                                                    <li id=''><a class='' href='ParlayBetMenuEx2.aspx' onclick=''>                                        Parlays</a> <i class=''></i></li>                                                                    <li id=''><a class='' href='TeaserBetMenuEx2.aspx' onclick=''>                                        Teasers</a> <i class=''></i></li>                                                                    <li id=''><a class='' href='IfBetReverseMenuEx2.aspx' onclick=''>                                        If Bets / Reverses</a> <i class='Invisible'></i></li>                                                    </ul>                </div>            </div>        </div></div>                                        <div id=\"headerWorkPlace\">        <div class=\"headerContinueSearch\">            <input id=\"txtSearch\" name=\"txtAITGeneralSearch\" type=\"text\"                onblur=\"if (this.value==''){this.value='Search...'}\" onfocus=\"if (this.value=='Search...') this.value='';\"                value=\"Search...\" />            <input type=\"submit\" name=\"ctl00$headerWorkPlace$cmdSearch\" value=\"Search\" onclick=\"javascript:__doPostBack(&#39;ctl00$headerWorkPlace$cmdSearch&#39;,&#39;&#39;);\" id=\"cmdSearch\" name=\"cmdSearch\" />        </div>        <div style=\"text-align: right; margin: 15px;\">            <span id=\"lblAitInvalidSearch\" style=\"padding-right: 10px;\" class=\"InvalidSearch\"></span>        </div>    </div>            </div>        </div>        <div class=\"content_holder\">                <span id=\"lblInfoPrincipal\"><input id='_hiddenCWS' name='_hiddenCWS' value='fc3954ab-93ce-43ae-b889-4fd68f1dafa5' type='hidden' /><input id='txtIdLastGameChange' name='txtIdLastGameChange' value='1032513435' type='hidden' /><input id='txtIdLastShadeChange' name='txtIdLastShadeChange' value='323832' type='hidden' /><input name=\"ctl00$cphWorkArea$txtPathContest\" type=\"hidden\" id=\"txtPathContest\" /><input name=\"ctl00$cphWorkArea$txtWagerType\" type=\"hidden\" id=\"txtWagerType\" value=\"1\" /><table class=\"TableHeaderControls\" border=\"0\">	<tr>		<td class=\"TableHeaderControlsCellWagerAmount\"><table class=\"tblWagerAmount\" border=\"0\">			<tr>				<td align=\"left\">Wager Amount:</td><td align=\"left\"><input id=\"Lay\" type=\"radio\" name=\"ctl00$cphWorkArea$amountType\" value=\"Lay\" /><label for=\"Lay\">Risk</label></td><td align=\"left\"><input id=\"Take\" type=\"radio\" name=\"ctl00$cphWorkArea$amountType\" value=\"Take\" /><label for=\"Take\">Win</label></td><td align=\"left\"><input id=\"BaseAmount\" type=\"radio\" name=\"ctl00$cphWorkArea$amountType\" value=\"BaseAmount\" checked=\"checked\" /><label for=\"BaseAmount\">Base Amount</label></td>			</tr>		</table></td><td class=\"TableHeaderControlsCellButtonHeader\"><table border=\"0\">			<tr>				<td align=\"left\"><input type=\"button\" name=\"ctl00$cphWorkArea$ctl06\" value=\"Main Menu\" onclick=\"javascript:WebForm_DoPostBackWithOptions(new WebForm_PostBackOptions(&quot;ctl00$cphWorkArea$ctl06&quot;, &quot;&quot;, false, &quot;&quot;, &quot;StraightBetMenuEx2.aspx&quot;, false, true))\" id=\"\" class=\"Main-Menu-and-Refresh\" runat=\"server\" /></td><td align=\"left\"><input type=\"button\" name=\"ctl00$cphWorkArea$ctl07\" value=\"Refresh\" onclick=\"javascript:WebForm_DoPostBackWithOptions(new WebForm_PostBackOptions(&quot;ctl00$cphWorkArea$ctl07&quot;, &quot;&quot;, false, &quot;&quot;, &quot;StraightBet.aspx&quot;, false, true))\" id=\"\" class=\"Main-Menu-and-Refresh\" runat=\"server\" /></td>			</tr>		</table></td>	</tr></table><table id=\"tblcc\" class=\"TableWager\" border=\"0\">	<tr>		<td class=\"HeaderTableLine\" colspan=\"7\"><Div><Div class=\"cellSportHeader\">College Basketball<br>Sunday, December 18, 2016</Div><Div class=\"submitwager\"><input type=\"submit\" name=\"ctl00$cphWorkArea$ctl11\" value=\"Continue\" onclick=\"javascript:WebForm_DoPostBackWithOptions(new WebForm_PostBackOptions(&quot;ctl00$cphWorkArea$ctl11&quot;, &quot;&quot;, false, &quot;&quot;, &quot;WagerTicket.aspx&quot;, false, false))\" id=\"\" title=\"go to see tickets wager\" class=\"Submitbutton\" runat=\"server\" /></Div></Div></td>	</tr><tr class=\"HeaderColumnsName\">		<td class=\"thHeaderPeriod\" colspan=\"3\">Game</td><td class=\"tdHeaderSpread\">Spread $500</td><td class=\"tdHeaderMoneyLine\">ML $500</td><td class=\"tdHeaderTotal\">Total $500</td><td class=\"tdHeaderTeamTotal\">Team Totals $500</td>	</tr><tr class=\"BackgroundLineGray\">		<td class=\"tdDate \">12/18</td><td class=\"tdTelevised \"><img id=\"\" src=\"../Images/blank.png\" style=\"border-width:0px;\" /></td><td class=\"tdTeamName \"><span class='rotation_number'>515</span><span class='team_name'>Miami Ohio</span></td><td class=\"tdSpread \"><table class=\"TableSpread\" border=\"0\">			<tr class=\"tr1\">				<td class=\"td1\"><img id=\"\" src=\"../Images/blank.png\" style=\"border-width:0px;\" /></td><td class=\"td2\"><input name=\"ctl00$cphWorkArea$Amt-660114-1-1-2-0-1\" type=\"text\" id=\"Amt-660114-1-1-2-0-1\" class=\"textBox onlyPositiveNumber\" onkeypress=\"return validateAmountWager(event)\" /></td><td class=\"td3\"><select name=\"ctl00$cphWorkArea$Cbo-660114-1-1-2-0-1\" id=\"Cbo-660114-1-1-2-0-1\" class=\"cboOdds cboLines\" State=\"0\" for=\"VVbivr60rv6f8uxr8kwtT__ImaVAQeChmX6KrXVv1g__9J3NFbgg6sFjxbdR4YtXEI\">					<option selected=\"selected\" value=\"0\" class=\"RadComboBoxDefaultItem SelItem\">+12 -110</option>				</select></td>			</tr>		</table></td><td class=\"tdMoneyLine \"><table class=\"TableMoneyLine\" border=\"0\">		</table></td><td class=\"tdTotal \"><table class=\"TableTotals\" border=\"0\">			<tr class=\"tr1\">				<td class=\"td1\"><img id=\"\" src=\"../Images/blank.png\" style=\"border-width:0px;\" /></td><td class=\"td2\"><input name=\"ctl00$cphWorkArea$Amt-660114-1-1-3-0-1\" type=\"text\" id=\"Amt-660114-1-1-3-0-1\" class=\"textBox onlyPositiveNumber\" onkeypress=\"return validateAmountWager(event)\" /></td><td class=\"td3\"><select name=\"ctl00$cphWorkArea$Cbo-660114-1-1-3-0-1\" id=\"Cbo-660114-1-1-3-0-1\" class=\"cboTotalOdds cboLines\" State=\"0\" for=\"VVbivr60rv6f8uxr8kwtT/U4pJ8pwLVn23m5Bd36rR6MDx9B8Aa55RmiMzrG2Qkh\">					<option selected=\"selected\" value=\"0\" class=\"RadComboBoxDefaultItem SelItem\">O 129&#189; -110</option>				</select></td>			</tr>		</table></td><td class=\"tdTeamTotal \"><table class=\"TableTeamTotal\" border=\"0\">			<tr>				<td class=\"td1\"><input name=\"ctl00$cphWorkArea$Amt-660114-1-1-8\" type=\"text\" id=\"Amt-660114-1-1-8\" class=\"textBox onlyPositiveNumber\" onkeypress=\"return validateAmountWager(event)\" />&nbsp;&nbsp;<font class='RadComboBoxItem'>O 59 -115</font></td><td class=\"td2\"><input name=\"ctl00$cphWorkArea$Amt-660114-1-1-9\" type=\"text\" id=\"Amt-660114-1-1-9\" class=\"textBox onlyPositiveNumber\" onkeypress=\"return validateAmountWager(event)\" />&nbsp;&nbsp;<font class='RadComboBoxItem'>U 59 -115</font></td>			</tr>		</table></td>	</tr><tr class=\"BackgroundLineGray lastRow\">		<td class=\"tdDate \">12:00 PM</td><td class=\"tdTelevised \"></td><td class=\"tdTeamName \"><span class='rotation_number'>516</span><span class='team_name'>Central Florida</span></td><td class=\"tdSpread \"><table class=\"TableSpread\" border=\"0\">			<tr class=\"tr2\">				<td class=\"td1\"><img id=\"\" src=\"../Images/blank.png\" style=\"border-width:0px;\" /></td><td class=\"td2\"><input name=\"ctl00$cphWorkArea$Amt-660114-1-2-2-0-1\" type=\"text\" id=\"Amt-660114-1-2-2-0-1\" class=\"textBox onlyPositiveNumber\" onkeypress=\"return validateAmountWager(event)\" /></td><td class=\"td3\"><select name=\"ctl00$cphWorkArea$Cbo-660114-1-2-2-0-1\" id=\"Cbo-660114-1-2-2-0-1\" class=\"cboOdds cboLines\" State=\"0\" for=\"VVbivr60rv6f8uxr8kwtTzRntUyInqFM2g__altUcFOUIWNTOEztLfnuOW5PJlDtW\">					<option selected=\"selected\" value=\"0\" class=\"RadComboBoxDefaultItem SelItem\">-12 -110</option>				</select></td>			</tr>		</table></td><td class=\"tdMoneyLine \"><table class=\"TableMoneyLine\" border=\"0\">		</table></td><td class=\"tdTotal \"><table class=\"TableTotals\" border=\"0\">			<tr class=\"tr2\">				<td class=\"td1\"><img id=\"\" src=\"../Images/blank.png\" style=\"border-width:0px;\" /></td><td class=\"td2\"><input name=\"ctl00$cphWorkArea$Amt-660114-1-2-4-0-1\" type=\"text\" id=\"Amt-660114-1-2-4-0-1\" class=\"textBox onlyPositiveNumber\" onkeypress=\"return validateAmountWager(event)\" /></td><td class=\"td3\"><select name=\"ctl00$cphWorkArea$Cbo-660114-1-2-4-0-1\" id=\"Cbo-660114-1-2-4-0-1\" class=\"cboTotalOdds cboLines\" State=\"0\" for=\"VVbivr60rv6f8uxr8kwtT4oLDSyyuSjlal0krVRvNiPrakAmU4mwGy9hpoqk3SBg\">					<option selected=\"selected\" value=\"0\" class=\"RadComboBoxDefaultItem SelItem\">U 129&#189; -110</option>				</select></td>			</tr>		</table></td><td class=\"tdTeamTotal \"><table class=\"TableTeamTotal\" border=\"0\">			<tr>				<td class=\"td1\"><input name=\"ctl00$cphWorkArea$Amt-660114-1-2-8\" type=\"text\" id=\"Amt-660114-1-2-8\" class=\"textBox onlyPositiveNumber\" onkeypress=\"return validateAmountWager(event)\" />&nbsp;&nbsp;<font class='RadComboBoxItem'>O 71 -115</font></td><td class=\"td2\"><input name=\"ctl00$cphWorkArea$Amt-660114-1-2-9\" type=\"text\" id=\"Amt-660114-1-2-9\" class=\"textBox onlyPositiveNumber\" onkeypress=\"return validateAmountWager(event)\" />&nbsp;&nbsp;<font class='RadComboBoxItem'>U 71 -115</font></td>			</tr>		</table></td>	</tr><tr class=\"BackgroundLineAlternateGray\">		<td class=\"tdDate \">12/18</td><td class=\"tdTelevised \"><img id=\"\" src=\"../Images/blank.png\" style=\"border-width:0px;\" /></td><td class=\"tdTeamName \"><span class='rotation_number'>517</span><span class='team_name'>Georgia State</span></td><td class=\"tdSpread \"><table class=\"TableSpread\" border=\"0\">			<tr class=\"tr1\">				<td class=\"td1\"><img id=\"\" src=\"../Images/blank.png\" style=\"border-width:0px;\" /></td><td class=\"td2\"><input name=\"ctl00$cphWorkArea$Amt-660115-1-1-2-0-1\" type=\"text\" id=\"Amt-660115-1-1-2-0-1\" class=\"textBox onlyPositiveNumber\" onkeypress=\"return validateAmountWager(event)\" /></td><td class=\"td3\"><select name=\"ctl00$cphWorkArea$Cbo-660115-1-1-2-0-1\" id=\"Cbo-660115-1-1-2-0-1\" class=\"cboOdds cboLines\" State=\"0\" for=\"VVbivr60rv6f8uxr8kwtT1whGfMsJQxTiIq7pLsKN16SiwgRIH24rGlCeh8F3/bv\">					<option selected=\"selected\" value=\"0\" class=\"RadComboBoxDefaultItem SelItem\">+5&#189; -110</option>				</select></td>			</tr>		</table></td><td class=\"tdMoneyLine \"><table class=\"TableMoneyLine\" border=\"0\">			<tr class=\"tr1\">				<td class=\"td1\"><img id=\"\" src=\"../Images/blank.png\" style=\"border-width:0px;\" /></td><td class=\"td2\"><input name=\"ctl00$cphWorkArea$Amt-660115-1-1-1\" type=\"text\" id=\"Amt-660115-1-1-1\" class=\"textBox\" onkeypress=\"return validateAmountWager(event)\" title=\"Limit|500.00\" /></td><td class=\"td3\"><font class='RadComboBoxItem'>+200</font></td>			</tr>		</table></td><td class=\"tdTotal \"><table class=\"TableTotals\" border=\"0\">		</table></td><td class=\"tdTeamTotal \"><table class=\"TableTeamTotal\" border=\"0\">		</table></td>	</tr><tr class=\"BackgroundLineAlternateGray lastRow\">		<td class=\"tdDate \">2:00 PM</td><td class=\"tdTelevised \"></td><td class=\"tdTeamName \"><span class='rotation_number'>518</span><span class='team_name'>Old Dominion</span></td><td class=\"tdSpread \"><table class=\"TableSpread\" border=\"0\">			<tr class=\"tr2\">				<td class=\"td1\"><img id=\"\" src=\"../Images/blank.png\" style=\"border-width:0px;\" /></td><td class=\"td2\"><input name=\"ctl00$cphWorkArea$Amt-660115-1-2-2-0-1\" type=\"text\" id=\"Amt-660115-1-2-2-0-1\" class=\"textBox onlyPositiveNumber\" onkeypress=\"return validateAmountWager(event)\" /></td><td class=\"td3\"><select name=\"ctl00$cphWorkArea$Cbo-660115-1-2-2-0-1\" id=\"Cbo-660115-1-2-2-0-1\" class=\"cboOdds cboLines\" State=\"0\" for=\"VVbivr60rv6f8uxr8kwtT8r1cB6N/0gMIBmwdyftZTjHxXecYXao__kmyGjIYtvb3\">					<option selected=\"selected\" value=\"0\" class=\"RadComboBoxDefaultItem SelItem\">-5&#189; -110</option>				</select></td>			</tr>		</table></td><td class=\"tdMoneyLine \"><table class=\"TableMoneyLine\" border=\"0\">			<tr class=\"tr2\">				<td class=\"td1\"><img id=\"\" src=\"../Images/blank.png\" style=\"border-width:0px;\" /></td><td class=\"td2\"><input name=\"ctl00$cphWorkArea$Amt-660115-1-2-1\" type=\"text\" id=\"Amt-660115-1-2-1\" class=\"textBox\" onkeypress=\"return validateAmountWager(event)\" title=\"Limit|500.00\" /></td><td class=\"td3\"><font class='RadComboBoxItem'>-240</font></td>			</tr>		</table></td><td class=\"tdTotal \"><table class=\"TableTotals\" border=\"0\">		</table></td><td class=\"tdTeamTotal \"><table class=\"TableTeamTotal\" border=\"0\">		</table></td>	</tr><tr class=\"BackgroundLineGray\">		<td class=\"tdDate \">12/18</td><td class=\"tdTelevised \"><img id=\"\" src=\"../Images/blank.png\" style=\"border-width:0px;\" /></td><td class=\"tdTeamName \"><span class='rotation_number'>519</span><span class='team_name'>Wright State</span></td><td class=\"tdSpread \"><table class=\"TableSpread\" border=\"0\">			<tr class=\"tr1\">				<td class=\"td1\"><img id=\"\" src=\"../Images/blank.png\" style=\"border-width:0px;\" /></td><td class=\"td2\"><input name=\"ctl00$cphWorkArea$Amt-660116-1-1-2-0-1\" type=\"text\" id=\"Amt-660116-1-1-2-0-1\" class=\"textBox onlyPositiveNumber\" onkeypress=\"return validateAmountWager(event)\" /></td><td class=\"td3\"><select name=\"ctl00$cphWorkArea$Cbo-660116-1-1-2-0-1\" id=\"Cbo-660116-1-1-2-0-1\" class=\"cboOdds cboLines\" State=\"0\" for=\"VVbivr60rv6f8uxr8kwtT5Yze8KOsvE6TFsskd7Rcbj3OTmAeJ__GYDiOBLKkW2QC\">					<option selected=\"selected\" value=\"0\" class=\"RadComboBoxDefaultItem SelItem\">+8 -110</option>				</select></td>			</tr>		</table></td><td class=\"tdMoneyLine \"><table class=\"TableMoneyLine\" border=\"0\">			<tr class=\"tr1\">				<td class=\"td1\"><img id=\"\" src=\"../Images/blank.png\" style=\"border-width:0px;\" /></td><td class=\"td2\"><input name=\"ctl00$cphWorkArea$Amt-660116-1-1-1\" type=\"text\" id=\"Amt-660116-1-1-1\" class=\"textBox\" onkeypress=\"return validateAmountWager(event)\" title=\"Limit|500.00\" /></td><td class=\"td3\"><font class='RadComboBoxItem'>+305</font></td>			</tr>		</table></td><td class=\"tdTotal \"><table class=\"TableTotals\" border=\"0\">		</table></td><td class=\"tdTeamTotal \"><table class=\"TableTeamTotal\" border=\"0\">		</table></td>	</tr><tr class=\"BackgroundLineGray lastRow\">		<td class=\"tdDate \">4:00 PM</td><td class=\"tdTelevised \"></td><td class=\"tdTeamName \"><span class='rotation_number'>520</span><span class='team_name'>Kent State</span></td><td class=\"tdSpread \"><table class=\"TableSpread\" border=\"0\">			<tr class=\"tr2\">				<td class=\"td1\"><img id=\"\" src=\"../Images/blank.png\" style=\"border-width:0px;\" /></td><td class=\"td2\"><input name=\"ctl00$cphWorkArea$Amt-660116-1-2-2-0-1\" type=\"text\" id=\"Amt-660116-1-2-2-0-1\" class=\"textBox onlyPositiveNumber\" onkeypress=\"return validateAmountWager(event)\" /></td><td class=\"td3\"><select name=\"ctl00$cphWorkArea$Cbo-660116-1-2-2-0-1\" id=\"Cbo-660116-1-2-2-0-1\" class=\"cboOdds cboLines\" State=\"0\" for=\"VVbivr60rv6f8uxr8kwtT5q/rg__fD7RhXF0oCQB0CIGEXThh1s0LFHM3JHwKfEfA\">					<option selected=\"selected\" value=\"0\" class=\"RadComboBoxDefaultItem SelItem\">-8 -110</option>				</select></td>			</tr>		</table></td><td class=\"tdMoneyLine \"><table class=\"TableMoneyLine\" border=\"0\">			<tr class=\"tr2\">				<td class=\"td1\"><img id=\"\" src=\"../Images/blank.png\" style=\"border-width:0px;\" /></td><td class=\"td2\"><input name=\"ctl00$cphWorkArea$Amt-660116-1-2-1\" type=\"text\" id=\"Amt-660116-1-2-1\" class=\"textBox\" onkeypress=\"return validateAmountWager(event)\" title=\"Limit|500.00\" /></td><td class=\"td3\"><font class='RadComboBoxItem'>-385</font></td>			</tr>		</table></td><td class=\"tdTotal \"><table class=\"TableTotals\" border=\"0\">		</table></td><td class=\"tdTeamTotal \"><table class=\"TableTeamTotal\" border=\"0\">		</table></td>	</tr><tr class=\"BackgroundLineAlternateGray\">		<td class=\"tdDate \">12/18</td><td class=\"tdTelevised \"><img id=\"\" src=\"../Images/blank.png\" style=\"border-width:0px;\" /></td><td class=\"tdTeamName \"><span class='rotation_number'>521</span><span class='team_name'>St. Joseph`s</span></td><td class=\"tdSpread \"><table class=\"TableSpread\" border=\"0\">			<tr class=\"tr1\">				<td class=\"td1\"><img id=\"\" src=\"../Images/blank.png\" style=\"border-width:0px;\" /></td><td class=\"td2\"><input name=\"ctl00$cphWorkArea$Amt-660117-1-1-2-0-1\" type=\"text\" id=\"Amt-660117-1-1-2-0-1\" class=\"textBox onlyPositiveNumber\" onkeypress=\"return validateAmountWager(event)\" /></td><td class=\"td3\"><select name=\"ctl00$cphWorkArea$Cbo-660117-1-1-2-0-1\" id=\"Cbo-660117-1-1-2-0-1\" class=\"cboOdds cboLines\" State=\"0\" for=\"VVbivr60rv6f8uxr8kwtT8X4qpLoXIbDwYf2E5ZRFrcw7IXMvD__zAgRLmBHbA1KW\">					<option selected=\"selected\" value=\"0\" class=\"RadComboBoxDefaultItem SelItem\">+5&#189; -110</option>				</select></td>			</tr>		</table></td><td class=\"tdMoneyLine \"><table class=\"TableMoneyLine\" border=\"0\">			<tr class=\"tr1\">				<td class=\"td1\"><img id=\"\" src=\"../Images/blank.png\" style=\"border-width:0px;\" /></td><td class=\"td2\"><input name=\"ctl00$cphWorkArea$Amt-660117-1-1-1\" type=\"text\" id=\"Amt-660117-1-1-1\" class=\"textBox\" onkeypress=\"return validateAmountWager(event)\" title=\"Limit|500.00\" /></td><td class=\"td3\"><font class='RadComboBoxItem'>+205</font></td>			</tr>		</table></td><td class=\"tdTotal \"><table class=\"TableTotals\" border=\"0\">		</table></td><td class=\"tdTeamTotal \"><table class=\"TableTeamTotal\" border=\"0\">		</table></td>	</tr><tr class=\"BackgroundLineAlternateGray lastRow\">		<td class=\"tdDate \">4:00 PM</td><td class=\"tdTelevised \"></td><td class=\"tdTeamName \"><span class='rotation_number'>522</span><span class='team_name'>Illinois State</span></td><td class=\"tdSpread \"><table class=\"TableSpread\" border=\"0\">			<tr class=\"tr2\">				<td class=\"td1\"><img id=\"\" src=\"../Images/blank.png\" style=\"border-width:0px;\" /></td><td class=\"td2\"><input name=\"ctl00$cphWorkArea$Amt-660117-1-2-2-0-1\" type=\"text\" id=\"Amt-660117-1-2-2-0-1\" class=\"textBox onlyPositiveNumber\" onkeypress=\"return validateAmountWager(event)\" /></td><td class=\"td3\"><select name=\"ctl00$cphWorkArea$Cbo-660117-1-2-2-0-1\" id=\"Cbo-660117-1-2-2-0-1\" class=\"cboOdds cboLines\" State=\"0\" for=\"VVbivr60rv6f8uxr8kwtTwVCRB8FbmVxzLVokSUMxIhkM6GVi7io05__d4YJ57vQG\">					<option selected=\"selected\" value=\"0\" class=\"RadComboBoxDefaultItem SelItem\">-5&#189; -110</option>				</select></td>			</tr>		</table></td><td class=\"tdMoneyLine \"><table class=\"TableMoneyLine\" border=\"0\">			<tr class=\"tr2\">				<td class=\"td1\"><img id=\"\" src=\"../Images/blank.png\" style=\"border-width:0px;\" /></td><td class=\"td2\"><input name=\"ctl00$cphWorkArea$Amt-660117-1-2-1\" type=\"text\" id=\"Amt-660117-1-2-1\" class=\"textBox\" onkeypress=\"return validateAmountWager(event)\" title=\"Limit|500.00\" /></td><td class=\"td3\"><font class='RadComboBoxItem'>-255</font></td>			</tr>		</table></td><td class=\"tdTotal \"><table class=\"TableTotals\" border=\"0\">		</table></td><td class=\"tdTeamTotal \"><table class=\"TableTeamTotal\" border=\"0\">		</table></td>	</tr><tr class=\"BackgroundLineGray\">		<td class=\"tdDate \">12/18</td><td class=\"tdTelevised \"><img id=\"\" src=\"../Images/blank.png\" style=\"border-width:0px;\" /></td><td class=\"tdTeamName \"><span class='rotation_number'>523</span><span class='team_name'>Clemson</span></td><td class=\"tdSpread \"><table class=\"TableSpread\" border=\"0\">			<tr class=\"tr1\">				<td class=\"td1\"><img id=\"\" src=\"../Images/blank.png\" style=\"border-width:0px;\" /></td><td class=\"td2\"><input name=\"ctl00$cphWorkArea$Amt-660118-1-1-2-0-1\" type=\"text\" id=\"Amt-660118-1-1-2-0-1\" class=\"textBox onlyPositiveNumber\" onkeypress=\"return validateAmountWager(event)\" /></td><td class=\"td3\"><select name=\"ctl00$cphWorkArea$Cbo-660118-1-1-2-0-1\" id=\"Cbo-660118-1-1-2-0-1\" class=\"cboOdds cboLines\" State=\"0\" for=\"VVbivr60rv6f8uxr8kwtT6MdGhf/4VYFhiO48AtUShyr6sIbppdiR6veWwm9E5qQ\">					<option selected=\"selected\" value=\"0\" class=\"RadComboBoxDefaultItem SelItem\">-4&#189; -110</option>				</select></td>			</tr>		</table></td><td class=\"tdMoneyLine \"><table class=\"TableMoneyLine\" border=\"0\">			<tr class=\"tr1\">				<td class=\"td1\"><img id=\"\" src=\"../Images/blank.png\" style=\"border-width:0px;\" /></td><td class=\"td2\"><input name=\"ctl00$cphWorkArea$Amt-660118-1-1-1\" type=\"text\" id=\"Amt-660118-1-1-1\" class=\"textBox\" onkeypress=\"return validateAmountWager(event)\" title=\"Limit|500.00\" /></td><td class=\"td3\"><font class='RadComboBoxItem'>-205</font></td>			</tr>		</table></td><td class=\"tdTotal \"><table class=\"TableTotals\" border=\"0\">		</table></td><td class=\"tdTeamTotal \"><table class=\"TableTeamTotal\" border=\"0\">		</table></td>	</tr><tr class=\"BackgroundLineGray\">		<td class=\"tdDate \">4:00 PM</td><td class=\"tdTelevised \"></td><td class=\"tdTeamName \"><span class='rotation_number'>524</span><span class='team_name'>Alabama</span></td><td class=\"tdSpread \"><table class=\"TableSpread\" border=\"0\">			<tr class=\"tr2\">				<td class=\"td1\"><img id=\"\" src=\"../Images/blank.png\" style=\"border-width:0px;\" /></td><td class=\"td2\"><input name=\"ctl00$cphWorkArea$Amt-660118-1-2-2-0-1\" type=\"text\" id=\"Amt-660118-1-2-2-0-1\" class=\"textBox onlyPositiveNumber\" onkeypress=\"return validateAmountWager(event)\" /></td><td class=\"td3\"><select name=\"ctl00$cphWorkArea$Cbo-660118-1-2-2-0-1\" id=\"Cbo-660118-1-2-2-0-1\" class=\"cboOdds cboLines\" State=\"0\" for=\"VVbivr60rv6f8uxr8kwtT4NTueWxqYiQbsY7j3UTXK4Lyw1elcRUnS__yAhh4u41r\">					<option selected=\"selected\" value=\"0\" class=\"RadComboBoxDefaultItem SelItem\">+4&#189; -110</option>				</select></td>			</tr>		</table></td><td class=\"tdMoneyLine \"><table class=\"TableMoneyLine\" border=\"0\">			<tr class=\"tr2\">				<td class=\"td1\"><img id=\"\" src=\"../Images/blank.png\" style=\"border-width:0px;\" /></td><td class=\"td2\"><input name=\"ctl00$cphWorkArea$Amt-660118-1-2-1\" type=\"text\" id=\"Amt-660118-1-2-1\" class=\"textBox\" onkeypress=\"return validateAmountWager(event)\" title=\"Limit|500.00\" /></td><td class=\"td3\"><font class='RadComboBoxItem'>+175</font></td>			</tr>		</table></td><td class=\"tdTotal \"><table class=\"TableTotals\" border=\"0\">		</table></td><td class=\"tdTeamTotal \"><table class=\"TableTeamTotal\" border=\"0\">		</table></td>	</tr><tr class=\"BackgroundLineGray lastRow\">		<td class=\"tdDate\" colspan=\"2\"></td><td class=\"tdWagerTeamComments\">Game played @ Legacy Arena - Birmingham, AL</td><td class=\"tdSpread\"></td><td class=\"tdMoneyLine\"></td><td class=\"tdTotal\"></td><td class=\"tdTeamTotal\"></td>	</tr><tr class=\"BackgroundLineAlternateGray\">		<td class=\"tdDate \">12/18</td><td class=\"tdTelevised \"><img id=\"\" src=\"../Images/blank.png\" style=\"border-width:0px;\" /></td><td class=\"tdTeamName \"><span class='rotation_number'>525</span><span class='team_name'>Gonzaga</span></td><td class=\"tdSpread \"><table class=\"TableSpread\" border=\"0\">			<tr class=\"tr1\">				<td class=\"td1\"><img id=\"\" src=\"../Images/blank.png\" style=\"border-width:0px;\" /></td><td class=\"td2\"><input name=\"ctl00$cphWorkArea$Amt-660119-1-1-2-0-1\" type=\"text\" id=\"Amt-660119-1-1-2-0-1\" class=\"textBox onlyPositiveNumber\" onkeypress=\"return validateAmountWager(event)\" /></td><td class=\"td3\"><select name=\"ctl00$cphWorkArea$Cbo-660119-1-1-2-0-1\" id=\"Cbo-660119-1-1-2-0-1\" class=\"cboOdds cboLines\" State=\"0\" for=\"VVbivr60rv6f8uxr8kwtTw4N9BUTb4j0Kq3wA6FzS3fH7dPI5pb6HfCbi00xFfk__\">					<option selected=\"selected\" value=\"0\" class=\"RadComboBoxDefaultItem SelItem\">-9 -110</option>				</select></td>			</tr>		</table></td><td class=\"tdMoneyLine \"><table class=\"TableMoneyLine\" border=\"0\">		</table></td><td class=\"tdTotal \"><table class=\"TableTotals\" border=\"0\">		</table></td><td class=\"tdTeamTotal \"><table class=\"TableTeamTotal\" border=\"0\">		</table></td>	</tr><tr class=\"BackgroundLineAlternateGray\">		<td class=\"tdDate \">4:00 PM</td><td class=\"tdTelevised \"></td><td class=\"tdTeamName \"><span class='rotation_number'>526</span><span class='team_name'>Tennessee</span></td><td class=\"tdSpread \"><table class=\"TableSpread\" border=\"0\">			<tr class=\"tr2\">				<td class=\"td1\"><img id=\"\" src=\"../Images/blank.png\" style=\"border-width:0px;\" /></td><td class=\"td2\"><input name=\"ctl00$cphWorkArea$Amt-660119-1-2-2-0-1\" type=\"text\" id=\"Amt-660119-1-2-2-0-1\" class=\"textBox onlyPositiveNumber\" onkeypress=\"return validateAmountWager(event)\" /></td><td class=\"td3\"><select name=\"ctl00$cphWorkArea$Cbo-660119-1-2-2-0-1\" id=\"Cbo-660119-1-2-2-0-1\" class=\"cboOdds cboLines\" State=\"0\" for=\"VVbivr60rv6f8uxr8kwtT8YeS1__cOfy48u/vgYt18XdP8vL7aexCPDGF__ihTY9ub\">					<option selected=\"selected\" value=\"0\" class=\"RadComboBoxDefaultItem SelItem\">+9 -110</option>				</select></td>			</tr>		</table></td><td class=\"tdMoneyLine \"><table class=\"TableMoneyLine\" border=\"0\">		</table></td><td class=\"tdTotal \"><table class=\"TableTotals\" border=\"0\">		</table></td><td class=\"tdTeamTotal \"><table class=\"TableTeamTotal\" border=\"0\">		</table></td>	</tr><tr class=\"BackgroundLineAlternateGray lastRow\">		<td class=\"tdDate\" colspan=\"2\"></td><td class=\"tdWagerTeamComments\">Game played @ Bridgestone Arena - Nashville, TN</td><td class=\"tdSpread\"></td><td class=\"tdMoneyLine\"></td><td class=\"tdTotal\"></td><td class=\"tdTeamTotal\"></td>	</tr><tr class=\"BackgroundLineGray\">		<td class=\"tdDate \">12/18</td><td class=\"tdTelevised \"><img id=\"\" src=\"../Images/blank.png\" style=\"border-width:0px;\" /></td><td class=\"tdTeamName \"><span class='rotation_number'>527</span><span class='team_name'>Bowling Green</span></td><td class=\"tdSpread \"><table class=\"TableSpread\" border=\"0\">			<tr class=\"tr1\">				<td class=\"td1\"><img id=\"\" src=\"../Images/blank.png\" style=\"border-width:0px;\" /></td><td class=\"td2\"><input name=\"ctl00$cphWorkArea$Amt-660120-1-1-2-0-1\" type=\"text\" id=\"Amt-660120-1-1-2-0-1\" class=\"textBox onlyPositiveNumber\" onkeypress=\"return validateAmountWager(event)\" /></td><td class=\"td3\"><select name=\"ctl00$cphWorkArea$Cbo-660120-1-1-2-0-1\" id=\"Cbo-660120-1-1-2-0-1\" class=\"cboOdds cboLines\" State=\"0\" for=\"VVbivr60rv6f8uxr8kwtT4fcfHRbOiXGcXB7z/qu5ZuKybEE7__lcw7e9/UJwgYOG\">					<option selected=\"selected\" value=\"0\" class=\"RadComboBoxDefaultItem SelItem\">+3&#189; -110</option>				</select></td>			</tr>		</table></td><td class=\"tdMoneyLine \"><table class=\"TableMoneyLine\" border=\"0\">			<tr class=\"tr1\">				<td class=\"td1\"><img id=\"\" src=\"../Images/blank.png\" style=\"border-width:0px;\" /></td><td class=\"td2\"><input name=\"ctl00$cphWorkArea$Amt-660120-1-1-1\" type=\"text\" id=\"Amt-660120-1-1-1\" class=\"textBox\" onkeypress=\"return validateAmountWager(event)\" title=\"Limit|500.00\" /></td><td class=\"td3\"><font class='RadComboBoxItem'>+150</font></td>			</tr>		</table></td><td class=\"tdTotal \"><table class=\"TableTotals\" border=\"0\">		</table></td><td class=\"tdTeamTotal \"><table class=\"TableTeamTotal\" border=\"0\">		</table></td>	</tr><tr class=\"BackgroundLineGray lastRow\">		<td class=\"tdDate \">5:00 PM</td><td class=\"tdTelevised \"></td><td class=\"tdTeamName \"><span class='rotation_number'>528</span><span class='team_name'>San Jose State</span></td><td class=\"tdSpread \"><table class=\"TableSpread\" border=\"0\">			<tr class=\"tr2\">				<td class=\"td1\"><img id=\"\" src=\"../Images/blank.png\" style=\"border-width:0px;\" /></td><td class=\"td2\"><input name=\"ctl00$cphWorkArea$Amt-660120-1-2-2-0-1\" type=\"text\" id=\"Amt-660120-1-2-2-0-1\" class=\"textBox onlyPositiveNumber\" onkeypress=\"return validateAmountWager(event)\" /></td><td class=\"td3\"><select name=\"ctl00$cphWorkArea$Cbo-660120-1-2-2-0-1\" id=\"Cbo-660120-1-2-2-0-1\" class=\"cboOdds cboLines\" State=\"0\" for=\"VVbivr60rv6f8uxr8kwtT4zLhgWmudzkCcKpAyLYT22/ZLp5KLKLhqAdMivGqIN0\">					<option selected=\"selected\" value=\"0\" class=\"RadComboBoxDefaultItem SelItem\">-3&#189; -110</option>				</select></td>			</tr>		</table></td><td class=\"tdMoneyLine \"><table class=\"TableMoneyLine\" border=\"0\">			<tr class=\"tr2\">				<td class=\"td1\"><img id=\"\" src=\"../Images/blank.png\" style=\"border-width:0px;\" /></td><td class=\"td2\"><input name=\"ctl00$cphWorkArea$Amt-660120-1-2-1\" type=\"text\" id=\"Amt-660120-1-2-1\" class=\"textBox\" onkeypress=\"return validateAmountWager(event)\" title=\"Limit|500.00\" /></td><td class=\"td3\"><font class='RadComboBoxItem'>-170</font></td>			</tr>		</table></td><td class=\"tdTotal \"><table class=\"TableTotals\" border=\"0\">		</table></td><td class=\"tdTeamTotal \"><table class=\"TableTeamTotal\" border=\"0\">		</table></td>	</tr><tr class=\"BackgroundLineAlternateGray\">		<td class=\"tdDate \">12/18</td><td class=\"tdTelevised \"><img id=\"\" src=\"../Images/blank.png\" style=\"border-width:0px;\" /></td><td class=\"tdTeamName \"><span class='rotation_number'>529</span><span class='team_name'>Northeastern</span></td><td class=\"tdSpread \"><table class=\"TableSpread\" border=\"0\">			<tr class=\"tr1\">				<td class=\"td1\"><img id=\"\" src=\"../Images/blank.png\" style=\"border-width:0px;\" /></td><td class=\"td2\"><input name=\"ctl00$cphWorkArea$Amt-660121-1-1-2-0-1\" type=\"text\" id=\"Amt-660121-1-1-2-0-1\" class=\"textBox onlyPositiveNumber\" onkeypress=\"return validateAmountWager(event)\" /></td><td class=\"td3\"><select name=\"ctl00$cphWorkArea$Cbo-660121-1-1-2-0-1\" id=\"Cbo-660121-1-1-2-0-1\" class=\"cboOdds cboLines\" State=\"0\" for=\"VVbivr60rv6f8uxr8kwtT__2itecn__TnZLIWg4HO__SxQhb7qzsXpj9UyIYZYnQH/p\">					<option selected=\"selected\" value=\"0\" class=\"RadComboBoxDefaultItem SelItem\">+10 -110</option>				</select></td>			</tr>		</table></td><td class=\"tdMoneyLine \"><table class=\"TableMoneyLine\" border=\"0\">		</table></td><td class=\"tdTotal \"><table class=\"TableTotals\" border=\"0\">		</table></td><td class=\"tdTeamTotal \"><table class=\"TableTeamTotal\" border=\"0\">		</table></td>	</tr><tr class=\"BackgroundLineAlternateGray lastRow\">		<td class=\"tdDate \">7:00 PM</td><td class=\"tdTelevised \"></td><td class=\"tdTeamName \"><span class='rotation_number'>530</span><span class='team_name'>Michigan State</span></td><td class=\"tdSpread \"><table class=\"TableSpread\" border=\"0\">			<tr class=\"tr2\">				<td class=\"td1\"><img id=\"\" src=\"../Images/blank.png\" style=\"border-width:0px;\" /></td><td class=\"td2\"><input name=\"ctl00$cphWorkArea$Amt-660121-1-2-2-0-1\" type=\"text\" id=\"Amt-660121-1-2-2-0-1\" class=\"textBox onlyPositiveNumber\" onkeypress=\"return validateAmountWager(event)\" /></td><td class=\"td3\"><select name=\"ctl00$cphWorkArea$Cbo-660121-1-2-2-0-1\" id=\"Cbo-660121-1-2-2-0-1\" class=\"cboOdds cboLines\" State=\"0\" for=\"VVbivr60rv6f8uxr8kwtT/C9TTP__FimlBIWn2zsHNFemxCWnH3Mvj8KKYDeLJx2r\">					<option selected=\"selected\" value=\"0\" class=\"RadComboBoxDefaultItem SelItem\">-10 -110</option>				</select></td>			</tr>		</table></td><td class=\"tdMoneyLine \"><table class=\"TableMoneyLine\" border=\"0\">		</table></td><td class=\"tdTotal \"><table class=\"TableTotals\" border=\"0\">		</table></td><td class=\"tdTeamTotal \"><table class=\"TableTeamTotal\" border=\"0\">		</table></td>	</tr><tr class=\"BackgroundLineGray\">		<td class=\"tdDate \">12/18</td><td class=\"tdTelevised \"><img id=\"\" src=\"../Images/blank.png\" style=\"border-width:0px;\" /></td><td class=\"tdTeamName \"><span class='rotation_number'>531</span><span class='team_name'>Western Michigan</span></td><td class=\"tdSpread \"><table class=\"TableSpread\" border=\"0\">			<tr class=\"tr1\">				<td class=\"td1\"><img id=\"\" src=\"../Images/blank.png\" style=\"border-width:0px;\" /></td><td class=\"td2\"><input name=\"ctl00$cphWorkArea$Amt-660122-1-1-2-0-1\" type=\"text\" id=\"Amt-660122-1-1-2-0-1\" class=\"textBox onlyPositiveNumber\" onkeypress=\"return validateAmountWager(event)\" /></td><td class=\"td3\"><select name=\"ctl00$cphWorkArea$Cbo-660122-1-1-2-0-1\" id=\"Cbo-660122-1-1-2-0-1\" class=\"cboOdds cboLines\" State=\"0\" for=\"VVbivr60rv6f8uxr8kwtTzXc/H0uuMIcOJzpDJJKbXbJd10QmtbqEB0QMdh9S83/\">					<option selected=\"selected\" value=\"0\" class=\"RadComboBoxDefaultItem SelItem\">+12&#189; -110</option>				</select></td>			</tr>		</table></td><td class=\"tdMoneyLine \"><table class=\"TableMoneyLine\" border=\"0\">		</table></td><td class=\"tdTotal \"><table class=\"TableTotals\" border=\"0\">		</table></td><td class=\"tdTeamTotal \"><table class=\"TableTeamTotal\" border=\"0\">		</table></td>	</tr><tr class=\"BackgroundLineGray lastRow\">		<td class=\"tdDate \">8:00 PM</td><td class=\"tdTelevised \"></td><td class=\"tdTeamName \"><span class='rotation_number'>532</span><span class='team_name'>Washington</span></td><td class=\"tdSpread \"><table class=\"TableSpread\" border=\"0\">			<tr class=\"tr2\">				<td class=\"td1\"><img id=\"\" src=\"../Images/blank.png\" style=\"border-width:0px;\" /></td><td class=\"td2\"><input name=\"ctl00$cphWorkArea$Amt-660122-1-2-2-0-1\" type=\"text\" id=\"Amt-660122-1-2-2-0-1\" class=\"textBox onlyPositiveNumber\" onkeypress=\"return validateAmountWager(event)\" /></td><td class=\"td3\"><select name=\"ctl00$cphWorkArea$Cbo-660122-1-2-2-0-1\" id=\"Cbo-660122-1-2-2-0-1\" class=\"cboOdds cboLines\" State=\"0\" for=\"VVbivr60rv6f8uxr8kwtT02U1fzEhDCV2IsZjEkaHEHwxQuJKtwIdRzIGzLj/fbJ\">					<option selected=\"selected\" value=\"0\" class=\"RadComboBoxDefaultItem SelItem\">-12&#189; -110</option>				</select></td>			</tr>		</table></td><td class=\"tdMoneyLine \"><table class=\"TableMoneyLine\" border=\"0\">		</table></td><td class=\"tdTotal \"><table class=\"TableTotals\" border=\"0\">		</table></td><td class=\"tdTeamTotal \"><table class=\"TableTeamTotal\" border=\"0\">		</table></td>	</tr><tr>		<td class=\"HeaderTableLine\" colspan=\"7\"><Div><Div class=\"cellSportHeader\">Holiday Festival - Madison Square Garden - New York, NY</Div><Div class=\"submitwager\"><input type=\"submit\" name=\"ctl00$cphWorkArea$ctl145\" value=\"Continue\" onclick=\"javascript:WebForm_DoPostBackWithOptions(new WebForm_PostBackOptions(&quot;ctl00$cphWorkArea$ctl145&quot;, &quot;&quot;, false, &quot;&quot;, &quot;WagerTicket.aspx&quot;, false, false))\" id=\"\" title=\"go to see tickets wager\" class=\"Submitbutton\" runat=\"server\" /></Div></Div></td>	</tr><tr class=\"HeaderColumnsName\">		<td class=\"thHeaderPeriod\" colspan=\"3\">Game</td><td class=\"tdHeaderSpread\">Spread $500</td><td class=\"tdHeaderMoneyLine\">ML $500</td><td class=\"tdHeaderTotal\">Total $500</td><td class=\"tdHeaderTeamTotal\">Team Totals $500</td>	</tr><tr class=\"BackgroundLineAlternateGray\">		<td class=\"tdDate \">12/18</td><td class=\"tdTelevised \"><img id=\"\" src=\"../Images/blank.png\" style=\"border-width:0px;\" /></td><td class=\"tdTeamName \"><span class='rotation_number'>535</span><span class='team_name'>Fordham</span></td><td class=\"tdSpread \"><table class=\"TableSpread\" border=\"0\">			<tr class=\"tr1\">				<td class=\"td1\"><img id=\"\" src=\"../Images/blank.png\" style=\"border-width:0px;\" /></td><td class=\"td2\"><input name=\"ctl00$cphWorkArea$Amt-660124-1-1-2-0-1\" type=\"text\" id=\"Amt-660124-1-1-2-0-1\" class=\"textBox onlyPositiveNumber\" onkeypress=\"return validateAmountWager(event)\" /></td><td class=\"td3\"><select name=\"ctl00$cphWorkArea$Cbo-660124-1-1-2-0-1\" id=\"Cbo-660124-1-1-2-0-1\" class=\"cboOdds cboLines\" State=\"0\" for=\"VVbivr60rv6f8uxr8kwtT8wLiDyOhm9c/EIXDIbnOs0RRavMMzl17A9zYjI__T/g3\">					<option selected=\"selected\" value=\"0\" class=\"RadComboBoxDefaultItem SelItem\">+6 -110</option>				</select></td>			</tr>		</table></td><td class=\"tdMoneyLine \"><table class=\"TableMoneyLine\" border=\"0\">			<tr class=\"tr1\">				<td class=\"td1\"><img id=\"\" src=\"../Images/blank.png\" style=\"border-width:0px;\" /></td><td class=\"td2\"><input name=\"ctl00$cphWorkArea$Amt-660124-1-1-1\" type=\"text\" id=\"Amt-660124-1-1-1\" class=\"textBox\" onkeypress=\"return validateAmountWager(event)\" title=\"Limit|500.00\" /></td><td class=\"td3\"><font class='RadComboBoxItem'>+195</font></td>			</tr>		</table></td><td class=\"tdTotal \"><table class=\"TableTotals\" border=\"0\">			<tr class=\"tr1\">				<td class=\"td1\"><img id=\"\" src=\"../Images/blank.png\" style=\"border-width:0px;\" /></td><td class=\"td2\"><input name=\"ctl00$cphWorkArea$Amt-660124-1-1-3-0-1\" type=\"text\" id=\"Amt-660124-1-1-3-0-1\" class=\"textBox onlyPositiveNumber\" onkeypress=\"return validateAmountWager(event)\" /></td><td class=\"td3\"><select name=\"ctl00$cphWorkArea$Cbo-660124-1-1-3-0-1\" id=\"Cbo-660124-1-1-3-0-1\" class=\"cboTotalOdds cboLines\" State=\"0\" for=\"VVbivr60rv6f8uxr8kwtT8ZMXSFwM7d__8H0FomHKT5ArMQ4c1UnnQ8JOBiinLdQc\">					<option selected=\"selected\" value=\"0\" class=\"RadComboBoxDefaultItem SelItem\">O 128 -110</option>				</select></td>			</tr>		</table></td><td class=\"tdTeamTotal \"><table class=\"TableTeamTotal\" border=\"0\">			<tr>				<td class=\"td1\"><input name=\"ctl00$cphWorkArea$Amt-660124-1-1-8\" type=\"text\" id=\"Amt-660124-1-1-8\" class=\"textBox onlyPositiveNumber\" onkeypress=\"return validateAmountWager(event)\" />&nbsp;&nbsp;<font class='RadComboBoxItem'>O 61 -115</font></td><td class=\"td2\"><input name=\"ctl00$cphWorkArea$Amt-660124-1-1-9\" type=\"text\" id=\"Amt-660124-1-1-9\" class=\"textBox onlyPositiveNumber\" onkeypress=\"return validateAmountWager(event)\" />&nbsp;&nbsp;<font class='RadComboBoxItem'>U 61 -115</font></td>			</tr>		</table></td>	</tr><tr class=\"BackgroundLineAlternateGray lastRow\">		<td class=\"tdDate \">1:30 PM</td><td class=\"tdTelevised \"></td><td class=\"tdTeamName \"><span class='rotation_number'>536</span><span class='team_name'>Rutgers</span></td><td class=\"tdSpread \"><table class=\"TableSpread\" border=\"0\">			<tr class=\"tr2\">				<td class=\"td1\"><img id=\"\" src=\"../Images/blank.png\" style=\"border-width:0px;\" /></td><td class=\"td2\"><input name=\"ctl00$cphWorkArea$Amt-660124-1-2-2-0-1\" type=\"text\" id=\"Amt-660124-1-2-2-0-1\" class=\"textBox onlyPositiveNumber\" onkeypress=\"return validateAmountWager(event)\" /></td><td class=\"td3\"><select name=\"ctl00$cphWorkArea$Cbo-660124-1-2-2-0-1\" id=\"Cbo-660124-1-2-2-0-1\" class=\"cboOdds cboLines\" State=\"0\" for=\"VVbivr60rv6f8uxr8kwtTw77LzhkSxrX78JuyhCO1nNwRzUAyazOuqSx2K6E1/TD\">					<option selected=\"selected\" value=\"0\" class=\"RadComboBoxDefaultItem SelItem\">-6 -110</option>				</select></td>			</tr>		</table></td><td class=\"tdMoneyLine \"><table class=\"TableMoneyLine\" border=\"0\">			<tr class=\"tr2\">				<td class=\"td1\"><img id=\"\" src=\"../Images/blank.png\" style=\"border-width:0px;\" /></td><td class=\"td2\"><input name=\"ctl00$cphWorkArea$Amt-660124-1-2-1\" type=\"text\" id=\"Amt-660124-1-2-1\" class=\"textBox\" onkeypress=\"return validateAmountWager(event)\" title=\"Limit|500.00\" /></td><td class=\"td3\"><font class='RadComboBoxItem'>-235</font></td>			</tr>		</table></td><td class=\"tdTotal \"><table class=\"TableTotals\" border=\"0\">			<tr class=\"tr2\">				<td class=\"td1\"><img id=\"\" src=\"../Images/blank.png\" style=\"border-width:0px;\" /></td><td class=\"td2\"><input name=\"ctl00$cphWorkArea$Amt-660124-1-2-4-0-1\" type=\"text\" id=\"Amt-660124-1-2-4-0-1\" class=\"textBox onlyPositiveNumber\" onkeypress=\"return validateAmountWager(event)\" /></td><td class=\"td3\"><select name=\"ctl00$cphWorkArea$Cbo-660124-1-2-4-0-1\" id=\"Cbo-660124-1-2-4-0-1\" class=\"cboTotalOdds cboLines\" State=\"0\" for=\"VVbivr60rv6f8uxr8kwtT9p9ZoYqyiRFNFnMO__9zYCWpCDZmC0llS6L2jp/DDTn6\">					<option selected=\"selected\" value=\"0\" class=\"RadComboBoxDefaultItem SelItem\">U 128 -110</option>				</select></td>			</tr>		</table></td><td class=\"tdTeamTotal \"><table class=\"TableTeamTotal\" border=\"0\">			<tr>				<td class=\"td1\"><input name=\"ctl00$cphWorkArea$Amt-660124-1-2-8\" type=\"text\" id=\"Amt-660124-1-2-8\" class=\"textBox onlyPositiveNumber\" onkeypress=\"return validateAmountWager(event)\" />&nbsp;&nbsp;<font class='RadComboBoxItem'>O 67 -115</font></td><td class=\"td2\"><input name=\"ctl00$cphWorkArea$Amt-660124-1-2-9\" type=\"text\" id=\"Amt-660124-1-2-9\" class=\"textBox onlyPositiveNumber\" onkeypress=\"return validateAmountWager(event)\" />&nbsp;&nbsp;<font class='RadComboBoxItem'>U 67 -115</font></td>			</tr>		</table></td>	</tr><tr>		<td class=\"HeaderTableLine\" colspan=\"7\"><Div><Div class=\"cellSportHeader\">Dam City Classic - Moda Center - Portland, OR (Final Round)</Div><Div class=\"submitwager\"><input type=\"submit\" name=\"ctl00$cphWorkArea$ctl175\" value=\"Continue\" onclick=\"javascript:WebForm_DoPostBackWithOptions(new WebForm_PostBackOptions(&quot;ctl00$cphWorkArea$ctl175&quot;, &quot;&quot;, false, &quot;&quot;, &quot;WagerTicket.aspx&quot;, false, false))\" id=\"\" title=\"go to see tickets wager\" class=\"Submitbutton\" runat=\"server\" /></Div></Div></td>	</tr><tr class=\"HeaderColumnsName\">		<td class=\"thHeaderPeriod\" colspan=\"3\">Game</td><td class=\"tdHeaderSpread\">Spread $500</td><td class=\"tdHeaderMoneyLine\">ML $500</td><td class=\"tdHeaderTotal\">Total $500</td><td class=\"tdHeaderTeamTotal\"></td>	</tr><tr class=\"BackgroundLineGray\">		<td class=\"tdDate \">12/18</td><td class=\"tdTelevised \"><img id=\"\" src=\"../Images/blank.png\" style=\"border-width:0px;\" /></td><td class=\"tdTeamName \"><span class='rotation_number'>537</span><span class='team_name'>Portland</span></td><td class=\"tdSpread \"><table class=\"TableSpread\" border=\"0\">			<tr class=\"tr1\">				<td class=\"td1\"><img id=\"\" src=\"../Images/blank.png\" style=\"border-width:0px;\" /></td><td class=\"td2\"><input name=\"ctl00$cphWorkArea$Amt-660125-1-1-2-0-1\" type=\"text\" id=\"Amt-660125-1-1-2-0-1\" class=\"textBox onlyPositiveNumber\" onkeypress=\"return validateAmountWager(event)\" /></td><td class=\"td3\"><select name=\"ctl00$cphWorkArea$Cbo-660125-1-1-2-0-1\" id=\"Cbo-660125-1-1-2-0-1\" class=\"cboOdds cboLines\" State=\"0\" for=\"VVbivr60rv6f8uxr8kwtT5QgkyZ7Tljk28T29N9KYVRH1n0vXH1xPXIQ__1cwyWQk\">					<option selected=\"selected\" value=\"0\" class=\"RadComboBoxDefaultItem SelItem\">-4 -110</option>				</select></td>			</tr>		</table></td><td class=\"tdMoneyLine \"><table class=\"TableMoneyLine\" border=\"0\">			<tr class=\"tr1\">				<td class=\"td1\"><img id=\"\" src=\"../Images/blank.png\" style=\"border-width:0px;\" /></td><td class=\"td2\"><input name=\"ctl00$cphWorkArea$Amt-660125-1-1-1\" type=\"text\" id=\"Amt-660125-1-1-1\" class=\"textBox\" onkeypress=\"return validateAmountWager(event)\" title=\"Limit|500.00\" /></td><td class=\"td3\"><font class='RadComboBoxItem'>-190</font></td>			</tr>		</table></td><td class=\"tdTotal \"><table class=\"TableTotals\" border=\"0\">		</table></td><td class=\"tdTeamTotal \"><table class=\"TableTeamTotal\" border=\"0\">		</table></td>	</tr><tr class=\"BackgroundLineGray lastRow\">		<td class=\"tdDate \">6:00 PM</td><td class=\"tdTelevised \"></td><td class=\"tdTeamName \"><span class='rotation_number'>538</span><span class='team_name'>Oregon State</span></td><td class=\"tdSpread \"><table class=\"TableSpread\" border=\"0\">			<tr class=\"tr2\">				<td class=\"td1\"><img id=\"\" src=\"../Images/blank.png\" style=\"border-width:0px;\" /></td><td class=\"td2\"><input name=\"ctl00$cphWorkArea$Amt-660125-1-2-2-0-1\" type=\"text\" id=\"Amt-660125-1-2-2-0-1\" class=\"textBox onlyPositiveNumber\" onkeypress=\"return validateAmountWager(event)\" /></td><td class=\"td3\"><select name=\"ctl00$cphWorkArea$Cbo-660125-1-2-2-0-1\" id=\"Cbo-660125-1-2-2-0-1\" class=\"cboOdds cboLines\" State=\"0\" for=\"VVbivr60rv6f8uxr8kwtT__BcGmb724co6iqwE4y71LlsDRpUdg3jttLZQ5U0f__U6\">					<option selected=\"selected\" value=\"0\" class=\"RadComboBoxDefaultItem SelItem\">+4 -110</option>				</select></td>			</tr>		</table></td><td class=\"tdMoneyLine \"><table class=\"TableMoneyLine\" border=\"0\">			<tr class=\"tr2\">				<td class=\"td1\"><img id=\"\" src=\"../Images/blank.png\" style=\"border-width:0px;\" /></td><td class=\"td2\"><input name=\"ctl00$cphWorkArea$Amt-660125-1-2-1\" type=\"text\" id=\"Amt-660125-1-2-1\" class=\"textBox\" onkeypress=\"return validateAmountWager(event)\" title=\"Limit|500.00\" /></td><td class=\"td3\"><font class='RadComboBoxItem'>+165</font></td>			</tr>		</table></td><td class=\"tdTotal \"><table class=\"TableTotals\" border=\"0\">		</table></td><td class=\"tdTeamTotal \"><table class=\"TableTeamTotal\" border=\"0\">		</table></td>	</tr></table></span>                        </div>    </div>    <div class=\"footer\">            <div class=\"footerWorkPlace\">    </div>    </div>    <div style=\"display: none;\">                    </div>    </form>    <script type=\"text/javascript\">    var NotificationStartTime = 120000; //milliseconds before session time out    var PopupRefreshDelay = 10000; //milliseconds between popup refresh    var timeout;        object = {       func: function() {                         WTSoftwareWeb.SessionTimeoutWS.GetSessionTimeout( OnSuccess, OnFailure);         }    }    $(document).ready(function() {                WTSoftwareWeb.SessionTimeoutWS.GetSessionTimeout( OnSuccess, OnFailure);      });    function RedirectToLoginPage() {                $('input[type=button][value=\"Log Off\"]').click();    }    function RefreshSession() {                  WTSoftwareWeb.SessionTimeoutWS.RefreshSession( OnSuccess, OnFailure);    }    function OnSuccess(response) {                if (response && response.IsAutentificated) {            if (response.Result) {                var TimeBeforeTimeOut = parseInt(response.Result)                if (TimeBeforeTimeOut>NotificationStartTime)                {                    TimeOutPopup.Hide();                    timeout = setTimeout( function() { object.func.call(object) } , TimeBeforeTimeOut - NotificationStartTime)  ;                }                else                {                    TimeOutPopup.Show();                    timeout = setTimeout( function() { object.func.call(object) } , PopupRefreshDelay);                 }            }                    }        else {            RedirectToLoginPage();        }    }    function OnFailure(response) {        RedirectToLoginPage();    }     var TimeOutPopup = { Show: function () {    $('#SessionTimeoutPopup').show();    }, Hide: function () {        $('#SessionTimeoutPopup').hide();    }};</script><div id=\"SessionTimeoutPopup\" class=\"SessionTimeoutScreen\" style=\"display: none\">    <div class=\"SessionTimeoutPopup\" >        <div class=\"PopupHeader\">            <div class=\"WarningCaption\">                Time Out Warning</div>            <div class=\"ReasonCaption\">                Your Session Will Time Out in 2 Minutes</div>        </div>        <div class=\"PopupBody\">            <div>            <br/>                For your safety and protection this session <br/>                 is about to be timed out and redirected to <br />                 the home page if there is no additional activity.</div>            <div>            <br />                If you are still working in the session simply <br />                 click ok to continue. <br /></div>            <div class=\"ButtonHolder\">                <input type=\"button\" id=\"btnTimeout\" onclick=\"RefreshSession();\" value=\"Ok\" class=\"TimeoutButton\" />            </div>        </div>    </div></div>		                <script language=\"javascript\" type=\"text/javascript\">        $(document).ready(function () {            $(document).keypress(function (e) {                var value = $(\"#txtSearch\").val();                if (e.which == 13) {                    if (value != \"Search...\") {                        $(\"#cmdSearch\").click();                    }                    else {                        var listInput = $(\"#tblcc input[type='text']\").filter(function () {                            return $(this).val() != \"\";                        });                        if (value == \"Search...\" && listInput.length > 0) {                            $(\".Submitbutton\").click();                        }                    }                }            });        });    </script>        <script type=\"text/javascript\" src=\"/Scripts/Ait.js?v=2\"></script>    <script type=\"text/javascript\" src=\"/Scripts/wise.js?v=2\"></script>    <script type=\"text/javascript\" src=\"/Scripts/Wagers.js?v=2\"></script>    <script type=\"text/javascript\" src=\"/Scripts/element.js\"></script>    <script type=\"text/javascript\" src=\"/Scripts/jquery/jquery.blockUI.js\"></script>    <script type=\"text/javascript\" src=\"/Scripts/CustomerAjax.js?v=2\"></script>    <script type=\"text/javascript\" src=\"/Scripts/jquery.validationEngine-en.js\"></script>    <script type=\"text/javascript\" src=\"/Scripts/jquery.validationEngine.js\"></script>    <script type=\"text/javascript\" src=\"/Scripts/Ajax.js?v=2\"></script>    <script type=\"text/javascript\" language=\"javascript\">        var orden = 1;        var onSubmit = false;        function OnSubmitRequest() {            if (!onSubmit) {                onSubmit = true;                return true;            }            return false;        }        function deleteorder(id) {            if (document.forms[0]['txtOrden'].value.indexOf(id) != -1) {                document.forms[0]['txtOrden'].value = document.forms[0]['txtOrden'].value.replace((id + '@'), ' ');            }        }        function sideorder(obj1) {            var valor = '';            valor = obj1.name;            deleteorder(valor);            if (obj1.value != 0 || obj1.value != '') {                document.forms[0]['txtOrden'].value = document.forms[0]['txtOrden'].value + valor + '@';            }            return true;        }        function OnChecked(count, chkName, disabled) {            var i;            for (i = 2; i <= count; i++) {                if (disabled == 'False') {                    document.getElementById(chkName + i).checked = false;                    document.forms[0]['txtWagerTypeParlay'].checked = false;                } else {                    document.getElementById(chkName + i).checked = false;                    document.forms[0]['txtWagerType'].checked = false;                }            }        }        function test() {            if (document.forms[0]['txtWagerTypeParlay'].checked == true) {                return false;            }            return true;        }        function setfocus() {            document.getElementById('txtWagerAmount').focus()        }        function mp_onload() {            if (window.body_onload != null)                window.body_onload();        }        function imageButtonOnKeyPress() {            var keyCodeEntered = (window.event.which) ? window.event.which : window.event.keyCode;            if (keyCodeEntered == 13) {                event.returnValue = false;                return false;            }            return true;        }        function ValidateLiveCasino(URL, clientCode) {            var items = JSON.stringify({ clientCode: clientCode });            var isOk = false;            $.ajax({                type: \"POST\",                url: \"ValidateLiveCasinoLCD.aspx/ValidateClientBalance\",                data: items,                async: false,                contentType: \"application/json;charset=utf-8\",                dataType: \"json\",                success: function (result) {                    res = result.d                    if (res) {                        // window.open(URL, \"Live Casino\", \"fullscreen=yes\");                        isOk = true;                    }                    else {                        alert(\"No balance is available to transfer. Please try again later.\");                    }                },                error: function (xhr, errorType, exception) { return false; OnFail(xhr, errorType, exception); }            });            return isOk;        }        function OnFail(xhr, errorType, exception) {            alert(\"Message: \" + xhr.statusText);        }         		$(document).keypress(function (e) {            if (e.which == 13) {                $(\"#cmdContinue\").click();            }        });    </script></body></html>";
//			String xhtml = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta name=\"google-translate-customization\" content=\"3233c073ab0f4e8f-812f5fbec3c21e36-gd944dbbbb163dd6b-17\" /><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><meta http-equiv=\"Content-Script-Type\" content=\"text/javascript\" /><meta http-equiv=\"Content-Style-Type\" content=\"text/css\" /><title>MSB247</title><link rel=\"shortcut icon\" href=\"/images/ikon_MSB247.ico\" /><meta name=\"keywords\" lang=\"en\" content=\"Title\" /><meta name=\"description\" lang=\"en\" content=\"Title\" /><meta name=\"language\" content=\"en\" /><meta http-equiv=\"imagetoolbar\" content=\"no\" /><meta name=\"apple-mobile-web-app-capable\" content=\"yes\"/><meta name=\"apple-mobile-web-app-status-bar-style\" content=\"black\"/><!-- BEGIN PUSH IN MENU--><link rel=\"stylesheet\" type=\"text/css\" href=\"/Scripts/jpush/css/demo.css\" /><!-- END PUSH IN MENU--><style type=\"text/css\">@import \"/Themes/Theme021/Styles/AllAjax.css?v=20\"; @import \"/Themes/Theme021/Styles/ClientStyle.css?v=20\"; @import \"/Themes/Theme021/Styles/AjaxClientStyle.css?v=20\"; @import \"/Themes/Theme021/Styles/Style.css?v=20\"; @import \"/Themes/Theme021/Styles/StyleTabs.css?v=20\"; @import \"/Themes/Theme021/Styles/SessionTimeoutPopup.css?v=20\"; @import \"/App_Themes/WtSoftware/ComboBox.WTSoftware.css?v=20\"; @import \"/App_Themes/WtSoftware/TreeView.WTSoftware.css?v=20\"; @import \"/App_Themes/WtSoftware/Menu.WTSoftware.css?v=20\";</style><style type=\"text/css\">.HorsesLabel {	min-height: 15px;}.dmmnubtn{visibility:hidden;display: none;}.closebtn{display: none;}</style><link href=\"Styles/jquery.cluetip.css\" rel=\"stylesheet\" type=\"text/css\" /><script type=\"text/javascript\" src=\"/Scripts/jquery/jquery-1.9.1.min.js\"></script><link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\"><script>if(this != top){ top.document.location.href = this.document.location.href;}</script><script src='https://cdnjs.cloudflare.com/ajax/libs/prefixfree/1.0.7/prefixfree.min.js'></script></head><body onload=\"mp_onload();\" class=\"mpbody_MSB247\"><form name=\"Form1\" method=\"post\" action=\"./WagerTicket.aspx\" id=\"Form1\" style=\"min-height: 100%; height: 100% !important\"><div><input type=\"hidden\" name=\"__EVENTTARGET\" id=\"__EVENTTARGET\" value=\"\" /><input type=\"hidden\" name=\"__EVENTARGUMENT\" id=\"__EVENTARGUMENT\" value=\"\" /><input type=\"hidden\" name=\"__VIEWSTATEFIELDCOUNT\" id=\"__VIEWSTATEFIELDCOUNT\" value=\"19\" /><input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"/wEPDwUKMjA4Mzk3MTI5MA8WFh4RSWRMYXN0U2hhZGVDaGFuZ2UoKVlTeXN0ZW0uSW50NjQsIG1zY29ybGliLCBWZXJzaW9uPTQuMC4wLjAsIEN1bHR1cmU9bmV1dHJhbCwgUHVibGljS2V5VG9rZW49Yjc3YTVjNTYxOTM0ZTA4OQcxMjMyMzQ4HglXYWdlclNhdmUyqiMAAQAAAP////8BAAAAAAAAAAwCAAAAV0FJVC1Nb2RlbCwgVmVyc2lvbj0yMDE4Mi4zLjE0MjEzLjE0NDIsIEN1bHR1cmU9bmV1dHJhbCwgUHVibGljS2V5VG9rZW49ZjYxZDdmOWIzZjAwYjE1NwQBAAAAlAFTeXN0ZW0uQ29sbGVjdGlvbnMuR2VuZXJpYy5MaXN0YDFbW1dUU29mdHdhcmUuTW9kZWwuV2FnZXIsIEFJVC1Nb2RlbCwgVmVyc2lvbj0yMDE4Mi4zLjE0MjEzLjE0NDIsIEN1bHR1cmU9\" /><input type=\"hidden\" name=\"__VIEWSTATE1\" id=\"__VIEWSTATE1\" value=\"bmV1dHJhbCwgUHVibGljS2V5VG9rZW49ZjYxZDdmOWIzZjAwYjE1N11dAwAAAAZfaXRlbXMFX3NpemUIX3ZlcnNpb24EAAAYV1RTb2Z0d2FyZS5Nb2RlbC5XYWdlcltdAgAAAAgICQMAAAABAAAAAQAAAAcDAAAAAAEAAAAEAAAABBZXVFNvZnR3YXJlLk1vZGVsLldhZ2VyAgAAAAkEAAAADQMFBAAAABZXVFNvZnR3YXJlLk1vZGVsLldhZ2VyJwAAAA1fdGlja2V0TnVtYmVyCF93YWdlcklkCl93YWdlclR5cGUMX2dldGxheVdhZ2VyDF9hbW91bnRUb0JldAxfYW1vdW50VG9XaW4IX29kZHNMYXkJX29kZHNUYWtlC193YWdlclN0YXRlD19hY3Rpb25PbklmQmV0cwxfcG9pbnRzQWRkZWQRX2Zyb21Db21wbGV4V2FnZXIKX2RhdGVTYXZlZA1fZGF0ZVJlc29sdmVkCV9p\" /><input type=\"hidden\" name=\"__VIEWSTATE2\" id=\"__VIEWSTATE2\" value=\"ZFRlYXNlcglfaWRQYXJsYXkRX25hbWVQYXJsYXlUZWFzZXILX2lzRnJlZVBsYXkOX2xzdFdhZ2VyU2lkZXMMX2N1cnJlbnRPZGRzDl9jdXJyZW50UG9pbnRzDl9jdXJyZW50UGF5T3V0CV9jb21tZW50cwdfc291cmNlCV91c2VyQ29kZQlfY29tcHV0ZXIPX2lzUm9sbGluZ0lmQmV0Dl9yb2xsaW5nTnVtYmVyD193YWdlcklkUm9sbGluZwtfY2xpZW50Q29kZRFfY29tYmluYXRpb25Db3VudBBfY29tYmluYXRpb25MaXN0D19kZWJpdFRvUGVuZGluZxNfbGl2ZUJldHRpbmdXYWdlcklkIDxDaGFydFBlcmNlbnRhZ2U+a19fQmFja2luZ0ZpZWxkGjxBZ2VudENvZGU+a19fQmFja2luZ0ZpZWxkIjxBdXRob3JpemF0aW9uVHlwZT5rX19CYWNraW5nRmllbGQnPEV4b3Rp\" /><input type=\"hidden\" name=\"__VIEWSTATE3\" id=\"__VIEWSTATE3\" value=\"Y1NwZWNpYWxXYWdlckxpc3Q+a19fQmFja2luZ0ZpZWxkHzxDaGVja0F2YWlsYWJsZT5rX19CYWNraW5nRmllbGQAAAAAAAAAAAAAAAEAAAAAAQADBAQEAQABAQAAAAEAAwAAAAEEAwAICAICBQUFBQICCw0NBwcBmQFTeXN0ZW0uQ29sbGVjdGlvbnMuR2VuZXJpYy5MaXN0YDFbW1dUU29mdHdhcmUuTW9kZWwuV2FnZXJTaWRlcywgQUlULU1vZGVsLCBWZXJzaW9uPTIwMTgyLjMuMTQyMTMuMTQ0MiwgQ3VsdHVyZT1uZXV0cmFsLCBQdWJsaWNLZXlUb2tlbj1mNjFkN2Y5YjNmMDBiMTU3XV0VV1RTb2Z0d2FyZS5Nb2RlbC5PZGRzAgAAABdXVFNvZnR3YXJlLk1vZGVsLlBvaW50cwIAAAAXV1RTb2Z0d2FyZS5Nb2RlbC5QYXlPdXQCAAAAAgEHCAh/U3lzdGVtLkNvbGxl\" /><input type=\"hidden\" name=\"__VIEWSTATE4\" id=\"__VIEWSTATE4\" value=\"Y3Rpb25zLkdlbmVyaWMuTGlzdGAxW1tTeXN0ZW0uU3RyaW5nLCBtc2NvcmxpYiwgVmVyc2lvbj00LjAuMC4wLCBDdWx0dXJlPW5ldXRyYWwsIFB1YmxpY0tleVRva2VuPWI3N2E1YzU2MTkzNGUwODldXQUJAiZXVFNvZnR3YXJlLk1vZGVsLkF1dGhvcml6YXRpb25UeXBlRW51bQIAAACUAVN5c3RlbS5Db2xsZWN0aW9ucy5HZW5lcmljLkxpc3RgMVtbV1RTb2Z0d2FyZS5Nb2RlbC5XYWdlciwgQUlULU1vZGVsLCBWZXJzaW9uPTIwMTgyLjMuMTQyMTMuMTQ0MiwgQ3VsdHVyZT1uZXV0cmFsLCBQdWJsaWNLZXlUb2tlbj1mNjFkN2Y5YjNmMDBiMTU3XV0BAgAAABmYAwMAAAAAAQEFMzMwLjADMzAwAzExMAMxMDABAAAAAAAGBQAAAACA4dSeUFXWCIDh1J5QVdYIAAAA\" /><input type=\"hidden\" name=\"__VIEWSTATE5\" id=\"__VIEWSTATE5\" value=\"AAoACQYAAAAJBwAAAAkIAAAACQkAAAAJBQAAAAAKCgAAAAAAAAAKAAAAAAoBMAAAAAAAAAAAAAoF9f///yZXVFNvZnR3YXJlLk1vZGVsLkF1dGhvcml6YXRpb25UeXBlRW51bQEAAAAHdmFsdWVfXwACAgAAAAAJDAAAAAAEBgAAAJkBU3lzdGVtLkNvbGxlY3Rpb25zLkdlbmVyaWMuTGlzdGAxW1tXVFNvZnR3YXJlLk1vZGVsLldhZ2VyU2lkZXMsIEFJVC1Nb2RlbCwgVmVyc2lvbj0yMDE4Mi4zLjE0MjEzLjE0NDIsIEN1bHR1cmU9bmV1dHJhbCwgUHVibGljS2V5VG9rZW49ZjYxZDdmOWIzZjAwYjE1N11dAwAAAAZfaXRlbXMFX3NpemUIX3ZlcnNpb24EAAAdV1RTb2Z0d2FyZS5Nb2RlbC5XYWdlclNpZGVzW10CAAAACAgJDQAAAAEAAAABAAAABQcAAAAVV1RTb2Z0\" /><input type=\"hidden\" name=\"__VIEWSTATE6\" id=\"__VIEWSTATE6\" value=\"d2FyZS5Nb2RlbC5PZGRzBwAAAAhfb2Rkc0xheQlfb2Rkc1Rha2UGX3ZhbGlkC19BZGp1c3RPZGRzEF9vcmlnaW5hbE9kZHNMYXkRX29yaWdpbmFsT2Rkc1Rha2UJX3JvdW5kaW5nAAAAAAAAAAUFAQEFBQICAAAAAzExMAMxMDABAAEwATAABQgAAAAXV1RTb2Z0d2FyZS5Nb2RlbC5Qb2ludHMEAAAAB19wb2ludHMKX3BvaW50VHlwZQZfdmFsaWQMX0FkanVzdFBvaW50AAEAAAsBAQIAAAAAgDBDBg4AAAACVE8BAAUJAAAAF1dUU29mdHdhcmUuTW9kZWwuUGF5T3V0BAAAAApfQW1vdW50TGF5C19BbW91bnRUYWtlB19hbW91bnQLX2lzV2luV2FnZXIAAAAABQUFAQIAAAAFMzMwLjADMzAwAzMwMAEBDAAAAAEAAAAJDwAAAAAAAAAAAAAABw0AAAAAAQAAAAQAAAAEG1dU\" /><input type=\"hidden\" name=\"__VIEWSTATE7\" id=\"__VIEWSTATE7\" value=\"U29mdHdhcmUuTW9kZWwuV2FnZXJTaWRlcwIAAAAJEAAAAA0DBw8AAAAAAQAAAAAAAAAEFldUU29mdHdhcmUuTW9kZWwuV2FnZXICAAAABRAAAAAbV1RTb2Z0d2FyZS5Nb2RlbC5XYWdlclNpZGVzOgAAAA1fdGlja2V0TnVtYmVyCF93YWdlcklkEF93YWdlclNpZGVOdW1iZXIHX2dhbWVJZAtfcGVyaW9kVHlwZQtfdGVhbU51bWJlcg9fcm90YXRpb25OdW1iZXIJX3NpZGVOYW1lDF93YWdlclRhcmdldAdfcG9pbnRzEF9wb2ludHNBZGRlZFNpZGUNX3RlYXNlclBvaW50cwpfc2lkZVN0YXRlEV9maXJzdFRlYW1QaXRjaGVyFl9maXJzdFBpdGNoZXJNdXN0U3RhcnQSX3NlY29uZFRlYW1QaXRjaGVyF19zZWNvbmRQaXRjaGVyTXVzdFN0YXJ0EF9hbW91bnRUb0JldFNp\" /><input type=\"hidden\" name=\"__VIEWSTATE8\" id=\"__VIEWSTATE8\" value=\"ZGUQX2Ftb3VudFRvV2luU2lkZRBfZ2V0bGF5V2FnZXJTaWRlDF9vZGRzTGF5U2lkZQ1fb2Rkc1Rha2VTaWRlFV9vcmlnaW5hbF9PZGRzTGF5U2lkZRZfb3JpZ2luYWxfb2Rkc1Rha2VTaWRlDF9jdXJyZW50T2RkcxFfZXh0cmFJbmZvcm1hdGlvbghfYXBwbGllZBFfZGF0ZVJlc29sdmVkU2lkZQlfZ2FtZVRpbWUJX2NvbW1lbnRzCF9pZFNwb3J0C19saW5lQ2hhbmdlDl9waXRjaGVyQ2hhbmdlB194X3RvX3kRX2dyYWRkaW5nQ29tbWVudHMNX2Rlc2NyaXB0aW9uMQ1fZGVzY3JpcHRpb24yDV9kZXNjcmlwdGlvbjMNX2Rlc2NyaXB0aW9uNBJfaWRDb250ZXN0Q2F0ZWdvcnkMX2NvbnRlc3ROYW1lBl91bml0cwZfZml4ZWQPX29wZW5TaWRlU3RhdHVzEV9kZXNjcmlw\" /><input type=\"hidden\" name=\"__VIEWSTATE9\" id=\"__VIEWSTATE9\" value=\"dGlvblNwb3J0Hzxpc01hbnVhbGx5T2Rkcz5rX19CYWNraW5nRmllbGQkPEFwcGx5UmVndWxhdGlvbk9ubHk+a19fQmFja2luZ0ZpZWxkGjxTcG9ydFR5cGU+a19fQmFja2luZ0ZpZWxkHDxPZmZlckxpbmVJRD5rX19CYWNraW5nRmllbGQbPE9mZmVyTGluZXM+a19fQmFja2luZ0ZpZWxkITxDdXJyZW50U2hhZGVUeXBlPmtfX0JhY2tpbmdGaWVsZBk8Um91bmRpbmc+a19fQmFja2luZ0ZpZWxkIDxTaGFkZVRlYXNlckxpbmU+a19fQmFja2luZ0ZpZWxkGzxHYW1lSGVhZGVyPmtfX0JhY2tpbmdGaWVsZBw8UGF5T3V0TWF4ZWQ+a19fQmFja2luZ0ZpZWxkKDxBbW91bnRUb0JldE1heGVkUGFpZE91dD5rX19CYWNraW5nRmllbGQoPEFtb3VudFRvV2luTWF4ZWRQYWlk\" /><input type=\"hidden\" name=\"__VIEWSTATE10\" id=\"__VIEWSTATE10\" value=\"T3V0PmtfX0JhY2tpbmdGaWVsZCM8UGF5T3V0TWF4ZWRNZXNzYWdlPmtfX0JhY2tpbmdGaWVsZAAAAAAAAAABAAQAAAABAAEAAAAAAAADAwQBAAAAAQABAQABAQEBAQABAQAEAQAABAABAAMEAQAAAAEIBwIIAgIIAhdXVFNvZnR3YXJlLk1vZGVsLlBvaW50cwIAAAALCwICAgUFAgUFcFN5c3RlbS5OdWxsYWJsZWAxW1tTeXN0ZW0uRGVjaW1hbCwgbXNjb3JsaWIsIFZlcnNpb249NC4wLjAuMCwgQ3VsdHVyZT1uZXV0cmFsLCBQdWJsaWNLZXlUb2tlbj1iNzdhNWM1NjE5MzRlMDg5XV1wU3lzdGVtLk51bGxhYmxlYDFbW1N5c3RlbS5EZWNpbWFsLCBtc2NvcmxpYiwgVmVyc2lvbj00LjAuMC4wLCBDdWx0dXJlPW5ldXRyYWwsIFB1YmxpY0tleVRva2VuPWI3N2E1YzU2\" /><input type=\"hidden\" name=\"__VIEWSTATE11\" id=\"__VIEWSTATE11\" value=\"MTkzNGUwODldXRVXVFNvZnR3YXJlLk1vZGVsLk9kZHMCAAAAAg0NBwEHASNXVFNvZnR3YXJlLk1vZGVsLk9wZW5TaWRlU3RhdHVzRW51bQIAAAABAR5XVFNvZnR3YXJlLk1vZGVsLlNwb3J0VHlwZUVudW0CAAAACAILU3lzdGVtLkJ5dGUXV1RTb2Z0d2FyZS5Nb2RlbC5Qb2ludHMCAAAAAQUFAgAAABmYAwMAAAG5VA8AAQHbAgAABhEAAAAZV2lsbGlhbSAmIE1hcnkgLSBNYXJzaGFsbAMJEgAAAAAAAAAAAAAAAQkFAAAAAAkFAAAAAAUzMzAuMAMzMDABAzExMAMxMDAKCgkHAAAACQUAAAAAgOHUnlBV1ggAEHlQW1XWCAkFAAAACgAGFgAAAAtMaW5lIENoYW5nZQkFAAAAAAkFAAAACQUAAAAJBQAAAAkFAAAACQUAAAAAAAkFAAAACQUAAAAABej///8jV1RTb2Z0d2Fy\" /><input type=\"hidden\" name=\"__VIEWSTATE12\" id=\"__VIEWSTATE12\" value=\"ZS5Nb2RlbC5PcGVuU2lkZVN0YXR1c0VudW0BAAAAB3ZhbHVlX18AAgIAAAAABhkAAAARQmFza2V0YmFsbCAtIE5DQUEAAAXm////HldUU29mdHdhcmUuTW9kZWwuU3BvcnRUeXBlRW51bQEAAAAHdmFsdWVfXwACAgAAAAQAAAAACQUAAAAACAIBCgkFAAAAAAEwATAKARIAAAAIAAAAAIAwQwYcAAAAAlRPAQALHg9XYWdlcklkT3BlblNpZGVkHgNDV1MFJGFkNGRlMmRkLThkYjMtNDBmMi05NTE2LTM2YWRlZDQwOTQxNR4QSWRMYXN0R2FtZUNoYW5nZSgrBAoxMjAwMTMzMTIzHhJDaGVja0xpbWl0TGFzdERhdGUGEAczn1BV1ggeD0Z1bGxQYXRoQ29udGVzdGUeCmFtb3VudFR5cGUoKXhXVFNvZnR3YXJlLk1vZGVsLkFtb3VudFR5cGVFbnVtLCBBSVQtTW9kZWwsIFZl\" /><input type=\"hidden\" name=\"__VIEWSTATE13\" id=\"__VIEWSTATE13\" value=\"cnNpb249MjAxODIuMy4xNDIxMy4xNDQyLCBDdWx0dXJlPW5ldXRyYWwsIFB1YmxpY0tleVRva2VuPWY2MWQ3ZjliM2YwMGIxNTcEVEFLRR4QQWN0aW9uQWxlcnRTYXZlZGceCXdhZ2VyVHlwZSgpd1dUU29mdHdhcmUuTW9kZWwuV2FnZXJUeXBlRW51bSwgQUlULU1vZGVsLCBWZXJzaW9uPTIwMTgyLjMuMTQyMTMuMTQ0MiwgQ3VsdHVyZT1uZXV0cmFsLCBQdWJsaWNLZXlUb2tlbj1mNjFkN2Y5YjNmMDBiMTU3CFNUUkFJR0hUHgtSb2xsaW5nVHlwZSgpd1dUU29mdHdhcmUuTW9kZWwuSWZCZXRUeXBlRW51bSwgQUlULU1vZGVsLCBWZXJzaW9uPTIwMTgyLjMuMTQyMTMuMTQ0MiwgQ3VsdHVyZT1uZXV0cmFsLCBQdWJsaWNLZXlUb2tlbj1mNjFkN2Y5YjNmMDBiMTU3\" /><input type=\"hidden\" name=\"__VIEWSTATE14\" id=\"__VIEWSTATE14\" value=\"C0lGX1dJTl9PTkxZFgJmD2QWAgIBD2QWEAIBDw8WAh4ISW1hZ2VVcmwFGH4vSW1hZ2VzL2xvZ29fbXNiMjQ3LnBuZ2RkAgIPDxYCHgdWaXNpYmxlaGRkAgQPDxYCHgRUZXh0BQZKRzEwMDdkZAIFDxYCHglpbm5lcmh0bWwFCFVzZXIgSUQ6ZAIGD2QWCgIBD2QWAmYPZBYEAgEPFgIfDgURQ3VycmVudCBCYWxhbmNlOiBkAgMPFgIfDQUyPGZvbnQgY2xhc3M9U3VtbWFyeVBvc2l0aXZlTnVtYmVyPjE0LDQ0NS4wMDwvZm9udD5kAgMPZBYCZg9kFgQCAQ8WAh8OBQtBdmFpbGFibGU6IGQCAw8WAh8NBUo8Zm9udCBpZD0nbGJsQXZhaWxhYmxlQW1vdW50JyBjbGFzcz1TdW1tYXJ5UG9zaXRpdmVOdW1iZXI+MjksODI1LjAwPC9mb250PmQCBQ8WAh8OBQlQZW5kaW5nOiBk\" /><input type=\"hidden\" name=\"__VIEWSTATE15\" id=\"__VIEWSTATE15\" value=\"AgcPFgIfDQVHPGZvbnQgaWQ9J2xibFBlbmRpbmdBbW91bnQnIGNsYXNzPVN1bW1hcnlQb3NpdGl2ZU51bWJlcj40LDYyMC4wMDwvZm9udD5kAgkPFgIfDGgWBgIBD2QWAmYPZBYCAgEPFgIfDgUJRlJFRSBQTEFZZAIDD2QWAgIBDxYCHw4FCUJhbGFuY2U6IGQCBQ9kFgICAQ8WAh8OBQlQZW5kaW5nOiBkAgcPFgIeC18hSXRlbUNvdW50AggWEGYPZBYCZg8VCAxmaXJzdCBhY3RpdmUNYnRuU3BvcnRzTWVudRdTdHJhaWdodEJldE1lbnVFeDIuYXNweAAABlNwb3J0cwAAZAIBD2QWAmYPFQgMbGl2ZUJldHRpbmcgEWJ0bkxpdmVJblBsYXlNZW51GS9DbGllbnQvTGl2ZUJldHRpbmcyLmFzcHgAAARMSVZFAABkAgIPZBYCZg8VCAEgDWJ0bkNhc2lub01lbnUWL0NsaWVu\" /><input type=\"hidden\" name=\"__VIEWSTATE16\" id=\"__VIEWSTATE16\" value=\"dC9DYXNpbm9BU1QuYXNweAAGX2JsYW5rBkNhc2lubwAAZAIDD2QWAmYPFQgBIA1idG5Ib3JzZXNNZW51Fy9DbGllbnQvSG9yc2VXYWdlci5hc3B4AAAGSG9yc2VzAABkAgQPZBYCZg8VCAEgDmJ0bkFjY291bnRNZW51GC9DbGllbnQvRGFpbHlGaWd1cmUuYXNweAAAB0FjY291bnQAAGQCBQ9kFgJmDxUIASAOYnRuUGVuZGluZ01lbnUbL0NsaWVudC9QZW5kaW5nc1dhZ2Vycy5hc3B4AAAHUGVuZGluZwAAZAIGD2QWAmYPFQgBIA1idG5TY29yZXNNZW51MWh0dHA6Ly9zY29yZXMuc3BvcnRzb3B0aW9ucy5jb20vc2NvcmVzL3RvZGF5Lmh0bWwABl9ibGFuawZTY29yZXMAAGQCBw9kFgJmDxUIASAMYnRuUnVsZXNNZW51Ei9DbGllbnQvUnVsZXMuYXNweAAABVJ1bGVz\" /><input type=\"hidden\" name=\"__VIEWSTATE17\" id=\"__VIEWSTATE17\" value=\"AABkAgkPZBYCAgEPZBYCAgEPFgIfDwIEFghmD2QWAmYPFQYHY3VycmVudANyZWQXU3RyYWlnaHRCZXRNZW51RXgyLmFzcHgACVN0cmFpZ2h0cwBkAgEPZBYCZg8VBgAAFVBhcmxheUJldE1lbnVFeDIuYXNweAAHUGFybGF5cwBkAgIPZBYCZg8VBgAAFVRlYXNlckJldE1lbnVFeDIuYXNweAAHVGVhc2VycwBkAgMPZBYCZg8VBgAAGElmQmV0UmV2ZXJzZU1lbnVFeDIuYXNweAASSWYgQmV0cyAvIFJldmVyc2VzCUludmlzaWJsZWQCDA9kFgICAQ9kFgoCAQ9kFgJmD2QWBAIBDxYCHw4FEUN1cnJlbnQgQmFsYW5jZTogZAIDDxYCHw0FMjxmb250IGNsYXNzPVN1bW1hcnlQb3NpdGl2ZU51bWJlcj4xNCw0NDUuMDA8L2ZvbnQ+ZAIDDxYCHwxoFgJmD2QWAgIBDxYCHw4F\" /><input type=\"hidden\" name=\"__VIEWSTATE18\" id=\"__VIEWSTATE18\" value=\"C0F2YWlsYWJsZTogZAIFDxYCHw4FCVBlbmRpbmc6IGQCBw8WAh8NBUc8Zm9udCBpZD0nbGJsUGVuZGluZ0Ftb3VudCcgY2xhc3M9U3VtbWFyeVBvc2l0aXZlTnVtYmVyPjQsNjIwLjAwPC9mb250PmQCCQ8WAh8MaBYGAgEPZBYCZg9kFgICAQ8WAh8OBQlGUkVFIFBMQVlkAgMPZBYCAgEPFgIfDgUJQmFsYW5jZTogZAIFD2QWAgIBDxYCHw4FCVBlbmRpbmc6IGRkeykFlEKNlEVZCtdZ9MJvHJHs3dw=\" /></div><script type=\"text/javascript\">//<![CDATA[var theForm = document.forms['Form1'];if (!theForm) { theForm = document.Form1;}function __doPostBack(eventTarget, eventArgument) { if (!theForm.onsubmit || (theForm.onsubmit() != false)) { theForm.__EVENTTARGET.value = eventTarget; theForm.__EVENTARGUMENT.value = eventArgument; theForm.submit(); }}//]]></script><script src=\"/WebResource.axd?d=Jb6kFaBFm7finf4QmAO8yFE6cC1fENxmf0aRhO2X0KK1MA0km4ExxopQJ0Skdaui19k5MhD4mGFeqKazEFKVjio58XE1&amp;t=636681423604795562\" type=\"text/javascript\"></script><script src=\"/ScriptResource.axd?d=Q1A8Qlz73lgJrOxSZ835LbeaaNyF0-ZUTZ1vRmsk7EgiRfBOBB9mUgl41mTN3zyvUogtmTFB8QBh19trlmhMbfJXDmwz-pqi6kkcsanHE6NcEXrkKyldrrTy3u9KrJk_twBEILD5rYJazip9TOf7ns_Z6tmbIPLu0DSmGlvErlAulses0&amp;t=545ba255\" type=\"text/javascript\"></script><script type=\"text/javascript\">//<![CDATA[if (typeof(Sys) === 'undefined') throw new Error('ASP.NET Ajax client-side framework failed to load.');//]]></script><script src=\"/ScriptResource.axd?d=Yg3CyeT5W5dN3B81YusGU4ziDIY_Gme-bdTeIm_fRJ4ccBlvAxl16-cVvfsosWQ8S_5B8IZhu1ThkrPatj8WQUdeypYXElOfcnbLDsLPqHQ_a0z1_QPm9dtCQsAqwEL9L21NSZQUEd0SI9WbNDje7-FJS6eRnpqYkbZkI1ldGrpK86JP0&amp;t=545ba255\" type=\"text/javascript\"></script><script type=\"text/javascript\">//<![CDATA[Type.registerNamespace('WTSoftwareWeb');WTSoftwareWeb.SessionTimeoutWS=function() {WTSoftwareWeb.SessionTimeoutWS.initializeBase(this);this._timeout = 0;this._userContext = null;this._succeeded = null;this._failed = null;}WTSoftwareWeb.SessionTimeoutWS.prototype={_get_path:function() { var p = this.get_path(); if (p) return p; else return WTSoftwareWeb.SessionTimeoutWS._staticInstance.get_path();},GetSessionTimeout:function(succeededCallback, failedCallback, userContext) {/// <param name=\"succeededCallback\" type=\"Function\" optional=\"true\" mayBeNull=\"true\"></param>/// <param name=\"failedCallback\" type=\"Function\" optional=\"true\" mayBeNull=\"true\"></param>/// <param name=\"userContext\" optional=\"true\" mayBeNull=\"true\"></param>return this._invoke(this._get_path(), 'GetSessionTimeout',false,{},succeededCallback,failedCallback,userContext); },RefreshSession:function(succeededCallback, failedCallback, userContext) {/// <param name=\"succeededCallback\" type=\"Function\" optional=\"true\" mayBeNull=\"true\"></param>/// <param name=\"failedCallback\" type=\"Function\" optional=\"true\" mayBeNull=\"true\"></param>/// <param name=\"userContext\" optional=\"true\" mayBeNull=\"true\"></param>return this._invoke(this._get_path(), 'RefreshSession',false,{},succeededCallback,failedCallback,userContext); }}WTSoftwareWeb.SessionTimeoutWS.registerClass('WTSoftwareWeb.SessionTimeoutWS',Sys.Net.WebServiceProxy);WTSoftwareWeb.SessionTimeoutWS._staticInstance = new WTSoftwareWeb.SessionTimeoutWS();WTSoftwareWeb.SessionTimeoutWS.set_path = function(value) {WTSoftwareWeb.SessionTimeoutWS._staticInstance.set_path(value); }WTSoftwareWeb.SessionTimeoutWS.get_path = function() { /// <value type=\"String\" mayBeNull=\"true\">The service url.</value>return WTSoftwareWeb.SessionTimeoutWS._staticInstance.get_path();}WTSoftwareWeb.SessionTimeoutWS.set_timeout = function(value) {WTSoftwareWeb.SessionTimeoutWS._staticInstance.set_timeout(value); }WTSoftwareWeb.SessionTimeoutWS.get_timeout = function() { /// <value type=\"Number\">The service timeout.</value>return WTSoftwareWeb.SessionTimeoutWS._staticInstance.get_timeout(); }WTSoftwareWeb.SessionTimeoutWS.set_defaultUserContext = function(value) { WTSoftwareWeb.SessionTimeoutWS._staticInstance.set_defaultUserContext(value); }WTSoftwareWeb.SessionTimeoutWS.get_defaultUserContext = function() { /// <value mayBeNull=\"true\">The service default user context.</value>return WTSoftwareWeb.SessionTimeoutWS._staticInstance.get_defaultUserContext(); }WTSoftwareWeb.SessionTimeoutWS.set_defaultSucceededCallback = function(value) { WTSoftwareWeb.SessionTimeoutWS._staticInstance.set_defaultSucceededCallback(value); }WTSoftwareWeb.SessionTimeoutWS.get_defaultSucceededCallback = function() { /// <value type=\"Function\" mayBeNull=\"true\">The service default succeeded callback.</value>return WTSoftwareWeb.SessionTimeoutWS._staticInstance.get_defaultSucceededCallback(); }WTSoftwareWeb.SessionTimeoutWS.set_defaultFailedCallback = function(value) { WTSoftwareWeb.SessionTimeoutWS._staticInstance.set_defaultFailedCallback(value); }WTSoftwareWeb.SessionTimeoutWS.get_defaultFailedCallback = function() { /// <value type=\"Function\" mayBeNull=\"true\">The service default failed callback.</value>return WTSoftwareWeb.SessionTimeoutWS._staticInstance.get_defaultFailedCallback(); }WTSoftwareWeb.SessionTimeoutWS.set_enableJsonp = function(value) { WTSoftwareWeb.SessionTimeoutWS._staticInstance.set_enableJsonp(value); }WTSoftwareWeb.SessionTimeoutWS.get_enableJsonp = function() { /// <value type=\"Boolean\">Specifies whether the service supports JSONP for cross domain calling.</value>return WTSoftwareWeb.SessionTimeoutWS._staticInstance.get_enableJsonp(); }WTSoftwareWeb.SessionTimeoutWS.set_jsonpCallbackParameter = function(value) { WTSoftwareWeb.SessionTimeoutWS._staticInstance.set_jsonpCallbackParameter(value); }WTSoftwareWeb.SessionTimeoutWS.get_jsonpCallbackParameter = function() { /// <value type=\"String\">Specifies the parameter name that contains the callback function name for a JSONP request.</value>return WTSoftwareWeb.SessionTimeoutWS._staticInstance.get_jsonpCallbackParameter(); }WTSoftwareWeb.SessionTimeoutWS.set_path(\"/Services/SessionTimeoutWS.asmx\");WTSoftwareWeb.SessionTimeoutWS.GetSessionTimeout= function(onSuccess,onFailed,userContext) {/// <param name=\"succeededCallback\" type=\"Function\" optional=\"true\" mayBeNull=\"true\"></param>/// <param name=\"failedCallback\" type=\"Function\" optional=\"true\" mayBeNull=\"true\"></param>/// <param name=\"userContext\" optional=\"true\" mayBeNull=\"true\"></param>WTSoftwareWeb.SessionTimeoutWS._staticInstance.GetSessionTimeout(onSuccess,onFailed,userContext); }WTSoftwareWeb.SessionTimeoutWS.RefreshSession= function(onSuccess,onFailed,userContext) {/// <param name=\"succeededCallback\" type=\"Function\" optional=\"true\" mayBeNull=\"true\"></param>/// <param name=\"failedCallback\" type=\"Function\" optional=\"true\" mayBeNull=\"true\"></param>/// <param name=\"userContext\" optional=\"true\" mayBeNull=\"true\"></param>WTSoftwareWeb.SessionTimeoutWS._staticInstance.RefreshSession(onSuccess,onFailed,userContext); }var gtc = Sys.Net.WebServiceProxy._generateTypedConstructor;Type.registerNamespace('WTSoftwareWeb.Helpers.Entities');if (typeof(WTSoftwareWeb.Helpers.Entities.AjaxResultEntity) === 'undefined') {WTSoftwareWeb.Helpers.Entities.AjaxResultEntity=gtc(\"WTSoftwareWeb.Helpers.Entities.AjaxResultEntity\");WTSoftwareWeb.Helpers.Entities.AjaxResultEntity.registerClass('WTSoftwareWeb.Helpers.Entities.AjaxResultEntity');}//]]></script><div>	<input type=\"hidden\" name=\"__VIEWSTATEGENERATOR\" id=\"__VIEWSTATEGENERATOR\" value=\"2104964A\" />	<input type=\"hidden\" name=\"__PREVIOUSPAGE\" id=\"__PREVIOUSPAGE\" value=\"Fr4XB3jJHQGza7Tm3NmQr74P1EGJ_7rgl4T2Az3lItQtefwATEaUEd9JYv0NpbrRpOMPjjjhQjpo8ehXDHNwDPw7MecQiX1dQRkcbIjPDvsAqPvM0\" />	<input type=\"hidden\" name=\"__EVENTVALIDATION\" id=\"__EVENTVALIDATION\" value=\"/wEdAAafZT4TFKlback2RWSoMYCbexAIb1cjB3p/gzcZcUqOEJGirj5lJ9D+8BqNYT42Xjo17tPy0hv0FLjGtQYW9Gy7kZ+/86t4P92I7m787BjFj7wVTqrKQqcoenUqrj2cN3YWyk4mYy4cBSe/wjeP8J0kSe7c2w==\" /></div> <script type=\"text/javascript\">//<![CDATA[Sys.WebForms.PageRequestManager._initialize('ctl00$ScriptManager1', 'Form1', ['tctl00$cphWorkArea$UpdatePanel1','UpdatePanel1'], [], [], 90, 'ctl00');//]]></script> <div class=\"dmmnubtn\">	<span onclick=\"openNav()\"><i class=\"fa fa-reorder\"></i></span> </div> <div class=\"main\"><div id=\"testDiv\" class=\"header\"> <div id=\"myNav\" class=\"overlay\"> <div class=\"overlay-content\"> <a href=\"javascript:void(0)\" class=\"closebtn\" onclick=\"closeNav()\">&times;</a> <div class=\"logo_container\"> <a class=\"logo\" href=\"#\"> <img id=\"mainLogoImage\" src=\"../Images/logo_msb247.png\" style=\"border-width:0px;\" /> </a> </div> <div class=\"deposit-block\"> </div> <div class=\"logoff\"> <input type=\"button\" name=\"ctl00$btnLogOut\" value=\"Log Out\" onclick=\"javascript:WebForm_DoPostBackWithOptions(new WebForm_PostBackOptions(&quot;ctl00$btnLogOut&quot;, &quot;&quot;, false, &quot;&quot;, &quot;../LogOff.aspx&quot;, false, true))\" id=\"btnLogOut\" /> <p> <font> <span id=\"userName\" class=\"userName\">JG1007</span> </font> </p> <span id=\"LabelUserID\">User ID:</span> </div> <div class=\"SummaryContainer\"> <!--PLEASE, DON'T CHANGE ID PROPERTY OF HTML ELEMENTS CAN YOU COMMENT OR DELETE SOMEBODY TAG HTML, BUT NOT CHANGE THE ID PROPERTY --><table class=\"SummaryAccountHeaderClient\" cellpadding=\"0\" cellspacing =\"0\"> <tr> <td class=\"tdSummaryAccountHeaderClient\"> <table class=\"\" cellpadding=\"0\" cellspacing =\"0\" id=\"tblSummaryRegularInfo\"> <tr>	<td class=\"\"> <span id=\"LabelCustomerBalance\">Current Balance: </span><a href=\"/Client/DailyFigure.aspx\"><font class=SummaryPositiveNumber>14,445.00</font></a> </td></tr>	<tr id=\"rowAvailable\">	<td class=\"\"> <span id=\"LabelAvailable\">Available: </span><a href=\"/client/Account.aspx\"><font id='lblAvailableAmount' class=SummaryPositiveNumber>29,825.00</font></a> </td></tr>	<tr>	<td class=\"\"> <span id=\"LabelPending\">Pending: </span><a href=\"../Client/PendingsWagers.aspx\"><font id='lblPendingAmount' class=SummaryPositiveNumber>4,620.00</font></a> </td>	</tr>	</table> </td> </tr></table> </div> <div style=\"position:absolute;top:136px;right:6px;width:167px;z-index:999;display:none\"> <div id=\"google_translate_element\"></div> <script type=\"text/javascript\">	function googleTranslateElementInit() {	new google.translate.TranslateElement({ pageLanguage: 'en', includedLanguages: 'en,es,hi,it,ko,ru,vi,zh-CN,zh-TW', layout: google.translate.TranslateElement.InlineLayout.SIMPLE, autoDisplay: false }, 'google_translate_element');	}</script><script type=\"text/javascript\" src=\"//translate.google.com/translate_a/element.js?cb=googleTranslateElementInit\"></script> </div> <div class=\"menu\"> <div class=\"main_menu\"> <ul> <li class='first active' id='btnSportsMenu'><a href='StraightBetMenuEx2.aspx' onclick='' target=''> Sports </a> <i></i></li> <li class='liveBetting ' id='btnLiveInPlayMenu'><a href='/Client/LiveBetting2.aspx' onclick='' target=''> LIVE </a> <i></i></li> <li class=' ' id='btnCasinoMenu'><a href='/Client/CasinoAST.aspx' onclick='' target='_blank'> Casino </a> <i></i></li> <li class=' ' id='btnHorsesMenu'><a href='/Client/HorseWager.aspx' onclick='' target=''> Horses </a> <i></i></li> <li class=' ' id='btnAccountMenu'><a href='/Client/DailyFigure.aspx' onclick='' target=''> Account </a> <i></i></li> <li class=' ' id='btnPendingMenu'><a href='/Client/PendingsWagers.aspx' onclick='' target=''> Pending </a> <i></i></li> <li class=' ' id='btnScoresMenu'><a href='http://scores.sportsoptions.com/scores/today.html' onclick='' target='_blank'> Scores </a> <i></i></li> <li class=' ' id='btnRulesMenu'><a href='/Client/Rules.aspx' onclick='' target=''> Rules </a> <i></i></li> </ul> </div> </div> <div id=\"headerWorkPlace\"> <div class=\"headerContinueSearch\"> </div></div>	</div>	</div> <div id=\"subMenuContainer\"> <div class=\"sportsSubmenu\"> <div class=\"bg1\"> <div class=\"bg2\"> <ul> <li id='current'><a class='red' href='StraightBetMenuEx2.aspx' onclick=''> Straights</a> <i class=''></i></li> <li id=''><a class='' href='ParlayBetMenuEx2.aspx' onclick=''> Parlays</a> <i class=''></i></li> <li id=''><a class='' href='TeaserBetMenuEx2.aspx' onclick=''> Teasers</a> <i class=''></i></li> <li id=''><a class='' href='IfBetReverseMenuEx2.aspx' onclick=''> If Bets / Reverses</a> <i class='Invisible'></i></li> </ul> </div> </div> </div></div> </div> <div class=\"content_holder\"> <div Class=\"HorsesLabel tsprtss\"><span></span></div> <script type=\"text/javascript\" language=\"javascript\"> Sys.WebForms.PageRequestManager.getInstance().add_beginRequest(BeginRequestHandler); Sys.WebForms.PageRequestManager.getInstance().add_endRequest(EndRequestHandler); function BeginRequestHandler(sender, args) { $.blockUI({ message: $('#domMessage') }); } function EndRequestHandler(sender, args) { $.unblockUI(); } function TryToExport() { //$('#btnExportWagers').click(); var ids = $('#wagerType').val(); window.open('/Helpers/PrintTicket.aspx?data=' + ids); } </script> <div id=\"domMessage\" style=\"display: none;\"> <table align=\"center\"> <tr> <td> <img width=\"120px\" height=\"120px\" src=\"../Images/ajax-loading.gif\" alt=\"\" /> </td> <td> <span style=\"font: bold 18px Arial; color: #800000\">Processing your request...</span> </td> </tr> </table> </div> <div id=\"UpdatePanel1\">	<span id=\"lblInfoPrincipal\"><br><br><table class=\"tblConfirmWager\" border=\"0\">	<tr>	<td class=\"HeaderTicket\" align=\"left\" colspan=\"2\">StraightsBet&nbsp;&nbsp;&nbsp;&nbsp;11/28/2018 5:43:27 PM - ET&nbsp;&nbsp;&nbsp;&nbsp;</td><td class=\"thRisk\" align=\"left\">Risk</td><td class=\"thWin\" align=\"left\" colspan=\"2\">Win</td>	</tr><tr class=\"BackgroundLineAlternateGray\">	<td class=\"selection_cell\" align=\"left\">Selection 1: </td><td class=\"TdWagerDetailTicket\" align=\"left\"><div class='TdDetailTicket'> <font class='SportTicket'>Basketball - NCAA</font> <br /> <font class='TeamRotationTicket'>[731]</font> <font class='TeamNameTicket'>William & Mary - Marshall</font> <font class='TeamDateTicket'>11/28/2018 <font class='GameTime'>(07:00 PM)</font></font> <br /> <font class='TargetTicket'></font> <font class='TargetTicket'>Over</font> <font class='PointsTicket'> 176Â½</font> <font class='ScoreTypeTicket'> Points</font> <font class='OddsTicket'> -110</font> <font class='PeriodTypeTicket'> for the Game <font class='LineChange'>(Line Change)</font> <font class='LineChange'></font></font> <div class='RegulationOnlyText'></div> <font class='BuyPointTicket'></font> <font class='PitcherTicket'></font> <font class='AdjustOddsTicket'></font> </div></td><td class=\"risk_cell\">330.00</td><td class=\"win_cell\">300.00</td><td class=\"TdRemove\" align=\"left\"><a id=\"cmdRemove/0\" class=\"buttonRemove btn_blue\" runat=\"server\" href=\"javascript:__doPostBack(&#39;ctl00$cphWorkArea$cmdRemove/0&#39;,&#39;&#39;)\"><span>Remove</span></a></td>	</tr><tr>	<td class=\"tdLineChangeMessage\" align=\"left\" colspan=\"6\">At least one line (or price or pitcher) has changed. Please review each wager carefully then you may press the remove button, re-enter password to confirm wagers or press cancel to return to the wager menu</td>	</tr><tr>	<td class=\"TdMessageIncorrectPassword\" align=\"left\" colspan=\"6\"><span></span></td>	</tr><tr>	<td align=\"center\" colspan=\"4\"><font class='labelPassword'>Password::</font><input name=\"ctl00$cphWorkArea$txtConfirmPassword\" type=\"password\" id=\"txtConfirmPassword\" class=\"txtPassword\" />&nbsp;&nbsp;<input type=\"submit\" name=\"ctl00$cphWorkArea$cmdSubmit\" value=\"Submit\" id=\"cmdSubmit\" title=\"Send Wager\" class=\"Submitbutton\" runat=\"server\" /></td>	</tr>	</table><br><br></span> </div><input type=\"submit\" name=\"ctl00$cphWorkArea$btnExportWagers\" value=\"\" id=\"btnExportWagers\" style=\"display:none;\" /> </div> </div> <div class=\"footer\"> <div class=\"footerWorkPlace\"> </div> </div> <div style=\"display: none;\"> </div></form><script type=\"text/javascript\"> var NotificationStartTime = 60000; //milliseconds before session time out var PopupRefreshDelay = 10000; //milliseconds between popup refresh var timeout; var firstTime = true; var counter = 60; var interval; object = { func: function() { WTSoftwareWeb.SessionTimeoutWS.GetSessionTimeout(OnSuccessTimeoutWS, OnFailureTimeoutWS); } } $(document).ready(function() { WTSoftwareWeb.SessionTimeoutWS.GetSessionTimeout(OnSuccessTimeoutWS, OnFailureTimeoutWS); }); function RedirectToLoginPage() { //$('input[type=button][value=\"Log Out\"]').click();//Logout btnLogOut $('#btnLogOut').click();//Logout btnLogOut } function RefreshSession() { WTSoftwareWeb.SessionTimeoutWS.RefreshSession(OnSuccessTimeoutWS, OnFailureTimeoutWS); firstTime = true; clearInterval(interval); counter = 60; try { keepSessionAlive(); } catch (err) { } } function OnSuccessTimeoutWS(response) { if (response && response.IsAutentificated) { if (response.Result) { var TimeBeforeTimeOut = parseInt(response.Result) if (TimeBeforeTimeOut > NotificationStartTime) { TimeOutPopup.Hide(); timeout = setTimeout(function() { object.func.call(object) } , TimeBeforeTimeOut - NotificationStartTime); } else { TimeOutPopup.Show(); timeout = setTimeout(function() { object.func.call(object) } , PopupRefreshDelay); if (firstTime) { interval = setInterval(function() { counter--; if (counter >= 0) { $(\"#countDown\").text(counter.toString()); } }, 1000); } firstTime = false; } } } else { RedirectToLoginPage(); } } function OnFailureTimeoutWS(response) { RedirectToLoginPage(); } var TimeOutPopup = { Show: function () { $('#SessionTimeoutPopup').show(); }, Hide: function () { $('#SessionTimeoutPopup').hide(); }};</script><div id=\"SessionTimeoutPopup\" class=\"SessionTimeoutScreen\"> <div class=\"SessionTimeoutPopup\" > <div class=\"PopupHeader\"> <div class=\"ReasonCaption\"> Your Session is about to expire </div> </div> <div class=\"PopupBody\"> <div> <p> You've been inactive for a while. For your security, we'll automatically sign out in approximately <label id=\"countDown\">60</label> seconds. </p> </div> <div class=\"ButtonHolder\"> <input type=\"button\" id=\"btnSignedOut\" onclick=\"RedirectToLoginPage();\" value=\"Sign out\" class=\"TimeoutButton\" /> <input type=\"button\" id=\"btnTimeout\" onclick=\"RefreshSession();\" value=\"Stay signed in\" class=\"TimeoutButton\" /> </div> </div> </div></div>	<script language=\"javascript\" type=\"text/javascript\"> $(document).ready(function () { $(\"#txtConfirmPassword\").focus();	}); </script><script type=\"text/javascript\" src=\"/Scripts/Ait.js?v=20\"></script> <script type=\"text/javascript\" src=\"/Scripts/wise.js?v=20\"></script> <script type=\"text/javascript\" src=\"/Scripts/Wagers.js?v=20\"></script> <script type=\"text/javascript\" src=\"/Scripts/element.js\"></script> <script type=\"text/javascript\" src=\"/Scripts/jquery/jquery.blockUI.js\"></script> <script type=\"text/javascript\" src=\"/Scripts/CustomerAjax.js?v=20\"></script> <script type=\"text/javascript\" src=\"/Scripts/jquery.validationEngine-en.js\"></script> <script type=\"text/javascript\" src=\"/Scripts/jquery.validationEngine.js\"></script> <script type=\"text/javascript\" src=\"/Scripts/Ajax.js?v=20\"></script> <script type=\"text/javascript\" language=\"javascript\"> var orden = 1; var onSubmit = false; function OnSubmitRequest() { if (!onSubmit) { onSubmit = true; return true; } return false; } function deleteorder(id) { if (document.forms[0]['txtOrden'].value.indexOf(id) != -1) { document.forms[0]['txtOrden'].value = document.forms[0]['txtOrden'].value.replace((id + '@'), ' '); } } function sideorder(obj1) { var valor = ''; valor = obj1.name; deleteorder(valor); if (obj1.value != 0 || obj1.value != '') { document.forms[0]['txtOrden'].value = document.forms[0]['txtOrden'].value + valor + '@'; } return true; } function OnChecked(count, chkName, disabled) { var i; for (i = 2; i <= count; i++) { if (disabled == 'False') { document.getElementById(chkName + i).checked = false; document.forms[0]['txtWagerTypeParlay'].checked = false; } else { document.getElementById(chkName + i).checked = false; document.forms[0]['txtWagerType'].checked = false; } } } function test() { if (document.forms[0]['txtWagerTypeParlay'].checked == true) { return false; } return true; } function setfocus() { document.getElementById('txtWagerAmount').focus() } function mp_onload() { if (window.body_onload != null) window.body_onload(); } function imageButtonOnKeyPress() { var keyCodeEntered = (window.event.which) ? window.event.which : window.event.keyCode; if (keyCodeEntered == 13) { event.returnValue = false; return false; } return true; } function ValidateLiveCasino(URL, clientCode) { var items = JSON.stringify({ clientCode: clientCode }); var isOk = false; $.ajax({ type: \"POST\", url: \"ValidateLiveCasinoLCD.aspx/ValidateClientBalance\", data: items, async: false, contentType: \"application/json;charset=utf-8\", dataType: \"json\", success: function (result) { res = result.d if (res) { // window.open(URL, \"Live Casino\", \"fullscreen=yes\"); isOk = true; } else { alert(\"No balance is available to transfer. Please try again later.\"); } }, error: function (xhr, errorType, exception) { return false; OnFail(xhr, errorType, exception); } }); return isOk; } function OnFail(xhr, errorType, exception) { alert(\"Message: \" + xhr.statusText); } $(document).keypress(function (e) { if (e.which == 13) { $(\"#cmdContinue\").click(); } }); </script> <script> /* Open when someone clicks on the span element */ function openNav() { document.getElementById(\"myNav\").style.width = \"100%\"; } /* Close when someone clicks on the \"x\" symbol inside the overlay */ function closeNav() {	document.getElementById(\"myNav\").style.width = \"0%\"; } </script> </script> <script type=\"text/javascript\"> if (window.addEventListener) { // Mozilla, Netscape, Firefox	window.addEventListener('load', WindowLoad, false); } else if (window.attachEvent) { // IE	window.attachEvent('onload', WindowLoad); } function WindowLoad(event) { document.getElementsByClassName(\"thHeaderTtForParlay\"); insertAdjacentHTML('colSpan=\"2\"') } </script></body></html>";
//			String xhtml = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta name=\"google-translate-customization\" content=\"3233c073ab0f4e8f-812f5fbec3c21e36-gd944dbbbb163dd6b-17\" /><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><meta http-equiv=\"Content-Script-Type\" content=\"text/javascript\" /><meta http-equiv=\"Content-Style-Type\" content=\"text/css\" /><title>betos365</title><link rel=\"shortcut icon\" href=\"/images/ikon_betos365.ico\" /><meta name=\"keywords\" lang=\"en\" content=\"Title\" /><meta name=\"description\" lang=\"en\" content=\"Title\" /><meta name=\"language\" content=\"en\" /><meta http-equiv=\"imagetoolbar\" content=\"no\" /><meta name=\"apple-mobile-web-app-capable\" content=\"yes\"/><meta name=\"apple-mobile-web-app-status-bar-style\" content=\"black\"/><!-- BEGIN PUSH IN MENU--><link rel=\"stylesheet\" type=\"text/css\" href=\"/Scripts/jpush/css/demo.css\" /><!-- END PUSH IN MENU--><style type=\"text/css\">@import \"/Themes/Theme012/Styles/AllAjax.css?v=20\"; @import \"/Themes/Theme012/Styles/ClientStyle.css?v=20\"; @import \"/Themes/Theme012/Styles/AjaxClientStyle.css?v=20\"; @import \"/Themes/Theme012/Styles/Style.css?v=20\"; @import \"/Themes/Theme012/Styles/StyleTabs.css?v=20\"; @import \"/Themes/Theme012/Styles/SessionTimeoutPopup.css?v=20\"; @import \"/App_Themes/WtSoftware/ComboBox.WTSoftware.css?v=20\"; @import \"/App_Themes/WtSoftware/TreeView.WTSoftware.css?v=20\"; @import \"/App_Themes/WtSoftware/Menu.WTSoftware.css?v=20\";</style><style type=\"text/css\">.HorsesLabel {	min-height: 15px;}.dmmnubtn{visibility:hidden;display: none;}.closebtn{display: none;}</style><link href=\"Styles/jquery.cluetip.css\" rel=\"stylesheet\" type=\"text/css\" /><script type=\"text/javascript\" src=\"/Scripts/jquery/jquery-1.9.1.min.js\"></script><link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\"><script>if(this != top){ top.document.location.href = this.document.location.href;}</script><script src='scripts/prefixfree.min.js'></script></head><body onload=\"mp_onload();\" class=\"mpbody_betos365\"><form name=\"Form1\" method=\"post\" action=\"./WagerTicket.aspx\" id=\"Form1\" style=\"min-height: 100%; height: 100% !important\"><div><input type=\"hidden\" name=\"__EVENTTARGET\" id=\"__EVENTTARGET\" value=\"\" /><input type=\"hidden\" name=\"__EVENTARGUMENT\" id=\"__EVENTARGUMENT\" value=\"\" /><input type=\"hidden\" name=\"__VIEWSTATEFIELDCOUNT\" id=\"__VIEWSTATEFIELDCOUNT\" value=\"19\" /><input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"/wEPDwUKMjA4Mzk3MTI5MA8WFh4RSWRMYXN0U2hhZGVDaGFuZ2UoKVlTeXN0ZW0uSW50NjQsIG1zY29ybGliLCBWZXJzaW9uPTQuMC4wLjAsIEN1bHR1cmU9bmV1dHJhbCwgUHVibGljS2V5VG9rZW49Yjc3YTVjNTYxOTM0ZTA4OQcxMjU0MDIwHglXYWdlclNhdmUyvSMAAQAAAP////8BAAAAAAAAAAwCAAAAV0FJVC1Nb2RlbCwgVmVyc2lvbj0yMDE4Mi4zLjE0MjEzLjE0NDIsIEN1bHR1cmU9bmV1dHJhbCwgUHVibGljS2V5VG9rZW49ZjYxZDdmOWIzZjAwYjE1NwQBAAAAlAFTeXN0ZW0uQ29sbGVjdGlvbnMuR2VuZXJpYy5MaXN0YDFbW1dUU29mdHdhcmUuTW9kZWwuV2FnZXIsIEFJVC1Nb2RlbCwgVmVyc2lvbj0yMDE4Mi4zLjE0MjEzLjE0NDIsIEN1bHR1cmU9\" /><input type=\"hidden\" name=\"__VIEWSTATE1\" id=\"__VIEWSTATE1\" value=\"bmV1dHJhbCwgUHVibGljS2V5VG9rZW49ZjYxZDdmOWIzZjAwYjE1N11dAwAAAAZfaXRlbXMFX3NpemUIX3ZlcnNpb24EAAAYV1RTb2Z0d2FyZS5Nb2RlbC5XYWdlcltdAgAAAAgICQMAAAABAAAAAQAAAAcDAAAAAAEAAAAEAAAABBZXVFNvZnR3YXJlLk1vZGVsLldhZ2VyAgAAAAkEAAAADQMFBAAAABZXVFNvZnR3YXJlLk1vZGVsLldhZ2VyJwAAAA1fdGlja2V0TnVtYmVyCF93YWdlcklkCl93YWdlclR5cGUMX2dldGxheVdhZ2VyDF9hbW91bnRUb0JldAxfYW1vdW50VG9XaW4IX29kZHNMYXkJX29kZHNUYWtlC193YWdlclN0YXRlD19hY3Rpb25PbklmQmV0cwxfcG9pbnRzQWRkZWQRX2Zyb21Db21wbGV4V2FnZXIKX2RhdGVTYXZlZA1fZGF0ZVJlc29sdmVkCV9p\" /><input type=\"hidden\" name=\"__VIEWSTATE2\" id=\"__VIEWSTATE2\" value=\"ZFRlYXNlcglfaWRQYXJsYXkRX25hbWVQYXJsYXlUZWFzZXILX2lzRnJlZVBsYXkOX2xzdFdhZ2VyU2lkZXMMX2N1cnJlbnRPZGRzDl9jdXJyZW50UG9pbnRzDl9jdXJyZW50UGF5T3V0CV9jb21tZW50cwdfc291cmNlCV91c2VyQ29kZQlfY29tcHV0ZXIPX2lzUm9sbGluZ0lmQmV0Dl9yb2xsaW5nTnVtYmVyD193YWdlcklkUm9sbGluZwtfY2xpZW50Q29kZRFfY29tYmluYXRpb25Db3VudBBfY29tYmluYXRpb25MaXN0D19kZWJpdFRvUGVuZGluZxNfbGl2ZUJldHRpbmdXYWdlcklkIDxDaGFydFBlcmNlbnRhZ2U+a19fQmFja2luZ0ZpZWxkGjxBZ2VudENvZGU+a19fQmFja2luZ0ZpZWxkIjxBdXRob3JpemF0aW9uVHlwZT5rX19CYWNraW5nRmllbGQnPEV4b3Rp\" /><input type=\"hidden\" name=\"__VIEWSTATE3\" id=\"__VIEWSTATE3\" value=\"Y1NwZWNpYWxXYWdlckxpc3Q+a19fQmFja2luZ0ZpZWxkHzxDaGVja0F2YWlsYWJsZT5rX19CYWNraW5nRmllbGQAAAAAAAAAAAAAAAEAAAAAAQADBAQEAQABAQAAAAEAAwAAAAEEAwAICAICBQUFBQICCw0NBwcBmQFTeXN0ZW0uQ29sbGVjdGlvbnMuR2VuZXJpYy5MaXN0YDFbW1dUU29mdHdhcmUuTW9kZWwuV2FnZXJTaWRlcywgQUlULU1vZGVsLCBWZXJzaW9uPTIwMTgyLjMuMTQyMTMuMTQ0MiwgQ3VsdHVyZT1uZXV0cmFsLCBQdWJsaWNLZXlUb2tlbj1mNjFkN2Y5YjNmMDBiMTU3XV0VV1RTb2Z0d2FyZS5Nb2RlbC5PZGRzAgAAABdXVFNvZnR3YXJlLk1vZGVsLlBvaW50cwIAAAAXV1RTb2Z0d2FyZS5Nb2RlbC5QYXlPdXQCAAAAAgEHCAh/U3lzdGVtLkNvbGxl\" /><input type=\"hidden\" name=\"__VIEWSTATE4\" id=\"__VIEWSTATE4\" value=\"Y3Rpb25zLkdlbmVyaWMuTGlzdGAxW1tTeXN0ZW0uU3RyaW5nLCBtc2NvcmxpYiwgVmVyc2lvbj00LjAuMC4wLCBDdWx0dXJlPW5ldXRyYWwsIFB1YmxpY0tleVRva2VuPWI3N2E1YzU2MTkzNGUwODldXQUJAiZXVFNvZnR3YXJlLk1vZGVsLkF1dGhvcml6YXRpb25UeXBlRW51bQIAAACUAVN5c3RlbS5Db2xsZWN0aW9ucy5HZW5lcmljLkxpc3RgMVtbV1RTb2Z0d2FyZS5Nb2RlbC5XYWdlciwgQUlULU1vZGVsLCBWZXJzaW9uPTIwMTgyLjMuMTQyMTMuMTQ0MiwgQ3VsdHVyZT1uZXV0cmFsLCBQdWJsaWNLZXlUb2tlbj1mNjFkN2Y5YjNmMDBiMTU3XV0BAgAAAIOJBQMAAAAAAQEHMTEwMC4wMAYxMDAwLjADMTEwAzEwMAEAAAAAAAYFAAAAAIBCMw5QWdYIgEIzDlBZ\" /><input type=\"hidden\" name=\"__VIEWSTATE5\" id=\"__VIEWSTATE5\" value=\"1ggAAAAACgAJBgAAAAkHAAAACQgAAAAJCQAAAAkFAAAAAAoKAAAAAAAAAAoAAAAACgEwAAAAAAAAAAAACgX1////JldUU29mdHdhcmUuTW9kZWwuQXV0aG9yaXphdGlvblR5cGVFbnVtAQAAAAd2YWx1ZV9fAAICAAAAAAkMAAAAAAQGAAAAmQFTeXN0ZW0uQ29sbGVjdGlvbnMuR2VuZXJpYy5MaXN0YDFbW1dUU29mdHdhcmUuTW9kZWwuV2FnZXJTaWRlcywgQUlULU1vZGVsLCBWZXJzaW9uPTIwMTgyLjMuMTQyMTMuMTQ0MiwgQ3VsdHVyZT1uZXV0cmFsLCBQdWJsaWNLZXlUb2tlbj1mNjFkN2Y5YjNmMDBiMTU3XV0DAAAABl9pdGVtcwVfc2l6ZQhfdmVyc2lvbgQAAB1XVFNvZnR3YXJlLk1vZGVsLldhZ2VyU2lkZXNbXQIAAAAICAkNAAAAAQAAAAEAAAAFBwAAABVX\" /><input type=\"hidden\" name=\"__VIEWSTATE6\" id=\"__VIEWSTATE6\" value=\"VFNvZnR3YXJlLk1vZGVsLk9kZHMHAAAACF9vZGRzTGF5CV9vZGRzVGFrZQZfdmFsaWQLX0FkanVzdE9kZHMQX29yaWdpbmFsT2Rkc0xheRFfb3JpZ2luYWxPZGRzVGFrZQlfcm91bmRpbmcAAAAAAAAABQUBAQUFAgIAAAADMTEwAzEwMAEAATABMAAFCAAAABdXVFNvZnR3YXJlLk1vZGVsLlBvaW50cwQAAAAHX3BvaW50cwpfcG9pbnRUeXBlBl92YWxpZAxfQWRqdXN0UG9pbnQAAQAACwEBAgAAAAAAIMEGDgAAAAJQUwEABQkAAAAXV1RTb2Z0d2FyZS5Nb2RlbC5QYXlPdXQEAAAACl9BbW91bnRMYXkLX0Ftb3VudFRha2UHX2Ftb3VudAtfaXNXaW5XYWdlcgAAAAAFBQUBAgAAAAcxMTAwLjAwBjEwMDAuMAYxMDAwLjABAQwAAAABAAAACQ8AAAAAAAAAAAAAAAcNAAAA\" /><input type=\"hidden\" name=\"__VIEWSTATE7\" id=\"__VIEWSTATE7\" value=\"AAEAAAAEAAAABBtXVFNvZnR3YXJlLk1vZGVsLldhZ2VyU2lkZXMCAAAACRAAAAANAwcPAAAAAAEAAAAAAAAABBZXVFNvZnR3YXJlLk1vZGVsLldhZ2VyAgAAAAUQAAAAG1dUU29mdHdhcmUuTW9kZWwuV2FnZXJTaWRlczoAAAANX3RpY2tldE51bWJlcghfd2FnZXJJZBBfd2FnZXJTaWRlTnVtYmVyB19nYW1lSWQLX3BlcmlvZFR5cGULX3RlYW1OdW1iZXIPX3JvdGF0aW9uTnVtYmVyCV9zaWRlTmFtZQxfd2FnZXJUYXJnZXQHX3BvaW50cxBfcG9pbnRzQWRkZWRTaWRlDV90ZWFzZXJQb2ludHMKX3NpZGVTdGF0ZRFfZmlyc3RUZWFtUGl0Y2hlchZfZmlyc3RQaXRjaGVyTXVzdFN0YXJ0El9zZWNvbmRUZWFtUGl0Y2hlchdfc2Vjb25kUGl0Y2hlck11c3RTdGFydBBf\" /><input type=\"hidden\" name=\"__VIEWSTATE8\" id=\"__VIEWSTATE8\" value=\"YW1vdW50VG9CZXRTaWRlEF9hbW91bnRUb1dpblNpZGUQX2dldGxheVdhZ2VyU2lkZQxfb2Rkc0xheVNpZGUNX29kZHNUYWtlU2lkZRVfb3JpZ2luYWxfT2Rkc0xheVNpZGUWX29yaWdpbmFsX29kZHNUYWtlU2lkZQxfY3VycmVudE9kZHMRX2V4dHJhSW5mb3JtYXRpb24IX2FwcGxpZWQRX2RhdGVSZXNvbHZlZFNpZGUJX2dhbWVUaW1lCV9jb21tZW50cwhfaWRTcG9ydAtfbGluZUNoYW5nZQ5fcGl0Y2hlckNoYW5nZQdfeF90b195EV9ncmFkZGluZ0NvbW1lbnRzDV9kZXNjcmlwdGlvbjENX2Rlc2NyaXB0aW9uMg1fZGVzY3JpcHRpb24zDV9kZXNjcmlwdGlvbjQSX2lkQ29udGVzdENhdGVnb3J5DF9jb250ZXN0TmFtZQZfdW5pdHMGX2ZpeGVkD19vcGVuU2lkZVN0\" /><input type=\"hidden\" name=\"__VIEWSTATE9\" id=\"__VIEWSTATE9\" value=\"YXR1cxFfZGVzY3JpcHRpb25TcG9ydB88aXNNYW51YWxseU9kZHM+a19fQmFja2luZ0ZpZWxkJDxBcHBseVJlZ3VsYXRpb25Pbmx5PmtfX0JhY2tpbmdGaWVsZBo8U3BvcnRUeXBlPmtfX0JhY2tpbmdGaWVsZBw8T2ZmZXJMaW5lSUQ+a19fQmFja2luZ0ZpZWxkGzxPZmZlckxpbmVzPmtfX0JhY2tpbmdGaWVsZCE8Q3VycmVudFNoYWRlVHlwZT5rX19CYWNraW5nRmllbGQZPFJvdW5kaW5nPmtfX0JhY2tpbmdGaWVsZCA8U2hhZGVUZWFzZXJMaW5lPmtfX0JhY2tpbmdGaWVsZBs8R2FtZUhlYWRlcj5rX19CYWNraW5nRmllbGQcPFBheU91dE1heGVkPmtfX0JhY2tpbmdGaWVsZCg8QW1vdW50VG9CZXRNYXhlZFBhaWRPdXQ+a19fQmFja2luZ0ZpZWxkKDxBbW91bnRU\" /><input type=\"hidden\" name=\"__VIEWSTATE10\" id=\"__VIEWSTATE10\" value=\"b1dpbk1heGVkUGFpZE91dD5rX19CYWNraW5nRmllbGQjPFBheU91dE1heGVkTWVzc2FnZT5rX19CYWNraW5nRmllbGQAAAAAAAAAAQAEAAAAAQABAAAAAAAAAwMEAQAAAAEAAQEAAQEBAQEAAQEABAEAAAQAAQADBAEAAAABCAcCCAICCAIXV1RTb2Z0d2FyZS5Nb2RlbC5Qb2ludHMCAAAACwsCAgIFBQIFBXBTeXN0ZW0uTnVsbGFibGVgMVtbU3lzdGVtLkRlY2ltYWwsIG1zY29ybGliLCBWZXJzaW9uPTQuMC4wLjAsIEN1bHR1cmU9bmV1dHJhbCwgUHVibGljS2V5VG9rZW49Yjc3YTVjNTYxOTM0ZTA4OV1dcFN5c3RlbS5OdWxsYWJsZWAxW1tTeXN0ZW0uRGVjaW1hbCwgbXNjb3JsaWIsIFZlcnNpb249NC4wLjAuMCwgQ3VsdHVyZT1uZXV0cmFsLCBQdWJsaWNLZXlU\" /><input type=\"hidden\" name=\"__VIEWSTATE11\" id=\"__VIEWSTATE11\" value=\"b2tlbj1iNzdhNWM1NjE5MzRlMDg5XV0VV1RTb2Z0d2FyZS5Nb2RlbC5PZGRzAgAAAAINDQcBBwEjV1RTb2Z0d2FyZS5Nb2RlbC5PcGVuU2lkZVN0YXR1c0VudW0CAAAAAQEeV1RTb2Z0d2FyZS5Nb2RlbC5TcG9ydFR5cGVFbnVtAgAAAAgCC1N5c3RlbS5CeXRlF1dUU29mdHdhcmUuTW9kZWwuUG9pbnRzAgAAAAEFBQIAAACDiQUDAAABoV4PAAMCCgIAAAYRAAAAClBpdHRzYnVyZ2gCCRIAAAAAAAAAAAAAAAEJBQAAAAAJBQAAAAAHMTEwMC4wMAYxMDAwLjABAzExMAMxMDAKCgkHAAAACQUAAAAAgEIzDlBZ1ggA0IkkSVnWCAkFAAAA2QAGFgAAAAtMaW5lIENoYW5nZQkFAAAAAAkFAAAACQUAAAAJBQAAAAkFAAAACQUAAAAAAAkFAAAACQUAAAAABej///8jV1RTb2Z0\" /><input type=\"hidden\" name=\"__VIEWSTATE12\" id=\"__VIEWSTATE12\" value=\"d2FyZS5Nb2RlbC5PcGVuU2lkZVN0YXR1c0VudW0BAAAAB3ZhbHVlX18AAgIAAAAABhkAAAAhQmFza2V0YmFsbCAtIE5DQUEgLSBBZGRlZCAvIEV4dHJhAAAF5v///x5XVFNvZnR3YXJlLk1vZGVsLlNwb3J0VHlwZUVudW0BAAAAB3ZhbHVlX18AAgIAAAAEAAAAAAkFAAAAAAgCAQoJBQAAAAABMAEwCgESAAAACAAAAAAAKMEGHAAAAAJQUwEACx4PV2FnZXJJZE9wZW5TaWRlZB4DQ1dTBSQzMGZhM2ZkMi00OTgxLTQyNmYtYTYxMi1kYjQ3MTczMmZlYjUeC1JvbGxpbmdUeXBlKCl3V1RTb2Z0d2FyZS5Nb2RlbC5JZkJldFR5cGVFbnVtLCBBSVQtTW9kZWwsIFZlcnNpb249MjAxODIuMy4xNDIxMy4xNDQyLCBDdWx0dXJlPW5ldXRyYWwsIFB1YmxpY0tleVRva2VuPWY2\" /><input type=\"hidden\" name=\"__VIEWSTATE13\" id=\"__VIEWSTATE13\" value=\"MWQ3ZjliM2YwMGIxNTcLSUZfV0lOX09OTFkeEElkTGFzdEdhbWVDaGFuZ2UoKwQKMTIwMTM3NTg2MR4PRnVsbFBhdGhDb250ZXN0ZR4KYW1vdW50VHlwZSgpeFdUU29mdHdhcmUuTW9kZWwuQW1vdW50VHlwZUVudW0sIEFJVC1Nb2RlbCwgVmVyc2lvbj0yMDE4Mi4zLjE0MjEzLjE0NDIsIEN1bHR1cmU9bmV1dHJhbCwgUHVibGljS2V5VG9rZW49ZjYxZDdmOWIzZjAwYjE1NwRUQUtFHhBBY3Rpb25BbGVydFNhdmVkZx4Jd2FnZXJUeXBlKCl3V1RTb2Z0d2FyZS5Nb2RlbC5XYWdlclR5cGVFbnVtLCBBSVQtTW9kZWwsIFZlcnNpb249MjAxODIuMy4xNDIxMy4xNDQyLCBDdWx0dXJlPW5ldXRyYWwsIFB1YmxpY0tleVRva2VuPWY2MWQ3ZjliM2YwMGIxNTcIU1RSQUlH\" /><input type=\"hidden\" name=\"__VIEWSTATE14\" id=\"__VIEWSTATE14\" value=\"SFQeEkNoZWNrTGltaXRMYXN0RGF0ZQbgArgOUFnWCBYCZg9kFgICAQ9kFhICAQ8PFgIeCEltYWdlVXJsBRp+L0ltYWdlcy9sb2dvX2JldG9zMzY1LnBuZ2RkAgIPDxYCHgdWaXNpYmxlaGRkAgQPDxYCHgRUZXh0BQVRNTgzMmRkAgUPFgIeCWlubmVyaHRtbAUIVXNlciBJRDpkAgYPZBYKAgEPZBYCZg9kFgQCAQ8WAh8OBRFDdXJyZW50IEJhbGFuY2U6IGQCAw8WAh8NBTE8Zm9udCBjbGFzcz1TdW1tYXJ5UG9zaXRpdmVOdW1iZXI+OSw1NTMuMTQ8L2ZvbnQ+ZAIDD2QWAmYPZBYEAgEPFgIfDgULQXZhaWxhYmxlOiBkAgMPFgIfDQVKPGZvbnQgaWQ9J2xibEF2YWlsYWJsZUFtb3VudCcgY2xhc3M9U3VtbWFyeVBvc2l0aXZlTnVtYmVyPjM1LDE1My4xNDwvZm9udD5k\" /><input type=\"hidden\" name=\"__VIEWSTATE15\" id=\"__VIEWSTATE15\" value=\"AgUPFgIfDgUJUGVuZGluZzogZAIHDxYCHw0FRzxmb250IGlkPSdsYmxQZW5kaW5nQW1vdW50JyBjbGFzcz1TdW1tYXJ5UG9zaXRpdmVOdW1iZXI+NCw0MDAuMDA8L2ZvbnQ+ZAIJDxYCHwxoFgYCAQ9kFgJmD2QWAgIBDxYCHw4FCUZSRUUgUExBWWQCAw9kFgICAQ8WAh8OBQlCYWxhbmNlOiBkAgUPZBYCAgEPFgIfDgUJUGVuZGluZzogZAIHDxYCHgtfIUl0ZW1Db3VudAIHFg5mD2QWAmYPFQgMZmlyc3QgYWN0aXZlDWJ0blNwb3J0c01lbnUWU3RyYWlnaHRCZXRNZW51RXguYXNweAAABlNwb3J0cwAAZAIBD2QWAmYPFQgMbGl2ZUJldHRpbmcgEWJ0bkxpdmVJblBsYXlNZW51GS9DbGllbnQvTGl2ZUJldHRpbmcyLmFzcHgAAARMSVZFAABkAgIPZBYCZg8VCAEgDWJ0\" /><input type=\"hidden\" name=\"__VIEWSTATE16\" id=\"__VIEWSTATE16\" value=\"bkhvcnNlc01lbnUXL0NsaWVudC9Ib3JzZVdhZ2VyLmFzcHgAAAZIb3JzZXMAAGQCAw9kFgJmDxUIASAOYnRuQWNjb3VudE1lbnUYL0NsaWVudC9EYWlseUZpZ3VyZS5hc3B4AAAHQWNjb3VudAAAZAIED2QWAmYPFQgBIA5idG5QZW5kaW5nTWVudRsvQ2xpZW50L1BlbmRpbmdzV2FnZXJzLmFzcHgAAAdQZW5kaW5nAABkAgUPZBYCZg8VCAEgDWJ0blNjb3Jlc01lbnUxaHR0cDovL3Njb3Jlcy5zcG9ydHNvcHRpb25zLmNvbS9zY29yZXMvdG9kYXkuaHRtbAAGX2JsYW5rBlNjb3JlcwAAZAIGD2QWAmYPFQgBIAxidG5SdWxlc01lbnUSL0NsaWVudC9SdWxlcy5hc3B4AAAFUnVsZXMAAGQCCQ9kFgICAQ9kFgICAQ8WAh8PAgQWCGYPZBYCZg8VBgdjdXJyZW50A3JlZBZT\" /><input type=\"hidden\" name=\"__VIEWSTATE17\" id=\"__VIEWSTATE17\" value=\"dHJhaWdodEJldE1lbnVFeC5hc3B4AAlTdHJhaWdodHMAZAIBD2QWAmYPFQYAABRQYXJsYXlCZXRNZW51RXguYXNweAAHUGFybGF5cwBkAgIPZBYCZg8VBgAAFFRlYXNlckJldE1lbnVFeC5hc3B4AAdUZWFzZXJzAGQCAw9kFgJmDxUGAAAXSWZCZXRSZXZlcnNlTWVudUV4LmFzcHgAEklmIEJldHMgLyBSZXZlcnNlcwlJbnZpc2libGVkAgsPZBYCAgMPZBYCZg9kFgICAQ9kFgICAg9kFgICAw9kFgJmD2QWAmYPDxYGHw1lHghDc3NDbGFzcwUPSW52YWxpZFBhc3N3b3JkHgRfIVNCAgJkZAIMD2QWAgIBD2QWCgIBD2QWAmYPZBYEAgEPFgIfDgURQ3VycmVudCBCYWxhbmNlOiBkAgMPFgIfDQUxPGZvbnQgY2xhc3M9U3VtbWFyeVBvc2l0aXZlTnVtYmVyPjksNTUzLjE0\" /><input type=\"hidden\" name=\"__VIEWSTATE18\" id=\"__VIEWSTATE18\" value=\"PC9mb250PmQCAw8WAh8MaBYCZg9kFgICAQ8WAh8OBQtBdmFpbGFibGU6IGQCBQ8WAh8OBQlQZW5kaW5nOiBkAgcPFgIfDQVHPGZvbnQgaWQ9J2xibFBlbmRpbmdBbW91bnQnIGNsYXNzPVN1bW1hcnlQb3NpdGl2ZU51bWJlcj40LDQwMC4wMDwvZm9udD5kAgkPFgIfDGgWBgIBD2QWAmYPZBYCAgEPFgIfDgUJRlJFRSBQTEFZZAIDD2QWAgIBDxYCHw4FCUJhbGFuY2U6IGQCBQ9kFgICAQ8WAh8OBQlQZW5kaW5nOiBkZEm4/JRYfQgPyGCrxT4NdQYelnFd\" /></div><script type=\"text/javascript\">//<![CDATA[var theForm = document.forms['Form1'];if (!theForm) { theForm = document.Form1;}function __doPostBack(eventTarget, eventArgument) { if (!theForm.onsubmit || (theForm.onsubmit() != false)) { theForm.__EVENTTARGET.value = eventTarget; theForm.__EVENTARGUMENT.value = eventArgument; theForm.submit(); }}//]]></script><script src=\"/WebResource.axd?d=Jb6kFaBFm7finf4QmAO8yFE6cC1fENxmf0aRhO2X0KK1MA0km4ExxopQJ0Skdaui19k5MhD4mGFeqKazEFKVjio58XE1&amp;t=636681423604795562\" type=\"text/javascript\"></script><script src=\"/ScriptResource.axd?d=Q1A8Qlz73lgJrOxSZ835LbeaaNyF0-ZUTZ1vRmsk7EgiRfBOBB9mUgl41mTN3zyvUogtmTFB8QBh19trlmhMbfJXDmwz-pqi6kkcsanHE6NcEXrkKyldrrTy3u9KrJk_twBEILD5rYJazip9TOf7ns_Z6tmbIPLu0DSmGlvErlAulses0&amp;t=545ba255\" type=\"text/javascript\"></script><script type=\"text/javascript\">//<![CDATA[if (typeof(Sys) === 'undefined') throw new Error('ASP.NET Ajax client-side framework failed to load.');//]]></script><script src=\"/ScriptResource.axd?d=Yg3CyeT5W5dN3B81YusGU4ziDIY_Gme-bdTeIm_fRJ4ccBlvAxl16-cVvfsosWQ8S_5B8IZhu1ThkrPatj8WQUdeypYXElOfcnbLDsLPqHQ_a0z1_QPm9dtCQsAqwEL9L21NSZQUEd0SI9WbNDje7-FJS6eRnpqYkbZkI1ldGrpK86JP0&amp;t=545ba255\" type=\"text/javascript\"></script><script type=\"text/javascript\">//<![CDATA[Type.registerNamespace('WTSoftwareWeb');WTSoftwareWeb.SessionTimeoutWS=function() {WTSoftwareWeb.SessionTimeoutWS.initializeBase(this);this._timeout = 0;this._userContext = null;this._succeeded = null;this._failed = null;}WTSoftwareWeb.SessionTimeoutWS.prototype={_get_path:function() { var p = this.get_path(); if (p) return p; else return WTSoftwareWeb.SessionTimeoutWS._staticInstance.get_path();},GetSessionTimeout:function(succeededCallback, failedCallback, userContext) {/// <param name=\"succeededCallback\" type=\"Function\" optional=\"true\" mayBeNull=\"true\"></param>/// <param name=\"failedCallback\" type=\"Function\" optional=\"true\" mayBeNull=\"true\"></param>/// <param name=\"userContext\" optional=\"true\" mayBeNull=\"true\"></param>return this._invoke(this._get_path(), 'GetSessionTimeout',false,{},succeededCallback,failedCallback,userContext); },RefreshSession:function(succeededCallback, failedCallback, userContext) {/// <param name=\"succeededCallback\" type=\"Function\" optional=\"true\" mayBeNull=\"true\"></param>/// <param name=\"failedCallback\" type=\"Function\" optional=\"true\" mayBeNull=\"true\"></param>/// <param name=\"userContext\" optional=\"true\" mayBeNull=\"true\"></param>return this._invoke(this._get_path(), 'RefreshSession',false,{},succeededCallback,failedCallback,userContext); }}WTSoftwareWeb.SessionTimeoutWS.registerClass('WTSoftwareWeb.SessionTimeoutWS',Sys.Net.WebServiceProxy);WTSoftwareWeb.SessionTimeoutWS._staticInstance = new WTSoftwareWeb.SessionTimeoutWS();WTSoftwareWeb.SessionTimeoutWS.set_path = function(value) {WTSoftwareWeb.SessionTimeoutWS._staticInstance.set_path(value); }WTSoftwareWeb.SessionTimeoutWS.get_path = function() { /// <value type=\"String\" mayBeNull=\"true\">The service url.</value>return WTSoftwareWeb.SessionTimeoutWS._staticInstance.get_path();}WTSoftwareWeb.SessionTimeoutWS.set_timeout = function(value) {WTSoftwareWeb.SessionTimeoutWS._staticInstance.set_timeout(value); }WTSoftwareWeb.SessionTimeoutWS.get_timeout = function() { /// <value type=\"Number\">The service timeout.</value>return WTSoftwareWeb.SessionTimeoutWS._staticInstance.get_timeout(); }WTSoftwareWeb.SessionTimeoutWS.set_defaultUserContext = function(value) { WTSoftwareWeb.SessionTimeoutWS._staticInstance.set_defaultUserContext(value); }WTSoftwareWeb.SessionTimeoutWS.get_defaultUserContext = function() { /// <value mayBeNull=\"true\">The service default user context.</value>return WTSoftwareWeb.SessionTimeoutWS._staticInstance.get_defaultUserContext(); }WTSoftwareWeb.SessionTimeoutWS.set_defaultSucceededCallback = function(value) { WTSoftwareWeb.SessionTimeoutWS._staticInstance.set_defaultSucceededCallback(value); }WTSoftwareWeb.SessionTimeoutWS.get_defaultSucceededCallback = function() { /// <value type=\"Function\" mayBeNull=\"true\">The service default succeeded callback.</value>return WTSoftwareWeb.SessionTimeoutWS._staticInstance.get_defaultSucceededCallback(); }WTSoftwareWeb.SessionTimeoutWS.set_defaultFailedCallback = function(value) { WTSoftwareWeb.SessionTimeoutWS._staticInstance.set_defaultFailedCallback(value); }WTSoftwareWeb.SessionTimeoutWS.get_defaultFailedCallback = function() { /// <value type=\"Function\" mayBeNull=\"true\">The service default failed callback.</value>return WTSoftwareWeb.SessionTimeoutWS._staticInstance.get_defaultFailedCallback(); }WTSoftwareWeb.SessionTimeoutWS.set_enableJsonp = function(value) { WTSoftwareWeb.SessionTimeoutWS._staticInstance.set_enableJsonp(value); }WTSoftwareWeb.SessionTimeoutWS.get_enableJsonp = function() { /// <value type=\"Boolean\">Specifies whether the service supports JSONP for cross domain calling.</value>return WTSoftwareWeb.SessionTimeoutWS._staticInstance.get_enableJsonp(); }WTSoftwareWeb.SessionTimeoutWS.set_jsonpCallbackParameter = function(value) { WTSoftwareWeb.SessionTimeoutWS._staticInstance.set_jsonpCallbackParameter(value); }WTSoftwareWeb.SessionTimeoutWS.get_jsonpCallbackParameter = function() { /// <value type=\"String\">Specifies the parameter name that contains the callback function name for a JSONP request.</value>return WTSoftwareWeb.SessionTimeoutWS._staticInstance.get_jsonpCallbackParameter(); }WTSoftwareWeb.SessionTimeoutWS.set_path(\"/Services/SessionTimeoutWS.asmx\");WTSoftwareWeb.SessionTimeoutWS.GetSessionTimeout= function(onSuccess,onFailed,userContext) {/// <param name=\"succeededCallback\" type=\"Function\" optional=\"true\" mayBeNull=\"true\"></param>/// <param name=\"failedCallback\" type=\"Function\" optional=\"true\" mayBeNull=\"true\"></param>/// <param name=\"userContext\" optional=\"true\" mayBeNull=\"true\"></param>WTSoftwareWeb.SessionTimeoutWS._staticInstance.GetSessionTimeout(onSuccess,onFailed,userContext); }WTSoftwareWeb.SessionTimeoutWS.RefreshSession= function(onSuccess,onFailed,userContext) {/// <param name=\"succeededCallback\" type=\"Function\" optional=\"true\" mayBeNull=\"true\"></param>/// <param name=\"failedCallback\" type=\"Function\" optional=\"true\" mayBeNull=\"true\"></param>/// <param name=\"userContext\" optional=\"true\" mayBeNull=\"true\"></param>WTSoftwareWeb.SessionTimeoutWS._staticInstance.RefreshSession(onSuccess,onFailed,userContext); }var gtc = Sys.Net.WebServiceProxy._generateTypedConstructor;Type.registerNamespace('WTSoftwareWeb.Helpers.Entities');if (typeof(WTSoftwareWeb.Helpers.Entities.AjaxResultEntity) === 'undefined') {WTSoftwareWeb.Helpers.Entities.AjaxResultEntity=gtc(\"WTSoftwareWeb.Helpers.Entities.AjaxResultEntity\");WTSoftwareWeb.Helpers.Entities.AjaxResultEntity.registerClass('WTSoftwareWeb.Helpers.Entities.AjaxResultEntity');}//]]></script><div>	<input type=\"hidden\" name=\"__VIEWSTATEGENERATOR\" id=\"__VIEWSTATEGENERATOR\" value=\"2104964A\" />	<input type=\"hidden\" name=\"__PREVIOUSPAGE\" id=\"__PREVIOUSPAGE\" value=\"Fr4XB3jJHQGza7Tm3NmQr74P1EGJ_7rgl4T2Az3lItQtefwATEaUEd9JYv0NpbrRpOMPjjjhQjpo8ehXDHNwDPw7MecQiX1dQRkcbIjPDvsAqPvM0\" />	<input type=\"hidden\" name=\"__EVENTVALIDATION\" id=\"__EVENTVALIDATION\" value=\"/wEdAAbnbboZcNgsA/ZaLkICaQatexAIb1cjB3p/gzcZcUqOEJGirj5lJ9D+8BqNYT42Xjo17tPy0hv0FLjGtQYW9Gy7kZ+/86t4P92I7m787BjFj7wVTqrKQqcoenUqrj2cN3awyvLlwzpX49Uo39AzBlPLf/DPZg==\" /></div> <script type=\"text/javascript\">//<![CDATA[Sys.WebForms.PageRequestManager._initialize('ctl00$ScriptManager1', 'Form1', ['tctl00$cphWorkArea$UpdatePanel1','UpdatePanel1'], [], [], 90, 'ctl00');//]]></script> <div class=\"dmmnubtn\">	<span onclick=\"openNav()\"><i class=\"fa fa-reorder\"></i></span> </div> <div class=\"main\"><div id=\"testDiv\" class=\"header\"> <div id=\"myNav\" class=\"overlay\"> <div class=\"overlay-content\"> <a href=\"javascript:void(0)\" class=\"closebtn\" onclick=\"closeNav()\">&times;</a> <div class=\"logo_container\"> <a class=\"logo\" href=\"#\"> <img id=\"mainLogoImage\" src=\"../Images/logo_betos365.png\" style=\"border-width:0px;\" /> </a> </div> <div class=\"deposit-block\"> </div> <div class=\"logoff\"> <input type=\"button\" name=\"ctl00$btnLogOut\" value=\"Log Out\" onclick=\"javascript:WebForm_DoPostBackWithOptions(new WebForm_PostBackOptions(&quot;ctl00$btnLogOut&quot;, &quot;&quot;, false, &quot;&quot;, &quot;../LogOff.aspx&quot;, false, true))\" id=\"btnLogOut\" /> <p> <font> <span id=\"userName\" class=\"userName\">Q5832</span> </font> </p> <span id=\"LabelUserID\">User ID:</span> </div> <div class=\"SummaryContainer\"> <!--PLEASE, DON'T CHANGE ID PROPERTY OF HTML ELEMENTS CAN YOU COMMENT OR DELETE SOMEBODY TAG HTML, BUT NOT CHANGE THE ID PROPERTY --><table class=\"SummaryAccountHeaderClient\" cellpadding=\"0\" cellspacing =\"0\"> <tr> <td class=\"tdSummaryAccountHeaderClient\"> <table class=\"\" cellpadding=\"0\" cellspacing =\"0\" id=\"tblSummaryRegularInfo\"> <tr>	<td class=\"\"> <span id=\"LabelCustomerBalance\">Current Balance: </span><a href=\"/Client/DailyFigure.aspx\"><font class=SummaryPositiveNumber>9,553.14</font></a> </td></tr>	<tr id=\"rowAvailable\">	<td class=\"\"> <span id=\"LabelAvailable\">Available: </span><a href=\"/client/Account.aspx\"><font id='lblAvailableAmount' class=SummaryPositiveNumber>35,153.14</font></a> </td></tr>	<tr>	<td class=\"\"> <span id=\"LabelPending\">Pending: </span><a href=\"../Client/PendingsWagers.aspx\"><font id='lblPendingAmount' class=SummaryPositiveNumber>4,400.00</font></a> </td>	</tr>	</table> </td> </tr></table> </div> <div style=\"position:absolute;top:136px;right:6px;width:167px;z-index:999;display:none\"> <div id=\"google_translate_element\"></div> <script type=\"text/javascript\">	function googleTranslateElementInit() {	new google.translate.TranslateElement({ pageLanguage: 'en', includedLanguages: 'en,es,hi,it,ko,ru,vi,zh-CN,zh-TW', layout: google.translate.TranslateElement.InlineLayout.SIMPLE, autoDisplay: false }, 'google_translate_element');	}</script><script type=\"text/javascript\" src=\"//translate.google.com/translate_a/element.js?cb=googleTranslateElementInit\"></script> </div> <div class=\"menu\"> <div class=\"main_menu\"> <ul> <li class='first active' id='btnSportsMenu'><a href='StraightBetMenuEx.aspx' onclick='' target=''> Sports </a> <i></i></li> <li class='liveBetting ' id='btnLiveInPlayMenu'><a href='/Client/LiveBetting2.aspx' onclick='' target=''> LIVE </a> <i></i></li> <li class=' ' id='btnHorsesMenu'><a href='/Client/HorseWager.aspx' onclick='' target=''> Horses </a> <i></i></li> <li class=' ' id='btnAccountMenu'><a href='/Client/DailyFigure.aspx' onclick='' target=''> Account </a> <i></i></li> <li class=' ' id='btnPendingMenu'><a href='/Client/PendingsWagers.aspx' onclick='' target=''> Pending </a> <i></i></li> <li class=' ' id='btnScoresMenu'><a href='http://scores.sportsoptions.com/scores/today.html' onclick='' target='_blank'> Scores </a> <i></i></li> <li class=' ' id='btnRulesMenu'><a href='/Client/Rules.aspx' onclick='' target=''> Rules </a> <i></i></li> </ul> </div> </div> <div id=\"headerWorkPlace\"> <div class=\"headerContinueSearch\"> </div></div>	</div>	</div> <div id=\"subMenuContainer\"> <div class=\"sportsSubmenu\"> <div class=\"bg1\"> <div class=\"bg2\"> <ul> <li id='current'><a class='red' href='StraightBetMenuEx.aspx' onclick=''> Straights</a> <i class=''></i></li> <li id=''><a class='' href='ParlayBetMenuEx.aspx' onclick=''> Parlays</a> <i class=''></i></li> <li id=''><a class='' href='TeaserBetMenuEx.aspx' onclick=''> Teasers</a> <i class=''></i></li> <li id=''><a class='' href='IfBetReverseMenuEx.aspx' onclick=''> If Bets / Reverses</a> <i class='Invisible'></i></li> </ul> </div> </div> </div></div> </div> <div class=\"content_holder\"> <div Class=\"HorsesLabel tsprtss\"><span></span></div> <script type=\"text/javascript\" language=\"javascript\"> Sys.WebForms.PageRequestManager.getInstance().add_beginRequest(BeginRequestHandler); Sys.WebForms.PageRequestManager.getInstance().add_endRequest(EndRequestHandler); function BeginRequestHandler(sender, args) { $.blockUI({ message: $('#domMessage') }); } function EndRequestHandler(sender, args) { $.unblockUI(); } function TryToExport() { //$('#btnExportWagers').click(); var ids = $('#wagerType').val(); window.open('/Helpers/PrintTicket.aspx?data=' + ids); } </script> <div id=\"domMessage\" style=\"display: none;\"> <table align=\"center\"> <tr> <td> <img width=\"120px\" height=\"120px\" src=\"../Images/ajax-loading.gif\" alt=\"\" /> </td> <td> <span style=\"font: bold 18px Arial; color: #800000\">Processing your request...</span> </td> </tr> </table> </div> <div id=\"UpdatePanel1\">	<span id=\"lblInfoPrincipal\"><br><br><br><br><table class=\"tblConfirmWager\" border=\"0\">	<tr>	<td class=\"HeaderTicket\" align=\"left\" colspan=\"2\">StraightsBet&nbsp;&nbsp;&nbsp;&nbsp;12/3/2018 7:49:34 PM - ET&nbsp;&nbsp;&nbsp;&nbsp;</td><td class=\"thRisk\" align=\"left\">Risk</td><td class=\"thWin\" align=\"left\" colspan=\"2\">Win</td>	</tr><tr class=\"BackgroundLineGreen\">	<td class=\"selection_cell\" align=\"left\">Selection 1: </td><td class=\"TdWagerDetailTicket\" align=\"left\"><div class='TdDetailTicket'> <font class='SportTicket'>Basketball - NCAA - Added / Extra</font> <br /> <font class='TeamRotationTicket'>[522]</font> <font class='TeamNameTicket'>Pittsburgh</font> <font class='TeamDateTicket'>12/03/2018 <font class='GameTime'>(07:00 PM)</font></font> <br /> <font class='TargetTicket'></font> <font class='TargetTicket'></font> <font class='PointsTicket'> -10Â½</font> <font class='ScoreTypeTicket'> Points</font> <font class='OddsTicket'> -110</font> <font class='PeriodTypeTicket'> for the 2nd Half <font class='LineChange'>(Line Change)</font> <font class='LineChange'></font></font> <div class='RegulationOnlyText'></div> <font class='BuyPointTicket'></font> <font class='PitcherTicket'></font> <font class='AdjustOddsTicket'></font> </div></td><td class=\"risk_cell\">1,100.00</td><td class=\"win_cell\">1,000.00</td><td class=\"TdRemove\" align=\"left\"><a id=\"cmdRemove/0\" class=\"buttonRemove btn_blue\" runat=\"server\" href=\"javascript:__doPostBack(&#39;ctl00$cphWorkArea$cmdRemove/0&#39;,&#39;&#39;)\"><span>Remove</span></a></td>	</tr><tr>	<td class=\"tdLineChangeMessage\" align=\"left\" colspan=\"6\">At least one line (or price or pitcher) has changed. Please review each wager carefully then you may press the remove button, re-enter password to confirm wagers or press cancel to return to the wager menu</td>	</tr><tr>	<td class=\"TdMessageIncorrectPassword\" align=\"left\" colspan=\"6\"><span class=\"InvalidPassword\"></span></td>	</tr><tr>	<td align=\"center\" colspan=\"4\"><font class='labelPassword'>Password::</font><input name=\"ctl00$cphWorkArea$txtConfirmPassword\" type=\"password\" id=\"txtConfirmPassword\" class=\"txtPassword\" />&nbsp;&nbsp;<input type=\"submit\" name=\"ctl00$cphWorkArea$cmdSubmit\" value=\"Submit\" id=\"cmdSubmit\" title=\"Send Wager\" class=\"Submitbutton\" runat=\"server\" /></td>	</tr>	</table></span> </div><input type=\"submit\" name=\"ctl00$cphWorkArea$btnExportWagers\" value=\"\" id=\"btnExportWagers\" style=\"display:none;\" /> </div> </div> <div class=\"footer\"> <div class=\"footerWorkPlace\"> </div> </div> <div style=\"display: none;\"> </div></form><script type=\"text/javascript\"> var NotificationStartTime = 60000; //milliseconds before session time out var PopupRefreshDelay = 10000; //milliseconds between popup refresh var timeout; var firstTime = true; var counter = 60; var interval; object = { func: function() { WTSoftwareWeb.SessionTimeoutWS.GetSessionTimeout(OnSuccessTimeoutWS, OnFailureTimeoutWS); } } $(document).ready(function() { WTSoftwareWeb.SessionTimeoutWS.GetSessionTimeout(OnSuccessTimeoutWS, OnFailureTimeoutWS); }); function RedirectToLoginPage() { //$('input[type=button][value=\"Log Out\"]').click();//Logout btnLogOut	$('#btnLogOut').click();//Logout btnLogOut } function RefreshSession() { WTSoftwareWeb.SessionTimeoutWS.RefreshSession(OnSuccessTimeoutWS, OnFailureTimeoutWS); firstTime = true; clearInterval(interval); counter = 60; try { keepSessionAlive(); } catch (err) { } } function OnSuccessTimeoutWS(response) { if (response && response.IsAutentificated) { if (response.Result) { var TimeBeforeTimeOut = parseInt(response.Result) if (TimeBeforeTimeOut > NotificationStartTime) { TimeOutPopup.Hide(); timeout = setTimeout(function() { object.func.call(object) } , TimeBeforeTimeOut - NotificationStartTime); } else { TimeOutPopup.Show(); timeout = setTimeout(function() { object.func.call(object) } , PopupRefreshDelay); if (firstTime) { interval = setInterval(function() { counter--; if (counter >= 0) { $(\"#countDown\").text(counter.toString()); } }, 1000); } firstTime = false; } } } else { RedirectToLoginPage(); } } function OnFailureTimeoutWS(response) { RedirectToLoginPage(); } var TimeOutPopup = { Show: function () { $('#SessionTimeoutPopup').show(); }, Hide: function () { $('#SessionTimeoutPopup').hide(); }};</script><div id=\"SessionTimeoutPopup\" class=\"SessionTimeoutScreen\"> <div class=\"SessionTimeoutPopup\" > <div class=\"PopupHeader\"> <div class=\"ReasonCaption\"> Your Session is about to expire </div> </div> <div class=\"PopupBody\"> <div> <p> You've been inactive for a while. For your security, we'll automatically sign out in approximately <label id=\"countDown\">60</label> seconds. </p> </div> <div class=\"ButtonHolder\"> <input type=\"button\" id=\"btnSignedOut\" onclick=\"RedirectToLoginPage();\" value=\"Sign out\" class=\"TimeoutButton\" /> <input type=\"button\" id=\"btnTimeout\" onclick=\"RefreshSession();\" value=\"Stay signed in\" class=\"TimeoutButton\" /> </div> </div> </div></div>	<script language=\"javascript\" type=\"text/javascript\"> $(document).ready(function () { $(\"#txtConfirmPassword\").focus();	}); </script><script type=\"text/javascript\" src=\"/Scripts/Ait.js?v=20\"></script> <script type=\"text/javascript\" src=\"/Scripts/wise.js?v=20\"></script> <script type=\"text/javascript\" src=\"/Scripts/Wagers.js?v=20\"></script> <script type=\"text/javascript\" src=\"/Scripts/element.js\"></script> <script type=\"text/javascript\" src=\"/Scripts/jquery/jquery.blockUI.js\"></script> <script type=\"text/javascript\" src=\"/Scripts/CustomerAjax.js?v=20\"></script> <script type=\"text/javascript\" src=\"/Scripts/jquery.validationEngine-en.js\"></script> <script type=\"text/javascript\" src=\"/Scripts/jquery.validationEngine.js\"></script> <script type=\"text/javascript\" src=\"/Scripts/Ajax.js?v=20\"></script> <script type=\"text/javascript\" language=\"javascript\"> var orden = 1; var onSubmit = false; function OnSubmitRequest() { if (!onSubmit) { onSubmit = true; return true; } return false; } function deleteorder(id) { if (document.forms[0]['txtOrden'].value.indexOf(id) != -1) { document.forms[0]['txtOrden'].value = document.forms[0]['txtOrden'].value.replace((id + '@'), ' '); } } function sideorder(obj1) { var valor = ''; valor = obj1.name; deleteorder(valor); if (obj1.value != 0 || obj1.value != '') { document.forms[0]['txtOrden'].value = document.forms[0]['txtOrden'].value + valor + '@'; } return true; } function OnChecked(count, chkName, disabled) { var i; for (i = 2; i <= count; i++) { if (disabled == 'False') { document.getElementById(chkName + i).checked = false; document.forms[0]['txtWagerTypeParlay'].checked = false; } else { document.getElementById(chkName + i).checked = false; document.forms[0]['txtWagerType'].checked = false; } } } function test() { if (document.forms[0]['txtWagerTypeParlay'].checked == true) { return false; } return true; } function setfocus() { document.getElementById('txtWagerAmount').focus() } function mp_onload() { if (window.body_onload != null) window.body_onload(); } function imageButtonOnKeyPress() { var keyCodeEntered = (window.event.which) ? window.event.which : window.event.keyCode; if (keyCodeEntered == 13) { event.returnValue = false; return false; } return true; } function ValidateLiveCasino(URL, clientCode) { var items = JSON.stringify({ clientCode: clientCode }); var isOk = false; $.ajax({ type: \"POST\", url: \"ValidateLiveCasinoLCD.aspx/ValidateClientBalance\", data: items, async: false, contentType: \"application/json;charset=utf-8\", dataType: \"json\", success: function (result) { res = result.d if (res) { // window.open(URL, \"Live Casino\", \"fullscreen=yes\"); isOk = true; } else { alert(\"No balance is available to transfer. Please try again later.\"); } }, error: function (xhr, errorType, exception) { return false; OnFail(xhr, errorType, exception); } }); return isOk; } function OnFail(xhr, errorType, exception) { alert(\"Message: \" + xhr.statusText); } $(document).keypress(function (e) { if (e.which == 13) { $(\"#cmdContinue\").click(); } }); </script> <script> /* Open when someone clicks on the span element */ function openNav() { document.getElementById(\"myNav\").style.width = \"100%\"; } /* Close when someone clicks on the \"x\" symbol inside the overlay */ function closeNav() {	document.getElementById(\"myNav\").style.width = \"0%\"; } </script> </script> <script type=\"text/javascript\"> if (window.addEventListener) { // Mozilla, Netscape, Firefox	window.addEventListener('load', WindowLoad, false); } else if (window.attachEvent) { // IE	window.attachEvent('onload', WindowLoad); } function WindowLoad(event) { document.getElementsByClassName(\"thHeaderTtForParlay\"); insertAdjacentHTML('colSpan=\"2\"') } </script></body></html>";
//			String xhtml = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head> <meta name=\"google-translate-customization\" content=\"3233c073ab0f4e8f-812f5fbec3c21e36-gd944dbbbb163dd6b-17\" /> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /> <meta http-equiv=\"Content-Script-Type\" content=\"text/javascript\" /> <meta http-equiv=\"Content-Style-Type\" content=\"text/css\" /> <title>WINDYCITY</title> <link rel=\"shortcut icon\" href=\"/images/ikon_WINDYCITY.ico\" /> <meta name=\"keywords\" lang=\"en\" content=\"Title\" /> <meta name=\"description\" lang=\"en\" content=\"Title\" /> <meta name=\"language\" content=\"en\" /> <meta http-equiv=\"imagetoolbar\" content=\"no\" /> <style type=\"text/css\"> @import \"/Themes/Theme001/Styles/AllAjax.css?v=5\"; @import \"/Themes/Theme001/Styles/ClientStyle.css?v=5\"; @import \"/Themes/Theme001/Styles/AjaxClientStyle.css?v=5\"; @import \"/Themes/Theme001/Styles/Style.css?v=5\"; @import \"/Themes/Theme001/Styles/StyleTabs.css?v=5\"; @import \"/Themes/Theme001/Styles/SessionTimeoutPopup.css?v=5\"; @import \"/App_Themes/WtSoftware/ComboBox.WTSoftware.css?v=5\"; @import \"/App_Themes/WtSoftware/TreeView.WTSoftware.css?v=5\"; @import \"/App_Themes/WtSoftware/Menu.WTSoftware.css?v=5\"; </style> <link href=\"Styles/jquery.cluetip.css\" rel=\"stylesheet\" type=\"text/css\" /> <script type=\"text/javascript\" src=\"/Scripts/jquery/jquery-1.9.1.min.js\"></script> <script>if(this != top){ top.document.location.href = this.document.location.href;}</script></head><body onload=\"mp_onload();\"> <form name=\"Form1\" method=\"post\" action=\"./WagerTicket.aspx\" id=\"Form1\" style=\"min-height: 100%; height: 100% !important\"><div><input type=\"hidden\" name=\"__EVENTTARGET\" id=\"__EVENTTARGET\" value=\"\" /><input type=\"hidden\" name=\"__EVENTARGUMENT\" id=\"__EVENTARGUMENT\" value=\"\" /><input type=\"hidden\" name=\"__VIEWSTATEFIELDCOUNT\" id=\"__VIEWSTATEFIELDCOUNT\" value=\"19\" /><input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"/wEPDwULLTExMDY5MzczOTYPFhYeEUlkTGFzdFNoYWRlQ2hhbmdlKClZU3lzdGVtLkludDY0LCBtc2NvcmxpYiwgVmVyc2lvbj00LjAuMC4wLCBDdWx0dXJlPW5ldXRyYWwsIFB1YmxpY0tleVRva2VuPWI3N2E1YzU2MTkzNGUwODkGMjQ1NzMxHglXYWdlclNhdmUyrSMAAQAAAP////8BAAAAAAAAAAwCAAAAV0FJVC1Nb2RlbCwgVmVyc2lvbj0yMDE3NC40LjE0MjEzLjE0NDIsIEN1bHR1cmU9bmV1dHJhbCwgUHVibGljS2V5VG9rZW49ZjYxZDdmOWIzZjAwYjE1NwQBAAAAlAFTeXN0ZW0uQ29sbGVjdGlvbnMuR2VuZXJpYy5MaXN0YDFbW1dUU29mdHdhcmUuTW9kZWwuV2FnZXIsIEFJVC1Nb2RlbCwgVmVyc2lvbj0yMDE3NC40LjE0MjEzLjE0NDIsIEN1bHR1cmU9\" /><input type=\"hidden\" name=\"__VIEWSTATE1\" id=\"__VIEWSTATE1\" value=\"bmV1dHJhbCwgUHVibGljS2V5VG9rZW49ZjYxZDdmOWIzZjAwYjE1N11dAwAAAAZfaXRlbXMFX3NpemUIX3ZlcnNpb24EAAAYV1RTb2Z0d2FyZS5Nb2RlbC5XYWdlcltdAgAAAAgICQMAAAABAAAAAQAAAAcDAAAAAAEAAAAEAAAABBZXVFNvZnR3YXJlLk1vZGVsLldhZ2VyAgAAAAkEAAAADQMFBAAAABZXVFNvZnR3YXJlLk1vZGVsLldhZ2VyJwAAAA1fdGlja2V0TnVtYmVyCF93YWdlcklkCl93YWdlclR5cGUMX2dldGxheVdhZ2VyDF9hbW91bnRUb0JldAxfYW1vdW50VG9XaW4IX29kZHNMYXkJX29kZHNUYWtlC193YWdlclN0YXRlD19hY3Rpb25PbklmQmV0cwxfcG9pbnRzQWRkZWQRX2Zyb21Db21wbGV4V2FnZXIKX2RhdGVTYXZlZA1fZGF0ZVJlc29sdmVkCV9p\" /><input type=\"hidden\" name=\"__VIEWSTATE2\" id=\"__VIEWSTATE2\" value=\"ZFRlYXNlcglfaWRQYXJsYXkRX25hbWVQYXJsYXlUZWFzZXILX2lzRnJlZVBsYXkOX2xzdFdhZ2VyU2lkZXMMX2N1cnJlbnRPZGRzDl9jdXJyZW50UG9pbnRzDl9jdXJyZW50UGF5T3V0CV9jb21tZW50cwdfc291cmNlCV91c2VyQ29kZQlfY29tcHV0ZXIPX2lzUm9sbGluZ0lmQmV0Dl9yb2xsaW5nTnVtYmVyD193YWdlcklkUm9sbGluZwtfY2xpZW50Q29kZRFfY29tYmluYXRpb25Db3VudBBfY29tYmluYXRpb25MaXN0D19kZWJpdFRvUGVuZGluZxNfbGl2ZUJldHRpbmdXYWdlcklkIDxDaGFydFBlcmNlbnRhZ2U+a19fQmFja2luZ0ZpZWxkGjxBZ2VudENvZGU+a19fQmFja2luZ0ZpZWxkIjxBdXRob3JpemF0aW9uVHlwZT5rX19CYWNraW5nRmllbGQnPEV4b3Rp\" /><input type=\"hidden\" name=\"__VIEWSTATE3\" id=\"__VIEWSTATE3\" value=\"Y1NwZWNpYWxXYWdlckxpc3Q+a19fQmFja2luZ0ZpZWxkHzxDaGVja0F2YWlsYWJsZT5rX19CYWNraW5nRmllbGQAAAAAAAAAAAAAAAEAAAAAAQADBAQEAQABAQAAAAEAAwAAAAEEAwAICAICBQUFBQICCw0NBwcBmQFTeXN0ZW0uQ29sbGVjdGlvbnMuR2VuZXJpYy5MaXN0YDFbW1dUU29mdHdhcmUuTW9kZWwuV2FnZXJTaWRlcywgQUlULU1vZGVsLCBWZXJzaW9uPTIwMTc0LjQuMTQyMTMuMTQ0MiwgQ3VsdHVyZT1uZXV0cmFsLCBQdWJsaWNLZXlUb2tlbj1mNjFkN2Y5YjNmMDBiMTU3XV0VV1RTb2Z0d2FyZS5Nb2RlbC5PZGRzAgAAABdXVFNvZnR3YXJlLk1vZGVsLlBvaW50cwIAAAAXV1RTb2Z0d2FyZS5Nb2RlbC5QYXlPdXQCAAAAAgEHCAh/U3lzdGVtLkNvbGxl\" /><input type=\"hidden\" name=\"__VIEWSTATE4\" id=\"__VIEWSTATE4\" value=\"Y3Rpb25zLkdlbmVyaWMuTGlzdGAxW1tTeXN0ZW0uU3RyaW5nLCBtc2NvcmxpYiwgVmVyc2lvbj00LjAuMC4wLCBDdWx0dXJlPW5ldXRyYWwsIFB1YmxpY0tleVRva2VuPWI3N2E1YzU2MTkzNGUwODldXQUJAiZXVFNvZnR3YXJlLk1vZGVsLkF1dGhvcml6YXRpb25UeXBlRW51bQIAAACUAVN5c3RlbS5Db2xsZWN0aW9ucy5HZW5lcmljLkxpc3RgMVtbV1RTb2Z0d2FyZS5Nb2RlbC5XYWdlciwgQUlULU1vZGVsLCBWZXJzaW9uPTIwMTc0LjQuMTQyMTMuMTQ0MiwgQ3VsdHVyZT1uZXV0cmFsLCBQdWJsaWNLZXlUb2tlbj1mNjFkN2Y5YjNmMDBiMTU3XV0BAgAAAE71HgAAAAAAAQEGMjIwLjAwBTIwMC4wAzExMAMxMDABAAAAAAAGBQAAAACAimWtAF3WCICKZa0AXdYI\" /><input type=\"hidden\" name=\"__VIEWSTATE5\" id=\"__VIEWSTATE5\" value=\"AAAAAAoACQYAAAAJBwAAAAkIAAAACQkAAAAJBQAAAAAKCgAAAAAAAAAKAAAAAAoBMAAAAAAAAAAAAAoF9f///yZXVFNvZnR3YXJlLk1vZGVsLkF1dGhvcml6YXRpb25UeXBlRW51bQEAAAAHdmFsdWVfXwACAgAAAAAJDAAAAAAEBgAAAJkBU3lzdGVtLkNvbGxlY3Rpb25zLkdlbmVyaWMuTGlzdGAxW1tXVFNvZnR3YXJlLk1vZGVsLldhZ2VyU2lkZXMsIEFJVC1Nb2RlbCwgVmVyc2lvbj0yMDE3NC40LjE0MjEzLjE0NDIsIEN1bHR1cmU9bmV1dHJhbCwgUHVibGljS2V5VG9rZW49ZjYxZDdmOWIzZjAwYjE1N11dAwAAAAZfaXRlbXMFX3NpemUIX3ZlcnNpb24EAAAdV1RTb2Z0d2FyZS5Nb2RlbC5XYWdlclNpZGVzW10CAAAACAgJDQAAAAEAAAABAAAABQcAAAAVV1RT\" /><input type=\"hidden\" name=\"__VIEWSTATE6\" id=\"__VIEWSTATE6\" value=\"b2Z0d2FyZS5Nb2RlbC5PZGRzBwAAAAhfb2Rkc0xheQlfb2Rkc1Rha2UGX3ZhbGlkC19BZGp1c3RPZGRzEF9vcmlnaW5hbE9kZHNMYXkRX29yaWdpbmFsT2Rkc1Rha2UJX3JvdW5kaW5nAAAAAAAAAAUFAQEFBQICAAAAAzExMAMxMDABAAEwATAABQgAAAAXV1RTb2Z0d2FyZS5Nb2RlbC5Qb2ludHMEAAAAB19wb2ludHMKX3BvaW50VHlwZQZfdmFsaWQMX0FkanVzdFBvaW50AAEAAAsBAQIAAAAAAG5CBg4AAAACVE8BAAUJAAAAF1dUU29mdHdhcmUuTW9kZWwuUGF5T3V0BAAAAApfQW1vdW50TGF5C19BbW91bnRUYWtlB19hbW91bnQLX2lzV2luV2FnZXIAAAAABQUFAQIAAAAGMjIwLjAwBTIwMC4wBTIwMC4wAQEMAAAAAQAAAAkPAAAAAAAAAAAAAAAHDQAAAAABAAAA\" /><input type=\"hidden\" name=\"__VIEWSTATE7\" id=\"__VIEWSTATE7\" value=\"BAAAAAQbV1RTb2Z0d2FyZS5Nb2RlbC5XYWdlclNpZGVzAgAAAAkQAAAADQMHDwAAAAABAAAAAAAAAAQWV1RTb2Z0d2FyZS5Nb2RlbC5XYWdlcgIAAAAFEAAAABtXVFNvZnR3YXJlLk1vZGVsLldhZ2VyU2lkZXM6AAAADV90aWNrZXROdW1iZXIIX3dhZ2VySWQQX3dhZ2VyU2lkZU51bWJlcgdfZ2FtZUlkC19wZXJpb2RUeXBlC190ZWFtTnVtYmVyD19yb3RhdGlvbk51bWJlcglfc2lkZU5hbWUMX3dhZ2VyVGFyZ2V0B19wb2ludHMQX3BvaW50c0FkZGVkU2lkZQ1fdGVhc2VyUG9pbnRzCl9zaWRlU3RhdGURX2ZpcnN0VGVhbVBpdGNoZXIWX2ZpcnN0UGl0Y2hlck11c3RTdGFydBJfc2Vjb25kVGVhbVBpdGNoZXIXX3NlY29uZFBpdGNoZXJNdXN0U3RhcnQQX2Ftb3Vu\" /><input type=\"hidden\" name=\"__VIEWSTATE8\" id=\"__VIEWSTATE8\" value=\"dFRvQmV0U2lkZRBfYW1vdW50VG9XaW5TaWRlEF9nZXRsYXlXYWdlclNpZGUMX29kZHNMYXlTaWRlDV9vZGRzVGFrZVNpZGUVX29yaWdpbmFsX09kZHNMYXlTaWRlFl9vcmlnaW5hbF9vZGRzVGFrZVNpZGUMX2N1cnJlbnRPZGRzEV9leHRyYUluZm9ybWF0aW9uCF9hcHBsaWVkEV9kYXRlUmVzb2x2ZWRTaWRlCV9nYW1lVGltZQlfY29tbWVudHMIX2lkU3BvcnQLX2xpbmVDaGFuZ2UOX3BpdGNoZXJDaGFuZ2UHX3hfdG9feRFfZ3JhZGRpbmdDb21tZW50cw1fZGVzY3JpcHRpb24xDV9kZXNjcmlwdGlvbjINX2Rlc2NyaXB0aW9uMw1fZGVzY3JpcHRpb240El9pZENvbnRlc3RDYXRlZ29yeQxfY29udGVzdE5hbWUGX3VuaXRzBl9maXhlZA9fb3BlblNpZGVTdGF0dXMR\" /><input type=\"hidden\" name=\"__VIEWSTATE9\" id=\"__VIEWSTATE9\" value=\"X2Rlc2NyaXB0aW9uU3BvcnQfPGlzTWFudWFsbHlPZGRzPmtfX0JhY2tpbmdGaWVsZCQ8QXBwbHlSZWd1bGF0aW9uT25seT5rX19CYWNraW5nRmllbGQaPFNwb3J0VHlwZT5rX19CYWNraW5nRmllbGQcPE9mZmVyTGluZUlEPmtfX0JhY2tpbmdGaWVsZBs8T2ZmZXJMaW5lcz5rX19CYWNraW5nRmllbGQhPEN1cnJlbnRTaGFkZVR5cGU+a19fQmFja2luZ0ZpZWxkGTxSb3VuZGluZz5rX19CYWNraW5nRmllbGQgPFNoYWRlVGVhc2VyTGluZT5rX19CYWNraW5nRmllbGQbPEdhbWVIZWFkZXI+a19fQmFja2luZ0ZpZWxkHDxQYXlPdXRNYXhlZD5rX19CYWNraW5nRmllbGQoPEFtb3VudFRvQmV0TWF4ZWRQYWlkT3V0PmtfX0JhY2tpbmdGaWVsZCg8QW1vdW50VG9XaW5N\" /><input type=\"hidden\" name=\"__VIEWSTATE10\" id=\"__VIEWSTATE10\" value=\"YXhlZFBhaWRPdXQ+a19fQmFja2luZ0ZpZWxkIzxQYXlPdXRNYXhlZE1lc3NhZ2U+a19fQmFja2luZ0ZpZWxkAAAAAAAAAAEABAAAAAEAAQAAAAAAAAMDBAEAAAABAAEBAAEBAQEBAAEBAAQBAAAEAAEAAwQBAAAAAQgHAggCAggCF1dUU29mdHdhcmUuTW9kZWwuUG9pbnRzAgAAAAsLAgICBQUCBQVwU3lzdGVtLk51bGxhYmxlYDFbW1N5c3RlbS5EZWNpbWFsLCBtc2NvcmxpYiwgVmVyc2lvbj00LjAuMC4wLCBDdWx0dXJlPW5ldXRyYWwsIFB1YmxpY0tleVRva2VuPWI3N2E1YzU2MTkzNGUwODldXXBTeXN0ZW0uTnVsbGFibGVgMVtbU3lzdGVtLkRlY2ltYWwsIG1zY29ybGliLCBWZXJzaW9uPTQuMC4wLjAsIEN1bHR1cmU9bmV1dHJhbCwgUHVibGljS2V5VG9rZW49\" /><input type=\"hidden\" name=\"__VIEWSTATE11\" id=\"__VIEWSTATE11\" value=\"Yjc3YTVjNTYxOTM0ZTA4OV1dFVdUU29mdHdhcmUuTW9kZWwuT2RkcwIAAAACDQ0HAQcBI1dUU29mdHdhcmUuTW9kZWwuT3BlblNpZGVTdGF0dXNFbnVtAgAAAAEBHldUU29mdHdhcmUuTW9kZWwuU3BvcnRUeXBlRW51bQIAAAAIAgtTeXN0ZW0uQnl0ZRdXVFNvZnR3YXJlLk1vZGVsLlBvaW50cwIAAAABBQUCAAAATvUeAAAAAcOOBgACAeMCAAAGEQAAABFSdXRnZXJzIC0gRm9yZGhhbQMJEgAAAAAAAAAAAAAAAQkFAAAAAAkFAAAAAAYyMjAuMDAFMjAwLjABAzExMAMxMDAKCgkHAAAACQUAAAAAgIplrQBd1ggAvKZAEV3WCAkFAAAACgAGFgAAAAtMaW5lIENoYW5nZQkFAAAAAAkFAAAACQUAAAAJBQAAAAkFAAAACQUAAAAAAAkFAAAACQUAAAAABej///8jV1RTb2Z0\" /><input type=\"hidden\" name=\"__VIEWSTATE12\" id=\"__VIEWSTATE12\" value=\"d2FyZS5Nb2RlbC5PcGVuU2lkZVN0YXR1c0VudW0BAAAAB3ZhbHVlX18AAgIAAAAABhkAAAARQmFza2V0YmFsbCAtIE5DQUEAAAXm////HldUU29mdHdhcmUuTW9kZWwuU3BvcnRUeXBlRW51bQEAAAAHdmFsdWVfXwACAgAAAAQAAAAACQUAAAAACAIBCgkFAAAAAAEwATAKARIAAAAIAAAAAABuQgYcAAAAAlRPAQALHg9XYWdlcklkT3BlblNpZGVkHgNDV1MFJDMyMDY0MjYzLTg5ZDQtNDRhNC1hODdjLWMxNmFhNzllNGUxMB4QSWRMYXN0R2FtZUNoYW5nZSgrBAg4ODQ5NjQ2Mx4SQ2hlY2tMaW1pdExhc3REYXRlBtBkd60AXdYIHg9GdWxsUGF0aENvbnRlc3RlHgphbW91bnRUeXBlKCl4V1RTb2Z0d2FyZS5Nb2RlbC5BbW91bnRUeXBlRW51bSwgQUlULU1vZGVsLCBW\" /><input type=\"hidden\" name=\"__VIEWSTATE13\" id=\"__VIEWSTATE13\" value=\"ZXJzaW9uPTIwMTc0LjQuMTQyMTMuMTQ0MiwgQ3VsdHVyZT1uZXV0cmFsLCBQdWJsaWNLZXlUb2tlbj1mNjFkN2Y5YjNmMDBiMTU3BFRBS0UeEEFjdGlvbkFsZXJ0U2F2ZWRnHgl3YWdlclR5cGUoKXdXVFNvZnR3YXJlLk1vZGVsLldhZ2VyVHlwZUVudW0sIEFJVC1Nb2RlbCwgVmVyc2lvbj0yMDE3NC40LjE0MjEzLjE0NDIsIEN1bHR1cmU9bmV1dHJhbCwgUHVibGljS2V5VG9rZW49ZjYxZDdmOWIzZjAwYjE1NwhTVFJBSUdIVB4LUm9sbGluZ1R5cGUoKXdXVFNvZnR3YXJlLk1vZGVsLklmQmV0VHlwZUVudW0sIEFJVC1Nb2RlbCwgVmVyc2lvbj0yMDE3NC40LjE0MjEzLjE0NDIsIEN1bHR1cmU9bmV1dHJhbCwgUHVibGljS2V5VG9rZW49ZjYxZDdmOWIzZjAwYjE1\" /><input type=\"hidden\" name=\"__VIEWSTATE14\" id=\"__VIEWSTATE14\" value=\"NwtJRl9XSU5fT05MWRYCZg9kFgICAQ9kFg4CAQ8PFgIeCEltYWdlVXJsBRt+L0ltYWdlcy9sb2dvX3dpbmR5Y2l0eS5wbmdkZAIDDw8WAh4EVGV4dAUGSlRTMTE1ZGQCBA8WAh4JaW5uZXJodG1sBQhVc2VyIElEOmQCBQ9kFhQCAQ8WAh8MBS48Zm9udCBjbGFzcz1TdW1tYXJ5QWNjb3VudEhlYWRlcj5KVFMxMTU8L2ZvbnQ+ZAIDDxYCHw0FEUN1cnJlbnQgQmFsYW5jZTogZAIFDxYCHwwFLTxmb250IGNsYXNzPVN1bW1hcnlQb3NpdGl2ZU51bWJlcj4wLjAwPC9mb250PmQCBw8WAh4HVmlzaWJsZWgWAgIBDxYCHw0FCUZSRUUgUExBWWQCCQ8WAh8NBQtBdmFpbGFibGU6IGQCCw8WAh8MBUk8Zm9udCBpZD0nbGJsQXZhaWxhYmxlQW1vdW50JyBjbGFzcz1TdW1tYXJ5\" /><input type=\"hidden\" name=\"__VIEWSTATE15\" id=\"__VIEWSTATE15\" value=\"UG9zaXRpdmVOdW1iZXI+MSw5MDAuMDA8L2ZvbnQ+ZAINDxYCHw5oFgQCAQ8WAh8NBQlCYWxhbmNlOiBkAgMPFgIfDAUtPGZvbnQgY2xhc3M9U3VtbWFyeVBvc2l0aXZlTnVtYmVyPjAuMDA8L2ZvbnQ+ZAIPDxYCHw0FCVBlbmRpbmc6IGQCEQ8WAh8MBUc8Zm9udCBpZD0nbGJsUGVuZGluZ0Ftb3VudCcgY2xhc3M9U3VtbWFyeVBvc2l0aXZlTnVtYmVyPjEsMTAwLjAwPC9mb250PmQCEw8WAh8OaBYEAgEPFgIfDQUJUGVuZGluZzogZAIDDxYCHwwFLTxmb250IGNsYXNzPVN1bW1hcnlQb3NpdGl2ZU51bWJlcj4wLjAwPC9mb250PmQCBg8WAh4LXyFJdGVtQ291bnQCBxYOZg9kFgJmDxUIDGxpdmVCZXR0aW5nIBFidG5MaXZlSW5QbGF5TWVudRkvQ2xpZW50L0xpdmVC\" /><input type=\"hidden\" name=\"__VIEWSTATE16\" id=\"__VIEWSTATE16\" value=\"ZXR0aW5nMi5hc3B4AAAETElWRQAAZAIBD2QWAmYPFQgMZmlyc3QgYWN0aXZlDWJ0blNwb3J0c01lbnUXU3RyYWlnaHRCZXRNZW51RXgyLmFzcHgAAAZTcG9ydHMAAGQCAg9kFgJmDxUIASANYnRuSG9yc2VzTWVudRcvQ2xpZW50L0hvcnNlV2FnZXIuYXNweAAABkhvcnNlcwAAZAIDD2QWAmYPFQgBIA5idG5BY2NvdW50TWVudRgvQ2xpZW50L0RhaWx5RmlndXJlLmFzcHgAAAdBY2NvdW50AABkAgQPZBYCZg8VCAEgDmJ0blBlbmRpbmdNZW51Gy9DbGllbnQvUGVuZGluZ3NXYWdlcnMuYXNweAAAB1BlbmRpbmcAAGQCBQ9kFgJmDxUIASANYnRuU2NvcmVzTWVudSJodHRwOi8vd3d3LmRvbmJlc3QuY29tL2FsbC9zY29yZXMvAAZfYmxhbmsGU2NvcmVzAABkAgYPZBYC\" /><input type=\"hidden\" name=\"__VIEWSTATE17\" id=\"__VIEWSTATE17\" value=\"Zg8VCAEgDGJ0blJ1bGVzTWVudRIvQ2xpZW50L1J1bGVzLmFzcHgAAAVSdWxlcwAAZAIHD2QWAgIBD2QWAgIBDxYCHw8CBBYIZg9kFgJmDxUGB2N1cnJlbnQDcmVkF1N0cmFpZ2h0QmV0TWVudUV4Mi5hc3B4AAlTdHJhaWdodHMAZAIBD2QWAmYPFQYAABVQYXJsYXlCZXRNZW51RXgyLmFzcHgAB1BhcmxheXMAZAICD2QWAmYPFQYAABVUZWFzZXJCZXRNZW51RXgyLmFzcHgAB1RlYXNlcnMAZAIDD2QWAmYPFQYAABhJZkJldFJldmVyc2VNZW51RXgyLmFzcHgAEklmIEJldHMgLyBSZXZlcnNlcwlJbnZpc2libGVkAgsPZBYCAgEPZBYUAgEPFgIfDAUuPGZvbnQgY2xhc3M9U3VtbWFyeUFjY291bnRIZWFkZXI+SlRTMTE1PC9mb250PmQCAw8WAh8NBRFDdXJyZW50IEJh\" /><input type=\"hidden\" name=\"__VIEWSTATE18\" id=\"__VIEWSTATE18\" value=\"bGFuY2U6IGQCBQ8WAh8MBS08Zm9udCBjbGFzcz1TdW1tYXJ5UG9zaXRpdmVOdW1iZXI+MC4wMDwvZm9udD5kAgcPFgIfDmgWAgIBDxYCHw0FCUZSRUUgUExBWWQCCQ8WAh8NBQtBdmFpbGFibGU6IGQCCw8WAh8MBUk8Zm9udCBpZD0nbGJsQXZhaWxhYmxlQW1vdW50JyBjbGFzcz1TdW1tYXJ5UG9zaXRpdmVOdW1iZXI+MSw5MDAuMDA8L2ZvbnQ+ZAINDxYCHw5oFgICAQ8WAh8NBQlCYWxhbmNlOiBkAg8PFgIfDQUJUGVuZGluZzogZAIRDxYCHwwFRzxmb250IGlkPSdsYmxQZW5kaW5nQW1vdW50JyBjbGFzcz1TdW1tYXJ5UG9zaXRpdmVOdW1iZXI+MSwxMDAuMDA8L2ZvbnQ+ZAITDxYCHw5oFgICAQ8WAh8NBQlQZW5kaW5nOiBkZJjmKpalxLFuqqPCXDB6tJppOJ/h\" /></div><script type=\"text/javascript\">//<![CDATA[var theForm = document.forms['Form1'];if (!theForm) { theForm = document.Form1;}function __doPostBack(eventTarget, eventArgument) { if (!theForm.onsubmit || (theForm.onsubmit() != false)) { theForm.__EVENTTARGET.value = eventTarget; theForm.__EVENTARGUMENT.value = eventArgument; theForm.submit(); }}//]]></script><script src=\"/WebResource.axd?d=Jb6kFaBFm7finf4QmAO8yFE6cC1fENxmf0aRhO2X0KK1MA0km4ExxopQJ0Skdaui19k5MhD4mGFeqKazEFKVjio58XE1&amp;t=636576850476690871\" type=\"text/javascript\"></script><script src=\"/ScriptResource.axd?d=Q1A8Qlz73lgJrOxSZ835LbeaaNyF0-ZUTZ1vRmsk7EgiRfBOBB9mUgl41mTN3zyvUogtmTFB8QBh19trlmhMbfJXDmwz-pqi6kkcsanHE6NcEXrkKyldrrTy3u9KrJk_twBEILD5rYJazip9TOf7ns_Z6tmbIPLu0DSmGlvErlAulses0&amp;t=ffffffffcd368728\" type=\"text/javascript\"></script><script type=\"text/javascript\">//<![CDATA[if (typeof(Sys) === 'undefined') throw new Error('ASP.NET Ajax client-side framework failed to load.');//]]></script><script src=\"/ScriptResource.axd?d=Yg3CyeT5W5dN3B81YusGU4ziDIY_Gme-bdTeIm_fRJ4ccBlvAxl16-cVvfsosWQ8S_5B8IZhu1ThkrPatj8WQUdeypYXElOfcnbLDsLPqHQ_a0z1_QPm9dtCQsAqwEL9L21NSZQUEd0SI9WbNDje7-FJS6eRnpqYkbZkI1ldGrpK86JP0&amp;t=ffffffffcd368728\" type=\"text/javascript\"></script><script type=\"text/javascript\">//<![CDATA[Type.registerNamespace('WTSoftwareWeb');WTSoftwareWeb.SessionTimeoutWS=function() {WTSoftwareWeb.SessionTimeoutWS.initializeBase(this);this._timeout = 0;this._userContext = null;this._succeeded = null;this._failed = null;}WTSoftwareWeb.SessionTimeoutWS.prototype={_get_path:function() { var p = this.get_path(); if (p) return p; else return WTSoftwareWeb.SessionTimeoutWS._staticInstance.get_path();},GetSessionTimeout:function(succeededCallback, failedCallback, userContext) {/// <param name=\"succeededCallback\" type=\"Function\" optional=\"true\" mayBeNull=\"true\"></param>/// <param name=\"failedCallback\" type=\"Function\" optional=\"true\" mayBeNull=\"true\"></param>/// <param name=\"userContext\" optional=\"true\" mayBeNull=\"true\"></param>return this._invoke(this._get_path(), 'GetSessionTimeout',false,{},succeededCallback,failedCallback,userContext); },RefreshSession:function(succeededCallback, failedCallback, userContext) {/// <param name=\"succeededCallback\" type=\"Function\" optional=\"true\" mayBeNull=\"true\"></param>/// <param name=\"failedCallback\" type=\"Function\" optional=\"true\" mayBeNull=\"true\"></param>/// <param name=\"userContext\" optional=\"true\" mayBeNull=\"true\"></param>return this._invoke(this._get_path(), 'RefreshSession',false,{},succeededCallback,failedCallback,userContext); }}WTSoftwareWeb.SessionTimeoutWS.registerClass('WTSoftwareWeb.SessionTimeoutWS',Sys.Net.WebServiceProxy);WTSoftwareWeb.SessionTimeoutWS._staticInstance = new WTSoftwareWeb.SessionTimeoutWS();WTSoftwareWeb.SessionTimeoutWS.set_path = function(value) {WTSoftwareWeb.SessionTimeoutWS._staticInstance.set_path(value); }WTSoftwareWeb.SessionTimeoutWS.get_path = function() { /// <value type=\"String\" mayBeNull=\"true\">The service url.</value>return WTSoftwareWeb.SessionTimeoutWS._staticInstance.get_path();}WTSoftwareWeb.SessionTimeoutWS.set_timeout = function(value) {WTSoftwareWeb.SessionTimeoutWS._staticInstance.set_timeout(value); }WTSoftwareWeb.SessionTimeoutWS.get_timeout = function() { /// <value type=\"Number\">The service timeout.</value>return WTSoftwareWeb.SessionTimeoutWS._staticInstance.get_timeout(); }WTSoftwareWeb.SessionTimeoutWS.set_defaultUserContext = function(value) { WTSoftwareWeb.SessionTimeoutWS._staticInstance.set_defaultUserContext(value); }WTSoftwareWeb.SessionTimeoutWS.get_defaultUserContext = function() { /// <value mayBeNull=\"true\">The service default user context.</value>return WTSoftwareWeb.SessionTimeoutWS._staticInstance.get_defaultUserContext(); }WTSoftwareWeb.SessionTimeoutWS.set_defaultSucceededCallback = function(value) { WTSoftwareWeb.SessionTimeoutWS._staticInstance.set_defaultSucceededCallback(value); }WTSoftwareWeb.SessionTimeoutWS.get_defaultSucceededCallback = function() { /// <value type=\"Function\" mayBeNull=\"true\">The service default succeeded callback.</value>return WTSoftwareWeb.SessionTimeoutWS._staticInstance.get_defaultSucceededCallback(); }WTSoftwareWeb.SessionTimeoutWS.set_defaultFailedCallback = function(value) { WTSoftwareWeb.SessionTimeoutWS._staticInstance.set_defaultFailedCallback(value); }WTSoftwareWeb.SessionTimeoutWS.get_defaultFailedCallback = function() { /// <value type=\"Function\" mayBeNull=\"true\">The service default failed callback.</value>return WTSoftwareWeb.SessionTimeoutWS._staticInstance.get_defaultFailedCallback(); }WTSoftwareWeb.SessionTimeoutWS.set_enableJsonp = function(value) { WTSoftwareWeb.SessionTimeoutWS._staticInstance.set_enableJsonp(value); }WTSoftwareWeb.SessionTimeoutWS.get_enableJsonp = function() { /// <value type=\"Boolean\">Specifies whether the service supports JSONP for cross domain calling.</value>return WTSoftwareWeb.SessionTimeoutWS._staticInstance.get_enableJsonp(); }WTSoftwareWeb.SessionTimeoutWS.set_jsonpCallbackParameter = function(value) { WTSoftwareWeb.SessionTimeoutWS._staticInstance.set_jsonpCallbackParameter(value); }WTSoftwareWeb.SessionTimeoutWS.get_jsonpCallbackParameter = function() { /// <value type=\"String\">Specifies the parameter name that contains the callback function name for a JSONP request.</value>return WTSoftwareWeb.SessionTimeoutWS._staticInstance.get_jsonpCallbackParameter(); }WTSoftwareWeb.SessionTimeoutWS.set_path(\"/Services/SessionTimeoutWS.asmx\");WTSoftwareWeb.SessionTimeoutWS.GetSessionTimeout= function(onSuccess,onFailed,userContext) {/// <param name=\"succeededCallback\" type=\"Function\" optional=\"true\" mayBeNull=\"true\"></param>/// <param name=\"failedCallback\" type=\"Function\" optional=\"true\" mayBeNull=\"true\"></param>/// <param name=\"userContext\" optional=\"true\" mayBeNull=\"true\"></param>WTSoftwareWeb.SessionTimeoutWS._staticInstance.GetSessionTimeout(onSuccess,onFailed,userContext); }WTSoftwareWeb.SessionTimeoutWS.RefreshSession= function(onSuccess,onFailed,userContext) {/// <param name=\"succeededCallback\" type=\"Function\" optional=\"true\" mayBeNull=\"true\"></param>/// <param name=\"failedCallback\" type=\"Function\" optional=\"true\" mayBeNull=\"true\"></param>/// <param name=\"userContext\" optional=\"true\" mayBeNull=\"true\"></param>WTSoftwareWeb.SessionTimeoutWS._staticInstance.RefreshSession(onSuccess,onFailed,userContext); }var gtc = Sys.Net.WebServiceProxy._generateTypedConstructor;Type.registerNamespace('WTSoftwareWeb.Helpers.Entities');if (typeof(WTSoftwareWeb.Helpers.Entities.AjaxResultEntity) === 'undefined') {WTSoftwareWeb.Helpers.Entities.AjaxResultEntity=gtc(\"WTSoftwareWeb.Helpers.Entities.AjaxResultEntity\");WTSoftwareWeb.Helpers.Entities.AjaxResultEntity.registerClass('WTSoftwareWeb.Helpers.Entities.AjaxResultEntity');}//]]></script><div>	<input type=\"hidden\" name=\"__VIEWSTATEGENERATOR\" id=\"__VIEWSTATEGENERATOR\" value=\"2104964A\" />	<input type=\"hidden\" name=\"__PREVIOUSPAGE\" id=\"__PREVIOUSPAGE\" value=\"Fr4XB3jJHQGza7Tm3NmQr74P1EGJ_7rgl4T2Az3lItQtefwATEaUEd9JYv0NpbrRpOMPjjjhQjpo8ehXDHNwDPw7MecQiX1dQRkcbIjPDvsAqPvM0\" />	<input type=\"hidden\" name=\"__EVENTVALIDATION\" id=\"__EVENTVALIDATION\" value=\"/wEdAAYpJMPmkyORCbiib1kQUIffexAIb1cjB3p/gzcZcUqOEJGirj5lJ9D+8BqNYT42Xjo17tPy0hv0FLjGtQYW9Gy7kZ+/86t4P92I7m787BjFj7wVTqrKQqcoenUqrj2cN3afwMfHuigl0YXa2I4sCubsTdR1og==\" /></div> <script type=\"text/javascript\">//<![CDATA[Sys.WebForms.PageRequestManager._initialize('ctl00$ScriptManager1', 'Form1', ['tctl00$cphWorkArea$UpdatePanel1','UpdatePanel1'], [], [], 90, 'ctl00');//]]></script>	<div class=\"main\"> <div class=\"header\"> <div class=\"logo_container\">	<a class=\"logo\" href=\"#\"> <img id=\"mainLogoImage\" src=\"../Images/logo_windycity.png\" style=\"border-width:0px;\" /> </a> </div>	<div>	<div class=\"logoff\"> <input type=\"button\" name=\"ctl00$btnLogOut\" value=\"Log Off\" onclick=\"javascript:WebForm_DoPostBackWithOptions(new WebForm_PostBackOptions(&quot;ctl00$btnLogOut&quot;, &quot;&quot;, false, &quot;&quot;, &quot;../LogOff.aspx&quot;, false, true))\" id=\"btnLogOut\" /> <p> <font> <span id=\"userName\" class=\"userName\">JTS115</span> </font> </p> <span id=\"LabelUserID\">User ID:</span> </div> </div>	<div class=\"SummaryContainer\"><!--PLEASE, DON'T CHANGE ID PROPERTY OF HTML ELEMENTS CAN YOU COMMENT OR DELETE SOMEBODY TAG HTML, BUT NOT CHANGE THE ID PROPERTY --><table class=\"SummaryAccountHeaderClient\" cellpadding=\"0\" cellspacing =\"0\"> <tr> <td class=\"tdSummaryAccountHeaderClient\"> <span id=\"LabelCustomerBalance\">Current Balance: </span><a href=\"/Client/DailyFigure.aspx\"><font class=SummaryPositiveNumber>0.00</font></a> </td> </tr>	<tr>	<td class=\"tdSummaryAccountHeaderClient\"> <span id=\"LabelAvailable\">Available: </span><a href=\"/client/Account.aspx\"><font id='lblAvailableAmount' class=SummaryPositiveNumber>1,900.00</font></a> </td>	</tr>	<tr>	<td class=\"tdSummaryAccountHeaderClient\"> <span id=\"LabelPending\">Pending: </span><a href=\"../Client/PendingsWagers.aspx\"><font id='lblPendingAmount' class=SummaryPositiveNumber>1,100.00</font></a> </td>	</tr>	</table></div>	<div style=\"position:absolute;top:136px;right:6px;width:167px;z-index:999;display:none\">	<div id=\"google_translate_element\"></div><script type=\"text/javascript\">	function googleTranslateElementInit() {	new google.translate.TranslateElement({ pageLanguage: 'en', includedLanguages: 'en,es,hi,it,ko,ru,vi,zh-CN,zh-TW', layout: google.translate.TranslateElement.InlineLayout.SIMPLE, autoDisplay: false }, 'google_translate_element');	}</script><script type=\"text/javascript\" src=\"//translate.google.com/translate_a/element.js?cb=googleTranslateElementInit\"></script> </div>	<div class=\"menu\"> <div> <div class=\"main_menu\"> <ul> <li class='liveBetting ' id='btnLiveInPlayMenu'><a href='/Client/LiveBetting2.aspx' onclick='' target=''> LIVE	</a> <i></i></li> <li class='first active' id='btnSportsMenu'><a href='StraightBetMenuEx2.aspx' onclick='' target=''> Sports	</a> <i></i></li> <li class=' ' id='btnHorsesMenu'><a href='/Client/HorseWager.aspx' onclick='' target=''> Horses	</a> <i></i></li> <li class=' ' id='btnAccountMenu'><a href='/Client/DailyFigure.aspx' onclick='' target=''> Account	</a> <i></i></li> <li class=' ' id='btnPendingMenu'><a href='/Client/PendingsWagers.aspx' onclick='' target=''> Pending	</a> <i></i></li> <li class=' ' id='btnScoresMenu'><a href='http://www.donbest.com/all/scores/' onclick='' target='_blank'> Scores	</a> <i></i></li> <li class=' ' id='btnRulesMenu'><a href='/Client/Rules.aspx' onclick='' target=''> Rules	</a> <i></i></li> </ul> </div> </div> <div id=\"subMenuContainer\"> <div class=\"sportsSubmenu\"> <div class=\"bg1\"> <div class=\"bg2\"> <ul> <li id='current'><a class='red' href='StraightBetMenuEx2.aspx' onclick=''> Straights</a> <i class=''></i></li> <li id=''><a class='' href='ParlayBetMenuEx2.aspx' onclick=''> Parlays</a> <i class=''></i></li> <li id=''><a class='' href='TeaserBetMenuEx2.aspx' onclick=''> Teasers</a> <i class=''></i></li> <li id=''><a class='' href='IfBetReverseMenuEx2.aspx' onclick=''> If Bets / Reverses</a> <i class='Invisible'></i></li> </ul> </div> </div> </div></div> <div id=\"headerWorkPlace\"> <div class=\"headerContinueSearch\"> </div></div> </div> </div> <div class=\"content_holder\"> <script type=\"text/javascript\" language=\"javascript\"> Sys.WebForms.PageRequestManager.getInstance().add_beginRequest(BeginRequestHandler); Sys.WebForms.PageRequestManager.getInstance().add_endRequest(EndRequestHandler); function BeginRequestHandler(sender, args) { $.blockUI({ message: $('#domMessage') }); } function EndRequestHandler(sender, args) { $.unblockUI(); } function TryToExport() { //$('#btnExportWagers').click(); var ids = $('#wagerType').val(); window.open('/Helpers/PrintTicket.aspx?data=' + ids); } </script> <div id=\"domMessage\" style=\"display: none;\"> <table align=\"center\"> <tr> <td> <img src=\"../Images/ajax-loading.gif\" alt=\"\" /> </td> <td> <span style=\"font: bold 18px Arial; color: #800000\">Processing your request...</span> </td> </tr> </table> </div> <div id=\"UpdatePanel1\">	<span id=\"lblInfoPrincipal\"><br><br><table class=\"tblConfirmWager\" border=\"0\">	<tr>	<td class=\"HeaderTicket\" align=\"left\" colspan=\"2\">StraightsBet&nbsp;&nbsp;&nbsp;&nbsp;12/8/2018 12:31:21 PM - ET&nbsp;&nbsp;&nbsp;&nbsp;</td><td class=\"thRisk\" align=\"left\">Risk</td><td class=\"thWin\" align=\"left\" colspan=\"2\">Win</td>	</tr><tr class=\"BackgroundLineBlue\">	<td class=\"selection_cell\" align=\"left\">Selection 1: </td><td class=\"TdWagerDetailTicket\" align=\"left\"><div class='TdDetailTicket'> <font class='SportTicket'>Basketball - NCAA</font> <br /> <font class='TeamRotationTicket'>[739]</font> <font class='TeamNameTicket'>Rutgers - Fordham</font> <font class='TeamDateTicket'>12/08/2018 <font class='GameTime'>(02:30 PM)</font></font> <br /> <font class='TargetTicket'></font> <font class='TargetTicket'>Over</font> <font class='PointsTicket'> 59Â½</font> <font class='ScoreTypeTicket'> Points</font> <font class='OddsTicket'> -110</font> <font class='PeriodTypeTicket'> for the 1st Half <font class='LineChange'>(Line Change)</font> <font class='LineChange'></font></font> <div class='RegulationOnlyText'></div> <font class='BuyPointTicket'></font> <font class='PitcherTicket'></font> <font class='AdjustOddsTicket'></font> </div></td><td class=\"risk_cell\">220.00</td><td class=\"win_cell\">200.00</td><td class=\"TdRemove\" align=\"left\"><a id=\"cmdRemove/0\" class=\"buttonRemove btn_blue\" runat=\"server\" href=\"javascript:__doPostBack(&#39;ctl00$cphWorkArea$cmdRemove/0&#39;,&#39;&#39;)\"><span>Remove</span></a></td>	</tr><tr>	<td class=\"tdLineChangeMessage\" align=\"left\" colspan=\"6\">At least one line (or price or pitcher) has changed. Please review each wager carefully then you may press the remove button, re-enter password to confirm wagers or press cancel to return to the wager menu</td>	</tr><tr>	<td class=\"TdMessageIncorrectPassword\" align=\"left\" colspan=\"6\"><span></span></td>	</tr><tr>	<td align=\"center\" colspan=\"4\"><font class='labelPassword'>Password::</font><input name=\"ctl00$cphWorkArea$txtConfirmPassword\" type=\"password\" id=\"txtConfirmPassword\" class=\"txtPassword\" />&nbsp;&nbsp;<input type=\"submit\" name=\"ctl00$cphWorkArea$cmdSubmit\" value=\"Submit\" id=\"cmdSubmit\" title=\"Send Wager\" class=\"Submitbutton\" runat=\"server\" /></td>	</tr>	</table><br><br></span> </div><input type=\"submit\" name=\"ctl00$cphWorkArea$btnExportWagers\" value=\"\" id=\"btnExportWagers\" style=\"display:none;\" /> </div> </div> <div class=\"footer\"> <div class=\"footerWorkPlace\"> </div> </div> <div style=\"display: none;\"> </div> </form> <script type=\"text/javascript\"> var NotificationStartTime = 120000; //milliseconds before session time out var PopupRefreshDelay = 10000; //milliseconds between popup refresh var timeout; var firstTime = true; var counter = 60; var interval; object = { func: function() { WTSoftwareWeb.SessionTimeoutWS.GetSessionTimeout(OnSuccessTimeoutWS, OnFailureTimeoutWS); } } $(document).ready(function() { WTSoftwareWeb.SessionTimeoutWS.GetSessionTimeout(OnSuccessTimeoutWS, OnFailureTimeoutWS); }); function RedirectToLoginPage() { //$('input[type=button][value=\"Log Out\"]').click();//Logout btnLogOut $('#btnLogOut').click();//Logout btnLogOut } function RefreshSession() { WTSoftwareWeb.SessionTimeoutWS.RefreshSession(OnSuccessTimeoutWS, OnFailureTimeoutWS); firstTime = true; clearInterval(interval); counter = 60; try { keepSessionAlive(); } catch (err) { } } function OnSuccessTimeoutWS(response) { if (response && response.IsAutentificated) { if (response.Result) { var TimeBeforeTimeOut = parseInt(response.Result) if (TimeBeforeTimeOut > NotificationStartTime) { TimeOutPopup.Hide(); timeout = setTimeout(function() { object.func.call(object) } , TimeBeforeTimeOut - NotificationStartTime); } else { TimeOutPopup.Show(); timeout = setTimeout(function() { object.func.call(object) } , PopupRefreshDelay); if (firstTime) { interval = setInterval(function() { counter--; if (counter >= 0) { $(\"#countDown\").text(counter.toString()); } }, 1000); } firstTime = false; } } } else { RedirectToLoginPage(); } } function OnFailureTimeoutWS(response) { RedirectToLoginPage(); } var TimeOutPopup = { Show: function () { $('#SessionTimeoutPopup').show(); }, Hide: function () { $('#SessionTimeoutPopup').hide(); }};</script><div id=\"SessionTimeoutPopup\" class=\"SessionTimeoutScreen\"> <div class=\"SessionTimeoutPopup\" > <div class=\"PopupHeader\"> <div class=\"ReasonCaption\"> Your Session is about to expire </div> </div> <div class=\"PopupBody\"> <div> <p> You've been inactive for a while. For your security, we'll automatically sign out in approximately <label id=\"countDown\">60</label> seconds. </p> </div> <div class=\"ButtonHolder\"> <input type=\"button\" id=\"btnSignedOut\" onclick=\"RedirectToLoginPage();\" value=\"Sign out\" class=\"TimeoutButton\" /> <input type=\"button\" id=\"btnTimeout\" onclick=\"RefreshSession();\" value=\"Stay signed in\" class=\"TimeoutButton\" /> </div> </div> </div></div>	<script language=\"javascript\" type=\"text/javascript\"> $(document).ready(function () { $(\"#txtConfirmPassword\").focus();	}); </script> <script type=\"text/javascript\" src=\"/Scripts/Ait.js?v=5\"></script> <script type=\"text/javascript\" src=\"/Scripts/wise.js?v=5\"></script> <script type=\"text/javascript\" src=\"/Scripts/Wagers.js?v=5\"></script> <script type=\"text/javascript\" src=\"/Scripts/element.js\"></script> <script type=\"text/javascript\" src=\"/Scripts/jquery/jquery.blockUI.js\"></script> <script type=\"text/javascript\" src=\"/Scripts/CustomerAjax.js?v=5\"></script> <script type=\"text/javascript\" src=\"/Scripts/jquery.validationEngine-en.js\"></script> <script type=\"text/javascript\" src=\"/Scripts/jquery.validationEngine.js\"></script> <script type=\"text/javascript\" src=\"/Scripts/Ajax.js?v=5\"></script> <script type=\"text/javascript\" language=\"javascript\"> var orden = 1; var onSubmit = false; function OnSubmitRequest() { if (!onSubmit) { onSubmit = true; return true; } return false; } function deleteorder(id) { if (document.forms[0]['txtOrden'].value.indexOf(id) != -1) { document.forms[0]['txtOrden'].value = document.forms[0]['txtOrden'].value.replace((id + '@'), ' '); } } function sideorder(obj1) { var valor = ''; valor = obj1.name; deleteorder(valor); if (obj1.value != 0 || obj1.value != '') { document.forms[0]['txtOrden'].value = document.forms[0]['txtOrden'].value + valor + '@'; } return true; } function OnChecked(count, chkName, disabled) { var i; for (i = 2; i <= count; i++) { if (disabled == 'False') { document.getElementById(chkName + i).checked = false; document.forms[0]['txtWagerTypeParlay'].checked = false; } else { document.getElementById(chkName + i).checked = false; document.forms[0]['txtWagerType'].checked = false; } } } function test() { if (document.forms[0]['txtWagerTypeParlay'].checked == true) { return false; } return true; } function setfocus() { document.getElementById('txtWagerAmount').focus() } function mp_onload() { if (window.body_onload != null) window.body_onload(); } function imageButtonOnKeyPress() { var keyCodeEntered = (window.event.which) ? window.event.which : window.event.keyCode; if (keyCodeEntered == 13) { event.returnValue = false; return false; } return true; } function ValidateLiveCasino(URL, clientCode) { var items = JSON.stringify({ clientCode: clientCode }); var isOk = false; $.ajax({ type: \"POST\", url: \"ValidateLiveCasinoLCD.aspx/ValidateClientBalance\", data: items, async: false, contentType: \"application/json;charset=utf-8\", dataType: \"json\", success: function (result) { res = result.d if (res) { // window.open(URL, \"Live Casino\", \"fullscreen=yes\"); isOk = true; } else { alert(\"No balance is available to transfer. Please try again later.\"); } }, error: function (xhr, errorType, exception) { return false; OnFail(xhr, errorType, exception); } }); return isOk; } function OnFail(xhr, errorType, exception) { alert(\"Message: \" + xhr.statusText); } $(document).keypress(function (e) { if (e.which == 13) { $(\"#cmdContinue\").click(); } }); </script></body></html>";
//			String xhtml = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head> <meta name=\"google-translate-customization\" content=\"3233c073ab0f4e8f-812f5fbec3c21e36-gd944dbbbb163dd6b-17\" /> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /> <meta http-equiv=\"Content-Script-Type\" content=\"text/javascript\" /> <meta http-equiv=\"Content-Style-Type\" content=\"text/css\" /> <title>WINDYCITY</title> <link rel=\"shortcut icon\" href=\"/images/ikon_WINDYCITY.ico\" /> <meta name=\"keywords\" lang=\"en\" content=\"Title\" /> <meta name=\"description\" lang=\"en\" content=\"Title\" /> <meta name=\"language\" content=\"en\" /> <meta http-equiv=\"imagetoolbar\" content=\"no\" /> <style type=\"text/css\"> @import \"/Themes/Theme001/Styles/AllAjax.css?v=5\"; @import \"/Themes/Theme001/Styles/ClientStyle.css?v=5\"; @import \"/Themes/Theme001/Styles/AjaxClientStyle.css?v=5\"; @import \"/Themes/Theme001/Styles/Style.css?v=5\"; @import \"/Themes/Theme001/Styles/StyleTabs.css?v=5\"; @import \"/Themes/Theme001/Styles/SessionTimeoutPopup.css?v=5\"; @import \"/App_Themes/WtSoftware/ComboBox.WTSoftware.css?v=5\"; @import \"/App_Themes/WtSoftware/TreeView.WTSoftware.css?v=5\"; @import \"/App_Themes/WtSoftware/Menu.WTSoftware.css?v=5\"; </style> <link href=\"Styles/jquery.cluetip.css\" rel=\"stylesheet\" type=\"text/css\" /> <script type=\"text/javascript\" src=\"/Scripts/jquery/jquery-1.9.1.min.js\"></script> <script>if(this != top){ top.document.location.href = this.document.location.href;}</script></head><body onload=\"mp_onload();\"> <form name=\"Form1\" method=\"post\" action=\"./WagerTicket.aspx\" id=\"Form1\" style=\"min-height: 100%; height: 100% !important\"><div><input type=\"hidden\" name=\"__EVENTTARGET\" id=\"__EVENTTARGET\" value=\"\" /><input type=\"hidden\" name=\"__EVENTARGUMENT\" id=\"__EVENTARGUMENT\" value=\"\" /><input type=\"hidden\" name=\"__VIEWSTATEFIELDCOUNT\" id=\"__VIEWSTATEFIELDCOUNT\" value=\"20\" /><input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"/wEPDwULLTExMDY5MzczOTYPFhYeEUlkTGFzdFNoYWRlQ2hhbmdlKClZU3lzdGVtLkludDY0LCBtc2NvcmxpYiwgVmVyc2lvbj00LjAuMC4wLCBDdWx0dXJlPW5ldXRyYWwsIFB1YmxpY0tleVRva2VuPWI3N2E1YzU2MTkzNGUwODkGMjQ1NzMxHglXYWdlclNhdmUyuyMAAQAAAP////8BAAAAAAAAAAwCAAAAV0FJVC1Nb2RlbCwgVmVyc2lvbj0yMDE3NC40LjE0MjEzLjE0NDIsIEN1bHR1cmU9bmV1dHJhbCwgUHVibGljS2V5VG9rZW49ZjYxZDdmOWIzZjAwYjE1NwQBAAAAlAFTeXN0ZW0uQ29sbGVjdGlvbnMuR2VuZXJpYy5MaXN0YDFbW1dUU29mdHdhcmUuTW9kZWwuV2FnZXIsIEFJVC1Nb2RlbCwgVmVyc2lvbj0yMDE3NC40LjE0MjEzLjE0NDIsIEN1bHR1cmU9\" /><input type=\"hidden\" name=\"__VIEWSTATE1\" id=\"__VIEWSTATE1\" value=\"bmV1dHJhbCwgUHVibGljS2V5VG9rZW49ZjYxZDdmOWIzZjAwYjE1N11dAwAAAAZfaXRlbXMFX3NpemUIX3ZlcnNpb24EAAAYV1RTb2Z0d2FyZS5Nb2RlbC5XYWdlcltdAgAAAAgICQMAAAABAAAAAQAAAAcDAAAAAAEAAAAEAAAABBZXVFNvZnR3YXJlLk1vZGVsLldhZ2VyAgAAAAkEAAAADQMFBAAAABZXVFNvZnR3YXJlLk1vZGVsLldhZ2VyJwAAAA1fdGlja2V0TnVtYmVyCF93YWdlcklkCl93YWdlclR5cGUMX2dldGxheVdhZ2VyDF9hbW91bnRUb0JldAxfYW1vdW50VG9XaW4IX29kZHNMYXkJX29kZHNUYWtlC193YWdlclN0YXRlD19hY3Rpb25PbklmQmV0cwxfcG9pbnRzQWRkZWQRX2Zyb21Db21wbGV4V2FnZXIKX2RhdGVTYXZlZA1fZGF0ZVJlc29sdmVkCV9p\" /><input type=\"hidden\" name=\"__VIEWSTATE2\" id=\"__VIEWSTATE2\" value=\"ZFRlYXNlcglfaWRQYXJsYXkRX25hbWVQYXJsYXlUZWFzZXILX2lzRnJlZVBsYXkOX2xzdFdhZ2VyU2lkZXMMX2N1cnJlbnRPZGRzDl9jdXJyZW50UG9pbnRzDl9jdXJyZW50UGF5T3V0CV9jb21tZW50cwdfc291cmNlCV91c2VyQ29kZQlfY29tcHV0ZXIPX2lzUm9sbGluZ0lmQmV0Dl9yb2xsaW5nTnVtYmVyD193YWdlcklkUm9sbGluZwtfY2xpZW50Q29kZRFfY29tYmluYXRpb25Db3VudBBfY29tYmluYXRpb25MaXN0D19kZWJpdFRvUGVuZGluZxNfbGl2ZUJldHRpbmdXYWdlcklkIDxDaGFydFBlcmNlbnRhZ2U+a19fQmFja2luZ0ZpZWxkGjxBZ2VudENvZGU+a19fQmFja2luZ0ZpZWxkIjxBdXRob3JpemF0aW9uVHlwZT5rX19CYWNraW5nRmllbGQnPEV4b3Rp\" /><input type=\"hidden\" name=\"__VIEWSTATE3\" id=\"__VIEWSTATE3\" value=\"Y1NwZWNpYWxXYWdlckxpc3Q+a19fQmFja2luZ0ZpZWxkHzxDaGVja0F2YWlsYWJsZT5rX19CYWNraW5nRmllbGQAAAAAAAAAAAAAAAEAAAAAAQADBAQEAQABAQAAAAEAAwAAAAEEAwAICAICBQUFBQICCw0NBwcBmQFTeXN0ZW0uQ29sbGVjdGlvbnMuR2VuZXJpYy5MaXN0YDFbW1dUU29mdHdhcmUuTW9kZWwuV2FnZXJTaWRlcywgQUlULU1vZGVsLCBWZXJzaW9uPTIwMTc0LjQuMTQyMTMuMTQ0MiwgQ3VsdHVyZT1uZXV0cmFsLCBQdWJsaWNLZXlUb2tlbj1mNjFkN2Y5YjNmMDBiMTU3XV0VV1RTb2Z0d2FyZS5Nb2RlbC5PZGRzAgAAABdXVFNvZnR3YXJlLk1vZGVsLlBvaW50cwIAAAAXV1RTb2Z0d2FyZS5Nb2RlbC5QYXlPdXQCAAAAAgEHCAh/U3lzdGVtLkNvbGxl\" /><input type=\"hidden\" name=\"__VIEWSTATE4\" id=\"__VIEWSTATE4\" value=\"Y3Rpb25zLkdlbmVyaWMuTGlzdGAxW1tTeXN0ZW0uU3RyaW5nLCBtc2NvcmxpYiwgVmVyc2lvbj00LjAuMC4wLCBDdWx0dXJlPW5ldXRyYWwsIFB1YmxpY0tleVRva2VuPWI3N2E1YzU2MTkzNGUwODldXQUJAiZXVFNvZnR3YXJlLk1vZGVsLkF1dGhvcml6YXRpb25UeXBlRW51bQIAAACUAVN5c3RlbS5Db2xsZWN0aW9ucy5HZW5lcmljLkxpc3RgMVtbV1RTb2Z0d2FyZS5Nb2RlbC5XYWdlciwgQUlULU1vZGVsLCBWZXJzaW9uPTIwMTc0LjQuMTQyMTMuMTQ0MiwgQ3VsdHVyZT1uZXV0cmFsLCBQdWJsaWNLZXlUb2tlbj1mNjFkN2Y5YjNmMDBiMTU3XV0BAgAAAOn1HgAAAAAAAQEGMjIwLjAwBTIwMC4wAzExMAMxMDABAAAAAAAGBQAAAACAdQMnDV3WCIB1AycNXdYI\" /><input type=\"hidden\" name=\"__VIEWSTATE5\" id=\"__VIEWSTATE5\" value=\"AAAAAAoACQYAAAAJBwAAAAkIAAAACQkAAAAJBQAAAAAKCgAAAAAAAAAKAAAAAAoBMAAAAAAAAAAAAAoF9f///yZXVFNvZnR3YXJlLk1vZGVsLkF1dGhvcml6YXRpb25UeXBlRW51bQEAAAAHdmFsdWVfXwACAgAAAAAJDAAAAAAEBgAAAJkBU3lzdGVtLkNvbGxlY3Rpb25zLkdlbmVyaWMuTGlzdGAxW1tXVFNvZnR3YXJlLk1vZGVsLldhZ2VyU2lkZXMsIEFJVC1Nb2RlbCwgVmVyc2lvbj0yMDE3NC40LjE0MjEzLjE0NDIsIEN1bHR1cmU9bmV1dHJhbCwgUHVibGljS2V5VG9rZW49ZjYxZDdmOWIzZjAwYjE1N11dAwAAAAZfaXRlbXMFX3NpemUIX3ZlcnNpb24EAAAdV1RTb2Z0d2FyZS5Nb2RlbC5XYWdlclNpZGVzW10CAAAACAgJDQAAAAEAAAABAAAABQcAAAAVV1RT\" /><input type=\"hidden\" name=\"__VIEWSTATE6\" id=\"__VIEWSTATE6\" value=\"b2Z0d2FyZS5Nb2RlbC5PZGRzBwAAAAhfb2Rkc0xheQlfb2Rkc1Rha2UGX3ZhbGlkC19BZGp1c3RPZGRzEF9vcmlnaW5hbE9kZHNMYXkRX29yaWdpbmFsT2Rkc1Rha2UJX3JvdW5kaW5nAAAAAAAAAAUFAQEFBQICAAAAAzExMAMxMDABAAEwATAABQgAAAAXV1RTb2Z0d2FyZS5Nb2RlbC5Qb2ludHMEAAAAB19wb2ludHMKX3BvaW50VHlwZQZfdmFsaWQMX0FkanVzdFBvaW50AAEAAAsBAQIAAAAAgBZDBg4AAAACVE8BAAUJAAAAF1dUU29mdHdhcmUuTW9kZWwuUGF5T3V0BAAAAApfQW1vdW50TGF5C19BbW91bnRUYWtlB19hbW91bnQLX2lzV2luV2FnZXIAAAAABQUFAQIAAAAGMjQwLjAwBTIwMC4wBTIwMC4wAQEMAAAAAQAAAAkPAAAAAAAAAAAAAAAHDQAAAAABAAAA\" /><input type=\"hidden\" name=\"__VIEWSTATE7\" id=\"__VIEWSTATE7\" value=\"BAAAAAQbV1RTb2Z0d2FyZS5Nb2RlbC5XYWdlclNpZGVzAgAAAAkQAAAADQMHDwAAAAABAAAAAAAAAAQWV1RTb2Z0d2FyZS5Nb2RlbC5XYWdlcgIAAAAFEAAAABtXVFNvZnR3YXJlLk1vZGVsLldhZ2VyU2lkZXM6AAAADV90aWNrZXROdW1iZXIIX3dhZ2VySWQQX3dhZ2VyU2lkZU51bWJlcgdfZ2FtZUlkC19wZXJpb2RUeXBlC190ZWFtTnVtYmVyD19yb3RhdGlvbk51bWJlcglfc2lkZU5hbWUMX3dhZ2VyVGFyZ2V0B19wb2ludHMQX3BvaW50c0FkZGVkU2lkZQ1fdGVhc2VyUG9pbnRzCl9zaWRlU3RhdGURX2ZpcnN0VGVhbVBpdGNoZXIWX2ZpcnN0UGl0Y2hlck11c3RTdGFydBJfc2Vjb25kVGVhbVBpdGNoZXIXX3NlY29uZFBpdGNoZXJNdXN0U3RhcnQQX2Ftb3Vu\" /><input type=\"hidden\" name=\"__VIEWSTATE8\" id=\"__VIEWSTATE8\" value=\"dFRvQmV0U2lkZRBfYW1vdW50VG9XaW5TaWRlEF9nZXRsYXlXYWdlclNpZGUMX29kZHNMYXlTaWRlDV9vZGRzVGFrZVNpZGUVX29yaWdpbmFsX09kZHNMYXlTaWRlFl9vcmlnaW5hbF9vZGRzVGFrZVNpZGUMX2N1cnJlbnRPZGRzEV9leHRyYUluZm9ybWF0aW9uCF9hcHBsaWVkEV9kYXRlUmVzb2x2ZWRTaWRlCV9nYW1lVGltZQlfY29tbWVudHMIX2lkU3BvcnQLX2xpbmVDaGFuZ2UOX3BpdGNoZXJDaGFuZ2UHX3hfdG9feRFfZ3JhZGRpbmdDb21tZW50cw1fZGVzY3JpcHRpb24xDV9kZXNjcmlwdGlvbjINX2Rlc2NyaXB0aW9uMw1fZGVzY3JpcHRpb240El9pZENvbnRlc3RDYXRlZ29yeQxfY29udGVzdE5hbWUGX3VuaXRzBl9maXhlZA9fb3BlblNpZGVTdGF0dXMR\" /><input type=\"hidden\" name=\"__VIEWSTATE9\" id=\"__VIEWSTATE9\" value=\"X2Rlc2NyaXB0aW9uU3BvcnQfPGlzTWFudWFsbHlPZGRzPmtfX0JhY2tpbmdGaWVsZCQ8QXBwbHlSZWd1bGF0aW9uT25seT5rX19CYWNraW5nRmllbGQaPFNwb3J0VHlwZT5rX19CYWNraW5nRmllbGQcPE9mZmVyTGluZUlEPmtfX0JhY2tpbmdGaWVsZBs8T2ZmZXJMaW5lcz5rX19CYWNraW5nRmllbGQhPEN1cnJlbnRTaGFkZVR5cGU+a19fQmFja2luZ0ZpZWxkGTxSb3VuZGluZz5rX19CYWNraW5nRmllbGQgPFNoYWRlVGVhc2VyTGluZT5rX19CYWNraW5nRmllbGQbPEdhbWVIZWFkZXI+a19fQmFja2luZ0ZpZWxkHDxQYXlPdXRNYXhlZD5rX19CYWNraW5nRmllbGQoPEFtb3VudFRvQmV0TWF4ZWRQYWlkT3V0PmtfX0JhY2tpbmdGaWVsZCg8QW1vdW50VG9XaW5N\" /><input type=\"hidden\" name=\"__VIEWSTATE10\" id=\"__VIEWSTATE10\" value=\"YXhlZFBhaWRPdXQ+a19fQmFja2luZ0ZpZWxkIzxQYXlPdXRNYXhlZE1lc3NhZ2U+a19fQmFja2luZ0ZpZWxkAAAAAAAAAAEABAAAAAEAAQAAAAAAAAMDBAEAAAABAAEBAAEBAQEBAAEBAAQBAAAEAAEAAwQBAAAAAQgHAggCAggCF1dUU29mdHdhcmUuTW9kZWwuUG9pbnRzAgAAAAsLAgICBQUCBQVwU3lzdGVtLk51bGxhYmxlYDFbW1N5c3RlbS5EZWNpbWFsLCBtc2NvcmxpYiwgVmVyc2lvbj00LjAuMC4wLCBDdWx0dXJlPW5ldXRyYWwsIFB1YmxpY0tleVRva2VuPWI3N2E1YzU2MTkzNGUwODldXXBTeXN0ZW0uTnVsbGFibGVgMVtbU3lzdGVtLkRlY2ltYWwsIG1zY29ybGliLCBWZXJzaW9uPTQuMC4wLjAsIEN1bHR1cmU9bmV1dHJhbCwgUHVibGljS2V5VG9rZW49\" /><input type=\"hidden\" name=\"__VIEWSTATE11\" id=\"__VIEWSTATE11\" value=\"Yjc3YTVjNTYxOTM0ZTA4OV1dFVdUU29mdHdhcmUuTW9kZWwuT2RkcwIAAAACDQ0HAQcBI1dUU29mdHdhcmUuTW9kZWwuT3BlblNpZGVTdGF0dXNFbnVtAgAAAAEBHldUU29mdHdhcmUuTW9kZWwuU3BvcnRUeXBlRW51bQIAAAAIAgtTeXN0ZW0uQnl0ZRdXVFNvZnR3YXJlLk1vZGVsLlBvaW50cwIAAAABBQUCAAAA6fUeAAAAAdyOBgABAhYDAAAGEQAAAB9GcmVzbm8gU3RhdGUgLSBMb25nIEJlYWNoIFN0YXRlBAkSAAAAAAAAAAAAAAABCQUAAAAACQUAAAAABjIyMC4wMAUyMDAuMAEDMTEwAzEwMAoKCQcAAAAJBQAAAACAdQMnDV3WCACQmvg2XdYICQUAAAAKAAYWAAAAC0xpbmUgQ2hhbmdlCQUAAAAACQUAAAAJBQAAAAkFAAAACQUAAAAJBQAAAAAACQUAAAAJBQAA\" /><input type=\"hidden\" name=\"__VIEWSTATE12\" id=\"__VIEWSTATE12\" value=\"AAAF6P///yNXVFNvZnR3YXJlLk1vZGVsLk9wZW5TaWRlU3RhdHVzRW51bQEAAAAHdmFsdWVfXwACAgAAAAAGGQAAABFCYXNrZXRiYWxsIC0gTkNBQQAABeb///8eV1RTb2Z0d2FyZS5Nb2RlbC5TcG9ydFR5cGVFbnVtAQAAAAd2YWx1ZV9fAAICAAAABAEAAAAJBQAAAAAIAgAKCQUAAAAAATABMAoBEgAAAAgAAAAAABZDBhwAAAACVE8BAAseD1dhZ2VySWRPcGVuU2lkZWQeA0NXUwUkZTEzMWU4MGQtY2FkYi00OGYwLThmYjAtOTY3NDNmMmIxZGNjHhBJZExhc3RHYW1lQ2hhbmdlKCsECDg4NTA2NzA0HhJDaGVja0xpbWl0TGFzdERhdGUGsOoDJw1d1ggeD0Z1bGxQYXRoQ29udGVzdGUeCmFtb3VudFR5cGUoKXhXVFNvZnR3YXJlLk1vZGVsLkFtb3VudFR5cGVFbnVt\" /><input type=\"hidden\" name=\"__VIEWSTATE13\" id=\"__VIEWSTATE13\" value=\"LCBBSVQtTW9kZWwsIFZlcnNpb249MjAxNzQuNC4xNDIxMy4xNDQyLCBDdWx0dXJlPW5ldXRyYWwsIFB1YmxpY0tleVRva2VuPWY2MWQ3ZjliM2YwMGIxNTcEVEFLRR4QQWN0aW9uQWxlcnRTYXZlZGceCXdhZ2VyVHlwZSgpd1dUU29mdHdhcmUuTW9kZWwuV2FnZXJUeXBlRW51bSwgQUlULU1vZGVsLCBWZXJzaW9uPTIwMTc0LjQuMTQyMTMuMTQ0MiwgQ3VsdHVyZT1uZXV0cmFsLCBQdWJsaWNLZXlUb2tlbj1mNjFkN2Y5YjNmMDBiMTU3CFNUUkFJR0hUHgtSb2xsaW5nVHlwZSgpd1dUU29mdHdhcmUuTW9kZWwuSWZCZXRUeXBlRW51bSwgQUlULU1vZGVsLCBWZXJzaW9uPTIwMTc0LjQuMTQyMTMuMTQ0MiwgQ3VsdHVyZT1uZXV0cmFsLCBQdWJsaWNLZXlUb2tlbj1m\" /><input type=\"hidden\" name=\"__VIEWSTATE14\" id=\"__VIEWSTATE14\" value=\"NjFkN2Y5YjNmMDBiMTU3C0lGX1dJTl9PTkxZFgJmD2QWAgIBD2QWDgIBDw8WAh4ISW1hZ2VVcmwFG34vSW1hZ2VzL2xvZ29fd2luZHljaXR5LnBuZ2RkAgMPDxYCHgRUZXh0BQZKVFMxMTVkZAIEDxYCHglpbm5lcmh0bWwFCFVzZXIgSUQ6ZAIFD2QWFAIBDxYCHwwFLjxmb250IGNsYXNzPVN1bW1hcnlBY2NvdW50SGVhZGVyPkpUUzExNTwvZm9udD5kAgMPFgIfDQURQ3VycmVudCBCYWxhbmNlOiBkAgUPFgIfDAUtPGZvbnQgY2xhc3M9U3VtbWFyeVBvc2l0aXZlTnVtYmVyPjAuMDA8L2ZvbnQ+ZAIHDxYCHgdWaXNpYmxlaBYCAgEPFgIfDQUJRlJFRSBQTEFZZAIJDxYCHw0FC0F2YWlsYWJsZTogZAILDxYCHwwFSTxmb250IGlkPSdsYmxBdmFpbGFibGVBbW91bnQn\" /><input type=\"hidden\" name=\"__VIEWSTATE15\" id=\"__VIEWSTATE15\" value=\"IGNsYXNzPVN1bW1hcnlQb3NpdGl2ZU51bWJlcj4xLDkwMC4wMDwvZm9udD5kAg0PFgIfDmgWBAIBDxYCHw0FCUJhbGFuY2U6IGQCAw8WAh8MBS08Zm9udCBjbGFzcz1TdW1tYXJ5UG9zaXRpdmVOdW1iZXI+MC4wMDwvZm9udD5kAg8PFgIfDQUJUGVuZGluZzogZAIRDxYCHwwFRzxmb250IGlkPSdsYmxQZW5kaW5nQW1vdW50JyBjbGFzcz1TdW1tYXJ5UG9zaXRpdmVOdW1iZXI+MSwxMDAuMDA8L2ZvbnQ+ZAITDxYCHw5oFgQCAQ8WAh8NBQlQZW5kaW5nOiBkAgMPFgIfDAUtPGZvbnQgY2xhc3M9U3VtbWFyeVBvc2l0aXZlTnVtYmVyPjAuMDA8L2ZvbnQ+ZAIGDxYCHgtfIUl0ZW1Db3VudAIHFg5mD2QWAmYPFQgMbGl2ZUJldHRpbmcgEWJ0bkxpdmVJblBsYXlNZW51\" /><input type=\"hidden\" name=\"__VIEWSTATE16\" id=\"__VIEWSTATE16\" value=\"GS9DbGllbnQvTGl2ZUJldHRpbmcyLmFzcHgAAARMSVZFAABkAgEPZBYCZg8VCAxmaXJzdCBhY3RpdmUNYnRuU3BvcnRzTWVudRdTdHJhaWdodEJldE1lbnVFeDIuYXNweAAABlNwb3J0cwAAZAICD2QWAmYPFQgBIA1idG5Ib3JzZXNNZW51Fy9DbGllbnQvSG9yc2VXYWdlci5hc3B4AAAGSG9yc2VzAABkAgMPZBYCZg8VCAEgDmJ0bkFjY291bnRNZW51GC9DbGllbnQvRGFpbHlGaWd1cmUuYXNweAAAB0FjY291bnQAAGQCBA9kFgJmDxUIASAOYnRuUGVuZGluZ01lbnUbL0NsaWVudC9QZW5kaW5nc1dhZ2Vycy5hc3B4AAAHUGVuZGluZwAAZAIFD2QWAmYPFQgBIA1idG5TY29yZXNNZW51Imh0dHA6Ly93d3cuZG9uYmVzdC5jb20vYWxsL3Njb3Jlcy8ABl9ibGFuawZT\" /><input type=\"hidden\" name=\"__VIEWSTATE17\" id=\"__VIEWSTATE17\" value=\"Y29yZXMAAGQCBg9kFgJmDxUIASAMYnRuUnVsZXNNZW51Ei9DbGllbnQvUnVsZXMuYXNweAAABVJ1bGVzAABkAgcPZBYCAgEPZBYCAgEPFgIfDwIEFghmD2QWAmYPFQYHY3VycmVudANyZWQXU3RyYWlnaHRCZXRNZW51RXgyLmFzcHgACVN0cmFpZ2h0cwBkAgEPZBYCZg8VBgAAFVBhcmxheUJldE1lbnVFeDIuYXNweAAHUGFybGF5cwBkAgIPZBYCZg8VBgAAFVRlYXNlckJldE1lbnVFeDIuYXNweAAHVGVhc2VycwBkAgMPZBYCZg8VBgAAGElmQmV0UmV2ZXJzZU1lbnVFeDIuYXNweAASSWYgQmV0cyAvIFJldmVyc2VzCUludmlzaWJsZWQCCw9kFgICAQ9kFhQCAQ8WAh8MBS48Zm9udCBjbGFzcz1TdW1tYXJ5QWNjb3VudEhlYWRlcj5KVFMxMTU8L2ZvbnQ+ZAIDDxYC\" /><input type=\"hidden\" name=\"__VIEWSTATE18\" id=\"__VIEWSTATE18\" value=\"Hw0FEUN1cnJlbnQgQmFsYW5jZTogZAIFDxYCHwwFLTxmb250IGNsYXNzPVN1bW1hcnlQb3NpdGl2ZU51bWJlcj4wLjAwPC9mb250PmQCBw8WAh8OaBYCAgEPFgIfDQUJRlJFRSBQTEFZZAIJDxYCHw0FC0F2YWlsYWJsZTogZAILDxYCHwwFSTxmb250IGlkPSdsYmxBdmFpbGFibGVBbW91bnQnIGNsYXNzPVN1bW1hcnlQb3NpdGl2ZU51bWJlcj4xLDkwMC4wMDwvZm9udD5kAg0PFgIfDmgWAgIBDxYCHw0FCUJhbGFuY2U6IGQCDw8WAh8NBQlQZW5kaW5nOiBkAhEPFgIfDAVHPGZvbnQgaWQ9J2xibFBlbmRpbmdBbW91bnQnIGNsYXNzPVN1bW1hcnlQb3NpdGl2ZU51bWJlcj4xLDEwMC4wMDwvZm9udD5kAhMPFgIfDmgWAgIBDxYCHw0FCVBlbmRpbmc6IGRkJL4YgQ7U\" /><input type=\"hidden\" name=\"__VIEWSTATE19\" id=\"__VIEWSTATE19\" value=\"qAZhkxMcTR0seW8INZ4=\" /></div><script type=\"text/javascript\">//<![CDATA[var theForm = document.forms['Form1'];if (!theForm) { theForm = document.Form1;}function __doPostBack(eventTarget, eventArgument) { if (!theForm.onsubmit || (theForm.onsubmit() != false)) { theForm.__EVENTTARGET.value = eventTarget; theForm.__EVENTARGUMENT.value = eventArgument; theForm.submit(); }}//]]></script><script src=\"/WebResource.axd?d=Jb6kFaBFm7finf4QmAO8yFE6cC1fENxmf0aRhO2X0KK1MA0km4ExxopQJ0Skdaui19k5MhD4mGFeqKazEFKVjio58XE1&amp;t=636475903203255666\" type=\"text/javascript\"></script><script src=\"/ScriptResource.axd?d=Q1A8Qlz73lgJrOxSZ835LbeaaNyF0-ZUTZ1vRmsk7EgiRfBOBB9mUgl41mTN3zyvUogtmTFB8QBh19trlmhMbfJXDmwz-pqi6kkcsanHE6NcEXrkKyldrrTy3u9KrJk_twBEILD5rYJazip9TOf7ns_Z6tmbIPLu0DSmGlvErlAulses0&amp;t=ffffffffad4b7194\" type=\"text/javascript\"></script><script type=\"text/javascript\">//<![CDATA[if (typeof(Sys) === 'undefined') throw new Error('ASP.NET Ajax client-side framework failed to load.');//]]></script><script src=\"/ScriptResource.axd?d=Yg3CyeT5W5dN3B81YusGU4ziDIY_Gme-bdTeIm_fRJ4ccBlvAxl16-cVvfsosWQ8S_5B8IZhu1ThkrPatj8WQUdeypYXElOfcnbLDsLPqHQ_a0z1_QPm9dtCQsAqwEL9L21NSZQUEd0SI9WbNDje7-FJS6eRnpqYkbZkI1ldGrpK86JP0&amp;t=ffffffffad4b7194\" type=\"text/javascript\"></script><script type=\"text/javascript\">//<![CDATA[Type.registerNamespace('WTSoftwareWeb');WTSoftwareWeb.SessionTimeoutWS=function() {WTSoftwareWeb.SessionTimeoutWS.initializeBase(this);this._timeout = 0;this._userContext = null;this._succeeded = null;this._failed = null;}WTSoftwareWeb.SessionTimeoutWS.prototype={_get_path:function() { var p = this.get_path(); if (p) return p; else return WTSoftwareWeb.SessionTimeoutWS._staticInstance.get_path();},GetSessionTimeout:function(succeededCallback, failedCallback, userContext) {/// <param name=\"succeededCallback\" type=\"Function\" optional=\"true\" mayBeNull=\"true\"></param>/// <param name=\"failedCallback\" type=\"Function\" optional=\"true\" mayBeNull=\"true\"></param>/// <param name=\"userContext\" optional=\"true\" mayBeNull=\"true\"></param>return this._invoke(this._get_path(), 'GetSessionTimeout',false,{},succeededCallback,failedCallback,userContext); },RefreshSession:function(succeededCallback, failedCallback, userContext) {/// <param name=\"succeededCallback\" type=\"Function\" optional=\"true\" mayBeNull=\"true\"></param>/// <param name=\"failedCallback\" type=\"Function\" optional=\"true\" mayBeNull=\"true\"></param>/// <param name=\"userContext\" optional=\"true\" mayBeNull=\"true\"></param>return this._invoke(this._get_path(), 'RefreshSession',false,{},succeededCallback,failedCallback,userContext); }}WTSoftwareWeb.SessionTimeoutWS.registerClass('WTSoftwareWeb.SessionTimeoutWS',Sys.Net.WebServiceProxy);WTSoftwareWeb.SessionTimeoutWS._staticInstance = new WTSoftwareWeb.SessionTimeoutWS();WTSoftwareWeb.SessionTimeoutWS.set_path = function(value) {WTSoftwareWeb.SessionTimeoutWS._staticInstance.set_path(value); }WTSoftwareWeb.SessionTimeoutWS.get_path = function() { /// <value type=\"String\" mayBeNull=\"true\">The service url.</value>return WTSoftwareWeb.SessionTimeoutWS._staticInstance.get_path();}WTSoftwareWeb.SessionTimeoutWS.set_timeout = function(value) {WTSoftwareWeb.SessionTimeoutWS._staticInstance.set_timeout(value); }WTSoftwareWeb.SessionTimeoutWS.get_timeout = function() { /// <value type=\"Number\">The service timeout.</value>return WTSoftwareWeb.SessionTimeoutWS._staticInstance.get_timeout(); }WTSoftwareWeb.SessionTimeoutWS.set_defaultUserContext = function(value) { WTSoftwareWeb.SessionTimeoutWS._staticInstance.set_defaultUserContext(value); }WTSoftwareWeb.SessionTimeoutWS.get_defaultUserContext = function() { /// <value mayBeNull=\"true\">The service default user context.</value>return WTSoftwareWeb.SessionTimeoutWS._staticInstance.get_defaultUserContext(); }WTSoftwareWeb.SessionTimeoutWS.set_defaultSucceededCallback = function(value) { WTSoftwareWeb.SessionTimeoutWS._staticInstance.set_defaultSucceededCallback(value); }WTSoftwareWeb.SessionTimeoutWS.get_defaultSucceededCallback = function() { /// <value type=\"Function\" mayBeNull=\"true\">The service default succeeded callback.</value>return WTSoftwareWeb.SessionTimeoutWS._staticInstance.get_defaultSucceededCallback(); }WTSoftwareWeb.SessionTimeoutWS.set_defaultFailedCallback = function(value) { WTSoftwareWeb.SessionTimeoutWS._staticInstance.set_defaultFailedCallback(value); }WTSoftwareWeb.SessionTimeoutWS.get_defaultFailedCallback = function() { /// <value type=\"Function\" mayBeNull=\"true\">The service default failed callback.</value>return WTSoftwareWeb.SessionTimeoutWS._staticInstance.get_defaultFailedCallback(); }WTSoftwareWeb.SessionTimeoutWS.set_enableJsonp = function(value) { WTSoftwareWeb.SessionTimeoutWS._staticInstance.set_enableJsonp(value); }WTSoftwareWeb.SessionTimeoutWS.get_enableJsonp = function() { /// <value type=\"Boolean\">Specifies whether the service supports JSONP for cross domain calling.</value>return WTSoftwareWeb.SessionTimeoutWS._staticInstance.get_enableJsonp(); }WTSoftwareWeb.SessionTimeoutWS.set_jsonpCallbackParameter = function(value) { WTSoftwareWeb.SessionTimeoutWS._staticInstance.set_jsonpCallbackParameter(value); }WTSoftwareWeb.SessionTimeoutWS.get_jsonpCallbackParameter = function() { /// <value type=\"String\">Specifies the parameter name that contains the callback function name for a JSONP request.</value>return WTSoftwareWeb.SessionTimeoutWS._staticInstance.get_jsonpCallbackParameter(); }WTSoftwareWeb.SessionTimeoutWS.set_path(\"/Services/SessionTimeoutWS.asmx\");WTSoftwareWeb.SessionTimeoutWS.GetSessionTimeout= function(onSuccess,onFailed,userContext) {/// <param name=\"succeededCallback\" type=\"Function\" optional=\"true\" mayBeNull=\"true\"></param>/// <param name=\"failedCallback\" type=\"Function\" optional=\"true\" mayBeNull=\"true\"></param>/// <param name=\"userContext\" optional=\"true\" mayBeNull=\"true\"></param>WTSoftwareWeb.SessionTimeoutWS._staticInstance.GetSessionTimeout(onSuccess,onFailed,userContext); }WTSoftwareWeb.SessionTimeoutWS.RefreshSession= function(onSuccess,onFailed,userContext) {/// <param name=\"succeededCallback\" type=\"Function\" optional=\"true\" mayBeNull=\"true\"></param>/// <param name=\"failedCallback\" type=\"Function\" optional=\"true\" mayBeNull=\"true\"></param>/// <param name=\"userContext\" optional=\"true\" mayBeNull=\"true\"></param>WTSoftwareWeb.SessionTimeoutWS._staticInstance.RefreshSession(onSuccess,onFailed,userContext); }var gtc = Sys.Net.WebServiceProxy._generateTypedConstructor;Type.registerNamespace('WTSoftwareWeb.Helpers.Entities');if (typeof(WTSoftwareWeb.Helpers.Entities.AjaxResultEntity) === 'undefined') {WTSoftwareWeb.Helpers.Entities.AjaxResultEntity=gtc(\"WTSoftwareWeb.Helpers.Entities.AjaxResultEntity\");WTSoftwareWeb.Helpers.Entities.AjaxResultEntity.registerClass('WTSoftwareWeb.Helpers.Entities.AjaxResultEntity');}//]]></script><div>	<input type=\"hidden\" name=\"__VIEWSTATEGENERATOR\" id=\"__VIEWSTATEGENERATOR\" value=\"2104964A\" />	<input type=\"hidden\" name=\"__PREVIOUSPAGE\" id=\"__PREVIOUSPAGE\" value=\"Fr4XB3jJHQGza7Tm3NmQr74P1EGJ_7rgl4T2Az3lItQtefwATEaUEd9JYv0NpbrRpOMPjjjhQjpo8ehXDHNwDPw7MecQiX1dQRkcbIjPDvsAqPvM0\" />	<input type=\"hidden\" name=\"__EVENTVALIDATION\" id=\"__EVENTVALIDATION\" value=\"/wEdAAbaPicUNMsolFFo+Q8877UvexAIb1cjB3p/gzcZcUqOEJGirj5lJ9D+8BqNYT42Xjo17tPy0hv0FLjGtQYW9Gy7kZ+/86t4P92I7m787BjFj7wVTqrKQqcoenUqrj2cN3au4Bo8f+TMPu4oO7saw9Zs3EJELw==\" /></div> <script type=\"text/javascript\">//<![CDATA[Sys.WebForms.PageRequestManager._initialize('ctl00$ScriptManager1', 'Form1', ['tctl00$cphWorkArea$UpdatePanel1','UpdatePanel1'], [], [], 90, 'ctl00');//]]></script>	<div class=\"main\"> <div class=\"header\"> <div class=\"logo_container\">	<a class=\"logo\" href=\"#\"> <img id=\"mainLogoImage\" src=\"../Images/logo_windycity.png\" style=\"border-width:0px;\" /> </a> </div>	<div>	<div class=\"logoff\"> <input type=\"button\" name=\"ctl00$btnLogOut\" value=\"Log Off\" onclick=\"javascript:WebForm_DoPostBackWithOptions(new WebForm_PostBackOptions(&quot;ctl00$btnLogOut&quot;, &quot;&quot;, false, &quot;&quot;, &quot;../LogOff.aspx&quot;, false, true))\" id=\"btnLogOut\" /> <p> <font> <span id=\"userName\" class=\"userName\">JTS115</span> </font> </p> <span id=\"LabelUserID\">User ID:</span> </div> </div>	<div class=\"SummaryContainer\"><!--PLEASE, DON'T CHANGE ID PROPERTY OF HTML ELEMENTS CAN YOU COMMENT OR DELETE SOMEBODY TAG HTML, BUT NOT CHANGE THE ID PROPERTY --><table class=\"SummaryAccountHeaderClient\" cellpadding=\"0\" cellspacing =\"0\"> <tr> <td class=\"tdSummaryAccountHeaderClient\"> <span id=\"LabelCustomerBalance\">Current Balance: </span><a href=\"/Client/DailyFigure.aspx\"><font class=SummaryPositiveNumber>0.00</font></a> </td> </tr>	<tr>	<td class=\"tdSummaryAccountHeaderClient\"> <span id=\"LabelAvailable\">Available: </span><a href=\"/client/Account.aspx\"><font id='lblAvailableAmount' class=SummaryPositiveNumber>1,900.00</font></a> </td>	</tr>	<tr>	<td class=\"tdSummaryAccountHeaderClient\"> <span id=\"LabelPending\">Pending: </span><a href=\"../Client/PendingsWagers.aspx\"><font id='lblPendingAmount' class=SummaryPositiveNumber>1,100.00</font></a> </td>	</tr>	</table></div>	<div style=\"position:absolute;top:136px;right:6px;width:167px;z-index:999;display:none\">	<div id=\"google_translate_element\"></div><script type=\"text/javascript\">	function googleTranslateElementInit() {	new google.translate.TranslateElement({ pageLanguage: 'en', includedLanguages: 'en,es,hi,it,ko,ru,vi,zh-CN,zh-TW', layout: google.translate.TranslateElement.InlineLayout.SIMPLE, autoDisplay: false }, 'google_translate_element');	}</script><script type=\"text/javascript\" src=\"//translate.google.com/translate_a/element.js?cb=googleTranslateElementInit\"></script> </div>	<div class=\"menu\"> <div> <div class=\"main_menu\"> <ul> <li class='liveBetting ' id='btnLiveInPlayMenu'><a href='/Client/LiveBetting2.aspx' onclick='' target=''> LIVE	</a> <i></i></li> <li class='first active' id='btnSportsMenu'><a href='StraightBetMenuEx2.aspx' onclick='' target=''> Sports	</a> <i></i></li> <li class=' ' id='btnHorsesMenu'><a href='/Client/HorseWager.aspx' onclick='' target=''> Horses	</a> <i></i></li> <li class=' ' id='btnAccountMenu'><a href='/Client/DailyFigure.aspx' onclick='' target=''> Account	</a> <i></i></li> <li class=' ' id='btnPendingMenu'><a href='/Client/PendingsWagers.aspx' onclick='' target=''> Pending	</a> <i></i></li> <li class=' ' id='btnScoresMenu'><a href='http://www.donbest.com/all/scores/' onclick='' target='_blank'> Scores	</a> <i></i></li> <li class=' ' id='btnRulesMenu'><a href='/Client/Rules.aspx' onclick='' target=''> Rules	</a> <i></i></li> </ul> </div> </div> <div id=\"subMenuContainer\"> <div class=\"sportsSubmenu\"> <div class=\"bg1\"> <div class=\"bg2\"> <ul> <li id='current'><a class='red' href='StraightBetMenuEx2.aspx' onclick=''> Straights</a> <i class=''></i></li> <li id=''><a class='' href='ParlayBetMenuEx2.aspx' onclick=''> Parlays</a> <i class=''></i></li> <li id=''><a class='' href='TeaserBetMenuEx2.aspx' onclick=''> Teasers</a> <i class=''></i></li> <li id=''><a class='' href='IfBetReverseMenuEx2.aspx' onclick=''> If Bets / Reverses</a> <i class='Invisible'></i></li> </ul> </div> </div> </div></div> <div id=\"headerWorkPlace\"> <div class=\"headerContinueSearch\"> </div></div> </div> </div> <div class=\"content_holder\"> <script type=\"text/javascript\" language=\"javascript\"> Sys.WebForms.PageRequestManager.getInstance().add_beginRequest(BeginRequestHandler); Sys.WebForms.PageRequestManager.getInstance().add_endRequest(EndRequestHandler); function BeginRequestHandler(sender, args) { $.blockUI({ message: $('#domMessage') }); } function EndRequestHandler(sender, args) { $.unblockUI(); } function TryToExport() { //$('#btnExportWagers').click(); var ids = $('#wagerType').val(); window.open('/Helpers/PrintTicket.aspx?data=' + ids); } </script> <div id=\"domMessage\" style=\"display: none;\"> <table align=\"center\"> <tr> <td> <img src=\"../Images/ajax-loading.gif\" alt=\"\" /> </td> <td> <span style=\"font: bold 18px Arial; color: #800000\">Processing your request...</span> </td> </tr> </table> </div> <div id=\"UpdatePanel1\">	<span id=\"lblInfoPrincipal\"><br><br><table class=\"tblConfirmWager\" border=\"0\">	<tr>	<td class=\"HeaderTicket\" align=\"left\" colspan=\"2\">StraightsBet&nbsp;&nbsp;&nbsp;&nbsp;12/8/2018 2:00:39 PM - ET&nbsp;&nbsp;&nbsp;&nbsp;</td><td class=\"thRisk\" align=\"left\">Risk</td><td class=\"thWin\" align=\"left\" colspan=\"2\">Win</td>	</tr><tr class=\"BackgroundLineAlternateGray\">	<td class=\"selection_cell\" align=\"left\">Selection 1: </td><td class=\"TdWagerDetailTicket\" align=\"left\"><div class='TdDetailTicket'> <font class='SportTicket'>Basketball - NCAA</font> <br /> <font class='TeamRotationTicket'>[790]</font> <font class='TeamNameTicket'>Fresno State - Long Beach State</font> <font class='TeamDateTicket'>12/08/2018 <font class='GameTime'>(07:00 PM)</font></font> <br /> <font class='TargetTicket'></font> <font class='TargetTicket'>Under</font> <font class='PointsTicket'> 150</font> <font class='ScoreTypeTicket'> Points</font> <font class='OddsTicket'> -110</font> <font class='PeriodTypeTicket'> for the Game <font class='LineChange'>(Line Change)</font> <font class='LineChange'></font></font> <div class='RegulationOnlyText'></div> <font class='BuyPointTicket'></font> <font class='PitcherTicket'></font> <font class='AdjustOddsTicket'></font> </div></td><td class=\"risk_cell\">220.00</td><td class=\"win_cell\">200.00</td><td class=\"TdRemove\" align=\"left\"><a id=\"cmdRemove/0\" class=\"buttonRemove btn_blue\" runat=\"server\" href=\"javascript:__doPostBack(&#39;ctl00$cphWorkArea$cmdRemove/0&#39;,&#39;&#39;)\"><span>Remove</span></a></td>	</tr><tr>	<td class=\"tdLineChangeMessage\" align=\"left\" colspan=\"6\">At least one line (or price or pitcher) has changed. Please review each wager carefully then you may press the remove button, re-enter password to confirm wagers or press cancel to return to the wager menu</td>	</tr><tr>	<td class=\"TdMessageIncorrectPassword\" align=\"left\" colspan=\"6\"><span></span></td>	</tr><tr>	<td align=\"center\" colspan=\"4\"><font class='labelPassword'>Password::</font><input name=\"ctl00$cphWorkArea$txtConfirmPassword\" type=\"password\" id=\"txtConfirmPassword\" class=\"txtPassword\" />&nbsp;&nbsp;<input type=\"submit\" name=\"ctl00$cphWorkArea$cmdSubmit\" value=\"Submit\" id=\"cmdSubmit\" title=\"Send Wager\" class=\"Submitbutton\" runat=\"server\" /></td>	</tr>	</table><br><br></span> </div><input type=\"submit\" name=\"ctl00$cphWorkArea$btnExportWagers\" value=\"\" id=\"btnExportWagers\" style=\"display:none;\" /> </div> </div> <div class=\"footer\"> <div class=\"footerWorkPlace\"> </div> </div> <div style=\"display: none;\"> </div> </form> <script type=\"text/javascript\"> var NotificationStartTime = 120000; //milliseconds before session time out var PopupRefreshDelay = 10000; //milliseconds between popup refresh var timeout; var firstTime = true; var counter = 60; var interval; object = { func: function() { WTSoftwareWeb.SessionTimeoutWS.GetSessionTimeout(OnSuccessTimeoutWS, OnFailureTimeoutWS); } } $(document).ready(function() { WTSoftwareWeb.SessionTimeoutWS.GetSessionTimeout(OnSuccessTimeoutWS, OnFailureTimeoutWS); }); function RedirectToLoginPage() { //$('input[type=button][value=\"Log Out\"]').click();//Logout btnLogOut $('#btnLogOut').click();//Logout btnLogOut } function RefreshSession() { WTSoftwareWeb.SessionTimeoutWS.RefreshSession(OnSuccessTimeoutWS, OnFailureTimeoutWS); firstTime = true; clearInterval(interval); counter = 60; try { keepSessionAlive(); } catch (err) { } } function OnSuccessTimeoutWS(response) { if (response && response.IsAutentificated) { if (response.Result) { var TimeBeforeTimeOut = parseInt(response.Result) if (TimeBeforeTimeOut > NotificationStartTime) { TimeOutPopup.Hide(); timeout = setTimeout(function() { object.func.call(object) } , TimeBeforeTimeOut - NotificationStartTime); } else { TimeOutPopup.Show(); timeout = setTimeout(function() { object.func.call(object) } , PopupRefreshDelay); if (firstTime) { interval = setInterval(function() { counter--; if (counter >= 0) { $(\"#countDown\").text(counter.toString()); } }, 1000); } firstTime = false; } } } else { RedirectToLoginPage(); } } function OnFailureTimeoutWS(response) { RedirectToLoginPage(); } var TimeOutPopup = { Show: function () { $('#SessionTimeoutPopup').show(); }, Hide: function () { $('#SessionTimeoutPopup').hide(); }};</script><div id=\"SessionTimeoutPopup\" class=\"SessionTimeoutScreen\"> <div class=\"SessionTimeoutPopup\" > <div class=\"PopupHeader\"> <div class=\"ReasonCaption\"> Your Session is about to expire </div> </div> <div class=\"PopupBody\"> <div> <p> You've been inactive for a while. For your security, we'll automatically sign out in approximately <label id=\"countDown\">60</label> seconds. </p> </div> <div class=\"ButtonHolder\"> <input type=\"button\" id=\"btnSignedOut\" onclick=\"RedirectToLoginPage();\" value=\"Sign out\" class=\"TimeoutButton\" /> <input type=\"button\" id=\"btnTimeout\" onclick=\"RefreshSession();\" value=\"Stay signed in\" class=\"TimeoutButton\" /> </div> </div> </div></div>	<script language=\"javascript\" type=\"text/javascript\"> $(document).ready(function () { $(\"#txtConfirmPassword\").focus();	}); </script> <script type=\"text/javascript\" src=\"/Scripts/Ait.js?v=5\"></script> <script type=\"text/javascript\" src=\"/Scripts/wise.js?v=5\"></script> <script type=\"text/javascript\" src=\"/Scripts/Wagers.js?v=5\"></script> <script type=\"text/javascript\" src=\"/Scripts/element.js\"></script> <script type=\"text/javascript\" src=\"/Scripts/jquery/jquery.blockUI.js\"></script> <script type=\"text/javascript\" src=\"/Scripts/CustomerAjax.js?v=5\"></script> <script type=\"text/javascript\" src=\"/Scripts/jquery.validationEngine-en.js\"></script> <script type=\"text/javascript\" src=\"/Scripts/jquery.validationEngine.js\"></script> <script type=\"text/javascript\" src=\"/Scripts/Ajax.js?v=5\"></script> <script type=\"text/javascript\" language=\"javascript\"> var orden = 1; var onSubmit = false; function OnSubmitRequest() { if (!onSubmit) { onSubmit = true; return true; } return false; } function deleteorder(id) { if (document.forms[0]['txtOrden'].value.indexOf(id) != -1) { document.forms[0]['txtOrden'].value = document.forms[0]['txtOrden'].value.replace((id + '@'), ' '); } } function sideorder(obj1) { var valor = ''; valor = obj1.name; deleteorder(valor); if (obj1.value != 0 || obj1.value != '') { document.forms[0]['txtOrden'].value = document.forms[0]['txtOrden'].value + valor + '@'; } return true; } function OnChecked(count, chkName, disabled) { var i; for (i = 2; i <= count; i++) { if (disabled == 'False') { document.getElementById(chkName + i).checked = false; document.forms[0]['txtWagerTypeParlay'].checked = false; } else { document.getElementById(chkName + i).checked = false; document.forms[0]['txtWagerType'].checked = false; } } } function test() { if (document.forms[0]['txtWagerTypeParlay'].checked == true) { return false; } return true; } function setfocus() { document.getElementById('txtWagerAmount').focus() } function mp_onload() { if (window.body_onload != null) window.body_onload(); } function imageButtonOnKeyPress() { var keyCodeEntered = (window.event.which) ? window.event.which : window.event.keyCode; if (keyCodeEntered == 13) { event.returnValue = false; return false; } return true; } function ValidateLiveCasino(URL, clientCode) { var items = JSON.stringify({ clientCode: clientCode }); var isOk = false; $.ajax({ type: \"POST\", url: \"ValidateLiveCasinoLCD.aspx/ValidateClientBalance\", data: items, async: false, contentType: \"application/json;charset=utf-8\", dataType: \"json\", success: function (result) { res = result.d if (res) { // window.open(URL, \"Live Casino\", \"fullscreen=yes\"); isOk = true; } else { alert(\"No balance is available to transfer. Please try again later.\"); } }, error: function (xhr, errorType, exception) { return false; OnFail(xhr, errorType, exception); } }); return isOk; } function OnFail(xhr, errorType, exception) { alert(\"Message: \" + xhr.statusText); } $(document).keypress(function (e) { if (e.which == 13) { $(\"#cmdContinue\").click(); } }); </script></body></html>";
//			Map<String, String> lines = lineTrackerParser.processLineChange(xhtml);
//			LOGGER.error("lines: " + lines);
			String json = "{\n" + 
					"    \"d\": {\n" + 
					"        \"ElementID\": \"Cbo-1037586-1-2-4-11-1\",\n" + 
					"        \"MultipleOffers\": null,\n" + 
					"        \"BuySellPoints\": \"\\\"<PS><lso LINE=\\\\\\\"-2\\\\\\\" TEXT=\\\\\\\"U 46Â½ +103\\\\\\\" SELECTED=\\\\\\\"\\\\\\\" />\\\\n<lso LINE=\\\\\\\"-1\\\\\\\" TEXT=\\\\\\\"U 47 -101\\\\\\\" SELECTED=\\\\\\\"\\\\\\\" />\\\\n<lso LINE=\\\\\\\"0\\\\\\\" TEXT=\\\\\\\"U 47Â½ -105\\\\\\\" SELECTED=\\\\\\\"selected\\\\\\\" />\\\\n<lso LINE=\\\\\\\"1\\\\\\\" TEXT=\\\\\\\"U 48 -112\\\\\\\" SELECTED=\\\\\\\"\\\\\\\" />\\\\n<lso LINE=\\\\\\\"2\\\\\\\" TEXT=\\\\\\\"U 48Â½ -119\\\\\\\" SELECTED=\\\\\\\"\\\\\\\" />\\\\n</PS>\\\"\",\n" + 
					"        \"DefaultLine\": \"\",\n" + 
					"        \"Status\": true\n" + 
					"    }\n" + 
					"}";
			LineTrackerTeamPackage lineTrackerTeamPackage = new LineTrackerTeamPackage();
			lineTrackerParser.parseJSON(json, "total", lineTrackerTeamPackage);
			LOGGER.error("lineTrackerTeamPackage: " + lineTrackerTeamPackage);

/*			Map<String, String> inputFields = new HashMap<String, String>();
			List<LineTrackerEventPackage> retValue = lineTrackerParser.parseGames(xhtml, "", inputFields);
			if (retValue != null) {
				for (int x = 0; x < retValue.size(); x++) {
					LineTrackerEventPackage ltp = retValue.get(x);
				}
			}
*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseIndex(java.lang.String)
	 */
	@Override
	public Map<String, String> parseIndex(String xhtml) throws BatchException {
		LOGGER.info("Entering parseIndex()");
		final Map<String, String> map = new HashMap<String, String>();

		// Parse the html
		final Document doc = parseXhtml(xhtml);

		// Check for iframe
		final Elements iframes = doc.select("iframe");
		if (iframes != null && iframes.size() > 0) {
			final Element iframe = iframes.get(0);
			if (iframe != null) {
				map.put("iframe", iframe.attr("src"));
			}
		}

		// Get form and input parameters
		final Elements forms = doc.select("form");
		for (int z = 0;(forms != null && z < forms.size()); z++) {
			final Element form = forms.get(z);
			if (form != null && "form1".equals(form.attr("name"))) {
				// Get form action field
				map.put("action", form.attr("action"));
				getAllElementsByName(form, "input", "value", map);
			}
		}

		LOGGER.info("Exiting parseIndex()");
		return map;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseLogin(java.lang.String)
	 */
	@Override
	public Map<String, String> parseLogin(String xhtml) throws BatchException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseMenu(java.lang.String, java.lang.String[], java.lang.String[])
	 */
	@Override
	public Map<String, String> parseMenu(String xhtml, String[] type, String[] sport) throws BatchException {
		LOGGER.info("Entering parseMenu()");
		LOGGER.debug("types: " +  java.util.Arrays.toString(type));
		LOGGER.debug("sport: " + java.util.Arrays.toString(sport));
		final Map<String, String> retValue = new HashMap<String, String>();

		// Parse the HTML
		final Document doc = parseXhtml(xhtml);

		// Get hidden input fields
		final String[] types = new String[] {"hidden"};
		final Elements forms = doc.select("form");
		for (int i = 0; (forms != null && i < forms.size()); i++) {
			final Element form = forms.get(i);
			retValue.put("action", form.attr("action"));
			getAllElementsByType(form, "input", types, retValue);
		}

		// Continue button
		Element cont = doc.getElementById("cmdContinue1");
		if (cont != null) {
			retValue.put(cont.attr("name"), cont.attr("value"));
			String onclick = cont.attr("onclick");
			if (onclick != null) {
				int index = onclick.indexOf(", \"/");
				if (index != -1) {
					onclick = onclick.substring(index + ", \"/".length() - 1);
					index = onclick.indexOf("\",");
					if (index != -1) {
						onclick = onclick.substring(0, index);
						retValue.put("action", onclick);
					}
				}
			}
		} else {
			cont = doc.getElementById("cmdContinue");
			if (cont != null) {
				retValue.put(cont.attr("name"), cont.attr("value"));
				String onclick = cont.attr("onclick");
				if (onclick != null) {
					int index = onclick.indexOf(", \"/");
					if (index != -1) {
						onclick = onclick.substring(index + ", \"/".length() - 1);
						index = onclick.indexOf("\",");
						if (index != -1) {
							onclick = onclick.substring(0, index);
							retValue.put("action", onclick);
						}
					}
				}
			}
		}

		// Menu items
		Elements tbodys = doc.select(".sportTable tbody");
		findMenu(tbodys, "tr th div span", "tr td div", sport, type, retValue);

		tbodys = doc.select(".StraightHtmlMenu tbody");
		findMenu(tbodys, "tr td div span", "td div div", sport, type, retValue);
		
		// check if we should throw an exception
		if (retValue.isEmpty()) {
			// Throw an exception
			throw new BatchException(BatchErrorCodes.SITE_PARSER_EXCEPTION, BatchErrorMessage.SITE_PARSER_EXCEPTION,
					" LineTrackerParser::parseMenu() exception");
		}

		LOGGER.info("Exiting parseMenu()");
		return retValue;
	}

	/**
	 * 
	 * @param tbodys
	 * @param select
	 * @param sport
	 * @param type
	 * @param retValue
	 */
	private void findMenu(Elements tbodys, String select, String select2, String[] sport, String[] type, Map<String, String> retValue) {
		LOGGER.info("Entering findMenu()");
		LOGGER.debug("tbodys: " + tbodys);
		LOGGER.debug("select: " + select);
		LOGGER.debug("select2: " + select2);
		for (int x = 0; (tbodys != null && x < tbodys.size()); x++) {
			final Element tbody = tbodys.get(x);
			final Elements spans = tbody.select(select);
			for (int y = 0; (spans != null && y < spans.size()); y++) {
				final Element span = spans.get(y);
				LOGGER.debug("Span: " + span);
				String spanValue = getHtmlFromElement(span);
				LOGGER.debug("SpanValue: " + spanValue);
				if (spanValue != null && spanValue.length() > 0) {
					boolean sportFound = false;
					for (int z = 0; z < sport.length; z++) {
						if (sport[z].equals(spanValue)) {
							sportFound = true;
						}
					}

					LOGGER.debug("SportFound: " + sportFound);
					// Now check for type
					if (sportFound) {
						sportFound(tbody, select2, type, retValue);
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param tbody
	 * @param select
	 * @param type
	 * @param retValue
	 */
	private void sportFound(Element tbody, String select, String[] type, Map<String, String> retValue) {
		Elements divs = tbody.select(select);
		for (int z = 0; (divs != null && z < divs.size()); z++) {
			boolean typeFound = false;
			final Element div = divs.get(z);
			final Elements divSpans = div.select("span");
			for (int a = 0; (divSpans != null && a < divSpans.size()); a++) {
				final Element divSpan = divSpans.get(a);
				if (divSpan != null) {
					String divSpanValue = getHtmlFromElement(divSpan);
					if (divSpanValue != null && divSpanValue.length() > 0) {
						for (int b = 0; b < type.length; b++) {
							LOGGER.debug("type[b]: " + type[b]);
							LOGGER.debug("divSpanValue: " + divSpanValue);
							if (type[b].equals(divSpanValue)) {
								typeFound = true;
							}
						}
					}
				}
			}

			LOGGER.debug("typeFound: " + typeFound);
			// Check if typeFound
			if (typeFound) {
				typeFound(div, retValue);
			}	
		}
	}

	/**
	 * 
	 * @param div
	 * @param retValue
	 */
	private void typeFound(Element div, Map<String, String> retValue) {
		final String sportClick = div.attr("onclick");
		if (sportClick != null && sportClick.length() > 0) {
			LOGGER.debug("sportClick: " + sportClick);
			int index = sportClick.indexOf("SportClick(\"");
			if (index != -1) {
				String value = sportClick.substring(index + "SportClick(\"".length());
				index = value.indexOf("\"");
				if (index != -1) {
					value = value.substring(0, index);
					String tempValue = retValue.get("hidden");
					LOGGER.debug("tempValue: " + tempValue);
					if (tempValue != null && tempValue.length() > 0) {
						tempValue += ";" + value;
						LOGGER.debug("tempValue2: " + tempValue);
						retValue.put("hidden", tempValue);
					} else {
						LOGGER.debug("value: " + value);
						retValue.put("hidden", value);	
					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseGames(java.lang.String, java.lang.String, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <LineTrackerEventPackage> List<LineTrackerEventPackage> parseGames(String xhtml, String type, Map<String, String> inputFields) throws BatchException {
		LOGGER.info("Entering parseGames()");
		LOGGER.debug("type: " + type);

		List<?> events = null;
		final Document doc = parseXhtml(xhtml);
		
		// Get hidden input fields
		final String[] types = new String[] { "hidden" };
		final Elements forms = doc.select("form");
		for (int i = 0; (forms != null && i < forms.size()); i++) {
//			getAllSelects(forms.get(i), inputFields);
			getAllElementsByType(forms.get(i), "input", types, inputFields);
		}

		// Continue button
		final Elements inputs = doc.select("input");
		for (int x = 0; (inputs != null && x < inputs.size()); x++) {
			final Element input = inputs.get(x);
			final String contValue = input.attr("value");
			if (contValue != null && contValue.equals("Continue")) {
				inputFields.put(input.attr("name"), input.attr("value"));
				final String onclick = input.attr("onclick");
				if (onclick != null && onclick.contains("WagerTicket.aspx")) {
					inputFields.put("action", "WagerTicket.aspx");
					break;
				}
			}
		}

		
		// Get the game data
		final Elements elements = doc.select(".TableWager tbody tr");
		if (elements != null && elements.size() > 0) {
			events = getGameData(elements);
		}

		LOGGER.info("Exiting parseGames()");
		return (List<LineTrackerEventPackage>)events;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseEventSelection(java.lang.String, com.ticketadvantage.services.dao.sites.SiteTeamPackage, java.lang.String)
	 */
	@Override
	public Map<String, String> parseEventSelection(String xhtml, SiteTeamPackage siteTeamPackage, String type) throws BatchException {
		LOGGER.info("Entering parseEventSelection()");
		final Map<String, String> map = new HashMap<String, String>();

		// Parse the HTML first
		final Document doc = parseXhtml(xhtml);

		// Get the risk value
		String risk = getHtmlFromDocumentByClass(doc, "risk_cell", 0);
		if (risk != null) {
			risk = risk.replaceAll(",", "");
			map.put("risk", risk);
		}

		// Get the win value
		String win = getHtmlFromDocumentByClass(doc, "win_cell", 0);
		if (win != null) {
			win = win.replaceAll(",", "");
			map.put("win", win);
		}

		// Get hidden input fields
		final Element form = doc.getElementById("Form1");
		final String[] types = new String[] { "hidden", "password" };
		if (form != null) {
			map.put("action", form.attr("action"));
			getAllElementsByType(form, "input", types, map);
		}

		// Get the submit button
		final Element submit = doc.getElementById("cmdSubmit");
		if (submit != null) {
			map.put(submit.attr("name"), submit.attr("value"));
		}

		if (xhtml.contains("At least one line (or price or pitcher) has changed")) {
			// Line changed throw exception
			throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed and cannot complete transaction", xhtml);
		}

		if (xhtml.contains("The maximum wager : $")) {
			int index = xhtml.indexOf("The maximum wager : $");
			if (index != -1) {
				xhtml = xhtml.substring(index + "The maximum wager : $".length());
				index = xhtml.indexOf(" USD");
				if (index != -1) {
					String wagerAmount = xhtml.substring(0, index);
					if (wagerAmount != null) {
						wagerAmount = wagerAmount.replaceAll(",", "");
						map.put("wageramount", wagerAmount);
					}
				}
			}
		}

		// Check for a wager limit and change it 
		if (xhtml.contains("Your wager limit for this bet is $")) {
			int index = xhtml.indexOf("Your wager limit for this bet is $");
			if (index != -1) {
				xhtml = xhtml.substring(index + "Your wager limit for this bet is $".length());
				index = xhtml.indexOf("</td>");
				if (index != -1) {
					String wagerAmount = xhtml.substring(0, index);
					if (wagerAmount != null) {
						wagerAmount = wagerAmount.replaceAll(",", "");
						map.put("wageramount", wagerAmount);
					}
				}
			}
		}
		if (xhtml.contains("risk amount for your selected wagers of")) {
			int index = xhtml.indexOf("risk amount for your selected wagers of");
			if (index != -1) {
				xhtml = xhtml.substring(index + "risk amount for your selected wagers of".length());
				index = xhtml.indexOf("is greater than your available balance of ");
				if (index != -1) {
					xhtml = xhtml.substring(index + "is greater than your available balance of ".length());
					index = xhtml.indexOf("USD");
					if (index != -1) {
						xhtml = xhtml.substring(0, index);
						xhtml = xhtml.replaceAll(",", "");
						xhtml = xhtml.replaceAll(" ", "");
						xhtml = xhtml.replaceAll("&nbsp;", "");
						map.put("wageraccountlimit", xhtml);
					}
				}
			}
		}
		
		LOGGER.info("Exiting parseEventSelection()");
		return map;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseTicketNumber(java.lang.String)
	 */
	@Override
	public String parseTicketNumber(String xhtml) throws BatchException {
		LOGGER.info("Entering parseTicketTransaction()");
		String ticketNumber = "Ticket Number - ";
		final String ticketInfo = "<span class='TicketNumber'>";

		if (xhtml.contains("At least one line (or price or pitcher) has changed")) {
			// Line changed throw exception
			throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed and cannot complete transaction", xhtml);
		}

		// First check for Wager has been accepted!
		if (xhtml != null && xhtml.contains(ticketInfo)) {
			// Great, wager is complete; now get the Ticket Number
			int index = xhtml.indexOf(ticketInfo);
			if (index != -1) {
				final String nxhtml = xhtml.substring(index + ticketInfo.length());
				index = nxhtml.indexOf("</span>");
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
			ticketNumber = "Failed to get ticket number";
			throw new BatchException(BatchErrorCodes.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, BatchErrorMessage.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, ticketNumber, xhtml);
		}

		LOGGER.info("Exiting parseTicketTransaction()");
		return ticketNumber;
	}

	/**
	 * 
	 * @param xhtml
	 * @return
	 * @throws BatchException
	 */
	public Map<String, String> processLineChange(String xhtml) throws BatchException {
		LOGGER.info("Entering processLineChange()");
		final Map<String, String> map = new HashMap<String, String>();

//		<div class='TdDetailTicket'>
//        	<font class='SportTicket'>Basketball - NBA</font><br />
//        	<font class='TeamRotationTicket'>[508]</font> 
//        	<font class='TeamNameTicket'>New York Knicks</font> 
//        	<font class='TeamDateTicket'>10/21/2017 <font class='GameTime'>(08:05 PM)</font></font><br />
//        	<font class='PointsTicket'>+2Â½</font> 
//        	<font class='ScoreTypeTicket'>Points</font> 
//        	<font class='OddsTicket'>-110</font> 
//        	<font class='PeriodTypeTicket'>for the Game <font class='LineChange'>(LineChange)</font></font>
//        	<div class='RegulationOnlyText'></div>
//      </div>

		final Document doc = parseXhtml(xhtml);
		final Elements fonts = doc.select("font");
		boolean isTotal = false;

		for (int x = 0; (fonts != null && x < fonts.size()); x++) {
			final Element font = fonts.get(x);
			LOGGER.debug("font: " + font);

			final String pointsTicket = font.attr("class");
			final String oddsTicket = font.attr("class");
			final String targetTicket = font.attr("class");
			LOGGER.debug("pointsTicket: " + pointsTicket);
			LOGGER.debug("oddsTicket: " + oddsTicket);
			LOGGER.debug("targetTicket: " + oddsTicket);
			

			if (targetTicket != null && targetTicket.equals("TargetTicket")) {
				final String html = font.html();
				if (html != null && html.length() > 0) {
					if (html.toLowerCase().equals("over")) {
						map.put("valueindicator", "o");
						isTotal = true;
					} else if (html.toLowerCase().equals("under")) {
						map.put("valueindicator", "u");
						isTotal = true;
					}
				}
			}

			if (pointsTicket != null && pointsTicket.equals("PointsTicket")) {
				String html = font.html();
				if (html != null && html.length() > 0) {
					LOGGER.debug("isTotal: " + isTotal);
					LOGGER.debug("html: " + html);

					if (isTotal) {
						html = html.trim();
						map.put("value", super.reformatValues(html));
					} else {
						final Map<String, String> line = parseSpread(super.reformatValues(html.trim()), -1);
						map.put("valueindicator", line.get("valindicator"));
						map.put("value", super.reformatValues(line.get("val")));
					}
				}
			}

			if (oddsTicket != null && oddsTicket.equals("OddsTicket")) {
				final String html = font.html();
				if (html != null && html.length() > 0) {
					final Map<String, String> spreadJuice = parseJuice(super.reformatValues(html.trim()), null, null);
					map.put("juiceindicator", spreadJuice.get("juiceindicator"));
					map.put("juice", super.reformatValues(spreadJuice.get("juice")));
				}
			}
		}

		// Parse xhtml and get action
		final Element form = doc.getElementById("Form1");
		if (form != null) {
			final String action = form.attr("action");
			LOGGER.debug("action: " + action);
			map.put("action", action);

			final String[] types = new String[] {"hidden", "password"};
			getAllElementsByType(form, "input", types, map);
		}

		LOGGER.info("Exiting processLineChange()");
		return map;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parsePendingBets(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Set<PendingEvent> parsePendingBets(String xhtml, String accountName, String accountId) throws BatchException {
		LOGGER.info("Entering parsePendingBets()");
		LOGGER.debug("accountName: " + accountName);
		LOGGER.debug("accountId: " + accountId);
		Set<PendingEvent> pendingEvents = new HashSet<PendingEvent>();

		// Get the document object
		final Document doc = parseXhtml(xhtml);
		final Elements trs = doc.select(".TablePendingWagers tbody tr");
		LOGGER.debug("trs: " + trs);
		if (trs != null) {
			for (Element tr : trs) {
				try {
					String trHtml = tr.html();
					if (trHtml != null && !trHtml.contains("Regular Season Wins")) {
						final String className = tr.attr("class");
						if (className != null && className.contains("BackgroundLine") && !className.contains(("Invisible"))) {
							final Elements tds = tr.select("td");
							final PendingEvent pe = new PendingEvent();
							for (Element td : tds) {
								LOGGER.debug("td: " + td);
								pe.setAccountname(accountName);
								pe.setAccountid(accountId);
								final String tdClass = td.attr("class");
								LOGGER.debug("tdClass: " + tdClass);
		
								if (tdClass != null && tdClass.length() > 0) {
									if (tdClass.equals("tdDateTime")) {
										getDateAccepted(td, pe);
									} else if (tdClass.equals("tdTicket")) {
										getTicketNumber(td, pe);
									} else if (tdClass.equals("tdGameDetail")) {
										getEventInfo(td, pe);								
									} else if (tdClass.equals("tdWagerType")) {
										getEventType(td, pe);
									} else if (tdClass.equals("tdRisk")) {
										getRisk(td, pe);
									} else if (tdClass.equals("tdWin")) {
										getWin(td, pe);								
									}
								}
							}
							pendingEvents.add(pe);
						}
					}
				} catch (Throwable t) {
					LOGGER.error(t.getMessage(), t);
				}
			}	
		}

		LOGGER.info("Exiting parsePendingBets()");
		return pendingEvents;
	}

	/**
	 * 
	 * @param json
	 * @param wagerType
	 * @param lineTrackerTeamPackage
	 * @return
	 * @throws BatchException
	 */
	public LineTrackerTeamPackage parseJSON(String json, String wagerType, LineTrackerTeamPackage lineTrackerTeamPackage) throws BatchException {
		LOGGER.info("Entering parseJSON()");
		LOGGER.debug("json: " + json);
		LOGGER.debug("wagerType: " + wagerType);

		int lbindex = json.indexOf("LINE=\\\\\\\"");
		int leindex = json.indexOf("\\\\\\\" TEXT=");
		while (lbindex != -1 && leindex != -1) {
			String line = json.substring(lbindex + "LINE=\\\\\\\"".length(), leindex);
			LOGGER.debug("line: " + line);
			json = json.substring(leindex + "\\\\\\\" TEXT=".length());
			lbindex = json.indexOf("\\\\\\\"");
			leindex = json.indexOf("\\\\\\\" SELECTED");
			String text = json.substring(lbindex + "\\\\\\\"".length(), leindex);
			LOGGER.debug("text: " + text);
//			text = text.replaceAll("[0xc2][0xbd]", ".5");
			text = text.replaceAll("Â½", ".5");
			LOGGER.debug("LINE: " + line);
			LOGGER.debug("TEXT: " + text);
			Integer lineNumber = Integer.parseInt(line);
			if (lineNumber != null) { 
				if ("spread".equals(wagerType)) {
					lineTrackerTeamPackage.addGameSpreadOptionValue(line, line);
	
					// -2½  +200 Now parse the data
					lineTrackerTeamPackage = (LineTrackerTeamPackage)parseSpreadData(reformatValues(text), lineNumber, " ", null, lineTrackerTeamPackage);
				} else if ("total".equals(wagerType)) {
					lineTrackerTeamPackage.addGameTotalOptionValue(line, line);
					
					// O 40 -125 Now parse the data
					lineTrackerTeamPackage = (LineTrackerTeamPackage)parseTotalData(reformatValues(text), lineNumber, " ", null, lineTrackerTeamPackage);					
				}
			}

			// Go to next line
			lbindex = json.indexOf("LINE=\\\\\\\"");
			leindex = json.indexOf("\\\\\\\" TEXT=");	
		}

		LOGGER.info("Exiting parseJSON()");
		return lineTrackerTeamPackage;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#getGameData(org.jsoup.select.Elements)
	 */
	@SuppressWarnings("unchecked")
	@Override	
	protected List<LineTrackerEventPackage> getGameData(Elements elements) throws BatchException {
		LOGGER.info("Entering getGameData()");

		final List<LineTrackerEventPackage> events = new ArrayList<LineTrackerEventPackage>();
		LineTrackerEventPackage eventPackage = null;
		LineTrackerTeamPackage team1 = null;
		LineTrackerTeamPackage team2 = null;
		int rows = 0;

		// Loop through all the elements
		for (Element element : elements) {
			if (!element.html().contains("chkBoxPitcher")) {
				// Loop through the elements and then check for the dates
				final String classInfo = element.attr("class");

				// Check which row we have
				if (classInfo != null && classInfo.contains("BackgroundLine") && rows == 1) {
					// Means we have second row/team
					team2 = getTeamData(element, new LineTrackerTeamPackage());

					// Setup the dates
					final String team1Date = team1.gettDate();
					final String team2Date = team2.gettDate();
					LOGGER.debug("team1Date: " + team1Date);
					LOGGER.debug("team2Date: " + team2Date);
					final Date datesData = setDates(team1Date, team2Date, eventPackage);
					team1.setEventdatetime(datesData);
					team2.setEventdatetime(datesData);

					// Setup the event package
					eventPackage.setSiteteamone(team1);
					eventPackage.setSiteteamtwo(team2);
					eventPackage.setTeamone(team1);
					eventPackage.setTeamtwo(team2);
					events.add(eventPackage);
					rows = 0;
					// Note this need to be second because of the contains
					// statement
				} else if (classInfo != null && classInfo.contains("BackgroundLine")) {
					rows++;

					if (!classInfo.contains("lastRow")) {
						// Means we have the first team
						eventPackage = new LineTrackerEventPackage();
						team1 = getTeamData(element, new LineTrackerTeamPackage());
						eventPackage.setId(team1.getId());
					} else {
						rows = 0;
					}
				}
			}
		}

		LOGGER.info("Exiting getGameData()");
		return events;
	}

	/**
	 * 
	 * @param element
	 * @param team
	 * @return
	 * @throws BatchException
	 */
	private LineTrackerTeamPackage getTeamData(Element element, LineTrackerTeamPackage team) throws BatchException {
		LOGGER.info("Entering getTeamData()");

		final Elements elements = element.select("td");
		for (int x = 0;(elements != null && x < elements.size()); x++) {
			final Element td = elements.get(x);
			if (td.html().contains("chkBoxPitcher")) {
				
			}
			String className = td.attr("class");

			// Check which one to work
			if (className != null) {
				className = className.trim();
				if ("tdDate".equals(className)) {
					team = getDate(td, team);
				} else if ("tdTeamName".equals(className)) {
					team = getTeam(td, team);
				} else if ("tdSpread".equals(className)) {
					team = getSpread(td, team);
				} else if ("tdMoneyLine".equals(className)) {
					team = getMoneyLine(td, team);
				} else if ("tdTotal".equals(className)) {
					team = getTotal(td, team);
				}
			}
		}
		LOGGER.info("Exiting getTeamData()");
		return team;
	}

	/**
	 * 
	 * @param td
	 * @param team
	 * @return
	 */
	private LineTrackerTeamPackage getDate(Element td, LineTrackerTeamPackage team) {
		LOGGER.info("Entering getDate()");

		// Get the date
		team.settDate(getHtmlFromElement(td, "font", 0, true));

		LOGGER.info("Exiting getDate()");
		return team;
	}

	/**
	 * 
	 * @param td
	 * @param team
	 * @return
	 * @throws BatchException
	 */
	private LineTrackerTeamPackage getTeam(Element td, LineTrackerTeamPackage team) throws BatchException {
		LOGGER.info("Entering getTeam()");

		// Get rotation ID
		String gameId = getHtmlByClassName(td, "rotation_number", 0);
		if (gameId != null && gameId.length() > 0) {
			team.setEventid(gameId);
			team.setId(Integer.parseInt(gameId));
		} else {
			// Throw an exception
			throw new BatchException(BatchErrorCodes.SITE_PARSER_EXCEPTION,  
					BatchErrorMessage.SITE_PARSER_EXCEPTION, "LineTrackerParser:getRotation ID is empty");
		}
		
		// Get team name
		String teamName = getHtmlByClassName(td, "team_name", 0);
		if (teamName != null && teamName.length() > 0) {
			team.setTeam(teamName);
		} else {
			// Throw an exception
			throw new BatchException(BatchErrorCodes.SITE_PARSER_EXCEPTION,  
					BatchErrorMessage.SITE_PARSER_EXCEPTION, "LineTrackerParser:getTeam name is empty");
		}

		LOGGER.info("Exiting getTeam()");
		return team;
	}

	/**
	 * 
	 * @param td
	 * @param team
	 * @return
	 */
	private LineTrackerTeamPackage getSpread(Element td, LineTrackerTeamPackage team) throws BatchException {
		LOGGER.info("Entering getSpread()");

		// First get the input field information
		Map<String, String> hashMap = parseInputField(td.select("table tbody tr td input"), 0);
		if (hashMap != null && !hashMap.isEmpty()) {
			team.setGameSpreadInputId(hashMap.get("id"));
			team.setGameSpreadInputName(hashMap.get("name"));
		}

		// Get the select fields
		hashMap = parseSelectField(td.select("table tbody tr td select"));
		if (hashMap != null && !hashMap.isEmpty()) {
			team.setGameSpreadSelectId(hashMap.get("id"));
			team.setGameSpreadSelectName(hashMap.get("name"));
			team.setGameSpreadSelectFor(hashMap.get("for"));
		}

		final Elements options = td.select("table tbody tr td select option");
		if (options != null && options.size() > 0) {
			team = (LineTrackerTeamPackage)parseSpreadSelectOption(options, team, " ", null);
		}

		LOGGER.info("Exiting getSpread()");
		return team;
	}

	/**
	 * 
	 * @param td
	 * @param team
	 * @return
	 */
	private LineTrackerTeamPackage getMoneyLine(Element td, LineTrackerTeamPackage team) {
		LOGGER.info("Entering getMoneyLine()");

		// First get the input field information
		final Map<String, String> hashMap = parseInputField(td.select("table tbody tr td input"), 0);
		if (hashMap != null && !hashMap.isEmpty()) {
			team.setGameMLInputId(hashMap.get("id"));
			team.setGameMLInputName(hashMap.get("name"));
		}

		final String mlData = getHtmlFromElement(td, "table tbody tr td font", 0, false);
		if (mlData != null && mlData.length() > 0) {
			team = (LineTrackerTeamPackage)parseMlData(reformatValues(mlData), 0, team);
		}

		LOGGER.info("Exiting getMoneyLine()");
		return team;
	}

	/**
	 * 
	 * @param td
	 * @param team
	 * @return
	 */
	private LineTrackerTeamPackage getTotal(Element td, LineTrackerTeamPackage team) throws BatchException {
		LOGGER.info("Entering getTotal()");

		// First get the input field information
		Map<String, String> hashMap = parseInputField(td.select("table tbody tr td input"), 0);
		if (hashMap != null && !hashMap.isEmpty()) {
			team.setGameTotalInputId(hashMap.get("id"));
			team.setGameTotalInputName(hashMap.get("name"));
		}

		// Get the select fields
		hashMap = parseSelectField(td.select("table tbody tr td select"));
		if (hashMap != null && !hashMap.isEmpty()) {
			team.setGameTotalSelectId(hashMap.get("id"));
			team.setGameTotalSelectName(hashMap.get("name"));
			team.setGameTotalSelectFor(hashMap.get("for"));
		}

		// o 42½ +200; Now parse the data
		final Elements options = td.select("table tbody tr td select option");
		if (options != null && options.size() > 0) {
			team = (LineTrackerTeamPackage)parseTotalSelectOption(options, team, " ", null);
		}

		LOGGER.info("Exiting getTotal()");
		return team;
	}

	/**
	 * 
	 * @param team1Date
	 * @param team2Date
	 * @param eventPackage
	 * @return
	 * @throws BatchException
	 */
	private Date setDates(String team1Date, String team2Date, LineTrackerEventPackage eventPackage) throws BatchException {
		LOGGER.info("Entering setDates()");
		
		// Now that we have both, set the dates correctly
		String cDate = "";
		Date newDate = null;

		try {
			final Calendar now = Calendar.getInstance();
			int offset = now.get(Calendar.DST_OFFSET);
			cDate = team1Date + "/" + String.valueOf(now.get(Calendar.YEAR)) + " " + team2Date;
			if (offset != 0) {
				cDate += " PDT";
			} else {
				cDate += " PST";
			}
			newDate = DATE_FORMAT.parse(cDate);
			eventPackage.setDateofevent(cDate);
		} catch (ParseException pe) {
			LOGGER.error("ParserExeption for " + cDate, pe);
			// Throw an exception
			throw new BatchException(BatchErrorMessage.SITE_PARSER_EXCEPTION
					+ " LineTrackerParser:parser parsing date exception for " + cDate);
		}

		LOGGER.info("Exiting setDates()");
		return newDate;
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 */
	private void getDateAccepted(Element td, PendingEvent pe) {
		LOGGER.info("Entering getDateAccepted()");

		// <td class="tdDateTime" align="center">2017/05/27<br>03:02:14 AM</td>
		String html = td.html();
		if (html != null && html.length() > 0) {
			LOGGER.info("html: " + html);
			pe.setDateaccepted(html.replaceAll("<br>", " ").trim());
		}

		LOGGER.info("Exiting getDateAccepted()");
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 */
	private void getTicketNumber(Element td, PendingEvent pe) {
		LOGGER.info("Entering getTicketNumber()");
		LOGGER.debug("td: " + td);

		// <td class="tdTicket" align="right">1678139</td>
		String html = td.html();
		if (html != null && html.length() > 0) {
			LOGGER.info("html: " + html);
			pe.setTicketnum(html.trim());
		}

		LOGGER.info("Exiting getTicketNumber()");
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 */
	private void getEventInfo(Element td, PendingEvent pe) {
		LOGGER.info("Entering getEventInfo()");
		LOGGER.debug("td: " + td);

		// <font class="SportTicket">Basketball - NBA</font>
		// <font class="SportTicket">Football - NCAA - Added / Extra</font>
		getSportInfo(td.select(".SportTicket"), pe);

		String gameType = null;
		final Elements periods = td.select(".PeriodTypeTicket");
		if (periods != null && periods.size() > 0) {
			gameType = periods.get(0).html();
		}

		// <font class="TeamRotationTicket">[702]</font>
		getRotationInfo(td.select(".TeamRotationTicket"), gameType, pe);

		// <font class="TeamNameTicket">Golden State Warriors</font>
		getTeamNameInfo(td.select(".TeamNameTicket"), pe);
		
		// <font class="TeamDateTicket">06/01/2017<font class="GameTime">(08:00 PM)</font><font class="WagerState">(Pending)</font></font>
		getEventDateInfo(td.select(".TeamDateTicket"), pe);
		
		// <font class="PointsTicket"> -4</font>
		// Means we have a spread
		getOverUnderMoneyLineInfo(td.select(".TargetTicket"), pe);

		// <font class="PointsTicket"> -4</font>
		// Means we have a spread
		getLineInfo(td.select(".PointsTicket"), pe);

		// <font class="OddsTicket"> -115</font>
		getLineJuiceInfo(td.select(".OddsTicket"), pe);

		// <font class="PeriodTypeTicket"> for the 1st Half</font>
		getLineTypeInfo(td.select(".PeriodTypeTicket"), pe);

		// <font class="PitcherTicket">M. Fulmer(must start) T. Bauer(must start)</font>
		getPitcherTicket(td.select(".PitcherTicket"), pe);

		LOGGER.info("Exiting getEventInfo()");
	}

	/**
	 * 
	 * @param fonts
	 * @param pe
	 */
	private void getSportInfo(Elements fonts, PendingEvent pe) {
		LOGGER.info("Exiting getSportInfo()");
		// <font class="SportTicket">Basketball - NBA</font>
		
		if (fonts != null && fonts.size() > 0) {
			String html = fonts.get(0).html();
			if (html != null && html.length() > 0) {
				int index = html.indexOf("-");
				if (index != -1) {
					html = html.replace(" - Added / Extra", "");
					final String sportName = html.substring(0 , index);
					String sportType = html.substring(index + 1);

					LOGGER.debug("sportName: " + sportName);
					if (sportName != null) { 
						pe.setGamesport(sportName.trim());
					}

					LOGGER.debug("sportType: " + sportType);
					if (sportType != null) {
						sportType = sportType.replace(" - Added / Extra", "").trim();
						sportType = sportType.replace(" - FCS", "").trim();
						pe.setGametype(sportType.trim());
					}
				}
			}
		}

		LOGGER.info("Exiting getSportInfo()");
	}

	/**
	 * 
	 * @param fonts
	 * @param gameType
	 * @param pe
	 */
	private void getRotationInfo(Elements fonts, String gameType, PendingEvent pe) {
		LOGGER.info("Exiting getRotationInfo()");
		// <font class="TeamRotationTicket">[702]</font>
		
		if (fonts != null && fonts.size() > 0) {
			String html = fonts.get(0).html();
			LOGGER.debug("html" + html);

			if (html != null && html.length() > 0) {
				html = html.replaceAll("\\[", "");
				html = html.replaceAll("\\]", "");

				if (gameType.contains("1st Half")) {
					html = "1" + html;
				} else if (gameType.contains("2nd Half")) {
					html = "2" + html;
				}

				pe.setRotationid(html);
			}
			
		}

		LOGGER.info("Exiting getRotationInfo()");
	}

	/**
	 * 
	 * @param fonts
	 * @param pe
	 */
	private void getTeamNameInfo(Elements fonts, PendingEvent pe) {
		LOGGER.info("Exiting getTeamNameInfo()");
		// <font class="TeamNameTicket">Golden State Warriors</font>
		
		if (fonts != null && fonts.size() > 0) {
			String html = fonts.get(0).html();
			LOGGER.debug("html" + html);

			if (html != null && html.length() > 0) {
				pe.setTeam(html.trim());
			}
		}

		LOGGER.info("Exiting getTeamNameInfo()");
	}

	/**
	 * 
	 * @param fonts
	 * @param pe
	 */
	private void getEventDateInfo(Elements fonts, PendingEvent pe) {
		LOGGER.info("Exiting getEventDateInfo()");
		// <font class="TeamDateTicket"> 06/01/2017<font class="GameTime">(08:00 PM)</font><font class="WagerState">(Pending)</font></font>

		if (fonts != null && fonts.size() > 0) {
			String html = fonts.get(0).html();
			LOGGER.debug("html" + html);

			String dateInfo = null;
			if (html != null && html.length() > 0) {
				int index = html.indexOf("<font");
				if (index != -1) {
					dateInfo = html.substring(0, index).trim();
				}
			}

			String timeInfo = null;
			Elements gFonts = fonts.get(0).select(".GameTime");
			if (gFonts != null && gFonts.size() > 0) {
				timeInfo = gFonts.get(0).html();
				if (timeInfo != null && timeInfo.length() > 0) {
					timeInfo = timeInfo.replaceAll("\\(", "");
					timeInfo = timeInfo.replaceAll("\\)", "").trim();
				}
			}

			pe.setEventdate(dateInfo + " " + timeInfo + " " + super.determineTimeZone(super.timezone));
			try {
				pe.setGamedate(DATE_FORMAT.parse(pe.getEventdate()));
			} catch (ParseException pee) {
				LOGGER.error(pee.getMessage(), pee);
			}
		}

		LOGGER.info("Exiting getEventDateInfo()");
	}

	/**
	 * 
	 * @param fonts
	 * @param pe
	 */
	private void getOverUnderMoneyLineInfo(Elements fonts, PendingEvent pe) {
		LOGGER.info("Entering getOverUnderMoneyLineInfo()");
		// <font class="PointsTicket"> -4</font>

		for (Element font : fonts) {
			String html = font.html();
			LOGGER.debug("html" + html);

			if (html != null && html.length() > 0) {
				html = html.trim();
				if ("Over".equals(html)) {
					pe.setEventtype("total");
					pe.setLineplusminus("o");
				} else if ("Under".equals(html)) {
					pe.setEventtype("total");
					pe.setLineplusminus("u");
				} else if ("Money Line".equals(html)) {
					pe.setEventtype("ml");
				}
			}
		}

		LOGGER.info("Exiting getOverUnderMoneyLineInfo()");
	}

	/**
	 * 
	 * @param fonts
	 * @param pe
	 */
	private void getLineInfo(Elements fonts, PendingEvent pe) {
		LOGGER.info("Entering getLineInfo()");
		// <font class="PointsTicket"> -4</font>

		if (fonts != null && fonts.size() > 0) {
			String html = fonts.get(0).html();
			LOGGER.debug("html" + html);
			if (html != null && html.length() > 0) {
				html = html.trim();
				html = html.replaceAll("Â", "");
				LOGGER.debug("pe,getEventtype(): " + pe.getEventtype());
				if ("total".equals(pe.getEventtype())) {
					// Set the line
					pe.setLine(super.reformatValues(html));
				} else if ("ml".equals(pe.getEventtype())) {
					// Do nothing
				} else {
					int plusdIndex = html.indexOf("+");
					int minusIndex = html.indexOf("-");
					if (plusdIndex != -1 || minusIndex != -1) {
						pe.setEventtype("spread");
						pe.setLineplusminus(html.substring(0, 1));
						pe.setLine(super.reformatValues(html.substring(1).trim()));
					} else {
						int pkIndex = html.indexOf("pk");
						int PKIndex = html.indexOf("PK");
						int evIndex = html.indexOf("ev");
						int EVIndex = html.indexOf("EV");
						if (pkIndex != -1 || PKIndex != -1 || evIndex != -1 || EVIndex != -1) {
							pe.setEventtype("spread");
							pe.setLineplusminus("+");
							pe.setLine("0");
						} else {
							int evenIndex = html.indexOf("even");
							int EvenIndex = html.indexOf("Even");
							if (evenIndex != -1 || EvenIndex != -1) {
								pe.setEventtype("spread");
								pe.setLineplusminus("+");
								pe.setLine("0");
							}
						}
					}
				}
			}
		}

		LOGGER.info("Exiting getLineInfo()");
	}

	/**
	 * 
	 * @param fonts
	 * @param pe
	 */
	private void getLineJuiceInfo(Elements fonts, PendingEvent pe) {
		LOGGER.info("Entering getLineJuiceInfo()");
		// <font class="OddsTicket"> -115</font>

		if (fonts != null && fonts.size() > 0) {
			String html = fonts.get(0).html();
			LOGGER.debug("html" + html);

			if (html != null && html.length() > 0) {
				html = html.trim();
				html = html.replaceAll("Â", "");
				int plusdIndex = html.indexOf("+");
				int minusIndex = html.indexOf("-");
				if (plusdIndex != -1 || minusIndex != -1) {
					pe.setJuiceplusminus(html.substring(0, 1));
					pe.setJuice(super.reformatValues(html.substring(1).trim()));
					if ("ml".equals(pe.getEventtype())) {
						pe.setLineplusminus(pe.getJuiceplusminus());
						pe.setLine(pe.getJuice());
					}
				} else {
					int pkIndex = html.indexOf("pk");
					int PKIndex = html.indexOf("PK");
					int evIndex = html.indexOf("ev");
					int EVIndex = html.indexOf("EV");
					if (pkIndex != -1 || PKIndex != -1 || evIndex != -1 || EVIndex != -1) {
						pe.setJuiceplusminus("+");
						pe.setJuice("100");
						if ("ml".equals(pe.getEventtype())) {
							pe.setLineplusminus(pe.getJuiceplusminus());
							pe.setLine(pe.getJuice());
						}
					} else {
						int evenIndex = html.indexOf("even");
						int EvenIndex = html.indexOf("Even");
						if (evenIndex != -1 || EvenIndex != -1) {
							pe.setJuiceplusminus("+");
							pe.setJuice("100");
							if ("ml".equals(pe.getEventtype())) {
								pe.setLineplusminus(pe.getJuiceplusminus());
								pe.setLine(pe.getJuice());
							}
						}
					}
				}
			}
		}

		LOGGER.info("Exiting getLineJuiceInfo()");
	}

	/**
	 * 
	 * @param fonts
	 * @param pe
	 */
	private void getLineTypeInfo(Elements fonts, PendingEvent pe) {
		LOGGER.info("Entering getLineTypeInfo()");
		// <font class="PeriodTypeTicket"> for the 1st Half</font>

		if (fonts != null && fonts.size() > 0) {
			String html = fonts.get(0).html();
			LOGGER.debug("html" + html);

			if (html != null && html.length() > 0) {
				if (html.contains("1st Half")) {
					pe.setLinetype("first");
				} else if (html.contains("2nd Half")) {
					pe.setLinetype("second");
				} else {
					pe.setLinetype("game");
				}
			}
		}
		
		LOGGER.info("Exiting getLineTypeInfo()");
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 */
	private void getEventType(Element td, PendingEvent pe) {
		LOGGER.info("Entering getEventType()");
		LOGGER.debug("td: " + td);

		// <td class="tdWagerType" align="center"><font class="FreePlay"></font><br>Straight</td>
		String html = td.html();
		if (html != null && html.length() > 0) {
			html = html.replaceAll("&nbsp;", "");
			LOGGER.info("html: " + html);
			if (html.contains("Money Line")) {
				pe.setEventtype("ml");
			} else if (html.contains("Spread")) {
				pe.setEventtype("spread");
			} else if (html.contains("Total")) {
				pe.setEventtype("total");
			}
		}

		LOGGER.info("Exiting getEventType()");
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 */
	private void getPitcherTicket(Elements fonts, PendingEvent pe) {
		LOGGER.info("Entering getPitcherTicket()");

		// <font class="PitcherTicket">M. Fulmer(must start) T. Bauer(must start)</font>
		if (fonts != null && fonts.size() > 0) {
			String html = fonts.get(0).html();
			LOGGER.debug("html" + html);

			if (html != null && html.length() > 0) {
				pe.setPitcher(html.trim());
			}
		}

		LOGGER.info("Exiting getPitcherTicket()");
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 */
	private void getRisk(Element td, PendingEvent pe) {
		LOGGER.info("Entering getRisk()");
		LOGGER.debug("td: " + td);

		// <td class="tdRisk" align="center">28.75</td>
		String html = td.html();
		if (html != null && html.length() > 0) {
			LOGGER.info("html: " + html);
			pe.setRisk(html.trim());
		}
		
		LOGGER.info("Exiting getRisk()");
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 */
	private void getWin(Element td, PendingEvent pe) {
		LOGGER.info("Entering getWin()");
		LOGGER.debug("td: " + td);

		// <td class="tdWin" align="center">25.00</td>
		String html = td.html();
		if (html != null && html.length() > 0) {
			LOGGER.info("html: " + html);
			pe.setWin(html.trim());
		}
		
		LOGGER.info("Exiting getWin()");
	}
}