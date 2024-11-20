package com.npu.aoxiangbackend.exception.business;

public class UserServiceException extends BusinessException {
    public UserServiceException(String message) {
        super(message);
    }

    public UserServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
