package com.recipe.config.spring.service;

import java.util.Optional;

public interface GenericService<T, ID>
{
    Optional<T> findById(final ID id);
    Iterable<T> findAll();
    void delete(final T objectBO);
    T update(final T objectBO);
    void create(final T objectBO);
}
