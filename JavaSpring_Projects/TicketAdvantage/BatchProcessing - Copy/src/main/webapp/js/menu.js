	// Get the games
	$.fn.getGames = function() {
		clearInterval(retryFunction);
		$.fn.setNavValues('games', '', '', '', '', '', '', '', '');
		$.fn.changeUrl();
		$.get("games.html", function(data) {
			$("#app-container").html(data);
			$.fn.loadGames();
		});		
	}

	// Get the accounts
	$.fn.getAccounts = function() {
		clearInterval(retryFunction);
		$.fn.setNavValues('accounts', '', '', '', '', '', '', '', '');
		$.fn.changeUrl();
		$.get("accounts.html", function(data) {
	    	$("#app-container").html(data);
	    	$.fn.loadAccounts();
	    });
	}

	// Get the groups
	$.fn.getGroups = function() {
		clearInterval(retryFunction);
		$.fn.setNavValues('groups', '', '', '', '', '', '', '', '');
		$.fn.changeUrl();
		$.get("groups.html", function(data) {
	    	$("#app-container").html(data);
	    	$.fn.loadGroups();
	    });
	}
	
	// Get the manage screen
	$.fn.getManage = function() {
		clearInterval(retryFunction);
		$.fn.setNavValues('manage', '', '', '', '', '', '', '', '');
		$.fn.changeUrl();
		$.get("groupmanagement.html", function(data) {
	    	$("#app-container").html(data);
	    	$.fn.loadGroupManagement();
	    });
	}
	
	// Search for game
	$.fn.searchGame = function() {
		clearInterval(retryFunction);
		// Get the search value
		var searchText = $('#search-text').val();
		if (searchText != '') {
			$.get('events.html', function(data) {
				$('#app-container').html(data);
				$.fn.serviceCall('GET', '', 'restapi/search?query=' + searchText, 15000, function(data) {
					$.fn.setNavValues('search', '', '', '', 'query=' + searchText, '', '', '', '');
					$.fn.changeUrl();
					$.fn.setupEvents(data);
					$('#search-text').val('');
				});
			});
		} else {
			$('#background').addClass('background_on');
			$('#error-body').html('Please enter a valid search string');
        	$('#error-message').addClass('error_message_on');
		}
	}

	// Get the transactions
	$.fn.getTransactions = function() {
		clearInterval(retryFunction);
		$.get('transactions.html', function(data) {
			$('#app-container').html(data);
			$.fn.setNavValues('transactions', '', '', '', '', '', '', '', '');
			$.fn.changeUrl();
			$.fn.getAllTransactions();
		});
	}

	// Get the scrappers
	$.fn.getScrapper = function() {
		clearInterval(retryFunction);
		$.get('scrapper.html', function(data) {
			$('#app-container').html(data);
			$.fn.setNavValues('scrapper', '', '', '', '', '', '', '', '');
			$.fn.changeUrl();
			$.fn.showPending();
		});
	}

$(document).ready(function() {
	// Games clicked
	$('#button-games').on('click', function() {
		$.fn.getGames();
	});

	// Accounts clicked
	$('#button-accounts').on('click', function() {
		$.fn.getAccounts();
	});

	// Groups clicked
	$('#button-groups').on('click', function() {
		$.fn.getGroups();
	});

	// Manage clicked
	$('#button-manage').on('click', function() {
		$.fn.getManage();
	});
	
	// Transactions clicked
	$('#button-transactions').on('click', function() {
		$.fn.getTransactions();
	});

	// Scrapper clicked
	$('#button-scrapper').on('click', function() {
		$.fn.getScrapper();
	});

	// Search clicked
	$('#search-button').on('click', function() {
		$.fn.searchGame();
	});
	
	// Catch enter key; call the search service
	$(document).keydown( function(event) {
		if (event.which === 13) {
			$.fn.searchGame();
		}
	});
});