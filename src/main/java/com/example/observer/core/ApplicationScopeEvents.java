package com.example.observer.core;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.BeforeDestroyed;
import jakarta.enterprise.context.Destroyed;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ApplicationScopeEvents {
    private static final Logger LOG = Logger.getLogger(ApplicationScopeEvents.class);

    public void onStartUpEvent(@Observes StartupEvent startupEvent) {
        LOG.info("startup event: " + startupEvent);
    }

    public void onInitialization(@Observes @Initialized(ApplicationScoped.class) Object o) {
        LOG.info("application scoped bean initialized, bean: " + o);
    }

    public void onBeforeDestruction(@Observes @BeforeDestroyed(ApplicationScoped.class) Object o) {
        LOG.info("application scoped bean about to be destroyed, bean: " + o);
    }

    public void onDestroyed(@Observes @Destroyed(ApplicationScoped.class) Object o) {
        LOG.info("application scoped bean destroyed, bean: " + o);
    }
}
