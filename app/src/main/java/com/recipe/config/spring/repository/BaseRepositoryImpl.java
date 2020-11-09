package com.recipe.config.spring.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class BaseRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements BaseRepository<T, ID> {
    private final EntityManager em;

    public BaseRepositoryImpl(final Class<T> domainClass, final EntityManager em) {
        super(domainClass, em);
        this.em = em;
    }

    @Override public void detach(final T bean) {
        em.detach(bean);
    }

    @Override
    public void overwrite(final T bean) {
        if (bean != null) {
            deleteByExample(bean, true);
            save(bean);
        }
    }

    @Override
    public void overwriteAll(final Iterable<T> iterable) {
        if (iterable != null && iterable.iterator().hasNext())
            iterable.forEach(b -> overwrite(b));
    }

    @Override
    public void deleteByExample(final T bean, final boolean flush) {
        if (bean != null) {
            final List<T> records = findAll(Example.of(bean));
            if (!records.isEmpty())
                records.forEach(curr -> delete(curr));
            if (flush)
                flush();
        }
    }

    @Override
    public void deleteByExamples(final Iterable<T> iterable) {
        if (iterable != null && iterable.iterator().hasNext()) {
            iterable.forEach(b -> deleteByExample(b, false));
            flush();
        }
    }
}
