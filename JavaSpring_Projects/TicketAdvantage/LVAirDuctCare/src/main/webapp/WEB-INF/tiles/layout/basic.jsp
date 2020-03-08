<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib  uri="http://www.springframework.org/tags" prefix="spring" %>
<!DOCTYPE html>
<html lang="en">
	<head>
	    <meta charset="utf-8"/>
	    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
	    <meta name="viewport" content="width=device-width, initial-scale=1"/>
	    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
	    <meta name="description" content="<tiles:getAsString name="meta"/>"/>
	    <title><tiles:getAsString name="pageTitle"/></title>
	    <link rel="icon" type="image/png" sizes="16x16" href="images/fav_icon.png?v=4" />
	    <link rel="shortcut icon" type="image/x-icon" href="images/fav_icon.png?v=4" />		
	    <link rel="stylesheet" type="text/css" href="css/kendo.common-material.min.css" />		
		<link rel="stylesheet" type="text/css" href="css/kendo.material.min.css" />
		<link rel="stylesheet" type="text/css" href="css/kendo.material.mobile.min.css" />
	    <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css" />
	    <link rel="stylesheet" type="text/css" href="css/bootstrap-formhelpers.min.css" />
	    <link rel="stylesheet" type="text/css" href="css/bootstrap-datetimepicker.min.css" />    
	    <link rel="stylesheet" type="text/css" href="css/bootstrap-datetimepicker.min.css" media="screen" />
	    <link rel="stylesheet" type="text/css" href="css/font-awesome.css"/>
	    <link rel="stylesheet" type="text/css" href="css/style.css" />
	    <link rel="stylesheet" type="text/css" href="css/aos.css" />
	    <script type="text/javascript" src="js/jquery.min.js"></script>
	    <script type="text/javascript" src="js/kendo.all.min.js"></script>
	    <script type="text/javascript" src="js/moment.min.js"></script>
	    <script type="text/javascript" src="js/bootstrap.min.js"></script>
	    <script type="text/javascript" src="js/bootstrap-formhelpers.min.js"></script>
	    <script type="text/javascript" src="js/bootstrap-datetimepicker.min.js"></script>
	    <link rel="stylesheet" type="text/css" href="css/resCarousel.css" />
		<link rel="stylesheet" type="text/css" href="css/fullcalendar.min.css" />
		<link rel="stylesheet" type="text/css" href="css/fullcalendar.print.min.css" media='print' />
		<script type="text/javascript" src="js/fullcalendar.min.js"></script>
		<script type="text/javascript" src="js/common.js"></script>
		<script>
		  	$.fn.pleaseEnterAllInfo = function() {
		  		return '<spring:message code="pleaseenterallinfo" text="default"/>';
		  	}
	
		  	$.fn.informationReceived = function() {
		  		return '<spring:message code="informationreceived" text="default"/>';
		  	}
	
		  	$.fn.emailError = function() {
		  		return '<spring:message code="emailerror" text="default"/>';
		  	}
		</script>
	</head>
	<body class="home" data-aos-easing="ease-in-out-sine" data-aos-duration="400" data-aos-delay="0">
		<!-- Header -->
		<tiles:insertAttribute name="header" />
		<!-- Body -->
		<tiles:insertAttribute name="body" />
		<!-- Footer -->
		<tiles:insertAttribute name="footer" />

		<script src="js/aos.js"></script>
		<script>
	      AOS.init({
	        easing: 'ease-in-out-sine'
	      });
		</script>
		<div class="modal fade request_form" id="modalCenter" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
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
						<form>
							<div class="form-group">
								<input type="text" class="form-control" placeholder="<spring:message code="name" text="default"/>" value="" name="quotename" id="quotename"/>
							</div>
							<div class="form-group">
								<input type="email" class="form-control" placeholder="<spring:message code="email" text="default"/>" value="" name="quoteemail" id="quoteemail"/>
							</div>
							<div class="form-group">
								<input type="text" class="form-control" placeholder="<spring:message code="phone" text="default"/>" value="" name="quotephone" id="quotephone"/>
							</div>
							<div class="form-group">
								<input type="text" class="form-control" placeholder="<spring:message code="zip" text="default"/>" value="" name="quotezipcode" id="quotezipcode"/>
							</div>
							<div class="form-group">
								<select class="form-control" id="quoteservice" name="quoteservice">
									<option><spring:message code="airductcleaningbasic" text="default"/></option>
									<option><spring:message code="dryerventcleaningbasic" text="default"/></option>
									<option><spring:message code="chimneysweepbasic" text="default"/></option>
									<option><spring:message code="furnacecleaningbasic" text="default"/></option>
									<option><spring:message code="ductrepairservicesbasic" text="default"/></option>
								</select>
							</div>
							<div class="form-group">
								<textarea class="form-control" placeholder="<spring:message code="lookingfor" text="default"/>" name="quotemessage" id="quotemessage"></textarea>
							</div>
							<button id="submitrequestdos" type="button" class="btn btn-default send_btn" onclick="javascript:sendEmailDos();" value="Submit"><spring:message code="submitrequest" text="default"/></button>
						</form>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>