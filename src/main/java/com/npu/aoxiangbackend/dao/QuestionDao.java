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
    public Optional<Question> findQuestionById(String id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(Question.class, id));
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
    public void deleteQuestion(String id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Question question = session.get(Question.class, id);
            if (question != null) {
                session.delete(question);
            }
            transaction.commit();
        }
    }

    @Override
    public void updateQuestion(Question question) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.update(question);
            transaction.commit();
        }
    }
}