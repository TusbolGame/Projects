var scrapperInfo = null;
var showScrapper = [];
var pendingRetry;
var idSelected;
var selectedSourceList = [];
var selectedDestinationList = [];

// Get all of the transactions
$.fn.getPendingBets = function() {
	$('#play-audio').trigger('stop');
	// Call the service to get all pending transactions
	$.fn.serviceCallNoSpinnerNoError('GET', '', 'restapi/scrapper/id/' + uId, 45000, function(data) {
		if (typeof scrapperInfo !== 'undefined' && scrapperInfo != null && typeof data !== 'undefined' && data != null) {
			// Now compare the two
			var ctr = 0;
			for (var i = 0; i < data.length; i++) {
				showScrapper = [];
				var dFound = false;
				for (var x = 0; x < scrapperInfo.length; x++) {
					var match = false;
					if ((data[i].accountId === scrapperInfo[x].accountId) &&
						(data[i].wagerline === scrapperInfo[x].wagerline) &&
						(data[i].team === scrapperInfo[x].team) &&
						(data[i].risk === scrapperInfo[x].risk) &&
						(data[i].win === scrapperInfo[x].win)) {
						match = true;
					}

					if (match) {
						showScrapper[ctr++] = data[i];
						dFound = true;
					}
				}
				
				// Do we have a new one?
				if (!dFound) {
					log('showScrapper: ' + data[i]);
					// Must be new, do a "ding"
					showScrapper[ctr++] = data[i];
					$('#play-audio').trigger('play');
				}
			}
		}
		scrapperInfo = data;
	});
}

//Get all of the transactions
$.fn.showPending = function() {
	// Call the service to get all pending transactions
	$.fn.serviceCallNoSpinnerNoError('GET', '', 'restapi/scrapper/id/' + uId, 45000, function(data) {
		if (typeof data !== 'undefined' && data != null) {
			var thtml = [];
			for (var i = 0; i < data.length; i++) {
				var oddeven = i % 2;
				if (oddeven === 0) {
					thtml.push('<div id="scrapper-data-header" class="scrapper_data_header">');
				} else {
					thtml.push('<div id="scrapper-data-header" class="scrapper_data_header scrapper_data_odd">');
				}
				thtml.push('<div class="scrapper_data_header_accountid">' + data[i].accountId + '</div>');
				thtml.push('<div class="scrapper_data_header_team">' + data[i].team + '</div>');
				thtml.push('<div class="scrapper_data_header_wager">' + data[i].wagerline + '</div>');
				thtml.push('<div class="scrapper_data_header_risk">' + data[i].risk + '</div>');
				thtml.push('<div class="scrapper_data_header_win">' + data[i].win + '</div>');
				thtml.push('</div>');
			}
			$('#scrapper-data').html(thtml.join('\n'));
			thtml = [];
		}
	});
}

$.fn.initialDelay = function() {
//	clearInterval(startDelay);
//	pendingRetry = setInterval($.fn.getPendingBets, 30000);
}

$.fn.setupWebAccountSelect = function(scrapper) {
	// Remove the current list
	$.fn.serviceCall('GET', '', 'restapi/account/userid/' + uId, 45000, function(data) {
		var liString = [];
	
		// Loop through all of the accounts
	    for (var i=0; (typeof data != 'undefined') && (data != null) && (i < data.length); ++i) {
		    	liString.push('<div id="acctdiv' + data[i].id + '" class="acctdiv">');
		    	liString.push('  <input id="src' + data[i].id + '" onclick="$.fn.determineWebSourceAction(' + scrapper.id + ', ' + data[i].id + ');" type="checkbox" value="' + data[i].id + '" /><span class="acctdivspan">' + data[i].name + '</span>');	
		    	liString.push('</div>');
	    }
		$('#source-account-list').html(liString.join('\n'));

		// Now map the source, destination and middle (if any)
		$.fn.mapWebSourceAccts(scrapper.sources);

		liString = [];
		// Loop through all of the accounts
	    for (var i=0; (typeof data != 'undefined') && (data != null) && (i < data.length); ++i) {
		    	liString.push('<div id="acctdiv' + data[i].id + '" class="acctdiv">');
		    	liString.push('  <input id="dest' + data[i].id + '" onclick="$.fn.determineWebDestinationAction(' + scrapper.id + ', ' + data[i].id + ');" type="checkbox" value="' + data[i].id + '" /><span class="acctdivspan">' + data[i].name + '</span>');	
		    	liString.push('</div>');
	    }
		$('#destination-account-list').html(liString.join('\n'));
		$.fn.mapWebDestinationAccts(scrapper.destinations);

		liString = [];
		// Loop through all of the accounts
	    for (var i=0; (typeof data != 'undefined') && (data != null) && (i < data.length); ++i) {
		    	liString.push('<div id="acctdiv' + data[i].id + '" class="acctdiv">');
		    	liString.push('  <input id="mid' + data[i].id + '" onclick="$.fn.determineWebMiddleAction(' + scrapper.id + ', ' + data[i].id + ');" type="checkbox" value="' + data[i].id + '" /><span class="acctdivspan">' + data[i].name + '</span>');	
		    	liString.push('</div>');
	    }
		$('#middle-account-list').html(liString.join('\n'));
		$.fn.mapWebMiddleAccts(scrapper.middledestinations);

		liString = [];
		// Loop through all of the accounts
	    for (var i=0; (typeof data != 'undefined') && (data != null) && (i < data.length); ++i) {
		    	liString.push('<div id="acctdiv' + data[i].id + '" class="acctdiv">');
		    	liString.push('  <input id="order' + data[i].id + '" onclick="$.fn.determineWebOrderAction(' + scrapper.id + ', ' + data[i].id + ');" type="checkbox" value="' + data[i].id + '" /><span class="acctdivspan">' + data[i].name + '</span>');	
		    	liString.push('</div>');
	    }
		$('#order-account-list').html(liString.join('\n'));
		$.fn.mapWebOrderAccts(scrapper.orderdestinations);

		liString.length = 0;
	});
}

$.fn.determineWebSourceAction = function(id, accountid) {
	if ($('#src' + accountid).is(':checked')) {
		$.fn.addWebSource(id, accountid);
	} else {
		$.fn.deleteWebSource(id, accountid);
	}
}

$.fn.determineWebDestinationAction = function(id, accountid) {
	if ($('#dest' + accountid).is(':checked')) {
		$.fn.addWebDestination(id, accountid);
	} else {
		$.fn.deleteWebDestination(id, accountid);
	}
}

$.fn.determineWebMiddleAction = function(id, accountid) {
	if ($('#mid' + accountid).is(':checked')) {
		$.fn.addWebMiddle(id, accountid);
	} else {
		$.fn.deleteWebMiddle(id, accountid);
	}	
}

$.fn.determineWebOrderAction = function(id, accountid) {
	if ($('#order' + accountid).is(':checked')) {
		$.fn.addWebOrder(id, accountid);
	} else {
		$.fn.deleteWebOrder(id, accountid);
	}	
}

