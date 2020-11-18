package com.recipe.config.spring.jpa.support;

import com.recipe.config.spring.jpa.specification.ExtensibleSpecification;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.convert.QueryByExamplePredicateBuilder;
import org.springframework.data.jpa.provider.PersistenceProvider;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.query.EscapeCharacter;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.jpa.repository.support.*;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.data.util.ProxyUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.util.*;

@Repository
@Transactional(readOnly=true)
public class ExtendedJpaRepositoryImpl<T, ID> implements ExtendedJpaRepository<T, ID> {
    private final JpaEntityInformation<T, ?> entityInformation;
    private final EntityManager em;
    private final PersistenceProvider provider;
    @Nullable
    private CrudMethodMetadata metadata;
    private EscapeCharacter escapeCharacter;

    private static <T> Collection<T> toCollection(Iterable<T> iterable) {
        if (iterable instanceof Collection) {
            return (Collection)iterable;
        } else {
            final List<T> collection = new ArrayList();
            final Iterator it = iterable.iterator();

            while(it.hasNext())
                collection.add((T) it.next());

            return collection;
        }
    }

    public ExtendedJpaRepositoryImpl(final JpaEntityInformation<T, ?> entityInformation, final EntityManager entityManager) {
        this.escapeCharacter = EscapeCharacter.DEFAULT;

        Assert.notNull(entityInformation, "JpaEntityInformation must not be null!");
        Assert.notNull(entityManager, "EntityManager must not be null!");

        this.entityInformation = entityInformation;
        this.em = entityManager;
        this.provider = PersistenceProvider.fromEntityManager(entityManager);
    }

    public ExtendedJpaRepositoryImpl(final Class<T> domainClass, final EntityManager em) {
        this(JpaEntityInformationSupport.getEntityInformation(domainClass, em), em);
    }

    @Override
    public void setRepositoryMethodMetadata(CrudMethodMetadata crudMethodMetadata) {
        this.metadata = crudMethodMetadata;
    }

    @Override
    public void setEscapeCharacter(EscapeCharacter escapeCharacter) {
        this.escapeCharacter = escapeCharacter;
    }

    @Nullable
    protected CrudMethodMetadata getRepositoryMethodMetadata() {
        return this.metadata;
    }

    protected Class<T> getDomainClass() {
        return this.entityInformation.getJavaType();
    }

    private String getDeleteAllQueryString() {
        return QueryUtils.getQueryString("delete from %s x", this.entityInformation.getEntityName());
    }

    private String getCountQueryString() {
        final String countQuery = String.format("select count(%s) from %s x", this.provider.getCountQueryPlaceholder(), "%s");
        return QueryUtils.getQueryString(countQuery, this.entityInformation.getEntityName());
    }

    @Override
    @Transactional
    public void deleteById(final ID id) {
        Assert.notNull(id, "The given id must not be null!");
        this.delete(this.findById(id).orElseThrow(() -> new EmptyResultDataAccessException(String.format("No %s entity with id %s exists!", this.entityInformation.getJavaType(), id), 1)));
    }

    @Override
    @Transactional
    public void delete(final T entity) {
        Assert.notNull(entity, "Entity must not be null!");
        if (!this.entityInformation.isNew(entity)) {
            final Class<?> type = ProxyUtils.getUserClass(entity);
            final T existing = this.em.find((Class<T>) type, this.entityInformation.getId(entity));
            if (existing != null)
                this.em.remove(this.em.contains(entity) ? entity : this.em.merge(entity));
        }
    }

    @Override
    @Transactional
    public void deleteAll(final Iterable<? extends T> entities) {
        final IterableSpecification spec = new IterableSpecification(entities, this.escapeCharacter);
        getDeleteQuery(spec, spec.getProbeType()).executeUpdate();
    }

