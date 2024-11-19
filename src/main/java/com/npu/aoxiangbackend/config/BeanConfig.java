package com.npu.aoxiangbackend.config;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

@org.springframework.context.annotation.Configuration
public class BeanConfig {
    @Bean
    @Scope("singleton")
    public SessionFactory sessionFactory() {
        try {
            // 创建 SessionFactory 对象
            return new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Hibernate 初始化失败！" + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
}
