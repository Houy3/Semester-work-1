package webapp.Utils;

import exceptions.DBException;
import exceptions.ServiceException;
import exceptions.WebException;
import models.Timetable;
import models.User;
import services.Inter.TimetablesService;
import services.Inter.UsersService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import static webapp.Utils.Constants.*;
import static webapp.Utils.Utils.requestCheck;

public class Options {

    public static String getAllUsersExceptSessionUser(HttpServletRequest request, String checkedLong) throws WebException {
        requestCheck(request);
        try {
            Long checked;
            try {
                checked = Long.parseLong(checkedLong);
            } catch (Exception e) {
                checked = null;
            }

            StringBuilder out = new StringBuilder();

            UsersService usersService = (UsersService) request.getServletContext().getAttribute(USERS_SERVICE.name());
            User me = (User) request.getSession().getAttribute(SESSION_USER.name());
            try {
                List<User> users = usersService.getAllUsers();
                for (User user : users) {
                    if (user.getId().equals(me.getId())) {
                        continue;
                    }
                    out.append("<option ");
                    if (user.getId().equals(checked)) {
                        out.append("selected ");
                    }
                    out.append("value=\"").append(user.getId()).append("\"");
                    out.append(">").append(user.getNickname()).append("</option>");
                }
            } catch (DBException | ServiceException e) {
                e.printStackTrace();
            }

            return out.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getAllTimetablesOfWhichSessionUserIsOwner(HttpServletRequest request, String checkedLong) throws WebException {
        requestCheck(request);
        try {
            Long checked;
            try {
                checked = Long.parseLong(checkedLong);
            } catch (Exception e) {
                checked = null;
            }

            StringBuilder out = new StringBuilder();

            TimetablesService timetablesService = (TimetablesService) request.getServletContext().getAttribute(TIMETABLES_SERVICE.name());
            User me = (User) request.getSession().getAttribute(SESSION_USER.name());
            try {
                Map<Timetable, Timetable.AccessRights> timetables = timetablesService.getTimetablesForUserId(me.getId());
                for (Timetable timetable : timetables.keySet().stream()
                        .filter(t -> timetables.get(t).equals(Timetable.AccessRights.OWNER))
                        .toList()) {
                    out.append("<option ");
                    if (timetable.getId().equals(checked)) {
                        out.append("selected ");
                    }
                    out.append("value=\"").append(timetable.getId()).append("\"");
                    out.append(">").append(timetable.getName()).append("</option>");
                }
            } catch (DBException | ServiceException e) {
                e.printStackTrace();
            }

            return out.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getAllTimetablesOfWhichSessionUserIsOwnerOrWriter(HttpServletRequest request, String checkedLong) throws WebException {
        requestCheck(request);
        try {
            Long checked;
            try {
                checked = Long.parseLong(checkedLong);
            } catch (Exception e) {
                checked = null;
            }

            StringBuilder out = new StringBuilder();

            TimetablesService timetablesService = (TimetablesService) request.getServletContext().getAttribute(TIMETABLES_SERVICE.name());
            User me = (User) request.getSession().getAttribute(SESSION_USER.name());
            try {
                Map<Timetable, Timetable.AccessRights> timetables = timetablesService.getTimetablesForUserId(me.getId());
                for (Timetable timetable : timetables.keySet().stream()
                        .filter(t -> timetables.get(t).equals(Timetable.AccessRights.OWNER)
                                 || timetables.get(t).equals(Timetable.AccessRights.WRITER))
                        .toList()) {
                    out.append("<option ");
                    if (timetable.getId().equals(checked)) {
                        out.append("selected ");
                    }
                    out.append("value=\"").append(timetable.getId()).append("\"");
                    out.append(">").append(timetable.getName()).append("</option>");
                }
            } catch (DBException | ServiceException e) {
                e.printStackTrace();
            }

            return out.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getAllTimetableAccessRightsWithoutOwner(String checkedEnum) {
        try {
            Timetable.AccessRights checked;
            try {
                checked = Timetable.AccessRights.valueOf(checkedEnum);
            } catch (Exception e) {
                checked = null;
            }

            StringBuilder out = new StringBuilder();

            for (Timetable.AccessRights accessRights : Timetable.AccessRights.values()) {
                if (accessRights.equals(Timetable.AccessRights.OWNER)) {
                    continue;
                }
                out.append("<option ");
                if (accessRights.equals(checked)) {
                    out.append("selected ");
                }
                out.append("value=\"").append(accessRights.name()).append("\"");
                out.append(">").append(accessRights.name()).append("</option>");
            }

            return out.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getDefault(HttpServletRequest request, String defaultName) throws WebException {
        requestCheck(request);
        if (defaultName == null) {
            throw new WebException("Default name is empty.");
        }
        return "<option selected value=\"" + DEFAULT_OPTION_VALUE + "\">" + defaultName + "</option>";
    }

}
