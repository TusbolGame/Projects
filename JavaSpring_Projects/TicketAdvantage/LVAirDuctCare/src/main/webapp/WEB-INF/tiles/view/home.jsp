<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
	<script>
		var 	globaljson;
		var selectedOption;

	  	$(document).ready(function() {
	  		var now = new Date();
	  		var today = now.getFullYear() + "-" + (now.getMonth() + 1) + "-" + now.getDate();;
	  	  	log('today: ' + today);

		    $('#calendar').fullCalendar({
		      header: {
		        left: 'prev, next',
		        center: 'title',
		        right: 'today'
		      },
		      defaultView: 'agendaDay',
		      minTime: "08:00:00",
		      maxTime: "18:00:00",
		      hiddenDays: [0],
		      allDaySlot: false,
		      nowIndicator: true,
		      slotEventOverlap: false,
		      eventClick: function(calEvent, jsEvent, view) {
//		    	    alert('Event: ' + calEvent.title);
//		    	    alert('Event.isowner: ' + calEvent.isowner);
//		    	    alert('Coordinates: ' + jsEvent.pageX + ',' + jsEvent.pageY);
//		    	    alert('View: ' + view.name);

		    	    // change the border color just for fun
//		    	    $(this).css('border-color', 'red');
		    	    if (calEvent.isavailable) {
		    	    		log('calEvent.starttime: ' + calEvent.starttime);
		    	    		log('calEvent.endtime: ' + calEvent.endtime);
		    	    		log('today: ' + today);
		    	    		$('#appointmentstarttime').val(calEvent.starttime);
		    	    		$('#appointmentendtime').val(calEvent.endtime);
		    	    		
		    	    		var moment = $('#calendar').fullCalendar('getDate');
		    	    		var cdate = moment.format();
		    	    		log('cdate: ' + cdate);
		    	    		var thedate = cdate.split('T');
		    	    		$('#appointmentday').val(thedate[0]);
		    	    		
		    	    		if (calEvent.starttime > 12) {
		    	    			calEvent.starttime = calEvent.starttime - 12;
		    	    			calEvent.starttime = calEvent.starttime + ':00 pm';
		    	    		} else if (calEvent.starttime < 12) {
		    	    			calEvent.starttime = calEvent.starttime + ':00 am';
		    	    		} else if (calEvent.starttime === 12) {
		    	    			calEvent.starttime = calEvent.starttime + ':00 pm';
		    	    		}

		    	    		if (calEvent.endtime > 12) {
		    	    			calEvent.endtime = calEvent.endtime - 12;
		    	    			calEvent.endtime = calEvent.endtime + ':00 pm';
		    	    		} else if (calEvent.endtime < 12) {
		    	    			calEvent.endtime = calEvent.endtime + ':00 am';
		    	    		} else if (calEvent.endtime === 12) {
		    	    			calEvent.endtime = calEvent.endtime + ':00 pm';
		    	    		}
		    	    		
		    	    		$('#calendarbutton').html(thedate[0] + '  from ' + calEvent.starttime + ' - ' + calEvent.endtime);
		    	    		$('#myModal').modal('hide');
		    	    } else {
			    	    if (calEvent.isowner) {
			    	    		alert('<spring:message code="deleteappointment" text="default"/>');
			    	    		var moment = $('#calendar').fullCalendar('getDate');
			    	    		var cdate = moment.format();
			    	    		log('cdate: ' + cdate);
			    	    		var thedate = cdate.split('T');
			    	    		$('#appointmentday').val(thedate[0]);

			    	    		$.fn.delete(calEvent.eventid, function() {
							$.fn.findByDay(thedate[0], function(tjson) {
								var tevents = $.fn.setupEvents(tjson);
							    	$('#calendar').fullCalendar('removeEvents');
								$('#calendar').fullCalendar('renderEvents', tevents);
							});
			    	    		});
			    	    }
		    	    }
		    	  },
		      navLinks: false, // can click day/week names to navigate views
		      selectable: false,
		      selectHelper: true,
		      select: function(start, end) {
		        var title = prompt('Event Title:');
		        var eventData;
		        if (title) {
		          eventData = {
		            title: title,
		            start: start,
		            end: end
		          };
		          $('#calendar').fullCalendar('renderEvent', eventData, true); // stick? = true
		        }
		        $('#calendar').fullCalendar('unselect');
		      },
		      editable: false,
		      eventLimit: true, // allow "more" link when too many events
		      events: function(start, end, timezone, callback) {
	  	    		var moment = $('#calendar').fullCalendar('getDate');
		    		var cdate = moment.format();
		    		log('cdate: ' + cdate);
		    		var thedate = cdate.split('T');

		    	    $.ajax({
		    	      url: '/restapi/findbyday?day=' + thedate[0],
		    	      type: 'GET',
		    	      dataType: 'json',
		    	      contentType: 'application/json',
		    	      success: function(json) {
		    	    	  	globaljson = json;
		    	    	  	log('json: ' + json);
		    	    	  	var events = $.fn.setupEvents(json);
		    	        callback(events);
		    	      }
		    	    });
		    	  }
		    });

	  		$('#myModal').on('shown.bs.modal', function() {
	  	    		var moment = $('#calendar').fullCalendar('getDate');
		    		var cdate = moment.format();
		    		var thedate = cdate.split('T');

				$.fn.findByDay(thedate[0], function(tjson) {
					log('tjson: ' + tjson);
					var tevents = $.fn.setupEvents(tjson);
				    	$('#calendar').fullCalendar('removeEvents');
					$('#calendar').fullCalendar('renderEvents', tevents);
				});
	  		});

		    $('#calendarbutton').click(function() {
		    		var doshowmodal = true;
	  	    		var moment = $('#calendar').fullCalendar('getDate');
		    		var cdate = moment.format();
		    		var thedate = cdate.split('T');

				$.fn.findByDay(thedate[0], function(tjson) {
					log('tjson: ' + tjson);
					var tevents = $.fn.setupEvents(tjson);
				    	$('#calendar').fullCalendar('removeEvents');
					$('#calendar').fullCalendar('renderEvents', tevents);
				});

				if ($('#firstname').val() === null || $('#firstname').val() === '') {
					$('#firstnameerror').show();
					doshowmodal = false;
				} else {
					$('#firstnameerror').hide();
				}

				if ($('#lastname').val() === null || $('#lastname').val() === '') {
					$('#lastnameerror').show();
					doshowmodal = false;
				} else {
					$('#lastnameerror').hide();
				}

				if ($('#phone').val() === null || $('#phone').val() === '') {
					$('#phoneerror').show();
					doshowmodal = false;
				} else {
					$('#phoneerror').hide();
				}
				
				if ($('#address').val() === null || $('#address').val() === '') {
					$('#addresserror').show();
					doshowmodal = false;
				} else {
					$('#addresserror').hide();
				}

				if ($('#city').val() === null || $('#city').val() === '') {
					$('#cityerror').show();
					doshowmodal = false;
				} else {
					$('#cityerror').hide();
				}
				
				if ($('#zipcode').val() === null || $('#zipcode').val() === '') {
					$('#zipcodeerror').show();
					doshowmodal = false;
				} else {
					$('#zipcodeerror').hide();
				}

				var servicetype = $('#servicetype').val();
				log('servicetype: ' + servicetype);
				if (servicetype.length == 0) {
					$('#servicetypeerror').show();
					doshowmodal = false;
				} else {
					var tempdoshowmodal = false;

					for (var i = 0; i < servicetype.length; i++) {
						var stype = parseInt(servicetype[i]);

						if (stype === 1) {
							var serviceunit = $('#airductcleaningunits').val();
							if ((typeof serviceunit != 'undefined') && (serviceunit != null) && (serviceunit.length > 0)) {
								tempdoshowmodal = true;
							}
						} else if (stype === 2) {
							var serviceunit = $('#dryerunits').val();
							if ((typeof serviceunit != 'undefined') && (serviceunit != null) && (serviceunit.length > 0)) {
								tempdoshowmodal = true;
							}
						} else if (stype === 3) {
							var serviceunit = $('#chimneyunits').val();
							if ((typeof serviceunit != 'undefined') && (serviceunit != null) && (serviceunit.length > 0)) {
								tempdoshowmodal = true;
							}
						} else if (stype === 4) {
							var serviceunit = $('#furnaceunits').val();
							if ((typeof serviceunit != 'undefined') && (serviceunit != null) && (serviceunit.length > 0)) {
								tempdoshowmodal = true;
							}
						} else if (stype === 5) {
							var serviceunit = $('#ovenhoodunits').val();
							if ((typeof serviceunit != 'undefined') && (serviceunit != null) && (serviceunit.length > 0)) {
								doshowmodal = true;
							}
						} else if (stype === 6) {
							var serviceunit = $('#evaporatorcoilunits').val();
							if ((typeof serviceunit != 'undefined') && (serviceunit != null) && (serviceunit.length > 0)) {
								tempdoshowmodal = true;
							}
						} else if (stype === 7) {
							var serviceunit = $('#fanblowersunits').val();
							if ((typeof serviceunit != 'undefined') && (serviceunit != null) && (serviceunit.length > 0)) {
								tempdoshowmodal = true;
							}
						} else if (stype === 8) {
							var serviceunit = $('#otherunits').val();
							if ((typeof serviceunit != 'undefined') && (serviceunit != null) && (serviceunit.length > 0)) {
								tempdoshowmodal = true;
							}
						} else {
							log('Should not be here!');
						}
					}
					
					if (tempdoshowmodal) {
						$('#servicetypeerror').hide();
					}

					if (doshowmodal && !tempdoshowmodal) {
						$('#servicetypeerror').show();
						doshowmodal = false;
					}
				}

				return doshowmodal;
		    });

			$('.fc-prev-button').click(function() {
			});

			$('.fc-next-button').click(function() {
			});

			$(".fc-today-button").click(function () {
			});

			$("#submitrequest").click(function () {
				var subdate = $('#calendarbutton').html();

				if (subdate !== '<spring:message code="appointmentdatetime" text="default"/>' && subdate !== '') {
					$('#calendarbuttonerror').hide();

		    			$.fn.save(function() {
			  	    		var moment = $('#calendar').fullCalendar('getDate');
				    		var cdate = moment.format();
				    		var thedate = cdate.split('T');
	
						$.fn.findByDay(thedate[0], function(tjson) {
					    	  	var tevents = $.fn.setupEvents(tjson);
					    		$('#calendar').fullCalendar('removeEvents');
					    	  	$('#calendar').fullCalendar('renderEvents', tevents);
						});

						alert('<spring:message code="saveappointment" text="default"/>');
		    	    		});
				} else {
					$('#calendarbuttonerror').show();
				}
			});

            // create MultiSelect from select HTML element
            var required = $("#servicetype").kendoMultiSelect().data("kendoMultiSelect");

            $("#servicetype").on('change', function(evt, params) {
                var currentSelection;
                if (selectedOption) {
                		var currentValues = $(this).val();
                  	currentSelection = currentValues.filter(function(el) {
                    		return selectedOption.indexOf(el) < 0;
                  	});
                }

                selectedOption = $(this).val();
                log('currentSelection: ' + currentSelection);
                log('selectedOption: ' + selectedOption);

                if (typeof currentSelection !== 'undefined' && 
                		currentSelection != null && 
                		currentSelection.length == 0 && 
                		selectedOption != '') {
	            		var selectedunits = $('#serviceunitsform div');
	            		if (typeof selectedunits !== 'undefined' && selectedunits != null && selectedunits.length > 0) {
	            			var found = false;
		            		selectedunits.each(function(idx, unit) {
		   					var value = $(unit).attr("value");

			                	if (selectedOption.length > 0) {
			                		var found = false;
				                	for (var i = 0; i < selectedOption.length; i++) {
				                	   	// Trim the excess whitespace.
				                	   	selectedOption[i] = selectedOption[i].replace(/^\s*/, "").replace(/\s*$/, "");
				                	   
				                	   	// Add additional code here, such as:
				                	   	log('selectedOption: ' + selectedOption[i]);
				                	   	
				                	   	if (typeof value !== 'undefined' && value.toString() === selectedOption[i].toString()) {
				                	   		found = true;
				                	   	}
				                	}
				                	
				                	if (typeof value !== 'undefined' ) {
					                	if (!found) {
					                		$(unit).remove();
					                		$('#calendarbutton').html('<spring:message code="appointmentdatetime" text="default"/>');
					                	}
				                	}
			                	}
						});
	            		}
				} else if (typeof currentSelection !== 'undefined' && 
	                    		currentSelection != null && 
	                    		currentSelection.length == 0 && 
	                    		selectedOption.length == 0) {
	    	        		var selectedunits = $('#serviceunitsform div');
	    	            	if (typeof selectedunits !== 'undefined' && selectedunits != null && selectedunits.length > 0) {
	    		            	selectedunits.each(function(idx, unit) {
	    		   				var value = $(unit).attr("value");
	    		   				$(unit).remove();
	    		   				$('#calendarbutton').html('<spring:message code="appointmentdatetime" text="default"/>');
	    		            	});
	    		        }
                } else if ((typeof currentSelection !== 'undefined' && currentSelection != '') || 
                		(typeof currentSelection === 'undefined' && selectedOption != '')) {
                		var selection = '';
                		if (typeof currentSelection === 'undefined') {
                			selection = selectedOption.toString();
                		} else {
                			selection = currentSelection.toString();
                		}

	        			switch (selection) {
		            	    case "1":
		            	    		$.fn.setupUnits('airductcleaningunits', '1', '<spring:message code="numac" text="default"/>');
		            	    		break;
		            	    case "2":
		            	    		$.fn.setupUnits('dryerunits', '2', '<spring:message code="numdr" text="default"/>');
		            	    		break;
		            	    case "3":
		            	    		$.fn.setupUnits('chimneyunits', '3', '<spring:message code="numch" text="default"/>');
		            	    		break;
		            	    case "4":
		            	    		$.fn.setupUnits('furnaceunits', '4', '<spring:message code="numfr" text="default"/>');
		            	    		break;
		            	    case "5":
		            	    		$.fn.setupUnits('ovenhoodunits', '5', '<spring:message code="numov" text="default"/>');
		            	    		break;
		            	    case "6":
		            	    		$.fn.setupUnits('evaporatorcoilunits', '6', '<spring:message code="numcl" text="default"/>');
		            	    		break;
		            	    case "7":
		            	    		$.fn.setupUnits('fanblowersunits', '7', '<spring:message code="numfn" text="default"/>');
		            	    		break;
		            	    case "8":
		            	    		$.fn.setupUnits('otherunits', '8', '<spring:message code="numun" text="default"/>');
		            	    		break;
	        			}
                }
            	});

            var promise = document.querySelector('video').play();

            if (promise !== undefined) {
              promise.then(_ => {
				// Autoplay started!
              }).catch(error => {
				// Autoplay was prevented.
				// Show a "Play" button so that user can start playback.
				document.querySelector('video').muted = true;
				document.querySelector('video').play();
              });
            }
		});

	  	$.fn.setupUnits = function(theunit, value, placeholder) {
	  		log('theunit: ' + theunit);
	    		var unitsString = [];
		    	unitsString += '<div id="' + theunit + 'fg" value="' + value + '" class="form-group">';
		    	unitsString += '  <select class="form-control" id="' + theunit + '" name="' + theunit + '" multiple="multiple" name="serviceunits" data-role="multiselect" data-max-selected-items="1" data-placeholder="' + placeholder + '">';
	  		unitsString += '    <option value="1">1</option>';
	        unitsString += '    <option value="2">2</option>';
	        unitsString += '    <option value="3">3</option>';
	        	unitsString += '  </select>';
	        	unitsString += '</div>';
	        	log('unitsString: ' + unitsString);
	        	$('#serviceunitsform').append(unitsString);

	        // create MultiSelect from select HTML element
	        var optional = $('#' + theunit).kendoMultiSelect({
	            		maxSelectedItems: 1 //only one item could be selected
	        }).data("kendoMultiSelect");
	  	}

		$.fn.createEvent = function(availtime, events) {
			log('Entering createEvent()');
			var isowner = false;
			var phone = availtime.phone;
			var title = 'Taken';
			var backgroundColor = 'gray';

			log('phone: ' + phone);
			if (phone === $('#phone').val()) 
			{
				title = availtime.firstname + ' ' + availtime.lastname + '<spring:message code="clicktodelete" text="default"/>';
				isowner = true;
				backgroundColor = 'gray';
			}

			events.push({
				title: title,
				start: availtime.start + ':00', // will be parsed
				end: availtime.end + ':00', // will be parsed
				backgroundColor: backgroundColor,
				starttime: availtime.start,
				endtime: availtime.end,
				eventid: availtime.eventid,
				isowner: isowner
			});
			
			log('Exiting createEvent()');
		}
		
		$.fn.createAvailableEvent = function(availtime, events) {
			log('Entering createAvailableEvent()');

	  		events.push({
				title: '<spring:message code="timeavailable" text="default"/>',
				start: availtime.start + ':00', // will be parsed
				end: availtime.start + availtime.length + ':00', // will be parsed
				isowner: false,
				starttime: availtime.start,
				endtime: availtime.start + availtime.length,
				eventid: availtime.eventid,
				isavailable: true
			});
	  
	  		log('Exiting createAvailableEvent()');
		}
		
		$.fn.gotoBooking = function() {
			window.location.href = "/#booking";
		}
	</script>
	<section class="banner_area aos-init" data-aos="fade-up" data-aos-duration="600">
		<img src="images/airductcleaning.png" alt="">
		<div class="home_banner_inner">
			<h1>
				<span><spring:message code="welcometo" text="default"/></span><br>
	    			<spring:message code="title" text="default"/>
			</h1>
			<p class="aos-init" data-aos="fade-down" data-aos-duration="600"><spring:message code="lvserviceexpertsshort" text="default"/></p>
		</div>
		<div class="home_banner_inner_right">
			<video id="lvairvideo" controls="controls" autoplay="autoplay">
  				<source src="videos/LVAirDuctCare.mp4" type="video/mp4">
				Your browser does not support the video tag.
			</video>
		</div>
	</section>
	<section class="about_area">
    		<div class="container">
      		<div class="row">
      			<div class="col-md-6 col-sm-6 col-xs-12 quality_img aos-init text-right" data-aos="fade-left" data-aos-duration="600">
          			<img src="images/quality_img.png" alt=""> 
        			</div>
        			<div class="col-md-6 col-sm-6 col-xs-12 aos-init" data-aos="fade-right" data-aos-duration="600">
	            		<div class="about_address"><i class="fa fa-map-marker" aria-hidden="true"></i><spring:message code="street" text="default"/><br><spring:message code="citystatezip" text="default"/></div>
	            		<div class="about_address"><i class="fa fa-mobile" aria-hidden="true"></i> 702-331-9043</div>
				</div>
			</div>
		</div>
	</section>
