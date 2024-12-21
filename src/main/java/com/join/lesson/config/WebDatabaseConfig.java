package com.join.lesson.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Profile("local")
@EnableJpaRepositories(
        basePackages = "com.join.lesson.feature.**.repository.web",
        entityManagerFactoryRef = "entityManagerFactoryWeb",
        transactionManagerRef = "webDBTransactionManager"
)
public class WebDatabaseConfig {

    private final String DATASOURCE_PREFIX = "spring.datasource.web.hikari";

    private final JpaProperties jpaProperties;

    public WebDatabaseConfig(JpaProperties jpaProperties) {
        this.jpaProperties = jpaProperties;
    }

    @Bean(name = "datasourceWeb")
    @Primary
    @ConfigurationProperties(prefix = DATASOURCE_PREFIX)
    public HikariDataSource dataSourceWeb() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Primary
    @Bean(name = "entityManagerFactoryWeb")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryWeb(EntityManagerFactoryBuilder builder) {
        Map<String, Object> properties = new HashMap<>(jpaProperties.getProperties());

        return builder
                .dataSource(dataSourceWeb())
                .packages("com.join.lesson.core.entity.web") // 엔티티 클래스 패키지
                .persistenceUnit("web")
                .properties(properties)
                .build();
    }

    @Primary
    @Bean(name = "webDBTransactionManager")
    public PlatformTransactionManager webDBTransactionManager(
            @Qualifier("entityManagerFactoryWeb") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Primary
    @Bean
    public JPAQueryFactory jpaQueryFactoryWeb(
            @Qualifier("entityManagerFactoryWeb") EntityManagerFactory entityManagerFactory) {
        return new JPAQueryFactory(entityManagerFactory.createEntityManager());
    }
}
