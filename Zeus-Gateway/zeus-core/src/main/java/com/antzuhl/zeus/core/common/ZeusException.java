package com.antzuhl.zeus.core.common;
/**
 * All handled exceptions in Zeus are ZeusExceptions
 */
public class ZeusException extends Exception {
    public int nStatusCode;
    public String errorCause;

    /**
     * Source Throwable, message, status code and info about the cause
     * @param throwable
     * @param sMessage
     * @param nStatusCode
     * @param errorCause
     */
    public ZeusException(Throwable throwable, String sMessage, int nStatusCode, String errorCause) {
        super(sMessage, throwable);
        this.nStatusCode = nStatusCode;
        this.errorCause = errorCause;
    }

    /**
     * error message, status code and info about the cause
     * @param sMessage
     * @param nStatusCode
     * @param errorCause
     */
    public ZeusException(String sMessage, int nStatusCode, String errorCause) {
        super(sMessage);
        this.nStatusCode = nStatusCode;
        this.errorCause = errorCause;
    }

    /**
     * Source Throwable,  status code and info about the cause
     * @param throwable
     * @param nStatusCode
     * @param errorCause
     */
    public ZeusException(Throwable throwable, int nStatusCode, String errorCause) {
        super(throwable.getMessage(), throwable);
        this.nStatusCode = nStatusCode;
        this.errorCause = errorCause;
    }
}
