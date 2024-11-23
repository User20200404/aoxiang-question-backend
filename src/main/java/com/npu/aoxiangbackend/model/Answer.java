package com.npu.aoxiangbackend.model;

import lombok.Data;
import java.time.ZonedDateTime;

@Data
public class Answer {
    private String id;
    private String responseId;
    private String questionId;
    private String content;
    private ZonedDateTime createdAt;
}