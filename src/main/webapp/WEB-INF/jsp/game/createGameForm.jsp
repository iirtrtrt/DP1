<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<petclinic:layout pageName="games">
    <div class="row">

    <c:if test="${not empty error}">
        <script>alert("hi")</script>
    </c:if>


        <!-- FORM for creating new game -->
        <div class="col-md-6">
            <h2 class="lead">New game</h2>
            <form:form method="POST" modelAttribute="game">
            <div class="section2">
                <h2>Game name</h2>
                <div class="form-group" ${status.error ? 'has-error' : ''}>
                    <form:input class = "form-control" path ="name" type="text" id="gamename" placeholder="Name"/>
                    <form:errors path="name" />
                </div>
                <div class="form-group" ${status.error ? 'has-error' : ''}>
                    <label for="max_player">Game type</label>:
                    <form:select  id="game_type" path="type">
                        <form:option value="Parchis" label="Parchis"/>
                        <form:option value="Oca" label="Oca"/>
                    </form:select><br/><br/>
                </div>

                <div class="form-group" ${status.error ? 'has-error' : ''}>
                    <label for="max_player">Max player</label>:
                    <form:select  id="max_player" path="max_player">
                        <form:option value="2">2</form:option>
                        <form:option value="3">3</form:option>
                        <form:option value="4">4</form:option>
                    </form:select><br/><br/>
                </div>

            </div>
            <div class="section2">
                <h2>Creator <c:out value="${user.username}'s"/> color</h2>
                <form:form method="POST" modelAttribute="user">
                    <div>
                        <label for="color">Color</label>: <form:select type="text" id="color" path="tokenColor">
                        <form:option value="yellow">yellow</form:option>
                        <form:option value="red">red</form:option>
                        <form:option value="blue">blue</form:option>
                        <form:option value="green">green</form:option>
                    </form:select><br/><br/><form:errors path="tokenColor" />
                    </div>

                </form:form>
                <div class="form-group">
                    <div class="col-sm-offset-4 col-sm-6" style="padding-bottom:50px">
                        <button type="submit" class="btn btn-md btn-primary"  >
                            <span class="glyphicon glyphicon-plus"></span> New Game </button>
                    </div>
                </div>
            </div>
            </form:form>

            <div class="col-md-6" >

                <table class="table table-hover table-striped table-condensed" >
                    <h2 class="lead">Join existing game</h2>
                    <thead>
                    <td>ID</td>
                    <td>Name</td>
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

            <form:form action = "${gameURL2}" method="POST" modelAttribute="colorWrapper"  >
            <td>
                            <c:out value="${game.game_id}"/>
                        </td>
                        <td>
                            <spring:url value="/game/join/{gameType}/{gameID}" var="gameURL">
                                <spring:param name="gameType" value="${game.type}"></spring:param>
                                <spring:param name="gameID" value="${game.game_id}"></spring:param>
                            </spring:url>
                            <c:out value="${game.name} "/>
                        </td>
                        <td>
                            <c:out value="${game.creator.username}"/>
                        </td>
                        <td>
                            <c:out value="${game.type}"/>
                        </td>
                        <td>
                            <c:out value="${game.status}"/>
                        </td>
                        <td>
                            <c:out value="${game.startTime}"/>
                        </td>
                    <td>
                        <div>
                            <label for="colorJoin">Color</label>:
                            <form:select type="text" id="colorJoin" path="colorName">
                                <form:option value="yellow">yellow</form:option>
                                <form:option value="red">red</form:option>
                                <form:option value="blue">blue</form:option>
                                <form:option value="green">green</form:option>
                        </form:select>
                            <form:errors path="colorName" />
                            <br/><br/>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-offset-4 col-sm-6" style="padding-bottom:50px">
                                <button type="submit" class="btn btn-md btn-primary"  >
                                    <span class="glyphicon glyphicon-plus"></span> New Game </button>
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

</petclinic:layout>
