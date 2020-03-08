	// Get all accounts for user
	$.fn.getAccountList = function() {
	    	// Clear out the data
	    	$('#accountname').val('');
	    	$('#accountusername').val('');
	    	$('#accountpassword').val('');
	    	$('#accounturl').val('');
	    	$("#accountspreadlimit").val('');
	    	$("#accounttotallimit").val('');
	    	$("#accountmllimit").val('');
	    	$('#accountisactive').prop("checked", true);
	    	$('#accountismobile').prop("checked", false);
	    	$('#accountiscomplexcaptcha').prop("checked", false);
	    	$('#accountshowrequestresponse').prop("checked", true);
	    	$("#accounttimezone option[value='ET']").prop('selected', true);
	    	$("#accountownerpercentage option[value='80']").prop('selected', true);
	    	$("#accountpartnerpercentage option[value='20']").prop('selected', true);
	    	$("#accountproxylocation option[value='None']").prop('selected', true);
	    	$("#accountsitename option[value='None']").prop('selected', true);
	    	$("#hourbefore option[value='00']").prop('selected', true);
	    	$("#minutebefore option[value='00']").prop('selected', true);
	    	$("#hourafter option[value='00']").prop('selected', true);
	    	$("#minuteafter option[value='00']").prop('selected', true);
	
	    	// Reset the buttons
	    	$('#button-add-account').show();
	    	$('#button-update-account').hide();
	    	$('#button-delete-account').hide();

	    	// Remove the current list
		$('#account-list-nav').remove();

		// Call the service to get all accounts
		$.fn.serviceCall('GET', '', 'restapi/account/userid/' + uId, 45000, function(data) {
        		var liString = '';
            for (var i=0; (typeof data != 'undefined') && (data != null) && (i < data.length); ++i)
            {
	            	var odd = i % 2;
	            	liString += '<li id=' + data[i].id + '>';
	            	liString += '  <input id="' + data[i].name + '_id" type="hidden" value="' + data[i].id + '"/>';
	            	liString += '  <input id="' + data[i].name + '_name" type="hidden" value="' + data[i].name + '"/>';
	            	liString += '  <input id="' + data[i].name + '_username" type="hidden" value="' + data[i].username + '"/>';                        	
	            	liString += '  <input id="' + data[i].name + '_password" type="hidden" value="' + data[i].password + '"/>';
	            	liString += '  <input id="' + data[i].name + '_url" type="hidden" value="' + data[i].url + '"/>';
	            	liString += '  <input id="' + data[i].name + '_spreadlimit" type="hidden" value="' + data[i].spreadlimitamount + '"/>';
	            	liString += '  <input id="' + data[i].name + '_totallimit" type="hidden" value="' + data[i].totallimitamount + '"/>';
	            	liString += '  <input id="' + data[i].name + '_mllimit" type="hidden" value="' + data[i].mllimitamount + '"/>';
	            	liString += '  <input id="' + data[i].name + '_timezone" type="hidden" value="' + data[i].timezone + '"/>';
	            	liString += '  <input id="' + data[i].name + '_ownerpercentage" type="hidden" value="' + data[i].ownerpercentage + '"/>';
	            	liString += '  <input id="' + data[i].name + '_partnerpercentage" type="hidden" value="' + data[i].partnerpercentage + '"/>';
	            	liString += '  <input id="' + data[i].name + '_proxylocation" type="hidden" value="' + data[i].proxylocation + '"/>';
	            	liString += '  <input id="' + data[i].name + '_sitetype" type="hidden" value="' + data[i].sitetype + '"/>';
	            	liString += '  <input id="' + data[i].name + '_hourbefore" type="hidden" value="' + data[i].hourbefore + '"/>';
	            	liString += '  <input id="' + data[i].name + '_minutebefore" type="hidden" value="' + data[i].minutebefore + '"/>';
	            	liString += '  <input id="' + data[i].name + '_hourafter" type="hidden" value="' + data[i].hourafter + '"/>';
	            	liString += '  <input id="' + data[i].name + '_minuteafter" type="hidden" value="' + data[i].minuteafter + '"/>';
	            	liString += '  <input id="' + data[i].name + '_isactive" type="hidden" value="' + data[i].isactive + '"/>';
	            	liString += '  <input id="' + data[i].name + '_ismobile" type="hidden" value="' + data[i].ismobile + '"/>';
	            	liString += '  <input id="' + data[i].name + '_iscomplexcaptcha" type="hidden" value="' + data[i].iscomplexcaptcha + '"/>';
	            	liString += '  <input id="' + data[i].name + '_showrequestresponse" type="hidden" value="' + data[i].showrequestresponse + '"/>';
	            	if (odd) {
	            		liString += '  <a id="' + data[i].name + '" class="account_list_odd" href="javascript:void(0);">' + data[i].name  + '</a>';
	            	} else {
	            		liString += '  <a id="' + data[i].name + '" class="account_list_even" href="javascript:void(0);">' + data[i].name  + '</a>';
	            	}
	            	liString += '</li>';
            }
            $('#account-list').html(liString);

        		// Check for row being selected
            $('#account-list a').on('click', function(e) {
	            	e.preventDefault();
	            	var aid = $(this).attr('id');
	            	$("#accountname").val($("[id='" + aid + "_name']").val());
	            	$("#accountusername").val($("[id='" + aid + "_username']").val());
	            	$("#accountpassword").val($("[id='" + aid + "_password']").val());
	            	$("#accounturl").val($("[id='" + aid + "_url']").val());
	            	$("#accountspreadlimit").val($("[id='" + aid + "_spreadlimit']").val());
	            	$("#accounttotallimit").val($("[id='" + aid + "_totallimit']").val());
	            	$("#accountmllimit").val($("[id='" + aid + "_mllimit']").val());
	            	$("#accounttimezone").val($("[id='" + aid + "_timezone']").val());
	            	$("#accountownerpercentage").val($("[id='" + aid + "_ownerpercentage']").val());
	            	$("#accountpartnerpercentage").val($("[id='" + aid + "_partnerpercentage']").val());
	            	$("#accountproxylocation").val($("[id='" + aid + "_proxylocation']").val());
	            	$("#accountsitetype").val($("[id='" + aid + "_sitetype']").val());
	            	$("#hourbefore").val($("[id='" + aid + "_hourbefore']").val());
	            	$("#minutebefore").val($("[id='" + aid + "_minutebefore']").val());
	            	$("#hourafter").val($("[id='" + aid + "_hourafter']").val());
	            	$("#minuteafter").val($("[id='" + aid + "_minuteafter']").val());
	            	
	            	var isActive = $("[id='" + aid + "_isactive']").val();
	            	var active = true;
	            	if (typeof isActive != 'undefined' && isActive != '' && isActive === 'false') {
	            		active = false;
	            	}
	            	$("#accountisactive").prop("checked", active);

	            	var isMobile = $("[id='" + aid + "_ismobile']").val();
	            	var mobile = false;
	            	if (typeof isMobile != 'undefined' && isMobile != '' && isMobile === 'true') {
	            		mobile = true;
	            	}
	            	$("#accountismobile").prop("checked", mobile);

	            	var isComplexCaptcha = $("[id='" + aid + "_iscomplexcaptcha']").val();
	            	var complexcaptcha = false;
	            	if (typeof isComplexCaptcha != 'undefined' && isComplexCaptcha != '' && isComplexCaptcha === 'true') {
	            		complexcaptcha = true;
	            	}
	            	$("#accountiscomplexcaptcha").prop("checked", complexcaptcha);

	            	var logData = $("[id='" + aid + "_showrequestresponse']").val();
	            	var showrequestresponse = true;
	            	if (typeof logData != 'undefined' && logData != '' && logData === 'false') {
	            		showrequestresponse = false;
	            	}
	            	$("#accountshowrequestresponse").prop("checked", showrequestresponse);
	
	            	$("#accountnameid").val(aid);
	            	$("#button-add-account").hide();
	            	$("#button-update-account").show();
	            	$("#button-delete-account").show();
            });

            // List nav
			$('#account-list').listnav();
		});
	}

	// Function to add an account
	$.fn.addAccount = function() {
		if ($('#accountname').val() === '' || 
			$('#accountusername').val() === '' || 
			$('#accountpassword').val() === '' || 
			$('#accounturl').val() === '' ||  
			$("#accountspreadlimit").val() === '' ||
        		$("#accounttotallimit").val() === '' ||
        		$("#accountmllimit").val() === '' ||
			$('#accounttimezone').val() === '' || 
			$('#accountownerpercentage').val() === '' || 
			$('#accountpartnerpercentage').val() === '' || 
			$('#accountproxylocation').val() === '' ||
			$('#accountsitetype').val() === '')
		{
			$('#background').addClass('background_on');
			$('#error-body').html('Please enter all data into fields');
			$('#error-message').addClass('error_message_on');
		} else {
			// First get isActive indicator
			var isChecked = document.getElementById('accountisactive').checked;
			var isactive = false;
			if (isChecked) {
				isactive = true;
			}
	        	var isMobile = document.getElementById('accountismobile').checked;
	        	var mobile = false;
	        	if (isMobile) {
	        		mobile = true;
	        	}
	        	var isComplexCaptcha = document.getElementById('accountiscomplexcaptcha').checked;
	        	var complexcaptcha = false;
	        	if (isComplexCaptcha) {
	        		complexcaptcha = true;
	        	}
	        	var logData = document.getElementById('accountshowrequestresponse').checked;
	        	var showrequestresponse = false;
	        	if (logData) {
	        		showrequestresponse = true;
	        	}

			// Call the service to add an account
			var accountObject = '{"name":"' + $('#accountname').val() + '","username":"' +  $('#accountusername').val() + '","password":"' + $('#accountpassword').val() + '","url":"' + $('#accounturl').val() + '","spreadlimitamount":"' + $('#accountspreadlimit').val() + '","totallimitamount":"' + $('#accounttotallimit').val() + '","mllimitamount":"' + $('#accountmllimit').val() + '","timezone":"' + $('#accounttimezone').val() + '","ownerpercentage":"' + $('#accountownerpercentage').val() + '","partnerpercentage":"' + $('#accountpartnerpercentage').val() + '","proxylocation":"' + $('#accountproxylocation').val() + '","sitetype":"' + $('#accountsitetype').val() +
			'","hourbefore":"' + $('#hourbefore').val() + 
			'","minutebefore":"' + $('#minutebefore').val() + 
			'","hourafter":"' + $('#hourafter').val() + 
			'","minuteafter":"' + $('#minuteafter').val() + 
			'","isactive":' + isactive + 
			',"ismobile":' + mobile +
			',"iscomplexcaptcha":' + complexcaptcha +
			',"showrequestresponse":' + showrequestresponse + '}';
			log(accountObject);
			$.fn.serviceCall('POST', accountObject, 'restapi/account/id/' + uId, 45000, function(data) {
	            $.fn.getAccountList();
			});
		}
	}

	// Function to update an account
	$.fn.updateAccount = function() {
		if ($('#accountname').val() === '' || 
			$('#accountusername').val() === '' || 
			$('#accountpassword').val() === '' || 
			$('#accounturl').val() === '' ||
	        	$("#accountspreadlimit").val() === '' ||
	        	$("#accounttotallimit").val() === '' ||
	        	$("#accountmllimit").val() === '' ||
			$('#accounttimezone').val() === '' || 
			$('#accountownerpercentage').val() === '' || 
			$('#accountpartnerpercentage').val() === '' || 
			$('#accountproxylocation').val() === '' ||
			$('#accountsitetype').val() === '') {
			$('#background').addClass('background_on');
			$('#error-body').html('Please enter all data into fields');
        		$('#error-message').addClass('error_message_on');
		} else {
			// First get isActive indicator
			var isChecked = document.getElementById('accountisactive').checked;
			var isactive = false;
			if (isChecked) {
				isactive = true;
			}
	        	var isMobile = document.getElementById('accountismobile').checked;
	        	var mobile = false;
	        	if (isMobile) {
	        		mobile = true;
	        	}
	        	var isComplexCaptcha = document.getElementById('accountiscomplexcaptcha').checked;
	        	var complexcaptcha = false;
	        	if (isComplexCaptcha) {
	        		complexcaptcha = true;
	        	}
	        	var logData = document.getElementById('accountshowrequestresponse').checked;
	        	var showrequestresponse = false;
	        	if (logData) {
	        		showrequestresponse = true;
	        	}

			// Call the service to update an account
			var id = $("[id='" + $('#accountnameid').val() + "_id']").val();
			var accountObject = '{"id":"' + id + '","name":"' + $('#accountname').val() + '","username":"' +  $('#accountusername').val() + '","password":"' + $('#accountpassword').val() + '","url":"' + $('#accounturl').val() + '","spreadlimitamount":"' + $('#accountspreadlimit').val() + '","totallimitamount":"' + $('#accounttotallimit').val() + '","mllimitamount":"' + $('#accountmllimit').val() + '","timezone":"' + $('#accounttimezone').val() + '","ownerpercentage":"' + $('#accountownerpercentage').val() + '","partnerpercentage":"' + $('#accountpartnerpercentage').val() + '","proxylocation":"' + $('#accountproxylocation').val() + '","sitetype":"' + $('#accountsitetype').val() + 
			'","hourbefore":"' + $('#hourbefore').val() + 
			'","minutebefore":"' + $('#minutebefore').val() + 
			'","hourafter":"' + $('#hourafter').val() + 
			'","minuteafter":"' + $('#minuteafter').val() + 
			'","isactive":' + isactive + 
			',"ismobile":' + mobile + 
			',"iscomplexcaptcha":' + complexcaptcha +
			',"showrequestresponse":' + showrequestresponse + '}';
			log(accountObject);
			$.fn.serviceCall('POST', accountObject, 'restapi/account/update', 45000, function(data) {
	            $.fn.getAccountList();
			});
		}
	}

	// Function to delete an account
	$.fn.deleteAccount = function() {
		var id = $("[id='" + $('#accountnameid').val() + "_id']").val();
		$.fn.serviceCall('DELETE', '', 'restapi/account/delete/' + id + '?userid=' + uId, 45000, function(data) {
			// Call the service to get all accounts
			$.fn.getAccountList();
	    	$("#button-add-account").show();
	    	$("#button-update-account").hide();
	    	$("#button-delete-account").hide();
		});
	}

	// Load all accounts
	$.fn.loadAccounts = function() {
		// Hide update and delete on initial load
    		$('#button-update-account').hide();
    		$('#button-delete-account').hide();

    		// Add button click
	    	$('#button-add-account').on('click', function() {
		    	$.fn.addAccount();
	    	});

	    	// Update button click
	    	$('#button-update-account').on('click', function() {
		    	$.fn.updateAccount();
	    	});

	    	// Delete button click
	    	$('#button-delete-account').on('click', function() {
		    	$.fn.deleteAccount();
	    	});

	    	// Reset button click
	    	$('#button-reset-account').on('click', function() {
			    	// Clear out the data
		        	$("#accountname").val('');
		        	$("#accountusername").val('');
		        	$("#accountpassword").val('');
		        	$("#accounturl").val('');
		        	$("#accountspreadlimit").val('');
		        	$("#accounttotallimit").val('');
		        	$("#accountmllimit").val('');
		        	$("#accounttimezone option[value='ET']").prop('selected', true);
		        	$("#accountownerpercentage option[value='80']").prop('selected', true);
		        	$("#accountpartnerpercentage option[value='20']").prop('selected', true);
		        	$("#accountproxylocation option[value='None']").prop('selected', true);
		        	$("#accountsitetype option[value='None']").prop('selected', true);
			    	$("#hourbefore option[value='00']").prop('selected', true);
			    	$("#minutebefore option[value='00']").prop('selected', true);
			    	$("#hourafter option[value='00']").prop('selected', true);
			    	$("#minuteafter option[value='00']").prop('selected', true);
		        	$('#accountisactive').prop('checked', true);
		        	$('#accountismobile').prop("checked", false);
		        	$('#accountiscomplexcaptcha').prop("checked", false);
		        	$('#accountshowrequestresponse').prop("checked", true);
		
		        	// Reset the buttons
		        	$("#button-add-account").show();
		        	$("#button-update-account").hide();
		        	$("#button-delete-account").hide();
	    	});

	    	$("#accountlimit").keydown(function (e) {
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
	    $('#accounttimezone').on('change', function() {
		    	var selectedValue = $(this).find("option:selected").attr('value');
		    	$('[name=accounttimezone]').val(selectedValue);
	    });

	    // Account Owner selection
	    $('#accountownerpercentage').on('change', function() {
		    	var selectedValue = $(this).find("option:selected").attr('value');
		    	var partnerValue = 100 - selectedValue;
		    	$('[name=accountpartnerpercentage]').val(partnerValue);
	    });
	    
	    // Account Partner selection
	    $('#accountpartnerpercentage').on('change', function() {
		    	var selectedValue = $(this).find("option:selected").attr('value');
		    	var partnerValue = 100 - selectedValue;
		    	$('[name=accountownerpercentage]').val(partnerValue);
	    });

		// Call the service to get all proxy locations
		$.fn.serviceCall('GET', '', 'restapi/account/proxylocations', 45000, function(data) {
			if (typeof data != 'undefined' && data != '' && data != null && data.length > 0) {
				var optionData = [];
				for (var i = 0; i < data.length; i++) {
					optionData.push('<option value="' + data[i].name + '">' + data[i].name + '</option>');
				}
				$('#accountproxylocation').html(optionData.join('\n'));
				$("#accountproxylocation option[value='None']").prop('selected', true);
			}
		});

		// Call the service to get all site types
		$.fn.serviceCall('GET', '', 'restapi/account/sitetypes', 45000, function(data) {
			if (typeof data != 'undefined' && data != '' && data != null && data.length > 0) {
				var optionData = [];
				for (var i = 0; i < data.length; i++) {
					optionData.push('<option value="' + data[i].name + '">' + data[i].name + '</option>');
				}
				$('#accountsitetype').html(optionData.join('\n'));
				$("#accountsitetype option[value='None']").prop('selected', true);
			}
		});

	    $.fn.getAccountList();
	}