<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="games">
    <div class="row">

    <!-- FORM for creating new game -->
    <div class="col-md-6">
        <h2 class="lead">New game</h2>
        <form:form name="createGameForm" class="form-group" modelAttribute="game">
            <div class="form-group">
                <label for="gamename">Game Name</label>
                <form:input class="form-control" path ="name" type="text" id="gamename" placeholder="Name"/>
            </div>
            <div class="form-group" ${status.error ? 'has-error' : ''}>
                <label for="type-select" >Game type:</label>
                <form:select path="type" id="type-select">
                    <form:option value="Parchis" label="Parchis"/>
                    <form:option value="Oca" label="Oca"/>
                </form:select>
            </div>
            <div class="form-group" ${status.error ? 'has-error' : ''}>
                <label for="type-select" >Token color:</label>
                <form:select path="tokenColor" id="type-select">
                    <form:option   value="yellow" label="Yellow"/>
                    <form:option   value="red" label="Red"/>
                    <form:option   value="blue" label="Blue"/>
                    <form:option  value="green" label="Green"/>

                </form:select>
            </div>
            <div class="form-group" ${status.error ? 'has-error' : ''}>
                <label for="type-select" >Max players:</label>
                <label> 2 <form:radiobutton path="max_player" value="2"/> </label>
                <label> 3 <form:radiobutton path="max_player"  value="3"/> </label>
                <label> 4 <form:radiobutton path="max_player" value="4"/> </label>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-4 col-sm-6" style="padding-bottom:50px">
                    <button type="submit" class="btn btn-md btn-primary" >
                        <span class="glyphicon glyphicon-plus"></span> New Game </button>
                </div>
            </div>
        </form:form>
    </div>
    </div>
</petclinic:layout>
