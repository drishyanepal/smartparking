package com.example.smartparkingone.Models;

import android.widget.ImageView;

public class HeaderModel {
    int userPhoto;
    String username, vehicleNumber;

    public HeaderModel() {
    }

    public HeaderModel(int userPhoto, String username, String vehicleNumber) {
        this.userPhoto = userPhoto;
        this.username = username;
        this.vehicleNumber = vehicleNumber;
    }

    public int getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(int userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }
}