$.fn.mapWebSourceAccts = function(data) {
/*	$(".acctdiv").each(function() {
		var acct = '';
		var input = $(this).find('input');
		if (input.is(':checked')) {
			if (accountcounter > 0) {
				acct = ',{"id":' + input.attr('value') + '}';
			} else {
				acct = '{"id":' + input.attr('value') + '}';
			}
			accounts += acct;
			accountcounter++;
		}
	});
*/
	$('#source-account-list .acctdiv').each(function () {
		for (var i=0; (typeof data != 'undefined') && (data != null) && (i < data.length); ++i) {
			var srcid = $(this).attr('id');
			var id = srcid.substring(7);
			if (data[i].id.toString() === id) {
				var input = $(this).find('input');
				$(input).prop("checked", true);
			}
		}
	});
}

$.fn.mapWebDestinationAccts = function(data) {
	$('#destination-account-list .acctdiv').each(function () {
		for (var i=0; (typeof data != 'undefined') && (data != null) && (i < data.length); ++i) {
			var srcid = $(this).attr('id');
			var id = srcid.substring(7);
			if (data[i].id.toString() === id) {
				var input = $(this).find('input');
				$(input).prop("checked", true);
			}
		}
	});
}

$.fn.mapWebMiddleAccts = function(data) {
	$('#middle-account-list .acctdiv').each(function () {
		for (var i=0; (typeof data != 'undefined') && (data != null) && (i < data.length); ++i) {
			var srcid = $(this).attr('id');
			var id = srcid.substring(7);
			if (data[i].id.toString() === id) {
				var input = $(this).find('input');
				$(input).prop("checked", true);
			}
		}
	});
}

$.fn.mapWebOrderAccts = function(data) {
	$('#order-account-list .acctdiv').each(function () {
		for (var i=0; (typeof data != 'undefined') && (data != null) && (i < data.length); ++i) {
			var srcid = $(this).attr('id');
			var id = srcid.substring(7);
			if (data[i].id.toString() === id) {
				var input = $(this).find('input');
				$(input).prop("checked", true);
			}
		}
	});
}

//Scrapper Listings
$.fn.getWebScrapperListings = function(id) {
	$.fn.serviceCall('GET', '', 'restapi/scrapper/id/' + id, 45000, function(data) {
    	// Get the account listings
		$.fn.setupWebAccountSelect(data);

		// Check to show middle accounts
		if (data.middlerules) {
			$('#order-accounts').hide();
			$('#destination-accounts').show();
			$('#middle-accounts').show();
		} else if (data.fullfill) {
			$('#order-accounts').show();
			$('#destination-accounts').hide();
			$('#middle-accounts').hide();
		} else if (data.bestprice) {
			$('#order-accounts').show();
			$('#destination-accounts').hide();
			$('#middle-accounts').hide();
		} else {
			$('#order-accounts').hide();
			$('#destination-accounts').show();
			$('#middle-accounts').hide();
		}

		$('#middlerules').on('click', function(e) {
			if ($(this).is(':checked')) {
				$('#middle-accounts').show();
				$('#order-accounts').hide();
			} else {
				$('#middle-accounts').hide();
			}
		});

		$('#fullfill').on('click', function(e) {
			if ($(this).is(':checked')) {
				$('#order-accounts').show();
				$('#middle-accounts').hide();
				$('#destination-accounts').hide();
			} else {
				$('#destination-accounts').show();
			}
		});
		
		$('#bestprice').on('click', function(e) {
			if ($(this).is(':checked')) {
				$('#order-accounts').show();
				$('#middle-accounts').hide();
				$('#destination-accounts').hide();
			} else {
				$('#destination-accounts').show();
			}
		});
	});
}

$.fn.addWebSource = function(id, accountid) {
	$.fn.serviceCall('PUT', '', 'restapi/scrapper/addsource/' + id + '/' + accountid, 45000, function(data) {
	});
}

$.fn.addWebDestination = function(id, accountid) {
	$.fn.serviceCall('PUT', '', 'restapi/scrapper/adddestination/' + id + '/' + accountid, 45000, function(data) {
	});
}

$.fn.addWebMiddle = function(id, accountid) {
	$.fn.serviceCall('PUT', '', 'restapi/scrapper/addmiddle/' + id + '/' + accountid, 45000, function(data) {
	});
}

$.fn.addWebOrder = function(id, accountid) {
	$.fn.serviceCall('PUT', '', 'restapi/scrapper/addorder/' + id + '/' + accountid, 45000, function(data) {
	});
}

$.fn.deleteWebSource = function(id, accountid) {
	$.fn.serviceCall('DELETE', '', 'restapi/scrapper/deletesource/' + id + '/' + accountid, 45000, function(data) {
	});
}

$.fn.deleteWebDestination = function(id, accountid) {
	$.fn.serviceCall('DELETE', '', 'restapi/scrapper/deletedestination/' + id + '/' + accountid, 45000, function(data) {
	});
}

$.fn.deleteWebMiddle = function(id, accountid) {
	$.fn.serviceCall('DELETE', '', 'restapi/scrapper/deletemiddle/' + id + '/' + accountid, 45000, function(data) {
	});
}

$.fn.deleteWebOrder = function(id, accountid) {
	$.fn.serviceCall('DELETE', '', 'restapi/scrapper/deleteorder/' + id + '/' + accountid, 45000, function(data) {
	});
}

