package com.npu.aoxiangbackend.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.ZonedDateTime;
import javax.persistence.*;


@Data
@Entity
@Table(name = "responses")
public class Response {
    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    private String id;

    @Column(nullable = false)
    private String surveyId;

    @Column(nullable = false)
    private long userId;

    @Column(nullable = false)
    private ZonedDateTime submittedAt;
}