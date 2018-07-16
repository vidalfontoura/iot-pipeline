package com.relay42;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.relay42.conf.MainModule;
import com.relay42.domain.SensorData;
import com.relay42.event.consumer.EventConsumer;
import com.relay42.serialization.JsonDeserializer;
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
        JsonDeserializer<SensorData> sensorDataSerde =
            new JsonDeserializer<SensorData>(SensorData.class);
        if (args.length == 1) {
            FileInputStream fileInputStream = new FileInputStream(args[0]);
            props.load(fileInputStream);
        } else {
            LOGGER.severe("Usage: Please provide a config.properties to setup sensor data consumer");
            System.exit(1);
        }
        // props.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.String()
        // .getClass().getName());
        // props.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, sensorDataSerde
        // .getClass().getName());

        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
            StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
            JacksonReadingSerializer.class.getName());

        Injector injector = Guice.createInjector(new MainModule(props));
        EventConsumer consumer = injector.getInstance(EventConsumer.class);
        consumer.run();
    }
}
