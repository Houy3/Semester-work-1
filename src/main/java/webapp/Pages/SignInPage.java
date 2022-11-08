package webapp.Pages;

import exceptions.DBException;
import exceptions.NotFoundException;
import exceptions.NullException;
import exceptions.ServiceException;
import models.encryptors.PasswordEncryptor;
import models.forms.UserSignUpForm;
import models.User;
import services.UsersService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static webapp.Constants.*;

@WebServlet(SIGN_IN)
public class SignInPage extends Page {

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


        try {
            UsersService usersService = (UsersService) getServletContext().getAttribute(USERS_SERVICE);
            PasswordEncryptor passwordEncryptor = (PasswordEncryptor) getServletContext().getAttribute(PASSWORD_ENCRYPTOR);
            UserSignUpForm userSignUpForm = new UserSignUpForm(
                    req.getParameter("email"),
                    passwordEncryptor.encrypt(req.getParameter("password")));
            User user = usersService.singIn(userSignUpForm);
            req.getSession().setAttribute(USER, user);
            resp.sendRedirect(req.getContextPath() + TIMETABLE);
            return;
        } catch (NotFoundException e) {
            error = "No user found. ";
        } catch (IllegalArgumentException | DBException | NullException | ServiceException e) {
            error(resp,e,500);
        }

        req.setAttribute("error", error);
        openPage(req,resp);
    }
}
