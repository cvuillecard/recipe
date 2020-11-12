package com.recipe.config.spring.repository;

import com.recipe.config.utils.ReflectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class BaseRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements BaseRepository<T, ID> {
    private static final Logger LOG = LoggerFactory.getLogger(BaseRepositoryImpl.class);
    private final EntityManager em;

    public BaseRepositoryImpl(final Class<T> domainClass, final EntityManager em) {
        super(domainClass, em);
        this.em = em;
    }

    @Override
    public void detach(final T bean) {
        em.detach(bean);
    }

    @Override
    public Iterable<T> findAll(final Iterable<T> iterable, final String... excludeFields) {
        if (iterable == null || !iterable.iterator().hasNext()) return Collections.EMPTY_LIST;

        T inst = iterable.iterator().next();
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final Class clazz = inst.getClass();
        final Map<String, Method> getterFields = ReflectUtil.mapGetters(inst, excludeFields);
        final CriteriaQuery<T> query = cb.createQuery(clazz);
        final Root<T> root = query.from(clazz);

        final Iterator<T> iterator = iterable.iterator();
        Predicate orCond = null;

        while (iterator.hasNext()) {
            inst = iterator.next();
            Predicate andCond = null;
            for (final Map.Entry<String, Method> entry : getterFields.entrySet()) {
                try {
                    final Path<Object> path = root.get(entry.getKey());
                    final Object value = entry.getValue().invoke(inst);
                    final Predicate fieldCond = (value != null) ? cb.equal(path, value) : cb.isNull(path);
                    andCond = (andCond == null) ? fieldCond : cb.and(andCond, fieldCond);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    LOG.error(e.getMessage());
                }
            }
            orCond = (orCond == null) ? andCond : cb.or(orCond, andCond);
        }

        query.where(orCond);

        final List<T> resultList = em.createQuery(query).getResultList();

        return resultList != null ? resultList : Collections.EMPTY_LIST;
    }

    @Override
    public void overwrite(final T bean) {
        if (bean != null) {
            deleteByExample(Collections.singletonList(bean));
            save(bean);
        }
    }

    @Override
    public void overwriteAll(final Iterable<T> iterable, final String... excludeFields) {
        if (iterable != null && iterable.iterator().hasNext()) {
            deleteByExample(iterable, excludeFields);
            saveAll(iterable);
        }
    }

    @Override
    public void deleteByExample(final Iterable<T> iterable, final String... excludeFields) {
        if (iterable != null && iterable.iterator().hasNext()) {
            final Iterable<T> records = findAll(iterable, excludeFields);
            if (records.iterator().hasNext()) {
                final Iterator<T> iterator = records.iterator();
                final String hql = "DELETE FROM " + records.iterator().next().getClass().getSimpleName() + " WHERE id IN (:ids)";
                final List<ID> ids = new ArrayList<>();

                while (iterator.hasNext()) {
                    final T inst = iterator.next();
                    try {
                        final Method getId = inst.getClass().getMethod("getId", null);
                        ids.add((ID) getId.invoke(inst));
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                        LOG.error(e.getMessage());
                    }
                }

                final Query query = em.createQuery(hql);
                query.setParameter("ids", ids);
                query.executeUpdate();
            }
        }
    }

    @Override
    public void deleteByExample(final T bean, final String... excludeFields) {
        deleteByExample(Collections.singletonList(bean), excludeFields);
    }

    @Override
    public void deleteByIds(final Iterable<ID> ids, final Class<T> clazz) {
        if (ids != null && ids.iterator().hasNext() && clazz != null) {
            final String delete = "DELETE FROM " + clazz.getSimpleName() + " WHERE id IN (:ids)";
            final Query query = em.createQuery(delete).setParameter("ids", ids);

            query.executeUpdate();
        }
    }

}
