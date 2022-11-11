package webapp.servlets.pages.edits;

import exceptions.*;
import models.*;
import services.Inter.EventsService;
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
import static webapp.Utils.Paths.EVENT_EDIT;
import static webapp.Utils.Paths.HOME;
import static webapp.Utils.Utils.notEmpty;
import static webapp.servlets.pages.edits.EventEditPage.Parameters.*;

@WebServlet(EVENT_EDIT)
public class EventEditPage extends Page {

    private final int countOfPeriods = 5;
    private final String uniqueFieldNameId = "id";
    private final String uniqueFieldNameNoteId = "noteId";

    public enum Parameters {
        ID,
        TIMETABLE,
        NAME,
        BODY,
        LAST_CHANGE_TIME,
        PLACE,
        LINK,
        DAY,
        START_TIME,
        END_TIME,
        TYPE
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        utf8(req, resp);
        if (req.getParameter(EventEditPage.Parameters.ID.name()) == null) {
            req.setAttribute(ERROR_MESSAGE_NAME.name(), "Unknown event. ");
            super.doGet(req, resp);
            return;
        }

        User user = (User) req.getSession().getAttribute(SESSION_USER.name());
        NotesService notesService = (NotesService) getServletContext().getAttribute(NOTES_SERVICE.name());
        EventsService eventsService = (EventsService) getServletContext().getAttribute(EVENTS_SERVICE.name());


        Note note = new Note();
        Event event = new Event();
        try {
            note.setId(Long.valueOf(req.getParameter(TaskEditPage.Parameters.ID.name())));
            event.setNoteId(note.getId());
        } catch (Exception e) {
            req.setAttribute(ERROR_MESSAGE_NAME.name(), "Invalid request format. ");
            super.doGet(req, resp);
            return;
        }
        boolean exist = true;
        try {
            notesService.getByUniqueField(note, uniqueFieldNameId);
            try {
                eventsService.getByUniqueField(event, uniqueFieldNameNoteId);
            } catch (Exception e) {
                exist = false;
            }
            req.setAttribute(IS_FOUND.name(), "true");
        } catch (NotFoundException e) {
            req.setAttribute(ERROR_MESSAGE_NAME.name(), "Unknown event. ");
            super.doGet(req, resp);
            return;
        } catch (DBException | NullException | ServiceException e) {
            error(resp, e, 500);
            return;
        }

        Timetable.AccessRights accessRights;
        try {
            accessRights = notesService.getAccessRightsOnNote(event.getNoteId(), user.getId());
        } catch (DBException | ServiceException | NullException e) {
            error(resp, e, 500);
            return;
        } catch (NotFoundException e) {
            req.setAttribute(ERROR_MESSAGE_NAME.name(), "Insufficient access rights to view this event. ");
            super.doGet(req, resp);
            return;
        }

        if (accessRights.equals(Timetable.AccessRights.READER)) {
            if (!exist) {
                req.setAttribute(IS_FOUND.name(), null);
                req.setAttribute(ERROR_MESSAGE_NAME.name(), "Unknown event. ");
                super.doGet(req, resp);
                return;
            }
            req.setAttribute(READ_ONLY.name(), "readonly");
        }

        req.setAttribute(ID.name(), note.getId());
        req.setAttribute(NAME.name(), note.getName());
        req.setAttribute(TIMETABLE.name(), note.getTimetableId());
        req.setAttribute(BODY.name(), note.getBody());
        req.setAttribute(LINK.name(), event.getLink());
        req.setAttribute(PLACE.name(), event.getPlace());



        DateFormat htmlDateFormat = new SimpleDateFormat((String) getServletContext().getAttribute(HTML_DATE_FORMAT.name()));
        DateFormat htmlTimeFormat = new SimpleDateFormat((String) getServletContext().getAttribute(HTML_TIME_FORMAT.name()));
        for (int i = 1; i <= countOfPeriods; i++) {
            try {
                Period period = event.getPeriods().get(i-1);
                System.out.println(period);
                req.setAttribute(DAY.name()+i, htmlDateFormat.format(period.getStartTime()));
                req.setAttribute(START_TIME.name()+i, htmlTimeFormat.format(period.getStartTime()));
                req.setAttribute(END_TIME.name()+i, htmlTimeFormat.format(period.getEndTime()));
            } catch (Exception ignored) {
            }
        }

