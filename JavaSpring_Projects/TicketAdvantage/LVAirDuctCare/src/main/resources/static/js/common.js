	var startday = 8;
	var endday = 17;

	log = function(message) {
		if (typeof console != 'undefined') {
			console.log(message);
		}
	}

	function sendEmail() {
		// First check for all fields
		if ($('#name').val() === '' || 
			$('#email').val() === '' || 
			$('#phone').val() === '' || 
			$('#zipcode').val() === '' || 
			$('#service').val() === '') {

			var message = $.fn.pleaseEnterAllInfo();
			alert(message);

			return;
		}

		document.getElementById('submitrequest').disabled = true;

		var data = '{"name":"' + $('#name').val() + '",' +
			'"email":"' + $('#email').val() + '",' + 
			'"phone":"' + $('#phone').val() + '",' +
			'"zipcode":"' + $('#zipcode').val() + '",' +
			'"service":"' + $('#service').val() + '",' +
			'"message":"' + $('#message').val() + '"}';

	    $.ajax({
	        type: 'PUT',
	        contentType: 'application/json',
	        data: data,
	        url: '/restapi/email',
	        timeout: 30000,
            beforeSend: function(jqXHR, settings) {
            		$.fn.setupHeader(jqXHR);
            },
	        success: function(data) {
				$('#name').val('');
				$('#email').val(''); 
				$('#phone').val('');
				$('#zipcode').val('');
				$('#service').val('');
				$('#message').val('');
		 		document.getElementById('submitrequest').disabled = false;
		 		var message = $.fn.informationReceived();
				alert(message);
	        },
            error: function(jqXHR, textStatus, errorThrown) {
				var message = $.fn.emailError();
				alert(message);
            },
	        statusCode: {
				// Auth error code
	            401: function(jqXHR, textStatus, errorThrown) {
					var message = $.fn.emailError();
					alert(message);
	            },
	            // Not found error code
            		404: function(jqXHR, textStatus, errorThrown) {
    					var message = $.fn.emailError();
    					alert(message);
	            }
	        },
	    });
	}
	
	function sendEmailDos() {
		// First check for all fields
		if ($('#quotename').val() === '' || 
			$('#quoteemail').val() === '' || 
			$('#quotephone').val() === '' || 
			$('#quotezipcode').val() === '' || 
			$('#quotemessage').val() === '') {
			var message = $.fn.pleaseEnterAllInfo();
			alert(message);

			return;
		}
		document.getElementById('submitrequestdos').disabled = true;

		var data = '{"name":"' + $('#quotename').val() + '",' +
		'"email":"' + $('#quoteemail').val() + '",' + 
		'"phone":"' + $('#quotephone').val() + '",' +
		'"zipcode":"' + $('#quotezipcode').val() + '",' +
		'"service":"' + $('#quoteservice').val() + '",' +
		'"message":"' + $('#quotemessage').val() + '"}';

	    $.ajax({
	        type: 'PUT',
	        contentType: 'application/json',
	        data: data,
	        url: '/restapi/email',
	        timeout: 30000,
            beforeSend: function(jqXHR, settings) {
            		$.fn.setupHeader(jqXHR);
            },
	        success: function(data) {
				$('#name').val('');
				$('#email').val(''); 
				$('#phone').val('');
				$('#zipcode').val('');
				$('#service').val('');
				$('#message').val('');
		 		document.getElementById('submitrequestdos').disabled = false;
		 		var message = $.fn.informationReceived();
				alert(message);
	        },
            error: function(jqXHR, textStatus, errorThrown) {
				var message = $.fn.emailError();
				alert(message);
            },
	        statusCode: {
				// Auth error code
	            401: function(jqXHR, textStatus, errorThrown) {
					var message = $.fn.emailError();
					alert(message);
	            },
	            // Not found error code
            		404: function(jqXHR, textStatus, errorThrown) {
    					var message = $.fn.emailError();
    					alert(message);
	            }
	        },
	    });
	}

	function openNav() {
		document.getElementById("bs-example-navbar-collapse-1").style.right = "0";
		$(".menubar").attr("onclick","closeNav()");
		$(".menubar").addClass('open-menu-icon');
		$(".menubar").removeClass('open');
		$("body").addClass('open-menu-body');
	}

	function closeNav() {
		document.getElementById("bs-example-navbar-collapse-1").style.right = "-100%";	
		$(".menubar").attr("onclick","openNav()");
		$(".menubar").removeClass('open-menu-icon');
		$(".menubar").addClass('open');
		$("body").removeClass('open-menu-body');
	}

	// Setup the header to send token and authorization info
	// this is used because we don't want hackers stealing our user "session"
	$.fn.setupHeader = function (jqXHR) {
		var token = Math.random().toString(36).substr(2) + Math.random().toString(36).substr(2);
		document.cookie = 'CSRF-TOKEN=' + token + '; path=/';
	    jqXHR.setRequestHeader('X-CSRF-TOKEN', token);
	    var basicAuth = 'sadf';
	    jqXHR.setRequestHeader('Authorization', basicAuth);
	}

	$.fn.save = function(callback) {
		var servicetypes = '';
		var serviceunits = '';
		
		var servicetype = $('#servicetype').val();
		log('servicetype: ' + servicetype);

		if (servicetype.length > 0) {
			var lengthrequired = 0;

			for (var i = 0; i < servicetype.length; i++) {
				var stype = parseInt(servicetype[i]);
				if (servicetypes === '') {
					servicetypes = stype;
				} else {
					servicetypes = servicetypes + ',' + stype;
				}
	
				if (stype === 1) {
					var serviceunit = $('#airductcleaningunits').val();
					if ((typeof serviceunit != 'undefined') && (serviceunit != null) && (serviceunit.length > 0)) {
						if (serviceunits === '') {
							serviceunits = serviceunit[0];
						} else {
							serviceunits = serviceunits + ',' + serviceunit[0];
						}
					}
				} else if (stype === 2) {
					var serviceunit = $('#evaporatorcoilunits').val();
					if ((typeof serviceunit != 'undefined') && (serviceunit != null) && (serviceunit.length > 0)) {
						if (serviceunits === '') {
							serviceunits = serviceunit[0];
						} else {
							serviceunits = serviceunits + ',' + serviceunit[0];
						}
					}
				} else if (stype === 3) {
					var serviceunit = $('#fanblowersunits').val();
					if ((typeof serviceunit != 'undefined') && (serviceunit != null) && (serviceunit.length > 0)) {
						if (serviceunits === '') {
							serviceunits = serviceunit[0];
						} else {
							serviceunits = serviceunits + ',' + serviceunit[0];
						}
					}
				} else if (stype === 4) {
					var serviceunit = $('#furnaceunits').val();
					if ((typeof serviceunit != 'undefined') && (serviceunit != null) && (serviceunit.length > 0)) {
						if (serviceunits === '') {
							serviceunits = serviceunit[0];
						} else {
							serviceunits = serviceunits + ',' + serviceunit[0];
						}
					}
				} else if (stype === 5) {
					var serviceunit = $('#chimneyunits').val();
					if ((typeof serviceunit != 'undefined') && (serviceunit != null) && (serviceunit.length > 0)) {
						if (serviceunits === '') {
							serviceunits = serviceunit[0];
						} else {
							serviceunits = serviceunits + ',' + serviceunit[0];
						}
					}
				} else if (stype === 6) {
					var serviceunit = $('#ovenhoodunits').val();
					if ((typeof serviceunit != 'undefined') && (serviceunit != null) && (serviceunit.length > 0)) {
						if (serviceunits === '') {
							serviceunits = serviceunit[0];
						} else {
							serviceunits = serviceunits + ',' + serviceunit[0];
						}
					}
				} else if (stype === 7) {
					var serviceunit = $('#dryerunits').val();
					if ((typeof serviceunit != 'undefined') && (serviceunit != null) && (serviceunit.length > 0)) {
						if (serviceunits === '') {
							serviceunits = serviceunit[0];
						} else {
							serviceunits = serviceunits + ',' + serviceunit[0];
						}
					}
				} else if (stype === 8) {
					var serviceunit = $('#otherunits').val();
					if ((typeof serviceunit != 'undefined') && (serviceunit != null) && (serviceunit.length > 0)) {
						if (serviceunits === '') {
							serviceunits = serviceunit[0];
						} else {
							serviceunits = serviceunits + ',' + serviceunit[0];
						}
					}
				}
			}
		}

		// Call the service to add an account
		var calendarData = '{"firstname":"' + $('#firstname').val() + 
			'","lastname":"' +  $('#lastname').val() + 
			'","phone":"' + $('#phone').val() + 
			'","email":"' + $('#email').val() + 
			'","address":"' + $('#address').val() + 
			'","city":"' + $('#city').val() + 
			'","zipcode":' + $('#zipcode').val() + 
			',"servicetype":"' + servicetypes + 
			'","serviceunits":"' + serviceunits + 
			'","message":"' + $('#message').val() + 
			'","appointmentstarttime":' + $('#appointmentstarttime').val() + 
			',"appointmentendtime":' + $('#appointmentendtime').val() + 
			',"appointmentday":"' + $('#appointmentday').val() + '"}';

	    $.ajax({
	        type: 'PUT',
	        contentType: 'application/json',
	        data: calendarData,
	        url: 'restapi/save',
	        timeout: 30000,
            beforeSend: function(jqXHR, settings) {
            		$.fn.setupHeader(jqXHR);
            },
	        success: function(data) {
	        		callback(data);
	        },
            error: function(jqXHR, textStatus, errorThrown) {
				var message = $.fn.emailError();
				alert(message);
            },
	        statusCode: {
				// Auth error code
	            401: function(jqXHR, textStatus, errorThrown) {
					var message = $.fn.emailError();
					alert(message);
	            },
	            // Not found error code
            		404: function(jqXHR, textStatus, errorThrown) {
        				var message = $.fn.emailError();
        				alert(message);
	            }
	        },
	    });
	}

	$.fn.delete = function(id, callback) {
	    $.ajax({
	        type: 'DELETE',
	        contentType: 'application/json',
	        data: '',
	        url: 'restapi/delete?id=' + id,
	        timeout: 30000,
            beforeSend: function(jqXHR, settings) {
            		$.fn.setupHeader(jqXHR);
            },
	        success: function(data) {
	        		callback();
	        },
            error: function(jqXHR, textStatus, errorThrown) {
				var message = $.fn.emailError();
				alert(message);
            },
	        statusCode: {
				// Auth error code
	            401: function(jqXHR, textStatus, errorThrown) {
					var message = $.fn.emailError();
					alert(message);
	            },
	            // Not found error code
            		404: function(jqXHR, textStatus, errorThrown) {
        				var message = $.fn.emailError();
        				alert(message);
	            }
	        },
	    }).then(function(data) {

	    }).responseJSON;
	}

	$.fn.findByDay = function (day, callback) {
	    $.ajax({
	        type: 'GET',
	        contentType: 'application/json',
	        data: '',
	        url: 'restapi/findbyday?day=' + day,
	        dataType: 'json',
	        timeout: 30000,
            beforeSend: function(jqXHR, settings) {
            		$.fn.setupHeader(jqXHR);
	        },
	        success: function(data) {
	        		callback(data);
	        },
	        error: function(jqXHR, textStatus, errorThrown) {
				var message = $.fn.emailError();
				alert(message);
	        },
	        statusCode: {
				// Auth error code
	            401: function(jqXHR, textStatus, errorThrown) {
					var message = $.fn.emailError();
					alert(message);
	            },
	            // Not found error code
	        		404: function(jqXHR, textStatus, errorThrown) {
	    				var message = $.fn.emailError();
	    				alert(message);
	            }
	        },
	    }).then(function(data) {
	    }).responseJSON;
	}

	$.fn.findByPhone = function (phone) {
	    $.ajax({
	        type: 'GET',
	        contentType: 'application/json',
	        data: '',
	        url: 'restapi/findbyphone?phone=' + phone,
	        dataType: 'json',
	        timeout: 30000,
            beforeSend: function(jqXHR, settings) {
            		$.fn.setupHeader(jqXHR);
            },
	        success: function(data) {
	        		log(data);
	        },
            error: function(jqXHR, textStatus, errorThrown) {
				var message = $.fn.emailError();
				alert(message);
            },
	        statusCode: {
				// Auth error code
	            401: function(jqXHR, textStatus, errorThrown) {
					var message = $.fn.emailError();
					alert(message);
	            },
	            // Not found error code
            		404: function(jqXHR, textStatus, errorThrown) {
        				var message = $.fn.emailError();
        				alert(message);
	            }
	        },
	    }).then(function(data) {
	    }).responseJSON;
	}

	$.fn.setupEvents = function(json) {
        var events = [];

			var servicetype = $('#servicetype').val();
			log('servicetype: ' + servicetype);
	
			if (servicetype.length > 0) {
				var lengthrequired = 0;
	
				for (var i = 0; i < servicetype.length; i++) {
					var stype = parseInt(servicetype[i]);
		
					if (stype === 1) {
						lengthrequired = $.fn.determineLength('airductcleaningunits', lengthrequired);
					} else if (stype === 2) {
						lengthrequired = $.fn.determineLength('evaporatorcoilunits', lengthrequired);
					} else if (stype === 3) {
						lengthrequired = $.fn.determineLength('fanblowersunits', lengthrequired);
					} else if (stype === 4) {
						lengthrequired = $.fn.determineLength('furnaceunits', lengthrequired);
					} else if (stype === 5) {
						lengthrequired = $.fn.determineLength('chimneyunits', lengthrequired);
					} else if (stype === 6) {
						lengthrequired = $.fn.determineLength('ovenhoodunits', lengthrequired);
					} else if (stype === 7) {
						lengthrequired = $.fn.determineLength('dryerunits', lengthrequired);
					} else if (stype === 8) {
						lengthrequired = $.fn.determineLength('otherunits', lengthrequired);
					} else {
						log('Should not be here!');
					}
				}
	
		        	var availtimes = [];
				availtimes = $.fn.determineAvailability(lengthrequired, json, availtimes);
				events = $.fn.setupCalendar(availtimes, events);
			}

		return events;
	}

	$.fn.determineLength = function(unitname, lengthrequired) {
		var serviceunits = $('#' + unitname).val();
		if ((typeof serviceunits != 'undefined') && (serviceunits != null) && (serviceunits.length > 0)) {
			var serviceunit = parseInt(serviceunits[0]);

			if (unitname === 'airductcleaningunits') {
				if (serviceunit === 1) {
					lengthrequired = lengthrequired + 2;
				} else if (serviceunit === 2) {
					lengthrequired = lengthrequired + 3;
				} else if (serviceunit === 3) {
					lengthrequired = lengthrequired + 4;
				} else {
					lengthrequired = lengthrequired + 8;
				}
			} else if (unitname === 'otherunits') {
				if (serviceunit === 1) {
					lengthrequired = lengthrequired + 2;
				} else if (serviceunit === 2 || serviceunit === 3) {
					lengthrequired = lengthrequired + 4;
				} else {
					lengthrequired = lengthrequired + 8;
				}
			} else {
				if (serviceunit === 1) {
					lengthrequired = lengthrequired + 1;
				} else if (serviceunit === 2 || serviceunit === 3) {
					lengthrequired = lengthrequired + 2;
				} else {
					lengthrequired = lengthrequired + 3;
				}
			}
		}

		return lengthrequired;
	}

	$.fn.determineAvailability = function(lengthrequired, json, availtimes) {
		log('jsonxxx: ' + json);

		if ((typeof json != 'undefined') && (json != null) && (json.length > 0)) {
			// Loop through existing appointments for the day
			for (var i=0; i < json.length; ++i) {
				var jsonstart = json[i].appointmentstarttime;
				var jsonend = json[i].appointmentendtime;
				var jsonlength = json[i].appointmentendtime - json[i].appointmentstarttime;
				
				// First remove any that conflict with existing
				availtimes = $.fn.checkAndRemoveTemp(json[i], availtimes);

				// Loop through all times of day
				for (var timeofday = startday; timeofday < endday; timeofday++) {
					log('determineAvailability() timeofday: ' + timeofday);
					log('determineAvailability() jsonstart: ' + jsonstart);
					var found = false;

					if (timeofday !== jsonstart) {
						log('determineAvailability() timeofday + lengthrequired: ' + (timeofday + lengthrequired));

						if ((timeofday + lengthrequired) <= jsonstart) {
							if (availtimes.length > 0) {
								found = $.fn.checkExisting(timeofday, lengthrequired, availtimes);
								log('determineAvailability() found: ' + found);

								if (found === false) {
									availtimes = $.fn.addTime(Math.random(), '', '', '', timeofday, timeofday + lengthrequired, lengthrequired, false, availtimes);
								}
							} else {
								availtimes = $.fn.addTime(Math.random(), '', '', '', timeofday, timeofday + lengthrequired, lengthrequired, false, availtimes);
							}
						} else if (timeofday >= jsonend) {
							if (availtimes.length > 0) {
								found = $.fn.checkExisting(timeofday, lengthrequired, availtimes);
								log('determineAvailability() found: ' + found);

								if (found === false) {
									availtimes = $.fn.addTime(Math.random(), '', '', '', timeofday, timeofday + lengthrequired, lengthrequired, false, availtimes);
								}
							} else {
								availtimes = $.fn.addTime(Math.random(), '', '', '', timeofday, timeofday + lengthrequired, lengthrequired, false, availtimes);
							}
						}
					}
				}
			}
		} else {
			if (lengthrequired > 0) {
				for (var timeofday = startday; timeofday < endday; timeofday += lengthrequired) {
					log('timeofday: ' + timeofday);
					availtimes = $.fn.addTime(Math.random(), '', '', '', timeofday, timeofday + lengthrequired, lengthrequired, false, availtimes);
				}
			}
		}

		return availtimes;
	}

	$.fn.checkExisting = function(timeofday, lengthrequired, availtimes) {
		log('Entering checkExisting()');
		var found = false;
		var aid = 0;

		for (var a=0; (typeof availtimes !== 'undefined') && (availtimes !== null) && (a < availtimes.length); ++a) {
			var astart = availtimes[a].start;
			var alength = availtimes[a].length;
			log('astart: ' + astart);
			log('alength: ' + alength);

			if (timeofday == astart) {
				found =true;
			} else if ((timeofday > astart) && (timeofday < (astart + alength))) {
				found = true;
			}
		}

		log('Exiting checkExisting()');
		return found;
	}

	$.fn.addTime = function(id, firstname, lastname, phone, start, end, lengthrequired, isexisting, availtimes) {
		log('Entering addTime()');
		log('id: ' + id);
		log('firstname: ' + firstname);
		log('lastname: ' + lastname);
		log('phone: ' + phone);
		log('start: ' + start);
		log('end: ' + end);
		log('lengthrequired: ' + lengthrequired);
		log('isexisting: ' + isexisting);

		if ((start + lengthrequired) <= endday) {
			log('startXXX: ' + start);
			log('lengthrequiredXXX: ' + lengthrequired);

			availtimes.push({
				eventid: id,
				firstname: firstname,
				lastname: lastname,
				phone: phone,
				start: start,
				end: end,
				length: lengthrequired,
				isexisting: isexisting
			});
		}

		log('Exiting addTime()');
		return availtimes;
	}
	
	$.fn.setupCalendar = function(availtimes, events) {
		log('Entering setupCalendar()');

		for (var t=0; (typeof availtimes !== 'undefined') && (availtimes !== null) && (t < availtimes.length); ++t) {
			if (availtimes[t].isexisting === true) {
				$.fn.createEvent(availtimes[t], events);
			} else {
				$.fn.createAvailableEvent(availtimes[t], events);
			}
		}

		log('Exiting setupCalendar()');
		return events;
	}

	$.fn.checkAndRemoveTemp = function(json, availtimes) {
		log('Entering checkAndRemoveTemp()');
		var jsonstart = json.appointmentstarttime;
		var jsonend = json.appointmentendtime;
		var jsonlength = jsonend - jsonstart;
		var iscomplete = false;

		if ((typeof availtimes !== 'undefined') && (availtimes !== null) && availtimes.length > 0) {
			for (var i = availtimes.length - 1; i >= 0; i--) {
				var astart = availtimes[i].start;
				var alength = availtimes[i].length;
				log('jsonstart: ' + jsonstart);
				log('astart: ' + astart);
				log('alength: ' + alength);
	
				if (jsonstart == astart && !availtimes[i].isexisting) {
					log('removing astart: ' + astart);
					availtimes.splice(i,1);
					
					if (iscomplete === false) {
						$.fn.addTime(json.id, json.firstname, json.lastname, json.phone, json.appointmentstarttime, json.appointmentendtime, json.appointmentendtime - json.appointmentstarttime, true, availtimes);
						iscomplete = true;
					}
				} else if ((jsonstart > astart) && (jsonstart < (astart + alength)) && !availtimes[i].isexisting) {					
					log('removing astart: ' + astart);
					availtimes.splice(i,1);

					if (iscomplete === false) {
						$.fn.addTime(json.id, json.firstname, json.lastname, json.phone, json.appointmentstarttime, json.appointmentendtime, json.appointmentendtime - json.appointmentstarttime, true, availtimes);
						iscomplete = true;
					}
				} else if ((jsonstart < astart) && ((jsonstart + jsonlength) <= (astart + alength)) && !availtimes[i].isexisting) {					
					log('removing astart: ' + astart);
					availtimes.splice(i,1);
					
					if (iscomplete === false) {
						$.fn.addTime(json.id, json.firstname, json.lastname, json.phone, json.appointmentstarttime, json.appointmentendtime, json.appointmentendtime - json.appointmentstarttime, true, availtimes);
						iscomplete = true;
					}
				}
			}
		} else {
			$.fn.addTime(json.id, json.firstname, json.lastname, json.phone, json.appointmentstarttime, json.appointmentendtime, json.appointmentendtime - json.appointmentstarttime, true, availtimes);
		}

		log('Exiting checkAndRemoveTemp()');
		return availtimes;
	}