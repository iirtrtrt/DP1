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
                        <td>Number of rolled Dices</td>
                        <td>Win Rate</td>
                    </thead>
                    <tbody>
                           <tr>
                                    <td>
                                        <c:out value="${mystatistic.numberOfPlayedGames}" />
                                        <c:if test="${empty mystatistic.numberOfPlayedGames}">
                                            None
                                        </c:if>
                                    </td>
                                    <td>
                                        <c:out value="${mystatistic.numberOfWins}" />
                                        <c:if test="${empty mystatistic.numberOfWins}">
                                            None
                                        </c:if>
                                    </td>
                                    <td>
                                        <c:out value="${mystatistic.numberOfrolledDices}" />
                                        <c:if test="${empty mystatistic.numberOfrolledDices}">
                                            None
                                        </c:if>
                                    </td>
                                    <td>
                                        <c:out value="${mystatistic.winRate}" />
                                        <c:if test="${empty mystatistic.winRate}">
                                            None
                                        </c:if>
                                        </td>
                                </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="col-md-6 p-3 m-3 border border-secondary w-100 rounded">
                    <h2 class="lead">Compare yourself to others</h2>
                    <hr>
                    <table class="table table-hover table-striped table-condensed">

                        <thead>
                            <td>Username</td>
                            <td>Number of joined games</td>
                            <td>Number of wins</td>
                            <td>Number of rolled Dices</td>
                            <td>Win Rate</td>
                        </thead>
                        <tbody>
                            <c:forEach items="${statistics}" var="statistic">
                                <tr>
                                    <td>
                                        <c:out value="${statistic.username}" />
                                        <c:if test="${empty statistic.username}">
                                            None
                                        </c:if>
                                    </td>
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
                                        <c:out value="${statistic.numberOfrolledDices}" />
                                        <c:if test="${empty statistic.numberOfrolledDices}">
                                            None
                                        </c:if>
                                    </td>
                                    <td>
                                        <c:out value="${statistic.winRate}" />
                                        <c:if test="${empty statistic.winRate}">
                                            None
                                        </c:if>
                                        </td>
                                </tr>
                            </c:forEach>
                        </tbody>

                    </table>
</parchisoca:layout>
