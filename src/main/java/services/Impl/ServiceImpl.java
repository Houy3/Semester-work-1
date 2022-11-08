package services.Impl;

import exceptions.*;
import models.*;
import repositories.Impl.RepositoryImpl;
import repositories.Repository;
import services.Service;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

public class ServiceImpl implements Service {

    protected RepositoryImpl repository;

    public ServiceImpl(Repository repository) {
        this.repository = (RepositoryImpl) repository;
    }

    //нет проверок внешнего ключа

    @Override
    public void add(Object object) throws NotUniqueException, NullException, ServiceException, DBException {
        repository.insert(object);
    }
    @Override
    public void add(Collection<Object> objects) throws NotUniqueException, NullException, ServiceException, DBException {
        for (Object object : objects) {
            add(object);
        }
    }

    @Override
    public void change(Object object, String uniqueFieldName) throws NotUniqueException, NullException, ServiceException, NotFoundException, DBException {
        repository.update(object, getField(object, uniqueFieldName));
    }
    @Override
    public void change(Collection<Object> objects, String uniqueFieldName) throws NotFoundException, DBException, NullException, NotUniqueException, ServiceException {
        for (Object object : objects) {
            change(object, uniqueFieldName);
        }
    }

    @Override
    public void delete(Object object, String uniqueFieldName) throws NotFoundException, ServiceException, DBException {
        repository.delete(object, getField(object, uniqueFieldName));
    }
    @Override
    public void delete(Collection<Object> objects, String uniqueFieldName) throws NotFoundException, ServiceException, DBException {
        for (Object object : objects) {
            delete(object, uniqueFieldName);
        }
    }


    @Override
    public void getByUniqueField(Object object, String uniqueFieldName) throws NotFoundException, ServiceException, DBException {
        repository.selectByUniqueField(object, getField(object, uniqueFieldName));
    }
    @Override
    public void getByUniqueField(Collection<Object> objects, String uniqueFieldName) throws NotFoundException, DBException, ServiceException {
        for (Object object : objects) {
            getByUniqueField(object, uniqueFieldName);
        }
    }

    //------------------------------------------------------------

    /** Получить Field по его имени */
    private static Field getField(Object object, String uniqueFieldName) throws ServiceException {
        Field uniqueField;
        try {
            uniqueField = object.getClass().getDeclaredField(uniqueFieldName);
        } catch (NoSuchFieldException e) {
            throw new ServiceException("Не найдено поле " + uniqueFieldName + " у класса " + object.getClass());
        }
        return uniqueField;
    }

}
