package com.recipe.config.spring.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;

/**
 * ExtendedSpecification is a fork of org.springframework.data.jpa.domain.Specification
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
 * @see org.springframework.data.jpa.domain.SpecificationComposition#toPredicate(Specification, Root, CriteriaQuery, CriteriaBuilder)
 * @see org.springframework.data.jpa.domain.Specification#toPredicate(Root, CriteriaQuery, CriteriaBuilder)
 *
 * @see com.recipe.config.spring.repository.specification.ExtendedSpecification#toPredicate(Root, CriteriaBuilder)
 */
public interface ExtendedSpecification<T> extends Serializable {
    long serialVersionUID = 1L;

    static <T> ExtendedSpecification<T> not(@Nullable final ExtendedSpecification<T> spec) {
        return spec == null ? (root, builder) -> null : (root, builder) -> builder.not(spec.toPredicate(root, builder));
    }

    static <T> ExtendedSpecification<T> where(@Nullable final ExtendedSpecification<T> spec) {
        return spec == null ? (root, builder) -> null : spec;
    }

    default ExtendedSpecification<T> and(@Nullable ExtendedSpecification<T> other) {
        return ExtendedSpecificationComposition.composed(this, other, CriteriaBuilder::and);
    }

    default ExtendedSpecification<T> or(@Nullable ExtendedSpecification<T> other) {
        return ExtendedSpecificationComposition.composed(this, other, CriteriaBuilder::or);
    }

    @Nullable
    Predicate toPredicate(Root<T> root, CriteriaBuilder criteriaBuilder);
}
