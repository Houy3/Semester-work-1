<%@ page import="webapp.Utils.Options" %>
<%@ page import="webapp.servlets.pages.edits.NoteEditPage" %>
<%@ page import="webapp.Utils.Tags" %>
<%@include file="/pages/samples/_header.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Memorandum</title>
    <meta charset='UTF-8'>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
</head>
<body>

<c:if test="${not empty IS_FOUND}">
<div class="container">
    <div class="row">
        <form name="signUp" method="post">
            <div class="d-flex justify-content-between">
                <div class="p-2">

                    <div class="form-group mb-3 mr-5">
                        <label>Name:
                            <input name="NAME"
                                   type="text"
                                   class="form-control"
                                   placeholder="Name"
                                   value="${NAME}"
                                ${READ_ONLY}>
                        </label>
                        <label>Timetable:
                            <c:if test="${empty READ_ONLY}">
                                <select name="TIMETABLE"
                                        class="form-control">
                                    <%out.print(Options.getDefault(request, "Select timetable:"));
                                        out.print(Options.getAllTimetablesOfWhichSessionUserIsOwnerOrWriter(request, String.valueOf(request.getAttribute(NoteEditPage.Parameters.TIMETABLE.name()))));%>
                                </select>
                            </c:if>
                            <c:if test="${not empty READ_ONLY}">
                                <input name="TIMETABLE"
                                       type="text"
                                       class="form-control"
                                       placeholder="Timetable"
                                       value="${TIMETABLE}"
                                    ${READ_ONLY}>
                            </c:if>
                        </label>
                    </div>
                </div>
                <div class="p-2">
                    <c:if test="${not empty ID}">
                        <a href="${pageContext.request.contextPath}<%out.print(TASK_EDIT + "?ID=" + request.getAttribute(NoteEditPage.Parameters.ID.name()));%>" type="button" class="btn btn-primary">
                            Task
                        </a>
                        <a href="${pageContext.request.contextPath}<%out.print(EVENT_EDIT + "?ID=" + request.getAttribute(NoteEditPage.Parameters.ID.name()));%>" type="button" class="btn btn-primary">
                            Event
                        </a>
                    </c:if>
                </div>
            </div>
            <div class="form-group">
                <textarea name="BODY" class="form-control" rows="8" ${READ_ONLY}>${BODY}</textarea>
            </div>
            <c:if test="${empty READ_ONLY}">
                <div class="d-flex justify-content-between">
                    <div class="p-2">
                        <button name="TYPE" value="ACTION_CHANGE" type="submit" class="btn btn-primary">Save</button>
                    </div>
                    <div class="p-2">
                        <button name="TYPE" value="ACTION_DELETE" type="submit"  class="btn btn-danger">Delete</button>
                    </div>
                </div>
            </c:if>
            <input name="LAST_CHANGE_TIME"
                   type="hidden"
                   value="${LAST_CHANGE_TIME}">
            <c:if test="${not empty LAST_CHANGE_TIME}">
                <p class="text-end">Last modified: ${LAST_CHANGE_TIME}</p>
            </c:if>
            <%out.print(Tags.errorMessage(request));%>
            <%out.print(Tags.successMessage(request));%>
        </form>
    </div>
</div>
</c:if>

<c:if test="${empty IS_FOUND}">
    <%out.print(Tags.errorMessage(request));%>
</c:if>

</body>
</html>

<%@include file="/pages/samples/_footer.jsp" %>
