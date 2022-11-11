package webapp.Utils;

import exceptions.*;
import models.*;
import services.Inter.EventsService;
import services.Inter.NotesService;
import services.Inter.TasksService;
import services.Inter.TimetablesService;
import webapp.servlets.pages.HomePage;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static webapp.Utils.Constants.*;
import static webapp.Utils.Paths.*;
import static webapp.Utils.Utils.requestCheck;

public class Forms {

    public static String getTimetablesForSessionUser(HttpServletRequest request, String back) throws WebException {
        requestCheck(request);

        User user = (User) request.getSession().getAttribute(SESSION_USER.name());
        TimetablesService timetablesService = (TimetablesService) request.getServletContext().getAttribute(TIMETABLES_SERVICE.name());

        Map<Timetable, Timetable.AccessRights> timetables;
        try {
            timetables = timetablesService.getTimetablesForUserId(user.getId());
        } catch (DBException | ServiceException | NullException e) {
            e.printStackTrace();
            return "";
        }

        StringBuilder out = new StringBuilder();
        out.append("<form id=\"selectTimetables\" name=\"setTimetables\" method=\"post\" action=\"").append(request.getContextPath()).append(CHOOSE_TIMETABLES).append("?back=").append(back).append("\">");
        out.append("<p class=\"font-weight-bold text-center\">Choose timetables</p>");
        if (timetables.isEmpty()) {
            out.append("<p class=\" my-3 text-center font-weight-bold\">You don't have timetables</p>" +
                        "</form>");
            return out.toString();
        }

        String selectedInPast = user.getSelectedTimetables();

        List<Long> checked = new ArrayList<>();
        try {
            checked = Arrays.stream(selectedInPast.split(A.name())).map(Long::parseLong).toList();
        } catch (Exception ignored) {}


        for (Timetable.AccessRights accessRights : new Timetable.AccessRights[]{Timetable.AccessRights.OWNER, Timetable.AccessRights.WRITER, Timetable.AccessRights.READER}) {
            if (timetables.containsValue(accessRights)) {
                out.append("<p> You have access rights \"").append(accessRights).append("\" for:</p>");
                out.append("<ul>");
                for (Timetable timetable : timetables.keySet().stream()
                        .filter(t -> timetables.get(t) == accessRights)
                        .toList()) {
                    String checkedS = checked.contains(timetable.getId()) ? "checked" : "";
                    out.append("<li>");
                    out.append("<div class=\"form-check\">" +
                            "  <input name=\"" + timetable.getId() + "\" class=\"form-check-input\" type=\"checkbox\" " + checkedS + ">" +
                            "  <label class=\"form-check-label\">");
                    out.append(timetable.getName());
                    out.append("  </label>\n" +
                            "</div>");
                    out.append("</li>");
                }
                out.append("</ul>");
            }
        }
        //out.append("<button type=\"submit\" class=\"btn btn-primary\">Refresh</button>");
        out.append("</form>");
        return out.toString();

    }

    public static String getAllNotes(HttpServletRequest request) throws WebException {
        requestCheck(request);

        User user = (User) request.getSession().getAttribute(SESSION_USER.name());
        NotesService notesService = (NotesService) request.getServletContext().getAttribute(NOTES_SERVICE.name());

        List<Long> timetablesId = getSelectedTimetablesId(user.getSelectedTimetables());

        List<Note> notes;
        try {
            notes = notesService.getNotesByTimetablesId(timetablesId);
        } catch (DBException | ServiceException | NullException e) {
            e.printStackTrace();
            return "";
        }


        StringBuilder out = new StringBuilder();
        out.append("<div class=\"d-flex align-content-start flex-wrap\">");
        out.append("<a href=\"").append(request.getContextPath()).append(NOTE_EDIT).append("\" class=\"nav-link px-0 py-0 mx-0 my-0 link-dark\">")
                .append("<div class=\"border rounded mx-3 my-3 p-2\">\n")
                .append("<div class=\"note d-flex flex-wrap align-items-center justify-content-center\">")
                .append("<i class=\"fa fa-plus fa-5x\"></i>")
                .append("</div></div></a>");


        for (Note note : notes) {
            out.append("<a href=\"").append(request.getContextPath()).append(NOTE_EDIT).append("?ID=").append(note.getId()).append("\" class=\"nav-link px-0 py-0 mx-0 my-0 link-dark\">")
                    .append("<div class=\"border rounded mx-3 my-3 p-2\">\n")
                    .append("<div class=\"note\">")
                    .append("<div class=\"note-head border-bottom\">")
                    .append("<p class=\"h5 text-truncate\">").append(note.getName()).append("</p>")
                    .append("</div>")
                    .append("<div class=\"note-body\">")
                    .append("<p>").append(note.getBody()).append("</p>")
                    .append("</div></div></div></a>");
        }
        out.append("</div>");


        return out.toString();

    }

