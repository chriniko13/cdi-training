package com.example.myscope;

import jakarta.enterprise.context.spi.Contextual;
import jakarta.enterprise.context.spi.CreationalContext;

public class BeanInstance<T> {

    private final T instance;
    private final Contextual<T> contextual;
    private final CreationalContext<T> creationalContext;

    public BeanInstance(T instance, Contextual<T> contextual, CreationalContext<T> creationalContext) {
        this.instance = instance;
        this.contextual = contextual;
        this.creationalContext = creationalContext;
    }

    T get() {
        return instance;
    }

    void destroy() {
        contextual.destroy(instance, creationalContext);
    }
}
