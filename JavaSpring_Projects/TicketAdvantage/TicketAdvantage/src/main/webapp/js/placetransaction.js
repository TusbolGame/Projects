var placeTransactionType = '';

// Record the spread event
$.fn.placeTransaction = function(eventObject, linetype) {
	log(eventObject);
	placeTransactionType = linetype;
	log('placeTransactionType1: ' + placeTransactionType);
	$.fn.serviceCall('POST', eventObject, 'restapi/placetransaction', 300000, function(data) {
		log(data);
		if (typeof data != 'undefined' && typeof data != null) {
			retryCount = 0;
			transactionid = data.id;
			log('transactionid: ' + transactionid);
			$.get('transactions.html', function(data) {
				$('#app-container').html(data);
				log('placeTransactionType2: ' + placeTransactionType);
				if (placeTransactionType === 'spread') {
					$.fn.getTransactionInfo('restapi/committedevents/spreadevent/' + transactionid);
					retryFunction = setInterval(function() {
						$.fn.getTransactionInfo('restapi/committedevents/spreadevent/' + transactionid);
					}, 5000);
				}

				if (placeTransactionType === 'total') {
					$.fn.getTransactionInfo('restapi/committedevents/totalevent/' + transactionid);
					retryFunction = setInterval(function() {
						$.fn.getTransactionInfo('restapi/committedevents/totalevent/' + transactionid);
					}, 5000);
				}

				if (placeTransactionType === 'ml') {
					$.fn.getTransactionInfo('restapi/committedevents/mlevent/' + transactionid);
					retryFunction = setInterval(function() {
						$.fn.getTransactionInfo('restapi/committedevents/mlevent/' + transactionid);
					}, 5000);
				}
			});
		}        	
	});
}

//Record the spread event
$.fn.previewTransaction = function(previewObject) {
	log(previewObject);

	// Preview
	$.get("betpreviewlines.html", function(data) {
		$("#app-container").html(data);

		// Now go get the data
		$.fn.serviceCall('POST', previewObject, 'restapi/previewtransaction', 300000, function(data) {
			log(data);
			if (typeof data != 'undefined' && typeof data != null) {
				var htmldata = [];
				for (var x=0; x < data.length; x++) {
					$('#bet-rotationnumber').html(data[x].rotationid);
					htmldata.push('<tr id="tr-' + data[x].accountid + '" class="previewlines_data_header">');
					htmldata.push('  <input id="linetype-' + data[x].accountid + '" value="' + data[x].linetype + '" type="hidden" />');
					htmldata.push('  <input id="rot-' + data[x].accountid + '" value="' + data[x].rotationid + '" type="hidden" />');
					htmldata.push('  <input id="sport-' + data[x].accountid + '" value="' + data[x].sporttype + '" type="hidden" />');
					htmldata.push('  <td class="previewlines_data_header_play">');
					htmldata.push('    <input id="' + data[x].accountid + '" value="0" type="checkbox" />');
					htmldata.push('  </td>');
					htmldata.push('  <td class="previewlines_data_header_amount">');					
					htmldata.push('    $ <input id="amount-' + data[x].accountid + '" type="text" size="5" value="' + data[x].amount + '"/>');
					htmldata.push('  </td>');
					htmldata.push('  <td class="previewlines_data_header_line">');
					htmldata.push('    <select id="li-' + data[x].accountid + '" name="li-' + data[x].accountid + '">');
					if (data[x].linetype === 'total' || data[x].linetype === 'teamtotal') {
						if (data[x].lineindicator === 'o') {
							htmldata.push('      <option selected="selected" value="o">o</option>');
							htmldata.push('      <option value="u">u</option>');
						} else {
							htmldata.push('      <option value="o">o</option>');
							htmldata.push('      <option selected="selected" value="u">u</option>');						
						}						
					} else {
						if (data[x].lineindicator === '+') {
							htmldata.push('      <option selected="selected" value="+">+</option>');
							htmldata.push('      <option value="-">-</option>');
						} else {
							htmldata.push('      <option value="+">+</option>');
							htmldata.push('      <option selected="selected" value="-">-</option>');						
						}
					}
					htmldata.push('    </select>'); 
					htmldata.push('    <input id="l-' + data[x].accountid + '" type="text" name="l-' + data[x].accountid + '" size="3" value="' + data[x].line + '"/>'); 
					htmldata.push('  </td>');
					htmldata.push('  <td class="previewlines_data_header_juice">');
					htmldata.push('    <select id="ji-' + data[x].accountid + '" name="ji-' + data[x].accountid + '">');
					if (data[x].juiceindicator === '+') {
						htmldata.push('      <option selected="selected" value="+">+</option>');
						htmldata.push('      <option value="-">-</option>');
					} else {
						htmldata.push('      <option value="+">+</option>');
						htmldata.push('      <option selected="selected" value="-">-</option>');						
					}
					htmldata.push('    </select>'); 
					htmldata.push('    <input id="j-' + data[x].accountid + '" type="text" name="j-' + data[x].accountid + '" size="3" value="' + data[x].juice + '"/>');
					htmldata.push('  </td>');
					htmldata.push('  <td class="previewlines_data_header_accountname">' + data[x].accountname + '</td>');
					htmldata.push('</tr>');
				}

				$('#previewlines-data').html(htmldata.join('\n'));
				htmldata = [];
			}
		});
		
		$.fn.setupBetpreviewButton();
	});
}