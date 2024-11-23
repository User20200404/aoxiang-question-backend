package com.npu.aoxiangbackend.exception.business;

public class QuestionServiceException extends BusinessException{
    public QuestionServiceException(String message) {
        super(message);
    }

    public QuestionServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
