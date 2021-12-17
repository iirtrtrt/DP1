<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="parchisoca" tagdir="/WEB-INF/tags" %>
<!-- %@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %-->

<parchisoca:layout pageName="home">
    <div class="row">
        <div class="col d-flex flex-wrap align-items-center">
            <img src="/resources/images/banner.png" />
        </div>
        <div class="col-9">
            <div class="row">
                <div class="col text-center">
                    <h2>
                        <fmt:message key="welcome" />
                    </h2>
                    <img class="w-100" src="/resources/images/members.png" />
                </div>
                <div class="col">
                    <h2> Group ${group}</h2>
                    <ul>
                        <c:forEach items="${persons}" var="person">
                            <li>${person.firstName} ${' '} ${person.lastName}</li>
                        </c:forEach>
                    </ul>
                </div>
            </div>
        </div>
    </div>
    <hr>
    <div class="row text-center">
        <div class="col">
            <h2> Project ${title}</h2>
        </div>
    </div>
    <div class="row text-center">
        <div class="col"><img style="width: 450px;" src="/resources/images/board_parchis.png" /></div>
        <div class="col"><img style="width: 450px;" src="/resources/images/board_oca.jpg" /></div>
    </div>
</parchisoca:layout>
