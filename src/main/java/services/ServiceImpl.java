package services;

import exceptions.DBException;
import exceptions.NotFoundException;
import exceptions.NotNullException;
import exceptions.NotUniqueException;
import models.*;
import repositories.RepositoryImpl;
import repositories.Repository;
import services.Service;

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

    private void uniqueCheck(Object object) throws NotUniqueException, DBException, NotNullException {
        repository.uniqueCheck(object);
    }

    //нет проверок внешнего ключа

    @Override
    public void add(Object object) throws NotUniqueException, DBException, NotNullException {
        uniqueCheck(object);
        repository.insert(object);
    }

    @Override
    public void change(Object object) throws NotFoundException, DBException, NotNullException {
        repository.update(object);
    }

    @Override
    public void delete(Object object) throws NotFoundException, DBException, NotNullException {
        repository.delete(object);
    }

    @Override
    public void getById(Object object) throws NotFoundException, DBException, NotNullException {
        repository.select_by_id(object);
    }

    //------------------------------------------------------------

    //User
    @Override
    public User userSingIn(String email, String password) throws NotFoundException, DBException, NotNullException {
        return repository.select_user_by_email_and_password(email, password);
    }

    @Override
    public List<User> getAllUsers() throws DBException {
        return repository.select_all_users();
    }

    //User_Timetable
    @Override
    public Map<User, Timetable.AccessRights> getUsersIdForTimetable(Timetable timetable) throws DBException {
        return repository.select_users_id_by_timetable(timetable);
    }

    @Override
    public Map<Timetable, Timetable.AccessRights> getTimetablesForUser(User user) throws DBException {
        return repository.select_timetables_by_user(user);
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
    public void addPeriods(Period period, Event.Repeatability repeatability, Date endTime) throws NotUniqueException, DBException, NotNullException {

    }



}
