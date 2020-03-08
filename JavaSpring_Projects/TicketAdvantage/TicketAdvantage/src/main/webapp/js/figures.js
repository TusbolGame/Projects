/**
 * 
 */
// Load the games
$.fn.loadFigures = function() {
	$.fn.buildFiguresTable();
	$( "#week_select" ).change(function() {
		var today = new Date();
		var tz = '';
		var timezn = today.getTimezoneOffset()/60;
		if (timezn == 4) {
			tz = 'ET';
		} else if (timezn === 5) {
			tz = 'CT';
		} else if (timezn === 6) {
			tz = 'MT';
		} else if (timezn === 7) {
			tz = 'PT';
		} else {
			tz = 'ET';
		}

		$.fn.populateTableData(this.value, tz);
	});
	
	$('#figures-export').on('click', function() {	
		var token = Math.random().toString(36).substr(2) + Math.random().toString(36).substr(2);
		document.cookie = 'CSRF-TOKEN=' + token + '; path=/';
		var xhr = new XMLHttpRequest();
		xhr.open('GET', 'restapi/account/userid/' + uId + '/weeklyfigures?start_date=' + $( "#week_select" ).val(), true);
		xhr.setRequestHeader('X-CSRF-TOKEN', token);
		xhr.setRequestHeader('Authorization', basicAuth);
		xhr.setRequestHeader('Content-Type', 'application/json');

		xhr.onload = function(e) {
		  if (this.status == 200) {
			  $.fn.convertToCsv(this.response, 'FiguresReport', true);
		  }
		};
		xhr.send();
	});
}

$.fn.convertToCsv = function (JSONData, ReportTitle, ShowLabel) {
    //If JSONData is not an object then JSON.parse will parse the JSON string in an Object
    var arrData = typeof JSONData != 'object' ? JSON.parse(JSONData) : JSONData;
    console.log(arrData);
    var CSV = '';    
    //Set Report title in first row or line
    
    CSV += ReportTitle + '\r\n\n';

    //This condition will generate the Label/Header
    if (ShowLabel) {
        var row = "";
        
        //This loop will extract the label from 1st index of on array
        for (var index in arrData[0]) {
            
            //Now convert each value to string and comma-seprated
            row += index + ',';
        }

        row = row.slice(0, -1);
        
        //append Label row with line break
        CSV += row + '\r\n';
    }
    
    //1st loop is to extract each row
    for (var i = 0; i < arrData.length; i++) {
        var row = "";
        
        //2nd loop will extract each column and convert it in string comma-seprated
        for (var index in arrData[i]) {
            row += '"' + arrData[i][index] + '",';
        }

        row.slice(0, row.length - 1);
        
        //add a line break after each row
        CSV += row + '\r\n';
    }

    if (CSV == '') {        
        alert("Invalid data");
        return;
    }   
    console.log(CSV);
    
    //Generate a file name
    var fileName = "Report_";
    //this will remove the blank-spaces from the title and replace it with an underscore
    fileName += ReportTitle.replace(/ /g,"_");   
    
    //Initialize file format you want csv or xls
    var uri = 'data:text/csv;charset=utf-8,' + escape(CSV);
    
    // Now the little tricky part.
    // you can use either>> window.open(uri);
    // but this will not work in some browsers
    // or you will not get the correct file extension    
    
    //this trick will generate a temp <a /> tag
    var link = document.createElement("a");    
    link.href = uri;
    
    //set the visibility hidden so it will not effect on your web-layout
    link.style = "visibility:hidden";
    link.download = fileName + ".csv";
    
    //this part will append the anchor tag and remove it after automatic click
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}

