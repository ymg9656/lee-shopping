package com.lee.shopping.application.response;

import com.lee.shopping.application.exception.ExceptionCode;
import lombok.Getter;

@Getter
public class ExceptionResponse {

    private final int status;
    private final String error;
    private final String message;
    private final String detail;

    ExceptionResponse(ExceptionCode code) {
        this(code, null);
    }

    ExceptionResponse(ExceptionCode code, String detail) {
        this.status = code.getStatus().value();
        this.error = code.getStatus().name();
        this.message = code.getMessage();
        this.detail = detail;
    }

    public static ExceptionResponse of(ExceptionCode code) {
        return new ExceptionResponse(code);
    }

    public static ExceptionResponse of(ExceptionCode code, Throwable e) {
        return new ExceptionResponse(code, e.getMessage());
    }

    public static ExceptionResponse of(ExceptionCode code, String message) {
        return new ExceptionResponse(code, message);
    }

}
