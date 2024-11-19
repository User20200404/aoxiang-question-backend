package com.npu.aoxiangbackend.util;

import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    @Getter
    private static final SessionFactory sessionFactory;

    static {
        sessionFactory = buildSessionFactory();
    }

    private static SessionFactory buildSessionFactory() {
        try {
            // 创建 SessionFactory 对象
            return new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session openSession() {
        return sessionFactory.openSession();
    }

    public static void shutdown() {
        // 关闭 SessionFactory
        getSessionFactory().close();
    }
}