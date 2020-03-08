$.fn.processAccountEvents = function(committedevents, id, thtml, open) {
	if (typeof committedevents.accountevents != 'undefined' && committedevents.accountevents != null && committedevents.accountevents.length > 0) {
		var accountsevents = committedevents.accountevents;
		if (open) {
			thtml.push('<tr id="sa' + id + '">');
		} else {
			thtml.push('<tr id="sa' + id + '" style="display: none;">');
		}
		thtml.push('<td colspan="9">');
		thtml.push('  <table cellspacing="0" cellpadding="0" class="account_event_table">');
		thtml.push('	<thead class="trans_data_acct">');
		thtml.push('		<tr class="trans_data_acct_header">');
		thtml.push('			<th class="trans_data_acct_event_name trans_data_acct_header_border">Acct Name</th>');
		thtml.push('			<th class="trans_data_acct_event_type trans_data_acct_header_border">Type</th>');
		thtml.push('			<th class="trans_data_acct_event_status trans_data_acct_header_border">Status</th>');
		thtml.push('			<th class="trans_data_acct_event_line trans_data_acct_header_border">Line</th>');
		thtml.push('			<th class="trans_data_acct_event_juice trans_data_acct_header_border">Juice</th>');
		thtml.push('			<th class="trans_data_acct_event_risk trans_data_acct_header_border">Risk</th>');
		thtml.push('			<th class="trans_data_acct_event_towin trans_data_acct_header_border">Win</th>');
		thtml.push('			<th class="trans_data_acct_event_owner trans_data_acct_header_border">Owner %</th>');
		thtml.push('			<th class="trans_data_acct_event_partner trans_data_acct_header_border">Partner %</th>');
		thtml.push('			<th class="trans_data_acct_event_ticket trans_data_acct_header_border">Ticket #</th>');
		thtml.push('			<th class="trans_data_acct_event_info">Information</th>');
		thtml.push('		</tr>');
		thtml.push('	</thead>');
		thtml.push('	<tbody>');
		var totalRiskAmount = Number("00.00");
		var totalWinAmount = Number("00.00");
		for (var i = 0; i < accountsevents.length; i++) {
			var oPer = accountsevents[i].ownerpercentage / 100;
			var pPer = accountsevents[i].partnerpercentage / 100;
			var oAmt = 0;
			var pAmt = 0;
			var tAmt = 0;
			var rAmt = 0;
			if (typeof accountsevents[i].towinamount != 'undefined' && accountsevents[i].towinamount != '') {
				oAmt = "$" + oPer * accountsevents[i].towinamount;
				pAmt = "$" + pPer * accountsevents[i].towinamount;
				tAmt = Number(accountsevents[i].towinamount);
			}

			if (typeof accountsevents[i].riskamount != 'undefined' && accountsevents[i].riskamount != '') {
				rAmt = Number(accountsevents[i].riskamount);
			}

			var lineValue = '&nbsp;';
			var juiceValue = '&nbsp;';
			if (typeof accountsevents[i].spreadindicator != 'undefined'
					&& typeof accountsevents[i].spread != 'undefined'
					&& typeof accountsevents[i].spreadjuice != 'undefined') {
				lineValue = accountsevents[i].spreadindicator
						+ accountsevents[i].spread;
				juiceValue = accountsevents[i].spreadjuice;
			}
			if (accountsevents[i].type === 'total'
					&& typeof accountsevents[i].totalindicator != 'undefined'
					&& typeof accountsevents[i].total != 'undefined'
					&& typeof accountsevents[i].totaljuice != 'undefined') {
				lineValue = accountsevents[i].totalindicator
						+ accountsevents[i].total;
				juiceValue = accountsevents[i].totaljuice;
			} else if (accountsevents[i].type === 'ml'
					&& typeof accountsevents[i].mlindicator != 'undefined'
					&& typeof accountsevents[i].mljuice != 'undefined') {
				lineValue = accountsevents[i].mlindicator
						+ accountsevents[i].mljuice;
				juiceValue = accountsevents[i].mljuice;
			}

			var ticketNumber = '&nbsp;';
			if (typeof accountsevents[i].accountconfirmation != 'undefined' && accountsevents[i].accountconfirmation != '') {
				ticketNumber = accountsevents[i].accountconfirmation;
			}

			var type = 'Spread';
			if (accountsevents[i].type === 'total') {
				type = 'Total';
			} else if (accountsevents[i].type === 'ml') {
				type = 'Money Line';
			}

			var information = '&nbsp;';
			if (typeof accountsevents[i].errormessage != 'undefined' && accountsevents[i].errormessage != '') {
				information = accountsevents[i].errormessage;
			}
			if (typeof accountsevents[i].errorexception != 'undefined' && accountsevents[i].errorexception != '') {
				information = information + "; " + accountsevents[i].errorexception;
			}

			var riskAmount = '&nbsp;';
			if (typeof accountsevents[i].riskamount != 'undefined' && accountsevents[i].riskamount != '') {
				riskamount = "$" + accountsevents[i].riskamount;
			}

			var towinamount = '&nbsp;';
			if (typeof accountsevents[i].towinamount != 'undefined' && accountsevents[i].towinamount != '') {
				towinamount = "$" + accountsevents[i].towinamount;
			}

			var status = 'Complete';
			if (accountsevents[i].status === 'Complete') {
				thtml.push('	<tr class="trans_data_acct_event trans_data_acct_event_completed">');
				totalWinAmount =  totalWinAmount + tAmt;
				totalRiskAmount = totalRiskAmount + rAmt;
			} else if (accountsevents[i].status === 'In Progress') {
				thtml.push('	<tr class="trans_data_acct_event trans_data_acct_event_inprogess">');
				status = 'In Progress';
			} else {
				thtml.push('	<tr class="trans_data_acct_event trans_data_acct_event_notcompleted">');
				status = 'Fail';
			}

			thtml.push('			<td class="trans_data_acct_event_name trans_data_acct_event_data">' + accountsevents[i].name + '</td>');
			thtml.push('			<td class="trans_data_acct_event_type trans_data_acct_event_data">' + type + '</td>');
			thtml.push('			<td class="trans_data_acct_event_status trans_data_acct_event_data">' + status + '</td>');
			thtml.push('			<td class="trans_data_acct_event_line trans_data_acct_event_data">' + lineValue + '</td>');
			thtml.push('			<td class="trans_data_acct_event_juice trans_data_acct_event_data">' + juiceValue + '</td>');
			thtml.push('			<td class="trans_data_acct_event_risk trans_data_acct_event_data">' + riskamount + '</td>');
			thtml.push('			<td class="trans_data_acct_event_towin trans_data_acct_event_data">' + towinamount + '</v>');
			thtml.push('			<td class="trans_data_acct_event_owner trans_data_acct_event_data">' + oAmt + '</td>');
			thtml.push('			<td class="trans_data_acct_event_partner trans_data_acct_event_data">' + pAmt + '</td>');
			thtml.push('			<td class="trans_data_acct_event_ticket trans_data_acct_event_data">' + ticketNumber + '</td>');
			thtml.push('			<td class="trans_data_acct_event_info trans_data_acct_event_data">' + information + '</td>');
			thtml.push('		</tr>');
		}
		thtml.push('		<tr class="trans_data_acct_event trans_data_acct_event_inprogess">');
		thtml.push('			<td class="trans_data_acct_event_name">TOTALS</td>');
		thtml.push('			<td class="trans_data_acct_event_type">&nbsp;</td>');
		thtml.push('			<td class="trans_data_acct_event_status">&nbsp;</td>');
		thtml.push('			<td class="trans_data_acct_event_line">&nbsp;</td>');
		thtml.push('			<td class="trans_data_acct_event_juice">&nbsp;</td>');
		thtml.push('			<td class="trans_data_acct_event_risk">$' + totalRiskAmount + '</td>');
		thtml.push('			<td class="trans_data_acct_event_towin">$' + totalWinAmount + '</td>');
		thtml.push('			<td class="trans_data_acct_event_owner">&nbsp;</td>');
		thtml.push('			<td class="trans_data_acct_event_partner">&nbsp;</td>');
		thtml.push('			<td class="trans_data_acct_event_ticket">&nbsp;</td>');
		thtml.push('			<td class="trans_data_acct_event_info">&nbsp;</td>');
		thtml.push('		</tr>');
		thtml.push('	</tbody>');
		thtml.push('</table>');
		thtml.push('</td>');
		thtml.push('</tr>');
	}
	return thtml;
}

