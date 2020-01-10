package com.antzuhl.zeus.exception;

import java.io.Serializable;

public class GenericException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 1L;
    int errorCode;
    String errorMessage;

    public GenericException() {
    }

    public GenericException(String message) {
        super(message);
    }

    public GenericException(Exception oriEx) {
        super(oriEx);
    }

    public GenericException(Exception oriEx, String message) {
        super(message, oriEx);
    }

    public GenericException(Throwable oriEx) {
        super(oriEx);
    }

    public GenericException(String message, Exception oriEx) {
        super(message, oriEx);
    }

    public GenericException(String message, Throwable oriEx) {
        super(message, oriEx);
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}