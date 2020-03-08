var betRotationid;
var betSport;
var betLinetype;
var betLineindicator;
var betLine;
var betJuiceindicator;
var betJuice;
var betBuyorder;
var betAmount;
var betBuyamount;
var betRiskwin;
var betAccounts;
var betGroups;

// Bet
$.fn.betTransaction = function(rotationid, sport, linetype, lineindicator, totalindicator, line, juiceindicator, juice, buyorder, amount, buyamount, riskwin, accounts, groups) {
	if (typeof accounts != 'undefined' && accounts != null && accounts.length > 0) {
 	   var eventObjects = '';
 	   var counter = 0;
 	   for (var x = 0; x < accounts.length; x++) {
 		   if (counter > 0) {
 			   eventObjects += ',' + JSON.stringify($.fn.createEvent(rotationid, sport, linetype, lineindicator, totalindicator, line, juiceindicator, juice, buyorder, amount, riskwin, accounts[x], '-99'));
 		   } else {
 			   eventObjects += JSON.stringify($.fn.createEvent(rotationid, sport, linetype, lineindicator, totalindicator, line, juiceindicator, juice, buyorder, amount, riskwin, accounts[x], '-99'));
 		   }
 		   counter++;
 	   }

 	   var placeTransaction = '"userid":' +  user.id +	
 	   	',"rotationid":' +  rotationid +
 		',"linetype":"' +  linetype +
 		'","lineindicator":"' +  lineindicator +
 		'","totalindicator":"' + totalindicator +
 		'","line":"' +  line +
 		'","juiceindicator":"' +  juiceindicator +
 		'","juice":"' +  juice +
 		'","sport":"' +  sport +
 		'","amount":"' +  amount +
 		'","buyamount":"' +  buyamount +
 		'","riskwin":"' +  riskwin +
 		'","buyorder":' +  buyorder;

 	   var eventTransaction = '{' +
 	   		placeTransaction + ',' + 
 	   		'"eventdatas":[' + eventObjects + ']}';
 
 	   log('eventTransaction: ' + eventTransaction);
 	   $.fn.placeTransaction(eventTransaction, linetype);
	} else if (typeof groups != 'undefined' && groups != null && groups.length > 0) {
		var eventObjects = '';
		var counter = 0;
		for (var x = 0; x < groups.length; x++) {
			if (counter > 0) {
				eventObjects += ',' + JSON.stringify($.fn.createEvent(rotationid, sport, linetype, lineindicator, totalindicator, line, juiceindicator, juice, buyorder, amount, riskwin, '-99', groups[x]));
			} else {
				eventObjects += JSON.stringify($.fn.createEvent(rotationid, sport, linetype, lineindicator, totalindicator, line, juiceindicator, juice, buyorder, amount, riskwin, '-99', groups[x]));
			}
			counter++;
		}

		var placeTransaction = '"userid":' +  user.id +	
	 		',"rotationid":' +  rotationid +
	 		',"linetype":"' +  linetype +
	 		'","lineindicator":"' +  lineindicator +
	 		'","totalindicator":"' + totalindicator +
	 		'","line":"' +  line +
	 		'","juiceindicator":"' +  juiceindicator +
	 		'","juice":"' +  juice +
	 		'","sport":"' +  sport +
	 		'","amount":"' +  amount +
	 		'","buyamount":"' +  buyamount +
	 		'","riskwin":"' +  riskwin +
	 		'","buyorder":' +  buyorder;

		var eventTransaction = '{' +
			placeTransaction + ',' + 
	 	   	'"eventdatas":[' + eventObjects + ']}';

		log('eventTransaction: ' + eventTransaction);
		$.fn.placeTransaction(eventTransaction, linetype);
	}
}

// Preview
$.fn.previewBet = function(rotationid, sport, linetype, lineindicator, amount, accounts, groups) {
	if (typeof accounts != 'undefined' && accounts != null && accounts.length > 0) {
 	   var accountids = [];
 	   var counter = 0;
 	   for (var x = 0; x < accounts.length; x++) {
 		  accountids.push(accounts[x]);
 	   }
 	   $.fn.previewTransaction(JSON.stringify($.fn.createPreview(rotationid, sport, linetype, lineindicator, amount, accountids, [])));
	} else if (typeof groups != 'undefined' && groups != null && groups.length > 0) {
		var groupids = [];
		var counter = 0;
		for (var x = 0; x < groups.length; x++) {
			groupids.push(groups[x]);
		}

		$.fn.previewTransaction(JSON.stringify($.fn.createPreview(rotationid, sport, linetype, lineindicator, amount, [], groupids)));
	}
}

