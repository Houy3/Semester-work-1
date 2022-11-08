<%@ page import="models.Timetable" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page import="models.Timetable.AccessRights" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.List" %>
<%@ page import="java.rmi.ServerError" %>
<%@ page import="exceptions.ServiceException" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="webapp.Forms" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="/pages/samples/_header.jsp" %>


<!DOCTYPE html>
<html>
<head>
    <title>Memorandum</title>
    <meta charset='UTF-8'>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/bootstrap.css">
    <script defer src="${pageContext.request.contextPath}/resources/js/pop-UpAuth.js"></script>
</head>

<body>


<div class="mx-5 my-3">
    <div class="container">
        <div class="row">
            <div class="col-3 border">
                <div>
                    <form name="setTimetables" method="post" action="${pageContext.request.contextPath}<%out.print(CHOOSE_TIMETABLES);%>?back=<%out.print(TIMETABLE);%>">
                        <%
                            out.print(Forms.checkedTimetables(request));
                        %>
                    </form>
                </div>
            </div>
            <div class="col-9">Здесь будет расписание</div>
        </div>
    </div>
</div>


</body>
</html>


<%@include file="/pages/samples/_footer.jsp" %>
