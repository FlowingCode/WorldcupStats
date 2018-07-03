package com.flowingcode.fixture;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * The entry point of the Spring Boot application.
 */
@SpringBootApplication
@EnableCaching
@EnableScheduling
@Configuration
public class FixtureApp extends SpringBootServletInitializer {

    public static void main(final String[] args) {
        SpringApplication.run(FixtureApp.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(
            final SpringApplicationBuilder builder) {
        return builder.sources(FixtureApp.class);
    }

    @Bean
    Executor executor() {
        return Executors.newFixedThreadPool(4);
    }

}
