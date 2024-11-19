package com.npu.aoxiangbackend.dao;

import com.npu.aoxiangbackend.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserDao implements IUserDao {
    private final SessionFactory sessionFactory;

    @Autowired
    public UserDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<User> findUserById(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User user = session.get(User.class, id);
            session.getTransaction().commit();
            return Optional.ofNullable(user); // 使用ofNullable防止null值导致异常
        }
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var query = session.createQuery("from User where email = :email", User.class);
            query.setParameter("email", email);
            var user = query.uniqueResultOptional();
            session.getTransaction().commit();
            return user;
        }
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var query = session.createQuery("from User where username = :username", User.class);
            query.setParameter("username", username);
            var user = query.uniqueResultOptional();
            session.getTransaction().commit();
            return user;
        }
    }

    @Override
    public boolean addUser(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
            return true; // 成功保存后返回true
        }
    }

    @Override
    public boolean updateUser(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(user);
            session.getTransaction().commit();
            return true; // 成功更新后返回true
        }
    }

    @Override
    public boolean deleteUser(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.delete(user);
                session.getTransaction().commit();
                return true; // 成功删除后返回true
            } else {
                session.getTransaction().commit(); // 如果用户不存在也提交事务
                return false; // 用户不存在返回false
            }
        }
    }
}