package com.example.decorator;

import jakarta.annotation.Priority;
import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptor;

@Decorator
@Priority(Interceptor.Priority.APPLICATION + 6)
public class GreetingDecoratorBean2 implements GreetingBean {

    @Inject
    @Delegate
    @NormalGreeting
    //@Any
    GreetingBean delegate;


    @Override
    public String greet(int times) {
        return "_!!_ " + delegate.greet(times) + " _!!_";
    }
}
