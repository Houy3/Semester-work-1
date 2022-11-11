package webapp.servlets.pages.edits;

import exceptions.*;
import models.Timetable;
import models.User;
import models.UserTimetable;
import services.Inter.TimetablesService;
import webapp.Utils.Constants;
import webapp.Utils.Utils;
import webapp.servlets.pages.HomePage;
import webapp.servlets.pages.Page;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

import static webapp.Utils.Constants.*;
import static webapp.Utils.Paths.TIMETABLE_EDIT;

@WebServlet(TIMETABLE_EDIT)
public class TimetableEditPage extends Page {

    private final String uniqueFieldNameId = "id";

    public enum Parameters {
        ID,
        NAME
    }

    @Override
    public void init() {
        params = Arrays.stream(TimetableEditPage.Parameters.values())
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
        utf8(req,resp);

        Timetable timetable = new Timetable();
        try {
            timetable.setId(Long.valueOf(req.getParameter(Parameters.ID.name())));
        } catch (Exception ignored) {}
        try {
            timetable.setName(Utils.notEmpty(req.getParameter(Parameters.NAME.name())));
        } catch (Exception ignored) {}

        TimetablesService timetablesService = (TimetablesService) getServletContext().getAttribute(TIMETABLES_SERVICE.name());
        try {
            switch (Constants.valueOf(req.getParameter(FORM_NAME.name()))) {
                case ACTION_ADD -> {
                    try {
                        timetablesService.add(timetable);
                        System.out.println(timetable.getName());
                        UserTimetable userTimetable = new UserTimetable();
                        userTimetable.setTimetableId(timetable.getId());
                        userTimetable.setUserId(((User)req.getSession().getAttribute(SESSION_USER.name())).getId());
                        userTimetable.setAccessRights(Timetable.AccessRights.OWNER);
                        timetablesService.add(userTimetable);
                    } catch (NotUniqueException e) {
                        error = "You have a table with that " + e.getMessage() + ". ";
                    }
                }
                case ACTION_CHANGE -> {
                    try {
                        timetablesService.change(timetable, uniqueFieldNameId);
                    } catch (NotFoundException e) {
                        error = "Timetable not found. ";
                    } catch (NotUniqueException e) {
                        error = "Timetable with this " + e.getMessage() + " already exists. ";
                    }
                }
                case ACTION_DELETE -> {
                    try {
                        timetablesService.delete(timetable, uniqueFieldNameId);
                    } catch (NotFoundException e) {
                        error = "Timetable not found. ";
                    }
                }
                default -> {
                    error(resp, new ServletException("Неизвестная форма у " + HomePage.class.getName() + "."), 500);
                    return;
                }
            }
        } catch (NullException e) {
            switch (e.getMessage()) {
                case "id" -> error = "Select timetable.";
                case "name" -> error = "Fill name.";
                default -> error = "Fill in all the fields. ";
            }
        } catch (DBException | ServiceException e) {
            error(resp, e, 500);
            return;
        }

        if (error.equals("")) {
            resp.sendRedirect(req.getContextPath() + TIMETABLE_EDIT + "?" + SUCCESS_MESSAGE_NAME +  "=Success");
            return;
        } else {
            req.setAttribute(ERROR_MESSAGE_NAME.name(), error);
        }

        super.doGet(req, resp);

    }

}
