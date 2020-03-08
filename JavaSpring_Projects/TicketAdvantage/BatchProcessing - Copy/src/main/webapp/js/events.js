	var eventId;
	var eventtype;

	// Load the selected game
	$.fn.loadSelectedGame = function(aid) {
        $.get( "eventselector.html", function(data) {
        	$("#app-container").html(data);
        	$.fn.setupAcctsGroups(aid);
        });
	}

	// Setup the events
	$.fn.setupEvents = function(data) {
		log('EventID: ' + eventId);
		var liString = [];
    	if (typeof data != 'undefined' && typeof data.events != 'undefined' && data.events != null && data.events.length > 0) {
            for (var i=0; i < data.events.length; ++i)
            {
       			if (typeof data.events[i].teamone != 'undefined' && data.events[i].teamone != null &&
       					typeof data.events[i].teamtwo != 'undefined' && data.events[i].teamtwo != null) {
					liString.push('<div id="game-id" class="game_list">');
					liString.push('  <a id="' + data.events[i].teamone.eventid + '" style="overflow: auto; margin: auto;" class="game_anchor" href="javascript:void(0);">');
					liString.push('    <input id="type' + data.events[i].teamone.eventid + '" type="hidden" value="' + data.events[i].eventtype + '">');
					liString.push('    <div style="display: block; height: 100%; overflow: auto; margin: auto;">');
					liString.push('      <div class="game_rotation_div">');
					liString.push('        <div class="game_id1"><div style="padding-left: 5px;">' + data.events[i].teamone.eventid + '</div></div>');
					liString.push('        <div class="game_date_div">' + $.fn.determineDate(new Date(data.events[i].eventdatetime)) + '</div>');
					liString.push('        <div class="game_id2"><div style="padding-right: 5px;">' + data.events[i].teamtwo.eventid + '</div></div>');
					liString.push('      </div>');
					liString.push('      <div class="game_teams_div">');
					liString.push('        <div class="game_team_name1"><div style="padding-left: 5px;">' + data.events[i].teamone.team + '</div></div>');
					liString.push('        <div class="game_team_vs">vs</div>');
					liString.push('        <div class="game_team_name2"><div style="padding-right: 5px;">' + data.events[i].teamtwo.team + '</div></div>');
					liString.push('      </div>');
					liString.push('    </div>');
					liString.push('  </a>');
					liString.push('</div>');
       			}
            }
        	$('#game-package').html(liString.join('\n'));
        	liString.length = 0;
        	$('#game-id a').on('click', function() {
        		// First check that a valid account is setup
        		if (typeof user != 'undefined' && user != null && typeof user.accounts != 'undefined' && user.accounts != null && user.accounts.length > 0) {
    	        	var aid = $(this).attr('id');
    	        	eventtype = $('#type' + aid).attr('value');
    				$.fn.setNavValues('games', eventId, 'event', '', 'id=' + aid, 'eventtype=' + eventId, '', '', '');
    				$.fn.changeUrl();
    				$.fn.loadSelectedGame(aid);
        		} else {
            		$('#background').addClass('background_on');
                    $('#error-body').html('Please setup an account first');
                    $('#error-message').addClass('error_message_on');
        		}
        	});
    	} else {
    		var noGames = '<h1 style="padding: 10px;">There are currently no games available</h1>';
    		$('#game-package').html(noGames);
    	}		
	}

	// Load the events
	$.fn.loadEvents = function(eventType) {
		log('EventType: ' + eventType);
		eventId = eventType;
		eventtype = eventType;
		$.fn.serviceCall('GET', '', 'restapi/events?eventtype=' + eventType, 15000, function(data) {
			$.fn.setupEvents(data);
		});
	}