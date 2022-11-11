package webapp.servlets.pages;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class Page extends HttpServlet {

    private static final String mainFold = "/pages/";

    protected List<String> params = new ArrayList<>();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        utf8(req,resp);
        saveOldValuesOfParams(req);
        openPage(req,resp);
    }


    protected void saveOldValuesOfParams(HttpServletRequest req) {
        params.forEach(p -> req.setAttribute(p, req.getParameter(p)));
    }

    protected void utf8(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            req.setCharacterEncoding("UTF-8");
            resp.setContentType("text/html;charset=UTF-8");
        } catch (IOException e) {
            error(resp, e, 500);
        }
    }

    protected void openPage(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathToPage = mainFold + this.getClass().getSimpleName() + ".jsp";
        try {
            getServletContext().getRequestDispatcher(pathToPage).forward(req, resp);
        } catch (ServletException | IOException e) {
            error(resp, e, 500);
        }
    }

    protected static void error(HttpServletResponse resp, Exception e, int errorCode) throws IOException {
        e.printStackTrace();
        resp.sendError(errorCode);
    }

}
