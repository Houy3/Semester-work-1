<%@ page import="webapp.Utils.Options" %>
<%@ page import="webapp.servlets.pages.TimetableSharePage" %>
<%@ page import="webapp.Utils.Tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/pages/samples/_header.jsp" %>
<html>
<head>
    <title>Memorandum</title>
    <meta charset='UTF-8'>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
</head>
<body>

<div class="mx-5 my-3">
    <div class="container">
        <div class="row">
            <div class="col-4">
                <p class="font-weight-bold text-center">Add access rights on table</p>
                <form name="addAccessRightsOnTable" method="post">
                    <input name="FORM_NAME"
                           type="hidden"
                           value="ACTION_ADD">
                    <div class="my-3">
                        <select name="USER"
                                class="form-control">
                            <%out.print(Options.getDefault(request, "Select user:"));
                                out.print(Options.getAllUsersExceptSessionUser(request, request.getParameter(TimetableSharePage.Parameters.USER.name())));%>
                        </select>
                    </div>
                    <div class="my-3">
                        <select name="TIMETABLE"
                                class="form-control">
                            <%out.print(Options.getDefault(request, "Select timetable:"));
                            out.print(Options.getAllTimetablesOfWhichSessionUserIsOwner(request, request.getParameter(TimetableSharePage.Parameters.TIMETABLE.name())));%>
                        </select>
                    </div>
                    <div class="my-3">
                        <select name="ACCESS_RIGHTS"
                                class="form-control">
                            <%out.print(Options.getDefault(request, "Select access rights:"));
                                out.print(Options.getAllTimetableAccessRightsWithoutOwner(request.getParameter(TimetableSharePage.Parameters.ACCESS_RIGHTS.name())));%>
                        </select>
                    </div>
                    <button type="submit" class="btn btn-primary">Add</button>
                </form>
            </div>
            <div class="col-4">
                <p class="font-weight-bold text-center">Change access rights</p>
                <form name="editAccessRightsOnTable" method="post">
                    <input name="FORM_NAME"
                           type="hidden"
                           value="ACTION_CHANGE">
                    <div class="my-3">
                        <select name="USER"
                                class="form-control">
                            <%out.print(Options.getDefault(request, "Select user:"));
                                out.print(Options.getAllUsersExceptSessionUser(request, request.getParameter(TimetableSharePage.Parameters.USER.name())));%>
                        </select>
                    </div>
                    <div class="my-3">
                        <select name="TIMETABLE"
                                class="form-control">
                            <%out.print(Options.getDefault(request, "Select timetable:"));
                                out.print(Options.getAllTimetablesOfWhichSessionUserIsOwner(request, request.getParameter(TimetableSharePage.Parameters.TIMETABLE.name())));%>
                        </select>
                    </div>
                    <div class="my-3">
                        <select name="ACCESS_RIGHTS"
                                class="form-control">
                            <%out.print(Options.getDefault(request, "Select access rights:"));
                                out.print(Options.getAllTimetableAccessRightsWithoutOwner(request.getParameter(TimetableSharePage.Parameters.ACCESS_RIGHTS.name())));%>
                        </select>
                    </div>
                    <button type="submit" class="btn btn-primary">Change</button>
                </form>
            </div>
            <div class="col-4">
                <p class="font-weight-bold text-center">Remove permission</p>
                <form name="deleteAccessRightsOnTable" method="post">
                    <input name="FORM_NAME"
                           type="hidden"
                           value="ACTION_DELETE">
                    <div class="my-3">
                        <select name="USER"
                                class="form-control">
                            <%out.print(Options.getDefault(request, "Select user:"));
                                out.print(Options.getAllUsersExceptSessionUser(request, request.getParameter(TimetableSharePage.Parameters.USER.name())));%>
                        </select>
                    </div>
                    <div class="my-3">
                        <select name="TIMETABLE"
                                class="form-control">
                            <%out.print(Options.getDefault(request, "Select timetable:"));
                                out.print(Options.getAllTimetablesOfWhichSessionUserIsOwner(request, request.getParameter(TimetableSharePage.Parameters.TIMETABLE.name())));%>
                        </select>
                    </div>
                    <button type="submit" class="btn btn-primary">Delete</button>
                </form>
            </div>
        </div>
        <div class="row">
            <%out.print(Tags.errorMessage(request));%>
            <%out.print(Tags.successMessage(request));%>
        </div>
    </div>
</div>

</body>
</html>


<%@include file="/pages/samples/_footer.jsp" %>

