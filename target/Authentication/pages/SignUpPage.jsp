<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/pages/samples/_header.jsp" %>

<!DOCTYPE html>
<html>
    <head>
        <title>Memorandum</title>
        <meta charset='UTF-8'>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/bootstrap.css">
    </head>

<body>

<div class="mx-5 my-3">
<form name="signUp" method="post">
    <div class="form-group my-3">
        <input name="email"
               type="email"
               class="form-control"
               aria-describedby="emailHelp"
               placeholder="Email"
               value="${email}">
    </div>
    <div class="form-group my-3">
        <input name="name"
               type="text"
               class="form-control"
               placeholder="Name"
               value="${name}">
    </div>
    <div class="form-group my-3">
        <input name="surname"
               type="surname"
               class="form-control"
               placeholder="Surname"
               value="${surname}">
    </div>
    <div class="form-group my-3">
        <input name="nickname"
               type="nickname"
               class="form-control"
               placeholder="Nickname"
               value="${nickname}">
    </div>
    <div class="form-group my-3">
        <input name="password"
               type="password"
               class="form-control"
               placeholder="Password"
               value="${password}">
    </div>
    <div class="form-group my-3">
        <input name="password2"
               type="password"
               class="form-control"
               placeholder="Repeat password"
               value="${password2}">
    </div>

    <button type="submit" class="btn btn-primary">Login</button>
    <p class="text-danger">${error}</p>
</form>
</div>

</body>
</html>


<%@include file="/pages/samples/_footer.jsp" %>



