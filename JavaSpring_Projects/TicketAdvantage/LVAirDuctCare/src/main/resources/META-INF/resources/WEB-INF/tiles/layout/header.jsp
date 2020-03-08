<%@ taglib  uri="http://www.springframework.org/tags" prefix="spring" %>
	<header>
		<div class="top_header">
			<div class="container">
				<div class="row">
					<div class="col-8 address_area aos-init" data-aos="fade-right" data-aos-duration="600">
						<div class="address"><i class="fa fa-map-marker" aria-hidden="true"></i><spring:message code="address" text="default"/></div>
						<div class="address"><i class="fa fa-mobile" aria-hidden="true"></i><spring:message code="phonenumberheader" text="default"/></div>
					</div>
					<div class="col-4 aos-init" data-aos="fade-left" data-aos-duration="600">
						<div class="social_area">
							<a href="https://twitter.com/lvairductcare" target="_blank"><i class="fa fa-twitter" aria-hidden="true"></i></a>
							<a href="https://www.facebook.com/John-Miller-861896690864360/?modal=admin_todo_tour" target="_blank"><i class="fa fa-facebook" aria-hidden="true"></i></a> 
							<a href="https://www.linkedin.com/company/lv-air-duct-care/" target="_blank"><i class="fa fa-linkedin" aria-hidden="true"></i></a>
							<a href="https://www.instagram.com/lvairductcare/" target="_blank"><i class="fa fa-instagram" aria-hidden="true"></i></a>
							<a href="http://www.youtube.com/" target="_blank"><i class="fa fa-youtube" aria-hidden="true"></i></a>
							<a href="javascript:void(0)" class="search-open search"><i class="fa fa-search" aria-hidden="true"></i></a>
							<div class="search-inline">
								<form>
                        				<input type="text" class="form-control" placeholder="<spring:message code="searching" text="default"/>"/>
			                        <!--<button type="submit">
			                            <i class="fa fa-search"></i>
			                        </button>-->
									<a href="javascript:void(0)" class="search-close">
										<i class="fa fa-times"></i>
									</a>
								</form>
							</div>
						</div>
			            <script>
							var sp = document.querySelector('.search-open');
				            var searchbar = document.querySelector('.search-inline');
				            var shclose = document.querySelector('.search-close');
				            function changeClass() {
				                searchbar.classList.add('search-visible');
				            }
				            function closesearch() {
				                searchbar.classList.remove('search-visible');
				            }
				            sp.addEventListener('click', changeClass);
				            shclose.addEventListener('click', closesearch);                
			            </script>
					</div>
				</div>
			</div>
		</div>
		<nav class="header-menu navbar navbar-default">
			<div class="container">
				<div class="col-2 aos-init" data-aos="fade-right" data-aos-duration="600">
					<div class="logo"><a href="/"><img src="images/logo.png"/></a></div>
				</div>
				<div class="col-10 aos-init" data-aos="fade-left" data-aos-duration="600">
					<button type="button" onClick="openNav()" class="open menubar"></button>
					<div class="navbar-collapse sidenav" id="bs-example-navbar-collapse-1">
						<ul class="nav">                                        
							<li><a class="active" href="/"><spring:message code="home" text="default"/></a></li>
							<li><a href="/airduct"><spring:message code="airduct" text="default"/></a></li>
							<li><a href="/dryerduct"><spring:message code="dryerduct" text="default"/></a></li>
							<li><a href="/chimney"><spring:message code="chimney" text="default"/></a></li>
<!-- 						<li><a href="/furnace"><spring:message code="furnace" text="default"/></a></li>  -->
<!--							<li><a href="/commerical"><spring:message code="commercial" text="default"/></a></li>  -->
							<li><a href="/#pricing"><spring:message code="pricing" text="default"/></a></li>
							<li><a href="/#booking"><spring:message code="booking" text="default"/></a></li>
							<li><a href="/contact"><spring:message code="contactus" text="default"/></a></li>
						</ul>
					</div>
				</div>
			</div>
		</nav> 
	</header>