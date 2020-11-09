package com.recipe.config.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {
    /**
     * Detach the bean from the entitymanager
     * @param bean
     */
    void detach(final T bean);

    /**
     * Find the corresponding records for the given entity and delete them if found.
     * Then save the entity given as parameter.
     *
     * Note : This ensures a new id is generated, instead of using 'entityManager.save(T b)'
     *
     * @param bean the entity to replace in database
     */
    void overwrite(final T bean);

    /**
     * Find the corresponding records for the given entities and delete them.
     * Then save the entities sequence given as parameter.
     *
     * @param iterable the sequence of entities to overwrite
     * @see com.recipe.config.spring.repository.BaseRepository#overwrite
     */
    void overwriteAll(final Iterable<T> iterable);

    /**
     * Find all records corresponding to the entity given and delete them if found.
     *
     * @param bean the entity to delete
     * @param flush frees and execute the cache instructions
     */
    void deleteByExample(final T bean, final boolean flush);

    /**
     * Find all entries corresponding to the element sequence and delete each found entry.
     * Then, flushes the instruction cache.
     *
     * @param iterable the sequence of entities to delete
     * @see com.recipe.config.spring.repository.BaseRepository#deleteByExample
     */
    void deleteByExamples(final Iterable<T> iterable);
}
