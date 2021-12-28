<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="parchisoca" tagdir="/WEB-INF/tags" %>

<parchisoca:admin pageName="adminUsers">
    <div class="row">
        <div class="col-md-6 p-3 m-3 border border-secondary w-100 rounded">
            <div class="row">
                <div class="col">
                    <h2>User List</h2>
                </div>
                <div class="col d-flex">
                    <a href='<c:url value="/admin/register" />'
                        class="btn btn-success btn-block text-capitalize ms-auto">Register a new User</a>
                </div>
            </div>
            <div>
                <table class="table table-hover table-striped table-condensed text-center">
                    <thead>
                        <td>Name</td>
                        <td>Email</td>
                        <td>UserRole</td>
                        <td>CreateTime</td>
                        <td>UserDetails</td>
                        <td>UserDelete</td>
                    </thead>
                    <tbody>
                        <c:forEach items="${users}" var="user">
                            <form:form method="POST" modelAttribute="colorWrapper">
                                <tr>
                                    <td>
                                        <c:out value="${user.username}" />
                                    </td>
                                    <td>
                                        <c:out value="${user.email}" />
                                        <c:if test="${empty user.email}">
                                            None
                                        </c:if>
                                    </td>
                                    <td>
                                        <c:out value="${user.role}" />
                                        <c:if test="${empty user.role}">
                                            None
                                        </c:if>
                                    </td>
                                    <td>
                                        <c:out value="${user.createTime}" />
                                        <c:if test="${empty user.createTime}">
                                            None
                                        </c:if>
                                    </td>
                                    <td>
                                        <c:if test="${user.role == 'PLAYER'}">
                                            <a href='<c:url value="/admin/users/details/${user.username}" />'
                                                class="btn btn-md btn-secondary">Details</a>
                                        </c:if>
                                    </td>
                                    <td>
                                        <c:if test="${user.role == 'PLAYER'}">
                                            <a href='<c:url value="/admin/users/delete/${user.username}" />'
                                                class="btn btn-md btn-secondary" id="del">Delete</a>
                                        </c:if>
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

<script type="text/javascript">
    $(document).on("click", "#del", function () {
        return confirm("Would you really like to delete it?");
    });

</script>
