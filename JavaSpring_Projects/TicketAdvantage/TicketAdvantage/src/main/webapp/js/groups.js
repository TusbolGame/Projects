	// Show groups
	$.fn.showGroups = function(data) {
    	// Remove the current list
		$('#group-list-nav').remove();

		var liString = '';
        for (var i=0; (typeof data != 'undefined') && (data != null) && (i < data.length); ++i)
        {
        	var odd = i % 2;
        	liString += '<li id=' + data[i].id + '>';
        	liString += '  <input id="' + data[i].name + '_id" type="hidden" value="' + data[i].id + '"/>';
        	liString += '  <input id="' + data[i].name + '_name" type="hidden" value="' + data[i].name + '"/>';
        	liString += '  <input id="' + data[i].name + '_isactive" type="hidden" value="' + data[i].isactive + '"/>';
        	if (odd) {
        		liString += '  <a id="' + data[i].name + '" class="group_list_odd" href="javascript:void(0);">' + data[i].name  + '</a>';
        	} else {
        		liString += '  <a id="' + data[i].name + '" class="group_list_even" href="javascript:void(0);">' + data[i].name  + '</a>';
        	}
        	liString += '</li>';
        }
    	$('#group-list').html(liString);

    	// Check for row being selected
        $('#group-list a').on('click', function(e) {
        	e.preventDefault();
        	var aid = $(this).attr('id');
        	$("#groupname").val($("[id='" + aid + "_name']").val());
        	var isActive = $("[id='" + aid + "_isactive']").val();
        	var active = true;
        	if (typeof isActive != 'undefined' && isActive != '' && isActive === 'false') {
        		active = false;
        	}
        	$("#groupisactive").prop("checked", active);
        	$("#groupnameid").val(aid);
        	$("#button-add-group").hide();
        	$("#button-update-group").show();
        	$("#button-delete-group").show();
        });

		$('#group-list').listnav();
	}

	// Get all groups for user
	$.fn.getGroupList = function() {
    	// Clear out the data
    	$("#groupname").val('');
    	$('#groupisactive').prop("checked", true);

    	// Reset the buttons
    	$("#button-add-group").show();
    	$("#button-update-group").hide();
    	$("#button-delete-group").hide();

    	// Remove the current list
		$('#group-list-nav').remove();

		// Call the service to get all group
		$.fn.serviceCall('GET', '', 'restapi/group/userid/' + uId, 45000, function(data) {
			$.fn.showGroups(data);
		});
	}

	// Function to add a group
	$.fn.addGroup = function() {
		if ($('#groupname').val() === '') 
		{
			$('#background').addClass('background_on');
			$('#error-body').html('Please enter all data into fields');
        	$('#error-message').addClass('error_message_on');
		} else {
			// First get isActive indicator
			var isChecked = document.getElementById('groupisactive').checked;
			var isactive = false;
			if (isChecked) {
				isactive = true;
			}

			// Call the service to add group
			var groupObject = '{"name":"' + $('#groupname').val() + '","isactive":' + isactive + '}';
			log(groupObject);
			$.fn.serviceCall('POST', groupObject, 'restapi/group/id/' + uId, 45000, function(data) {
	            $.fn.getGroupList();
			});
		}
	}

	// Function to update a group
	$.fn.updateGroup = function() {
		if ($('#groupname').val() === '') 
		{
			$('#background').addClass('background_on');
			$('#error-body').html('Please enter all data into fields');
        	$('#error-message').addClass('error_message_on');
		} else {
			// First get isActive indicator
			var isChecked = document.getElementById('groupisactive').checked;
			var isactive = false;
			if (isChecked) {
				isactive = true;
			}

			// Call the service to update an group
			var id = $("[id='" + $('#groupnameid').val() + "_id']").val();
			var groupObject = '{"id":' + id + ',"name":"' + $('#groupname').val() + '","isactive":' + isactive + '}';
			log(groupObject);
			$.fn.serviceCall('POST', groupObject, 'restapi/group/update', 45000, function(data) {
	            $.fn.getGroupList();
			});
		}
	}

	// Function to delete an group
	$.fn.deleteGroup = function() {
		var id = $("[id='" + $('#groupnameid').val() + "_id']").val();
		$.fn.serviceCall('DELETE', '', 'restapi/group/delete/' + id + '?userid=' + uId, 45000, function(data) {
			// Call the service to get all groups
			$.fn.getGroupList();
	    	$("#button-add-group").show();
	    	$("#button-update-group").hide();
	    	$("#button-delete-group").hide();
		});
	}

	// Load all groups
	$.fn.loadGroups = function() {
		// Hide update and delete on initial load
    	$("#button-update-group").hide();
    	$("#button-delete-group").hide();

    	// Add button click
	    $('#button-add-group').on('click', function() {
	    	$.fn.addGroup();
	    });

    	// Update button click
	    $('#button-update-group').on('click', function() {
	    	$.fn.updateGroup();
	    });

    	// Delete button click
	    $('#button-delete-group').on('click', function() {
	    	$.fn.deleteGroup();
	    });

	    // Reset button click
	    $('#button-reset-group').on('click', function() {
	    	// Clear out the data
        	$("#groupname").val('');
        	$('#groupisactive').prop("checked", true);

        	// Reset the buttons
        	$("#button-add-group").show();
        	$("#button-update-group").hide();
        	$("#button-delete-group").hide();
	    });
	    $.fn.getGroupList();
	}