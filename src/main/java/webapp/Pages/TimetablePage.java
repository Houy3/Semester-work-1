package webapp.Pages;

import exceptions.DBException;
import exceptions.NullException;
import exceptions.ServiceException;
import models.Timetable;
import models.User;
import services.TimetablesService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static webapp.Constants.TIMETABLE;
import static webapp.Constants.USER;

@WebServlet(TIMETABLE)
public class TimetablePage extends Page {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        utf8(req, resp);

        User user = (User) req.getSession().getAttribute(USER);

        TimetablesService timetablesService = (TimetablesService) getServletContext().getAttribute("timetablesService");
        Map<Timetable, Timetable.AccessRights> timetables;
        try {
            timetables = timetablesService.getTimetablesForUserId(user.getId());
        } catch (DBException | ServiceException | NullException e) {
            throw new RuntimeException(e);
        }
        req.setAttribute("timetables", timetables);


        openPage(req, resp);
    }
}
