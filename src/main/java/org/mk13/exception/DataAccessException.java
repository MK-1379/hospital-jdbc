package org.mk13.exception;

public class DataAccessException extends RuntimeException{
    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
