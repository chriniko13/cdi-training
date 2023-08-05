package com.example.dynamicbeans;

import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.enterprise.inject.spi.PassivationCapable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Set;

public class MyStringBean implements Bean<String>, PassivationCapable {

    private final StringBeanAttributes stringBeanAttributes = new StringBeanAttributes();

    private final StringContextual stringContextual = new StringContextual();

    @Override
    public Class<?> getBeanClass() {
        return getClass();
    }

    @Override
    public Set<InjectionPoint> getInjectionPoints() {
        return Collections.emptySet();
    }


    // -----
    @Override
    public String create(CreationalContext<String> creationalContext) {
        return stringContextual.create(creationalContext);
    }

    @Override
    public void destroy(String instance, CreationalContext<String> creationalContext) {
        stringContextual.destroy(instance, creationalContext);
    }


    // -----
    @Override
    public Set<Type> getTypes() {
        return stringBeanAttributes.getTypes();
    }

    @Override
    public Set<Annotation> getQualifiers() {
        return stringBeanAttributes.getQualifiers();
    }

    @Override
    public Class<? extends Annotation> getScope() {
        return stringBeanAttributes.getScope();
    }

    @Override
    public String getName() {
        return stringBeanAttributes.getName();
    }

    @Override
    public Set<Class<? extends Annotation>> getStereotypes() {
        return stringBeanAttributes.getStereotypes();
    }

    @Override
    public boolean isAlternative() {
        return stringBeanAttributes.isAlternative();
    }


    // -----
    @Override
    public String getId() {
        return getClass().getName();
    }
}
