package com.example.dynamicbeans;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.AfterBeanDiscovery;
import jakarta.enterprise.inject.spi.Extension;

public class MyStringBeanDynamicExtension implements Extension {

    public void afterBean(@Observes AfterBeanDiscovery afterBeanDiscovery) {
        afterBeanDiscovery
              .addBean(new MyStringBean());
    }
}
    
