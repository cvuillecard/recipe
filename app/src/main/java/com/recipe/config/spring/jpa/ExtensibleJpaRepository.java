package com.recipe.config.spring.jpa;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.util.List;

@NoRepositoryBean
public interface ExtensibleJpaRepository<T, ID> extends PagingAndSortingRepository<T, ID>, QueryByExampleExecutor<T> {
    List<T> findAll(final Iterable<T> entities);
    List<T> findAll(final Iterable<T> entities, final Sort sort);
    int insertAll(final Iterable<T> entities, final boolean withId);
    int overwriteAll(final Iterable<T> entities);
    // old versions
    void deleteAllSimpleJpaRepository(final Iterable<? extends T> entities);
}
