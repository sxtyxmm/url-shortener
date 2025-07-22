package com.urlefy;

import com.urlefy.service.UrlService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import java.util.concurrent.Executor;

@SpringBootApplication
@EnableAsync
@EnableCaching
@EnableMongoAuditing
public class UrlShortenerApplication {
    
    @Autowired
    private UrlService urlService;
    
    public static void main(String[] args) {
        SpringApplication.run(UrlShortenerApplication.class, args);
    }
    
    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20);
        executor.setMaxPoolSize(100);
        executor.setQueueCapacity(1000);
        executor.setThreadNamePrefix("UrlShortener-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);
        executor.initialize();
        return executor;
    }
    
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        // Warm up cache when application is ready
        urlService.warmUpCache();
    }
}