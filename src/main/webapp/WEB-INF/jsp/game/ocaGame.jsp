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

    <c:if test="${game.status == GameStatus.FINISHED}">
        <script type="text/javascript">
            if(confirm("The game has finished. Return back to the start screen?")) {
                window.location.href = "/"
            }
        </script>
    </c:if>

    <div class="row">
        <div class="col-6">
            <h2>
                <fmt:message key="welcome_to_new_game" />
            </h2>
        </div>
        <div class="col-6">
            <button onclick="return alert('OBJECTIVE \nWalk through the whole board the faster you can, getting into the space: number 63, \'The garden of the geese\''+
        
       '\n\nHOW TO PLAY \nThe game of the Goose is a board game for 2 to 4 players each with a colored piece'+
        
        '\nThe spiral shaped board has 63 spaces numbered from 1 to 63 with different drawings. Depending on the space you enter, you piece will advance, move back or suffer a penalty'+
        
        '\nIn a turn a player rolls a dice wich indicates the number of spaces to advance'+
        
        '\nThe space 63 can only be entered with an exact roll. If a player rolls and gets a higher number than the number of remaining spaces to the goal, the player will advance to the space 63 and then go back until completing the number rolled'
        
        )" style="margin-top: 5px;" type="button" class="btn btn-secondary">RULES</button>
        </div>
        <div class="col-6">
            <spring:url value="{gameId}/quit" var="quitURL">
                <spring:param name="gameId" value="${game.game_id}" />
            </spring:url>
            <a class="btn btn-secondary" href=${fn:escapeXml(quitURL)}>QUIT</a>
        </div>

    </div>

    <div class="row">
        <table>
            <td>
                <div class="col-md-6">
                    <parchisoca:oca oca="${game.gameboard}" />
                    <c:forEach items="${game.gameboard.fields}" var="field">
                        <parchisoca:ocaBoardField size="100" field="${field}" />
                    </c:forEach>
                    <c:forEach items="${game.other_players}" var="player">
                        <c:forEach items="${player.gamePieces}" var="piece">
                            <parchisoca:ocaPiece size="100" piece="${piece}" />
                        </c:forEach>
                    </c:forEach>
                    <c:forEach items="${game.creator.gamePieces}" var="piece">
                        <parchisoca:ocaPiece size="100" piece="${piece}" />
                    </c:forEach>
                </div>
            </td>
            <td>
                <div class="col-md-12">
                    <c:if test="${game.has_started}">
                        <c:if test="${currentuser.myTurn}">
                            <h2>It's your turn</h2>
                            <c:if test="${game.actionMessage == 1}">
                                <h5>You stepped into a goose, so you moved to the next goose and reroll the dice</h5>
                            </c:if>
                            <c:if test="${game.actionMessage == 2}">
                                <h5>You stepped into a dice, so you moved to the other dice and reroll the dice</h5>
                            </c:if>
                            <c:if test="${game.actionMessage == 3}">
                                <h5>You stepped into a bridge, so you moved to the other bridge and reroll the dice</h5>
                            </c:if>
                            <!-- <c:if test="${game.actionMessage == 4}">
                                <h5>You stepped into a stun, so you will be kept </h5>
                            </c:if>
                            <c:if test="${game.actionMessage == 5}">
                                <h5>You stepped into a goose, so you moved to the next goose and reroll the dice</h5>
                            </c:if> -->
                            
                            <c:if test="${game.turn_state == TurnState.INIT}">
                                <spring:url value="{gameId}/dice" var="diceUrl">
                                    <spring:param name="gameId" value="${game.game_id}" />
                                </spring:url>
                                <a href="${fn:escapeXml(diceUrl)}" class="btn btn-secondary active" role="button"
                                    aria-pressed="true">Roll Dice</a>
                            </c:if>
    
                            <c:if test="${game.turn_state == TurnState.CHOOSEPLAY || game.turn_state == TurnState.DIRECTPASS}">
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

<script>
    $('a').click(function (e) {
        if (e.ctrlKey) {
            return false;
        }
    });

</script>
