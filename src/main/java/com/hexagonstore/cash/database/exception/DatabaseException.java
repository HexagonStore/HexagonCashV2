package com.hexagonstore.cash.database.exception;

public class DatabaseException extends Exception {

    private static final long serialVersionUID = 1871171981695012342L;

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}