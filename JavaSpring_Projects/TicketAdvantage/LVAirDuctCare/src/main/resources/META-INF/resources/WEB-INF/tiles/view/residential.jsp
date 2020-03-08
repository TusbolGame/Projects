<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<section class="banner_area aos-init" data-aos="fade-up" data-aos-duration="600">
	<img src="images/airductcleaning.png" alt="">
	<div class="home_banner_inner">
		<h1><spring:message code="title" text="default" /></h1>
		<p><spring:message code="fedup" text="default" /></p>
		<a href="/residential"><spring:message code="viewservice" text="default" /></a>
	</div>
</section>
<section class="about_area">
	<div class="container">
		<h2 class="aos-init" data-aos="fade-down" data-aos-duration="600"><span><spring:message code="welcometo" text="default" /></span><spring:message code="title" text="default" /></h2>
		<p class="aos-init" data-aos="fade-down" data-aos-duration="600"><spring:message code="lvserviceexpertsshort" text="default" /></p>
		<div class="row">
			<div class="col-md-6 col-sm-6 col-xs-12 quality_img aos-init text-right" data-aos="fade-left" data-aos-duration="600">
				<img src="images/quality_img.png" alt="">
			</div>
			<div class="col-md-6 col-sm-6 col-xs-12 aos-init" data-aos="fade-right" data-aos-duration="600">
				<div class="about_address">
					<i class="fa fa-map-marker" aria-hidden="true"></i>
					<spring:message code="street" text="default" />
					<br>
					<spring:message code="citystatezip" text="default" />
				</div>
				<div class="about_address">
					<i class="fa fa-mobile" aria-hidden="true"></i> 702-331-9043
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-3 col-sm-6 col-xs-12 aos-init" data-aos="fade-up" data-aos-duration="600">
				<div class="services">
					<div class="service_img">
						<img src="images/service_img1.png" alt="">
					</div>
					<h3><spring:message code="airductcleaning" text="default" /></h3>
					<p><spring:message code="airductstatement" text="default" /></p>
					<a href="/residentialairductcleaning"><spring:message code="readmore" text="default" /></a>
				</div>
			</div>
			<div class="col-md-3 col-sm-6 col-xs-12 aos-init" data-aos="fade-up" data-aos-duration="900">
				<div class="services">
					<div class="service_img">
						<img src="images/service_img2.png" alt="">
					</div>
					<h3><spring:message code="dryerventcleaning" text="default" /></h3>
					<p><spring:message code="dryerventstatement" text="default" /></p>
					<a href="/residentialdryerventcleaning"><spring:message code="readmore" text="default" /></a>
				</div>
			</div>
			<div class="col-md-3 col-sm-6 col-xs-12 aos-init" data-aos="fade-up" data-aos-duration="1500">
				<div class="services">
					<div class="service_img">
						<img src="images/service_img4.png" alt="">
					</div>
					<h3><spring:message code="furnacecleaning" text="default" /></h3>
					<p><spring:message code="ductworkstatement" text="default" /></p>
					<a href="/residentialfurnacecleaning"><spring:message code="readmore" text="default" /></a>
				</div>
			</div>
			<div class="col-md-3 col-sm-6 col-xs-12 aos-init" data-aos="fade-up" data-aos-duration="1200">
				<div class="services">
					<div class="service_img">
						<img src="images/service_img3.png" alt="">
					</div>
					<h3><spring:message code="commericalservices" text="default" /></h3>
					<p><spring:message code="commercialstatement" text="default" /></p>
					<a href="/commercialairductcleaning"><spring:message code="readmore" text="default" /></a>
				</div>
			</div>
		</div>
	</div>
</section>
<section class="about_area why_area">
	<div class="container">
		<h2 class="aos-init" data-aos="fade-down" data-aos-duration="600">
			<spring:message code="whyare" text="default" />
			<span><spring:message code="wedifferent" text="default" /></span>
		</h2>
		<div class="row">
			<div class="col-md-3 col-sm-6 col-xs-12 aos-init" data-aos="fade-up" data-aos-duration="600">
				<div class="services">
					<img src="images/service_img5.png" alt="">
					<h3><spring:message code="professionalism" text="default" /></h3>
					<p><spring:message code="guaranteework" text="default" /></p>
				</div>
			</div>
			<div class="col-md-3 col-sm-6 col-xs-12 aos-init" data-aos="fade-up" data-aos-duration="900">
				<div class="services">
					<img src="images/service_img6.png" alt="">
					<h3><spring:message code="integrity" text="default" /></h3>
					<p><spring:message code="guaranteework" text="default" /></p>
				</div>
			</div>
			<div class="col-md-3 col-sm-6 col-xs-12 aos-init" data-aos="fade-up" data-aos-duration="1200">
				<div class="services">
					<img src="images/service_img7.png" alt="">
					<h3><spring:message code="guaranteedservices" text="default" /></h3>
					<p><spring:message code="deliverpromise" text="default" /></p>
				</div>
			</div>
			<div class="col-md-3 col-sm-6 col-xs-12 aos-init" data-aos="fade-up" data-aos-duration="1500">
				<div class="services">
					<img src="images/service_img8.png" alt="">
					<h3><spring:message code="experience" text="default" /></h3>
					<p><spring:message code="tenyearsexperience" text="default" /></p>
				</div>
			</div>
		</div>
	</div>
</section>
<section class="about_area quote_area aos-init" data-aos="fade-up" data-aos-duration="600">
	<h2><spring:message code="calltoday" text="default" /></h2>
	<p><spring:message code="flatratepricing" text="default" /></p>
	<button type="button" class="" data-toggle="modal" data-target="#modalCenter"><spring:message code="getafreequote" text="default" /></button>
</section>