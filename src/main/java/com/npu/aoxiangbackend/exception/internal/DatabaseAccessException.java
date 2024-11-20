package com.npu.aoxiangbackend.exception.internal;

public class DatabaseAccessException extends InternalException {
    protected final static String defaultMessage = "访问数据库时发生错误。";

    public DatabaseAccessException(String message) {
        super(message);
    }

    public DatabaseAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatabaseAccessException(Throwable cause) {
        this(defaultMessage, cause);
    }

    public DatabaseAccessException() {
        this(defaultMessage);
    }
}
