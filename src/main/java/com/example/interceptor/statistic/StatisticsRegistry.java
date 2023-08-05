package com.example.interceptor.statistic;

import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

@ApplicationScoped
@Startup
public class StatisticsRegistry implements Serializable {

    private ConcurrentHashMap<String, LongAdder> visitsByEndpointName;

    @PostConstruct
    public void init() {
        visitsByEndpointName = new ConcurrentHashMap<>();
    }

    public void addVisit(final String endpointName) {
        visitsByEndpointName.compute(endpointName, (s, longAdder) -> {
            if (longAdder == null) {
                longAdder = new LongAdder();
            }
            longAdder.increment();
            return longAdder;
        });
    }

    public Map<String, Long> getView() {
        final Map<String, Long> result = new LinkedHashMap<>();

        visitsByEndpointName.forEach((s, longAdder) -> {
            result.put(s, longAdder.sum());
        });

        return result;
    }

    private Optional<Long> getVisits(final String endpointName) {
        return Optional
              .ofNullable(visitsByEndpointName.get(endpointName))
              .map(LongAdder::sum);
    }
}
