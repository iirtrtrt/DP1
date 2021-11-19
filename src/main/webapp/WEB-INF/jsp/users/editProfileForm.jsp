<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="editProfile">

 <form:form method="POST" modelAttribute="user" class="form-signin">
             <h2 class="form-signin-heading">Edit your profile</h2>
             <spring:bind path="username">
                 <div class="form-group ${status.error ? 'has-error' : ''}">
                     <form:input type="text" path="username" class="form-control" placeholder="Username"
                                 autofocus="true" readonly="true"></form:input>
                     <form:errors path="username"></form:errors>
                 </div>
             </spring:bind>

            <spring:bind path="firstname">
               <div class="form-group ${status.error ? 'has-error' : ''}">
                  <form:input type="text" path="firstname" class="form-control" placeholder="First Name"
                         autofocus="true"></form:input>
                  <form:errors path="firstname"></form:errors>
                </div>
            </spring:bind>

            <spring:bind path="lastname">
               <div class="form-group ${status.error ? 'has-error' : ''}">
                  <form:input type="text" path="lastname" class="form-control" placeholder="Last Name"
                         autofocus="true"></form:input>
                  <form:errors path="lastname"></form:errors>
                </div>
            </spring:bind>
             <spring:bind path="password">
                 <div class="form-group ${status.error ? 'has-error' : ''}">
                     <form:input type="password" path="password" class="form-control" placeholder="Password"></form:input>
                     <form:errors path="password"></form:errors>
                 </div>
             </spring:bind>

             <spring:bind path="passwordConfirm">
                 <div class="form-group ${status.error ? 'has-error' : ''}">
                     <form:input type="password" path="passwordConfirm" class="form-control"
                                 placeholder="Confirm your password"></form:input>
                     <form:errors path="passwordConfirm"></form:errors>
                 </div>
             </spring:bind>

             <button class="btn btn-lg btn-primary btn-block" type="submit">Submit</button>
         </form:form>
</petclinic:layout>
