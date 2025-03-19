package com.bakouan.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "com.bakouan.app.repositories")
@EntityScan(basePackages = "com.bakouan.app")
@SpringBootApplication(scanBasePackages = {"com.bakouan"})
public class DgpeServiceApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(DgpeServiceApplication.class);
    }

    public static void main(final String[] args) {
        SpringApplication.run(DgpeServiceApplication.class, args);
    }

}
