package com.fundooproject.exception;

public class UserException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    public exceptionType type;

    public UserException(exceptionType type) {
        this.type = type;
    }

    public enum exceptionType {
        USER_NOT_FOUND,
        INVALID_EMAIL_ID,
        INVALID_PASSWORD,
        USER_ALREADY_EXIST,
        EMAIL_ALREADY_VERIFIED;
        
    }

}
