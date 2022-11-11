package webapp.servlets.pages.edits;

import exceptions.*;
import models.Note;
import models.Task;
import models.Timetable;
import models.User;
import services.Inter.NotesService;
import services.Inter.TasksService;
import webapp.servlets.pages.Page;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static webapp.Utils.Constants.*;
import static webapp.Utils.Paths.*;
import static webapp.Utils.Utils.notEmpty;
import static webapp.servlets.pages.edits.TaskEditPage.Parameters.*;

@WebServlet(TASK_EDIT)
public class TaskEditPage extends Page {

    private final String uniqueFieldNameId = "id";
    private final String uniqueFieldNameNoteId = "noteId";

    public enum Parameters {
        ID,
        TIMETABLE,
        NAME,
        BODY,
        LAST_CHANGE_TIME,
        START_NOTIFICATION_DATE,
        DEADLINE_DATE,
        DEADLINE_TIME,

        IS_DONE,
        TYPE
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        utf8(req, resp);
        if (req.getParameter(TaskEditPage.Parameters.ID.name()) == null) {
            req.setAttribute(ERROR_MESSAGE_NAME.name(), "Unknown task. ");
            super.doGet(req, resp);
            return;
        }

        User user = (User) req.getSession().getAttribute(SESSION_USER.name());
        NotesService notesService = (NotesService) getServletContext().getAttribute(NOTES_SERVICE.name());
        TasksService tasksService = (TasksService) getServletContext().getAttribute(TASKS_SERVICE.name());


        Note note = new Note();
        Task task = new Task();
        try {
            note.setId(Long.valueOf(req.getParameter(TaskEditPage.Parameters.ID.name())));
            task.setNoteId(note.getId());
        } catch (Exception e) {
            req.setAttribute(ERROR_MESSAGE_NAME.name(), "Invalid request format. ");
            super.doGet(req, resp);
            return;
        }
        boolean exist = true;
        try {
            notesService.getByUniqueField(note, uniqueFieldNameId);
            try {
                tasksService.getByUniqueField(task, uniqueFieldNameNoteId);
            } catch (Exception e) {
                exist = false;
            }
            req.setAttribute(IS_FOUND.name(), "true");
        } catch (NotFoundException e) {
            req.setAttribute(ERROR_MESSAGE_NAME.name(), "Unknown task. ");
            super.doGet(req, resp);
            return;
        } catch (DBException | NullException | ServiceException e) {
            error(resp, e, 500);
            return;
        }

        Timetable.AccessRights accessRights;
        try {
            accessRights = notesService.getAccessRightsOnNote(task.getNoteId(), user.getId());
        } catch (DBException | ServiceException | NullException e) {
            error(resp, e, 500);
            return;
        } catch (NotFoundException e) {
            req.setAttribute(ERROR_MESSAGE_NAME.name(), "Insufficient access rights to view this task. ");
            super.doGet(req, resp);
            return;
        }

        if (accessRights.equals(Timetable.AccessRights.READER)) {
            if (!exist) {
                req.setAttribute(IS_FOUND.name(), null);
                req.setAttribute(ERROR_MESSAGE_NAME.name(), "Unknown task. ");
                super.doGet(req, resp);
                return;
            }
            req.setAttribute(READ_ONLY.name(), "readonly");
        //    req.setAttribute(GREY.name(), "grey");
        }

        req.setAttribute(ID.name(), note.getId());
        req.setAttribute(NAME.name(), note.getName());
        req.setAttribute(TIMETABLE.name(), note.getTimetableId());
        req.setAttribute(BODY.name(), note.getBody());
        req.setAttribute(IS_DONE.name(), task.getDone() != null && task.getDone() ? "checked" : "");

        DateFormat htmlDateFormat = new SimpleDateFormat((String) getServletContext().getAttribute(HTML_DATE_FORMAT.name()));
        DateFormat htmlTimeFormat = new SimpleDateFormat((String) getServletContext().getAttribute(HTML_TIME_FORMAT.name()));
        try {
            req.setAttribute(START_NOTIFICATION_DATE.name(), htmlDateFormat.format(task.getNotificationStartDate()));
            req.setAttribute(DEADLINE_DATE.name(), htmlDateFormat.format(task.getDeadlineTime()));
            req.setAttribute(DEADLINE_TIME.name(), htmlTimeFormat.format(task.getDeadlineTime()));
        } catch (Exception ignored) {}

        DateFormat dateFormat = new SimpleDateFormat((String) getServletContext().getAttribute(DATE_FORMAT.name()));
        req.setAttribute(LAST_CHANGE_TIME.name(), dateFormat.format(note.getLastChangeTime()));

