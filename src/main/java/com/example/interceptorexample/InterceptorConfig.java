package com.example.interceptorexample;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    public InterceptorConfig(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    private final StatisticsService statisticsService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new IncomingInterceptor(statisticsService)).addPathPatterns("/**");
    }
}
