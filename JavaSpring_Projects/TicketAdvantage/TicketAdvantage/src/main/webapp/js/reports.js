$.fn.loadReportButtons = function() {
	$('#ncaabreport-export').on('click', function() {			
		var token = Math.random().toString(36).substr(2) + Math.random().toString(36).substr(2);
		document.cookie = 'CSRF-TOKEN=' + token + '; path=/';
		var xhr = new XMLHttpRequest();
		xhr.open('GET', 'restapi/ncaabreport', true);
		xhr.setRequestHeader('X-CSRF-TOKEN', token);
		xhr.setRequestHeader('Authorization', basicAuth);
		xhr.setRequestHeader('Content-Type', 'application/json');

		// You should set responseType as blob for binary responses
		xhr.responseType = 'blob';

		xhr.onload = function(e) {
		  if (this.status == 200) {
		    // get binary data as a response
		    var blob = this.response;
	        // Trick for making download-able link
	        a = document.createElement('a');
	        a.href = window.URL.createObjectURL(this.response);
	        // Give filename you wish to download
	        a.download = "HoopsData.xls";
	        a.style.display = 'none';
	        document.body.appendChild(a);
	        a.click();
		  }
		};			 
		xhr.send();
	});
	
	$('#ncaab-transaction').on('click', function() {
		$.fn.serviceCall('GET', '', 'restapi/playncaablastthree', 90000, function(data) {
			log(data)
		});
	});
}