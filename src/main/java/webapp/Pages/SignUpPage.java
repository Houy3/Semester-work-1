package webapp.Pages;

import exceptions.DBException;
import exceptions.NotUniqueException;
import exceptions.NullException;
import exceptions.ServiceException;
import models.User;
import models.encryptors.PasswordEncryptor;
import models.validators.EmailValidator;
import models.validators.PasswordValidator;
import services.UsersService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static webapp.Constants.*;

@WebServlet(SIGN_UP)
public class SignUpPage extends Page {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        utf8(req,resp);
        saveOldValuesOrMakeNull(req, new String[]{"name", "surname", "nickname","email", "password", "password2", "error"});

        openPage(req,resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        utf8(req,resp);
        saveOldValuesOrMakeNull(req, new String[]{"name", "surname", "nickname","email", "password", "password2", "error"});

        String error = "";

        try {
            EmailValidator emailValidator = (EmailValidator) getServletContext().getAttribute("emailValidator");
            emailValidator.check(req.getParameter("email"));
            PasswordValidator passwordValidator = (PasswordValidator) getServletContext().getAttribute("passwordValidator");
            passwordValidator.check(req.getParameter("password"));
        } catch (IllegalArgumentException e) {
            error = error + e.getMessage();
            req.setAttribute("error", error);
            openPage(req,resp);
        }

        if (!req.getParameter("password").equals(req.getParameter("password2"))) {
            error = "Пароли не совпадают";
            req.setAttribute("error", error);
            openPage(req,resp);
        }

        PasswordEncryptor passwordEncryptor = (PasswordEncryptor) getServletContext().getAttribute(PASSWORD_ENCRYPTOR);
        User user = new User();
        user.setName(req.getParameter("name").equals("") ? null : req.getParameter("name"));
        user.setSurname(req.getParameter("surname").equals("") ? null : req.getParameter("surname"));
        user.setNickname(req.getParameter("nickname").equals("") ? null : req.getParameter("nickname"));
        user.setEmail(req.getParameter("email"));
        user.setPasswordHash(passwordEncryptor.encrypt(req.getParameter("password")));
        user.setAccessRights(User.AccessRights.REGULAR);

        try {
            UsersService usersService = (UsersService) getServletContext().getAttribute("usersService");
            usersService.add(user);
            req.getSession().setAttribute(USER, user);
            resp.sendRedirect(req.getContextPath() + TIMETABLE);
            return;
        } catch (NotUniqueException e) {
            error = e.getMessage() + " is already taken. ";
        } catch (NullException e) {
            error = "Please, fill " + e.getMessage() + ". ";
        } catch (IllegalArgumentException | DBException | ServiceException e) {
            error(resp, e, 500);
        }


        req.setAttribute("error", error);
        openPage(req,resp);
    }
}