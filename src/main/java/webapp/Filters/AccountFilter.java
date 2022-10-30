package webapp.Filters;

import models.User;
import webapp.Pages.AuthenticationPage;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/account")
public class AccountFilter extends HttpFilter {

    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {

        User user = (User)req.getSession().getAttribute("user");
        if (user == null || user.getId() == null) {
            WebServlet webServlet = AuthenticationPage.class.getAnnotation(WebServlet.class);
            System.out.println(webServlet.name());
            res.sendRedirect(req.getContextPath() + "/authentication");
            return;
        }

        chain.doFilter(req, res);
    }
}
