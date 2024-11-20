package com.npu.aoxiangbackend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import com.npu.aoxiangbackend.util.ColoredPrintStream;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.format.DateTimeFormatter;

@org.springframework.context.annotation.Configuration
public class BeanConfig {
    @Bean
    @Scope("singleton")
    public SessionFactory sessionFactory() {
        try {
            // 创建 SessionFactory 对象
            return new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Hibernate initialization failed, check if you have started the db service :" + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    @Bean
    @Scope("singleton")
    public ColoredPrintStream coloredPrintStream() {
        return new ColoredPrintStream(System.out);
    }
}
