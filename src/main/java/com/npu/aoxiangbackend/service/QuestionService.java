package com.npu.aoxiangbackend.service;

import cn.dev33.satoken.exception.NotImplException;
import com.npu.aoxiangbackend.dao.IQuestionDao;
import com.npu.aoxiangbackend.exception.business.QuestionServiceException;
import com.npu.aoxiangbackend.exception.business.SurveyServiceException;
import com.npu.aoxiangbackend.exception.business.UserServiceException;
import com.npu.aoxiangbackend.exception.internal.DatabaseAccessException;
import com.npu.aoxiangbackend.exception.internal.InternalException;
import com.npu.aoxiangbackend.model.Question;
import com.npu.aoxiangbackend.model.QuestionType;
import com.npu.aoxiangbackend.util.ColoredPrintStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    private final IQuestionDao questionDao;
    private final SurveyService surveyService;
    private final ColoredPrintStream printer;

    @Autowired
    public QuestionService(IQuestionDao questionDao, SurveyService surveyService, ColoredPrintStream coloredPrintStream) {
        this.questionDao = questionDao;
        this.surveyService = surveyService;
        this.printer = coloredPrintStream;
    }

    public List<Question> getQuestionsOfSurvey(String surveyId, String tokenValue) throws DatabaseAccessException, QuestionServiceException {
        if (!surveyService.canViewSurvey(surveyId, tokenValue)) {
            throw new QuestionServiceException("你没有该问卷下的问题的权限。");
        }
        try {
            return questionDao.findQuestionsBySurveyId(surveyId);
        } catch (Exception e) {
            printer.shortPrintException(e);
            throw new DatabaseAccessException(e);
        }
    }

    public Question getQuestion(long questionId, String tokenValue) throws DatabaseAccessException, QuestionServiceException {
        var question = getRequiredQuestion(questionId);
        if (!surveyService.canViewSurvey(question.getSourceSurveyId(), tokenValue)) {
            throw new QuestionServiceException("你没有查看该问题所属问卷的权限。");
        }
        return question;
    }

    public long createQuestion(String surveyId, String tokenValue) throws DatabaseAccessException, QuestionServiceException {
        if (!surveyService.canEditSurvey(surveyId, tokenValue)) {
            throw new QuestionServiceException("你没有编辑该问卷的权限。");
        }

        int existingQuestionCount;
        try {
            existingQuestionCount = questionDao.getQuestionCountOfSurvey(surveyId);
        } catch (Exception e) {
            printer.shortPrintException(e);
            throw new DatabaseAccessException(e);
        }

        Question question = new Question();
        question.setContent("新建问题");
        question.setRequired(true);
        question.setType(QuestionType.PlainText);
        question.setOrderIndex(existingQuestionCount + 1);
        question.setSourceSurveyId(surveyId);

        try {
            return questionDao.addQuestion(question);
        } catch (Exception e) {
            printer.shortPrintException(e);
            throw new DatabaseAccessException(e);
        }
    }

    public void removeQuestion(long questionId, String tokenValue) throws DatabaseAccessException, QuestionServiceException {
        Question question = getRequiredQuestion(questionId);
        var surveyId = question.getSourceSurveyId();
        if (!surveyService.canEditSurvey(surveyId, tokenValue)) {
            throw new QuestionServiceException("你没有从所属问卷中移除该问题的权限。");
        }

        //删除该问卷，重排次序的逻辑已经在Dao层处理。
        try {
            questionDao.deleteQuestion(questionId);
        } catch (Exception e) {
            printer.shortPrintException(e);
            throw new DatabaseAccessException(e);
        }
    }

    private void reorderQuestion(long questionId, String tokenValue) {
        throw new NotImplException("该方法暂未实现");
    }

    private Question getRequiredQuestion(long questionId) throws DatabaseAccessException, QuestionServiceException {
        Optional<Question> questionOptional;
        try {
            questionOptional = questionDao.findQuestionById(questionId);
        } catch (Exception e) {
            printer.shortPrintException(e);
            throw new DatabaseAccessException(e);
        }
        if (questionOptional.isEmpty()) {
            throw new QuestionServiceException(String.format("该问题不存在：%d", questionId));
        }
        return questionOptional.get();
    }
}