<!-- 
	<section id="pricing" class="about_area why_area pricing_plan">
		<h2 class="aos-init" data-aos="fade-down" data-aos-duration="600">
			<spring:message code="pricing" text="default"/> <span><spring:message code="plan" text="default"/></span>
		</h2>
		<div class="container aos-init" data-aos="fade-down" data-aos-duration="600">
			<div class="item">
				<div class="tile">
					<div class="card-header">
						<h4 class="my-0 font-weight-normal"><spring:message code="airductcleaning" text="default"/></h4>
					</div>
					<div class="card-body">
						<h1 class="card-title pricing-card-title">
							$125 <small class="text-muted"><spring:message code="peraircondition" text="default"/></small>
						</h1>
						<ul class="list-unstyled mt-3 mb-4">
							<li><spring:message code="dirtyairductsmessage" text="default"/></li>
						</ul>
						<button type="button" class="btn btn-lg btn-block" onclick="javascript:$.fn.gotoBooking();"><spring:message code="getstarted" text="default"/></button>
					</div>
				</div>
			</div>
			<div class="item">
				<div class="tile">
					<div class="card-header">
						<h4 class="my-0 font-weight-normal"><spring:message code="dryerventcleaning" text="default"/></h4>
					</div>
					<div class="card-body">
						<h1 class="card-title pricing-card-title">
							$90 <small class="text-muted"><spring:message code="perdryer" text="default"/></small>
						</h1>
						<ul class="list-unstyled mt-3 mb-4">
							<li><spring:message code="dryermessage" text="default"/></li>
						</ul>
						<button type="button" class="btn btn-lg btn-block" onclick="javascript:$.fn.gotoBooking();"><spring:message code="getstarted" text="default"/></button>
					</div>
				</div>
			</div>
			<div class="item">
				<div class="tile">
					<div class="card-header">
						<h4 class="my-0 font-weight-normal"><spring:message code="chimneycleaning" text="default"/></h4>
					</div>
					<div class="card-body">
						<h1 class="card-title pricing-card-title">
							$85 <small class="text-muted"><spring:message code="flatfee" text="default"/></small>
						</h1>
						<ul class="list-unstyled mt-3 mb-4">
							<li><spring:message code="chimneymessage" text="default"/></li>
						</ul>
						<button type="button" class="btn btn-lg btn-block" onclick="javascript:$.fn.gotoBooking();"><spring:message code="getstarted" text="default"/></button>
					</div>
				</div>
			</div>
			<div class="item">
				<div class="tile">
					<div class="card-header">
						<h4 class="my-0 font-weight-normal"><spring:message code="furnacecleaning" text="default"/></h4>
					</div>
					<div class="card-body">
						<h1 class="card-title pricing-card-title">
							$50 <small class="text-muted"><spring:message code="perunit" text="default"/></small>
						</h1>
						<ul class="list-unstyled mt-3 mb-4">
							<li><spring:message code="furnacemessage" text="default"/></li>
						</ul>
						<button type="button" class="btn btn-lg btn-block" onclick="javascript:$.fn.gotoBooking();"><spring:message code="getstarted" text="default"/></button>
					</div>
				</div>
			</div>
			<div class="item">
				<div class="tile">
					<div class="card-header">
						<h4 class="my-0 font-weight-normal"><spring:message code="overhoodcleaning" text="default"/></h4>
					</div>
					<div class="card-body">
						<h1 class="card-title pricing-card-title">
							$125 <small class="text-muted"><spring:message code="perovenhood" text="default"/></small>
						</h1>
						<ul class="list-unstyled mt-3 mb-4">
							<li><spring:message code="ovenhoodmessage" text="default"/></li>
						</ul>
						<button type="button" class="btn btn-lg btn-block" onclick="javascript:$.fn.gotoBooking();"><spring:message code="getstarted" text="default"/></button>
					</div>
				</div>
			</div>
			<div class="item">
				<div class="tile">
					<div class="card-header">
						<h4 class="my-0 font-weight-normal"><spring:message code="evaporatorcoilcleaning" text="default"/></h4>
					</div>
					<div class="card-body">
						<h1 class="card-title pricing-card-title">
							$100 <small class="text-muted"><spring:message code="perunit" text="default"/></small>
						</h1>
						<ul class="list-unstyled mt-3 mb-4">
							<li><spring:message code="coilcleaning" text="default"/></li>
						</ul>
						<button type="button" class="btn btn-lg btn-block" onclick="javascript:$.fn.gotoBooking();"><spring:message code="getstarted" text="default"/></button>
					</div>
				</div>
			</div>
			<div class="item">
				<div class="tile">
					<div class="card-header">
						<h4 class="my-0 font-weight-normal"><spring:message code="fanblowercleaning" text="default"/></h4>
					</div>
					<div class="card-body">
						<h1 class="card-title pricing-card-title">
							$50 <small class="text-muted"><spring:message code="perunit" text="default"/></small>
						</h1>
						<ul class="list-unstyled mt-3 mb-4">
							<li><spring:message code="fanblowermessage" text="default"/></li>
						</ul>
						<button type="button" class="btn btn-lg btn-block" onclick="javascript:$.fn.gotoBooking();"><spring:message code="getstarted" text="default"/></button>
					</div>
				</div>
			</div>
			<div class="item">
				<div class="tile">
					<div class="card-header">
						<h4 class="my-0 font-weight-normal"><spring:message code="otherservices" text="default"/></h4>
					</div>
					<div class="card-body">
						<h1 class="card-title pricing-card-title">
							$50 <small class="text-muted"><spring:message code="dependingonservice" text="default"/></small>
						</h1>
						<ul class="list-unstyled mt-3 mb-4">
							<li><spring:message code="dependingonservice" text="default"/></li>
						</ul>
						<button type="button" class="btn btn-lg btn-block" onclick="javascript:$.fn.gotoBooking();"><spring:message code="getstarted" text="default"/></button>
					</div>
				</div>
			</div>
		</div>	
	</section>
 -->
	<section id="booking" class="service_area lasvegas_area feel_free appoinment">
		<div class="container">
			<h2>
				<spring:message code="appointment" text="default"/> <span><spring:message code="booking" text="default"/></span>
			</h2>
			<p><spring:message code="servingmessage" text="default"/></p>
			<div class="row aos-init" data-aos="fade-up" data-aos-duration="600">
				<div class="col-md-6 col-sm-12 col-12 contact_left">
					<img src="images/appointment.jpeg" alt="">
				</div>
				<div class="col-md-6 col-sm-12 col-xs-12 contact_right">
					<form>
						<div class="form-group">
							<input type="text" class="form-control" placeholder="<spring:message code="firstname" text="default"/>" value="" id="firstname">
							<div id="firstnameerror" style="display:none; color:red">
								<span><spring:message code="providefn" text="default"/></span>
							</div>
						</div>
						<div class="form-group">
							<input type="text" class="form-control" placeholder="<spring:message code="lastname" text="default"/>" value="" id="lastname">
							<div id="lastnameerror" style="display:none; color:red">
								<span><spring:message code="provideln" text="default"/></span>
							</div>
						</div>
						<div class="form-group">
							<input type="text" class="form-control" placeholder="<spring:message code="phonenumber" text="default"/>" value="" id="phone">
							<div id="phoneerror" style="display:none; color:red">
								<span><spring:message code="providepn" text="default"/></span>
							</div>
						</div>
						<div class="form-group">
							<input type="email" class="form-control" placeholder="<spring:message code="emailaddress" text="default"/>" value="" id="email">
						</div>
						<div class="form-group">
							<input type="text" class="form-control" placeholder="<spring:message code="physicaladdress" text="default"/>" value="" id="address">
							<div id="addresserror" style="display:none; color:red">
								<span><spring:message code="providead" text="default"/></span>
							</div>
						</div>
						<div class="form-group">
							<input type="text" class="form-control" placeholder="<spring:message code="cityname" text="default"/>" value="" id="city">
							<div id="cityerror" style="display:none; color:red">
								<span><spring:message code="providect" text="default"/></span>
							</div>
						</div>
						<div class="form-group">
							<input type="text" class="form-control" placeholder="<spring:message code="physicalzipcode" text="default"/>" value="" id="zipcode">
							<div id="zipcodeerror" style="display:none; color:red">
								<span><spring:message code="providezc" text="default"/></span>
							</div>
						</div>
						<div class="form-group">
							<select class="form-control" id="servicetype" multiple="multiple" name="servicetype" data-placeholder="<spring:message code="selectservices" text="default"/>">
								<option value="1"><spring:message code="airductcleaning" text="default"/></option>
								<option value="2"><spring:message code="dryerventcleaning" text="default"/></option>
								<option value="3"><spring:message code="chimneycleaning" text="default"/></option>
								<option value="4"><spring:message code="furnacecleaning" text="default"/></option>
								<option value="5"><spring:message code="overhoodcleaning" text="default"/></option>
								<option value="6"><spring:message code="evaporatorcoilcleaning" text="default"/></option>
								<option value="7"><spring:message code="fanblowercleaning" text="default"/></option>
								<option value="8"><spring:message code="otherservices" text="default"/></option>
							</select>
							<div id="servicetypeerror" style="display:none; color:red">
								<span><spring:message code="selecttypeunit" text="default"/></span>
							</div>
						</div>
						<div id="serviceunitsform">
						</div>
						<div class="form-group">
							<button id="calendarbutton" type="button" class="appoint_btn form-control" data-toggle="modal" data-target="#myModal"><spring:message code="appointmentdatetime" text="default"/></button>
							<div id="calendarbuttonerror" style="display:none; color:red">
								<span><spring:message code="selecectdatetime" text="default"/></span>
							</div>
						</div>
						<div class="form-group">
							<textarea class="form-control" placeholder="<spring:message code="commentmessage" text="default"/>"></textarea>
						</div>
						<input type="hidden" class="form-control" value="" id="appointmentstarttime">
						<input type="hidden" class="form-control" value="" id="appointmentendtime">
						<input type="hidden" class="form-control" value="" id="appointmentday">
						<button type="button" class="btn btn-default send_btn" id="submitrequest"><spring:message code="submitrequest" text="default"/></button>
					</form>
				</div>
			</div>
		</div>
	</section>
	<section class="about_area commercial_area">
		<div class="container">
			<h2 class="aos-init" data-aos="fade-down" data-aos-duration="600"><spring:message code="commericalservices" text="Commercial Services"/></h2>
			<ul class="aos-init" data-aos="fade-up" data-aos-duration="600">
				<li><i class="fa fa-check-square-o" aria-hidden="true"></i> <spring:message code="airductcleaning" text="Air Duct Cleaning"/></li>              
				<li><i class="fa fa-check-square-o" aria-hidden="true"></i> <spring:message code="dryerventcleaning" text="Dryer Vent Cleaning"/></li>              
				<li><i class="fa fa-check-square-o" aria-hidden="true"></i> <spring:message code="furnacecleaning" text="Furnace Cleaning"/></li>
			</ul>
			<button type="button" class="quote_btn aos-init" data-aos="fade-up" data-aos-duration="600" data-toggle="modal" data-target="#modalCenter"><spring:message code="getafreequote" text="default"/></button>
		</div>
	</section>
	<section class="about_area why_area">
		<div class="container">
			<h2 class="aos-init" data-aos="fade-down" data-aos-duration="600"><spring:message code="whyare" text="default"/> <span><spring:message code="wedifferent" text="default"/></span></h2>    
			<div style="text-align: center" class="row">
				<div style="margin: 0 auto" class="col-md-3 col-sm-6 col-xs-12 aos-init" data-aos="fade-up" data-aos-duration="1200">
					<div class="services">
						<img src="images/service_img7.png" alt="">
						<h3><spring:message code="guaranteedservices" text="default"/></h3>
						<p><spring:message code="deliverpromise" text="default"/></p>
					</div>
				</div>
				<div style="margin: 0 auto" class="col-md-3 col-sm-6 col-xs-12 aos-init" data-aos="fade-up" data-aos-duration="1500">
					<div class="services">
						<img src="images/service_img8.png" alt="">
						<h3><spring:message code="experience" text="default"/></h3>
						<p><spring:message code="tenyearsexperience" text="default"/></p>
					</div>
				</div>
			</div>
		</div>
	</section>
	<section class="about_area quote_area aos-init" data-aos="fade-up" data-aos-duration="600">
		<h2><spring:message code="calltoday" text="default"/></h2>
		<p><spring:message code="flatratepricing" text="default"/></p>
		<button type="button" class="" data-toggle="modal" data-target="#modalCenter">
			<spring:message code="getafreequote" text="default"/>
		</button>
	</section>

	<!-- Modal -->
	<div id="myModal" class="modal appointment_modal fade" role="dialog">
		<div class="modal-dialog">
			<!-- Modal content-->
			<div class="modal-content">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<div class="modal-body">
					<div class="col-xs-12">
						<div class="col-md-9 col-sm-12 col-xs-12 pull-left calendar_left">
							<div class="">
								<div id='calendar'></div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

