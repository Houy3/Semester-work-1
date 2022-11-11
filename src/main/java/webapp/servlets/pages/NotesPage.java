package webapp.servlets.pages;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

import static webapp.Utils.Paths.NOTES;

@WebServlet(NOTES)
public class NotesPage extends Page {

    public enum Parameters {
        SHOW_ARCHIVE
    }

    @Override
    public void init() throws ServletException {
        params = Arrays.stream(NotesPage.Parameters.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
