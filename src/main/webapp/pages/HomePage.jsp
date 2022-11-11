<%@ page import="webapp.Utils.Forms" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="/pages/samples/_header.jsp" %>


<!DOCTYPE html>
<html>
<head>
    <title>Memorandum</title>
    <meta charset='UTF-8'>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/mine.css">
    <script defer src="${pageContext.request.contextPath}/resources/js/RefreshSelectedTimetables.js"></script>
</head>

<body>


<div class="mx-5 my-3">
    <div class="container">
        <div class="row">
            <div class="col-auto">
                <div class="border rounded px-2 py-3">
                    <%out.print(Forms.getTimetablesForSessionUser(request, HOME));%>
                </div>
                <a href="${pageContext.request.contextPath}<%out.print(TIMETABLE_EDIT);%>" type="button" class="btn btn-primary align-content-center my-3">
                    Edit timetables
                </a>
            </div>
            <div class="col">
                <%out.print(Forms.getAllTasksAndEvents(request));%>
            </div>
        </div>
    </div>
</div>


</body>
</html>


<%@include file="/pages/samples/_footer.jsp" %>
