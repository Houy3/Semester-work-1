package services;

import exceptions.*;

import java.util.Collection;
import java.util.List;

public interface Service {

    void add(Object object) throws NotUniqueException, DBException, NullException, ServiceException;
    void add(Collection<Object> objects) throws NotUniqueException, DBException, NullException, ServiceException;

    void change(Object object, String uniqueFieldName) throws NotFoundException, DBException, NullException, NotUniqueException, ServiceException;
    void change(Collection<Object> objects, String uniqueFieldName) throws NotFoundException, DBException, NullException, NotUniqueException, ServiceException;

    void delete(Object object, String uniqueFieldName) throws NotFoundException, DBException, NullException, ServiceException;
    void delete(Collection<Object> objects, String uniqueFieldName) throws NotFoundException, DBException, NullException, ServiceException;

    void getByUniqueField(Object object, String uniqueFieldName) throws NotFoundException, DBException, NullException, ServiceException;
    void getByUniqueField(Collection<Object> objects, String uniqueFieldName) throws NotFoundException, DBException, NullException, ServiceException;


}
