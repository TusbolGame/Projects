var eid;

$.fn.setupAccountSelector = function(eid) {
	var data = user.accounts;
	var liString = [];

	// Setup an All Accounts
	liString.push('<div id="acctdiv0" class="acctdiv">');
	liString.push('  <input id="acct0" type="checkbox" onclick="javascript:$.fn.selectAllAccounts();" value="0"/><span class="acctdivspan">All Accounts</span>');
	liString.push('</div>');

	// Loop through all of the accounts
    for (var i=0; (typeof data != 'undefined') && (data != null) && (i < data.length); ++i)
    {
    		var odd = i % 2;
    		if (odd) {
    			// liString.push('<div id="acctdiv' + data[i].id + '" class="acctdiv account_list_odd">');
    			liString.push('<div id="acctdiv' + data[i].id + '" class="acctdiv">');
    		} else {
    			// liString.push('<div id="acctdiv' + data[i].id + '" class="acctdiv account_list_even">');
    			liString.push('<div id="acctdiv' + data[i].id + '" class="acctdiv">');
    		}
	    	liString.push('  <input id="acct' + data[i].id + '" onclick="javascript:void(0);" type="checkbox" value="' + data[i].id + '" /><span class="acctdivspan">' + data[i].name + '</span>');	
	    	liString.push('</div>');
    }
	$('#account-list').html(liString.join('\n'));
	liString.length = 0;		

	// Check for row being selected
    $('#account-list a').on('click', function(e) {
	    	e.preventDefault();
	    	var id = $(this).attr('id');
	    	var aid = $("[id='" + id + "_id']").val();
	    	var aname = $("[id='" + id + "_name']").val();

        $.get("eventdetails.html", function(data) {
        		$("#app-container").html(data);
        		log('EventTYPE: ' + eventtype);
			$.fn.setNavValues('games', level2Nav, 'event', 'transaction', params1, params2, 'accountid=' + aid, 'accountname=' + aname, 'etype=' + eventtype);
			$.fn.changeUrl();
			$.fn.getEvent(eid, aid, aname, '', '');
        });
    });
}

$.fn.setupGroupSelector = function(eid) {
	// Now setup the groups listing
	var data = user.groups;
	var liString = [];

	// Setup an All Groups
	liString.push('<div id="grpdiv0" class="grpdiv">');
	liString.push('  <input id="grp0" type="checkbox" onclick="javascript:$.fn.selectAllGroups();" value="0"/><span class="grpdivspan">All Groups</span>');
	liString.push('</div>');

	for (var i=0; (typeof data != 'undefined') && (data != null) && (i < data.length); ++i)
    {
    		liString.push('<div id="grpdiv' + data[i].id + '" class="grpdiv">');
    		liString.push('  <input id="grp' + data[i].id + '" onclick="javascript:void(0);" type="checkbox" value="' + data[i].id + '" /><span class="grpdivspan">' + data[i].name + '</span>');	
    		liString.push('</div>');
    }

	$('#group-list').html(liString.join('\n'));
	liString.length = [];

	$('#group-list a').on('click', function(e) {
	    	e.preventDefault();
	    	var id = $(this).attr('id');
	    	var gid = $("[id='" + id + "_id']").val();
	    	var gname = $("[id='" + id + "_name']").val();

        $.get("eventdetails.html", function(data) {
        		$("#app-container").html(data);
        		log('EventTYPE: ' + eventtype);
			$.fn.setNavValues('games', level2Nav, 'event', 'transaction', params1, params2, 'groupid=' + gid, 'groupname=' + gname, 'etype=' + eventtype);
			$.fn.changeUrl();
			$.fn.getEvent(eid, '', '', gid, gname);
        });
    });
}

$.fn.selectAllAccounts = function() {
	$('.acctdiv').each(function() {
		var input = $(this).find('input');
		log(input);
		if (input.attr('id') === 'acct0') {
			if (input.is(':checked')) {
				input.prop('checked', true);
			} else {
				input.prop('checked', false);
			}			
		} else {
			if (input.is(':checked')) {
				input.prop('checked', false);
			} else {
				input.prop('checked', true);
			}
		}
	});
}

$.fn.selectAllGroups = function() {
	$('.grpdiv').each(function() {
		var input = $(this).find('input');
		log(input);
		if (input.attr('id') === 'grp0') {
			if (input.is(':checked')) {
				input.prop('checked', true);
			} else {
				input.prop('checked', false);
			}			
		} else {
			if (input.is(':checked')) {
				input.prop('checked', false);
			} else {
				input.prop('checked', true);
			}
		}
	});	
}

$.fn.continueButton = function() {
	log('Calling continueButton()');
	var accounts = '';
	var accountcounter = 0;
	$(".acctdiv").each(function() {
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

	if (accounts !== '') {
        $.get("eventdetails.html", function(data) {
	    		$("#app-container").html(data);
	    		log('EventTYPE: ' + eventtype);
	    		$.fn.setNavValues('games', level2Nav, 'event', 'transaction', params1, params2, 'accountid=', 'accountname=', 'etype=' + eventtype);
			$.fn.changeUrl();
			$.fn.getEvent(eid, accounts, '', '', '');
			// transaction += '"accountids":[' + accounts + '],';
        });
	} else {
		// $.fn.getEvent(eid, '', '', '', '');
		// transaction += '"accountids":[],';
	}
	
	var groups = '';
	var groupscounter = 0;
	$(".grpdiv").each(function() {
		var group = '';
		var input = $(this).find('input');
		if (input.is(':checked')) {
			if (groupscounter > 0) {
				group = ',{"id":' + input.attr('value') + '}';
			} else {
				group = '{"id":' + input.attr('value') + '}';
			}
			groups += group;
			groupscounter++;
		}
	});

	if (groups !== '') {
        $.get("eventdetails.html", function(data) {
	    		$("#app-container").html(data);
	    		log('EventTYPE: ' + eventtype);
			$.fn.setNavValues('games', level2Nav, 'event', 'transaction', params1, params2, 'groupid=' + gid, 'groupname=' + gname, 'etype=' + eventtype);
			$.fn.changeUrl();
			$.fn.getEvent(eid, '', '', groups, '');
        });
	} else {
		// $.fn.getEvent(eid, '', '', '', '');
		// transaction += '"groupids":[]';
	}
}

$.fn.setupContinueButton = function(aid) {
	// Continue button click
	$('#button-continue').on('click', function() {
		$.fn.continueButton();
	});
}

// Show the accounts and groups
$.fn.setupAcctsGroups = function(aid) {
	eid = aid;

	// Show the accounts and groups
	$.fn.setupAccountSelector(eid);
	$.fn.setupGroupSelector(eid);
	$.fn.setupContinueButton();
}