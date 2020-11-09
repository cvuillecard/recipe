package com.recipe.config.spring.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Optional;

@Transactional
public abstract class AbstractService<T, ID extends Serializable> implements GenericService<T, ID> {
    protected JpaRepository<T, ID> repository;

    public AbstractService(final JpaRepository<T, ID> repository) { this.repository = repository; }

    @Override public Optional<T> findById(ID id) { return repository.findById(id); }
    @Override public Iterable<T> findAll() { return this.repository.findAll(); }
    @Override public void delete(final T b) { this.repository.delete(b); }
    @Override public T update(final T b) { return this.repository.save(b); }
    @Override public void create(final T b) { this.repository.save(b); }
}