    @Override
    @Transactional
    public void deleteInBatch(Iterable<T> entities) {
        Assert.notNull(entities, "Entities must not be null!");
        if (entities.iterator().hasNext())
            QueryUtils.applyAndBind(QueryUtils.getQueryString("delete from %s x", this.entityInformation.getEntityName()), entities, this.em).executeUpdate();
    }

    @Override
    @Transactional
    public void deleteAll() {
        final Iterator it = this.findAll().iterator();

        while(it.hasNext()) {
            this.delete((T) it.next());
        }

    }

    @Override
    @Transactional
    public void deleteAllInBatch() {
        this.em.createQuery(this.getDeleteAllQueryString()).executeUpdate();
    }

    @Override
    public Optional<T> findById(ID id) {
        Assert.notNull(id, "The given id must not be null!");
        Class<T> domainType = this.getDomainClass();
        if (this.metadata == null) {
            return Optional.ofNullable(this.em.find(domainType, id));
        } else {
            LockModeType type = this.metadata.getLockModeType();
            Map<String, Object> hints = new HashMap();
            this.getQueryHints().withFetchGraphs(this.em).forEach(hints::put);
            return Optional.ofNullable(type == null ? this.em.find(domainType, id, hints) : this.em.find(domainType, id, type, hints));
        }
    }

    protected QueryHints getQueryHints() {
        return this.metadata == null ? QueryHints.NoHints.INSTANCE : DefaultQueryHints.of(this.entityInformation, this.metadata);
    }

    @Override
    public T getOne(ID id) {
        Assert.notNull(id, "The given id must not be null!");
        return this.em.getReference(this.getDomainClass(), id);
    }

    @Override
    public boolean existsById(ID id) {
        Assert.notNull(id, "The given id must not be null!");
        if (this.entityInformation.getIdAttribute() == null) {
            return this.findById(id).isPresent();
        } else {
            final String placeholder = this.provider.getCountQueryPlaceholder();
            final String entityName = this.entityInformation.getEntityName();
            final Iterable<String> idAttributeNames = this.entityInformation.getIdAttributeNames();
            final String existsQuery = QueryUtils.getExistsQueryString(entityName, placeholder, idAttributeNames);
            final TypedQuery<Long> query = this.em.createQuery(existsQuery, Long.class);
            if (!this.entityInformation.hasCompositeId()) {
                query.setParameter(idAttributeNames.iterator().next(), id);
                return query.getSingleResult() == 1L;
            } else {
                Iterator it = idAttributeNames.iterator();

                while(it.hasNext()) {
                    String idAttributeName = (String)it.next();
                    Object idAttributeValue = this.entityInformation.getCompositeIdAttributeValue(id, idAttributeName);
                    boolean complexIdParameterValueDiscovered = idAttributeValue != null && !query.getParameter(idAttributeName).getParameterType().isAssignableFrom(idAttributeValue.getClass());
                    if (complexIdParameterValueDiscovered) {
                        return this.findById(id).isPresent();
                    }

                    query.setParameter(idAttributeName, idAttributeValue);
                }

                return query.getSingleResult() == 1L;
            }
        }
    }

    @Override
    public List<T> findAll() {
        return this.getSelectQuery((ExtensibleSpecification)null, Sort.unsorted()).getResultList();
    }

    @Override
    public List<T> findAllById(Iterable<ID> ids) {
        Assert.notNull(ids, "Ids must not be null!");

        if (!ids.iterator().hasNext()) {
            return Collections.emptyList();
        } else if (!this.entityInformation.hasCompositeId()) {
            final Collection<ID> idCollection = toCollection(ids);
            final ByIdsSpecification<T> specification = new ByIdsSpecification(this.entityInformation);
            final TypedQuery<T> query = this.getSelectQuery(specification, (Sort)Sort.unsorted());
            return query.setParameter(specification.parameter, idCollection).getResultList();
        } else {
            final List<T> results = new ArrayList();
            final Iterator it = ids.iterator();

            while(it.hasNext())
                this.findById((ID) it.next()).ifPresent(results::add);

            return results;
        }
    }

