package webapp.servlets.pages.edits;

import exceptions.*;
import models.Note;
import models.Timetable;
import models.User;
import services.Inter.NotesService;
import webapp.servlets.pages.Page;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static webapp.Utils.Constants.*;
import static webapp.Utils.Paths.*;
import static webapp.Utils.Utils.notEmpty;
import static webapp.servlets.pages.edits.NoteEditPage.Parameters.*;

@WebServlet(NOTE_EDIT)
public class NoteEditPage extends Page {

    private final String uniqueFieldNameId = "id";

    public enum Parameters {
        ID,
        TIMETABLE,
        NAME,
        BODY,
        LAST_CHANGE_TIME,
        TYPE
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        utf8(req, resp);
        if (req.getParameter(Parameters.ID.name()) == null) {
            req.setAttribute(IS_FOUND.name(), "true");
            super.doGet(req, resp);
            return;
        }

        User user = (User) req.getSession().getAttribute(SESSION_USER.name());
        NotesService notesService = (NotesService) getServletContext().getAttribute(NOTES_SERVICE.name());


        Note note = new Note();
        try {
            note.setId(Long.valueOf(req.getParameter(Parameters.ID.name())));
        } catch (Exception e) {
            req.setAttribute(ERROR_MESSAGE_NAME.name(), "Invalid request format. ");
            super.doGet(req, resp);
            return;
        }
        try {
            notesService.getByUniqueField(note, uniqueFieldNameId);
            req.setAttribute(IS_FOUND.name(), "true");
        } catch (NotFoundException e) {
            req.setAttribute(ERROR_MESSAGE_NAME.name(), "Unknown note. ");
            super.doGet(req, resp);
            return;
        } catch (DBException | NullException | ServiceException e) {
            error(resp, e, 500);
            return;
        }

        Timetable.AccessRights accessRights;
        try {
            accessRights = notesService.getAccessRightsOnNote(note.getId(), user.getId());
        } catch (DBException | ServiceException | NullException e) {
            error(resp, e, 500);
            return;
        } catch (NotFoundException e) {
            req.setAttribute(ERROR_MESSAGE_NAME.name(), "Insufficient access rights to view this note. ");
            super.doGet(req, resp);
            return;
        }

        if (accessRights.equals(Timetable.AccessRights.READER)) {
            req.setAttribute(READ_ONLY.name(), "readonly");
        }

        req.setAttribute(ID.name(), note.getId());
        req.setAttribute(NAME.name(), note.getName());
        req.setAttribute(TIMETABLE.name(), note.getTimetableId());
        req.setAttribute(BODY.name(), note.getBody());
        DateFormat dateFormat = new SimpleDateFormat((String) getServletContext().getAttribute(DATE_FORMAT.name()));
        req.setAttribute(LAST_CHANGE_TIME.name(), dateFormat.format(note.getLastChangeTime()));

        openPage(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        utf8(req,resp);

        NotesService notesService = (NotesService) getServletContext().getAttribute(NOTES_SERVICE.name());

        if (req.getParameter(TYPE.name()).equals(ACTION_DELETE.name())) {
            if (req.getParameter(ID.name()) != null) {
                Note note = new Note();
                try {
                    note.setId(Long.valueOf(req.getParameter(Parameters.ID.name())));
                } catch (Exception e) {
                    error(resp, e, 500);
                    return;
                }
                try {
                    notesService.delete(note, uniqueFieldNameId);
                } catch (NotFoundException ignored) {
                } catch ( DBException | NullException | ServiceException e) {
                    error(resp, e, 500);
                    return;
                }
            }
        }

        String error = "";
        if (req.getParameter(TYPE.name()).equals(ACTION_CHANGE.name())) {
            if (req.getParameter(ID.name()) != null) {
                Note note = new Note();
                try {
                    note.setId(Long.valueOf(req.getParameter(ID.name())));
                } catch (NumberFormatException ignored) {
                }
                setNoteFields(req, note);
                try {
                    notesService.change(note, uniqueFieldNameId);
                } catch (NullException e) {
                    error = nullError(e);
                } catch (NotFoundException | DBException | NotUniqueException | ServiceException e) {
                    error(resp, e, 500);
                    return;
                }
            } else {
                Note note = new Note();
                setNoteFields(req, note);
                try {
                    notesService.add(note);
                } catch (NullException e) {
                    error = nullError(e);
                } catch (DBException | NotUniqueException | ServiceException e) {
                    error(resp, e, 500);
                    return;
                }
            }
        }

        if (error.equals("")) {
            resp.sendRedirect(req.getContextPath()+NOTES);
            return;
        }

        req.setAttribute(ID.name(), req.getParameter(ID.name()));
        req.setAttribute(NAME.name(), req.getParameter(NAME.name()));
        req.setAttribute(TIMETABLE.name(), req.getParameter(TIMETABLE.name()));
        req.setAttribute(BODY.name(), req.getParameter(BODY.name()));
        req.setAttribute(LAST_CHANGE_TIME.name(), req.getParameter(LAST_CHANGE_TIME.name()));

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
}
