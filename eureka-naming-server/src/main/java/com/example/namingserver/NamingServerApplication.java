package com.example.namingserver;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Extends #{SpringBootServletInitializer.class} and override the configure() to enbale war
 * deployment to explicit web application server instead of embedded one.
 */
@SpringBootApplication
@EnableEurekaServer
@RequiredArgsConstructor
public class NamingServerApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(NamingServerApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(NamingServerApplication.class);
    }
}
