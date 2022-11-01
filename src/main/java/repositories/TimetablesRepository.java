package repositories;

import Exceptions.DBException;
import Exceptions.NotFoundException;
import models.Timetable;
import models.User;

import java.util.Map;

public interface TimetablesRepository extends Repository<Timetable> {

    void insert_access_rights(Timetable timetable, User user, Timetable.AccessRights accessRights) throws NotFoundException, DBException;

    void update_access_rights(Timetable timetable, User user, Timetable.AccessRights accessRights) throws NotFoundException, DBException;

    void delete_access_rights(Timetable timetable, User user) throws NotFoundException, DBException;


    Map<User, Timetable.AccessRights> select_users_id_by_timetable(Timetable timetable) throws DBException;

    Map<Timetable, Timetable.AccessRights> select_timetables_by_user(User user) throws DBException;

}
