// Load the Home page
$.fn.loadHome = function() {
	console.log("We are loading the home page...");
	$("#daily-total-label").text("Daily Total (" + moment().format('ddd') + "):");
	$.fn.getWeeklyCount();
	$.fn.getWeeklyAmount();
	$.fn.getDailyAmount();
	$.fn.getBalance();
}

$.fn.getWeeklyCount = function() {
	var start_date = "";
	var end_date   = "";
	var day = new Date().getDay();
	var days_to_subtract = 6;
	if (day >= 1) {
		var days_to_subtract = (day - 1);
		
	}
	start_date 		= moment().subtract(days_to_subtract, 'days').startOf('day');
	start_date_str 	= start_date.format('YYYY-MM-DD');
	end_date 		= start_date.add(7, 'days');
	end_date_str 	= end_date.format('YYYY-MM-DD');

	$.fn.serviceCall('GET', '', 'restapi/accountevent/userid/' + uId + '/count?start_date=' + start_date_str + '&end_date=' + end_date_str, 45000, function(data) {
		if (Number(data) < 0) {
			$( "#weekly-count" ).addClass("negative-value");
		} else {
			$( "#weekly-count" ).addClass("positive-value");
		}
		$( "#weekly-count" ).text(data);
	});
}

$.fn.getWeeklyAmount = function() {
	var start_date = "";
	var end_date   = "";
	var day = new Date().getDay();
	var days_to_subtract = 6;
	if (day >= 1) {
		var days_to_subtract = (day - 1);
		
	}
	start_date 		= moment().subtract(days_to_subtract, 'days').startOf('day');
	start_date_str 	= start_date.format('YYYY-MM-DD');
	end_date 		= start_date.add(7, 'days');
	end_date_str 	= end_date.format('YYYY-MM-DD');

	$.fn.serviceCall('GET', '', 'restapi/accountevent/userid/' + uId + '/amount?start_date=' + start_date_str + '&end_date=' + end_date_str, 45000, function(data) {
		if (Number(data) < 0) {
			$( "#weekly-total" ).addClass("negative-value");
		} else {
			$( "#weekly-total" ).addClass("positive-value");
		}
		$( "#weekly-total" ).text("$" + data);
	});
}

$.fn.getDailyAmount = function() {
	var start_date = "";
	var end_date   = "";
	var day = new Date().getDay();
	
	start_date 		= moment().startOf('day');
	start_date_str 	= start_date.format('YYYY-MM-DD');
	end_date 		= start_date.add(1, 'days');
	end_date_str 	= end_date.format('YYYY-MM-DD');

	$.fn.serviceCall('GET', '', 'restapi/accountevent/userid/' + uId + '/amount?start_date=' + start_date_str + '&end_date=' + end_date_str, 45000, function(data) {
		$( "#daily-total" ).text("$" + data);
		if (Number(data) < 0) {
			$( "#daily-total" ).addClass("negative-value");
		} else {
			$( "#daily-total" ).addClass("positive-value");
		}
	});
}

$.fn.getBalance = function() {
	var start_date = "";
	var day = new Date().getDay();
	var days_to_subtract = 6;
	if (day >= 1) {
		var days_to_subtract = (day - 1);
		
	}
	start_date 		= moment().subtract(days_to_subtract, 'days').startOf('day');
	start_date_str 	= start_date.format('YYYY-MM-DD');
	
	$.fn.serviceCall('GET', '', 'restapi/userbilling/userid/' + uId + '?week_start_date=' + start_date_str, 45000, function(data) {
		if(data.length > 0) {
			if (Number(data[0].weeklybalance) < 0) {
				$( "#balance" ).addClass("negative-value");
			} else {
				$( "#balance" ).addClass("positive-value");
			}
			$( "#balance" ).text("$" + data[0].weeklybalance);
		}
	});
}
