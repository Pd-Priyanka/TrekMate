package com.trekmate.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.data.jpa.domain.Specification;
import com.trekmate.dto.TrekSearchCriteria;
import com.trekmate.entity.Trek;

public final class TrekSpecifications {
    private TrekSpecifications() {
    }

    public static Specification<Trek> withCriteria(TrekSearchCriteria criteria) {
        return (root, query, builder) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();
            if (hasText(criteria.keyword())) {
                String keyword = "%" + criteria.keyword().trim().toLowerCase(Locale.ROOT) + "%";
                predicates.add(builder.or(
                        builder.like(builder.lower(root.get("name")), keyword),
                        builder.like(builder.lower(root.get("location")), keyword),
                        builder.like(builder.lower(root.get("state")), keyword),
                        builder.like(builder.lower(root.get("country")), keyword),
                        builder.like(builder.lower(root.get("description")), keyword)));
            }
            if (hasText(criteria.state()))
                predicates.add(builder.equal(builder.lower(root.get("state")), criteria.state().trim().toLowerCase(Locale.ROOT)));
            if (criteria.difficulty() != null)
                predicates.add(builder.equal(root.get("difficulty"), criteria.difficulty()));
            if (hasText(criteria.season()))
                predicates.add(builder.like(builder.lower(root.get("bestSeason")), "%" + criteria.season().trim().toLowerCase(Locale.ROOT) + "%"));
            if (criteria.minDurationDays() != null)
                predicates.add(builder.greaterThanOrEqualTo(root.get("durationDays"), criteria.minDurationDays()));
            if (criteria.maxDurationDays() != null)
                predicates.add(builder.lessThanOrEqualTo(root.get("durationDays"), criteria.maxDurationDays()));
            return builder.and(predicates.toArray(jakarta.persistence.criteria.Predicate[]::new));
        };
    }

    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
