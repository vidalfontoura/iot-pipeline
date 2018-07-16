package com.relay42.serializer;

import static com.relay42.serializer.JacksonReadingSerializer.SerializationHelper.from;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.relay42.domain.SensorData;

import java.io.Closeable;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

/**
 * (De)serializes SensorData using Jackson.
 */
public class JacksonReadingSerializer implements Closeable, AutoCloseable, Serializer<SensorData>, Deserializer<SensorData> {
    private ObjectMapper mapper;

    public JacksonReadingSerializer() {
        this(null);
    }

    public JacksonReadingSerializer(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public static JacksonReadingSerializer defaultConfig() {
        return new JacksonReadingSerializer(new ObjectMapper());
    }

    @Override
    public void configure(Map<String, ?> map, boolean b) {
        if(mapper == null) {
            mapper = new ObjectMapper();
        }
    }

    @Override
    public byte[] serialize(String s, SensorData sensorData) {
        try {
            return mapper.writeValueAsBytes(from(sensorData));
        }
        catch(JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public SensorData deserialize(String s, byte[] bytes) {
        try {
            return mapper.readValue(bytes, SerializationHelper.class).to();
        }
        catch(IOException e) {
            throw new IllegalArgumentException(e);
        }
    }


    @Override
    public void close() {
        mapper = null;
    }

    public static class SerializationHelper {
        public String sensorId;
        public Date readingDate;
        public double readingValue;

        public static SerializationHelper from(SensorData sensorData) {
            SerializationHelper helper = new SerializationHelper();
            helper.sensorId = sensorData.getSensorId();
            helper.readingValue = sensorData.getReadingValue();
            helper.readingDate = sensorData.getReadingDate();

            return helper;
        }

        public SensorData to() {

            SensorData sensorData = new SensorData();
            sensorData.setReadingValue(readingValue);
            sensorData.setReadingDate(readingDate);
            sensorData.setSensorId(sensorId);
            return sensorData;
        }
    }
}