<%@page import="com.ticketadvantage.services.util.ServerInfo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>Captcha Text</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<script type='text/javascript' src="js/jquery/2.2.3/jquery.min.js"></script>
	<script type='text/javascript' src="js/common.js"></script>
	<script>
		var id=<%= request.getParameter("id") %>;
		var url='http://<%= ServerInfo.getIp() %>:<%= ServerInfo.getAppPort() %>/ticketadvantage/restapi/captcha/getcaptcha/id/';

		$.fn.serviceCall('GET', '', url + id, 15000, function(data) {
			$('#captchaimage').attr('src', 'data:image/png;base64,' + data.imagedata);
		});

		function sendAndClose() {
	    		var ctext = document.getElementById("captchatext").value;
	    		console.log('ctext: ' + ctext);
	    		var uurl='http://<%= ServerInfo.getIp() %>:<%= ServerInfo.getAppPort() %>/ticketadvantage/restapi/captcha/updatecaptcha/';

			$.fn.serviceCall('PUT', '',  uurl + id + '/' + ctext, 15000, function(data) {
				// do nothing    			
	    		});

	    		window.close();
		}
	</script>
</head>
<body>
	<form id="captchaformid" action="javascript:sendAndClose()">
		<img id="captchaimage" src="" HEIGHT="700" WIDTH="880"/><br/><br/><br/><br/><br/>
<!-- 	<img id="captchaimage" src="data:image/png;base64,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" HEIGHT="220" WIDTH="320"/><br/> -->
		
		<input type="text" style="height:120px; width:800px; font-size:80px;" name="captchatext" id="captchatext" autocomplete="off"><br/>
		<button type="submit" style="height:250px; width:800px; font-size:80px;" ><span style="font-size:80px;">Submit</span></button>
	</form>
</body>
</html>