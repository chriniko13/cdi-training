package com.example.producer;

import jakarta.enterprise.context.Dependent;
import jakarta.ws.rs.Produces;

public class DummyServiceProducer {


    @Produces
    @Dependent
    DummyService produceDummyService() {
        return new DummyService();
    }
}
