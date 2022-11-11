package webapp.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static webapp.Utils.Constants.SESSION_USER;
import static webapp.Utils.Paths.SIGN_OUT;

@WebServlet(SIGN_OUT)
public class SignOutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.getSession().setAttribute(SESSION_USER.name(), null);
        resp.sendRedirect(req.getContextPath());
    }
}