    @Override
    public List<T> findAll(final Sort sort) {
        return this.getSelectQuery((ExtensibleSpecification)null, sort).getResultList();
    }

    @Override
    public Page<T> findAll(final Pageable pageable) {
        return (Page)(isUnpaged(pageable) ? new PageImpl(this.findAll()) : this.findAll((ExtensibleSpecification)null, pageable));
    }

    @Override
    public Optional<T> findOne(@Nullable final ExtensibleSpecification<T> spec) {
        try {
            return Optional.of(this.getSelectQuery(spec, Sort.unsorted()).getSingleResult());
        } catch (NoResultException var3) {
            return Optional.empty();
        }
    }

    @Override
    public List<T> findAll(@Nullable final ExtensibleSpecification<T> spec) {
        return this.getSelectQuery(spec, Sort.unsorted()).getResultList();
    }

    @Override
    public Page<T> findAll(@Nullable final ExtensibleSpecification<T> spec, final Pageable pageable) {
        final TypedQuery<T> query = this.getSelectQuery(spec, pageable);
        return (Page)(isUnpaged(pageable) ? new PageImpl(query.getResultList()) : this.readPage(query, this.getDomainClass(), pageable, spec));
    }

    @Override
    public List<T> findAll(@Nullable final ExtensibleSpecification<T> spec, final Sort sort) {
        return this.getSelectQuery(spec, sort).getResultList();
    }

    @Override
    public <S extends T> Optional<S> findOne(final Example<S> example) {
        try {
            return (Optional<S>) Optional.of(this.getSelectQuery(new ExampleSpecification(example, this.escapeCharacter), example.getProbeType(), Sort.unsorted()).getSingleResult());
        } catch (NoResultException var3) {
            return Optional.empty();
        }
    }

    @Override
    public <S extends T> long count(final Example<S> example) {
        return executeCountQuery(this.getCountQuery(new ExampleSpecification(example, this.escapeCharacter), example.getProbeType()));
    }

    @Override
    public <S extends T> boolean exists(final Example<S> example) {
        return !this.getSelectQuery(new ExampleSpecification(example, this.escapeCharacter), example.getProbeType(), Sort.unsorted()).getResultList().isEmpty();
    }

    @Override
    public <S extends T> List<S> findAll(final Example<S> example) {
        return this.getSelectQuery(new ExampleSpecification(example, this.escapeCharacter), example.getProbeType(), Sort.unsorted()).getResultList();
    }

    @Override
    public <S extends T> List<S> findAll(final Iterable<T> entities) {
        return this.findAll(entities, Sort.unsorted());
    }

    @Override
    public <S extends T> List<S> findAll(final Iterable<T> entities, final Sort sort) {
        final IterableSpecification<T> spec = new IterableSpecification<>(entities, this.escapeCharacter);
        return (List<S>) this.getSelectQuery(spec, spec.getProbeType(), sort).getResultList();
    }

    @Override
    public <S extends T> List<S> findAll(final Example<S> example, Sort sort) {
        return this.getSelectQuery(new ExampleSpecification(example, this.escapeCharacter), example.getProbeType(), sort).getResultList();
    }

    @Override
    public <S extends T> Page<S> findAll(final Example<S> example, final Pageable pageable) {
        final ExampleSpecification<S> spec = new ExampleSpecification(example, this.escapeCharacter);
        final Class<S> probeType = example.getProbeType();
        final TypedQuery<S> query = this.getSelectQuery(new ExampleSpecification(example, this.escapeCharacter), probeType, pageable);

        return (Page)(isUnpaged(pageable) ? new PageImpl(query.getResultList()) : this.readPage(query, probeType, pageable, spec));
    }

    @Override
    public long count() {
        return this.em.createQuery(this.getCountQueryString(), Long.class).getSingleResult();
    }

