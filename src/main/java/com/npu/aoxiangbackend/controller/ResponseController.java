package com.npu.aoxiangbackend.controller;

import cn.dev33.satoken.context.model.SaRequest;
import cn.dev33.satoken.util.SaResult;
import com.npu.aoxiangbackend.exception.business.UserServiceException;
import com.npu.aoxiangbackend.exception.internal.DatabaseAccessException;
import com.npu.aoxiangbackend.model.Response;
import com.npu.aoxiangbackend.service.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/response")
public class ResponseController {

    private final ResponseService responseService;

    @Autowired
    public ResponseController(ResponseService responseService) {
        this.responseService = responseService;
    }

    @GetMapping("/all")
    public SaResult listUserResponses(@RequestParam(required = true) String token) {
        try {
            List<Response> responses = responseService.getResponsesByToken(token);
            return SaResult.ok().setData(responses);
        } catch (UserServiceException | DatabaseAccessException e) {
            return SaResult.error(e.getMessage());
        }
    }

    @PostMapping("/create")
    public SaResult createResponse(@RequestParam(required = true) String surveyId, @RequestParam(required = true) String token) {
        try {
            String responseId = responseService.addResponse(surveyId, token);
            return SaResult.ok("成功创建响应。").setData(responseId);
        } catch (UserServiceException | DatabaseAccessException e) {
            return SaResult.error(e.getMessage());
        }
    }

    @GetMapping("/{responseId}")
    public SaResult getResponse(@PathVariable String responseId, @RequestParam String token) {
        try {
            Response response = responseService.getResponseById(responseId, token);
            return SaResult.ok().setData(response);
        } catch (UserServiceException | DatabaseAccessException | RuntimeException e) {
            return SaResult.error(e.getMessage());
        }
    }

    @PostMapping("/update/{responseId}")
    public SaResult updateResponse(@PathVariable String responseId, @RequestParam String token) {
        try {
            responseService.updateResponse(responseId, token);
            return SaResult.ok("成功更新响应。");
        } catch (UserServiceException | DatabaseAccessException | RuntimeException e) {
            return SaResult.error(e.getMessage());
        }
    }

    @PostMapping("/delete/{responseId}")
    public SaResult deleteResponse(@PathVariable String responseId, @RequestParam String token) {
        try {
            responseService.deleteResponse(responseId, token);
            return SaResult.ok("成功删除响应。");
        } catch (UserServiceException | DatabaseAccessException | RuntimeException e) {
            return SaResult.error(e.getMessage());
        }
    }

    @GetMapping("/can-view/{responseId}")
    public SaResult canViewResponse(@PathVariable String responseId, @RequestParam String token) {
        try {
            boolean canView = responseService.canViewResponse(responseId, token);
            return SaResult.ok().setData(canView);
        } catch (DatabaseAccessException e) {
            return SaResult.error(e.getMessage());
        }
    }
}