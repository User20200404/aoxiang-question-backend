package com.npu.aoxiangbackend.dao;

import com.npu.aoxiangbackend.model.Survey;
import com.npu.aoxiangbackend.model.User;
import java.util.List;
import java.util.Optional;

public interface ISurveyDao {
    public Optional<Survey> findSurveyById(String id);

    public Optional<Survey> findSurveyByTitle(String title);

    public List<Survey> findSurveysByUserId(long userId);

    /**
     * 将指定的问卷对象添加到数据库，返回自动创建的主键uuid。
     * @param survey 问卷对象。
     * @return 问卷的uuid。
     */
    public String addSurvey(Survey survey);

    public boolean updateSurvey(Survey survey);

    public boolean deleteSurvey(String id);
}