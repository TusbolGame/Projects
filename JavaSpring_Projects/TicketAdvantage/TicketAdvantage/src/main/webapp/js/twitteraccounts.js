	// Get all accounts for user
	$.fn.getTwitterAccountList = function() {
	    	// Clear out the data
	    	$('#twittername').val('');
	    	$('#twitterinet').val('');
	    	$('#twitteraccountid').val('');
	    	$('#twitterscreenname').val('');
	    	$('#twitterhandleid').val('');
	    	$("#twittersitetype").val('');
	    	$('#twitterisactive').prop("checked", true);
	
	    	// Reset the buttons
	    	$('#button-add-account').show();
	    	$('#button-update-account').hide();
	    	$('#button-delete-account').hide();
	
	    	// Remove the current list
		$('#twitter-list-nav').remove();

		// Call the service to get all accounts
		$.fn.serviceCall('GET', '', 'restapi/twitteraccount/userid/' + uId, 45000, function(data) {
        		var liString = '';
            for (var i=0; (typeof data != 'undefined') && (data != null) && (i < data.length); ++i)
            {
	            	var odd = i % 2;
	            	liString += '<li id=' + data[i].id + '>';
	            	liString += '  <input id="' + data[i].name + '_id" type="hidden" value="' + data[i].id + '"/>';
	            	liString += '  <input id="' + data[i].name + '_name" type="hidden" value="' + data[i].name + '"/>';
	            	liString += '  <input id="' + data[i].name + '_inet" type="hidden" value="' + data[i].inet + '"/>';
	            	liString += '  <input id="' + data[i].name + '_accountid" type="hidden" value="' + data[i].accountid + '"/>';
	            	liString += '  <input id="' + data[i].name + '_screenname" type="hidden" value="' + data[i].screenname + '"/>';
	            	liString += '  <input id="' + data[i].name + '_handleid" type="hidden" value="' + data[i].handleid + '"/>';
	            	liString += '  <input id="' + data[i].name + '_sitetype" type="hidden" value="' + data[i].sitetype + '"/>';
	            	liString += '  <input id="' + data[i].name + '_isactive" type="hidden" value="' + data[i].isactive + '"/>';
	            	if (odd) {
	            		liString += '  <a id="' + data[i].name + '" class="account_list_odd" href="javascript:void(0);">' + data[i].name  + '</a>';
	            	} else {
	            		liString += '  <a id="' + data[i].name + '" class="account_list_even" href="javascript:void(0);">' + data[i].name  + '</a>';
	            	}
	            	liString += '</li>';
            }

            $('#twitter-list').html(liString);

        		// Check for row being selected
        		$('#twitter-list a').on('click', function(e) {
	            	e.preventDefault();
	            	var aid = $(this).attr('id');
	
	            	$("#twittername").val($("[id='" + aid + "_name']").val());
	            	$("#twitterinet").val($("[id='" + aid + "_inet']").val());
	            	$("#twitteraccountid").val($("[id='" + aid + "_accountid']").val());
	            	$("#twitterscreenname").val($("[id='" + aid + "_screenname']").val());
	            	$("#twitterhandleid").val($("[id='" + aid + "_handleid']").val());
	            	$("#twittersitetype").val($("[id='" + aid + "_sitetype']").val());        	
	            	var isActive = $("[id='" + aid + "_isactive']").val();
	            	var active = true;
	            	if (typeof isActive != 'undefined' && isActive != '' && isActive === 'false') {
	            		active = false;
	            	}

	            	$("#twitterisactive").prop("checked", active);
	            	$("#twitternameid").val(aid);
	            	$("#button-add-account").hide();
	            	$("#button-update-account").show();
	            	$("#button-delete-account").show();
            });

            // List nav
			$('#twitter-list').listnav();
		});
	}

	// Function to add an account
	$.fn.addTwitterAccount = function() {
		if ($('#twittername').val() === '' || 
			$('#twitterinet').val() === '' ||
			$('#twitteraccountid').val() === '' ||
			$('#twitterscreenname').val() === '' || 
			$('#twitterhandleid').val() === '' || 
			$("#twittersitetype").val() === '')
		{
			$('#background').addClass('background_on');
			$('#error-body').html('Please enter all data into fields');
			$('#error-message').addClass('error_message_on');
		} else {
			isChecked = document.getElementById('twitterisactive').checked;
			var isactive = false;
			if (isChecked) {
				isactive = true;
			}

			// Call the service to add an account
			var twitterObject = '{"name":"' + $('#twittername').val() + 
				'","inet":"' +  $('#twitterinet').val() +
				'","accountid":"' +  $('#twitteraccountid').val() +
				'","screenname":"' + $('#twitterscreenname').val() + 
				'","handleid":"' + $('#twitterhandleid').val() +  
				'","sitetype":"' + $('#twittersitetype').val() + 
				'","isactive":' + isactive + '}';
			log(twitterObject);

			$.fn.serviceCall('POST', twitterObject, 'restapi/twitteraccount/id/' + uId, 45000, function(data) {
	            $.fn.getTwitterAccountList();
			});
		}
	}

	// Function to update an account
	$.fn.updateTwitterAccount = function() {
		if ($('#twittername').val() === '' || 
			$('#twitterinet').val() === '' ||
			$('#twitteraccountid').val() === '' ||
			$('#twitterscreenname').val() === '' || 
			$('#twitterhandleid').val() === '' || 
	        	$("#twittersitetype").val() === '')
		{
			$('#background').addClass('background_on');
			$('#error-body').html('Please enter all data into fields');
			$('#error-message').addClass('error_message_on');
		} else {
			isChecked = document.getElementById('twitterisactive').checked;
			var isactive = false;
			if (isChecked) {
				isactive = true;
			}

			// Call the service to add an account
			var id = $("[id='" + $('#twitternameid').val() + "_id']").val();
			var twitterObject = '{"id":' + id + 
				',"name":"' + $('#twittername').val() +
				'","inet":"' +  $('#twitterinet').val() + 
				'","accountid":"' +  $('#twitteraccountid').val() +
				'","screenname":"' + $('#twitterscreenname').val() + 
				'","handleid":"' + $('#twitterhandleid').val() + 
				'","sitetype":"' + $('#twittersitetype').val() + 
				'","isactive":' + isactive + '}';
			log(twitterObject);

			$.fn.serviceCall('POST', twitterObject, 'restapi/twitteraccount/update', 45000, function(data) {
	            $.fn.getTwitterAccountList();
			});
		}
	}

	// Function to delete an account
	$.fn.deleteTwitterAccount = function() {
		var id = $("[id='" + $('#twitternameid').val() + "_id']").val();

		$.fn.serviceCall('DELETE', '', 'restapi/twitteraccount/delete/' + id + '?userid=' + uId, 45000, function(data) {
			// Call the service to get all accounts
			$.fn.getTwitterAccountList();
			$("#button-add-account").show();
			$("#button-update-account").hide();
			$("#button-delete-account").hide();
		});
	}

	// Load all accounts
	$.fn.loadTwitterAccounts = function() {
		// Hide update and delete on initial load
		$('#button-update-account').hide();
		$('#button-delete-account').hide();

		// Add button click
	    $('#button-add-account').on('click', function() {
	    		$.fn.addTwitterAccount();
	    });

	    // Update button click
	    $('#button-update-account').on('click', function() {
	    		$.fn.updateTwitterAccount();
	    });

	    // Delete button click
	    $('#button-delete-account').on('click', function() {
	    		$.fn.deleteTwitterAccount();
	    });

	    // Reset button click
	    $('#button-reset-account').on('click', function() {
		    	// Clear out the data
		    	$('#twittername').val('');
		    	$('#twitterinet').val('');
		    	$('#twitteraccountid').val('');
		    	$('#twitterscreenname').val('');
		    	$('#twitterhandleid').val('');
		    	$("#twittersitetype").val('');
		    	$('#twitterisactive').prop("checked", true);
	
	        	// Reset the buttons
	        	$("#button-add-account").show();
	        	$("#button-update-account").hide();
	        	$("#button-delete-account").hide();
	    });

	    // Account Owner selection
	    $('#twittertimezone').on('change', function() {
	    		var selectedValue = $(this).find("option:selected").attr('value');
	    		$('[name=twittertimezone]').val(selectedValue);
	    });

	    $.fn.getTwitterAccountList();
	}