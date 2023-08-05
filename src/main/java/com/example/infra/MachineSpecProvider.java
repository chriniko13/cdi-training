package com.example.infra;

import io.quarkus.arc.Unremovable;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.Dependent;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

@Dependent
@MachineSpecProviderBasic

// Important Note: it is needed from Quarkus, so we can use CDI.select and pick it up with AnnotationLiteral<MachineSpecProviderBasic>
// https://quarkus.io/guides/cdi-reference#eliminate_false_positives
@Unremovable

public class MachineSpecProvider {

    private RuntimeMXBean runtimeMXBean;


    @PostConstruct
    public void init() {
        runtimeMXBean = ManagementFactory.getRuntimeMXBean();
    }

    public String getJvmName() {
        return runtimeMXBean.getName();
    }
}
