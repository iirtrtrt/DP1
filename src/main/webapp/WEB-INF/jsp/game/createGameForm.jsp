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
</petclinic:layout>
