package com.lee.shopping.application.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExceptionCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),""),
    NOT_FOUND(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase(),""),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(),""),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "Invalid","Invalid %s"),
    DUPLICATE(HttpStatus.BAD_REQUEST, "Duplicate","Duplicate %s"),
    REQUEST_TIMEOUT(HttpStatus.REQUEST_TIMEOUT, HttpStatus.REQUEST_TIMEOUT.getReasonPhrase(),""),
    TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase(),""),
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase(),""),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.getReasonPhrase(),""),
    FORBIDDEN(HttpStatus.FORBIDDEN, HttpStatus.FORBIDDEN.getReasonPhrase(),"");

    private final HttpStatus status;
    private final String message;
    private final String messageFormat;

}
