<%@ taglib  uri="http://www.springframework.org/tags" prefix="spring" %>
<footer class="contact_area">
	<div class="container">
		<h2><spring:message code="contactuslower" text="default"/></h2>
		<div class="row">
			<div class="col-md-6 col-sm-6 col-xs-12 aos-init" data-aos="fade-right" data-aos-duration="600">
				<div class="footer_about">
					<h3><spring:message code="aboutus" text="default"/></h3>
        				<p><spring:message code="dedicated" text="default"/></p>
				</div>
        		</div>
			<div class="col-md-6 col-sm-6 col-xs-12 aos-init" data-aos="fade-left" data-aos-duration="600">
				<div class="col">
					<div class="block_inner">
						<i class="fa fa-map-marker" aria-hidden="true"></i>
						<p><span><spring:message code="office" text="default"/></span><spring:message code="address" text="default"/></p>
					</div>
					<div class="block_inner">
						<i class="fa fa-phone" aria-hidden="true"></i>
						<p><span><spring:message code="phonenumbername" text="default"/></span> 702-331-9043</p>
					</div>
					<div class="block_inner">
						<i class="fa fa-envelope" aria-hidden="true"></i>
						<p><span><spring:message code="emailid" text="default"/></span> contact@lvairductcare.com</p>
					</div>
				</div>
			</div>
		</div>
	</div>
    <div class="social_network">
		<a href="https://twitter.com/lvairductcare" target="_blank"><i class="fa fa-twitter" aria-hidden="true"></i></a>
		<a href="https://www.facebook.com/John-Miller-861896690864360/?modal=admin_todo_tour" target="_blank"><i class="fa fa-facebook" aria-hidden="true"></i></a> 
		<a href="https://www.linkedin.com/company/lv-air-duct-care/" target="_blank"><i class="fa fa-linkedin" aria-hidden="true"></i></a>
		<a href="https://www.instagram.com/lvairductcare/" target="_blank"><i class="fa fa-instagram" aria-hidden="true"></i></a>
		<a href="http://www.youtube.com/" target="_blank"><i class="fa fa-youtube" aria-hidden="true"></i></a>
        <div class="copyright"><spring:message code="copyright" text="default"/></div> 
	</div>
</footer>