package com.relay42.dao;

import com.relay42.domain.SensorData;

import java.util.Date;
import java.util.List;

public interface Dao {

    void save(SensorData sensorData);

    SensorData queryById(String id);

    List<SensorData> queryByIds(List<String> ids);
    
    List<SensorData> queryByTimeFrame(String sensorId, Date start, Date end);
}
