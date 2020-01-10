package com.antzuhl.zeus.exception;


import com.antzuhl.zeus.enums.StatusEnum;

public class ZeusException extends GenericException {

    public ZeusException(int errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public ZeusException(Exception e, int errorCode, String errorMessage) {
        super(e, errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public ZeusException(String message) {
        super(message);
        this.errorMessage = message;
    }

    public ZeusException(StatusEnum statusEnum) {
        super(statusEnum.getMessage());
        this.errorMessage = statusEnum.message();
        this.errorCode = statusEnum.getCode();
    }

    public ZeusException(StatusEnum statusEnum, String message) {
        super(message);
        this.errorMessage = message;
        this.errorCode = statusEnum.getCode();
    }

    public ZeusException(Exception oriEx) {
        super(oriEx);
    }

    public ZeusException(Throwable oriEx) {
        super(oriEx);
    }

    public ZeusException(String message, Exception oriEx) {
        super(message, oriEx);
        this.errorMessage = message;
    }

    public ZeusException(String message, Throwable oriEx) {
        super(message, oriEx);
        this.errorMessage = message;
    }

    public static boolean isResetByPeer(String msg) {
        if ("Connection reset by peer".equals(msg)) {
            return true;
        }
        return false;
    }

}
