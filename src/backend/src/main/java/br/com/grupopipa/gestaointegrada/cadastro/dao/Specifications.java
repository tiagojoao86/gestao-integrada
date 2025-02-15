package br.com.grupopipa.gestaointegrada.cadastro.dao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import br.com.grupopipa.gestaointegrada.core.dto.FilterItemDTO;
import br.com.grupopipa.gestaointegrada.core.entity.BaseEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class Specifications<T extends BaseEntity> {

    public Specification<T> withItem(FilterItemDTO item, Class<T> klazz) {
        return (root, query, criteriaBuilder) -> {
            if (ObjectUtils.isEmpty(item)) {
                return null;
            }
            return buildPredicate(item, criteriaBuilder, root, klazz);
        };
    }

    private Predicate buildPredicate(FilterItemDTO item, CriteriaBuilder criteriaBuilder, Root<T> root,
            Class<T> klazz) {
        switch (item.getOperator()) {
            case EQ:
                return buildPredicateEq(item, criteriaBuilder, root, klazz);
            case NEQ:
                return buildPredicateNeq(item, criteriaBuilder, root, klazz);
            case GT:
                return buildPredicateGt(item, criteriaBuilder, root, klazz);
            case LT:
                return buildPredicateLt(item, criteriaBuilder, root, klazz);
            case GE:
                return buildPredicateGe(item, criteriaBuilder, root, klazz);
            case LE:
                return buildPredicateLe(item, criteriaBuilder, root, klazz);
            case CONTAINS:
                return buildPredicateContains(item, criteriaBuilder, root, klazz);
            case NOT_CONTAINS:
                return buildPredicateNotContains(item, criteriaBuilder, root, klazz);
            case IN:
                return buildPredicateIn(item, criteriaBuilder, root, klazz);
            case NOT_IN:
                return buildPredicateNotIn(item, criteriaBuilder, root, klazz);
            case BT:
                return buildPredicateBetween(item, criteriaBuilder, root, klazz);
            default:
                return buildPredicateEq(item, criteriaBuilder, root, klazz);
        }
    }

    private Predicate buildPredicateEq(FilterItemDTO item, CriteriaBuilder criteriaBuilder, Root<T> root,
            Class<T> klazz) {
        Class<?> targetType = getFieldType(klazz, item.getProperty());
        Object value = convertValue(item.getProperty(), item.getValues().get(0), klazz, targetType);

        if (targetType.equals(LocalDate.class) || targetType.equals(LocalDateTime.class)) {
            return criteriaBuilder.between(root.get(item.getProperty()), getStartOfDay(targetType, value),
                    getEndOfDay(targetType, value));
        }

        return criteriaBuilder.equal(root.get(item.getProperty()), value);
    }

    private Predicate buildPredicateNeq(FilterItemDTO item, CriteriaBuilder criteriaBuilder, Root<T> root,
            Class<T> klazz) {
        Class<?> targetType = getFieldType(klazz, item.getProperty());
        Object value = convertValue(item.getProperty(), item.getValues().get(0), klazz, targetType);

        if (targetType.equals(LocalDate.class) || targetType.equals(LocalDateTime.class)) {
            return criteriaBuilder.not(criteriaBuilder.between(root.get(item.getProperty()),
                    getStartOfDay(targetType, value), getEndOfDay(targetType, value)));
        }

        return criteriaBuilder.notEqual(root.get(item.getProperty()), value);
    }

    private Predicate buildPredicateGt(FilterItemDTO item, CriteriaBuilder criteriaBuilder, Root<T> root,
            Class<T> klazz) {
        Class<?> targetType = getFieldType(klazz, item.getProperty());
        Object value = convertValue(item.getProperty(), item.getValues().get(0), klazz, targetType);

        if (targetType.equals(LocalDate.class) || targetType.equals(LocalDateTime.class)) {
            return criteriaBuilder.greaterThan(root.get(item.getProperty()), getEndOfDay(targetType, value));
        }

        return criteriaBuilder.greaterThan(root.get(item.getProperty()), value.toString());
    }

    private Predicate buildPredicateLt(FilterItemDTO item, CriteriaBuilder criteriaBuilder, Root<T> root,
            Class<T> klazz) {
        Class<?> targetType = getFieldType(klazz, item.getProperty());
        Object value = convertValue(item.getProperty(), item.getValues().get(0), klazz, targetType);

        if (targetType.equals(LocalDate.class) || targetType.equals(LocalDateTime.class)) {
            return criteriaBuilder.lessThan(root.get(item.getProperty()), getStartOfDay(targetType, value));
        }

        return criteriaBuilder.lessThan(root.get(item.getProperty()), value.toString());
    }

    private Predicate buildPredicateGe(FilterItemDTO item, CriteriaBuilder criteriaBuilder, Root<T> root,
            Class<T> klazz) {
        Class<?> targetType = getFieldType(klazz, item.getProperty());
        Object value = convertValue(item.getProperty(), item.getValues().get(0), klazz, targetType);

        if (targetType.equals(LocalDate.class) || targetType.equals(LocalDateTime.class)) {
            return criteriaBuilder.greaterThanOrEqualTo(root.get(item.getProperty()), getStartOfDay(targetType, value));
        }

        return criteriaBuilder.greaterThanOrEqualTo(root.get(item.getProperty()), value.toString());
    }

    private Predicate buildPredicateLe(FilterItemDTO item, CriteriaBuilder criteriaBuilder, Root<T> root,
            Class<T> klazz) {
        Class<?> targetType = getFieldType(klazz, item.getProperty());
        Object value = convertValue(item.getProperty(), item.getValues().get(0), klazz, targetType);

        if (targetType.equals(LocalDate.class) || targetType.equals(LocalDateTime.class)) {
            return criteriaBuilder.lessThanOrEqualTo(root.get(item.getProperty()), getEndOfDay(targetType, value));
        }

        return criteriaBuilder.lessThanOrEqualTo(root.get(item.getProperty()), value.toString());
    }

    private Predicate buildPredicateContains(FilterItemDTO item, CriteriaBuilder criteriaBuilder, Root<T> root,
            Class<T> klazz) {
        Class<?> targetType = getFieldType(klazz, item.getProperty());
        Object value = convertValue(item.getProperty(), item.getValues().get(0), klazz, targetType);

        return criteriaBuilder.like(criteriaBuilder.lower(root.get(item.getProperty())),
                "%" + value.toString().toLowerCase() + "%");
    }

    private Predicate buildPredicateNotContains(FilterItemDTO item, CriteriaBuilder criteriaBuilder, Root<T> root,
            Class<T> klazz) {
        Class<?> targetType = getFieldType(klazz, item.getProperty());
        Object value = convertValue(item.getProperty(), item.getValues().get(0), klazz, targetType);

        return criteriaBuilder.notLike(criteriaBuilder.lower(root.get(item.getProperty())),
                "%" + value.toString().toLowerCase() + "%");
    }

    private Predicate buildPredicateIn(FilterItemDTO item, CriteriaBuilder criteriaBuilder, Root<T> root,
            Class<T> klazz) {
        return root.get(item.getProperty()).in(item.getValues().stream().map(Object::toString).toList());
    }

    private Predicate buildPredicateNotIn(FilterItemDTO item, CriteriaBuilder criteriaBuilder, Root<T> root,
            Class<T> klazz) {
        return criteriaBuilder
                .not(root.get(item.getProperty()).in(item.getValues().stream().map(Object::toString).toList()));
    }

    private Predicate buildPredicateBetween(FilterItemDTO item, CriteriaBuilder criteriaBuilder, Root<T> root,
            Class<T> klazz) {
        if (ObjectUtils.isEmpty(item.getValues()) || item.getValues().size() < 2) {
            return null;
        }

        Class<?> targetType = getFieldType(klazz, item.getProperty());
        Object value1 = convertValue(item.getProperty(), item.getValues().get(0), klazz, targetType);
        Object value2 = convertValue(item.getProperty(), item.getValues().get(1), klazz, targetType);

        if (targetType.equals(LocalDate.class) || targetType.equals(LocalDateTime.class)) {
            return criteriaBuilder.between(root.get(item.getProperty()), getStartOfDay(targetType, value1),
                    getEndOfDay(targetType, value2));
        }

        return criteriaBuilder.between(root.get(item.getProperty()), value1.toString(), value2.toString());
    }

    private LocalDateTime getStartOfDay(Class<?> targetType, Object value) {
        LocalDateTime start = null;
        if (targetType.equals(LocalDateTime.class)) {
            start = ((LocalDateTime) value).toLocalDate().atStartOfDay();
        }

        if (targetType.equals(LocalDate.class)) {
            start = ((LocalDate) value).atStartOfDay();
        }

        return start;
    }

    private LocalDateTime getEndOfDay(Class<?> targetType, Object value) {
        LocalDateTime end = null;
        if (targetType.equals(LocalDateTime.class)) {
            end = ((LocalDateTime) value).toLocalDate().atTime(LocalTime.MAX);
        }

        if (targetType.equals(LocalDate.class)) {
            end = ((LocalDate) value).atTime(LocalTime.MAX);
        }

        return end;
    }

    private Object convertValue(String property, Object value, Class<T> klazz, Class<?> targetType) {
        if (value == null) {
            return null;
        }

        String stringValue = value.toString();
        try {
            if (targetType.equals(Integer.class) || targetType.equals(int.class)) {
                return Integer.parseInt(stringValue);
            } else if (targetType.equals(Long.class) || targetType.equals(long.class)) {
                return Long.parseLong(stringValue);
            } else if (targetType.equals(Double.class) || targetType.equals(double.class)) {
                return Double.parseDouble(stringValue);
            } else if (targetType.equals(Boolean.class) || targetType.equals(boolean.class)) {
                return Boolean.parseBoolean(stringValue);
            } else if (targetType.equals(LocalDate.class)) {
                return LocalDate.parse(stringValue, DateTimeFormatter.ISO_LOCAL_DATE);
            } else if (targetType.equals(LocalDateTime.class)) {
                return LocalDateTime.parse(stringValue, DateTimeFormatter.ISO_DATE_TIME);
            } else if (targetType.equals(String.class)) {
                return stringValue;
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Erro ao converter valor: " + value + " para " + targetType.getSimpleName(), e);
        }

        return value;
    }

    private Class<?> getFieldType(Class<?> klazz, String fieldName) {
        try {
            return klazz.getDeclaredField(fieldName).getType();
        } catch (NoSuchFieldException e) {
            if (klazz == BaseEntity.class) {
                throw new RuntimeException("Campo não encontrado: " + fieldName);
            }

            return getFieldType(BaseEntity.class, fieldName);
        }
    }

}
