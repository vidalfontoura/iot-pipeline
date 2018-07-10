package com.relay42.request;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GetSensorDataRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    public GetSensorDataRequest() {

    }

    private String sensorId;

    private Date start;

    private Date end;

    public Date getStart() {

        return start;
    }

    public void setStart(Date start) {

        this.start = start;
    }

    public Date getEnd() {

        return end;
    }

    public void setEnd(Date end) {

        this.end = end;
    }

    public String getSensorId() {

        return sensorId;
    }

    public void setSensorId(String sensorId) {

        this.sensorId = sensorId;
    }

}
