package com.npu.aoxiangbackend.service;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.npu.aoxiangbackend.dao.ISurveyDao;
import com.npu.aoxiangbackend.exception.business.SurveyServiceException;
import com.npu.aoxiangbackend.exception.business.UserServiceException;
import com.npu.aoxiangbackend.exception.internal.DatabaseAccessException;
import com.npu.aoxiangbackend.model.Survey;
import com.npu.aoxiangbackend.model.User;
import com.npu.aoxiangbackend.util.ColoredPrintStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
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

    /**
     * 使用用户token获取该用户创建的所有问卷。
     *
     * @param tokenValue 用户token值。
     * @return 该用户名下的所有问卷对象。
     * @throws UserServiceException    当token校验失败时抛出。
     * @throws DatabaseAccessException 当数据库访问失败时抛出。
     */
    public List<Survey> getSurveysByToken(String tokenValue) throws UserServiceException, DatabaseAccessException {
        var userId = userService.checkAndGetUserId(tokenValue);
        try {
            return surveyDao.findSurveysByUserId(userId);
        } catch (Exception e) {
            printer.shortPrintException(e);
            throw new DatabaseAccessException(e);
        }
    }

    /**
     * 使用指定用户token在该用户名下创建一个问卷。
     *
     * @param tokenValue 用户token值。
     * @return 新创建的问卷的uuid。
     * @throws UserServiceException    当token校验失败时抛出。
     * @throws DatabaseAccessException 当数据库访问失败时抛出。
     */
    public String createSurvey(String tokenValue) throws UserServiceException, DatabaseAccessException {
        var userId = userService.checkAndGetUserId(tokenValue);

        Survey survey = new Survey();
        survey.setCreatorId(userId);
        survey.setInitialized(false);
        survey.setLoginRequired(false);
        survey.setPublic(false);
        survey.setTitle("新建问卷");
        survey.setDescription("新建问卷");
        survey.setTotalResponses(0);
        survey.setCreatedAt(ZonedDateTime.now());
        survey.setUpdatedAt(ZonedDateTime.now());
        survey.setStartTime(ZonedDateTime.now());
        survey.setEndTime(ZonedDateTime.now().plusDays(1));

        try {
            return surveyDao.addSurvey(survey);
        } catch (Exception e) {
            printer.shortPrintException(e);
            throw new DatabaseAccessException(e);
        }
    }

    /**
     * 尝试使用指定登录状态访问某个问卷。
     *
     * @param surveyId   目标问卷ID。
     * @param tokenValue 登录用户的token值。（如果问卷不需要登录且公开，该值被忽略）
     * @return 问卷对象。
     * @throws DatabaseAccessException 如果数据库访问错误。
     * @throws SurveyServiceException  如果问卷不存在，或当前没有访问权限。
     * @throws UserServiceException    如果需要登录验证，且提供的token无效。
     */
    public Survey getSurvey(String surveyId, String tokenValue) throws DatabaseAccessException, SurveyServiceException, UserServiceException {
        Optional<Survey> surveyOptional;
        try {
            surveyOptional = surveyDao.findSurveyById(surveyId);
        } catch (Exception e) {
            printer.shortPrintException(e);
            throw new DatabaseAccessException(e);
        }

        if (surveyOptional.isEmpty()) {
            throw new SurveyServiceException(String.format("该问卷不存在：%s", surveyId));
        }
        Survey survey = surveyOptional.get();

        boolean loginRequired = survey.isLoginRequired();
        boolean isPublic = survey.isPublic();
        if (!loginRequired && isPublic) {
            //如果问卷不需要登录且公开，直接返回即可。
            return survey;
        }

        //验证是否登录。
        var userId = userService.checkAndGetUserId(tokenValue);

        //到这里已经登录，如果问卷是公开的直接返回。
        if (isPublic) {
            return survey;
        }
        //问卷非公开，检查当前登录用户是否为创建者
        if (userId == survey.getCreatorId())
            return survey;


        throw new SurveyServiceException("该问卷由其他用户创建，且暂未公开。");
    }
}
