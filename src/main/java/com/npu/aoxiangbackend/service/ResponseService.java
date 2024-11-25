package com.npu.aoxiangbackend.service;

import com.npu.aoxiangbackend.dao.IResponseDao;
import com.npu.aoxiangbackend.exception.business.UserServiceException;
import com.npu.aoxiangbackend.exception.internal.DatabaseAccessException;
import com.npu.aoxiangbackend.model.Response;
import com.npu.aoxiangbackend.util.ColoredPrintStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ResponseService {

    private final IResponseDao responseDao;
    private final UserService userService;
    private final ColoredPrintStream printer;

    @Autowired
    public ResponseService(IResponseDao responseDao, UserService userService, ColoredPrintStream printer) {
        this.responseDao = responseDao;
        this.userService = userService;
        this.printer = printer;
    }

    /**
     * 使用用户token获取该用户的所有响应。
     *
     * @param tokenValue 用户token值。
     * @return 该用户的响应列表。
     * @throws UserServiceException    当token校验失败时抛出。
     * @throws DatabaseAccessException 当数据库访问失败时抛出。
     */
    public List<Response> getResponsesByToken(String tokenValue) throws UserServiceException, DatabaseAccessException {
        var userId = userService.checkAndGetUserId(tokenValue);
        try {
            return responseDao.findResponsesByUserId(userId);
        } catch (Exception e) {
            printer.shortPrintException(e);
            throw new DatabaseAccessException(e);
        }
    }

    /**
     * 使用指定用户token为某个调查添加一个响应。
     *
     * @param surveyId   调查ID。
     * @param tokenValue 用户token值。
     * @return 新创建的响应的uuid。
     * @throws UserServiceException    当token校验失败时抛出。
     * @throws DatabaseAccessException 当数据库访问失败时抛出。
     */
    public String addResponse(String surveyId, String tokenValue) throws UserServiceException, DatabaseAccessException {
        var userId = userService.checkAndGetUserId(tokenValue);

        Response response = new Response();
        response.setSurveyId(surveyId);
        response.setUserId(userId);
        response.setSubmittedAt(ZonedDateTime.now());

        try {
            return responseDao.addResponse(response);
        } catch (Exception e) {
            printer.shortPrintException(e);
            throw new DatabaseAccessException(e);
        }
    }

    /**
     * 更新指定的响应对象。
     *
     * @param responseId 响应ID。
     * @param tokenValue 用户token值。
     * @throws UserServiceException    当token校验失败时抛出。
     * @throws DatabaseAccessException 当数据库访问失败时抛出。
     */
    public void updateResponse(String responseId, String tokenValue) throws UserServiceException, DatabaseAccessException {
        var userId = userService.checkAndGetUserId(tokenValue);

        Optional<Response> responseOptional;
        try {
            responseOptional = responseDao.findResponseById(responseId);
        } catch (Exception e) {
            printer.shortPrintException(e);
            throw new DatabaseAccessException(e);
        }

        if (responseOptional.isEmpty()) {
            throw new RuntimeException("响应不存在");
        }

        Response response = responseOptional.get();
        if (response.getUserId() != userId) {
            throw new RuntimeException("当前用户无权更新此响应");
        }

        response.setSubmittedAt(ZonedDateTime.now()); // 更新提交时间

        try {
            responseDao.updateResponse(response);
        } catch (Exception e) {
            printer.shortPrintException(e);
            throw new DatabaseAccessException(e);
        }
    }

    /**
     * 删除指定ID的响应对象。
     *
     * @param responseId 响应ID。
     * @param tokenValue 用户token值。
     * @throws UserServiceException    当token校验失败时抛出。
     * @throws DatabaseAccessException 当数据库访问失败时抛出。
     */
    public void deleteResponse(String responseId, String tokenValue) throws UserServiceException, DatabaseAccessException {
        var userId = userService.checkAndGetUserId(tokenValue);

        Optional<Response> responseOptional;
        try {
            responseOptional = responseDao.findResponseById(responseId);
        } catch (Exception e) {
            printer.shortPrintException(e);
            throw new DatabaseAccessException(e);
        }

        if (responseOptional.isEmpty()) {
            throw new RuntimeException("响应不存在");
        }

        Response response = responseOptional.get();
        if (response.getUserId() != userId) {
            throw new RuntimeException("当前用户无权删除此响应");
        }

        try {
            responseDao.deleteResponse(responseId);
        } catch (Exception e) {
            printer.shortPrintException(e);
            throw new DatabaseAccessException(e);
        }
    }

    /**
     * 获取指定ID的响应对象。
     *
     * @param responseId 响应ID。
     * @param tokenValue 用户token值。
     * @return 响应对象。
     * @throws UserServiceException    当token校验失败时抛出。
     * @throws DatabaseAccessException 当数据库访问失败时抛出。
     */
    public Response getResponseById(String responseId, String tokenValue) throws UserServiceException, DatabaseAccessException {
        var userId = userService.checkAndGetUserId(tokenValue);

        Optional<Response> responseOptional;
        try {
            responseOptional = responseDao.findResponseById(responseId);
        } catch (Exception e) {
            printer.shortPrintException(e);
            throw new DatabaseAccessException(e);
        }

        if (responseOptional.isEmpty()) {
            throw new RuntimeException("响应不存在");
        }

        Response response = responseOptional.get();
        if (response.getUserId() != userId) {
            throw new RuntimeException("当前用户无权查看此响应");
        }

        return response;
    }

    /**
     * 检查用户是否有权限查看某个响应。
     *
     * @param responseId 响应ID。
     * @param tokenValue 用户token值。
     * @return 用户是否有权限查看该响应。
     * @throws DatabaseAccessException 当数据库访问失败时抛出。
     */
    public boolean canViewResponse(String responseId, String tokenValue) throws DatabaseAccessException {
        try {
            var userId = userService.checkAndGetUserId(tokenValue);
            Optional<Response> responseOptional = responseDao.findResponseById(responseId);
            if (responseOptional.isPresent() && responseOptional.get().getUserId() == userId) {
                return true;
            }
        } catch (UserServiceException e) {
            return false;
        }
        return false;
    }
}