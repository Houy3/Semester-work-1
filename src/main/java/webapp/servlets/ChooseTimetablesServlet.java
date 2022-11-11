package webapp.servlets;

import exceptions.*;
import models.User;
import services.Inter.UsersService;
import webapp.servlets.pages.Page;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static webapp.Utils.Constants.*;
import static webapp.Utils.Paths.CHOOSE_TIMETABLES;


@WebServlet(CHOOSE_TIMETABLES)
public class ChooseTimetablesServlet extends Page {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect(req.getContextPath());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = (User) req.getSession().getAttribute(SESSION_USER.name());
        UsersService usersService = (UsersService) getServletContext().getAttribute(USERS_SERVICE.name());

        try {
            String selectedTimetables = req.getParameter("selected");
            user.setSelectedTimetables(selectedTimetables);
            usersService.change(user, "id");
        } catch (DBException | ServiceException | NullException | NotFoundException | NotUniqueException e) {
            error(resp, e, 500);
        }

    }
}