        openPage(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        utf8(req,resp);

        NotesService notesService = (NotesService) getServletContext().getAttribute(NOTES_SERVICE.name());
        TasksService tasksService = (TasksService) getServletContext().getAttribute(TASKS_SERVICE.name());

        if (req.getParameter(TYPE.name()).equals(ACTION_DELETE.name())) {
            Task task = new Task();
            try {
                task.setNoteId(Long.valueOf(req.getParameter(TaskEditPage.Parameters.ID.name())));
            } catch (Exception e) {
                error(resp, e, 500);
                return;
            }
            try {
                tasksService.delete(task, uniqueFieldNameNoteId);
            } catch (NotFoundException ignored) {
            } catch ( DBException | NullException | ServiceException e) {
                error(resp, e, 500);
                return;
            }
        }

        String error = "";
        if (req.getParameter(TYPE.name()).equals(ACTION_CHANGE.name())) {
            Note note = new Note();
            Task task = new Task();
            try {
                note.setId(Long.valueOf(req.getParameter(ID.name())));
                task.setNoteId(note.getId());
            } catch (NumberFormatException ignored) {
            }
            setNoteFields(req, note);
            try {
                setTaskFields(req, task);
                notesService.change(note, uniqueFieldNameId);
                tasksService.change(task, uniqueFieldNameNoteId);
            } catch (NotFoundException e) {
                try {
                    tasksService.add(task);
                } catch (NullException | NotUniqueException | DBException | ServiceException ex) {
                    error(resp, e, 500);
                    return;
                }
            } catch (IllegalArgumentException e) {
                error = e.getMessage();
            }
            catch (NullException e) {
                error = nullError(e);
            } catch ( DBException | NotUniqueException | ServiceException e) {
                error(resp, e, 500);
                return;
            }
        }

        if (error.equals("")) {
            resp.sendRedirect(req.getContextPath()+HOME);
            return;
        }

        req.setAttribute(ID.name(), req.getParameter(ID.name()));
        req.setAttribute(NAME.name(), req.getParameter(NAME.name()));
        req.setAttribute(TIMETABLE.name(), req.getParameter(TIMETABLE.name()));
        req.setAttribute(BODY.name(), req.getParameter(BODY.name()));
        req.setAttribute(LAST_CHANGE_TIME.name(), req.getParameter(LAST_CHANGE_TIME.name()));
        req.setAttribute(START_NOTIFICATION_DATE.name(), req.getParameter(START_NOTIFICATION_DATE.name()));
        req.setAttribute(DEADLINE_DATE.name(), req.getParameter(DEADLINE_DATE.name()));
        req.setAttribute(DEADLINE_TIME.name(), req.getParameter(DEADLINE_TIME.name()));


        req.setAttribute(IS_FOUND.name(), "true");
        req.setAttribute(ERROR_MESSAGE_NAME.name(), error);
        openPage(req, resp);
    }

    private static String nullError(NullException e) {
        String error;
        switch (e.getMessage()) {
            case "name" -> error = "Fill in the name. ";
            case "body" -> error = "Write something in note.";
            case "timetableId" -> error = "Select timetable";
            case "notificationStartDate" -> error = "Fill in notification start date.";
            case "deadlineTime" -> error = "Fill in deadline time.";
            default -> error = "Something go wrong";
        }
        return error;
    }

    private void setNoteFields(HttpServletRequest req, Note note) {
        note.setName(notEmpty(req.getParameter(NAME.name())));
        try {
            note.setTimetableId(Long.valueOf(req.getParameter(TIMETABLE.name())));
        } catch (NumberFormatException ignored) {}
        note.setBody(req.getParameter(BODY.name()));
    }

    private void setTaskFields(HttpServletRequest req, Task task) {
        DateFormat htmlDateFormat = new SimpleDateFormat((String) getServletContext().getAttribute(HTML_DATE_FORMAT.name()));
        DateFormat htmlDateTimeFormat = new SimpleDateFormat( getServletContext().getAttribute(HTML_DATE_FORMAT.name()) +  " " + getServletContext().getAttribute(HTML_TIME_FORMAT.name()));
        try {
            task.setNotificationStartDate(htmlDateFormat.parse(req.getParameter(START_NOTIFICATION_DATE.name())));
            Date deadlineTime = htmlDateTimeFormat.parse(req.getParameter(DEADLINE_DATE.name()) + " " + req.getParameter(DEADLINE_TIME.name()));
            task.setDeadlineTime(deadlineTime);
            if (task.getNotificationStartDate().compareTo(task.getDeadlineTime()) >= 0) {
                throw new IllegalArgumentException("The deadline time can't be earlier then notification start time. ");
            }
        } catch (Exception ignored) {
        }
        task.setDone(req.getParameter(IS_DONE.name()) != null && req.getParameter(IS_DONE.name()).equals("on"));
    }
}
