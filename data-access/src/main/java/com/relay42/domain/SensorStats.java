package com.relay42.domain;


public class SensorStats {

    String sensorId;
    int countReads;
    double sum;
    double min;
    double average;
    double max;

    public SensorStats add(SensorData sensorData) {

        if (countReads == 0)
            this.min = sensorData.getReadingValue();

        this.countReads = this.countReads + 1;
        this.sensorId = sensorData.getSensorId();
        this.sum = this.sum + sensorData.getReadingValue();
        this.min =
            this.min < sensorData.getReadingValue() ? this.min
                : sensorData.getReadingValue();

        this.max =
            this.max > sensorData.getReadingValue() ? this.max : sensorData
                .getReadingValue();

        return this;
    }

    public SensorStats computeAvgPrice() {

        this.average = this.sum / this.countReads;
        return this;
    }

    @Override
    public String toString() {

        return "SensorStats [sensorId=" + sensorId + ", countReads="
            + countReads + ", sum=" + sum + ", min=" + min + ", average="
            + average + ", max=" + max + "]";
    }


}