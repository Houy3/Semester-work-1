package webapp.servlets.pages.user;

import exceptions.DBException;
import exceptions.NotUniqueException;
import exceptions.NullException;
import exceptions.ServiceException;
import models.User;
import models.encryptors.PasswordEncryptor;
import models.validators.EmailValidator;
import models.validators.NicknameValidator;
import models.validators.PasswordValidator;
import services.Inter.UsersService;
import webapp.Utils.Paths;
import webapp.servlets.pages.Page;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

import static webapp.Utils.Constants.*;
import static webapp.servlets.pages.user.SignUpPage.Parameters.*;
import static webapp.Utils.Paths.*;
import static webapp.Utils.Utils.notEmpty;

@WebServlet(SIGN_UP)
public class SignUpPage extends Page {

     enum Parameters {
        NAME,
        SURNAME,
        NICKNAME,
        EMAIL,
        PASSWORD,
        PASSWORD2
    }


    @Override
    public void init() {
        params = Arrays.stream(Parameters.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String error = "";
        utf8(req, resp);

        try {
            EmailValidator emailValidator = (EmailValidator) getServletContext().getAttribute(EMAIL_VALIDATOR.name());
            emailValidator.check(req.getParameter(EMAIL.name()));

            PasswordValidator passwordValidator = (PasswordValidator) getServletContext().getAttribute(PASSWORD_VALIDATOR.name());
            passwordValidator.check(req.getParameter(PASSWORD.name()));

            NicknameValidator nicknameValidator = (NicknameValidator) getServletContext().getAttribute(NICKNAME_VALIDATOR.name());
            nicknameValidator.check(NICKNAME.name());
        } catch (IllegalArgumentException e) {
            req.setAttribute(ERROR_MESSAGE_NAME.name(), e.getMessage());
            super.doGet(req, resp);
        }

        if (!req.getParameter(PASSWORD.name()).equals(req.getParameter(PASSWORD2.name()))) {
            req.setAttribute(ERROR_MESSAGE_NAME.name(), "Passwords do not match. ");
            super.doGet(req, resp);
        }

        PasswordEncryptor passwordEncryptor = (PasswordEncryptor) getServletContext().getAttribute(PASSWORD_ENCRYPTOR.name());

        User user = new User();
        user.setName(notEmpty(req.getParameter(NAME.name())));
        user.setSurname(notEmpty(req.getParameter(SURNAME.name())));
        user.setNickname(notEmpty(req.getParameter(NICKNAME.name())));
        user.setEmail(req.getParameter(EMAIL.name()));
        user.setPasswordHash(passwordEncryptor.encrypt(req.getParameter(PASSWORD.name())));
        user.setAccessRights(User.AccessRights.REGULAR);

        try {
            UsersService usersService = (UsersService) getServletContext().getAttribute(USERS_SERVICE.name());
            usersService.add(user);
            req.getSession().setAttribute(SESSION_USER.name(), user);
            resp.sendRedirect(req.getContextPath() + Paths.HOME);
            return;
        } catch (NotUniqueException e) {
            error = "User with this " + e.getMessage() + " already exists. ";
        } catch (NullException e) {
            error = "Fill " + e.getMessage() + ". ";
        } catch (IllegalArgumentException | DBException | ServiceException e) {
            error(resp, e, 500);
            return;
        }

        req.setAttribute(ERROR_MESSAGE_NAME.name(), error);
        super.doGet(req, resp);
    }
}