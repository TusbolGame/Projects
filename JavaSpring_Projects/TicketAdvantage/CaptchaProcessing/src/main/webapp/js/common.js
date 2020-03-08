	var basicAuth;
	var user;
	var notAuthenticated = false;
	var mainNav;
	var level2Nav;
	var level3Nav;
	var level4Nav;
	var params1;
	var params2;
	var params3;
	var params4;
	var params5;

	log = function(message) {
		if (typeof console != 'undefined') {
			console.log(message);
		}
	}

	$.fn.setupBoolean = function(name) {
		var isChecked = document.getElementById(name).checked;
		var isactive = false;
		if (isChecked) {
			isactive = true;
		}
		return isactive;
	}

	// Function to setup all the variables
	$.fn.setNavValues = function(mn, l2, l3, l4, p1, p2, p3, p4, p5) {
		mainNav = mn;
		level2Nav = l2;
		level3Nav = l3;
		level4Nav = l4;
		params1 = p1;
		params2 = p2;
		params3 = p3;
		params4 = p4;
		params5 = p5;
	}

	function isDST(t) { //t is the date object to check, returns true if daylight saving time is in effect.
		var jan = new Date(t.getFullYear(),0,1);
		var jul = new Date(t.getFullYear(),6,1);
		return Math.min(jan.getTimezoneOffset(),jul.getTimezoneOffset()) == t.getTimezoneOffset();  
	}
	 
	// Date function
	$.fn.determineDate = function(date) {
		var months = date.getMonth() + 1;
		if (months < 10) {
			months = "0" + months;
		}
		var day = date.getDate();
		if (day < 10) {
			day = "0" + day;
		}
		var hours = date.getHours();
		var ampm = 'AM';
		if (hours >= 12) {
			hours = hours - 12;
			ampm = 'PM';
			if (hours === 0) {
				hours = '12';
			}
		}
		if (hours < 10) {
			hours = "0" + hours;
		}
		var minutes = date.getMinutes();
		if (minutes < 10) {
			minutes = "0" + minutes;
		}
		var tz = '';
		var timezn = date.getTimezoneOffset()/60;
		log('timezn: ' + timezn);
		if (isDST(date)) {
			if (timezn == 4) {
				tz = 'ET';
			} else if (timezn === 5) {
				tz = 'CT';
			} else if (timezn === 6) {
				tz = 'MT';
			} else if (timezn === 7) {
				tz = 'PT';
			}			
		} else {
			if (timezn == 5) {
				tz = 'ET';
			} else if (timezn === 6) {
				tz = 'CT';
			} else if (timezn === 7) {
				tz = 'MT';
			} else if (timezn === 8) {
				tz = 'PT';
			}
		}

		var dateString = months + "/" + day + "/" + date.getFullYear() + " " + hours + ":" + minutes + " " + ampm + " " + tz;
		return dateString;
	}

	// Error message popup OK button
	$.fn.errorButtonFunc = function() {
		if ($('#background-popup-error').hasClass('background_popup_error_on')) {
			$('#background-popup-error').removeClass('background_popup_error_on');			
		} else {
			$('#background').removeClass('background_on');
		}
		$('#error-message').removeClass('error_message_on');
		if (notAuthenticated) {
			window.location = "login.html";
		}
	}

	// Show the spinner
	$.fn.setupSpinner = function() {
		$('#background').addClass('background_on');
		$('#overlay-back').addClass('overlay_back_on');
	}
	// Disable spinner
	$.fn.disableSpinner = function() {
		$('#background').removeClass('background_on');
		$('#overlay-back').removeClass('overlay_back_on');
	}
	
	// Setup the header to send token and authorization info
	// this is used because we don't want hackers stealing our user "session"
	$.fn.setupHeader = function (jqXHR) {
		var token = Math.random().toString(36).substr(2) + Math.random().toString(36).substr(2);
		document.cookie = 'CSRF-TOKEN=' + token + '; path=/';
	    jqXHR.setRequestHeader('X-CSRF-TOKEN', token);
	    jqXHR.setRequestHeader('Authorization', basicAuth);
	}

	// Handle the error(s) in a uniform way
	$.fn.handleError = function(jqXHR, textStatus, errorThrown) {
	    	log(jqXHR);
	    	log(textStatus);
	    	log(errorThrown);
	    	// Let the status code function handle "normal" errors
	    	if (typeof jqXHR != 'undefined') {
	    		if (jqXHR.status === 401) {
	    			$('#background').addClass('background_on');
	    			$('#error-body').html('Error: User is not authenticated');
	            	$('#error-message').addClass('error_message_on');
	            	notAuthenticated = true;
	    			basicAuth = '';
	    		} else if (typeof jqXHR.responseJSON != 'undefined') {
	        		log(jqXHR.responseJSON.message);
	        		$('#background').addClass('background_on');
	                $('#error-body').html('Error: ' + jqXHR.responseJSON.message);
	                $('#error-message').addClass('error_message_on');
	        	} else {
	        		log(jqXHR.textStatus);
	        		$('#background').addClass('background_on');
	        		$('#error-body').html('Error: ' + textStatus);
	        		$('#error-message').addClass('error_message_on');
	        	}
	    	} else if (typeof textStatus != 'undefined' && textStatus != null) {
	    		$('#background').addClass('background_on');
	    		$('#error-h1').html('Error: ' + textStatus);
	    		$('#error-message').addClass('error_message_on');
	    	}
	}

	// Service call function
	$.fn.serviceCall = function(type, data, url, timeout, successCallback) {
		if (window.btoa) {
			encodedCredentials = btoa('jhmojax'+':'+'3id39d');
		} else {
	    		var Base64={_keyStr:"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=",encode:function(e){var t="";var n,r,i,s,o,u,a;var f=0;e=Base64._utf8_encode(e);while(f<e.length){n=e.charCodeAt(f++);r=e.charCodeAt(f++);i=e.charCodeAt(f++);s=n>>2;o=(n&3)<<4|r>>4;u=(r&15)<<2|i>>6;a=i&63;if(isNaN(r)){u=a=64}else if(isNaN(i)){a=64}t=t+this._keyStr.charAt(s)+this._keyStr.charAt(o)+this._keyStr.charAt(u)+this._keyStr.charAt(a)}return t},decode:function(e){var t="";var n,r,i;var s,o,u,a;var f=0;e=e.replace(/[^A-Za-z0-9+/=]/g,"");while(f<e.length){s=this._keyStr.indexOf(e.charAt(f++));o=this._keyStr.indexOf(e.charAt(f++));u=this._keyStr.indexOf(e.charAt(f++));a=this._keyStr.indexOf(e.charAt(f++));n=s<<2|o>>4;r=(o&15)<<4|u>>2;i=(u&3)<<6|a;t=t+String.fromCharCode(n);if(u!=64){t=t+String.fromCharCode(r)}if(a!=64){t=t+String.fromCharCode(i)}}t=Base64._utf8_decode(t);return t},_utf8_encode:function(e){e=e.replace(/rn/g,"n");var t="";for(var n=0;n<e.length;n++){var r=e.charCodeAt(n);if(r<128){t+=String.fromCharCode(r)}else if(r>127&&r<2048){t+=String.fromCharCode(r>>6|192);t+=String.fromCharCode(r&63|128)}else{t+=String.fromCharCode(r>>12|224);t+=String.fromCharCode(r>>6&63|128);t+=String.fromCharCode(r&63|128)}}return t},_utf8_decode:function(e){var t="";var n=0;var r=c1=c2=0;while(n<e.length){r=e.charCodeAt(n);if(r<128){t+=String.fromCharCode(r);n++}else if(r>191&&r<224){c2=e.charCodeAt(n+1);t+=String.fromCharCode((r&31)<<6|c2&63);n+=2}else{c2=e.charCodeAt(n+1);c3=e.charCodeAt(n+2);t+=String.fromCharCode((r&15)<<12|(c2&63)<<6|c3&63);n+=3}}return t}}
	    		encodedCredentials = Base64.encode('jhmojax'+':'+'3id39d');
	    	}
        basicAuth = ' Basic ' + encodedCredentials;
        document.cookie = 'BasicAuth=' + basicAuth + '; path=/';

		// Call the search service
	    $.ajax({
	        type: type,
	        contentType: 'application/json',
	        data: data,
	        url: url,
	        dataType: 'json',
	        timeout: timeout,
            beforeSend: function(jqXHR, settings) {
	            	$.fn.setupHeader(jqXHR);
	            	$.fn.setupSpinner();
            },
	        success: function(data) {
		        	log(data);
		        	successCallback(data);
	        },
            error: function(jqXHR, textStatus, errorThrown) {
	            	$.fn.disableSpinner();
	            	$.fn.handleError(jqXHR, textStatus, errorThrown);
	        },
	        statusCode: {
	        		// Auth error code
	            401: function(jqXHR, textStatus, errorThrown) {
		            	$.fn.disableSpinner();
		            	$.fn.handleError(jqXHR, textStatus, errorThrown);
	            },
	            // Not found error code
            		404: function(jqXHR, textStatus, errorThrown) {
	                	$.fn.disableSpinner();
	                	$.fn.handleError(jqXHR, textStatus, errorThrown);
	            }
	        },
	    }).then(function(data) {
	    		$.fn.disableSpinner();
	    }).responseJSON;		
	}

	// Service call function
	$.fn.serviceCallNoSpinner = function(type, data, url, timeout, successCallback) {
		// Call the search service
	    $.ajax({
	        type: type,
	        contentType: 'application/json',
	        data: data,
	        url: url,
	        dataType: 'json',
	        timeout: 120000,
            beforeSend: function(jqXHR, settings) {
            		$.fn.setupHeader(jqXHR);
            },
	        success: function(data) {
	        	log(data);
	        	successCallback(data);
	        },
            error: function(jqXHR, textStatus, errorThrown) {
            	$.fn.handleError(jqXHR, textStatus, errorThrown);
            },
	        statusCode: {
	        	// Auth error code
	            401: function(jqXHR, textStatus, errorThrown) {
	            	$.fn.handleError(jqXHR, textStatus, errorThrown);
	            },
	            // Not found error code
            	404: function(jqXHR, textStatus, errorThrown) {
                	$.fn.handleError(jqXHR, textStatus, errorThrown);
	            }
	        }
	    }).responseJSON;		
	}

	// Service call function
	$.fn.serviceCallNoSpinnerNoError = function(type, data, url, timeout, successCallback) {
		// Call the search service
	    $.ajax({
	        type: type,
	        contentType: 'application/json',
	        data: data,
	        url: url,
	        dataType: 'json',
	        timeout: 120000,
            beforeSend: function(jqXHR, settings) {
            	$.fn.setupHeader(jqXHR);
            },
	        success: function(data) {
	        	log(data);
	        	successCallback(data);
	        },
            error: function(jqXHR, textStatus, errorThrown) {
            	log(jqXHR);
            },
	        statusCode: {
	        	// Auth error code
	            401: function(jqXHR, textStatus, errorThrown) {
	            	$.fn.handleError(jqXHR, textStatus, errorThrown);
	            },
	            // Not found error code
            	404: function(jqXHR, textStatus, errorThrown) {
                	$.fn.handleError(jqXHR, textStatus, errorThrown);
	            }
	        }
	    }).responseJSON;		
	}

	// Dynamically create a class
	$.fn.createClass = function (name, rules) {
	    var style = document.createElement('style');
	    style.type = 'text/css';
	    document.getElementsByTagName('head')[0].appendChild(style);
	    if (!(style.sheet || {}).insertRule) {
	        (style.styleSheet || style.sheet).addRule(name, rules);
	    } else {
	        style.sheet.insertRule(name + "{" + rules + "}", 0);
	    }
	}

	// Dynamically delete a class
	$.fn.deleteClass = function (name) {
	    var hd = $('head').children();
	    hd.each(
			function(index) {
				var ele = $(this)[0];
				if (typeof ele != 'undefined' && ele != null) {
					var sheet = $(this)[0].sheet;
				    try {
						if (typeof sheet != 'undefined' && sheet != null && typeof sheet.cssRules != 'undefined' && sheet.cssRules != null) {
							if (typeof sheet.cssRules[0].selectorText != 'undefined' && sheet.cssRules[0].selectorText != null) {
								var cssEle = sheet.cssRules[0].selectorText;
								if (typeof cssEle != 'undefined' && cssEle != null && cssEle != '' && cssEle === name) {
									// Fix for IE 9/Opera
									var theThis = $(this)[0];
									$(theThis).remove();
								}
							}
						}
				    } catch (e) {
				        try {
							if (typeof sheet != 'undefined' && sheet != null && typeof sheet.cssRules != 'undefined' && sheet.cssRules != null) {
								if (typeof sheet.cssRules[0].selectorText != 'undefined' && sheet.cssRules[0].selectorText != null) {
									var cssEle = sheet.cssRules[0].selectorText;
									if (typeof cssEle != 'undefined' && cssEle != null && cssEle != '' && cssEle === name) {
										// Fix for IE 9/Opera
										var theThis = $(this)[0];
										$(theThis).remove();
									}
								}
							}
				        } catch (e) {
				            try {
								if (typeof sheet != 'undefined' && sheet != null && typeof sheet.cssRules != 'undefined' && sheet.cssRules != null) {
									if (typeof sheet.cssRules[0].selectorText != 'undefined' && sheet.cssRules[0].selectorText != null) {
										var cssEle = sheet.cssRules[0].selectorText;
										if (typeof cssEle != 'undefined' && cssEle != null && cssEle != '' && cssEle === name) {
											// Fix for IE 9/Opera
											var theThis = $(this)[0];
											$(theThis).remove();
										}
									}
								}
				            } catch (e) {
				                alert(e);
				            }
				        }
				    }
				}
			}
		);
	}

	// Get all the cookies
	$.fn.getCookies = function() {
		var pairs = document.cookie.split(";");
		var cookies = {};
		for (var i=0; i<pairs.length; i++){
		    var pair = pairs[i].split("=");
		    cookies[pair[0]] = unescape(pair[1]);
		}
		return cookies;
	}
	
	// Get an individual cookie
	$.fn.getCookie = function(c_name) {
	    var i, x, y, ARRcookies = document.cookie.split(";");
	    for (i = 0; i < ARRcookies.length; i++) {
	        x = ARRcookies[i].substr(0, ARRcookies[i].indexOf("="));
	        y = ARRcookies[i].substr(ARRcookies[i].indexOf("=") + 1);
	        x = x.replace(/^\s+|\s+$/g, "");
	        if (x == c_name) {
	            return unescape(y);
	        }
	    }
	}

	// Check for IE version
	$.fn.isIE = function() {
		  var myNav = navigator.userAgent.toLowerCase();
		  return (myNav.indexOf('msie') != -1) ? parseInt(myNav.split('msie')[1]) : false;
	}

	// Get parameter by name
	$.fn.getParameterByName = function(name) {
	    var match = RegExp('[?&]' + name + '=([^&]*)').exec(window.location.search);
	    var retValue = match && decodeURIComponent(match[1].replace(/\+/g, ' '));
	    if (retValue === 'undefined' || retValue === null || retValue === 'null') {
	    	retValue = '';
	    }
	    return retValue;
	}

	// Change the browser URL
	$.fn.changeUrl = function() {
		// First check if pushState is supported and if so is it enabled
		// Changes the browser URL to use the parameters so users can save searches and/or bookmark them
		if (history.pushState && typeof historyPushEnabled != 'undefined' && historyPushEnabled) {
			var menuNav = [];
			var menuQuery = '';
			var params = [];
			params[0] = params1;
			params[1] = params2;
			params[2] = params3;
			params[3] = params4;
			params[4] = params5;
			menuNav.push('<a href="javascript:$.fn.gotoNavLocation(\'\',\'' + params + '\');">Home</a>');

			if (typeof mainNav != 'undefined' && mainNav != '') {
				var navToShow = mainNav.charAt(0).toUpperCase() + mainNav.slice(1);
				menuNav.push('>&nbsp;<a href="javascript:$.fn.gotoNavLocation(\'' + mainNav + '\', \'' + params + '\');">' + navToShow + '</a>');
				menuQuery = mainNav;
			}
			if (typeof level2Nav != 'undefined' && level2Nav != '') {
				var navToShow = level2Nav.charAt(0).toUpperCase() + level2Nav.slice(1);
				menuNav.push('>&nbsp;<a href="javascript:$.fn.gotoNavLocation(\'' + level2Nav + '\', \'' + params + '\');">' + navToShow + '</a>');
				menuQuery = level2Nav;
			}
			if (typeof level3Nav != 'undefined' && level3Nav != '') {
				var navToShow = level3Nav.charAt(0).toUpperCase() + level3Nav.slice(1);
				menuNav.push('>&nbsp;<a href="javascript:$.fn.gotoNavLocation(\'' + level3Nav + '\', \'' + params + '\');">' + navToShow + '</a>');
				menuQuery = level3Nav;
			}
			if (typeof level4Nav != 'undefined' && level4Nav != '') {
				var navToShow = level4Nav.charAt(0).toUpperCase() + level4Nav.slice(1);
				menuNav.push('>&nbsp;<a href="javascript:$.fn.gotoNavLocation(\'' + level4Nav + '\', \'' + params + '\');">' + navToShow + '</a>');
				menuQuery = level4Nav;
			}

			pquery = '';
			if (params1 != '') {
				pquery += '&' + params1;
			}
			if (params2 != '') {
				pquery += '&' + params2;
			}
			if (params3 != '') {
				pquery += '&' + params3;
			}
			if (params4 != '') {
				pquery += '&' + params4;
			}
			if (params5 != '') {
				pquery += '&' + params5;
			}

			var query = '?nav=' + menuQuery + pquery;
			$('.menu_buttom_left').html(menuNav.join('\n'));
			menuNav.length = 0;
	    	var stateObj = { path: query };
			// IE9 has an issue with history
			if (navigator.userAgent.toLowerCase().indexOf("msie") != -1) {
				history.replaceState(stateObj, "newPage", query);
			} else {
				history.pushState(stateObj, "newPage", 'ticketadvantage.html' + query);
			}
		}
	}
	
	// Function to go to a specific location
	$.fn.gotoNavLocation = function(nav, params) {
		log('Nav: ' + nav);
		log('Params: ' + params);
		if (nav != '') {
			// Now check for which one
			if (nav === 'games') {
				$.fn.setNavValues('games', '', '', '', '', '', '', '', '');
				$.fn.getGames();
			} else if (nav === 'accounts') {
				$.fn.setNavValues('accounts', '', '', '', '', '', '', '', '');
				$.fn.getAccounts();
			} else if (nav === 'groups') {
				$.fn.setNavValues('groups', '', '', '', '', '', '', '', '');
				$.fn.getGroups();
			} else if (nav === 'manage') {
				$.fn.setNavValues('manage', '', '', '', '', '', '', '', '');
				$.fn.getManage();
			} else if (nav === 'transactions') {
				$.fn.setNavValues('transactions', '', '', '', '', '', '', '', '');
				$.fn.getTransactions();
			} else if (nav === 'search') {
				if (Array.isArray) {
				    var isArry =  Array.isArray(params);
					var nParams;
					if (isArry) {
						nParams = params;
					} else {
						nParams = params.split(',');
					}
					var searchText = nParams[0].split('=')[1];
					$('#search-text').val(searchText);
					$.fn.searchGame();
					$.fn.setNavValues('search', '', '', '', nParams[0], '', '', '', '');
				}
			} else if (nav === 'ncaaflines' || nav === 'ncaaffirst' || nav === 'ncaafsecond' ||
					nav === 'nfllines' || nav === 'nflfirst' || nav === 'nflsecond' ||
					nav === 'wnbalines' || nav === 'wnbafirst' || nav === 'wnbasecond' ||
					nav === 'nhllines' || nav === 'nhlfirst' || nav === 'nhlsecond' ||
					nav === 'mlblines') {
				$.fn.setNavValues('games', nav, '', '', '', '', '', '', '');
				$.fn.changeUrl();
				$.fn.eventGameLoad(nav);
			} else if (nav === 'event') {
				// Check for a valid array function
				if (Array.isArray) {
				    var isArry =  Array.isArray(params);
					var nParams;
					if (isArry) {
						nParams = params;
					} else {
						nParams = params.split(',');
					}
					var aid = nParams[0].split('=')[1];

					$.fn.setNavValues('games', nParams[1].split('=')[1], 'event', '', nParams[0], nParams[1], '', '', '');
					$.fn.changeUrl();
					$.fn.loadSelectedGame(aid);
				}
			} else if (nav === 'transaction') {
				// Check for a valid array function
				if (Array.isArray) {
				    var isArry =  Array.isArray(params);
					var nParams;
					if (isArry) {
						nParams = params;
					} else {
						nParams = params.split(',');
					}
					log('nParams: ' + nParams);
					var aid = nParams[0].split('=')[1];

					// Get the html first
			        $.get("eventdetails.html", function(data) {
			        	$("#app-container").html(data);
			        	eventtype = nParams[4].split('=')[1];
			        	$.fn.setNavValues('games', nParams[1].split('=')[1], 'event', 'transaction', nParams[0], nParams[1], nParams[2], nParams[3], nParams[4]);
			        	$.fn.changeUrl();
			        	var n = nParams[2].includes("account");
			        	var accountId = '';
			        	var accountName = '';
			        	var groupId = '';
			        	var groupName = '';

			        	if (n) {
			        		accountId = nParams[2].split('=')[1];
			        		accountName = nParams[3].split('=')[1];
			        	} else {
			        		groupId = nParams[2].split('=')[1];
			        		groupName = nParams[3].split('=')[1];
			        	}
			        	$.fn.getEvent(nParams[0].split('=')[1], accountId, accountName, groupId, groupName);
			        });
				}
			} else {
				// Unknown nav go to games
				$.fn.setNavValues('tahome', '', '', '', '', '', '', '', '');
				$.fn.getTaHome();				
			}
		} else {
			$.fn.setNavValues('tahome', '', '', '', '', '', '', '', '');
			$.fn.getTaHome();
		}		
	}

	// Check for URL parameters
	$.fn.checkUrlParamaters = function() {
		var nav = $.fn.getParameterByName('nav');
		var params = [];
		if (typeof nav != 'undefined') {
			if (nav === 'search') {
				var query = $.fn.getParameterByName('query');
				mainNav = 'search';
				level2Nav = '';
				level3Nav = '';
				level4Nav = '';
	
				params1 = 'query=' + query;
				params[0] = params1;
				params[1] = params2;
				params[2] = params3;
				params[3] = params4;
				params[4] = params5;
			} else if (nav === 'event') {
				var gid = $.fn.getParameterByName('id');
				var eventtype = $.fn.getParameterByName('eventtype');
				mainNav = 'games';
				level2Nav = eventtype;
				level3Nav = 'event';
				level4Nav = '';
	
				params1 = 'id=' + gid;
				params2 = 'eventtype=' + eventtype;
				params[0] = params1;
				params[1] = params2;
				params[2] = params3;
				params[3] = params4;
				params[4] = params5;
			} else if (nav === 'transaction') {
				var gid = $.fn.getParameterByName('id');
				var eventtype = $.fn.getParameterByName('eventtype');
				var accountid = $.fn.getParameterByName('accountid');
				var accountname = $.fn.getParameterByName('accountname');
				var groupid = $.fn.getParameterByName('groupid');
				var groupname = $.fn.getParameterByName('groupname');
				var etype = $.fn.getParameterByName('etype');
				mainNav = 'games';
				level2Nav = eventtype;
				level3Nav = 'event';
				level4Nav = 'transaction';
	
				params1 = 'id=' + gid;
				params2 = 'eventtype=' + eventtype;
				if (accountid != '') {
					params3 = 'accountid=' + accountid;
					params4 = 'accountname=' + accountname;					
				} else {
					params3 = 'groupid=' + groupid;
					params4 = 'groupname=' + groupname;
				}
				params5 = 'etype=' + etype;
				params[0] = params1;
				params[1] = params2;
				params[2] = params3;
				params[3] = params4;
				params[4] = params5;
			} else {
				mainNav = nav;
				level2Nav = '';
				level3Nav = '';
				level4Nav = '';
				
				params1 = '';
				params2 = '';
				params3 = '';
				params4 = '';
				params5 = '';
				params[0] = params1;
				params[1] = params2;
				params[2] = params3;
				params[3] = params4;
				params[4] = params5;
			}
		} else {
			mainNav = 'tahome';
			level2Nav = '';
			level3Nav = '';
			level4Nav = '';
			
			params1 = '';
			params2 = '';
			params3 = '';
			params4 = '';
			params5 = '';
			params[0] = params1;
			params[1] = params2;
			params[2] = params3;
			params[3] = params4;
			params[4] = params5;
		}
		$.fn.gotoNavLocation(nav, params);
	}

	// Get the users information
	$.fn.getUserInfo = function() {
		// Call the service to get user info
		if (typeof uId != 'undefined' && uId != null) {
			$.fn.serviceCall('GET', '', 'restapi/user/id/' + uId, 45000, function(data) {
				user = data;
				// Check for URL parameters
				$.fn.checkUrlParamaters();
			});
		} else {
			$.cookie('BasicAuth', null, { path: '/' });
			$.cookie('AgentInfo', null, { path: '/' });
			basicAuth = '';
			$('#widgetHeader-name').html('');
			window.document.location = 'login.html';
		}
	}