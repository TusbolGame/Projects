// Load the events
$.fn.eventGameLoad = function(id) {
	$.get("events.html", function(data) {
		$("#app-container").html(data);
		log('eventType id: ' + id);
		$.fn.loadEvents(id);
	});	
}

// Get user info
$.fn.getGameUserInfo = function(uId, eventTypeId) {
	if (typeof uId != 'undefined' && uId != null) {
		$.fn.serviceCall('GET', '', 'restapi/user/id/' + uId, 15000, function(data) {
			user = data;
			$.fn.setNavValues('games', eventTypeId, '', '', '', '', '', '', '', '');
			$.fn.changeUrl();
			$.fn.eventGameLoad(eventTypeId);
		});
	}
}

// Load the games
$.fn.loadGames = function() {
	// NFL Lines
	$('#nfl ul li a').on('click', function() {
		var nflid = $(this).attr('id');
		$.fn.getGameUserInfo(uId, nflid);
    });

    // NCAAF Lines
	$('#ncaaf ul li a').on('click', function() {
		var ncaafid = $(this).attr('id');
		$.fn.getGameUserInfo(uId, ncaafid);
    });

    // NCAAF Lines
	$('#nba ul li a').on('click', function() {
		var nba = $(this).attr('id');
		$.fn.getGameUserInfo(uId, nba);
    });

    // NCAAF Lines
	$('#ncaab ul li a').on('click', function() {
		var ncaabid = $(this).attr('id');
		$.fn.getGameUserInfo(uId, ncaabid);
    });

	// WNBA Lines
	$('#wnba ul li a').on('click', function() {
		var wnbaid = $(this).attr('id');
		$.fn.getGameUserInfo(uId, wnbaid);
    });

    // MLB Lines
	$('#mlb ul li a').on('click', function() {
		var mlbaid = $(this).attr('id');
		$.fn.getGameUserInfo(uId, mlbaid);
    });
}