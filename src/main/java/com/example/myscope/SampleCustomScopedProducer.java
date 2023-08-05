package com.example.myscope;

import jakarta.enterprise.inject.Produces;

public class SampleCustomScopedProducer {

    @Produces
    @CustomScoped
    public SampleCustomScoped provideSampleCustomScoped() {
        return new SampleCustomScoped();
    }
}
