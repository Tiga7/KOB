package com.kob.matchingsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 自动注入
 * @author Gan
 */
@Configuration
public class RestTempLateConfig {
    @Bean
    public RestTemplate getRestTemplate()
    {
        return new RestTemplate();
    }
}
