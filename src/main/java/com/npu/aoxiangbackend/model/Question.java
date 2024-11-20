package com.npu.aoxiangbackend.model;

import lombok.Data;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "questions")
@Data
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private String id;

    @JoinColumn(name = "survey_id", nullable = false, referencedColumnName = "id")
    @ManyToOne
    private Survey sourceSurvey;

    @Column(nullable = false)
    private String type;

    private String content;
    @Column(nullable = false)
    private int orderIndex;
    @Column(nullable = false)
    private boolean required;

    @Column(nullable = false)
    private ZonedDateTime createdAt;
    @Column(nullable = false)
    private ZonedDateTime updatedAt;
}