package repositories;

import exceptions.DBException;
import exceptions.NotFoundException;
import exceptions.NullException;
import models.Timetable;
import models.User;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public interface Repository {

    void insert(Object object, Field uniqueField) throws DBException, NullException;

    void update(Object object, Field uniqueField) throws NotFoundException, DBException, NullException;

    void delete(Object object, Field uniqueField) throws NotFoundException, DBException, NullException;

    void selectByUniqueField(Object object, Field uniqueField) throws NotFoundException, DBException, NullException;

    //----------------------------------------------------------
    //TODO: разбить репозитории
    //User
    List<User> selectAllUsers() throws DBException;

    User selectUserByEmailAndPassword(String email, String password) throws DBException, NotFoundException, NullException;

    //Timetable
    Map<Timetable, Timetable.AccessRights> selectTimetablesByUser(Long user_id) throws DBException;


}
