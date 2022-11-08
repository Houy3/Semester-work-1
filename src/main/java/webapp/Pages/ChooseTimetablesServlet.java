package webapp.Pages;

import exceptions.DBException;
import exceptions.NullException;
import exceptions.ServiceException;
import models.Timetable;
import models.User;
import services.TimetablesService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static webapp.Constants.*;


@WebServlet(CHOOSE_TIMETABLES)
public class ChooseTimetablesServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute(USER);
        TimetablesService timetablesService = (TimetablesService) getServletContext().getAttribute(TIMETABLES_SERVICE);

        StringBuilder cookieValue = new StringBuilder();
        try {
            for (Long id : timetablesService.getTimetablesForUserId(user.getId())
                .keySet().stream().map(Timetable::getId).toList()) {

                String param = req.getParameter(id.toString());
                System.out.println(id + " " + param);
                if (param != null && param.equals("on")) {
                    cookieValue.append(id).append(SPLIT_CHAR);
                }
        }
        } catch (DBException | ServiceException | NullException e) {
            throw new RuntimeException(e);
        }

        Cookie cookie = new Cookie(CHOSEN_TIMETABLES, cookieValue.toString());
        cookie.setMaxAge(-1);
        System.out.println(cookie.getName() + " " + cookie.getValue());
        resp.addCookie(cookie);

        String back = req.getParameter("back");
        if (back != null) {
            resp.sendRedirect(req.getContextPath() + back);
        } else {
            throw new ServletException("Обратного запроса нет");
        }

    }
}
