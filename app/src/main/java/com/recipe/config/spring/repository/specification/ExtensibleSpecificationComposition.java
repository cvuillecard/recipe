package com.recipe.config.spring.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;

/**
 * ExtendedSpecificationComposition is a fork of org.springframework.data.jpa.domain.SpecificationComposition
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
class ExtendedSpecificationComposition {
    ExtendedSpecificationComposition() {}

    static <T> ExtendedSpecification<T> composed(@Nullable final ExtendedSpecification<T> lhs, @Nullable final ExtendedSpecification<T> rhs, final ExtendedSpecificationComposition.Combiner combiner) {
        return (root, builder) -> {
            final Predicate otherPredicate = toPredicate(lhs, root, builder);
            final Predicate thisPredicate = toPredicate(rhs, root, builder);

            return thisPredicate == null ? otherPredicate
                    : (otherPredicate == null ? thisPredicate : combiner.combine(builder, thisPredicate, otherPredicate));
        };
    }

    @Nullable
    private static <T> Predicate toPredicate(@Nullable final ExtendedSpecification<T> spec, final Root<T> root, final CriteriaBuilder cb) {
        return spec == null ? null : spec.toPredicate(root, cb);
    }

    interface Combiner extends Serializable {
        Predicate combine(final CriteriaBuilder criteriaBuilder, @Nullable final Predicate predicate, @Nullable final Predicate otherPredicate);
    }
}