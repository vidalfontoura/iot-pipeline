package com.relay42.domain;

import com.datastax.driver.mapping.annotations.Table;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@Table(name = "sensor_data")
@XmlRootElement
public class SensorData {

    private String sensorId;

    private double readingValue;

    @JsonFormat(shape = JsonFormat.Shape.STRING,
                pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
    private Date readingDate;

    public String getSensorId() {

        return sensorId;
    }

    public void setSensorId(String sensorId) {

        this.sensorId = sensorId;
    }

    public double getReadingValue() {

        return readingValue;
    }

    public void setReadingValue(double readingValue) {

        this.readingValue = readingValue;
    }

    public Date getReadingDate() {

        return readingDate;
    }

    public void setReadingDate(Date readingDate) {

        this.readingDate = readingDate;
    }

    @Override
    public String toString() {

        return "SensorData [sensorId=" + sensorId + ", readingValue="
            + readingValue + ", readingDate=" + readingDate + "]";
    }

}
