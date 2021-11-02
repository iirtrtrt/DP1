<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<!-- %@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %-->  

<petclinic:layout pageName="new game">


    <h2><fmt:message key="welcome_to_new_game"/></h2>
    

    <div class="row">
        <div class="col-md-12">
            <petclinic:parchis parchis="${parchis}"/>
            <c:forEach items="${parchis.pieces}" var="piece">
            	<petclinic:gamePiece size="100" piece="${piece}"/> 
            	
            </c:forEach>
            <c:forEach items="${parchis.fields}" var="field">
            	 <petclinic:boardField size="40" field="${field}"/> 
            </c:forEach>

        </div>
    </div>
</petclinic:layout>