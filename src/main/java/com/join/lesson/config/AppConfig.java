//package com.join.lesson.config;
//
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//
//@Configuration
//public class AppConfig {
//
//    /* JPA 관련 설정 */
//    @PersistenceContext(unitName = "webManager")
//    private EntityManager webEntityManager;
//
//    /* QueryDsl 관련 설정 */
//    @Primary
//    @Bean
//    public JPAQueryFactory jpaQueryFactory() {
//        return new JPAQueryFactory(webEntityManager);
//    }
//}