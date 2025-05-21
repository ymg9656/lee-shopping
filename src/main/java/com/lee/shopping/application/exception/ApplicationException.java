package com.lee.shopping.application.exception;

public class ApplicationException extends RuntimeException {

    private final ExceptionCode exceptionCode;
    private final String formattedMessage;
    public ApplicationException(ExceptionCode exceptionCode) {
        super();
        this.exceptionCode = exceptionCode;
        this.formattedMessage=null;
    }

    public ApplicationException(ExceptionCode exceptionCode, Object... args) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
        this.formattedMessage = String.format(exceptionCode.getMessageFormat(), args);

    }

    public ApplicationException(ExceptionCode exceptionCode, String message, Throwable cause) {
        super(message, cause);
        this.exceptionCode = exceptionCode;
        this.formattedMessage = null;
    }

    public ApplicationException(ExceptionCode exceptionCode, Throwable cause) {
        super(cause);
        this.exceptionCode = exceptionCode;
        this.formattedMessage = null;
    }

    public ExceptionCode getErrorCode(){
        return exceptionCode;
    }
    public String getFormattedMessage(){
        return formattedMessage;
    }

}
