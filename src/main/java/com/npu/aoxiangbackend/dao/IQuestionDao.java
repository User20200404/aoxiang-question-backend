package com.npu.aoxiangbackend.dao;

import com.npu.aoxiangbackend.model.Question;

import java.util.List;
import java.util.Optional;

public interface IQuestionDao {
    long addQuestion(Question question);

    Optional<Question> findQuestionById(String id);

    List<Question> findQuestionsBySurveyId(String surveyId);

    void deleteQuestion(String id);

    void updateQuestion(Question question);
}
