<%@ page import="webapp.Utils.Forms" %>
<%@ page import="webapp.servlets.pages.NotesPage" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/pages/samples/_header.jsp" %>

<html>
<head>
    <title>Memorandum</title>
    <meta charset='UTF-8'>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/mine.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <link href="//maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
    <script defer src="${pageContext.request.contextPath}/resources/js/RefreshSelectedTimetables.js"></script>
</head>
<body>

<div class="mx-5 my-3">
    <div class="container">
        <div class="row">
            <div class="col-auto">
                <div class="border rounded px-2 py-3">
                    <%out.print(Forms.getTimetablesForSessionUser(request, NOTES));%>
                </div>
                <a href="${pageContext.request.contextPath}<%out.print(TIMETABLE_EDIT);%>" type="button" class="btn btn-primary align-content-center my-3">
                    Edit timetables
                </a>
            </div>
            <div class="col">
                <%out.print(Forms.getAllNotes(request));%>
            </div>
        </div>
    </div>
</div>


</body>
</html>

<%@include file="/pages/samples/_footer.jsp" %>
