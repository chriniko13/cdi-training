package com.example.rest;

import com.example.myscope.SampleCustomScoped;
import com.example.observer.event.HelloEvent;
import com.example.observer.event.HelloSpecializedQualifier;
import com.example.observer.event.HelloSpecializedQualifier.Literal;
import com.example.producer.DummyService;
import com.example.service.ExampleService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.UUID;
import org.jboss.logging.Logger;


/**
 * Demonstrates basic usages of CDI:
 * <ul>
 *     <li>- injecting a cdi bean</li>
 *     <li>- firing a CDI event with qualifier and with not a qualifier</li>
 *     <li>- injecting the {@link BeanManager} and doing the above programmatically</li>
 * </ul>
 */
@Path("/hello")
@ApplicationScoped
public class ExampleResource {

    private static final Logger LOG = Logger.getLogger(ExampleResource.class);

    @Inject
    ExampleService exampleService;

    @Inject
    SampleCustomScoped sampleCustomScoped;

    @Inject
    Event<HelloEvent> helloEvent;

    @Inject
    @HelloSpecializedQualifier
    Event<HelloEvent> helloSpecializedEvent;

    @Inject
    BeanManager beanManager;

    @Inject
    DummyService dummyService;


    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        LOG.info("HELLO REQUEST RECEIVED!!!");

        // ---
        HelloEvent evt = new HelloEvent(UUID.randomUUID().toString(), "hello there buddy");
        helloEvent.fire(evt);
        helloSpecializedEvent.fire(evt);

        // ---
        beanManager.getEvent()
              .select(String.class, new Literal() {
              })
              .fire("I got emitted from bean-manager");

        return exampleService.hello()
              + " --- " + sampleCustomScoped.ping()
              + " --- " + dummyService.execute();
    }
}
