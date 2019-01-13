package com.d4h.application.model.user;


/**
 * Representation of health parameters to be sent to users and third parties.
 */
public class HealthParametersSent {
    private int minHeartBeat;
    private int maxHeartBeat;
    private int avgHeartBeat;
    private int minMinPressure;
    private int maxMaxPressure;
    private double minTemperature;
    private double maxTemperature;
    private int height;
    private int weight;

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getMinHeartBeat() {
        return minHeartBeat;
    }

    public void setMinHeartBeat(int minHeartBeat) {
        this.minHeartBeat = minHeartBeat;
    }

    public int getMaxHeartBeat() {
        return maxHeartBeat;
    }

    public void setMaxHeartBeat(int maxHeartBeat) {
        this.maxHeartBeat = maxHeartBeat;
    }

    public int getAvgHeartBeat() {
        return avgHeartBeat;
    }

    public void setAvgHeartBeat(int avgHeartBeat) {
        this.avgHeartBeat = avgHeartBeat;
    }

    public int getMinMinPressure() {
        return minMinPressure;
    }

    public void setMinMinPressure(int minMinPressure) {
        this.minMinPressure = minMinPressure;
    }

    public int getMaxMaxPressure() {
        return maxMaxPressure;
    }

    public void setMaxMaxPressure(int maxMaxPressure) {
        this.maxMaxPressure = maxMaxPressure;
    }

    public double getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(double minTemperature) {
        this.minTemperature = minTemperature;
    }

    public double getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }
}
