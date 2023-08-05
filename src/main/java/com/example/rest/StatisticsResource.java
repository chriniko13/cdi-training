package com.example.rest;

import com.example.interceptor.statistic.StatisticsRegistry;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;


/**
 * Provides the statistics view, which are updated by using interceptor and pointcut(intercept) methods, etc.
 * <p>
 * For example have a look on {@link AsyncResource#doAsyncCalc()} which is annotated with:
 * <p>
 * {@link com.example.interceptor.statistic.StatisticsAware} and {@link com.example.interceptor.statistic.StatisticsEndpointName}
 * <p>
 * So it will get intercepted from {@link com.example.interceptor.statistic.StatisticsInterceptor} which will update the {@link StatisticsRegistry}
 */
@Path("/statistics")
@ApplicationScoped
public class StatisticsResource {

    private static final Logger LOG = Logger.getLogger(StatisticsResource.class);

    @Inject
    StatisticsRegistry statisticsRegistry;

    @GET
    public Response getStatistics() {
        return Response.ok(statisticsRegistry.getView()).build();
    }
}
