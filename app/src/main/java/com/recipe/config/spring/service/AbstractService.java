package com.recipe.config.spring.service;

import com.recipe.config.spring.dao.GenericDao;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.Serializable;

@Transactional
public abstract class AbstractService<T, ID extends Serializable> implements GenericService<T, ID> {
    protected GenericDao<T, ID> dao;

    private AbstractService() {}
    public AbstractService(final GenericDao<T, ID> dao) { this.dao = dao; }

    @Override public T findById(ID id) { return this.dao.findById(id); }
    @Override public Iterable<T> findAll() { return this.dao.findAll(); }
    @Override public void delete(final T bo) { this.dao.delete(bo); }
    @Override public T update(final T bo) { return this.dao.merge(bo); }
    @Override public void create(final T bo) { this.dao.save(bo); }
    @Override public void purgeTable() { this.dao.purgeTable(); }
}
