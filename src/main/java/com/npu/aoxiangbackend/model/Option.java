package com.npu.aoxiangbackend.model;

import lombok.Data;
import java.time.ZonedDateTime;

@Data
public class Option {
    private String id;
    private String questionId;
    private String content;
    private boolean isCorrect;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}