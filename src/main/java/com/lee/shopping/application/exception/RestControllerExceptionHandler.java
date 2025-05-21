package com.lee.shopping.application.exception;

import com.lee.shopping.application.response.ExceptionResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class RestControllerExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<ExceptionResponse> handleValidationExceptions(WebRequest request, MethodArgumentNotValidException e) {

        ObjectError error = e.getBindingResult().getAllErrors().get(0);

        String fieldName = ((FieldError) error).getField();
        String errorMessage = error.getDefaultMessage();
        String detailMessage = "%s %s".formatted(fieldName, errorMessage);

        return handle(request, ExceptionCode.BAD_REQUEST, detailMessage);
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<ExceptionResponse> handleValidationTypeMismatchExceptions(WebRequest request, MethodArgumentTypeMismatchException e) {

        return handle(request, ExceptionCode.BAD_REQUEST, e.getMessage());
    }


    @ExceptionHandler(ApplicationException.class)
    ResponseEntity<ExceptionResponse> handleApplicationException(WebRequest request, HttpServletResponse response, ApplicationException e) {
        response.setStatus(e.getErrorCode().getStatus().value());
        String formatted=e.getFormattedMessage();
        String detail = formatted!=null ? formatted : e.getMessage();
        return handle(request, e.getErrorCode(), detail);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ResponseEntity<ExceptionResponse> noHandlerFoundException(WebRequest request, Exception e) {
        return handle(request, ExceptionCode.NOT_FOUND, e);
    }
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ResponseEntity<ExceptionResponse> handleException(WebRequest request, Exception e) {
        return handle(request, ExceptionCode.INTERNAL_SERVER_ERROR, e);
    }

    private ResponseEntity<ExceptionResponse> handle(WebRequest request, ExceptionCode exceptionCode, Throwable e) {
        log.error("[EXCEPTION] {}\n request : {}", e.getMessage(), request,e);
        ExceptionResponse response = ExceptionResponse.of(exceptionCode, e);
        return new ResponseEntity<>(response, exceptionCode.getStatus());
    }

    private ResponseEntity<ExceptionResponse> handle(WebRequest request, ExceptionCode exceptionCode, String detail) {
        log.error("[EXCEPTION] {}\n request {}", exceptionCode.getMessage(), request);
        ExceptionResponse response = ExceptionResponse.of(exceptionCode, detail);
        return new ResponseEntity<>(response, exceptionCode.getStatus());
    }
}
