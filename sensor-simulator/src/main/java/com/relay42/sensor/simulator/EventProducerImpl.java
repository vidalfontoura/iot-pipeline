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
    private static final String SENSOR_ID = "sensor.id";

    Random random = new Random();

    private Producer<String, SensorData> kafkaProducer;
    private Properties props;

    public EventProducerImpl(Properties props) {

        this.kafkaProducer = new KafkaProducer<>(props);
        this.props = props;
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

            sensorData.setSensorId(props.getProperty(SENSOR_ID));
            sensorData.setReadingDate(new Date());
            sensorData.setReadingValue(random.nextDouble());
            record = new ProducerRecord<>(props.getProperty(TOPIC_NAME_KEY), sensorData);

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

        }
    }

    
}