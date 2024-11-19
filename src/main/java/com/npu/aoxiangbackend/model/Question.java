package com.npu.aoxiangbackend.model;

import lombok.Data;
import java.time.ZonedDateTime;

@Data
public class Question {
    private String id;
    private String surveyId;
    private String type;
    private String content;
    private int orderIndex;
    private boolean required;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}