<script src="js/aos.js"></script>
<script>
	AOS.init({
		easing : 'ease-in-out-sine'
	});
</script>

<div class="modal fade request_form" id="exampleModalCenter" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="exampleModalLongTitle"><spring:message code="requestquote" text="default"/></h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">
				<p><spring:message code="nameaddress" text="default"/></p>
				<form action="/action_page.php">
				  <div class="form-group">
				    <input type="text" class="form-control" placeholder="<spring:message code="name" text="default"/>" value="" id="name">
				  </div>
				  <div class="form-group">
				    <input type="email" class="form-control" placeholder="<spring:message code="email" text="default"/>" value="" id="email">
				  </div>
				  <div class="form-group">
				    <input type="text" class="form-control" placeholder="<spring:message code="phone" text="default"/>" value="" id="phone">
				  </div>
				  <div class="form-group">
				    <input type="text" class="form-control" placeholder="<spring:message code="zip" text="default"/>" value="" id="zip">
				  </div>
				  <div class="form-group">
				    <textarea class="form-control" placeholder="<spring:message code="lookingfor" text="default"/>" id="message"></textarea>
				  </div>
					<button type="submit" class="btn btn-default send_btn"><spring:message code="submitrequest" text="default"/></button>
				</form>
			</div>
		</div>
	</div>
