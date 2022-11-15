package com.scnu.gotravel.config.exception;



public class PhotographerExistException extends RuntimeException{
    public PhotographerExistException() {
    }

    public PhotographerExistException(String message) {
        super(message);
    }

    public PhotographerExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
