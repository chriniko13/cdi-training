package com.example.rest;

import com.example.observer.event.AsyncComputationEvent;
import com.example.interceptor.statistic.StatisticsAware;
import com.example.interceptor.statistic.StatisticsEndpointName;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.Instant;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadLocalRandom;
import org.jboss.logging.Logger;

/**
 * Provides an example on how to trigger an asynchronous event, which will be picked up from an observer.
 * <p></p>
 * Moreover, it demonstrates a usage of cdi interceptor with the use of: {@link StatisticsAware} and {@link StatisticsEndpointName} which the
 * {@link AsyncResource#doAsyncCalc()} will get intercepted from {@link com.example.interceptor.statistic.StatisticsInterceptor}
 */
@Path("/async")
@ApplicationScoped
public class AsyncResource {

    private static final Logger LOG = Logger.getLogger(AsyncResource.class);

    @Inject
    ObjectMapper mapper;

    @Inject
    Event<AsyncComputationEvent> asyncComputationEvent;


    @StatisticsAware
    @StatisticsEndpointName("/async")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response doAsyncCalc() {

        ObjectNode result = mapper.createObjectNode()
              .put("success", true)
              .put("occurredAt", Instant.now().toString());

        asyncComputationEvent
              .fireAsync(
                    new AsyncComputationEvent(ThreadLocalRandom.current().nextInt(5) + 1)
              )
              .handleAsync((outcome, error) -> {
                        if (error != null) {
                            LOG.info("failure occurred during async computation, error: {}", error.getMessage(), error);
                        } else {
                            LOG.info("async computation was successful");
                        }
                        return null;
                    }, ForkJoinPool.commonPool()
              );

        return Response.ok(result).build();
    }

}
