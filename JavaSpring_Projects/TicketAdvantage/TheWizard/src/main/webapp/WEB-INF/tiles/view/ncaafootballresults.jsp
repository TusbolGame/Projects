<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<section class="bet_preview transaction_area">
	<form action="/thewizard/ncaafootball" method="get">
		<button type="submit" class="submit_btn btn">back</button>
	</form>
	<br/>
	<br/>

	<c:forEach var="espnGame" items="${espnGames}">
 		<c:out value="${espnGame}" />
 		<br/>
 		<br/>
	</c:forEach>
</section>