    @Override
    public long count(@Nullable final ExtensibleSpecification<T> spec) {
        return executeCountQuery(this.getCountQuery(spec, this.getDomainClass()));
    }

    @Override
    @Transactional
    public <S extends T> S save(final S entity) {
        Assert.notNull(entity, "Entity must not be null.");

        if (this.entityInformation.isNew(entity)) {
            this.em.persist(entity);
            return entity;
        }

        return this.em.merge(entity);
    }

    @Override
    @Transactional
    public <S extends T> S saveAndFlush(final S entity) {
        S result = this.save(entity);
        this.flush();
        return result;
    }

    @Override
    @Transactional
    public <S extends T> List<S> saveAll(final Iterable<S> entities) {
        Assert.notNull(entities, "Entities must not be null!");

        final List<S> result = new ArrayList();
        final Iterator it = entities.iterator();

        while(it.hasNext())
            result.add(this.save((S) it.next()));

        return result;
    }

    @Override
    @Transactional
    public void flush() {
        this.em.flush();
    }

    /** @deprecated */
    @Deprecated
    protected Page<T> readPage(final TypedQuery<T> query, final Pageable pageable, @Nullable final ExtensibleSpecification<T> spec) {
        return this.readPage(query, this.getDomainClass(), pageable, spec);
    }

    protected <S extends T> Page<S> readPage(final TypedQuery<S> query, final Class<S> domainClass, final Pageable pageable, @Nullable final ExtensibleSpecification<S> spec) {
        if (pageable.isPaged()) {
            query.setFirstResult((int)pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());
        }

        return PageableExecutionUtils.getPage(query.getResultList(), pageable,
                () -> executeCountQuery(this.getCountQuery(spec, domainClass)));
    }

    // SELECT
    protected TypedQuery<T> getSelectQuery(@Nullable final ExtensibleSpecification<T> spec, final Pageable pageable) {
        Sort sort = pageable.isPaged() ? pageable.getSort() : Sort.unsorted();
        return this.getSelectQuery(spec, this.getDomainClass(), sort);
    }

    protected <S extends T> TypedQuery<S> getSelectQuery(@Nullable final ExtensibleSpecification<S> spec, final Class<S> domainClass, final Pageable pageable) {
        Sort sort = pageable.isPaged() ? pageable.getSort() : Sort.unsorted();
        return this.getSelectQuery(spec, domainClass, sort);
    }

    protected TypedQuery<T> getSelectQuery(@Nullable final ExtensibleSpecification<T> spec, final Sort sort) {
        return this.getSelectQuery(spec, this.getDomainClass(), sort);
    }

    protected <S extends T> TypedQuery<S> getSelectQuery(@Nullable final ExtensibleSpecification<S> spec, final Class<S> domainClass, final Sort sort) {
        final CriteriaBuilder builder = this.em.getCriteriaBuilder();
        final CriteriaQuery<S> query = builder.createQuery(domainClass);
        final Root<S> root = this.applySpecificationToCriteria(spec, domainClass, query);

        query.select(root);

        if (sort.isSorted()) {
            query.orderBy(QueryUtils.toOrders(sort, root, builder));
        }

        return this.applyRepositoryMethodMetadata(this.em.createQuery(query));
    }

    // DELETE
    protected <S extends T> Query getDeleteQuery(@Nullable final ExtensibleSpecification<S> spec, final Class<S> domainClass) {
        final CriteriaBuilder builder = this.em.getCriteriaBuilder();
        final CriteriaDelete<S> delete = builder.createCriteriaDelete(domainClass);
        final Root<S> root = delete.from(domainClass);

        delete.where(spec.toPredicate(root, builder));

        return this.em.createQuery(delete);
    }

    /** @deprecated */
    @Deprecated
    protected TypedQuery<Long> getCountQuery(@Nullable final ExtensibleSpecification<T> spec) {
        return this.getCountQuery(spec, this.getDomainClass());
    }

