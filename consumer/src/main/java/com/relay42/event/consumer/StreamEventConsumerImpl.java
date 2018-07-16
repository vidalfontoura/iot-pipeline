package com.relay42.event.consumer;

import com.google.inject.Inject;
import com.relay42.constants.Constants;
import com.relay42.dao.Dao;
import com.relay42.domain.SensorData;
import com.relay42.domain.SensorStats;
import com.relay42.domain.Window;
import com.relay42.serialization.JsonDeserializer;
import com.relay42.serialization.JsonSerializer;
import com.relay42.serialization.WrapperSerde;

import java.util.Properties;
import java.util.logging.Logger;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.TimeWindows;

public class StreamEventConsumerImpl implements EventConsumer {

    private static final Logger LOGGER = Logger
        .getLogger(EventConsumerImpl.class.getName());

    private Properties props;
    private Dao dao;

    @Inject
    public StreamEventConsumerImpl(Properties properties, Dao dao) {

        this.props = properties;
        this.dao = dao;
    }

    public void run() {

        KStreamBuilder builder = new KStreamBuilder();

        KStream<String, SensorData> source =
            builder.stream(props.getProperty(Constants.SENSOR_DATA_TOPIC));

        source
            .groupByKey()
            .aggregate(SensorStats::new, (k, v, sensorStats) -> {
                LOGGER.info("Receiving sensor readings from sensor id: " + k);
                dao.save(v);
                sensorStats.add(v);
                return sensorStats;
            }, TimeWindows.of(5000).advanceBy(1000), new SensorStatsSerde(),
                "sensor-stats-store")
            .toStream(
                (key, value) -> new Window(key.key(), key.window().start()))
            .mapValues((sensor) -> sensor.computeAvgPrice())
            .to(new WindowSerde(), new SensorStatsSerde(),
                props.getProperty(Constants.SENSOR_STATS_TOPIC));

        KafkaStreams streams = new KafkaStreams(builder, props);

        streams.cleanUp();
        streams.start();


    }

    static public final class SensorDataSerde extends WrapperSerde<SensorData> {

        public SensorDataSerde() {

            super(new JsonSerializer<SensorData>(),
                new JsonDeserializer<SensorData>(SensorData.class));
        }
    }


    static public final class SensorStatsSerde extends
        WrapperSerde<SensorStats> {

        public SensorStatsSerde() {

            super(new JsonSerializer<SensorStats>(),
                new JsonDeserializer<SensorStats>(SensorStats.class));
        }
    }

    static public final class WindowSerde extends WrapperSerde<Window> {

        public WindowSerde() {

            super(new JsonSerializer<Window>(), new JsonDeserializer<Window>(
                Window.class));
        }
    }

    
}