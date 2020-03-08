	var playrowcount = 0;

	$.fn.setupAccountSelectorPreview = function() {
		// Remove the current list
		var data = user.accounts;
		var liString = [];

		// Setup an All Accounts
		liString.push('<div id="acctdiv0" class="acctdiv">');
		liString.push('  <input id="acct0" type="checkbox" value="0"/><span class="acctdivspan">All Accounts</span>');
		liString.push('</div>');

		// Loop through all of the accounts
	    for (var i=0; (typeof data != 'undefined') && (data != null) && (i < data.length); ++i)
	    {
		    	liString.push('<div id="acctdiv' + data[i].id + '" class="acctdiv">');
		    	liString.push('  <input id="acct' + data[i].id + '" onclick="javascript:void(0);" type="checkbox" value="' + data[i].id + '" /><span class="acctdivspan">' + data[i].name + '</span>');	
		    	liString.push('</div>');
	    }
		$('#account-list').html(liString.join('\n'));
		liString.length = 0;	
	}

	$.fn.setupGroupSelectorPreview = function() {
		// Now setup the groups listing
		var data = user.groups;
		var liString = [];

		// Setup an All Groups
		liString.push('<div id="grpdiv0" class="grpdiv">');
		liString.push('  <input id="grp0" type="checkbox" value="0"/><span class="grpdivspan">All Groups</span>');
		liString.push('</div>');

		for (var i=0; (typeof data != 'undefined') && (data != null) && (i < data.length); ++i)
	    {
		    	liString.push('<div id="grpdiv' + data[i].id + '" class="grpdiv">');
		    	liString.push('  <input id="grp' + data[i].id + '" onclick="javascript:void(0);" type="checkbox" value="' + data[i].id + '" /><span class="grpdivspan">' + data[i].name + '</span>');	
		    	liString.push('</div>');
	    }

		$('#group-list').html(liString.join('\n'));
		liString.length = [];
	}

	// Show the accounts and groups
	$.fn.setupAcctsGroupsPreview = function() {		
		// Show the accounts and groups
		$.fn.setupAccountSelectorPreview();
		$.fn.setupGroupSelectorPreview();
	}

	// Determine sport
	$.fn.determineSportEvent = function(sport, type) {
		return sport + type;
	}

	// Setup all the data
	$.fn.getTheGameData = function(betorpreview) {
		var rotationId = $('#bet-rotation').val();
		var sport = $('#bet-sport').val();
	    	var type = $('#bet-type').val();
	    	var linetype = $('input[name=bet-linetype]:checked').val();
	    	var lineindicator = $('#bet-lineindicator').val();
	    	var totalindicator = $('#bet-totalindicator').val();
	    	var line = $('#bet-line').val();
	    	var juiceindicator = $('#bet-juiceindicator').val();
	    	var juice = $('#bet-juice').val();
	    	var buyorder = $('#bet-buyorder').is(":checked");
	    	var amount = $('#bet-amount').val();
	    	var buyamount = $('#bet-buyamount').val();
	    	if (buyamount === '') {
	    		buyamount = '0';
	    	}
	    	var riskwin = $('input[name=bet-riskwin]:checked').val();
	
	    	// Get accounts
	    	var accounts = [];
		$(".acctdiv").each(function() {
			var acct = '';
			var input = $(this).find('input');
			if (input.is(':checked')) {			
				accounts.push(input.attr('value'));
			}
		});

		var groups = [];
		$(".grpdiv").each(function() {
			var input = $(this).find('input');
			if (input.is(':checked')) {
				groups.push(input.attr('value'));
			}
		});

		log('sport: ' + sport);
		log('type: ' + type)
		var sportType = $.fn.determineSportEvent(sport, type);
		
		if (betorpreview === 0) {
			// Bet
			$.fn.betTransaction(rotationId, sportType, linetype, lineindicator, totalindicator, line, juiceindicator, juice, buyorder, amount, buyamount, riskwin, accounts, groups);
		} else {
			// Preview
			
			if (linetype === 'total' || linetype === 'teamtotal') {
				$.fn.previewBet(rotationId, sportType, linetype, totalindicator, amount, accounts, groups);				
			} else {
				$.fn.previewBet(rotationId, sportType, linetype, lineindicator, amount, accounts, groups);				
			}
		}
	}

	// Bet
	$.fn.bet = function() {
		$.fn.getTheGameData(0);
	}

	// Preview
	$.fn.preview = function() {
		$.fn.getTheGameData(1);
	}

	// Load the bet page
	$.fn.loadBetPreview = function() {
		// Monitor the line type
		$('#bet-linetype-spread').on('click', function() {
			$("#bet-lineindicator-div").css("display", "inline-block");
			$("#bet-totalindicator-div").css("display", "none");
			$("#bet-juice-div").css("display", "inline-block");
	    });
		$('#bet-linetype-total').on('click', function() {
			$("#bet-lineindicator-div").css("display", "none");
			$("#bet-totalindicator-div").css("display", "inline-block");
			$("#bet-juice-div").css("display", "inline-block");
		});
		$('#bet-linetype-ml').on('click', function() {
			$("#bet-lineindicator-div").css("display", "inline-block");
			$("#bet-totalindicator-div").css("display", "none");
			$("#bet-juice-div").css("display", "none");
		});
		$('#bet-linetype-teamtotal').on('click', function() {
			$("#bet-lineindicator-div").css("display", "none");
			$("#bet-totalindicator-div").css("display", "inline-block");
			$("#bet-juice-div").css("display", "inline-block");
		});

		$('#bet-buyorder').mousedown(function() {
		    if (!$(this).is(':checked')) {
		    		$("#bet_buyamount_label").css("display", "inline-block");
		    		$("#bet-buyamount-div").css("display", "inline-block");
		    } else {
		    		$("#bet_buyamount_label").css("display", "none");
		    		$("#bet-buyamount-div").css("display", "none");
		    }
		});

	    // Continue Bet
		$('#bet-button-bet').on('click', function() {
			$.fn.getTheGameData(0);
	    });

	    // Preview Bet
		$('#bet-button-preview').on('click', function() {
			$.fn.getTheGameData(1);
	    });

		// Setup the UI
		$.fn.setupAcctsGroupsPreview();
	}