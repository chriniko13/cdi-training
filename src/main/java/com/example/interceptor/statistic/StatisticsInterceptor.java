package com.example.interceptor.statistic;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import java.lang.reflect.Method;
import org.jboss.logging.Logger;

@StatisticsAware
@Interceptor
@Priority(Interceptor.Priority.APPLICATION + 5)
public class StatisticsInterceptor {

    private static final Logger LOG = Logger.getLogger(StatisticsInterceptor.class);

    @Inject
    StatisticsRegistry statisticsRegistry;

    @AroundInvoke
    public Object logInvocation(final InvocationContext context) throws Exception {
        final Method method = context.getMethod();
        LOG.info("Entering method: " + method.getName());

        final StatisticsEndpointName statisticsEndpointName = method.getAnnotation(StatisticsEndpointName.class);
        if (statisticsEndpointName != null) {
            statisticsRegistry.addVisit(statisticsEndpointName.value());
        } else {
            statisticsRegistry.addVisit("commonBucket");
        }

        return context.proceed();
    }
}
