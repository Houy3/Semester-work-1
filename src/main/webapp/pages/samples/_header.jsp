
<%@ page import="static webapp.Constants.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE html>
<html>
<head>
    <title>Memorandum</title>
    <meta charset='UTF-8'>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/bootstrap.css">
    <script defer src="${pageContext.request.contextPath}/resources/js/pop-UpAuth.js"></script>
</head>

<body>

<div class="container">
    <header class="d-flex flex-wrap align-items-center justify-content-center justify-content-md-between py-3 mb-4 border-bottom">
        <a href="${pageContext.request.contextPath}" class="d-flex align-items-center col-md-3 mb-2 mb-md-0 text-dark text-decoration-none">
            <span class="fs-4">Memorandum</span>
        </a>

        <c:if test="${not empty user}">
        <ul class="nav col-12 col-md-auto mb-2 justify-content-center mb-md-0">
            <li><a href="${pageContext.request.contextPath}<%out.print(TIMETABLE);%>" class="nav-link px-2 link-dark">TimeTable</a></li>
            <li><a href="${pageContext.request.contextPath}/notes" class="nav-link px-2 link-dark">Notes</a></li>
            <li><a href="${pageContext.request.contextPath}/newNote" class="nav-link px-2 link-dark">New Note</a></li>
            <li><a href="${pageContext.request.contextPath}/newTask" class="nav-link px-2 link-dark">New Task</a></li>
            <li><a href="${pageContext.request.contextPath}/newEvent" class="nav-link px-2 link-dark">New Event</a></li>
        </ul>
        </c:if>

        <div class="col-md-3 text-end">
            <c:if test="${empty user}">
                <a href="${pageContext.request.contextPath}<%out.print(SIGN_IN);%>" type="button" class="btn btn-outline-primary me-2">
                    Sign in
                </a>
                <a href="${pageContext.request.contextPath}<%out.print(SIGN_UP);%>" type="button" class="btn btn-primary">
                    Sign up
                </a>
            </c:if>
            <c:if test="${not empty user}">
                <a href="${pageContext.request.contextPath}<%out.print(SIGN_OUT);%>" type="button" class="btn btn-outline-primary me-2">
                    Sign out
                </a>
            </c:if>
        </div>
    </header>
</div>
</body>
</html>