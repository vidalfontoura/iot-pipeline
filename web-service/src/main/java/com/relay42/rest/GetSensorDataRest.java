package com.relay42.rest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import com.relay42.dao.Dao;
import com.relay42.domain.SensorData;
import com.relay42.request.GetSensorDataRequest;
import com.relay42.response.GetSensorDataResponse;

import java.util.Date;
import java.util.DoubleSummaryStatistics;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("sensor")
public class GetSensorDataRest {

    private Dao dao;

    @Inject
    public GetSensorDataRest(Dao dao) {

        this.dao = dao;
    }


    @POST
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public GetSensorDataResponse getSensorStatistics(GetSensorDataRequest query) {

        String sensorId = query.getSensorId();
        Date startDate = query.getStart();
        Date endDate = query.getEnd();

        List<SensorData> queryByTimeFrame = dao.queryByTimeFrame(sensorId, startDate, endDate);

        DoubleSummaryStatistics summaryStatistics = queryByTimeFrame.stream().mapToDouble(v -> v.getReadingValue()).summaryStatistics();

        return mapStatisticsToResponse(summaryStatistics);

    }

    // @GET
    // @Path("{id}")
    // @Produces(APPLICATION_JSON)
    // public SensorData getAllReadings(@PathParam("id") String id) {
    //
    // return dao.queryById(id);
    //
    // }

    private GetSensorDataResponse mapStatisticsToResponse(DoubleSummaryStatistics summaryStatistics) {

        GetSensorDataResponse getSensorDataResponse = new GetSensorDataResponse();
        getSensorDataResponse.setAverage(summaryStatistics.getAverage());
        getSensorDataResponse.setMax(summaryStatistics.getMax());
        getSensorDataResponse.setMin(summaryStatistics.getMin());
        getSensorDataResponse.setCount(summaryStatistics.getCount());
        getSensorDataResponse.setSum(summaryStatistics.getSum());
        return getSensorDataResponse;
    }


}