package services;

import Exceptions.DBException;
import Exceptions.NotFoundException;
import Exceptions.NotUniqueException;

public interface Service<T> {

    void add(T t) throws NotUniqueException, DBException;

    void change(T t) throws NotFoundException, DBException;

    void delete(T t) throws NotFoundException, DBException;

    void getById(T t) throws NotFoundException, DBException;


}
