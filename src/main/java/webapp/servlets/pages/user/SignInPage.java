package webapp.servlets.pages.user;

import exceptions.DBException;
import exceptions.NotFoundException;
import exceptions.NullException;
import exceptions.ServiceException;
import models.User;
import models.encryptors.PasswordEncryptor;
import models.forms.UserSignUpForm;
import models.validators.EmailValidator;
import services.Inter.UsersService;
import webapp.servlets.pages.Page;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

import static webapp.servlets.pages.user.SignInPage.Parameters.EMAIL;
import static webapp.servlets.pages.user.SignInPage.Parameters.PASSWORD;
import static webapp.Utils.Constants.*;
import static webapp.Utils.Paths.SIGN_IN;
import static webapp.Utils.Paths.HOME;

@WebServlet(SIGN_IN)
public class SignInPage extends Page {

     enum Parameters {
        EMAIL,
        PASSWORD
    }

    @Override
    public void init() {
        params = Arrays.stream(Parameters.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setAttribute(ERROR_MESSAGE_NAME.name(), "");
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String error = "";
        utf8(req, resp);

        try {
            EmailValidator emailValidator = (EmailValidator) getServletContext().getAttribute(EMAIL_VALIDATOR.name());
            emailValidator.check(req.getParameter(EMAIL.name()));
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
            req.setAttribute(ERROR_MESSAGE_NAME.name(), error);
            super.doGet(req, resp);
        }

        try {
            PasswordEncryptor passwordEncryptor = (PasswordEncryptor) getServletContext().getAttribute(PASSWORD_ENCRYPTOR.name());
            UserSignUpForm userSignUpForm = new UserSignUpForm(
                    req.getParameter(EMAIL.name()),
                    passwordEncryptor.encrypt(req.getParameter(PASSWORD.name())));

            UsersService usersService = (UsersService) getServletContext().getAttribute(USERS_SERVICE.name());
            User user = usersService.singIn(userSignUpForm);
            req.getSession().setAttribute(SESSION_USER.name(), user);
            resp.sendRedirect(req.getContextPath() + HOME);
            return;
        } catch (NotFoundException e) {
            error = "User with this email and password not found.";
        } catch (NullException e) {
            error = "Fill " + e.getMessage() + ". ";
        } catch (DBException | ServiceException e) {
            error(resp,e,500);
            return;
        }

        req.setAttribute(ERROR_MESSAGE_NAME.name(), error);
        super.doGet(req, resp);
    }
}
