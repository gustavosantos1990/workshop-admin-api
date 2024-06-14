package com.gdas.shopadminapi.controllers.rest;

import com.gdas.shopadminapi.dtos.CustomErrorResponse;
import com.gdas.shopadminapi.exceptions.BusinessException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
public class APIExceptionHandler {

    private final MessageSource messageSource;

    public APIExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler({BusinessException.class})
    protected ResponseEntity<CustomErrorResponse> handleBusinessException(BusinessException be) {
        String message = messageSource.getMessage(be.getMessageCode(), be.getArgs(), LocaleContextHolder.getLocale());
        return ResponseEntity.status(be.getStatusCode()).body(new CustomErrorResponse(message));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorResponse> handleArgumentNotValidException(MethodArgumentNotValidException ex) {
        Locale locale = LocaleContextHolder.getLocale();
        BindingResult result = ex.getBindingResult();
        List<String> errors = result.getFieldErrors()
                .stream()
                .map(objectError -> format("%s %s", objectError.getField(), messageSource.getMessage(objectError, locale)))
                .collect(Collectors.toList());
        String title = messageSource.getMessage("validation.error.title", new Object[]{}, locale);
        return new ResponseEntity<>(new CustomErrorResponse(title, errors), BAD_REQUEST);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    protected ResponseEntity<CustomErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        Locale locale = LocaleContextHolder.getLocale();
        List<String> errors = ex.getConstraintViolations().stream().map(err -> format("%s %s", err.getPropertyPath().toString(), err.getMessage())).collect(Collectors.toList());
        String title = messageSource.getMessage("validation.error.title", new Object[]{}, locale);
        return ResponseEntity.status(BAD_REQUEST).body(new CustomErrorResponse(title, errors));
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    protected ResponseEntity<CustomErrorResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        Locale locale = LocaleContextHolder.getLocale();
        String title = messageSource.getMessage("missing.request.parameter", new Object[]{ex.getParameterName()}, locale);
        return ResponseEntity.status(BAD_REQUEST).body(new CustomErrorResponse(title));
    }

    @ExceptionHandler(Throwable.class)
    protected ResponseEntity<CustomErrorResponse> handleThrowable(Throwable throwable) {
        Locale locale = LocaleContextHolder.getLocale();
        String title = messageSource.getMessage("internal.server.error", new Object[]{}, locale);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new CustomErrorResponse(title));
    }


}
