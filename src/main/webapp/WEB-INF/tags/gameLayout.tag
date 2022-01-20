<%@ tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="parchisoca" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ attribute name="pageName" required="true" %>
<%@ attribute name="customScript" required="false" fragment="true"%>

<!doctype html>
<html>
<parchisoca:htmlHeader />

<body style="background-image: url('/resources/images/game_background.jpg'); background-size: 100% 100%;">
    <div class="container-fluid" style="margin-top: 5px;">
        <div class="container xd-container">
            <c:if test="${not empty message}">
                <div class="alert alert-${not empty messageType ? messageType : 'info'}" role="alert">
                    <c:out value="${message}"></c:out>
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
            </c:if>

            <jsp:doBody />

        </div>
    </div>
    <parchisoca:footer />
    <jsp:invoke fragment="customScript" />
</body>

</html>

<script>
    $('a').click(function (e) {
        if (e.ctrlKey) {
            return false;
        }
    });

</script>
