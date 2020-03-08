	// Get TA Home page
	$.fn.getTaHome = function() {
		clearInterval(retryFunction);
		$.fn.setNavValues('', '', '', '', '', '', '', '', '');
		$.fn.changeUrl();
		$.get("tahome.html", function(data) {
			$("#app-container").html(data);
			$.fn.loadHome();
		});		
	}
	
	// Get Figures page
	$.fn.getFigures = function() {
		clearInterval(retryFunction);
		$.fn.setNavValues('figures', '', '', '', '', '', '', '', '');
		$.fn.changeUrl();
		$.get("figures.html", function(data) {
			$("#app-container").html(data);
			$.fn.loadFigures();
		});		
	}

	// Get the reports
	$.fn.getReports = function() {
		clearInterval(retryFunction);
		$.fn.setNavValues('reports', '', '', '', '', '', '', '', '');
		$.fn.changeUrl();
		$.get("reports.html", function(data) {
			$("#app-container").html(data);
			$.fn.loadReportButtons();
		});
	}

	// Get the preview
	$.fn.getBetPreview = function() {
		clearInterval(retryFunction);
		$.fn.setNavValues('betpreview', '', '', '', '', '', '', '', '');
		$.fn.changeUrl();
		$.get("betpreview.html", function(data) {
			$("#app-container").html(data);
			$.fn.loadBetPreview();
		});
	}

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

	// Get the web accounts
	$.fn.getAccounts = function() {
		clearInterval(retryFunction);
		$.fn.setNavValues('accounts', '', '', '', '', '', '', '', '');
		$.fn.changeUrl();
		$.get("accounts.html", function(data) {
	    	$("#app-container").html(data);
	    	$.fn.loadAccounts();
	    });
	}

	// Get the email accounts
	$.fn.getEmailAccounts = function() {
		clearInterval(retryFunction);
		$.fn.setNavValues('emailaccounts', '', '', '', '', '', '', '', '');
		$.fn.changeUrl();
		$.get("emailaccounts.html", function(data) {
	    	$("#app-container").html(data);
	    	$.fn.loadEmailAccounts();
	    });
	}

	// Get the email accounts
	$.fn.getTwitterAccounts = function() {
		clearInterval(retryFunction);
		$.fn.setNavValues('twitteraccounts', '', '', '', '', '', '', '', '');
		$.fn.changeUrl();
		$.get("twitteraccounts.html", function(data) {
		    	$("#app-container").html(data);
		    	$.fn.loadTwitterAccounts();
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
				$.fn.serviceCall('GET', '', 'restapi/search?query=' + searchText, 45000, function(data) {
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

	// Get the web scrappers
	$.fn.getWebScrapper = function() {
		clearInterval(retryFunction);
		$.get('scrapper.html', function(data) {
			$('#app-container').html(data);
			$.fn.setNavValues('webscrapper', '', '', '', '', '', '', '', '');
			$.fn.changeUrl();
			$.fn.loadWebScrapperData();
		});
	}

	// Get the email scrappers
	$.fn.getEmailScrapper = function() {
		clearInterval(retryFunction);
		$.get('emailscrapper.html', function(data) {
			$('#app-container').html(data);
			$.fn.setNavValues('emailscrapper', '', '', '', '', '', '', '', '');
			$.fn.changeUrl();
			$.fn.loadEmailScrapperData();
		});
	}

	// Get the email scrappers
	$.fn.getTwitterScrapper = function() {
		clearInterval(retryFunction);
		$.get('twitterscrapper.html', function(data) {
			$('#app-container').html(data);
			$.fn.setNavValues('twitterscrapper', '', '', '', '', '', '', '', '');
			$.fn.changeUrl();
			$.fn.loadTwitterScrapperData();
		});
	}

$(document).ready(function() {
	// Games clicked
	$('#button-reports').on('click', function() {
		$.fn.getReports();
	});

	// Preview clicked
	$('#button-betpreview-line').on('click', function() {
		$.fn.getBetPreview();
	});

	// Games clicked
	$('#button-games').on('click', function() {
		$.fn.getGames();
	});

	// Accounts clicked
	$('#button-accounts').on('click', function() {
		$.fn.getAccounts();
	});

	// Email accounts clicked
	$('#button-emailaccounts').on('click', function() {
		$.fn.getEmailAccounts();
	});

	// Twitter accounts clicked
	$('#button-twitteraccounts').on('click', function() {
		$.fn.getTwitterAccounts();
	});

	// Figures clicked
	$('#button-figures').on('click', function() {
		$.fn.getFigures();
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

	// Web Scrapper clicked
	$('#button-webscrapper').on('click', function() {
		$.fn.getWebScrapper();
	});

	// Email Scrapper clicked
	$('#button-emailscrapper').on('click', function() {
		$.fn.getEmailScrapper();
	});

	// Twitter Scrapper clicked
	$('#button-twitterscrapper').on('click', function() {
		$.fn.getTwitterScrapper();
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