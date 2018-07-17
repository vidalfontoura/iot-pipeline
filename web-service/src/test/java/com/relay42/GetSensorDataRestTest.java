package com.relay42;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.relay42.dao.Dao;
import com.relay42.domain.SensorData;
import com.relay42.request.GetSensorDataRequest;
import com.relay42.response.GetSensorDataResponse;
import com.relay42.rest.GetSensorDataRest;

import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GetSensorDataRestTest {

    private GetSensorDataRest resource;

    @Mock
    private Dao dao;

    @Before
    public void setup() {


        when(dao.querySensorDataByTimeFrame(any(), any(), any()))
            .thenReturn(
            buildSensorDataMock());

        resource = new GetSensorDataRest(dao);
    }

    @Test
    public void testResource() {
        GetSensorDataRequest query = new GetSensorDataRequest();
        query.setEnd(new Date());
        query.setStart(new Date());
        query.setSensorId("sen1");

        Response sensorStatistics = resource.getSensorStatistics(query);

        GetSensorDataResponse response =
            (GetSensorDataResponse) sensorStatistics.getEntity();

        Assert.assertEquals(2.375, response.getAverage());
        Assert.assertEquals(4.0, response.getCount());

        Assert.assertEquals(4.0, response.getMax());
        Assert.assertEquals(1.0, response.getMin());
        Assert.assertEquals(9.5, response.getSum());

    }

    private List<SensorData> buildSensorDataMock() {

        List<SensorData> list = Lists.newArrayList();
        SensorData sensorData = new SensorData();
        sensorData.setReadingDate(new Date());
        sensorData.setReadingValue(1.0);
        sensorData.setSensorId("sen1");
        list.add(sensorData);
        sensorData = new SensorData();
        sensorData.setReadingDate(new Date());
        sensorData.setReadingValue(2.0);
        sensorData.setSensorId("sen1");
        list.add(sensorData);
        sensorData = new SensorData();
        sensorData.setReadingDate(new Date());
        sensorData.setReadingValue(4.0);
        sensorData.setSensorId("sen1");
        list.add(sensorData);

        sensorData = new SensorData();
        sensorData.setReadingDate(new Date());
        sensorData.setReadingValue(2.5);
        sensorData.setSensorId("sen1");
        list.add(sensorData);
        return list;
    }
}
