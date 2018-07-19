package com.relay42.dao;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.relay42.domain.SensorData;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public class SensorDao implements Dao {

    private static final Logger LOGGER = Logger.getLogger(SensorDao.class
        .getName());

    private Mapper<SensorData> mapper;
    private Session session;

    @Inject
    public SensorDao(Session session) {

        MappingManager manager = new MappingManager(session);
        this.mapper = manager.mapper(SensorData.class);
        this.session = session;
    }

    public void saveSensorData(SensorData sensorData) {

        mapper.saveAsync(sensorData);
    }

    public SensorData querySensorDataById(String id) {

        return mapper.get(id);
    }

    public List<SensorData> querySensorDataByIds(List<String> ids) {

        List<SensorData> lists = Lists.newArrayList();
        for (String i : ids) {
            SensorData queryById = this.querySensorDataById(i);
            lists.add(queryById);
        }
        return lists;
    }

    public List<SensorData> querySensorDataByTimeFrame(String sensorId, Date start, Date end) {

        try {
            
            Statement statement =
                QueryBuilder.select().all().from("sensor_data").where(QueryBuilder.eq("sensorId", sensorId)).and(QueryBuilder.gte("readingDate", start)).and(QueryBuilder.lte("readingDate", end));
            
            ResultSet resultSet = session.execute(statement);

            Result<SensorData> sensorData = mapper.map(resultSet);

            return sensorData.all();

        } catch (IllegalArgumentException e) {

            LOGGER.severe("Error ocurred while queryin sensor data: "
                + e.getMessage());
        }
        return null;
    }

}
