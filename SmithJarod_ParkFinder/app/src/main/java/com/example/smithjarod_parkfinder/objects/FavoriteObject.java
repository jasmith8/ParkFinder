package com.example.smithjarod_parkfinder.objects;

public class FavoriteObject {

    String _name;
    boolean _isPark;
    String _idCode;

// --Commented out by Inspection START (11/22/20, 3:01 PM):
//    public FavoriteObject(){
//
//    }
// --Commented out by Inspection STOP (11/22/20, 3:01 PM)
    public FavoriteObject(String name, boolean isPark, String idCode) {
        this._name = name;
        this._isPark = isPark;
        this._idCode = idCode;
    }

    public String name(){
        return _name;
    }
    public boolean isPark(){
        return _isPark;
    }
    public String idCode(){
        return _idCode;
    }

}
