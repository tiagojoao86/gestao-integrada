package br.com.grupopipa.gestaointegrada.core.exception;

import java.util.UUID;

public class EntidadeNaoEncontradaException extends RuntimeException {

    public EntidadeNaoEncontradaException(String className, UUID id) {
        super(String.format("Não foi possível encontrar a entidade '%s' com o id '%s'", className, id));
    }

}
