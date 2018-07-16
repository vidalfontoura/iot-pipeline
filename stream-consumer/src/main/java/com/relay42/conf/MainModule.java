package com.relay42.conf;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import com.relay42.config.PersistenceModule;
import com.relay42.constants.Constants;
import com.relay42.domain.SensorData;
import com.relay42.event.consumer.EventConsumer;
import com.relay42.event.consumer.EventConsumerImpl;

import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.KafkaConsumer;

public class MainModule extends AbstractModule {

    private Properties properties;

    public MainModule(Properties properties) {

        this.properties = properties;
    }

    @Override
    protected void configure() {

        install(new PersistenceModule());
        
        bind(EventConsumer.class).to(EventConsumerImpl.class);
        
        Names.bindProperties(binder(), this.properties);

    }

    @Provides
    public KafkaConsumer<String, SensorData> getKafkaConsumer() {

        KafkaConsumer<String, SensorData> consumer =
            new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList(properties
            .getProperty(Constants.SENSOR_DATA_TOPIC)));
        return consumer;

    }

    @Provides
    public Properties getProperies() {
        
        return this.properties;
    }


}
