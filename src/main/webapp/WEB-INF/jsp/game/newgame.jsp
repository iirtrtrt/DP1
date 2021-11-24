<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<%@ page import="org.springframework.samples.parchisoca.enums.TurnState" %>

<!-- %@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %-->

<petclinic:layout pageName="new game">


    <h2>
        <fmt:message key="welcome_to_new_game" />
    </h2>


    <div class="row">
        <table>
            <td>
                <div class="col-md-6">
                    <petclinic:parchis parchis="${game.gameboard}" />
                    <c:forEach items="${game.gameboard.fields}" var="field">
                        <petclinic:boardField size="40" field="${field}" />
                    </c:forEach>
                    <c:forEach items="${game.other_players}" var="player">
                        <c:forEach items="${player.gamePieces}" var="piece">
                            <petclinic:gamePiece size="40" piece="${piece}" />
                        </c:forEach>
                    </c:forEach>
                    <c:forEach items="${game.creator.gamePieces}" var="piece">
                        <petclinic:gamePiece size="40" piece="${piece}" />
                        <h2>${piece.field}</h2>
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

                            <c:if test="${game.turn_state == TurnState.CHOOSEPLAY}">
                                <h5> You rolled: ${game.dice}</h5>
                                <petclinic:dice number="${game.dice}" />
                                <c:choose>
                                    <c:when test="${game.gameboard.options.size()} == 1">
                                        <h5>${game.options.get(0)}</h5>
                                    </c:when>
                                    <c:otherwise>
                                        <table class="table table-hover table-striped table-condensed">
                                            <thead>
                                                <td>Option</td>
                                                <td>Choose</td>
                                            </thead>
                                            <tbody>
                                                <c:forEach items="${game.gameboard.options}" var="option">
                                                    <td>
                                                        <c:out value="${option.text}" />
                                                    </td>
                                                    <td>
                                                        <spring:url value="{gameid}/choice/{choiceid}" var="choiceUrl">
                                                            <spring:param name="choiceid" value="${option.number}" />
                                                            <spring:param name="gameid" value="${game.game_id}" />
                                                        </spring:url>
                                                        <a href="${fn:escapeXml(choiceUrl)}"
                                                            class="btn btn-default">Choose</a>
                                                    </td>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </c:otherwise>
                                </c:choose>
                            </c:if>
                        </c:if>
                    </c:if>
                </div>
            </td>
        </table>
    </div>
</petclinic:layout>
