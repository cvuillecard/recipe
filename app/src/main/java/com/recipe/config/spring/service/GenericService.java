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
    <S extends T> List<S> findAll(Iterable<T> iterable);
    <S extends T> List<S> findAll(final Iterable<T> iterable, final Sort sort);

    void save(final T objectBO);
    T update(final T objectBO);
    <S extends T> Iterable<S> saveAll(Iterable<S> it);

    void delete(final T objectBO);
    void deleteAll(final Iterable<T> it);
}
