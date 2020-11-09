package com.recipe.config.spring.service;

import java.util.Optional;

public interface GenericService<T, ID>
{
    Optional<T> findById(final ID id);
    Iterable<T> findAll();
    void delete(final T b);
    T update(final T b);
    void create(final T b);
}
