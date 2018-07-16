package com.relay42.event.consumer;

import com.google.inject.Inject;
import com.relay42.dao.Dao;
import com.relay42.domain.SensorData;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class EventConsumerImpl implements EventConsumer {

    private static final Logger LOGGER = Logger.getLogger(EventConsumerImpl.class.getName());

    private Consumer<String, SensorData> consumer;
    private Dao dao;

    @Inject
    public EventConsumerImpl(KafkaConsumer<String, SensorData> consumer, Dao dao) {

        this.dao = dao;
        this.consumer = consumer;

    }

    public void run() {

        while (true) {
            final ConsumerRecords<String, SensorData> consumerRecords =
                consumer.poll(1000);

            consumerRecords.forEach(record -> {
                try {

                    SensorData sensorData = record.value();
                    LOGGER.log(Level.INFO,
                        "Received and saving sensor data into data store");
                    dao.save(sensorData);
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Error while reading message: "
                        + record.value(), e.getMessage());
                }
            });
            consumer.commitAsync();
        }
    }
    
}