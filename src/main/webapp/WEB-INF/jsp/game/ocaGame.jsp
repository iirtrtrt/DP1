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
        <table>
            <td>
                <div class="col-md-12">
                    <petclinic:oca oca="${game.gameboard}" />
                    <c:forEach items="${game.gameboard.fields}" var="field">
                        <petclinic:ocaBoardField size="100" field="${field}" />
                    </c:forEach>
                    <c:forEach items="${game.other_players}" var="player">
                        <c:forEach items="${player.gamePieces}" var="piece">
                            <petclinic:ocaPiece size="100" piece="${piece}" />
                        </c:forEach>
                    </c:forEach>
                    <c:forEach items="${game.creator.gamePieces}" var="piece">
                        <petclinic:ocaPiece size="100" piece="${piece}" />
                    </c:forEach>
                </div>
            </td>
            <td>
                <div class="col-md-12">
                    <c:if test="${game.has_started}">
                        <c:if test="${currentuser.myTurn}">
                            <h2>It's your turn</h2>

                            <c:if test="${game.turn_state == TurnState.INIT}">
                                <spring:url value="{gameId}/dice" var="diceUrl">
                                    <spring:param name="gameId" value="${game.game_id}" />
                                </spring:url>
                                <a href="${fn:escapeXml(diceUrl)}" class="btn btn-default">Roll Dice</a>
                            </c:if>
                        </c:if>
                    </c:if>
                </div>
            </td>
        </table>
    </div>
</parchisoca:layout>
