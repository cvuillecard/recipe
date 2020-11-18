package com.recipe.config.spring.jpa;

import com.recipe.config.spring.repository.ExtensibleRepositoryImpl;
import com.recipe.config.spring.jpa.support.ExtendedJpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class ExtensibleRepositoryFactoryBean<R extends ExtendedJpaRepository<T, ID>, T, ID extends Serializable> extends JpaRepositoryFactoryBean<R, T, ID> {

    public ExtensibleRepositoryFactoryBean(final Class<? extends R> repositoryInterface) { super(repositoryInterface); }

    @Override
    protected RepositoryFactorySupport createRepositoryFactory(final EntityManager em) {
        return new BaseRepositoryFactory(em);
    }

    private static class BaseRepositoryFactory<T, ID extends Serializable>
            extends JpaRepositoryFactory {

        private final EntityManager em;

        public BaseRepositoryFactory(final EntityManager em) {
            super(em);
            this.em = em;
        }

        @Override
        protected JpaRepositoryImplementation<?, ?> getTargetRepository(final RepositoryInformation information, final EntityManager entityManager) {
            return new ExtensibleRepositoryImpl<T, ID>((Class<T>) information.getDomainType(), em);
        }

        @Override
        protected Class<?> getRepositoryBaseClass(final RepositoryMetadata metadata) {
            return ExtensibleRepositoryImpl.class;
        }
    }
}
