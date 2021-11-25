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
                        <span class="nav-link text-capitalize">Home</span>
                    </parchisoca:menuItem>
                </li>
                <li class="nav-item">
                    <parchisoca:menuItem active="${name eq 'game'}" url="/game/create" title="Create Game">
                        <span class="nav-link text-capitalize">Create game</span>
                    </parchisoca:menuItem>
                </li>
                <li class="nav-item">
                    <parchisoca:menuItem active="${name eq 'game'}" url="/game/join" title="Join Game">
                        <span class="nav-link text-capitalize">Join Game</span>
                    </parchisoca:menuItem>
                </li>
                <li class="nav-item">
                    <parchisoca:menuItem active="${name eq 'highscore'}" url="/highscore" title="My score">
                        <span class="nav-link text-capitalize">My score</span>
                    </parchisoca:menuItem>
                </li>
                <li class="nav-item">
                    <parchisoca:menuItem active="${name eq 'error'}" url="/oups"
                        title="trigger a RuntimeException to see how it is handled">
                        <span class="nav-link text-capitalize">Error</span>
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
