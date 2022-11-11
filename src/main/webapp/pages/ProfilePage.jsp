<%@ page import="webapp.Utils.Tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/pages/samples/_header.jsp" %>



<html>
<head>
    <title>Memorandum</title>
    <meta charset='UTF-8'>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.min.js" integrity="sha384-cVKIPhGWiC2Al4u+LWgxfKTRIcfu0JTxR+EQDz/bgldoEyl4H0zUF0QKbrJ0EcQF" crossorigin="anonymous"></script>
</head>


<body>
<div class="container">
    <div class="row">
        <div class="col-4 ">
            <div class="text-center">
                <img src="${pageContext.request.contextPath}/resources/images/Profile.png" class="rounded" alt="...">
            </div>
        </div>
        <div class="col-8">
            <form name="changeProfile" method="post">
                <div class="form-group my-3">
                    <label>Email:</label>
                    <input name="EMAIL"
                           type="email"
                           class="form-control"
                           aria-describedby="emailHelp"
                           placeholder="Email"
                           value="${EMAIL}">
                </div>
                <div class="form-group my-3">
                    <label>Name:</label>
                    <input name="NAME"
                           type="text"
                           class="form-control"
                           placeholder="Name"
                           value="${NAME}">
                </div>
                <div class="form-group my-3">
                    <label>Surname:</label>
                    <input name="SURNAME"
                           type="text"
                           class="form-control"
                           placeholder="Surname"
                           value="${SURNAME}">
                </div>
                <div class="form-group my-3">
                    <label>Nickname:</label>
                    <input name="NICKNAME"
                           type="text"
                           class="form-control"
                           placeholder="Nickname"
                           value="${NICKNAME}">
                </div>
                <div class="d-flex justify-content-between">
                    <div class="p-2">
                        <button name="TYPE" value="ACTION_CHANGE" type="submit" class="btn btn-primary">Save</button>
                    </div>
                    <div class="p-2">
                        <button name="TYPE" value="ACTION_DELETE" type="submit"  class="btn btn-danger">Delete</button>
                    </div>
                </div>
                <%out.print(Tags.errorMessage(request));%>
                <%out.print(Tags.successMessage(request));%>
            </form>

        </div>
    </div>
</div>

</body>
</html>

<%@include file="/pages/samples/_footer.jsp" %>

