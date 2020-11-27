package com.recipe.config.spring.repository;

import com.recipe.config.spring.jpa.support.ExtendedJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface ExtensibleRepository<T, ID extends Serializable> extends ExtendedJpaRepository<T, ID> {
    /**
     * Detach the bean from the entitymanager
     * @param bean
     */
    void detach(final T bean);
    void deleteByIds(final Iterable<ID> ids);

}
