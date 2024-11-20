package com.npu.aoxiangbackend.exception.business;

public class SurveyServiceException extends BusinessException {
    public SurveyServiceException(String message) {
        super(message);
    }

    public SurveyServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
