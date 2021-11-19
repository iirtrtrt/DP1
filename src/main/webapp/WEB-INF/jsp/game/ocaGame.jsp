<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<!-- %@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %-->
<script type="text/javascript" src="/resources/js/jquery-3.6.0.min.js"></script>
<script type="text/javascript" src="/resources/js/bootstrap.min.js"></script>
<link href="/resources/css/bootstrap.min.css" rel="stylesheet">
<petclinic:layout pageName="new game">

    <h2>
        <fmt:message key="welcome_to_new_game" />
    </h2>

    <div class="row">
        <div class="col">left</div>
        <div class="col-9">
            <!-- <petclinic:oca oca="${game.gameboard}" /> -->
            <c:forEach items="${game.other_players}" var = "player">
                <c:forEach items="${player.gamePieces}" var="piece">
                    <petclinic:gamePiece size="102" piece="${piece}"/> 
                </c:forEach>
            </c:forEach>

            <c:forEach items="${game.gameboard.fields}" var="field">
            	 <petclinic:boardField size="102" field="${field}"/> 
            </c:forEach>
        </div>
        <div class="col">
            <petclinic:dice />
        </div>
    </div>
</petclinic:layout>
