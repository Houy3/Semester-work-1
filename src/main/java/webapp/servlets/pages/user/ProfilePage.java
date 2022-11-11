package webapp.servlets.pages.user;

import exceptions.*;
import models.User;
import models.validators.EmailValidator;
import models.validators.NicknameValidator;
import services.Inter.UsersService;
import webapp.servlets.pages.Page;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;



import static webapp.Utils.Constants.*;
import static webapp.Utils.Paths.PROFILE;
import static webapp.Utils.Utils.notEmpty;
import static webapp.servlets.pages.user.ProfilePage.Parameters.*;

@WebServlet(PROFILE)
public class ProfilePage extends Page {

    enum Parameters {
        NAME,
        SURNAME,
        NICKNAME,
        EMAIL,
        TYPE
    }

    @Override
    public void init() {
        params = Arrays.stream(ProfilePage.Parameters.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        utf8(req, resp);
        User user = (User) req.getSession().getAttribute(SESSION_USER.name());
        req.setAttribute(NAME.name(), user.getName());
        req.setAttribute(SURNAME.name(), user.getSurname());
        req.setAttribute(NICKNAME.name(), user.getNickname());
        req.setAttribute(EMAIL.name(), user.getEmail());
        openPage(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String error = "";
        utf8(req, resp);

        if (req.getParameter(TYPE.name()).equals(ACTION_DELETE.name())) {
            this.doDelete(req, resp);
            return;
        }

        try {
            EmailValidator emailValidator = (EmailValidator) getServletContext().getAttribute(EMAIL_VALIDATOR.name());
            emailValidator.check(req.getParameter(EMAIL.name()));

            NicknameValidator nicknameValidator = (NicknameValidator) getServletContext().getAttribute(NICKNAME_VALIDATOR.name());
            nicknameValidator.check(NICKNAME.name());
        } catch (IllegalArgumentException e) {
            req.setAttribute(ERROR_MESSAGE_NAME.name(), e.getMessage());
            super.doGet(req, resp);
        }


        User user = (User) req.getSession().getAttribute(SESSION_USER.name());
        user.setName(notEmpty(req.getParameter(NAME.name())));
        user.setSurname(notEmpty(req.getParameter(SURNAME.name())));
        user.setNickname(notEmpty(req.getParameter(NICKNAME.name())));
        user.setEmail(req.getParameter(EMAIL.name()));

        try {
            UsersService usersService = (UsersService) getServletContext().getAttribute(USERS_SERVICE.name());
            usersService.change(user, "id");
        } catch (NotUniqueException e) {
            error = "User with this " + e.getMessage() + " already exists. ";
        } catch (NullException e) {
            error = "Fill " + e.getMessage() + ". ";
        } catch (NotFoundException | DBException | ServiceException e) {
            error(resp, e, 500);
            return;
        }

        if (error.equals("")) {
            req.setAttribute(SUCCESS_MESSAGE_NAME.name(), "Success");
        } else {
            req.setAttribute(ERROR_MESSAGE_NAME.name(), error);
        }
        this.doGet(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        User user = (User) req.getSession().getAttribute(SESSION_USER.name());
        if (user.getAccessRights().equals(User.AccessRights.ADMIN)) {
            req.setAttribute(ERROR_MESSAGE_NAME.name(), "You are an admin. You can't delete an account. ");
            this.doGet(req, resp);
            return;
        }
        UsersService usersService = (UsersService) getServletContext().getAttribute(USERS_SERVICE.name());
        try {
            usersService.delete(user, "id");
            req.getSession().setAttribute(SESSION_USER.name(), null);
        } catch (NotFoundException | DBException | NullException | ServiceException e) {
            error(resp, e, 500);
            return;
        }
        resp.sendRedirect(req.getContextPath());
    }
}
