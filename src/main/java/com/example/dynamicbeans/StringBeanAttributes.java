package com.example.dynamicbeans;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Default.Literal;
import jakarta.enterprise.inject.spi.BeanAttributes;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Set;

public class StringBeanAttributes implements BeanAttributes<String> {

    @Override
    public Set<Type> getTypes() {
        return Set.of(String.class, Object.class);
    }

    @Override
    public Set<Annotation> getQualifiers() {
        return Set.of(Literal.INSTANCE);
    }

    @Override
    public Class<? extends Annotation> getScope() {
        return Dependent.class;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Set<Class<? extends Annotation>> getStereotypes() {
        return Collections.emptySet();
    }

    @Override
    public boolean isAlternative() {
        return false;
    }
}
