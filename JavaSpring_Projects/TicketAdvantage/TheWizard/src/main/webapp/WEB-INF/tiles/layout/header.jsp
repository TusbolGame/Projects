<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib  uri="http://www.springframework.org/tags" prefix="spring" %>
<header>
	<div class="top_header">
		<div class="container">
			<div class="user_img">
				<span><img src="img/user_img.jpg" alt=""></span>
				Hi Dexter
			</div>
			<div class="social_area">
				<a href="#"><i class="fa fa-sign-out" aria-hidden="true"></i></a>
			</div>
		</div>
	</div>
	<div class="logo">
		<a href="/">
			<img src="img/logo.png">
		</a>
	</div>
	<nav class="header-menu navbar navbar-default">
		<div class="container menu_icon">
			<div class="menu_text">Menu</div>
			<button type="button" onclick="openNav()" class="open menubar"></button>
			<div class="navbar-collapse sidenav" id="bs-example-navbar-collapse-1">
				<ul>
				    <c:if test="${activetab.equals('ncaaf')}">
				    		<li class="active"><a href="/thewizard/ncaafootball">ncaa footbalL</a></li>
						<li><a href="/thewizard/ncaabasketball">ncaa basketball</a></li>
						<li><a href="http://design.pinesucceed.com/the_wizard_html/mlb.html">mlb</a></li>
						<li><a href="http://design.pinesucceed.com/the_wizard_html/nfl.html">nfl</a></li>
						<li><a href="http://design.pinesucceed.com/the_wizard_html/nba.html">nba</a></li>
				    </c:if>
				    <c:if test="${activetab.equals('ncaab')}">
				    		<li><a href="/thewizard/ncaafootball">ncaa footbalL</a></li>
						<li class="active"><a href="/thewizard/ncaabasketball">ncaa basketball</a></li>
						<li><a href="http://design.pinesucceed.com/the_wizard_html/mlb.html">mlb</a></li>
						<li><a href="http://design.pinesucceed.com/the_wizard_html/nfl.html">nfl</a></li>
						<li><a href="http://design.pinesucceed.com/the_wizard_html/nba.html">nba</a></li>
				    </c:if>
				    <c:if test="${activetab.equals('nfl')}">
				    		<li><a href="/thewizard/ncaafootball">ncaa footbalL</a></li>
						<li><a href="/thewizard/ncaabasketball">ncaa basketball</a></li>
						<li><a href="http://design.pinesucceed.com/the_wizard_html/mlb.html">mlb</a></li>
						<li class="active"><a href="http://design.pinesucceed.com/the_wizard_html/nfl.html">nfl</a></li>
						<li><a href="http://design.pinesucceed.com/the_wizard_html/nba.html">nba</a></li>
				    </c:if>
				    <c:if test="${activetab.equals('nba')}">
				    		<li><a href="/thewizard/ncaafootball">ncaa footbalL</a></li>
						<li><a href="/thewizard/ncaabasketball">ncaa basketball</a></li>
						<li><a href="http://design.pinesucceed.com/the_wizard_html/mlb.html">mlb</a></li>
						<li><a href="http://design.pinesucceed.com/the_wizard_html/nfl.html">nfl</a></li>
						<li class="active"><a href="http://design.pinesucceed.com/the_wizard_html/nba.html">nba</a></li>
				    </c:if>
				    <c:if test="${activetab.equals('mlb')}">
				    		<li><a href="/thewizard/ncaafootball">ncaa footbalL</a></li>
						<li><a href="/thewizard/ncaabasketball">ncaa basketball</a></li>
						<li class="active"><a href="http://design.pinesucceed.com/the_wizard_html/mlb.html">mlb</a></li>
						<li><a href="http://design.pinesucceed.com/the_wizard_html/nfl.html">nfl</a></li>
						<li><a href="http://design.pinesucceed.com/the_wizard_html/nba.html">nba</a></li>
				    </c:if>
				</ul>
			</div>
		</div>
	</nav>
</header>