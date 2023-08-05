package com.example.dynamicbeans;

import jakarta.enterprise.context.spi.Contextual;
import jakarta.enterprise.context.spi.CreationalContext;

public class StringContextual implements Contextual<String> {

    @Override
    public String create(CreationalContext<String> creationalContext) {
        return "some bean...";
    }

    @Override
    public void destroy(String instance, CreationalContext<String> creationalContext) {
        // Note: not needed.
    }
}
