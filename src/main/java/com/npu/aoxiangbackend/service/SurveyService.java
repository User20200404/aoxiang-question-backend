package com.npu.aoxiangbackend.service;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.npu.aoxiangbackend.dao.ISurveyDao;
import com.npu.aoxiangbackend.model.Survey;
import com.npu.aoxiangbackend.util.ColoredPrintStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SurveyService {

    private final ISurveyDao surveyDao;
    private final UserService userService;
    private final ColoredPrintStream printer;

    @Autowired
    public SurveyService(ISurveyDao surveyDao, UserService userService, ColoredPrintStream printer) {
        this.surveyDao = surveyDao;
        this.userService = userService;
        this.printer = printer;
    }

    public Optional<List<Survey>> getSurveysByToken(String tokenValue) {
        var token = StpUtil.getLoginIdByToken(tokenValue);
        if (token == null) return Optional.empty();
        try {
            long userId = Long.parseLong(token.toString());
            var surveys = surveyDao.findSurveysByUserId(userId);
        } catch (Exception e) {
            return
        }
    }

}
