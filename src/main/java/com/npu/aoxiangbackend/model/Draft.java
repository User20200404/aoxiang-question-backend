package com.npu.aoxiangbackend.model;

import lombok.Data;
import java.time.ZonedDateTime;

@Data
public class Draft {
    private String id;
    private String surveyId;
    private String data;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}