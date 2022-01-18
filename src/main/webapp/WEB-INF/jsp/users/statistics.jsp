<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="parchisoca" tagdir="/WEB-INF/tags" %>

<parchisoca:layout pageName="statistics">
    <div class="row">
        <div class="col-md-6 p-3 m-3 border border-secondary w-100 rounded">
            <div class="row">
                <div class="col">
                    <h2>My Statistics</h2>
                </div>
            </div>
            <div>
                <table class="table table-hover table-striped table-condensed text-center">
                    <thead>
                        <td>Number of joined games</td>
                        <td>Number of wins</td>
                        <td>Highscore</td>
                    </thead>
                    <tbody>
                           <tr>
                                    <td>
                                        <c:out value="${statistic.numberOfPlayedGames}" />
                                        <c:if test="${empty statistic.numberOfPlayedGames}">
                                            None
                                        </c:if>
                                    </td>
                                    <td>
                                        <c:out value="${statistic.numberOfWins}" />
                                        <c:if test="${empty statistic.numberOfWins}">
                                            None
                                        </c:if>
                                    </td>
                                    <td>
                                        <c:out value="${statistic.highscore}" />
                                        <c:if test="${empty statistic.highscore}">
                                            None
                                        </c:if>
                                    </td>
                                </tr>
                    </tbody>
                </table>

                <c:if test="${empty users}">
                    <div>
                        No users.
                    </div>
                </c:if>
            </div>
        </div>
</parchisoca:layout>
