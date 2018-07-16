package com.relay42.conf;

import com.google.inject.AbstractModule;
import com.relay42.config.PersistenceModule;

public class ConsumerModule extends AbstractModule {

    @Override
    protected void configure() {

        install(new PersistenceModule());

    }

}