// Get all Scrappers for user
$.fn.getWebScrapperList = function() {
	// Clear out the data
	$('#scrappername').val('');
	$('#spreadlineadjustment').val('');
	$("#spreadjuiceindicator option[value='-']").prop('selected', true);
	$('#spreadjuice').val('');
	$('#spreadjuiceadjustment').val('');
	$('#spreadmaxamount').val('');
	$('#spreadsourceamount').val('0');
	$("#totallineadjustment").val('');
	$("#totaljuiceindicator option[value='-']").prop('selected', true);
	$("#totaljuice").val('');
	$("#totaljuiceadjustment").val('');
	$('#totalmaxamount').val('');
	$('#totalsourceamount').val('0');
	$("#mlindicator option[value='-']").prop('selected', true);
	$('#mlline').val('');
	$('#mllineadjustment').val('');
	$('#mlmaxamount').val('');
	$('#mlsourceamount').val('0');
	$('#pullinginterval').val('');
	$('#mobiletext').val('');
	$('#telegramnumber').val('');
	$("#servernumber option[value='2']").prop('selected', true);
	$('#enableretry').prop("checked", false);
	$('#fullfill').prop("checked", false);
	$('#orderamount').val('0');
	$('#middlerules').prop("checked", false);
	$('#checkdupgame').prop("checked", false);
	$('#playotherside').prop("checked", false);
	$('#bestprice').prop("checked", false);
	$('#sendtextforaccount').prop("checked", false);
	$('#sendtextforgame').prop("checked", false);
	$('#onoff').prop("checked", true);
	$('#gameonoff').prop("checked", true);
	$('#firstonoff').prop("checked", true);
	$('#secondonoff').prop("checked", true);
	$('#thirdonoff').prop("checked", true);
	$('#nflspreadonoff').prop("checked", true);
	$('#nfltotalonoff').prop("checked", true);
	$('#nflmlonoff').prop("checked", true);
	$('#ncaafspreadonoff').prop("checked", true);
	$('#ncaaftotalonoff').prop("checked", true);
	$('#ncaafmlonoff').prop("checked", true);
	$('#nbaspreadonoff').prop("checked", true);
	$('#nbatotalonoff').prop("checked", true);
	$('#nbamlonoff').prop("checked", true);
	$('#ncaabspreadonoff').prop("checked", true);
	$('#ncaabtotalonoff').prop("checked", true);
	$('#ncaabmlonoff').prop("checked", true);
	$('#wnbaspreadonoff').prop("checked", true);
	$('#wnbatotalonoff').prop("checked", true);
	$('#wnbamlonoff').prop("checked", true);
	$('#nhlspreadonoff').prop("checked", true);
	$('#nhltotalonoff').prop("checked", true);
	$('#nhlmlonoff').prop("checked", true);
	$('#mlbspreadonoff').prop("checked", true);
	$('#mlbtotalonoff').prop("checked", true);
	$('#mlbmlonoff').prop("checked", true);
	$('#intbaseballspreadonoff').prop("checked", true);
	$('#intbaseballtotalonoff').prop("checked", true);
	$('#intbaseballmlonoff').prop("checked", true);
	$('#keynumber').prop("checked", false);
	$('#humanspeed').prop("checked", false);
	$('#unitsenabled').prop("checked", false);
	$('#spreadunit').val('0');
	$('#totalunit').val('0');
	$('#mlunit').val('0');
	$('#leanssenabled').prop("checked", false);
	$('#spreadlean').val('0');
	$('#totallean').val('0');
	$('#mllean').val('0');

	// Reset the buttons
	$('#scrapper-add-scrapper').show();
	$('#scrapper-update-scrapper').hide();
	$('#scrapper-delete-scrapper').hide();

	// Remove the current list
	$('#scrapper-list-nav').remove();

	// Call the service to get scrapper by id
	$.fn.serviceCall('GET', '', 'restapi/scrapper/userid/' + uId, 45000, function(data) {
    		var liString = '';
        for (var i=0; (typeof data != 'undefined') && (data != null) && (i < data.length); ++i)
        {
        	var odd = i % 2;
        	liString += '<li id=' + data[i].id + '>';
        	liString += '  <input id="' + data[i].scrappername + '_id" type="hidden" value="' + data[i].id + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_scrappername" type="hidden" value="' + data[i].scrappername + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_spreadlineadjustment" type="hidden" value="' + data[i].spreadlineadjustment + '"/>';                        	
        	liString += '  <input id="' + data[i].scrappername + '_spreadjuiceindicator" type="hidden" value="' + data[i].spreadjuiceindicator + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_spreadjuice" type="hidden" value="' + data[i].spreadjuice + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_spreadjuiceadjustment" type="hidden" value="' + data[i].spreadjuiceadjustment + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_spreadmaxamount" type="hidden" value="' + data[i].spreadmaxamount + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_spreadsourceamount" type="hidden" value="' + data[i].spreadsourceamount + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_totallineadjustment" type="hidden" value="' + data[i].totallineadjustment + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_totaljuiceindicator" type="hidden" value="' + data[i].totaljuiceindicator + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_totaljuice" type="hidden" value="' + data[i].totaljuice + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_totaljuiceadjustment" type="hidden" value="' + data[i].totaljuiceadjustment + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_totalmaxamount" type="hidden" value="' + data[i].totalmaxamount + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_totalsourceamount" type="hidden" value="' + data[i].totalsourceamount + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_mlindicator" type="hidden" value="' + data[i].mlindicator + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_mlline" type="hidden" value="' + data[i].mlline + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_mllineadjustment" type="hidden" value="' + data[i].mllineadjustment + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_mlmaxamount" type="hidden" value="' + data[i].mlmaxamount + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_mlsourceamount" type="hidden" value="' + data[i].mlsourceamount + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_pullinginterval" type="hidden" value="' + data[i].pullinginterval + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_mobiletext" type="hidden" value="' + data[i].mobiletext + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_telegramnumber" type="hidden" value="' + data[i].telegramnumber + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_servernumber" type="hidden" value="' + data[i].servernumber + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_enableretry" type="hidden" value="' + data[i].enableretry + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_fullfill" type="hidden" value="' + data[i].fullfill + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_orderamount" type="hidden" value="' + data[i].orderamount + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_middlerules" type="hidden" value="' + data[i].middlerules + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_checkdupgame" type="hidden" value="' + data[i].checkdupgame + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_playotherside" type="hidden" value="' + data[i].playotherside + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_bestprice" type="hidden" value="' + data[i].bestprice + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_sendtextforaccount" type="hidden" value="' + data[i].sendtextforaccount + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_sendtextforgame" type="hidden" value="' + data[i].sendtextforgame + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_onoff" type="hidden" value="' + data[i].onoff + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_gameonoff" type="hidden" value="' + data[i].gameonoff + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_firstonoff" type="hidden" value="' + data[i].firstonoff + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_secondonoff" type="hidden" value="' + data[i].secondonoff + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_thirdonoff" type="hidden" value="' + data[i].thirdonoff + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_nflspreadonoff" type="hidden" value="' + data[i].nflspreadonoff + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_nfltotalonoff" type="hidden" value="' + data[i].nfltotalonoff + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_nflmlonoff" type="hidden" value="' + data[i].nflmlonoff + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_ncaafspreadonoff" type="hidden" value="' + data[i].ncaafspreadonoff + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_ncaaftotalonoff" type="hidden" value="' + data[i].ncaaftotalonoff + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_ncaafmlonoff" type="hidden" value="' + data[i].ncaafmlonoff + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_nbaspreadonoff" type="hidden" value="' + data[i].nbaspreadonoff + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_nbatotalonoff" type="hidden" value="' + data[i].nbatotalonoff + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_nbamlonoff" type="hidden" value="' + data[i].nbamlonoff + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_ncaabspreadonoff" type="hidden" value="' + data[i].ncaabspreadonoff + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_ncaabtotalonoff" type="hidden" value="' + data[i].ncaabtotalonoff + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_ncaabmlonoff" type="hidden" value="' + data[i].ncaabmlonoff + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_wnbaspreadonoff" type="hidden" value="' + data[i].wnbaspreadonoff + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_wnbatotalonoff" type="hidden" value="' + data[i].wnbatotalonoff + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_wnbamlonoff" type="hidden" value="' + data[i].wnbamlonoff + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_nhlspreadonoff" type="hidden" value="' + data[i].nhlspreadonoff + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_nhltotalonoff" type="hidden" value="' + data[i].nhltotalonoff + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_nhlmlonoff" type="hidden" value="' + data[i].nhlmlonoff + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_mlbspreadonoff" type="hidden" value="' + data[i].mlbspreadonoff + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_mlbtotalonoff" type="hidden" value="' + data[i].mlbtotalonoff + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_mlbmlonoff" type="hidden" value="' + data[i].mlbmlonoff + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_intbaseballspreadonoff" type="hidden" value="' + data[i].internationalbaseballspreadonoff + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_intbaseballtotalonoff" type="hidden" value="' + data[i].internationalbaseballtotalonoff + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_intbaseballmlonoff" type="hidden" value="' + data[i].internationalbaseballmlonoff + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_keynumber" type="hidden" value="' + data[i].keynumber + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_humanspeed" type="hidden" value="' + data[i].humanspeed + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_unitsenabled" type="hidden" value="' + data[i].unitsenabled + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_spreadunit" type="hidden" value="' + data[i].spreadunit + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_totalunit" type="hidden" value="' + data[i].totalunit + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_mlunit" type="hidden" value="' + data[i].mlunit + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_leanssenabled" type="hidden" value="' + data[i].leanssenabled + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_spreadlean" type="hidden" value="' + data[i].spreadlean + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_totallean" type="hidden" value="' + data[i].totallean + '"/>';
        	liString += '  <input id="' + data[i].scrappername + '_mllean" type="hidden" value="' + data[i].mllean + '"/>';

        	if (odd) {
        		liString += '  <a id="' + data[i].scrappername + '" class="scrapper_list_odd" href="javascript:void(0);">' + data[i].scrappername  + '</a>';
        	} else {
        		liString += '  <a id="' + data[i].scrappername + '" class="scrapper_list_even" href="javascript:void(0);">' + data[i].scrappername  + '</a>';
        	}
        	liString += '</li>';
        }
    	$('#scrapper-list').html(liString);

    	// Check for row being selected
        $('#scrapper-list a').on('click', function(e) {
        	e.preventDefault();
        	var sid = $(this).attr('id');
        	$("#scrappername").val($("[id='" + sid + "_scrappername']").val());
        	$("#spreadlineadjustment").val($("[id='" + sid + "_spreadlineadjustment']").val());
        	$("#spreadjuiceindicator").val($("[id='" + sid + "_spreadjuiceindicator']").val());
        	$("#spreadjuice").val($("[id='" + sid + "_spreadjuice']").val());
        	$("#spreadjuiceadjustment").val($("[id='" + sid + "_spreadjuiceadjustment']").val());
        	$("#spreadmaxamount").val($("[id='" + sid + "_spreadmaxamount']").val());
        	$("#spreadsourceamount").val($("[id='" + sid + "_spreadsourceamount']").val());
        	$("#totallineadjustment").val($("[id='" + sid + "_totallineadjustment']").val());
        	$("#totaljuiceindicator").val($("[id='" + sid + "_totaljuiceindicator']").val());
        	$("#totaljuice").val($("[id='" + sid + "_totaljuice']").val());
        	$("#totaljuiceadjustment").val($("[id='" + sid + "_totaljuiceadjustment']").val());
        	$("#totalmaxamount").val($("[id='" + sid + "_totalmaxamount']").val());
        	$("#totalsourceamount").val($("[id='" + sid + "_totalsourceamount']").val());
        	$("#mlindicator").val($("[id='" + sid + "_mlindicator']").val());
        	$("#mlline").val($("[id='" + sid + "_mlline']").val());
        	$("#mllineadjustment").val($("[id='" + sid + "_mllineadjustment']").val());
        	$("#mlmaxamount").val($("[id='" + sid + "_mlmaxamount']").val());
        	$("#mlsourceamount").val($("[id='" + sid + "_mlsourceamount']").val());
        	$("#pullinginterval").val($("[id='" + sid + "_pullinginterval']").val());
        	$("#mobiletext").val($("[id='" + sid + "_mobiletext']").val());
        	$("#telegramnumber").val($("[id='" + sid + "_telegramnumber']").val());
        	$("#servernumber").val($("[id='" + sid + "_servernumber']").val());
        	$("#orderamount").val($("[id='" + sid + "_orderamount']").val());

        	var isActive = $("[id='" + sid + "_enableretry']").val();
        	var active = true;
        	if (typeof isActive != 'undefined' && isActive != '' && isActive === 'false') {
        		active = false;
        	}
        	$("#enableretry").prop("checked", active);

        	isActive = $("[id='" + sid + "_fullfill']").val();
        	active = true;
        	if (typeof isActive != 'undefined' && isActive != '' && isActive === 'false') {
        		active = false;
        	}
        	$("#fullfill").prop("checked", active);

        	isActive = $("[id='" + sid + "_middlerules']").val();
        	active = true;
        	if (typeof isActive != 'undefined' && isActive != '' && isActive === 'false') {
        		active = false;
        	}
        	$("#middlerules").prop("checked", active);

        	isActive = $("[id='" + sid + "_checkdupgame']").val();
        	active = true;
        	if (typeof isActive != 'undefined' && isActive != '' && isActive === 'false') {
        		active = false;
        	}
        	$("#checkdupgame").prop("checked", active);

        	isActive = $("[id='" + sid + "_playotherside']").val();
        	active = true;
        	if (typeof isActive != 'undefined' && isActive != '' && isActive === 'false') {
        		active = false;
        	}
        	$("#playotherside").prop("checked", active);

        	isActive = $("[id='" + sid + "_bestprice']").val();
        	active = true;
        	if (typeof isActive != 'undefined' && isActive != '' && isActive === 'false') {
        		active = false;
        	}
        	$("#bestprice").prop("checked", active);

        	isActive = $("[id='" + sid + "_sendtextforaccount']").val();
        	active = true;
        	if (typeof isActive != 'undefined' && isActive != '' && isActive === 'false') {
        		active = false;
        	}
        	$("#sendtextforaccount").prop("checked", active);

        	isActive = $("[id='" + sid + "_sendtextforgame']").val();
        	active = true;
        	if (typeof isActive != 'undefined' && isActive != '' && isActive === 'false') {
        		active = false;
        	}
        	$("#sendtextforgame").prop("checked", active);

        	isActive = $("[id='" + sid + "_onoff']").val();
        	active = true;
        	if (typeof isActive != 'undefined' && isActive != '' && isActive === 'false') {
        		active = false;
        	}
        	$("#onoff").prop("checked", active);

        	isActive = $("[id='" + sid + "_gameonoff']").val();
        	active = true;
        	if (typeof isActive != 'undefined' && isActive != '' && isActive === 'false') {
        		active = false;
        	}
        	$("#gameonoff").prop("checked", active);

        	isActive = $("[id='" + sid + "_firstonoff']").val();
        	active = true;
        	if (typeof isActive != 'undefined' && isActive != '' && isActive === 'false') {
        		active = false;
        	}
        	$("#firstonoff").prop("checked", active);

        	isActive = $("[id='" + sid + "_secondonoff']").val();
        	active = true;
        	if (typeof isActive != 'undefined' && isActive != '' && isActive === 'false') {
        		active = false;
        	}
        	$("#secondonoff").prop("checked", active);

        	isActive = $("[id='" + sid + "_thirdonoff']").val();
        	active = true;
        	if (typeof isActive != 'undefined' && isActive != '' && isActive === 'false') {
        		active = false;
        	}
        	$("#thirdonoff").prop("checked", active);

        	isActive = $("[id='" + sid + "_nflspreadonoff']").val();
        	active = true;
        	if (typeof isActive != 'undefined' && isActive != '' && isActive === 'false') {
        		active = false;
        	}
        	$("#nflspreadonoff").prop("checked", active);

        	isActive = $("[id='" + sid + "_nfltotalonoff']").val();
        	active = true;
        	if (typeof isActive != 'undefined' && isActive != '' && isActive === 'false') {
        		active = false;
        	}
        	$("#nfltotalonoff").prop("checked", active);

        	isActive = $("[id='" + sid + "_nflmlonoff']").val();
        	active = true;
        	if (typeof isActive != 'undefined' && isActive != '' && isActive === 'false') {
        		active = false;
        	}
        	$("#nflmlonoff").prop("checked", active);

        	isActive = $("[id='" + sid + "_ncaafspreadonoff']").val();
        	active = true;
        	if (typeof isActive != 'undefined' && isActive != '' && isActive === 'false') {
        		active = false;
        	}
        	$("#ncaafspreadonoff").prop("checked", active);

        	isActive = $("[id='" + sid + "_ncaaftotalonoff']").val();
        	active = true;
        	if (typeof isActive != 'undefined' && isActive != '' && isActive === 'false') {
        		active = false;
        	}
        	$("#ncaaftotalonoff").prop("checked", active);

        	isActive = $("[id='" + sid + "_ncaafmlonoff']").val();
        	active = true;
        	if (typeof isActive != 'undefined' && isActive != '' && isActive === 'false') {
        		active = false;
        	}
        	$("#ncaafmlonoff").prop("checked", active);

        	isActive = $("[id='" + sid + "_nbaspreadonoff']").val();
        	active = true;
        	if (typeof isActive != 'undefined' && isActive != '' && isActive === 'false') {
        		active = false;
        	}
        	$("#nbaspreadonoff").prop("checked", active);

        	isActive = $("[id='" + sid + "_nbatotalonoff']").val();
        	active = true;
        	if (typeof isActive != 'undefined' && isActive != '' && isActive === 'false') {
        		active = false;
        	}
        	$("#nbatotalonoff").prop("checked", active);

        	isActive = $("[id='" + sid + "_nbamlonoff']").val();
        	active = true;
        	if (typeof isActive != 'undefined' && isActive != '' && isActive === 'false') {
        		active = false;
        	}
        	$("#nbamlonoff").prop("checked", active);

        	isActive = $("[id='" + sid + "_ncaabspreadonoff']").val();
        	active = true;
        	if (typeof isActive != 'undefined' && isActive != '' && isActive === 'false') {
        		active = false;
        	}
        	$("#ncaabspreadonoff").prop("checked", active);

        	isActive = $("[id='" + sid + "_ncaabtotalonoff']").val();
        	active = true;
        	if (typeof isActive != 'undefined' && isActive != '' && isActive === 'false') {
        		active = false;
        	}
        	$("#ncaabtotalonoff").prop("checked", active);

        	isActive = $("[id='" + sid + "_ncaabmlonoff']").val();
        	active = true;
        	if (typeof isActive != 'undefined' && isActive != '' && isActive === 'false') {
        		active = false;
        	}
        	$("#ncaabmlonoff").prop("checked", active);

        	isActive = $("[id='" + sid + "_wnbaspreadonoff']").val();
        	active = true;
        	if (typeof isActive != 'undefined' && isActive != '' && isActive === 'false') {
        		active = false;
        	}
        	$("#wnbaspreadonoff").prop("checked", active);

        	isActive = $("[id='" + sid + "_wnbatotalonoff']").val();
        	active = true;
        	if (typeof isActive != 'undefined' && isActive != '' && isActive === 'false') {
        		active = false;
        	}
        	$("#wnbatotalonoff").prop("checked", active);

        	isActive = $("[id='" + sid + "_wnbamlonoff']").val();
        	active = true;
        	if (typeof isActive != 'undefined' && isActive != '' && isActive === 'false') {
        		active = false;
        	}
        	$("#wnbamlonoff").prop("checked", active);

        	isActive = $("[id='" + sid + "_nhlspreadonoff']").val();
        	active = true;
        	if (typeof isActive != 'undefined' && isActive != '' && isActive === 'false') {
        		active = false;
        	}
        	$("#nhlspreadonoff").prop("checked", active);

        	isActive = $("[id='" + sid + "_nhltotalonoff']").val();
        	active = true;
        	if (typeof isActive != 'undefined' && isActive != '' && isActive === 'false') {
        		active = false;
        	}
        	$("#nhltotalonoff").prop("checked", active);

        	isActive = $("[id='" + sid + "_nhlmlonoff']").val();
        	active = true;
        	if (typeof isActive != 'undefined' && isActive != '' && isActive === 'false') {
        		active = false;
        	}
        	$("#nhlmlonoff").prop("checked", active);

        	isActive = $("[id='" + sid + "_mlbspreadonoff']").val();
        	active = true;
        	if (typeof isActive != 'undefined' && isActive != '' && isActive === 'false') {
        		active = false;
        	}
        	$("#mlbspreadonoff").prop("checked", active);

        	isActive = $("[id='" + sid + "_mlbtotalonoff']").val();
        	active = true;
        	if (typeof isActive != 'undefined' && isActive != '' && isActive === 'false') {
        		active = false;
        	}
        	$("#mlbtotalonoff").prop("checked", active);

        	isActive = $("[id='" + sid + "_mlbmlonoff']").val();
        	active = true;
        	if (typeof isActive != 'undefined' && isActive != '' && isActive === 'false') {
        		active = false;
        	}
        	$("#mlbmlonoff").prop("checked", active);

        	isActive = $("[id='" + sid + "_intbaseballspreadonoff']").val();
        	active = true;
        	if (typeof isActive != 'undefined' && isActive != '' && isActive === 'false') {
        		active = false;
        	}
        	$("#intbaseballspreadonoff").prop("checked", active);

        	isActive = $("[id='" + sid + "_intbaseballtotalonoff']").val();
        	active = true;
        	if (typeof isActive != 'undefined' && isActive != '' && isActive === 'false') {
        		active = false;
        	}
        	$("#intbaseballtotalonoff").prop("checked", active);

        	isActive = $("[id='" + sid + "_intbaseballmlonoff']").val();
        	active = true;
        	if (typeof isActive != 'undefined' && isActive != '' && isActive === 'false') {
        		active = false;
        	}
        	$("#intbaseballmlonoff").prop("checked", active);

        	isActive = $("[id='" + sid + "_keynumber']").val();
        	active = true;
        	if (typeof isActive != 'undefined' && isActive != '' && isActive === 'false') {
        		active = false;
        	}
        	$("#keynumber").prop("checked", active);

        	isActive = $("[id='" + sid + "_humanspeed']").val();
        	active = true;
        	if (typeof isActive != 'undefined' && isActive != '' && isActive === 'false') {
        		active = false;
        	}
        	$("#humanspeed").prop("checked", active);

        	isActive = $("[id='" + sid + "_unitsenabled']").val();
        	active = true;
        	if (typeof isActive != 'undefined' && isActive != '' && isActive === 'false') {
        		active = false;
        	}
        	$("#unitsenabled").prop("checked", active);

        	$("#spreadunit").val($("[id='" + sid + "_spreadunit']").val());
        	$("#totalunit").val($("[id='" + sid + "_totalunit']").val());
        	$("#mlunit").val($("[id='" + sid + "_mlunit']").val());

        	isActive = $("[id='" + sid + "_leanssenabled']").val();
        	active = true;
        	if (typeof isActive != 'undefined' && isActive != '' && isActive === 'false') {
        		active = false;
        	}
        	$("#leanssenabled").prop("checked", active);

        	$("#spreadlean").val($("[id='" + sid + "_spreadlean']").val());
        	$("#totallean").val($("[id='" + sid + "_totallean']").val());
        	$("#mllean").val($("[id='" + sid + "_mllean']").val());

        	$("#scrappernameid").val(sid);
        	$("#button-add-scrapper").hide();
        	$("#button-update-scrapper").show();
        	$("#button-delete-scrapper").show();
        	
        	idSelected = $("[id='" + sid + "_id']").val();
        	$.fn.getWebScrapperListings(idSelected);
        });

        // List nav
		$('#scrapper-list').listnav();
	});
}

