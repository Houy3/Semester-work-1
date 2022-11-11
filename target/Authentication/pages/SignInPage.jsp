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

<div class="my-3">
    <div class="container">
        <div class="row">
            <form name="login" method="post">
                <div class="my-3">
                    <input name="EMAIL"
                           type="email"
                           class="form-control"
                           aria-describedby="emailHelp"
                           placeholder="Email"
                           value="${EMAIL}">
                </div>
                <div class="my-3">
                    <input name="PASSWORD"
                           type="password"
                           class="form-control"
                           placeholder="Password"
                           value="${PASSWORD}">
                </div>
                <button type="submit" class="btn btn-primary">Login</button>
                <%out.print(Tags.errorMessage(request));%>
            </form>
        </div>
    </div>
</div>

</body>
</html>


<%@include file="/pages/samples/_footer.jsp" %>


