package com.relay42.domain;

import com.datastax.driver.mapping.annotations.Table;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@Table(name = "sensor_data")
@XmlRootElement
public class SensorData {

    private String sensorId;
    private double readingValue;
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


}
