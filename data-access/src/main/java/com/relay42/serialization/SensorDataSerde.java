package com.relay42.serialization;

import com.relay42.domain.SensorData;



public final class SensorDataSerde extends WrapperSerde<SensorData> {

    public SensorDataSerde() {

        super(new JsonSerializer<SensorData>(),
            new JsonDeserializer<SensorData>(SensorData.class));
    }
}