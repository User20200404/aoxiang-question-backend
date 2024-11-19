package com.npu.aoxiangbackend.dao;

import com.npu.aoxiangbackend.model.Survey;
import com.npu.aoxiangbackend.model.User;
import java.util.List;
import java.util.Optional;

public interface ISurveyDao {
    public Optional<Survey> findSurveyById(String id);

    public Optional<Survey> findSurveyByTitle(String title);

    public List<Survey> findSurveysByUserId(long userId);

    public boolean addSurvey(Survey survey);

    public boolean updateSurvey(Survey survey);

    public boolean deleteSurvey(String id);
}