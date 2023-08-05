package com.example.decorator;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@NormalGreeting
public class GreetingBeanBasic implements GreetingBean {

    @Override
    public String greet(int times) {
        final StringBuilder sb = new StringBuilder();

        for (int i = 0; i < times; i++) {
            sb.append("hello!");
            if (i < times - 1) {
                sb.append(" ");
            }
        }

        return sb.toString();
    }

}
