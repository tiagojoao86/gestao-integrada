package br.com.grupopipa.gestaointegrada.core.exception.beanvalidation;

import java.util.Set;
import java.util.stream.Collectors;

public class BeanValidationException extends RuntimeException {

    private final Set<BeanValidationMessage> violations;

    public BeanValidationException(Set<BeanValidationMessage> violations) {
        super(buildMessage(violations));
        this.violations = violations;
    }

    private static String buildMessage(Set<BeanValidationMessage> violations) {
        return violations.stream()
                .map(v -> String.format("'%s': %s", v.getKey(),
                        v.getMessage()))
                .collect(Collectors.joining(", "));
    }

    public Set<BeanValidationMessage> getViolations() {
        return violations;
    }
}