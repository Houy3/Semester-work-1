package webapp.servlets.pages;

import webapp.Utils.Constants;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static webapp.Utils.Paths.HOME;

@WebServlet(HOME)
public class HomePage extends Page {

    public enum Parameters {
        DAY
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        utf8(req, resp);

        if (req.getParameter(Parameters.DAY.name()) == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0,0,0);
            DateFormat dateFormat = new SimpleDateFormat((String) getServletContext().getAttribute(Constants.HTML_DATE_FORMAT.name()));
            req.setAttribute(Parameters.DAY.name(), dateFormat.format(calendar.getTime()));
        } else {
            req.setAttribute(Parameters.DAY.name(), req.getParameter(Parameters.DAY.name()));
        }

        super.doGet(req, resp);
    }
}
