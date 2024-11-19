package com.npu.aoxiangbackend.model;

import lombok.Data;
import java.time.ZonedDateTime;

@Data
public class Response {
    private String id;
    private String surveyId;
    private String userId;
    private ZonedDateTime completedAt;
    private String ipAddress;
    private ZonedDateTime createdAt;
}