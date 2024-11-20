package com.npu.aoxiangbackend.exception.internal;

public class InternalException extends Exception {
    protected static final String defaultMessage = "发生内部错误。";

    public InternalException(String message) {
        super(message);
    }

    public InternalException(String message, Throwable cause) {
        super(message, cause);
    }

    public InternalException(Throwable cause) {
        this(defaultMessage, cause);
    }

    public InternalException() {
        this(defaultMessage);
    }
}
