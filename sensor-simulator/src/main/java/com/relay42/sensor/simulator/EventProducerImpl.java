package com.relay42.sensor.simulator;

import com.relay42.domain.SensorData;

import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

public class EventProducerImpl implements EventProducer {

    private static final Logger LOGGER = Logger.getLogger(EventProducerImpl.class.getName());

    private static final String TOPIC_NAME_KEY = "topic.name";

    private final Random random;

    private Producer<String, SensorData> kafkaProducer;
    private Properties props;
    private String sensorId;

    public EventProducerImpl(Properties props, String sensorId) {

        this.kafkaProducer = new KafkaProducer<>(props);
        this.props = props;
        this.random = new Random();
        this.sensorId = sensorId;
    }


    @Override
    public void run() {

        try {
            produce();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private void produce() throws Exception {

        ProducerRecord<String, SensorData> record = null;

        while (true) {
            SensorData sensorData = new SensorData();

            sensorData.setSensorId(sensorId);
            sensorData.setReadingDate(new Date());
            sensorData.setReadingValue(random.nextDouble());

            record =
                new ProducerRecord<>(props.getProperty(TOPIC_NAME_KEY),
                    sensorId,
                    sensorData);
            kafkaProducer.send(record, new Callback() {

                @Override
                public void onCompletion(RecordMetadata rm, Exception excptn) {

                    if (excptn != null) {
                        LOGGER.log(Level.WARNING, "Error sending sensorId data {0}\n{1}", new Object[] { sensorData.getSensorId(), excptn.getMessage() });
                    } else {
                        LOGGER.log(Level.INFO, "Data for sensorId {0} added in partition {1}", new Object[] { sensorData.getSensorId(), rm.partition() });
                    }

                }
            });
            Thread.sleep(1000);
        }
    }

    
}