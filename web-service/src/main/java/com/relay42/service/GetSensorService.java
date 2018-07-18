package com.relay42.service;

import java.util.Date;
import java.util.DoubleSummaryStatistics;


public interface GetSensorService {
    
    DoubleSummaryStatistics getSensorStats(String sensorId, Date startDate,
                                           Date endDate);
}
