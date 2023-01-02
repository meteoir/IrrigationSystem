package com.andela.irrigation_system.persistence.spec;


import lombok.experimental.UtilityClass;
import com.andela.irrigation_system.persistence.entity.IrrigationConfig;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@UtilityClass
public class IrrigationConfigSpec {

    public static Specification<IrrigationConfig> configByNameSpec(Long id, String name) {
        if (Stream.of(id, name).allMatch(Objects::isNull)) {
            return (root, query, cb) -> cb.conjunction();
        } else if (nonNull(id)) {
            return equalSpec("id", id);
        } else if (nonNull(name)) {
            return equalSpec("plotOfLand", "name", name);
        } else {
            return dateAndUserFilter(id, name);
        }
//        return (root, query, cb) -> cb.disjunction();
    }

    private static Specification<IrrigationConfig> dateAndUserFilter(Long id, String name) {
        return (root, query, cb) ->
                cb.and(toPredicates(root, query, cb,
                        equalSpec("id", id),
                        equalSpec("name", name)
                ));
    }

    private static <E, T> Specification<E> equalSpec(String field, T value) {
        return (root, query, builder) -> builder.equal(root.get(field), value);
    }

    private static <E, T> Specification<E> equalSpec(String field, String attr, T value) {
        return (root, query, builder) -> builder.equal(root.get(field).get(attr), value);
    }

    @SafeVarargs
    private static <T> Predicate[] toPredicates(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb, Specification<T>... filters) {
        return Arrays.stream(filters)
                .filter(Objects::nonNull)
                .map(f -> f.toPredicate(root, query, cb))
                .filter(Objects::nonNull)
                .toArray(Predicate[]::new);
    }
}

