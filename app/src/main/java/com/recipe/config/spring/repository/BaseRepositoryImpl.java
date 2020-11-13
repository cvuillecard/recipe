package com.recipe.config.spring.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.convert.QueryByExamplePredicateBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.EscapeCharacter;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.*;

public class BaseRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements BaseRepository<T, ID> {
    private static final Logger LOG = LoggerFactory.getLogger(BaseRepositoryImpl.class);
    private final EntityManager em;

    public BaseRepositoryImpl(final Class<T> domainClass, final EntityManager em) {
        super(domainClass, em);
        this.em = em;
    }

    private static class IterableSpecification<T> implements Specification<T> {
        private static final long serialVersionUID = 1L;
        private final Iterable<T> examples;
        private Class<? extends T> probeType;
        private final EscapeCharacter escapeCharacter;

        IterableSpecification(final Iterable<T> examples, final EscapeCharacter escapeCharacter) {
            Assert.notNull(examples, "Examples must not be null!");
            Assert.isTrue(examples.iterator().hasNext(), "Examples must not be empty!");
            Assert.notNull(escapeCharacter, "EscapeCharacter must not be null!");

            this.examples = examples;
            this.escapeCharacter = escapeCharacter;
        }

        public Predicate toPredicate(final Root<T> root, final CriteriaQuery<?> query, final CriteriaBuilder cb) {
            final Iterator<T> it = this.examples.iterator();
            final Example<T> example = Example.of(it.next());
            this.probeType = example.getProbeType();

            Predicate where = QueryByExamplePredicateBuilder.getPredicate(root, cb, example, this.escapeCharacter);

            while (it.hasNext()) {
                final Predicate cond = QueryByExamplePredicateBuilder.getPredicate(root, cb, Example.of(it.next()), this.escapeCharacter);
                where = cb.or(where, cond);
            }

            return where;
        }

        public <S extends T> Class<S> getProbeType() {
            return (Class<S>) (probeType == null ? Example.of(examples.iterator().next()).getProbeType() : probeType);
        }
    }

    @Override
    public void detach(final T bean) {
        em.detach(bean);
    }

    @Override
    public <S extends T> List<S>  findAll(final Iterable<T> iterable) {
        return findAll(iterable, Sort.unsorted());
    }

    @Override
    //todo : not tested
    public <S extends T> List<S>  findAll(final Iterable<T> iterable, final Sort sort) {
        if (iterable == null || !iterable.iterator().hasNext()) return null;
        final IterableSpecification<T> spec = new IterableSpecification<>(iterable, EscapeCharacter.DEFAULT);

        return (List<S>) this.getQuery(spec, spec.getProbeType(), sort).getResultList();
    }

    @Override
    //todo : not tested just written, perhaps doesn't work - do tests, perhaps to be re-implemented
    public void deleteByIds(final Iterable<ID> ids) {
        if (ids != null && ids.iterator().hasNext()) {
            final Class<T> clazz = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            final String delete = "DELETE FROM " + clazz.getSimpleName() + " WHERE id IN (:ids)";
            final Query query = em.createQuery(delete).setParameter("ids", ids);

            query.executeUpdate();
        }
    }

}
