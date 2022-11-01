package repositories;

import exceptions.DBException;
import exceptions.NotFoundException;
import exceptions.NotNullException;
import models.Timetable;
import models.User;

import java.util.List;
import java.util.Map;

public interface Repository {

    void insert(Object object) throws DBException, NotNullException;

    void update(Object object) throws NotFoundException, DBException, NotNullException;

    void delete(Object object) throws NotFoundException, DBException, NotNullException;

    void select_by_id(Object object) throws NotFoundException, DBException, NotNullException;

    //----------------------------------------------------------

    //User
    List<User> select_all_users() throws DBException;

    User select_user_by_email_and_password(String email, String password) throws DBException, NotFoundException, NotNullException;

    //Timetable
    Map<User, Timetable.AccessRights> select_users_id_by_timetable(Timetable timetable) throws DBException;

    Map<Timetable, Timetable.AccessRights> select_timetables_by_user(User user) throws DBException;


}