        DateFormat dateFormat = new SimpleDateFormat((String) getServletContext().getAttribute(DATE_FORMAT.name()));
        req.setAttribute(LAST_CHANGE_TIME.name(), dateFormat.format(note.getLastChangeTime()));

        openPage(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        utf8(req,resp);

        NotesService notesService = (NotesService) getServletContext().getAttribute(NOTES_SERVICE.name());
        EventsService eventsService = (EventsService) getServletContext().getAttribute(EVENTS_SERVICE.name());

        if (req.getParameter(TYPE.name()).equals(ACTION_DELETE.name())) {
            Event event = new Event();
            try {
                event.setNoteId(Long.valueOf(req.getParameter(TaskEditPage.Parameters.ID.name())));
            } catch (Exception e) {
                error(resp, e, 500);
                return;
            }
            try {
                eventsService.delete(event, uniqueFieldNameNoteId);
            } catch (NotFoundException ignored) {
            } catch ( DBException | NullException | ServiceException e) {
                error(resp, e, 500);
                return;
            }
        }

        String error = "";
        if (req.getParameter(TYPE.name()).equals(ACTION_CHANGE.name())) {
            Note note = new Note();
            Event event = new Event();
            try {
                note.setId(Long.valueOf(req.getParameter(ID.name())));
                event.setNoteId(note.getId());
            } catch (NumberFormatException ignored) {
            }
            setNoteFields(req, note);
            try {
                setEventFields(req, event);
                notesService.change(note, uniqueFieldNameId);
                eventsService.change(event, uniqueFieldNameNoteId);
            } catch (NotFoundException e) {
                try {
                    eventsService.add(event);
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
        req.setAttribute(LINK.name(), req.getParameter(LINK.name()));
        req.setAttribute(PLACE.name(), req.getParameter(PLACE.name()));

        for (int i = 1; i <= countOfPeriods; i++) {
            try {
                req.setAttribute(DAY.name()+i, req.getParameter(DAY.name()+i));
                req.setAttribute(START_TIME.name()+i, req.getParameter(START_TIME.name()+i));
                req.setAttribute(END_TIME.name()+i, req.getParameter(END_TIME.name()+i));
            } catch (Exception ignored) {
            }
        }

        req.setAttribute(IS_FOUND.name(), "true");
        req.setAttribute(ERROR_MESSAGE_NAME.name(), error);
        openPage(req, resp);
    }

    private static String nullError(NullException e) {
        String error;
        switch (e.getMessage()) {
            case "name" -> error = "Fill in the name. ";
            case "body" -> error = "Write something in note.";
            case "timetableId" -> error = "Select timetable.";
            default -> error = "Something go wrong. " + e.getMessage();
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

    private void setEventFields(HttpServletRequest req, Event event) {
        event.setLink(notEmpty(req.getParameter(LINK.name())));
        event.setPlace(notEmpty(req.getParameter(PLACE.name())));

        boolean flag = true;
        DateFormat htmlDateTimeFormat = new SimpleDateFormat( getServletContext().getAttribute(HTML_DATE_FORMAT.name()) +  " " + getServletContext().getAttribute(HTML_TIME_FORMAT.name()));
        for (int i = 1; i <= countOfPeriods; i++) {
            Period period = new Period();
            period.setEventId(event.getNoteId());
            try {
                Date startTime = htmlDateTimeFormat.parse(req.getParameter(DAY.name() +i) + " " + req.getParameter(START_TIME.name() +i));
                System.out.println("--");
                Date endTime = htmlDateTimeFormat.parse(req.getParameter(DAY.name() +i) + " " + req.getParameter(END_TIME.name()+i));
                System.out.println("--");
                period.setStartTime(startTime);
                period.setEndTime(endTime);
                if (period.getStartTime().compareTo(period.getEndTime()) >= 0) {
                    throw new IllegalArgumentException("The end time can't be earlier then start time. ");
                }
                event.addPeriod(period);
                flag = false;
            } catch (Exception ignored) {
            }
        }
        if (flag) {
            throw new IllegalArgumentException("Fill in at least one period. ");
        }
    }


}
