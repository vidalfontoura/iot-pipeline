package com.relay42.rest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import com.relay42.request.GetSensorDataRequest;
import com.relay42.response.GetSensorDataResponse;
import com.relay42.service.GetSensorService;

import java.util.Date;
import java.util.DoubleSummaryStatistics;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;

@Path("sensor")
public class GetSensorDataRest {

    private GetSensorService service;

    @Inject
    public GetSensorDataRest(GetSensorService service) {

        this.service = service;
    }


    @POST
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    @RequiresPermissions("getsensordata:allowed")
    @RequiresAuthentication
    public Response getSensorStatistics(GetSensorDataRequest query) {

        String sensorId = query.getSensorId();
        Date startDate = query.getStart();
        Date endDate = query.getEnd();

        DoubleSummaryStatistics stats =
            service.getSensorStats(sensorId, startDate, endDate);

        if (stats == null) {
            return Response.noContent().build();
        } else {
            GetSensorDataResponse mapStatisticsToResponse =
                mapStatisticsToResponse(stats);
            return Response.ok(mapStatisticsToResponse).build();
        }

    }

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