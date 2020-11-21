package com.example.smithjarod_parkfinder.objects;

public class StateObject {
    String _stateName;
    String _stateCode;
    double _lat;
    double _lon;

    public StateObject(String stateName, String stateCode, double lat, double lon) {
        this._stateName = stateName;
        this._stateCode = stateCode;
        this._lat = lat;
        this._lon = lon;
    }

    public String stateName(){
        return _stateName;
    }

    public String stateCode(){
        return _stateCode;
    }

    public double lat(){
        return _lat;
    }

    public double lon(){
        return _lon;
    }
}
