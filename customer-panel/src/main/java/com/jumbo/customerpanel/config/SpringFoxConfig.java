package com.jumbo.customerpanel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Config swagger API
 */
@Configuration
public class SpringFoxConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                //exclude error for base-error-controller for spring
                .apis(requestHandler -> requestHandler.groupName().contains("controller")
                        && !requestHandler.groupName().contains("error"))
                .paths(PathSelectors.any())
                .build();
    }
}