<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="parchisoca" tagdir="/WEB-INF/tags" %>


<parchisoca:layout pageName="games">
    <div class="row">

        <!-- FORM for creating new game -->
        <div class="col-md-6 p-3 m-3 border border-secondary w-100 rounded">
            <h2 class="lead">New game</h2>
            <hr>
            <form:form method="POST" modelAttribute="game">
                <div class="section2">
                    <div class="row">
                        <div class="col-3 text-end">
                            <h3>Game name :</h3>
                        </div>
                        <div class="col">
                            <div class="form-group" ${status.error ? 'has-error' : '' }>
                                <form:input class="form-control" path="name" type="text" id="gamename"
                                    placeholder="Name" />
                                <form:errors path="name" />
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-3 text-end">
                            <h3>Game type :</h3>
                        </div>
                        <div class="col">
                            <div class="form-group" ${status.error ? 'has-error' : '' }>
                                <form:select id="game_type" path="type">
                                    <form:option value="Parchis" label="Parchis" />
                                    <form:option value="Oca" label="Oca" />
                                </form:select>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-3 text-end">
                            <h3>Max player :</h3>
                        </div>
                        <div class="col">
                            <div class="form-group" ${status.error ? 'has-error' : '' }>
                                <form:select id="max_player" path="max_player">
                                    <form:option value="2">2</form:option>
                                    <form:option value="3">3</form:option>
                                    <form:option value="4">4</form:option>
                                </form:select>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-3 text-end">
                            <h3>AI :</h3>
                        </div>
                        <div class="col">
                            <div class="form-group" ${status.error ? 'has-error' : '' }>
                                <form:checkbox path="AI" />
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-3 text-end">
                            <h3>
                                <c:out value="${user.username}'s" /> color :</h3>
                        </div>
                        <div class="col">
                            <form:form method="POST" modelAttribute="user">
                                <div>
                                    <form:select type="text" id="color" path="tokenColor">
                                        <form:option value="yellow">yellow</form:option>
                                        <form:option value="red">red</form:option>
                                        <form:option value="blue">blue</form:option>
                                        <form:option value="green">green</form:option>
                                    </form:select>
                                    <form:errors path="tokenColor" />
                                </div>
                            </form:form>
                        </div>
                    </div>
                    <br>
                    <div class="row form-group">
                        <button type="submit" class="btn btn-md btn-primary"> New Game </button>
                    </div>
                </div>
            </form:form>
</parchisoca:layout>
