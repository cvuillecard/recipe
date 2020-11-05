package com.recipe.config.spring.dao;

import com.recipe.config.spring.bo.Entity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * Abstract Data Access Object
 *
 * @param <T>  Interface contract
 * @param <ID> Primary key type
 * @param <B>  bean implementation of T interface
 */
public abstract class AbstractJpaDao<B extends T, T extends Entity<ID>, ID extends Serializable> implements GenericDao<T, ID> {
    private final Class<T> type;
    private final String mPersistentClassName;

    @PersistenceContext
    protected EntityManager entityManager;

    public AbstractJpaDao() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.mPersistentClassName = this.type.getName().substring(this.type.getName().lastIndexOf('.') + 1);
    }

    public Class<T> getPersistentClass() {
        return type;
    }

    public T findById(final ID id) {
        return entityManager.find(getPersistentClass(), id);
    }

    @Override
    public List<T> findAll() {
        return entityManager.createQuery("from " + type.getName()).getResultList();
    }

    @Override
    public long countAll() {
        return (Long) entityManager.createQuery("select count(*) from " + mPersistentClassName).getSingleResult();
    }

    @Override
    public T merge(final T bo) {
        return entityManager.merge((B) bo);
    }

    @Override
    public T save(final T bo) {
        entityManager.persist(bo);
        return bo;
    }

    @Override
    public void delete(final T bo) {
        entityManager.remove(isDetached(bo) ? merge(bo) : bo);
        entityManager.flush();
    }

    @Override
    public void purgeTable() {
        entityManager.createQuery("DELETE FROM " + type.getName()).executeUpdate();
    }

    @Override public void flush() { entityManager.flush(); }
    @Override public void clear() { entityManager.clear(); }
    @Override public void detach(final T bo) { entityManager.detach(bo); }
    @Override public boolean isDetached(final T bo) { return !entityManager.contains(bo); }
}
