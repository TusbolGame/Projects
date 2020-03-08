<%@ taglib  uri="http://www.springframework.org/tags" prefix="spring" %>
<section class="banner_area aos-init" data-aos="fade-up" data-aos-duration="600">
	<div class="row">
		<div class="col-md-4 col-sm-4 col-xs-12 banner_bg blue text-right">
			<div class="banner_inner">
				<h1><spring:message code="contactuslower" text="default"/></h1>
			</div>
		</div>
		<div class="col-md-8 col-sm-8 col-xs-12 padding_none text-right">
			<img src="images/contact_banner.jpg" alt="">
		</div>
	</div>
</section>
<section class="service_area lasvegas_area feel_free">
	<div class="container">
		<h2>
			<spring:message code="feelfree" text="default"/><span><spring:message code="contactuslower" text="default"/></span>
		</h2>
		<p><spring:message code="servingmessage" text="default"/></p>
		<div class="row aos-init" data-aos="fade-up" data-aos-duration="600">
			<div class="col-md-6 col-sm-12 col-12 contact_left">
				<div class="col">
					<div class="block_inner">
						<i class="fa fa-map-marker" aria-hidden="true"></i>
						<p>
							<span><spring:message code="addressname" text="default"/></span><spring:message code="address" text="default"/>
						</p>
					</div>
					<div class="block_inner">
						<i class="fa fa-phone" aria-hidden="true"></i>
						<p>
							<span><spring:message code="phonenumbername" text="default"/></span> 702-331-9043
						</p>
					</div>
					<div class="block_inner">
						<i class="fa fa-envelope" aria-hidden="true"></i>
						<p>
							<span><spring:message code="emailid" text="default"/></span> contact@lvairductcare.com
						</p>
					</div>
					<div class="block_inner">
						<i class="fa fa-clock-o" aria-hidden="true"></i>
						<p>
							<span><spring:message code="hoursofoperation" text="default"/></span> Mon - Fri: 8:00am - 6:00pm<br>
							Sat: 10:00am - 5:00pm<br>
						</p>
					</div>
				</div>
			</div>
			<div class="col-md-6 col-sm-12 col-xs-12 contact_right">
				<div>
					<div class="form-group">
						<input type="text" class="form-control" placeholder="<spring:message code="name" text="default"/>" value="" name="name" id="name" />
					</div>
					<div class="form-group">
						<input type="email" class="form-control"  placeholder="<spring:message code="email" text="default"/>" value="" name="email" id="email" />
					</div>
					<div class="form-group">
						<input type="text" class="form-control" placeholder="<spring:message code="phone" text="default"/>" value="" name="phone" id="phone" />
					</div>
					<div class="form-group">
						<input type="text" class="form-control" placeholder="<spring:message code="zip" text="default"/>" value="" name="zipcode" id="zipcode" />
					</div>
					<div class="form-group">
						<select class="form-control" id="service" name="service">
							<option><spring:message code="airductcleaningbasic" text="default"/></option>
							<option><spring:message code="dryerventcleaningbasic" text="default"/></option>
							<option><spring:message code="chimneysweepbasic" text="default"/></option>
							<option><spring:message code="furnacecleaningbasic" text="default"/></option>
							<option><spring:message code="ductrepairservicesbasic" text="default"/></option>
						</select>
					</div>
					<div class="form-group">
						<textarea class="form-control" placeholder="<spring:message code="commentmessage" text="default"/>" name="message" id="message"></textarea>
					</div>
					<button id="submitrequest" type="button" class="btn btn-default send_btn" onclick="javascript:sendEmail();" value="Submit"><spring:message code="submitrequest" text="default"/></button>
				</div>
			</div>
		</div>
		<p>
			<img src="images/map_img.png" alt="">
		</p>
	</div>
</section>
<section class="about_area quote_area aos-init" data-aos="fade-up" data-aos-duration="600">
	<h2><spring:message code="calltoday" text="default"/></h2>
	<p><spring:message code="flatratepricing" text="default"/></p>
	<button type="button" class="" data-toggle="modal" data-target="#modalCenter"><spring:message code="getafreequote" text="default"/></button>
</section>