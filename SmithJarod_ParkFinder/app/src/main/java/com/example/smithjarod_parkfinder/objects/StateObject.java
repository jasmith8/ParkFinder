package com.example.smithjarod_parkfinder.objects;

public class StateObject {
    // --Commented out by Inspection (11/22/20, 3:01 PM):
    final String _stateName;
    final String _stateCode;
    final double _lat;
    final double _lon;

    public StateObject(String stateName, String stateCode, double lat, double lon) {
        this._stateName = stateName;
        this._stateCode = stateCode;
        this._lat = lat;
        this._lon = lon;
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
