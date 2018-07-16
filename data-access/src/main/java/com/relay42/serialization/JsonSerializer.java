package com.relay42.serialization;

import com.google.gson.Gson;

import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;

public class JsonSerializer<T> implements Serializer<T> {

    private Gson gson = new Gson();

    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public byte[] serialize(String topic, T t) {

        return gson.toJson(t).getBytes();
    }

    @Override
    public void close() {

    }
}