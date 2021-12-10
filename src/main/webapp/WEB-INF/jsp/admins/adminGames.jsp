<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="parchisoca" tagdir="/WEB-INF/tags" %>

<parchisoca:admin pageName="adminGames">
    <div class="row">

        <c:if test="${not empty error}">
            <input type="hidden" id="name-parameter" value="<c:out value=" ${error.error_message}" />"/>
            <script type="text/javascript">
                var name = document.getElementById('name-parameter').value;
                alert('ERROR: ' + name);

            </script>
        </c:if>
        <div class="col-md-6 p-3 m-3 border border-secondary w-100 rounded">
            <h2 class="lead">List of games</h2>
            <hr>
            <table class="table table-hover table-striped table-condensed">

                <thead>
                    <td>ID</td>
                    <td>Name</td>
                    <td>Joined players</td>
                    <td>Creator</td>
                    <td>Game type</td>
                    <td>Game status</td>
                    <td>Created</td>
                </thead>
                <tbody>
                    <c:forEach items="${games}" var="game">
                        <spring:url value="join/{gameType}/{gameID}" var="gameURL2">
                            <spring:param name="gameType" value="${game.type}"></spring:param>
                            <spring:param name="gameID" value="${game.game_id}"></spring:param>
                        </spring:url>
                            <td>
                                <c:out value="${game.game_id}" />
                            </td>
                            <td>
                                <!-- <spring:url value="/game/join/{gameType}/{gameID}" var="gameURL">
                                    <spring:param name="gameType" value="${game.type}"></spring:param>
                                    <spring:param name="gameID" value="${game.game_id}"></spring:param>
                                </spring:url> -->
                                <c:out value="${game.name} " />
                            </td>
                            <td>
                                <c:out value="${fn:length(game.other_players) + 1} /  ${game.max_player}" />
                            </td>
                            <td>
                                <c:out value="${game.creator.username}" />
                            </td>
                            <td>
                                <c:out value="${game.type}" />
                            </td>
                            <td>
                                <c:out value="${game.status}" />
                            </td>
                            <td>
                                <c:out value="${game.startTime}" />
                            </td>
                            </tr>
                    </c:forEach>
                </tbody>

            </table>

            <c:if test="${empty games}">
                <div>
                    No games.
                </div>
            </c:if>

        </div>

</parchisoca:admin>
