package webapp.Pages;

import Exceptions.DBException;
import Exceptions.NotFoundException;
import services.UsersService;
import models.User;
import services.Impl.UsersServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/authentication")
public class AuthenticationPage extends Page {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        utf8(req,resp);
        saveOldValuesOrMakeNull(req, new String[]{"email", "password", "error"});

        openPage(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        utf8(req,resp);
        saveOldValuesOrMakeNull(req, new String[]{"email", "password", "error"});

        String error = "";

        User user = new User();
        try {
            user.setEmail(req.getParameter("email"));
            user.setPassword(req.getParameter("password"));
        } catch (IllegalArgumentException e) {
            error = error + e.getMessage() + "\n";
        }

        if (error.equals("")) {
            try {
                UsersService userService = (UsersServiceImpl) getServletContext().getAttribute("userService");
                userService.singIn(user);
                req.getSession().setAttribute("user", user);
                resp.sendRedirect(req.getContextPath() + "/account");
                return;
            } catch (NotFoundException e) {
                error = "Пользователь с таким логином и паролем не найден";
            } catch (IllegalArgumentException | DBException e) {
                error(resp,e,500);
            }
        }

        req.setAttribute("error", error);
        openPage(req,resp);
    }
}
