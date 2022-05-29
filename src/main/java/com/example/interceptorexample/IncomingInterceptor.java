package com.example.interceptorexample;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class IncomingInterceptor implements HandlerInterceptor {

    private final StatisticsService stats;

    public IncomingInterceptor(StatisticsService stats) {
        this.stats = stats;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        stats.countUp(request.getRequestURI());
        return true;
    }
}
