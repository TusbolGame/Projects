	var spreadEventObject = {};
	var totalEventObject = {};
	var mlEventObject = {};
	var eventid;
	var accountid;
	var accountname;
	var groupid;
	var groupname;
	var group;
	var submitType;
	var transactionid;
	var retryFunction;
	var retryCount = 0;

	// Get the transaction information
	$.fn.getTransactionInfo = function(url) {
		// Only refresh for one minute
		if (retryCount++ < 12) {
			$.fn.serviceCallNoSpinner('GET', '', url, 15000, function(data) {
				if (typeof data != 'undefined' && data != 'null' && typeof data.committedevents != 'undefined' && data.committedevents.length > 0) {
					var isFinished = true;
					for (var i = 0; i < data.committedevents.length; i++) {
						if (typeof data.committedevents[i].accountevents != 'undefined' && data.committedevents[i].accountevents != null && data.committedevents[i].accountevents.length > 0) {
							var accountsevents = data.committedevents[i].accountevents;
							for (var x = 0; x < accountsevents.length; x++) {
								if (accountsevents[x].status === 'In Progress') {
									isFinished = false;
								}
							}
						}
					}
		
					// Check if finished
					if (isFinished) {
						$.fn.setupTransactions(data, true);
						clearInterval(retryFunction);
						retryCount = 0;
					} else {
						$.fn.setupTransactions(data, true);
					}
				}
			});
		} else {
			clearInterval(retryFunction);
			retryCount = 0;
		}
	}

	// Record the spread event
	$.fn.recordSpreadEvent = function(eventObject) {
		log(eventObject);
		$.fn.serviceCall('POST', eventObject, 'restapi/recordevent/spread', 300000, function(data) {
			log(data);

			// Reset the data
	    	$('#spread-input-first-one').val('');
	    	$("#spread-plusminus-first-one option[value='+']").prop('selected', true);
	    	$('#spread-input-juice-first-one').val('');
	    	$("#spread-juice-plusminus-first-one option[value='-']").prop('selected', true);
	    	$('#spread-input-first-two').val('');
	    	$("#spread-plusminus-first-two option[value='+']").prop('selected', true);
	    	$('#spread-input-juice-first-two').val('');
	    	$("#spread-juice-plusminus-first-two option[value='-']").prop('selected', true);
	    	$('#spread-input-second-one').val('');
	    	$("#spread-plusminus-second-one option[value='-']").prop('selected', true);
	    	$('#spread-input-juice-second-one').val('');
	    	$("#spread-juice-plusminus-second-one option[value='-']").prop('selected', true);
	    	$('#spread-input-second-two').val('');
	    	$("#spread-plusminus-second-two option[value='-']").prop('selected', true);
	    	$('#spread-input-juice-second-two').val('');
	    	$("#spread-juice-plusminus-second-two option[value='-']").prop('selected', true);

			if (typeof data != 'undefined' && typeof data != null) {
				retryCount = 0;
				transactionid = data.id;
				log('transactionid: ' + transactionid);
				$.get('transactions.html', function(data) {
					$('#app-container').html(data);
					$.fn.getTransactionInfo('restapi/committedevents/spreadevent/' + transactionid);
					retryFunction = setInterval(function() {
						$.fn.getTransactionInfo('restapi/committedevents/spreadevent/' + transactionid);
					}, 5000);
				});
			}        	
		});
	}

	// Record the total event
	$.fn.recordTotalEvent = function(eventObject) {
		log(eventObject);
		$.fn.serviceCall('POST', eventObject, 'restapi/recordevent/total', 300000, function(data) {
			log(data);

			// Reset the data
        	$('#total-input-first-one').val('');
	    	$('#total-input-juice-first-one').val('');
	    	$("#total-juice-plusminus-first-one option[value='-']").prop('selected', true);
	    	$('#total-input-first-two').val('');
	    	$('#total-input-juice-first-two').val('');
	    	$("#total-juice-plusminus-first-two option[value='-']").prop('selected', true);
	    	$('#total-input-second-one').val('');
	    	$('#total-input-juice-second-one').val('');
	    	$("#total-juice-plusminus-second-one option[value='-']").prop('selected', true);
	    	$('#total-input-second-two').val('');
	    	$('#total-input-juice-second-two').val('');
	    	$("#total-juice-plusminus-second-two option[value='-']").prop('selected', true);
	    	
	    	// Update the data
			if (typeof data != 'undefined' && typeof data != null) {
				retryCount = 0;
				transactionid = data.id;
				log('transactionid: ' + transactionid);
				$.get('transactions.html', function(data) {
					$('#app-container').html(data);
					$.fn.getTransactionInfo('restapi/committedevents/totalevent/' + transactionid);
					retryFunction = setInterval(function() {
						$.fn.getTransactionInfo('restapi/committedevents/totalevent/' + transactionid);
					}, 5000);
				});
			}
		});
	}

	// Record the money line event
	$.fn.recordMlEvent = function(eventObject) {
		log(eventObject);
		$.fn.serviceCall('POST', eventObject, 'restapi/recordevent/ml', 300000, function(data) {
			log(data);
//			$('#background').addClass('background_on');
//			$('#error-body').html('Transaction saved');
//       	$('#error-message').addClass('error_message_on');
        	
	    	$("#ml-plusminus-first-one option[value='-']").prop('selected', true);
	    	$('#ml-input-first-one').val('');
	    	$("#ml-plusminus-first-two option[value='-']").prop('selected', true);
	    	$('#ml-input-first-two').val('');
	    	$("#ml-plusminus-second-one option[value='-']").prop('selected', true);
	    	$('#ml-input-second-one').val('');
	    	$("#ml-plusminus-second-two option[value='-']").prop('selected', true);
	    	$('#ml-input-second-two').val('');

	    	// Update the data
			if (typeof data != 'undefined' && typeof data != null) {
				retryCount = 0;
				transactionid = data.id;
				log('transactionid: ' + transactionid);
				$.get('transactions.html', function(data) {
					$('#app-container').html(data);
					$.fn.getTransactionInfo('restapi/committedevents/mlevent/' + transactionid);
					retryFunction = setInterval(function() {
						$.fn.getTransactionInfo('restapi/committedevents/mlevent/' + transactionid);
					}, 5000);
				});
			}
		});
	}

	// Setup the buttons
	$.fn.setupButtons = function() {
		// Spread button
	    $('#spread-button').on('click', function() {
	    	submitType = 'spread';
	    	$('input[name=oneattempt]').prop('checked', true);
	    	$("#RISKWIN[value=2]").prop("checked", true);
	    	$('#attemptcounter').css('display', 'none');

	    	// Get the first teams information
	    	var spreadinputfirstone = $('#spread-input-first-one').val();
	    	var spreadplusminusfirstone = '';
	    	if (spreadinputfirstone != '') {
	    		spreadplusminusfirstone = $('#spread-plusminus-first-one').find(':selected').val();
	    	}
	    	var spreadinputjuicefirstone = $('#spread-input-juice-first-one').val();
	    	var spreadjuiceplusminusfirstone = '';
	    	if (spreadinputjuicefirstone != '') {
	    		spreadjuiceplusminusfirstone = $('#spread-juice-plusminus-first-one').find(':selected').val();
	    	}
	    	var spreadinputfirsttwo = $('#spread-input-first-two').val();
	    	var spreadplusminusfirsttwo = '';
	    	if (spreadinputfirsttwo != '') {
	    		spreadplusminusfirsttwo = $('#spread-plusminus-first-two').find(':selected').val();
	    	}
	    	var spreadinputjuicefirsttwo = $('#spread-input-juice-first-two').val();
	    	var spreadjuiceplusminusfirsttwo = '';
	    	if (spreadinputjuicefirsttwo != '') {
	    		spreadjuiceplusminusfirsttwo = $('#spread-juice-plusminus-first-two').find(':selected').val();
	    	}
	    	// Get the Second teams information
	    	var spreadinputsecondone = $('#spread-input-second-one').val();
	    	var spreadplusminussecondone = '';
	    	if (spreadinputsecondone != '') {
	    		spreadplusminussecondone = $('#spread-plusminus-second-one').find(':selected').val();
	    	}
	    	var spreadinputjuicesecondone = $('#spread-input-juice-second-one').val();
	    	var spreadjuiceplusminussecondone = '';
	    	if (spreadinputjuicesecondone != '') {
	    		spreadjuiceplusminussecondone = $('#spread-juice-plusminus-second-one').find(':selected').val();
	    	}
	    	var spreadinputsecondtwo = $('#spread-input-second-two').val();
	    	var spreadplusminussecondtwo = '';
	    	if (spreadinputsecondtwo != '') {
	    		spreadplusminussecondtwo = $('#spread-plusminus-second-two').find(':selected').val();
	    	}
	    	var spreadinputjuicesecondtwo = $('#spread-input-juice-second-two').val();
	    	var spreadjuiceplusminussecondtwo = '';
	    	if (spreadinputjuicesecondtwo != '') {
	    		spreadjuiceplusminussecondtwo = $('#spread-juice-plusminus-second-two').find(':selected').val();
	    	}

	    	// Now check if we have multiple teams being setup
	    	if ((spreadinputfirstone === '' && spreadinputfirsttwo === '' && spreadinputsecondone === '' && spreadinputsecondtwo === '')) {
				$('#background').addClass('background_on');
				$('#error-body').html('Setup at least one spread');
	        	$('#error-message').addClass('error_message_on');
	    	} else if ((spreadinputfirstone != '' || spreadinputfirsttwo != '') && (spreadinputsecondone != '' || spreadinputsecondtwo != '')) {
				$('#background').addClass('background_on');
				$('#error-body').html('Cannot have spread information for both teams');
	        	$('#error-message').addClass('error_message_on');
	    	} else if ((spreadinputfirsttwo != '' && spreadinputjuicefirsttwo != '') && (spreadinputfirstone === '' || spreadinputjuicefirstone === '')) {
				$('#background').addClass('background_on');
				$('#error-body').html('First spread must also be used');
		        $('#error-message').addClass('error_message_on');
	    	} else if ((spreadinputsecondtwo != '' && spreadinputjuicesecondtwo != '') && (spreadinputsecondone === '' || spreadinputjuicesecondone === '')) {
				$('#background').addClass('background_on');
				$('#error-body').html('First spread must also be used');
		        $('#error-message').addClass('error_message_on');
	    	} else if (spreadinputfirstone != '' && spreadinputjuicefirstone === '') {
				$('#background').addClass('background_on');
				$('#error-body').html('Please enter juice information');
	        	$('#error-message').addClass('error_message_on');
	    	} else if (spreadinputfirsttwo != '' && spreadinputjuicefirsttwo === '') {
				$('#background').addClass('background_on');
				$('#error-body').html('Please enter juice information');
		        $('#error-message').addClass('error_message_on');
	    	} else if (spreadinputsecondone != '' && spreadinputjuicesecondone === '') {
				$('#background').addClass('background_on');
				$('#error-body').html('Please enter juice information');
	        	$('#error-message').addClass('error_message_on');
	    	} else if (spreadinputsecondtwo != '' && spreadinputjuicesecondtwo === '') {
				$('#background').addClass('background_on');
				$('#error-body').html('Please enter juice information');
	        	$('#error-message').addClass('error_message_on');
	    	} else {
		    	// Show the dialog
		    	$('#eventamount').dialog('open');	    		
	    	}

	    	var event_team1 = $('.game_team_name1').html();
	    	var event_team2 = $('.game_team_name2').html();
	    	var event_name = event_team1 + ' vs ' + event_team2;
	    	var event_date_time = $('.game_date_div').html();
	    	spreadEventObject = {
	    			"eventname": event_name,
	    			"eventtype": 'spread',
	    			"sport": eventtype,
	    			"userid": user.id,
	    			"accountid": accountid,
	    			"groupid": groupid,
	    			"eventid": eventid,
	    			"eventid1": $('.game_id1').html(),
	    			"eventid2": $('.game_id2').html(),
	    			"eventteam1": event_team1,
	    			"eventteam2": event_team2,
	    			"amount": "",
	    			"wtype": "",
	    			"datentime" : "",
	    			"attempts" : 0,
	    			"eventdatetime" : event_date_time,
	    			"spreadinputfirstone": spreadinputfirstone, 
	    	    	"spreadplusminusfirstone" : spreadplusminusfirstone,
	    	    	"spreadinputjuicefirstone" : spreadinputjuicefirstone,
	    	    	"spreadjuiceplusminusfirstone" : spreadjuiceplusminusfirstone,
	    	    	"spreadinputfirsttwo" : spreadinputfirsttwo,
	    	    	"spreadplusminusfirsttwo" : spreadplusminusfirsttwo,
	    	    	"spreadinputjuicefirsttwo" : spreadinputjuicefirsttwo,
	    	    	"spreadjuiceplusminusfirsttwo" : spreadjuiceplusminusfirsttwo,
	    	    	"spreadinputsecondone" : spreadinputsecondone,
	    	    	"spreadplusminussecondone" : spreadplusminussecondone,
	    	    	"spreadinputjuicesecondone" : spreadinputjuicesecondone,
	    	    	"spreadjuiceplusminussecondone" : spreadjuiceplusminussecondone,
	    	    	"spreadinputsecondtwo" : spreadinputsecondtwo,
	    	    	"spreadplusminussecondtwo" : spreadplusminussecondtwo,
	    	    	"spreadinputjuicesecondtwo" : spreadinputjuicesecondtwo, 
	    	    	"spreadjuiceplusminussecondtwo" : spreadjuiceplusminussecondtwo
	    	}
	    });
	    
	    // Total button
	    $('#total-button').on('click', function() {
	    	submitType = 'total';
	    	$('input[name=oneattempt]').prop('checked', true);
	    	$("#RISKWIN[value='2']").prop("checked", true);
	    	$('#attemptcounter').css('display', 'none');

	    	// Get the first teams information
	    	var totalinputfirstone = $('#total-input-first-one').val();
	    	var totalinputjuicefirstone = $('#total-input-juice-first-one').val();
	    	var totaljuiceplusminusfirstone = '';
	    	if (totalinputjuicefirstone != '') {
	    		totaljuiceplusminusfirstone = $('#total-juice-plusminus-first-one').find(':selected').val();
	    	}
	    	var totalinputfirsttwo = $('#total-input-first-two').val();
	    	var totalinputjuicefirsttwo = $('#total-input-juice-first-two').val();
	    	var totaljuiceplusminusfirsttwo = '';
	    	if (totalinputjuicefirsttwo != '') {
	    		totaljuiceplusminusfirsttwo = $('#total-juice-plusminus-first-two').find(':selected').val();
	    	}
	    	// Get the second teams information
	    	var totalinputsecondone = $('#total-input-second-one').val();
	    	var totalinputjuicesecondone = $('#total-input-juice-second-one').val();
	    	var totaljuiceplusminussecondone = '';
	    	if (totalinputjuicesecondone != '') {
	    		totaljuiceplusminussecondone = $('#total-juice-plusminus-second-one').find(':selected').val();
	    	}
	    	var totalinputsecondtwo = $('#total-input-second-two').val();
	    	var totalinputjuicesecondtwo = $('#total-input-juice-second-two').val();
	    	var totaljuiceplusminussecondtwo = '';
	    	if (totalinputjuicesecondtwo != '') {
	    		totaljuiceplusminussecondtwo = $('#total-juice-plusminus-second-two').find(':selected').val();
	    	}

	    	// Now check if we have multiple teams being setup
	    	if ((totalinputfirstone === '' && totalinputfirsttwo === '' && totalinputsecondone === '' && totalinputsecondtwo === '')) {
				$('#background').addClass('background_on');
				$('#error-body').html('Setup at least one total');
	        	$('#error-message').addClass('error_message_on');
	    	// Now check if we have multiple teams being setup
	    	} else if ((totalinputfirstone != '' || totalinputfirsttwo != '') && (totalinputsecondone != '' || totalinputsecondtwo != '')) {
				$('#background').addClass('background_on');
				$('#error-body').html('Cannot have total information for both teams');
	        	$('#error-message').addClass('error_message_on');
	    	} else if ((totalinputfirsttwo != '' && totalinputjuicefirsttwo != '') && (totalinputfirstone === '' && totalinputjuicefirstone === '')) {
				$('#background').addClass('background_on');
				$('#error-body').html('First total must also be used');
		        $('#error-message').addClass('error_message_on');	    			
	    	} else if ((totalinputsecondtwo != '' && totalinputjuicesecondtwo != '') && (totalinputsecondone === '' && totalinputjuicesecondone === '')){
				$('#background').addClass('background_on');
				$('#error-body').html('First total must also be used');
		        $('#error-message').addClass('error_message_on');
	    	} else if (totalinputfirstone != '' && totalinputjuicefirstone === '') {
				$('#background').addClass('background_on');
				$('#error-body').html('Please enter total information');
	        	$('#error-message').addClass('error_message_on');
	    	} else if (totalinputfirsttwo != '' && totalinputjuicefirsttwo === '') {
				$('#background').addClass('background_on');
				$('#error-body').html('Please enter toal information');
	        	$('#error-message').addClass('error_message_on');
	    	} else if (totalinputsecondone != '' && totalinputjuicesecondone === '') {
				$('#background').addClass('background_on');
				$('#error-body').html('Please enter total information');
	        	$('#error-message').addClass('error_message_on');
	    	} else if (totalinputsecondtwo != '' && totalinputjuicesecondtwo === '') {
				$('#background').addClass('background_on');
				$('#error-body').html('Please enter total information');
	        	$('#error-message').addClass('error_message_on');
	    	} else {
		    	// Show the dialog
		    	$('#eventamount').dialog('open');	    		
	    	}

	    	var event_team1 = $('.game_team_name1').html();
	    	var event_team2 = $('.game_team_name2').html();
	    	var event_name = event_team1 + ' vs ' + event_team2;
	    	var event_date_time = $('.game_date_div').html();
	    	totalEventObject = {
	    			"eventname": event_name,
	    			"eventtype": 'total',
	    			"sport": eventtype,
	    			"userid": user.id,
	    			"accountid": accountid,
	    			"groupid": groupid,
	    			"eventid": eventid,
	    			"eventid1": $('.game_id1').html(),
	    			"eventid2": $('.game_id2').html(),
	    			"eventteam1": event_team1,
	    			"eventteam2": event_team2,
	    			"amount": "",
	    			"wtype": "",
	    			"datentime" : "",
	    			"attempts" : 0,
	    			"eventdatetime" : event_date_time,
	    			"totalinputfirstone": totalinputfirstone, 
	    	    	"totaljuiceplusminusfirstone" : totaljuiceplusminusfirstone,
	    	    	"totalinputjuicefirstone" : totalinputjuicefirstone,
	    			"totalinputfirsttwo": totalinputfirsttwo, 
	    	    	"totaljuiceplusminusfirsttwo" : totaljuiceplusminusfirsttwo,
	    	    	"totalinputjuicefirsttwo" : totalinputjuicefirsttwo,
	    			"totalinputsecondone": totalinputsecondone,
	    	    	"totaljuiceplusminussecondone" : totaljuiceplusminussecondone,
	    	    	"totalinputjuicesecondone" : totalinputjuicesecondone,
	    			"totalinputsecondtwo": totalinputsecondtwo,
	    	    	"totaljuiceplusminussecondtwo" : totaljuiceplusminussecondtwo,
	    	    	"totalinputjuicesecondtwo" : totalinputjuicesecondtwo,
	    	}
	    });
	    
	    // Money line button
	    $('#ml-button').on('click', function() {
	    	submitType = 'ml';
	    	$('input[name=oneattempt]').prop('checked', true);
	    	$("#RISKWIN[value='2']").prop("checked", true);
	    	$('#attemptcounter').css('display', 'none');

	    	var mlinputfirstone = $('#ml-input-first-one').val();
	    	var mlplusminusfirstone = '';
	    	if (mlinputfirstone != '') {
	    		mlplusminusfirstone = $('#ml-plusminus-first-one').find(':selected').val();
	    	}
	    	var mlinputfirsttwo = $('#ml-input-first-two').val();
	    	var mlplusminusfirsttwo = '';
	    	if (mlinputfirsttwo != '') {
	    		mlplusminusfirsttwo = $('#ml-plusminus-first-two').find(':selected').val();
	    	}
	    	var mlinputsecondone = $('#ml-input-second-one').val();
	    	var mlplusminussecondone = '';
	    	if (mlinputsecondone != '') {
	    		mlplusminussecondone = $('#ml-plusminus-second-one').find(':selected').val();
	    	}
	    	var mlinputsecondtwo = $('#ml-input-second-two').val();
	    	var mlplusminussecondtwo = '';
	    	if (mlinputsecondtwo != '') {
	    		mlplusminussecondtwo = $('#ml-plusminus-second-two').find(':selected').val();
	    	}

	    	// Now check if we have multiple teams being setup
	    	if ((mlinputfirstone === '' && mlinputfirsttwo === '' && mlinputsecondone === '' && mlinputsecondtwo === '')) {
				$('#background').addClass('background_on');
				$('#error-body').html('Setup at least one money line');
	        	$('#error-message').addClass('error_message_on');
	    	// Now check if we have multiple teams being setup
	    	} else if ((mlinputfirstone != '' || mlinputfirsttwo != '') && (mlinputsecondone != '' || mlinputsecondtwo != '')) {
				$('#background').addClass('background_on');
				$('#error-body').html('Cannot have money line information for both teams');
	        	$('#error-message').addClass('error_message_on');
	    	} else if (mlinputfirsttwo != '') {
				$('#background').addClass('background_on');
				$('#error-body').html('First money line must also be used');
		        $('#error-message').addClass('error_message_on');	    			
	    	} else if (mlinputsecondtwo != '') {
				$('#background').addClass('background_on');
				$('#error-body').html('First money line must also be used');
		        $('#error-message').addClass('error_message_on');	    			
	    	} else {
	    		$('#eventamount').dialog('open');
	    	}
	    	
	    	var event_team1 = $('.game_team_name1').html();
	    	var event_team2 = $('.game_team_name2').html();
	    	var event_name = event_team1 + ' vs ' + event_team2;
	    	var event_date_time = $('.game_date_div').html();
	    	mlEventObject = {
	    			"eventname": event_name,
	    			"eventtype": 'ml',
	    			"sport": eventtype,
	    			"userid": user.id,
	    			"accountid": accountid,
	    			"groupid": groupid,
	    			"eventid": eventid,
	    			"eventid1": $('.game_id1').html(),
	    			"eventid2": $('.game_id2').html(),
	    			"eventteam1": event_team1,
	    			"eventteam2": event_team2,
	    			"amount": "",
	    			"wtype": "",
	    			"datentime" : "",
	    			"attempts" : 0,
	    			"eventdatetime" : event_date_time,
	    			"mlinputfirstone": mlinputfirstone, 
	    	    	"mlplusminusfirstone" : mlplusminusfirstone,
	    			"mlinputfirsttwo": mlinputfirsttwo, 
	    	    	"mlplusminusfirsttwo" : mlplusminusfirsttwo,
	    			"mlinputsecondone": mlinputsecondone, 
	    	    	"mlplusminussecondone" : mlplusminussecondone,
	    			"mlinputsecondtwo": mlinputsecondtwo, 
	    	    	"mlplusminussecondtwo" : mlplusminussecondtwo,
	    	}
	    });
	}

	// Setup date/time
	$.fn.setupDateTime = function() {
		$('#dandt').datetimepicker(
		{
			/*
				timeFormat
				Default: "HH:mm",
				A Localization Setting - String of format tokens to be replaced with the time.
			*/
			timeFormat: "hh:mm tt",
			/*
				hourMin
				Default: 0,
				The minimum hour allowed for all dates.
			*/
			hourMin: 0,
			/*
				hourMax
				Default: 23, 
				The maximum hour allowed for all dates.
			*/
			hourMax: 23,
			/*
				numberOfMonths
				jQuery DatePicker option
				that will show two months in datepicker
			*/
			numberOfMonths: 1,
			/*
				minDate
				jQuery datepicker option 
				which set today date as minimum date
			*/
			minDate: 0,
			/*
				maxDate
				jQuery datepicker option 
				which set 30 days later date as maximum date
			*/
			maxDate: 30
		});
	}
	
	// Send record
	$.fn.sendRecord = function() {
 	   var wamount = $("#WAMT_").val();
 	   var riskwin = $('input[name=RISKWIN]:checked').val();
       var tz = jstz.determine();
       var response_text = tz.name();
       log(response_text);
	   var dandt = $('#dandt').val();
	   if (dandt === '') {
		   dandt = moment().format('MM/DD/YYYY hh:mm a');
		   dandt = dandt + ' ' + response_text;
	   } else {
		   dandt = dandt + ' ' + response_text;
	   }
	   
	   var attemptsperday = 0;
	   if (!$('#oneattempt').prop('checked')) {
		   var nattempts = $('#attempts').val();
		   attemptsperday = nattempts;
	   }

	   if (submitType === 'spread') {
    	   spreadEventObject['amount'] = wamount;
    	   spreadEventObject['wtype'] = riskwin;
    	   spreadEventObject['datentime'] = dandt;
    	   spreadEventObject['attempts'] = attemptsperday;
    	   spreadEventObject = JSON.stringify(spreadEventObject);
		   $(this).recordSpreadEvent(spreadEventObject);
	   } else if (submitType === 'total') {
    	   totalEventObject['amount'] = wamount;
    	   totalEventObject['wtype'] = riskwin;
    	   totalEventObject['datentime'] = dandt;
    	   totalEventObject['attempts'] = attemptsperday;
    	   totalEventObject = JSON.stringify(totalEventObject);
		   $(this).recordTotalEvent(totalEventObject);
	   } else if (submitType === 'ml') {
    	   mlEventObject['amount'] = wamount;
    	   mlEventObject['wtype'] = riskwin;
    	   mlEventObject['datentime'] = dandt;
    	   mlEventObject['attempts'] = attemptsperday;
    	   mlEventObject = JSON.stringify(mlEventObject);
		   $(this).recordMlEvent(mlEventObject);            		   
	   }
	   $(this).dialog("close");
	}

	// Setup the dialog
	$.fn.setupDialog = function(data) {
		var cancel = false;
		var sent = false;
		
		$('#oneattempt').on('click', function(data) {
			if ($('#oneattempt').prop('checked')) {
				$('#attemptcounter').css('display', 'none');
			} else {
				$('#attemptcounter').css('display', 'block');
			}
		});

		$('#eventamount').dialog({
			autoOpen: false, 
            buttons: {
               Continue: function() {
               		log('RadioButton: ' + $('input[name=RISKWIN]:checked').val());
               		log('Cancel: ' + cancel);

            	   var riskwin = $('input[name=RISKWIN]:checked').val();
            	   if (riskwin != '' &&
            		   $('#WAMT_').val() != '') {
            		   // Send the record
            		   log('Send Record');
            		   $.fn.sendRecord();
            		   sent = true;
            		   $('#WAMT_').val('');
            		   $('input[name=RISKWIN]').prop('checked', true);
            	   } else {
            		   sent = false;
            	   }
            	   cancel = false;
            	   $(this).dialog('close');
               },
			   Cancel: function() {
				   cancel = true;
			       $(this).dialog('close');
			       cancel = false;
			   }
            },
            beforeClose: function(event, ui) {
            	log('Wager2: ' + $('#WAMT_').val());
            	log('Cancel2: ' + cancel);
            	
            	if ($('#WAMT_').val() === '' && !cancel && !sent) {
            		alert("Please enter a wager amount");
            		event.preventDefault();
            		cancel = false;
            		sent = false;
            	}
            },
            width: 320
		});
	}

	// Process the event
	$.fn.processEvent = function(data) {
		log(data);
    	$('.game_id1').html(data.teamone.eventid);
    	$('.game_date_div').html($.fn.determineDate(new Date(data.eventdatetime)));
    	$('.game_id2').html(data.teamtwo.eventid);
    	$('.game_team_name1').html(data.teamone.team);
    	$('.game_team_name2').html(data.teamtwo.team);
    	if (accountname != '') {
    		$('.game-line-group-info').html(accountname);
    	} else {
    		$('.game-line-group-info').html(groupname);
    	}
    	$('.game-line-team-name').html(data.teamone.team + ' <span class="game_id_span">(' + data.teamone.eventid + ')</span>');
    	$('.game-line-team-name-2').html(data.teamtwo.team + ' <span class="game_id_span">(' + data.teamtwo.eventid + ')</span>');
    	$('.game-line-team-over').html('Over <span class="game_id_span">(' + data.teamone.eventid + ')</span>');
    	$('.game-line-team-under').html('Under <span class="game_id_span">(' + data.teamtwo.eventid + ')</span>');

    	// Setup buttons
    	$.fn.setupButtons();
    	$.fn.setupDateTime();
    	$.fn.setupDialog();
	}

	// Setup the transaction
	$.fn.setupTransaction = function() {
		// Call the service to get event
		$.fn.serviceCall('GET', '', 'restapi/event?eventid=' + eventid, 15000, function(data) {
			log(data);
			if (typeof data != 'undefined' && typeof data != null) {
				$.fn.processEvent(data);
			}
		});
	}

	// Get the specific event
	$.fn.getEvent = function(evntId, account_id, account, group_id, group) {
		accountid = account_id;
		accountname = account;
		groupid = group_id;
		groupname = group;
		eventid = evntId;

		$.fn.setupTransaction();
	}