package com.example.smartparkingone.Models;

public class DurationModel {
    String entryTime, duration, exitTime;
    public DurationModel() {
    }

    public DurationModel(String entryTime,String duration, String exitTime){
        this.entryTime = entryTime;
        this.duration = duration;
        this.exitTime = exitTime;
    }

    public String getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(String entryTime) {
        this.entryTime = entryTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getExitTime() {
        return exitTime;
    }

    public void setExitTime(String exitTime) {
        this.exitTime = exitTime;
    }
}
