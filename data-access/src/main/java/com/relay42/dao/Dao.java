package com.relay42.dao;

import com.relay42.domain.SensorData;

import java.util.Date;
import java.util.List;

public interface Dao {

    void saveSensorData(SensorData sensorData);

    SensorData querySensorDataById(String id);

    List<SensorData> querySensorDataByIds(List<String> ids);
    
    List<SensorData> querySensorDataByTimeFrame(String sensorId, Date start, Date end);
}
