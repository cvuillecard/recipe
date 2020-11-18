package com.recipe.config.spring.jpa;

import com.recipe.config.spring.jpa.specification.ExtensibleSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;

public interface ExtensibleJpaSpecificationExecutor<T> {
    Optional<T> findOne(@Nullable ExtensibleSpecification<T> spec);

    List<T> findAll(@Nullable ExtensibleSpecification<T> spec);

    Page<T> findAll(@Nullable ExtensibleSpecification<T> spec, Pageable pageable);

    List<T> findAll(@Nullable ExtensibleSpecification<T> spec, Sort sort);

    long count(@Nullable ExtensibleSpecification<T> spec);
}
