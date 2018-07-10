package com.relay42;

import com.relay42.sensor.simulator.EventProducerImpl;
import com.relay42.serializer.JacksonReadingSerializer;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

/**
 * Main class to bootstrap the producer application
 *
 */
public class MainProducer {

    private static final Logger LOGGER = Logger.getLogger(MainProducer.class.getName());

    public static void main(String[] args) throws Exception {

        Properties props = new Properties();
        if (args.length == 1) {
            FileInputStream fileInputStream = new FileInputStream(args[0]);
            props.load(fileInputStream);
        } else {
            LOGGER.severe("Usage: Please provide a config.properties to setup the sensor simulator");
            System.exit(1);
        }
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonReadingSerializer.class.getName());
        props.put(ProducerConfig.ACKS_CONFIG, "1");

        EventProducerImpl eventConsumer = new EventProducerImpl(props);

        eventConsumer.run();
    }
}
