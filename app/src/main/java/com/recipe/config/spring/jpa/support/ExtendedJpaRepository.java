package com.recipe.config.spring.jpa.support;

import com.recipe.config.spring.jpa.ExtensibleJpaRepository;
import com.recipe.config.spring.jpa.ExtensibleJpaSpecificationExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@NoRepositoryBean
public interface ExtendedJpaRepository<T, ID> extends JpaRepositoryImplementation<T, ID>, ExtensibleJpaRepository<T, ID>, ExtensibleJpaSpecificationExecutor<T> {
    default Optional<T> findOne(final Specification<T> specification) { return Optional.empty(); }
    default List<T> findAll(final Specification<T> specification) { return Collections.EMPTY_LIST; }
    default Page<T> findAll(final Specification<T> specification, final Pageable pageable) { return Page.empty(); }
    default List<T> findAll(final Specification<T> specification, final Sort sort) { return Collections.EMPTY_LIST; }
    default long count(final Specification<T> specification) { return 0; }

}

//
//@NoRepositoryBean
//public interface ExtendedJpaRepository<T, ID> extends ExtensibleJpaRepository<T, ID>, ExtensibleJpaSpecificationExecutor<T> {
//    void setRepositoryMethodMetadata(CrudMethodMetadata var1);
//
//    default void setEscapeCharacter(EscapeCharacter escapeCharacter) {}
//}