$.fn.determineTotalRiskAmount = function(committedevents) {
	var totalRiskAmount = Number("00.00"); 
	if (typeof committedevents.accountevents != 'undefined' && committedevents.accountevents != null && committedevents.accountevents.length > 0) {
		var accountsevents = committedevents.accountevents;
		for (var i = 0; i < accountsevents.length; i++) {
			var tAmt = 0;
			if (typeof accountsevents[i].riskamount != 'undefined' && accountsevents[i].riskamount != '') {
				tAmt = Number(accountsevents[i].riskamount);
			}

			if (accountsevents[i].status === 'Complete') {
				totalRiskAmount =  totalRiskAmount + tAmt;
			}
		}
	}
	return totalRiskAmount;
}

$.fn.determineTotalWinAmount = function(committedevents) {
	var totalWinAmount = Number("00.00"); 
	if (typeof committedevents.accountevents != 'undefined' && committedevents.accountevents != null && committedevents.accountevents.length > 0) {
		var accountsevents = committedevents.accountevents;
		for (var i = 0; i < accountsevents.length; i++) {
			var tAmt = 0;
			if (typeof accountsevents[i].towinamount != 'undefined' && accountsevents[i].towinamount != '') {
				tAmt = Number(accountsevents[i].towinamount);
			}

			if (accountsevents[i].status === 'Complete') {
				totalWinAmount =  totalWinAmount + tAmt;
			}
		}
	}
	return totalWinAmount;
}

