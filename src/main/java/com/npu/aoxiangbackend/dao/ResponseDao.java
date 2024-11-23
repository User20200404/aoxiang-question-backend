package com.npu.aoxiangbackend.dao;

import com.npu.aoxiangbackend.model.Response;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository // 标记为Spring管理的Repository组件
public class ResponseDao {

    private final SessionFactory sessionFactory; // 注入SessionFactory用于创建Session

    @Autowired // 自动注入SessionFactory
    public ResponseDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory; // 构造器注入SessionFactory
    }

    public Optional<Response> findResponseById(String id) {
        try (Session session = sessionFactory.openSession()) { // 打开一个新的Session
            session.beginTransaction(); // 开始事务
            Response response = session.get(Response.class, id); // 根据ID查找Response对象
            session.getTransaction().commit(); // 提交事务
            return Optional.ofNullable(response); // 返回可能为空的结果
        }
    }

    public Optional<Response> findResponseBySurveyIdAndUserId(String surveyId, long userId) {
        try (Session session = sessionFactory.openSession()) { // 打开一个新的Session
            session.beginTransaction(); // 开始事务
            var query = session.createQuery("from Response where surveyId = :surveyId and userId = :userId", Response.class); // 创建查询
            query.setParameter("surveyId", surveyId); // 设置查询参数
            query.setParameter("userId", userId); // 设置查询参数
            var response = query.uniqueResultOptional(); // 获取唯一结果或空
            session.getTransaction().commit(); // 提交事务
            return response; // 返回结果
        }
    }

    public List<Response> findResponsesBySurveyId(String surveyId) {
        try (Session session = sessionFactory.openSession()) { // 打开一个新的Session
            session.beginTransaction(); // 开始事务
            var query = session.createQuery("from Response where surveyId = :surveyId", Response.class); // 创建查询
            query.setParameter("surveyId", surveyId); // 设置查询参数
            var responses = query.getResultList(); // 获取结果列表
            session.getTransaction().commit(); // 提交事务
            return responses; // 返回结果列表
        }
    }

    public List<Response> findResponsesByUserId(long userId) {
        try (Session session = sessionFactory.openSession()) { // 打开一个新的Session
            session.beginTransaction(); // 开始事务
            var query = session.createQuery("from Response where userId = :userId", Response.class); // 创建查询
            query.setParameter("userId", userId); // 设置查询参数
            var responses = query.getResultList(); // 获取结果列表
            session.getTransaction().commit(); // 提交事务
            return responses; // 返回结果列表
        }
    }

    public String addResponse(Response response) {
        try (Session session = sessionFactory.openSession()) { // 打开一个新的Session
            session.beginTransaction(); // 开始事务
            String responseId = (String) session.save(response); // 保存新的Response对象并获取生成的ID
            session.getTransaction().commit(); // 提交事务
            return responseId; // 返回新生成的ID
        }
    }

    public boolean updateResponse(Response response) {
        try (Session session = sessionFactory.openSession()) { // 打开一个新的Session
            session.beginTransaction(); // 开始事务
            session.update(response); // 更新已存在的Response对象
            session.getTransaction().commit(); // 提交事务
            return true; // 返回成功标志
        }
    }

    public boolean deleteResponse(String id) {
        try (Session session = sessionFactory.openSession()) { // 打开一个新的Session
            session.beginTransaction(); // 开始事务
            Response response = session.get(Response.class, id); // 根据ID查找Response对象
            if (response != null) { // 如果对象存在
                session.delete(response); // 删除对象
                session.getTransaction().commit(); // 提交事务
                return true; // 返回成功标志
            } else {
                session.getTransaction().commit(); // 如果对象不存在，仍然提交事务
                return false; // 返回失败标志
            }
        }
    }
}