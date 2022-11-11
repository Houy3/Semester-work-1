package webapp.servlets.pages;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static webapp.Utils.Paths.HOME;

@WebServlet(HOME)
public class HomePage extends Page {

    public enum Parameters {
        DAY
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        utf8(req, resp);

//        User user = (User) req.getSession().getAttribute(SESSION_USER.name());
//
//        TimetablesService timetablesService = (TimetablesService) getServletContext().getAttribute(TIMETABLES_SERVICE.name());
//        Map<Timetable, Timetable.AccessRights> timetables;
//        try {
//            timetables = timetablesService.getTimetablesForUserId(user.getId());
//        } catch (DBException | ServiceException | NullException e) {
//            error(resp, e, 500);
//        }

        req.setAttribute(Parameters.DAY.name(), "11.11.2022");

        super.doGet(req, resp);
    }
}
