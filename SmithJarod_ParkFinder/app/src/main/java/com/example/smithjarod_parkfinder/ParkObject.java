package com.example.smithjarod_parkfinder;

import java.io.Serializable;

public class ParkObject implements Serializable {

    String parkName;
    String parkStates;
    String parkCode;
    String latitude;
    String longitude;

    public ParkObject(String parkName, String parkStates, String parkCode, String latitude, String longitude) {
        this.parkName = parkName;
        this.parkStates = parkStates;
        this.parkCode = parkCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName(){
        return parkName;
    }
    public String getState(){ return parkStates; }
    public String getCode(){
        return parkCode;
    }
    public String getLatitude(){ return latitude; }
    public String getLongitude(){return longitude; }


}
