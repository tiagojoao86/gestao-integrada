package br.com.grupopipa.gestaointegrada.core.controller;

import br.com.grupopipa.gestaointegrada.core.exception.EntityNotFoundException;
import br.com.grupopipa.gestaointegrada.core.exception.beanvalidation.BeanValidationException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String INVALID_DATA = "Invalid Data";
    private static final String RESOURCE_NOT_FOUND = "Resource not found";
    private static final String INTERNAL_SERVER_ERROR = "Internal server error";
    private static final String UNEXPECTED_ERROR_DETAIL = "An unexpected internal system error has occurred.";

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntidadeNaoEncontrada(EntityNotFoundException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        String title = RESOURCE_NOT_FOUND;
        String detail = ex.getMessage();

        ApiError apiError = ApiError.builder()
                .status(status.value())
                .timestamp(OffsetDateTime.now())
                .title(title)
                .userMessageKey(List.of(ErrorKeys.RESOURCE_NOT_FOUND))
                .detail(List.of(detail))
                .build();

        log.error(title + ": " + detail);

        return handleExceptionInternal(ex, apiError, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(BeanValidationException.class)
    public ResponseEntity<Object> handleBeanValidationException(BeanValidationException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String title = INVALID_DATA;
        String detail = ex.getViolations().stream()
                .map(v -> String.format("'%s': %s", v.getKey(), v.getMessage()))
                .collect(Collectors.joining(",\n"));

        List<ApiError.Field> fields = ex.getViolations().stream()
                .map(v -> {
                    String key = v.getKey();
                    String name = key.contains(".") ? key.substring(0, key.indexOf(".")) : key;
                    String userMessageKey = StringUtils.hasText(ex.getEntityName())
                            ? ex.getEntityName() + "." + key
                            : key;

                    return ApiError.Field.builder()
                            .name(name)
                            .userMessageKey(userMessageKey)
                            .build();
                })
                .toList();

        ApiError apiError = ApiError.builder()
                .status(status.value())
                .timestamp(OffsetDateTime.now())
                .title(title)
                .fields(fields)
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
                .detail(List.of(detail))
                .build();

        log.error(title + ": " + detail);

        return handleExceptionInternal(ex, apiError, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String title = INTERNAL_SERVER_ERROR;
        String detail = UNEXPECTED_ERROR_DETAIL;

        log.error("Unexpected internal error: ", ex);

        ApiError apiError = ApiError.builder()
                .status(status.value()).timestamp(OffsetDateTime.now()).title(title)
                .userMessageKey(List.of(ErrorKeys.INTERNAL_SERVER_ERROR)).detail(List.of(detail)).build();

        return handleExceptionInternal(ex, apiError, new HttpHeaders(), status, request);
    }
}
