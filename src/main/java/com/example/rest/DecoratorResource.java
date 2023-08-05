package com.example.rest;


import com.example.decorator.GreetingBean;
import com.example.decorator.NormalGreeting;
import com.example.dto.DecoratorRequestDto;
import com.example.dto.DecoratorResponseDto;
import com.example.interceptor.statistic.StatisticsAware;
import com.example.interceptor.statistic.StatisticsEndpointName;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.jboss.logging.Logger;

/**
 * Provides an example for decorator.
 */
@Path("/decorator-example")
@ApplicationScoped
public class DecoratorResource {

    private static final Logger LOG = Logger.getLogger(DecoratorResource.class);

    @Inject
    @NormalGreeting
    //@Any
    GreetingBean greetingBean;


    @StatisticsAware
    @StatisticsEndpointName("/decorator-example")
    @POST
    public DecoratorResponseDto run(DecoratorRequestDto requestDto) {
        int times = requestDto.getTimes();
        String result = greetingBean.greet(times);

        return new DecoratorResponseDto(result);
    }
}
