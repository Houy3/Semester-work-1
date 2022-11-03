package services;

import exceptions.*;
import models.*;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface Service {

    //не эффективные реализации множественной вставки, изменения и тп,
    //но пока большего и не нужно
    void add(Object object, String uniqueFieldName) throws NotUniqueException, DBException, NullException, ServiceException;
    default void add(List<Object> objects, String uniqueFieldName) throws NotUniqueException, DBException, NullException, ServiceException {
        for (Object object : objects) {
            add(object, uniqueFieldName);
        }
    }

    void change(Object object, String uniqueFieldName) throws NotFoundException, DBException, NullException, NotUniqueException, ServiceException;
    default void change(List<Object> objects, String uniqueFieldName) throws NotFoundException, DBException, NullException, NotUniqueException, ServiceException {
        for (Object object : objects) {
            change(object, uniqueFieldName);
        }
    }

    void delete(Object object, String uniqueFieldName) throws NotFoundException, DBException, NullException, ServiceException;
    default void delete(List<Object> objects, String uniqueFieldName) throws NotFoundException, DBException, NullException, ServiceException {
        for (Object object : objects) {
            delete(object, uniqueFieldName);
        }
    }

    void getByUniqueField(Object object, String uniqueFieldName) throws NotFoundException, DBException, NullException, ServiceException;
    default void getByUniqueField(List<Object> objects, String uniqueFieldName) throws NotFoundException, DBException, NullException, ServiceException {
        for (Object object : objects) {
            getByUniqueField(object, uniqueFieldName);
        }
    }


    //------------------------------------------------------------

    //@User
    User userSingIn(String email, String password) throws NotFoundException, DBException, NullException;

    List<User> getAllUsers() throws DBException;

    //User_Timetable
    Map<Timetable, Timetable.AccessRights> getTimetablesForUser(Long user_id) throws DBException;

    //Note
    List<Note> getNotesByTimetablesId(List<Long> timetablesId, Boolean isInArchive);
    List<Note> getNotesByTimetableId(Long timetableId, Boolean isInArchive);


    List<Note> getNotesByTimetablesId(List<Long> timetablesId);
    List<Note> getNotesByTimetableId(Long timetableId);

    //Task
    List<Task> getTasksByTimetablesIdAndDate(List<Long> timetablesId, Date date);
    List<Task> getTasksByTimetableIdAndDate(Long timetableId, Date date);


    List<Task> getTasksByTimetablesId(List<Long> timetablesId);
    List<Task> getTasksByTimetableId(Long timetableId);


    //Event
    List<Event> getEventsByTimetablesIdAndDate(List<Long> timetablesId, Date date);
    List<Event> getEventsByTimetableIdAndDate(Long timetableId, Date date);


    List<Event> getEventsByTimetablesId(List<Long> timetablesId);
    List<Event> getEventsByTimetableId(Long timetableId);

    //Period
    void addPeriods(Period period, Event.Repeatability repeatability, Date endTime) throws NotUniqueException, DBException, NullException;


}
