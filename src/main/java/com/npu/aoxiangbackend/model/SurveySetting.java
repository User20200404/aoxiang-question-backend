package com.npu.aoxiangbackend.model;

import lombok.Data;
import java.time.ZonedDateTime;

@Data
public class SurveySetting {
    private long id;
    private String surveyId;
    private String key;
    private String value;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}