package com.jumbo.map.service;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.MongoDBContainer;

import javax.annotation.PreDestroy;

import static java.lang.String.format;

@Profile("test")
@Configuration("MongoDBTestConfiguration")
public class MongoDBTestConfiguration {

    public static MongoDBContainer mongoDBContainer;

    static {
        mongoDBContainer = new MongoDBContainer("mongo:4.4.14");
        mongoDBContainer.start();
    }

    public static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {

            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(configurableApplicationContext,
                    format("spring.data.mongodb.uri=mongodb://%s:%s/db",
                            mongoDBContainer.getHost(), mongoDBContainer.getFirstMappedPort()));
        }
    }

    @PreDestroy
    public void preDestroy() {
        mongoDBContainer.stop();
    }
}
