package com.recipe.config.spring.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Optional;

@Transactional
public abstract class AbstractService<T, ID extends Serializable> implements GenericService<T, ID> {
    protected JpaRepository<T, ID> dao;

    public AbstractService(final JpaRepository<T, ID> dao) { this.dao = dao; }

    @Override public Optional<T> findById(ID id) { return dao.findById(id); }
    @Override public Iterable<T> findAll() { return this.dao.findAll(); }
    @Override public void delete(final T bo) { this.dao.delete(bo); }
    @Override public T update(final T bo) { return this.dao.save(bo); }
    @Override public void create(final T bo) { this.dao.save(bo); }
}
