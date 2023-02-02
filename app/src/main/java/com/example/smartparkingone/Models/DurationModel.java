package com.example.smartparkingone.Models;

public class DurationModel {
    String entryTime, bookedDuration, exitTime, parkedDuration, cost;
    public DurationModel() {
    }

    public DurationModel(String entryTime, String bookedDuration, String exitTime, String parkedDuration, String cost) {
        this.entryTime = entryTime;
        this.bookedDuration = bookedDuration;
        this.exitTime = exitTime;
        this.parkedDuration = parkedDuration;
        this.cost = cost;
    }

    public String getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(String entryTime) {
        this.entryTime = entryTime;
    }

    public String getBookedDuration() {
        return bookedDuration;
    }

    public void setBookedDuration(String bookedDuration) {
        this.bookedDuration = bookedDuration;
    }

    public String getExitTime() {
        return exitTime;
    }

    public void setExitTime(String exitTime) {
        this.exitTime = exitTime;
    }

    public String getParkedDuration() {
        return parkedDuration;
    }

    public void setParkedDuration(String parkedDuration) {
        this.parkedDuration = parkedDuration;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
}
