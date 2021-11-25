<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="error">
    <div class="row">
        <div class="col-5">
            <h2>Error occurred</h2>
            <p>${exception.message}</p>
        </div>
        <div class="col">
            <spring:url value="/resources/images/eyes.png" var="eyes" />
            <img style="width : 700px" src="${eyes}" />
        </div>
    </div>
</petclinic:layout>
