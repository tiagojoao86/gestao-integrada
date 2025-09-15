package br.com.grupopipa.gestaointegrada.core.controller;

import br.com.grupopipa.gestaointegrada.core.exception.EntidadeNaoEncontradaException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Response handleEntidadeNaoEncontradaException(EntidadeNaoEncontradaException ex) {
        log.error("Entidade não encontrada: {}", ex.getMessage());
        return Response.notFoundException(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Response handleGeneralException(Exception ex) {
        String errorMessage = String.format("Causa: %s - Mensagem: %s", ex.getCause(), ex.getMessage());
        log.error("Erro interno da aplicação: {}", errorMessage, ex);
        return Response.internalServerError(errorMessage);
    }
}