// Function to add a scrapper
$.fn.addWebScrapper = function() {
	if ($('#scrappername').val() === '' ||
		$('#spreadlineadjustment').val() === '' || 
		$('#spreadmaxamount').val() === '' ||  
		$("#totallineadjustment").val() === '' ||
		$('#totalmaxamount').val() === '' || 
		$('#mlmaxamount').val() === '' ||
		$('#pullinginterval').val() === '' ||
		$('#onoff').val() === '')
	{
		$('#background').addClass('background_on');
		$('#error-body').html('Please enter all data into fields');
    		$('#error-message').addClass('error_message_on');
	} else {
		var spreadjuiceindicator = $('#spreadjuiceindicator').find(':selected').val();
		var totaljuiceindicator = $('#totaljuiceindicator').find(':selected').val();
		var mlindicator = $('#mlindicator').find(':selected').val();

		// Call the service to add an account
		var scrapperObject = '{"userid":' + uId +
			',"scrappername":"' + $('#scrappername').val() +
			'","spreadlineadjustment":"' + $('#spreadlineadjustment').val() + 
			'","spreadjuiceindicator":"' + spreadjuiceindicator + 
			'","spreadjuice":"' + $('#spreadjuice').val() +
			'","spreadjuiceadjustment":"' + $('#spreadjuiceadjustment').val() +
			'","spreadmaxamount":"' + $('#spreadmaxamount').val() +
			'","spreadsourceamount":"' + $('#spreadsourceamount').val() +
			'","totallineadjustment":"' + $('#totallineadjustment').val() + 
			'","totaljuiceindicator":"' + totaljuiceindicator + 
			'","totaljuice":"' + $('#totaljuice').val() +
			'","totaljuiceadjustment":"' + $('#totaljuiceadjustment').val() +
			'","totalmaxamount":"' + $('#totalmaxamount').val() +
			'","totalsourceamount":"' + $('#totalsourceamount').val() +
			'","mlindicator":"' + mlindicator +
			'","mlline":"' + $('#mlline').val() +
			'","mllineadjustment":"' + $('#mllineadjustment').val() + 
			'","mlmaxamount":"' + $('#mlmaxamount').val() +
			'","mlsourceamount":"' + $('#mlsourceamount').val() +
			'","pullinginterval":"' + $('#pullinginterval').val() +
			'","mobiletext":"' + $('#mobiletext').val() +
			'","telegramnumber":"' + $('#telegramnumber').val() +
			'","servernumber":' + $('#servernumber').val() +
			',"enableretry":' + $.fn.setupBoolean('enableretry') +
			',"fullfill":' + $.fn.setupBoolean('fullfill') +
			',"orderamount":' + $('#orderamount').val() +
			',"middlerules":' + $.fn.setupBoolean('middlerules') +
			',"checkdupgame":' + $.fn.setupBoolean('checkdupgame') +
			',"playotherside":' + $.fn.setupBoolean('playotherside') +
			',"bestprice":' + $.fn.setupBoolean('bestprice') +
			',"sendtextforaccount":' + $.fn.setupBoolean('sendtextforaccount') +
			',"sendtextforgame":' + $.fn.setupBoolean('sendtextforgame') +
			',"onoff":' + $.fn.setupBoolean('onoff') +  
			',"gameonoff":' + $.fn.setupBoolean('gameonoff') +  
			',"firstonoff":' + $.fn.setupBoolean('firstonoff') +  
			',"secondonoff":' + $.fn.setupBoolean('secondonoff') +  
			',"thirdonoff":' + $.fn.setupBoolean('thirdonoff') +  
			',"nflspreadonoff":' + $.fn.setupBoolean('nflspreadonoff') +  
			',"nfltotalonoff":' + $.fn.setupBoolean('nfltotalonoff') +  
			',"nflmlonoff":' + $.fn.setupBoolean('nflmlonoff') +  
			',"ncaafspreadonoff":' + $.fn.setupBoolean('ncaafspreadonoff') +  
			',"ncaaftotalonoff":' + $.fn.setupBoolean('ncaaftotalonoff') +  
			',"ncaafmlonoff":' + $.fn.setupBoolean('ncaafmlonoff') +  
			',"nbaspreadonoff":' + $.fn.setupBoolean('nbaspreadonoff') +  
			',"nbatotalonoff":' + $.fn.setupBoolean('nbatotalonoff') +  
			',"nbamlonoff":' + $.fn.setupBoolean('nbamlonoff') +  
			',"ncaabspreadonoff":' + $.fn.setupBoolean('ncaabspreadonoff') +  
			',"ncaabtotalonoff":' + $.fn.setupBoolean('ncaabtotalonoff') +  
			',"ncaabmlonoff":' + $.fn.setupBoolean('ncaabmlonoff') +  
			',"wnbaspreadonoff":' + $.fn.setupBoolean('wnbaspreadonoff') +  
			',"wnbatotalonoff":' + $.fn.setupBoolean('wnbatotalonoff') +  
			',"wnbamlonoff":' + $.fn.setupBoolean('wnbamlonoff') +  
			',"nhlspreadonoff":' + $.fn.setupBoolean('nhlspreadonoff') +  
			',"nhltotalonoff":' + $.fn.setupBoolean('nhltotalonoff') +  
			',"nhlmlonoff":' + $.fn.setupBoolean('nhlmlonoff') +  
			',"mlbspreadonoff":' + $.fn.setupBoolean('mlbspreadonoff') +  
			',"mlbtotalonoff":' + $.fn.setupBoolean('mlbtotalonoff') +  
			',"mlbmlonoff":' + $.fn.setupBoolean('mlbmlonoff') + 
			',"internationalbaseballspreadonoff":' + $.fn.setupBoolean('intbaseballspreadonoff') +  
			',"internationalbaseballtotalonoff":' + $.fn.setupBoolean('intbaseballtotalonoff') +  
			',"internationalbaseballmlonoff":' + $.fn.setupBoolean('intbaseballmlonoff') + 
			',"keynumber":' + $.fn.setupBoolean('keynumber') + 
			',"humanspeed":' + $.fn.setupBoolean('humanspeed') +
			',"unitsenabled":' + $.fn.setupBoolean('unitsenabled') +
			',"spreadunit":' + $('#spreadunit').val() +
			',"totalunit":' + $('#totalunit').val() +
			',"mlunit":' + $('#mlunit').val() +
			',"leanssenabled":' + $.fn.setupBoolean('leanssenabled') +
			',"spreadlean":' + $('#spreadlean').val() +
			',"totallean":' + $('#totallean').val() +
			',"mllean":' + $('#mllean').val() + '}';

		log(scrapperObject);
		$.fn.serviceCall('POST', scrapperObject, 'restapi/scrapper/add', 45000, function(data) {
            $.fn.getWebScrapperList();
		});
	}
}

