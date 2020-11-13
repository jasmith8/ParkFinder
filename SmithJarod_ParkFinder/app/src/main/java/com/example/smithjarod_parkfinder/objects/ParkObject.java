package com.example.smithjarod_parkfinder.objects;

import java.io.Serializable;

public class ParkObject implements Serializable {

    String parkName;
    String parkStates;
    String parkId;
    String latitude;
    String longitude;

    public ParkObject(String parkName, String parkStates, String parkId, String latitude, String longitude) {
        this.parkName = parkName;
        this.parkStates = parkStates;
        this.parkId = parkId;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName(){
        return parkName;
    }
    public String getState(){ return parkStates; }
    public String getParkId(){ return parkId; }
    public String getLatitude(){ return latitude; }
    public String getLongitude(){return longitude; }


}
