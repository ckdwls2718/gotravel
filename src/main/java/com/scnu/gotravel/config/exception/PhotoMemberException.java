package com.scnu.gotravel.config.exception;

public class PhotoMemberException extends RuntimeException{
    public PhotoMemberException() {
    }

    public PhotoMemberException(String message) {
        super(message);
    }

    public PhotoMemberException(String message, Throwable cause) {
        super(message, cause);
    }

}
