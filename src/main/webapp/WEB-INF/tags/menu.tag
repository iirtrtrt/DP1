<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="parchisoca" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!--  >%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%-->
<%@ attribute name="name" required="true" rtexprvalue="true"
	description="Name of the active menu: home, owners, vets or error"%>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark navbar-default" role="navigation">
    <div class="container-fluid">
        <spring:url value="/resources/images/US_logo.png" var="US_logo" />
        <img src="${US_logo}" alt="US_logo" style="height : 50px; margin: 5px 30px 5px 10px;">

        <div id="navbarCollapse" class="collapse navbar-collapse h4 text-decoration-none">
            <ul class="nav navbar-nav">
                <li class="nav-item">
                    <parchisoca:menuItem active="${name eq 'home'}" url="/" title="home page">
                        <a href="<c:url value="/" />" class="nav-link text-capitalize">Home</a>
                    </parchisoca:menuItem>
                </li>
                <li class="nav-item">
                    <parchisoca:menuItem active="${name eq 'game'}" url="/game/create" title="Create Game">
                        <a href="<c:url value="/game/create" />"  class="nav-link text-capitalize">Create game</a>
                    </parchisoca:menuItem>
                </li>
                <li class="nav-item">
                    <parchisoca:menuItem active="${name eq 'game'}" url="/game/join" title="Join Game">
                        <a href="<c:url value="/game/join" />" class="nav-link text-capitalize">Join Game</a>
                    </parchisoca:menuItem>
                </li>
                <li class="nav-item">
                    <parchisoca:menuItem active="${name eq 'highscore'}" url="/highscore" title="My score">
                        <a href= "<c:url value="/highscore" />" class="nav-link text-capitalize"> My score</a>
                    </parchisoca:menuItem>
                </li>
                <li class="nav-item">
                    <parchisoca:menuItem active="${name eq 'invite'}" url="/invite" title="Invite friends">
                        <span class="glyphicon glyphicon-send" aria-hidden="true"></span>
                        <a href="<c:url value="/invite" />" class="nav-link text-capitalize">Invite</a>
                    </parchisoca:menuItem>
                </li>
                <li class="nav-item">
                    <parchisoca:menuItem active="${name eq 'error'}" url="/oups"
                        title="trigger a RuntimeException to see how it is handled">
                        <a href="<c:url value="/oups" />" class="nav-link text-capitalize">Error</a>
                    </parchisoca:menuItem>
                </li>
            </ul>

            <ul class="nav navbar-nav ms-auto">
                <sec:authorize access="!isAuthenticated()">
                    <li class="nav-item"><a class="nav-link text-capitalize" href="<c:url value="/login" />">Login</a></li>
                    <li class="nav-item"><a class="nav-link text-capitalize" href="<c:url value="/register" />">Register</a></li>
                </sec:authorize>

                <sec:authorize access="isAuthenticated()">
                    <li class="dropdown nav-item">
                        <a href="#" class="nav-link dropdown-toggle" data-toggle="dropdown" data-bs-toggle="dropdown">
                            <strong>
                                <sec:authentication property="name" />
                            </strong>
                        </a>

                        <div class="dropdown-menu dropdown-menu-end bg-dark p-1">
                                <a href="<c:url value="/editProfile" />" class="btn btn-danger btn-block w-100 text-capitalize">Edit Profile</a>
                            <div class="dropdown-divider bg-light"></div>
                                <a href="<c:url value="/logout" />" class="btn btn-primary btn-block w-100 text-capitalize">Logout</a>
                        </div>
                    </li>
                </sec:authorize>
            </ul>
        </div>
    </div>
</nav>
