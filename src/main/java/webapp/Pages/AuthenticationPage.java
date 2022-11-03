package webapp.Pages;

import exceptions.DBException;
import exceptions.NotFoundException;
import exceptions.NullException;
import services.Service;
import models.User;

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
            user.setPasswordHash(req.getParameter("password"));
        } catch (IllegalArgumentException e) {
            error = error + e.getMessage() + "\n";
        }

        if (error.equals("")) {
            try {
                Service service = (Service) getServletContext().getAttribute("service");
                service.userSingIn(req.getParameter("email"), req.getParameter("password"));
                req.getSession().setAttribute("user", user);
                resp.sendRedirect(req.getContextPath() + "/account");
                return;
            } catch (NotFoundException e) {
                error = "Пользователь с таким логином и паролем не найден";
            } catch (IllegalArgumentException | DBException | NullException e) {
                error(resp,e,500);
            }
        }

        req.setAttribute("error", error);
        openPage(req,resp);
    }
}
