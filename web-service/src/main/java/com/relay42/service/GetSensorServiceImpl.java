package com.relay42.service;

import com.relay42.dao.Dao;
import com.relay42.domain.SensorData;

import java.util.Date;
import java.util.DoubleSummaryStatistics;
import java.util.List;

import javax.inject.Inject;


public class GetSensorServiceImpl implements GetSensorService {

    private Dao dao;

    @Inject
    public GetSensorServiceImpl(Dao dao) {

        this.dao = dao;
    }

    public DoubleSummaryStatistics getSensorStats(String sensorId,
                                                  Date startDate, Date endDate) {
        
        List<SensorData> queryByTimeFrame =
                        dao.querySensorDataByTimeFrame(sensorId, startDate, endDate);
        if (queryByTimeFrame != null && !queryByTimeFrame.isEmpty()) {
            return queryByTimeFrame.stream()
                .mapToDouble(v -> v.getReadingValue()).summaryStatistics();
        }
        return null;
        
    }

}
