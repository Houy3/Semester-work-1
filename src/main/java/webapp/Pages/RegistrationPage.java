package webapp.Pages;

import Exceptions.DBException;
import Exceptions.NotUniqueException;
import services.UsersService;
import models.User;
import services.Impl.UsersServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/registration")
public class RegistrationPage extends Page {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        utf8(req,resp);
        saveOldValuesOrMakeNull(req, new String[]{"email", "password", "password2", "error"});

        openPage(req,resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        utf8(req,resp);
        saveOldValuesOrMakeNull(req, new String[]{"email", "password", "password2", "error"});

        String error = "";

        User user = new User();
        try {
            user.setEmail(req.getParameter("email"));
            user.setPassword(req.getParameter("password"));
        } catch (IllegalArgumentException e) {
            error = error + e.getMessage() + "\n";
        }

        if (error.equals("")) {
            if (!req.getParameter("password").equals(req.getParameter("password2"))) {
                error = "Пароли не совпадают";
            }
        }

        if (error.equals("")) {
            try {
                UsersService userService = (UsersServiceImpl) getServletContext().getAttribute("userService");
                userService.add(user);
                req.getSession().setAttribute("user", user);
                resp.sendRedirect(req.getContextPath() + "/account");
                return;
            } catch (NotUniqueException e) {
                error = "Логин занят";
            } catch (IllegalArgumentException | DBException e) {
                error(resp, e, 500);
            }
        }

        req.setAttribute("error", error);
        openPage(req,resp);
    }
}