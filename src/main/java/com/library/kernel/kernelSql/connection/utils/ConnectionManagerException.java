package com.library.kernel.kernelSql.connection.utils;

public class ConnectionManagerException extends Exception {
    public ConnectionManagerException(String message) {
        super(message);
    }
    public ConnectionManagerException(String message, Throwable cause) {
        super(message, cause);
    }
}