// Get all accounts for user
$.fn.setupTransactions = function(data, open) {
	var thtml = [];
	var totalRiskAmount = Number("00.00");
	var totalWinAmount = Number("00.00");
	if (typeof data != 'undefined' && data != 'null' && typeof data.committedevents != 'undefined' && data.committedevents.length > 0) {
		var committedevents = data.committedevents;
		for (var i = 0; i < committedevents.length; i++) {
			status = 'Incomplete';
			if (committedevents[i].iscompleted) {
				status = 'Complete';
			}
			var idValue = 's' + committedevents[i].id;
			var type = 'Spread';
			if (committedevents[i].eventtype === 'total') {
				type = 'Total';
				idValue = 't' + committedevents[i].id;
			} else if (committedevents[i].eventtype === 'ml') {
				type = 'Money Line';
				idValue = 'sam' + committedevents[i].id;
			}
			var oddeven = i % 2;
			if (oddeven === 0) {
				thtml.push('<tr id="' + idValue + '" class="trans_data_header">');
			} else {
				thtml.push('<tr id="' + idValue + '" class="trans_data_header trans_data_odd">');
			}

			if (open) {
				thtml.push('  <td id="pm' + idValue + '" class="trans_data_header_plusminus">-</td>');
			} else {
				thtml.push('  <td id="pm' + idValue + '" class="trans_data_header_plusminus">+</td>');
			}
			var customdate = committedevents[i].datentime; 
//			var index = customdate.indexOf("IST");;
//			var date = new Date(customdate.substring(0,index));
			var date = new Date(customdate);

			thtml.push('  <td class="trans_data_header_datetime">' + $.fn.determineDate(date) + '</td>');
			thtml.push('  <td class="trans_data_header_type">' + type + '</td>');
			thtml.push('  <td class="trans_data_header_status">' + status + '</td>');
			thtml.push('  <td class="trans_data_header_description">' + committedevents[i].eventname + '</td>');
			var eventdate = committedevents[i].eventdatetime; 
//			var index = customdate.indexOf("IST");
//			var date = new Date(customdate.substring(0,index));
			var edate = new Date(eventdate);

			thtml.push('  <td class="trans_data_header_eventdatetime">' + $.fn.determineDate(edate) + '</td>');
			if (committedevents[i].wtype === '1') {
				thtml.push('  <td class="trans_data_header_amount">$' + committedevents[i].amount + ' (Risk)</td>');
			} else {
				thtml.push('  <td class="trans_data_header_amount">$' + committedevents[i].amount + ' (To Win)</td>');
			}
			if (committedevents[i].scrappername !== '') {
				thtml.push('  <td class="trans_data_header_source">' + committedevents[i].scrappername + '</td>');
			} else {
				thtml.push('  <td class="trans_data_header_source">&nbsp;</td>');
			}
			if (committedevents[i].actiontype !== '') {
				thtml.push('  <td class="trans_data_header_play">' + committedevents[i].actiontype + '</td>');				
			} else {
				thtml.push('  <td class="trans_data_header_play">&nbsp;</td>');
			}
			thtml.push('</tr>');
			thtml = $.fn.processAccountEvents(committedevents[i], idValue, thtml, open);
			totalRiskAmount += $.fn.determineTotalRiskAmount(committedevents[i]);
			totalWinAmount += $.fn.determineTotalWinAmount(committedevents[i]);
		}
		thtml.push('  <tr>');
		thtml.push('	<td colspan="4" class="trans_total"><b>Grand Win Total: $' + totalWinAmount + '</b></td>');
		thtml.push('	<td colspan="4" class="trans_total"><b>Grand Risk Total: $' + totalRiskAmount + '</b></td>');
		thtml.push('  </tr>');
		$('#trans-data').html(thtml.join('\n'));
		thtml = [];

    	// Check for + being selected
        $('.trans_data_header').on('click', function(e) {
        	var aid = $(this).attr('id');
        	console.log('aid: ' + aid);
        	var pmvalue = 'pm' + aid;
        	var pm = $('#' + pmvalue).html();
        	if (pm === '+') {
        		$('#sa' + aid).attr( "style", "" );
//        		$('#sa' + aid).css('display', 'block');
        		$('#' + pmvalue).html('-');
        	} else {
        		$('#sa' + aid).attr( "style", "display: none;" );
 //       		$('#sa' + aid).css('display', 'none');
        		$('#' + pmvalue).html('+');
        	}
        });
	}
}

