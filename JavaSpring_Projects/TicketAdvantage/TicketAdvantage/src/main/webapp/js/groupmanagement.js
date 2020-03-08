	var selectedList = [];

	// Setup account lists
	$.fn.setupAccountListing = function(data) {
    	// Remove the current list
		$('#account-list-nav').remove();

    	var liString = '';
        for (var i=0; i < data.length; ++i)
        {
        	var found = false;
        	for (var x=0; x < selectedList.length; x++) {
        		if (selectedList[x] === data[i].id) {
        			found = true;
        		}
        	}
        	if (!found) {
            	liString += '<li id=' + data[i].id + ' class="sortable-item">';
            	liString += '  <input id="' + data[i].name + '_name" type="hidden" value="' + data[i].name + '"/>';
            	liString += '  <input id="' + data[i].name + '_username" type="hidden" value="' + data[i].username + '"/>';                        	
            	liString += '  <input id="' + data[i].name + '_password" type="hidden" value="' + data[i].password + '"/>';
            	liString += '  <input id="' + data[i].name + '_url" type="hidden" value="' + data[i].url + '"/>';
            	liString += data[i].name;
            	liString += '</li>';
        	}
        }
        console.log(liString);
    	$('#account-list').html(liString);		
	}

	// Get the accounts
	$.fn.getAccountListing = function() {
		$.fn.serviceCall('GET', '', 'restapi/account/userid/' + uId, 45000, function(data) {
			$.fn.setupAccountListing(data);
		});
	}

	// Get the group
	$.fn.getGroup = function(id) {
		$.fn.serviceCall('GET', '', 'restapi/group/id/' + id, 45000, function(data) {
        	var selectedString = '';
        	selectedList = [];
        	if (typeof data != 'undefined' && data.accounts != null && data.accounts.length > 0) {
            	for (var j=0; j < data.accounts.length; ++j) {
            		selectedString += '<li id="' +  data.accounts[j].id + '" class="sortable-item">';
            		selectedString += data.accounts[j].name;
            		selectedString += '</li>';
            		selectedList[j] = data.accounts[j].id;
            	}
        	}
        	$('#sortable').html(selectedString);
        	$.fn.getAccountListing();
		});
	}

	// Setup the group list
	$.fn.setupGroup = function(data) {
    	var liString = '';
        for (var i=0; i < data.length; ++i)
        {
        	if (i === 0) {
        		liString += '<li id="' + data[i].id + '" class="group_selected">';
        	} else {
        		liString += '<li id="' + data[i].id + '" class="group_not_selected">';
        	}
        	liString += data[i].name;
        	liString += '</li>';
        }
    	$('#groups').html(liString);
	}

	// Show account data for the first time
	$.fn.setupAccountFirstTime = function(data) {
    	var selectedString = '';
    	if (typeof data != 'undefined' && data != null && data.length > 0) {
		    for (var j=0; j < data[0].accounts.length; ++j) {
		    	selectedString += '<li id="' +  data[0].accounts[j].id + '" class="sortable-item">';
		    	selectedString += data[0].accounts[j].name;
		    	selectedString += '</li>';
		    	selectedList[j] = data[0].accounts[j].id;
		    }
    	}
    	$('#sortable').html(selectedString);		
	}

	// Setup group selected
	$.fn.setupGroupSelected = function() {
        $('#groups li').click(function() {
        	var aid = $(this).attr('id');
        	var idGroup = $(this).val($(aid).val());

        	var listItems = $("#groups li");
        	listItems.each(function(idx, li) {
        		var product = $(li);
        	    if (product.hasClass('group_selected')) {
        	    	product.removeClass('group_selected').addClass('group_not_selected');
        	    }
        	});

        	var cs1 = idGroup.attr("class");
        	if (cs1 === 'group_selected') {
        		idGroup.removeClass('group_selected').addClass('group_not_selected');
        	} else {
        		idGroup.removeClass('group_not_selected').addClass('group_selected');
        	}
        	$.fn.getGroup(aid);
        });		
	}

	// Group Listing
	$.fn.getGroupListing = function() {
		if (typeof $("#account-list-nav") != 'undefined') {
			$("#account-list-nav").remove();
		}
		$.fn.serviceCall('GET', '', 'restapi/group/userid/' + uId, 45000, function(data) {
			// First get the groups
        	$.fn.setupGroup(data);
        	// Next set up the account for first time
        	$.fn.setupAccountFirstTime(data);
        	// Get the account listings
        	$.fn.getAccountListing();
        	// Setup the group selected
        	$.fn.setupGroupSelected();

    		$('#account-list').sortable({
    			receive: function(event, ui) {
    				var end_pos = ui.item.index();
    				var i = 0;
    				$("#account-list li").each(function(index) {
    					if (i === end_pos) {
    						var groupId = 1;
    						$("#groups li").each(function(index) {
    							var li = $(this);
    							if (li.hasClass('group_selected')) {
    								groupId = li.attr('id');
    							}
    						});
    						
    						// Call Delete
    						$.fn.serviceCall('DELETE', '{"groupid":"' + groupId + '","accountid":"' +  $(this).attr('id') + '"}', 'restapi/groupaccount', 45000, function(data) {
    			            	log('Success');
    						});
    					}
    					i++;
    				});
    			},
    		});

    		$('#sortable').sortable({
    			receive: function(event, ui) {
    				var end_pos = ui.item.index();
    				var i = 0;
    				$("#sortable li").each(function(index) {
    					if (i === end_pos) {
    						var groupId = 1;
    						$("#groups li").each(function(index) {
    							var li = $(this);
    							if (li.hasClass('group_selected')) {
    								groupId = li.attr('id');
    							}
    						});
    						$.fn.serviceCall('PUT', '{"groupid":"' + groupId + '","accountid":"' +  $(this).attr('id') + '"}', 'restapi/groupaccount', 45000, function(data) {
    			            	log('Success');
    						});
    					}
    					i++;
    				});
    			}
    		});

    		$('#account-info .sortable-list').sortable({
    			connectWith: '#account-info .sortable-list',
    			placeholder: 'placeholder',
    		});
		});
	}
	
	// Load all groups and accounts
	$.fn.loadGroupManagement = function() {
		selectedList = [];
		$.fn.getGroupListing();
	}