// Function to update an account
$.fn.updateWebScrapper = function() {
	if ($('#scrappername').val() === '' ||
		$('#spreadlineadjustment').val() === '' || 
		$('#spreadmaxamount').val() === '' ||  
	    $("#totallineadjustment").val() === '' ||
		$('#totalmaxamount').val() === '' || 
		$('#mlmaxamount').val() === '' ||
		$('#pullinginterval').val() === '' ||
		$('#onoff').val() === '') {
		$('#background').addClass('background_on');
		$('#error-body').html('Please enter all data into fields');
    		$('#error-message').addClass('error_message_on');
	} else {
		var spreadjuiceindicator = $('#spreadjuiceindicator').find(':selected').val();
		var totaljuiceindicator = $('#totaljuiceindicator').find(':selected').val();
		var mlindicator = $('#mlindicator').find(':selected').val();

		// Call the service to update a scrapper
		var id = $("[id='" + $('#scrappernameid').val() + "_id']").val();

		// Call the service to add a scrapper
		var scrapperObject = '{"id":' + id + 
			',"userid":' + uId +
			',"scrappername":"' + $('#scrappername').val() +
			'","spreadlineadjustment":"' + $('#spreadlineadjustment').val() + 
			'","spreadjuiceindicator":"' + spreadjuiceindicator + 
			'","spreadjuice":"' + $('#spreadjuice').val() +
			'","spreadjuiceadjustment":"' + $('#spreadjuiceadjustment').val() +
			'","spreadmaxamount":"' + $('#spreadmaxamount').val() +
			'","spreadsourceamount":"' + $('#spreadsourceamount').val() +
			'","totallineadjustment":"' + $('#totallineadjustment').val() + 
			'","totaljuiceindicator":"' + totaljuiceindicator + 
			'","totaljuice":"' + $('#totaljuice').val() +
			'","totaljuiceadjustment":"' + $('#totaljuiceadjustment').val() +
			'","totalmaxamount":"' + $('#totalmaxamount').val() +
			'","totalsourceamount":"' + $('#totalsourceamount').val() +
			'","mlindicator":"' + mlindicator +
			'","mlline":"' + $('#mlline').val() +
			'","mllineadjustment":"' + $('#mllineadjustment').val() + 
			'","mlmaxamount":"' + $('#mlmaxamount').val() +
			'","mlsourceamount":"' + $('#mlsourceamount').val() +
			'","pullinginterval":"' + $('#pullinginterval').val() +
			'","mobiletext":"' + $('#mobiletext').val() +
			'","telegramnumber":"' + $('#telegramnumber').val() +
			'","servernumber":' + $('#servernumber').val() +
			',"enableretry":' + $.fn.setupBoolean('enableretry') +
			',"fullfill":' + $.fn.setupBoolean('fullfill') +
			',"orderamount":' + $('#orderamount').val() +
			',"middlerules":' + $.fn.setupBoolean('middlerules') +
			',"checkdupgame":' + $.fn.setupBoolean('checkdupgame') +
			',"playotherside":' + $.fn.setupBoolean('playotherside') +
			',"bestprice":' + $.fn.setupBoolean('bestprice') +
			',"sendtextforaccount":' + $.fn.setupBoolean('sendtextforaccount') +
			',"sendtextforgame":' + $.fn.setupBoolean('sendtextforgame') +
			',"onoff":' + $.fn.setupBoolean('onoff') +  
			',"gameonoff":' + $.fn.setupBoolean('gameonoff') +  
			',"firstonoff":' + $.fn.setupBoolean('firstonoff') +  
			',"secondonoff":' + $.fn.setupBoolean('secondonoff') +  
			',"thirdonoff":' + $.fn.setupBoolean('thirdonoff') +  
			',"nflspreadonoff":' + $.fn.setupBoolean('nflspreadonoff') +  
			',"nfltotalonoff":' + $.fn.setupBoolean('nfltotalonoff') +  
			',"nflmlonoff":' + $.fn.setupBoolean('nflmlonoff') +  
			',"ncaafspreadonoff":' + $.fn.setupBoolean('ncaafspreadonoff') +  
			',"ncaaftotalonoff":' + $.fn.setupBoolean('ncaaftotalonoff') +  
			',"ncaafmlonoff":' + $.fn.setupBoolean('ncaafmlonoff') +  
			',"nbaspreadonoff":' + $.fn.setupBoolean('nbaspreadonoff') +  
			',"nbatotalonoff":' + $.fn.setupBoolean('nbatotalonoff') +  
			',"nbamlonoff":' + $.fn.setupBoolean('nbamlonoff') +  
			',"ncaabspreadonoff":' + $.fn.setupBoolean('ncaabspreadonoff') +  
			',"ncaabtotalonoff":' + $.fn.setupBoolean('ncaabtotalonoff') +  
			',"ncaabmlonoff":' + $.fn.setupBoolean('ncaabmlonoff') +  
			',"wnbaspreadonoff":' + $.fn.setupBoolean('wnbaspreadonoff') +  
			',"wnbatotalonoff":' + $.fn.setupBoolean('wnbatotalonoff') +  
			',"wnbamlonoff":' + $.fn.setupBoolean('wnbamlonoff') +  
			',"nhlspreadonoff":' + $.fn.setupBoolean('nhlspreadonoff') +  
			',"nhltotalonoff":' + $.fn.setupBoolean('nhltotalonoff') +  
			',"nhlmlonoff":' + $.fn.setupBoolean('nhlmlonoff') +  
			',"mlbspreadonoff":' + $.fn.setupBoolean('mlbspreadonoff') +  
			',"mlbtotalonoff":' + $.fn.setupBoolean('mlbtotalonoff') +  
			',"mlbmlonoff":' + $.fn.setupBoolean('mlbmlonoff') + 
			',"internationalbaseballspreadonoff":' + $.fn.setupBoolean('intbaseballspreadonoff') +  
			',"internationalbaseballtotalonoff":' + $.fn.setupBoolean('intbaseballtotalonoff') +  
			',"internationalbaseballmlonoff":' + $.fn.setupBoolean('intbaseballmlonoff') + 
			',"keynumber":' + $.fn.setupBoolean('keynumber') + 
			',"humanspeed":' + $.fn.setupBoolean('humanspeed') +
			',"unitsenabled":' + $.fn.setupBoolean('unitsenabled') +
			',"spreadunit":' + $('#spreadunit').val() +
			',"totalunit":' + $('#totalunit').val() +
			',"mlunit":' + $('#mlunit').val() +
			',"leanssenabled":' + $.fn.setupBoolean('leanssenabled') +
			',"spreadlean":' + $('#spreadlean').val() +
			',"totallean":' + $('#totallean').val() +
			',"mllean":' + $('#mllean').val() + '}';

		log(scrapperObject);
		$.fn.serviceCall('POST', scrapperObject, 'restapi/scrapper/update', 45000, function(data) {
            $.fn.getWebScrapperList();
		});
	}
}

