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
    <S extends T> List<S> findAll(final Example<S> example);
    <S extends T> List<S> findAll(final Example<S> example, final Sort sort);

    void save(final T objectBO);
    T update(final T objectBO);
    <S extends T> Iterable<S> saveAll(Iterable<S> it);

    void overwrite(final T bo);
    void overwriteAll(final Iterable<T> iterable);

    void delete(final T objectBO);
    void deleteAll(final Iterable<T> it);
    void deleteByExample(final T bean, final boolean flush);
    void deleteByExamples(final Iterable<T> iterable);
}
