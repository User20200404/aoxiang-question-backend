package com.npu.aoxiangbackend.service;

import com.npu.aoxiangbackend.dao.IQuestionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {
    private final IQuestionDao questionDao;

    @Autowired
    public QuestionService(IQuestionDao questionDao) {
        this.questionDao = questionDao;
    }


}
