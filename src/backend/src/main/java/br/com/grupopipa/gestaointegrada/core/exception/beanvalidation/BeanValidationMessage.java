package br.com.grupopipa.gestaointegrada.core.exception.beanvalidation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BeanValidationMessage {

    private String key;
    private String message;
    
}
