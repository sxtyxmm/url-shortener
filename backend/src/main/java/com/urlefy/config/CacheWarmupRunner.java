package com.urlefy.config;

import com.urlefy.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class CacheWarmupRunner implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(CacheWarmupRunner.class);

    @Autowired
    private UrlService urlService;

    @Override
    public void run(ApplicationArguments args) {
        logger.info("Starting cache warmup...");
        long startTime = System.currentTimeMillis();
        
        try {
            urlService.warmUpCache();
            long endTime = System.currentTimeMillis();
            logger.info("Cache warmup completed in {} ms", (endTime - startTime));
        } catch (Exception e) {
            logger.error("Error during cache warmup: {}", e.getMessage());
        }
    }
}
