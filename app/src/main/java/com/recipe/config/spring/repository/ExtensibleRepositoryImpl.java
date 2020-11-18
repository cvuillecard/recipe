package com.recipe.config.spring.repository;

import com.recipe.config.spring.jpa.support.ExtendedJpaRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

public class ExtensibleRepositoryImpl<T, ID extends Serializable> extends ExtendedJpaRepositoryImpl<T, ID> implements ExtensibleRepository<T, ID> {
    private static final Logger LOG = LoggerFactory.getLogger(ExtensibleRepositoryImpl.class);
    private final EntityManager em;

    public ExtensibleRepositoryImpl(final Class<T> domainClass, final EntityManager em) {
        super(domainClass, em);
        this.em = em;
    }


//    protected <S extends T> TypedQuery<S> getDeleteQuery(@Nullable IterableSpecification<S> spec, Class<S> domainClass, Sort sort) {
//        CriteriaBuilder builder = this.em.getCriteriaBuilder();
//        CriteriaDelete<S> delete = builder.createCriteriaDelete(domainClass);
//        Root<S> root = delete.from(domainClass);
//        // Actual spring implementation doesn't allow the use of CriteriaDelete in interface Specification :
//        // method Specification.toPredicate() doesn't provide this type of parameter so we hard code the implementation in the method to
//        // avoid impact on the spring actual implementation
//        Predicate where = spec.toPredicate(root, delete, builder);
//        delete.select(root);
//        if (sort.isSorted()) {
//            delete.orderBy(QueryUtils.toOrders(sort, root, builder));
//        }
//
//        return this.applyRepositoryMethodMetadata(this.em.createQuery(delete));
//    }

    @Override
    public void detach(final T bean) {
        em.detach(bean);
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
