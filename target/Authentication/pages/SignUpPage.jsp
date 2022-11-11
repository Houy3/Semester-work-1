<%@ page import="webapp.Utils.Tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/pages/samples/_header.jsp" %>

<!DOCTYPE html>
<html>
    <head>
        <title>Memorandum</title>
        <meta charset='UTF-8'>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    </head>

<body>

<div class="container">
    <div class="row">
        <form name="signUp" method="post">
            <div class="form-group my-3">
                <input name="EMAIL"
                       type="email"
                       class="form-control"
                       aria-describedby="emailHelp"
                       placeholder="Email"
                       value="${EMAIL}">
            </div>
            <div class="form-group my-3">
                <input name="NAME"
                       type="text"
                       class="form-control"
                       placeholder="Name"
                       value="${NAME}">
            </div>
            <div class="form-group my-3">
                <input name="SURNAME"
                       type="text"
                       class="form-control"
                       placeholder="Surname"
                       value="${SURNAME}">
            </div>
            <div class="form-group my-3">
                <input name="NICKNAME"
                       type="text"
                       class="form-control"
                       placeholder="Nickname"
                       value="${NICKNAME}">
            </div>
            <div class="form-group my-3">
                <input name="PASSWORD"
                       type="password"
                       class="form-control"
                       placeholder="Password"
                       value="${PASSWORD}">
            </div>
            <div class="form-group my-3">
                <input name="PASSWORD2"
                       type="password"
                       class="form-control"
                       placeholder="Repeat password"
                       value="${PASSWORD2}">
            </div>

            <button type="submit" class="btn btn-primary">Register</button>
            <%out.print(Tags.errorMessage(request));%>
        </form>
    </div>
</div>


</body>
</html>


<%@include file="/pages/samples/_footer.jsp" %>



