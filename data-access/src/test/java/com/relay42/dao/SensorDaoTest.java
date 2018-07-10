package com.relay42.dao;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.CodecRegistry;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.TypeCodec;
import com.relay42.codec.DateCodec;
import com.relay42.domain.SensorData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;


public class SensorDaoTest {

    private Dao dao;

    @Before
    public void setup() {

        CodecRegistry codecRegistry = new CodecRegistry();
        codecRegistry.register(new DateCodec(TypeCodec.date(), Date.class));

        Session session = Cluster.builder().withCodecRegistry(codecRegistry).addContactPoint("127.0.0.1").build().connect("relay42");
        dao = new SensorDao(session);
    }

    @Test
    public void testQueryByTimeFrame() throws ParseException {
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date start = sdf.parse("2018-07-07 13:41:36");
        Date end = sdf.parse("2018-07-07 13:41:38");
        List<SensorData> queryByTimeFrame = dao.queryByTimeFrame("fbdb9aba-c6db-46d3-985d-dd20137e160a", start, end);
        for (SensorData sensorData : queryByTimeFrame) {
            System.out.print(sensorData.getReadingDate());
            System.out.println(" " + sensorData.getReadingValue());
        }
    }
}
