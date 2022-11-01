package services;

import Exceptions.DBException;
import Exceptions.NotFoundException;
import Exceptions.NotUniqueException;
import repositories.AbstractRepository;

public abstract class AbstractService<T> implements Service<T> {

    protected AbstractRepository<T> repository;

    public AbstractService(AbstractRepository<T> repository) {
        this.repository = repository;
    }


    protected void uniqueCheck(T t) throws NotUniqueException, DBException {
        //TODO
    }

    @Override
    public void add(T t) throws NotUniqueException, DBException {
        uniqueCheck(t);
        repository.insert(t);
    }

    @Override
    public void change(T t) throws NotFoundException, DBException {
        repository.update(t);
    }

    @Override
    public void delete(T t) throws NotFoundException, DBException {
        repository.delete(t);
    }

    @Override
    public void getById(T t) throws NotFoundException, DBException {
        repository.select_by_id(t);
    }
}
