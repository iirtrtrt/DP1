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


    <div class="row">
        <div class="col-6"><h2>
            <fmt:message key="welcome_to_new_game" />
        </h2></div>
        <div class="col-6">
            <button onclick="return alert('OBJECTIVE \nEach player has 4 pieces of the same colour (yellow, red, green and blue) and a start pace called home in the board' + 
            '\nThe board is composed by 68 numerated fields, from which, 12 are safe place (circle inside).'+
            '\nMoreover, each player has 7 fields of arrival and one finish (field of bigger size on the center of the board), exclusive for the pieces of that colour'+
            '\nThe goal is to be the first one to take the 4 pieces from the home field to the finish field, going through the whole board'+
            
           '\n\nHOW TO PLAY \nEvery player starts the game with their pieces on their respective home fields'+

            '\nBefore starting the match, each player will roll the dice. The one with the higher number will start'+

            '\nThe players can move their first piece from home, only if they get a 5. This action is obligated as long as the player has pieces at home, except if the starting field is occupied by 2 pieces')"
            style="margin-top: 5px;" type="button" class="btn btn-secondary">RULES</button>
            
        </div>
    </div>


    <div class="row">
        <table>
            <td>
                <div class="col-md-6">
                    <parchisoca:parchis parchis="${game.gameboard}" />
                    <c:forEach items="${game.gameboard.fields}" var="field">
                        <parchisoca:boardField size="40" field="${field}" />
                    </c:forEach>
                    <c:forEach items="${game.other_players}" var="player">
                        <c:forEach items="${player.gamePieces}" var="piece">
                            <parchisoca:gamePiece size="40" piece="${piece}" />
                        </c:forEach>
                    </c:forEach>
                    <c:forEach items="${game.creator.gamePieces}" var="piece">
                        <parchisoca:gamePiece size="40" piece="${piece}" />
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
                                <a href="${fn:escapeXml(diceUrl)}" class="btn btn-secondary active" role="button"
                                    aria-pressed="true">Roll Dice</a>
                            </c:if>

                            <c:if test="${game.turn_state == TurnState.CHOOSEPLAY}">
                                <h5> You rolled: ${game.dice}</h5>
                                <parchisoca:dice number="${game.dice}" />
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
                                                            class="btn btn-secondary active" role="button"
                                                            aria-pressed="true">Choose</a>
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
</parchisoca:layout>
