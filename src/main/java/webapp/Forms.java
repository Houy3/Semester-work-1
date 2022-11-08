package webapp;

import exceptions.ServiceException;
import models.Timetable;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.net.http.HttpRequest;
import java.util.Map;

import static webapp.Constants.CHOSEN_TIMETABLES;
import static webapp.Constants.SPLIT_CHAR;

public class Forms {

    public static String checkedTimetables(HttpServletRequest request) throws ServiceException {
        try {
            StringBuilder out = new StringBuilder();
            out.append("<p class=\"font-weight-bold text-center\">Choose timetables</p>");
            out.append("<button type=\"submit\" class=\"btn btn-primary\" >Reset</button>");

            Map<Timetable, Timetable.AccessRights> timetables = (Map<Timetable, Timetable.AccessRights>) request.getAttribute("timetables");
            if (timetables.isEmpty()) {
                out.append("You don't have timetables");
                return out.toString();
            }

            List<Long> checked = new ArrayList<>();
            Boolean all = false;
            try {
                Cookie cookie = Arrays.stream(request.getCookies()).filter(c -> c.getName().equals(CHOSEN_TIMETABLES)).findAny().orElse(null);
                if (cookie == null) {
                    all = true;
                } else {
                    if (!cookie.getValue().equals("")) {
                        String[] cookieValue = cookie.getValue().split(SPLIT_CHAR);
                        checked = Arrays.stream(cookieValue).map(Long::parseLong).toList();
                    }
                }
            } catch (Exception e) {
                throw new ServiceException("Куки сломались");
            }

            for (Timetable.AccessRights accessRights : new Timetable.AccessRights[]{Timetable.AccessRights.OWNER, Timetable.AccessRights.WRITER, Timetable.AccessRights.READER}) {
                if (timetables.containsValue(accessRights)) {
                    out.append("<div> You have access rights \"").append(accessRights).append("\" for:</div>");
                    out.append("<ul>");
                    for (Timetable timetable : timetables.keySet().stream()
                            .filter(t -> timetables.get(t) == accessRights)
                            .toList()) {
                        String checkedS = checked.contains(timetable.getId()) || all ? "checked" : "";
                        out.append("<li>");
                        out.append("<div class=\"form-check\">\n" +
                                "  <input name=\"" + timetable.getId() + "\" class=\"form-check-input\" type=\"checkbox\" " + checkedS + ">\n" +
                                "  <label class=\"form-check-label\">");
                        out.append(timetable.getName());
                        out.append("  </label>\n" +
                                "</div>");
                        out.append("</li>");
                    }
                    out.append("</ul>");
                }
            }
            return out.toString();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "";
        }
    }
}
