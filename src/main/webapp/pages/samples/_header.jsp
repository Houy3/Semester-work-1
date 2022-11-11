<%@ page import="static webapp.Utils.Paths.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE html>
<html>
<head>
    <title>Memorandum</title>
    <meta charset='UTF-8'>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
</head>

<body>

<div class="container">
    <header class="d-flex flex-wrap align-items-center justify-content-center justify-content-lg-between py-3 mb-4 border-bottom">
        <a href="${pageContext.request.contextPath}" class="d-flex align-items-center col-md-3 mb-2 mb-md-0 text-dark text-decoration-none">
            <span class="fs-4">Memorandum</span>
        </a>

        <c:if test="${not empty SESSION_USER}">
        <ul class="nav col-12 col-md-auto mb-2 justify-content-center mb-md-0">
            <li><a href="${pageContext.request.contextPath}<%out.print(HOME);%>" class="nav-link px-2 link-dark">Home</a></li>
            <li><a href="${pageContext.request.contextPath}<%out.print(NOTES);%>" class="nav-link px-2 link-dark">Notes</a></li>
            <li><a href="${pageContext.request.contextPath}<%out.print(TIMETABLE_SHARE);%>" class="nav-link px-2 link-dark">Timetable Share</a></li>
            <li><a href="${pageContext.request.contextPath}<%out.print(PROFILE);%>" class="nav-link px-2 link-dark">Profile</a></li>
        </ul>
        </c:if>

        <div class="col-md-3 text-end">
            <c:if test="${empty SESSION_USER}">
                <a href="${pageContext.request.contextPath}<%out.print(SIGN_IN);%>" type="button" class="btn btn-outline-primary me-2">
                    Sign in
                </a>
                <a href="${pageContext.request.contextPath}<%out.print(SIGN_UP);%>" type="button" class="btn btn-primary">
                    Sign up
                </a>
            </c:if>
            <c:if test="${not empty SESSION_USER}">
                <a onclick="handle2()" href="${pageContext.request.contextPath}<%out.print(SIGN_OUT);%>" type="button" class="btn btn-primary">
                    Sign out
                </a>
            </c:if>
        </div>
    </header>
</div>
</body>
</html>