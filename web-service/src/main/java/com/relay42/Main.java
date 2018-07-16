package com.relay42;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.relay42.conf.ServiceModule;
import com.relay42.config.PersistenceModule;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class Main extends GuiceServletContextListener {

    @Override
    protected Injector getInjector() {

        return Guice.createInjector(new ServletModule() {

            @Override
            protected void configureServlets() {

                ResourceConfig rc = new PackagesResourceConfig("com.relay42.rest");
                for (Class<?> resource : rc.getClasses()) {
                    bind(resource);
                }
                
                install(new ServiceModule());
                install(new PersistenceModule());

                serve("/services/*").with(GuiceContainer.class);
            }
        });
    }
}