package com.npu.aoxiangbackend.controller;

import cn.dev33.satoken.context.model.SaRequest;
import cn.dev33.satoken.util.SaResult;
import com.npu.aoxiangbackend.exception.business.SurveyServiceException;
import com.npu.aoxiangbackend.exception.business.UserServiceException;
import com.npu.aoxiangbackend.exception.internal.DatabaseAccessException;
import com.npu.aoxiangbackend.model.Survey;
import com.npu.aoxiangbackend.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/survey")
public class SurveyController {

    private final SurveyService surveyService;

    @Autowired
    public SurveyController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @GetMapping("/all")
    public SaResult listUserSurveys(@RequestParam(required = true) String token) {
        List<Survey> surveys = null;
        try {
            surveys = surveyService.getSurveysByToken(token);
        } catch (UserServiceException | DatabaseAccessException e) {
            return SaResult.error(e.getMessage());
        }
        return SaResult.ok().setData(surveys);
    }

    @RequestMapping(value = "/create", method = {RequestMethod.POST, RequestMethod.GET})
    public SaResult createSurvey(@RequestParam(required = true) String token) {
        try {
            return SaResult.ok("成功创建问卷。").setData(surveyService.createSurvey(token));
        } catch (UserServiceException | DatabaseAccessException e) {
            return SaResult.error(e.getMessage());
        }
    }

    @RequestMapping(value = "/{surveyId}", method = {RequestMethod.POST, RequestMethod.GET})
    public SaResult getSurvey(@PathVariable String surveyId, @RequestParam String token) {
        try {
            return SaResult.ok().setData(surveyService.accessSurvey(surveyId, token));
        } catch (SurveyServiceException | UserServiceException | DatabaseAccessException e) {
            return SaResult.error(e.getMessage());
        }
    }

    @RequestMapping(value = "/remove/{surveyId}", method = {RequestMethod.POST, RequestMethod.GET})
    public SaResult removeSurvey(@PathVariable String surveyId, @RequestParam String token) {
        try {
            surveyService.deleteSurvey(surveyId, token);
            return SaResult.ok("成功删除问卷。");
        } catch (SurveyServiceException | UserServiceException | DatabaseAccessException e) {
            return SaResult.error(e.getMessage());
        }
    }

    @RequestMapping(value = "/init/{surveyId}", method = {RequestMethod.POST, RequestMethod.GET})
    public SaResult initSurvey(@PathVariable String surveyId, @RequestParam String token) {
        try {
            surveyService.initializeSurvey(surveyId, token);
            return SaResult.ok("成功启用问卷。");
        } catch (SurveyServiceException | UserServiceException | DatabaseAccessException e) {
            return SaResult.error(e.getMessage());
        }
    }
}