// Setup event
$.fn.createEvent = function(rotationid, sport, linetype, lineindicator, totalindicator, line, juiceindicator, juice, buyorder, amount, riskwin, account, group) {
	var 	eventObject = {
		"userid": user.id,	
		"rotationid": rotationid,
	    	"linetype": linetype,
	    	"lineindicator": lineindicator,
	    	"totalindicator": totalindicator,
	    	"line": line,
	    	"juiceindicator": juiceindicator,
	    	"juice": juice,
	    	"sport": sport,
	    	"amount": amount,
	    	"riskwin": riskwin,
	    	"accountid": account,
	    	"groupid": group
	};

	return eventObject;
}

// Setup preview
$.fn.createPreview = function(rotationid, sport, linetype, lineindicator, amount, accounts, groups) {
	var 	previewObject = {
		"userid": user.id,	
		"rotationid": rotationid,
	    	"linetype": linetype,
	    	"lineindicator": lineindicator,
	    	"sporttype": sport,
	    	"amount": amount,
	    	"accountids": accounts,
	    	"groupids": groups
	};

	return previewObject;
}

// Bet Preview
$.fn.betPreview = function() {
	var eventObjects = '';
	var counter = 0;
   	var rotationid = '';
	var linetype = '';
	var lineindicator = '';
	var line = '';
	var juiceindicator = '';
	var juice = '';
	var amount = '';
	var sport = '';
	var riskwin = $('input[name=bet-riskwin]:checked').val();

	$('#previewlines-data tr').each(function(cmp) { 
	    var trid = $(this).attr('id');
	    var index = trid.indexOf('-');
	    if (index !== -1) {
	    		var id = trid.substring(index + 1);

	    		// First check if checkbox is checked
	    		var doPlay = $('#' + id).is(":checked");
	    		if (doPlay) {
	    			var rotid = $('#rot-' + id).val();
	    			var sporttype = $('#sport-' + id).val();
	    			var ltype = $('#linetype-' + id).val();
	    			var amnt = $('#amount-' + id).val();
	    			var lindicator = $('#li-' + id).val();
	    			var ln = $('#l-' + id).val();
	    			var jindicator = $('#ji-' + id).val();
	    			var jc = $('#j-' + id).val();

	    			if (counter > 0) {
	    				eventObjects += ',' + JSON.stringify($.fn.createEvent(rotid, sporttype, ltype, lindicator, lindicator, ln, jindicator, jc, false, amnt, riskwin, id, '-99'));
	    			} else {
	    				eventObjects += JSON.stringify($.fn.createEvent(rotid, sporttype, ltype, lindicator, lindicator, ln, jindicator, jc, false, amnt, riskwin, id, '-99'));
	    			   	rotationid = rotid;
	    				linetype = ltype;
	    				lineindicator = lindicator;
	    				line = ln;
	    				juiceindicator = jindicator;
	    				juice = jc;
	    				amount = amnt;
	    				sport = sporttype;
	    			}
	    			counter++;
	    		}
	    }
	});

	if (counter > 0) {
		var placeTransaction = '"userid":' +  user.id +	
		   	',"rotationid":' +  rotationid +
			',"linetype":"' +  linetype +
			'","lineindicator":"' +  lineindicator +
			'","totalindicator":"' + lineindicator +
			'","line":"' +  line +
			'","juiceindicator":"' +  juiceindicator +
			'","juice":"' +  juice +
			'","sport":"' +  sport +
			'","amount":"' +  amount +
			'","buyamount":"' +  '' +
			'","riskwin":"' +  riskwin +
			'","buyorder":' +  false;
	
		var eventTransaction = '{' + placeTransaction + ',' + 
			'"eventdatas":[' + eventObjects + ']}';
	
		$.fn.placeTransaction(eventTransaction, linetype);
	}
}

$.fn.setupBetpreviewButton = function() {
    // Bet button
	$('#bet-button-betpreview').on('click', function() {
		$.fn.betPreview();
    });
}