$.fn.buildFiguresTable = function() {
	var day = new Date().getDay();
	var days_to_subtract = 6;
	if (day >= 1) {
		var days_to_subtract = (day - 1);
		
	}
	var start_date 			= moment().subtract(days_to_subtract, 'days').startOf('day');
	var week_date 			= start_date;
	var start_date_str 		= start_date.format('YYYY-MM-DD');
	var start_date_short 	= start_date.format('MMM D');
	var end_date 			= start_date.add(7, 'days');
	var end_date_str		= end_date.format('YYYY-MM-DD');
	var end_date_short 		= end_date.format('MMM D');
	var week_date_range 	= start_date_short + " - " + end_date_short;
	var html_text 			= "Current Week";
	
	for(var i = 0; i < 12; i++)
	{	
	  if (i == 1) {
		  html_text = "Previous Week";
	  } else if(i > 1) {
		  html_text = i + " Weeks Ago";
	  }

	  var weekOption= "<option value=" + week_date.format('YYYY-MM-DD') + ">" + html_text + "</option>";
	  $("#week_selector select").append(weekOption) ;//.append(weekOption);​
	  week_date = week_date.subtract(7, 'days');
	}

	var today = new Date();
	var tz = '';
	var timezn = today.getTimezoneOffset()/60;
	if (timezn == 4) {
		tz = 'ET';
	} else if (timezn === 5) {
		tz = 'CT';
	} else if (timezn === 6) {
		tz = 'MT';
	} else if (timezn === 7) {
		tz = 'PT';
	} else {
		tz = 'ET';
	}

	$.fn.populateTableData(start_date_str, tz);
	$.fn.populateWeekRange(week_date_range);
}

$.fn.populateTableData = function(start_date_str, timezone) {
	$.fn.serviceCall('GET', '', 'restapi/account/userid/' + uId + '/weeklyfigures?start_date=' + start_date_str + '&timezone=' + timezone, 45000, function(data) {
		console.log(data);
		console.log(data.toString());
		$('.table').html('');
		$('.table').footable({
			"columns": [
				{ "name": "accountId", "title": "ID", "breakpoints": "xs sm"},
				{ "name": "accountName", "title": "Name" },
				{ "name": "accountPassword", "title": "PW" },
				{ "name": "mon", "title": "Mon", "breakpoints": "xs sm" },
				{ "name": "tue", "title": "Tue", "breakpoints": "xs sm md" },
				{ "name": "wed", "title": "Wed", "breakpoints": "xs sm md" },
				{ "name": "thu", "title": "Thu", "breakpoints": "xs sm md" },
				{ "name": "fri", "title": "Fri", "breakpoints": "xs sm md" },
				{ "name": "sat", "title": "Sat", "breakpoints": "xs sm md" },
				{ "name": "sun", "title": "Sun", "breakpoints": "xs sm md" },
				{ "name": "week", "title": "Week", "breakpoints": "xs sm md" },
				{ "name": "pending", "title": "Pending", "breakpoints": "xs sm md" },
				{ "name": "balance", "title": "Balance", "breakpoints": "xs sm md" }
			],
			"rows": data
		});
		$.fn.styleTable();
		var start_date 				= moment(start_date_str);
		var start_date_short		= start_date.format('MMM D');
		var end_date				= start_date.add(7, 'days');
		var end_date_short			= end_date.format('MMM D');
		
		var week_date_range 	= start_date_short + " - " + end_date_short;
		$.fn.populateWeekRange(week_date_range);
	});
}

$.fn.styleTable = function() {
    $('.figures-table tr').each(function(){
    	$cells = $(this).find("td");
    	index = 0;
    	$cells.each(function(){
    		if (index == 0) {
    			$(this).addClass("account");
    			index++;
    		} else {
    			var cell_content = $(this).html();
    			if ($.isNumeric(cell_content)) {
    				if (parseInt(cell_content) < 0) {
    					$(this).addClass("negative_number");
    				}
    				if (parseInt(cell_content) > 0) {
    					$(this).addClass("positive_number");
    				}
    			}
    		}
    		
    	});
    });
}

$.fn.populateWeekRange = function(week_range_text) {
	$('#week_dates').text(week_range_text);
}