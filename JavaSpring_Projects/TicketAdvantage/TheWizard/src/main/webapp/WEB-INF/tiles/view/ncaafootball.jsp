<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<section class="bet_preview transaction_area">
	<div class="container">
		<nav class="breadcrumb_area" aria-label="breadcrumb">
			<ol class="breadcrumb">
				<li class="breadcrumb-item"><a href="#">HOME</a></li>
				<li class="breadcrumb-item active" aria-current="page">NCAA FOOTBALL</li>
			</ol>
		</nav>
		<div class="ncaa_area">
			<form action="/thewizard/searchncaaf" method="get">
				<div class="row">
					<div class=" col-12">
						<div class="btn_area">
							<div class="form-group">
								<div class="form_content search">
									<label for="searchterm">Search :</label>
									<input id="searchterm" name="searchterm" class="form-control" placeholder="search term (s)" />
								</div>
								<div class="form_content form-details">
									<label for="week">Week :</label> 
									<select id="week" name="week">
										<option value="0" selected="selected">Please Select</option>
										<option value="1">1</option>
										<option value="2">2</option>
										<option value="3">3</option>
										<option value="4">4</option>
										<option value="5">5</option>
										<option value="6">6</option>
										<option value="7">7</option>
										<option value="8">8</option>
										<option value="9">9</option>
										<option value="10">10</option>
										<option value="11">11</option>
										<option value="12">12</option>
										<option value="13">13</option>
										<option value="14">14</option>
									</select>
									<span>From :</span> 
									<input id="fromdate" name="fromdate" placeholder="8-27-2018 et" class="form-control" type="text" value="" />
									<span>To :</span> 
									<input id="todate" name="todate" placeholder="8-27-2018 et" class="form-control" type="text" value="" />
									<button type="submit" class="submit_btn btn">search</button>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="row list_box">
					<div class="box_list col-md-3 col-sm-6 col-xs-12">
						<div class="stock_block">
							<h2>Year</h2>
							<div class="stock_inner">
								<ul class="halfwidth">
									<li>
										<label class="radio_btn checkbox_btn"> 
											<input type="checkbox" id="year" name="year" value="2014" /> 
											<span class="checkmark"></span>
										</label> 2014
									</li>
									<li>
										<label class="radio_btn checkbox_btn"> 
											<input type="checkbox" id="year" name="year" value="2015" />
											<span class="checkmark"></span>
										</label> 2015
									</li>
									<li>
										<label class="radio_btn checkbox_btn"> 
											<input type="checkbox" id="year" name="year" value="2016" />
											<span class="checkmark"></span>
										</label> 2016
									</li>
									<li>
										<label class="radio_btn checkbox_btn"> 
											<input type="checkbox" id="year" name="year" value="2017" />
											<span class="checkmark"></span>
										</label> 2017
									</li>
									<li>
										<label class="radio_btn checkbox_btn"> 
											<input type="checkbox" id="year" name="year" value="2018" />
											<span class="checkmark"></span>
										</label> 2018
									</li>
									<li>
										<label class="radio_btn checkbox_btn"> 
											<input type="checkbox" id="year" name="year" value="2019" />
											<span class="checkmark"></span>
										</label> 2019
									</li>
								</ul>
							</div>
						</div>
					</div>
					<div class="box_list col-md-3 col-sm-6 col-xs-12">
						<div class="stock_block">
							<h2>bet type</h2>
							<div class="stock_inner">
								<ul class="halfwidth">
									<li>
										<label class="radio_btn checkbox_btn"> 
											<input type="checkbox" id="bettype" name="bettype" value="side" /> 
											<span class="checkmark"></span>
										</label> Side
									</li>
									<li>
										<label class="radio_btn checkbox_btn"> 
											<input type="checkbox" id="bettype" name="bettype" value="total" /> 
											<span class="checkmark"></span>
										</label> Total
									</li>
									<li>
										<label class="radio_btn checkbox_btn"> 
											<input type="checkbox" id="bettype" name="bettype" value="ml" /> 
											<span class="checkmark"></span>
										</label> Money Line
									</li>
									<li>
										<label class="radio_btn checkbox_btn"> 
											<input type="checkbox" id="bettype" name="bettype" value="teamtotal" /> 
											<span class="checkmark"></span>
										</label> Team Total
									</li>
									<li>
										<label class="radio_btn checkbox_btn"> 
											<input type="checkbox" id="bettype" name="bettype" value="game" /> 
											<span class="checkmark"></span>
										</label> Game
									</li>
									<li>
										<label class="radio_btn checkbox_btn"> 
											<input type="checkbox" id="bettype" name="bettype" value="1h" /> 
											<span class="checkmark"></span>
										</label> 1H
									</li>
									<li>
										<label class="radio_btn checkbox_btn"> 
											<input type="checkbox" id="bettype" name="bettype" value="2h" /> 
											<span class="checkmark"></span>
										</label> 2H
									</li>
								</ul>
							</div>
						</div>
					</div>
					<div class="box_list col-md-3 col-sm-6 col-xs-12">
						<div class="stock_block">
							<h2>options</h2>
							<div class="stock_inner">
								<ul class="halfwidth">
									<li>
										<label class="radio_btn checkbox_btn"> 
											<input type="checkbox" id="options" name="options" value="opponent"> 
											<span class="checkmark"></span>
										</label> Include Opponent
									</li>
									<li>
										<label class="radio_btn checkbox_btn"> 
											<input type="checkbox" id="options" name="options" value="team"> 
											<span class="checkmark"></span>
										</label> Team
									</li>
									<li>
										<label class="radio_btn checkbox_btn"> 
											<input type="checkbox" id="options" name="options" value="masseyrating"> 
											<span class="checkmark"></span>
										</label> Massey Rating
									</li>
									<li>
										<label class="radio_btn checkbox_btn"> 
											<input type="checkbox" id="options" name="options" value="sos"> 
											<span class="checkmark"></span>
										</label> Strength of Schedule
									</li>
									<li>
										<label class="radio_btn checkbox_btn"> 
											<input type="checkbox" id="options" name="options" value="conference"> 
											<span class="checkmark"></span>
										</label> Conference Only
									</li>
									<li>
										<label class="radio_btn checkbox_btn"> 
											<input type="checkbox" id="options" name="options" value="homegames"> 
											<span class="checkmark"></span>
										</label> Home Games
									</li>
									<li>
										<label class="radio_btn checkbox_btn"> 
											<input type="checkbox" id="options" name="options" value="awaygames"> 
											<span class="checkmark"></span>
										</label> Away Games
									</li>
									<li>
										<label class="radio_btn checkbox_btn"> 
											<input type="checkbox" id="options" name="options" value="coach"> 
											<span class="checkmark"></span>
										</label> Coach
									</li>
									<li>
										<label class="radio_btn checkbox_btn"> 
											<input type="checkbox" id="options" name="options" value="oyards"> 
											<span class="checkmark"></span>
										</label> Offensive Yards
									</li>
									<li>
										<label class="radio_btn checkbox_btn"> 
											<input type="checkbox" id="options" name="options" value="fcs"> 
											<span class="checkmark"></span>
										</label> Include FCS
									</li>
									<li>
										<label class="radio_btn checkbox_btn"> 
											<input type="checkbox" id="options" name="options" value="stadium"> 
											<span class="checkmark"></span>
										</label> Stadium
									</li>
									<li>
										<label class="radio_btn checkbox_btn"> 
											<input type="checkbox" id="options" name="options" value="win"> 
											<span class="checkmark"></span>
										</label> Win
									</li>
									<li>
										<label class="radio_btn checkbox_btn"> 
											<input type="checkbox" id="options" name="options" value="loss"> 
											<span class="checkmark"></span>
										</label> Loss
									</li>
									<li>
										<label class="radio_btn checkbox_btn"> 
											<input type="checkbox" id="options" name="options" value="linemovement"> 
											<span class="checkmark"></span>
										</label> Show Line Movement
									</li>
									<li>
										<label class="radio_btn checkbox_btn"> 
											<input type="checkbox" id="options" name="options" value="points"> 
											<span class="checkmark"></span>
										</label> Points
									</li>
								</ul>
							</div>
						</div>
					</div>
					<div class="box_list col-md-3 col-sm-6 col-xs-12">
						<div class="stock_block">
							<h2>sql query</h2>
							<div class="stock_inner">
								<ul class="halfwidth">
									<li>
										<label class="radio_btn checkbox_btn"> 
											<input type="checkbox" id="sqlquery" name="sqlquery" value="yes" /> 
											<span class="checkmark"></span>
										</label>SQL Query
									</li>
								</ul>
							</div>
						</div>
					</div>
					<div class="box_list col-md-3 col-sm-6 col-xs-12">
						<div class="stock_block">
							<h2>search fields</h2>
							<div class="stock_inner">
								<ul class="halfwidth">
								<c:forEach var="names" items="${names}">
									<li>
										<label class="radio_btn checkbox_btn"> 
											<input type="checkbox" id="searchfields" name="searchfields" value="<c:out value="${names}" />"> 
											<span class="checkmark"></span>
										</label> <c:out value="${names}" />
									</li>
								</c:forEach>				
								</ul>
							</div>
						</div>
					</div>
				</div>
				<div class="col-12 margin-0 serach-btn d-inline-flex w-100 ">
					<button type="submit" class="margin0 submit_btn btn">search</button>
				</div>
			</form>
		</div>
	</div>
</section>