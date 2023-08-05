package com.example.observer;

import com.example.observer.event.HelloEvent;
import com.example.observer.event.HelloSpecializedQualifier;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.EventMetadata;
import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.interceptor.Interceptor;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.List;
import org.jboss.logging.Logger;

@ApplicationScoped
public class HelloEventObserver implements Serializable {

    private static final Logger LOG = Logger.getLogger(HelloEventObserver.class);

    public void firstObserver(@Observes @Priority(Interceptor.Priority.APPLICATION) HelloEvent event) {
        LOG.info("Called first: " + event);
    }

    public void secondObserver(@Observes @Priority(Interceptor.Priority.APPLICATION + 5) HelloEvent event) {
        LOG.info("Called second: " + event);
    }

    public void thirdObserver(@Observes HelloEvent event) {
        LOG.info("Called third: " + event);
    }

    public void observerWithMetadata(@Observes HelloEvent event, EventMetadata eventMetadata) {
        InjectionPoint injectionPoint = eventMetadata.getInjectionPoint();
        Bean<?> bean = injectionPoint.getBean();

        String injectionPointBeanClassName = bean.getBeanClass().getName();
        Class<? extends Annotation> beanScope = bean.getScope();

        List<String> annotatedWithQualifiers = eventMetadata.getQualifiers().stream()
              .map(Annotation::toString)
              .toList();

        LOG.info("Called observer with metadata: " + event
              + " --- injectionPoint class name: " + injectionPointBeanClassName
              + " --- injectionPoint bean scope: " + beanScope
              + " --- annotatedWithQualifiers: " + annotatedWithQualifiers);
    }


    public void observerSpecialized(@Observes @HelloSpecializedQualifier HelloEvent event) {
        LOG.info("Called specialized: " + event);
    }

    public void someObserver(@Observes @HelloSpecializedQualifier String msg) {
        LOG.info("Some observer: " + msg);
    }
}
