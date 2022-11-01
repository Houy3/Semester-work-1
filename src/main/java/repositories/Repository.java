package repositories;

import Exceptions.DBException;
import Exceptions.NotFoundException;
import Exceptions.NotUniqueException;
import models.Note;

public interface Repository<T> {

    void insert(T t) throws DBException;

    void update(T t) throws NotFoundException, DBException;

    void delete(T t) throws NotFoundException, DBException;

    void select_by_id(T t) throws NotFoundException, DBException;
}
