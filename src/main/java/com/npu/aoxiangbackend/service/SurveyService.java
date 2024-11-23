package com.npu.aoxiangbackend.service;

import com.npu.aoxiangbackend.dao.ISurveyDao;
import com.npu.aoxiangbackend.exception.business.SurveyServiceException;
import com.npu.aoxiangbackend.exception.business.UserServiceException;
import com.npu.aoxiangbackend.exception.internal.DatabaseAccessException;
import com.npu.aoxiangbackend.model.Survey;
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
    public Survey accessSurvey(String surveyId, String tokenValue) throws DatabaseAccessException, SurveyServiceException, UserServiceException {
        Survey survey = getRequiredSurvey(surveyId);

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

    /**
     * 根据surveyId从数据库获取Survey对象，这个方法会在未找到时抛出异常。
     *
     * @param surveyId Survey标识符
     * @return 问卷对象
     * @throws DatabaseAccessException 如果数据库访问失败
     * @throws SurveyServiceException  如果问卷不存在
     */
    private Survey getRequiredSurvey(String surveyId) throws DatabaseAccessException, SurveyServiceException {
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
        return surveyOptional.get();
    }

    private Survey getRequiredSurveyAndCheckLogin(String surveyId, String tokenValue, boolean creatorOnly) throws DatabaseAccessException, SurveyServiceException, UserServiceException {
        var userId = userService.checkAndGetUserId(tokenValue);
        Survey survey = getRequiredSurvey(surveyId);
        if (creatorOnly && survey.getCreatorId() != userId)
            throw new SurveyServiceException("当前用户不是该问卷的创建者。");
        return survey;
    }

    /**
     * 尝试以指定登录状态删除问卷。
     *
     * @param surveyId   问卷标识符。
     * @param tokenValue 登录Token。
     * @throws DatabaseAccessException 如果数据库访问失败。
     * @throws SurveyServiceException  如果当前用户没有删除权限。
     * @throws UserServiceException    如果登录状态无效。
     */
    public void deleteSurvey(String surveyId, String tokenValue) throws DatabaseAccessException, SurveyServiceException, UserServiceException {
        getRequiredSurveyAndCheckLogin(surveyId, tokenValue, true);
        try {
            surveyDao.deleteSurvey(surveyId);
        } catch (Exception e) {
            printer.shortPrintException(e);
            throw new DatabaseAccessException(e);
        }
    }

    public void updateSurvey(String surveyId, String tokenValue, boolean loginRequired, boolean isPublic,
                             String title, String description, ZonedDateTime startTime, ZonedDateTime endTime) throws SurveyServiceException, DatabaseAccessException, UserServiceException {
        var survey = getRequiredSurveyAndCheckLogin(surveyId, tokenValue, true);
        survey.setLoginRequired(loginRequired);
        survey.setPublic(isPublic);
        survey.setTitle(title);
        survey.setDescription(description);
        survey.setStartTime(startTime);
        survey.setEndTime(endTime);

        try {
            surveyDao.updateSurvey(survey);
        } catch (Exception e) {
            printer.shortPrintException(e);
            throw new DatabaseAccessException(e);
        }
    }

    /**
     * 以指定登录状态初始化一个问卷。问卷只有在被初始化后才能开始填写。
     *
     * @param surveyId   问卷ID。
     * @param tokenValue 登录token。
     * @throws DatabaseAccessException 如果数据库访问失败。
     * @throws SurveyServiceException  如果当前用户没有操作权限。
     * @throws UserServiceException    如果登录状态无效。
     */
    public void initializeSurvey(String surveyId, String tokenValue) throws DatabaseAccessException, SurveyServiceException, UserServiceException {
        var survey = getRequiredSurveyAndCheckLogin(surveyId, tokenValue, true);
        if (!survey.isInitialized()) {
            survey.setInitialized(true);
        } else throw new SurveyServiceException("该问卷已经被初始化。");

        try {
            surveyDao.updateSurvey(survey);
        } catch (Exception e) {
            printer.shortPrintException(e);
            throw new DatabaseAccessException(e);
        }
    }

    public boolean canViewSurvey(String surveyId, String tokenValue) throws DatabaseAccessException {
        try {
            accessSurvey(surveyId, tokenValue);
            return true;
        } catch (SurveyServiceException | UserServiceException e) {
            return false;
        }
    }

    public boolean canEditSurvey(String surveyId, String tokenValue) throws DatabaseAccessException {
        try {
            getRequiredSurveyAndCheckLogin(surveyId, tokenValue, true);
            return true;
        } catch (SurveyServiceException | UserServiceException e) {
            return false;
        }
    }


}
