	// Get all accounts for user
	$.fn.getEmailAccountList = function() {
    	// Clear out the data
    	$('#emailname').val('');
    	$('#emailinet').val('');
    	$('#emailaccountid').val('');
    	$('#emailaddress').val('');
    	$('#emailpassword').val('');
    	$("#emailhost").val('');
    	$("#emailport").val('');
    	$('#emailtls').prop("checked", true);
    	$("#emailtimezone option[value='ET']").prop('selected', true);
    	$("#emailsitetype").val('');
    	$("#emailprovider").val('');
    	$("#emailtype").val('');
    	$("#emailauthenticationtype").val('');
    	$("#emailclientid").val('');
    	$("#emailclientsecret").val('');
    	$("#emailrefreshtoken").val('');
    	$("#emailgranttype").val('');
    	$('#emailisactive').prop("checked", true);

    	// Reset the buttons
    	$('#button-add-account').show();
    	$('#button-update-account').hide();
    	$('#button-delete-account').hide();

    	// Remove the current list
		$('#email-list-nav').remove();

		// Call the service to get all accounts
		$.fn.serviceCall('GET', '', 'restapi/emailaccount/userid/' + uId, 45000, function(data) {
        	var liString = '';
            for (var i=0; (typeof data != 'undefined') && (data != null) && (i < data.length); ++i)
            {
            	var odd = i % 2;
            	liString += '<li id=' + data[i].id + '>';
            	liString += '  <input id="' + data[i].name + '_id" type="hidden" value="' + data[i].id + '"/>';
            	liString += '  <input id="' + data[i].name + '_name" type="hidden" value="' + data[i].name + '"/>';
            	liString += '  <input id="' + data[i].name + '_inet" type="hidden" value="' + data[i].inet + '"/>';
            	liString += '  <input id="' + data[i].name + '_accountid" type="hidden" value="' + data[i].accountid + '"/>';
            	liString += '  <input id="' + data[i].name + '_address" type="hidden" value="' + data[i].address + '"/>';
            	liString += '  <input id="' + data[i].name + '_password" type="hidden" value="' + data[i].password + '"/>';
            	liString += '  <input id="' + data[i].name + '_host" type="hidden" value="' + data[i].host + '"/>';
            	liString += '  <input id="' + data[i].name + '_port" type="hidden" value="' + data[i].port + '"/>';
            	liString += '  <input id="' + data[i].name + '_tls" type="hidden" value="' + data[i].tls + '"/>';
            	liString += '  <input id="' + data[i].name + '_timezone" type="hidden" value="' + data[i].timezone + '"/>';
            	liString += '  <input id="' + data[i].name + '_sitetype" type="hidden" value="' + data[i].sitetype + '"/>';
            	liString += '  <input id="' + data[i].name + '_provider" type="hidden" value="' + data[i].provider + '"/>';
            	liString += '  <input id="' + data[i].name + '_emailtype" type="hidden" value="' + data[i].emailtype + '"/>';
            	liString += '  <input id="' + data[i].name + '_authenticationtype" type="hidden" value="' + data[i].authenticationtype + '"/>';
            	liString += '  <input id="' + data[i].name + '_clientid" type="hidden" value="' + data[i].clientid + '"/>';
            	liString += '  <input id="' + data[i].name + '_clientsecret" type="hidden" value="' + data[i].clientsecret + '"/>';
            	liString += '  <input id="' + data[i].name + '_refreshtoken" type="hidden" value="' + data[i].refreshtoken + '"/>';
            	liString += '  <input id="' + data[i].name + '_granttype" type="hidden" value="' + data[i].granttype + '"/>';
            	liString += '  <input id="' + data[i].name + '_isactive" type="hidden" value="' + data[i].isactive + '"/>';
            	if (odd) {
            		liString += '  <a id="' + data[i].name + '" class="account_list_odd" href="javascript:void(0);">' + data[i].name  + '</a>';
            	} else {
            		liString += '  <a id="' + data[i].name + '" class="account_list_even" href="javascript:void(0);">' + data[i].name  + '</a>';
            	}
            	liString += '</li>';
            }
        	$('#email-list').html(liString);

        	// Check for row being selected
            $('#email-list a').on('click', function(e) {
            	e.preventDefault();
            	var aid = $(this).attr('id');

            	$("#emailname").val($("[id='" + aid + "_name']").val());
            	$("#emailinet").val($("[id='" + aid + "_inet']").val());
            	$("#emailaccountid").val($("[id='" + aid + "_accountid']").val());
            	$("#emailaddress").val($("[id='" + aid + "_address']").val());
            	$("#emailpassword").val($("[id='" + aid + "_password']").val());
            	$("#emailhost").val($("[id='" + aid + "_host']").val());
            	$("#emailport").val($("[id='" + aid + "_port']").val());
            	var isTls = $("[id='" + aid + "_tls']").val();
            	var tls = true;
            	if (typeof isTls != 'undefined' && isTls != '' && isTls === 'false') {
            		tls = false;
            	}
            	$("#emailtls").prop("checked", tls);
            	$("#emailtimezone").val($("[id='" + aid + "_timezone']").val());
            	$("#emailsitetype").val($("[id='" + aid + "_sitetype']").val());
            	$("#emailprovider").val($("[id='" + aid + "_provider']").val());
            	$("#emailtype").val($("[id='" + aid + "_emailtype']").val());
            	$("#emailauthenticationtype").val($("[id='" + aid + "_authenticationtype']").val());
            	$("#emailclientid").val($("[id='" + aid + "_clientid']").val());
            	$("#emailclientsecret").val($("[id='" + aid + "_clientsecret']").val());
            	$("#emailrefreshtoken").val($("[id='" + aid + "_refreshtoken']").val());
            	$("#emailgranttype").val($("[id='" + aid + "_granttype']").val());            	
            	var isActive = $("[id='" + aid + "_isactive']").val();
            	var active = true;
            	if (typeof isActive != 'undefined' && isActive != '' && isActive === 'false') {
            		active = false;
            	}
            	$("#emailisactive").prop("checked", active);
            	$("#emailnameid").val(aid);
            	$("#button-add-account").hide();
            	$("#button-update-account").show();
            	$("#button-delete-account").show();
            });

            // List nav
			$('#email-list').listnav();
		});
	}

	// Function to add an account
	$.fn.addEmailAccount = function() {
		if ($('#emailname').val() === '' || 
			$('#emailinet').val() === '' ||
			$('#emailaccountid').val() === '' ||
			$('#emailaddress').val() === '' || 
			$('#emailhost').val() === '' ||  
        	$("#emailport").val() === '' ||
        	$("#emailtls").val() === '' ||
			$('#emailtimezone').val() === '' ||
        	$("#emailsitetype").val() === '' ||
			$('#emailprovider').val() === '' || 
			$('#emailtype').val() === '' || 
			$('#emailauthenticationtype').val() === '')
		{
			$('#background').addClass('background_on');
			$('#error-body').html('Please enter all data into fields');
        	$('#error-message').addClass('error_message_on');
		} else {
			var isChecked = document.getElementById('emailtls').checked;
			var istls = false;
			if (isChecked) {
				istls = true;
			}
			isChecked = document.getElementById('emailisactive').checked;
			var isactive = false;
			if (isChecked) {
				isactive = true;
			}

			// Call the service to add an account
			var emailObject = '{"name":"' + $('#emailname').val() + 
				'","inet":"' +  $('#emailinet').val() +
				'","accountid":"' +  $('#emailaccountid').val() +
				'","address":"' + $('#emailaddress').val() + 
				'","password":"' + $('#emailpassword').val() + 
				'","host":"' + $('#emailhost').val() + 
				'","port":"' + $('#emailport').val() + 
				'","tls":' + istls + 
				',"timezone":"' + $('#emailtimezone').val() + 
				'","sitetype":"' + $('#emailsitetype').val() + 
				'","provider":"' + $('#emailprovider').val() + 
				'","emailtype":"' + $('#emailtype').val() +
				'","authenticationtype":"' + $('#emailauthenticationtype').val() +
				'","clientid":"' + $('#emailclientid').val() +
				'","clientsecret":"' + $('#emailclientsecret').val() +
				'","refreshtoken":"' + $('#emailrefreshtoken').val() +
				'","granttype":"' + $('#emailgranttype').val() +
				'","isactive":' + isactive + '}';
			log(emailObject);
			$.fn.serviceCall('POST', emailObject, 'restapi/emailaccount/id/' + uId, 45000, function(data) {
	            $.fn.getEmailAccountList();
			});
		}
	}

	// Function to update an account
	$.fn.updateEmailAccount = function() {
		if ($('#emailname').val() === '' || 
				$('#emailinet').val() === '' ||
				$('#emailaccountid').val() === '' ||
				$('#emailaddress').val() === '' || 
				$('#emailhost').val() === '' ||  
	        	$("#emailport").val() === '' ||
	        	$("#emailtls").val() === '' ||
				$('#emailtimezone').val() === '' ||
	        	$("#emailsitetype").val() === '' ||
				$('#emailprovider').val() === '' || 
				$('#emailtype').val() === '' || 
				$('#emailauthenticationtype').val() === '')
		{
			$('#background').addClass('background_on');
			$('#error-body').html('Please enter all data into fields');
        	$('#error-message').addClass('error_message_on');
		} else {
			var isChecked = document.getElementById('emailtls').checked;
			var istls = false;
			if (isChecked) {
				istls = true;
			}
			isChecked = document.getElementById('emailisactive').checked;
			var isactive = false;
			if (isChecked) {
				isactive = true;
			}

			// Call the service to add an account
			var id = $("[id='" + $('#emailnameid').val() + "_id']").val();
			var emailObject = '{"id":' + id + 
				',"name":"' + $('#emailname').val() +
				'","inet":"' +  $('#emailinet').val() + 
				'","accountid":"' +  $('#emailaccountid').val() +
				'","address":"' + $('#emailaddress').val() + 
				'","password":"' + $('#emailpassword').val() + 
				'","host":"' + $('#emailhost').val() + 
				'","port":"' + $('#emailport').val() + 
				'","tls":' + istls + 
				',"timezone":"' + $('#emailtimezone').val() + 
				'","sitetype":"' + $('#emailsitetype').val() + 
				'","provider":"' + $('#emailprovider').val() + 
				'","emailtype":"' + $('#emailtype').val() +
				'","authenticationtype":"' + $('#emailauthenticationtype').val() +
				'","clientid":"' + $('#emailclientid').val() +
				'","clientsecret":"' + $('#emailclientsecret').val() +
				'","refreshtoken":"' + $('#emailrefreshtoken').val() +
				'","granttype":"' + $('#emailgranttype').val() +
				'","isactive":' + isactive + '}';
			log(emailObject);
			$.fn.serviceCall('POST', emailObject, 'restapi/emailaccount/update', 45000, function(data) {
	            $.fn.getEmailAccountList();
			});
		}
	}

	// Function to delete an account
	$.fn.deleteEmailAccount = function() {
		var id = $("[id='" + $('#emailnameid').val() + "_id']").val();
		$.fn.serviceCall('DELETE', '', 'restapi/emailaccount/delete/' + id + '?userid=' + uId, 45000, function(data) {
			// Call the service to get all accounts
			$.fn.getEmailAccountList();
	    	$("#button-add-account").show();
	    	$("#button-update-account").hide();
	    	$("#button-delete-account").hide();
		});
	}

	// Load all accounts
	$.fn.loadEmailAccounts = function() {
		// Hide update and delete on initial load
    	$('#button-update-account').hide();
    	$('#button-delete-account').hide();

    	// Add button click
	    $('#button-add-account').on('click', function() {
	    	$.fn.addEmailAccount();
	    });

    	// Update button click
	    $('#button-update-account').on('click', function() {
	    	$.fn.updateEmailAccount();
	    });

    	// Delete button click
	    $('#button-delete-account').on('click', function() {
	    	$.fn.deleteEmailAccount();
	    });

	    // Reset button click
	    $('#button-reset-account').on('click', function() {
	    	// Clear out the data
	    	$('#emailname').val('');
	    	$('#emailinet').val('');
	    	$('#emailaccountid').val('');
	    	$('#emailaddress').val('');
	    	$('#emailpassword').val('');
	    	$("#emailhost").val('');
	    	$("#emailport").val('');
	    	$('#emailtls').prop("checked", true);
	    	$("#emailtimezone option[value='ET']").prop('selected', true);
	    	$("#emailsitetype").val('');
	    	$("#emailprovider").val('');
	    	$("#emailtype").val('');
	    	$("#emailauthenticationtype").val('');
	    	$("#emailclientid").val('');
	    	$("#emailclientsecret").val('');
	    	$("#emailrefreshtoken").val('');
	    	$("#emailgranttype").val('');
	    	$('#emailisactive').prop("checked", true);

        	// Reset the buttons
        	$("#button-add-account").show();
        	$("#button-update-account").hide();
        	$("#button-delete-account").hide();
	    });

	    $("#emailport").keydown(function (e) {
	        // Allow: backspace, delete, tab, escape, enter and .
	        if ($.inArray(e.keyCode, [46, 8, 9, 27, 13, 110, 190]) !== -1 ||
	             // Allow: Ctrl+A
	            (e.keyCode == 65 && e.ctrlKey === true) ||
	             // Allow: Ctrl+C
	            (e.keyCode == 67 && e.ctrlKey === true) ||
	             // Allow: Ctrl+X
	            (e.keyCode == 88 && e.ctrlKey === true) ||
	             // Allow: home, end, left, right
	            (e.keyCode >= 35 && e.keyCode <= 39)) {
	                 // let it happen, don't do anything
	                 return;
	        }
	        // Ensure that it is a number and stop the keypress
	        if ((e.shiftKey || (e.keyCode < 48 || e.keyCode > 57)) && (e.keyCode < 96 || e.keyCode > 105)) {
	            e.preventDefault();
	        }
	    });

	    // Account Owner selection
	    $('#emailtimezone').on('change', function() {
	    	var selectedValue = $(this).find("option:selected").attr('value');
	    	$('[name=emailtimezone]').val(selectedValue);
	    });

	    $.fn.getEmailAccountList();
	}