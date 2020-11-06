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
    public String getState(){
//        String stateCodeList="";
//        for (String code: parkStates){
//            stateCodeList = stateCodeList+", "+code;
//        }
//        return stateCodeList;
        return parkStates;
    }
    public String getCode(){
        return parkCode;
    }


}
