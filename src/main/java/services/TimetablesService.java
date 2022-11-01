package services;

import java.util.Map;

import Exceptions.DBException;
import Exceptions.NotFoundException;
import models.Timetable;
import models.User;

public interface TimetablesService extends Service<Timetable> {

    void addAccessRights(Timetable timetable, User user, Timetable.AccessRights accessRights) throws NotFoundException, DBException;

    void changeAccessRights(Timetable timetable, User user, Timetable.AccessRights accessRights) throws NotFoundException, DBException;

    void deleteAccessRights(Timetable timetable, User user, Timetable.AccessRights accessRights) throws NotFoundException, DBException;

    Map<Timetable, Timetable.AccessRights> getTimetablesForUser(User user) throws DBException;

    Map<User, Timetable.AccessRights> getUsersIdForTimetable(Timetable timetable) throws DBException;
}
