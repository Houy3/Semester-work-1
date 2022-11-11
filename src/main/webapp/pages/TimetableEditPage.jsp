<%@ page import="webapp.Utils.Options" %>
<%@ page import="webapp.servlets.pages.TimetableSharePage" %>
<%@ page import="webapp.Utils.Tags" %>
<%@ page import="webapp.servlets.pages.edits.TimetableEditPage" %>
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
        <p class="font-weight-bold text-center">Create timetable</p>
        <form name="addTimetable" method="post">
          <input name="FORM_NAME"
                 type="hidden"
                 value="ACTION_ADD">
          <div class="form-group my-3">
            <input name="NAME"
                   type="text"
                   class="form-control"
                   placeholder="Name"
                   value="${NAME}">
          </div>
          <button type="submit" class="btn btn-primary">Create</button>
        </form>
      </div>
      <div class="col-4">
        <p class="font-weight-bold text-center">Rename timetable</p>
        <form name="editTimetable" method="post">
          <input name="FORM_NAME"
                 type="hidden"
                 value="ACTION_CHANGE">
          <div class="my-3">
            <select name="ID"
                    class="form-control">
              <%out.print(Options.getDefault(request, "Select timetable:"));
                out.print(Options.getAllTimetablesOfWhichSessionUserIsOwner(request, request.getParameter(TimetableEditPage.Parameters.ID.name())));%>
            </select>
          </div>
          <div class="form-group my-3">
            <input name="NAME"
                   type="text"
                   class="form-control"
                   placeholder="New name"
                   value="${NAME}">
          </div>
          <button type="submit" class="btn btn-primary">Rename</button>
        </form>
      </div>
      <div class="col-4">
        <p class="font-weight-bold text-center">Delete timetable</p>
        <form name="deleteAccessRightsOnTable" method="post">
          <input name="FORM_NAME"
                 type="hidden"
                 value="ACTION_DELETE">
          <div class="my-3">
            <select name="ID"
                    class="form-control">
              <%out.print(Options.getDefault(request, "Select timetable:"));
                out.print(Options.getAllTimetablesOfWhichSessionUserIsOwner(request, request.getParameter(TimetableEditPage.Parameters.ID.name())));%>
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

