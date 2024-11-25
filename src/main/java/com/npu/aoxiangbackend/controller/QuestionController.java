package com.npu.aoxiangbackend.controller;

import cn.dev33.satoken.util.SaResult;
import com.npu.aoxiangbackend.exception.business.QuestionServiceException;
import com.npu.aoxiangbackend.exception.internal.DatabaseAccessException;
import com.npu.aoxiangbackend.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/question")
public class QuestionController {
    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @RequestMapping("/all")
    public SaResult enumerateQuestions(@RequestParam(required = true) String surveyId, @RequestParam(required = false) String token) {
        try {
            return SaResult.ok("成功获取所有问题").setData(questionService.getQuestionsOfSurvey(surveyId, token));
        } catch (QuestionServiceException | DatabaseAccessException e) {
            return SaResult.error(e.getMessage());
        }
    }

    @RequestMapping(value = "/delete/{questionId}", method = {RequestMethod.GET, RequestMethod.POST})
    public SaResult deleteQuestion(@PathVariable(name = "questionId") long questionId, @RequestParam(required = true) String token) {
        try {
            questionService.removeQuestion(questionId, token);
            return SaResult.ok("成功删除该问题");
        } catch (QuestionServiceException | DatabaseAccessException e) {
            return SaResult.error(e.getMessage());
        }
    }

    @RequestMapping(value = "/create", method = {RequestMethod.GET, RequestMethod.POST})
    public SaResult createQuestion(@RequestParam(required = true) String surveyId, @RequestParam(required = true) String token) {
        try {
            return SaResult.ok("成功创建问题").setData(questionService.createQuestion(surveyId, token));
        } catch (QuestionServiceException | DatabaseAccessException e) {
            return SaResult.error(e.getMessage());
        }
    }

    @RequestMapping(value = "/{questionId}", method = {RequestMethod.GET, RequestMethod.POST})
    public SaResult getQuestion(@PathVariable(name = "questionId") long questionId, @RequestParam(required = false) String token) {
        try {
            return SaResult.ok("成功查询问题").setData(questionService.getQuestion(questionId, token));
        } catch (QuestionServiceException | DatabaseAccessException e) {
            return SaResult.error(e.getMessage());
        }
    }
}
