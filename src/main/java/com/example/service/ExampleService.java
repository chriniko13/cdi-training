package com.example.service;

import com.example.GreetingConfig;
import com.example.interceptor.statistic.StatisticsAware;
import com.example.interceptor.statistic.StatisticsEndpointName;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ExampleService {

    @Inject
    GreetingConfig greetingConfig;

    @StatisticsAware
    @StatisticsEndpointName("/hello")
    public String hello() {
        return "Hello " + greetingConfig.message();
    }
}
