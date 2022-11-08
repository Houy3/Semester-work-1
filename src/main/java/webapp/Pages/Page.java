package webapp.Pages;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class Page extends HttpServlet {

    private static final String mainFold = "/pages/";

    protected void saveOldValuesOrMakeNull(HttpServletRequest req, String[] attributes) {
        for (String i : attributes) {
            req.setAttribute(i, req.getParameter(i));
        }
    }

    protected void utf8(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            req.setCharacterEncoding("UTF-8");
            resp.setContentType("text/html;charset=UTF-8");
        } catch (IOException ignored) {
            resp.sendError(500);
        }
    }

    public void openPage(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathToPage = mainFold + this.getClass().getSimpleName() + ".jsp";
        try {
            getServletContext().getRequestDispatcher(pathToPage).forward(req, resp);
        } catch (ServletException | IOException e) {
            System.out.println(e.getMessage());
            resp.sendError(500);
        }
    }


    protected static void error(HttpServletResponse resp, Exception e, int errorCode) throws IOException {
        System.out.println(e.getMessage());
        resp.sendError(errorCode);
    }
}