    protected <S extends T> TypedQuery<Long> getCountQuery(@Nullable final ExtensibleSpecification<S> spec, final Class<S> domainClass) {
        final CriteriaBuilder builder = this.em.getCriteriaBuilder();
        final CriteriaQuery<Long> query = builder.createQuery(Long.class);
        final Root<S> root = this.applySpecificationToCriteria(spec, domainClass, query);

        query.select(query.isDistinct() ? builder.countDistinct(root) : builder.count(root)).orderBy(Collections.emptyList());

        return this.em.createQuery(query);
    }

    private <S, U extends T> Root<U> applySpecificationToCriteria(@Nullable final ExtensibleSpecification<U> spec, final Class<U> domainClass, final CriteriaQuery<S> query) {
        Assert.notNull(domainClass, "Domain class must not be null!");
        Assert.notNull(query, "CriteriaQuery must not be null!");
        final Root<U> root = query.from(domainClass);

        if (spec == null) {
            return root;
        } else {
            CriteriaBuilder builder = this.em.getCriteriaBuilder();
            Predicate predicate = spec.toPredicate(root, builder);
            if (predicate != null) {
                query.where(predicate);
            }

            return root;
        }
    }

    private <S> TypedQuery<S> applyRepositoryMethodMetadata(final TypedQuery<S> query) {
        if (this.metadata == null) {
            return query;
        } else {
            final LockModeType type = this.metadata.getLockModeType();
            final TypedQuery<S> toReturn = type == null ? query : query.setLockMode(type);
            this.applyQueryHints(toReturn);
            return toReturn;
        }
    }

    private void applyQueryHints(final Query query) { this.getQueryHints().withFetchGraphs(this.em).forEach(query::setHint); }

    private static long executeCountQuery(final TypedQuery<Long> query) {
        Assert.notNull(query, "TypedQuery must not be null!");
        final List<Long> totals = query.getResultList();
        long total = 0L;

        Long element;
        for(Iterator var4 = totals.iterator(); var4.hasNext(); total += element == null ? 0L : element) {
            element = (Long)var4.next();
        }

        return total;
    }

    private static boolean isUnpaged(Pageable pageable) {
        return pageable.isUnpaged();
    }

    private static class ExampleSpecification<T> implements ExtensibleSpecification<T> {
        private static final long serialVersionUID = 1L;
        private final Example<T> example;
        private final EscapeCharacter escapeCharacter;

        ExampleSpecification(final Example<T> example, final EscapeCharacter escapeCharacter) {
            Assert.notNull(example, "Example must not be null!");
            Assert.notNull(escapeCharacter, "EscapeCharacter must not be null!");
            this.example = example;
            this.escapeCharacter = escapeCharacter;
        }

        @Override
        public Predicate toPredicate(final Root<T> root, final CriteriaBuilder cb) {
            return QueryByExamplePredicateBuilder.getPredicate(root, cb, this.example, this.escapeCharacter);
        }
    }

    private static class IterableSpecification<T> implements ExtensibleSpecification<T> {
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

        @Override
        public Predicate toPredicate(final Root<T> root, final CriteriaBuilder cb) {
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

    private static final class ByIdsSpecification<T> implements ExtensibleSpecification<T> {
        private static final long serialVersionUID = 1L;
        private final JpaEntityInformation<T, ?> entityInformation;
        private @Nullable ParameterExpression<Collection> parameter;

        ByIdsSpecification(final JpaEntityInformation<T, ?> entityInformation) {
            this.entityInformation = entityInformation;
        }

        public Predicate toPredicate(final Root<T> root, final CriteriaBuilder cb) {
            Path<?> path = root.get(this.entityInformation.getIdAttribute());
            this.parameter = cb.parameter(Collection.class);
            return path.in(this.parameter);
        }
    }
}