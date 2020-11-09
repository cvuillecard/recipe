package com.recipe.config.spring.service;

import com.recipe.config.spring.repository.BaseRepository;
import org.springframework.data.domain.Example;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Transactional
public abstract class AbstractService<T, R, ID extends Serializable> implements GenericService<T, ID> {
    private BaseRepository<T, ID> repository;

    public AbstractService(final BaseRepository<T, ID> repository) {
        this.repository = repository;
    }

    @Override public Optional<T> findById(ID id) { return repository.findById(id); }
    @Override public Iterable<T> findAll() { return this.repository.findAll(); }
    @Override public Iterable<T> findAll(final Sort sort) { return this.repository.findAll(sort); }
    @Override public Iterable<T> findAllById(final Iterable<ID> it) { return this.repository.findAllById(it); }
    @Override public <S extends T> List<S> findAll(final Example<S> example) { return this.repository.findAll(example); }
    @Override public <S extends T> List<S> findAll(final Example<S> example, final Sort sort) { return this.repository.findAll(example, sort); }

    @Override public void save(final T bo) { this.repository.save(bo); }
    @Override public T update(final T bo) { return this.repository.save(bo); }
    @Override public <S extends T> Iterable<S> saveAll(final Iterable<S> it) { return this.repository.saveAll(it); }
    @Override public void overwrite(final T bo) { this.repository.overwrite(bo); }
    @Override public void overwriteAll(final Iterable<T> iterable) { this.repository.overwriteAll(iterable); }

    @Override public void delete(final T bo) { this.repository.delete(bo); }
    @Override public void deleteAll(final Iterable<T> it) { this.repository.deleteAll(it); }
    @Override public void deleteByExample(final T bean, final boolean flush) { this.repository.deleteByExample(bean, flush); }
    @Override public void deleteByExamples(final Iterable<T> iterable) { this.repository.deleteByExamples(iterable); }

    protected R repository() { return (R) this.repository; }
}
