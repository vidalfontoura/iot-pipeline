package com.relay42.event.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.relay42.dao.Dao;
import com.relay42.domain.SensorData;

import java.util.Collections;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class EventConsumerImpl implements EventConsumer {

    private static final Logger LOGGER = Logger.getLogger(EventConsumerImpl.class.getName());


    public final static String TOPIC_NAME_KEY = "topic_name";

    private Consumer<String, SensorData> consumer;
    private Dao dao;
    private ObjectMapper mapper;

    public EventConsumerImpl(Properties props, Dao dao) {

        this.dao = dao;
        // Create the consumer using props.
        this.consumer = new KafkaConsumer<>(props);
        // Subscribe to the topic.
        this.consumer.subscribe(Collections.singletonList(props.getProperty(TOPIC_NAME_KEY)));

        this.mapper = new ObjectMapper();
    }


    public void run() {

        final int giveUp = 1000;
        int noRecordsCount = 0;
        while (true) {
            final ConsumerRecords<String, SensorData> consumerRecords = consumer.poll(1000);
            if (consumerRecords.count() == 0) {
                noRecordsCount++;
                if (noRecordsCount > giveUp)
                    break;
                else
                    continue;
            }
            consumerRecords.forEach(record -> {
                try {
                    System.out.printf("Consumer Record:(%s, %s, %d, %d)\n", record.key(), record.value(), record.partition(), record.offset());
                    SensorData sensorData = record.value();
                    dao.save(sensorData);
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Error while reading message: "+record.value(), e.getMessage());
                }
            });
            consumer.commitAsync();
        }
        consumer.close();
        System.out.println("DONE");
    }

    
}