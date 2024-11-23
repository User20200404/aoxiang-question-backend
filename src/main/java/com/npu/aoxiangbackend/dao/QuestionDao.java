package com.npu.aoxiangbackend.dao;

import com.npu.aoxiangbackend.model.Question;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class QuestionDao implements IQuestionDao {

    private final SessionFactory sessionFactory;

    @Autowired
    public QuestionDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public long addQuestion(Question question) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            long questionId = (long) session.save(question);
            transaction.commit();
            return questionId;
        }
    }

    @Override
    public Optional<Question> findQuestionById(long id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(Question.class, id));
        }
    }


    @Override
    public int getQuestionCountOfSurvey(String surveyID) {
        try (Session session = sessionFactory.openSession()) {
            Query<Long> query = session.createQuery("select count(*) from Question where sourceSurveyId = :surveyId", Long.class);
            query.setParameter("surveyId", surveyID);
            Long count = query.uniqueResult();
            return count != null ? count.intValue() : 0;
        }
    }

    @Override
    public List<Question> findQuestionsBySurveyId(String surveyId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Question> query = session.createQuery("from Question where sourceSurveyId = :surveyId", Question.class);
            query.setParameter("surveyId", surveyId);
            return query.list();
        }
    }

    @Override
    public boolean deleteQuestion(long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Question question = session.get(Question.class, id);
            if (question == null) {
                return false;
            }

            //对于每一个排在要删除的问题后面的问题，将它们的次序往前挪一位
            List<Question> questionsOfSurvey = findQuestionsBySurveyId(question.getSourceSurveyId());
            for (Question questionOfSurvey : questionsOfSurvey.stream().filter(q -> q.getOrderIndex() > question.getOrderIndex()).collect(Collectors.toUnmodifiableList())) {
                questionOfSurvey.setOrderIndex(questionOfSurvey.getOrderIndex() - 1);
                updateQuestion(questionOfSurvey);
            }

            session.delete(question);
            transaction.commit();
            return true;
        }
    }

    @Override
    public boolean updateQuestion(Question question) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.update(question);
            transaction.commit();
            return true;
        }
    }
}