$.fn.isDST = function(t) { //t is the date object to check, returns true if daylight saving time is in effect.
    var jan = new Date(t.getFullYear(),0,1);
    var jul = new Date(t.getFullYear(),6,1);
    return Math.min(jan.getTimezoneOffset(),jul.getTimezoneOffset()) == t.getTimezoneOffset();  
}

// Get all of the transactions
$.fn.getAllTransactions = function() {
	// Call the service to get all accounts
	$.fn.serviceCall('GET', '', 'restapi/account/userid/' + uId, 45000, function(data) {
		var optionString = [];
		optionString.push('<option value="0" selected>All</option>');
	    for (var i=0; (typeof data != 'undefined') && (data != null) && (i < data.length); ++i)
	    {
	    	optionString.push('<option value="' + data[i].id + '">' + data[i].name + '</option>');
	    }
	    $('#transaccount').html(optionString.join('\n'))
	});

	// Call the service to get all group
	$.fn.serviceCall('GET', '', 'restapi/group/userid/' + uId, 45000, function(data) {
		var optionString = [];
		optionString.push('<option value="0" selected>All</option>');
	    for (var i=0; (typeof data != 'undefined') && (data != null) && (i < data.length); ++i)
	    {
	    		optionString.push('<option value="' + data[i].id + '">' + data[i].name + '</option>');
	    }
	    $('#transgroup').html(optionString.join('\n'))
	});

	var today = new Date();
	var todayDay = today.getDay();
	var startOfWeek = new Date();
	var thisWeek = new Date();

	if (todayDay === 0) {
		// Sunday - For us it's the last day of the week
		startOfWeek.setDate(today.getDate());
		thisWeek.setDate(startOfWeek.getDate() - 6);
	} else if (todayDay === 1) {
		// Monday
		startOfWeek.setDate(today.getDate());
		thisWeek.setDate(startOfWeek.getDate() - 0);
	} else if (todayDay === 2) {
		// Tuesday
		startOfWeek.setDate(today.getDate());
		thisWeek.setDate(startOfWeek.getDate() - 1);
	} else if (todayDay === 3) {
		// Wednesday
		startOfWeek.setDate(today.getDate());
		thisWeek.setDate(startOfWeek.getDate() - 2);
	} else if (todayDay === 4) {
		// Thursday
		startOfWeek.setDate(today.getDate());
		thisWeek.setDate(startOfWeek.getDate() - 3);
	} else if (todayDay === 5) {
		// Friday
		startOfWeek.setDate(today.getDate());
		thisWeek.setDate(startOfWeek.getDate() - 4);
	} else if (todayDay === 6) {
		// Saturday
		startOfWeek.setDate(today.getDate());
		thisWeek.setDate(startOfWeek.getDate() - 5);
	}
	var twoWeeks = new Date();
	twoWeeks.setDate(thisWeek.getDate() - 7);
	var threeWeeks = new Date();
	threeWeeks.setDate(thisWeek.getDate() - 14);
	var fourWeeks = new Date();
	fourWeeks.setDate(thisWeek.getDate() - 21);
	var fiveWeeks = new Date();
	fiveWeeks.setDate(thisWeek.getDate() - 28);

	var tz = '';
	var timezn = today.getTimezoneOffset()/60;
	
	if ($.fn.isDST(today)) {
		if (timezn == 4) {
			tz = 'ET';
		} else if (timezn === 5) {
			tz = 'CT';
		} else if (timezn === 6) {
			tz = 'MT';
		} else if (timezn === 7) {
			tz = 'PT';
		} else {
			tz = 'ET';
		}
	} else {
		if (timezn == 5) {
			tz = 'ET';
		} else if (timezn === 6) {
			tz = 'CT';
		} else if (timezn === 7) {
			tz = 'MT';
		} else if (timezn === 8) {
			tz = 'PT';
		} else {
			tz = 'ET';
		} 
	}

	var dataOptions = '';
	dataOptions += '<option value="' + (thisWeek.getMonth()+1) + "-" + thisWeek.getDate() + "-" + thisWeek.getFullYear() + ' ' + tz + ',' + (today.getMonth()+1) + "-" + today.getDate() + "-" + today.getFullYear() + ' ' + tz + '">This Week</option>';
	var n = dataOptions.indexOf(",");
	if (n != -1) {
		$("#fromdate").val((thisWeek.getMonth()+1) + "-" + thisWeek.getDate() + "-" + thisWeek.getFullYear() + ' ' + tz);
		$("#todate").val((today.getMonth()+1) + "-" + today.getDate() + "-" + today.getFullYear() + ' ' + tz);
	}
	var tempDate = new Date();
	tempDate.setDate(startOfWeek.getDate() - (todayDay));
	dataOptions += '<option value="' + (twoWeeks.getMonth()+1) + "-" + twoWeeks.getDate() + "-" + twoWeeks.getFullYear() + ' ' + tz + ',' + (tempDate.getMonth()+1) + "-" + tempDate.getDate() + "-" + tempDate.getFullYear() + ' ' + tz + '">Last Week</option>';
	tempDate = new Date();
	tempDate.setDate(thisWeek.getDate() - 8);
	dataOptions += '<option value="' + (threeWeeks.getMonth()+1) + "-" + threeWeeks.getDate() + "-" + threeWeeks.getFullYear() + ' ' + tz + ',' + (tempDate.getMonth()+1) + "-" + tempDate.getDate() + "-" + tempDate.getFullYear() + ' ' + tz + '">2 Weeks Ago</option>';
	var tempDate = new Date();
	tempDate.setDate(thisWeek.getDate() - 15);
	dataOptions += '<option value="' + (fourWeeks.getMonth()+1) + "-" + fourWeeks.getDate() + "-" + fourWeeks.getFullYear() + ' ' + tz + ',' + (tempDate.getMonth()+1) + "-" + tempDate.getDate() + "-" + tempDate.getFullYear() + ' ' + tz + '">3 Weeks Ago</option>';
	tempDate = new Date();
	tempDate.setDate(thisWeek.getDate() - 22);
	dataOptions += '<option value="' + (fiveWeeks.getMonth()+1) + "-" + fiveWeeks.getDate() + "-" + fiveWeeks.getFullYear() + ' ' + tz + ',' + (tempDate.getMonth()+1) + "-" + tempDate.getDate() + "-" + tempDate.getFullYear() + ' ' + tz + '">4 Weeks Ago</option>';
	dataOptions += '<option value="-99">Entered Dates</option>';
	$('#transfilter').html(dataOptions);

	$("#todate").attr("disabled", "disabled"); 
	$("#fromdate").attr("disabled", "disabled");

	$('#todate').datetimepicker({
	  onClose:function(dateText, inst) {
		  var tz = '';
		  var timezn = today.getTimezoneOffset()/60;
			if ($.fn.isDST(today)) {
				if (timezn == 4) {
					tz = 'ET';
				} else if (timezn === 5) {
					tz = 'CT';
				} else if (timezn === 6) {
					tz = 'MT';
				} else if (timezn === 7) {
					tz = 'PT';
				} else {
					tz = 'ET';
				}
			} else {
				if (timezn == 5) {
					tz = 'ET';
				} else if (timezn === 6) {
					tz = 'CT';
				} else if (timezn === 7) {
					tz = 'MT';
				} else if (timezn === 8) {
					tz = 'PT';
				} else {
					tz = 'ET';
				}
			}
		  dateText = dateText + ' ' + tz;
		  $('#todate').val(dateText);	  
	  },
		timeFormat : "",
		dateFormat: 'mm-dd-yy',
		numberOfMonths : 1,
		minDate: '-6m',
		maxDate : 30
	});

	$('#fromdate').datetimepicker({
	  onClose:function(dateText, inst) {
		  var tz = '';
		  var timezn = today.getTimezoneOffset()/60;
			if ($.fn.isDST(today)) {
				if (timezn == 4) {
					tz = 'ET';
				} else if (timezn === 5) {
					tz = 'CT';
				} else if (timezn === 6) {
					tz = 'MT';
				} else if (timezn === 7) {
					tz = 'PT';
				} else {
					tz = 'ET';
				}
			} else {
				if (timezn == 5) {
					tz = 'ET';
				} else if (timezn === 6) {
					tz = 'CT';
				} else if (timezn === 7) {
					tz = 'MT';
				} else if (timezn === 8) {
					tz = 'PT';
				} else {
					tz = 'ET';
				}
			}
		  dateText = dateText + ' ' + tz;
		  $('#fromdate').val(dateText);
	  },
		timeFormat : "",
		dateFormat: 'mm-dd-yy',
		numberOfMonths : 1,
		minDate: '-6m',
		maxDate : 30
	});

	// Call the service
	$.fn.serviceCall('GET', '', 'restapi/committedevents/userbyfilter?userid=' + uId + '&fromdate=' + $('#fromdate').val() + '&todate=' + $('#todate').val() + '&groupid=0&accountid=0', 90000, function(data) {
		$.fn.setupTransactions(data, false);		
	});

	$('#trans-view').on('click', function() {
		$('#trans-data').html('');

		// Call the service
		$.fn.serviceCall('GET', '', 'restapi/committedevents/userbyfilter?userid=' + uId + '&fromdate=' + $('#fromdate').val() + '&todate=' + $('#todate').val() + '&groupid=' + $('#transgroup').val() + '&accountid=' + $('#transaccount').val(), 90000, function(data) {
			$.fn.setupTransactions(data, false);
		});
	});

	$('#trans-export').on('click', function() {			
		var token = Math.random().toString(36).substr(2) + Math.random().toString(36).substr(2);
		document.cookie = 'CSRF-TOKEN=' + token + '; path=/';
		var xhr = new XMLHttpRequest();
		xhr.open('GET', 'restapi/committedevents/excelfilter?userid=' + uId + '&fromdate=' + $('#fromdate').val() + '&todate=' + $('#todate').val() + '&groupid=' + $('#transgroup').val() + '&accountid=' + $('#transaccount').val(), true);
		xhr.setRequestHeader('X-CSRF-TOKEN', token);
		xhr.setRequestHeader('Authorization', basicAuth);
		xhr.setRequestHeader('Content-Type', 'application/json');

		// You should set responseType as blob for binary responses
		xhr.responseType = 'blob';

		xhr.onload = function(e) {
		  if (this.status == 200) {
		    // get binary data as a response
		    var blob = this.response;
	        // Trick for making download-able link
	        a = document.createElement('a');
	        a.href = window.URL.createObjectURL(this.response);
	        // Give filename you wish to download
	        a.download = "TransactionsReport.xls";
	        a.style.display = 'none';
	        document.body.appendChild(a);
	        a.click();
		  }
		};			 
		xhr.send();
	});

	$('#trans-export-all').on('click', function() {			
		var token = Math.random().toString(36).substr(2) + Math.random().toString(36).substr(2);
		document.cookie = 'CSRF-TOKEN=' + token + '; path=/';
		var xhr = new XMLHttpRequest();
		xhr.open('GET', 'restapi/committedevents/excelfilterall?userid=' + uId + '&fromdate=' + $('#fromdate').val() + '&todate=' + $('#todate').val() + '&groupid=' + $('#transgroup').val() + '&accountid=' + $('#transaccount').val(), true);
		xhr.setRequestHeader('X-CSRF-TOKEN', token);
		xhr.setRequestHeader('Authorization', basicAuth);
		xhr.setRequestHeader('Content-Type', 'application/json');

		// You should set responseType as blob for binary responses
		xhr.responseType = 'blob';

		xhr.onload = function(e) {
		  if (this.status == 200) {
		    // get binary data as a response
		    var blob = this.response;
	        // Trick for making download-able link
	        a = document.createElement('a');
	        a.href = window.URL.createObjectURL(this.response);
	        // Give filename you wish to download
	        a.download = "TransactionsReport.xls";
	        a.style.display = 'none';
	        document.body.appendChild(a);
	        a.click();
		  }
		};			 
		xhr.send();
	});

	$('#transfilter').on('change', function() {
		var value = $(this).find("option:selected").attr("value");
		if (value === '-99') {
			$("#todate").removeAttr("disabled");
			$("#fromdate").removeAttr("disabled");
			$("#todate").val('');
			$("#fromdate").val('');
		} else {
			var n = value.indexOf(",");
			$("#todate").attr("disabled", "disabled"); 
			$("#fromdate").attr("disabled", "disabled");
			var n = value.indexOf(",");
			if (n != -1) {
				$("#fromdate").val(value.substring(0, n));
				$("#todate").val(value.substring(n+1));
			}
		}
	});
}