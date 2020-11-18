package com.recipe.config.spring.jpa;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.util.List;

@NoRepositoryBean
public interface ExtensibleJpaRepository<T, ID> extends PagingAndSortingRepository<T, ID>, QueryByExampleExecutor<T> {
    <S extends T> List<S> findAll(final Iterable<T> entities);
    <S extends T> List<S> findAll(final Iterable<T> entities, final Sort sort);
}
