<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="parchisoca" tagdir="/WEB-INF/tags" %>
<parchisoca:layout pageName="games">
    <div class="row">

        <c:if test="${not empty error}">
            <input type="hidden" id="name-parameter" value="<c:out value=" ${error.error_message}" />"/>
            <script type="text/javascript">
                var name = document.getElementById('name-parameter').value;
                alert('ERROR: ' + name);

            </script>
        </c:if>
        <div class="col-md-6 p-3 m-3 border border-secondary w-100 rounded">
            <h2 class="lead">Join existing game</h2>
            <hr>
            <table class="table table-hover table-striped table-condensed">

                <thead>
                    <td>ID</td>
                    <td>Name</td>
                    <td>Joined players</td>
                    <td>Started player</td>
                    <td>Game type</td>
                    <td>Game status</td>
                    <td>Created</td>
                    <td>Color</td>
                    <td></td>
                </thead>
                <tbody>
                    <c:forEach items="${games}" var="game">
                        <spring:url value="join/{gameType}/{gameID}" var="gameURL2">
                            <spring:param name="gameType" value="${game.type}"></spring:param>
                            <spring:param name="gameID" value="${game.game_id}"></spring:param>
                        </spring:url>

                        <form:form action="${gameURL2}" method="POST" modelAttribute="colorWrapper">
                            <td>
                                <c:out value="${game.game_id}" />
                            </td>
                            <td>
                                <spring:url value="/game/join/{gameType}/{gameID}" var="gameURL">
                                    <spring:param name="gameType" value="${game.type}"></spring:param>
                                    <spring:param name="gameID" value="${game.game_id}"></spring:param>
                                </spring:url>
                                <c:out value="${game.name} " />
                            </td>
                            <td>
                                <c:out value="${fn:length(game.current_players)} /  ${game.max_player}" />
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
                            <td>
                                <label for="colorName">Color</label>:
                                <form:select type="text" id="colorName" path="colorName">
                                    <form:option value="yellow">yellow</form:option>
                                    <form:option value="red">red</form:option>
                                    <form:option value="blue">blue</form:option>
                                    <form:option value="green">green</form:option>
                                </form:select>
                            </td>
                            <td>
                                <form:errors path="colorName" cssClass="error" />
                            </td>
                            <td>
                                <div class="form-group">
                                    <div class="col-sm-offset-4 col-sm-6" style="padding-bottom:50px">
                                        <button type="submit" class="btn btn-md btn-primary">
                                            <span class="glyphicon glyphicon-plus"></span> Join Game</button>
                                    </div>
                                </div>
                            </td>
                            </tr>
                        </form:form>
                    </c:forEach>
                </tbody>

            </table>

            <c:if test="${empty games}">
                <div>
                    No games to join.
                </div>
            </c:if>

        </div>

</parchisoca:layout>
