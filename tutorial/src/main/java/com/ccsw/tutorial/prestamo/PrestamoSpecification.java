package com.ccsw.tutorial.prestamo;

import com.ccsw.tutorial.common.criteria.SearchCriteria;
import com.ccsw.tutorial.prestamo.model.Prestamo;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class PrestamoSpecification implements Specification<Prestamo> {
    private static final long seralVersionUID = 1L;
    private final SearchCriteria criteria;

    public PrestamoSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Prestamo> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        Path<String> path = getPath(root);

        if (criteria.getOperation().equalsIgnoreCase(":") && criteria.getValue() != null) {

            if (path.getJavaType() == String.class) {
                return builder.like(path, "%" + criteria.getValue() + "%");
            } else {
                return builder.equal(path, criteria.getValue());
            }
        } else if (criteria.getOperation().equalsIgnoreCase("<=") && criteria.getValue() != null) {
            LocalDate date = (LocalDate) criteria.getValue();
            return builder.lessThanOrEqualTo(path.as(LocalDate.class), date);
        } else if (criteria.getOperation().equalsIgnoreCase(">=") && criteria.getValue() != null) {
            LocalDate date = (LocalDate) criteria.getValue();
            return builder.greaterThanOrEqualTo(path.as(LocalDate.class), date);
        }

        return null;
    }

    private Path<String> getPath(Root<Prestamo> root) {
        String key = criteria.getKey();
        String[] split = key.split("[.]", 0);

        Path<String> expression = root.get(split[0]);
        for (int i = 1; i < split.length; i++) {
            expression = expression.get(split[i]);
        }
        return expression;
    }

}
