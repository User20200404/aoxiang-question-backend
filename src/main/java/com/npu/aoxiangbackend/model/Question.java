package com.npu.aoxiangbackend.model;

import lombok.Data;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "questions")
@Data
public class Question {
    /**
     * 该问题的ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    /**
     * 表示该问题所在问卷。
     */
    @Column(nullable = false)
    private String sourceSurveyId;

    /**
     * 表示问题类型。
     */
    @Column(nullable = false)
    private QuestionType type;

    /**
     * 问题的内容。
     */
    private String content;
    @Column(nullable = false)

    /**
     * 问题在问卷中的索引。
     */
    private int orderIndex;

    /**
     * 是否必填。
     */
    @Column(nullable = false)
    private boolean required;

    /**
     * 创建时间。
     */
    @Column(nullable = false)
    private ZonedDateTime createdAt;

    /**
     * 修改时间。
     */
    @Column(nullable = false)
    private ZonedDateTime updatedAt;
}