</div>

<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.21.0/moment.min.js" type="text/javascript"></script>
<script src="js/bootstrap-datetimepicker.min.js"></script>
<script type="text/javascript">
	$(function() {
		$('#datetimepicker1').datetimepicker();
	});
</script>
<script type="text/javascript">
	var _gaq = _gaq || [];
	_gaq.push([ '_setAccount', 'UA-36251023-1' ]);
	_gaq.push([ '_setDomainName', 'jqueryscript.net' ]);
	_gaq.push([ '_trackPageview' ]);

	(function() {
		var ga = document.createElement('script');
		ga.type = 'text/javascript';
		ga.async = true;
		ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
		var s = document.getElementsByTagName('script')[0];
		s.parentNode.insertBefore(ga, s);
	})();
</script>

<script>
	//ResCarouselCustom();
	var pageRefresh = true;

	function ResCarouselCustom() {
		var items = $("#dItems").val(), slide = $("#dSlide").val(), speed = $("#dSpeed").val(), interval = $("#dInterval").val();
		var itemsD = "data-items=\"" + items + "\"", slideD = "data-slide=\""
				+ slide + "\"", speedD = "data-speed=\"" + speed + "\"", intervalD = "data-interval=\""
				+ interval + "\"";

		var atts = "";
		atts += items != "" ? itemsD + " " : "";
		atts += slide != "" ? slideD + " " : "";
		atts += speed != "" ? speedD + " " : "";
		atts += interval != "" ? intervalD + " " : "";

		//console.log(atts);

		var dat = "";
		dat += '<h4 >' + atts + '</h4>';
		dat += '<div class=\"resCarousel\" ' + atts + '>';
		dat += '<div class="resCarousel-inner">';
		for (var i = 1; i <= 14; i++) {
			dat += '<div class=\"item\"><div><h1>' + i + '</h1></div></div>';
		}
		dat += '</div>';
		dat += '<button class=\'btn btn-default leftRs\'><i class=\"fa fa-fw fa-angle-left\"></i></button>';
		dat += '<button class=\'btn btn-default rightRs\'><i class=\"fa fa-fw fa-angle-right\"></i></button>;    </div>'
		console.log(dat);
		$("#customRes").html(null).append(dat);

		if (!pageRefresh) {
			ResCarouselSize();
		} else {
			pageRefresh = false;
		}
		//ResCarouselSlide();
	}

	$("#eventLoad").on('ResCarouselLoad', function() {
		//console.log("triggered");
		var dat = "";
		var lenghtI = $(this).find(".item").length;
		if (lenghtI <= 30) {
			for (var i = lenghtI; i <= lenghtI + 10; i++) {
				dat += '<div class="item"><div class="tile"><div><h1>'
					+ (i + 1)
					+ '</h1></div><h3>Title</h3><p>content</p></div></div>';
			}
			$(this).append(dat);
		}
	});
</script>

<script src="js/resCarousel.js"></script>
<script type="text/javascript" src="js/bootstrap-datetimepicker.js" charset="UTF-8"></script>
<script type="text/javascript">
	$('.form_datetime').datetimepicker({
		//language:  'fr',
		weekStart : 1,
		todayBtn : 1,
		autoclose : 1,
		todayHighlight : 1,
		startView : 2,
		forceParse : 0,
		showMeridian : 1
	});
	$('.form_date').datetimepicker({
		language : 'fr',
		weekStart : 1,
		todayBtn : 1,
		autoclose : 1,
		todayHighlight : 1,
		startView : 2,
		minView : 2,
		forceParse : 0
	});
	$('.form_time').datetimepicker({
		language : 'fr',
		weekStart : 1,
		todayBtn : 1,
		autoclose : 1,
		todayHighlight : 1,
		startView : 1,
		minView : 0,
		maxView : 1,
		forceParse : 0
	});
</script>