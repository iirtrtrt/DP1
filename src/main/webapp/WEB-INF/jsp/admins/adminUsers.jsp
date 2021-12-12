<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="parchisoca" tagdir="/WEB-INF/tags" %>

<parchisoca:admin pageName="users">
    <div class="row">
        <div class="col-md-6 p-3 m-3 border border-secondary w-100 rounded">
            <div class="row">
                <div class="col">
                    <h2 class="lead">User List</h2>
                </div>
                <div class="col d-flex">
                    <a href='<c:url value="/admin/register" />'
                        class="btn btn-success btn-block text-capitalize ms-auto">Register a new User</a>
                </div>
            </div>
            <hr>
            <div>
                <h3>Admins</h3>
                <table></table>
            </div>
            <hr>
            <div>
                <h3>Users</h3>
                <table class="table table-hover table-striped table-condensed text-center">
                    <thead>
                        <td>Name</td>
                        <td>Email</td>
                        <td>CreateDate</td>
                        <td>UserDetails</td>
                    </thead>
                    <tbody>
                        <c:forEach items="${users}" var="user">
                            <spring:url value="users/{username}" var="userURL">
                                <spring:param name="username" value="${user.username}"></spring:param>
                            </spring:url>

                            <form:form action="${userURL}" method="POST" modelAttribute="colorWrapper">
                                <tr>
                                    <td>
                                        <c:out value="${user.username}" />
                                    </td>
                                    <td>
                                        <c:if test="${user.email}">
                                            <c:out value="${user.email}" />
                                        </c:if>
                                        <c:if test="${!user.email}">
                                            None
                                        </c:if>
                                    </td>
                                    <td>
                                        <c:if test="${user.createdTime}">
                                            <c:out value="${user.createdTime}" />
                                        </c:if>
                                        <c:if test="${!user.createdTime}">
                                            None
                                        </c:if>
                                    </td>
                                    <td>
                                        <div class="form-group"><button type="submit"
                                                class="btn btn-md btn-secondary">Details</button>
                                    </td>
                                </tr>
                            </form:form>
                        </c:forEach>
                    </tbody>
                </table>

                <c:if test="${empty users}">
                    <div>
                        No users.
                    </div>
                </c:if>
            </div>
        </div>
</parchisoca:admin>
