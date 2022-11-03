package services;

import exceptions.*;
import models.*;
import repositories.RepositoryImpl;
import repositories.Repository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

//TODO
public class ServiceImpl implements Service {

    protected RepositoryImpl repository;

    public ServiceImpl(Repository repository) {
        this.repository = (RepositoryImpl) repository;
    }

    private void uniqueCheck(Object object) throws NotUniqueException, DBException, NullException {
        repository.uniqueCheck(object);
    }

    //нет проверок внешнего ключа

    @Override
    public void add(Object object, String uniqueFieldName) throws NotUniqueException, DBException, NullException, ServiceException {
        uniqueCheck(object);

        repository.insert(object, getField(object, uniqueFieldName));
    }

    @Override
    public void change(Object object, String uniqueFieldName) throws NotFoundException, DBException, NullException, NotUniqueException, ServiceException {
        uniqueCheck(object);
        repository.update(object, getField(object, uniqueFieldName));
    }

    @Override
    public void delete(Object object, String uniqueFieldName) throws NotFoundException, DBException, NullException, ServiceException {
        repository.delete(object, getField(object, uniqueFieldName));
    }

    @Override
    public void getByUniqueField(Object object, String uniqueFieldName) throws NotFoundException, DBException, NullException, ServiceException {
        repository.selectByUniqueField(object, getField(object, uniqueFieldName));
    }

    //------------------------------------------------------------


    private static Field getField(Object object, String uniqueFieldName) throws ServiceException {
        Field uniqueField;
        try {
            uniqueField = object.getClass().getDeclaredField(uniqueFieldName);
        } catch (NoSuchFieldException e) {
            throw new ServiceException("Не найдено поле у класса " + object.getClass());
        }
        return uniqueField;
    }

    //User
    @Override
    public User userSingIn(String email, String password) throws NotFoundException, DBException, NullException {
        return repository.selectUserByEmailAndPassword(email, password);
    }

    @Override
    public List<User> getAllUsers() throws DBException {
        return repository.selectAllUsers();
    }

    //User_Timetable
    @Override
    public Map<Timetable, Timetable.AccessRights> getTimetablesForUser(Long user_id) throws DBException {
        return repository.selectTimetablesByUser(user_id);
    }

    @Override
    public List<Note> getNotesByTimetablesId(List<Long> timetablesId, Boolean isInArchive) {
        List<Note> notes = new ArrayList<>();
        for (Long timetableId : timetablesId) {
            notes.addAll(getNotesByTimetableId(timetableId, isInArchive));
        }
        return notes;
    }
    @Override
    public List<Note> getNotesByTimetableId(Long timetableId, Boolean isInArchive) {
        return null;
    }

    @Override
    public List<Note> getNotesByTimetablesId(List<Long> timetablesId) {
        List<Note> notes = new ArrayList<>();
        for (Long timetableId : timetablesId) {
            notes.addAll(getNotesByTimetableId(timetableId));
        }
        return notes;
    }
    @Override
    public List<Note> getNotesByTimetableId(Long timetableId) {
        return null;
    }

    @Override
    public List<Task> getTasksByTimetablesIdAndDate(List<Long> timetablesId, Date date) {
        List<Task> tasks = new ArrayList<>();
        for (Long timetableId : timetablesId) {
            tasks.addAll(getTasksByTimetableIdAndDate(timetableId, date));
        }
        return tasks;
    }
    @Override
    public List<Task> getTasksByTimetableIdAndDate(Long timetableId, Date date) {
        return null;
    }

    @Override
    public List<Task> getTasksByTimetablesId(List<Long> timetablesId) {
        List<Task> tasks = new ArrayList<>();
        for (Long timetableId : timetablesId) {
            tasks.addAll(getTasksByTimetableId(timetableId));
        }
        return tasks;
    }
    @Override
    public List<Task> getTasksByTimetableId(Long timetableId) {
        return null;
    }

    @Override
    public List<Event> getEventsByTimetablesIdAndDate(List<Long> timetablesId, Date date) {
        List<Event> events = new ArrayList<>();
        for (Long timetableId : timetablesId) {
            events.addAll(getEventsByTimetableIdAndDate(timetableId, date));
        }
        return events;
    }
    @Override
    public List<Event> getEventsByTimetableIdAndDate(Long timetableId, Date date) {
        return null;
    }

    @Override
    public List<Event> getEventsByTimetablesId(List<Long> timetablesId) {
        List<Event> events = new ArrayList<>();
        for (Long timetableId : timetablesId) {
            events.addAll(getEventsByTimetableId(timetableId));
        }
        return events;
    }
    @Override
    public List<Event> getEventsByTimetableId(Long timetableId) {
        return null;
    }

    @Override
    public void addPeriods(Period period, Event.Repeatability repeatability, Date endTime) throws NotUniqueException, DBException, NullException {

    }



}
