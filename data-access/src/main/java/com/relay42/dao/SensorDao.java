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

public class SensorDao implements Dao {

    private Mapper<SensorData> mapper;
    private Session session;

    @Inject
    public SensorDao(Session session) {

        MappingManager manager = new MappingManager(session);
        this.mapper = manager.mapper(SensorData.class);
        this.session = session;
    }

    public void save(SensorData sensorData) {

        mapper.saveAsync(sensorData);
    }

    public SensorData queryById(String id) {

        return mapper.get(id);
    }

    public List<SensorData> queryByIds(List<String> ids) {

        List<SensorData> lists = Lists.newArrayList();
        for (String i : ids) {
            SensorData queryById = this.queryById(i);
            lists.add(queryById);
        }
        return lists;
    }

    public List<SensorData> queryByTimeFrame(String sensorId, Date start, Date end) {

        try {
            
            Statement statement =
                QueryBuilder.select().all().from("sensor_data").where(QueryBuilder.eq("sensorId", sensorId)).and(QueryBuilder.gte("readingDate", start)).and(QueryBuilder.lte("readingDate", end));
            
            ResultSet resultSet = session.execute(statement);

            Result<SensorData> sensorData = mapper.map(resultSet);

            return sensorData.all();

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }

}
