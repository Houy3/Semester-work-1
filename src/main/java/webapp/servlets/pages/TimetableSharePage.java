package webapp.servlets.pages;

import exceptions.*;
import models.Timetable;
import models.UserTimetable;
import services.Service;
import webapp.Utils.Constants;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

import static webapp.Utils.Constants.*;
import static webapp.Utils.Paths.TIMETABLE_SHARE;

@WebServlet(TIMETABLE_SHARE)
public class TimetableSharePage extends Page {

    private final String uniqueFieldNameUserId = "userId";


    public enum Parameters {
        USER,
        TIMETABLE,
        ACCESS_RIGHTS
    }

    @Override
    public void init() {
        params = Arrays.stream(TimetableSharePage.Parameters.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getParameter(SUCCESS_MESSAGE_NAME.name()) != null) {
            req.setAttribute(SUCCESS_MESSAGE_NAME.name(), "Success");
        }
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String error = "";

        UserTimetable userTimetable = new UserTimetable();
        try {
            userTimetable.setUserId(Long.valueOf(req.getParameter(Parameters.USER.name())));
            userTimetable.setTimetableId(Long.valueOf(req.getParameter(Parameters.TIMETABLE.name())));
            userTimetable.setAccessRights(Timetable.AccessRights.valueOf(req.getParameter(Parameters.ACCESS_RIGHTS.name())));
        } catch (Exception ignored) {}

        Service service = (Service) getServletContext().getAttribute(TIMETABLES_SERVICE.name());
        try {
            switch (Constants.valueOf(req.getParameter(FORM_NAME.name()))) {
                case ACTION_ADD -> {
                    try {
                        service.add(userTimetable);
                    } catch (NotUniqueException e) {
                        error = "Access rights to this timetable for this user already exists. Try to change.";
                    }
                }
                case ACTION_CHANGE -> {
                    try {
                        service.change(userTimetable, uniqueFieldNameUserId);
                    } catch (NotFoundException e) {
                        error = "Access rights to this timetable for this user are not found. Nothing to change.";
                    } catch (NotUniqueException e) {
                        error = "How?";
                    }
                }
                case ACTION_DELETE -> {
                    try {
                        service.delete(userTimetable, uniqueFieldNameUserId);
                    } catch (NotFoundException e) {
                        error = "Access rights to this timetable for this user are not found. Nothing to delete.";
                    }
                }
                default -> {
                    error(resp, new ServletException("Неизвестная форма у " + HomePage.class.getName() + "."), 500);
                    return;
                }
            }
        } catch (NullException e) {
            switch (e.getMessage()) {
                case "usedId" -> error = "Select user.";
                case "timetableId" -> error = "Select timetable.";
                case "accessRights" -> error = "Select access rights.";
                default -> error = "Fill in all the fields. ";
            }
        } catch (DBException | ServiceException e) {
            error(resp, e, 500);
            return;
        }

        if (error.equals("")) {
            resp.sendRedirect(req.getContextPath() + TIMETABLE_SHARE + "?" + SUCCESS_MESSAGE_NAME +  "=Success");
            return;
        } else {
            req.setAttribute(ERROR_MESSAGE_NAME.name(), error);
        }

        super.doGet(req, resp);
    }
}
