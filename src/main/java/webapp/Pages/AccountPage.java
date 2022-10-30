package webapp.Pages;

import models.User;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/account")
public class AccountPage extends Page {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        utf8(req, resp);

        User user = (User)req.getSession().getAttribute("user");
        req.setAttribute("email", user.getEmail());

        openPage(req, resp);
    }
}
