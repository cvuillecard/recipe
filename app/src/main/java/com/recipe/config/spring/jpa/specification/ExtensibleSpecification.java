package com.recipe.config.spring.jpa.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;

/**
 * <pre>
 * ExtensibleSpecification is a fork of org.springframework.data.jpa.domain.Specification
 *
 * Purpose : delete the CriteriaQuery parameter of the default interface methods which blocks the implementation of custom specifications.
 *
 * Reason 1 : because of the method Specification.toPredicate() which only provide a 'CriteriaQuery' type as parameter
 * instead of the CommonAbstractCriteria (the common interface ancestor of CriteriaQuery, CriteriaDelete, and CriteriaUpdate)
 * > Therefore it restricts to read only operations as select in implementations of Specification.toPredicate().
 *
 * Reason 2 : because the parameter 'CriteriaQuery query' is never used in the actual spring implementation
 * of methods org.springframework.data.jpa.domain.SpecificationComposition.toPredicate()
 * which uses org.springframework.data.jpa.domain.Specification.toPredicate()
 *
 * Note : All specification classes implemented in SimpleJpaRepository (which is the default implementation of CrudRepository.class)
 * are inner static and private. So it's hard to extend the specification panel. These specification classes provide read-only operations.
 *
 * </pre>
 *
 * @see org.springframework.data.jpa.domain.SpecificationComposition#toPredicate(Specification, Root, CriteriaQuery, CriteriaBuilder)
 * @see org.springframework.data.jpa.domain.Specification#toPredicate(Root, CriteriaQuery, CriteriaBuilder)
 *
 * @see ExtensibleSpecification#toPredicate(Root, CriteriaBuilder)
 */
public interface ExtensibleSpecification<T> extends Serializable {
    long serialVersionUID = 1L;

    static <T> ExtensibleSpecification<T> not(@Nullable final ExtensibleSpecification<T> spec) {
        return spec == null ? (root, builder) -> null : (root, builder) -> builder.not(spec.toPredicate(root, builder));
    }

    static <T> ExtensibleSpecification<T> where(@Nullable final ExtensibleSpecification<T> spec) {
        return spec == null ? (root, builder) -> null : spec;
    }

    default ExtensibleSpecification<T> and(@Nullable final ExtensibleSpecification<T> other) {
        return ExtensibleSpecificationComposition.composed(this, other, CriteriaBuilder::and);
    }

    default ExtensibleSpecification<T> or(@Nullable final ExtensibleSpecification<T> other) {
        return ExtensibleSpecificationComposition.composed(this, other, CriteriaBuilder::or);
    }

    @Nullable
    Predicate toPredicate(final Root<T> root, final CriteriaBuilder criteriaBuilder);
}
