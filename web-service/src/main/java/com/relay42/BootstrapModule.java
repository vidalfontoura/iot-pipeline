package com.relay42;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.relay42.service.GetSensorService;
import com.relay42.service.GetSensorServiceImpl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;



public class BootstrapModule extends AbstractModule {

    @Override
    protected void configure() {

        try {
            bind(GetSensorService.class).to(GetSensorServiceImpl.class);

            Properties props = new Properties();
            props.load(new FileInputStream(
                GenericBootstrapConstants.BOOTSTRAP_PROPERTIES_FILE));
            Names.bindProperties(binder(), props);
        } catch (FileNotFoundException e) {
            System.out.println("The configuration file "
                + GenericBootstrapConstants.BOOTSTRAP_PROPERTIES_FILE
                + " can not be found");
        } catch (IOException e) {
            System.out.println("I/O Exception during loading configuration");
        }
    }

}