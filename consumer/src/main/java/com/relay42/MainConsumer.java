package com.relay42;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.relay42.config.PersistenceModule;
import com.relay42.dao.Dao;
import com.relay42.event.consumer.EventConsumerImpl;
import com.relay42.serializer.JacksonReadingSerializer;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

/**
 * Main class to bootstrap the consumer application
 *
 */
public class MainConsumer {

    private static final Logger LOGGER = Logger.getLogger(MainConsumer.class.getName());


    public static void main(String[] args) throws Exception {

        Properties props = new Properties();
        if (args.length == 1) {
            FileInputStream fileInputStream = new FileInputStream(args[0]);
            props.load(fileInputStream);
        } else {
            LOGGER.severe("Usage: Please provide a config.properties to setup sensor data consumer");
            System.exit(1);
        }
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JacksonReadingSerializer.class.getName());

        Injector injector = Guice.createInjector(new PersistenceModule());
        Dao dao = injector.getInstance(Dao.class);

        EventConsumerImpl eventConsumer = new EventConsumerImpl(props, dao);
        eventConsumer.run();
    }
}