    public static String getAllTasksAndEvents(HttpServletRequest request) throws WebException {

        User user = (User) request.getSession().getAttribute(SESSION_USER.name());
        NotesService notesService = (NotesService) request.getServletContext().getAttribute(NOTES_SERVICE.name());
        TasksService tasksService = (TasksService) request.getServletContext().getAttribute(TASKS_SERVICE.name());
        EventsService eventsService = (EventsService) request.getServletContext().getAttribute(EVENTS_SERVICE.name());
        Date now = new Date();
        DateFormat htmlDateFormat = new SimpleDateFormat((String) request.getServletContext().getAttribute(HTML_DATE_FORMAT.name()));
        DateFormat htmlTimeFormat = new SimpleDateFormat((String) request.getServletContext().getAttribute(HTML_TIME_FORMAT.name()));

        Date day = null;
        try {
            day = htmlDateFormat.parse((String) request.getAttribute(HomePage.Parameters.DAY.name()));
        } catch (ParseException ignored) {}
        if (day == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0,0);
            day = calendar.getTime();
        }

        StringBuilder out = new StringBuilder();
        out.append("<div class=\"d-flex justify-content-center\">")
                .append("<div class=\"p-2 tasks border\">")
                .append("<div class=\"tasks-head border-bottom\">")
                .append("<form><label>").append("<input type=\"date\" name=\"DAY\" class=\"form-control\" value=\"")
                .append(htmlDateFormat.format(day))
                .append("\">")
                .append("</label>").append("<button type=\"submit\" class=\"btn btn-primary\">Reset</button>")
                .append("</form></div>");

        List<Long> timetablesId = getSelectedTimetablesId(user.getSelectedTimetables());

        List<Task> tasks;
        List<Event> events;
        try {
            tasks = tasksService.getTasksByTimetablesIdAndDate(timetablesId, day);
            events = eventsService.getEventsByTimetablesIdAndDate(timetablesId, day);
        } catch (DBException | ServiceException | NullException e) {
            e.printStackTrace();
            return "";
        }

        List<Container> deadlineOut = new ArrayList<>();
        List<Container> deadlineFar = new ArrayList<>();
        List<Container> baseList = new ArrayList<>();
        for (Task task : tasks) {
            Note note = new Note();
            note.setId(task.getNoteId());
            try {
                notesService.getByUniqueField(note, "id");
            } catch (NotFoundException | DBException | NullException | ServiceException ignored) {
            }
            Container container = new Container(note.getId(),
                    note.getName(), note.getBody(), null,
                    task.getDeadlineTime(), task.getDone(), Container.Type.TASK);
            Date nextDay = new Date(day.getTime() + 1000*60*60*24);
            if (task.getDeadlineTime().compareTo(now) <= 0) {
                deadlineOut.add(container);
            } else if (task.getDeadlineTime().compareTo(day) <= 0) {

            } else if (task.getDeadlineTime().compareTo(nextDay) > 0) {
                deadlineFar.add(container);
            } else {
                baseList.add(container);
            }
        }
        for (Event event : events) {
            Note note = new Note();
            note.setId(event.getNoteId());
            try {
                notesService.getByUniqueField(note, "id");
            } catch (NotFoundException | DBException | NullException | ServiceException ignored) {
            }
            for (Period period : event.getPeriods()) {
                Container container = new Container(note.getId(),
                        note.getName(), note.getBody(), period.getStartTime(),
                        period.getEndTime(), false, Container.Type.EVENT);
                baseList.add(container);
            }
        }


