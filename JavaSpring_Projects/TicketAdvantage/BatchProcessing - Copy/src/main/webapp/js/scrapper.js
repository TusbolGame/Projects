var scrapperInfo = null;
var showScrapper = [];
var pendingRetry;

// Get all of the transactions
$.fn.getPendingBets = function() {
	$('#play-audio').trigger('stop');
	// Call the service to get all pending transactions
	$.fn.serviceCallNoSpinnerNoError('GET', '', 'restapi/scrapper/id/' + uId, 15000, function(data) {
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
	$.fn.serviceCallNoSpinnerNoError('GET', '', 'restapi/scrapper/id/' + uId, 15000, function(data) {
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
	clearInterval(startDelay);
	pendingRetry = setInterval($.fn.getPendingBets, 30000);
}