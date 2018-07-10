package com.relay42.response;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GetSensorDataResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private double average;
    private double count;
    private double max;
    private double min;
    private double sum;

    public GetSensorDataResponse() {

    }

    public double getSum() {

        return sum;
    }

    public void setSum(double sum) {

        this.sum = sum;
    }

    public double getAverage() {

        return average;
    }

    public void setAverage(double average) {

        this.average = average;
    }

    public double getCount() {

        return count;
    }

    public void setCount(double median) {

        this.count = median;
    }

    public double getMax() {

        return max;
    }

    public void setMax(double max) {

        this.max = max;
    }

    public double getMin() {

        return min;
    }

    public void setMin(double min) {

        this.min = min;
    }

}
