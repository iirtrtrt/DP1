<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="parchisoca" tagdir="/WEB-INF/tags" %>
<parchisoca:layout pageName="invitationForm">
    <div class="row">


        <div class="col-md-6 p-3 m-3 border border-secondary w-100 rounded">
            <h2 class="lead">Invite users</h2>
            <hr>
            <table class="table table-hover table-striped table-condensed">

                <thead>
                    <td>Username</td>
                    <td>First name</td>
                    <td>Last name</td>
                    <td>Email</td>
                    <td></td>
                </thead>
                <tbody>
                    <c:forEach items="${users}" var="user">

                        <form:form action="${gameURL2}" method="POST" modelAttribute="colorWrapper">
                            <td>
                                <c:out value="${user.username}" />
                            </td>
                            <td>
                                <c:out value="${user.firstname}" />
                            </td>
                            <td>
                                <c:out value="${user.lastname}" />
                            </td>
                            <td>
                                <c:out value="${user.email}" />
                            </td>                            <td>
                                <div class="form-group">
                                    <div class="col-sm-offset-4 col-sm-6" style="padding-bottom:50px">
                                        <button type="submit" class="btn btn-md btn-primary">
                                            <span class="glyphicon glyphicon-send"></span> Join Game</button>
                                    </div>
                                </div>
                            </td>
                            </tr>
                        </form:form>
                    </c:forEach>
                </tbody>

            </table>



        </div>

</parchisoca:layout>
