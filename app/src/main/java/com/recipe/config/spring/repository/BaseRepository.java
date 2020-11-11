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
     * find all the elements of the sequence given and returns records corresponding
     *
     * @param iterable entity sequence
     * @param excludeFields field names to exclude from reflection
     *
     * @return records
     *
     *
     */
    Iterable<T> findAll(final Iterable<T> iterable, final String... excludeFields);

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
     * @param excludeFields field names of the class entity to exclude from query
     * @see com.recipe.config.spring.repository.BaseRepository#overwrite
     */
    void overwriteAll(final Iterable<T> iterable, final String... excludeFields);

    /**
     * Find all records corresponding to the sequence of entities given and delete them if found.
     *
     * @param iterable the entities to delete
     * @param excludeFields field names of the class entity to exclude from query
     */
    void deleteByExample(final Iterable<T> iterable, final String... excludeFields);

    /**
     * Find all records for the entity given and delete them if found.
     *
     * @param bean to delete
     * @param excludeFields field names to exclude from query
     */
    void deleteByExample(final T bean, final String... excludeFields);

    void deleteByIds(final Iterable<ID> ids, final Class<T> clazz);

}