        if (!deadlineOut.isEmpty()) {
            out.append("<p class=\"lead\">Deadline is out:</p>");
            for (int i = 0; i < deadlineOut.size(); i++) {
                Container container = deadlineOut.get(i);
                out.append("<a").append(urlTo(request, container.id, Container.Type.TASK));
                boolean flag = i == deadlineOut.size() - 1;
                boolean flag2 = container.body != null && !container.body.trim().equals("");
                out.append("<p class=\"text-danger ").append(flag ? "border-bottom" : "").append(" text-truncate\"> ").append(container.name).append(flag2 ? " (" + container.body + ")" : "").append("</p></a>");
            }
        }
        if (!deadlineFar.isEmpty()) {
            out.append("<p class=\"lead\">Deadline is far:</p>");
            for (int i = 0; i < deadlineFar.size(); i++) {
                Container container = deadlineFar.get(i);
                out.append("<a").append(urlTo(request, container.id, Container.Type.TASK));
                boolean flag = i == deadlineFar.size() - 1;
                boolean flag2 = container.body != null && !container.body.trim().equals("");
                out.append("<p class=\" ").append(container.isDone ? "text-success " : "").append(flag ? " border-bottom " : "").append(" text-truncate\"> ").append(container.name).append(flag2 ? " (" + container.body + ")" : "").append("</p></a>");
            }
        }

        baseList.sort(Comparator.comparing(a -> a.endTime));
        if (!baseList.isEmpty()) {
            out.append("<p class=\"lead\">Schedule for today:</p>");
            for (Container container : baseList) {
                boolean flag2 = container.body != null && !container.body.trim().equals("");
                if (container.type.equals(Container.Type.TASK)) {
                    out.append("<a").append(urlTo(request, container.id, Container.Type.TASK));
                    out.append("<p class=\"").append(container.isDone ? "text-success " : "").append("text-truncate\">").append(htmlTimeFormat.format(container.endTime)).append(" ").append(container.name).append(flag2 ? " (" + container.body + ")" : "").append("</p></a>");
                }
                if (container.type.equals(Container.Type.EVENT)) {
                    out.append("<a").append(urlTo(request, container.id, Container.Type.EVENT));
                    out.append("<p class=\"text-truncate\">").append(htmlTimeFormat.format(container.startTime)).append("-").append(htmlTimeFormat.format(container.endTime)).append(" ").append(container.name).append(flag2 ? " (" + container.body + ")" : "").append("</p></a>");
                }
            }
        }
        out.append("</div></div>");

        return out.toString();
    }

    private record Container(Long id, String name, String body, Date startTime, Date endTime, boolean isDone,
                             Forms.Container.Type type)  {

            public enum Type {
                EVENT,
                TASK
            }
        }
    //----------------------------------------------------------------------------


    private static List<Long> getSelectedTimetablesId(String selectedTimetables) {
        List<Long> timetablesId = new ArrayList<>();
        try {
            timetablesId = Arrays.stream(selectedTimetables.split(A.name())).map(Long::parseLong).toList();
        } catch (Exception ignored) {}
        return timetablesId;
    }

    private static String urlTo(HttpServletRequest request, Long id, Container.Type type) {
        StringBuilder out = new StringBuilder();
        if (type.equals(Container.Type.TASK)) {
            out.append(" href=\"").append(request.getContextPath()).append(TASK_EDIT).append("?ID=").append(id).append("\" class=\"nav-link px-0 py-0 mx-0 my-0 link-dark\">");
        }
        if (type.equals(Container.Type.EVENT)) {
            out.append(" href=\"").append(request.getContextPath()).append(EVENT_EDIT).append("?ID=").append(id).append("\" class=\"nav-link px-0 py-0 mx-0 my-0 link-dark\">");
        }

        return out.toString();
    }
}
