package com.relay42.conf;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ServiceModule extends AbstractModule {

    private static final Logger LOGGER = Logger.getLogger(ServiceModule.class
        .getName());

    @Override
    protected void configure() {

        Properties defaults = new Properties();
        try {
            Properties props = new Properties(defaults);
            props.load(new FileInputStream("config.properties"));
            Names.bindProperties(binder(), props);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Could not load config: ", e);
            System.exit(1);
        }

    }

}