// Function to delete an account
$.fn.deleteWebScrapper = function() {
	var id = $("[id='" + $('#scrappernameid').val() + "_id']").val();
	$.fn.serviceCall('DELETE', '', 'restapi/scrapper/delete/' + id, 45000, function(data) {
		// Call the service to get all accounts
		$.fn.getWebScrapperList();
	    	$("#button-add-scrapper").show();
	    	$("#button-update-scrapper").hide();
	    	$("#button-delete-scrapper").hide();
	});
}

// Load all groups and accounts
$.fn.loadWebScrapperData = function() {
	selectedList = [];
	
	// Hide update and delete on initial load
	$('#button-update-scrapper').hide();
	$('#button-delete-scrapper').hide();

	// Add button click
    $('#button-add-scrapper').on('click', function() {
    		$.fn.addWebScrapper();
    });

	// Update button click
    $('#button-update-scrapper').on('click', function() {
    		$.fn.updateWebScrapper();
    });

	// Delete button click
    $('#button-delete-scrapper').on('click', function() {
    		$.fn.deleteWebScrapper();
    });

    // Reset button click
    $('#button-reset-scrapper').on('click', function() {
	    	// Clear out the data
	    	$('#scrappername').val('');
	    	$('#spreadlineadjustment').val('');
	    	$("#spreadjuiceindicator option[value='-']").prop('selected', true);
	    	$('#spreadjuice').val('');
	    	$('#spreadjuiceadjustment').val('');
	    	$('#spreadmaxamount').val('');
	    	$('#spreadsourceamount').val('0');
	    	$("#totallineadjustment").val('');
	    	$("#totaljuiceindicator option[value='-']").prop('selected', true);
	    	$("#totaljuice").val('');
	    	$("#totaljuiceadjustment").val('');
	    	$('#totalmaxamount').val('');
	    	$('#totalsourceamount').val('0');
	    	$("#mlindicator option[value='-']").prop('selected', true);
	    	$('#mlline').val('');
	    	$('#mllineadjustment').val('');
	    	$('#mlmaxamount').val('');
	    	$('#mlsourceamount').val('0');
	    	$('#pullinginterval').val('');
	    	$('#mobiletext').val('');
	    	$('#telegramnumber').val('');
	    	$('#servernumber').val('');
	    	$('#enableretry').prop("checked", false);
	    	$('#fullfill').prop("checked", false);
	    	$('#orderamount').val('0');
	    	$('#middlerules').prop("checked", false);
	    	$('#checkdupgame').prop("checked", false);
	    	$('#playotherside').prop("checked", false);
	    	$('#bestprice').prop("checked", false);
	    	$('#sendtextforaccount').prop("checked", false);
	    	$('#sendtextforgame').prop("checked", false);
	    	$('#onoff').prop("checked", true);
	    	$('#gameonoff').prop("checked", true);
	    	$('#firstonoff').prop("checked", true);
	    	$('#secondonoff').prop("checked", true);
	    	$('#thirdonoff').prop("checked", true);
	    	$('#nflspreadonoff').prop("checked", true);
	    	$('#nfltotalonoff').prop("checked", true);
	    	$('#nflmlonoff').prop("checked", true);
	    	$('#ncaafspreadonoff').prop("checked", true);
	    	$('#ncaaftotalonoff').prop("checked", true);
	    	$('#ncaafmlonoff').prop("checked", true);
	    	$('#nbaspreadonoff').prop("checked", true);
	    	$('#nbatotalonoff').prop("checked", true);
	    	$('#nbamlonoff').prop("checked", true);
	    	$('#ncaabspreadonoff').prop("checked", true);
	    	$('#ncaabtotalonoff').prop("checked", true);
	    	$('#ncaabmlonoff').prop("checked", true);
	    	$('#wnbaspreadonoff').prop("checked", true);
	    	$('#wnbatotalonoff').prop("checked", true);
	    	$('#wnbamlonoff').prop("checked", true);
	    	$('#nhlspreadonoff').prop("checked", true);
	    	$('#nhltotalonoff').prop("checked", true);
	    	$('#nhlmlonoff').prop("checked", true);
	    	$('#mlbspreadonoff').prop("checked", true);
	    	$('#mlbtotalonoff').prop("checked", true);
	    	$('#mlbmlonoff').prop("checked", true);
	    	$('#intbaseballspreadonoff').prop("checked", true);
	    	$('#intbaseballtotalonoff').prop("checked", true);
	    	$('#intbaseballmlonoff').prop("checked", true);
	    	$('#keynumber').prop("checked", false);
	    	$('#humanspeed').prop("checked", false);
	    	$('#unitsenabled').prop("checked", false);
	    	$('#spreadunit').val('0');
	    	$('#totalunit').val('0');
	    	$('#mlunit').val('0');
	    	$('#leanssenabled').prop("checked", false);
	    	$('#spreadlean').val('0');
	    	$('#totallean').val('0');
	    	$('#mllean').val('0');

	    	// Reset the buttons
	    	$("#button-add-scrapper").show();
	    	$("#button-update-scrapper").hide();
	    	$("#button-delete-scrapper").hide();
    });

    $.fn.getWebScrapperList();
}