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
    </div>

    <div class="row">
        <table>
            <td>
                <div class="col-md-12">
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
                    <c:if test="${game.status eq 'ONGOING'}">
                        <c:if test="${currentuser.myTurn}">
                            <h2>It's your turn</h2>
                            <c:if test="${game.has_started}">
                                <spring:url value="{gameId}/dice" var="diceUrl">
                                    <spring:param name="gameId" value="${game.game_id}" />
                                </spring:url>
                                <a href="${fn:escapeXml(diceUrl)}" class="btn btn-secondary active" role="button">Roll
                                    Dice</a>
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
