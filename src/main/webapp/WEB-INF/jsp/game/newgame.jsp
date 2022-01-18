<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ page import="org.springframework.samples.parchisoca.enums.GameStatus" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="parchisoca" tagdir="/WEB-INF/tags" %>

<%@ page import="org.springframework.samples.parchisoca.enums.TurnState" %>

<!-- %@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %-->

<parchisoca:gameLayout pageName="new game">
    <c:if test="${game.status == GameStatus.FINISHED}">
        <script type="text/javascript">
            if (confirm("The game has finished. Return back to the start screen?")) {
                window.location.href = "/"
            }

        </script>
    </c:if>

    <div class="row">
        <div class="col-md-5">
            <h2>PARCHIS</h2>
        </div>
        <div class="col-md-7">
            <button
                onclick="return alert('OBJECTIVE \nEach player has 4 pieces of the same colour (yellow, red, green and blue) and a start pace called home in the board' + 
            '\nThe board is composed by 68 numerated fields, from which, 12 are safe place (circle inside).'+
            '\nMoreover, each player has 7 fields of arrival and one finish (field of bigger size on the center of the board), exclusive for the pieces of that colour'+
            '\nThe goal is to be the first one to take the 4 pieces from the home field to the finish field, going through the whole board'+
            
           '\n\nHOW TO PLAY \nEvery player starts the game with their pieces on their respective home fields'+

            '\nBefore starting the match, each player will roll the dice. The one with the higher number will start'+

            '\nThe players can move their first piece from home, only if they get a 5. This action is obligated as long as the player has pieces at home, except if the starting field is occupied by 2 pieces')"
                type="button" class="btn btn-secondary m-1">RULES</button>

            <spring:url value="{gameId}/quit" var="quitURL">
                <spring:param name="gameId" value="${game.game_id}" />
            </spring:url>
            <a class="btn btn-danger m-1" href=${fn:escapeXml(quitURL)}>QUIT</a>
        </div>
    </div>


    <div class="row">
<<<<<<< HEAD
        <table>
            <td>
                <div class="col-md-6">
                    <c:if test="${game.status == FINISHED}">
                                <h5>The game has already finished, you can press the Quit button.</h5>
                    </c:if>
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
=======
        <div class="col-md-9">
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

        <div class="col-md-3">
            <c:if test="${game.has_started}">
                <div class="row">
                    <table class="table table-hover table-striped table-condensed rounded-3"
                        style="background-color: #FFFFFF;">
                        <tr class="fw-bold">
                            <th>Last Plays</th>
                        </tr>
                        <tbody>
                            <c:forEach items="${game.history_board}" var="history">
                                <tr class="fw-bolder">
                                    <td>
                                        <c:out value="${history}" />
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
>>>>>>> origin/develop
                </div>

                <c:if test="${currentuser.myTurn}">
                    <h3>It's your turn</h3>

                    <c:if test="${game.turn_state == TurnState.INIT}">
                        <spring:url value="{gameId}/dice" var="diceUrl">
                            <spring:param name="gameId" value="${game.game_id}" />
                        </spring:url>
                        <a href="${fn:escapeXml(diceUrl)}" class="btn btn-secondary active" role="button"
                            aria-pressed="true">Roll Dice</a>
                    </c:if>


                    <c:if test="${game.turn_state == TurnState.CHOOSEPLAY || game.turn_state == TurnState.DIRECTPASS}">
                        <div class="row">
                            <h6> You rolled: ${game.dice}</h6>

                            <parchisoca:dice number="${game.dice}" />

                            <c:choose>
                                <c:when test="${game.gameboard.options.size()} == 1">
                                    <h6>${game.options.get(0)}</h6>
                                </c:when>
                                <c:otherwise>

                                    <table class="table table-hover table-striped table-condensed rounded-3"
                                        style="background-color: #FFFFFF;">
                                        <tr class="fw-bold">
                                            <th>Option</th>
                                            <th>Choose</th>
                                        </tr>
                                        <tbody>
                                            <c:forEach items="${game.gameboard.options}" var="option">
                                                <tr>
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
<<<<<<< HEAD
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                        
                                    </c:otherwise>
                                </c:choose>
                            </c:if>

                            <c:if test="${game.turn_state == TurnState.CHOOSEEXTRA}">
                                <h5> Extra move</h5>

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
=======
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </c:if>

                    <c:if test="${game.turn_state == TurnState.CHOOSEEXTRA}">
                        <div class="row">
                            <h6> Extra move</h6>
                            <c:choose>
                                <c:when test="${game.gameboard.options.size()} == 1">
                                    <h6>${game.options.get(0)}</h6>
                                </c:when>
                                <c:otherwise>
                                    <table class="table table-hover table-striped table-condensed rounded-3"
                                        style="background-color: #FFFFFF;">
                                        <tr class="fw-bold">
                                            <th>Option</th>
                                            <th>Choose</th>
                                        </tr>
                                        <tbody>
                                            <c:forEach items="${game.gameboard.options}" var="option">
                                                <tr>
>>>>>>> origin/develop
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
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </c:if>
                </c:if>
            </c:if>
        </div>
    </div>
</parchisoca:gameLayout>
