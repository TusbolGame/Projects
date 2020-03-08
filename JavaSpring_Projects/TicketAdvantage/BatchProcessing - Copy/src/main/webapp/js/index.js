var basicAuth;
var userId;
$(document).ready(function() {
	$.fn.generateToken = function() {
		return Math.random().toString(36).substr(2) + Math.random().toString(36).substr(2);
	} 
	$.fn.handleError = function(jqXHR, textStatus, errorThrown) {
    	// Let the status code function handle "normal" errors
    	if (jqXHR.status != 401) {
    		if ($('#login_message_id') != 'undefined') {
            	$('#login_message_id').text('');
            	$('#login_message_id').hide();    			
    		}
        	if (typeof jqXHR.responseJSON != 'undefined') {
                $('#error_message_id').text(jqXHR.responseJSON.message);
                $('#error_message_id').show();
        	} else {
                $('#error_message_id').text(textStatus);
                $('#error_message_id').show();
        	}
    	}
	}
	$.fn.login = function(username, password) {
    	$.ajax({
            type: "POST",
            contentType: "application/json",
            data: '{"id":"","username":"' + username + '","password":"' + password + '"}',
            url: "restapi/login",
            dataType: "json",
            timeout: 15000,
            beforeSend: function(jqXHR, settings) {
                // Set the CSRF Token in the header for security
            	var token = $.fn.generateToken();
            	document.cookie = 'CSRF-TOKEN=' + token;
                jqXHR.setRequestHeader('X-CSRF-TOKEN', token);
            	$('#login_message_id').text('Logging in...');
            	$('#login_message_id').show();
            },
            success: function(data) {
            	userId = data.id;
            	var username = $('#username').val();
            	var password= $('#password').val();
            	var encodedCredentials = btoa(username+':'+password);
            	basicAuth = ' Basic ' + encodedCredentials;
            	$.get( "menu.html", function(data) {
            		$("#menuwrapper").html(data);
            		$.fn.enableButtons();
            	});
            	$.get( "games.html", function(data) {
                	$("#mainwrapper").removeClass('login-main-wrapper').addClass('main-wrapper');
            		$("#mainwrapper").html(data);
            		$.fn.loadGames();
            	});
            },
            error: function(jqXHR, textStatus, errorThrown) {
            	$.fn.handleError(jqXHR, textStatus, errorThrown);
            },
            statusCode: {
            	401: function(jqXHR, textStatus, errorThrown) {
	              $('#login_message_id').text('');
	              $('#login_message_id').hide();
                  $('#error_message_id').text(jqXHR.responseJSON.message);
                  $('#error_message_id').show();
                }
            },
        }).then(function(data) {
           $('.greeting-id').append(data.id);
           $('.greeting-content').append(data.content);
        }).responseJSON;		
	}
    $('#button_log').click(function() {
    	var username = $('#username').val();
    	var password= $('#password').val();
    	if (username === '') {
    		$('#error_message_id').text('Please enter a username');
    		$('#error_message_id').show();
    	} else if (password === '') {
    		$('#error_message_id').text('Please enter a password');
    		$('#error_message_id').show();    		
    	} else {
    		$('#error_message_id').text('');
    		$('#error_message_id').hide();
    		$.fn.login(username, password);
    	}
    });
    $('#show_password').click(function() {
		var isChecked = this.checked;
		if (isChecked) {
			// Have to use this syntax and not JQuery
			document.getElementById('password').type = 'text';
		} else {
			document.getElementById('password').type = 'password';
		}
	});
});

