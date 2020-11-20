package com.recipe.config.spring.service;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface GenericService<T, ID>
{
    Optional<T> findById(final ID id);
    Iterable<T> findAll(final Sort sort);
    Iterable<T> findAll();
    Iterable<T> findAllById(final Iterable<ID> it);
    List<T> findAll(final Example<T> example);
    List<T> findAll(final Example<T> example, final Sort sort);
    List<T> findAll(Iterable<T> iterable);
    List<T> findAll(final Iterable<T> iterable, final Sort sort);

    void save(final T objectBO);
    T update(final T objectBO);
    Iterable<T> saveAll(Iterable<T> it);
    int insertAll(final Iterable<T> it, final boolean withId);
    int overwriteAll(final Iterable<T> entities);

    void delete(final T objectBO);
    void deleteAll(final Iterable<T> it);

    // re-written
    void deleteAllSimpleJpaRepository(final Iterable<? extends T> entities); // replace : void deleteAll(final Iterable<T> it)
}
