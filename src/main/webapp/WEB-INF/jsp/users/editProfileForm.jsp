<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="parchisoca" tagdir="/WEB-INF/tags" %>

<parchisoca:layout pageName="editProfile">
    <div class="col-md-6 p-3 m-3 border border-secondary w-100 rounded">
 <form:form method="POST" modelAttribute="user" class="form-signin">
             <h2 class="lead">Edit your profile</h2>
             <hr>
             <div class="row">
                <div class="col-3 text-end">
                    <h3 class="text-capitalize">Username :</h3>
                </div>
                <div class="col">
                    <spring:bind path="username">
                 <div class="form-group ${status.error ? 'has-error' : ''}">
                     <form:input type="text" path="username" class="form-control" placeholder="Username"
                                 autofocus="true" readonly="true"></form:input>
                     <form:errors path="username"></form:errors>
                 </div>
             </spring:bind>
                </div>
            </div>

            <div class="row">
                <div class="col-3 text-end">
                    <h3 class="text-capitalize">First name :</h3>
                </div>
                <div class="col">
                    <spring:bind path="firstname">
               <div class="form-group ${status.error ? 'has-error' : ''}">
                  <form:input type="text" path="firstname" class="form-control" placeholder="First Name"
                         autofocus="true"></form:input>
                  <form:errors path="firstname"></form:errors>
                </div>
            </spring:bind>
                </div>
            </div>

            <div class="row">
                <div class="col-3 text-end">
                    <h3 class="text-capitalize">Last name :</h3>
                </div>
                <div class="col">
                    <spring:bind path="lastname">
               <div class="form-group ${status.error ? 'has-error' : ''}">
                  <form:input type="text" path="lastname" class="form-control" placeholder="Last Name"
                         autofocus="true"></form:input>
                  <form:errors path="lastname"></form:errors>
                </div>
            </spring:bind>
                </div>
            </div>

            <div class="row">
                <div class="col-3 text-end">
                    <h3 class="text-capitalize">Password :</h3>
                </div>
                <div class="col">
                    <spring:bind path="password">
                        <div class="form-group ${status.error ? 'has-error' : ''}">
                            <form:input type="password" path="password" class="form-control" placeholder="Password"></form:input>
                            <form:errors path="password"></form:errors>
                        </div>
                    </spring:bind>
                </div>
            </div>

            <div class="row">
                <div class="col-3 text-end">
                    <h3 class="text-capitalize">Password confoirm :</h3>
                </div>
                <div class="col">
                    <spring:bind path="passwordConfirm">
                 <div class="form-group ${status.error ? 'has-error' : ''}">
                     <form:input type="password" path="passwordConfirm" class="form-control"
                                 placeholder="Password Confoirm"></form:input>
                     <form:errors path="passwordConfirm"></form:errors>
                 </div>
             </spring:bind>
                </div>
            </div>
            <br>
                <div class="row">
                    <button class="btn btn-lg btn-primary btn-block" type="submit">Submit</button>
                </div>
         </form:form>
        </div>
</parchisoca:layout>
