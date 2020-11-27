package com.recipe.config.spring.service;

import com.recipe.config.spring.repository.ExtensibleRepository;
import org.springframework.data.domain.Example;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Transactional
public abstract class AbstractService<T, R, ID extends Serializable> implements GenericService<T, ID> {
    private ExtensibleRepository<T, ID> repository;

    public AbstractService(final ExtensibleRepository<T, ID> repository) {
        this.repository = repository;
    }

    @Override public Optional<T> findById(ID id) { return repository.findById(id); }
    @Override public Iterable<T> findAll() { return this.repository.findAll(); }
    @Override public Iterable<T> findAll(final Sort sort) { return this.repository.findAll(sort); }
    @Override public Iterable<T> findAllById(final Iterable<ID> it) { return this.repository.findAllById(it); }
    @Override public List<T> findAll(final Example<T> example) { return this.repository.findAll(example); }
    @Override public List<T> findAll(final Example<T> example, final Sort sort) { return this.repository.findAll(example, sort); }
    @Override public List<T> findAll(final Iterable<T> iterable) { return this.repository.findAll(iterable); }
    @Override public List<T> findAll(final Iterable<T> iterable, final Sort sort) { return this.repository.findAll(iterable, sort); }

    @Override public T save(final T bo) { return this.repository.save(bo); }
    @Override public T update(final T bo) { return this.repository.save(bo); }
    @Override public List<T> saveAll(final Iterable<T> it) { return this.repository.saveAll(it); }
    @Override public int insertAll(final Iterable<T> it, final boolean withId) { return this.repository.insertAll(it, withId); }
    @Override public int overwriteAll(final Iterable<T> entities) { return this.repository.overwriteAll(entities); }

    @Override public void delete(final T bo) { this.repository.delete(bo); }
    @Override public void deleteAll(final Iterable<T> it) { this.repository.deleteAll(it); }
    @Override public void deleteAllSimpleJpaRepository(final Iterable<? extends T> entities) { this.repository.deleteAllSimpleJpaRepository(entities);}

    protected R repository() { return (R) this.repository; }
}
