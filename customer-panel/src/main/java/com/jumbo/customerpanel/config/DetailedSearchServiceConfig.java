package com.jumbo.customerpanel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Config task executor for Detailed search
 * add queue capacity to manage backpressure
 */
@Configuration
public class DetailedSearchServiceConfig {

    public static final String Detailed_SEARCH_SERVICE_EXECUTOR = "detailed_search_service_executor";

    @Bean(name = Detailed_SEARCH_SERVICE_EXECUTOR)
    public TaskExecutor detailedSearchServiceTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(2);
        taskExecutor.setMaxPoolSize(4);
        taskExecutor.setQueueCapacity(20);
        taskExecutor.afterPropertiesSet();
        return taskExecutor;
    }
}
