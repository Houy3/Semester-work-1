package services;

import exceptions.DBException;
import exceptions.NotFoundException;
import exceptions.NotNullException;
import exceptions.NotUniqueException;
import models.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface Service {

    //не эффективные реализации множественной вставки, изменения и тп,
    //но пока большего и не нужно
    void add(Object object) throws NotUniqueException, DBException, NotNullException;
    default void add(List<Object> objects) throws NotUniqueException, DBException, NotNullException {
        for (Object object : objects) {
            add(object);
        }
    }

    void change(Object object) throws NotFoundException, DBException, NotNullException;
    default void change(List<Object> objects) throws NotFoundException, DBException, NotNullException {
        for (Object object : objects) {
            change(object);
        }
    }

    void delete(Object object) throws NotFoundException, DBException, NotNullException;
    default void delete(List<Object> objects) throws NotFoundException, DBException, NotNullException {
        for (Object object : objects) {
            delete(object);
        }
    }

    void getById(Object object) throws NotFoundException, DBException, NotNullException;
    default void getById(List<Object> objects) throws NotFoundException, DBException, NotNullException {
        for (Object object : objects) {
            getById(object);
        }
    }


    //------------------------------------------------------------

    //@User
    User userSingIn(String email, String password) throws NotFoundException, DBException, NotNullException;

    List<User> getAllUsers() throws DBException;

    //User_Timetable
    Map<Timetable, Timetable.AccessRights> getTimetablesForUser(User user) throws DBException;

    Map<User, Timetable.AccessRights> getUsersIdForTimetable(Timetable timetable) throws DBException;

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
    void addPeriods(Period period, Event.Repeatability repeatability, Date endTime) throws NotUniqueException, DBException, NotNullException;


}
