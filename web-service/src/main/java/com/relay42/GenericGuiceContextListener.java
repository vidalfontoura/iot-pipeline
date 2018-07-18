package com.relay42;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;


public class GenericGuiceContextListener extends GuiceServletContextListener {

    @Override
    protected Injector getInjector() {

        return Guice.createInjector(new BootstrapServletModule());
    }


}