package com.jumbo.map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.jumbo.map")
public class MapTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(MapTestApplication.class, args);
    }
}
