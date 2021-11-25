<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:url value="/resources/js/bootstrap.min.js" var="bootstrapJs"/>
<script src="${bootstrapJs}"></script>

<spring:url value="/resources/js/jquery-3.6.0.min.js" var="jquery"/>
<script src="${jquery}"></script>

<spring:url value="/resources/js/jquery-ui.min.js" var="jqueryUi"/>
<script src="${jqueryUi}"></script>