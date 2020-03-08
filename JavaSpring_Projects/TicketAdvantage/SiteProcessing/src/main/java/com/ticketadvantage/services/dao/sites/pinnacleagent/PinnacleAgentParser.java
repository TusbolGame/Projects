/**
 * 
 */
package com.ticketadvantage.services.dao.sites.pinnacleagent;

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
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.PendingEvent;

/**
 * @author jmiller
 *
 */
public class PinnacleAgentParser extends SiteParser {
	private static final Logger LOGGER = Logger.getLogger(PinnacleAgentParser.class);

	/**
	 * Constructor
	 */
	public PinnacleAgentParser() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// String xhtml = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><link rel=\"stylesheet\" type=\"text/css\" href=\"Main.css\" /><title>	Bets History</title>    <script type=\"text/javascript\" language=\"javascript\">        function show(row)        {		        var hid=document.getElementById('show'+row);		        if(hid)	        {		        if (hid.style.display==\"none\")		        {			        hid.style.display=\"\";		        }		        else		        {		        hid.style.display=\"none\";		        }	        }        }                function OnDropDownChange(dropDownName)        {            var ddlGames=document.getElementById('ddlGames');	            var ddlContests=document.getElementById('ddlContests');            var ddlRaces=document.getElementById('ddlRaces');                         switch(dropDownName)            {                case \"ddlGames\":                    ddlContests.selectedIndex = 0;                    ddlRaces.selectedIndex = 0;                    break;                case \"ddlContest\":                    ddlGames.selectedIndex = 0;                    ddlRaces.selectedIndex = 0;                    break;                                    case \"ddlRaces\":                    ddlGames.selectedIndex = 0;                    ddlContests.selectedIndex = 0;                    break;                                }        }                function disableControls(dropDownName)        {             var frm = document.forms[0];                         switch(dropDownName)            {                case \"ddlAgentGroup\":                    frm.ddlStatus.disabled = true;                                        break;                case \"ddlStatus\":                    frm.ddlAgentGroup.disabled = true;                    break;                                               }            frm.ddlSortDirection.disabled = true;            frm.ddlCust.disabled = true;            frm.ddlBetType.disabled = true;            frm.DRSUC_SB.disabled = true;            frm.DRSUC_STB.disabled = true;            frm.DRSUC_ETB.disabled = true;                        frm.DRSUC_btnOneDay.disabled = true;            frm.DRSUC_btnSevenDays.disabled = true;            frm.DRSUC_btnFourteenDays.disabled = true;                        if (frm.DRSUC_btnAll!=null)                frm.DRSUC_btnAll.disabled = true;            if (frm.btnGotoPage != null)                frm.btnGotoPage.disabled = true;            if (frm.txtGotoPage != null)                frm.txtGotoPage.disabled = true;	                        if (frm.btnFirst != null)                frm.btnFirst.disabled = true;	            if (frm.btnPrevious!=null)                frm.btnPrevious.disabled = true;	            if (frm.btnNext != null)                frm.btnNext.disabled = true;            if (frm.btnLast != null)                frm.btnLast.disabled = true;                                                   }                function ShowWagerTypeCombo()		{		    var ddlBt=document.getElementById('ddlBetType');	    	    var vGames = document.getElementById(\"divGames\");     	    var vContests = document.getElementById(\"divContests\");     	    var vRaces = document.getElementById(\"divRaces\");     	        	    switch(ddlBt.options[ddlBt.selectedIndex].value)    	    {      	        case \"\":    	            vGames.style.display = 'none';    	            vContests.style.display = 'none';    	            vRaces.style.display = 'none';    	            break;          	        case \"G\":    	            vGames.style.display = '';                	            vContests.style.display = 'none';    	            vRaces.style.display = 'none';    	            break;          	        case \"C\":    	            vContests.style.display = '';    	            vGames.style.display = 'none';    	            vRaces.style.display = 'none';    	            break;      	        case \"R\":    	            vRaces.style.display = '';    	            vGames.style.display = 'none';    	            vContests.style.display = 'none';    	            break;       	                	                      	    }		}    </script>    <link href=\"App_Themes/BetsHistory/BetsHistory.css\" type=\"text/css\" rel=\"stylesheet\" /></head><body onload='ShowWagerTypeCombo()'>    <script type=\"text/javascript\" src=\"/Scripts/JQuery/jquery-1.6.2.js\"></script>    <script type=\"text/javascript\" >        $(document).ready(function () {            $(\"#txtGotoPage\").keydown(function (event) {                // Allow only backspace and delete                if (event.keyCode == 46 || event.keyCode == 8) {                    // let it happen, don't do anything                }                else {                    // Ensure that it is a number and stop the keypress                    if (event.keyCode < 48 || event.keyCode > 57) {                        event.preventDefault();                    }                }            });        })    </script>    <form name=\"form1\" method=\"post\" action=\"./BetsHistory.aspx\" language=\"javascript\" onsubmit=\"javascript:return WebForm_OnSubmit();\" id=\"form1\"><input type=\"hidden\" name=\"__EVENTTARGET\" id=\"__EVENTTARGET\" value=\"\" /><input type=\"hidden\" name=\"__EVENTARGUMENT\" id=\"__EVENTARGUMENT\" value=\"\" /><input type=\"hidden\" name=\"__LASTFOCUS\" id=\"__LASTFOCUS\" value=\"\" /><input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"/wEPDwUJODYzMjEzMzczD2QWAgIBD2QWBAIBD2QWCAIDDw8WAh4EVGV4dAUQTG9nZ2VkIGluOiBNc3RycGRkAgsPDxYCHwAFClcvTCBUb3RhbHNkZAIXDw8WAh4HVmlzaWJsZWhkZAIZDw8WAh8BaGRkAgMPZBYUAgMPEA8WBh4ORGF0YVZhbHVlRmllbGQFCUFnZW50Q29kZR4NRGF0YVRleHRGaWVsZAUJQWdlbnRDb2RlHgtfIURhdGFCb3VuZGcWAh4Ib25jaGFuZ2UFK2phdmFzY3JpcHQ6ZGlzYWJsZUNvbnRyb2xzKCdkZGxBZ2VudEdyb3VwJykQFQgFeHBpa2UGeHBpa2UxBnhQaWtlMQZ4UGlrZTIGWFBJS0UzBlhQSUtFNAZ4UElLRTUGeHBpa2U2FQgFeHBpa2UGeHBpa2UxBnhQaWtlMQZ4UGlrZTIGWFBJS0UzBlhQSUtFNAZ4UElLRTUGeHBpa2U2FCsDCGdnZ2dnZ2dnFgFmZAIHDxAPZBYCHwUFJ2phdmFzY3JpcHQ6ZGlzYWJsZUNvbnRyb2xzKCdkZGxTdGF0dXMnKWQWAQICZAIJD2QWDAIEDw8WAh8ABQExZGQCBg8PFgIfAAUBMWRkAggPDxYCHgdFbmFibGVkaGRkAgkPDxYCHwZoZGQCCg8PFgIfBmhkZAILDw8WAh8GaGRkAg0PEA8WAh8EZ2QQFQgABTUyMDAxBTUyMDEwBTUyMDQ4BTUyMDY1BTUyMDY3BTUyNTA3BTU0MDA1FQgABTUyMDAxBTUyMDEwBTUyMDQ4BTUyMDY1BTUyMDY3BTUyNTA3BTU0MDA1FCsDCGdnZ2dnZ2dnFgFmZAIRDxAPZBYCHwUFH2phdmFzY3JpcHQ6U2hvd1dhZ2VyVHlwZUNvbWJvKCkQFQMABUdhbWVzCENvbnRlc3RzFQMAAUcBQxQrAwNnZ2cWAWZkAhUPEGRkFgECAWQCHQ8QDxYGHwIFBVZhbHVlHwMFBFRleHQfBGcWAh8FBSdqYXZhc2NyaXB0Ok9uRHJvcERvd25DaGFuZ2UoJ2RkbEdhbWVzJykQFQcAG0NhcmxvcyBDb25kaXQtdnMtTWF0dCBCcm93bg5EdWtlLXZzLUthbnNhcyBNaWNoYWVsIENoaWVzYS12cy1BbnRob255IFBldHRpcy1SQiBMZWlwemlnIHRvIGFkdmFuY2UtdnMtTWFyc2VpbGxlIHRvIGFkdmFuY2UkUm9zZSBOYW1hanVuYXMtdnMtSm9hbm5hIEplZHJ6ZWpjenlrL1NldmlsbGEgdG8gYWR2YW5jZS12cy1CYXllcm4gTXVuY2hlbiB0byBhZHZhbmNlFQcACTgyNDg4MTAxOAk4MzE4NjQ2NzkJODIyMjEwNjEwCTgyOTE5ODMwMgk4MTA0NzkzNjcJODI5MTc5OTY0FCsDB2dnZ2dnZ2dkZAIhDxAPFgYfAgUFVmFsdWUfAwUEVGV4dB8EZxYCHwUFKmphdmFzY3JpcHQ6T25Ecm9wRG93bkNoYW5nZSgnZGRsQ29udGVzdHMnKRAVEQBNQmFzZWJhbGwgUHJvcHN8TUxCfFJlZ3VsYXIgU2Vhc29uIFdpbnN8Q2hpY2FnbyBXaGl0ZSBTb3ggUmVndWxhciBTZWFzb24gV2lucz9MQmFzZWJhbGwgUHJvcHN8TUxCfFJlZ3VsYXIgU2Vhc29uIFdpbnN8Q29sb3JhZG8gUm9ja2llcyBSZWd1bGFyIFNlYXNvbiBXaW5zP05CYXNlYmFsbCBQcm9wc3xNTEJ8UmVndWxhciBTZWFzb24gV2luc3xLYW5zYXMgQ2l0eSBSb3lhbHMgUmVndWxhciBTZWFzb24gV2lucz9JQmFzZWJhbGwgUHJvcHN8TUxCfFJlZ3VsYXIgU2Vhc29uIFdpbnN8TWlhbWkgTWFybGlucyBSZWd1bGFyIFNlYXNvbiBXaW5zP01CYXNlYmFsbCBQcm9wc3xNTEJ8UmVndWxhciBTZWFzb24gV2luc3xNaWx3YXVrZWUgQnJld2VycyBSZWd1bGFyIFNlYXNvbiBXaW5zP1FCYXNlYmFsbCBQcm9wc3xNTEJ8UmVndWxhciBTZWFzb24gV2luc3xQaGlsYWRlbHBoaWEgUGhpbGxpZXMgUmVndWxhciBTZWFzb24gV2lucz9PQmFzZWJhbGwgUHJvcHN8TUxCfFJlZ3VsYXIgU2Vhc29uIFdpbnN8U3QuIExvdWlzIENhcmRpbmFscyBSZWd1bGFyIFNlYXNvbiBXaW5zP1BCYXNlYmFsbCBQcm9wc3xNTEJ8UmVndWxhciBTZWFzb24gV2luc3xXYXNoaW5ndG9uIE5hdGlvbmFscyBSZWd1bGFyIFNlYXNvbiBXaW5zP0tCYXNrZXRiYWxsIFByb3BzfE5CQXxSZWd1bGFyIFNlYXNvbiBXaW5zfEF0bGFudGEgSGF3a3MgUmVndWxhciBTZWFzb24gV2lucz9MQmFza2V0YmFsbCBQcm9wc3xOQkF8UmVndWxhciBTZWFzb24gV2luc3xCb3N0b24gQ2VsdGljcyBSZWd1bGFyIFNlYXNvbiBXaW5zP01CYXNrZXRiYWxsIFByb3BzfE5CQXxSZWd1bGFyIFNlYXNvbiBXaW5zfERldHJvaXQgUGlzdG9ucyBSZWd1bGFyIFNlYXNvbiBXaW5zP1BCYXNrZXRiYWxsIFByb3BzfE5CQXxSZWd1bGFyIFNlYXNvbiBXaW5zfExvcyBBbmdlbGVzIExha2VycyBSZWd1bGFyIFNlYXNvbiBXaW5zP1RCYXNrZXRiYWxsIFByb3BzfE5CQXxSZWd1bGFyIFNlYXNvbiBXaW5zfE1pbm5lc290YSBUaW1iZXJ3b2x2ZXMgUmVndWxhciBTZWFzb24gV2lucz9JQmFza2V0YmFsbCBQcm9wc3xOQ0FBfE1pZHdlc3QgUmVnaW9uIFdpbm5lcnxLYW5zYXMgdG8gd2luIE1pZHdlc3QgUmVnaW9uP0dCYXNrZXRiYWxsIFByb3BzfE5DQUF8TWlkd2VzdCBSZWdpb24gV2lubmVyfFRlYW0gdG8gd2luIE1pZHdlc3QgUmVnaW9uPz9Tb2NjZXIgUHJvcHN8VUVGQSAtIEV1cm9wYSBMZWFndWV8T3V0cmlnaHR8RXVyb3BhIExlYWd1ZSBXaW5uZXIVEQAJODE4ODE0NzAzCTgxODgxNDc5OQk4MTg4MTUwMDIJODE4ODE1MDg5CTgxODgxNTEzNQk4MTg4MTUzMjIJODE4ODE1NjAzCTgxODgxNTc3Ngk3MzM4OTg4NjQJNzMzODk4OTczCTczMzg5OTQ0NAk3MzM5MDEwMDMJNzMzOTAxMjM2CTgyNzg2MzUwNwk4Mjc4NjI4MDUJNzk1OTUyODczFCsDEWdnZ2dnZ2dnZ2dnZ2dnZ2dnZGQCJQ8QDxYGHwIFBVZhbHVlHwMFBFRleHQfBGdkEBUBABUBABQrAwFnZGQCJw9kFgQCAg8PFgQeDE1heGltdW1WYWx1ZQUJMy8yNi8yMDE4HgxNaW5pbXVtVmFsdWUFCTMvMjUvMjAxNGRkAgUPDxYEHwcFCTMvMjYvMjAxOB8IBQkzLzI1LzIwMTRkZBgBBQZUQldfR1YPPCsADAEIAgFk05rS5DXhSyMin4e4VSg+H3rpNPOttYopabl5dJnqUtw=\" /><script type=\"text/javascript\"><!--var theForm = document.forms['form1'];if (!theForm) {    theForm = document.form1;}function __doPostBack(eventTarget, eventArgument) {    if (!theForm.onsubmit || (theForm.onsubmit() != false)) {        theForm.__EVENTTARGET.value = eventTarget;        theForm.__EVENTARGUMENT.value = eventArgument;        theForm.submit();    }}// --></script><script src=\"/WebResource.axd?d=S4Sz6Z0QgCp_DS-FYfAGYG5oj0yoPqfPyvuNzQZPdCululyt_4MASq6NwQwigXzmrDMJeSxcr076mM5G4YadzquTcqS9JxrFXFg4bY1pZ9U1&amp;t=636161530540000000\" type=\"text/javascript\"></script><script src=\"/WebResource.axd?d=YZQgc39B7EmGHSOvt7SLj06AsOOLyKxRw3tb6x_0__m92wevvvZv8MEov4-ml-B5G0Rr_t6wQQWEI120a2udGnOtdlaPP2DW_D28ktjwcVM1&amp;t=636161530540000000\" type=\"text/javascript\"></script><script type=\"text/javascript\"><!--function WebForm_OnSubmit() {if (typeof(ValidatorOnSubmit) == \"function\" && ValidatorOnSubmit() == false) return false;return true;}// --></script><input type=\"hidden\" name=\"__VIEWSTATEGENERATOR\" id=\"__VIEWSTATEGENERATOR\" value=\"9997B33F\" /><input type=\"hidden\" name=\"__EVENTVALIDATION\" id=\"__EVENTVALIDATION\" value=\"/wEdAENm6UaKMbUXBk31WW8ECufhyLlAjqg4XtI41Mw6vK9uEHJLoK5U+nS46oQ4JjecaURYdsfy82JuWBEC+ABqTox7soH0vKtS8BdBgK5WTDoP10dNC3y3BYtuAw1gvR9JZUfO6dD8Zv3WlPrERIWAxkf/uszP5LxCTuKnj7iG8+Ev5+H6O9DyMyEXxoYss/aTh29PStedrsEvyMVJnODXztiqp1uOtL2fovFZjtn/2twD1dJ4dyDDtyrjxyFIaHIsTa47YxMCLQXTALvpj6barERLEh+F0sM036YEiscgG3GIMMxR7TN+BPh9bd/6fcCTh4cR6yND9+hJjabz3qZgOJAsS6CKxq4NO1oGHpr0o1MHZzBr9jssiwIaSQOAWpmE3UyBY6SC6BYHkKr11gPu2hA0A3sKKRfJWOelyk1KBhWs60EkX3mmUGnkw7Dq7iIBd1OpzscnQIEBcZMjSpsbrorr4yqIycC1H6Gwav8VRF2tX8p4yNBtabBcg/CuzD9ykTiXRvCxwKNvDtguCknaYpaSj23/wnbfnDncp+CLKcf7sGmQcGabN58NtumQ8D3fUD/dX+DlNR3MNFt68lLIfl/2XIoVtmI5Gw8J0MbwFAsyj9NhrdFWl2O0tDV+kbesK3xnvT1oH7VlUSvwc5gb627vsP+XHEwYHnOewS4BqrEfMklyPjjsbBsWeb9KFx2rI+LsiKGORarcSfR+vVUmljYzFa1xhAV30rpAYVQQ97/aqEJ3UjMhcc/tkO+7+WtMVWToxxoIze9WQMl6Y1e3vGF0b9aCpH+9sHRUKXZofI9pk2Ce7sPDdJpEPVyCZ2Z6xYu/JBbghrYiJyP7yjA7Ee2gyuTf+c3L+WXgRaRWFSVUpl6tIpj+KFdiIqW7v1pbAQZuKkhatYTlf7KRzYC7KF8cfNftM92qVyTjDRDaoRbueKExVlkddgFx9TFzHOFXjb9vdmfM7EpMYk+x+HGUkPx3Sfhr7ERvu/5QUsyesaBvma+uwWYEFqstl5op90nQ3TjUR5vF+MYoAAHz0vBjE+mQhiRdY6iKNkMWK5cfdXcXljaYkrDRpzYfe3Yk5akA7iEihA7DkZC+G+WwwLVSDCUcG5NjI8yPmTb//x/wRNc3yEbp+xuciKiu9DM0LRoKJJFKn++M56vqHnnjxMy+ljzHM3ZQnLtAoKQH+C7xgf3B0tCDd18lAIHA2ktBcRxTe5nGj9ADm+W4PwQxgKzg0yc2ziJDSkLhcHw844jOTYlTJtmCG49/N7HGjn0pU0rnKM0+uH7anNdAANlPAyLgjF80SUKAs1UQ9BgYsJJGyGyyFC8jXQC/NgExGWriK2TV9KZ9vzmh7slm12A7sjPb7jRGeVsCwq9P1hrj3jTm2LM3uDaW/O0TROqK4FNO+CSp9R1Dttz5dX4QD7VzZ7AhK7tLJpEjcj3qIbBA5POm4ImSqIvKw+pxrdW3VIplzR0qVhg=\" />    <div id=\"header\">       <table width=\"100%\">        <tr>            <td style=\"width: 20%; font-size: 1em; vertical-align:bottom;\">            </td>            <td style=\"text-align:center;\">                <img id=\"PaHeader1_Image1\" src=\"Images/Header.gif\" align=\"absmiddle\" border=\"0\" />            </td>             <td style=\"width: 20%;  text-align:right; font-size: .8em; vertical-align:bottom;\">                <span id=\"PaHeader1_loggedIn\" style=\"color:White;\">Logged in: Mstrp</span>            </td>        </tr>    </table>               </div><div id=\"menu\">    <table>    <tr>    <td width=\"100%\">	    <a id=\"PaHeader1_hlHome\" class=\"MenuItem\" href=\"Default.aspx\">Home</a>	    <a id=\"PaHeader1_hlLogout\" class=\"MenuItem\" href=\"AgentLogin.aspx\">Logout</a>	    <a id=\"PaHeader1_hlBankingFees\" class=\"MenuItem\" href=\"paBankingFees.aspx\">Banking Fees</a>	    <a id=\"PaHeader1_hlWL\" class=\"MenuItem\" href=\"paDailyFigTotals.aspx\">W/L Totals</a>	    <a id=\"PaHeader1_hlCasinoWL\" class=\"MenuItem\" href=\"paCasinoWinLoss.aspx\">Casino Win/Loss</a>	    <a id=\"PaHeader1_hlCommission\" class=\"MenuItem\" href=\"paCommission.aspx\">Commission</a>	    <a id=\"PaHeader1_hlAgentDetails\" class=\"MenuItem\" href=\"paAgent.aspx\">Agent Details</a>	    <a id=\"PaHeader1_hlSummary\" class=\"MenuItem\" href=\"paAgentSummary.aspx\">Summary</a>	    <a id=\"PaHeader1_hlAllBalance\" class=\"MenuItem\" href=\"paAllBalance.aspx\">All Balance</a>	    	    	    <a id=\"PaHeader1_hlExposure\" class=\"MenuItem\" href=\"AAPRReport.aspx\">Exposure</a>	    <a id=\"PaHeader1_hlBetsHistory\" class=\"MenuItem\" href=\"BetsHistory.aspx\">Bets History</a>        <a id=\"PaHeader1_hlChangePassword\" class=\"MenuItem\" href=\"ChangePassword.aspx\">Change Password</a>    </td>    </tr>    </table></div>        <div style=\"height:100%;width:100%;\">	                        <table width=\"100%\">                <tr>                <td>                                <span id=\"lblAgentGroup\">AgentID:</span>                <select name=\"ddlAgentGroup\" onchange=\"javascript:disableControls(&#39;ddlAgentGroup&#39;);setTimeout(&#39;__doPostBack(\\&#39;ddlAgentGroup\\&#39;,\\&#39;\\&#39;)&#39;, 0)\" language=\"javascript\" id=\"ddlAgentGroup\">		<option selected=\"selected\" value=\"xpike\">xpike</option>		<option value=\"xpike1\">xpike1</option>		<option value=\"xPike1\">xPike1</option>		<option value=\"xPike2\">xPike2</option>		<option value=\"XPIKE3\">XPIKE3</option>		<option value=\"XPIKE4\">XPIKE4</option>		<option value=\"xPIKE5\">xPIKE5</option>		<option value=\"xpike6\">xpike6</option>	</select>                <span id=\"lblStatus\">Status:</span>                <select name=\"ddlStatus\" onchange=\"javascript:disableControls(&#39;ddlStatus&#39;);setTimeout(&#39;__doPostBack(\\&#39;ddlStatus\\&#39;,\\&#39;\\&#39;)&#39;, 0)\" language=\"javascript\" id=\"ddlStatus\">		<option value=\"X\">Cancelled</option>		<option value=\"G\">Graded</option>		<option selected=\"selected\" value=\"P\">Pending</option>	</select>                                </td>                                <td style=\"text-align: left;\">                    <input type=\"submit\" name=\"btnGotoPage\" value=\"Go to Page\" id=\"btnGotoPage\" /><input name=\"txtGotoPage\" type=\"text\" value=\"1\" maxlength=\"4\" id=\"txtGotoPage\" style=\"width:3em;\" />                        &nbsp;                        <span id=\"lblPage\"> Page </span><span id=\"lblCurrentPage\">1</span><span id=\"lblOf\"> of </span><span id=\"lblTotalPages\">1</span>                        &nbsp;                                                <input type=\"submit\" name=\"btnFirst\" value=\"First\" id=\"btnFirst\" disabled=\"disabled\" /><input type=\"submit\" name=\"btnPrevious\" value=\"Prev\" id=\"btnPrevious\" disabled=\"disabled\" /><input type=\"submit\" name=\"btnNext\" value=\"Next\" id=\"btnNext\" disabled=\"disabled\" /><input type=\"submit\" name=\"btnLast\" value=\"Last\" id=\"btnLast\" disabled=\"disabled\" />                </td>                           </tr>                        <tr>                <td>                         <span id=\"lblCust\">Cust:</span>                <select name=\"ddlCust\" onchange=\"javascript:setTimeout(&#39;__doPostBack(\\&#39;ddlCust\\&#39;,\\&#39;\\&#39;)&#39;, 0)\" language=\"javascript\" id=\"ddlCust\">		<option selected=\"selected\" value=\"\"></option>		<option value=\"52001\">52001</option>		<option value=\"52010\">52010</option>		<option value=\"52048\">52048</option>		<option value=\"52065\">52065</option>		<option value=\"52067\">52067</option>		<option value=\"52507\">52507</option>		<option value=\"54005\">54005</option>	</select>                <span id=\"lblBetType\">Type:</span>                <select name=\"ddlBetType\" onchange=\"javascript:ShowWagerTypeCombo();setTimeout(&#39;__doPostBack(\\&#39;ddlBetType\\&#39;,\\&#39;\\&#39;)&#39;, 0)\" language=\"javascript\" id=\"ddlBetType\">		<option selected=\"selected\" value=\"\"></option>		<option value=\"G\">Games</option>		<option value=\"C\">Contests</option>	</select>                       <span id=\"lblSort\">Sort:</span>                <select name=\"ddlSortDirection\" onchange=\"javascript:setTimeout(&#39;__doPostBack(\\&#39;ddlSortDirection\\&#39;,\\&#39;\\&#39;)&#39;, 0)\" language=\"javascript\" id=\"ddlSortDirection\">		<option value=\"A\">Date Ascending</option>		<option selected=\"selected\" value=\"D\">Date Descending</option>	</select>                                    <span id=\"lblTicket\">Ticket:</span>                    <input name=\"txtTicket\" type=\"text\" id=\"txtTicket\" onkeydown=\"return (!((event.keyCode>=65 &amp;&amp; event.keyCode &lt;= 95) || event.keyCode >= 106) &amp;&amp; event.keyCode!=32);\" style=\"width:120px;\" />            </td>            </tr>                <tr>            <td colspan=\"2\">            <div id=\"divGames\" style=\"DISPLAY: none\">                                <span id=\"lblGames\">Games:</span>                <select name=\"ddlGames\" id=\"ddlGames\" onchange=\"javascript:OnDropDownChange(&#39;ddlGames&#39;)\">		<option value=\"\"></option>		<option value=\"824881018\">Carlos Condit-vs-Matt Brown</option>		<option value=\"831864679\">Duke-vs-Kansas</option>		<option value=\"822210610\">Michael Chiesa-vs-Anthony Pettis</option>		<option value=\"829198302\">RB Leipzig to advance-vs-Marseille to advance</option>		<option value=\"810479367\">Rose Namajunas-vs-Joanna Jedrzejczyk</option>		<option value=\"829179964\">Sevilla to advance-vs-Bayern Munchen to advance</option>	</select>                        </div>                    <div id=\"divContests\" style=\"DISPLAY: none\">                                <span id=\"lblContests\">Contests:</span>                <select name=\"ddlContests\" id=\"ddlContests\" onchange=\"javascript:OnDropDownChange(&#39;ddlContests&#39;)\">		<option value=\"\"></option>		<option value=\"818814703\">Baseball Props|MLB|Regular Season Wins|Chicago White Sox Regular Season Wins?</option>		<option value=\"818814799\">Baseball Props|MLB|Regular Season Wins|Colorado Rockies Regular Season Wins?</option>		<option value=\"818815002\">Baseball Props|MLB|Regular Season Wins|Kansas City Royals Regular Season Wins?</option>		<option value=\"818815089\">Baseball Props|MLB|Regular Season Wins|Miami Marlins Regular Season Wins?</option>		<option value=\"818815135\">Baseball Props|MLB|Regular Season Wins|Milwaukee Brewers Regular Season Wins?</option>		<option value=\"818815322\">Baseball Props|MLB|Regular Season Wins|Philadelphia Phillies Regular Season Wins?</option>		<option value=\"818815603\">Baseball Props|MLB|Regular Season Wins|St. Louis Cardinals Regular Season Wins?</option>		<option value=\"818815776\">Baseball Props|MLB|Regular Season Wins|Washington Nationals Regular Season Wins?</option>		<option value=\"733898864\">Basketball Props|NBA|Regular Season Wins|Atlanta Hawks Regular Season Wins?</option>		<option value=\"733898973\">Basketball Props|NBA|Regular Season Wins|Boston Celtics Regular Season Wins?</option>		<option value=\"733899444\">Basketball Props|NBA|Regular Season Wins|Detroit Pistons Regular Season Wins?</option>		<option value=\"733901003\">Basketball Props|NBA|Regular Season Wins|Los Angeles Lakers Regular Season Wins?</option>		<option value=\"733901236\">Basketball Props|NBA|Regular Season Wins|Minnesota Timberwolves Regular Season Wins?</option>		<option value=\"827863507\">Basketball Props|NCAA|Midwest Region Winner|Kansas to win Midwest Region?</option>		<option value=\"827862805\">Basketball Props|NCAA|Midwest Region Winner|Team to win Midwest Region?</option>		<option value=\"795952873\">Soccer Props|UEFA - Europa League|Outright|Europa League Winner</option>	</select>                        </div>                        <div id=\"divRaces\" style=\"DISPLAY: none\">                                <span id=\"lblHorseRaces\">Races:</span>                <select name=\"ddlRaces\" id=\"ddlRaces\">		<option value=\"\"></option>	</select>                        </div>            </td>            </tr>                            <tr>            <td colspan=\"2\"><div>    <span id=\"DRSUC_SDL\">From date:</span>    <input name=\"DRSUC:STB\" type=\"text\" value=\"3/25/2014\" id=\"DRSUC_STB\" />    <span controltovalidate=\"DRSUC_STB\" errormessage=\"Maximum date range is the last 4 years.\" display=\"Dynamic\" id=\"DRSUC_STBRV\" type=\"Date\" dateorder=\"mdy\" cutoffyear=\"2029\" century=\"2000\" evaluationfunction=\"RangeValidatorEvaluateIsValid\" maximumvalue=\"3/26/2018\" minimumvalue=\"3/25/2014\" style=\"color:Red;display:none;\">Maximum date range is the last 4 years.</span>        <span id=\"DRSUC_EDL\">To:</span>    <input name=\"DRSUC:ETB\" type=\"text\" value=\"3/25/2018\" id=\"DRSUC_ETB\" />    <span controltovalidate=\"DRSUC_ETB\" errormessage=\"Maximum date range is the last 4 years.\" display=\"Dynamic\" id=\"DRSUC_ETBRV\" type=\"Date\" dateorder=\"mdy\" cutoffyear=\"2029\" century=\"2000\" evaluationfunction=\"RangeValidatorEvaluateIsValid\" maximumvalue=\"3/26/2018\" minimumvalue=\"3/25/2014\" style=\"color:Red;display:none;\">Maximum date range is the last 4 years.</span>        <span controltovalidate=\"DRSUC_ETB\" errormessage=\"The &#39;from&#39; date cannot be later than the &#39;to&#39; date.\" display=\"Dynamic\" id=\"DRSUC_ETBCV\" type=\"Date\" dateorder=\"mdy\" cutoffyear=\"2029\" century=\"2000\" evaluationfunction=\"CompareValidatorEvaluateIsValid\" controltocompare=\"DRSUC_STB\" controlhookup=\"DRSUC_STB\" operator=\"GreaterThanEqual\" style=\"color:Red;display:none;\">The 'from' date cannot be later than the 'to' date.</span>    <input type=\"submit\" name=\"DRSUC:SB\" value=\"Submit\" onclick=\"javascript:WebForm_DoPostBackWithOptions(new WebForm_PostBackOptions(&quot;DRSUC:SB&quot;, &quot;&quot;, true, &quot;&quot;, &quot;&quot;, false, false))\" language=\"javascript\" id=\"DRSUC_SB\" />    <input type=\"submit\" name=\"DRSUC:btnOneDay\" value=\"1 Day\" id=\"DRSUC_btnOneDay\" style=\"width:70px;\" />    <input type=\"submit\" name=\"DRSUC:btnSevenDays\" value=\"7 Days\" id=\"DRSUC_btnSevenDays\" style=\"width:70px;\" />    <input type=\"submit\" name=\"DRSUC:btnFourteenDays\" value=\"14 Days\" id=\"DRSUC_btnFourteenDays\" style=\"width:70px;\" />    <input type=\"submit\" name=\"DRSUC:btnAll\" value=\"All\" id=\"DRSUC_btnAll\" style=\"width:70px;\" /></div><script type=\"text/javascript\">    var pathUrl = \"http://agent.pinnaclesports.com/BetsHistory.aspx\";    var i = pathUrl.lastIndexOf(\"/\");    var baseUrl = pathUrl.slice(0, i);</script><script type=\"text/javascript\" src=\"/Scripts/JQuery/jquery-1.6.2.js\"></script><script type=\"text/javascript\" src=\"/Scripts/JQuery/UI/jquery.ui.core.js\"></script><script type=\"text/javascript\" src=\"/Scripts/JQuery/UI/jquery.ui.widget.js\"></script><script type=\"text/javascript\" src=\"/Scripts/JQuery/UI/jquery.ui.datepicker.js\"></script><script type=\"text/javascript\" src=\"/Scripts/JQuery/UI/DatePickerLocalisation/jquery.ui.datepicker-en-US.js\"></script><link rel=\"stylesheet\" type=\"text/css\" href=\"/Styles/JQueryRelated/jquery.ui.core.css\" /><link rel=\"stylesheet\" href=\"/Styles/JQueryRelated/jquery.ui.datepicker.css\" /><link rel=\"stylesheet\" href=\"/Styles/JQueryRelated/jquery.ui.theme.css\" /><script type=\"text/javascript\">    $(function () {                $.datepicker.setDefaults($.datepicker.regional['en-US']);        //init the calendar by invoking it with a button        $(\"#\" + STBID).datepicker({            showOn: \"button\",            buttonImage: baseUrl + \"/images/calendar.gif\",            buttonImageOnly: true        });                $(\"#\" + STBID).datepicker({ minDate: '-4y' }); //init minDate        $(\"#\" + STBID).datepicker({ maxDate: '+1d' }); //init maxDate        $(\"#\" + STBID).datepicker({ changeMonth: true }); //init ChangeMonth        $(\"#\" + STBID).datepicker({ changeYear: true }); //init ChangeYear        $(\"#\" + STBID).datepicker(\"option\", \"minDate\", '-4y');        $(\"#\" + STBID).datepicker(\"option\", \"maxDate\", '+1d');        $(\"#\" + STBID).datepicker(\"option\", \"changeMonth\", true);        $(\"#\" + STBID).datepicker(\"option\", \"changeYear\", true);        $(\"#\" + ETBID).datepicker({            showOn: \"button\",            buttonImage: baseUrl + \"/images/calendar.gif\",            buttonImageOnly: true        });        $(\"#\" + ETBID).datepicker({ minDate: '-4y' }); //init minDate        $(\"#\" + ETBID).datepicker({ maxDate: '+1d' }); //init maxDate        $(\"#\" + ETBID).datepicker({ changeMonth: true }); //init ChangeMonth        $(\"#\" + ETBID).datepicker({ changeYear: true }); //init ChangeYear        $(\"#\" + ETBID).datepicker(\"option\", \"minDate\", '-4y');        $(\"#\" + ETBID).datepicker(\"option\", \"maxDate\", '+1d');        $(\"#\" + ETBID).datepicker(\"option\", \"changeMonth\", true);        $(\"#\" + ETBID).datepicker(\"option\", \"changeYear\", true);    });</script></td>            </tr>            </table>                             <p>                <div>		<table cellpadding=\"2\" bordercolor=\"LightGrey\" border=\"0\" id=\"TBW_GV\" style=\"border-color:LightGrey;border-width:1px;border-style:None;font-size:10pt;width:100%;\">			<tr class=\"sortableheader\" valign=\"top\" style=\"color:White;background-color:#303498;\">				<th scope=\"col\">                                <span id=\"TBW_GV__ctl1_L2\">Cust Id</span>                            </th><th align=\"center\" scope=\"col\">                                <span id=\"TBW_GV__ctl1_L3\">Trans.Time</span>                            </th><th align=\"center\" scope=\"col\">                                <span id=\"TBW_GV__ctl1_L4\">Event</span>                            </th><th align=\"center\" scope=\"col\">                                <span id=\"TBW_GV__ctl1_L5\">Choice</span>                            </th><th align=\"center\" scope=\"col\">                                <span id=\"TBW_GV__ctl1_L6\">Handicap</span>                            </th><th align=\"center\" scope=\"col\">                                <span id=\"TBW_GV__ctl1_L7\">Odds</span>                            </th><th align=\"right\" scope=\"col\">                                <span id=\"TBW_GV__ctl1_L8\">Stake (USD)</span>                            </th><th align=\"right\" scope=\"col\">                                <span id=\"TBW_GV__ctl1_L8\">To Win (USD)</span>                            </th><th align=\"left\" scope=\"col\">                                <span id=\"TBW_GV__ctl1_L9\">Status</span>                            </th>			</tr><tr style=\"color:Black;background-color:#E6E6E6;\"><td valign=\"top\" align=\"left\"> 54044 </td><td valign=\"top\" align=\"center\">3/24/2018<br>8:21:28 AM</td><td style=\"width:400px;\" valign=\"top\" align=\"center\"><span id=\"hidden33\" onclick=\"show(33);\" style=\"FONT-WEIGHT: bold; COLOR: blue;CURSOR: pointer;\">Parlay 3 selections </span> Ref No: 831956301-1<br><br><span id=\"show33\" style=\"font-size: 8pt; font-weight: normal;\"><table><tbody><tr><td> 1.  </td><td nowrap=\"\"> 3/24/2018</td><td class=\"MT1\" nowrap=\"\">Minnesota Timberwolves vs <b><u>Philadelphia 76ers</u></b></td></tr><tr><td> </td><td nowrap=\"\">3:05 PM</td><td class=\"MT1\" nowrap=\"\">Basketball/NBA<br>(Game Money Line<br><b>2</b>@-320    [Win]</td><td colspan=\"2\"> </td></tr><tr><td colspan=\"3\"> </td></tr><tr><td> 2.  </td><td nowrap=\"\"> 3/24/2018</td><td class=\"MT1\" nowrap=\"\">Chicago Bulls vs <b><u>Detroit Pistons</u></b></td></tr><tr><td> </td><td nowrap=\"\">4:05 PM</td><td class=\"MT1\" nowrap=\"\">Basketball/NBA<br>(Game Money Line<br><b>2</b>@-1289    [Win]</td><td colspan=\"2\"> </td></tr><tr><td colspan=\"3\"> </td></tr><tr><td> 3.  </td><td nowrap=\"\">3/24/2018</td><td class=\"MT1\" nowrap=\"\">Phoenix Suns vs <b><u>Orlando Magic</u></b></td></tr><tr><td> </td><td nowrap=\"\">4:05 PM</td><td class=\"MT1\" nowrap=\"\">Basketball/NBA<br>(Game Money Line<br><b>2</b>@-245    [Win]</td><td colspan=\"2\"> </td></tr><tr><td colspan=\"3\"> </td></tr></tbody></table></span></td><td valign=\"top\" align=\"center\"><b> </b></td><td valign=\"top\" align=\"center\"> </td><td valign=\"top\" align=\"center\"> - </td><td valign=\"top\" align=\"right\"> 1,000.00 </td><td valign=\"top\" align=\"left\"> Win </td><td valign=\"top\" align=\"right\"> 991.60 </td></tr><tr style=\"color:Black;background-color:#E6E6E6;\">				<td align=\"left\" valign=\"top\">                                52010                            </td><td align=\"center\" valign=\"top\">                                3/24/2018<br>11:29:17 AM                            </td><td align=\"center\" valign=\"top\" style=\"width:400px;\">                                <a class='boldblueanchor'  href='BetDetail.aspx?ticketno=832044125&wagerno=1'>Ref No:&nbsp;832044125-1</a><br><b>Duke</b>&nbsp;-vs-&nbsp;Kansas<br>Handicap&nbsp;(Game)                            </td><td align=\"center\" valign=\"top\">                                <b>                                    Duke                                </b>                            </td><td align=\"center\" valign=\"top\">                                -2.5                            </td><td align=\"center\" valign=\"top\">                                -110                            </td><td align=\"right\" valign=\"top\">                                2,200.00                            </td><td align=\"right\" valign=\"top\">                                2,000.00                            </td><td align=\"left\" valign=\"top\">                                Pending                            </td>			</tr><tr style=\"color:Black;background-color:#E6E6E6;\">				<td align=\"left\" valign=\"top\">                                54005                            </td><td align=\"center\" valign=\"top\">                                3/24/2018<br>9:02:42 AM                            </td><td align=\"center\" valign=\"top\" style=\"width:400px;\">                                <a class='boldblueanchor'  href='BetDetail.aspx?ticketno=831974582&wagerno=1'>Ref No:&nbsp;831974582-1</a><br>Duke&nbsp;-vs-&nbsp;<b>Kansas</b><br>Handicap&nbsp;(1st Half)                            </td><td align=\"center\" valign=\"top\">                                <b>                                    Kansas                                </b>                            </td><td align=\"center\" valign=\"top\">                                +1.5                            </td><td align=\"center\" valign=\"top\">                                -108                            </td><td align=\"right\" valign=\"top\">                                1,620.00                            </td><td align=\"right\" valign=\"top\">                                1,500.00                            </td><td align=\"left\" valign=\"top\">                                Pending                            </td>			</tr><tr style=\"color:Black;background-color:#E6E6E6;\">				<td align=\"left\" valign=\"top\">                                52067                            </td><td align=\"center\" valign=\"top\">                                3/24/2018<br>7:44:22 AM                            </td><td align=\"center\" valign=\"top\" style=\"width:400px;\">                                <a class='boldblueanchor'  href='BetDetail.aspx?ticketno=831935732&wagerno=1'>Ref No:&nbsp;831935732-1</a><br>Duke&nbsp;-vs-&nbsp;Kansas<br>Over/Under&nbsp;(Game)                            </td><td align=\"center\" valign=\"top\">                                <b>                                    Under                                </b>                            </td><td align=\"center\" valign=\"top\">                                155.5                            </td><td align=\"center\" valign=\"top\">                                -113                            </td><td align=\"right\" valign=\"top\">                                565.00                            </td><td align=\"right\" valign=\"top\">                                500.00                            </td><td align=\"left\" valign=\"top\">                                Pending                            </td>			</tr><tr style=\"color:Black;background-color:#E6E6E6;\">				<td align=\"left\" valign=\"top\">                                52001                            </td><td align=\"center\" valign=\"top\">                                3/16/2018<br>8:54:15 PM                            </td><td align=\"center\" valign=\"top\" style=\"width:400px;\">                                <a class='boldblueanchor'  href='BetDetail.aspx?ticketno=829432059&wagerno=1'>Ref No:&nbsp;829432059-1</a><br>Sevilla to advance&nbsp;-vs-&nbsp;<b>Bayern Munchen to advance</b><br>Handicap&nbsp;(Match)                            </td><td align=\"center\" valign=\"top\">                                <b>                                    Bayern Munchen to advance                                </b>                            </td><td align=\"center\" valign=\"top\">                                0                            </td><td align=\"center\" valign=\"top\">                                -521                            </td><td align=\"right\" valign=\"top\">                                372.52                            </td><td align=\"right\" valign=\"top\">                                71.50                            </td><td align=\"left\" valign=\"top\">                                Pending                            </td>			</tr><tr style=\"color:Black;background-color:#E6E6E6;\">				<td align=\"left\" valign=\"top\">                                52001                            </td><td align=\"center\" valign=\"top\">                                3/16/2018<br>8:44:55 PM                            </td><td align=\"center\" valign=\"top\" style=\"width:400px;\">                                <a class='boldblueanchor'  href='BetDetail.aspx?ticketno=829430569&wagerno=1'>Ref No:&nbsp;829430569-1</a><br>RB Leipzig to advance&nbsp;-vs-&nbsp;<b>Marseille to advance</b><br>Handicap&nbsp;(Match)                            </td><td align=\"center\" valign=\"top\">                                <b>                                    Marseille to advance                                </b>                            </td><td align=\"center\" valign=\"top\">                                0                            </td><td align=\"center\" valign=\"top\">                                +101                            </td><td align=\"right\" valign=\"top\">                                110.45                            </td><td align=\"right\" valign=\"top\">                                111.55                            </td><td align=\"left\" valign=\"top\">                                Pending                            </td>			</tr><tr style=\"color:Black;background-color:#E6E6E6;\">				<td align=\"left\" valign=\"top\">                                52507                            </td><td align=\"center\" valign=\"top\">                                3/15/2018<br>3:44:25 PM                            </td><td align=\"center\" valign=\"top\" style=\"width:400px;\">                                <a class='boldblueanchor'  href='BetDetail.aspx?ticketno=829028998&wagerno=1'>Ref No:&nbsp;829028998-1</a><br>Michael Chiesa&nbsp;-vs-&nbsp;<b>Anthony Pettis</b><br>Money Line&nbsp;(Fight)                            </td><td align=\"center\" valign=\"top\">                                <b>                                    Anthony Pettis                                </b>                            </td><td align=\"center\" valign=\"top\">                                N/A                            </td><td align=\"center\" valign=\"top\">                                -120                            </td><td align=\"right\" valign=\"top\">                                240.00                            </td><td align=\"right\" valign=\"top\">                                200.00                            </td><td align=\"left\" valign=\"top\">                                Pending                            </td>			</tr><tr style=\"color:Black;background-color:#E6E6E6;\">				<td align=\"left\" valign=\"top\">                                52048                            </td><td align=\"center\" valign=\"top\">                                3/15/2018<br>3:43:20 PM                            </td><td align=\"center\" valign=\"top\" style=\"width:400px;\">                                <a class='boldblueanchor'  href='BetDetail.aspx?ticketno=829028639&wagerno=1'>Ref No:&nbsp;829028639-1</a><br>Michael Chiesa&nbsp;-vs-&nbsp;<b>Anthony Pettis</b><br>Money Line&nbsp;(Fight)                            </td><td align=\"center\" valign=\"top\">                                <b>                                    Anthony Pettis                                </b>                            </td><td align=\"center\" valign=\"top\">                                N/A                            </td><td align=\"center\" valign=\"top\">                                -118                            </td><td align=\"right\" valign=\"top\">                                442.50                            </td><td align=\"right\" valign=\"top\">                                375.00                            </td><td align=\"left\" valign=\"top\">                                Pending                            </td>			</tr><tr style=\"color:Black;background-color:#E6E6E6;\">				<td align=\"left\" valign=\"top\">                                52065                            </td><td align=\"center\" valign=\"top\">                                3/15/2018<br>9:12:01 AM                            </td><td align=\"center\" valign=\"top\" style=\"width:400px;\">                                <a class='boldblueanchor'  href='BetDetail.aspx?ticketno=828921387&wagerno=1'>Ref No:&nbsp;828921387-1</a><br>Basketball Props<br>NCAA<br>Midwest Region Winner<br>Team to win Midwest Region?                            </td><td align=\"center\" valign=\"top\">                                <b>                                    Kansas                                </b>                            </td><td align=\"center\" valign=\"top\">                                                            </td><td align=\"center\" valign=\"top\">                                +261                            </td><td align=\"right\" valign=\"top\">                                1,500.00                            </td><td align=\"right\" valign=\"top\">                                3,915.00                            </td><td align=\"left\" valign=\"top\">                                Pending                            </td>			</tr><tr style=\"color:Black;background-color:#E6E6E6;\">				<td align=\"left\" valign=\"top\">                                52065                            </td><td align=\"center\" valign=\"top\">                                3/15/2018<br>9:12:01 AM                            </td><td align=\"center\" valign=\"top\" style=\"width:400px;\">                                <a class='boldblueanchor'  href='BetDetail.aspx?ticketno=828921387&wagerno=2'>Ref No:&nbsp;828921387-2</a><br>Basketball Props<br>NCAA<br>Midwest Region Winner<br>Kansas to win Midwest Region?                            </td><td align=\"center\" valign=\"top\">                                <b>                                    Yes                                </b>                            </td><td align=\"center\" valign=\"top\">                                                            </td><td align=\"center\" valign=\"top\">                                +260                            </td><td align=\"right\" valign=\"top\">                                1,500.00                            </td><td align=\"right\" valign=\"top\">                                3,900.00                            </td><td align=\"left\" valign=\"top\">                                Pending                            </td>			</tr><tr style=\"color:Black;background-color:#E6E6E6;\">				<td align=\"left\" valign=\"top\">                                52065                            </td><td align=\"center\" valign=\"top\">                                3/15/2018<br>8:54:24 AM                            </td><td align=\"center\" valign=\"top\" style=\"width:400px;\">                                <a class='boldblueanchor'  href='BetDetail.aspx?ticketno=828918379&wagerno=1'>Ref No:&nbsp;828918379-1</a><br>Basketball Props<br>NCAA<br>Midwest Region Winner<br>Team to win Midwest Region?                            </td><td align=\"center\" valign=\"top\">                                <b>                                    Kansas                                </b>                            </td><td align=\"center\" valign=\"top\">                                                            </td><td align=\"center\" valign=\"top\">                                +267                            </td><td align=\"right\" valign=\"top\">                                1,500.00                            </td><td align=\"right\" valign=\"top\">                                4,005.00                            </td><td align=\"left\" valign=\"top\">                                Pending                            </td>			</tr><tr style=\"color:Black;background-color:#E6E6E6;\">				<td align=\"left\" valign=\"top\">                                52065                            </td><td align=\"center\" valign=\"top\">                                3/15/2018<br>8:52:04 AM                            </td><td align=\"center\" valign=\"top\" style=\"width:400px;\">                                <a class='boldblueanchor'  href='BetDetail.aspx?ticketno=828917494&wagerno=1'>Ref No:&nbsp;828917494-1</a><br>Basketball Props<br>NCAA<br>Midwest Region Winner<br>Kansas to win Midwest Region?                            </td><td align=\"center\" valign=\"top\">                                <b>                                    Yes                                </b>                            </td><td align=\"center\" valign=\"top\">                                                            </td><td align=\"center\" valign=\"top\">                                +274                            </td><td align=\"right\" valign=\"top\">                                1,500.00                            </td><td align=\"right\" valign=\"top\">                                4,110.00                            </td><td align=\"left\" valign=\"top\">                                Pending                            </td>			</tr><tr style=\"color:Black;background-color:#E6E6E6;\">				<td align=\"left\" valign=\"top\">                                52065                            </td><td align=\"center\" valign=\"top\">                                3/15/2018<br>8:49:39 AM                            </td><td align=\"center\" valign=\"top\" style=\"width:400px;\">                                <a class='boldblueanchor'  href='BetDetail.aspx?ticketno=828916540&wagerno=1'>Ref No:&nbsp;828916540-1</a><br>Basketball Props<br>NCAA<br>Midwest Region Winner<br>Kansas to win Midwest Region?                            </td><td align=\"center\" valign=\"top\">                                <b>                                    Yes                                </b>                            </td><td align=\"center\" valign=\"top\">                                                            </td><td align=\"center\" valign=\"top\">                                +289                            </td><td align=\"right\" valign=\"top\">                                1,500.00                            </td><td align=\"right\" valign=\"top\">                                4,335.00                            </td><td align=\"left\" valign=\"top\">                                Pending                            </td>			</tr><tr style=\"color:Black;background-color:#E6E6E6;\">				<td align=\"left\" valign=\"top\">                                52010                            </td><td align=\"center\" valign=\"top\">                                3/9/2018<br>11:04:11 AM                            </td><td align=\"center\" valign=\"top\" style=\"width:400px;\">                                <a class='boldblueanchor'  href='BetDetail.aspx?ticketno=826711093&wagerno=1'>Ref No:&nbsp;826711093-1</a><br>Baseball Props<br>MLB<br>Regular Season Wins<br>St. Louis Cardinals Regular Season Wins?                            </td><td align=\"center\" valign=\"top\">                                <b>                                    Over<br>85&nbsp;Wins                                </b>                            </td><td align=\"center\" valign=\"top\">                                                            </td><td align=\"center\" valign=\"top\">                                -135                            </td><td align=\"right\" valign=\"top\">                                675.00                            </td><td align=\"right\" valign=\"top\">                                500.00                            </td><td align=\"left\" valign=\"top\">                                Pending                            </td>			</tr><tr style=\"color:Black;background-color:#E6E6E6;\">				<td align=\"left\" valign=\"top\">                                52010                            </td><td align=\"center\" valign=\"top\">                                3/9/2018<br>10:57:09 AM                            </td><td align=\"center\" valign=\"top\" style=\"width:400px;\">                                <a class='boldblueanchor'  href='BetDetail.aspx?ticketno=826708695&wagerno=1'>Ref No:&nbsp;826708695-1</a><br>Baseball Props<br>MLB<br>Regular Season Wins<br>Washington Nationals Regular Season Wins?                            </td><td align=\"center\" valign=\"top\">                                <b>                                    Under<br>94.5&nbsp;Wins                                </b>                            </td><td align=\"center\" valign=\"top\">                                                            </td><td align=\"center\" valign=\"top\">                                -121                            </td><td align=\"right\" valign=\"top\">                                605.00                            </td><td align=\"right\" valign=\"top\">                                500.00                            </td><td align=\"left\" valign=\"top\">                                Pending                            </td>			</tr><tr style=\"color:Black;background-color:#E6E6E6;\">				<td align=\"left\" valign=\"top\">                                52010                            </td><td align=\"center\" valign=\"top\">                                3/9/2018<br>10:56:51 AM                            </td><td align=\"center\" valign=\"top\" style=\"width:400px;\">                                <a class='boldblueanchor'  href='BetDetail.aspx?ticketno=826708640&wagerno=1'>Ref No:&nbsp;826708640-1</a><br>Baseball Props<br>MLB<br>Regular Season Wins<br>Washington Nationals Regular Season Wins?                            </td><td align=\"center\" valign=\"top\">                                <b>                                    Under<br>94.5&nbsp;Wins                                </b>                            </td><td align=\"center\" valign=\"top\">                                                            </td><td align=\"center\" valign=\"top\">                                -113                            </td><td align=\"right\" valign=\"top\">                                565.00                            </td><td align=\"right\" valign=\"top\">                                500.00                            </td><td align=\"left\" valign=\"top\">                                Pending                            </td>			</tr><tr style=\"color:Black;background-color:#E6E6E6;\">				<td align=\"left\" valign=\"top\">                                52010                            </td><td align=\"center\" valign=\"top\">                                3/9/2018<br>10:52:02 AM                            </td><td align=\"center\" valign=\"top\" style=\"width:400px;\">                                <a class='boldblueanchor'  href='BetDetail.aspx?ticketno=826704606&wagerno=1'>Ref No:&nbsp;826704606-1</a><br>Baseball Props<br>MLB<br>Regular Season Wins<br>Philadelphia Phillies Regular Season Wins?                            </td><td align=\"center\" valign=\"top\">                                <b>                                    Over<br>76&nbsp;Wins                                </b>                            </td><td align=\"center\" valign=\"top\">                                                            </td><td align=\"center\" valign=\"top\">                                -137                            </td><td align=\"right\" valign=\"top\">                                685.00                            </td><td align=\"right\" valign=\"top\">                                500.00                            </td><td align=\"left\" valign=\"top\">                                Pending                            </td>			</tr><tr style=\"color:Black;background-color:#E6E6E6;\">				<td align=\"left\" valign=\"top\">                                52010                            </td><td align=\"center\" valign=\"top\">                                3/9/2018<br>10:51:33 AM                            </td><td align=\"center\" valign=\"top\" style=\"width:400px;\">                                <a class='boldblueanchor'  href='BetDetail.aspx?ticketno=826704169&wagerno=1'>Ref No:&nbsp;826704169-1</a><br>Baseball Props<br>MLB<br>Regular Season Wins<br>Philadelphia Phillies Regular Season Wins?                            </td><td align=\"center\" valign=\"top\">                                <b>                                    Over<br>76&nbsp;Wins                                </b>                            </td><td align=\"center\" valign=\"top\">                                                            </td><td align=\"center\" valign=\"top\">                                -127                            </td><td align=\"right\" valign=\"top\">                                635.00                            </td><td align=\"right\" valign=\"top\">                                500.00                            </td><td align=\"left\" valign=\"top\">                                Pending                            </td>			</tr><tr style=\"color:Black;background-color:#E6E6E6;\">				<td align=\"left\" valign=\"top\">                                52010                            </td><td align=\"center\" valign=\"top\">                                3/9/2018<br>10:45:30 AM                            </td><td align=\"center\" valign=\"top\" style=\"width:400px;\">                                <a class='boldblueanchor'  href='BetDetail.aspx?ticketno=826700151&wagerno=1'>Ref No:&nbsp;826700151-1</a><br>Baseball Props<br>MLB<br>Regular Season Wins<br>Colorado Rockies Regular Season Wins?                            </td><td align=\"center\" valign=\"top\">                                <b>                                    Under<br>81.5&nbsp;Wins                                </b>                            </td><td align=\"center\" valign=\"top\">                                                            </td><td align=\"center\" valign=\"top\">                                -102                            </td><td align=\"right\" valign=\"top\">                                510.00                            </td><td align=\"right\" valign=\"top\">                                500.00                            </td><td align=\"left\" valign=\"top\">                                Pending                            </td>			</tr><tr style=\"color:Black;background-color:#E6E6E6;\">				<td align=\"left\" valign=\"top\">                                52010                            </td><td align=\"center\" valign=\"top\">                                3/9/2018<br>10:24:26 AM                            </td><td align=\"center\" valign=\"top\" style=\"width:400px;\">                                <a class='boldblueanchor'  href='BetDetail.aspx?ticketno=826693131&wagerno=1'>Ref No:&nbsp;826693131-1</a><br>Baseball Props<br>MLB<br>Regular Season Wins<br>Miami Marlins Regular Season Wins?                            </td><td align=\"center\" valign=\"top\">                                <b>                                    Over<br>65&nbsp;Wins                                </b>                            </td><td align=\"center\" valign=\"top\">                                                            </td><td align=\"center\" valign=\"top\">                                +126                            </td><td align=\"right\" valign=\"top\">                                500.00                            </td><td align=\"right\" valign=\"top\">                                630.00                            </td><td align=\"left\" valign=\"top\">                                Pending                            </td>			</tr><tr style=\"color:Black;background-color:#E6E6E6;\">				<td align=\"left\" valign=\"top\">                                52010                            </td><td align=\"center\" valign=\"top\">                                3/9/2018<br>10:15:09 AM                            </td><td align=\"center\" valign=\"top\" style=\"width:400px;\">                                <a class='boldblueanchor'  href='BetDetail.aspx?ticketno=826690401&wagerno=1'>Ref No:&nbsp;826690401-1</a><br>Baseball Props<br>MLB<br>Regular Season Wins<br>Chicago White Sox Regular Season Wins?                            </td><td align=\"center\" valign=\"top\">                                <b>                                    Over<br>70.5&nbsp;Wins                                </b>                            </td><td align=\"center\" valign=\"top\">                                                            </td><td align=\"center\" valign=\"top\">                                +108                            </td><td align=\"right\" valign=\"top\">                                500.00                            </td><td align=\"right\" valign=\"top\">                                540.00                            </td><td align=\"left\" valign=\"top\">                                Pending                            </td>			</tr><tr style=\"color:Black;background-color:#E6E6E6;\">				<td align=\"left\" valign=\"top\">                                52010                            </td><td align=\"center\" valign=\"top\">                                3/9/2018<br>10:07:05 AM                            </td><td align=\"center\" valign=\"top\" style=\"width:400px;\">                                <a class='boldblueanchor'  href='BetDetail.aspx?ticketno=826688441&wagerno=1'>Ref No:&nbsp;826688441-1</a><br>Baseball Props<br>MLB<br>Regular Season Wins<br>Milwaukee Brewers Regular Season Wins?                            </td><td align=\"center\" valign=\"top\">                                <b>                                    Under<br>85&nbsp;Wins                                </b>                            </td><td align=\"center\" valign=\"top\">                                                            </td><td align=\"center\" valign=\"top\">                                -153                            </td><td align=\"right\" valign=\"top\">                                765.00                            </td><td align=\"right\" valign=\"top\">                                500.00                            </td><td align=\"left\" valign=\"top\">                                Pending                            </td>			</tr><tr style=\"color:Black;background-color:#E6E6E6;\">				<td align=\"left\" valign=\"top\">                                52010                            </td><td align=\"center\" valign=\"top\">                                3/9/2018<br>10:00:43 AM                            </td><td align=\"center\" valign=\"top\" style=\"width:400px;\">                                <a class='boldblueanchor'  href='BetDetail.aspx?ticketno=826686677&wagerno=1'>Ref No:&nbsp;826686677-1</a><br>Baseball Props<br>MLB<br>Regular Season Wins<br>Kansas City Royals Regular Season Wins?                            </td><td align=\"center\" valign=\"top\">                                <b>                                    Under<br>71.5&nbsp;Wins                                </b>                            </td><td align=\"center\" valign=\"top\">                                                            </td><td align=\"center\" valign=\"top\">                                -171                            </td><td align=\"right\" valign=\"top\">                                855.00                            </td><td align=\"right\" valign=\"top\">                                500.00                            </td><td align=\"left\" valign=\"top\">                                Pending                            </td>			</tr><tr style=\"color:Black;background-color:#E6E6E6;\">				<td align=\"left\" valign=\"top\">                                52001                            </td><td align=\"center\" valign=\"top\">                                3/8/2018<br>9:25:08 AM                            </td><td align=\"center\" valign=\"top\" style=\"width:400px;\">                                <a class='boldblueanchor'  href='BetDetail.aspx?ticketno=826351273&wagerno=1'>Ref No:&nbsp;826351273-1</a><br>Soccer Props<br>UEFA - Europa League<br>Outright<br>Europa League Winner                            </td><td align=\"center\" valign=\"top\">                                <b>                                    Dynamo Kiev                                </b>                            </td><td align=\"center\" valign=\"top\">                                                            </td><td align=\"center\" valign=\"top\">                                +8071                            </td><td align=\"right\" valign=\"top\">                                25.00                            </td><td align=\"right\" valign=\"top\">                                2,017.75                            </td><td align=\"left\" valign=\"top\">                                Pending                            </td>			</tr><tr style=\"color:Black;background-color:#E6E6E6;\">				<td align=\"left\" valign=\"top\">                                52048                            </td><td align=\"center\" valign=\"top\">                                3/7/2018<br>2:30:10 PM                            </td><td align=\"center\" valign=\"top\" style=\"width:400px;\">                                <a class='boldblueanchor'  href='BetDetail.aspx?ticketno=826161362&wagerno=1'>Ref No:&nbsp;826161362-1</a><br>Michael Chiesa&nbsp;-vs-&nbsp;<b>Anthony Pettis</b><br>Money Line&nbsp;(Fight)                            </td><td align=\"center\" valign=\"top\">                                <b>                                    Anthony Pettis                                </b>                            </td><td align=\"center\" valign=\"top\">                                N/A                            </td><td align=\"center\" valign=\"top\">                                -118                            </td><td align=\"right\" valign=\"top\">                                442.50                            </td><td align=\"right\" valign=\"top\">                                375.00                            </td><td align=\"left\" valign=\"top\">                                Pending                            </td>			</tr><tr style=\"color:Black;background-color:#E6E6E6;\">				<td align=\"left\" valign=\"top\">                                52048                            </td><td align=\"center\" valign=\"top\">                                3/7/2018<br>5:49:40 AM                            </td><td align=\"center\" valign=\"top\" style=\"width:400px;\">                                <a class='boldblueanchor'  href='BetDetail.aspx?ticketno=826005560&wagerno=1'>Ref No:&nbsp;826005560-1</a><br>Michael Chiesa&nbsp;-vs-&nbsp;<b>Anthony Pettis</b><br>Money Line&nbsp;(Fight)                            </td><td align=\"center\" valign=\"top\">                                <b>                                    Anthony Pettis                                </b>                            </td><td align=\"center\" valign=\"top\">                                N/A                            </td><td align=\"center\" valign=\"top\">                                -118                            </td><td align=\"right\" valign=\"top\">                                442.50                            </td><td align=\"right\" valign=\"top\">                                375.00                            </td><td align=\"left\" valign=\"top\">                                Pending                            </td>			</tr><tr style=\"color:Black;background-color:#E6E6E6;\">				<td align=\"left\" valign=\"top\">                                52048                            </td><td align=\"center\" valign=\"top\">                                3/6/2018<br>4:01:30 PM                            </td><td align=\"center\" valign=\"top\" style=\"width:400px;\">                                <a class='boldblueanchor'  href='BetDetail.aspx?ticketno=825847051&wagerno=1'>Ref No:&nbsp;825847051-1</a><br><b>Rose Namajunas</b>&nbsp;-vs-&nbsp;Joanna Jedrzejczyk<br>Money Line&nbsp;(Fight)                            </td><td align=\"center\" valign=\"top\">                                <b>                                    Rose Namajunas                                </b>                            </td><td align=\"center\" valign=\"top\">                                N/A                            </td><td align=\"center\" valign=\"top\">                                +126                            </td><td align=\"right\" valign=\"top\">                                500.00                            </td><td align=\"right\" valign=\"top\">                                630.00                            </td><td align=\"left\" valign=\"top\">                                Pending                            </td>			</tr><tr style=\"color:Black;background-color:#E6E6E6;\">				<td align=\"left\" valign=\"top\">                                52048                            </td><td align=\"center\" valign=\"top\">                                3/6/2018<br>4:00:36 PM                            </td><td align=\"center\" valign=\"top\" style=\"width:400px;\">                                <a class='boldblueanchor'  href='BetDetail.aspx?ticketno=825846935&wagerno=1'>Ref No:&nbsp;825846935-1</a><br><b>Rose Namajunas</b>&nbsp;-vs-&nbsp;Joanna Jedrzejczyk<br>Money Line&nbsp;(Fight)                            </td><td align=\"center\" valign=\"top\">                                <b>                                    Rose Namajunas                                </b>                            </td><td align=\"center\" valign=\"top\">                                N/A                            </td><td align=\"center\" valign=\"top\">                                +130                            </td><td align=\"right\" valign=\"top\">                                1,000.00                            </td><td align=\"right\" valign=\"top\">                                1,300.00                            </td><td align=\"left\" valign=\"top\">                                Pending                            </td>			</tr><tr style=\"color:Black;background-color:#E6E6E6;\">				<td align=\"left\" valign=\"top\">                                52048                            </td><td align=\"center\" valign=\"top\">                                3/6/2018<br>12:51:45 PM                            </td><td align=\"center\" valign=\"top\" style=\"width:400px;\">                                <a class='boldblueanchor'  href='BetDetail.aspx?ticketno=825780584&wagerno=1'>Ref No:&nbsp;825780584-1</a><br>Michael Chiesa&nbsp;-vs-&nbsp;<b>Anthony Pettis</b><br>Money Line&nbsp;(Fight)                            </td><td align=\"center\" valign=\"top\">                                <b>                                    Anthony Pettis                                </b>                            </td><td align=\"center\" valign=\"top\">                                N/A                            </td><td align=\"center\" valign=\"top\">                                -121                            </td><td align=\"right\" valign=\"top\">                                302.50                            </td><td align=\"right\" valign=\"top\">                                250.00                            </td><td align=\"left\" valign=\"top\">                                Pending                            </td>			</tr><tr style=\"color:Black;background-color:#E6E6E6;\">				<td align=\"left\" valign=\"top\">                                52048                            </td><td align=\"center\" valign=\"top\">                                3/6/2018<br>12:51:39 PM                            </td><td align=\"center\" valign=\"top\" style=\"width:400px;\">                                <a class='boldblueanchor'  href='BetDetail.aspx?ticketno=825780573&wagerno=1'>Ref No:&nbsp;825780573-1</a><br>Michael Chiesa&nbsp;-vs-&nbsp;<b>Anthony Pettis</b><br>Money Line&nbsp;(Fight)                            </td><td align=\"center\" valign=\"top\">                                <b>                                    Anthony Pettis                                </b>                            </td><td align=\"center\" valign=\"top\">                                N/A                            </td><td align=\"center\" valign=\"top\">                                -118                            </td><td align=\"right\" valign=\"top\">                                295.00                            </td><td align=\"right\" valign=\"top\">                                250.00                            </td><td align=\"left\" valign=\"top\">                                Pending                            </td>			</tr><tr style=\"color:Black;background-color:#E6E6E6;\">				<td align=\"left\" valign=\"top\">                                52048                            </td><td align=\"center\" valign=\"top\">                                3/4/2018<br>5:47:53 PM                            </td><td align=\"center\" valign=\"top\" style=\"width:400px;\">                                <a class='boldblueanchor'  href='BetDetail.aspx?ticketno=825265293&wagerno=1'>Ref No:&nbsp;825265293-1</a><br><b>Carlos Condit</b>&nbsp;-vs-&nbsp;Matt Brown<br>Money Line&nbsp;(Fight)                            </td><td align=\"center\" valign=\"top\">                                <b>                                    Carlos Condit                                </b>                            </td><td align=\"center\" valign=\"top\">                                N/A                            </td><td align=\"center\" valign=\"top\">                                -103                            </td><td align=\"right\" valign=\"top\">                                257.50                            </td><td align=\"right\" valign=\"top\">                                250.00                            </td><td align=\"left\" valign=\"top\">                                Pending                            </td>			</tr><tr style=\"color:Black;background-color:#E6E6E6;\">				<td align=\"left\" valign=\"top\">                                52048                            </td><td align=\"center\" valign=\"top\">                                3/4/2018<br>5:47:48 PM                            </td><td align=\"center\" valign=\"top\" style=\"width:400px;\">                                <a class='boldblueanchor'  href='BetDetail.aspx?ticketno=825265287&wagerno=1'>Ref No:&nbsp;825265287-1</a><br><b>Carlos Condit</b>&nbsp;-vs-&nbsp;Matt Brown<br>Money Line&nbsp;(Fight)                            </td><td align=\"center\" valign=\"top\">                                <b>                                    Carlos Condit                                </b>                            </td><td align=\"center\" valign=\"top\">                                N/A                            </td><td align=\"center\" valign=\"top\">                                -100                            </td><td align=\"right\" valign=\"top\">                                250.00                            </td><td align=\"right\" valign=\"top\">                                250.00                            </td><td align=\"left\" valign=\"top\">                                Pending                            </td>			</tr><tr style=\"color:Black;background-color:#E6E6E6;\">				<td align=\"left\" valign=\"top\">                                52048                            </td><td align=\"center\" valign=\"top\">                                2/5/2018<br>7:35:41 PM                            </td><td align=\"center\" valign=\"top\" style=\"width:400px;\">                                <a class='boldblueanchor'  href='BetDetail.aspx?ticketno=815289237&wagerno=1'>Ref No:&nbsp;815289237-1</a><br>Rose Namajunas&nbsp;-vs-&nbsp;<b>Joanna Jedrzejczyk</b><br>Money Line&nbsp;(Fight)                            </td><td align=\"center\" valign=\"top\">                                <b>                                    Joanna Jedrzejczyk                                </b>                            </td><td align=\"center\" valign=\"top\">                                N/A                            </td><td align=\"center\" valign=\"top\">                                -167                            </td><td align=\"right\" valign=\"top\">                                1,252.50                            </td><td align=\"right\" valign=\"top\">                                750.00                            </td><td align=\"left\" valign=\"top\">                                Pending                            </td>			</tr><tr style=\"color:Black;background-color:#E6E6E6;\">				<td align=\"left\" valign=\"top\">                                52048                            </td><td align=\"center\" valign=\"top\">                                2/5/2018<br>7:34:05 PM                            </td><td align=\"center\" valign=\"top\" style=\"width:400px;\">                                <a class='boldblueanchor'  href='BetDetail.aspx?ticketno=815288730&wagerno=1'>Ref No:&nbsp;815288730-1</a><br>Rose Namajunas&nbsp;-vs-&nbsp;<b>Joanna Jedrzejczyk</b><br>Money Line&nbsp;(Fight)                            </td><td align=\"center\" valign=\"top\">                                <b>                                    Joanna Jedrzejczyk                                </b>                            </td><td align=\"center\" valign=\"top\">                                N/A                            </td><td align=\"center\" valign=\"top\">                                -166                            </td><td align=\"right\" valign=\"top\">                                1,245.00                            </td><td align=\"right\" valign=\"top\">                                750.00                            </td><td align=\"left\" valign=\"top\">                                Pending                            </td>			</tr><tr style=\"color:Black;background-color:#E6E6E6;\">				<td align=\"left\" valign=\"top\">                                52010                            </td><td align=\"center\" valign=\"top\">                                10/18/2017<br>4:02:25 PM                            </td><td align=\"center\" valign=\"top\" style=\"width:400px;\">                                <a class='boldblueanchor'  href='BetDetail.aspx?ticketno=776211377&wagerno=1'>Ref No:&nbsp;776211377-1</a><br>Basketball Props<br>NBA<br>Regular Season Wins<br>Atlanta Hawks Regular Season Wins?                            </td><td align=\"center\" valign=\"top\">                                <b>                                    Over<br>25.5&nbsp;Regular Season Wins                                </b>                            </td><td align=\"center\" valign=\"top\">                                                            </td><td align=\"center\" valign=\"top\">                                -116                            </td><td align=\"right\" valign=\"top\">                                580.00                            </td><td align=\"right\" valign=\"top\">                                500.00                            </td><td align=\"left\" valign=\"top\">                                Pending                            </td>			</tr><tr style=\"color:Black;background-color:#E6E6E6;\">				<td align=\"left\" valign=\"top\">                                52010                            </td><td align=\"center\" valign=\"top\">                                10/17/2017<br>6:24:55 AM                            </td><td align=\"center\" valign=\"top\" style=\"width:400px;\">                                <a class='boldblueanchor'  href='BetDetail.aspx?ticketno=775652425&wagerno=1'>Ref No:&nbsp;775652425-1</a><br>Basketball Props<br>NBA<br>Regular Season Wins<br>Boston Celtics Regular Season Wins?                            </td><td align=\"center\" valign=\"top\">                                <b>                                    Under<br>55.5&nbsp;Regular Season Wins                                </b>                            </td><td align=\"center\" valign=\"top\">                                                            </td><td align=\"center\" valign=\"top\">                                -160                            </td><td align=\"right\" valign=\"top\">                                800.00                            </td><td align=\"right\" valign=\"top\">                                500.00                            </td><td align=\"left\" valign=\"top\">                                Pending                            </td>			</tr><tr style=\"color:Black;background-color:#E6E6E6;\">				<td align=\"left\" valign=\"top\">                                52010                            </td><td align=\"center\" valign=\"top\">                                10/17/2017<br>6:15:13 AM                            </td><td align=\"center\" valign=\"top\" style=\"width:400px;\">                                <a class='boldblueanchor'  href='BetDetail.aspx?ticketno=775650764&wagerno=1'>Ref No:&nbsp;775650764-1</a><br>Basketball Props<br>NBA<br>Regular Season Wins<br>Detroit Pistons Regular Season Wins?                            </td><td align=\"center\" valign=\"top\">                                <b>                                    Under<br>38.5&nbsp;Regular Season Wins                                </b>                            </td><td align=\"center\" valign=\"top\">                                                            </td><td align=\"center\" valign=\"top\">                                -130                            </td><td align=\"right\" valign=\"top\">                                650.00                            </td><td align=\"right\" valign=\"top\">                                500.00                            </td><td align=\"left\" valign=\"top\">                                Pending                            </td>			</tr><tr style=\"color:Black;background-color:#E6E6E6;\">				<td align=\"left\" valign=\"top\">                                52010                            </td><td align=\"center\" valign=\"top\">                                10/16/2017<br>10:17:19 PM                            </td><td align=\"center\" valign=\"top\" style=\"width:400px;\">                                <a class='boldblueanchor'  href='BetDetail.aspx?ticketno=775576398&wagerno=1'>Ref No:&nbsp;775576398-1</a><br>Basketball Props<br>NBA<br>Regular Season Wins<br>Minnesota Timberwolves Regular Season Wins?                            </td><td align=\"center\" valign=\"top\">                                <b>                                    Over<br>48.5&nbsp;Regular Season Wins                                </b>                            </td><td align=\"center\" valign=\"top\">                                                            </td><td align=\"center\" valign=\"top\">                                +122                            </td><td align=\"right\" valign=\"top\">                                500.00                            </td><td align=\"right\" valign=\"top\">                                610.00                            </td><td align=\"left\" valign=\"top\">                                Pending                            </td>			</tr><tr style=\"color:Black;background-color:#E6E6E6;\">				<td align=\"left\" valign=\"top\">                                52010                            </td><td align=\"center\" valign=\"top\">                                10/16/2017<br>9:53:51 PM                            </td><td align=\"center\" valign=\"top\" style=\"width:400px;\">                                <a class='boldblueanchor'  href='BetDetail.aspx?ticketno=775574573&wagerno=1'>Ref No:&nbsp;775574573-1</a><br>Basketball Props<br>NBA<br>Regular Season Wins<br>Detroit Pistons Regular Season Wins?                            </td><td align=\"center\" valign=\"top\">                                <b>                                    Under<br>38.5&nbsp;Regular Season Wins                                </b>                            </td><td align=\"center\" valign=\"top\">                                                            </td><td align=\"center\" valign=\"top\">                                -117                            </td><td align=\"right\" valign=\"top\">                                585.00                            </td><td align=\"right\" valign=\"top\">                                500.00                            </td><td align=\"left\" valign=\"top\">                                Pending                            </td>			</tr><tr style=\"color:Black;background-color:#E6E6E6;\">				<td align=\"left\" valign=\"top\">                                52010                            </td><td align=\"center\" valign=\"top\">                                10/16/2017<br>9:53:33 PM                            </td><td align=\"center\" valign=\"top\" style=\"width:400px;\">                                <a class='boldblueanchor'  href='BetDetail.aspx?ticketno=775574556&wagerno=1'>Ref No:&nbsp;775574556-1</a><br>Basketball Props<br>NBA<br>Regular Season Wins<br>Detroit Pistons Regular Season Wins?                            </td><td align=\"center\" valign=\"top\">                                <b>                                    Under<br>38.5&nbsp;Regular Season Wins                                </b>                            </td><td align=\"center\" valign=\"top\">                                                            </td><td align=\"center\" valign=\"top\">                                -110                            </td><td align=\"right\" valign=\"top\">                                550.00                            </td><td align=\"right\" valign=\"top\">                                500.00                            </td><td align=\"left\" valign=\"top\">                                Pending                            </td>			</tr><tr style=\"color:Black;background-color:#E6E6E6;\">				<td align=\"left\" valign=\"top\">                                52010                            </td><td align=\"center\" valign=\"top\">                                10/16/2017<br>9:42:25 PM                            </td><td align=\"center\" valign=\"top\" style=\"width:400px;\">                                <a class='boldblueanchor'  href='BetDetail.aspx?ticketno=775574094&wagerno=1'>Ref No:&nbsp;775574094-1</a><br>Basketball Props<br>NBA<br>Regular Season Wins<br>Los Angeles Lakers Regular Season Wins?                            </td><td align=\"center\" valign=\"top\">                                <b>                                    Under<br>33.5&nbsp;Regular Season Wins                                </b>                            </td><td align=\"center\" valign=\"top\">                                                            </td><td align=\"center\" valign=\"top\">                                -138                            </td><td align=\"right\" valign=\"top\">                                690.00                            </td><td align=\"right\" valign=\"top\">                                500.00                            </td><td align=\"left\" valign=\"top\">                                Pending                            </td>			</tr><tr style=\"color:Black;background-color:#E6E6E6;\">				<td align=\"left\" valign=\"top\">                                52010                            </td><td align=\"center\" valign=\"top\">                                10/16/2017<br>9:42:03 PM                            </td><td align=\"center\" valign=\"top\" style=\"width:400px;\">                                <a class='boldblueanchor'  href='BetDetail.aspx?ticketno=775574082&wagerno=1'>Ref No:&nbsp;775574082-1</a><br>Basketball Props<br>NBA<br>Regular Season Wins<br>Los Angeles Lakers Regular Season Wins?                            </td><td align=\"center\" valign=\"top\">                                <b>                                    Under<br>33.5&nbsp;Regular Season Wins                                </b>                            </td><td align=\"center\" valign=\"top\">                                                            </td><td align=\"center\" valign=\"top\">                                -130                            </td><td align=\"right\" valign=\"top\">                                650.00                            </td><td align=\"right\" valign=\"top\">                                500.00                            </td><td align=\"left\" valign=\"top\">                                Pending                            </td>			</tr><tr align=\"right\" style=\"color:White;background-color:#303498;\">				<td align=\"left\" valign=\"top\">                                <span id=\"TBW_GV__ctl43_F1\">Totals</span>                            </td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>                                30,362.97                            </td><td>                                41,500.80                            </td><td>&nbsp;</td>			</tr>		</table>	</div>            </p>        </div>    <script type=\"text/javascript\"><!--var Page_Validators =  new Array(document.getElementById(\"DRSUC_STBRV\"), document.getElementById(\"DRSUC_ETBRV\"), document.getElementById(\"DRSUC_ETBCV\"));// --></script><script type=\"text/javascript\"><!--var STBID = \"DRSUC_STB\"; var _yearButtonsVisible = \"True\"; var ETBID = \"DRSUC_ETB\"; var _minYear = 2014; var _minMonth = 2; var _minDay = 25; var _maxYear = 2018; var _maxMonth = 2; var _maxDay = 26; var _monthNames=new Array(\"January\", \"February\", \"March\", \"April\", \"May\", \"June\", \"July\", \"August\", \"September\", \"October\", \"November\", \"December\"); var _rightToLeft=false; var _shortDatePattern=\"M/d/yyyy\"; var _calendarCallbackFunction=\"CalendarCallback\"; var Page_ValidationActive = false;if (typeof(ValidatorOnLoad) == \"function\") {    ValidatorOnLoad();}function ValidatorOnSubmit() {    if (Page_ValidationActive) {        return ValidatorCommonOnSubmit();    }    else {        return true;    }}        // --></script></form></body></html>";
			String xhtml = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><link rel=\"stylesheet\" type=\"text/css\" href=\"Main.css\" /><title>	Bets History</title>    <script type=\"text/javascript\" language=\"javascript\">        function show(row)        {		        var hid=document.getElementById('show'+row);		        if(hid)	        {		        if (hid.style.display==\"none\")		        {			        hid.style.display=\"\";		        }		        else		        {		        hid.style.display=\"none\";		        }	        }        }                function OnDropDownChange(dropDownName)        {            var ddlGames=document.getElementById('ddlGames');	            var ddlContests=document.getElementById('ddlContests');            var ddlRaces=document.getElementById('ddlRaces');                         switch(dropDownName)            {                case \"ddlGames\":                    ddlContests.selectedIndex = 0;                    ddlRaces.selectedIndex = 0;                    break;                case \"ddlContest\":                    ddlGames.selectedIndex = 0;                    ddlRaces.selectedIndex = 0;                    break;                                    case \"ddlRaces\":                    ddlGames.selectedIndex = 0;                    ddlContests.selectedIndex = 0;                    break;                                }        }                function disableControls(dropDownName)        {             var frm = document.forms[0];                         switch(dropDownName)            {                case \"ddlAgentGroup\":                    frm.ddlStatus.disabled = true;                                        break;                case \"ddlStatus\":                    frm.ddlAgentGroup.disabled = true;                    break;                                               }            frm.ddlSortDirection.disabled = true;            frm.ddlCust.disabled = true;            frm.ddlBetType.disabled = true;            frm.DRSUC_SB.disabled = true;            frm.DRSUC_STB.disabled = true;            frm.DRSUC_ETB.disabled = true;                        frm.DRSUC_btnOneDay.disabled = true;            frm.DRSUC_btnSevenDays.disabled = true;            frm.DRSUC_btnFourteenDays.disabled = true;                        if (frm.DRSUC_btnAll!=null)                frm.DRSUC_btnAll.disabled = true;            if (frm.btnGotoPage != null)                frm.btnGotoPage.disabled = true;            if (frm.txtGotoPage != null)                frm.txtGotoPage.disabled = true;	                        if (frm.btnFirst != null)                frm.btnFirst.disabled = true;	            if (frm.btnPrevious!=null)                frm.btnPrevious.disabled = true;	            if (frm.btnNext != null)                frm.btnNext.disabled = true;            if (frm.btnLast != null)                frm.btnLast.disabled = true;                                                   }                function ShowWagerTypeCombo()		{		    var ddlBt=document.getElementById('ddlBetType');	    	    var vGames = document.getElementById(\"divGames\");     	    var vContests = document.getElementById(\"divContests\");     	    var vRaces = document.getElementById(\"divRaces\");     	        	    switch(ddlBt.options[ddlBt.selectedIndex].value)    	    {      	        case \"\":    	            vGames.style.display = 'none';    	            vContests.style.display = 'none';    	            vRaces.style.display = 'none';    	            break;          	        case \"G\":    	            vGames.style.display = '';                	            vContests.style.display = 'none';    	            vRaces.style.display = 'none';    	            break;          	        case \"C\":    	            vContests.style.display = '';    	            vGames.style.display = 'none';    	            vRaces.style.display = 'none';    	            break;      	        case \"R\":    	            vRaces.style.display = '';    	            vGames.style.display = 'none';    	            vContests.style.display = 'none';    	            break;       	                	                      	    }		}    </script>    <link href=\"App_Themes/BetsHistory/BetsHistory.css\" type=\"text/css\" rel=\"stylesheet\" /></head><body onload='ShowWagerTypeCombo()'>    <script type=\"text/javascript\" src=\"/Scripts/JQuery/jquery-1.6.2.js\"></script>    <script type=\"text/javascript\" >        $(document).ready(function () {            $(\"#txtGotoPage\").keydown(function (event) {                // Allow only backspace and delete                if (event.keyCode == 46 || event.keyCode == 8) {                    // let it happen, don't do anything                }                else {                    // Ensure that it is a number and stop the keypress                    if (event.keyCode < 48 || event.keyCode > 57) {                        event.preventDefault();                    }                }            });        })    </script>    <form name=\"form1\" method=\"post\" action=\"./BetsHistory.aspx\" language=\"javascript\" onsubmit=\"javascript:return WebForm_OnSubmit();\" id=\"form1\"><input type=\"hidden\" name=\"__EVENTTARGET\" id=\"__EVENTTARGET\" value=\"\" /><input type=\"hidden\" name=\"__EVENTARGUMENT\" id=\"__EVENTARGUMENT\" value=\"\" /><input type=\"hidden\" name=\"__LASTFOCUS\" id=\"__LASTFOCUS\" value=\"\" /><input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"/wEPDwUJODYzMjEzMzczD2QWAgIBD2QWBAIBD2QWCAIDDw8WAh4EVGV4dAUQTG9nZ2VkIGluOiBNc3RycGRkAgsPDxYCHwAFClcvTCBUb3RhbHNkZAIXDw8WAh4HVmlzaWJsZWhkZAIZDw8WAh8BaGRkAgMPZBYUAgMPEA8WBh4ORGF0YVZhbHVlRmllbGQFCUFnZW50Q29kZR4NRGF0YVRleHRGaWVsZAUJQWdlbnRDb2RlHgtfIURhdGFCb3VuZGcWAh4Ib25jaGFuZ2UFK2phdmFzY3JpcHQ6ZGlzYWJsZUNvbnRyb2xzKCdkZGxBZ2VudEdyb3VwJykQFQgFeHBpa2UGeHBpa2UxBnhQaWtlMQZ4UGlrZTIGWFBJS0UzBlhQSUtFNAZ4UElLRTUGeHBpa2U2FQgFeHBpa2UGeHBpa2UxBnhQaWtlMQZ4UGlrZTIGWFBJS0UzBlhQSUtFNAZ4UElLRTUGeHBpa2U2FCsDCGdnZ2dnZ2dnFgFmZAIHDxAPZBYCHwUFJ2phdmFzY3JpcHQ6ZGlzYWJsZUNvbnRyb2xzKCdkZGxTdGF0dXMnKWQWAQICZAIJD2QWDAIEDw8WAh8ABQExZGQCBg8PFgIfAAUBMWRkAggPDxYCHgdFbmFibGVkaGRkAgkPDxYCHwZoZGQCCg8PFgIfBmhkZAILDw8WAh8GaGRkAg0PEA8WAh8EZ2QQFQgABTUyMDAxBTUyMDEwBTUyMDQ4BTUyMDY1BTUyMDY3BTUyNTA3BTU0MDA1FQgABTUyMDAxBTUyMDEwBTUyMDQ4BTUyMDY1BTUyMDY3BTUyNTA3BTU0MDA1FCsDCGdnZ2dnZ2dnFgFmZAIRDxAPZBYCHwUFH2phdmFzY3JpcHQ6U2hvd1dhZ2VyVHlwZUNvbWJvKCkQFQMABUdhbWVzCENvbnRlc3RzFQMAAUcBQxQrAwNnZ2cWAWZkAhUPEGRkFgECAWQCHQ8QDxYGHwIFBVZhbHVlHwMFBFRleHQfBGcWAh8FBSdqYXZhc2NyaXB0Ok9uRHJvcERvd25DaGFuZ2UoJ2RkbEdhbWVzJykQFQcAG0NhcmxvcyBDb25kaXQtdnMtTWF0dCBCcm93bg5EdWtlLXZzLUthbnNhcyBNaWNoYWVsIENoaWVzYS12cy1BbnRob255IFBldHRpcy1SQiBMZWlwemlnIHRvIGFkdmFuY2UtdnMtTWFyc2VpbGxlIHRvIGFkdmFuY2UkUm9zZSBOYW1hanVuYXMtdnMtSm9hbm5hIEplZHJ6ZWpjenlrL1NldmlsbGEgdG8gYWR2YW5jZS12cy1CYXllcm4gTXVuY2hlbiB0byBhZHZhbmNlFQcACTgyNDg4MTAxOAk4MzE4NjQ2NzkJODIyMjEwNjEwCTgyOTE5ODMwMgk4MTA0NzkzNjcJODI5MTc5OTY0FCsDB2dnZ2dnZ2dkZAIhDxAPFgYfAgUFVmFsdWUfAwUEVGV4dB8EZxYCHwUFKmphdmFzY3JpcHQ6T25Ecm9wRG93bkNoYW5nZSgnZGRsQ29udGVzdHMnKRAVEQBNQmFzZWJhbGwgUHJvcHN8TUxCfFJlZ3VsYXIgU2Vhc29uIFdpbnN8Q2hpY2FnbyBXaGl0ZSBTb3ggUmVndWxhciBTZWFzb24gV2lucz9MQmFzZWJhbGwgUHJvcHN8TUxCfFJlZ3VsYXIgU2Vhc29uIFdpbnN8Q29sb3JhZG8gUm9ja2llcyBSZWd1bGFyIFNlYXNvbiBXaW5zP05CYXNlYmFsbCBQcm9wc3xNTEJ8UmVndWxhciBTZWFzb24gV2luc3xLYW5zYXMgQ2l0eSBSb3lhbHMgUmVndWxhciBTZWFzb24gV2lucz9JQmFzZWJhbGwgUHJvcHN8TUxCfFJlZ3VsYXIgU2Vhc29uIFdpbnN8TWlhbWkgTWFybGlucyBSZWd1bGFyIFNlYXNvbiBXaW5zP01CYXNlYmFsbCBQcm9wc3xNTEJ8UmVndWxhciBTZWFzb24gV2luc3xNaWx3YXVrZWUgQnJld2VycyBSZWd1bGFyIFNlYXNvbiBXaW5zP1FCYXNlYmFsbCBQcm9wc3xNTEJ8UmVndWxhciBTZWFzb24gV2luc3xQaGlsYWRlbHBoaWEgUGhpbGxpZXMgUmVndWxhciBTZWFzb24gV2lucz9PQmFzZWJhbGwgUHJvcHN8TUxCfFJlZ3VsYXIgU2Vhc29uIFdpbnN8U3QuIExvdWlzIENhcmRpbmFscyBSZWd1bGFyIFNlYXNvbiBXaW5zP1BCYXNlYmFsbCBQcm9wc3xNTEJ8UmVndWxhciBTZWFzb24gV2luc3xXYXNoaW5ndG9uIE5hdGlvbmFscyBSZWd1bGFyIFNlYXNvbiBXaW5zP0tCYXNrZXRiYWxsIFByb3BzfE5CQXxSZWd1bGFyIFNlYXNvbiBXaW5zfEF0bGFudGEgSGF3a3MgUmVndWxhciBTZWFzb24gV2lucz9MQmFza2V0YmFsbCBQcm9wc3xOQkF8UmVndWxhciBTZWFzb24gV2luc3xCb3N0b24gQ2VsdGljcyBSZWd1bGFyIFNlYXNvbiBXaW5zP01CYXNrZXRiYWxsIFByb3BzfE5CQXxSZWd1bGFyIFNlYXNvbiBXaW5zfERldHJvaXQgUGlzdG9ucyBSZWd1bGFyIFNlYXNvbiBXaW5zP1BCYXNrZXRiYWxsIFByb3BzfE5CQXxSZWd1bGFyIFNlYXNvbiBXaW5zfExvcyBBbmdlbGVzIExha2VycyBSZWd1bGFyIFNlYXNvbiBXaW5zP1RCYXNrZXRiYWxsIFByb3BzfE5CQXxSZWd1bGFyIFNlYXNvbiBXaW5zfE1pbm5lc290YSBUaW1iZXJ3b2x2ZXMgUmVndWxhciBTZWFzb24gV2lucz9JQmFza2V0YmFsbCBQcm9wc3xOQ0FBfE1pZHdlc3QgUmVnaW9uIFdpbm5lcnxLYW5zYXMgdG8gd2luIE1pZHdlc3QgUmVnaW9uP0dCYXNrZXRiYWxsIFByb3BzfE5DQUF8TWlkd2VzdCBSZWdpb24gV2lubmVyfFRlYW0gdG8gd2luIE1pZHdlc3QgUmVnaW9uPz9Tb2NjZXIgUHJvcHN8VUVGQSAtIEV1cm9wYSBMZWFndWV8T3V0cmlnaHR8RXVyb3BhIExlYWd1ZSBXaW5uZXIVEQAJODE4ODE0NzAzCTgxODgxNDc5OQk4MTg4MTUwMDIJODE4ODE1MDg5CTgxODgxNTEzNQk4MTg4MTUzMjIJODE4ODE1NjAzCTgxODgxNTc3Ngk3MzM4OTg4NjQJNzMzODk4OTczCTczMzg5OTQ0NAk3MzM5MDEwMDMJNzMzOTAxMjM2CTgyNzg2MzUwNwk4Mjc4NjI4MDUJNzk1OTUyODczFCsDEWdnZ2dnZ2dnZ2dnZ2dnZ2dnZGQCJQ8QDxYGHwIFBVZhbHVlHwMFBFRleHQfBGdkEBUBABUBABQrAwFnZGQCJw9kFgQCAg8PFgQeDE1heGltdW1WYWx1ZQUJMy8yNi8yMDE4HgxNaW5pbXVtVmFsdWUFCTMvMjUvMjAxNGRkAgUPDxYEHwcFCTMvMjYvMjAxOB8IBQkzLzI1LzIwMTRkZBgBBQZUQldfR1YPPCsADAEIAgFk05rS5DXhSyMin4e4VSg+H3rpNPOttYopabl5dJnqUtw=\" /><script type=\"text/javascript\"><!--var theForm = document.forms['form1'];if (!theForm) {    theForm = document.form1;}function __doPostBack(eventTarget, eventArgument) {    if (!theForm.onsubmit || (theForm.onsubmit() != false)) {        theForm.__EVENTTARGET.value = eventTarget;        theForm.__EVENTARGUMENT.value = eventArgument;        theForm.submit();    }}// --></script><script src=\"/WebResource.axd?d=S4Sz6Z0QgCp_DS-FYfAGYG5oj0yoPqfPyvuNzQZPdCululyt_4MASq6NwQwigXzmrDMJeSxcr076mM5G4YadzquTcqS9JxrFXFg4bY1pZ9U1&amp;t=636161530540000000\" type=\"text/javascript\"></script><script src=\"/WebResource.axd?d=YZQgc39B7EmGHSOvt7SLj06AsOOLyKxRw3tb6x_0__m92wevvvZv8MEov4-ml-B5G0Rr_t6wQQWEI120a2udGnOtdlaPP2DW_D28ktjwcVM1&amp;t=636161530540000000\" type=\"text/javascript\"></script><script type=\"text/javascript\"><!--function WebForm_OnSubmit() {if (typeof(ValidatorOnSubmit) == \"function\" && ValidatorOnSubmit() == false) return false;return true;}// --></script><input type=\"hidden\" name=\"__VIEWSTATEGENERATOR\" id=\"__VIEWSTATEGENERATOR\" value=\"9997B33F\" /><input type=\"hidden\" name=\"__EVENTVALIDATION\" id=\"__EVENTVALIDATION\" value=\"/wEdAENm6UaKMbUXBk31WW8ECufhyLlAjqg4XtI41Mw6vK9uEHJLoK5U+nS46oQ4JjecaURYdsfy82JuWBEC+ABqTox7soH0vKtS8BdBgK5WTDoP10dNC3y3BYtuAw1gvR9JZUfO6dD8Zv3WlPrERIWAxkf/uszP5LxCTuKnj7iG8+Ev5+H6O9DyMyEXxoYss/aTh29PStedrsEvyMVJnODXztiqp1uOtL2fovFZjtn/2twD1dJ4dyDDtyrjxyFIaHIsTa47YxMCLQXTALvpj6barERLEh+F0sM036YEiscgG3GIMMxR7TN+BPh9bd/6fcCTh4cR6yND9+hJjabz3qZgOJAsS6CKxq4NO1oGHpr0o1MHZzBr9jssiwIaSQOAWpmE3UyBY6SC6BYHkKr11gPu2hA0A3sKKRfJWOelyk1KBhWs60EkX3mmUGnkw7Dq7iIBd1OpzscnQIEBcZMjSpsbrorr4yqIycC1H6Gwav8VRF2tX8p4yNBtabBcg/CuzD9ykTiXRvCxwKNvDtguCknaYpaSj23/wnbfnDncp+CLKcf7sGmQcGabN58NtumQ8D3fUD/dX+DlNR3MNFt68lLIfl/2XIoVtmI5Gw8J0MbwFAsyj9NhrdFWl2O0tDV+kbesK3xnvT1oH7VlUSvwc5gb627vsP+XHEwYHnOewS4BqrEfMklyPjjsbBsWeb9KFx2rI+LsiKGORarcSfR+vVUmljYzFa1xhAV30rpAYVQQ97/aqEJ3UjMhcc/tkO+7+WtMVWToxxoIze9WQMl6Y1e3vGF0b9aCpH+9sHRUKXZofI9pk2Ce7sPDdJpEPVyCZ2Z6xYu/JBbghrYiJyP7yjA7Ee2gyuTf+c3L+WXgRaRWFSVUpl6tIpj+KFdiIqW7v1pbAQZuKkhatYTlf7KRzYC7KF8cfNftM92qVyTjDRDaoRbueKExVlkddgFx9TFzHOFXjb9vdmfM7EpMYk+x+HGUkPx3Sfhr7ERvu/5QUsyesaBvma+uwWYEFqstl5op90nQ3TjUR5vF+MYoAAHz0vBjE+mQhiRdY6iKNkMWK5cfdXcXljaYkrDRpzYfe3Yk5akA7iEihA7DkZC+G+WwwLVSDCUcG5NjI8yPmTb//x/wRNc3yEbp+xuciKiu9DM0LRoKJJFKn++M56vqHnnjxMy+ljzHM3ZQnLtAoKQH+C7xgf3B0tCDd18lAIHA2ktBcRxTe5nGj9ADm+W4PwQxgKzg0yc2ziJDSkLhcHw844jOTYlTJtmCG49/N7HGjn0pU0rnKM0+uH7anNdAANlPAyLgjF80SUKAs1UQ9BgYsJJGyGyyFC8jXQC/NgExGWriK2TV9KZ9vzmh7slm12A7sjPb7jRGeVsCwq9P1hrj3jTm2LM3uDaW/O0TROqK4FNO+CSp9R1Dttz5dX4QD7VzZ7AhK7tLJpEjcj3qIbBA5POm4ImSqIvKw+pxrdW3VIplzR0qVhg=\" />    <div id=\"header\">       <table width=\"100%\">        <tr>            <td style=\"width: 20%; font-size: 1em; vertical-align:bottom;\">            </td>            <td style=\"text-align:center;\">                <img id=\"PaHeader1_Image1\" src=\"Images/Header.gif\" align=\"absmiddle\" border=\"0\" />            </td>             <td style=\"width: 20%;  text-align:right; font-size: .8em; vertical-align:bottom;\">                <span id=\"PaHeader1_loggedIn\" style=\"color:White;\">Logged in: Mstrp</span>            </td>        </tr>    </table>               </div><div id=\"menu\">    <table>    <tr>    <td width=\"100%\">	    <a id=\"PaHeader1_hlHome\" class=\"MenuItem\" href=\"Default.aspx\">Home</a>	    <a id=\"PaHeader1_hlLogout\" class=\"MenuItem\" href=\"AgentLogin.aspx\">Logout</a>	    <a id=\"PaHeader1_hlBankingFees\" class=\"MenuItem\" href=\"paBankingFees.aspx\">Banking Fees</a>	    <a id=\"PaHeader1_hlWL\" class=\"MenuItem\" href=\"paDailyFigTotals.aspx\">W/L Totals</a>	    <a id=\"PaHeader1_hlCasinoWL\" class=\"MenuItem\" href=\"paCasinoWinLoss.aspx\">Casino Win/Loss</a>	    <a id=\"PaHeader1_hlCommission\" class=\"MenuItem\" href=\"paCommission.aspx\">Commission</a>	    <a id=\"PaHeader1_hlAgentDetails\" class=\"MenuItem\" href=\"paAgent.aspx\">Agent Details</a>	    <a id=\"PaHeader1_hlSummary\" class=\"MenuItem\" href=\"paAgentSummary.aspx\">Summary</a>	    <a id=\"PaHeader1_hlAllBalance\" class=\"MenuItem\" href=\"paAllBalance.aspx\">All Balance</a>	    	    	    <a id=\"PaHeader1_hlExposure\" class=\"MenuItem\" href=\"AAPRReport.aspx\">Exposure</a>	    <a id=\"PaHeader1_hlBetsHistory\" class=\"MenuItem\" href=\"BetsHistory.aspx\">Bets History</a>        <a id=\"PaHeader1_hlChangePassword\" class=\"MenuItem\" href=\"ChangePassword.aspx\">Change Password</a>    </td>    </tr>    </table></div>        <div style=\"height:100%;width:100%;\">	                        <table width=\"100%\">                <tr>                <td>                                <span id=\"lblAgentGroup\">AgentID:</span>                <select name=\"ddlAgentGroup\" onchange=\"javascript:disableControls(&#39;ddlAgentGroup&#39;);setTimeout(&#39;__doPostBack(\\&#39;ddlAgentGroup\\&#39;,\\&#39;\\&#39;)&#39;, 0)\" language=\"javascript\" id=\"ddlAgentGroup\">		<option selected=\"selected\" value=\"xpike\">xpike</option>		<option value=\"xpike1\">xpike1</option>		<option value=\"xPike1\">xPike1</option>		<option value=\"xPike2\">xPike2</option>		<option value=\"XPIKE3\">XPIKE3</option>		<option value=\"XPIKE4\">XPIKE4</option>		<option value=\"xPIKE5\">xPIKE5</option>		<option value=\"xpike6\">xpike6</option>	</select>                <span id=\"lblStatus\">Status:</span>                <select name=\"ddlStatus\" onchange=\"javascript:disableControls(&#39;ddlStatus&#39;);setTimeout(&#39;__doPostBack(\\&#39;ddlStatus\\&#39;,\\&#39;\\&#39;)&#39;, 0)\" language=\"javascript\" id=\"ddlStatus\">		<option value=\"X\">Cancelled</option>		<option value=\"G\">Graded</option>		<option selected=\"selected\" value=\"P\">Pending</option>	</select>                                </td>                                <td style=\"text-align: left;\">                    <input type=\"submit\" name=\"btnGotoPage\" value=\"Go to Page\" id=\"btnGotoPage\" /><input name=\"txtGotoPage\" type=\"text\" value=\"1\" maxlength=\"4\" id=\"txtGotoPage\" style=\"width:3em;\" />                        &nbsp;                        <span id=\"lblPage\"> Page </span><span id=\"lblCurrentPage\">1</span><span id=\"lblOf\"> of </span><span id=\"lblTotalPages\">1</span>                        &nbsp;                                                <input type=\"submit\" name=\"btnFirst\" value=\"First\" id=\"btnFirst\" disabled=\"disabled\" /><input type=\"submit\" name=\"btnPrevious\" value=\"Prev\" id=\"btnPrevious\" disabled=\"disabled\" /><input type=\"submit\" name=\"btnNext\" value=\"Next\" id=\"btnNext\" disabled=\"disabled\" /><input type=\"submit\" name=\"btnLast\" value=\"Last\" id=\"btnLast\" disabled=\"disabled\" />                </td>                           </tr>                        <tr>                <td>                         <span id=\"lblCust\">Cust:</span>                <select name=\"ddlCust\" onchange=\"javascript:setTimeout(&#39;__doPostBack(\\&#39;ddlCust\\&#39;,\\&#39;\\&#39;)&#39;, 0)\" language=\"javascript\" id=\"ddlCust\">		<option selected=\"selected\" value=\"\"></option>		<option value=\"52001\">52001</option>		<option value=\"52010\">52010</option>		<option value=\"52048\">52048</option>		<option value=\"52065\">52065</option>		<option value=\"52067\">52067</option>		<option value=\"52507\">52507</option>		<option value=\"54005\">54005</option>	</select>                <span id=\"lblBetType\">Type:</span>                <select name=\"ddlBetType\" onchange=\"javascript:ShowWagerTypeCombo();setTimeout(&#39;__doPostBack(\\&#39;ddlBetType\\&#39;,\\&#39;\\&#39;)&#39;, 0)\" language=\"javascript\" id=\"ddlBetType\">		<option selected=\"selected\" value=\"\"></option>		<option value=\"G\">Games</option>		<option value=\"C\">Contests</option>	</select>                       <span id=\"lblSort\">Sort:</span>                <select name=\"ddlSortDirection\" onchange=\"javascript:setTimeout(&#39;__doPostBack(\\&#39;ddlSortDirection\\&#39;,\\&#39;\\&#39;)&#39;, 0)\" language=\"javascript\" id=\"ddlSortDirection\">		<option value=\"A\">Date Ascending</option>		<option selected=\"selected\" value=\"D\">Date Descending</option>	</select>                                    <span id=\"lblTicket\">Ticket:</span>                    <input name=\"txtTicket\" type=\"text\" id=\"txtTicket\" onkeydown=\"return (!((event.keyCode>=65 &amp;&amp; event.keyCode &lt;= 95) || event.keyCode >= 106) &amp;&amp; event.keyCode!=32);\" style=\"width:120px;\" />            </td>            </tr>                <tr>            <td colspan=\"2\">            <div id=\"divGames\" style=\"DISPLAY: none\">                                <span id=\"lblGames\">Games:</span>                <select name=\"ddlGames\" id=\"ddlGames\" onchange=\"javascript:OnDropDownChange(&#39;ddlGames&#39;)\">		<option value=\"\"></option>		<option value=\"824881018\">Carlos Condit-vs-Matt Brown</option>		<option value=\"831864679\">Duke-vs-Kansas</option>		<option value=\"822210610\">Michael Chiesa-vs-Anthony Pettis</option>		<option value=\"829198302\">RB Leipzig to advance-vs-Marseille to advance</option>		<option value=\"810479367\">Rose Namajunas-vs-Joanna Jedrzejczyk</option>		<option value=\"829179964\">Sevilla to advance-vs-Bayern Munchen to advance</option>	</select>                        </div>                    <div id=\"divContests\" style=\"DISPLAY: none\">                                <span id=\"lblContests\">Contests:</span>                <select name=\"ddlContests\" id=\"ddlContests\" onchange=\"javascript:OnDropDownChange(&#39;ddlContests&#39;)\">		<option value=\"\"></option>		<option value=\"818814703\">Baseball Props|MLB|Regular Season Wins|Chicago White Sox Regular Season Wins?</option>		<option value=\"818814799\">Baseball Props|MLB|Regular Season Wins|Colorado Rockies Regular Season Wins?</option>		<option value=\"818815002\">Baseball Props|MLB|Regular Season Wins|Kansas City Royals Regular Season Wins?</option>		<option value=\"818815089\">Baseball Props|MLB|Regular Season Wins|Miami Marlins Regular Season Wins?</option>		<option value=\"818815135\">Baseball Props|MLB|Regular Season Wins|Milwaukee Brewers Regular Season Wins?</option>		<option value=\"818815322\">Baseball Props|MLB|Regular Season Wins|Philadelphia Phillies Regular Season Wins?</option>		<option value=\"818815603\">Baseball Props|MLB|Regular Season Wins|St. Louis Cardinals Regular Season Wins?</option>		<option value=\"818815776\">Baseball Props|MLB|Regular Season Wins|Washington Nationals Regular Season Wins?</option>		<option value=\"733898864\">Basketball Props|NBA|Regular Season Wins|Atlanta Hawks Regular Season Wins?</option>		<option value=\"733898973\">Basketball Props|NBA|Regular Season Wins|Boston Celtics Regular Season Wins?</option>		<option value=\"733899444\">Basketball Props|NBA|Regular Season Wins|Detroit Pistons Regular Season Wins?</option>		<option value=\"733901003\">Basketball Props|NBA|Regular Season Wins|Los Angeles Lakers Regular Season Wins?</option>		<option value=\"733901236\">Basketball Props|NBA|Regular Season Wins|Minnesota Timberwolves Regular Season Wins?</option>		<option value=\"827863507\">Basketball Props|NCAA|Midwest Region Winner|Kansas to win Midwest Region?</option>		<option value=\"827862805\">Basketball Props|NCAA|Midwest Region Winner|Team to win Midwest Region?</option>		<option value=\"795952873\">Soccer Props|UEFA - Europa League|Outright|Europa League Winner</option>	</select>                        </div>                        <div id=\"divRaces\" style=\"DISPLAY: none\">                                <span id=\"lblHorseRaces\">Races:</span>                <select name=\"ddlRaces\" id=\"ddlRaces\">		<option value=\"\"></option>	</select>                        </div>            </td>            </tr>                            <tr>            <td colspan=\"2\"><div>    <span id=\"DRSUC_SDL\">From date:</span>    <input name=\"DRSUC:STB\" type=\"text\" value=\"3/25/2014\" id=\"DRSUC_STB\" />    <span controltovalidate=\"DRSUC_STB\" errormessage=\"Maximum date range is the last 4 years.\" display=\"Dynamic\" id=\"DRSUC_STBRV\" type=\"Date\" dateorder=\"mdy\" cutoffyear=\"2029\" century=\"2000\" evaluationfunction=\"RangeValidatorEvaluateIsValid\" maximumvalue=\"3/26/2018\" minimumvalue=\"3/25/2014\" style=\"color:Red;display:none;\">Maximum date range is the last 4 years.</span>        <span id=\"DRSUC_EDL\">To:</span>    <input name=\"DRSUC:ETB\" type=\"text\" value=\"3/25/2018\" id=\"DRSUC_ETB\" />    <span controltovalidate=\"DRSUC_ETB\" errormessage=\"Maximum date range is the last 4 years.\" display=\"Dynamic\" id=\"DRSUC_ETBRV\" type=\"Date\" dateorder=\"mdy\" cutoffyear=\"2029\" century=\"2000\" evaluationfunction=\"RangeValidatorEvaluateIsValid\" maximumvalue=\"3/26/2018\" minimumvalue=\"3/25/2014\" style=\"color:Red;display:none;\">Maximum date range is the last 4 years.</span>        <span controltovalidate=\"DRSUC_ETB\" errormessage=\"The &#39;from&#39; date cannot be later than the &#39;to&#39; date.\" display=\"Dynamic\" id=\"DRSUC_ETBCV\" type=\"Date\" dateorder=\"mdy\" cutoffyear=\"2029\" century=\"2000\" evaluationfunction=\"CompareValidatorEvaluateIsValid\" controltocompare=\"DRSUC_STB\" controlhookup=\"DRSUC_STB\" operator=\"GreaterThanEqual\" style=\"color:Red;display:none;\">The 'from' date cannot be later than the 'to' date.</span>    <input type=\"submit\" name=\"DRSUC:SB\" value=\"Submit\" onclick=\"javascript:WebForm_DoPostBackWithOptions(new WebForm_PostBackOptions(&quot;DRSUC:SB&quot;, &quot;&quot;, true, &quot;&quot;, &quot;&quot;, false, false))\" language=\"javascript\" id=\"DRSUC_SB\" />    <input type=\"submit\" name=\"DRSUC:btnOneDay\" value=\"1 Day\" id=\"DRSUC_btnOneDay\" style=\"width:70px;\" />    <input type=\"submit\" name=\"DRSUC:btnSevenDays\" value=\"7 Days\" id=\"DRSUC_btnSevenDays\" style=\"width:70px;\" />    <input type=\"submit\" name=\"DRSUC:btnFourteenDays\" value=\"14 Days\" id=\"DRSUC_btnFourteenDays\" style=\"width:70px;\" />    <input type=\"submit\" name=\"DRSUC:btnAll\" value=\"All\" id=\"DRSUC_btnAll\" style=\"width:70px;\" /></div><script type=\"text/javascript\">    var pathUrl = \"http://agent.pinnaclesports.com/BetsHistory.aspx\";    var i = pathUrl.lastIndexOf(\"/\");    var baseUrl = pathUrl.slice(0, i);</script><script type=\"text/javascript\" src=\"/Scripts/JQuery/jquery-1.6.2.js\"></script><script type=\"text/javascript\" src=\"/Scripts/JQuery/UI/jquery.ui.core.js\"></script><script type=\"text/javascript\" src=\"/Scripts/JQuery/UI/jquery.ui.widget.js\"></script><script type=\"text/javascript\" src=\"/Scripts/JQuery/UI/jquery.ui.datepicker.js\"></script><script type=\"text/javascript\" src=\"/Scripts/JQuery/UI/DatePickerLocalisation/jquery.ui.datepicker-en-US.js\"></script><link rel=\"stylesheet\" type=\"text/css\" href=\"/Styles/JQueryRelated/jquery.ui.core.css\" /><link rel=\"stylesheet\" href=\"/Styles/JQueryRelated/jquery.ui.datepicker.css\" /><link rel=\"stylesheet\" href=\"/Styles/JQueryRelated/jquery.ui.theme.css\" /><script type=\"text/javascript\">    $(function () {                $.datepicker.setDefaults($.datepicker.regional['en-US']);        //init the calendar by invoking it with a button        $(\"#\" + STBID).datepicker({            showOn: \"button\",            buttonImage: baseUrl + \"/images/calendar.gif\",            buttonImageOnly: true        });                $(\"#\" + STBID).datepicker({ minDate: '-4y' }); //init minDate        $(\"#\" + STBID).datepicker({ maxDate: '+1d' }); //init maxDate        $(\"#\" + STBID).datepicker({ changeMonth: true }); //init ChangeMonth        $(\"#\" + STBID).datepicker({ changeYear: true }); //init ChangeYear        $(\"#\" + STBID).datepicker(\"option\", \"minDate\", '-4y');        $(\"#\" + STBID).datepicker(\"option\", \"maxDate\", '+1d');        $(\"#\" + STBID).datepicker(\"option\", \"changeMonth\", true);        $(\"#\" + STBID).datepicker(\"option\", \"changeYear\", true);        $(\"#\" + ETBID).datepicker({            showOn: \"button\",            buttonImage: baseUrl + \"/images/calendar.gif\",            buttonImageOnly: true        });        $(\"#\" + ETBID).datepicker({ minDate: '-4y' }); //init minDate        $(\"#\" + ETBID).datepicker({ maxDate: '+1d' }); //init maxDate        $(\"#\" + ETBID).datepicker({ changeMonth: true }); //init ChangeMonth        $(\"#\" + ETBID).datepicker({ changeYear: true }); //init ChangeYear        $(\"#\" + ETBID).datepicker(\"option\", \"minDate\", '-4y');        $(\"#\" + ETBID).datepicker(\"option\", \"maxDate\", '+1d');        $(\"#\" + ETBID).datepicker(\"option\", \"changeMonth\", true);        $(\"#\" + ETBID).datepicker(\"option\", \"changeYear\", true);    });</script></td>            </tr>            </table>                             <p>                <div>		<table cellpadding=\"2\" bordercolor=\"LightGrey\" border=\"0\" id=\"TBW_GV\" style=\"border-color:LightGrey;border-width:1px;border-style:None;font-size:10pt;width:100%;\">			<tr class=\"sortableheader\" valign=\"top\" style=\"color:White;background-color:#303498;\">				<th scope=\"col\">                                <span id=\"TBW_GV__ctl1_L2\">Cust Id</span>                            </th><th align=\"center\" scope=\"col\">                                <span id=\"TBW_GV__ctl1_L3\">Trans.Time</span>                            </th><th align=\"center\" scope=\"col\">                                <span id=\"TBW_GV__ctl1_L4\">Event</span>                            </th><th align=\"center\" scope=\"col\">                                <span id=\"TBW_GV__ctl1_L5\">Choice</span>                            </th><th align=\"center\" scope=\"col\">                                <span id=\"TBW_GV__ctl1_L6\">Handicap</span>                            </th><th align=\"center\" scope=\"col\">                                <span id=\"TBW_GV__ctl1_L7\">Odds</span>                            </th><th align=\"right\" scope=\"col\">                                <span id=\"TBW_GV__ctl1_L8\">Stake (USD)</span>                            </th><th align=\"right\" scope=\"col\">                                <span id=\"TBW_GV__ctl1_L8\">To Win (USD)</span>                            </th><th align=\"left\" scope=\"col\">                                <span id=\"TBW_GV__ctl1_L9\">Status</span>                            </th>			</tr><tr style=\"color:Black;background-color:#E6E6E6;\"><td valign=\"top\" align=\"left\"> 54044 </td><td valign=\"top\" align=\"center\">3/24/2018<br>8:21:28 AM</td><td style=\"width:400px;\" valign=\"top\" align=\"center\"><span id=\"hidden33\" onclick=\"show(33);\" style=\"FONT-WEIGHT: bold; COLOR: blue;CURSOR: pointer;\">Parlay 3 selections </span> Ref No: 831956301-1<br><br><span id=\"show33\" style=\"font-size: 8pt; font-weight: normal;\"><table><tbody><tr><td> 1.  </td><td nowrap=\"\"> 3/24/2018</td><td class=\"MT1\" nowrap=\"\">Minnesota Timberwolves vs <b><u>Philadelphia 76ers</u></b></td></tr><tr><td> </td><td nowrap=\"\">3:05 PM</td><td class=\"MT1\" nowrap=\"\">Basketball/NBA<br>(Game Money Line<br><b>2</b>@-320    [Win]</td><td colspan=\"2\"> </td></tr><tr><td colspan=\"3\"> </td></tr><tr><td> 2.  </td><td nowrap=\"\"> 3/24/2018</td><td class=\"MT1\" nowrap=\"\">Chicago Bulls vs <b><u>Detroit Pistons</u></b></td></tr><tr><td> </td><td nowrap=\"\">4:05 PM</td><td class=\"MT1\" nowrap=\"\">Basketball/NBA<br>(Game Money Line<br><b>2</b>@-1289    [Win]</td><td colspan=\"2\"> </td></tr><tr><td colspan=\"3\"> </td></tr><tr><td> 3.  </td><td nowrap=\"\">3/24/2018</td><td class=\"MT1\" nowrap=\"\">Phoenix Suns vs <b><u>Orlando Magic</u></b></td></tr><tr><td> </td><td nowrap=\"\">4:05 PM</td><td class=\"MT1\" nowrap=\"\">Basketball/NBA<br>(Game Money Line<br><b>2</b>@-245    [Win]</td><td colspan=\"2\"> </td></tr><tr><td colspan=\"3\"> </td></tr></tbody></table></span></td><td valign=\"top\" align=\"center\"><b> </b></td><td valign=\"top\" align=\"center\"> </td><td valign=\"top\" align=\"center\"> - </td><td valign=\"top\" align=\"right\"> 1,000.00 </td><td valign=\"top\" align=\"left\"> Win </td><td valign=\"top\" align=\"right\"> 991.60 </td></tr>		</table>	</div>            </p>        </div>    <script type=\"text/javascript\"><!--var Page_Validators =  new Array(document.getElementById(\"DRSUC_STBRV\"), document.getElementById(\"DRSUC_ETBRV\"), document.getElementById(\"DRSUC_ETBCV\"));// --></script><script type=\"text/javascript\"><!--var STBID = \"DRSUC_STB\"; var _yearButtonsVisible = \"True\"; var ETBID = \"DRSUC_ETB\"; var _minYear = 2014; var _minMonth = 2; var _minDay = 25; var _maxYear = 2018; var _maxMonth = 2; var _maxDay = 26; var _monthNames=new Array(\"January\", \"February\", \"March\", \"April\", \"May\", \"June\", \"July\", \"August\", \"September\", \"October\", \"November\", \"December\"); var _rightToLeft=false; var _shortDatePattern=\"M/d/yyyy\"; var _calendarCallbackFunction=\"CalendarCallback\"; var Page_ValidationActive = false;if (typeof(ValidatorOnLoad) == \"function\") {    ValidatorOnLoad();}function ValidatorOnSubmit() {    if (Page_ValidationActive) {        return ValidatorCommonOnSubmit();    }    else {        return true;    }}        // --></script></form></body></html>";
			final PinnacleAgentParser pap = new PinnacleAgentParser();
			final Set<PendingEvent> pendingEvents = pap.parsePendingBets(xhtml, "x", "y");
		    if (pendingEvents != null && pendingEvents.size() > 0) {
	    			for (PendingEvent pe : pendingEvents) {
	    				LOGGER.error("PendingEvent: " + pe);
	    			}
		    }
		} catch (BatchException be) {
			LOGGER.error(be.getMessage(), be);
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

		// Get the document object
		final Document doc = parseXhtml(xhtml);

		// Get hidden input fields
		final String[] types = new String[] { "hidden", "text", "password", "submit" };
		final Elements forms = doc.select("form");
		if (forms != null && forms.size() > 0) {
			final Element form = forms.get(0);

			// Get form action field
			map.put("action", form.attr("action"));
			getAllElementsByType(form, "input", types, map);
		}

		LOGGER.info("Exiting parseIndex()");
		return map;
	}

	/**
	 * 
	 * @param xhtml
	 * @param accountName
	 * @param accountId
	 * @param map
	 * @param indiList
	 * @param wooList
	 * @return
	 * @throws BatchException
	 */
	public Set<PendingEvent> parsePendingBets(String xhtml, String accountName, String accountId, Map<String, String> map, String[][] indiList, String[][] wooList) throws BatchException {
		LOGGER.info("Entering parsePendingBets()");
		final Set<PendingEvent> pendingEvents = new HashSet<PendingEvent>();

		// Get the document object
		final Document doc = parseXhtml(xhtml);

		// Get hidden input fields
		final String[] types = new String[] { "hidden", "text", "password", "submit" };
		final Elements forms = doc.select("form");
		if (forms != null && forms.size() > 0) {
			LOGGER.debug("forms.size(): " + forms.size());
			final Element form = forms.get(0);

			// Get form action field
			map.put("action", form.attr("action"));
			getAllElementsByType(form, "input", types, map);
		}

		final Elements trs = doc.select("#TBW_GV tbody tr");
		if (trs != null && trs.size() > 0) {
			LOGGER.debug("trs.size(): " + trs.size());
			for (Element tr : trs) {
				// Parse the transaction
				parseTransaction(tr, pendingEvents, indiList, wooList);
			}
		}

		LOGGER.info("Exiting parsePendingBets()");
		return pendingEvents;
	}

	/**
	 * 
	 * @param tr
	 * @param pendingEvents
	 * @param indiList
	 * @param wooList
	 */
	private void parseTransaction(Element tr, Set<PendingEvent> pendingEvents, String[][] indiList, String[][] wooList) {
		LOGGER.info("Entering parseTransaction()");

		final String style = tr.attr("style");
		if (style.contains("color:Black;background-color:#E6E6E6;")) {
			final String trhtml = tr.html();
			if (trhtml.contains("Parlay")) {
				final Elements tds = tr.select("td");
				if (tds != null && tds.size() > 0) {
					String custid = null;
					String dateAccepted = null;
					String risk = null;
					String win = null;
					Element parlay = null;
					int count = 0;
					for (Element td : tds) {
						try {
							switch (count) {
								case 0:
									// Customer id
									custid = getCustomerId(td);
									break;
								case 1:
									// Date accepted
									dateAccepted = getDateAccepted(td.html());
									break;
								case 2:
									// Event
									parlay = td;
									break;
								case 6:
									// Risk
									risk = getRisk(td);
									break;
								case 7:
									// Win
									win = getWin(td);
									break;
								case 3:
								case 4:
								case 5:
								default:
									break;
								case 8:
									break;
							}
						} catch (Throwable t) {
							LOGGER.error(t.getMessage(), t);
						}
						count++;
					}

					// Parse the Parlay
					parseParlay(parlay, pendingEvents, indiList, wooList, custid, dateAccepted, risk, win);
				}
			} else {
				final Elements tds = tr.select("td");
				if (tds != null && tds.size() > 0) {
					processTransaction(tds, pendingEvents, indiList, wooList);
				}
			}
		}

		LOGGER.info("Exiting parseTransaction()");
	}

	/**
	 * 
	 * @param tds
	 * @param pendingEvents
	 * @param indiList
	 * @param wooList
	 */
	private void processTransaction(Elements tds, Set<PendingEvent> pendingEvents, String[][] indiList, String[][] wooList) {
		LOGGER.info("Entering processTransaction()");
		final PendingEvent pe = new PendingEvent();
		int count = 0;

		for (Element td : tds) {
			try {
				switch (count) {
					case 0:
						// Customer id
						parseCustomerId(td, pe);
						break;
					case 1:
						// Date accepted
						parseDateAccepted(td, pe);
						break;
					case 2:
						// Event
						parseEvent(td, pe);
						break;
					case 3:
						// Choice
						parseChoice(td, pe);
						break;
					case 4:
						// Handicap
						parseHandicap(td, pe);
						break;
					case 5:
						// Odds
						parseOdds(td, pe);
						break;
					case 6:
						// Risk
						parseRisk(td, pe);
						break;
					case 7:
						// Win
						parseWin(td, pe);
						break;
					case 8:
					default:
						break;
				}
			} catch (Throwable t) {
				LOGGER.error(t.getMessage(), t);
			}
			count++;
		}

		LOGGER.debug("pe: "  + pe);

		// Add a pending event; make sure it's from a good source
		for (int x = 0; x < indiList.length; x++) {
			if (pe.getCustomerid().equals(indiList[x][0])) {
				final Double win = Double.valueOf(pe.getWin().replace("$", "").replaceAll(",", ""));
				final Double indiWin = Double.valueOf(indiList[x][1]);
				LOGGER.debug("win: " + win);
				LOGGER.debug("indiWin: " + indiWin);
				if (win >= indiWin) {
					LOGGER.debug("peYYY: "  + pe);
					pendingEvents.add(pe);
				}
			}			
		}

		for (int x = 0; x < wooList.length; x++) {
			if (pe.getCustomerid().equals(wooList[x][0])) {
				final Double win = Double.valueOf(pe.getWin().replace("$", "").replaceAll(",", ""));
				final Double wooWin = Double.valueOf(wooList[x][1]);
				LOGGER.debug("win: " + win);
				LOGGER.debug("wooWin: " + wooWin);
				if (win >= wooWin) {
					LOGGER.debug("peXXX: "  + pe);
					pendingEvents.add(pe);
				}
			}
		}

		LOGGER.info("Exiting processTransaction()");
	}

	/**
	 * 
	 * @param dateAccepted
	 * @return
	 */
	private String getDateAccepted(String dateAccepted) {
		LOGGER.info("Entering getDateAccepted()");

		if (dateAccepted != null && dateAccepted.length() > 0) {
			dateAccepted = dateAccepted.replace("<br>", " ").trim();
		}

		LOGGER.info("Exiting getDateAccepted()");
		return dateAccepted;
	}

	/**
	 * 
	 * @param eventInfo
	 * @param pe
	 */
	private void parseEvent(Element td, PendingEvent pe) {
		LOGGER.info("Entering parseEvent()");

		if (td != null) {
//			<a class="boldblueanchor" href="BetDetail.aspx?ticketno=832150011&wagerno=1">Ref No: 832150011-1</a>
//			<br>
//			Florida State -vs- 
//			<b>Michigan</b>
//			<br>
//			Handicap (1st Half)
			final Elements as = td.select("a");
			if (as != null && as.size() > 0) {
				//
				// Normal "Game"
				//

				// Parse ticket number
				parseTicketNumber(as, pe);

				// Parse event Info
				parseEventInfo(td, pe);
			}
		}

		LOGGER.info("Exiting parseEvent()");
	}

	/**
	 * 
	 * @param as
	 * @param pe
	 */
	private void parseTicketNumber(Elements as, PendingEvent pe) {
		LOGGER.info("Entering parseTicketNumber()");

		final Element a = as.get(0);
		pe.setPosturl(a.attr("href"));
		String ahtml = a.html();
		if (ahtml != null && ahtml.length() > 0) {
			ahtml = ahtml.replace("Ref No:&nbsp;", "").trim();
			pe.setTicketnum(ahtml);
		}

		LOGGER.info("Exiting parseTicketNumber()");
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 */
	private void parseDateAccepted(Element td, PendingEvent pe) {
		LOGGER.info("Entering parseDateAccepted()");

		pe.setDateaccepted(getDateAccepted(td.html()));

		LOGGER.info("Exiting parseDateAccepted()");
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 */
	private void parseCustomerId(Element td, PendingEvent pe) {
		LOGGER.info("Entering parseCustomerId()");

		// Customer id
		pe.setCustomerid(getCustomerId(td));

		LOGGER.info("Entering parseCustomerId()");
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 */
	private void parseChoice(Element td, PendingEvent pe) {
		LOGGER.info("Entering parseChoice()");

		if (!"total".equals(pe.getEventtype())) { 
			final Elements bs = td.select("b");
			if (bs != null && bs.size() > 0) {
				final Element b = bs.get(0);
				String bhtml = b.html();
				if (bhtml != null && bhtml.length() > 0) {
					bhtml = bhtml.trim();
					pe.setTeam(bhtml);
				}
			}
		} else if ("total".equals(pe.getEventtype())) {
			final Elements bs = td.select("b");
			if (bs != null && bs.size() > 0) {
				final Element b = bs.get(0);
				String bhtml = b.html();
				if (bhtml != null && bhtml.length() > 0) {
					bhtml = bhtml.replace("&nbsp;", "").trim();
					if (bhtml != null && bhtml.length() > 0) {
						if (bhtml.equals("Under")) {
							pe.setLineplusminus("u");
						} else if (bhtml.equals("Over")) {
							pe.setLineplusminus("o");
						}
					}
				}
			}
		}

		LOGGER.info("Exiting parseChoice()");
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 */
	private void parseEventInfo(Element td, PendingEvent pe) {
		LOGGER.info("Entering parseEventInfo()");

		String tdhtml = td.html();
		LOGGER.debug("html: " + tdhtml);
		int bindex = tdhtml.indexOf("<br>");
		if (bindex != -1) {
			tdhtml = tdhtml.substring(bindex + 4);
			if (tdhtml != null && tdhtml.length() > 0) {
				bindex = tdhtml.indexOf("<br>");
				if (bindex != -1) {
					// Parse team
					parseTeam(tdhtml, bindex, pe);

					String type = tdhtml.substring(bindex + 4);
					LOGGER.debug("type: " + type);
					if (type != null && tdhtml.length() > 0) {
						int pindex = type.indexOf("(");
						if (pindex != -1) {
							// Parse event type
							parseEventType(type, pindex, pe);
	
							// Parse line type
							parseLineType(type, pindex, pe);
						}
					}
				}
			}
		}

		LOGGER.info("Exiting parseEventInfo()");
	}

	/**
	 * 
	 * @param tdhtml
	 * @param bindex
	 * @param pe
	 */
	private void parseTeam(String tdhtml, int bindex, PendingEvent pe) {
		LOGGER.info("Entering parseTeam()");

		final String teams = tdhtml.substring(0, bindex);
		LOGGER.debug("teams: " + teams);
		if (teams != null && teams.length() > 0) {
			// Duke -vs- 
			// <b>Kansas</b>
			
			// <b>Duke</b>
			// -vs- Kansas
			boolean nomain = false;
			int findex = teams.indexOf("<b>");
			if (findex != -1) {
				int eindex = teams.indexOf("</b>");
				if (eindex != -1) {
					String team1 = teams.substring(findex + 3, eindex);
					LOGGER.debug("team1: " + team1);
					pe.setTeam(team1);
				}
			} else {
				// No main team
				nomain = true;
			}
			
			int index = teams.indexOf("-vs-");
			if (index != -1) {
				String first = teams.substring(0, index);
				String second = teams.substring(index + 4);
				LOGGER.debug("first: " + first);
				LOGGER.debug("second: " + second);

				if (first != null && first.length() > 0) {
					first = first.replace("&nbsp;", "").trim();
					if (!first.contains("<b>")) {
						if (nomain) {
							pe.setTeam(first);
						} else {
							pe.setPitcher(first);
						}
					}
				}

				if (second != null && second.length() > 0) {
					second = second.replace("&nbsp;", "").trim();
					if (!second.contains("<b>")) {
						pe.setPitcher(second);
					}								
				}
			}
		}

		LOGGER.info("Exiting parseTeam()");
	}

	/**
	 * 
	 * @param type
	 * @param pindex
	 * @param pe
	 */
	private void parseEventType(String type, int pindex, PendingEvent pe) {
		LOGGER.info("Entering parseEventType()");

		String bettype = type.substring(0, pindex);
		LOGGER.debug("bettype: " + bettype);
		if (bettype != null && bettype.length() > 0) {
			bettype = bettype.replace("&nbsp;", "").trim();
			if ("Handicap".equals(bettype)) {
				pe.setEventtype("spread");
			} else if ("Over/Under".equals(bettype)) {
				pe.setEventtype("total");
			} else if ("Money Line".equals(bettype)) {
				pe.setEventtype("ml");
			}
		}

		LOGGER.info("Exiting parseEventType()");
	}

	/**
	 * 
	 * @param type
	 * @param pindex
	 * @param pe
	 */
	private void parseLineType(String type, int pindex, PendingEvent pe) {
		LOGGER.info("Entering parseLineType()");

		type = type.substring(pindex + 1);
		LOGGER.debug("type: " + type);
		if (type != null && type.length() > 0) {
			pindex = type.indexOf(")");
			if (pindex != -1) {
				type = type.substring(0, pindex);
				LOGGER.debug("type2: " + type);
				if (type != null && type.length() > 0) {
					type = type.replace("&nbsp;", "").trim();
					if ("Game".equals(type)) {
						pe.setLinetype("game");
					} else if ("1st Half".equals(type)) {
						pe.setLinetype("first");
					} else if ("2nd Half".equals(type)) {
						pe.setLinetype("second");
					}
				}
			}
		}

		LOGGER.info("Exiting parseLineType()");
	}

	/**
	 * 
	 * @param td
	 * @param pendingEvents
	 * @param indiList
	 * @param wooList
	 * @param custid
	 * @param dateAccepted
	 * @param risk
	 * @param win
	 */
	private void parseParlay(Element td, Set<PendingEvent> pendingEvents, String[][] indiList, String[][] wooList, String custid, String dateAccepted, String risk, String win) {
		LOGGER.info("Entering parseParlay()");

		// First get the ticket number
		final String ticketNumber = parseParlayTicketNumber(td);
		LOGGER.debug("ticketNumber: " + ticketNumber);

		// Now get all of the games
		final Elements trs = td.select("span table tbody tr");
		parseGames(ticketNumber, trs, pendingEvents, indiList, wooList, custid, dateAccepted, risk, win);

		LOGGER.info("Exiting parseParlay()");
	}

	/**
	 * 
	 * @param ticketNumber
	 * @param trs
	 * @param pendingEvents
	 * @param indiList
	 * @param wooList
	 * @param custid
	 * @param dateAccepted
	 * @param risk
	 * @param win
	 */
	private void parseGames(String ticketNumber, Elements trs, Set<PendingEvent> pendingEvents, String[][] indiList, String[][] wooList, String custid, String dateAccepted, String risk, String win) {
		LOGGER.info("Entering parseGames()");

		if (trs != null && trs.size() > 0) {
			LOGGER.debug("trs.size(): " + trs.size());
			int count = 0;
			PendingEvent pe = null;

			for (Element tr : trs) {
				switch (count) {
					case 0:
						pe = new PendingEvent();
						pe.setCustomerid(custid);
						pe.setDateaccepted(dateAccepted);
						pe.setRisk(risk);
						pe.setWin(win);
						pe.setTicketnum(ticketNumber);
						parseParlayTeams(tr.select("td"), pe);
						count++;
						break;
					case 1:
						parseParlayGame(tr.select("td"), pe);
						LOGGER.debug("PendingEvent: " + pe);
						try {
							// Add a pending event; make sure it's from a good source
							for (int x = 0; x < indiList.length; x++) {
								if (pe.getCustomerid().equals(indiList[x][0])) {
									Double pwin = Double.valueOf(pe.getWin().replace("$", "").replaceAll(",", ""));
									Double indiWin = Double.valueOf(indiList[x][1]);
									if (pwin >= indiWin) {
										pendingEvents.add(pe);
									}
								}			
							}
	
							for (int x = 0; x < wooList.length; x++) {
								if (pe.getCustomerid().equals(wooList[x][0])) {
									Double pwin = Double.valueOf(pe.getWin().replace("$", "").replaceAll(",", ""));
									Double wooWin = Double.valueOf(wooList[x][1]);
									if (pwin >= wooWin) {
										pendingEvents.add(pe);
									}
								}
							}
						} catch (Throwable t) {
							LOGGER.error(t.getMessage(), t);
							LOGGER.error("PE: " + pe);
							LOGGER.error("TR: " + tr);
						}

						count++;
						break;
					case 2:
						count = 0;
						break;
					default:
						break;
				}
			}
		}

		LOGGER.info("Exiting parseGames()");
	}

	/**
	 * 
	 * @param tds
	 * @param pe
	 */
	private void parseParlayTeams(Elements tds, PendingEvent pe) {
		LOGGER.info("Entering parseParlayTeams()");

		if (tds != null && tds.size() > 0) {
			LOGGER.debug("tds.size(): " + tds.size());
			LOGGER.debug("tds.html(): " + tds.html());
			int count = 0;
			for (Element td : tds) {
				switch (count) {
					case 1:
						String date = td.html();
						LOGGER.debug("datehtml: " + date);
						if (date != null) {
							date = date.trim();
							date = date.replace("&nbsp;", "");
							LOGGER.debug("date: " + date);
							pe.setEventdate(date);
						}
						break;
					case 2:
						final Elements us = td.select("b u");
						if (us != null && us.size() > 0) {
							final Element u = us.get(0);
							String team = u.html();
							if (team != null && team.length() > 0) {
								team = team.replace("&nbsp;", "");
								LOGGER.debug("team: " + team);
								pe.setTeam(team);
							}
						}

						String tdhtml = td.html().trim();
						tdhtml = td.html().replace("&nbsp;", "");
						int index = tdhtml.indexOf("vs");
						if (index != -1) {
							String first = tdhtml.substring(0, index);
							String second = tdhtml.substring(index + 2);
							LOGGER.debug("first: " + first);
							LOGGER.debug("second: " + second);

							if (first != null && first.length() > 0) {
								first = first.trim();
								if (!first.contains("<b>")) {
									pe.setPitcher(first);
								}
							}

							if (second != null && second.length() > 0) {
								second = second.trim();
								if (!second.contains("<b>")) {
									pe.setPitcher(second);
								}								
							}
						}
						break;
					case 0:
					default:
						break;
				}
				count++;
			}
		}

		LOGGER.info("Exiting parseParlayTeams()");
	}

	/**
	 * 
	 * @param tds
	 * @param pe
	 */
	private void parseParlayGame(Elements tds, PendingEvent pe) {
		LOGGER.info("Entering parseParlayGame()");

		if (tds != null && tds.size() > 0) {
			int count = 0;
			for (Element td : tds) {
				LOGGER.debug("td: " + td);
				switch (count) {
					case 1:
						String date = td.html();
						if (date != null) {
							date = date.replace("&nbsp;", " ");
							date = date.trim();
							String prevDate = pe.getEventdate();
							LOGGER.debug("prevDate: " + prevDate);
							pe.setEventdate(prevDate + " " + date);
						}
						break;
					case 2:
						// Baseball/Alt Runlines<br>(Game&nbsp;Handicap<br>@<b>-1.5</b>/+199&nbsp;&nbsp;&nbsp;&nbsp;[Pending] 
						String gameInfo = td.html();
						LOGGER.debug("gameInfo: " + gameInfo);
						if (gameInfo != null && gameInfo.length() > 0 && gameInfo.contains("Baseball/MLB")) {
							gameInfo = gameInfo.replace("&nbsp;", " ");
							int index = gameInfo.indexOf("<br>");
							if (index != -1) {
								gameInfo = gameInfo.substring(index + 4);
								if (gameInfo != null && gameInfo.length() > 0) {
									index = gameInfo.indexOf("<br>");
									if (index != -1) {
										String gInfo = gameInfo.substring(0, index);
										if (gInfo != null && gInfo.length() > 0) {
											int jindex = gInfo.indexOf(" ");
											if (jindex != -1) {
												String ltype = gInfo.substring(0, jindex);
												if (ltype != null && ltype.length() > 0) {
													ltype = ltype.replace("(", "").trim();
													LOGGER.debug("ltype: " + ltype);
													if ("Game".equals(ltype)) {
														pe.setLinetype("game");
													} else if ("1st Half".equals(ltype)) {
														pe.setLinetype("first");
													} else if ("2nd Half".equals(ltype)) {
														pe.setLinetype("second");
													}
												}

												final String etype = gInfo.substring(jindex + 1);
												LOGGER.debug("etype: " + etype);
												if ("Handicap".equals(etype)) {
													pe.setEventtype("spread");
												} else if ("Total".equals(etype)) {
													pe.setEventtype("total");
												} else if ("Money Line".equals(etype)) {
													pe.setEventtype("ml");
												}
											}
										}

										String line = gameInfo.substring(index + 4);
										index = line.indexOf("</b>");
										if (index != -1) {
											line = line.substring(index + 5);
											LOGGER.debug("line: " + line);

											index = line.indexOf("[");
											if (index != -1) {
												line = line.substring(0, index).trim();
												LOGGER.debug("line: " + line);

												if ("spread".equals(pe.getEventtype())) {
													if (line.startsWith("-")) {
														pe.setLineplusminus("-");
														pe.setLine(line.substring(1));
													} else {
														pe.setLineplusminus("+");
														pe.setLine(line);														
													}
												} else if ("total".equals(pe.getEventtype())) {
													
												} else if ("ml".equals(pe.getEventtype())) {
													if (line.startsWith("-")) {
														pe.setLineplusminus("-");
														pe.setLine(line.substring(1));
														pe.setJuiceplusminus("-");
														pe.setJuice(line.substring(1));
													} else {
														pe.setLineplusminus("+");
														pe.setLine(line);
														pe.setJuiceplusminus("+");
														pe.setJuice(line);
													}				
												}
											}
										}
									}
								}
							}
						}
						break;
					case 0:
					default:
						break;
				}
				count++;
			}
		}

		LOGGER.info("Exiting parseParlayGame()");
	}

	/**
	 * 
	 * @param td
	 * @return
	 */
	private String parseParlayTicketNumber(Element td) {
		LOGGER.info("Entering parseParlayTicketNumber()");
		String ticketNumber = null;

		String tdhtml = td.html();
		int index = tdhtml.indexOf("</span>");
		if (index != -1) {
			tdhtml = tdhtml.substring(index + 7);
			if (tdhtml != null && tdhtml.length() > 0) {
				index = tdhtml.indexOf("<br>");
				if (index != -1) {
					tdhtml = tdhtml.substring(0, index);
					tdhtml = tdhtml.replace("&nbsp;Ref No:&nbsp;", "").trim();
					ticketNumber = tdhtml;
				}
			}
		}

		LOGGER.info("Exiting parseParlayTicketNumber()");
		return ticketNumber;
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 */
	private void parseHandicap(Element td, PendingEvent pe) {
		LOGGER.info("Entering parseHandicap()");

		String handicap = td.html();
		LOGGER.debug("handicap: " + handicap);

		if (handicap != null && handicap.length() > 0) {
			handicap = handicap.replace("&nbsp;", "").trim();
			final String eventType = pe.getEventtype();
			LOGGER.debug("eventType: " + eventType);
			if (eventType != null && eventType.length() > 0) {
				if ("spread".equals(eventType)) {
					if (handicap.startsWith("-")) {
						handicap = handicap.substring(1);
						pe.setLineplusminus("-");
						pe.setLine(handicap);
					} else {
						pe.setLineplusminus("+");
						pe.setLine(handicap);							
					}
				} else if ("total".equals(eventType)) {
					pe.setLine(handicap);
				}
			}
		}

		LOGGER.info("Exiting parseHandicap()");
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 */
	private void parseOdds(Element td, PendingEvent pe) {
		LOGGER.info("Entering parseOdds()");

		String odds = td.html();
		if (odds != null && odds.length() > 0) {
			odds = odds.trim();
			odds = odds.replace("&nbsp;", "");
			final String eventType = pe.getEventtype();
			if (eventType != null && eventType.length() > 0) {
				if (odds.startsWith("-")) {
					LOGGER.debug("odds: " + odds);
					odds = odds.substring(1);
					pe.setJuiceplusminus("-");
					pe.setJuice(odds);
					if ("ml".equals(eventType)) {
						pe.setLineplusminus("-");
						pe.setLine(odds);									
					}
				} else {
					LOGGER.debug("odds: " + odds);
					odds = odds.substring(1);
					pe.setJuiceplusminus("+");
					pe.setJuice(odds);
					if ("ml".equals(eventType)) {
						pe.setLineplusminus("+");
						pe.setLine(odds);						
					}
				}
			}
		}

		LOGGER.info("Exiting parseOdds()");
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 */
	private void parseRisk(Element td, PendingEvent pe) {
		LOGGER.info("Entering parseRisk()");

		pe.setRisk(getRisk(td));

		LOGGER.info("Exiting parseRisk()");
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 */
	private void parseWin(Element td, PendingEvent pe) {
		LOGGER.info("Entering parseWin()");

		pe.setWin(getWin(td));

		LOGGER.info("Exiting parseWin()");
	}

	/**
	 * 
	 * @param td
	 * @return
	 */
	private String getCustomerId(Element td) {
		LOGGER.info("Entering getCustomerId()");

		String custid = td.html();
		if (custid != null) {
			custid = custid.trim();
		}

		LOGGER.info("Entering getCustomerId()");
		return custid;
	}

	/**
	 * 
	 * @param td
	 * @return
	 */
	private String getRisk(Element td) {
		LOGGER.info("Entering getRisk()");
		String risk = td.html();

		if (risk != null && risk.length() > 0) {
			risk = risk.trim();
		}

		LOGGER.info("Exiting parseRisk()");
		return risk;
	}

	/**
	 * 
	 * @param td
	 * @return
	 */
	private String getWin(Element td) {
		LOGGER.info("Entering getWin()");
		String win = td.html();

		if (win != null && win.length() > 0) {
			win = win.trim();
		}

		LOGGER.info("Exiting getWin()");
		return win;
	}

	@Override
	public Map<String, String> parseLogin(String xhtml) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> parseMenu(String xhtml, String[] type, String[] sport) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> parseGames(String xhtml, String type, Map<String, String> inputFields) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> parseEventSelection(String xhtml, SiteTeamPackage siteTeamPackage, String type)
			throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String parseTicketNumber(String xhtml) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected <T> List<T> getGameData(Elements elements) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}
}