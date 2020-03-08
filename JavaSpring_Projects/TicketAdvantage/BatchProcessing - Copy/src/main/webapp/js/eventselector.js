var eid;

$.fn.setupAccountSelector = function(eid) {
	// Remove the current list
	$('#account-list-nav').remove();
	var data = user.accounts;
	var liString = [];

	// Setup an All Accounts
	liString.push('<li id="0">');
	liString.push('  <input id="all_accounts_id" type="hidden" value="0"/>');
	liString.push('  <input id="all_accounts_name" type="hidden" value="All Accounts"/>');
	liString.push('  <a id="all_accounts" class="account_list_odd" href="javascript:void(0);">All Accounts</a>');
	liString.push('</li>');
	
	// Loop through all of the accounts
    for (var i=0; (typeof data != 'undefined') && (data != null) && (i < data.length); ++i)
    {
    	var odd = i % 2;
    	liString.push('<li id="' + data[i].id + '">');
    	liString.push('  <input id="' + data[i].name + '_id" type="hidden" value="' + data[i].id + '"/>');
    	liString.push('  <input id="' + data[i].name + '_name" type="hidden" value="' + data[i].name + '"/>');
    	liString.push('  <input id="' + data[i].name + '_username" type="hidden" value="' + data[i].username + '"/>');                        	
    	liString.push('  <input id="' + data[i].name + '_password" type="hidden" value="' + data[i].password + '"/>');
    	liString.push('  <input id="' + data[i].name + '_url" type="hidden" value="' + data[i].url + '"/>');
    	liString.push('  <input id="' + data[i].name + '_ownerpercentage" type="hidden" value="' + data[i].ownerpercentage + '"/>');
    	liString.push('  <input id="' + data[i].name + '_partnerpercentage" type="hidden" value="' + data[i].partnerpercentage + '"/>');
    	liString.push('  <input id="' + data[i].name + '_vpnlocation" type="hidden" value="' + data[i].vpnlocation + '"/>');
    	liString.push('  <input id="' + data[i].name + '_isactive" type="hidden" value="' + data[i].isactive + '"/>');
    	if (odd) {
    		liString.push('  <a id="' + data[i].name + '" class="account_list_odd" href="javascript:void(0);">' + data[i].name  + '</a>');
    	} else {
    		liString.push('  <a id="' + data[i].name + '" class="account_list_even" href="javascript:void(0);">' + data[i].name  + '</a>');
    	}
    	liString.push('</li>');
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
	$('#account-list').listnav();
}

$.fn.setupGroupSelector = function(eid) {
	// Now setup the groups listing
	$('#group-list-nav').remove();
	var data = user.groups;
	var liString = [];

	// Setup an All Groups
	liString.push('<li id="0">');
	liString.push('  <input id="all_groups_id" type="hidden" value="0"/>');
	liString.push('  <input id="all_groups_name" type="hidden" value="All Groups"/>');
	liString.push('  <a id="all_groups" class="account_list_odd" href="javascript:void(0);">All Groups</a>');
	liString.push('</li>');

	for (var i=0; (typeof data != 'undefined') && (data != null) && (i < data.length); ++i)
    {
    	var odd = i % 2;
    	liString.push('<li id=' + data[i].id + '>');
    	liString.push('  <input id="' + data[i].name + '_id" type="hidden" value="' + data[i].id + '"/>');
    	liString.push('  <input id="' + data[i].name + '_name" type="hidden" value="' + data[i].name + '"/>');
    	liString.push('  <input id="' + data[i].name + '_isactive" type="hidden" value="' + data[i].isactive + '"/>');
    	if (odd) {
    		liString.push('  <a id="' + data[i].name + '" class="group_list_odd" href="javascript:void(0);">' + data[i].name  + '</a>');
    	} else {
    		liString.push('  <a id="' + data[i].name + '" class="group_list_even" href="javascript:void(0);">' + data[i].name  + '</a>');
    	}
    	liString.push('</li>');
    }
	$('#group-list').html(liString.join('\n'));
	liString.length = 0;
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
	$('#group-list').listnav();
}

// Show the accounts and groups
$.fn.setupAcctsGroups = function(aid) {
	eid = aid;
	log('EventTYPEAAA: ' + eventtype);
	
	// Show the accounts and groups
	$.fn.setupAccountSelector(eid);
	$.fn.setupGroupSelector(eid);
}