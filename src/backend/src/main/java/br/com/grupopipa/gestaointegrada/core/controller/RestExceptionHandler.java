package br.com.grupopipa.gestaointegrada.core.controller;

import br.com.grupopipa.gestaointegrada.core.exception.EntityNotFoundException;
import br.com.grupopipa.gestaointegrada.core.exception.beanvalidation.BeanValidationException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.List;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String MSG_ERRO_GENERICA_USUARIO = "Ocorreu um erro interno inesperado no sistema. " +
            "Tente novamente e, se o problema persistir, entre em contato com o administrador do sistema.";

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntidadeNaoEncontrada(EntityNotFoundException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        String title = "Resource not found";
        String detail = ex.getMessage();

        ApiError apiError = ApiError.builder()
                .status(status.value())
                .timestamp(OffsetDateTime.now())
                .title(title)
                .userMessageKey(List.of(ErrorKeys.RESOURCE_NOT_FOUND))
                .detail(detail)
                .build();

        log.error(title + ": " + detail);

        return handleExceptionInternal(ex, apiError, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(BeanValidationException.class)
    public ResponseEntity<Object> handleBeanValidationException(BeanValidationException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        String title = "Resource not found";
        String detail = ex.getMessage();

        ApiError apiError = ApiError.builder()
                .status(status.value())
                .timestamp(OffsetDateTime.now())
                .title(title)
                .userMessageKey(List.of(ErrorKeys.RESOURCE_NOT_FOUND))
                .detail(detail)
                .build();

        log.error(title + ": " + detail);

        return handleExceptionInternal(ex, apiError, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<Object> handleTransactionSystemException(TransactionSystemException ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String title = HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
        String detail = ex.getMessage();
        List<String> userMessageKeys = List.of(ErrorKeys.INTERNAL_SERVER_ERROR);

        if (ex.getCause().getClass().getName().equals("jakarta.persistence.RollbackException") && 
                ex.getCause().getCause().getClass().getName()
                        .equals("jakarta.validation.ConstraintViolationException")) {
            
            ((ConstraintViolationException) ex.getCause().getCause()).getConstraintViolations();
        }

        ApiError apiError = ApiError.builder()
                .status(status.value())
                .timestamp(OffsetDateTime.now())
                .title(title)
                .userMessageKey(userMessageKeys)
                .detail(detail)
                .build();

        log.error(title + ": " + detail);

        return handleExceptionInternal(ex, apiError, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String title = "Internal server error";
        String detail = "An unexpected internal system error has occurred. Please try again. " +
                "If the problem persists, contact the system administrator.";

        log.error("Erro interno não capturado", ex);

        ApiError apiError = ApiError.builder()
                .status(status.value()).timestamp(OffsetDateTime.now()).title(title)
                .userMessageKey(List.of(ErrorKeys.INTERNAL_SERVER_ERROR)).detail(detail).build();

        return handleExceptionInternal(ex, apiError, new HttpHeaders(), status, request);
    }
}
