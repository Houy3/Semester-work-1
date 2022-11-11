package webapp.Filters;

import models.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static webapp.Utils.Constants.*;
import static webapp.Utils.Paths.*;

@WebFilter(urlPatterns = {HOME, NOTES, TIMETABLE_SHARE, TIMETABLE_EDIT, NOTE_EDIT, TASK_EDIT, EVENT_EDIT, PROFILE})
public class UserFilter extends HttpFilter {

    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {

        User user = (User)req.getSession().getAttribute(SESSION_USER.name());
        if (user == null || user.getId() == null) {
            res.sendRedirect(req.getContextPath()+SIGN_IN);
            return;
        }
        chain.doFilter(req, res);
    }
}
