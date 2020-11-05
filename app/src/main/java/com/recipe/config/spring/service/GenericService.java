package com.recipe.config.spring.service;

public interface GenericService<T, ID>
{
    T findById(final ID id);
    Iterable<T> findAll();
    void delete(final T objectBO);
    T update(final T objectBO);
    void create(final T objectBO);
    void purgeTable();
}
