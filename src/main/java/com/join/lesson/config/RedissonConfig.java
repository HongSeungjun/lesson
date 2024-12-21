//package com.join.lesson.config;
//
//import org.redisson.Redisson;
//import org.redisson.api.RedissonClient;
//import org.redisson.config.Config;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class RedissonConfig {
//
//    private String redisHost = "localhost";
//
//    private int redisPort = 6379;
//
//    private static final String REDISSON_HOST_PREFIX = "redis://";
//
//    @Bean
//    public RedissonClient redissonClient() {
//        RedissonClient redisson = null;
//        Config config = new Config();
//        config.useSingleServer().setAddress(REDISSON_HOST_PREFIX + redisHost + ":" + redisPort);
//        redisson = Redisson.create(config);
//        return redisson;
//    }
//}