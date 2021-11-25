<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="parchisoca" tagdir="/WEB-INF/tags" %>

<%@ page import="org.springframework.samples.parchisoca.enums.TurnState" %>

<!-- %@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %-->

<parchisoca:layout pageName="new game">


    <h2>
        <fmt:message key="welcome_to_new_game" />
    </h2>


    <div class="row">
        <div class="col-md-12">
            <parchisoca:oca oca="${game.gameboard}" />
            <c:forEach items="${game.gameboard.fields}" var="field">
                <parchisoca:ocaBoardField size="100" field="${field}" />
            </c:forEach>
            <c:forEach items="${game.other_players}" var="player">
                <c:forEach items="${player.gamePieces}" var="piece">
                    <parchisoca:ocaPiece size="40" piece="${piece}" />
                </c:forEach>
            </c:forEach>
            <c:forEach items="${game.creator.gamePieces}" var="piece">
                <parchisoca:ocaPiece size="40" piece="${piece}" />
            </c:forEach>
        </div>
    </div>
</parchisoca:layout>
